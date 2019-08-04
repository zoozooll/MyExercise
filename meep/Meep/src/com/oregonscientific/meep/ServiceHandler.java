/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;

import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.msm.MessageReceiver;

/**
 * The base class for handling 
 */
public abstract class ServiceHandler {
	
	private final String TAG = getClass().getSimpleName();
	
	/** The execution service to handle messages received */
	private final ExecutorService mService = Executors.newCachedThreadPool();

	/** The handler's context */
	private Context mContext; 
	
	/**
	 * The receivers should be synchronized
	 */
	private final Object mLock = new Object();
	private final List<MessageReceiver> mReceivers = new ArrayList<MessageReceiver>();
	
	/**
	 * Register a {@link MessageReceiver} to run when a {@link Message} matching the
	 * given {@code filter} is received
	 * 
	 * @param receiver The {@link MessageReceiver} to run
	 */
	public void registerReceiver(MessageReceiver receiver) {
		synchronized (mLock) {
			if (receiver != null && !mReceivers.contains(receiver)) {
				mReceivers.add(receiver);
			}
		}
	}
	
	/**
	 * Unregister a previously registered {@link MessageReceiver}
	 * 
	 * @param receiver The {@link MessageReceiver} to unregister
	 */
	public void unregisterReceiver(MessageReceiver receiver) {
		synchronized (mLock) {
			if (receiver != null) {
				mReceivers.remove(receiver);
			}
		}
	}
	
	/**
	 * Handle server message for a particular service. Users should not call this method
	 * directly.
	 * 
	 * @param helper The database helper from service
	 * @param message Message object that sent from Message Service
	 */
	public void handleMessage(final Message message) {
		// Quick return if the message is null
		if (message == null) {
			return;
		}
		
		try {
			synchronized (mLock) {
				// Iterate through the list of registered receivers to find the one(s)
				// that are registered to handle the message. 
				Iterator<MessageReceiver> iterator = mReceivers.iterator();
				while (iterator.hasNext()) {
					final MessageReceiver receiver = iterator.next();
					
					if (receiver.shouldReceive(message)) {
						// Handles the {@code message} in a new thread to ensure that long-running
						// operations can complete running
						mService.execute(new Runnable() {

							@Override
							public void run() {
								receiver.onReceive(message);
								
								// Unregister the receiver if it is not persistent
								if (!receiver.isPersistent()) {
									unregisterReceiver(receiver);
								}
							}
							
						});
						
					}
				}
			}
		} catch (Exception ex) {
			// Ignore message
			Log.e(TAG, message + " cannot be processed because " + ex + " occurred");
		}	
	}

	/**
	 * Constructor for the Handler
	 * 
	 * @param context The context should be one of the service
	 */
	public ServiceHandler(Context context) {
		mContext = context;
	}
	
	/**
	 * Returns the context the handler is running in, through which it can 
	 * access the current theme, resources, etc.
	 * 
	 * @return the handler's context
	 */
	protected final Context getContext() {
		return mContext;
	}
	
	/**
	 * Send the server message through MessageService 
	 * 
	 * @param message The server message to be send
	 */
	protected void sendMessage(Message message) {
		// Send the message to server by calling MessageManager.sendMessage()
		if (getContext() != null) {
			MessageManager manager = (MessageManager) ServiceManager.getService(getContext(), ServiceManager.MESSAGE_SERVICE);
			manager.sendMessage(message);
		}
	}
	
	protected boolean isServerConnected()
	{
		MessageManager manager = (MessageManager) ServiceManager.getService(getContext(), ServiceManager.MESSAGE_SERVICE);
		return manager.isConnectedBlocking();
	}
}
