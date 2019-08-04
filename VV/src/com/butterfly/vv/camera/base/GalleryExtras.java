/**
 * 
 */
package com.butterfly.vv.camera.base;

import android.content.Intent;

/**
 * @author Aaron Lee Created at 下午4:24:10 2016-1-18
 */
public class GalleryExtras {
	public static final String KEY_PICKCOUNT = "KEY_PICKCOUNT";
	public static final String KEY_OUTPUT = "KEY_OUTPUT";
	public static final String KEY_INTPUTFOLDER = "KEY_INTPUTFOLDER";
	public static final String KEY_ISCROP = "KEY_ISCROP";
	public static final String KEY_ISFROMCAMERA = "KEY_ISFROMCAMERA";
	/** The max number of picked pictures */
	private int pickCount;
	/** Input folders */
	private String inputFolder;
	/**
	 * the path of output data. The result data will be saved on the path. Only data from Crop and
	 * Camera can use it. The pictures from store will still return their file paths
	 */
	private String outPut;
	/** If the chose picture need to crop. Only support it when pickCount is 1 */
	private boolean isCrop;
	/** If the choose picture can load from gallery */
	private boolean isFromCamera;

	public GalleryExtras(Intent i) {
		this.pickCount = i.getIntExtra(KEY_PICKCOUNT, 0);
		this.isCrop = i.getBooleanExtra(KEY_ISCROP, false);
		this.isFromCamera = i.getBooleanExtra(KEY_ISFROMCAMERA, false);
		this.outPut = i.getStringExtra(KEY_INTPUTFOLDER);
		this.inputFolder = i.getStringExtra(KEY_INTPUTFOLDER);
	}
	public int getPickCount() {
		return pickCount;
	}
	public String getInputFolder() {
		return inputFolder;
	}
	public String getOutPut() {
		return outPut;
	}
	public boolean isCrop() {
		return isCrop;
	}
	public boolean isFromCamera() {
		return isFromCamera;
	}
}
