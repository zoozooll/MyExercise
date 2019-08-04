package com.oregonscientific.meep.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The UserFriend object that represents a userFriend object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "userFriends")
public class UserFriend extends Model<UserFriend, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String FRIEND_FIELD_NAME = "friend";
	public static final String USER_FIELD_NAME = "user";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "userFriend_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@DatabaseField(
			columnName = FRIEND_FIELD_NAME + "Id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_userFriends_friend_idx",
			columnDefinition = "BIGINT REFERENCES friends(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(FRIEND_FIELD_NAME)
	@Expose
	private Friend friend;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "Id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_userfriends_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE NO ACTION ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Expose
	private User user;
	

	
	public UserFriend() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public UserFriend(Friend friend, User user) {
		this.friend = friend;
		this.user = user;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Friend getFriend() {
		return friend;
	}
	
	public void setFriend(Friend friend) {
		this.friend = friend;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
}
