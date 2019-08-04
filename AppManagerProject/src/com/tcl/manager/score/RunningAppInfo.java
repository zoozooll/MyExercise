package com.tcl.manager.score;

import com.tcl.manager.base.BaseInfo;
/**
 * Application Information
 * 
 * @author jiaquan.huang
 */
public class RunningAppInfo extends BaseInfo {
    private static final long serialVersionUID = -7247491501596822671L;
    protected String appName;// 程序名
    private long memsize;// 内存大小
    private int pid;// 进程pid

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}