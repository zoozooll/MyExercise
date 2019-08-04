/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import java.sql.SQLException;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.dao.CloseableWrappedIterable;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Identity;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.Omit;

/**
 * The Category object that represents a category object in the underlying
 * datastore
 */
@DatabaseTable(tableName = "categories")
public class Category extends Model<Category, Long> implements Identity<Long> {
	
	private final String TAG = getClass().getSimpleName();
	
	// Default categories
	public static final String CATEGORY_GAMES = "games";
	
	/**
	 * Application packages in this category are not permitted to run on MEEP
	 */
	public static final String CATEGORY_BLACKLIST = "blacklist";
	
	/**
	 * Application packages in this category will be ignored in "Recently Used"
	 */
	public static final String CATEGORY_IGNORED = "hidelist";
	
	/**
	 * Application packages in this category should be hidden from APP launcher
	 */
	public static final String CATEGORY_HIDDEN = "apphidelist";
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String NAME_FIELD_NAME = "name";
	public static final String COMPONENT_CATEGORIES_FIELD_NAME = "component_categories";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "category_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	@Omit
	private long id;
	
	@ForeignCollectionField(eager = false)
	@SerializedName(COMPONENT_CATEGORIES_FIELD_NAME)
	@Omit
	private ForeignCollection<ComponentCategory> componentCategories;
	
	@DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
	@SerializedName(NAME_FIELD_NAME)
	@Expose
	private String name;
	
	public Category() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public Category(String name) {
		this.name = name;
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
	
	public ForeignCollection<ComponentCategory> getComponentCategories() {
		return componentCategories;
	}
	
	/**
	 * Returns whether or not the {@link Category} contains the given {@code component}
	 * 
	 * @param component the component to check
	 * @return true if this category contains the {@code component}
	 */
	public boolean hasComponent(Component component) {
		boolean result = false;
		if (component != null && componentCategories != null) {
			// Since all foreign collections are lazy, no need to refresh
			CloseableWrappedIterable<ComponentCategory> iterator = componentCategories.getWrappedIterable();
			try {
				for (ComponentCategory c : iterator) {
					Component comp = c.getComponent();
					if (comp == null) {
						continue;
					}
					
					comp.refresh();
					if (component.equals(comp)) {
						result = true;
						break;
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "Cannot determine " + this + " has " + component + " because " + ex);
			} finally {
				try {
					iterator.close();
				} catch (SQLException e) {
					// Ignore
				}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
	@Override
	public Long getIdentity() {
		return Long.valueOf(getId());
	}

	@Override
	public String getIdentityAttribute() {
		return ID_FIELD_NAME;
	}
	
}
