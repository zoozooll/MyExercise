/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.util.Date;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.LocalCurrentTimeStampType;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The History object that represents a history object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "histories")
public class History extends Model<History, Long> implements Identity<Long> {
	
	private static final String LOG_TAG = "History";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String COMPONENT_FIELD_NAME = "component";
	public static final String ACCUMULATED_ACTIVE_TIME_FIELD_NAME = "accumulated_active_time";
	public static final String LAST_ACTIVE_DATE_FIELD_NAME = "last_active_date";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "history_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_histories_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Expose
	private $User user;
	
	@DatabaseField(
			columnName = COMPONENT_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_histories_component_idx",
			columnDefinition = "BIGINT REFERENCES components(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(COMPONENT_FIELD_NAME)
	@Expose
	private Component component;
	
	@DatabaseField(columnName = ACCUMULATED_ACTIVE_TIME_FIELD_NAME)
	@SerializedName(ACCUMULATED_ACTIVE_TIME_FIELD_NAME)
	private Long accumulatedActiveTime;
	
	@DatabaseField(
			columnName = LAST_ACTIVE_DATE_FIELD_NAME, 
			canBeNull = false,
			persisterClass = LocalCurrentTimeStampType.class,
			version = true,
			format = "yyyy-MM-dd HH:mm:ss")
	@SerializedName(LAST_ACTIVE_DATE_FIELD_NAME)
	@Expose
	private Date lastActiveDate;
	
	public History() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public History($User user, Component component, Date lastActiveDate) {
		this.user = user;
		this.component = component;
		this.lastActiveDate = lastActiveDate;
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
	
	public Component getComponent() {
		return component;
	}
	
	public void setComponent(Component component) {
		this.component = component;
	}
	
	public Long getAccumulatedActiveTime() {
		return accumulatedActiveTime;
	}
	
	public void setAccumulatedActiveTime(Long value) {
		accumulatedActiveTime = value;
	}
	
	/**
	 * Accumulate the active running time by {@code value}
	 * 
	 * @param value the value to accumulate
	 */
	public void accumulateActiveTime(Long value) {
		if (value != null) {
			accumulatedActiveTime += value;	
		}
	}
	
	public Date getLastActiveDate() {
		return lastActiveDate;
	}
	
	public void setLastActiveDate(Date value) {
		lastActiveDate = value;
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
	public static final Creator<History> CREATOR = new Creator<History>() {
		
		@Override
		public History createFromParcel(Parcel source) {
			History result = null;
			try {
				String className = source.readString();
				result = (History) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a History object from a Parcel. Ignored.
				Log.e(LOG_TAG, "Cannot create History from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public History[] newArray(int size) {
			return new History[size];
		}
	};
	
}
