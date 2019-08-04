/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceManager;

/**
 * The message service is a MEEP system service that handles messaging between a MEEP
 * and the server. You can register with this service to handle the specific type
 * of message received from server.
 * 
 * Certain system messages are restricted. That is, even though you can register
 * with the service, the service does not guarantee the message is delivered to you
 * because you may not possess sufficient permission.
 * 
 * @author Stanley Lam
 */
public class MessageService extends Service {
	
	private final String TAG = "MessageService";
	
	/**
	 * The actions that the service subscribes to
	 */
	private static enum Action {
		NETWORK_STATE_CHANGE(WifiManager.NETWORK_STATE_CHANGED_ACTION),
		CONNECTION_STATE_CHANGE(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		
		private String name;

		Action(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		public static Action fromString(String text) {
	    if (text != null) {
	      for (Action b : Action.values()) {
	        if (text.equalsIgnoreCase(b.name)) {
	          return b;
	        }
	      }
	    }
	    return null;
	  }
	}
	
	/**
	 * This is the object that receives interactions from clients.
	 */
	private final IMessageService.Stub mBinder = new IMessageService.Stub() {
		
		@Override
		public boolean isConnected() throws RemoteException {
			return mHandler == null ? false : mHandler.isConnected();
		}

		@Override
		public void sendMessage(Message message) throws RemoteException {
			// Delegates handling of the message to Handler
			mHandler.sendMessage(message);
		}

		@Override
		public void registerCallback(ComponentName componentName, MessageFilter filter) throws RemoteException {
			// Register the given component to handle messages filtered by the given filter
			if (mHandler != null) {
				mHandler.registerReceiver(componentName, filter);
			}
		}

		@Override
		public void unregisterCallback(ComponentName componentName) throws RemoteException {
			// Unregister a previously registered component
			if (mHandler != null) {
				mHandler.unregisterReceiver(componentName);
			}
		}
		
		@Override 
		public void reconnect(int environmentInt) {
			if (mHandler != null)
				mHandler.reconnect(environmentInt);
		}
		
	};
	
	/**
	 * For handling messaging between the device and server
	 */
	private ServerMessageHandler mHandler;
	
	/**
	 * The BroadcastReceiver that receives the broadcasted intents
	 */
	private LocalReceiver mReceiver = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Message service is starting");
		
		if (mHandler != null) {
			mHandler.stop();
			mHandler = null;
		}
		mHandler = new ServerMessageHandler(this);
		mHandler.start(!Build.DEBUG);
		
		// Register receivers to receive server messages
		// ---------------------------------------------
		
		// Register AccountService to receive authentication messages
		MessageFilter filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_ACCOUNT);
		ComponentName componentName = new ComponentName(ServiceManager.PACKAGE_BASE, ServiceManager.ACCOUNT_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		// Register PermissionService to receive "parent" and permission settings 
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_PARENTAL);
		filter.addOperation(Message.OPERATION_CODE_CHECK_URL);
		filter.addOperation(Message.OPERATION_CODE_GET_PERMISSION);
		filter.addOperation(Message.OPERATION_CODE_GET_BLACKLIST);
		componentName = new ComponentName(ServiceManager.PACKAGE_BASE, ServiceManager.PERMISSION_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_SYSTEM);
		filter.addOperation(Message.COMMAND_GET_APPS_CATEGORY);
		mHandler.registerReceiver(componentName, filter);
		
		// Register RecommendationService to process "recommendations"
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_PARENTAL);
		filter.addOperation(Message.OPERATION_CODE_GET_RECOMMENDATIONS);
		componentName = new ComponentName(ServiceManager.PACKAGE_BASE, ServiceManager.RECOMMENDATION_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		// Register SystemService to process "system" commands
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_SYSTEM);
		filter.addOperation(Message.OPERATION_CODE_RUN_COMMAND);
		componentName = new ComponentName(ServiceManager.PACKAGE_BASE, ServiceManager.SYSTEM_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		// Register Communicator to process run-commands for get-friend list
		componentName = new ComponentName(ServiceManager.COMMUNICATOR_BASE, ServiceManager.COMMUNICATOR_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		// Register LogService to process logs
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_SYSTEM);
		filter.addOperation(Message.OPERATION_CODE_LOG);
		componentName = new ComponentName(ServiceManager.PACKAGE_BASE, ServiceManager.LOG_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		// Register Communicator to process messages
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_INSTANT_MESSAGING);
		componentName = new ComponentName(ServiceManager.COMMUNICATOR_BASE, ServiceManager.COMMUNICATOR_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		// Register MeepStore to process store messages
		filter = new MessageFilter();
		filter.addProcess(Message.PROCESS_STORE);
		componentName = ComponentName.unflattenFromString(ServiceManager.STORE_SERVICE);
		mHandler.registerReceiver(componentName, filter);
		
		if (mReceiver == null) {
			// Register local {@link BroadcastReceiver}
			mReceiver = new LocalReceiver();
			IntentFilter intentFilter = new IntentFilter();
			for (Action action : Action.values()) {
				intentFilter.addAction(action.toString());
			}
			registerReceiver(mReceiver, intentFilter);
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// Return the binder that exposes the interfaces accessible by
		// other packages
		return mBinder;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Message service is shutting down");
		
		mHandler.stop();
		mHandler = null;
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
		
		// Unregister previously registered receiver(s)
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
	
	/**
	 * Handles the command as specified in the {@code} intent 
	 * 
	 * @param intent The intent supplied to {@link #onStartCommand(Intent, int, int)}
	 */
	private void handleCommand(Intent intent) {
		// Quick return if there is nothing to process
		if (intent == null) {
			return;
		}
		
		try {
			switch (Action.fromString(intent.getAction())) {
			case CONNECTION_STATE_CHANGE:
				boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
				if (!connected) {
					Log.d(TAG, "Connection signal is lost, stopping message service...");
					mHandler.stop();
				}
				break;
			case NETWORK_STATE_CHANGE:
				NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (networkInfo != null && networkInfo.isConnected()) {
					Log.d(TAG, "Wifi connection established, starting network services...");
					
					// Try to establish connection with server
					if (mHandler != null && !mHandler.isConnected() && !mHandler.isConnecting()) {
						Log.d(TAG, "Handler is starting...");
						mHandler.start(!Build.DEBUG);
					}
				} else {
					Log.d(TAG, "Wifi connection is lost, stopping network services...");
					// Stops network tasks and disconnect from server
					mHandler.stop();
				}
				break;
			}
		} catch (Exception ex) {
			// Ignore. 
			Log.e(TAG, "Service cannot understand action " + intent.getAction());
		}
	}
	
	/**
	 * The local receiver that receive intents sent by sendBroadcast(). The handling
	 * of the intents are delegated to the service
	 */
	private final class LocalReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// Delegate intent processing to the service
			if (intent != null && context != null) {
				try {
					Class<?> clazz = MessageService.class;
					intent.setComponent(new ComponentName(ServiceManager.PACKAGE_BASE, clazz.getName()));
					context.startService(intent);
				} catch (Exception ex) {
					// Ignored
					Log.e(TAG, "Receiver cannot start intent because " + ex + " occurred");
				}
			}	
		}

	}
	
}
