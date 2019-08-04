/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.os.RemoteException;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.recommendation.IRecommendationServiceCallback;
import com.oregonscientific.meep.recommendation.Recommendation;
import com.oregonscientific.meep.recommendation.RecommendationManager;
import com.oregonscientific.meep.recommendation.RecommendationService;

public class RecommendationServiceTestCase extends BaseServiceTestCase<RecommendationService> {
	
	private final String TAG = RecommendationServiceTestCase.class.getSimpleName();

	private MockWebSocketServer mServer;
	private Runnable mTask = null;
	private IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

		@Override
		public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
			if (success) {
				if (mTask != null) {
					mTask.run();
				}
			} else {
				if (AccountManager.ERROR_AUTH_TOKEN_EXPIRED.equals(errorMessage)) {
					AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
					am.signIn();
				} else {
					completeTest(false);
				}
			}
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
	
	public RecommendationServiceTestCase(Class<RecommendationService> serviceClass) {
		super(serviceClass);
	}
	
	public RecommendationServiceTestCase() {
		super(RecommendationService.class);
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
		// Clean up test environment
		mTask = null;
		
		// Tear down WebSocket server
		Log.d(TAG, "Tearing down WebSocket server...");
		if (mServer != null) {
			mServer.stop();
			mServer.removeAllResponders();
			mServer = null;
		}
		
		super.tearDown();
	}
	
	protected Recommendation getWebRecommendation(String type) {
		Recommendation recommendation = new Recommendation();
		recommendation.setType(type);
		recommendation.setUrl("http://www.google.com");
		recommendation.setThumbnail("https://static.oregonscientific.com/meepuser/thumbnails/");
		return recommendation;
	}
	
	protected Recommendation getYouTubeRecommendation(String type) {
		Recommendation recommendation = new Recommendation();
		recommendation.setType(type);
		recommendation.setUrl("http://youtu.be/96URE_-aj-8");
		recommendation.setThumbnail("https://static.oregonscientific.com/meepuser/thumbnails/");
		return recommendation;
	}
	
	private void runGetRecommendationCommandWithMockServer(final String type) {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				List<Recommendation> listOfRecommendations = rm.getRecommendations(Test.DATA_ACCOUNT_USER_ID, type);
				
				completeTest(recommendations != null && recommendations.equals(listOfRecommendations));
			}
		};
		
		// Setup build environment to test against MockWebSocketServer
		Build.environment = Build.Environment.SANDBOX;
		
		// Setup mock server response
		if (mServer != null) {
			mServer.addResponder(MessageResponders.SIGN_IN);
			mServer.addResponder(MessageResponders.GET_RECOMMENDATIONS);
		}
		
		// Issue calls to the method in test. After sign-in a call to synchronize recommendations will be issued
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(type, recommendationCallback);
		
		mTask = new Runnable() {

			@Override
			public void run() {
				if (mServer != null) {
					JsonObject json = new JsonObject();
					json.addProperty("received", true);
					json.addProperty("proc", Message.PROCESS_SYSTEM);
					json.addProperty("opcode", Message.OPERATION_CODE_RUN_COMMAND);
					json.addProperty("messageid", UUID.randomUUID().toString());
					json.addProperty("command", Message.OPERATION_CODE_GET_RECOMMENDATIONS);
					json.addProperty("message", "");
					json.addProperty("parameter", type);
					
					Gson gson = new GsonBuilder().create();
					mServer.sendToAll(gson.toJson(json));
				}
			}
			
		};
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(type, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testIsWebUrlRecommended() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.isUrlRecommendedBlocking(Test.DATA_ACCOUNT_USER_ID, Test.DATA_RECOMMENDED_WEB_URL);
				completeTest(result);
				
				result = rm.isUrlRecommended(Test.DATA_ACCOUNT_USER_ID, Test.DATA_NOT_RECOMMENDED_WEB_URL);
				completeTest(!result);
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testIsYouTubeUrlRecommended() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.isUrlRecommendedBlocking(Test.DATA_ACCOUNT_USER_ID, Test.DATA_RECOMMENDED_YOUTUBE_URL);
				completeTest(result);
				
				result = rm.isUrlRecommendedBlocking(Test.DATA_ACCOUNT_USER_ID, Test.DATA_NOT_RECOMMENDED_YOUTUBE_URL);
				completeTest(!result);
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testRunGetYouTubeFromParentRecommendationCommandWithMockServer() {
		runGetRecommendationCommandWithMockServer(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
	}
	
	@SmallTest
	public void testRunGetYouTubeFromAdminRecommendationCommandWithMockServer() {
		runGetRecommendationCommandWithMockServer(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN);
	}
	
	@MediumTest
	public void testRunGetWebFromParentRecommendationCommandWithMockServer() {
		runGetRecommendationCommandWithMockServer(RecommendationManager.TYPE_WEB_FROM_PARENT);
	}
	
	@SmallTest
	public void testRunGetWebFromAdminRecommendationCommandWithMockServer() {
		runGetRecommendationCommandWithMockServer(RecommendationManager.TYPE_WEB_FROM_ADMIN);
	}
	
	@SmallTest
	public void testSyncParentWebRecommendationsWithProductionServer() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				completeTest(recommendations != null);
			}
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test. After sign-in a call to synchronize recommendations will be issued
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testSyncParentYouTubeRecommendationsWithProductionServer() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				completeTest(recommendations != null);
			}
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test. After sign-in a call to synchronize recommendations will be issued
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testSyncAdminYouTubeRecommendationsWithProductionServer() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				completeTest((recommendations != null && recommendations.size() > 0));
			}
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test. After sign-in a call to synchronize recommendations will be issued
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testSyncAdminWebRecommendationsWithProductionServer() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				completeTest((recommendations != null && recommendations.size() > 0));
			}
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test. After sign-in a call to synchronize recommendations will be issued
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testRemoveParentWebRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				Recommendation r = getWebRecommendation(RecommendationManager.TYPE_WEB_FROM_PARENT);
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.addRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				
				// Fail the test if the {@link Recommendation} cannot be added
				if (!result) {
					completeTest(result);
					return;
				}
				
				result = rm.removeRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				if (!result) {
					completeTest(result);
					return;
				}
				
				List<Recommendation> rs = rm.getRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID, RecommendationManager.TYPE_WEB_FROM_PARENT);
				// Fail the test if {@code recommendations} is {@code null}
				if (rs == null || rs.size() == 0) {
					completeTest(true);
				} else {
					for (Recommendation recommendation : rs) {
						if (r.equals(recommendation)) {
							completeTest(false);
							return;
						}
					}
					completeTest(true);
				}
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testRemoveParentYouTubeRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				Recommendation r = getYouTubeRecommendation(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.addRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				
				// Fail the test if the {@link Recommendation} cannot be added
				if (!result) {
					completeTest(result);
					return;
				}
				
				result = rm.removeRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				if (!result) {
					completeTest(result);
					return;
				}
				
				List<Recommendation> rs = rm.getRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID, RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
				// Fail the test if {@code recommendations} is {@code null}
				if (rs == null || rs.size() == 0) {
					completeTest(true);
				} else {
					for (Recommendation recommendation : rs) {
						if (r.equals(recommendation)) {
							completeTest(false);
							return;
						}
					}
					completeTest(true);
				}
			}
		};

		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testAddParentYouTubeRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				Recommendation r = getYouTubeRecommendation(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.addRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				
				// Fail the test if the {@link Recommendation} cannot be added
				if (!result) {
					completeTest(result);
					return;
				}
				
				List<Recommendation> rs = rm.getRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID, RecommendationManager.TYPE_YOUTUBE_FROM_PARENT);
				// Fail the test if {@code recommendations} is {@code null}
				if (rs == null || rs.size() == 0) {
					completeTest(false);
					return;
				}
				
				
				for (Recommendation recommendation : rs) {
					if (r.equals(recommendation)) {
						completeTest(true);
						return;
					}
				}
				completeTest(false);
			}
		};

		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testAddParentWebRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				Recommendation r = getWebRecommendation(RecommendationManager.TYPE_WEB_FROM_PARENT);
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.addRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				
				// Fail the test if the {@link Recommendation} cannot be added
				if (!result) {
					completeTest(result);
					return;
				}
				
				List<Recommendation> rs = rm.getRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID, RecommendationManager.TYPE_WEB_FROM_PARENT);
				// Fail the test if {@code recommendations} is {@code null}
				if (rs == null || rs.size() == 0) {
					completeTest(false);
					return;
				}
				
				
				for (Recommendation recommendation : rs) {
					if (r.equals(recommendation)) {
						completeTest(true);
						return;
					}
				}
				completeTest(false);
			}
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testAddAdminWebRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				Recommendation r = getWebRecommendation(RecommendationManager.TYPE_WEB_FROM_ADMIN);
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.addRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				
				// Adding a recommendation from administrator should fail
				completeTest(!result);
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testAddAdminYouTubeRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				Recommendation r = getYouTubeRecommendation(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN);
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				boolean result = rm.addRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, r);
				
				// Adding a recommendation from administrator should fail
				completeTest(!result);
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testRemoveAdminWebRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				List<Recommendation> rs = rm.getRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID, RecommendationManager.TYPE_WEB_FROM_ADMIN);
				// Fail the test if {@code recommendations} is {@code null}
				if (rs == null || rs.size() == 0) {
					completeTest(false);
					return;
				}
				
				boolean result = rm.removeRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, rs.get(0));
				// Removing a recommendation from administrator should fail
				completeTest(!result);
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testRemoveAdminYouTubeRecommendation() {
		// Recommendation callbacks
		final IRecommendationServiceCallback recommendationCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
				List<Recommendation> rs = rm.getRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID, RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN);
				// Fail the test if {@code recommendations} is {@code null}
				if (rs == null || rs.size() == 0) {
					completeTest(false);
					return;
				}
				
				boolean result = rm.removeRecommendationBlocking(Test.DATA_ACCOUNT_USER_ID, rs.get(0));
				// Removing a recommendation from administrator should fail
				completeTest(!result);
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, recommendationCallback);
		
		assertTrue(mTestResult);
	}
	
	@SmallTest
	public void testGetAllRecommendations() {
		final Map<String, Integer> callbackResults = new HashMap<String, Integer>();
		final Runnable test = new Runnable() {

			@Override
			public void run() {
				if (callbackResults.containsKey(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN) 
						&& callbackResults.containsKey(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT)
						&& callbackResults.containsKey(RecommendationManager.TYPE_WEB_FROM_ADMIN)
						&& callbackResults.containsKey(RecommendationManager.TYPE_WEB_FROM_PARENT)) {
					
					Collection<Integer> counts = callbackResults.values();
					int totalNumberOfRecommendations = 0;
					for (Integer val : counts) {
						totalNumberOfRecommendations += val;
					}
					
					RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
					List<Recommendation> rs = rm.getAllRecommendationsBlocking(Test.DATA_ACCOUNT_USER_ID);
					completeTest((rs != null && rs.size() == totalNumberOfRecommendations));
				}
			}
			
		};
		
		// Recommendation callbacks
		final IRecommendationServiceCallback youtubeFromAdminCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				int count = recommendations == null ? 0 : recommendations.size();
				callbackResults.put(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, Integer.valueOf(count));
				test.run();
			}
			
		};
		final IRecommendationServiceCallback youtubeFromParentCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				int count = recommendations == null ? 0 : recommendations.size();
				callbackResults.put(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, Integer.valueOf(count));
				test.run();
			}
			
		};
		final IRecommendationServiceCallback webFromAdminCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				int count = recommendations == null ? 0 : recommendations.size();
				callbackResults.put(RecommendationManager.TYPE_WEB_FROM_ADMIN, Integer.valueOf(count));
				test.run();
			}
			
		};
		final IRecommendationServiceCallback webFromParentCallback = new IRecommendationServiceCallback.Stub() {
			
			@Override
			public void onReceiveRecommendation(List<Recommendation> recommendations) throws RemoteException {
				int count = recommendations == null ? 0 : recommendations.size();
				callbackResults.put(RecommendationManager.TYPE_WEB_FROM_PARENT, Integer.valueOf(count));
				test.run();
			}
			
		};
		
		// Setup build environment to test against Production WebSocketServer
		Build.environment = Build.Environment.PRODUCTION;
		
		// Issue calls to the method in test.
		RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, youtubeFromAdminCallback);
		rm.registerCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, youtubeFromParentCallback);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, webFromAdminCallback);
		rm.registerCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, webFromParentCallback);
		
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		am.registerCallback(accountCallback);
		am.signOut();
		am.signIn();
		
		// Tear down configurations
		waitUntilComplete();
		am.signOut();
		am.unregisterCallback(accountCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, youtubeFromAdminCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, youtubeFromParentCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_ADMIN, webFromAdminCallback);
		rm.unregisterCallback(RecommendationManager.TYPE_WEB_FROM_PARENT, webFromParentCallback);
		
		assertTrue(mTestResult);
	}
	
}
