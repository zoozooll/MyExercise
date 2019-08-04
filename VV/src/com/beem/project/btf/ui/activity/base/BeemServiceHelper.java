package com.beem.project.btf.ui.activity.base;

import org.jivesoftware.smack.packet.XMPPError;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.aidl.IConnectionListener;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.utils.LogUtils;

import de.greenrobot.event.EventBus;

/**
 * @func xmppserice绑定帮助类
 * @author yuedong bao
 * @time 2015-1-5 下午3:20:05
 */
public class BeemServiceHelper {
	private volatile static BeemServiceHelper instance;
	private boolean isBind;
	private IXmppFacade xmppFacade;
	private BeemServiceConnection mServConn = new BeemServiceConnection();
	private Context context;
	private static final Intent SERVICE_INTENT = new Intent();
	static {
		SERVICE_INTENT.setComponent(new ComponentName("com.beem.project.btf",
				"com.beem.project.btf.BeemService"));
	}
	private XSBroadcastReciver xsBroadcastReceiver;
	private IXSListener listener;

	private BeemServiceHelper(Context context) {
		super();  
		this.context = context;
	}

	public class XSBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (listener != null) {
				listener.onReceive(intent.getAction(), intent);
			}
		}
	}

	public interface IXSListener {
		void onReceive(String action, Intent intent);
	}

	public void registerXSBroadCastReciever(String[] actions,
			IXSListener listener) {
		if (actions != null && listener != null) {
			this.listener = listener;
			if (xsBroadcastReceiver == null) {
				xsBroadcastReceiver = new XSBroadcastReciver();
			}
			IntentFilter filter = new IntentFilter();
			for (String action : actions) {
				filter.addAction(action);
			}
			context.registerReceiver(xsBroadcastReceiver, filter);
		} else {
			throw new IllegalArgumentException(
					"the receiverEntity actions and listern must not be null~");
		}
	}
	// 反注册
	public void unRegisterXSBroadCastReciever() {
		if (xsBroadcastReceiver != null) {
			context.unregisterReceiver(xsBroadcastReceiver);
		}
	}
	public IXmppFacade getXmppFacade() {
		if (xmppFacade == null) {
			bindBeemService();
		}
		return xmppFacade;
	}
	public void xmppLoginAsync(final String jid, final String pass) {
		try {
			xmppFacade.connectAsyncEx(jid, pass,
					new IConnectionListener.Stub() {
						@Override
						public void onResult(final String result)
								throws RemoteException {
							if (result == null) {
								EventBus.getDefault()
										.post(new EventBusData(
												EventAction.LOGIN_SUCCESS, null));
								LoginManager.getInstance().onLogin(jid, pass);
							} else if (result
									.equals(XMPPError.Condition.remote_server_timeout.value)
									|| result
											.equals(XMPPError.Condition.remote_server_error.value)
									|| result
											.equals(XMPPError.Condition.request_timeout.value)) {
								EventBus.getDefault()
										.post(new EventBusData(
												EventAction.LOGIN_TIMEOUT, null));
							} else {
								EventBus.getDefault()
										.post(new EventBusData(
												EventAction.LOGIN_FAILED, null));
								xmppFacade.disconnect();
								LoginManager.getInstance().onLogout();
							}
						}
					});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public boolean xmppLoginSilently() {
		if (!LoginManager.getInstance().isLogined()) {
			return false;
		}
		synchronized (this) {
			boolean res = false;
			try {
				if (xmppFacade.isAuthentificated()) {
					return true;
				}
				res = xmppFacade.connectSync();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return res;
		}
	}
	public void xmppLogout() {
		if (xmppFacade != null) {
			try {
				xmppFacade.disconnect();
				LoginManager.getInstance().onLogout();
				EventBus.getDefault().post(
						new EventBusData(EventAction.LOGOUT, null));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The ServiceConnection used to connect to the Beem service.
	 */
	public final class BeemServiceConnection implements ServiceConnection {
		/**
		 * Constructor.
		 */
		public BeemServiceConnection() {
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogUtils.d("onServiceConnected");
			xmppFacade = IXmppFacade.Stub.asInterface(service);
		}
		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogUtils.d("onServiceDisconnected");
			xmppFacade = null;
		}
	}
	
	public boolean isAuthentificated() {
		if (xmppFacade != null) {
			try {
				return xmppFacade.isAuthentificated();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void bindBeemService(int... paramInt) {
		context.bindService(SERVICE_INTENT, mServConn,
					paramInt.length > 0 ? paramInt[0]
							: Context.BIND_AUTO_CREATE);
	}
	public void unBindBeemSerivice() {
		context.unbindService(mServConn);
	}
	public static BeemServiceHelper getInstance(Context c) {
		if (instance == null) {
			synchronized (BeemServiceHelper.class) {
				if (instance == null) {
					instance = new BeemServiceHelper(c);
				}
			}
		}
		return instance;
	}

	public static interface OnLoginResult {
		public void onLoginSuccess();
		public void onLoginFailed(String error);
		public void onTimeout();
	}
}
