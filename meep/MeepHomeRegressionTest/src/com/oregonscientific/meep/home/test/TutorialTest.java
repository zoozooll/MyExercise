package com.oregonscientific.meep.home.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.content.Intent;

import com.bitbar.recorder.extensions.ExtSolo;
import com.oregonscientific.meep.home.HomeActivity;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.home.TutorialActivity;
import com.oregonscientific.meep.home.view.CircleIndicator;
import com.oregonscientific.meep.junit.test.BaseSingleLaunchActivityTestCase;

public class TutorialTest extends
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
		solo.goBack();
		solo.tearDown();
		super.tearDown();
	}
	
	
	public void test1_Tutorial_Normal_Usage() {
		solo.waitForActivity("HomeActivity", 3000);
		solo.assertCurrentActivity("Fail to detect home activity", HomeActivity.class);
		getActivity().startActivity(new Intent(getActivity(), TutorialActivity.class));
		assertTrue("Fail to launch tutorial activity", solo.waitForActivity("TutorialActivity", 5000));
		
		CircleIndicator ci = (CircleIndicator) solo.findViewById(R.id.tutorial_circle_indicator);
		assertTrue("Tutorial not at Page 1", ci.getCurrentIndex() == 0);
		solo.sleep(6400);
		solo.drag(solo.toScreenX(0.854f), solo.toScreenX(0.370f),
				solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
		solo.sleep(4200);
		assertTrue("Tutorial not at Page 2", ci.getCurrentIndex() == 1);
		solo.drag(solo.toScreenX(0.845f), solo.toScreenX(0.390f),
				solo.toScreenY(0.615f), solo.toScreenY(0.687f), 10);
		solo.sleep(2600);
		assertTrue("Tutorial not at Page 3", ci.getCurrentIndex() == 2);
		solo.drag(solo.toScreenX(0.855f), solo.toScreenX(0.401f),
				solo.toScreenY(0.597f), solo.toScreenY(0.606f), 10);
		solo.sleep(2500);
		assertTrue("Tutorial not at Page 4", ci.getCurrentIndex() == 3);
		solo.drag(solo.toScreenX(0.925f), solo.toScreenX(0.374f),
				solo.toScreenY(0.568f), solo.toScreenY(0.545f), 10);
		solo.sleep(2700);
		assertTrue("Tutorial not at Page 5", ci.getCurrentIndex() == 4);
		solo.drag(solo.toScreenX(0.884f), solo.toScreenX(0.369f),
				solo.toScreenY(0.586f), solo.toScreenY(0.619f), 11);
		solo.sleep(2600);
		assertTrue("Tutorial not at Page 6", ci.getCurrentIndex() == 5);
		solo.drag(solo.toScreenX(0.911f), solo.toScreenX(0.459f),
				solo.toScreenY(0.615f), solo.toScreenY(0.588f), 11);
		solo.sleep(1070);
		
	}
	
	public void test2_Tutorial_Swipe_RightAndLeft() {
		
		solo.waitForActivity("HomeActivity", 3000);
		solo.assertCurrentActivity("Fail to detect home activity", HomeActivity.class);
		
		getActivity().startActivity(new Intent(getActivity(), TutorialActivity.class));
		assertTrue("Fail to launch tutorial activity", solo.waitForActivity("TutorialActivity", 5000));
		
		// swipe right to last page
		CircleIndicator ci = (CircleIndicator) solo.findViewById(R.id.tutorial_circle_indicator);
		assertTrue("Tutorial not at Page 1", ci.getCurrentIndex() == 0);
		solo.sleep(6400);
		solo.drag(solo.toScreenX(0.854f), solo.toScreenX(0.370f),
				solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
		solo.sleep(4200);
		assertTrue("Tutorial not at Page 2", ci.getCurrentIndex() == 1);
		solo.drag(solo.toScreenX(0.845f), solo.toScreenX(0.390f),
				solo.toScreenY(0.615f), solo.toScreenY(0.687f), 10);
		solo.sleep(2600);
		assertTrue("Tutorial not at Page 3", ci.getCurrentIndex() == 2);
		solo.drag(solo.toScreenX(0.855f), solo.toScreenX(0.401f),
				solo.toScreenY(0.597f), solo.toScreenY(0.606f), 10);
		solo.sleep(2500);
		assertTrue("Tutorial not at Page 4", ci.getCurrentIndex() == 3);
		solo.drag(solo.toScreenX(0.925f), solo.toScreenX(0.374f),
				solo.toScreenY(0.568f), solo.toScreenY(0.545f), 10);
		solo.sleep(2700);
		assertTrue("Tutorial not at Page 5", ci.getCurrentIndex() == 4);
		solo.drag(solo.toScreenX(0.884f), solo.toScreenX(0.369f),
				solo.toScreenY(0.586f), solo.toScreenY(0.619f), 11);
		solo.sleep(2600);
		assertTrue("Tutorial not at Page 6", ci.getCurrentIndex() == 5);
		
		// swipe left back to first page
		solo.drag(solo.toScreenX(0.370f), solo.toScreenX(0.854f),
				solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
		solo.sleep(4200);
		assertTrue("Tutorial not at Page 5", ci.getCurrentIndex() == 4);
		solo.drag(solo.toScreenX(0.390f), solo.toScreenX(0.854f),
				solo.toScreenY(0.615f), solo.toScreenY(0.687f), 10);
		solo.sleep(2600);
		assertTrue("Tutorial not at Page 4", ci.getCurrentIndex() == 3);
		solo.drag(solo.toScreenX(0.401f), solo.toScreenX(0.855f),
				solo.toScreenY(0.597f), solo.toScreenY(0.606f), 10);
		solo.sleep(2500);
		assertTrue("Tutorial not at Page 3", ci.getCurrentIndex() == 2);
		solo.drag(solo.toScreenX(0.374f), solo.toScreenX(0.925f),
				solo.toScreenY(0.568f), solo.toScreenY(0.545f), 10);
		solo.sleep(2700);
		assertTrue("Tutorial not at Page 2", ci.getCurrentIndex() == 1);
		solo.drag(solo.toScreenX(0.369f), solo.toScreenX(0.884f),
				solo.toScreenY(0.586f), solo.toScreenY(0.619f), 11);
		solo.sleep(2600);
		assertTrue("Tutorial not at Page 1", ci.getCurrentIndex() == 0);
		solo.sleep(1000);

		for (int i=0; i < 6; i++) {
			solo.sleep(500);
			solo.drag(solo.toScreenX(0.854f), solo.toScreenX(0.370f),
				solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
			solo.sleep(500);
		}
		
	}
	
	public void test3_Tutorial_Swipe_RightAndLeftQuickly() {
		int currentIndex = 0;
		int assertIndex = 0;
		int swipeCount = 10;
		
		solo.waitForActivity("HomeActivity", 3000);
		solo.assertCurrentActivity("Fail to detect home activity", HomeActivity.class);
		getActivity().startActivity(new Intent(getActivity(), TutorialActivity.class));
		assertTrue("Fail to launch tutorial activity", solo.waitForActivity("TutorialActivity", 5000));
		
		// swipe right to last page
		CircleIndicator ci = (CircleIndicator) solo.findViewById(R.id.tutorial_circle_indicator);
		
		
		for (int i=0; i < swipeCount; i++) {
			
			boolean right = true;
			if (currentIndex != 0 && currentIndex != 5) { 
				right = (Math.round(Math.random() * 100) % 2) == 1;
			} else if (currentIndex == 0) {
				right = true;
			} else if (currentIndex == 5) {
				right = false;
			}
			
			if (right) {
				assertIndex = currentIndex + 1;
				solo.sleep(500);
				solo.drag(solo.toScreenX(0.854f), solo.toScreenX(0.370f),
					solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
				solo.sleep(500);
				assertTrue("Right Swipe: Tutorial not at Page " + (assertIndex +1) + " but is at Page " + (ci.getCurrentIndex()+1), ci.getCurrentIndex() == assertIndex);
				currentIndex = ci.getCurrentIndex();
			} else {
				assertIndex = currentIndex - 1;
				solo.sleep(500);
				solo.drag(solo.toScreenX(0.370f), solo.toScreenX(0.854f),
					solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
				solo.sleep(500);
				assertTrue("Left Swipe: Tutorial not at Page " + (assertIndex + 1) + " but is at Page " + (ci.getCurrentIndex()+1), ci.getCurrentIndex() == assertIndex);
				currentIndex = ci.getCurrentIndex();
			}
		}

		for (int i=0; i < 6; i++) {
			solo.sleep(500);
			solo.drag(solo.toScreenX(0.854f), solo.toScreenX(0.370f),
				solo.toScreenY(0.498f), solo.toScreenY(0.568f), 6);
			solo.sleep(500);
		}
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(
				TutorialTest.class);
		return suite;
	}
}
