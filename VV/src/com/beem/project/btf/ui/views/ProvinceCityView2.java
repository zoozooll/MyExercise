package com.beem.project.btf.ui.views;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.wheelviewWidget.OnWheelChangedListener;
import com.beem.project.btf.ui.views.wheelviewWidget.WheelView;
import com.beem.project.btf.ui.views.wheelviewWidget.adapters.ArrayWheelAdapter;
import com.butterfly.vv.db.SQLdm;

public class ProvinceCityView2 {
	private static final String TAG = "ProvinceCityView2";
	private View mView;
	private Context mContext;
	private WheelView mProvinceView, mCityView;
	private String[] mProvinceDatas;
	private String[] mProvinceCodes;
	private String[] mCitiesDatas;
	private String[] mCitiesCodes;
	private SQLiteDatabase db;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mCitisCodesMap = new HashMap<String, String[]>();
	private String mCurrentProviceName;
	private String mCurrentProviceCode;
	private String mCurrentCityName;
	private String mCurrentCityCode;
	private Cursor cursor;

	public ProvinceCityView2(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.setting_cities, null);
		queryAllProvinces();
		mProvinceView = (WheelView) mView.findViewById(R.id.mProvince);
		mCityView = (WheelView) mView.findViewById(R.id.mCity);
		mProvinceView.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				mProvinceDatas));
		mProvinceView.addChangingListener(new MyWheelChangedListener());
		mCityView.addChangingListener(new MyWheelChangedListener());
		mProvinceView.setVisibleItems(9);
		mCityView.setVisibleItems(9);
		updateCitieCodes();
	}
	// 返回布局对象
	public View getmView() {
		return mView;
	}
	private void initDbData() {
		db = SQLdm.openDatabase(mContext);
	}
	// 查找所有省份封装成字符串数组,封装省-市键值对
	private void queryAllProvinces() {
		initDbData();
		cursor = db.rawQuery("select pcode,pname from provice", null);
		mProvinceDatas = new String[cursor.getCount()];
		mProvinceCodes = new String[cursor.getCount()];
		// cursor下标从-1开始
		for (int i = 0; i < cursor.getCount(); i++) {
			if (cursor.moveToNext()) {
				mProvinceDatas[i] = cursor.getString(cursor
						.getColumnIndex("pname"));// 记录省份名称
				mProvinceCodes[i] = cursor.getString(cursor
						.getColumnIndex("pcode"));// 记录省份id
				Cursor cursor2 = db.rawQuery(
						"select * from city where cpcode like "
								+ mProvinceCodes[i], null);
				String[] mCitiesDatas = new String[cursor2.getCount()];
				String[] mCitiesCodes = new String[cursor2.getCount()];
				for (int j = 0; j < cursor2.getCount(); j++) {
					if (cursor2.moveToNext()) {
						mCitiesDatas[j] = cursor2.getString(cursor2
								.getColumnIndex("cname"));// 记录市的名称
						mCitiesCodes[j] = cursor2.getString(cursor2
								.getColumnIndex("ccode"));// 记录市的id
					}
				}
				mCitisDatasMap.put(mProvinceDatas[i], mCitiesDatas);
				mCitisCodesMap.put(mProvinceCodes[i], mCitiesCodes);
				cursor2.close();
			}
		}
		cursor.close();
		db.close();
	}

	class MyWheelChangedListener implements OnWheelChangedListener {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			// TODO Auto-generated method stub
			if (wheel == mProvinceView) {
				// updateCities();
				updateCitieCodes();
			} else if (wheel == mCityView) {
				mCurrentCityCode = mCitiesCodes[mCityView.getCurrentItem()];
				mCurrentCityName = mCitiesDatas[mCityView.getCurrentItem()];
			}
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mProvinceView.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		mCitiesDatas = mCitisDatasMap.get(mCurrentProviceName);
		if (mCitiesDatas == null) {
			mCitiesDatas = new String[] { "" };
		}
		mCityView.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				mCitiesDatas));
		mCityView.setCurrentItem(0);
		mCurrentCityName = mCitiesDatas[mCityView.getCurrentItem()];
	}
	public String[] getCurrentDatas() {
		String[] data = { mCurrentProviceName, mCurrentCityName };
		return data;
	}
	private void updateCitieCodes() {
		int pCurrent = mProvinceView.getCurrentItem();
		mCurrentProviceCode = mProvinceCodes[pCurrent];
		mCitiesCodes = mCitisCodesMap.get(mCurrentProviceCode);
		/*
		 * if (mCitiesCodes == null) { mCitiesCodes = new String[] { "" }; }
		 */
		mCurrentCityCode = mCitiesCodes[0];
		mCurrentProviceName = mProvinceDatas[pCurrent];
		mCitiesDatas = mCitisDatasMap.get(mCurrentProviceName);
		if (mCitiesDatas == null) {
			mCitiesDatas = new String[] { "" };
		}
		mCityView.setViewAdapter(new ArrayWheelAdapter<String>(mContext,
				mCitiesDatas));
		mCityView.setCurrentItem(0);
		mCurrentCityName = mCitiesDatas[mCityView.getCurrentItem()];
	}
	public String[] getCurrentCodesAndName() {
		String CurrentName = "";
		if (mCurrentProviceName.equals(mCurrentCityName)) {
			CurrentName = mCurrentProviceName;
		} else {
			CurrentName = mCurrentProviceName + "-" + mCurrentCityName;
		}
		String[] codes = { CurrentName, mCurrentCityCode };
		return codes;
	}
	// 从外部设置滚轮渐变色
	public void setShadowColor(int start, int middle, int end) {
		mProvinceView.setShadowColor(start, middle, end);
		mCityView.setShadowColor(start, middle, end);
	}
}
