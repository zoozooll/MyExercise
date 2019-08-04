package com.oregonscientific.meep.communicator;

/**
 * Copyright (C) 2013 IDT International Ltd.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The User object that represents an account object in the underlying datastore
 */
@DatabaseTable(tableName = "users")
public class User extends Model<User, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String MEEP_TAG_FIELD_NAME = "meeptag";
	public static final String USER_FRIENDS_FIELD_NAME = "userFriends";
	public static final String ICON_ADDRESS_FIELD_NAME = "iconAddress";
	public static final String FIRST_NAME_FIELD_NAME = "firstName";
	public static final String ACCOUNT_ID_FIELD_NAME = "accountId";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "user_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@DatabaseField(
			columnName = MEEP_TAG_FIELD_NAME,
			uniqueIndex = true)
	@SerializedName(MEEP_TAG_FIELD_NAME)
	@Expose
	private String meepTag;
	
	
	@DatabaseField(
			columnName = ICON_ADDRESS_FIELD_NAME,
			canBeNull = true)
	@SerializedName(ICON_ADDRESS_FIELD_NAME)
	@Expose
	private String iconAddress;
	
	
	@DatabaseField(
			columnName = FIRST_NAME_FIELD_NAME,
			canBeNull = true)
	@SerializedName(FIRST_NAME_FIELD_NAME)
	@Expose
	private String firstName;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(USER_FRIENDS_FIELD_NAME)
	private ForeignCollection<UserFriend> userFriends;

	
	@DatabaseField(
			columnName = ACCOUNT_ID_FIELD_NAME,
			canBeNull = true)
	@SerializedName(ACCOUNT_ID_FIELD_NAME)
	@Expose
	private String accountId;
	
	public User() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public User(String accountId) {
		this.accountId = accountId;
	}
	

	public User(String accountId, String iconAddress, String firstName, String meepTag) {
		this.accountId = accountId;
		this.iconAddress = iconAddress;
		this.firstName = firstName;
		this.meepTag = meepTag;
	}
	
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getMeepTag() {
		return meepTag;
	}
	
	public void setMeepTag(String meepTag) {
		this.meepTag = meepTag;
	}
	
	public void setUserFriends(ForeignCollection<UserFriend> userFriends) {
		this.userFriends = userFriends;
	}
	
	public ForeignCollection<UserFriend> getUserFriends() {
		return userFriends;
	}
	
	public String getIconAddress() {
		return iconAddress;
	}
	
	public void setIconAddress(String iconAddress) {
		this.iconAddress = iconAddress;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	
}
