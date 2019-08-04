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

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;

import android.net.Uri;
import android.os.RemoteException;

import com.beem.project.btf.BeemService;
import com.beem.project.btf.service.aidl.IChatManager;
import com.beem.project.btf.service.aidl.IConnectionListener;
import com.beem.project.btf.service.aidl.IPrivacyListManager;
import com.beem.project.btf.service.aidl.IRoster;
import com.beem.project.btf.service.aidl.IXmppConnection;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.utils.PresenceType;

/**
 * This class is a facade for the Beem Service.
 * @author darisk
 */
public class XmppFacade extends IXmppFacade.Stub {
	private XmppConnectionAdapter mConnexion;
	private BeemService service;

	/**
	 * Create an XmppFacade.
	 * @param service the service providing the facade
	 */
	public XmppFacade(final BeemService service) {
		this.service = service;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changeStatus(int status, String msg) {
		initConnection();
		mConnexion.changeStatus(status, msg);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectAsync() throws RemoteException {
		initConnection();
		mConnexion.connectAsync();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean connectSync() throws RemoteException {
		initConnection();
		return mConnexion.connectSync();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IXmppConnection createConnection() throws RemoteException {
		initConnection();
		return mConnexion;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IChatManager getChatManager() throws RemoteException {
		initConnection();
		return mConnexion.getChatManager();
	}
	public MessageManager getMessageManager() {
		initConnection();
		return mConnexion.getMessageManager();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRoster getRoster() throws RemoteException {
		initConnection();
		return mConnexion.getRoster();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPrivacyListManager getPrivacyListManager() {
		initConnection();
		return mConnexion.getPrivacyListManager();
	}
	@Override
	public void sendPresencePacket(PresenceAdapter presence)
			throws RemoteException {
		initConnection();
		Presence presence2 = new Presence(
				PresenceType.getPresenceTypeFrom(presence.getType()));
		presence2.setTo(presence.getTo());
		mConnexion.getAdaptee().sendPacket(presence2);
	}
	@Override
	public void call(String jid) throws RemoteException {
	}
	@Override
	public boolean publishAvatar(Uri avatarUri) throws RemoteException {
		initConnection();
		BeemAvatarManager mgr = mConnexion.getAvatarManager();
		if (mgr == null)
			return false;
		return mgr.publishAvatar(avatarUri);
	}
	@Override
	public void disableAvatarPublishing() throws RemoteException {
		initConnection();
		BeemAvatarManager mgr = mConnexion.getAvatarManager();
		if (mgr != null)
			mgr.disableAvatarPublishing();
	}
	@Override
	public UserInfo getUserInfo() throws RemoteException {
		initConnection();
		return mConnexion.getUserInfo();
	}
	/**
	 * Initialize the connection.
	 */
	private void initConnection() {
		mConnexion = service.createConnection();
	}
	@Override
	public void sendVCardPacket(String jid) throws RemoteException {
		VCard vcard = new VCard();
		vcard.setJabberId(jid);
		VCardAdapter vcardAdpter = new VCardAdapter(vcard);
		PacketCollector collector = mConnexion.getAdaptee()
				.createPacketCollector(
						new PacketIDFilter(vcardAdpter.getMvCard()
								.getPacketID()));
		mConnexion.getAdaptee().sendPacket(vcardAdpter.getMvCard());
		Packet response = collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
	}
	@Override
	public void sendMessage(Message msg) throws RemoteException {
		org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
		message.setBody(msg.getBody());
		message.setTo(msg.getTo());
		message.setType(org.jivesoftware.smack.packet.Message.Type.headline);
		mConnexion.getAdaptee().sendPacket(message);
	}
	@Override
	public void sendVVPacket(VVPacketAdapter vcard) throws RemoteException {
		Packet packet = vcard.getPacket();
		mConnexion.getAdaptee().sendPacket(packet);
	}
	@Override
	public void addVVPacketListener(VVPacketListenerAdapter packetListener,
			VVPacketFilterAdapter filter) throws RemoteException {
		PacketListener arg0 = packetListener.getListener();
		PacketFilter arg1 = filter.getFilter();
		if (mConnexion != null && mConnexion.getAdaptee() != null) {
			mConnexion.getAdaptee().addPacketListener(arg0, arg1);
		}
	}
	@Override
	public void removeVVPacketListener(VVPacketListenerAdapter packetListener)
			throws RemoteException {
		// TODO Auto-generated method stub
		if (mConnexion != null && mConnexion.getAdaptee() != null)
			mConnexion.getAdaptee().removePacketListener(
					packetListener.getListener());
	}
	@Override
	public VVPacketAdapter collectVVPacket(VVPacketAdapter vcard)
			throws RemoteException {
		PacketCollector collector = mConnexion.getAdaptee()
				.createPacketCollector(
						new PacketIDFilter(vcard.getPacket().getPacketID()));
		mConnexion.getAdaptee().sendPacket(vcard.getPacket());
		Packet response = collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		if (response == null) {
			try {
				throw new XMPPException(
						"No response from server on status set.");
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		} else if (response.getError() != null) {
			try {
				throw new XMPPException(response.getError());
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		if (response == null)
			return null;
		return new VVPacketAdapter(response);
	}
	@Override
	public void load(String jid) throws RemoteException {
		VCard card = new VCard();
		try {
			if (jid == null) {
				card.load(mConnexion.getAdaptee());
			} else {
				card.load(mConnexion.getAdaptee(), jid);
			}
		} catch (XMPPException e1) {
			e1.printStackTrace();
		}
	}
	@Override
	public String getVVAccount() throws RemoteException {
		return mConnexion.getAdaptee().getUser();
	}
	@Override
	public void connectAsyncEx(String jid, String password,
			IConnectionListener lis) throws RemoteException {
		mConnexion = service.createConnection(jid, password);
		mConnexion.connectAsync(lis);
	}
	@Override
	public void disconnect() throws RemoteException {
		if (mConnexion != null) {
			if (mConnexion.isAuthentificated()) {
				mConnexion.disconnect();
			}
			mConnexion = null;
		}
	}
	@Override
	public boolean isConnected() throws RemoteException {
		return mConnexion.connect();
	}
	@Override
	public boolean isAuthentificated() throws RemoteException {
		if (mConnexion == null) {
			initConnection();
		}
		return mConnexion.isAuthentificated();
	}
}
