/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;

/**
 * Display a {@link MessageDialogFragment}
 */
public class VolumeAlertActivity extends Activity {
	
	private final String TAG = VolumeAlertActivity.class.getSimpleName();
	
	private final long VOLUME_CHECK_PERIOD = 7200000;
	
	/**
	 * Activity Action: display the dialog
	 * 
	 * {@link #EXTRA_DIALOG_MESSAGE}
	 * {@link #EXTRA_DIALOG_TITLE}
	 */
	public static final String ACTION_SHOW = 
		"com.oregonscientific.meep.home.ACTION_SHOW";
	
	/**
	 * Activity Action: dismisses the dialog
	 */
	public static final String ACTION_DISMISS = 
		"com.oregonscientific.meep.home.ACTION_DISMISS";
	
	/**
	 * The title of the dialog 
	 */
	public static final String EXTRA_DIALOG_TITLE = "title";
	
	/**
	 * The textual content of in the dialog
	 */
	public static final String EXTRA_DIALOG_MESSAGE = "message";
	
	/**
	 * The request code to display the dialog
	 */
	public static final String EXTRA_REQUEST_CODE = "request-code";
	
	/**
	 * For displaying alert dialog
	 */
	private static final String INTENT_ALERT_DIALOG = 
		"com.oregonscientific.meep.home/.VolumeAlertActivity";
	
	/**
	 * The request code to display the dialog
	 */
	private static final int SHOW_DIALOG = 1;
	
	private static boolean mIsRunning = false;
	
	@Override
	protected void onResume() {
		super.onResume();
	
		Intent intent = getIntent();
		// Cannot continue if no intent was passed into the activity
		if (intent == null) {
			return;
		}
		
		if (ACTION_SHOW.equals(intent.getAction())) {
			String title = intent.getStringExtra(EXTRA_DIALOG_TITLE);
			String message = intent.getStringExtra(EXTRA_DIALOG_MESSAGE);
			int requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, -1);
			show(title, message, requestCode);
		} else if (ACTION_DISMISS.equals(intent.getAction())) {
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mIsRunning = false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Indicates whether or not the dialog is shown 
	 * 
	 * @return {@code true} if the dialog is on top of the view stack, {@code false} otherwise
	 */
	public static boolean isShown() {
		return mIsRunning;
	}
	
	/**
	 * Display a dialog window 
	 * 
	 * @param title the title of the dialog
	 * @param message the content in the dialog
	 * @param requestCode the request code that will be returned in {@link MessageDialogFragment.OnDismissListener#onDismiss(int, int)}
	 */
	public void show(String title, String message, int requestCode) {
		// Only one instance of the dialog can be displayed at any given time
		if (isShown()) {
			return;
		}
		
		DialogFragment dialog = DialogFragment.newInstance(title, message, requestCode);
		dialog.setPositiveButton(getString(R.string.alert_button_ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogFragment dialog, int which) {
				Log.d(TAG, "Check volume again in 20 hours...");
				HomeApplication app = (HomeApplication) getApplicationContext();
				app.getVolumeService().assertVolumeLevelAtFixedRate(VOLUME_CHECK_PERIOD);
				dialog.dismiss();
				finish();
			}
			
		});
		dialog.setNegativeButton(getString(R.string.alert_button_cancel), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogFragment dialog, int which) {
				dialog.dismiss();
				finish();
			}
			
		});
		dialog.show(getFragmentManager());
		mIsRunning = true;
	}
	
	/**
	 * Dismisses the dialog
	 */
	public void dismiss() {
		FragmentManager fm = getFragmentManager();
		DialogFragment dialog = (DialogFragment) fm.findFragmentByTag(DialogFragment.TAG_DIALOG);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
	/**
	 * Displays the dialog window 
	 * 
	 * @param context the context that this dialog runs in
	 * @param title the title to be displayed in the dialog
	 * @param message the message to be displayed in the dialog
	 */
	public static void show(Context context, String title, String message) {
		show(context, SHOW_DIALOG, title, message);
	}
	
	/**
	 * Displays the dialog window 
	 * 
	 * @param context the context that this dialog runs in
	 * @param requestCode the code to display this dialog
	 * @param title the title to be displayed in the dialog
	 * @param message the message to be displayed in the dialog
	 */
	public static void show(Context context, int requestCode, String title, String message) {
		// Cannot display a dialog if there is no running context
		if (context == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(INTENT_ALERT_DIALOG));
		intent.setAction(ACTION_SHOW);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(EXTRA_DIALOG_TITLE, title);
		intent.putExtra(EXTRA_DIALOG_MESSAGE, message);
		intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
		
		context.startActivity(intent);
	}
	
	/**
	 * Dismisses the dialog that was shown 
	 * 
	 * @param context the context that displayed this dialog
	 */
	public static void dismiss(Context context) {
		dismiss(context, SHOW_DIALOG);
	}
	
	/**
	 * Dismisses the dialog that was shown 
	 * 
	 * @param context the context that displayed this dialog
	 * @param requestCode the code supplied to display the dialog
	 */
	public static void dismiss(Context context, int requestCode) {
		// We cannot dismiss the dialog if there is not running context
		if (context == null) {
			return;
		}
		
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(INTENT_ALERT_DIALOG));
		intent.setAction(ACTION_DISMISS);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(EXTRA_REQUEST_CODE, requestCode);
		
		context.startActivity(intent);
	}
	
}
