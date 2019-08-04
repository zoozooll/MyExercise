package com.butterfly.vv;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.CustomTitleBtn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class VVprotocolActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vvprotocol_layout);
		CustomTitleBtn back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("用户协议", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		TextView contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		contacts_textView2.setText("");
		WebView protocal_webview = (WebView) findViewById(R.id.protocal_webview);
		protocal_webview.loadUrl("file:///android_asset/protocol.html");
	}
}
