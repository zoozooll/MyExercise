/**
 * 
 */
package com.oregonscientific.bbq.ble;

import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothDevice;

import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.DonenessTemperature;

/**
 * @author aaronli
 *
 */
public interface BbqDeviceController {
	
	public BluetoothDevice lookforDevices(String address);
	
	public boolean pairDevices(String address);

	public void requestChannelData(int channel);
	
	public BbqSettings requestCurrentChannelSettings(int channel);
	
	public void setupBbqChannel(BbqSettings settings);
	
	public void setOperatingStart(int channel, Mode mode);
	
	public void setOperatingStop(int channel);
	
	public void requestCustomDoneness(int meatTypeInt);
	
	public void clearCustomDoneness();
	
	public void setCustomDonenessTemperature(DonenessTemperature donnessTemperature);
}
