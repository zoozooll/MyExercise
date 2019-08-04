package com.oregonscientific.meep.communicator.sendChatMessage;

import junit.framework.AssertionFailedError;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.communicator.ConversationMessage;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;
import com.oregonscientific.meep.communicator.view.conversation.ConversationPagerAdapter;
import com.oregonscientific.meep.communicator.view.conversation.ConversationViewPager;

public class Test2_SendBadwordChatMessage extends
		ActivityInstrumentationTestCase2<CommunicatorActivity> {

	private ExtSolo solo; // ExtSolo is an extension of Robotium Solo that helps collecting better test execution data during test runs

	public Test2_SendBadwordChatMessage() {
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

	public void test2_sendBadwordChatMessage() throws Exception {
		try {
			solo.waitForActivity("CommunicatorActivity");
			solo.sleep(15400);
			assertTrue("Wait for list failed.",
					solo.waitForView(AbsListView.class, 1, 20000, true));
			solo.clickInList(1);
			solo.sleep(10900);
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
					"damn you bitch");
			solo.sleep(1500);
			assertTrue(
					"Wait for text (id: com.oregonscientific.meep.communicator.R.id.send) failed.",
					solo.waitForView(
							solo.findViewById(com.oregonscientific.meep.communicator.R.id.send),
							20000, true));
			solo.clickOnView(solo
					.findViewById(com.oregonscientific.meep.communicator.R.id.send));
			
			boolean errorPopup = solo.waitForFragmentByTag("dialog", 10000);
			
			TextView tv = (TextView) solo.findViewById(R.id.message);
			
			if (tv != null) {
				assertFalse("Failed to send message due to: " + tv.getText(), errorPopup);
			} else {
				assertFalse("Fail to send message", errorPopup);
			}
			
			solo.sleep(1000);
			solo.goBack();
			
		} catch (AssertionFailedError e) {
			solo.fail(
					"com.oregonscientific.meep.communicator.sendChatMessage.Test1_SendNormalChatMessage.testRecorded_scr_fail",
					e);
			throw e;
		} catch (Exception e) {
			solo.fail(
					"com.oregonscientific.meep.communicator.sendChatMessage.Test1_SendNormalChatMessage.testRecorded_scr_fail",
					e);
			throw e;
		}
	}

}
