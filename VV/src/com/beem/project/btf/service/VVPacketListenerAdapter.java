package com.beem.project.btf.service;

import org.jivesoftware.smack.PacketListener;

import android.os.Parcel;
import android.os.Parcelable;

public class VVPacketListenerAdapter implements Parcelable {
	private PacketListener listener;
	public static final Parcelable.Creator<VVPacketListenerAdapter> CREATOR = new Creator<VVPacketListenerAdapter>() {
		@Override
		public VVPacketListenerAdapter[] newArray(int size) {
			return new VVPacketListenerAdapter[size];
		}
		@Override
		public VVPacketListenerAdapter createFromParcel(Parcel source) {
			return new VVPacketListenerAdapter(source);
		}
	};

	public VVPacketListenerAdapter(Parcel source) {
		listener = (PacketListener) source.readValue(PacketListener.class
				.getClassLoader());
	}
	public VVPacketListenerAdapter(PacketListener listener) {
		this.listener = listener;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(listener);
	}
	public PacketListener getListener() {
		return listener;
	}
	public void setListener(PacketListener listener) {
		this.listener = listener;
	}
}
