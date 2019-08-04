/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.internal.DateTypeAdapter;
import com.oregonscientific.meep.database.internal.ForeignCollectionTypeAdapterFactory;
import com.oregonscientific.meep.database.internal.ModelSerializationPolicy;
import com.oregonscientific.meep.database.internal.ModelSerializationStrategy;
import com.oregonscientific.meep.database.internal.ModelTypeAdapterFactory;
import com.oregonscientific.meep.http.Status;
import com.oregonscientific.meep.msm.internal.MessageTypeAdapterFactory;
import com.oregonscientific.meep.serialization.MessagePropertyTypeAdapterFactories;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;

/**
 * Defines a message containing meta information and protocol data that can be sent to a 
 * MEEP server.
 * 
 * @author Stanley Lam
 */
public class Message implements Parcelable {
	
	private static final String TAG = "Message";
	
	/**
	 * Recommendation: web browser
	 */
	public static final String RECOMMENDATION_BROWSER = "os-browser";
	
	/**
	 * Recommendation: YouTube
	 */
	public static final String RECOMMENDATION_YOUTUBE = "os-youtube";
	
	/**
	 * Recommendation: Add
	 */
	public static final String RECOMMENDATION_ADD = "add";
	
	
	/**
	 * Recommendation: Delete
	 */
	public static final String RECOMMENDATION_DELETE = "delete";
	
	// process
	/**
	 * Protocol: A process related to instant messaging
	 */
	public static final String PROCESS_INSTANT_MESSAGING = "chat";
	
	/**
	 * Protocol: A process related to user credentials
	 */
	public static final String PROCESS_ACCOUNT = "account";
	
	/**
	 * Protocol: A process related to permission settings
	 */
	public static final String PROCESS_PARENTAL = "parental";
	
	/**
	 * Protocol: A system process
	 */
	public static final String PROCESS_SYSTEM = "system";
	
	/**
	 * Protocol: A process related to MEEP store
	 */
	public static final String PROCESS_STORE = "store";
	
	// operation code
	/**
	 * Operation Code: Acknowledges that the message is received
	 */
	public static final String OPERATION_CODE_ACK = "ack";
	
	/**
	 * Operation Code: Sends log records to remote server
	 */
	public static final String OPERATION_CODE_LOG = "log";
	
	/**
	 * Operation Code: A message in a conversation between you and a particular
	 * recipient.
	 */
	public static final String OPERATION_CODE_TEXT_MSG = "text-msg";
	
	public static final String OPERATION_CODE_IMG = "mm-msg";
	
	/**
	 * Operation Code: Searches for a user with the specified MEEP tag on
	 * server.
	 */
	public static final String OPERATION_CODE_SEARCH_FRIEND = "search-friend";
	
	/**
	 * Operation Code: Adds a friend in communicator. This sends a request
	 * to the recipient. The request will be reviewed first by the parent
	 * of the requester. When approved, the recipient's parent will review 
	 * your request. The recipient will only see your request if both your 
	 * parent and the recipient's parent approved your request.
	 */
	public static final String OPERATION_CODE_ADD_FRIEND = "add-friend";

	/**
	 * Operation Code: Delete a friend in communicator. The MEEP tag of the friend
	 * deleted can be retrieved by passing "affectedUserId" into {@link #getProperty(String)}
	 * The new contact list as the result of the operation can be retreived through
	 * the "contactList" property
	 */
	public static final String OPERATION_CODE_DELETE_FRIEND = "delete-friend";
	
	/**
	 * Operation Code: Retrieves the list of friends and their current status
	 * from server.
	 */
	public static final String OPERATION_CODE_GET_FRIEND_LIST = "get-friend-list";

	/**
	 * Operation Code: Retrieve the blacklist from server
	 */
	public static final String OPERATION_CODE_GET_BLACKLIST = "get-blacklist";
	
	/**
	 * Command: Retrieve recommendations from server. There are several
	 * types of recommendations.
	 */
	public static final String OPERATION_CODE_GET_RECOMMENDATIONS = "get-recommendation";
	
	/**
	 * Operation Code: Sends log records to server. The log records are sent to
	 * server periodically. Log records in the period are aggregated when sent
	 * to server. 
	 */
	public static final String OPERATION_CODE_SYS_LOG = "log";
	
	/**
	 * Operation code of signing in
	 */
	public static final String OPERATION_CODE_SIGN_IN = "sign-in";
	
	/**
	 * Operation code of signing out
	 */
	public static final String OPERATION_CODE_SIGN_OUT = "sign-out";
	
	/**
	 * Operation code of validating purchase item
	 */
	public static final String OPERATION_CODE_CHECK_PURCHASED_ITEM = "validate-purchase";
	
	/**
	 * Operation code of sending online notice to friend
	 */
	public static final String OPERATION_CODE_FRIEND_ONLINE_NOTICE = "presence";
	
	/**
	 * Operation code of sending friend request
	 */
	public static final String OPERATION_CODE_FRIEND_REQUEST = "friend-request";
	
	/**
	 * Operation code of receiving friend request status
	 */
	public static final String OPERATION_CODE_FRIEND_REQUEST_STATUS = "friend-request-status";
	
	/**
	 * Operation code of accepting friend request
	 */
	public static final String OPERATION_CODE_ACCEPT_FRIEND = "accept-friend";
	
	/**
	 * Operation code of reject friend request
	 */
	public static final String OPERATION_CODE_REJECT_FRIEND = "reject-friend";

	/**
	 * Operation code of running command
	 */
	public static final String OPERATION_CODE_RUN_COMMAND = "run-command";
	
	/**
	 * Operation Code: Retrieve a list of permission settings from server
	 */
	public static final String OPERATION_CODE_GET_PERMISSION = "get-permission";
	
	/**
	 * Operation Code: Updates permission settings on server.
	 */
	public static final String OPERATION_CODE_SET_PERMISSION = "set-permission";
	
	/**
	 * Operation code of setting nickname of user
	 */
	public static final String OPERATION_CODE_SET_USER_NICKNAME = "set-nickname";
	
	/**
	 * Operation code of checking url
	 */
	public static final String OPERATION_CODE_CHECK_URL = "check-url";
	
	/**
	 * Operation Code: An instruction to download an audio track from a remote
	 * resource. The URL to the resource is included in "url" property
	 */
	public static final String OPERATION_CODE_DOWNLOAD_MUSIC = "download-music";
	
	/**
	 * Operation Code: An instruction to download a video from a remote
	 * resource. The URL to the resource is included in "url" property
	 */
	public static final String OPERATION_CODE_DOWNLOAD_MOVIE = "download-movie";
	
	/**
	 * Operation Code: An instruction to download an e-book from a remote
	 * resource. The URL to the resource is included in "url" property
	 */
	public static final String OPERATION_CODE_DOWNLOAD_EBOOK = "download-ebook";

	/**
	 * Operation Code: An instruction to download an application from a remote
	 * resource. The URL to the resource is included in "url" property
	 */
	public static final String OPERATION_CODE_DOWNLOAD_APP = "download-app";
	
	/**
	 * Operation Code: An instruction to download a game from a remote
	 * resource. The URL to the resource is included in "url" property
	 */
	public static final String OPERATION_CODE_DOWNLOAD_GAME = "download-game";

	
	/**
	 * OCommand: Reboots the device and wipes the user data partition. This
	 * will restore the system to its factory state
	 */
	public static final String COMMAND_REMOTE_WIPE = "remote-wipe";
	
	/**
	 * Command: Reboots the system. This is only for use by the MEEP system
	 */
	public static final String COMMAND_REBOOT = "reboot-device";
	
	/**
	 * Command: Launch application installer
	 */
	public static final String COMMAND_INSTALL_PACKAGE = "install-app";
	
	/**
	 * Command: Launch the package and make it's default {@link Activity} the
	 * top level Activity
	 */
	public static final String COMMAND_LAUNCH_PACKAGE = "launch-app";
	
	/**
	 * Command: Upgrades the system
	 */
	public static final String COMMAND_UPGRADE_SYSTEM = "ota-upgrade";
	
	/**
	 * Command: Post a notification to the system, alerting user of the 
	 * happening of an event
	 */
	public static final String COMMAND_BROADCAST_MESSAGE = "message-broadcast";
	
	/**
	 * Command: Retrieve category types for the known discrete components on the device
	 */
	public static final String COMMAND_GET_APPS_CATEGORY = "get-apps-category";
	
	/**
	 * Command: Retrieve category types for the known discrete components on the device
	 */
	public static final String COMMAND_GET_FRIEND_LIST = "get-friend-list";
	
	
	/**
	 * Command: Report the current running tasks on the device to server
	 */
	public static final String COMMAND_REPORT_RUNNING = "report-running";
	
	/**
	 * Command: Reports device version information to server
	 */
	public static final String COMMAND_REPORT_VERSION = "report-version";
    
	/**
	 * Command: Reports device meta data to server
	 */
	public static final String COMMAND_REPORT_SYSTEM = "report-system";
	
	/**
	 * Operation Code: An instruction to download a remote resource from the URL
	 * as specified in the "url" property
	 */
	public static final String COMMAND_REMOTE_DOWNLOAD = "remote-download";
	
	/**
	 * Operation Code: Executes the given command on device as specified in the
	 * "command" property
	 */
	public static final String COMMAND_REMOTE_CONSOLE = "remote-console";
	
	@SerializedName("opcode")
	@Expose
	private String operationCode = null;
	
	@SerializedName("proc")
	@Expose
	private String process = null;
	
	@SerializedName("messageid")
	@Expose
	private UUID messageID = null;
	
	@SerializedName("code")
	@Expose
	private int status = -1;
	
	@SerializedName("status")
	@Expose
	private String message = null;
	
	@SerializedName("received")
	@Expose
	private boolean received = false;
	
	@SerializedName("requestid")
	@Expose
	@Deprecated
	private long requestId;
	
	// Internally used
	private Map<String, Object> mProperties;
	
	private static long globalIdSequence = 0;
	
	synchronized static private long getIdentifier(){
		return ++globalIdSequence;
	}

	/**
	 * Constructor
	 * 
	 * @param process
	 *          The process code of the message. Should be one of
	 *          {@link PROCESS_ACCOUNT}, {@link PROCESS_INSTANT_MESSAGING},
	 *          {@link PROCESS_PARENTAL}, {@link PROCESS_SYSTEM}, or
	 *          {@link PROCESS_STORE}
	 * @param operation
	 *          The operation code of the message. Should be one of
	 */
	public Message(String process, String operation) {
		messageID = UUID.randomUUID();
		requestId = getIdentifier();
		this.process = process;
		operationCode = operation;
		mProperties = new HashMap<String, Object>();
	}
	
	/**
	 * Gets the protocol process of this message
	 * @return The protocol process code. Should be one of
	 *          {@link PROCESS_ACCOUNT}, {@link PROCESS_INSTANT_MESSAGING},
	 *          {@link PROCESS_PARENTAL}, {@link PROCESS_SYSTEM}, or
	 *          {@link PROCESS_STORE}
	 */
	public String getProcess() {
		return process;
	}
	
	/**
	 * Gets the operation specified for this protocol message
	 * @return The operation for this protocol message
	 */
	public String getOperation() {
		return operationCode;
	}
	
	/**
	 * Gets the status code of the message
	 * @return One of the {@link Status} code
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Gets the global unique identifier of the message
	 * @return The UUID of the message
	 */
	public UUID getMessageID() {
		return messageID;
	}
	
	/**
	 * Determines whether receipt of the message was acknowledged 
	 * @return true if receipt of the message was acknowledged, false otherwise
	 */
	public boolean isReceived() {
		return received;
	}
	
	/**
	 * Specifies whether or not the receipt of the message was acknowledged
	 * @param value true if receipt of the message is acknowledged, false otherwise
	 */
	public void setReceived(boolean value) {
		received = value;
	}
	
	/**
	 * Returns the detailed message of this protocol instruction
	 * @return the detailed message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Add an object to the properties HashMap
	 * @param name The name of the property
	 * @param value The value of the property
	 */
	public synchronized void addProperty(String name, Object value) {
		if (name != null) {
			if (value == null) {
				removeProperty(name);
			} else {
				if (mProperties == null) {
					mProperties = new HashMap<String, Object>();
				}
				mProperties.put(name, value);
			}
		}
	}
	
	/**
	 * Get the property from properties HashMap with name
	 * @param name The name of the property
	 * @return The value of the property, or <code>null</code> if no mapping for the specified key is found
	 */
	public Object getProperty(String name) {
		Object result = null;
		if (name != null && mProperties != null) {
			result = mProperties.get(name);
		}
		return result;
	}
	
	/**
	 * Returns an unmodifiable view of the properties of the Message
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ?> getProperties() {
		return (Map<String, ?>) (mProperties == null ? Collections.emptyMap() : Collections.unmodifiableMap(mProperties));
	}
	
	/**
	 * Remove the property from properties HashMap with name
	 * @param name The name of the property
	 */
	public synchronized void removeProperty(String name) {
		if (name != null && mProperties != null) {
			mProperties.remove(name);
		}
	}
	
	/**
	 * Returns a string containing a concise, human readable description of this object
	 */
	public String toString() {
		return toJson();
	}
	
	/**
	 * Serializes the message into its JSON equivalent JSON representation.
	 * @return JSON String representation of the object
	 */
	public String toJson() {
		// Create and configures a {@link TypeAdapterFactory} for data models
		// that may exist in the properties the message
		ModelTypeAdapterFactory modelTypeAdapterFactory = new ModelTypeAdapterFactory(
				new ConstructorConstructor(),
				FieldNamingPolicy.IDENTITY, 
				Excluder.DEFAULT.excludeFieldsWithoutExposeAnnotation());
		ModelSerializationStrategy serializationStrategy = ModelSerializationPolicy.DEFAULT.disableIdFieldOnlySerialization();
		modelTypeAdapterFactory.registerSerializationAdapter(Model.class, serializationStrategy);
		ForeignCollectionTypeAdapterFactory fcTypeAdapterFactory = new ForeignCollectionTypeAdapterFactory();
		
		// Creates and configures a {@link TypeAdapterFactory} to parse a {@link
		// Message}
		MessageTypeAdapterFactory messageTypeAdapterFactory = new MessageTypeAdapterFactory(
				new ConstructorConstructor(),
				FieldNamingPolicy.IDENTITY, 
				Excluder.DEFAULT.excludeFieldsWithoutExposeAnnotation());
		PropertyTypeAdapterFactory factory = MessagePropertyTypeAdapterFactories.getInstance().getTypeAdapterFactory(this);
		messageTypeAdapterFactory.usePropertyTypeAdapterFactory(factory);
		
		// Creates the JSON builder
		GsonBuilder gsonb = new GsonBuilder()
				.registerTypeAdapterFactory(messageTypeAdapterFactory)
				.registerTypeAdapterFactory(modelTypeAdapterFactory)
				.registerTypeAdapterFactory(fcTypeAdapterFactory)
				.registerTypeAdapter(Date.class, new DateTypeAdapter());
		Gson gson = gsonb.serializeNulls().create();
		
		return gson.toJson(this, getClass());
	}
	
	/**
	 * Deserializes the JSON element into a Message
	 * 
	 * @param json The JSON element to deserialize
	 * @throws JsonParseException if json is not a valid representation for message
	 * @throws JsonSyntaxException if json is not a valid representation for an object of type
	 * @throws IOException 
	 */
	public static Message fromJson(JsonElement json) throws JsonSyntaxException, JsonParseException, IOException {
		if (json == null || !json.isJsonObject()) {
			throw new JsonParseException("The JSON was not a '" + Message.class.getSimpleName() + "' object");
		}
		return fromJson(json.getAsJsonObject());
	}
	
	/**
	 * Deserializes the JSON element into a Message
	 * 
	 * @param json The JSON element to deserialize
	 * @throws JsonSyntaxException if json is not a valid representation for an object of type
	 */
	public static Message fromJson(JsonObject json) throws JsonSyntaxException {
		if (json == null) {
			return null;
		}
		return fromJson(json.toString());
	}
	
	/**
	 * Deserializes the JSON element into a Message
	 * 
	 * @param json The JSON element to deserialize
	 * @throws JsonSyntaxException if json is not a valid representation for an object of type
	 */
	public static Message fromJson(String json) throws JsonSyntaxException {
		return fromJson(json, null);
	}
	
	/**
	 * Deserializes the JSON element into a Message using the given {@link com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory}
	 * 
	 * @param json The JSON element to deserialize
	 * @param factory The {@link com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory} to use 
	 * to parse properties of the message
	 * @throws JsonSyntaxException if json is not a valid representation for an object of type
	 */
	public static Message fromJson(String json, PropertyTypeAdapterFactory factory) {
		Gson gson;
		
		if (json == null) {
			return null;
		}
		
		// Creates and configures a {@link TypeAdapterFactory} to parse a {@link
		// Message}
		MessageTypeAdapterFactory messageTypeAdapterFactory = new MessageTypeAdapterFactory(
				new ConstructorConstructor(),
				FieldNamingPolicy.IDENTITY, 
				Excluder.DEFAULT.excludeFieldsWithoutExposeAnnotation());
		
		// Find the most appropriate property type adapter to parse the properties of the message
		// if not factory was given
		if (factory == null) {
			messageTypeAdapterFactory.omitParsingProperties();
			
			// Creates the JSON builder
			gson = new GsonBuilder()
					.registerTypeAdapterFactory(messageTypeAdapterFactory)
					.serializeNulls()
					.excludeFieldsWithoutExposeAnnotation()
					.create();
			Message message = gson.fromJson(json, Message.class);
			
			// Retrieves the most appropriate {@link PropertyTypeAdapterFactory} for
			// parsing properties in a {@link Message}
			factory = MessagePropertyTypeAdapterFactories.getInstance().getTypeAdapterFactory(message);
			messageTypeAdapterFactory.parseProperties();
		}
		messageTypeAdapterFactory.usePropertyTypeAdapterFactory(factory);
		
		// Create type factories for data models that may exist in the properties
		// the message
		ModelTypeAdapterFactory modelTypeAdapterFactory = new ModelTypeAdapterFactory(
				new ConstructorConstructor(),
				FieldNamingPolicy.IDENTITY, 
				Excluder.DEFAULT.excludeFieldsWithoutExposeAnnotation());
		ForeignCollectionTypeAdapterFactory fcTypeAdapterFactory = new ForeignCollectionTypeAdapterFactory();
		
		// Creates the JSON builder
		gson = new GsonBuilder()
				.registerTypeAdapterFactory(messageTypeAdapterFactory)
				.registerTypeAdapterFactory(modelTypeAdapterFactory)
				.registerTypeAdapterFactory(fcTypeAdapterFactory)
				.registerTypeAdapter(Date.class, new DateTypeAdapter())
				.serializeNulls()
				.excludeFieldsWithoutExposeAnnotation()
				.create();
		
		return gson.fromJson(json, Message.class);
	}
	
	/** Implement the Parcelable interface {@hide} */
	@Override
	public int describeContents() {
		return 0;
	}
	
	/** Implement the Parcelable interface {@hide} */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(toString());
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Message> CREATOR = new Creator<Message>() {
		
			@Override
			public Message createFromParcel(Parcel source) {
				Message result = null;
				try {
					String json = source.readString();
					result = (Message) Message.fromJson(json);
				} catch (Exception ex) {
					// Cannot create a Message object from a Parcel. Ignored.
					Log.e(TAG, "Cannot create Message from a Parcel: " + source.toString());
				}
				
				return result; 
			}

			@Override
			public Message[] newArray(int size) {
				return new Message[size];
			}
		
	};
}
