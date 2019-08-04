/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.oregonscientific.meep.app.DialogMessage;
import com.oregonscientific.meep.home.internal.VolumeManager;
import com.oregonscientific.meep.home.view.AnimatedImageView;
import com.oregonscientific.meep.home.view.AnimatedImageView.AnimatedImageViewItem;
import com.oregonscientific.meep.home.view.DrawerPanel;
import com.oregonscientific.meep.home.view.HeaderProfileView;
import com.oregonscientific.meep.home.view.MenuItem2;
import com.oregonscientific.meep.logging.LogManager;
import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.store2.banner.Banner;
import com.oregonscientific.meep.store2.banner.MeepStoreBannerItems;
import com.oregonscientific.meep.store2.banner.MeepStoreBannerItemsCallback;
import com.oregonscientific.meep.util.NetworkUtils;
import com.oregonscientific.meep.util.SystemUtils;

public class HomeActivity extends Activity {
	
	private final String TAG = getClass().getSimpleName();
	
	private final long MAX_INTERVAL = 60000;
	
	/* The package names */
	private final String PACKAGE_SETUP = "com.oregonscientific.meep.meepopenbox/.MeepOpenBoxLanguageSelect";
	private final String PACKAGE_NEWS = "com.oregonscientific.meep.notification/.internal.NewsService";
	
	private final Handler mHandler = new Handler();
	private boolean isForeground = false;
	
	enum Action {
		NETWORK_STATE_CHANGED(WifiManager.NETWORK_STATE_CHANGED_ACTION),
		NOTIFICATION_POSTED(NotificationManager.NOTIFICATION_POSTED_ACTION),
		NOTIFICATION_CANCELED(NotificationManager.NOTIFICATION_CANCELED_ACTION),
		VOLUME_CHANGED(VolumeManager.VOLUME_CHANGED_ACTION),
		VOLUME_THRESHOLD_REACHED(VolumeManager.VOLUME_THRESHOLD_REACHED_ACTION);
		
		private String name = null;

		Action(String accessString) {
			this.name = accessString;
		}

		public String toString() {
			return name;
		}
		
		public static Action fromString(String text) {
			if (text != null) {
				for (Action b : Action.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return null;
		}
	}
	
	// The callback of Account Service
	private final IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

		@Override
		public void onSignIn(boolean success, String errorMessage, Account account) throws RemoteException {
			if (success) {
				// update the view
				updateUserProfile(account);
				updateNotificationCount(account);
			} else {
				final Account acct = account;
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						AccountManager accountManager = (AccountManager) ServiceManager.getService(HomeActivity.this, ServiceManager.ACCOUNT_SERVICE);
						accountManager.signIn(acct);
					}
					
				};
				
				if (NetworkUtils.hasInternetConnection(HomeActivity.this)) {
					ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
					service.schedule(runnable, MAX_INTERVAL, TimeUnit.MILLISECONDS);
				} else {
					addFutureTask(TASK_SIGN_IN, runnable);
				}
			}
		}

		@Override
		public void onSignOut(boolean success, String errorMessage, Account account) throws RemoteException {
			if (success) {
				updateUserProfile(account);
			} else {
				if (!AccountManager.ERROR_ACCOUNT_NOT_FOUND.equals(errorMessage)) {
					// sign out again if fail
					ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
					service.schedule(new Runnable() {

						@Override
						public void run() {
							AccountManager accountManager = (AccountManager) ServiceManager.getService(HomeActivity.this, ServiceManager.ACCOUNT_SERVICE);
							accountManager.signOut();
						}
						
					}, MAX_INTERVAL, TimeUnit.MILLISECONDS);
				}
			}
			
		}

		@Override
		public void onUpdateUser(boolean success, String errorMessage, Account account) throws RemoteException {
			// If update failed, retrieve the Account object from cache prior to
			// the update. The Account object passed to this method contains
			// information about the original request
			if (!success) {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(HomeActivity.this, ServiceManager.ACCOUNT_SERVICE);
				account = accountManager.getLastLoggedInAccountBlocking();
			}
			updateUserProfile(account);
			
			// Display dialog if the activity is running in foreground of the activity stack
			if (isForeground()) {
				int resId = success ? R.string.alert_profile_picture_updated : R.string.alert_profile_update_failed;
				int titleId = success ? R.string.title_notice : R.string.title_oops;
				
				DialogFragment dialog = DialogFragment.newInstance(
						HomeActivity.this.getString(titleId), 
						HomeActivity.this.getString(resId));
				dialog.setPositiveButton(
						HomeActivity.this.getString(R.string.alert_button_ok), 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogFragment dialog, int which) {
								dialog.dismiss();
							}
						});
				dialog.show(getFragmentManager());
			}
		}
	};

	/**
	 * The {@link BroadcastReceiver} is monitor network state change
	 */
	private LocalReceiver mReceiver = null;
	
	private final String TASK_SIGN_IN = "sign-in";
	
	// A list of future tasks
	private final Object mLock = new Object();
	private final Map<String, Runnable> mFutureTasks = new HashMap<String, Runnable>();
	
	MeepStoreBannerItems mService;
	private final ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = MeepStoreBannerItems.Stub.asInterface(service);
			Log.d(TAG, "MeepStoreBannerItems Service Connected : " + mService);
			try {
				mService.registerCallback(mCallback);
				setSlideShow(mService.getLatestThreeBannerItems());
			} catch (RemoteException e) {
				// Ignore
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
			Log.d(TAG, "MeepStoreBannerItems Service DisConnected :" + mService);
		}
	};
	
    public static final int RESULT_OK = 0;
    public static final int RESULT_USER_CANCELED = 1;
    public static final int RESULT_ERROR = 2;
    public static final int RESULT_ITEM_UNAVAILABLE = 3;
    
    //key in Bundle response
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_LIST_BANNER_ITEMS = "LIST_BANNER_ITEMS";
    
    final MeepStoreBannerItemsCallback mCallback = new MeepStoreBannerItemsCallback.Stub() {
		
		@Override
		public void onGetBannerItems(Bundle bundle) throws RemoteException {
			setSlideShow(bundle);
		}

	};
	
	/**
	 * Adds the given task to the list of future tasks
	 * 
	 * @param type the type of task
	 * @param task the task to execute
	 */
	private void addFutureTask(String type, Runnable task) {
		// Quick return if the request cannot be processed
		if (type == null || task == null) {
			return;
		}
		
		synchronized (mLock) {
			mFutureTasks.put(type, task);
		}
	}
	
	public ArrayList<Banner> getThreeBannerItems() {
		try {
			Bundle bundle = mService.getLatestThreeBannerItems();
			return getBannerItems(bundle);
		} catch (Exception e) {

		}
		return null;
	}
	
	public ArrayList<Banner> getBannerItems(Bundle bundle) throws RemoteException {
		try {
			
			if (bundle != null) {
				Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriDeserializer()).create();
				// get response status
				int code = bundle.getInt(RESPONSE_CODE);
				// success status
				if (code == RESULT_OK) {

					ArrayList<String> bannerStrings = bundle.getStringArrayList(RESPONSE_GET_LIST_BANNER_ITEMS);
					ArrayList<Banner> array = new ArrayList<Banner>();
					String message = "";
					for (String s : bannerStrings) {
						Banner banner = gson.fromJson(s, Banner.class);
						array.add(banner);
						message += banner.getPackageName() + ":"
								+ banner.getImageUrl() + "\n";
						// add button into linearlayout
						Button item = new Button(this);
						item.setTag(banner.getIntent());
						item.setText(banner.getPackageName());
						item.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity((Intent) v.getTag());
							}
						});
					}
					Log.d(TAG,"getbanner success:" + message);

					// banner information
					return array;
				}
				// TODO:others status
				else if (code == RESULT_ITEM_UNAVAILABLE) {
					Log.d(TAG, "getbanner item unavailable");
				} else {
					Log.d(TAG, "getbanner failed");
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns whether or not the {@link Activity} is running in the foreground task and top
	 * of the activity stack
	 */
	protected boolean isForeground() {
		return isForeground;
	}
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		if (PermissionManager.PERMISSION_VIOLATED_ACTION.equals(intent.getAction())) {
			String packageName = intent == null ? null : intent.getStringExtra(PermissionManager.EXTRA_PACKAGE_NAME);
			int reason = intent == null ? PermissionManager.FLAG_PERMISSION_DENIED
					: intent.getIntExtra(
							PermissionManager.EXTRA_VIOLATION_REASON,
							PermissionManager.FLAG_PERMISSION_DENIED);
			Log.e(TAG, packageName + " violated permission...");
			
			// Show alert dialog if the activity was resumed because permission of a
			// running application was violated
			if (packageName != null) {
				int resId = reason == PermissionManager.FLAG_PERMISSION_TIMEOUT ? R.string.title_timeout : R.string.title_oops;
				DialogFragment dialog = DialogFragment.newInstance(
						getString(resId), 
						DialogMessage.getPackagePermissionViolatedMessage(this, packageName, reason));
				dialog.setPositiveButton(
						getString(R.string.alert_button_ok), 
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogFragment dialog, int which) {
								dialog.dismiss();
							}
							
						});
				dialog.show(getFragmentManager());
			}
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		isForeground = false;
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		
		isForeground = true;
		
		// Check to need launch Open box
		if (SystemUtils.isSystemRestored(this)) {
			try {
				// Set default wallpaper
				WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
				Point point = new Point();
				//getWindowManager().getDefaultDisplay().getSize(point);
				//wallpaperManager.suggestDesiredDimensions(point.x , point.y);
				wallpaperManager.setResource(R.drawable.meep2_bg);
				
				// Determine whetehr or not we should launch OpenBox
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				if (!SystemUtils.isSystemConfigured(this)) {
					intent.setComponent(ComponentName.unflattenFromString(PACKAGE_SETUP));
				} else {
					intent.setClass(this, TutorialActivity.class);
				}
				startActivity(intent);
			} catch (Exception e) {
				// Ignored
				Log.e(TAG, "Failure because " + e);
			}
		}
		
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString("com.oregonscientific.meep.store2/.banner.BannerItemsService"));
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Update system properties
		Build.update(this);
		
		// Set the activity content from resource
		setContentView(R.layout.activity_home);
		
		// Register to receive broadcasts
		mReceiver = new LocalReceiver();
		IntentFilter filter = new IntentFilter();
		for (Action action : Action.values()) {
			filter.addAction(action.toString());
		}
		registerReceiver(mReceiver, filter);
		
		// Start background services and register for callbacks
		final AccountManager accountManager = (AccountManager) ServiceManager.getService(this, ServiceManager.ACCOUNT_SERVICE);
		accountManager.registerCallback(accountCallback);
		
		if (NetworkUtils.hasInternetConnection(this)) {
			Log.d(TAG, "Signing in... Retrieve permissions? " + SystemUtils.isSystemRestored(this));
			accountManager.signIn(SystemUtils.isSystemRestored(this));
		} else {
			Log.d(TAG, "Schedule sign-in to run after network connection is established. Retrieve permissions? " + SystemUtils.isSystemRestored(this));
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					accountManager.signIn(SystemUtils.isSystemRestored(HomeActivity.this));
				}
				
			};
			addFutureTask(TASK_SIGN_IN, runnable);
		}
		
		// Starts store service
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(ServiceManager.STORE_SERVICE));
		startService(intent);
		// Starts news service
		intent.setComponent(ComponentName.unflattenFromString(PACKAGE_NEWS));
		startService(intent);
		// Starts permission service
		intent.setComponent(ComponentName.unflattenFromString("com.oregonscientific.meep.store2/.banner.BannerItemsService"));
		startService(intent);
		ServiceManager.getService(this, ServiceManager.PERMISSION_SERVICE);
		// Starts logger
		LogManager logManager = (LogManager) ServiceManager.getService(this, ServiceManager.LOG_SERVICE);
		logManager.startLogger(MAX_INTERVAL);
		
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				Account account = accountManager.getLastLoggedInAccountBlocking();
				updateUserProfile(account);
				
				// Initialize language detector
				try {
					DetectorFactory.loadProfiles(getAssets(), "profiles");
					Log.i(TAG, "Initialized language detector...");
				} catch (Exception ex) {
					Log.e(TAG, "Cannot initialize language detector factory...");
				}
			}
		});
		
	}
	
	/**
	 * Start a Thread to keep check the notification count
	 */
	public void updateNotification() {
		ExecutorService service = Executors.newSingleThreadExecutor();
			service.execute(new Runnable() {

				@Override
				public void run() {
					AccountManager accountManager = (AccountManager) ServiceManager.getService(HomeActivity.this, ServiceManager.ACCOUNT_SERVICE);
					if (accountManager == null) {
						return;
					}
							
					Account account = accountManager.getLastLoggedInAccountBlocking();	
					if (account == null) {
						return;
					}			
					updateNotificationCount(account);
				}
						
			});
		
	}
	
	/**
	 * Start a new Thread Executor to handle update bage
	 * @param account 
	 */
	private void updateNotificationCount(final Account account) {	
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				updateBadge(account);
			}
		});
	}
	
	/**
	 * Get all non-read notifications to update the badge view  
	 * @param account the account object for fetch the notification
	 */
	private void updateBadge(Account account) {
		// Quick return if the request cannot be processed
		if (account == null && mHandler != null) {
			return;
		}
		
		NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(HomeActivity.this, ServiceManager.NOTIFICATION_SERVICE);
		// Get the count of unread news
		final int newsCount = notificationManager.countBlocking(account.getId(), Notification.KIND_NEWS, Notification.FLAG_READ);
		
		// Get the count of alerts
		int notificationCount = notificationManager.countBlocking(account.getId(), Notification.KIND_ALERT);
		notificationCount = notificationCount + notificationManager.countBlocking(account.getId(), Notification.KIND_STORE);

		// Get the number of unread messages
		final int messageCount = notificationManager.countBlocking(account.getId(), Notification.KIND_MESSAGE, Notification.FLAG_READ);
		List<Notification> notifications = notificationManager.get(account.getId(), Notification.KIND_MESSAGE, false, 0, 1);
		final int communicatorMessageCount = notifications == null ? 0 : notifications.get(0).number;
		
		// Get the number of unread warnings
		final int warningCount = notificationManager.countBlocking(account.getId(), Notification.KIND_WARNING, Notification.FLAG_READ);
		
		final int notificationCountCopy = notificationCount;
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				MenuItem2 news = (MenuItem2) findViewById(R.id.menu_item_news);
				MenuItem2 notification = (MenuItem2) findViewById(R.id.menu_item_notifications);
				MenuItem2 communicator = (MenuItem2) findViewById(R.id.menu_item_communicator);
				boolean playSFX = false;
				
				// Update badge count
				if (news != null) {
					String oldText = news.getBadgeText();
					int oldCount = oldText == null || oldText.length() == 0 ? 0 : Integer.valueOf(oldText);
					playSFX = newsCount > oldCount;
					
					String text = newsCount == 0 ? null : String.valueOf(newsCount);
					news.setBadgeText(text);
				}
				
				if (notification != null) {
					String oldText = notification.getBadgeText();
					int oldCount = oldText == null || oldText.length() == 0 ? 0 : Integer.valueOf(oldText);
					playSFX |= (notificationCountCopy + messageCount + warningCount) > oldCount;
					
					String text = notificationCountCopy + messageCount + warningCount == 0 ? null : String.valueOf(notificationCountCopy + messageCount + warningCount);
					notification.setBadgeText(text);
				}
				
				if (communicator != null){
					String text = communicatorMessageCount == 0 ? null : String.valueOf(communicatorMessageCount);
					communicator.setBadgeText(text);
				}
				
				// Play SFX
				if (playSFX) {
					Uri sfxUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), sfxUri);
					if (r != null) {
						r.play();
					}
				}
			}
			
		};
		mHandler.post(runnable);
	}
	
	/**
	 * Update the user nick name, meep tag and profile picture
	 * @param account the info for update the view
	 */
	private void updateUserProfile(final Account account) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				HeaderProfileView headerProfileView = (HeaderProfileView) findViewById(R.id.left_menu_header_profile);
				if (headerProfileView == null) {
					return;
				}
				
				String nickName = account == null ? getString(R.string.menu_item_profile_default_name) : account.getNickname();
				String meepTag = account == null ? "" : account.getMeepTag();
				boolean isSignedIn = account == null ? false : account.getIsLoggedIn();
				String avatarUrl = account == null ? null : account.getIconAddress();
				
				
				headerProfileView.setUserDisplayName(nickName);
				headerProfileView.setUserTag(meepTag);
				headerProfileView.signedIn(isSignedIn);
				
				if (account != null && isSignedIn) {
					File userFolder = MEEPEnvironment.getUserMediaStorageDirectory(account.getId());
					if (!userFolder.exists()) {
						userFolder.mkdirs();
					}
				}
				
				if (avatarUrl == null || avatarUrl.isEmpty()) {
					headerProfileView.setProfilePicture(R.drawable.default_avatar);
				} else {
					headerProfileView.setProfilePicture(avatarUrl);
				}
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "Home is being destroyed...");
		
		// Unregister a previously registered callback
		AccountManager accountManager = (AccountManager) ServiceManager.getService(this, ServiceManager.ACCOUNT_SERVICE);
		if (accountManager != null) {
			accountManager.unregisterCallback(accountCallback);
		}
		
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
		try {
			if (mService != null) {
				mService.unregisterCallback(mCallback);
				mService = null;
			}
			unbindService(mConnection);
		} catch (RemoteException e) {
			// Ignore
		}
		
		// Unbinds any previously binded services and shutdown the application
		ServiceManager.unbindServices(this);
		((HomeApplication) getApplicationContext()).shutdown();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case KeyEvent.KEYCODE_BACK:
				// close the menu if opened with press back button
				DrawerPanel leftPanel = (DrawerPanel) findViewById(R.id.leftPanel);
				DrawerPanel rightPanel = (DrawerPanel) findViewById(R.id.rightPanel);
				if (leftPanel == null || rightPanel == null) {
					return true;
				}
				
				if (leftPanel.isOpen()) {
					leftPanel.setOpen(false, true);
					return true;
				}
				
				if (rightPanel.isOpen()) {
					rightPanel.setOpen(false, true);
					return true;
				}
				
				//force to launch HomeActivity again
				return true;
				
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void setSlideShow(Bundle bundle) {
		final Bundle b = bundle;
		mHandler.post(new Runnable(){

			@Override
			public void run() {
				AnimatedImageView view = (AnimatedImageView) findViewById(R.id.store_slideshow);
				List<Banner> banners = null;
				try {
					banners = getBannerItems(b);
					view.clearItems();
					if (view != null && banners != null) {
						for (Banner banner : banners) {
							AnimatedImageViewItem item = view.new AnimatedImageViewItem(banner.getIntent(), banner.getImageUrl());
							view.addItem(item);
						}
						view.startAnimation();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	/**
	 * The local receiver that receive intents sent by sendBroadcast(). The handling
	 * of the intents are delegated to the service
	 */
	private final class LocalReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// Delegate intent processing to the service
			if (intent != null && context != null) {
				try {
					switch (Action.fromString(intent.getAction())) {
					case NETWORK_STATE_CHANGED:
						NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
						if (networkInfo != null && networkInfo.isConnected()) {
							Log.d(TAG, "Connected to network, run scheduled tasks...");
							
							ExecutorService service = Executors.newCachedThreadPool();
							synchronized (mLock) {
								Collection<Runnable> tasks = mFutureTasks.values();
								for (Runnable task : tasks) {
									service.execute(task);
								}
								mFutureTasks.clear();
							}
						}
						break;
						
					case NOTIFICATION_POSTED:
					case NOTIFICATION_CANCELED:
						updateNotification();
						break;
						
					case VOLUME_CHANGED:
						int volume = (Integer) intent.getIntExtra(VolumeManager.EXTRA_VOLUME_STREAM_VALUE, -1);
						Log.d(TAG, "Volume level changed to: " + volume);
						HomeApplication app = (HomeApplication) getApplicationContext();
						app.getVolumeService().assertVolumeStreamIsWithinThreshold(volume);
						break;
						
					case VOLUME_THRESHOLD_REACHED:
						int threshold = (Integer) intent.getIntExtra(VolumeManager.EXTRA_VOLUME_THRESHOLD, VolumeManager.VOLUME_OK);
						
						if (threshold == VolumeManager.VOLUME_OK) {
							VolumeAlertActivity.dismiss(context);
						} else {
							int resId = threshold == VolumeManager.VOLUME_TOO_HIGH ? R.string.alert_volume_too_high : R.string.alert_volume_too_low;
							String title = HomeActivity.this.getString(R.string.title_warning);
							String message = HomeActivity.this.getString(resId);
							VolumeAlertActivity.show(context, title, message);
						}
						break;
					}
				} catch (Exception ex) {
					// Ignored
					Log.e(TAG, "Receiver cannot start intent because " + ex + " occurred");
				}
			}	
		}

	}
	
	public class UriDeserializer implements JsonDeserializer<Uri> {
		@Override
		public Uri deserialize(JsonElement arg0, java.lang.reflect.Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			return Uri.parse(arg0.getAsString());
		}
	}
	
	
	
	
}
