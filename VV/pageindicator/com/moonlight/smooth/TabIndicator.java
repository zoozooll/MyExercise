package com.moonlight.smooth;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @ClassName: AutoScrollIndicator
 * @Description: 自动滚动导航栏
 * @author: yuedong bao
 * @date: 2015-11-12 上午11:42:41
 */
public class TabIndicator extends HorizontalScrollView implements PageIndicator {
	protected LinearLayout mTabLayout;
	protected RelativeLayout mTabLayoutContainer;
	//一屏幕显示几个
	protected int column = 1;
	protected int mMaxTabWidth;
	protected int curTabIndex = 0;
	private Runnable mTabSelector;
	protected IndicatorItemClickListener indicatorClickListener;

	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public TabIndicator(Context context) {
		this(context, null);
	}
	public TabIndicator(Context context, int column) {
		this(context, null);
		this.column = column;
	}
	public void init() {
		setHorizontalScrollBarEnabled(false);
		setFadingEdgeLength(0);
		mTabLayoutContainer = new RelativeLayout(getContext());
		mTabLayout = new LinearLayout(getContext());
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabLayoutContainer.addView(mTabLayout, new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		addView(mTabLayoutContainer, new HorizontalScrollView.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
	}
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);
		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (1f
						* (MeasureSpec.getSize(widthMeasureSpec)
								- getPaddingLeft() - getPaddingRight())
						/ column + 0.5);
			} else {
				mMaxTabWidth = (int) (1f * (MeasureSpec
						.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight()) / 2 + 0.5);
			}
		} else {
			mMaxTabWidth = -1;
		}
		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();
		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(curTabIndex);
		}
	}
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}
	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}
	@Override
	public void setCurrentItem(int item) {
		curTabIndex = item;
		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}
	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			@Override
			public void run() {
				//滚动到指定位置
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}
	@Override
	public void setonIndicatorItemClickListener(
			IndicatorItemClickListener listener) {
		this.indicatorClickListener = listener;
	}
	public void addTab(View view, int index, Object data) {
		TabViewContainer tabContainer = new TabViewContainer(getContext());
		TabViewContainer.LayoutParams lp = new TabViewContainer.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		lp.gravity = Gravity.CENTER;
		tabContainer.addView(view, lp);
		tabContainer.setOnClickListener(mTabClickListener);
		tabContainer.mIndex = index >= 0 ? index : mTabLayout.getChildCount();
		tabContainer.data = data;
		tabContainer.setClickable(true);
		view.setClickable(false);
		mTabLayout.addView(tabContainer, index, new LinearLayout.LayoutParams(
				0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
	}
	public void addTab(View view) {
		addTab(view, -1, null);
	}

	public class TabViewContainer extends LinearLayout {
		private int mIndex;
		private Object data;

		public TabViewContainer(Context context) {
			super(context);
		}
		public TabViewContainer(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() < mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}
	}

	private final OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			TabViewContainer tabView = (TabViewContainer) view;
			curTabIndex = tabView.mIndex;
			if (indicatorClickListener != null) {
				indicatorClickListener.onClick(view, tabView.mIndex,
						tabView.data);
			}
		}
	};

	public void setIndicatorClickListener(
			IndicatorItemClickListener indicatorClickListener) {
		this.indicatorClickListener = indicatorClickListener;
	}
	public int getCurTabIndex() {
		return curTabIndex;
	}
	public View getTabView(int index) {
		View retVal = null;
		TabViewContainer tabView = (TabViewContainer) mTabLayout
				.getChildAt(index);
		if (tabView != null) {
			retVal = tabView.getChildAt(0);
		}
		return retVal;
	}
	//货权当前
	public View getCurTabView() {
		return getTabView(curTabIndex);
	}
	public void setColumn(int column) {
		this.column = column;
	}
}
