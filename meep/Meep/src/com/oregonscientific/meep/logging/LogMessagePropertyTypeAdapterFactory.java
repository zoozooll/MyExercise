/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.database.Model;
import com.oregonscientific.meep.database.internal.ModelSerializationPolicy;
import com.oregonscientific.meep.database.internal.ModelSerializationStrategy;
import com.oregonscientific.meep.database.internal.ModelTypeAdapter;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;
import com.oregonscientific.meep.serialization.TypeAdapterRuntimeTypeWrapper;

/**
 * The {@link PropertyTypeAdapterAdapterFactory} that creates {@link TypeAdapter}
 * to serialize or deserialize the internal properties of a log {@link Message}
 */
public class LogMessagePropertyTypeAdapterFactory implements PropertyTypeAdapterFactory {
	
	private final String TAG = getClass().getSimpleName();
	
	/** Keys used to decode a "log" message */
	private final String KEY_LOG = "logs";

	@SuppressWarnings("unchecked")
	@Override
	public <T> TypeAdapter<T> create(Gson gson, Object key) {
		String keyString = key == null ? "" : key.toString().replaceAll("^\"|\"$", "");
		if (!KEY_LOG.equals(keyString)) {
			return null; // We only handle serialization and deserialization of LogRecord
		}
		return (TypeAdapter<T>) new Adapter(gson);
	}
	

	public final class Adapter extends TypeAdapter<Object> {

		final Gson context;
		final TypeAdapter<$Log$Record> elementTypeAdapter;

		@SuppressWarnings("unchecked")
		Adapter(Gson ctx) {
			context = ctx;
			
			TypeToken<?> typeToken = TypeToken.get($Log$Record.class);
			ModelTypeAdapter<$Log$Record> delegate = (ModelTypeAdapter<$Log$Record>) Model.getTypeAdapterFactory($Log$Record.class).create(context, typeToken);
			
			// Creates the serialization strategy for the adapter
			Map<String, ?> expansions = new HashMap<String, Object>();
			expansions.put($Log$Record.PARAMETERS_FIELD_NAME, null);
			ModelSerializationStrategy policy = ModelSerializationPolicy.DEFAULT
					.disableIdFieldOnlySerialization()
					.withExpansionTree(expansions);
			delegate.setSerializationStrategy(policy);
			
			elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<$Log$Record>(ctx, delegate, typeToken.getType());
		}
		
		@Override
		public List<$Log$Record> read(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			if (peek == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			
			List<$Log$Record> collection = new ArrayList<$Log$Record>();
			try {
				in.beginArray();
				while (in.hasNext()) {
					$Log$Record instance = elementTypeAdapter.read(in);
					collection.add(instance);
				}
				in.endArray();
			} catch (IllegalStateException e) {
				throw new JsonSyntaxException(e);
			}
			
			return collection;
		}
		
		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			
			if (!Collection.class.isAssignableFrom(value.getClass())) {
				throw new IllegalStateException(value + " is not Iterable");
			}
			Collection<?> collection = (Collection<?>) value;
			
			out.beginArray();
			for (Object element : collection) {
				if (!$Log$Record.class.isAssignableFrom(element.getClass())) {
					throw new IllegalStateException(TAG + " cannot serialize " + element.getClass());
				}
				elementTypeAdapter.write(out, ($Log$Record) element);
			}
			out.endArray();
		}
		
	}

}
