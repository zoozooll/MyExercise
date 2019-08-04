/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.database.internal;

import java.io.IOException;
import java.lang.reflect.Field;

import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.database.PasswordField;
import com.oregonscientific.meep.serialization.BoundField;

/**
 * The internal bound field implementation for Model objects
 */
public abstract class $Bound$Field extends BoundField {
	
	protected $Bound$Field(
			String name, 
			Field field, 
			boolean shouldSerialize,
			boolean shouldDeserialize) {
		super(name, field, shouldSerialize, shouldDeserialize);
	}

	protected boolean isPasswordField() {
		PasswordField passwordField = field.getAnnotation(PasswordField.class);
		return passwordField != null;
	}

	@Override
	public void write(
			JsonWriter writer, 
			Object value) throws IOException, IllegalAccessException {
		
		throw new RuntimeException("Not implemented");
	}
	
	public abstract void write(JsonWriter writer, Object value, ModelSerializationStrategy serializationStrategy) throws IOException, IllegalAccessException;

}
