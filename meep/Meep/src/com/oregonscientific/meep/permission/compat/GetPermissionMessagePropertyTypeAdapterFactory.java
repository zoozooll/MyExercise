/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission.compat;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.database.ModelAttributes;
import com.oregonscientific.meep.database.Schema;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;

/**
 * The {@link PropertyTypeAdapterAdapterFactory} that creates {@link TypeAdapter}
 * to serialize or deserialize the internal properties of a permission {@link Message}
 */
public class GetPermissionMessagePropertyTypeAdapterFactory implements PropertyTypeAdapterFactory {
	
	private final String TAG = getClass().getSimpleName();
	
	private final List<String> PERMISSION_FIELD_NAMES = new ArrayList<String>() {
		
		private static final long serialVersionUID = 7584406057255449607L;

		{
			Set<SimpleEntry<String, String>> keys = Component.SYSTEM_COMPONENTS.keySet();
			for (SimpleEntry<String, String> entry : keys) {
				add(entry.getKey());
			}
		}
		
	};

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, Object key) {
		String keyString = key == null ? "" : key.toString().replaceAll("^\"|\"$", "");
		if (!PERMISSION_FIELD_NAMES.contains(keyString)) {
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
		public Permission read(JsonReader in) throws IOException {
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
			
			if (!Permission.class.isAssignableFrom(value.getClass())) {
				throw new IllegalStateException(value + " is not a Permission object");
			}
			Permission permission = (Permission) value;
			
			out.beginObject();
			
			// Writes time limit in seconds
			out.name(Permission.TIME_LIMIT_SERIALIZED_NAME);
			out.value(permission.getTimeLimit() / 60000);
			
			// Writes access level value
			out.name(Permission.ACCESS_FIELD_NAME);
			out.value(permission.getAccessLevel().toString());
			
			out.endObject();
		}
		
	}

}
