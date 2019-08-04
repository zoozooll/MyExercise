/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import java.sql.SQLException;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.CloseableIterator;
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
	
	private static final String TAG = "User";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String IDENTIFIER_FIELD_NAME = "identifier";
	public static final String NOTIFICATIONS_FIELD_NAME = "notifications";
	
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
	@SerializedName(NOTIFICATIONS_FIELD_NAME)
	@Omit
	private ForeignCollection<$Notification> notifications;
	
	$User() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	$User(String identifier) {
		this.identifier = identifier;
	}
	
	public long getId() {
		return id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public ForeignCollection<$Notification> getNotifications() {
		return notifications;
	}
	
	/**
	 * Returns the {@link $Notification} identified by {@code id}  
	 * 
	 * @param id the identifier of the {@link Notification}
	 * @return the {@link Notification} if found, {@code null} otherwise
	 */
	public $Notification getNotification(long id) {
		$Notification result = null;
		
		// Since all foreign collections are lazy, no need to refresh
		CloseableIterator<$Notification> iterator = notifications.closeableIterator();
		try {
			while (iterator.hasNext()) {
				$Notification notification = iterator.next();
				if (notification.getId() == id) {
					result = notification;
					break;
				}
			}
		} finally {
			try {
				iterator.close();
			} catch (SQLException ignored) {
				// Ignored
			}
		}
		
		return result;
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
