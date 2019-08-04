package com.oregonscientific.meep.store;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.store.HttpDownloader.OnDownloadListener;

public class MeepStoreClientActivity extends Activity {
	
	private WebView mWebviewMain = null;
	private RelativeLayout mMainLayout = null;
	private final String TAB = "meepStore";
	
	HttpDownloader mDownloader = null;
	//HashMap<String, String> mHeaderMap = null;
	String mToken;
	
	final String URL_MEEP_STORE = "https://store.meeptablet.com"; 
	//final String URL_MEEP_STORE = "http://static.oregonscientific.com/store/store.html"; 
	//final String URL_MEEP_STORE = "http://10.1.2.202:8080/home.html"; 
	//final String URL_MEEP_STORE = "http://10.1.2.202:8080/store.html"; 
	
	private AnimationDrawable animate = null;
	ImageView mScreenBlock = null;
	ImageView mAnimtedView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String userId = getIntent().getStringExtra(Global.STRING_ID);
        mToken = userId;
        
        initAnimation();
        initWebView();
        initDownLoader();
        
       
        
        //mHeaderMap = new HashMap<String, String>();
        //mHeaderMap.put("authorization", "OSA " + userId);
        
        
        Log.d("token", "token:" + userId);
        
     //   showNotification("angry bird", 100, 50);
    }
    
	private void initAnimation() {
		mScreenBlock = (ImageView) findViewById(R.id.imageViewScreen);
		mAnimtedView = (ImageView) findViewById(R.id.imageViewLoading);
		animate = (AnimationDrawable) mAnimtedView.getDrawable();
	}
	
	private void showLoadingAnimation()
	{
		mScreenBlock.setVisibility(View.VISIBLE);
		mAnimtedView.setVisibility(View.VISIBLE);
		animate.run();
	}
	
	private void hideLoadingAnimation()
	{
		mScreenBlock.setVisibility(View.GONE);
		mAnimtedView.setVisibility(View.GONE);
		animate.stop();
	}
    
    
    
    @Override
	protected void onStart() {
		if(!isOnline())
		{
			showMeepPopUpMessage(R.string.communicator_title_uh_oh, R.string.browser_msg_no_network);
		}
    	
		Uri browserItem = getIntent().getData();
		if (browserItem != null){
			Log.d("browserItem", "browserItem:" + browserItem);
			String url = URL_MEEP_STORE + "?" + browserItem.getQuery() + "#" + mToken;
			mWebviewMain.loadUrl(url);
			Log.d("meepstore", "first url:" + url);
		}
		
		super.onStart();
	}



	private void initWebView()
    {
    	mMainLayout = (RelativeLayout)findViewById(R.id.RelativeLayoutMain);
    	
    	mWebviewMain = (WebView)findViewById(R.id.webViewStoreMain);
    	mWebviewMain.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
    	mWebviewMain.getSettings().setSupportZoom(false);
    	
    	//mWebviewMain.setVerticalScrollBarEnabled(false);
    	mWebviewMain.setVerticalFadingEdgeEnabled(false);
    	//mWebviewMain.setHorizontalScrollBarEnabled(false);
    	mWebviewMain.setHorizontalFadingEdgeEnabled(false);
    	
    	WebSettings setting = mWebviewMain.getSettings();
    	//setting.setSupportZoom(true);
    	//setting.setBuiltInZoomControls(true);
    	setting.setJavaScriptEnabled(true);
    	mWebviewMain.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(TAB, "onPageFinished url:" + url);
				//url += "#" + mToken;
				Log.d(TAB, "onPageFinished new url:" + url);
				hideLoadingAnimation();
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.d(TAB, "onPageStarted url:" + url);
//				if(mToken!= null && !url.contains(mToken))
//				{
//					url += "#" + mToken;
//					mWebviewMain.loadUrl(url);
//					return;
//				}
				Log.d(TAB, "onPageStarted new  url:" + url);
				showLoadingAnimation();
				super.onPageStarted(view, url, favicon);
			}
			

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.d(TAB, "pageshouldOverrideUrlLoading url:" + url);
				
				//TODO 1. base different type of product, to set the storage path
				//2. if app/game, install the apk and update the database
				//3. update the server, when game has installed
				if(url == "")
				{
					downLoadFile(url, "name", "path");
				}
				
				
//				else if (url.contains("store.meeptablet.com"))
//				{
//					
//					mWebviewMain.loadUrl(url, mHeaderMap);
//					return false;
//				}
				
				
				return super.shouldOverrideUrlLoading(view, url);
			}
    		
    	});
    	String url = URL_MEEP_STORE + "#"+ mToken;
    	mWebviewMain.loadUrl(url);

    	
    }
	
	private void initDownLoader()
	{
		mDownloader = new HttpDownloader(this);
		mDownloader.setOnDownloadListener(new OnDownloadListener() {
			
			@Override
			public void onDownloadProgress(String name, int downloadedBytes, int contentLength) {
				// TODO Auto-generated method stub
				showDownloadNotification(name, contentLength, downloadedBytes);
			}
			
			@Override
			public void onDownloadCompleted(boolean downloadAborted) {
				// TODO Auto-generated method stub
				hideDownloadNotification();
				
				
				//TODO get the application path
				installApp("");
			}
		});
	}
	
	private void installApp(String path)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		startActivity(intent);  
	}
	
	private void downLoadFile(String url, String name, String storePath)
	{
		mDownloader.setUrl(url);
		mDownloader.setName(name);
		mDownloader.setStorePath(storePath);
		mDownloader.startDownload();
	}
	
	
	private void showDownloadNotification(String itemName, int fileSize, int downLoadedSize)
	{
		Intent notificationIntent = new Intent(this, MeepStoreClientActivity.class);
        notificationIntent.setAction("download");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
        .setContentTitle(getResources().getText(R.string.mainpage_title_store_downloading).toString())
        .setContentText(getResources().getString(R.string.mainpage_info_store_downloading_xxx))
        .setTicker(itemName+ "aa")
        .setSmallIcon(R.drawable.ic_launcher)
        .setProgress(fileSize, downLoadedSize, false)
        .setWhen(System.currentTimeMillis())
        .setContentIntent(contentIntent)
        .setOngoing(true)
        .getNotification();
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1002, notification);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (mWebviewMain.canGoBack() == true) {
					mWebviewMain.goBack();
				} else {
					finish();
				}
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN)
		{
			mWebviewMain.clearCache(true);
			mWebviewMain.clearHistory();
		}
		return super.onTouchEvent(event);
	}
	
	
    
}