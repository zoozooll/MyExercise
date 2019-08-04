package com.mogoo.market.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.ibetask.sales.SalesStatistics;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.IBEManager;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.widget.ToolBar;

/** 
 * 使用说明：
 * 1.进入下一级：goToNextActivity(Intent childIntent)
 * 2.返回上一级
 * MusicStoreActivity msa = MusicStoreActivity.getInstance();
 * msa.setupTabIntent(MusicStoreActivity.getCurrentTab(), NowActivity.this.getBackIntent());  //backIntent从上一级获取到的上一级的Intent
 * msa.setupTabView(true);
 * 二级以上界面onBackPressed监听事件需要调用MusicStoreActivity的：
 * MarketGroupActivity.getInstance().onBackPressed(Intent backIntent);
 */
public class MarketGroupActivity extends ActivityGroup implements ToolBar.OnTabClickListener
{
	public static final String ACTION_NECESSARY = "com.mogoo.market.action.NECESSARY";
	public static final String ACTION_RECOMMEND = "com.mogoo.market.action.RECOMMEND";
	public static final String ACTION_GAMECENTER = "com.mogoo.market.action.GAMECENTER";
	
	public static final String EXTRA_FINISH_SELF = "extra_finish_self";
	public static final String EXTRA_CURRENT_NUM = "tabnum" ;
	public static final String EXTRA_BACK_INTENT = "backintent" ;
	
	/**
	 * TAB ID
	 */
	public static final int TAB_MAIN = 0 ;
	public static final int TAB_CATE = 1 ;
//	public static final int TAB_TOP = 2 ;
	public static final int TAB_SEARCH = 2 ;
	public static final int TAB_MANAGER = 3 ;
	
	public LinearLayout container = null;
	public LinearLayout mConnectTip;
	//保存2级以上界面的Intent，3级界面返回2级界面需要通过setupTabIntent方法设置2级界面的tabIntent
	private HashMap<Integer, Intent> tabIntents = new HashMap<Integer, Intent>();
	private ToolBar toolBar = null;
	private static int currentTab = TAB_MAIN;
	
	private LocalActivityManager activityManager = null;
	
	private static MarketGroupActivity instance = null;
	private ArrayList<HotApp> mResultArrayList;
	private Intent mFromIntent = null;
	
	public static MarketGroupActivity getInstance()
	{
		if(instance == null)
		{
			instance = new MarketGroupActivity();
		}
		return instance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		instance = this;
        setContentView(R.layout.market_group_activity);
        mResultArrayList = (ArrayList<HotApp>) getIntent().getSerializableExtra("result");
        init();
        
		IntentFilter filter = new IntentFilter(); 
	    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    registerReceiver(myReceiver, filter); 
	}
	
	
	@Override
    protected void onStop() 
    {
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() 
    {
    	// 清空所有数据库中列表的缓存.
    	DaoFactory.clearDataBase(this);
    	//暂停下载任务
    	pauseDownload(getApplicationContext());
    	if(myReceiver!=null){
    		unregisterReceiver(myReceiver);
    	}
    	super.onDestroy();
    }
    
    /**
     * csq:本来想做到按Home键，在次进入也能再次更新一下软件更新数据，每次onResume都重新onUploadApkInfo获取一遍，
     * 但下载完成频繁弹出安装软件界面，完成后也会执行onResume，会频繁onUploadApkInfo，所以得加个时间间隔5分钟。
     * 判断不了是按Home键返回还是软件安装界面返回，暂时这样处理了。
     */
    private long LastUploadTime = 0;
    @Override
    protected void onResume() 
    {
    	long currentMil = System.currentTimeMillis();
    	if((currentMil - LastUploadTime) > 300000) 
		{
    		LastUploadTime = currentMil;
    		MarketApplication.getInstance().onUploadApkInfo();  //上传安装的软件信息以获取软件更新列表
		}
    	super.onResume();
    }
    
    /**
     * 退出商城时，暂停所有未下载完的任务
     */
    private void pauseDownload(final Context context) 
    {
    	DownPrefsUtil prefs = DownPrefsUtil.getInstance(instance);
    	HashMap<String, String> allDownloads = DownPrefsUtil.getAll(instance);
    	Set<String> keys = allDownloads.keySet();
    	MogooDownloadManager downloadManager = new MogooDownloadManager(context);
    	for(String key : keys)
    	{
    		String value = prefs.getPrefsValue(key, "");
        	String[] result = prefs.parseResponse(value);
        	if(result.length>1)  //有下载
        	{
        		if (TextUtils.equals("true", result[DownPrefsUtil.INFO_DOWN_LOAD_STATUS])) //下载中
        		{
        			ToolsUtils.pauseDownload(instance, downloadManager, key, result[DownPrefsUtil.INFO_APK_ID]);
    			}
        	}
    	}
	}
    
	@Override
	protected void onNewIntent(Intent intent) {
		mFromIntent = intent;
		if(intent != null) {
			String action = intent.getAction();
			if(action != null && (action.equals(ACTION_NECESSARY) || action.equals(ACTION_RECOMMEND))) {
		        currentTab = TAB_MAIN;
				setCurrentTab(true);
			} else if(action != null && action.equals(ACTION_GAMECENTER)) {
		        currentTab = TAB_CATE;
				setCurrentTab(true);
			} else {
				boolean isFinishSelf = intent.getBooleanExtra(EXTRA_FINISH_SELF, false);
				if(isFinishSelf) {
					MarketGroupActivity.this.finish();
				}
			}
		}
		super.onNewIntent(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return getCurrentActivity().onKeyDown(keyCode, event);
	}

	private void init()
    {
    	activityManager = getLocalActivityManager();
    	
    	container = (LinearLayout)findViewById(R.id.idContainer);
    	mConnectTip=(LinearLayout)findViewById(R.id.connectTip);
        toolBar = (ToolBar)findViewById(R.id.idToolBar);
        toolBar.setOnTabClickListener(this);
        
        setupToolBar();
        
//        currentTab = getIntent().getIntExtra(EXTRA_CURRENT_NUM, 0);
        mFromIntent = getIntent();
        
        if(mFromIntent != null) {
        	String action = mFromIntent.getAction();
        	if(action != null && (action.equals(ACTION_NECESSARY) || action.equals(ACTION_RECOMMEND))) {
        		currentTab = TAB_MAIN;
        	}else if(action != null && action.equals(ACTION_GAMECENTER)) {
        		currentTab = TAB_CATE;
        	}else {
        		 currentTab = mFromIntent.getIntExtra(EXTRA_CURRENT_NUM, 0);
        	}
        }
        setCurrentTab(false);
    }
    
    /**
     * 初始化选项卡 
     */
    private void setupToolBar()
    {
        String hotText = getResources().getString(R.string.tab_main);
        String cateText = getResources().getString(R.string.tab_cate);
		// String topText = getResources().getString(R.string.tab_top);
        String searchText = getResources().getString(R.string.tab_search);
        String managerText = getResources().getString(R.string.tab_manager);
        
        toolBar.setTabMember(TAB_MAIN, hotText,  R.drawable.tab_home_normal, R.drawable.tab_home_pressed);
        toolBar.setTabMember(TAB_CATE, cateText,  R.drawable.tab_cate_normal, R.drawable.tab_cate_pressed);
        // toolBar.setTabMember(TAB_TOP, topText, R.drawable.tab_top_normal, R.drawable.tab_top_pressed);
        toolBar.setTabMember(TAB_SEARCH, searchText, R.drawable.tab_search_normal, R.drawable.tab_search_pressed);
        toolBar.setTabMember(TAB_MANAGER, managerText,  R.drawable.tab_mgr_normal, R.drawable.tab_mgr_pressed);
    }

	public void onTabClick(int tabId) 
	{
		// TODO Auto-generated method stub
		currentTab = tabId;
        setCurrentTab(false);
	}
	
	//设置当前切换卡
    private void setCurrentTab(boolean isDispatchDestroy)
    {
        toolBar.setActiveTabId(currentTab);
        
        if(currentTab == -1)
        	return;
        
        setupTabView(isDispatchDestroy);
    }
    
    //boolean destroyNowRunningActivity切换选项卡时上一个选项卡是否销毁,现在设置为false不销毁
    public void setupTabView(boolean destroyNowRunningActivity) 
    {
    	Intent intent = new Intent();
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    	View view = null;
    	
    	if(destroyNowRunningActivity)
    	{
    		activityManager.dispatchDestroy(true);
    	}
    	else
    	{
    		activityManager.dispatchPause(false);
    	}
    	
    	switch (currentTab) 
    	{
	    	case TAB_MAIN:
	    		if(mFromIntent != null && mFromIntent.getAction() != null
						&& mFromIntent.getAction().equals(ACTION_NECESSARY)) {
	    			intent.setClass(this, MainActivity.class);
	    			intent.putExtra("result", mResultArrayList);
					intent.putExtra(MainActivity.EXTRA_LISTVIEW_INDEX, MainActivity.SORT_NES_NUMBER);
					setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("main",intent).getDecorView();
	    		} else if(mFromIntent != null && mFromIntent.getAction() != null
	    				&& mFromIntent.getAction().equals(ACTION_RECOMMEND)) {
	    			intent.setClass(this, MainActivity.class);
	    			intent.putExtra("result", mResultArrayList);
	    			intent.putExtra(MainActivity.EXTRA_LISTVIEW_INDEX, MainActivity.SORT_HOT_NUMBER);
	    			setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("main",intent).getDecorView();
	    		} else if(tabIntents.containsKey(TAB_MAIN) && tabIntents.get(TAB_MAIN)!=null) {
	    			//二级界面，"child"可以任意，只是一个标识
	    			view = activityManager.startActivity("main",tabIntents.get(TAB_MAIN)).getDecorView();
	    		} else {   //一级界面，"hot"可以任意，只是一个标识
	    			intent.setClass(this, MainActivity.class);
	    			intent.putExtra("result", mResultArrayList);
	    			setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("main",intent).getDecorView();
	    		}
	    		break;
			case TAB_CATE:
				if(mFromIntent != null && mFromIntent.getAction() != null
						&& mFromIntent.getAction().equals(ACTION_GAMECENTER)) {
	    			intent.setClass(this, CateActivity.class);
					intent.putExtra(MainActivity.EXTRA_LISTVIEW_INDEX, CateActivity.SORT_METHOD_GAME);
					setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("cate",intent).getDecorView();
	    		} else if(tabIntents.containsKey(TAB_CATE) && tabIntents.get(TAB_CATE)!=null){
					view = activityManager.startActivity("cate",tabIntents.get(TAB_CATE)).getDecorView();
	    		}else{
	    			intent.setClass(this, CateActivity.class);
	    			setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("cate",intent).getDecorView();
	    		}
				break;
			/*			
 			case TAB_TOP:
				if(tabIntents.containsKey(TAB_TOP) && tabIntents.get(TAB_TOP)!=null)
	    		{
					view = activityManager.startActivity("top",tabIntents.get(TAB_TOP)).getDecorView();
	    		}
	    		else
	    		{
	    			intent.setClass(this, TopActivity.class);
	    			setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("top",intent).getDecorView();
	    		}
				break;
			*/
			case TAB_SEARCH:
				if(tabIntents.containsKey(TAB_SEARCH) && tabIntents.get(TAB_SEARCH)!=null)
	    		{
					view = activityManager.startActivity("search",tabIntents.get(TAB_SEARCH)).getDecorView();
	    		}
	    		else
	    		{
	    			intent.setClass(this, SearchActivity.class);
	    			setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("search",intent).getDecorView();
	    		}
				break;
			case TAB_MANAGER:
				if(tabIntents.containsKey(TAB_MANAGER) && tabIntents.get(TAB_MANAGER)!=null)
	    		{
					view = activityManager.startActivity("manager",tabIntents.get(TAB_MANAGER)).getDecorView();
	    		}
	    		else
	    		{
	    			intent.setClass(this, ManagerActivity.class);
	    			setupTabIntent(currentTab , intent);
	    			view = activityManager.startActivity("manager",intent).getDecorView();
	    		}
				break;
			default:
				break;
		}
        container.removeAllViews();
        container.addView(view);
        LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.height = LayoutParams.FILL_PARENT;
        params.width = LayoutParams.FILL_PARENT;
        view.setLayoutParams(params);
    }
    
    //设置子界面
    public void setupTabChildView(Intent intent) 
    {
    	// activityManager.dispatchDestroy(true);    //i界面进入i+1界面，销毁i界面
    	
    	setupTabIntent(currentTab , intent);
    	
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	View view = activityManager.startActivity("child",intent).getDecorView();
    	container.removeAllViews();
    	container.addView(view);
        LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.height = LayoutParams.FILL_PARENT;
        params.width = LayoutParams.FILL_PARENT;
        view.setLayoutParams(params);
    }
    
    public void setupTabIntent(int index , Intent intent) 
    {
    	tabIntents.put(index, intent);
    }
    
    //获得当前呈现的Activity的Intent，如果是一级界面就返回null
    public Intent getNowTabIntent()   
    {
    	return tabIntents.get(currentTab);
    }
    
    public static int getCurrentTab()
    {
    	return currentTab;
    }
    
	public void onBackPressed(Intent backintent) 
	{
		if(backintent != null)  //现在处于二级以上界面
		{
			setupTabIntent(currentTab, backintent);
			setupTabView(false);
		}
		else   //现在处于一级界面，退出
		{
			this.finish();
		}
	}
	
	/**
	 * 进入下一级界面
	 * @param childIntent 下一级界面的Intent
	 */
	public static void goToNextActivity(Intent childIntent) 
	{
		childIntent.putExtra(EXTRA_BACK_INTENT, instance.getNowTabIntent());
		instance.setupTabChildView(childIntent);
		instance.setupTabIntent(MarketGroupActivity.getCurrentTab(),childIntent);
		instance.setupTabView(false);
	}
	
	private BroadcastReceiver myReceiver =new BroadcastReceiver() {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	    ConnectivityManager connectMgr=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
	    NetworkInfo mobileNetInfo=connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    NetworkInfo wifiNetInfo=connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    if(!mobileNetInfo.isConnected()&&!wifiNetInfo.isConnected()){
	    	mConnectTip.setVisibility(View.VISIBLE);
	    }else{
	    	mConnectTip.setVisibility(View.GONE);
	    	// 上传销量统计的信息
			// add by fdl:不是IBE的环境下，自己上传销量信息
			if (!IBEManager.isIBEExist())
			{
				new Thread(){
					public void run()
					{
						SalesStatistics.getInstance(getApplicationContext()).uploadSalesStatistics();
					}
				}.start();
			}
	    }
	    
}
};
}