package com.oregonscientific.meep.app;

import java.util.List;
import com.oregonscientific.meep.app.R;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.database.table.MeepDbCommunicationCtrl;
import com.oregonscientific.meep.database.table.TableAppsCategory;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.opengl.BaseSnakeAdapter;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.opengl.OpenGlRender;
import com.oregonscientific.meep.opengl.SnakeAdapter;
import com.oregonscientific.meep.opengl.StateManager.SystemState;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class SnakeFragment extends Fragment implements OnGestureListener{
	private AppManager mAppManager;
	private GestureDetector mGuestureDetector;  //touching event handler
	private ScaleGestureDetector mScaleGuestureDetector;
	OpenGlRender mRender;
	GLSurfaceView mGlView;	//open gl surface view
	private FullListViewActivity mContext;
	Handler mHandlerUpdateUI = null;
	List<OsListViewItem> sAppListItems = null;
	ImageView mFullListIcon = null;
	
	private TextView mTextViewTitle = null;
	//private ImageView mUiBackLight = null;
	private MeepDbCommunicationCtrl mMeepDbCommunicationCtrl = null;
	private BroadcastReceiver mMsgReciver = null;
	
	private float mInitX;
	private float mInitY;
	
	private boolean isGlReady = false;
	
	ViewGroup mRootView;
	/**
	 * 
	 * @param tag save instance value.match the key "tag"
	 * @return
	 * @author aaronli at May22,2013
	 */
	public static SnakeFragment newInstance(String tag) {
		SnakeFragment f = new SnakeFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("tag", tag);
        f.setArguments(args);

        return f;
    }
	
	//2013-5-21 -Amy- add pupupwindow for rename and delete item
	private PopupWindow popWindow = null;
	private View ringMenuShow = null;
	private RelativeLayout mPopupLayout = null;
	private RelativeLayout popupLayout;
	private NotificationMessage notification;
	private EditText mRenameText;
	
	String[] mGameList= null;
	String[] mBlockedAppList = null;
	private Bitmap frameImage;
	private Bitmap mBmpBg ;
	private Bitmap mBmpTop;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	  mContext =(FullListViewActivity)getActivity();
	  View v =inflater.inflate(R.layout.main, container,false);
	  mAppManager = new AppManager(mContext);
      
      mMeepDbCommunicationCtrl = new MeepDbCommunicationCtrl(mContext, AppType.App);
      
      initOpenglRender(v);
      initComponent(v);
//      String[] array = getIntent().getStringArrayExtra(Global.STRING_LIST);
//      mRender.setAppFiltering(array);
      
      // 2013-6-13 - Zoya - Monitor application installation and uninstall events
   	  mInstallReceiver();
      
      mGameList =mContext.getIntent().getStringArrayExtra(Global.STRING_GAME_LIST);
      mBlockedAppList =mContext.getIntent().getStringArrayExtra(Global.STRING_BLOCK_LIST);
      
      mMsgReciver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String[] array = intent.getStringArrayExtra(Global.STRING_LIST);
				//mRender.setAppFiltering(array);
				//Log.d("MeepAppActivity", "onReceive "+ Arrays.toString(array));
				//mRender.setAppItemList(mAppManager.getAllItems(false));
				//mRender.refreshDataSource();
				//mRender.setSnackCtrlState(SnackCtrlState.LEVEL1_TO_4C);
			}
      };
      
      mHandlerUpdateUI = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					//update title
					String title = msg.getData().getString("title");
					//mTextViewTitle.setText(title);
					updateTitle(title);
				}
				
				super.handleMessage(msg);
			}
      };
      
      IntentFilter filter = new IntentFilter();
		filter.addAction(Global.INTENT_MSG_PREFIX + Global.AppType.App);
		mContext.registerReceiver(mMsgReciver, filter);
		
	    mRootView = (ViewGroup)v;
		
	    return v;
	}

	// 2013-6-13 - Zoya - MyInstalledReceiver class
	private final BroadcastReceiver MyInstalledReceiver  = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("tag", "sdcard action----" + action);
			if (action.equals("android.intent.action.PACKAGE_ADDED")) {//install
				sAppListItems = mAppManager.getAllItems(true);
				mRender.changedDataItem();
			} else if (action.equals("android.intent.action.PACKAGE_REMOVED")) {// uninstall  
				sAppListItems = mAppManager.getAllItems(true);
				mRender.changedDataItem();
			}
		}
	};

	private void mInstallReceiver() {
		IntentFilter intentFilter = new IntentFilter(); 
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		intentFilter.addDataScheme("package");
		mContext.registerReceiver(MyInstalledReceiver, intentFilter);
	}


/*  *//** Called when the activity is first created. *//*
	@Override
    public View onCreate(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = Inflate
        
        setContentView(R.layout.main);
        mAppManager = new AppManager(this);
        
        mMeepDbCommunicationCtrl = new MeepDbCommunicationCtrl(this, AppType.App);
        
        initOpenglRender();
        initComponent();
//        String[] array = getIntent().getStringArrayExtra(Global.STRING_LIST);
//        mRender.setAppFiltering(array);
        
        mGameList = getIntent().getStringArrayExtra(Global.STRING_GAME_LIST);
        mBlockedAppList = getIntent().getStringArrayExtra(Global.STRING_BLOCK_LIST);
        
        mMsgReciver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String[] array = intent.getStringArrayExtra(Global.STRING_LIST);
				//mRender.setAppFiltering(array);
				//Log.d("MeepAppActivity", "onReceive "+ Arrays.toString(array));
				//mRender.setAppItemList(mAppManager.getAllItems(false));
				//mRender.refreshDataSource();
				//mRender.setSnackCtrlState(SnackCtrlState.LEVEL1_TO_4C);
			}
        };
        
        mHandlerUpdateUI = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 1){
					//update title
					String title = msg.getData().getString("title");
					//mTextViewTitle.setText(title);
					updateTitle(title);
				}
				
				super.handleMessage(msg);
			}
        };
        
        IntentFilter filter = new IntentFilter();
		filter.addAction(Global.INTENT_MSG_PREFIX + Global.AppType.App);
		registerReceiver(mMsgReciver, filter);
    }
    
     (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 
	@Override
	public void onPause() {
		// add by aaronli at Mar20 2013,refresh the list of apps when installed or uninstalled,it
		// also open gl render was changed paused when the acitivty was not on top of Activity
		//mGlView.onPause();
		super.onPause();
	}
*/
	@Override
	public void onResume() {
		super.onResume();
		// add by aaronli at Mar20 2013,refresh the list of apps when installed or uninstalled,it
		// also resume when the open gl render was pause
		//mGlView.onResume();
		mMeepDbCommunicationCtrl.sendDBQuery("SELECT * FROM " + TableAppsCategory.S_TABLE_NAME, MeepAppMessage.OPCODE_DATABASE_GET_APPS_CATEGORY);
		
		
//		mRender.setOnOspadRenderListener(new OspadRenderListener() {
//			@Override
//			public void OnSystemStateChanged(SystemState state) {
//				
//			}
//			
//			@Override
//			public void OnSurfaceCreated() {
//				mRender.setSnackCtrlState(SnackCtrlState.LEVEL1_TO_4C);
//				mMeepDbCommunicationCtrl.sendDBQuery("SELECT * FROM " + TableAppsCategory.S_TABLE_NAME, MeepAppMessage.OPCODE_DATABASE_GET_APPS_CATEGORY);
//				
//				isGlReady = true;
//			}
//			
//			@Override
//			public void OnItemSelectedChanged(OSButton item) {
//				String title = item.getName();
//				Message msg = new Message();
//				msg.what = 1;
//				Bundle b = new Bundle();
//				b.putString("title", title);
//				msg.setData(b);
//				mHandlerUpdateUI.sendMessage(msg);
//			}
//		});
	}
    
   
	
	 
	@Override
	public void onStop() {
		super.onStop();
		if (mRender != null) {
			mRender.stopLoadingSnakeButtonImages();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mGlView = null;
		mRender = null;
		mContext.unregisterReceiver(MyInstalledReceiver);
		mContext.unregisterReceiver(mMsgReciver);
	}
	
    private float mInitSpan = 0;
    
    private void initOpenglRender(View v)
    {
    	try {
    		mGlView = (GLSurfaceView)v.findViewById(R.id.surfaceViewOpenGl);
    		mGuestureDetector = new GestureDetector(this);
//    		mScaleGuestureDetector = new ScaleGestureDetector(this, new OnScaleGestureListener() {
//				@Override
//				public void onScaleEnd(ScaleGestureDetector detector) {
//					Log.d("onScale", "onScale end");
//					if (detector.getCurrentSpan() - mInitSpan > 100) {
//						Log.d("onScale", "onScale end - zoom in");
//					} else if (detector.getCurrentSpan() - mInitSpan < -100) {
//						goToFullListView();
//						// Log.d("onScale", "onScale end - zoom out");
//					}
//				}
//				
//				@Override
//				public boolean onScaleBegin(ScaleGestureDetector detector) {
//					Log.d("onScale", "onScale begin");
//					mInitSpan =  detector.getCurrentSpan();
//					return true;
//				}
//				
//				@Override
//				public boolean onScale(ScaleGestureDetector detector) {
//					Log.d("onScale", "onScale" + detector.getCurrentSpan());
//					return false;
//				}
//			});
    		Options op = new Options();
    		op.inPreferredConfig = Config.ARGB_8888;
    		Bitmap bgImg = BitmapFactory.decodeResource(getResources(), R.drawable.os_app_bg, op);
			Bitmap dummyImg = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_mask, op);
			mBmpBg = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_mask, op);
    		mBmpTop = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_cover, op);
    		Bitmap defaultHighLight = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_cover, op);
    		Bitmap defaultHighDim = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_cover, op);
			mRender = new OpenGlRender(mContext);
			SnakeAdapter<OsListViewItem> adapter = new MySnakeAdapter(loadDataSource());
			mRender.initRender(AppType.App, adapter, bgImg, dummyImg, defaultHighLight, defaultHighDim);
			
			// modified by aaronli at May22 2013. For the new interface
			mRender.setOnOspadRenderListener(new OpenGlRender.OspadRenderListener() {
				
				@Override
				public void OnSystemStateChanged(SystemState state) {
					
				}
				
				@Override
				public void OnSurfaceCreated() {
		   			//mRender.setAppItemList(mAppManager.getAllItems(false));
					mMeepDbCommunicationCtrl.sendDBQuery("SELECT * FROM " + TableAppsCategory.S_TABLE_NAME, MeepAppMessage.OPCODE_DATABASE_GET_APPS_CATEGORY);
					
					isGlReady = true;
					
				}
				//Marked with by winder
				@Override
				public void OnItemSelectedChanged(Object itemData) {
					String title = null;
					if (itemData != null) {
						title = ((OsListViewItem)itemData).getName();
					}
					Message msg = new Message();
					msg.what = 1;
					Bundle b = new Bundle();
					b.putString("title", title);
					msg.setData(b);
					mHandlerUpdateUI.sendMessage(msg);

				
									
				}

				@Override
				public void onLoadingButtonList(OSButton view) {
					if (view !=null && view.getContentPath() != null && view.getContentPath().size() > 0) {
						mAppManager.loadingAppBitmap(view, frameImage, mBmpBg, mBmpTop);
					}
				}
				// end
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
							// added by aaronli at Jun26 2013. fixed #4539
							mRender.handleMouseUpEvent(event.getX(), event.getY());
							// fixed #4539 end
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
						}
						if (isGlReady) {
							mGuestureDetector.onTouchEvent(event);
							// mScaleGuestureDetector.onTouchEvent(event);
						}
						return false;
					}
				});
				mGlView.setLongClickable(true);
			} else {
				Log.e("HelloTriangle", "OpenGL ES 2.0 not supported on device.  Exiting...");
				this.getActivity().finish();
			}
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
    }
    
    private void updateTitle(String title)
    {
    	/*if (mUiBackLight.getVisibility() != View.VISIBLE) {
			mUiBackLight.setVisibility(View.VISIBLE);
			mTextViewTitle.setVisibility(View.VISIBLE);
		}*/
    	/*if(title != null && title.length()>18)
    	{
    		title = title.substring(0,16) + "..";
    	}*/
    	//2013-6-24 - Zoya - fixed popup "Unfortunately, MeepApp has stopped" when delete all contents.
    	if(title == null){
    		title = " ";
    	}
		mTextViewTitle.setText(title);
    }
    
    private void initComponent(View v)
    {
    	initFullListIcon(v);
    	mTextViewTitle = (TextView)v.findViewById(R.id.textViewTitle);
    	//mUiBackLight = (ImageView)findViewById(R.id.imageViewBackLight);
    	frameImage = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_frame);
    }
    
    
	private void initFullListIcon(View v) {
		mFullListIcon = (ImageView)v.findViewById(R.id.imageViewFullList);
		mFullListIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.w("mFullListIcon", "on click");
				goToFullListView();
			}
		});
		
//		mFullListIcon.setOnTouchListener(new View.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// Log.d("mFullListIcon", "on touch");
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					mFullListIcon.setImageResource(R.drawable.full_bk);
//				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					mFullListIcon.setImageResource(R.drawable.full_wt);
//				}
//				return false;
//			}
//		});
	}
    
	private boolean detectOpenGLES20() {
		ActivityManager am = (ActivityManager)this.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}

    
	private void goToFullListView() {
		mContext.showDetails(FullListViewActivity.STATE_FRAG_GRID);

	}
    
	@Override
	public boolean onDown(MotionEvent event) {
		mInitX = event.getX();
  		mInitY = event.getY();
  		
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		mRender.setFunctionMenuFling(mInitX,mInitY, velocityX, velocityY);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepApp-onLongPress","DISABLE_RING_MENU");
			return;
		}
		
		//mRender.changedDataItem();
		//Log.e("cdf",""+e.getX()+" -- "+e.getY());
		//2013-5-21 -Amy- add pupupwindow for rename and delete item
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		ImageView btnRename;
		ImageView btnDelete;
		int xx = (int) e.getX();
		int yy = (int) e.getY();
		if(mAppManager.getAllItems(false).size()>0 && mAppManager.getAllItems(false)!=null){
			//2013-6-24 -Amy- The RingMenu can be shown only tab the selected item
			if(xx>=480&&xx<=800 && yy>=180&&yy<=430){
			LayoutInflater layoutInflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_delete, null);
			two_button_left = (FrameLayout) ringMenuShow.findViewById(R.id.two_button_left);
			contentLayout = (RelativeLayout) ringMenuShow.findViewById(R.id.contentLayoutleft);
			btnRename = (ImageView) ringMenuShow.findViewById(R.id.btnRename);
			btnDelete = (ImageView) ringMenuShow.findViewById(R.id.btnDelete);
			android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) contentLayout
					.getLayoutParams();
			// change the bottom of ring_menu
			if (yy > 230)
			{
				yy = 230;
			}
			if (xx > 600)
			{
				xx = 600;
			}
			layoutParams.leftMargin = xx;
			layoutParams.topMargin = yy;
			contentLayout.setLayoutParams(layoutParams);
			popWindow.setContentView(ringMenuShow);
			popWindow.update();
			two_button_left.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ringMenuShow.setVisibility(View.GONE);
				}
			});
			popWindow.showAtLocation(mGlView, Gravity.START, xx, yy);
			//ringMenuHandler(left, buttonNum, item);
			final int fx = xx, fy = yy;
			final OnClickListener clickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (v.getId()) {
					case R.id.imageViewQuit:
						//hidePopupMessageLayout();
						mPopupLayout.setVisibility(View.GONE);
						break;
					case R.id.textViewOkBtn:
						//mRender.renameSelectedItem();
						//hidePopupMessageLayout();
						break;
					case R.id.textViewNoBtn:
						popupLayout.setVisibility(View.GONE);
						break;
					case R.id.textViewYesBtn:
						//mRender.deleteSelectedItem();
						OsListViewItem item = mRender.getSelectedLevel4FuncButton(fx, fy);
						if (item != null && item.getPath() != null) {
							uninstallApplication(AppManager.getPackageName(item.getPath()));
							
						}
						//popupLayout.setVisibility(View.GONE);
						
						break;
					}
				}
			};
					btnDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							hideRingMenu();
							OsListViewItem item = mRender.getSelectedLevel4FuncButton(fx, fy);
							if (item != null && item.getPath() != null) {
								uninstallApplication(AppManager.getPackageName(item.getPath()));
								
							}
						}
					});
			}
		}
	}

	private void hideRingMenu() {
		if (popWindow != null && popWindow.isShowing())
		{
			popWindow.dismiss();
		}
		ringMenuShow = null;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		mRender.setLevel4XScroll(distanceX);
		return false;
		}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		OsListViewItem btn = mRender.getSelectedLevel4FuncButton(e.getX(), e.getY());
		/*String[] paths = null;
		String selectedPath = null;
		if (btn != null && btn.getContentPath()!= null) {
			if (btn.getContentPath().size() ==1 ) {
				selectedPath = btn.getContentPath().get(0);
				
			}
			
			if (selectedPath == null) {
				//Toast.makeText(this, "cannot get the path of the selected os button", Toast.LENGTH_SHORT);
			} else {
				goToApp(selectedPath);
			}
		}else
		{
			mRender.moveItemToSelected(e.getX(), e.getY());
		}
		*/
		String path = null;
		if (btn != null && (path = btn.getPath()) != null) {
			goToApp(path);
		} else {
			mRender.moveItemToSelected(e.getX(), e.getY());
		}
		return false;
	}

	private void goToApp(String selectedPath) {
		String appName = getAppNameByPackageName(selectedPath.split("/")[0]);
		
		Intent intent = new Intent(this.getActivity(), FullListViewActivity.class);
		intent.setComponent(ComponentName.unflattenFromString(selectedPath));
		intent.addCategory("android.intent.category.LAUNCHER");
		
		try {
			startActivity(intent);

            MeepLogger meepLogger = new MeepLogger(mContext);
            meepLogger.p("has opened: " + appName + " [" + selectedPath + "]");
            Log.d("openApp", "app name" + appName);
		} catch (Exception ex) {
			Toast.makeText(mContext,ex.toString(), Toast.LENGTH_SHORT);
			Log.e("meepApp", "go to app:" + ex.toString());
		}
	}
	
	private String getAppNameByPackageName(String packageName)
	{
		PackageManager packageManager = this.getActivity().getPackageManager();
		List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {
			if(packageInfo.packageName.equals(packageName))
			{
				return packageInfo.loadLabel(packageManager).toString();
			}
		}
		return null;
	}

	public List<OsListViewItem> loadDataSource() {
		return mAppManager.getAllItems(false);
	}
	
	public void loadButtonWithDummyImages() {
		
	}

	// new snackAdapter to do onDelectedItem and onRenameItem
	private class MySnakeAdapter extends BaseSnakeAdapter{

		public MySnakeAdapter(List<OsListViewItem> data) {
			super(data);
		}

		@Override
		public void onDelectedItem(OsListViewItem deleteItem) {
			//delete item from database
			//AppManager.deleteItemFiles(deleteItem);
			//AppManager.deleteDataItems(deleteItem);
			if (deleteItem != null) {
				uninstallApplication(getPackageName(deleteItem));
			}
			// redrawListView();
			//popupLayout.setVisibility(View.GONE);
		}

		@Override
		public void onRenameItem(OsListViewItem deleteItem) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private void uninstallApplication(String packageName)
	{
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		startActivity(uninstallIntent);
		/*PackageDeleteObserver observer = new PackageDeleteObserver();  
        mContext.getPackageManager().deletePackage(packageName, observer, 0);*/
	}
	private String getPackageName(OsListViewItem item)
	{
		if(item != null)
		{
			String content = item.getPath();
			String[] strArr = content.split("/");
			return strArr[0];
		}
		return null;
	}
}
