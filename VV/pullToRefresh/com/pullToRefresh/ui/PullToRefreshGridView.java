package com.pullToRefresh.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.pullToRefresh.ui.ILoadingLayout.State;

/**
 * 这个类实现了GridView下拉刷新，上加载更多和滑到底部自动加载
 * @author Li Hong
 * @since 2013-8-15
 */
public class PullToRefreshGridView extends PullToRefreshBase<GridView>
		implements OnScrollListener {
	/** ListView */
	private PullGridView mGridView;
	/** 滚动的监听器 */
	private OnScrollListener mScrollListener;

	/**
	 * 构造方法
	 * @param context context
	 */
	public PullToRefreshGridView(Context context) {
		this(context, null);
	}
	/**
	 * 构造方法
	 * @param context context
	 * @param attrs attrs
	 */
	public PullToRefreshGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	/**
	 * 构造方法
	 * @param context context
	 * @param attrs attrs
	 * @param defStyle defStyle
	 */
	public PullToRefreshGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected GridView createRefreshableView(Context context, AttributeSet attrs) {
		mGridView = new PullGridView(context, this);
		mGridView.setOnScrollListener(this);
		mGridView.setVerticalScrollBarEnabled(false);
		mGridView.setFadingEdgeLength(0);
		mGridView.setSmoothScrollbarEnabled(true);
		mGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		mGridView.setScrollingCacheEnabled(false);
		mGridView.setOnScrollListener(this);
		return mGridView;
	}
	/**
	 * 设置是否有更多数据的标志
	 * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
	 */
	public void setHasMoreData(boolean hasMoreData) {
		LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
		if (null != footerLoadingLayout) {
			if (!hasMoreData) {
				footerLoadingLayout.setState(State.NO_MORE_DATA);
			} else {
				footerLoadingLayout.setState(State.RESET);
			}
		}
	}
	/**
	 * 设置滑动的监听器
	 * @param l 监听器
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}
	@Override
	protected boolean isReadyForPullUp() {
		return isPullLoadEnabled() && hasMoreData()
				&& mGridView.isLastItemVisible()
				&& mGridView.isFillScreenItem();
	}
	@Override
	protected boolean isReadyForPullDown() {
		return isPullRefreshEnabled() && isFirstItemVisible()
				&& mRefreshableView.getFirstVisiblePosition() == 0;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isScrollLoadEnabled() && hasMoreData()) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					|| scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (isReadyForPullUp()) {
					startLoading();
				}
			}
		}
		if (null != mScrollListener) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (null != mScrollListener) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}
	/**
	 * 表示是否还有更多数据
	 * @return true表示还有更多数据
	 */
	@Override
	public boolean hasMoreData() {
		if (getFooterLoadingLayout().getState() == State.NO_MORE_DATA) {
			return false;
		}
		return true;
	}
	/**
	 * 判断第一个child是否完全显示出来
	 * @return true完全显示出来，否则false
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mGridView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			return true;
		}
		int mostTop = (mGridView.getChildCount() > 0) ? mGridView.getChildAt(0)
				.getTop() : 0;
		if (mostTop >= 0) {
			return true;
		}
		return false;
	}
	@Override
	public void addHeaderAndFooter(Context context) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LoadingLayout headerLayout = getHeaderLoadingLayout();
		if (null != headerLayout) {
			if (this == headerLayout.getParent()) {
				removeView(headerLayout);
			}
			addView(headerLayout, 0, params);
		}
		LoadingLayout footerLayout = getFooterLoadingLayout();
		if (null != footerLayout) {
			if (footerLayout.getParent() != null) {
				((ViewGroup) footerLayout.getParent()).removeView(footerLayout);
			}
			mGridView.addFooterView(footerLayout, null, false);
			footerLayout.show(false);
		}
	}
}