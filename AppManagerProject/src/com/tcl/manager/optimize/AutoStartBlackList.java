package com.tcl.manager.optimize;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.tcl.framework.log.NLog;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.RunningAppInfo;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.NotificationSender;
import com.tcl.manager.util.PkgManagerTool;

/***
 * 自启动黑名单
 * 
 * @author jiaquan.huang
 * 
 */
public class AutoStartBlackList {
    /** 开机广播 **/
    public static String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    final static String STOREKEEPERNAME = "AUTO_START_BLACK_LIST";
    static volatile AutoStartBlackList blackNameList;
    SharedPreferences storeKeeper;
    Context mContext;
    /** key包名，boolean **/
    HashMap<String, Boolean> needAddBlackMap = new HashMap<String, Boolean>();

    public static AutoStartBlackList getInstance() {
        synchronized (AutoStartBlackList.class) {
            if (blackNameList == null) {
                blackNameList = new AutoStartBlackList();
            }
        }
        return blackNameList;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        storeKeeper = mContext.getSharedPreferences(STOREKEEPERNAME, Context.MODE_PRIVATE);
    }

    /** 加入黑名单 **/
    public boolean addToBlacklist(String packageName) {
        Editor edit = storeKeeper.edit();
        edit.putBoolean(packageName, true);
        needAddBlackMap.put(packageName, true);
        return edit.commit();
    }

    /** 删除黑名单 **/
    public boolean removeFromBlackList(String packageName) {
        Editor edit = storeKeeper.edit();
        edit.remove(packageName);
        needAddBlackMap.put(packageName, false);
        return edit.commit();
    }

    /** 获取状态 **/
    public boolean getValue(String pkgName) {
        return storeKeeper.getBoolean(pkgName, false);
    }

    /** 获取黑名单 **/
    public Map<String, Boolean> get() {
        Map<String, Boolean> all = new HashMap<String, Boolean>((Map<String, Boolean>) storeKeeper.getAll());
        Iterator<String> iter = all.keySet().iterator();
        while (iter.hasNext()) {
            String pkgName = iter.next();
            boolean isInstalled = PkgManagerTool.isAppInstalled(pkgName);
            if (!isInstalled) {
                iter.remove();
                removeFromBlackList(pkgName);
            }
        }
        return all;
    }

    /** 关闭 开机服务 **/
    private void closeBootCompleted(Map<String, Boolean> dataMap) {
        if (dataMap.isEmpty()) {
            return;
        }

        PackageManager pm = mContext.getPackageManager();
        Intent intent = new Intent(BOOT_COMPLETED);
        List<ResolveInfo> resolveInfo;
        try {
            /** package manager has dead 异常 **/
            resolveInfo = pm.queryBroadcastReceivers(intent, PackageManager.GET_DISABLED_COMPONENTS);

            for (ResolveInfo info : resolveInfo) {
                if (dataMap.containsKey(info.activityInfo.packageName) == false) {
                    continue;
                }
                boolean needAddBlackList = dataMap.get(info.activityInfo.packageName);
                String packageName = info.activityInfo.packageName;
                String reciverName = info.activityInfo.name;
                /** 直接修改不必管当前状态，当前状态无法判断 **/
                // boolean nowState = info.activityInfo.isEnabled();
                if (needAddBlackList == true) {
                    AdbCmdTool.disableReciver(packageName, reciverName);
                } else {
                    AdbCmdTool.enableReciver(packageName, reciverName);
                }
            }

        } catch (Exception e) {
            return;
        }

    }

    /** 关闭开机自启动 **/
    public void closeAutoStart() {
        Thread close = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    synchronized (AutoStartBlackList.class) {
                        closeBootCompleted(needAddBlackMap);
                        needAddBlackMap.clear();
                    }
                } catch (Exception e) {
                    NLog.e("package manager exception", e);
                }
            }
        });
        close.start();
    }

    public void killBackground() {
        String pkgName = AndroidUtil.getTopActivityPkgName(mContext);
        Map<String, RunningAppInfo> running = PkgManagerTool.getRunningAppInfos(mContext);
        Map<String, Boolean> blackList = get();
        /** 不杀在界面的 */
        blackList.remove(pkgName);
        Map<String, RunningAppInfo> needKill = new HashMap<String, RunningAppInfo>();
        for (String packageName : blackList.keySet()) {
            RunningAppInfo obj = running.get(packageName);
            if (obj != null) {
                needKill.put(packageName, obj);
            }
        }
        long total = 0;
        StringBuffer appName = new StringBuffer();
        StringBuffer memorySize = new StringBuffer();
        for (Entry<String, RunningAppInfo> entry : needKill.entrySet()) {
            RunningAppInfo temp = entry.getValue();
            total += temp.getMemsize();
            PageFunctionProvider.stop(temp.pkgName);
            appName.append(PkgManagerTool.getAppName(mContext, temp.pkgName));
            appName.append("、");
        }
        if (total == 0) {
            return;
        }

        memorySize.append(AndroidUtil.byteToMB(mContext, total));
        // NotificationSender.killBackgroundApp(mContext, appName.toString(),
        // memorySize.toString(), null);
        // Intent intent = new Intent(mContext, MainActivity.class);
        // NotificationSender.killBackgroundApp(mContext, releaseCount, intent);
        NotificationSender.notificationKillerProcesses(com.tcl.mie.manager.R.drawable.ic_launcher, needKill);
    }

    private final Object lock = AutoStartBlackList.class;

    /** 开启杀后台 */
    public void startKillThread() {
        Thread kill = new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (lock) {
                    killBackground();
                }
            }
        });
        kill.start();
    }

}
