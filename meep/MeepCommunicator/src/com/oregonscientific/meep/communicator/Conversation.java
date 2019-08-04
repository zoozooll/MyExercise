package com.oregonscientific.meep.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The Conversation object that represents a conversation object in the
 * underlying datastore
 */
@DatabaseTable(tableName = "conversations")
public class Conversation extends Model<Conversation, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String FRIEND_FIELD_NAME = "friend";
	
	public static final String CONVERSATION_MESSAGES_FIELD_NAME = "conversationMessages";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "conversation_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(CONVERSATION_MESSAGES_FIELD_NAME)
	@Expose
	private ForeignCollection<ConversationMessage> conversationMessages;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "Id",
			canBeNull = false,
			indexName = "fk_conversations_user_idx")
	@SerializedName(USER_FIELD_NAME)
	@Expose
	private long userId;
	
	@DatabaseField(
			columnName = FRIEND_FIELD_NAME + "Id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_conversations_friend_idx",
			columnDefinition = "BIGINT REFERENCES friends(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(FRIEND_FIELD_NAME)
	@Expose
	private Friend friend;
	
	public Conversation() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Conversation(long userId, Friend friend) {
		this.userId = userId;
		this.friend = friend;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public Friend getFriend() {
		return friend;
	}
	
	public void setFriend(Friend friend) {
		this.friend = friend;
	}
	
	public ForeignCollection<ConversationMessage> getConversationMessages() {
		return conversationMessages;
	}
	
	public void setConversationMessages(
			ForeignCollection<ConversationMessage> conversationMessages) {
		this.conversationMessages = conversationMessages;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
}
