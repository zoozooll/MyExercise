/**
 * Copyright (C) 2013 IDT International Ltd.
 */

package com.oregonscientific.meep.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oregonscientific.meep.R;
import com.oregonscientific.meep.widget.DialogButton;
import com.oregonscientific.meep.widget.StrokeTextView;

/**
 * A dialog fragment to display any message
 */
public class DialogFragment extends android.app.DialogFragment {
	
	/**
	 * The unique identifier identifying this instance of the dialog
	 */
	public static final String TAG_DIALOG = "com.oregonscientific.meep.app.DIALOG_FRAGMENT";
	
	private final int TITLE_TEXT_SIZE_SMALL = 32;
	private final int TITLE_TEXT_SIZE_LARGE = 50;
	
	private final static String KEY_TITLE = "dialog_title";
	private final static String KEY_MESSAGE = "dialog_message";
	private final static String KEY_REQUEST_CODE = "dialog_request_code";
	
	private final int COLOR_TEXT = Color.rgb(90, 90, 90);
	
	public final static int TYPE_NOTICE = 0;
	public final static int TYPE_WAITING = 1;
	public final static int TYPE_WARNING = 2;
	
	private DialogInterface.OnCancelListener mOnCancelListener;
	private DialogInterface.OnClickListener mNegativeButtonListener;
	private DialogInterface.OnClickListener mNeutralButtonListener;
	private DialogInterface.OnClickListener mPositiveButtonListener;
	
	private String mNegativeButtonText;
	private String mNeutralButtonText;
	private String mPositiveButtonText;
	
	/**
	 * Create a dialog fragment with no frame and transparent background
	 * 
	 * @param title the dialog title
	 * @param message the dialog message
	 * @return A new instance of the {@link DialogFragment}
	 */
	public static DialogFragment newInstance(String title, String message) {
		return newInstance(title, message, -1);
	}

	/**
	 * Create a dialog fragment with no frame and transparent background
	 * 
	 * @param title
	 *            the dialog title
	 * @param message
	 *            the dialog message
	 * @param requestCode
	 *            this code identifying the dialog. If a dialog with the given
	 *            {@code requestCode} was already displayed, calling
	 *            {@link #show(FragmentManager)} will only bring the previously displayed dialog to front
	 * @return A new instance of the {@link DialogFragment}
	 */
	public static DialogFragment newInstance(String title, String message, int requestCode) {
		DialogFragment dialog = new DialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString(KEY_TITLE, title);
		bundle.putString(KEY_MESSAGE, message);
		bundle.putInt(KEY_REQUEST_CODE, requestCode);
		dialog.setArguments(bundle);
		dialog.setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		return dialog;
	}
	
	/**
	 * Display the dialog, adding the fragment to the given FragmentManager.
	 * 
	 * @param manager The {@link FragmentManager} this fragment will be added to.
	 */
	public void show(FragmentManager manager) {
		// Quick return if the request cannot be processed
		if (manager == null) {
			return;
		}
		
		// Only display dialog with request code that is not already in the fragment stack
		int requestCode = getRequestCode();
		Fragment fragment = manager.findFragmentByTag(String.valueOf(requestCode));
		if (fragment != null) {
			FragmentTransaction ft = manager.beginTransaction();
			ft.show(fragment);
			ft.commit();
			return;
		}
		
		super.show(manager, String.valueOf(requestCode));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dialog, container, false);
		StrokeTextView title = (StrokeTextView) v.findViewById(R.id.title);
		title.setTextColor(Color.WHITE);
		
		TextView message = (TextView) v.findViewById(R.id.message);
		message.setTextColor(COLOR_TEXT);
		Bundle bundle = getArguments();
		
		String titleString = bundle == null ? null : bundle.getString(KEY_TITLE);
		if (titleString == null) {
			titleString = "";
		}
		if (title != null) {
			int titleSize = titleString.length() > 12 ? TITLE_TEXT_SIZE_SMALL : TITLE_TEXT_SIZE_LARGE;
			title.setTextSize(titleSize);
			title.setText(titleString);
		}
		
		String messageString = bundle == null ? null : bundle.getString(KEY_MESSAGE);
		if (messageString == null) {
			messageString = "";
		}
		
		if (message != null) {
			message.setText(messageString);
		}
		
		Button closeButton = (Button) v.findViewById(R.id.close_dialog);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Closes the dialog fragment
				getDialog().cancel();
			}
			
		});
		
		DialogButton button = (DialogButton) v.findViewById(R.id.negative_button);
		setDialogButton(button, DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText, mNegativeButtonListener);
		
		button = (DialogButton) v.findViewById(R.id.neutral_button);
		setDialogButton(button, DialogInterface.BUTTON_NEUTRAL, mNeutralButtonText, mNeutralButtonListener);
		
		button = (DialogButton) v.findViewById(R.id.positive_button);
		setDialogButton(button, DialogInterface.BUTTON_POSITIVE, mPositiveButtonText, mPositiveButtonListener);
		
		return v;
	}
	
	/**
	 * Sets a listener to be invoked when the negative button of the dialog is pressed
	 * 
	 * @param text The text to display in the negative button
	 * @param listener The listener to be invoked
	 */
	public void setNegativeButton(String text, final DialogInterface.OnClickListener listener) {
		mNegativeButtonText = text;
		mNegativeButtonListener = listener;
		
		if (getView() != null) {
			setDialogButton(
					(DialogButton) getView().findViewById(R.id.negative_button), 
					DialogInterface.BUTTON_NEGATIVE, 
					mNegativeButtonText, 
					mNegativeButtonListener);
		}
	}
	
	/**
	 * Sets a listener to be invoked when the neutral button of the dialog is pressed
	 * 
	 * @param text The text to display in the neutral button
	 * @param listener The listener to be invoked
	 */
	public void setNeutralButton(String text, DialogInterface.OnClickListener listener) {
		mNeutralButtonText = text;
		mNeutralButtonListener = listener;
		
		if (getView() != null) {
			setDialogButton(
					(DialogButton) getView().findViewById(R.id.neutral_button), 
					DialogInterface.BUTTON_NEUTRAL, 
					mNeutralButtonText, 
					mNeutralButtonListener);
		}
	}
	
	/**
	 * Sets a listener to be invoked when the positive button of the dialog is pressed
	 * 
	 * @param text The text to display in the positive button
	 * @param listener The listener to be invoked
	 */
	public void setPositiveButton(String text, DialogInterface.OnClickListener listener) {
		mPositiveButtonText = text;
		mPositiveButtonListener = listener;
		
		if (getView() != null) {
			setDialogButton(
					(DialogButton) getView().findViewById(R.id.positive_button), 
					DialogInterface.BUTTON_POSITIVE, 
					mPositiveButtonText, 
					mPositiveButtonListener);
		}
	}
	
	private void setDialogButton(DialogButton button, final int position, String text, final DialogInterface.OnClickListener listener) {
		if (button != null) {
			button.setText(text);
			button.setVisibility(text == null ? View.GONE : View.VISIBLE);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null) {
						listener.onClick(DialogFragment.this, position);
					}
				}
				
			});
		}
	}
	
	/**
	 * Sets the callback that will be called if the dialog is canceled.
	 */
	public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
		mOnCancelListener = listener;
	}
	
	/**
	 * Returns the original request code that displayed this dialog
	 */
	public int getRequestCode() {
		Bundle bundle = getArguments();
		return bundle == null ? -1 : bundle.getInt(KEY_REQUEST_CODE, -1);
	}
	
	@Override
	public void onCancel(android.content.DialogInterface dialog) {
		super.onCancel(dialog);
		
		if (mOnCancelListener != null) {
			mOnCancelListener.onCancel(this);
		}
	}

}
