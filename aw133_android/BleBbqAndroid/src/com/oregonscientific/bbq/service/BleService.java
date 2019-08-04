package com.oregonscientific.bbq.service;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.act.OperationActivity;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.ble.BbqDonenessCallback;
import com.oregonscientific.bbq.ble.BleBbqCommandManager;
import com.oregonscientific.bbq.ble.BleBbqCommandManager.OnBleScanningCallback;
import com.oregonscientific.bbq.ble.BbqDeviceCallback;
import com.oregonscientific.bbq.ble.Command;
import com.oregonscientific.bbq.ble.CommandManager;
import com.oregonscientific.bbq.ble.SampleGattAttributes;
import com.oregonscientific.bbq.ble.CommandManager.BlueConnectState;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class BleService extends Service {
	
	private final String TAG = "BleService";
	
	private BleBbqCommandManager commandManager; 
	public static final String SERVICE_ACTION = "com.oregonscientific.bbq.COMMAND";
	public static final String KEY_COMMAND_INDEX = "ble_service_command_index";
	public static final String KEY_CONNECT_DEVICE = "ble_service_command_connect";
	public static final String KEY_CONNECT_SCAN = "ble_service_command_scan";
	public static final String KEY_CONNECT_PAIR = "ble_service_command_pair";
	public static final String KEY_STOP_SCANNING = "ble_service_command_stopscanning";
	public static final String KEY_STOP_DISCONNECT = "ble_service_command_disconnect";
	public static final String KEY_STOP_FORCE_DISCONNECT = "ble_service_command_forcedisconnect";
	public static final String KEY_COMMAND_SINGLE = "ble_service_command_single";
	public static final String KEY_COMMAND_MULTIPLE = "ble_service_command_nultiple";
	public static final String KEY_REQUEST_CHANNEL_DATA = "ble_request_channel data";
	public static final String KEY_REQUEST_CURRENTCHANNELSETTINGS = "ble_request_currentchannel_settings";
	public static final String KEY_SETUP_BBQCHANNEL = "ble_setuup_bbqchannel";
	public static final String KEY_SET_OPERATINGSTART = "ble_set_operating_start";
	public static final String KEY_SET_OPERATINGSTOP = "ble_set_operating_stop";
	public static final String KEY_REQUEST_CUSTOMDONENESS = "ble_request_customdoneness";
	public static final String KEY_CLEAR_CUSTOMDONENESS = "ble_clear_customdoneness";
	public static final String KEY_SET_CUSTOMDONENESSTEMPERATURE = "ble_set_customdoneness";
	
	public static final String KEY_BROADCAST_VALUE = "value"; 
	
	public static final int CONNECT_STATUS_DISCONNECT = 0;
	public static final int CONNECT_STATUS_SCANNING = 1;
	public static final int CONNECT_STATUS_PAIRING = 2;
	public static final int CONNECT_PAIRED = 4;
	
	public final static String ACTION_CONNECT_STATE_CHANGED =
            "com.oregonscientific.bbq.ACTION_CONNECT_STATE_CHANGED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.oregonscientific.bbq.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_COMMAND_CALLBACK =
            "com.oregonscientific.bbq.ACTION_COMMAND_CALLBACK";
    public final static String ACTION_COMMAND_CHANNEL_DATA =
            "com.oregonscientific.bbq.ACTION_COMMAND_CALLBACK";
    public final static String EXTRA_DATA =
            "com.oregonscientific.bbq.EXTRA_DATA";
    public final static String EXTRA_ERROR =
            "com.oregonscientific.bbq.EXTRA_ERROR";
	
	private String mDeviceAddress;
	private SharingPreferenceDao dao;
	private boolean autoConnecting;	

	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		commandManager = BleBbqCommandManager.getInstance(this);
		commandManager.setOnBleScanningCallback(new MyScanningCallback());
		commandManager.setmGattCallback(new BleCallback());
		commandManager.setmDonenessCallback(new MyDonenessCallback());
		dao = SharingPreferenceDao.getInstance(this);
	}



	@Override
	public IBinder onBind(Intent intent) {
		return new LocalBinder();
	}
	
	@Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        return super.onUnbind(intent);
    }

	
	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		BleBbqCommandManager.clear();
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand "+ startId);
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		final String extra = intent.getStringExtra(KEY_COMMAND_INDEX);
		if (KEY_CONNECT_DEVICE.equals(extra)) {
			autoConnecting = true;
			mDeviceAddress = dao.getLastConnectDeviceAddress();
			if (mDeviceAddress != null) {
				commandManager.scanLeDevice(true);
			}
		}else if (KEY_CONNECT_SCAN.equals(extra)) {
			autoConnecting = true;
			mDeviceAddress = null;
			commandManager.scanLeDevice(true);
		} else if (KEY_CONNECT_PAIR.equals(extra)) {
			if (TextUtils.isEmpty(mDeviceAddress)) {
				//commandManager.pairDevices(mDeviceAddress);
				autoConnecting = true;
				
			}
		} else if (KEY_STOP_SCANNING.equals(extra)) {
			commandManager.scanLeDevice(false);
			autoConnecting = false;

		} else if (KEY_STOP_DISCONNECT.equals(extra)) {
			autoConnecting = false;
			commandManager.close();
		} else if (KEY_STOP_FORCE_DISCONNECT.equals(extra)) {
			autoConnecting = false;
			mDeviceAddress = null;
			dao.setLastConnectDeviceAddress("");
			commandManager.close();
			commandManager.scanLeDevice(false);
			sendConnectStatusChangedBroadcast(BlueConnectState.DISCONNECTED);
		} else if (KEY_COMMAND_SINGLE.equals(extra)) {
			commandManager.putCommand((Command) intent.getParcelableExtra(KEY_COMMAND_SINGLE));
		} else if (KEY_COMMAND_MULTIPLE.equals(extra)) {
			Parcelable[] commands = intent.getParcelableArrayExtra(KEY_COMMAND_MULTIPLE);
			for (Parcelable item  : commands) {
				commandManager.putCommand((Command) item);
			}
		} else if (KEY_REQUEST_CHANNEL_DATA.equals(extra)) {
			final int channel = intent.getIntExtra(KEY_REQUEST_CHANNEL_DATA, 1);
			commandManager.requestChannelData(channel);
		}else if (KEY_REQUEST_CURRENTCHANNELSETTINGS.equals(extra)) {
			final int channel = intent.getIntExtra(KEY_REQUEST_CURRENTCHANNELSETTINGS, 1);
			commandManager.requestCurrentChannelSettings(channel);
		}else if (KEY_SETUP_BBQCHANNEL.equals(extra)) {
			final BbqSettings settings = (BbqSettings) intent.getSerializableExtra(KEY_SETUP_BBQCHANNEL);
			commandManager.setupBbqChannel(settings);
		}else if (KEY_SET_OPERATINGSTART.equals(extra)) {
			final int channel = intent.getIntExtra(KEY_SET_OPERATINGSTART + "channel", 1);
			final Mode mode = (Mode) intent.getSerializableExtra(KEY_SET_OPERATINGSTART+ "mode");
			commandManager.setOperatingStart(channel, mode);
		}else if (KEY_SET_OPERATINGSTOP.equals(extra)) {
			final int channel = intent.getIntExtra(KEY_SET_OPERATINGSTOP, 1);
			commandManager.setOperatingStop(channel);
		}else if (KEY_REQUEST_CUSTOMDONENESS.equals(extra)) {
			final int meatTypeInt = intent.getIntExtra(KEY_REQUEST_CUSTOMDONENESS, 1);
			commandManager.requestCustomDoneness(meatTypeInt);
		}else if (KEY_CLEAR_CUSTOMDONENESS.equals(extra)) {
			commandManager.clearCustomDoneness();
		}else if (KEY_SET_CUSTOMDONENESSTEMPERATURE.equals(extra)) {
			final DonenessTemperature donnessTemperature = (DonenessTemperature) intent.getSerializableExtra(KEY_SET_CUSTOMDONENESSTEMPERATURE);
			commandManager.setCustomDonenessTemperature(donnessTemperature);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendConnectStatusChangedBroadcast(BlueConnectState connectedStatus) {
		Intent intent = new Intent(ACTION_CONNECT_STATE_CHANGED);
		intent.putExtra(KEY_BROADCAST_VALUE, connectedStatus);
		sendBroadcast(intent);
	}

	private void sendCommandCallbackBroadcast() {
		Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
		//intent.putExtra(KEY_BROADCAST_VALUE,  ACTION_COMMAND_CALLBACK);
		sendBroadcast(intent);
	}

	public class LocalBinder extends Binder {
		public BleService getService() {
            return BleService.this;
        }
    }
	
	private class MyScanningCallback implements OnBleScanningCallback {
		
		@Override
		public void onScaningStateChanged(BlueConnectState state) {
			//sendConnectStatusChangedBroadcast(state);
			switch (state) {
			case SCANNING:
				sendConnectStatusChangedBroadcast(state);
				break;

			default:
				break;
			}
		}

		@Override
		public synchronized void onDescoverDevices(BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			Log.d(TAG, "onDescoverDevices " + device.getName());
			String flagAddress = dao.getLastConnectDeviceAddress();
			if (flagAddress != null && flagAddress.equals(device.getAddress())) {
				if (commandManager.getConnectionState(flagAddress) == BluetoothProfile.STATE_DISCONNECTED) {
					connectHandler.sendEmptyMessage(2);
				}
				commandManager.scanLeDevice(false);
				sendConnectStatusChangedBroadcast(BlueConnectState.FOUND_DEVICE_SUCCESS);
			} else if (TextUtils.isEmpty(flagAddress) ) {
				if (device != null &&  device.getName() != null 
	        			&& device.getName().contains(SampleGattAttributes.BLE_BBQ_SCANNABLE)) {
					flagAddress = device.getAddress();
					dao.setLastConnectDeviceAddress(flagAddress);
					autoConnecting = true;
					connectHandler.sendEmptyMessage(2);
					commandManager.scanLeDevice(false);
					sendConnectStatusChangedBroadcast(BlueConnectState.FOUND_DEVICE_SUCCESS);
				}
			}
		}

		@Override
		public void onStopScanning() {
			String flagAddress = dao.getLastConnectDeviceAddress();
			if (autoConnecting) {
				commandManager.scanLeDevice(true);
				sendConnectStatusChangedBroadcast(BlueConnectState.FOUND_DEVICE_FAIL);
			}
		}
		
	}
	
	private class BleCallback implements BbqDeviceCallback{

		@Override
		public void onDeviceJoin() {
			sendConnectStatusChangedBroadcast(BlueConnectState.PAIRED_OK);
		}

		@Override
		public void replyChannelDataResend(int channel, BBQDataSet value) {
			Intent intent = new Intent(ACTION_COMMAND_CHANNEL_DATA);
			intent.putExtra(KEY_COMMAND_INDEX, KEY_REQUEST_CHANNEL_DATA);
			intent.putExtra(KEY_REQUEST_CHANNEL_DATA, value);
			intent.putExtra(KEY_REQUEST_CHANNEL_DATA + "channel", channel);
			sendBroadcast(intent);
		}

		@Override
		public void replySetupBbqChanged(String msg) {
			if (TextUtils.isEmpty(msg)) {
				
				//mCommandManager.requestChannelData(channel);
				Intent intent = new Intent(ACTION_COMMAND_CHANNEL_DATA);
				intent.putExtra(KEY_COMMAND_INDEX, KEY_SETUP_BBQCHANNEL);
				sendBroadcast(intent);
			}
			
			
		}

		@Override
		public void indiacateStart(int channel, final String msg) {
			Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
			intent.putExtra(KEY_COMMAND_INDEX, KEY_SET_OPERATINGSTART);
			intent.putExtra(EXTRA_DATA, channel);
			if (TextUtils.isEmpty(msg)) {
			} else {
				intent.putExtra(EXTRA_ERROR , msg);
			}
			sendBroadcast(intent);
			//commandManager.requestChannelData(channel);
		}

		@Override
		public void indiacateStop(int channel, final String msg) {
			Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
			intent.putExtra(KEY_COMMAND_INDEX, KEY_SET_OPERATINGSTOP);
			intent.putExtra(EXTRA_DATA, channel);
			if (TextUtils.isEmpty(msg)) {
			} else {
				intent.putExtra(EXTRA_ERROR , msg);
			}
			sendBroadcast(intent);
			//commandManager.requestChannelData(channel);
		}

		@Override
		public void onDisconnected() {
			Log.d(TAG, "onDisconnected");
			sendConnectStatusChangedBroadcast(BlueConnectState.DISCONNECTED);
			if (autoConnecting && commandManager.isBluetoothEnabled()) {
				connectHandler.sendEmptyMessage(1);
			}
		}

		
	}
	
	private class MyDonenessCallback implements BbqDonenessCallback {

		@Override
		public void replyCustomDoneness(final DonenessTemperature values) {
			Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
			intent.putExtra(KEY_COMMAND_INDEX, KEY_REQUEST_CUSTOMDONENESS);
			intent.putExtra(EXTRA_DATA, values);
			sendBroadcast(intent);
		}

		@Override
		public void indiacateClearCustomDoneness(String msg) {
			if (TextUtils.isEmpty(msg)) {
				/*Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
				intent.putExtra(KEY_COMMAND_INDEX, KEY_CLEAR_CUSTOMDONENESS);
				sendBroadcast(intent);*/
			}
			
		}

		@Override
		public void replySetCustomDoneness(String msg) {
			if (TextUtils.isEmpty(msg)) {
				/*Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
				intent.putExtra(KEY_COMMAND_INDEX, KEY_SET_CUSTOMDONENESSTEMPERATURE);
				sendBroadcast(intent);*/
			}
			
		}

		@Override
		public void replyDefaultDoneness() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSetupCustomDonenessSent(int meatTypeIndex) {
			/*Intent intent = new Intent(ACTION_COMMAND_CALLBACK);
			intent.putExtra(KEY_COMMAND_INDEX, KEY_SET_CUSTOMDONENESSTEMPERATUR);
			intent.putExtra(EXTRA_DATA, values);
			sendBroadcast(intent);*/
			
		}
	}
	
	private Handler connectHandler = new Handler(){
		 @Override
		 public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				Log.d(TAG, "show fail message ");
				Toast.makeText(getApplication(), R.string.err_disconnected, Toast.LENGTH_LONG).show();
				removeCallbacksAndMessages(null);
				sendEmptyMessageDelayed(2, 10000);
				break;	
			case 2:
//				Log.d(TAG, "pairDevices ");
				String deviceAddress = dao.getLastConnectDeviceAddress();
				if (!TextUtils.isEmpty(dao.getLastConnectDeviceAddress()))
						commandManager.pairDevices(deviceAddress);
				break;
			case 3:
				
				break;
			default:
				break;
			}
		 };
	 };
}
