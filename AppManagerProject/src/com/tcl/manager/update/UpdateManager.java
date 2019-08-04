package com.tcl.manager.update;

import java.io.File;
import java.util.Date;

import org.apache.http.util.VersionInfo;
 
import com.tcl.base.http.IProviderCallback;
import com.tcl.framework.log.NLog;
import com.tcl.manager.application.AMDirType;
import com.tcl.manager.application.AMDirectorManager;
import com.tcl.manager.protocol.VersionInfoProtocol;
import com.tcl.manager.protocol.data.VersionInfoPojo;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.LogSetting;
import com.tcl.manager.util.NotificationSender;
import com.tcl.manager.util.ObjectSharedPreference;
import com.tcl.manager.util.SharedStorekeeper;
import com.tcl.mie.manager.R;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

/**
 * @Description:版本管理
 * @author jiaquan.huang
 * @date 2014-12-13 下午8:08:33
 * @copyright TCL-MIE
 */

public class UpdateManager {
    UpdateManagerLoader loader;// 下载
    Context context;
    View view;// 窗口
    boolean isChecking = false;
    boolean isDowning = false;
    final int CHECK_BACK_SUCCESS = 0;// 检测成功
    final int CHECK_BACK_ERROR = 1;// 检测异常
    final int DOWNLOAD_SUCCESS = 2;// 下载成功
    final int DOWNLOAD_ERROR = 3;// 下载异常
    final int DOWNLOAD_UPDATE = 4;// 下载更新
    final int DOWNLOAD_CANCLE = 5;// 取消
    static UpdateManager manager;
    int forceUpdate = 0;
    VersionInfoPojo versionInfo; 
    OnUpadateConfirmListener mListener;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case CHECK_BACK_SUCCESS:
                update();
                break;
            case CHECK_BACK_ERROR:
                break;
            case DOWNLOAD_SUCCESS:
                notifyUpdate(allPath);
                break;
            case DOWNLOAD_ERROR:
                NLog.w(LogSetting.DEBUG_TAG, "update download error");
                break;
            case DOWNLOAD_UPDATE:

                break;
            case DOWNLOAD_CANCLE:

                break;
            default:
                break;
            }
        }
    };

    private UpdateManager() {

    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public static UpdateManager getInstance() {
        synchronized (UpdateManager.class) {
            if (manager == null) {
                manager = new UpdateManager();
            }
        }
        return manager;
    } 

    public void check(OnUpadateConfirmListener listener) { 
    	mListener = listener;
        if (isDowning) {
            // 正在下载无需在检测
            return;
        }
        VersionInfoProtocol infoProtocol = new VersionInfoProtocol(new IProviderCallback<VersionInfoPojo>() {

            @Override
            public void onSuccess(VersionInfoPojo obj) {
                if (obj != null) {
                    /** 成功 **/
                    versionInfo = obj;
                    handler.sendEmptyMessage(CHECK_BACK_SUCCESS);
                    
                }
            }

            @Override
            public void onFailed(int code, String msg, Object obj) {
                handler.sendEmptyMessage(CHECK_BACK_ERROR);
            }

            @Override
            public void onCancel() {
                handler.sendEmptyMessage(DOWNLOAD_CANCLE);
            }
        });
        infoProtocol.send();
    }

    private void update() {
        if (versionInfo == null || versionInfo.data == null) {
            return;
        }
        //int versionCode = versionInfo.data.versionCode;
        String versionName = versionInfo.data.versionName;
        forceUpdate = versionInfo.data.forceUpdate;
        //String downloadUrl = versionInfo.data.downloadUrl;
        //String updateInfo = versionInfo.data.downloadUrl;

        String fileNameTemp = fileName + versionName + ".apk";
        allPath = savePath + "/" + fileNameTemp;
        versionInfo.data.filePath = allPath;
        new ObjectSharedPreference(context).save(UpdateChecker.VISION_INFO, versionInfo.data);
       
        /**** 检查 **/
        if (!isValidUpdate(versionInfo)) {
            return;// 无效
        }

        /****** 分析是否已经下载 *****/
        if (isExists(allPath)) {
            handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
            return;
        }

        if (mListener == null) 
        {  
        	if (AndroidUtil.isWifiConnect(context))
        	{
        		download(savePath, fileNameTemp);
        	}
        }
        else
        {
        	mListener.onConfirm(versionInfo.data,savePath, fileNameTemp);
        }
        
//        /** wifi下需要升级 ***/
//        if (AndroidUtil.isWifiConnect(context)) {
//            download(savePath, fileNameTemp);
//        } else {
//            // IntentFilter filter = new IntentFilter();
//            // filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
//            // context.registerReceiver(mDownloadReceiver, filter);
//        }
    }

    /**
     * 检查App更新是否有效
     */
    private boolean isValidUpdate(VersionInfoPojo versionInfo) {
        boolean ret = true;
        if (versionInfo.data.versionCode <= AndroidUtil.getVersionCode(context) || TextUtils.isEmpty(versionInfo.data.downloadUrl)) {
            return false;
        }
        return ret;
    }

    /** thinking more **/
    String savePath = AMDirectorManager.getInstance().getDirectoryPath(AMDirType.apps);
    String fileName = "appManager";
    String allPath = null;

    /** thinking more **/

    public synchronized void download(String savePath, String fileName) {
        loader = new UpdateManagerLoader();
        String downloadUrl = versionInfo.data.downloadUrl;
        loader.init(savePath, fileName, downloadUrl, new ILoadingListener() {

            @Override
            public void notify(int totalSize, int tempSize, int schedule) {
                handler.sendEmptyMessage(DOWNLOAD_UPDATE);
                NLog.i("app", schedule / 1024f / 1024f + "MB");
            }

            @Override
            public void downloadError(int schedule) {
                handler.sendEmptyMessage(DOWNLOAD_ERROR);
                isDowning = false;
            }

            @Override
            public void downloadSuccess() {
                handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                isDowning = false;
            }
        });
        isDowning = true;
        loader.downloadApk();
    }

    private boolean isExists(String path) {
        String apkFilePath = path;
        File apk = new File(apkFilePath);
        // 是否已有最新文件
        if (apk.exists() && apk.canRead()) {
            return true;
        }
        return false;
    }

    private void notifyUpdate(String apkPath) {
        Intent intent = AndroidUtil.getInstallIntent(context, new File(apkPath));
        NotificationSender.appsUpdateNotification(context, 1, intent);
    }
    
    public interface OnUpadateConfirmListener
    {
    	public void onConfirm(UpdateInfo info, String savePath, String fileName);
    }
}
