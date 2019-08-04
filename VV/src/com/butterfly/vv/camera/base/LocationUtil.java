package com.butterfly.vv.camera.base;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationUtil {
	private static final String TAG = "LocationUtil";
	private Context mContext;

	public LocationUtil(Context context) {
		mContext = context;
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) mContext
				.getSystemService(serviceName);
		/*
		 * String provider = LocationManager.GPS_PROVIDER; Criteria criteria = new Criteria();
		 * criteria.setAccuracy(Criteria.ACCURACY_FINE); criteria.setAltitudeRequired(false);
		 * criteria.setBearingRequired(false); criteria.setCostAllowed(true);
		 * criteria.setPowerRequirement(Criteria.POWER_LOW); String provider =
		 * locationManager.getBestProvider(criteria, true);
		 */
		String provider = LocationManager.NETWORK_PROVIDER;
		Location location = locationManager.getLastKnownLocation(provider);
		updateWithNewLocation(location);
		locationManager.requestLocationUpdates(provider, 2000, 10,
				locationListener);
	}

	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}
		@Override
		public void onProviderDisabled(String provider) {
			updateWithNewLocation(null);
		}
		@Override
		public void onProviderEnabled(String provider) {
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	private void updateWithNewLocation(Location location) {
		String latLongString;
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "纬度:" + lat + "\n经度:" + lng;
		} else {
			latLongString = "无法获取地理信息";
		}
		List<Address> addList = null;
		Geocoder ge = new Geocoder(mContext);
		try {
			addList = ge.getFromLocation(24.463, 118.1, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (addList != null && addList.size() > 0) {
			for (int i = 0; i < addList.size(); i++) {
				Address ad = addList.get(i);
				latLongString += "\n";
				latLongString += ad.getCountryName() + ";" + ad.getLocality();
			}
		}
	}
	public String getLocationCityByLatLon(double lat, double lon) {
		String latLongString = null;
		List<Address> addList = null;
		Geocoder ge = new Geocoder(mContext);
		try {
			addList = ge.getFromLocation(lat, lon, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (addList != null && addList.size() > 0) {
			for (int i = 0; i < addList.size(); i++) {
				Address ad = addList.get(i);
				latLongString += "\n";
				latLongString += ad.getCountryName() + ";" + ad.getLocality();
			}
		}
		return latLongString;
	}
	public String getLocationCityByNormalLatLon(String strLat, String StrLon) {
		String latLongString = "";
		double lat = 0;
		double lon = 0;
		lat = getLatLonByString(strLat);
		lon = getLatLonByString(StrLon);
		List<Address> addList = null;
		Geocoder ge = new Geocoder(mContext);
		try {
			addList = ge.getFromLocation(lat, lon, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (addList != null && addList.size() > 0) {
			for (int i = 0; i < addList.size(); i++) {
				Address ad = addList.get(i);
				// latLongString += ad.getCountryName() + " " +
				// ad.getLocality();
				latLongString += ad.getLocality();
			}
		}
		if (latLongString.length() < 2) {
			latLongString = lon + ", " + lat;
		}
		return latLongString;
	}
	public double getLatLonByString(String strIn) {
		double latLon = 0;
		String[] splitTemp;
		double degree = 0;
		double min = 0;
		double second = 0;
		if (strIn != null) {
			splitTemp = strIn.split(",");
			for (int i = 0; i < splitTemp.length; i++) {
				String[] splitTemp2 = splitTemp[i].split("/");
				double d1 = Double.valueOf(splitTemp2[0]);
				double d2 = Double.valueOf(splitTemp2[1]);
				if (i == 0) {
					degree = d1 / d2;
				} else if (i == 1) {
					min = d1 / d2;
				} else if (i == 2) {
					second = d1 / d2;
				}
			}
		}
		latLon = degree + min / 60 + second / 3600;
		Log.d(TAG, "getLatLonByString, latLon = " + latLon);
		return latLon;
	}
}
