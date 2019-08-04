package com.oregonscientific.meep.safty;

import java.io.File;
import java.util.Locale;



import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.oregonscientific.meep.safty.ui.*;


public class MeepSaftyAndHelpActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        	setContentView(R.layout.category);
    }
    
	
	private WebView mWebviewMain = null;
	private RelativeLayout mMainLayout = null;
	private final String TAB = "meepStore";
	

	String URL_Safety = "http://docs.meeptablet.com/safety/v3/en/"; 
	String URL_usermanual = "http://docs.meeptablet.com/manual/v3/en/"; 
    
    @Override
	protected void onStart() {
	    	
    	ImageView btnSafty = (ImageView) findViewById(R.id.btnSafty);
    	ImageView btnHelp = (ImageView) findViewById(R.id.btnHelp);

    	String currentLocate = Locale.getDefault().getLanguage();
    	URL_Safety = "http://docs.meeptablet.com/safety/v3/"+currentLocate+"/";
    	URL_usermanual = "http://docs.meeptablet.com/manual/v3/"+currentLocate+"/";
    	

    	btnSafty.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		        Intent intent = new Intent(MeepSaftyAndHelpActivity.this,WebBrowserActivity.class);
		        intent.putExtra("weburl",URL_Safety);
				startActivity(intent);  
			}
		});

    	btnHelp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MeepSaftyAndHelpActivity.this,WebBrowserActivity.class);
		        intent.putExtra("weburl",URL_usermanual);
				startActivity(intent);
			}
		});
    	
  	
		if(!isOnline())
		{
			//showMeepPopUpMessage(R.string.browser_title_uh_oh, R.string.browser_msg_no_network);
		}
    	
		super.onStart();
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
//		LayoutInflater lf = getLayoutInflater();
//		mPopupLayout = (RelativeLayout) lf.inflate(R.layout.layout_popup_msg, null);
//		for(int i=0; i<mPopupLayout.getChildCount(); i++)
//		{
//			View view = mPopupLayout.getChildAt(i);
//			if (view.getId() == R.id.imageViewPopupClose) {
//				view.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						hidePopupMessageLayout();
//					}
//				});
//			} else if(view.getId() == R.id.textViewPopupOk)
//			{
//				view.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						hidePopupMessageLayout();
//					}
//				});
//			} else if (view.getId() == R.id.textViewPopupMsg) {
//				TextView msg = (TextView)view;
//				msg.setText(messageId);
//			} else if (view.getId() == R.id.textViewPopupTitle) {
//				TextView textviewTitle = (TextView)view;
//				textviewTitle.setText(titleId);
//			}
//			
//		}
//		mPopupLayout.setVisibility(View.VISIBLE);
//		mPopupLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//		mMainLayout.addView(mPopupLayout);
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