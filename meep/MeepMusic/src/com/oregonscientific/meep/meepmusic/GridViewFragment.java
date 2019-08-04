package com.oregonscientific.meep.meepmusic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyEditText;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.message.common.MeepAppMessageCtrl;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.tool.ImageDownloader;
import static com.oregonscientific.meep.meepmusic.ServiceController.*;

public class GridViewFragment extends Fragment {

	/*
	 * private String RENAME = "Rename"; private String ADD = "Add"; private
	 * String DELETE = "Delete";
	 */
	List<File> fileList;
	// String DELETE_TITLE = "Delete Album";
	// String DELETE_MESSAGE = "This album is deleted!";
	// RingMenu ringMenu ;
	private ImageView ring_delete;
	private ImageView ring_rename;
	/*
	 * private ImageView ring_menu_top_right_two; private ImageView
	 * ring_menu_top_left_two; private ImageView ring_menu_bottom_right_two;
	 * private ImageView ring_menu_bottom_left_two;
	 */

	private NotificationMessage notification;
	private String Rename = "";

	private FullListViewActivity mContext;
	private ViewGroup mViewGroup = null;
	String[] blackListWord;
	private BadWordChecker badWordChecker = new BadWordChecker();
	//private static final String BASIC_STORAGE_PATH = "/data/home/";
//	private static final String PATH_MUSIC_SDCARD = "/mnt/sdcard/";
//	private static final String PATH_MUSIC_EXTSD_DIR = "/mnt/extsd/";
//	private static final String PATH_MUSIC_DATA_DIR = BASIC_STORAGE_PATH + "music/data/";
//	private static final String PATH_MUSIC_HOME_DIR = "/mnt/sdcard/home/music/data/";

	private final String BASE_STORAGE_DIR = Environment.getExternalStorageDirectory().getPath();

	// 2013-3-26 -Amy- fix issue # 3163��The default album cover will disappear
	// after rename

	private List<OsListViewItem> mListViewItemList = null;
	private List<Bitmap> mImageList = null;
	private List<String> mNameList = null;
	// 2013-3-14 - Amy - GridView replace ListView
	// LinearLayout mLayout = null;
	// ScrollView mScrollView = null;
	private RelativeLayout mOptionLayout = null;
	private RelativeLayout mRenameLayout = null;
	//private AbsoluteLayout mMainLayout = null;
	private EditText mTxtRename = null;

	private ImageView mSnackIcon = null;

	private Button mBtnConfirm = null;
	private RelativeLayout mPopupLayout = null;
	private EditText mRenameText = null;

	private EditMode mEditMode = EditMode.VIEW;
	private Handler mHandlerReadImg = null;
	private Thread mThread = null;
	private String mType = "";
	private GestureDetector mGuestureDetector = null;
	private GestureDetector mItemGuestureDetector = null;

	private Bitmap mImgTick = null;
	private OsListViewItem mListViewItem = null;
	private Bitmap defaultCdCover;
	// 2013-4-3 -Amy- add default image
	private Bitmap mDefaultImg;

	// 2013-3-18 -Amy- Use PopupWindow replace RingMenu
	private PopupWindow popWindow = null;
	private View ringMenuShow = null;

	private View mSelectedView = null;

	private float mInitSpan = 0;

	private int mCorX = 0;
	private int mCorY = 0;
	
	OSButton button;
	Bitmap bmpPreviewBg;
	Bitmap bmpPrevieTop;

	// 2013-3-14 - Amy - GridView replace ListView
	private GridView myMusics = null;
	private MusicsAdapter musicAdapter = null;
	private LoadImagesThread musicLoadThread = null;
	private ImageDownloader imageDownloader = null;
	private DialogFragment popupFragment;

	/**
	 * 
	 * @param tag save instance value.match the key "tag"
	 * @return
	 * @author aaronli at May22,2013
	 */
	public static GridViewFragment newInstance(String tag) {
		GridViewFragment f = new GridViewFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("tag", tag);
        f.setArguments(args);

        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.full_list_view, container, false);

		defaultCdCover = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_cd_cover);
		mDefaultImg = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_cd_cover);

		mListViewItemList = new ArrayList<OsListViewItem>();
		mImageList = new ArrayList<Bitmap>();
		mNameList = new ArrayList<String>();
		mType = "music";// getIntent().getStringExtra(Global.STRING_TYPE);
		mContext = (FullListViewActivity) this.getActivity();
		initUIComponent(v);
		initHandler();
		
        //2013-4-2 - Zoya - listener to sdcard insert and pull out events.
      	registerSDCardListener();

		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");

		imageDownloader = new ImageDownloader(mContext, BASE_STORAGE_DIR);
		imageDownloader.setmImageDownloadListener(listener);
		musicLoadThread = new LoadImagesThread();
		musicLoadThread.start();

		getMusicBlackList();
		// getAccount();

		/*if (getUser() == null)
		{
			getAccount(getActivity());
		}*/

		return v;
	}

	//2013-6-13 - Zoya - SDCardListener class
	private final BroadcastReceiver sdcardListener = new BroadcastReceiver() {        
        @Override
        public void onReceive(Context context, Intent intent) {            
            String action = intent.getAction();
            
            if(action.equals("android.intent.action.MEDIA_MOUNTED")){//mount SDcard success
            	musicLoadThread = new LoadImagesThread();
        		musicLoadThread.start();                
            } else if(action.equals("android.intent.action.MEDIA_REMOVED")// SDcard is not mounted
					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")){
            	musicLoadThread = new LoadImagesThread();
        		musicLoadThread.start();                
            }                    
        }
    };
    
	//2013-6-13 - Zoya - registerSDCardListener
	private void registerSDCardListener(){
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

	private void initUIComponent(View v) {
		mImgTick = BitmapFactory.decodeResource(getResources(), R.drawable.del_icon_s);
		mBtnConfirm = (Button) v.findViewById(R.id.buttonConfirmButton);

		myMusics = (GridView) v.findViewById(R.id.myMusics);
		myMusics.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myMusics.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
				{
					hideRingMenu();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		musicAdapter = new MusicsAdapter();
		myMusics.setAdapter(musicAdapter);

		// 2013-4-8 - Zoya - Integration OnClickListener code.
		OnClickListener onClicklistener = new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				/*
				 * case R.id.imageViewBtnBlock: setEditMode(EditMode.DELETE);
				 * break; case R.id.imageViewBtnEdit:
				 * mRenameLayout.setVisibility(View.VISIBLE);
				 * mTxtRename.requestFocus(); InputMethodManager imm =
				 * (InputMethodManager)
				 * getSystemService(Context.INPUT_METHOD_SERVICE);
				 * imm.showSoftInput
				 * (mTxtRename,InputMethodManager.SHOW_IMPLICIT); break;
				 */
				case R.id.buttonConfirmButton:
					if (mEditMode == EditMode.DELETE)
					{
						for (int i = 0; i < mListViewItemList.size(); i++)
						{
							if (mListViewItemList.get(i).isChecked())
							{
								// deleteFile(mListViewItemList.get(i).getPath());
								deleteItem(mListViewItemList.get(i));
							}
						}
					}
					break;
				}
			}
		};
		mBtnConfirm.setOnClickListener(onClicklistener);

		mSnackIcon = (ImageView) v.findViewById(R.id.imageViewSnackView);
		mSnackIcon.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v) {
				goToMeepMusicView();
				// closeFullListView();
			}
		});

		mRenameLayout = (RelativeLayout) v.findViewById(R.id.relativeLayoutRename);
		mTxtRename = (EditText) v.findViewById(R.id.editTextRename);
		mTxtRename.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{

				}
				return false;
			}
		});

		mViewGroup = (ViewGroup) v.findViewById(R.id.musicFullListViewLayout);
	}

	private void showRingMenu(float x, float y, OsListViewItem item) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepMusic-showRingMenu","DISABLE_RING_MENU");
			return;
		}
		
		int left = 0;
		int right = 1;
		int buttonNum = 2;
		if (y < 0)
		{
			y = -20;
		}

		// 2013-4-16 -Amy- modified to change pop images
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		int xx = (int) x + 55;
		int yy = (int) y + 5;
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_rename_delete, null);
		two_button_left = (FrameLayout) ringMenuShow.findViewById(R.id.two_button_left);
		contentLayout = (RelativeLayout) ringMenuShow.findViewById(R.id.contentLayoutleft);
		android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) contentLayout
				.getLayoutParams();
		layoutParams.leftMargin = xx;
		// change the bottom of ring_menu
		//Log.e("cdf", "" + yy);
		if (yy > 230)
		{
			yy = 230;
		}
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

		popWindow.showAtLocation(myMusics, Gravity.START, (int) x + 70, (int) y - 120);
		ringMenuHandler(left, buttonNum, item);
	}

	private void hideRingMenu() {
		if (popWindow != null && popWindow.isShowing())
		{
			popWindow.dismiss();
		}
		ringMenuShow = null;
	}

	private void ringMenuHandler(int leftRight, int buttonNum, OsListViewItem item) {
		if (leftRight == 0 && buttonNum == 2)
		{
			// left menu with two button
			leftMenuTwoButton(item);
		}
	}

	private void leftMenuTwoButton(final OsListViewItem item) {
		ring_delete = (ImageView) ringMenuShow.findViewById(R.id.btnDelete);
		ring_rename = (ImageView) ringMenuShow.findViewById(R.id.btnRename);

		// 2013-4-7 - Zoya - Integration OnClickListener code.
		OnClickListener leftMenuButtonClickListener = new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btnDelete:
					mViewGroup.removeView(ringMenuShow);
					deleteItem(item);
					imageDownloader.clearCache();
					musicAdapter.notifyDataSetChanged();
					hideRingMenu();
					// notification = new NotificationMessage(mContext, null,//
					// DELETE_TITLE, DELETE_MESSAGE);
					break;
				case R.id.btnRename:
					mViewGroup.removeView(ringMenuShow);
//					2013-6-13 -Amy- delete function: can not rename system file 
					//if("/data/home/".equals(item.getPath())){
//					if((item.getPath()).contains("/data/home/music/data/")){
//						//Toast.makeText(getActivity(), "It is system file ,can not be renamed .", 0).show();
//						String renameErr = (String) getResources().getString(R.string.music_title_rename_error);
//						String msg_system_file = (String) getResources().getString(R.string.msg_system_file);
//						notification = new NotificationMessage(mContext, null, renameErr, msg_system_file);
//						hideRingMenu();
//						return;
//					}else{
						renameLayer(item);
						imageDownloader.clearCache();
						musicAdapter.notifyDataSetChanged();
						hideRingMenu();
//					}
					break;
				}
			}
		};
		ring_delete.setOnClickListener(leftMenuButtonClickListener);
		ring_rename.setOnClickListener(leftMenuButtonClickListener);
	}

	private void renameLayer(final OsListViewItem item) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mPopupLayout = (RelativeLayout) inflater.inflate(R.layout.layout_popup_text_input, null);
		ImageButton imageViewQuit = (ImageButton) mPopupLayout.findViewById(R.id.imageViewQuit);
		MyButton textViewOkBtn = (MyButton) mPopupLayout.findViewById(R.id.textViewOkBtn);
		EditText editTextRename = (EditText) mPopupLayout.findViewById(R.id.editTextRename);
		MyTextView textViewNotice = (MyTextView) mPopupLayout.findViewById(R.id.textViewNotice);
		ImageView imageViewEditUserBg = (ImageView) mPopupLayout.findViewById(R.id.imageViewEditUserBg);

		// 2013-4-8 - Zoya - Integration OnClickListener code.
		OnClickListener renameLayerListener = new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.imageViewQuit:
					hidePopupMessageLayout();
					break;
				case R.id.textViewOkBtn:
					boolean isExist = false;
					badWordChecker.setBlacklist(blackListWord);

					String renameErr = (String) getResources().getString(R.string.music_title_rename_error);

					// 2013-03-22 - raymond - avoid rename system folder
					if (item.getName().equals("data") || item.getName().equals("extsd")|| item.getName().equals("sdcard1")|| item.getName().equals("sdcard"))
					{
						String msg_system_file = (String) getResources().getString(R.string.msg_system_file);
						notification = new NotificationMessage(mContext, null, renameErr, msg_system_file,mViewGroup);
						return;
					}

					// 2013-03-22 - raymond - replace all special char
					//2013-7-1 -Amy- add trim method to filter space
					
					
					
					
					String tmpName = mRenameText.getText().toString().trim();
					
					
					
					
					
					
					boolean shouldBlock = false;
					/*if (user != null)
					{
						PermissionManager pm = (PermissionManager) ServiceManager.getService(mContext,
								ServiceManager.PERMISSION_SERVICE);
						// pm.isBadword(user, tmpName);
						// System.out.println(pm.isBadword(user,
						// tmpName)+"%%%%");
						// System.out.println(tmpName+"@@@@");
						// System.out.println(pm.containsBadword(user,
						// tmpName)+"!!!!!!!!!!");

						if (pm.containsBadword(user, tmpName) == true)
						{
							Log.e("cdf","R.string.msg_error");
							String msg_error = (String) getResources().getString(R.string.msg_error);
							notification = new NotificationMessage(mContext, null, renameErr, msg_error);
							return;
						}
					}*/
					//2013-6-26 - Zoya - solve #4269
					if (item.getName().equals(tmpName)) {
						// String renameErrMsgInvalidName = (String) getResources()	.getString(R.string.msg_rename_same);
						// notification = new NotificationMessage(mContext, null,renameErr, null);
						mPopupLayout.setVisibility(View.GONE);
						return;
					}
					if (isContainBadWord(getActivity(), tmpName)) {
						String msg_error = (String) getResources().getString(R.string.msg_error);
						//2013-7-5 -Amy- popup notice screen, both text and background are white, can't see clearly
						notification = new NotificationMessage(mContext, null, renameErr, msg_error,mViewGroup);
						return;
					}
					//2013-4-8 - Zoya - Solve the issue of #4085
					//2013-6-26 -Amy- rename file's name containing space charector not in first or last
					/*if (tmpName.indexOf(" ") != -1){
						shouldBlock = true;
					}*/
					/*if (tmpName.indexOf("%") >= 0 || tmpName.indexOf("^") >= 0 || tmpName.indexOf("&") >= 0
							|| tmpName.indexOf("*") >= 0)
					{
						shouldBlock = true;
					}
					if (tmpName.indexOf("!") >= 0 || tmpName.indexOf("@") >= 0 || tmpName.indexOf("#") >= 0
							|| tmpName.indexOf("$") >= 0)
					{
						shouldBlock = true;
					}
					if (tmpName.indexOf("(") >= 0 || tmpName.indexOf(")") >= 0 || tmpName.indexOf(";") >= 0
							|| tmpName.indexOf(":") >= 0)
					{
						shouldBlock = true;
					}
					if (tmpName.indexOf(",") >= 0 || tmpName.indexOf(".") >= 0 || tmpName.indexOf("/") >= 0
							|| tmpName.indexOf("\\") >= 0)
					{
						shouldBlock = true;
					}
					if (tmpName.indexOf("'") >= 0 || tmpName.indexOf("\"") >= 0 || tmpName.indexOf("*") >= 0)
					{
						shouldBlock = true;
					}
					//2013-7-17 -Amy- Filtering "<>" characters
					if(tmpName.indexOf("<")>=0 || tmpName.indexOf(">")>=0){
						shouldBlock=true;
					}*/	
					shouldBlock=MeepStorageCtrl.isRegexBadCharctors(tmpName);
					if (tmpName.equals(""))
					{
						String renameErrMsgInvalidName = (String) getResources().getString(R.string.msg_null);
						notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
						return;
					} else if (shouldBlock){
						String renameErrMsgInvalidName = (String) getResources().getString(R.string.music_msg_blocked);
						notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
						return;
					} else{
						mRenameText.setText(tmpName);
					}

					if (!mRenameText.getText().toString().equals(""))
					{
						// renamePhoto(renameFileName+".png", item);
						for (int i = 0; i < mListViewItemList.size(); i++)
						{
							// Log.d("All photo name", "All photo name: " +
							// mListViewItemList.get(i).getName());
							// Log.d("All photo name", "All photo name: " +
							// mListViewItemList.get(i).getName().substring(0,
							// mListViewItemList.get(i).getName().lastIndexOf(".")));
							if (mListViewItemList.get(i).getName().toLowerCase()
									.equals(mRenameText.getText().toString().toLowerCase()))
							{
								isExist = true;
							}
						}

						if (isExist)
						{
							String renameErrMsgFormat = (String) getResources().getString(
									R.string.msg_blocked);
							String renameErrMsg = String.format(renameErrMsgFormat, mRenameText.getText().toString());
							notification = new NotificationMessage(mContext, null, renameErr, renameErrMsg,mViewGroup);
							// notification = new NotificationMessage(mContext,
							// null, "Rename error",
							// "This destination already contains a file named \""
							// + mRenameText.getText().toString() +
							// "\". \nPlease check it before renaming this file.");
						} else if ((mRenameText.getText().toString().substring(0, 1)).equals("."))
						{
							String renameErrMsgInvalidName = (String) getResources().getString(
									R.string.music_msg_name_invalid);
							notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
							// notification = new NotificationMessage(mContext,
							// null, "Rename error",
							// "The file name cannot start with \".\", please change the file name");
						} 
						/*else if (!badWordChecker.isStringSafe(mRenameText.getText().toString()))
						{
							String renameErrMsgInvalidName = (String) getResources().getString(
									R.string.msg_error);
							notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
						} 
						*/
						else
						{
							// 2013-3-14 - Amy - GridView replace ListView
							String renameFileNameHasSpace = mRenameText.getText().toString();
							//2013-6-26 -Amy- rename file's name containing space charector not in first or last
							String renameFileName = renameFileNameHasSpace.trim();
						/*	String pathTo = item.getPath().substring(0,
									item.getPath().length() - item.getName().length() - 1)
									+ renameFileName + "/";*/
							// Log.e("cdf","music pathTo--------"+pathTo);
							DataSourceManager.renameFile(renameFileName, item);
							/*item.setName(renameFileName);
							item.setPath(pathTo);*/
							musicAdapter.notifyDataSetChanged();
							// renameFile(mRenameText.getText().toString(),
							// item);
							// redrawListView();
						}
					}
					hidePopupMessageLayout();
					break;
				}
			}
		};

		imageViewQuit.setOnClickListener(renameLayerListener);
		textViewOkBtn.setText(R.string.music_btn_ok);
		textViewOkBtn.setOnClickListener(renameLayerListener);
		mRenameText = editTextRename;
		textViewNotice.setText(R.string.music_btn_ring_rename);
		imageViewEditUserBg.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mPopupLayout.setVisibility(View.VISIBLE);
		mPopupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mViewGroup.addView(mPopupLayout);
		// 2012-3-22 - Amy - During renaming, then touch the screen outside the
		// Rename blank, it will enter into corresponding moive or others
		mPopupLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
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
		if (mTxtRename != null)
		{
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mTxtRename.getWindowToken(), 0);
		}
		mViewGroup.removeView(mPopupLayout);
		mPopupLayout = null;
	}

/*	private void renameFile(String renameFileName, OsListViewItem item) {
		String pathTo = item.getPath().substring(0, item.getPath().length() - item.getName().length() - 1)
				+ renameFileName;

		Log.i("path from", "path from: " + item.getPath());
		Log.i("path to", "path to: " + pathTo);
		Log.i("path from", "path from: " + PATH_LARGE_ICON_DIR + item.getName());
		Log.i("path to", "path to: " + PATH_LARGE_ICON_DIR + renameFileName);

		File from = new File(item.getPath());
		File to = new File(pathTo);
		// Log.e("cdf","to 1-- "+to);
		from.renameTo(to);

		File fromLargeIcon = new File(PATH_LARGE_ICON_DIR + item.getName() + ".png");
		File toLargeIcon = new File(PATH_LARGE_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 2-- "+to);
		fromLargeIcon.renameTo(toLargeIcon);

		File fromDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + item.getName() + ".png");
		File toDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 3-- "+to);
		fromDimIcon.renameTo(toDimIcon);

		File fromSmallIcon = new File(PATH_SMALL_ICON_DIR + item.getName() + ".png");
		File toSmallIcon = new File(PATH_SMALL_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 4-- "+to);
		fromSmallIcon.renameTo(toSmallIcon);

		// 2013-3-26 -Amy- fix issue # 3163��The default album cover will
		// disappear after rename
		File fromIcon = new File(PATH_ICON_DIR + item.getName() + ".png");
		File toIcon = new File(PATH_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 5-- "+to);
		fromIcon.renameTo(toIcon);

		File fromLMIcon = new File(PATH_LM_ICON_DIR + item.getName() + ".png");
		File toLMIcon = new File(PATH_LM_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 6-- "+to);
		fromLMIcon.renameTo(toLMIcon);
	}*/

	private void initHandler() {
		if (mListViewItemList != null && mListViewItemList.size() > 0)
		{
			return;
		}

		mHandlerReadImg = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					int idx = msg.getData().getInt("imgIdx");
					// drawImage(idx);
					popupFragment.dismiss();
					musicAdapter.notifyDataSetChanged();
					Log.w("thread", "thread full list view alive");
					break;
				case 2:
					// drawShelfOnly();
					break;
				default:
					break;
				}
				// mLayout.invalidate();
				super.handleMessage(msg);
			}
		};

		mThread = new Thread(runReadImage);
		mThread.start();

	}

	private Runnable runReadImage = new Runnable()
	{

		@Override
		public void run() {
			// loadAndDrawImage();
			try
			{
				mThread.join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// ******start define listener******
	OnGestureListener mOnGuestureListener = new OnGestureListener()
	{
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (mEditMode == EditMode.VIEW)
			{
				if (mSelectedView != null)
				{
					String name = (String) mSelectedView.getTag();
					OsListViewItem item = findListViewItemByName(name);
					if (mSelectedView.getTag() != null)
					{
						if (item != null)
						{
							goToMusic(item.getPath());
						}
					}
				}
			} else
			{
				hideOption();
			}

			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// float x = e.getX() - 120 + mCorX;
			// float y = e.getY() - 90 + mCorY;
			// showOption(x,y);
			// setEditMode(EditMode.OPTION_SHOWN);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	OnClickListener btnItemOnclickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			//String name = (String) v.getTag(R.id.TAG);
			//OsListViewItem item = findListViewItemByName(name);
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
			Log.e("cdf", "onclick=========");
			if (mEditMode == EditMode.VIEW)
			{
				if (v.getTag(R.id.TAG) != null)
				{
					if (item != null && ringMenuShow == null)
					{
						goToMusic(item.getPath());
					}
				}
			}
			hideRingMenu();
		}
	};

	OnLongClickListener btnItemLongClickListener = new OnLongClickListener()
	{
		@Override
		public boolean onLongClick(View v) {
			final int[] location = new int[2];
			v.getLocationOnScreen(location);
			mCorX = location[0];
			mCorY = location[1];

			float x = mCorX - 90;
			float y = mCorY - 75;
			hideRingMenu();

			//String name = (String) v.getTag(R.id.TAG);
			//OsListViewItem item = findListViewItemByName(name);
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
			showRingMenu(x, y, item);

			return true;
		}
	};

	OnTouchListener scrollViewOnTouchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			hideRingMenu();
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				final int[] location = new int[2];
				v.getLocationOnScreen(location);
				mCorX = location[0];
				mCorY = location[1];
			}
			mGuestureDetector.onTouchEvent(event);
			return false;
		}
	};

	OnTouchListener btnItemOnTouchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			hideRingMenu();
			return true;
		}
	};

	// end define listener

	private OsListViewItem findListViewItemByName(String name) {
		for (int i = 0; i < mListViewItemList.size(); i++)
		{
			if (mListViewItemList.get(i).getName().equals(name))
			{
				return mListViewItemList.get(i);
			}
		}
		return null;
	}

/*	private void traverseDir(File dir, List<File> theList) {
		Log.i("music-traversal", "traverseDir LOADED");
		File[] files = dir.listFiles();
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					traverseDir(files[i], theList);
				} else
				{
					if (!files[i].isHidden()
							&& (files[i].getName().toLowerCase().contains(Global.FILE_TYPE_MP3)
									|| files[i].getName().toLowerCase().contains(Global.FILE_TYPE_WAV) || files[i]
									.getName().toLowerCase().contains(Global.FILE_TYPE_AMR)))
					{
						theList.add(files[i]);
						Log.e("music-traversal", files[i].getAbsolutePath());
					}
				}
			}
		}
	}

	// TODO: This method should combine to traverseDir
	private void traverseDirOnce(File dir, List<File> theList) {
		Log.i("music-traversal", "traverseDir LOADED");
		File[] files = dir.listFiles();
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					// traverseDir(files[i], theList);
				} else
				{
					if (!files[i].isHidden()
							&& (files[i].getName().toLowerCase().contains(Global.FILE_TYPE_MP3)
									|| files[i].getName().toLowerCase().contains(Global.FILE_TYPE_WAV) || files[i]
									.getName().toLowerCase().contains(Global.FILE_TYPE_AMR)))
					{
						theList.add(files[i]);
						Log.e("music-traversal", files[i].getAbsolutePath());
					}
				}
			}
		}
	}*/

	// 2013-3-14 -Amy- replace GridView to List <898��~990��>
	private static class ViewHolder {
		ImageView pic;
		TextView pic_title;
	}

	private class MusicsAdapter extends BaseAdapter {
		private LayoutInflater inflater = LayoutInflater.from(mContext);

		@Override
		public int getCount() {
			if (mListViewItemList == null)
			{
				return 0;
			} else
			{
				return mListViewItemList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			if (mListViewItemList == null)
			{
				return null;
			}
			return mListViewItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			if (mListViewItemList == null)
			{
				return -1;
			} else
			{
				return mListViewItemList.get(position).hashCode();
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null)
			{
				holder = new ViewHolder();

				convertView = inflater.inflate(R.layout.gridview_item, null);

				holder.pic = (ImageView) convertView.findViewById(R.id.musicpic);
				holder.pic_title = (TextView) convertView.findViewById(R.id.musicpic_title);
				convertView.setTag(holder);
			} else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			final OsListViewItem item = mListViewItemList.get(position);
			holder.pic_title.setText(item.getName());
			// holder.pic.setImageBitmap(item.getImage());
			//holder.pic.setTag(R.id.TAG, item.getName());
			holder.pic.setTag(R.id.TAG, item);
			holder.pic.setOnClickListener(btnItemOnclickListener);
			holder.pic.setOnLongClickListener(btnItemLongClickListener);
			// String path = PATH_SMALL_ICON_DIR + item.getName() +
			// Global.FILE_TYPE_PNG;
			// modified by aaronli at Apr
			// imageDownloader.downloadMusic(path, SHOW_WIDTH, SHOW_HEIGHT,
			// holder.pic,defaultCdCover,mDefaultImg);
			imageDownloader.download(item.getPath(), holder.pic, defaultCdCover, null);
			convertView.setVisibility(View.VISIBLE);

			return convertView;
		}
	}

	private Bitmap getImageBitmap(String path) {
		// Log.d("aaron","path == "+path);
		// modified by aaronli Apr19 2013.Show icons in fulllistview,too
		Bitmap bmap = null;
		try
		{
			bmap = DataSourceManager.loadFullListViewIcons(path, defaultCdCover);
			if (bmap == null)
			{
				bmap = defaultCdCover;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bmap;
	}

	private ImageDownloader.ImageDownloadListener listener = new ImageDownloader.ImageDownloadListener()
	{
		@Override
		public void onDownloadSuccessed(ImageView view, Bitmap bitmap) {
			// TODO Auto-generated method stub
			view.setImageBitmap(bitmap);
		}

		@Override
		public void onDownloadFromCache(ImageView view, Bitmap bitmapFromCache) {
			// TODO Auto-generated method stub
			view.setImageBitmap(bitmapFromCache);
		}

		@Override
		public void onDownloadFail(ImageView view) {
			// TODO Auto-generated method stub
			view.setImageBitmap(mDefaultImg);
		}

		@Override
		public Bitmap loadImageFromUrl(String url) {
			// TODO Auto-generated method stub
			return DataSourceManager.loadFullListViewIcons(url, defaultCdCover);
		}
	};

	class LoadImagesThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mListViewItemList != null)
			{
				mListViewItemList.clear();
			}
			Message message = new Message();
			//loadAndDrawImage();
			//2013-6-18 -Amy- Delete one CD album in grid view, but in snake view, it still exit, and there is no song in it.
			mListViewItemList = DataSourceManager.getAllItems(false);
			message.what = 1;
			mHandlerReadImg.sendMessage(message);
			super.run();
		}
	}

	// winder
	/*public static void loadAndDrawImage() {
		List<File> fileList = DataSourceManager.scanDirectList();
		DataSourceManager.getDataSource(fileList, mListViewItemList);
	}*/

	private void goToMusic(String path) {
		Intent intent = new Intent(mContext, MusicPlayerActivity.class);
		intent.putExtra(Global.STRING_PATH, path);
		try
		{
			Log.e("cdf", "onclick========="+path);
			Log.d("GridViewFragment", "goToMusic "+path);
			startActivity(intent);
			MeepLogger meepLogger = new MeepLogger(mContext);
			meepLogger.p("was playing music album: " + path);
		} catch (Exception ex)
		{
			Log.e("meepMusic", "go to music:" + ex.toString());
		}
	}

	private void showOption(float x, float y) {
		mOptionLayout.setX(x);
		mOptionLayout.setY(y);
		mOptionLayout.setVisibility(View.VISIBLE);
	}

	private void hideOption() {
		hideRingMenu();
		mOptionLayout.setVisibility(View.GONE);
		setEditMode(EditMode.VIEW);
	}

	private void setEditMode(EditMode editMode) {
		mEditMode = editMode;
		if (editMode == EditMode.DELETE)
		{
			// showAllDeleteIcons();
			mBtnConfirm.setVisibility(View.VISIBLE);
		} else
		{
			// hideAllDeleteIcons();
			mBtnConfirm.setVisibility(View.GONE);
		}
	}
	
	public LayoutInflater getLayoutInflater() {
        return this.getActivity().getWindow().getLayoutInflater();
    }

	private void deleteItem(final OsListViewItem item) {
		//2013-5-8 -Amy- add delete dialog
		String delErr = (String)getResources().getString(R.string.delete);
		String delMsg = (String)getResources().getString(R.string.delete_sure);
		//notification = new NotificationMessage(mContext, null, delErr, delMsg);
		
		LayoutInflater lf = getLayoutInflater();
		final RelativeLayout popupLayout = (RelativeLayout) lf.inflate(R.layout.notification_delete, null);
		//2013-7-9 -Amy- add quit button
		ImageButton imageViewQuitDelete = (ImageButton) popupLayout.findViewById(R.id.imageViewQuitDelete);
		MyButton textViewYesBtn = (MyButton) popupLayout.findViewById(R.id.textViewYesBtn);
		MyButton textViewNoBtn = (MyButton) popupLayout.findViewById(R.id.textViewNoBtn);
		MyTextView textViewNotice = (MyTextView) popupLayout.findViewById(R.id.textViewNotice);
		//2013-7-9 -Amy- change popup window text style
		TextView textViewMessage = (TextView) popupLayout.findViewById(R.id.textViewMessage);
		
		textViewNotice.setText(delErr);
		textViewMessage.setText(delMsg);
		textViewYesBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//delete item from database
				DataSourceManager.deleteItemFiles(item);

				mListViewItemList.remove(item);
				// redrawListView();
				popupLayout.setVisibility(View.GONE);
				musicAdapter.notifyDataSetChanged();
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
		mViewGroup.addView(popupLayout);
		
		popupLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	private void goToMeepMusicView() {
		/*
		 * Intent myIntent = new Intent(mContext, MeepMusicActivity.class);
		 * myIntent.putExtra(Global.STRING_TYPE, "music");
		 * 
		 * try { startActivityForResult(myIntent, 0); } catch (Exception ex) {
		 * ex.printStackTrace(); }
		 */

		/*FragmentManager fm = getFragmentManager();
		gridViewFrag = fm.findFragmentById(R.id.gridViewFrag);
		snakeFrag = fm.findFragmentById(R.id.snakeFrag);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		 ΪFragment���õ��뵭��Ч��Android��������ʾ������}�����Դ��android�ڲ���Դ���������ֶ����塣 
		ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

		if (gridViewFrag.isHidden())
		{
			ft.show(gridViewFrag);
			ft.hide(snakeFrag);
		} else
		{
			ft.hide(gridViewFrag);
			ft.show(snakeFrag);
		}
		ft.commit();*/
		mContext.showDetails(FullListViewActivity.STATE_FRAG_SNACK);
	}

	private void getMusicBlackList() {
		String sql = "select * from " + TableBlacklist.S_TABLE_NAME + " where " +
		// TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Photo + "'" ;
				TableBlacklist.S_LIST_TYPE + " = 'browser'";
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_BLACK_LIST,
				sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		mContext.sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}

	public enum EditMode
	{
		VIEW, DELETE, EDIT, OPTION_SHOWN,
	}
	
	
/*
 * 	// deleted by aaronli at May27 2013. remove and changed to SeriviceController.
	public void getAccount() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable()
		{
			@Override
			public void run() {
				//Log.e("run", "run method  executed");
				AccountManager am = (AccountManager) ServiceManager
						.getService(mContext, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLoggedInAccountBlocking();
				// Account account = am.getLoggedInAccount();

			//	System.out.println("getLoggedInAccountBlocking " + account);

				String username = null;
				mAccount = account;

				// TODO: perform additional actions after retrieving the
				// currently logged in {@link Account}

				if (account != null)
				{
					//Log.e("account", "account != null");
					username = account.getMeepTag();
					Identity identiry = account.getIdentity();
					user = identiry.getName();
					//Log.e("account", "user " + user);
					//System.out.println(username);
				}
				PermissionManager pm = (PermissionManager) ServiceManager.getService(mContext,
						ServiceManager.PERMISSION_SERVICE);
				List<Component> blockedComponents = pm.getComponentsBlocking(username,
						com.oregonscientific.meep.permission.Category.CATEGORY_BLACKLIST);
				List<Component> games = pm.getComponents(username,
						com.oregonscientific.meep.permission.Category.CATEGORY_GAMES);
			}
		});

	}*/
}
