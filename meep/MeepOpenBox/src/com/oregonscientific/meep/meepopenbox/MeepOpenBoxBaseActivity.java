package com.oregonscientific.meep.meepopenbox;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxViewManager;
import com.oregonscientific.meep.util.SystemUtils;

/**
 * Base activity defining basic actions to be performed by all activities
 * @author Charles
 */
public class MeepOpenBoxBaseActivity extends Activity {
	
	private final String TAG = MeepOpenBoxBaseActivity.class.getSimpleName();
	public static final String SPLASH_SCREEN_ACTIVITY_PACKAGE_NAME = "com.oregonscientific.meep.together/.activity.MeepTogetherSplashScreen";
	public static final String HOME_ACTIVITY_PACKAGE_NAME = "com.oregonscientific.meep.home/.HomeActivity";

	@Override
	public void onStart() {
		super.onStart();
		
		try {
			if (!SystemUtils.isSystemRestored(this)) {
				startActivityByPackage(HOME_ACTIVITY_PACKAGE_NAME);
				quitOpenBox();
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot access already ran file because: " + ex.toString());
		}
	}
	
	@Override
	public void onBackPressed() {
		MeepOpenBoxViewManager.goToPreviousPage(this);
	}
	
	/**
	 * Hides back button
	 */
	protected void hideBackButton() {
	}
	
	/**
	 * Sets next button enabled or not
	 */
	public void setNextButtonEnabled(boolean enabled) {
	}
	
	/**
	 * {@link MeepOpenBoxViewManager#clearPreferences}
	 */
	public void clearPreferences() {
		MeepOpenBoxViewManager.clearPreferences(this);
	}
	
	/**
	 * Starts an activity by package
	 * @param context context
	 * @param packageName package string
	 */
	public void startActivityByPackage(String packageName) {
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(packageName));
		try {
			startActivity(intent);
		} catch (Exception ex) {
			Toast.makeText(this, intent.getComponent().getClassName() + " not found!", Toast.LENGTH_LONG).show();
			Log.e(TAG, intent.getComponent().getClassName() + " cannot be started because:" + ex.toString());
		}
	}
	
	/**
	 * Finishes all activities and quit open box
	 */
	public void quitOpenBox() {
		setResult(Activity.RESULT_OK);
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
}
