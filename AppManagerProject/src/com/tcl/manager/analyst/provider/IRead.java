package com.tcl.manager.analyst.provider;

/**
 * 读取基础数据
 * 
 * @author jiaquan.huang
 * 
 */
public interface IRead {
    public <T> T read(long fromTime, long endTime);
}
