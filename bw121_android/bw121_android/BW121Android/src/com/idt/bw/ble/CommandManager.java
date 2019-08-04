/**
 * 
 */
package com.idt.bw.ble;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;

import static com.idt.bw.ble.SampleGattAttributes.*;

/**
 * 
 * The BLE commands manager. It is used to control the working of BLE: scanning, connection, pairing, getting data and closing<br>
 * use method startScalage to start scalaging the weight. <br>
 * 
 * @author aaronli
 *
 */
public class CommandManager {
	
	private static final String TAG = "CommandManager";
	
	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;

	private static final long SCAN_PERIOD = 15000;
	
	protected ContextWrapper mContextWrapper;
	private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private Handler mHandler;
    // this app try to scan device 3 times.If it is still no devices, 
    // It will judge no devices; 
//    private int scanningRemaining;
    private WeightData mData;
    private BluetoothDevice bwDevice;
    private boolean confirmWeight;
    
    private BluetoothGattCharacteristic bpdCharacteristicMeasurement;
    private BluetoothGattCharacteristic bpdCharacteristicCuffPresure ;
    
    private BLECommandCallback callback;

	/**
	 * 
	 */
	public CommandManager(ContextWrapper context) {
		
		mContextWrapper = context;
		initialize();
	}
	
	 /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    private  boolean initialize() {
    	if (mContextWrapper.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
    		if (callback != null)
    			callback.onNotSuport();
		}
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContextWrapper.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false; 
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        mHandler = new Handler();
        return true;
    }
    
    public boolean isBluetoothEnabled() {
    	 return mBluetoothAdapter == null ? false :mBluetoothAdapter.isEnabled();
    }
    
    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mConnectionState = STATE_DISCONNECTED;
        mBluetoothGatt.close();
        bwDevice = null;
        mBluetoothGatt = null;
        if (callback != null) {
        	callback.onDisconnect(null);
        }
    }
    

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }
    
    private void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
//    	Log.d(TAG, "writeCharacteristic ready "+ characteristic.getUuid() + Arrays.toString(data));
    	characteristic.setValue(data);
    	mBluetoothGatt.writeCharacteristic(characteristic);
    }
    
    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
	private void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        //Log.d(TAG, "setCharacteristicNotification "+ characteristic.getUuid());
        if (!mBluetoothGatt.setCharacteristicNotification(characteristic,
				enabled))
			return ;
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
				.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor == null)
        	return ;
        if (enabled) {
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
        	descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        mBluetoothGatt.writeDescriptor(descriptor);
    }
    
    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
	private void setCharacteristicIndiacation(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        //Log.d(TAG, "setCharacteristicNotification "+ characteristic.getUuid());
        if (!mBluetoothGatt.setCharacteristicNotification(characteristic,
				enabled))
			return ;
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
				.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor == null)
        	return ;
        if (enabled) {
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
        } else {
        	descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }
    
    public void scanLeDevice(final boolean enable) {
		Log.d(TAG, "scanLeDevice " + enable);
		bwDevice = null;
        if (enable) {
			// Stops scanning after a pre-defined scan period.
//        	scanningRemaining = 3;
            mHandler.postDelayed(stopScanningCallback, SCAN_PERIOD);
//            updateConnectionState(BlueConnectState.SCANNING);
            mBluetoothAdapter.startLeScan(new UUID[] {SampleGattAttributes.UUID_BPD_SERVICE}, mLeScanCallback);
        } else {
//        	updateConnectionState(BlueConnectState.SCAN_CANCEL);
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mHandler.removeCallbacksAndMessages(null);
        }
    }
    
    public void startScalage () {
    	scanLeDevice(true);
    }

	/**
	 * @return the mConnectionState
	 */
	public int getConnectionState() {
		if (mBluetoothManager == null || mBluetoothDeviceAddress == null) {
    		return BluetoothProfile.STATE_DISCONNECTED;
    	}
		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mBluetoothDeviceAddress);
        if (device == null) {
            return BluetoothProfile.STATE_DISCONNECTED;
        }
    	return mBluetoothManager.getConnectionState(device, BluetoothProfile.GATT);
	}
	
	public BluetoothDevice lookforDevices(String address) {
		if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return null;
        }
       
        BluetoothDevice device = null;
        try {
			
        	device = mBluetoothAdapter.getRemoteDevice(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
        }
        return device;
	}

	
	protected boolean connect(String address) {
		if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

		if (mBluetoothGatt !=null) {
			mBluetoothGatt.close();
		}
//		Log.v(TAG, "scan mode" + mBluetoothAdapter.getScanMode());
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContextWrapper, false, mGattCallback);
//        Log.i(TAG, "Trying to create a new connection.");
        
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;	
		
	}
	
	protected boolean connect(BluetoothDevice device) {
		if (mBluetoothAdapter == null || device == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

		if (mBluetoothGatt !=null) {
			mBluetoothGatt.close();
		}
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(mContextWrapper, false, mGattCallback);
//        Log.i(TAG, "Trying to create a new connection.");
        
        mBluetoothDeviceAddress = device.getAddress();
        mConnectionState = STATE_CONNECTING;
        return true;	
		
	}
	
	protected void onConnectedSuccess(BluetoothDevice device) {
		if (callback != null) {
			callback.onConnectSuccess(device);
		}
		confirmWeight = false;
	}
	
	protected void onDisconnected(BluetoothDevice device) {
		if (callback != null) {
			if (!confirmWeight) {
				try {
					callback.onScalingFail();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			callback.onDisconnect(device);
		}
		confirmWeight = false;
	}
	
	protected void initCharacteristics(BluetoothGatt gatt) {
		BluetoothGattService bpdService = gatt
				.getService(SampleGattAttributes.UUID_BPD_SERVICE);
		bpdCharacteristicMeasurement = bpdService
				.getCharacteristic(SampleGattAttributes.UUID_BPD_CHARACTERISTIC_MEASUREMENT);
		bpdCharacteristicCuffPresure = bpdService
				.getCharacteristic(SampleGattAttributes.UUID_BPD_CHARACTERISTIC_CUFF_PRESSURE);
		startPairing();
	}
	
	private void startPairing() {
		setCharacteristicIndiacation(bpdCharacteristicMeasurement, true);
	}
	
	protected void onReadMessages(BluetoothGattCharacteristic characteristic) {
	}
	
	protected void notifyCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
		UUID uuid = characteristic.getUuid();
		if (UUID_BPD_CHARACTERISTIC_MEASUREMENT.equals(uuid) || UUID_BPD_CHARACTERISTIC_CUFF_PRESSURE.equals(uuid))
		{
//			printlnData(characteristic);
			if (mData == null) {
				mData = new WeightData();
			}
			int num = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 18);
			mData.setFlag(num);
			mData.setUnit(num & 0x01);
			mData.setWeight(characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, 1).intValue() / 10.0F);
			
			boolean isConfirm = (num & 0x80) != 0;
			if (isConfirm) {
				if (!confirmWeight) {
					callback.onReturnResult(mData);
				}
				confirmWeight = true;
			}
			else {
				confirmWeight = false;
				if (callback != null) {
					
					callback.onVariableData(mData);
				}
			}
		}
	}
	
	protected void descriptorWriteSuccess(BluetoothGattDescriptor descriptor) {
		if (UUID_BPD_CHARACTERISTIC_MEASUREMENT.equals(descriptor.getCharacteristic().getUuid())) {
			setCharacteristicNotification(bpdCharacteristicCuffPresure, true);
		} else if (UUID_BPD_CHARACTERISTIC_CUFF_PRESSURE.equals(descriptor.getCharacteristic().getUuid())) {
			if (callback != null) {
				callback.onPairSuccess();
			}
		}
	}
	
	protected void onConnectFailed(BluetoothDevice device) {
		if (callback != null) {
			callback.onConnectFail(device);
		}
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(BLECommandCallback callback) {
		this.callback = callback;
	}
	
	public void printlnData(BluetoothGattCharacteristic characteristic) {
		final byte[] data = characteristic.getValue();
    	final StringBuilder stringBuilder = new StringBuilder(data.length);
        for(byte byteChar : data)
            stringBuilder.append(String.format("%02X ", byteChar));
    	Log.i(TAG, "onCharacteristicChanged " + characteristic.getUuid() + ":" + stringBuilder.toString());
	}

	// Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, "onConnectionStateChange" + status + "->"+newState);
            try {
				
            	if (BluetoothGatt.GATT_SUCCESS == status) {
            		if (newState == BluetoothProfile.STATE_CONNECTED) {
            			mConnectionState = STATE_CONNECTED;
            			onConnectedSuccess(gatt.getDevice());
            			boolean b = gatt.discoverServices();
            		} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            			mConnectionState = STATE_DISCONNECTED;
            			onDisconnected(gatt.getDevice());
            		}
            	} else {
            		mConnectionState = STATE_DISCONNECTED;
            		onConnectFailed(gatt.getDevice());
            	}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        	try {
				
        		if (status == BluetoothGatt.GATT_SUCCESS) {
//            	Log.i(TAG, "onServicesDiscovered:" + status);
        			initCharacteristics(gatt);
        			
        		} else {
        			Log.w(TAG, "onServicesDiscovered received: " + status);
        		}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
	         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	Log.i(TAG, "onCharacteristicRead:" + characteristic.getUuid() + Arrays.toString(characteristic.getValue()));
            	onReadMessages(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
               BluetoothGattCharacteristic characteristic) {
        	notifyCharacteristicChanged(characteristic);
        }

		/* (non-Javadoc)
		 * @see android.bluetooth.BluetoothGattCallback#onCharacteristicWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)
		 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
//				Log.i(TAG, "onCharacteristicWrite " + Arrays.toString(characteristic.getValue()));
				//about doneness level setting
				/*byte[] value = characteristic.getValue();
				if (value[0] == COMMANDBYTE_SET_CUSTOM_DONENESS) {
					onSetupCustomDonenessSent(value);
				}
				if (value[0] == COMMANDBYTE_START_OPERATION || value[0] == COMMANDBYTE_STOP_OPERATION) {
					//requestChannelData(value[1]);
				}*/
			}
		}

		/* (non-Javadoc)
		 * @see android.bluetooth.BluetoothGattCallback#onDescriptorRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattDescriptor, int)
		 */
		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
//				Log.i(TAG, "onDescriptorRead");
				super.onDescriptorRead(gatt, descriptor, status);
			}
		}

		/* (non-Javadoc)
		 * @see android.bluetooth.BluetoothGattCallback#onDescriptorWrite(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattDescriptor, int)
		 */
		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			super.onDescriptorWrite(gatt, descriptor, status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.i(TAG, "onDescriptorWrite " + descriptor.getCharacteristic().getUuid() + Arrays.toString(descriptor.getValue() ));
				descriptorWriteSuccess(descriptor);
			}
		}

		/* (non-Javadoc)
		 * @see android.bluetooth.BluetoothGattCallback#onReadRemoteRssi(android.bluetooth.BluetoothGatt, int, int)
		 */
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
//			Log.i(TAG, "onReadRemoteRssi");
			super.onReadRemoteRssi(gatt, rssi, status);
		}

		/* (non-Javadoc)
		 * @see android.bluetooth.BluetoothGattCallback#onReliableWriteCompleted(android.bluetooth.BluetoothGatt, int)
		 */
		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
//			Log.i(TAG, "onReliableWriteCompleted");
			super.onReliableWriteCompleted(gatt, status);
		}
    };
    
    
    public static enum BlueConnectState {
    	SCANNING,
    	FOUND_DEVICE_SUCCESS,
    	FOUND_DEVICE_FAIL,
    	SCAN_CANCEL,
    	CONNECTION_OK,
    	CONNECTION_FAILED,
    	DISCONNECTED;
    }
    
    private Runnable stopScanningCallback = new Runnable() {
        
		@Override
        public void run() {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            if (bwDevice != null ) {
            	
            } else  {
            	mHandler.postDelayed(stopScanningCallback, SCAN_PERIOD);
                mBluetoothAdapter.startLeScan(mLeScanCallback);
//            	scanningRemaining --;
            } 
        }
    };
    
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
    	
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        	final String name = device.getName();
        	if (name != null && "ScaleDevice".contentEquals(name)) {
        		if (bwDevice == null) {
        			bwDevice = device;
        			mHandler.removeCallbacks(null);
        			mHandler.post(stopScanningCallback);
        			mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							connect(device);
							
						}
					});
        		} else {
        			mHandler.removeCallbacks(null);
        			mHandler.post(stopScanningCallback);
        		}
        	}
        }
    };

	public static interface BLECommandCallback {
		
		public void onNotSuport();
		
		public void onDevicesNotFound();
		
		public void onConnectFail(BluetoothDevice device);
		
		public void onConnectSuccess(BluetoothDevice device);
		
		public void onPairSuccess();
		
		public void onReturnResult(WeightData data);
		
		public void onDisconnect(BluetoothDevice device);
		
		public void onVariableData(WeightData data);
		
		public void onScalingFail();
	}
}
