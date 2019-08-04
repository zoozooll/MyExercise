package com.beem.project.btf.service;

import android.os.Parcel;
import android.os.Parcelable;

public class PacketResult implements Parcelable {
	private boolean ok;
	private String error;

	public PacketResult(Parcel source) {
		readFromParcel(source);
	}
	public PacketResult() {
	}
	public PacketResult(boolean ok, String error) {
		this.ok = ok;
		this.error = error;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(ok ? 0 : 1);
		dest.writeString(error);
	}
	public void readFromParcel(Parcel dest) {
		ok = dest.readInt() == 0;
		error = dest.readString();
	}

	public static final Parcelable.Creator<PacketResult> CREATOR = new Parcelable.Creator<PacketResult>() {
		@Override
		public PacketResult createFromParcel(Parcel source) {
			return new PacketResult(source);
		}
		@Override
		public PacketResult[] newArray(int size) {
			return new PacketResult[size];
		}
	};

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
}
