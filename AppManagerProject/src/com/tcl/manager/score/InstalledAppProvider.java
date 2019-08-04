package com.tcl.manager.score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.tcl.framework.notification.NotificationCenter;
import com.tcl.manager.firewall.FirewallManager;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.util.PkgManagerTool;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 缓存安装的用户的APP信息，为了少调用PkgManager产生的异常 package manager has died
 * 
 * @author jiaquan.huang
 * 
 */
public class InstalledAppProvider {
    Context mContext;
    private HashMap<String, ApplicationInfo> installMap = null;
    private HashMap<String, PackageInfo> installPackageInfoMap = null;
    static InstalledAppProvider provider;
    private HashMap<String, Bitmap> iconMap = new HashMap<String, Bitmap>();
    private LruCache<String, Bitmap> cache;

    final static Object lock = new Object();

    public static InstalledAppProvider getInstance() {
        synchronized (InstalledAppProvider.class) {
            if (provider == null) {
                provider = new InstalledAppProvider();
            }
            return provider;
        }
    }

    private InstalledAppProvider() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 8;
        cache = new LruCache<String, Bitmap>(cacheSize) {
            public int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight());
            }
        };
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        register();
    }

    public synchronized void load() {
        if (installMap == null) {
            installMap = new HashMap<String, ApplicationInfo>();
            installPackageInfoMap = new HashMap<String, PackageInfo>();
            PackageManager pm = mContext.getPackageManager();
            Collection<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pkg : packs) {
                installPackageInfoMap.put(pkg.packageName, pkg);
                installMap.put(pkg.packageName, pkg.applicationInfo);
            }
        }
    }

    public boolean isInstalled(String packageName) {
        if (installMap == null) {
            load();
        }
        return installMap.containsKey(packageName);
    }

    public ApplicationInfo getAppInfo(String packageName) {
        if (installMap == null) {
            load();
        }
        return installMap.get(packageName);
    }

    public PackageInfo getPackageInfo(String packageName) {
        if (installPackageInfoMap == null) {
            load();
        }
        return installPackageInfoMap.get(packageName);
    }

    /**
     * 获取用户安装
     **/
    public Collection<ApplicationInfo> getInstallUserApp() {
        if (installMap == null) {
            load();
        }
        return new ArrayList<ApplicationInfo>(installMap.values());
    }

    private void register() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addDataScheme("package");
            mContext.registerReceiver(mReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getIcon(String packageName) {
        return cache.get(packageName);
    }

    public void setIcon(String packageName, Bitmap bitmap) {
        cache.put(packageName, bitmap);
    }

    /**
     * 添加
     **/
    public void add(ApplicationInfo appInfo) {
        if (appInfo == null)
            return;
        if (installMap == null) {
            load();
        }
        synchronized (lock) {
            installMap.put(appInfo.packageName, appInfo);
        }
    }

    /** 移除 **/
    public void remove(String pkgName) {
        if (installMap == null) {
            load();
        }
        synchronized (lock) {
            installMap.remove(pkgName);
        }
    }

    public void onRemove(String packageName) {
        if (!PageDataProvider.getInstance().isInit()) {
            AutoStartBlackList.getInstance().removeFromBlackList(packageName);
            FirewallManager.getSingleInstance().remove(packageName);
        }
        PageFunctionProvider.setAutoStart(packageName, true);
        PageFunctionProvider.setDataAccess(packageName, true);
        PageFunctionProvider.uninstalledSuccess(packageName);
        NotificationCenter.defaultCenter().publish(new PackageChangeEvent(packageName, PackageChangeEvent.REMOVE));
    }

    public void onAdd(String packageName) {
        NotificationCenter.defaultCenter().publish(new PackageChangeEvent(packageName, PackageChangeEvent.ADD));
    }

    public static ApplicationInfo getApplicationInfo(String packageName) throws NameNotFoundException {
        return InstalledAppProvider.getInstance().getAppInfo(packageName);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getDataString() == null)
                return;
            final String pkgName = intent.getDataString().substring("package:".length());
            final String action = intent.getAction();
            new Thread("installAppsThread") {
                @Override
                public void run() {
                    super.run();
                    try {
                        if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                            /** 卸载 **/
                            remove(pkgName);
                            onRemove(pkgName);
                        } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                            /** 安装 **/
                            ApplicationInfo pkgInfo = PkgManagerTool.getApplicationInfo(mContext, pkgName, true);
                            add(pkgInfo);
                            onAdd(pkgName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }.start();

        }
    };
}
