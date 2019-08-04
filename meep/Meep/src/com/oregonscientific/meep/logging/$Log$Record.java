/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The object that represents a log record in the underlying data store
 */
@DatabaseTable(tableName = "log_records")
class $Log$Record extends Model<$Log$Record, Long>  {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String TYPE_FIELD_NAME = "type";
	public static final String USER_FIELD_NAME = "user";
	public static final String MESSAGE_FIELD_NAME = "message";
	public static final String BUNDLE_NAME_FIELD_NAME = "bundle_name";
	public static final String RESOURCE_FIELD_NAME = "resource";
	public static final String LEVEL_FIELD_NAME = "level";
	public static final String SENT_FIELD_NAME = "sent";
	public static final String SENDING_FIELD_NAME = "sending";
	public static final String LAST_ATTEMPT_DATE_FIELD_NAME = "last_attempt_date";
	public static final String PARAMETERS_FIELD_NAME = "parameters";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "log_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = TYPE_FIELD_NAME,
			canBeNull = false,
			indexName = "log_type_idx")
	@SerializedName(TYPE_FIELD_NAME)
	@Expose
	private String type;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			canBeNull = false,
			foreign = true,
			indexName = "fk_logs_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	private $User user;
	
	@DatabaseField(columnName = BUNDLE_NAME_FIELD_NAME)
	@SerializedName(BUNDLE_NAME_FIELD_NAME)
	@Expose
	private String resourceBundleName;
	
	@DatabaseField(columnName = MESSAGE_FIELD_NAME)
	@SerializedName(MESSAGE_FIELD_NAME)
	@Expose
	private String message;
	
	@DatabaseField(columnName = RESOURCE_FIELD_NAME)
	@SerializedName(RESOURCE_FIELD_NAME)
	@Expose
	private String resourceName;
	
	@DatabaseField(
			columnName = LEVEL_FIELD_NAME,
			canBeNull = false)
	@SerializedName(LEVEL_FIELD_NAME)
	@Expose
	private String level;
	
	@DatabaseField(columnName = SENT_FIELD_NAME)
	@Omit
	private boolean sent;
	
	@DatabaseField(columnName = SENDING_FIELD_NAME)
	@Omit
	private boolean sending;
	
	@DatabaseField(
			columnName = LAST_ATTEMPT_DATE_FIELD_NAME,
			canBeNull = true,
			dataType = DataType.DATE_STRING,
			format = "yyyy-MM-dd HH:mm:ss")
	@SerializedName(LAST_ATTEMPT_DATE_FIELD_NAME)
	@Expose
	@Omit
	private Date lastAttemptDate;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(PARAMETERS_FIELD_NAME)
	@Expose
	@Omit
	private ForeignCollection<$Parameter> parameters;
	
	public $Log$Record() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	$Log$Record(String type, String level, String message) {
		if (level == null) {
			throw new NullPointerException("Level of a log record cannot be null");
		}
		this.type = type;
		this.level = level;
		this.message = message;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public $User getUser() {
		return user;
	}
	
	public void setUser($User value) {
		user = value;
	}
	
	/**
	 * Gets whether or not the log record is sent to remote server
	 * 
	 * @return true if the log record is sent already, false otherwise
	 */
	public boolean isSent() {
		return sent;
	}
	
	/**
	 * Sets whether or not the log record is sent to remote server already
	 * 
	 * @param value true if the log record is sent, false otherwise
	 */
	public void setSent(boolean value) {
		sent = value;
	}
	
	/**
	 * Gets whether or not the log record is being sent to remote server
	 * 
	 * @return true if the log record is in the process of sending to remote server, false otherwise
	 */
	public boolean isSending() {
		return sending;
	}
	
	/**
	 * Sets whether or not the log record is in process of being sent to remote server
	 * 
	 * @param value {@code true} if this log record is being sent, {@code false} otherwise
	 */
	public void setSending(boolean value) {
		sending = value;
	}
	
	/**
	 * Gets the date that the log record was last attempted to be sent to remote server 
	 * 
	 * @return the date that this log record was last attempted to be sent to remote server
	 */
	public Date getLastAttemptDate() {
		return lastAttemptDate;
	}
	
	/**
	 * Sets the date that the log record was last attempted to be sent to remote server
	 * 
	 * @param value the date that the log record was last attempted to be sent to remote server
	 */
	public void setLastAttemptDate(Date value) {
		lastAttemptDate = value;
	}
	
	/**
     * Gets the raw message.
     *
     * @return the raw message, may be {@code null}.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the raw message. When this record is formatted by a logger that has
     * a localization resource bundle that contains an entry for {@code message},
     * then the raw message is replaced with its localized version.
     *
     * @param message
     *            the raw message to set, may be {@code null}.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * Gets the name of the resource bundle.
     *
     * @return the name of the resource bundle, {@code null} if none is
     *         available or the message is not localizable.
     */
    public String getResourceBundleName() {
        return resourceBundleName;
    }

    /**
     * Sets the name of the resource bundle.
     *
     * @param resourceBundleName
     *            the name of the resource bundle to set.
     */
    public void setResourceBundleName(String resourceBundleName) {
        this.resourceBundleName = resourceBundleName;
    }
    
    /**
     * Gets the parameters.
     *
     * @return a list of parameters or {@code null} if there are no
     *         parameters.
     */
    public ForeignCollection<$Parameter> getParameters() {
    	return parameters;
    }
    
    /**
     * Gets the logging level.
     *
     * @return the logging level.
     */
    public Level getLevel() {
    	return Level.parse(level);
    }

    /**
     * Sets the logging level.
     *
     * @param level
     *            the level to set.
     * @throws NullPointerException
     *             if {@code level} is {@code null}.
     */
    public void setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level == null");
        }
        this.level = level.getName();
    }
    
    /**
     * Gets the name of the resource.
     *
     * @return the resource name.
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the name of the resource.
     *
     * @param resourceName
     *            the resource name to set.
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    /**
     * Deserializes the given object into a {@link $Log$Record} 
     * 
     * @return the {@link $Log$Record} object
     */
    public static $Log$Record readObject() {
    	return null;
    }

}
