package com.tcl.manager.networkmonitor;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tcl.base.http.IProviderCallback;
import com.tcl.framework.log.NLog;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.blackwhitelist.WhitelistSync;
import com.tcl.manager.protocol.PhoneInfoProtocol;
import com.tcl.manager.statistic.frequency.LogReport;
import com.tcl.manager.update.UpdateManager;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.util.SharedStorekeeper;

public class NetworkMonitorReceiver extends BroadcastReceiver {

	/**
	 * 检查更新时间24小时
	 */
	private static final int CHECK_UPDATE_INTEVAL = 24 * 3600 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		NLog.v("NetworkBroadcastReceiver", "onReceive");
		if (intent == null)
			return;

		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getActiveNetworkInfo();
			if (info == null || !info.isAvailable()) {
				NLog.i("NetworkBroadcastReceiver", "network not reachable");
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				NLog.i("NetworkBroadcastReceiver", "network reachable via wwan");
				onMobileConnected();
			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				NLog.i("NetworkBroadcastReceiver", "network reachable via wifi");

				onWifiConnected();

			}
		}
	} 

	private void onMobileConnected() {
		// For APM-77 .the the data is more than 7 days, it will upload data with mobile data.
		// Add by zuokang.li. Jan7,2015
		doReportForWeek();
		
		doCheckWhitelist();
	}

	/**
	 * 当网络连上wifi后调用
	 */
	private static void onWifiConnected() {
		// 检查更新
		doCheckUpdate();

		// 上报
		doReport();
		
		
		doCheckWhitelist();
	}

	private static void doReport() {
		// Report and upload the device messages
		sendPhoneMsgReport();
		
		// Report and upload the log data
		String lastreportk = SharedStorekeeper.get(SharedStorekeeper.LOG_LAST_REPORT_TIME);
		SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
		final String current = format.format(new Date());
		if (!current.equals(lastreportk)) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					LogReport report = new LogReport(ManagerApplication.sApplication);
					report.report();
					SharedStorekeeper.save(SharedStorekeeper.LOG_LAST_REPORT_TIME, current);
				}
			}, "do upload log information");
			t.start();
		}
		
	}

	private static void sendPhoneMsgReport() {
		long lastDeviceMsgReport = SharedStorekeeper.getLong(SharedStorekeeper.DEVEICE_LAST_REPORT_TIME);
		if (lastDeviceMsgReport == 0) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					new PhoneInfoProtocol(new IProviderCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean obj) {
							SharedStorekeeper.save(SharedStorekeeper.DEVEICE_LAST_REPORT_TIME, System.currentTimeMillis());
							
						}
						
						@Override
						public void onFailed(int code, String msg, Object obj) {
							
						}
						
						@Override
						public void onCancel() {
							
						}
					}).send();
					
				}
			}, "");
			t.start();
		}
	}

	/**
	 * 进入检查更新逻辑
	 */
	private static void doCheckUpdate() {
		long lastCheckUpdate = SharedStorekeeper.getLong("LAST_CHECK_UPDATE_TIME");
		// if( System.currentTimeMillis() - lastCheckUpdate >
		// CHECK_UPDATE_INTEVAL) {
		doRealcheckUpdate();
		// }

	}
	
	private static void doReportForWeek() {
		String lastreportk = SharedStorekeeper.get(SharedStorekeeper.LOG_LAST_REPORT_TIME);
		final SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
		long current = System.currentTimeMillis();
		long lastUploadTime = 0L;
		try {
			
			lastUploadTime = format.parse(lastreportk).getTime();
		} catch (java.text.ParseException  e) {
			e.printStackTrace();
		}
		if (lastUploadTime == 0L) {
			lastUploadTime = SharedStorekeeper.getLong(SharedStorekeeper.FIRST_TIME_RUNNING);
		}
		if (lastUploadTime == 0L) {
			lastUploadTime = PkgManagerTool.getInstallTime(ManagerApplication.sApplication, ManagerApplication.sApplication.getPackageName());
		}
		if ((current - lastUploadTime) > 1000 * 3600 * 24 * 7) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					LogReport report = new LogReport(ManagerApplication.sApplication);
					report.report();
					SharedStorekeeper.save(SharedStorekeeper.LOG_LAST_REPORT_TIME, format.format(new Date()));
				}
			}, "do upload phone information");
			t.start();
			
			sendPhoneMsgReport();
		}
	}


	/**
	 * 真实的检查
	 */
	private static void doRealcheckUpdate() {
		try {
			UpdateManager um = UpdateManager.getInstance();
			um.init(ManagerApplication.sApplication);
			um.check(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void doCheckWhitelist() {
		WhitelistSync.checkWhiteList();
	}

}
