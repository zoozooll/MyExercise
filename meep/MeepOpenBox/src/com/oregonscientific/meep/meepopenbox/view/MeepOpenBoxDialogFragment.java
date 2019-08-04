package com.oregonscientific.meep.meepopenbox.view;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.MeepOpenBoxBaseActivity;
import com.oregonscientific.meep.meepopenbox.MeepOpenBoxParentalSetting;
import com.oregonscientific.meep.meepopenbox.MeepOpenBoxWiFiSetup;
import com.oregonscientific.meep.meepopenbox.R;
import com.oregonscientific.meep.util.SystemUtils;
import com.oregonscientific.meep.widget.OutlinedTextView;

/**
 * Dialog Fragment for Meep Open Box
 * @author Charles
 *
 */
public class MeepOpenBoxDialogFragment extends DialogFragment {
	
	private final String TAG = MeepOpenBoxDialogFragment.class.getSimpleName();
	
	public static final int LOADING_DIALOG_ID = 0;
	public static final int LANGUAGE_NOT_SELECTED_DIALOG_ID = 1;
	public static final int TIMEZONE_NOT_SELECTED_DIALOG_ID = 2;
	public static final int SKIP_PARENTAL_SETTING_DIALOG_ID = 3;
	public static final int SKIP_WIFI_SETUP_DIALOG_ID = 4;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}
	
	/**
	 * Creates a dialog
	 * @param title id of dialog
	 */
	public static MeepOpenBoxDialogFragment newInstance(int title) {
		MeepOpenBoxDialogFragment myDialogFragment = new MeepOpenBoxDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		
		int id = getArguments().getInt("title");
		if (id == LOADING_DIALOG_ID) {
			View view = inflater.inflate(R.layout.loading, container, false);
			createLoading(view);
			setDismissEnabled(view, true);
			return view;
		}
		
		View view = inflater.inflate(R.layout.open_box_dialog_layout, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.transparent_half_alpha));
		
		OutlinedTextView okButtonText = (OutlinedTextView) view.findViewById(R.id.okBtnText);
		ImageButton okButton = (ImageButton) view.findViewById(R.id.okBtn);
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				MeepOpenBoxDialogFragment.this.dismiss();
				((MeepOpenBoxBaseActivity) getActivity()).setNextButtonEnabled(true);
			}
		});
		
		OutlinedTextView skipButtonText = (OutlinedTextView) view.findViewById(R.id.skipBtnText);
		ImageButton skipButton = (ImageButton) view.findViewById(R.id.skipBtn);
		skipButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				MeepOpenBoxDialogFragment.this.dismiss();
				MeepOpenBoxBaseActivity activity = (MeepOpenBoxBaseActivity) getActivity();
				activity.clearPreferences();
				SystemUtils.setSystemConfigured(activity, true);
				activity.startActivityByPackage(MeepOpenBoxBaseActivity.HOME_ACTIVITY_PACKAGE_NAME);
				activity.quitOpenBox();
			}
		});
		
		OutlinedTextView backButtonText = (OutlinedTextView) view.findViewById(R.id.backBtnText);
		ImageButton backButton = (ImageButton) view.findViewById(R.id.backBtn);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				MeepOpenBoxDialogFragment.this.dismiss();
				((MeepOpenBoxBaseActivity) getActivity()).setNextButtonEnabled(true);
			}
		});
		
		TextView message = (TextView) view.findViewById(R.id.message);
		switch (getArguments().getInt("title")) {
			case LANGUAGE_NOT_SELECTED_DIALOG_ID:
				okButton.setVisibility(View.VISIBLE);
				okButtonText.setVisibility(View.VISIBLE);
				message.setText(getResources().getString(R.string.select_language));
				((View) okButton.getParent()).setVisibility(View.VISIBLE);
				break;
			case TIMEZONE_NOT_SELECTED_DIALOG_ID:
				okButton.setVisibility(View.VISIBLE);
				okButtonText.setVisibility(View.VISIBLE);
				message.setText(getResources().getString(R.string.select_time_zone));
				((View) okButton.getParent()).setVisibility(View.VISIBLE);
				break;
			case SKIP_PARENTAL_SETTING_DIALOG_ID:
				backButton.setVisibility(View.VISIBLE);
				backButtonText.setVisibility(View.VISIBLE);
				skipButton.setVisibility(View.VISIBLE);
				skipButtonText.setVisibility(View.VISIBLE);
				message.setText(getResources().getString(R.string.skip_parental_setting_message));
				((View) backButton.getParent()).setVisibility(View.VISIBLE);
				((View) skipButton.getParent()).setVisibility(View.VISIBLE);
				break;
			case SKIP_WIFI_SETUP_DIALOG_ID:
				backButton.setVisibility(View.VISIBLE);
				backButtonText.setVisibility(View.VISIBLE);
				skipButton.setVisibility(View.VISIBLE);
				skipButtonText.setVisibility(View.VISIBLE);
				message.setText(getResources().getString(R.string.skip_wifi_setup_message));
				((View) backButton.getParent()).setVisibility(View.VISIBLE);
				((View) skipButton.getParent()).setVisibility(View.VISIBLE);
				break;
		}
		
		return view;
	}
	
	/**
	 * Sets properties of dialog fragment
	 * @param view the view of dialog fragment
	 */
	private void createLoading(View view) {
		view.setFocusableInTouchMode(true);
		view.setFocusable(true);
		view.requestFocus();
	}
  
	/**
	 * Sets whether back button can dismiss dialog fragment
	 * @param view the view of dialog fragment
	 * @param enableDismiss true to enable dismiss dialog by pressing back button, false otherwise
	 */
	private void setDismissEnabled(View view, final boolean enableDismiss) {
		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (enableDismiss) {
						dismiss();
					}
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	public void onCancel(DialogInterface dialog){
		// the dismiss event is same of cancel in this DialogFragment
		super.onCancel(dialog);
		if (getActivity() instanceof MeepOpenBoxParentalSetting) {
			((MeepOpenBoxParentalSetting) getActivity()).setNextButtonEnabled(true);
		}
	}
	
	@Override
	public void dismiss() {
		try {
			if (getActivity() instanceof MeepOpenBoxParentalSetting) {
				((MeepOpenBoxParentalSetting) getActivity()).setButtonsVisible(true);
			}
			super.dismiss();
		} catch (Exception ex) {
			Log.e(TAG, "Dialog cannot be dismissed because: " + ex.toString());
		}
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (getActivity() instanceof MeepOpenBoxParentalSetting) {
			((MeepOpenBoxParentalSetting) getActivity()).setButtonsVisible(true);
		}
		super.onDismiss(dialog);
	}
	
}
