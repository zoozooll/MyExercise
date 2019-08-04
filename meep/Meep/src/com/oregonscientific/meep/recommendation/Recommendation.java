/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

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
 * The Recommendation object that represents a recommendation object in the
 * underlying datastore
 */
@DatabaseTable(tableName = "recommendations")
public class Recommendation extends Model<Recommendation, Long> implements Identity<Long> {
	
	private static final String LOG_TAG = "Recommendation";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String TYPE_FIELD_NAME = "type";
	public static final String URL_FIELD_NAME = "url";
	public static final String THUMBNAIL_FIELD_NAME = "thumbnail";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "recommendation_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			canBeNull = false,
			foreign = true,
			indexName = "fk_recommendations_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Omit
	private $User user;
	
	@DatabaseField(
			columnName = TYPE_FIELD_NAME,
			canBeNull = false)
	@SerializedName(TYPE_FIELD_NAME)
	@Expose
	private String type;
	
	@DatabaseField(columnName = URL_FIELD_NAME)
	@SerializedName(URL_FIELD_NAME)
	@Expose
	private String url;
	
	@DatabaseField(columnName = THUMBNAIL_FIELD_NAME)
	@SerializedName(THUMBNAIL_FIELD_NAME)
	@Expose
	private String thumbnail;
	
	public Recommendation() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Recommendation(String type) {
		this.type = type;
	}
	
	public Recommendation($User user, String type) {
		this.user = user;
		this.type = type;
	}
	
	public Recommendation(String type, String url, String thumbnail) {
		this.type = type;
		this.url = url;
		this.thumbnail = thumbnail;
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url == null ? url : url.replaceAll("^(?:(?:.*?:)?//)?(?:youtu\\.be|youtube\\.com)/", "");
	}
	
	public String getThumbnail() {
		return thumbnail;
	}
	
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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
	public static final Creator<Recommendation> CREATOR = new Creator<Recommendation>() {
		
		@Override
		public Recommendation createFromParcel(Parcel source) {
			Recommendation result = null;
			try {
				String className = source.readString();
				result = (Recommendation) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Recommendation object from a Parcel. Ignored.
				Log.e(LOG_TAG, "Cannot create Recommendation from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public Recommendation[] newArray(int size) {
			return new Recommendation[size];
		}
	};
	
}
