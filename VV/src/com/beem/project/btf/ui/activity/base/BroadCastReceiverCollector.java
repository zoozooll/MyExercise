package com.beem.project.btf.ui.activity.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;

public class BroadCastReceiverCollector {
	private final Map<String, VVBaseBroadCastReceiver> castReceivers;
	private final Context context;

	protected BroadCastReceiverCollector(Context context) {
		super();
		this.context = context;
		castReceivers = Collections
				.synchronizedMap(new HashMap<String, VVBaseBroadCastReceiver>());
	}
	public void registerBroadCastReceiver(VVBaseBroadCastReceiver castReceiver,
			IntentFilter filter) {
		registerBroadCastReceiverInner(castReceiver, filter);
		castReceivers.put(castReceiver.getId(), castReceiver);
	};
	protected final void unRegisterBroadCastReceiver(
			VVBaseBroadCastReceiver castReceiver) {
		unRegisterBroadCastReceiverInner(castReceiver);
		castReceivers.remove(castReceiver.getId());
	};
	public void unRegisterBroadCastReceiverInner(
			VVBaseBroadCastReceiver castReceiver) {
		if (!castReceivers.containsKey(castReceiver.getId())) {
			//LogUtils.e("the castReceiver has unRegister! id:" + castReceiver.getId(), 3);
			return;
		}
		if (castReceiver.isLocal()) {
			LocalBroadcastManager.getInstance(context).unregisterReceiver(
					castReceiver);
		} else {
			context.unregisterReceiver(castReceiver);
		}
	}
	public void unRegisterBroadCastReceivers() {
		for (Iterator<Entry<String, VVBaseBroadCastReceiver>> it = castReceivers
				.entrySet().iterator(); it.hasNext();) {
			Entry<String, VVBaseBroadCastReceiver> entry = it.next();
			VVBaseBroadCastReceiver receiver = entry.getValue();
			unRegisterBroadCastReceiverInner(receiver);
			it.remove();
		}
		if (!castReceivers.isEmpty()) {
			throw new IllegalStateException(
					"castReceivers's size should be zero");
		}
	}
	private void registerBroadCastReceiverInner(
			VVBaseBroadCastReceiver castReceiver, IntentFilter filter) {
		if (castReceivers.containsKey(castReceiver.getId())) {
			//LogUtils.e("the castReceiver has register,please unRegister first! id:" + castReceiver.getId(), 3);
			return;
		}
		if (castReceiver.isLocal()) {
			LocalBroadcastManager.getInstance(context).registerReceiver(
					castReceiver, filter);
		} else {
			context.registerReceiver(castReceiver, filter);
		}
	}
}
