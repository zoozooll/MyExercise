/**
 * 
 */
package com.dvr.android.dvr.gps;

import com.dvr.android.dvr.bean.DrivePosition;

/**
 * GPS info changed call back listener<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 20 Feb 2012]
 */
public interface GPSInfoChangedListener
{
    /**
     * GPS数据通过这个接口返回，封装在DrivePosition�?由于GPS打开时间延迟，所以需要判�?mValid 如果是false说明GPS还没准备好，这个数据就只有时间�?
     * 
     * @param position
     */
    public void onGPSInfoChanged(DrivePosition position);
}
