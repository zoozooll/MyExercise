package com.tcl.manager.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import android.content.Context;
import android.os.Build;

import com.tcl.framework.log.NLog;
import com.tcl.manager.pkgsize.ForceStop;

/**
 * @Description: adb命令
 * @author jiaquan.huang
 * @date 2014-11-28 下午2:33:23
 * @copyright TCL-MIE
 */
public class AdbCmdTool {

    public final static String ERROR = "error";
    public final static String SUCCESS = "success";
    private static boolean isSystemApp = false;

    public static void init(Context context) {
        isSystemApp = !PkgManagerTool.isUserApp(context, context.getPackageName());
    }

    /**
     * 强制关闭APP,清除和它相关的一切
     **/
    public static boolean forceStop(Context mContext, String packageName) {
        if (isSystemApp == true) {
            return ForceStop.forceStopPackage(mContext, packageName);
        } else {
            String cmd = "am force-stop " + packageName;
            String result = adbCmd(cmd);
            return SUCCESS.equals(result);
        }
    }

    /**
     * 冻结广播
     **/
    public static boolean disableReciver(String packageName, String reciverName) {
        String cmd = "pm disable " + packageName + "/" + reciverName;
        String result = adbCmd(cmd);
        return SUCCESS.equals(result);
    }

    /**
     * 解冻广播
     * 
     * @return
     **/
    public static boolean enableReciver(String packageName, String reciverName) {
        String cmd = "pm enable " + packageName + "/" + reciverName;
        String result = adbCmd(cmd);
        return SUCCESS.equals(result);
    }

    /**
     * 冻结APP
     * 
     * @return
     **/
    public static boolean disableApp(String packageName) {
        String cmd = "pm disable " + packageName;
        String result = adbCmd(cmd);
        return SUCCESS.equals(result);
    }

    /**
     * 解冻APP
     * 
     * @return
     **/
    public static boolean enableApp(String packageName) {
        String cmd = "pm enable " + packageName;
        String result = adbCmd(cmd);
        return SUCCESS.equals(result);
    }

    /** 修改文件读取权限 **/
    public static boolean modifyFilePermission(String filePath) {
        String cmd = "chmod 777 " + filePath;
        String result = adbCmd(cmd);
        return SUCCESS.equals(result);
    }

    /** 静默安装 **/
    public static boolean install(String apkPath) {
        String cmd = "pm install -r " + apkPath;
        Process process = cmd(cmd);
        InputStream successStream = process.getInputStream();
        String successString = streamToString(successStream);
        if (!isEmpty(successString)) {
            return true;
        }
        return false;
    }

    public static String streamToString(InputStream in) {
        InputStream inputStream = in;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i = -1;
            while ((i = inputStream.read()) != -1) {
                baos.write(i);
            }
            return baos.toString("UTF-8");
        } catch (IOException e) {
            return null;
        }
    }

    /** 执行命令,返回String型的结果 **/
    public static String adbCmd(String cmd) {
        Process process = null;
        try {
            process = cmd(cmd);
            if (process == null) {
                NLog.e("adb", "没有syetem权限");
                return ERROR;
            }
            InputStream errorStream = process.getErrorStream();
            String errorString = streamToString(errorStream).trim();
            if (!isEmpty(errorString)) {
                NLog.e("adb", errorString);
                errorStream.close();
                return ERROR;
            }
            errorStream.close();
            InputStream successStream = process.getInputStream();
            String successString = streamToString(successStream);
            if (isEmpty(successString)) {
                successStream.close();
                return SUCCESS;
            }
            successStream.close();
            return successString;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * 执行adb命令
     * 
     * @return
     * **/
    public static Process cmd(String cmd) {
        NLog.i("CMD", cmd);
        if (isSystemApp == false) {
            return cmdSU(cmd);
        }
        DataOutputStream dos = null;
        String adbCmd = cmd;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(adbCmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            return process;
        } catch (Exception e) {
            return process;
        } finally {
            try {
                if (dos != null)
                    dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行adb命令
     * 
     * @return
     * **/
    public static Process cmdSU(String cmd) {
        DataOutputStream dos = null;
        String adbCmd = cmd;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(process.getOutputStream());
            dos.writeBytes(adbCmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            return process;
        } catch (Exception e) {
            return process;
        } finally {
            try {
                if (dos != null)
                    dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 内置不需要执行su,直接执行adb命令
     * 
     * @param apkPath
     * @return
     */
    public static Process cmdWithoutSU(String... cmd) {
        NLog.i("CMD", Arrays.toString(cmd));
        ProcessBuilder processBuilder;
        Process process = null;
        try {
            processBuilder = new ProcessBuilder(cmd);
            process = processBuilder.start();
            process.waitFor();
            return process;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
