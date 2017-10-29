package com.dvr.android.dvr.bean;

import android.net.Uri;

public class VideoInfo {
	
	private Uri mUri;
	
	private boolean mCanDelete;
	
	private String mVideoName;
	
	public VideoInfo(Uri uri, boolean canDelete, String videoName) {
		mUri = uri;
		mCanDelete = canDelete;
		mVideoName = videoName;
	}
	
	public Uri getUri() {
		return mUri;
	}
	
	public boolean canDelete() {
		return mCanDelete;
	}
	
	public String getVideoName() {
		return mVideoName;
	}
}