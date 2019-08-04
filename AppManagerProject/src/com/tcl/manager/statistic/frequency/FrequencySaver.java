package com.tcl.manager.statistic.frequency;

import java.util.Calendar;
import java.util.List;
import android.content.Context;

/**
 * 频率保存
 * 
 * @author jiaquan.huang
 * 
 */
public class FrequencySaver {
    Context mContext;
    static volatile FrequencySaver saver = null;

    private FrequencySaver(Context context) {
        mContext = context;
    }

    public static FrequencySaver getInstance(Context context) {
        synchronized (FrequencySaver.class) {
            if (saver == null) {
                saver = new FrequencySaver(context);
            }
        }
        return saver;
    }

    public void saveFrequency() {
        /** 取“相对一天”的数据 **/
        List<FrequencyEntity> infos = EveryDayUseInfoPrivoder.provide(mContext);
        if (infos.isEmpty()) {
            return;
        }
        /** 整合 **/
        FrequencyColumn column = new FrequencyColumn();
        long fromTime = 0;
        long endTime = 0;
        for (FrequencyEntity info : infos) {
            /** 取子项中开始和结束时间最大的作为整个集合的时间段 **/
            if (info.fromTime > fromTime) {
                fromTime = info.fromTime;
            }
            if (info.endTime > endTime) {
                endTime = info.endTime;
            }
        }
        column.fromTime = fromTime;
        column.endTime = endTime;
        column.frequencyEntity = infos;
        /** 存 **/
        FrequencyDBHelper.getInstance().saveOrUpdate(column);

    }

    public void clean() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -30);
        FrequencyDBHelper.getInstance().clean(c.getTimeInMillis());
    }
}
