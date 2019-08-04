/**
 * 
 */
package com.oregonscientific.bbq.act;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.BbqSettings;
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.utils.BbqConfig;


public class ModeTargetTemperatureFragment extends Fragment implements OnValueChangeListener,OnScrollListener{
	
	private View view = null;
	private NumberPicker num_picker_hundred,num_picker_ten,num_picker_one;
	private float targetTem;
	private ModeActivity mActivity;
	private TextView wheeltempf;
	
	//SharedPreferences tempcf;
	private String ischeckf;
	
	public static ModeTargetTemperatureFragment newInstance(String tag) {
		ModeTargetTemperatureFragment fragment = new ModeTargetTemperatureFragment();
		Bundle args = new Bundle();
        args.putString(ModeActivity.TEMPERATURE_FRAGMENT, tag);
        fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (ModeActivity) getActivity();
		SharingPreferenceDao dao = SharingPreferenceDao.getInstance(mActivity);
		ischeckf = dao.getShowingTemperatureUnit();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.modetargettempereature, null);
		init(view);
		return view;
	}
	
	@Override
	public void onStop() {
		super.onStop();
//		mActivity.requestChannelSettingTarget(getWeelTemperatureF());
	}
	
	public void setData(BbqSettings set) {
		targetTem = set.getTargetTemperature();
	}
	
	public float getData() {
		if(BbqConfig.TEMPERATURE_UNIT_F.equals(ischeckf)){
			return getNumPickerTemperatureF();
		}else if (BbqConfig.TEMPERATURE_UNIT_C.equals(ischeckf)) {
			return  ParseManager.tranCelsiusToFahrenheit(getNumPickerTemperatureF());
		}
		return 0;
	}
	
	public void init(View v){
		wheeltempf = (TextView) v.findViewById(R.id.wheeltempf);
		num_picker_hundred = (NumberPicker) v.findViewById(R.id.num_picker_hundred);
		num_picker_ten = (NumberPicker) v.findViewById(R.id.num_picker_ten);
		num_picker_one = (NumberPicker) v.findViewById(R.id.num_picker_one);
		
		num_picker_hundred.setOnValueChangedListener(this);
		num_picker_hundred.setOnScrollListener(this);
		num_picker_hundred.setMaxValue(3);
		num_picker_hundred.setMinValue(0);
		
		num_picker_ten.setOnValueChangedListener(this);
		num_picker_ten.setOnScrollListener(this);
		num_picker_ten.setMaxValue(9);
		num_picker_ten.setMinValue(0);
		
		num_picker_one.setOnValueChangedListener(this);
		num_picker_one.setOnScrollListener(this);
		num_picker_one.setMaxValue(9);
		num_picker_one.setMinValue(0);
		
		
		/*tempcf = getActivity().getSharedPreferences("ischeckf",0);
		ischeckf = tempcf.getBoolean("ischeckf", true);*/
		if(BbqConfig.TEMPERATURE_UNIT_F.equals(ischeckf)){
			wheeltempf.setText(R.string.tempf);
			num_picker_hundred.setValue((int)targetTem/100);
			num_picker_ten.setValue(((int)targetTem%100)/10);
			num_picker_one.setValue((int)targetTem % 10);
		}else if (BbqConfig.TEMPERATURE_UNIT_C.equals(ischeckf)) {
			int toc= (int)ParseManager.tranFahrenheitToCelsius(targetTem);
			int hun =toc/100;
			int ten =(toc-100)/10;
			int one =(toc-100-ten*10);
			num_picker_hundred.setValue(hun-1);
			num_picker_ten.setValue(ten-1);
			num_picker_one.setValue(one-1);
			wheeltempf.setText(R.string.tempc);
		}
	}
	
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        //Log.e("cdf", "oldValue:" + oldVal + "   ; newValue: " + newVal);
    }

    /*public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }*/

    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (scrollState) {
        case OnScrollListener.SCROLL_STATE_FLING:
            break;
        case OnScrollListener.SCROLL_STATE_IDLE:
            break;
        case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
            break;
        }
    }
	
	private int getNumPickerTemperatureF() {
		return num_picker_hundred.getValue() * 100 + num_picker_ten.getValue() * 10 + num_picker_one.getValue();
	}
}
