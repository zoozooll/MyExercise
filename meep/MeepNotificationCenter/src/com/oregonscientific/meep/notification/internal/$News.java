/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification.internal;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The News object that represents a news item in the
 * underlying datastore
 */
@DatabaseTable(tableName = "news")
public class $News extends Model<$News, Long> implements Identity<Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String IDENTIFIER_FIELD_NAME = "identifier";
	public static final String USER_FIELD_NAME = "user";
	public static final String ICON_FIELD_NAME = "icon";
	public static final String PICTURE_FIELD_NAME = "image";
	public static final String TITLE_FIELD_NAME = "headline";
	public static final String TEXT_FIELD_NAME = "content";
	public static final String URL_FIELD_NAME = "url";
	public static final String POST_DATE_FIELD_NAME = "post_date";
	public static final String NOTIFICATION_FIELD_NAME = "notification";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "news_idx")
	@Omit
	private long id;
	
	@DatabaseField(columnName = IDENTIFIER_FIELD_NAME)
	@SerializedName("news_" + ID_FIELD_NAME)
	@Expose
	private String identifier;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			canBeNull = false,
			foreign = true,
			indexName = "fk_notifications_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Omit
	private $User user;
	
	@DatabaseField(columnName = ICON_FIELD_NAME)
	@SerializedName(ICON_FIELD_NAME)
	@Expose
	private String icon;
	
	@DatabaseField(columnName = PICTURE_FIELD_NAME)
	@SerializedName(PICTURE_FIELD_NAME)
	@Expose
	private String picture;
	
	@DatabaseField(columnName = TITLE_FIELD_NAME)
	@SerializedName(TITLE_FIELD_NAME)
	@Expose
	private String title;
	
	@DatabaseField(columnName = TEXT_FIELD_NAME)
	@SerializedName(TEXT_FIELD_NAME)
	@Expose
	private String text;
	
	@DatabaseField(columnName = URL_FIELD_NAME)
	@SerializedName(URL_FIELD_NAME)
	@Expose
	private String url;
	
	@DatabaseField(columnName = POST_DATE_FIELD_NAME)
	@SerializedName(POST_DATE_FIELD_NAME)
	@Expose
	private Date postDate;
	
	@DatabaseField(columnName = NOTIFICATION_FIELD_NAME)
	@SerializedName(NOTIFICATION_FIELD_NAME)
	@Expose
	private long notificationId;
	
	$News() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long value) {
		id = value;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String value) {
		identifier = value;
	}
	
	public $User getUser() {
		return user;
	}
	
	public void setUser($User user) {
		this.user = user;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String value) {
		icon = value;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String value) {
		text = value;
	}
	
	public String getUrl() {
		return url;
	}
	
	public Date getPostDate() {
		return postDate;
	}
	
	public long getNotificationId() {
		return notificationId;
	}
	
	public void setNotificationId(long id) {
		notificationId = id;
	}
	
	@Override
	public Long getIdentity() {
		return getId();
	}

	@Override
	public String getIdentityAttribute() {
		return ID_FIELD_NAME;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}

}
