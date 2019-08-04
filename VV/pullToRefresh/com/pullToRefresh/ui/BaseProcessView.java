package com.pullToRefresh.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @ClassName: BaseProcessView
 * @Description: 过程view
 * @author: yuedong bao
 * @date: 2015-8-19 上午10:26:56
 */
public abstract class BaseProcessView extends FrameLayout {
	protected View mContent;

	public BaseProcessView(Context context) {
		super(context);
		initView(context);
	}
	private void initView(Context context) {
		mContent = createProcessView(context);
		if (null == mContent) {
			throw new NullPointerException("Loading view can not be null.");
		}
		addView(mContent, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
	}
	/**
	 * sss
	 * @Title: createProcessView
	 * @Description: 创建流程View：如等待的view，数据空的view,超时的view
	 * @param: @param context
	 * @param: @param attrs
	 * @param: @return
	 * @return: View
	 * @throws:
	 */
	public abstract View createProcessView(Context context);
}
