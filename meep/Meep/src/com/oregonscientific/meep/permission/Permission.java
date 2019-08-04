/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;
import com.oregonscientific.meep.database.internal.ModelSerializationPolicy;

/**
 * The Permission object that represents a permission object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "permissions")
public class Permission extends Model<Permission, Long> implements Identity<Long> {
	
	private static final String LOG_TAG = "Permission";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String USER_FIELD_NAME = "user";
	public static final String COMPONENT_FIELD_NAME = "component";
	public static final String ACCESS_FIELD_NAME = "access";
	public static final String TIME_LIMIT_FIELD_NAME = "time_limit";
	
	// For use in serialization and deserialization
	public static final String TIME_LIMIT_SERIALIZED_NAME = "timelimit";
	
	/**
	 * An enumeration of access rights
	 */
	public static enum AccessLevels {

		ALLOW("allow"), 
		LOW("low"),
		MEDIUM("medium"), 
		APPROVAL("approval"),
		DENY("deny"),
		HIGH("high");

		private String name = null;

		AccessLevels(String accessLevel) {
			this.name = accessLevel;
		}

		public String toString() {
			return name;
		}
		
		public static AccessLevels fromString(String text) {
			if (text != null) {
				for (AccessLevels b : AccessLevels.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return DENY;
		}
	}
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "permission_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = USER_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_permissions_user_idx",
			columnDefinition = "BIGINT REFERENCES users(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(USER_FIELD_NAME)
	@Omit
	private $User user;
	
	@DatabaseField(
			columnName = COMPONENT_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_permissions_component_idx",
			columnDefinition = "BIGINT REFERENCES components(id) ON DELETE CASCADE ON UPDATE NO ACTION")
	@SerializedName(COMPONENT_FIELD_NAME)
	@Expose
	private Component component;
	
	@DatabaseField(columnName = ACCESS_FIELD_NAME, canBeNull = false)
	@SerializedName(ACCESS_FIELD_NAME)
	@Expose
	private String accessLevel;
	
	@DatabaseField(columnName = TIME_LIMIT_FIELD_NAME, canBeNull = false)
	@SerializedName(TIME_LIMIT_SERIALIZED_NAME)
	@Expose
	private Long timeLimit;
	
	public Permission() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Permission(String accessLevel, Long timeLimit) {
		this(AccessLevels.fromString(accessLevel), timeLimit);
	}
	
	public Permission(AccessLevels accessLevel, Long timeLimit) {
		this.accessLevel = accessLevel == null ? AccessLevels.DENY.toString() : accessLevel.toString();
		this.timeLimit = timeLimit;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public $User getUser() {
		return user;
	}
	
	public void setUser($User user) {
		this.user = user;
	}
	
	public Component getComponent() {
		return component;
	}
	
	public void setComponent(Component component) {
		this.component = component;
	}
	
	public AccessLevels getAccessLevel() {
		return AccessLevels.fromString(accessLevel);
	}
	
	public void setAccessLevel(AccessLevels value) {
		accessLevel = value == null ? AccessLevels.DENY.toString() : value.toString();
	}
	
	public Boolean getCanAccess() {
		return (!AccessLevels.DENY.toString().equals(accessLevel));
	}
	
	public Long getTimeLimit() {
		return timeLimit;
	}
	
	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	@Override
	public String toJson() {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put(COMPONENT_FIELD_NAME, null);
		ModelSerializationPolicy policy = ModelSerializationPolicy.DEFAULT
				.disableIdFieldOnlySerialization()
				.withExpansionTree(fields);
		return toJson(policy);
	}
	
	@Override
	public Long getIdentity() {
		return Long.valueOf(getId());
	}

	@Override
	public String getIdentityAttribute() {
		return ID_FIELD_NAME;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Permission> CREATOR = new Creator<Permission>() {
		
		@Override
		public Permission createFromParcel(Parcel source) {
			Permission result = null;
			try {
				String className = source.readString();
				result = (Permission) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Permission object from a Parcel. Ignored.
				Log.e(LOG_TAG, "Cannot create Permission from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public Permission[] newArray(int size) {
			return new Permission[size];
		}
	};
	
}
