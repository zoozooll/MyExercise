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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.Environment;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.otr.BeemOtrManager;
import com.beem.project.btf.service.aidl.IChat;
import com.beem.project.btf.service.aidl.IMessageListener;
import com.beem.project.btf.ui.activity.ChatActivity.MessageState;
import com.btf.push.ImageNotificationExtension;
import com.btf.push.Item;

import net.java.otr4j.OtrException;
import net.java.otr4j.session.SessionID;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateListener;

/**
 * An adapter for smack's Chat class.
 * @author darisk
 */
public class ChatAdapter extends IChat.Stub {
	private static final int HISTORY_MAX_SIZE = 50;
	private static final String TAG = "ChatAdapter";
	private static final String PROTOCOL = "XMPP";
	private final Chat mAdaptee;
	private final Contact mParticipant;
	private String mState;
	private boolean mIsOpen;
	private final List<Message> mMessages;
	private final RemoteCallbackList<IMessageListener> mRemoteListeners = new RemoteCallbackList<IMessageListener>();
	private final MsgListener mMsgListener = new MsgListener();
	private SessionID mOtrSessionId;
	private boolean mIsHistory;
	private File mHistoryPath;
	private String mAccountUser;
	private int mUnreadMsgCount;

	/**
	 * Constructor.
	 * @param chat The chat to adapt
	 */
	public ChatAdapter(final Chat chat, final MessageManager messageManager) {
		mAdaptee = chat;
		mParticipant = new Contact(chat.getParticipant());
		mMessages = new LinkedList<Message>();
		mAdaptee.addMessageListener(mMsgListener);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Contact getParticipant() throws RemoteException {
		return mParticipant;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(com.beem.project.btf.service.Message message)
			throws RemoteException {
		//		//LogUtils.i("send msg,timestamp:" + message.getTimestamp() + " jid:" + message.getTo() + " from:"
		//				+ message.getFrom() + " content:" + message.getBody() + " msgType: chat");
		com.beem.project.btf.service.Message encrypted = otrEncryptMessage(message);
		if (encrypted != null) {
			transferMessage(encrypted);
		} else {
			transferMessage(message);
		}
		addMessage(message, true);
		interceptMessage(message);
	}
	// 截获本段发送的Message
	private void interceptMessage(com.beem.project.btf.service.Message message) {
		try {
			final int n = mRemoteListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IMessageListener listener = mRemoteListeners
						.getBroadcastItem(i);
				if (listener != null) {
					listener.interceptMessage(this, message);
				}
			}
		} catch (RemoteException e) {
			Log.w(TAG, "Error while diffusing message to listener", e);
		} finally {
			mRemoteListeners.finishBroadcast();
		}
	}
	/**
	 * private method for sending message.
	 * @param message the message to send
	 */
	private void transferMessage(com.beem.project.btf.service.Message message) {
		org.jivesoftware.smack.packet.Message send = new org.jivesoftware.smack.packet.Message();
		String msgBody = message.getBody();
		send.setTo((message.getTo()));
		send.setBody(msgBody);
		send.setThread(message.getThread());
		send.setSubject(message.getSubject());
		send.setType(org.jivesoftware.smack.packet.Message.Type.chat);
		send.setTime(message.getTimestampStr());
		// TODO gerer les messages contenant des XMPPError
		try {
			mAdaptee.sendMessage(send);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
	/**
	 * send message.
	 * @param msg to send.
	 */
	public void injectMessage(String msg) {
		Message msgToSend = new Message(mParticipant.getJIDCompleted(),
				Message.MSG_TYPE_CHAT);
		msgToSend.setBody(msg);
		transferMessage(msgToSend);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessageListener(IMessageListener listen) {
		if (listen != null)
			mRemoteListeners.register(listen);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMessageListener(IMessageListener listen) {
		if (listen != null) {
			mRemoteListeners.unregister(listen);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getState() throws RemoteException {
		return mState;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setState(String state) throws RemoteException {
		mState = state;
	}
	/**
	 * Get the adaptee for the Chat.
	 * @return The real chat object
	 */
	public Chat getAdaptee() {
		return mAdaptee;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOpen(boolean isOpen) {
		this.mIsOpen = isOpen;
		if (isOpen)
			mUnreadMsgCount = 0;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		return mIsOpen;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Message> getMessages() throws RemoteException {
		return Collections.unmodifiableList(mMessages);
	}
	/**
	 * Add a message in the chat history.
	 * @param msg the message to add
	 */
	public void addMessage(Message msg, boolean isLocal) {
		if (mMessages.size() == HISTORY_MAX_SIZE)
			mMessages.remove(0);
		mMessages.add(msg);
		if (!isOpen() && (!isLocal))
			mUnreadMsgCount++;
		if (!"".equals(msg.getBody()) && msg.getBody() != null) {
			logMessage(msg);
		}
	}
	/**
	 * Save message in SDCard.
	 * @param msg the message receive
	 * @param contactName the name of the contact
	 */
	public void saveHistory(Message msg, String contactName) {
		File path = getHistoryPath();
		File filepath;
		if (contactName.equals(msg.getFrom()))
			filepath = new File(path, StringUtils.parseBareAddress(contactName));
		else
			filepath = new File(path, StringUtils.parseBareAddress(msg.getTo()));
		path.mkdirs();
		try {
			FileWriter file = new FileWriter(filepath, true);
			String log = msg.getTimestamp() + " " + contactName + " "
					+ msg.getBody() + System.getProperty("line.separator");
			file.write(log);
			file.close();
		} catch (IOException e) {
			Log.e(TAG, "Error writing chat history", e);
		}
	}
	/**
	 * set History enable/disable.
	 * @param isHisory history state
	 */
	public void setHistory(boolean isHisory) {
		this.mIsHistory = isHisory;
	}
	/**
	 * get History state.
	 * @return mIsHistory
	 */
	public boolean getHistory() {
		return mIsHistory;
	}
	/**
	 * Set Account user name.
	 * @param accountUser user name
	 */
	public void setAccountUser(String accountUser) {
		mAccountUser = accountUser;
	}
	/**
	 * get Account user name.
	 * @return mAccountUser
	 */
	public String getAccountUser() {
		return mAccountUser;
	}
	/**
	 * set History path.
	 * @param historyPath history path
	 */
	public void setHistoryPath(File historyPath) {
		this.mHistoryPath = historyPath;
	}
	/**
	 * get History path.
	 * @return mHistoryPath;
	 */
	public File getHistoryPath() {
		return mHistoryPath;
	}
	/**
	 * log a message.
	 * @param message message to log
	 */
	private void logMessage(com.beem.project.btf.service.Message message) {
		String state = Environment.getExternalStorageState();
		if (mIsHistory && Environment.MEDIA_MOUNTED.equals(state))
			saveHistory(message, mAccountUser);
	}
	/**
	 * encrypt a message with an otr session.
	 * @param unencrypted message with cleartext body
	 * @return message with encrypted body
	 */
	private com.beem.project.btf.service.Message otrEncryptMessage(
			com.beem.project.btf.service.Message unencrypted) {
		if (mOtrSessionId != null && unencrypted != null
				&& unencrypted.getBody() != null) {
			try {
				String body = BeemOtrManager.getInstance().getOtrManager()
						.transformSending(mOtrSessionId, unencrypted.getBody());
				Message result = new Message(unencrypted.getTo(),
						unencrypted.getType());
				result.setBody(body);
				return result;
			} catch (OtrException e) {
				Log.e(TAG, "OTR: Unable to encrypt message", e);
			}
		}
		return null;
	}
	/**
	 * This method is executed when the otr session status change.
	 * @param otrState the new state of otr session.
	 */
	public void otrStateChanged(final String otrState) {
		Message m = new Message(null, Message.MSG_TYPE_INFO);
		m.setBody(otrState);
		addMessage(m, false);
		final int n = mRemoteListeners.beginBroadcast();
		for (int i = 0; i < n; i++) {
			IMessageListener listener = mRemoteListeners.getBroadcastItem(i);
			try {
				listener.otrStateChanged(otrState);
			} catch (RemoteException e) {
				Log.w(TAG, e.getMessage());
			}
		}
		mRemoteListeners.finishBroadcast();
	}
	@Override
	public void startOtrSession() throws RemoteException {
		if (mOtrSessionId == null) {
			mOtrSessionId = new SessionID(mAccountUser,
					mParticipant.getJIDCompleted(), PROTOCOL);
			BeemOtrManager.getInstance().addChat(mOtrSessionId, this);
		}
		try {
			BeemOtrManager.getInstance().getOtrManager()
					.startSession(mOtrSessionId);
		} catch (OtrException e) {
			mOtrSessionId = null;
			e.printStackTrace();
			throw new RemoteException();
		}
	}
	@Override
	public void endOtrSession() throws RemoteException {
		try {
			localEndOtrSession();
		} catch (OtrException e) {
			e.printStackTrace();
			throw new RemoteException();
		}
	}
	/**
	 * end an Otr session.
	 * @return false if something bad happened.
	 * @throws OtrException an exception from otr
	 */
	public boolean localEndOtrSession() throws OtrException {
		if (mOtrSessionId == null)
			return true;
		BeemOtrManager.getInstance().getOtrManager().endSession(mOtrSessionId);
		BeemOtrManager.getInstance().removeChat(mOtrSessionId);
		mOtrSessionId = null;
		listenOtrSession();
		return true;
	}
	/**
	 * Start listenning to an OTR session.
	 */
	public void listenOtrSession() {
		if (mOtrSessionId != null)
			return;
		mOtrSessionId = new SessionID(mAccountUser,
				mParticipant.getJIDCompleted(), PROTOCOL);
		BeemOtrManager.getInstance().addChat(mOtrSessionId, this);
		// OtrEngineImpl will make a call to "this.getSession(sessionID)" which
		// will instantiate our
		// session.
		BeemOtrManager.getInstance().getOtrManager()
				.getSessionStatus(mOtrSessionId);
	}
	@Override
	public String getLocalOtrFingerprint() throws RemoteException {
		if (mOtrSessionId == null)
			return null;
		return BeemOtrManager.getInstance().getLocalFingerprint(mOtrSessionId);
	}
	@Override
	public String getRemoteOtrFingerprint() throws RemoteException {
		if (mOtrSessionId == null)
			return null;
		return BeemOtrManager.getInstance().getRemoteFingerprint(mOtrSessionId);
	}
	@Override
	public void verifyRemoteFingerprint(boolean ok) {
		if (mOtrSessionId != null) {
			if (ok)
				BeemOtrManager.getInstance().verifyRemoteFingerprint(
						mOtrSessionId);
			else
				BeemOtrManager.getInstance().unverifyRemoteFingerprint(
						mOtrSessionId);
		}
	}
	@Override
	public String getOtrStatus() throws RemoteException {
		if (mOtrSessionId == null)
			return null;
		return BeemOtrManager.getInstance().getOtrManager()
				.getSessionStatus(mOtrSessionId).toString();
	}
	@Override
	public int getUnreadMessageCount() throws RemoteException {
		return mUnreadMsgCount;
	}

	/**
	 * Listener.
	 */
	private class MsgListener implements ChatStateListener {
		public MsgListener() {
		}
		@Override
		public void processMessage(Chat chat,
				org.jivesoftware.smack.packet.Message message) {
			Message msg = new Message(message);
			//			//LogUtils.i("MsgListener:new msg [" + msg.getBody() + "] msg Type:" + msg.getType());
			if (msg.getType() == Message.MSG_TYPE_NORMAL) {
				Log.d(TAG,
						"notification " + msg.toString() + " body:"
								+ msg.getBody());
				PacketExtension packetExtension = message
						.getExtension(ImageNotificationExtension.NAMESPACE);
				if (packetExtension instanceof ImageNotificationExtension) {
					ImageNotificationExtension extension = (ImageNotificationExtension) packetExtension;
					Log.d(TAG,
							"ImageNotificationExtension "
									+ extension.getImgcount());
				}
			} else {
				try {
					if (mOtrSessionId != null) {
						try {
							String body = BeemOtrManager
									.getInstance()
									.getOtrManager()
									.transformReceiving(mOtrSessionId,
											msg.getBody());
							msg.setBody(body);
						} catch (OtrException e) {
							Log.w(TAG, "Unable to decrypt OTR message", e);
						}
					}
					// TODO add que les message pas de type errors
					ChatAdapter.this.addMessage(msg, false);
					try {
						final int n = mRemoteListeners.beginBroadcast();
						for (int i = 0; i < n; i++) {
							IMessageListener listener = mRemoteListeners
									.getBroadcastItem(i);
							if (listener != null)
								listener.processMessage(ChatAdapter.this, msg);
						}
					} catch (RemoteException e) {
						Log.w(TAG, "Error while diffusing message to listener",
								e);
					} finally {
						mRemoteListeners.finishBroadcast();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void stateChanged(Chat chat, ChatState state) {
			mState = state.name();
			final int n = mRemoteListeners.beginBroadcast();
			for (int i = 0; i < n; i++) {
				IMessageListener listener = mRemoteListeners
						.getBroadcastItem(i);
				try {
					listener.stateChanged(ChatAdapter.this);
				} catch (RemoteException e) {
					Log.w(TAG, e.getMessage());
				}
			}
			mRemoteListeners.finishBroadcast();
		}
	}

	// 离线消息的Message也加入
	public void addMessage(Item item, boolean isLocal) {
		Message message = new Message(LoginManager.getInstance().getJidParsed());
		message.setFrom(item.getJid());
		message.setBody(item.getMessage());
		message.setTimestampStr(item.getTimestamp());
		message.setSubType(item.getMsgTypeSub());
		addMessage(message, isLocal);
		item.setUnReadMsgCount(mUnreadMsgCount);
	}
	@Override
	public void setMessageState(int msgState, long timeStamp)
			throws RemoteException {
		for (Message messageOne : mMessages) {
			if (messageOne.getTimestamp().getTime() == timeStamp) {
				messageOne.setMsgState(MessageState.values()[msgState]);
				break;
			}
		}
	}
}
