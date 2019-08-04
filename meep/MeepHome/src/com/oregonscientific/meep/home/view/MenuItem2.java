/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.oregonscientific.meep.app.DialogMessage;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.util.NetworkUtils;

/**
 * Base class of an item in the main menu in MeepHome
 */
public class MenuItem2 extends RelativeLayout {
	
	protected final String TAG = getClass().getSimpleName();
	
	private final int BADGE_MARGIN_TOP = -4;
	private final int BADGE_TEXT_SIZE_SMALL = 28;
	private final int BADGE_TEXT_SIZE_MEDIUM = 38;
	private final int BADGE_TEXT_SIZE_LARGE = 48;
	private final int BADGE_TEXT_COLOR = 0xff000000;
	
	public enum Position {
		LEFT(0),
		RIGHT(1),
		TOP(2),
		BOTTOM(3);
		
		private final int value;
		
		private Position(final int pos) {
			value = pos;
		}
		
		public static final Position valueOf(int pos) {
			for (Position position : Position.values()) {
				if (pos == position.value) {
					return position;
				}
			}
			return LEFT;
		}
	}
	
	private RelativeLayout mContainer;
	protected RelativeLayout mContent;
	
	private String mLaunchPackage;
	private String mLaunchActivity;
	private String mLaunchAction;
	
	private Position mBadgePosition = Position.RIGHT;
	private TextView mBadge;
	
	private boolean mRequireNetwork = false;
	private boolean mRequireRegisteredUser = false;
	
	private final Handler mHandler = new Handler();
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	private Future<?> mLaunchFuture = null;
	
	protected final OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// Cannot proceed if the listener has not finished processing a
			// previous click event
			if (mLaunchFuture != null && !mLaunchFuture.isDone()) {
				return;
			}
			
			mLaunchFuture = mExecutor.submit(new Runnable() {

				@Override
				public void run() {
					// Determines whether or not the menu item can launch the package
					if (mRequireNetwork) {
						if (!NetworkUtils.hasInternetConnection(getContext())) {
							showAlertDialog(
									getContext().getString(R.string.title_notice),
									DialogMessage.getLaunchPackageFailedMessage(getContext(), getLaunchIntent(), R.string.alert_launch_online),
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogFragment dialog, int which) {
											Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											getContext().startActivity(intent);
											dialog.dismiss();
										}
									});
							return;
						}
					}
					
					Intent intent = getLaunchIntent();
					// Cannot continue if the intent is null
					if (intent == null) {
						return;
					}
					
					AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
					Account account = am.getLastLoggedInAccountBlocking();
					account = account == null ? am.getDefaultAccount() : account;
					if (account != null) {
						if (mRequireRegisteredUser) {
							// Determines whether the user is registered
							String meepTag = account.getMeepTag();
							if (meepTag == null || meepTag.length() == 0) {
								showAlertDialog(
										getContext().getString(R.string.title_oops), 
										DialogMessage.getLaunchPackageFailedMessage(getContext(), intent, R.string.alert_launch_register));
								return;
							}
						}
						
						PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
						int isAccessible = pm.isAccessibleBlocking(account.getId(), intent.getComponent());
						
						if (isAccessible != PermissionManager.FLAG_PERMISSION_OK) {
							int resId = isAccessible == PermissionManager.FLAG_PERMISSION_TIMEOUT ? R.string.title_timeout : R.string.title_oops;
							showAlertDialog( 
									getContext().getString(resId), 
									DialogMessage.getPackagePermissionViolatedMessage(getContext(), intent.getComponent(), isAccessible));
							return;
						}
					}
					
					// By default, if no user was ever logged in, owner of
					// the device can still access all installed
					// applications. On the other hand, if a user was logged
					// in previously, permission settings would then be
					// enforced.
					getContext().startActivity(intent);
				}
				
			});
		}
		
	};

	public MenuItem2(Context context) {
		this(context, null);
	}
	
	public MenuItem2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MenuItem2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mContainer = new RelativeLayout(context);
		mContainer.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mContent = new RelativeLayout(context);
		mContent.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mContent.setOnClickListener(mOnClickListener);
		
		mContainer.addView(mContent);
		addView(mContainer);
		
		mBadge = new TextView(context);
		mBadge.setGravity(Gravity.CENTER);
		mBadge.setVisibility(View.INVISIBLE);
		addView(mBadge);
		
		// Retrieves style attributes from layout XML
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuItem);
		
		setBackground(a.getDrawable(R.styleable.MenuItem_menuItemBackground));
		setMenuItemClickable(a.getBoolean(R.styleable.MenuItem_menuItemClickable, true));
		setLaunchPackage(a.getString(R.styleable.MenuItem_launchPackage));
		setLaunchActivity(a.getString(R.styleable.MenuItem_launchActivity));
		setLaunchAction(a.getString(R.styleable.MenuItem_launchPackageAction));
		setBadgeBackground(a.getDrawable(R.styleable.MenuItem_badgeBackground));
		setBadgePosition(a.getInteger(R.styleable.MenuItem_badgePosition, Position.RIGHT.value));
		setBadgeTextColor(a.getInteger(R.styleable.MenuItem_badgeTextColor, BADGE_TEXT_COLOR));
		setRequireNetwork(a.getBoolean(R.styleable.MenuItem_requireNetwork, false));
		setRequireRegisteredUser(a.getBoolean(R.styleable.MenuItem_requireRegisteredUser, false));
		
		String font = a.getString(R.styleable.MenuItem_badgeTypeface);
		if (font != null) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(), font);
			setBadgeTypeface(tf);
		}
		setBadgeText(a.getString(R.styleable.MenuItem_badgeText));
		
		a.recycle();
	}
	
	/**
	 * Displays an alert dialog with only 1 button 
	 * 
	 * @param title the title of the dialog
	 * @param message the message to be displayed in the dialog
	 */
	private void showAlertDialog(String title, String message) {
		showAlertDialog(title, message, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogFragment dialog, int which) {
				dialog.dismiss();
			}
			
		});
	}
	
	private void showAlertDialog(String title, String message, DialogInterface.OnClickListener listener) {
		final DialogFragment dialog = DialogFragment.newInstance(title, message);
		dialog.setPositiveButton(getContext().getString(R.string.alert_button_ok), listener);
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				Activity activity = (Activity) getContext();
				dialog.show(activity.getFragmentManager());
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("Override")
	public void setBackground(Drawable background) {
		if (mContent != null) {
			mContent.setBackgroundDrawable(background);
		}
	}
	
	/**
	 * Enables or disables click events for this menu item. 
	 * 
	 * @param clickable true to make the view clickable, false otherwise
	 */
	public void setMenuItemClickable(boolean clickable) {
		if (mContent != null) {
			mContent.setClickable(clickable);
		}
	}
	
	/**
	 * Specifies the package to launch when the menu item is selected
	 * 
	 * @param packageName The name of the package to launch.
	 */
	public void setLaunchPackage(String packageName) {
		mLaunchPackage = packageName;
	}
	
	/**
	 * Specifies the activity to launch
	 * 
	 * @param activity The name of the activity to launch
	 */
	public void setLaunchActivity(String activity) {
		mLaunchActivity = activity;
	}
	
	/**
	 * Specifies the general action to perform
	 * 
	 * @param action An action name, such as ACTION_VIEW. Application-specific actions should be prefixed with the vendor's package name.
	 */
	public void setLaunchAction(String action) {
		mLaunchAction = action;
	}
	
	/**
	 * Defines the position to place the badge. Note that setting a position does not automatically
	 * enable this menu item to display a badge. You must call {@link #setBadgeBackground(Drawable)} to
	 * display a badge
	 * 
	 * @param position The position in the menu item the badge is displayed
	 */
	public void setBadgePosition(int position) {
		setBadgePosition(Position.valueOf(position));
	}
	
	/**
	 * Defines the position to place the badge. Note that setting a position does not automatically
	 * enable this menu item to display a badge. You must call {@link #setBadgeEnabled(boolean)} to
	 * enable display a badge
	 * 
	 * @param position The position in the menu item the badge is displayed
	 */
	public void setBadgePosition(Position pos) {
		mBadgePosition = pos;
		if (mBadge != null) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 
					LayoutParams.WRAP_CONTENT);
			
			Drawable background = mBadge.getBackground();
			int width = background == null ? -1 : background.getIntrinsicWidth();
			int horizontalMargin = width == -1 ? 0 : (width / 5) * 3;
			
			switch (pos) {
			case LEFT:
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				layoutParams.setMargins(-horizontalMargin, BADGE_MARGIN_TOP, 0, 0);
				break;
			default:
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				layoutParams.setMargins(0, BADGE_MARGIN_TOP, -horizontalMargin, 0);
				break;
			}
			mBadge.setLayoutParams(layoutParams);
			setClipToPadding(false);
			setClipChildren(false);
			
			requestLayout();
		}
	}
	
	/**
	 * Sets the background of the badge to the given {@link Drawable}
	 * 
	 * @param badgeBackground The Drawable to use as the background, or null to remove the background
	 */
	@SuppressWarnings("deprecation")
	public void setBadgeBackground(Drawable badgeBackground) {
		if (mBadge != null) {
			mBadge.setBackgroundDrawable(badgeBackground);
			setBadgePosition(mBadgePosition);
		}
	}
	
	/**
	 * Sets the text to be displayed in the badge 
	 * 
	 * @param text the text to display in the badge
	 */
	public void setBadgeText(String text) {
		if (mBadge != null) {
			if (text == null) {
				mBadge.setText(null);
				mBadge.setVisibility(View.INVISIBLE);
			} else {
				switch (text.length()) {
				case 1:
					mBadge.setTextSize(BADGE_TEXT_SIZE_LARGE);
					break;
				case 2:
					mBadge.setTextSize(BADGE_TEXT_SIZE_MEDIUM);
					break;
				case 3:
					mBadge.setTextSize(BADGE_TEXT_SIZE_SMALL);
					break;
				default:
					mBadge.setEllipsize(TextUtils.TruncateAt.END);
					break;
				}
				mBadge.setText(text);
				mBadge.setVisibility(View.VISIBLE);
			}
			invalidate();
		}
	}
	
	/**
	 * Returns the text displayed in the badge. Note: content of the returned value should not be modified.
	 */
	public String getBadgeText() {
		CharSequence chars = mBadge.getText();
		return chars == null ? null : chars.toString();
	}
	
	/**
	 * Sets the typeface in which the text in badge should be displayed.
	 * 
	 * @param tf the typeface to use
	 */
	public void setBadgeTypeface(Typeface tf) {
		if (mBadge != null && tf != null) {
			mBadge.setTypeface(tf);
		}
	}
	
	/**
	 * Sets the text color of the badge for all the states (normal, selected, focused) to be this color.
	 * @param color
	 */
	public void setBadgeTextColor(int color) {
		if (mBadge != null) {
			mBadge.setTextColor(color);
		}
	}
	
	/**
	 * Return a "good" intent to launch
	 */
	public Intent getLaunchIntent() {
		Intent result = new Intent();
		if (mLaunchPackage == null && mLaunchAction == null) {
			return null;
		}
		
		if (mLaunchAction != null) {
			result.setAction(mLaunchAction);
		} else if (mLaunchActivity != null) {
			result.setComponent(new ComponentName(mLaunchPackage, mLaunchActivity));
		} else {
			result = getContext().getPackageManager().getLaunchIntentForPackage(mLaunchPackage);
		}
		
		return result;
	}
	
	/**
	 * Specifies whether or not network connection is required before this menu item can launch the
	 * intent 
	 */
	public void setRequireNetwork(boolean r) {
		mRequireNetwork = r;
	}
	
	/**
	 * Specifies whether or not the last logged in user needs to be registered before this menu item can launch the
	 * intent 
	 */
	public void setRequireRegisteredUser(boolean r) {
		mRequireRegisteredUser = r;
	}
	
}
