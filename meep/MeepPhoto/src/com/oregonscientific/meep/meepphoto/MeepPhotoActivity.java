package com.oregonscientific.meep.meepphoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.opengl.OpenGlRender;
import com.oregonscientific.meep.opengl.OpenGlRender.OspadRenderListener;
import com.oregonscientific.meep.opengl.SnackShapeCtrl.SnackCtrlState;
import com.oregonscientific.meep.opengl.StateManager.SystemState;

public class MeepPhotoActivity extends Activity implements OnGestureListener {

	private GestureDetector mGuestureDetector; // touching event handler
	private ScaleGestureDetector mScaleGuestureDetector;
	OpenGlRender mRender;
	GLSurfaceView mGlView; // open gl surface view

	Handler mHandlerUpdateUI = null;

	ImageView mFullListIcon = null;

	private boolean isGlReady = false;

	private TextView mTextViewTitle = null;
	private ImageView mUiBackLight = null;

	private float mInitX;
	private float mInitY;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initOpenglRender();
		initComponent();

		mRender.setOnOspadRenderListener(new OspadRenderListener() {

			@Override
			public void OnSystemStateChanged(SystemState state) {
				// TODO Auto-generated method stub

			}

			@Override
			public void OnSurfaceCreated() {
				mRender.setSnackCtrlState(SnackCtrlState.LEVEL1_TO_4C);
				isGlReady = true;
			}

			@Override
			public void OnItemSelectedChanged(OSButton item) {
				String title = item.getName();
				Message msg = new Message();
				msg.what = 1;
				Bundle b = new Bundle();
				b.putString("title", title);
				msg.setData(b);
				mHandlerUpdateUI.sendMessage(msg);

			}
		});

		mHandlerUpdateUI = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// update title
					String title = msg.getData().getString("title");
					// mTextViewTitle.setText(title);
					updateTitle(title);
				}

				super.handleMessage(msg);
			}
		};

	}
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// If used,power off the screen and should not running onDrawFrames but load from beginning when power on again.  
		// write by Aaron 
		mGlView.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mGlView.onPause();
	}



	private float mInitSpan = 0;

	private void initOpenglRender() {
		try {
			mGlView = (GLSurfaceView) findViewById(R.id.surfaceViewOpenGl);
			mGuestureDetector = new GestureDetector(this);
			mScaleGuestureDetector = new ScaleGestureDetector(this, new OnScaleGestureListener() {
				@Override
				public void onScaleEnd(ScaleGestureDetector detector) {
					Log.d("onScale", "onScale end");
					if (detector.getCurrentSpan() - mInitSpan > 100) {
						Log.d("onScale", "onScale end - zoom in");
					} else if (detector.getCurrentSpan() - mInitSpan < -100) {
						goToFullListView();
						// Log.d("onScale", "onScale end - zoom out");
					}
				}

				@Override
				public boolean onScaleBegin(ScaleGestureDetector detector) {
					Log.d("onScale", "onScale begin");
					mInitSpan = detector.getCurrentSpan();
					return true;
				}

				@Override
				public boolean onScale(ScaleGestureDetector detector) {
					Log.d("onScale", "onScale" + detector.getCurrentSpan());
					return false;
				}
			});
			mRender = new OpenGlRender(this);
			Bitmap bmpBg = BitmapFactory.decodeResource(getResources(), R.drawable.os_photo_bg_l);
			Bitmap bmpDummy = BitmapFactory.decodeResource(getResources(), R.drawable.photo_dummy);
			Bitmap bmpPreviewBg =  BitmapFactory.decodeResource(getResources(), R.drawable.photo_downcover);
			Bitmap bmpPrevieTop =  BitmapFactory.decodeResource(getResources(), R.drawable.photo_upcover);
			mRender.initRender(AppType.Photo, bmpBg, bmpDummy, bmpPreviewBg, bmpPreviewBg, bmpPreviewBg, bmpPrevieTop);
			mRender.setOnOspadRenderListener(new OpenGlRender.OspadRenderListener() {

				@Override
				public void OnSystemStateChanged(SystemState state) {
					// TODO Auto-generated method stub

				}

				@Override
				public void OnSurfaceCreated() {
					// TODO Auto-generated method stub

				}

				@Override
				public void OnItemSelectedChanged(OSButton item) {
					String title = item.getName();
					Message msg = new Message();
					msg.what = 1;
					Bundle b = new Bundle();
					b.putString("title", title);
					msg.setData(b);
					mHandlerUpdateUI.sendMessage(msg);
				}
			});

			if (detectOpenGLES20()) {
				// Tell the surface view we want to create an OpenGL ES
				// 2.0-compatible
				// context, and set an OpenGL ES 2.0-compatible renderer.
				mGlView.setEGLContextClientVersion(2);
				mGlView.setRenderer(mRender);
				mGlView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							// mRender.handleMouseUpEvent();
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						}
						if (isGlReady) {
							mGuestureDetector.onTouchEvent(event);
							mScaleGuestureDetector.onTouchEvent(event);
						}
						return false;
					}
				});
				mGlView.setLongClickable(true);
			} else {
				Log.e("HelloTriangle", "OpenGL ES 2.0 not supported on device.  Exiting...");
				finish();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void updateTitle(String title) {
		if (title != null) {
			if (mUiBackLight.getVisibility() != View.VISIBLE) {
				mUiBackLight.setVisibility(View.VISIBLE);
				mTextViewTitle.setVisibility(View.VISIBLE);
		    
			}
			// set the title name sub the extension;
			if (title.lastIndexOf('.')>=0) {
				title = title.substring(0, title.lastIndexOf('.'));
			}
			if(title.length()>18)
			{
				title = title.substring(0,16) + "..";
			}
			//2013-03-20 - raymond - avoid index of out bound
			// modify by aaronli at Mar 21
			mTextViewTitle.setText(title);

		}
	}

	private void initComponent() {
		initFullListIcon();
		mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
		mUiBackLight = (ImageView) findViewById(R.id.imageViewBackLight);
	}

	private void initFullListIcon() {
		mFullListIcon = (ImageView) findViewById(R.id.imageViewFullList);
		mFullListIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Log.d("mFullListIcon", "on click");
				goToFullListView();
			}
		});
		mFullListIcon.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Log.d("mFullListIcon", "on touch");
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mFullListIcon.setImageResource(R.drawable.full_bk);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					mFullListIcon.setImageResource(R.drawable.full_wt);
				}
				return false;
			}
		});
	}

	private boolean detectOpenGLES20() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}
	
	@Override
	protected void onStop() {
		super.onStop();

		if (mRender != null) {
			mRender.stopLoadingSnakeButtonImages();
		}
	}
	
	private void goToFullListView() {

//		long memory = Debug.getNativeHeapAllocatedSize();
//		long free = Debug.getNativeHeapFreeSize();
//		Log.d("ramtest", "local use:" + memory + " free:" + free);
//
//		Intent myIntent = new Intent(this, FullListViewActivity.class);
//		myIntent.putExtra(Global.STRING_TYPE, "photo");
//
//		try {
//			startActivityForResult(myIntent, 0);
//			finish();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		if (mRender != null) {
			mRender.stopLoadingSnakeButtonImages();
		}
		finish();
	}

	@Override
	public boolean onDown(MotionEvent event) {
		mInitX = event.getX();
		mInitY = event.getY();

		return false;
	}

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		mRender.setFunctionMenuFling(mInitX, mInitY, velocityX, velocityY);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float ditanceX, float distanceY) {
		mRender.setLevel4XScroll(ditanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		OSButton btn = mRender.getSelectedLevel4FuncButton(e.getX(), e.getY());
		String[] paths = null;
		String selectedPath = null;
		if (btn != null) {
			if (btn.getContentPath().size() == 1) {
				paths = mRender.getContentPathPool().toArray(new String[0]);
				selectedPath = btn.getContentPath().get(0);
			}
		}
		if (selectedPath == null) {
			mRender.moveItemToSelected(e.getX(), e.getY());
			// Toast.makeText(this,
			// "cannot get the path of the selected os button",
			// Toast.LENGTH_SHORT);
		} else {
			goToPhoto(paths, selectedPath);
			Log.i("path", "path:" + selectedPath);
		}

		return false;
	}

	private void goToPhoto(String[] paths, String selectedPath) {

		Intent intent = new Intent(this, MeepPhotoViewPagerActivity.class);
		// intent.setComponent(ComponentName.unflattenFromString("com.oregonscientific.meep.photozoom/.PhotoZoomActivity"));
		// intent.addCategory("android.intent.category.LAUNCHER");
		intent.putExtra(Global.STRING_TYPE, "photo");
		intent.putExtra(Global.STRING_PATH, selectedPath);
		intent.putExtra(Global.STRING_LIST, paths);

		try {
			startActivity(intent);
		} catch (Exception ex) {
			Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT);
			Log.e("meepPhoto", "go to photo:" + ex.toString());
		}
	}

}