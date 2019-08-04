package com.oregonscientific.meep.meepmusic.opengl;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.media.MediaMetadataRetriever;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.oregonscientific.meep.meepmusic.DataSourceManager;
import com.oregonscientific.meep.meepmusic.MusicPlayerActivity;
import com.oregonscientific.meep.meepmusic.R;
import com.oregonscientific.meep.meepmusic.SongObj;
import com.oregonscientific.meep.meepmusic.SongUiObj;
import com.oregonscientific.meep.meepmusic.SongUiObj.DisplayLevel;
import com.oregonscientific.meep.opengl.ESTransform;
import com.oregonscientific.meep.opengl.MediaManager;

public class MusicUiControl {
	
	private final static String TAG = "MusicUiControl";

	private final int START_ANGLE = 180;
	private final int RIGHT_ANGLE = 180;
	private final int MAX_NUM_OF_DISPLAY_SONG = 5;
	private final float MAX_FLING_TIME = 1.3f;
	private final float TOTAL_DAMP_VALUE = 19.787376f;
	
	private final String PATH_DUMMY_ICON_S = "/mnt/sdcard/home/music/default/cd_cover_s.png";	

	private float mTotalDistance = 0;
	private boolean mIsRepeatable = false;
	
	private String mContentPath = "";

	
	List<SongUiObj> mSongUiList;
	List<SongUiObj> mDisplayList;
	
	private int mSongBgLevel1TxtId = 0;
	private int mSongBgLevel2TxtId = 0;
	private int mSongBgLevel3TxtId = 0;
	
	private final int Y_ADJUSTMENT = 40;
	
	private float mFirstAngle = 180;
	private float mPrevFirstAngle = 180;
	private Context mContext = null;
	
	private float mFlingSpeed= 0;
	private float mFlingTime = 0;
	private boolean mIsNextFlingTime = false;
	private float mMove = 0;
	private boolean musicFilesRended;
	
	private int mSelectedIndex = -1;
	public MusicUiControlListener mOnMusicUiControlListener = null;
	
	ActionState mActionState = ActionState.Idle;
	/*Added by Aaronli at Sep27 2013*/
	private int[] contentRenderIds = new int[14];
	
		
	enum ActionState {
		Scroll, Fling, Idle
	}
	
	interface MusicUiControlListener
	{
		public void onSlectedIndexChanged(int index);
	}
	
	
	
	public MusicUiControl(Context context)
	{
		mContext = context;
		//initSongUiList();		
		
		initDisplayList();
		
	}
	//*******settter******
	public void setOnMusicUiControlListener(MusicUiControlListener musicUiControlListener)
	{
		mOnMusicUiControlListener = musicUiControlListener;
	}
	
	//--------setter-------
	
	public void initSongUiList()
	{
		mSongBgLevel1TxtId = getUiBgLevel1TextId();
		mSongBgLevel2TxtId = getUiBgLevel2TextId();
		mSongBgLevel3TxtId = getUiBgLevel3TextId();
		
		//loadMusicInfo();
	
	}
	
	public void reloadMusicInfo(String[] musicFileList)
	{
		//Log.d("musicPlayer", "reload music info" + Arrays.toString(musicFileList));
		musicFilesRended = false;
		loadMusicInfo(musicFileList);
	
	}
	
	private void initDisplayList() {
		mDisplayList = new ArrayList<SongUiObj>();
		mSongUiList = new ArrayList<SongUiObj>();
	}
		
	public List<SongUiObj> getSongUiList()
	{
		return mSongUiList;
	}
	
	public List<SongUiObj> getDisplaySongList()
	{
		
		if (mSongUiList != null && !mSongUiList.isEmpty()) {
			
			mDisplayList.clear();
			
			for (int i = 0; i < mSongUiList.size(); i++) {
				if (mSongUiList.get(i).getDisplayLevel() == DisplayLevel.LEVEL3) {
					mDisplayList.add(mSongUiList.get(i));
				}
			}
			for (int i = 0; i < mSongUiList.size(); i++) {
				if (mSongUiList.get(i).getDisplayLevel() == DisplayLevel.LEVEL2) {
					mDisplayList.add(mSongUiList.get(i));
				}
			}
			for (int i = 0; i < mSongUiList.size(); i++) {
				if (mSongUiList.get(i).getDisplayLevel() == DisplayLevel.LEVEL1) {
					mDisplayList.add(mSongUiList.get(i));
				}
			}
		}
		
		return mDisplayList;

	}
	
	private SongUiObj getDisaplayObject(int currentPos, int adjustPos) {
		int newPos = currentPos + adjustPos;
		if (newPos >= 0) {
			if (newPos >= mSongUiList.size()) {
				return mSongUiList.get(newPos - mSongUiList.size());
			} else {
				return mSongUiList.get(newPos);
			}
		} else {
			return mSongUiList.get(newPos + mSongUiList.size());
		}
	}
	
	public int findSelectedSongObjectInx() {
		for (int i = 0; i < mSongUiList.size(); i++) {
			if (mSongUiList.get(i).getDisplayLevel() == DisplayLevel.LEVEL1) {
				return i;
			}
		}
		return -1;
	}
	
	public static String getExtension(File f) {
		return (f != null) ? getExtension(f.getName()) : "";
	}

	public static String getExtension(String filename) {
		return getExtension(filename, "");
	}
	
	public static String getExtension(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return defExt;
	}
	
	/*private void loadMusicInfo()
	{
		//String dataFolderPath =   Environment.getExternalStorageDirectory() + File.separator + "home" + File.separator + "music"  + File.separator + "data" + File.separator;
		String dataFolderPath = mContentPath;
		//Log.e("musicplayer", "loadMusicInfo:" + mContentPath);
		File dataFolder = new File(dataFolderPath);
		//Log.e("musicplayer", "dataFolder:" + dataFolder);
		
		if(dataFolder.exists() && dataFolder.isDirectory()){
			String[] tempSongList = dataFolder.list();
			List<String> songFileNameList = new ArrayList<String>();
			for (int i = 0; i < tempSongList.length; i++) {
				if(((MusicPlayerActivity) mContext).getmRunning()  == false) {
					return;
				}
				//2013-7-19 -Amy- use map
				String extension = getExtension(tempSongList[i]).toLowerCase();
				if (DataSourceManager.SUPPORT_MUSIC_FORMAT.containsKey(extension) && !tempSongList[i].startsWith(".")) {
					String path = mContentPath+tempSongList[i];
					boolean fileType = getFileHeader(path);
					if(fileType){
						songFileNameList.add(tempSongList[i]);
					}
					
				}
			}
			for (int i = 0; i < songFileNameList.size(); i++) {		
				if(((MusicPlayerActivity) mContext).getmRunning()  == false) {
					return;
				}
				String fullPath = dataFolderPath + songFileNameList.get(i);
				SongUiObj songUiObj = new SongUiObj();
				songUiObj.setSongObj(loadMetaData(fullPath, songFileNameList.get(i)));
				//argb(50, 51, 77, 104)
				songUiObj.setTitleWhiteTexId(MediaManager.getTitleTextureId(songUiObj.getSongObj().getTitle(), 24, Color.WHITE, Align.LEFT));
				songUiObj.setTitleBlackTexId(MediaManager.getTitleTextureId(songUiObj.getSongObj().getTitle(), 24, Color.argb(255, 51, 77, 104),  Align.LEFT));
				songUiObj.setAuthorWhiteTexId(MediaManager.getTitleTextureId(songUiObj.getSongObj().getAuthur(), 16, Color.WHITE,  Align.LEFT));
				songUiObj.setAuthorBlackTexId(MediaManager.getTitleTextureId(songUiObj.getSongObj().getAuthur(), 16, Color.GRAY,  Align.LEFT));

				songUiObj.setPosition(START_ANGLE - i * 20);
				mSongUiList.add(songUiObj);
			}
			setFirstAngle(RIGHT_ANGLE);
		}
	}*/
	
	private void loadMusicInfo(String[] songFileNameList)
	{
		//String dataFolderPath =   Environment.getExternalStorageDirectory() + File.separator + "home" + File.separator + "music"  + File.separator + "data" + File.separator;
		String dataFolderPath = mContentPath;
		//Log.e("musicplayer", "loadMusicInfo:" + mContentPath);
		File dataFolder = new File(dataFolderPath);
		//Log.e("musicplayer", "dataFolder:" + dataFolder);
		
		if(dataFolder.exists() && dataFolder.isDirectory()){
			for (int i = 0, size = songFileNameList.length; i < size; i++) {		
				if(((MusicPlayerActivity) mContext).getmRunning()  == false) {
					return;
				}
				String fullPath = songFileNameList[i];
				//Log.d("musicplayer", "loadMusicInfo fullPath"+fullPath);
				SongUiObj songUiObj = new SongUiObj();
				songUiObj.setSongObj(loadMetaData(fullPath, DataSourceManager.getFileNameWithoutExten(fullPath)));
				//argb(50, 51, 77, 104)
				/*
				 * int id = MediaManager.getTitleTextureId(songUiObj.getSongObj().getTitle(), 24, Color.WHITE, Align.LEFT);
				//Log.d("music player", "getTitleTextureId 1 "+ id);
				songUiObj.setTitleWhiteTexId(id);
				id = MediaManager.getTitleTextureId(songUiObj.getSongObj().getTitle(), 24, Color.argb(255, 51, 77, 104),  Align.LEFT);
				//Log.d("music player", "getTitleTextureId 2 "+ id);
				songUiObj.setTitleBlackTexId(id);
				id = MediaManager.getTitleTextureId(songUiObj.getSongObj().getAuthur(), 16, Color.WHITE,  Align.LEFT);
				//Log.d("music player", "getTitleTextureId 3 "+ id);
				songUiObj.setAuthorWhiteTexId(id);
				id = MediaManager.getTitleTextureId(songUiObj.getSongObj().getAuthur(), 16, Color.GRAY,  Align.LEFT);
				//Log.d("music player", "getTitleTextureId 4 "+ id);
				songUiObj.setAuthorBlackTexId(id);
				*/
				songUiObj.setTitleWhiteTexId(contentRenderIds[i % (contentRenderIds.length/2)]);
				songUiObj.setAuthorWhiteTexId(contentRenderIds[i % (contentRenderIds.length/2) + (contentRenderIds.length/2)]);
				songUiObj.setPosition(START_ANGLE - i * 20);
				mSongUiList.add(songUiObj);
			}
			setFirstAngle(RIGHT_ANGLE);
			musicFilesRended = true;
		}
/*		if (mOnMusicUiControlListener != null) {
			mOnMusicUiControlListener.
		}*/
	}
	
	public int bindTitleTextureId(SongUiObj obj)
    {
		if (obj.getDisplayLevel() == DisplayLevel.LEVEL1) {
			Reference<Bitmap> reference = obj.getWhiteImage();
			Bitmap b;
			if (reference == null || (b = reference.get()) == null || b.isRecycled()) {
				b = createSongBitmap(obj, DisplayLevel.LEVEL1);
				reference = new SoftReference<Bitmap>(b);
				obj.setWhiteImage(reference);
				//MediaManager.saveImageToExternal(b, "/mnt/sdcard/5.png");
			}
			meepGlTexBitmap(obj.getmTextureId(), b);
		} else {
			Reference<Bitmap> reference = obj.getBlackImage();
			Bitmap b;
			if (reference == null || (b = reference.get()) == null || b.isRecycled()) {
				b = createSongBitmap(obj, obj.getDisplayLevel());
				reference = new SoftReference<Bitmap>(b);
				obj.setBlackImage(reference);
				//MediaManager.saveImageToExternal(b, "/mnt/sdcard/4.png");
			}
			meepGlTexBitmap(obj.getmTextureId(), b);
		}
		
    	return obj.getmTextureId();
    }
	
	public int bindTitleTextureId(String text,int textureId, int fontSize, int color, Align align)
    {
    	return bindTextureIdByText(text,textureId, fontSize, color, 550, 50,0,35, align);
    }
	
	private int bindTextureIdByText(String text, int textureId, int fontSize, int color, int w, int h, int x, int y,Align align )
    {
    	//long time1 = System.currentTimeMillis();
    	
    	// Create an empty, mutable bitmap
    	Bitmap bitmap = createSongBitmap(text, fontSize, color, w, h, x, y,
				align);

		meepGlTexBitmap(textureId, bitmap);
		MediaManager.saveImageToExternal(bitmap, "/mnt/sdcard/5.png");
		//Log.d("getTextTextureId", "save img");

    	//Clean up
    	//bitmap.recycle();
    	
    	 //long timediff = System.currentTimeMillis() - time1;
         
        // Log.d("performance", "load texture(draw text):" + timediff);
    	
    	return textureId;
    }
	
	private Bitmap createSongBitmap(SongUiObj obj, DisplayLevel level) {
		Bitmap bmp ;
		int titleFontSize;
		int authorFontSize;
		int titleColor;
		int authorColor;
		boolean titleBold;
		boolean authorBold;
		int titleX, titleY;
		int authorX, authorY;
		if (level == DisplayLevel.LEVEL1) {
			
			bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player_highline).copy(Bitmap.Config.ARGB_8888, true);
			titleFontSize = 20;
			authorFontSize = 14;
			titleColor = Color.WHITE;
			authorColor = Color.WHITE;
			titleBold = true;
			authorBold = true;
			titleX = 100;
			titleY = 35;
			authorX = 100;
			authorY = 65;
		} else {
			bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player_highlight).copy(Bitmap.Config.ARGB_8888, true);
			titleFontSize = 20;
			authorFontSize = 14;
			titleColor = Color.argb(255, 51, 77, 104);
			authorColor = Color.GRAY;
			titleBold = true;
			authorBold = true;
			titleX = 80;
			titleY = 35;
			authorX = 80;
			authorY = 60;
		}
		Canvas canvas = new Canvas(bmp);
		
		// draw title font 
		drawSongFont(canvas, obj.getSongObj().getTitle(), titleFontSize, titleColor, titleBold, titleX, titleY);
    	// draw author font
		drawSongFont(canvas, obj.getSongObj().getAuthor(), authorFontSize, authorColor, authorBold, authorX, authorY);
		return bmp;
	}
	
	private void drawSongFont(Canvas canvas, String title, int textSize, int color, boolean bold, float x, float y) {
		Paint textPaint = new Paint();
		textPaint.setTextSize(textSize);
    	textPaint.setAntiAlias(true);
    	textPaint.setTextAlign(Align.LEFT);
    	textPaint.setFakeBoldText(bold);
    	textPaint.setColor(color);
    	//Log.d(TAG, "drawSongFont "+title);
    	canvas.drawText(title, x, y, textPaint);
	}
	
	private Bitmap createSongBitmap(String text, int fontSize, int color,
			int w, int h, int x, int y, Align align) {
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    	// get a canvas to paint over the bitmap
    	Canvas canvas = new Canvas(bitmap);
    	bitmap.eraseColor(0);

    	// Draw the text
    	Paint textPaint = new Paint();
    	textPaint.setTextSize(fontSize);
    	textPaint.setAntiAlias(true);
    	textPaint.setTextAlign(align);
    	textPaint.setFakeBoldText(true);
    	//textPaint.setARGB(0xff, 0xff, 0x00, 0x00);
    	textPaint.setColor(color);
    	// deleted by aaronli Jul8 2013/ removed the shadow of titles
    	//textPaint.setShadowLayer(5f, 2f, 2f, Color.argb(180, 0, 0, 0));
    	
    	// draw the text centered
		canvas.drawText(text, x, y, textPaint);
		return bitmap;
	}
	
	
	private void meepGlTexBitmap(int textureId, Bitmap bitmap) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
	}
	
	/*//2013-7-2 - Zoya - fixed music player will be stopped after play an invalid MP3 format file.
	public static boolean getFileHeader(String filePath) {
		boolean type = false;
		FileInputStream is = null;
		StringBuilder value = null;
		StringBuilder sum = new StringBuilder() ;
		try {
			is = new FileInputStream(filePath);
			int c = 0; 
			boolean startBit = false;
			while(c!=-1){
				byte[] b = new byte[3];
				c = is.read(b, 0, b.length);
				value = bytesToHexString(b);
				if (value.toString().contains("FFFB")){
					startBit = true;
				}
				if(startBit) {
					sum.append(value);
				}
				if(sum.toString().contains("FFFB")){
					type=true;
					break;
				}
			}
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}

			}

		}
		return type;
	}*/

	private static StringBuilder bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder;
	}
	
	private SongObj loadMetaData(String filePath, String name)
    {
		SongObj song = new SongObj();
		
    	MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		/*try {
			mmr.setDataSource(filePath);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		if(album != null){
			song.setAlbum(album);
		}*/
		
		String authur = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		if(authur != null)
		{
			song.setAuthor(authur);
		}
		else{
			song.setAuthor("Unknown Authur");
		}
		String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		if(title != null)
		{
			song.setTitle(title);
		}
		else{
			song.setTitle(name);
		}
		
    	song.setPath(filePath);
    	/*byte[] art = mmr.getEmbeddedPicture();
    	Bitmap bitmap = null;
    	if(art == null)
    	{
    		bitmap = BitmapFactory.decodeFile(PATH_DUMMY_ICON_S);
    		if(bitmap == null)
    		{
    			Log.w("loadmusicicon", " bitmap is null");
    		}
    	}
    	else
    	{
    		bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
    	}
    	if(bitmap!=null)
    	{
	    	Matrix mx = new Matrix(); 
	    	float scalef = 48f/bitmap.getHeight();
	    	mx.postScale(scalef, scalef);
	    	Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mx , false);
	    	song.setConverImage(bitmap2);
	    	bitmap.recycle();
    	}*/
    	return song;
    	
    }

	
	private int getUiBgLevel1TextId()
	{
//		String path = Environment.getExternalStorageDirectory() + File.separator + 
//						"home" + File.separator + "music" + File.separator + "music_song_bg.png";
		try {
			//Bitmap bmp = MediaManager.LoadBitmapFile(path);
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player_highline);
			int id =  MediaManager.loadTexture(bmp);
			bmp.recycle();
			return id;
		} catch (Exception e) {
			Log.e("getUiBgTextId", "music - load background error" + e.toString());
		}
		return 0;
	}
	
	private int getUiBgLevel2TextId()
	{
		try {
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player_highlight);
			int id = MediaManager.loadTexture(bmp);
			bmp.recycle();
			return id;
		} catch (Exception e) {
			Log.e("getUiBgTextId", "music - load background error" + e.toString());
		}
		return 0;
	}
	
	private int getUiBgLevel3TextId()
	{
		try {
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player_highlight);
			int id = MediaManager.loadTexture(bmp);
			bmp.recycle();
			return id;
		} catch (Exception e) {
			Log.e("getUiBgTextId", "music - load background error" + e.toString());
		}
		return 0;
	}
	
	

	public int getSongBgLevel1TxtId() {
		return mSongBgLevel1TxtId;
	}

	public int getSongBgLevel2TxtId() {
		return mSongBgLevel2TxtId;
	}

	public int getSongBgLevel3TxtId() {
		return mSongBgLevel3TxtId;
	}
	
	
	
	public void setFirstAngle(float firstAngle)
	{
		if(firstAngle<180)
		{
			return;
		}
		else if (firstAngle>180+((mSongUiList.size()-1)*20))
		{
			return;
		}
		mFirstAngle = firstAngle;
		applyFirstAngle(firstAngle);
	}
	
	public float getFirstAngle()
	{
		return mFirstAngle;
	}
	
	public ActionState getActionState()
	{
		return mActionState;
	}
	public void setActionState(ActionState actionState)
	{
		mActionState = actionState;
	}
	
	public void setContentPath(String contentPath)
	{
		mContentPath = contentPath;
	}
	//------------end get/set-----------
	
	private void applyFirstAngle(float angle)
	{
		//Log.d("applyfirstAngle", "apply angle:" + angle);
		float diff = angle -  mPrevFirstAngle;
		mPrevFirstAngle = angle;
		if (mSongUiList != null && !mSongUiList.isEmpty()) {
			
			for(int i = 0; i< mSongUiList.size(); i++)
			{
				SongUiObj item = mSongUiList.get(i);
				
				item.setPosition(item.getPosition() + diff);
				
				if (item.getPosition() >= 170 && item.getPosition() < 190) {
					item.setDisplayLevel(DisplayLevel.LEVEL1);
					if(mSelectedIndex!=i)
					{
						mSelectedIndex = i;
						//select index changed;
						/*if(mOnMusicUiControlListener!= null)
						{
							mOnMusicUiControlListener.onSlectedIndexChanged(mSelectedIndex);
						}*/
					}
					item.setBgId(getSongBgLevel1TxtId());
					//Log.e("applyfirstAngle", "apply level 1 angle");
				} else if ((item.getPosition() >= 150 && item.getPosition() < 170) || (item.getPosition() >= 190 && item.getPosition() < 210)) {
					item.setDisplayLevel(DisplayLevel.LEVEL2);
					item.setBgId(getSongBgLevel2TxtId());
					//Log.e("applyfirstAngle", "apply level 2 angle");
				} else if ((item.getPosition() >= 130 && item.getPosition() < 150) || (item.getPosition() >= 210 && item.getPosition() < 230)) {
					item.setDisplayLevel(DisplayLevel.LEVEL3);
					item.setBgId(getSongBgLevel3TxtId());
					//Log.e("applyfirstAngle", "apply level 3 angle");
				} else {
					item.setDisplayLevel(DisplayLevel.LEVEL4);
					//Log.e("applyfirstAngle", "apply level 4 angle");
				}	
			}	
		}
	}
	
	
	
	public void functionButtonShowup(ESTransform transform, float aspect, float angle) {
		
		//Log.d("angle", "angle:" + angle);
		
		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();

		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();
		perspective.perspective(60.0f, aspect, 1.0f, 60.0f);

		modelview.matrixLoadIdentity();
		modelview.scale(2, 0.25f, 1);
		//modelview.translate(0, 0, -2f);

		float x = (float) (Math.cos(angle * 3.1417 / 180)) * 0.2f;
		float y = (float) (Math.sin(angle * 3.1417 / 180)) * -3f;
		float z = (float) (Math.cos(angle * 3.1417 / 180)) * -1.5f -2.8f ;
		modelview.translate(0,y-0.45f,z);
		modelview.scale(1-Math.abs(180-angle)/180, 1-Math.abs(180-angle)/180, 1);
		
		//Log.d("cor", "cor x:" + x + " y:" + y + " z:" + z);
		
		
		transform.matrixMultiply(modelview.get(), perspective.get());
	}
	
	public void showTitle(ESTransform transform, float aspect, float angle) {
		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();

		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();
		perspective.perspective(60.0f, aspect, 1.0f, 60.0f);

		modelview.matrixLoadIdentity();
		//modelview.scale(1, 1f, 1);
		modelview.scale(2f, 0.25f, 1);
		modelview.translate(0f, 0.3f-1f, -2f);
		
		float y = (float) (Math.sin(angle * 3.1417 / 180)) * -6.8f;
		float z = (float) (Math.cos(angle * 3.1417 / 180)) * -1.5f -2.8f ;
		modelview.translate(-0.1f,y,z);
		float scalef= (1-Math.abs(180-angle)/180)*1.4f;
		modelview.scale(scalef,scalef, 1);

		transform.matrixMultiply(modelview.get(), perspective.get());
	}
	
	public void showAuthur(ESTransform transform, float aspect, float angle) {
		ESTransform perspective = new ESTransform();
		ESTransform modelview = new ESTransform();

		// Generate a perspective matrix with a 60 degree FOV
		perspective.matrixLoadIdentity();
		perspective.perspective(60.0f, aspect, 1.0f, 60.0f);

		modelview.matrixLoadIdentity();
		//modelview.scale(1, 1f, 1);
		modelview.scale(2f, 0.25f, 1);
		modelview.translate(0f, -0.4f-1f, -2f);
		
		float y = (float) (Math.sin(angle * 3.1417 / 180)) * -6.5f;
		float z = (float) (Math.cos(angle * 3.1417 / 180)) * -1.5f -2.8f ;
		modelview.translate(-0.1f,y,z);
		float scalef= (1-Math.abs(180-angle)/180)*1.4f;
		modelview.scale(scalef,scalef, 1);

		transform.matrixMultiply(modelview.get(), perspective.get());
	}
	
	public void SetFling()
	{
		if (mFlingTime<MAX_FLING_TIME && mFlingTime != 0) {
			if (mIsNextFlingTime) {
				float dampValue = getDampValue(1, mFlingTime, 0.8f, 2.4f);
				mMove = (dampValue / TOTAL_DAMP_VALUE) * mTotalDistance;
		//		Log.e("setButtonAngle", "setButtonAngleS: "+ mFirstAngle + "move " + mMove + " flingtime:" + mFlingTime );
				setFirstAngle(mFirstAngle + mMove);
				mIsNextFlingTime = false;
			}
		} 
		else
		{
			//Log.d(TAG, "change action state to idle");
			
			if(mActionState == ActionState.Fling && mOnMusicUiControlListener!= null)
			{
				mOnMusicUiControlListener.onSlectedIndexChanged(mSelectedIndex);
			}
			mActionState = ActionState.Idle;
		}
	}
	
	public void setScroll(float move)
	{
		move = move/10;
		setFirstAngle(getFirstAngle() + move);
	}
	
	public void setFlingSpeed(float speed)
	{
		// fixed #4090.added by aaronli at Jul9 2013
		// the songlist should scroll faster but not too fast.
		if (speed > 1000) {
			speed = 1000;
		}
		if (speed < - 1000) {
			speed = -1000;
		}
		// added end
		mFlingSpeed = speed/500f;
		setFlingDistance(getFlingDistance());
	}
	
	public void setFlingDistance(float distance)
	{
		resetNextFlingTime();
		mTotalDistance = distance;
		//Log.d("setFlingDistance", "setFlingDistance : " + distance);
	}
	
	public void flingToIndex() {
		
	}
	
	private float getFlingDistance()
	{
		float tempTotalDistance = TOTAL_DAMP_VALUE * mFlingSpeed;
		int numOfItems = (int)(Math.abs(tempTotalDistance)/20);
		//Log.d(this.getClass().getSimpleName(),"getFlingDistance speed= " + mFlingSpeed + " number of item:" + numOfItems);
		
		//control the movie not to move outside the boundry
		if (mFlingSpeed > 0) {
			//float maxDistance =  mFirstAngle -180-((mSongUiList.size()-1)*20) ;
			float maxDistance =  180+((mSongUiList.size()-1)*20) - mFirstAngle;
			//Log.d("getFlingDistance", "getFlingDistance tempTotalDistance path 1:" + tempTotalDistance + " max distance:" + maxDistance + " mangle:"
			//		+ mFirstAngle);
			if (tempTotalDistance > maxDistance) {
			//	Log.d("getFlingDistance", "getFlingDistance path 1 :" + maxDistance);
				return maxDistance;
			}
		} else {
			float maxDistance = 180 - mFirstAngle;
			//Log.d("getFlingDistance", "getFlingDistance tempTotalDistance path 2:" + tempTotalDistance + " max distance:" + maxDistance + " mangle:"
			//		+ mFirstAngle + " lastIdx:" );
			if (tempTotalDistance < maxDistance) {
				//Log.d("getFlingDistance", "getFlingDistance path 2:" + maxDistance);
				return maxDistance;
			}
		}
		
		if (numOfItems != 0) {
			float flingDistance = 0;
			//Log.d("getFlingDistance", "getFlingDistance tempTotalDistance" + tempTotalDistance);
			if (tempTotalDistance > 0) {
				//Log.d("getFlingDistance", "getFlingDistance path 3");
				flingDistance = numOfItems * 20 - mFirstAngle % (20);
				//Log.d("Total", "getFlingDistance flingDistance > 0 --:" + flingDistance);
			} else {
				//Log.d("getFlingDistance", "getFlingDistance path 4 ( " + (-numOfItems * 5) + " - " +  mFirstAngle % (5) + ")");
				flingDistance = -(numOfItems) * 20 -mFirstAngle % (20);
				//Log.d("Total", "getFlingDistance flingDistance < 0 --:" + flingDistance);
			}
			return flingDistance;
		}
		else {
			float flingDistance = 0;
			float remainMove = mFirstAngle% 20 + 20;
			float flingMove =  tempTotalDistance % 20;
			if (flingMove < 0) {
				flingMove += 20;
			}
			float movePos = remainMove + flingMove;
			//Log.d("getFlingDistance", "getFlingDistance path 5/6 movePos: " +remainMove + " + " +flingMove + " = " +movePos);
			
			if(tempTotalDistance>0)
			{
				if(movePos>10){
					//Log.d("getFlingDistance", "getFlingDistance  5.1");
					flingDistance = 20 - remainMove;
				}
				else{
					//Log.d("getFlingDistance", "getFlingDistance  5.2");
					flingDistance =  remainMove;
				}
			}
			else
			{
				if(movePos>10){
					Log.d("getFlingDistance", "getFlingDistance  6.1");
					flingDistance = - remainMove;
				}
				else{
					Log.d("getFlingDistance", "getFlingDistance  6.2");
					flingDistance =  -20 - remainMove;
				}
			}
			Log.d("getFlingDistance", "getFlingDistance  flingdistance: " + flingDistance);
			
			return flingDistance;

		}
		//return 0;
	}
	
	public void setNextFlingTime() {
		mFlingTime += 0.02;
		mIsNextFlingTime = true;
	}

	public void resetNextFlingTime() {
		mFlingTime = 0;
	}

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
    
	
	public void printItems()
	{
		for(int i = 0; i< mSongUiList.size(); i++)
		{
			SongUiObj item = mSongUiList.get(i);
			Log.d("debug_music", "name:" + item.getSongObj().getTitle() + " pos:" + item.getPosition() + " kevel:" + item.getDisplayLevel().toString());
		}
	}
	public boolean IsRepeatable() {
		return mIsRepeatable;
	}
	public void setIsRepeatable(boolean isRepeatable) {
		this.mIsRepeatable = isRepeatable;
	}
	/**
	 * @return the musicFilesRended
	 */
	public boolean isMusicFilesRended() {
		return musicFilesRended;
	}
	/**
	 * @return the contentRenderIds
	 */
	public int[] getContentRenderIds() {
		return contentRenderIds;
	}
			
}
