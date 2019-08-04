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
package com.beem.project.btf;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;

/**
 * This class contains informations that needs to be global in the application. Theses informations
 * must be necessary for the activities and the service.
 * @author Da Risk <darisk972@gmail.com>
 * @param <GeofenceClient>
 */
public class BeemApplication extends Application {
	/*
	 * Constants for PREFERENCE_KEY The format of the Preference key is : $name_KEY = "$name"
	 */
	/** Preference key for account username. */
	public static final String ACCOUNT_USERNAME_KEY = "account_username";
	/** Preference key for account password. */
	public static final String ACCOUNT_PASSWORD_KEY = "account_password";
	/** Preference key set to true if using an Android account . */
	public static final String USE_SYSTEM_ACCOUNT_KEY = "use_system_account";
	/** Preference key for Android account type . */
	public static final String ACCOUNT_SYSTEM_TYPE_KEY = "account_system_type";
	/** Preference key for status (available, busy, away, ...). */
	public static final String STATUS_KEY = "status";
	/** Preference key for status message. */
	public static final String STATUS_TEXT_KEY = "status_text";
	/** Preference key for connection resource . */
	public static final String CONNECTION_RESOURCE_KEY = "connection_resource";
	/** Preference key for connection priority. */
	public static final String CONNECTION_PRIORITY_KEY = "connection_priority";
	/** Preference key for the use of a proxy. */
	public static final String PROXY_USE_KEY = "proxy_use";
	/** Preference key for the type of proxy. */
	public static final String PROXY_TYPE_KEY = "proxy_type";
	/** Preference key for the proxy server. */
	public static final String PROXY_SERVER_KEY = "proxy_server";
	/** Preference key for the proxy port. */
	public static final String PROXY_PORT_KEY = "proxy_port";
	/** Preference key for the proxy username. */
	public static final String PROXY_USERNAME_KEY = "proxy_username";
	/** Preference key for the proxy password. */
	public static final String PROXY_PASSWORD_KEY = "proxy_password";
	/** Preference key for vibrate on notification. */
	public static final String NOTIFICATION_VIBRATE_KEY = "notification_vibrate";
	/** Preference key for notification sound. */
	public static final String NOTIFICATION_SOUND_KEY = "notification_sound";
	/** Preference key for smack debugging. */
	public static final String SMACK_DEBUG_KEY = "smack_debug";
	/** Preference key for full Jid for login. */
	public static final String FULL_JID_LOGIN_KEY = "full_jid_login";
	/** Preference key for display offline contact. */
	public static final String SHOW_OFFLINE_CONTACTS_KEY = "show_offline_contacts";
	/** Preference key for hide the groups. */
	public static final String HIDE_GROUPS_KEY = "hide_groups";
	/** Preference key for auto away enable. */
	public static final String USE_AUTO_AWAY_KEY = "use_auto_away";
	/** Preference key for auto away message. */
	public static final String AUTO_AWAY_MSG_KEY = "auto_away_msg";
	/** Preference key for compact chat ui. */
	public static final String USE_COMPACT_CHAT_UI_KEY = "use_compact_chat_ui";
	/** Preference key for history path on the SDCard. */
	public static final String CHAT_HISTORY_KEY = "settings_chat_history_path";
	/** Preference key to show the jid in the contact list. */
	public static final String SHOW_JID = "show_jid";
	public static final String IMAGE_VIEWED_HISTORY_KEY = "downloadpath_edittext_preference";
	public static final String IMAGE_TO_UPLOAD_KEY = "uploadpath_list_preference";
	public static final String ACCOUNT_GPS_KEY = "account_gps_location";
	public static final String ACCOUNT_GPS_ONOFF_KEY = "gprs_checkbox_preference";
	public static final String GPS_RECIEVER_ACTION = "com.geo.vv";
	// 头像
	public static final String ACCOUNT_AVARTA_KEY = "account_avatar";
	// TODO add the other one
	private boolean mIsAccountConfigured;
	private boolean mPepEnabled;
	private SharedPreferences mSettings;
	private final PreferenceListener mPreferenceListener = new PreferenceListener();
	// 表示是否网络畅通
	private static boolean mIsNetworkOk;
	public boolean m_bKeyRight = true;
	private static Context context;
	private Intent i;
	private static final int PERIOD = 30 * 1000; // 30 sec
	private PendingIntent pi = null;
	private AlarmManager mgr = null;
	public static boolean sDebuggerEnabled;
	public static GeoCoder geoCoder;

	/**
	 * Constructor.
	 */
	public BeemApplication() {
	}
	@Override
	public void onCreate() {
		super.onCreate();
		setMutileChannel();
		LogUtils.v("onCreate:" + getClass().getSimpleName());
		context = getApplicationContext();
		checkConnectionOnDemand(context);
		ImageLoaderConfigers.initImageLoader(this);
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		String login = SharedPrefsUtil.getValue(context,
				SettingKey.account_username, "");
		String password = SharedPrefsUtil.getValue(context,
				SettingKey.account_password, "");
		mIsAccountConfigured = !("".equals(login) || "".equals(password));
		mSettings.registerOnSharedPreferenceChangeListener(mPreferenceListener);
		instance = this;
		new BeemCrashHandler().init(context);
		SDKInitializer.initialize(this.getApplicationContext());
		geoCoder = GeoCoder.newInstance();
		startService(new Intent(this, BeemService.class));
		BeemServiceHelper.getInstance(this).bindBeemService();
	}
	private void setMutileChannel() {
		ApplicationInfo appInfo;
		String market = "";
		try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			market = appInfo.metaData.getString("PublishMarket");
			if ("DEBUG".equals(market)) {
				sDebuggerEnabled = true;
			} else {
				sDebuggerEnabled = false;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//重新加载应用配置
	public void reloadAppProperty() {
		AppProperty.getInstance().initConfig();
		/*if (AppProperty.getInstance().logServiceEnable) {
			startService(new Intent(this, LogService.class));
		}*/
	}
	public static Context getContext() {
		return context;
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		//		Log.w("aaron", "onLowMemory ");
	}
	@TargetApi(14)
	@Override
	public void onTrimMemory(int level) {
//		LogUtils.v("onTrimMemory " + getClass().getSimpleName());
		super.onTrimMemory(level);
	}
	@Override
	public void onTerminate() {
		LogUtils.v("onTerminate " + getClass().getSimpleName());
		BeemServiceHelper.getInstance(this).unBindBeemSerivice();
		super.onTerminate();
	}
	/*private void initGEO() {
		i = new Intent(this, LocationSynch.class);
		i.putExtra("username", getString(R.string.login_defaultname));
		mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(this, LocationPoller.class);
		Bundle bundle = new Bundle();
		LocationPollerParameter parameter = new LocationPollerParameter(bundle);
		parameter.setIntentToBroadcastOnCompletion(new Intent(this, LocationReceiver.class));
		// First try for GPS and fall back to NETWORK_PROVIDER
		parameter.setProviders(new String[] { LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER });
		parameter.setTimeout(6000);
		i.putExtras(bundle);
		pi = PendingIntent.getBroadcast(this, 0, i, 0);
		if (mSettings.getBoolean("gprs_checkbox_preference", false)) {
			if (pi != null && mgr != null) {
				mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PERIOD, pi);
				//				//LogUtils.i("mgr sent the wakeup service.");
			}
		}
	}*/
	/*private void cancle() {
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(getApplicationContext(), LocationPoller.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		mgr.cancel(pi);
	}*/
	public static void setNetWorkOk(boolean isOk) {
		LogUtils.i("setNetWorkOk " + isOk);
		mIsNetworkOk = isOk;
	}
	public static boolean isNetworkOk() {
		return mIsNetworkOk;
	}
	/**
	 * Tell if a XMPP account is configured.
	 * @return false if there is no account configured.
	 */
	public boolean isAccountConfigured() {
		return mIsAccountConfigured;
	}
	/**
	 * Enable Pep in the application context.
	 * @param enabled true to enable pep
	 */
	public void setPepEnabled(boolean enabled) {
		mPepEnabled = enabled;
	}
	/**
	 * Check if Pep is enabled.
	 * @return true if enabled
	 */
	public boolean isPepEnabled() {
		return mPepEnabled;
	}

	/**
	 * A listener for all the change in the preference file. It is used to maintain the global state
	 * of the application.
	 */
	private class PreferenceListener implements
			SharedPreferences.OnSharedPreferenceChangeListener {
		/**
		 * Constructor.
		 */
		public PreferenceListener() {
		}
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (BeemApplication.ACCOUNT_USERNAME_KEY.equals(key)
					|| BeemApplication.ACCOUNT_PASSWORD_KEY.equals(key)
					|| BeemApplication.USE_SYSTEM_ACCOUNT_KEY.equals(key)) {
				String login = SharedPrefsUtil.getValue(context,
						SettingKey.account_username, "");
				String password = SharedPrefsUtil.getValue(context,
						SettingKey.account_password, "");
				mIsAccountConfigured = !TextUtils.isEmpty(login)
						&& !TextUtils.isEmpty(password);
			}
			if (mSettings.getBoolean("gprs_checkbox_preference", false)) {
				if (mgr != null)
					mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
							SystemClock.elapsedRealtime(), PERIOD, pi);
			} else {
				if (mgr != null)
					mgr.cancel(pi);
			}
		}
	}

	public static String userUID;
	public static String[] facebookPermissions = { "offline_access",
			"publish_stream", "photo_upload" };
	private static BeemApplication instance;

	public static BeemApplication getInstance() {
		return instance;
	}
	// 检查是否网络可用
	public static void checkConnectionOnDemand(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info == null || info.getState() != State.CONNECTED) {
			BeemApplication.setNetWorkOk(false);
			//			//LogUtils.i("app网络不可用");
		} else {
			BeemApplication.setNetWorkOk(true);
			//			//LogUtils.i("app网络可用");
		}
	}
}
