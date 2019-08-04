package com.beem.project.btf.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.util.Log;

import com.beem.project.btf.constant.Constants;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.butterfly.vv.vv.utils.JsonParseUtils.FindParam;

/**
 * 上传工具类
 * @author spring sky<br>
 *         Email :vipa1888@163.com<br>
 *         QQ: 840950105<br>
 *         支持上传文件和参数
 */
public class UploadImageUtil {
	private static final String TAG = "UploadImageUtil";
	private volatile int loadimageNumber = 0;
	private int loadedNumber = 0;
	private int errorNumber = 0;
	private String listindex = null;
	private int[] index = null;

	/**
	 * 单例模式获取上传工具类
	 * @return
	 */
	private static class UploadImageUtilHolder {
		public static UploadImageUtil instance = new UploadImageUtil();
	}

	public static UploadImageUtil getInstance() {
		return UploadImageUtilHolder.instance;
	}
	/**
	 * android上传文件到服务器
	 * @param listpath 需要上传的图片列表
	 * @param param 上传图片的参数
	 */
	public synchronized void uploadFile(final ArrayList<String> listpath,
			final Map<String, String> param) {
		if (listpath == null || listpath.size() == 0) {
			sendMessage(Constants.UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			return;
		}
		LogUtils.i("--jj uploadFile loadedNumber=" + loadedNumber
				+ "--loadimageNumber=" + loadimageNumber);
		loadimageNumber = listpath.size();
		index = new int[loadimageNumber];
		LogUtils.i("upload list file Size=" + loadimageNumber);
		// onUploadProcessListener.initUpload(i);
		int i = 0;
		do {
			String filePath = listpath.get(i);
			// onUploadProcessListener.onUploadProcess(i); //当前上传的是第i+1张图片
			if (filePath != null && filePath.length() > 0) {
				uploadFile(filePath, param, i);
				i++;
			} else {
				// sendMessage(Constants.UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
				onUploadProcessListener.initUploadError();
				return;
			}
		} while (i < loadimageNumber);
	}
	/**
	 * android上传文件到服务器
	 * @param filePath 需要上传的图片路径
	 * @param param 上传图片的参数
	 * @param position 上传图片的位置
	 */
	public void uploadFile(final String filePath,
			final Map<String, String> param, final int position) {
		Log.i(TAG, "uploading filePath=" + filePath);
		final String authority = param.get("authority");
		final String lon = param.get("lon");
		final String lat = param.get("lat");
		final String gid = param.get("gid");
		final String createTime = param.get("create_time");
		new Thread(new Runnable() { // 开启线程上传文件
					@Override
					public void run() {
						toUploadImage(authority, lon, lat, filePath, gid,
								createTime, position);
					}
				}, "upload Images").start();
	}
	private void toUploadImage(String authority, String lon, String lat,
			String pathToImage, String gid, String gid_create_time, int position) {
		Map<String, Object> mapslist = TimeflyService.uploadPicture(authority,
				lon, lat, pathToImage, gid, gid_create_time);
		if (JsonParseUtils.getResult(mapslist)) {
			Log.i(TAG, "toUploadImage position=" + position);
			String new_gid = JsonParseUtils.getParseValue(mapslist,
					String.class, new FindParam("data", 0),
					new FindParam("gid"));
			String pid = JsonParseUtils.getParseValue(mapslist, String.class,
					new FindParam("data", 0), new FindParam("pid"));
			String path = JsonParseUtils.getParseValue(mapslist, String.class,
					new FindParam("data", 0), new FindParam("photo_big"));
			String pathThumb = JsonParseUtils.getParseValue(mapslist,
					String.class, new FindParam("data", 0), new FindParam(
							"photo_small"));
			String albumn = JsonParseUtils.getParseValue(mapslist,
					String.class, new FindParam("data", 0), new FindParam(
							"album_url"));
			mapslist.clear();
			String responseMessage = StringUtils.join(new Object[] { position,
					",", new_gid, ",", pid, ",", path, ",", pathThumb, ",",
					gid_create_time, ",", albumn });
			loadedNumber++;
			Log.i(TAG, "toUploadImage responseMessage " + responseMessage);
			if (loadedNumber < loadimageNumber) {
				onUploadProcessListener.onUploadProcess(
						Constants.UPLOAD_SUCCESS_CODE, responseMessage);
			} else {
				sendMessage(Constants.UPLOAD_SUCCESS_CODE, responseMessage);
				loadedNumber = 0;
				loadimageNumber = 0;
			}
			return;
		} else {
			Log.e(TAG, "request error");
			index[errorNumber] = position + 1;
			errorNumber++;
			if (loadedNumber + errorNumber >= loadimageNumber) {
				Log.i(TAG, "request error down");
				if (loadedNumber != 0) {
					int temp[] = Arrays.copyOfRange(index, 0, errorNumber);
					Log.i(TAG, "-- temp=" + Arrays.toString(temp));
					index = temp;
				}
				Arrays.sort(index);
				int size = index.length;
				for (int i = size - 1; i >= 0; i--) {
					if (listindex == null) {
						listindex = index[i] + "";
					} else {
						listindex = listindex + "," + index[i];
					}
				}
				Log.i(TAG, "--jj listindex=" + listindex);
				sendMessage(Constants.UPLOAD_SERVER_ERROR_CODE, listindex);
				listindex = null;
				errorNumber = 0;
				loadedNumber = 0;
				loadimageNumber = 0;
				index = null;
			}
			return;
		}
	}
	/**
	 * 发送上传结果
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode, String responseMessage) {
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 * @author shimingzheng
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);
		/**
		 * 上传中
		 * @param uploadSize
		 */
		void onUploadProcess(int responseCode, String message);
		/**
		 * 准备上传
		 * @param fileSize
		 */
		void initUploadError();
	}

	private OnUploadProcessListener onUploadProcessListener;

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public static interface uploadProcessListener {
	}
}
