package com.beem.project.btf.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 自定义WebView带进度条
 * @author gaochun
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {
	private ProgressBar progressbar;

	/** 进度条 */
	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化进度条
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		// 设置进度条风格
		progressbar.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 5, 0, 0));
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());
		WebSettings webSetting = getSettings();
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				progressbar.setVisibility(GONE); // 加载完成隐藏进度条
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
}
