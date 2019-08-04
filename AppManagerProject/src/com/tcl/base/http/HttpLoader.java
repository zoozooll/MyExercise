package com.tcl.base.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context; 
import android.text.TextUtils;

import com.tcl.framework.log.NLog;
import com.tcl.framework.network.http.NetworkError; 
import com.tcl.manager.application.ManagerApplication; 
import com.tcl.manager.util.AndroidUtil; 
import com.tcl.manager.util.LogUtil;
//import com.tcl.manager.util.ParamsUtils;

public class HttpLoader {

	private static final String TAG = "HttpLoader";

	public interface LoadCallback {
		void onLoaded(int err, HttpEntity entity);
	}

	private static final int STATE_NONE = 0;
	private static final int STATE_BEGIN = 1;
	private static final int STATE_LOADING = 2;
	private static final int STATE_END = 3;

	String mURL;
	int mState;
	HttpTask mCurrentTask;
	LoadCallback mCallback;
	Context mContext;

	public HttpLoader(String url) {
		this.mURL = url;
		mState = STATE_NONE;
		mContext = ManagerApplication.sApplication.getApplicationContext();
	}

	public void setCallback(LoadCallback callback) {
		this.mCallback = callback;
	}

	protected String getURL() {
		return mURL;
	}

	/**
	 * 增加一个同步请求
	 * 
	 * @param paramsMap
	 * @param entityMap
	 * @param fileMap
	 * @param supportPost
	 * @return
	 */
	public boolean syncload(Map<String, String> paramsMap,
			Map<String, byte[]> entityMap, Map<String, File> fileMap,
			boolean supportPost) {
		if (mState != STATE_NONE && mState != STATE_END)
			throw new IllegalStateException("state illegal");

		String url = getURL();
		if (TextUtils.isEmpty(url)) {
			throw new NullPointerException("url is empty!");
		}

		if (!AndroidUtil.isNetConnect(mContext)) {
			LogUtil.v("syncload:NetworkHelper");
			NLog.w(TAG, "network unavailable!");
			return false;
		}

		mState = STATE_BEGIN;
		HttpRequestBase request = createRequest(paramsMap, entityMap, fileMap,
				supportPost);
		HttpTask task = new HttpTask(mContext, request);
		mCurrentTask = task;

		task.setCallback(mTaskCallback);
		task.setName("http");
		task.run();

		return true;
	}

	public synchronized boolean load(Map<String, String> paramsMap,
			Map<String, byte[]> entityMap, Map<String, File> fileMap,
			boolean supportPost) {
		if (mState != STATE_NONE && mState != STATE_END)
			throw new IllegalStateException("state illegal");

		String url = getURL();
		if (TextUtils.isEmpty(url)) {
			throw new NullPointerException("url is empty!");
		}

		if (!AndroidUtil.isNetConnect(mContext)) {
			LogUtil.v("syncload:NetworkHelper");
			NLog.w(TAG, "network unavailable!");
			return false;
		}

		mState = STATE_BEGIN;
		HttpRequestBase request = createRequest(paramsMap, entityMap, fileMap,
				supportPost);
		HttpTask task = new HttpTask(mContext, request);
		mCurrentTask = task;

		task.setCallback(mTaskCallback);
		task.setName("http");
		task.start();

		return true;
	}

	public synchronized boolean load(Map<String, String> paramsMap,
			Map<String, byte[]> entityMap, boolean supportPost) {
		return load(paramsMap, entityMap, null, supportPost);
	}

	public synchronized boolean load(Map<String, String> paramsMap) {
		return load(paramsMap, null, false);
	}

	public synchronized void cancel() {
		if (mState == STATE_NONE || mState == STATE_END)
			return;

		if (mCurrentTask != null) {
			mCurrentTask.cancel();
		}
	}

	private void onSuccess(HttpEntity entity) {

		int ret = NetworkError.SUCCESS;

		final LoadCallback callback = mCallback;
		if (callback == null) {
			return;
		}
		callback.onLoaded(ret, entity);
		/*
		 * JSONObject obj = null; try { byte[] bytes =
		 * EntityUtils.toByteArray(entity); // byte[] md5 = MD5.encode16(bytes);
		 * // String hash = StringUtils.bytesToHexes(md5); String jsonStr = new
		 * String(bytes, "utf-8"); // NLog.i(TAG,
		 * "hash:%s, json result: %s",hash, jsonStr); NLog.i(TAG,
		 * "json result: %s", jsonStr); obj = new SmartJSONObject(jsonStr); //
		 * obj.put(JsonConstants.HASH, hash); } catch (ParseException e) {
		 * NLog.printStackTrace(e); ret = NetworkError.FAIL_IO_ERROR; } catch
		 * (IOException e) { NLog.printStackTrace(e); ret =
		 * NetworkError.FAIL_IO_ERROR; } catch (JSONException e) {
		 * NLog.printStackTrace(e); ret = NetworkError.FAIL_IO_ERROR; } finally
		 * { callback.onLoaded(ret, obj); }
		 */
	}

	private void onFail(int err) {
		final LoadCallback callback = mCallback;
		if (callback != null) {
			callback.onLoaded(err, null);
		}
	}

	private HttpTask.HttpCallback mTaskCallback = new HttpTask.HttpCallback() {

		@Override
		public void onPrepared() {
			mState = STATE_LOADING;
		}

		@Override
		public void onCompleted(int ret, HttpEntity entity) {
			mState = STATE_END;
			mCurrentTask = null;

			if (ret == NetworkError.SUCCESS && entity != null) {
				onSuccess(entity);
			}

			else if (ret != NetworkError.SUCCESS) {
				onFail(ret);
			}
		}
	};

	@SuppressWarnings("deprecation")
	protected HttpRequestBase createRequest(Map<String, String> paramsMap,
			Map<String, byte[]> entityMap, Map<String, File> fileMap,
			boolean supportPost) {
		if (paramsMap == null) {
			paramsMap = new HashMap<String, String>();
		}
		if (entityMap == null) {
			entityMap = new HashMap<String, byte[]>();
		}

//		paramsMap.putAll(ParamsUtils.fin(mContext));
//		Context mContext = ManagerApplication.sApp.getApplicationContext();
//		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//		paramsMap.put("imsi", telephonyManager.getSubscriberId());// "724556789123456789",
//		// "46000342424234324"
//		paramsMap.put("imei", telephonyManager.getDeviceId());
//		paramsMap.put("model", Build.MODEL);// Constant.MODEL"RAV4_EMEA");
//		paramsMap.put("language", mContext.getResources().getConfiguration().locale.toString());
//		paramsMap.put("region", AndroidUtil.getMetaData(mContext, "REGION"));
//		paramsMap.put("channel", AndroidUtil.getMetaData(mContext, "CHANNEL"));
//		paramsMap.put("req_from", AndroidUtil.getMetaData(mContext, "TAG"));
//		paramsMap.put("version_name", AndroidUtil.getVersionName(mContext));
//		paramsMap.put("version_code", String.valueOf(AndroidUtil.getVersionCode(mContext)));
//		// ----start add
//		paramsMap.put("screen_size",AndroidUtil.getDisplayMetricsHeight(mContext) 
//				+ "#"
//				+ AndroidUtil.getDisplayMetricsWidth(mContext));// 手机屏幕分辨率
//		paramsMap.put("network", AndroidUtil.getnetworkInfoName(mContext));// 当前使用网络
//		paramsMap.put("os_version_code",
//				String.valueOf(AndroidUtil.getVersionSDKINT()));// android系统版本
//		paramsMap.put("os_version", AndroidUtil.getVersionRelease());// android系统版本
//		paramsMap.put("user_id", "");

		// Context context = ACContext.getInstance().getApplicationContext();
		// TelephonyManager telephonyManager = (TelephonyManager) context
		// .getSystemService(Context.TELEPHONY_SERVICE);
		//
		// paramsMap.put("imsi", telephonyManager.getSubscriberId());
		// paramsMap.put("imei", telephonyManager.getDeviceId());
		// paramsMap.put("model", Build.MODEL);
		//
		// String lang =
		// context.getResources().getConfiguration().locale.toString();
		// paramsMap.put("language",lang);
		// paramsMap.put("region", ContextUtils.getMetaData(context, "REGION"));
		// paramsMap.put("channel", ContextUtils.getMetaData(context,
		// "CHANNEL"));
		//
		// PackageManager manager = context.getPackageManager();
		// try {
		// PackageInfo info = manager.getPackageInfo(context.getPackageName(),
		// 0);
		// paramsMap.put("version", info.versionName);
		//
		// } catch (NameNotFoundException e) {
		// }

		// ConnectivityManager connectionManager = (ConnectivityManager) context
		// .getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		//
		// if (networkInfo != null)
		// paramsMap.put("network", networkInfo.getTypeName());// 当前使用网络
		// else
		// paramsMap.put("network", "none");
		//
		// paramsMap.put("os_version", Build.VERSION.RELEASE);// android系统版本

		HttpRequestBase request = null;
		LogUtil.v("createRequest supportPost:" + supportPost);
		if (!supportPost) {
			if (entityMap != null && entityMap.size() > 0) {
				NLog.w(TAG, "http get not support postint entities, ignore it");
			}
			int index = mURL.lastIndexOf("?");

			StringBuffer urlParam = new StringBuffer(mURL);
			Set<Map.Entry<String, String>> entries = paramsMap.entrySet();
			boolean first = true;
			for (Map.Entry<String, String> entry : entries) {
				if (!first)
					urlParam.append("&");
				else {

					if (index < 0)
						urlParam.append("?");
					else if (index != mURL.length() - 1)
						urlParam.append("&");

					first = false;
				}
				urlParam.append(entry.getKey()).append("=");
				if (!TextUtils.isEmpty(entry.getValue())) {
					String text = URLEncoder.encode(entry.getValue());
					urlParam.append(text);
				}
			}

			String urlstr = urlParam.toString();
			if (urlstr.contains("+")) {
				urlstr = urlstr.replaceAll("\\+", "%20");
			}
			NLog.d(TAG, "get url: %s", urlstr);
			HttpGet get = new HttpGet(urlstr);
			request = get;

		} else {

			// HttpPost req = new HttpPost(mURL);
			// try {
			// req.setEntity(new UrlEncodedFormEntity(getHttpData(paramsMap),
			// HTTP.UTF_8));
			// } catch (UnsupportedEncodingException e) {
			// LogUtil.v("HttpPost: UnsupportedEncodingException");
			// e.printStackTrace();
			// }
			//
			// request = req;

			HttpPost req = new HttpPost(mURL);
			try {
				if (NLog.isDebug()) {
					StringBuffer urlParam = new StringBuffer();
					Set<Map.Entry<String, String>> entries = paramsMap
							.entrySet();
					boolean first = true;
					for (Map.Entry<String, String> entry : entries) {
						if (!first)
							urlParam.append("&");
						else
							first = false;
						urlParam.append(entry.getKey()).append("=");
						if (!TextUtils.isEmpty(entry.getValue()))
							urlParam.append(entry.getValue());
					}

					NLog.d(TAG, "post url: %s", mURL);
					NLog.d(TAG, "post params: %s", urlParam);
				}

				if ((entityMap == null || entityMap.size() == 0)
						&& (fileMap == null || fileMap.size() == 0)) {

					final HttpEntity paramEntity = new UrlEncodedFormEntity(
							toParamEntity(paramsMap), HTTP.UTF_8);
					req.setEntity(paramEntity);
				}

				else {
					MultipartEntity multipartEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					// 处理字符参数
					Iterator<Entry<String, String>> paramIterator = paramsMap
							.entrySet().iterator();
					while (paramIterator.hasNext()) {
						Entry<String, String> entry = paramIterator.next();
						String text = entry.getValue();
						if (text == null)
							text = "";
						multipartEntity.addPart(entry.getKey(), new StringBody(
								text));
					}

					// 处理二进制数据
					if (entityMap != null && entityMap.size() > 0) {
						Iterator<Entry<String, byte[]>> iterator = entityMap
								.entrySet().iterator();
						while (iterator.hasNext()) {
							Entry<String, byte[]> entry = iterator.next();
							String key = entry.getKey();
							byte[] b = entry.getValue();
							multipartEntity.addPart(key, new ByteArrayBody(b,
									key));
						}
					}

					// 处理文件数据
					if (fileMap != null && fileMap.size() > 0) {
						Iterator<Entry<String, File>> fileIterator = fileMap
								.entrySet().iterator();
						while (fileIterator.hasNext()) {
							Entry<String, File> entry = fileIterator.next();
							String key = entry.getKey();
							File b = entry.getValue();
							multipartEntity.addPart(key, new FileBody(b));
						}
					}

					req.setEntity(multipartEntity);
				}

			} catch (UnsupportedEncodingException e) {
			}

			request = req;

		}

		return request;
	}

	/**
	 * post方法map转化为httpClient参数的通用转化方法
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<NameValuePair> getHttpData(Map map) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			NameValuePair nameValue = new BasicNameValuePair(key, val);
			list.add(nameValue);
		}

		return list;
	}

	private List<NameValuePair> toParamEntity(Map<String, String> paramsMap) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (paramsMap.size() == 0)
			return list;

		Iterator<Map.Entry<String, String>> iter = paramsMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter
					.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			NameValuePair nameValue = new BasicNameValuePair(key, val);
			list.add(nameValue);
		}

		return list;
	}
}
