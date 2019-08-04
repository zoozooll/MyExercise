package com.tcl.manager.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.DebugUtils;

import com.tcl.framework.log.NLog;
import com.tcl.manager.analyst.AppFilter;
import com.tcl.manager.score.InstalledAppProvider;
import com.tcl.manager.score.RunningAppInfo;

/**
 * @Description:代理PackageManager\ActivityManager相关的功能
 * @author jiaquan.huang
 * @date 2014-12-9 下午5:59:56
 * @copyright TCL-MIE
 */
public class PkgManagerTool {

    /** 获取手机中所有的APP **/
    public static Collection<ApplicationInfo> getInstalledApp(Context context) {
        return InstalledAppProvider.getInstance().getInstallUserApp();
    }

    /** Get all applications removed system applications */
    public static Collection<ApplicationInfo> getInstalledAppFilter(Context context) {
        return InstalledAppProvider.getInstance().getInstallUserApp();
    }

    /** 获取有某权限的APP **/
    public static List<ApplicationInfo> getAppByPremssion(Context context, String permssion) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();
        Collection<ApplicationInfo> installedApps = getInstalledApp(context);
        for (ApplicationInfo appInfo : installedApps) {
            int iBoot = pm.checkPermission(permssion, appInfo.packageName);
            if (iBoot == PackageManager.PERMISSION_GRANTED) {
                apps.add(appInfo);
            }
        }
        return apps;
    }

    /** 获取冻结APP列表 **/
    public static List<ApplicationInfo> listBootApps(Context context) {
        List<ApplicationInfo> apps = new ArrayList<ApplicationInfo>();
        Collection<ApplicationInfo> appInfos = getInstalledApp(context);
        for (ApplicationInfo appInfo : appInfos) {
            if (!appInfo.enabled) {
                apps.add(appInfo);
            }
        }
        return apps;
    }

    /** 获取正在运行的进程 **/
    public static List<RunningAppProcessInfo> getRunningProcessInfo(Context context) {
        try {

            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> runingappsInfos = am.getRunningAppProcesses();
            return runingappsInfos;
        } catch (Exception e) {
            return new ArrayList<ActivityManager.RunningAppProcessInfo>();
        }
    }

    /** 通过进程pid获取进程所占内存的大小 **/
    public static long getProcessMemory(Context context, int pid) {
        long memsize = 0;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            memsize = am.getProcessMemoryInfo(new int[] { pid })[0].getTotalPrivateDirty() * 1024;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memsize;
    }

    /**
     * 通过packageName获取应用程序对象ApplicationInfo
     * 
     **/
    public static ApplicationInfo getApplicationInfo(Context context, String packageName) {
        return getApplicationInfo(context, packageName, false);
    }

    public static ApplicationInfo getApplicationInfo(Context context, String packageName, boolean forceGet) {
        try {
            if (forceGet) {
                PackageManager pm = context.getPackageManager();
                return pm.getApplicationInfo(packageName, 0);
            } else {
                return InstalledAppProvider.getInstance().getAppInfo(packageName);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /** 通过包名获取图标 **/
    public static Drawable getIcon(Context context, String packageName) {
        ApplicationInfo info;
        try {
            info = getApplicationInfo(context, packageName);
            PackageManager pm = context.getPackageManager();
            return info.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 通过包名获取APP名字 **/
    public static String getAppName(Context context, String packageName) {
        ApplicationInfo info;
        try {
            info = getApplicationInfo(context, packageName);
            PackageManager pm = context.getPackageManager();
            return info.loadLabel(pm).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return packageName;
        }
    }

    /** 通过包名获取APP安装时间 **/
    public static long getInstallTime(Context context, String packageName) {
        PackageInfo info;
        try {
            info = InstalledAppProvider.getInstance().getPackageInfo(packageName);
            return info.firstInstallTime;
        } catch (Exception e) {
            NLog.e(LogSetting.DEBUG_TAG, e);
            return 0;
        }
    }

    public static long getUpdateTime(Context context, String pkgName) {
        PackageInfo info;
        try {
            info = InstalledAppProvider.getInstance().getPackageInfo(pkgName);
            return info.lastUpdateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*** app是否安装 **/

    public static boolean isAppInstalled(String pkgName) {
        return InstalledAppProvider.getInstance().isInstalled(pkgName);
    }

    /**
     * 三方应用的过滤器 ,如
     * 
     * @param info
     * @return true 三方应用 false 系统应用
     */
    private static boolean isUserApp(ApplicationInfo info) {
        try {
            if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 通过包名判别存在的三方应用
     * 
     * @param packageName
     *            包名
     * @return true 三方应用; false 系统应用||不存在的应用
     */
    public static boolean isUserApp(Context context, String packageName) {
        ApplicationInfo info;
        try {
            info = getApplicationInfo(context, packageName);
            if (info == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return isUserApp(info);
    }

    /**
     * 通过包名判别存在的三方应用
     * 
     * @param packageName
     *            包名
     * @return true 三方应用; false 系统应用||不存在的应用
     */
    public static boolean isDefaultUserApp(Context context, String packageName) {
        if (packageName.equals(context.getPackageName())) {
            /** 自身算系统应用 **/
            return false;
        }
        return isUserApp(context, packageName);
    }

    /**
     * 返回所有的正在运行的程序信息
     * 
     * @return
     */
    public static Map<String, RunningAppInfo> getRunningAppInfos(Context context) {
        List<RunningAppProcessInfo> runningAppProcessInfo = PkgManagerTool.getRunningProcessInfo(context);
        Map<String, RunningAppInfo> appInfoMaps = new HashMap<String, RunningAppInfo>();
        for (RunningAppProcessInfo info : runningAppProcessInfo) {
            RunningAppInfo AppInfo = new RunningAppInfo();
            try {
                String pkgName = null;
                if (info.processName.contains(":")) {
                    pkgName = info.processName.substring(0, info.processName.indexOf(":"));
                } else {
                    pkgName = info.processName;
                }
                if (appInfoMaps.containsKey(pkgName)) {
                    continue;
                }
                if (!AppFilter.getInstance().isNeedShow(context, pkgName)) {
                    continue;
                }
                int pid = info.pid; // 进程的pid
                long memsize = PkgManagerTool.getProcessMemory(context, pid); // 内存空间
                AppInfo.setPid(pid);
                AppInfo.pkgName = pkgName;
                AppInfo.setMemsize(memsize);
                AppInfo.setAppName(PkgManagerTool.getAppName(context, AppInfo.pkgName));
                appInfoMaps.put(AppInfo.pkgName, AppInfo);
            } catch (Exception e) {
                // NLog.w(LogSetting.DEBUG_TAG, e.getMessage());
            }
        }
        return appInfoMaps;
    }

    public static int getUid(Context context, String pkgName) {
        ApplicationInfo info = getApplicationInfo(context, pkgName);
        if (info != null) {
            return info.uid;
        } else {
            return -1;
        }
    }

}
