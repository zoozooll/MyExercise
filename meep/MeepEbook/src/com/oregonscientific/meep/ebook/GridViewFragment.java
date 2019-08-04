package com.oregonscientific.meep.ebook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.oregonscientific.meep.control.EbookCtrl;
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
import com.oregonscientific.meep.tool.ImageDownloader;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
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
import android.widget.TextView;
import static com.oregonscientific.meep.ebook.ServiceController.*;


public class GridViewFragment extends Fragment {
	/*
	 * String RENAME = "Rename"; String ADD = "Add"; String DELETE = "Delete";
	 */

	// RingMenu ringMenu ;
	ImageView ring_delete;
	ImageView ring_rename;
	/*
	 * ImageView ring_menu_top_right_two; ImageView ring_menu_top_left_two;
	 * ImageView ring_menu_bottom_right_two; ImageView
	 * ring_menu_bottom_left_two;
	 */

	NotificationMessage notification;
	String Rename = "";

	Bitmap mBmpPreviewBg = null;
	Bitmap mBmpPrevieTop = null;

	private FullListViewActivity mContext;
	private ViewGroup mViewGroup = null;
	private String[] blackListWord;
	private BadWordChecker badWordChecker = new BadWordChecker();

	/** add new cache dir about ebook 's caches */
	private final static String CACHE_DIR = "ebook";
	private final static String BASE_STORAGE_PATH = "/data/home/";
	/*
	 * private final static String PATH_SDCARD = "/mnt/sdcard/"; private final
	 * static String PATH_EXT_SD = "/mnt/extsd/"; private final static String
	 * PATH_ICON_DIR = BASE_STORAGE_PATH + "ebook/icon/"; private final static
	 * String PATH_SMALL_ICON_DIR = BASE_STORAGE_PATH +"ebook/icon_s/"; private
	 * final static String PATH_LARGE_ICON_DIR = BASE_STORAGE_PATH
	 * +"ebook/icon_l/"; private final static String PATH_LARGE_DIM_ICON_DIR =
	 * BASE_STORAGE_PATH +"ebook/icon_ld/"; private final static String
	 * PATH_EBOOK_DATA_DIR = BASE_STORAGE_PATH +"ebook/data/"; private final
	 * static String EBOOK_PATH_BOOK_ICON_DEFAULT = BASE_STORAGE_PATH
	 * +"default/book_icon_default.png";
	 */
	private final static String PATH_FOR_UPDATE_CHECKING = BASE_STORAGE_PATH
			+ "ebook/checked";

	List<OsListViewItem> mListViewItemList = null;
	List<Bitmap> mImageList = null;
	List<String> mNameList = null;
	// 2013-3-15 - Amy - update shelves
	// LinearLayout mLayout = null;
	// ScrollView mScrollView = null;
	RelativeLayout mOptionLayout = null;
	RelativeLayout mRenameLayout = null;
	EditText mTxtRename = null;

	ImageView mSnackIcon = null;

	ImageView mBtnOptionPlay = null;
	ImageView mBtnOptionEdit = null;
	ImageView mBtnOptionDelete = null;
	ImageView mBtnOptionFavourite = null;
	ImageView mBtnOptionMessage = null;

	Button mBtnConfirm = null;
	RelativeLayout mPopupLayout = null;
	EditText mRenameText = null;

	private EditMode mEditMode = EditMode.VIEW;
	Handler mHandlerReadImg = null;
	Thread mThread = null;
	String mType = "";
	String mPath = "";
	GestureDetector mGuestureDetector = null;
	GestureDetector mItemGuestureDetector = null;

	Bitmap mImgTick = null;
	OsListViewItem mListViewItem = null;

	View mSelectedView = null;

	int mCorX = 0;
	int mCorY = 0;

	// 2013-3-15 - Amy - update shelves
	private GridView myEbooks = null;
	private EbooksAdapter ebookAdapter = null;
	private LoadImagesThread ebooksLoadThread = null;
	// 2013-3-16 - Amy - add AsyncTask to loading picture
	private ImageDownloader imageDownloader = null;
	Bitmap defaultCdCover;
	DialogFragment popupFragment;

	public static GridViewFragment newInstance(String tag) {
		GridViewFragment f = new GridViewFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putString("tag", tag);
		f.setArguments(args);

		return f;
	}

	public enum EditMode {
		VIEW, DELETE, EDIT, OPTION_SHOWN,
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.full_list_view, container, false);

		mGuestureDetector = new GestureDetector(mContext, mOnGuestureListener);
		mItemGuestureDetector = new GestureDetector(mContext,
				mOnGuestureListener);
		mListViewItemList = new ArrayList<OsListViewItem>();
		mImageList = new ArrayList<Bitmap>();
		mNameList = new ArrayList<String>();
		mType = "ebook";
		// mType = getIntent().getStringExtra(Global.STRING_TYPE);
		// mPath = getIntent().getStringExtra(Global.STRING_PATH);
		mContext = (FullListViewActivity) this.getActivity();
		initUIComponent(view);
		initHandler();
		
		//Insert and pull out the sdcard Listener
		registerSDCardListener();
		
		// 2013-3-15 - Amy - update shelves
		ebooksLoadThread = new LoadImagesThread();
		ebooksLoadThread.start();
		// 2013-3-16 - Amy - add AsyncTask to loading picture
		popupFragment = PopUpDialogFragment
				.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");
		imageDownloader = new ImageDownloader(mContext, CACHE_DIR);
		imageDownloader.setmImageDownloadListener(mDownloadListener);

		getEbookBlackList();
		/*
		 * if(!checkUpdateFile() && false) { notificationDownloadAfterUpdate();
		 * }
		 */

		/*if (ServiceController.getUser() == null) {
			getAccount(getActivity());
		}*/
		return view;
	}

	// 2013-6-11 - Zoya - SDCardListener class
	private final BroadcastReceiver sdcardListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals("android.intent.action.MEDIA_MOUNTED")) {//insert sdcard
				ebooksLoadThread = new LoadImagesThread();
				ebooksLoadThread.start();	
			} else if (action.equals("android.intent.action.MEDIA_REMOVED")//pull sdcard
					|| action.equals("android.intent.action.MEDIA_BAD_REMOVAL")) {
				ebooksLoadThread = new LoadImagesThread();
				ebooksLoadThread.start();	
			}else if (action.equals("com.meepstore.action.DOWNLOAD_COMPLETE")) {
			ebooksLoadThread = new LoadImagesThread();
			ebooksLoadThread.start();	
		}
		}
	};

	// 2013-6-11 - Zoya - registerSDCardListener
	private void registerSDCardListener() {
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addDataScheme("file");
		mContext.registerReceiver(sdcardListener, intentFilter);
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction("com.meepstore.action.DOWNLOAD_COMPLETE");
		mContext.registerReceiver(sdcardListener, intentFilter1);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(sdcardListener);
		super.onDestroy();
	}

	private void initUIComponent(View v) {
		mImgTick = BitmapFactory.decodeResource(getResources(),
				R.drawable.del_icon_s);
		// 2013-3-15 - Amy - update shelves
		/*
		 * mScrollView = (ScrollView)findViewById(R.id.scrollViewFullList);
		 * mScrollView.setOnTouchListener(scrollViewOnTouchListener);
		 * mScrollView.setLongClickable(true);
		 * 
		 * mLayout = (LinearLayout)findViewById(R.id.linearLayoutFullList);
		 */

		/*
		 * mOptionLayout = (RelativeLayout)findViewById(R.id.gridLayoutOption);
		 * 
		 * mBtnOptionFavourite =
		 * (ImageView)findViewById(R.id.imageViewBtnFavourite);
		 * mBtnOptionMessage =
		 * (ImageView)findViewById(R.id.imageViewBtnMessage); mBtnOptionPlay =
		 * (ImageView)findViewById(R.id.imageViewBtnPlay);
		 */
		defaultCdCover = BitmapFactory.decodeResource(getResources(),R.drawable.book_dummy);

		// 2013-3-15 - Amy - update shelves
		myEbooks = (GridView) v.findViewById(R.id.myEbook);
		myEbooks.setSelector(new ColorDrawable(Color.TRANSPARENT));
		ebookAdapter = new EbooksAdapter();
		myEbooks.setAdapter(ebookAdapter);

		mSnackIcon = (ImageView) v.findViewById(R.id.imageViewSnackView);

		mSnackIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				goToEbookView();
			}
		});

		mBtnConfirm = (Button) v.findViewById(R.id.buttonConfirmButton);
		mBtnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mEditMode == EditMode.DELETE) {
					for (int i = 0; i < mListViewItemList.size(); i++) {
						if (mListViewItemList.get(i).isChecked()) {
							// deleteFile(mListViewItemList.get(i).getPath());
							deleteItem(mListViewItemList.get(i));

						}

					}
				}
			}
		});

		mRenameLayout = (RelativeLayout) v
				.findViewById(R.id.relativeLayoutRename);
		mTxtRename = (EditText) v.findViewById(R.id.editTextRename);

		mTxtRename.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {

				}
				return false;
			}
		});

		mViewGroup = (ViewGroup) v.findViewById(R.id.ebookFullListViewLayout);
	}

	// 2013-3-19 -Amy- Use PopupWindow replace RingMenu
	PopupWindow popWindow = null;
	private View ringMenuShow = null;

	private void showRingMenu(float x, float y, OsListViewItem item) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepEbook-showRingMenu","DISABLE_RING_MENU");
			return;
		}
		
		int left = 0;
		//int right = 1;
		int buttonNum = 2;

		if (y < 0) {
			y = -20;
		}

		// 2013-4-16 -Amy- modified to change pop images
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		int xx = (int) x + 55;
		int yy = (int) y + 5;
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_rename_delete,	null);
		two_button_left = (FrameLayout) ringMenuShow.findViewById(R.id.two_button_left);
		contentLayout = (RelativeLayout) ringMenuShow.findViewById(R.id.contentLayoutleft);
		android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) contentLayout.getLayoutParams();
		layoutParams.leftMargin = xx;
		// Log.e("cdf",""+yy);
		if (yy > 250) {
			yy = 250;
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

		popWindow.showAtLocation(myEbooks, Gravity.START, (int) x + 70,
				(int) y - 120);
		ringMenuHandler(left, buttonNum, item);
	}

	private void hideRingMenu() {
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
		}
		ringMenuShow = null;
	}

	private void ringMenuHandler(int leftRight, int buttonNum,
			OsListViewItem item) {
		// if (leftRight == 0 && buttonNum == 2) {
		// left menu with two button
		leftMenuTwoButton(item);
		// }else if (leftRight == 1 && buttonNum == 2) {
		// right menu with two button
		// rightMenuTwoButton(item);
		// }
	}

	private void leftMenuTwoButton(final OsListViewItem item) {
		ring_delete = (ImageView) ringMenuShow.findViewById(R.id.btnDelete);
		ring_rename = (ImageView) ringMenuShow.findViewById(R.id.btnRename);

		// 2013-4-17 -Zoya- update ring menu layout.
		OnClickListener myLeftClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btnDelete:
					deleteItem(item);
					imageDownloader.clearCache();
					ebookAdapter.notifyDataSetChanged();
					hideRingMenu();
					break;
				case R.id.btnRename:
//					2013-6-13 -Amy- delete function: can not rename system file 
//					if((item.getPath()).contains("/data/home/ebook/unzip/")){
//						//Toast.makeText(getActivity(), "It is system file ,can not be renamed .", 0).show();
//						String renameErr = (String) getResources().getString(R.string.music_title_rename_error);
//						String msg_system_file = (String) getResources().getString(R.string.msg_system_file_ebook);
//						notification = new NotificationMessage(mContext, null, renameErr, msg_system_file);
//						hideRingMenu();
//						return;
//					}else{
						renameLayer(item);
						imageDownloader.clearCache();
						ebookAdapter.notifyDataSetChanged();
						hideRingMenu();
//					}
					break;
				}
			}
		};
		ring_delete.setOnClickListener(myLeftClickListener);
		ring_rename.setOnClickListener(myLeftClickListener);
	}

	public LayoutInflater getLayoutInflater() {
		return this.getActivity().getWindow().getLayoutInflater();
	}

	private void renameLayer(final OsListViewItem item) {
		LayoutInflater lf = getLayoutInflater();
		mPopupLayout = (RelativeLayout) lf.inflate(R.layout.layout_popup_text_input, null);
		ImageButton imageViewQuit = (ImageButton) mPopupLayout.findViewById(R.id.imageViewQuit);
		MyButton textViewOkBtn = (MyButton) mPopupLayout.findViewById(R.id.textViewOkBtn);
		MyEditText editTextRename = (MyEditText) mPopupLayout.findViewById(R.id.editTextRename);
		MyTextView textViewNotice = (MyTextView) mPopupLayout.findViewById(R.id.textViewNotice);
		ImageView imageViewEditUserBg = (ImageView) mPopupLayout.findViewById(R.id.imageViewEditUserBg);

		imageViewQuit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hidePopupMessageLayout();
			}
		});
		textViewOkBtn.setText(R.string.ebook_btn_ok);
		textViewOkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isExist = false;
				badWordChecker.setBlacklist(blackListWord);
				String renameErr = (String) getResources().getString(R.string.ebook_title_rename_error);
				
				//2013-03-22 - raymond - avoid rename system folder
				if(item.getName().equals("data") || item.getName().equals("extsd")){
					//mRenameText.setText("");
					String msg_system_file = (String) getResources().getString(R.string.msg_system_file_ebook);
					notification = new NotificationMessage(mContext, null, renameErr, msg_system_file,mViewGroup);
					return;
				}
				
				// 2013-03-20 - raymond - replace all special char
				String tmpName = mRenameText.getText().toString().trim();
				boolean shouldBlock = false;
				/*if (user != null) {
					PermissionManager pm = (PermissionManager) ServiceManager .getService(mContext, ServiceManager.PERMISSION_SERVICE);
					if (pm.containsBadword(user, tmpName) == true) {
						String msg_error = (String) getResources().getString( R.string.msg_error);
						notification = new NotificationMessage(mContext, null, renameErr, msg_error);
						return;
					}
				}*/
				//2013-6-20 - Zoya - solve #4292
				if (item.getName().equals(tmpName)) {
					// String renameErrMsgInvalidName = (String) getResources()	.getString(R.string.msg_rename_same);
					// notification = new NotificationMessage(mContext, null,renameErr, null);
					mPopupLayout.setVisibility(View.GONE);
					return;
				}
				
				//2013-6-26 -Amy- rename file's name containing space charector not in first or last
				/*if (tmpName.indexOf(" ") != -1){
					shouldBlock = true;
				}*/
				shouldBlock=MeepStorageCtrl.isRegexBadCharctors(tmpName);
				if (tmpName.equals("")) {
					String renameErrMsgInvalidName = (String) getResources() .getString(R.string.msg_null);
					notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
					return;
				} else if (shouldBlock) {
					String renameErrMsgInvalidName = (String) getResources() .getString(R.string.ebook_msg_blocked);
					notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
					return;
				/*}else if (item.getName().equals(tmpName)) {
					String renameErrMsgInvalidName = (String) getResources() .getString(R.string.msg_rename_same);
					notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName);
					return;*/
				}else {
					mRenameText.setText(tmpName);
				}
				
				if (isContainBadWord(getActivity(), tmpName)) {
					String msg_error = (String) getResources().getString(R.string.msg_error);
					notification = new NotificationMessage(mContext, null, renameErr, msg_error,mViewGroup);
					return;
				}

				// 3-21-2013 aaronli use tmpname
				if (mRenameText != null && !tmpName.equals("")) {
					for (int i = 0; i < mListViewItemList.size(); i++) {
						boolean a = mListViewItemList .get(i) .getName() .toLowerCase() .equals(mRenameText.getText().toString() .toLowerCase());
						if (mListViewItemList .get(i) .getName() .toLowerCase() .equals(mRenameText.getText().toString() .toLowerCase())) {
							isExist = true;
						}
					}
					if (isExist) {
						String renameErrMsgFormat = (String) getResources() .getString(R.string.msg_blocked);
						String renameErrMsg = String.format(renameErrMsgFormat, mRenameText.getText().toString());
						notification = new NotificationMessage(mContext, null, renameErr, renameErrMsg,mViewGroup);
					} else if ((tmpName.substring(0, 1)).equals(".")) {
						String renameErrMsgInvalidName = (String) getResources() .getString(R.string.ebook_msg_name_invalid);
						notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
					} 
					/*else if (!badWordChecker.isStringSafe(tmpName)) {
						String renameErrMsgInvalidName = (String) getResources() .getString(R.string.msg_error);
						notification = new NotificationMessage(mContext, null, renameErr, renameErrMsgInvalidName,mViewGroup);
					} */
					else {
						renameFile(tmpName.trim(), item);
						// 2013-3-18 -Amy- Rename after refres
						imageDownloader.clearCache();
						ebookAdapter.notifyDataSetChanged();
						// redrawListView();
					}
				}
				hidePopupMessageLayout();
			}
		});
		mRenameText = (EditText) editTextRename;

		ImageGetter imageGetter = new ImageGetter() {
			public Drawable getDrawable(String source) {
				int id = Integer.parseInt(source);
				Drawable d = getResources().getDrawable(id);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			}
		};

		// CharSequence cs = Html.fromHtml("<img src='" + R.drawable.del_icon_s+
		// "'/>",imageGetter,null);

		textViewNotice.setText(R.string.ebook_btn_ring_rename);
		// 2012-3-22 - Amy - During renaming, then touch the screen outside the
		// Rename blank, it will enter into corresponding moive or others
		imageViewEditUserBg.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		mPopupLayout.setVisibility(View.VISIBLE);
		mPopupLayout.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mViewGroup.addView(mPopupLayout);
		// 2012-3-22 - Amy - During renaming, then touch the screen outside the
		// Rename blank, it will enter into corresponding moive or others
		mPopupLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		// private void renameLayer(final OsListViewItem item){
		//
		// final Dialog dialog = new Dialog(mContext);
		// dialog.setContentView(R.layout.notification_rename);
		// dialog.setCancelable(true);
		// dialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(android.graphics.Color.TRANSPARENT));
		// //String rename = "Rename eBook from \"" + item.getName() +
		// "\" to: ";
		//
		// TextView textViewOkBtn = (TextView)
		// dialog.findViewById(R.id.textViewOkBtn);
		//
		// TextView textViewMessage = (TextView)
		// dialog.findViewById(R.id.textViewMessage);
		// //textViewMessage.setText(rename);
		//
		// ImageView imageViewQuit = (ImageView)
		// dialog.findViewById(R.id.imageViewQuit);
		// imageViewQuit.setImageResource(R.drawable.quit_room);
		//
		// final EditText editTextRename = (EditText)
		// dialog.findViewById(R.id.editTextRename);
		// editTextRename.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		//
		// imageViewQuit.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View pV) {
		// dialog.dismiss();
		// }
		// });
		//
		// textViewOkBtn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View pV) {
		// String renameFileName = editTextRename.getText().toString();
		// // if (!renameFileName.equals("")){
		// // renameEbook(renameFileName, item);
		// // }
		// boolean isExist = false;
		// if (!renameFileName.equals("")){
		// //renameEbook(renameFileName+".png", item);
		// for (int i = 0; i < mListViewItemList.size(); i++) {
		// //Log.d("All ebook name", "All ebook name: " +
		// mListViewItemList.get(i).getName());
		// //Log.d("All ebook name", "All ebook name: " +
		// mListViewItemList.get(i).getName().substring(0,
		// mListViewItemList.get(i).getName().lastIndexOf(".")));
		// if (mListViewItemList.get(i).getName().equals(renameFileName)){
		// isExist = true;
		// }
		// }
		//
		// if (isExist){
		// notification = new NotificationMessage(mContext, null,
		// "Rename error", "This destination already contains a file named \"" +
		// renameFileName + "\". \nPlease check it before renaming this file.");
		// }else if ((renameFileName.substring(0, 1)).equals(".")){
		// notification = new NotificationMessage(mContext, null,
		// "Rename error",
		// "The file name cannot start with \".\", please change the file name");
		// } else {
		// renameFile(renameFileName, item);
		// }
		// }
		// dialog.dismiss();
		// redrawListView();
		// }
		// });
		//
		// dialog.show();

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
		if (mTxtRename != null) {
			InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mTxtRename.getWindowToken(), 0);
		}
		mViewGroup.removeView(mPopupLayout);
		mPopupLayout = null;
	}

	private void renameFile(String renameFileName, OsListViewItem item) {
		// 2013-3-18 -Amy- Rename after refres
		// modified by aaronli at Jul9 2013 .- and return the new file path
		DataSourceManager.renameFile(renameFileName, item);
		//String newPath = EbookCtrl.renameEbook(item.getPath(), renameFileName);
		//item.setPath(newPath);
		//item.setName(renameFileName);
		// EbookCtrl.renameEbook(item.getPath(),
		// EncodingBase64.encode(renameFileName));

		// String oldName = EncodingBase64.decode(item.getName().substring(0,
		// item.getName().length()));
		// String pathTo = item.getPath().substring(0,
		// item.getPath().lastIndexOf("/")+1);
		//
		// Log.i("path from", "path from: " + item.getPath());
		// Log.i("path to", "path to: " + pathTo + renameFileName + ".epub");
		//
		//
		// File from = new File(item.getPath());
		// File to = new File(pathTo + renameFileName + ".epub");
		// from.renameTo(to);
		//
		// File fromIcon = new File(PATH_ICON_DIR + oldName + ".png");
		// File toIcon = new File(PATH_ICON_DIR + renameFileName + ".png");
		// fromIcon.renameTo(toIcon);
		//
		// File fromLargeIcon = new File(PATH_LARGE_ICON_DIR + oldName +
		// ".png");
		// File toLargeIcon = new File(PATH_LARGE_ICON_DIR + renameFileName +
		// ".png");
		// fromLargeIcon.renameTo(toLargeIcon);
		//
		// File fromDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + oldName +
		// ".png");
		// File toDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + renameFileName +
		// ".png");
		// fromDimIcon.renameTo(toDimIcon);
		//
		// File fromSmallIcon = new File(PATH_SMALL_ICON_DIR + oldName +
		// ".png");
		// File toSmallIcon = new File(PATH_SMALL_ICON_DIR + renameFileName +
		// ".png");
		// fromSmallIcon.renameTo(toSmallIcon);
	}

	private void initHandler() {
		mHandlerReadImg = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					int idx = msg.getData().getInt("imgIdx");
					popupFragment.dismiss();
					ebookAdapter.notifyDataSetChanged();
					// drawImage(idx);
					Log.w("thread", "thread full list view alive");
					break;
				case 2: // draw shelf only
					// drawShelfOnly();
					break;
				default:
					break;
				}
				// 2013-3-15 - Amy - update shelves
				// mLayout.invalidate();
				super.handleMessage(msg);
			}
		};

		mThread = new Thread(runReadImage);
		mThread.start();
	}

	private Runnable runReadImage = new Runnable() {

		@Override
		public void run() {
			// loadAndDrawImage();
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
			hideRingMenu();
			if (mEditMode == EditMode.VIEW) {
				// if(mSelectedView!=null)
				// {
				// String name = (String) mSelectedView.getTag();
				// OsListViewItem item = findListViewItemByName(name);
				// String[] paths = getAllEbookPaths();
				// if (mEditMode == EditMode.VIEW) {
				// if (mSelectedView.getTag() != null) {
				// if (item != null) {
				// goToEbook(paths, item.getPath());
				// }
				// }
				// }
				// }
			} else {
				// hideOption();
			}
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
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
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	OnTouchListener scrollViewOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			hideRingMenu();
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
	// String[] paths = getAllEbookPaths();
	// if (mEditMode == EditMode.VIEW) {
	// if (v.getTag() != null) {
	// if (item != null) {
	// goToEbook(paths, item.getPath());
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
			return false;
		}
	};

	OnClickListener btnItemClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i("mEditMode", "mEditMode:" + mEditMode.toString());

			if (mEditMode == EditMode.VIEW) {
				// 2013-3-15 - Amy - update shelves
				// String name = (String) v.getTag();
				//String name = (String) v.getTag(R.id.TAG);
				//OsListViewItem item = findListViewItemByName(name);
				OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
				String itemName = "";
				String path = "";
				if (item != null) {
					path = item.getPath();
				}

				String extension = getExtension(path);

				if (extension.equals(Global.FILE_TYPE_EPUB)) {
					itemName = item.getName() + Global.FILE_TYPE_EPUB;
				} else if (extension.equals(Global.FILE_TYPE_PDF)) {
					itemName = item.getName() + Global.FILE_TYPE_PDF;
				} else {
					// delete by aaronli at Mar21 2013,never change the path
					// string
					// path = "/data/home/ebook/unzip/" + item.getName();
				}

				if (mEditMode == EditMode.VIEW && ringMenuShow == null) {
					if (item != null) {
						goToEbook(path, itemName);
					}
				}
				hideRingMenu();
			}
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

			//String name = (String) v.getTag(R.id.TAG);
			//OsListViewItem item = findListViewItemByName(name);
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
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

	// private void traverseDir(File dir, List<File> theList) {
	// Log.i("ebook-traversal-(Ebook)", "traverseDir LOADED");
	// File[] files = dir.listFiles();
	// if (files != null) {
	// for (int i=0; i<files.length; i++) {
	// if (files[i].isDirectory()) {
	// traverseDir(files[i], theList);
	// } else {
	// if (!files[i].isHidden() &&
	// files[i].getName().toLowerCase().contains(Global.FILE_TYPE_EPUB)) {
	// theList.add(files[i]);
	// Log.i("ebook-traversal-(Ebook)", files[i].getAbsolutePath());
	// }
	// }
	// }
	// }
	// }

	private String getFileNameOnly(String fileName) {
		// 2013-03-20 - raymond - check index of out bound error
		int extIndex = fileName.lastIndexOf('.');
		if (extIndex > 0) {
			return fileName.substring(0, extIndex);
		} else {
			return fileName;
		}
	}

	// 2013-4-24 - Zoya - update shelves use GridView
	private static class ViewHolder {
		ImageView pic;
		TextView pic_title;
	}

	private class EbooksAdapter extends BaseAdapter {
		private LayoutInflater inflater = LayoutInflater.from(mContext);

		@Override
		public int getCount() {
			// Log.e("cdf","mListViewItemList.size() == "+mListViewItemList.size());
			if (mListViewItemList == null) {
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
			} else {
				return mListViewItemList.get(position).hashCode();
			}

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = inflater.inflate(R.layout.gridview_item, null);

				holder.pic = (ImageView) convertView
						.findViewById(R.id.ebookpic);
				holder.pic_title = (TextView) convertView
						.findViewById(R.id.ebookpic_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final OsListViewItem item = mListViewItemList.get(position);
			holder.pic_title.setText(item.getName());
			// holder.pic.setImageBitmap(item.getImage());
			holder.pic.setTag(R.id.TAG, item);
			// Log.e("cdf","item.getName() == 2  "+item.getName());
			holder.pic.setOnClickListener(btnItemClickListener);
			holder.pic.setOnLongClickListener(btnItemLongClickListener);
			// 2013-3-16 - Amy - add AsyncTask to loading picture
			// String path = PATH_SMALL_ICON_DIR +
			// getSimpleNameOfFilePath(item.getPath()) + ".png";
			// String bigpath = PATH_ICON_DIR +
			// getSimpleNameOfFilePath(item.getPath()) + ".png";
			imageDownloader.download(item.getPath(), holder.pic,
					defaultCdCover, null);
			// imageDownloader.downloadEbook(path,bigpath, SHOW_WIDTH,
			// SHOW_HEIGHT, holder.pic,defaultCdCover);
			convertView.setVisibility(View.VISIBLE);

			return convertView;
		}
	}

	// 2013-3-15 - Amy - update shelves
	class LoadImagesThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mListViewItemList != null) {
				mListViewItemList.clear();
			}
			Message message = new Message();
			// Log.e("cdf","cdf-----------");
			loadAndDrawImage();
			message.what = 1;
			mHandlerReadImg.sendMessage(message);
			super.run();
		}
	}

	private void loadAndDrawImage() {
		// draw default 3 shell
		// mHandlerReadImg.sendEmptyMessage(2);
		// get ebook file list
		/*
		 * List<File> fileList = EbookCtrl.getEbookList();
		 * 
		 * int count = 0; //Log.e("cdf","fileList.size() --> " +
		 * fileList.size()); for (int i = 0; i < fileList.size(); i++) { File
		 * thisFile = fileList.get(i); String filename = thisFile.getName();
		 * 
		 * String encodedName = null; if (thisFile.isDirectory()) { // decode
		 * the filename shown in director // modified by aaronli Mar21 2013
		 * encodedName = EncodingBase64.decode(filename); } else { String
		 * fileNameOnly = getFileNameOnly(filename); //2013-3-18 -Amy- Rename
		 * after refresh // Mar 19 2013 aaronli show the ecodename of books
		 * which in data dir if
		 * (thisFile.getAbsolutePath().contains(PATH_EBOOK_DATA_DIR)) {
		 * 
		 * encodedName = EncodingBase64.decode(fileNameOnly);
		 * 
		 * //2013-03-22 - raymond - remove downloading file fileList.remove(i);
		 * continue; } else { encodedName = fileNameOnly; } }
		 * 
		 * //get icon bitmap Bitmap bmap = null;
		 * 
		 * // try to load small icon first bmap =
		 * BitmapFactory.decodeFile(PATH_SMALL_ICON_DIR + encodedName + ".png");
		 * if (bmap == null) { Log.d("EbookListView",
		 * "small image null, try to generate from icon image"); // create small
		 * icon from original icon bmap = BitmapFactory.decodeFile(PATH_ICON_DIR
		 * + encodedName + ".png"); if (bmap != null) { bmap =
		 * ThumbnailUtils.extractThumbnail(bmap, 90, 107); } else {
		 * Log.w("EbookListView", "icon image null, try to get default image");
		 * } }
		 * 
		 * if (bmap == null) { // Load default ebook image bmap =
		 * BitmapFactory.decodeResource(getResources(), R.drawable.book_dummy);
		 * bmap = ThumbnailUtils.extractThumbnail(bmap, 90, 107); }
		 * 
		 * //if (bmap != null) { OsListViewItem item = new OsListViewItem();
		 * //item.setImage(bmap); item.setName(encodedName);
		 * item.setPath(thisFile.getAbsolutePath());
		 * mListViewItemList.add(item); //2013-3-15 - Amy - update shelves
		 * Message msg = new Message(); Bundle data = new Bundle();
		 * data.putInt("imgIdx", count); msg.setData(data); msg.what = 1;
		 * mHandlerReadImg.sendMessage(msg); //Log.e("cdf","encodedName --> " +
		 * encodedName); //Log.e("cdf","thisFile.getAbsolutePath() --> " +
		 * thisFile.getAbsolutePath()); count++; //} else { //
		 * Log.e("EbookListView", "default bmap is null, cannot show the icon");
		 * //} }
		 */

		//List<File> files = DataSourceManager.getEbookList();
		//DataSourceManager.getDataSource(files, mListViewItemList);
		// Cache method
		mListViewItemList = DataSourceManager.getAllItems(false);

	}

	private void redrawListView() {
		// 2013-3-15 - Amy - update shelves
		// mLayout.removeAllViews();
		mListViewItemList.removeAll(mListViewItemList);
		// loadAndDrawImage();
	}

	/*
	 * private void drawShelfOnly() { LinearLayout ll = null;
	 * 
	 * ll = new LinearLayout(this);
	 * ll.setBackgroundResource(R.drawable.movie_shelf); ll.setPadding(0, 0, 0,
	 * 15); ll.setOrientation(LinearLayout.HORIZONTAL); mLayout.addView(ll);
	 * 
	 * ll = new LinearLayout(this);
	 * ll.setBackgroundResource(R.drawable.movie_shelf); ll.setPadding(0, 0, 0,
	 * 15); ll.setOrientation(LinearLayout.HORIZONTAL); mLayout.addView(ll);
	 * 
	 * ll = new LinearLayout(this);
	 * ll.setBackgroundResource(R.drawable.movie_shelf); ll.setPadding(0, 0, 0,
	 * 15); ll.setOrientation(LinearLayout.HORIZONTAL); mLayout.addView(ll); }
	 */

	/*
	 * private void drawImage(int index) {
	 * 
	 * OsListViewItem item = mListViewItemList.get(index); boolean isNewRow =
	 * false; LinearLayout ll = null;
	 * 
	 * if (index < 15) { ll = (LinearLayout) mLayout.getChildAt((int) ((index) /
	 * 5)); } else { if (mLayout.getChildCount() > 0) { ll = (LinearLayout)
	 * mLayout.getChildAt(mLayout.getChildCount() - 1); }
	 * 
	 * if (mLayout.getChildCount() == 0 || ll.getChildCount() >= 5) {
	 * 
	 * ll = new LinearLayout(this); ll.setLayoutParams(new
	 * LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
	 * 
	 * ll.setBackgroundResource(R.drawable.movie_shelf); ll.setPadding(0, 30, 0,
	 * 15); ll.setOrientation(LinearLayout.HORIZONTAL); isNewRow = true; } }
	 * 
	 * // image ImageView image = new ImageView(this);
	 * RelativeLayout.LayoutParams lpImg = new RelativeLayout.LayoutParams(120,
	 * 120); lpImg.setMargins(-10, 0, 0, 0); image.setLayoutParams(lpImg);
	 * image.setImageBitmap(item.getImage()); image.setTag(item.getName());
	 * image.setOnClickListener(btnItemClickListener);
	 * image.setOnLongClickListener(btnItemLongClickListener);
	 * 
	 * // name TextView txt = new TextView(this); //String tempName =
	 * item.getName().substring(0, item.getName().length() - 4); String tempName
	 * = EncodingBase64.decode(item.getName()); if (tempName.length() > 15) {
	 * tempName = tempName.substring(0, 13) + ".."; } txt.setText(tempName);
	 * 
	 * txt.setBackgroundResource(R.drawable.text_bar); //
	 * txt.setText(item.getName().substring(0, item.getName().length())); //
	 * txt.setBackgroundResource(R.drawable.text_bar);
	 * //txt.setOnTouchListener(btnItemOnTouchListener);
	 * 
	 * RelativeLayout.LayoutParams lp = new
	 * RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
	 * RelativeLayout.LayoutParams.WRAP_CONTENT); lp.setMargins(0, 125 , 0, 0);
	 * txt.setWidth(103); txt.setLayoutParams(lp);
	 * txt.setGravity(Gravity.CENTER);
	 * 
	 * // //tick icon // ImageView imageTick = new ImageView(this); //
	 * RelativeLayout.LayoutParams lp2 = new
	 * RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
	 * RelativeLayout.LayoutParams.WRAP_CONTENT); // lp2.setMargins(42, 0 , 0,
	 * 0); // imageTick.setLayoutParams(lp2); //
	 * imageTick.setImageBitmap(mImgTick); // if (mEditMode == EditMode.DELETE)
	 * { // imageTick.setVisibility(View.VISIBLE); // } else { //
	 * imageTick.setVisibility(View.GONE); // } //
	 * imageTick.setTag(item.getName()); // imageTick.setOnClickListener(new
	 * View.OnClickListener() { // // @Override // public void onClick(View v) {
	 * // String name = (String) v.getTag(); // OsListViewItem item =
	 * findListViewItemByName(name); // deleteItem(item); // } // });
	 * 
	 * 
	 * RelativeLayout rl = new RelativeLayout(this);
	 * //rl.setOnTouchListener(btnItemOnTouchListener);
	 * rl.setTag(item.getName()); if (ll.getChildCount() == 0) {
	 * rl.setPadding(30, 10, 12, 0); } else { rl.setPadding(12, 10, 12, 0); }
	 * //rl.setOnClickListener(btnItemOnclickListener);
	 * 
	 * rl.addView(image); rl.addView(txt); //rl.addView(imageTick);
	 * 
	 * ll.addView(rl);
	 * 
	 * if (isNewRow) { try { mLayout.addView(ll); } catch (Exception ex) {
	 * ex.printStackTrace(); } } }
	 */

	private void goToEbook(String path, String name) {
		// // Old Ebook reader (1.0)
		// Intent intent = new Intent(this, FullListViewActivity.class);
		// intent.setComponent(ComponentName.unflattenFromString("com.oregonscientific.meep.Reader.main/.BooksReaderActivity"));
		// intent.addCategory("android.intent.category.LAUNCHER");
		// intent.putExtra(Global.STRING_PATH, path);
		// intent.putExtra(Global.STRING_NAME, name);

		// New Ebook reader (2.0)
		Intent intent = new Intent(mContext, FullListViewActivity.class);
		intent.setComponent(new ComponentName(
				"com.oregonscientific.meep.epubreader",
				"com.oregonscientific.meep.epubreader.BookRendering"));
		intent.addCategory("android.intent.category.LAUNCHER");
		intent.putExtra("path", path);
		intent.putExtra(Global.STRING_NAME, name);
		try {
			startActivity(intent);
			Log.e("cdf",""+path);
			MeepLogger meepLogger = new MeepLogger(mContext);
			meepLogger.p("was reading eBook: " + name);
		} catch (Exception ex) {
			Log.e("meepbook", "go to ebookReader:" + ex.toString());
		}
	}

	// 2013-3-15 - Amy - update shelves
	/*
	 * private void showOption(float x, float y) { mOptionLayout.setX(x);
	 * mOptionLayout.setY(y); mOptionLayout.setVisibility(View.VISIBLE); }
	 */

	/*
	 * private void hideOption() { mOptionLayout.setVisibility(View.GONE);
	 * setEditMode(EditMode.VIEW); }
	 */

	/*
	 * private void setEditMode(EditMode editMode) { mEditMode = editMode;
	 * if(editMode == EditMode.DELETE) { showAllDeleteIcons();
	 * mBtnConfirm.setVisibility(View.VISIBLE); } else { hideAllDeleteIcons();
	 * mBtnConfirm.setVisibility(View.GONE); } }
	 */

	/*
	 * private void showAllDeleteIcons() { for(int i= 0;
	 * i<mLayout.getChildCount(); i++) { LinearLayout rowLayout =
	 * (LinearLayout)mLayout.getChildAt(i); for(int j = 0; j<
	 * rowLayout.getChildCount();j++) { RelativeLayout rl =
	 * (RelativeLayout)rowLayout.getChildAt(j); if(rl!= null) { ImageView img =
	 * (ImageView)rl.getChildAt(2); img.setVisibility(View.VISIBLE); } } } }
	 */

	/*
	 * private void hideAllDeleteIcons() { for(int i= 0;
	 * i<mLayout.getChildCount(); i++) { RelativeLayout rl =
	 * (RelativeLayout)mOptionLayout.getChildAt(i); if(rl!= null) { ImageView
	 * img = (ImageView)rl.getChildAt(2); img.setVisibility(View.GONE); } } }
	 */

	private void deleteItem(OsListViewItem item) {
		// delete item from database

		// EbookCtrl.deleteEbook(item.getPath());
		// Log.d("delete path", "delete path0:" + PATH_SDCARD + item.getName());
		// Log.d("delete path", "delete path1:" + "/mnt/sdcard/home/ebook/temp/"
		// + item.getName());
		// Log.d("delete path", "delete path2:" + item.getPath());
		// Log.d("delete path", "delete path3:" + PATH_ICON_DIR +
		// item.getName());
		// Log.d("delete path", "delete path4:" + PATH_LARGE_ICON_DIR +
		// item.getName());
		// Log.d("delete path", "delete path5:" + PATH_LARGE_DIM_ICON_DIR +
		// item.getName());
		// Log.d("delete path", "delete path6:" + PATH_SMALL_ICON_DIR +
		// item.getName());
		//
		// deleteDirectory(new File(PATH_SDCARD + item.getName() + ".epub"));
		// deleteDirectory(new File("/mnt/sdcard/home/ebook/temp/" +
		// item.getName() + ".epub"));
		// deleteItemFile(item.getPath());
		// deleteItemFile(PATH_ICON_DIR + item.getName() + ".png");
		// deleteItemFile(PATH_LARGE_ICON_DIR + item.getName() + ".png");
		// deleteItemFile(PATH_LARGE_DIM_ICON_DIR + item.getName() + ".png");
		// deleteItemFile(PATH_SMALL_ICON_DIR + item.getName() + ".png");
		//
		// mListViewItemList.remove(item);
		// ebookAdapter.notifyDataSetChanged();
		// redrawListView();

		// 2013-5-14 -Zoya- add delete dialog
		String delErr = (String) getResources().getString(R.string.delete);
		String delMsg = (String) getResources().getString(R.string.delete_sure);
		final OsListViewItem mitem = item;
		// notification = new NotificationMessage(mContext, null, delErr,
		// delMsg);

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
				// delete item from database
				EbookCtrl.deleteEbook(mitem.getPath());

				mListViewItemList.remove(mitem);
				//ebookAdapter.notifyDataSetChanged();
				// redrawListView();
				popupLayout.setVisibility(View.GONE);
				ebookAdapter.notifyDataSetChanged();
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
		popupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mViewGroup.addView(popupLayout);

		popupLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

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

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	private void goToEbookView() {
		/*
		 * Intent myIntent = new Intent(this, MeepEbookActivity.class);
		 * myIntent.putExtra(Global.STRING_TYPE, "ebook");
		 * 
		 * try { startActivityForResult(myIntent, 0); } catch (Exception ex) {
		 * ex.printStackTrace(); }
		 */
		mContext.showDetails(FullListViewActivity.STATE_FRAG_SNACK);
	}

	private void getEbookBlackList() {
		String sql = "select * from " + TableBlacklist.S_TABLE_NAME + " where "
				+
				// TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Ebook +
				// "'" ;
				TableBlacklist.S_LIST_TYPE + " = 'browser'";
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY,
				MeepAppMessage.OPCODE_DATABASE_BLACK_LIST, sql,
				Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		mContext.sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam,
				AppType.MeepMessage));
	}

	private void notificationDownloadAfterUpdate() {
		/*
		 * final Dialog dialog = new Dialog(this);
		 * dialog.setContentView(R.layout.custom_dialog);
		 * dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT);
		 * dialog.getWindow().setBackgroundDrawable(new
		 * ColorDrawable(android.graphics.Color.TRANSPARENT));
		 * 
		 * Button ok = (Button) dialog.findViewById(R.id.btnOk); Button quit =
		 * (Button) dialog.findViewById(R.id.btnQuit);
		 * 
		 * // if button is clicked, directly link to MEEP! Store
		 * ok.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { //Log.d("test",
		 * "send open meep store"); MeepAppMessage mam = new
		 * MeepAppMessage(Category.SYSTEM,
		 * MeepAppMessage.OPCODE_OPEN_MEEP_STORE,"", Global.INTENT_MSG_PREFIX +
		 * Global.AppType.Ebook); String jsonStr = new Gson().toJson(mam);
		 * Intent i = new Intent(); i.setAction(Global.INTENT_MSG_PREFIX +
		 * Global.AppType.Store); i.putExtra(Global.STRING_MESSAGE, jsonStr);
		 * sendBroadcast(i); } }); // if button is clicked, close this dialog
		 * quit.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { dialog.dismiss(); } });
		 * 
		 * dialog.show();
		 */
	}

	public boolean checkUpdateFile() {
		File file = new File(PATH_FOR_UPDATE_CHECKING);
		if (file.exists()) {
			return true;
		}
		try {
			file.createNewFile();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
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
			view.setImageBitmap(defaultCdCover);

		}

		/*
		 * @Override public Bitmap loadImageFromUrl(String url) { // TODO
		 * Auto-generated method stub return null; }
		 */

		@Override
		public Bitmap loadImageFromUrl(String url) {
			return getBitmapFromDisk(url);
		}
	};

	private Bitmap getBitmapFromDisk(String path) {
		// String pngName = path.substring(0, path.length() - 4) +
		// Global.FILE_TYPE_PNG;
		Bitmap bmap = null;
		try {
			// try to load small icon first
			/*
			 * bmap = BitmapFactory.decodeFile(path); if (bmap == null) {
			 * //Log.d("EbookListView",
			 * "small image null, try to generate from icon image"); // create
			 * small icon from original icon bmap =
			 * BitmapFactory.decodeFile(path); if (bmap != null) { bmap =
			 * ThumbnailUtils.extractThumbnail(bmap, 90, 107); } else {
			 * Log.w("EbookListView",
			 * "icon image null, try to get default image"); } }
			 */

			bmap = DataSourceManager.loadListImage(defaultCdCover,
					DataSourceManager.getEbookImgName(path));
			//bmap = BitmapFactory.decodeFile(PATHLARGEICONDIR + imageName + Global.FILE_TYPE_PNG);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bmap;
	}

	// TODO: should call MeepLib
	private String getExtension(String filename) {
		// dot is included
		int dotIndex = filename.lastIndexOf(".");

		if (dotIndex > 0) {
			return filename.substring(dotIndex, filename.length()).toLowerCase();
		} else {
			return "";
		}
	}

	public static String getSimpleNameOfFilePath(String path) {
		Log.d("FullListViewActivity", "getSimpleNameOfFilePath path " + path);
		int lastSeparatorIndex = path.lastIndexOf(File.separator);
		int lastDotIndex = path.lastIndexOf('.');
		lastDotIndex = lastDotIndex > lastSeparatorIndex ? lastDotIndex : path
				.length();
		Log.d("FullListViewActivity",
				"getSimpleNameOfFilePath lastSeparatorIndex "
						+ lastSeparatorIndex);
		Log.d("FullListViewActivity", "getSimpleNameOfFilePath lastDotIndex "
				+ lastDotIndex);

		return path.substring(lastSeparatorIndex + 1, lastDotIndex);
	}

}
