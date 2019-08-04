/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission.compat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;

public class SetPermissionMessagePropertyTypeAdapterFactory implements PropertyTypeAdapterFactory {
	
	private final String TAG = getClass().getSimpleName();
	
	private final String PERMISSION_FIELD_NAME = "permission"; 
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, Object key) {
		String keyString = key == null ? "" : key.toString().replaceAll("^\"|\"$", "");
		if (!PERMISSION_FIELD_NAME.equals(keyString)) {
			return null; // We only handle serialization and deserialization of Permission
		}
		
		return (TypeAdapter<T>) new Adapter(gson);
	}
	
	public final class Adapter extends TypeAdapter<Object> {
		
		final Gson context;
		
		Adapter(Gson ctx) {
			context = ctx;
		}
		
		@Override
		public Map<String, Permission> read(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			if (peek == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			
			Map<String, Permission> map = new HashMap<String, Permission>();
			try {
				in.beginObject();
				
				while (in.hasNext()) {
					String key = in.nextName();
					Permission value = readPermission(in);
					Permission replaced = map.put(key, value);
					if (replaced != null) {
						throw new JsonSyntaxException("duplicate key: " + key);
					}
				}
				
				in.endObject();
			} catch (IllegalStateException e) {
				throw new JsonSyntaxException(e);
			}
			return map;
		}
		
		private Permission readPermission(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			if (peek == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			
			Permission permission = new Permission();
			try {
				in.beginObject();
				while (in.hasNext()) {
					String name = in.nextName();
					
					// Reads access
					if (Permission.ACCESS_FIELD_NAME.equals(name)) {
						Permission.AccessLevels accessLevel = Permission.AccessLevels.fromString(in.nextString());
						permission.setAccessLevel(accessLevel);
					
					// Reads time limit
					} else if (Permission.TIME_LIMIT_SERIALIZED_NAME.equals(name)) {
						// Converts the received time to milliseconds
						long timeLimit = in.nextLong() * 60000;
						permission.setTimeLimit(timeLimit);
					
					// Reads other fields
					} else {
						ModelAttributes attrs = Schema.getAttributes(Permission.class);
						Map<String, Field> fields = attrs.getColumns();
						Field field = fields.get(name);
						if (field != null) {
							TypeAdapter<?> typeAdapter = context.getAdapter(field.getType());
							if (typeAdapter != null) {
								try {
									field.setAccessible(true);
									field.set(permission, typeAdapter.read(in));
								} catch (Exception ex) {
									Log.e(TAG, "Cannot deserialize " + name + " because " + ex);
								}
							}
						}
					}
				}
				in.endObject();
			} catch (IllegalStateException e) {
				throw new JsonSyntaxException(e);
			}
			return permission;
		}
		
		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			
			// When writing, we start with a Map
			if (!Map.class.isAssignableFrom(value.getClass())) {
				throw new IllegalStateException(value + " is not a Map object");
			}
			Map<?, ?> permissions = (Map<?, ?>) value;
			
			out.beginObject();
			for (Map.Entry<?, ?> entry : permissions.entrySet()) {
				out.name(String.valueOf(entry.getKey()));
				
				// We cannot proceed if value in the Map is not a Permission object
				if (!Permission.class.isAssignableFrom(entry.getValue().getClass())) {
					throw new IllegalStateException(entry.getValue() + " is not a Permission object");
				}
				
				out.beginObject();
				
				Permission permission = (Permission) entry.getValue();
				// Writes time limit in seconds
				out.name(Permission.TIME_LIMIT_SERIALIZED_NAME);
				out.value(permission.getTimeLimit() / 60000);
				
				// Writes access level value
				out.name(Permission.ACCESS_FIELD_NAME);
				out.value(permission.getAccessLevel().toString());
				
				out.endObject();
			}
			out.endObject();
		}
	
	}

}
