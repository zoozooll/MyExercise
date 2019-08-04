/**
 * 
 */
package com.oregonscientific.bbq.ble;

import java.util.ArrayList;
import java.util.List;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.CookingStatus;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.bean.Timer;

/**
 * @author aaronli
 *
 */
public class ParseManager {
	
	public static final int[] MEAT_TYPE_ICONS = new int[]{0, R.drawable.hcon_beef,R.drawable.hcon_veal,
		R.drawable.hcon_lamb,R.drawable.hcon_pork,R.drawable.hcon_chicken,R.drawable.hcon_turkey,
		R.drawable.hcon_fish,R.drawable.hcon_hamburger};
	
	/**
	 * translate the temperature degree from Fahrenheit to Celsius
	 * @param degreeF degree Fahrenheit value
	 * @return degree celsius value
	 */
	public static float tranFahrenheitToCelsius(float degreeF) {
		return degreeF/1.8f - 17.8f;
	}
	
	/**
	 * translate the temperature degree from Celsius to Fahrenheit
	 * @param degreeC degree celsius value
	 * @return degree fahrenheit value
	 */
	public static float tranCelsiusToFahrenheit(float degreeC) {
		return (degreeC + 17.8f) * 1.8f;
	}
	
	/**
     * Convert a signed byte to an unsigned int.
     */
    private static  int unsignedByteToInt(byte b) {
        return b & 0xFF;
    }

    /**
     * Convert signed bytes to a 16-bit unsigned int.
     */
    private static int unsignedBytesToInt(byte b0, byte b1) {
        return (unsignedByteToInt(b0) + (unsignedByteToInt(b1) << 8));
    }
    
    
    /**
     * return the channel informations. if return null that is wrong 
     * @param bytes
     * @param result The result,need to general before the method called.
     * @return the result, it is the same object as param result. If it is empty that is wrong.
     */
	public static BBQDataSet parseChannelInfo(byte[] bytes, final BBQDataSet result) {
		if (bytes.length < 16 || result == null) {
			return null;
		}
		// parsing bytes[0]
		boolean bFlag =  (bytes[0] & 0xf0) != 0;
		result.setLowBattery(bFlag);
		bFlag = (bytes[0] & 0x20) != 0;
		result.setNormal(bFlag);
		int iFlag = (bytes[0] & 0x03);
		result.setMode(Mode.get(iFlag));
		iFlag = (bytes[0] & 0x1C) >> 2;
		result.setStatus(CookingStatus.get(iFlag));
		// parsing bytes[1]
		if (bytes[1]>= 1 && bytes[1] <= 8) {
			result.setMeatTypeInt(bytes[1]);
		}
		// parsing bytes[2]
		result.setDonelessLevel(DonenessLevel.get(bytes[2]));
		// parsing bytes[3] ~ [5]
		Timer timerFlag = new Timer(bytes[3], bytes[4], bytes[5]);
		result.setReloadTimer(timerFlag);
		// parsing bytes[6] ~ [8]
		timerFlag = new Timer(bytes[6], bytes[7], bytes[8]);
		result.setCurrentTimer(timerFlag);
		// parsing bytes[9]
		result.setUpCountTimer(bytes[9] != 0 ) ;
		// parsing bytes[10] ~ [11]
		result.setTargetTemperature(unsignedBytesToInt(bytes[10], bytes[11]) / 10.f);
		// parsing bytes[12] ~ [13]
		result.setProbeTemperature(unsignedBytesToInt(bytes[12], bytes[13]) / 10.f);
		// parsing bytes[14] ~ [15]
		int percentage = unsignedBytesToInt(bytes[14], bytes[15]);
		result.setPercentage((percentage >= 0 && percentage < 9999) ? percentage : 0);
		return result;
	}
	
	/**
	 * 
	 * @param bytes
	 * @return 1 means that channel1, 2 means channel2, other is error message string'id
	 */
	public static int parseChannelDataResend (byte[] bytes){
		if (bytes.length < 2) {
			return R.string.err_parse_bytes;
		}
		switch (bytes[1]) {
		case 1:
		case 2:
			return bytes[1];
		default:
			return R.string.err_others;
		}
	}
	
	/**
	 * 
	 * @param bytes
	 * @return the value is 0 means success, if the value is another means that it is id of error string
	 */
	public static int parseSetupBbqChannel(byte[] bytes){
		if (bytes.length < 2) {
			return R.string.err_parse_bytes;
		}
		switch (bytes[1]) {
		case 0:
			break;
		case 1:
			return R.string.err_setup;
		default:
			return R.string.err_others;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param start if true, it is start, if false it is stop.
	 * @param channel 
	 * @param bytes
	 * @return the value is 0 means success, if the value is another means that it is id of error string
	 */
	public static int parseOperatingState(boolean start,int channel,  byte[] bytes) {
		if (bytes.length < 2) {
			return R.string.err_parse_bytes;
		}
		if (start) {
			switch (bytes[2]) {
			case 0:
				break;
			case 1:
				return R.string.err_startoperating_1;
			case 2:
				return R.string.err_startoperating_2;
			case 3:
				return R.string.err_startoperating_3;
			case 4:
				return R.string.err_startoperating_insetting;
			case 5:
				return R.string.err_startoperating_neverset;
			case 6:
				return R.string.err_startoperating_noprobee;
			default:
				return R.string.err_others;
			}
		} else {
			switch (bytes[1]) {
			case 1:
			case 2:
			case 3:
				return bytes[1];
			case 4:
				return R.string.err_stop_already;
			default:
				return R.string.err_others;
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static DonenessTemperature parseCustomDoneness(byte[] bytes) {
		/*if (bytes.length != 3 || (bytes.length - 1) % 11 != 0) {
			return null;
		}*/
		DonenessTemperature item = null;
		if (bytes.length == 2) {
			//for (int i = 1; i<=8; i++) {
			item = new DonenessTemperature();
			item.setMeatTypeIndex(bytes[1]);
			//}
		} else if (bytes.length == 12) {
			item = new DonenessTemperature(bytes[1],
				unsignedBytesToInt(bytes[2], bytes[3]), 
				unsignedBytesToInt(bytes[4], bytes[5]), 
				unsignedBytesToInt(bytes[6], bytes[7]),
				unsignedBytesToInt(bytes[8], bytes[9]), 
				unsignedBytesToInt(bytes[10], bytes[11]),
				true);
		}
		return item;
	}
	
	/**
	 * parse the clear custom doneness result
	 * @param bytes
	 * @return 0 means success, another number means the id of error string
	 */
	public static int parseClearCustomDoneness(byte[] bytes) {
		if (bytes.length < 2) {
			return R.string.err_parse_bytes;
		}
		switch (bytes[1]) {
		case 0:
			break;
		case 1:
			return R.string.err_setcustom;
		default:
			return R.string.err_others;
		}
		return 0;
	}
	
	/**
	 * parse the set custom doneness result
	 * @param bytes
	 * @return 0 means success, another number means the id of error string
	 */
	public static int parseSetCustomDoneness(byte[] bytes) {
		if (bytes.length < 2) {
			return R.string.err_parse_bytes;
		}
		switch (bytes[1]) {
		case 0:
			break;
		case 1:
			return R.string.err_setcustom;
		default:
			return R.string.err_others;
		}
		return 0;
	}
	
	public static BbqSettings dataToSetting(BBQDataSet data) {
		BbqSettings s = new BbqSettings();
		s.setCurrentTimer(data.getCurrentTimer());
		s.setDonenessLevel(data.getDonelessLevel());
		s.setMeatTypeInt(data.getMeatTypeInt());
		s.setReloadTimer(data.getReloadTimer());
		//s.setCurrentTimer(data.getCurrentTimer());
		s.setTargetTemperature(data.getTargetTemperature());
		s.setUpCountTimer(data.isUpCountTimer());
		return s;
	}
}
