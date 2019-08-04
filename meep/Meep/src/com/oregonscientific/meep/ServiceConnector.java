/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep;

import java.util.Vector;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.util.Log;

/**
 * Finds and connects to the given service.
 */
public class ServiceConnector implements ServiceConnection {
	
	private final String TAG = "ServiceConnector";
	
	public static final String EXTRA_SERVICE_VERSION = "serviceVersion";
	
	private final Context mContext;
	private final ComponentName mName;
	private final Vector<Callback> mCallbacks = new Vector<Callback>();
	
	private final Object mLock = new Object();
	
	private boolean isBounded = false;
	
	// Should be synchronized with lock
	private IBinder mBinder;
	private int mVersion = Integer.MIN_VALUE;
	
	/**
	 * Constructor
	 * 
	 * @param context the context to use
	 * @param name the name of the serivce to connect to
	 */
	public ServiceConnector(Context context, ComponentName name) {
		mContext = context;
		mName = name;
	}
	
	/**
	 * @return the base context as set by the constructor
	 */
	public Context getBaseContext() {
		return mContext;
	}
	
	/**
	 * Retrieves the concrete component associated with the connector  
	 */
	public ComponentName getComponent() {
		return mName;
	}
	
	/**
	 * Registers the {@code callback} to be invoked when a connection to service
	 * is established or lost 
	 * 
	 * @param callback the callback to invoke
	 */
	public void addCallback(Callback callback) {
		synchronized (mLock) {
			if (callback != null && !mCallbacks.contains(callback)) {
				mCallbacks.add(callback);
			}
		}
	}
	
	/**
	 * Unregister the {@code callback} from being called 
	 * 
	 * @param callback the callback to remove
	 */
	public void removeCallback(Callback callback) {
		synchronized (mLock) {
			if (callback != null && mCallbacks.contains(callback)) {
				mCallbacks.remove(callback);
			}
		}
	}
	
	/**
	 * Connects to the given service
	 */
	public void connect() {
		// Quick return if there is nothing process
		if (mContext == null || mName == null) {
			return;
		}
		
		ServiceInfo info = null;
		try {
			Log.i(TAG, "Retrieving information for " + mName);
			info = mContext.getPackageManager().getServiceInfo(mName, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException ex) {
			Log.e(TAG, mName + " is not a service");
		}
		
		// Check version
		int version = 0;
		if (info != null && info.metaData != null) {
			version = info.metaData.getInt(EXTRA_SERVICE_VERSION, 0);
		}
		
		if (version > mVersion) {
			bindToServiceLocked(version);
		}
	}
	
	/**
	 * Disconnect from the service. This unbinds the context from the service. You will no
	 * longer receive callbacks even if the service was restarted
	 */
	public void disconnect() {
		unbindLocked();
	}
	
	/**
	 * Returns a reference to the named service
	 * 
	 * @return a reference to the service, or <code>null</code> if the service does not exist
	 */
	public IBinder getBinder() {
		synchronized (mLock) {
			return mBinder;
		}
	}
	
	/**
	 * Returns whether or not the connector is bound to the service
	 * 
	 * @return true if the connector is bound, false otherwise
	 */
	public boolean isBounded() {
		return isBounded && getBinder() != null;
	}
	
	private void bindToServiceLocked(int version) {
		// Disconnect with any previously connected service
		unbindLocked();
		
		Intent intent = new Intent();
		intent.setComponent(mName);
		mVersion = version;
		mContext.bindService(intent, this, Context.BIND_AUTO_CREATE | Context.BIND_DEBUG_UNBIND);
	}
	
	private void unbindLocked() {
		synchronized (mLock) {
			mVersion = Integer.MIN_VALUE;
			if (mBinder != null) {
				mContext.unbindService(this);
			}
		}
	}
	
	private void invokeCallbacks() {
		synchronized (mLock) {
			for (Callback callback : mCallbacks) {
				callback.handleConnection(mBinder != null);
			}	
		}
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		synchronized (mLock) {
			mBinder = service;
			isBounded = true;
		}
		invokeCallbacks();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		synchronized (mLock) {
			mBinder = null;
			isBounded = false;
		}
		invokeCallbacks();
	}
	
	/**
	 * Callback interface that can be used to be notified when a connection
	 * with the service is established or lost 
	 */
	public interface Callback {
		
		/**
		 * Called when connection with a remote service is established or
		 * lost
		 * 
		 * @param connected true if connction to the service is established, false if connection is lost
		 */
		public void handleConnection(boolean connected);
		
	}

}
