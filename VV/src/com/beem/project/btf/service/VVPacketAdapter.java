package com.beem.project.btf.service;

import org.jivesoftware.smack.packet.Packet;

import com.beem.project.btf.service.aidl.IXmppFacade;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public class VVPacketAdapter implements Parcelable {
	private Packet packet;
	private String jid;

	public VVPacketAdapter(Parcel source) {
		this.jid = source.readString();
	}
	public VVPacketAdapter(Packet packet) {
		this.jid = packet.getFrom();
		this.packet = packet;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(jid);
	}

	public static final Parcelable.Creator<VVPacketAdapter> CREATOR = new Creator<VVPacketAdapter>() {
		@Override
		public VVPacketAdapter[] newArray(int size) {
			return new VVPacketAdapter[size];
		}
		@Override
		public VVPacketAdapter createFromParcel(Parcel source) {
			return new VVPacketAdapter(source);
		}
	};

	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public Packet getPacket() {
		return packet;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	// 封装下XmppFacade中的collectVVPacket(),进一步解析出Packet
	public static Packet collectVVPacket(IXmppFacade pXmppFacade, Packet pPacket) {
		VVPacketAdapter packetAdapter = null;
		try {
			packetAdapter = pXmppFacade.collectVVPacket(new VVPacketAdapter(
					pPacket));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return packetAdapter == null ? null : packetAdapter.getPacket();
	}
}
