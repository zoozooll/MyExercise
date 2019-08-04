/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification;

import android.content.Intent;
import android.os.Parcel;
import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The Notification object that represents a notification object in the
 * underlying datastore
 */
@DatabaseTable(tableName = "notifications")
class $Notification extends Model<$Notification, Long> implements Identity<Long> {
	
	private static final String TAG = "Notification";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String ICON_FIELD_NAME = "icon";
	public static final String PICTURE_FIELD_NAME = "picture";
	public static final String NUMBER_FIELD_NAME = "number";
	public static final String TITLE_FIELD_NAME = "title";
	public static final String TEXT_FIELD_NAME = "text";
	public static final String RESOURCE_BUNDLE_NAME_FIELD_NAME = "resource_bundle_name";
	public static final String RESOURCE_TITLE_FIELD_NAME = "resource_title";
	public static final String RESOURCE_TEXT_FIELD_NAME = "resource_text";
	public static final String STYLE_FIELD_NAME = "style";
	public static final String KIND_FIELD_NAME = "kind";
	public static final String PROGRESS_FIELD_NAME = "progress";
	public static final String PROGRESS_MAX_FIELD_NAME = "progress_max";
	public static final String PROGRESS_INDETERMINATE_FIELD_NAME = "progress_indeterminate";
	public static final String CONTENT_INTENT_FIELD_NAME = "content_intent";
	public static final String CONTENT_ACTION_FIELD_NAME = "content_action";
	public static final String POSITIVE_INTENT_FIELD_NAME = "positive_intent";
	public static final String POSITIVE_ACTION_FIELD_NAME = "positive_action";
	public static final String NEGATIVE_INTENT_FIELD_NAME = "negative_intent";
	public static final String NEGATIVE_ACTION_FIELD_NAME = "negative_action";
	public static final String NEUTRAL_INTENT_FIELD_NAME = "neutral_intent";
	public static final String NEUTRAL_ACTION_FIELD_NAME = "neutral_action";
	public static final String FLAGS_FIELD_NAME = "flags";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "notification_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
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
	
	@DatabaseField(columnName = NUMBER_FIELD_NAME)
	@SerializedName(NUMBER_FIELD_NAME)
	@Expose
	private int number;
	
	@DatabaseField(columnName = TITLE_FIELD_NAME)
	@SerializedName(TITLE_FIELD_NAME)
	@Expose
	private String title;
	
	@DatabaseField(columnName = TEXT_FIELD_NAME)
	@SerializedName(TEXT_FIELD_NAME)
	@Expose
	private String text;
	
	@DatabaseField(columnName = RESOURCE_BUNDLE_NAME_FIELD_NAME)
	@SerializedName(RESOURCE_BUNDLE_NAME_FIELD_NAME)
	@Expose
	private String resourceBundleName;
	
	@DatabaseField(columnName = RESOURCE_TITLE_FIELD_NAME)
	@SerializedName(RESOURCE_TITLE_FIELD_NAME)
	@Expose
	private String resourceTitle;
	
	@DatabaseField(columnName = RESOURCE_TEXT_FIELD_NAME)
	@SerializedName(RESOURCE_TEXT_FIELD_NAME)
	@Expose
	private String resourceText;
	
	@DatabaseField(columnName = STYLE_FIELD_NAME)
	@SerializedName(STYLE_FIELD_NAME)
	@Expose
	private int style;
	
	@DatabaseField(columnName = KIND_FIELD_NAME)
	@SerializedName(KIND_FIELD_NAME)
	@Expose
	private String kind;
	
	@DatabaseField(columnName = PROGRESS_FIELD_NAME)
	@SerializedName(PROGRESS_FIELD_NAME)
	@Expose
	private int progress;
	
	@DatabaseField(columnName = PROGRESS_MAX_FIELD_NAME)
	@SerializedName(PROGRESS_MAX_FIELD_NAME)
	@Expose
	private int progressMax;
	
	@DatabaseField(columnName = PROGRESS_INDETERMINATE_FIELD_NAME)
	@SerializedName(PROGRESS_INDETERMINATE_FIELD_NAME)
	@Expose
	private boolean progressIndeterminate;
	
	@DatabaseField(columnName = CONTENT_INTENT_FIELD_NAME)
	@SerializedName(CONTENT_INTENT_FIELD_NAME)
	@Expose
	private String contentIntent;
	
	@DatabaseField(columnName = CONTENT_ACTION_FIELD_NAME)
	@SerializedName(CONTENT_ACTION_FIELD_NAME)
	@Expose
	private String contentAction;
	
	@DatabaseField(columnName = POSITIVE_INTENT_FIELD_NAME)
	@SerializedName(POSITIVE_INTENT_FIELD_NAME)
	@Expose
	private String positiveIntent;
	
	@DatabaseField(columnName = POSITIVE_ACTION_FIELD_NAME)
	@SerializedName(POSITIVE_ACTION_FIELD_NAME)
	@Expose
	private String positiveAction;
	
	@DatabaseField(columnName = NEGATIVE_INTENT_FIELD_NAME)
	@SerializedName(NEGATIVE_INTENT_FIELD_NAME)
	@Expose
	private String negativeIntent;
	
	@DatabaseField(columnName = NEGATIVE_ACTION_FIELD_NAME)
	@SerializedName(NEGATIVE_ACTION_FIELD_NAME)
	@Expose
	private String negativeAction;
	
	@DatabaseField(columnName = NEUTRAL_INTENT_FIELD_NAME)
	@SerializedName(NEUTRAL_INTENT_FIELD_NAME)
	@Expose
	private String neutralIntent;
	
	@DatabaseField(columnName = NEUTRAL_ACTION_FIELD_NAME)
	@SerializedName(NEUTRAL_ACTION_FIELD_NAME)
	@Expose
	private String neutralAction;
	
	@DatabaseField(columnName = FLAGS_FIELD_NAME)
	@SerializedName(FLAGS_FIELD_NAME)
	@Expose
	private int flags;
	
	$Notification() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
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
	
	public void setNumber(int value) {
		number = value;
	}
	
	public int getNumber() {
		return number;
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
	
	/**
	 * Retrieve the {@link android.content.Intent} unmarshalled from the persisted data 
	 * 
	 * @return the {@link android.content.Intent}
	 */
	public Intent getContentIntent() {
		return getIntent(contentIntent);
	}
	
	/**
	 * Specifies a {@link android.content.Intent} to be sent when the notification is clicked 
	 * 
	 * @param contentIntent
	 */
	public void setContentIntent(Intent i) {
		contentIntent = convertIntentToString(i);
	}
	
	private Intent getIntent(String intent) {
		Intent result = null;
		if (intent != null) {
			byte[] data = Base64.decode(intent, Base64.NO_WRAP);
			if (data != null) {
				Parcel parcel = Parcel.obtain();
				parcel.unmarshall(data, 0, data.length);
				parcel.setDataPosition(0);
				result = Intent.CREATOR.createFromParcel(parcel);
				parcel.recycle();
			}
		}
		return result;
	}
	
	private String convertIntentToString(Intent intent) {
		String result = null;
		if (intent != null) {
			Parcel parcel = Parcel.obtain();
			intent.writeToParcel(parcel, 0);
			byte[] data = parcel.marshall();
			result = Base64.encodeToString(data, Base64.NO_WRAP);
			parcel.recycle();
		}
		return result;
	}
	
	public String getContentAction() {
		return contentAction;
	}
	
	public void setContentAction(String value) {
		contentAction = value;
	}
	
	public Intent getPositiveIntent() {
		return getIntent(positiveIntent);
	}
	
	public void setPositiveIntent(Intent i) {
		positiveIntent = convertIntentToString(i);
	}
	
	public String getPositiveAction() {
		return positiveAction;
	}
	
	public void setPositiveAction(String action) {
		positiveAction = action;
	}
	
	public Intent getNegativeIntent() {
		return getIntent(negativeIntent);
	}
	
	public void setNegativeIntent(Intent i) {
		negativeIntent = convertIntentToString(i);
	}
	
	public String getNegativeAction() {
		return negativeAction;
	}
	
	public void setNegativeAction(String action) {
		negativeAction = action;
	}
	
	public Intent getNeutralIntent() {
		return getIntent(neutralIntent);
	}
	
	public void setNeutralIntent(Intent i) {
		neutralIntent = convertIntentToString(i);
	}
	
	public String getNeutralAction() {
		return neutralAction;
	}
	
	public void setNeutralAction(String action) {
		neutralAction = action;
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
	
	/**
	 * Gets the name of the loaded resource bundle used by this logger to localize logging messages. 
	 * 
	 * @return the name of the loaded resource bundle used by this notification
	 */
	public String getResourceBundleName() {
		return resourceBundleName;
	}
	
	public void setResourceBundleName(String value) {
		resourceBundleName = value;
	}
	
	public String getResourceTitle() {
		return resourceTitle;
	}
	
	public void setResourceTitle(String value) {
		resourceTitle = value;
	}
	
	public String getResourceText() {
		return resourceText;
	}
	
	public void setResourceText(String value) {
		resourceText = value;
	}
	
	public int getStyle() {
		return style;
	}
	
	public void setStyle(int value) {
		style = value;
	}
	
	public String getKind() {
		return kind;
	}
	
	public void setKind(String value) {
		kind = value;
	}
	
	/**
	 * Get the current level of progress
	 * 
	 * @return the current progress, between 0 and {@link #getProgressMax()}
	 */
	public int getProgress() {
		return progress < 0 ? 0 : progress > getProgressMax() ? getProgressMax() : progress;
	}

	/**
	 * Set the current progress to the specified value. Does not do anything if
	 * the progress bar is in indeterminate mode.
	 * 
	 * @param value the new progress, between 0 and get {@link #getProgressMax()}
	 */
	public void setProgress(int value) {
		if (!progressIndeterminate) {
			progress = value;
		}
	}
	
	/**
	 * The upper limit of the progress of an operation
	 */
	public int getProgressMax() {
		return progressMax;
	}
	
	/**
	 * Set the range of the progress of an operation
	 */
	public void setProgressMax(int value) {
		progressMax = value;
	}
	
	/**
	 * Indicates whether progress bar in this {@link Notification} is in indeterminate mode
	 */
	public boolean isProgressIndeterminate() {
		return progressIndeterminate;
	}
	
	/**
	 * Change the indeterminate mode for the progress bar in this {@link Notification}
	 */
	public void setProgressIndeterminate(boolean value) {
		progressIndeterminate = value;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public void setFlags(int value) {
		flags = value;
	}
	
	/**
	 * Returns a package scoped {@link $Notification} representation of the publicly
	 * accessible {@link Notification} object
	 */
	static $Notification fromNotification(Notification notification) {
		return notification == null ? null : Model.fromJson(notification.toJson(), $Notification.class);
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
	public static final Creator<$Notification> CREATOR = new Creator<$Notification>() {
		
		@Override
		public $Notification createFromParcel(Parcel source) {
			$Notification result = null;
			try {
				String className = source.readString();
				result = ($Notification) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Notification object from a Parcel. Ignored.
				Log.e(TAG, "Cannot create Notification from a Parcel: " + source.toString());
			}
			
			return result;
		}
		
		@Override
		public $Notification[] newArray(int size) {
			return new $Notification[size];
		}
		
	};
	
}
