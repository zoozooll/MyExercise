/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.message.BasicNameValuePair;
import org.jasypt.util.password.PasswordEncryptor;

import android.content.Context;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceHandler;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.database.PasswordField;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.http.Header;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.msm.MessageReceiver;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.recommendation.RecommendationManager;
import com.oregonscientific.meep.util.HmacUtils;
import com.oregonscientific.meep.util.NetworkUtils;
import com.oregonscientific.meep.util.UriUtils;

public class AccountServiceHandler extends ServiceHandler {

	private static final String TAG = "AccountServiceHandler";
	
	/**
	 * The client agent string, for initializing AndroidHTTPClient
	 */
	private final String CLIENT_AGENT = "meep-android";
	
	/**
	 * The activation URI, for initializing HTTPPost object
	 */
	private final String ACTIVATION_URI = "https://portal.meeptablet.com/1/device/activation/";
	private final String AVATAR_URI = "https://portal.meeptablet.com/1/objectstore/avatar";
	
	private final String AVATAR_SANDBOX_HOST = "http://meeptablet-userdelivery.storage.googleapis.com";
	private final String AVATAR_PRODUCTION_HOST = "http://dqdfa9ixcoqmg.cloudfront.net";
	
	private final String AUTHORIZATION_KEY = "41c4a8bbab6f48fbb3af3e0c0ca748979dee7e0d13e945628bbf32d6f30477990964c54a0a90423ea1d2342f0ddc868af5e575d839a048e790c921f3f2b92a76";
	
	/**
	 * A Flag indicating is logging in or not
	 */
	private boolean mIsLoggingIn = false;
	
	private final String KEY_USER_ID = "userid";
	private final String KEY_NICKNAME = "nickname";
	private final String KEY_AVATAR = "avatar";
	private final String KEY_MEEPTAG = "meeptag";
	private final String KEY_CHECKSUM = "checksum";
	private final String KEY_TOKEN = "token";
	private final String KEY_VERSION = "version";
	private final String KEY_MAC_ADDRESS = "mac_address";
	private final String KEY_FILE = "file";
	
	private final String KEY_TEMP_IDENTIFIER = UUID.randomUUID().toString();
	
	private final long MAX_RESEND_TIME = 300;
	
	/**
	 * This is a list of callbacks that have been registered with the
	 * service.  Note that this is package scoped (instead of private) so
	 * that it can be accessed more efficiently from inner classes.
	 */
	private final RemoteCallbackList<IAccountServiceCallback> mCallbacks = 
			new RemoteCallbackList<IAccountServiceCallback>();
	
	/**
	 * A cache of auth token
	 */
	private final ConcurrentHashMap<String, String> tokenCache =
		new ConcurrentHashMap<String, String>();
	
	/**
	 * A scheduler for executing async tasks
	 */
	private final ScheduledExecutorService mScheduler = 
		Executors.newScheduledThreadPool(5);
	
	private final OrmLiteSqliteOpenHelper mHelper;
	private final Handler mCallbackHandler;
	
	/**
	 * The internally used message codes
	 */
	private static final int OPERATION_SIGN_IN = 1;
	private static final int OPERATION_SIGN_OUT = 2;
	private static final int OPERATION_UPDATE_ACCOUNT = 3;
	
	/**
	 * Internally Message for delivering to registered callbacks
	 */
	private final class CallbackMessage {
		
		private boolean result;
		private String errorMessage;
		private Account account;
		
		private CallbackMessage(boolean result, String errorMessage, Account account) {
			this.result = result;
			this.errorMessage = errorMessage;
			this.account = account;
		}
		
	}
	
	/**
	 * The Handler used to execute operations on the main thread. This is used to
	 * invoke methods on the registered callbacks
	 */
	private static final class CallbackHandler extends Handler {
		
		final WeakReference<RemoteCallbackList<IAccountServiceCallback>> callbacks;
		
		CallbackHandler(RemoteCallbackList<IAccountServiceCallback> callbacks) {
			this.callbacks = new WeakReference<RemoteCallbackList<IAccountServiceCallback>>(callbacks);
		}
		
		
		@Override 
		public void handleMessage(android.os.Message msg) {
			CallbackMessage m = msg == null ? null : (CallbackMessage) msg.obj;
			RemoteCallbackList<IAccountServiceCallback> callbackList = callbacks.get();
			
			// Quick return if there is nothing to process
			if (m == null || callbackList == null) {
				return;
			}
			
			int i = callbackList.beginBroadcast();
			while (i > 0) {
				i--;
				try {
					switch (msg.what) {
					case OPERATION_SIGN_IN:
						callbackList.getBroadcastItem(i).onSignIn(m.result, m.errorMessage, m.account);
						break;
					case OPERATION_SIGN_OUT:
						callbackList.getBroadcastItem(i).onSignOut(m.result, m.errorMessage, m.account);
						break;
					case OPERATION_UPDATE_ACCOUNT:
						callbackList.getBroadcastItem(i).onUpdateUser(m.result, m.errorMessage, m.account);
						break;
					}
				} catch (Exception ex) {
					// Error occurred while invoking callback
					Log.e(TAG, ex + " occurred while invoking callback");
				}
			}
			callbackList.finishBroadcast();
		}
		
	}
	
	public AccountServiceHandler(Context context, OrmLiteSqliteOpenHelper helper) {
		super(context);
		
		mHelper = helper;
		mCallbackHandler = new CallbackHandler(mCallbacks);
	}
	
	/**
	 * Determines whether or not the message service is connected with messaging server
	 * @return true if connected, false otherwise
	 */
	private boolean isMessageServiceConnected() {
		MessageManager mm = (MessageManager) ServiceManager.getService(getContext(), ServiceManager.MESSAGE_SERVICE);
		return mm.isConnected();
	}
	
	/**
	 * Sign in with MEEP Server using the {@code account} that was last logged
	 * in with server. 
	 * 
	 * @param retrievePermissions {@code true} to ask {@link com.oregonscientific.meep.home.permission.PermissionManager}
	 * to retrieve permission settings from server, {@code false} otherwise. By default, this is {@code false}
	 */
	public void signIn(boolean retrievePermissions) {
		// Quick return if the message service is not connected to server
		// The service will automatically sign-in with the last logged 
		// in account when message service connects with the server
		if (!isMessageServiceConnected()) {
			return;
		}
		
		Account account = getLastLoggedInAccount();
		signIn(account, retrievePermissions);
	}
	
	/**
	 * Sign in with MEEP Server using the given {@code account}
	 * 
	 * @param account The Account object representing the user
	 * @param retrievePermission {@code true} to ask {@link com.oregonscientific.meep.home.permission.PermissionManager}
	 * to retrieve permission settings from server, {@code false} otherwise. By default, this is {@code false}
	 */
	public synchronized void signIn(Account account, boolean retrievePermissions) {
		// Quick return if the user has already signed in or another user
		// is already signed in
		Account loggedInAccount = getLoggedInAccount();
		if (loggedInAccount != null) {
			if (!loggedInAccount.equals(account)) {
				Log.e(TAG, "Another account is already logged in");
				return;
			} else {
				signOut();
			}
		}
		
		String token = account == null ? null : account.getToken();
		String key = account == null ? KEY_TEMP_IDENTIFIER : account.getId();
		signIn(key, token, retrievePermissions);
	}
	
	/**
	 * Sign-in using the given {@code token}.
	 * 
	 * @param key The key to use to cache the auth token
	 * @param token The token to use to sign-in with server
	 * @param retrievePermissions {@code true} to ask {@link com.oregonscientific.meep.home.permission.PermissionManager}
	 * to retrieve permission settings from server, {@code false} otherwise. By default, this is {@code false}
	 */
	private synchronized void signIn(final String key, String token, final boolean retrievePermissions) {
		// Quick return if the request cannot be processed
		if (key == null) {
			return;
		}
		
		MessageFilter filter = new MessageFilter(Message.PROCESS_ACCOUNT);
		filter.addOperation(Message.OPERATION_CODE_SIGN_IN);
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				boolean result = false;
				Account account = null;
				String errorMessage = null;
				
				if (message != null) {
					switch (message.getStatus()) {
					case Status.SUCCESS_OK:
						// Retrieves the unique identifier of the user
						final String identifier = (String) message.getProperty(KEY_USER_ID);
						
						try {
							account = getAccount(identifier);
							if (account == null) {
								// Creates a new "Account" if the an account with the 
								// identifier cannot be found
								account = new Account(identifier);
								Dao<Account, String> dao = mHelper.getDao(Account.class);
								account.setDao(dao);
								account.setMeepTag((String) message.getProperty(KEY_MEEPTAG));
								
								// The "Account" must have used the temporary auth token to
								// sign-in, replace the auth token cache
								String token = tokenCache.get(KEY_TEMP_IDENTIFIER);
								tokenCache.remove(KEY_TEMP_IDENTIFIER);
								if (identifier != null && token != null) {
									tokenCache.put(identifier, token);
								}
							}
							
							// Retrieves token from cache
							String authToken = tokenCache.get(identifier);
							account.setToken(authToken);
							
							account.setIsLastSignIn(true);
							account.setIsLoggedIn(true);
							updateLocalUserAccount(account, message);
							
							// Set account sign-in status
							setAccountSigninStatus(account);
							
							PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
							pm.syncAccessSchedule(identifier);
							
							// Retrieve recommendations
							RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
							rm.syncRecommendations(identifier, RecommendationManager.TYPE_WEB_FROM_ADMIN);
							rm.syncRecommendations(identifier, RecommendationManager.TYPE_WEB_FROM_PARENT);
							rm.syncRecommendations(identifier, RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN);
							rm.syncRecommendations(identifier, RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
							
							// Successfully performed all tasks after sign-in
							result = true;
						} catch (SQLException ex) {
							errorMessage = "Cannot sign-in " + identifier == null ? "" : identifier + " because " + ex;
						}
						break;
					case Status.CLIENT_ERROR_UNAUTHORIZED:
						account = getAccount(key);
						if (account != null) {
							try {
								account.setToken(null);
								account.update();
								errorMessage = AccountManager.ERROR_AUTH_TOKEN_EXPIRED;
							} catch (Exception ex) {
								// Failed to update auth token for the {@code account}. Ignore
								errorMessage = "Failed to update auth token for " + key + " because " + ex;
							}
						}
						break;
					default:
						errorMessage = message.getMessage();
						break;
					}
					
					// The handler is no longer trying sign in with server
					mIsLoggingIn = false;
					
					// Notify registered callbacks that we signed-in
					invokeCallback(OPERATION_SIGN_IN, result, errorMessage, account);
				}
			}
			
		};
		
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
		// Perform sign-in
		login(key, token, retrievePermissions);
	}
	
	/**
	 * Perform the actual login mechanism using the given {@code token}
	 * 
	 * @param key The key to use to cache the auth token
	 * @param token The token to use to sign-in with server
	 * @param retrievePermissions {@code true} to ask {@link com.oregonscientific.meep.home.permission.PermissionManager}
	 * to retrieve permission settings from server, {@code false} otherwise. By default, this is {@code false}
	 */
	private synchronized void login(final String key, final String token, final boolean retrievePermissions) {
		if (mIsLoggingIn == true) {
			Log.e(TAG, "Another user is logging in");
			return;
		}
		
		// The handler is trying to login
		mIsLoggingIn = true;
		
		if (key != null && token != null && token.length() > 0) {
			// Stores the auth token in cache
			tokenCache.put(key, token);
			
			// Serialize the content into Message Object
			Message message = new Message(
					Message.PROCESS_ACCOUNT,
					Message.OPERATION_CODE_SIGN_IN);
			
			long time = System.currentTimeMillis();
			time /= 60000;
			message.addProperty(KEY_CHECKSUM, HmacUtils.process(Long.toString(time), Build.SERIAL));
			message.addProperty(KEY_TOKEN, token);
			message.addProperty(KEY_VERSION, Build.VERSION.NAME);
			sendMessage(message);
		} else {
			Runnable resendTask = new Runnable() {
				@Override
				public void run() {
					Log.d(TAG, "Have to retrieve auth token...");
					// Get auth token from server, the handler should still be
					// considered logging in while retrieving auth token
					String t = getAuthToken();
					
					mIsLoggingIn = false;
					if (t != null) {
						signIn(key, t, retrievePermissions);
					} else {
						// Either the authentication server is down or the device is not registered
						invokeCallback(OPERATION_SIGN_IN, false, AccountManager.ERROR_NO_AUTH_TOKEN, null);
						Log.w(TAG, "Either the authentication server is down or the device is not registered");
					}
				}
			};
			// Resend the sign-in message
			Log.d(TAG, "Scheduling to send sign-in request...");
			try {
				mScheduler.schedule(resendTask, MAX_RESEND_TIME, TimeUnit.MILLISECONDS);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot schdule to send sign-in request because " + ex + " occurred");
			}
		}
	}
	
	/**
	 * Sign out with MEEP server.
	 * 
	 * @return true if the current logged in user is signed-out, false
	 * if no user was signed in or there was an error signing out.
	 */
	public synchronized boolean signOut() {
		boolean result = false;
		String errorMessage = null;
		
		// Retrieve currently logged in account
		Account account = getLoggedInAccount();
		if (account == null) {
			errorMessage = AccountManager.ERROR_ACCOUNT_NOT_FOUND;
		} else {
			try {
				// Removes auth token from cache
				tokenCache.remove(account.getMeepTag());
				
				// Updates user account
				account.setIsLoggedIn(false);
				account.update();
				result = true;
			} catch (SQLException ex) {
				errorMessage = "Cannot sign out " + account + " because " + ex + " occurred"; 
				Log.e(TAG, errorMessage);
			}
		}
		
		// The service is no long in the process of signing in
		mIsLoggingIn = false;
		
		// Notify registered callbacks that we signed-out
		invokeCallback(OPERATION_SIGN_OUT, result, errorMessage, account);
		
		// Return result
		return result;
	}
	
	/**
	 * Authenticates the given {@code identity} against the authority
	 * 
	 * @param identity The Identity to authenticate
	 * @return true if the Identity is successfully authenticated, false otherwise
	 */
	public boolean authenticate(Identity identity) {
		// Quick return if authentication cannot be performed
		if (identity == null || mHelper == null) {
			return false;
		}
		
		Account account = getAccount(identity.getName());
		return account.getPassword().equals(identity.password);
	}
	
	/**
	 * Returns a value indicating whether a user's credentials are valid
	 * 
	 * @return true if the specified user name and password are valid, false otherwise
	 */
	public boolean authenticateUser(String username, String password) {
		// Quick return if authentication cannot be performed
		if (username == null || password == null || mHelper == null) {
			return false;
		}
		
		Account account = getAccount(username);
		return account == null ? false : checkPassword(account, password);
	}
	
	/**
	 * Determines whether or not an unencrypted (plain) password matches against 
	 * the encrypted (a digest) password of an User
	 * @param account the account against which to check
	 * @param plainPassword the plain password to check
	 * @return true if the passwords match, false otherwise
	 */
	private boolean checkPassword(Account account, String plainPassword) {
		try {
			Field field = Schema.getAttributes(Account.class).getColumns().get(Account.PASSWORD_FIELD_NAME);
			PasswordField pwdField = field.getAnnotation(PasswordField.class);
			PasswordEncryptor encryptor = pwdField.encryptorClass().newInstance();
			return encryptor.checkPassword(plainPassword, (String) account.getPassword());
		} catch (Exception e) {
			// The passwords cannot be matched
			return false;
		}
	}
	
	/**
	 * Notify registered callbacks of an event
	 * 
	 * @param what the message code of the event took place
	 * @param result the result of the operation
	 * @param errorMessage the detailed error message if any
	 * @param account the {@link Account} associated with the event, can be {@code null}
	 */
	private void invokeCallback(int what, boolean result, String errorMessage, Account account) {
		// Notify registered callbacks that we updated the user account
		android.os.Message msg = mCallbackHandler.obtainMessage(
				what, 
				new CallbackMessage(result, errorMessage, account));
		mCallbackHandler.sendMessage(msg);
	}
	
	/**
	 * Returns the full URL to the remote resource 
	 * 
	 * @param path the relative path to the remote resource
	 * @return the full URL to the remote resource or {@code null} if the relative path is {@code null}
	 */
	private String getAvatarUrl(String path) {
		// Quick return if the request cannot be processed
		if (path == null) {
			return null;
		}
		
		String host = Build.Environment.SANDBOX.equals(Build.environment) ? AVATAR_SANDBOX_HOST : AVATAR_PRODUCTION_HOST;
		return getAvatarUrl(host, path);
	}
	
	/**
	 * Returns the full URL to the remote resource 
	 * 
	 * @param prefix the scheme, host and port of an absolute URL
	 * @param path the relative path to the remote resource
	 * @return the full URL to the remote resource or {@code null} if the relative path is {@code null}
	 */
	private String getAvatarUrl(String prefix, String path) {
		// check the pass in path is it absolute
		String uriString = path == null ? "" : path.trim();
		while (uriString.startsWith("/")) {
			uriString = uriString.substring(1);
		}
		
		Uri uri = prefix == null ? null : Uri.parse(prefix);
		Uri.Builder builder = uri == null ? new Uri.Builder() : uri.buildUpon();
		builder.appendEncodedPath(uriString);
		
		return prefix == null ? path : builder.build().toString();
	}
	
	/**
	 * Modify the user profile and update server's data
	 *  
	 * @param account Account object that is going to be updated in both server and client
	 */
	public void updateUserAccount(final Account account) {
		// Quick return if there is nothing to process
		if (account == null) {
			return;
		}
		
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				try {
					$Avatar avatar = null;
					String profileIconUri = account.getIconAddress();
					
					if (UriUtils.isFileURI(profileIconUri)) {
						File file = new File(URI.create(profileIconUri));
						String response = uploadResource(AVATAR_URI, file);
						
						if (response != null) {
							Gson gson = new GsonBuilder().serializeNulls().create();
							avatar = gson.fromJson(response, $Avatar.class);
						}
						
						if (avatar == null || Status.isError(avatar.statusCode)) {
							invokeCallback(OPERATION_UPDATE_ACCOUNT, false, AccountManager.ERROR_UPDATING_PROFILE_PICTURE, account);
							return;
						} else {
							account.setIconAddress(getAvatarUrl(avatar.prefix, avatar.url));
						}
					}
					
					// Update server's data
					final Message msg = new Message(
							Message.PROCESS_ACCOUNT, 
							Message.OPERATION_CODE_SET_USER_NICKNAME);

					// Set properties {@see UserInfoMsgSender}
					String nickname = account.getNickname();
					msg.addProperty(KEY_NICKNAME, nickname == null ? "" : nickname);
					String relativePath = avatar == null ? null : avatar.url;
					msg.addProperty(KEY_AVATAR, relativePath);
					msg.addProperty(KEY_MEEPTAG, account.getMeepTag());
					
					MessageFilter filter = new MessageFilter(msg.getMessageID());
					MessageReceiver receiver = new MessageReceiver(filter) {

						@Override
						public void onReceive(Message message) {
							boolean result = false;
							String errorMessage = null;
							
							if (message != null) {
								switch (message.getStatus()) {
								case Status.SUCCESS_OK:
									try {
										// When updating an Account object, URL
										// of the avatar is expected to be an
										// absolute URL. However, when sending
										// update command to server, URL of the
										// avatar is expected to be a relative
										// URL. As a result, we need to change
										// the avatar URL in the message
										// returned from server to the absolute
										// URL constructed after the avatar is
										// uploaded to server
										msg.addProperty(KEY_AVATAR, account.getIconAddress());
										updateLocalUserAccount(account, msg);
										result = true;
									} catch (SQLException ex) {
										errorMessage = "Cannot update account for " + account + " because " + ex + " occurred";
										Log.e(TAG, errorMessage);
									}
									break;
								case Status.CLIENT_ERROR_NOT_FOUND:
									errorMessage = AccountManager.ERROR_ACCOUNT_NOT_FOUND;
									break;
								default:
									errorMessage = message.getMessage();
									break;
								}
							}
							
							// Notify registered callbacks that we updated the user account
							invokeCallback(OPERATION_UPDATE_ACCOUNT, result, errorMessage, account);
						}
						
					};
					
					// Register the inline receiver to process the {@link Message}
					registerReceiver(receiver);
					// Update account on remote server
					sendMessage(msg);
				} catch (Exception ex) {
					Log.e(TAG, "Cannot update user account because " + ex);
				}
			}
			
		});
	} 

	/**
	 * Retrieves the currently logged in account
	 *  
	 * @return  The currently logged in account, null if no account is logged in
	 * with server. 
	 */
	public Account getLoggedInAccount() {
		// Quick return if the call cannot be processed
		if (mHelper == null) {
			return null;
		}
		
		Account result = null;
		try {
			// Retrieve from database the "account" with the 
			// logged in field set to true
			Dao<Account, Long> dao = mHelper.getDao(Account.class);
			QueryBuilder<Account, Long> qb = dao.queryBuilder();
			qb.where().eq(Account.IS_LOGGED_IN_FIELD_NAME, true);
			result = qb.queryForFirst();
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot retrieve the current logged in account because " + ex);
		}
		return result;
	}
	
	/**
	 * Retrieves the system default account
	 * 
	 * @return the system default {@link Account}
	 */
	public Account getDefaultAccount() {
		// Quick return if the call cannot be processed
		if (mHelper == null) {
			return null;
		}
		
		Account result = null;
		try {
			// Retrieve from database the "account" with the 
			// logged in field set to true
			Dao<Account, Long> dao = mHelper.getDao(Account.class);
			QueryBuilder<Account, Long> qb = dao.queryBuilder();
			qb.where().eq(Account.ID_FIELD_NAME, Account.DEFAULT_USER_ID);
			result = qb.queryForFirst();
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot retrieve the system default account because " + ex);
		}
		return result;
	}
	
	/**
	 * Sets sign-in status of accounts in the system. At any given time,
	 * only 1 user is allowed to be logged in with server
	 * 
	 * @param account the account signed-in with server
	 */
	private void setAccountSigninStatus(final Account account) {
		// Quick return if there is nothing to process
		if (mHelper == null || account == null) {
			return;
		}
		
		try {
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					Dao<Account, Long> dao = mHelper.getDao(Account.class);
					
					// Reset last logged in account
					UpdateBuilder<Account, Long> ub = dao.updateBuilder();
					ub.updateColumnValue(Account.IS_LAST_LOGGED_IN_FIELD_NAME, false);
					ub.where().ne(Account.ID_FIELD_NAME, account.getId());
					ub.update();
					
					// Reset current log in status
					ub = dao.updateBuilder();
					ub.updateColumnValue(Account.IS_LOGGED_IN_FIELD_NAME, false);
					ub.where().ne(Account.ID_FIELD_NAME, account.getId());
					ub.update();
					
					// Sets the last logged in account
					ub = dao.updateBuilder();
					ub.updateColumnValue(Account.IS_LAST_LOGGED_IN_FIELD_NAME, true);
					ub.where().eq(Account.ID_FIELD_NAME, account.getId());
					ub.update();
					
					// Sets the current log in status
					ub = dao.updateBuilder();
					ub.updateColumnValue(Account.IS_LOGGED_IN_FIELD_NAME, true);
					ub.where().eq(Account.ID_FIELD_NAME, account.getId());
					ub.update();
					
					return null;
				}
				
			});
		} catch (SQLException ex) {
			Log.e(TAG, "Failed to update sign-in status because " + ex);
		}
	}
	
	/**
	 * Returns the Account with the given {@code identifier}
	 * @param identifier The unique identifier of the account
	 * @return The Account with the given {@code identifier}, null if there 
	 * is no Account with the given {@code identifier}
	 */
	private Account getAccount(String identifier) {
		// Quick return if there is nothing to process
		if (identifier == null || mHelper == null) {
			return null;
		}
		
		Account result = null;
		try {
			Dao<Account, Long> dao = mHelper.getDao(Account.class);
			QueryBuilder<Account, Long> qb = dao.queryBuilder();
			qb.where().eq(Account.ID_FIELD_NAME, identifier);
			result = qb.queryForFirst();
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot retrieve account for " + identifier + " because " + " occurred");
		}
		return result;
	}
	
	/**
	 * Updates attributes in the Account object with properties in the {@code message}
	 * 
	 * @param account The Account object to update
	 * @param message The Message containing the new properties
	 * @throws SQLException thrown if there was an error
	 */
	private void updateLocalUserAccount(Account account, Message message) throws SQLException {
		// Quick return if there is nothing to process
		if (mHelper == null || account == null) {
			return;
		}
		
		// Updates user account data
		if (message != null) {
			String firstName = (String) message.getProperty(Account.FIRST_NAME_FIELD_NAME);
			String lastName = (String) message.getProperty(Account.LAST_NAME_FIELD_NAME);
			String nickname = (String) message.getProperty(Account.NICKNAME_FIELD_NAME);
			
			if (firstName != null) {
				account.setFirstName(firstName);
			}
			if (lastName != null) {
				account.setLastName(lastName);
			}
			
			// Note: Set nickname as the first name too, since sign-in request does not return nickname
			if (nickname != null) {
				account.setNickname(nickname);
			} else if (firstName != null) {
				account.setNickname(firstName);
			}
			
			String avatarUrl = (String) message.getProperty(KEY_AVATAR);
			if (avatarUrl != null) {
				if (!Uri.parse(avatarUrl).isAbsolute()) {
					avatarUrl = getAvatarUrl(avatarUrl);
				}
			}
			account.setIconAddress(avatarUrl);
		}
		
		Dao<Account, Long> dao = mHelper.getDao(Account.class);
		dao.createOrUpdate(account);
	}
	
	/**
	 * Retrieve the account last logged in with server
	 * 
	 * @return The Account that was last logged in with server
	 */
	public Account getLastLoggedInAccount() {
		// Quick return if the call cannot be processed
		if (getContext() == null || mHelper == null) {
			return null;
		}
		
		Account result = null;
		try {
			Dao<Account, Long> dao = mHelper.getDao(Account.class);
			QueryBuilder<Account, Long> qb = dao.queryBuilder();
			qb.where().eq(Account.IS_LAST_LOGGED_IN_FIELD_NAME, true);
			result = qb.queryForFirst();
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot retrieve the account last logged in because " + ex + " occurred");
		}
		return result;
	}
	
	/** 
	 * Register as a callback for Account Service to receive the result after asynchronous server messaging
	 * 
	 * @param callback An interface of type IAccountServiceCallback
	 */
	public synchronized void registerCallback(IAccountServiceCallback callback) throws RemoteException {
		if (callback != null) {
			Log.d(TAG, "Registering callback...");
			mCallbacks.register(callback);
		}
	}

	/** 
	 * Unregister a previously registered callback in Account Service
	 * 
	 * @param callback An interface of type IAccountServiceCallback
	 */
	public synchronized void unregisterCallback(IAccountServiceCallback callback) throws RemoteException {
		if (callback != null) {
			mCallbacks.unregister(callback);
			Log.d(TAG, "Unregistered callback.");
		}
	}
	
	/**
	 * Do some cleanup on destroy
	 */
	public synchronized void clearCallbacks() {
		// Unregister all callbacks.
		mCallbacks.kill();
	}
	
	/**
	 * Uploads the file at {@code filePath} to remote server
	 * 
	 * @param file the file to upload
	 * @return A String representation of the remote file
	 */
	private String uploadResource(String uri, File file) {
		// Quick return if there is no network connection
		if (!NetworkUtils.hasConnection(getContext()) || file == null || uri == null) {
			return null;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance(CLIENT_AGENT);
		
		String result = null;
		try {
			// The target remote location
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setHeader(Header.AUTHORIZATION, AUTHORIZATION_KEY);
			
			// Create the POST body
			MultipartEntity postBody = new MultipartEntity();
			ByteArrayBody body = new ByteArrayBody(FileUtils.readFileToByteArray(file), file.getName());
			postBody.addPart(KEY_FILE, body);
			httpPost.setEntity(postBody);
			
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine == null ? Status.SUCCESS_NO_CONTENT : statusLine.getStatusCode();
			
			if (!Status.isError(statusCode)) {
				HttpEntity entity = response.getEntity();
				StringBuilder sb = new StringBuilder();
				
				// Append result to String Builder
				if (entity != null) {
					InputStream is = entity.getContent();
					if (is != null) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(is));
						String line;
						while ((line = reader.readLine()) != null) {
							sb.append(line);
						}
						is.close();
					}
					entity.consumeContent();
				}
				
				// Server reponds with the URL of the avatar
				result = sb.toString();
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot upload local resource because " + ex + " occurred");
		} finally {
			// Close the HTTP client
			client.close();
		}
		
		// Returns a String representation of the URL of the remote resource
		return result;
	}
	
	/**
	 * Retrieves auth token from server through HTTP request
	 * 
	 * @return The auth token returned from server, empty string if there 
	 * was an error interpreting the response returned from server or {@code null} if 
	 * server returned an error
	 */
	private String getAuthToken() {
		// Quick return if there is no network connection
		if (!NetworkUtils.hasConnection(getContext())) {
			return null;
		}
		
		AndroidHttpClient client = AndroidHttpClient.newInstance(CLIENT_AGENT);
		
		// Get MAC address of device
		WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
		String macAddress = wifiManager.getConnectionInfo().getMacAddress();
		
		String result = null;
		try {
			// The target remote location
			HttpPost httpPost = new HttpPost(ACTIVATION_URI + Build.SERIAL);
			
			// Always provide MAC address of the running device
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(KEY_MAC_ADDRESS, macAddress));
			
			// Set the Entity (Post body)
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = client.execute(httpPost);
			
			// Get entity from response
			HttpEntity entity = response.getEntity();
			StringBuilder sb = new StringBuilder();
			
			// Append result to String Builder
			if (entity != null) {
				InputStream is = entity.getContent();
				if (is != null) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					is.close();
				}
				entity.consumeContent();
			}

			StatusLine statusLine = response.getStatusLine(); 
			int statusCode = statusLine == null ? 500 : statusLine.getStatusCode();
			if (statusCode == 200) {
				Message message = Message.fromJson(sb.toString());
				if (message != null && message.getStatus() == Status.SUCCESS_OK) {
					result = (String) message.getProperty(KEY_TOKEN);
				}
			} else {
				result = null;
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot obtain auth token because " + ex + " occurred");
			result = "";
		} finally {
			// Close the HTTP client
			client.close();
		}
		
		// Returns the auth token returned from server
		return result;
	}
	
}
