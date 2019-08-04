/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.oregonscientific.meep.ServiceHandler;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.msm.MessageReceiver;
import com.oregonscientific.meep.util.Iterables;

public class RecommendationHandler extends ServiceHandler {

	private static final String TAG = RecommendationHandler.class.getSimpleName();
	
	private final OrmLiteSqliteOpenHelper mHelper;
	private final Handler mCallbackHandler;
	
	/** The extra used to retrieve recommendations from remote server */
	private static final String KEY_TYPE = "type";
	private static final String KEY_THUMBNAIL = "thumbnail";
	private static final String KEY_LIST = "list";
	
	/**
	 * An enumeraton for different system command
	 */
	private enum Type {
		
		WEB_FROM_ADMIN(RecommendationManager.TYPE_WEB_FROM_ADMIN),
		WEB_FROM_PARENT(RecommendationManager.TYPE_WEB_FROM_PARENT),
		YOUTUBE_FROM_ADMIN(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN),
		YOUTUBE_FROM_PARENT(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
		
		private String name;

		Type(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
		
		public static Type fromString(String text) {
			if (text != null) {
				for (Type b : Type.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	
	/**
	 * A list of types that can neither be added or removed on device
	 */
	private final Type[] restrictedTypes = { Type.WEB_FROM_ADMIN, Type.YOUTUBE_FROM_ADMIN };
	
	/**
	 * This is a list of callbacks that have been registered with the service.
	 * Note that this is package scoped (instead of private) so that it can be
	 * accessed more efficiently from inner classes.
	 */
	final Map<Type, RemoteCallbackList<IRecommendationServiceCallback>> mCallbacks = 
		new ConcurrentHashMap<Type, RemoteCallbackList<IRecommendationServiceCallback>>();
	
	/**
	 * The internally used message codes
	 */
	private static final int RECOMMENDATION_WEB_FROM_ADMIN = 1;
	private static final int RECOMMENDATION_WEB_FROM_PARENT = 2;
	private static final int RECOMMENDATION_YOUTUBE_FROM_ADMIN = 3;
	private static final int RECOMMENDATION_YOUTUBE_FROM_PARENT = 4;
	
	/**
	 * The Handler used to execute operations on the main thread. This is used to
	 * invoke methods on the registered callbacks
	 */
	private static final class CallbackHandler extends Handler {
		
		final WeakReference<RecommendationHandler> ref;
		
		CallbackHandler(RecommendationHandler handler) {
			ref = new WeakReference<RecommendationHandler>(handler);
		}
		
		@Override 
		public void handleMessage(android.os.Message msg) {
			RecommendationHandler handler = ref.get();
			// Quick return if the request cannot be processed
			if (handler == null || msg == null) {
				return;
			}
			
			RemoteCallbackList<IRecommendationServiceCallback> callbacks = null;
			switch(msg.what) {
			case RECOMMENDATION_WEB_FROM_ADMIN:
				callbacks = handler.mCallbacks.get(Type.WEB_FROM_ADMIN);
				break;
			case RECOMMENDATION_WEB_FROM_PARENT:
				callbacks = handler.mCallbacks.get(Type.WEB_FROM_PARENT);
				break;
			case RECOMMENDATION_YOUTUBE_FROM_ADMIN:
				callbacks = handler.mCallbacks.get(Type.YOUTUBE_FROM_ADMIN);
				break;
			case RECOMMENDATION_YOUTUBE_FROM_PARENT:
				callbacks = handler.mCallbacks.get(Type.YOUTUBE_FROM_PARENT);
				break;
			}
			
			// We cannot proceed without a callback list
			if (callbacks == null || msg.obj == null || !(msg.obj instanceof List)) {
				return;
			}
			
			@SuppressWarnings("unchecked")
			List<Recommendation> recommendations = (List<Recommendation>) msg.obj;
			int i = callbacks.beginBroadcast();
			while (i > 0) {
				i--;
				try {
					callbacks.getBroadcastItem(i).onReceiveRecommendation(recommendations);
				} catch (Exception ex) {
					// Error occurred while invoking callback
					Log.e(TAG, ex + " occurred while invoking callback");
				}
			}
			callbacks.finishBroadcast();
		}
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param context The context that this handler runs in
	 * @param helper The SQLiteOpenHelper that this handler uses to connect to the
	 * underlying data store
	 */
	public RecommendationHandler(Context context, OrmLiteSqliteOpenHelper helper) {
		super(context);
		
		mHelper = helper;
		mCallbackHandler = new CallbackHandler(this);
	}
	
	/**
	 * Determines whether or not the given {@link Recommendation} is of types that are restricted
	 * 
	 * @param recommendation the {@link Recommendation} to verify
	 * @return {@code true} if the {@code recommendation} is one of the restricted types, {@code false} otherwise
	 */
	private boolean isRestrictedType(Recommendation recommendation) {
		boolean result = false;
		Type recommendationType = Type.fromString(recommendation.getType());
		
		if (recommendationType != null) {
			List<Type> restricted = Arrays.asList(restrictedTypes);
			result = restricted.contains(recommendationType);
		}
		
		return result;
	}
	
	/**
	 * Adds an entry to the recommendation list in local store. Note that the 
	 * recommendation is only added in the local store. Account must call 
	 * {@link #syncRecommendations()} to update the list of recommendations with
	 * 
	 * @param userId The user whose recommendation to add to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param recommendation The recommendation to add to the local store
	 */
	public boolean addRecommendation(String userId, Recommendation recommendation) {
		// Quick return if execute cannot proceed
		if (mHelper == null || recommendation == null || userId == null) {
			return false;
		}
		
		// Cannot add a {@link Recommendation} from administrator
		if (isRestrictedType(recommendation)) {
			return false;
		}
		
		// Cannot proceed if the user is not found. 
		$User user = getUser(userId);
		if (user == null) {
			return false;
		}
		
		boolean result = false;
		if (!user.hasRecommendation(recommendation)) {
			// Creates the recommendation for the given {@code username}
			recommendation.setId(0);
			recommendation.setUser(user);
			
			try {
				Dao<Recommendation, Long> dao = mHelper.getDao(Recommendation.class);
				int rows = dao.create(recommendation);
				result = rows == 1;
			} catch (SQLException ex) {
				Log.e(TAG, "Cannot create " + recommendation + " for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Removes the {@code recommendation} from the recommendation list. Note that
	 * the recommendation is only removed in the local store. Account must call 
	 * {@link #syncRecommendations()} to update the list of recommendations with
	 * server 
	 * 
	 * @param userId The user whose recommendation to add to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param recommendation The recommendation to remove from the local store
	 */
	public boolean removeRecommendation(String userId, Recommendation recommendation) {
		// Quick return if execute cannot proceed
		if (mHelper == null || recommendation == null || userId == null) {
			return false;
		}
		
		// Cannot add a {@link Recommendation} from administrator
		if (isRestrictedType(recommendation)) {
			return false;
		}
		
		// Cannot proceed if there is user is not found. 
		$User user = getUser(userId);
		if (user == null) {
			return false;
		}
		
		return user.removeRecommendation(recommendation);
	}

	/**
	 * Synchronize recommendation list in database with server's recommendation
	 * list for the given {@code username}
	 * 
	 * @param userId the name of the user whose recommendation list is to be synchronized.
	 * This should be the identifier that uniquely identifies the user.
	 * @param type the type of recommendation to synchronize with server
	 */
	public void syncRecommendations(final String userId, final String type) {
		// Creates the message to send to remote server
		Message message = new Message(
				Message.PROCESS_PARENTAL, 
				Message.OPERATION_CODE_GET_RECOMMENDATIONS);
		
		if (type != null) {
			message.addProperty(KEY_TYPE, type);
		}
		
		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}
				
				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage() == null ? "" : message.getMessage());
				}
				
				// Cannot proceed if there is user is not found. If the user is not
				// found, see if the given user is the current logged in user. We cannot
				// proceed if the specified user is neither located in the recommendation
				// database nor the current logged in user
				$User user = getUser(userId);
				if (user == null) {
					user = getLoggedInUser();
					String identifier = user == null ? null : user.getIdentifier();
					if (identifier == null || !identifier.equals(userId)) {
						return;
					}
				}
				
				if (user != null) {
					// Replaces the local blacklist with the one retrieved from server
					updateLocalRecommendations(user, type, message);
					// Notify registered callbacks
					invokeCallback(type, user.getRecommendations(type));
				}
			}
			
		};
		
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
		// Retrieve recommendations from server 
		sendMessage(message);
	}
	
	/**
	 * Retrieve recommendation list from the local database. Warning: If the given user has a lot of recommendations,
	 * this will load all of them into memory. 
	 * 
	 * @param userId the name of the user whose recommendation list is to be returned
	 * @return A list of Recommendation objects, {@code null} if the recommendation list is empty or encountered error
	 */
	public List<Recommendation> getAllRecommendations(String userId) {
		List<Recommendation> result = new ArrayList<Recommendation>();
		for (Type type : Type.values()) {
			List<Recommendation> recommendations = getLocalRecommendations(userId, type.toString());
			if (recommendations != null && recommendations.size() > 0)
				result.addAll(recommendations);
		}
		return result;
	} 
	
	/**
	 * Retreive recommendations of the given {@code type} for the {@code user} 
	 * 
	 * @param userId the name of the user whose recommendations are to be returned
	 * @param type the type of recommendation to return
	 * @return a list of recommendations or null if the user is not found
	 */
	public List<Recommendation> getLocalRecommendations(String userId, String type) {
		List<Recommendation> result = null;
		
		// Cannot proceed if there is user is not found.
		$User user = getUser(userId);
		if (user == null) {
			return result;
		}
		
		return user.getRecommendations(type);
	}
	
	/**
	 * Determines whether or not the given {@code url} is recommended to {@code userId}
	 * 
	 * @param userId the unique identifier of the user.
	 * @param url the URL of the item to determine whether or not it is recommended
	 * @return {@code true} if the URL is recommended, {@code false} otherwise
	 */
	public boolean isUrlRecommended(String userId, String url) {
		boolean result = false;
		try {
			Uri uri = Uri.parse(url);
			result = isUrlRecommended(userId, uri);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot determine if " + url + " is recommended because " + ex);
		}
		return result;
	}
	
	private boolean isUrlRecommended(String userId, Uri url) {
		$User user = getUser(userId);
		// Quick return if user is not found
		if (user == null || url == null) {
			return false;
		}
		
		boolean result = false;
		try {
			ModelAttributes attrs = Schema.getAttributes(Recommendation.class);
			Dao<Recommendation, Long> dao = mHelper.getDao(Recommendation.class);
			QueryBuilder<Recommendation, Long> qb = dao.queryBuilder();
			
			qb.where().eq(attrs.getColumnName(Recommendation.USER_FIELD_NAME), user.getId());
			CloseableIterator<Recommendation> iterator = dao.iterator(qb.prepare());
			
			boolean ignoreCaseForPathComparison = url.isAbsolute();
			while (iterator.hasNext()) {
				Recommendation recommendation = iterator.next();
				Uri recommendedUri = Uri.parse(recommendation.getUrl());
				
				// Each part of the passing in URL must match the recommended URL
				result = stringEqual(url.getHost(), recommendedUri.getHost(), true)
						&& (url.getPort() == recommendedUri.getPort())
						&& stringEqual(url.getPath(), recommendedUri.getPath(), ignoreCaseForPathComparison)
						&& stringEqual(url.getFragment(), recommendedUri.getFragment(), true);
				
				if (result) {
					break;
				}
			}
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot determine whether " + url + " is recommended to " + userId + " because " + ex);
		}
		return result;
	}
	
	/**
	 * Compares 2 strings ignoring the case of the characters and returns true if they are equal. This method will
	 * return {@code true} if both strings are {@code null}
	 * 
	 * @param str1 the string to compare
	 * @param str2 the string to compare
	 * @param ignoreCase {@code true} to ignore case, {@code false} otherwise
	 */
	private boolean stringEqual(String str1, String str2, boolean ignoreCase) {
		if (str1 == null) {
			return str2 == null;
		}
		
		return ignoreCase ? str1.equalsIgnoreCase(str2) : str1.equals(str2);
	}
	
	/**
	 * Invoke registered callback to notify them the given {@code recommendations} are
	 * received
	 * 
	 * @param type the type of recommendation
	 * @param recommendations the list of recommendations received
	 */
	private void invokeCallback(String type, List<Recommendation> recommendations) {
		try {
			int what = -1;
			switch (Type.fromString(type)) {
			case WEB_FROM_ADMIN:
				what = RECOMMENDATION_WEB_FROM_ADMIN;
				break;
			case WEB_FROM_PARENT:
				what = RECOMMENDATION_WEB_FROM_PARENT;
				break;
			case YOUTUBE_FROM_ADMIN:
				what = RECOMMENDATION_YOUTUBE_FROM_ADMIN;
				break;
			case YOUTUBE_FROM_PARENT:
				what = RECOMMENDATION_YOUTUBE_FROM_PARENT;
				break;
			}
			// Notify registered callbacks that we received recommendations
			android.os.Message msg = mCallbackHandler.obtainMessage(what, recommendations);
			mCallbackHandler.sendMessage(msg);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot invoke callback because " + ex);
		}
	}

	/**
	 * Register the {@code callback} to be run when the given {@code type} of recommendation
	 * is received
	 * 
	 * @param type The type of recommendation to register for. Usually one of {@link #TYPE_WEB_FROM_ADMIN} or
	 * {@link #TYPE_YOUTUBE_FROM_ADMIN}
	 * @param callback The callback to invoke when the given {@code type} of recommendation is
	 * received
	 */
	public synchronized void registerCallback(String type, IRecommendationServiceCallback callback) {
		// Quick return if there is nothing to register for
		if (callback == null || type == null) {
			return;
		}
		
		if (callback != null) {
			try {
				Type recommendationType = Type.fromString(type);
				RemoteCallbackList<IRecommendationServiceCallback> callbackList = mCallbacks.get(recommendationType);
				
				if (callbackList == null) {
					callbackList = new RemoteCallbackList<IRecommendationServiceCallback>();
				}
				
				callbackList.register(callback);
				mCallbacks.put(recommendationType, callbackList);
			} catch (Exception ex) {
				// Cannot recognize the given type.
				Log.e(TAG, "Cannot register callback because " + type + " cannot be recognized");
			}
		}
	}

	/**
	 * Removes a previously registered {@code callback} from receiving the given {@code type} 
	 * of recommendation
	 * 
	 * @param type The type of recommendation to register for. Usually one of {@link #TYPE_WEB_FROM_ADMIN} or
	 * {@link #TYPE_YOUTUBE_FROM_ADMIN}
	 * @param callback The callback to unregister for
	 */
	public synchronized void unregisterCallback(String type, IRecommendationServiceCallback callback) {
		// Quick return if there is nothing to register for
		if (callback == null || type == null) {
			return;
		}

		if (callback != null) {
			try {
				Type recommendationType = Type.fromString(type);
				RemoteCallbackList<IRecommendationServiceCallback> callbackList = mCallbacks.get(recommendationType);
				
				if (callbackList != null) {
					callbackList.unregister(callback);
					mCallbacks.put(recommendationType, callbackList);
				}
			} catch (Exception ex) {
				// Cannot recognize the given type.
				Log.e(TAG, "Cannot unregister callback because " + type + " cannot be recognized");
			}
		}
	}
	
	/**
	 * Unregister all the callbacks including browser and youtube
	 */
	public void unregisterAllCallbacks() {
		// Disable all callback list
		Collection<RemoteCallbackList<IRecommendationServiceCallback>> callbacks = mCallbacks.values();
		for (RemoteCallbackList<?> callback : callbacks) {
			callback.kill();
		}
		
		// Remove all callback list from the hash map
		mCallbacks.clear();
	}
	
	/**
	 * Replace the recommendations in local data store with {@code recommendations}. All existing
	 * entries in the local data store will be removed and replaced with {@code recommendations}
	 * 
	 * <h2>Note</h2> The operation can lock access to the underlying data store
	 * for a long period of time if the list is large. Use this function is caution
	 * 
	 * @param user the user whose recommendations are to be replaced
	 * @param type the type of recommendations to be replaced
	 * @param message The {@link Message} containing information the new recommendations
	 */
	private void updateLocalRecommendations($User user, String type, Message message) {
		// Quick return if one of the pass in parameter is null
		if (user == null || type == null || message == null) {
			return;
		}
		
		Map<String, ?> properties = message.getProperties();
		Object listType = (String) properties.get(KEY_TYPE);
		
		// Do not continue if type of the recommendations returned does not match the type
		// of recommendations requested
		if (listType == null || !type.equals(listType)) {
			return;
		}
		
		// Updates recommendations
		Object thumbnail = properties.get(KEY_THUMBNAIL);
		Object object = properties.get(KEY_LIST);
		
		// Ensures that the list is an array of string
		if (Iterables.isIterable(object)) {
			setRecommendations(user, type, thumbnail == null ? null : thumbnail.toString(), Iterables.asArray(object));
		}
	}
	
	private void setRecommendations(final $User user, final String type, final String thumbnail, final Object[] items) {
		// Quick return if one of the pass in parameter is null
		if (mHelper == null || user == null || type == null) {
			return;
		}
		
		try {
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					ModelAttributes attrs = Schema.getAttributes(Recommendation.class);
					Dao<Recommendation, Long> dao = mHelper.getDao(Recommendation.class);
					
					// Delete items in table "recommendations" that do not present in the
					// {@code items} String array
					DeleteBuilder<Recommendation, Long> db = dao.deleteBuilder();
					Where<Recommendation, Long> where = db
							.where()
							.eq(attrs.getColumnName(Recommendation.TYPE_FIELD_NAME), type)
							.and()
							.eq(attrs.getColumnName(Recommendation.USER_FIELD_NAME), Long.valueOf(user.getId()));
					if (items != null) {
						where.and().notIn(attrs.getColumnName(Recommendation.URL_FIELD_NAME), items);
					}
					db.delete();
					
					// Refreshes the {@link $User} object
					user.refresh();
					
					// Creates items that do not already exist
					if (items != null) {
						Recommendation recommendation = new Recommendation(user, type);
						for (Object item : items) {
							recommendation.setId(0);
							recommendation.setThumbnail(thumbnail);
							recommendation.setUrl(item.toString());
							
							// Creates the recommendation if it does not already exist
							if (!user.hasRecommendation(recommendation)) {
								dao.create(recommendation);
							}
						}
					}
					
					return null;
				}
				
			});
		} catch (Exception ex) {
			Log.e(TAG, "Cannot update " + type + " type of recommendations because " + ex);
		}
	}
	
	/**
	 * Retrieves the User object that is currently logged in. This method calls the blocking
	 * method in {@link AccountManager} to retrieve the current logged in user. User must not
	 * call this method in the main thread.
	 * 
	 * <p>This method will create the User object if the logged in user is not found in the
	 * permission database</p>
	 * 
	 * @return The User object or <code>null</code> if no user is currently logged in
	 */
	private $User getLoggedInUser() {
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		Account account = am.getLoggedInAccountBlocking();
		return account == null ? null : getUser(account, true);
	}
	
	/**
	 * Retrieve the User object with the given {@code username}
	 * 
	 * @param userId the unique name identifying the user
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private $User getUser(String userId) {
		return getUser(userId, false);
	}
	
	/**
	 * Retrieves the User object identified by the {@code account}
	 *  
	 * @param account The Account to retrieve
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return The User object or <code>null</code> if the user was not found
	 */
	private $User getUser(Account account, boolean createIfNotExists) {
		String userId = account == null ? "" : account.getId();
		return getUser(userId, createIfNotExists);
	}
	
	/**
	 * Retrieve the User object with the given {@code userId}. If the user cannot be
	 * found, creates the User object if {@code createIfNotExist} is set to true
	 * 
	 * @param userId the unique name identifying the user
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private synchronized $User getUser(String userId, boolean createIfNotExists) {
		if (mHelper == null) {
			return null;
		}
		
		$User result = null;
		try {
			Dao<$User, Long> dao = mHelper.getDao($User.class);
			QueryBuilder<$User, Long> qb = dao.queryBuilder();
			qb.where().eq($User.IDENTIFIER_FIELD_NAME, userId);
			result = dao.queryForFirst(qb.prepare());
			
			if (result == null && createIfNotExists) {
				result = new $User(userId);
				dao.createIfNotExists(result);
			}
		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve " + userId + " because " + ex);
			result = null;
		}
		return result;
	}
	
}
