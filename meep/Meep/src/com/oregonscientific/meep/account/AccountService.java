/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.util.SystemUtils;

public class AccountService extends DatabaseService<AccountDatabaseHelper> {

	private final String TAG = getClass().getSimpleName();
	
	/**
	 * BroadCast Receiver for receiving notifications about network status change,
	 * and perform login / logout operation
	 */
	private ConnectionStateChangeReceiver mReceiver = null;
	
	/**
	 * For handling messaging Message service and Account service
	 */
	private AccountServiceHandler mHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mHandler = new AccountServiceHandler(this, getHelper());
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(MessageManager.SERVER_CONNECTION_STATE_CHANGED_ACTION);
		mReceiver = new ConnectionStateChangeReceiver();
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		// Server messages will be inside the intent's extras
		handleCommand(intent);
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mHandler.clearCallbacks();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
		
		// Unregister previously registered receiver(s)
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
	
	/**
	 * This is the object that receives interactions from clients.
	 */
	private IAccountService.Stub mBinder = new IAccountService.Stub() {

		/**
		 * Sign in with MEEP Server
		 * 
		 * @param user
		 *            User object for sign in
		 * @param retrievePermissions
		 *            {@code true} to ask
		 *            {@link com.oregonscientific.meep.home.permission.PermissionManager}
		 *            to retrieve permission settings from server, {@code false}
		 *            otherwise. By default, this is {@code false}
		 */
		@Override
		public void signIn(Account account, boolean retrievePermissions) throws RemoteException {
			if (mHandler != null) {
				if (account == null) {
					mHandler.signIn(retrievePermissions);
				} else {
					mHandler.signIn(account, retrievePermissions);
				}
			}
		}

		/**
		 * Sign out Locally
		 * 
		 * @return true if the current logged in user is signed-out, false
		 * if no user was signed in or there was an error signing out.
		 */
		@Override
		public boolean signOut() throws RemoteException {
			boolean isSuccess = false;
			if (mHandler != null) {
				isSuccess = mHandler.signOut();
			}
			return isSuccess;
		}

		/**
		 * Returns a value indicating whether a user's credentials are valid
		 * 
		 * @return true if the specified user name and password are valid, false otherwise
		 */
		@Override
		public boolean authenticate(Identity identity) throws RemoteException {
			boolean result = false;
			if (mHandler != null) {
				result = mHandler.authenticate(identity);
			}
			return result;
		}

		/**
		 * Modify the user profile and update server's data
		 *  
		 * @param user User object that is going to be updated in both server and client
		 */
		@Override
		public void updateUserAccount(Account account) throws RemoteException {
			if (mHandler != null) {
				mHandler.updateUserAccount(account);
			}
		}

		/**
		 * Retrieve currently logged in user in db
		 *  
		 * @return  The currently logged in user, null if no user signed in. 
		 */
		@Override
		public Account getLoggedInAccount() throws RemoteException {
			Account result = null;
			if (mHandler != null) {
				result = mHandler.getLoggedInAccount();
			}
			return result;
		}
		
		/**
		 * Returns the last logged in Account
		 */
		@Override
		public Account getLastLoggedInAccount() throws RemoteException {
			Account result = null;
			if (mHandler != null) {
				result = mHandler.getLastLoggedInAccount();
			}
			return result;
		}
		
		/**
		 * Returns the system default Account
		 */
		@Override
		public Account getDefaultAccount() throws RemoteException {
			Account result = null;
			if (mHandler != null) {
				result = mHandler.getDefaultAccount();
			}
			return result;
		}

		/** 
		 * Register as a callback for Account Service to receive the result after asynchronous server messaging
		 * 
		 * @param callback An interface of type IAccountServiceCallback
		 */
		@Override
		public void registerCallback(IAccountServiceCallback callback) throws RemoteException {
			if (mHandler != null) {
				mHandler.registerCallback(callback);
			}
		}

		/** 
		 * Unregister a previously registered callback in Account Service
		 * 
		 * @param callback An interface of type IAccountServiceCallback
		 */
		@Override
		public void unregisterCallback(IAccountServiceCallback callback) throws RemoteException {
			if (mHandler != null) {
				mHandler.unregisterCallback(callback);
			}
		}
	};
	
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
		
		// Only handles messages received from MessageService
		if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
			if (mHandler != null) {
				mHandler.handleMessage(message);
			}
		}
	}
	
	/**
	 * The local receiver that receive intents sent by sendBroadcast(). The handling
	 * of the intents are delegated to the service
	 */
	private final class ConnectionStateChangeReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// Delegate intent processing to the service
			if (intent != null && context != null && mHandler != null) {
				try {
					Boolean isConnected = intent.getBooleanExtra(MessageManager.EXTRA_SERVER_CONNECTED, false);
					if (isConnected == true) {
						// Sign-in with the last signed in account. If the system was restored, retrieve
						// permission settings from remote server as well
						boolean retrievePermissions = SystemUtils.isSystemRestored(AccountService.this);
						mHandler.signIn(retrievePermissions);
					} else {
						mHandler.signOut();
					}
					
				} catch (Exception ex) {
					// Ignored
					Log.e(TAG, "Receiver cannot start intent because " + ex + " occurred");
				}
			}	
		}

	}
}
