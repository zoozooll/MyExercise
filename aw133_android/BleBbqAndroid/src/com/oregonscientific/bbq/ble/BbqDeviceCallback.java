/**
 * 
 */
package com.oregonscientific.bbq.ble;

import java.util.List;

import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.bean.Timer;

/**
 * @author aaronli
 *
 */
public interface BbqDeviceCallback {
 
	public void onDeviceJoin();
	
	public void replyChannelDataResend(int channel,  BBQDataSet value);
	
	public void replySetupBbqChanged(String msg);
	
	public void indiacateStart(int channel, String msg);
	
	public void indiacateStop(int channel, String msg);
	
	public void onDisconnected();
}
