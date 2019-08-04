package com.tcl.base.http;

public interface IProviderCallback<T> {
	
	void onSuccess(T obj);
	
	void onFailed(int code, String msg, Object obj);

	void onCancel();
}
