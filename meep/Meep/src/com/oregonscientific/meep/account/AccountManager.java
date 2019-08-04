/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceAgent;
import com.oregonscientific.meep.ServiceConnector;

/**
 * This class provides access to the MEEP user accounts. User provides credentials 
 * (username and password) and account manager will authenticate against MEEP server.
 */
public class AccountManager extends ServiceAgent<IAccountService> {
	
	private static final String TAG = "AccountManager";
	
	/**
	 * The error message if the {@link Account} profile picture cannot be updated
	 */
	public static final String ERROR_UPDATING_PROFILE_PICTURE = "profile_picture";
	
	/**
	 * The error message if the {@link Account} is not found
	 */
	public static final String ERROR_ACCOUNT_NOT_FOUND = "account_not_found";
	
	/**
	 * The error message if an auth token is not available
	 */
	public static final String ERROR_NO_AUTH_TOKEN = "auth_token";
	
	/**
	 * The error message if an auth token is expired
	 */
	public static final String ERROR_AUTH_TOKEN_EXPIRED = "auth_token_expired";
	
	public AccountManager(ServiceConnector connector) {
		super(connector);
	}
	
	@Override
	protected IAccountService getInterface() {
		return IAccountService.Stub.asInterface(getConnector().getBinder());
	}
	
	/**
	 * Retrieve the Account currently logged in with MEEP server on the device. This 
	 * method will return <code>null</code> if the service is not ready
	 *  
	 * @return  The currently logged in Account, null if no user is logged in. 
	 */
	public Account getLoggedInAccount() {
		Account result = null;
		if (isReady()) {
			try {
				result = getService().getLoggedInAccount();
			} catch (RemoteException ex) {
				// Ignore
				Log.e(TAG, "Cannot retrieve currently logged in account because " + ex + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Retrieve the Account currently logged in with MEEP server on the device. This method
	 * will block until the underlying service becomes ready. User should not call this method
	 * in the main thread
	 * 
	 * @return The currently logged in Account, null if no user is logged in.
	 */
	public Account getLoggedInAccountBlocking() {
		Account result = null;
		if (isReady()) {
			result = getLoggedInAccount();
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Account> future = new FutureTask<Account>(new Callable<Account>() {

				@Override
				public Account call() throws Exception {
					return getLoggedInAccount();
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot obtain last logged in account because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Returns the system default Account
	 * 
	 * @return the system default {@link Account}
	 */
	public Account getDefaultAccount() {
		Account result = null;
		if (isReady()) {
			try {
				result = getService().getDefaultAccount();
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot retrieve the system default account because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the system default {@link Account}. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @return the default system {@link Account}
	 */
	public Account getDefaultAccountBlocking() {
		Account result = null;
		if (isReady()) {
			result = getDefaultAccount();
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Account> future = new FutureTask<Account>(new Callable<Account>() {

				@Override
				public Account call() throws Exception {
					return getDefaultAccount();
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot obtain last logged in account because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the last logged in account
	 * 
	 * @return the {@link Account} that was last logged in with MEEP server
	 */
	public Account getLastLoggedInAccount() {
		Account result = null;
		if (isReady()) {
			try {
				result = getService().getLastLoggedInAccount();
			} catch (RemoteException ex) {
				// Ignore
				Log.e(TAG, "Cannot retrieve the account that was last logged in because " + ex + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Retrieves the {@link Account} that was last logged in with the server. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @return the {@link Account} that was last logged in with MEEP server
	 */
	public Account getLastLoggedInAccountBlocking() {
		Account result = null;
		if (isReady()) {
			result = getLastLoggedInAccount();
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Account> future = new FutureTask<Account>(new Callable<Account>() {

				@Override
				public Account call() throws Exception {
					return getLastLoggedInAccount();
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot obtain last logged in account because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Sign in with server using the last user account that was logged in with server.
	 */
	public void signIn() {
		signIn(false);
	}
	
	/**
	 * Sign in with server using the last user account that was logged in. 
	 * 
	 * @param retrievePermissions {@code true} to ask {@link com.oregonscientific.meep.home.permission.PermissionManager}
	 * to retrieve permission settings from server, {@code false} otherwise. By default, this is {@code false}
	 */
	public void signIn(boolean retrievePermissions) {
		signIn(null, retrievePermissions);
	}
	
	/**
	 * Sign the given user in with MEEP Server.
	 * 
	 * @param account The Account to sign-in
	 */
	public void signIn(Account account) {
		signIn(account, false);
	}
	
	/**
	 * Sign the given user in with MEEP Server.
	 * 
	 * @param account The Account to sign-in
	 * @param retrievePermissions {@code true} to ask {@link com.oregonscientific.meep.home.permission.PermissionManager}
	 * to retrieve permission settings from server, {@code false} otherwise. By default, this is {@code false}
	 */
	public void signIn(Account account, boolean retrievePermissions) {
		if (isReady()) {
			try {
				getService().signIn(account, retrievePermissions);
			} catch (RemoteException e) {
				Log.e(TAG, account + " cannot sign-in because " + e + " occurred");
			}
		} else {
			signInDelayed(account, retrievePermissions);
		}
	}
	
	private void signInDelayed(final Account account, final boolean retrievePermissions) {
		if (isReady()) {
			signIn(account);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					signIn(account, retrievePermissions);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Sign out with MEEP server. This method will return <code>false</code> if the
	 * agent is not ready
	 * 
	 * @return true if the current logged in user is signed-out, false
	 * if no user was signed in or there was an error signing out.
	 */
	public boolean signOut() {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().signOut();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot sign out because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Sign out with MEEP server. This method will block until the underlying service 
	 * becomes ready. Calling this method in main thread may cause dead lock. User 
	 * should not call this method in the main thread
	 * 
	 * @return true if the current logged in user is signed-out, false
	 * if no user was signed in or there was an error signing out.
	 */
	public boolean signOutBlocking() {
		boolean result = false;
		if (isReady()) {
			result = signOut();
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return signOut();
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot sign out because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Updates attributes of the {@link Account} in the underlying data store.
	 * 
	 * @param account The user account to update
	 */
	public void updateUserAccount(Account account) {
		// Quick return if there is nothing to process
		if (account == null) {
			return;
		}
		
		if (isReady()) {
			try {
				getService().updateUserAccount(account);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot update " + account + " because " + ex + " occurred");
			}
		} else {
			updateUserAccountDelayed(account);
		}
	}
	
	private void updateUserAccountDelayed(final Account account) {
		if (isReady()) {
			updateUserAccount(account);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					updateUserAccount(account);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Returns a value indicating whether a user's credentials are valid. This method
	 * will return <code>false</code> if the agent is not ready
	 * 
	 * @return true if the specified user name and password are valid, false otherwise
	 */
	public boolean authenticate(String username, String password) {
		boolean result = false;
		if (username != null && password != null) {
			Identity identity = new Identity(username, password);
			result = authenticate(identity);
		}
		return result;
	}
	
	/**
	 * Returns a value indicating whether a user's credentials are valid. This
	 * method will block until the underlying service becomes ready. Calling this
	 * method in main thread may cause dead lock. User should not call this method
	 * in the main thread
	 * 
	 * @return true if the specified user name and password are valid, false
	 *         otherwise
	 */
	public boolean authenticateBlocking(final String username, final String password) {
		boolean result = false;
		if (isReady()) {
			result = authenticate(username, password);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return authenticate(username, password);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot authenticate the identity because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Returns a value indicating whether the given {@code identity} is valid. This method
	 * will return <code>false</code> if the agent is not ready
	 * 
	 * @return true if the specified user name and password are valid, false otherwise
	 */
	public boolean authenticate(Identity identity) {
		boolean result = false;
		if (isReady()) {
			try {
				return getService().authenticate(identity);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot authenticate the identity because " + ex + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Returns a value indicating whether the given {@code identity} is valid.
	 * This method will block until the underlying service becomes ready. Calling
	 * this method in main thread may cause dead lock. User should not call this
	 * method in the main thread
	 * 
	 * @param identity the identity to authenticate
	 * @return  true if the given {@code identity} is valid, false otherwise
	 */
	public boolean authenticateBlocking(final Identity identity) {
		boolean result = false;
		if (isReady()) {
			result = authenticate(identity);
		} else if (identity != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return getService().authenticate(identity);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot authenticate the identity because " + ex);
			}
		}
		return result;
	}
	
	/** 
	 * Register as a callback for Account Service to receive the result after asynchronous server messaging
	 * 
	 * @param callback An interface of type IAccountServiceCallback
	 */
	public void registerCallback(IAccountServiceCallback callback) {
		// Quick return if there is nothing to process
		if (callback == null) {
			return;
		}
		
		if (isReady()) {
			try {
				getService().registerCallback(callback);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot register callback because of " + ex);
			}
		} else {
			registerCallbackDelayed(callback);
		}
	}
	
	private void registerCallbackDelayed(final IAccountServiceCallback callback) {
		if (isReady()) {
			registerCallback(callback);
		} else if (callback != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					registerCallback(callback);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}

	/** 
	 * Unregister a previously registered callback in Account Service
	 * 
	 * @param callback An interface of type IAccountServiceCallback
	 */
	public void unregisterCallback(IAccountServiceCallback callback) {
		// Quick return if there is nothing to process
		if (callback == null) {
			return;
		}
		
		if (isReady()) {
			try {
				getService().unregisterCallback(callback);
			} catch (Exception ex) {
				Log.e(TAG, "Cannot unregister callback because of " + ex);
			}
		} else {
			unregisterCallbackDelayed(callback);
		}
	}
	
	private void unregisterCallbackDelayed(final IAccountServiceCallback callback) {
		if (isReady()) {
			registerCallback(callback);
		} else if (callback != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					unregisterCallback(callback);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}

}
