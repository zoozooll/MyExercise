package com.tcl.manager.statistic.frequency;

import java.util.List;
import com.tcl.framework.db.EntityManager;
import com.tcl.framework.db.EntityManagerFactory;
import com.tcl.framework.db.sqlite.Selector;
import com.tcl.framework.db.sqlite.WhereBuilder;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.util.Constant;

/**
 * 频率数据库操作
 * 
 * @author jiaquan.huang
 * 
 */
public class FrequencyDBHelper {
    private volatile static FrequencyDBHelper frequncyDBHelper;
    EntityManager<FrequencyColumn> entityManager;

    public static FrequencyDBHelper getInstance() {
        synchronized (FrequencyEntity.class) {
            if (frequncyDBHelper == null) {
                frequncyDBHelper = new FrequencyDBHelper();
            }
        }
        return frequncyDBHelper;
    }

    private FrequencyDBHelper() {
        entityManager = EntityManagerFactory.getInstance(ManagerApplication.sApplication, 2, Constant.DB_NAME, null, null).getEntityManager(FrequencyColumn.class, "frequncy");
    }

    /** 保存并删除时段重复的数据 **/
    public void saveOrUpdate(FrequencyColumn column) {
        deleteSameData(column.fromTime, column.endTime);
        entityManager.saveOrUpdate(column);
    }

    /** 查 与fromTime-endTime有交集的数据 **/
    public List<FrequencyColumn> select(long fromTime, long endTime) {
        Selector selector = Selector.create();
        WhereBuilder where = WhereBuilder.create("fromTime", "<=", endTime);
        where.and("endTime", ">=", fromTime);
        selector.where(where);
        List<FrequencyColumn> value = entityManager.findAll(selector);
        return value;
    }

    /** 查给定时段中最后一个分时段的频率数据 **/
    public FrequencyColumn getFirst(long fromTime, long endTime) {
        Selector selector = Selector.create();
        WhereBuilder where = WhereBuilder.create("fromTime", "<=", endTime);
        where.and("endTime", ">=", fromTime);
        selector.where(where);
        selector.orderBy("endTime", true);
        return entityManager.findFirst(selector);
    }

    /** 删与fromTime-endTime有并集的数据 **/
    public void deleteSameData(long fromTime, long endTime) {
        WhereBuilder where = WhereBuilder.create("fromTime", ">=", fromTime);
        where.and("endTime", "<=", endTime);
        entityManager.delete(where);
    }

    /** 清除开始时间是某点以前的数据 **/
    public void clean(long fromTime) {
        WhereBuilder where = WhereBuilder.create("fromTime", "<=", fromTime);
        entityManager.delete(where);
    }
}
