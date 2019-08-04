package com.oregonscientific.meep.meepmusic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.meepmusic.media.IPlayerController;
import com.oregonscientific.meep.meepmusic.media.MusicPlayerController;
import com.oregonscientific.meep.meepmusic.opengl.MusicPlayerRender;
import com.oregonscientific.meep.meepmusic.opengl.MusicPlayerRender.OspadRenderListener;
import com.oregonscientific.meep.opengl.StateManager.SystemState;

public class MusicPlayerActivity extends Activity implements OnGestureListener {

	private static final String TAG = MusicPlayerActivity.class.getSimpleName();
	private static final String ACTION = "com.example.playerserver.PlayerService";
	
	private GestureDetector mGuestureDetector; // touching event handler
	private MusicPlayerRender mRender;
	private GLSurfaceView glView; // open gl surface view
	private AudioManager mAudioMgr = null;
	private boolean mIsPrepared = false;
	private boolean mIsMute = false;
	private boolean mIsReadyToPlay = false;
	private int mLeftAngle = 90;
	private long mMusicLength = 0;

	private int maxVolume;
	private int volumeLevel;

	private SongObj mPlayingSongObj = null;
	private boolean mIsPlayingMusic = false;
	private BroadcastReceiver myReceiver;

	private RelativeLayout mMainLayout = null;
	//MediaPlayer mMediaPlayer;
	private IPlayerController mController;
	private boolean mIsLeftMenuEnable = false;
	private boolean mIsRightMenuEnable = false;

	private Handler mHandler = null;
	private Runnable mThread = null;
	private boolean mEnableTimeInfoThread = false;

	private boolean mRunning;
	private boolean mPlayable;

	private final int Y_ADJUSTMENT = 40;

	private String mPath = null;

	// title
	private TextView mTxtTitle = null;
	private ImageView mCoverImage = null;
	//private ImageView mRepeatableImage = null;

	//private Bitmap mBmapTitleRepeat = null;
	//private Bitmap mBmapTitleRepeatGlow = null;

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

	private int mCurrentPosition = -1;

	// 2013-03-26 - raymond - android has bug on mute. we use volum instead
	private int currentVolume;
	private boolean isBinded = false;

	private DialogFragment popupFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(ACTION));
		setContentView(R.layout.musicplayer);
		myReceiver = new BroadcastReceiver() {  
			  
	        @Override  
	        public void onReceive(Context context, Intent intent) {
	        	if (intent.getAction().equals("com.oregonscientific.meep.VolumeChanged")) {
	        		
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
	        	}/* else  if (intent.getAction().equals(MeepMusicPlayerService.SERVICE_BROADCAST)) {
	        		onReceivedPlayerServiceChanged(intent.getExtras());
	        	}*/
	        }  
	  
	    };  
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.oregonscientific.meep.VolumeChanged");
		//intentFilter.addAction(MeepMusicPlayerService.SERVICE_BROADCAST);
		//intentFilter.setPriority(Integer.MAX_VALUE);  
	    registerReceiver(myReceiver, intentFilter);
	    mController = new MusicPlayerController(this);
	    mController.setOnMediaStatusChanged(new MusicControllListener());
	    
		mRunning = true;
		mPlayable = false;
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");

		mMainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		initMediaPlayer();
		initOpenglRender();
		initLeftMenu();
		initRightMenu();
		initMenu();

		initProgressCaptureThread();
	}

	@Override
	public void onResume() {
		// if (mIsPlayingMusic && mCurrentPosition >= 0) {
		// mMediaPlayer.seekTo(mCurrentPosition);
		// startMusic();
		// }
		
		/*try {
			mService.play();
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
		mIsReadyToPlay = true;
		//mIsRightMenuEnable = false;
		//Log.e("mIsRightMenuEnable", "onResume####mIsRightMenuEnable=="+mIsRightMenuEnable);
//		glView.onResume();
		mPlayable = true;
		super.onResume();
	}

	@Override
	public void onPause() {
		// if (mIsPlayingMusic) {
		// mCurrentPosition = mMediaPlayer.getCurrentPosition();
		// mMediaPlayer.pause();
		// }
		// finish();
		/*try {
			mService.pause();
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
		//mIsReadyToPlay = false;
		//mIsRightMenuEnable = false;
		//pauseMusic();
		//Log.e("mIsRightMenuEnable", "onPause####mIsRightMenuEnable=="+mIsRightMenuEnable);
		mPlayable = false;
		Log.i(TAG, "onPause");
		if (mIsPlayingMusic) {
			pauseMusic();
		}
		super.onPause();
	}

	private void initMediaPlayer() {
		
		
		/*if (mMediaPlayer == null)
		{
			Log.d("mediaplayer", "player is null");
		}
		mMediaPlayer = new MediaPlayer();

		mMediaPlayer.setOnPreparedListener(new OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp) {
				mIsPrepared = true;
				popupFragment.dismiss();
				Log.d("progressbar", "progress bar: duration:" + mp.getDuration());
				if (mPlayingSongObj != null)
				{
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);

					mCoverImage.setImageBitmap(mPlayingSongObj.getConverImage());
					if (mPlayingSongObj != null && !mPlayingSongObj.getAlbum().equals(""))
					{
						mTxtTitle.setText(mPlayingSongObj.getAlbum());
					} else
					{
						File file = new File(mPlayingSongObj.getPath());
						String name = file.getName();
						mTxtTitle.setText(name);
					}
				}
				setAudioLength(mp.getDuration());
				if (mIsPlayingMusic)
				{
					mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
					// mMediaPlayer.start();
				}
			}
		});
		mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer mp) {
				if (mRender.IsRepeatable())
				{
					startMusic();
				} else
				{
					playNext();
					Log.e("playNext()", "playNext()**********");

					
				}

			}
		});*/

		// try {
		// String path = "/mnt/sdcard/home/music/data/m01.mp3";
		// File file = new File(path);
		// if (file.exists()) {
		// mMediaPlayer.setDataSource(path);
		// mMediaPlayer.prepare();
		// } else {
		// Toast.makeText(this, "file not exit", Toast.LENGTH_SHORT);
		// }
		// } catch (IllegalArgumentException e) {
		// //  Auto-generated catch block
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// //  Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalStateException e) {
		// //  Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// //  Auto-generated catch block
		// e.printStackTrace();
		// }
		mPath = getIntent().getStringExtra(Global.STRING_PATH);
		mController.openFile(mPath);
	}

	@Override
	protected void onDestroy() {
		//mMediaPlayer.stop();
		// Mar 13,2013 written by aaron li,stop the thread when destroy.
		/*try {
			mService.stop();
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
		mEnableTimeInfoThread = false;
		unregisterReceiver(myReceiver);
		mRunning = false;
		glView.onPause();
		super.onDestroy();
		mController.release();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void setAudioLength(long musicLength) {
		mMusicLength = musicLength;
		mTxtTotalTime.setText(getTimeString(musicLength));
	}

	private String getTimeString(long timeValue) {
		long totalSeconds = timeValue / 1000;
		long seconds = totalSeconds % 60;
		long minutes = totalSeconds / 60;
		// return String.format("%tMM:*tSS", timeValue);
		return String.format("%02d:%02d", minutes, seconds);
	}

	private void initMenu() {
		mTxtTitle = new TextView(this);
		mTxtTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 80));
		mTxtTitle.setBackgroundColor(Color.argb(128, 0, 0, 0));
		mTxtTitle.setBackgroundResource(R.drawable.player_title);
		mTxtTitle.setGravity(Gravity.CENTER);
		mTxtTitle.setTextSize(30);
		mTxtTitle.setBackgroundDrawable(getResources().getDrawable(R.drawable.player_title));
		mTxtTitle.setShadowLayer(2f, 2f, 2f, Color.argb(100, 0, 0, 0));
		// 2013-6-27 Set single-line text display, use... End
		mTxtTitle.setSingleLine(true);
		mTxtTitle.setEllipsize(TruncateAt.END);
		mTxtTitle.setTextColor(Color.WHITE);

		mCoverImage = new ImageView(this);
		mCoverImage.setX(40);
		mCoverImage.setY(16);

		//mRepeatableImage = new ImageView(this);
		//mBmapTitleRepeat = BitmapFactory.decodeFile(TITLE_REPEAT_PATH);
		//mBmapTitleRepeatGlow = BitmapFactory.decodeFile(TITLE_REPEAT_GLOW_PATH);

		//mRepeatableImage.setImageBitmap(mBmapTitleRepeat);
		//mRepeatableImage.setX(700);
		//mRepeatableImage.setY(30);

		/*mRepeatableImage.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				boolean isRepeatable = mRender.IsRepeatable();

				if (isRepeatable)
				{
					mRepeatableImage.setImageBitmap(mBmapTitleRepeat);
				} else
				{
					mRepeatableImage.setImageBitmap(mBmapTitleRepeatGlow);
				}

				mRender.setIsRepeatable(!isRepeatable);
			}
		});*/

		mMainLayout.addView(mTxtTitle);
		mMainLayout.addView(mCoverImage);
		//mMainLayout.addView(mRepeatableImage);

	}

	// 2013-4-8 - Zoya - Integration OnClickListener code.
	OnClickListener initleftRightMenuListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
		/*	case R.id.LeftID:
				muteSeting();*/
				// 2013-03-26 - raymond - android has bug on mute. we use volum
				// instead
			/*	mIsMute = !mIsMute;
				// mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, mIsMute);
				if (mIsMute)
				{
					mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, true);

					// mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
					// 0);
					// volumeLevel = 0;
					// VolumeControl();
					mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute);

				} else
				{
					mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);

					// volumeLevel = currentVolume;
					// VolumeControl();
					// mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC,
					// currentVolume, 0);
					mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
				}
				break;
*/
			case R.id.RightID:
                if (mController.isPlaying())
                {
                	pauseMusic();
                	//mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
                } else
                {
                	//mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
                	startMusic();
                }
				break;
			}

		}
	};
/*	int volumeLevel1;
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

			Log.v("!mIsMute", "!mIsMute静音状态");
		  volumeLevel2 = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		  volumeLevel1 = volumeLevel2;
			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
			//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, true);

			mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeMute);

		}
		
	}*/
	private void initLeftMenu() {

		// left control
		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int volumeLevel = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

		// set audio un-mute
		mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
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
		mBmapLeftFrontBar = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_white_right);
		mBmapLeftBtnVolumeIdx = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_button);
		mBmapLeftBtnVolumeOn = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_volume_on);
		mBmapLeftBtnVolumeMute = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_volume_muted);

		mLeftImgViewBase = new ImageView(this);
		mLeftImgViewBackBar = new ImageView(this);
		mLeftImgViewFrontBar = new ImageView(this);
		mLeftBtnVolumeIdx = new ImageView(this);
		mLeftBtnVolume = new ImageView(this);
		mLeftBtnVolume.setId(R.id.LeftID);

		mLeftImgViewBase.setX(-10);
		mLeftImgViewBase.setY(117 + Y_ADJUSTMENT);
		mLeftImgViewBackBar.setX(-10);
		mLeftImgViewBackBar.setY(128 + Y_ADJUSTMENT);
		mLeftImgViewFrontBar.setX(-102);
		mLeftImgViewFrontBar.setY(128 + Y_ADJUSTMENT);
		mLeftBtnVolume.setX(7);
		mLeftBtnVolume.setY(203 + Y_ADJUSTMENT);
		/*
		 * mRightImgViewBase.setX(694); mRightImgViewBase.setY(117);
		 * mRightImgViewBackBar.setX(707); mRightImgViewBackBar.setY(128);
		 * mRightImgViewFrontBar.setX(710); mRightImgViewFrontBar.setY(131);
		 * mRightBtnPlay.setX(753); mRightBtnPlay.setY(203);
		 */

		// get volume and display
		// mLeftAngle = (int) (270 + (1 - volumeLevel / (float) maxVolume) * 180
		// * 0.98f);
		// 2013-5-13 -Amy-
		mLeftAngle = (int) (270 + (1 - volumeLevel / (float) maxVolume) * 180 * 0.98f);

		Matrix m = new Matrix();
		m.setRotate(mLeftAngle);
		Bitmap bmap = Bitmap.createBitmap(mBmapLeftBtnVolumeIdx, 0, 0, mBmapLeftBtnVolumeIdx.getWidth(),
				mBmapLeftBtnVolumeIdx.getHeight(), m, true);

		int x = -10 + (int) (90f * Math.cos(mLeftAngle * 3.1416f / 180f));
		int y = 222 + (int) (90f * Math.sin(mLeftAngle * 3.1416f / 180f)) + Y_ADJUSTMENT;

		// int x = 800 + (int) (90f * Math.cos(ang * 3.1416f / 180f));
		// int y = 222 + (int) (90f * Math.sin(ang * 3.1416f / 180f));

		mLeftImgViewFrontBar.setRotation(270 + mLeftAngle);
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
					//Log.e("cdf", "mLeftBtnVolumeIdx touch");
				}
				return false;
			}
		});

		mLeftImgViewBase.setImageBitmap(mBmapLeftBase);
		mLeftImgViewBackBar.setImageBitmap(mBmapLeftBackBar);
		mLeftImgViewFrontBar.setImageBitmap(mBmapLeftFrontBar);
		mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
		//mLeftBtnVolume.setOnClickListener(initleftRightMenuListener);
		mMainLayout.addView(mLeftImgViewBase);
		mMainLayout.addView(mLeftImgViewBackBar);
		mMainLayout.addView(mLeftImgViewFrontBar);
		mMainLayout.addView(mLeftBtnVolumeIdx);
		mMainLayout.addView(mLeftBtnVolume);

	}

	// 2013-3-26-Winder Hao Encapsulates a method to control the volume
	public void VolumeControl(int level,int maxVolume) {

		mAudioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	//	int maxVolume = 150;
		// 	int maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);

		int angle = (int) (270 + (1 - level / (float) maxVolume) * 180 * 0.98f);
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
		//boolean runing = false;
		Handler handler =  new Handler();
		// 2013-4-02-Winder Hao Listening system volume changes
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.e("onKeyDown", "keycode=************************" + "" + keyCode);

		mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		volumeLevel = mAudioMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// currentVolume = volumeLevel;
		switch (keyCode) {
		/*case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
			
			if (mMediaPlayer.isPlaying())
			{
				pauseMusic();
			} else
			{
				startMusic();
			}
			
			break;
		case  KeyEvent.KEYCODE_MEDIA_PLAY:
			startMusic();

			//Log.e("onKeyDown", "press 耳机暂停键");
			break;
		case KeyEvent.KEYCODE_MEDIA_PAUSE:
			mRunning = false;

			pauseMusic();

			//Log.e("onKeyDown", "press 耳机暂停键");
			break;
		case KeyEvent.KEYCODE_MEDIA_NEXT:
		
			
		
			//handler.postDelayed(runnable, 0);
			
			
		//	handler.removeCallbacks(runnable);
			//playNext();
			//Log.e("onKeyDown", "***********keyCode***********"+keyCode);

			//Log.e("onKeyDown", "press 耳机下一首键");
			
			break;
		case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			
			//playPrev();
			//startMusic();

			//Log.e("onKeyDown", "press 耳机上一首键");
			break;*/
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			//Log.e("onKeyDown", "press 耳机升音");

			mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);

			if (volumeLevel > 0)
			{
				volumeLevel--;
			}
			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, -2);
			VolumeControl(volumeLevel,maxVolume);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:

			//Log.e("onKeyDown", "press 耳机降音");
			mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);

			mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
			if (volumeLevel < maxVolume)
			{
				volumeLevel++;
			}
			mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, -2);
			VolumeControl(volumeLevel,maxVolume);
			return true;
		case KeyEvent.KEYCODE_BACK:

			mRunning = false;
			break;
			//Log.e("onKeyDown", "press the return key");
		case KeyEvent.KEYCODE_HOME:

			//Log.d("onKeyDown", "press the home key");
			mRunning = false;
			//stopMusic();
			//finish();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	//winder
	

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
		mBmapRightFrontBar = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_white_right);
		mBmapRightBtnProgressIdx = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_button);
		mBmapRightBtnPlay = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_play_btn);
		mBmapRightBtnPause = BitmapFactory.decodeResource(this.getResources(), R.drawable.player_pause_normal);

		mRightImgViewBase = new ImageView(this);
		mRightImgViewBackBar = new ImageView(this);
		mRightImgViewFrontBar = new ImageView(this);
		mRightBtnProgressIdx = new ImageView(this);
		mRightBtnPlay = new ImageView(this);
		mRightBtnPlay.setId(R.id.RightID);

		mTxtCurrTime = new TextView(this);
		mTxtTotalTime = new TextView(this);

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
		m.setRotate(ang);
		Bitmap bmap = Bitmap.createBitmap(mBmapRightBtnProgressIdx, 0, 0, mBmapRightBtnProgressIdx.getWidth(),
				mBmapRightBtnProgressIdx.getHeight(), m, true);

		int x = 800 + (int) (90f * Math.cos(ang * 3.1416f / 180f));
		int y = 222 + (int) (90f * Math.sin(ang * 3.1416f / 180f)) + Y_ADJUSTMENT;

		mRightImgViewFrontBar.setRotation(ang + 90);

		mRightBtnProgressIdx.setImageBitmap(bmap);
		mRightBtnProgressIdx.setX(x - (int) (bmap.getWidth() / 2f));
		mRightBtnProgressIdx.setY(y - (int) (bmap.getHeight() / 2f));
		//Log.d("debug", "debug:x:" + x + "   y:" + y);
		mRightBtnProgressIdx.setOnTouchListener(new View.OnTouchListener()
		{

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
				//	mIsRightMenuEnable = true;
				}
				return false;
			}
		});

		mRightImgViewBase.setImageBitmap(mBmapRightBase);
		mRightImgViewBackBar.setImageBitmap(mBmapRightBackBar);
		mRightImgViewFrontBar.setImageBitmap(mBmapRightFrontBar);
		mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
		mRightBtnPlay.setOnClickListener(initleftRightMenuListener);
		// text view
		mTxtCurrTime.setX(750);
		mTxtCurrTime.setY(330 + Y_ADJUSTMENT);
		mTxtCurrTime.setTextSize(20);
		mTxtCurrTime.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		mTxtCurrTime.setText("00:00");
		mTxtCurrTime.setShadowLayer(2f, 2f, 2f, Color.BLACK);
		mTxtCurrTime.setTextColor(Color.WHITE);

		mTxtTotalTime.setX(750);
		mTxtTotalTime.setY(90 + Y_ADJUSTMENT);
		mTxtTotalTime.setTextSize(20);
		mTxtTotalTime.setTextAppearance(this, android.R.attr.textAppearanceLarge);
		mTxtTotalTime.setText("00:00");
		mTxtTotalTime.setShadowLayer(2f, 2f, 2f, Color.BLACK);
		mTxtTotalTime.setTextColor(Color.WHITE);

		mMainLayout.addView(mRightImgViewBase);
		mMainLayout.addView(mRightImgViewBackBar);
		mMainLayout.addView(mRightImgViewFrontBar);
		mMainLayout.addView(mRightBtnProgressIdx);
		mMainLayout.addView(mRightBtnPlay);
		mMainLayout.addView(mTxtCurrTime);
		mMainLayout.addView(mTxtTotalTime);
	}

	private void initProgressCaptureThread() {

		mEnableTimeInfoThread = true;
		mHandler = new Handler();
		/*mThread = new Thread(new Runnable()
		{
			@Override
			public void run() {
				// while (!Thread.currentThread().isInterrupted()) {
				while (mEnableTimeInfoThread)
				{
					if (mIsPrepared)
					{
						//if (mMediaPlayer.getCurrentPosition() != mMediaPlayer.getDuration()){
							Log.d("progressbar", "progressbar: getCurrentPosition:" + mMediaPlayer.getCurrentPosition()
									+ " -- " + mMediaPlayer.getDuration());
							Message message = new Message();
							message.what = 1;
							mHandler.sendMessage(message);
							//Log.d("progressbar", "progressbar:" + message.what);
						//}
					}
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException e)
					{
						Thread.currentThread().interrupt();
					}
				}
				//Log.d("progressbar", "progressbar: thread ended");
			}
		});
		mThread.start();*/
		
		mThread = new Runnable() {
			
			@Override
			public void run() {
				try
				{
					// progress.setProgress(mVideo.getCurrentPosition());
	/*				setMisicProgress(mMediaPlayer.getCurrentPosition());*/
					/*Log.d(TAG, "progressbar2: max:" + mService.duration() + " curr:"
							+ mService.getSeek());*/
					setMisicProgress(mController.getSeek());
				} catch (Exception ex)
				{
					Log.w("progressbar", "progressbar error", ex);
				}
				
				mHandler.postDelayed(this, 1000);
				
			}
		};
	}

	private void initOpenglRender() {
		try
		{
			glView = (GLSurfaceView) findViewById(R.id.surfaceViewMusicPlayer);
			mGuestureDetector = new GestureDetector(this);
			mRender = new MusicPlayerRender(this);

			mRender.setOnOspadRenderListener(new OspadRenderListener()
			{

				@Override
				public void onMusicSelectedIndexChanged(int index) {
					
					Log.d(TAG, "onMusicSelectedIndexChanged "+index);
					if (mPlayable && mController.getQueuePosition() != index) {
						mController.openQueuePositio(index);
					}
						/*if (!mService.isPlaying()) {
							mService.play();
						}*/
				}

				@Override
				public void OnSystemStateChanged(SystemState state) {

				}

				@Override
				public void OnSurfaceCreated() {
					/*// mRender.setContentPath(mPath);
					//mPath = getIntent().getStringExtra(Global.STRING_PATH);
					if (mPath != null && mPath != "")
					{
						mRender.setContentPath(mPath);
						//mRender.reloadMusicInfo();
						// initProgressCaptureThread();
					}*/
					
					mEnableTimeInfoThread = true;
				}

				@Override
				public void onMusicRenderReady() {
					/*popupFragment.dismiss();
					try {
						mService.play();
					} catch (RemoteException e) {
						e.printStackTrace();
					}*/
				}
			});

			if (detectOpenGLES20())
			{
				// Tell the surface view we want to create an OpenGL ES
				// 2.0-compatible
				// context, and set an OpenGL ES 2.0-compatible renderer.
				glView.setEGLContextClientVersion(2);
				glView.setRenderer(mRender);
				glView.setOnTouchListener(new GlOnTouchListener());
				glView.setLongClickable(true);
			} else
			{
				//Log.e("HelloTriangle", "OpenGL ES 2.0 not supported on device.  Exiting...");
				finish();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private boolean detectOpenGLES20() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
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

	private int getSelectedLoaction(float angle) {
		return (int) ((angle + 90f) / 180f * mMusicLength);
	}

	private void setMisicProgress(int currentPos) {
		if (mMusicLength <= 0 && mController.isPlaying()) {
			setAudioLength(mController.duration());
		}
		if (!mIsRightMenuEnable)
		{
			float angle = (currentPos / (float) mMusicLength) * 180 * 0.98f + 90;
			Matrix m = new Matrix();
			m.setRotate(angle);
			//Log.d(TAG, "setMisicProgress "+mBmapRightBtnProgressIdx);
			try {
				Bitmap bmap = Bitmap.createBitmap(mBmapRightBtnProgressIdx, 0, 0, mBmapRightBtnProgressIdx.getWidth(),
						mBmapRightBtnProgressIdx.getHeight(), m, true);
				
				int x = 800 + (int) (90f * Math.cos(angle * 3.1416f / 180f));
				int y = 222 + (int) (90f * Math.sin(angle * 3.1416f / 180f)) + Y_ADJUSTMENT;
				
				mRightImgViewFrontBar.setRotation(angle + 90);
				
				mRightBtnProgressIdx.setImageBitmap(bmap);
				mRightBtnProgressIdx.setX(x - (int) (bmap.getWidth() / 2f));
				mRightBtnProgressIdx.setY(y - (int) (bmap.getHeight() / 2f));
				
				// show current time
				mTxtCurrTime.setText(getTimeString(currentPos));
				
			} catch (Exception e) {
				Log.w(TAG, "Bitmap of Progress "+ e);
				return;
			}
		}
	}

	private void playNext() {
		mRender.setFlingDistance(20);
		
	}

	private void playPrev() {
		mRender.setFlingDistance(-20);
	}

	private void pauseMusic() {
		//mMediaPlayer.pause();
		mController.pause();
		
	}

	private void startMusic() {
		//mMediaPlayer.start();
		mController.play();
		
	}

	public void stopMusic() {
		//mMediaPlayer.stop();
		
		mController.stop();
		
	}

	/*private void onReceivedPlayerServiceChanged(Bundle b) {
		Log.d(TAG, "onReceivedPlayerServiceChanged "+b.getString(MeepMusicPlayerService.MSG_CHANGED));
		final String changed = b.getString(MeepMusicPlayerService.MSG_CHANGED);
		if (("prepared").equals(changed)) {
			//mWaiter.setLoadedMusicFile(true);
			// mRender.setContentPath(mPath);
			//mPath = getIntent().getStringExtra(Global.STRING_PATH);
			if (mPath != null && !mPath.equals(""))
			{
				mRender.setContentPath(mPath);
				mRender.reloadMusicInfo(mController.getSongFileList());
				// initProgressCaptureThread();
			}
		} else if ("initialized".equals(changed)) {
			showingOnInitialized();
		} else if (MeepMusicPlayerService.MSG_ALBUMCHANGED.equals(changed)) {
			 getIntent().getStringExtra("albumPath");
		} else if ("seek".equals(changed)) {
			try {
				//int position = mService.getSeek();
				//Log.d(TAG, "mService.getSeek " + position);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		} else if ("started".equals(changed)) {
			invidateViewStarted();
			
		} else if ("paused".equals(changed)) {
			invidateViewPaused();
			
		} else if (MeepMusicPlayerService.MSG_ALLLIST_END.equals(changed)) {
			invidateViewPaused();
		}
	}*/

	private void showingOnInitialized() {
		popupFragment.dismiss();
		mTxtTitle.setText(mController.getName());
		setAudioLength(mController.duration());
		//Log.d(TAG, "fling to "+mService.getQueuePosition());
		mRender.flingToSelected(mController.getQueuePosition());
		if (mPlayable) {
			mController.play();
		}
		invidateViewStarted();
	}
	
	private void invidateViewStarted() {
		mIsPlayingMusic = true;
		mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
	}
	
	private void invidateViewPaused() {
		mIsPlayingMusic = false;
		mRightBtnPlay.setImageBitmap(mBmapRightBtnPlay);
	}

	@Override
	protected void onStart() {
		// Log.d("musicplayer", "onstart");
		// mPath = getIntent().getStringExtra(Global.STRING_PATH);
		// if (mRender.IsSurfaceCreated()) {
		// mRender.setContentPath(mPath);
		// mRender.reloadMusicInfo();
		// Log.d("musicplayer", "reload");
		// if(mPath!=null && mPath != ""){
		// mEnableTimeInfoThread = true;
		//
		// }
		// }
		/*String path = getIntent().getStringExtra(Global.STRING_PATH);
		Log.d(TAG, "onstart "+path);
		if (!path.equals(mPath)) {
			try {
				mService.openFile(path);
				mPath = path;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}*/
		mHandler.postDelayed(mThread, 1000);
		super.onStart();
	}

	@Override
	protected void onStop() {
		stopMusic();
		//mAudioMgr.setStreamMute(AudioManager.STREAM_MUSIC, false);
		mHandler.removeCallbacksAndMessages(null);
		finish();
		super.onStop();
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
		// Log.d(this.getClass().getSimpleName(), "first event "+e1.getX()+
		// ","+e1.getY());
		// Log.d(this.getClass().getSimpleName(), "end event "+e2.getX()+
		// ","+e2.getY());
		// modified by aaronli at jul9 2013. The songlist should show faster.
		if (e1.getX() > 200 && e1.getX() < 600 && e2.getX() > 200 && e2.getX() < 600)
		{
			mRender.setFling(vx, vy);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		mRender.printItem();

	}

	@Override
	public boolean onScroll(MotionEvent e0, MotionEvent e1, float distanceX, float distanceY) {
		if (e0.getX() > 200 && e0.getX() < 600)
		{

			mRender.setScroll(distanceY);
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}
	
	/*
	 * public boolean getmRunning() { return mRunning; }
	 */

	public void setmRunning(boolean mRunning) {
		this.mRunning = mRunning;
	}

	public boolean getmRunning() {
		return mRunning;
	}
	
	private class GlOnTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				// Log.e("cdf","up:"+event.getX()+"--"+event.getY());
				if (mIsRightMenuEnable)
				{
					float x0 = 800, y0 = 270;
					float x1 = event.getX(), y1 = event.getY();
					float avg = (float) Math.sqrt(Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2));
					if (60 <= avg && avg <= 120)
					{
						double atanAngle = getRightAtanAngle(event.getX(), event.getY());
						float angle = (float) (atanAngle * 180 / Math.PI);
						long currLoc = getSelectedLoaction(angle);
						// long --> int
						int curr =new Long(currLoc).intValue();
						if (event.getX() > 800)
						{
							if (event.getY() > 222)
							{
								currLoc = 0;
							} else
							{
								currLoc = mMusicLength;
							}
						}
                        if (mController.isPlaying())
                        {
                        	mController.seek(curr-1);
                            //mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
                        }else {
                        	mController.play();
                        	mController.seek(curr-1);
							//mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
						}
						/*if (mMediaPlayer.isPlaying())
						{
							mMediaPlayer.seekTo(currLoc - 1);
							mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
						} else
						{
							mMediaPlayer.start();
							mIsPlayingMusic = true;
							mMediaPlayer.seekTo(currLoc - 1);
							mRightBtnPlay.setImageBitmap(mBmapRightBtnPause);
							// mVideo.pause();
						}*/
					}
				}
				mIsLeftMenuEnable = false;
				mIsRightMenuEnable = false;
				//Log.d("gesture", "gesture : action up");
			} else if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				mIsLeftMenuEnable = event.getX() < 120;
				mIsRightMenuEnable = event.getX() > 680;

			} else if (event.getAction() == MotionEvent.ACTION_MOVE)
			{
				// 2013-03-13 - raymond - restrict the touch area
				// 2013-5-14 -Amy- decrease touch music control area
				// Log.e("cdf","move:"+event.getX()+"--"+event.getY());
				if (mIsLeftMenuEnable && event.getY() < 390 && event.getY() > 150 && event.getX() < 120
						&& event.getX() > 0)
				{
					// 2013-5-14 -Amy- decrease touch music control
					// area
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
								mBmapLeftBtnVolumeIdx.getWidth(), mBmapLeftBtnVolumeIdx.getHeight(), m,
								true);
						mLeftBtnVolumeIdx.setImageBitmap(bmap);

						mLeftBtnVolumeIdx.setX(x - (int) (mLeftBtnVolumeIdx.getWidth() / 2f));
						mLeftBtnVolumeIdx.setY(y - (int) (mLeftBtnVolumeIdx.getHeight() / 2f));

						int maxVolume = mAudioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
						int volume = (int) (maxVolume * (1 - ((atanAngle.floatValue() * 180 / 3.1417f) + 90) / 180f));
						// 2013-03-26 - raymond - android has bug on
						// mute. we use volum instead
						currentVolume = volume;
						mLeftBtnVolume.setImageBitmap(mBmapLeftBtnVolumeOn);
						// set volume
						//Log.d("mLeftImgViewBase", y + " vs mleft:" + mLeftImgViewBase.getY());
						mAudioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

						//Log.d("gesture", "gesture left menu: action move - angle:" + atanAngle * 180
						//		/ 3.14f + " x:" + x + " y:" + y);
					}
				}
				if (mIsRightMenuEnable && event.getY() < 390 && event.getY() > 150 && event.getX() > 680
						&& event.getX() < 800)
				{
					//
					float x0 = 800, y0 = 270;
					float x1 = event.getX(), y1 = event.getY();
					float avg = (float) Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
					if (60 <= avg && avg <= 120)
					{

						double atanAngle = getRightAtanAngle(event.getX(), event.getY());// *
						int x = 800 + (int) (-90 * Math.cos(atanAngle));
						int y = 222 + (int) (-90 * Math.sin(atanAngle)) + Y_ADJUSTMENT;
						float angle = (float) (atanAngle * 180 / Math.PI);

						mRightImgViewFrontBar.setRotation(-90 + angle);
						//Log.e("gesture", "gesture right mRightImgViewFrontBar atan: =" + atanAngle
						//		+ "angle:===" + angle + " x:=" + event.getX() + " y:= " + event.getY());

						Matrix m = new Matrix();
						m.setRotate(angle);
						Bitmap bmap = Bitmap.createBitmap(mBmapRightBtnProgressIdx, 0, 0,
								mBmapRightBtnProgressIdx.getWidth(), mBmapRightBtnProgressIdx.getHeight(),
								m, true);
						mRightBtnProgressIdx.setImageBitmap(bmap);

						mRightBtnProgressIdx.setX(x - (int) (mRightBtnProgressIdx.getWidth() / 2f));
						mRightBtnProgressIdx.setY(y - (int) (mRightBtnProgressIdx.getHeight() / 2f));

						//Log.e("gesture", "gesture right menu: action move - angle:" + atanAngle * 180
						//		/ 3.1417f + " x:" + x + " y:" + y);

					} else
					{
						return false;
					}
				}
			}
			// }
			mGuestureDetector.onTouchEvent(event);
			return false;
		}
	}
	
	private class MusicControllListener implements IPlayerController.OnMediaStatusChanged {

		@Override
		public void onPrepared(String albumPath) {
			if (mPath != null && !mPath.equals(""))
			{
				mRender.setContentPath(mPath);
				mRender.reloadMusicInfo(mController.getSongFileList());
				// initProgressCaptureThread();
			}
			
		}

		@Override
		public void onInitialized(String songPath, int songIndex) {
			showingOnInitialized();
			
		}

		@Override
		public void albumChanged(String albumPath) {
			getIntent().getStringExtra("albumPath");
			
		}

		@Override
		public void onSeek(int whereTo) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStarted() {
			invidateViewStarted();
			
		}

		@Override
		public void onPaused() {
			invidateViewPaused();
			
			
		}

		@Override
		public void allListEnd() {
			invidateViewPaused();
			
		}

		@Override
		public void onInitializedFailed() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMediaDied() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
