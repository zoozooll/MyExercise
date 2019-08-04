/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.app;


/**
 * The interfaces and control variables of dialogs
 */
public class DialogInterface {
	
	/**
	 * The identifier for the POSITIVE button
	 */
	public static final int BUTTON_POSITIVE = -1;
	
	/**
	 * The identifier for the NEGATIVE button
	 */
	public static final int BUTTON_NEGATIVE = -2;
	
	/**
	 * The identifier for the NEUTRAL button
	 */
	public static final int BUTTON_NEUTRAL = -3;
	
	/**
	 * Interface used to allow the creator of a dialog to run some code when a button in the dialog is clicked.
	 */
	public interface OnClickListener {
		
		/**
		 * This method will be invoked when a button in the dialog is clicked.
		 * 
		 * @param dialog The dialog that received the click.
		 * @param which The button that was clicked (e.g. {@link BUTTON_POSITIVE}). 
		 */
		public void onClick(DialogFragment dialog, int which);
		
	}
	
	/**
	 * Interface used to allow the creator of a dialog to run some code when the dialog is canceled.
	 */
	public interface OnCancelListener {
		
		/**
		 * This method will be invoked when the dialog is canceled.
		 * 
		 * @param dialog The dialog that was canceled will be passed into the method.
		 */
		public void onCancel(DialogFragment dialog);
		
	}

}
