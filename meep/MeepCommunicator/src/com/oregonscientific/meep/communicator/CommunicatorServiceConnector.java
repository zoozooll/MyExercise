package com.oregonscientific.meep.communicator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.oregonscientific.meep.communicator.CommunicatorService.LocalBinder;

public class CommunicatorServiceConnector {

	private Context mContext;
	private LocalBinder mBinder;
	private ServiceConnection mServiceConnection;
	private boolean isBound = false;
	private ICommunicatorServiceCallback mCallback;
	
	public CommunicatorServiceConnector(Context context, ICommunicatorServiceCallback callback) {
		mContext = context;
		mCallback = callback;
	}
	
	/**
	 * Bind the service, communicator app should call this in the onCreate() / onStart() method in 
	 * order to work with the service 
	 * 
	 */
	public void connect() {
		
		final Intent intent = new Intent(mContext, CommunicatorService.class);

		mServiceConnection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {

				if (mCallback != null) {
					mCallback.onServiceDisconnected();
				}
				mBinder = null;
				isBound = false;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mBinder = (LocalBinder) service;
				isBound = true;
				
				if (mCallback != null) {
					CommunicatorService communicatorSerivce = getService();
					communicatorSerivce.setCallback(mCallback);
					mCallback.onServiceConnected();
				}
			}

		};
		
		mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		
	}
	
	/**
	 * Unbind the service, communicator app should call this in the onDestory()/onStop() method in 
	 * order to clean up the service connection
	 */
	public void disconnect() {
		
		mCallback = null;
		getService().setCallback(null);
		mContext.unbindService(mServiceConnection);
		
	}
	
	/**
	 * Retrieve the communicator service instance in order to call the method synchronously,
	 * target should check return value is null or not, null means the service is not bound 
	 * or encountered other errors
	 * 
	 * @return Communicator service instance, null if the service is not ready / not yet bound 
	 */
	public CommunicatorService getService() {
		
		if (!isBound)
			return null;
		
		return mBinder.getService();
	}
	
}
