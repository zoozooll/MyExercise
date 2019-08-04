/**
 * 
 */
package com.beem.project.btf.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;

/**
 * @author Aaron Lee Created at 下午2:16:49 2016-1-21
 */
public class FolderItemVideoPlayerActivity extends VVBaseFragmentActivity {
	private static final String TAG = "FolderItemVideoPlayerActivity";
	public static final String EXTRA_URL = "url";
	private String url;
	private WebView webview;
	private View back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		webview = (WebView) findViewById(R.id.webview);
		back = findViewById(R.id.back);
		url = getIntent().getDataString();
		//if (TextUtils.isEmpty(url)){
		//					url = "http://192.168.12.41/album/0b14e59dbed66f51e41dbc198c8053f7619d8471.htm";
		//}
		if (TextUtils.isEmpty(url)) {
			Toast.makeText(this, R.string.shareranking_novideo,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		setupWebview();
	}
	private void setupWebview() {
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportZoom(false);
		webview.getSettings().setBuiltInZoomControls(false);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.loadUrl(url);
	}
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.back:
				onBackPressed();
				break;
			default:
				break;
		}
	}
	@Override
	public void onBackPressed() {
		// Webview will be destroied when this activity try to close.
		super.onBackPressed();
		webview.destroy();
	}
	void goBackOnePageOrQuit() {
		moveTaskToBack(true);
	}
}
