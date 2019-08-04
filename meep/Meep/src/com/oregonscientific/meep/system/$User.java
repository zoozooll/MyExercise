/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.system;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * An object used internally in the package to uniquely identify an
 * user account
 */
@DatabaseTable(tableName = "users")
class $User extends Model<$User, Long> implements Identity<Long> {
	
	private static final String TAG = "$User";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String IDENTIFIER_FIELD_NAME = "identifier";
	
	public static final String RECOMMENDATIONS_FIELD_NAME = "recommendations";
	
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
	@SerializedName(RECOMMENDATIONS_FIELD_NAME)
	@Omit
	private ForeignCollection<CommandHistory> commands;
	
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
	
	public String getIdentifier() {
		return identifier;
	}
	
	public ForeignCollection<CommandHistory> getCommands() {
		return commands;
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
