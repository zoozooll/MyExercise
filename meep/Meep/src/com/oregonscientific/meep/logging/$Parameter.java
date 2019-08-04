/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * An object used internally in the package to store parameters associated
 * with a {@link LogRecord}
 */
@DatabaseTable(tableName = "parameters")
class $Parameter extends Model<$Parameter, Long>  {
	
	private static final String TAG = "$Parameter";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String VALUE_FIELD_NAME = "value";
	public static final String LOG_RECORDS_FIELD_NAME = "log";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "parameter_idx")
	@SerializedName(ID_FIELD_NAME)
	@Omit
	private long id;
	
	@DatabaseField(columnName = VALUE_FIELD_NAME)
	@Expose
	private String value;
	
	@DatabaseField(
			columnName = LOG_RECORDS_FIELD_NAME + "_id",
			canBeNull = false,
			foreign = true,
			indexName = "fk_parameters_log_idx",
			columnDefinition = "BIGINT REFERENCES log_records(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(LOG_RECORDS_FIELD_NAME)
	@Omit
	private $Log$Record logRecord;
	
	$Parameter() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	$Parameter(String value) {
		this.value = value;
	}
	
	public long getId() {
		return id;
	}
	
	void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Gets the underlying value of the parameter
	 * 
	 * @return the underlying value of this parameter
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the underlying value of the parameter
	 * 
	 * @param value the value of this parameter
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the {@link LogRecord} associated with this parameter 
	 * 
	 * @return the {@link LogRecord} associated with this parameter
	 */
	public $Log$Record getLogRecord() {
		return logRecord;
	}
	
	/**
	 * Sets the {@link LogRecord} associated with this parameter 
	 * 
	 * @param logRecord the {@link LogRecord} associated with this parameter
	 */
	public void setLogRecord($Log$Record logRecord) {
		this.logRecord = logRecord;
	}
	
	@Override
	public String toString() {
		return getValue();
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<$Parameter> CREATOR = new Creator<$Parameter>() {
		
		@Override
		public $Parameter createFromParcel(Parcel source) {
			$Parameter result = null;
			try {
				String className = source.readString();
				result = ($Parameter) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a User object from a Parcel. Ignored.
				Log.e(TAG, "Cannot create User from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public $Parameter[] newArray(int size) {
			return new $Parameter[size];
		}
	};

}
