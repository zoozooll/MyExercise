package com.tcl.manager.pkgsize;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import com.tcl.framework.log.NLog;
import com.tcl.manager.score.RunningAppInfo;

/**
 * @Description:通过反射，获取APK大小和缓存
 * @premission android.permission.GET_PACKAGE_SIZE
 * @author jiaquan.huang
 * @date 2014-12-11 下午5:10:52
 * @copyright TCL-MIE
 */
public class StorageProvider {
    private static String TAG = "StorageManager";
    private static Method mdGetPackageSizeInfo;
    private IDataBack dataBack;

    public interface IDataBack {
        public void dataBack(HashMap<String, PkgSizeInfo> data);
    };

    static {
        try {
            mdGetPackageSizeInfo = PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        } catch (Exception e) {
            NLog.e(TAG, e);
        }
    }

    public void loadingData(Context context, Collection<PkgSizeInfo> appInfo, IDataBack dataBack) {
        HashMap<String, PkgSizeInfo> apps = new HashMap<String, PkgSizeInfo>();
        for (PkgSizeInfo info : appInfo) {
            apps.put(info.pkgName, info);
        }
        this.dataBack = dataBack;
        Load loading = new Load(context, apps);
        loading.start();
        try {
            /** 父线程等待 **/
            loading.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** 加载storage **/
    class Load extends Thread {
        private CountDownLatch count;
        private HashMap<String, PkgSizeInfo> appCache;
        PackageManager pm;

        public Load(Context context, HashMap<String, PkgSizeInfo> apps) {
            pm = context.getApplicationContext().getPackageManager();
            appCache = apps;
        }

        @Override
        public void run() {
            int size = appCache.size();
            if (size == 0) {
                return;
            }
            count = new CountDownLatch(size);
            PkgSizeObserver sizeObserver = new PkgSizeObserver(count, appCache);
            for (PkgSizeInfo info : appCache.values()) {
                sizeObserver.invokeGetPkgSize(info.pkgName, pm);
            }
            try {
                count.await();
            } catch (Exception e) {
                NLog.e(TAG, e);
            } finally {
                if (dataBack != null) {
                    dataBack.dataBack(appCache);
                }
            }
        }
    };

    /**
     * PackageSizeObserver
     */
    private static final class PkgSizeObserver extends IPackageStatsObserver.Stub {

        private CountDownLatch count;
        private HashMap<String, PkgSizeInfo> appCache;

        PkgSizeObserver(CountDownLatch count, HashMap<String, PkgSizeInfo> appCache) {
            this.count = count;
            this.appCache = appCache;
        }

        private void invokeGetPkgSize(String pkgName, PackageManager pm) {
            if (mdGetPackageSizeInfo != null) {
                try {
                    mdGetPackageSizeInfo.invoke(pm, pkgName, this);
                } catch (Exception e) {
                    count.countDown();
                    NLog.e(TAG, e);
                }
            }
        }

        /** 赋值 **/
        private void parseData(PackageStats pStats) {
            try {
                PkgSizeInfo info = appCache.get(pStats.packageName);
                if (info != null) {
                    info.codeSize = pStats.codeSize;
                    info.dataSize = pStats.dataSize;
                    info.cacheSize = pStats.cacheSize;
                }
            } catch (Exception e) {
                NLog.e(TAG, e);
            }
        }

        /** 数据回调 **/
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            parseData(pStats);
            count.countDown();
        }
    }

}
