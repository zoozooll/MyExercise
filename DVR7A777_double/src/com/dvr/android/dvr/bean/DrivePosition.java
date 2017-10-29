/**
 * 
 */
package com.dvr.android.dvr.bean;

import java.io.Serializable;

/**
 * 经纬度数据封�?BR>
 * 经度 东经 �?西经 �?纬度 北纬 �?南纬 �? * 
 * @author sunshine
 * @version [yecon Android_Platform, 4 Feb 2012]
 */
public class DrivePosition implements Serializable , Cloneable
{
    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 纬度
     */
    public double mLatitude;

    /**
     * 经度
     */
    public double mLongitude;

    /**
     * 当前时间
     */
    public long mTime;

    /**
     * 当前速度
     */
    public float mSpeed;

    /**
     * 数据是否有效 false 无效 true有效
     */
    public boolean mValid = false;

    /**
     * 从录制开始到现在的偏移秒�?     */
    public long mDelta;

    /**
     * 默认构�?函数
     */
    public DrivePosition()
    {
        mValid = false;
    }

    /**
     * 构�?函数
     * 
     * @param latitude
     * @param longitude
     * @param time
     * @param speed
     */
    public DrivePosition(double latitude, double longitude, long time, float speed, boolean valid, long delta)
    {
        mLatitude = latitude;
        mLongitude = longitude;
        mTime = time;
        mSpeed = speed;
        mValid = valid;
        mDelta = delta;
    }

    @Override
    public String toString()
    {
        return "[mLatitude =" + mLatitude + "  mLongitude=" + mLongitude + " mTime=" + mTime + " mSpeed="
            + mSpeed + " mValid=" + mValid + " mDelta=" + mDelta + "]";
    }

    @Override
    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 填充数据
     * 
     * @param latitude
     * @param longitude
     * @param time
     * @param speed
     */
    // public void fill(float latitude, float longitude, long time, float speed, boolean valid)
    // {
    // mLatitude = latitude;
    // mLongitude = longitude;
    // mTime = time;
    // mSpeed = speed;
    // mValid = valid;
    // }

}
