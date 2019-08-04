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

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.entitycaps.SimpleDirectoryPersistentCache;
import org.jivesoftware.smackx.entitycaps.packet.CapsExtension;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.CapsExtensionProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.PEPProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.pubsub.provider.EventProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemProvider;
import org.jivesoftware.smackx.pubsub.provider.ItemsProvider;
import org.jivesoftware.smackx.pubsub.provider.PubSubProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.service.XmppConnectionAdapter;
import com.beem.project.btf.service.XmppFacade;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.service.auth.AccountAuthenticator;
import com.beem.project.btf.service.auth.PreferenceAuthenticator;
import com.beem.project.btf.smack.avatar.AvatarMetadataProvider;
import com.beem.project.btf.smack.avatar.AvatarProvider;
import com.beem.project.btf.smack.ping.PingExtension;
import com.beem.project.btf.smack.sasl.SASLGoogleOAuth2Mechanism;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.BeemBroadcastReceiver;
import com.beem.project.btf.utils.BeemBroadcastReceiver.OnNetworkAvailableListener;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.Status;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.AddRosterPacket;
import com.btf.push.AddRosterPacketProvider;
import com.btf.push.BlackRosterPacketProvider;
import com.btf.push.BlacklistedPacketProvider;
import com.btf.push.CityPacketProvider;
import com.btf.push.GPSPacketProvider;
import com.btf.push.GpsPacketExtension;
import com.btf.push.GpsPacketExtensionProvider;
import com.btf.push.ImageNotificationExtension;
import com.btf.push.ImageNotificationExtensionProvider;
import com.btf.push.Item;
import com.btf.push.NeighborHoodPacket;
import com.btf.push.NeighborHoodPacketProvider;
import com.btf.push.OfflineMsgPacket;
import com.btf.push.OfflineMsgPacketProvider;
import com.btf.push.PhoneContactPacket;
import com.btf.push.PhoneContactPacketProvider;
import com.btf.push.PhoneGetPacket;
import com.btf.push.SchoolMatePacket;
import com.btf.push.SchoolMatePacketProvider;
import com.btf.push.SchoolPacketProvider;
import com.btf.push.SynDataPacket;
import com.btf.push.SynDataPacketProvider;
import com.btf.push.UserInfoPacketProvider;
import com.btf.push.phoneGetPacketProvider;
import de.duenndns.ssl.MemorizingTrustManager;
import de.greenrobot.event.EventBus;

/**
 * This class is for the Beem service. It must contains every global informations needed to maintain
 * the background service. The connection to the xmpp server will be made asynchronously when the
 * service will start.
 * @author darisk
 */
public class BeemService extends Service implements IEventBusAction {
	/** The id to use for status notification. */
	public static final int NOTIFICATION_STATUS_ID = 100;
	public static final int NOTIFICATION_CHAT_ID = 101;
	public static final int NOTIFICATION_COMMENT_ID = 102;
	public static final int NOTIFICATION_FRIENDASK_ID = 103;
	public static final int NOTIFICATION_LIKE_ID = 104;
	public static final String XMPP_DISCONECT = "XMPP_DISCONECT";
	private static final String TAG = "BeemService";
	private NotificationManager mNotificationManager;
	private XmppConnectionAdapter mConnection;
	private SharedPreferences mSettings;
	private ConnectionConfiguration mConnectionConfiguration;
	private ProxyInfo mProxyInfo;
	private XmppFacade mBind;
	private BeemBroadcastReceiver mReceiver = new BeemBroadcastReceiver(this);
	private BeemServiceBroadcastReceiver mOnOffReceiver = new BeemServiceBroadcastReceiver();
	private BeemServicePreferenceListener mPreferenceListener = new BeemServicePreferenceListener();
	private boolean mOnOffReceiverIsRegistered;
	private SSLContext sslContext;
	private static BeemService INSTANCE;

	/**
	 * Initialize the connection.
	 */
	private void initConnectionConfig() {
		// TODO add an option for this ?
		//		//LogUtils.i("login#initConnectionConfig");
		String mHost = AppProperty.getInstance().XMPPSERVER_HOST;
		int mPort = AppProperty.getInstance().XMPPSERVER_PORT;
		String mService = AppProperty.getInstance().XMPPSERVER_SERVENAME;
		boolean mUseProxy = mSettings.getBoolean(BeemApplication.PROXY_USE_KEY,
				false);
		if (mUseProxy) {
			String stype = mSettings.getString(BeemApplication.PROXY_TYPE_KEY,
					"HTTP");
			String phost = mSettings.getString(
					BeemApplication.PROXY_SERVER_KEY, "");
			String puser = mSettings.getString(
					BeemApplication.PROXY_USERNAME_KEY, "");
			String ppass = mSettings.getString(
					BeemApplication.PROXY_PASSWORD_KEY, "");
			int pport = Integer.parseInt(mSettings.getString(
					BeemApplication.PROXY_PORT_KEY, "1080"));
			ProxyInfo.ProxyType type = ProxyType.valueOf(stype);
			mProxyInfo = new ProxyInfo(type, phost, pport, puser, ppass);
		} else {
			mProxyInfo = ProxyInfo.forNoProxy();
		}
		boolean useSystemAccount = mSettings.getBoolean(
				BeemApplication.USE_SYSTEM_ACCOUNT_KEY, false);
		if (mSettings.getBoolean("settings_key_specific_server", false))
			mConnectionConfiguration = new ConnectionConfiguration(mHost,
					mPort, mService, mProxyInfo);
		if (useSystemAccount) {
			SASLAuthentication
					.supportSASLMechanism(SASLGoogleOAuth2Mechanism.MECHANISM_NAME);
			String accountType = mSettings.getString(
					BeemApplication.ACCOUNT_SYSTEM_TYPE_KEY, "");
			String accountName = mSettings.getString(
					BeemApplication.ACCOUNT_USERNAME_KEY, "");
			Account account = getAccount(accountName, accountType);
			if (account == null) {
				mSettings.edit().putString(
						BeemApplication.ACCOUNT_USERNAME_KEY, "");
				mConnectionConfiguration = new ConnectionConfiguration(
						mService, mProxyInfo);
				mConnectionConfiguration
						.setCallbackHandler(new PreferenceAuthenticator(this));
			} else if ("com.google".equals(accountType)) {
				mConnectionConfiguration = new ConnectionConfiguration(
						"talk.google.com", 5222, mProxyInfo);
				//				//LogUtils.i("server:" + StringUtils.parseServer(accountName));
				mConnectionConfiguration.setServiceName(StringUtils
						.parseServer(accountName));
				mConnectionConfiguration
						.setCallbackHandler(new AccountAuthenticator(this,
								account));
				mConnectionConfiguration.setSecurityMode(SecurityMode.required);
			} else
				mConnectionConfiguration
						.setCallbackHandler(new AccountAuthenticator(this,
								account));
		} else {
			SASLAuthentication
					.unsupportSASLMechanism(SASLGoogleOAuth2Mechanism.MECHANISM_NAME);
			//			//LogUtils.i("mService:" + mService + " unsupportSASLMechanism= " + StringUtils.parseServer(mService));
			mConnectionConfiguration = new ConnectionConfiguration(mHost,
					mPort, mService, mProxyInfo);
			mConnectionConfiguration
					.setCallbackHandler(new PreferenceAuthenticator(this));
		}
		if (mSettings.getBoolean("settings_key_xmpp_tls_use", false)
				|| mSettings.getBoolean("settings_key_gmail", false)) {
			mConnectionConfiguration.setSecurityMode(SecurityMode.required);
		}
		//		if (mSettings.getBoolean(BeemApplication.SMACK_DEBUG_KEY, false))
		//			mConnectionConfiguration.setDebuggerEnabled(AppProperty.getInstance().debuggerEnabled);
		mConnectionConfiguration.setSendPresence(false);
		mConnectionConfiguration.setSendHeartBeat(true);
		mConnectionConfiguration.setRosterLoadedAtLogin(true);
		mConnectionConfiguration.setCompressionEnabled(AppProperty
				.getInstance().compressionEnable);
		// maybe not the universal path, but it works on most devices (Samsung
		// Galaxy, Google Nexus
		// One)
		mConnectionConfiguration.setTruststoreType("BKS");
		mConnectionConfiguration
				.setTruststorePath("/system/etc/security/cacerts.bks");
		if (sslContext != null)
			mConnectionConfiguration.setCustomSSLContext(sslContext);
		//		//LogUtils.i("login#BeemSerive connect host:" + mHost + " port:" + mPort + " mService:" + mService);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBinder onBind(Intent intent) {
		LogUtils.i("~onBind~");
		if (mBind == null) {
			mBind = new XmppFacade(this);
			//绑定之后再去监测网络  
			mReceiver
					.setOnNetworkAvailableListener(new OnNetworkAvailableListener() {
						/*@Override
						public void onNetworkUnavailable() {
							//					LogUtils.i("onNetworkUnavailable");
							BeemApplication.setOff(true);
							BeemApplication.setNetWorkOk(false);
							EventBus.getDefault().post(new EventBusData(EventAction.XmppConnectState, null));
							//					disconnect();
						}
						@Override
						public void onNetworkAvailable() {
							//					LogUtils.i("onNetworkAvailable");
							BeemApplication.setNetWorkOk(true);
							//当主类起来后，如果网络有切换才去做连接操作
							if (ActivityController.getInstance().isAcitivityAlive(ContactList.class)) {
								if (BeemApplication.getInstance().isAccountConfigured()) {
									try {
										//切换网络时也断开连接
										disconnect();
										String jid = LoginManager.getInstance().getToken();
										String pass = LoginManager.getInstance().getPassword();
										LoginHelper.login(mBind, jid, pass, new LoginHelper.OnLoginResult() {
											@Override
											public void onLoginSuccess() {
												//										//LogUtils.i("the network is avaliable,connect the connnection");
											}
											@Override
											public void onLoginFailed(XMPPException e) {
												//										//LogUtils.e("connect failed error_msg:" + e.getMessage());
												String error = BBSUtils.getXmppErrorMsg(BeemService.this, e);
												String error_login_authentication = getString(R.string.error_login_authentication);
												//LogUtils.e("error:" + error + " error_login_authentication:"
												//												+ error_login_authentication + " equals?"
												//												+ error.equals(error_login_authentication));
												if (error.equals(error_login_authentication)) {
													//账号密码已过期
													SharedPrefsUtil.getValue(BeemService.this, SettingKey.account_password, "");
													showReloginDlg(error_login_authentication);
												}
											}
										});
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									//							//LogUtils.i("invalid account info cannot connect the server.");
								}
							} else {
								//						//LogUtils.i("the Contactlist is not foreground not reconnect.");
							}
						}*/
						@Override
						public void onConnectionClosed(Intent intent) {
							CharSequence message = intent
									.getCharSequenceExtra("message");
//							LogUtils.i("connection closed,message:" + message);
							if (message != null
									&& message
											.equals("stream:error (conflict)")) {
								// 账号被抢登
								LogUtils.e("connection closed,stream:error:" + message);
								showReloginDlg(getResources().getString(
										R.string.login_out_of_city));
							} else {
								//						EventBus.getDefault().post(new EventBusData(EventAction.XmppConnectState, null));
							}
						}
					});
		}
		return mBind;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		LogUtils.i("~onUnbind~");
		if (mConnection != null && !mConnection.getAdaptee().isConnected()) {
			mConnection.disconnect();
		}
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// 注册广播接收器[networkstate,sp监听器，]
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED);
		registerReceiver(mReceiver, filter);
		IntentFilter timefilter = new IntentFilter();
		timefilter.addAction(Intent.ACTION_TIME_CHANGED);
		timefilter.addAction(Intent.ACTION_DATE_CHANGED);
		registerReceiver(mTimeChangeReceiver, timefilter);
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		mSettings.registerOnSharedPreferenceChangeListener(mPreferenceListener);
		if (mSettings.getBoolean(BeemApplication.USE_AUTO_AWAY_KEY, false)) {
			mOnOffReceiverIsRegistered = true;
			registerReceiver(mOnOffReceiver, new IntentFilter(
					Intent.ACTION_SCREEN_OFF));
			registerReceiver(mOnOffReceiver, new IntentFilter(
					Intent.ACTION_SCREEN_ON));
		}
		initMemorizingTrustManager();
		configure(ProviderManager.getInstance());
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
		INSTANCE = this;
		EventBus.getDefault().register(this);
	}
	public static BeemService getInstance() {
		return INSTANCE;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i("~onDestroy BeemService~");
		mNotificationManager.cancelAll();
		unregisterReceiver(mReceiver);
		unregisterReceiver(mTimeChangeReceiver);
		mSettings
				.unregisterOnSharedPreferenceChangeListener(mPreferenceListener);
		if (mOnOffReceiverIsRegistered)
			unregisterReceiver(mOnOffReceiver);
		EventBus.getDefault().unregister(this);
		INSTANCE = null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * Create the XmppConnectionAdapter. This method makes a network request so it must not be
	 * called on the main thread.
	 * @return the connection
	 */
	public XmppConnectionAdapter createConnection() {
		if (mConnection == null) {
			initConnectionConfig();
			String mLogin = SharedPrefsUtil.getValue(
					BeemApplication.getContext(), SettingKey.account_username,
					"");
			String mPassword = SharedPrefsUtil.getValue(
					BeemApplication.getContext(), SettingKey.account_password,
					"");
			mConnection = new XmppConnectionAdapter(mConnectionConfiguration,
					mLogin, mPassword, this,
					AppProperty.getInstance().XMPPSERVER_RESOUCE);
		}
		return mConnection;
	}
	public XmppConnectionAdapter createConnection(String mLogin,
			String mPassword) {
		if (mConnection == null) {
			initConnectionConfig();
			mConnection = new XmppConnectionAdapter(mConnectionConfiguration,
					mLogin, mPassword, this,
					AppProperty.getInstance().XMPPSERVER_RESOUCE);
		}
		mConnection.setLoginInfo(mLogin, mPassword,
				AppProperty.getInstance().XMPPSERVER_RESOUCE);
		return mConnection;
	}
	/**
	 * Show a notification using the preference of the user.
	 * @param id the id of the notification.
	 * @param notif the notification to show
	 */
	public void sendNotification(int id, Notification notif) {
		if (mSettings
				.getBoolean(BeemApplication.NOTIFICATION_VIBRATE_KEY, true))
			notif.defaults |= Notification.DEFAULT_VIBRATE;
		notif.ledARGB = 0xff0000ff; // Blue color
		notif.ledOnMS = 1000;
		notif.ledOffMS = 1000;
		notif.flags |= Notification.FLAG_SHOW_LIGHTS;
		String ringtoneStr = mSettings.getString(
				BeemApplication.NOTIFICATION_SOUND_KEY,
				Settings.System.DEFAULT_NOTIFICATION_URI.toString());
		notif.sound = Uri.parse(ringtoneStr);
		mNotificationManager.notify(id, notif);
	}
	// 发送通知到状态栏
	public void sendNotificationEx(int id, Notification notif) {
		// notif.defaults |= Notification.DEFAULT_VIBRATE;
		notif.ledARGB = 0xff0000ff; // Blue color
		notif.ledOnMS = 1000;
		notif.ledOffMS = 1000;
		notif.flags |= Notification.FLAG_SHOW_LIGHTS;
		// String ringtoneStr =
		// mSettings.getString(BeemApplication.NOTIFICATION_SOUND_KEY,
		// Settings.System.DEFAULT_NOTIFICATION_URI.toString());
		// notif.sound = Uri.parse(ringtoneStr);
		mNotificationManager.notify(id, notif);
	}
	public void vibrateAndRing(boolean isVibrate, boolean isSound) {
		if (isVibrate) {
			vibrateNotice();
		}
		if (isSound) {
			ringNotice();
		}
	}
	private void vibrateNotice() {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(100);
	}
	private void ringNotice() {
		MediaPlayer mPlayer = new MediaPlayer();
		try {
			AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			mPlayer.setDataSource(this, RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			mPlayer.prepare();
			int current = mAudioManager
					.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
			mPlayer.setVolume(current, current);
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					stopPlaying(mp);
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			stopPlaying(mPlayer);
		}
	}
	private void stopPlaying(MediaPlayer mPlayer) {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
		}
	}
	/**
	 * Delete a notification.
	 * @param id the id of the notification
	 */
	public void deleteNotification(int id) {
		mNotificationManager.cancel(id);
	}
	public void deleteNotificationAll() {
		mNotificationManager.cancel(BeemService.NOTIFICATION_CHAT_ID);
		mNotificationManager.cancel(BeemService.NOTIFICATION_FRIENDASK_ID);
		mNotificationManager.cancel(BeemService.NOTIFICATION_COMMENT_ID);
	}
	/**
	 * Reset the status to online after a disconnect.
	 */
	public void resetStatus() {
		Editor edit = mSettings.edit();
		edit.putInt(BeemApplication.STATUS_KEY, 1);
		edit.commit();
	}
	/**
	 * Initialize Jingle from an XmppConnectionAdapter.
	 * @param adaptee XmppConnection used for jingle.
	 */
	public void initJingle(XMPPConnection adaptee) {
	}
	/**
	 * Return a bind to an XmppFacade instance.
	 * @return IXmppFacade a bind to an XmppFacade instance
	 */
	public IXmppFacade getBind() {
		return mBind;
	}
	/**
	 * Get the preference of the service.
	 * @return the preference
	 */
	public SharedPreferences getServicePreference() {
		return mSettings;
	}
	/**
	 * Get the notification manager system service.
	 * @return the notification manager service.
	 */
	public NotificationManager getNotificationManager() {
		return mNotificationManager;
	}
	/**
	 * Utility method to create and make a connection asynchronously.
	 */
	private synchronized void createConnectAsync() {
		if (mConnection == null) {
			ThreadUtils.executeTask(new Runnable() {
				@Override
				public void run() {
					createConnection();
					connectAsync();
				}
			});
		} else {
			connectAsync();
		}
	}
	/**
	 * Utility method to connect asynchronously.
	 */
	private void connectAsync() {
		try {
			mConnection.connectAsync();
		} catch (RemoteException e) {
			Log.w(TAG, "unable to connect", e);
		}
	}
	/**
	 * Get the specified Android account.
	 * @param accountName the account name
	 * @param accountType the account type
	 * @return the account or null if it does not exist
	 */
	private Account getAccount(String accountName, String accountType) {
		AccountManager am = AccountManager.get(this);
		for (Account a : am.getAccountsByType(accountType)) {
			if (a.name.equals(accountName)) {
				return a;
			}
		}
		return null;
	}
	/**
	 * Install the MemorizingTrustManager in the ConnectionConfiguration of Smack.
	 */
	private void initMemorizingTrustManager() {
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, MemorizingTrustManager.getInstanceList(this),
					new java.security.SecureRandom());
		} catch (GeneralSecurityException e) {
			Log.w(TAG, "Unable to use MemorizingTrustManager", e);
		}
	}
	/**
	 * A sort of patch from this thread: http://www.igniterealtime.org/community/thread/31118. Avoid
	 * ClassCastException by bypassing the classloading shit of Smack.
	 * @param pm The ProviderManager.
	 */
	private void configure(ProviderManager pm) {
		Log.d(TAG, "configure");
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Privacy
		// pm.addIQProvider("query", "jabber:iq:privacy", new
		// PrivacyProvider());
		// Delayed Delivery only the new version
		pm.addExtensionProvider("delay", "urn:xmpp:delay",
				new DelayInfoProvider());
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Chat State
		ChatStateExtension.Provider chatState = new ChatStateExtension.Provider();
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates", chatState);
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates", chatState);
		// capabilities
		pm.addExtensionProvider(CapsExtension.NODE_NAME, CapsExtension.XMLNS,
				new CapsExtensionProvider());
		// Pubsub
		pm.addIQProvider("pubsub", "http://jabber.org/protocol/pubsub",
				new PubSubProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub",
				new ItemsProvider());
		pm.addExtensionProvider("items", "http://jabber.org/protocol/pubsub",
				new ItemsProvider());
		pm.addExtensionProvider("item", "http://jabber.org/protocol/pubsub",
				new ItemProvider());
		pm.addExtensionProvider("items",
				"http://jabber.org/protocol/pubsub#event", new ItemsProvider());
		pm.addExtensionProvider("item",
				"http://jabber.org/protocol/pubsub#event", new ItemProvider());
		pm.addExtensionProvider("event",
				"http://jabber.org/protocol/pubsub#event", new EventProvider());
		// TODO rajouter les manquants pour du full pubsub
		// PEP avatar
		pm.addExtensionProvider("metadata", "urn:xmpp:avatar:metadata",
				new AvatarMetadataProvider());
		pm.addExtensionProvider("data", "urn:xmpp:avatar:data",
				new AvatarProvider());
		PEPProvider pep = new PEPProvider();
		AvatarMetadataProvider avaMeta = new AvatarMetadataProvider();
		pep.registerPEPParserExtension("urn:xmpp:avatar:metadata", avaMeta);
		pm.addExtensionProvider("event",
				"http://jabber.org/protocol/pubsub#event", pep);
		// ping
		pm.addIQProvider(PingExtension.ELEMENT, PingExtension.NAMESPACE,
				PingExtension.class);
		// {
		pm.addExtensionProvider(GpsPacketExtension.ELEMENT,
				GpsPacketExtension.NAMESPACE, new GpsPacketExtensionProvider());
		pm.addExtensionProvider(ImageNotificationExtension.ELEMENT,
				ImageNotificationExtension.NAMESPACE,
				new ImageNotificationExtensionProvider());
		// pm.addExtensionProvider(BlackRosterPacketExtension.ELEMENT,
		// BlackRosterPacketExtension.NAMESPACE, new
		// BlackRosterPacketExtensionProvider());//20130414
		// 20130408pm.addIQProvider("vCard", "stranger-temp", new
		// GPSPacketProvider());
		pm.addIQProvider("vCard", "distance", new GPSPacketProvider());
		pm.addIQProvider("vCard", "school", new SchoolPacketProvider());
		pm.addIQProvider("vCard", "city", new CityPacketProvider());
		// pm.addIQProvider("vCard", "info", new
		// com.btf.push.VCardProvider());//20130311
		// pm.addIQProvider("vCard", "info", new
		// com.btf.push.VCardProvider());// 20130311
		pm.addIQProvider("query", "blacklist", new BlackRosterPacketProvider());
		pm.addIQProvider("query", "blacklisted",
				new BlacklistedPacketProvider());
		pm.addIQProvider(PhoneContactPacket.element, PhoneContactPacket.xmlns,
				new PhoneContactPacketProvider());
		pm.addIQProvider("vCard", "info", new UserInfoPacketProvider());
		pm.addIQProvider(AddRosterPacket.element, AddRosterPacket.xmlns,
				new AddRosterPacketProvider());
		pm.addIQProvider(NeighborHoodPacket.element, NeighborHoodPacket.xmlns,
				new NeighborHoodPacketProvider());
		pm.addIQProvider(SchoolMatePacket.element, SchoolMatePacket.xmlns,
				new SchoolMatePacketProvider());
		pm.addIQProvider(PhoneGetPacket.element, PhoneGetPacket.xmlns,
				new phoneGetPacketProvider());
		pm.addIQProvider(SynDataPacket.element, SynDataPacket.xmlns,
				new SynDataPacketProvider());
		pm.addIQProvider(OfflineMsgPacket.element, OfflineMsgPacket.xmlns,
				new OfflineMsgPacketProvider());
		// }
		// 20121129{/*
		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Version");
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());// 20130311
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		// FileTransfer
		// pm.addIQProvider("si", "http://jabber.org/protocol/si", new
		// StreamInitiationProvider());
		// pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
		// new
		// BytestreamsProvider());
		// pm.addIQProvider("open", "http://jabber.org/protocol/ibb", new
		// IBBProviders.Open());
		// pm.addIQProvider("close", "http://jabber.org/protocol/ibb", new
		// IBBProviders.Close());
		// pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb", new
		// IBBProviders.Data());
		final String COMMAND_NAMESPACE = "http://jabber.org/protocol/commands";
		pm.addIQProvider("command", COMMAND_NAMESPACE,
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action", COMMAND_NAMESPACE,
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale", COMMAND_NAMESPACE,
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload", COMMAND_NAMESPACE,
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid", COMMAND_NAMESPACE,
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired", COMMAND_NAMESPACE,
				new AdHocCommandDataProvider.SessionExpiredError());
		// }20121129*/
		/* register additionnals sasl mechanisms */
		SASLAuthentication.registerSASLMechanism(
				SASLGoogleOAuth2Mechanism.MECHANISM_NAME,
				SASLGoogleOAuth2Mechanism.class);
		// Configure entity caps manager. This must be done only once
		File f = new File(getCacheDir(), "entityCaps");
		f.mkdirs();
		try {
			EntityCapsManager
					.setPersistentCache(new SimpleDirectoryPersistentCache(f));
		} catch (IllegalStateException e) {
			Log.v(TAG, "EntityCapsManager already initialized", e);
		} catch (IOException e) {
			Log.w(TAG, "EntityCapsManager not able to reuse persistent cache");
		}
	}

	/**
	 * Listen on preference changes.
	 */
	private class BeemServicePreferenceListener implements
			SharedPreferences.OnSharedPreferenceChangeListener {
		public BeemServicePreferenceListener() {
		}
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (BeemApplication.USE_AUTO_AWAY_KEY.equals(key)) {
				if (sharedPreferences.getBoolean(
						BeemApplication.USE_AUTO_AWAY_KEY, false)) {
					mOnOffReceiverIsRegistered = true;
					registerReceiver(mOnOffReceiver, new IntentFilter(
							Intent.ACTION_SCREEN_OFF));
					registerReceiver(mOnOffReceiver, new IntentFilter(
							Intent.ACTION_SCREEN_ON));
				} else {
					mOnOffReceiverIsRegistered = false;
					unregisterReceiver(mOnOffReceiver);
				}
			}
		}
	}

	/**
	 * Listen on some Intent broadcast, ScreenOn and ScreenOff.
	 */
	private class BeemServiceBroadcastReceiver extends BroadcastReceiver {
		private String mOldStatus;
		private int mOldMode;

		/**
		 * Constructor.
		 */
		public BeemServiceBroadcastReceiver() {
		}
		@Override
		public void onReceive(final Context context, final Intent intent) {
			String intentAction = intent.getAction();
			//			//LogUtils.i("BeemServiceBroadcastReceiver:" + intentAction);
			if (intentAction.equals(Intent.ACTION_SCREEN_OFF)) {
				mOldMode = mConnection.getPreviousMode();
				mOldStatus = mConnection.getPreviousStatus();
				if (mConnection.isAuthentificated())
					mConnection.changeStatus(Status.CONTACT_STATUS_AWAY,
							mSettings.getString(
									BeemApplication.AUTO_AWAY_MSG_KEY, "Away"));
			} else if (intentAction.equals(Intent.ACTION_SCREEN_ON)) {
				if (mConnection.isAuthentificated())
					mConnection.changeStatus(mOldMode, mOldStatus);
			}
		}
	}

	private BroadcastReceiver mTimeChangeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)
					|| intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
				// 系统时间存在变更,重新获取服务器系统时间
				if (mConnection != null)
					mConnection.synSystemTime();
			}
		}
	};

	@Override
	public void onEventMainThread(EventBusData data) {
		switch (data.getAction()) {
			case MessageChatOpen: {
				Item item = (Item) data.getMsg();
				mConnection.getMessageManager().readMessage(item);
				break;
			}
			case MessageFriendAskOpen: {
				Item item = (Item) data.getMsg();
				mConnection.getMessageManager().readMessage(item);
				break;
			}
			case MessageComment: {
				Item item = (Item) data.getMsg();
				mConnection.getMessageManager().readMessage(item);
				break;
			}
			default:
				break;
		}
	}
	// 弹出重新登录dialog
	private void showReloginDlg(String prompt) {
		Activity act = ActivityController.getInstance().getCurrentActivity();
		if (act == null) {
			//			//LogUtils.i("the current activity is null, so can not show relogin dialog.");
			return;
		}
		if (act.isFinishing()) {
			//出现重新登录的弹出框的Activity可能正在销毁
			//			//LogUtils.v("act_current:" + act.getClass().getSimpleName());
			act = ActivityController.getInstance().getBottomActivity();
		}
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(act,
				R.style.blurdialog);
		TextView promptTextView = new TextView(act);
		promptTextView.setText(prompt);
		promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		promptTextView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		promptTextView
				.setPadding((int) BBSUtils.toPixel(act,
						TypedValue.COMPLEX_UNIT_DIP, 10), 0, (int) BBSUtils
						.toPixel(act, TypedValue.COMPLEX_UNIT_DIP, 10), 0);
		SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(act);
		dilaogView.setTitle("重新登录:");
		dilaogView.setContentView(promptTextView);
		dilaogView.setMargin();
		dilaogView.setPositiveButton("重新登录", new BtnListener() {
			@Override
			public void ensure(View contentView) {
				blurDlg.dismiss();
				ActivityController.getInstance().relogin();
			}
		});
		blurDlg.setContentView(dilaogView.getmView());
		blurDlg.setCancelable(false);
		blurDlg.show();
		//		//LogUtils.v("showReloginDlg");
	}
}
