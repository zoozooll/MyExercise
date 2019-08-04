package com.beem.project.btf.ui.activity;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FolderItemNewsActivity extends Activity {
	private WebView wv_intro;
	private ProgressBar pb_intro;
	protected String mEntryUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folderitem_news);
		mEntryUrl = getIntent().getDataString();
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
		CustomTitleBtn sharebtn = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		sharebtn.setVisibility(View.VISIBLE);
		sharebtn.setTextViewVisibility(View.GONE);
		sharebtn.setViewPaddingRight();
		sharebtn.setImgResource(R.drawable.share_btn_normal);
		sharebtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		pb_intro = (ProgressBar) findViewById(R.id.pb_intro);
		wv_intro = (WebView) findViewById(R.id.wv_intro);
		wv_intro.getSettings().setJavaScriptEnabled(true);
		wv_intro.loadUrl(mEntryUrl);
		initListener();
	}
	public static void launch(final Context context, Uri url) {
		Intent i = new Intent(context, FolderItemNewsActivity.class);
		i.setData(url);
		context.startActivity(i);
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
