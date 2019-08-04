package com.mogoo.market.ui;

import java.util.ArrayList;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Gallery.LayoutParams;

import com.mogoo.market.adapter.HotAdapter;
import com.mogoo.market.adapter.HotCursorAdapter;
import com.mogoo.market.database.ApkListDaoImpl;
import com.mogoo.market.database.ApkListSQLTable;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.widget.TitleBar;

public class ChildCateActivity extends BaseLoadableListActivityWithViewAndHeader {
	static final String TAG = "ChildCateActivity";
    public static final String EXTRA_REQUEST_ID = "request_id";
    public static final String EXTRA_REQUEST_TYPE = "request_type";
    public static final String EXTRA_REQUEST_TITLE = "request_title";
    
	static final String TOPID = "topid";
	static final String PID = "pid";
	
	static final boolean DEBUG = true;
	
	static final int TYPE_TOPIC_INFO = 0;
	static final int TYPE_CHILD_CATE_INFO = 1;
	
	private PaginateListView_1<HotApp> mHotListView;
	private ListFooterView mFooterView;
	
	private HotCursorAdapter<HotApp> mHotAdapter;
	
	private int mRequestType;
	private int mId = -1;
	private String mTitleText = "";
	
	private String mRequestUrl = "";
	
	private RefreshReceiver mRefreshReceiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent == null) {
			finish();
		}
		
		mRequestType = intent.getIntExtra(EXTRA_REQUEST_TYPE, TYPE_TOPIC_INFO);
		mTitleText = intent.getStringExtra(EXTRA_REQUEST_TITLE);
		mId = intent.getIntExtra(EXTRA_REQUEST_ID, 0);
		Map<String, String> baseParamPair = HttpUrls.getBaseParamPair();
		if(mRequestType == TYPE_CHILD_CATE_INFO) {
			baseParamPair.put(PID, String.valueOf(mId));
			mRequestUrl = HttpUrls.createBaseUrlWithExtendPairs(
					HttpUrls.URL_CHILD_CATE_INFO, baseParamPair);
		}else {
			baseParamPair.put(TOPID, String.valueOf(mId));
			mRequestUrl = HttpUrls.createBaseUrlWithExtendPairs(
					HttpUrls.URL_TOPIC_INFO, baseParamPair);
		}
		ensureUi();
		registerRefreshReceiver();
//		Debug.startMethodTracing("lxr@ChildCateActitivty");
	}
	
	@Override
	protected void onResume() {
		refreshView();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		ApkListDaoImpl.getInstance(this, ApkListSQLTable.TABLE_CHILD_CATE).clearAllBean();
//		Debug.stopMethodTracing();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent backIntent = ChildCateActivity.this.getBackIntent();
			MarketGroupActivity.getInstance().onBackPressed(backIntent);
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 当下载文件删除时，通知界面刷新
	 */
	private void registerRefreshReceiver() {
		IntentFilter filter = new IntentFilter(ManagerActivity.BROADCAST_ACTION_FILE_DELETE);
		mRefreshReceiver = new RefreshReceiver();
		registerReceiver(mRefreshReceiver, filter);
	}
	
	private void setUpTitleBar() {
		TitleBar titleBar = new TitleBar(this);
		titleBar.setMidText(mTitleText);
		titleBar.rightBtn.setVisibility(View.INVISIBLE);
		titleBar.leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent backIntent = ChildCateActivity.this.getBackIntent();
				MarketGroupActivity.getInstance().onBackPressed(backIntent);
			}
		});
		setBaseTopLayout(titleBar);
	}
	
	private void ensureUi() {
		setUpTitleBar();
		ensureUiListView();
	}
	
	private void ensureUiListView() {
		initListViewAdapter();
		initFooterViewAndEmptyView(mHotListView,mHotAdapter);
		initPaginateListView(mHotListView);
		mHotListView.setXmlResultCallback(new HotApp.HotAppListCallback());
		mHotListView.doFirsetQuery(mRequestUrl);
	}
	
	/**
	 * initialize the lists
	 * @author lcq-motone 2012-6-7
	 */
	private void initListViewAdapter() {
		IBeanDao<HotApp> childCateDao = DaoFactory.getChildCateDao(this);
		
		mHotAdapter = new HotCursorAdapter<HotApp>(this, childCateDao.getAllBean());
		mHotListView = new PaginateListView_1<HotApp>(this, mHotAdapter, childCateDao, mRequestUrl);
	}
	
	/**
	 * initialize the foot view and empty view
	 * @author lcq-motone 2012-6-7
	 */
	@SuppressWarnings("rawtypes")
	private void initFooterViewAndEmptyView(PaginateListView_1 listView, CursorAdapter adapter) {
		super.setBaseCenterLayout(listView);
		
		mFooterView = new ListFooterView(this, adapter);
		listView.addFooterView(mFooterView);
		listView.setAdapter(adapter);
		
		CommonEmptyView emptyView = new CommonEmptyView(this, listView);
		ViewGroup layout = (ViewGroup) listView.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		listView.setEmptyView(emptyView);
		
	}
	
	@SuppressWarnings("rawtypes")
	private void initPaginateListView(PaginateListView_1 listview) {
		listview.setCacheColorHint(Color.WHITE);
		listview.setVerticalScrollBarEnabled(false);
	}
	
	public class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ManagerActivity.BROADCAST_ACTION_FILE_DELETE)){
				refreshView();
			}
		}
		
	}
	/**
	 * when you delete a file in the manageActivity,we should update the view
	 */
	private void refreshView(){
		if(mHotAdapter !=null){
			mHotAdapter.notifyDataSetChanged();
		}
	}
}
