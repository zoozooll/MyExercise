package com.butterfly.vv.camera.base;

import java.io.Serializable;

/**
 * 文件夹信息
 * @ClassName: ImageFolderInfo
 * @Description: TODO
 * @author: zhenggen xie
 * @date: 2015年3月5日 下午5:04:25
 */
public class ImageFolderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 图片路径
	 */
	private String mFolderPath;
	/**
	 * 图片文件夹名称
	 */
	private String mFolderName;
	/**
	 * 图片文件第一张图片路径
	 */
	private String mFolderFirstImgPath;
	/**
	 * 图片文件第一张文件大小
	 */
	private long mFirstImgSizeInKB;
	/**
	 * 图片张数
	 */
	private int mFolderImgCount;
	private boolean mIsRecursion;
	private boolean mIsHide = false;

	public String getmFolderPath() {
		return mFolderPath;
	}
	public void setmFolderPath(String mFolderPath) {
		this.mFolderPath = mFolderPath;
	}
	public String getmFolderName() {
		return mFolderName;
	}
	public void setmFolderName(String mFolderName) {
		this.mFolderName = mFolderName;
	}
	public String getmFolderFirstImgPath() {
		return mFolderFirstImgPath;
	}
	public void setmFolderFirstImgPath(String mFolderFirstImgPath) {
		this.mFolderFirstImgPath = mFolderFirstImgPath;
	}
	public long getmFirstImgSizeInKB() {
		return mFirstImgSizeInKB;
	}
	public void setmFirstImgSizeInKB(long mFirstImgSizeInKB) {
		this.mFirstImgSizeInKB = mFirstImgSizeInKB;
	}
	public int getmFolderImgCount() {
		return mFolderImgCount;
	}
	public void setmFolderImgCount(int mFolderImgCount) {
		this.mFolderImgCount = mFolderImgCount;
	}
	public boolean ismIsRecursion() {
		return mIsRecursion;
	}
	public void setmIsRecursion(boolean mIsRecursion) {
		this.mIsRecursion = mIsRecursion;
	}
	public boolean ismIsHide() {
		return mIsHide;
	}
	public void setmIsHide(boolean mIsHide) {
		this.mIsHide = mIsHide;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
