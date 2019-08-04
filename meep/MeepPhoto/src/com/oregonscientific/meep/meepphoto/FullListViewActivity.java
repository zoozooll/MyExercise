
package com.oregonscientific.meep.meepphoto;

import java.io.File;

import java.io.FileOutputStream;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore.Files;
import android.text.InputType;
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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.meepphoto.MessageReceiver.OnMessageReceivedListener;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.message.common.MeepAppMessageCtrl;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.ringmenu.RingMenu;
import com.oregonscientific.meep.tool.ImageDownloader;
import com.oregonscientific.meep.view.MyGridView;


public class FullListViewActivity extends Activity {
	
	private final static String TAG = "FullListViewActivity";

	String RENAME = "Rename";
	String ADD = "Add";
	String DELETE = "Delete";

	//	String DELETE_TITLE = "Delete Photo";
	//	String DELETE_MESSAGE = "This photo is deleted!";

	//RingMenu ringMenu;
	ImageView ring_delete;
	ImageView ring_rename;
	ImageView ring_menu_top_right_two;
	ImageView ring_menu_top_left_two;
	ImageView ring_menu_bottom_right_two;
	ImageView ring_menu_bottom_left_two;
	private PopupWindow popWindow;
	private View ringMenuShow;

	RelativeLayout mPopupLayout = null;
	EditText mRenameText = null;
	
	NotificationMessage notification;
	String Rename = "";

	Context mContext;
	private ViewGroup mViewGroup = null;
	private final String BASIC_STORAGE_PATH = "/data/home/";

	private final String PATH_SMALL_ICON_DIR = BASIC_STORAGE_PATH + "photo/icon_s/";
	private final String PATH_LARGE_ICON_DIR = BASIC_STORAGE_PATH + "photo/icon_l/";
	private final String PATH_LARGE_DIM_ICON_DIR = BASIC_STORAGE_PATH + "photo/icon_ld/";
	private final String PATH_PHOTO_DATA_DIR = BASIC_STORAGE_PATH + "photo/data/";
	
	private final String PATH_PRIVATE_ICON_PATH = "/data/home/photo/icon/";
	private final String PATH_PRIVATE_LARGE_ICON_PATH = "/data/home/photo/icon_l/";
	private final String PATH_PRIVATE_SMALL_ICON_PATH = "/data/home/photo/icon_s/";
	
	private final static int SHOW_WIDTH = 128;
	private final static int SHOW_HEIGHT = 120;
	private static final String IMAGE_CACHE_DIR = "images_shelf";

	List<OsListViewItem> mListViewItemList = null;
	List<Bitmap> mImageList = null;
	List<String> mNameList = null;
	//LinearLayout mLayout = null;
	//ScrollView mScrollView = null;
	RelativeLayout mOptionLayout = null;
	RelativeLayout mRenameLayout = null;
	EditText mTxtRename = null;

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
	private ImageDownloader mImageDownloader;


	private Bitmap mCoverImageDown = null;
	private Bitmap mCoverImageUp = null;
	private String pathCoverDownL= null;
	private String pathCoverUpL = null;
	//	private final String PHOTO_PATH_CD_COVER_UP_L = "/mnt/sdcard/home/photo/default/photo_upcover.png";
	//	private final String PHOTO_PATH_CD_COVER_DOWN_L = "/mnt/sdcard/home/photo/default/photo_downcover.png";
	
	// Button mBtnConfirm = null;

	private EditMode mEditMode = EditMode.VIEW;
	Handler mHandlerReadImg = null;
	Thread mThread = null;
	String mType = "";
	String mPath = "";
	GestureDetector mGuestureDetector = null;
	GestureDetector mItemGuestureDetector = null;
	
	LoadImagesThread loadImages = null;
	PicturesAdapter picture_adapter;
	private MyGridView myPictures;
	DialogFragment popupFragment;
	List<String> mFileList;

	Bitmap mImgTick = null;
	OsListViewItem mListViewItem = null;

	//broadcast message
	MessageReceiver mMsgReceiver = null;
	String[] blackListWord;
	BadWordChecker badWordChecker = new BadWordChecker();
	
	Bitmap mBmpPreviewBg =  null;
	Bitmap mBmpPrevieTop =  null;
	
	private float mInitSpan = 0;

	int mCorX = 0;
	int mCorY = 0;

	public enum EditMode {
		VIEW, DELETE, RENAME, OPTION_SHOWN
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_list_view);

		mGuestureDetector = new GestureDetector(this, mOnGuestureListener);
		mItemGuestureDetector = new GestureDetector(this, mOnGuestureListener);
		mListViewItemList = new ArrayList<OsListViewItem>();
		mImageList = new ArrayList<Bitmap>();
		mNameList = new ArrayList<String>();
		mType = getIntent().getStringExtra(Global.STRING_TYPE);
		mPath = getIntent().getStringExtra(Global.STRING_PATH);
		mContext = this;
		initUIComponent();
		initHandler();
		
		//2013-4-2 - Zoya - sdcardListener listener to sdcard insert and pull out events.
		registerSDCardListener();
		
		// add loading spinner when loading thumbnail images
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");
		
		//loadAndDrawImage2();
		loadImages = new LoadImagesThread();
		loadImages.start();
		mImageDownloader = new ImageDownloader(this, IMAGE_CACHE_DIR);
		mImageDownloader.setmImageDownloadListener(mDownloadListener);
		mMsgReceiver = new MessageReceiver(this);
		mMsgReceiver.setOnMessageReceivedListener(new OnMessageReceivedListener() {

			@Override
			public void onQueryPhotoBlackListReceived(List<TableBlacklist> photoBlacklist) {
				// TODO Auto-generated method stub
				if (photoBlacklist.size() != 0) {
					TableBlacklist blacklist = photoBlacklist.get(0);

					if (blacklist.getListType().toString().equals("bypass")) {
//						byPassWord = blacklist.getListEntry();
//						if (byPassWord != null){
//							for (int i = 0; i < byPassWord.length; i++) {
//	
//								Log.d("photo", "photo bypass received: " + byPassWord[i]);
//							}
//						}
					} else {
						blackListWord = blacklist.getListEntry();
//						for (int i = 0; i < blackListWord.length; i++) {
//							Log.d("photo", "photo blacklist received: "	+ blackListWord[i]);
//						}
					}
				}
			}
		});
		
		getPhotoBlackList();

	}

	@Override
	protected void onStart() {
		//initHandler();
		super.onStart();
	}

	private void initUIComponent() {
		mBmpPreviewBg =  BitmapFactory.decodeResource(getResources(), R.drawable.photo_downcover);
		mBmpPrevieTop =  BitmapFactory.decodeResource(getResources(), R.drawable.photo_upcover);
		
		mImgTick = BitmapFactory.decodeResource(getResources(), R.drawable.del_icon_s);

		/*mScrollView = (ScrollView) findViewById(R.id.scrollViewFullList);
		mScrollView.setOnTouchListener(scrollViewOnTouchListener);
		mScrollView.setLongClickable(true);

		mLayout = (LinearLayout) findViewById(R.id.linearLayoutFullList);*/

		mOptionLayout = (RelativeLayout) findViewById(R.id.gridLayoutOption);

		mBtnBg1 = (ImageView) findViewById(R.id.imageViewBtnBg1);
		mBtnBg2 = (ImageView) findViewById(R.id.imageViewBtnBg2);
		mBtnBg3 = (ImageView) findViewById(R.id.imageViewBtnBg3);
		mBtnBg4 = (ImageView) findViewById(R.id.imageViewBtnBg4);
		mBtnBg5 = (ImageView) findViewById(R.id.imageViewBtnBg5);

		mBtnOptionDelete = (ImageView) findViewById(R.id.imageViewBtnBlock);
		mBtnOptionEdit = (ImageView) findViewById(R.id.imageViewBtnEdit);
		mBtnOptionFavourite = (ImageView) findViewById(R.id.imageViewBtnFavourite);
		mBtnOptionMessage = (ImageView) findViewById(R.id.imageViewBtnMessage);
		mBtnOptionPlay = (ImageView) findViewById(R.id.imageViewBtnPlay);
		
		myPictures = (MyGridView) findViewById(R.id.myPictures);
		myPictures.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myPictures.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
					hideRingMenu();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

		picture_adapter = new PicturesAdapter();
		myPictures.setAdapter(picture_adapter);
		
		mBtnOptionDelete.setOnClickListener(onClickListener);
		mBtnOptionEdit.setOnClickListener(onClickListener);

		mSnackIcon = (ImageView) findViewById(R.id.imageViewSnackView);

		mSnackIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goToMeepPhotoView();
			}
		});

		// mBtnConfirm = (Button)findViewById(R.id.buttonConfirmButton);
		// mBtnConfirm.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (mEditMode == EditMode.DELETE) {
		// for (int i = 0; i < mListViewItemList.size(); i++) {
		// if (mListViewItemList.get(i).isChecked()) {
		// // deleteFile(mListViewItemList.get(i).getPath());
		// deleteItem(mListViewItemList.get(i));
		// }
		// }
		// }
		// }
		// });

		mRenameLayout = (RelativeLayout) findViewById(R.id.relativeLayoutRename);
		mTxtRename = (EditText) findViewById(R.id.editTextRename);
		mTxtRename.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {

				}
				return false;
			}
		});

		mViewGroup = (ViewGroup) findViewById(R.id.photoFullListViewLayout);
		popWindow = new PopupWindow(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popWindow.setOutsideTouchable(true);
	}
	
	private void showRingMenu(float x, float y, OsListViewItem item) {
		int left = 0;
		int right = 1;
		int buttonNum = 2;
		
		/*if (y < 0) {
			y = -20;
		}*/
		
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
		if(yy>250){
			yy=250;
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
	
		popWindow.showAtLocation(myPictures, Gravity.START, (int)x+70, (int)y-120);
		ringMenuHandler(left, buttonNum, item);
		
	}
	
	
	private void hideRingMenu() {
		//mViewGroup.removeView(ringMenu);
		if(popWindow != null && popWindow.isShowing()){
			popWindow.dismiss();
		}
		ringMenuShow = null;
	}

	private void ringMenuHandler(int leftRight, int buttonNum, OsListViewItem item) {
		if (leftRight == 0 && buttonNum == 2) {
			// left menu with two button
			leftMenuTwoButton(item);
		}
	}

	private void leftMenuTwoButton(final OsListViewItem item) {
		// LayoutInflater inflater = (LayoutInflater)
		// getSystemService(LAYOUT_INFLATER_SERVICE);
		// View view = inflater.inflate(R.layout.ring_menu_left_two_button,
		// (ViewGroup) findViewById(R.id.two_button_left));
		ring_delete = (ImageView) ringMenuShow.findViewById(R.id.btnDelete);
		ring_rename = (ImageView) ringMenuShow.findViewById(R.id.btnRename);
		
		//2013-4-7 -Amy- Combination OnclickListener
		OnClickListener myLeftClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId()){
					case R.id.btnDelete:
						//mViewGroup.removeView(viewa);
						deleteItem(item);
						picture_adapter.notifyDataSetChanged();
						hideRingMenu();
						break;
					case R.id.btnRename:
						hideRingMenu();
						renameLayer(item);
						break;
				}
			}
		};
		ring_delete.setOnClickListener(myLeftClickListener);
		ring_rename.setOnClickListener(myLeftClickListener);
	}

	private void renameLayer(final OsListViewItem item) {
		LayoutInflater lf = getLayoutInflater();
		mPopupLayout = (RelativeLayout) lf.inflate(R.layout.layout_popup_text_input, null);
		for (int i = 0; i < mPopupLayout.getChildCount(); i++) {
			View view = mPopupLayout.getChildAt(i);
			if (view.getId() == R.id.imageViewQuit) {
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						hidePopupMessageLayout();
					}
				});
			} else if(view.getId() == R.id.textViewOkBtn) {
				TextView okButton = (TextView)view;
				okButton.setText(R.string.picture_btn_ok);
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean isExist = false;
						badWordChecker.setBlacklist(blackListWord);
						String renameStr = mRenameText.getText().toString();

						String renameErr = (String)getResources().getString(R.string.picture_title_rename_error);

						//2013-03-22 - raymond - avoid rename system folder
						if(item.getName().equals("data") || item.getName().equals("extsd")){
							mRenameText.setText("");
						}

						//2013-03-20 - raymond - replace all special char
						String tmpName = mRenameText.getText().toString();
						boolean shouldBlock = false;
						if(tmpName.indexOf("%")>=0 || tmpName.indexOf("^")>=0 || tmpName.indexOf("&")>=0 || tmpName.indexOf("*")>=0){
							shouldBlock=true;
						}
						if(tmpName.indexOf("!")>=0 || tmpName.indexOf("@")>=0 || tmpName.indexOf("#")>=0 || tmpName.indexOf("$")>=0){
							shouldBlock=true;
						}
						if(tmpName.indexOf("(")>=0 || tmpName.indexOf(")")>=0 || tmpName.indexOf(";")>=0 || tmpName.indexOf(":")>=0){
							shouldBlock=true;
						}
						if(tmpName.indexOf(",")>=0 || tmpName.indexOf(".")>=0 || tmpName.indexOf("/")>=0 || tmpName.indexOf("\\")>=0){
							shouldBlock=true;
						}
						if(tmpName.indexOf("'")>=0 || tmpName.indexOf("\"")>=0 || tmpName.indexOf("*")>=0){
							shouldBlock=true;
						}		
						
					if(shouldBlock || tmpName.equals("")){					
						String renameErrMsgInvalidName = (String)getResources().getString(R.string.picture_msg_blocked);
						notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName);
						return;
					}else{
						mRenameText.setText(tmpName);
					}

					
						if (!renameStr.equals("")) {
							// renamePhoto(renameFileName+".png", item);
							for (int i = 0; i < mListViewItemList.size(); i++) {
								// Log.d("All photo name", "All photo name: " +
								// mListViewItemList.get(i).getName());
								// Log.d("All photo name", "All photo name: " +
								// mListViewItemList.get(i).getName().substring(0,
								// mListViewItemList.get(i).getName().lastIndexOf(".")));
								//2013-3-18 -Amy- Rename after refres
								//if (mListViewItemList.get(i).getName().substring(0, mListViewItemList.get(i).getName().lastIndexOf(".")).toLowerCase().equals(mRenameText.getText().toString().toLowerCase())) {
								if (mListViewItemList.get(i).getName().equals(renameStr.toLowerCase())) {
									isExist = true;
								}
							}

							
							if (isExist){
								String renameErrMsgFormat = (String)getResources().getString(R.string.picture_msg_rename_error);
								String renameErrMsg = String.format(renameErrMsgFormat, renameStr.toString());
								notification = new NotificationMessage(mContext, null, renameErr, renameErrMsg);
//								notification = new NotificationMessage(mContext, null, "Rename error", "This destination already contains a file named \"" + mRenameText.getText().toString() + "\". \nPlease check it before renaming this file.");
//								notification.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
							} else if ((renameStr.substring(0, 1)).equals(".")){
								String renameErrMsgInvalidName = (String)getResources().getString(R.string.picture_msg_name_invalid);
								notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName);		
//								notification = new NotificationMessage(mContext, null, "Rename error", "The file name cannot start with \".\", please change the file name");
//								notification.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

							} else if (!badWordChecker.isStringSafe(renameStr)){
								String renameErrMsgInvalidName = (String)getResources().getString(R.string.picture_msg_blocked);
								notification = new NotificationMessage(mContext, null,renameErr, renameErrMsgInvalidName);		
							} else {
								String pathTo = item.getPath().substring(0, item.getPath().lastIndexOf(File.separator)+1) + renameStr + '.'+getExtension(item.getPath());
								renameFile(renameStr, item);
								item.setName(renameStr);
								item.setPath(pathTo);
								picture_adapter.notifyDataSetChanged();
								
								/*renameFile(mRenameText.getText().toString(), item);
								redrawListView();*/
							}
						}
						hidePopupMessageLayout();
					}
				});
			} else if (view.getId() == R.id.editTextRename) {
				mRenameText = (EditText)view;
				
			} else if (view.getId() == R.id.textViewNotice) {
				TextView textviewTitle = (TextView)view;
				textviewTitle.setText(R.string.picture_btn_ring_rename);
				
			}else if(view.getId() == R.id.imageViewEditUserBg){
				//2012-3-22 - Amy - During renaming, then touch the screen outside the Rename blank, it will enter into corresponding moive or others
				view.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						return true;
					}
				});
			}
			
		}
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
		/*
		final Dialog dialog = new Dialog(mContext);
		//dialog.setContentView(R.layout.notification_rename);
		dialog.setContentView(R.layout.layout_popup_text_input);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//String rename = "Rename Photo from \"" + item.getName().substring(0, item.getName().lastIndexOf(".")) + "\" to: ";

		// LayoutInflater layoutInflater = (LayoutInflater)
		// mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View view = layoutInflater.inflate(R.layout.notification_message,
		// mViewGroup);

		// TextView textViewTitle = (TextView)
		// dialog.findViewById(R.id.textViewTitle);
		// textViewTitle.setText("Rename");

		TextView textViewOkBtn = (TextView) dialog.findViewById(R.id.textViewOkBtn);

		//TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessage);
		//textViewMessage.setText(rename);

		// ImageView imageViewIcon = (ImageView)
		// dialog.findViewById(R.id.imageViewIcon);
		// imageViewIcon.setImageResource(R.drawable.ic_launcher);

		ImageView imageViewQuit = (ImageView) dialog.findViewById(R.id.imageViewQuit);
		imageViewQuit.setImageResource(R.drawable.quit_room);

		final EditText editTextRename = (EditText) dialog.findViewById(R.id.editTextRename);
		editTextRename.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

		imageViewQuit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View pV) {
				dialog.dismiss();
			}
		});

		textViewOkBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View pV) {
				String renameFileName = editTextRename.getText().toString();
				boolean isExist = false;
				if (!renameFileName.equals("")) {
					// renamePhoto(renameFileName+".png", item);
					for (int i = 0; i < mListViewItemList.size(); i++) {
						// Log.d("All photo name", "All photo name: " +
						// mListViewItemList.get(i).getName());
						// Log.d("All photo name", "All photo name: " +
						// mListViewItemList.get(i).getName().substring(0,
						// mListViewItemList.get(i).getName().lastIndexOf(".")));
						if (mListViewItemList.get(i).getName().substring(0, mListViewItemList.get(i).getName().lastIndexOf(".")).equals(renameFileName)) {
							isExist = true;
						}
					}

					if (isExist){
						notification = new NotificationMessage(mContext, null, "Rename error", "This destination already contains a file named \"" + renameFileName + "\". \nPlease check it before renaming this file.");
					}else if ((renameFileName.substring(0, 1)).equals(".")){
						notification = new NotificationMessage(mContext, null, "Rename error", "The file name cannot start with \".\", please change the file name");			
					} else {
						renameFile(renameFileName+".png", item);
					}
				}
				dialog.dismiss();
				redrawListView();
			}
		});*/

//		dialog.show();

		// return Rename;
	}
	//2013-03-26 - raymond - remove Jump to gridview feature
	//2013-3-22 -Amy- when click back, Jump to gridview 
	//2013-4-2 -Amy- when click back, Jump to gridview --add mPopupLayout != null && 
	public void onBackPressed() {
		if(mPopupLayout != null && mPopupLayout.isShown()){
			mViewGroup.removeViewInLayout(mPopupLayout);
		}else{
			super.onBackPressed();
		}
		return;
	}
	
	private void hidePopupMessageLayout() {
		//2013-03-25 - raymond - dismiss rename keyboard
		if(mTxtRename!=null){
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mTxtRename.getWindowToken(), 0);
		}		
		mViewGroup.removeView(mPopupLayout);
		mPopupLayout = null;
	}
	
	private void renameFile(String renameFileName, OsListViewItem item) {
		String pathFrom = item.getPath();
		String pathTo = pathFrom.substring(0, pathFrom.lastIndexOf(File.separator)+1) + renameFileName + '.'+getExtension(pathFrom);

		Log.i("path from", "path from: " + pathFrom);
		Log.i("path to", "path to: " + pathTo);

		
		File from = new File(item.getPath());
		File to = new File(pathTo);
		String pngName = from.getName().substring(0, from.getName().length()-4) + Global.FILE_TYPE_PNG;
		from.renameTo(to);

		File fromLargeIcon = new File(PATH_LARGE_ICON_DIR + item.getName());
		if (fromLargeIcon.exists()) {
			File toLargeIcon = new File(PATH_LARGE_ICON_DIR + renameFileName);
			fromLargeIcon.renameTo(toLargeIcon);
		}

		File fromDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + item.getName());
		if (fromDimIcon.exists()) {
			File toDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + renameFileName);
			fromDimIcon.renameTo(toDimIcon);
		}

		File fromSmallIcon = new File(PATH_SMALL_ICON_DIR + item.getName());
		if (fromSmallIcon.exists()) {
			File toSmallIcon = new File(PATH_SMALL_ICON_DIR + renameFileName);
			fromSmallIcon.renameTo(toSmallIcon);
		}

		File fromPrivateDataIcon = new File(MeepStorageCtrl.getDataFolderPath(AppType.Photo) + pngName);
		if (fromPrivateDataIcon.exists()) {
			File toPrivateDataIcon = new File(MeepStorageCtrl.getDataFolderPath(AppType.Photo) + renameFileName);
			fromPrivateDataIcon.renameTo(toPrivateDataIcon);
		}

		File fromPrivateLargeDimIcon = new File(MeepStorageCtrl.getLargeDimIconFolderPath(AppType.Photo) + pngName);
		if (fromPrivateLargeDimIcon.exists()) {
			File toPrivateLargeDimIcon = new File(MeepStorageCtrl.getLargeDimIconFolderPath(AppType.Photo) + renameFileName);
			fromPrivateLargeDimIcon.renameTo(toPrivateLargeDimIcon);
		}

		File fromPrivateLargeIcon = new File(MeepStorageCtrl.getLargeIconFolderPath(AppType.Photo) + pngName);
		if (fromPrivateLargeIcon.exists()) {
			File toPrivateLargeIcon = new File(MeepStorageCtrl.getLargeIconFolderPath(AppType.Photo) + renameFileName);
			fromPrivateLargeIcon.renameTo(toPrivateLargeIcon);
		}

		File fromPrivateLargeMirrorIcon = new File(MeepStorageCtrl.getLargeMirrorIconFolderPath(AppType.Photo) + pngName);
		if (fromPrivateLargeMirrorIcon.exists()) {
			File toPrivateLargeMirrorIcon = new File(MeepStorageCtrl.getLargeMirrorIconFolderPath(AppType.Photo) + renameFileName);
			fromPrivateLargeMirrorIcon.renameTo(toPrivateLargeMirrorIcon);
		}
		
		File fromPrivateSmallIcon = new File(MeepStorageCtrl.getSmallIconFolderPath(AppType.Photo) + pngName);
		if (fromPrivateSmallIcon.exists()) {
			File toPrivateSmallIcon = new File(MeepStorageCtrl.getSmallIconFolderPath(AppType.Photo) + renameFileName);
			fromPrivateSmallIcon.renameTo(toPrivateSmallIcon);
		}
	}

	private void initHandler() {
		mHandlerReadImg = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					int idx = msg.getData().getInt("imgIdx");
					//drawImage(idx);
					Log.w("thread", "thread full list view alive");
					break;
				case 2: 
					popupFragment.dismiss();
					Log.e("cdf","-------add three shelves  then refresh pictures------------");
					picture_adapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
				//mLayout.invalidate();
				super.handleMessage(msg);
			}
		};

		/*mThread = new Thread(runReadImage);
		mThread.start();*/
	}

	private Runnable runReadImage = new Runnable() {
		
		@Override
		public void run() {
			Looper.prepare();
			
			Looper.loop();
			try {
				mThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// ******start define listener******
	OnGestureListener mOnGuestureListener = new OnGestureListener() {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// if (mEditMode == EditMode.VIEW) {
			// if (mSelectedView != null) {
			// String name = (String) mSelectedView.getTag();
			// OsListViewItem item = findListViewItemByName(name);
			// String[] paths = getAllPhotoPaths();
			// if (mEditMode == EditMode.VIEW) {
			// if (mSelectedView.getTag() != null) {
			// if (item != null) {
			// goToPhoto(paths, item.getPath());
			// }
			// }
			// }
			// }
			// } else {
			// hideOption();
			// }
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

			// float x = e.getX();
			// float y = e.getY();
			// try
			// {
			// showRingMenu(0,1, x, y);
			// }
			// catch (Exception ex) {
			// Log.e("Ringmenu", "error:" + ex.toString());
			// }
			// showOption(x,y);
			// float x = e.getX() - 120 + mCorX;
			// float y = e.getY() - 90 + mCorY;
			//
			// Log.i("ring menu XY:", "x:"+ e.getX() +" y:"+e.getY()+ " mCorX:"
			// + mCorX + " mCorY:" + mCorY);
			//
			// if (ringMenu == null){
			// showRingMenu(x, y);
			// }else {
			// hideRingMenu();
			// }
			setEditMode(EditMode.OPTION_SHOWN);
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

	//2013-4-7 -Amy- Combination OnclickListener
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.imageViewBtnBlock:
					setEditMode(EditMode.DELETE);
					break;
				case R.id.imageViewBtnEdit:
					mRenameLayout.setVisibility(View.VISIBLE);
					mTxtRename.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(mTxtRename, InputMethodManager.SHOW_IMPLICIT);
					break;
				case R.id.pic:
					Log.i("mEditMode", "mEditMode:" + mEditMode.toString());
					if (mEditMode == EditMode.VIEW) {
						String name = (String) v.getTag(R.id.TAG);
	
						OsListViewItem item = findListViewItemByName(name);
						String[] paths = getAllPhotoPaths();
						if (mEditMode == EditMode.VIEW) {
							if (item != null && ringMenuShow == null) {
								Log.i("mEditMode", "mEditMode2:" + item.getPath());
								goToPhoto(paths, item.getPath());
							}
							hideRingMenu();
						}
					}
					break;
				default:
					break;
			}
		}
	};
	/*OnClickListener btnOptonOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// ImageView btnView = (ImageView)v;
			// if (btnView.getText().equals(DELETE)) {
			// setEditMode(EditMode.DELETE);
			// deleteItem((String)v.getTag());
			// } else if (btnView.getText().equals(RENAME)) {
			// setEditMode(EditMode.EDIT);
			// } else {
			// setEditMode(EditMode.OPTION_SHOWN);
			// }
		}
	};*/
	OnTouchListener scrollViewOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				hideRingMenu();
				final int[] location = new int[2];
				v.getLocationOnScreen(location);
				mCorX = location[0];
				mCorY = location[1];
			}
			mGuestureDetector.onTouchEvent(event);
			return false;
		}
	};

	// OnClickListener btnItemOnclickListener = new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// String name = (String) v.getTag();
	// OsListViewItem item = findListViewItemByName(name);
	// String[] paths = getAllPhotoPaths();
	// if (mEditMode == EditMode.VIEW) {
	// if (v.getTag() != null) {
	// if (item != null) {
	// goToPhoto(paths, item.getPath());
	// }
	// }
	// }else if (mEditMode == EditMode.DELETE){
	// RelativeLayout layout = (RelativeLayout)v;
	// ImageView tickView = (ImageView) layout.getChildAt(2);
	// if (tickView.getVisibility() != View.VISIBLE) {
	// tickView.setVisibility(View.VISIBLE);
	// item.setIsChecked(true);
	// } else {
	// tickView.setVisibility(View.GONE);
	// item.setIsChecked(false);
	// }
	// }
	// }
	// };

	OnTouchListener btnItemOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			hideRingMenu();
			// if(event.getAction()==MotionEvent.ACTION_DOWN)
			// {
			// mSelectedView = v;
			// final int[] location = new int[2];
			// v.getLocationOnScreen(location);
			// mCorX = location[0];
			// mCorY = location[1];
			// }
			// else if (event.getAction()==MotionEvent.ACTION_UP)
			// {
			// if(mEditMode !=EditMode.VIEW)
			// {
			// mSelectedView = null;
			// }
			// }
			// mItemGuestureDetector.onTouchEvent(event);
			return true;
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

			String name = (String) v.getTag(R.id.TAG);
			OsListViewItem item = findListViewItemByName(name);
			showRingMenu(x, y, item);

			return true;
		}
	};

	// end define listener

	private OsListViewItem findListViewItemByName(String name) {
		for (int i = 0; i < mListViewItemList.size(); i++) {
			if (mListViewItemList.get(i).getName().equals(name)) {
				return mListViewItemList.get(i);
			}
		}
		return null;
	}

	
	private void traverseDir(File dir, List<File> theList) {
		Log.i("photo-traversal-(Photo)", "traverseDir LOADED");
		File[] files = dir.listFiles();
		
		if (files != null) {
			
			for (int i = 0; i < files.length; i++) {
				
				
			    // skip all files or folders starting with a dot
				
			    if (files[i].getName().startsWith(".")) {
			        continue;
			    }

			    if (files[i].isDirectory()) {
					traverseDir(files[i], theList);
				} else {
					if (!files[i].isHidden() && (files[i].getName().toLowerCase().contains(Global.FILE_TYPE_PNG) || files[i].getName().toLowerCase().contains(Global.FILE_TYPE_JPG) || files[i].getName().toLowerCase().contains(Global.FILE_TYPE_JEPG))) 
					{
						
						//2013-04-10 Winder Hao-- To determine whether the resource as a picture file to exclude with extension png and jpg false picture
						if (isRealFormat(files[i])==true) {
						//traverseDir(new File("/mnt/sdcard/DCIM/Camera/"), fileList);
						
							theList.add(files[i]);
						}
					}
						//Log.i("photo-traversal-(Photo)", files[i].getAbsolutePath());
					}
				}
			}
		}
    //2013-4-12 winder hao To determine whether the file can take to the height and width
	private boolean isRealFormat(File file) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;  
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		if (options.outHeight<=0 && options.outWidth<=0)
		{
			return false;
		}
		return true;
	}

	private void traverseFileList(List<File> List1, List<File> finalList) {
		//Log.i("photo-traversal", "traverseDir LOADED");
		if (List1 != null) {
			for (int i = 0; i < List1.size(); i++) {
                // skip all files or folders starting with a dot
                if (List1.get(i).getName().startsWith(".")) {
                    continue;
                }

                if (!List1.get(i).isHidden()) {
                	if (isRealFormat(List1.get(i))==true)
					{
                		finalList.add(List1.get(i));
					}
					
					//Log.i("traverseFileList", List1.get(i).getAbsolutePath());

				}
			}
		}
	}

//	private void loadAndDrawImage() {
//		List<File> fileList = new ArrayList<File>();
//		traverseDir(new File("/mnt/sdcard/DCIM/Camera/"), fileList);
//		Collections.sort(fileList);
//		Collections.reverse(fileList);
//		List<File> tempFileList = new ArrayList<File>();
//		traverseDir(new File("/mnt/extsd/"), tempFileList);
//		traverseDir(new File(PATH_PHOTO_DATA_DIR), tempFileList);
//		Collections.sort(tempFileList);
//		traverseFileList(tempFileList, fileList);
//
//		Log.d("fileList", "count:" + fileList.size());
//
//		int count = 0;
//		// draw default 3 shell
//		Message msg2 = new Message();
//		msg2.what = 2;
//		mHandlerReadImg.sendMessage(msg2);
//		
//		for (int i = 0; i < fileList.size(); i++) {
//			File thisFile = fileList.get(i);
//			String filename = thisFile.getName();
//			Bitmap bmap = null;
//
//			// try to load small icon first
//			bmap = BitmapFactory.decodeFile(PATH_SMALL_ICON_DIR + filename);
//
//			if (bmap == null) {
//				bmap = BitmapFactory.decodeFile(PATH_LARGE_ICON_DIR + filename);
//				if (bmap == null) {
//					//bmap = BitmapFactory.decodeFile(thisFile.getAbsolutePath());
//					
//					//generate a large photo from original one and save it
//					pathCoverUpL = PHOTO_PATH_CD_COVER_UP_L;
//					pathCoverDownL= PHOTO_PATH_CD_COVER_DOWN_L;
//					String largeImgPath = PATH_LARGE_ICON_DIR + filename;
//					String dataImagePath = thisFile.getAbsolutePath();
//					
//					Bitmap originalImage = BitmapFactory.decodeFile(dataImagePath);
//					
//					Bitmap bottomImage = getCoverImageDown();
//					Bitmap topImage =  getCoverImageUp();
//					Bitmap largeImage = getPhotoIconL(originalImage, bottomImage, topImage, 247, 200, 28, 28, filename);
//					
//					originalImage.recycle();
//					
//					saveImage(largeImage, largeImgPath);
//					bmap = largeImage;
//					//largeImage.recycle();
//				}
//				if (bmap != null) {
//					bmap = MediaManager.getIconForListView(bmap);
//					MediaManager.saveImageToExternal(bmap, PATH_SMALL_ICON_DIR + filename);
//				}
//				// Create thumbnail on fly (TODO: save it)
//				// bmap =
//				// MediaManager.generateMovieThumbnail(thisFile.getAbsolutePath());
//				// bmap = ThumbnailUtils.extractThumbnail(bmap, 90, 107);
//			}
//
//			if (bmap != null) {
//				OsListViewItem item = new OsListViewItem();
//				item.setImage(bmap);
//				item.setName(filename);
//				item.setPath(thisFile.getAbsolutePath());
//				mListViewItemList.add(item);
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				data.putInt("imgIdx", count);
//				msg.setData(data);
//				msg.what = 1;
//				mHandlerReadImg.sendMessage(msg);
//				count++;
//			}
//		}
//	}
//	

	// 2013-4-2 - Zoya - SDCardListener class
	private final BroadcastReceiver sdcardListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("tag", "sdcard action----" + action);
			if (action.equals("android.intent.action.MEDIA_MOUNTED")) {// mount SDcard success
				loadImages = new LoadImagesThread();
				loadImages.start();
			} else if (action.equals("android.intent.action.MEDIA_REMOVED")// SDcard is not mounted
					|| action.equals("android.intent.action.MEDIA_UNMOUNTED")
					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")) {
				loadImages = new LoadImagesThread();
				loadImages.start();
			}
		}
	};

	// 2013-4-2 - Zoya - registerSDCardListener
	private void registerSDCardListener() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addDataScheme("file");
		registerReceiver(sdcardListener, intentFilter);
	}
		
	private static class ViewHolder {  
		ImageView pic;
		TextView pic_title;
	}  
	
	private class PicturesAdapter extends BaseAdapter
	{
		private LayoutInflater inflater = LayoutInflater.from(FullListViewActivity.this);
		
		@Override
		public synchronized int getCount() {
			if(mListViewItemList == null) {
				return 0;
			}else if(mListViewItemList.size() <= 15){
				return 15;
			} else {
				return mListViewItemList.size();
			}
		}

		@Override
		public synchronized Object getItem(int position) {
			if (mListViewItemList == null) {
				return null;
			}else if(position < mListViewItemList.size()){
				return mListViewItemList.get(position);
			}
			return null;
		}

		@Override
		public synchronized long getItemId(int position) {
			if (mListViewItemList == null) {
				return -1;
			} else if(position < mListViewItemList.size()){
				return mListViewItemList.get(position).hashCode();
			}else {
				return -1;
			}
		}
		@Override
		public synchronized View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			
			if(convertView == null){
				holder = new ViewHolder();
				
				convertView = inflater.inflate(R.layout.gridview_item, null);
				
				holder.pic = (ImageView)convertView.findViewById(R.id.pic);
				holder.pic_title = (TextView) convertView.findViewById(R.id.pic_title);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(position < mListViewItemList.size()){
				final OsListViewItem item = mListViewItemList.get(position);
				holder.pic_title.setText(item.getName());
				//Log.e("Photoitem.getName()-----------",""+item.getName().substring(0,item.getName().lastIndexOf(".")));
				//holder.pic.setImageBitmap(item.getImage());
				holder.pic.setTag(R.id.TAG, item.getName());
				holder.pic.setOnClickListener(onClickListener);
				holder.pic.setOnLongClickListener(btnItemLongClickListener);
				//mImageDownloader.download(item.getPath(), SHOW_WIDTH, SHOW_HEIGHT, holder.pic);
				final String path = item.getPath();
				mImageDownloader.download(path, holder.pic, mBmpPrevieTop, null);
				convertView.setVisibility(View.VISIBLE);
			}else{
				holder.pic.setImageBitmap(null);
				holder.pic_title.setText(null);
				holder.pic.setTag(R.id.TAG, null);
				holder.pic.setOnClickListener(null);
				holder.pic.setOnLongClickListener(null);
				convertView.setVisibility(View.INVISIBLE);
			}
			
			return convertView;
		}
	}
	
	class LoadImagesThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(mListViewItemList != null){
				mListViewItemList.clear();
			}
			Message message = new Message();
			loadAndDrawImage2();
			message.what = 2;
			mHandlerReadImg.sendMessage(message);
			super.run();
		}
	}
	
	private synchronized void loadAndDrawImage2() {
		List<File> fileList = new ArrayList<File>();
		traverseDir(new File("/mnt/sdcard/DCIM/Camera/"), fileList);
		Collections.sort(fileList);
		Collections.reverse(fileList);
	 
		
		List<File> tempFileList = new ArrayList<File>();
		traverseDir(new File("/mnt/extsd/"), tempFileList);
		traverseDir(new File(PATH_PHOTO_DATA_DIR), tempFileList);
		traverseDir(new File("/mnt/sdcard/home/photo/data/"), tempFileList);
		traverseDir(new File("/mnt/sdcard/PicArts/"), tempFileList);
		traverseDir(new File("/mnt/sdcard/PicArt/"), tempFileList);
		Collections.sort(tempFileList);
		traverseFileList(tempFileList, fileList);
		
		Log.d("fileList", "count:" + fileList.size());
		 
		// draw default 3 shell
		/*Message msg2 = new Message();
		msg2.what = 2;
		mHandlerReadImg.sendMessage(msg2);*/
		for (int i = 0; i < fileList.size(); i++) {
			File thisFile = fileList.get(i);
			String filename = thisFile.getName();
	
			
			//2013-3-18 -Amy- Rename after refres
			//Log.e("cdf--filename- ", filename);
			String photoName = filename.substring(0,filename.lastIndexOf("."));
			//Log.e("cdf--photoName- ", photoName);
			
			//String pngName = filename.substring(0, thisFile.getName().length() - 4) + Global.FILE_TYPE_PNG;
			
			// try to load small icon first
			//Bitmap bmap = null;
			/*try {
			bmap = MeepStorageCtrl.getPrivateSmallIcon(
					pngName, 
					thisFile.getAbsolutePath(), 
					AppType.Photo, 
					mBmpPreviewBg, 
					mBmpPrevieTop);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			if (bmap == null) {
				//continue;
				//return;
			} else {*/
				OsListViewItem item = new OsListViewItem();
				//item.setImage(bmap);
				item.setName(photoName);
				item.setPath(thisFile.getAbsolutePath());
				mListViewItemList.add(item);
				/*Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("imgIdx", count);
				msg.setData(data);
				msg.what = 1;
				mHandlerReadImg.sendMessage(msg);*/
				//count++;
			//}
		}
		}
		// dismiss loading spinner after finish loading all images
		/*if (popupFragment != null) {
			popupFragment.dismiss();
		}*/
		
	
	
	private synchronized Bitmap getPhotoPrivateSmallIcon(String name) {
		File file = new File(PATH_PRIVATE_SMALL_ICON_PATH + name);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			return null;
		}
	}
	
	private synchronized void saveImage(Bitmap bmp, String path) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bmp.compress(CompressFormat.PNG, 80, os);
			os.flush();
			Log.w("SnackShape2","saveImage to :" + path);
		} catch (Exception e) {
			Log.w("SnackShape2","saveImage fail:" + e.toString());
		}
	}
	
	private synchronized Bitmap getCoverImageDown()
	{
		if(mCoverImageDown == null){
			return BitmapFactory.decodeFile(pathCoverDownL);
		}
		return mCoverImageDown;
	}
	
	private synchronized Bitmap getCoverImageUp() {
		if (mCoverImageUp == null) {
			return BitmapFactory.decodeFile(pathCoverUpL);
		}
		return mCoverImageUp;
	}
	

	private synchronized Bitmap getPhotoIconL(Bitmap orgImage, Bitmap bottom, Bitmap top, int w, int h, int leftMargin, int topMargin, String name) {
		Bitmap bmpBg = bottom;
		Bitmap bmpTop = top;
		Bitmap bmp = orgImage;
		float scale = (float)h / bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		int cutx = (int) (resizedBitmap.getWidth() / 2 - w/2);
		Bitmap cropBitmap = null;
		int moveX = 0;
		if (resizedBitmap.getWidth() > w) {
			cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, w, resizedBitmap.getHeight());
		} else {
			moveX = (int)((w - resizedBitmap.getWidth())/2);
			cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
		}

		Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		
		canvas.drawBitmap(cropBitmap, leftMargin + moveX, topMargin, null);
		canvas.drawBitmap(bmpTop, 0, 0, null);
		
		return mutableBm;
	}

	private synchronized void redrawListView() {
		//mLayout.removeAllViews();
		mListViewItemList.removeAll(mListViewItemList);
		loadAndDrawImage2();

		// for (int i = 0; i < mListViewItemList.size(); i++) {
		// drawImage(i);
		// }
	}

	/*private void drawShelfOnly() {
		LinearLayout ll = null;

		ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.movie_shelf);
		ll.setPadding(0, 0, 0, 15);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		mLayout.addView(ll);
		
		ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.movie_shelf);
		ll.setPadding(0, 0, 0, 15);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		mLayout.addView(ll);
		
		ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.movie_shelf);
		ll.setPadding(0, 0, 0, 15);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		mLayout.addView(ll);
		
	}*/
	
	/*private void drawImage(int index) {

		OsListViewItem item = mListViewItemList.get(index);
		boolean isNewRow = false;
		LinearLayout ll = null;
		if (index < 15) {

			ll = (LinearLayout) mLayout.getChildAt((int) ((index) / 5));
		} else {
			if (mLayout.getChildCount() > 0) {
				ll = (LinearLayout) mLayout.getChildAt(mLayout.getChildCount() - 1);
			}

			if (mLayout.getChildCount() == 0 || ll.getChildCount() >= 5) {
				ll = new LinearLayout(this);
				ll.setBackgroundResource(R.drawable.movie_shelf);
				ll.setPadding(0, 0, 0, 15);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				isNewRow = true;
			}
		}

		// image
		ImageView image = new ImageView(this);
		image.setImageBitmap(item.getImage());
		image.setTag(item.getName());
		//image.setPadding(0, 0, 0, 20);
		// image.setOnTouchListener(btnItemOnTouchListener);
		RelativeLayout.LayoutParams lpImg = new RelativeLayout.LayoutParams(100, 100);
		lpImg.setMargins(0, 15 , 0, 0);
		image.setLayoutParams(lpImg);
		image.setOnClickListener(btnItemClickListener);
		image.setOnLongClickListener(btnItemLongClickListener);
		image.setLongClickable(true);

		// name
		TextView txt = new TextView(this);
		String tempName = item.getName().substring(0, item.getName().length() - 4);
		if (tempName.length() > 15) {
			tempName = tempName.substring(0, 13) + "..";
		} else {
			tempName = item.getName().substring(0, item.getName().length() - 4);
		}
		txt.setText(tempName);
		
		//txt.setText(item.getName().substring(0, item.getName().length() - 4));
		txt.setBackgroundResource(R.drawable.text_bar);
		// txt.setOnTouchListener(btnItemOnTouchListener);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 120, 0, 0);
		txt.setWidth(103);
		txt.setLayoutParams(lp);
		txt.setGravity(Gravity.CENTER);

		// tick icon
		ImageView imageTick = new ImageView(this);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(42, 0, 0, 0);
		imageTick.setLayoutParams(lp2);
		imageTick.setImageBitmap(mImgTick);
		if (mEditMode == EditMode.DELETE) {
			imageTick.setVisibility(View.VISIBLE);
		} else {
			imageTick.setVisibility(View.GONE);
		}
		imageTick.setTag(item.getName());
		imageTick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = (String) v.getTag();
				OsListViewItem item = findListViewItemByName(name);
				deleteItem(item);
			}
		});

		RelativeLayout rl = new RelativeLayout(this);
		// rl.setOnTouchListener(btnItemOnTouchListener);
		rl.setTag(item.getName());
		if (ll.getChildCount() == 0) {
			rl.setPadding(20, 10, 15, 0);
		} else {
			rl.setPadding(15, 10, 15, 0);
		}
		// rl.setOnClickListener(btnItemOnclickListener);

		rl.addView(image);
		rl.addView(txt);
		rl.addView(imageTick);

		ll.addView(rl);
		ll.setOnTouchListener(btnItemOnTouchListener);
		if (isNewRow) {
			try {
				mLayout.addView(ll);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}*/

	private synchronized void goToPhoto(String[] paths, String selectedPath) {
		//Intent intent = new Intent(this, MeepPhotoGalleryActivity.class);
		Intent intent = new Intent(this, MeepPhotoViewPagerActivity.class);
		// intent.setComponent(ComponentName
		// .unflattenFromString(Global.APP_PHOTO_VIEWER_PACKAGE_STRING));
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

	private void showOption(float x, float y) {
		mOptionLayout.setX(x);
		mOptionLayout.setY(y);
		mOptionLayout.setVisibility(View.VISIBLE);
	}

	private void hideOption() {
		// mOptionLayout.setVisibility(View.GONE);
		// Log.d("ringMenu", "hide option called");
		hideRingMenu();
		setEditMode(EditMode.VIEW);
	}

	private boolean isDeleteButtonShown = false;

	// private boolean isRenameButtonShown = false;

	private synchronized void setEditMode(EditMode editMode) {
		mEditMode = editMode;
		if (editMode == EditMode.DELETE) {
			// showAllDeleteIcons();
			isDeleteButtonShown = true;
			// mBtnConfirm.setVisibility(View.VISIBLE);
			// } else if(editMode == EditMode.RENAME) {
			// isRenameButtonShown = true;
			// //mBtnConfirm.setVisibility(View.VISIBLE);
		} else {
			if (isDeleteButtonShown) {
				hideAllDeleteIcons();
			}

			// if (isRenameButtonShown) {
			//
			// }

			// mBtnConfirm.setVisibility(View.GONE);
		}
	}

	/*private void showAllDeleteIcons() {
		for (int i = 0; i < mLayout.getChildCount(); i++) {
			LinearLayout rowLayout = (LinearLayout) mLayout.getChildAt(i);
			for (int j = 0; j < rowLayout.getChildCount(); j++) {
				RelativeLayout rl = (RelativeLayout) rowLayout.getChildAt(j);
				if (rl != null) {
					ImageView img = (ImageView) rl.getChildAt(2);
					img.setVisibility(View.VISIBLE);
				}
			}
		}
	}*/

	private void hideAllDeleteIcons() {
		for (int i = 0; i < mListViewItemList.size(); i++) {
			RelativeLayout rl = (RelativeLayout) mOptionLayout.getChildAt(i);
			if (rl != null) {
				ImageView img = (ImageView) rl.getChildAt(2);
				img.setVisibility(View.GONE);
			}
		}
	}

	private void changeToEditMode() {
		for (int i = 0; i < mListViewItemList.size(); i++) {

		}
	}

	private void changeToDeleteMode() {

	}

	private void deleteItem(final OsListViewItem item) {

		File file = new File(item.getPath());
		if (file.exists()) {
			String pngName = file.getName().substring(0, file.getName().length() - 4) + Global.FILE_TYPE_PNG;
			
			deleteItemFile(PATH_LARGE_ICON_DIR + item.getName());
			deleteItemFile(PATH_LARGE_DIM_ICON_DIR + item.getName());
			deleteItemFile(PATH_SMALL_ICON_DIR + item.getName());
			deleteItemFile(MeepStorageCtrl.getDataFolderPath(AppType.Photo) + pngName);
			deleteItemFile(MeepStorageCtrl.getLargeDimIconFolderPath(AppType.Photo) + pngName);
			deleteItemFile(MeepStorageCtrl.getLargeIconFolderPath(AppType.Photo) + pngName);
			deleteItemFile(MeepStorageCtrl.getLargeMirrorIconFolderPath(AppType.Photo) + pngName);
			deleteItemFile(MeepStorageCtrl.getSmallIconFolderPath(AppType.Photo) + pngName);
			deleteItemFile(item.getPath());

			mListViewItemList.remove(item);
			redrawListView();
			picture_adapter.notifyDataSetChanged();
		}
	}

	private void deleteItemFile(String path) {
		// delete the real item
		File file = new File(path);
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			Log.e("MovieFullListView", "delete file error : " + e.toString());
		}
	}

	private void goToMeepPhotoView() {

		long memory = Debug.getNativeHeapAllocatedSize();
		long free = Debug.getNativeHeapFreeSize();
		Log.d("ramtest", "fulllistview : local use:" + memory + " free:" + free);

		Intent myIntent = new Intent(this, MeepPhotoActivity.class);
		myIntent.putExtra(Global.STRING_TYPE, "photo");

		try {
			startActivityForResult(myIntent, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private synchronized String[] getAllPhotoPaths() {
		String[] photoPaths = new String[mListViewItemList.size()];
		for (int i = 0; i < mListViewItemList.size(); i++) {
			photoPaths[i] = mListViewItemList.get(i).getPath();
		}
		return photoPaths;
	}
	
	private void getPhotoBlackList()
	{
		String sql = "select * from " + TableBlacklist.S_TABLE_NAME +  " where " + 
		// TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Photo + "'" ;
		TableBlacklist.S_LIST_TYPE + " = 'browser'" ;
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_BLACK_LIST, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(sdcardListener);
		mMsgReceiver.close();
	}
	
	
	private synchronized Bitmap getBitmap(String name) {
		Bitmap b = null;
		if(mNameList.contains(name)){
			b = mImageList.get(mNameList.indexOf(name));
		} else {
			b = getBitmapFromDisk(name);
			mNameList.add(name);
			mImageList.add(b);
		}
		if (mNameList.size() > 80) {
			mNameList.remove(0);
			mImageList.get(0).recycle();
			mImageList.remove(0);
		}
		return b;
	}
	
	private Bitmap getBitmapFromDisk(String path) {
		String pngName = path.substring(0, path.length() - 4) + Global.FILE_TYPE_PNG;
		
		// try to load small icon first
		Bitmap bmap = null;
		try {
		bmap = MeepStorageCtrl.getPrivateSmallIcon(
				pngName, 
				path, 
				AppType.Photo, 
				mBmpPreviewBg, 
				mBmpPrevieTop);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bmap;
	}
	
	private ImageDownloader.ImageDownloadListener mDownloadListener = new ImageDownloader.ImageDownloadListener() {
		
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
			view.setImageBitmap(mBmpPrevieTop);
			
		}
		
		@Override
		public Bitmap loadImageFromUrl(String url) {
			return getBitmapFromDisk(url);
		}
	};
	
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

	public static String trimExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}
	
	
	

    //2013-4-2 -Amy- when click back, Jump to gridview --Release onBackPressed method
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if(event.getAction() == KeyEvent.ACTION_DOWN){
	       switch(keyCode)
	       {
	       case KeyEvent.KEYCODE_BACK:
	    	   Log.e("back","fun:");
	               finish();
	           return true;
	       }
	
	   }
	   return super.onKeyDown(keyCode, event);
	}*/
	


}
