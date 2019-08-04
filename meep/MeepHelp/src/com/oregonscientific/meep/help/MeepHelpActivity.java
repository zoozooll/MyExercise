package com.oregonscientific.meep.help;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MeepHelpActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initWebView();
    }
    
	
	private WebView mWebviewMain = null;
	private RelativeLayout mMainLayout = null;
	private final String TAB = "meepStore";
	
	//final String URL_MEEP_STORE = "https://store.meeptablet.com";
	final String URL_MEEP_STORE ="https://store.meeptablet.com/usermanual";
	//final String URL_MEEP_STORE = "http://10.1.2.202:8080/home.html"; 
    
    @Override
	protected void onStart() {
		if(!isOnline())
		{
			showMeepPopUpMessage(R.string.browser_title_uh_oh, R.string.browser_msg_no_network);
		}
		//mWebviewMain.clearCache(true);
		super.onStart();
	}



	private void initWebView()
    {
    	mMainLayout = (RelativeLayout)findViewById(R.id.RelativeLayoutMain);
    	
    	mWebviewMain = (WebView)findViewById(R.id.webViewStoreMain);
    	//mWebviewMain.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
    	//mWebviewMain.getSettings().setSupportZoom(false);
    	
    	//mWebviewMain.setVerticalScrollBarEnabled(false);
    	//mWebviewMain.setVerticalFadingEdgeEnabled(false);
    	//mWebviewMain.setHorizontalScrollBarEnabled(false);
    	//mWebviewMain.setHorizontalFadingEdgeEnabled(false);
    	
    	WebSettings setting = mWebviewMain.getSettings();
    	setting.setSupportZoom(true);
    	setting.setBuiltInZoomControls(true);
    	setting.setJavaScriptEnabled(true);
    	//setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    	mWebviewMain.clearCache(true);
    	mWebviewMain.clearHistory();
    	mWebviewMain.clearFormData();
    	Log.d("cache", "clear all cache");
    	
    	mWebviewMain.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(TAB, "onPageFinished url:" + url);
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.d(TAB, "onPageStarted url:" + url);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d(TAB, "shouldOverrideUrlLoading url:" + url);
				
				return super.shouldOverrideUrlLoading(view, url);
			}
    		
    	});
    	
    	mWebviewMain.loadUrl(URL_MEEP_STORE);

    	
    }
	
	
	private void installApp(String path)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		startActivity(intent);  
	}
	
	
	private void hideDownloadNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(1002);

	}
    
    public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
    
	private RelativeLayout mPopupLayout = null;
    private void showMeepPopUpMessage(int titleId, int messageId)
	{
		LayoutInflater lf = getLayoutInflater();
		mPopupLayout = (RelativeLayout) lf.inflate(R.layout.layout_popup_msg, null);
		for(int i=0; i<mPopupLayout.getChildCount(); i++)
		{
			View view = mPopupLayout.getChildAt(i);
			if (view.getId() == R.id.imageViewPopupClose) {
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						hidePopupMessageLayout();
					}
				});
			} else if(view.getId() == R.id.textViewPopupOk)
			{
				view.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						hidePopupMessageLayout();
					}
				});
			} else if (view.getId() == R.id.textViewPopupMsg) {
				TextView msg = (TextView)view;
				msg.setText(messageId);
			} else if (view.getId() == R.id.textViewPopupTitle) {
				TextView textviewTitle = (TextView)view;
				textviewTitle.setText(titleId);
			}
			
		}
		mPopupLayout.setVisibility(View.VISIBLE);
		mPopupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mMainLayout.addView(mPopupLayout);
	}
	
	
	
	private void hidePopupMessageLayout()
	{
		mMainLayout.removeView(mPopupLayout);
		mPopupLayout = null;
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	
	
//	 @Override
//	    public boolean onKeyDown(int keyCode, KeyEvent event) {
//	        if(event.getAction() == KeyEvent.ACTION_DOWN){
//	            switch(keyCode)
//	            {
//	            case KeyEvent.KEYCODE_BACK:
//	                if(mWebviewMain.canGoBack() == true){
//	                	mWebviewMain.goBack();
//	                }else{
//	                    finish();
//	                }
//	                return true;
//	            }
//
//	        }
//	        return super.onKeyDown(keyCode, event);
//	    }
    
}