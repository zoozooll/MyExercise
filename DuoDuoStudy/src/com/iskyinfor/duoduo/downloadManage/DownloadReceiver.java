package com.iskyinfor.duoduo.downloadManage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 下载组件的广播接收类，主要接收网络状态变化 和 主动启动下载服务 两种广播进行处理
 * 
 * @author pKF29007
 */
public class DownloadReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {

		if (Constants.ACTION_RESTART_SERVICE.equals(intent.getAction())) {
			Intent reStartIntent = new Intent(context, DownloadService.class);
			reStartIntent.setAction(Constants.ACTION_RESTART_SERVICE);
			context.startService(reStartIntent);
		} else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(
				intent.getAction())) {
			NetworkInfo info = (NetworkInfo) intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (info != null && info.isConnected()) {
				Intent netWakeIntent = new Intent(context,
						DownloadService.class);
				netWakeIntent
						.setAction(Constants.ACTION_NETCHANGE_WAKEUP_SERVICE);
				context.startService(netWakeIntent);
			}
		}
	}
}
