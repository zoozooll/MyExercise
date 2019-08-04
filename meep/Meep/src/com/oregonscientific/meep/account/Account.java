/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import org.jasypt.util.password.StrongPasswordEncryptor;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;
import com.oregonscientific.meep.database.PasswordField;

/**
 * The Account object that represents a MEEP account
 */
@DatabaseTable(tableName = "accounts")
public class Account extends Model<Account, String> {
	
	private static final String TAG = "Account";
	
	// System default
	static final String DEFAULT_USER_ID = "meep00000000000000000000";
	static final String DEFAULT_MEEP_TAG = "";
	static final String DEFAULT_FIRST_NAME = "MEEPer";
	static final String DEFAULT_NICKNAME = "MEEPer";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String TOKEN_FIELD_NAME = "token";
	public static final String PASSWORD_FIELD_NAME = "password";
	public static final String FIRST_NAME_FIELD_NAME = "first_name";
	public static final String LAST_NAME_FIELD_NAME = "last_name";
	public static final String NICKNAME_FIELD_NAME = "nickname";
	public static final String MEEP_TAG_FIELD_NAME = "meeptag";
	public static final String ICON_ADDRESS_FIELD_NAME = "icon_address";
	public static final String IS_LOGGED_IN_FIELD_NAME = "is_logged_in";
	public static final String IS_LAST_LOGGED_IN_FIELD_NAME = "is_last_logged_in";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME, 
			canBeNull = false, 
			id = true, 
			indexName = "account_idx")
	@SerializedName("user_" + ID_FIELD_NAME)
	@Expose
	@Omit
	private String id;
	
	@DatabaseField(columnName = TOKEN_FIELD_NAME, canBeNull = true)
	@Expose
	@Omit
	private String token;
	
	@DatabaseField(columnName = PASSWORD_FIELD_NAME)
	@PasswordField(encryptorClass = StrongPasswordEncryptor.class)
	@Expose(serialize = false)
	@Omit
	private String password;
	
	@DatabaseField(columnName = MEEP_TAG_FIELD_NAME, uniqueIndex = true)
	@SerializedName(MEEP_TAG_FIELD_NAME)
	@Expose
	private String meepTag;
	
	@DatabaseField(columnName = FIRST_NAME_FIELD_NAME)
	@SerializedName(FIRST_NAME_FIELD_NAME)
	@Expose
	private String firstName;
	
	@DatabaseField(columnName = LAST_NAME_FIELD_NAME)
	@SerializedName(LAST_NAME_FIELD_NAME)
	@Expose
	private String lastName;
	
	@DatabaseField(columnName = NICKNAME_FIELD_NAME)
	@Expose
	private String nickname;
	
	@DatabaseField(columnName = ICON_ADDRESS_FIELD_NAME)
	@SerializedName("iconAddr")
	@Expose
	private String iconAddress;
	
	@DatabaseField(columnName = IS_LOGGED_IN_FIELD_NAME, canBeNull = false)
	@SerializedName(IS_LOGGED_IN_FIELD_NAME)
	@Omit
	private boolean isLoggedIn;
	
	@DatabaseField(columnName = IS_LAST_LOGGED_IN_FIELD_NAME, canBeNull = false)
	@SerializedName(IS_LAST_LOGGED_IN_FIELD_NAME)
	@Omit
	private boolean isLastLoggedIn;
	
	public Account() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	Account(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMeepTag() {
		return meepTag;
	}
	
	public void setMeepTag(String value) {
		meepTag = value;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getIconAddress() {
		return iconAddress;
	}
	
	public void setIconAddress(String iconAddress) {
		this.iconAddress = iconAddress;
	}
	
	public boolean getIsLoggedIn() {
		return isLoggedIn;
	}
	
	public void setIsLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	
	public boolean getIsLastSignIn() {
		return isLastLoggedIn;
	}
	
	public void setIsLastSignIn(boolean isLastSignIn) {
		this.isLastLoggedIn = isLastSignIn;
	}
	
	/**
	 * Retrieves an Identity of this Account
	 * @return The Identity identifying the Account
	 */
	public Identity getIdentity() {
		return new Identity(meepTag, password);
	}
	
	@Override
	public String toString() {
		return meepTag == null ? firstName == null ? "" : firstName : meepTag;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Account> CREATOR = new Creator<Account>() {
		
		@Override
		public Account createFromParcel(Parcel source) {
			Account result = null;
			try {
				String className = source.readString();
				result = (Account) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a User object from a Parcel. Ignored.
				Log.e(TAG, "Cannot create $Account from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public Account[] newArray(int size) {
			return new Account[size];
		}
	};

}
