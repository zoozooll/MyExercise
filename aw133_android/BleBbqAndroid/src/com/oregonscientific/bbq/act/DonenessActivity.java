/**
 * 
 */
package com.oregonscientific.bbq.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.bean.DonenessTemperature;
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.utils.BbqConfig;
import com.oregonscientific.bbq.view.DonenessRotary;
import com.oregonscientific.bbq.view.DonenessRotary.OnDonenessChangedListener;

public class DonenessActivity extends Activity implements OnClickListener {

	private static final String TAG = "DonenessActivity";
	private String[] meatTypeStrs;
	private String[] temparray;
	private SparseIntArray colorarray;
	private LinearLayout tempchoose;
	private ImageView donenessbackiv;
	private TextView donenessback,tvwDonenessTitle;
	private LayoutInflater inflater;
	private TextView donedefault, donecustomize;
	private TextView tempaddpluseunit,temperaturef;
	private DonenessRotary chooseDoneness;
	// private int mMeattypeIndex;
	private DonenessTemperature donenessTemperature;
	private boolean customast;
	private SparseArray<Float> mDonenessLevelData;
	private SparseArray<TextView> mDonenessLevelViews;
	private boolean modified;
	//private SharingPreferenceDao dao;
	private String temperatureUnit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.doneness);
		super.onCreate(savedInstanceState);

		init();
	}

	private void init() {
		temperatureUnit = SharingPreferenceDao.getInstance(this).getShowingTemperatureUnit();
		
		donenessTemperature = (DonenessTemperature) getIntent()
				.getSerializableExtra(SettingsActivity.KEY_MEATTYPE);
		int mMeattypeIndex = donenessTemperature.getMeatTypeIndex();
		
		temperaturef = (TextView) findViewById(R.id.temperaturef);
		customast = donenessTemperature.isCustom();
		tempchoose = (LinearLayout) findViewById(R.id.tempchoose);
		donenessbackiv = (ImageView) findViewById(R.id.donenessbackiv);
		donenessback = (TextView) findViewById(R.id.donenessback);
		donedefault = (TextView) findViewById(R.id.donedefault);
		donecustomize = (TextView) findViewById(R.id.donecustomize);
		tvwDonenessTitle = (TextView) findViewById(R.id.tvwDonenessTitle);
		tvwDonenessTitle = (TextView) findViewById(R.id.tvwDonenessTitle);
		chooseDoneness = (DonenessRotary) findViewById(R.id.chooseDoneness);
		//tempaddpluseunit = (TextView) findViewById(R.id.tempaddpluseunit);
		chooseDoneness.setmListener(new MyOnDonenessChangedListener());
		Resources res = getResources();
		meatTypeStrs = res.getStringArray(R.array.meatname);
		temparray = res.getStringArray(R.array.donenessLevelStrs);
		tvwDonenessTitle.setText(meatTypeStrs[mMeattypeIndex]);
		colorarray = new SparseIntArray();
		colorarray.put(5, R.drawable.box_welldone);
		colorarray.put(4, R.drawable.box_mediumwell);
		colorarray.put(3, R.drawable.box_medium);
		colorarray.put(2, R.drawable.box_mediumrare);
		colorarray.put(1, R.drawable.box_rare);
		mDonenessLevelViews = new SparseArray<TextView>();
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
		 * tempAdapter = new TemperatureChooseAdapter();
		 * tempchoose.setAdapter(tempAdapter);
		 * setListViewHeightBasedOnChildren(tempchoose);
		 */
		donenessbackiv.setOnClickListener(this);
		donenessback.setOnClickListener(this);

		donedefault.setOnClickListener(this);
		donecustomize.setOnClickListener(this);
		if (customast) {
			showingCustom();
		} else {
			showingDefault();
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		setupDonenessTemperature();
		super.finish();
	}

	/**
	 * 
	 */
	private void setupDonenessTemperature() {
		if (modified) {
			Intent data = new Intent();
			data.putExtra(SettingsActivity.KEY_CUSTOM, customast);
			if (customast) {
				data.putExtra(SettingsActivity.KEY_MEATTYPE, donenessTemperature);
			} 
			setResult(SettingsActivity.RESULT_SETUP_DONENESS, data);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.donenessbackiv:
			finish();
			break;
		case R.id.donenessback:
			finish();
			break;
		case R.id.donedefault:
			modified = true;
			showingDefault();
			break;
		case R.id.donecustomize:
			modified = true;
			showingCustom();
			break;
		case R.id.templ:
			if (customast) {
				modified = true;
				donenessTemperature.temperatureAdd(-1f);
				refreshCustomTemperatures(donenessTemperature);
			}
			break;
		case R.id.tempr:
			if (customast) {
				modified = true;
				donenessTemperature.temperatureAdd(1f);
				refreshCustomTemperatures(donenessTemperature);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 */
	private void showingCustom() {
		customast = true;
		donecustomize.setBackgroundResource(R.drawable.tab_selected_right);
		donedefault.setBackgroundResource(R.drawable.tab_unselected_left);
		chooseDoneness.setTouchable(true);
		initDonenessView();
	}

	/**
	 * 
	 */
	private void showingDefault() {
		customast = false;
		donecustomize.setBackgroundResource(R.drawable.tab_unselected_right);
		donedefault.setBackgroundResource(R.drawable.tab_selected_left);
		chooseDoneness.setTouchable(false);
		initDonenessView();
	}

/*	private SparseArray<Float> changeMeatDonenessData() {
		if (!donenessTemperature.isCustom()) {
			return BbqConfig.MAP_MEATTYPE_INDEX.get(donenessTemperature
					.getMeatTypeIndex());
		} else {
			SparseArray<Float> saf = new SparseArray<Float>();
			float f = donenessTemperature.getRareTemperature();
			if (f > 0) {
				saf.put(1, f);
			}
			f = donenessTemperature.getMediumrareTemperature();
			if (f > 0) {
				saf.put(2, f);
			}
			f = donenessTemperature.getMediumTemperature();
			if (f > 0) {
				saf.put(3, f);
			}
			f = donenessTemperature.getMediumwellTemperature();
			if (f > 0) {
				saf.put(4, f);
			}
			f = donenessTemperature.getWelldoneTemperature();
			if (f > 0) {
				saf.put(5, f);
			}
			return saf;
		}
	}*/

	private void initDonenessView() {
		float tem;
		if (customast) {
			if (!donenessTemperature.isCustom()) {

				try {
					donenessTemperature = BbqConfig.MAP_MEATTYPE_INITIAL_INDEX
							.get(donenessTemperature.getMeatTypeIndex()).copy();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				donenessTemperature = BbqConfig.MAP_MEATTYPE_INDEX.get(
						donenessTemperature.getMeatTypeIndex()).copy();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		chooseDoneness.initAngles(donenessTemperature);
		boolean isCuston = donenessTemperature.isCustom();
		tempchoose.removeAllViews();
		mDonenessLevelViews.clear();
		tem = donenessTemperature.getWelldoneTemperature();
		/*if(BbqConfig.TEMPERATURE_UNIT_C.equals(temperatureUnit)) {
			tem = ParseManager.tranFahrenheitToCelsius(tem);
		}*/
		if (tem > 0 ) {
			addDonenessShowing(5, tem);
		}
		tem = donenessTemperature.getMediumwellTemperature();
		if (tem > 0 ) {
			addDonenessShowing(4, tem);
		}
		tem = donenessTemperature.getMediumTemperature();
		if (tem > 0 ) {
			addDonenessShowing(3, tem);
		}
		tem = donenessTemperature.getMediumrareTemperature();
		if (tem > 0 ) {
			addDonenessShowing(2, tem);
		}
		tem = donenessTemperature.getRareTemperature();
		if (tem > 0 ) {
			addDonenessShowing(1, tem);
		}
		
	}
	
	private void addDonenessShowing(int index, float tem) {
		String result = null;
		View v = inflater.inflate(R.layout.tempchoose, null);
		TextView tempname = (TextView) v.findViewById(R.id.tempname);
		ImageView templ = (ImageView) v.findViewById(R.id.templ);
		TextView tempaddpluse = (TextView) v
				.findViewById(R.id.tempaddpluse);
		TextView tempaddpluseunit = (TextView) v.findViewById(R.id.tempaddpluseunit);
		ImageView tempr = (ImageView) v.findViewById(R.id.tempr);
		tempname.setText(temparray[index]);
		v.setBackgroundResource(colorarray.get(index));
		if (BbqConfig.TEMPERATURE_UNIT_F.equals(temperatureUnit)) {
			tempaddpluseunit.setText(R.string.tempf);
			temperaturef.setText(R.string.tempf);
			result = String.format("%.0f", tem);
		} else if (BbqConfig.TEMPERATURE_UNIT_C.equals(temperatureUnit)) {
			tempaddpluseunit.setText(R.string.tempc);
			temperaturef.setText(R.string.tempc);
			tem = ParseManager.tranFahrenheitToCelsius(tem);
			result = String.format("%.0f", tem);
		}
		tempaddpluse.setText(result);
		
		if (customast) {
			templ.setVisibility(View.VISIBLE);
			tempr.setVisibility(View.VISIBLE);
		} else {
			templ.setVisibility(View.INVISIBLE);
			tempr.setVisibility(View.INVISIBLE);
		}
		templ.setOnClickListener(this);
		tempr.setOnClickListener(this);
		mDonenessLevelViews.put(index, tempaddpluse);
		tempchoose.addView(v);
	}

	private void refreshCustomTemperatures(DonenessTemperature dt) {
		
		chooseDoneness.refreshAngles(donenessTemperature);
		
		for (int i = 0; i < mDonenessLevelViews.size(); i++) {
			int level = mDonenessLevelViews.keyAt(i);
			TextView tvw = mDonenessLevelViews.get(level);
			float tem = 0;
			switch (level) {
			case 1:
				tem = dt.getRareTemperature();
				break;
			case 2:
				tem = dt.getMediumrareTemperature();
				break;
			case 3:
				tem = dt.getMediumTemperature();
				break;
			case 4:
				tem = dt.getMediumwellTemperature();
				break;
			case 5:
				tem = dt.getWelldoneTemperature();
				break;
			default:
				break;
			}
			
			if (BbqConfig.TEMPERATURE_UNIT_C.equals(temperatureUnit)) {
				tem = ParseManager.tranFahrenheitToCelsius(tem);
			}
			String resultwo = String.format("%.0f", tem);
			tvw.setText(resultwo);
		}
	}
	
	private class MyOnDonenessChangedListener implements OnDonenessChangedListener {

		@Override
		public void onDonenessTemperatureChanged(float firstTemF) {
			//Log.d(TAG, "onDonenessTemperatureChanged "+ firstTemF);
			modified = true;
			donenessTemperature.setFirstTemperature(firstTemF);
			refreshCustomTemperatures(donenessTemperature);
			
		}
		
	}
}
