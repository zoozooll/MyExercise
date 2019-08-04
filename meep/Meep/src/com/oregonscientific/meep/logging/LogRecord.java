/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.internal.DateTypeAdapter;
import com.oregonscientific.meep.database.internal.ForeignCollectionInstanceCreator;
import com.oregonscientific.meep.database.internal.ForeignCollectionTypeAdapterFactory;
import com.oregonscientific.meep.database.internal.ModelSerializationPolicy;
import com.oregonscientific.meep.database.internal.ModelTypeAdapterFactory;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The Log object that represents a log object in the underlying data store
 */
public class LogRecord implements Parcelable {
	
	/**
	 * The identifier of the {@link LogRecord}
	 */
	@SerializedName($Log$Record.ID_FIELD_NAME)
	@Expose
	private long id;
	
	/**
	 * The type of the log record
	 */
	@SerializedName($Log$Record.TYPE_FIELD_NAME)
	@Expose
	private String type;
	
	/**
	 * The name of the resource bundle to use to interpret the message text
	 */
	@SerializedName($Log$Record.BUNDLE_NAME_FIELD_NAME)
	@Expose
	private String resourceBundleName;
	
	/**
	 * The original message text
	 */
	@SerializedName($Log$Record.MESSAGE_FIELD_NAME)
	@Expose
	private String message;
	
	/**
     * The name of the resource to use as the logging message
     */
	@SerializedName($Log$Record.RESOURCE_FIELD_NAME)
	@Expose
	private String resourceName;
	
	/**
     * The logging level.
     */
	@SerializedName($Log$Record.LEVEL_FIELD_NAME)
	@Expose
	private String level;
	
	// The parameters
	@SerializedName($Log$Record.PARAMETERS_FIELD_NAME)
	@Expose
	private Object[] parameters;
	
	/**
	 * Constructs a {@code LogRecord} object using the supplied the logging
	 * level and message.
	 * 
	 * @param type the type of event
	 * @param level the logging level, may not be {@code null}.
	 * @param message the raw message.
	 */
	public LogRecord(String type, Level level, String message) {
		this(type, level == null ? null : level.getName(), message);
	}
	
	/**
	 * Constructs a {@code LogRecord} object using the supplied the logging
	 * level and message.
	 * 
	 * @param type the type of event
	 * @param level the logging level, may not be {@code null}.
	 * @param message the raw message.
	 */
	public LogRecord(String type, String level, String message) {
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
     * Gets the parameters.
     *
     * @return a list of parameters or {@code null} if there are no
     *         parameters.
     */
    public Object[] getParameters() {
    	return parameters;
    }
    
    /**
     * Sets the parameters.
     *
     * @param parameters
     *            the array of parameters to set, may be {@code null}.
     */
    public void setParameters(Object[] parameters) {
    	this.parameters = parameters;
    }
    
    /**
     * Determines whether or not this {@link LogRecord} contains the given {@code parameter}
     * 
     * @param parameter the parameter to check against
     * @return true if the given parameter is one of the parameters in this {@link LogRecord}, 
     * false otherwise
     */
    public boolean containsParameter(Object parameter) {
    	// Quick return if the request cannot be processed
    	if (parameters == null || parameter == null) {
    		return false;
    	}
    	
    	List<Object> params = Arrays.asList(parameters);
    	return params.contains(parameter);
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
     * Gets the logging level.
     *
     * @return the logging level.
     */
    public Level getLevel() {
    	return level == null ? null : Level.parse(level);
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
     * Return a JSON representation of the {@link LogRecord}
     */
    public String toJson() {
    	// Creates the JSON builder
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
		
		// Serialize the data model into a JSON representation
		return gson.toJson(this);
    }
    
    @Override
    public String toString() {
    	return toJson();
    }
    
    @Override
	public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(toJson());
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<LogRecord> CREATOR = new Creator<LogRecord>() {
		
		@Override
		public LogRecord createFromParcel(Parcel source) {
			return LogRecord.fromJson(source.readString());
		}
		
		@Override
		public LogRecord[] newArray(int size) {
			return new LogRecord[size];
		}
	};
	
	/**
	 * Returns the a package scoped {@link $Log$Record} representation of this {@link LogRecord}
	 * object
	 * 
	 * @param creator the creator to create foreign collections
	 * @return the resulting {@link $Log$Record} object
	 */
	$Log$Record to$Log$Record(ForeignCollectionInstanceCreator creator) {
		// Creates a custom type factory for data models
		ModelTypeAdapterFactory modelTypeAdapterFactory = Model.getTypeAdapterFactory($Log$Record.class);
		
		// Register serialization policy that only expands up to the {@link $Parameter} field
		Map<String, Object> expands = new HashMap<String, Object>();
		expands.put($Log$Record.PARAMETERS_FIELD_NAME, null);
		ModelSerializationPolicy policy = ModelSerializationPolicy.DEFAULT.disableIdFieldOnlySerialization().withExpansionTree(expands);
		modelTypeAdapterFactory.registerSerializationAdapter($Log$Record.class, policy);
		
		// Creates the foreign collection {@link TypeAdapterFactory}
		ForeignCollectionTypeAdapterFactory fcTypeAdapterFactory = new ForeignCollectionTypeAdapterFactory(creator);
		
		// Creates the JSON builder
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.registerTypeAdapterFactory(modelTypeAdapterFactory);
		gsonb.registerTypeAdapterFactory(fcTypeAdapterFactory);
		gsonb.registerTypeAdapter($Parameter.class, new $ParameterTypeAdapter());
		gsonb.registerTypeAdapter(Date.class, new DateTypeAdapter());
		Gson gson = gsonb.serializeNulls().create();
		
		String json = toJson();
		return gson.fromJson(json, $Log$Record.class);
	}
	
	/**
	 * Creates an instance of {@link LogRecord} from the given JSON 
	 * 
	 * @param json the json to parse as the {@link LogRecord}
	 * @return the {@link LogRecord}
	 */
	private static LogRecord fromJson(String json) {
		// Creates the JSON builder
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
		
		return gson.fromJson(json, LogRecord.class);
	}
	
}
