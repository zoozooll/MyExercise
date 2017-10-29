
package com.iskyinfor.duoduo.ui.custom.page;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ����DiscardOldestPolicy���Ե��̳߳���
 * 
 * @author pKF29007
 */
public class DiscardOldestThreadPool extends ThreadPoolExecutor {
    public static final String TAG = "DiscardOldestThreadPool";

    /**
     * �����̳߳صĴ�С
     */
    public static final int CORE_POOL_SIZE = 1;

    /**
     * ���д�С
     */
    public static final int QUEUE_SIZE = 1;

    /**
     * ����ͬʱִ�е��߳�����?
     */
    public static final int MAXIMUM_POOL_SIZE = 2;

    /**
     * ����CORE_POOL_SIZE���̵߳Ĵ���ʱ�䣬�����ʱ���̳߳ػ��Զ����ո��߳�?
     */
    public static final long KEEP_ALIVE_TIME = 2000;

    /**
     * �̳߳�ʵ�����?
     */
    private static DiscardOldestThreadPool instance = null;

    /**
     * ���Թ�������
     */
    private static LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(
            QUEUE_SIZE);

    /**
     * �̳߳ز���
     */
    private static ThreadPoolExecutor.DiscardOldestPolicy policy = new ThreadPoolExecutor.DiscardOldestPolicy();

    /**
     * ��ȡ�̳߳�ʵ��
     * 
     * @return
     */
    synchronized static public DiscardOldestThreadPool getInstance() {

//        HWLog.d(HWLog.HWTAG, TAG + ":getInstance()");
        if (instance == null) {
            instance = new DiscardOldestThreadPool(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, policy);
        }
        return instance;
    }

    private DiscardOldestThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
//        HWLog.d(HWLog.HWTAG, TAG + ":DiscardOldestThreadPool()");
    }

    /**
     * ����̳߳�?
     */
    public static void releaseThreadPool() {

//        HWLog.d(HWLog.HWTAG, TAG + ":releaseThreadPool()");
        if (instance != null && !instance.isShutdown()) {
            instance.shutdown();
        }
    }
}
