package com.butterfly;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.AppProperty;

public class IntroductionActivity extends VVBaseFragmentActivity {
	private WebView wv_intro;
	private ProgressBar pb_intro;
	protected String mEntryUrl;
	private static final String tag = IntroductionActivity.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_introcuction);
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setVisibility(View.GONE);
		btBack.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		pb_intro = (ProgressBar) findViewById(R.id.pb_intro);
		wv_intro = (WebView) findViewById(R.id.wv_intro);
		wv_intro.getSettings().setJavaScriptEnabled(true);
		wv_intro.loadUrl(AppProperty.getInstance().INTRODUCTION_URL);
		initListener();
	}
	private void initListener() {
		wv_intro.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (errorCode == WebViewClient.ERROR_CONNECT
						|| errorCode == WebViewClient.ERROR_TIMEOUT
						|| errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
					//showErrorPage();
				}
			}
		});
		wv_intro.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					pb_intro.setVisibility(View.GONE);
				}
			}
		});
	}
}
