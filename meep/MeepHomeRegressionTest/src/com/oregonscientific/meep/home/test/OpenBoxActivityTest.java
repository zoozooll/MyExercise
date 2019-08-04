package com.oregonscientific.meep.home.test;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.test.ActivityInstrumentationTestCase2;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.home.HomeActivity;
import com.oregonscientific.meep.junit.test.TestData;
import com.oregonscientific.meep.util.SystemUtils;

public class OpenBoxActivityTest extends ActivityInstrumentationTestCase2<HomeActivity> {

	public OpenBoxActivityTest(Class<HomeActivity> activityClass) {
		super(activityClass);
	}
	
	public OpenBoxActivityTest() {
		super(HomeActivity.class);
	}

	private final String OPENBOX_PACKAGE = "com.oregonscientific.meep.meepopenbox";
	
	private ExtSolo solo;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());		
	}
	
	@Override
	protected void tearDown() throws Exception {
		solo.tearDown();
		super.tearDown();
	}
	
	
	
	public void test1_launchOpenBox()  throws Exception {
		
		solo.waitForActivity("HomeActivity");
		
		solo.sleep(10000);
		
		ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);

	     List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 

	     ComponentName componentInfo = taskInfo.get(0).topActivity;
	     
	     if (!SystemUtils.isSystemConfigured(getActivity())) { 
	    	 assertTrue("MeepOpenBox fail to launch", componentInfo.getPackageName().equalsIgnoreCase(OPENBOX_PACKAGE));
	    	 	    	 
	     } else {
	    	 assertTrue("MeepOpenBox launched when system configured", componentInfo.getPackageName().equalsIgnoreCase(TestData.PACKAGE_NAME));
	     }		 
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				OpenBoxActivityTest.class);
		return suite;
	}
}
