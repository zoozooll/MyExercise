package com.tcl.manager.pkgsize;

import java.lang.reflect.Method;
import com.tcl.framework.log.NLog;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.RemoteException;

/**
 * 清理Appcache,需要系统签名
 * 
 * @author jiaquan.huang
 */
public class CleanAppCache {
    private static String TAG = "deleteApplicationCacheFiles";
    private static Method deleteApplicationCacheFiles;

    public interface ICallBack {
        public void callBack(boolean isSuccess);
    };

    static {
        try {
            deleteApplicationCacheFiles = PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class, IPackageDataObserver.class);
        } catch (Exception e) {
            NLog.e(TAG, e.getMessage());
        }
    }

    /** 清理缓存 **/
    public static void cleanCache(PackageManager pm, String pkgName, ICallBack callBack) {
        try {
            deleteApplicationCacheFiles.invoke(pm, pkgName, new PkgDataObserver(pkgName, callBack));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final class PkgDataObserver extends IPackageDataObserver.Stub {
        private ICallBack iCallBack;
        private String pkgName;

        public PkgDataObserver(String pkgName, ICallBack iCallBack) {
            this.iCallBack = iCallBack;
            this.pkgName = pkgName;
        }

        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            if (iCallBack != null && pkgName != null && pkgName.equals(packageName)) {
                iCallBack.callBack(succeeded);
            }
        }
    }
}
