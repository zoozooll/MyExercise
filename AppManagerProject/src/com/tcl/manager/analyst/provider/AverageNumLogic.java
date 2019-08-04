package com.tcl.manager.analyst.provider;

import java.util.Collection;
import java.util.HashMap;

import com.tcl.manager.analyst.entity.TimeGroupInfo;

/**
 * 平均次 数逻辑
 * 
 * @author jiaquan.huang
 **/
public class AverageNumLogic {
    /** 剔除App更新时间之前的数据,得到平均数 **/
    public static int getAverageNum(Collection<TimeGroupInfo> timeGroup, long appUpdateTime) {
        int num = 0;
        for (TimeGroupInfo time : timeGroup) {
            if (time.endTime > appUpdateTime) {
                num++;
            }
        }
        return num;
    }
}
