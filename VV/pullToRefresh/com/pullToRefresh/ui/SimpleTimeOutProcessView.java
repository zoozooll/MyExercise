package com.pullToRefresh.ui;

import com.beem.project.btf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * @ClassName: SimpleTimeOutProcessView
 * @Description: 超时流程视图简单实现
 * @author: yuedong bao
 * @date: 2015-8-19 下午5:44:09
 */
public class SimpleTimeOutProcessView extends BaseProcessView {
	private LinearLayout loadFaillayout;
	private Button bn_refresh;

	public SimpleTimeOutProcessView(Context context) {
		super(context);
		loadFaillayout = (LinearLayout) findViewById(R.id.view_load_fail);
		bn_refresh = (Button) findViewById(R.id.bn_refresh);
	}
	@Override
	public View createProcessView(Context context) {
		View container = LayoutInflater.from(context).inflate(
				R.layout.load_failed_layout, null);
		return container;
	}
	/**
	 * @Title: reload
	 * @Description: 设置重新加载监听器
	 * @param: @param click
	 * @return: void
	 * @throws:
	 */
	public void setOnReloadListener(final OnClickListener click) {
		bn_refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				click.onClick(arg0);
			}
		});
	}
}
