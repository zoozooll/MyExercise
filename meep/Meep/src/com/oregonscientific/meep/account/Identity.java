/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * An Identity represents a person on the device
 */
public class Identity implements Parcelable {
	
	private final String name;
	
	/** Package scoped such that the service can authenticate an Identity */
	final String password;
	
	Identity(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	Identity(Parcel source) {
		name = source.readString();
		password = source.readString();
	}
	
	/**
	 * Returns the name of of this #Identity
	 * @return
	 */
	public final String getName() {
		return name;
	}

	@Override
	public int describeContents() {
		return name != null ? name.hashCode() : 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(password);
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Identity> CREATOR = new Creator<Identity>() {
		
		@Override
		public Identity createFromParcel(Parcel source) {
			return new Identity(source);
		}
		
		@Override
		public Identity[] newArray(int size) {
			return new Identity[size];
		}
	};

}
