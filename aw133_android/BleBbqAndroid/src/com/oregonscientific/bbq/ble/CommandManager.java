/**
 * 
 */
package com.oregonscientific.bbq.ble;

import static com.oregonscientific.bbq.ble.SampleGattAttributes.COMMANDBYTE_SET_CUSTOM_DONENESS;
import static com.oregonscientific.bbq.ble.SampleGattAttributes.COMMANDBYTE_START_OPERATION;
import static com.oregonscientific.bbq.ble.SampleGattAttributes.COMMANDBYTE_STOP_OPERATION;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.oregonscientific.bbq.ble.CommandPool.CommandPoolControll;

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
import android.util.Log;

/**
 * @author aaronli
 *
 */
public abstract class CommandManager {
	
	private static final String TAG = "CommandManager";
	
	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;
	
	protected ContextWrapper mContextWrapper;
	private BluetoothManager mBluetoothManager;
    protected BluetoothAdapter mBluetoothAdapter;
    protected String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private CommandPool pool;
    private int mConnectionState = STATE_DISCONNECTED;

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
       
        return true;
    }
    
    public boolean isBluetoothEnabled() {
    	 return mBluetoothAdapter.isEnabled();
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
        stopPool();
        mConnectionState = STATE_DISCONNECTED;
        mBluetoothGatt.close();
        onDisconnected();
        mBluetoothGatt = null;
    }
    
    public void putCommand(Command c) {
    	if (pool != null)
    		pool.putCommand(c);
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || characteristic == null) {
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }
    
    private void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null || characteristic == null) {
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
    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        //Log.d(TAG, "setCharacteristicNotification "+ characteristic.getUuid());
        if (!mBluetoothGatt.setCharacteristicNotification(characteristic,
				enabled))
			return ;
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
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
    private void setCharacteristicIndiacation(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        //Log.d(TAG, "setCharacteristicNotification "+ characteristic.getUuid());
        if (!mBluetoothGatt.setCharacteristicNotification(characteristic,
				enabled))
			return ;
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
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

	/**
	 * @return the mConnectionState
	 */
	public int getConnectionState(String address) {
		if (mBluetoothManager == null || address == null) {
    		return BluetoothProfile.STATE_DISCONNECTED;
    	}
		int state = 0;
		try {
			
			final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			if (device == null) {
				state = BluetoothProfile.STATE_DISCONNECTED;
			}
			state =  mBluetoothManager.getConnectionState(device, BluetoothProfile.GATT);
		} catch (Exception e) {
			e.printStackTrace();
			state =  BluetoothProfile.STATE_DISCONNECTED;
		}
		return state;
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
//        Log.i(TAG, "connect "+ address);
        // Previously connected device.  Try to reconnect.
//        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
////            Log.i(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                return true;
//            } else {
//                return false;
//            }
//        }
//        
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
	
	private void startPool() {
		pool.setStarting();
		new Thread(pool).start();
	}
	
	private void stopPool() {
		if (pool != null)
			pool.setStopping();
		pool = null;
	}
	
	protected abstract void onConnectedSuccess();
	
	protected abstract void onDisconnected();
	
	protected abstract void initCharacteristics(BluetoothGatt gatt);
	
	protected abstract void onReadMessages(BluetoothGattCharacteristic characteristic);
	
	protected abstract void notifyCharacteristicChanged(BluetoothGattCharacteristic characteristic);
	
	protected abstract void descriptorWriteSuccess(BluetoothGattDescriptor descriptor);
	
	protected abstract void onConnectFailed();

	// Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i(TAG, "onConnectionStateChange" + status + "->"+newState);
        	if (BluetoothGatt.GATT_SUCCESS == status) {
        		
        		if (newState == BluetoothProfile.STATE_CONNECTED) {
        			mConnectionState = STATE_CONNECTED;
        			onConnectedSuccess();
        			boolean b = gatt.discoverServices();
        			pool = new CommandPool();
        			pool.setCommandPoolControll(commandPoolCtrl);
        			startPool();
        		} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
        			mConnectionState = STATE_DISCONNECTED;
        			onDisconnected();
        			stopPool();
        		}
        	} else {
        		mConnectionState = STATE_DISCONNECTED;
        		onConnectFailed();
        	}
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
//            	Log.i(TAG, "onServicesDiscovered:" + status);
                initCharacteristics(gatt);
                
			} else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
	         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	Log.i(TAG, "onCharacteristicRead:" + characteristic.getUuid() + Arrays.toString(characteristic.getValue()));
            	onReadMessages(characteristic);
            	pool.setCurrentCommandEnd(characteristic);
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
				pool.setCurrentCommandEnd(characteristic);
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
//				Log.i(TAG, "onDescriptorWrite " + descriptor.getUuid() + Arrays.toString(descriptor.getValue() ));
				descriptorWriteSuccess(descriptor);
				pool.setCurrentCommandEnd(descriptor.getCharacteristic());
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
    
    private CommandPoolControll commandPoolCtrl =  new CommandPoolControll() {
		
		@Override
		public void startUsingCommand(Command c) {
			switch (c.getType()) {
			case READ:
				readCharacteristic(c.getCharacteristic());
				break;
			case WRITE:
				BluetoothGattCharacteristic ch = c.getCharacteristic();
				writeCharacteristic(ch, c.getValue());
				break;
			case INDIACATION:
				setCharacteristicIndiacation(c.getCharacteristic(), true);
				break;
			case NOTIFY:
				setCharacteristicNotification(c.getCharacteristic(), true);
				break;

			default:
				break;
			}
			
		}
	};
    
    public static enum BlueConnectState {
    	SCANNING,
    	FOUND_DEVICE_SUCCESS,
    	FOUND_DEVICE_FAIL,
    	SCAN_CANCEL,
    	PAIRED_OK,
    	PAIRED_FAILED,
    	DISCONNECTED;
    }

	
}
