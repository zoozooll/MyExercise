package com.beem.project.btf.service;

import org.jivesoftware.smack.XMPPException;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: VVXmppExceptionAdapter
 * @Description: XmppException包裹类
 * @author: yuedong bao
 * @date: 2015-6-29 下午6:05:06
 */
public class VVXmppExceptionAdapter implements Parcelable {
	private XMPPException exception;

	public VVXmppExceptionAdapter() {
	}
	public VVXmppExceptionAdapter(XMPPException exception) {
		super();
		this.exception = exception;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeValue(exception);
	}

	public static final Parcelable.Creator<VVXmppExceptionAdapter> CREATOR = new Creator<VVXmppExceptionAdapter>() {
		@Override
		public VVXmppExceptionAdapter[] newArray(int size) {
			return new VVXmppExceptionAdapter[size];
		}
		@Override
		public VVXmppExceptionAdapter createFromParcel(Parcel source) {
			VVXmppExceptionAdapter adapter = new VVXmppExceptionAdapter();
			adapter.readFromParcel(source);
			return adapter;
		}
	};

	protected void readFromParcel(Parcel source) {
		exception = (XMPPException) source.readValue(XMPPException.class
				.getClassLoader());
	}
	public XMPPException getException() {
		return exception;
	}
	public void setException(XMPPException exception) {
		this.exception = exception;
	}
}
