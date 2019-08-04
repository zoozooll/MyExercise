package com.oregonscientific.meep.store2.ctrl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.ItemVersionReport;
import com.oregonscientific.meep.store2.object.ItemVersionReportList;

public class MeepVersionReportCtrl {

	private final String URL_REPORT_VERSION = "";
	private final String LAST_VERSION_REPORT_TIME = "lastVersionReportTime.txt";
	private Thread mThread;
	private Context mContext;
	private String mToken;
	
	private ItemVersionReportList mInstalledVersionList = null;
	
	public MeepVersionReportCtrl(Context context, String token){
		mContext = context;
		mToken = token;
		mInstalledVersionList = new ItemVersionReportList(new ArrayList<ItemVersionReport>());
		mThread = new Thread(runnableReportVersion);
	}
	
	private Runnable runnableReportVersion = new Runnable() {
		
		@Override
		public void run() {
			while (true) {
				if (mInstalledVersionList.getApps().size() > 0) {
					reportInstallVersion();
				}
				try {
					if (isTimeToReportVersion()) {
						try {
							reportVersion();
						} catch (Exception e) {
							
						}
						try {
							Thread.sleep(600000);// check every 10 mins
						} catch (InterruptedException ex) {
							MeepStoreLog.logcatMessage("reportVersion", "sleep 8 hours");
							ex.printStackTrace();
						}

					} else {
						try {
							Thread.sleep(600000);// check every 10 mins
						} catch (InterruptedException e) {
							MeepStoreLog.logcatMessage("reportVersion", "sleep 8 hours");
							e.printStackTrace();
						}
					}
				} catch (Exception ex) {
					try {
						Thread.sleep(600000);// check every 10 mins
					} catch (InterruptedException e) {
						MeepStoreLog.logcatMessage("reportVersion", "sleep 8 hours");
						e.printStackTrace();
					}
				}
			}

		}
	};
	
	public void startReportVersion(){
		mThread.start();
	}
	
	private void reportVersion(){
		ItemVersionReportList versionList = new ItemVersionReportList(getVersions());
		if(versionList.getApps().size()>0){
			Gson gson = new Gson();
			String jsonStr = gson.toJson(versionList);
			sendJsonToServer(jsonStr);
			MeepStoreLog.logcatMessage("reportVersion", jsonStr);
			FileStorageCtrl.saveFileToPrivate(LAST_VERSION_REPORT_TIME, Long.toString(System.currentTimeMillis()), mContext);
		}
	}
	
	private void reportInstallVersion(){
		if(mInstalledVersionList.getApps().size()>0){
			Gson gson = new Gson();
			String jsonStr = gson.toJson(mInstalledVersionList);
			sendJsonToServer(jsonStr);
			MeepStoreLog.logcatMessage("reportVersion", jsonStr);
			FileStorageCtrl.saveFileToPrivate(LAST_VERSION_REPORT_TIME, Long.toString(System.currentTimeMillis()), mContext);
		}
		mInstalledVersionList.getApps().clear();
	}
	
	public void reportInstall(String packageName, int version, String versionName, String installStatus){
		ItemVersionReport item = new ItemVersionReport(packageName, version, versionName, installStatus);
		mInstalledVersionList.getApps().add(item);
	}
	
	
	private void sendJsonToServer(String jsonStr){
		AsyncHttpClient client = new AsyncHttpClient(); 
		String url = "https://portal.meeptablet.com/1/store/update";
		client.addHeader("Content-Type", "application/json");
		client.addHeader("Authorization", "OST "+ mToken);
		HttpEntity httpEntity;
        StringEntity stringEntity;
		try {
			stringEntity = new StringEntity(jsonStr);
			stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpEntity = stringEntity;
			// RequestParams p = new RequestParams();
			// p.put("body", jsonStr);
			client.post(mContext, url, httpEntity, "application/json", new JsonHttpResponseHandler() {
				// client.post(url, p, new JsonHttpResponseHandler(){

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					MeepStoreLog.logcatMessage("received", "report version:" + arg1.toString());
					super.onFailure(arg0, arg1);
					// if(mLoginListener!= null)
					// mLoginListener.onLoginReceived(null);
				}

				@Override
				public void onSuccess(JSONObject json) {
					// String jsonStr = json.toString();
					MeepStoreLog.logcatMessage("received", "report version: " + json.toString());
					// Gson gson = new Gson();
					
					FileStorageCtrl.saveFileToPrivate(LAST_VERSION_REPORT_TIME, String.valueOf(System.currentTimeMillis()), mContext);
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
	}
	
	
	
	private ArrayList<ItemVersionReport> getVersions(){
		ArrayList<ItemVersionReport> versionList = new ArrayList<ItemVersionReport>();
		
		PackageManager packageManager = mContext.getPackageManager();
		List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {

			PackageInfo pInfo;
			try {
				pInfo = packageManager.getPackageInfo(packageInfo.packageName, 0);
				String packageName = packageInfo.packageName;
				int versionCode = pInfo.versionCode;
				String versionName = pInfo.versionName;
				ItemVersionReport version = new ItemVersionReport(packageName, versionCode, versionName, ItemVersionReport.INSTALL_STATUS_INSTALLED);
				versionList.add(version);	
			} catch (NameNotFoundException e) {
				Log.e("reportVersion", "getversion error:" + e.toString());
				e.printStackTrace();
			}

		}
		return versionList;
	}
	
	private boolean isTimeToReportVersion(){
		
		String lastUpdateTimeStr = FileStorageCtrl.loadPrivatesFile(LAST_VERSION_REPORT_TIME, mContext);
		if(lastUpdateTimeStr == null){
			return true;
		}
		
		try {
			long lastUpdateTime = getDigit(lastUpdateTimeStr);
			//report > 8hrs
			if((System.currentTimeMillis() - lastUpdateTime) > 28800000){
				return true;
			}
		} catch (Exception ex) {
			MeepStoreLog.logcatMessage("reportversion", displayCharValues(lastUpdateTimeStr));
			
			MeepStoreLog.logcatMessage("reportversion", displayCharValues("1234567890"));
			return true;
		}
		
		return false;
	}
	
	public static String displayCharValues(String s) {
	    StringBuilder sb = new StringBuilder();
	    for (char c : s.toCharArray()) {
	        sb.append((int) c).append(",");
	    }
	    return sb.toString();
	}
	
	public static long getDigit(String s){
		StringBuilder sb = new StringBuilder();
	    for (char c : s.toCharArray()) {
	    	if((int)c >=48 && (int)c <=57){
	    		sb.append(c);
	    	}
	    }
	    
	    return Long.valueOf(sb.toString());
	}
	
}
