package com.pullToRefresh.ui;

import com.beem.project.btf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * @ClassName: SimplePreloadProcessView
 * @Description: 预加载视图简单实现
 * @author: yuedong bao
 * @date: 2015-8-19 下午5:36:27
 */
public class SimplePreloadProcessView extends BaseProcessView {
	private LinearLayout search_loading;
	private ProgressBar progressbar;

	public SimplePreloadProcessView(Context context) {
		super(context);
		progressbar = (ProgressBar) findViewById(R.id.load_progressBar);
		search_loading = (LinearLayout) findViewById(R.id.search_loading);
	}
	@Override
	public View createProcessView(Context context) {
		View container = LayoutInflater.from(context).inflate(
				R.layout.loading_layout, null);
		return container;
	}
	// 切换加载等待布局(是否显示图片加文字的布局)
	public void setLoadinglayout(boolean isPic) {
		if (isPic) {
			progressbar.setVisibility(View.GONE);
			search_loading.setVisibility(View.VISIBLE);
		} else {
			progressbar.setVisibility(View.VISIBLE);
		}
	}
}
