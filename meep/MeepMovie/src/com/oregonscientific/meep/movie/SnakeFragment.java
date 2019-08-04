package com.oregonscientific.meep.movie;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyEditText;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessageCtrl;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.opengl.BaseSnakeAdapter;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.opengl.OpenGlRender;
import com.oregonscientific.meep.opengl.SnakeAdapter;
import com.oregonscientific.meep.opengl.StateManager.SystemState;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.PermissionManager;

import static com.oregonscientific.meep.movie.ServiceController.isContainBadWord;


public class SnakeFragment extends Fragment implements OnGestureListener{
	
	private GestureDetector mGuestureDetector;  //touching event handler
	private ScaleGestureDetector mScaleGuestureDetector;
	OpenGlRender mRender;
	GLSurfaceView glView;	//open gl surface view
	ViewGroup mRootView;
	Handler mHandlerUpdateUI = null;
	List<OsListViewItem> mListViewItemList = null;
	ImageView mFullListIcon = null;
	
	//private ImageView mImageDropDown;
	//private LinearLayout mLayoutDropDown;
	private TextView mTextViewTitle = null;
	//private ImageView mUiBackLight = null;
	
	boolean isGlReady = false;
	String[] blackListWord;
	private Bitmap mBmpBg;
	private Account mAccount;
	private float mInitX;
	private float mInitY;
	
	private FullListViewActivity mContext;
	BadWordChecker badWordChecker = new BadWordChecker();
	//2013-5-23 -Amy- add pupupwindow for rename and delete item
		private PopupWindow popWindow = null;
		private View ringMenuShow = null;
		private RelativeLayout mPopupLayout = null;
		private RelativeLayout popupLayout;
		
		private EditText mRenameText;
		private NotificationMessage notification;
	
	public static SnakeFragment newInstance(String tag) {
		SnakeFragment f = new SnakeFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("tag", tag);
        f.setArguments(args);

        return f;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContext = (FullListViewActivity) getActivity();
		View v = inflater.inflate(R.layout.main, container, false);
		//Log.e("cdf","snake-------------------------------------");
		initOpenglRender(v);
        initComponent(v);
        
		// 2013-6-13 - Zoya - listener to sdcard insert and pull out events.
		registerSDCardListener();
      		
        mHandlerUpdateUI = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				//Log.d("aaron", "return message "+msg.getData().getString("title"));
				if(msg.what == 1){
					//update title
					String title = msg.getData().getString("title");
					//mTextViewTitle.setText(title);
					updateTitle(title);
				}
				
				super.handleMessage(msg);
			}
        };
        mRootView = (ViewGroup) v;
        ServiceController.getAccount(getActivity());

		if (ServiceController.getUser() == null) {
	        ServiceController.getAccount(getActivity());
		}
		return v;
	}

	// 2013-6-13 - Zoya - SDCardListener class
	private final BroadcastReceiver sdcardListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("TAG", "sdcard action----" + action);
			if (action.equals("android.intent.action.MEDIA_MOUNTED")) {// insert sdcard
				mListViewItemList = DataSourceManager.getAllItems(true);
				mRender.changedDataItem();
			} else if (action.equals("android.intent.action.MEDIA_REMOVED")// pull out sdcard
					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")) {
				mListViewItemList = DataSourceManager.getAllItems(true);
				mRender.changedDataItem();
			}
		}
	};

	// 2013-6-13 - Zoya - registerSDCardListener
	private void registerSDCardListener() {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addDataScheme("file");
		mContext.registerReceiver(sdcardListener, intentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(sdcardListener);
		super.onDestroy();
	}
	/* (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//glView.onResume();
	}

	@Override
	public void onPause() {
		//mRender.onSurfacePause();
		//glView.onPause();
		super.onPause();
	}
	
    private float mInitSpan = 0;
    
    private void initOpenglRender(View v)
    {
    	try {
    		glView = (GLSurfaceView)v.findViewById(R.id.surfaceViewOpenGl);
    		mGuestureDetector = new GestureDetector(this);
    		mScaleGuestureDetector = new ScaleGestureDetector(this.getActivity(), new OnScaleGestureListener() {
				@Override
				public void onScaleEnd(ScaleGestureDetector detector) {
					//Log.d("onScale", "onScale end");
					if (detector.getCurrentSpan() - mInitSpan > 100) {
						//Log.d("onScale", "onScale end - zoom in");
					} else if (detector.getCurrentSpan() - mInitSpan < -100) {
						goToFullListView();
						// Log.d("onScale", "onScale end - zoom out");
					}
				}
				
				@Override
				public boolean onScaleBegin(ScaleGestureDetector detector) {
					//Log.d("onScale", "onScale begin");
					mInitSpan =  detector.getCurrentSpan();
					return true;
				}
				
				@Override
				public boolean onScale(ScaleGestureDetector detector) {
					Log.d("onScale", "onScale" + detector.getCurrentSpan());
					return false;
				}
			});
    		mRender = new OpenGlRender(this.getActivity());
    		Bitmap bmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.meepmovie_video_bg);
    		Bitmap bmapDummy = BitmapFactory.decodeResource(getResources(), R.drawable.movie_mirror_dummy);
    		Bitmap bmapPreviewBg = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
    		mBmpBg = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
			//mRender.initRender(AppType.Movie,this, bmapBg, bmapDummy, null, bmapPreviewBg);
    		//mRender.setRenderType(AppType.Movie);
    		
    		SnakeAdapter<OsListViewItem> adapter = new MySnakeAdapter(DataSourceManager.getAllItems(false));
    		// add default album on meepmovie.
    		mRender.initRender(AppType.Movie, adapter, bmapBg, bmapDummy, mBmpBg, bmapPreviewBg);
			mRender.setOnOspadRenderListener(new OpenGlRender.OspadRenderListener() {
				@Override
				public void OnSystemStateChanged(SystemState state) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnSurfaceCreated() {
					
					//mRender.setSnackCtrlState(SnackCtrlState.LEVEL1_TO_4C);
					isGlReady = true;
					
				}
				// Modified by aaronli may21 2013, for the new snack view about adapter.
				private void OnItemSelectedChanged(OsListViewItem item) {
					//Log.d("aaron", "OnItemSelectedChanged");
					String title = null;
					if (item != null) {
						title = ((OsListViewItem)item).getName();
					}
					Message msg = new Message();
					msg.what = 1;
					Bundle b = new Bundle();
					b.putString("title", title);
					msg.setData(b);
					mHandlerUpdateUI.sendMessage(msg);
				}

				@Override
				public void OnItemSelectedChanged(Object itemData) {
					OnItemSelectedChanged((OsListViewItem)itemData);
					
				}
				// end 

				@Override
				public void onLoadingButtonList(OSButton view) {
					loadingButtonImage(view);
				}
			});
			
			if (detectOpenGLES20()) {
				// Tell the surface view we want to create an OpenGL ES
				// 2.0-compatible
				// context, and set an OpenGL ES 2.0-compatible renderer.
				glView.setEGLContextClientVersion(2);
				glView.setRenderer(mRender);
				glView.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							// added by aaronli at Jun26 2013. fixed #4539
							mRender.handleMouseUpEvent(event.getX(), event.getY());
							// fixed #4539 end
						}
						else if(event.getAction() == MotionEvent.ACTION_MOVE)
						{
//							if(isDropDownButtonHold)
//							{
//								moveDropDownView(event);
//							}
						}
						if (isGlReady) {
							mGuestureDetector.onTouchEvent(event);
							mScaleGuestureDetector.onTouchEvent(event);
						}
						return false;
					}
				});
				glView.setLongClickable(true);
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
    	//2013-6-24 - Zoya - fixed popup "Unfortunately, MeepMovie has stopped" when delete all contents.
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
    }
    
	private void initFullListIcon(View v) {
		mFullListIcon = (ImageView) v.findViewById(R.id.imageViewFullList);
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
		ActivityManager am = (ActivityManager) this.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
  		
  		//checkDropDownButtonHold();
  		
		return false;
	}
	


	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
		mRender.setFunctionMenuFling(mInitX,mInitY, velocityX, velocityY);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepMovie-onLongPress","DISABLE_RING_MENU");
			return;
		}		
		// TODO Auto-generated method stub
		//mRender.changedDataItem();
		Log.e("cdf",""+arg0.getX()+" -- "+arg0.getY());
		//2013-5-21 -Amy- add pupupwindow for rename and delete item
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		ImageView btnRename;
		ImageView btnDelete;
		int xx = (int) arg0.getX();
		int yy = (int) arg0.getY();
		if(DataSourceManager.getAllItems(false).size()>0 && DataSourceManager.getAllItems(false)!=null){
		//2013-6-24 -Amy- The RingMenu can be shown only tab the selected item
		if(xx>=480&&xx<=800 && yy>=180&&yy<=430){
		LayoutInflater layoutInflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_rename_delete, null);
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
			//Log.e("cdf", "230");
		}
		if (xx > 600)
		{
			xx = 600;
			//Log.e("cdf", "700");
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
		popWindow.showAtLocation(glView, Gravity.START, xx, yy);
		//ringMenuHandler(left, buttonNum, item);
		
		final OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.imageViewQuit:
					hidePopupMessageLayout();
					//mPopupLayout.setVisibility(View.GONE);
					break;
				case R.id.textViewOkBtn:
					mRender.renameSelectedItem();
					//hidePopupMessageLayout();
					break;
				case R.id.textViewNoBtn:
					mRootView.removeView(popupLayout);
					break;
				case R.id.imageViewQuitDelete:
					mRootView.removeView(mPopupLayout);
					break;
				case R.id.textViewYesBtn:
					mRender.deleteSelectedItem();
					mRootView.removeView(popupLayout);
					break;
				}
			}
		};
//		2013-6-13 -Amy- delete function: can not rename system file 
//		final OsListViewItem btn = mRender.getSelectedLevel4FuncButton(xx, yy);
//		String path = null;
//		if (btn != null && (path = btn.getPath()) != null) {
			btnRename.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if((btn.getPath()).contains("/data/home/movie/data/")){
						//Toast.makeText(getActivity(), "It is system file ,can not be renamed .", 0).show();
//						String renameErr = (String) getResources().getString(R.string.music_title_rename_error);
//						String msg_system_file = (String) getResources().getString(R.string.msg_system_file_movie);
//						notification = new NotificationMessage(mContext, null, renameErr, msg_system_file);
//						hideRingMenu();
//						return;
//					}else{
						hideRingMenu();
						//mViewGroup.removeView(ringMenuShow);
						LayoutInflater inflater = LayoutInflater.from(getActivity());
						mPopupLayout = (RelativeLayout) inflater.inflate(R.layout.layout_popup_text_input, null);
						ImageButton imageViewQuit = (ImageButton) mPopupLayout.findViewById(R.id.imageViewQuit);
						MyButton textViewOkBtn = (MyButton) mPopupLayout.findViewById(R.id.textViewOkBtn);
						MyEditText editTextRename = (MyEditText) mPopupLayout.findViewById(R.id.editTextRename);
						MyTextView textViewNotice = (MyTextView) mPopupLayout.findViewById(R.id.textViewNotice);
						ImageView imageViewEditUserBg = (ImageView) mPopupLayout.findViewById(R.id.imageViewEditUserBg);
						mRenameText = editTextRename;
						
						imageViewQuit.setOnClickListener(clickListener);
						textViewOkBtn.setText(R.string.video_btn_ok);
						textViewOkBtn.setOnClickListener(clickListener);
						//mRenameText = editTextRename;
						textViewNotice.setText(R.string.video_btn_ring_rename);
						imageViewEditUserBg.setOnTouchListener(new OnTouchListener()
						{
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								return true;
							}
						});
						mPopupLayout.setVisibility(View.VISIBLE);
						mPopupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
						//getActivity().addContentView(mPopupLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
						mRootView.addView(mPopupLayout);
						//During renaming, then touch the screen outside the
						// Rename blank, it will enter into corresponding moive or others
						mPopupLayout.setOnTouchListener(new OnTouchListener()
						{
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								//2013-8-9 rename in snake view, tap other places except rename dialog box, the dialog box will disappear
								//mRootView.removeView(mPopupLayout);
								return true;
							}
						});
//					}
				}
			});
//		}
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideRingMenu();
				// TODO Auto-generated method stub
				String delErr = (String)getResources().getString(R.string.delete);
				String delMsg = (String)getResources().getString(R.string.delete_sure);
				//final OsListViewItem mitem = item;
				//notification = new NotificationMessage(mContext, null, delErr, delMsg);
				
				LayoutInflater lf = LayoutInflater.from(getActivity());
				popupLayout = (RelativeLayout) lf.inflate(R.layout.notification_delete, null);
				//2013-7-9 -Amy- add quit button
				ImageButton imageViewQuitDelete = (ImageButton) popupLayout.findViewById(R.id.imageViewQuitDelete);
				MyButton textViewYesBtn = (MyButton) popupLayout.findViewById(R.id.textViewYesBtn);
				MyButton textViewNoBtn = (MyButton) popupLayout.findViewById(R.id.textViewNoBtn);
				MyTextView textViewNotice = (MyTextView) popupLayout.findViewById(R.id.textViewNotice);
				//2013-7-9 -Amy- change popup window text style
				TextView textViewMessage = (TextView) popupLayout.findViewById(R.id.textViewMessage);
				
				textViewNotice.setText(delErr);
				textViewMessage.setText(delMsg);
				textViewYesBtn.setOnClickListener(clickListener);
				textViewNoBtn.setOnClickListener(clickListener);
				imageViewQuitDelete.setOnClickListener(clickListener);

				popupLayout.setVisibility(View.VISIBLE);
				popupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				//mViewGroup.addView(popupLayout);
				//getActivity().addContentView(popupLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				mRootView.addView(popupLayout);
				popupLayout.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return true;
					}
				});
			}
		});
	}
		}
	}
	
	
	private void renameConfirm(final OsListViewItem item) {
		
		boolean isExist = false;
		badWordChecker.setBlacklist(blackListWord);
		String renameErr = (String)getResources().getString(R.string.video_title_rename_error);
		
		//2013-03-22 - raymond - avoid rename system folder
		if(item.getName().equals("data") || item.getName().equals("extsd")){
			//mRenameText.setText("");
			String msg_system_file = (String) getResources().getString(R.string.msg_system_file_movie);
			notification = new NotificationMessage(mContext, null, renameErr, msg_system_file,mRootView);
			return;
		}
		
		//2013-03-20 - raymond - replace all special char
		String tmpName = mRenameText.getText().toString().trim();
		boolean shouldBlock = false;
		
		//2013-6-26 -Amy- rename file's name containing space charector not in first or last
		/*if(tmpName.indexOf(" ") != -1){
			shouldBlock=true;
		}*/
		//2013-6-26 - Zoya - solve #4269
		if (item.getName().equals(tmpName)) {
			// String renameErrMsgInvalidName = (String) getResources()	.getString(R.string.msg_rename_same);
			// notification = new NotificationMessage(mContext, null,renameErr, null);
			mPopupLayout.setVisibility(View.GONE);
			return;
		}
		shouldBlock=MeepStorageCtrl.isRegexBadCharctors(tmpName);
		if(tmpName.equals("")){							
			String renameErrMsgInvalidName = (String)getResources().getString(R.string.msg_null);
			notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mRootView);
			return;
		}else if(shouldBlock){
			String renameErrMsgInvalidName = (String) getResources().getString(R.string.video_msg_blocked);
			notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mRootView);
			return;
		}else{
			mRenameText.setText(tmpName);
		}
		
		if (isContainBadWord(getActivity(), tmpName)) {
			String msg_error = (String) getResources().getString(R.string.msg_error);
			notification = new NotificationMessage(mContext, null, renameErr, msg_error,mRootView);
			return;
		}
		
		if (!tmpName.equals("")) {
			if (DataSourceManager.fileNameisAlreadyExecised(tmpName)){
				String renameErrMsgFormat = (String)getResources().getString(R.string.msg_blocked);
				String renameErrMsg = String.format(renameErrMsgFormat, mRenameText.getText().toString());
				notification = new NotificationMessage(mContext, null, renameErr, renameErrMsg,mRootView);								
				//notification = new NotificationMessage(mContext, null, "Rename error", "This destination already contains a file named \"" + mRenameText.getText().toString() + "\". \nPlease check it before renaming this file.");
			}else if (tmpName.charAt(0) == '.'){
				String renameErrMsgInvalidName = (String)getResources().getString(R.string.video_msg_name_invalid);
				notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mRootView);									
				//notification = new NotificationMessage(mContext, null, "Rename error", "The file name cannot start with \".\", please change the file name");			
			}
			/*else if (!badWordChecker.isStringSafe(tmpName)){
			else if (!badWordChecker.isStringSafe(mRenameText.getText().toString())){
			/*else if (!badWordChecker.isStringSafe(mRenameText.getText().toString())){
				String renameErrMsgInvalidName = (String)getResources().getString(R.string.msg_error);
				notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mRootView);		
			} */
			
			
			else {
				//String renameFileNameHasSpace = mRenameText.getText().toString();
				//2013-6-26 -Amy- rename file's name containing space charector not in first or last
				//String renameFileName = renameFileNameHasSpace.trim();
				//String pathTo = item.getPath().substring(0,item.getPath().length() - item.getName().length() - 4) + renameFileName + item.getPath().substring(item.getPath().lastIndexOf("."));
				DataSourceManager.renameFile(tmpName, item);
				//2013-3-16 -Amy- Rename after refresh
				item.setName(tmpName);
				//item.setPath(pathTo);
			}
		}
		//hidePopupMessageLayout();
	}
	private void hidePopupMessageLayout() {
		// 2013-03-25 - raymond - dismiss rename keyboard
		if (mRenameText != null)
		{
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mRenameText.getWindowToken(), 0);
		}
		((ViewGroup) getView()).removeView(mPopupLayout);
		mPopupLayout = null;
	}
	private void hideRingMenu() {
		if (popWindow != null && popWindow.isShowing())
		{
			popWindow.dismiss();
		}
		ringMenuShow = null;
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
		//Log.i("meepMovie", "onSingleTapUp");
		// Modified by aaronli at May21 2013.For the new snack view with adapter.
		OsListViewItem btn = mRender.getSelectedLevel4FuncButton(e.getX(), e.getY());
		String path = null;
		/*if (btn != null) {
			if (btn.getContentPath().size() == 1) {
				path = btn.getContentPath().get(0);
			}
		}
		if (path.equals("")) {
			//Toast.makeText(this, "cannot get the path of the selected os button", Toast.LENGTH_SHORT);
			mRender.moveItemToSelected(e.getX(), e.getY());
		} else {
			goToMovie(path);
		}*/
		if (btn != null && (path = btn.getPath()) != null) {
		/*	Bitmap mBitmap= ThumbnailUtils.createVideoThumbnail(btn.getPath(), Thumbnails.MINI_KIND);
			if (mBitmap!=null)
			{
				goToMovie(path);
			}
			else
			{
				//Toast.makeText(mContext, "���ϸ��ļ�", 5000).show();
				int framework_err = 0;
				int messageId;
		    	 if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
		                messageId = android.R.string.VideoView_error_text_invalid_progressive_playback;
		            } else {
		                messageId = android.R.string.VideoView_error_text_unknown;
		            }
				new AlertDialog.Builder(mContext)
		        .setTitle(android.R.string.VideoView_error_title)
		        .setMessage(messageId)
		        .setPositiveButton(android.R.string.VideoView_error_button,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int whichButton) {
		                         If we get here, there is no onError listener, so
		                         * at least inform them that the video is over.
		                         
		                        //finish();
		                    }
		                })
		        .setCancelable(false)
		        .show();
			}*/
			goToMovie(path);
		} else {
			mRender.moveItemToSelected(e.getX(), e.getY());
		}
		// end Modified
		return false;
	}

	private void goToMovie(String path) {
//		Intent intent = new Intent(this, FullListViewActivity.class);
//		intent.setComponent(ComponentName.unflattenFromString("com.oregonscientific.meep.movieplayer/com.oregonscientific.meep.movieplayer.MoviePlayerActivity"));
//		intent.addCategory("android.intent.category.LAUNCHER");
		Intent intent = new Intent(this.getActivity(), MoviePlayerActivity.class);
		intent.putExtra(Global.STRING_MOVIE_PATH, path);
		try {
			startActivity(intent);
            
            MeepLogger meepLogger = new MeepLogger(this.getActivity());
            meepLogger.p("was playing movie: " + path);
		} catch (Exception ex) {
			Toast.makeText(this.getActivity(),ex.toString(), Toast.LENGTH_SHORT);
			Log.e("meepMovie", "go to movie:" + ex.toString());
		}
	}

	public List<OsListViewItem> loadDataSource() {
		List<File> fileList = DataSourceManager.scanDirectList();
		List<OsListViewItem> items = new Vector<OsListViewItem>();
		DataSourceManager.getDataSource(fileList, items);
	
		return items;
		
	}


	public void loadingButtonImage(OSButton button) {
		
		// To prevent the none pointer exception
		// modified by aaronli Jun11 2013
		if (button == null || button.getContentPath() == null || button.getContentPath().size()<=0) {
			return;
		}
		if (button.getTextureBmp() != null && button.getDimTextureBmp() != null) {
			return ;
			
		}


		DataSourceManager.loadingMovieButtonImages(button, mBmpBg);
	
	}
	
	// new snackAdapter to do onDelectedItem and onRenameItem
	private class MySnakeAdapter extends BaseSnakeAdapter{
		public MySnakeAdapter(List<OsListViewItem> data) {
			super(data);
		}
		@Override
		public void onDelectedItem(OsListViewItem deleteItem) {
			DataSourceManager.deleteItemFiles(deleteItem);
			DataSourceManager.deleteDataItems(deleteItem);
			
			//popupLayout.setVisibility(View.GONE);
			mRootView.removeView(mPopupLayout);
			//movieAdapter.notifyDataSetChanged();
		}
		@Override
		public void onRenameItem(OsListViewItem deleteItem) {
			if (deleteItem != null) {
				renameConfirm(deleteItem);
			}
			hidePopupMessageLayout();
		}
	}
	private void getMovieBlackList()
	{
		String sql = "select * from " + TableBlacklist.S_TABLE_NAME +  " where " + 
				//TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Photo + "'" ;
					TableBlacklist.S_LIST_TYPE + " = 'browser'" ;
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_BLACK_LIST, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		mContext.sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}

}

