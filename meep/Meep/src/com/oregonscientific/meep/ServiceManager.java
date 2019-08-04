/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.logging.LogManager;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.recommendation.RecommendationManager;

/**
 * Handles binding and unbinding to services used in a particular 
 * {@link android.content.Context}
 */
public final class ServiceManager {
	
	private static final String TAG = "ServiceManager";
	
	public static final String PACKAGE_BASE = "com.oregonscientific.meep.home";
		
	public static final String COMMUNICATOR_BASE = "com.oregonscientific.meep.communicator";

	/**
	 * Use with {@code #getService(Context, String)} to retrieve an AccountManager
	 * for managing use accounts
	 */
	public static final String ACCOUNT_SERVICE = "com.oregonscientific.meep.account.AccountService";
	
	/**
	 * Use with {@code #getService(Context, String)} to retrieve a LogManager
	 * to log events
	 */
	public static final String LOG_SERVICE = "com.oregonscientific.meep.logging.LogService";
	

	/**
	 * Use with {@code #getService(Context, String)} to retrieve a MessageManager
	 * for sending messages to MEEP server
	 */
	public static final String MESSAGE_SERVICE = "com.oregonscientific.meep.msm.MessageService";
	
	/**
	 * Use with {@code #getService(Context, String)} to retrieve a NotificationManager
	 * for informing user of events
	 */
	public static final String NOTIFICATION_SERVICE = "com.oregonscientific.meep.notification.NotificationService";
	
	/**
	 * Use with {@code #getService(Context, String)} to retrieve a PermissionManager
	 * for controlling permission settings on device
	 */
	public static final String PERMISSION_SERVICE = "com.oregonscientific.meep.permission.PermissionService";
	
	/**
	 * The service that responds to all commands received from the remote server
	 */
	public static final String SYSTEM_SERVICE = "com.oregonscientific.meep.system.SystemService";
	
	/**
	 * Use with {@code #getService(Context, String)} to retrieve a RecommendationManager
	 * for managing recommendations
	 */
	public static final String RECOMMENDATION_SERVICE = "com.oregonscientific.meep.recommendation.RecommendationService";
	
	/**
	 * Use with {@code #getService(Context, String)} to retrieve a StoreManager
	 * for accessing MEEP store
	 */
	public static final String STORE_SERVICE = "com.oregonscientific.meep.store2/.MeepStoreService";
	
	/**
	 *  Define a the communicator package name here for delivering communicator messages
	 */
	public static final String COMMUNICATOR_SERVICE = "com.oregonscientific.meep.communicator.CommunicatorService";
	
	private static final ConcurrentHashMap<String, ServiceFetcher> SYSTEM_SERVICE_MAP =
		new ConcurrentHashMap<String, ServiceFetcher>();
	
	private static final ConcurrentHashMap<ContextService, Object> mCache =
		new ConcurrentHashMap<ContextService, Object>();
	
	private static final Vector<ServiceWatcher> mWatchers = new Vector<ServiceWatcher>();
	
	static {
		
		registerService(ACCOUNT_SERVICE, new ServiceFetcher() {
			
			@Override
			public Object createService(ServiceConnector connector) {
				return new AccountManager(connector);
			}
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, ACCOUNT_SERVICE);
			}
			
		});
		
		registerService(LOG_SERVICE, new ServiceFetcher() {
			
			@Override
			public Object createService(ServiceConnector connector) {
				return new LogManager(connector);
			}
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, LOG_SERVICE);
			}
			
		});
		
		registerService(MESSAGE_SERVICE, new ServiceFetcher() {

			@Override
			public Object createService(ServiceConnector connector) {
				return new MessageManager(connector);
			}
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, MESSAGE_SERVICE);
			}
			
		});

		registerService(NOTIFICATION_SERVICE, new ServiceFetcher() {
			
			@Override
			public Object createService(ServiceConnector connector) {
				return new NotificationManager(connector);
			}
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, NOTIFICATION_SERVICE);
			}

		});

		registerService(PERMISSION_SERVICE, new ServiceFetcher() {
			
			@Override
			public Object createService(ServiceConnector connector) {
				return new PermissionManager(connector);
			}
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, PERMISSION_SERVICE);
			}

		});

		registerService(RECOMMENDATION_SERVICE, new ServiceFetcher() {
			
			@Override
			public Object createService(ServiceConnector connector) {
				return new RecommendationManager(connector);
			}
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, RECOMMENDATION_SERVICE);
			}

		});
		
		registerService(STORE_SERVICE, new ServiceFetcher() {
			
			@Override
			public ComponentName getComponent() {
				return new ComponentName(PACKAGE_BASE, STORE_SERVICE);
			}

		});

	}

	/**
	 * Returns a reference to a service with the given name
	 * 
	 * @param context
	 *          the context that the service attaches to
	 * @param name
	 *          the name of the service. Should be one of {@link #STORE_SERVICE},
	 *          {@link #RECOMMENDATION_SERVICE}, {@link #PERMISSION_SERVICE},
	 *          {@link #NOTIFICATION_SERVICE}, {@link #MESSAGE_SERVICE} or
	 *          {@link #LOG_SERVICE}
	 * @return a reference to the service, or <code>null</code> if the service
	 *         does not exist
	 */
	public static Object getService(Context context, String name) {
		if (context == null) {
			throw new IllegalArgumentException("context argument cannot be null");
		}
		
		ContextService key = new ContextService(context, name);
		ServiceFetcher fetcher = SYSTEM_SERVICE_MAP.get(name);
		return fetcher == null ? null : fetcher.getService(key);
	}

	/**
	 * Disconnect any previously binded application service. You <b>must</b>
	 * call this method to when a {@link Context} is to be destroyed. Failure to
	 * do so will result in leak.
	 * 
	 * @param context
	 *            the context that the services may have attached to
	 */
	public static void unbindServices(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("context argument cannot be null");
		}
		
		Set<ContextService> keys = mCache.keySet();
		for (ContextService key : keys) {
			if (context.equals(key.context.get())) {
				ServiceAgent<?> agent = (ServiceAgent<?>) mCache.get(key);
				agent.getConnector().disconnect();
				mCache.remove(key);
			}
		}
	}
	
	private static void registerService(String name, ServiceFetcher fetcher) {
		SYSTEM_SERVICE_MAP.put(name, fetcher);
	}
	
	static class ServiceFetcher {
		
		/**
		 * Binds to the service if it does not already exist in cache
		 * 
		 * @param context the context that the service binds to
		 * @return the service object, or <code>null</code> if the service does not exist
		 */
		public Object getService(ContextService key) {
			Object service = mCache.get(key);
			if (service != null) {
				return service;
			}
			
			// The context is released, do not create the service
			if (key.context == null) {
				return null;
			}
			
			// Creates the connector and connects to the service
			ServiceConnector connector = new ServiceConnector(key.context.get(), getComponent());
			mWatchers.add(new ServiceWatcher(connector));
			
			// Creates the service
			service = createService(connector);
			mCache.put(key, service);
			return service;
		}
		
		/**
		 * Override this to create a new per-Context instance of the service.
		 * getService() will handle locking and caching.
		 */
		public Object createService(ServiceConnector connector) {
			throw new RuntimeException("Not implemented");
		}

		/**
		 * Override this to retrieve the concrete component of the service
		 */
		public ComponentName getComponent() {
			throw new RuntimeException("Not implemented");
		}
		
	}
	
	/**
	 * Monitors the service connection. Removes it from cache if the service was dead
	 */
	private static class ServiceWatcher implements ServiceConnector.Callback, IBinder.DeathRecipient {
		
		ServiceConnector connector;
		
		ServiceWatcher(ServiceConnector connector) {
			this.connector = connector;
			this.connector.addCallback(this);
		}

		@Override
		public void binderDied() {
			Log.d(TAG, connector + " died, removing cache and watcher...");
			if (connector != null) {
				ContextService key = new ContextService(
						connector.getBaseContext(), 
						connector.getComponent().getClassName());
				mCache.remove(key);
			}
			mWatchers.remove(this);
		}

		@Override
		public void handleConnection(boolean connected) {
			if (connected) {
				linkDeathRecipient();
			} else {
				unlinkDeathRecipient();
			}
		}
		
		private void unlinkDeathRecipient() {
			if (connector != null && connector.getBinder() != null) {
				connector.getBinder().unlinkToDeath(this, 0);
			}
			binderDied();
		}
		
		private void linkDeathRecipient() {
			try {
				if (connector != null && connector.getBinder() != null) {
					connector.getBinder().linkToDeath(this, 0);
				}
			} catch (RemoteException ex) {
				Log.e(TAG, getClass().getSimpleName() + " cannot link to the death of service");
				binderDied();
			}
		}
		
	}
	
	/**
	 * Key for the service cache
	 */
	private static class ContextService {
		WeakReference<Context> context;
		String name;
		
		ContextService(Context context, String name) {
			this.context = context == null ? null : new WeakReference<Context>(context);
			this.name = name;
		}
		
		@Override
		public int hashCode() {
			final int prime = 7;
			int result = 0;
			if (context != null) {
				Context ctx = context.get();
				result = prime + ctx.hashCode();
			}
			result = prime * result + name.hashCode();
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			
			// Remove this key from cache if the associated Context was released
			if (context == null) {
				mCache.remove(this);
				return false;
			}
			
			ContextService other = (ContextService) obj;
			Context ctx = context.get();
			Context otherContext = other.context == null ? null : other.context.get();
			
			if (!ctx.equals(otherContext)) {
				return false;
			} else if (!name.equals(other.name)) {
				return false;
			} else {
				return true;
			}
		}
	}

}
