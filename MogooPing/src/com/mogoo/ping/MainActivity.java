package com.mogoo.ping;


import org.apache.http.ParseException;

import com.mogoo.ping.ctrl.NotificationController;
import com.mogoo.ping.ctrl.RemoteApksManager;
import com.mogoo.ping.ctrl.TabWidgetController;
import com.mogoo.ping.ctrl.TabWidgetController1;
import com.mogoo.ping.ctrl.ToastController;
import com.mogoo.ping.ctrl.UrlController;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.SharedPreferencesManager;
import com.mogoo.ping.network.NetworkWorking;
import com.mogoo.ping.utils.UsedDataManager;
import com.mogoo.ping.utils.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabWidget;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	
	private static Context mContext;
	
	private NotificationController notificationController;
	private TabWidgetController1 mTabWidgetController;
	
	private TabHost mTabHost;
	private TabWidget tabs;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		findviews();
		control();
		setContentView(mTabHost);
	}

	private void findviews() {
		mTabHost = (TabHost) LayoutInflater.from(this).inflate(R.layout.main, null);
		mTabHost.setup();
		tabs = mTabHost.getTabWidget();
	}
	
	private void control() {
		notificationController = NotificationController.getInstance(this);
		notificationController.showNotification(R.string.app_name, R.drawable.ic_launcher);
		
		mTabWidgetController = new TabWidgetController1(this, mTabHost, tabs);
		//��վ��
		//mWebViewController = new WebViewController(webview_tab_content_favorite);
		//mWebViewController.startConnect();
		
		//RemoteApksManager.getInstance(this).startSync(0xF);
	}
	// modify by lizuokang 2012-10-17
	// run the sharepreferences in the method onStart() instead of onResume
	@Override
	protected void onResume() {
		
		super.onResume();
	}

	@Override
	protected void onStart() {
		final SharedPreferencesManager preferences = SharedPreferencesManager.getSharedPreferencesManager(this);
		try {
			Log.i(TAG, "url getPathRegrestSelf "+UrlController.getPathRegrestSelf());
			if (!preferences.isFirstRunning() && (NetworkWorking.checkInternet(this) != -1)) {
				NetworkWorking.requestByGet(UrlController.getPathRegrestSelf(), new Runnable() {
					
					@Override
					public void run() {
						preferences.writeFirstRunning();
					}
				});
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		mTabWidgetController.onStart();
		super.onStart();
	}
	// end

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		mContext = null;
		ApksDao.getInstance(this).closeDB();
		ToastController.clearToast();
		super.onDestroy();
	}
	
	public static Context getApplicationContext(Context context) {
		if (mContext != null) 
			return mContext;
		else {
			return new Application();
		}
	}
	
}
