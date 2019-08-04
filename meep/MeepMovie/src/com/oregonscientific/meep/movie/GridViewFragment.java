package com.oregonscientific.meep.movie;

import static com.oregonscientific.meep.movie.ServiceController.isContainBadWord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Thumbnails;
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
import android.widget.Toast;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyEditText;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.database.table.TableMovie;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.message.common.MeepAppMessageCtrl;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.tool.ImageDownloader;


public class GridViewFragment extends Fragment{
	boolean isMovie = false;
	String RENAME = "Rename";
	String ADD = "Add";
	String DELETE = "Delete";

	private Account mAccount;
//	private static String user;
	//RingMenu ringMenu ;
	ImageView ring_delete;
	ImageView ring_rename;
	ImageView ring_menu_top_right_two;
	ImageView ring_menu_top_left_two;
	ImageView ring_menu_bottom_right_two;
	ImageView ring_menu_bottom_left_two;
	
	NotificationMessage notification;
	String Rename = "";
	
	FullListViewActivity mContext;
	private ViewGroup mViewGroup = null;
	private final String BASE_STORAGE_PATH = "/data/home/";
	
	private final String PATH_SMALL_ICON_DIR = BASE_STORAGE_PATH + "movie/icon_s/";
	private final String PATH_LARGE_ICON_DIR = BASE_STORAGE_PATH + "movie/icon_l/";
	private final String PATH_LARGE_DIM_ICON_DIR = BASE_STORAGE_PATH + "movie/icon_ld/";
	private final String PATH_PHOTO_DATA_DIR = BASE_STORAGE_PATH + "movie/data/";
	
	List<OsListViewItem> mListViewItemList = null;
	List<Bitmap> mImageList = null;
	List<String> mNameList = null;
	//2013-3-16 - Amy - update shelves use GridView
	//LinearLayout mLayout = null;
	//ScrollView mScrollView = null;
	RelativeLayout mOptionLayout = null;
	RelativeLayout mRenameLayout = null;
	EditText mTxtRename = null;
	
	//broadcast message
	MessageReceiver mMsgReceiver = null;
	String[] blackListWord;
	BadWordChecker badWordChecker = new BadWordChecker();
	
	ImageView mSnackIcon = null;
	
	ImageView mBtnBg1 = null;
	ImageView mBtnBg2 = null;
	ImageView mBtnBg3 = null;
	ImageView mBtnBg4 = null;
	ImageView mBtnBg5 = null;
	
	
	ImageView mBtnOptionPlay = null;
	ImageView mBtnOptionEdit = null;
	ImageView mBtnOptionDelete = null;
	ImageView mBtnOptionFavourite = null;
	ImageView mBtnOptionMessage = null;
	
	Button mBtnConfirm = null;

	private EditMode mEditMode = EditMode.VIEW; 
	Handler mHandlerReadImg = null;
	Thread mThread = null;
	String mType = "";
	GestureDetector mGuestureDetector = null;
	
	Bitmap mImgTick = null;
	OsListViewItem mListViewItem = null;
	
	private float mInitSpan = 0;
	//2013-3-16 - Amy - update shelves use GridView
	private GridView myMovies = null;
	private MoviesAdapter movieAdapter = null;
	LoadImagesThread moviesLoadThread = null;
	//2013-3-16 - Amy - add AsyncTask to loading picture
	DialogFragment popupFragment;
	private ImageDownloader imageDownloader = null;
	
	Bitmap movieBmpBg = null;
	Bitmap movieBmpTop = null;
	//2013-4-3 -Amy- add default image
	Bitmap mDefaultImg;
	
	int mCorX = 0;
	int mCorY = 0;
	
	public enum EditMode {
		VIEW, DELETE, EDIT, OPTION_SHOWN,
	}
	
	public static GridViewFragment newInstance(String tag) {
		GridViewFragment f = new GridViewFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("tag", tag);
        f.setArguments(args);

        return f;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	/*	getAccount(getActivity());
		if (user==null)
		{
			getAccount(getActivity());

		}*/
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.full_list_view, container,false);
		//Log.e("cdf","grid-------------------------------------");
		mGuestureDetector = new GestureDetector(this.getActivity(), mOnGuestureListener);
		mListViewItemList = new ArrayList<OsListViewItem>();
		mImageList = new ArrayList<Bitmap>();
		mNameList = new ArrayList<String>();
		mType = "movie";//getIntent().getStringExtra("type");
		mContext = (FullListViewActivity)this.getActivity();
		
		initUIComponent(v);
		initHandler();
		
		//2013-6-13 - Zoya - listener to sdcard insert and pull out events.
		registerSDCardListener();
		
		movieBmpBg = BitmapFactory.decodeResource(getResources(), R.drawable.movie_dvd_bg);
		movieBmpTop = BitmapFactory.decodeResource(getResources(), R.drawable.movie_dvd_top);
		//2013-4-3 -Amy- add default image
		mDefaultImg = BitmapFactory.decodeResource(getResources(), R.drawable.mask);
		
		//2013-3-16 - Amy - update shelves use GridView
		moviesLoadThread = new LoadImagesThread();
		moviesLoadThread.start();
		//2013-3-16 - Amy - add AsyncTask to loading picture
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");
		imageDownloader = new ImageDownloader(mContext, BASE_STORAGE_PATH);
		imageDownloader.setmImageDownloadListener(listener);
				
		
		getMovieBlackList();

		if (ServiceController.getUser() == null) {
			ServiceController.getAccount(mContext);
		}
		
		return v;
	}
	
	// 2013-6-13 - Zoya - SDCardListener class
	private final BroadcastReceiver sdcardListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals("android.intent.action.MEDIA_MOUNTED")) {// insert sdcard
				moviesLoadThread = new LoadImagesThread();
				moviesLoadThread.start();
			} else if (action.equals("android.intent.action.MEDIA_REMOVED")// pull out sdcard
					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")) {
				moviesLoadThread = new LoadImagesThread();
				moviesLoadThread.start();
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
		
		intentFilter.addAction("com.oregonscientific.meep");

		
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

	//2013-04-08 Winder Hao  Integration of listening to events
	OnClickListener onClickListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buttonConfirmButton:
				if(mEditMode == EditMode.DELETE)
				{
					for(int i = 0; i< mListViewItemList.size(); i++)
					{
						if(mListViewItemList.get(i).isChecked())
						{
							//deleteFile(mListViewItemList.get(i).getPath());
							deleteItem(mListViewItemList.get(i));
						}
							
					}
				}
				break;
				case R.id.imageViewSnackView:
				goToMeepMovieView();
				break;
			
			default:
				break;
			}
			
		}
	};

	private void initUIComponent(View v)
	{
		mImgTick = BitmapFactory.decodeResource(getResources(), R.drawable.icon_tick);
		myMovies = (GridView) v.findViewById(R.id.myMovie);
		myMovies.setSelector(new ColorDrawable(Color.TRANSPARENT));
		movieAdapter = new MoviesAdapter();
		myMovies.setAdapter(movieAdapter);
		
		//mBtnOptionDelete.setOnClickListener(onDeleteBtnClickListener);
		//mBtnOptionEdit.setOnClickListener(btnEditOnClickListener);
		
		mSnackIcon = (ImageView)v.findViewById(R.id.imageViewSnackView);
		mSnackIcon.setOnClickListener(onClickListener);
//		  mSnackIcon.setOnClickListener(new View.OnClickListener() {
//				
//			@Override
//			public void onClick(View v) {
//				goToMeepMovieView();
//			}
//		});
		//convertFullListViewThumbnail();
		
		mBtnConfirm = (Button)v.findViewById(R.id.buttonConfirmButton);
		mBtnConfirm.setOnClickListener(onClickListener);
		/*
		 * mBtnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEditMode == EditMode.DELETE)
				{
					for(int i = 0; i< mListViewItemList.size(); i++)
					{
						if(mListViewItemList.get(i).isChecked())
						{
							//deleteFile(mListViewItemList.get(i).getPath());
							deleteItem(mListViewItemList.get(i));
						}
							
					}
				}
			}
		});*/
		
		mRenameLayout = (RelativeLayout)v.findViewById(R.id.relativeLayoutRename);
		mTxtRename = (EditText)v.findViewById(R.id.editTextRename);
		mTxtRename.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER)
				{
					
				}
				return false;
			}
		});	
		
		mViewGroup = (ViewGroup) v.findViewById(R.id.movieFullListViewLayout);
		// delete by aaronli Mar21 remove the no used layout 
		//mMainLayout = (AbsoluteLayout)findViewById(R.id.absoluteLayoutMain);
	}
	
	//2013-3-19 -Amy- Use PopupWindow replace RingMenu
	PopupWindow popWindow = null;
	private View ringMenuShow = null;
	private void showRingMenu(float x, float y, OsListViewItem item) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepMovie-showRingMenu","DISABLE_RING_MENU");
			return;
		}
		
		int left = 0;
		int right = 1;
		int buttonNum = 2;
		
		if (y < 0){
			y= -20;
		} 
		//2013-4-16 -Amy- modified to change pop images
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		int xx = (int)x+55;
		int yy = (int)y+5;
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_rename_delete, null);
		two_button_left = (FrameLayout) ringMenuShow.findViewById(R.id.two_button_left);
		contentLayout = (RelativeLayout) ringMenuShow.findViewById(R.id.contentLayoutleft);
		android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) contentLayout.getLayoutParams();
		layoutParams.leftMargin = xx;
		//Log.e("cdf",""+yy);
		if(yy>230){
			yy=230;
		}
		layoutParams.topMargin = yy;
		contentLayout.setLayoutParams(layoutParams);
		popWindow.setContentView(ringMenuShow);
		popWindow.update();
		two_button_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ringMenuShow.setVisibility(View.GONE);
			}
		});
	
		popWindow.showAtLocation(myMovies, Gravity.START, (int)x+70, (int)y-120);
		ringMenuHandler(left, buttonNum, item);
	}
	
	private void hideRingMenu() {
		if(popWindow != null && popWindow.isShowing()){
			popWindow.dismiss();
		}
		ringMenuShow = null;
	}
	
	private void ringMenuHandler(int leftRight, int buttonNum, OsListViewItem item){
		if (leftRight == 0  && buttonNum == 2) {
			//left menu with two button
			leftMenuTwoButton(item);
		}
	}
	
	private void leftMenuTwoButton(final OsListViewItem item) {
//		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.ring_menu_left_two_button,
//				(ViewGroup) findViewById(R.id.two_button_left));
		//****start***2013-04-17 Meng update ring menu layout 
		ring_delete = (ImageView) ringMenuShow.findViewById(R.id.btnDelete);
		ring_rename = (ImageView) ringMenuShow.findViewById(R.id.btnRename);
		
		//2013-04-08 Winder Hao  Integration of listening to events
		OnClickListener listener = new OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btnDelete:
					mViewGroup.removeView(ringMenuShow);
					deleteItem(item);
					imageDownloader.clearCache();
					movieAdapter.notifyDataSetChanged();
					hideRingMenu();
					break;
				case R.id.btnRename:
//					2013-6-13 -Amy- delete function: can not rename system file 
//					if((item.getPath()).contains("/data/home/movie/data/")){
//						//Toast.makeText(getActivity(), "It is system file ,can not be renamed .", 0).show();
//						String renameErr = (String) getResources().getString(R.string.music_title_rename_error);
//						String msg_system_file = (String) getResources().getString(R.string.msg_system_file_movie);
//						notification = new NotificationMessage(mContext, null, renameErr, msg_system_file);
//						hideRingMenu();
//						return;
//					}else{
						mViewGroup.removeView(ringMenuShow);
						renameLayer(item);
						imageDownloader.clearCache();
						movieAdapter.notifyDataSetChanged();
						hideRingMenu();
//					}
					break;
				default:
					break;
				}
				
			}
		};
		ring_delete.setOnClickListener(listener);
		ring_rename.setOnClickListener(listener);
	}
	
	RelativeLayout mPopupLayout = null;
	EditText mRenameText = null;
	private void renameLayer(final OsListViewItem item){
		LayoutInflater lf = LayoutInflater.from(mContext);
		mPopupLayout = (RelativeLayout) lf.inflate(R.layout.layout_popup_text_input, null);
		
		ImageButton imageViewQuit = (ImageButton)mPopupLayout.findViewById(R.id.imageViewQuit);
		MyButton textViewOkBtn = (MyButton)mPopupLayout.findViewById(R.id.textViewOkBtn);
		MyEditText editTextRename = (MyEditText)mPopupLayout.findViewById(R.id.editTextRename);
		MyTextView textViewNotice = (MyTextView)mPopupLayout.findViewById(R.id.textViewNotice);
		ImageView imageViewEditUserBg = (ImageView)mPopupLayout.findViewById(R.id.imageViewEditUserBg);
		
		imageViewQuit.setOnClickListener(new View.OnClickListener() {
			//winder
			@Override
			public void onClick(View v) {
				hidePopupMessageLayout();
			}
		});
		textViewOkBtn.setText(R.string.video_btn_ok);
		textViewOkBtn.setOnClickListener(new View.OnClickListener() {
			//winder
			@Override
			public void onClick(View v) {
				boolean isExist = false;
				badWordChecker.setBlacklist(blackListWord);
				
				String renameErr = (String)getResources().getString(R.string.video_title_rename_error);
				
				//2013-03-22 - raymond - avoid rename system folder
				if(item.getName().equals("data") || item.getName().equals("extsd")){
					//mRenameText.setText("");
					String msg_system_file = (String) getResources().getString(R.string.msg_system_file_movie);
					notification = new NotificationMessage(mContext, null, renameErr, msg_system_file,mViewGroup);
					return;
				}
				
				//2013-03-20 - raymond - replace all special char
				String tmpName = mRenameText.getText().toString().trim();
				boolean shouldBlock = false;
		/*	if (user != null)
				{
					System.out.println("user!============null");
					PermissionManager pm = (PermissionManager) ServiceManager.getService(getActivity(),ServiceManager.PERMISSION_SERVICE);
					if (pm.containsBadword(user, tmpName) == true)
					{					
						System.out.println(pm.containsBadword(user, tmpName)+"***********************");

						String msg_error = (String) getResources().getString(R.string.msg_error);
						notification = new NotificationMessage(mContext, null, renameErr, msg_error);
						return;
					}
				}*/

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
					notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mViewGroup);
					return;
				}else if(shouldBlock){
					String renameErrMsgInvalidName = (String) getResources().getString(R.string.video_msg_blocked);
					notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
					return;
				}else{
					mRenameText.setText(tmpName);
				}
				
				if (isContainBadWord(getActivity(), tmpName)) {
					String msg_error = (String) getResources().getString(R.string.msg_error);
					notification = new NotificationMessage(mContext, null, renameErr, msg_error,mViewGroup);
					return;
				}
				
				if (!mRenameText.getText().toString().equals("")) {
					// renameMovie(renameFileName+".png", item);
					for (int i = 0; i < mListViewItemList.size(); i++) {
						// Log.d("All movie name", "All movie name: " +
						// mListViewItemList.get(i).getName());
						// Log.d("All movie name", "All movie name: " +
						// mListViewItemList.get(i).getName().substring(0,
						// mListViewItemList.get(i).getName().lastIndexOf(".")));
						if (mListViewItemList.get(i).getName().toLowerCase().equals(mRenameText.getText().toString().toLowerCase())) {
							isExist = true;
						}
					}
					
					if (isExist){
						String renameErrMsgFormat = (String)getResources().getString(R.string.msg_blocked);
						String renameErrMsg = String.format(renameErrMsgFormat, mRenameText.getText().toString());
						notification = new NotificationMessage(mContext, null, renameErr, renameErrMsg,mViewGroup);								
						//notification = new NotificationMessage(mContext, null, "Rename error", "This destination already contains a file named \"" + mRenameText.getText().toString() + "\". \nPlease check it before renaming this file.");
					}else if ((mRenameText.getText().toString().substring(0, 1)).equals(".")){
						String renameErrMsgInvalidName = (String)getResources().getString(R.string.video_msg_name_invalid);
						notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mViewGroup);									
						//notification = new NotificationMessage(mContext, null, "Rename error", "The file name cannot start with \".\", please change the file name");			
					} 
					/*else if (!badWordChecker.isStringSafe(mRenameText.getText().toString())){
						String renameErrMsgInvalidName = (String)getResources().getString(R.string.msg_error);
						notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName,mViewGroup);		
					} */
					else {
						String renameFileNameHasSpace = mRenameText.getText().toString();
						//2013-6-26 -Amy- rename file's name containing space charector not in first or last
						String renameFileName = renameFileNameHasSpace.trim();
						String pathTo = item.getPath().substring(0,item.getPath().length() - item.getName().length() - 4) + renameFileName + item.getPath().substring(item.getPath().lastIndexOf("."));
						//2013-6-28 -Amy- Change the extension and reset the path in renameFile method
						DataSourceManager.renameFile(renameFileName, item);
						//2013-3-16 -Amy- Rename after refresh
						item.setName(mRenameText.getText().toString());
						//2013-6-28 -Amy- Change the extension and reset the path in renameFile method
						//item.setPath(pathTo);
						movieAdapter.notifyDataSetChanged();
						//redrawListView();
					}
				}
				hidePopupMessageLayout();
			}
		});
		mRenameText = (EditText)editTextRename;
		
		textViewNotice.setText(R.string.video_btn_ring_rename);
		//2012-3-22 - Amy - During renaming, then touch the screen outside the Rename blank, it will enter into corresponding moive or others
		imageViewEditUserBg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
			
		mPopupLayout.setVisibility(View.VISIBLE);
		mPopupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mViewGroup.addView(mPopupLayout);
		//2012-3-22 - Amy - During renaming, then touch the screen outside the Rename blank, it will enter into corresponding moive or others
		mPopupLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}
	/*//2013-4-2 -Amy- when click back, Jump to gridview --add mPopupLayout != null && 
	public void onBackPressed() {
		if(mPopupLayout != null && mPopupLayout.isShown()){
			Log.e("cdf","back pressed ! ");
			mViewGroup.removeView(mPopupLayout);
			//mViewGroup.removeViewInLayout(mPopupLayout);
		}else{
			super.onBackPressed();
		}
		return;
	}*/

	private void hidePopupMessageLayout()
	{
		//2013-03-25 - raymond - dismiss rename keyboard
		if(mTxtRename!=null){
			InputMethodManager imm = (InputMethodManager)mContext.getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mTxtRename.getWindowToken(), 0);
		}		
		mViewGroup.removeView(mPopupLayout);
		mPopupLayout = null;
	}
	
	private void renameFile(String renameFileName, OsListViewItem item){
		String pathTo = item.getPath().substring(0,
				item.getPath().length() - item.getName().length() - 4)
				+ renameFileName;
		
//		TableMovie movie = new TableMovie();
//		movie.setDataAddr(pathTo + ".mp4");
//		
		Log.i("path from", "path from: " + item.getPath());
		Log.i("path to", "path to: " + pathTo + ".mp4");
		Log.i("path name", "path name: " + PATH_LARGE_ICON_DIR + item.getName()+ ".png");
		Log.i("path to", "path to: " + PATH_LARGE_ICON_DIR + renameFileName+".png");
		
		File from = new File(item.getPath());
		File to = new File(pathTo + ".mp4");
		from.renameTo(to);
		
		File fromLargeIcon = new File(PATH_LARGE_ICON_DIR + item.getName() + ".png");
		File toLargeIcon = new File(PATH_LARGE_ICON_DIR + renameFileName + ".png");
		fromLargeIcon.renameTo(toLargeIcon);
		
		File fromDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + item.getName() + ".png");
		File toDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + renameFileName + ".png");
		fromDimIcon.renameTo(toDimIcon);
		
		File fromSmallIcon = new File(PATH_SMALL_ICON_DIR + item.getName()+ ".png");
		File toSmallIcon = new File(PATH_SMALL_ICON_DIR + renameFileName + ".png");
		fromSmallIcon.renameTo(toSmallIcon);
	}
	
	private void initHandler() {
		mHandlerReadImg = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					int idx = msg.getData().getInt("imgIdx");
					
					popupFragment.dismiss();
					movieAdapter.notifyDataSetChanged();
					//drawImage(idx);
					Log.w("thread", "thread full list view alive");
					break;
				case 2: //draw shelf only
					//drawShelfOnly();
					 break;
				default:
					break;
				}
				//mLayout.invalidate();
				super.handleMessage(msg);
			}
		};
		//2013-3-16 - Amy - update shelves use GridView
		//mThread = new Thread(runReadImage);
		//mThread.start();
	}

	private Runnable runReadImage = new Runnable() {

		@Override
		public void run() {
			//loadAndDrawImage();
			try {
				mThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	//******start define listener******
	
	OnGestureListener mOnGuestureListener = new OnGestureListener() {
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (mEditMode != EditMode.VIEW) {
				hideOption();
				hideRingMenu();
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
			hideRingMenu();
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
//			float x = e.getX() - 120;
//			float y = e.getY() - 90;
//			showOption(x,y);
//			setEditMode(EditMode.OPTION_SHOWN);
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

	OnClickListener onPlayBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			
			
		}
	};
	
	OnClickListener onDeleteBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setEditMode(EditMode.DELETE);
			
		}
	};
	
	OnClickListener btnOptonOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			ImageView btnView = (ImageView)v;
//			if (btnView.getText().equals(DELETE)) {
//				setEditMode(EditMode.DELETE);
//				deleteItem((String)v.getTag());
//			} else if (btnView.getText().equals(RENAME)) {
//				setEditMode(EditMode.EDIT);
//			} else {
//				setEditMode(EditMode.OPTION_SHOWN);
//			}	
		}
	};
	
	OnClickListener btnEditOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			mRenameLayout.setVisibility(View.VISIBLE);
			mTxtRename.requestFocus();
			InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mTxtRename, InputMethodManager.SHOW_IMPLICIT);
		}
	};
	
	
	
	
	OnClickListener btnItemOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//2013-3-16 - Amy - add AsyncTask to loading picture
			/*String name = (String) v.getTag(R.id.TAG);
			OsListViewItem item = findListViewItemByName(name);*/
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
			Log.e("cdf","name == ");
			//if (mEditMode == EditMode.VIEW) {
				if (v.getTag(R.id.TAG) != null) {
					if (item != null && ringMenuShow == null) {
						goToMovie(item.getPath());
						/*Bitmap mBitmap= ThumbnailUtils.createVideoThumbnail(item.getPath(), Thumbnails.MINI_KIND);
						if (mBitmap!=null)
						{
						goToMovie(item.getPath());
						}
						else {
							//Toast.makeText(mContext, "闁跨喐鏋婚幏鐑芥晸鏉堝啴娼婚幏鐑芥晸娓氥儳銆嬮幏锟� 5000).show();
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
					}
					hideRingMenu();
				}
			//}else if (mEditMode == EditMode.DELETE){
//				RelativeLayout layout = (RelativeLayout)v;
//				ImageView tickView = (ImageView) layout.getChildAt(2);
//				if (tickView.getVisibility() != View.VISIBLE) {
//					tickView.setVisibility(View.VISIBLE);
//					item.setIsChecked(true);
//				} else {
//					tickView.setVisibility(View.GONE);
//					item.setIsChecked(false);
//				}
			//}
		}
	};
	
	
	OnLongClickListener btnItemLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			final int[] location = new int[2]; 
			v.getLocationOnScreen(location);
			mCorX = location[0];
			mCorY = location[1];
			
			float x = mCorX - 90;
			float y = mCorY - 75;
			hideRingMenu();

			/*String name = (String) v.getTag(R.id.TAG);
			OsListViewItem item = findListViewItemByName(name);*/
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
			showRingMenu(x, y, item);	

			return true;
		}
	};
	
	//end define listener
	
	private OsListViewItem findListViewItemByName(String name)
	{
		for(int i = 0; i< mListViewItemList.size(); i++){
			if(mListViewItemList.get(i).getName().equals(name))
			{
				return mListViewItemList.get(i);
			}
		}
		return null;
	}
	
	//2013-3-16 - Amy - update shelves use GridView
	private static class ViewHolder {  
		ImageView pic;
		TextView pic_title;
		
	}  
	private class MoviesAdapter extends BaseAdapter
	{
		private LayoutInflater inflater = LayoutInflater.from(mContext);
		
		@Override
		public int getCount() {
			//Log.e("cdf","mListViewItemList.size() == "+mListViewItemList.size()); 
			if(mListViewItemList == null) {
				return 0;
			} else {
				return mListViewItemList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			if (mListViewItemList == null) {
				return null;
			}
			return mListViewItemList.get(position);
			
		}
		

		@Override
		public long getItemId(int position) {
			if (mListViewItemList == null) {
				return -1;
			} 
			return mListViewItemList.get(position).hashCode();
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			
			if(convertView == null){
				holder = new ViewHolder();
				
				convertView = inflater.inflate(R.layout.gridview_item, null);
				
				holder.pic = (ImageView)convertView.findViewById(R.id.moviepic);
				holder.pic_title = (TextView) convertView.findViewById(R.id.moviepic_title);
				
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
		
			final OsListViewItem item = mListViewItemList.get(position);
			holder.pic_title.setText(item.getName());
			//holder.pic.setImageBitmap(item.getImage());
			holder.pic.setTag(R.id.TAG, item);
			//Log.e("cdf","item.getName() == 2  "+item.getName());
			holder.pic.setOnClickListener(btnItemOnclickListener);
			holder.pic.setOnLongClickListener(btnItemLongClickListener);
			//2013-3-16 - Amy - add AsyncTask to loading picture
			//String path = PATH_SMALL_ICON_DIR + item.getName();
			//imageDownloader.downloadMovie(path,SHOW_WIDTH, SHOW_HEIGHT, holder.pic,item.getPath(),item.getName(),movieBmpBg,movieBmpTop,mDefaultImg);
			imageDownloader.download(item.getPath(), holder.pic,mDefaultImg,null);
			convertView.setVisibility(View.VISIBLE);
		
			return convertView;
		}
	}
	
	private ImageDownloader.ImageDownloadListener listener = new ImageDownloader.ImageDownloadListener() {
		
		@Override
		public void onDownloadSuccessed(ImageView view, Bitmap bitmap) {
			view.setImageBitmap(bitmap);
		}
		
		@Override
		public void onDownloadFromCache(ImageView view, Bitmap bitmapFromCache) {
			view.setImageBitmap(bitmapFromCache);
		}
		
		@Override
		public void onDownloadFail(ImageView view) {
			view.setImageBitmap(mDefaultImg);
		}
		
		@Override
		public Bitmap loadImageFromUrl(String url) {
			return DataSourceManager.getLargebitmapBitmap(mDefaultImg, url);
		}
	};
	class LoadImagesThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mListViewItemList != null){
				mListViewItemList.clear();
			}
			Message message = new Message();
			//Log.e("cdf","cdf-----------");
			loadAndDrawImage();
			message.what = 1;
			mHandlerReadImg.sendMessage(message);
			super.run();
		}
	}
	
	private void loadAndDrawImage() {
		
		//List<File> files = DataSourceManager.scanDirectList();
		//DataSourceManager.getDataSource(files, mListViewItemList);
		//Cache method
		mListViewItemList = DataSourceManager.getAllItems(false);
		
	}
	
	private void redrawListView()
	{
		//2013-3-16 - Amy - update shelves use GridView
		//mLayout.removeAllViews();
		mListViewItemList.removeAll(mListViewItemList);
		loadAndDrawImage();
//		for(int i=0; i<mListViewItemList.size(); i++)
//		{
//			drawImage(i);
//		}
	}
	
	
	private String getContentPath(String fileName)
	{
		String path = PATH_PHOTO_DATA_DIR;
		String name = fileName.substring(0,fileName.lastIndexOf('.'));
		
		return path  + name + ".mp4";
	}
	
	private String getFileNameOnly(String fileName)
	{
		return fileName.substring(0,fileName.lastIndexOf('.'));
	}
	
	private void goToMovie(String path) {
//		Intent intent = new Intent(this, FullListViewActivity.class);
//		intent.setComponent(ComponentName
//				.unflattenFromString("com.oregonscientific.meep.movieplayer/com.oregonscientific.meep.movieplayer.MoviePlayerActivity"));
//		intent.addCategory("android.intent.category.LAUNCHER");
		Intent intent = new Intent(this.getActivity(), MoviePlayerActivity.class);
		intent.putExtra(Global.STRING_MOVIE_PATH, path);
		try {
			startActivity(intent);
			Log.e("cdf","path == "+path);
            MeepLogger meepLogger = new MeepLogger(mContext);
            meepLogger.p("was playing movie: " + path);
		} catch (Exception ex) {
			Toast.makeText(this.getActivity(), ex.toString(), Toast.LENGTH_SHORT);
			Log.e("meepMovie", "go to movie:" + ex.toString());
		}

	}
	
	private void showOption(float x, float y) {
		mOptionLayout.setX(x);
		mOptionLayout.setY(y);
		mOptionLayout.setVisibility(View.VISIBLE);
	}

	private void hideOption() {
		mOptionLayout.setVisibility(View.GONE);
	}

	private void setEditMode(EditMode editMode) {
		mEditMode = editMode;
		if(editMode == EditMode.DELETE)
		{
		//showAllDeleteIcons();
		mBtnConfirm.setVisibility(View.VISIBLE);
		}
	}
	
	
	private void changeToDeleteMode()
	{
		
	}
	
	private void deleteItem(OsListViewItem item) {
		// 2013-5-14 -Zoya- add delete dialog
		String delErr = (String) getResources().getString(R.string.delete);
		String delMsg = (String) getResources().getString(R.string.delete_sure);
		final OsListViewItem mitem = item;
		// notification = new NotificationMessage(mContext, null, delErr,
		// delMsg);
	
		LayoutInflater lf = LayoutInflater.from(mContext);
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
				// delete item from database
				TableMovie movie = new TableMovie();
				movie.setDataAddr(mitem.getPath());
				deleteItemFile(mitem.getPath());
				deleteItemFile(PATH_SMALL_ICON_DIR + mitem.getName() + ".png");
				deleteItemFile(PATH_LARGE_DIM_ICON_DIR + mitem.getName()+ ".png");
				deleteItemFile(PATH_LARGE_ICON_DIR + mitem.getName()+ ".png");
				mListViewItemList.remove(mitem);
				redrawListView();
				
				movieAdapter.notifyDataSetChanged();
				// redrawListView();
				popupLayout.setVisibility(View.GONE);
				movieAdapter.notifyDataSetChanged();
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
		popupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,	LayoutParams.MATCH_PARENT));
		mViewGroup.addView(popupLayout);
	
		popupLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

	}
	
	private void deleteItemFile(String path)
	{
		//delete the real item
				File file = new File(path);
				try {
					if (file.exists()) {
						file.delete();
					}
				} catch (Exception e) {
					Log.e("MovieFullListView", "delete file error : " + e.toString());
				}
	}
	

	private void goToMeepMovieView(){
		mContext.showDetails(FullListViewActivity.STATE_FRAG_SNACK);
	}
	
	private Bitmap getMovieFullListViewIcon(Bitmap sourceBmp)
	{
		float scale = 90f / sourceBmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		
		Bitmap resizedBitmap = Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.getWidth(), sourceBmp.getHeight(), matrix, true);
		int cutx = (int) (resizedBitmap.getWidth() / 2 - 39);
		Bitmap cropBitmap = null;
		if (resizedBitmap.getWidth() > 78) {
			cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 78, resizedBitmap.getHeight());
		} else {
			cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
		}

		Bitmap mutableBm = movieBmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		canvas.drawBitmap(cropBitmap, 8, 4, null);
		canvas.drawBitmap(movieBmpTop, 0, 0, null);
		
		return mutableBm;
	}
	
	
	private void convertFullListViewThumbnail() {
		try {
			String dataPath = PATH_PHOTO_DATA_DIR;
			String path = "/date/home/movie/preview/";
			File file = new File(dataPath);
			if (file.exists() && file.isDirectory()) {
				String[] fileNames = file.list();
				for (int i = 0; i < fileNames.length; i++) {
					String name = fileNames[i].substring(0, fileNames[i].length() -4);
					String smallImgPath = PATH_SMALL_ICON_DIR + name + Global.FILE_TYPE_PNG;
					File smallImgFile = new File(smallImgPath);
					if (smallImgFile.exists()) {
						continue;
					}
					File image = new File(path + name + Global.FILE_TYPE_PNG);
					if(image.isHidden())
					{
						continue;
					}
					Bitmap bmpBg = BitmapFactory.decodeFile("/mnt/sdcard/home/movie/player/listview/Movie_DVD.png");
					Bitmap bmpTop = BitmapFactory.decodeFile("/mnt/sdcard/home/movie/player/listview/Movie_DVD_Top.png");
					Bitmap bmp = BitmapFactory.decodeFile(path + name + Global.FILE_TYPE_PNG);
					float scale = 100f / bmp.getHeight();
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);

					Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
					int cutx = (int) (resizedBitmap.getWidth() / 2 - 39);
					Bitmap cropBitmap = null;
					if (resizedBitmap.getWidth() > 78) {
						cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 78, resizedBitmap.getHeight());
					} else {
						cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
					}

					Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
					Canvas canvas = new Canvas(mutableBm);
					canvas.drawBitmap(cropBitmap, 13, 4, null);
					canvas.drawBitmap(bmpTop, 0, 0, null);

					OutputStream os = null;
					try {
						os = new FileOutputStream(PATH_SMALL_ICON_DIR + name + Global.FILE_TYPE_PNG);
						mutableBm.compress(CompressFormat.PNG, 50, os);
					} catch (Exception e) {
						// TODO: handle exception
		
					}
					Log.d("movie", "decodeImg:" + PATH_SMALL_ICON_DIR + name + Global.FILE_TYPE_PNG);
					
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
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
	
/*	
	public  void getAccount(final Context context) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable()
		{
			@Override
			public void run() {
				//Log.e("run", "run method  executed");
				AccountManager am = (AccountManager) ServiceManager
						.getService(context, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLoggedInAccountBlocking();
				// Account account = am.getLoggedInAccount();

				System.out.println("getLoggedInAccountBlocking " + account);

				String username = null;

				// TODO: perform additional actions after retrieving the
				// currently logged in {@link Account}

				if (account != null)
				{
					Log.e("account", "account != null");
					user = account.getMeepTag();
					Identity identiry = account.getIdentity();
					username = identiry.getName();
					Log.e("account", "user " + user);
					System.out.println(username);
				}
				PermissionManager pm = (PermissionManager) ServiceManager.getService(mContext,
						ServiceManager.PERMISSION_SERVICE);
				List<Component> blockedComponents = pm.getComponentsBlocking(username,
						com.oregonscientific.meep.permission.Category.CATEGORY_BLACKLIST);
				List<Component> games = pm.getComponents(username,
						com.oregonscientific.meep.permission.Category.CATEGORY_GAMES);
			}
		});

	}
	*/
}


