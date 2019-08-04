/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.Identity;

/**
 * Interface for interacting with the AccountService
 */
interface IAccountService {
	
	/**
	 * Sign in with MEEP Server
	 * 
	 * @param account The Account to sign-in
	 * @param retrievePermission {@code true} to ask PermissionManager to retrieve permission settings from
	 * server, {@code false} otherwise
	 */
	void signIn(in Account account, boolean retrievePermission);
	
	/**
	 * Sign out with MEEP server.
	 * 
	 * @return true if the current logged in user is signed-out, false
	 * if no user was signed in or there was an error signing out.
	 */
	boolean signOut();
	
	/**
	 * Returns a value indicating whether a user's credentials are valid
	 * 
	 * @return true if the specified user name and password are valid, false otherwise
	 */
	boolean authenticate(in Identity identity);
	
	/**
	 * Modify the user profile and update server's data
	 *  
	 * @param user User object that is going to be updated in both server and client
	 */
	void updateUserAccount(in Account account);
		
	/**
	 * Retrieve currently logged in user
	 *  
	 * @return  The currently logged in user, null if no user signed in. 
	 */
	Account getLoggedInAccount();
	
	/**
	 * Returns the Account that was last logged in
	 */
	Account getLastLoggedInAccount();
	
	/**
	 * Returns the system default Account
	 */
	Account getDefaultAccount();
	
	/** 
	 * Register as a callback for Account Service to receive the result after asynchronous server messaging
	 * 
	 * @param callback An interface of type IAccountServiceCallback
	 */
	void registerCallback(IAccountServiceCallback callback);
	
	/** 
	 * Unregister a previously registered callback in Account Service
	 * 
	 * @param callback An interface of type IAccountServiceCallback
	 */
	void unregisterCallback(IAccountServiceCallback callback);
}
