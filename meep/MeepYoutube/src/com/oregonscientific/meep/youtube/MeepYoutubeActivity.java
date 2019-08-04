package com.oregonscientific.meep.youtube;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;
import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.customdialog.CommonPopup;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickCloseButtonListener;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;
import com.oregonscientific.meep.database.table.MeepDbCommunicationCtrl;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.database.table.TablePermission;
import com.oregonscientific.meep.database.table.TableRecommendation;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.message.common.MeepAppMessageCtrl;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.youtube.ImageThreadLoader.ImageLoadedListener;
import com.oregonscientific.meep.youtube.MessageReceiver.OnMessageReceivedListener;
import com.oregonscientific.meep.youtube.YoutubeGData.Entry;
import com.oregonscientific.meep.youtube.ui.fragment.MainViewFragment;
import com.oregonscientific.meep.youtube.ui.fragment.MenuRecentlyFragment;
import com.oregonscientific.meep.youtube.ui.fragment.RightMenuFragment;
import com.oregonscientific.meep.youtube.ui.fragment.RightMenuFragment.MainViewCallback;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
//gesture
//Sliding
//Sliding


@SuppressLint("NewApi")
public class MeepYoutubeActivity extends SlidingFragmentActivity implements MainViewCallback {
	SimpleAdapter mSchedule = null;
	ListView mRightListYoutube;
	ListView mLeftListYoutube;
	ArrayList<HashMap<String, Object>> mylist;
//	ArrayList<HashMap<String, Object>> myRecommendationlist;
	ArrayList<HashMap<String, Object>> myRecommendationOslist;
	ArrayList<HashMap<String, Object>> myRecommendationParentlist;
	ArrayList<HashMap<String, Object>> relatedList;
	ImageView imageView;
	ImageView imagePlay;
	//TextView centralTitle;
	//TextView textViewCount;
	ImageThreadLoader imageLoader = new ImageThreadLoader();
	String mSearchText = null;
	//EditText searchTextView;
	//ImageView searchYoutubeButton;
	//ImageView youtubeBigImagePlay;
	//ImageView youtubeBigImageView;
	
	/*ImageView youtubeRightLayoutClose= null;
	ImageView youtubeRightBtnClose = null;
	ImageView youtubeRightLayoutOpen= null;
	ImageView youtubeRightBtnOpen= null;
	
	ImageView youtubeLeftLayoutClose= null;
	ImageView youtubeLeftBtnClose = null;
	ImageView youtubeLeftLayoutOpen= null;
	ImageView youtubeLeftBtnOpen= null;*/
	
	/*Animation fadeOutRightCloseAnimation = null;
	Animation fadeInRightOpenAnimation = null;
	
	Animation fadeOutLeftCloseAnimation = null;
	Animation fadeInLeftOpenAnimation = null;*/
	
	MyAdapter mLeftAdapter = null;
	MyAdapter mRightAdapter = null;
	
	//-----------------------
	//RelativeLayout youtubeLayoutMain;
	//Central Layout
	//FlowLayout flowLayoutCentral = null;
	//ScrollView scrollViewCentral;
	//LinearLayout centralLayout;
	//LinearLayout centralLayoutOne;
	//LinearLayout centralLayoutTwo;
	//LinearLayout centralLayoutThree;
	//TextView searchCount;
	//------------------------
	DecimalFormat formatter = new DecimalFormat("#,###,###");


	String videoId = "";
	//LinearLayout rightLayout;
	//LinearLayout leftLayout;
	
	String[] byPassWord;
	String[] blackListWord;
	//left recommend
	String[] mRecommendationList;
	//right recommend
	String[] mRecommendationParentList;
	int permissionList;
	String recentlyview = "";
	
	NotificationMessage notification;
	Context mContext;

	// Popup
	//RelativeLayout layoutPopUp;
	//ImageView PopUp;
	//TextView popUpText, webPopupTitle, btnOk;
	// Button btnPopUp1;
	//ImageView btnCancel, webPopupTitleBg;
	
	//broadcast message
	MessageReceiver mMsgReceiver = null;
	private MeepDbCommunicationCtrl mMeepDbCommunicationCtrl = null;
	
	//left menu
	ArrayDeque<String> mRecommendQ = null;
	//right menu
	ArrayDeque<String> mRecommendParentQ = null;
	
	//thread
	Thread mThread;
	
	//time schedule
	private Timer mTimerSeconds;
    private int mIntIdleSeconds;
    
    private int mLastY=0;

	private static final String TAG = "WebBrowserActivity";
	FrameLayout container;
	private MainViewFragment mainViewFragment = null;
	public static SlidingMenu menu;
	public MenuRecentlyFragment firstFrag;
	private RightMenuFragment rightMenuFragment = null;

	@Override
	public void onCreate(Bundle pSavedInstanceState) {
	  	super.onCreate(pSavedInstanceState);

		setContentView(R.layout.main_menu);
		container = (FrameLayout) findViewById(R.id.fragment_container);


		if (container != null) {
			if (pSavedInstanceState != null)
				return;
			firstFrag = MenuRecentlyFragment.newInstance();
			firstFrag.setContainer(this);
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFrag).commit();
		}
		initRightMenu();

		//initMessageReceiver();
		mGson = new Gson();
		mContext = this;
		badWordChecker = new BadWordChecker();

		initRecentlyView();

		
		if (!YouTubeUtility.isNetworkAvailable(this)) {
			alertAccessDenied(R.string.nonetwork);
		} else {
			retriveAccountInformation();
		}
		
		
	}
	
	

	@Override
	protected void onStart() {
//		onlineCheckAction();
//		
//		getYoutubeWhiteList();
//		getYoutubeBlackList();
//		//getYoutubeRecommendationList();
//		//-------recommendlist-------
//		getYoutubeOsRecommendationList();
//		getYoutubeParentRecommendationList();
//		//---------------------------
//		getPermissionList();
		super.onStart();
	}


	@Override
	protected void onResume() {
		super.onResume();
		initRecentlyView();
	}
	
	private void initThread()
	{
		mThread = new Thread(run);
		mThread.start();
	}
	
	private void initMessageReceiver() {
		mMsgReceiver = new MessageReceiver(this);
		mMsgReceiver.setOnMessageReceivedListener(new OnMessageReceivedListener() {

			@Override
			public void onQueryYouTubeBlackListReceived(List<TableBlacklist> youtubeBlacklist) {
				if (youtubeBlacklist.size() != 0) {
					TableBlacklist blacklist = youtubeBlacklist.get(0);

					if (blacklist.getListType().toString().equals("bypass")) {
						byPassWord = blacklist.getListEntry();
						if (byPassWord != null) {
							for (int i = 0; i < byPassWord.length; i++) {

								Log.d("youtube", "youtube bypass received: " + byPassWord[i]);
							}
						}
					} else {
						blackListWord = blacklist.getListEntry();
					}
				}
			}

			@Override
			public void onQueryYouTubeRecommendationListReceived(List<TableRecommendation> youtubeRecommendationlist) {
			if(true){return;}
				if (youtubeRecommendationlist.size() != 0) {
					TableRecommendation recommendationlist = youtubeRecommendationlist.get(0);

					if (recommendationlist.getListType().toString().equals("os-youtube") ) {
						//left list
						mRecommendationList = recommendationlist.getListEntry();
						for (int i = 0; i < mRecommendationList.length; i++) {
							Log.d("youtube", "youtube recommendationList received: " + mRecommendationList[i]);
							mRecommendQ.add(mRecommendationList[i]);
						}
						//leftListViewSet(mRecommendationList);
					}
					else if(recommendationlist.getListType().toString().equals("youtube"))
					{
						//right list
						mRecommendationParentList = recommendationlist.getListEntry();
						for (int i = 0; i < mRecommendationParentList.length; i++) {
							Log.d("youtube", "youtube parent recommendationList received: " + mRecommendationParentList[i]);
							mRecommendParentQ.add(mRecommendationParentList[i]);
						}
					}
				}

			}

			@Override
			public void onQueryYouTubePermissionListReceived(List<TablePermission> youtubePermissionlist) {
				if (youtubePermissionlist.size() != 0) {
					TablePermission permissionlist = youtubePermissionlist.get(0);

					/*if (permissionlist.getAppName().toString().equals("SecurityLevel")) {
						permissionList = permissionlist.getCanAccess();
						Log.d("youtube", "youtube permissionList received: " + permissionList);
						if (permissionList == 0) {
							searchTextView.setVisibility(View.GONE);
							searchYoutubeButton.setVisibility(View.GONE);

						} else {
							searchTextView.setVisibility(View.VISIBLE);
							searchYoutubeButton.setVisibility(View.VISIBLE);
						}
					}*/
				}
			}

			@Override
			public void onUpdateYouTubeRecommendationListReceived(String type,String[] array) {
				if (array!=null) {
					if (type.equals(TableRecommendation.S_YOUTUBE_TYPE_OS) ) {
						//left list
						for (int i = 0; i < array.length; i++) {
							Log.d("youtube", "youtube recommendationList UI left update: " + array[i]);
							mRecommendQ.add(array[i]);
						}
						//leftListViewSet(mRecommendationList);
					}
					else if(type.equals(TableRecommendation.S_YOUTUBE_TYPE_PARENT))
					{
						//right list
						for (int i = 0; i < array.length; i++) {
							Log.d("youtube", "youtube parent recommendationList UI right update: " + array[i]);
							mRecommendParentQ.add(array[i]);
						}
					}
				}
			}

			@Override
			public void onDeleteYouTubeRecommendationListReceived(String type,String[] array) {
				HashMap<String,Object> map = null;
				if (array!=null) {
					if (type.equals(TableRecommendation.S_YOUTUBE_TYPE_OS) ) {
						//left list
						for (int i = 0; i < array.length; i++) {
							Log.d("youtube", "youtube recommendationList UI left delete: " + array[i]);
							map = generateOsListItemEntry(array[i]);
							if(myRecommendationOslist.contains(map))
								myRecommendationOslist.remove(map); 
							mLeftAdapter.notifyDataSetChanged();
						}
						//leftListViewSet(mRecommendationList);
					}
					else if(type.equals(TableRecommendation.S_YOUTUBE_TYPE_PARENT))
					{
						//right list
						for (int i = 0; i < array.length; i++) {
							Log.d("youtube", "youtube parent recommendationList UI right delete: " + array[i]);
							map = generateListItemEntry(array[i]);
							if(myRecommendationParentlist.contains(map))
								myRecommendationParentlist.remove(map); 
							mRightAdapter.notifyDataSetChanged();
						}
					}
				}
				
			}
		});
	}

	private youtubeUrl storeUrl = null;
	private int isNewSearch = 1;
	//Update interface after search
	Runnable run = new Runnable() {
		@Override
		public void run() {
			while (true) {
				if (mSearchText != null && mSearchText != "") {
					youtubeUrl url = new youtubeUrl("https://gdata.youtube.com/feeds/api/videos/-/" + mSearchText.replaceAll(" ", "-"));
					// url.author = "";
					 url.maxResults = 30;
					 url.startIndex = 1;
					 storeUrl = url;
					convertJSON(url.toString());
					Log.i("URL", "youtube url:" + url.toString());
					mSearchText = null;
				} 
				
				if(mRecommendQ.size()>0)
				{
					String id = mRecommendQ.poll();
					loadLeftYoutubeEntry(id);
				}
				if(mRecommendParentQ.size()>0)
				{
					String id = mRecommendParentQ.poll();
					loadRightYoutubeEntry(id);
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	Handler mHandlerReadImg = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					loadAndDrawImage(msg);
					break;
				case 2:
//					String notice = getResources().getString(R.string.youtube_info_results_blocked);
//					String title = getResources().getString(R.string.youtube_title_search_results);
//					notification = new NotificationMessage(mContext, null, title, notice);
					break;
				case 3:
					loadAndDrawImage(msg);
					break;
				default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	
	private void initUiComponent()
	{/*
		layoutPopUp = (RelativeLayout) findViewById(R.id.layoutPopUp);
		PopUp = (ImageView) findViewById(R.id.PopUp);
		//layoutDark = (LinearLayout) findViewById(R.id.layoutDark);
		popUpText = (TextView) findViewById(R.id.popUpText);
		webPopupTitle = (TextView) findViewById(R.id.webPopupTitle);
		btnCancel = (ImageView) findViewById(R.id.btnCancel);
		btnOk = (TextView) findViewById(R.id.btnOk);		
		webPopupTitleBg = (ImageView) findViewById(R.id.webPopupTitleBg);
		
		layoutPopUp.setVisibility(View.INVISIBLE);
		
		youtubeBigImagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
		youtubeBigImageView = (ImageView) findViewById(R.id.youtubeBigImageView);
		
		rightLayout = (LinearLayout) findViewById(R.id.linearLayoutRightList);
		leftLayout = (LinearLayout) findViewById(R.id.linearLayoutLeftList);
		
		//------------------------
		youtubeLayoutMain = (RelativeLayout) findViewById(R.id.youtubeLayoutMain);
		//Central Layout
		scrollViewCentral = (ScrollView) findViewById(R.id.scrollViewCentral);
		centralLayout = (LinearLayout) findViewById(R.id.linearLayoutCentral);
		centralLayoutOne = (LinearLayout) findViewById(R.id.linearLayoutCentralOne);
		centralLayoutTwo = (LinearLayout) findViewById(R.id.linearLayoutCentralTwo);
		centralLayoutThree = (LinearLayout) findViewById(R.id.linearLayoutCentralThree);
		flowLayoutCentral =(FlowLayout)findViewById(R.id.flowLayoutCentral);
		searchCount = (TextView)findViewById(R.id.youtubeIdLabelSearchResultsCount);
		//------------------------

		
		
		youtubeRightLayoutClose= (ImageView)findViewById(R.id.youtubeRightArrowClose);
		youtubeRightBtnClose = (ImageView)findViewById(R.id.youtubeRightBtnClose);
		youtubeRightLayoutOpen= (ImageView)findViewById(R.id.youtubeRightArrowOpen);
		youtubeRightBtnOpen= (ImageView)findViewById(R.id.youtubeRightBtnOpen);
		
		youtubeLeftLayoutClose= (ImageView)findViewById(R.id.youtubeLeftArrowClose);
		youtubeLeftBtnClose = (ImageView)findViewById(R.id.youtubeLeftBtnClose);
		youtubeLeftLayoutOpen= (ImageView)findViewById(R.id.youtubeLeftArrowOpen);
		youtubeLeftBtnOpen= (ImageView)findViewById(R.id.youtubeLeftBtnOpen);
		
		fadeOutRightCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout_right_close);
		fadeInRightOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein_right_open);
		
		fadeOutLeftCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout_left_close);
		fadeInLeftOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein_left_open);
		
        centralTitle = (TextView) findViewById(R.id.youtubeCentralTitle);
        textViewCount = (TextView) findViewById(R.id.youtubeViewCountBig);
        
		searchTextView = (EditText) findViewById(R.id.youtubeSearchText);
//		searchTextView.setShadowLayer(2, 0, 0, Color.RED);
		searchTextView.setInputType(0);
		searchYoutubeButton 	= (ImageView) findViewById(R.id.buttonSearch);
		//searchTextView.setCursorVisible(false);
		
		searchTextView.setVisibility(View.GONE);
		searchYoutubeButton.setVisibility(View.GONE);
		
		searchTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View pV) {
				searchTextView.requestFocus();
				searchTextView.setCursorVisible(true);
				searchTextView.setInputType(1);
				if (!searchTextView.isInEditMode()) {
					// rightLayout.setVisibility(View.GONE);
					// youtubeRightLayoutClose.setVisibility(View.GONE);
					// youtubeRightBtnClose.setVisibility(View.GONE);
//					searchTextView.setText("");
					searchTextView.clearFocus();
				}
			}
		});
		searchTextView.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE)
					
				{
					storeUrl=null;
					isNewSearch=1;
					//mRightAdapter = null;
					String text = searchTextView.getText().toString();
					// searchTextView.setText(searchText);
					String checkByblacklist = ""; 

					checkByblacklist = checkBlacklist(text);
//					//searchTextView.setText(checkByblacklist);
//					searchText = checkByblacklist;
					
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							searchTextView.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					searchTextView.setCursorVisible(false);
					old codes
					 * 
				 	rightLayout.startAnimation(fadeInRightOpenAnimation);
					
					youtubeRightBtnClose
							.startAnimation(fadeInRightOpenAnimation);
					youtubeRightLayoutClose
							.startAnimation(fadeInRightOpenAnimation);
					rightLayout.setVisibility(View.VISIBLE);
					youtubeRightBtnClose.setVisibility(View.VISIBLE);
					youtubeRightLayoutClose.setVisibility(View.VISIBLE);
					
					
					
					//search results are shown in central
					//clear other elements
					leftLayout.setVisibility(View.GONE);
					rightLayout.setVisibility(View.GONE);
					youtubeLeftBtnClose.setVisibility(View.GONE);
					youtubeLeftLayoutClose.setVisibility(View.GONE);
					youtubeLeftLayoutOpen.setVisibility(View.VISIBLE);
					youtubeLeftBtnOpen.setVisibility(View.VISIBLE);
					youtubeRightBtnClose.setVisibility(View.GONE);
					youtubeRightLayoutClose.setVisibility(View.GONE);
					youtubeRightLayoutOpen.setVisibility(View.VISIBLE);
					youtubeRightBtnOpen.setVisibility(View.VISIBLE);
					
					//change the context of central area
					centralLayoutOne.setVisibility(View.GONE);
					centralLayoutTwo.setVisibility(View.VISIBLE);
					centralLayoutThree.setVisibility(View.VISIBLE);
					scrollViewCentral.fullScroll(ScrollView.FOCUS_UP);
					
				}
				return false;
			}
		});
		
		
		
		searchTextView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER) {
						storeUrl=null;
						isNewSearch=1;
						String text = searchTextView.getText().toString();
						// searchTextView.setText(searchText);
						String checkByblacklist = ""; 

						checkByblacklist = checkBlacklist(text);
//						//searchTextView.setText(checkByblacklist);
//						searchText = checkByblacklist;
						
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								searchTextView.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
						searchTextView.setCursorVisible(false);
						
						 old code
						 * when search results are shown in right layout
						 * 
						rightLayout.startAnimation(fadeInRightOpenAnimation);
						youtubeRightBtnClose
								.startAnimation(fadeInRightOpenAnimation);
						youtubeRightLayoutClose
								.startAnimation(fadeInRightOpenAnimation);
						rightLayout.setVisibility(View.VISIBLE);
						youtubeRightBtnClose.setVisibility(View.VISIBLE);
						youtubeRightLayoutClose.setVisibility(View.VISIBLE);
						
						
						//search results are shown in central
						//clear other elements
						leftLayout.setVisibility(View.GONE);
						rightLayout.setVisibility(View.GONE);
						youtubeLeftBtnClose.setVisibility(View.GONE);
						youtubeLeftLayoutClose.setVisibility(View.GONE);
						youtubeLeftLayoutOpen.setVisibility(View.VISIBLE);
						youtubeLeftBtnOpen.setVisibility(View.VISIBLE);
						youtubeRightBtnClose.setVisibility(View.GONE);
						youtubeRightLayoutClose.setVisibility(View.GONE);
						youtubeRightLayoutOpen.setVisibility(View.VISIBLE);
						youtubeRightBtnOpen.setVisibility(View.VISIBLE);
						
						//change the context of central area
						centralLayoutOne.setVisibility(View.GONE);
						centralLayoutTwo.setVisibility(View.VISIBLE);
						centralLayoutThree.setVisibility(View.VISIBLE);
						scrollViewCentral.fullScroll(ScrollView.FOCUS_UP);
						

					}
				}
				return false;
			}
		});
		
		searchYoutubeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View pV) {

				storeUrl=null;
				isNewSearch=1;
				onlineCheckAction();
				
				//mRightAdapter = null;
				String checkByblacklist = ""; 
				checkByblacklist = checkBlacklist(searchTextView.getText().toString());
				//searchText = searchTextView.getText().toString();
				mSearchText = checkByblacklist;
				//searchTextView.setText(searchText);
				
				 old code
				 * when search results are shown in right layout
				 * 
				rightLayout.setVisibility(View.VISIBLE);
				youtubeRightLayoutClose.setVisibility(View.VISIBLE);
				youtubeRightBtnClose.setVisibility(View.VISIBLE);
				leftLayout.setVisibility(View.GONE);
				youtubeLeftBtnClose.setVisibility(View.GONE);
				youtubeLeftLayoutClose.setVisibility(View.GONE);
				youtubeRightLayoutOpen.setVisibility(View.GONE);
				youtubeRightBtnOpen.setVisibility(View.GONE);
				
				
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						searchTextView.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				searchTextView.setCursorVisible(false);
				//search results are shown in central
				//clear other elements
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
				youtubeLeftBtnClose.setVisibility(View.GONE);
				youtubeLeftLayoutClose.setVisibility(View.GONE);
				youtubeLeftLayoutOpen.setVisibility(View.VISIBLE);
				youtubeLeftBtnOpen.setVisibility(View.VISIBLE);
				youtubeRightBtnClose.setVisibility(View.GONE);
				youtubeRightLayoutClose.setVisibility(View.GONE);
				youtubeRightLayoutOpen.setVisibility(View.VISIBLE);
				youtubeRightBtnOpen.setVisibility(View.VISIBLE);
				
				//change the context of central area
				centralLayoutOne.setVisibility(View.GONE);
				centralLayoutTwo.setVisibility(View.VISIBLE);
				centralLayoutThree.setVisibility(View.VISIBLE);
				scrollViewCentral.fullScroll(ScrollView.FOCUS_UP);
				
			}
		});
		
		scrollViewCentral.setOnTouchListener(new OnTouchListener() {
			@Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(mLastY == scrollViewCentral.getScrollY()){
                         //TODO
                    	if(storeUrl!=null&&centralLayoutThree.isShown())
                    	{
                    		storeUrl.startIndex+=storeUrl.maxResults;
                    		convertJSON(storeUrl.toString());
                    	}
                        }else{
                        mLastY = scrollViewCentral.getScrollY();
                    }
                }
                return false;
            }            
		});
		
		//right listview header
		mRightListYoutube = (ListView) findViewById(R.id.rightListViewYoutube);
		mRightListYoutube.addHeaderView(LayoutInflater.from(this).inflate( 
		        R.layout.right_listview_header, null));
		
		//left listview header
		mLeftListYoutube = (ListView) findViewById(R.id.leftListViewYoutube);
		mLeftListYoutube.addHeaderView(LayoutInflater.from(this).inflate( 
		        R.layout.left_listview_header, null));
		
		//Right Layout Close
		youtubeRightLayoutClose.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View pV) {
				// TODO Auto-generated method stub
				rightLayout.startAnimation(fadeOutRightCloseAnimation);
				rightLayout.setVisibility(View.GONE);
				youtubeRightBtnClose.setVisibility(View.GONE);
				youtubeRightLayoutClose.setVisibility(View.GONE);
				youtubeRightLayoutOpen.setVisibility(View.VISIBLE);
				youtubeRightBtnOpen.setVisibility(View.VISIBLE);
				
			}});
		
		//Right Layout Open
		youtubeRightLayoutOpen.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View pV) {
				// TODO Auto-generated method stub
				rightLayout.startAnimation(fadeInRightOpenAnimation);
				youtubeRightBtnClose.startAnimation(fadeInRightOpenAnimation);
				youtubeRightLayoutClose.startAnimation(fadeInRightOpenAnimation);
				rightLayout.setVisibility(View.VISIBLE);
				youtubeRightBtnClose.setVisibility(View.VISIBLE);
				youtubeRightLayoutClose.setVisibility(View.VISIBLE);
				leftLayout.setVisibility(View.GONE);
				youtubeLeftBtnClose.setVisibility(View.GONE);
				youtubeLeftLayoutClose.setVisibility(View.GONE);
				youtubeRightLayoutOpen.setVisibility(View.GONE);
				youtubeRightBtnOpen.setVisibility(View.GONE);
				youtubeLeftLayoutOpen.setVisibility(View.VISIBLE);
				youtubeLeftBtnOpen.setVisibility(View.VISIBLE);
			}});
		
		//Left Layout Close
		youtubeLeftLayoutClose.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View pV) {
				// TODO Auto-generated method stub
				leftLayout.startAnimation(fadeOutLeftCloseAnimation);
				leftLayout.setVisibility(View.GONE);
				youtubeLeftBtnClose.setVisibility(View.GONE);
				youtubeLeftLayoutClose.setVisibility(View.GONE);
				youtubeLeftLayoutOpen.setVisibility(View.VISIBLE);
				youtubeLeftBtnOpen.setVisibility(View.VISIBLE);
			}});
		
		//Left Layout Open
		youtubeLeftLayoutOpen.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View pV) {
				// TODO Auto-generated method stub
				leftLayout.startAnimation(fadeInLeftOpenAnimation);
				youtubeLeftBtnClose.startAnimation(fadeInLeftOpenAnimation);
				youtubeLeftLayoutClose.startAnimation(fadeInLeftOpenAnimation);
				leftLayout.setVisibility(View.VISIBLE);
				youtubeLeftBtnClose.setVisibility(View.VISIBLE);
				youtubeLeftLayoutClose.setVisibility(View.VISIBLE);
				rightLayout.setVisibility(View.GONE);
				youtubeRightBtnClose.setVisibility(View.GONE);
				youtubeRightLayoutClose.setVisibility(View.GONE);
				youtubeLeftLayoutOpen.setVisibility(View.GONE);
				youtubeLeftBtnOpen.setVisibility(View.GONE);
				youtubeRightLayoutOpen.setVisibility(View.VISIBLE);
				youtubeRightBtnOpen.setVisibility(View.VISIBLE);
			}});
		
		//Central Big Image View
		youtubeBigImagePlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View pV) {
			    
				if(videoId == null || videoId.trim().equals("")){
					return;
				}
				Log.i("VideoID", "VideoID: "+ videoId);
				playYoutubeVideo(videoId);
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (popUpText.getText().toString().equals(getResources().getString(R.string.youtube_msg_no_network))) {
					startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
				}
				
			}
		});
		
		
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (popUpText.getText().toString().equals(getResources().getString(R.string.youtube_msg_no_network))) {
					startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
				}
				
			}
		});
		
		onlineCheckAction();
		
	*/}
	
	private void playYoutubeVideo(String videoId)
	{
		Log.e("ZZZZZ","playYoutubeVideo:"+videoId);
		try {
			saveRecentlyView(videoId);
		} catch (Exception e1) {
			Log.e("XXXXX",e1.toString());
		}
		
		
		Intent lVideoIntent = new Intent(null, Uri.parse("ytv://"+videoId), MeepYoutubeActivity.this, OpenYouTubePlayerActivity.class);
		startActivity(lVideoIntent); 
		
//		rightLayout.setVisibility(View.GONE);
//		youtubeRightLayoutClose.setVisibility(View.GONE);
//		youtubeRightBtnClose.setVisibility(View.GONE);
//		leftLayout.setVisibility(View.GONE);
//		youtubeLeftBtnClose.setVisibility(View.GONE);
//		youtubeLeftLayoutClose.setVisibility(View.GONE);
//		youtubeRightLayoutOpen.setVisibility(View.VISIBLE);
//		youtubeRightBtnOpen.setVisibility(View.VISIBLE);
//		youtubeLeftLayoutOpen.setVisibility(View.VISIBLE);
//		youtubeLeftBtnOpen.setVisibility(View.VISIBLE);
//		
//		if(centralLayoutOne.isShown())
//			getRelatedVideos(videoId);
	}

	private void loadAndDrawImage(Message msg) {
		/*if(isNewSearch==1)
		{
			flowLayoutCentral.setLayoutHeight(0);
			flowLayoutCentral.removeAllViews();
		}*/
		//get GSON data
		Bundle bundle = msg.getData();  
		String[] title = bundle.getStringArray("title");  
		String[] id = bundle.getStringArray("id");  
		int[] duration = bundle.getIntArray("duration"); 
		int[] viewCount = bundle.getIntArray("viewCount"); 
		
		int count = id.length;
		String[] youtubePhoto = new String[count];
		String[] titleShort = new String[count];
		
		//right listview setting
		//mRightListYoutube.setDivider(null);
		/*if(centralLayoutTwo.isShown())
		{
			//count of search results
//			int text = title.length;  
//			int text = storeUrl.startIndex + storeUrl.maxResults-1;
			int text = bundle.getInt("total");
			String strMeatFormat = getResources().getString(R.string.youtube_info_about_xxx_results);  
			String strMeatMsg = String.format(strMeatFormat, text); 
			searchCount.setText(strMeatMsg);
		}*/
		mylist = new ArrayList<HashMap<String, Object>>(); 

		isNewSearch = 0;
		flag = 0;
		for(int i=0;i<title.length;i++){
			youtubePhoto[i]= "http://i.ytimg.com/vi/"+id[i]+"/2.jpg";
			URL url = null;
			//2013
			if(title[i]==null){
				continue;
			}
			
			if (title[i].length() > 40){
				titleShort[i] = title[i].substring(0, 35) + "...";
			}
			
			try {
				url = new URL(youtubePhoto[i]);
			} catch (MalformedURLException e) {
				Log.e("Youtube", "MalformedURLException:" + e.toString());
			}
			Log.i("ViewCount", "ViewCount"+ viewCount[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();  
			map.put("youtubeId", id[i]);
			map.put("youtubeTitleShort", titleShort[i]);  
			map.put("youtubeTitleLong", title[i]);  
			map.put("youtubeImageView", youtubePhoto[i]);  
			map.put("youtubeDuration", getDuration(duration[i])); 
			String viewText = getResources().getString(R.string.youtube_title_no_of_views);
			map.put("youtubeViewCount", getViewCountFormat(viewCount[i]) + "  " + viewText); 
			mylist.add(map);
			addFlowChildren(map);
			
			
		}
		
		
		
		/*
		 * old codes
		 * search results it right layout out 
		mRightAdapter = new MyAdapter(MeepYoutubeActivity.this, R.layout.right_listitem, mylist);
		mRightListYoutube.setAdapter(mRightAdapter);
		*/
		
//		mLeftListYoutube.setOnItemClickListener(new OnItemClickListener() {
//	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//	        	
//	        	//Disable the onclick event for the list view header
//	        	if (position == 0){
//	        		return;
//	        	}
//
//	        	//position-1 because of the listview Header
//	        	HashMap<String,Object> item = mylist.get(position-1);
//	        	
//	            Log.i("Click","Item:"+item.get("youtubeId"));
//	            videoId = item.get("youtubeId").toString();
//	            
//	            centralTitle.setText((String)item.get("youtubeTitleLong"));
//	            
//	            textViewCount.setText((String)item.get("youtubeViewCount"));
//	            
//	            imageView = (ImageView) findViewById(R.id.youtubeBigImageView);
//	            imagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
//	    	    Bitmap cachedImage = null;
//	    	    try {
//	    	    	cachedImage = imageLoader.loadImage((String) item.get("youtubeImageView"), new ImageLoadedListener() {	
//	    	    		public void imageLoaded(Bitmap imageBitmap) {
//	    	    			imageView.setImageBitmap(imageBitmap);
//	    	    			imagePlay.setVisibility(View.GONE);
//	    	    			imagePlay.setVisibility(View.VISIBLE);
//	    	    			//notifyDataSetChanged();                
//	    	    			}
//	    	    		});
//	    	    	if(cachedImage!=null){
//	    	    		imageView.setImageBitmap(cachedImage);
//	    	    		imagePlay.setVisibility(View.GONE);
//	    	    		imagePlay.setVisibility(View.VISIBLE);
//	    	    	}
//	    	    } catch (MalformedURLException e) {
//	    	    	Log.e("URL", "Bad remote image URL: " + e.toString());
//	    	    }
//	    	    playYoutubeVideo(videoId);
//	        }
//	    });
	}
	
//	public void leftListViewSet(String[] recommendationList){
//		//left listview setting
//		leftListYoutube.setDivider(null);
//		myRecommendationlist = new ArrayList<HashMap<String, Object>>(); 
//		
//        int count = 0;
//        count = recommendationList.length;
//        
//        //return data for the list view
//        String[] title = new String[count];
//        String[] id = new String[count];
//        int[] duration = new int[count];
//        int[] viewCount = new int[count];
//        String[] youtubePhoto = new String[count];
//		String[] titleShort = new String[count];
//        
//		for (int i = 0; i < count; i++) {
//			String url = "https://gdata.youtube.com/feeds/api/videos/" + recommendationList[i] + "?alt=json";
//
//			Gson gson = new Gson();
//			Reader r = new InputStreamReader(getJSONData(url));
//			YoutubeGData leftListView;
//			try {
//				// convert JSON to GSON class
//				leftListView = gson.fromJson(r, YoutubeGData.class);
//			} catch (Exception e) {
//				Log.w("Youtube", "cannot load youtube video:" + url);
//				continue;
//			}
//	        
//	    	title[i] = leftListView.getEntry().getTitle().get$t();
//	    	id[i] = recommendationList[i];
//	    	duration[i] = leftListView.getEntry().getMedia$group().getYt$duration().getSeconds();
//	    	viewCount[i] = leftListView.getEntry().getYt$statistics().getViewCount();
//
//			youtubePhoto[i]= "http://i.ytimg.com/vi/"+recommendationList[i]+"/2.jpg";
//
//			titleShort[i] = title[i];
//			if (title[i].length() > 40){
//				titleShort[i] = title[i].substring(0, 35) + "...";
//			}
//			
//			try {
//				new URL(youtubePhoto[i]);
//			} catch (MalformedURLException e) {
//				Log.e("Youtube", "MalformedURLException:" + e.toString());
//			}
//			Log.i("ViewCount", "ViewCount"+ viewCount[i]);
//			HashMap<String, Object> map = new HashMap<String, Object>();  
//			map.put("youtubeId", id[i]);
//			map.put("youtubeTitleShort", titleShort[i]);  
//			map.put("youtubeTitleLong", title[i]);  
//			map.put("youtubeImageView", youtubePhoto[i]);  
//			map.put("youtubeDuration", getDuration(duration[i])); 
//			map.put("youtubeViewCount", viewCount[i] + "    Views"); 
//			myRecommendationlist.add(map);  
//		}
////		String url = 
////				"https://gdata.youtube.com/feeds/api/videos/"
////						+ "B4DKgEROAds" + "?alt=json";
////		
////        Gson gson = new Gson();
////        Reader r = new InputStreamReader(getJSONData(url));
////        //convert JSON to GSON class
////        YoutubeGData leftListView = gson.fromJson(r, YoutubeGData.class);
////        
////        //return data for the list view
////        String title = "";
////        String id = "";
////        int duration = 0;
////        int viewCount = 0;
////    	title = leftListView.getEntry().getTitle().get$t();
////    	id = "B4DKgEROAds";
////    	duration = leftListView.getEntry().getMedia$group().getYt$duration().getSeconds();
////    	viewCount = leftListView.getEntry().getYt$statistics().getViewCount();
////
////		String youtubePhoto = "";
////		String titleShort = "";
////
////		youtubePhoto= "http://i.ytimg.com/vi/"+"B4DKgEROAds"+"/2.jpg";
////		URL photoUrl = null;
////		titleShort = title;
////		if (title.length() > 40){
////			titleShort = title.substring(0, 35) + "...";
////		}
////			
////		try {
////			photoUrl = new URL(youtubePhoto);
////		} catch (MalformedURLException e) {
////			Log.e("Youtube", "MalformedURLException:" + e.toString());
////		}
////		Log.i("ViewCount", "ViewCount"+ viewCount);
////		HashMap<String, Object> map = new HashMap<String, Object>();  
////		map.put("youtubeId", id);
////		map.put("youtubeTitleShort", titleShort);  
////		map.put("youtubeTitleLong", title);  
////		map.put("youtubeImageView", youtubePhoto);  
////		map.put("youtubeDuration", getDuration(duration)); 
////		map.put("youtubeViewCount", viewCount + "    Views"); 
////		myRecommendationlist.add(map);  
//
//		myAdapter = new MyAdapter(MeepYoutubeActivity.this, R.layout.left_listitem, myRecommendationlist);
//		leftListYoutube.setAdapter(myAdapter);
//		
//		leftListYoutube.setOnItemClickListener(new OnItemClickListener() {
//	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//	        	
//	        	//Disable the onclick event for the list view header
//	        	if (position == 0){
//	        		return;
//	        	}
//
//	        	//position-1 because of the listview Header
//	        	HashMap<String,Object> item = myRecommendationlist.get(position-1);
//	        	
//	            Log.i("Click","Item:"+item.get("youtubeId"));
//	            videoId = item.get("youtubeId").toString();
//	            
//	            centralTitle.setText((String)item.get("youtubeTitleLong"));
//	            
//	            textViewCount = (TextView) findViewById(R.id.youtubeViewCountBig);
//	            textViewCount.setText((String)item.get("youtubeViewCount"));
//	            
//	            imageView = (ImageView) findViewById(R.id.youtubeBigImageView);
//	            imagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
//	    	    Bitmap cachedImage = null;
//	    	    try {
//	    	    	cachedImage = imageLoader.loadImage((String) item.get("youtubeImageView"), new ImageLoadedListener() {	
//	    	    		public void imageLoaded(Bitmap imageBitmap) {
//	    	    			imageView.setImageBitmap(imageBitmap);
//	    	    			imagePlay.setVisibility(View.GONE);
//	    	    			imagePlay.setVisibility(View.VISIBLE);
//	    	    			//notifyDataSetChanged();                
//	    	    			}
//	    	    		});
//	    	    	if(cachedImage!=null){
//	    	    		imageView.setImageBitmap(cachedImage);
//	    	    		imagePlay.setVisibility(View.GONE);
//	    	    		imagePlay.setVisibility(View.VISIBLE);
//	    	    	}
//	    	    } catch (MalformedURLException e) {
//	    	    	Log.e("URL", "Bad remote image URL: " + e.toString());
//	    	    }
//	        }
//	    });
//	}
		
	public InputStream getJSONData(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		URI uri;
		InputStream data = null;
		
		try {
			uri = new URI(url);
			HttpGet method = new HttpGet(uri);
			HttpResponse response = httpClient.execute(method);
			data = response.getEntity().getContent();
		} catch (Exception ex) {
			Log.e("Youtube", "Http Execption:" + ex.toString());
		}
		return data;
	}
	
	public void convertJSON(String url){
		try{
						
	        Gson gson = new Gson();
	        Reader r = new InputStreamReader(getJSONData(url));
	        //--2013-4-23 -Amy-
	        //InputStream inStream=getApplicationContext().getAssets().open("resultjson.txt");
	        //Reader r = new InputStreamReader(inStream);
	        
	        //convert JSON to GSON class
	        YoutubeGData youtubeGData = gson.fromJson(r, YoutubeGData.class);
	                
	        if (youtubeGData.getFeed().getEntry() != null){
		        //get data from the GSON
		        int count = 0;
		        count = youtubeGData.getFeed().getEntry().size();
		        
		        //return data for the list view
		        String[] title = new String[count];
		        String[] id = new String[count];
		        int[] duration = new int[count];
		        int[] viewCount = new int[count];
		        int i = 0;
		        int total = 0;
		        total = youtubeGData.getFeed().getOpenSearch$totalResults().get$t();
		        for(Entry entry : youtubeGData.getFeed().getEntry()) { 


				//2013-02-21 - Raymond - Check Video status
		        	if(entry.getApp$control()!=null || (entry.getMedia$group().getMedia$restriction()!=null)){
		        		total--;
		        		count--;		        		
		        		continue;
		        	}else{
		        		
			        	title[i] = entry.getTitle().get$t();
			        	id[i] = entry.getMedia$group().getYt$videoid().get$t();
			        	duration[i] = entry.getMedia$group().getYt$duration().getSeconds();
			        	viewCount[i] = entry.getYt$statistics().getViewCount();

			            i++;		        		
		        	}		        	
		        }
		        
				Message msg = new Message();
				msg.what = 1;
				Bundle bundle = new Bundle();  
				bundle.putStringArray("title", title);  
				bundle.putStringArray("id", id);  
				bundle.putIntArray("duration", duration);
				bundle.putIntArray("viewCount", viewCount);
				bundle.putInt("total", total);
//				Log.d("test=====","total="+total);
				msg.setData(bundle);  
				MeepYoutubeActivity.this.mHandlerReadImg.sendMessage(msg);
	        }else{
	        	Message msg = new Message();
	        	msg.what = 2;
	        	mHandlerReadImg.sendMessage(msg);
	        }
		}catch(Exception ex){
			ex.printStackTrace();  
		}
	}
	private Gson mGson = null;
	
	private void initLeftAdaptor()
	{
		myRecommendationOslist = new ArrayList<HashMap<String, Object>>(); 
		mLeftAdapter = new MyAdapter(MeepYoutubeActivity.this, R.layout.left_listitem, myRecommendationOslist);
		mLeftListYoutube.setAdapter(mLeftAdapter);
		
		mLeftListYoutube.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	
	        	//Disable the onclick event for the list view header
	        	if (position == 0){
	        		return;
	        	}

	        	//position-1 because of the listview Header
	        	HashMap<String,Object> item = myRecommendationOslist.get(position-1);
	        	
	            Log.i("Click","Item:"+item.get("youtubeId"));
	            videoId = item.get("youtubeId").toString();
	            
	            /*centralTitle.setText((String)item.get("youtubeTitleLong"));
	            
	            textViewCount = (TextView) findViewById(R.id.youtubeViewCountBig);
	            textViewCount.setText((String)item.get("youtubeViewCount"));*/
	            
	            imageView = (ImageView) findViewById(R.id.youtubeBigImageView);
	            imagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
	    	    Bitmap cachedImage = null;
	    	    try {
	    	    	
	    	    	cachedImage = imageLoader.loadImage(replaceBigViewImage((String) item.get("youtubeImageView")), new ImageLoadedListener() {	
	    	    		public void imageLoaded(Bitmap imageBitmap) {
	    	    			imageView.setImageBitmap(imageBitmap);
	    	    			imagePlay.setVisibility(View.GONE);
	    	    			imagePlay.setVisibility(View.VISIBLE);
	    	    			//notifyDataSetChanged();                
	    	    			}
	    	    		});
	    	    	if(cachedImage!=null){
	    	    		imageView.setImageBitmap(cachedImage);
	    	    		imagePlay.setVisibility(View.GONE);
	    	    		imagePlay.setVisibility(View.VISIBLE);
	    	    	}
	    	    } catch (MalformedURLException e) {
	    	    	Log.e("URL", "Bad remote image URL: " + e.toString());
	    	    }
	    	    
	    	    playYoutubeVideo(videoId);
	        }
	    });
	}
	
	HashMap<String, Object> mLeftLoadingMap =null;
	private void loadLeftYoutubeEntry(String id)
	{
		try{
//			String url = "https://gdata.youtube.com/feeds/api/videos/" + id + "?alt=json";
//			Reader r = new InputStreamReader(getJSONData(url));
//			YoutubeGData leftListView = null;
//			try {
//				// convert JSON to GSON class
//				leftListView = mGson.fromJson(r, YoutubeGData.class);
//			} catch (Exception e) {
//				Log.w("Youtube", "cannot load youtube video:" + url);
//				return;
//			}
//	        
//	    	String title = leftListView.getEntry().getTitle().get$t();
//	    	int duration = leftListView.getEntry().getMedia$group().getYt$duration().getSeconds();
//	    	int viewCount = leftListView.getEntry().getYt$statistics().getViewCount();
//
//			String youtubePhoto = "http://i.ytimg.com/vi/"+id+"/2.jpg";
//
//			String titleShort = title;
//			if (title.length() > 40){
//				titleShort = title.substring(0, 35) + "...";
//			}
//			
//			try {
//				new URL(youtubePhoto);
//			} catch (MalformedURLException e) {
//				Log.e("Youtube", "MalformedURLException:" + e.toString());
//			}
//			Log.i("ViewCount", "ViewCount"+ viewCount);
//			
//			HashMap<String, Object> map = new HashMap<String, Object>();  
//			map.put("youtubeId", id);
//			map.put("youtubeTitleShort", titleShort);  
//			map.put("youtubeTitleLong", title);  
//			map.put("youtubeImageView", youtubePhoto);  
//			map.put("youtubeDuration", getDuration(duration)); 
//			map.put("youtubeViewCount", getViewCountFormat(viewCount) + "  Views"); 
			mLeftLoadingMap = generateOsListItemEntry(id);
			//myRecommendationlist.add(map);  
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//2013-03-13 - raymond - fix null youtube data issue
					if(mLeftLoadingMap!=null){
						if(!myRecommendationOslist.contains(mLeftLoadingMap))
						{
							myRecommendationOslist.add(mLeftLoadingMap); 
						}
						mLeftAdapter.notifyDataSetChanged();
					}					
				}
			});
		}catch(Exception ex){
			//Log.e("ST:", "JSON Exception:" + ex.toString());  
		}
	}
	//---------------------------------
	//right adapter
	private void initRightAdaptor()
	{
		myRecommendationParentlist = new ArrayList<HashMap<String, Object>>();
	
		mRightAdapter = new MyAdapter(MeepYoutubeActivity.this, R.layout.right_listitem, myRecommendationParentlist);
		mRightListYoutube.setAdapter(mRightAdapter);
		
		
    	
        
 
		mRightListYoutube.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        	
	        	//Disable the onclick event for the list view header
	        	if (position == 0){
	        		return;
	        	}

	        	//position-1 because of the listview Header
	        	HashMap<String,Object> item = myRecommendationParentlist.get(position-1);
	        	
	            Log.i("Click","Item:"+item.get("youtubeId"));
	            videoId = item.get("youtubeId").toString();
	            
	            /*centralTitle.setText((String)item.get("youtubeTitleLong"));
	            
	            textViewCount = (TextView) findViewById(R.id.youtubeViewCountBig);
	            textViewCount.setText((String)item.get("youtubeViewCount"));*/
	            
	            imageView = (ImageView) findViewById(R.id.youtubeBigImageView);
	            imagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
	    	    Bitmap cachedImage = null;
	    	    try {
	    	    	cachedImage = imageLoader.loadImage(replaceBigViewImage((String) item.get("youtubeImageView")), new ImageLoadedListener() {	
	    	    		public void imageLoaded(Bitmap imageBitmap) {
	    	    			imageView.setImageBitmap(imageBitmap);
	    	    			imagePlay.setVisibility(View.GONE);
	    	    			imagePlay.setVisibility(View.VISIBLE);
	    	    			//notifyDataSetChanged();                
	    	    			}
	    	    		});
	    	    	if(cachedImage!=null){
	    	    		imageView.setImageBitmap(cachedImage);
	    	    		imagePlay.setVisibility(View.GONE);
	    	    		imagePlay.setVisibility(View.VISIBLE);
	    	    	}
	    	    } catch (MalformedURLException e) {
	    	    	Log.e("URL", "Bad remote image URL: " + e.toString());
	    	    }
	    	    
	    	    playYoutubeVideo(videoId);
	        }
	    });
	}
	
	HashMap<String, Object> mRightLoadingMap =null;
	private void loadRightYoutubeEntry(String id)
	{
		try{
//			String url = "https://gdata.youtube.com/feeds/api/videos/" + id + "?alt=json";
//			Reader r = new InputStreamReader(getJSONData(url));
//			YoutubeGData rightListView = null;
//			try {
//				// convert JSON to GSON class
//				rightListView = mGson.fromJson(r, YoutubeGData.class);
//			} catch (Exception e) {
//				Log.w("Youtube", "cannot load youtube video:" + url);
//				return;
//			}
//	        
//	    	String title = rightListView.getEntry().getTitle().get$t();
//	    	int duration = rightListView.getEntry().getMedia$group().getYt$duration().getSeconds();
//	    	int viewCount = rightListView.getEntry().getYt$statistics().getViewCount();
//
//			String youtubePhoto = "http://i.ytimg.com/vi/"+id+"/2.jpg";
//
//			String titleShort = title;
//			if (title.length() > 40){
//				titleShort = title.substring(0, 35) + "...";
//			}
//			
//			try {
//				new URL(youtubePhoto);
//			} catch (MalformedURLException e) {
//				Log.e("Youtube", "MalformedURLException:" + e.toString());
//			}
//			Log.i("ViewCount", "ViewCount"+ viewCount);
//			
//			HashMap<String, Object> map = new HashMap<String, Object>();  
//			map.put("youtubeId", id);
//			map.put("youtubeTitleShort", titleShort);  
//			map.put("youtubeTitleLong", title);  
//			map.put("youtubeImageView", youtubePhoto);  
//			map.put("youtubeDuration", getDuration(duration)); 
//			map.put("youtubeViewCount", getViewCountFormat(viewCount) + "  Views"); 
			mRightLoadingMap = generateListItemEntry(id);
			//myRecommendationParentlist.add(map);  
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//2013-03-13 - raymond - fix null youtube data issue
					if(mRightLoadingMap!=null){
						if(!myRecommendationParentlist.contains(mRightLoadingMap))
						{
							myRecommendationParentlist.add(mRightLoadingMap); 
						}
						mRightAdapter.notifyDataSetChanged();
					}					
				}
			});
		}catch(Exception ex){
			//Log.e("ST:", "JSON Exception:" + ex.toString());  
		}
	}
	
	
	public HashMap<String,Object> generateListItemEntry(String id)
	{
		String url = "https://gdata.youtube.com/feeds/api/videos/" + id + "?alt=json";
		Reader r = new InputStreamReader(getJSONData(url));
		YoutubeGData rightListView = null;
		try {
			// convert JSON to GSON class
			rightListView = mGson.fromJson(r, YoutubeGData.class);
		} catch (Exception e) {
			Log.w("Youtube", "cannot load youtube video:" + url);
			return null;
		}
        
		//2013-02-21 - Raymond - Check Video status
    	if(rightListView.getEntry().getApp$control()!=null && rightListView.getEntry().getApp$control().getYt$state()!=null && rightListView.getEntry().getApp$control().getYt$state().get$t()!=null){
    		return null;
    	}
		
    	String title = rightListView.getEntry().getTitle().get$t();
    	int duration = rightListView.getEntry().getMedia$group().getYt$duration().getSeconds();
    	int viewCount = rightListView.getEntry().getYt$statistics().getViewCount();

		String youtubePhoto = "http://i.ytimg.com/vi/"+id+"/2.jpg";
		Log.e("youtubePhoto link",youtubePhoto);
		
		String titleShort = title;
		if (title.length() > 40){
			titleShort = title.substring(0, 35) + "...";
		}
		
		try {
			new URL(youtubePhoto);
		} catch (MalformedURLException e) {
			Log.e("Youtube", "MalformedURLException:" + e.toString());
		}
		Log.i("ViewCount", "ViewCount"+ viewCount);
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
		map.put("youtubeId", id);
		map.put("youtubeTitleShort", titleShort);  
		map.put("youtubeTitleLong", title);  
		map.put("youtubeImageView", youtubePhoto);  
		map.put("youtubeDuration", getDuration(duration)); 
		map.put("youtubeViewCount", viewCount + "    Views"); 	
		return map;
	}
	
	public HashMap<String,Object> generateOsListItemEntry(String id)
	{
		String url = "https://gdata.youtube.com/feeds/api/videos/" + id + "?alt=json";
		Reader r = new InputStreamReader(getJSONData(url));
		YoutubeGData leftListView = null;
		try {
			// convert JSON to GSON class
			leftListView = mGson.fromJson(r, YoutubeGData.class);
		} catch (Exception e) {
			Log.w("Youtube", "cannot load youtube video:" + url);
			return null;
		}
        
    	String title = leftListView.getEntry().getTitle().get$t();
    	int duration = leftListView.getEntry().getMedia$group().getYt$duration().getSeconds();
    	int viewCount = leftListView.getEntry().getYt$statistics().getViewCount();

		String youtubePhoto = "http://i.ytimg.com/vi/"+id+"/2.jpg";

		String titleShort = title;
		if (title.length() > 40){
			titleShort = title.substring(0, 35) + "...";
		}
		
		try {
			new URL(youtubePhoto);
		} catch (MalformedURLException e) {
			Log.e("Youtube", "MalformedURLException:" + e.toString());
		}
		Log.i("ViewCount", "ViewCount"+ viewCount);
		
		HashMap<String, Object> map = new HashMap<String, Object>();  
		map.put("youtubeId", id);
		map.put("youtubeTitleShort", titleShort);  
		map.put("youtubeTitleLong", title);  
		map.put("youtubeImageView", youtubePhoto);  
		map.put("youtubeDuration", getDuration(duration)); 
		map.put("youtubeViewCount", getViewCountFormat(viewCount) + "  Views"); 
		return map;
	}
	//-----------------------
	
	//-----------------------
	//center
//	private void initCenterAdaptor()
//	{
//	}
	//-----------------------

	
	public class youtubeUrl extends GenericUrl {
		@Key final String alt = "json";
		@Key String author;
		@Key("max-results")
		public Integer maxResults;
		@Key("start-index")
		public Integer startIndex;
		@Key String safeSearch = "strict";
		@Key int v = 2;

		public youtubeUrl(String url) {
			super(url);
	    }
	}
		
	public String getDuration(int duration){
		String formatedDuration = "00:00:00";
		
		if (duration > 0){
			int hours = (int) Math.floor(duration/60/24);
			int minutes = (int) Math.floor(duration/60);
			int seconds = duration % 60;
			formatedDuration = getFormatedTime(hours) + ":" + getFormatedTime(minutes) + ":" + getFormatedTime(seconds);
		}
		
		return formatedDuration;
	}
	  
	public String getFormatedTime(int time){
		String formatedTime = "00";
		
		if (time >= 0 && time<10){
			formatedTime = "0" + time;
		}else {
			formatedTime = String.valueOf(time);
		}
		
		return formatedTime;
	}
	
	public void saveRecentlyView(String in_videoId) throws IOException{
		String FILENAME = "recently_view";
		String string = "id:" + in_videoId + "youtubeCentralTitle:" + "null" + "youtubeViewCount:" + "null";
		Log.e("ZZZZZ","act:"+string);

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(string.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e("ZZZZZ","saveRecentlyView:"+e.toString());
		}
	}
	
	public void saveRecentlyView() throws IOException{
		String FILENAME = "recently_view";
		String string = "id:" + videoId + "youtubeCentralTitle:" + "null" + "youtubeViewCount:" + "null";
		Log.e("ZZZZZ","act:"+string);

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(string.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e("ZZZZZ","saveRecentlyView:"+e.toString());
		}
	}
	
	public String loadRecentlyView() throws IOException{
		FileInputStream inputStream = mContext.openFileInput("recently_view");
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while ((len=inputStream.read(buffer))!=-1){
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		byte[] data=outStream.toByteArray();
		String name=new String(data);
		return name.replaceAll("null","");
	}
	
	private String checkBlacklist(String searchText){
		String searchFilterText = searchText;
		boolean isBlackLsit = false;
		if (byPassWord != null){
			for (int i = 0; i < byPassWord.length; i++) {
				if (searchText.equals(byPassWord[i])){
						Log.d("youtube", "youtube Search filter1: " + byPassWord[i]);
						return searchFilterText;
					}
					//Log.d("youtube", "youtube bypass received: " + byPassWord[i]);
			}
		}

		badWordChecker.setBlacklist(blackListWord);
		if (!badWordChecker.isStringSafe(searchText)){
			searchFilterText = "";
			isBlackLsit = true;
		}

		if (isBlackLsit){
//			String title = getResources().getString(R.string.youtube_title_search_results);
//			String notice = getResources().getString(R.string.youtube_info_results_blocked);
//			notification = new NotificationMessage(mContext, null, title, notice);
			searchText = "";
		}
		else
		{
			mSearchText = searchText;
		}
		
		Log.d("youtube", "youtube Search: " + searchFilterText);
		return searchFilterText;
	}
	BadWordChecker badWordChecker;
	private void getYoutubeWhiteList()
	{
		String sql = "select * from " + TableBlacklist.S_TABLE_NAME +  " where " + 
				//TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Youtube + "'" ;
					TableBlacklist.S_LIST_TYPE + " = 'bypass'" ;
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_BLACK_LIST, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}
	
	private void getYoutubeBlackList()
	{
		String sql = "select * from " + TableBlacklist.S_TABLE_NAME +  " where " + 
				//TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Youtube + "'" ;
					TableBlacklist.S_LIST_TYPE + " = 'browser'" ;
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_BLACK_LIST, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}
	/* old codes
	 * get os and parent recommendation lsit
	 * 
	private void getYoutubeRecommendationList()
	{
		String sql = "select * from " + TableRecommendation.S_TABLE_NAME +  " where " + 
				//TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Youtube + "'" ;
				TableRecommendation.S_LIST_TYPE + " = 'os-youtube' OR " + TableRecommendation.S_LIST_TYPE + " = 'youtube'";
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_GET_RECOMMENDATION, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}*/
	
	private void getYoutubeOsRecommendationList()
	{
		String sql = "select * from " + TableRecommendation.S_TABLE_NAME +  " where " + 
				//TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Youtube + "'" ;
				TableRecommendation.S_LIST_TYPE + " = 'os-youtube'";
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_GET_RECOMMENDATION, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}
	
	private void getYoutubeParentRecommendationList()
	{
		String sql = "select * from " + TableRecommendation.S_TABLE_NAME +  " where " + 
				//TableBlacklist.S_LIST_TYPE + " = '" + Global.AppType.Youtube + "'" ;
				TableRecommendation.S_LIST_TYPE + " = 'youtube'";
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_GET_RECOMMENDATION, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}

	
	private void getPermissionList()
	{
		String sql = "SELECT * FROM " + TablePermission.S_TABLE_NAME + " WHERE " + TablePermission.S_APP_NAME + " = 'SecurityLevel'";
		
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DATABASE_GET_PERMISSION, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Youtube);
		sendBroadcast(MeepAppMessageCtrl.getBroadcastIntent(mam, AppType.MeepMessage));
	}
	
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	public void onlineCheckAction() {
		if(true){return;}
		if (!isOnline()) {
			//layoutDark.setVisibility(View.VISIBLE);
			/*layoutPopUp.setVisibility(View.VISIBLE);
			webPopupTitle.setText(R.string.youtube_title_uh_oh);
			popUpText.setText(R.string.youtube_msg_no_network);*/
//			btnCancel.setVisibility(View.VISIBLE);
//			btnCancel.setText("Wifi Settings");

			// engine.setVisibility(View.INVISIBLE);
			// btnPopUp1.setVisibility(View.INVISIBLE);
//			btnOk.setVisibility(View.INVISIBLE);
		} else {
			//layoutDark.setVisibility(View.INVISIBLE);
			//layoutPopUp.setVisibility(View.INVISIBLE);
//			btnCancel.setVisibility(View.INVISIBLE);

		}
	}
	
	@Override
	protected void onDestroy() {
		if(mMsgReceiver!=null){
			mMsgReceiver.close();
		}
		//timer
		if (mTimerSeconds != null)
	    {
	        mTimerSeconds.cancel();
	    }

		// unbind services
		ServiceManager.unbindServices(this);
		
		//android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
	



	private int flag=0;
	public void addFlowChildren(HashMap<String,Object> map)
	{
		/*View view = LayoutInflater.from(this).inflate(R.layout.center_listitem, flowLayoutCentral,false);
		
		TextView textId;
		final ImageView image;    
		TextView textTitleShort;
		TextView textTitleLong;
		TextView textDuration;
		TextView textvViewCount;

		try {
			textId = (TextView)view.findViewById(R.id.youtubeId);
			image = (ImageView)view.findViewById(R.id.youtubeImageView);
			textTitleShort = (TextView)view.findViewById(R.id.youtubeTitleShort);
			textTitleLong = (TextView)view.findViewById(R.id.youtubeTitleLong);
			textDuration = (TextView)view.findViewById(R.id.youtubeDuration);
			textvViewCount = (TextView)view.findViewById(R.id.youtubeViewCount);
		} catch( ClassCastException e ) {
			Log.e("Youtube related videos", "Your layout must provide an image and a text view with ID's icon and text.", e);
			throw e;
		}*/
		Bitmap cachedImage = null;
		/*try{
			cachedImage = imageLoader.loadImage((String) map.get("youtubeImageView"), new ImageLoadedListener() {	
				public void imageLoaded(Bitmap imageBitmap) {
					image.setImageBitmap(imageBitmap);
					Log.d("Youtube related videos", "youtube network image loaded");
				}
				
			});
			
		} catch (MalformedURLException e) {
			Log.e("Youtube related videos", "Bad remote image URL: " + e.toString());
		}
		textId.setText((String) map.get("youtubeId"));
		textTitleShort.setText((String) map.get("youtubeTitleShort"));
		textTitleLong.setText((String) map.get("youtubeTitleLong"));
		textDuration.setText((String) map.get("youtubeDuration"));
		textvViewCount.setText((String)map.get("youtubeViewCount"));
		if( cachedImage != null ) {
			image.setImageBitmap(cachedImage);
		}else{
//			image.setImageResource(R.drawable.icon);
		}
		
		view.setOnClickListener((new View.OnClickListener() {
			
			@Override
			public void onClick(View pV) {
				
				TextView tid = (TextView)pV.findViewById(R.id.youtubeId);
				TextView tTitleShort = (TextView)pV.findViewById(R.id.youtubeTitleShort);
//				TextView tTitleLong = (TextView)pV.findViewById(R.id.youtubeTitleLong);
//				TextView tDuration = (TextView)pV.findViewById(R.id.youtubeDuration);
				TextView tvViewCount = (TextView)pV.findViewById(R.id.youtubeViewCount);


				videoId  = tid.getText().toString();
				
				if(videoId == null || videoId.trim().equals("")){
					return;
				}
				playYoutubeVideo(videoId);
				
				Log.i("Click","Item:"+videoId);
	            
	            centralTitle.setText(tTitleShort.getText().toString());
	            
	            textViewCount = (TextView) findViewById(R.id.youtubeViewCountBig);
	            textViewCount.setText(tvViewCount.getText().toString());
	            
	            imageView = (ImageView) findViewById(R.id.youtubeBigImageView);
	            imagePlay = (ImageView) findViewById(R.id.youtubeBigImagePlay);
	    	    Bitmap cachedImage = null;
	    	    try {
	    	    	cachedImage = imageLoader.loadImage("http://i.ytimg.com/vi/"+videoId+"/0.jpg", new ImageLoadedListener() {	
	    	    		public void imageLoaded(Bitmap imageBitmap) {
	    	    			imageView.setImageBitmap(imageBitmap);
	    	    			imagePlay.setVisibility(View.GONE);
	    	    			imagePlay.setVisibility(View.VISIBLE);
	    	    			//notifyDataSetChanged();            
	    	    			
	    	    			}
	    	    		});
	    	    	if(cachedImage!=null){
	    	    		imageView.setImageBitmap(cachedImage);
	    	    		imagePlay.setVisibility(View.GONE);
	    	    		imagePlay.setVisibility(View.VISIBLE);
	    	    	}
	    	    } catch (MalformedURLException e) {
	    	    	Log.e("URL", "Bad remote image URL: " + e.toString());
	    	    }
			}
		}));
		
		int v = 3;
		int h = 6;
		//add view and set padding
        flowLayoutCentral.addView(view, new FlowLayout.LayoutParams(v, h));
        
        flag++;
		if(flag>=3)
		{
	        //item height=122;padding=6
	        flowLayoutCentral.addLayoutHeight(122 + h);
	        flag-=3;
		}*/
	}
	//------------------------
	//related video finished
	//------------------------	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	System.exit(0);
    		moveTaskToBack(true);
//    		
//	    	if(centralLayoutTwo.isShown())
//	    	{
//	    		centralLayoutTwo.setVisibility(View.GONE);
//	    		centralLayoutOne.setVisibility(View.VISIBLE);
//	    		centralLayoutThree.setVisibility(View.GONE);
//	    		scrollViewCentral.fullScroll(ScrollView.FOCUS_UP);
//	    		storeUrl=null;
////	    		getRelatedVideos(videoId);
//	    	}
//	    	else
//	    	{
//	    		System.exit(0);
//	    		moveTaskToBack(true);
//	    	}
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	DecimalFormat format = new DecimalFormat("#,###,###,###,###"); 
	public String getViewCountFormat(int viewcount)
	{
		String view = format.format(viewcount);
		return view;
	}
	
	public String replaceBigViewImage(String s)
	{
		return s.replaceAll("2.jpg", "0.jpg");
	}


	@Override
	public void actionSelectedItem(String videoId) {		
		Log.e("YYYYY","actionSelectedItem: "+videoId);
		playYoutubeVideo(videoId);
//		menu.showContent();
//		if (mainViewFragment == null) {
//			mainViewFragment = mainViewFragment.newInstance();
//			mainViewFragment.init(url);
//			addFragment(mainViewFragment);
//		} else {
//			mainViewFragment.updateUrl(url);
//		}
	}

	private void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.setCustomAnimations( R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left );
		fragmentTransaction.commit();
	}

	public void popFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}
	
	public void initRightMenu() {
		// set right menu
		setSlidingActionBarEnabled(false);
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setBehindScrollScale(1.0f);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.5f);
		// menu.setSecondaryMenu(R.layout.properties);
		// menu.setSecondaryShadowDrawable(R.drawable.shadow);
		setTitle("Sliding Bar");

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		rightMenuFragment = new RightMenuFragment();
		t.replace(R.id.menu_frame, rightMenuFragment);
		t.commit();
	}

	public void displayRightMenu() {
		menu.showMenu();
		if(rightMenuFragment != null)
		{
			rightMenuFragment.getCurrentAllRecommendations();
		}
	}

	public void dismissRightMenu() {
		menu.showContent();
	}

	public void popupAddBookmark(String url) {
		
	}

	public void popup(int title, int message) {
		CommonPopup popup = new CommonPopup(MeepYoutubeActivity.this, title, message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public void hideKeyboard(View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	

	public void onClickMenuButton(View view) {
		hideKeyboard(view);
		switch (view.getId()) {
		case R.id.btnAddBookmark:
			popupAddBookmark("http://www.google.com.hk");
			break;
		case R.id.btnMenu:
			displayRightMenu();
			break;
		default:
			break;
		}
	}
	

	public void browserWebsite(String url) {
		menu.showContent();
		if (mainViewFragment == null) {
			mainViewFragment = mainViewFragment.newInstance();
			mainViewFragment.init(url);
			addFragment(mainViewFragment);
		} else {
			mainViewFragment.updateUrl(url);
		}
	}
	
	private void toBackPage() {
		if (mainViewFragment != null) {
			if(!mainViewFragment.goBack())
			{
				popFragment(mainViewFragment);
				mainViewFragment = null;
			}
		}
	}
	private void toForwardsPage() {
		if (mainViewFragment != null) {
			mainViewFragment.goForwards();
		}
	}
	private void refreshWebview() {
		if (mainViewFragment != null) {
			mainViewFragment.reload();
		}
	}
	
	public void parseSearchResult(Message msg){
		Bundle bundle = msg.getData();  
		String[] title = bundle.getStringArray("title");  
		String[] id = bundle.getStringArray("id");  
		int[] duration = bundle.getIntArray("duration"); 
		int[] viewCount = bundle.getIntArray("viewCount"); 
		
		int count = id.length;
		String[] youtubePhoto = new String[count];
		String[] titleShort = new String[count];
		
		mylist = new ArrayList<HashMap<String, Object>>(); 

		for(int i=0;i<title.length;i++){
			youtubePhoto[i]= "http://i.ytimg.com/vi/"+id[i]+"/2.jpg";
			URL url = null;
			//2013
			if(title[i]==null){
				continue;
			}
			
			if (title[i].length() > 40){
				titleShort[i] = title[i].substring(0, 35) + "...";
			}
			
			try {
				url = new URL(youtubePhoto[i]);
			} catch (MalformedURLException e) {
				Log.e("Youtube", "MalformedURLException:" + e.toString());
			}
			Log.i("ViewCount", "ViewCount"+ viewCount[i]);
			HashMap<String, Object> map = new HashMap<String, Object>();  
			map.put("youtubeId", id[i]);
			map.put("youtubeTitleShort", titleShort[i]);  
			map.put("youtubeTitleLong", title[i]);  
			map.put("youtubeImageView", youtubePhoto[i]);  
			map.put("youtubeDuration", getDuration(duration[i]));
			Log.e("XXXXX","parseSearchResult->"+titleShort[i]);
//			String viewText = getResources().getString(R.string.youtube_title_no_of_views);
//			map.put("youtubeViewCount", getViewCountFormat(viewCount[i]) + "  " + viewText); 
//			mylist.add(map);
//			addFlowChildren(map);
		}		
	}	
	
	
	/**
	 * 
	 * @return whether store account information successful or not
	 */
	public void retriveAccountInformation() {

		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(MeepYoutubeActivity.this, ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					YouTubeUtility.printLogcatDebugMessage("AccountManager is NULL");
					handler.sendMessage(YouTubeUtility.generateAlertMessageObject());
					return;
				}

				Account account = accountManager.getLastLoggedInAccountBlocking();
				if (account == null) {
					YouTubeUtility.printLogcatDebugMessage("Account is NULL");
					handler.sendMessage(YouTubeUtility.generateAlertMessageObject());
					return;
				}
				// store account information to preference
				YouTubeUtility.setAccountInformation(getApplicationContext(), account);
				YouTubeUtility.printLogcatDebugMessage(account.toJson());
				// init recommendation list in right menu
				handler.sendEmptyMessage(Consts.MESSAGE_WHAT_GET_ALL_RECOMMENDATIONS);
			}

		});
	}

	

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Consts.MESSAGE_WHAT_ALERT_MESSAGE_CONTENT:
				Log.e("XXXXX","MESSAGE_WHAT_ALERT_MESSAGE_CONTENT");
				alertAccessDenied(msg.getData().getInt(Consts.BUNDLE_KEY_ALERT_MESSAGE_CONTENT));
				break;
			case Consts.MESSAGE_WHAT_GET_ALL_RECOMMENDATIONS:
				Log.e("XXXXX","MESSAGE_WHAT_GET_ALL_RECOMMENDATIONS");
				rightMenuFragment.initRecommendationList();
//				startGetRecommendationService();
				break;

			default:
				break;
			}
		}
	};

	
	/**
	 * Alert user cannot access browser
	 * 
	 * @param resId
	 *            string recource to display
	 */
	public void alertAccessDenied(final int resId) {
		CommonPopup popup = new CommonPopup(MeepYoutubeActivity.this, R.string.notice, resId);
		popup.blockBackButton();
		popup.setOnClickOkButtonListener(new OnClickOkButtonListener() {
			@Override
			public void onClickOk() {
				if(resId == R.string.nonetwork)
				{
					YouTubeUtility.openWifiSettings(MeepYoutubeActivity.this);
				}
				finish();
			}
		});
		if (resId == R.string.nonetwork) {
			popup.setOnClickCloseButtonListener(new OnClickCloseButtonListener() {
				@Override
				public void onClickClose() {
					MeepYoutubeActivity.this.finish();
				}
			});
			popup.setEnableCloseButton(true);
		}
		popup.show();
	}

	public void getPermission() {
		PermissionManager permissionManager = (PermissionManager) ServiceManager.getService(MeepYoutubeActivity.this, ServiceManager.PERMISSION_SERVICE);
		// permissionManager.isBadword(user, word)

	}

	public void initRecentlyView(){
		try {
			recentlyview = loadRecentlyView();		
			if (recentlyview.length() > 0){
				videoId = recentlyview.substring(3, recentlyview.lastIndexOf("youtubeCentralTitle"));
				String title = recentlyview.substring(recentlyview.lastIndexOf("youtubeCentralTitle:")+20, recentlyview.lastIndexOf("youtubeViewCount:"));

				firstFrag.setTitle(title);
				firstFrag.setVideoId(videoId);
				
				Log.d("Youtube recently", "recently view: "+ recentlyview);
			    Bitmap cachedImage = null;
			    try {
			    	cachedImage = imageLoader.loadImage( "http://i.ytimg.com/vi/"+videoId+"/0.jpg", new ImageLoadedListener() {	
			    		public void imageLoaded(Bitmap imageBitmap) {
			    				firstFrag.setPreviewImage(imageBitmap);
			    			}
			    		});
			    } catch (MalformedURLException e) {
			    	Log.e("Youtube recently", "Bad remote image URL: " + e.toString());
			    }
			    
			    if( cachedImage != null ) {
			    	firstFrag.setPreviewImage(cachedImage);
			    }else{
			    	//image.setImageResource(R.drawable.icon);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
}




