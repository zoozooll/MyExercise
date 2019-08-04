/**
 * 
 */
package com.oregonscientific.bbq.ble;

import java.util.List;

import com.oregonscientific.bbq.bean.DonenessTemperature;

/**
 * @author aaronli
 *
 */
public interface BbqDonenessCallback {
	
	public void replyDefaultDoneness();
	
	public void replyCustomDoneness(DonenessTemperature values);
	
	public void indiacateClearCustomDoneness(String msg);
	
	public void replySetCustomDoneness(String msg);
	
	public void onSetupCustomDonenessSent(int meatTypeIndex);
}
