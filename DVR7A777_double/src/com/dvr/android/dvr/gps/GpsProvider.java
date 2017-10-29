/**
 * 
 */
package com.dvr.android.dvr.gps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.dvr.android.dvr.bean.DrivePath;
import com.dvr.android.dvr.bean.DrivePosition;
import com.dvr.android.dvr.file.PathHelper;
import com.dvr.android.dvr.R;

/**
 * GPS数据获取�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 4 Feb 2012]
 */
public class GpsProvider implements LocationListener
{
    // private static final String TAG = "GpsProvider";

    private static final int UPDATE_RECORD_TIME = 1000;

    private LocationManager mLocationManager = null;

    private boolean mMediaRecorderRecording = false;

    /**
     * 录制�?��时间
     */
    private long mRecordingStartTime;

    private DrivePath mPath = null;

    private DrivePosition curPos = null;

    private long mCurSeconds;

    // private final DecimalFormat positionFormat = new DecimalFormat("0.");
    // create时注册此listener，Destroy时需要Remove
    // LocationListener mLocationListener = null;

    private Context mContext = null;

    private static GpsProvider mProvider = null;

    private ArrayList<GPSInfoChangedListener> mGpsListeners = new ArrayList<GPSInfoChangedListener>();

    private String provider = LocationManager.GPS_PROVIDER;

    private MyNetWorkLocationProvider netProvider = null;

    private String mSaveFilePath = null;

    private UpdateHadler mHandler = null;

    private HandlerThread updateThread = null;

    /**
     * 构�?
     * 
     * @param context
     */
    protected GpsProvider(Context context)
    {
        mContext = context;
        mPath = new DrivePath();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mProvider = this;
        initHandler();
        // networkLaction();
    }

    /**
     * 获取�?���?���?��位置
     */
    public Location getLastLocation()
    {
        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return location;
    }

    /**
     * 网络定位,使用网络辅助定位，能更快的获取粗略位�?     */
    private void networkLaction()
    {
        netProvider = new MyNetWorkLocationProvider(mContext);
        netProvider.getMyLocation(new LocationListener()
        {
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            public void onProviderEnabled(String provider)
            {
            }

            public void onProviderDisabled(String provider)
            {
            }

            public void onLocationChanged(Location location)
            {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                float speed = location.getSpeed();
                curPos =
                    new DrivePosition(latitude,
                        longitude,
                        System.currentTimeMillis(),
                        speed,
                        true,
                        mCurSeconds);
//                Log.d("tag", "net location = " + curPos.toString());
                // 定位到了然后移除定位�?                
                if (null != netProvider)
                {
                    netProvider.close();
                    netProvider = null;
                }
            }
        });
    }

    /**
     * 返回GPS管理�?     * 
     * @return
     */
    public static GpsProvider getGpsProvider(Context context)
    {
        if (null == mProvider)
        {
            mProvider = new GpsProvider(context);
        }
        return mProvider;
    }

    /**
     * �?��记录，与记录文件同步，�?
     * 
     * @param timeStr
     */
    public void startRecoder(String fileName)
    {
        mPath.clear();
        mSaveFilePath = fileName;
        //mMediaRecorderRecording = true;
        mRecordingStartTime = SystemClock.uptimeMillis(); //liujie add 1020
        // �?��循环
        //mHandler.sendEmptyMessage(UPDATE_RECORD_TIME);
    }
    
    public void startGps()
    {
    	mMediaRecorderRecording = true;
        mRecordingStartTime = SystemClock.uptimeMillis();
        mHandler.sendEmptyMessage(UPDATE_RECORD_TIME);
    }

    /**
     * 照相时调用，调用此方�?     * 
     * @param fileName
     */
    public void capture(String fileName)
    {
        DrivePath capturePath = new DrivePath();
        if (null != curPos)
        {
            DrivePosition temp = (DrivePosition) curPos.clone();
            temp.mValid = curPos.mValid;
            temp.mDelta = 0;
            temp.mTime = System.currentTimeMillis();
            capturePath.getPathData().add(temp);
        }
        else
        {
            curPos = new DrivePosition();
            curPos.mValid = false;
            curPos.mTime = System.currentTimeMillis();
            curPos.mDelta = 0;
            capturePath.getPathData().add((DrivePosition) curPos.clone());
        }
        PathHelper.writePath(capturePath, fileName);
    }

    /**
     * 停止�?��记录，传入需要保存的文件�?     * 
     * @param fileName
     */
    public void stopRecoder()
    {
        //mMediaRecorderRecording = false;
        PathHelper.writePath(mPath, mSaveFilePath);
    }

    /**
     * 放入此回调接口，可以获取当前GPS数据
     * 
     * @param listener
     */
    public void setGPSListener(GPSInfoChangedListener listener)
    {
        if (null != listener)
        {
            mGpsListeners.add(listener);
        }
    }

    /**
     * 移除监听
     * 
     * @param listener
     */
    public void removeListener(GPSInfoChangedListener listener)
    {
        if (null != listener)
        {
            mGpsListeners.remove(listener);
        }
    }

    /**
     * 停止记录GPS信息
     */
    public void stopGPS()
    {
        mMediaRecorderRecording = false;
        mLocationManager.removeUpdates(this);
        if (null != updateThread)
        {
            updateThread.quit();
        }
        // updateThread.interrupted();
    }

    /**
     * 完全结束，GPS定位，主意：只在程序�?��时使�?     */
    public void destory()
    {

    }

    private void updateRecordingTime()
    {
        if (!mMediaRecorderRecording)
        {
            return;
        }
        long now = SystemClock.uptimeMillis();
        long delta = now - mRecordingStartTime;

        // Starting a minute before reaching the max duration
        // limit, we'll countdown the remaining time instead.

        // 这里这样子计算时间会有误�?要修�?        
        long next_update_delay = 1000 - (delta % 1000);
        long seconds;

        seconds = delta / 1000; // round to nearest
        mCurSeconds = seconds;

        if (null != curPos)
        {
            // curPos.mValid = true;
            curPos.mDelta = mCurSeconds;
            curPos.mTime = System.currentTimeMillis();
            // Log.d(TAG, "recoder " + curPos.toString());
            // clone�?�� 防止出现诡异BUG
            DrivePosition tmpPosition = null;

            tmpPosition = (DrivePosition) curPos.clone();
            // 记录这个�?            
            mPath.recordPosition(tmpPosition);

        }
        // 如果还没有定位好就拿这里时间,定位还是使用原来的定位吗？？？？
        else
        {
            curPos = new DrivePosition();
            curPos.mValid = false;
            curPos.mTime = System.currentTimeMillis();
            curPos.mDelta = mCurSeconds;
            // Log.d(TAG, "recoder " + curPos.toString());
            // 记录这个�?            
            mPath.recordPosition(curPos);
        }

        // 将数据回�?        
        for (int i = 0; i < mGpsListeners.size(); i++)
        {
            GPSInfoChangedListener listener = mGpsListeners.get(i);
            if (null == listener)
            {
                mGpsListeners.remove(i);
                i--;
            }
            else
            {   //Log.i("PLJ", "GpsProvider---->updateRecordingTime:"+next_update_delay);
                listener.onGPSInfoChanged(curPos);
            }
        }
        mHandler.sendEmptyMessageDelayed(UPDATE_RECORD_TIME, next_update_delay);
    }

    /**
     * 停止记录
     */
    // public void stop()
    // {
    // mMediaRecorderRecording = false;
    // }

    /**
     * 返回行车路劲
     * 
     * @return
     */
    public DrivePath getPath()
    {
        return mPath;
    }

    /**
     * init handler
     */
    private void initHandler()
    {
        HandlerThread updateThread = new HandlerThread("updateGPS");
        updateThread.start();
        mHandler = new UpdateHadler(updateThread.getLooper());
        mLocationManager.requestLocationUpdates(provider, 1000, 0, this, updateThread.getLooper());
    }

    public void onLocationChanged(Location location)
    {
        if (null == location)
        {
            // curPos = new DrivePosition();
            // Log.d(TAG, "location is null");
        }
        else
        {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            float speed = location.getSpeed();
            // long delta = getStartOffTime();
            curPos =
                new DrivePosition(latitude, longitude, System.currentTimeMillis(), speed, true, mCurSeconds);
            // Log.d(TAG, curPos.toString());
//            Log.d("tag", "gps location = " + curPos.toString());
            if (null != netProvider)
            {
                netProvider.close();
                netProvider = null;
            }
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    public void onProviderEnabled(String provider)
    {
    }

    public void onProviderDisabled(String provider)
    {
    }

    /**
     * GPS是否可用，true可用 false不可�?     * 
     * @return
     */
    public boolean isGpsEnable()
    {
        boolean enable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enable;
    }

    /**
     * 跳转到GPS设置界面
     */
    public void toSettingGPS()
    {
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 构建GPS提示�?     * 
     * @return
     */
    public Dialog createGPSNoSeetingDialog()
    {
        return new AlertDialog.Builder(mContext).setIcon(R.drawable.dialog_icon_warring)
            .setTitle(R.string.wariness)
            .setMessage(R.string.gps_tips)
            .setPositiveButton(R.string.gps_gosetting, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    toSettingGPS();
                }
            })
            .setNegativeButton(R.string.gps_nosetting, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {

                }
            })
            .create();
    }

    class UpdateHadler extends Handler
    {
        public UpdateHadler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 更新时间
                case UPDATE_RECORD_TIME:
                    updateRecordingTime();
                    break;
            }
        }
    }
}
