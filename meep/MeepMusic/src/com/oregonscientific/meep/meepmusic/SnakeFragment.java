package com.oregonscientific.meep.meepmusic;

import static com.oregonscientific.meep.meepmusic.ServiceController.isContainBadWord;
import java.io.File;
import java.util.List;
import java.util.Vector;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyEditText;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.BaseSnackAdapter;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.opengl.OpenGlRender;
import com.oregonscientific.meep.opengl.SnackAdapter;
import com.oregonscientific.meep.opengl.StateManager.SystemState;

public class SnakeFragment extends Fragment implements OnGestureListener{
	private GestureDetector mGuestureDetector;  //touching event handler
	//private ScaleGestureDetector mScaleGuestureDetector;
	OpenGlRender mRender;
	ViewGroup mRootView;
	GLSurfaceView mGlView;	//open gl surface view
	String[] blackListWord;
	BadWordChecker badWordChecker = new BadWordChecker();


	private FullListViewActivity mContext;
	Handler mHandlerUpdateUI = null;
	
	ImageView mFullListIcon = null;
	List<OsListViewItem> mListViewItemList = null;
	private TextView mTextViewTitle = null;
	//private ImageView mUiBackLight = null;
	
	private float mInitX;
	private float mInitY;
	private float mInitSpan = 0;
	private Bitmap bmapPreviewBg;
	private Bitmap bmapPreviewTop;
	
	boolean isGlReady = false;
		
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		
		mContext = (FullListViewActivity) getActivity();
		View v = inflater.inflate(R.layout.main, container, false);
		
		initOpenglRender(v);
	    initComponent(v);
	     
		// 2013-6-11 - Zoya - Listener to insert and pull out the sdcard events.
		registerSDCardListener();
	  		
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
        mRootView = (ViewGroup) v;
		return v;
	}
	
	// 2013-6-13 - Zoya - SDCardListener class
	private final BroadcastReceiver sdcardListener = new BroadcastReceiver() {
		@Override
        public void onReceive(Context context, Intent intent) {            
            String action = intent.getAction();
            Log.d("tag", "sdcard action----" + action);
            if(action.equals("android.intent.action.MEDIA_MOUNTED")){//mount SDcard success
            	mListViewItemList = DataSourceManager.getAllItems(true);
				mRender.changedDataItem();               
            } else if(action.equals("android.intent.action.MEDIA_REMOVED")// SDcard is not mounted
					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")){
            	mListViewItemList = DataSourceManager.getAllItems(true);
				mRender.changedDataItem();              
            }                    
        }
	};

	// 2013-6-13 - Zoya - registerSDCardListener
	private void registerSDCardListener() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
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
    
   /* @Override
	protected void onStop() {
		super.onStop();
		if (mRender != null) {
			mRender.stopLoadingSnakeButtonImages();
		}
	}*/
    
    @Override
	public void onResume() {
		//mGlView.onResume();
		super.onResume();
	}

    // music black in snack view #4366 #4365
    // modified by aaronli Jun11 2013
	@Override
	public void onPause() {
		//mGlView.onPause();
		super.onPause();
		// added by aaronli at May30. setting on gl surface pause.
		//mRender.onSurfacePause();
	}

	private void initOpenglRender(View v)
    {
    	try {
    		mGlView = (GLSurfaceView)v.findViewById(R.id.surfaceViewOpenGl);
    		mGuestureDetector = new GestureDetector(this);

			mRender = new OpenGlRender(this.getActivity());
			Bitmap bmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.os_music_bg_l);
			Bitmap bmapDummy = BitmapFactory.decodeResource(getResources(), R.drawable.music_dummy);
			Bitmap bmapDefaultCd = BitmapFactory.decodeResource(getResources(), R.drawable.default_cd_cover);
			Bitmap bmapDefaultCdDim = BitmapFactory.decodeResource(getResources(), R.drawable.music_dummy);
			bmapPreviewBg= BitmapFactory.decodeResource(getResources(), R.drawable.default_cd_cover);
			bmapPreviewTop = BitmapFactory.decodeResource(getResources(), R.drawable.music_dummy);
			
			SnackAdapter<OsListViewItem> adapter = new MySnackAdapter(DataSourceManager.getAllItems(false));
			
			mRender.initRender(AppType.Music, adapter, bmapBg, bmapDummy, bmapDefaultCd, bmapDefaultCdDim);
			//Log.d("aaron", "dummy "+bmapDummy.getWidth() +" "+bmapDummy.getHeight());
			mRender.setOnOspadRenderListener(new OpenGlRender.OspadRenderListener() {
				
				@Override
				public void OnSystemStateChanged(SystemState state) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnSurfaceCreated() {
					// TODO Auto-generated method stub
					isGlReady = true;
				}
				
				@Override
				public void OnItemSelectedChanged(Object item) {
					
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
				public void onLoadingButtonList(OSButton button) {
					if (button == null) {
						return;
					}
					if(button.getContentPath() == null || button.getContentPath().size()<=0){
						return;	
					}
					
					if (button.getTextureBmp() != null && button.getDimTextureBmp() != null) {
						return ;
					}
					
					DataSourceManager.loadingSnackTexture(button, bmapPreviewBg);
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
							// added by aaronli at Jun26 2013. fixed #4539
							mRender.handleMouseUpEvent(event.getX(), event.getY());
							// fixed #4539 end
						}
						else if(event.getAction() == MotionEvent.ACTION_MOVE)
						{
						}
						
						if (isGlReady) {
							mGuestureDetector.onTouchEvent(event);
							
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
		/*mFullListIcon.setOnTouchListener(new View.OnTouchListener() {
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
		});*/
	}
    
	private boolean detectOpenGLES20() {
		ActivityManager am = (ActivityManager) this.getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}

    
	private void goToFullListView() {
//		Intent myIntent = new Intent(this, FullListViewActivity.class);
//		myIntent.putExtra(Global.STRING_TYPE, "music");
//		
//		try {
//			startActivityForResult(myIntent, 0);
//			finish();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		//this.getActivity().finish();
/*		FragmentManager fm = getFragmentManager();
		gridViewFrag = fm.findFragmentById(R.id.gridViewFrag);
		snakeFrag = fm.findFragmentById(R.id.snakeFrag);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
        ΪFragment���õ��뵭��Ч��Android��������ʾ������}�����Դ��android�ڲ���Դ���������ֶ����塣
        ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out); 

        if (snakeFrag.isHidden()) {
            ft.show(snakeFrag);
            ft.hide(gridViewFrag);
        } else {
            ft.hide(snakeFrag);
            ft.show(gridViewFrag);
        }
        ft.commit();*/
		mContext.showDetails(FullListViewActivity.STATE_FRAG_GRID);
	}
	
	private void renameConfirm(final OsListViewItem item) {
		boolean isExist = false;
		badWordChecker.setBlacklist(blackListWord);

		String renameErr = (String) getResources().getString(R.string.music_title_rename_error);

		// 2013-03-22 - raymond - avoid rename system folder
		if (item.getName().equals("data") || item.getName().equals("extsd")|| item.getName().equals("sdcard1")|| item.getName().equals("sdcard"))
		{
			String msg_system_file = (String) getResources().getString(R.string.msg_system_file);
			notification = new NotificationMessage(mContext, null, renameErr, msg_system_file,mRootView);
			return;
		}

		// 2013-03-22 - raymond - replace all special char
		String tmpName = mRenameText.getText().toString().trim();
		boolean shouldBlock = false;
		//2013-6-26 - Zoya - solve #4269
		if (item.getName().equals(tmpName)) {
			// String renameErrMsgInvalidName = (String) getResources()	.getString(R.string.msg_rename_same);
			// notification = new NotificationMessage(mContext, null,renameErr, null);
			mPopupLayout.setVisibility(View.GONE);
			return;
		}
		if (isContainBadWord(getActivity(), tmpName)) {
			String msg_error = (String) getResources().getString(R.string.msg_error);
			notification = new NotificationMessage(mContext, null, renameErr, msg_error,mRootView);
			return;
		}
		
		//2013-4-8 - Zoya - Solve the issue of #4085
		//2013-6-26 -Amy- rename file's name containing space charector not in first or last
		/*if (tmpName.indexOf(" ") != -1){
			shouldBlock = true;
		}*/
		shouldBlock=MeepStorageCtrl.isRegexBadCharctors(tmpName);
		if (tmpName.equals(""))
		{
			String renameErrMsgInvalidName = (String) getResources().getString(R.string.msg_null);
			notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mRootView);
			return;
		} else if (shouldBlock){
			String renameErrMsgInvalidName = (String) getResources().getString(R.string.music_msg_blocked);
			notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mRootView);
			return;
		} else{
			mRenameText.setText(tmpName);
		}

		if (!mRenameText.getText().toString().equals(""))
		{
			// renamePhoto(renameFileName+".png", item);
			

			if (DataSourceManager.fileNameisAlreadyExecised(tmpName))
			{
				String renameErrMsgFormat = (String) getResources().getString(R.string.msg_blocked);
				String renameErrMsg = String.format(renameErrMsgFormat, mRenameText.getText().toString());
				notification = new NotificationMessage(mContext, null, renameErr, renameErrMsg,mRootView);
				// notification = new NotificationMessage(mContext,
				// null, "Rename error",
				// "This destination already contains a file named \""
				// + mRenameText.getText().toString() +
				// "\". \nPlease check it before renaming this file.");
			} else if ((mRenameText.getText().toString().substring(0, 1)).equals("."))
			{
				String renameErrMsgInvalidName = (String) getResources().getString(
						R.string.music_msg_name_invalid);
				notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mRootView);
				// notification = new NotificationMessage(mContext,
				// null, "Rename error",
				// "The file name cannot start with \".\", please change the file name");
			}
			/*else if (!badWordChecker.isStringSafe(mRenameText.getText().toString())){
				String renameErrMsgInvalidName = (String)getResources().getString(R.string.msg_error);
				notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mRootView);		
			} */
			else
			{
				// 2013-3-14 - Amy - GridView replace ListView
				String renameFileNameHasSpace = mRenameText.getText().toString();
				//2013-6-26 -Amy- rename file's name containing space charector not in first or last
				String renameFileName = renameFileNameHasSpace.trim();
				/*String pathTo = item.getPath().substring(0,
						item.getPath().length() - item.getName().length() - 1)
						+ renameFileName + "/";*/
				// Log.e("cdf","music pathTo--------"+pathTo);
				DataSourceManager.renameFile(renameFileName, item);
/*				item.setName(renameFileName);
				item.setPath(pathTo);*/
				// renameFile(mRenameText.getText().toString(),
				// item);
				// redrawListView();
			}
		}
		
	}
    
	/*
	 * //2013-03-26 - raymond - remove Jump to gridview feature //2013-4-2 -Amy-
	 * when click back, Jump to gridview --add mPopupLayout != null && public
	 * void onBackPressed() { if(mPopupLayout != null &&
	 * mPopupLayout.isShown()){ Log.e("cdf","back pressed ! ");
	 * mViewGroup.removeView(mPopupLayout);
	 * //mViewGroup.removeViewInLayout(mPopupLayout); }else{
	 * super.onBackPressed(); } return; }
	 */

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

	@Override
	public boolean onDown(MotionEvent event) {
  		mInitX = event.getX();
  		mInitY = event.getY();
  		
		return false;
	}

	

	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
			float velocityY) {
		mRender.setFunctionMenuFling(mInitX,mInitY, velocityX, velocityY);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepMusic-onLongPress","DISABLE_RING_MENU");
			return;
		}
		Log.e("cdf",""+arg0.getX()+" -- "+arg0.getY());
		//2013-5-21 -Amy- add pupupwindow for rename and delete item
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		ImageView btnRename;
		ImageView btnDelete;
		int xx = (int) arg0.getX();
		int yy = (int) arg0.getY();
		//Log.e("cdf","---"+DataSourceManager.getAllItems(false)+"---"+DataSourceManager.getAllItems(false).size());
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
				Log.e("cdf", "230");
			}
			if (xx > 600)
			{
				xx = 600;
				Log.e("cdf", "700");
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
						popupLayout.setVisibility(View.GONE);
						break;
					case R.id.textViewYesBtn:
						mRender.deleteSelectedItem();
						break;
					}
				}
			};
		
//		2013-6-13 -Amy- delete function: can not rename system file 
//		final OsListViewItem btn = mRender.getSelectedLevel4FuncButton(xx, yy);
//		String path = null;
//		if (btn != null && (path = btn.getPath()) != null) {
//			Log.e("cdf",btn.getPath());
			btnRename.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					if((btn.getPath()).contains("/data/home/music/data/")){
//						//Toast.makeText(getActivity(), "It is system file ,can not be renamed .", 0).show();
//						String renameErr = (String) getResources().getString(R.string.music_title_rename_error);
//						String msg_system_file = (String) getResources().getString(R.string.msg_system_file);
//						notification = new NotificationMessage(mContext, null, renameErr, msg_system_file);
//						hideRingMenu();
//						return;
//					}else{
						// TODO Auto-generated method stub
						hideRingMenu();
						//mViewGroup.removeView(ringMenuShow);
						LayoutInflater inflater = LayoutInflater.from(getActivity());
						mPopupLayout = (RelativeLayout) inflater.inflate(R.layout.layout_popup_text_input, null);
						ImageButton imageViewQuit = (ImageButton) mPopupLayout.findViewById(R.id.imageViewQuit);
						MyButton textViewOkBtn = (MyButton) mPopupLayout.findViewById(R.id.textViewOkBtn);
						EditText editTextRename = (EditText) mPopupLayout.findViewById(R.id.editTextRename);
						MyTextView textViewNotice = (MyTextView) mPopupLayout.findViewById(R.id.textViewNotice);
						ImageView imageViewEditUserBg = (ImageView) mPopupLayout.findViewById(R.id.imageViewEditUserBg);
						mRenameText = editTextRename;
						
						imageViewQuit.setOnClickListener(clickListener);
						textViewOkBtn.setText(R.string.music_btn_ok);
						textViewOkBtn.setOnClickListener(clickListener);
						//mRenameText = editTextRename;
						textViewNotice.setText(R.string.music_btn_ring_rename);
						imageViewEditUserBg.setOnTouchListener(new OnTouchListener()
						{
							@Override
							public boolean onTouch(View v, MotionEvent event) {
								if (popupLayout != null)
									popupLayout.setVisibility(View.GONE);
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
							//	popupLayout.setVisibility(View.GONE);
								//2013-8-2 - Zoya - When renaming, click on the blank area, rename dialog not disappear.
								//mRootView.removeView(mPopupLayout);
								return true;
							}
						});
						//musicAdapter.notifyDataSetChanged();
					//}
				}
			});
			//OsListViewItem item;
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
					textViewYesBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							popupLayout.setVisibility(View.GONE);
							mRender.deleteSelectedItem();
							
						}
					});
					textViewNoBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							popupLayout.setVisibility(View.GONE);
						}
					});
					imageViewQuitDelete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							popupLayout.setVisibility(View.GONE);
						}
					});
					
					popupLayout.setVisibility(View.VISIBLE);
					popupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					mRootView.addView(popupLayout);
					//getActivity().addContentView(popupLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
		OsListViewItem btn = mRender.getSelectedLevel4FuncButton(e.getX(), e.getY());
		String path = null;
		if (btn != null && (path = btn.getPath()) != null) {
			goToMusic(path);
			
		} else {
			Log.d("Snack", "moveItemToSelected");
			mRender.moveItemToSelected(e.getX(), e.getY());
		}
		

		return false;
	}

	private void goToMusic(String path) {
		
//		Intent intent = new Intent(this, FullListViewActivity.class);
//		intent.setComponent(ComponentName.unflattenFromString("com.oregonscientific.meep.musicplayer/com.oregonscientific.meep.musicplayer.MusicPlayerActivity"));
//		intent.addCategory("android.intent.category.LAUNCHER");

		
		Intent intent = new Intent(this.getActivity(), MusicPlayerActivity.class);
		intent.putExtra(Global.STRING_TYPE, "music");
		intent.putExtra(Global.STRING_PATH, path + File.separator);
		try {
			startActivity(intent);
			Log.e("cdf",""+path + File.separator);
            MeepLogger meepLogger = new MeepLogger(this.getActivity());
            meepLogger.p("was playing music album: " + path);
		} catch (Exception ex) {
			//Toast.makeText(this.getActivity(), ex.toString(), Toast.LENGTH_SHORT);
			Log.e("meepMusic", "go to music ", ex);
		}
	}

	private List<OsListViewItem> loadDataSource() {
		List<File> fileList = DataSourceManager.scanDirectList();
		List<OsListViewItem> items = new Vector<OsListViewItem>();
		DataSourceManager.getDataSource(fileList, items);
		return items;
	}
	
	private class MySnackAdapter extends BaseSnackAdapter{

		public MySnackAdapter(List<OsListViewItem> data) {
			super(data);
		}

		@Override
		public void onDelectedItem(OsListViewItem deleteItem) {
			//delete item from database
			if (deleteItem != null) {
				DataSourceManager.deleteItemFiles(deleteItem);
				DataSourceManager.deleteDataItems(deleteItem);
			}
			// redrawListView();
			popupLayout.setVisibility(View.GONE);
		}

		@Override
		public void onRenameItem(OsListViewItem deleteItem) {
			if (deleteItem != null) {
				renameConfirm(deleteItem);
			}
			hidePopupMessageLayout();
		}
		
	}


	/*
	@Override
	public void loadButtonWithDummyImages() {
		
		
	}



	@Override
	public void loadingButtonImage(OSButton button) {
		if (button == null) {
			return;
		}
		
		if (button.getTextureBmp() != null && button.getDimTextureBmp() != null) {
			return ;
		}
		if(button.getContentPath().size()<=0){
			return;	
		}
		
		DataSourceManager.loadingSnackTexture(button, bmapPreviewBg, bmapPreviewTop);
	}*/
	
}