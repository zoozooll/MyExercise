/**
 * 
 */
package com.mogoo.ping.ctrl;

import com.astuetz.viewpager.extensions.FixedTabsView;
import com.mogoo.ping.R;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * @author Aaron Lee
 * use viewpaper extensions framework to show secondtitle
 * @Date 下午5:33:45  2012-10-30
 */
public class TabContentController1 implements ViewPager.OnPageChangeListener {
	
	private static final String TAG = "TabContentController";
	private Context mContext;
	private ViewGroup mTabContentView;
	private FixedTabsView fixed_icon_tabs;
	private ViewPager mPager;
	private GridSoftwaresController1[] mGridSoftwaresControllers;
	private int mPagerIndex;

	/**
	 * @Author Aaron Lee
	 * @param mContext
	 * @param mTabsView
	 * @Date 上午11:43:12  2012-11-1
	 */
	public TabContentController1(Context mContext, ViewGroup mTabContentView) {
		this.mContext = mContext;
		this.mTabContentView = mTabContentView;
		init();
	}
	
	private void init() {
		fixed_icon_tabs = (FixedTabsView)mTabContentView.findViewById(R.id.fixed_icon_tabs);
		mPager = (ViewPager) mTabContentView.findViewById(R.id.pager);
		mPager.setAdapter(new ContentsPaperAdapter(mContext));
		fixed_icon_tabs.setAdapter(new ContentsTabsAdapter(mContext));
		fixed_icon_tabs.setViewPager(mPager);
		mPager.setCurrentItem(0);
		mPagerIndex = mPager.getCurrentItem();
		mPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		mPagerIndex = position;
		fixed_icon_tabs.selectTab(position);
		mGridSoftwaresControllers[position].startToRefreshContents();
	}
	
	/*public void refreshContents(int flag, Cursor newCursor) {
		switch (flag) {
		case GridSoftwaresController1.TAG_APPLICATIONS_LASTED:
		case GridSoftwaresController1.TAG_APPLICATIONS_RECOMEND:
			mGridSoftwaresControllers[0].refreshContents(newCursor);
			break;
		case GridSoftwaresController1.TAG_GAME_LASTED:
		case GridSoftwaresController1.TAG_GAME_RECOMEND:
			mGridSoftwaresControllers[1].refreshContents(newCursor);
			break;

		default:
			break;
		}
	}*/
	
	public void setContentSyncThreads(int... tags) {
		try {
			mGridSoftwaresControllers = new GridSoftwaresController1[tags.length];
			for (int i = 0; i < tags.length; i++) {
				Log.d(TAG, "pager item "+i+" "+mPager.getChildAt(i));
				mGridSoftwaresControllers[i] = new GridSoftwaresController1(
						mContext, (GridView)((ContentsPaperAdapter)mPager.getAdapter()).getItemView(i), tags[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void beginShowingInfront() {
		mGridSoftwaresControllers[mPagerIndex].startToRefreshContents();
	}

	
	/*private void startTabContentDataThread(int currentItem) {
		// TODO Auto-generated method stub
		
	}*/

	public void onStart() {
		mGridSoftwaresControllers[mPagerIndex].startToRefreshContents();
	}
	
	public void onStop() {
		
	}
	
	public void onDestroy() {
		
	}

	
}
