/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import java.util.List;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;

/**
 * This is the service that handles recommendations by third parties. You
 * should register with this service to receive recommendations pushed to the 
 * device from MEEP server. 
 * 
 * @see IRecommendationServiceCallback#onReceiveRecommendation(Recommendation)
 */
public class RecommendationService extends DatabaseService<RecommendationDatabaseHelper> {

	private final String TAG = "RecommendationService";
	
	/**
	 * For handling messages received
	 */
	private RecommendationHandler mHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mHandler = new RecommendationHandler(this, getHelper());
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
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
		
		// Unregister all callbacks.
		if (mHandler != null) {
			mHandler.unregisterAllCallbacks();
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
		
		// Only handles messages received from MessageService
		if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
			
			if (mHandler != null) {
				mHandler.handleMessage(message);
			}
		}
	}
	
	private IRecommendationService.Stub mBinder = new IRecommendationService.Stub() {
		
		/**
		 * Determines whether or not the given {@code url} is one of the recommended item for the {@code user} 
		 * 
		 * @param user the user to determine whether the item is recommended to
		 * @param url the URL of the item
		 * @return {@code true} if the {@code url} is recommended to {@code user}, {@code false} otherwise
		 */
		@Override
		public boolean isUrlRecommended(String user, String url) throws RemoteException {
			boolean result = false;
			if (mHandler != null) {
				result = mHandler.isUrlRecommended(user, url);
			}
			return result;
		}
		
		/**
		 * Adds an entry to the recommendation list in local store. Note that the 
		 * recommendation is only added in the local store. Account must call 
		 * {@link #updateRecommendations()} to update the list of recommendations with
		 * 
		 * @param recommendation The recommendation to add to the local store
		 */
		@Override
		public boolean addRecommendation(String user, Recommendation recommendation) throws RemoteException {
			boolean result = false;
			if (mHandler != null) {
				result = mHandler.addRecommendation(user, recommendation);
			} else {
				Log.e(TAG, "Cannot add " + recommendation + " because the service is not initialized");
			}
			return result;
		}

		/**
		 * Removes the {@code recommendation} from the recommendation list. Note that
		 * the recommendation is only removed in the local store. Account must call 
		 * {@link #updateRecommendations()} to update the list of recommendations with
		 * server 
		 * 
		 * @param recommendation The recommendation to remove from the local store
		 */
		@Override
		public boolean removeRecommendation(String user, Recommendation recommendation) throws RemoteException {
			boolean result = false;
			if (mHandler != null) {
				result = mHandler.removeRecommendation(user, recommendation);
			} else {
				Log.e(TAG, "Cannot remove " + recommendation + " because the service is not initialized");
			}
			return result;
		}
		
		/**
		 * Synchronize recommendation list in database with server's recommendation
		 * list
		 */
		@Override
		public void syncRecommendations (String user, String type) throws RemoteException {
			if (mHandler != null) {
				mHandler.syncRecommendations(user, type);
			} else {
				Log.e(TAG, "Cannot synchronize recommendations for " + user + " because the service is not initialized");
			}
		}
		 
		/**
		 * Retrieve recommendation list from database
		 * 
		 * @return A list of Recommendation objects, null if the recommendation list is empty or encountered error
		 */
		@Override
		public List<Recommendation> getAllRecommendations(String user) throws RemoteException {
			List<Recommendation> result = null;
			if (mHandler != null) {
				result = mHandler.getAllRecommendations(user);
			} else {
				Log.e(TAG, "Cannot retrieve recommendations for " + user + " because the service is not initialized");
			}
			return result;
		}
		
		/**
		 * Retrieve recommendation list from database
		 * 
		 * @param user user the use whose {@link Recommendation} to retrieve. This should be the meep tag that uniquely identifies the user
		 * @param type the type of recommendation to retrieve
		 * @return A list of Recommendation objects
		 */
		@Override
		public List<Recommendation> getRecommendations(String user, String type) throws RemoteException {
			List<Recommendation> result = null;
			if (mHandler != null) {
				result = mHandler.getLocalRecommendations(user, type);
			} else {
				Log.e(TAG, "Cannot retrieve " + type + " recommendations for " + user + " because the service is not initialized");
			}
			return result;
		}

		/**
		 * Register the {@code callback} to be run when the given {@code type}
		 * of recommendation is received
		 * 
		 * @param type
		 *            The type of recommendation to register for. Usually one of
		 *            {@link #TYPE_WEB_FROM_ADMIN}
		 *            {@link #TYPE_YOUTUBE_FROM_ADMIN},
		 *            {@link #TYPE_WEB_FROM_PARENT}, or
		 *            {@link #TYPE_YOUTUBE_FROM_PARENT}
		 * @param callback
		 *            The callback to invoke when the given {@code type} of
		 *            recommendation is received
		 */
		@Override
		public void registerCallback(String type, IRecommendationServiceCallback callback) throws RemoteException {
			if (mHandler != null) {
				mHandler.registerCallback(type, callback);
			}
		}

		/**
		 * Removes a previously registered {@code callback} from receiving the
		 * given {@code type} of recommendation
		 * 
		 * @param type
		 *            The type of recommendation to register for. Usually one of
		 *            {@link #TYPE_WEB_FROM_ADMIN}
		 *            {@link #TYPE_YOUTUBE_FROM_ADMIN},
		 *            {@link #TYPE_WEB_FROM_PARENT}, or
		 *            {@link #TYPE_YOUTUBE_FROM_PARENT}
		 * @param callback
		 *            The callback to unregister for
		 */
		@Override
		public void unregisterCallback(String type, IRecommendationServiceCallback callback) throws RemoteException {
			if (mHandler != null) {
				mHandler.unregisterCallback(type, callback);
			}
		}
		
	};
	
}
