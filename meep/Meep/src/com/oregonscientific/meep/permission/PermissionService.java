/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.util.DateUtils;

/**
 * PermissionService enforces permission settings and handles permission 
 * operation requests. 
 * 
 * <p>The service periodically scan the running activities to determine whether
 * or not the device permission settings are violated. Authenticated user
 * can access and update device permission settings. Further, permission 
 * settings are periodically synchronized with MEEP server.</p>
 * 
 * <p>Applications should verify with the service to determine whether or not
 * access to external resources e.g. web page, YouTube video, etc. is granted.
 * </p> 
 */
public class PermissionService extends DatabaseService<PermissionDatabaseHelper> {

	private final String TAG = "PermissionService";
	
	private final long PERMISSION_ENFORCEMENT_INTERVAL = 30000;
	private final long PERMISSION_KILL_PROCESS_DELAY = 900;
	
	private final String ACTIVITY_HOME = "com.oregonscientific.meep.home/.HomeActivity";
	
	/** The underlying task enforces permission settings */
	private EnforcePermissionTask mPermissionTask;
	
	/** The actual logic implementation */
	private PermissionHandler mHandler;
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mPermissionTask = new EnforcePermissionTask();
		mPermissionTask.schedule(PERMISSION_ENFORCEMENT_INTERVAL, true);
		
		mHandler = new PermissionHandler(this, getHelper());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
		
		// Stops the permission enforcer task
		if (mPermissionTask != null) {
			mPermissionTask.cancel();
		}
	}
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		// Server messages will be inside the intent's extras
		handleCommand(intent);
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}
	
	@Override
	public void onLowMemory() {
		// Clears cached objects in the handler
		if (mHandler != null) {
			mHandler.clearCache();
		}
	}
	
	/**
	 * Handles the command as specified in the {@code} intent 
	 * 
	 * @param intent The intent supplied to {@link #onStartCommand(Intent, int, int)}
	 */
	private void handleCommand(Intent intent) {
		// Quick return if there is nothing to process
		if (intent == null)
			return;

		// Only handles messages received from MessageService
		if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			// retrieve extended data (added with putExtra()) from the intent
			Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
			mHandler.handleMessage(message);
		}
			
	}

	/**
	 * This is the object that receives interactions from clients.
	 */
	private final IPermissionService.Stub mBinder = new IPermissionService.Stub () {
		
		/**
		 * Determines whether or not the given user is allowed to access the {@code url} specified
		 * 
		 * @param userId the user to determine whether s/he is allowed to access the {@code url}
		 * @param url a string representation of the URL to determine whether or not it is accessible
		 * @return {@code true} if the URL is accessible by the user, {@code false} otherwise
		 */
		@Override
		public boolean isUrlAccessible(String user, String url) throws RemoteException {
			return mHandler.isUrlAccessible(user, url);
		}
		
		/**
		 * Determines whether or not the given user requires parent approval in order
		 * to make purchases in store
		 * 
		 * @param user the user to determine whether s/he can make purchases on store. May not be {@code null}
		 * @return {@code true} if parent approval is required, {@code false} otherwise.
		 */
		@Override
		public boolean requireApprovalToMakePurchases(String user) throws RemoteException {
			return mHandler.requireApprovalToMakePurchases(user);
		}
		
		/**
		 * Determines whether or not the given user can make purchases on store
		 * 
		 * @param user the user to determine whether s/he can make purchases on store. May not be {@code null}
		 * @return {@code true} if the user is allowed to make purchases in store directly, {@code false} otherwise.
		 */
		@Override
		public boolean canMakePurchases(String user) throws RemoteException {
			return mHandler.canMakePurchases(user);
		}
		
		/**
		 * Retrieves application permission settings for the given user
		 * 
		 * @param identity
		 *            The {@link Identity} to authenticate the function call
		 * @param user
		 *            returns permission settings for this user. his should be
		 *            the tag that uniquely identifies the user
		 */
		@Override
		public List<Permission> getAccessSchedule(Identity identity, String user) throws RemoteException {
			return mHandler.getAccessSchedule(identity, user);
		}

		/**
		 * Synchronize local blacklist in database with server's blacklist
		 * 
		 * @param user
		 *            The user whose blacklist to synchronize with server. This
		 *            should be the tag that uniquely identifies the user
		 * @param type
		 *            The type of the blacklist
		 * @param locale
		 *            The locale of the blacklist
		 */
		@Override
		public void syncBlacklist(String user, String type, String locale) throws RemoteException {
			mHandler.syncBlacklist(user, type, locale);
		}

		/**
		 * Synchronize access schedule in database with server's access
		 * schedules
		 * 
		 * @param user
		 *            The user whose access schedule to synchronize with server.
		 *            This should be the tag that uniquely identifies the user
		 */
		@Override
		public void syncAccessSchedule(String user) throws RemoteException {
			mHandler.syncAccessSchedule(user);
		}

		/**
		 * Check the item if it is in a specific blacklist
		 * 
		 * @param user The user's blacklist to check against with. This should be the tag 
		 * that uniquely identifies the user
		 * @param listType The type of the blacklist
		 * @param item The item that used to compare with blacklist
		 * @param locale The {@link Locale} of the item
		 * @return true if the blacklist contains the item, false if it doesn't
		 */
		@Override
		public boolean isItemInBlacklist(String user, String listType, String item, Locale locale) throws RemoteException {
			return mHandler.isItemInBlacklist(user, listType, item, locale);
		}

		/**
		 * Check the word if it is in the badword list
		 * 
		 * @param user The user's bad word list to check against with. This should be the
		 * tag that uniquely identifies the user
		 * @param word The word that used to compare with the bad word list
		 * @return true if the bad word list contains the word, false if otherwise
		 */
		@Override
		public boolean isBadword(String user, String word) throws RemoteException {
			return mHandler.isBadword(user, word);
		}

		/**
		 * Determines whether or not the given component is accessible
		 * 
		 * @param user The user whose access schedule to check against with. This should 
		 * be the tag that uniquely identifies the user
		 * @param component The component name to check
		 * @return true if the component of the user is accessible, false if otherwise
		 */
		@Override
		public int isAccessible(String user, ComponentName component) throws RemoteException {
			return mHandler.isAccessible(user, component);
		}

		/**
		 * Replace the permissions for each package. All existing entrie in the local
		 * data store will be removed and replaced with {@code permissions}
		 * 
		 * <h2>Note</h2> The operation can be lock access to the underlying data store
		 * for a long period of time if the list is large. Use this function is caution
		 * 
		 * @param identity The Identity to authenticate the function call
		 * @param user The user the permission schedule to apply to. This should be the 
		 * tag that uniquely identifies the user
		 * @param permissions An list of Permission objects that contains every package's permission  
		 */
		@Override
		public void setAccessSchedule(Identity identity, String user, List<Permission> permissions) throws RemoteException {
			mHandler.setAccessSchedule(identity, user, permissions);
			
		}
		/**
		 * Update the given permission for the given user
		 * 
		 * @param identity The Identity to authenticate the function call
		 * @param user The user to set permission against. This should be the tag that uniquely identifies the user
		 * @param permission The permission to update
		 */
		@Override
		public void updatePermission(Identity identity, String user, Permission permission) throws RemoteException {
			mHandler.updatePermission(identity, user, permission);
			
		}

		/**
		 * Replace the blacklist in local data store with {@code blacklist}. All existing
		 * entries in the local data store will be removed and replaced with {@code blacklist}
		 * 
		 * <h2>Note</h2> The operation can be lock access to the underlying data store
		 * for a long period of time if the list is large. Use this function is caution
		 * 
		 * @param identity The Identity to authenticate the function call
		 * @param user The user's blacklist to replace. This should be the tag that uniquely identifies the user
		 * @param blacklists An list of Blacklist Objects which is the entire blacklist
		 */
		@Override
		public void setBlacklist(Identity identity, String user, List<Blacklist> blacklists) throws RemoteException {
			mHandler.setBlacklist(identity, user, blacklists);
		}

		/**
		 * Add a blacklist item into the local blacklist
		 * 
		 * @param identity The Identity to authenticate the function call
		 * @param user The user whose blacklist to apply to. This should be the tag that uniquely identifies the user
		 * @param blacklistItem A blacklist item to be added into the local blacklist
		 * @return true if the item is successfully added to local store, false
		 * if the item cannot be added or the item already exist in the data
		 * store
		 */
		@Override
		public boolean addBlacklistItem(Identity identity, String user, Blacklist blacklistItem)throws RemoteException  {
			return mHandler.addBlacklistItem(identity, user, blacklistItem);

		}

		/**
		 * Remove blacklist item from the local blacklist
		 * 
		 * @param identity The Identity to authenticate the function call
		 * @param user The user whose blacklist to apply to. This should be the tag that uniquely identifies the user
		 * @param blacklistItem A blacklist item to be removed from the local blacklist
		 * @return true if the item is successfully removed from local store, false
		 * if the item cannot be removed or the item does not exist in the data
		 * store
		 */
		@Override
		public boolean removeBlacklistItem(Identity identity, String user, Blacklist blacklistItem) throws RemoteException {
			return mHandler.removeBlacklistItem(identity, user, blacklistItem);
			
		}

		/**
		 * Determine whether or not the given character sequence contains a bad
		 * word as defined in the {@code user}'s bad word list
		 * 
		 * @param user The user whose bad word list to check against. This should be the 
		 * tag that uniquely identifies the user
		 * @param str The character sequence to verify
		 */
		@Override
		public boolean containsBadword(String user, String str) throws RemoteException {
			return mHandler.containsBadword(user, str);
			
		}

		/**
		 * Retreive the Last launched package from the History table
		 * 
		 * @param user The user's most recently ran app to return. This should be the tag 
		 * that uniquely identifies the user
		 * @return A Component object contain package names and other information
		 */
		@Override
		public Component getMostRecentApp(String user) throws RemoteException {
			return mHandler.getMostRecentApp(user);
		}
		
		/**
		 * Replaces all occurences of bad word in the given {@code str} with {@code replacement}
		 * 
		 * @param user the user whose bad word list to use
		 * @param str the string to replace check for bad words
		 * @param replacement the string to replace with for each occurrence of the bad word defined
		 */
		@Override
		public String replaceBadwords(String user, String str, String replacement) throws RemoteException {
			return mHandler.replaceBadwords(user, str, replacement);
		}
		
		/**
		 * Returns a list of {@link Component} in the {@link Category} identified by {@code categoryName}
		 * 
		 * @param user the user whose component list to return
		 * @param categoryName the name of the category
		 */
		@Override
		public List<Component> getComponents(String user, String categoryName) throws RemoteException {
			return mHandler.getComponents(user, categoryName);
		}
		
		/**
		 * Clears run history of the given application component
		 *
		 * @param identity The Identity to authenticate the function call
		 * @param user the user to clear run history
		 * @param component the application component to clear run history
		 */
		@Override
		public void clearRunHistory(Identity identity, String user, ComponentName component) throws RemoteException {
			mHandler.clearRunHistory(identity, user, component);
		}
		
		/**
		 * Clears all run histories for the given user
		 * 
		 * @param identity The Identity to authenticate the function call
		 * @param user the user to clear run history
		 */
		public void clearRunHistories(Identity identity, String user) throws RemoteException {
			mHandler.clearRunHistories(identity, user);
		}

	};
	
	/**
	 * The task that periodically checks the running foreground tasks against the permission settings. If
	 * any of the foreground running tasks violated permission settings, it will try to kill the task and
	 * broadcast a message indicating that a task has violated permission settings
	 */
	private final class EnforcePermissionTask {
		private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		private ScheduledFuture<?> handle;
		
		private long period;
		
		/** 
		 * Get applications that currently running in foreground 
		 * 
		 * @return a List of package names of applications in foreground 
		 */
		private Map<String, Integer> getForegroundRunningProcesses() {
			Map<String, Integer> componentNames = null;
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
			
			if (runningProcesses != null && runningProcesses.size() > 0) {

				componentNames = new HashMap<String, Integer>();
				for (RunningAppProcessInfo rapi : runningProcesses) {
					switch (rapi.importance) {
					case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:
						componentNames.put(rapi.processName, Integer.valueOf(rapi.pid));
						Log.i(TAG, "Running: " + rapi.processName);
					}
				}
			}
			
			if (componentNames != null) {
				Log.d(TAG, "Number of foreground processes : " + Integer.toString(componentNames.size()));
			}
			return  componentNames;
		}
		
		private void killPackage(final String packageName, final int pid, int reason) {
			// Starts the default launcher and indicates to it that it was
			// started because a running application violated permission
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.setComponent(ComponentName.unflattenFromString(ACTIVITY_HOME));
			startMain.addCategory(Intent.CATEGORY_LAUNCHER);
			startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startMain.setAction(PermissionManager.PERMISSION_VIOLATED_ACTION);
			startMain.putExtra(PermissionManager.EXTRA_PACKAGE_NAME, packageName);
			startMain.putExtra(PermissionManager.EXTRA_VIOLATION_REASON, reason);
			startActivity(startMain);
			
			// Kill the process after it is moved into background
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.schedule(new Runnable() {

				@Override
				public void run() {
					ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					am.killBackgroundProcesses(packageName);
					
					android.os.Process.killProcess(pid);
					android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
					android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_QUIT);
					Log.w(TAG, "The system killed the package " + packageName + " due to violation of permission");
				}
				
			}, PERMISSION_KILL_PROCESS_DELAY, TimeUnit.MILLISECONDS);
		}
		
		/** 
		 * Check current running processes with permission, and update the permission table if needed.
		 */
		private final Runnable permissionTask = new Runnable() {

			@Override
			public void run() {
				Log.d(TAG, "Start enforcing permission");

				// Get current logged in user from AccountManager
				$User user = mHandler.getLastLoggedInUser();
				
				Map<String, Integer> runningProcesses = getForegroundRunningProcesses();
				// Cannot continue if there no running processes
				if (runningProcesses == null) {
					return;
				}
				
				// Retrieve and check guard for each running package
				Set<String> runningPackages = runningProcesses.keySet();
				for (String packageName : runningPackages) {
					Component component = mHandler.getComponent(packageName);
					Category category = mHandler.getCategory(Category.CATEGORY_GAMES);
					
					// If the package is not one of the known discrete
					// component, consider it as either a "game" or "app". 
					if (component == null || (category != null && category.hasComponent(component))) {
						component = mHandler.getComponent(Component.COMPONENT_GAME);
					}
					
					int isComponentAccessible = mHandler.isAccessible(user, component, period);
					if (isComponentAccessible != PermissionManager.FLAG_PERMISSION_OK) {
						killPackage(packageName, runningProcesses.get(packageName), isComponentAccessible);
					}
					
					// No permission setting defined for the package
					History history = component.getHistory(user);
					if (history == null) {
						history = new History();
						history.setUser(user);
						history.setComponent(component);
					}
					
					// Update history of the package
					updateHistory(history);
				}
			}
		};
		
		private void updateHistory(History history) {
			// Quick return if there is nothing to process
			if (history == null) {
				return;
			}
			
			// Reads current configuration settings
			android.content.res.Configuration config = new android.content.res.Configuration();
			Settings.System.getConfiguration(getContentResolver(), config);
			Date currentDate = Calendar.getInstance(config.locale).getTime();
			
			// Updates the last active date and accumulate running time
			Date lastActiveDate = history.getLastActiveDate();
			if (lastActiveDate == null) {
				// The component had never been active
				history.setAccumulatedActiveTime(Long.valueOf(0));
			} else {
				int result = DateUtils.compare(currentDate, lastActiveDate);
				switch (result) {
				case DateUtils.BEFORE:
					// User must have tempered with the system date. We set the accumulated
					// active time to the maximum
					history.setAccumulatedActiveTime(Long.valueOf(24 * 60 * 60 * 1000));
					break;
				case DateUtils.EQUAL:
					// Should not happen but we set it to 0 so that user can access the application
					history.setAccumulatedActiveTime(Long.valueOf(0));
					break;
				case DateUtils.AFTER:
					if (DateUtils.dayDifference(currentDate, lastActiveDate) == 0) {
						// Accumulate the active running time by the run interval of the task
						history.accumulateActiveTime(period);
					} else {
						// Reset the run-time since it is another day
						history.setAccumulatedActiveTime(Long.valueOf(0));
					}
					break;
				}
			}
			history.setLastActiveDate(currentDate);
			
			try {
				Dao<History, Long> dao = getHelper().getDao(History.class);
				dao.createOrUpdate(history);
			} catch (SQLException e) {
				// Cannot update history of the package. Ignore
				Log.e(TAG, "Cannot update history of the package because " + e + " occurred");
			}
		}
		
		
		/**
		 * Schedule periodic permission enforcement 
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
						permissionTask, 
						period, 
						period,
						TimeUnit.MILLISECONDS);
				this.period = period;
			} catch (Exception ex) {
				// The task cannot be scheduled
				Log.e(TAG, "Permission controller cannot be scheduled to run because " + ex.getMessage());
				
				if (autoReschedule) {
					// Try to reschedule
					ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
					service.schedule(new Runnable() {

						@Override
						public void run() {
							schedule(period, true);
						}
						
					}, PERMISSION_ENFORCEMENT_INTERVAL, TimeUnit.MILLISECONDS);
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
