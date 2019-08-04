package com.tcl.manager.update;

/**
 * @Description: loading
 * @author jiaquan.huang
 * @date 2014-12-13 下午8:28:50
 * @copyright TCL-MIE
 */
public interface ILoadingListener {
    public void notify(int totalSize, int tempSize, int schedule);

    public void downloadSuccess();

    public void downloadError(int schedule);
}
