/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.system;

import java.io.File;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RecoverySystem;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.oregonscientific.meep.DatabaseService;
import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.logging.Level;
import com.oregonscientific.meep.logging.LogManager;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.recommendation.RecommendationManager;
import com.oregonscientific.meep.util.RuntimeUtils;

/**
 * A system service handles commands received from MEEP server that
 * perform system operations such as remote wipe, etc. on the device
 */
public class SystemService extends DatabaseService<SystemDatabaseHelper> {

	private final String TAG = "SystemService";
	
	private final String EXTRA_LAUNCH_PACKAGE = "MEEP_LAUNCH";
	private final String EXTRA_INSTALL_PACKAGE = "MEEP_INSTALL";
	private final String EXTRA_PACKAGE_EXTENSION = "apk";
	
	// Keys to packageInfo
	private final String ACTION_ALL_PACKAGES = "all-packages";
	private final String ACTION_INSTALL_PACKAGE = "application/vnd.android.package-archive";
	
	// Property keys for a {@link Message}
	private final String KEY_PARAM = "parameter";
	private final String KEY_MESSAGE = "message";
	private final String KEY_LOCALE = "locale";
	
	// Keys for remote console
	private final String KEY_COMMAND = "command";
	private final String KEY_RESULT = "result";
	
	// Keys for report running task
	private final String KEY_RUNNING_TASK = "runningTask";
	private final int MAX_TASKS = 1;
	
	// Keys for ACTION_REBOOT
	private final String EXTRA_REBOOT_NOWAIT = "nowait";
	private final String EXTRA_REBOOT_INTERVAL = "interval";
	private final String EXTRA_REBOOT_WINDOW = "window";
	
	// Keys for report version
	private final String KEY_APP_NAME = "appName";
	private final String KEY_VERSION_CODE = "versionCode";
	private final String KEY_VERSION_NAME = "versionName";
	private final String KEY_FIRST_INSTALL_TIME = "firstInstallTime";
	private final String KEY_LAST_UPDATE_TIME = "lastUpdateTime";
	
	// Keys for report system info
	private final String KEY_FINGERPRINT = "fingerprint";
	private final String KEY_MANUFACTURER = "manufacturer";
	private final String KEY_MODEL = "model";
	private final String KEY_PRODUCT = "product";
	private final String KEY_INCREMENTAL = "incremental";
	private final String KEY_SERIAL_NUMBER = "serialno";
	private final String KEY_LANGUAGE = "language";
	private final String KEY_HARDWARE_ADDRESS = "hwaddr";
	
	/** The executor that executes tasks as per requested */
	private final ExecutorService mExecutor = Executors.newCachedThreadPool();
	
	/**
	 * An enumeration for system commands
	 */
	private enum Command {
		BROADCAST_MESSAGE(Message.COMMAND_BROADCAST_MESSAGE),
		GET_PERMISSION(Message.OPERATION_CODE_GET_PERMISSION),
		GET_BLACKLIST(Message.OPERATION_CODE_GET_BLACKLIST),
		GET_RECOMMENDATIONS(Message.OPERATION_CODE_GET_RECOMMENDATIONS),
		GET_APP_CATEGORIES(Message.COMMAND_GET_APPS_CATEGORY),
		GET_FRIEND_LIST(Message.OPERATION_CODE_GET_FRIEND_LIST),
		INSTALL_PACKAGE(Message.COMMAND_INSTALL_PACKAGE),
		INSTALL_OTA(Message.COMMAND_UPGRADE_SYSTEM),
		LAUNCH_PACKAGE(Message.COMMAND_LAUNCH_PACKAGE),
		REMOTE_DOWNLOAD(Message.COMMAND_REMOTE_DOWNLOAD),
		REMOTE_CONSOLE(Message.COMMAND_REMOTE_CONSOLE),
		REMOTE_WIPE(Message.COMMAND_REMOTE_WIPE),
		REBOOT(Message.COMMAND_REBOOT),
		REPORT_RUNNING_TASKS(Message.COMMAND_REPORT_RUNNING),
		REPORT_VERSION(Message.COMMAND_REPORT_VERSION),
		REPORT_SYSTEM(Message.COMMAND_REPORT_SYSTEM);
		
		private String name;

		Command(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}

		public static Command fromString(String text) {
			if (text != null) {
				for (Command b : Command.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	
	/**
	 * Installs a package at the given URL
	 * 
	 * @param url URL to the package 
	 * @param packageName The name of the package to install
	 */
	private synchronized void installPackage(String url, String packageName) {
		if (isFileURL(url)) {
			installPackage(url);
		} else if (packageName != null) {
			File storageDirectory = MEEPEnvironment.getStoragePublicDirectory(MEEPEnvironment.DIRECTORY_APP);
			if (!packageName.endsWith("." + EXTRA_PACKAGE_EXTENSION)) {
				packageName = packageName + "." + EXTRA_PACKAGE_EXTENSION;
			}
			
			File packageFile = new File(storageDirectory, packageName);
			if (packageFile.exists()) {
				packageFile.delete();
			}
			
			// Download the remote resource
			downloadResource(packageName, url, packageFile.getAbsolutePath(), "");
		}
	}
	
	/**
	 * Download the given remote resource 
	 * 
	 * @param name The name to be given to the resource
	 * @param url URL to the remote resource
	 * @param localPath the local File path to store the downloaded resource
	 * @param type the type of the resource to download
	 */
	private void downloadResource(
			String name, 
			String url, 
			String localPath, 
			String type) {
		
		// TODO: download the remote resource
	}
	
	/**
	 * Downloads the remote resource as specified by {@code url}. After the resource is 
	 * downloaded launch the system recovery sequence to upgrade the sytem 
	 * 
	 * @param url
	 */
	private void upgradeSystem(String url) {
		// TODO: asks StoreService to download and upgrade the system
	}
	
	/**
	 * Returns whether the given URL uses the file scheme
	 * 
	 * @param url The 
	 * @return
	 */
	private boolean isFileURL(String url) {
		boolean result = false;
		try {
			URI uri = new URI(url);
			result = uri.getScheme().equalsIgnoreCase("");
		} catch (Exception ex) {
			// Ignore
			Log.e(TAG, "Cannot determine whether or not " + url + " is a file URL");
		}
		return result;
	} 
	
	/**
	 * Installs a package at the given URL
	 * 
	 * @param handler The handler the receive is running in
	 * @param url The URL to the package
	 */
	private void installPackage(final String url) {
		try {
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.setDataAndType(Uri.fromFile(new File(new URI(url))), ACTION_INSTALL_PACKAGE);
			installIntent.putExtra(EXTRA_INSTALL_PACKAGE, true);
			startActivity(installIntent);
			
			// Creates a log record
			mExecutor.execute(new Runnable() {

				@Override
				public void run() {
					AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
					Account account = am.getLastLoggedInAccountBlocking();
					
					// Do not proceed if we cannot obtain an {@link Account}
					if (account == null) {
						return;
					}
					
					LogManager logManager = (LogManager) ServiceManager.getService(SystemService.this, ServiceManager.LOG_SERVICE);
					logManager.logrb(
							Level.INFO, 
							LogManager.LOG_SYSTEM, 
							account.getId(), 
							"", 
							"System", 
							"install", 
							Arrays.asList( new String[] { url }));
				}
				
			});
			
		} catch (Exception ex) {
			// Cannot install the package specified by the URL. Ignored.
			Log.e(TAG, "Cannot install package " + url);
		}
	}
	
	/**
	 * Launch an application with the given package name, do nothing if the given package name is null 
	 * 
	 * @param packageName The package name of the application
	 * 
	 * @author Andy Au
	 */
	private synchronized void launchPackage(final String packageName) {
		// Launch the component as a new task
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(packageName));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.putExtra(EXTRA_LAUNCH_PACKAGE, true);
		startActivity(intent);
		
		// Creates a log record
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// Do not proceed if we cannot obtain an {@link Account}
				if (account == null) {
					return;
				}
				
				LogManager logManager = (LogManager) ServiceManager.getService(SystemService.this, ServiceManager.LOG_SERVICE);
				logManager.logrb(
						Level.INFO, 
						LogManager.LOG_SYSTEM, 
						account.getId(), 
						"", 
						"System", 
						"launch", 
						Arrays.asList( new String[] { packageName }));
			}
			
		});
	}

	/**
	 * Reboot into the recovery system to wipe the /cache partition
	 * 
	 * @author Andy Au
	 */
	private synchronized void wipeUserDate() {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// Do not proceed if we cannot obtain an {@link Account}
				if (account == null) {
					return;
				}
				
				// Creates a log record
				LogManager logManager = (LogManager) ServiceManager.getService(SystemService.this, ServiceManager.LOG_SERVICE);
				logManager.logrb(
						Level.INFO, 
						LogManager.LOG_SYSTEM, 
						account.getId(), 
						"", 
						"System", 
						"rebootWipeUserData", 
						null);
				
				try {
					// Reboots the device and wipes the user data partition
					RecoverySystem.rebootWipeUserData(SystemService.this);
				} catch (Exception ex) {
					// Recovery failed
					Log.e(TAG, "Cannot wipe user data because " + ex.getMessage());
				}
			}
			
		});
	}
	
	/**
	 * Reboots the device
	 * 
	 * @author Andy Au
	 */
	private synchronized void rebootDevice() {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// Do not proceed if we cannot obtain an {@link Account}
				if (account == null) {
					return;
				}
				
				LogManager logManager = (LogManager) ServiceManager.getService(SystemService.this, ServiceManager.LOG_SERVICE);
				logManager.logrb(
						Level.INFO, 
						LogManager.LOG_SYSTEM, 
						account.getId(), 
						"", 
						"System", 
						"reboot", 
						null);
				
				// Broadcast the reboot Intent
				Intent intent = new Intent(Intent.ACTION_REBOOT); 
				intent.putExtra(EXTRA_REBOOT_NOWAIT, 1); 
				intent.putExtra(EXTRA_REBOOT_INTERVAL, 1); 
				intent.putExtra(EXTRA_REBOOT_WINDOW, 0); 
				intent.setAction(Intent.ACTION_REBOOT); 
				SystemService.this.sendBroadcast(intent); 
			}
			
		});
	}

	/**
	 *  Reports the current running task on the device to server
	 * 
	 * @author Andy Au
	 */
	private synchronized void reportRunningTasks() {
		// Retrieve a list of tasks that are current running
		ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(MAX_TASKS);
		RunningTaskInfo info = tasks.size() == 0 ? null : tasks.get(0);
		
		// Send to MEEP server
		Message message = new Message(
				Message.PROCESS_PARENTAL, 
				Message.COMMAND_REPORT_RUNNING);
		if (info != null) {
			message.addProperty(KEY_RUNNING_TASK, info.baseActivity.flattenToString());
		}
		
		MessageManager manager = (MessageManager) ServiceManager.getService(this, ServiceManager.MESSAGE_SERVICE);
		manager.sendMessage(message);
	}
	
	/**
	 * Reports the given package information to server
	 * 
	 * @param packageName The name of the package
	 * @param versionCode the version of the package
	 * @param versionName the version name of the package
	 * @param firstIntallTime the time at which the app was first installed
	 * @param lastUpdateTime the time at which the app was last updated
	 */
	private void reportPackageInfo(
			String packageName,
			int versionCode,
			String versionName,
			long firstInstallTime,
			long lastUpdateTime) {
		
		Message message = new Message(
				Message.PROCESS_SYSTEM, 
				Message.COMMAND_REPORT_VERSION);
		
		message.addProperty(KEY_APP_NAME, packageName);
		message.addProperty(KEY_VERSION_CODE, versionCode);
		message.addProperty(KEY_VERSION_NAME, versionName);
		message.addProperty(KEY_FIRST_INSTALL_TIME, firstInstallTime);
		message.addProperty(KEY_LAST_UPDATE_TIME, lastUpdateTime);
		
		MessageManager manager = (MessageManager) ServiceManager.getService(this, ServiceManager.MESSAGE_SERVICE);
		manager.sendMessage(message);	
	}
	
	/**
	 * Reports version information of the given type 
	 * 
	 * @param packageName The name of the package
	 * 
	 * @author Andy Au
	 */
	private void reportPackageInfo(String packageName) {
		try {
			// Report information on all installed packages if the package name 
			// specified is "all-packages"
			if (ACTION_ALL_PACKAGES.equalsIgnoreCase(packageName)) {
				PackageManager manager = getPackageManager();
				Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
				mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				
				// Retrieve all activities that can be performed for the given intent.
				final List<ResolveInfo> activities = manager.queryIntentActivities(mainIntent, 0);
				for (ResolveInfo activity : activities) {
					// Retrieve overall information about an application package that is installed on the system. 
					PackageInfo info = this.getPackageManager().getPackageInfo(activity.activityInfo.packageName, 0);
					reportPackageInfo(
							info.packageName,
							info.versionCode,
							info.versionName,
							info.firstInstallTime,
							info.lastUpdateTime);
				}
			} else {
				// Retrieve overall information about an application package that is installed on the system.
				PackageInfo info = this.getPackageManager().getPackageInfo(packageName, 0);
				reportPackageInfo(
						info.packageName,
						info.versionCode,
						info.versionName,
						info.firstInstallTime,
						info.lastUpdateTime);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve information on package " + packageName + " because " + ex);
			// Reports to server that the package named {@code packageName} is not
			// installed on the system
			reportPackageInfo(packageName, 0, "NOT_INSTALLED", 0, 0);
		}
	}
	
	/**
	 * Reports system information on the device
	 *
	 * @author Andy Au
	 */
	private void reportSystemInfo() {
		Message message = new Message(
				Message.PROCESS_SYSTEM, 
				Message.COMMAND_REPORT_SYSTEM);

		message.addProperty(KEY_FINGERPRINT, android.os.Build.FINGERPRINT);
		message.addProperty(KEY_MANUFACTURER, android.os.Build.MANUFACTURER);
		message.addProperty(KEY_MODEL, android.os.Build.MODEL);
		message.addProperty(KEY_PRODUCT, android.os.Build.PRODUCT);
		message.addProperty(KEY_SERIAL_NUMBER, android.os.Build.SERIAL);
		message.addProperty(KEY_INCREMENTAL, android.os.Build.VERSION.INCREMENTAL);
		message.addProperty(KEY_LANGUAGE, Locale.getDefault().getLanguage());

		// MAC address of WiFi internet
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		String macAddress = wifiManager.getConnectionInfo().getMacAddress();
		message.addProperty(KEY_HARDWARE_ADDRESS, macAddress);
		
		// Sends the message via socket
		MessageManager manager = (MessageManager) ServiceManager.getService(this, ServiceManager.MESSAGE_SERVICE);
		manager.sendMessage(message);
	}
	
	/**
	 * Retrieve categories of recognized applications from MEEP server
	 */
	private void getComponents() {
		Message message = new Message(
				Message.PROCESS_SYSTEM, 
				Message.COMMAND_GET_APPS_CATEGORY);
		
		// Sends the message via socket
		MessageManager manager = (MessageManager) ServiceManager.getService(this, ServiceManager.MESSAGE_SERVICE);
		manager.sendMessage(message);
	}
	
	/**
	 * Call {@link PermissionManager} to synchronize local blacklist with server
	 * 
	 * @param type the type of blacklist to synchronize
	 * @param locale the locale of the blacklist to synchronize
	 */
	private void syncBlacklist(final String type, final String locale) {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// Cannot proceed with execution if there is no user currently
				// logged in
				if (account != null) {
					PermissionManager pm = (PermissionManager) ServiceManager.getService(SystemService.this, ServiceManager.PERMISSION_SERVICE);
					pm.syncBlacklist(account.getId(), type, locale);
				}
			}
			
		});
	}
	
	/**
	 * Call {@link RecommendationManager} to synchronize local recommendations with
	 * server
	 * 
	 * @param type the type of recommendation to synchronize
	 */
	private void syncRecommendations(final String type) {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// Cannot proceed with execution if there is no user currently
				// logged in
				if (account != null) {
					RecommendationManager rm = (RecommendationManager) ServiceManager.getService(SystemService.this, ServiceManager.RECOMMENDATION_SERVICE);
					rm.syncRecommendations(account.getId(), type);
				}
			}
			
		});
	}
	
	/**
	 * Call {@link PermissionManager} to synchronize local permission settings with
	 * server
	 */
	private void syncPermissions() {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// Cannot proceed with execution if there is no user currently
				// logged in
				if (account != null) {
					PermissionManager pm = (PermissionManager) ServiceManager.getService(SystemService.this, ServiceManager.PERMISSION_SERVICE);
					pm.syncAccessSchedule(account.getId());
				}
			}
			
		});
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// This service does not allow others to bind to it
		return null;
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
	public void onDestroy() {
		super.onDestroy();
		
		// Unbinds any previously binded service
		ServiceManager.unbindServices(this);
	}
	
	/**
	 * Handles the command as specified in the {@code} intent 
	 * 
	 * @param intent The intent supplied to {@link #onStartCommand(Intent, int, int)}
	 */
	private void handleCommand(Intent intent) {
		// Quick return if there is nothing to process
		if (intent == null) {
			return;
		}
		
		// Only handles messages received from MessageService
		if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
			Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
			try {
				// Quick return if there is no {@code message} to process
				if (message == null) {
					return;
				}
				
				// Retrieve attributes of the {@code message}
				String operation = message.getOperation();
				String command = (String) message.getProperty(KEY_COMMAND);
				String cmd = (String) message.getProperty(KEY_MESSAGE);
				String param = (String) message.getProperty(KEY_PARAM);
				String locale = (String) message.getProperty(KEY_LOCALE);
				
				if (Message.OPERATION_CODE_RUN_COMMAND.equals(operation)) {
					switch (Command.fromString(command)) {
					case BROADCAST_MESSAGE:
						// TODO: post a notification via NotificationManager
						break;
					case GET_APP_CATEGORIES:
						getComponents();
						break;
					case GET_BLACKLIST:
						syncBlacklist(param, locale);
						break;
					case GET_PERMISSION:
						syncPermissions();
						break;
					case GET_RECOMMENDATIONS:
						syncRecommendations(param);
						break;
					case INSTALL_PACKAGE:
						installPackage(param, cmd);
						break;
					case INSTALL_OTA:
						upgradeSystem(param);
						break;
					case LAUNCH_PACKAGE:
						launchPackage(param);
						break;
					case REMOTE_CONSOLE:
						executeCommand(cmd);
						break;
					case REMOTE_WIPE:
						wipeUserDate();
						break;
					case REBOOT:
						rebootDevice();
						break;
					case REPORT_RUNNING_TASKS:
						reportRunningTasks();
						break;
					case REPORT_VERSION:
						reportPackageInfo(param);
						break;
					case REPORT_SYSTEM:
						reportSystemInfo();
						break;
					}
				}
			} catch (Exception ex) {
				// Ignore message
				Log.e(TAG, message + " cannot be processed because " + ex + " occurred");
			}
		}
	}
	
	/**
	 * Executes the given {@code command} in Android Runtime
	 * 
	 * @param command the command to execute
	 */
	private synchronized void executeCommand(final String command) {
		// Quick return if there is no command to execute
		if (command == null) {
			return;
		}
		
		// Executes the command
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// Retrieve the current logged in user
				AccountManager am = (AccountManager) ServiceManager.getService(SystemService.this, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLoggedInAccountBlocking();
				
				// Cannot proceed if there is not user logged in
				if (account == null) {
					return;
				}
				
				// Logs the command
				LogManager logManager = (LogManager) ServiceManager.getService(SystemService.this, ServiceManager.LOG_SERVICE);
				logManager.logrb(
						Level.INFO, 
						LogManager.LOG_SYSTEM, 
						account.getId(), 
						"", 
						"System", 
						"execute", 
						Arrays.asList(new String[] { command }));
				
				// Executes the command and sends the result to server
				recordCommand(account, command);
				String result = RuntimeUtils.exec(command);
				
				// Sends the result from executing the command to MEEP server
				Message message = new Message(
						Message.PROCESS_SYSTEM, 
						Message.OPERATION_CODE_RUN_COMMAND);
				message.addProperty(KEY_COMMAND, command);
				message.addProperty(KEY_RESULT, result);
				
				MessageManager mm = (MessageManager) ServiceManager.getService(SystemService.this, ServiceManager.MESSAGE_SERVICE);
				mm.sendMessage(message);
			}
			
		});
	}
	
	/**
	 * Creates a log entry for the execution of the {@code command} 
	 * 
	 * @param account the account that executed the 
	 * @param command
	 */
	private void recordCommand(Account account, String command) {
		// Quick return if the request cannot be processed
		if (account == null || command == null) {
			return;
		}
		
		try {
			CommandHistory history = new CommandHistory(command);
			history.setUser(getUser(account, true));
			
			Dao<CommandHistory, Long> dao = getHelper().getDao(CommandHistory.class);
			dao.create(history);
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot log command: " + command + " because " + ex);
		}
	}
	
	/**
	 * Retrieves the User object identified by the {@code account}
	 *  
	 * @param account The Account to retrieve
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return The User object or <code>null</code> if the user was not found
	 */
	private $User getUser(Account account, boolean createIfNotExists) {
		String userId = account == null ? "" : account.getId();
		return getUser(userId, createIfNotExists);
	}
	
	/**
	 * Retrieve the User object with the given {@code userId}. If the user cannot be
	 * found, creates the User object if {@code createIfNotExist} is set to true
	 * 
	 * @param userId the unique name identifying the user
	 * @param createIfNotExists true to create the User object if it was not found
	 * @return the $User object or <code>null</code> if the user was not found
	 */
	private synchronized $User getUser(String userId, boolean createIfNotExists) {
		if (getHelper() == null) {
			return null;
		}
		
		$User result = null;
		try {
			Dao<$User, Long> dao = getHelper().getDao($User.class);
			QueryBuilder<$User, Long> qb = dao.queryBuilder();
			qb.where().eq($User.IDENTIFIER_FIELD_NAME, userId);
			result = dao.queryForFirst(qb.prepare());
			
			if (result == null && createIfNotExists) {
				result = new $User(userId);
				dao.createIfNotExists(result);
			}
		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve " + userId + " because " + ex);
			result = null;
		}
		return result;
	}
	
}
