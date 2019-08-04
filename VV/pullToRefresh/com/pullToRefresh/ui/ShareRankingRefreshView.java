/**
 * 
 */
package com.pullToRefresh.ui;

import com.pullToRefresh.ui.ILoadingLayout.State;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView.OnScrollListener;

/**
 * @author Aaron Lee Created at 下午5:10:19 2016-1-21
 */
public class ShareRankingRefreshView extends PullToRefreshListView {
	private float lastTouchY = -1.f;
	private int maxTranslate = -150;
	private int mScrollState;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ShareRankingRefreshView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public ShareRankingRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * @param context
	 */
	public ShareRankingRefreshView(Context context) {
		super(context);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handled = false;
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = ev.getY();
				mIsHandledTouchEvent = false;
				lastTouchY = ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float deltaY = ev.getY() - mLastMotionY;
				float moveY = ev.getY() - lastTouchY;
				mLastMotionY = ev.getY();
				if (getTranslationY() == 0 && deltaY < 0) {
					//执行整个view向上移动
					setTranslationY(moveY);
					mScrollState = OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
					return true;
				} else if (getTranslationY() == maxTranslate && deltaY > 0) {
					//执行整个view向下移动
					setTranslationY(moveY);
					mScrollState = OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;
					return true;
				} else {
					if (isReadyForPullDown()) {
						pullHeaderLayout(deltaY / OFFSET_RADIO);
						handled = true;
					} else if (isReadyForPullUp()) {
						handled = true;
						startLoading();
					} else {
						mIsHandledTouchEvent = false;
					}
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mIsHandledTouchEvent = false;
				// 当第一个显示出来时
				if (isHeaderLoadingLayoutVisible()) {
					// 调用刷新
					if (mPullDownState == State.RELEASE_TO_REFRESH) {
						startRefreshing();
						handled = true;
					}
					resetHeaderLayout();
				}
				break;
			default:
				break;
		}
		return handled;
	}
}
