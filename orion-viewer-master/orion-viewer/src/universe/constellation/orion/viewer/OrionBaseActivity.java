/*
 * Orion Viewer - pdf, djvu, xps and cbz file viewer for android devices
 *
 * Copyright (C) 2011-2013  Michael Bogdanov & Co
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package universe.constellation.orion.viewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import universe.constellation.orion.viewer.prefs.GlobalOptions;
import universe.constellation.orion.viewer.prefs.OrionApplication;

/**
 * User: mike
 * Date: 24.12.11
 * Time: 17:00
 */
public class OrionBaseActivity extends ActionBarActivity {

    public static final String DONT_OPEN_RECENT = "DONT_OPEN_RECENT";

    protected Device device ;

    protected SharedPreferences.OnSharedPreferenceChangeListener listener;

    public OrionBaseActivity() {
        if (supportDevice()) {
            device = Common.createDevice();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getOrionContext().applyTheme(this, getViewerType() == Device.VIEWER_ACTIVITY);
        getOrionContext().updateLanguage(getResources());

        if (this instanceof OrionViewerActivity || this instanceof OrionFileManagerActivity) {
            int screenOrientation = getScreenOrientation(getApplicationDefaultOrientation());
            changeOrientation(screenOrientation);
        }

        super.onCreate(savedInstanceState);

        if (device != null) {
            device.onCreate(this);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (device != null) {
            device.onDestroy();
        }
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if (device != null) {
//            device.onResume();
//        }
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (device != null) {
                device.onWindowGainFocus();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (device != null) {
            device.onPause();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        if (device != null) {
            device.onUserInteraction();
        }
    }

    public Device getDevice() {
        return device;
    }

    public View getView() {
        return null;
    }

    public int getViewerType() {
        return Device.DEFAULT_ACTIVITY;
    }

    protected View findMyViewById(int id) {
        return findViewById(id);
    }

    protected void onAnimatorCancel() {

    }

    protected void onApplyAction() {

    }

    public boolean supportDevice() {
        return true;
    }

    public OrionApplication getOrionContext() {
        return (OrionApplication) getApplicationContext();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if (device != null) {
            device.onSetContentView();
        }
    }


    public void showWarning(String warning) {
        Toast.makeText(this, warning, Toast.LENGTH_SHORT).show();
    }

    public void showWarning(int stringId) {
        showWarning(getResources().getString(stringId));
    }

    public void showFastMessage(int stringId) {
        showWarning(getResources().getString(stringId));
    }

    public void showLongMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void showFastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showError(Exception e) {
        showError("Error", e);
    }

    public void showError(String error, Exception ex) {
        Toast.makeText(this, error + ": " + ex.getMessage(), Toast.LENGTH_LONG).show();
        Common.d(ex);
    }

    class Localizer {
        private Resources res;

        public Localizer(Resources res) {
            this.res = res;
        }
        public String get(String key) {
            try {
                int id = res.getIdentifier(key, "string", "universe.constellation.orion.viewer");
                return res.getString(id);
            } catch (Exception e) {
                return key;
            }
        }
    }

    public void changeOrientation(int orientationId) {
        System.out.println("Display orientation "+ getRequestedOrientation() + " screenOrientation " + getWindow().getAttributes().screenOrientation);
        if (getRequestedOrientation() != orientationId) {
            setRequestedOrientation(orientationId);
        }
    }

    public int getScreenOrientation(String id) {
        int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        if ("LANDSCAPE".equals(id)) {
            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else if ("PORTRAIT".equals(id)) {
            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else if ("LANDSCAPE_INVERSE".equals(id)) {
            screenOrientation = getOrionContext().getSdkVersion() < 9 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : 8;
        } else if ("PORTRAIT_INVERSE".equals(id)) {
            screenOrientation = getOrionContext().getSdkVersion() < 9 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : 9;
        }
        return screenOrientation;
    }

    public int getScreenOrientationItemPos(String id) {
        int screenOrientation = 0;
        if ("LANDSCAPE".equals(id)) {
            screenOrientation = 2;
        } else if ("PORTRAIT".equals(id)) {
            screenOrientation = 1;
        } else if ("LANDSCAPE_INVERSE".equals(id)) {
            screenOrientation = getOrionContext().getSdkVersion() < 9 ? 2 : 4;
        } else if ("PORTRAIT_INVERSE".equals(id)) {
            screenOrientation = getOrionContext().getSdkVersion() < 9 ? 1 : 3;
        }
        return screenOrientation;
    }

    public String getApplicationDefaultOrientation() {
        return getOrionContext().getOptions().getStringProperty(GlobalOptions.SCREEN_ORIENTATION, "DEFAULT");
    }

    public void showAlert(String title, String message) {
        AlertDialog.Builder builder = createThemedAlertBuilder();
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public void showAlert(int titleId, int messageId) {
        AlertDialog.Builder builder = createThemedAlertBuilder();
        builder.setTitle(titleId);
        builder.setMessage(messageId);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public Dialog createThemedDialog() {
        int sdkVersion = getOrionContext().getSdkVersion();
        if (sdkVersion >= 14) {
            return new Dialog(this);
        } else {
            boolean isLightTheme = getOrionContext().isLightTheme();
            return new Dialog(this, isLightTheme ? R.style.dialog_light : R.style.dialog_dark);
        }
    }

    public AlertDialog.Builder createThemedAlertBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int sdkVersion = getOrionContext().getSdkVersion();
        if (sdkVersion < 14) {
            boolean isLightTheme = getOrionContext().isLightTheme();
            if (isLightTheme) {
                builder.setInverseBackgroundForced(true);
            }
        }
        return builder;
    }

    public boolean isLevel5ApiEnabled() {
        return getOrionContext().getSdkVersion() >= 5;
    }

    protected boolean doTrack(int keyCode) {
        return keyCode != KeyEvent.KEYCODE_MENU && keyCode != KeyEvent.KEYCODE_BACK;
    }
}
