/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The locale supported in the system
 */
@DatabaseTable(tableName = "locales")
public class Locale extends Model<Locale, Long> implements Identity<Long> {
	
	/** A list of locales */
	public static final String LOCALE_DEFAULT = "custom";
	public static final String LOCALE_ENGLISH = "en";
	
	/** A default list of locales supported */
	static final List<SimpleEntry<String, Boolean>> LOCALES = 
		new ArrayList<SimpleEntry<String, Boolean>>() {
		
		private static final long serialVersionUID = 7592365066188347522L;

		{
			add(new SimpleEntry<String, Boolean>(LOCALE_ENGLISH, Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>("de", Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>("es", Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>("fr", Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>("it", Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>("ja", Boolean.FALSE));
			add(new SimpleEntry<String, Boolean>("ko", Boolean.FALSE));
			add(new SimpleEntry<String, Boolean>("pt", Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>("ru", Boolean.TRUE));
			add(new SimpleEntry<String, Boolean>(LOCALE_DEFAULT, Boolean.FALSE));
		}
		
	};
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String LOCALE_FIELD_NAME = "locale";
	public static final String EXACT_MATCH_FIELD_NAME = "exact_match";
	
	public static final String BLACKLISTS_FIELD_NAME = "blacklists"; 
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "locale_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@DatabaseField(
			columnName = LOCALE_FIELD_NAME, 
			canBeNull = false)
	@SerializedName(LOCALE_FIELD_NAME)
	@Expose
	private String locale;
	
	@DatabaseField(columnName = EXACT_MATCH_FIELD_NAME)
	@SerializedName(EXACT_MATCH_FIELD_NAME)
	@Expose
	@Omit
	private boolean exactMatch;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(BLACKLISTS_FIELD_NAME)
	@Omit
	private ForeignCollection<Blacklist> blacklists;
	
	public Locale() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Locale(String locale) {
		this(locale, false);
	}
	
	public Locale(String locale, Boolean exactMatch) {
		this(locale, exactMatch.booleanValue());
	}
	
	public Locale(String locale, boolean exactMatch) {
		this.locale = locale;
		this.exactMatch = exactMatch;
	}
	
	public long getId() {
		return id;
	}
	
	public java.util.Locale getLocale() {
		return locale == null ? null : new java.util.Locale(locale);
	}
	
	public void setLocale(java.util.Locale value) {
		locale = value == null ? null : value.toString();
	}
	
	public boolean getExactMatch() {
		return exactMatch;
	}
	
	public ForeignCollection<Blacklist> getBlacklists() {
		return blacklists;
	}
	
	/**
	 * Sets whether or not the "exact match" should be performed on the given locale
	 * in bad word filtering 
	 * 
	 * @param value {@code true} to perform exact match, {@code false} to perform partial match
	 */
	public void setExactMatch(boolean value) {
		exactMatch = value;
	}
	
	@Override
	public int hashCode() {
		return locale == null ? 0 : locale.hashCode();
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
		return locale;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(getId()).intValue();
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Locale> CREATOR = new Creator<Locale>() {
		
		@Override
		public Locale createFromParcel(Parcel source) {
			Locale result = null;
			try {
				String className = source.readString();
				result = (Locale) Model.fromJson(source.readString(), Class.forName(className));
			} catch (Exception ex) {
				// Cannot create a Model object from a Parcel. Ignored.
			}
			return result;
		}
		
		@Override
		public Locale[] newArray(int size) {
			return new Locale[size];
		}
	};
	
}
