/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes notifications canceled
 */
public class CancellationInfo implements Parcelable {
	
	public enum Cause {
		/** A specific notification was canceled */
		ID, 
		/** All notifications for a particular user was canceled */
		USER,
		/** A specific kind of notification was canceled */
		KIND,
		/** All notifications in the system were canceled */
		ALL
	}
	
	private Cause mCause;
	private String mData;
	
	CancellationInfo(Cause cause, String data) {
		mCause = cause;
		mData = data;
	}
	
	/**
	 * Return the data associated with this object. It can be an ID of a notification, ID of the user
	 * whose notifications are canceled or the kind of notifications canceled 
	 * 
	 * @return
	 */
	public String getData() {
		synchronized (this) {
			return mData;
		}
	}
	
	/**
	 * Return the cause of the cancellation
	 */
	public Cause getCause() {
		synchronized (this) {
			return mCause;
		}
	}

	/**
     * Implement the Parcelable interface
     * @hide
     */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		synchronized (this) {
			dest.writeString(mCause.name());
			if (mData != null) {
				dest.writeInt(1);
				dest.writeString(mData);
			} else {
				dest.writeInt(0);
			}
		}
	}
	
	@Override
    public String toString() {
		synchronized (this) {
			StringBuilder builder = new StringBuilder("CancellationInfo: ");
			builder.append("cause: ")
					.append(getCause().name())
					.append(", data: ")
					.append(getData());
			return builder.toString();
		}
	}
	
	/**
	 * Unflatten the notification from a parcel.
	 */
	CancellationInfo(Parcel parcel) {
		mCause = Cause.valueOf(parcel.readString());
		
		if (parcel.readInt() != 0) {
			mData = parcel.readString();
		}
	}
	
	/**
	 * Parcelable.Creator that instantiates CancellationInfo objects
	 */
	public static final Parcelable.Creator<CancellationInfo> CREATOR = new Parcelable.Creator<CancellationInfo>() {
		public CancellationInfo createFromParcel(Parcel parcel) {
			return new CancellationInfo(parcel);
		}

		public CancellationInfo[] newArray(int size) {
			return new CancellationInfo[size];
		}
	};

}
