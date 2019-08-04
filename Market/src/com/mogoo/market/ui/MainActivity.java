package com.mogoo.market.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;

import com.mogoo.market.R;
import com.mogoo.market.adapter.FixedTabsAdapter;
import com.mogoo.market.adapter.HomePagerAdapter;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.viewpager.extensions.FixedTabsView;
import com.mogoo.market.viewpager.extensions.MultiSlideViewPager;
import com.mogoo.market.viewpager.extensions.TabsAdapter;

/**
 * 首页
 */
public class MainActivity extends BaseListActivity {
	public static final String EXTRA_LISTVIEW_INDEX = "listview_index";

	public static final int SORT_HOT_NUMBER = 0;
	public static final int SORT_LATEST_NUMBER = 1;
	public static final int SORT_NES_NUMBER = 2;

	private int mSortMethod;
	private RefreshReceiver mRefreshReceiver;

	private MultiSlideViewPager mPager;
	private FixedTabsView mFixedTabs;

	private HomePagerAdapter mPagerAdapter;
	private TabsAdapter mFixedTabsAdapter;

	private boolean mInit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handleIntent();
		ensureUi();
		registerRefreshReceiver();
	}

	/**
	 * 当下载文件删除时，通知界面刷新
	 */
	private void registerRefreshReceiver() {
		IntentFilter filter = new IntentFilter(
				ManagerActivity.BROADCAST_ACTION_FILE_DELETE);
		mRefreshReceiver = new RefreshReceiver();
		registerReceiver(mRefreshReceiver, filter);
	}

	/**
	 * 处理传送过来的intent
	 */
	private void handleIntent() {
		Intent fromIntent = getIntent();
		if (fromIntent != null) {
			mSortMethod = fromIntent.getIntExtra(EXTRA_LISTVIEW_INDEX, 0);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		refreshView();
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if(mPagerAdapter != null) {
			mPagerAdapter.stop();
		}
		unregisterReceiver(mRefreshReceiver);
		super.onDestroy();
	}
	
	@Override
	public void onAttachedToWindow() {
		if(!mInit) {
			mInit = true;
			return;
		}
		refreshView();
		super.onAttachedToWindow();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 5000) {
				ToolsUtils.showExitToast(getApplicationContext());
				exitTime = System.currentTimeMillis();
			} else {
				Intent intent = new Intent(MainActivity.this,
						MarketGroupActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(MarketGroupActivity.EXTRA_FINISH_SELF, true);
				startActivity(intent);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void ensureUi() {
		initFixedTabsView();
	}

	private void initFixedTabsView() {
		setBaseTopLayout(R.layout.fixed_tabs_layout);
		initViewPager();
		mFixedTabs = (FixedTabsView) findViewById(R.id.fixed_tabs);
		mFixedTabsAdapter = new FixedTabsAdapter(this, new String[] {
				getResources().getString(R.string.app_recommend),
				getResources().getString(R.string.app_latest),
				getResources().getString(R.string.app_necessary) });
		mFixedTabs.setAdapter(mFixedTabsAdapter);
		mFixedTabs.setViewPager(mPager);
		mFixedTabs.addListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mSortMethod = position;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void initViewPager() {
		mPager = (MultiSlideViewPager) this.getLayoutInflater().inflate(
				R.layout.viewpager_layout, null);
		mPagerAdapter = new HomePagerAdapter(this);
		setBaseCenterLayout(mPager);
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(mSortMethod);
		mPager.setPageMargin(1);
		// 设置viewPager第一页带有可滑动视图，viewPager将触摸事件交给slideView.
		mPager.setSlideView(SORT_HOT_NUMBER, mPagerAdapter.getADView());
	}

	public class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					ManagerActivity.BROADCAST_ACTION_FILE_DELETE)) {
				refreshView();
			}
		}

	}

	/**
	 * when you delete a file in the manageActivity,we should update the view
	 */
	private void refreshView() {
		mPagerAdapter.refresh();
	}
}
