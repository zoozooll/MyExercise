/**
 * 
 */
package com.oregonscientific.bbq.ble;

import com.oregonscientific.bbq.bean.Timer;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;

/**
 * @author aaronli
 *
 */
public interface ChannelSettingsChangedCallback {
	

	public void onChannelSettingModeChanged(int ch, Mode preMode, Mode curMode);
	
	public void onChannelSettingMeattypeChanged(int ch, int preTypeIndex, int curTypeIndex);
	
	public void onChannelSettingDonenessChanged(int ch, DonenessLevel preLevel, DonenessLevel curLevel);
	
	public void onChannelSettingTargetChanged(int ch, float preTarget, float curTarget);
	
	public void onChannelSettingTimerChanged(int ch, Timer preMode, Timer curMode);
}
