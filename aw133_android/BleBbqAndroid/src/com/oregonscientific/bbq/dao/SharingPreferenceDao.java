/**
 * 
 */
package com.oregonscientific.bbq.dao;

import com.oregonscientific.bbq.utils.BbqConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author aaronli
 *
 */
public class SharingPreferenceDao {
	
	private static final String  SP_NAME = "IDT_AW133_BBQ";
	
	private static final String KEY_ADDRESS = "last_address";
	private static final String KEY_SCAN_SWITCH = "last_scan_switch";
	private static final String KEY_TEMPERATURE_UNIT = "temperature_unit";
	private static SharingPreferenceDao sInstance;
	
	private SharedPreferences msp ;
	
	private SharingPreferenceDao(Context context) {
		msp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
	}
	
	public static SharingPreferenceDao getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SharingPreferenceDao(context);
		}
		return sInstance;
	}
	
	public void setLastConnectDeviceAddress(String address) {
		if (!TextUtils.equals(getLastConnectDeviceAddress(), address)) {
			msp.edit().putString(KEY_ADDRESS, address).commit();
		}
	}
	
	public String getLastConnectDeviceAddress() {
		return msp.getString(KEY_ADDRESS, "");
	}
	
	public void setKeyValue(String key, String value) {
		msp.edit().putString(key, value).commit();
	}
	
	public String getKeyValue(String key, String defaultValue) {
		return msp.getString(key, defaultValue);
	}
	
	public boolean isLastScanSwitch(){
		return msp.getBoolean(KEY_SCAN_SWITCH, true);
	}
	
	public void setLastScanSwitch(boolean b) {
		if (isLastScanSwitch() != b)
			msp.edit().putBoolean(KEY_SCAN_SWITCH, b).commit();
	}
	
	public String getShowingTemperatureUnit() {
		return msp.getString(KEY_TEMPERATURE_UNIT, BbqConfig.TEMPERATURE_UNIT_F);
	}
	
	public void setShowingTemperatureUnit(String unit) {
		msp.edit().putString(KEY_TEMPERATURE_UNIT, unit).commit();
	}
}
