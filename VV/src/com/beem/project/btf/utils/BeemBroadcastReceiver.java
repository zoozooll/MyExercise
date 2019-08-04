/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;

/**
 * Manage broadcast disconnect intent.
 * @author nikita
 */
public class BeemBroadcastReceiver extends VVBaseBroadCastReceiver {
	/** Broadcast intent type. */
	public static final String BEEM_CONNECTION_CLOSED = "BeemConnectionClosed";
	private final ConnectivityManager connectivityManager;
	private OnNetworkAvailableListener onNetworkAvailableListener;

	public static interface OnNetworkAvailableListener {
		//		public void onNetworkAvailable();
		//		public void onNetworkUnavailable();
		public void onConnectionClosed(Intent intent);
	}

	/**
	 * constructor.
	 */
	public BeemBroadcastReceiver(Context context) {
		super(false);
		connectivityManager = (ConnectivityManager) BeemApplication
				.getInstance().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReceive(final Context context, final Intent intent) {
		String intentAction = intent.getAction();
		//LogUtils.i("action:" + intentAction);
		if (intentAction.equals(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED)) {
			if (onNetworkAvailableListener != null)
				onNetworkAvailableListener.onConnectionClosed(intent);
		}
		//		else if (intentAction.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
		//			/*if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
		//				if (onNetworkAvailableListener != null)
		//					onNetworkAvailableListener.onNetworkUnavailable();
		//			} else {
		//				if (onNetworkAvailableListener != null)
		//					onNetworkAvailableListener.onNetworkAvailable();
		//			}*/
		//			boolean success = false;
		//			//获得网络连接服务 
		//			ConnectivityManager connManager = (ConnectivityManager) context
		//					.getSystemService(Context.CONNECTIVITY_SERVICE);
		//			// State state = connManager.getActiveNetworkInfo().getState(); 
		//			NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); // 获取网络连接状态 
		//			if (info != null && State.CONNECTED == info.getState()) { // 判断是否正在使用WIFI网络 
		//				success = true;
		//			}
		//			info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); // 获取网络连接状态 
		//			if (info != null && State.CONNECTED == info.getState()) { // 判断是否正在使用GPRS网络 
		//				success = true;
		//			}
		//			//			LogUtils.i("onReceive isNetworkOk:" + success);
		//			if (success != BeemApplication.isNetworkOk()) {
		//				if (success) {
		//					if (onNetworkAvailableListener != null)
		//						onNetworkAvailableListener.onNetworkAvailable();
		//				} else {
		//					if (onNetworkAvailableListener != null)
		//						onNetworkAvailableListn   ener.onNetworkUnavailable();
		//				}
		//			}
		//		}
	}
	/*public void checkConnectionOnDemand() {
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null || info.getState() != State.CONNECTED) {
			if (onNetworkAvailableListener != null)
				onNetworkAvailableListener.onNetworkUnavailable();
		} else {
			if (onNetworkAvailableListener != null)
				onNetworkAvailableListener.onNetworkAvailable();
		}
	}*/
	public void setOnNetworkAvailableListener(
			OnNetworkAvailableListener onNetworkAvailableListener) {
		this.onNetworkAvailableListener = onNetworkAvailableListener;
	}
}
