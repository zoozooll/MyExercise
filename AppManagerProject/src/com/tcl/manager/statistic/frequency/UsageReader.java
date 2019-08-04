package com.tcl.manager.statistic.frequency;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.tcl.framework.log.NLog;
import com.tcl.framework.util.FileUtils;
import com.tcl.manager.application.AMDirType;
import com.tcl.manager.application.AMDirectorManager;
import com.tcl.manager.statistic.frequency.UsageStats.PkgUsageStatsExtended;
import com.tcl.manager.util.AdbCmdTool;
import android.content.Context;
import android.text.TextUtils;

/**
 * @Description:通过读取文件，获取程序运行次数与时间
 * @author jiaquan.huang
 * @date 2014-12-8 上午10:23:47
 * @copyright TCL-MIE
 */

public class UsageReader {
    private static volatile UsageReader info = null;
    private static String TAG = "appUsedInfo类:";
    private String sysoFilePath = "/data/system/usagestats";
    private String prefix = "usage-";
    private String savePathPrefix = null;
    private UsageStats usageStats = new UsageStats();
    private Map<String, Map<String, PkgUsageStatsExtended>> data = new HashMap<String, Map<String, PkgUsageStatsExtended>>();

    private UsageReader(Context context) {
        String loggerPath = AMDirectorManager.getInstance().getDirectoryPath(AMDirType.log);
        savePathPrefix = loggerPath;
    }

    public static UsageReader getInstance(Context context) {
        synchronized (UsageReader.class) {
            if (null == info) {
                info = new UsageReader(context);
            }
        }
        return info;
    }

 

    /** 获取文件夹下面的目录 **/
    private String getFileNameCatlog(String filePath) {
        String ls = "ls " + filePath;
        String result = AdbCmdTool.adbCmd(ls);
        return result;
    }

    /** 解析文件的名字 **/
    private String[] parseFileName(String data) {
        String[] result = data.split("\n");
        return result;
    }

    /** 移动文件的 **/
    private boolean moveFileToSD(String fromAbsolutePath, String toAbsolutePath) {
        String cmd = "cat " + fromAbsolutePath + " > " + toAbsolutePath;// 移动文件
        String result = AdbCmdTool.adbCmd(cmd);
        return AdbCmdTool.SUCCESS.equals(result);
    }

    /** 获取指定文件的数据 **/
    private Map<String, PkgUsageStatsExtended> getUserStats(String absolutePath) {
        return usageStats.getUserStats(absolutePath);
    }

    private String generateSavedPath(String fileName) {
        String tempToPath = savePathPrefix + "/" + fileName;
        return tempToPath;
    }

    private String generateSysPath(String fileName) {
        String tempToPath = sysoFilePath + "/" + fileName;
        return tempToPath;
    }

    /** 修改文件读写权限 **/
    private void modifyPermission(String[] saveFileName) {
        for (String name : saveFileName) {
            if (!"".equals(name) && null != name) {
                String path = generateSavedPath(name);
                AdbCmdTool.modifyFilePermission(path);
            }
        }

    }

    /** 生成日期 **/
    private String getTime(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return ymdToString(year, month, day);
    }

    /** 日期时间 转换成标准位数 **/
    private String ymdToString(int year, int month, int day) {
        StringBuffer time = new StringBuffer();
        time.append(year);
        month = month + 1;
        if (month < 10) {
            time.append("0" + month);
        } else {
            time.append((month));
        }
        if (day < 10) {
            time.append(("0" + day));
        } else {
            time.append(day);
        }
        return time.toString();
    }

    /** 取某天的 **/
    private String filterByDay(String[] fileName, long milliseconds) {
        String name = prefix + getTime(milliseconds);
        for (String tempName : fileName) {
            if (name.equals(tempName)) {
                return tempName;
            }
        }
        return "";
    }

    private File createFiles(String path) {
        File file = new File(path);
        if (!file.exists()) {
            boolean success = file.mkdirs();
            if (success == false) {
                return null;
            }
        }
        return file;
    }

    /** 运行过程，获取某天的信息 **/
    private Map<String, PkgUsageStatsExtended> read(long milliseconds) {
        String fromPath = null;
        String toPath = null;
        try {
            File f = createFiles(savePathPrefix);
            if (f == null) {
                return new HashMap<String, UsageStats.PkgUsageStatsExtended>();
            }
            String fileNameCatalog = getFileNameCatlog(sysoFilePath);
            String[] fileName = parseFileName(fileNameCatalog);
            String newFile = filterByDay(fileName, milliseconds);
            if (TextUtils.isEmpty(newFile)) {
                return new HashMap<String, UsageStats.PkgUsageStatsExtended>();
            }
            fromPath = sysoFilePath + "/" + newFile;
            toPath = savePathPrefix + "/" + newFile;
            moveFileToSD(fromPath, toPath);
            // AdbCmdTool.modifyFilePermission(toPath);
            Map<String, PkgUsageStatsExtended> map = getUserStats(toPath);
            return map;
        } catch (Exception e) {
            NLog.e(TAG, e);
            return new HashMap<String, UsageStats.PkgUsageStatsExtended>();
        } finally {
            if (null != toPath) {
                FileUtils.deleteFile(toPath);
            }
        }
    }

    /**
     * @param time某天的milliseconds
     * @return 格式Map<包名，PkgUsageStatsExtended>
     **/
    public synchronized Map<String, PkgUsageStatsExtended> readByTime(long milliseconds) {
        return read(milliseconds);
    }

}
