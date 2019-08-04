/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.notification.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;

/**
 * The {@link PropertyTypeAdapterAdapterFactory} that creates {@link TypeAdapter}
 * to serialize or deserialize the internal properties of a news {@link Message}
 */
public class NewsMessagePropertyTypeAdapterFactory implements PropertyTypeAdapterFactory {
	
	private final List<String> NEWS_FIELD_NAMES = Arrays.asList(new String[] {
			"news"
	});

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, Object key) {
		String keyString = key == null ? "" : key.toString().replaceAll("^\"|\"$", "");
		if (!NEWS_FIELD_NAMES.contains(keyString)) {
			return null; // We only handle serialization and deserialization of Permission
		}
		return (TypeAdapter<T>) new Adapter(gson);
	}
	
	public final class Adapter extends TypeAdapter<Object> {

		final Gson context;
		private TypeAdapter<$News> componentTypeAdapter;

		Adapter(Gson ctx) {
			context = ctx;
			componentTypeAdapter = ctx.getAdapter($News.class);
		}

		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			if (peek == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			
			List<$News> list = new ArrayList<$News>();
			try {
				in.beginArray();
				while (in.hasNext()) {
					$News instance = componentTypeAdapter.read(in);
					list.add(instance);
				}
				in.endArray();
			} catch (IllegalStateException e) {
				throw new JsonSyntaxException(e);
			}
			return list;
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			
			
			if (!List.class.isAssignableFrom(value.getClass())) {
				throw new IllegalStateException(value + " is not recognized");
			}
			
			out.beginArray();
			// TODO: implement the mechanism to write the object into JSON
			out.endArray();
		}
	}

}
