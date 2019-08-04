package com.pullToRefresh.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @ClassName: PullListView
 * @Description: PullToRefreshListView真正的listview
 * @author: yuedong bao
 * @date: 2015-8-12 下午1:20:52
 */
class PullListView extends ListView {
	private final PullToRefreshBase<ListView> base;
	private float mLastMotionY;
	private float mTouchSlop;
	private boolean isOverTouchSlop;
	// 解决vewpager滑动事件冲突
	private float xDistance, yDistance, xLast, yLast;

	public PullListView(Context context, PullToRefreshBase<ListView> base) {
		super(context);
		this.base = base;
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				xDistance = yDistance = 0f;
				xLast = ev.getX();
				yLast = ev.getY();
				// mLastMotionY不能放到onTouchEvent,因为有些时候onTouch接受不到actiondown事件
				mLastMotionY = ev.getY();
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				float curX = ev.getX();
				float curY = ev.getY();
				xDistance += Math.abs(curX - xLast);
				yDistance += Math.abs(curY - yLast);
				xLast = curX;
				yLast = curY;
				if (xDistance > yDistance) {
					return false; // 表示向下传递事件
				}
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean retVal = false;
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			float deltaY = ev.getY() - mLastMotionY;
			if (Math.abs(deltaY) > mTouchSlop) {
				isOverTouchSlop = true;
			}
			if (isOverTouchSlop) {
				if (base.isHeaderLoadingLayoutVisible()) {
					retVal = base.onTouchEvent(ev);
				} else {
					// 头没出来,但是
					if (deltaY > 0 && base.isReadyForPullDown()) {
						retVal = base.onTouchEvent(ev);
					} else if (deltaY < 0 && base.isReadyForPullUp()) {
						retVal = base.onTouchEvent(ev);
					}
				}
			}
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			// 抬手时如果头部存在,需要还原
			if (base.isHeaderLoadingLayoutVisible()) {
				retVal = base.onTouchEvent(ev);
			}
			mLastMotionY = -1;
			isOverTouchSlop = false;
		}
		if (!retVal) {
			retVal = super.onTouchEvent(ev);
		}
		return retVal;
	}
	/**
	 * @Title: isFillScreenItem
	 * @Description: 是否满屏，如果满屏footer,header不在同一页
	 * @param: @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isFillScreenItem() {
		int visibleItemCount = getLastVisiblePosition()
				- getFirstVisiblePosition() + 1 - getHeaderViewsCount()
				- getFooterViewsCount();
		int totalItemCount = getCount() - getFooterViewsCount()
				- getHeaderViewsCount();
		if (visibleItemCount < totalItemCount
				|| (getScrollY2() > 0 && base.hasMoreData())) {
			// 可见条目小于总的条目表示已经填满整个屏幕
			// 充满屏幕的时候将footer显示出来
			return true;
		}
		return false;
	}
	public int getScrollY2() {
		View c = getChildAt(0);
		if (c == null) {
			return 0;
		}
		int firstVisiblePosition = getFirstVisiblePosition();
		int top = c.getTop();
		return -top + firstVisiblePosition * c.getHeight();
	}
	/**
	 * 判断最后一个child是否完全显示出来
	 * @return true完全显示出来，否则false
	 */
	public boolean isLastItemVisible() {
		final Adapter adapter = getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			return true;
		}
		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = getLastVisiblePosition();
		/**
		 * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
		 * internally uses a FooterView which messes the positions up. For me we'll just subtract
		 * one to account for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition - 1) {
			final int childIndex = lastVisiblePosition
					- getFirstVisiblePosition();
			final int childCount = getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= getBottom();
			}
		}
		return false;
	}
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		adapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				showFooter();
			}
			@Override
			public void onInvalidated() {
				super.onInvalidated();
				showFooter();
			}
		});
		// 延迟等待listView生成View后决定是否显示footer
		showFooter();
	}
	private void showFooter() {
		post(new Runnable() {
			@Override
			public void run() {
				getViewTreeObserver().addOnGlobalLayoutListener(
						new OnGlobalLayoutListener() {
							@Override
							public void onGlobalLayout() {
								base.getFooterLoadingLayout().show(
										base.isPullLoadEnabled()
												&& isFillScreenItem());
								getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							}
						});
			}
		});
	}
}
