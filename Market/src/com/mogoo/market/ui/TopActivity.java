package com.mogoo.market.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;

import com.mogoo.market.R;
import com.mogoo.market.adapter.HotCursorAdapter;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.widget.SegmentedButton;
import com.mogoo.market.widget.SegmentedButton.OnTouchListenerSegmentedButton;
import com.mogoo.parser.XmlResultCallback;

/**
 * 排行
 */
public class TopActivity extends BaseLoadableListActivityWithViewAndHeader {
	static final String TOP_RANKING_REQUEST_URL = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_TOP_HOT_LIST);
	static final String APPS_RANKING_REQUEST_URL = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_TOP_APPS_LIST);
	static final String GAME_RANKING_REQUEST_URL = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_TOP_GAME_LIST);
	
	private static final int SORT_HOT_NUMBER = 0;
	private static final int SORT_APPS_NUMBER = 1;
	private static final int SORT_GAME_NUMBER = 2;
	
	private PaginateListView_1<HotApp> mHotListView;
	private PaginateListView_1<HotApp> mAppsListView;
	private PaginateListView_1<HotApp> mGameListView;
	private HotCursorAdapter<HotApp> mHotAdapter;
	private HotCursorAdapter<HotApp> mAppsAdapter;
	private HotCursorAdapter<HotApp> mGameAdapter;

	private RefreshReceiver mRefreshReceiver;
	
	private int mSortMethod;
	private boolean mInit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ensureUi();
		registerRefreshReceiver();
	}

	@Override
	protected void onStart() {
		super.onStart();
	    mHotListView.setXmlResultCallback(new HotApp.HotAppListCallback());
	}
	
	@Override
	protected void onResume() {
		refreshAllListView();
		super.onResume();
	}
	
	@Override
	public void onAttachedToWindow() {
		if(!mInit) {
			mInit = true;
			return;
		}
		refreshAllListView();
		super.onAttachedToWindow();
	}
	
	@Override
	protected void onDestroy() {
		if(mHotAdapter.getCursor() != null && !mHotAdapter.getCursor().isClosed())
			mHotAdapter.getCursor().close();
		if(mAppsAdapter.getCursor() != null && !mAppsAdapter.getCursor().isClosed())
			mAppsAdapter.getCursor().close();
		if(mGameAdapter.getCursor() != null && !mGameAdapter.getCursor().isClosed())
			mGameAdapter.getCursor().close();
		super.onDestroy();
	}
	
	private long exitTime = 0;
	private CommonEmptyView emptyView;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if((System.currentTimeMillis() - exitTime) > 5000) {
				ToolsUtils.showExitToast(getApplicationContext());
				exitTime = System.currentTimeMillis();
			}else {
				Intent intent = new Intent(TopActivity.this, MarketGroupActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(MarketGroupActivity.EXTRA_FINISH_SELF, true);
				startActivity(intent);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 当下载文件删除时，通知界面刷新
	 */
	private void registerRefreshReceiver() {
		IntentFilter filter = new IntentFilter(ManagerActivity.BROADCAST_ACTION_FILE_DELETE);
		mRefreshReceiver = new RefreshReceiver();
		registerReceiver(mRefreshReceiver, filter);
	}
	
	/**
	 * initialize the lists
	 * @author lcq-motone 2012-6-7
	 */
	private void initListViewAdapter() {
		IBeanDao<HotApp> topRankingDao = DaoFactory.getTopRankingDao(this);
		IBeanDao<HotApp> appsRankingDao = DaoFactory.getAppsRankingDao(this);
		IBeanDao<HotApp> gameRankingDao = DaoFactory.getGameRankingDao(this);
		
		mHotAdapter = new HotCursorAdapter<HotApp>(this, topRankingDao.getAllBean());
		mHotListView = new PaginateListView_1<HotApp>(this, mHotAdapter, topRankingDao, TOP_RANKING_REQUEST_URL);
		
		mAppsAdapter = new HotCursorAdapter<HotApp>(this, appsRankingDao.getAllBean());
		mAppsListView = new PaginateListView_1<HotApp>(this, mAppsAdapter, appsRankingDao, APPS_RANKING_REQUEST_URL);
		
		mGameAdapter = new HotCursorAdapter<HotApp>(this, gameRankingDao.getAllBean());
		mGameListView = new PaginateListView_1<HotApp>(this, mGameAdapter, gameRankingDao, GAME_RANKING_REQUEST_URL);
		
		initPaginateListView(mHotListView, mHotAdapter, new HotApp.HotAppListCallback());
		initPaginateListView(mAppsListView, mAppsAdapter, new HotApp.HotAppListCallback());
		initPaginateListView(mGameListView, mGameAdapter, new HotApp.HotAppListCallback());

		ensureUiListView(mSortMethod);
	}

	private void ensureUi() {
		SegmentedButton buttons = getHeaderButton();
		buttons.clearButtons();
		buttons.addButtons(getString(R.string.top_activity_btn_hot),
				getString(R.string.top_activity_btn_apps),
				getString(R.string.top_activity_btn_game));
		buttons.setOnTouchListener(new OnTouchListenerSegmentedButton() {

			public void onTouch(int index) {
				mSortMethod = index;
				ensureUiListView(mSortMethod);
			}
		});
		initListViewAdapter();
	}
	
	private void ensureUiListView(int index) {
		if(mSortMethod == SORT_HOT_NUMBER) {
			setHotListView();
		}else if(mSortMethod == SORT_APPS_NUMBER) {
			setAppsListView();
		}else if(mSortMethod == SORT_GAME_NUMBER) {
			setGameListView();
		}
	}
	
	private void setHotListView() {
		mHotAdapter.notifyDataSetChanged();
		setBaseCenterLayout(mHotListView);
		
		addEmptyView(this, mHotListView);
		
		if(mHotAdapter.getCount() == 0) {
			mHotListView.doFirsetQuery(TOP_RANKING_REQUEST_URL);
		}
	}
	
	private void setGameListView() {
		mGameAdapter.notifyDataSetChanged();
		setBaseCenterLayout(mGameListView);
		
		addEmptyView(this, mGameListView);
		
		if(mGameAdapter.getCount() == 0) {
			mGameListView.doFirsetQuery(GAME_RANKING_REQUEST_URL);
		}
	}
	
	private void setAppsListView() {
		mAppsAdapter.notifyDataSetChanged();
		setBaseCenterLayout(mAppsListView);
		
		addEmptyView(this, mAppsListView);
		
		if(mAppsAdapter.getCount() == 0) {
			mAppsListView.doFirsetQuery(APPS_RANKING_REQUEST_URL);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void addEmptyView(Context context, PaginateListView_1 listview) {
		emptyView = new CommonEmptyView(this, listview);

		ViewGroup layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}
	
	@SuppressWarnings("rawtypes")
	private void initPaginateListView(PaginateListView_1 listview, BaseAdapter adapter,
			XmlResultCallback xmlResultCallback) {
		listview.setCacheColorHint(Color.WHITE);
		listview.setVerticalScrollBarEnabled(false);
		listview.setXmlResultCallback(xmlResultCallback);
		ListFooterView footerView = new ListFooterView(this, adapter);
		listview.addFooterView(footerView);
		listview.setAdapter(adapter);
	}
	
	/**
	 * when you delete a file in the manageActivity,we should update the view
	 */
	private void refreshAllListView(){
		// 本来定义的规则是CommonEmptyView在接受到DatasetChange消息后就显示加载中,
		// 但是现在要刷新，也调用NotifyDatasetChange，所以当作异常的情况处理好了
		if(emptyView != null) {
			emptyView.encountError();
		}
		if(mHotAdapter != null)
			mHotAdapter.notifyDataSetChanged();
		if(mAppsAdapter != null)	
			mAppsAdapter.notifyDataSetChanged();
		if(mGameAdapter != null)	
			mGameAdapter.notifyDataSetChanged();
	}
	
	public class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ManagerActivity.BROADCAST_ACTION_FILE_DELETE)){
				refreshAllListView();
			}
		}
	}
}
