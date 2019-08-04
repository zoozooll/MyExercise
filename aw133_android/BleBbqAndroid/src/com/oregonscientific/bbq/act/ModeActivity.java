/**
 * 
 */
package com.oregonscientific.bbq.act;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.Timer;
import com.oregonscientific.bbq.ble.BleBbqCommandManager;
import com.oregonscientific.bbq.ble.ChannelSettingsChangedCallback;

public class ModeActivity extends Activity implements OnClickListener{
	
	private static final String TAG = "ModeActivity";
	
	public static final String MEATTYPE_FRAGMENT = "meattype";
	public static final String TEMPERATURE_FRAGMENT = "temperature";
	public static final String TIMER_FRAGMENT = "timer";
	public static final int RESULT_CHANGED = 1;
	public static final int RESULT_CANNEL = 0;
	
	private int currentChannel;
	private Map<String, Fragment> mFragments;
	private Mode firstMode;
	private String mCurrentTag;
	//private BleBbqCommandManager command;
	private BbqSettings mCurrentSet;
	//private Fragment fragment=null;
	private ModeMeatTypeFragment mMeatTypeFragment;
	private ModeTargetTemperatureFragment mTargetTemperatureFragment;
	private ModeTimerFragment mTimerFragment;
	
	private ImageView backiv;
	private TextView tvwChannelTitle,meattype,targettemperature,timer;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.modechoose);
		super.onCreate(savedInstanceState);
		mFragments = new HashMap<String, Fragment>();
		init();
		if(findViewById(R.id.fragment_mode_container) != null){
			/*ModeMeatTypeFragment modeMeatTypeFragment = new ModeMeatTypeFragment();
			Bundle args = new Bundle();
			modeMeatTypeFragment.setArguments(args);
			getFragmentManager().beginTransaction().add(R.id.fragment_mode_container, modeMeatTypeFragment).commit();*/
			Mode m = (Mode) getIntent().getSerializableExtra(OperationActivity.EXTRA_EDIT_KEY_MODE);
			firstMode = m;
			switch (m) {
			case MEAN_TYPE_MODE:
				replaceFrag(MEATTYPE_FRAGMENT);
				break;
			case TARGET_TEMPERATURE_MODE:
				replaceFrag(TEMPERATURE_FRAGMENT);
				break;
			case TIMER_MODE:
				replaceFrag(TIMER_FRAGMENT);
				break;
			default:
				break;
			}
		}
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		//command.setupBbqChannel(mCurrentSet);
		returnSettingsResult();
		super.finish();
	}

	/**
	 * 
	 */
	private void returnSettingsResult() {
		BbqSettings set = null;
		Mode settingMode = requestCurrentSettingMode();
		boolean changed = false;
		try {
			set = mCurrentSet.copy();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (mMeatTypeFragment != null) {
			mCurrentSet.setDonenessLevel(mMeatTypeFragment.getCurrentDoneness());
			mCurrentSet.setMeatTypeInt(mMeatTypeFragment.getCurrentMeattypeIndex());
		}
		
		if (mTargetTemperatureFragment != null) {
			mCurrentSet.setTargetTemperature(mTargetTemperatureFragment.getData());
		}
		if (mTimerFragment != null) {
			mCurrentSet.setReloadTimer(mTimerFragment.getData());
			mCurrentSet.setCurrentTimer(mTimerFragment.getData());
		}
		Intent data = new Intent();
		if (set != null && !set.equals(mCurrentSet)) {
			changed = true;
			data.putExtra(OperationActivity.EXTRA_EDIT_KEY_SETTINGS, mCurrentSet);
		}
		if (firstMode != null && !firstMode.equals(settingMode)) {
			changed = true;
			data.putExtra(OperationActivity.EXTRA_EDIT_KEY_MODE, settingMode);
		}
		if (changed) {
			setResult(RESULT_CHANGED, data);
		} 
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
//		command.setChannelSettingsChangedCallback(null);
	}


	public void init(){
		String channelTag = getIntent().getStringExtra(OperationActivity.EXTRA_EDIT_KEY_CHANNEL);
		getCurrrentChannel(channelTag);
//		command = BleBbqCommandManager.getInstance(this);
//		command.setChannelSettingsChangedCallback(new MyChannelSettingsChangedCallback());
		mCurrentSet = (BbqSettings) getIntent().getSerializableExtra(OperationActivity.EXTRA_EDIT_KEY_SETTINGS);
		mCurrentSet.setChannel(currentChannel);
		meattype = (TextView) findViewById(R.id.meattype);
		targettemperature = (TextView) findViewById(R.id.targettemperature);
		timer = (TextView) findViewById(R.id.timer);
		tvwChannelTitle = (TextView) findViewById(R.id.tvwChannelTitle);
		backiv = (ImageView) findViewById(R.id.ivwBack);
		tvwChannelTitle = (TextView) findViewById(R.id.tvwChannelTitle);
		if (currentChannel == 1) {
			tvwChannelTitle.setText(R.string.str_title_ch1);
		} else if (currentChannel == 2) {
			tvwChannelTitle.setText(R.string.str_title_ch2);
		}
		
		meattype.setOnClickListener(this);
		targettemperature.setOnClickListener(this);
		timer.setOnClickListener(this);
		backiv.setOnClickListener(this);
		tvwChannelTitle.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.meattype:
			//Log.e("cdf","one");
			replaceFrag(MEATTYPE_FRAGMENT);
			
			break;
		case R.id.targettemperature:
			//Log.e("cdf","two");
			replaceFrag(TEMPERATURE_FRAGMENT);
			
			break;
		case R.id.timer:
			//Log.e("cdf","three");
			replaceFrag(TIMER_FRAGMENT);
			
			break;
		case R.id.ivwBack:
			finish();
			break;
		case R.id.tvwChannelTitle:
			finish();
			break;
		

		default:
			break;
		}
	}
	
	public void replaceFrag(String tag){
		if (tag.equals(mCurrentTag)) {
			return;
		}
		Fragment fragment = mFragments.get(tag);
		if (fragment == null) 
		{
	    	if (MEATTYPE_FRAGMENT.equals(tag)) 
	    	{
	    		fragment = mMeatTypeFragment = ModeMeatTypeFragment.newInstance(tag);
	    		
	    	} else if (TEMPERATURE_FRAGMENT.equals(tag)) 
	    	{
	    		fragment = mTargetTemperatureFragment =  ModeTargetTemperatureFragment.newInstance(tag);
	    	} else if (TIMER_FRAGMENT.equals(tag)) 
			{
				fragment = mTimerFragment = ModeTimerFragment.newInstance(tag);
			}
	    	mFragments.put(tag, fragment);
	    }
		mCurrentTag = tag;
		
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_mode_container, fragment);
		transaction.commit();
		refreshSettingData() ;
		if (MEATTYPE_FRAGMENT.equals(tag)) 
    	{
			meattype.setBackgroundResource(R.drawable.tab_selected_left);
			targettemperature.setBackgroundResource(R.drawable.tab_unselected_mid);
			timer.setBackgroundResource(R.drawable.tab_unselected_right);
    		
    	} else if (TEMPERATURE_FRAGMENT.equals(tag)) 
    	{
    		meattype.setBackgroundResource(R.drawable.tab_unselected_left);
			targettemperature.setBackgroundResource(R.drawable.tab_selected_mid);
			timer.setBackgroundResource(R.drawable.tab_unselected_right);
    	} else if (TIMER_FRAGMENT.equals(tag)) 
		{
    		meattype.setBackgroundResource(R.drawable.tab_unselected_left);
			targettemperature.setBackgroundResource(R.drawable.tab_unselected_mid);
			timer.setBackgroundResource(R.drawable.tab_selected_right);
		}
	}
	
	/*public void requestChannelSettingsTimer(Timer reloadTimer) {
		//mCurrentSet = command.requestCurrentChannelSettings(currentChannel);
		Log.d(TAG, "requestChannelSettingsTimer");
		mCurrentSet.setReloadTimer(reloadTimer);
	}
	
	public void requestChannelSettingMeatType(int meatTypeIndex, DonenessLevel level) {
		Log.d(TAG, "requestChannelSettingMeatType");
		mCurrentSet.setDonenessLevel(level);
		mCurrentSet.setMeatTypeInt(meatTypeIndex);
	}
	
	public void requestChannelSettingTarget(float temF) {
		Log.d(TAG, "requestChannelSettingTarget");
		mCurrentSet.setTargetTemperature(temF);
	}*/
	
	private void getCurrrentChannel(String tagMsg) {
		if (OperationActivity.CHANNELONE.equalsIgnoreCase(tagMsg)) {
			currentChannel = 1;
		} else if (OperationActivity.CHANNELTWO.equalsIgnoreCase(tagMsg)) {
			currentChannel = 2;
		}
	}
	
	private void refreshSettingData() {
		if (MEATTYPE_FRAGMENT.equals(mCurrentTag)) {
			mMeatTypeFragment.setData(mCurrentSet);
		} else if (TEMPERATURE_FRAGMENT.equals(mCurrentTag)) {
			mTargetTemperatureFragment.setData(mCurrentSet);
		} else if (TIMER_FRAGMENT.equals(mCurrentTag)) {
			mTimerFragment.setData(mCurrentSet);
		}
	}
	
	private Mode requestCurrentSettingMode() {
		if (MEATTYPE_FRAGMENT.equals(mCurrentTag)) {
			return Mode.MEAN_TYPE_MODE;
		} else if (TEMPERATURE_FRAGMENT.equals(mCurrentTag)) {
			return Mode.TARGET_TEMPERATURE_MODE;
		} else if (TIMER_FRAGMENT.equals(mCurrentTag)) {
			return Mode.TIMER_MODE;
		}
		return null;
	}
	
	
	private class MyChannelSettingsChangedCallback implements ChannelSettingsChangedCallback {

		@Override
		public void onChannelSettingModeChanged(int ch, Mode preMode,
				Mode curMode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onChannelSettingMeattypeChanged(int ch, int preTypeIndex,
				int curTypeIndex) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onChannelSettingDonenessChanged(int ch,
				DonenessLevel preLevel, DonenessLevel curLevel) {
			
			
		}

		@Override
		public void onChannelSettingTargetChanged(int ch, float preTarget,
				float curTarget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onChannelSettingTimerChanged(int ch, Timer preMode,
				Timer curMode) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
