package com.beem.project.btf.ui.receiver.base;

import org.jivesoftware.smack.util.StringUtils;

import android.content.BroadcastReceiver;

/**
 * @ClassName: VVBaseBroadCastReceiver
 * @Description: 封装Android广播
 * @author: yuedong bao
 * @date: 2015-3-23 下午4:05:59
 */
public abstract class VVBaseBroadCastReceiver extends BroadcastReceiver {
	protected boolean isLocal;
	private static String prefix = StringUtils.randomString(5);
	private static long id = 0;
	protected final String mId;

	// isLocal ：true：本地广播 ;false：普通广播
	public VVBaseBroadCastReceiver(boolean isLocal) {
		super();
		this.isLocal = isLocal;
		this.mId = nextID();
	}
	public boolean isLocal() {
		return isLocal;
	}
	public String getId() {
		return mId;
	}
	private static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}
}
