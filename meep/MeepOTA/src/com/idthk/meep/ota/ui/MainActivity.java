package com.idthk.meep.ota.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.idthk.meep.ota.R;
import com.idthk.meep.ota.rest.OtaUpdateFeedback;
import com.idthk.meep.ota.rest.RestRequest;
import com.idthk.meep.ota.rest.RestRequest.OtaUpdateListener;
import com.idthk.meep.ota.utility.Constants;
import com.idthk.meep.ota.utility.OtaUpgradeUtility;
import com.idthk.meep.ota.utility.OtaUpgradeUtility.ProgressListener;
import com.oregonscientific.meep.customdialog.CommonPopup;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private final static String TAG = "OTA";

	private String url;
	public String not_available="";
	
	public final static String localPath_sdcard_extend = "/storage/external_storage/sdcard1/update.zip";
	public final static String localPath_sdcard = "/mnt/sdcard/update.zip";
	public final static String localPath_cache = "/cache/update.zip";
	public final static int GET_VERSION_CURRENT = 0;
	public final static int GET_VERSION_NEW = 1;
	public final static int DOWNLOAD_FAILURE = 2;
	public final static int DISPLAY_BUTTON_DOWNLOAD = 3;
	public final static int WIFI_OFF = 4;
	public final static int VERIFY_OTA_FAIL = 5;
	public final static int DISPLAY_PROGRESS = 6;
	public final static int DOWNLOAD_COMPLETE = 7;
	RestRequest rest;
	public Context mContext;
	ProgressBar bar;
	TextView hint;
	Button download;
	Button local;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        not_available = mContext.getResources().getString(R.string.not_available);
        handler.sendEmptyMessage(GET_VERSION_CURRENT);
        
        rest = new RestRequest(this);
        
        handler.sendEmptyMessage(GET_VERSION_NEW);
        
        bar = (ProgressBar) findViewById(R.id.progressBar);
        hint = (TextView)findViewById(R.id.hint_text);
        download = (Button) findViewById(R.id.download);
        local = (Button) findViewById(R.id.local);
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(Constants.ACTION_DOWNLOAD_PROGRESS);
        filter.addAction(Constants.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(Constants.ACTION_DOWNLOAD_ABORT);
        registerReceiver(myReceiver, filter);
        
        if(DownloadService.mDownloading)
        {
        	displayProgress(R.string.downloading);
        	enableAllButtons(false);
        	if(!rest.isWifiAvailable())
    		{
    			popupMessage(R.string.wifi_off);
    		}
        }
        else 
        {
    		if(!rest.isWifiAvailable())
    		{
    			download.setText(R.string.check_agin);
    		}
        }
        
    }
	
	Handler handler = new Handler()
    {
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
    		case GET_VERSION_CURRENT:
    			((TextView)findViewById(R.id.current)).setText(OtaUpgradeUtility.getVersionName());
    			break;
			case GET_VERSION_NEW:
				rest.setmOtaUpdateListener(new OtaUpdateListener() {
					@Override
					public void onReceivedSuccess(OtaUpdateFeedback otaUpdateFeedback) {
						((TextView)findViewById(R.id.update)).setText(otaUpdateFeedback.getVersionName());
						((TextView)findViewById(R.id.update)).setTextColor(mContext.getResources().getColor(R.color.text_orange));
				    	((TextView)findViewById(R.id.description)).setText(otaUpdateFeedback.getChangelog());
						url = otaUpdateFeedback.getUrl();
						handler.sendEmptyMessage(DISPLAY_BUTTON_DOWNLOAD);
					}
					
					@Override
					public void onReceivedFailued(OtaUpdateFeedback otaUpdateFeedback) {
						((TextView)findViewById(R.id.update)).setText(not_available);
				    	((TextView)findViewById(R.id.description)).setText(not_available);
					}
				});
				getNewUpdate();
				break;
			case DOWNLOAD_FAILURE:
				dismissProgress();
				popupMessage(R.string.download_abort);
	    		enableAllButtons(true);
	    		break;
			case DOWNLOAD_COMPLETE:
				dismissProgress();
				popupMessageBlockBack(R.string.proceeding);
				
				break;
			case DISPLAY_BUTTON_DOWNLOAD:
				download.setText(R.string.ota_download);
				if(!DownloadService.mDownloading)
					enableAllButtons(true);
				break;
			case VERIFY_OTA_FAIL:
				dismissProgress();
    			popupMessage(R.string.verify_file_fail);
    			enableAllButtons(true);
    			//delete OTA file
    			deleteFile(localPath_cache);
    			break;
			case DISPLAY_PROGRESS:
				int progress = msg.getData().getInt("progress");
	    		int resId = msg.getData().getInt("text");
				displayProgress(resId);
				bar.setProgress(progress);
				break;
			default:
				break;
			}
    		
    	};
    };
    
    public void upgradeLocal(View view)
    {
    	if(!isPowerConnected())
    	{
    		alertPowerConnectionLost();
    		return;
    	}
    	popupMessageBlockBack(R.string.proceeding);
    	enableAllButtons(false);
    	String localPath = getLocalPath();
    	if(localPath == null)
    	{
    		//Pop up message:cannot find local file
    		popupMessage(R.string.find_file_fail);
    		enableAllButtons(true);
    		return;
    	}
//    	startUpgradeOta(localPath);
    	new UpgradeOtaTask().execute(localPath);
    	
    }
    
    public boolean deleteFile(String path){
		File file = new File(path);
//		file.deleteOnExit();
		try {
			if (file.exists()) {
				file.delete();
				return true;
			}
		} catch (Exception e) {
			Log.e("isEbookInstalled", "delete file error : " + e.toString());
			return false;
		}
		return false;
	}
    
    public void upgradeDownload(View view)
    {
    	if(rest.isWifiAvailable())
    	{
    		if(!isPowerConnected())
    		{
    			alertPowerConnectionLost();
    			return;
    		}
    		if(url!=null)
    		{
    			enableAllButtons(false);
    			displayProgress(R.string.downloading);
    			Intent service = new Intent(this,DownloadService.class);
    			service.putExtra(Constants.VALUE_STRING_URL, url);
    			startService(service);
    		}
    		else
    		{
    			handler.sendEmptyMessage(GET_VERSION_NEW);
    		}
    	}
    	else
    	{
    		popupMessage(R.string.wifi_off);
    	}
    }
    
    
    private class UpgradeOtaTask extends AsyncTask<String, Void, Void>{
    	
    	@Override
    	protected Void doInBackground(String... arg0) {
    		OtaUpgradeUtility ota = new OtaUpgradeUtility(MainActivity.this);
        	Log.d(TAG,"file path is:"+arg0[0]);
        	if (!ota.verifyPackage(arg0[0])) 
    		{
    			Log.d(TAG, "failed package verification, installation aborted.");
    			//Pop up:Verify OTA failed
    			handler.sendEmptyMessage(VERIFY_OTA_FAIL);
    			return null;
    		}
        	if(commonPopup!=null) commonPopup.dismiss();
    		//verify ota ok
    		Log.d(TAG, "package verified, installing OTA package");
    		ota.beginUpgrade(arg0[0],new ProgressListener() {
    			@Override
    			public void onProgress(int progress) {
    				Log.d(TAG,"progress:"+progress);
    				Message message = new Message();
    				message.what = DISPLAY_PROGRESS;
    				Bundle data = new Bundle();
    				data.putInt("progress", progress);
    				data.putInt("text", R.string.downloading);
    				message.setData(data);
    				handler.sendMessage(message);
    			}

    			@Override
    			public void onVerifyFailed(int errorCode, Object object) {
    				Log.d(TAG,"errorCode:"+errorCode);
    				handler.sendEmptyMessage(VERIFY_OTA_FAIL);
    			}

    			@Override
    			public void onCopyProgress(int progress) {
    				Log.d(TAG,"copy progress:"+progress);
    				Message message = new Message();
    				message.what = DISPLAY_PROGRESS;
    				Bundle data = new Bundle();
    				data.putInt("progress", progress);
    				data.putInt("text", R.string.downloading);
    				message.setData(data);
    				handler.sendMessage(message);
    			}

    			@Override
    			public void onCopyFailed(int errorCode, Object object) {
    				Log.d(TAG,"copy error:"+errorCode);
    				handler.sendEmptyMessage(VERIFY_OTA_FAIL);
    			}
    		});
    		return null;
    	}
    };
    
    
//    public void startUpgradeOta(String path)
//    {
//    	OtaUpgradeUtility ota = new OtaUpgradeUtility(this);
//    	Log.d(TAG,"file path is:"+path);
//    	if (!ota.verifyPackage(path)) 
//		{
//			Log.d(TAG, "failed package verification, installation aborted.");
//			//Pop up:Verify OTA failed
//			dismissProgress();
//			popup(PopUpDialogFragment.COMMON_MESSAGE,R.string.verify_file_fail);
//			enableAllButtons(true);
//			return;
//		}
//		//verify ota ok
//		Log.d(TAG, "package verified, installing OTA package");
//		ota.beginUpgrade(path,new ProgressListener() {
//			@Override
//			public void onProgress(int progress) {
//				Log.d(TAG,"progress:"+progress);
//				if(progress == 1)
//				{
//					displayProgress(R.string.proceeding);
//				}
//				bar.setProgress(progress);
//			}
//
//			@Override
//			public void onVerifyFailed(int errorCode, Object object) {
//				Log.d(TAG,"errorCode:"+errorCode);
//				dismissProgress();
//			}
//
//			@Override
//			public void onCopyProgress(int progress) {
//				Log.d(TAG,"copy progress:"+progress);
//				if(progress == 1)
//				{
//					displayProgress(R.string.copying);
//				}
//				bar.setProgress(progress);
//			}
//
//			@Override
//			public void onCopyFailed(int errorCode, Object object) {
//				Log.d(TAG,"copy error:"+errorCode);
//				dismissProgress();
//			}
//		});
//    }
    
    public String getLocalPath()
    {
    	File file = new File(localPath_sdcard_extend);
    	//whether file exists in external sdcard
    	if(file.exists())
    	{
    		return localPath_sdcard_extend;
    	}
    	//whether file exists in external storage sdcard
    	file = new File(localPath_sdcard);
    	if(file.exists())
    	{
    		return localPath_sdcard;
    	}
    	return null;
    }
    
    
    private void getNewUpdate()
    {
    	String current = OtaUpgradeUtility.getVersionCode();
    	String serial = OtaUpgradeUtility.getSerialNumber();
    	if(rest.isWifiAvailable())
        {
        	rest.otaUpdate(current,serial);
        }
    }
    
    private int mProgress = 0;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {  
        
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if(Constants.ACTION_DOWNLOAD_PROGRESS.equals(action))
            {
            	mProgress = intent.getIntExtra(Constants.VALUE_STRING_PROGRESS, 0);
            	bar.setProgress(mProgress);
            }
            else if(Constants.ACTION_DOWNLOAD_COMPLETE.equals(action))
            {
        		handler.sendEmptyMessage(DOWNLOAD_COMPLETE);
        		new UpgradeOtaTask().execute(OtaUpgradeUtility.CACHE_PARTITION + OtaUpgradeUtility.DEFAULT_PACKAGE_NAME);
            }
            else if(Constants.ACTION_DOWNLOAD_ABORT.equals(action))
            {
            	handler.sendEmptyMessage(DOWNLOAD_FAILURE);
            }
            else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
            	int status = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            	if(WifiManager.WIFI_STATE_ENABLED == status)
            	{
            		Log.d(TAG,"wifi on");
//            		handler.sendEmptyMessage(GET_VERSION_NEW);
            	}
            	else if(WifiManager.WIFI_STATE_DISABLED == status)
            	{
            		Log.d(TAG,"wifi off");
            		
            	}
            }
            
        }  
    };
    
    
    
    public void displayProgress(int resId)
    {
    	hint.setText(mContext.getResources().getString(resId));
		hint.setVisibility(View.VISIBLE);
		bar.setVisibility(View.VISIBLE);
    }
    
    public void dismissProgress()
    {
    	hint.setVisibility(View.INVISIBLE);
		bar.setVisibility(View.INVISIBLE);
    }
    
    CommonPopup commonPopup;
    public void popupMessage(int title,int resId)
    {
		if(commonPopup!=null) commonPopup.dismiss();
		commonPopup = new CommonPopup(this,title,resId);
		commonPopup.show();
    }
    public void popupMessage(int resId)
    {
    	if(commonPopup!=null) commonPopup.dismiss();
    	commonPopup = new CommonPopup(this,R.string.ota,resId);
    	commonPopup.show();
    }
    public void popupMessageBlockBack(int resId)
    {
    	if(commonPopup!=null) commonPopup.dismiss();
    	commonPopup = new CommonPopup(this,R.string.ota,resId);
    	commonPopup.blockBackButton();
    	commonPopup.show();
    }
    
    @Override
    protected void onDestroy() {
    	super.onStop();
    	unregisterReceiver(myReceiver);
    	
    }
    
    public void enableAllButtons(boolean enable)
    {
    	download.setEnabled(enable);
    	local.setEnabled(enable);
    	
    }
    
    public boolean isPowerConnected()
    {
    	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    	Intent batteryStatus = this.registerReceiver(null, ifilter);

    	// Are we charging / charged?
    	int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    	boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
    	                     status == BatteryManager.BATTERY_STATUS_FULL;
    	return isCharging;
    }
    
    public void alertPowerConnectionLost()
    {
    	String message = getResources().getString(R.string.power_connection_needed);
		CommonPopup cp = new CommonPopup(this,R.string.ota,message);
		cp.show();
    }
    
}
