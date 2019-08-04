package com.tcl.manager.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tcl.framework.util.AndroidNewApi;
import com.tcl.mie.manager.R;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;

/**
 * @Description:内存信息
 * @author jiaquan.huang
 * @date 2014-12-9 下午2:44:43
 * @copyright TCL-MIE
 */

public class MemoryInfoProvider {
    private static MemoryInfoProvider provider;
    /** 可用内存,单位B **/
    public long availMem;
    /** 总内存,单位B **/
    public long totalMem;
    /** 是否低内存运行 **/
    public boolean lowMemoryRun;

    private MemoryInfoProvider() {
    }

    public static MemoryInfoProvider getInstance(Context context) {
        if (provider == null) {
            provider = new MemoryInfoProvider();
        }
        Context cxt = context.getApplicationContext();
        provider.getInfo(cxt);
        return provider;
    }

    /** 获取内存信息 **/
    @SuppressLint("NewApi")
    private void getInfo(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        availMem = outInfo.availMem;
        totalMem = outInfo.totalMem;
        if (AndroidNewApi.IsSDKLevelAbove(16)) {
            totalMem = outInfo.totalMem;
        } else {
            totalMem = getTotalMem();
        }
        lowMemoryRun = outInfo.lowMemory;
    }

    /** 从文件中获取总内存信息 **/
    private static long getTotalMem() {
        long mTotal;
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
            // beginIndex
            if (content == null) {
                return 0;
            }
            int begin = content.indexOf(':');
            // endIndex
            int end = content.indexOf('k');
            content = content.substring(begin + 1, end).trim();
            mTotal = Integer.parseInt(content);
            return mTotal;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /** 内存转换,B转MB **/
    public static String byteToMB(Context context, long size) {
        // String mb = Formatter.formatFileSize(context, size);
        if (size == 0) {
            return "0" + context.getResources().getString(R.string.main_size_danwei);
        }
        long mbSize = size / (1024 * 1024);
        return mbSize + context.getResources().getString(R.string.main_size_danwei);
    }
}
