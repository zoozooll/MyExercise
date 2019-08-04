package com.beem.project.btf.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * @author le yang 获取相机预览及图片最优分辨率
 */
public class CamParaUtil {
	private static final String TAG = "CamParaUtil";
	private CameraSizeComparator sizeComparator = new CameraSizeComparator();
	private static CamParaUtil myCamPara = null;

	private CamParaUtil() {
	}
	public static CamParaUtil getInstance() {
		if (myCamPara == null) {
			myCamPara = new CamParaUtil();
			return myCamPara;
		} else {
			return myCamPara;
		}
	}
	/**
	 * 获取预览分辨率
	 */
	public Size getPropPreviewSize(List<Camera.Size> list, float th,
			int maxHeight) {
		Collections.sort(list, sizeComparator);
		// 打印排序后的值
		for (int i = 0; i < list.size(); i++) {
			Size size = list.get(i);
			Log.i(TAG, "排序后previewSizes:width = " + size.width + " height = "
					+ size.height);
		}
		// //////////////选择最匹配的预览分辨率////////////////
		int minHeight = 720;
		if (maxHeight <= minHeight) {
			maxHeight = minHeight;
			minHeight = 240;
		}
		TreeMap<Float, Integer> ratemap = new TreeMap<Float, Integer>(
				new Comparator<Float>() {
					@Override
					public int compare(Float lhs, Float rhs) {
						// TODO Auto-generated method stub
						// 升序排列
						if (lhs > rhs) {
							return 1;
						} else if (lhs < rhs) {
							return -1;
						} else {
							return 0;
						}
					}
				});
		for (int i = 0; i < list.size(); i++) {
			if ((list.get(i).height <= maxHeight)
					&& (list.get(i).height >= minHeight)) {
				Log.i(TAG, "PreviewSize:w = " + list.get(i).width + "h = "
						+ list.get(i).height);
				ratemap.put(equalRate2(list.get(i), th), i);
			}
		}
		Object[] fo = ratemap.keySet().toArray();
		for (int i = 0; i < fo.length; i++) {
			Log.i(TAG, "PictureSize~~ri~~" + fo[i]);
		}
		float rate = (Float) ratemap.keySet().toArray()[0];
		Log.i(TAG, "PreviewSize~~rate~~" + rate);
		Integer index = ratemap.get(rate);
		Log.i(TAG, "PreviewSize~~index~~" + index);
		return list.get(index);
	}
	/**
	 * 获取图片分辨率
	 */
	public Size getPropPictureSize(List<Camera.Size> list, float th,
			int maxHeight) {
		Collections.sort(list, sizeComparator);
		// 打印排序后的值
		for (int i = 0; i < list.size(); i++) {
			Size size = list.get(i);
			Log.i(TAG, "排序后PictureSize:width = " + size.width + " height = "
					+ size.height);
		}
		// //////////////选择最匹配的图片分辨率////////////////
		int minHeight = 720;
		if (maxHeight <= minHeight) {
			maxHeight = minHeight;
			minHeight = 240;
		}
		TreeMap<Float, Integer> ratemap = new TreeMap<Float, Integer>(
				new Comparator<Float>() {
					@Override
					public int compare(Float lhs, Float rhs) {
						// TODO Auto-generated method stub
						// 升序排列
						if (lhs > rhs) {
							return 1;
						} else if (lhs < rhs) {
							return -1;
						} else {
							return 0;
						}
					}
				});
		for (int i = 0; i < list.size(); i++) {
			if ((list.get(i).height <= maxHeight)
					&& (list.get(i).height >= minHeight)) {
				Log.i(TAG, "PictureSize:w = " + list.get(i).width + "h = "
						+ list.get(i).height);
				ratemap.put(equalRate2(list.get(i), th), i);
			}
		}
		Object[] fo = ratemap.keySet().toArray();
		for (int i = 0; i < fo.length; i++) {
			Log.i(TAG, "PictureSize~~ri~~" + fo[i]);
		}
		float rate = (Float) ratemap.keySet().toArray()[0];
		Log.i(TAG, "PictureSize~~rate~~" + rate);
		Integer index = ratemap.get(rate);
		Log.i(TAG, "PictureSize~~index~~" + index);
		return list.get(index);
	}
	/**
	 * 匹配最佳比例算法
	 */
	private boolean equalRate(Size s, float rate) {
		float r = (float) (s.width) / (float) (s.height);
		if (Math.abs(r - rate) <= 0.03) {
			return true;
		} else {
			return false;
		}
	}
	private float equalRate2(Size s, float rate) {
		float r = (float) (s.width) / (float) (s.height);
		return Math.abs(r - rate);
	}

	private class CameraSizeComparator implements Comparator<Camera.Size> {
		@Override
		public int compare(Size lhs, Size rhs) {
			// TODO Auto-generated method stub
			// 根据宽度进行排序,如果宽度相等则根据高度进行排序,升序排列(由小到大)
			if (lhs.width != rhs.width) {
				return lhs.width - rhs.width;
			} else if (lhs.height != rhs.height) {
				return lhs.height - rhs.height;
			} else {
				return 0;
			}
		}
	}

	/**
	 * 打印支持的previewSizes
	 * @param params
	 */
	public void printSupportPreviewSize(Camera.Parameters params) {
		List<Size> previewSizes = params.getSupportedPreviewSizes();
		for (int i = 0; i < previewSizes.size(); i++) {
			Size size = previewSizes.get(i);
			Log.i(TAG, "previewSizes:width = " + size.width + " height = "
					+ size.height);
		}
	}
	/**
	 * 打印支持的pictureSizes
	 * @param params
	 */
	public void printSupportPictureSize(Camera.Parameters params) {
		List<Size> pictureSizes = params.getSupportedPictureSizes();
		for (int i = 0; i < pictureSizes.size(); i++) {
			Size size = pictureSizes.get(i);
			Log.i(TAG, "pictureSizes:width = " + size.width + " height = "
					+ size.height);
		}
	}
	/**
	 * 打印支持的聚焦模式
	 * @param params
	 */
	public void printSupportFocusMode(Camera.Parameters params) {
		List<String> focusModes = params.getSupportedFocusModes();
		for (String mode : focusModes) {
			Log.i(TAG, "focusModes--" + mode);
		}
	}
}
