/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.content.ComponentName;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceAgent;
import com.oregonscientific.meep.ServiceConnector;

/**
 * This class provides the primary API for communicating with the MEEP server.
 * Obtain an instance of this class by calling 
 * {@link com.oregonscientific.meep.ServiceManager#getService(android.content.Context, String)}
 */
public class MessageManager extends ServiceAgent<IMessageService> {
	
	private static final String TAG = "MessageManager";
	
	/**
	 * Broadcast intent action indicating that connection with MEEP server is
	 * connected or closed. One extra provides this state as a boolean, where {@code true}
	 * indicates a connection with MEEP server is established.
	 * 
	 * @see #EXTRA_SERVER_CONNECTED
	 */
	public static final String SERVER_CONNECTION_STATE_CHANGED_ACTION = 
		"com.oregonscientific.meep.msm.SERVER_CONNECTION_STATE_CHANGED";
	
	/**
	 * The lookup key for an int that indicates whether connection with MEEP server
	 * is established. {@code true} means a connection is establieshed. Retrieve it with 
	 * {@link android.content.Intent#getIntExtra(String, int)}.
	 * 
	 * @see #SERVER_CONNECTION_STATE_CHANGED_ACTION
	 */
	public static final String EXTRA_SERVER_CONNECTED = "connected";
	
	/**
	 * The intent action indicating that a message is received from MEEP server. One
	 * extra provides the message the message as string.
	 * 
	 * @see #EXTRA_MESSAGE
	 */
	public static final String MESSAGE_RECEIVED_ACTION =
		"com.oregonscientific.meep.msm.MESSAGE_RECEIVED";
	
	/**
	 * The lookup key for a {@link com.oregonscientific.meep.msm.Message} object received
	 * from server. Retrieve with {@link android.content.Intent#getParcelableExtra(String)}.
	 */
	public static final String EXTRA_MESSAGE = "message";
	
	public MessageManager(ServiceConnector connector) {
		super(connector);
	}
	
	@Override
	protected IMessageService getInterface() {
		return IMessageService.Stub.asInterface(getConnector().getBinder());
	}
	
	/**
	 * Returns whether or not the service is connected with the messaging server
	 * 
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected() {
		boolean result = false;
		try {
			if (getService() != null) {
				result = getService().isConnected();
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot determine whether the service is connected to messaging server because " + ex + " occurred");
		}
		return result;
	}
	
	public boolean isConnectedBlocking()
	{
		boolean result = false;
		if(isReady())
		{
			result = isConnected();
		}
		else
		{
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return getService().isConnected();
				}

			});
			addConnectedTasks(future);

			// Wait until the underlying service is connected then return the
			// result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot check server connection because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Sends a {@code message} to server
	 * 
	 * @param message the message to send to server
	 * @return true if the message was sent, false otherwise
	 */
	public void sendMessage(Message message) {
		if (!isReady()) {
			// Resend the message at a later time
			sendMessageDelayed(message);
		} else {
			try {
				getService().sendMessage(message);
			} catch (Exception ex) {
				Log.e(TAG, message + " was not sent because " + ex + " occurred");
			}
		}
	}
	
	/**
	 * Sends the {@code message} after connection with the underlying service is 
	 * established.
	 * 
	 * @param message The message to deliver
	 */
	private void sendMessageDelayed(final Message message) {
		if (isReady()) {
			sendMessage(message);
		} else if (message != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					sendMessage(message);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Registers the given component to be run when a message that matches {@code filter}
	 * is received.
	 * 
	 * @param componentName The concrete component name of the service to handle the message
	 * @param filter selects the message to receive
	 */
	public void registerCallback(ComponentName componentName, MessageFilter filter) {
		if (!isReady()) {
			registerCallbackDelayed(componentName, filter);
		} else {
			try {
				getService().registerCallback(componentName, filter);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot register " + componentName);
			}
		}
	}
	
	private void registerCallbackDelayed(final ComponentName componentName, final MessageFilter filter) {
		if (isReady()) {
			registerCallback(componentName, filter);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					registerCallback(componentName, filter);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Remove a previously registered component. All filters that have been registered
	 * for this component will be removed
	 */
	public void unregisterCallback(ComponentName componentName) {
		if (!isReady()) {
			unregisterCallbackDelayed(componentName);
		} else {
			try {
				getService().unregisterCallback(componentName);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot unregister " + componentName);
			}
		}
	}
	
	private void unregisterCallbackDelayed(final ComponentName componentName) {
		if (isReady()) {
			unregisterCallback(componentName);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					unregisterCallback(componentName);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Restart the message service with given environment
	 * 
	 * @param environmentInt 0 is for Production, 1 is for Sandbox
	 */
	public void reconnect(int environmentInt) {
		if (!isReady())
			reconnectDelayed(environmentInt);
		else {
			try {
				getService().reconnect(environmentInt);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot reconnect");
			}
		}
		
	}

	private void reconnectDelayed(final int environmentInt) {
		if (isReady()) {
			reconnect(environmentInt);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					reconnect(environmentInt);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
}
