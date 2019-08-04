/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.internal;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

/**
 * Checks whether or not system volume exceeded defined threshold
 */
public final class VolumeManager {
	
	private static final String TAG = VolumeManager.class.getSimpleName();
	
	/**
	 * Broadcasted when the system volume reached threshold. Includes the threshold value reached
	 * 
	 * @see #EXTRA
	 */
	public static final String VOLUME_THRESHOLD_REACHED_ACTION = "com.oregonscientific.meep.home.VOLUME_THREASHOLD_REACHED_ACTION";
	
	/**
	 * The threshold reached
	 */
	public static final String EXTRA_VOLUME_THRESHOLD = "com.oregonscientific.meep.home.VOLUME_THRESHOLD";
	
	/**
	 * Broadcasted by {@link AudioManager} when the master volume changes.
	 * Includes the new volume. It should have been "android.media.VOLUME_CHANGED_ACTION"
	 * 
	 * @see #EXTRA_MASTER_VOLUME_VALUE
	 */
	public static final String VOLUME_CHANGED_ACTION = "com.osgd.meep.MeepMessage";

	/**
	 * The volume associated with the stream for the volume changed intent. If
	 * the action was "android.media.VOLUME_CHANGED_ACTION", the extra should have
	 * been "android.media.EXTRA_VOLUME_STREAM_VALUE"
	 */
	public static final String EXTRA_VOLUME_STREAM_VALUE = "volume";
	
	/**
	 * The previous volume associated with the stream for the volume changed
	 * intent.
	 */
	public static final String EXTRA_PREV_VOLUME_STREAM_VALUE = "android.media.EXTRA_PREV_VOLUME_STREAM_VALUE";
	
	private final long RETRY_INTERVAL = 60000;
	
	private static final int THRESHOLD_MAX = 10;
	
	private static final int THRESHOLD_MIN = 0;
	
	/** Location of the large volume description file */
	private static final String FILE_LVD = ".LVD";
	
	/**
	 * Indicates that the system volume stream is within the defined threshold
	 */
	public static final int VOLUME_OK = 0;
	
	/**
	 * Indicates that the system volume stream is too high
	 */
	public static final int VOLUME_TOO_HIGH = 1;
	
	/**
	 * Indicates that the system volume stream is too low
	 */
	public static final int VOLUME_TOO_LOW = 2;
	
	private final Context mContext;
	private CheckerTask mTask = null;
	
	private int mLastCheckedVolumeLevel = -1;
	
	/**
	 * Construct a new VolumeChecker
	 * 
	 * @param context the context that this checker runs in
	 */
	public VolumeManager(Context context) {
		mContext = context;
	}
	
	/**
	 * Checks whether or not system volume exceeded the defined threshold 
	 * 
	 * @param volume the current volume level
	 */
	public void assertVolumeStreamIsWithinThreshold(int volume) {
		volume = Math.min(THRESHOLD_MAX, volume);
		volume = Math.max(THRESHOLD_MIN, volume);
		int result = isVolumeExceededThreshold(volume);
		
		switch (result) {
		case VOLUME_OK:
			// Remove LVD file
			deleteLargeVolumeDescriptionFile();
			break;
		case VOLUME_TOO_HIGH:
			// Create LVD file and display dialog
			if (mLastCheckedVolumeLevel != volume) {
				createLargeVolumeDescriptionFile();
			}
			break;
		case VOLUME_TOO_LOW:
			// Create LVD file and display dialog
			if (mLastCheckedVolumeLevel != volume) {
				createLargeVolumeDescriptionFile();
			}
			break;
		}
		
		if (mContext != null && mLastCheckedVolumeLevel != volume) {
			Intent intent = new Intent();
			intent.setAction(VOLUME_THRESHOLD_REACHED_ACTION);
			intent.putExtra(EXTRA_VOLUME_THRESHOLD, result);
			mContext.sendBroadcast(intent);
		}
		mLastCheckedVolumeLevel = volume;
	}
	
	/**
	 * Schedule to assert volume level after a period of time 
	 * 
	 * @param period the period between successive check
	 */
	public void assertVolumeLevelAtFixedRate(long period) {
		if (mTask == null) {
			mTask = new CheckerTask();
		}
		
		mTask.schedule(period, true);
		deleteLargeVolumeDescriptionFile();
	}
	
	/**
	 * Stops asserting volume level
	 */
	public void stopAssert() {
		if (mTask != null) {
			mTask.cancel();
		}
	}
	
	private void createLargeVolumeDescriptionFile() {
		try {
			File file = new File(mContext.getFilesDir(), FILE_LVD);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException ex) {
			Log.e(TAG, "Cannot create LVD file...");
		}	
	}
	
	private void deleteLargeVolumeDescriptionFile() {
		try {
			File file = new File(mContext.getFilesDir(), FILE_LVD);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot delete LVD file...");
		}
		
	}
	
	/**
	 * Determines whether or not the given volume level exceeded the defined threshold
	 *  
	 * @param volume the current volume level
	 * @return {@link} #VOLUME_TOO_HIGH} if the volume exceeded the maximum threshold, 
	 * {@link #VOLUME_TOO_LOW} if volume is lower than the minimum threshold, 
	 * {@link #VOLUME_OK} if the volume level is within the minimum and maximum threshold
	 */
	private int isVolumeExceededThreshold(int volume) {
		Log.d(TAG, "Volume: " + volume);
		int result = VOLUME_OK;
		
		if (volume >= THRESHOLD_MAX) {
			result = VOLUME_TOO_HIGH;
		}
		
		return result;
	}
	
	/**
	 * The task that periodically checks the device volume level to determine whether
	 * or not the current volume level exceeded the thresholds.
	 */
	private final class CheckerTask {
		
		/** Scheduler */
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle;
		
		/** 
		 * Check current running processes with permission, and update the permission table if needed.
		 */
		private final Runnable checker = new Runnable() {

			@Override
			public void run() {
				// We cannot continue to run if there is not running context
				if (mContext == null) {
					return;
				}
				
				// Retrieve the current volume level
				AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
				int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
				Log.d(TAG, "Current volume level: " + volume);
				
				// Reset the last checked volume level
				mLastCheckedVolumeLevel = -1;
				assertVolumeStreamIsWithinThreshold(volume);
			}
			
		};
		
		/**
		 * Schedule periodic volume checker 
		 * 
		 * @param period The time from now to delay execution
		 * @param autoReschedule true to re-schedule the task if scheduling failed
		 */
		public void schedule(final long period, boolean autoReschedule) {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
			
			try {
				handle = scheduler.scheduleWithFixedDelay(
						checker, 
						period, 
						period,
						TimeUnit.MILLISECONDS);
			} catch (Exception ex) {
				// The task cannot be scheduled
				Log.e(TAG, "Volume checker cannot be scheduled to run because " + ex.getMessage());
				
				if (autoReschedule) {
					// Try to reschedule
					ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
					service.schedule(new Runnable() {

						@Override
						public void run() {
							schedule(period, true);
						}
						
					}, RETRY_INTERVAL, TimeUnit.MILLISECONDS);
				}
			}
		}
		
		/**
		 * Cancel execution of the task. If the task was already running, it will be interrupted
		 */
		public void cancel() {
			if (handle != null) {
				handle.cancel(true);
				handle = null;
			}
		}
	}

}
