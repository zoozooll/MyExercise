package com.oregonscientific.meep.opengl;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.RecycleBin.RecyclerListener;

/*************************************************
 * It was greate change at Apr19 2013.
 * Follow:
 * 1. create the class {@link ISnakeDataSource}.The interface should be used to load datasource in 
 * every software.The private methods is still at {@link SnakeShapeCtrl} and the custom methods should
 * be changed in every software.
 * 2. Loading files should be change to every software
 * 3.  the methods and field no used should be deprecated. And I had modified to the end of this file
 * 
 * @author aaronli
 * 
 *****************************************/
class SnakeShapeCtrl implements RecyclerListener {
	
	private static final String TAG = "SnackShapeCtrl";

	private final float ADJUST_ANGLE = 2;
	private final float START_ANGLE = 120;
	private final float MIN_ANGLE =-120;
	private final float MAX_ANGLE = 180;
	private final float RIGHT_ANGLE = -75;
	private final float MIN_BOUND = RIGHT_ANGLE + ADJUST_ANGLE -2;
	private final float MAX_BOUND = RIGHT_ANGLE + ADJUST_ANGLE +2;
	private final float MAX_FLING_TIME = 1.3f;
	private final float TOTAL_DAMP_VALUE = 19.787376f;
	private static final int FIRST_LENGTH_TEXTURES = 30;
	
	
	private AppType mAppType =AppType.NotDefined; 
	private ISnakeDataSource mdataSource;
	// add by aaronli May22 for snackAdapter
	private SnakeAdapter<OsListViewItem> mAdapter;
	private Context mContext = null;
	private boolean surefaceRenderCreated;
	private float mFlingTime;
	private float mFlingSpeed;
	private float mTotalDistance = 0;
	private boolean mIsNextFlingTime;
	//added by aaronli Jun26 2013.
	private short mNextScrollState;
	//added end
	private float mFirstButtonAngle = 0;
	private int mLastIndex = 0;
	private int mSelectedButtonIdx = 0;
	/**The first item in show.Its position is bigger than MIN_BOUND */
	private int mFirstShowing;
	/**The end item in show.Its position is less than MAX_BOUND */
	private int mLastShowing;
	
	private int mDummyImageTxtID = -1;
	private int mDefaultImageTxtId = -1;
	private int mDefaultImageDimTxtId = -1;
	
	//private List<OSButton> mButtonPool = null;
	//private List<OSButton> mButtonList = null;
	/** The same as {@link OpenGlRender}. mLightTextureIds*/
	private int[] mLightTextureIds;
	/** The same as {@link OpenGlRender}. mDimTextureIds*/
	//private int[] mDimTextureIds;
	
	private int textureIdsIndex;
	
	private SnackCtrlState mSnackCtrlState = SnackCtrlState.LEVEL1_TO_4C;
	private ActionState mActionState = ActionState.Idle;
	//added the fields by aaronli at Jun19 2013. it is value of dummy image'width and height
	private int mDummyWidth;
	private int mDummyHeight;
	// added end
	private Bitmap mDummyImage = null;
	private Bitmap mDefaultImage = null;
	private Bitmap mDefaultImageDim = null;
	
	private boolean mIsInitSnackShape = false;
	
	private SnackCtrlListener onSnackCtrlListener;

	private RecycleBin mRecycler;
	
	public SnakeShapeCtrl(AppType appType, ISnakeDataSource dataSource, Bitmap dummyImage,
			Bitmap defaultImage, Bitmap defaultImageDim) {
		mAppType = appType;
		mdataSource = dataSource;
		mDummyImage = dummyImage;
		mDefaultImage = defaultImage;
		mDefaultImageDim = defaultImageDim;
		init();
	}

	public SnakeShapeCtrl(AppType appType, SnakeAdapter<OsListViewItem> adapter, Bitmap dummyImage,
			Bitmap defaultImage, Bitmap defaultImageDim) {
		mAppType = appType;
		mAdapter = adapter;
		mDummyImage = dummyImage;
		mDefaultImage = defaultImage;
		mDefaultImageDim = defaultImageDim;
		init();
	}
	
	public SnackCtrlState getSnackCtrlState() {
		return mSnackCtrlState;
	}

	public void setSnackCtrlState(SnackCtrlState state) {
		mSnackCtrlState = state;
	}

	public void setActionState(ActionState actionState) {
		mActionState = actionState;
	}

	public ActionState getActionState() {
		return mActionState;
	}

	public boolean isInitSnackShape()
	{
		return mIsInitSnackShape;
	}
	
	private void init() {
		mRecycler = new RecycleBin();
		mRecycler.setRecyclerListener(this);
		setFlingDistance(RIGHT_ANGLE - START_ANGLE);

		setFirstButtonAngle(START_ANGLE);
		
		mLightTextureIds = new int[FIRST_LENGTH_TEXTURES];
		//mDimTextureIds = new int[FIRST_LENGTH_TEXTURES];
		mDummyWidth = mDummyImage.getWidth();
		mDummyHeight = mDummyImage.getHeight();
		//initPath();
		
		//mButtonPool = new ArrayList<OSButton>();
		
		
		//mContentPathPool = new ArrayList<String>();
		
	}
		
	public void initSnake() {
		mIsInitSnackShape = true;
		//loadButtonWithDummyImage();
		//boolean initOSButtonComplete = initOSButtonList();
		
	/*	if (initOSButtonComplete) {
			initLoadImageThread();
			startLoadImageThread();
		}*/
	}
	
	public void onSnackSurfaceCreated() {
		getDefaultImageTxtID();
		getDefaultImageDimTxtId();
		//if (mAppType != AppType.App && mAppType != AppType.Game) {
		initSnake();
		//}
		surefaceRenderCreated = true;
	}
	
	
	
	
	//*********interface*******
	public interface SnackCtrlListener
    {
		/**
		 * 
		 * @param data
		 * @param index
		 */
    	public void buttonSelectedIndexChanged(Object data, int index);
    	
    	/**
    	 * @author aaronli at Jun26 2013
    	 */
    	public void onLoadingButtonList(OSButton view);
    	
    	/**
    	 * @author aaronli at Jun26 2013
    	 */
    	public void onFlingStop();
    }
	
	
	public void setOnSnackCtrlListener(SnackCtrlListener listener)
	{
		onSnackCtrlListener = listener;
	}
	
	//----------------------
	
	
	//*******************************load image************************
	
	/*public List<OSButton> getButtonList() {
		return mButtonList;
	}
*/
	/**
	 * @Did Great modified by Aaronli at Apr9 2013. All {@link AppType} used the same 
	 * this method.The different is implementing {@link ISnakeDataSource}'s loadDataSource()
	 *
	 */
	private void loadButtonWithDummyImage() {
		/*List<OsListViewItem> datas = mdataSource.loadDataSource();

		int count = 15;
		if (datas.size() > 15) {
			count = datas.size();
		}
		
		for (int i = 0; i < count; i++) {
			OSButton button = new OSButton();
			String name = "";
			
			if (i < datas.size()) {
				OsListViewItem item = datas.get(i);
				name = item.getName();
				button.setName(name);
				button.setText(name);
				button.getContentPath().add(item.getPath());
				//mContentPathPool.add(thisFile.getAbsolutePath());
				button.setTexureID(getDefaultImageTxtID());
				button.setDimTexId(getDefaultImageDimTxtId());
				// Release the variable
			} else {
				button.setName(name);
				button.setText(name);
			}
			
			button.setDummyTexId(getDummyImageTxtID());
			button.setDimDummyTexId(getDummyImageTxtID());
			mButtonPool.add(button);
			
			// Release the variable
			name = null;
			button = null;
		}*/
	}

	
	public int getDummyImageTxtID() {
		if (mDummyImageTxtID == -1) {
			//Bitmap bmp = BitmapFactory.decodeFile( BASIC_STORAGE_PATH + "app/default/dummy.png");
			//mDummyImageTxtID =  MediaManager.loadTexture(bmp);
			mDummyImageTxtID =  MediaManager.loadTexture(mDummyImage);
			//Log.d("dummyTextureId", "dummy textureID:" + mDummyImageTxtID);
		}
		//Log.d("mDummyImageTxtID", ""+mDummyImageTxtID);
		return mDummyImageTxtID;
	}
	
	public int getDefaultImageTxtID() {
		if (mDefaultImageTxtId <= 0) {
			//Bitmap bmp = BitmapFactory.decodeFile( BASIC_STORAGE_PATH + "app/default/dummy.png");
			//mDummyImageTxtID =  MediaManager.loadTexture(bmp);
			mDefaultImageTxtId =  MediaManager.loadTexture(mDefaultImage);
			//Log.d("mDefaultImageTxtId", "mDefaultImageTxtId:" + mDefaultImageTxtId);
		}
		//Log.d("mDefaultImageTxtId", ""+mDefaultImageTxtId);
		return mDefaultImageTxtId;
	}
	public int getDefaultImageDimTxtId() {
		if (mDefaultImageDimTxtId <= 0) {
			//Bitmap bmp = BitmapFactory.decodeFile( BASIC_STORAGE_PATH + "app/default/dummy.png");
			//mDummyImageTxtID =  MediaManager.loadTexture(bmp);
			mDefaultImageDimTxtId =  MediaManager.loadTexture(mDefaultImageDim);
			//Log.d("mDefaultImageDimTxtId", "mDefaultImageDimTxtId:" + mDefaultImageDimTxtId);
		}
		//Log.d("mDefaultImageDimTxtId", ""+mDefaultImageDimTxtId);
		return mDefaultImageDimTxtId;
	}
	 

	public void recylingPhotoButtonBitmap(OSButton button) {
		Bitmap b = button.getTextureBmp();
		if (b != null && !b.isRecycled() && b != mDefaultImage) {
			b.recycle();
		}
		// mofified by aaronli at Mar22 2013 made snack view run faster
		button.setTextureBmp(null);
		
		b = button.getDimTextureBmp();
		if (b != null && !b.isRecycled() && b != mDefaultImageDim) {
			b.recycle();
		}
		button.setDimTextureBmp(null);
	}
	
	
	/**
	 * get index of the osbutton which does not use the dummy photo
	 * this is for calculating the boundry
	 */
	public synchronized void resetDummyIndex() {
		
//		for (int i = 0; i < mButtonPool.size(); i++) {
//			Log.d("resetDummyIndex", "" + i + ":" + mButtonPool.get(i).getTexureID() );
//		}

		/*for (int i = 0; i < mButtonList.size(); i++) {
			if (mButtonList.get(i).getTexureID() == 0) {
				if (i != 0) {
					mLastIndex = i-1;
					//Log.d("resetDummyIndex", "size: " +mButtonList.size()+ " Dummy index:" + i);
					return;
				}
			}
		}
		mLastIndex = mButtonList.size()-1;*/
		
	}
	
	/*private synchronized OSButton findOsButtonByName(String name)
	{
		for(int i = 0; i<mButtonPool.size();i++)
		{
			if(mButtonPool.get(i).getName().equals(name)){
				return mButtonPool.get(i);
			}
		}
		return null;
	}*/
	
	//2013-02-18 - removeOSButtonByName
	/*private synchronized OSButton removeOsButtonByName(String name)
	{
		Log.e("removeOsButtonByName","before"+mButtonPool.size());
		for(int i = 0; i<mButtonPool.size();i++)
		{
			if(mButtonPool.get(i).getName().equals(name)){
				mButtonPool.remove(i);
			}
		}
		Log.e("removeOsButtonByName","after:"+mButtonPool.size());
		return null;
	}	*/
	
	/*private synchronized OSButton findOsButtonByText(String text)
	{
		for(int i = 0; i<mButtonPool.size();i++)
		{
			if(mButtonPool.get(i).getText().equals(text)){
				return mButtonPool.get(i);
			}
		}
		return null;
	}*/
	
	/*private synchronized OSButton findOsButtonByContent(String content)
	{
		for(int i = 0; i<mButtonPool.size();i++)
		{
			if(mButtonPool.get(i).getContentPath().get(0).equals(content)){
				return mButtonPool.get(i);
			}
		}
		return null;
	}*/
	

	
	//______end load image______
	
	//******handle animation******
	/*public boolean initOSButtonList() {
		if (mButtonPool.size() > 0) {
			mButtonList = new Vector<OSButton>();
			for (int i = 0; i < mButtonPool.size(); i++) {
				if (mButtonPool.get(i).isVisible()) {
					mButtonList.add(mButtonPool.get(i));
				} else {
					mButtonPool.get(i).setPosition(-120);
				}
			}

			for (int i = 0; i < mButtonList.size(); i++) {
				//mButtonList.get(i).setPosition(RIGHT_ANGLE - i * 15);
				mButtonList.get(i).setPosition(START_ANGLE);
				//Log.d("initOSButtonList", "initOSButtonList:" + mButtonList.get(i).getPosition());
			}
			setFlingDistance(RIGHT_ANGLE - START_ANGLE);

			setFirstButtonAngle(mButtonList.get(0).getPosition());
			
			return true;
		} else {
			Log.e("initOSButtonList", "initOSButtonList : no button in the list");
			
			return false;
		}
	}*/
	
	private float getDampValue(float startDist, float time, float dampingFactor, float w0) {
		double omegaD = w0;
		double omega0 = w0 / Math.sqrt(1 - Math.pow(dampingFactor, 2));
		double degT = omegaD * time;
		double resultTemp;
		resultTemp = startDist * Math.cos(degT);
		resultTemp += dampingFactor * omega0 * startDist / omegaD * Math.sin(degT);
		resultTemp *= Math.exp(-dampingFactor * omega0 * time);

		return (float) resultTemp;
	}
	
	public void setFlingSpeed(float flingSpeed)
	{
		// fixed #4181 by aaronli at Jun26 2013
		mFlingSpeed = flingSpeed/1000f;
		resetNextFlingTime();
		setFlingDistance(getFlingDistance());
	}
	
	public void setFlingToRightPosition () {
		//Log.d(TAG, "setFlingToRightPosition " + mSelectedButtonIdx);
		float dis = RIGHT_ANGLE - getCurrentSelectedAngle();
		if (Math.abs(dis) > .0001f) {
			//Log.d(TAG, "setFlingToRightPosition "+getCurrentSelectedAngle());
			mActionState = ActionState.Fling;
			mFlingSpeed = 150.0f;
			resetNextFlingTime();
			setFlingDistance(dis);
		}
	}
	
	public void setNextFlingTime() {
		mFlingTime += 0.02;
		mIsNextFlingTime = true;
	}

	public void resetNextFlingTime() {
		mFlingTime = 0;
	}
	
	private void setFlingDistance(float distance)
	{
		mTotalDistance= distance;
		//Log.d("setFlingDistance", "setFlingDistance : " + distance);
	}
	
	/*public synchronized void setHideDistance()
	{
		setFlingDistance(-75f - mButtonList.get(mButtonList.size() - 1).getPosition() + 180);
	}*/
	
	public boolean move1Item()
	{
		float maxDistance = RIGHT_ANGLE - getCurrentSelectedAngle();
		//Log.d("adjust", "move 1 max " + maxDistance );
		if(maxDistance<-10f)
		{
			setFlingDistance(- 15);
			return true;
		}
		return false;
	}
	
	public boolean move2Item()
	{
		float maxDistance = RIGHT_ANGLE - getCurrentSelectedAngle();
		//Log.d("adjust", "move 2 " + maxDistance);
		if(maxDistance<-25f)
		{
			setFlingDistance(- 30);
			return true;
		}
		return false;
	}
	
	public OsListViewItem getLevel4SelectedButton()
	{
		/*
		 * 
		 * for(int i = 0; i< mButtonList.size(); i++)
		{
			if(mButtonList.get(i).isSelected())
			{
				 return mButtonList.get(i);
			}
		}
		return null;*/
		if (mAdapter != null && mSelectedButtonIdx != -1) {
			
			return mAdapter.getItem(mSelectedButtonIdx);
		}
		return null;
	}
	
	// fixed snack view not scrolled smoothly #4092
	// by aaronli Jun21 2013
	public float getFlingDistance()
	{
		float tempTotalDistance = TOTAL_DAMP_VALUE * mFlingSpeed;
		int numOfItems = (int)(Math.abs(tempTotalDistance)/15);
		//Log.d("getFlingDistance","getFlingDistance speed= " + mFlingSpeed + " number of item:" + numOfItems);
		//control the movie not to move outside the boundry
		if (mFlingSpeed > 0) {
			float maxDistance = RIGHT_ANGLE - mFirstButtonAngle;
			/*Log.d("getFlingDistance", "getFlingDistance tempTotalDistance path 1:" + tempTotalDistance + " max distance:" + maxDistance + " mangle:"
					+ mFirstButtonAngle);*/
			if (tempTotalDistance > maxDistance) {
				//Log.d("getFlingDistance", "getFlingDistance path 1");
				return maxDistance;
			}
		} else {
			float maxDistance = RIGHT_ANGLE - getLastItemAngle();
			//Log.d("getFlingDistance", "getFlingDistance tempTotalDistance path 2:" + tempTotalDistance + " max distance:" + maxDistance + " mangle:"
					//+ mFirstButtonAngle + " lastIdx:" + mLastIndex);
			if (tempTotalDistance < maxDistance) {
				//Log.d("getFlingDistance", "getFlingDistance path 2");
				return maxDistance;
			}
		}
		
		if (numOfItems != 0) {
			float flingDistance = 0;
			//Log.d("getFlingDistance", "getFlingDistance tempTotalDistance" + tempTotalDistance);
			if (tempTotalDistance > 0) {
				//Log.d("getFlingDistance", "getFlingDistance path 3");
				flingDistance = numOfItems * 15 + (15 - mFirstButtonAngle % (15));
				//Log.d("Total", "onfling flingDistance > 0 --:" + flingDistance);
			} else {
				//Log.d("getFlingDistance", "getFlingDistance path 4 ( " + (-numOfItems * 15) + " - " +  mButtonList.get(0).getPosition() % (15) + ")");
				flingDistance = -(numOfItems+1) * 15 - mFirstButtonAngle % (15);
				//Log.d("Total", "onfling flingDistance < 0 --:" + flingDistance);
			}
			return flingDistance;
		}
		else {
			float flingDistance = 0;
			float remainMove = mFirstButtonAngle % 15 + 15;
			float flingMove =  tempTotalDistance % 15;
			if (flingMove < 0) {
				flingMove += 15;
			}
			float movePos = remainMove + flingMove;
			//Log.d("getFlingDistance", "getFlingDistance path 5/6 movePos: " +remainMove + " + " +flingMove + " = " +movePos);
			
			if(tempTotalDistance>0)
			{
				if(movePos>7.5){
					//Log.d("getFlingDistance", "getFlingDistance  5.1");
					flingDistance = 15 - remainMove;
				}
				else{
					//Log.d("getFlingDistance", "getFlingDistance  5.2");
					flingDistance =  remainMove;
				}
			}
			else
			{
				if(movePos>7.5){
					//Log.d("getFlingDistance", "getFlingDistance  6.1");
					flingDistance = - remainMove;
				}
				else{
					//Log.d("getFlingDistance", "getFlingDistance  6.2");
					flingDistance =  -15 - remainMove;
				}
			}
			//Log.d("getFlingDistance", "getFlingDistance  flingdistance: " + flingDistance);
			return flingDistance;

		}
	}
	// fixed #4092 end
	
	public void setLevel4XDistance(float xdistance)
	{
		setButtonAngle(mFirstButtonAngle + xdistance);
		//Log.d("setLevel4XDistance", "setLevel4XDistance:" + xdistance);
	}
	
	private synchronized void setFirstButtonAngle(float firstButtonAngle)
	{
		//Log.d("setButtonAngle", "setButtonAngleS: " + firstButtonAngle );
		mFirstButtonAngle = firstButtonAngle;
	}
	
	private synchronized void setButtonAngle(float firstAngle)
	{
		if (mAdapter != null) {
			//Log.d("setButtonAngle", "setButtonAngleS: " + firstAngle + " buttonlistsize  = " + " buttonlistsize  = " + mButtonList.size());
		}
		//SystemClock.uptimeMillis();
		setFirstButtonAngle(firstAngle);
		/*float tempPosition;
		for (int i = 0; i < mButtonList.size(); i++) {
			final OSButton button = mButtonList.get(i);
			tempPosition = mFirstButtonAngle + 15 * i;
			button.setPosition(tempPosition);
			if (tempPosition <= MAX_ANGLE && tempPosition >= MIN_ANGLE) {
				//the button is showing in screen.
				if (!button.isInShow()) {
					new Thread(new LoadingBitmapTask(button)).start();
					button.setInShow(true);
				}
				
				// set selected button
				if (tempPosition > MIN_BOUND && tempPosition < MAX_BOUND) {
					setSelectedIndex(i);
				}
			} else {
				// the button is not showing in screen.
				if (button.isInShow()) {
					new Thread(new RecycleBitmapTask(button)).start();
					button.setInShow(false);
				}
			}
		}*/
		//Log.d(TAG, "setButtonAngle firstAngle "+firstAngle);
		setFirstAndLastShowing((int) (((MIN_ANGLE - firstAngle)/15) - 1), (int) ((MAX_ANGLE - firstAngle)/15) + 1) ;
		
		int flag = (int) ((MAX_BOUND - firstAngle)/15);
		/*if (flag * 15 >= MAX_BOUND) {
			flag--;
		}*/ 
		if (flag < 0){
			flag = 0;
		}
		if (flag >= mAdapter.getCount() - 15) {
			flag = mAdapter.getCount() - 16;
		}
		setSelectedIndex(flag);
		
		for (int i = mFirstShowing; i <= mLastShowing; i++) {
			final float tempPosition = mFirstButtonAngle + 15 * i;
			OSButton button = obtainView(i);
			if (button != null) {
				button.setPosition(tempPosition);
			}
		}
	}
	
	private synchronized void setSelectedIndex(int selectedIndex)
	{
		//Log.d(TAG, "setSelectedIndex " + selectedIndex);
		if(selectedIndex != mSelectedButtonIdx)
		{
			if(mSelectedButtonIdx != -1)
			{
			//mButtonList.get(mSelectedButtonIdx).setIsSelected(false);
			}
			//mButtonList.get(selectedIndex).setIsSelected(true);
			if (selectedIndex < 0) {
				selectedIndex = 0;
			} else if (selectedIndex >= mAdapter.getCount() - 15) {
				selectedIndex = mAdapter.getCount() - 16;
			}
			// added by aaronli Jul12 2013.showing in when empty.fixed #4181
			if (mAdapter.isEmpty()) {
				selectedIndex = 0;
			}
			//Log.d(TAG, "setSelectedIndex " + selectedIndex);
			mSelectedButtonIdx = selectedIndex;
		}
		if(onSnackCtrlListener!= null)
		{
			onSnackCtrlListener.buttonSelectedIndexChanged(mAdapter.getItem(selectedIndex), selectedIndex);
		}
	}
	
	/*
	 * added by aaronli at May22.
	 * every item should be loaded by adapter.
	 */
	private synchronized void setFirstAndLastShowing(int first, int last) {
		if (first< 0) {
			first = 0;
		}
		if (first > mFirstShowing) {
			//new Thread(new RecycleBitmapTask(obtainView(mFirstShowing))).start();
			mRecycler.removeActiveView(mFirstShowing);
			mFirstShowing = first;
		} else if(first < mFirstShowing) {
			//new Thread(new LoadingBitmapTask(obtainView(mFirstShowing))).start();
			mFirstShowing = first;
		}
		
		if (last >= mAdapter.getCount()) {
			last = mAdapter.getCount() -1;
		}
		if (last < mLastShowing) {
			//new Thread(new RecycleBitmapTask(obtainView(mLastShowing))).start();
			mRecycler.removeActiveView(mLastShowing);
			mLastShowing = last;
		} else if (last > mLastShowing) {
			//new Thread(new LoadingBitmapTask(obtainView(mLastShowing))).start();
			mLastShowing = last;
		}
		
	}
	//end added
	
	
	
	//*******handle state **********

	//private float move = 0;
	
	void level1To4CShowup(OSButton button, float aspect, int idx)
	{

		if (mFlingTime<MAX_FLING_TIME) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				float move = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
				setButtonAngle(mFirstButtonAngle + move);
				mIsNextFlingTime = false;
			}
			//Log.d("level1_to_4Showup", "level1_to_4Showup, angle:" + button.getPosition());
			//button.setPosition(button.getPosition() + move);
		} else {
			setSnackCtrlState(SnackCtrlState.LEVEL4);
		}
		calculatePositionForSnackShape(button,aspect );
	}
	
	public void level1To4CShowup(float aspect)
	{
		if (mFlingTime<MAX_FLING_TIME) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				float move = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
				setButtonAngle(mFirstButtonAngle + move);
				mIsNextFlingTime = false;
			}
			//Log.d("level1_to_4Showup", "level1_to_4Showup, angle:" + button.getPosition());
			//button.setPosition(button.getPosition() + move);
		} else {
			setSnackCtrlState(SnackCtrlState.LEVEL4);
		}
		//2013-03-19 - raymond - catch concurrent access exception
		try{
			/*for (OSButton button: mButtonList) {
				calculatePositionForSnackShape(button,aspect );
			}*/
			for (int i = mFirstShowing; i <= mLastShowing ; i++) {
				OSButton button = obtainView(i);
				calculatePositionForSnackShape(button,aspect);
			}
		}catch(Exception e){
			Log.w(TAG, "Snacklevel1To4CShowup", e);
		}
	}
	
	public void level4_to_1Hide(OSButton button, float aspect)
	{
		if (mFlingTime<MAX_FLING_TIME-0.5f) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				float move = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
				setButtonAngle(mFirstButtonAngle + move);
				mIsNextFlingTime = false;
			}
			//Log.d("level1_to_4Showup", "level1_to_4Showup, angle:" + button.getPosition());
		} else {
			setSnackCtrlState(SnackCtrlState.LEVEL4_TO_1B);
		}
		calculatePositionForSnackShape(button,aspect );
	}
	
	/*
	 * added by aaronli at May22.
	 * every item should be loaded by adapter.
	 */
	/**
	 * 
	 * @param aspect
	 */
	public void level4_to_1Hide(float aspect)
	{
		if (mFlingTime<MAX_FLING_TIME-0.5f) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				float move = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
				setButtonAngle(mFirstButtonAngle + move);
				mIsNextFlingTime = false;
			}
			//Log.d("level1_to_4Showup", "level1_to_4Showup, angle:" + button.getPosition());
		} else {
			setSnackCtrlState(SnackCtrlState.LEVEL4_TO_1B);
		}
		/*for(OSButton button: mButtonList) {
			calculatePositionForSnackShape(button,aspect );
		}*/
		for (int i = mFirstShowing; i <= mLastShowing ; i++) {
			OSButton button = obtainView(i);
			calculatePositionForSnackShape(button,aspect);
		}
	}
	
	public synchronized void level4Idle(OSButton button, float aspect)
	{
		//Log.d("fling", "fling:");
		calculatePositionForSnackShape(button,aspect);
	}
	
	/*
	 * modified by aaronli at May22.
	 * every item should be loaded by adapter.
	 */
	public void level4Idle(float aspect)
	{
		//2013-03-19 - raymond - catch concurrent access exception
		try{
			/*for (OSButton button: mButtonList) {
				calculatePositionForSnackShape(button,aspect );
			}*/
			for (int i = mFirstShowing; i <= mLastShowing ; i++) {
				OSButton button = obtainView(i);
				calculatePositionForSnackShape(button,aspect);
			}
		}catch(Exception e){
			Log.e("Snacklevel4Idle",e.toString());
		}
		
	}
	
	public synchronized void level4Scroll(OSButton button, float aspect)
	{
		calculatePositionForSnackShape(button,aspect);
	}
	
	/*
	 * modified by aaronli at May22.
	 * every item should be loaded by adapter.
	 */
	public void level4Scroll(float aspect)
	{
		
		//2013-03-19 - raymond - catch concurrent access exception
		try{
			/*for (OSButton button : mButtonList) {
				calculatePositionForSnackShape(button,aspect);
			}*/
			for (int i = mFirstShowing; i <= mLastShowing ; i++) {
				OSButton button = obtainView(i);
				calculatePositionForSnackShape(button,aspect);
			}
		}catch(Exception e){
			Log.e("Snacklevel4Idle",e.toString());
		}		
	}
	
	/*
	 * scrolled more smoothy  than before. fixed #4539.
	 */
	public void scrollToFling() {
		mActionState = ActionState.Fling;
	}
	
	synchronized void level4Fling(OSButton button, float aspect)
	{
		if (mFlingTime<MAX_FLING_TIME && mFlingTime != 0) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				float move = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
				//Log.d("setButtonAngle", "setButtonAngleS: "+ mFirstButtonAngle + "move " + move + " flingtime:" + mFlingTime );
				setButtonAngle(mFirstButtonAngle + move);
				
				mIsNextFlingTime = false;
			}
			//button.setPosition(button.getPosition() + move);
		} 
		calculatePositionForSnackShape(button,aspect);
	}
	
	
	/*
	 * modified by aaronli at May22.
	 * every item should be loaded by adapter.
	 */
	public void level4Fling(float aspect)
	{
		//Log.d(TAG, "mFlingTime "+ mFlingTime);
		
		if (mActionState != ActionState.Fling) {
			return;
		}
		if (mFlingTime<MAX_FLING_TIME && mFlingTime != 0) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				float move = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
				setButtonAngle(mFirstButtonAngle + move);
				
				mIsNextFlingTime = false;
			}
			//button.setPosition(button.getPosition() + move);
		}  else { // modified by aaronli at Jun26 2013.
			mActionState = ActionState.Idle;
			mFlingTime = 0;
			if (onSnackCtrlListener != null) {
				onSnackCtrlListener.onFlingStop();
			}
		}
		// modified end
		/*for (OSButton button : mButtonList) {
			calculatePositionForSnackShape(button,aspect);
		}*/
		for (int i = mFirstShowing; i <= mLastShowing ; i++) {
			OSButton button = obtainView(i);
			calculatePositionForSnackShape(button,aspect);
		}
	}
	
	/**
	 * Get item from RecycleBin. If empty ,it should get from {@link SnakeAdapter}
	 * @param indexAllItems The index of all items.The first number is 0 and the last number is {@link SnakeAdapter}.getCount - 1
	 * @return 
	 * @author aaronli at May22,2013
	 */
	OSButton obtainView(int indexAllItems) {
		if (indexAllItems >= mAdapter.getCount()) {
			return null;
		}
		OSButton activeView = mRecycler.getActiveView(indexAllItems);
		if (activeView != null) {
			//Log.d(TAG, "activeView "+activeView.getName());
			return activeView;
		}
		OSButton scrapView;
		scrapView = mRecycler.getScrapView(indexAllItems);
		OSButton child;
		if (scrapView != null) {
			//it must be the view scrapView,which changed the name,path.
			child = mAdapter.getView(indexAllItems, scrapView);
			if (child != scrapView) {
				mRecycler.removeScrapView(scrapView);
				//setDefaultButtonTexture(child);
				//mRecycler.addActiveView(child, indexAllItems);
			}
		} else {
			// it must be new view. Added by aaronli,Jun6 2013
			child = mAdapter.getView(indexAllItems, null);
			setDefaultButtonTexture(child);
			
			bindTextureIdToView(child);
		}
		
		if(indexAllItems >= mFirstShowing && indexAllItems <= mLastShowing) {
			mRecycler.addActiveView(child, indexAllItems);
		} else {
			mRecycler.removeActiveView(indexAllItems);
		}
		//Log.d(TAG, "obtainView "+child.getName());
		return child;
	}
	
	private void bindTextureIdToView(OSButton view) {
		// modified by aaronli at Jul2 2013.Fixed the #4666 #4681.
		// did: grew the textureIdsIndex when the mLightTextureIds to the end, and general the textures
		//Log.d(TAG, "bindTextureIdToView "+textureIdsIndex);
		if (textureIdsIndex < mLightTextureIds.length) {
			//view.setDimTexId(mDimTextureIds[textureIdsIndex]);
			view.setTexureID(mLightTextureIds[textureIdsIndex]);
		} else {
			int[] flatIds = new int[FIRST_LENGTH_TEXTURES];
			MediaManager.genTextureIds(flatIds);
			int[] newIds = new int[mLightTextureIds.length + FIRST_LENGTH_TEXTURES];
			mLightTextureIds = MediaManager.combine(mLightTextureIds, newIds);
			bindTextureIdToView(view);
			return;
		}
		// fixed #4666 #4681 end;
		textureIdsIndex ++;
	}
	
	void deleteSelectedItem() {
		OsListViewItem item = mAdapter.getItem(mSelectedButtonIdx);
		mAdapter.onDelectedItem(item);
		loadDataChanged();
		
	}
	
	void renameSelectedItem() {
		OsListViewItem item = mAdapter.getItem(mSelectedButtonIdx);
		mAdapter.onRenameItem(item);
		loadDataChanged();
	}
	
	void clearTempViewTextureId() {
		mDefaultImageTxtId = -1;
		mDefaultImageDimTxtId = -1;
		mRecycler.scrapAllActiveView();
		mRecycler.removeAllScrapViews();
		//mRecycler.resetAllScrapViewsTexture();
	}

	private void setDefaultButtonTexture(OSButton child) {
		child.setPosition(START_ANGLE);
		child.setDummyTexId(getDummyImageTxtID());
		child.setDimDummyTexId(getDummyImageTxtID());
	}
	
	void loadDataChanged() {
		mRecycler.scrapAllActiveView();
		//mRecycler.removeAllScrapViews();
		setFirstAndLastShowing((int) (((MIN_ANGLE - mFirstButtonAngle)/15) - 1), (int) ((MAX_ANGLE - mFirstButtonAngle)/15) + 1) ;
		if (onSnackCtrlListener != null && mSelectedButtonIdx >=0 && mSelectedButtonIdx < mAdapter.getCount()) {
			onSnackCtrlListener.buttonSelectedIndexChanged(mAdapter.getItem(mSelectedButtonIdx), mSelectedButtonIdx);
		}
		setActionState(ActionState.Fling);
		//setFlingDistance(RIGHT_ANGLE - START_ANGLE);
		//setFirstButtonAngle(START_ANGLE);
	}
	
	
	private synchronized void calculatePositionForSnackShape(OSButton button, float aspect) {
		if (button == null) {
			return;
		}
		
		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();

		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();
		perspective.perspective(60.0f, aspect, 1.0f, 60.0f);
		perspective.translate(10.2f, 1.1f, -5.6f);
        perspective.rotate(-93, 0f, 1f, 0f);
        perspective.rotate(-19, 0f, 0f, 1f);

		modelview.matrixLoadIdentity();
		float aspectDummy = (float)mDummyWidth/(float)mDummyHeight;

		if(button.getPosition() > MAX_ANGLE)
        {
        	//skip
        }
		else if(button.getPosition()  >= 0)
        {
	        modelview.translate((float)( Math.sin((button.getPosition())*Math.PI/180))*4,  
	        							0 , 
	        							-12.5f + (float)Math.sin((button.getPosition()+90)*Math.PI/180)*4 -4);
	        // Rotate the cube
	        modelview.rotate(-button.getPosition()+180, 0.0f, 1.0f, 0.0f);
	        modelview.rotate(9, 1.0f, 0.0f, 0.0f);
	        
	        /*if (mAppType == AppType.Movie || mAppType == AppType.App || mAppType == AppType.Ebook || mAppType == AppType.Game || mAppType == AppType.Photo || mAppType == AppType.Music) {
	        	modelview.scale(1f, 1.5f, 1f);
	        }*/
	        modelview.scale(aspectDummy, 1.0f, 1.0f);
        	modelview.translate(0f,-0.2f, 0f);
        }
        else if(button.getPosition()  > MIN_ANGLE)
        {	
        	modelview.translate((float)( Math.sin(-(button.getPosition()-180)*Math.PI/180))*4, 
        								0, 
        								-4.5f + (float)Math.sin((button.getPosition()-90 )*Math.PI/180)*4-4);
        	modelview.rotate(button.getPosition()+180, 0.0f, 1.0f, 0.0f);
        	modelview.rotate(9, 1.0f, 0.0f, 0.0f);
        	
        	/*if (mAppType == AppType.Movie || mAppType == AppType.App|| mAppType == AppType.Ebook || mAppType == AppType.Game|| mAppType == AppType.Photo || mAppType == AppType.Music) {
        		modelview.scale(1f, 1.5f, 1f);
        	}*/
        	
        	//Log.d(TAG, "aspect "+aspectDummy);
        	modelview.scale(aspectDummy, 1.0f, 1.0f);
        	modelview.translate(0f,-0.2f, 0f);
        }

		button.getButtonTransform().matrixMultiply(modelview.get(),perspective.get());
	}
	
	/*public List<String> getContentPathPool()
	{
		return mContentPathPool;
	}*/
	

	@Override
	public void onMovedToScrapHeap(OSButton view) {
		
	}

	@Override
	public void onAddActiveView(OSButton view) {
		new Thread(new LoadingBitmapTask(view)).start();
		
	}

	@Override
	public void onRemoveActiveView(OSButton view) {
//		Log.d(TAG, "onRemoveActiveView "+view.getName());
		recylingPhotoButtonBitmap(view);
		//Log.d(TAG, "==========="+b);
		//new Thread(new RecycleBitmapTask(b)).start();
		view.setText(null);
		view.setName(null);
		view.setContentPath(null);
		//view.setTexureID(getDefaultImageTxtID());
		//view.setDimTexId(getDefaultImageDimTxtId());
	}

	// fixed snack view not scrolled smoothly #4092
	// by aaronli Jun21 2013
	private float getLastItemAngle() {
		if (mAdapter != null && !mAdapter.isEmpty()) {
			
			return mFirstButtonAngle + (mAdapter.getCount() - 16 ) * 15;
		}
		return mFirstButtonAngle;
	}
	// fixed end
	
	private float getLastIndexAngle() {
		return mFirstButtonAngle + mLastIndex *15;
	}
	
	private float getCurrentSelectedAngle() {
		return mFirstButtonAngle + mSelectedButtonIdx *15;
	}
	
/*	public String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	*/
/*	private static String getPackageName(OsListViewItem item)
	{
		if(item != null)
		{
			String content = item.getPath();
			return content.substring(0, content.indexOf('/'));
		}
		return null;
	}
	*/
	public int getFirstShowing() {
		return mFirstShowing;
	}



	public int getLastShowing() {
		return mLastShowing;
	}

	/**
	 * @return the mSelectedButtonIdx
	 */
	public int getmSelectedButtonIdx() {
		return mSelectedButtonIdx;
	}




	/**
	 * @return the surefaceRenderCreated
	 */
	boolean isSurefaceRenderCreated() {
		return surefaceRenderCreated;
	}

	/**
	 * @param surefaceRenderCreated the surefaceRenderCreated to set
	 */
	void setSurefaceRenderCreated(boolean surefaceRenderCreated) {
		this.surefaceRenderCreated = surefaceRenderCreated;
	}




	/**
	 * @param mLightTextureIds the mLightTextureIds to set
	 */
	public void setmLightTextureIds(int[] mLightTextureIds) {
		this.mLightTextureIds = mLightTextureIds;
	}

	/**
	 * @return the mLightTextureIds
	 */
	public int[] getmLightTextureIds() {
		return mLightTextureIds;
	}

	/**
	 * @param mNextScrollState the mNextScrollState to set
	 */
	public void setmNextScrollState(short mNextScrollState) {
		this.mNextScrollState = mNextScrollState;
	}




	/**
	 * the runnable running when recycling bitmap
	 * @author aaronli
	 *
	 */
	private class RecycleBitmapTask implements Runnable{
		
		private Bitmap mBitmap;
		
		/**
		 * @param mButton: the {@link OSButton} need to be recyclebitmap
		 */
		public RecycleBitmapTask(Bitmap b) {
			super();
			this.mBitmap = b;
		}

		@Override
		public void run() {
			/*switch (mAppType) {
			case Photo:
			case Music:
			case Movie:
			case Ebook:
				recylingPhotoButtonBitmap(mButton);
				break;
			default:
				break;
			}*/
			
			//modified by aaronli Jun7,2013
			
			//end
			if (mBitmap != null && !mBitmap.isRecycled()) {
				mBitmap.recycle();
			}
		}
		
	}
	
	private class LoadingBitmapTask implements Runnable{
		
		private OSButton mButton;
		
		/**
		 * @param mButton
		 */
		public LoadingBitmapTask(OSButton mButton) {
			super();
			this.mButton = mButton;
		}

		@Override
		public void run() {
			// modified by aaronli Apr9 2013
			onSnackCtrlListener.onLoadingButtonList(mButton);

		}
		
	}
	
	/**
	 * The snack showing state
	 * @author aaronli
	 *
	 */
	public enum SnackCtrlState {
		LEVEL1_TO_4C, LEVEL4, LEVEL4_TO_1A, LEVEL4_TO_1B, LEVEL4_TO_1C
	}

	public enum ActionState {
		Fling, Scroll, Idle
	}

}
