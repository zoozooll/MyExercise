package com.beem.project.btf.update;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.utils.PictureUtil;

/**
 * 音频图片文件上传工具类
 * @author zhenggen xie
 */
public class UploadUtil {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	/**
	 * android上传文件到服务器
	 * @param file 需要上传的文件
	 * @param RequestURL 请求的rul
	 * @param String file_type 文件发送类型 表示上传图片的类型（聊天图片，头像图片，时光图片）
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL,
			Map<String, String> params, String file_type) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			Log.i("WWW", "RequestURL=" + RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				/**
				 * 这段代码用于添加用户账号和密码
				 */
				StringBuilder textEntity = new StringBuilder();
				for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
					textEntity.append("--");
					textEntity.append(BOUNDARY);
					textEntity.append("\r\n");
					textEntity.append("Content-Disposition: form-data; name=\""
							+ entry.getKey() + "\"\r\n\r\n");
					textEntity.append(entry.getValue());
					textEntity.append("\r\n");
				}
				dos.write(textEntity.toString().getBytes());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"" + file_type
						+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				dos.close();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if (res == HttpStatus.SC_OK) {
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					Log.i(TAG, "result : " + result);
				} else {
					Log.e(TAG, "request error:" + res);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * android上传图片到服务器[太大的图片会经过压缩发送到服务器中]
	 * @param file 需要上传的文件
	 * @param url 请求的rul
	 * @param String chat_file 文件发送类型 表示上传图片的类型（聊天图片，头像图片，时光图片）
	 * @return 返回响应的内容
	 */
	public static String uploadImage(String[] filePaths, String requestUrl,
			Map<String, String> params, String imageType,
			boolean... isCreateThumb) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			/**
			 * 这段代码用于添加用户账号和密码
			 */
			StringBuilder textEntity = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				textEntity.append(entry.getValue());
				textEntity.append("\r\n");
			}
			dos.write(textEntity.toString().getBytes());
			for (String filePath : filePaths) {
				StringBuffer sb = new StringBuffer();
				File file_name = new File(filePath);
				// revitionImage对图片有压缩处理
				byte[] fileBytes = PictureUtil.getBytes(PictureUtil
						.revitionImage(file_name.getPath()));
				if (fileBytes == null) {
					//LogUtils.e("the fillBytes is null,uploadImage failed~filePath:" + filePath);
					continue;
				}
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\"" + imageType
						+ "\"; filename=\"" + file_name.getName() + "\""
						+ LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				dos.write(fileBytes);
				dos.write(LINE_END.getBytes());
				if (isCreateThumb.length > 0 && isCreateThumb[0]) {
					// 上传缩略图
					StringBuffer sb2 = new StringBuffer();
					sb2.append(PREFIX);
					sb2.append(BOUNDARY);
					sb2.append(LINE_END);
					// 上传缩略图
					sb2.append("Content-Disposition: form-data; name=\""
							+ imageType + "\"; filename=\""
							+ file_name.getName() + "_thumb" + "\"" + LINE_END);
					sb2.append("Content-Type: application/octet-stream; charset="
							+ CHARSET + LINE_END);
					sb2.append(LINE_END);
					dos.write(sb2.toString().getBytes());
					byte[] fileBytes2 = PictureUtil.getBytes(PictureUtil
							.revitionImage(file_name.getPath(), 280, 280));
					dos.write(fileBytes2);
					dos.write(LINE_END.getBytes());
				}
			}
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
					.getBytes();
			dos.write(end_data);
			dos.flush();
			dos.close();
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			if (res == HttpStatus.SC_OK) {
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.i(TAG, "result : " + result);
			} else {
				Log.e(TAG, "request error:" + res);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * android上传文件到服务器
	 * @param file 需要上传的文件
	 * @param RequestURL 请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			Log.i("YYY", "url=" + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				System.out.println(file.getName());
				sb.append("Content-Disposition: form-data; name=\"chat_file\"; filename=\""
						+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				Log.e(TAG, "response code:" + res);
				if (res == HttpStatus.SC_OK) {
					Log.e(TAG, "request success");
					InputStream input = conn.getInputStream();
					StringBuffer sb1 = new StringBuffer();
					int ss;
					while ((ss = input.read()) != -1) {
						sb1.append((char) ss);
					}
					result = sb1.toString();
					Log.e(TAG, "result : " + result);
				} else {
					Log.e(TAG, "request error");
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void updateAccountIcon(String path, String url,
			UpdateTaskCallback callback) {
		new UpLoadFileRunnable(callback).execute(path, url);
	}

	public static interface UpdateTaskCallback {
		public void preExecute();
		public void postExecute(String[] result);
		public void cancelExecute();
		public void onUploading(String[] result);
	}

	private static class UpLoadFileRunnable extends
			AsyncTask<String, Void, String[]> {
		private UpdateTaskCallback callback;
		private String path;
		private String url;

		private UpLoadFileRunnable(UpdateTaskCallback callback) {
			super();
			this.callback = callback;
		}
		@Override
		protected void onPreExecute() {
			if (callback != null) {
				callback.preExecute();
			}
		}
		@Override
		protected void onCancelled() {
			if (callback != null) {
				callback.cancelExecute();
			}
		}
		@Override
		protected void onPostExecute(final String[] uploadUrl) {
			if (callback != null) {
				callback.postExecute(uploadUrl);
			}
		}
		@Override
		protected String[] doInBackground(String... params) {
			if (params.length < 2) {
				cancel(false);
				return null;
			}
			this.path = params[0];
			this.url = params[1];
			HashMap<String, String> maps = new HashMap<String, String>();
			String jidParsed = LoginManager.getInstance().getJidParsed();
			maps.put("tm_id", jidParsed);
			maps.put("session_id", LoginManager.getInstance().getSessionId());
			String result = UploadUtil.uploadImage(new String[] { path }, url,
					maps, "portrait_file", true);
			String[] uploadUrl = null;
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("url");
				uploadUrl = new String[] { jsonArray.getString(0),
						jsonArray.getString(1) };
				if (callback != null) { 
					callback.onUploading(uploadUrl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return uploadUrl;
		}
	}
}
