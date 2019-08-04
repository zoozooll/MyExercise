package com.oregonscientific.meep.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The ContentType object that represents a contentType object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "contentTypes")
public class ContentType extends Model<ContentType, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String CONTENT_TYPE_FIELD_NAME = "contentType";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "contentType_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@DatabaseField(columnName = CONTENT_TYPE_FIELD_NAME, canBeNull = false)
	@SerializedName(CONTENT_TYPE_FIELD_NAME)
	@Expose
	private String contentType;
	
	public ContentType() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public ContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
}
