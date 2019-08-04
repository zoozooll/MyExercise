package com.mogoo.market.network.http;

import java.io.InputStream;

public interface NetworkTaskListener {
	/**
	 * 异步任务类回调该方法，并返回服务器响应的数据。 调用者可以在此处理响应数据
	 */
	abstract void onLoadingComplete(InputStream responseData);

	abstract void onLoadingStarted();

	abstract void onLoadingFailed();

	abstract void onLoadingCancelled();
}
