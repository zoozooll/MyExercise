/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep;

import java.util.ArrayDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.IInterface;
import android.util.Log;

/**
 * This is the base implementation of the service agent (the bridge with 
 * individual applications and services created using AIDL)
 */
public abstract class ServiceAgent<T extends IInterface> implements ServiceConnector.Callback {
	
	private final String TAG = "ServiceAgent";
	
	// Internal constants 
	protected final long MAX_DELAY_TIME = 200;
	protected final int MAX_THREAD_COUNT = 10;
	
	private ScheduledExecutorService nonBlockingExecutor =
		Executors.newScheduledThreadPool(MAX_THREAD_COUNT);
	
	private final Object mLock = new Object();
	private ArrayDeque<FutureTask<?>> mTasks = null;
	
	private ServiceConnector mConnector;
	
	/** The binder to the underlying service */
	private T mService;
	
	protected ServiceAgent(ServiceConnector connector) {
		mConnector = connector;
		mConnector.addCallback(this);
		mConnector.connect();
	}
	
	/**
	 * Adds a task to be executed when the agent is connected with the underlying 
	 * service
	 * 
	 * @param future the future task to be executed
	 */
	protected void addConnectedTasks(FutureTask<?> future) {
		synchronized (mLock) {
			if (isReady()) {
				try {
					ExecutorService executor = Executors.newSingleThreadExecutor();
					executor.execute(future);
				} catch (Exception ex) {
					Log.e(TAG, "Failed to execute task because " + ex);
				}
				
				return;
			}
			
			if (mTasks == null) {
				mTasks = new ArrayDeque<FutureTask<?>>();
			}
			
			if (future != null && !mTasks.contains(future)) {
				mTasks.add(future);
			}
		}
	}
	
	/**
	 * Returns the connector associated with the agent
	 * @return
	 */
	protected ServiceConnector getConnector() {
		return mConnector;
	}
	
	/**
	 * Returns whether or not the {@link ServiceAgent} is ready to serve 
	 * clients on behalf of the underlying service
	 * 
	 * @return
	 */
	public boolean isReady() {
		return mService != null && getConnector().isBounded();
	}
	
	/**
	 * Returns the {@link IInterface} to the remote object
	 * @return
	 */
	protected abstract T getInterface(); 
	
	@Override
	public void handleConnection(boolean connected) {
		if (connected) {
			synchronized (mLock) {
				mService = getInterface();
				
				try {
					// Execute tasks scheduled to run after the service is connected.
					// The underlying task list is a linked list, therefore iterating 
					// through the list will execute the scheduled tasks in the order
					// they are scheduled
					if (mTasks != null) {
						ExecutorService service = Executors.newCachedThreadPool();
						FutureTask<?> task = mTasks.poll();
						
						// Executes and removes task from the Deque
						while (task != null) {
							service.execute(task);
							task = mTasks.poll();
						}
					}
				} catch (Exception ex) {
					Log.e(TAG, "Failed to execute task after connection to the service is established because " + ex);
				}
			}
		} else {
			mService = null;
		}
	}
	
	/**
	 * Returns the interface to the underlying service
	 */
	protected T getService() {
		return mService;
	}
	
	/**
	 * Schedule a task to execute after a default delay period
	 *  
	 * @param task The task to execute
	 */
	protected void executeTaskDelayed(Runnable task) {
		executeTaskDelayed(task, MAX_DELAY_TIME);
	}
	
	/**
	 * Schedule a task to execute after a given delay
	 * 
	 * @param task The task to execute
	 * @param delay the time from now to execution in milliseconds
	 */
	protected void executeTaskDelayed(Runnable task, long delay) {
		try {
			nonBlockingExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			// The task cannot be scheduled for execution
			Log.e(TAG, task + " cannot be scheduled for execution");
		}
	}

}
