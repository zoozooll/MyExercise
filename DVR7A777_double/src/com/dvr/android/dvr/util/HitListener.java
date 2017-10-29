/**
 * 
 */
package com.dvr.android.dvr.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.dvr.android.dvr.msetting.OnSettingChangeListener;
import com.dvr.android.dvr.msetting.SettingBean;

/**
 * 碰撞监听�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 14 Mar 2012]
 */
public class HitListener implements OnSettingChangeListener , SensorEventListener
{
    private static HitListener mInstance = null;

    private OnHitListener mListener = null;

    private int level = 6;

    private SensorManager sensorMgr;

    private static Context mContext = null;

    private float[] values = new float[3];

    private boolean isFirst = true;

    /**
     * �?��标准单位
     */
    private float unitValue = 9.8f / 25;

    /**
     * �?��单位
     */
    private float minValue = 9.8f *28/10;

    private float threshold = 0;

    /**
     * 私有构�?，单�?     */
    private HitListener()
    {
        SettingBean.setSettingListener(this);
        sensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        SharedPreferences share = mContext.getSharedPreferences("xgx", Context.MODE_PRIVATE);
        SettingBean.mHitDelicacy = share.getInt("mHitDelicacy", SettingBean.MHitDelicacy);
        setLevelAndThreshold();
        // 注册listener，第三个参数是检测的精确�?        
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

    }

    /**
     * 获取这个实例
     * 
     * @return
     */
    public static HitListener getInstance(Context context)
    {
        mContext = context;
        if (null == mInstance)
        {
            mInstance = new HitListener();
        }
        return mInstance;
    }

    public void setListener(OnHitListener listener)
    {
        mListener = listener;
    }

    private void setLevelAndThreshold() {
        level = SettingBean.mHitDelicacy;
        // threshold = level * minValue;//threshold = (30 - level) * unitValue +
        // minValue;
        if (level == 5) {
            threshold = (float) 25.0;
        } else if (level == 2) {
            threshold = (float) 50.0;
        } else if (level == 0) {
            threshold = (float) 75.0;
        } //Log.i("PLJ", "HitListener---->setLevelAndThreshold:" + SettingBean.mHitDelicacy+"   "+threshold);
    }
    
    public void onSettingChange(int type)
    {
    	setLevelAndThreshold();
    }

    public void onSensorChanged(SensorEvent event)
    {
        float x = event.values[SensorManager.DATA_X];
        float y = event.values[SensorManager.DATA_Y];
        float z = event.values[SensorManager.DATA_Z];
        
        // 撞击不保�?        
        if (!SettingBean.isHitAutosave)
        {
            values[0] = x;
            values[1] = y;
            values[2] = z;
            isFirst = false;
        }
        else
        {
            if (isFirst)
            {
                values[0] = x;
                values[1] = y;
                values[2] = z;
                isFirst = false;
            }
            else
            {   //Log.i("PLJ", "HitListener---->onSensorChanged111:" +Math.abs(x - values[0])+ "   "+Math.abs(y - values[1])+"   "+Math.abs(z - values[2])+"   "+threshold);
                if (Math.abs(x - values[0]) > threshold || Math.abs(y - values[1]) > threshold
                    || Math.abs(z - values[2]) > threshold)
                {
                    if (null != mListener)
                    {
                        mListener.OnHit();
                        //Log.i("PLJ", "HitListener---->onSensorChanged222:" + x + " y = " + y + " z = " + z);
                    }
                }

                values[0] = x;
                values[1] = y;
                values[2] = z;
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }
}
