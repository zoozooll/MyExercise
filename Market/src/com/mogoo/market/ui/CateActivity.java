package com.mogoo.market.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.LinearLayout;

import com.mogoo.market.R;
import com.mogoo.market.adapter.AppsPagerAdapter;
import com.mogoo.market.adapter.GamePagerAdapter;
import com.mogoo.market.adapter.ScrollingTabsAdapter;
import com.mogoo.market.adapter.TopicAdapter;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.Topic;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.viewpager.extensions.ScrollingTabsView;
import com.mogoo.market.viewpager.extensions.TabsAdapter;
import com.mogoo.market.widget.SegmentedButton;
import com.mogoo.market.widget.SegmentedButton.OnTouchListenerSegmentedButton;
import com.mogoo.parser.XmlResultCallback;

/**
 * 分类
 */
public class CateActivity extends BaseListActivity {
	public static final String TAG = "CateActivity";
	public static final boolean DEBUG = true;
	
	public static final String EXTRA_LISTVIEW_INDEX = "listview_index";
	
	public static final int SORT_METHOD_TOPIC = 0;
	public static final int SORT_METHOD_APPS = 1;
	public static final int SORT_METHOD_GAME = 2;
	
	private static final String TOPIC_REQUEST_URL = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_TOPIC_LIST);
	private static final String APPS_REQUEST_URL = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_APPS_LIST);
	private static final String GAME_REQUEST_URL = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_GAME_LIST);
	
	private ImageDownloader mImageLoader;
	private CursorAdapter mTopicAdapter;
	
	private PaginateListView_1<Topic> mTopicListView;
	
	private int mSortMethod = SORT_METHOD_APPS;
	
	private SegmentedButton mHeaderButton;
	
	private ScrollingTabsView mAppsSwipeyTabs;
	private TabsAdapter mAppsScrollingTabsAdapter;
	private ViewPager mAppsPager;
	private AppsPagerAdapter mAppsPagerAdapter;
	
	private ScrollingTabsView mGameSwipeyTabs;
	private TabsAdapter mGameScrollingTabsAdapter;
	private ViewPager mGamePager;
	private GamePagerAdapter mGamePagerAdapter;
	
	private boolean mInit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageLoader = ImageDownloader.getInstance(this);
		handleIntent();
		ensureUi();
	}
	
	@Override
	protected void onResume() {
		refreshAllListView();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		if(mTopicAdapter.getCursor() != null && !mTopicAdapter.getCursor().isClosed())
			mTopicAdapter.getCursor().close();
		if(mAppsPagerAdapter != null) {
			mAppsPagerAdapter.stop();
		}
		if(mGamePagerAdapter != null) {
			mGamePagerAdapter.stop();
		}
		super.onDestroy();
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onAttachedToWindow() {
		if(!mInit) {
			mInit = true;
			return;
		}
		if(mImageLoader == null) {
			mImageLoader = ImageDownloader.getInstance(this);
		}
		if(mSortMethod == SORT_METHOD_TOPIC) {
		}else {
		}
		refreshAllListView();
		super.onAttachedToWindow();
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
				Intent intent = new Intent(CateActivity.this, MarketGroupActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(MarketGroupActivity.EXTRA_FINISH_SELF, true);
				startActivity(intent);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理传送过来的intent
	 */
	private void handleIntent() {
		Intent fromIntent = getIntent();
		if(fromIntent != null) {
			mSortMethod = fromIntent.getIntExtra(EXTRA_LISTVIEW_INDEX, SORT_METHOD_APPS);
		}
	}
	
	private void ensureUi() {
		initListViewAdapter();
		initPager();
		
		setBaseTopLayout(R.layout.cate_top_layout);
		mHeaderButton = (SegmentedButton) findViewById(R.id.segmented);
		initHeaderButton();
		
		mHeaderButton.clearButtons();
		mHeaderButton.addButtons(getString(R.string.cate_activity_btn_topic), 
				getString(R.string.cate_activity_btn_apps), 
				getString(R.string.cate_activity_btn_game));
		mHeaderButton.setOnTouchListener(new OnTouchListenerSegmentedButton() {
			
			public void onTouch(int index) {
				mSortMethod = index;
				ensureUiListView(mSortMethod);
			}
		});
		mHeaderButton.setPushedButtonIndex(mSortMethod);
		ensureUiListView(mSortMethod);
	}
	
	/**
	 * 设置顶部3个按钮的宽度，在activitygroup中会出现width不全屏的情况，要手动设置
	 */
	private void initHeaderButton() {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHeaderButton.getLayoutParams();
		params.width = LayoutParams.FILL_PARENT;
		mHeaderButton.setLayoutParams(params);
	}
	
	private void initListViewAdapter() {
		IBeanDao<Topic> topicDao = DaoFactory.getTopicDao(this);
		
		mTopicAdapter = new TopicAdapter<Topic> (this, topicDao.getAllBean(), mImageLoader);
		mTopicListView = new PaginateListView_1<Topic>(this, mTopicAdapter, topicDao, TOPIC_REQUEST_URL);
		
		initPaginateListView(mTopicListView, mTopicAdapter, new Topic.TopicsCallback());
	}
	
	private void initPager() {
		LayoutInflater inflater = this.getLayoutInflater();
		mAppsSwipeyTabs = (ScrollingTabsView) inflater.inflate(R.layout.scrolling_tabs_layout, null);
		mAppsScrollingTabsAdapter = new ScrollingTabsAdapter(this, this
				.getResources().getStringArray(R.array.cate_apps_subclss));
		mAppsSwipeyTabs.setAdapter(mAppsScrollingTabsAdapter);
		mAppsPager =  (ViewPager) inflater.inflate(R.layout.viewpager_layout, null);
		
		mGameSwipeyTabs = (ScrollingTabsView) inflater.inflate(R.layout.scrolling_tabs_layout, null);
		mGameScrollingTabsAdapter = new ScrollingTabsAdapter(this, this
				.getResources().getStringArray(R.array.cate_game_subclss));
		mGameSwipeyTabs.setAdapter(mGameScrollingTabsAdapter);
		mGamePager =  (ViewPager) inflater.inflate(R.layout.viewpager_layout, null);
		
		initViewPager();
		mAppsSwipeyTabs.setViewPager(mAppsPager);
		mGameSwipeyTabs.setViewPager(mGamePager);
	}
	
	private void initViewPager() {
		mAppsPagerAdapter = new AppsPagerAdapter(this);
		mAppsPager.setAdapter(mAppsPagerAdapter);
		mAppsPager.setCurrentItem(1);
		mAppsPager.setPageMargin(1);
		
		mGamePagerAdapter = new GamePagerAdapter(this);
		mGamePager.setAdapter(mGamePagerAdapter);
		mGamePager.setCurrentItem(1);
		mGamePager.setPageMargin(1);
	}
	
	private void ensureUiListView(int index) {
		if(mSortMethod == SORT_METHOD_TOPIC) {
			setTopicPage();
		}else if(mSortMethod == SORT_METHOD_APPS) {
			setAppsPage();
		}else if(mSortMethod == SORT_METHOD_GAME) {
			setGamePage();
		}
	}
	
	private void setTopicPage() {
		mTopicAdapter.notifyDataSetChanged();
		getBaseHeaderLayout().setVisibility(View.GONE);
		setBaseCenterLayout(mTopicListView);
		
		addEmptyView(this, mTopicListView);
		
		if(mTopicAdapter.getCount() == 0) {
			mTopicListView.doFirsetQuery(TOPIC_REQUEST_URL);
		}
	}
	
	private void setGamePage() {
		getBaseHeaderLayout().setVisibility(View.VISIBLE);
		setBaseHeaderLayout(mGameSwipeyTabs);
		setBaseCenterLayout(mGamePager);
	}
	
	private void setAppsPage() {
		getBaseHeaderLayout().setVisibility(View.VISIBLE);
		setBaseHeaderLayout(mAppsSwipeyTabs);
		setBaseCenterLayout(mAppsPager);
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
	
	private void refreshAllListView() {
		// 本来定义的规则是CommonEmptyView在接受到DatasetChange消息后就显示加载中,
		// 但是现在要刷新，也调用NotifyDatasetChange，所以当作异常的情况处理好了
		if(emptyView != null) {
			emptyView.encountError();
		}
		if(mTopicAdapter != null)
		{
			mTopicAdapter.notifyDataSetChanged();
		}
		if(mAppsPagerAdapter != null) {
			mAppsPagerAdapter.refresh();
		}
		if(mGamePagerAdapter != null) {
			mGamePagerAdapter.refresh();
		}
	}
}