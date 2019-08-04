package com.butterfly.vv.camera.base;

import java.io.Serializable;

/**
 * @ClassName: ImageInfoHolder
 * @Description: 图片结构信息
 * @author: yuedong bao
 * @date: 2015-6-1 下午1:51:42
 */
public class ImageInfoHolder implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	public static final int BASE_DATE_LENGTH = 10;// 图片按照时间排序用到 2014::09:09 
	public String mImagePath;
	public String mImageFileName;
	public String mImageDateTime; // 拍摄时间
	public String mImageModifiedTime; // 修改时间
	public String mLatestTime;
	public String mImageWidth;
	public String mImageHeight;
	public String mImageMetrics;// 尺寸信息:如1024X768
	public String mImageDeviceModel;// 设备型号：如m2
	public String mImageSize;
	public int mImageSizeKB;
	public String mExposureTime; // 曝光时间
	public String mPhotoAperture;// 相机光圈
	public String mPhotoFlash;// 闪光灯开关
	public String mFocalLength;// 焦距(mm)
	public String mPhotoIso;// 感光度值
	public String mDeviceMake;// 制造商：如小米
	public String mWhiteBalance;// 白平衡数值
	public String mLongitude;
	public String mLatitude;
	public String mPhotoCity;// 根据mLongitude和mLatitude得出城市
	public boolean mIsChecked;// 标志图片是否勾选

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mImagePath == null) ? 0 : mImagePath.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageInfoHolder other = (ImageInfoHolder) obj;
		if (mImagePath == null) {
			if (other.mImagePath != null)
				return false;
		} else if (!mImagePath.equals(other.mImagePath))
			return false;
		return true;
	}
}
