package com.beem.project.btf.receiver;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.utils.LogUtils;

import de.greenrobot.event.EventBus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetworkReceiver extends BroadcastReceiver {
	public NetworkReceiver() {
	}
	@Override
	public void onReceive(Context context, Intent intentAction) {
		if (intentAction != null
				&& intentAction.getAction().equals(
						ConnectivityManager.CONNECTIVITY_ACTION)) {
			/*if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
				if (onNetworkAvailableListener != null)
					onNetworkAvailableListener.onNetworkUnavailable();
			} else {
				if (onNetworkAvailableListener != null)
					onNetworkAvailableListener.onNetworkAvailable();
			}*/
			boolean success = false;
			//获得网络连接服务 
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// State state = connManager.getActiveNetworkInfo().getState(); 
			NetworkInfo info = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 获取网络连接状态 
			if (info != null && State.CONNECTED == info.getState()) { // 判断是否正在使用WIFI网络 
				success = true;
			}
			info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 获取网络连接状态 
			if (info != null && State.CONNECTED == info.getState()) { // 判断是否正在使用GPRS网络 
				success = true;
			}
			LogUtils.d("onReceive isNetworkOk:" + success);
			if (success != BeemApplication.isNetworkOk()) {
				// 发送eventbus网络状态改变广播。
				BeemApplication.setNetWorkOk(success);
				EventBus.getDefault().post(
						new EventBusData(
								EventBusData.EventAction.NETWORK_ACTIVE,
								success));
			}
		}
	}
}
