/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.oregonscientific.meep.util.SystemUtils;

/**
 * The activity for displaying the tutorial
 */
public class TutorialActivity extends Activity{
	
//	private GestureDetector gestureDector;
	private static final String KEY_CURRENT_PAGE_INDEX = "index";
	private int currentPageIndex;
	
	private int[] resourceIds = { 
			R.layout.tutorial_step1,
			R.layout.tutorial_step2, 
			R.layout.tutorial_step3,
			R.layout.tutorial_step4, 
			R.layout.tutorial_step5 };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		
		setContentView(R.layout.tutorial_activity);
		
		
		currentPageIndex = 0;
	}
	
	/**
	 * Create and add the fragment of next page when click the screen
	 * @param next true is going to next page, false is going to previous page
	 */
	void goToNextPage(boolean next) {
		FragmentManager fragmentManager = getFragmentManager();
		if (next) {
			// Can not continue with last page, will go to HomeActivity and
			// destroy this activity
			if (currentPageIndex == resourceIds.length - 1) {
				// The system is now considered as restored
				SystemUtils.setSystemRestored(this, true);
				
				finish();
				return;
			} 
			
			// Create and add the fragment to current Activity
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			Fragment fragment = new TutorialFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(TutorialFragment.KEY_RESOURCES_ID, resourceIds[currentPageIndex + 1]);
			fragment.setArguments(bundle);
			fragmentTransaction.replace(R.id.tutorial_fragment_container, fragment);
			fragmentTransaction.addToBackStack("");
			fragmentTransaction.commit();
			currentPageIndex = currentPageIndex + 1;
		} else {
			if (currentPageIndex == 0 ) {
				return ;
			}
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			Fragment fragment = new TutorialFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(TutorialFragment.KEY_RESOURCES_ID, resourceIds[currentPageIndex - 1]);
			fragment.setArguments(bundle);
			fragmentTransaction.replace(R.id.tutorial_fragment_container, fragment);
			fragmentTransaction.addToBackStack("");
			fragmentTransaction.commit();
			currentPageIndex = currentPageIndex - 1;
		}
		
		
	}
	
	@Override
	public void onBackPressed() {
		// Cannot press back button with first page
		if (currentPageIndex == 0) {
			return ;
		}
		if (currentPageIndex > 0 ) {
			currentPageIndex = currentPageIndex - 1;
		}
		super.onBackPressed();
	}	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt(KEY_CURRENT_PAGE_INDEX, currentPageIndex);	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		// Save the current viewing index into preference such that when the
		// activity is resumed, user can start from the last viewing page
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		preference.edit().putInt(KEY_CURRENT_PAGE_INDEX, currentPageIndex).commit();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Remove any preferences saved
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		preference.edit().remove(KEY_CURRENT_PAGE_INDEX).commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		currentPageIndex = preference.getInt(KEY_CURRENT_PAGE_INDEX, 0);
		
		// Set the layout of tutorial page
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		Fragment fragment = new TutorialFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(TutorialFragment.KEY_RESOURCES_ID, resourceIds[currentPageIndex]);
		fragment.setArguments(bundle);
		fragmentTransaction.add(R.id.tutorial_fragment_container, fragment);
		fragmentTransaction.commit();
		
	}
	
	
}
