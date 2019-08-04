/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Parcel;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * An object used internally in the package to uniquely identify an
 * user account
 */
@DatabaseTable(tableName = "users")
class $User extends Model<$User, Long> implements Identity<Long> {
	
	private static final String TAG = "$User";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String IDENTIFIER_FIELD_NAME = "identifier";
	
	public static final String RECOMMENDATIONS_FIELD_NAME = "recommendations";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "user_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = IDENTIFIER_FIELD_NAME,
			uniqueIndex = true)
	@SerializedName(IDENTIFIER_FIELD_NAME)
	@Expose
	private String identifier;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(RECOMMENDATIONS_FIELD_NAME)
	@Omit
	private ForeignCollection<Recommendation> recommendations;
	
	$User() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	/**
	 * Creates a new user with the given unique {@code identifier}
	 * 
	 * @param identifier the unique identifier of the user
	 */
	$User(String identifier) {
		this.identifier = identifier;
	}
	
	public long getId() {
		return id;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public ForeignCollection<Recommendation> getRecommendations() {
		return recommendations;
	}
	
	/**
	 * Retrieve recommendations of the given {@code type}
	 * 
	 * @param type the type of recommendations to return
	 * @return a list of recommendations matching the given {@code type}
	 */
	public List<Recommendation> getRecommendations(String type) {
		// Quick return if the request cannot be processed
		if (type == null || recommendations == null) {
			return Collections.emptyList();
		}
		
		List<Recommendation> result = new ArrayList<Recommendation>();
		CloseableIterator<Recommendation> iterator = recommendations.closeableIterator();
		try {
			while (iterator.hasNext()) {
				Recommendation recommendation = iterator.next();
				if (type.equals(recommendation.getType())) {
					result.add(recommendation);
				}
			}
		} finally {
			try {
				iterator.close();
			} catch (SQLException ex) {
				// Ignore
			}
		}
		return result;
	}
	
	/**
	 * Removes the {@code recommendation} from the recommendation list.
	 * 
	 * @param recommendation the recommendation to remove from the user's list of 
	 * recommendations
	 * @return true if the recommendation is removed, false if the recommendation
	 * is not found
	 */
	public boolean removeRecommendation(Recommendation recommendation) {
		// Quick return if the request cannot be processed
		if (recommendation == null || recommendations == null) {
			return false;
		}
		
		boolean result = false;
		CloseableIterator<Recommendation> iterator = recommendations.closeableIterator();
		try {
			while (iterator.hasNext()) {
				Recommendation r = iterator.next();
				if (r.equals(recommendation)) {
					iterator.remove();
					result = true;
					break;
				}
			}
		} finally {
			try {
				iterator.close();
			} catch (SQLException ex) {
				// Ignore
			}
		}
		return result;
	}
	
	/**
	 * Determines whether or not the user has the given {@code recommendation} 
	 * 
	 * @param recommendation the recommendation to check against
	 * @return true if the user has the recommendation, false otherwise
	 */
	public boolean hasRecommendation(Recommendation recommendation) {
		// Quick return if the request cannot be processed
		if (recommendation == null || recommendations == null) {
			return false;
		}
		
		// Since all foreign collections are lazy, no need to refresh
		boolean result = false;
		CloseableIterator<Recommendation> iterator = recommendations.closeableIterator();
		try {
			while (iterator.hasNext()) {
				Recommendation r = iterator.next();
				if (r.equals(recommendation)) {
					result = true;
					break;
				}
			}
		} finally {
			try {
				iterator.close();
			} catch (SQLException ex) {
				// Ignore
			}
		}
		return result;
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
	public String toString() {
		return getIdentifier();
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<$User> CREATOR = new Creator<$User>() {
		
		@Override
		public $User createFromParcel(Parcel source) {
			$User result = null;
			try {
				String className = source.readString();
				result = ($User) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a User object from a Parcel. Ignored.
				Log.e(TAG, "Cannot create User from a Parcel: " + source.toString());
			}
			return result;
		}
		
		@Override
		public $User[] newArray(int size) {
			return new $User[size];
		}
	};

}
