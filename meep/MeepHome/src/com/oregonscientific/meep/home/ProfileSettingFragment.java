/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.oregonscientific.meep.home.view.UserImageView;
import com.oregonscientific.meep.util.NetworkUtils;
import com.oregonscientific.meep.widget.StrokedTextView;

/**
 * A Fragment class for handle the action of view from left hand side profile setting page 
 */
public class ProfileSettingFragment extends Fragment {
	
	@SuppressWarnings("unused")
	private final String TAG = getClass().getSimpleName();

	private View changeProfileButton;
	private View changeWallpaperButton;
	private UserImageView userImageView;
	private View profilePicView;
	private View wallpaperView;
	private ProfileActivity parentActivity;
	
	private final Handler mHandler = new Handler() ;
	
	/**
	 * For executing profile edit
	 */
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	private Future<?> mFuture;
	
	private final Runnable mChangeProfile = new Runnable() {

		@Override
		public void run() {
			// Cannot continue if have not network connection
			if (!NetworkUtils.hasInternetConnection(getActivity())) {
				// Display dialog
				String title = getActivity().getString(R.string.title_oops);
				String message = getActivity().getString(R.string.alert_profile_no_network);
				DialogFragment dialog = DialogFragment.newInstance(title, message);
				dialog.setPositiveButton(
						getActivity().getString(R.string.alert_button_ok), 
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogFragment dialog, int which) {
								dialog.dismiss();
							}
							
						});
				dialog.show(getFragmentManager());
				return;
			}
			
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					enableButtons(false);
					changeOptionFragment(ProfileSettingOptionFragment.TYPE_PROFILE_PICTURE);
				}
				
			});
		}
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profile_setting, container, false);
		if (v == null) {
			return null;
		}
		
		changeProfileButton = v.findViewById(R.id.setting_profile_picture_button);
		if (changeProfileButton != null) {
			changeProfileButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (mFuture == null || mFuture.isDone()) {
						mFuture = mExecutor.submit(mChangeProfile);;
					}
				}
				
			});
		}
		
		changeWallpaperButton = v.findViewById(R.id.setting_wallpaper_button);
		if (changeWallpaperButton != null) {
			changeWallpaperButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					enableButtons(false);
					changeOptionFragment(ProfileSettingOptionFragment.TYPE_WALLPAPER);
				}
				
			});
		}
		
		userImageView = (UserImageView) v.findViewById(R.id.user_profile_image);
		if (userImageView != null) {
			userImageView.setClickable(false);
		}
		userImageView.setEditLableBackgroundColor(Color.BLACK);
		
		StrokedTextView serialNum = (StrokedTextView) v.findViewById(R.id.serial_number);
		String serialNumPrefix = getString(R.string.profile_setting_serial_number);
		if (serialNum != null && serialNumPrefix != null) {
			serialNum.setText(serialNumPrefix+Build.SERIAL);

		}
		
		StrokedTextView versionNum = (StrokedTextView) v.findViewById(R.id.version_number);
		String versionNumPrefix = getString(R.string.profile_setting_software_version);
		if (versionNum != null && versionNumPrefix != null) {
			versionNum.setText(versionNumPrefix + Build.VERSION.NAME);
		}
		
		enableSettingProfilePicture(true);
			
		parentActivity = (ProfileActivity) getActivity();
		profilePicView = parentActivity.findViewById(R.id.profile_picture_fragment);
		wallpaperView = parentActivity.findViewById(R.id.wallpaper_fragment);
		
		// Reduce the font size if the text width is bigger than the view width
		ViewTreeObserver viewTreeObserver = v.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				StrokedTextView changeWallpaperButtonText = (StrokedTextView) v.findViewById(R.id.setting_wallpaper_button_text);
				if (changeWallpaperButtonText != null) {
					Rect bounds = new Rect();
					String text = changeWallpaperButtonText.getText().toString();
					TextPaint textPaint = changeWallpaperButtonText.getPaint();
					textPaint.getTextBounds(text, 0, text.length(), bounds);
					
					if (bounds.width() > changeWallpaperButtonText.getWidth()) {
						changeWallpaperButtonText.setTextSize(changeWallpaperButtonText.getTextSize() - 1f);
					}
				}
			}
				
		});
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Create a thread to get the account info from service
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(getActivity(), ServiceManager.ACCOUNT_SERVICE);
				final Account account = accountManager.getLastLoggedInAccountBlocking();
				if (account != null) {
					// Notify UI after got the last logged in account
					updateUserProfile(account);
				} else {
					Account acct = accountManager.getDefaultAccount();
					updateUserProfile(acct);
				}
				
			}
			
		});
	}
	

	/**
	 * Set the profile image with bitmap
	 * @param bitmap a bitmap of image
	 */
	public void setProfileImage(Bitmap bitmap) {
		if(bitmap != null && userImageView != null) {
			userImageView.setUserImageBitmap(bitmap);
		}
	}
	
	/**
	 * Set the profile image with file path
	 * @param path the file path
	 */
	public void setProfileImage(String path) {
		if (path != null && userImageView != null) {
			userImageView.setUserImage(path);
			showProgressBar(false);
		}
	}
	
	public void setProfileImageResource(int resId) {
		if (userImageView != null) {
			userImageView.setUserImageResource(resId);
		}
	}
	
	public void updateUserProfile(final Account account) {
		// Quick return if the call cannot be serviced
		if (profilePicView == null) {
			return;
		}
		
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				// Display user name
				String nickName = account == null ? "" : account.getNickname();
				if (nickName == null) {
					nickName = "";
				}
				
				StrokedTextView userName = (StrokedTextView) parentActivity.findViewById(R.id.user_name);
				if (userName != null) {
					userName.setText(nickName);
				}
				
				// Display MEEP tag
				String tag = account.getMeepTag();
				String meepTagPrefix = getString(R.string.profile_setting_meep_id);
				if (meepTagPrefix != null) {
					tag = meepTagPrefix + tag;
				}
				
				StrokedTextView userTag = (StrokedTextView) parentActivity.findViewById(R.id.user_tag);
				if (userTag != null && tag != null) {
					userTag.setText(tag);
				}
						
				// Display user avatar
				if (userImageView != null) {
					String avatarUrl = account.getIconAddress();
					if (avatarUrl == null || avatarUrl.equals("")) {
						userImageView.setUserImageResource(R.drawable.default_avatar);
					} else {
						userImageView.setUserImage(avatarUrl);
					}
					enableSettingProfilePicture(true);
				}
			}
			
		});
	}
	
	public void showProgressBar(boolean b) {
		if (userImageView != null) {
			userImageView.showProgressBar(b);
		}
	}
	
	/**
	 * Change the right side fragment content or style with type
	 * @param type the type of ProfileSettingOptionFragment. eg. ProfileSettingOptionFragment.TYPE_WALLPAPER
	 */
	private void changeOptionFragment(int type) {
		if (profilePicView == null || wallpaperView == null) {
			return;
		}
		
		switch(type) {
		case ProfileSettingOptionFragment.TYPE_PROFILE_PICTURE:
			wallpaperView.setVisibility(View.INVISIBLE);
			break;
		case ProfileSettingOptionFragment.TYPE_WALLPAPER:
			wallpaperView.setVisibility(View.VISIBLE);
			break;
		}
		
		((ProfileActivity) parentActivity).slideIn();
	}	
	
	private void enableSettingProfilePicture(boolean b) {
		if (changeProfileButton != null) {
			changeProfileButton.setEnabled(b);
		}
	}
	
	private void enableButtons(boolean enable) {
		if (changeProfileButton != null) {
			changeProfileButton.setEnabled(enable);
		}
		if (changeWallpaperButton != null) {
			changeWallpaperButton.setEnabled(enable);
		}
	}

}
