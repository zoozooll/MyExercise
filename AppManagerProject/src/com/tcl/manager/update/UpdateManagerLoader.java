package com.tcl.manager.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import android.text.TextUtils;
import com.tcl.framework.log.NLog;
import com.tcl.manager.util.LogSetting;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @Description: 版本下载
 * @author jiaquan.huang
 * @date 2014-12-13 下午7:33:45
 * @copyright TCL-MIE
 */
public class UpdateManagerLoader {

    private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_EXCEPTION = 3;
    // 终止标记
    private boolean interceptFlag;
    // 下载文件大小
    private int apkFileSize;
    // 已下载文件大小
    private int tmpFileSize;
    // 进度值
    private int progress = 0;
    // apk保存完整路径
    private String apkFilePath = "";
    // 临时下载文件路径
    private String tmpFilePath = "";
    // 返回的安装包url
    private String apkUrl = "";
    ILoadingListener notificaton;
    /**
     * 实例化Handler
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case DOWN_UPDATE:
                notificaton.notify(apkFileSize, tmpFileSize, progress);
                break;
            case DOWN_OVER:
                notificaton.downloadSuccess();
                break;
            case DOWN_NOSDCARD:
                notificaton.downloadError(progress);
                break;
            case DOWN_EXCEPTION:
                notificaton.downloadError(progress);
                break;
            }
        };
    };

    private boolean initFile(String savePath, String fileName, String downloadUrl) {
        apkUrl = downloadUrl;
        String apkName = fileName;
        String tmpApk = fileName + ".tmp";
        // 判断是否挂载了SD卡
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            apkFilePath = savePath + "/" + apkName;
            tmpFilePath = savePath + "/" + tmpApk;
        }

        // 没有挂载SD卡，无法下载文件
        if (TextUtils.isEmpty(apkFilePath)) {
            mHandler.sendEmptyMessage(DOWN_NOSDCARD);
            return false;
        }
        return true;
    }

    public void init(String savePath, String fileName, String downloadUrl, ILoadingListener notificaton) {
        initFile(savePath, fileName, downloadUrl);
        this.notificaton = notificaton;
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (UpdateManager.class) {
                try {
                    File ApkFile = new File(apkFilePath);
                    // 输出临时下载文件
                    File tmpFile = new File(tmpFilePath);
                    FileOutputStream fos = new FileOutputStream(tmpFile);
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    // 进度条下面显示的总文件大小
                    apkFileSize = length;

                    int count = 0;
                    byte buf[] = new byte[8192];

                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 进度条下面显示的当前下载文件大小
                        tmpFileSize = count;
                        // 当前进度值
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            // 下载完成 - 将临时下载文件转成APK文件
                            if (tmpFile.renameTo(ApkFile)) {
                                // 通知安装
                                mHandler.sendEmptyMessage(DOWN_OVER);
                            }
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!interceptFlag);// 点击取消就停止下载

                    fos.close();
                    is.close();
                } catch (Exception e) {
                    NLog.e(LogSetting.DEBUG_TAG, e);
                    mHandler.sendEmptyMessage(DOWN_EXCEPTION);
                }
            }
        }
    };

    public void stop() {
        // 终止标记
        interceptFlag = false;
    }

    public void reStart() {
    }

    /**
     * 下载apk
     */
    public void downloadApk() {
        Thread downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

}
