/**
 * 
 */
package com.oregonscientific.bbq.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.ble.BbqDeviceCallback;
import com.oregonscientific.bbq.ble.BleBbqCommandManager;
import com.oregonscientific.bbq.ble.CommandManager.BlueConnectState;
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.dao.DatabaseManager;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.drawer.NavDrawerItem;
import com.oregonscientific.bbq.drawer.NavDrawerListAdapter;
import com.oregonscientific.bbq.service.BleService;

/**
 * @author aaronli
 *
 */
public class OperationActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private static final String TAG = "OperationActivity";
	private ImageView menuimg,editmode,settingsButt;
	private TextView chooseone,choosetwo;
	private Map<String, Fragment> mFragments;
	public static final String EXTRA_EDIT_KEY_CHANNEL = "edit_channel";
	public static final String EXTRA_EDIT_KEY_MODE = "edit_mode";
	public static final String EXTRA_EDIT_KEY_SETTINGS = "edit_settings";
	public static final String CHANNELONE = "channelone";
	public static final String CHANNELTWO = "channeltwo";
	
	private ChannelOneFragment mCurrentFragment=null;
	private String mCurrentTag;
//	private String mDeviceAddress;
	//private BBQDataSet mCurrentData;
	private SharingPreferenceDao spDao;
	private  DatabaseManager dbManager;
	private static final String STATE_TAG = "tag";
	//private BluetoothLeService mBluetoothLeService;
	private BleBbqCommandManager mCommandManager;
	private boolean mConnected;
	
	private String[] mNavMenuTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private List<NavDrawerItem> mNavDrawerItems;
	private TypedArray mNavMenuIconsTypeArray;
	private NavDrawerListAdapter mAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private  boolean deviceJoined;
	//private BbqDeviceCallback mCallBack =  new BleCallback();
	protected BleService mBluetoothLeService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Added by aaronli at Mar 7 2014 for checking if the blue tooth is turn on.
		// Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
		//setContentView(R.layout.homes);
		setContentView(R.layout.activity_main);
		spDao = SharingPreferenceDao.getInstance(this);
		dbManager = DatabaseManager.instance(this);
		init();
		mFragments = new HashMap<String, Fragment>();
		mCurrentTag = spDao.getKeyValue(STATE_TAG, CHANNELONE);
		//showDetails(mCurrentTag);
		//replaceFrag(mCurrentTag);
		initializeConnectDevice();
		Intent gattServiceIntent = new Intent(this, BleService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}
	
	 @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume");
        // for the device connect to another device on SettingsActivity. 
        // Modified by aaronli at Mar 7
//        mCommandManager.setmGattCallback(mCallBack);
        if (mCurrentFragment == null) {
        	replaceFrag(CHANNELONE);
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        /*if (mBluetoothLeService != null && !TextUtils.isEmpty(mDeviceAddress)) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + mDeviceAddress);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.d(TAG, "onPause");
        unregisterReceiver(mGattUpdateReceiver);
        //mCommandManager.setmGattCallback(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.d(TAG, "onDestroy");
        Intent intent = new Intent(BleService.SERVICE_ACTION);
        intent.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_STOP_DISCONNECT);
        intent.setFlags(Service.START_FLAG_REDELIVERY);
		startService(intent);
        unbindService(mServiceConnection);
        spDao.setKeyValue(STATE_TAG, mCurrentTag);
       // unbindService(mServiceConnection);
//        if (mCommandManager != null)
//        	mCommandManager.close();
//        mCommandManager.setmGattCallback(null);
        dbManager.close();
        deviceJoined = false;
        
    }
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if (!mCommandManager.isBluetoothEnabled()) {
			finish();
			return;
		}
	}

	

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
//		connectHandler.removeCallbacksAndMessages(null);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult "+ resultCode);
		if (requestCode == 1 && resultCode == 1) { 
			Mode setMode = (Mode) data.getSerializableExtra(EXTRA_EDIT_KEY_MODE);
			if (setMode != null) {
				mCurrentFragment.setCurrentMode(setMode);
				mCurrentFragment.notifyOperationMode(setMode);
			}
			BbqSettings settings = (BbqSettings) data.getSerializableExtra(EXTRA_EDIT_KEY_SETTINGS);
			if (settings != null) {
//				mCommandManager.setupBbqChannel(settings);
				Intent service = new Intent(BleService.SERVICE_ACTION);
				service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_SETUP_BBQCHANNEL);
				service.putExtra(BleService.KEY_SETUP_BBQCHANNEL, settings);
				service.setFlags(Service.START_FLAG_REDELIVERY);
				startService(service);
			}
		} else if (requestCode == 2) {
			if (mCommandManager.getConnectionState(spDao.getLastConnectDeviceAddress()) == BleBbqCommandManager.STATE_DISCONNECTED) {
				 if (!TextUtils.isEmpty(spDao.getLastConnectDeviceAddress())) {
		            	Intent intent = new Intent(BleService.SERVICE_ACTION);
		            	intent.setFlags(Service.START_FLAG_REDELIVERY);
		            	intent.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_CONNECT_DEVICE);
		            	startService(intent);
		         }
			}
			
		}

	}

	public void init(){
		
		mCommandManager = BleBbqCommandManager.getInstance(this);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_title);
     	mNavMenuIconsTypeArray = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mNavDrawerItems = new ArrayList<NavDrawerItem>();
        
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIconsTypeArray.getResourceId(0, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIconsTypeArray.getResourceId(1, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIconsTypeArray.getResourceId(2, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIconsTypeArray.getResourceId(3, -1)));
		
		mNavMenuIconsTypeArray.recycle();
		mAdapter = new NavDrawerListAdapter(getApplicationContext(),mNavDrawerItems);
		TextView tv = new TextView(getApplicationContext());
		tv.setText(R.string.munu_drawerlayout);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setBackgroundResource(R.drawable.actionbarbg);
		mDrawerList.addHeaderView(tv,null,false);
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(this);
		
		menuimg = (ImageView) findViewById(R.id.menuimg);
		editmode = (ImageView) findViewById(R.id.editmode);
		settingsButt = (ImageView) findViewById(R.id.settingsButt);
		chooseone = (TextView) findViewById(R.id.chooseone);
		choosetwo = (TextView) findViewById(R.id.choosetwo);
		
		menuimg.setOnClickListener(this);
		editmode.setOnClickListener(this);
		settingsButt.setOnClickListener(this);
		chooseone.setOnClickListener(this);
		choosetwo.setOnClickListener(this);
	}
	
	
	public void replaceFrag(String tag){
		// Added by aaronli at Mar 6 2014
		// block data cannot change fragments;
		if (mCurrentFragment != null && mCurrentFragment.isDataBlock()) {
			return;
		}
		Fragment fragment = mFragments.get(tag);
		if (fragment == null) 
		{
			fragment = ChannelOneFragment.newInstance(tag);
	    	mFragments.put(tag, fragment);
	    }
		if (CHANNELONE.equals(tag)) 
		{
//			if (mCommandManager != null) {
//				mCommandManager.requestChannelData(1);
//			}
			chooseone.setBackgroundResource(R.drawable.tab_selected_left);
			choosetwo.setBackgroundResource(R.drawable.tab_unselected_right);
		} else if (CHANNELTWO.equals(tag)) 
		{
//			if (mCommandManager != null) {
//				mCommandManager.requestChannelData(2);
//			}
			chooseone.setBackgroundResource(R.drawable.tab_unselected_left);
			choosetwo.setBackgroundResource(R.drawable.tab_selected_right);
		}
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
//		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//		transaction.addToBackStack(null);
		transaction.commit();
		mCurrentTag = tag;
		mCurrentFragment = (ChannelOneFragment) fragment;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem(position);
	}
	private void selectItem(int position) {
		switch (position) {
		case 1:
			Intent intent1 = new Intent(OperationActivity.this, OperationActivity.class);
	        startActivity(intent1);
			break;
		case 2:
			Intent intent2 = new Intent(OperationActivity.this, HistoryActivity.class);
	        startActivity(intent2);
			break;
		case 3:
			Intent intent3 = new Intent(OperationActivity.this, RecipeActivity.class);
	        startActivity(intent3);
			break;
		case 4:
			Intent intent4 = new Intent(OperationActivity.this, SharingActivity.class);
	        startActivity(intent4);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menuimg:
			mDrawerLayout.openDrawer(mDrawerList); 
			break;
		case R.id.editmode: {
			if (mCurrentFragment != null && mCurrentFragment.getCurrentData() != null && !mCurrentFragment.isCookingStarted()) {
				jumpToChannelSetting();
			}
			break;
		}
		case R.id.settingsButt:
			//if (mCurrentFragment != null && mCurrentFragment.getCurrentData() != null && !mCurrentFragment.isCookingStarted()) {
				Intent settingsintent = new Intent(OperationActivity.this,SettingsActivity.class);
				startActivityForResult(settingsintent, 2);
				break;
			//}
		case R.id.chooseone:
			//Log.e("cdf","one");
			replaceFrag(CHANNELONE);
			break;
		case R.id.choosetwo:
			//Log.e("cdf","two");
			replaceFrag(CHANNELTWO);
			break;
		default:
			break;
		}
	}
	
	private void onConnectSuccess() {
		//spDao.setLastConnectDeviceAddress(mDeviceAddress);
	}
	
//	public void startOperation(int ch, Mode mode) {
//		mCommandManager.setOperatingStart(ch, mode);
//	}
	
//	public void stopOperation(int ch) {
//		mCommandManager.setOperatingStop(ch);
//	}
	
	/**
	 * 
	 */
	private void forceSettingDevices() {
		Toast.makeText(this, R.string.err_connect, Toast.LENGTH_LONG).show();
		/*Intent intent = new Intent();
		intent.setClass(OperationActivity.this, SettingsActivity.class);
		startActivityForResult(intent, 2);*/
	}
	
	/**
	 * 
	 */
	private void initializeConnectDevice() {
		
		if(!mCommandManager.isBluetoothEnabled()) {
			//Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, 1);
           // forceSettingDevices();
			forceSettingDevices();
			finish();
            return;
		}
/*		mDeviceAddress = getIntent().getStringExtra(BluetoothConnectionManager.ATTR_CONNECTED_ADDRESS);
		if (TextUtils.isEmpty(mDeviceAddress)) {
			mDeviceAddress = spDao.getLastConnectDeviceAddress();
		} 
		if (TextUtils.isEmpty(mDeviceAddress)) {
			forceSettingDevices();
			return;
		}*/
//		if(mCommandManager.getConnectionState() != BleBbqCommandManager.STATE_CONNECTED) {
			//mConnectManager.scanLeDevice(true);
//		} else {
//			connectHandler.sendEmptyMessage(2);
//		}
		
		/*if (!mCommandManager.pairDevices(mDeviceAddress)) {
			Log.v(TAG, "pairDevices true");
			forceConnectDeviceLater();
			return;
		}*/
//		connectHandler.sendEmptyMessage(2);
	}
	
	/**
	 * 
	 */
	private void jumpToChannelSetting() {
		Intent modeintent = new Intent(OperationActivity.this,ModeActivity.class);
		modeintent.putExtra(EXTRA_EDIT_KEY_CHANNEL, mCurrentTag);
		modeintent.putExtra(EXTRA_EDIT_KEY_MODE, mCurrentFragment.getCurrentMode());
		modeintent.putExtra(EXTRA_EDIT_KEY_SETTINGS, ParseManager.dataToSetting(mCurrentFragment.getCurrentData()));
		startActivityForResult(modeintent, 1);
	}
	
	private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_CONNECT_STATE_CHANGED);
        return intentFilter;
    }
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_CONNECT_STATE_CHANGED.equals(action)) {
            	Log.d(TAG, "ACTION_CONNECT_STATE_CHANGED " + intent.getIntExtra(BleService.ACTION_CONNECT_STATE_CHANGED, 0));
            	BlueConnectState state = (BlueConnectState) intent.getSerializableExtra(BleService.KEY_BROADCAST_VALUE);
            	switch (state) {
				case DISCONNECTED:
					if (!mCommandManager.isBluetoothEnabled()) {
						finish();
						return;
					}
					break;

				default:
					break;
				}
            }
        }
    };
	
/*	private class BleCallback implements BbqDeviceCallback{

		@Override
		public void onDeviceJoin() {
			Log.d(TAG, "onDeviceJoin");
			deviceJoined = true;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(findViewById(R.id.fragment_container) != null && mCurrentFragment == null){
						replaceFrag(CHANNELONE);
					}
					
				}
			});
			onConnectSuccess();
		}

		@Override
		public void replyChannelDataResend(int channel, BBQDataSet value) {
			if (CHANNELONE.equals(mCurrentTag )){
				if (channel != 1) {
					return;
				}
				
			} else if (CHANNELTWO.equals(mCurrentTag )) {
				if (channel != 2) {
					return;
				}
			}
			//mCurrentData = value;
			try {
				mCurrentFragment.notifyBbqDataChanged(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void replySetupBbqChanged(String msg) {
			if (TextUtils.isEmpty(msg)) {
				int channel = 1;
				if (CHANNELONE.equals(mCurrentTag )){
					channel  = 1;
					
				} else if (CHANNELTWO.equals(mCurrentTag )) {
					channel = 2;
				}
				//mCommandManager.requestChannelData(channel);
			}
			
			
		}

		@Override
		public void indiacateStart(int channel, final String msg) {
			if (TextUtils.isEmpty(msg)) {
//				mCommandManager.requestChannelData(channel);
			} else {
//				Log.w(TAG, "indiacateStart error msg "+msg);
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(OperationActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		}

		@Override
		public void indiacateStop(int channel, final String msg) {
			Log.d(TAG, "onstop "+channel);
			if (TextUtils.isEmpty(msg)) { // if the msg is empty means that it is success to stop
				ChannelOneFragment f = null;
				if (channel == 1) {
					f = (ChannelOneFragment) mFragments.get(CHANNELONE);
				} else if (channel == 2) {
					f = (ChannelOneFragment) mFragments.get(CHANNELTWO);
				}
				if (f != null) {
					BbqRecord record = new BbqRecord();
					BBQDataSet data = f.getCurrentData();
					record.setChannel(channel);
					record.setCookingState(data.getStatus());
					record.setMode(data.getMode());
					record.setCostTime(100);
					record.setFinishedDate(System.currentTimeMillis());
					record.setFinishedTemperature(data.getProbeTemperature());
					record.setSetDoneness(data.getDonelessLevel());
					record.setSetMeattype(data.getMeatTypeInt());
					record.setSetTargeTemperature(data.getTargetTemperature());
					record.setSetTotalTime(data.getReloadTimer().totalSeconds());
					dbManager.insertSingleRecord(record);
				}
				mCommandManager.requestChannelData(channel);
			} else {
//				Log.w(TAG, "indiacateStart error msg "+msg);
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(OperationActivity.this, msg, Toast.LENGTH_SHORT).show();
					}
				});
			}
			
		}

		@Override
		public void onDisconnected() {
			deviceJoined = false;
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(OperationActivity.this, R.string.err_disconnected, Toast.LENGTH_LONG).show();
				}
			});
			//Log.d(TAG, "onDisconnected");
			//connectHandler.sendEmptyMessage(1);
		}

		
	}*/
	
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BleService.LocalBinder) service).getService();
            
            // Automatically connects to the device upon successful start-up initialization.
            if (!TextUtils.isEmpty(spDao.getLastConnectDeviceAddress())) {
            	Intent intent = new Intent(BleService.SERVICE_ACTION);
            	intent.setFlags(Service.START_FLAG_REDELIVERY);
            	intent.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_CONNECT_DEVICE);
            	startService(intent);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
	
}
