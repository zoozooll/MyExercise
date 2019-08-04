/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.serialization.BoundField;

/**
 * The adapter that serializes/deserializes a Message object
 * @param <T>
 */
public class MessageTypeAdapter<T extends Message> extends TypeAdapter<Message> {
	
	@SuppressWarnings("unused")
	private final String TAG = "MessageTypeAdapter";
	
	/** The field name to store internal properties of the object */ 
	private final String PROPERTIES_FIELD_NAME = "mProperties";
	
	protected final ObjectConstructor<T> constructor;
	protected final Map<String, BoundField> boundFields;
	
	MessageTypeAdapter(ObjectConstructor<T> constructor, Map<String, BoundField> boundFields) {
		this.constructor = constructor;
		this.boundFields = boundFields;
	}
	
	protected BoundField getIdField() {
		Collection<BoundField> fields = boundFields.values();
		for (BoundField field : fields) {
			if (field.isIdField())
				return field;
		}
		return null;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public Message read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		
		Message instance = constructor.construct();
		try {
			in.beginObject();
			while (in.hasNext()) {
				String name = null;
				JsonToken jsonToken = in.peek();
				switch (jsonToken) {
				case NAME:
					name = in.nextName();
				}
				
				BoundField field = name == null ? null : boundFields.get(name);
				if (field == null) {
					field = boundFields.get(PROPERTIES_FIELD_NAME);
					if (field == null) {
						in.skipValue();
					} else {
						field.read(in, instance, name);
					}
				} else if (!field.shouldDeserialize) {
					in.skipValue();
				} else {
					field.read(in, instance, name);
				}
			}
			in.endObject();
		} catch (IllegalStateException e) {
			throw new JsonSyntaxException(e);
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}
		
		return instance;
	}
	
	@Override
	public void write(JsonWriter out, Message value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		
		try {
			out.beginObject();
			for (BoundField boundField : boundFields.values()) {
				if (boundField.shouldSerialize) {
					boundField.write(out, value);
				}
			}
			out.endObject();
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
	
}
