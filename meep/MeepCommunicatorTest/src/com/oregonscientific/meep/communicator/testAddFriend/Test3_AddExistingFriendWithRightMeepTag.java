package com.oregonscientific.meep.communicator.testAddFriend;

import junit.framework.AssertionFailedError;
import android.app.DialogFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;

public class Test3_AddExistingFriendWithRightMeepTag extends
ActivityInstrumentationTestCase2<com.oregonscientific.meep.communicator.activity.CommunicatorActivity> {

	private ExtSolo solo; // ExtSolo is an extension of Robotium Solo that helps collecting better test execution data during test runs

	public Test3_AddExistingFriendWithRightMeepTag() {
		super(CommunicatorActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass()
				.getCanonicalName(), getName());

	}

	@Override
	public void tearDown() throws Exception {
		solo.goBackToActivity("CommunicatorActivity");
		solo.tearDown();
		super.tearDown();
	}

	public void test3_AddExistingFriendWithRightMeepTag() throws Exception {
		try {
			solo.waitForActivity("CommunicatorActivity");
			solo.sleep(20500);
			assertTrue(
					"Wait for image (id: com.oregonscientific.meep.communicator.R.id.top_bar) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.top_bar),
							20000, true));
			solo.clickOnView(solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.top_bar));
			solo.sleep(14100);
			assertTrue(
					"Wait for text (id: com.oregonscientific.meep.communicator.R.id.add_friends) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.add_friends),
							20000, true));
			solo.clickOnView(solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.add_friends));

			assertTrue(
					"Wait for Search friend Fragment failed.",
					solo.waitForFragmentByTag("dialog", 20000));

			assertTrue(
					"Wait for edit text (id: com.oregonscientific.meep.communicator.R.id.text) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.text),
							20000, true));
			solo.clearEditText((EditText) solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.text));
			solo.enterText(
					(EditText) solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.text),
					"andy#51");
			solo.sleep(2000);
			assertTrue(
					"Wait for text (id: com.oregonscientific.meep.communicator.R.id.add) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.add),
							20000, true));
			solo.clickOnView(solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.add));


			assertTrue(
					"Wait for Add friend Fragment failed.",
					solo.waitForFragmentByTag("dialog", 20000));


			assertTrue(
					"Wait for edit text (id: com.oregonscientific.meep.communicator.R.id.text) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.text),
							20000, true));
			solo.clearEditText((EditText) solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.text));
			solo.enterText(
					(EditText) solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.text),
					"hi andy");
			solo.sleep(900);
			assertTrue(
					"Wait for text (id: com.oregonscientific.meep.communicator.R.id.add) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.add),
							20000, true));
			solo.clickOnView(solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.add));

			solo.sleep(2000);
	
			assertFalse("Pop up fragment appear to avoid adding the existing friend", 
					solo.waitForFragmentByTag("dialog", 10000)
			);

		} catch (AssertionFailedError e) {
			solo.fail(
					"com.oregonscientific.meep.communicator.test.CommunicatorActivityTest.testRecorded_scr_fail",
					e);
			throw e;
		} catch (Exception e) {
			solo.fail(
					"com.oregonscientific.meep.communicator.test.CommunicatorActivityTest.testRecorded_scr_fail",
					e);
			throw e;
		}
	}
}
