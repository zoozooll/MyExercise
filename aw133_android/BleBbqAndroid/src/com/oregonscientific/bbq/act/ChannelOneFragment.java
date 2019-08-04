/**
 * 
 */
package com.oregonscientific.bbq.act;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;


import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.CookingStatus;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqRecord;
import com.oregonscientific.bbq.ble.BleBbqCommandManager;
import com.oregonscientific.bbq.ble.CommandManager;
import com.oregonscientific.bbq.ble.CommandManager.BlueConnectState;
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.dao.DatabaseManager;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.history.WriteReadTempFile;
import com.oregonscientific.bbq.service.BleService;
import com.oregonscientific.bbq.utils.BbqConfig;
import com.oregonscientific.bbq.view.CalendarFilperView;
import com.oregonscientific.bbq.view.CalendarFilperView.OnDateTapListener;
import com.oregonscientific.bbq.view.CookProgressBar;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author aaronli
 *
 */
public class ChannelOneFragment extends Fragment implements OnClickListener {
	
	//private ArrayList<BbqRecord> BbqRecordPack = new ArrayList<BbqRecord>();
	private static final String TAG = "ChannelOneFragment";
	private static final int MSG_REFRESH = 1;
	
	private int[] meattypeIconId = ParseManager.MEAT_TYPE_ICONS;
	private String[] donenessLevelStrs;
	private String[] cookingStatusStrs;
	private OperationActivity mActivity;
	private View view = null;
	private ImageView ivTopIcon;
	private TextView tvwTopMsg;
	private TextView tvwTimer;
	private TextView tvwCookingPercental;
	private TextView tvwCurrentTemp,tvwCurrentTempUnit;
	private TextView tvwCurrentStatus;
	private TextView tvwCurrentTimer;
	private ProgressBar ivwCookingPercental,buttRightPro;
	private ImageView clockIcon,ivwCurrentTimerIcon;
	private CookProgressBar cpbPercetial;
	private TextView btnOperatingCtrl;
	private RelativeLayout startstoppro;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private Handler mtimingHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				updateTextView();
				break;
			default:
				break;
			}
		}
	};
	private int count;
	private int cost_time;
	private boolean isPause = false;
	private boolean isStop = true;
	
	// block data, if true, cannot send command to ble;
	private boolean isDataBlock;

	private static int delay = 1000;  //1s
	private static int period = 1000;  //1s
	private static int WriteInterval = 30;  //6s
	private static int DBInterval = 30;  //30s
	private static final int UPDATE_TEXTVIEW = 0;
	// The current channel setting fields
	private int channel;
	private Mode currentMode;
	private BBQDataSet currentData;
	//SharedPreferences tempcf;
	private String temperatureUnit;
	private Resources res;
	private WriteReadTempFile wrt;
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (OperationActivity) getActivity();
		donenessLevelStrs = getResources().getStringArray(R.array.donenessLevelStrs);
		cookingStatusStrs = getResources().getStringArray(R.array.operationStatusStrs);
		
		res = getResources();
		isDataBlock = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView "+ getArguments().getString(TAG));
		if (view == null)
			view = inflater.inflate(R.layout.homemid, container, false);
		initViews(view); 
		getCurrrentChannel();
		return view;
	}
	
	public static Fragment newInstance(String tag) {
		ChannelOneFragment one = new ChannelOneFragment();
		Bundle args = new Bundle();
        args.putString(TAG, tag);
        one.setArguments(args);
		return one;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		// get information of showing temperature unit
		CommandManager commandManager = BleBbqCommandManager.getInstance(mActivity);
		SharingPreferenceDao dao = SharingPreferenceDao.getInstance(mActivity);
		if (!(commandManager.getConnectionState(dao.getLastConnectDeviceAddress()) == BleBbqCommandManager.STATE_CONNECTED)) {
			initViewShowing();
		} else {
			// modified by aaronli at Apr 10. // Showing loading when return from other activity
			buttRightPro.setVisibility(View.VISIBLE);
			Intent service = new Intent(BleService.SERVICE_ACTION);
			service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_REQUEST_CHANNEL_DATA);
			service.putExtra(BleService.KEY_REQUEST_CHANNEL_DATA, channel);
			service.setFlags(Service.START_FLAG_REDELIVERY);
			mActivity.startService(service);
		}
		temperatureUnit  = dao.getShowingTemperatureUnit();
		mActivity.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		super.onStart();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		mActivity.unregisterReceiver(mGattUpdateReceiver);
	}

	public void notifyBbqDataChanged(BBQDataSet set) {
		if (currentMode == null)  {
			setCurrentMode(set.getMode());
		}
		if (currentData == null || !set.getStatus().equals(currentData.getStatus())) {
			switch (set.getStatus()) {
			case ALMOST:
				
				break;
			case READY:
							
				break;
			case OVERCOOK:
				
				break;

			default:
				break;
			}
		}
		currentData = set;
		if (isCookingStarted() && !currentMode.equals(set.getMode())) {
			setCurrentMode(set.getMode());
		}
		mHandler.sendEmptyMessage(MSG_REFRESH);
	}
	
	private void initViews(View rootView) {
		startstoppro = (RelativeLayout) rootView.findViewById(R.id.startstoppro);
		btnOperatingCtrl = (TextView) rootView.findViewById(R.id.btnOperatingCtrl);
		buttRightPro = (ProgressBar) rootView.findViewById(R.id.buttRightPro);
		ivTopIcon = (ImageView) rootView.findViewById(R.id.ivTopIcon);
		tvwTopMsg = (TextView) rootView.findViewById(R.id.tvwTopMsg);
		clockIcon = (ImageView) rootView.findViewById(R.id.clockicon);
		ivwCookingPercental = (ProgressBar) rootView.findViewById(R.id.ivwCookingPercental);
		tvwTimer = (TextView) rootView.findViewById(R.id.tvwTimer);
		tvwCookingPercental = (TextView) rootView.findViewById(R.id.tvwCookingPercental);
		tvwCurrentTemp = (TextView) rootView.findViewById(R.id.tvwCurrentTemp);
		tvwCurrentTempUnit = (TextView) rootView.findViewById(R.id.tvwCurrentTempUnit);
		tvwCurrentStatus = (TextView) rootView.findViewById(R.id.tvwCurrentStatus);
		ivwCurrentTimerIcon = (ImageView) rootView.findViewById(R.id.ivwCurrentTimerIcon);
		tvwCurrentTimer = (TextView) rootView.findViewById(R.id.tvwCurrentTimer);
		cpbPercetial = (CookProgressBar) rootView.findViewById(R.id.cpbPercetial);
		btnOperatingCtrl.setOnClickListener(this);
		startstoppro.setOnClickListener(this);
		
		tvwTimer.setOnClickListener(this);
		
	}
	
	private void initViewShowing() {
		tvwTopMsg.setText("");
		tvwCookingPercental.setText(R.string.percent);
		//cpbPercetial.setPercential(R.string.str_home_empty_temperature);
		tvwCurrentTemp.setText(R.string.str_home_empty_temperature);
		tvwCurrentStatus.setText("");
		tvwCurrentTimer.setText("");
		stopTimer();
		
	}

	
	public void setCurrentTemperatureText(){
		// Modified by aaronli at Mar20 2014. Showing empty temperature.
		if (currentData.getProbeTemperature() < 0 || currentData.getProbeTemperature() > 400) {
			tvwCurrentTemp.setText(R.string.str_home_empty_temperature);
			tvwCurrentTempUnit.setText("");
		} else {
			if(BbqConfig.TEMPERATURE_UNIT_F.equals(temperatureUnit)){
				BigDecimal b = new BigDecimal(currentData.getProbeTemperature());  
				tvwCurrentTemp.setText(String.valueOf(b.setScale(0,BigDecimal.ROUND_HALF_UP)));
				tvwCurrentTempUnit.setText(res.getString(R.string.tempf));
			}else if (BbqConfig.TEMPERATURE_UNIT_C.equals(temperatureUnit)) {
				float toc= ParseManager.tranFahrenheitToCelsius(currentData.getProbeTemperature());
				//String result = String.format("%.1f", toc);
				BigDecimal b = new BigDecimal(toc);  
				tvwCurrentTemp.setText(String.valueOf(b.setScale(0,BigDecimal.ROUND_HALF_UP)));
				tvwCurrentTempUnit.setText(res.getString(R.string.tempc));
			}
		}
	}
	
	public void setCurrentMode(Mode mode) {
		currentMode = mode;
	}
	
	public void notifyOperationMode(Mode mode) {
		if (currentData != null) {
			// block data set free; modified by aaronli at Mar 5 2014
			buttRightPro.setVisibility(View.GONE);
			isDataBlock = false;
			if (isCookingStarted()) {
				ivwCookingPercental.setVisibility(View.VISIBLE);
				btnOperatingCtrl.setText(R.string.str_home_stop);
				startTimer();
//				buttRightPro.setVisibility(View.GONE);
			} else {
				cost_time = count;
				ivwCookingPercental.setVisibility(View.INVISIBLE);
				btnOperatingCtrl.setText(R.string.str_home_start);
				stopTimer();
//				buttRightPro.setVisibility(View.VISIBLE);
				/*if (isCookingTimeup()) {
					stopTimer();
				} else {
					pauseTimer();
				}*/
			}
			switch (mode) {
			case MEAN_TYPE_MODE: {
				showingModeMeantype();
				break;
			}
			case TARGET_TEMPERATURE_MODE: {
				showingModeTargetTemperature() ;
				break;
			}
			case TIMER_MODE: {
				showingModeTimer();
				break;
			}	
			
			default:
				break;
			}
		}
	}
	
	private void showingModeMeantype() {
		//Log.d(TAG, "showingModeMeantype");
		ivTopIcon.setImageResource(meattypeIconId[currentData.getMeatTypeInt()]);
		tvwTopMsg.setText(donenessLevelStrs[currentData.getDonelessLevel().ordinal()]);
		tvwCookingPercental.setText(currentData.getPercentage() + "%");
		cpbPercetial.setPercential(currentData.getPercentage());
		setCurrentTemperatureText();
		tvwCurrentStatus.setText(cookingStatusStrs[currentData.getStatus().ordinal()]);
		//count = 0;
		clockIcon.setVisibility(View.VISIBLE);
		tvwTimer.setVisibility(View.VISIBLE);
//		ivwCookingPercental.setVisibility(View.VISIBLE);
		tvwCookingPercental.setVisibility(View.VISIBLE);
		ivwCurrentTimerIcon.setVisibility(View.GONE);
		tvwCurrentTimer.setVisibility(View.GONE);
	}
	
	private void showingModeTargetTemperature() {
		//Log.d(TAG, "showingModeTargetTemperature");
		ivTopIcon.setImageResource(R.drawable.icon_temperature);
		if(BbqConfig.TEMPERATURE_UNIT_F.equals(temperatureUnit)){
			tvwTopMsg.setText(String.format("%.0f", currentData.getTargetTemperature()) + res.getString(R.string.tempf));
			//tvwCurrentTemp.setText(String.valueOf(currentData.getProbeTemperature()) + res.getString(R.string.tempf));
		}else if (BbqConfig.TEMPERATURE_UNIT_C.equals(temperatureUnit)) {
			float toc= ParseManager.tranFahrenheitToCelsius(currentData.getTargetTemperature());
			String result = String.format("%.0f", toc);
			tvwTopMsg.setText(result + res.getString(R.string.tempc));
			//tvwCurrentTemp.setText(String.valueOf(toc) + res.getString(R.string.tempc));
		}
		tvwCookingPercental.setText(currentData.getPercentage() + "%");
		cpbPercetial.setPercential(currentData.getPercentage());
		setCurrentTemperatureText();
		tvwCurrentStatus.setText(cookingStatusStrs[currentData.getStatus().ordinal()]);
		//count = 0; 
		clockIcon.setVisibility(View.VISIBLE);
		tvwTimer.setVisibility(View.VISIBLE);
//		ivwCookingPercental.setVisibility(View.VISIBLE);
		tvwCookingPercental.setVisibility(View.VISIBLE);
		ivwCurrentTimerIcon.setVisibility(View.GONE);
		tvwCurrentTimer.setVisibility(View.GONE);
	}
	
	private void showingModeTimer() {
		//Log.d(TAG, "showingModeTimer");
		ivTopIcon.setImageResource(R.drawable.icon_timer);
		tvwTopMsg.setText(String.format(res.getString(R.string.str_timer), currentData.getReloadTimer().getHours(), currentData.getReloadTimer().getMinute(), currentData.getReloadTimer().getSecond()));
		//tvwCookingPercental.setText(currentData.getPercentage() + "%");
		cpbPercetial.setPercential(0);
		if (currentData.isUpCountTimer()) {
			//tvwCurrentTimer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconcountup, 0, 0, 0);
			ivwCurrentTimerIcon.setImageResource(R.drawable.iconcountup);
		} else {
			//tvwCurrentTimer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconcountdown, 0, 0, 0);
			ivwCurrentTimerIcon.setImageResource(R.drawable.iconcountdown);
		}
		count = currentData.getCurrentTimer().totalSeconds();
		//tvwCurrentTimer.setText(currentData.getCurrentTimer().toString());
		setCurrentTemperatureText();
		tvwCurrentStatus.setText(cookingStatusStrs[currentData.getStatus().ordinal()]);
		clockIcon.setVisibility(View.GONE);
		tvwTimer.setVisibility(View.GONE);
		ivwCookingPercental.setVisibility(View.GONE);
		tvwCookingPercental.setVisibility(View.GONE);
		ivwCurrentTimerIcon.setVisibility(View.VISIBLE);
		tvwCurrentTimer.setVisibility(View.VISIBLE);
		
	}
	
	private IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_COMMAND_CHANNEL_DATA);
        intentFilter.addAction(BleService.ACTION_CONNECT_STATE_CHANGED);
        intentFilter.addAction(BleService.ACTION_COMMAND_CALLBACK);
        return intentFilter;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvwTimer: 
			break;
		case R.id.btnOperatingCtrl: 
		case R.id.startstoppro: 
			if (!isDataBlock) {
				if (!isCookingStarted()) {
					Intent service = new Intent(BleService.SERVICE_ACTION);
					service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_SET_OPERATINGSTART);
					service.putExtra(BleService.KEY_SET_OPERATINGSTART+"channel", channel);
					service.putExtra(BleService.KEY_SET_OPERATINGSTART+"mode", currentMode);
					service.setFlags(Service.START_FLAG_REDELIVERY);
					mActivity.startService(service);
				} else {
					Intent service = new Intent(BleService.SERVICE_ACTION);
					service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_SET_OPERATINGSTOP);
					service.putExtra(BleService.KEY_SET_OPERATINGSTOP, channel);
					service.setFlags(Service.START_FLAG_REDELIVERY);
					mActivity.startService(service);
				}
				buttRightPro.setVisibility(View.VISIBLE);
				isDataBlock = true;
			}
			break;
			
		

		default:
			break;
		}
		
	}
	
	public boolean isCookingStarted() {
		return currentData.getStatus().ordinal() >= 1 && currentData.getStatus().ordinal() <= 4;
	}
	
	public boolean isCookingTimeup() {
		return currentData.getStatus().ordinal() >= 3 && currentData.getStatus().ordinal() <= 4;
	}
	
	private void updateTextView(){
		switch (currentMode) {
		case MEAN_TYPE_MODE:
		case TARGET_TEMPERATURE_MODE:
			tvwTimer.setText(countToString(count));
			break;
		case TIMER_MODE:
			tvwCurrentTimer.setText(countToString(count));
			break;
		default:
			break;
		}
	}
	public String countToString(int count){
		String min = (count/60)+" m ";
		String sec = (count%60)+" s";
		return min+sec;
	}

	public void startTimer(){
		//zjz add
			
		if (!isStop) {
			resumeTimer();
			return;
		}
		isStop = false;
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					//Log.i(TAG, "count: "+String.valueOf(count));
					sendMessage(UPDATE_TEXTVIEW);
					do {
						try {
							//Log.i(TAG, "sleep(1000)...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}	
					} while (isPause);
					if (Mode.TIMER_MODE.equals(currentMode) && !currentData.isUpCountTimer()) {
						count --;
						//TODO 开始每分钟保存温度 count % 60 == 0
						if(count % WriteInterval == 0){
							//JsonWriter.getBbqRecordone(channel,currentData);
							wrt.getEveryHalfMinuteTemperature(currentData);
						}
						if (count < 0) {
							count = 0;
						} 
					} else {
						count ++;  
						//TODO 开始每分钟保存温度 count % 60 == 0
						if(count % WriteInterval == 0){
							//JsonWriter.getBbqRecordone(channel,currentData);
							wrt.getEveryHalfMinuteTemperature(currentData);
						}
					}
				}
			};
		}
		if(mTimer != null && mTimerTask != null ){
			mTimer.schedule(mTimerTask, delay, period);
		}
		// TODO 保存开始的温度
		//if(count % WriteInterval == 0){
			//JsonWriter.getBbqRecordone(channel,currentData);
		wrt = new WriteReadTempFile();
		
		wrt.getEveryHalfMinuteTemperature(currentData);
		//}
	}

	public void stopTimer(){
		if (isStop) {
			return;
		}
		isStop = true;
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
//<<<<<<< .mine
		// TODO 保存结束的温度
		ArrayList<Float>  AllTemperature = wrt.getEveryHalfMinuteTemperature(currentData);
		if (Mode.TIMER_MODE.equals(currentMode)) {
			if ( !currentData.isUpCountTimer()) 
				cost_time = currentData.getReloadTimer().getSecond() + cost_time;
			else 
				cost_time = currentData.getReloadTimer().getSecond() - cost_time;
		} else {
			cost_time = count;
		}
		// 根据isCookingTimeup() == true即保存一条数据库记录。并且把保存的每分钟温度值。  == false则删除每分钟的文件		
		if(cost_time > DBInterval)//if(isCookingTimeup() == true)60*3
		{
			//add temperature to JSON file(aw3312.txt)		
			//WriteReadTempFile writeReadTempFile = new WriteReadTempFile();			
			wrt.writeTempToFile();
			//add BbqRecord to DB
			BbqRecord record = wrt.PackRecord(channel,currentData,cost_time);
			DatabaseManager.instance(mActivity).insertSingleRecord(record);
		}
		wrt.recyle();
//		AllTemperature.clear();  
//=======

		reloadTimer();
//>>>>>>> .r160
	}
	
	private void reloadTimer() {
		if (Mode.TIMER_MODE.equals(currentMode) && !currentData.isUpCountTimer()) {
			count = currentData.getReloadTimer().totalSeconds();
		} else {
			count = 0;
		}
		
	}

	public void pauseTimer() {
		if(!isPause)
			isPause = true;
	}
	
	public void resumeTimer() {
		if (isPause)
			isPause = false;
	}
	
	public void sendMessage(int id){
		if (mtimingHandler != null) {
			Message message = Message.obtain(mtimingHandler, id);   
			mtimingHandler.sendMessage(message); 
		}
	}
	
	private void getCurrrentChannel() {
		String tagMsg = getArguments().getString(TAG);
		if (OperationActivity.CHANNELONE.equalsIgnoreCase(tagMsg)) {
			channel = 1;
		} else if (OperationActivity.CHANNELTWO.equalsIgnoreCase(tagMsg)) {
			channel = 2;
		}
	}
	
	public Mode getCurrentMode() {
		return currentMode;
	}
	
	/**
	 * @return the currentData
	 */
	public BBQDataSet getCurrentData() {
		return currentData;
	}

	/**
	 * @return the isDataBlock
	 */
	public boolean isDataBlock() {
		return isDataBlock;
	}

	private Handler mHandler  = new Handler() {

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REFRESH: {
				notifyOperationMode(currentMode);
				break;
			}
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BleService.ACTION_COMMAND_CHANNEL_DATA.equals(action)) {
            	final int channel = intent.getIntExtra(BleService.KEY_REQUEST_CHANNEL_DATA + "channel", 0);
            	if (channel == ChannelOneFragment.this.channel) {
            		notifyBbqDataChanged((BBQDataSet) intent.getSerializableExtra(BleService.KEY_REQUEST_CHANNEL_DATA));
            	}
            }
            if (BleService.ACTION_CONNECT_STATE_CHANGED.equals(action)) {
            	/*if (intent.getIntExtra(BleService.ACTION_CONNECT_STATE_CHANGED, 0) == BleService.CONNECT_STATUS_DISCONNECT) {
            		initViewShowing();
            	}
            	if (intent.getIntExtra(BleService.ACTION_CONNECT_STATE_CHANGED, 0) == BleService.CONNECT_STATUS_DISCONNECT) {
            		initViewShowing();
            	}*/
            	
            	switch ((BlueConnectState)(intent.getSerializableExtra(BleService.KEY_BROADCAST_VALUE))) {
				case DISCONNECTED:
					initViewShowing();
					break;
				case PAIRED_OK:
					Intent service = new Intent(BleService.SERVICE_ACTION);
					service.putExtra(BleService.KEY_COMMAND_INDEX, BleService.KEY_REQUEST_CHANNEL_DATA);
					service.putExtra(BleService.KEY_REQUEST_CHANNEL_DATA, channel);
					service.setFlags(Service.START_FLAG_REDELIVERY);
					mActivity.startService(service);
					break;
				default:
					break;
				}
            } 
            if (BleService.ACTION_COMMAND_CALLBACK.equals(action)) {
            	String errorStr = intent.getStringExtra(BleService.EXTRA_ERROR);
            	if (!TextUtils.isEmpty(errorStr)) {
            		Toast.makeText(mActivity, errorStr, Toast.LENGTH_SHORT).show();
            	}
            }
        }
    };
	
}
