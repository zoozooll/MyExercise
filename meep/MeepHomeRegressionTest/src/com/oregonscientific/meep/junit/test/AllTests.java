package com.oregonscientific.meep.junit.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.oregonscientific.meep.home.test.HomeActivityTest;
import com.oregonscientific.meep.home.test.OpenBoxActivityTest;
import com.oregonscientific.meep.home.test.TutorialTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTest(OpenBoxActivityTest.suite());
		suite.addTest(TutorialTest.suite());
		suite.addTest(HomeActivityTest.suite());
		//$JUnit-END$
		return suite;
	}

	
}
