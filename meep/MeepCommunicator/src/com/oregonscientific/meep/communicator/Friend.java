package com.oregonscientific.meep.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The Friend object that represents a friend object in the underlying datastore
 */
@DatabaseTable(tableName = "friends")
public class Friend extends Model<Friend, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String NAME_FIELD_NAME = "name";
	public static final String ICON_ADDRESS_FIELD_NAME = "iconAddress";
	public static final String ONLINE_FIELD_NAME = "online";
	public static final String ACCOUNT_ID_FIELD_NAME = "accountId";
	public static final String CONVERSATIONS_FIELD_NAME = "conversations";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "friend_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(CONVERSATIONS_FIELD_NAME)
	@Expose
	private ForeignCollection<Conversation> conversations;
	
	@DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
	@SerializedName(NAME_FIELD_NAME)
	@Expose
	private String name;
	
	@DatabaseField(columnName = ICON_ADDRESS_FIELD_NAME)
	@SerializedName(ICON_ADDRESS_FIELD_NAME)
	@Expose
	private String iconAddress;
	
	@DatabaseField(columnName = ONLINE_FIELD_NAME)
	@SerializedName(ONLINE_FIELD_NAME)
	@Expose
	private Boolean online;
	
	@DatabaseField(columnName = ACCOUNT_ID_FIELD_NAME)
	@SerializedName(ACCOUNT_ID_FIELD_NAME)
	@Expose
	private String accountId;
	
	public Friend() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Friend(String name, String iconAddress, Boolean online) {

		this.name = name;
		this.iconAddress = iconAddress;
		this.online = online;
	}

	public Friend(String name, String iconAddress, String accountId, Boolean online) {
		
		this.name = name;
		this.iconAddress = iconAddress;
		this.online = online;
		this.accountId = accountId;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIconAddress() {
		return iconAddress;
	}
	
	public void setIconAddress(String iconAddress) {
		this.iconAddress = iconAddress;
	}
	
	public Boolean getOnline() {
		return online;
	}
	
	public void setOnline(Boolean online) {
		this.online = online;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getAccountId() {
		return accountId;
	}
	
	public ForeignCollection<Conversation> getConversations() {
		return conversations;
	}
	
	public void setConversations(ForeignCollection<Conversation> conversations) {
		this.conversations = conversations;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
}
