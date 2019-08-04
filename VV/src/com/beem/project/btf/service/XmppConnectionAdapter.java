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
package com.beem.project.btf.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.PrivacyListManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ChatStateManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.entitycaps.EntityCapsManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.pubsub.Subscription;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.BeemService;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.aidl.IBeemConnectionListener;
import com.beem.project.btf.service.aidl.IChatManager;
import com.beem.project.btf.service.aidl.IConnectionListener;
import com.beem.project.btf.service.aidl.IRoster;
import com.beem.project.btf.service.aidl.IXmppConnection;
import com.beem.project.btf.smack.avatar.AvatarCache;
import com.beem.project.btf.smack.avatar.AvatarListener;
import com.beem.project.btf.smack.avatar.AvatarMetadataExtension;
import com.beem.project.btf.smack.pep.PepSubManager;
import com.beem.project.btf.smack.ping.PingExtension;
import com.beem.project.btf.utils.BeemBroadcastReceiver;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.Status;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.BindPacketListener;
import com.btf.push.GpsPacketExtensionPacketListener;
import com.btf.push.SynDataPacket;
import com.btf.push.base.BaseIQ.BaseIQKey;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.Debug;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

/**
 * This class implements an adapter for XMPPConnection.
 * @author darisk
 */
public class XmppConnectionAdapter extends IXmppConnection.Stub {
	/**
	 * Beem connection closed Intent name.
	 */
	private static final int SMACK_PRIORITY_MIN = -128;
	private static final int SMACK_PRIORITY_MAX = 128;
	private static final String TAG = "XMPPConnectionAdapter";
	private XMPPConnection mXmppConn;
	private BeemChatManager mChatManager;
	private MessageManager mMessageManager;
	private String mLogin;
	private String mPassword;
	private String mResource;
	private String mErrorMsg;
	private RosterAdapter mRosterAdap;
	private int mPreviousPriority;
	private int mPreviousMode;
	private String mPreviousStatus;
	private PrivacyListManagerAdapter mPrivacyListManager;
	private ChatStateManager mChatStateManager;
	private final BeemService mService;
	private BeemApplication mApplication;
	private BeemAvatarManager mAvatarManager;
	private PepSubManager mPepManager;
	private SharedPreferences mPref;
	private final RemoteCallbackList<IBeemConnectionListener> mRemoteConnListeners = new RemoteCallbackList<IBeemConnectionListener>();
	private final SubscribePacketListener mSubscribePacketListener = new SubscribePacketListener();
	private final PingListener mPingListener = new PingListener();
	private final NotificationPacketListener notificationPacketListener = new NotificationPacketListener();
	private final GpsPacketExtensionPacketListener gpsPacketExtensionPacketListener = new GpsPacketExtensionPacketListener();
	private final BindPacketListener bindListener = new BindPacketListener();
	private final ConnexionListenerAdapter mConListener = new ConnexionListenerAdapter();
	private UserInfo mUserInfo;
	private final UserInfoManager mUserInfoManager = new UserInfoManager();

	/**
	 * Constructor.
	 * @param config Configuration to use in order to connect
	 * @param login login to use on connect
	 * @param password password to use on connect
	 * @param service the background service associated with the connection.
	 */
	public XmppConnectionAdapter(final ConnectionConfiguration config,
			final String login, final String password,
			final BeemService service, final String resources) {
		this(new XMPPConnection(config), login, password, service, resources);
	}
	/**
	 * Constructor.
	 * @param con The connection to adapt
	 * @param login The login to use
	 * @param password The password to use
	 * @param service the background service associated with the connection.
	 */
	public XmppConnectionAdapter(final XMPPConnection con, final String login,
			final String password, final BeemService service,
			final String resource) {
		mXmppConn = con;
		ContactService.getInstance().initXMMPConnnection(con);
		PrivacyListManager.getInstanceFor(mXmppConn);
		mLogin = login;
		mPassword = password;
		mService = service;
		mResource = resource;
		Context ctx = mService.getApplicationContext();
		if (ctx instanceof BeemApplication) {
			mApplication = (BeemApplication) ctx;
		}
		mPref = mService.getServicePreference();
		try {
			mPreviousPriority = Integer.parseInt(mPref.getString(
					BeemApplication.CONNECTION_PRIORITY_KEY, "0"));
		} catch (NumberFormatException ex) {
			mPreviousPriority = 0;
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addConnectionListener(IBeemConnectionListener listen)
			throws RemoteException {
		if (listen != null)
			mRemoteConnListeners.register(listen);
	}
	@Override
	public boolean connect() throws RemoteException {
		if (mXmppConn.isConnected())
			return true;
		else {
			try {
				mXmppConn.connect();
				mXmppConn.addConnectionListener(mConListener);
				return true;
			} catch (XMPPException e) {
				try {
					// TODO NIKITA DOES SOME SHIT !!! Fix this monstruosity
					String str = mService.getResources().getString(
							mService.getResources().getIdentifier(
									e.getXMPPError().getCondition()
											.replace("-", "_"), "string",
									"com.beem.project.btf"));
					mErrorMsg = str;
				} catch (NullPointerException e2) {
					if (!"".equals(e.getMessage()))
						mErrorMsg = e.getMessage();
					else
						mErrorMsg = e.toString();
				}
			}
			return false;
		}
	}
	public boolean connectEx() throws RemoteException {
		if (!BeemApplication.isNetworkOk()) {
			// 如果网络不通不要连接操作
			//LogUtils.i("connect~~2");
			return false;
		}
		if (mXmppConn.isConnected()) {
			//LogUtils.i("connect~~3");
			return true;
		} else {
			try {
				mXmppConn.connect();
				mXmppConn.addConnectionListener(mConListener);
				return true;
			} catch (XMPPException e) {
				try {
					// TODO NIKITA DOES SOME SHIT !!! Fix this monstruosity
					String str = mService.getResources().getString(
							mService.getResources().getIdentifier(
									e.getXMPPError().getCondition()
											.replace("-", "_"), "string",
									"com.beem.project.btf"));
					mErrorMsg = str;
				} catch (NullPointerException e2) {
					if (!"".equals(e.getMessage()))
						mErrorMsg = e.getMessage();
					else
						mErrorMsg = e.toString();
				}
				// wifi连上了但连接不上主机 ,返回false
				return false;
			}
		}
	}
	@Override
	public boolean login() throws RemoteException {
		if (mXmppConn.isAuthenticated()) {
			return true;
		}
		if (!mXmppConn.isConnected()) {
			return false;
		}
		try {
			this.initFeatures(); // pour declarer les features xmpp qu'on
			// supporte
			PacketFilter filter = new PacketFilter() {
				@Override
				public boolean accept(Packet packet) {
					if (packet instanceof Presence) {
						Presence pres = (Presence) packet;
						if (pres.getType() == Presence.Type.subscribe)
							return true;
					}
					return false;
				}
			};
			mXmppConn.addPacketListener(mSubscribePacketListener, filter);
			mXmppConn.addPacketListener(mPingListener, new PacketTypeFilter(
					PingExtension.class));
			mXmppConn.addPacketListener(notificationPacketListener,
					new PacketTypeFilter(Message.class));
			mXmppConn.addPacketListener(gpsPacketExtensionPacketListener,
					new PacketTypeFilter(Presence.class));
			mXmppConn.addPacketListener(gpsPacketExtensionPacketListener,
					new PacketTypeFilter(Presence.class));
			//LogUtils.i("~mLogin~" + mLogin + "~mPassword~" + mPassword + "~mResource~" + mResource);
			mXmppConn.login(mLogin, mPassword, mResource);
			mUserInfo = new UserInfo(mXmppConn.getUser());
			// save the login info to the SharedPreference
			mChatManager = new BeemChatManager(mXmppConn.getChatManager(),
					mService);
			mMessageManager = new MessageManager(mXmppConn, mService,
					mChatManager);
			mChatManager.setMessageManager(mMessageManager);
			// nikita: I commented this line because of the logs provided in
			// http://www.beem-project.com/issues/321
			// Also, since the privacylistmanager isn't finished and used, it
			// will be safer to not
			// initialize it
			// mPrivacyListManager = new
			// PrivacyListManagerAdapter(PrivacyListManager.getInstanceFor(mAdaptee));
			mService.initJingle(mXmppConn);
			// discoverServerFeatures();
			mRosterAdap = new RosterAdapter(mXmppConn.getRoster(), mService,
					mAvatarManager);
			mRosterAdap.setMyJid(mLogin);
			int mode = mPref.getInt(BeemApplication.STATUS_KEY, 0);
			String status = mPref
					.getString(BeemApplication.STATUS_TEXT_KEY, "");
			changeStatus(mode, status);
			synSystemTime();
			return true;
		} catch (XMPPException e) {
			mErrorMsg = mService.getString(R.string.error_login_authentication);
			return false;
		}
	}
	public String loginEx() throws RemoteException {
		try {
			this.initFeatures(); // pour declarer les features xmpp qu'on
			// supporte
			PacketFilter filter = new PacketFilter() {
				@Override
				public boolean accept(Packet packet) {
					if (packet instanceof Presence) {
						Presence pres = (Presence) packet;
						if (pres.getType() == Presence.Type.subscribe)
							return true;
					}
					return false;
				}
			};
			mXmppConn.addPacketListener(mSubscribePacketListener, filter);
			mXmppConn.addPacketListener(mPingListener, new PacketTypeFilter(
					PingExtension.class));
			mXmppConn.addPacketListener(notificationPacketListener,
					new PacketTypeFilter(Message.class));
			mXmppConn.addPacketListener(gpsPacketExtensionPacketListener,
					new PacketTypeFilter(Presence.class));
			mXmppConn.addPacketListener(bindListener, new PacketTypeFilter(Bind.class));
			mXmppConn.login(mLogin, mPassword, mResource);
			mChatManager = new BeemChatManager(mXmppConn.getChatManager(),
					mService);
			mMessageManager = new MessageManager(mXmppConn, mService,
					mChatManager);
			mChatManager.setMessageManager(mMessageManager);
			mUserInfo = new UserInfo(mXmppConn.getUser());
			mService.initJingle(mXmppConn);
			// discoverServerFeatures();
			mRosterAdap = new RosterAdapter(mXmppConn.getRoster(), mService,
					mAvatarManager);
			mRosterAdap.setMyJid(mLogin);
			int mode = mPref.getInt(BeemApplication.STATUS_KEY, 0);
			String status = mPref
					.getString(BeemApplication.STATUS_TEXT_KEY, "");
			changeStatus(mode, status);
			synSystemTime();
			//			ContactService.getInstance().getFriendlist(true);
			LoginManager.getInstance().getMyContactInfoAsync(mLogin);
			//			ContactService.getInstance().getBlacklist(true);
			//			sendWelcomeMessage();
			// notify the observer to login success.
			return null;
		} catch (XMPPException e) {
			//			mErrorMsg = mService.getString(R.string.error_login_authentication);
			disconnect();
			try {
				return e.getXMPPError().getCondition();
			} catch (Exception e2) {
				return XMPPError.Condition.error_login_authentication.value;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return XMPPError.Condition.error_login_authentication.value;
		}
	}
	// 给自己发送一个欢迎的Message
	public void sendWelcomeMessage() {
		if (!SharedPrefsUtil.getValue(mService, SettingKey.sendWelcomeMessage,
				false)) {
			SharedPrefsUtil.putValue(mService, SettingKey.sendWelcomeMessage,
					true);
			com.beem.project.btf.service.Message msgToSend = new com.beem.project.btf.service.Message(
					LoginManager.getInstance().getJidCompleted(),
					com.beem.project.btf.service.Message.MSG_TYPE_CHAT);
			msgToSend.setTimestampStr(LoginManager.getInstance()
					.getSystemTime());
			msgToSend.setTo(LoginManager.getInstance().getJidCompleted());
			Message msg = msgToSend.toSmackMessage();
			msg.setFrom(VVXMPPUtils.makeJidCompleted(Constants.GENER_NUM));
			if (LoginManager.getInstance().getMyContact()
					.getFieldStr(DBKey.onlinetime).equals("0")) {
				msg.setBody("欢迎使用时光机");
				mXmppConn.injectPacketToReader(msg);
			} else {
				msg.setBody("欢迎回来");
				mXmppConn.injectPacketToReader(msg);
			}
		}
	}
	/**
	 * @Title: synSystemTime
	 * @Description: 同步系统时间
	 * @return: void
	 */
	public void synSystemTime() {
		if (!isAuthentificated()) {
			// 离线的话用上次本地的
			LoginManager.getInstance().synSystemTime(
					SharedPrefsUtil.getValue(BeemApplication.getContext(),
							SettingKey.systemTimeDeltalMils, 0L));
		} else {
			// 在登录或者本机时间变更时，向服务器获取一次系统时间
			SynDataPacket sendPacket = new SynDataPacket();
			SynDataPacket itemPacket = new SynDataPacket();
			itemPacket.setField(BaseIQKey.xmlns.toString(),
					BaseIQKey.syndata_systime.val);
			List<SynDataPacket> itemPackets = new ArrayList<SynDataPacket>();
			itemPackets.add(itemPacket);
			sendPacket.setField(BaseIQKey.item, itemPackets);
			mXmppConn.sendPacket(sendPacket);
			mXmppConn.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					SynDataPacket synPacket = (SynDataPacket) packet;
					if (synPacket != null
							&& synPacket.getType() == org.jivesoftware.smack.packet.IQ.Type.RESULT) {
						List<SynDataPacket> packetList = synPacket.getField(
								BaseIQKey.item, List.class);
						mXmppConn.removePacketListener(this);
						String systemTime = packetList.get(0).getField(
								BaseIQKey.digest, String.class);
						SimpleDateFormat sf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.US);
						try {
							long systemTimeLong = sf.parse(systemTime)
									.getTime();
							long localTimeLong = System.currentTimeMillis();
							//LogUtils.i("synSystemTime:" + systemTime);
							LoginManager.getInstance().synSystemTime(
									systemTimeLong - localTimeLong);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}, new PacketTypeFilter(SynDataPacket.class));
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void connectAsync() throws RemoteException {
		if (mXmppConn.isConnected() || mXmppConn.isAuthenticated()) {
			return;
		}
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				try {
					connectSync();
				} catch (RemoteException e) {
				}
			}
		});
	}
	public final void connectAsync(final IConnectionListener lis)
			throws RemoteException {
		if (mXmppConn.isConnected() && mXmppConn.isAuthenticated()) {
			lis.onResult(null);
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final String result = connectSyncEx();
					lis.onResult(result);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean connectSync() throws RemoteException {
		boolean result = false;
		if (connectEx())
			result = TextUtils.isEmpty(loginEx());
		return result;
	}
	public String connectSyncEx() throws RemoteException {
		if (connectEx()) {
			return loginEx();
		}
		return Condition.remote_server_timeout.value;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeStatusAndPriority(int status, String msg, int priority) {
		Presence pres = new Presence(Presence.Type.available);
		String m;
		if (msg != null)
			m = msg;
		else
			m = mPreviousStatus;
		pres.setStatus(m);
		mPreviousStatus = m;
		Presence.Mode mode = Status.getPresenceModeFromStatus(status);
		if (mode != null) {
			pres.setMode(mode);
			mPreviousMode = status;
		} else {
			pres.setMode(Status.getPresenceModeFromStatus(mPreviousMode));
		}
		int p = priority;
		if (priority < SMACK_PRIORITY_MIN)
			p = SMACK_PRIORITY_MIN;
		if (priority > SMACK_PRIORITY_MAX)
			p = SMACK_PRIORITY_MAX;
		mPreviousPriority = p;
		pres.setPriority(p);
		mXmppConn.sendPacket(pres);
		// 屏蔽掉上线通知提醒
		// updateNotification(Status.getStatusFromPresence(pres), m);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeStatus(int status, String msg) {
		changeStatusAndPriority(status, msg, mPreviousPriority);
	}
	/**
	 * @return
	 */
	public String getXMPPUser() {
		return mXmppConn.getUser();
	}
	/**
	 * Get the AvatarManager of this connection.
	 * @return the AvatarManager or null if there is not
	 */
	public BeemAvatarManager getAvatarManager() {
		return mAvatarManager;
	}
	public XMPPConnection getXmppConnection() {
		return mXmppConn;
	}
	/**
	 * get the previous status.
	 * @return previous status.
	 */
	public String getPreviousStatus() {
		return mPreviousStatus;
	}
	/**
	 * get the previous mode.
	 * @return previous mode.
	 */
	public int getPreviousMode() {
		return mPreviousMode;
	}
	public void addVVPacketListener(PacketListener arg0, PacketFilter arg1) {
		mXmppConn.addPacketListener(arg0, arg1);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean disconnect() {
		if (mXmppConn != null && mXmppConn.isConnected()) {
			try {
				mXmppConn.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * Get the Smack XmppConnection.
	 * @return Smack XmppConnection
	 */
	public XMPPConnection getAdaptee() {
		return mXmppConn;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChatManager getChatManager() throws RemoteException {
		return mChatManager;
	}
	public MessageManager getMessageManager() {
		return mMessageManager;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoster getRoster() throws RemoteException {
		if (mRosterAdap != null)
			return mRosterAdap;
		Roster adap = mXmppConn.getRoster();
		if (adap == null)
			return null;
		mRosterAdap = new RosterAdapter(adap, mService, mAvatarManager);
		return mRosterAdap;
	}
	/**
	 * Get the user informations.
	 * @return the user infos or null if not logged
	 */
	public UserInfo getUserInfo() {
		return mUserInfo;
	}
	@Override
	public boolean isConnected() {
		return mXmppConn.isConnected();
	}
	/**
	 * Returns true if currently authenticated by successfully calling the login method.
	 * @return true when successfully authenticated
	 */
	@Override
	public boolean isAuthentificated() {
		return mXmppConn.isAuthenticated();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeConnectionListener(IBeemConnectionListener listen)
			throws RemoteException {
		if (listen != null)
			mRemoteConnListeners.unregister(listen);
	}
	/**
	 * PrivacyListManagerAdapter mutator.
	 * @param privacyListManager the privacy list manager
	 */
	public void setPrivacyListManager(
			PrivacyListManagerAdapter privacyListManager) {
		this.mPrivacyListManager = privacyListManager;
	}
	/**
	 * PrivacyListManagerAdapter accessor.
	 * @return the mPrivacyList
	 */
	@Override
	public PrivacyListManagerAdapter getPrivacyListManager() {
		return mPrivacyListManager;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getErrorMessage() {
		return mErrorMsg;
	}
	/**
	 * Initialize the features provided by beem.
	 */
	private void initFeatures() {
		ServiceDiscoveryManager.setIdentityName("Beem");
		ServiceDiscoveryManager.setIdentityType("phone");
		ServiceDiscoveryManager sdm = ServiceDiscoveryManager
				.getInstanceFor(mXmppConn);
		if (sdm == null)
			sdm = new ServiceDiscoveryManager(mXmppConn);
		sdm.addFeature("http://jabber.org/protocol/disco#info");
		// nikita: must be uncommented when the feature will be enabled
		// sdm.addFeature("jabber:iq:privacy");
		sdm.addFeature("http://jabber.org/protocol/caps");
		sdm.addFeature("urn:xmpp:avatar:metadata");
		sdm.addFeature("urn:xmpp:avatar:metadata+notify");
		sdm.addFeature("urn:xmpp:avatar:data");
		sdm.addFeature("http://jabber.org/protocol/nick");
		sdm.addFeature("http://jabber.org/protocol/nick+notify");
		sdm.addFeature(PingExtension.NAMESPACE);
		mChatStateManager = ChatStateManager.getInstance(mXmppConn);
		EntityCapsManager em = sdm.getEntityCapsManager();
		em.setNode("http://www.beem-project.com");
	}
	/**
	 * Discover the features provided by the server.
	 */
	private void discoverServerFeatures() {
		try {
			// jid et server
			ServiceDiscoveryManager sdm = ServiceDiscoveryManager
					.getInstanceFor(mXmppConn);
			DiscoverInfo info = sdm.discoverInfo(mXmppConn.getServiceName());
			Iterator<DiscoverInfo.Identity> it = info.getIdentities();
			while (it.hasNext()) {
				DiscoverInfo.Identity identity = it.next();
				Debug.getDebugInstance().log(
						"discoverServerFeatures#getCategory:"
								+ identity.getCategory() + identity.getType());
				/*
				 * 20121129 if ("pubsub".equals(identity.getCategory()) &&
				 * "pep".equals(identity.getType())) { initPEP(); }
				 */
				if ("server".equals(identity.getCategory())
						&& "im".equals(identity.getType())) {
					initPEP();
				}
			}
		} catch (XMPPException e) {
		}
	}
	/**
	 * Initialize PEP.
	 */
	private void initPEP() {
		// Enable pep sending
		// API 8
		// mService.getExternalCacheDir()
		mPepManager = new PepSubManager(mXmppConn,
				StringUtils.parseBareAddress(mXmppConn.getUser()));
		AvatarCache avatarCache = new BeemAvatarCache(mService);
		mAvatarManager = new BeemAvatarManager(mService, mXmppConn,
				mPepManager, avatarCache, true);
		mAvatarManager.addAvatarListener(mUserInfoManager);
		mApplication.setPepEnabled(true);
	}
	/**
	 * Reset the application state.
	 */
	private void resetApplication() {
		mApplication.setPepEnabled(false);
	}

	/**
	 * Listener for XMPP connection events. It will calls the remote listeners for connection
	 * events.
	 */
	private class ConnexionListenerAdapter implements ConnectionListener {
		/**
		 * Defaut constructor.
		 */
		public ConnexionListenerAdapter() {
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void connectionClosed() {
			mRosterAdap = null;
			Intent intent = new Intent(
					BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED);
			intent.putExtra("message", mService
					.getString(R.string.BeemBroadcastReceiverDisconnect));
			intent.putExtra("normally", true);
			mService.sendBroadcast(intent);
			//			mService.stopSelf();
			resetApplication();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void connectionClosedOnError(Exception exception) {
			mRosterAdap = null;
			Intent intent = new Intent(
					BeemBroadcastReceiver.BEEM_CONNECTION_CLOSED);
			intent.putExtra("message", exception.getMessage());
			mService.sendBroadcast(intent);
			//			mService.stopSelf();
			resetApplication();
		}
		/**
		 * Connection failed callback.
		 * @param errorMsg smack failure message
		 */
		public void connectionFailed(String errorMsg) {
			final int n = mRemoteConnListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemConnectionListener listener = mRemoteConnListeners
						.getBroadcastItem(i);
				try {
					if (listener != null)
						listener.connectionFailed(errorMsg);
				} catch (RemoteException e) {
					// The RemoteCallbackList will take care of removing the
					// dead listeners.
				}
			}
			mRemoteConnListeners.finishBroadcast();
			//			mService.stopSelf();
			resetApplication();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reconnectingIn(int arg0) {
			final int n = mRemoteConnListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemConnectionListener listener = mRemoteConnListeners
						.getBroadcastItem(i);
				try {
					if (listener != null)
						listener.reconnectingIn(arg0);
				} catch (RemoteException e) {
					// The RemoteCallbackList will take care of removing the
					// dead listeners.
				}
			}
			mRemoteConnListeners.finishBroadcast();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reconnectionFailed(Exception arg0) {
			final int r = mRemoteConnListeners.beginBroadcast();
			for (int i = 0; i < r; i++) {
				IBeemConnectionListener listener = mRemoteConnListeners
						.getBroadcastItem(i);
				try {
					if (listener != null)
						listener.reconnectionFailed();
				} catch (RemoteException e) {
					// The RemoteCallbackList will take care of removing the
					// dead listeners.
				}
			}
			mRemoteConnListeners.finishBroadcast();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void reconnectionSuccessful() {
			PacketFilter filter = new PacketFilter() {
				@Override
				public boolean accept(Packet packet) {
					if (packet instanceof Presence) {
						Presence pres = (Presence) packet;
						if (pres.getType() == Presence.Type.subscribe)
							return true;
					}
					return false;
				}
			};
			mXmppConn.addPacketListener(mSubscribePacketListener, filter);
			final int n = mRemoteConnListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemConnectionListener listener = mRemoteConnListeners
						.getBroadcastItem(i);
				try {
					if (listener != null)
						listener.reconnectionSuccessful();
				} catch (RemoteException e) {
					// The RemoteCallbackList will take care of removing the
					// dead listeners.
				}
			}
			mRemoteConnListeners.finishBroadcast();
		}
	}

	private class NotificationPacketListener implements PacketListener {
		public NotificationPacketListener() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			Debug.getDebugInstance().log(
					getClass().getName() + " " + packet.toXML());
			if ((packet instanceof Message)) {
				Message message = (Message) packet;
				if (message.getType().equals(Message.Type.headline)) {
					String from = message.getFrom();
					NotificationCompat.Builder notif = new NotificationCompat.Builder(
							mService);
					// String title =
					// mService.getString(R.string.AcceptContactRequest, from);
					// String text =
					// mService.getString(R.string.AcceptContactRequestFrom,
					// from);
					String title = message.getFrom();
					String text = message.getBody();
					notif.setTicker(title).setContentTitle(title);
					notif.setContentText(text);
					notif.setAutoCancel(true).setWhen(
							System.currentTimeMillis());
					// Intent intent = new Intent(mService, Subscription.class);
					/*Intent intent = new Intent(mService, ContactList.class);
					// intent.setData(Contact.makeXmppUri(from));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent notifIntent = PendingIntent.getActivity(mService, 0, intent,
							PendingIntent.FLAG_ONE_SHOT);
					notif.setContentIntent(notifIntent);*/
					int id = message.hashCode();
					mService.sendNotification(id, notif.getNotification());
				}
			}
		}
	}

	/**
	 * This PacketListener will set a notification when you got a subscribtion request.
	 * @author Da Risk <da_risk@elyzion.net>
	 */
	private class SubscribePacketListener implements PacketListener {
		/**
		 * Constructor.
		 */
		public SubscribePacketListener() {
		}
		@Override
		public void processPacket(Packet packet) {
			if (packet == null) {
				return;
			}
			boolean exist = false;
			if (mXmppConn != null && mXmppConn.getRoster() != null) {
				for (Iterator<RosterEntry> iterator = mXmppConn.getRoster()
						.getEntries().iterator(); iterator.hasNext();) {
					RosterEntry type = iterator.next();
					if (type.getUser().equals(
							VVXMPPUtils.makeJidCompleted(packet.getFrom()))) {
						exist = true;
					}
				}
			}
			Presence subscribe = (Presence) packet;
			Type subscribeType = null;
			if (subscribe != null) {
				subscribeType = subscribe.getType();
			}
			if (mXmppConn != null && exist
					&& Presence.Type.subscribe.equals(subscribeType)) {
				Presence presence = new Presence(Type.subscribed);
				presence.setTo(packet.getFrom());
				mXmppConn.sendPacket(presence);
			} else {
				String from = packet.getFrom();
				NotificationCompat.Builder notif = new NotificationCompat.Builder(
						mService);
				String title = mService.getString(
						R.string.AcceptContactRequest, from);
				String text = mService.getString(
						R.string.AcceptContactRequestFrom, from);
				notif.setTicker(title).setContentTitle(title);
				notif.setContentText(text);
				notif.setAutoCancel(true).setWhen(System.currentTimeMillis());
				sendPresenceBroadcast(from);// 20130408
				Intent intent = new Intent(mService, Subscription.class);
				intent.setData(Contact.makeXmppUri(from));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent notifIntent = PendingIntent.getActivity(mService,
						0, intent, PendingIntent.FLAG_ONE_SHOT);
				notif.setContentIntent(notifIntent);
				int id = packet.hashCode();
				mService.sendNotification(id, notif.getNotification());
			}
		}
	}

	private void sendPresenceBroadcast(String from) {
		Intent intentmessage = new Intent("com.vv.im.last.message");
		intentmessage.putExtra("from", from);
		intentmessage.putExtra("myself",
				StringUtils.parseBareAddress(mUserInfo.getJid()));
		mApplication.sendBroadcast(intentmessage);
	}

	/**
	 * The UserInfoManager listen to XMPP events and update the user information accoldingly.
	 */
	private class UserInfoManager implements AvatarListener {
		/**
		 * Constructor.
		 */
		public UserInfoManager() {
		}
		@Override
		public void onAvatarChange(String from, String avatarId,
				List<AvatarMetadataExtension.Info> avatarInfos) {
			String jid = StringUtils.parseBareAddress(mUserInfo.getJid());
			String mfrom = StringUtils.parseBareAddress(from);
			if (jid.equalsIgnoreCase(mfrom)) {
				mUserInfo.setAvatarId(avatarId);
			}
		}
	}

	public void setLoginInfo(String username, String password, String resource) {
		this.mLogin = username;
		this.mPassword = password;
		this.mResource = resource;
	}

	/**
	 * Listener for Ping request. It will respond with a Pong.
	 */
	private class PingListener implements PacketListener {
		/**
		 * Constructor.
		 */
		public PingListener() {
		}
		@Override
		public void processPacket(Packet packet) {
			if (!(packet instanceof PingExtension))
				return;
			PingExtension p = (PingExtension) packet;
			if (p.getType() == IQ.Type.GET) {
				PingExtension pong = new PingExtension();
				pong.setType(IQ.Type.RESULT);
				pong.setTo(p.getFrom());
				pong.setPacketID(p.getPacketID());
				mXmppConn.sendPacket(pong);
			}
		}
	}
}
