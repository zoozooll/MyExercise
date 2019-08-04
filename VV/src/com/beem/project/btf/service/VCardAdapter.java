package com.beem.project.btf.service;

import org.jivesoftware.smackx.packet.VCard;

import android.os.Parcel;
import android.os.Parcelable;

public class VCardAdapter implements Parcelable {
	private String jid;
	private VCard mvCard;
	public static final Parcelable.Creator<VCardAdapter> CREATOR = new Creator<VCardAdapter>() {
		@Override
		public VCardAdapter createFromParcel(Parcel source) {
			return new VCardAdapter(source);
		}
		@Override
		public VCardAdapter[] newArray(int size) {
			return new VCardAdapter[size];
		}
	};

	public VCardAdapter(Parcel source) {
		// TODO Auto-generated constructor stub
		jid = source.readString();
	}
	public VCardAdapter(VCard vCard) {
		jid = vCard.getJabberId();
		this.mvCard = vCard;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jid);
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public VCard getMvCard() {
		return mvCard;
	}
	public void setMvCard(VCard mvCard) {
		this.mvCard = mvCard;
	}
}
