/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;
import com.oregonscientific.meep.permission.Permission.AccessLevels;

/**
 * The Component object that represents a component object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "components")
public class Component extends Model<Component, Long> implements Identity<Long> {
	
	private final String TAG = getClass().getSimpleName();
	
	static final String COMPONENT_SECURITY = "com.oregonscientific.meep.SECURITY";
	static final String COMPONENT_IN_APP = "com.oregonscientific.meep.store2.IN_APP";
	static final String COMPONENT_PURCHASE = "com.oregonscientific.meep.store2.PURCHASE";
	static final String COMPONENT_BAD_WORD_FILTERING = "com.oregonscientific.meep.BAD_WORD_FILTERING";
	static final String COMPONENT_GAME = "com.oregonscientific.meep.app";
	
	/** A list of display names for system components */
	static final String NAME_PICTURE = "picture";
	static final String NAME_YOUTUBE = "youtube";
	static final String NAME_EBOOK = "ebook";
	static final String NAME_MOVIE = "movie";
	static final String NAME_APPS = "apps";
	static final String NAME_GAMES = "game";
	static final String NAME_MUSIC = "music";
	static final String NAME_BROWSER = "browser";
	static final String NAME_COMMUNICATOR = "communicator";
	static final String NAME_IAP = "inapp";
	static final String NAME_GOOGLE_PLAY = "googleplay";
	static final String NAME_SECURITY_LEVEL = "securitylevel";
	static final String NAME_PURCHASE = "purchase";
	static final String NAME_BAD_WORD = "osgdbadword";
	
	/** A default list of permissions controlled packages */
	public static final Map<AbstractMap.SimpleEntry<String, String>, Permission.AccessLevels> SYSTEM_COMPONENTS = 
		new HashMap<AbstractMap.SimpleEntry<String, String>, AccessLevels>() {
		
		private static final long serialVersionUID = 7670088447323001035L;

		{
			put(new AbstractMap.SimpleEntry<String, String>(NAME_PICTURE, "com.android.gallery3d"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_YOUTUBE, "com.oregonscientific.meep.youtube"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_EBOOK, "com.oregonscientific.meep.ebook"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_MOVIE, "com.oregonscientific.meep.movie"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_APPS, "com.oregonscientific.meep.APPS"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_GAMES, COMPONENT_GAME), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_MUSIC, "com.oregonscientific.meep.meepmusic"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_BROWSER, "com.oregonscientific.meep.browser"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_COMMUNICATOR, "com.oregonscientific.meep.communicator"), AccessLevels.DENY);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_IAP, COMPONENT_IN_APP), AccessLevels.DENY);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_GOOGLE_PLAY, "com.android.vending"), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_SECURITY_LEVEL, COMPONENT_SECURITY), AccessLevels.MEDIUM);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_PURCHASE, COMPONENT_PURCHASE), AccessLevels.ALLOW);
			put(new AbstractMap.SimpleEntry<String, String>(NAME_BAD_WORD, COMPONENT_BAD_WORD_FILTERING), AccessLevels.ALLOW);
		}
	};
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String NAME_FIELD_NAME = "name";
	public static final String DISPLAY_NAME_FIELD_NAME ="display_name";
	
	public static final String COMPONENT_CATEGORIES_FIELD_NAME = "categories";
	public static final String COMPONENT_HISTORIES_FIELD_NAME = "histories";
	public static final String COMPONENT_PERMISSIONS_FIELD_NAME = "permissions";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "component_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(COMPONENT_CATEGORIES_FIELD_NAME)
	@Omit
	private ForeignCollection<ComponentCategory> componentCategories;
	
	@DatabaseField(
			columnName = NAME_FIELD_NAME, 
			canBeNull = true,
			unique = true)
	@SerializedName(NAME_FIELD_NAME)
	@Expose
	private String name;
	
	@DatabaseField(columnName = DISPLAY_NAME_FIELD_NAME, canBeNull = true)
	@SerializedName(DISPLAY_NAME_FIELD_NAME)
	@Expose
	private String displayName;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(COMPONENT_HISTORIES_FIELD_NAME)
	@Omit
	private ForeignCollection<History> histories;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(COMPONENT_PERMISSIONS_FIELD_NAME)
	@Omit
	private ForeignCollection<Permission> permissions;
	
	public Component() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Component(String name) {
		this.name = name;
	}
	
	public Component(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String value) {
		displayName = value;
	}
	
	public ForeignCollection<ComponentCategory> getComponentCategories() {
		return componentCategories;
	}
	
	/**
	 * Returns whether or not the {@link Component} is in the given {@code category}
	 * 
	 * @param category the name of the category
	 * @return true if this component is in the category with the given name
	 */
	public boolean inCategory(String category) {
		boolean result = false;
		if (category != null && componentCategories != null) {
			// Since all foreign collections are lazy, no need to refresh
			CloseableWrappedIterable<ComponentCategory> iterator = componentCategories.getWrappedIterable();
			try {
				for (ComponentCategory cc : iterator) {
					Category cat = cc.getCategory();
					if (cat == null) {
						continue;
					}
					
					cat.refresh();
					if (category.equals(cat.getName())) {
						result = true;
						break;
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine whether " + getName() + " is in " + category + " because " + ex);
			} finally {
				try {
					iterator.close();
				} catch (SQLException e) {
					// Ignore
				}
			}
		}
		return result;
	}
	
	public ForeignCollection<History> getHistories() {
		return histories;
	}
	
	/**
	 * Returns the run history of the component for {@code user}
	 * @param user the user whose run history of the component is to be returned
	 * @return the run history for the component
	 */
	public History getHistory($User user) {
		// Quick return if the request cannot be processed
		if (histories == null) {
			return null;
		}
		
		// Since all foreign collections are lazy, no need to refresh
		CloseableWrappedIterable<History> wrappedIterable = histories.getWrappedIterable();
		try {
			for (History history : wrappedIterable) {
				$User usr = history.getUser();
				if (usr == null) {
					continue;
				}
				
				usr.refresh();
				if (usr.equals(user)) {
					return history;
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve history for " + user + " because " + ex);
		} finally {
			try {
				wrappedIterable.close();
			} catch (SQLException e) {
				// Ignore
			}
		}
		return null;
	}
	
	public ForeignCollection<Permission> getPermissions() {
		return permissions;
	}
	
	/**
	 * Returns the permission setting of the component for {@code user}
	 * 
	 * @param user the permission setting for this user
	 * @return the Permission object if a setting for found, null otherwise
	 */
	public Permission getPermission($User user) {
		// Quick return if the request cannot be processed
		if (permissions == null) {
			return null;
		}
		
		// Since all foreign collections are lazy, no need to refresh
		CloseableWrappedIterable<Permission> wrappedIterable = permissions.getWrappedIterable();
		try {
			for (Permission permisison : wrappedIterable) {
				$User usr = permisison.getUser();
				if (usr == null) {
					continue;
				}
				
				usr.refresh();
				if (usr.equals(user)) {
					return permisison;
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve permission for " + user + " because " + ex);
		} finally {
			try {
				wrappedIterable.close();
			} catch (SQLException e) {
				// Ignore
			}
		}
		return null;
	}
	
	@Override
	public Long getIdentity() {
		return Long.valueOf(getId());
	}

	@Override
	public String getIdentityAttribute() {
		return ID_FIELD_NAME;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Component> CREATOR = new Creator<Component>() {
		
		@Override
		public Component createFromParcel(Parcel source) {
			Component result = null;
			try {
				String className = source.readString();
				result = (Component) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Model object from a Parcel. Ignored.
			}
			return result;
		}
		
		@Override
		public Component[] newArray(int size) {
			return new Component[size];
		}
	};
	
}
