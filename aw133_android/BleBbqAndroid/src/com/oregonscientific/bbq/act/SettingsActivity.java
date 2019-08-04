/**
 * 
 */
package com.oregonscientific.bbq.act;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.ble.BbqDonenessCallback;
import com.oregonscientific.bbq.ble.BleBbqCommandManager;
import com.oregonscientific.bbq.ble.SampleGattAttributes;
import com.oregonscientific.bbq.ble.CommandManager.BlueConnectState;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.service.BleService;
import com.oregonscientific.bbq.utils.BbqConfig;

public class SettingsActivity extends Activity implements OnClickListener,
		CompoundButton.OnCheckedChangeListener{
	
	private static final String TAG = "SettingsActivity";
	
	public static final String KEY_MEATTYPE = "meat_type";
	public static final String KEY_CUSTOM = "isCustom";
	public static final int RESULT_SETUP_DONENESS = 2;
	private ListView meatList;
	private MeatListAdapter meatadapter;
	private String[] meatnamearray;
	private LayoutInflater inflater;
//	private ListView listScanedDevices;
//	private LeDeviceListAdapter mLeDeviceListAdapter;
	private BleBbqCommandManager mCommand;
	private SharingPreferenceDao dao;
//	private ArrayList<BluetoothDevice> mLeDevices;
	private BluetoothDevice connectiingDevice;
	private List<DonenessTemperature> mDonenessList;
	private ImageView settingsbackiv,setingsicon;
	private TextView settingstv;
	private Switch switchBleConnection;
	private String defaultAddress;
	private Switch switchtempcf;
	
	private TextView versionnum;
	private View deviceSearchingHead;
	private View layoutCurrentConnected;
	
	private SparseArray<DonenessTemperature> presetDonenessLevel;
	// the data block. if it is ture,it cannot send any commands.
	private boolean isDataBlock;
	
//	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.settings);
		super.onCreate(savedInstanceState);
		
		init();
	}
	
	protected void onStart() {
		super.onStart();
		if (!mCommand.isBluetoothEnabled()) {
			finish();
			return;
		}
		final boolean currentConnectedSuccess = mCommand.getConnectionState(defaultAddress) == BleBbqCommandManager.STATE_CONNECTED;
		if (currentConnectedSuccess) {
			isDataBlock = true;
			//for (int i = 1; i <= 8; i++)
//				mCommand.requestCustomDoneness(1);
			Intent service = new Intent(BleService.SERVICE_ACTION);
			service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_REQUEST_CUSTOMDONENESS);
			service.putExtra(BleService.KEY_REQUEST_CUSTOMDONENESS, 1);
			service.setFlags(Service.START_FLAG_REDELIVERY);
			startService(service);
			layoutCurrentConnected.setVisibility(View.VISIBLE);
		} else {
			/*Intent service = new Intent(BleService.SERVICE_ACTION);
			service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_STOP_SCANNING);
			service.setFlags(Service.START_FLAG_REDELIVERY);
			startService(service);*/
//			stopScanning();
			layoutCurrentConnected.setVisibility(View.GONE);
		}
		String defAddress = dao.getLastConnectDeviceAddress();
		if (TextUtils.isEmpty(defAddress)) {
			switchBleConnection.setChecked(false);
		} else {
			switchBleConnection.setChecked(true);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
//        mLeDeviceListAdapter.clear();
		super.onPause();
		Intent service = new Intent(BleService.SERVICE_ACTION);
		service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_STOP_SCANNING);
		service.setFlags(Service.START_FLAG_REDELIVERY);
		startService(service);
		unregisterReceiver(mGattUpdateReceiver);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dao.setLastScanSwitch(switchBleConnection.isChecked());
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	
	public void init(){
		versionnum = (TextView) findViewById(R.id.versionnum);
		versionnum.setText(""+getVersion(this));
		mCommand = BleBbqCommandManager.getInstance(this);
		// start to command request custom doneness level
		mDonenessList = new ArrayList<DonenessTemperature>();
		presetDonenessLevel = new SparseArray<DonenessTemperature>();
		
		//mCommand.setmDonenessCallback(new MyDonenessCallback());
		dao = SharingPreferenceDao.getInstance(this);
		
//		listScanedDevices = (ListView) findViewById(R.id.listScanedDevices);
//		mLeDeviceListAdapter = new LeDeviceListAdapter();
//		listScanedDevices.setAdapter(mLeDeviceListAdapter);
//		listScanedDevices.setOnItemClickListener(this);
		meatList = (ListView) findViewById(R.id.meatlist);
		settingsbackiv = (ImageView) findViewById(R.id.settingsbackiv);
		setingsicon = (ImageView) findViewById(R.id.setingsicon);
		settingstv = (TextView) findViewById(R.id.settingstv);
		switchBleConnection = (Switch) findViewById(R.id.switchBleConnection);
		deviceSearchingHead = findViewById(R.id.deviceSearchingHead);
		
		
		inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		Resources res=getResources();
		meatnamearray = res.getStringArray(R.array.meatname);
		//initializeDonenessLevel() ;
		
		
//		setListViewHeightBasedOnChildren(meatList);
		
		setingsicon.setOnClickListener(this);
		settingsbackiv.setOnClickListener(this);
		settingstv.setOnClickListener(this);
		
		// Modified by aaronli Mar 6
		/*if (dao.isLastScanSwitch()) {
			switchBleConnection.setChecked(true);
			startScanning();
		}*/
		
		switchBleConnection.setOnCheckedChangeListener(this);
		switchtempcf = (Switch) findViewById(R.id.switchtempcf);
		switchtempcf.setOnCheckedChangeListener(this);
		
		switchtempcf.setChecked(BbqConfig.TEMPERATURE_UNIT_F.equals(dao.getShowingTemperatureUnit()));
		
		layoutCurrentConnected = findViewById(R.id.layoutCurrentConnected);
		defaultAddress = dao.getLastConnectDeviceAddress();
		
			
	}
	
	public static String getVersion(Context context)//��ȡ�汾��
	{
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return context.getString(R.string.version_unknown);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.meattype:
			//Log.e("cdf","one");
			break;
		case R.id.targettemperature:
			//Log.e("cdf","two");
			break;
		case R.id.timer:
			//Log.e("cdf","three");
			break;
		case R.id.settingsbackiv:
			finish();
			break;
		case R.id.setingsicon:
			finish();
			break;
		case R.id.settingstv:
			finish();
			break;

		default:
			break;
		}
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == RESULT_SETUP_DONENESS) {
			preSetupDoneness(data);
		}
	}

	/**
	 * @param data
	 */
	private void preSetupDoneness(Intent data) {
		isDataBlock = true;
		boolean custom = data.getBooleanExtra(KEY_CUSTOM, false);
		if (custom) {
			DonenessTemperature dt = (DonenessTemperature) data.getSerializableExtra(KEY_MEATTYPE);
			//presetDonenessLevel.put(dt.getMeatTypeIndex(), dt);
			//mCommand.setCustomDonenessTemperature(dt); 
			Intent service = new Intent(BleService.SERVICE_ACTION);
			service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_SET_CUSTOMDONENESSTEMPERATURE);
			service.putExtra(BleService.KEY_SET_CUSTOMDONENESSTEMPERATURE, dt);
			service.setFlags(Service.START_FLAG_REDELIVERY);
			startService(service);
		} else {
			//mCommand.clearCustomDoneness();
			Intent service = new Intent(BleService.SERVICE_ACTION);
			service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_CLEAR_CUSTOMDONENESS);
			service.setFlags(Service.START_FLAG_REDELIVERY);
			startService(service);
		}
		mDonenessList.clear();
		meatadapter.notifyDataSetChanged();
		//mCommand.requestCustomDoneness(1);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
			case R.id.switchBleConnection:
			{
				/*if (layoutCurrentConnected != null) {
					layoutCurrentConnected.setVisibility(View.GONE);
				}*/
				if (isChecked) {
					//Log.d(TAG, "isChecked");
					startScanning();
				} else {
					stopScanning();
				}
				break;
			}
			case R.id.switchtempcf:
//				SharedPreferences tempcf = getSharedPreferences("ischeckf",0);
//				Editor editor = tempcf.edit();
				
				if(isChecked){
//					editor.putBoolean("ischeckf", true);
					dao.setShowingTemperatureUnit(BbqConfig.TEMPERATURE_UNIT_F);
				}else{
					//editor.putBoolean("ischeckf", false);
					dao.setShowingTemperatureUnit(BbqConfig.TEMPERATURE_UNIT_C);
				}
//				editor.commit();
				break;
			default:
				break;
		}
		
	}

/*	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mBluetoothConnectionManager.scanLeDevice(false);
		String address = (String) view.getTag(R.id.bleDeviceAddress);
		setDefaultDeviceAndConnect(address);
	}*/
	
	private void startScanning() {
//		mCommand.setmGattCallback(null);
		if (TextUtils.isEmpty(dao.getLastConnectDeviceAddress()) || 
				mCommand.getConnectionState(dao.getLastConnectDeviceAddress()) == BluetoothProfile.STATE_DISCONNECTED) {
//			mBluetoothConnectionManager.scanLeDevice(true);
			Intent service = new Intent(BleService.SERVICE_ACTION );
			service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_CONNECT_SCAN);
			service.setFlags(Service.START_FLAG_REDELIVERY);
			startService(service);
			layoutCurrentConnected.setVisibility(View.GONE);
		}
	}
	
	private void stopScanning() {
		//Log.d(TAG, "unChecked");
		Intent service = new Intent(BleService.SERVICE_ACTION);
		service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_STOP_FORCE_DISCONNECT);
		service.setFlags(Service.START_FLAG_REDELIVERY);
		startService(service);
//		mCommand.close();
		dao.setLastConnectDeviceAddress("");
//		mLeDevices.clear();
//		mLeDeviceListAdapter.notifyDataSetChanged();
		connectiingDevice = null;
		layoutCurrentConnected.setVisibility(View.GONE);
	}
	
	private void initializeDonenessLevel() {
		/*if (mDonenessList == null) {
			mDonenessList = new ArrayList<DonenessTemperature>();
		}*/
		//String[] meatnamearray = getResources().getStringArray(R.array.meatname);
	/*	DonenessTemperature dt;
		for (int i = 1; i < meatnamearray.length; i ++) {
			dt = new DonenessTemperature();
			dt.setMeatTypeIndex(i);
			mDonenessList.add(dt);
		}*/
		if (meatadapter == null) {
			meatadapter = new MeatListAdapter(mDonenessList);
			meatList.setAdapter(meatadapter);
			meatList.setOnItemClickListener(meatadapter);
		} else {
			meatadapter.notifyDataSetChanged();
		}
	}
	
	private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_CONNECT_STATE_CHANGED);
        intentFilter.addAction(BleService.ACTION_COMMAND_CALLBACK);
        return intentFilter;
    }
	
	private void setDonenessLevel() {
		
	}
	
/*	private void setDefaultDeviceAndConnect(final String address) {
		defaultAddress = address;
		dao.setLastConnectDeviceAddress(address);
//		mLeDeviceListAdapter.notifyDataSetChanged();
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				mCommand.pairDevices(address);
				layoutCurrentConnected.setVisibility(View.VISIBLE);
				
			}
		});
	}*/
	
/*	private class MyBleScanningCallback implements OnBleScanningCallback {

		@Override
		public void onDescoverDevices(BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			if (connectiingDevice == null
        			&& device.getName() != null 
        			&& device.getName().contains(SampleGattAttributes.BLE_BBQ_SCANNABLE)) {
				mLeDeviceListAdapter.addDevice(device);
				mLeDeviceListAdapter.notifyDataSetChanged();
				connectiingDevice = device;
				runOnUiThread(new Runnable() {
					public void run() {
						mBluetoothConnectionManager.scanLeDevice(false);
					}
				});
			}
		}

		@Override
		public void onStopScanning() {
			deviceSearchingHead.setVisibility(View.GONE);
//			if (switchBleConnection.isChecked() && (mLeDevices == null || mLeDevices.isEmpty())) {
			if (switchBleConnection.isChecked() && (connectiingDevice == null)) {
				mBluetoothConnectionManager.scanLeDevice(true);
			} else {
				try {
					String address = mLeDevices.get(0).getAddress();
					setDefaultDeviceAndConnect(address);
				} catch (Exception e) {
					e.printStackTrace();
				}
				setDefaultDeviceAndConnect(connectiingDevice.getAddress());
			}
			
		}

		

		@Override
		public void onScaningStateChanged(BlueConnectState state) {

			if (BlueConnectState.SCANNING.equals(state)) {
				deviceSearchingHead.setVisibility(View.VISIBLE);
//				if (listScanedDevices != null)
//					listScanedDevices.addHeaderView(deviceSearchingHead);

			} else {
				deviceSearchingHead.setVisibility(View.GONE);
//				if (listScanedDevices != null)
//					listScanedDevices.addHeaderView(deviceSearchingHead);
				if (connectiingDevice != null) {
					setDefaultDeviceAndConnect(connectiingDevice.getAddress());
				}

			}

		}
		
	}*/
	

	private class MeatListAdapter extends BaseAdapter implements OnClickListener, OnItemClickListener {
		
		private List<DonenessTemperature> data;
		
		/**
		 * @param data
		 */
		public MeatListAdapter(List<DonenessTemperature> data) {
			super();
			this.data = data;
		}

		@Override
		public int getCount() {
			if (data != null) {
				return data.size();
			}
			return 0;
		}

		@Override
		public DonenessTemperature getItem(int position) {
			if (data != null) {
				return data.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			if (data != null) {
				return data.get(position).getMeatTypeIndex();
			}
			return -1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView == null){
				convertView =inflater.inflate(R.layout.meatlist, parent, false);  
				holder = new Holder();
				holder.meatnames = (TextView) convertView.findViewById(R.id.meatnames);
				holder.defcus = (TextView) convertView.findViewById(R.id.defcus);
				convertView.setTag(holder);
				//convertView.setOnClickListener(this);
			} else {
				holder = (Holder) convertView.getTag();
			}
			//Log.e("cdf","-----------"+meatnamearray[position]);
			holder.meatnames.setText(meatnamearray[(int) getItemId(position)]);
			if (!data.get(position).isCustom()) {
				holder.defcus.setText(R.string.str_doneness_default);
			} else {
				holder.defcus.setText(R.string.str_doneness_customize);
			}
			convertView.setTag(R.id.meatDonenessLevel, getItem(position));
			return convertView;
		}
		
		@Override
		public void onClick(View v) {
			
			//finish();
		}
		
		class Holder{
			private TextView defcus;
			private TextView meatnames;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!isDataBlock) {
				Intent i = new Intent(SettingsActivity.this, DonenessActivity.class);
				i.putExtra(KEY_MEATTYPE, (DonenessTemperature)view.getTag(R.id.meatDonenessLevel));
				startActivityForResult(i, 1);
			}
		}
	}

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_CONNECT_STATE_CHANGED.equals(action)) {
            	Log.d(TAG, "ACTION_CONNECT_STATE_CHANGED " + intent.getSerializableExtra(BleService.KEY_BROADCAST_VALUE));
            	BlueConnectState state = (BlueConnectState) intent.getSerializableExtra(BleService.KEY_BROADCAST_VALUE);
            	switch (state) {
				case SCANNING:
					deviceSearchingHead.setVisibility(View.VISIBLE);
					layoutCurrentConnected.setVisibility(View.GONE);
					break;
				case PAIRED_OK:
					deviceSearchingHead.setVisibility(View.GONE);
					layoutCurrentConnected.setVisibility(View.VISIBLE);
					break;
				case DISCONNECTED:
					deviceSearchingHead.setVisibility(View.GONE);
					layoutCurrentConnected.setVisibility(View.GONE);
					if (!mCommand.isBluetoothEnabled()) {
						finish();
					}
					break;
				default:
//					deviceSearchingHead.setVisibility(View.GONE);
					layoutCurrentConnected.setVisibility(View.GONE);
					break;
				}
            }
            if (BleService.ACTION_COMMAND_CALLBACK.equals(action)) {
            	final String commandIndex = intent.getStringExtra(BleService.KEY_COMMAND_INDEX);
            	if (BleService.KEY_REQUEST_CUSTOMDONENESS.equals(commandIndex)) {
            		DonenessTemperature values = (DonenessTemperature) intent.getSerializableExtra(BleService.EXTRA_DATA);
            		if (values == null || values.getMeatTypeIndex() == 0x0f) {
						DonenessTemperature item;
						if (mDonenessList.isEmpty()) {
							for (int i = 1; i<=8; i++) {
								item = new DonenessTemperature();
								item.setMeatTypeIndex(i);
								mDonenessList.add(item);
							}
						}
						isDataBlock = false;
					} else {
						if (values.getMeatTypeIndex() == 1) {
							mDonenessList.clear();
						}
						
						mDonenessList.add(values);
						if (values.getMeatTypeIndex()> 0 && values.getMeatTypeIndex() < 8) {
							//mCommand.requestCustomDoneness(values.getMeatTypeIndex() + 1);
							try {
								Intent service = new Intent(BleService.SERVICE_ACTION);
								service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_REQUEST_CUSTOMDONENESS);
								service.putExtra(BleService.KEY_REQUEST_CUSTOMDONENESS, values.getMeatTypeIndex() + 1);
								service.setFlags(Service.START_FLAG_REDELIVERY);
								startService(service);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (values.getMeatTypeIndex() == 8) {
							isDataBlock = false;
						}
					}
					initializeDonenessLevel();
            	}
            }
        }
    };

}
