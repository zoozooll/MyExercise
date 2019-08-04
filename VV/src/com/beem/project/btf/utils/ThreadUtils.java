package com.beem.project.btf.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ThreadUtils
 * @Description:线程工具类
 * @author: yuedong bao
 * @date: 2015-7-31 上午10:43:04
 */
public class ThreadUtils {
	public static final ExecutorService fixedExec = Executors
			.newFixedThreadPool(5, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable runnable) {
					Thread thread = new Thread(runnable,
							"ThreadUtils_fixedExec");
					return thread;
				}
			});
	private static final ScheduledExecutorService scheduExec = Executors
			.newScheduledThreadPool(3, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable runnable) {
					Thread thread = new Thread(runnable,
							"ThreadUtils_scheduExec");
					return thread;
				}
			});

	/**
	 * @Title: splitTaksk
	 * @Description:将线程中的一个大任务拆分成几个小任务并行执行,注意：此方法会阻塞所调用的线程
	 * @param: @param threadPoolsize
	 * @param: @param runnable
	 * @return: void
	 * @throws:
	 */
	public static void executeDivideTasks(List<Runnable> runnables) {
		Object lock = new Object();
		Map<String, Boolean> resultMap = new HashMap<String, Boolean>();
		if (runnables != null && !runnables.isEmpty()) {
			for (int i = 0; i < runnables.size(); i++) {
				fixedExec.submit(new InnerRunnalbe(runnables.get(i), runnables
						.size(), lock, String.valueOf(i), resultMap));
			}
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * @Title: executeTask
	 * @Description: 向线程池提交一个任务
	 * @param: @param runnables
	 * @return: void
	 * @throws:
	 */
	public static void executeTask(Runnable... runnables) {
		for (Runnable runnable : runnables) {
			fixedExec.submit(runnable);
		}
	}

	/**
	 * @ClassName: InnerRunnalbe
	 * @Description: Runnable的代理类，内部执行线程，执行完所有任务后通知释放锁
	 * @author: yuedong bao
	 * @date: 2015-7-31 上午10:55:12
	 */
	private static class InnerRunnalbe implements Runnable {
		private final Runnable task;
		private final int taskSize;
		private final Object lock;
		private final Map<String, Boolean> reslutMap;
		private final String taskName;

		public InnerRunnalbe(Runnable task, int taskSize, Object lock,
				String taskName, Map<String, Boolean> reslutMap) {
			super();
			this.task = task;
			this.taskSize = taskSize;
			this.lock = lock;
			this.taskName = taskName;
			this.reslutMap = reslutMap;
		}
		@Override
		public void run() {
			try {
				task.run();
				synchronized (reslutMap) {
					reslutMap.put(taskName, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				synchronized (reslutMap) {
					reslutMap.put(taskName, false);
				}
			} finally {
				if (reslutMap.size() == taskSize) {
					synchronized (lock) {
						lock.notify();
					}
				}
			}
		}
	}

	/**
	 * 执行一次
	 * @Title: addDelayTask
	 * @param @param autoTask
	 * @param @param delay
	 * @return void
	 * @throws
	 */
	public static void addDelayTask(AutoTask autoTask, int delayMills) {
		ScheduledFuture<?> scheduledFuture = scheduExec.schedule(autoTask,
				delayMills, TimeUnit.MILLISECONDS);
		autoTask.setScheduledFuture(scheduledFuture);
	}
	/**
	 * 循环
	 * @Title: addRateTask
	 * @param @param autoTask
	 * @param @param period
	 * @return void
	 * @throws
	 */
	public static void addRateTask(AutoTask autoTask, int period) {
		ScheduledFuture<?> scheduledFuture = scheduExec.scheduleAtFixedRate(
				autoTask, 0, period, TimeUnit.MILLISECONDS);
		autoTask.setScheduledFuture(scheduledFuture);
	}
	/**
	 * 延迟多久执行
	 * @Title: addRateTask
	 * @param @param autoTask
	 * @param @param initialDelay
	 * @param @param period
	 * @return void
	 * @throws
	 */
	public static void addRateTask(AutoTask autoTask, int initialDelay,
			int period) {
		ScheduledFuture<?> scheduledFuture = scheduExec.scheduleAtFixedRate(
				autoTask, initialDelay, period, TimeUnit.MILLISECONDS);
		autoTask.setScheduledFuture(scheduledFuture);
	}

	public static abstract class AutoTask implements Runnable {
		private ScheduledFuture<?> scheduledFuture;

		public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
			this.scheduledFuture = scheduledFuture;
		}
		public abstract void onCancel();
		/**
		 * Returns true if this task completed. Completion may be due to normal termination, an
		 * exception, or cancellation -- in all of these cases, this method will return true.
		 * @Title: isDone
		 * @param @return
		 * @return boolean
		 * @throws
		 */
		public boolean isDone() {
			if (scheduledFuture != null) {
				return scheduledFuture.isDone();
			}
			return true;
		}
		public boolean isCancelled() {
			if (scheduledFuture != null) {
				scheduledFuture.isCancelled();
			}
			return true;
		}
		/**
		 * 关闭任务
		 * @Title: stop
		 * @param @param mayInterruptIfRunning true 正在执行的中断,false等待完成
		 * @return void
		 * @throws
		 */
		public void stop(boolean mayInterruptIfRunning) {
			if (scheduledFuture != null) {
				scheduledFuture.cancel(mayInterruptIfRunning);
				onCancel();
			}
			scheduledFuture = null;
		}
	}

	public static void main(String[] args) {
	}
}
