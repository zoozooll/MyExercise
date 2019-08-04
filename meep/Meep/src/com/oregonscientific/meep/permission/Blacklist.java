/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The Blacklist object that represents a blacklist object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "blacklists")
public class Blacklist extends Model<Blacklist, Long> implements Identity<Long> {
	
	private static final String LOG_TAG = "Blacklist";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String TYPE_FIELD_NAME = "type";
	public static final String ENTRY_FIELD_NAME = "entry";
	public static final String LOCALE_FIELD_NAME = "locale";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "blacklist_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_blacklist_user",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Omit
	private $User user;
	
	@DatabaseField(
			columnName = TYPE_FIELD_NAME,
			indexName = "blacklist_type_idx",
			canBeNull = false)
	@SerializedName(TYPE_FIELD_NAME)
	@Expose
	private String type;
	
	@DatabaseField(columnName = ENTRY_FIELD_NAME, canBeNull = false)
	@SerializedName(ENTRY_FIELD_NAME)
	@Expose
	private String entry;
	
	@DatabaseField(
			columnName = LOCALE_FIELD_NAME + "_id", 
			canBeNull = true,
			foreign = true,
			indexName = "fk_blacklist_locale",
			columnDefinition = "BIGINT REFERENCES locales(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(LOCALE_FIELD_NAME)
	@Expose
	private Locale locale;
	
	public Blacklist() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Blacklist($User user, String type) {
		this(user, type, null);
	}
	
	public Blacklist($User user, String type, String entry) {
		this.user = user;
		this.type = type;
		this.entry = entry;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public $User getUser() {
		return user;
	}
	
	public void setUser($User user) {
		this.user = user;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale value) {
		locale = value;
	}

	/**
	 * Retrieves the type of the blacklisted item. Should be one of
	 * {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * 
	 * @return the type of this blacklisted item
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type of the blacklisted item. Should be one of
	 * {@link #BLACKLIST_TYPE_KEYWORD} or {@link #BLACKLIST_TYPE_BROWSER}
	 * 
	 * @param type type of this blacklisted item
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEntry() {
		return entry;
	}
	
	public void setEntry(String entry) {
		this.entry = entry;
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
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Blacklist> CREATOR = new Creator<Blacklist>() {
		
		@Override
		public Blacklist createFromParcel(Parcel source) {
			Blacklist result = null;
			try {
				String className = source.readString();
				result = (Blacklist) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Blacklist object from a Parcel. Ignored.
				Log.e(LOG_TAG, "Cannot create Blacklist from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public Blacklist[] newArray(int size) {
			return new Blacklist[size];
		}
	};
	
}
