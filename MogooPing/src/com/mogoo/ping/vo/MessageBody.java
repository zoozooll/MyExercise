package com.mogoo.ping.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mogoo.ping.app.ToolUtils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MessageBody implements Serializable{
	private static final String tag = "lcq:PushMsgBody";

	private String mShowType;
	private String mAppName;
	private String mAppImage;
	private String mAppDescribe;
	private int mAppType;
	private String mVerName;
	private List<String> mPreviewLists;
	private String mSize;
	private String mIsDownUrl;
	private String mDownUrl;
	private String packageName;
	private String appId;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public MessageBody() {
		super();
	}



	private int indexOf(String s, String c) {
		return s.indexOf(c);
	}

	public String getmShowType() {
		return mShowType;
	}

	public void setmShowType(String mShowType) {
		this.mShowType = mShowType;
	}

	public String getmAppImage() {
		return mAppImage;
	}

	public void setmAppImage(String mAppImage) {
		this.mAppImage = mAppImage;
	}

	public String getmAppDescribe() {
		return mAppDescribe;
	}

	public void setmAppDescribe(String mAppDescribe) {
		this.mAppDescribe = mAppDescribe;
	}

	public int getmAppType() {
		return mAppType;
	}

	public void setmAppType(int mAppType) {
		this.mAppType = mAppType;
	}

	public String getmVerName() {
		return mVerName;
	}

	public void setmVerName(String mVerName) {
		this.mVerName = mVerName;
	}

	public List<String> getmPreviewLists() {
		return mPreviewLists;
	}

	public void setmPreviewLists(List<String> mPreviewLists) {
		this.mPreviewLists = mPreviewLists;
	}

	public String getmSize() {
		return mSize;
	}

	public void setmSize(String mSize) {
		this.mSize = mSize;
	}

	public String getmIsDownUrl() {
		return mIsDownUrl;
	}

	public void setmIsDownUrl(String mIsDownUrl) {
		this.mIsDownUrl = mIsDownUrl;
	}

	public String getmAppName() {
		return mAppName;
	}

	public void setmAppName(String mAppName) {
		this.mAppName = mAppName;
	}

	public String getmDownUrl() {
		return mDownUrl;
	}

	public void setmDownUrl(String mDownUrl) {
		this.mDownUrl = mDownUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}
