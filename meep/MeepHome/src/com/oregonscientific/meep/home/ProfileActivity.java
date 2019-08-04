/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * A Activity for holding two Fragment of profile setting
 */
public class ProfileActivity extends SlidingFragmentActivity {
	
	private SlidingMenu menu;
	
	private final IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

		@Override
		public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
			// Ignore
		}

		@Override
		public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
			// Ignore
		}

		@Override
		public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
			// If update failed, retrieve the Account object from cache prior to
			// the update. The Account object passed to this method contains
			// information about the original request
			if (!success) {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(ProfileActivity.this, ServiceManager.ACCOUNT_SERVICE);
				account = accountManager.getLastLoggedInAccountBlocking();
			}
			
			// Update UI
			ProfileSettingFragment fragment = (ProfileSettingFragment) getFragmentManager().findFragmentById(R.id.left_fragment_wrapper);
			if (fragment != null) {
				fragment.updateUserProfile(account);
				fragment.showProgressBar(false);
			}
			
			// Display dialog
			int resId = success ? R.string.alert_profile_picture_updated : R.string.alert_profile_update_failed;
			int titleId = success ? R.string.title_notice : R.string.title_oops;
			
			DialogFragment dialog = DialogFragment.newInstance(
					ProfileActivity.this.getString(titleId), 
					ProfileActivity.this.getString(resId));
			dialog.setPositiveButton(
					ProfileActivity.this.getString(R.string.alert_button_ok), 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogFragment dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.show(getFragmentManager());
		}
		
	};

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.profile_setting_layout);
		
		// Add the fragment to the view
		ProfileSettingFragment profileSettingFragment = new ProfileSettingFragment();
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.left_fragment_wrapper, profileSettingFragment);
		fragmentTransaction.commit();
		setSlidingActionBarEnabled(false);
		
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setBehindScrollScale(1.0f);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeEnabled(true);
		menu.setFadeDegree(0.5f);
		setBehindContentView(R.layout.menu_frame);
		
		fragmentTransaction = getFragmentManager().beginTransaction();
		ProfileSettingOptionFragment profilePicFragment = createProfileSettinOptionFragment(ProfileSettingOptionFragment.TYPE_PROFILE_PICTURE);
		ProfileSettingOptionFragment wallpaperFragment = createProfileSettinOptionFragment(ProfileSettingOptionFragment.TYPE_WALLPAPER);
		fragmentTransaction.replace(R.id.profile_picture_fragment, profilePicFragment);
		fragmentTransaction.replace(R.id.wallpaper_fragment, wallpaperFragment);
		fragmentTransaction.commit();
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		final View profileButton = findViewById(R.id.setting_profile_picture_button);
		final View wallpaperButton = findViewById(R.id.setting_wallpaper_button);
		menu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				if (profileButton != null) {
					profileButton.setEnabled(true);
				}
				if (wallpaperButton != null) {
					wallpaperButton.setEnabled(true);
				}
			}
			
		});
	}
	
	/**
	 * Create the ProfileSettingOptionFragment with type
	 * @param type
	 * @return
	 */
	private ProfileSettingOptionFragment createProfileSettinOptionFragment(int type) {
		ProfileSettingOptionFragment fragment = new ProfileSettingOptionFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(ProfileSettingOptionFragment.KEY_TYPE, type);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**
	 * notify the right of menu to open
	 */
	public void slideIn() {
		menu.showMenu();
	}
	
	/**
	 * notify the right of menu to close
	 */
	public void slideOut() {
		menu.showContent();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && menu != null && menu.isMenuShowing()) {
			slideOut();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AccountManager accountManager = (AccountManager)ServiceManager.getService(this, ServiceManager.ACCOUNT_SERVICE);
		if (accountManager != null) {
			accountManager.registerCallback(accountCallback);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		AccountManager accountManager = (AccountManager)ServiceManager.getService(this, ServiceManager.ACCOUNT_SERVICE);
		if (accountManager != null) {
			accountManager.unregisterCallback(accountCallback);
		}
	}

}
