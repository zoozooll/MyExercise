package com.mogoo.market.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;

import com.mogoo.market.R;
import com.mogoo.market.adapter.FixedTabsAdapter;
import com.mogoo.market.adapter.ManagerPagerAdapter;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.viewpager.extensions.FixedTabsView;
import com.mogoo.market.viewpager.extensions.MultiSlideViewPager;
import com.mogoo.market.viewpager.extensions.TabsAdapter;

/**
 * 管理：包括软件管理和下载管理
 */
public class ManagerActivity extends BaseLoadableListActivityWithViewAndHeader
{
	static final String TAG = "ManagerActivity";
	
	public final static String BROADCAST_ACTION_FILE_DELETE = "action_file_delete";   //删除文件广播ACTION，有些地方可能需要
	
	public static final int RESULT_DELETE_OK = 99;   //软件卸载的请求码，因为有软件包变更回调，所以不需要通过这个监听软件卸载
	
	private int mSortMethod = 0;    					 //判断当前点击的是那个选项卡：DOWNLOAD_MANAGER  or  SOFT_MANAGER
	public static final int DOWNLOAD_MANAGER = 1;
	public static final int SOFT_MANAGER = 0;
	public static final int MORE_SETTINGS = 2;
	
	private FixedTabsView mFixedTabs;
	private TabsAdapter mFixedTabsAdapter;
	
	private ViewPager mPager;
	private ManagerPagerAdapter mPagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ensureUi();
	}
	
	public int getCurrentItem() {
		return mSortMethod;
	}
	
	private void ensureUi() {
		initFixedTabsView();
	}
	
	private void initFixedTabsView() {
		setBaseTopLayout(R.layout.fixed_tabs_layout);
		initViewPager();
		mFixedTabs = (FixedTabsView) findViewById(R.id.fixed_tabs);
		mFixedTabsAdapter = new FixedTabsAdapter(this, new String[] {
				getResources().getString(R.string.soft_manager),
				getResources().getString(R.string.download_manager),
				getResources().getString(R.string.more_settings) });
		mFixedTabs.setAdapter(mFixedTabsAdapter);
		mFixedTabs.setViewPager(mPager);
		mFixedTabs.addListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mSortMethod = position;
				if(mPagerAdapter != null) {
					mPagerAdapter.refresh(mSortMethod);
				}
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
		mPagerAdapter = new ManagerPagerAdapter(this);
		setBaseCenterLayout(mPager);
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(mSortMethod);
		mPager.setPageMargin(1);
	}
	
	/**
	 * 切换底部切换卡，切回管理界面
	 */
	@Override
	public void onAttachedToWindow() 
	{
		//需要重新更新列表数据
		if (mPagerAdapter != null) {
			mPagerAdapter.refresh();
		}
		super.onAttachedToWindow();
	}
	
	@Override
	public void onRestart(){
		if (mPagerAdapter != null) {
			mPagerAdapter.refresh();
		}
		super.onRestart();
	}
	@Override
	protected void onDestroy() 
	{
		if (mPagerAdapter != null) {
			mPagerAdapter.stop();
		}
		super.onDestroy();
	}
	
	/**
	 * 监听返回键，连按2次退出商城
	 */
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (mPagerAdapter != null && mPagerAdapter.isClearAllShowing()) {
				mPagerAdapter.dismissClearAll();
			} else {
				if ((System.currentTimeMillis() - exitTime) > 5000) {
					ToolsUtils.showExitToast(getApplicationContext());
					exitTime = System.currentTimeMillis();
				} else {
					Intent intent = new Intent(ManagerActivity.this,
							MarketGroupActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra(MarketGroupActivity.EXTRA_FINISH_SELF, true);
					startActivity(intent);
				}
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
