package com.butterfly.vv.camera.photo;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ListView
 **/
public class PhotoCellView {
	public RelativeLayout mTopImgLayout;
	public LinearLayout mBottomImgLayout;
	public RelativeLayout mLeftImgLayout;
	public RelativeLayout mRightImgLayout;
	public RelativeLayout mTopCheckLayout;
	public RelativeLayout mLeftCheckLayout;
	public RelativeLayout mRightCheckLayout;
	public LinearLayout mTopHideLayout;
	public LinearLayout mLeftHideLayout;
	public LinearLayout mRightHideLayout;
	public ImageView mTopImage;
	public String mTopImgPath;
	public ImageView mLeftImage;
	public String mLeftImgPath;
	public ImageView mRightImage;
	public String mRightImgPath;
	public TextView mTopDirName;
	public TextView mTopDirPhotoNumber;
	public TextView mLeftDirName;
	public TextView mLeftDirPhotoNumber;
	public TextView mRightDirName;
	public TextView mRightDirPhotoNumber;
}
