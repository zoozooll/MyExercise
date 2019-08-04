package com.beem.project.btf.service;

import org.jivesoftware.smack.filter.PacketFilter;

import android.os.Parcel;
import android.os.Parcelable;

public class VVPacketFilterAdapter implements Parcelable {
	private PacketFilter filter;
	public static final Parcelable.Creator<VVPacketFilterAdapter> CREATOR = new Creator<VVPacketFilterAdapter>() {
		@Override
		public VVPacketFilterAdapter[] newArray(int size) {
			return new VVPacketFilterAdapter[size];
		}
		@Override
		public VVPacketFilterAdapter createFromParcel(Parcel source) {
			return new VVPacketFilterAdapter(source);
		}
	};

	public VVPacketFilterAdapter(Parcel source) {
		filter = (PacketFilter) source.readValue(PacketFilter.class
				.getClassLoader());
	}
	public VVPacketFilterAdapter(PacketFilter filter) {
		this.filter = filter;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(filter);
	}
	public PacketFilter getFilter() {
		return filter;
	}
	public void setFilter(PacketFilter filter) {
		this.filter = filter;
	}
}
