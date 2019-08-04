package com.oregonscientific.meep.movie;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.global.Global;

public class MoviePlayerActivity extends Activity {
	
	private final static String TAG = "MoviePlayerActivity"; 
	
	int maxVolume;
	int volumeLevel;
	int currentVolume;
	private String EMPTY_MOVIE_PATH = "Movie path is empty";
	// Copied from MediaPlaybackService in the Music Player app.
    private static final String SERVICECMD = "com.android.music.musicservicecommand";
    private static final String CMDNAME = "command";
    private static final String CMDPAUSE = "pause";

	TextView mTitleName;
	RelativeLayout mControlLayout = null;

	private boolean mIsLeftMenuEnable = false;
	private boolean mIsRightMenuEnable = false;

	AudioManager mAudioMgr = null;

	VideoView mVideo;
	int mVideoLength = 0;

	boolean mIsMute = false;
	boolean mIsMenuShown = false;
	boolean mIsPlayCompleted = false;

	RelativeLayout mMainLayout = null;
	Handler mHandler = null;
	MyThread mThread = null;
	//private boolean mEnableTimeInfoThread = false;

	private final int Y_ADJUSTMENT = 40;

	private CountDownTimer mCountDownTimer = null;

	// left control
	private ImageView mLeftImgViewBackBar = null;
	private ImageView mLeftImgViewFrontBar = null;
	private ImageView mLeftImgViewBase = null;
	private ImageView mLeftBtnVolumeIdx = null;
	private ImageView mLeftBtnVolume = null;

	private Bitmap mBmapLeftBase = null;
	private Bitmap mBmapLeftBackBar = null;
	private Bitmap mBmapLeftFrontBar = null;
	private Bitmap mBmapLeftBtnVolumeIdx = null;
	private Bitmap mBmapLeftBtnVolumeOn = null;
	private Bitmap mBmapLeftBtnVolumeMute = null;


	// right control
	private ImageView mRightImgViewBackBar = null;
	private ImageView mRightImgViewFrontBar = null;
	private ImageView mRightImgViewBase = null;
	private ImageView mRightBtnProgressIdx = null;
	private ImageView mRightBtnPlay = null;

	private Bitmap mBmapRightBase = null;
	private Bitmap mBmapRightBackBar = null;
	private Bitmap mBmapRightFrontBar = null;
	private Bitmap mBmapRightBtnProgressIdx = null;
	private Bitmap mBmapRightBtnPlay = null;
	private Bitmap mBmapRightBtnPause = null;

	private TextView mTxtCurrTime = null;
	private TextView mTxtTotalTime = null;

	// screen off
	PowerManager mPowerManager = null;
	WakeLock mWakeLock = null;
	String mMoviePath = null;

	// 2013-03-26 - raymond - android has bug on mute. we use volum instead
	// int currentVolume;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movieplayer);
		Intent i = new Intent(SERVICECMD);
        i.putExtra(CMDNAME, CMDPAUSE);
        sendBroadcast(i);
		BroadcastReceiver myReceiver = new BroadcastReceiver() {  
			  
	        @Override  
	        public void onReceive(Context context, Intent intent) {
	        	int volume = intent.getIntExtra("volume", 0);
	        	//int volume1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	        	
	        	
				VolumeControl(volume,150);
	        	
	        	
				/*if (volume==0)
				{
					mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute);
				}
				else {
					mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
				}*/
	        }  
	    };  
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction("com.oregonscientific.meep.VolumeChanged");
			//intentFilter.setPriority(Integer.MAX_VALUE);  
		    registerReceiver(myReceiver, intentFilter);
		try
		{
			//Log.d("external", Environment.getExternalStorageDirectory().getAbsolutePath());
			// main layout
			mMainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

			// video
			mVideo = (VideoView) findViewById(R.id.videoView1);

			// title
			mTitleName = (TextView) findViewById(R.id.textViewTitleName);

			// System.out.println("aaaaaaaaaaaaaaaa");
			initLeftMenu();
			initRightMenu();
			initController();
			
			initProgressCaptureThread();
			
			hideMenu();
			mLeftBtnVolume = (ImageView) findViewById(R.id.left_btn_volume);
			//mLeftBtnVolume.setOnClickListener(listener);
			mRightBtnPlay = (ImageView) findViewById(R.id.right_btn_play);
			mRightBtnPlay.setOnClickListener(listener);
			// winder hao If the current volume to 0, the default muted
			/*
			 * if (volumeLevel==0) {
			 * mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute); }
			 */
			mTitleName.setVisibility(View.GONE);

			String moviePath = getIntent().getStringExtra(Global.STRING_MOVIE_PATH);
			mMoviePath = moviePath;
			// moviePath = "/mnt/sdcard/home/movie/data/movie01.mp4";
			if (moviePath == null || moviePath == "")
			{
				Uri uri = getIntent().getData();
				if (uri != null)
				{
					String selectedFilePath = null;
					//Log.d("test",uri.getScheme());
					//Log.d("test",uri.getPath());
					if(uri.getScheme().equals("file"))
					{
						//uri: contains absolute file path 
						selectedFilePath = uri.getPath();
					}
					else
					{
						//content uri
						selectedFilePath = getRealPathFromURI(uri);
					}
					moviePath = selectedFilePath;
					mMoviePath = selectedFilePath;
				} else
				{

					//Toast.makeText(this, EMPTY_MOVIE_PATH, Toast.LENGTH_LONG);
					//Log.d("movieplayer", "empty movie path");
				}
				// finish();
			} else
			{
				File file = new File(moviePath);
				mTitleName.setText(file.getName().substring(0, file.getName().length() - 4));
				//Log.d("movieplayer", "movie path:" + moviePath);
			}

			// 2013-3-21 -Amy- update bug#2913 :The popup can not be dismissed
			mVideo.setOnErrorListener(mErrorListener);
			// 锟叫讹拷锟斤拷锟斤拷锟街凤拷锟斤拷募锟斤拷锟绞斤拷欠锟斤拷锟饺凤拷锟�			
			Bitmap mBitmap = ThumbnailUtils.createVideoThumbnail(moviePath, Thumbnails.MINI_KIND);
			if (mBitmap != null)
			{
				mVideo.setVideoPath(moviePath);
				mBitmap.recycle();
			} else
			{
				int messageId = android.R.string.VideoView_error_text_unknown;
				new AlertDialog.Builder(MoviePlayerActivity.this)
						.setTitle(android.R.string.VideoView_error_title)
						.setMessage(messageId)
						.setPositiveButton(android.R.string.VideoView_error_button,
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int whichButton) {
										/*
										 * If we get here, there is no onError
										 * listener, so at least inform them
										 * that the video is over.
										 */
										finish();
									}
								}).setCancelable(false).show();
			}

			mVideo.setOnTouchListener(new View.OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// if (mTitleName.isShown() && event.getX()>100 &&
					// event.getX()<700) {
					// hideMenu();
					// stopTimer();
					// } else {
					// showMenu();
					// startTimer();
					// }
					return false;
				}
			});
			mVideo.setLongClickable(true);
			//Log.d("VideoActivity", "VideoActivity -> play movie:" + moviePath);
			// mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);

			initScreenOff();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	// 2013-3-21 -Amy- update bug#2913 :The popup can not be dismissed
	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener()
	{
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			int messageId;
			/*
			 * Bitmap mBitmap= ThumbnailUtils.createVideoThumbnail(mMoviePath,
			 * Thumbnails.MINI_KIND); ||mBitmap==null
			 * System.out.println(mBitmap==null);
			 * System.out.println(mMoviePath+"锟侥硷拷路锟斤拷");
			 */
			if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK)
			{

				messageId = android.R.string.VideoView_error_text_invalid_progressive_playback;
			} else
			{
				messageId = android.R.string.VideoView_error_text_unknown;
			}
			new AlertDialog.Builder(MoviePlayerActivity.this).setTitle(android.R.string.VideoView_error_title)
					.setMessage(messageId)
					.setPositiveButton(android.R.string.VideoView_error_button, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int whichButton) {
							/*
							 * If we get here, there is no onError listener, so
							 * at least inform them that the video is over.
							 */
							finish();
						}
					}).setCancelable(false).show();
			return true;
		}
	};

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onStart() {
		mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);

		mWakeLock.acquire();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mWakeLock.release();

		//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		// deleted by aaronli at Jun21 2013
		/*
		 * try { mThread.join(1000); } catch (InterruptedException e) {
		 * Log.e("moviePlayer", "cannot stop thread:" + e.toString());
		 * e.printStackTrace(); }
		 */
		super.onStop();
	}
	
	// added by aaronli Jun21 2013. start progressCapture three when onresume
	// and stop when on pause
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		//int volumeLevel1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		mIsRightMenuEnable = false;

		//initProgressCaptureThread();
		//mThread.mEnableTimeInfoThread = true;
		super.onResume();
	}

	// added end

	@Override
	protected void onPause() {
		int volumeLevel1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		mIsRightMenuEnable = false;

		mVideo.pause();
		mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);

		/*
		 * if (mIsMute) { mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,
		 * 0, 0); } else {
		 */
		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel1, 0); // tempVolume:锟斤拷锟斤拷锟斤拷锟街�		// }
		// modified by aaronli Jun21 2013. start progressCapture three when
		// onresume
		// and stop when on pause
		
		// modified end
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		mThread.mEnableTimeInfoThread = false;

		super.onDestroy();
	}

	private void initScreenOff() {
		mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
	}
	//static int volumeLevel1 ;
	/*int volumeLevel1;
	private void muteSeting(){
		 //volumeLevel = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		int  volumeLevel2 ; 
		//mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel1, 0);
  
		mIsMute = !mIsMute;
		if (!mIsMute){
			

			mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
			//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);

			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel1, 0);
		} else
	
		{

			Log.v("!mIsMute", "!mIsMute锟斤拷锟斤拷状态");
		  volumeLevel2 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		  volumeLevel1 = volumeLevel2;
			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, true);

			mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute);

		}
		
	}*/
	// 2013-04-07 Winder Hao Integration of listening to events
	OnClickListener listener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			/*case R.id.left_btn_volume:
				int volumeLevel1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

				muteSeting();
				mIsMute = !mIsMute;
				if (!mIsMute)
				{
					mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
					mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
					int volumeLevel1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

					mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel1, 0);
				} else
				{

					Log.v("!mIsMute", "!mIsMute锟斤拷2锟斤拷执锟斤拷");
					int volumeLevel1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

					mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel1, 0);
					mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, true);

					mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute);

				}

				break;*/
			case R.id.right_btn_play:
				if (mVideo.isPlaying())
				{
					mVideo.pause();
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
				} else
				{

					mVideo.start();
					mIsPlayCompleted = false;
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
				}
				break;

			default:
				break;
			}

		}
	};

	public void initLeftMenu() {

		// left control
		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volumeLevel = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

		// set audio un-mute
		//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		mIsMute = false;

		// mBmapLeftBase = BitmapFactory.decodeFile(LEFT_BASE_BG_PATH);
		// mBmapLeftBackBar = BitmapFactory.decodeFile(LEFT_BACK_BAR_IMG_PATH);
		// mBmapLeftFrontBar =
		// BitmapFactory.decodeFile(LEFT_FRONT_BAR_IMG_PATH);
		// mBmapLeftBtnVolumeIdx = BitmapFactory.decodeFile(LEFT_PLAY_IMG_PATH);
		// mBmapLeftBtnVolumeOn = BitmapFactory.decodeFile(LEFT_VOLUME_ON);
		// mBmapLeftBtnVolumeMute = BitmapFactory.decodeFile(LEFT_VOLUME_MUTED);

		mBmapLeftBase = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_base_left);
		mBmapLeftBackBar = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_blue_left);
		mBmapLeftFrontBar = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_white_left);
		mBmapLeftBtnVolumeIdx = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_button);
		mBmapLeftBtnVolumeOn = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_volume_on);
		mBmapLeftBtnVolumeMute = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_volume_muted);

		mLeftImgViewBase = new ImageView(this);
		mLeftImgViewBackBar = new ImageView(this);
		mLeftImgViewFrontBar = new ImageView(this);
		mLeftBtnVolumeIdx = new ImageView(this);
		mLeftBtnVolume = new ImageView(this);
		// 2013-04-07 WinderHao
		mLeftBtnVolume.setId(R.id.left_btn_volume);
		// mLeftImgViewBase.setY(117);
		// mLeftImgViewBackBar.setX(-5);
		// mLeftImgViewBackBar.setY(128);
		// mLeftImgViewFrontBar.setX(-94);
		// mLeftImgViewFrontBar.setY(131);
		// mLeftBtnVolume.setX(7);
		// mLeftBtnVolume.setY(203);

		mLeftImgViewBase.setX(-10);
		mLeftImgViewBase.setY(117 + Y_ADJUSTMENT);
		mLeftImgViewBackBar.setX(-10);
		mLeftImgViewBackBar.setY(128 + Y_ADJUSTMENT);
		mLeftImgViewFrontBar.setX(-102);
		mLeftImgViewFrontBar.setY(128 + Y_ADJUSTMENT);
		mLeftBtnVolume.setX(7);
		
		mLeftBtnVolume.setY(203 + Y_ADJUSTMENT);

		// get volume and display
		angle = (int) (270 + (1 - volumeLevel / (float) maxVolume) * 180 * 0.98f);
		System.out.println(angle + "--------------------");
		Matrix m = new Matrix();
		m.setRotate(angle);
		Bitmap bmap = Bitmap.createBitmap(mBmapLeftBtnVolumeIdx, 0, 0, mBmapLeftBtnVolumeIdx.getWidth(),
				mBmapLeftBtnVolumeIdx.getHeight(), m, true);

		int x = -10 + (int) (90f * Math.cos(angle * 3.1416f / 180f));
		int y = 222 + (int) (90f * Math.sin(angle * 3.1416f / 180f)) + Y_ADJUSTMENT;

		mLeftImgViewFrontBar.setRotation(270 + angle);
		mLeftBtnVolumeIdx.setImageBitmap(bmap);
		mLeftBtnVolumeIdx.setX(x - (int) (bmap.getWidth() / 2f));
		mLeftBtnVolumeIdx.setY(y - (int) (bmap.getHeight() / 2f));
		mLeftBtnVolumeIdx.setOnTouchListener(new View.OnTouchListener()
		{

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					// mIsLeftMenuEnable = true;
				}
				return false;
			}
		});

		mLeftImgViewBase.setImageBitmap(mBmapLeftBase);
		mLeftImgViewBackBar.setImageBitmap(mBmapLeftBackBar);
		mLeftImgViewFrontBar.setImageBitmap(mBmapLeftFrontBar);
		mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);

		/*
		 * mLeftBtnVolume.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { //int volumeLevel_before =
		 * mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		 * 
		 * //2013-03-26 - raymond - android has bug on mute. we use volum
		 * instead mIsMute = !mIsMute;
		 * //mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, mIsMute); if
		 * (!mIsMute) {
		 * 
		 * //mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, true);
		 * 
		 * //2013-4-02-Winder Hao According to the the mute change to change the
		 * interface changes
		 * mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,0 , 0);
		 * volumeLevel=0; VolumeControl();
		 * mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute); } else{
		 * volumeLevel=currentVolume;
		 * 
		 * VolumeControl(); mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
		 * //mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC,false);
		 * mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume ,
		 * 0); } } });
		 */
		mMainLayout.addView(mLeftImgViewBase);
		mMainLayout.addView(mLeftImgViewBackBar);
		mMainLayout.addView(mLeftImgViewFrontBar);
		mMainLayout.addView(mLeftBtnVolumeIdx);
		mMainLayout.addView(mLeftBtnVolume);
	}

	// 2013-3-26-Winder Hao Encapsulates a method to control the volume
	public void VolumeControl(int level,int maxVolume) {

		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//int maxVolume = 150;
		// int volumeLevel =
		// mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

		angle = (int) (270 + (1 - level / (float) maxVolume) * 180 * 0.98f);
		// mLeftImgViewFrontBar.setRotation(270 + atanAngle.floatValue() * 180 /
		// 3.1417f);

		Matrix m = new Matrix();
		m.setRotate(angle);
		Bitmap bmap = Bitmap.createBitmap(mBmapLeftBtnVolumeIdx, 0, 0, mBmapLeftBtnVolumeIdx.getWidth(),
				mBmapLeftBtnVolumeIdx.getHeight(), m, true);

		int x = -10 + (int) (90f * Math.cos(angle * 3.1416f / 180f));
		int y = 222 + (int) (90f * Math.sin(angle * 3.1416f / 180f)) + Y_ADJUSTMENT;

		mLeftBtnVolumeIdx.setX(x - (int) (bmap.getWidth() / 2f));
		mLeftBtnVolumeIdx.setY(y - (int) (bmap.getHeight() / 2f));
		mLeftImgViewFrontBar.setRotation(270 + angle);

		mLeftImgViewBackBar.setImageBitmap(mBmapLeftBackBar);
		mLeftImgViewFrontBar.setImageBitmap(mBmapLeftFrontBar);
		mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);

	}

/*	public class Receiver extends BroadcastReceiver {  
		  
	    @Override  
	    public void onReceive(Context context, Intent intent) {  
	        String name = intent.getExtras().getString("name");  
	        Log.e("Recevier1", "锟斤拷锟秸碉拷:"+name);  
	    }  
	  
	}  */
	// 2013-4-02-Winder Hao Listening system volume changes
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {


		//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		volumeLevel = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		showMenu();

		// mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,
		// mVerticalSoundView.mCurrentVolume, -2);
		// currentVolume=volumeLevel;
		//Log.e("onKeyDown", "keycode=************************" + "" + keyCode);

		switch (keyCode) {
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			
			if (mVideo.isPlaying())
			{
				mVideo.pause();
				mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
			} else
			{
				//Log.d("mIsPlayCompleted33333", "mIsPlayCompleted====" + mIsPlayCompleted);

				mVideo.start();
				mIsPlayCompleted = false;
				mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
			}
			
			break;
		case  KeyEvent.KEYCODE_MEDIA_PLAY:
			mVideo.start();
			mIsPlayCompleted = false;
			mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);

			//Log.e("onKeyDown", "press 锟斤拷锟绞硷拷锟�");
			break;
		case KeyEvent.KEYCODE_MEDIA_PAUSE:
			mVideo.pause();
			mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);


			

			//Log.e("onKeyDown", "press 锟斤拷锟斤拷锟酵ｏ拷锟�");
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			//Log.e("锟斤拷锟斤拷KEYCODE_VOLUME_DOWN锟斤拷锟斤拷", "222222222222222222222");

			if (volumeLevel > 0)
			{
				volumeLevel--;
			}
			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, AudioManager.FLAG_PLAY_SOUND);
			VolumeControl(volumeLevel,maxVolume);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			// showMenu();
			//Log.e("锟斤拷锟斤拷KEYCODE_VOLUME_UP锟斤拷锟斤拷", "222222222222222222222");

			if (volumeLevel < maxVolume)
			{
				volumeLevel++;
			}
			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel,  AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

			VolumeControl(volumeLevel,maxVolume);
			return true;
/*
		case KeyEvent.KEYCODE_VOLUME_MUTE:
		
			Log.e("锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷", "222222222222222222222");
		
			//sendBroadcast(intent)
			Intent intent = new Intent("com.oregonscientific.meep");
			intent.putExtra("volumeValue", volumeLevel);
			sendBroadcast(intent);
			Log.e("锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷intent", "3333333333");

			//com.oregonscientific.meep

			
			
			
			//int volumeLevel1 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			// mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, true);
			//mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel,  AudioManager.FLAG_PLAY_SOUND);
			//mAudioMgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			//muteSeting();

			//break;
			return true;*/
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
		// return true;
	}
	
	

	

	private void initRightMenu() {
		// right control
		// mBmapRightBase = BitmapFactory.decodeFile(RIGHT_BASE_BG_PATH);
		// mBmapRightBackBar =
		// BitmapFactory.decodeFile(RIGHT_BACK_BAR_IMG_PATH);
		// mBmapRightFrontBar =
		// BitmapFactory.decodeFile(RIGHT_FRONT_BAR_IMG_PATH);
		// mBmapRightBtnProgressIdx =
		// BitmapFactory.decodeFile(LEFT_PLAY_IMG_PATH);
		// mBmapRightBtnPlay = BitmapFactory.decodeFile(RIGHT_BTN_PLAY);
		// mBmapRightBtnPause = BitmapFactory.decodeFile(RIGHT_BTN_PAUSE);

		mBmapRightBase = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_base_right);
		mBmapRightBackBar = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_blue_right);
		mBmapRightFrontBar = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_white_left);
		mBmapRightBtnProgressIdx = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_button);
		mBmapRightBtnPlay = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_play_btn);
		mBmapRightBtnPause = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_pause_normal);

		mRightImgViewBase = new ImageView(this);
		mRightImgViewBackBar = new ImageView(this);
		mRightImgViewFrontBar = new ImageView(this);
		mRightBtnProgressIdx = new ImageView(this);
		mRightBtnPlay = new ImageView(this);

		mRightBtnPlay.setId(R.id.right_btn_play);
		mTxtCurrTime = new TextView(this);
		mTxtTotalTime = new TextView(this);

		// mRightImgViewBase.setX(694);
		// mRightImgViewBase.setY(117);
		// mRightImgViewBackBar.setX(707);
		// mRightImgViewBackBar.setY(128);
		// mRightImgViewFrontBar.setX(710);
		// mRightImgViewFrontBar.setY(131);
		// mRightBtnPlay.setX(753);
		// mRightBtnPlay.setY(203);

		mRightImgViewBase.setX(694);
		mRightImgViewBase.setY(117 + Y_ADJUSTMENT);
		mRightImgViewBackBar.setX(707);
		mRightImgViewBackBar.setY(128 + Y_ADJUSTMENT);
		mRightImgViewFrontBar.setX(706);
		mRightImgViewFrontBar.setY(128 + Y_ADJUSTMENT);
		mRightBtnPlay.setX(753);
		mRightBtnPlay.setY(203 + Y_ADJUSTMENT);

		// get progress info and display
		float ang = 90 * 0.98f;
		Matrix m = new Matrix();
		//m.setRotate(ang);
		//Bitmap bmap = Bitmap.createBitmap(mBmapRightBtnProgressIdx, 0, 0, mBmapRightBtnProgressIdx.getWidth(),mBmapRightBtnProgressIdx.getHeight(), m, true);

		int x = 800 + (int) (90f * Math.cos(ang * 3.1416f / 180f));
		int y = 222 + (int) (90f * Math.sin(ang * 3.1416f / 180f)) + Y_ADJUSTMENT;

		mRightImgViewFrontBar.setRotation(ang + 90);

		mRightBtnProgressIdx.setImageBitmap(mBmapRightBtnProgressIdx);
		mRightBtnProgressIdx.setX(x - (int) (mBmapRightBtnProgressIdx.getWidth() / 2f));
		mRightBtnProgressIdx.setY(y - (int) (mBmapRightBtnProgressIdx.getHeight() / 2f));
		//Log.d("debug", "debug:x:" + x + "   y:" + y);
		mRightBtnProgressIdx.setOnTouchListener(new View.OnTouchListener()
		{

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					// mIsRightMenuEnable = true;
				}
				return false;
			}
		});

		mRightImgViewBase.setImageBitmap(mBmapRightBase);
		mRightImgViewBackBar.setImageBitmap(mBmapRightBackBar);
		mRightImgViewFrontBar.setImageBitmap(mBmapRightFrontBar);
		mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
		/*
		 * mRightBtnPlay.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { if(mVideo.isPlaying()){
		 * mVideo.pause(); mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay); }
		 * else { mVideo.start(); mIsPlayCompleted =false;
		 * mRightBtnPlay.setImageBitmap(mBmapRightBtnPause); } } });
		 */
		// text view
		mTxtCurrTime.setX(750);
		mTxtCurrTime.setY(330 + Y_ADJUSTMENT);
		mTxtCurrTime.setShadowLayer(2f, 2f, 2f, Color.BLACK);
		mTxtCurrTime.setTextSize(20);
		mTxtCurrTime.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		mTxtCurrTime.setTextColor(Color.WHITE);
		mTxtCurrTime.setText("00:00");

		mTxtTotalTime.setX(750);
		mTxtTotalTime.setY(90 + Y_ADJUSTMENT);
		mTxtTotalTime.setShadowLayer(2f, 2f, 2f, Color.BLACK);
		mTxtTotalTime.setTextSize(20);
		mTxtTotalTime.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		mTxtTotalTime.setTextColor(Color.WHITE);
		mTxtTotalTime.setText("00:00");

		mMainLayout.addView(mRightImgViewBase);
		mMainLayout.addView(mRightImgViewBackBar);
		mMainLayout.addView(mRightImgViewFrontBar);
		mMainLayout.addView(mRightBtnProgressIdx);
		mMainLayout.addView(mRightBtnPlay);
		mMainLayout.addView(mTxtCurrTime);
		mMainLayout.addView(mTxtTotalTime);
	}

	private double getLeftAtanAngle(float x, float y) {
		double oX = 0;
		double oY = 222 + Y_ADJUSTMENT;
		double val = (y - oY) / (x - oX);
		return Math.atan(val);
	}

	private double getRightAtanAngle(float x, float y) {
		double oX = 800;
		double oY = 222 + Y_ADJUSTMENT;
		double val = Math.atan((y - oY) / (x - oX));
		// Log.d("gesture", "gesture getRightAtanAngle: " + (y-oY) + "/" +
		// (x-oX) + "=" + val);
		return Math.max(-1.9952f, Math.min(1.9952f, val));
	}

	private int angle = 90;

	private void initProgressCaptureThread() {
		mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				//Log.d("MoviePlayerActivity", "isPlaying "+mVideo.isPlaying());
				switch (msg.what) {
				case 1:
					try
					{
						// progress.setProgress(mVideo.getCurrentPosition());
						setVideoProgress(msg.arg1);
						/*Log.d("progressbar",
								"progressbar2: max:" + mVideo.getDuration() + " curr:" + mVideo.getCurrentPosition());*/
					} catch (Exception ex)
					{
						//Log.d("progressbar", "progressbar error:" + ex.toString());
					}
					break;
				default:
					break;
				}

				super.handleMessage(msg);
			}

		};
		mThread = new MyThread();
		mThread.start();
	}
	public class MyThread extends Thread{
		boolean mEnableTimeInfoThread = true;
		@Override
		public void run() {
			// while (!Thread.currentThread().isInterrupted()) {
			while (mEnableTimeInfoThread)
			{
				
				Message message = new Message();
				message.what = 1;
				message.arg1 = mVideo.getCurrentPosition();
				mHandler.sendMessage(message);
				/*Log.d("progressbar", " progressbar: "+this
						.getId() +" ----->" + message.arg1);*/
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private void setVideoProgress(int currentPos) {
		if (!mIsRightMenuEnable)
		{
			float angle = (currentPos / (float) mVideoLength) * 180 * 0.98f + 90;
			Matrix m = new Matrix();
			//m.setRotate(angle);
			//Bitmap bmap = Bitmap.createBitmap(mBmapRightBtnProgressIdx, 0, 0, mBmapRightBtnProgressIdx.getWidth(),mBmapRightBtnProgressIdx.getHeight(), m, true);

			int x = 800 + (int) (90f * Math.cos(angle * 3.1416f / 180f));
			int y = 222 + (int) (90f * Math.sin(angle * 3.1416f / 180f)) + Y_ADJUSTMENT;

			mRightImgViewFrontBar.setRotation(angle + 90);

			mRightBtnProgressIdx.setImageBitmap(mBmapRightBtnProgressIdx);
			mRightBtnProgressIdx.setX(x - (int) (mBmapRightBtnProgressIdx.getWidth() / 2f));
			mRightBtnProgressIdx.setY(y - (int) (mBmapRightBtnProgressIdx.getHeight() / 2f));

			// show current time
			mTxtCurrTime.setText(getTimeString(currentPos));
		}
	}

	private void initController() {
		mCountDownTimer = new CountDownTimer(5000, 5000)
		{

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				hideMenu();
			}
		};

		mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer mp) {
				String serialNumber = Build.SERIAL.trim();
				//Log.d(TAG, "onCompletion " + serialNumber);
				if (serialNumber.equals("MEEP_SN0000")||serialNumber.equals("unknown"))
				{
					mVideo.start();
					mVideo.seekTo(0);
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
				}else{
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
				}
				//mVideo.seekTo(0);
				
				mIsPlayCompleted = true;
				mVideo.setVideoPath(mMoviePath);
				showMenu();
			}
		});

		mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp) {
				if (!mIsPlayCompleted)
				{

					// Bitmap mBitmap=
					// ThumbnailUtils.createVideoThumbnail(mMoviePath,
					// Thumbnails.MINI_KIND);
					// if (mBitmap!=null){
					// mBitmap.recycle();
					setVideoLength(mp.getDuration());
					mVideo.requestFocus();
					mVideo.start();
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
					// }
					// else {
					// Toast.makeText(MoviePlayerActivity.this, "锟斤拷式锟斤拷锟斤拷",
					// 3000).show();
					// }
				}
				/*
				 * setVideoLength(mp.getDuration()); mVideo.requestFocus();
				 * mVideo.start();
				 * mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
				 */

			}

		});
	}

	private void goBack() {
		finish();
		// stopTimer();
	}

	private void showMenu() {
		mIsMenuShown = true;
		mTitleName.setVisibility(View.VISIBLE);
		mLeftImgViewBackBar.setVisibility(View.VISIBLE);
		mLeftImgViewFrontBar.setVisibility(View.VISIBLE);
		mLeftImgViewBase.setVisibility(View.VISIBLE);
		mLeftBtnVolumeIdx.setVisibility(View.VISIBLE);
		mLeftBtnVolume.setVisibility(View.VISIBLE);
		mRightImgViewBase.setVisibility(View.VISIBLE);
		mRightImgViewBackBar.setVisibility(View.VISIBLE);
		mRightImgViewFrontBar.setVisibility(View.VISIBLE);
		mRightBtnProgressIdx.setVisibility(View.VISIBLE);
		mRightBtnPlay.setVisibility(View.VISIBLE);
		mTxtCurrTime.setVisibility(View.VISIBLE);
		mTxtTotalTime.setVisibility(View.VISIBLE);

		mLeftImgViewBase.requestFocus();
		mRightImgViewBase.requestFocus();
	}

	private void hideMenu() {
		mIsMenuShown = false;
		mTitleName.setVisibility(View.GONE);
		mLeftImgViewBackBar.setVisibility(View.GONE);
		mLeftImgViewFrontBar.setVisibility(View.GONE);
		mLeftImgViewBase.setVisibility(View.GONE);
		mLeftBtnVolumeIdx.setVisibility(View.GONE);
		mLeftBtnVolume.setVisibility(View.GONE);
		mRightImgViewBase.setVisibility(View.GONE);
		mRightImgViewBackBar.setVisibility(View.GONE);
		mRightImgViewFrontBar.setVisibility(View.GONE);
		mRightBtnProgressIdx.setVisibility(View.GONE);
		mRightBtnPlay.setVisibility(View.GONE);
		mTxtCurrTime.setVisibility(View.GONE);
		mTxtTotalTime.setVisibility(View.GONE);
	}

	private Bitmap getVideoThumbnail() {
		String path = "/mnt/sdcard/home/movie/data/movie01.mp4";
		return ThumbnailUtils.createVideoThumbnail(path, Video.Thumbnails.MINI_KIND);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP)
		{

			if (mTitleName.isShown() && event.getX() > 100 && event.getX() < 700)
			{
				hideMenu();
				stopTimer();
			} else
			{
				showMenu();
				startTimer();
			}

			startTimer();
			if (mIsRightMenuEnable)
			{

				float x0 = 800, y0 = 270;
				float x1 = event.getX(), y1 = event.getY();
				float avg = (float) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
				if (60 <= avg && avg <= 120)
				{

					double atanAngle = getRightAtanAngle(event.getX(), event.getY());
					float angle = (float) (atanAngle * 180 / Math.PI);
					int currLoc = getSelectedLoaction(angle);
					if (event.getX() > 800)
					{
						if (event.getY() > 222)
						{
							currLoc = 0;
						} else
						{
							currLoc = mVideoLength;
						}
					}
					if (mVideo.isPlaying()&&mIsPlayCompleted)
					{
						mVideo.seekTo(currLoc - 1);
						mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);

					} else if (!mIsPlayCompleted)
					
					{
						//mIsPlayCompleted = false;
						mVideo.start();
						mVideo.seekTo(currLoc - 1);
						mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
						// mVideo.pause();
					}
				}
			}
			mIsLeftMenuEnable = false;
			mIsRightMenuEnable = false;
			//Log.d("gesture", "gesture : action up");
		} else if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			mIsLeftMenuEnable = event.getX() < 120 & mIsMenuShown;
			mIsRightMenuEnable = event.getX() > 680 & mIsMenuShown;

			stopTimer();
			//Log.d("gesture", "gesture : action down");
		} else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (mIsMenuShown)
			{
				// 2013-03-13 - raymond - restrict the touch area
				// 2013-5-22 - Zoya - decrease touch music control area
				// Log.e("sign","move:"+event.getX()+"--"+event.getY());
				if (mIsLeftMenuEnable && event.getY() < 390 && event.getY() > 150 && event.getX() < 120
						&& event.getX() > 0)
				{
					float x0 = 0, y0 = 270;
					float x1 = event.getX(), y1 = event.getY();
					float avg = (float) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
					if (60 <= avg && avg <= 120)
					{
						Double atanAngle = getLeftAtanAngle(event.getX(), event.getY()) * 0.98f;
						int x = -10 + (int) (90 * Math.cos(atanAngle));
						int y = 222 + (int) (90 * Math.sin(atanAngle)) + Y_ADJUSTMENT;

						mLeftImgViewFrontBar.setRotation(270 + atanAngle.floatValue() * 180 / 3.1417f);

						Matrix m = new Matrix();
						m.setRotate(atanAngle.floatValue() * 180 / 3.1417f);
						Bitmap bmap = Bitmap.createBitmap(mBmapLeftBtnVolumeIdx, 0, 0,
								mBmapLeftBtnVolumeIdx.getWidth(), mBmapLeftBtnVolumeIdx.getHeight(), m, true);
						mLeftBtnVolumeIdx.setImageBitmap(bmap);

						mLeftBtnVolumeIdx.setX(x - (int) (mLeftBtnVolumeIdx.getWidth() / 2f));
						mLeftBtnVolumeIdx.setY(y - (int) (mLeftBtnVolumeIdx.getHeight() / 2f));
						// 2013-08-16-winderhao
					//	mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);

						int maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
						int volume = (int) (maxVolume * (1 - ((atanAngle.floatValue() * 180 / 3.1417f) + 90) / 180f));
						// 2013-03-26 - raymond - android has bug on mute. we
						// use volum instead
						currentVolume = volume;
						// set volume
						mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
						mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
						//Log.d("gesture", "gesture left menu: action move - angle:" + atanAngle * 180 / 3.14f + " x:"
						//		+ x + " y:" + y);
					}
				} else if (mIsRightMenuEnable && event.getY() < 390 && event.getY() > 150 && event.getX() > 680
						&& event.getX() < 800)
				{
					//
					float x0 = 800, y0 = 270;
					float x1 = event.getX(), y1 = event.getY();
					// System.out.println(x1+"x1111");
					// System.out.println(y1+"x2222");
					float avg = (float) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
					// System.out.println(avg+"avg");
					if (60 <= avg && avg <= 120)
					{
						// 2013-03-13 - raymond - restrict the touch area
						// if (event.getX() < 800 && event.getY() > 170) {
						double atanAngle = getRightAtanAngle(event.getX(), event.getY()) * 0.98f;// *
						int x = 800 + (int) (-90 * Math.cos(atanAngle));
						int y = 222 + (int) (-90 * Math.sin(atanAngle)) + Y_ADJUSTMENT;
						float angle = (float) (atanAngle * 180 / Math.PI);
						System.out.println(" avg  80<=avg && avg<=120");
						mRightImgViewFrontBar.setRotation(-90 + angle);
						//Log.d("gesture", "gesture right mRightImgViewFrontBar atan: " + atanAngle + "angle:" + angle
						//		+ " x:" + event.getX() + " y: " + event.getY());

						Matrix m = new Matrix();
						m.setRotate(angle);
						Bitmap bmap = Bitmap.createBitmap(mBmapRightBtnProgressIdx, 0, 0,
								mBmapRightBtnProgressIdx.getWidth(), mBmapRightBtnProgressIdx.getHeight(), m, true);
						mRightBtnProgressIdx.setImageBitmap(bmap);

						mRightBtnProgressIdx.setX(x - (int) (mRightBtnProgressIdx.getWidth() / 2f));
						mRightBtnProgressIdx.setY(y - (int) (mRightBtnProgressIdx.getHeight() / 2f));

						//Log.d("gesture", "gesture right menu: action move - angle:" + atanAngle * 180 / 3.1417f + " x:"
						//		+ x + " y:" + y);

					} else
					{

						System.out.println("******************************");

					}
				}
			}
		}
		return super.onTouchEvent(event);
	}

	private void setVideoLength(int videoLength) {
		mVideoLength = videoLength;
		mTxtTotalTime.setText(getTimeString(videoLength));
	}

	private int getVideoLength() {
		return mVideoLength;
	}

	private String getTimeString(int timeValue) {
		int totalSeconds = timeValue / 1000;
		int seconds = totalSeconds % 60;
		int minutes = totalSeconds / 60;
		// return String.format("%tMM:*tSS", timeValue);
		return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}

	private int getSelectedLoaction(float angle) {
		return (int) ((angle + 90f) / 180f * mVideoLength);
	}

	private void startTimer() {
		mCountDownTimer.start();
	}

	private void stopTimer() {
		mCountDownTimer.cancel();
	}

	private void resetTimer() {
		stopTimer();
		startTimer();
	}

	// String ns = Context.NOTIFICATION_SERVICE;
	// NotificationManager mNotificationManager;
	// private void testNotification()
	// {
	// mNotificationManager = (NotificationManager) getSystemService(ns);
	//
	// RemoteViews contentView = new RemoteViews(getPackageName(),
	// R.layout.volume);
	// contentView.setImageViewResource(R.id.image, R.drawable.bar_icon_back2);
	// contentView.setTextViewText(R.id.title, "Custom notification");
	// contentView.setTextViewText(R.id.seekBar1, "button1");
	// //contentView.setTextViewText(R.id.text, "This is a custom layout");
	// //notification.contentView = contentView;
	//
	//
	//
	//
	//
	// Intent notificationIntent = new Intent(this, MoviePlayerActivity.class);
	// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	// notificationIntent, 0);
	//
	// // the next two lines initialize the Notification, using the
	// configurations above
	// Notification notification = new Notification(R.drawable.ic_launcher,
	// "test", System.currentTimeMillis());
	// //Notification notifi = new Notification();
	// notification.contentView = contentView;
	// notification.contentIntent = contentIntent;
	// //notification.setLatestEventInfo(context, contentTitle, contentText,
	// contentIntent);
	//
	// mNotificationManager.notify(6, notification);
	// }
	//
	// Handler handler = new Handler();

}