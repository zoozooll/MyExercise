/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.provider.Settings;
import android.util.Log;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.R;
import com.oregonscientific.meep.ServiceHandler;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageFilter;
import com.oregonscientific.meep.msm.MessageReceiver;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.permission.compat.GetPermissionMessagePropertyTypeAdapterFactory;
import com.oregonscientific.meep.permission.compat.SetPermissionMessagePropertyTypeAdapterFactory;
import com.oregonscientific.meep.recommendation.RecommendationManager;
import com.oregonscientific.meep.serialization.MessagePropertyTypeAdapterFactories;
import com.oregonscientific.meep.util.DateUtils;
import com.oregonscientific.meep.util.Iterables;
import com.oregonscientific.meep.util.NetworkUtils;

@SuppressLint("DefaultLocale")
public class PermissionHandler extends ServiceHandler {

	private final String TAG = getClass().getSimpleName();

	private final String EXTRA_PERMISSION = "permission";
	private final String EXTRA_BLACKLIST = "list";
	private final String EXTRA_BLACKLIST_TYPE = "type";
	private final String EXTRA_BLACKLIST_LOCALE = "locale";
	private final String EXTRA_BLACKLIST_URL = "url";
	private final String EXTRA_BLACKLIST_ACCESS = "access";

	private final String TAG_BLACKLIST = "blacklist";
	private final String ATTR_TYPE = "type";
	private final String ATTR_LOCALE = "locale";

	private final String[] EXTRA_CATEGORIES = { Category.CATEGORY_GAMES,
			Category.CATEGORY_BLACKLIST, Category.CATEGORY_IGNORED,
			Category.CATEGORY_HIDDEN };

	private static final List<Character> NON_CHARS = Arrays.asList(new Character[] {
			' ', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+',
			'{', '}', '[', '}', '\\', '\n', '\t', '\b', '\f', '\r', ',', '.',
			'/', ':', ';', '|', '=', '_', '\'', '\"' });

	/** Keys used to decode a "permission" message */
	private final String EXTRA_TOKEN = "token";
	private final String EXTRA_PERMISSION_LAST_UPDATE = "last_update";

	/** Default accessibility of packages */
	private final boolean DEFAULT_ACCESSIBILITY_URL = false;

	/** The timeout period for a WebSocket call */
	private final long TIMEOUT_PERIOD = 15000;

	private final OrmLiteSqliteOpenHelper mHelper;

	/**
	 * The Aho Corascik string search algorithm tree
	 */
	private final Object mLock = new Object();
	private final Map<String, Map<Locale, AhoCorasick>> mTreeMap = new HashMap<String, Map<Locale, AhoCorasick>>();

	/**
	 * The executor to create bad word lists
	 */
	private final ExecutorService mExecutor = Executors.newCachedThreadPool();
	private final Vector<$User> mRunningTasks = new Vector<$User>();
	private final Map<$User, List<FutureTask<?>>> mTasks = new HashMap<$User, List<FutureTask<?>>>();

	private final int ANDROID_NOTIFICATION_BUILDING_TREE_ID = 10000;

	/**
	 * Constructor for the Handler
	 * 
	 * @param context
	 *            The context should be be PermissionService
	 */
	public PermissionHandler(Context context, OrmLiteSqliteOpenHelper helper) {
		super(context);

		mHelper = helper;
		init();
	}

	private void init() {
		// Register a type adapter to serialize or deserialize permission
		// setting messages
		MessageFilter filter = new MessageFilter(Message.PROCESS_PARENTAL);
		filter.addOperation(Message.OPERATION_CODE_GET_PERMISSION);
		MessagePropertyTypeAdapterFactories.getInstance().registerTypeAdapterFactory(filter, new GetPermissionMessagePropertyTypeAdapterFactory());

		filter = new MessageFilter(Message.PROCESS_PARENTAL);
		filter.addOperation(Message.OPERATION_CODE_SET_PERMISSION);
		MessagePropertyTypeAdapterFactories.getInstance().registerTypeAdapterFactory(filter, new SetPermissionMessagePropertyTypeAdapterFactory());

		// Registers a persistent {@link MessageReceiver} to process "system"
		// process messages
		filter = new MessageFilter(Message.PROCESS_SYSTEM);
		filter.addOperation(Message.COMMAND_GET_APPS_CATEGORY);
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null) {
					return;
				}

				List<String> components = new ArrayList<String>();
				// Updates components in the categories
				for (String category : EXTRA_CATEGORIES) {
					Object value = message.getProperty(category);
					if (value instanceof String[]) {
						components.addAll(Arrays.asList((String[]) value));
						setComponentsInCategory(category, components);
					}
				}
			}

			@Override
			public boolean isPersistent() {
				return true;
			}

		};
		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
	}

	/**
	 * Clears any cached objects
	 */
	public void clearCache() {
		synchronized (mLock) {
			mTreeMap.clear();
		}
	}

	/**
	 * Adds the given {@code AhoCorasick} search tree to cache
	 * 
	 * @param locale
	 *            the locale of the {@link AhoCorasick} search tree
	 * @param tree
	 *            the {@link AhoCorasick} search tree to cache
	 */
	private void addTreeToMap(String key, Locale locale, AhoCorasick tree) {
		// Quick return if the parameters are incorrect
		if (key == null || locale == null) {
			return;
		}

		Map<Locale, AhoCorasick> trees = lookupTrees(key);
		synchronized (mLock) {
			if (tree == null) {
				if (trees != null) {
					trees.remove(locale);
				}
			} else {
				if (trees == null) {
					trees = new HashMap<Locale, AhoCorasick>();
				}
				trees.put(locale, tree);
			}
		}
		addTreesToMap(key, trees);
	}

	/**
	 * Adds the given Aho-Corasick search tree to cache
	 * 
	 * @param key
	 *            the key with which the Aho-Corasick search tree is to be
	 *            associated with
	 * @param tree
	 *            A map of localized Aho-Corasick search trees
	 */
	private void addTreesToMap(String key, Map<Locale, AhoCorasick> trees) {
		// Quick return if the request cannot be processed
		if (key == null) {
			return;
		}

		synchronized (mLock) {
			if (trees == null) {
				mTreeMap.remove(key);
			} else {
				mTreeMap.put(key, trees);
			}
		}
	}

	/**
	 * Remove a cached {@link AhoCorasick} search tree associated with the given
	 * {@code locale}
	 * 
	 * @param key
	 *            the key whose mapping is to be removed
	 * @param locale
	 *            the locale whose mapping is to be removed from cache
	 */
	private void removeTreeFromMap(String key, Locale locale) {
		// Quick return if the request cannot be processed
		if (key == null || locale == null) {
			return;
		}

		Map<Locale, AhoCorasick> trees = lookupTrees(key);
		synchronized (mLock) {
			if (trees != null) {
				trees.remove(locale);
			}
		}
		addTreesToMap(key, trees);
	}

	/**
	 * Removes the cached Aho-Corasick search tree associated with the
	 * {@code key}
	 * 
	 * @param key
	 *            the key whose mapping is to be removed from the map
	 */
	private void removeTreesFromMap(String key) {
		// Quick return if the request cannot be processed
		if (key == null) {
			return;
		}

		synchronized (mLock) {
			mTreeMap.remove(key);
		}
	}

	/**
	 * Returns a map of localized Aho-Corasick search tree to which the
	 * specified key is mapped, or null if no cached tree maps to the key.
	 * 
	 * @param key
	 *            the key whose associated Aho-Corasick search tree is to be
	 *            returned
	 * @return a map of localized Aho-Corasick search trees to which the
	 *         specified key is mapped, or null if this map contains no mapping
	 *         for the key
	 */
	private Map<Locale, AhoCorasick> lookupTrees(String key) {
		// Quick return if the request cannot be processed
		if (key == null) {
			return null;
		}

		synchronized (mLock) {
			return mTreeMap.get(key);
		}
	}

	/**
	 * Sends a {@link Message} to server to update permission settings for the
	 * given {@code user}
	 * 
	 * @param user
	 *            The user permission settings to update on server
	 */
	private void updateRemotePermissions(final $User user,
			final long lastLocalUpateTime) {
		// Quick return if there is nothing to process
		if (user == null) {
			return;
		}

		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				// Cannot proceed without retrieving the auth token of the user
				AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				if (account == null) {
					return;
				}

				// Cannot proceed if the user updating the remote permission is
				// not the currently logged in user
				String identifier = user.getIdentifier();
				if (!identifier.equals(account.getId())) {
					return;
				}

				// Creates the {@link Message} and send it via the socket server
				Message updateMessage = new Message(Message.PROCESS_PARENTAL, Message.OPERATION_CODE_SET_PERMISSION);
				updateMessage.addProperty(EXTRA_TOKEN, account.getToken());
				updateMessage.addProperty(EXTRA_PERMISSION_LAST_UPDATE, lastLocalUpateTime / 1000); // server
																									// timestamp
																									// unit
																									// is
																									// second

				// Write the permission objects
				Map<String, Permission> permissions = new HashMap<String, Permission>();
				CloseableIterator<Permission> iterator = user.getPermissions().closeableIterator();
				try {
					while (iterator.hasNext()) {
						Permission permission = iterator.next();
						Component component = permission.getComponent();

						if (component != null) {
							component.refresh();
							permissions.put(component.getDisplayName(), permission);
						}
					}
				} catch (Exception ex) {
					Log.e(TAG, "Cannot create message to update permission settings because "
							+ ex);
				} finally {
					try {
						iterator.close();
					} catch (SQLException ex) {
						Log.e(TAG, "Failed to close the iterator while iterating through permissions for "
								+ user);
					}
				}

				updateMessage.addProperty(EXTRA_PERMISSION, permissions);
				sendMessage(updateMessage);
			}

		});

	}

	/**
	 * Updates blacklist for {@code user} in the local database
	 * 
	 * @param user
	 *            The user blacklist to update in local database
	 * @param locale
	 *            The locale of the blacklist to update in local database
	 * @param message
	 *            The {@link Message} containing information the new blacklist
	 */
	private void updateLocalBacklist(final $User user, final String type,
			final String locale, final Message message) {
		// Quick return if one of the pass in parameter is null
		if (mHelper == null || user == null || type == null || message == null) {
			return;
		}

		Map<String, ?> properties = message.getProperties();
		String listType = (String) properties.get(EXTRA_BLACKLIST_TYPE);

		// Do not continue if type of the blacklist returned does not match the
		// type
		// of blacklist requested
		if (listType == null || !type.equals(listType)) {
			return;
		}

		// Updates blacklist
		Object object = properties.get(EXTRA_BLACKLIST);
		if (object instanceof String[]) {
			setBlacklist(user, type, locale, Arrays.asList((String[]) object));
		} else if (Iterables.isIterable(object)) {
			setBlacklist(user, type, locale, (Iterable<?>) object);
		}
	}

	/**
	 * Updates permission settings for {@code user} in the local database
	 * 
	 * @param user
	 *            The user permission settings to update in local database
	 * @param message
	 *            The {@link Message} containing information on new permission
	 *            settings
	 * @param lastUpdateTime
	 *            the time the permssion settings were last updated
	 */
	private void updateLocalPermissions(final $User user,
			final Message message, final long lastUpdateTime) {
		// Quick return if there is nothing to process
		if (user == null || message == null || mHelper == null) {
			return;
		}

		try {
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					Map<String, ?> properties = message.getProperties();
					for (String key : properties.keySet()) {
						Component component = getComponent(Component.DISPLAY_NAME_FIELD_NAME, key);
						// We can only update permission settings on discrete
						// components
						// that are known to the system
						if (component == null) {
							continue;
						}

						Dao<Permission, Long> dao = mHelper.getDao(Permission.class);
						Permission permission = component.getPermission(user);
						permission = permission == null ? new Permission()
								: permission;
						Long id = dao.extractId(permission);

						// Parses properties in the {@code message} into the
						// {@code permission}
						permission.parseObject(properties.get(key));
						permission.setComponent(component);
						permission.setUser(user);
						permission.setLastModifiedDate(new Date(lastUpdateTime));

						// This is required because the lastmoddate is a version
						// field. Setting a version
						// field to a value returned from server is impossible
						// using ORMLite
						if (dao.idExists(id)) {
							permission.setId(id);
							dao.delete(permission);
						}
						dao.create(permission);
					}
					return null;
				}

			});
		} catch (Exception ex) {
			Log.e(TAG, "Cannot update permission settings for " + user
					+ " because of " + ex);
		}

	}

	/**
	 * Retrieve the User object with the given {@code username}
	 * 
	 * @param username
	 *            the unique name identifying the user
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private $User getUser(String username) {
		return getUser(username, false);
	}

	/**
	 * Retrieve the User object with the given {@code userId}. If the user
	 * cannot be found, creates the User object if {@code createIfNotExist} is
	 * set to true
	 * 
	 * @param userId
	 *            the unique name identifying the user
	 * @param createIfNotExists
	 *            true to create the User object if it was not found
	 * @return the User object or <code>null</code> if the user was not found
	 */
	private $User getUser(String userId, boolean createIfNotExists) {
		if (mHelper == null) {
			return null;
		}

		$User result = null;
		try {
			Dao<$User, Long> dao = mHelper.getDao($User.class);
			QueryBuilder<$User, Long> qb = dao.queryBuilder();
			qb.where().eq($User.IDENTIFIER_FIELD_NAME, userId);
			result = dao.queryForFirst(qb.prepare());

			if (result == null && createIfNotExists) {
				result = new $User(userId);
				dao.createIfNotExists(result);

				// Create default permissions, configuration settings and
				// blacklist for the user
				createDefaultPermissions(result);
				createDefaultBlacklist(Build.DEBUG ? R.xml.blacklists_shortened
						: R.xml.blacklists, result);
			}
		} catch (Exception ex) {
			// The given user does not exist
			Log.e(TAG, "Cannot retrieve user identified by: " + userId
					+ " because " + ex);
			result = null;
		}
		return result;
	}

	/**
	 * Creates default permission settings for the given user
	 * 
	 * @param user
	 *            the user to create permission settings for
	 */
	private void createDefaultPermissions($User user) {
		// Quick return if the request cannot be processed
		if (mHelper == null || user == null) {
			return;
		}

		try {
			Set<SimpleEntry<String, String>> packages = Component.SYSTEM_COMPONENTS.keySet();
			for (SimpleEntry<String, String> pkg : packages) {
				Component component = getComponent(pkg.getValue());
				if (component != null) {
					Permission permission = new Permission(Component.SYSTEM_COMPONENTS.get(pkg), Long.valueOf(24 * 60 * 60 * 1000));
					permission.setUser(user);
					permission.setComponent(component);
					permission.setLastModifiedDate(new Date(0));

					Dao<Permission, Long> dao = mHelper.getDao(Permission.class);
					dao.create(permission);
				}
			}
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot create default permission settings for " + user
					+ " because " + ex);
		}
	}

	/**
	 * Retrieves the User object that is currently logged in. This method calls
	 * the blocking method in {@link AccountManager} to retrieve the current
	 * logged in user. User must not call this method in the main thread.
	 * 
	 * <p>
	 * This method will create the User object if the logged in user is not
	 * found in the permission database
	 * </p>
	 * 
	 * @return The User object or <code>null</code> if no user is currently
	 *         logged in
	 */
	$User getLoggedInUser() {
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		Account account = am.getLoggedInAccountBlocking();
		if (account == null) {
			account = am.getDefaultAccount();
		}
		return account == null ? null : getUser(account, true);
	}

	/**
	 * Returns the last user logged in with server. This method calls the
	 * blocking method in {@link AccountManager} to retrieve the current logged
	 * in user. User must not call this method in the main thread.
	 * 
	 * <p>
	 * This method will create the User object if the logged in user is not
	 * found in the permission database
	 * </p>
	 * 
	 * @return The User object or <code>null</code> if no user was ever logged
	 *         in
	 */
	$User getLastLoggedInUser() {
		AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
		Account account = am.getLastLoggedInAccountBlocking();
		if (account == null) {
			account = am.getDefaultAccount();
		}
		return account == null ? null : getUser(account, true);
	}

	/**
	 * Retrieves the User object identified by the {@code account}
	 * 
	 * @param account
	 *            The Account to retrieve
	 * @return The User object or <code>null</code> if the user was not found
	 */
	$User getUser(Account account) {
		return getUser(account, false);
	}

	/**
	 * Retrieves the User object identified by the {@code account}
	 * 
	 * @param account
	 *            The Account to retrieve
	 * @param createIfNotExists
	 *            true to create the User object if it was not found
	 * @return The User object or <code>null</code> if the user was not found
	 */
	private $User getUser(Account account, boolean createIfNotExists) {
		String username = account == null ? "" : account.getId();
		return getUser(username, createIfNotExists);
	}

	/**
	 * Returns a list of blacklist items of the given {@code type} for the
	 * {@code user}
	 * 
	 * @param user
	 *            the user's whose blacklist to be retrieved. This should be the
	 *            tag that uniquely identifies the user
	 * @param type
	 *            the type of blacklist to retrieve
	 * @return A {@link CloseableIterator} of blacklist items or an {@code null}
	 *         list if there is an error
	 */
	protected CloseableIterator<Blacklist> getBlacklist($User user, String type) {
		return getBlacklist(user, type, null);
	}

	/**
	 * Returns a list of blacklist items of the given {@code type} for the
	 * {@code user}
	 * 
	 * @param user
	 *            the user's whose blacklist to be retrieved. This should be the
	 *            tag that uniquely identifies the user
	 * @param type
	 *            the type of blacklist to retrieve
	 * @param locale
	 *            the locale of the blacklist
	 * @return A {@link CloseableIterator} of blacklist items or an {@code null}
	 *         list if there is an error
	 */
	protected CloseableIterator<Blacklist> getBlacklist($User user,
			String type, Locale locale) {
		// Quick return if one of the pass in parameter is null
		if (mHelper == null || type == null || user == null) {
			return null;
		}

		CloseableIterator<Blacklist> result = null;
		try {
			ModelAttributes attrs = Schema.getAttributes(Blacklist.class);
			Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
			QueryBuilder<Blacklist, Long> qb = dao.queryBuilder();
			Where<Blacklist, Long> where = qb.where().eq(attrs.getColumnName(Blacklist.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(attrs.getColumnName(Blacklist.TYPE_FIELD_NAME), type);

			if (locale != null) {
				where.and().eq(attrs.getColumnName(Blacklist.LOCALE_FIELD_NAME), Long.valueOf(locale.getId()));
			}

			result = qb.iterator();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve " + type
					+ " type of blacklist because " + ex);
		}

		return result;
	}

	/**
	 * Synchronize local blacklist in database with server's blacklist for the
	 * given user
	 * 
	 * @param user
	 *            The user whose blacklist to synchronize with server. This
	 *            should be the id that uniquely identifies the user
	 * @param type
	 *            The type of the blacklist
	 * @param locale
	 *            The locale of the blacklist
	 */
	public void syncBlacklist(final String username, final String type,
			final String locale) {
		// Creates the message to send to remote server
		Message message = new Message(Message.PROCESS_PARENTAL, Message.OPERATION_CODE_GET_BLACKLIST);
		message.addProperty(EXTRA_BLACKLIST_TYPE, type);
		message.addProperty(EXTRA_BLACKLIST_LOCALE, locale);

		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if one of the pass in parameter is null
				if (message == null) {
					return;
				}

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
					return;
				}

				// Cannot proceed if there is user is not found. If the user is
				// not
				// found see if the given user is the current logged in user. We
				// cannot
				// proceed if the specified user can neither be located in the
				// permission
				// database nor the current logged in user
				$User user = getUser(username);
				if (user == null) {
					user = getLoggedInUser();
					String identifier = user == null ? null
							: user.getIdentifier();
					if (identifier == null || !identifier.equals(username)) {
						return;
					}
				}

				// Replaces the local blacklist with the one retrieved from
				// server
				updateLocalBacklist(user, type, locale, message);
			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
		// Retrieve permission settings from server
		sendMessage(message);
	}

	/**
	 * Retrieves the {@link Category} with the given {@code name}
	 * 
	 * @param name
	 *            the name of the category to retrieve
	 * @return the {@link Category} identified by the given {@code name}, null
	 *         if the category is not found
	 */
	public Category getCategory(String name) {
		// Quick return if the request cannot be processed
		if (name == null) {
			return null;
		}

		Category result = null;
		try {
			// Construct a query to find the {@link Category} with the given
			// {@code name}
			Dao<Category, Long> dao = mHelper.getDao(Category.class);
			QueryBuilder<Category, Long> qb = dao.queryBuilder();
			qb.where().eq(Category.NAME_FIELD_NAME, name);

			// Returns the first item in the query result
			result = dao.queryForFirst(qb.prepare());
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot find a category named " + name + " because "
					+ ex);
		}
		return result;
	}

	/**
	 * Synchronize access schedule in database with server's access schedules
	 * for the given user
	 * 
	 * @param username
	 *            The user whose access schedule to synchronize with server.
	 *            This should be that uniquely identifies the user
	 */
	public void syncAccessSchedule(final String username) {
		// TODO: retrieve permission settings via REST server
		Message message = new Message(Message.PROCESS_PARENTAL, Message.OPERATION_CODE_GET_PERMISSION);

		MessageFilter filter = new MessageFilter(message.getMessageID());
		MessageReceiver receiver = new MessageReceiver(filter) {

			@Override
			public void onReceive(Message message) {
				// Quick return if there is nothing to process
				if (message == null) {
					return;
				}

				// The request was unsuccessful
				if (message.getStatus() != Status.SUCCESS_OK) {
					Log.e(TAG, message.getMessage());
				}

				// Cannot proceed if there is user is not found. If the user is
				// not found
				// see if the given user is the current logged in user. We
				// cannot proceed
				// if the specified user can neither be located in the
				// permission database
				// nor the current logged in user
				$User user = getUser(username);
				if (user == null) {
					user = getLoggedInUser();
					String identifier = user == null ? null
							: user.getIdentifier();
					if (identifier == null || !identifier.equals(username)) {
						return;
					}
				}

				long lastLocalUpdateTime = 0;
				long lastRemoteUpdateTime = 0;
				try {
					// Retrieves the time the permission settings was last
					// updated on server
					Object lastUpdate = message.getProperty(EXTRA_PERMISSION_LAST_UPDATE);
					if (lastUpdate != null
							&& Number.class.isAssignableFrom(lastUpdate.getClass())) {
						lastRemoteUpdateTime = ((Number) lastUpdate).longValue() * 1000;
					}

					// Retrieves the time the permission settings was last
					// updated on device
					ModelAttributes attrs = Schema.getAttributes(Permission.class);
					Dao<Permission, Long> dao = mHelper.getDao(Permission.class);
					QueryBuilder<Permission, Long> qb = dao.queryBuilder();
					qb.where().eq(attrs.getColumnName(Permission.USER_FIELD_NAME), Long.valueOf(user.getId()));
					qb.orderBy(attrs.getColumnName(Model.LAST_MODIFIED_DATE_FIELD_NAME), false);
					Permission permission = qb.queryForFirst();

					if (permission != null) {
						lastLocalUpdateTime = permission.getLastModifiedDate().getTime();
					}
				} catch (Exception ex) {
					Log.e(TAG, "Cannot retrieve last modified dates of permission settings");
				}

				// Updates local permission settings if the last update time of
				// permission settings is later than or equal to the last update
				// time of local permission settings
				if (lastRemoteUpdateTime >= lastLocalUpdateTime) {
					updateLocalPermissions(user, message, lastRemoteUpdateTime);
				} else {
					updateRemotePermissions(user, lastLocalUpdateTime);
				}
			}

		};

		// Register the inline receiver to process the {@link Message}
		registerReceiver(receiver);
		// Retrieve permission settings from server
		sendMessage(message);
	}

	/**
	 * Check the item if it is in a specific blacklist
	 * 
	 * @param user
	 *            The user's blacklist to check against with.
	 * @param listType
	 *            The type of the blacklist
	 * @param item
	 *            The item that used to compare with blacklist
	 * @param localeId
	 *            The unique ID of the locale
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklist($User user, String listType, String item,
			long localeId) {
		boolean result = false;

		// Fetch the entries that match the user, listType and item in database
		// return true if exist, false if otherwise
		if (mHelper != null && user != null) {
			try {
				ModelAttributes attrs = Schema.getAttributes(Blacklist.class);
				Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
				QueryBuilder<Blacklist, Long> qb = dao.queryBuilder();
				qb.where().eq(attrs.getColumnName(Blacklist.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(attrs.getColumnName(Blacklist.TYPE_FIELD_NAME), listType).and().eq(attrs.getColumnName(Blacklist.ENTRY_FIELD_NAME), item).and().eq(attrs.getColumnName(Blacklist.LOCALE_FIELD_NAME), localeId);
				Blacklist blacklist = dao.queryForFirst(qb.prepare());
				result = blacklist != null;
			} catch (SQLException e) {
				Log.e(TAG, "Cannot determine whether or not the item exist in blacklist because "
						+ e + " occurred");
			}

		}

		return result;
	}

	/**
	 * Check the item if it is in a specific blacklist
	 * 
	 * @param user
	 *            The user's blacklist to check against with. This should be
	 *            that uniquely identifies the user
	 * @param listType
	 *            The type of the blacklist
	 * @param item
	 *            The item that used to compare with blacklist
	 * @param localeId
	 *            The unique ID of the locale
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklist(String user, String listType, String item,
			long localeId) {
		// Retrieves the User object
		$User usr = getUser(user);
		return isItemInBlacklist(usr, listType, item, localeId);
	}

	/**
	 * Check the item if it is in a specific blacklist
	 * 
	 * @param user
	 *            The user's blacklist to check against with. This should be
	 *            that uniquely identifies the user
	 * @param listType
	 *            The type of the blacklist
	 * @param item
	 *            The item that used to compare with blacklist
	 * @param locale
	 *            The {@link Locale} of the item
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklist(String user, String listType, String item,
			Locale locale) {
		return isItemInBlacklist(user, listType, item, locale == null ? 0
				: locale.getId());
	}

	/**
	 * Determines whether or not the given blacklist item already exist for user
	 * 
	 * @param item
	 *            the blacklist item to verify
	 * @return true if the item already exists, false otherwise
	 */
	public boolean isItemInBlacklist(Blacklist item) {
		return isItemInBlacklist(item == null ? null : item.getUser(), item == null ? ""
				: item.getType(), item == null ? "" : item.getEntry(), item == null ? 0
				: item.getLocale() == null ? 0 : item.getLocale().getId());
	}

	/**
	 * Determines whether or not the given blacklist item already exist for user
	 * 
	 * @param user
	 *            the user whose blacklist item to verify. This should be that
	 *            uniquely identifies the user
	 * @param item
	 *            the blacklist item to verify
	 * @return true if the item already exists, false otherwise
	 */
	public boolean isItemInBlacklist(String user, Blacklist item) {
		return isItemInBlacklist(user, item == null ? "" : item.getType(), item == null ? ""
				: item.getEntry(), item == null ? 0
				: item.getLocale() == null ? 0 : item.getLocale().getId());
	}

	/**
	 * Check the word if it is in the bad word list
	 * 
	 * @param user
	 *            The user's bad word list to check against with. This should be
	 *            that uniquely identifies the user
	 * @param word
	 *            The word that used to compare with the bad word list
	 * @return true if the bad word list contains the word, false if otherwise
	 */
	public boolean isBadword(String user, String word) {
		// Retrieves the User object
		$User usr = getUser(user);
		return isBadword(usr, word);
	}

	/**
	 * Check the word if it is in the bad word list
	 * 
	 * @param user
	 *            The user's bad word list to check against with.
	 * @param word
	 *            The word that used to compare with the bad word list
	 * @return true if the bad word list contains the word, false if otherwise
	 */
	public boolean isBadword($User user, String word) {
		boolean result = false;

		// Fetch the entries that match the user, word, return true if exist,
		// false if otherwise
		if (mHelper != null && user != null) {
			try {
				ModelAttributes attrs = Schema.getAttributes(Blacklist.class);
				Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
				QueryBuilder<Blacklist, Long> qb = dao.queryBuilder();
				qb.where().eq(attrs.getColumnName(Blacklist.USER_FIELD_NAME), Long.valueOf(user.getId())).and().eq(attrs.getColumnName(Blacklist.ENTRY_FIELD_NAME), word).and().eq(attrs.getColumnName(Blacklist.TYPE_FIELD_NAME), PermissionManager.BLACKLIST_TYPE_KEYWORD);
				Blacklist item = dao.queryForFirst(qb.prepare());
				result = item != null;
			} catch (SQLException e) {
				Log.e(TAG, "Cannot determine whether or not " + word
						+ " exist in list of bad words because " + e
						+ " occurred");
			}

		}

		return result;
	}

	/**
	 * Determines whether or not the given {@code permission} exist for
	 * {@code user}
	 * 
	 * @param user
	 *            The user to determine whether permission setting exist. This
	 *            should be that uniquely identifies the user
	 * @param permission
	 *            the permission setting to verify
	 * @return true if the permission setting exists, false otherwise
	 */
	public boolean isPermissionExist(String user, Permission permission) {
		// Retrieves the User object
		$User usr = getUser(user);
		return isPermissionExist(usr, permission);
	}

	/**
	 * Determines whether or not the given {@code permission} exist for
	 * {@code user}
	 * 
	 * @param user
	 *            The user to determine whether permission setting exist.
	 * @param permission
	 *            the permission setting to verify
	 * @return true if the permission setting exists, false otherwise
	 */
	public boolean isPermissionExist($User user, Permission permission) {
		boolean result = false;

		// Retrieve a Permission object matching the attributes
		// of the permission object provided
		if (mHelper != null && user != null && permission != null) {
			try {
				ModelAttributes attrs = Schema.getAttributes(Permission.class);
				Dao<Permission, Long> dao = mHelper.getDao(Permission.class);
				QueryBuilder<Permission, Long> qb = dao.queryBuilder();
				qb.where().eq(attrs.getColumnName(Permission.ACCESS_FIELD_NAME), permission.getAccessLevel().toString()).and().eq(attrs.getColumnName(Permission.TIME_LIMIT_SERIALIZED_NAME), permission.getTimeLimit()).and().eq(attrs.getColumnName(Permission.COMPONENT_FIELD_NAME), Long.valueOf(permission.getComponent().getId())).and().eq(attrs.getColumnName(Permission.USER_FIELD_NAME), Long.valueOf(user.getId()));
				Permission existingItem = qb.queryForFirst();
				result = existingItem != null;
			} catch (SQLException ex) {
				Log.e(TAG, "Cannot determine whether permission setting exist for user because "
						+ ex + " occurred");
			}
		}

		return result;
	}

	/**
	 * Returns a list of {@link Component} in the {@link Category} identified by
	 * {@code categoryName}
	 * 
	 * @param username
	 *            the unique identifier of the user who's component list to
	 *            return. This should be the MEEP tag of the user
	 * @param categoryName
	 *            the name of the category to return. This should be
	 *            {@link Category#CATEGORY_BLACKLIST} or
	 *            {@link Category#CATEGORY_GAMES}
	 * @return a list of {@link Component} in the {@link Category} or
	 *         {@code null} if the category specified is not found
	 */
	public List<Component> getComponents(String username, String categoryName) {
		$User user = getUser(username);
		return getComponents(user, categoryName);
	}

	/**
	 * Returns a list of {@link Component} in the {@link Category} identified by
	 * {@code categoryName}
	 * 
	 * @param username
	 *            the unique identifier of the user who's component list to
	 *            return. This should be the MEEP tag of the user
	 * @param categoryName
	 *            the name of the category to return. This should be
	 *            {@link Category#CATEGORY_BLACKLIST} or
	 *            {@link Category#CATEGORY_GAMES}
	 * @return a list of {@link Component} in the {@link Category} or
	 *         {@code null} if the category specified is not found
	 */
	public List<Component> getComponents($User user, String categoryName) {
		List<Component> result = null;

		// Quick return if the request cannot be processed
		if (user == null) {
			return result;
		}

		// We cannot proceed if the category specified is not found
		Category category = getCategory(categoryName);
		if (category == null) {
			return result;
		}

		List<Component> components = null;
		CloseableIterator<ComponentCategory> iterator = category.getComponentCategories().closeableIterator();
		try {
			while (iterator.hasNext()) {
				Component component = iterator.next().getComponent();
				component.refresh();

				// Adds the {@link Component} to the result list
				if (components == null) {
					components = new ArrayList<Component>();
				}
				components.add(component);
			}

			// Return the result
			result = components;
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve components in " + categoryName
					+ " because " + ex);
		} finally {
			try {
				iterator.close();
			} catch (SQLException ignored) {
				// Ignored
			}
		}

		return result;
	}

	/**
	 * Retrieves Component of the given {@code name}
	 * 
	 * @param name
	 *            The class name of the component
	 * @return The Component object if it was found, null otherwise
	 */
	public Component getComponent(String name) {
		return getComponent(Component.NAME_FIELD_NAME, name);
	}

	/**
	 * Retrieves Component with the field {@code fieldName} equals to
	 * {@code value}
	 * 
	 * @param fieldName
	 *            The name of the field to match against
	 * @param value
	 *            The value of the field
	 * @return The Component object if it was found, null otherwise
	 */
	public Component getComponent(String fieldName, String value) {
		return getComponent(fieldName, value, false);
	}

	/**
	 * Retrieves Component with the field {@code fieldName} equals to
	 * {@code value}. This method will also create the {@link Component} if the
	 * no entry matching the given criteria is found and
	 * {@code createIfNotExist} is set to true
	 * 
	 * @param fieldName
	 *            The name of the field to match against
	 * @param value
	 *            The value of the field
	 * @param createIfNotExist
	 *            a boolean that indicates whether or not a {@link Component}
	 *            should be created if no {@link Component} match the given
	 *            criteria
	 * @return The Component object if it was found, null otherwise
	 */
	public Component getComponent(String fieldName, String value,
			boolean createIfNotExist) {
		// Quick return if component cannot be retrieved
		if (fieldName == null || mHelper == null) {
			return null;
		}

		Component result = null;
		try {
			// Construct a query to find the {@link Component} that matches the
			// given criteria
			Dao<Component, Long> dao = mHelper.getDao(Component.class);
			QueryBuilder<Component, Long> qb = dao.queryBuilder();
			qb.where().eq(fieldName, value);
			result = dao.queryForFirst(qb.prepare());

			if (result == null && createIfNotExist) {
				result = new Component();
				result.setFieldValue(fieldName, value);
				dao.create(result);
			}
		} catch (SQLException e) {
			// Cannot retrieve the component named {@code name}
			Log.e(TAG, "Cannot retrieve component with " + fieldName
					+ " that equals to " + value == null ? "" : value);
		}
		return result;
	}

	/**
	 * Checks whether access to the service should be granted to the given
	 * {@code identity}
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @return true if access should be granted, false otherwise
	 */
	private boolean checkGuard(Identity identity) {
		// TODO: authenticate with {@link AccountManager}
		return true;
	}

	/**
	 * Determines whether or not the given {@code user} is given access to the
	 * {@code component}
	 * 
	 * @param user
	 *            the user to determine whether s/he should be given the
	 *            permission. This should be that uniquely identifies the user
	 * @param component
	 *            the discrete component to determine the permission setting
	 * @param timeElapsed
	 *            the amount of time, in milliseconds, to add to the total
	 *            accumulated running time for the component to determine the
	 *            component's accessibility
	 * @return {@link #FLAG_PERMISSION_OK} if {@code user} is given access to
	 *         the {@code component}, {@link #FLAG_PERMISSION_TIMEOUT} or
	 *         {@link #FLAG_PERMISSION_DENIED} otherwise
	 */
	public int isAccessible($User user, Component component, long timeElapsed) {
		// Quick return if one of the pass in parameter is null
		if (component == null) {
			return PermissionManager.FLAG_PERMISSION_OK;
		}

		// The component is not accessible if it is one of the component
		// in the blocked category
		if (component.inCategory(Category.CATEGORY_BLACKLIST)) {
			return PermissionManager.FLAG_PERMISSION_DENIED;
		}

		// The component is accessible albeit it should be hidden from users if
		// it
		// existed in the ignored list. Note that the ignored list is composed
		// of
		// the blacklist list. That is, the ignored list contains packages
		// defined
		// in the blacklist list.
		//
		// To ensure packages in the blacklist is inaccessible, the check for
		// blacklist
		// MUST precede the check for ignored list or hidden list
		//
		// @see Category#CATEGORY_IGNORED
		if (component.inCategory(Category.CATEGORY_IGNORED)) {
			return PermissionManager.FLAG_PERMISSION_OK;
		}

		// Fetch the entries that match the user, word, return true if exist,
		// false if otherwise
		Permission permission = user == null ? null
				: user.getPermission(component);

		// A discrete component is accessible by default
		if (permission == null) {
			return PermissionManager.FLAG_PERMISSION_OK;
		}

		if (!permission.getCanAccess()) {
			return PermissionManager.FLAG_PERMISSION_DENIED;
		}

		// Time limit special cases: 0 or 1 day
		long limit = permission.getTimeLimit();
		if (limit == 0 || limit >= 86400000) {
			return PermissionManager.FLAG_PERMISSION_OK;
		}

		// Reads current configuration settings
		android.content.res.Configuration config = new android.content.res.Configuration();
		Settings.System.getConfiguration(getContext().getContentResolver(), config);

		Date systemDate = Calendar.getInstance(config.locale).getTime();
		Date lastActiveDate = null;
		long accumulatedActiveTime = 0;
		try {
			lastActiveDate = permission.getComponent().getHistory(permission.getUser()).getLastActiveDate();
			accumulatedActiveTime = permission.getComponent().getHistory(permission.getUser()).getAccumulatedActiveTime().longValue();
		} catch (NullPointerException ex) {
			// The component had never been active. Ignore
		}

		// If the component was never launched, set the date to the present date
		if (lastActiveDate == null) {
			lastActiveDate = Calendar.getInstance(config.locale).getTime();
		}

		int result = PermissionManager.FLAG_PERMISSION_OK;
		if (DateUtils.dayDifference(systemDate, lastActiveDate) == 0) {
			result = accumulatedActiveTime + timeElapsed < permission.getTimeLimit() ? PermissionManager.FLAG_PERMISSION_OK
					: PermissionManager.FLAG_PERMISSION_TIMEOUT;
		} else if (DateUtils.compare(systemDate, lastActiveDate) == DateUtils.AFTER) {
			// The current date is later than the last active date of the
			// component
			// Package is allowed to run the package since the accumulated
			// running
			// time should be reset
			result = PermissionManager.FLAG_PERMISSION_OK;
		} else if (DateUtils.compare(systemDate, lastActiveDate) == DateUtils.BEFORE) {
			// The last time that the app was launched should not be later
			// than the system date. The user may have tempered with the
			// configuration
			// Therefore, permission is violated
			result = PermissionManager.FLAG_PERMISSION_TIMEOUT;
		}

		return result;
	}

	/**
	 * Determines whether or not the given {@code user} is given access to the
	 * {@code component}
	 * 
	 * @param user
	 *            the user to determine whether s/he should be given the
	 *            permission. This should be that uniquely identifies the user
	 * @param component
	 *            the discrete component to determine the permission setting
	 * @param timeElapsed
	 *            the amount of time, in milliseconds, to add to the total
	 *            accumulated running time for the component to determine the
	 *            component's accessibility
	 * @return {@link #FLAG_PERMISSION_OK} if {@code user} is given access to
	 *         the {@code component}, {@link #FLAG_PERMISSION_TIMEOUT} or
	 *         {@link #FLAG_PERMISSION_DENIED} otherwise
	 */
	public int isAccessible(String user, Component component, long timeElapsed) {
		$User targetUser = getUser(user);
		return isAccessible(targetUser, component, timeElapsed);
	}

	/**
	 * Determines whether or not the given {@code user} is allowed to access
	 * {@code component} after adding {@code timeElapsed} to the accumulated
	 * active time to the component
	 * 
	 * @param user
	 *            the user to determine whether s/he should be given the
	 *            permission. This should be that uniquely identifies the user
	 * @param component
	 *            the discrete component to determine the permission setting
	 * @param timeElapsed
	 *            the amount of time, in milliseconds, to add to the total
	 *            accumulated running time for the component to determine the
	 *            component's accessibility
	 * @return {@link #FLAG_PERMISSION_OK} if {@code user} is given access to
	 *         the {@code component}, {@link #FLAG_PERMISSION_TIMEOUT} or
	 *         {@link #FLAG_PERMISSION_DENIED} otherwise
	 */
	public int isAccessible(String user, ComponentName component,
			long timeElapsed) {
		// Quick return if one of the pass in parameter is null
		if (component == null) {
			return PermissionManager.FLAG_PERMISSION_OK;
		}

		Component c = getComponent(component.getPackageName());
		return isAccessible(user, c, timeElapsed);
	}

	/**
	 * Determines whether or not the given component is accessible
	 * 
	 * @param user
	 *            The user whose access schedule to check against with. This
	 *            should be that uniquely identifies the user
	 * @param component
	 *            The component name to check
	 * @return {@link #FLAG_PERMISSION_OK} if {@code user} is given access to
	 *         the {@code component}, {@link #FLAG_PERMISSION_TIMEOUT} or
	 *         {@link #FLAG_PERMISSION_DENIED} otherwise
	 */
	public int isAccessible(String user, ComponentName component) {
		return isAccessible(user, component, 0);
	}

	/**
	 * Replaces {@link Component} in the given {@link Category}
	 * 
	 * @param categoryName
	 *            the name of the category
	 * @param components
	 *            an Array of component names in the given category
	 */
	public void setComponentsInCategory(String categoryName,
			final List<String> components) {
		// Quick return if there is nothing to process
		if (categoryName == null) {
			return;
		}

		final Category category = getCategory(categoryName);
		// We cannot proceed if the category is not found
		if (category == null) {
			return;
		}

		try {
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					Dao<ComponentCategory, Long> dao = mHelper.getDao(ComponentCategory.class);

					// Deletes components that do not exist in the {@code
					// componentNames}
					// in {@link ComponentCategory}
					CloseableIterator<ComponentCategory> iterator = category.getComponentCategories().closeableIterator();
					try {
						while (iterator.hasNext()) {
							ComponentCategory componentCategory = iterator.next();
							Component component = componentCategory.getComponent();
							component.refresh();

							if (!components.contains(component.getName())) {
								iterator.remove();
							}
						}
					} finally {
						try {
							iterator.close();
						} catch (SQLException ex) {
							// Ignore
						}
					}

					// Creates the {@link Component} and associate it with the
					// {@link Category} if it
					// does not already exist
					for (String name : components) {
						Component component = getComponent(Component.NAME_FIELD_NAME, name, true);
						if (!category.hasComponent(component)) {
							ComponentCategory componentCategory = new ComponentCategory(component, category);
							dao.create(componentCategory);
						}
					}

					return null;
				}

			});
		} catch (Exception ex) {
			Log.e(TAG, "Failed to update components in " + categoryName
					+ " because " + ex);
		}
	}

	/**
	 * Replace the permissions for each package. All existing entries in the
	 * local data store will be removed and replaced with {@code permissions}
	 * 
	 * <h2>Note</h2> The operation can be lock access to the underlying data
	 * store for a long period of time if the list is large. Use this function
	 * is caution
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param username
	 *            The user the permission schedule to apply to
	 * @param permissions
	 *            An list of Permission objects that contains every package's
	 *            permission
	 */
	public void setAccessSchedule(Identity identity, String username,
			final List<Permission> permissions) {
		// Quick return if one of the pass in parameter is null
		if (mHelper == null || username == null || permissions == null
				|| !checkGuard(identity)) {
			return;
		}

		// Iterate through the incoming permission and compare with entries in
		// database
		// If the entry already exists, update it, if not, insert it into the
		// database
		// Delete if it does not exist in incoming permissions
		try {
			final $User user = getUser(username, true);
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					Dao<Permission, Long> dao = mHelper.getDao(Permission.class);

					// Deletes the permission setting in database that do not
					// exist in the new
					// list
					CloseableIterator<Permission> iterator = user.getPermissions().closeableIterator();
					try {
						while (iterator.hasNext()) {
							Permission permission = iterator.next();

							// Need to refresh the component first before the
							// comparison can be made
							Component component = permission.getComponent();
							if (component != null) {
								component.refresh();
							}

							if (!permissions.contains(permission)) {
								permission.delete();
							}
						}
					} finally {
						iterator.close();
					}

					for (Permission permission : permissions) {
						// Creates the bad word if it does not already exist
						if (!isPermissionExist(user, permission)) {
							dao.create(permission);
						} else {
							dao.update(permission);
						}
					}

					return null;
				}

			});
		} catch (Exception e) {
			Log.e(TAG, "Cannot update permission settings because " + e
					+ " occurred");
		}
	}

	/**
	 * Update the given permission for the given user
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param username
	 *            The user to set permission for
	 * @param permission
	 *            The permission to update
	 */
	public void updatePermission(Identity identity, String username,
			Permission permission) {
		// Quick return if one of the pass in parameter is null
		if (!checkGuard(identity)) {
			return;
		}

		$User user = getUser(username);
		updatePermission(user, permission);
	}

	private void updatePermission($User user, Permission permission) {
		// Quick return if the request cannot be processed
		if (mHelper == null || user == null || permission == null) {
			return;
		}

		try {
			Dao<Permission, Long> dao = mHelper.getDao(Permission.class);
			permission.setUser(user);

			long id = dao.extractId(permission);
			if (dao.idExists(id)) {
				Permission per = dao.queryForId(id);
				permission.setLastModifiedDate(per.getLastModifiedDate());
				dao.update(permission);
			} else {
				dao.create(permission);
			}
		} catch (Exception e) {
			// Failed to update the permission.
			Log.e(TAG, "Cannot update " + permission + " because " + e);
		}
	}

	/**
	 * Replace the blacklist in local data store with {@code blacklist}. All
	 * existing entries in the local data store will be removed and replaced
	 * with {@code blacklist}
	 * 
	 * <h2>Note</h2> The operation can lock access to the underlying data store
	 * for a long period of time if the list is large. Use this function is
	 * caution
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param helper
	 *            The database helper for database operations
	 * @param username
	 *            The user's blacklist to replace
	 * @param blacklists
	 *            An list of Blacklist Objects which is the entire blacklist
	 */
	public void setBlacklist(Identity identity, String username,
			final List<Blacklist> blacklists) {
		// Quick return if one of the pass in parameter is null or if the
		// given {@code identity} is not authenticated to perform the action
		if (username == null || !checkGuard(identity)) {
			return;
		}

		// Cannot proceed if the is user is not found. If the user is not
		// found, see if the given user is the current logged in user. We cannot
		// proceed if the specified user can neither be located in the
		// permission
		// database nor the current logged in user
		$User user = getUser(username);
		if (user == null) {
			user = getLoggedInUser();
			String identifier = user == null ? null : user.getIdentifier();
			if (identifier == null || !username.equals(identifier)) {
				return;
			}
		}
		setBlacklist(user, blacklists);
	}

	private void setBlacklist(final $User user, final String type,
			final String localeString, final Iterable<?> items) {
		// Quick return if one of the pass in parameter is null
		if (mHelper == null || user == null || type == null) {
			return;
		}

		try {
			final Locale locale = getLocale(localeString);

			// Remove the Aho-Corasick search tree if the type of blacklist is
			// {@link PermissionManager#BLACKLIST_TYPE_BAD_WORD}
			if (PermissionManager.BLACKLIST_TYPE_KEYWORD.equals(type)) {
				removeTreeFromMap(user.getIdentifier(), locale);
			}

			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					ModelAttributes attrs = Schema.getAttributes(Blacklist.class);
					Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);

					// Delete items in table "blacklist" that do not present in
					// the
					// {@code items} String array
					DeleteBuilder<Blacklist, Long> db = dao.deleteBuilder();
					Where<Blacklist, Long> where = db.where().eq(attrs.getColumnName(Blacklist.TYPE_FIELD_NAME), type).and().eq(attrs.getColumnName(Blacklist.USER_FIELD_NAME), Long.valueOf(user.getId()));

					if (locale != null) {
						where = where.and().eq(attrs.getColumnName(Blacklist.LOCALE_FIELD_NAME), Long.valueOf(locale.getId()));
					}
					if (items != null) {
						where.and().notIn(attrs.getColumnName(Blacklist.ENTRY_FIELD_NAME), items);
					}

					db.delete();

					if (items != null) {
						for (Object item : items) {
							if (String.class.isAssignableFrom(item.getClass())) {
								Blacklist blacklist = new Blacklist(user, type);
								blacklist.setLocale(locale);
								blacklist.setEntry((String) item);

								// Creates the bad word if it does not already
								// exist
								if (!isItemInBlacklist(blacklist)) {
									dao.create(blacklist);
								}
							}
						}
					}

					return null;
				}

			});
		} catch (Exception ex) {
			Log.e(TAG, "Cannot update " + type + " type of blacklist because "
					+ ex);
		}
	}

	private void setBlacklist(final $User user, final List<Blacklist> blacklists) {
		// Quick return if one of the pass in parameter is null
		if (mHelper == null || user == null) {
			return;
		}

		// Iterate the incoming blacklist and compare with db's entries one by
		// one
		// if the entry exists, update it, if not, insert into it
		// delete if it does not exist in incoming blacklists
		try {
			TransactionManager.callInTransaction(mHelper.getConnectionSource(), new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
					boolean changed = false;

					// Deletes the blacklist items in database that do not exist
					// in the new
					// list
					CloseableIterator<Blacklist> iterator = user.getBlacklists().closeableIterator();
					try {
						while (iterator.hasNext()) {
							Blacklist item = iterator.next();
							boolean containItem = blacklists == null ? false
									: blacklists.contains(item);
							if (!containItem) {
								item.delete();
								changed = PermissionManager.BLACKLIST_TYPE_KEYWORD.equals(item.getType())
										|| changed;
							}
						}
					} finally {
						iterator.close();
					}

					// Adds the item into blacklist if it was not already in the
					// blacklist
					if (blacklists != null) {
						for (Blacklist item : blacklists) {
							// Creates the bad word if it does not already exist
							item.setUser(user);
							if (!isItemInBlacklist(item)) {
								dao.create(item);
								changed = PermissionManager.BLACKLIST_TYPE_KEYWORD.equals(item.getType())
										|| changed;
							} else {
								int result = dao.update(item);
								changed = (PermissionManager.BLACKLIST_TYPE_KEYWORD.equals(item.getType()) && result == 1)
										|| changed;
							}
						}
					}

					// Remove the Aho-Corasick search tree if the type of
					// blacklist is
					// {@link PermissionManager#BLACKLIST_TYPE_BAD_WORD}
					if (changed) {
						removeTreesFromMap(user.getIdentifier());
					}

					return null;
				}

			});
		} catch (Exception ex) {
			// Error updating the blacklist. Ignore
			Log.e(TAG, "Cannot update blacklist because " + ex + " occurred");
		}
	}

	/**
	 * Add a blacklist item into the local blacklist
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param user
	 *            The user whose blacklist to apply to
	 * @param item
	 *            A blacklist item to be added into the local blacklist
	 * @return true if the item is successfully added to local store, false if
	 *         the item cannot be added or the item already exist in the data
	 *         store
	 */
	public boolean addBlacklistItem(Identity identity, String user,
			Blacklist item) {
		// Quick return if execute cannot proceed
		if (mHelper == null || item == null || user == null
				|| !checkGuard(identity)) {
			return false;
		}

		boolean result = false;
		$User targetUser = getUser(user);

		// Fetch from the database to determine if the item already exist in
		// the blacklist
		try {
			if (isItemInBlacklist(user, item)) {
				// Cannot add the item because it already exist in the database
				result = false;
			} else {
				Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
				// Insert the item into database
				item.setUser(targetUser);
				item.setDao(dao);
				item.create();

				// Remove the Aho-Corasick search tree if the type of blacklist
				// is
				// {@link PermissionManager#BLACKLIST_TYPE_BAD_WORD}
				if (PermissionManager.BLACKLIST_TYPE_KEYWORD.equals(item.getType())) {
					removeTreeFromMap(targetUser.getIdentifier(), item.getLocale());
				}

				result = true;
			}
		} catch (Exception e) {
			// Cannot add the item. Ignore
			Log.e(TAG, "Cannot add " + item + " to blacklist because " + e
					+ " occurred");
		}

		return result;
	}

	/**
	 * Remove blacklist item from the local blacklist
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param user
	 *            The user whose blacklist to apply to
	 * @param item
	 *            A blacklist item to be removed from the local blacklist
	 * @return true if the item is successfully removed from local store, false
	 *         if the item cannot be removed or the item does not exist in the
	 *         data store
	 */
	public boolean removeBlacklistItem(Identity identity, String user,
			Blacklist item) {
		// Quick return if execute cannot proceed
		if (mHelper == null || item == null || user == null
				|| !checkGuard(identity)) {
			return false;
		}

		boolean result = false;
		$User targetUser = getUser(user);

		if (targetUser != null) {
			try {
				item.setUser(targetUser);
				if (isItemInBlacklist(item)) {
					// Deletes the blacklist item
					ModelAttributes attrs = Schema.getAttributes(Blacklist.class);
					Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
					DeleteBuilder<Blacklist, Long> db = dao.deleteBuilder();
					db.where().eq(attrs.getColumnName(Blacklist.USER_FIELD_NAME), Long.valueOf(targetUser.getId())).and().eq(attrs.getColumnName(Blacklist.ENTRY_FIELD_NAME), item.getEntry()).and().eq(attrs.getColumnName(Blacklist.TYPE_FIELD_NAME), item.getType());
					db.delete();

					// Remove the Aho-Corasick search tree if the type of
					// blacklist is
					// {@link PermissionManager#BLACKLIST_TYPE_BAD_WORD}
					if (PermissionManager.BLACKLIST_TYPE_KEYWORD.equals(item.getType())) {
						removeTreeFromMap(targetUser.getIdentifier(), item.getLocale());
					}
				} else {
					// The given blacklist item does not exist
					result = false;
				}
			} catch (Exception e) {
				// Cannot delete the item. Ignore
				Log.e(TAG, "Cannot delete " + item + " from blacklist because "
						+ e + " occurred");
			}
		}

		return result;
	}

	/**
	 * Returns an {@link AhoCorasick} search tree for the given {@code locale}
	 * 
	 * @param user
	 *            The user whose blacklist is to be used to construct the tree
	 * @param locale
	 *            The locale of the blacklist
	 * @return An {@code AhoCorasick} search tree
	 */
	private AhoCorasick getTree(final $User user, final Locale locale) {
		// Quick return if the request cannot be processed
		if (user == null || user.getDao() == null
				|| user.getIdentifier() == null) {
			return null;
		}

		FutureTask<AhoCorasick> task = new FutureTask<AhoCorasick>(new Callable<AhoCorasick>() {

			@Override
			public AhoCorasick call() throws Exception {
				Map<Locale, AhoCorasick> trees = lookupTrees(user.getIdentifier());
				AhoCorasick tree = trees == null ? null : trees.get(locale);

				if (tree != null) {
					return tree;
				}

				createAndroidNotification(ANDROID_NOTIFICATION_BUILDING_TREE_ID);

				// Adds user specific bad words to the tree
				CloseableIterator<Blacklist> iterator = getBlacklist(user, PermissionManager.BLACKLIST_TYPE_KEYWORD, locale);
				tree = getAhoCorasickTree(iterator);

				// Caches the AhoCorasick search tree
				if (tree != null) {
					addTreeToMap(user.getIdentifier(), locale, tree);
				}

				removeAndroidNotification(ANDROID_NOTIFICATION_BUILDING_TREE_ID);

				return tree;
			}

		});

		// If blacklist for the given user is still in the process of creating,
		// return the tree only after the blacklist is created
		synchronized (mLock) {
			if (mRunningTasks.contains(user)) {
				List<FutureTask<?>> tasks = mTasks.get(user);
				if (tasks == null) {
					tasks = new ArrayList<FutureTask<?>>();
				}
				tasks.add(task);
				mTasks.put(user, tasks);
			} else {
				task.run();
			}
		}

		AhoCorasick result = null;
		try {
			result = task.get();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve Aho-Corasick search tree for " + user
					+ " because " + ex);
		}
		return result;
	}

	/**
	 * Creates a default blacklist for the given {@code user}
	 * 
	 * @param resId
	 *            the resource identifier
	 * @param user
	 *            the user to create blacklist for
	 */
	private void createDefaultBlacklist(final int resId, final $User user) {
		// Quick return if the request cannot be processed
		synchronized (mLock) {
			if (user == null || mRunningTasks.contains(user)) {
				return;
			}

			Log.v(TAG, "Creating default blacklist...");
			mRunningTasks.add(user);
		}

		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				Map<String, Locale> locales = new HashMap<String, Locale>();
				XmlResourceParser xrp = null;

				try {
					Dao<Blacklist, Long> dao = mHelper.getDao(Blacklist.class);
					xrp = getContext().getResources().getXml(resId);

					// Parse the XML document
					xrp.next();
					int eventType = xrp.getEventType();
					String type = null, localeString = null, blacklistType = null, value = null;
					Locale locale = null;

					while (eventType != XmlPullParser.END_DOCUMENT) {
						switch (eventType) {
						case XmlPullParser.START_DOCUMENT:
							// We ignore this tag
							break;
						case XmlPullParser.START_TAG:
							// Found the "blacklist" XML tag
							if (TAG_BLACKLIST.equalsIgnoreCase(xrp.getName())) {
								type = localeString = blacklistType = value = null;
								locale = null;
								int attributeCount = xrp.getAttributeCount();

								// Retrieve attributes for XML tag
								for (int i = 0; i < attributeCount; i++) {
									String attributeName = xrp.getAttributeName(i);
									if (ATTR_TYPE.equalsIgnoreCase(attributeName)) {
										type = xrp.getAttributeValue(i);
									} else if (ATTR_LOCALE.equalsIgnoreCase(attributeName)) {
										localeString = xrp.getAttributeValue(i);
									}
								}

								// Retrieves the {@link Locale} object
								// identified by the locale attribute
								if (localeString != null) {
									locale = locales.get(localeString);
									if (locale == null) {
										locale = getLocale(localeString);
										locales.put(localeString, locale);
									}
								}

								// Currently, we only support 2 types of
								// blacklist
								if (PermissionManager.BLACKLIST_TYPE_KEYWORD.equalsIgnoreCase(type)) {
									blacklistType = PermissionManager.BLACKLIST_TYPE_KEYWORD;
								} else if (PermissionManager.BLACKLIST_TYPE_BROWSER.equalsIgnoreCase(type)) {
									blacklistType = PermissionManager.BLACKLIST_TYPE_BROWSER;
								}
							}
							break;
						case XmlPullParser.TEXT:
							value = xrp.getText();
							break;
						case XmlPullParser.END_TAG:
							if (TAG_BLACKLIST.equalsIgnoreCase(xrp.getName())) {
								if (blacklistType != null && value != null
										&& value.length() > 0) {
									Blacklist blacklist = new Blacklist(user, blacklistType, value);
									blacklist.setLocale(locale);
									dao.create(blacklist);
								}
							}
							break;
						}

						eventType = xrp.next();
					}

					Log.v(TAG, "Added default bad words for " + user);
				} catch (Exception e) {
					Log.e(TAG, "Cannot add bad words for " + user + " because "
							+ e);
				} finally {
					// Closes the parser
					if (xrp != null) {
						xrp.close();
					}

					synchronized (mLock) {
						// The task is now completed
						mRunningTasks.remove(user);

						// Runs pending tasks
						List<FutureTask<?>> tasks = mTasks.get(user);
						if (tasks != null) {
							for (FutureTask<?> task : tasks) {
								task.run();
							}
						}
						mTasks.remove(user);
					}
				}
			}

		});
	}

	/**
	 * Returns a {@link AhoCorasick} search tree created from the given
	 * {@code iterator}
	 * 
	 * @param iterator
	 *            An {@link CloseableIterator} of {@link Blacklist}
	 * @return An {@link AhoCorasick} search tree
	 */
	private AhoCorasick getAhoCorasickTree(CloseableIterator<Blacklist> iterator) {
		// Quick return if there is nothing to process
		if (iterator == null) {
			return null;
		}

		AhoCorasick result = null;
		try {
			result = new AhoCorasick();
			while (iterator.hasNext()) {
				Blacklist item = iterator.next();

				String w = item.getEntry().toLowerCase();
				result.add(w.getBytes(), w);
			}
		} catch (Exception ex) {
			result = null;
			Log.e(TAG, "Cannot create Aho-Corasick search tree because " + ex);
		} finally {
			// Closes the iterator
			try {
				iterator.close();
			} catch (SQLException e) {
				Log.e(TAG, "Cannot close the bad word list iterator while building the Aho-Corascik search tree because "
						+ e);
			}

			// Prepares the AhoCorasick search tree
			if (result != null) {
				result.prepare();
			}
		}
		return result;
	}

	/**
	 * Determine whether or not the given character sequence contains a bad word
	 * as defined in the {@code user}'s bad word list
	 * 
	 * @param username
	 *            The user whose bad word list to check against
	 * @param str
	 *            The character sequence to verify
	 * @return true if any of the word in the given character sequence contains
	 *         a word that is in the list of banned words, false otherwise
	 */
	public boolean containsBadword(String username, String str) {
		boolean result = false;

		try {
			Detector detector = DetectorFactory.create();
			detector.append(str);
			String language = detector.detect();

			if (language != null) {
				Log.e(TAG, "The language of " + str + " is detected to be: "
						+ language);
			}

			java.util.Locale locale = getContext().getResources().getConfiguration().locale;
			Locale currentLocale = getLocale(locale.getLanguage());
			Locale englishLocale = getLocale(Locale.LOCALE_ENGLISH);
			Locale defaultLocale = getLocale(Locale.LOCALE_DEFAULT);
			Locale textLocale = language == null ? null : getLocale(language);

			$User user = getUser(username);
			boolean performFiltering = getShouldPerformBadwordFiltering(user, true);

			if (str != null) {
				Iterator<?> itr = searchText(getTree(user, defaultLocale), str, defaultLocale == null ? false
						: defaultLocale.getExactMatch());
				result = itr.hasNext();

				if (!result && performFiltering) {
					itr = searchText(getTree(user, englishLocale), str, englishLocale == null ? true
							: englishLocale.getExactMatch());
					result = itr.hasNext();

					if (!result && currentLocale != null
							&& !currentLocale.equals(englishLocale)) {
						itr = searchText(getTree(user, currentLocale), str, currentLocale.getExactMatch());
						result = itr.hasNext();
					}

					if (!result && textLocale != null
							&& !textLocale.equals(englishLocale)
							&& !textLocale.equals(currentLocale)) {
						itr = searchText(getTree(user, textLocale), str, textLocale.getExactMatch());
						result = itr.hasNext();
					}
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot determine whether " + str
					+ " contains blacklisted words because " + ex);
		}

		return result;
	}

	/**
	 * Replaces occurrences of blacklisted words in the given passage with
	 * {@code replacement}
	 * 
	 * @param username
	 *            The unique ID of the user whose blacklist to search against
	 * @param str
	 *            The passage to replace backlisted words
	 * @param replacement
	 *            The replacement of blacklisted words
	 * @return A string with backlisted words replaced with {@code replacement}
	 */
	public String replaceBadwords(String username, String str,
			String replacement) {
		String result = str;

		try {
			Detector detector = DetectorFactory.create();
			detector.append(str);
			String language = detector.detect();

			if (language != null) {
				Log.e(TAG, "The language of " + str + " is detected to be: "
						+ language);
			}

			java.util.Locale locale = getContext().getResources().getConfiguration().locale;
			Locale currentLocale = getLocale(locale.getLanguage());
			Locale englishLocale = getLocale(Locale.LOCALE_ENGLISH);
			Locale defaultLocale = getLocale(Locale.LOCALE_DEFAULT);
			Locale textLocale = language == null ? null : getLocale(language);

			$User user = getUser(username, Build.DEBUG);
			boolean performFiltering = getShouldPerformBadwordFiltering(user, true);

			result = replaceBadwords(getTree(user, defaultLocale), str, replacement, defaultLocale == null ? false
					: defaultLocale.getExactMatch());

			// If bad word filtering is turned on, perform filtering against
			// the predefined English and the current language blacklist
			if (performFiltering) {
				result = replaceBadwords(getTree(user, englishLocale), result, replacement, englishLocale == null ? true
						: englishLocale.getExactMatch());

				if (currentLocale != null
						&& !currentLocale.equals(englishLocale)) {
					result = replaceBadwords(getTree(user, currentLocale), result, replacement, currentLocale.getExactMatch());
				}

				if (textLocale != null && !textLocale.equals(englishLocale)
						&& !textLocale.equals(currentLocale)) {
					result = replaceBadwords(getTree(user, textLocale), result, replacement, textLocale.getExactMatch());
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot replace blacklisted words in " + str
					+ " because " + ex);
		}

		return result;
	}

	/**
	 * Replaces occurrences of blacklisted words in the given passage with
	 * {@code replacement}
	 * 
	 * @param tree
	 *            The {@link AhoCorasick} search tree to perform the search
	 * @param text
	 *            The passage to replace backlisted words
	 * @param replacement
	 *            The replacement of blacklisted words
	 * @param exactMatch
	 *            A boolean that indicates whether or not the entire word in the
	 *            passage must match the backlisted word
	 * @return A string with backlisted words replaced with {@code replacement}
	 */
	@SuppressWarnings("unchecked")
	private String replaceBadwords(AhoCorasick tree, String text,
			String replacement, boolean exactMatch) {
		String result = text;
		replacement = replacement == null ? "" : replacement;

		int diff = 0;
		Iterator<?> itr = searchText(tree, text, exactMatch);
		while (itr.hasNext()) {
			SearchResult rst = (SearchResult) itr.next();
			Set<String> outputs = rst.getOutputs();

			// There is no way that a shorter word would match better than a
			// longer word, we replace the word with a longer length
			String output = Collections.max(outputs, new Comparator<String>() {

				@Override
				public int compare(String lhs, String rhs) {
					if (lhs == null && rhs != null) {
						return -rhs.length();
					} else if (lhs != null && rhs == null) {
						return lhs.length();
					} else if (lhs == null && rhs == null) {
						return 0;
					} else {
						return lhs.length() - rhs.length();
					}
				}

			});

			if (output != null && output.length() > 0) {
				byte[] bytes = result.getBytes();
				byte[] outputBytes = output.getBytes();

				// Finds the index range of the word to replace
				int trailingIndex = Math.min(rst.getLastIndex() - diff, bytes.length);
				trailingIndex = Math.max(trailingIndex, 0);
				int leadingIndex = Math.max(trailingIndex - outputBytes.length, 0);
				leadingIndex = Math.min(leadingIndex, bytes.length);

				// Replaces the matching word with the replacement
				StringBuilder builder = new StringBuilder();
				if (leadingIndex > 0) {
					builder.append(new String(bytes, 0, leadingIndex));
				}
				builder.append(replacement);
				diff += outputBytes.length - replacement.getBytes().length;
				if (trailingIndex < bytes.length) {
					builder.append(new String(bytes, trailingIndex, bytes.length
							- trailingIndex));
				}
				result = builder.toString();
			}
		}

		return result;
	}

	/**
	 * Searches in the given {@code text} that matches any pattern defined in
	 * the {@link AhoCorasick} search tree
	 * 
	 * @param tree
	 *            The {@link AhoCorasick} search tree to search against
	 * @param text
	 *            The text to search for patterns defined in the search tree
	 * @param exactMatch
	 *            A boolean that indicates whether or not the match should match
	 *            the entire word (i.e. the next and previous characters in
	 *            input are non-characters like spaces or punctuation)
	 * @return An {@link Iterator} of {@link SearchResult}
	 */
	@SuppressWarnings("unchecked")
	protected Iterator<?> searchText(AhoCorasick tree, String text,
			boolean exactMatch) {
		// Quick return if there is nothing to process
		if (tree == null || text == null) {
			return Collections.emptyList().iterator();
		}

		Iterator<?> itr = tree.search(text.toLowerCase().getBytes());
		Iterator<?> result = null;

		if (exactMatch) {
			List<SearchResult> list = new ArrayList<SearchResult>();
			while (itr.hasNext()) {
				SearchResult rst = (SearchResult) itr.next();
				Set<String> outputs = rst.getOutputs();

				byte[] bytes = text.getBytes();
				boolean match = false;
				Iterator<String> iterator = outputs.iterator();

				while (iterator.hasNext()) {
					String output = iterator.next();
					byte[] outputBytes = output.getBytes();

					// Finds the leading and trailing character and determine if
					// they are non-characters
					int trailingIndex = Math.min(rst.getLastIndex(), bytes.length);
					trailingIndex = Math.max(trailingIndex, 0);
					int leadingIndex = Math.max(trailingIndex
							- outputBytes.length - 1, 0);
					leadingIndex = Math.min(leadingIndex, bytes.length);

					if ((leadingIndex == 0 || NON_CHARS.contains(Character.valueOf((char) bytes[leadingIndex])))
							&& (trailingIndex == bytes.length || NON_CHARS.contains(Character.valueOf((char) bytes[trailingIndex])))) {

						// The leading and trailing character are non-characters
						match = true;
						break;
					} else if (leadingIndex == 0
							&& trailingIndex == bytes.length) {
						// The string matches the entire output
						match = true;
						break;
					}
				}

				// Only add the result to list of the leading and trailing
				// characters of the matching word are non-characters. This
				// indicates that the entire word matches the word in the
				// AhoCorasick search tree
				if (match) {
					list.add(rst);
				}
			}
			result = list.iterator();
		} else {
			result = itr;
		}
		return result;
	}

	/**
	 * Retrieve the last launched package from the History table
	 * 
	 * @param user
	 *            The user's most recent launched app
	 * @return A Component object contain package names and other information,
	 *         null if there was unexpected error or no applications opened by
	 *         the user
	 */
	public Component getMostRecentApp(String user) {
		$User targetUser = getUser(user);
		return getMostRecentApp(targetUser);
	}

	/**
	 * Retrieve the last launched package from the History table
	 * 
	 * @param user
	 *            Returns the most recently ran app for this user
	 * @return A Component object contain package names and other information,
	 *         null if there was unexpected error or no applications opened by
	 *         the user
	 */
	public Component getMostRecentApp($User user) {
		// Quick return if there is nothing to process
		if (mHelper == null || user == null) {
			return null;
		}

		Component result = null;
		try {
			ModelAttributes attrs = Schema.getAttributes(Category.class);
			QueryBuilder<Category, ?> categoryQB = mHelper.getDao(Category.class).queryBuilder();
			categoryQB.where().eq(attrs.getColumnName(Category.NAME_FIELD_NAME), Category.CATEGORY_IGNORED);

			QueryBuilder<ComponentCategory, ?> componentCategoryQB = mHelper.getDao(ComponentCategory.class).queryBuilder();
			componentCategoryQB.join(categoryQB);

			attrs = Schema.getAttributes(Component.class);
			QueryBuilder<Component, ?> componentQB = mHelper.getDao(Component.class).queryBuilder();
			componentQB.selectColumns(attrs.getColumnName(Component.ID_FIELD_NAME));
			componentQB.join(componentCategoryQB);

			attrs = Schema.getAttributes(History.class);
			QueryBuilder<History, ?> historyQB = mHelper.getDao(History.class).queryBuilder();
			historyQB.where().eq(attrs.getColumnName(History.USER_FIELD_NAME), Long.valueOf(user.getId())).and().notIn(attrs.getColumnName(History.COMPONENT_FIELD_NAME), componentQB);
			historyQB.orderBy(History.LAST_ACTIVE_DATE_FIELD_NAME, false);

			List<History> histories = historyQB.query();
			if (histories != null && histories.size() > 0) {
				// Result is sorted by last active date, first one is latest
				History latestHistory = histories.get(0);
				result = latestHistory.getComponent();
				result.refresh();
			}
		} catch (Exception e) {
			Log.e(TAG, "Cannot retreive the last launched application for "
					+ user + " because " + e + " occurred");
		}
		return result;
	}

	/**
	 * Returns whether or not bad word filtering should be performed
	 * 
	 * @param user
	 *            the user whose configuration setting to retrieve
	 * @return the boolean value of the configuration or {@code defaultValue} if
	 *         the configuration is not found
	 */
	protected boolean getShouldPerformBadwordFiltering($User user,
			boolean defaultValue) {
		// Quick return if the request cannot be processed
		if (mHelper == null || user == null) {
			return defaultValue;
		}

		boolean result = defaultValue;
		try {
			Component component = this.getComponent(Component.COMPONENT_BAD_WORD_FILTERING);
			Permission permission = user.getPermission(component);
			result = permission == null ? defaultValue
					: AccessLevels.ALLOW.equals(permission.getAccessLevel());
		} catch (Exception ex) {
			Log.e(TAG, "Cannot determine if bad word filtering should be performed because "
					+ ex);
		}
		return result;
	}

	/**
	 * Returns the {@link Locale} identified by the given locale string
	 * 
	 * @param locale
	 *            the string representation of the Locale
	 */
	protected Locale getLocale(String locale) {
		// Quick return if the request cannot be processed
		if (mHelper == null || locale == null) {
			return null;
		}

		Locale result = null;
		try {
			Dao<Locale, Long> dao = mHelper.getDao(Locale.class);
			QueryBuilder<Locale, Long> qb = dao.queryBuilder();
			qb.where().eq(Locale.LOCALE_FIELD_NAME, locale.toLowerCase());
			result = qb.queryForFirst();
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve locale because " + ex);
		}
		return result;
	}

	/**
	 * Retrieves application permission settings for the given user
	 * 
	 * @param identity
	 *            The {@link Identity} to authenticate the function call
	 * @param user
	 *            returns permission settings for this user. his should be that
	 *            uniquely identifies the user
	 * 
	 * @return A list of {@link Permission} objects or {@code null} if there was
	 *         error
	 */
	public List<Permission> getAccessSchedule(Identity identity, String user) {
		$User usr = getUser(user);
		return getAccessSchedule(identity, usr);
	}

	/**
	 * Retrieves application permission settings for the given user
	 * 
	 * @param identity
	 *            The {@link Identity} to authenticate the function call
	 * @param user
	 *            returns permission settings for this user.
	 * 
	 * @return A list of {@link Permission} objects or {@code null} if there was
	 *         error
	 */
	protected List<Permission> getAccessSchedule(Identity identity, $User user) {
		// Quick return if the request cannot be processed
		if (!checkGuard(identity) || user == null) {
			return null;
		}

		List<Permission> result = null;
		if (user.getPermissions() != null) {
			CloseableIterator<Permission> iterator = user.getPermissions().closeableIterator();
			try {
				while (iterator.hasNext()) {
					Permission permission = iterator.next();
					permission.getComponent().refresh();

					if (result == null) {
						result = new ArrayList<Permission>();
					}
					result.add(permission);
				}
			} catch (SQLException ex) {
				Log.e(TAG, "Failed to retrieve access schedule for " + user
						+ " because " + ex);
				result = null;
			} finally {
				try {
					iterator.close();
				} catch (SQLException ex) {
					// Ignored
				}
			}
		}
		return result;
	}

	/**
	 * Clears all run histories for the given user
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param user
	 *            the user to clear run history
	 */
	public void clearRunHistories(Identity identity, String user) {
		// The request cannot be processed
		$User usr = getUser(user);
		if (!checkGuard(identity) || usr == null || mHelper == null) {
			return;
		}

		try {
			ModelAttributes attrs = Schema.getAttributes(History.class);
			Dao<History, Long> dao = mHelper.getDao(History.class);
			UpdateBuilder<History, Long> ub = dao.updateBuilder();

			String columnName = attrs.getColumnName(History.ACCUMULATED_ACTIVE_TIME_FIELD_NAME);
			ub.updateColumnValue(columnName, Long.valueOf(0));
			ub.where().eq(attrs.getColumnName(History.USER_FIELD_NAME), usr.getId());
			ub.update();
		} catch (SQLException ex) {
			Log.e(TAG, "Failed to clear run history for " + user + " because "
					+ ex);
		}
	}

	/**
	 * Clears run history of the given application component
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param user
	 *            the user to clear run history
	 * @param component
	 *            the application component to clear run history
	 */
	public void clearRunHistory(Identity identity, String user,
			ComponentName component) {
		// Quick return if one of the pass in parameter is null
		if (component == null) {
			return;
		}

		Component c = getComponent(component.getPackageName());
		$User usr = getUser(user);
		clearRunHistory(identity, usr, c);
	}

	/**
	 * Clears run history of the given application component
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param user
	 *            the user to clear run history
	 * @param component
	 *            the application component to clear run history
	 */
	private void clearRunHistory(Identity identity, $User user,
			Component component) {
		// Quick return if one of the pass in parameter is null
		if (component == null || user == null || !checkGuard(identity)) {
			return;
		}

		try {
			History history = component.getHistory(user);
			if (history != null) {
				history.setAccumulatedActiveTime(Long.valueOf(0));
				history.update();
			}
		} catch (SQLException ex) {
			Log.e(TAG, "Failed to clear run history for " + component
					+ " because " + ex);
		}
	}

	/**
	 * Determines whether or not the given user can make purchases on store.
	 * 
	 * @param userId
	 *            the user to determine whether s/he can make purchases on
	 *            store. May not be {@code null}
	 * @return {@code true} if the user is allowed to make purchases in store
	 *         directly or if parental approval is required, {@code false}
	 *         otherwise.
	 */
	public boolean canMakePurchases(String userId) {
		$User user = getUser(userId);
		// Quick return if the request cannot be processed
		if (user == null) {
			return false;
		}

		boolean result = false;
		try {
			Component component = getComponent(Component.COMPONENT_PURCHASE);
			Permission permission = component.getPermission(user);
			Permission.AccessLevels accessLevel = permission.getAccessLevel();
			result = Permission.AccessLevels.ALLOW.equals(accessLevel)
					|| Permission.AccessLevels.APPROVAL.equals(accessLevel);
		} catch (Exception ex) {
			Log.e(TAG, userId + " cannot make purchase because " + ex);
		}
		return result;
	}

	/**
	 * Determines whether or not the given user requires parent approval in
	 * order to make purchases in store
	 * 
	 * @param userId
	 *            the user to determine whether s/he can make purchases on
	 *            store. May not be {@code null}
	 * @return {@code true} if parent approval is required, {@code false}
	 *         otherwise.
	 */
	public boolean requireApprovalToMakePurchases(String userId) {
		$User user = getUser(userId);
		// Quick return if the request cannot be processed
		if (user == null) {
			return false;
		}

		boolean result = false;
		try {
			Component component = getComponent(Component.COMPONENT_PURCHASE);
			Permission permission = component.getPermission(user);
			Permission.AccessLevels accessLevel = permission.getAccessLevel();
			result = Permission.AccessLevels.APPROVAL.equals(accessLevel);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot determine whether " + userId
					+ " requires parent approval to make purchase because "
					+ ex);
		}
		return result;
	}

	/**
	 * Determines whether or not the given user is allowed to access the
	 * {@code url} specified
	 * 
	 * @param userId
	 *            the user to determine whether s/he is allowed to access the
	 *            {@code url}
	 * @param url
	 *            a string representation of the URL to determine whether or not
	 *            it is accessible
	 * @return {@code true} if the URL is accessible by the user, {@code false}
	 *         otherwise
	 */
	public boolean isUrlAccessible(final String userId, final String url) {
		final $User user = getUser(userId);
		// Quick return if the request cannot be processed
		if (user == null) {
			return DEFAULT_ACCESSIBILITY_URL;
		}
		
		Log.d("checkurl","user is not null");
		FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				boolean result = DEFAULT_ACCESSIBILITY_URL;
				try {
					Component component = getComponent(Component.COMPONENT_SECURITY);
					Permission permission = user.getPermission(component);
					
					if (Permission.AccessLevels.HIGH.equals(permission.getAccessLevel())) {
						Log.d("checkurl","security level is high");
						// User is only allowed to access recommended web
						// sites and YouTube
						// videos if security level is high
						RecommendationManager rm = (RecommendationManager) ServiceManager.getService(getContext(), ServiceManager.RECOMMENDATION_SERVICE);
						return rm.isUrlRecommendedBlocking(userId, url);
					} else {
						Log.d("checkurl","security level is low");
						// doesn't have connection
						if (!isServerConnected()) {
							Log.d("checkurl","doesn't have connection");
							try {
								//sleep 10s
								Thread.sleep(10000);
								// check connection again
								if(!isServerConnected())
								{
									//still not connected, skip server checking
									return true;
								}
							} catch (Exception e) {
								Log.e(TAG, "Thread sleep exception");
							}
						}
						Log.d("checkurl","has connection");
						// has Internet connection
						final CheckUrlResponder responder = new CheckUrlResponder();
						final FutureTask<Boolean> checkUrlFuture = new FutureTask<Boolean>(responder);

						// User is only allowed to access whitelisted web
						// sites and YouTube
						// videos. Have to ask WebSocket server whether the
						// item is whitelisted
						Message message = new Message(Message.PROCESS_PARENTAL, Message.OPERATION_CODE_CHECK_URL);
						message.addProperty(EXTRA_BLACKLIST_URL, url);

						MessageFilter filter = new MessageFilter(message.getMessageID());
						MessageReceiver receiver = new MessageReceiver(filter) {

							@Override
							public void onReceive(Message message) {
								if (message != null) {
									switch (message.getStatus()) {
									case Status.SUCCESS_OK:
										String access = (String) message.getProperty(EXTRA_BLACKLIST_ACCESS);
										Permission.AccessLevels accessLevel = Permission.AccessLevels.fromString(access);
										responder.returnValue = Permission.AccessLevels.ALLOW.equals(accessLevel);
										break;
									}
								}

								checkUrlFuture.run();
							}

						};

						// Register the inline receiver to process the
						// {@link Message}
						registerReceiver(receiver);
						// Retrieve permission settings from server
						sendMessage(message);

						// Only return after MessageReceiver received
						// response from server
						result = checkUrlFuture.get();
					}
				} catch (Exception ex) {
					Log.e(TAG, "Cannot determine whether " + url + " is accessible for " + userId + " because " + ex);
				}
				return result;
			}
			
		});
		
		boolean result = DEFAULT_ACCESSIBILITY_URL;
		try {
			future.run();
			result = future.get(TIMEOUT_PERIOD, TimeUnit.MILLISECONDS);
		} catch (Exception ex) {
			Log.e(TAG, "Cannot determine whether " + url + " is accessible for " + userId + " because " + ex);
		}
		
		return result;
	}

	@SuppressWarnings("deprecation")
	private void createAndroidNotification(int id) {

		android.app.NotificationManager nm = (android.app.NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		android.app.Notification.Builder builder = new android.app.Notification.Builder(getContext().getApplicationContext());
		builder.setContentTitle(getContext().getString(R.string.construct_bad_word_list));
		builder.setSmallIcon(R.drawable.home);
		builder.setProgress(0, 0, true);
		android.app.Notification n = builder.getNotification();
		nm.notify(id, n);

	}

	private void removeAndroidNotification(int id) {

		android.app.NotificationManager nm = (android.app.NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(id);

	}

	/** The default response to {@link Message#OPERATION_CODE_CHECK_URL} */
	private class CheckUrlResponder implements Callable<Boolean> {

		boolean returnValue = DEFAULT_ACCESSIBILITY_URL;

		@Override
		public Boolean call() throws Exception {
			return returnValue;
		}

	}

}
