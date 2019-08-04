package com.oregonscientific.meep.meepopenbox;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxDialogFragment;
import com.oregonscientific.meep.util.NetworkUtils;
import com.oregonscientific.meep.util.SystemUtils;
import com.oregonscientific.meep.widget.OutlinedTextView;

/**
 * Activity for Parental Setting page
 * @author Charles
 */
public class MeepOpenBoxParentalSetting extends MeepOpenBoxBaseActivity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_box_parental_setting_layout);
		
		Button backButton = (Button) findViewById(R.id.parentalSettingBackBtn);
		backButton.setOnClickListener(this);
		ImageButton yesButton = (ImageButton) findViewById(R.id.parentalSettingYesBtn);
		yesButton.setOnClickListener(this);
		ImageButton noButton = (ImageButton) findViewById(R.id.parentalSettingNoBtn);
		noButton.setOnClickListener(this);

	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.parentalSettingBackBtn) {
			onBackPressed();
		}
		if (view.getId() == R.id.parentalSettingYesBtn) {
			clearPreferences();
			SystemUtils.setSystemConfigured(this, true);
			if (NetworkUtils.hasInternetConnection(this)) {
				startActivityByPackage(SPLASH_SCREEN_ACTIVITY_PACKAGE_NAME);
			} else {
				startActivityByPackage(HOME_ACTIVITY_PACKAGE_NAME);
			}
			quitOpenBox();
		}
		if (view.getId() == R.id.parentalSettingNoBtn) {
			DialogFragment newFragment = MeepOpenBoxDialogFragment.newInstance(MeepOpenBoxDialogFragment.SKIP_PARENTAL_SETTING_DIALOG_ID);
			newFragment.show(getFragmentManager(), "dialog");
			setNextButtonEnabled(false);	
			setButtonsVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void hideBackButton() {
		Button backButton = (Button) findViewById(R.id.parentalSettingBackBtn);
		backButton.setVisibility(View.INVISIBLE);
		TextView backButtonText = (TextView) findViewById(R.id.parentalSettingBackText);
		backButtonText.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void setNextButtonEnabled(boolean enabled) {
		ImageButton noButton = (ImageButton) findViewById(R.id.parentalSettingNoBtn);
		noButton.setEnabled(enabled);
	}
	
	/**
	 * Sets the visibility of buttons
	 * @param visible true if visible, false otherwise
	 */
	public void setButtonsVisible(boolean visible) {
		if (!visible) {
			setButtonsVisibility(View.INVISIBLE);
		} else {
			setButtonsVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Sets the visibility of buttons
	 * @param value value of visibility
	 */
	private void setButtonsVisibility(int value) {
		Button backButton = (Button) findViewById(R.id.parentalSettingBackBtn);
		backButton.setVisibility(value);
		View backButtonText = findViewById(R.id.parentalSettingBackText);
		backButtonText.setVisibility(value);
		ImageButton yesButton = (ImageButton) findViewById(R.id.parentalSettingYesBtn);
		yesButton.setVisibility(value);
		OutlinedTextView yesButtonText = (OutlinedTextView) findViewById(R.id.parentalSettingYesText);
		yesButtonText.setVisibility(value);
		ImageButton noButton = (ImageButton) findViewById(R.id.parentalSettingNoBtn);
		noButton.setVisibility(value);
		OutlinedTextView noButtonText = (OutlinedTextView) findViewById(R.id.parentalSettingNoText);
		noButtonText.setVisibility(value);
	}
	
}