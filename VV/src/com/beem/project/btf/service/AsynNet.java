package com.beem.project.btf.service;

import org.jivesoftware.smack.packet.Packet;

import com.beem.project.btf.service.NetRequest.ReqState;
import android.os.AsyncTask;

/**
 * @author xun zhong
 */
public class AsynNet extends AsyncTask<Packet, Void, NetResult> {
	private NetRequest mReq;

	public AsynNet(NetRequest req) {
		mReq = req;
	}
	@Override
	protected NetResult doInBackground(Packet... params) {
		// TODO Auto-generated method stub
		Packet packet = params[0];
		assert (packet != null);
		return mReq.doRequest(packet);
	}
	@Override
	protected void onPostExecute(NetResult result) {
		// TODO Auto-generated method stub
		if (result != null) {
			mReq.mReqState = ReqState.finished;
			mReq.onResult(result);
		} else {
			mReq.mReqState = ReqState.failed;
			mReq.onFaild();
		}
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mReq.mReqState = ReqState.loading;
	}
}
