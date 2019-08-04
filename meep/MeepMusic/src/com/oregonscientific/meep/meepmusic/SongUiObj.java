package com.oregonscientific.meep.meepmusic;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

import com.oregonscientific.meep.opengl.ESTransform;

public class SongUiObj {
	private SongObj mSongObj = null;
	private ESTransform mBgTransform = null;
	private ESTransform mTitleTransform = null;
	private ESTransform mAuthorTansform = null;
	private DisplayLevel mDisplayLevel;
	private int mBgId = 0;
	private int mTextureId;
	private int mTitleWhiteTexId = 0;
	private int mAuthorWhiteTexId = 0;
	private int mTitleBlackTexId = 0;
	private int mAuthorBlackTexId = 0;
	private boolean mIsVisible = false;
	private float mPosition = 0;
	private Reference<Bitmap> whiteImage;
	private Reference<Bitmap> blackImage;
	
	

	public enum DisplayLevel
	{
		LEVEL1,
		LEVEL2,
		LEVEL3,
		LEVEL4
	}

	public SongUiObj()
	{
		mBgTransform = new ESTransform();
		mTitleTransform = new ESTransform();
		mAuthorTansform =new ESTransform();
	}
			
	public SongObj getSongObj() {
		return mSongObj;
	}
	public void setSongObj(SongObj songObj) {
		this.mSongObj = songObj;
	}
	public ESTransform getBgTransform() {
		return mBgTransform;
	}
	public void setmBgTransform(ESTransform bgTransform) {
		this.mBgTransform = bgTransform;
	}
	public DisplayLevel getDisplayLevel() {
		return mDisplayLevel;
	}
	public void setDisplayLevel(DisplayLevel displayLevel) {
		this.mDisplayLevel =displayLevel;
	}
	public int getBgId() {
		return mBgId;
	}
	public void setBgId(int mBgId) {
		this.mBgId = mBgId;
	}
	public boolean isVisible() {
		return mIsVisible;
	}
	public void setIsVisible(boolean isVisible) {
		this.mIsVisible = isVisible;
	}
	public float getPosition() {
		return mPosition;
	}
	public void setPosition(float position) {
		this.mPosition = position;
	}
	public int getTitleWhiteTexId() {
		return mTitleWhiteTexId;
	}
	public void setTitleWhiteTexId(int titleTexId) {
		this.mTitleWhiteTexId = titleTexId;
	}
	public int getAuthorWhiteTexId() {
		return mAuthorWhiteTexId;
	}
	public void setAuthorWhiteTexId(int authorTexId) {
		this.mAuthorWhiteTexId = authorTexId;
	}
	public ESTransform getTitleTransform() {
		return mTitleTransform;
	}
	public void setTitleTransform(ESTransform titleTransform) {
		this.mTitleTransform = titleTransform;
	}
	public ESTransform getAuthorTansform() {
		return mAuthorTansform;
	}
	public void setAuthorTansform(ESTransform authorTansform) {
		this.mAuthorTansform = authorTansform;
	}

	public int getTitleBlackTexId() {
		return mTitleBlackTexId;
	}

	public void setTitleBlackTexId(int titleBlackTexId) {
		this.mTitleBlackTexId = titleBlackTexId;
	}

	public int getAuthorBlackTexId() {
		return mAuthorBlackTexId;
	}

	public void setAuthorBlackTexId(int authorBlackTexId) {
		this.mAuthorBlackTexId = authorBlackTexId;
	}

	/**
	 * @return the mTextureId
	 */
	public int getmTextureId() {
		return mTextureId;
	}

	/**
	 * @param mTextureId the mTextureId to set
	 */
	public void setmTextureId(int mTextureId) {
		this.mTextureId = mTextureId;
	}
	
	/**
	 * @return the blackImage
	 */
	public Reference<Bitmap> getBlackImage() {
		return blackImage;
	}

	/**
	 * @param blackImage the blackImage to set
	 */
	public void setBlackImage(Reference<Bitmap> blackImage) {
		this.blackImage = blackImage;
	}

	/**
	 * @return the whiteImage
	 */
	public Reference<Bitmap> getWhiteImage() {
		return whiteImage;
	}

	/**
	 * @param whiteImage the whiteImage to set
	 */
	public void setWhiteImage(Reference<Bitmap> whiteImage) {
		this.whiteImage = whiteImage;
	}
	
}
