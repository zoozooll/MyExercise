/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceAgent;
import com.oregonscientific.meep.ServiceConnector;

/**
 * This class provides API access to the MEEP recommendation engine.
 * Applications can register with the service to receive push notifications when
 * new recommendation is received. Obtain an instance of this class by calling
 * {@link com.oregonscientific.meep.ServiceManager#getService(android.content.Context, String)}
 * 
 * <p>
 * After a user is successfully logged-in with server, the system will send a
 * request to server to retrieve YouTube and web recommendations. If there are
 * registered callbacks for a given type of recommendation, then the callbacks
 * will be invoked when that type of recommendation is returned from server.
 * </p>
 * 
 * <p>
 * Typically, an app such as Meep Browser, can call the method
 * {@link #getRecommendations(String, String)} in {@link RecommendationManager}
 * to retrieve the list of recommendations of a given type of recommendation
 * from the on device database. This is a synchronous operation and
 * recommendations returned may not necessary be the latest.
 * </p>
 * 
 * <p>
 * Alternatively, the app can also register as a callback with
 * {@link RecommendationManager} by calling
 * {@link #registerCallback(String, IRecommendationServiceCallback)} and then
 * invoke the method {@link #syncRecommendations(String, String)} to synchronize
 * recommendations in the on device database with server. This is an
 * asynchronous call and recommendations retrieved from server should be the
 * latest.
 * </p>
 * 
 * <p>
 * Registering as callback with RecommendationManager also allows
 * recommendations be pushed from server to device. The use case is as follow:
 * <ol>
 * <li>Parent entered a new recommendation on portal</li>
 * <li>Server send the newly entered recommendation to device</li>
 * <li>MessageService received the message from server</li>
 * <li>MessageService relay the message to RecommendationService since it is the
 * registered handler</li>
 * <li>RecommendationService updates the on device database and invoke any
 * registered callback that a recommendation is received</li>
 * </ol>
 * </p>
 * 
 * <p>
 * In fact, the expected implementation of an app that wish to incorporate
 * functions provided by {@link RecommendationManager} is as follow:
 * <ol>
 * <li>Upon launching, the app retrieves a list of recommendations from the
 * on-device database</li>
 * <li>Update UI accordingly upon returning from the call in the previous step</li>
 * <li>Register as callback to {@link RecommendationManager}</li>
 * <li>Issue a call to synchronize recommendations</li>
 * </ol>
 * </p>
 * 
 * <p>
 * The above implementation will allow an app to display locally cached
 * recommendations quickly. Then, it can optionally synchronize cached
 * recommendations with server behind the scene. Any new updates can be
 * reflected in the UI by implementing the callback function.
 * </p>
 * 
 * <p>
 * {@link RecommendationManager} is designed such that an app is not required to
 * implement the callback. An app would only implement the callback if it wishes
 * to reflect recommendations received from server in a timely manner.
 * </p>
 * 
 * <p>
 * One of the following 2 actions will be taken when
 * {@link RecommendationManager} received recommendations from server:
 * <ul>
 * <li>If there is no registered callback, upon receiving recommendations from
 * server, {@link RecommendationManager} will only update entries in the
 * on-device database. No additional action will be taken</li>
 * <li>If there are at registered callbacks, upon receiving a recommendation
 * from server, {@link RecommendationManager} will update the on-device
 * database. After updating the on-device database, it will then notify callbacks of
 * the recommendations received</li>
 * </ul>
 * </p>
 */
public class RecommendationManager extends ServiceAgent<IRecommendationService> {
	
	private static final String TAG = "RecommendationManager";

	/**
	 * The type of recommendation, as recommended by Oregon-Scientific, that is best viewed with a web browser.
	 */
	public static final String TYPE_WEB_FROM_ADMIN = "os-browser";
	
	/**
	 * An Oregon-Scientific recommended YouTube video
	 */
	public static final String TYPE_YOUTUBE_FROM_ADMIN = "os-youtube";
	
	/**
	 * The type of recommendation that is best view with a web browser
	 */
	public static final String TYPE_WEB_FROM_PARENT = "browser";
	
	/**
	 * A recommended YouTube video from parent
	 */
	public static final String TYPE_YOUTUBE_FROM_PARENT = "youtube";
	
	public RecommendationManager(ServiceConnector connector) {
		super(connector);
	}

	@Override
	protected IRecommendationService getInterface() {
		return IRecommendationService.Stub.asInterface(getConnector().getBinder());
	}
	
	/**
	 * Determines whether or not the given {@code url} is recommended to {@code user}
	 * 
	 * @param user the unique identifier of the user.
	 * @param url the URL of the item to determine whether or not it is recommended
	 * @return {@code true} if the URL is recommended, {@code false} otherwise
	 */
	public boolean isUrlRecommended(String user, String url) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().isUrlRecommended(user, url);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot determine whether " + url + " is recommended to " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given {@code url} is recommended to {@code user}. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the unique identifier of the user.
	 * @param url the URL of the item to determine whether or not it is recommended
	 * @return {@code true} if the URL is recommended, {@code false} otherwise
	 */
	public boolean isUrlRecommendedBlocking(final String user, final String url) {
		boolean result = false;
		if (isReady()) {
			result = isUrlRecommended(user, url);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return isUrlRecommended(user, url);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine whether " + url + " is recommended to " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Adds an entry to the recommendation list in local store. Note that the 
	 * recommendation is only added in the local store. Account must call 
	 * {@link #syncRecommendations(String, String)()} to update the list of recommendations with. 
	 * 
	 * @param user  The user whose recommendation to add to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param recommendation The recommendation to add to the local store
	 */
	public boolean addRecommendation(String user, Recommendation recommendation) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().addRecommendation(user, recommendation);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot add " + recommendation + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Adds an entry to the recommendation list in local store. Note that the 
	 * recommendation is only added in the local store. Account must call 
	 * {@link #syncRecommendations(String, String)} to update the list of recommendations. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user  The user whose recommendation to add to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param recommendation The recommendation to add to the local store
	 */
	public boolean addRecommendationBlocking(final String user, final Recommendation recommendation) {
		boolean result = false;
		if (isReady()) {
			result = addRecommendation(user, recommendation);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return addRecommendation(user, recommendation);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot add " + recommendation + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Removes the {@code recommendation} from the recommendation list. Note that
	 * the recommendation is only removed in the local store. Account must call 
	 * {@link #syncRecommendations(String, String)} to update the list of recommendations with
	 * server 
	 * 
	 * @param user  The user whose recommendation to add to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param recommendation The recommendation to remove from the local store
	 */
	public boolean removeRecommendation(String user, Recommendation recommendation) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().removeRecommendation(user, recommendation);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot remove " + recommendation + " for " + user + " because " + ex);
			}
		}
		return result;
	}

	/**
	 * Removes the {@code recommendation} from the recommendation list. Note that
	 * the recommendation is only removed in the local store. Account must call
	 * {@link #syncRecommendations(String, String)} to update the list of recommendations with
	 * server. This method will block until the underlying service becomes ready.
	 * Calling this method in main thread may cause dead lock. User should not
	 * call this method in the main thread
	 * 
	 * @param user
	 *          The user whose recommendation to add to. This should be the identifier
	 *          that uniquely identifies the user
	 * @param recommendation
	 *          The recommendation to remove from the local store
	 */
	public boolean removeRecommendationBlocking(final String user, final Recommendation recommendation) {
		boolean result = false;
		if (isReady()) {
			result = removeRecommendation(user, recommendation);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return removeRecommendation(user, recommendation);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot remove " + recommendation + " for " + user + " because " + ex);
			}
		}
		return result;
	}

	/**
	 * Synchronize recommendation list in database with server's recommendation
	 * list
	 * 
	 * @param account
	 *          The account whose recommendation to apply to
	 * @param type
	 *          the type of recommendation to synchronize with server
	 */
	public void syncRecommendations(String user, String type) {
		if (isReady()) {
			try {
				getService().syncRecommendations(user, type);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to synchronize local recommendations with remote server because " + e);
			}
		} else {
			// Service not yet binded, retrying after some delay ...
			syncRecommendationsDelayed(user, type);
		}
	}
	
	/**
	 * Schedule to synchronize local recommendations with remote server after the underlying
	 * service is connected
	 * 
	 * @param account The account whose recommendations are to be synchronized with remote server.
	 * @param type the type of recommendation to synchronize with server
	 */
	private void syncRecommendationsDelayed(final String user, final String type) {
		if (isReady()) {
			syncRecommendations(user, type);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					syncRecommendations(user, type);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Retreive recommendations of the given {@code type} for the {@code user} 
	 * 
	 * @param username the name of the user whose recommendations are to be returned
	 * @param type the type of recommendation to return
	 * @return a list of recommendations or null if the user is not found
	 */
	public List<Recommendation> getRecommendations(String user, String type) {
		List<Recommendation> result = null;
		if (isReady()) {
			try {
				result = getService().getRecommendations(user, type);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to retrieve " + type + " recommendations for " + user + " because " + e);
			}
		}
		return result;
	}

	/**
	 * Retreive recommendations of the given {@code type} for the {@code user}
	 * This method will block until the underlying service becomes ready.
	 * Calling this method in main thread may cause dead lock. User should not
	 * call this method in the main thread
	 * 
	 * @param username
	 *            the name of the user whose recommendations are to be returned
	 * @param type
	 *            the type of recommendation to return
	 * @return a list of recommendations or null if the user is not found
	 */
	public List<Recommendation> getRecommendationsBlocking(final String user, final String type) {
		List<Recommendation> result = null;
		if (isReady()) {
			result = getRecommendations(user, type);
		} else {
		// Adds the FutureTask to be executed when the service is connected
			FutureTask<List<Recommendation>> future = new FutureTask<List<Recommendation>>(new Callable<List<Recommendation>>() {

				@Override
				public List<Recommendation> call() throws Exception {
					return getRecommendations(user, type);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot get " + type + " recommendations for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Retrieve recommendation list from database. 
	 * 
	 * @param user The user whose recommendation to retrieve. This 
	 * should be the identifier that uniquely identifies the user
	 * @return A list of Recommendation objects or null if there was an error or the
	 * {@code user} is not found
	 */
	public List<Recommendation> getAllRecommendations(String user) {
		List<Recommendation> result = null;
		if (isReady()) {
			try {
				result = getService().getAllRecommendations(user);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to retrieve recommendations for " + user + " because " + e);
			}
		}
		return result;
	}
	
	/**
	 * Retrieve recommendation list from database. This method will block until
	 * the underlying service becomes ready. Calling this method in main thread
	 * may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user
	 *          The user whose recommendation to retrieve. This should be the identifier
	 *          that uniquely identifies the user
	 * @return A list of Recommendation objects or null if there was an error or
	 *         the {@code user} is not found
	 */
	public List<Recommendation> getAllRecommendationsBlocking(final String user) {
		List<Recommendation> result = null;
		if (isReady()) {
			result = getAllRecommendations(user);
		} else {
		// Adds the FutureTask to be executed when the service is connected
			FutureTask<List<Recommendation>> future = new FutureTask<List<Recommendation>>(new Callable<List<Recommendation>>() {

				@Override
				public List<Recommendation> call() throws Exception {
					return getAllRecommendations(user);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot get recommendations for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Register the {@code callback} to be run when the given {@code type} of recommendation
	 * is received
	 * 
	 * @param type The type of recommendation to register for. Usually one of {@link #TYPE_WEB_FROM_ADMIN} or
	 * {@link #TYPE_YOUTUBE_FROM_PARENT}
	 * @param callback The callback to invoke when the given {@code type} of recommendation is
	 * received
	 */
	public void registerCallback(String type, IRecommendationServiceCallback callback) {
		// Quick return if there is nothing to process
		if (callback == null) {
			return;
		}
		
		if (isReady()) {
			try {
				getService().registerCallback(type, callback);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot register callback because of " + ex);
			}
		} else {
			registerCallbackDelayed(type, callback);
		}
	}
	
	private void registerCallbackDelayed(final String type, final IRecommendationServiceCallback callback) {
		if (isReady()) {
			registerCallback(type, callback);
		} else if (callback != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					registerCallback(type, callback);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	  * Removes a previously registered {@code callback} from receiving the given {@code type} 
	  * of recommendation
	  * 
	  * @param type The type of recommendation to register for. Usually one of {@link #TYPE_WEB_FROM_ADMIN} or
	 * {@link #TYPE_YOUTUBE_FROM_PARENT}
	 * @param callback The callback to unregister for
	  */
	public void unregisterCallback(String type, IRecommendationServiceCallback callback) {
	// Quick return if there is nothing to process
		if (callback == null) {
			return;
		}
		
		if (isReady()) {
			try {
				getService().unregisterCallback(type, callback);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot register callback because of " + ex);
			}
		} else {
			unregisterCallbackDelayed(type, callback);
		}
	}
	
	private void unregisterCallbackDelayed(final String type, final IRecommendationServiceCallback callback) {
		if (isReady()) {
			unregisterCallback(type, callback);
		} else if (callback != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					unregisterCallback(type, callback);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}

}
