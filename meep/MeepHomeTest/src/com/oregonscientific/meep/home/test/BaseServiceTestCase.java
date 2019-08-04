/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.ServiceTestCase;
import android.util.Log;

public abstract class BaseServiceTestCase<T extends Service> extends ServiceTestCase<T> {
	
	private final String TAG = BaseServiceTestCase.class.getSimpleName();
	
	/** Variables used to control testing of asyn tasks */
	private final long MAX_DELAY_TIME = 500;
	protected boolean mTestCompleted;
	protected boolean mTestResult;

	public BaseServiceTestCase(Class<T> serviceClass) {
		super(serviceClass);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// Sets debugging to true
		mTestCompleted = false;
		mTestResult = false;
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	/**
	 * Creates a context for this test
	 * 
	 * @return the context for this test
	 */
	protected Context getTestContext() {
		try {
			return getContext().createPackageContext(getClass().getPackage().getName(), Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			// Cannot create mock context for the test. Ignore
			Log.e(TAG, "Cannot create mock context for the test because " + e);
			return null;
		}
	}
	
	protected Context getHomeContext() {
		try {
			return getContext().createPackageContext("com.oregonscientific.meep.home", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			// Cannot create context for the testing application. Ignore
			Log.e(TAG, "Cannot create context for the testing application because " + e);
			return null;
		}
	}
	
	/**
	 * Causes the test to complete
	 * 
	 * @param succeed {@code true} to indicate the test completed successfully, {@code false} to indicate the test failed
	 */
	protected void completeTest(boolean succeed) {
		mTestResult = succeed;
		mTestCompleted = true;
		
		if (!succeed) {
			fail();
		}
	}
	
	/**
	 * Causes the calling thread to wait until {@link #mTestCompleted} is {@code true}
	 */
	protected void waitUntilComplete() {
		while (!mTestCompleted) {
			try {
				Thread.sleep(MAX_DELAY_TIME);
			} catch (Exception ex) {
				mTestCompleted = true;
			}
		}
	}
	
	/**
	 * Causes the test to sleep for the given interval of time (given in milliseconds).
	 * 
	 * @param time The time to sleep in milliseconds.
	 */
	protected void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignored) {
		}
	}
	
	/**
	 * Reads the resource as specified by {@code resId} as a String 
	 * 
	 * @param ctx the context to retrieve the resource from
	 * @param resId the ID of the resource
	 * @return the resource string
	 */
	protected String readRawTextFile(Context ctx, int resId) {
		// Quick return if the request cannot be processed
		if (ctx == null) {
			return null;
		}
		
		// Open the raw resource file
		InputStream inputStream = ctx.getResources().openRawResource(resId);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		StringBuilder text = new StringBuilder();

		try {
			String line;
			while ((line = buffreader.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			return null;
		}
		
		return text.toString();
	}

}
