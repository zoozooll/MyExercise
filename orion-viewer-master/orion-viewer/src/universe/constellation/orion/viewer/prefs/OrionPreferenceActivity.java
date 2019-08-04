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

package universe.constellation.orion.viewer.prefs;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import universe.constellation.orion.viewer.R;
import universe.constellation.orion.viewer.device.EInkDevice;

/**
 * User: mike
 * Date: 02.01.12
 * Time: 17:35
 */
public class OrionPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getOrionContext().applyTheme(this);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferences);

        PreferenceScreen screen = getPreferenceScreen();

        PreferenceCategory eInkDevice = (PreferenceCategory) screen.findPreference("NOOK2_EINK");

        if (!(getOrionContext().getDevice() instanceof EInkDevice)) {
            screen.removePreference(eInkDevice);
        }

        ListPreference SCREEN_ORIENTATION = (ListPreference) findPreference("SCREEN_ORIENTATION");

        if (getOrionContext().getSdkVersion() < 9) {
            SCREEN_ORIENTATION.setEntries(getResources().getTextArray(R.array.screen_orientation_desc));
            SCREEN_ORIENTATION.setEntryValues(getResources().getTextArray(R.array.screen_orientation));
        }
    }

//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//        super.onPreferenceTreeClick(preferenceScreen, preference);
//        if (preference != null) {
//            if (preference instanceof PreferenceScreen)
//                if (((PreferenceScreen) preference).getDialog() != null)
//                    ((PreferenceScreen) preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());
//
//            if (preference instanceof DialogPreference)
//                if (((DialogPreference) preference).getDialog() != null)
//                    ((DialogPreference) preference).getDialog().getWindow().getDecorView().setBackgroundDrawable(this.getWindow().getDecorView().getBackground().getConstantState().newDrawable());
//        }
//        return false;
//    }
//

    public OrionApplication getOrionContext() {
        return (OrionApplication) getApplicationContext();
    }


}
