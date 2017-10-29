package com.vstar3d.VRLauncher;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import com.vstar3d.Obj.AppObj;
import com.vstar3d.Obj.Base;
import com.vstar3d.View.HomeView;
import com.vstar3d.View.SpotView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends CardboardActivity {
	private static final String TAG = "MainActivity";
	private static final int OFFSETX = 40;
	HomeView mView;
	SpotView mSpotL;
	SpotView mSpotR;
	private int viewId=-1;
	private int currentChoosedViewId =-1;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
				case 3333:
					currentChoosedViewId =msg.arg1;
					if(mSpotL!=null)
    			{
    				mSpotL.SetAnimation(msg.arg1,msg.arg2);
    			}
    			if(mSpotR!=null)
    			{
    				mSpotR.SetAnimation(msg.arg1,msg.arg2);
    			}
    			break;
    		case 8888:
    			int id=msg.arg1;
    			if(id==0)//app
    			{
    				mView.ShowAppList(true);
					viewId=1;
    			}else if(id==27)//app
    			{
    				mView.ShowAppList(false);
					viewId=0;
    			}else if(id==28)//app
    			{
    				mView.LastApp();	
    			}else if(id==29)//app
    			{
    				mView.NextApp();	
    			}else if(id>=10 && id<(10+AppObj.pagesize))
    			{
    				mView.startActivity(id-10);
    			}  else if (id == 1) {
					Base.startApp(MainActivity.this, "com.vstar3d.V3DPlayer");
				} else if (id == 2) {
//					try {
//					Intent intent = new Intent();
//					ComponentName component = new ComponentName("com.vstar3d.VRPlayer", "com.vstar3d.activity.VRPlayerMainActivity");
//					intent.setComponent(component);
//					intent.putExtra("type", 1);
//					startActivity(intent);
//					} catch (Exception e1) {
//						e1.printStackTrace();
//						Toast.makeText(MainActivity.this, "请安装程序以后再试一次", Toast.LENGTH_SHORT).show();
//					}
					try {
					Intent intent = getPackageManager().getLaunchIntentForPackage("com.vstar3d.VRPlayer");
					intent.putExtra("type", 1);
					if (intent == null) {
						Toast.makeText(MainActivity.this, getResources().getText(R.string.app_notfound), Toast.LENGTH_SHORT).show();
						return;
					}
					startActivity(new Intent(intent));
					} catch (Exception e1) {
						e1.printStackTrace();
						Toast.makeText(MainActivity.this, getResources().getText(R.string.app_notfound), Toast.LENGTH_SHORT).show();
					}

				} else if (id == 3) {
					Base.startApp(MainActivity.this, "com.vstar3d.VRPlayer");
				} else if (id == 4) {
					Base.startApp(MainActivity.this, "com.vstar3d.market");
				} else if (id == 5) {
					Base.startApp(MainActivity.this, "com.android.settings");
				}
				currentChoosedViewId=-1;

    			break;
    		}
    	}
	};

	IntentFilter mIntentFilter; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		mView = (HomeView) findViewById(R.id.homeview);
		setCardboardView(mView);
		mView.setActivity(this);

		mSpotL = (SpotView) findViewById(R.id.spotl);
		mSpotR = (SpotView) findViewById(R.id.spotr);
		mSpotR.mOffsetX = -OFFSETX;
		mSpotL.mOffsetX = 0;
		mView.SetHandler(mHandler);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		mIntentFilter.addAction(Intent.ACTION_BATTERY_LOW);
		mIntentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
		mIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
		mIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);

		mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);

		mIntentFilter.addAction(Intent.ACTION_TIME_TICK);
		mIntentFilter.addAction(Intent.ACTION_TIME_CHANGED);
		mIntentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);


//		mIntentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//		mIntentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//		mIntentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//		mIntentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//		mIntentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

		registerReceiver(mIntentReceiver, mIntentFilter);
	}

	private int wv=0;
    private int bv=0;
    int wifiState=0;
    int mWifiLevel=0;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {  
		@Override 
		public void onReceive(Context context, Intent intent) {  
			final String action = intent.getAction();
            if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
            	int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                int lv=(int)(level*10/scale);
                if(status==BatteryManager.BATTERY_STATUS_CHARGING) {
					lv += 20;
					if (mView != null)
						mView.SetBv(lv);
				}else {
					bv = lv;
					if (mView != null)
						mView.SetBv(bv);
				}
			} else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
				BluetoothAdapter bluetoothAdapter = BluetoothAdapter
						.getDefaultAdapter();
				if (bluetoothAdapter!=null) {
					if (bluetoothAdapter.isEnabled()) {
						mView.setBluetoothStatus(bluetoothAdapter.isEnabled());
					}
				}
			} else if (action.equalsIgnoreCase(WifiManager.RSSI_CHANGED_ACTION) || action.equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				if (action.equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
					wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
							WifiManager.WIFI_STATE_UNKNOWN);
				} else if (action.equalsIgnoreCase(WifiManager.RSSI_CHANGED_ACTION)) {
					int mWifiRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, -127);
					//	Log.e("Ar110","mWifiRssi="+WifiManager.calculateSignalLevel(mWifiRssi, 4));
					mWifiLevel = WifiManager.calculateSignalLevel(mWifiRssi, 4);
				}


				int wifivalue = 00;
				//	Log.e("3dvstar","wifiState="+wifiState);
				if (WifiManager.WIFI_STATE_ENABLED == wifiState) //已启动
				{
					wifivalue = 30 + mWifiLevel;
				} else if (WifiManager.WIFI_STATE_ENABLING == wifiState) //正在启动
				{
					wifivalue = 30;
					//Log.e("3dvstar","wifiState=WIFI_STATE_ENABLING");
				} else if (WifiManager.WIFI_STATE_DISABLING == wifiState) //正在关闭
				{
					wifivalue = 20;
					//Log.e("3dvstar","wifiState=WIFI_STATE_DISABLING");
				} else if (WifiManager.WIFI_STATE_DISABLED == wifiState) //已关闭
				{
					wifivalue = 10;
					//Log.e("3dvstar","wifiState=WIFI_STATE_DISABLED");
				} else {
					wifivalue = 00;
				}
				wv = wifivalue;
				if (mView != null)
					mView.SetWv(wv);

			}
			// if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
            	
            //}

			if (action.equals(Intent.ACTION_TIME_TICK)||action.equals(Intent.ACTION_TIME_CHANGED)||action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				if(mView!=null)
					mView.updateTime();
			}
         }
    };
    
    @Override
	protected void onPause() {
    
		super.onPause();
		unregisterReceiver(mIntentReceiver);
		mView.onPause();
		
	}
    @Override

    protected void onDestroy() {
		super.onDestroy();

    }

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mIntentReceiver, mIntentFilter);
		mView.onResume();
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter!=null) {
			if (bluetoothAdapter.isEnabled()) {
				mView.setBluetoothStatus(true);
			}else {
				mView.setBluetoothStatus(false);
			}
		}

		mView.updateTime();
		mView.SetWv(30);
		mView.SetBv(100);

	}

	@Override
	protected void onStop() {
		super.onStop();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		boolean bluetoothEnable= Base.getBluetoothEnabled();
		mView.setBluetoothStatus(bluetoothEnable);
		mView.updateTime();
		mView.SetWv(0);
		mView.SetBv(100);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(viewId==1){
				Message msg = mHandler.obtainMessage();
				msg.arg1=27;
				msg.what=8888;
				mHandler.sendMessage(msg);
			}
			return true;
		}else if (keyCode==KeyEvent.KEYCODE_ENTER||keyCode==KeyEvent.KEYCODE_DPAD_CENTER||keyCode==KeyEvent.KEYCODE_BUTTON_A){

			if(currentChoosedViewId !=-1){
				mHandler.removeMessages(8888);

				Message msg = mHandler.obtainMessage();
				msg.arg1= currentChoosedViewId;
				msg.what=8888;
				mHandler.sendMessage(msg);
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if(keyCode==KeyEvent.KEYCODE_ENTER||keyCode==KeyEvent.KEYCODE_DPAD_CENTER||keyCode==KeyEvent.KEYCODE_BUTTON_A){
			if(event.isLongPress()){
				mView.sendResetMessage();
			}

		}
		return super.dispatchKeyEvent(event);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if(event.getAction()==MotionEvent.ACTION_HOVER_ENTER){
//			if(currentChoosedViewId !=-1){
//				mHandler.removeMessages(8888);
//
//				Message msg = mHandler.obtainMessage();
//				msg.arg1= currentChoosedViewId;
//				msg.what=8888;
//				mHandler.sendMessage(msg);
//			}
//		}
//		return super.onTouchEvent(event);
//	}
}
