/**
 * 
 */
package com.dvr.android.dvr.util;

import com.dvr.android.dvr.msetting.SettingBean;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 电源管理工具<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 24 May 2012]
 */
public class PowerUitl
{
    public static PowerUitl instance = null;

    private PowerHadler mPowerHandler = null;

    private LightListener mListener = null;

    private int mValue = -1;

    /**
     * 是否是在低背光状�?     */
    private boolean isLightDown = false;
    
    /**
    *如果不在主录制界面，就不进行屏幕的降�?    */
    private boolean isOut = true;

    private PowerUitl()
    {
        initHandler();
    }

    public static PowerUitl getPowerUitl()
    {
        if (null == instance)
        {
            instance = new PowerUitl();
            instance.setValue(SettingBean.powersaving_time);
        }

        return instance;
    }

    public void setListener(LightListener listener)
    {
        mListener = listener;
//        lightDownDelay();
    }

    /**
     * 设置背光延迟�?     */
    public void setValue(int value)
    {
        mValue = value;
        if (mValue > 0)
        {
            lightDownDelay();
        }
        else
        {
            cancel();
        }
    }

    /**
     * init handler
     */
    private void initHandler()
    {
//        HandlerThread updateThread = new HandlerThread("powersaving");
//        updateThread.start();
        mPowerHandler = new PowerHadler();
    }

    class PowerHadler extends Handler
    {
//        public PowerHadler(Looper looper)
//        {
//            super(looper);
//        }

        @Override
        public void handleMessage(Message msg)
        {
            lightDown();
        }
    }

    public void lightDownDelay()
    {
        mPowerHandler.removeMessages(0);
        if (mValue > 0 && !isOut)
        {
            Log.d("TAG", "send message delay " + mValue);
            mPowerHandler.sendEmptyMessageDelayed(0, mValue);
        }
    }

    /**
     * 取消背光消息
     */
    public void cancel()
    {
        mPowerHandler.removeMessages(0);
    }

    /**
     * 是否是低背光状�?
     * 
     * @return
     */
    public boolean isLowLight()
    {
        return isLightDown;
    }

    /**
     * 将背光恢�?     */
    public void listhUp()
    {
    	if(isLightDown)
    	{
    		if (null != mListener)
            {
                mListener.executeUp();
                isLightDown = false;
            }
    	}
    }

    /**
     * 降低背光
     */
    private void lightDown()
    {
        if (null != mListener && !isOut)
        {
            mListener.executeDown();
            isLightDown = true;
        }
    }

    /**
     * 比如手指触摸屏幕等，会重新计算背光的时间
     */
    public void interruptDelay()
    {
        lightDownDelay();
    }
    
    public void onDisplayOut()
    {
        cancel();
        listhUp();
        isOut = true;
    }
    
    public void onDisplayIn()
    {
        isOut = false;
        lightDownDelay();
    }
}
