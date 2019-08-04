package com.oregonscientific.meep.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout.LayoutParams;
import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.app.AppManager.OnApkListChangeListener;
import com.oregonscientific.meep.database.table.MeepDbCommunicationCtrl;
import com.oregonscientific.meep.database.table.TableAppsCategory;
import com.oregonscientific.meep.global.Global;

import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.notification.NotificationMessage;
import com.oregonscientific.meep.tool.ImageDownloader;

public class FullListViewActivity extends Activity{

	private static final String TAG = "FullListViewActivity";
	
	//2013-3-20 -Amy- Use PopupWindow replace RingMenu
	//RingMenu ringMenu ;
	private ImageView ring_delete;
//	private ImageView ring_menu_top_left_two;
//	private ImageView ring_menu_right_one;
//	private ImageView ring_menu_left_one;
	
	private ViewGroup mViewGroup = null;
	private NotificationMessage notification;
	private Context mContext;
	
	private final String BASE_STORAGE_DIR = "/data/home/";
	private final String PATH_ICON_DIR = BASE_STORAGE_DIR + "app/icon/";
	private final String PATH_SMALL_ICON_DIR = BASE_STORAGE_DIR + "app/icon_s/";
	private final String PATH_LARGE_ICON_DIR = BASE_STORAGE_DIR + "app/icon_l/";
	private final String PATH_LARGE_DIM_ICON_DIR = BASE_STORAGE_DIR + "app/icon_ld/";
	private final static String CACHE_DIR = "ebook";

	public static final String STATE_FRAG_GRID = "stackFragGird";
	public static final String STATE_FRAG_SNACK = "stackFragSnack";
	private static final String STATE_TAG = "tag";

	//2013-3-20 - Amy - update shelves use GridView
	private List<OsListViewItem> mListViewItemList = null;
	//2013-3-20 - Amy - update shelves use GridView
	//LinearLayout mLayout = null;
	//ScrollView mScrollView = null;
	private RelativeLayout mOptionLayout = null;
	private RelativeLayout mRenameLayout = null;
	//private AbsoluteLayout mMainLayout = null;
	private EditText mTxtRename = null;
	
	private ImageView mSnackIcon = null;
	
	private Button mBtnConfirm = null;

	private EditMode mEditMode = EditMode.VIEW; 
	private Handler mHandlerReadImg = null;
	private Thread mThread = null;
	private GestureDetector mGuestureDetector = null;
	private GestureDetector mItemGuestureDetector = null;
	
	private View mSelectedView = null;
	
	int mCorX = 0;
	int mCorY = 0;
	
	private float mInitSpan = 0;
	
	
	private MeepDbCommunicationCtrl mMeepDbCommunicationCtrl = null;
	private BroadcastReceiver mMsgReciver = null;
	
	private String[] mGameArray = null;
	private String[] mBlockArray = null;
	
	//2013-3-20 - Amy - update shelves use GridView
	private GridView myApps = null;
	private ImageDownloader imageDownloader;
	private Bitmap frameImage;

	private AppManager mAppManager;
	private FragmentManager mFragmentManager;
	private String mCurrentTag;
	private Map<String, Fragment> mFragments;
	private SharedPreferences sp;
		
	public enum EditMode {
		VIEW, DELETE, EDIT, OPTION_SHOWN,
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frags);
		ServiceController.getAccount(this);
		mFragments = new HashMap<String, Fragment>();
		mFragmentManager = getFragmentManager();
		sp = getSharedPreferences("MeepApp", Context.MODE_PRIVATE);
		mCurrentTag = sp.getString(STATE_TAG, STATE_FRAG_GRID);
		
		mAppManager = new AppManager(this);
		mListViewItemList = new ArrayList<OsListViewItem>();
		showDetails(mCurrentTag);

	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
/*	@Override
	protected void onResume() {
		super.onResume();
		
		packageManager = getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, PackageManager.GET_INTENT_FILTERS);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));  
        
        for(int i=0; i<appList.size(); i++) {
        	String packageName = appList.get(i).activityInfo.packageName;
        	
        	if (!uninstallPackageName.equals("") && packageName.contains(uninstallPackageName)){
        		Log.d("onresume", "onresume: " + packageName);
        		Log.d("onresume", "onresume:XXXXXXXXXXXXXXXXXXXXXXXX");
        		uninstalledPackage = false;
        	}
        }
		
        if (uninstalledPackage && !uninstallItemName.equals("")){
        	deleteItemFile(PATH_SMALL_ICON_DIR + uninstallItemName + ".png");
			deleteItemFile(PATH_LARGE_ICON_DIR + uninstallItemName+ ".png");
			deleteItemFile(PATH_LARGE_DIM_ICON_DIR + uninstallItemName+ ".png");
			//redrawListView();
			uninstalledPackage = true;
			uninstallPackageName = "";
			uninstallItemName = "";
		}
        uninstalledPackage = true;
        
		mMeepDbCommunicationCtrl.sendDBQuery("SELECT * FROM " + TableAppsCategory.S_TABLE_NAME, MeepAppMessage.OPCODE_DATABASE_GET_APPS_CATEGORY);
	}*/
	public void showDetails(String tag) {
		// Instantiate a new fragment.
		/*if (mCurrentTag.equals(tag)) {
			return;
		}*/
		mCurrentTag = tag;
/*		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);*/
		Fragment fragment = mFragments.get(tag);
	    if (fragment == null) {
	    	if (STATE_FRAG_SNACK.equals(tag)) {
	    		fragment = SnakeFragment.newInstance(tag);
	    	} else if (STATE_FRAG_GRID.equals(tag)) {
	    		fragment = GridViewFragment.newInstance(tag);
	    	}
	    	//mFragmentManager.putFragment(fragment.getArguments(), tag, fragment);
	    	mFragments.put(tag, fragment);
	    }
	    // Add the fragment to the activity, pushing this transaction
	    // on to the back stack.
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	   // ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out); 
	    ft.replace(R.id.layout_frags, fragment);
	   // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    //ft.addToBackStack(null);
	    ft.commit();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		sp.edit().putString(STATE_TAG, mCurrentTag).commit();
		//unregisterReceiver(mMsgReciver);
		ServiceManager.unbindServices(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString(STATE_TAG, mCurrentTag);
	}

	

}
