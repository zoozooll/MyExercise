package com.tcl.manager.analyst;

import java.util.List;
import com.tcl.framework.db.EntityManager;
import com.tcl.framework.db.EntityManagerFactory;
import com.tcl.framework.db.sqlite.Selector;
import com.tcl.framework.db.sqlite.WhereBuilder;
import com.tcl.manager.analyst.entity.AppInfoBuffer;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.util.Constant;

/**
 * 缓存操作
 * 
 * @author jiaquan.huang
 * 
 */
public class CacheDBHelper {
    private volatile static CacheDBHelper frequncyDBHelper;
    EntityManager<AppInfoBuffer> entityManager;

    public static CacheDBHelper getInstance() {
        synchronized (CacheDBHelper.class) {
            if (frequncyDBHelper == null) {
                frequncyDBHelper = new CacheDBHelper();
            }
        }
        return frequncyDBHelper;
    }

    private CacheDBHelper() {
        entityManager = EntityManagerFactory.getInstance(ManagerApplication.sApplication, 2, Constant.DB_NAME, null, null).getEntityManager(AppInfoBuffer.class, "cache");
    }

    /** 保存并删除时段重复的数据 **/
    public void saveOrUpdate(AppInfoBuffer appBuffer) {
        entityManager.deleteAll();
        entityManager.saveOrUpdate(appBuffer);
    }

    public AppInfoBuffer getCache() {
        List<AppInfoBuffer> cache = entityManager.findAll();
        AppInfoBuffer buffer = null;
        if (cache != null) {
            if (cache.isEmpty() == false) {
                buffer = cache.get(0);
            }
        }
        return buffer;
    }

}
