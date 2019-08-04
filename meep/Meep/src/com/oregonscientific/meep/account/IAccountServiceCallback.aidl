/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import com.oregonscientific.meep.account.Account;

/**
 * Interface for interacting with the AccountService
 */
oneway interface IAccountServiceCallback {

	/**
	 * Called by the account service when the {@code user} signed-in with MEEP server
	 * 
	 * @param success A boolean flag indicates sign-in operation was successful or not
	 * @param errorMessage The error message returned back from server, null if the sign-in operation was successful
	 * @param account The Account signed-in with server
	 */
	void onSignIn(boolean success, String errorMessage, out Account account);
	
	/**
	 * Called by the account service when the {@code account} signed-out from MEEP server
	 * 
	 * @param success A boolean flag indicates sign-out operation was successful or not
	 * @param errorMessage The error message returned back from server, null if the sign-out operation was successful
	 * @param account The account that was logged out
	 */
	void onSignOut(boolean success, String errorMessage, out Account account);
	
	/**
	 * Called by the account service when profile of the {@code account} is updated on
	 * MEEP server
	 * 
	 * @param success A boolean flag indicates update operation of user profile was successful or not
	 * @param errorMessage The error message returned back from server, null if the update operation of user profile was successful
	 * @param account the account that is updated
	 */
	void onUpdateUser(boolean success, String errorMessage, out Account account);

}

