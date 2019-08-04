package com.tcl.manager.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.tcl.framework.db.EntityManagerFactory;
import com.tcl.manager.application.ManagerApplication;

@SuppressLint("NewApi")
public class AndroidUtil {
    public static final int DB_VERSION = 1;
    public static final String DB_ACCOUNT = "ostore";

    public static String getnetworkInfoName(Context mContext) {
        ConnectivityManager connectionManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        /** 无网络 */
        if (networkInfo == null) {
            return "";
        }
        return networkInfo.getTypeName();
    }

    /** 内存转换,B转MB **/
    public static String byteToMB(Context context, long size) {
        String mb = Formatter.formatFileSize(context, size);
        return mb;
    }

    // 获取系统版本
    public static String getVersionRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    // 获取系统版本
    public static int getVersionSDKINT() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @SuppressWarnings("deprecation")
    public static int getDisplayMetricsWidth(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getWidth();
    }

    @SuppressWarnings("deprecation")
    public static int getDisplayMetricsHeight(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取打开应用的Intent
     */
    public static Intent getOpenIntent(ResolveInfo resolveInfo, String packageName) {
        ComponentName componet = new ComponentName(packageName, resolveInfo.activityInfo.name);
        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setComponent(componet);
        return i;
    }

    /**
     * 获取安装应用的Intent
     */
    public static Intent getInstallIntent(Context mContext, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 是否为系统应用
     * 
     * @param context
     * @return
     */
    public static boolean isSystemApp(Context context) {
        return ((context.getApplicationInfo()).flags & ApplicationInfo.FLAG_SYSTEM) > 0;
    }

    public static boolean systemInstall(String apkPath) {
        String result = sysInstall(apkPath).trim();
        int lastIndex = result.lastIndexOf("/n");
        if (lastIndex == -1) {
            return false;
        }
        result = result.substring(lastIndex + 2).toLowerCase();
        LogUtil.v(apkPath + " install path result :" + result);
        return "success".equals(result);
    }

    /**
     * 系统级自动安装
     * 
     * @param apkPath
     * @return
     */
    public static String sysInstall(String apkPath) {
        String[] args = { "pm", "install", "-r", apkPath };
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write("/n".getBytes("UTF-8"));
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    /**
     * root手机的自动安装
     * 
     * @param cmd
     * @return
     */
    public static boolean execRootCmdSilent(String apkPath) {
        int result = -1;
        DataOutputStream dos = null;
        String cmd = "pm install -r " + apkPath;
        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result == 0;
    }

    /**
     * 获取图片资源
     * 
     * @param mContext
     * @param name
     * @return
     */
    public static int getResource(Context mContext, String name) {
        ApplicationInfo appInfo = mContext.getApplicationInfo();
        return mContext.getResources().getIdentifier(name, "drawable", appInfo.packageName);
    }

    /**
     * 获取app信息默认图片资源
     * 
     * @param mContext
     * @param name
     * @return
     */
    public static int getDefaultAppInfoPicture(Context mContext) {
        return getResource(mContext, "screen"); /* 暂时为这个图片 */
    }

    /**
     * 获取app默认图片资源
     * 
     * @param mContext
     * @param name
     * @return
     */
    public static int getDefaultAppPicture(Context mContext) {
        return getResource(mContext, "ic_list_app_default"); /* 暂时为这个图片 */
    }

    /**
     * 获取banner big默认图片资源
     * 
     * @param mContext
     * @param name
     * @return
     */
    public static int getDefaultBigBannerPicture(Context mContext) {
        return getResource(mContext, "banner_big_default"); /* 暂时为这个图片 */
    }

    /**
     * 获取banner small默认图片资源
     * 
     * @param mContext
     * @param name
     * @return
     */
    public static int getDefaultSmallBannerPicture(Context mContext) {
        return getResource(mContext, "banner_small_default"); /* 暂时为这个图片 */
    }

    /**
     * 判断网络是否连接
     * 
     * @param mContext
     * @return
     */
    public static boolean isNetConnect(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected()) {
            return true;
        }
        if (gprs != null && gprs.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否wifi网络
     * 
     * @param mContext
     * @return
     */
    public static boolean isWifiConnect(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi != null && wifi.isConnected()) {
            return true;
        }
        return false;
    }

    public static String getImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSubscriberId();
    }

    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取版本name
     * 
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            return (manager.getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本code
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {

        }
        return verCode;
    }

    /**
     * make true current connect service is wifi
     * 
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 指定位置颜色
     * 
     * @param text
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder setStringPartColor(String text) {
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.setSpan(new ForegroundColorSpan(0xFF5db224), text.lastIndexOf("->"), text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return style;
    }

    public static boolean isHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        List<String> homePackageNames = getHomes(context);
        return homePackageNames.contains(rti.get(0).topActivity.getPackageName());
    }

    private static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        // 属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
            // System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }

    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private static int systemRootState = kSystemRootStateUnknow;

    public static boolean isRootSystem() {
        if (systemRootState == kSystemRootStateEnable) {
            return true;
        } else if (systemRootState == kSystemRootStateDisable) {

            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/" };
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e) {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

    /**
     * convert dip to px
     * 
     * @param dipValue
     * @param scale
     * @return
     */
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    public static float convertDIP2PX(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /*
     * 获取当前程序包名的名字
     */
    public static String getPackAgeName(Context context) {
        String packageNames = "";
        packageNames = context.getPackageName();
        return packageNames;

    }

    public static String getTopActivityPkgName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        try {
            if (runningTaskInfos != null)
                return (runningTaskInfos.get(0).topActivity).getPackageName();
            else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

	/**
	 * String 时间传入 转换出 yyyy/MM/dd
	 * 
	 * @param time
	 * @return
	 */
	/*public static String time2Date(String time) {
		String dateString = "";
		LogUtil.v("time:" + time);
		if (!TextUtils.isEmpty(time) && TextUtils.isDigitsOnly(time)) {
			Date currentTime = new Date(Long.valueOf(time));



			java.text.DateFormat formatter = DateFormat.getDateFormat(ManagerApplication.sApplication);
			dateString = formatter.format(currentTime);
		}
		return dateString;
	}*/
    /**
     * 自动适配保持底部水平
     * 
     * @param context
     * @param view
     * @param minHeight
     * @param maxHeight
     * @param position
     * @param marginBottom
     */
    public static void autoScrollToAlignBottom(Context context, ListView view, int minHeight, int maxHeight, int position, int marginBottom) {
        int height = (position - view.getFirstVisiblePosition()) * minHeight + maxHeight;
        int distance = height + marginBottom - view.getHeight();

        if (distance > 0) {
            if (Build.VERSION.SDK_INT >= 11) {
                view.smoothScrollToPositionFromTop(view.getFirstVisiblePosition(), -distance);
            } else {
                view.smoothScrollToPosition(position + 1);
            }
        }
    }

    /**
     * 自动适配保持底部水平
     * 
     * @param context
     * @param view
     * @param position
     */
    public static void autoScrollToAlignBottom(Context context, ListView view, int position) {
        autoScrollToAlignBottom(context, view, convertDIP2PX(context, 81), convertDIP2PX(context, 157), position, convertDIP2PX(context, 10));
    }

    /**
     * 删除当前应用的桌面快捷方式
     * 
     * @param cx
     */
    public static void delShortcut(Context cx) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), PackageManager.GET_META_DATA)).toString();
        } catch (Exception e) {
        }
        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        Intent shortcutIntent = cx.getPackageManager().getLaunchIntentForPackage(cx.getPackageName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        cx.sendBroadcast(shortcut);
    }

    /**
     * 判断桌面是否已添加快捷方式
     * 
     * @param cx
     * @param titleName
     *            快捷方式名称
     * @return
     */
    public static boolean hasShortcut(Context cx) {
        boolean result = false;
        // 获取当前应用名称
        String title = null;
        try {
            final PackageManager pm = cx.getPackageManager();
            title = pm.getApplicationLabel(pm.getApplicationInfo(cx.getPackageName(), 0)).toString();
        } catch (Exception e) {
        }

        final String uriStr;
        if (android.os.Build.VERSION.SDK_INT < 8) {
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        final Uri CONTENT_URI = Uri.parse(uriStr);
        final Cursor c = cx.getContentResolver().query(CONTENT_URI, null, "title=?", new String[] { title }, null);
        if (c != null && c.getCount() > 0) {
            result = true;
        }
        /* System.out.println("判断" + result); */
        return result;
    }

    /**
     * 嵌套listView 解决冲突
     * 
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        int height = getListViewHeight(listView);

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;
        listView.setLayoutParams(params);
    }

    public static int getListViewHeight(ListView listView) {
        HeaderViewListAdapter appAdapter = (HeaderViewListAdapter) listView.getAdapter();
        if (appAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < appAdapter.getCount(); i++) {
            View listItem = appAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        return totalHeight + (listView.getDividerHeight() * (appAdapter.getCount() - 1));
    }

    public static String getMetaData(Context context, String name) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        Object value = null;
        try {

            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return value == null ? null : value.toString();
    }

    public static String formatTime(long time, String hours, String mins) {
        DecimalFormat df = new DecimalFormat("0.0");
        if (time >= 60 * 60) {
            return df.format(time / 60f / 60f) + hours;
        }

        return df.format(time / 60f) + mins;
    }

    public static String formatSize(long size) {
        DecimalFormat df = new DecimalFormat("0.0");
        if (size >= 1024 * 1024 * 1024) {
            return df.format(size / 1024f / 1024f / 1024f) + "GB";
        }
        if (size >= 1024 * 1024) {
            return df.format(size / 1024f / 1024f) + "MB";
        }
        if (size >= 1024) {
            return df.format(size / 1024f) + "KB";
        }

        return df.format(size) + "B";
    }

    public static String formatBigSize(BigDecimal size) {
        BigDecimal big1 = new BigDecimal(1024f * 1024f);
        BigDecimal big2 = big1.multiply(BigDecimal.valueOf(1024f));

        DecimalFormat df = new DecimalFormat("0.0");

        if (size.compareTo(big2) >= 0) {
            return df.format(size.divide(big2)) + "GB";
        }
        if (size.compareTo(big1) >= 0) {
            return df.format(size.divide(big1)) + "MB";
        }
        if (size.compareTo(BigDecimal.valueOf(1024f)) >= 0) {
            return df.format(size.divide(BigDecimal.valueOf(1024f))) + "KB";
        }

        return df.format(size) + "B";
    }

    /**
     * uninstall apk
     * 
     * @param packageName
     */
    public static void uninstallAPK(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*
     * public boolean isHome(Context context) { ActivityManager mActivityManager
     * = (ActivityManager) context .getSystemService(Context.ACTIVITY_SERVICE);
     * List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
     * List<String> homePackageNames = getHomes(context); return
     * homePackageNames.contains(rti.get(0).topActivity .getPackageName()); }
     * 
     * private List<String> getHomes(Context context) { List<String> names = new
     * ArrayList<String>(); PackageManager packageManager =
     * context.getPackageManager(); // 属性 Intent intent = new
     * Intent(Intent.ACTION_MAIN); intent.addCategory(Intent.CATEGORY_HOME);
     * List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
     * intent, PackageManager.MATCH_DEFAULT_ONLY); for (ResolveInfo ri :
     * resolveInfo) { names.add(ri.activityInfo.packageName); //
     * System.out.println(ri.activityInfo.packageName); } return names; }
     */

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
