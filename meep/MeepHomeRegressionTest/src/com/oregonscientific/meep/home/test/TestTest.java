package com.oregonscientific.meep.home.test;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.home.view.UserImageView;
import com.oregonscientific.meep.junit.test.BaseSingleLaunchActivityTestCase;

public class TestTest
	extends
	BaseSingleLaunchActivityTestCase {

	private final String TUTORIAL_PACKAGE_NAME = "com.oregonscientific.meep.home.TutorialActivity";
	@Override
	public void setUp() throws Exception {
		super.setUp();

		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());
		
	
	}

	@Override
	public void tearDown() throws Exception {
		//solo.goBackToActivity("com.oregonscientific.meep.home.HomeActivity");
		solo.tearDown();
		super.tearDown();
	}
	
	public void testA_launchProfileSetting() {
		
		final String PROFILE_ACTIVITY = "com.oregonscientific.meep.home.ProfileActivity";
		UserImageView hdv = (UserImageView) solo.findViewById(R.id.header_profile_user_image);
		int location[] = {0,0};
		hdv.getLocationOnScreen(location);
		solo.clickOnScreen(location[0], location[1]);
		solo.sleep(3000);
		ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
	     List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 
	     ComponentName componentInfo = taskInfo.get(0).topActivity;
	     assertTrue("Profile activity fail to launch, topActivity is " + componentInfo.getClassName(), 
	    		 componentInfo.getClassName().equalsIgnoreCase(PROFILE_ACTIVITY));

	     solo.goBack();
	     

	}
	
}
