/**
 * 
 */
package com.oregonscientific.bbq.act;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.bean.Timer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.TextView;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;

public class ModeTimerFragment extends Fragment implements OnClickListener,OnValueChangeListener,OnScrollListener,Formatter {
	private View view = null;
	//private WheelView wheelTimerHour,wheelTimerMin,wheelTimerSec;
	private NumberPicker num_picker_hour,num_picker_min,num_picker_sec;
	private Timer mReloadTimer;
	private ModeActivity mActivity;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (ModeActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.modetimer, null);
		init(view);
		return view;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		super.onStop();
		//mActivity.requestChannelSettingsTimer(getWheelTimer(mReloadTimer));
	}
	
	public static ModeTimerFragment newInstance(String tag) {
		// TODO Auto-generated method stub
		ModeTimerFragment fragment = new ModeTimerFragment();
		Bundle args = new Bundle();
        args.putString(ModeActivity.TIMER_FRAGMENT, tag);
        fragment.setArguments(args);
		return fragment;
	}
	
	
	public void init(View v){
		num_picker_hour= (NumberPicker) v.findViewById(R.id.num_picker_hour);
		num_picker_min= (NumberPicker) v.findViewById(R.id.num_picker_min);
		num_picker_sec= (NumberPicker) v.findViewById(R.id.num_picker_sec);
		
		num_picker_hour.setFormatter(this);
		num_picker_hour.setOnValueChangedListener(this);
		num_picker_hour.setOnScrollListener(this);
		num_picker_hour.setMaxValue(23);
		num_picker_hour.setMinValue(0);
		
		num_picker_min.setFormatter(this);
		num_picker_min.setOnValueChangedListener(this);
		num_picker_min.setOnScrollListener(this);
		num_picker_min.setMaxValue(59);
		num_picker_min.setMinValue(0);
		
		num_picker_sec.setFormatter(this);
		num_picker_sec.setOnValueChangedListener(this);
		num_picker_sec.setOnScrollListener(this);
		num_picker_sec.setMaxValue(59);
		num_picker_sec.setMinValue(0);
		
		setWheelTimer(mReloadTimer);
	}
	
	public void setData(BbqSettings set) {
		mReloadTimer = set.getReloadTimer();
	}
	
	public Timer getData() {
		return getWheelTimer(mReloadTimer);
	}
	
	private void setWheelTimer(Timer timer) {
		num_picker_hour.setValue(timer.getHours());
		num_picker_min.setValue(timer.getMinute());
		num_picker_sec.setValue(timer.getSecond());
	}
	
	private Timer getWheelTimer(Timer in) {
		if (in == null) {
			in = new Timer();
		}
		in.setHours(num_picker_hour.getValue());
		in.setMinute(num_picker_min.getValue());
		in.setSecond(num_picker_sec.getValue());
		return in;
	}

	public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onValueChange(NumberPicker arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}

