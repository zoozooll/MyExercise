/**
 * 
 */
package com.oregonscientific.bbq.ble;

import static com.oregonscientific.bbq.ble.SampleGattAttributes.*;
import static android.bluetooth.BluetoothGattCharacteristic.*;
import static com.oregonscientific.bbq.ble.ParseManager.*;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.oregonscientific.bbq.BuildConfig;
import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.act.NotificationController;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.ble.Command.CommandType;
import com.oregonscientific.bbq.ble.CommandManager.BlueConnectState;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.utils.BbqConfig;

/**
 * @author aaronli
 *
 */
public class BleBbqCommandManager extends CommandManager implements BbqDeviceController {
	
	private static final String TAG = "BleBbqCommandManager";
	private static final long SCAN_PERIOD = 15000;
	
	private static BleBbqCommandManager instance;

    private BbqDeviceCallback mBbqDeviceCallback;
    private ChannelSettingsChangedCallback mChannelSettingsChangedCallback;
    private BbqDonenessCallback mDonenessCallback;
    private OnBleScanningCallback mOnBleScanningCallback;
    
    private BluetoothGattCharacteristic characteristicCommand;
    private BluetoothGattCharacteristic bbqCharacterisCH1 ;
    private BluetoothGattCharacteristic bbqCharacterisCH2 ;
    private BluetoothGattCharacteristic characteristicBbqBuildVersion;
    
    private BBQDataSet dataSetCh1;
    private BBQDataSet dataSetCh2;
    private BBQDataSet preDataSetCh1;
    private BBQDataSet preDataSetCh2;

	private Handler mHandler;
    
    
	public static BleBbqCommandManager getInstance(ContextWrapper context) {
		if (instance == null) {
			synchronized (BleBbqCommandManager.class) {
				if (instance == null) {
					instance = new BleBbqCommandManager(context);
				}
			}
		}
		return instance;

	}
    /**
	 * 
	 */
	private  BleBbqCommandManager(ContextWrapper context) {
		super(context);
		mHandler = new Handler();
	}
	
	public static void clear() {
		instance = null;
	}

    
    protected void descriptorWriteSuccess(BluetoothGattDescriptor descriptor) {
		if (descriptor.getCharacteristic().getUuid().equals(UUID_CHARACTERISTIC_CH2INFO)) {
			
			if (mBbqDeviceCallback != null) {
				mBbqDeviceCallback.onDeviceJoin();
			}
		}
	}
    
    protected void onReadMessages(BluetoothGattCharacteristic characteristic) {
    	if (characteristic.getUuid().equals(UUID.fromString(UUIDSTR_CHARACTERISTIC_BBQ_VERSION))) {
    		BbqConfig.sBbqVersion = characteristic.getStringValue(0);
    	}
    }
    
    protected void notifyCharacteristicChanged(
			BluetoothGattCharacteristic characteristic) {
    	final UUID uuid = characteristic.getUuid();
		final byte[] data = characteristic.getValue();
		if (characteristic.equals(characteristicCommand)) {
			logForBytesHex("notifyCharacteristicChanged", characteristic, data);
		}
    	
    	if (UUID_CHARACTERISTIC_CH1INFO.equals(uuid)) {
    		onNotifyChannelInfo(1, data);
    	} else if (UUID_CHARACTERISTIC_CH2INFO.equals(uuid)) {
    		onNotifyChannelInfo(2, data);
    	} else if (UUID_CHARACTERISTIC_COMMAND.equals(uuid)) {
    		onNotifyCommand(data);
    	}
	}
    
    protected void onDisconnected() {
		
//		Log.i(TAG, "Disconnected from GATT server.");
		if(mBbqDeviceCallback != null) {
			mBbqDeviceCallback.onDisconnected();
		}
		// stop notifying when disconnected;
		NotificationController notify = NotificationController.getInstance(mContextWrapper);
		notify.forceStopNotify();
	}

	protected void onConnectedSuccess() {
		
	}
	
	protected void onConnectFailed() {
		if(mBbqDeviceCallback != null) {
			mBbqDeviceCallback.onDisconnected();
		}
	}
    
	protected void initCharacteristics(BluetoothGatt gatt) {
		BluetoothGattService serviceInformations = gatt.getService(UUID.fromString(UUIDSTR_SERVICE_INFORMATIONS));
		characteristicBbqBuildVersion = serviceInformations.getCharacteristic(UUID.fromString(UUIDSTR_CHARACTERISTIC_BBQ_VERSION));
    	 BluetoothGattService serviceAdvertise = gatt.getService(UUID_SERVICE_ADVERTISE);
    	 if (serviceAdvertise == null) {
        	 connect(mBluetoothDeviceAddress);
        	 return;
         } 
         characteristicCommand = serviceAdvertise.getCharacteristic(UUID_CHARACTERISTIC_COMMAND);
         bbqCharacterisCH1 = serviceAdvertise.getCharacteristic(UUID_CHARACTERISTIC_CH1INFO);
         bbqCharacterisCH2 = serviceAdvertise.getCharacteristic(UUID_CHARACTERISTIC_CH2INFO);
         if (characteristicCommand == null || bbqCharacterisCH1 == null || bbqCharacterisCH2 == null) {
        	 connect(mBluetoothDeviceAddress);
         } else {
        	 startControll();
         }
    }
	
    
    private void onNotifyChannelInfo(int channel,  byte[] bytes) {
    	BBQDataSet result = null;
    	BBQDataSet preDataSet = null;
    	if (channel == 1) {
    		if (dataSetCh1 == null) {
    			dataSetCh1 = new BBQDataSet();
    		}
    		result = dataSetCh1; 
    	/*	if (preDataSetCh1 == null) {
    			try {
					preDataSetCh1 = result.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
    		}*/
    		preDataSet = preDataSetCh1;
    	} else if (channel == 2) {
    		if (dataSetCh2 == null) {
    			dataSetCh2 = new BBQDataSet();
    		}
    		result = dataSetCh2; 
    		/*if (preDataSetCh2 == null) {
    			try {
					preDataSetCh2 = result.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
    		}*/
    		preDataSet = preDataSetCh2;
    	}
    	result = ParseManager.parseChannelInfo(bytes, result);
    	// added by aaron at Mar 5 2014 for notification on title bar.
    	NotificationController notify = NotificationController.getInstance(mContextWrapper);
    	notify.showDonenessLevelNotify(channel, result.getStatus());
    	
    	if (channel == 1) {
    		if (preDataSetCh1 == null) {
    			try {
					preDataSetCh1 = result.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
    		}
    	} else if (channel == 2) {
    		if (preDataSetCh2 == null) {
    			try {
					preDataSetCh2 = result.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
    		}
    	}
    	//Log.d(TAG, "result " +channel + ":" + result);
    	if (mBbqDeviceCallback != null) {
    		mBbqDeviceCallback.replyChannelDataResend(channel, result);
    		/*if (preDataSet == null) {
    			try {
					preDataSet = result.copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
    		}*/
    	}
    	if (preDataSet != null && result != null && !result.equals(preDataSet)) {
			if (!result.getMode().equals(preDataSet.getMode())) {
				if (mChannelSettingsChangedCallback != null)
					mChannelSettingsChangedCallback
							.onChannelSettingModeChanged(channel,
									preDataSet.getMode(), result.getMode());
			}
			if (result.getMeatTypeInt() != (preDataSet.getMeatTypeInt())) {
				if (mChannelSettingsChangedCallback != null)
					mChannelSettingsChangedCallback
							.onChannelSettingMeattypeChanged(channel,
									preDataSet.getMeatTypeInt(),
									result.getMeatTypeInt());
			}
			if (!result.getDonelessLevel()
					.equals(preDataSet.getDonelessLevel())) {
				
				if (mChannelSettingsChangedCallback != null)
					mChannelSettingsChangedCallback
							.onChannelSettingDonenessChanged(channel,
									preDataSet.getDonelessLevel(),
									result.getDonelessLevel());
			}
			if (result.getTargetTemperature() != (preDataSet
					.getTargetTemperature())) {
				if (mChannelSettingsChangedCallback != null)
					mChannelSettingsChangedCallback
							.onChannelSettingTargetChanged(channel,
									preDataSet.getTargetTemperature(),
									result.getTargetTemperature());
			}
			if (!result.getReloadTimer().equals(preDataSet.getReloadTimer())) {
				if (mChannelSettingsChangedCallback != null)
					mChannelSettingsChangedCallback
							.onChannelSettingTimerChanged(channel,
									preDataSet.getReloadTimer(),
									result.getReloadTimer());
			}
    		
    		if (channel == 1) {
    			try {
    				preDataSetCh1 = result.copy();
    			} catch (CloneNotSupportedException e) {
    				e.printStackTrace();
    			}
    		} else if (channel == 2) {
    			try {
    				preDataSetCh2 = result.copy();
    			} catch (CloneNotSupportedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    }
    
    /**
	 * @param data
	 */
	private void onNotifyCommand(final byte[] data) {
		byte commandByte = data[0];
		String errorMsg = null;
		switch (commandByte) {
		case COMMANDBYTE_REQUEST_CHANNEL_DATA_RESEND: {
			
			ParseManager.parseChannelDataResend(data);
			/*if (mBbqDeviceCallback != null) {
				mBbqDeviceCallback.replyChannelDataResend(channel, value)
			}*/
			break;
		}
		case COMMANDBYTE_SETUP_BBQ_CHANNEL: {
			int resultCode = ParseManager.parseSetupBbqChannel(data);
			if (resultCode != 0) {
				errorMsg = mContextWrapper.getResources().getString(resultCode);
			}
			if (mBbqDeviceCallback != null) {
				mBbqDeviceCallback.replySetupBbqChanged(errorMsg);
			}
			break;
		}
		case COMMANDBYTE_START_OPERATION: {
			int channel = data[1];
			int resultCode = ParseManager.parseOperatingState(true, channel, data);
			if (resultCode != 0) {
				errorMsg = mContextWrapper.getResources().getString(resultCode);
			}
			if (mBbqDeviceCallback != null) {
				mBbqDeviceCallback.indiacateStart(channel, errorMsg);
			}
			break;
		}
		case COMMANDBYTE_STOP_OPERATION: {
			int channel = data[1];
			int resultCode = ParseManager.parseOperatingState(false,channel, data);
			if (resultCode < 1 && resultCode >3) {
				errorMsg = mContextWrapper.getResources().getString(resultCode); 
			}
			if (mBbqDeviceCallback != null) {
				mBbqDeviceCallback.indiacateStop(channel, errorMsg);
			}
			break;
		}
		
		case COMMANDBYTE_REQUEST_CUSTOM_DONENESS: {
			if (data[1] == 15) {
				if (mDonenessCallback != null) {
					mDonenessCallback.replyDefaultDoneness();
				}
			}
			try {
				DonenessTemperature dt = ParseManager.parseCustomDoneness(data);
				if (mDonenessCallback != null) {
					mDonenessCallback.replyCustomDoneness(dt);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		case COMMANDBYTE_CLEAR_CUSTOM_DONENESS: {
			int resultCode = ParseManager.parseClearCustomDoneness(data);
			if (resultCode != 0) {
				errorMsg = mContextWrapper.getResources().getString(resultCode);
			}
			if (mDonenessCallback != null) {
				mDonenessCallback.indiacateClearCustomDoneness(errorMsg);
			}	
			break;
		}
		case COMMANDBYTE_SET_CUSTOM_DONENESS: {
			int resultCode = ParseManager.parseSetCustomDoneness(data);
			if (resultCode != 0) {
				errorMsg = mContextWrapper.getResources().getString(resultCode);
			}
			if (mDonenessCallback != null) {
				mDonenessCallback.replySetCustomDoneness(errorMsg);
			}	
			break;
		}
		default:
			break;
		}
	}
	
	private void onSetupCustomDonenessSent(byte[] value ) {
		//requestChannelData(value[1]);
		if (mDonenessCallback != null) {
			mDonenessCallback.onSetupCustomDonenessSent(value[1]);
		}
	}

	
    
    private void startControll() {
    	final int charaProp = characteristicCommand.getProperties();
    	if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
//    		setCharacteristicNotification(
//        		  characteristicCommand, true);
    		Command c = new Command();
    		c.setCharacteristic(characteristicBbqBuildVersion);
    		c.setType(CommandType.READ);
    		putCommand(c);
    		c = new Command();
    		c.setCharacteristic(characteristicCommand);
    		c.setType(CommandType.INDIACATION);
    		putCommand(c);
    		c = new Command();
    		c.setCharacteristic(bbqCharacterisCH1);
    		c.setType(CommandType.INDIACATION);
    		putCommand(c);
    		c = new Command();
    		c.setCharacteristic(bbqCharacterisCH2);
    		c.setType(CommandType.INDIACATION);
    		putCommand(c);
    	}
    }

   /* private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

       
        sendBroadcast(intent);
    }*/

    public void scanLeDevice(final boolean enable) {
//		Log.v(TAG, "scanLeDevice " + enable);
		// modified by aaronli at Mar 25 2014
		if (mBluetoothAdapter == null) {
			return;
		}
        if (enable) {
			// Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(stopScanningCallback, SCAN_PERIOD);
            updateConnectionState(BlueConnectState.SCANNING);
            mBluetoothAdapter.startLeScan( mLeScanCallback);
            if (mOnBleScanningCallback != null) {
            	mOnBleScanningCallback.onScaningStateChanged(BlueConnectState.SCANNING);
            }
        } else {
        	updateConnectionState(BlueConnectState.SCAN_CANCEL);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            if (mOnBleScanningCallback != null) {
            	mOnBleScanningCallback.onScaningStateChanged(BlueConnectState.SCAN_CANCEL);
            }
            mHandler.removeCallbacksAndMessages(null);
        }
    }

	@Override
	public BluetoothDevice lookforDevices(String address) {
		return super.lookforDevices(address);
	}

	@Override
	public boolean pairDevices(String address) {
        return connect(address);	
		
	}

	@Override
	public void requestChannelData(int channel) {
		byte channelByte = (byte) channel;
/*		final int charaProp = characteristicCommand.getProperties();
		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {*/
		byte[] value = new byte[] {COMMANDBYTE_REQUEST_CHANNEL_DATA_RESEND, channelByte};
		//writeCharacteristic(characteristicCommand, value);
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
//		}
		
	}
	
	@Override
	public BbqSettings requestCurrentChannelSettings(int ch) {
		BbqSettings s;
		switch (ch) {
		case 1:
			s = dataToSetting(preDataSetCh1);
			s.setChannel(1);
			return s;
		case 2:
			s = dataToSetting(preDataSetCh2);
			s.setChannel(2);
			return s;
		default:
			break;
		}
		return null;
	}

	@Override
	public void setupBbqChannel(BbqSettings settings) {
//		final int charaProp = characteristicCommand.getProperties();
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		byte[] value = new byte[13];
		value[0] = COMMANDBYTE_SETUP_BBQ_CHANNEL;
		value[1] = (byte) settings.getChannel();
		value[2] = (byte) (settings.getMeatTypeInt());
		value[3] = (byte) settings.getDonenessLevel().ordinal();
		int temp = (int) (settings.getTargetTemperature() * 10);
		value[4] = (byte)(temp & 0xFF);
		value[5] = (byte)((temp >> 8) & 0xFF);
		
		value[6] = (byte) settings.getReloadTimer().getHours();
		value[7] = (byte) settings.getReloadTimer().getMinute();
		value[8] = (byte) settings.getReloadTimer().getSecond();
		value[9] = (byte) settings.getCurrentTimer().getHours();
		value[10] = (byte) settings.getCurrentTimer().getMinute();
		value[11] = (byte) settings.getCurrentTimer().getSecond();
		if (settings.isUpCountTimer()) {
			value[12] = (byte) 0x01;
		} else {
			value[12] = (byte) 0x00;
		}
		//writeCharacteristic(characteristicCommand, value);
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
		requestChannelData(settings.getChannel());
			//characteristicCommand.setValue(value);
			//mBluetoothGatt.writeCharacteristic(characteristicCommand);
//		}
		
		
	}

	@Override
	public void setOperatingStart(int channel, Mode mode) {
		Log.d(TAG, "setOperatingStart");
//		final int charaProp = characteristicCommand.getProperties();
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		byte[] value = new byte[3];
		value[0] = COMMANDBYTE_START_OPERATION;
		value[1] = (byte) channel;
		if (BbqConfig.isOldFirmwire()) {
			value[2] = (byte) (mode.ordinal() - 1);
		} else {
			value[2] = (byte) mode.ordinal();
		}
		
		//writeCharacteristic(characteristicCommand, value);
		// use command pool instead of writing
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
		requestChannelData(channel);
//			characteristicCommand.setValue(value);
//			mBluetoothGatt.writeCharacteristic(characteristicCommand);
//		}
		
	}
	
	@Override
	public void setOperatingStop(int channel) {
//		Log.d(TAG, "setOperatingStop");
//		final int charaProp = characteristicCommand.getProperties();
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		byte[] value = new byte[2];
		value[0] = COMMANDBYTE_STOP_OPERATION;
		value[1] = (byte) channel;
		
//		writeCharacteristic(characteristicCommand, value);
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
		requestChannelData(channel);
//			characteristicCommand.setValue(value);
//			mBluetoothGatt.writeCharacteristic(characteristicCommand);
//		}
	}

	@Override
	public void requestCustomDoneness(int meatTypeInt) {
//		final int charaProp = characteristicCommand.getProperties();
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		byte[] value = new byte[2];
		value[0] = COMMANDBYTE_REQUEST_CUSTOM_DONENESS;
		value[1] = (byte) meatTypeInt;
		
//		writeCharacteristic(characteristicCommand, value);
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
//			characteristicCommand.setValue(value);
//			mBluetoothGatt.writeCharacteristic(characteristicCommand);
//		}
		
	}

	@Override
	public void clearCustomDoneness() {
//		final int charaProp = characteristicCommand.getProperties();
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		byte[] value = new byte[2];
		value[0] = COMMANDBYTE_CLEAR_CUSTOM_DONENESS;
		value[1] = (byte) 0x00;
		
//		writeCharacteristic(characteristicCommand, value);
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
			//characteristicCommand.setValue(value);
			//mBluetoothGatt.writeCharacteristic(characteristicCommand);
//		}
	}

	@Override
	public void setCustomDonenessTemperature(
			DonenessTemperature donnessTemperature) {
		//final int charaProp = characteristicCommand.getProperties();
		int offset = 0;
		//if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
		byte[] value = new byte[12];
		value[offset ++] = COMMANDBYTE_SET_CUSTOM_DONENESS;
		value[offset ++] = (byte) donnessTemperature.getMeatTypeIndex();
		int temp = (int) (donnessTemperature.getRareTemperature());
		value[offset ++] = (byte)((temp) & 0xFF);
		value[offset ++] = (byte)((temp >> 8) & 0xFF);
		temp = (int) (donnessTemperature.getMediumrareTemperature());
		value[offset ++] = (byte)((temp) & 0xFF);
		value[offset ++] = (byte)((temp >> 8) & 0xFF);
		temp = (int) (donnessTemperature.getMediumTemperature());
		value[offset ++] = (byte)((temp) & 0xFF);
		value[offset ++] = (byte)((temp >> 8) & 0xFF);
		temp = (int) (donnessTemperature.getMediumwellTemperature());
		value[offset ++] = (byte)((temp) & 0xFF);
		value[offset ++] = (byte)((temp >> 8) & 0xFF);
		temp = (int) (donnessTemperature.getWelldoneTemperature());
		value[offset ++] = (byte)((temp) & 0xFF);
		value[offset ++] = (byte)((temp >> 8) & 0xFF);
//		characteristicCommand.setValue(value);
//		mBluetoothGatt.writeCharacteristic(characteristicCommand);
//		writeCharacteristic(characteristicCommand, value);
		Command c = new Command();
		c.setCharacteristic(characteristicCommand);
		c.setType(CommandType.WRITE);
		c.setValue(value);
		putCommand(c);
		//}
		
	}
	
	private void updateConnectionState(BlueConnectState state) {
		switch (state) {
		
		default:
			break;
		}
	}
	

	/**
	 * @param mChannelSettingsChangedCallback the mChannelSettingsChangedCallback to set
	 */
	public void setChannelSettingsChangedCallback(
			ChannelSettingsChangedCallback mChannelSettingsChangedCallback) {
		this.mChannelSettingsChangedCallback = mChannelSettingsChangedCallback;
	}
	
	/**
	 * @param mDonenessCallback the mDonenessCallback to set
	 */
	public void setmDonenessCallback(BbqDonenessCallback mDonenessCallback) {
		this.mDonenessCallback = mDonenessCallback;
	}
	
	/**
	 * @param mBbqDeviceCallback the mBbqDeviceCallback to set
	 */
	public void setmGattCallback(BbqDeviceCallback mBbqDeviceCallback) {
		this.mBbqDeviceCallback = mBbqDeviceCallback;
	}
	
	 /**
	 * @param mOnBleScanningCallback the mOnBleScanningCallback to set
	 */
	public void setOnBleScanningCallback(
			OnBleScanningCallback mOnBleScanningCallback) {
		this.mOnBleScanningCallback = mOnBleScanningCallback;
	}
	
	private Runnable stopScanningCallback = new Runnable() {
        
		@Override
        public void run() {
			// Modified by aaronli at Mar 25
			if (mBluetoothAdapter != null) {
				try {
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				} catch (Exception e) {
					if (BuildConfig.DEBUG) 
						e.printStackTrace();
				}
			}
            if (mOnBleScanningCallback != null) {
            	mOnBleScanningCallback.onStopScanning();
            }
        }
    };
    
 // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
    	
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        	
        	if (device.getName() != null) {
        		if (mOnBleScanningCallback != null) {
        			mOnBleScanningCallback.onDescoverDevices(device, rssi, scanRecord);
        		}
        	}
        }
    };
    
   

	public interface OnBleScanningCallback {
		
		public void onScaningStateChanged(BlueConnectState state) ;
    	
    	public void onDescoverDevices(BluetoothDevice device, int rssi, byte[] scanRecord);
    	
    	public void onStopScanning();
    	
    }
	/**
	 * @param characteristic
	 * @param data
	 */
	public static void logForBytesHex(String head, BluetoothGattCharacteristic characteristic,
			final byte[] data) {
		final StringBuilder stringBuilder = new StringBuilder(data.length);
        for(byte byteChar : data)
            stringBuilder.append(String.format("%02X ", byteChar));
    	Log.i(TAG,head+" " + characteristic.getUuid() + ":" + stringBuilder.toString());
	}

	
	
}
