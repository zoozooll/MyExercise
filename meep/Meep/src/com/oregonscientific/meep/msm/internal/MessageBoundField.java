/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm.internal;

import java.io.IOException;
import java.lang.reflect.Field;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.serialization.BoundField;
import com.oregonscientific.meep.serialization.TypeAdapterRuntimeTypeWrapper;

public class MessageBoundField extends BoundField {
	
	final protected Gson context;
	final TypeToken<?> fieldType;
	final TypeAdapter<?> typeAdapter;
	
	protected MessageBoundField(
			Gson context,
			Field field,
			String name,
			TypeToken<?> fieldType,
			boolean serialize,
			boolean deserialize) {
		
		super(name, field, serialize, deserialize);
		
		this.context = context;
		this.fieldType = fieldType;
		typeAdapter = context.getAdapter(fieldType);
	}
	
	public static MessageBoundField create(
			Gson context,
			Field field,
			String name,
			TypeToken<?> fieldType,
			boolean serialize,
			boolean deserialize) {
		
		return new MessageBoundField(context, field, name, fieldType, serialize, deserialize);
	}
	
	// the type adapter and field type always agree
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
		Object fieldValue = field.get(value);
		TypeAdapter t = new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
		writer.name(name);
		t.write(writer, fieldValue);
	}
	
	@Override
	public void read(
			JsonReader reader, 
			final Object value, 
			String name) throws IOException, IllegalAccessException {
		
		boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
		
		Object fieldValue = typeAdapter.read(reader);
		if (fieldValue != null || !isPrimitive) {
			field.set(value, fieldValue);
		}
	}
	
}
