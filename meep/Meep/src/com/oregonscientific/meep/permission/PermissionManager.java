/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.content.ComponentName;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceAgent;
import com.oregonscientific.meep.ServiceConnector;
import com.oregonscientific.meep.account.Identity;

/**
 * The primary API to access security permission known to MEEP. Applications
 * with sufficient privilege can access the API to set security settings on
 * the device. Obtain an instance of this class by calling 
 * {@link com.oregonscientific.meep.ServiceManager#getService(android.content.Context, String)}
 */
public class PermissionManager extends ServiceAgent<IPermissionService> {

	private static final String TAG = "PermissionManager";
	
	/**
	 * Broadcast intent action indicating that permission setting is violated.
	 * One extra provides information on the package violated permission
	 * 
	 * @see #EXTRA_PACKAGE_NAME
	 */
	public static final String PERMISSION_VIOLATED_ACTION = 
		"com.oregonscientific.meep.permission.PERMISSION_VIOLATED";
	
	/**
	 * The lookup key for the name of the package that violated permission
	 * settings on the device. Retrieve it with {@link android.content.Intent#getStringExtra(String)}
	 * 
	 * @see #PERMISSION_VIOLATED_ACTION
	 */
	public static final String EXTRA_PACKAGE_NAME = "package";
	
	/**
	 * The lookup key for the reason of the violation. Retrieve it with {@link android.content.Intent#getIntExtra(String, int)}
	 * 
	 * @see #PERMISSION_VIOLATED_ACTION
	 */
	public static final String EXTRA_VIOLATION_REASON = "reason";
	
	/**
	 * A blacklist of URLs
	 * 
	 * @see #isItemInBlacklist(String, String, String)
	 */
	public static final String BLACKLIST_TYPE_BROWSER = "browser";
	
	/**
	 * A blacklist of bad words
	 * 
	 * @see #isBadword(String, String)
	 * @see #containsBadword(String, String)
	 */
	public static final String BLACKLIST_TYPE_KEYWORD = "badword";
	
	/**
	 * A flag that indicates an application is accessible
	 */
	public static final int FLAG_PERMISSION_OK = 0x10000000;
	
	/**
	 * A flag that indicates an application is not accessible because the allocated amount of run time
	 * is reached
	 */
	public static final int FLAG_PERMISSION_TIMEOUT = 0x20000000;
	
	/**
	 * A flag that indicates an application is not accessible because it was prohibited
	 */
	public static final int FLAG_PERMISSION_DENIED = 0x30000000;

	public PermissionManager(ServiceConnector connector) {
		super(connector);
	}
	
	@Override
	protected IPermissionService getInterface() {
		return IPermissionService.Stub.asInterface(getConnector().getBinder());
	}
	
	/**
	 * Synchronize local blacklist in database with server's blacklist
	 * 
	 * @param user The user whose blacklist to synchronize with server. This 
	 * should be the identifier that uniquely identifies the user
	 * @param type The type of the blacklist. Should be one of {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * @param locale The locale of the blacklist. Should only contain the language portion of a {@code java.util.Locale}
	 */
	public void syncBlacklist(String user, String type, String locale) {
		// Quick return if no type is specified
		if (type == null) {
			return;
		}
		
		if (isReady()) {
			try {
				getService().syncBlacklist(user, type, locale);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to synchronize blacklist of type " + type + " because " + e + " occurred");
			}
		} else {
			// Service not yet binded, retrying ...
			syncBlacklistDelayed(user, type, locale);
		}
	}
	
	/**
	 * Schedule synchronizing blacklist after the underlying service is connected
	 * 
	 * @param user The user whose blacklist to synchronize with server. This 
	 * should be the identifier that uniquely identifies the user
	 * @param type The type of the blacklist. Should be one of {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * @param locale The locale of the blacklist. Should only contain the language portion of a {@code java.util.Locale}
	 */
	private void syncBlacklistDelayed(final String user, final String type, final String locale) {
		if (isReady()) {
			syncBlacklist(user, type, locale);
		} else if (type != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					syncBlacklist(user, type, locale);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	
	/**
	 * Synchronize access schedule in database with server's access schedules
	 * 
	 * @param user The user whose blacklist to synchronize with server. This 
	 * should be the identifier that uniquely identifies the user
	 */
	public void syncAccessSchedule(String user) {
		if (isReady()) {
			try {
				getService().syncAccessSchedule(user);
			} catch (RemoteException e) {
				Log.e(TAG, "Failed to sync access schedules with error :" + e);
			}
			
		} else {
			// Service not yet binded, retrying ...
			syncAccessScheduleDelayed(user);
		}
	}
	
	/**
	 * Schedule synchronizing permission settings after the underlying service is
	 * connected
	 * 
	 * @param user The user whose access schedule to synchronize with server. This 
	 * should be the identifier that uniquely identifies the user
	 */
	private void syncAccessScheduleDelayed(final String user) {
		if (isReady()) {
			syncAccessSchedule(user);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					syncAccessSchedule(user);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Check the item if it is in a specific blacklist
	 * 
	 * @param user The user's blacklist to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param listType The type of the blacklist. Should be one of {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * @param item The item that used to compare with blacklist
	 * @param locale The {@code Locale} of the item
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklist(String user, String listType, String item, Locale locale) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().isItemInBlacklist(user, listType, item, locale);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot check item in blacklist with error: " + e);
			}
		}
		return result;
	}
	
	/**
	 * Check the item if it is in a specific blacklist
	 * 
	 * @param user The user's blacklist to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param listType The type of the blacklist. Should be one of {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * @param item The item that used to compare with blacklist
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklist(String user, String listType, String item) {
		return isItemInBlacklist(user, listType, item, null);
	}
	
	/**
	 * Determines whether or not the given {@code item} is in the blacklist. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user The user's blacklist to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param listType The type of the blacklist. Should be one of {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * @param item The item that used to compare with blacklist
	 * @param locale The {@link Locale} of the item
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklistBlocking(final String user, final String listType, final String item, final Locale locale) {
		boolean result = false;
		if (isReady()) {
			result = isItemInBlacklist(user, listType, item, locale);
		} else if (item != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return isItemInBlacklist(user, listType, item, locale);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine if " + item + " is in blacklist because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given {@code item} is in the blacklist. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user The user's blacklist to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param listType The type of the blacklist. Should be one of {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * @param item The item that used to compare with blacklist
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	public boolean isItemInBlacklistBlocking(final String user, final String listType, final String item) {
		return isItemInBlacklistBlocking(user, listType, item, null);
	}
	
	/**
	 * Check the word if it is in the badword list
	 * 
	 * @param user The user's bad word list to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param word The word that used to compare with the bad word list
	 * @return true if the bad word list contains the word, false if it doesn't
	 */
	public boolean isBadword(String user, String word) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().isBadword(user, word);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot check bad word: " + word + " because " + e + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given {@code word} is one of the prohibited words. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user The user's bad word list to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param word The word that used to compare with the bad word list
	 * @return true if the bad word list contains the word, false if it doesn't
	 */
	public boolean isBadwordBlocking(final String user, final String word) {
		boolean result = false;
		if (isReady()) {
			result = isBadword(user, word);
		} else if (word != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return isBadword(user, word);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine if " + word + " is one of the prohibited words because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Replaces all occurrence of bad words in {@code str} with {@code replacement}
	 * 
	 * @param user Matches against this user's bad word list
	 * @param str The string to check
	 * @param replacement The string to replace the bad words with
	 * @return A string with the bad words replaced by {@code replacement}
	 */
	public String replaceBadwords(String user, String str, String replacement) {
		String result = str;
		if (isReady()) {
			try {
				result = getService().replaceBadwords(user, str, replacement);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot replace occurrences of bad words in " + str + " with " + replacement + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Replaces all occurrence of bad words in {@code str} with {@code replacement} This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user Matches against this user's bad word list
	 * @param str The string to check
	 * @param replacement The string to replace the bad words with
	 * @return A string with the bad words replaced by {@code replacement}
	 */
	public String replaceBadwordsBlocking(final String user, final String str, final String replacement) {
		String result = str;
		if (isReady()) {
			result = replaceBadwords(user, str, replacement);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<String> future = new FutureTask<String>(new Callable<String>() {

				@Override
				public String call() throws Exception {
					return replaceBadwords(user, str, replacement);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine if " + str + " contains one of the prohibited words because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determine whether or not the given character sequence contains a bad
	 * word as defined in the {@code user}'s bad word list
	 * 
	 * @param user The user whose bad word list to check against. This 
	 * should be the identifier that uniquely identifies the user
	 * @param str The character sequence to verify
	 */
	public boolean containsBadword(String user, String str) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().containsBadword(user, str);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot check whether " + str + " contains bad words because " + e + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Determine whether or not the given character sequence contains a bad
	 * word as defined in the {@code user}'s bad word list. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user The user whose bad word list to check against. This 
	 * should be the identifier that uniquely identifies the user
	 * @param str The character sequence to verify
	 */
	public boolean containsBadwordBlocking(final String user, final String str) {
		boolean result = false;
		if (isReady()) {
			result = containsBadword(user, str);
		} else if (str != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return containsBadword(user, str);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine if " + str + " contains one of the prohibited words because " + ex);
			}
		}
		return result;
	}
	
	/**
 	 * Determines whether or not the given component is accessible
 	 * 
 	 * @param user The user whose access schedule to check against with. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param component The component name to check
 	 * @return {@link #FLAG_PERMISSION_OK} if {@code user} is given access to the {@code component},
 	 * {@link #FLAG_PERMISSION_TIMEOUT} or {@link #FLAG_PERMISSION_DENIED} otherwise
 	 */
	public int isAccessible(String user, ComponentName component) {
		int flag = FLAG_PERMISSION_OK;
		if (isReady()) {
			try {
				flag = getService().isAccessible(user, component);
			} catch (RemoteException e) {
				String msg = component == null ? "" : component.toString();
				Log.e(TAG, "Cannot determine whether " + msg + " is accessible because " + e + " occurred");
			}
		}
		return flag;
	}
	
	/**
 	 * Determines whether or not the given component is accessible. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
 	 * 
 	 * @param user The user whose access schedule to check against with. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param component The component name to check
 	 * @return {@link #FLAG_PERMISSION_OK} if {@code user} is given access to the {@code component},
 	 * {@link #FLAG_PERMISSION_TIMEOUT} or {@link #FLAG_PERMISSION_DENIED} otherwise
 	 */
	public int isAccessibleBlocking(final String user, final ComponentName component) {
		int result = FLAG_PERMISSION_OK;
		if (isReady()) {
			result = isAccessible(user, component);
		} else if (component != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Integer> future = new FutureTask<Integer>(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					return isAccessible(user, component);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine if " + component + " is accessible because " + ex);
			}
		}
		return result;
	}
 	
 	/**
 	 * Replace the permissions for each package. All existing entries in the local
 	 * data store will be removed and replaced with {@code permissions}
 	 * 
 	 * <h2>Note</h2> The operation can be lock access to the underlying data store
 	 * for a long period of time if the list is large. Use this function is caution
 	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user the permission schedule to apply to. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param permissions An list of Permission objects that contains every package's permission  
 	 */
	public void setAccessSchedule(Identity identity, String user, List<Permission> permissions) {
		if (isReady()) {
			try {
				getService().setAccessSchedule(identity, user, permissions);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot set access schedule because " + e + " occurred");
			}
		} else {
			// Service not yet binded, retrying ...
			setAccessScheduleDelayed(identity, user, permissions);
		}

	}
	
	/**
	 * Set access schedule after the underlying service is connected
	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user the permission schedule to apply to. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param permissions An list of Permission objects that contains every package's permission  
	 */
	private void setAccessScheduleDelayed (final Identity identity, final String user, final List<Permission> permissions) {
		if (isReady()) {
			setAccessSchedule(identity, user, permissions);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					setAccessSchedule(identity, user, permissions);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
 	/**
 	 * Update the given permission for the given user
 	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user to set permission against. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param permission The permission to update
 	 */
	public void updatePermission(Identity identity, String user, Permission permission) {
		if (isReady()) {
			try {
				getService().updatePermission(identity, user, permission);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot update permission setting because " + e + " occurred");
			}
		} else {
			updatePermissionDelayed(identity, user, permission);
		}
	}
	
	
	/**
	 * Updates the given {@code permission} after the underlying service is connection
	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user to set permission against. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param permission The permission to update
	 */
	private void updatePermissionDelayed (final Identity identity, final String user, final Permission permission) {
		if (isReady()) {
			updatePermission(identity, user, permission);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					updatePermission(identity, user, permission);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}

	/**
	 * Replace the blacklist in local data store with {@code blacklist}. All
	 * existing entries in the local data store will be removed and replaced
	 * with {@code blacklist} This method will block until the underlying
	 * service becomes ready. Calling this method in main thread may cause dead
	 * lock. User should not call this method in the main thread
	 * 
	 * <h2>Note</h2> The operation can be lock access to the underlying data
	 * store for a long period of time if the list is large. Use this function
	 * is caution
	 * 
	 * @param identity
	 *            The Identity to authenticate the function call
	 * @param user
	 *            The user's blacklist to replace. This should be the identifier that
	 *            uniquely identifies the user
	 * @param blacklist
	 *            An list of Blacklist Objects which is the entire blacklist
	 */
	public void setBlacklist(Identity identity, String user, List<Blacklist> blacklists) {
		if (isReady()) {
			try {
				getService().setBlacklist(identity, user, blacklists);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot set blacklist because " + e + " occurred");
			}
		} else {
			setBlacklistDelayed(identity, user, blacklists);
		}
	}
	
	/**
	 * Replace the existing blacklist with {@code blacklists} after the underlying service is connected
	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user to set permission against. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param permission The permission to update
	 */
	private void setBlacklistDelayed (final Identity identity, final String user, final List<Blacklist> blacklists) {
		if (isReady()) {
			setBlacklist(identity, user, blacklists);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					setBlacklist(identity, user, blacklists);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Add a blacklist item into the local blacklist
	 * 
 	 * @param identity The Identity to authenticate the function call
	 * @param user The user whose blacklist to apply to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param item A blacklist item to be added into the local blacklist
	 * @return true if the item is successfully added to local store, false
 	 * if the item cannot be added or the item already exist in the data
 	 * store
	 */
	public boolean addBlacklistItem(Identity identity, String user, Blacklist item) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().addBlacklistItem(identity, user, item);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot add item to blacklist because " + e + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Add a blacklist item into the local blacklist. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
 	 * @param identity The Identity to authenticate the function call
	 * @param user The user whose blacklist to apply to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param item A blacklist item to be added into the local blacklist
	 * @return true if the item is successfully added to local store, false
 	 * if the item cannot be added or the item already exist in the data
 	 * store
	 */
	public boolean addBlacklistItemBlocking(final Identity identity, final String user, final Blacklist item) {
		boolean result = false;
		if (isReady()) {
			result = addBlacklistItem(identity, user, item);
		} else if (item != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return addBlacklistItem(identity, user, item);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot add " + item + " to the blacklist because " + ex);
			}
		}
		return result; 
	}
	
	/**
	 * Remove the {@code item} for {@code user} from the blacklist.
	 * 
 	 * @param identity The Identity to authenticate the function call
	 * @param user The user whose blacklist to apply to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param item A blacklist item to be removed from the local blacklist
	 * @return true if the item is successfully removed from local store, false
	 * if the item cannot be removed or the item does not exist in the data
 	 * store
	 */
	public boolean removeBlacklistItem(Identity identity, String user, Blacklist item) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().removeBlacklistItem(identity, user, item);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot remove item from blacklist because " + e + " occurred");
			}
		}

		return result;
	}
	
	/**
	 * Remove the {@code item} for {@code user} from the blacklist. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
 	 * @param identity The Identity to authenticate the function call
	 * @param user The user whose blacklist to apply to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param item A blacklist item to be removed from the local blacklist
	 * @return true if the item is successfully removed from local store, false
	 * if the item cannot be removed or the item does not exist in the data
 	 * store
	 */
	public boolean removeBlacklistItemBlocking(final Identity identity, final String user, final Blacklist item) {
		boolean result = false;
		if (isReady()) {
			result = removeBlacklistItem(identity, user, item);
		} else if (item != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return removeBlacklistItem(identity, user, item);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot add " + item + " to the blacklist because " + ex);
			}
		}
		return result; 
	}
	
	/**
	 * Returns a list of {@link Component} in the {@link Category} identified by {@code categoryName}
	 * 
	 * @param user the user whose component list to return
	 * @param categoryName the name of the category. It should be one of {@link Category#CATEGORY_BLACKLIST}
	 * or {@link Category#CATEGORY_GAMES}
	 * @return a list of {@link Component} in the given {@link Category}, or {@code null} if the
	 * category is not found
	 */
	public List<Component> getComponents(String user, String categoryName) {
		List<Component> result = null;
		if (isReady()) {
			try {
				result = getService().getComponents(user, categoryName);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot retrieve components in " + categoryName + " because " + e + " occurred");
			}
		}
		return result;
	}
	
	/**
	 * Returns a list of {@link Component} in the {@link Category} identified by {@code categoryName}
	 * This method will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the user whose component list to return
	 * @param categoryName the name of the category. It should be one of {@link Category#CATEGORY_BLACKLIST}
	 * or {@link Category#CATEGORY_GAMES}
	 * @return a list of {@link Component} in the given {@link Category}, or {@code null} if the
	 * category is not found
	 */
	public List<Component> getComponentsBlocking(final String user, final String categoryName) {
		List<Component> result = null;
		if (isReady()) {
			result = getComponents(user, categoryName);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<List<Component>> future = new FutureTask<List<Component>>(new Callable<List<Component>>() {

				@Override
				public List<Component> call() throws Exception {
					return getComponents(user, categoryName);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve components in " + categoryName + " because " + ex);
			}
		}
		return result; 
	}
	
	/**
	 * Retrieve most recent used application for {@code user}
	 * 
	 * @param user the identifier that uniquely identifies the user. This 
	 * should be the identifier that uniquely identifies the user
	 * @return the Component object contains the package name, null if there
	 * is no app ran previously
	 */
	public Component getMostRecentApp(String user){
		Component component = null;
		if (isReady()) {
			try {
				component = getService().getMostRecentApp(user);
			} catch (RemoteException e) {
				Log.e(TAG, "Cannot retrieve the most recent running app because " + e + " occurred");
			}
		}
		return component;
	}
	
	/**
	 * Retrieve most recent used application for {@code user}. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param user the identifier that uniquely identifies the user. This 
	 * should be the identifier that uniquely identifies the user
	 * @return the Component object contains the package name, null if there
	 * is no app ran previously
	 */
	public Component getMostRecentAppBlocking(final String user) {
		Component result = null;
		if (isReady()) {
			result = getMostRecentApp(user);
		} else if (user != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Component> future = new FutureTask<Component>(new Callable<Component>() {

				@Override
				public Component call() throws Exception {
					return getMostRecentApp(user);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve " + user + "'s most recently used application because " + ex);
			}
		}
		return result; 
	}
	
	/**
	 * Clears all run histories for the given user
	 * 
	 * @param identity The Identity to authenticate the function call
	 * @param user the user to clear run history
	 */
	public void clearRunHistories(Identity identity, String user) {
		if (isReady()) {
			try {
				getService().clearRunHistories(identity, user);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot clear run history for " + user + " because " + ex);
			}
		} else {
			clearRunHistoriesDelayed(identity, user);
		}
	}
	
	/**
	 * Clears all run histories for the given user
	 * 
	 * @param identity The Identity to authenticate the function call
	 * @param user the user to clear run history
	 */
	private void clearRunHistoriesDelayed(final Identity identity, final String user) {
		if (isReady()) {
			clearRunHistories(identity, user);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					clearRunHistories(identity, user);
					return null;
				}
				
			});
			addConnectedTasks(future);
		}
	}
	
	/**
	 * Clears run history of the given application component after the underlying service is connected
	 *
	 * @param identity The Identity to authenticate the function call
	 * @param user the user to clear run history
	 * @param component the application component to clear run history
	 */
	public void clearRunHistory(Identity identity, String user, ComponentName component) {
		if (isReady()) {
			try {
				getService().clearRunHistory(identity, user, component);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot clear run history for " + component + " because " + ex);
			}
		} else {
			clearRunHistoryDelayed(identity, user, component);
		}
	}
	
	/**
	 * Clears run history of the given application component after the underlying service is connected
	 *
	 * @param identity The Identity to authenticate the function call
	 * @param user the user to clear run history
	 * @param component the application component to clear run history
	 */
	private void clearRunHistoryDelayed(final Identity identity, final String user, final ComponentName component) {
		if (isReady()) {
			clearRunHistory(identity, user, component);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Void> future = new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					clearRunHistory(identity, user, component);
					return null;
				}
				
			});
			addConnectedTasks(future);
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
	public boolean canMakePurchases(String user) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().canMakePurchases(user);
			} catch (RemoteException ex) {
				Log.e(TAG, user + " cannot make purchase because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given user can make purchases on store. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param userId
	 *            the user to determine whether s/he can make purchases on
	 *            store. May not be {@code null}
	 * @return {@code true} if the user is allowed to make purchases in store
	 *         directly or if parental approval is required, {@code false}
	 *         otherwise.
	 */
	public boolean canMakePurchasesBlocking(final String user) {
		boolean result = false;
		if (isReady()) {
			result = canMakePurchases(user);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return canMakePurchases(user);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, user + " cannot make purchase because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given user is allowed to access the {@code url} specified
	 * 
	 * @param userId the user to determine whether s/he is allowed to access the {@code url}
	 * @param url a string representation of the URL to determine whether or not it is accessible
	 * @return {@code true} if the URL is accessible by the user, {@code false} otherwise
	 */
	public boolean isUrlAccessible(String user, String url) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().isUrlAccessible(user, url);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot determine whether " + url + " is accessible for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given user is allowed to access the {@code url} specified. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param userId the user to determine whether s/he is allowed to access the {@code url}
	 * @param url a string representation of the URL to determine whether or not it is accessible
	 * @return {@code true} if the URL is accessible by the user, {@code false} otherwise
	 */
	public boolean isUrlAccessibleBlocking(final String user, final String url) {
		boolean result = false;
		if (isReady()) {
			result = isUrlAccessible(user, url);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return isUrlAccessible(user, url);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine whether " + url + " is accessible for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given user requires parent approval in order
	 * to make purchases in store
	 * 
	 * @param userId the user to determine whether s/he can make purchases on store. May not be {@code null}
	 * @return {@code true} if parent approval is required, {@code false} otherwise.
	 */
	public boolean requireApprovalToMakePurchases(String user) {
		boolean result = false;
		if (isReady()) {
			try {
				result = getService().requireApprovalToMakePurchases(user);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot determine whether " + user + " requires parent approval to make purchase because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the given user requires parent approval in order. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * to make purchases in store
	 * 
	 * @param userId the user to determine whether s/he can make purchases on store. May not be {@code null}
	 * @return {@code true} if parent approval is required, {@code false} otherwise.
	 */
	public boolean requireApprovalToMakePurchasesBlocking(final String user) {
		boolean result = false;
		if (isReady()) {
			result = requireApprovalToMakePurchasesBlocking(user);
		} else {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return requireApprovalToMakePurchasesBlocking(user);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine whether " + user + " requires parent approval to make purchase because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Retrieves application permission settings for the given user
	 * 
	 * @param identity
	 *            The {@link Identity} to authenticate the function call
	 * @param user
	 *            returns permission settings for this user. his should be
	 *            the identifier that uniquely identifies the user
	 *            
	 * @return A list of {@link Permission} objects or {@code null} if there was error
	 */
	public List<Permission> getAccessSchedule(Identity identity, String user) {
		List<Permission> result = null;
		if (isReady()) {
			try {
				result = getService().getAccessSchedule(identity, user);
			} catch (RemoteException ex) {
				Log.e(TAG, "Cannot retrieve access schedule for " + user + " because " + ex);
			}
		}
		return result;
	}
	
	/**
	 * Retrieves application permission settings for the given user. This method
	 * will block until the underlying service becomes ready. Calling this method in main 
	 * thread may cause dead lock. User should not call this method in the main thread
	 * 
	 * @param identity
	 *            The {@link Identity} to authenticate the function call
	 * @param user
	 *            returns permission settings for this user. his should be
	 *            the identifier that uniquely identifies the user
	 *            
	 * @return A list of {@link Permission} objects or {@code null} if there was error
	 */
	public List<Permission> getAccessScheduleBlocking(final Identity identity, final String user) {
		List<Permission> result = null;
		if (isReady()) {
			result = getAccessSchedule(identity, user);
		} else if (user != null) {
			// Adds the FutureTask to be executed when the service is connected
			FutureTask<List<Permission>> future = new FutureTask<List<Permission>>(new Callable<List<Permission>>() {

				@Override
				public List<Permission> call() throws Exception {
					return getAccessSchedule(identity, user);
				}
				
			});
			addConnectedTasks(future);
			
			// Wait until the underlying service is connected then return the result
			try {
				result = future.get();
			} catch (Exception ex) {
				Log.e(TAG, "Cannot retrieve access schedule for " + user + " because " + ex);
			}
		}
		return result; 
	}
 	
}
