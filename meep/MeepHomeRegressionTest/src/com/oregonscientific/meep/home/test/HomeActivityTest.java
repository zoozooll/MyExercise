package com.oregonscientific.meep.home.test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.home.view.ContentLayout;
import com.oregonscientific.meep.home.view.FadingScrollView;
import com.oregonscientific.meep.home.view.MenuItem;
import com.oregonscientific.meep.home.view.MyTextView;
import com.oregonscientific.meep.home.view.UserImageView;
import com.oregonscientific.meep.junit.test.BaseSingleLaunchActivityTestCase;
import com.oregonscientific.meep.junit.test.TestData;
import com.oregonscientific.meep.permission.PermissionManager;

public class HomeActivityTest extends
		BaseSingleLaunchActivityTestCase {

	private ExtSolo solo; // ExtSolo is an extension of Robotium Solo that helps collecting better test execution data during test runs
	private boolean requireMenuToRestore = false;
	private boolean requireToExitTopActivity = false;
	private boolean requireToExitParentalSetting = false;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());
	}

	@Override
	public void tearDown() throws Exception {
		
		if (requireToExitParentalSetting) {
			// self exit
			solo.sleep(1000);
			solo.goBack();
			solo.sleep(1000);
			solo.clickOnScreen(solo.toScreenX(0.572f), solo.toScreenY(0.680f));
			solo.sleep(1000);
			requireToExitParentalSetting = false;
		}
		
		
		if (requireToExitTopActivity) {
			solo.sleep(1000);
			solo.goBack();
			solo.sleep(1000);
			requireToExitTopActivity = false;
		}
		
		if (requireMenuToRestore) {
			dragDownScrollView(true);
			dragDownScrollView(false);
			dragDownLeftMenu();
			dragDownRightMenu();
			requireMenuToRestore = false;
		}
		
		solo.tearDown();
		super.tearDown();
	}

	
	public void test0_startServer () {
		startServer(getActivity());
	}
	public void testZ_stopServer() {
		stopServer(getActivity());
	}

	public void test1_AutoSignIn() {
		assertTrue("Fail to launch Home Activity", solo.waitForActivity("HomeActivity", 3000));
		solo.sleep(5000);
		MyTextView tv = (MyTextView)solo.findViewById(R.id.header_profile_user_name);
		assertTrue("Fail to login or login wrongly", tv.getText().toString().equalsIgnoreCase(TestData.TEST_USER_NAME));
		
	}

	public void test2_openMenuWithClick() {

		clickLeftMenu(false);
		clickRightMenu(false);
		solo.sleep(3000);
	}
	
	public void test3_closeMenuWithClick() {
		clickLeftMenu(true);
		clickRightMenu(true);
		solo.sleep(3000);

	}
	
	public void test4_openMenuWithDrag() {
		dragUpLeftMenu();
		dragUpRightMenu();
	}
	
	public void test5_closeMenuWithDrag() {
		dragDownLeftMenu();
		dragDownRightMenu();
	}
	
	public void test6_dragUpAndDownMenusQuickly() {

		requireMenuToRestore = true;
		
		Date startDate = Calendar.getInstance().getTime();
		Date endDate = new Date(startDate.getTime() + 10000);
		Date iteratingDate = startDate;
		while (iteratingDate.before(endDate)) {
			boolean left = (Math.round(Math.random()*100) %2 == 1);
			if (left)
				dragLeftMenuQuickly();
			else
				dragRightMenuQuickly();
			
			iteratingDate = Calendar.getInstance().getTime();
		}

	}
	
	public void test7_dragUpAndDownScrollViewInMenuQuickly() {
		
		requireMenuToRestore = true;
		
		clickLeftMenu(false);
		clickRightMenu(false);
	
		Date startDate = Calendar.getInstance().getTime();
		Date endDate = new Date(startDate.getTime() + 10000);
		Date iteratingDate = startDate;
		while (iteratingDate.before(endDate)) {
			boolean left = (Math.round(Math.random()*100) %2 == 1);
			if (left)
				dragLeftScrollViewQuickly();
			else
				dragRightScrollViewQuickly();
			
			iteratingDate = Calendar.getInstance().getTime();
		}
		

	}
	

	public void test8_recentlyUsedAppWithCorrectName() {
		
		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpLeftMenu();
		launchAppWithResourceId(R.id.menu_item_notifications);
		solo.sleep(9000);
		solo.goBack();
		solo.sleep(1000);
		TextView tv = (TextView) solo.findViewById(R.id.menu_item_header_recently_used_title);
		assertTrue("Recently used app not Notifications, is " + tv.getText().toString(), tv.getText().toString().contains("Notification"));
		solo.sleep(3000);
	}
	
	public void test9_launchRecentlyUsedApp() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		final String NOTIFICATION_PACKAGE = getActivity().getString(R.string.package_meep_notification);
		ImageView iv = (ImageView) solo.findViewById(R.id.menu_item_header_recently_used_image);
		int location[] = {0,0};
		iv.getLocationOnScreen(location);
		solo.clickOnScreen(location[0], location[1]);
		solo.sleep(3000);
		ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
	     List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 
	     ComponentName componentInfo = taskInfo.get(0).topActivity;
	     assertTrue("Notification fail to launch, topActivity is " + componentInfo.getPackageName(), 
	    		 componentInfo.getPackageName().equalsIgnoreCase(NOTIFICATION_PACKAGE));

	}
	
	public void testA_launchProfileSetting() {
		
		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
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

	}
	
	public void testB_launchNews() {
		
		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpLeftMenu();
		final String NOTIFICATION_PACKAGE = getActivity().getString(R.string.package_meep_notification);
		launchAppWithResourceId(R.id.menu_item_news, NOTIFICATION_PACKAGE);
	}
	
	public void testC_launchBrowser() {
		
		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpLeftMenu();
		final String BROWSER_PACKAGE = getActivity().getString(R.string.package_meep_browser);
		launchAppWithResourceId(R.id.menu_item_browser, BROWSER_PACKAGE);
	}
	
	public void testD_launchCommunicator() {
		
		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpLeftMenu();
		final String COMMUNICATOR_PACKAGE = getActivity().getString(R.string.package_meep_communicator);
		launchAppWithResourceId(R.id.menu_item_communicator, COMMUNICATOR_PACKAGE);

	}
	
	public void testE_launchStore() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpLeftMenu();
		final String STORE_PACKAGE = getActivity().getString(R.string.package_meep_store);
		launchAppWithResourceId(R.id.menu_item_store, STORE_PACKAGE);
	}
	
	public void testF_launchParentalSetting() {
		
		requireMenuToRestore = true;
		requireToExitParentalSetting = true;
		dragUpLeftMenu();
		dragUpLeftScrollView();
		final String TOGETHER_PACKAGE = getActivity().getString(R.string.package_meep_portal);
		launchAppWithResourceId(R.id.menu_item_portal, TOGETHER_PACKAGE);
		solo.sleep(1000);

	}
	
	public void testG_launchYouTube() {
		
		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpLeftMenu();
		dragUpLeftScrollView();
		final String YOUTUBE_PACKAGE = getActivity().getString(R.string.package_meep_youtube);
		launchAppWithResourceId(R.id.menu_item_youtube, YOUTUBE_PACKAGE);
		solo.sleep(1000);
		// close the keyboard first
		solo.goBack();
	}
	
	
	
	public void testH_launchMusic() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		final String MUSIC_PACKAGE = getActivity().getString(R.string.package_meep_music);
		launchAppWithResourceId(R.id.menu_item_music, MUSIC_PACKAGE);

	}
	
	public void testI_launchGame() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		final String GAME_PACKAGE = getActivity().getString(R.string.package_meep_game);
		launchAppWithResourceId(R.id.menu_item_game, GAME_PACKAGE);

	}
	
	public void testJ_launchPicture() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		final String PHOTO_PACKAGE = getActivity().getString(R.string.package_meep_photo);
		launchAppWithResourceId(R.id.menu_item_picture, PHOTO_PACKAGE);
		solo.sleep(1000);
	}
	
	public void testK_launchEbooks() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		final String EBOOK_PACKAGE = getActivity().getString(R.string.package_meep_ebook);
		launchAppWithResourceId(R.id.menu_item_ebook, EBOOK_PACKAGE);
		solo.sleep(1000);
	}
	
	public void testL_launchVideos() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		final String MOVIE_PACKAGE = getActivity().getString(R.string.package_meep_movie);
		launchAppWithResourceId(R.id.menu_item_movie, MOVIE_PACKAGE);
		solo.sleep(1000);
	}
	
	public void testM_launchSafty() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		dragUpRightScrollView();
		final String SAFTY_PACKAGE = getActivity().getString(R.string.package_meep_safty);
		launchAppWithResourceId(R.id.menu_item_help, SAFTY_PACKAGE);
		solo.sleep(1000);
	}
	
	
	public void testN_launchCamera() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		dragUpRightScrollView();

		final String CAMERA_PACKAGE = getActivity().getString(R.string.package_meep_camera);
		
		MenuItem mi = (MenuItem) solo.findViewById(R.id.menu_item_camera);
		if (mi != null) {
			Log.d("HomeActivityTest", "Launching" + mi.getName());
			int location[] = {0,0};
			mi.getLocationOnScreen(location);
			Log.e("HomeActivityTest", location[0] + ":" + location[1]);
			solo.clickOnScreen(location[0], location[1]);
		} 
		
		solo.sleep(5000);
		
		solo.clickOnScreen(solo.toScreenX(0.361f), solo.toScreenY(0.493f));
		solo.sleep(1000);
		solo.clickOnScreen(solo.toScreenX(0.356f), solo.toScreenY(0.543f));
		solo.sleep(2800);
		
		AccountManager accm = (AccountManager) ServiceManager.getService(getActivity(), ServiceManager.ACCOUNT_SERVICE);
		Account a = accm.getLoggedInAccount();
		PermissionManager pm = (PermissionManager) ServiceManager.getService(getActivity(), ServiceManager.PERMISSION_SERVICE);
		ComponentName  componentName = null;
		if (!CAMERA_PACKAGE.equals(getActivity().getString(R.string.package_meep_camera))) {
			Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(CAMERA_PACKAGE);
			componentName = launchIntent.getComponent();
		} else {
			componentName = new ComponentName("com.android.camera", "com.android.camera.CameraEntry");
		}
	
		if (pm.isAccessible(a.getMeepTag(), componentName)) {
		
			if (CAMERA_PACKAGE != null) {

				ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
				List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 
				ComponentName componentInfo = taskInfo.get(0).topActivity;
				assertTrue("Activity fail to launch, topActivity is " + componentInfo.getPackageName(), 
						componentInfo.getPackageName().equalsIgnoreCase(CAMERA_PACKAGE));
			}
			
		} else {
			assertTrue("Blocked pop up did not came up", solo.waitForFragmentByTag("dialog", 20000));
		}
		
		
		solo.sleep(1000);
	}
	
	public void testO_launchSetting() {

		requireMenuToRestore = true;
		requireToExitTopActivity = true;
		
		dragUpRightMenu();
		dragUpRightScrollView();

		final String SETTING_PACKAGE = getActivity().getString(R.string.package_meep_settins);
		launchAppWithResourceId(R.id.menu_item_setting, SETTING_PACKAGE);
		solo.sleep(1000);
	}
	
	private void dragLeftMenuQuickly() {
		
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);	
		int location[] = {0,0};
		leftMenu.getLocationOnScreen(location);
		int leftMenuOriginalTopPosition = location[1];
		
		boolean up = (leftMenuOriginalTopPosition > 200);
		
		if (up) {
			solo.sleep(300);
			solo.drag(solo.toScreenX(0.172f), solo.toScreenX(0.190f),
					solo.toScreenY(0.874f), solo.toScreenY(0.124f), 8);
			solo.sleep(300);
		} else {
			solo.sleep(300);
			solo.drag(solo.toScreenX(0.172f), solo.toScreenX(0.190f),
					solo.toScreenY(0.124f), solo.toScreenY(0.874f), 8);
			solo.sleep(300);
		}
		
		int afterlocation[] = {0,0};
		leftMenu.getLocationOnScreen(afterlocation);
		if (up)
			assertTrue("Left Menu not opened", afterlocation[1]  < leftMenuOriginalTopPosition);
		else
			assertTrue("Left Menu still opened", afterlocation[1]  > leftMenuOriginalTopPosition);
	}
	
	private void dragRightMenuQuickly() {
		
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		int location[] = {0,0};
		rightMenu.getLocationOnScreen(location);
		int rightMenuOriginalTopPosition = location[1];
		
		boolean up = (rightMenuOriginalTopPosition > 200);
		
		if (up) {
			solo.sleep(300);
			solo.drag(solo.toScreenX(0.764f), solo.toScreenX(0.800f),
					solo.toScreenY(0.874f), solo.toScreenY(0.124f), 8);
			solo.sleep(300);
		} else {
			solo.sleep(300);
			solo.drag(solo.toScreenX(0.764f), solo.toScreenX(0.800f),
					solo.toScreenY(0.124f), solo.toScreenY(0.874f), 8);
			solo.sleep(300);
		}
		
		int afterlocation[] = {0,0};
		rightMenu.getLocationOnScreen(afterlocation);
		if (up)
			assertTrue("Right Menu not opened", afterlocation[1]  < rightMenuOriginalTopPosition);
		else
			assertTrue("Right Menu still opened", afterlocation[1]  > rightMenuOriginalTopPosition);
	}
	
	private void dragUpLeftMenu() {
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);
		int location[] = {0,0};
		leftMenu.getLocationOnScreen(location);
		int leftMenuOriginalTopPosition = location[1];

		solo.sleep(5700);

		solo.drag(solo.toScreenX(0.172f), solo.toScreenX(0.190f),
		solo.toScreenY(0.874f), solo.toScreenY(0.124f), 10);
		solo.sleep(1000);

		int afterlocation[] = {0,0};
		leftMenu.getLocationOnScreen(afterlocation);

		assertTrue("Left Menu not opened", afterlocation[1]  <= leftMenuOriginalTopPosition);
	}
	
	private void dragDownLeftMenu() {
		
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);		
		int location[] = {0,0};
		leftMenu.getLocationOnScreen(location);
		int leftMenuOriginalTopPosition = location[1];
		
		solo.sleep(5700);
		solo.drag(solo.toScreenX(0.172f), solo.toScreenX(0.190f),
				solo.toScreenY(0.124f), solo.toScreenY(0.874f), 10);
		
		solo.sleep(1000);
		int afterlocation[] = {0,0};
		leftMenu.getLocationOnScreen(afterlocation);

		assertTrue("Left Menu still opened", afterlocation[1]  >= leftMenuOriginalTopPosition);
	}
	
	
	
	private void dragUpRightMenu() {
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		int location[] = {0,0};
		rightMenu.getLocationOnScreen(location);
		int rightMenuOriginalTopPosition = location[1];

		solo.sleep(5000);
		solo.drag(solo.toScreenX(0.764f), solo.toScreenX(0.800f),
				solo.toScreenY(0.874f), solo.toScreenY(0.124f), 10);
		solo.sleep(1000);
		
		int afterlocation[] = {0,0};
		rightMenu.getLocationOnScreen(afterlocation);

		assertTrue("Right Menu not opened", afterlocation[1]  <= rightMenuOriginalTopPosition);
	}
	
	private void dragDownRightMenu() {
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		int location[] = {0,0};
		rightMenu.getLocationOnScreen(location);
		int rightMenuOriginalTopPosition = location[1];
		
		solo.sleep(5000);
		solo.drag(solo.toScreenX(0.764f), solo.toScreenX(0.800f),
				solo.toScreenY(0.124f), solo.toScreenY(0.874f), 10);
		solo.sleep(1000);

		int afterlocation[] = {0,0};
		rightMenu.getLocationOnScreen(afterlocation);

		assertTrue("Right Menu still opened"  + afterlocation[1] + " orig:" + rightMenuOriginalTopPosition, afterlocation[1] >= rightMenuOriginalTopPosition);
	}
	
	
	private void clickLeftMenu(boolean opened) {
		
		solo.sleep(1000);
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);
		int location[] = {0,0};
		leftMenu.getLocationOnScreen(location);
		int leftMenuOriginalTopPosition = location[1];

		solo.clickOnView(solo.findViewById(com.oregonscientific.meep.home.R.id.header_profile_user_tag));
		solo.sleep(2700);
		int afterlocation[] = {0,0};
		leftMenu.getLocationOnScreen(afterlocation);
		

		if (!opened) {
			assertTrue("Left Menu not opened" + afterlocation[1] + " orig:" + leftMenuOriginalTopPosition, afterlocation[1] < leftMenuOriginalTopPosition);
		} else {
			assertTrue("Left Menu still opened"  + afterlocation[1] + " orig:" + leftMenuOriginalTopPosition, afterlocation[1] > leftMenuOriginalTopPosition);
		}
		
		solo.sleep(2000);
	}
	
	private void clickRightMenu(boolean opened) {
		solo.sleep(1000);
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		int location[] = {0,0};
		rightMenu.getLocationOnScreen(location);
		int rightMenuOriginalTopPosition = location[1];
		assertTrue(
				"Wait for text (id: com.oregonscientific.meep.home.R.id.menu_item_header_recently_used_title) failed.",
				solo.waitForView(
						solo.findViewById(com.oregonscientific.meep.home.R.id.menu_item_header_recently_used_title),
						20000, true));
		solo.clickOnView(solo
				.findViewById(com.oregonscientific.meep.home.R.id.menu_item_header_recently_used_title));
		solo.sleep(5500);
		
		int afterlocation[] = {0,0};
		rightMenu.getLocationOnScreen(afterlocation);
		
		Log.e("HomeActivityTest", afterlocation[1] + " orig:" + rightMenuOriginalTopPosition);
		if (!opened) {
			assertTrue("Left Menu not opened" + afterlocation[1] + " orig:" + rightMenuOriginalTopPosition, afterlocation[1] < rightMenuOriginalTopPosition);
		} else {
			assertTrue("Left Menu still opened"  + afterlocation[1] + " orig:" + rightMenuOriginalTopPosition, afterlocation[1] > rightMenuOriginalTopPosition);
		}
	}
	
	
	
	private void dragUpScrollView(boolean left){
		if (left) {
			solo.sleep(100);
			solo.drag(solo.toScreenX(0.226f), solo.toScreenX(0.222f),
					solo.toScreenY(0.885f), solo.toScreenY(0.327f), 8);
			solo.sleep(200);
		} else {
			solo.sleep(100);
			solo.drag(solo.toScreenX(0.783f), solo.toScreenX(0.788f),
					solo.toScreenY(0.917f), solo.toScreenY(0.315f), 5);
			solo.sleep(200);
		}
	}
	
	private void dragDownScrollView(boolean left){
		if (left) {
			solo.sleep(100);
			solo.drag(solo.toScreenX(0.233f), solo.toScreenX(0.216f),
					solo.toScreenY(0.320f), solo.toScreenY(0.875f), 5);
			solo.sleep(200);
		} else {
			solo.sleep(100);
			solo.drag(solo.toScreenX(0.783f), solo.toScreenX(0.779f),
					solo.toScreenY(0.365f), solo.toScreenY(1.014f), 8);
			solo.sleep(200);
		}
	}
	
	
	public void dragUpLeftScrollView () {
		
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);
		FadingScrollView sv = (FadingScrollView) leftMenu.findViewById(R.id.menu_item_scrollview);
		int originalOffset = sv.getScrollY();
		
		dragUpScrollView(true);
		
		int afterOffset = sv.getScrollY();
		assertTrue("Scroll view did now scroll down", afterOffset > originalOffset);
	}

	
	public void dragDownLeftScrollView () {
		
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);
		FadingScrollView sv = (FadingScrollView) leftMenu.findViewById(R.id.menu_item_scrollview);
		int originalOffset = sv.getScrollY();
		
		dragDownScrollView(true);
		
		int afterOffset = sv.getScrollY();
		assertTrue("Scroll view did not scroll up", afterOffset < originalOffset);
	}
	
	public void dragUpRightScrollView () {
		
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		FadingScrollView sv = (FadingScrollView) rightMenu.findViewById(R.id.menu_item_scrollview);
		int originalOffset = sv.getScrollY();
			
		dragUpScrollView(false);
		
		int afterOffset = sv.getScrollY();
		assertTrue("Scroll view did not wscroll down", afterOffset > originalOffset);
	}
	
	public void dragDownRightScrollView () {
		
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		FadingScrollView sv = (FadingScrollView) rightMenu.findViewById(R.id.menu_item_scrollview);
		int originalOffset = sv.getScrollY();
		
		dragDownScrollView(false);
		
		int afterOffset = sv.getScrollY();
		assertTrue("Scroll view did now scroll up", afterOffset < originalOffset);
	}
	
	
	public void dragLeftScrollViewQuickly() {
		
		ContentLayout leftMenu = (ContentLayout) solo.findViewById(R.id.leftPanelContent);
		FadingScrollView sv = (FadingScrollView) leftMenu.findViewById(R.id.menu_item_scrollview);
		int originalOffset = sv.getScrollY();
		if (originalOffset < 100)
			dragUpLeftScrollView();
		else 
			dragDownLeftScrollView();
		
	}
	
	public void dragRightScrollViewQuickly() {
		
		ContentLayout rightMenu = (ContentLayout) solo.findViewById(R.id.rightPanelContent);
		FadingScrollView sv = (FadingScrollView) rightMenu.findViewById(R.id.menu_item_scrollview);
		int originalOffset = sv.getScrollY();
		if (originalOffset < 100)
			dragUpRightScrollView();
		else 
			dragDownRightScrollView();
		
	}
	
	private void launchAppWithResourceId(int resId) {
		launchAppWithResourceId(resId, null);
	}
	
	private void launchAppWithResourceId(int resId, String packageName) {
		
		MenuItem mi = (MenuItem) solo.findViewById(resId);
		if (mi != null) {
			Log.d("HomeActivityTest", "Launching" + mi.getName());
			int location[] = {0,0};
			mi.getLocationOnScreen(location);
			Log.e("HomeActivityTest", location[0] + ":" + location[1]);
			solo.clickOnScreen(location[0], location[1]);
		} 
		
		solo.sleep(5000);
		
		if (packageName == null)
			return;
		
		AccountManager accm = (AccountManager) ServiceManager.getService(getActivity(), ServiceManager.ACCOUNT_SERVICE);
		Account a = accm.getLoggedInAccount();
		PermissionManager pm = (PermissionManager) ServiceManager.getService(getActivity(), ServiceManager.PERMISSION_SERVICE);
		ComponentName  componentName = null;
		if (!packageName.equals(getActivity().getString(R.string.package_meep_camera))) {
			Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
			componentName = launchIntent.getComponent();
		} else {
			componentName = new ComponentName("com.android.camera", "com.android.camera.CameraEntry");
		}
	
		if (pm.isAccessible(a.getMeepTag(), componentName)) {
		
			if (packageName != null) {

				ActivityManager am = (ActivityManager) getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
				List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1); 
				ComponentName componentInfo = taskInfo.get(0).topActivity;
				assertTrue("Activity fail to launch, topActivity is " + componentInfo.getPackageName(), 
						componentInfo.getPackageName().equalsIgnoreCase(packageName));
			}
			
		} else {
			assertTrue("Blocked pop up did not came up", solo.waitForFragmentByTag("dialog", 20000));
		}
	}
	
	
	public static Test suite() {
		TestSuite suite = new TestSuite(
				HomeActivityTest.class);
		return suite;
	}
}
