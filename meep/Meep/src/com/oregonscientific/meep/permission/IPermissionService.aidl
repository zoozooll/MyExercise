/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.Blacklist;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Locale;

/**
 * Interface for interacting with the PermissionService
 */ 
interface IPermissionService {
	
	/**
	 * Synchronize local blacklist in database with server's blacklist
	 * 
	 * @param user The user whose blacklist to synchronize with server. This 
	 * should be the identifier that uniquely identifies the user
	 * @param type The type of the blacklist
	 * @param locale The locale of the blacklist
	 */
	void syncBlacklist(String user, String type, String locale);
	
	/**
	 * Synchronize access schedule in database with server's access schedules
	 * 
	 * @param user The user whose access schedule is to be synchronized with server. This 
	 * should be the tag that uniquely identifies the user
	 */
	void syncAccessSchedule(String user);
	
	/**
	 * Check the item if it is in a specific blacklist
	 * 
	 * @param user The user's blacklist to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param listType The type of the blacklist
	 * @param item The item that used to compare with blacklist
	 * @param locale The locale of the item to check against
	 * @return true if the blacklist contains the item, false if it doesn't
	 */
	boolean isItemInBlacklist(String user, String listType, String item, in Locale locale);
	
	/**
	 * Check the word if it is in the badword list
	 * 
	 * @param user The user's bad word list to check against with. This 
	 * should be the identifier that uniquely identifies the user
	 * @param word The word that used to compare with the bad word list
	 * @return true if the bad word list contains the word, false if it doesn't
	 */
	boolean isBadword(String user, String word);
	
	/**
	 * Determine whether or not the given character sequence contains a bad
	 * word as defined in the {@code user}'s bad word list
	 * 
	 * @param user The user whose bad word list to check against. This 
	 * should be the identifier that uniquely identifies the user
	 * @param str The character sequence to verify
	 */
	boolean containsBadword(String user, String str);
	
	/**
	 * Replaces all occurences of bad word in the given {@code str} with {@code replacement}
	 */
	String replaceBadwords(String user, String str, String replacement);
	
	/**
 	 * Determines whether or not the given component is accessible
 	 * 
 	 * @param user The user whose access schedule to check against with. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param component The component name to check
 	 */
 	int isAccessible(String user, in ComponentName component);
 	
 	/**
 	 * Replace the permissions for each package. All existing entire in the local
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
 	void setAccessSchedule(in Identity identity, String user, in List<Permission> permissions);
 	
 	/**
 	 * Update the given permission for the given user
 	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user to set permission against. This 
	 * should be the identifier that uniquely identifies the user
 	 * @param permission The permission to update
 	 */
 	void updatePermission(in Identity identity, String user, in Permission permission);
 	
 	/**
 	 * Replace the blacklist in local data store with {@code blacklist}. All existing
 	 * entries in the local data store will be removed and replaced with {@code blacklist}
 	 * 
 	 * <h2>Note</h2> The operation can be lock access to the underlying data store
 	 * for a long period of time if the list is large. Use this function is caution
 	 * 
 	 * @param identity The Identity to authenticate the function call
 	 * @param user The user whose blacklist to replace. This should be the identifier that uniquely identifies the user
 	 * @param blacklist An list of Blacklist Objects which is the entire blacklist
 	 */
 	void setBlacklist(in Identity identity, String user, in List<Blacklist> blacklists);
	
	/**
	 * Add a blacklist item into the local blacklist
	 * 
	 * @param identity The Identity to authenticate the function call
	 * @param user The user whose blacklist to apply to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param blacklistItem A blacklist item to be added into the local blacklist
	 * @return true if the item is successfully added to local store, false
 	 * if the item cannot be added or the item already exist in the data
 	 * store
	 */
	boolean addBlacklistItem(in Identity identity, String user, in Blacklist blacklistItem);
	
	/**
	 * Remove blacklist item from the local blacklist
	 * 
	 * @param identity The Identity to authenticate the function call
	 * @param user The user whose blacklist to apply to. This 
	 * should be the identifier that uniquely identifies the user
	 * @param blacklistItem A blacklist item to be removed from the local blacklist
	 * @return true if the item is successfully removed from local store, false
	 * if the item cannot be removed or the item does not exist in the data
 	 * store
	 */
	boolean removeBlacklistItem(in Identity identity, String user, in Blacklist blacklistItem);
	
	/**
	 * Retrieve most recent used application for a specific user
	 * 
	 * @param user The identifier that uniquely identifies the user
	 * @return the Component object contains the package name
	 */
	Component getMostRecentApp(String user);
	
	/**
	 * Returns a list of {@link Component} in the {@link Category} identified by {@code categoryName}
	 * 
	 * @param The user whose component list to return. This should be the identifier that uniquely identifies the user
	 * @param categoryName the name of the category
	 */
	List<Component> getComponents(String user, String categoryName);
	
	/**
	 * Retrieves application permission settings for the given user
	 */
	List<Permission> getAccessSchedule(in Identity identity, String user);
	
	/**
	 * Clears run history of the given application component
	 *
	 * @param identity The Identity to authenticate the function call
	 * @param user the user to clear run history
	 * @param component the application component to clear run history
	 */
	void clearRunHistory(in Identity identity, String user, in ComponentName component);
	
	/**
	 * Clears all run histories for the given user
	 * 
	 * @param identity The Identity to authenticate the function call
	 * @param user the user to clear run history
	 */
	void clearRunHistories(in Identity identity, String user);
	
	/**
	 * Determines whether or not the given user can make purchase on store
	 */
	boolean canMakePurchases(String user);
	
	/**
	 * Determines whether or not the given user requires third-party approval to make purchases in store
	 */
	boolean requireApprovalToMakePurchases(String user);
	
	/**
	 * Determines whether or not the given user is allowed access to the given URL
	 */
	boolean isUrlAccessible(String user, String url);
 }