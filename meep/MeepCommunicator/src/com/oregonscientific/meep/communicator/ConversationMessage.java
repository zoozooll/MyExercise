package com.oregonscientific.meep.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The ConversationMessage object that represents a conversationMessage object
 * in the underlying datastore
 */
@DatabaseTable(tableName = "conversationMessages")
public class ConversationMessage extends Model<ConversationMessage, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String CONVERSATION_FIELD_NAME = "conversation";
	public static final String NAME_FIELD_NAME = "name";
	public static final String CONTENT_FIELD_NAME = "content";
	public static final String CONTENT_TYPE_FIELD_NAME = "contentType";
	public static final String IS_INCOMING_MESSAGE_FIELD_NAME = "isIncomingMessage";
	public static final String READ_FIELD_NAME = "read";
	public static final String NOTIFICATION_ID_FIELD_NAME = "notificationId";
	
	public static final long NOTIFICATION_EMPTY_ID = 0;
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "chat_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@DatabaseField(
			columnName = CONVERSATION_FIELD_NAME + "Id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_conversationMessages_conversation_idx",
			columnDefinition = "BIGINT REFERENCES conversations(id) ON DELETE NO ACTION ON UPDATE NO ACTION")
	@SerializedName(CONVERSATION_FIELD_NAME)
	@Expose
	private Conversation conversation;
	
	@DatabaseField(columnName = NAME_FIELD_NAME)
	@SerializedName(NAME_FIELD_NAME)
	@Expose
	private String name;
	
	@DatabaseField(columnName = CONTENT_FIELD_NAME, canBeNull = false)
	@SerializedName(CONTENT_FIELD_NAME)
	@Expose
	private String content;
	
	@DatabaseField(
			columnName = CONTENT_TYPE_FIELD_NAME + "Id",
			foreign = true,
			canBeNull = true,
			indexName = "fk_chats_contentType_idx",
			columnDefinition = "BIGINT REFERENCES contentTypes(id) ON DELETE NO ACTION ON UPDATE NO ACTION")
	@SerializedName(CONTENT_TYPE_FIELD_NAME)
	@Expose
	private ContentType contentType;
	
	@DatabaseField(columnName = IS_INCOMING_MESSAGE_FIELD_NAME)
	@SerializedName(IS_INCOMING_MESSAGE_FIELD_NAME)
	@Expose
	private Boolean isIncomingMessage;
	
	@DatabaseField(columnName = READ_FIELD_NAME)
	@SerializedName(READ_FIELD_NAME)
	@Expose
	private Boolean read;
	
	
	@DatabaseField(columnName = NOTIFICATION_ID_FIELD_NAME)
	@SerializedName(NOTIFICATION_ID_FIELD_NAME)
	@Expose
	private long notificationId;
	

	public ConversationMessage() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public ConversationMessage(
			Conversation conversation,
			String name,
			String content,
			ContentType contentType,
			Boolean isIncomingMessage,
			Boolean read,
			long notificationId) {
		
		this.conversation = conversation;
		this.name = name;
		this.content = content;
		this.contentType = contentType;
		this.isIncomingMessage = isIncomingMessage;
		this.read = read;
		this.notificationId = notificationId;
	}
	
	public ConversationMessage(
			ConversationMessage message) {
		
		this.conversation = message.getConversation();
		this.name = message.getName();
		this.content = message.getContent();
		this.contentType = message.getContentType();
		this.isIncomingMessage = message.getIsIncomingMessage();
		this.read = message.getRead();
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public ContentType getContentType() {
		return contentType;
	}
	
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}
	
	public Boolean getIsIncomingMessage() {
		return isIncomingMessage;
	}
	
	public void setIsIncomingMessage(Boolean isIncomingMessage) {
		this.isIncomingMessage = isIncomingMessage;
	}
	
	public Boolean getRead() {
		return read;
	}
	
	public void setRead(Boolean read) {
		this.read = read;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

}
