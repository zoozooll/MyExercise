/**
 * 
 */
package com.oregonscientific.bbq.utils;

import java.util.HashMap;
import java.util.Map;

import com.oregonscientific.bbq.bean.DonenessTemperature;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author aaronli
 *
 */
public class BbqConfig {
	

	public static final String TEMPERATURE_UNIT_F = "F";
	public static final String TEMPERATURE_UNIT_C = "C";
	
	public static DonenessTemperature donenessDefBeef = new DonenessTemperature(1, 135f, 145f, 160f, 0, 170f);
	public static DonenessTemperature donenessDefVeal = new DonenessTemperature(2, 135f, 145f, 160f, 0, 170f);
	public static DonenessTemperature donenessDefLamb = new DonenessTemperature(3, 135f, 145f, 160f, 0, 170f);
	public static DonenessTemperature donenessDefPork = new DonenessTemperature(4, 0, 145f, 160f, 0, 170f);
	public static DonenessTemperature donenessDefChecken = new DonenessTemperature(5, 0, 0f, 0, 0, 180f);
	public static DonenessTemperature donenessDefTurkey = new DonenessTemperature(6, 0, 0f, 0, 0, 180f);
	public static DonenessTemperature donenessDefFish = new DonenessTemperature(6, 0, 0f, 0, 0, 145f);
	public static DonenessTemperature donenessDefHamburger = new DonenessTemperature(6, 0, 0f, 0, 0, 160f);
	
	public static DonenessTemperature donenessInitialBeef = new DonenessTemperature(1, 135f, 140f, 145f, 160f, 170f, true);
	public static DonenessTemperature donenessInitialVeal = new DonenessTemperature(2, 135f, 140f, 145f, 160, 170f, true);
	public static DonenessTemperature donenessInitialLamb = new DonenessTemperature(3, 135f, 140f, 145f, 160f, 170f, true);
	public static DonenessTemperature donenessInitialPork = new DonenessTemperature(4, 0, 145f, 145f, 160, 170f, true);
	public static DonenessTemperature donenessInitialChecken = new DonenessTemperature(5, 0, 0f, 0, 0, 180f, true);
	public static DonenessTemperature donenessInitialTurkey = new DonenessTemperature(6, 0, 0f, 0, 0, 180f, true);
	public static DonenessTemperature donenessInitialFish = new DonenessTemperature(6, 0, 0f, 0, 0, 145f, true);
	public static DonenessTemperature donenessInitialHamburger = new DonenessTemperature(6, 0, 0f, 0, 0, 160f, true);
	
	public static SparseArray<DonenessTemperature> MAP_MEATTYPE_INDEX = new SparseArray<DonenessTemperature>();
	public static SparseArray<DonenessTemperature> MAP_MEATTYPE_INITIAL_INDEX = new SparseArray<DonenessTemperature>();
	
	// Added bbqversion from bbq device.
	public static String sBbqVersion;
	
	static {
		
		MAP_MEATTYPE_INDEX.put(1, donenessDefBeef);
		MAP_MEATTYPE_INDEX.put(2, donenessDefVeal);
		MAP_MEATTYPE_INDEX.put(3, donenessDefLamb);
		MAP_MEATTYPE_INDEX.put(4, donenessDefPork);
		MAP_MEATTYPE_INDEX.put(5, donenessDefChecken);
		MAP_MEATTYPE_INDEX.put(6, donenessDefTurkey);
		MAP_MEATTYPE_INDEX.put(7, donenessDefFish);
		MAP_MEATTYPE_INDEX.put(8, donenessDefHamburger);
		
		MAP_MEATTYPE_INITIAL_INDEX.put(1, donenessInitialBeef);
		MAP_MEATTYPE_INITIAL_INDEX.put(2, donenessInitialVeal);
		MAP_MEATTYPE_INITIAL_INDEX.put(3, donenessInitialLamb);
		MAP_MEATTYPE_INITIAL_INDEX.put(4, donenessInitialPork);
		MAP_MEATTYPE_INITIAL_INDEX.put(5, donenessInitialChecken);
		MAP_MEATTYPE_INITIAL_INDEX.put(6, donenessInitialTurkey);
		MAP_MEATTYPE_INITIAL_INDEX.put(7, donenessInitialFish);
		MAP_MEATTYPE_INITIAL_INDEX.put(8, donenessInitialHamburger);
	}
	
	/**
	 * confirm whether the bbq device is old firmwire.
	 * @author aaronli at Mar 10 2014
	 * @return
	 */
	public static boolean isOldFirmwire() {
		if (!TextUtils.isEmpty(sBbqVersion)) {
			final String flag = BbqConfig.sBbqVersion.substring(1);
			final int versionNum = Integer.parseInt(flag);
			if (versionNum <= 24) {
				return true;
			}
		}
		return false;
	}
	
	
}
