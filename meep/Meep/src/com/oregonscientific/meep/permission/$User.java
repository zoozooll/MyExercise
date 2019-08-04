/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.sql.SQLException;

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

/**
 * The User object that represents an account object in the underlying datastore
 */
@DatabaseTable(tableName = "users")
class $User extends Model<$User, Long> implements Identity<Long> {
	
	private static final String TAG = "$User";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String IDENTIFIER_FIELD_NAME = "identifier";
	
	public static final String BLACKLISTS_FIELD_NAME = "blacklists";
	public static final String PERMISSIONS_FIELD_NAME = "permissions";
	public static final String HISTORIES_FIELD_NAME = "histories";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "user_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = IDENTIFIER_FIELD_NAME,
			uniqueIndex = true)
	@SerializedName(IDENTIFIER_FIELD_NAME)
	@Expose
	private String identifier;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(BLACKLISTS_FIELD_NAME)
	@Omit
	private ForeignCollection<Blacklist> blacklists;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(PERMISSIONS_FIELD_NAME)
	@Omit
	private ForeignCollection<Permission> permissions;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(HISTORIES_FIELD_NAME)
	@Omit
	private ForeignCollection<History> histories;
	
	$User() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	/**
	 * Creates a new user with the given unique {@code identifier}
	 * 
	 * @param identifier the unique identifier of the user
	 */
	$User(String identifier) {
		this.identifier = identifier;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public ForeignCollection<Blacklist> getBlacklists() {
		return blacklists;
	}
	
	public ForeignCollection<Permission> getPermissions() {
		return permissions;
	}
	
	/**
	 * Retreive permission setting for the {@code component}
	 * 
	 * @param component the component to retrieve the permission setting
	 * @return Permission setting for the component if found, null otherwise
	 */
	public Permission getPermission(Component component) {
		// Quick return if the request cannot be processed
		if (permissions == null) {
			return null;
		}
			
		// Since all foreign collections are lazy, no need to refresh
		CloseableWrappedIterable<Permission> wrappedIterable = permissions.getWrappedIterable();
		try {
			for (Permission permission : wrappedIterable) {
				Component comp = permission.getComponent();
				if (comp == null) {
					continue;
				}
				
				comp.refresh();
				if (comp.equals(component)) {
					return permission;
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve permission for " + component + " because " + ex);
		} finally {
			try {
				wrappedIterable.close();
			} catch (SQLException e) {
				// Ignore
			}
		}
		return null;
	}
	
	public ForeignCollection<History> getHistories() {
		return histories;
	}
	
	/**
	 * Returns run history for the {@code component}
	 * 
	 * @param component the component to retrieve run history
	 * @return History object for the component if found, null otherwise
	 */
	public History getHistory(Component component) {
		// Quick return if the request cannot be processed
		if (histories == null) {
			return null;
		}
		
		// Since all foreign collections are lazy, no need to refresh
		CloseableWrappedIterable<History> wrappedIterable = histories.getWrappedIterable();
		try {
			for (History history : wrappedIterable) {
				Component comp = history.getComponent();
				if (comp == null) {
					continue;
				}
				
				comp.refresh();
				if (comp.equals(component)) {
					return history;
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot retrieve history for " + component + " because " + ex);
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
		return getIdentifier();
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<$User> CREATOR = new Creator<$User>() {
		
		@Override
		public $User createFromParcel(Parcel source) {
			$User result = null;
			try {
				String className = source.readString();
				result = ($User) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a User object from a Parcel. Ignored.
				Log.e(TAG, "Cannot create User from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public $User[] newArray(int size) {
			return new $User[size];
		}
	};
	
}
