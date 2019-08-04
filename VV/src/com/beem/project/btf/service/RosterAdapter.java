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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.aidl.IBeemRosterListener;
import com.beem.project.btf.smack.avatar.AvatarListener;
import com.beem.project.btf.smack.avatar.AvatarManager;
import com.beem.project.btf.smack.avatar.AvatarMetadataExtension.Info;
import com.beem.project.btf.utils.Status;
import com.butterfly.vv.service.ContactService;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

/**
 * This class implement a Roster adapter for BEEM.
 */
public class RosterAdapter extends
		com.beem.project.btf.service.aidl.IRoster.Stub {
	private static final String TAG = "RosterAdapter";
	private final Roster mAdaptee;
	private final RemoteCallbackList<IBeemRosterListener> mRemoteRosListeners = new RemoteCallbackList<IBeemRosterListener>();
	private final Map<Integer, String> mDefaultStatusMessages;
	private final RosterListenerAdapter mRosterListener = new RosterListenerAdapter();
	private Map<String, String> mAvatarIdmap = new HashMap<String, String>();
	private AvatarManager mAvatarManager;
	private String myJid;
	private Service mService;

	/**
	 * Constructor.
	 * @param roster The roster to adapt.
	 * @param context The context of the RosterAdapter.
	 * @param avatarMgr The AvatarManager of the connection
	 */
	public RosterAdapter(final Roster roster, final Service service,
			final AvatarManager avatarMgr) {
		mAdaptee = roster;
		roster.addRosterListener(mRosterListener);
		mDefaultStatusMessages = createDefaultStatusMessagesMap(service);
		mAvatarManager = avatarMgr;
		if (mAvatarManager != null)
			mAvatarManager.addAvatarListener(new AvatarEventListener());
		this.mService = service;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addRosterListener(IBeemRosterListener listen)
			throws RemoteException {
		if (listen != null)
			mRemoteRosListeners.register(listen);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createGroup(String groupname) throws RemoteException {
		if (mAdaptee.getGroup(groupname) == null)
			mAdaptee.createGroup(groupname);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getContact(String jid) throws RemoteException {
		return mAdaptee.getContact(jid);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Contact> getFriendList() throws RemoteException {
		List<Contact> coList = new ArrayList<Contact>();
		try {
			Map<String, Contact> retVal = new HashMap<String, Contact>();
			for (Contact contact : mAdaptee.getFriendlist()) {
				retVal.put(contact.getJid(), contact);
			}
			for (Contact contact : mAdaptee.getBlacklist()) {
				retVal.remove(contact.getJid());
			}
			coList.addAll(retVal.values());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return coList;
	}
	// 获取离线好友列表
	protected List<Contact> getOffLineList(List<Contact> coList) {
		//LogUtils.i("getOffLineList#" + myJid + " coList.size:" + coList.size());
		return coList;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getGroupsNames() throws RemoteException {
		Collection<RosterGroup> groups = mAdaptee.getGroups();
		List<String> result = new ArrayList<String>(groups.size());
		for (RosterGroup rosterGroup : groups) {
			result.add(rosterGroup.getName());
		}
		return result;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeRosterListener(IBeemRosterListener listen)
			throws RemoteException {
		if (listen != null)
			mRemoteRosListeners.unregister(listen);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setContactName(String jid, String name) throws RemoteException {
		mAdaptee.getEntry(jid).setName(name);
	}
	@Override
	public PresenceAdapter getPresence(String jid) throws RemoteException {
		return new PresenceAdapter(mAdaptee.getPresence(jid));
	}
	@Override
	public void addContactToGroup(String groupName, String jid)
			throws RemoteException {
		createGroup(groupName);
		RosterGroup group = mAdaptee.getGroup(groupName);
		try {
			group.addEntry(mAdaptee.getEntry(jid));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void removeContactFromGroup(String groupName, String jid)
			throws RemoteException {
		RosterGroup group = mAdaptee.getGroup(groupName);
		try {
			group.removeEntry(mAdaptee.getEntry(jid));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create a map which contains default status messages.
	 * @param context The context of the roster adapter.
	 * @return A Map<Integer, String> which assigns a status to a message.
	 */
	private Map<Integer, String> createDefaultStatusMessagesMap(Context context) {
		Map<Integer, String> defaultStatusMessages = new HashMap<Integer, String>();
		defaultStatusMessages.put(Status.CONTACT_STATUS_AVAILABLE,
				context.getString(R.string.contact_status_msg_available));
		defaultStatusMessages.put(Status.CONTACT_STATUS_AVAILABLE_FOR_CHAT,
				context.getString(R.string.contact_status_msg_available_chat));
		defaultStatusMessages.put(Status.CONTACT_STATUS_AWAY,
				context.getString(R.string.contact_status_msg_away));
		defaultStatusMessages.put(Status.CONTACT_STATUS_BUSY,
				context.getString(R.string.contact_status_msg_dnd));
		defaultStatusMessages.put(Status.CONTACT_STATUS_DISCONNECT,
				context.getString(R.string.contact_status_msg_offline));
		defaultStatusMessages.put(Status.CONTACT_STATUS_UNAVAILABLE,
				context.getString(R.string.contact_status_msg_xa));
		return defaultStatusMessages;
	}

	/**
	 * Listener for the roster events. It will call the remote listeners registered.
	 * @author darisk
	 */
	private class RosterListenerAdapter implements RosterListener {
		/**
		 * Constructor.
		 */
		public RosterListenerAdapter() {
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void entriesAdded(Collection<String> addresses) {
			final int n = mRemoteRosListeners.beginBroadcast();
			List<String> tab = new ArrayList<String>();
			tab.addAll(addresses);
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onEntriesAdded(tab);
				} catch (RemoteException e) {
					Log.w(TAG, "Error while adding roster entries", e);
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void entriesDeleted(Collection<String> addresses) {
			//LogUtils.i("~entriesDeleted~" + addresses);
			final int n = mRemoteRosListeners.beginBroadcast();
			List<String> tab = new ArrayList<String>();
			tab.addAll(addresses);
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onEntriesDeleted(tab);
				} catch (RemoteException e) {
					Log.w(TAG, "Error while deleting roster entries", e);
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void entriesUpdated(Collection<String> addresses) {
			//LogUtils.i("~entriesUpdated~" + addresses);
			final int n = mRemoteRosListeners.beginBroadcast();
			List<String> tab = new ArrayList<String>();
			tab.addAll(addresses);
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onEntriesUpdated(tab);
				} catch (RemoteException e) {
					Log.w(TAG, "Error while updating roster entries", e);
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void presenceChanged(Presence presence) {
			final int n = mRemoteRosListeners.beginBroadcast();
			Log.v(TAG, ">>> Presence changed for " + presence.getFrom());
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					if (presence.getStatus() == null
							|| "".equals(presence.getStatus())) {
						presence.setStatus(mDefaultStatusMessages.get(Status
								.getStatusFromPresence(presence)));
					}
					listener.onPresenceChanged(new PresenceAdapter(presence));
				} catch (RemoteException e) {
					Log.w(TAG, "Error while updating roster presence entries",
							e);
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		@Override
		public void onRostersUpdate(List<Contact> rosters) {
			final int n = mRemoteRosListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onRostersUpdate(rosters);
				} catch (RemoteException e) {
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		@Override
		public void onNewFriendsUpdate(List<Contact> newfriends) {
			final int n = mRemoteRosListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onNewFriendsUpdate(newfriends);
				} catch (RemoteException e) {
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		@Override
		public void onBlacklistUpdate(List<Contact> blacklist) {
			final int n = mRemoteRosListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onBlacklistUpdate(blacklist);
				} catch (RemoteException e) {
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		@Override
		public void onUserUpdate(String user, Contact contact) {
			final int n = mRemoteRosListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onUserUpdate(user, contact);
				} catch (RemoteException e) {
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
		@Override
		public void onBlacklistedUpdate(List<String> blacklisted) {
			final int n = mRemoteRosListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IBeemRosterListener listener = mRemoteRosListeners
						.getBroadcastItem(i);
				try {
					listener.onBlacklistedUpdate(blacklisted);
				} catch (RemoteException e) {
				}
			}
			mRemoteRosListeners.finishBroadcast();
		}
	}

	/**
	 * Listener on avatar metadata event.
	 */
	private class AvatarEventListener implements AvatarListener {
		/**
		 * Constructor.
		 */
		public AvatarEventListener() {
		}
		@Override
		public void onAvatarChange(String from, String avatarId,
				List<Info> avatarInfos) {
			String bare = StringUtils.parseBareAddress(from);
			if (avatarId == null)
				mAvatarIdmap.remove(bare);
			else if (avatarInfos.size() > 0) {
				mAvatarIdmap.put(bare, avatarId);
			}
		}
	}

	@Override
	public List<Contact> getBlacklist() throws RemoteException {
		try {
			return mAdaptee.getBlacklist();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
	// 拉黑
	@Override
	public PacketResult addBlacklist(String[] user) throws RemoteException {
		try {
			PacketResult result = mAdaptee.addBlacklist(user);
			if (result != null && result.isOk()) {
				ContactService.getInstance()
						.sendFriendOptBR(Constants.ACTION_FRIEND_ADD_BLACKLIST,
								isFriendIn(user));
			}
			return result;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
	private boolean isFriendIn(String... users) {
		for (String jid : users) {
			if (mAdaptee.friendYet(jid)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public PacketResult removeBlacklist(String[] jids) throws RemoteException {
		try {
			PacketResult result = mAdaptee.removeBlacklist(jids);
			if (result != null && result.isOk()) {
				ContactService.getInstance().sendFriendOptBR(
						Constants.ACTION_FRIEND_REMOVE_BLACKLIST,
						isFriendIn(jids));
			}
			return result;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean blackYet(String user) throws RemoteException {
		return mAdaptee.blackYet(user);
	}
	protected void setMyJid(String jid) throws RemoteException {
		this.myJid = jid;
		System.out.println("setRosterJid: " + jid);
	}
	@Override
	public boolean friendYet(String user) throws RemoteException {
		return mAdaptee.friendYet(user);
	}
	@Override
	public PacketResult removeFriend(String jid) throws RemoteException {
		try {
			PacketResult result = mAdaptee.removeFriend(jid);
			if (result != null && result.isOk()) {
				ContactService.getInstance().sendFriendOptBR(
						Constants.ACTION_FRIEND_REMOVE_FRIEND, true);
			}
			return result;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public PacketResult addFriend(String jid, String content, String operation)
			throws RemoteException {
		try {
			PacketResult result = mAdaptee.addFriend(jid, content, operation);
			// 此处不对friends和allContacts做处理，等待对方同意发送iq，再做处理
			return result;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public PacketResult modifyFriendAlias(String jid, String alias)
			throws RemoteException {
		try {
			PacketResult result = mAdaptee.modifyFriendAlias(jid, alias);
			if (result != null && result.isOk()) {
				getContact(jid).setAlias(alias);
				ContactService.getInstance().sendFriendOptBR(
						Constants.ACTION_FRIEND_MODIFY_ALIAS, isFriendIn(jid),
						jid);
			}
			return result;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Contact getMyContact() throws RemoteException {
		return getContact(myJid);
	}
	@Override
	public PacketResult modifyContactInfo(Map modifyMap) throws RemoteException {
		try {
			PacketResult result = mAdaptee.modifyContactInfo(modifyMap);
			if (result != null && result.isOk()) {
				mAdaptee.getContact(LoginManager.getInstance().getJidParsed())
						.saveData(modifyMap);
			}
			return result;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}
}
