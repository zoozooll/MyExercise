/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ComponentName;
import android.os.RemoteException;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.google.gson.JsonObject;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.permission.Category;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.permission.PermissionService;

public class PermissionServiceTestCase  extends BaseServiceTestCase<PermissionService> {
	
	private final String TAG = PermissionServiceTestCase.class.getSimpleName();
	
	private final int NUMBER_OF_PERMISSION_SETTINGS = 13;
	
	private MockWebSocketServer mServer;
	
	public PermissionServiceTestCase(Class<PermissionService> serviceClass) {
		super(serviceClass);
	}
	
	public PermissionServiceTestCase() {
		super(PermissionService.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		try {
			// Sets the context of the test
			setContext(getTestContext());
			
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
	
	protected void resetAccessSchedule(String userId) {
		PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
		List<Permission> permissions = pm.getAccessScheduleBlocking(null, userId);
		
		for (Permission permission : permissions) {
			Component component = permission.getComponent();
			if (component != null && component.getDisplayName().equals("securitylevel")) {
				permission.setAccessLevel(Permission.AccessLevels.LOW);
			} else {
				permission.setAccessLevel(Permission.AccessLevels.ALLOW);
			}
		}
		
		pm.setAccessSchedule(null, userId, permissions);
	}
	
	@SmallTest
	public void testBadwordFiltering() {
		ExecutorService executor = Executors.newCachedThreadPool();
		executor.submit(new Runnable() {

			@Override
			public void run() {
				// Initialize language detector
				try {
					DetectorFactory.loadProfiles(getContext().getAssets(), "profiles");
					Log.i(TAG, "Initialized language detector...");
				} catch (Exception ex) {
					completeTest(false);
					return;
				}
				
				String original = readRawTextFile(getContext(), R.raw.badword_short);
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				String replaced = pm.replaceBadwordsBlocking(Test.DATA_ACCOUNT_USER_ID, original, "****");
				Log.i(TAG, "Original: " + original + " Replaced: " + replaced);
				
				completeTest(!original.equals(replaced));
			}
			
		});
		
		// Tear down configurations
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testBadwordCheckerShortPassage() {
		// Setup build environment to test against MockWebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

			@Override
			public void onSignIn(boolean success, String errorMessage, final Account account) throws RemoteException {
				if (success) {
					ExecutorService executor = Executors.newSingleThreadExecutor();
					executor.execute(new Runnable() {

						@Override
						public void run() {
							String original = readRawTextFile(getContext(), R.raw.badword_short);
							
							PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
							String replaced = pm.replaceBadwordsBlocking(account.getId(), original, "****");
							Log.i(TAG, "Original: " + original + " Replaced: " + replaced);
							
							completeTest(!original.equals(replaced));
						}
						
					});
				} else {
					// Cannot sign-in, the test failed
					completeTest(false);
				}
			}

			@Override
			public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
				// Ignore
			}

			@Override
			public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
				// Not in the scope of this test. Ignore
			}
			
		};
		
		// Issue calls to the method in test
		AccountManager accountManager = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		accountManager.registerCallback(accountCallback);
		accountManager.signOut();
		accountManager.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		accountManager.unregisterCallback(accountCallback);
		
		assertTrue(mTestResult);
	}
	
	public void testCheckUrlForUser() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				boolean isAccessible = pm.isUrlAccessibleBlocking(Test.DATA_ACCOUNT_USER_ID, Test.DATA_BLACKLIST_URL);
				
				if (isAccessible) {
					completeTest(false);
					return;
				}
				
				isAccessible = pm.isUrlAccessibleBlocking(Test.DATA_ACCOUNT_USER_ID, Test.DATA_WHITELIST_URL);
				completeTest(isAccessible);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testSyncAccessScheduleForUser() {
		// Setup build environment to test against MockWebSocketServer
		Build.environment = Build.Environment.SANDBOX;
		
		// Setup mock server response
		if (mServer != null) {
			mServer.addResponder(MessageResponders.GET_PERMISSIONS);
		}
		
		PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
		pm.syncAccessSchedule(Test.DATA_ACCOUNT_USER_ID);
		
		// Sleep for 2 seconds
		sleep(2000);
		
		List<Permission> permissions = pm.getAccessSchedule(null, Test.DATA_ACCOUNT_USER_ID);
		assertTrue(permissions != null);
		
		for (Permission permission : permissions) {
			Component component = permission.getComponent();
			assertTrue(component != null);
			
			Log.v(TAG, component.getDisplayName());
			
			JsonObject json = (JsonObject) Test.PERMISSIONS.get(component.getDisplayName());
			assertTrue(json != null);
			
			// Server response of time limit is in minutes and MEEP uses milliseconds
			JsonObject access = Test.getAccessLevel(permission.getAccessLevel().toString(), permission.getTimeLimit() / 60000);
			assertTrue(access.equals(json));
		}
	}
	
	public void testGetAccessScheduleForUser() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				List<Permission> permissions = pm.getAccessScheduleBlocking(null, Test.DATA_ACCOUNT_USER_ID);
				
				if (permissions == null || permissions.size() != NUMBER_OF_PERMISSION_SETTINGS) {
					completeTest(false);
				}
				
				Component comp = permissions.get(5).getComponent();
				Log.v(TAG, "Name of the component: " + comp.getName());
				
				completeTest(comp != null && comp.getName() != null);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testSetAccessScheduleForUser() {
		// Setup build environment to test against production
		Build.environment = Build.Environment.PRODUCTION;
		
		final AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

			@Override
			public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
				final String accountId = account.getId();
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.execute(new Runnable() {

					@Override
					public void run() {
						PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
						List<Permission> permissions = pm.getAccessScheduleBlocking(null, accountId);
						
						if (permissions == null) {
							completeTest(false);
							return;
						}
						
						for (Permission permission : permissions) {
							Component component = permission.getComponent();
							if (component != null && component.getDisplayName().equals("communicator")) {
								permission.setTimeLimit(900000l);
							}
						}
						
						pm.setAccessSchedule(null, accountId, permissions);
						pm.syncAccessSchedule(accountId);
						
						sleep(5000);
						
						permissions = pm.getAccessSchedule(null, accountId);
						for (Permission permission : permissions) {
							Component component = permission.getComponent();
							if (component != null && component.getDisplayName().equals("communicator")) {
								long limit = permission.getTimeLimit();
								completeTest(limit == 900000l);
								return;
							}
						}
						
						completeTest(false);
					}
					
				});
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
		am.registerCallback(accountCallback);
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.unregisterCallback(accountCallback);
		
		assertTrue(mTestResult);
	}
	
	public void testClearRunHistoryForComponent() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				ComponentName component = ComponentName.unflattenFromString(Test.DATA_COMPONENT_COMMUNICATOR);
				pm.clearRunHistory(null, Test.DATA_ACCOUNT_USER_ID, component);
				
				sleep(50000);
				completeTest(true);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testClearRunHistoriesForUser() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				pm.clearRunHistories(null, Test.DATA_ACCOUNT_USER_ID);
				
				sleep(50000);
				completeTest(true);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testGetComponentsInCategoryBlacklist() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				List<Component> components = pm.getComponentsBlocking(Test.DATA_ACCOUNT_USER_ID, Category.CATEGORY_BLACKLIST);
				
				completeTest((components != null && components.size() > 0));
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}
	
	public void testGetMostRecentApp() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				Component component = pm.getMostRecentAppBlocking(Test.DATA_ACCOUNT_USER_ID);
				
				completeTest(component != null);
			}
			
		});
		
		waitUntilComplete();
		assertTrue(mTestResult);
	}

}
