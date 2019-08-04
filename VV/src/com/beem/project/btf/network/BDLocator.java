package com.beem.project.btf.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.beem.project.btf.utils.ThreadUtils.AutoTask;
import com.butterfly.vv.service.ContactService;

/**
 * @ClassName: BaiduLocator
 * @Description: 百度定位
 * @author: yuedong bao
 * @date: 2015-5-4 下午5:27:02
 */
public class BDLocator {
	private static final int LOCATION_VALID_TIME = 30 * 60 * 1000;
	private LocationClient mLocationClient = null;
	private static BDLocator instance = null;
	public MyLocationListener mMyLocationListener = new MyLocationListener();
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private LocationClientOption option;
	private String tempcoor = "gcj02";
	private int span = 1 * 1000;
	private final int loc_timeout = 2 * 1000;
	private boolean isNeedAddress = true;
	private final String location_key = "BaiduLocator_loc";
	private LocationInfo mLocationInfo;
	private SharedPreferences mSetting;
	private GeoCoder geocoder;
	private long lastLocateTime;

	private BDLocator() {
		// 将百度定位放到线程中执行
		option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(span);// 设置发起定位请求的间隔时间
		option.setIsNeedAddress(isNeedAddress);
		option.setTimeOut(loc_timeout);
		geocoder = BeemApplication.geoCoder;
		// SP信息
		mSetting = PreferenceManager
				.getDefaultSharedPreferences(BeemApplication.getContext());
		mSetting.registerOnSharedPreferenceChangeListener(locLis);
		mLocationInfo = new LocationInfo();
		String str = mSetting.getString(location_key, "");
		fillUpLocationInfo(mLocationInfo, str);
	}

	// 定位信息
	public class LocationInfo {
		public double lat;
		public double lon;
		public String addr;
		public String province;
		public String city;
		public String district;
		public String street;

		@Override
		public String toString() {
			return "LocationInfo [lat=" + lat + ", lon=" + lon + ", addr="
					+ addr + ", province=" + province + ", city=" + city
					+ ", district=" + district + ", street=" + street + "]";
		}
	}

	public static BDLocator getInstance() {
		if (instance == null) {
			synchronized (BDLocator.class) {
				if (instance == null) {
					instance = new BDLocator();
				}
			}
		}
		return instance;
	}
	/**
	 * @Title: requestLatlon
	 * @Description: 请求获取经纬度,因为请求是异步的，很多情况下结果返回时间为5秒以上，因此本次会用上次请求的结果
	 * @param lis
	 * @return: void
	 */
	public void requestLatlon() {
		long now = SystemClock.elapsedRealtime();
		if (now - lastLocateTime < LOCATION_VALID_TIME) {
			return;
		}
		mLocationClient = new LocationClient(BeemApplication.getContext());
		mLocationClient.setLocOption(option);
//		LogUtils.i("start the baidulocator...");
		mLocationClient.start();
		/*
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation( )后，每隔设定的时间，定位SDK就会进行一次定位。
		 * 如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，返回上一次定位的结果 ；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
		 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 */
		mLocationClient.requestLocation();
		mLocationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				LogUtils.d("onReceiveLocation " + location.getLatitude() + ","
						+ location.getLongitude());
				// 此回调运行在Ui线程
				// 将数据写到sp中
				Map<String, Object> map = new HashMap<String, Object>();
				if (location.getLatitude() < 1 || location.getLongitude() < 1) {
					mLocationInfo.lat = 22.726886;
					mLocationInfo.lon = 114.392081;
					map.put("lat", mLocationInfo.lat);
					map.put("lon", mLocationInfo.lon);
				} else {
					lastLocateTime = SystemClock.elapsedRealtime();
					mLocationInfo.lat = location.getLatitude();
					mLocationInfo.lon = location.getLongitude();
					map.put("lat", mLocationInfo.lat);
					map.put("lon", mLocationInfo.lon);
				}
				mLocationInfo.addr = !TextUtils.isEmpty(location.getAddrStr()) ? location
						.getAddrStr() : "未知";
				mLocationInfo.street = !TextUtils.isEmpty(location.getStreet()) ? location
						.getStreet() : "未知";
				mLocationInfo.city = !TextUtils.isEmpty(location.getCity()) ? location
						.getCity() : "未知";
				mLocationInfo.province = !TextUtils.isEmpty(location
						.getProvince()) ? location.getProvince() : "未知";
				mLocationInfo.district = !TextUtils.isEmpty(location
						.getDistrict()) ? location.getDistrict() : "未知";
				map.put("addr", mLocationInfo.addr);
				map.put("province", mLocationInfo.province);
				map.put("city", mLocationInfo.city);
				map.put("district", mLocationInfo.district);
				map.put("street", mLocationInfo.street);
				String str = buildJson(map);
				Editor editor = mSetting.edit();
				editor.putString(location_key, str);
				editor.commit();
				if (LoginManager.getInstance().isLogined()) {
					ContactService.getInstance().synGeoInfo(mLocationInfo.lat,
							mLocationInfo.lon);
				}
				//				//LogUtils.i("onReceiveLocation_location:" + str);
				mLocationClient.unRegisterLocationListener(this);
				stop();
			}
		});
	}
	public double getLat() {
		return mLocationInfo.lat;
	}
	public double getLon() {
		return mLocationInfo.lon;
	}
	private void fillUpLocationInfo(LocationInfo location, String str) {
		try {
			LogUtils.i("location " + location.toString() + ", str " + str);
			JSONObject jsonObj = new JSONObject(str);
			location.lat = jsonObj.getDouble("lat");
			location.lon = jsonObj.getDouble("lon");
			location.addr = jsonObj.getString("addr");
			location.province = jsonObj.getString("province");
			location.city = jsonObj.getString("city");
			location.district = jsonObj.getString("district");
			location.street = jsonObj.getString("street");
		} catch (JSONException e) {
			e.printStackTrace();
			//			//LogUtils.e("parse location json failed:" + str);
			location.lat = 22.726886;
			location.lon = 114.392081;
			location.addr = "深圳坪山新区翠景路35号";
			location.province = "广东省";
			location.city = "深圳市";
			location.district = "坪山区";
			location.street = "翠景路";
		}
		//		//LogUtils.v("fillUpLocationInfo_str:" + location);
	}

	private OnSharedPreferenceChangeListener locLis = new OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (location_key.equals(key)) {
				String str = sharedPreferences.getString(key, "");
				fillUpLocationInfo(mLocationInfo, str);
			}
		}
	};

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append(" error code : ");
			sb.append(location.getLocType());
			sb.append(" latitude : ");
			sb.append(location.getLatitude());
			sb.append(" lontitude : ");
			sb.append(location.getLongitude());
			sb.append(" radius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append(" speed : ");
				sb.append(location.getSpeed());
				sb.append(" satellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append(" direction : ");
				sb.append(" addr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append(" addr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append(" operationers : ");
				sb.append(location.getOperators());
			}
			logMsg("onReceiveLocation:" + sb.toString());
			if (location.getLocType() == BDLocation.TypeNetWorkLocation
					|| location.getLocType() == BDLocation.TypeGpsLocation
					|| location.getLocType() == BDLocation.TypeCacheLocation
					|| location.getLocType() == BDLocation.TypeOffLineLocation
					|| location.getLocType() == BDLocation.TypeOffLineLocationNetworkFail) {
				// 将数据写到sp中
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("lat", location.getLatitude());
				map.put("lon", location.getLongitude());
				map.put("addr",
						!TextUtils.isEmpty(location.getAddrStr()) ? location
								.getAddrStr() : "未知");
				String str = buildJson(map);
				Editor editor = mSetting.edit();
				editor.putString(location_key, str);
				editor.commit();
			}
		}
	}

	private String buildJson(Map<String, Object> map) {
		JSONObject jsonObject = new JSONObject();
		for (String key : map.keySet()) {
			Object val = map.get(key);
			try {
				jsonObject.put(key, val);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject.toString();
	}
	private void logMsg(String string) {
		Log.i(getClass().getSimpleName(), "locate:" + string);
	}
	public void stop() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			//			//LogUtils.v("stop the baidulocator...success");
		} else {
			//			//LogUtils.v("stop the baidulocator...failed");
		}
	}
	/**
	 * @Title: reverseGeoCode
	 * @Description: 地理位置反编码,根据经纬度反击
	 * @param lat
	 * @param lon
	 * @param lis
	 * @return: void
	 */
	public void reverseGeoCode(final double lat, final double lon,
			final onReverseGeoCodeRstListener lis) {
		geocoder.setOnGetGeoCodeResultListener(new onReverseGeoCodeRstListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					// 没有找到检索结果
					//					if (result == null) {
					//						//LogUtils.e("Reverse:result is null~lat:" + lat + "lon:" + lon);
					//					} else {
					//						//LogUtils.e("Reverse:result is null~lat:" + lat + "lon:" + lon + " error:" + result.error);
					//					}
				} else {
					lis.onGetReverseGeoCodeResult(result);
				}
			}
		});
		geocoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(
				lat, lon)));
	}

	public abstract static class onReverseGeoCodeRstListener implements
			OnGetGeoCoderResultListener {
		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
		}
	}
}
