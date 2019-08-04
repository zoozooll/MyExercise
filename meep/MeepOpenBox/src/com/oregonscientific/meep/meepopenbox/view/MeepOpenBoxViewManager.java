package com.oregonscientific.meep.meepopenbox.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.oregonscientific.meep.meepopenbox.MeepOpenBoxBaseActivity;
import com.oregonscientific.meep.meepopenbox.MeepOpenBoxLanguageSelect;
import com.oregonscientific.meep.meepopenbox.MeepOpenBoxParentalSetting;
import com.oregonscientific.meep.meepopenbox.MeepOpenBoxTimeZoneSelect;
import com.oregonscientific.meep.meepopenbox.MeepOpenBoxWiFiSetup;

/**
 * Manager manages the view settings and the input data in each view
 * @author Charles
 */
public class MeepOpenBoxViewManager {
	
	private static final String TAG = MeepOpenBoxViewManager.class.getSimpleName();
	
	private static final String NAME = "OpenBox";
	private static final String LANG_KEY = "langSelected";
	private static final String TIME_ZONE_KEY = "timeZoneSelected";
	private static final String PAGE_NUM_KEY = "pageNum";
	private static final String PAGE_SWITCH_KEY = "havePreviousPage";
	
	public static final int PAGE_LANGUAGE_SELECT = 0;
	public static final int PAGE_TIMEZONE = 1;
	public static final int PAGE_WIFI_SETUP = 2;
	public static final int PAGE_PARENTAL_SETTING = 3;
	
	/**
	 * Clears all shared preferences
	 * @param context current context
	 */
	public static void clearPreferences(Context context) {
		SharedPreferences page = context.getSharedPreferences(NAME, 0);
		SharedPreferences.Editor preferencesEditor = page.edit();
		preferencesEditor.clear();
		preferencesEditor.commit();
	}
	
	/**
	 * Gets the memorized language
	 * @param context current context
	 * @return the position of language in ListView
	 */
	public static String getLanguagePreference(Context context) {
		SharedPreferences page = context.getSharedPreferences(NAME, 0);
		return page.getString(LANG_KEY, null);
	}
	
	/**
	 * Sets the language selected to be memorized
	 * @param context current context
	 * @param position the position of language in ListView
	 */
	public static void setLanguagePreference(Context context, String position) {
		SharedPreferences page = context.getSharedPreferences(NAME, 0);
		SharedPreferences.Editor preferencesEditor = page.edit();
		preferencesEditor.putString(LANG_KEY, position);
		preferencesEditor.commit();
	}
	
	/**
	 * Gets the memorized time zone
	 * @param context current context
	 * @return the name of time zone
	 */
	public static String getTimeZonePreference(Context context) {
		SharedPreferences page = context.getSharedPreferences(NAME, 0);
		return page.getString(TIME_ZONE_KEY, null);
	}
	
	/**
	 * Sets the time zone selected to be memorized
	 * @param context current context
	 * @param value the name of time zone
	 */
	public static void setTimeZonePreference(Context context, String value) {
		SharedPreferences page = context.getSharedPreferences(NAME, 0);
		SharedPreferences.Editor preferencesEditor = page.edit();
		preferencesEditor.putString(TIME_ZONE_KEY, value);
		preferencesEditor.commit();
	}
	
	/**
	 * Gets the memorized page number
	 * @param context current context
	 * @return the page number memorized
	 */
	private static int getPagePreference(Context context) {
		SharedPreferences page = context.getSharedPreferences(NAME, 0);
		int pageNumber = page.getInt(PAGE_NUM_KEY, PAGE_LANGUAGE_SELECT);
		return pageNumber;
	}
	
	/**
	 * Sets the page number to be memorized
	 * @param activity current activity
	 */
	public static void setPagePreference(MeepOpenBoxBaseActivity activity) {
		SharedPreferences page = activity.getSharedPreferences(NAME, 0);
		SharedPreferences.Editor preferencesEditor = page.edit();
		if (activity instanceof MeepOpenBoxLanguageSelect) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_LANGUAGE_SELECT);
		} else if (activity instanceof MeepOpenBoxTimeZoneSelect) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_TIMEZONE);
		} else if (activity instanceof MeepOpenBoxWiFiSetup) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_WIFI_SETUP);
		} else if (activity instanceof MeepOpenBoxParentalSetting) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_PARENTAL_SETTING);
		}
		preferencesEditor.commit();
	}
	
	/**
	 * Sets the next page number to be memorized
	 * @param activity current activity
	 */
	private static void setPagePreferenceToNextPage(
			MeepOpenBoxBaseActivity activity) {
		SharedPreferences page = activity.getSharedPreferences(NAME, 0);
		SharedPreferences.Editor preferencesEditor = page.edit();
		if (activity instanceof MeepOpenBoxLanguageSelect) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_TIMEZONE);
		} else if (activity instanceof MeepOpenBoxTimeZoneSelect) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_WIFI_SETUP);
		} else if (activity instanceof MeepOpenBoxWiFiSetup) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_PARENTAL_SETTING);
		}
		preferencesEditor.commit();
	}
	
	/**
	 * Sets the previous page number to be memorized
	 * @param activity current activity
	 */
	private static void setPagePreferenceToPreviousPage(
			MeepOpenBoxBaseActivity activity) {
		SharedPreferences page = activity.getSharedPreferences(NAME, 0);
		SharedPreferences.Editor preferencesEditor = page.edit();
		if (activity instanceof MeepOpenBoxTimeZoneSelect) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_LANGUAGE_SELECT);
		} else if (activity instanceof MeepOpenBoxWiFiSetup) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_TIMEZONE);
		} else if (activity instanceof MeepOpenBoxParentalSetting) {
			preferencesEditor.putInt(PAGE_NUM_KEY, PAGE_WIFI_SETUP);
		}
		preferencesEditor.commit();
	}
	
	/**
	 * Launches the page next to current one
	 * @param activity current activity
	 */
	public static void goToNextPage(MeepOpenBoxBaseActivity activity) {
		setPagePreferenceToNextPage(activity);
		if (activity instanceof MeepOpenBoxLanguageSelect) {
			startActivity(activity, MeepOpenBoxTimeZoneSelect.class, PAGE_TIMEZONE);
		} else if (activity instanceof MeepOpenBoxTimeZoneSelect) {
			startActivity(activity, MeepOpenBoxWiFiSetup.class, PAGE_WIFI_SETUP);
		} else if (activity instanceof MeepOpenBoxWiFiSetup) {
			startActivity(activity, MeepOpenBoxParentalSetting.class, PAGE_PARENTAL_SETTING);
		}
	}
	
	/**
	 * Launches the page previous to current one
	 * @param activity current activity
	 */
	public static void goToPreviousPage(MeepOpenBoxBaseActivity activity) {
		setPagePreferenceToPreviousPage(activity);
		if (activity instanceof MeepOpenBoxTimeZoneSelect) {
			startActivity(activity, MeepOpenBoxLanguageSelect.class, PAGE_LANGUAGE_SELECT);
		} else if (activity instanceof MeepOpenBoxWiFiSetup) {
			startActivity(activity, MeepOpenBoxTimeZoneSelect.class, PAGE_TIMEZONE);
		} else if (activity instanceof MeepOpenBoxParentalSetting) {
			startActivity(activity, MeepOpenBoxWiFiSetup.class, PAGE_PARENTAL_SETTING);
		}
	}
	
	/**
	 * Starts an activity, reuse it if it is running
	 * @param activity current activity
	 * @param clazz class of new activity
	 * @param pageTag page tag of new activity
	 */
	private static void startActivity(
			MeepOpenBoxBaseActivity activity,
			Class<?> clazz,
			int pageTag) {
		if (activity == null || clazz == null) {
			Log.e(TAG, "Activity cannot be started!");
			return;
		}
		Intent intent = new Intent(activity, clazz);
		// intent.putExtra(PAGE_SWITCH_KEY, true);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		activity.startActivityForResult(intent, pageTag);
	}
	
	/**
	 * Gets an activity by package
	 * @param packageName package string
	 * @return activity of the package specified
	 */
	private static Intent getIntentByPackage(String packageName) {
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(packageName));
		return intent;
	}
	
	/**
	 * Checks whether the current activity is root activity
	 * @param activity current activity
	 * @return true if current activity is root activity, false otherwise
	 */
	public static boolean isPageRoot(MeepOpenBoxBaseActivity activity) {
		if (activity.getIntent().getBooleanExtra(PAGE_SWITCH_KEY, false)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the memorized page
	 * @param activity current activity
	 * @return the page memorized
	 */
	public static Intent getPageInPreference(MeepOpenBoxBaseActivity activity) {
		Intent intent = null;
		switch (getPagePreference(activity)) {
			case PAGE_TIMEZONE:
				intent = getIntentByPackage("com.oregonscientific.meep.meepopenbox/." + MeepOpenBoxTimeZoneSelect.class.getSimpleName());
				break;
			case PAGE_WIFI_SETUP:
				intent = getIntentByPackage("com.oregonscientific.meep.meepopenbox/." + MeepOpenBoxWiFiSetup.class.getSimpleName());
				break;
			case PAGE_PARENTAL_SETTING:
				intent = getIntentByPackage("com.oregonscientific.meep.meepopenbox/." + MeepOpenBoxParentalSetting.class.getSimpleName());
				break;
			default:
				intent = getIntentByPackage("com.oregonscientific.meep.meepopenbox/." + MeepOpenBoxLanguageSelect.class.getSimpleName());
				break;
		}
		return intent;
	}
	
}
