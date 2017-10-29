/**
 * 
 */
package com.dvr.android.dvr.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 用于�?��网络<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 8 Mar 2012]
 */
public class NetWorkUtil
{
    /**
     * �?��网络是否可用
     * 
     * @return
     */
    public static boolean checkNetWorkEnable(Context context)
    {
        ConnectivityManager conMan =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状�? 则进入Network Setting界面 由用户配置网络连�?        
        if (mobile == State.CONNECTED || mobile == State.CONNECTING)
        {
            return true;
        }
        if (wifi == State.CONNECTED || wifi == State.CONNECTING)
        {
            return true;
        }
        return false;
    }
    
}