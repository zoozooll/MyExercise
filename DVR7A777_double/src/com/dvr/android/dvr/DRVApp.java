package com.dvr.android.dvr;

import android.app.Application;
import android.widget.Toast;

import com.dvr.android.dvr.gps.GpsProvider;

public class DRVApp extends Application
{

    static DRVApp mDemoApp;

//    // 百度MapAPI的管理类
//    private BMapManager mBMapMan = null;

    // 授权Key
    // TODO: 请输入您的Key,
    // 申请地址：http://dev.baidu.com/wiki/static/imap/key/
//    //String mStrKey = "E9480AF2D01B48ED0A82C2852C57B0BB7D74390A";
//    String mStrKey = "E9480AF2D01B48ED0A82C2852C57B0BB7D74390A";
//    
//    boolean m_bKeyRight = true; // 授权Key正确，验证�?�?
    
    private Toast mToast = null;

//    // 常用事件监听，用来处理�?常的网络错误，授权验证错误等
//    public static class MyGeneralListener implements MKGeneralListener
//    {
//        @Override
//        public void onGetNetworkState(int iError)
//        {
////            Toast.makeText(DRVApp.mDemoApp.getApplicationContext(), "您的网络出错啦！", Toast.LENGTH_LONG).show();
//            Log.d("TAG", "baidmap network is disable");
//        }
//
//        @Override
//        public void onGetPermissionState(int iError)
//        {
//            if (iError == MKEvent.ERROR_PERMISSION_DENIED)
//            {
//                // 授权Key错误�?
//                Toast.makeText(DRVApp.mDemoApp.getApplicationContext(),
//                    "请在BMapApiDemoApp.java文件输入正确的授权Key�?,
//                    Toast.LENGTH_LONG).show();
//                DRVApp.mDemoApp.m_bKeyRight = false;
//            }
//        }
//
//    }

//    public String getKey()
//    {
//        return mStrKey;
//    }
//
//    public BMapManager getBMapManager()
//    {
//        return mBMapMan;
//    }
//    
//    public void setBMapManager(BMapManager bmManager)
//    {
//        mBMapMan = bmManager;
//    }
    
    public static Application getApplication()
    {
        return mDemoApp;
    }

    @Override
    public void onCreate()
    {
        mDemoApp = this;
//        mBMapMan = new BMapManager(this);
//        mBMapMan.init(this.mStrKey, new MyGeneralListener());
        GpsProvider.getGpsProvider(this);
        super.onCreate();
        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext());
    }

    @Override
    // 建议在您app的�?出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消�?
    public void onTerminate()
    {
        // TODO Auto-generated method stub
//        if (mBMapMan != null)
//        {
//            mBMapMan.destroy();
//            mBMapMan = null;
//        }
        GpsProvider.getGpsProvider(this).stopGPS();
        super.onTerminate();
    }
    
    /**
     * 弹出toast提示
     * 
     * @param tip string
     */
    public void showToast(String tip)
    {
        if (null == mToast)
        {
            mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }

        mToast.setText(tip);
        mToast.show();
    }

}
