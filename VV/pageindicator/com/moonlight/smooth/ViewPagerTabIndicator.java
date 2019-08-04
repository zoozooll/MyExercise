package com.moonlight.smooth;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @ClassName: ViewPagerTabIndicator
 * @Description:A TabIndicator which is customized for the viewpager.
 * @author: yuedong bao
 * @date: 2015-11-12 下午3:49:31
 */
public class ViewPagerTabIndicator extends TabIndicator implements
		OnPageChangeListener {
	private ViewPager viewPager;
	private OnPageChangeListener onPageChangeListener;
	private ImageView bottomLine;
	private int bottomLine_h = 10;
	private int bottomLine_color = Color.parseColor("#33b5e5");

	public ViewPagerTabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		createBottomView();
	}
	public ViewPagerTabIndicator(Context context, int column) {
		super(context, column);
		createBottomView();
	}
	public ViewPagerTabIndicator(Context context) {
		super(context);
		createBottomView();
	}
	/**
	 * @Title: setViewPager
	 * @Description: set the viewpager and listener, so that the indicator can keep step with the
	 *               viewpager.
	 * @param: @param pViewPager
	 * @param: @param pListener
	 * @return: void
	 * @throws:
	 */
	public void setViewPager(final ViewPager pViewPager,
			OnPageChangeListener pListener) {
		this.onPageChangeListener = pListener;
		viewPager = pViewPager;
		if (viewPager != null) {
			viewPager.setOnPageChangeListener(this);
		}
		if (indicatorClickListener == null) {
			setIndicatorClickListener(new IndicatorItemClickListener() {
				@Override
				public void onClick(View v, int index, Object data) {
					viewPager.setCurrentItem(index, true);
				}
			});
		}
	}
	/**
	 * @Title: addTab
	 * @Description: add the tab into the indicator
	 * @param: @param text
	 * @param: @param resId
	 * @return: void
	 * @throws:
	 */
	public void addTab(String text, int resId) {
		TextView tv = new TextView(getContext());
		tv.setText(text);
		tv.setBackgroundResource(resId);
		tv.setGravity(Gravity.CENTER);
		addTab(tv);
	}
	/**
	 * @Title: addTabs
	 * @Description: add some tabs into the indicator
	 * @param: @param text
	 * @param: @param resId
	 * @return: void
	 * @throws:
	 */
	public void addTabs(String[] text, int[] resId) {
		for (int i = 0; i < text.length; i++) {
			addTab(text[i], resId[i]);
		}
	}
	/**
	 * @Title: setColumn
	 * @Description: set the num which the screen shows
	 * @param column
	 * @see com.moonlight.smooth.TabIndicator#setColumn(int)
	 */
	@Override
	public void setColumn(int column) {
		super.setColumn(column);
		if (bottomLine != null) {
			mTabLayoutContainer.removeView(bottomLine);
			createBottomView();
		}
	}
	public void createBottomView() {
		//mMaxTabWidth可能由于tabView长度没有计算好而为0
		post(new Runnable() {
			@Override
			public void run() {
				if (bottomLine == null) {
					bottomLine = new ImageView(getContext());
					if (bottomLine_h == 0) {
						bottomLine_h = (int) toPixel(getContext(),
								TypedValue.COMPLEX_UNIT_DIP, 4);
					}
				}
				bottomLine.setBackgroundColor(bottomLine_color);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						mMaxTabWidth, bottomLine_h);
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				lp.leftMargin = curTabIndex * mMaxTabWidth;
				mTabLayoutContainer.addView(bottomLine, lp);
			}
		});
	}
	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item);
		viewPager.setCurrentItem(item);
	}
	@Override
	public void addTab(View view, int index, Object data) {
		super.addTab(view, index, data);
	}
	@Override
	public void onPageScrollStateChanged(int position) {
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageScrollStateChanged(position);
		}
	}
	@Override
	public void onPageScrolled(final int position, final float positionOffset,
			int positionOffsetPixels) {
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
		}
		if (bottomLine == null) {
			post(new Runnable() {
				@Override
				public void run() {
					RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) bottomLine
							.getLayoutParams();
					lp.leftMargin = (int) (position * mMaxTabWidth + positionOffset
							* mMaxTabWidth);
					bottomLine.setLayoutParams(lp);
				}
			});
		} else {
			RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) bottomLine
					.getLayoutParams();
			lp.leftMargin = (int) (position * mMaxTabWidth + positionOffset
					* mMaxTabWidth);
			bottomLine.setLayoutParams(lp);
		}
	}
	@Override
	public void onPageSelected(int position) {
		setCurrentItem(position);
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageSelected(position);
		}
	}
	/**
	 * @Title: toPixel
	 * @Description: Translate dip,sp,pt to pixel.
	 * @param: @param mContext
	 * @param: @param unit
	 * @param: @param value
	 * @param: @return
	 * @return: float
	 * @throws:
	 */
	public float toPixel(Context mContext, int unit, float value) {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		switch (unit) {
			case TypedValue.COMPLEX_UNIT_PX:
				return value;
			case TypedValue.COMPLEX_UNIT_DIP:
				return value * metrics.density;
			case TypedValue.COMPLEX_UNIT_SP:
				return value * metrics.scaledDensity;
			case TypedValue.COMPLEX_UNIT_PT:
				return value * metrics.xdpi * (1.0f / 72);
			case TypedValue.COMPLEX_UNIT_IN:
				return value * metrics.xdpi;
			case TypedValue.COMPLEX_UNIT_MM:
				return value * metrics.xdpi * (1.0f / 25.4f);
		}
		return 0;
	}
	/**
	 * @Title: setBottomLineColor
	 * @Description: set the bottomline's background color
	 * @param: @param color
	 * @return: void
	 * @throws:
	 */
	public void setBottomLineColor(int color) {
		this.bottomLine_color = color;
		if (bottomLine != null) {
			bottomLine.setBackgroundColor(bottomLine_color);
		}
	}
	/**
	 * @Title: setBottomLineHeight
	 * @Description: set the bottomline's background heigth
	 * @param: @param height
	 * @return: void
	 * @throws:
	 */
	public void setBottomLineHeight(final int height) {
		this.bottomLine_h = height;
		if (bottomLine != null) {
			ViewGroup.LayoutParams lp = bottomLine.getLayoutParams();
			lp.height = height;
			bottomLine.setLayoutParams(lp);
		}
	}
}
