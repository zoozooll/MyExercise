package com.dvr.android.dvr.util;

import android.util.Log;

public class UsbCameraCallBack {
	static UsbCameraCallBack mUsbCameraCall;

	private OnMethodCallback mCallback;

	private UsbCameraCallBack() {

	}

	public static UsbCameraCallBack getInstance() {
		if (mUsbCameraCall == null) {
			mUsbCameraCall = new UsbCameraCallBack();
		}
		return mUsbCameraCall;
	}

	public void invokeMethod(boolean bool) {
		if (mCallback != null) {
			mCallback.doMethod(bool);
		}
	}
	
	public void BatteryMethod(boolean bool) {
		if (mCallback != null) {
			mCallback.doBatteryMethod(bool);
		}
	}
	
	public void doRecStartMethod(boolean bool) { Log.i("PLJ", "libucamera---->doRecStartMethod:222:"+(mCallback != null));
		if (mCallback != null) {
			mCallback.doRecStartMethod(bool);
		}
	}

	public void setOnMethodCallback(OnMethodCallback callback) {
		mCallback = callback;
	}

	public static interface OnMethodCallback {
		public void doMethod(boolean bool);
		public void doBatteryMethod(boolean bool);
		public void doRecStartMethod(boolean bool);
	}

}
