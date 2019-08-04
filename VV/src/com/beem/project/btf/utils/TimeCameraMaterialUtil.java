package com.beem.project.btf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.beem.project.btf.ui.fragment.TimeCameraImageFragement;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

public class TimeCameraMaterialUtil {
	/*	private static String materials_30="image/TimeCamera/photo_materials_30.json";
		private static String materials_60="image/TimeCamera/photo_materials_60.json";
		private static String materials_80="image/TimeCamera/photo_materials_80.json";*/
	private static final String TAG = "TimeCameraMaterialUtil";
	private static String matrialsFormat = "image/TimeCamera/photo_materials_%s.json";

	/**
	 * 初始化素材数据库
	 */
	public static void initMaterialDB() {
		for (TimeCameraImageFragement.DecadeType item : TimeCameraImageFragement.DecadeType
				.values()) {
			//			Log.d("aaron", "initMaterialDB " + item.name());
			String materialsPath = String.format(matrialsFormat,
					item.getGroupId());
			SaveToDB(getJsonData(materialsPath), true);
		}
		/*	SaveToDB(getJsonData(materials_60), true);
			SaveToDB(getJsonData(materials_80), true);
			SaveToDB(getJsonData(materials_30), true);*/
	}
	/**
	 * 解析json并保存到数据库
	 */
	public static void SaveToDB(String str, boolean islocal) {
		try {
			JSONObject responseJObject = new JSONObject(str);
			JSONArray data = responseJObject.getJSONArray("data");
			if (data != null && data.length() > 0) {
				for (int i = 0; i < data.length(); i++) {
					JSONObject item = data.getJSONObject(i);
					// 表中没有的素材才去记录
					TimeCameraImageInfo ImageInfo = new TimeCameraImageInfo();
					// 字段赋值并保存到数据库
					ImageInfo.setField(DBKey.id, item.optString("id"));
					ImageInfo
							.setField(DBKey.groupid, item.optString("groupid"));
					ImageInfo.setField(DBKey.isLocal, islocal);
					//是否已经下载是属于本地的操作，跟服务器以及json数据无关，不应在此赋值；
					if (islocal) {
						ImageInfo.setField(DBKey.isDownloaded, islocal);
					}
					ImageInfo.setField(DBKey.pathThumb,
							item.optString("thumbpath"));
					ImageInfo.setField(DBKey.pathThumbLarge,
							item.optString("thumbpathlarge"));
					ImageInfo.setField(DBKey.notetext, item.optString("text"));
					ImageInfo.setField(DBKey.laypath1,
							item.optString("laypath1"));
					ImageInfo.setField(DBKey.laypath2,
							item.optString("laypath2"));
					ImageInfo.setField(DBKey.laypath3,
							item.optString("laypath3"));
					ImageInfo.setField(DBKey.laycount,
							item.optString("laycount"));
					ImageInfo.setField(DBKey.textposition,
							item.optString("textposition"));
					ImageInfo.setField(DBKey.textsize,
							item.optString("textsize"));
					ImageInfo.setField(DBKey.posepath,
							item.optString("posepath"));
					ImageInfo.setField(DBKey.textcolor,
							item.optString("textcolor"));
					ImageInfo.setField(DBKey.bitmapposition,
							item.optString("bitmapposition"));
					JSONArray mapping = item.getJSONArray("mapping");
					String bf = getStringforJSON(mapping);
					ImageInfo.setField(DBKey.mapping, bf);
					ImageInfo.saveToDatabase();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * json数组转化为字符串
	 * @throws JSONException
	 */
	private static String getStringforJSON(JSONArray jarray)
			throws JSONException {
		StringBuffer bf = new StringBuffer();
		if (jarray != null && jarray.length() > 0) {
			for (int i = 0; i < jarray.length(); i++) {
				String item = jarray.getString(i);
				if (i == jarray.length() - 1) {
					bf.append(item);
				} else {
					bf.append(item).append(Constants.TIMECAMERA_MAPPINGSPLIT);
				}
			}
		}
		// Log.i(TAG, "json数组数据~"+bf.toString());
		return bf.toString();
	}
	/**
	 * 从assert文件夹中读取json文件，模拟网络请求
	 */
	private static String getJsonData(String filename) {
		Context context = BeemApplication.getContext();
		String str = null;
		try {
			StringBuilder sb = new StringBuilder();
			InputStream is = context.getAssets().open(filename);
			int len;
			// 用reader方式读取文本的文件，而不直接用inputstream方式。防止utf-8编码出错问题。
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			char[] buf = new char[256];
			while ((len = br.read(buf)) > -1) {
				sb.append(buf, 0, len);
			}
			is.close();
			br.close();
			str = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * 字符串转化为int 数组
	 */
	public static int[] getIntArrayForString(
			TimeCameraImageInfo currentImageInfo) {
		String[] strs = BBSUtils.splitString(Constants.TIMECAMERA_MAPPINGSPLIT,
				currentImageInfo.getMapping());
		int[] points = new int[strs.length];
		for (int i = 0; i < points.length; i++) {
			points[i] = Integer.parseInt(strs[i]);
		}
		// Log.i(TAG, "int数组~~"+Arrays.toString(points));
		return points;
	}
	/**
	 * @Title: requestNetwork
	 * @Description: 请求网络
	 * @param: @param groupid
	 * @param: @return
	 * @return: String
	 * @throws WSError 
	 * @throws:
	 */
	public static String requestNetwork(String groupid) throws WSError {
		String response = null;
		String url = AppProperty.getInstance().VVAPI + "/get_photo_materials";
		String[] names = new String[] { "tm_id", "group_id", "session_id" };
		String jid = "0", sessionId = "0";
		try {
			jid = LoginManager.getInstance().getJidParsed();
			sessionId = LoginManager.getInstance().getSessionId();
		} catch (IllegalStateException e) {
		}
		Object[] values = new Object[] { jid, groupid, sessionId };
		response = Caller.doGet(url, names, values);
		return response;
	}
}
