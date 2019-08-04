/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.system;

import java.util.Date;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;

/**
 * The CommandHistory object that represents a commandHistory object in the
 * underlying datastore
 */
@DatabaseTable(tableName = "command_histories")
public class CommandHistory extends Model<CommandHistory, Long> implements Identity<Long> {
	
	private static final String TAG = "CommandHistory";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String COMMAND_FIELD_NAME = "command";
	public static final String EXECUTE_DATE_FIELD_NAME = "execute_date";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "command_history_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			canBeNull = false,
			indexName = "fk_command_histories_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Expose
	private $User user;
	
	@DatabaseField(columnName = COMMAND_FIELD_NAME, canBeNull = false)
	@SerializedName(COMMAND_FIELD_NAME)
	@Expose
	private String command;
	
	@DatabaseField(columnName = EXECUTE_DATE_FIELD_NAME)
	@SerializedName(EXECUTE_DATE_FIELD_NAME)
	@Expose
	private Date executeDate;
	
	public CommandHistory() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public CommandHistory(String command) {
		this.command = command;
		this.executeDate = new Date();
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
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public Date getExecuteDate() {
		return executeDate;
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
	public static final Creator<CommandHistory> CREATOR = new Creator<CommandHistory>() {
		
		@Override
		public CommandHistory createFromParcel(Parcel source) {
			CommandHistory result = null;
			try {
				String className = source.readString();
				result = (CommandHistory) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a User object from a Parcel. Ignored.
				Log.e(TAG, "Cannot create CommandHistory from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public CommandHistory[] newArray(int size) {
			return new CommandHistory[size];
		}
	};
	
}
