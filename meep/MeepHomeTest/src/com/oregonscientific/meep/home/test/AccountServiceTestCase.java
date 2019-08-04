/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import android.os.RemoteException;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.AccountService;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.http.RestClient;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.Message;

public class AccountServiceTestCase extends BaseServiceTestCase<AccountService> {
	
	private final String TAG = AccountServiceTestCase.class.getSimpleName();
	
	private MockWebSocketServer mServer;

	public AccountServiceTestCase(Class<AccountService> serviceClass) {
		super(serviceClass);
	}
	
	public AccountServiceTestCase() {
		super(AccountService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		try {
			Log.d(TAG, "Setting up WebSocket server...");
			if (mServer == null) {
				mServer = new MockWebSocketServer();
			}
			mServer.start();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot create WebSocket server because " + ex + " occurred");
			mServer = null;
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		// Tear down WebSocket server
		Log.d(TAG, "Tearing down WebSocket server...");
		if (mServer != null) {
			mServer.stop();
			mServer.removeAllResponders();
			mServer = null;
		}
		
		super.tearDown();
	}
	
	protected void signinTest() {
		IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

			@Override
			public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
				completeTest(success);
			}

			@Override
			public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
				// Not in the scope of this test. Ignore
			}

			@Override
			public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
				// Not in the scope of this test. Ignore
			}
			
		};
		
		// Issue calls to the method in test
		AccountManager accountManager = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		accountManager.registerCallback(accountCallback);
		accountManager.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		accountManager.unregisterCallback(accountCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testSigninWithMockServer() {
		// Setup build environment to test against MockWebSocketServer
		Build.environment = Build.Environment.SANDBOX;
		
		// Setup mock server response
		if (mServer != null) {
			mServer.addResponder(MessageResponders.SIGN_IN);
		}
		
		signinTest();
	}
	
	@SmallTest
	public void testSigninWithProductionServer() {
		// Setup build environment to test against MockWebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		signinTest();
	}
	
	@SmallTest
	public void testSignOutWithMockServer() {
		// Setup build environment to test against MockWebSocketServer
		Build.environment = Build.Environment.SANDBOX;
		
		// Setup mock server response
		if (mServer != null) {
			mServer.addResponder(MessageResponders.SIGN_IN);
		}
		
		IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

			@Override
			public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
				if (success) {
					AccountManager accountManager = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
					accountManager.signOut();
				} else {
					// Cannot sign-in, the test failed
					completeTest(false);
				}
			}

			@Override
			public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
				completeTest(success);
			}

			@Override
			public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
				// Not in the scope of this test. Ignore
			}
			
		};
		
		// Issue calls to the method in test
		AccountManager accountManager = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		accountManager.registerCallback(accountCallback);
		accountManager.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		accountManager.unregisterCallback(accountCallback);
		
		assertTrue(mTestResult);
	}
	
	protected void updateUserAccountTest() {
		final AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

			@Override
			public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
				if (success && account != null) {
					account.setIconAddress(Test.DATA_ACCOUNT_ICON);
					account.setFirstName(Test.DATA_ACCOUNT_FIRST_NAME);
					account.setNickname(Test.DATA_ACCOUNT_NICKNAME);
					am.updateUserAccount(account);
				} else {
					completeTest(success);
				}
			}

			@Override
			public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
				// Not in the scope of this test. Ignore
			}

			@Override
			public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
				completeTest(success);
			}
			
		};
		
		// Issue calls to the method in test
		am.registerCallback(accountCallback);
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.unregisterCallback(accountCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testUpdateUserAccountWithMockServer() {
		// Setup build environment to test against production
		Build.environment = Build.Environment.SANDBOX;
		
		// Setup mock server response
		if (mServer != null) {
			mServer.addResponder(MessageResponders.SIGN_IN);
			mServer.addResponder(MessageResponders.UPDATE_ACCOUNT);
		}
		
		updateUserAccountTest();
	}
	
	@SmallTest
	public void testUpdateUserAccountWithProductionServer() {
		// Setup build environment to test against production
		Build.environment = Build.Environment.PRODUCTION;
		
		updateUserAccountTest();
	}
	
	@MediumTest
	public void testPollForNews() {
		final AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

			@Override
			public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
				if (success && account != null) {
					RestClient client = new RestClient();
					client.setCredentials(account.getToken());
					client.get("news/fetch/000000000000000000000000", new AsyncHttpResponseHandler() {
						
						@Override
						public void onSuccess(String content) {
							Log.d(TAG, "Response from polling for news: " + content);
							Message message = Message.fromJson(content);
							if (message == null) {
								completeTest(false);
							}
							
							completeTest(message.getStatus() == Status.SUCCESS_OK);
						}
						
						@Override
						public void onFailure(Throwable error, String content) {
							completeTest(false);
						}
						
					});
				} else {
					completeTest(success);
				}
			}

			@Override
			public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
				// Not in the scope of this test. Ignore
			}

			@Override
			public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
				completeTest(success);
			}
			
		};
		
		// Issue calls to the method in test
		am.registerCallback(accountCallback);
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.unregisterCallback(accountCallback);
		
		assertTrue(mTestResult);
	}
	
}
