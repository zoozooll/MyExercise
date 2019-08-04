package com.butterfly.vv.service;

import java.sql.SQLException;

import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

/**
 * @author yuedong bao
 */
public class BBSService {
	private static BBSService instance = null;
	private static final String TAG = "BBSService";

	// 保存当前板块基本信息：板块id，用户id，学校id或者城市id
	private BBSService() {
	}
	public static BBSService getInstance() {
		if (instance == null) {
			synchronized (BBSService.class) {
				if (instance == null) {
					instance = new BBSService();
				}
			}
		}
		return instance;
	}
	/**
	 * @param objectDao
	 * @param createT
	 * @param id
	 * @param updateColumn
	 * @param updateValue
	 * @func 不存在则添加，存在则更新指定字段
	 */
	public static <T> void updateVVDao(Dao<T, String> objectDao, T createT,
			String id, String[] updateColumn, Object[] updateValue) {
		try {
			int updateCol = 0;
			UpdateBuilder<T, String> bu = objectDao.updateBuilder();
			for (int i = 1; i < updateColumn.length; i++) {
				bu.updateColumnValue(updateColumn[i], updateValue[i]);
			}
			bu.where().idEq(id);
			updateCol = bu.update();
			//LogUtils.i("=====updateCol=========" + updateCol);
			if (updateCol < 1) {
				objectDao.create(createT);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}
	/**
	 * @param jsonStr
	 * @return 剥离得到json中data中数据
	 * @func
	 */
	public static JSONArray parseJsonData(String jsonStr) {
		if (jsonStr == null || jsonStr.isEmpty()) {
			//LogUtils.v("Error: result is null~~" + jsonStr);
			return null;
		}
		JSONArray jsonData = null;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonStr);
			int status = (Integer) jsonObject.get("status");
			String info = (String) jsonObject.get("info");
			if (status != 1 || !info.equals("success")) {
				Log.e(TAG, "Error:parseBBSJson-->status:" + status + "-->info:"
						+ info);
				return null;
			}
			if (jsonObject.get("data") != null) {
				if (jsonObject.get("data") instanceof JSONObject) {
					JSONObject jsonData1 = (JSONObject) jsonObject.get("data");
					if (jsonData1.get("data") != null) {
						if (jsonData1.get("data") instanceof JSONArray) {
							jsonData = (JSONArray) jsonData1.get("data");
							return jsonData;
						}
					}
				} else if (jsonObject.get("data") instanceof JSONArray) {
					return (JSONArray) jsonObject.get("data");
				}
			}
			return null;
		} catch (JSONException e) {
			//LogUtils.i("======" + e);
			e.printStackTrace();
		}
		return null;
	}
	public static void GetBBSPostListRequest() {
	}
	// 发送超时
	public static void sendTimeOutHandler(Handler handler) {
		Message msg = Message.obtain();
		msg.what = Constants.TIMEOUT;
		handler.sendMessageDelayed(msg, Constants.TIMEOUT_TIME);
	}
	// 发送超时
	public static void sendTimeOutHandler(Handler handler, int timeout) {
		Message msg = Message.obtain();
		msg.what = Constants.TIMEOUT;
		handler.sendMessageDelayed(msg, timeout);
	}
	public static void downloadImage(ImageView tImageView, String[] params,
			boolean... isThumb) {
		String uri = AppProperty.getInstance().VVAPI + "&uid="
				+ StringUtils.parseName(params[0]) + "&file=" + params[1];
		BBSUtils.getImageView(uri, tImageView, isThumb);
	}
}
