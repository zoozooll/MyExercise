package com.oregonscientific.meep.junit.test;

import java.io.IOException;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.home.HomeActivity;
import com.oregonscientific.meep.msm.MessageManager;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class BaseMultipleLaunchActivityTestCase extends
		ActivityInstrumentationTestCase2<HomeActivity> {

	public BaseMultipleLaunchActivityTestCase(Class<HomeActivity> activityClass) {
		super(activityClass);
	}


	private static MockWebSocketServer mServer;
	protected final int CONDITION_TIMEOUT = 20000;
	protected ExtSolo solo; // ExtSolo is an extension of Robotium Solo that helps collecting better test execution data during test runs
	protected static void startServer (Context context) {
		
		String TAG = "MockWebSocketServer";
		
		try {
			Log.d(TAG, "Setting up WebSocket server...");
			if (mServer == null) {
				mServer = new MockWebSocketServer();
			}
			mServer.start();
			mServer.addResponder(new SignInMessageResponder());
			restartServer(context, 1);
			
		} catch (Exception ex) {
			Log.e(TAG, "Cannot create WebSocket server because " + ex + " occurred");
			mServer = null;
		}
	}
	
	protected static void stopServer(Context context) {
		
		String TAG = "MockWebSocketServer";
		
		Log.d(TAG, "Tearing down WebSocket server...");
		try {
			if (mServer != null) {
				mServer.stop();
				mServer.removeAllResponders();
				mServer = null;
			}
			restartServer(context, 0);
		} catch (IOException e) {
			Log.e(TAG, "Fail to shut down server");
		} catch (InterruptedException e) {
			Log.e(TAG, "Fail to shut down server");
		}

	}
	
	protected static void restartServer(Context context, int environmentInt) {
		// environmentInt: 1 is Sandbox, 0 is Production
		MessageManager msm = (MessageManager) ServiceManager.getService(context, ServiceManager.MESSAGE_SERVICE);
		msm.reconnect(environmentInt);
	}
	
	protected static MockWebSocketServer getServer() {
		return mServer;
	}
	

	public static void pushMessage(String json) {
		mServer.sendToAll(json);
	}
	
}
