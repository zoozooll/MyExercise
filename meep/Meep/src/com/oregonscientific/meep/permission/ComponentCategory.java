/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.oregonscientific.meep.database.Model;

/**
 * The ComponentCategory object that represents a componentCategory object in
 * the underlying datastore
 */
@DatabaseTable(tableName = "component_categories")
public class ComponentCategory extends Model<ComponentCategory, Long> {
	
	// For QueryBuilder to be able to find the fields
	public static final String ID_FIELD_NAME = "id";
	public static final String COMPONENT_FIELD_NAME = "component";
	public static final String CATEGORY_FIELD_NAME = "category";
	
	@DatabaseField(
			columnName = ID_FIELD_NAME,
			canBeNull = false,
			generatedId = true,
			allowGeneratedIdInsert = true,
			indexName = "componentCategory_idx")
	@SerializedName(ID_FIELD_NAME)
	@Expose
	private long id;
	
	@DatabaseField(
			columnName = COMPONENT_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_component_categories_component_idx",
			columnDefinition = "BIGINT REFERENCES components(id) ON DELETE NO ACTION ON UPDATE NO ACTION")
	@SerializedName(COMPONENT_FIELD_NAME)
	@Expose
	private Component component;
	
	@DatabaseField(
			columnName = CATEGORY_FIELD_NAME + "_id",
			foreign = true,
			canBeNull = false,
			indexName = "fk_component_categories_category_idx",
			columnDefinition = "BIGINT REFERENCES categories(id) ON DELETE NO ACTION ON UPDATE NO ACTION")
	@SerializedName(CATEGORY_FIELD_NAME)
	@Expose
	private Category category;
	
	public ComponentCategory() {
		// all persisted classes must define a no-arg constructor with at least
		// package visibility
	}
	
	public ComponentCategory(Component component, Category category) {
		this.component = component;
		this.category = category;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Component getComponent() {
		return component;
	}
	
	public void setComponent(Component component) {
		this.component = component;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public int describeContents() {
		return Long.valueOf(id).intValue();
	}
	
}
