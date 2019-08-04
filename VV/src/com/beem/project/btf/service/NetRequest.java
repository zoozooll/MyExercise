package com.beem.project.btf.service;

import org.jivesoftware.smack.packet.Packet;

public abstract class NetRequest {
	public static final long ASYN_REQ_WAIT_TIME = 30000;

	public static enum ReqState {
		not_started, loading, finished, failed
	}

	public ReqState mReqState = ReqState.not_started;

	/**
	 * 发送请求，将在AsynNet新线程里调用 实现此方法时需要注意线程安全
	 */
	public abstract NetResult doRequest(Packet packet);
	/**
	 * 执行成功，返回结果，在主线程里通过该方法回调，
	 */
	public void onResult(NetResult result) {
		if (result != null) {
			result.onResult();
		}
	}
	/**
	 * 执行失败，在主线程里通过该方法回调
	 */
	public abstract void onFaild();
}
