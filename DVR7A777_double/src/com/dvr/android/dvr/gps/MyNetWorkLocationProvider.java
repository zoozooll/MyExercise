/**
 * 
 */
package com.dvr.android.dvr.gps;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * 我的网络位置提供器，主要获取�?��城市,辅助快�?定位，定位到后直接移�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 8 Mar 2012]
 */
public class MyNetWorkLocationProvider
{
    private LocationManager mLocationManager = null;

    private LocationListener mListener = null;

    public MyNetWorkLocationProvider(Context context)
    {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void getMyLocation(LocationListener listener)
    {
        mListener = listener;
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, listener);
    }

    public void close()
    {
        if (null != mLocationManager)
        {
            mLocationManager.removeUpdates(mListener);
            mLocationManager = null;
        }
    }
}
