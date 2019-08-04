package com.tcl.manager.util;

import java.util.Calendar;

public class TimeUtil {

    /** 获取今天的日期 **/
    public static String getTodayCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return ymdToString(year, month, day);
    }

    /** 日期时间 转换成标准位数 **/
    private static String ymdToString(int year, int month, int day) {
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
}
