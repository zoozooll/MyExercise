package com.oregonscientific.meep.youtube;

import android.graphics.Bitmap;

public class OsListViewItem {
	private Bitmap mImage = null;
	private String mName = null;
	private String count = null;
	private String mPath = null;
	private int mX = 0;
	private int mY = 0;
	private boolean mIsChecked = false;
	
	public OsListViewItem()
	{
		
	}

	public Bitmap getImage() {
		return mImage;
	}

	public void setImage(Bitmap image) {
		this.mImage = image;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String path) {
		this.mPath = path;
	}

	public boolean isChecked() {
		return mIsChecked;
	}

	public void setIsChecked(boolean isChecked) {
		this.mIsChecked = isChecked;
	}
	
	public int getX() {
		return mX;
	}

	public void setX(int x) {
		mX = x;
	}

	public int getY() {
		return mY;
	}

	public void setY(int y) {
		mY = y;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
			
}
