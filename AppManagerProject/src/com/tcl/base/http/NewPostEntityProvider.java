package com.tcl.base.http;

import android.os.Handler;
import android.os.Looper;

import com.tcl.framework.network.http.NetworkError;

public abstract class NewPostEntityProvider<T> extends StringResponsePostEntityProvider {

	protected T result;

	protected String errorMsg;

	public void setResult(T result) {
		this.result = result;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	private IProviderCallback<T> callback;

	private Handler handler;

	public NewPostEntityProvider(IProviderCallback<T> callback) {
		super();
		this.callback = callback;
		handler = new Handler(Looper.getMainLooper());
	}

	@Override
	public void onSuccess() {
		super.onSuccess();
		if (callback != null) {
			if (isMainThread()) {
				try {
					callback.onSuccess(result);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							callback.onSuccess(result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void onCancel() {
		super.onCancel();
		if (callback != null) {
			if (isMainThread()) {
				try {
					callback.onCancel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							callback.onCancel();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	@Override
	public void onError(final int err) {
		super.onError(err);
		if (callback != null) {
			if (isMainThread()) {
				try {
					callback.onFailed(err, errorMsg, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							callback.onFailed(err, errorMsg, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	public void send() {
		EntityLoader entityLoader = new EntityLoader(this);
		boolean ret = entityLoader.load();
		if (!ret) {
			onError(NetworkError.FAIL_UNKNOWN);
		}
	}

	private static boolean isMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}
}
