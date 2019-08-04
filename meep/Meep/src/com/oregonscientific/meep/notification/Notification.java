package com.oregonscientific.meep.notification;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oregonscientific.meep.database.Model;

/**
 * A class that represents how a persistent notification is to be presented to
 * the user using the {@link com.oregonscientific.meep.notification.NotificationManager}.
 */
public class Notification implements Parcelable {
	
	/**
	 * The identifier of the {@link Notification}
	 */
	@SerializedName($Notification.ID_FIELD_NAME)
	@Expose
	public long id;
	
	/**
	 * A timestamp related to this notification, in milliseconds since the epoch.
	 * 
	 * Default value: {@link System#currentTimeMillis() Now}.
	 * 
	 * This timestamp represents the time the notification is last persisted in the system. 
	 */
	@SerializedName(Model.LAST_MODIFIED_DATE_FIELD_NAME)
	@Expose
	public long when;
	
	/**
	 * The URL of an image to use as the icon in notification center.
	 */
	@SerializedName($Notification.ICON_FIELD_NAME)
	@Expose
	public String icon;
	
	/**
	 * URL of the picture attachment of the notification.
	 */
	@SerializedName($Notification.PICTURE_FIELD_NAME)
	@Expose
	public String picture;
	
	/**
	 * The number of events that this notification represents. For example, a new conversation
	 * notification, this could be the number of unread conversations
	 * 
	 * The system may not may not use this field to modify the appearance of notifications. For 
	 * example, the system may aggregate the total number of unread notifications of certain type
	 * and display that number.
	 */
	@SerializedName($Notification.NUMBER_FIELD_NAME)
	@Expose
	public int number;
	
	/**
	 * The intent to execute when the expanded status entry is clicked. If no intent is specified,
	 * the system will merely flag the notification as read.
	 */
	@SerializedName($Notification.CONTENT_INTENT_FIELD_NAME)
	@Expose
	public Intent contentIntent;
	
	/**
	 * The intent to execute when the positive button in an {@link #KIND_ALERT} notification is clicked
	 */
	@SerializedName($Notification.POSITIVE_INTENT_FIELD_NAME)
	@Expose
	public Intent positiveIntent;
	
	/**
	 * The intent to execute when the negative button in an {@link #KIND_ALERT} notification is clicked
	 * 
	 * @see #KIND_ALERT
	 */
	@SerializedName($Notification.NEGATIVE_INTENT_FIELD_NAME)
	@Expose
	public Intent negativeIntent;
	
	/**
	 * The intent to execute when the neutral button in an {@link #KIND_ALERT} notification is clicked
	 * 
	 * @see #KIND_ALERT
	 */
	@SerializedName($Notification.NEUTRAL_INTENT_FIELD_NAME)
	@Expose
	public Intent neutralIntent;

	/**
	 * Calls {@link android.content.Context#startActivity(Intent)} when
	 * {@link #contentIntent} is executed
	 * 
	 * @see {@link #ACTION_SEND_BROADCAST}, {@link #ACTION_START_SERVICE}
	 */
	public static final String ACTION_START_ACTIVITY = "activity";
	
	/**
	 * Calls {@link android.content.Context#startService(Intent)} when
	 * {@link #contentIntent} is executed
	 * 
	 * @see {@link #ACTION_START_ACTIVITY}, {@link #ACTION_SEND_BROADCAST}
	 */
	public static final String ACTION_START_SERVICE = "service";
	
	/**
	 * Calls {@link android.content.Context#sendBroadcast(Intent)} when
	 * {@link #contentIntent} is executed
	 * 
	 * @see {@link #ACTION_START_ACTIVITY}, {@link #ACTION_START_SERVICE}
	 */
	public static final String ACTION_SEND_BROADCAST = "broadcast";

	/**
	 * The action to be taken when executing the {@link #contentIntent}. It
	 * should be one of {@link #ACTION_START_ACTIVITY},
	 * {@link #ACTION_START_SERVICE}, or {@link #ACTION_SEND_BROADCAST}. Other
	 * values are ignored and {@link #contentIntent} will not be executed when
	 * the notification is clicked
	 */
	@SerializedName($Notification.CONTENT_ACTION_FIELD_NAME)
	@Expose
	public String contentAction;
	
	/**
	 * The action to be taken when executing the {@link #positiveIntent}. It
	 * should be one of {@link #ACTION_START_ACTIVITY},
	 * {@link #ACTION_START_SERVICE}, or {@link #ACTION_SEND_BROADCAST}. Other
	 * values are ignored and {@link #positiveIntent} will not be executed when
	 * the notification is clicked
	 */
	@SerializedName($Notification.POSITIVE_ACTION_FIELD_NAME)
	@Expose
	public String positiveAction;
	
	/**
	 * The action to be taken when executing the {@link #negativeIntent}. It
	 * should be one of {@link #ACTION_START_ACTIVITY},
	 * {@link #ACTION_START_SERVICE}, or {@link #ACTION_SEND_BROADCAST}. Other
	 * values are ignored and {@link #negativeIntent} will not be executed when
	 * the notification is clicked
	 */
	@SerializedName($Notification.NEGATIVE_ACTION_FIELD_NAME)
	@Expose
	public String negativeAction;
	
	/**
	 * The action to be taken when executing the {@link #neutralIntent}. It
	 * should be one of {@link #ACTION_START_ACTIVITY},
	 * {@link #ACTION_START_SERVICE}, or {@link #ACTION_SEND_BROADCAST}. Other
	 * values are ignored and {@link #neutralIntent} will not be executed when
	 * the notification is clicked
	 */
	@SerializedName($Notification.NEUTRAL_ACTION_FIELD_NAME)
	@Expose
	public String neutralAction;
	
	/**
	 * The one line of text to display at the top of a notification
	 */
	@SerializedName($Notification.TITLE_FIELD_NAME)
	@Expose
	public String title;
	
	/**
	 * Text to display in notification center when the notification is posted. This will override
	 * the any resource settings.
	 */
	@SerializedName($Notification.TEXT_FIELD_NAME)
	@Expose
	public String text;

	/**
	 * The name of the resource bundle to use to localize the raw message during
	 * formatting. If no resource name is specified, specifying the resource
	 * bundle name will have no effect. That is, the system will display the
	 * text specified in notification center
	 */
	@SerializedName($Notification.RESOURCE_BUNDLE_NAME_FIELD_NAME)
	@Expose
	public String resourceBundleName;
	
	/**
	 * The name of the resource in the resource bundle to use to localize the
	 * title during formatting.
	 */
	@SerializedName($Notification.RESOURCE_TITLE_FIELD_NAME)
	@Expose
	public String resourceTitle;

	/**
	 * The name of the resource in the resource bundle to use to localize the
	 * raw message during formatting.
	 */
	@SerializedName($Notification.RESOURCE_TEXT_FIELD_NAME)
	@Expose
	public String resourceText;
	
	/**
	 * Notification style: a notification that includes a title, one line of text and a progress bar
	 */
	public static final int STYLE_PROGRESS = 3;
	
	/**
	 * Notification style: a large format notification that includes a large image attachment
	 */
	public static final int STYLE_BIG_PICTURE = 2;
	
	/**
	 * Notification style: a large format notification that includes a lot of text
	 */
	public static final int STYLE_BIG_TEXT = 1;
	
	/**
	 * Notification style: the default format that includes a title and one line of text
	 */
	public static final int STYLE_DEFAULT = 0;
	
	/**
	 * The style of the notification. The notification system only recognize one
	 * of 
	 */
	@SerializedName($Notification.STYLE_FIELD_NAME)
	@Expose
	public int style;
	
	/**
	 * Notification type: news, promotion or advertisement
	 */
	public static final String KIND_NEWS = "meep.news";
	
	/**
	 * Notification type: incoming direct message (chat message, etc)
	 */
	public static final String KIND_MESSAGE = "meep.message";
	
	/**
	 * Notification type: alerts
	 */
	public static final String KIND_ALERT = "meep.alert";
	
	/**
	 * Notification type: MEEP store requests
	 */
	public static final String KIND_STORE = "meep.store";

	/**
	 * Notification type: MEEP store requests
	 */
	public static final String KIND_WARNING = "meep.warning";
	
	/**
	 * The kind of notification. Should be one of {@link #KIND_NEWS},
	 * {@link #KIND_MESSAGE} or {@link #KIND_ALERT}
	 */
	@SerializedName($Notification.KIND_FIELD_NAME)
	@Expose
	public String kind;

	/**
	 * The current value of the progress of an operation. This value must be
	 * less than or equal to {@link progressMax}. All {@link progress},
	 * {@link progressMax} and {@link progressIntermediate} will be ignored and
	 * no progress bar will be displayed if the type of the notification is not
	 * {@link STYLE_PROGRESS}
	 */
	@SerializedName($Notification.PROGRESS_FIELD_NAME)
	@Expose
	public int progress;

	/**
	 * The 100% value of the progress of an operation. This value must be
	 * greater than 0. A maximum that is less than or equal to 0 will be
	 * considered as an intermediate mode.
	 * 
	 * {@see progress}
	 */
	@SerializedName($Notification.PROGRESS_MAX_FIELD_NAME)
	@Expose
	public int progressMax;

	/**
	 * Determines whether or not a progress is in indeterminate mode. In
	 * indeterminate mode, {@link progress} is ignored and the progress bar
	 * should display an infinite animation.
	 * 
	 * {@see progress}
	 */
	@SerializedName($Notification.PROGRESS_INDETERMINATE_FIELD_NAME)
	@Expose
	public boolean progressIndeterminate;
	
	/**
	 * Bit to be bitwise-ored into the {@link #flags} field that should be
     * set if this notification is read
	 */
	public static final int FLAG_READ = 0x00000001;
	
	/**
     * Bit to be bitwise-ored into the {@link #flags} field that should be
     * set if this notification should be marked as important and rendered
     * differently in Notification Center
     */
	public static final int FLAG_IMPORTANT = 0x00000010;
	
	@SerializedName($Notification.FLAGS_FIELD_NAME)
	@Expose
	public int flags;
	
	/**
     * Constructs a Notification object with default values.
     * User should use {@link Builder} instead.
     */
	Notification() {
		when = System.currentTimeMillis();
	}
	
	/**
	 * Unflatten the notification from a parcel.
	 */
	Notification(Parcel parcel) {
		id = parcel.readLong();
		when = parcel.readLong();
		
		if (parcel.readInt() != 0) {
			icon = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			picture = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			title = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			text = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			resourceBundleName = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			resourceTitle = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			resourceText = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			kind = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			contentIntent = Intent.CREATOR.createFromParcel(parcel);
		}
		
		if (parcel.readInt() != 0) {
			contentAction = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			positiveIntent = Intent.CREATOR.createFromParcel(parcel);
		}
		
		if (parcel.readInt() != 0) {
			positiveAction = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			negativeIntent = Intent.CREATOR.createFromParcel(parcel);
		}
		
		if (parcel.readInt() != 0) {
			negativeAction = parcel.readString();
		}
		
		if (parcel.readInt() != 0) {
			neutralIntent = Intent.CREATOR.createFromParcel(parcel);
		}
		
		if (parcel.readInt() != 0) {
			neutralAction = parcel.readString();
		}
		
		number = parcel.readInt();
		style = parcel.readInt();
		progress = parcel.readInt();
		progressMax = parcel.readInt();
		progressIndeterminate = parcel.readByte() == 1;
		flags = parcel.readInt();
	}
	
	/**
	 * Returns a JSON representation of this {@link Notification}
	 */
	public String toJson() {
		// Creates the JSON builder
		Gson gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.registerTypeAdapterFactory(new IntentTypeAdapterFactory())
				.serializeNulls()
				.create();
		return gson.toJson(this, getClass());
	}
	
	/**
	 * Deserializes the given JSON into a {@link Notification} object
	 * 
	 * @param json the JSON to deserialize
	 * @return the {@link Notification} object
	 */
	public static Notification fromJson(String json) {
		// Creates the JSON builder
		Gson gson = new GsonBuilder()
				.excludeFieldsWithoutExposeAnnotation()
				.registerTypeAdapterFactory(new IntentTypeAdapterFactory())
				.serializeNulls()
				.create();
		return gson.fromJson(json, Notification.class);
	}
	
	@Override
	public boolean equals(Object value) {
		if (value != null && value instanceof Notification) {
			return id == ((Notification) value).id;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return toJson();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeLong(when);
		
		if (icon != null) {
			dest.writeInt(1);
			dest.writeString(icon);
		} else {
			dest.writeInt(0);
		}
		
		if (picture != null) {
			dest.writeInt(1);
			dest.writeString(picture);
		} else {
			dest.writeInt(0);
		}
		
		if (title != null) {
			dest.writeInt(1);
			dest.writeString(title);
		} else {
			dest.writeInt(0);
		}
		
		if (text != null) {
			dest.writeInt(1);
			dest.writeString(text);
		} else {
			dest.writeInt(0);
		}
		
		if (resourceBundleName != null) {
			dest.writeInt(1);
			dest.writeString(resourceBundleName);
		} else {
			dest.writeInt(0);
		}
		
		if (resourceTitle != null) {
			dest.writeInt(1);
			dest.writeString(resourceTitle);
		} else {
			dest.writeInt(0);
		}
		
		if (resourceText != null) {
			dest.writeInt(1);
			dest.writeString(resourceText);
		} else {
			dest.writeInt(0);
		}
		
		if (kind != null) {
			dest.writeInt(1);
			dest.writeString(kind);
		} else {
			dest.writeInt(0);
		}
		
		if (contentIntent != null) {
			dest.writeInt(1);
			contentIntent.writeToParcel(dest, 0);
		} else {
			dest.writeInt(0);
		}
		
		if (contentAction != null) {
			dest.writeInt(1);
			dest.writeString(contentAction);
		} else {
			dest.writeInt(0);
		}
		
		if (positiveIntent != null) {
			dest.writeInt(1);
			positiveIntent.writeToParcel(dest, 0);
		} else {
			dest.writeInt(0);
		}
		
		if (positiveAction != null) {
			dest.writeInt(1);
			dest.writeString(positiveAction);
		} else {
			dest.writeInt(0);
		}
		
		if (negativeIntent != null) {
			dest.writeInt(1);
			negativeIntent.writeToParcel(dest, 0);
		} else {
			dest.writeInt(0);
		}
		
		if (negativeAction != null) {
			dest.writeInt(1);
			dest.writeString(negativeAction);
		} else {
			dest.writeInt(0);
		}
		
		if (neutralIntent != null) {
			dest.writeInt(1);
			neutralIntent.writeToParcel(dest, 0);
		} else {
			dest.writeInt(0);
		}
		
		if (neutralAction != null) {
			dest.writeInt(1);
			dest.writeString(neutralAction);
		} else {
			dest.writeInt(0);
		}
		
		dest.writeInt(number);
		dest.writeInt(style);
		dest.writeInt(progress);
		dest.writeInt(progressMax);
		dest.writeByte((byte) (progressIndeterminate ? 1 : 0));
		dest.writeInt(this.flags);
	}
	
	/**
	 * Parcelable.Creator that instantiates Notification objects
	 */
	public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
		public Notification createFromParcel(Parcel parcel) {
			return new Notification(parcel);
		}

		public Notification[] newArray(int size) {
			return new Notification[size];
		}
	};
	
	public static class Builder {
		
		private long mId;
		private long mWhen;
		private String mIcon;
		private String mPicture;
		private String mTitle;
		private String mText;
		private String mResourceBundle;
		private String mResourceTitle;
		private String mResourceText;
		private String mKind;
		private int mNumber;
		private int mStyle;
		private Intent mContentIntent;
		private String mContentAction;
		private Intent mPositiveIntent;
		private String mPositiveAction;
		private Intent mNegativeIntent;
		private String mNegativeAction;
		private Intent mNeutralIntent;
		private String mNeutralAction;
		private int mFlags;
		
		private Style mStyler;
		
		/**
		 * Construct a new Builder with default style
		 */
		public Builder() {
			mWhen = System.currentTimeMillis();
			mStyle = STYLE_DEFAULT;
		}

		/**
		 * Specifies the identifier of the notification. This identifier should
		 * be unique across the entire system.
		 */
		public Builder setIdentifier(long id) {
			mId = id;
			return this;
		}
		
		/**
         * Add a timestamp pertaining to the notification (usually the time the event occurred).
         * It will be shown in the notification content view by default; use
         * {@link Builder#setShowWhen(boolean) setShowWhen} to control this.
         *
         * @see Notification#when
         */
        public Builder setWhen(long when) {
            mWhen = when;
            return this;
        }
        
        /**
         * Set the icon resource, which will be used to represent the notification in the
         * status bar.
         *
         * The platform template for the expanded view will draw this icon in the left.
         *
         * @param icon
         *            URL to an image to use as an icon for this {@link Notification}
         * @see Notification#icon
         */
        public Builder setIcon(String icon) {
            mIcon = icon;
            return this;
        }
        
        /**
         * Add a large picture to the notification
         * 
         * @param picture URL to the large picture
         * @see Notification#picture
         */
        public Builder setPicture(String picture) {
        	mPicture = picture;
        	return this;
        }
        
        /**
         * Set the first line of text in the platform notification template.
         */
        public Builder setContentTitle(String title) {
        	mTitle = title;
        	return this;
        }
        
        /**
         * Set the text in the platform notification template
         */
        public Builder setContentText(String text) {
        	mText = text;
        	return this;
        }
        
        /**
         * Supply an {@link Intent} to be sent when the notification is clicked.
         *
         * @see Notification#contentIntent 
         */
        public Builder setContentIntent(Intent intent, String action) {
        	mContentIntent = intent;
        	mContentAction = action;
        	return this;
        }
        
        /**
         * Supply an {@link Intent} to be sent when the positive button in a notification of {@link Notification#KIND_ALERT} is clicked.
         * 
         * @see Notification#positiveIntent
         */
        public Builder setPositiveIntent(Intent intent, String action) {
        	mPositiveIntent = intent;
        	mPositiveAction = action;
        	return this;
        }
        
        /**
         * Supply an {@link Intent} to be sent when the negative button in a notification of {@link Notification#KIND_ALERT} is clicked.
         * 
         * @see Notification#negativeIntent
         */
        public Builder setNegativeIntent(Intent intent, String action) {
        	mNegativeIntent = intent;
        	mNegativeAction = action;
        	return this;
        }
        
        /**
         * Supply an {@link Intent} to be sent when the neutral button in a notification of {@link Notification#KIND_ALERT} is clicked.
         * 
         * @see Notification#positiveIntent
         */
        public Builder setNeutralIntent(Intent intent, String action) {
        	mNeutralIntent = intent;
        	mNeutralAction = action;
        	return this;
        }
        
        /**
         * Sets the name of the resource bundle
         */
        public Builder setResourceBundle(String bundleName) {
        	mResourceBundle = bundleName;
        	return this;
        }

		/**
		 * Sets the resource key in the resource bundle to localize the title in
		 * the platform notification template. Setting this key will have no effect
		 * if the name of the resource bundle is not specified
		 */
        public Builder setResourceTitle(String titleKey) {
        	mResourceTitle = titleKey;
        	return this;
        }

		/**
		 * Sets the resource key in the resource bundle to localize the text in
		 * the platform notification template. Setting this key will have no effect
		 * if the name of the resource bundle is not specified
		 */
        public Builder setResourceText(String textKey) {
        	mResourceText = textKey;
        	return this;
        }
        
        /**
         * Set the badge number at the right-hand side of the notification.  
         */
        public Builder setNumber(int number) {
            mNumber = number;
            return this;
        }
        
        /**
         * Sets the kind of the notification. Required.
         */
        public Builder setKind(String kind) {
        	mKind = kind;
        	return this;
        }
        
        /**
         * Mark this notification as important.
         * 
         * @see Notification#FLAG_IMPORTANT
         */
        public Builder setImportant(boolean important) {
        	setFlag(FLAG_IMPORTANT, important);
        	return this;
        }
        
        /**
         * Add a rich notification styler to create the notification
         */
        private Builder setStyler(Style styler) {
        	if (mStyler != styler) {
        		mStyler = styler;
        		if (mStyler != null) {
        			mStyler.setBuilder(this);
        		}
        	}
        	return this;
        }
        
        private void setFlag(int mask, boolean value) {
        	if (value) {
        		mFlags |= mask;
        	} else {
        		mFlags &= ~mask;
        	}
        }
        
        private Notification buildUnstyled() {
        	Notification n = new Notification();
        	
        	n.id = mId;
        	n.when = mWhen;
        	n.icon = mIcon;
        	n.picture = mPicture;
        	n.title = mTitle;
        	n.text = mText;
        	n.resourceBundleName = mResourceBundle;
        	n.resourceTitle = mResourceTitle;
        	n.resourceText = mResourceText;
        	n.kind = mKind;
        	n.number = mNumber;
        	n.contentIntent = mContentIntent;
        	n.contentAction = mContentAction;
        	n.positiveIntent = mPositiveIntent;
        	n.positiveAction = mPositiveAction;
        	n.negativeIntent = mNegativeIntent;
        	n.negativeAction = mNegativeAction;
        	n.neutralIntent = mNeutralIntent;
        	n.neutralAction = mNeutralAction;
        	n.style = mStyle;
        	n.flags = mFlags;
        	
        	return n;
        }
        
        /**
         * Combine all of the options that have been set and return a new {@link Notification}
         * object.
         */
        public Notification build() {
        	if (mStyler != null) {
        		return mStyler.build();
        	} else {
        		return buildUnstyled();
        	}
        }
		
	}
	
	/**
	 * An object that can apply a rich notification style to a
	 * {@link Notification.Builder} object.
	 */
	public static abstract class Style {
		
		protected Builder mBuilder;
		
		public void setBuilder(Builder builder) {
			if (mBuilder != builder) {
				mBuilder = builder;
				if (mBuilder != null) {
					mBuilder.setStyler(this);
				}
			}
		}
		
		protected void checkBuilder() {
            if (mBuilder == null) {
                throw new IllegalArgumentException("Style requires a valid Builder object");
            }
        }
		
		public abstract Notification build();
	}
	
	/**
	 * Helper class for generating a notification that include a progress bar
	 */
	public static class ProgressBarStyle extends Style {
		
		private int mProgressMax;
		private int mProgress;
		private boolean mProgressIndeterminate;
		
		public ProgressBarStyle() {
		}
		
		public ProgressBarStyle(Builder builder) {
			setBuilder(builder);
		}
		
		/**
         * Set the progress this notification represents.
         *
         * The platform template will represent this using a {@link ProgressBar}.
         */
        public ProgressBarStyle setProgress(int max, int progress, boolean indeterminate) {
            mProgressMax = max;
            mProgress = progress;
            mProgressIndeterminate = indeterminate;
            return this;
        }
        
        @Override
        public Notification build() {
        	checkBuilder();
        	Notification n = mBuilder.buildUnstyled();
        	n.style = Notification.STYLE_PROGRESS;
        	n.progress = mProgress;
        	n.progressMax = mProgressMax;
        	n.progressIndeterminate = mProgressIndeterminate;
        	return n;
        }
	}

}
