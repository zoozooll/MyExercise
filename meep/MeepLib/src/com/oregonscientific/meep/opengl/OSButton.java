package com.oregonscientific.meep.opengl;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class OSButton {

	private String mName;
	private String mText;
	private OSIcon mIcon;
	private float mPosition;
	private float mX;
	private int mNumOfReceivedMsg;
	private int mTexureID;
	private int mButtonNameTexureID;
	private int mThumbnailTexId;
	private int mDimTexId;
	private int mDummyTexId;
	private int mDimDummyTexId;
	private float dummyWidth;
	private float dummyHeight;
	private Object mTag;
	private Bitmap mTextureBmp = null;
	private Bitmap mDimTextureBmp = null;
	private List<Integer> mAnimatedTexIDs = null;
	private int mTextTexId;
	private int mDimTextTexId;

	private ESTransform mButtonTransform;
	private ESTransform mButtonNameTransform;
	private boolean mVisible;
	private ButtonType mType;
	private List<String> mContentPath;
	private boolean mIsSelected;
	private boolean mIsOnline = false;
	private int mChildCount = 0;
	private boolean inShow;
	/*Added by aaronli at May15*/
	private int showIndex;
	private boolean alRendedBitmap;
	
	
	public enum ButtonType
	{
		NotDefined,
		Group,
		Friend,
		Function,
		Music,
		Ebook,
		Movie,
		Photo,
		Game,
		Camera,
		Youtube,
		Web,
		App, 
		Settings,
		//2013-02-25 - Raymond - Add Partent Control button in MeepHome!
		Parent,
		//2013-02-26 - Raymond - Add JP Yahoo Control button in MeepHome! for JP Lang only
		JPYahoo,
		Safe, 
		Help,
		MeepStore
	}

	public OSButton() {
		mName = "";
		mText = "";
		mIcon = null;
		mPosition = 0;
		mNumOfReceivedMsg = 0;
		mTexureID = 0;
		mDimTexId = 0;
		mDummyTexId = 0;
		mDimDummyTexId = 0;
		mButtonTransform = new ESTransform();
		mButtonNameTransform = new ESTransform();
		setButtonNameTexureID(0);
		mVisible = true;
		mX = 0;
		setType(ButtonType.NotDefined);
		mContentPath = new ArrayList<String>();
		setAnimatedTexIDs(new ArrayList<Integer>());
		mTextTexId = 0;
	}
	
	public float getX() {
		return mX;
	}
	public void setX(float x) {
		this.mX = x;
	}

	public boolean isVisible() {
		return mVisible;
	}

	public void setVisible(boolean isVisible) {
		this.mVisible = isVisible;
	}

	public OSButton(String name, String text, OSIcon icon, int position) {
		mName = name;
		mText = text;
		mIcon = icon;
		mPosition = position;
		mNumOfReceivedMsg = 0;
		mTexureID = 0;
		mButtonTransform = new ESTransform();
		setContentPath(new ArrayList<String>());
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public OSIcon getIcon() {
		return mIcon;
	}

	public void setIcon(OSIcon icon) {
		mIcon = icon;
	}

	public float getPosition() {
		return mPosition;
	}

	public void setPosition(float position) {
		mPosition = position;
	}

	public int getNumOfReceivedMsg() {
		return mNumOfReceivedMsg;
	}

	public void setNumReceivedMsg(int numOfReceivedMsg) {
		this.mNumOfReceivedMsg = numOfReceivedMsg;
	}

	public int getTexureID() {
		return mTexureID;
	}

	public void setTexureID(int texureID) {
		this.mTexureID = texureID;
	}

	public ESTransform getButtonTransform() {
		return mButtonTransform;
	}

	public void setmButtonTransform(ESTransform buttonTransform) {
		this.mButtonTransform = buttonTransform;
	}
	
	public ButtonType getType() {
		return mType;
	}

	public void setType(ButtonType mType) {
		this.mType = mType;
	}

	public List<String> getContentPath() {
		return mContentPath;
	}

	public void setContentPath(List<String> contentPath) {
		this.mContentPath = contentPath;
	}

	public ESTransform getButtonNameTransform() {
		return mButtonNameTransform;
	}

	public void setButtonNameTransform(ESTransform buttonNameTransform) {
		this.mButtonNameTransform = buttonNameTransform;
	}

	public int getButtonNameTexureID() {
		return mButtonNameTexureID;
	}

	public void setButtonNameTexureID(int buttonNameTexureID) {
		this.mButtonNameTexureID = buttonNameTexureID;
	}

	public boolean isSelected() {
		return mIsSelected;
	}

	public void setIsSelected(boolean isSelected) {
		this.mIsSelected = isSelected;
	}

	public int getThumbnailTexId() {
		return mThumbnailTexId;
	}

	public void setThumbnailTexId(int thumbnailTexId) {
		this.mThumbnailTexId = thumbnailTexId;
	}

	public int getDimTexId() {
		return mDimTexId;
	}

	public void setDimTexId(int dimTexId) {
		this.mDimTexId = dimTexId;
	}

	public Object getTag() {
		return mTag;
	}

	public void setTag(Object tag) {
		this.mTag = tag;
	}

	public Bitmap getTextureBmp() {
		return mTextureBmp;
	}

	public void setTextureBmp(Bitmap textureBmp) {
		this.mTextureBmp = textureBmp;
	}

	public Bitmap getDimTextureBmp() {
		return mDimTextureBmp;
	}

	public void setDimTextureBmp(Bitmap dimTextureBmp) {
		this.mDimTextureBmp = dimTextureBmp;
	}

	public int getDummyTexId() {
		return mDummyTexId;
	}

	public void setDummyTexId(int dummyTexId) {
		this.mDummyTexId = dummyTexId;
	}

	public int getDimDummyTexId() {
		return mDimDummyTexId;
	}

	public void setDimDummyTexId(int dimDummyTexId) {
		this.mDimDummyTexId = dimDummyTexId;
	}

	public int getTextTexId() {
		return mTextTexId;
	}

	public void setTextTexId(int mTextTexId) {
		this.mTextTexId = mTextTexId;
	}
	
	public List<Integer> getAnimatedTexIDs() {
		return mAnimatedTexIDs;
	}

	public void setAnimatedTexIDs(List<Integer> animatedTexIDs) {
		this.mAnimatedTexIDs = animatedTexIDs;
	}

	public boolean isOnline() {
		return mIsOnline;
	}

	public void setIsOnline(boolean isOnline) {
		this.mIsOnline = isOnline;
	}

	public int getChildCount() {
		return mChildCount;
	}

	public void setChildCount(int childCount) {
		this.mChildCount = childCount;
	}

	public int getDimTextTexId() {
		return mDimTextTexId;
	}

	public void setDimTextTexId(int dimTextTexId) {
		this.mDimTextTexId = dimTextTexId;
	}

	/**
	 * @return the inShow
	 */
	public boolean isInShow() {
		return inShow;
	}

	/**
	 * @param inShow the inShow to set
	 */
	public void setInShow(boolean inShow) {
		this.inShow = inShow;
	}

	public int getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

	/**
	 * @return the alRendedBitmap
	 */
	public boolean isAlRendedBitmap() {
		return alRendedBitmap;
	}

	/**
	 * @param alRendedBitmap the alRendedBitmap to set
	 */
	public void setAlRendedBitmap(boolean alRendedBitmap) {
		this.alRendedBitmap = alRendedBitmap;
	}

	/**
	 * @return the dummyWidth
	 */
	public float getDummyWidth() {
		return dummyWidth;
	}

	/**
	 * @param dummyWidth the dummyWidth to set
	 */
	public void setDummyWidth(float dummyWidth) {
		this.dummyWidth = dummyWidth;
	}

	/**
	 * @return the dummyHeight
	 */
	public float getDummyHeight() {
		return dummyHeight;
	}

	/**
	 * @param dummyHeight the dummyHeight to set
	 */
	public void setDummyHeight(float dummyHeight) {
		this.dummyHeight = dummyHeight;
	}
}
