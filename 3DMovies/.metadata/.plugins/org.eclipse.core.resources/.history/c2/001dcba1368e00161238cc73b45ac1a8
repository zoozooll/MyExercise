package com.oregonscientific.meep.opengl;

import android.graphics.Bitmap;

public class OSIcon {
	
	public OSIcon(Bitmap source,int x,int y,int width,int height,int xDiff,int yDiff,int timeInterval, boolean isAnimated, int numOfAnimatedIcon)
	{
		mSource = source;
		mX = x;
		mY = y;
		mWidth = width;
		mHeight = height;
		mXDiff = xDiff;
		mYDiff = yDiff;
		mTimeInterval = timeInterval;
		mIsAnimated = isAnimated;
		mNumOfAnimatedIcon = numOfAnimatedIcon;
	}
	
	public Bitmap getIcon(int index)
	{
		int x = mX + index * mXDiff;
		int y = mY + index * mYDiff;
		
		return Bitmap.createBitmap(mSource, x, y, mWidth, mHeight);
	}
	
	public int getNumOfAnimatedIcon()
	{
		return mNumOfAnimatedIcon;
	}
	
	private Bitmap mSource;
	private int mX;
	private int mY;
	private int mWidth;
	private int mHeight;
	private int mXDiff;
	private int mYDiff;
	private int mTimeInterval;
	private boolean mIsAnimated;
	private int mNumOfAnimatedIcon;
	
}
