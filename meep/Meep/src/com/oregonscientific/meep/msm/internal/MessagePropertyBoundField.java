/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm.internal;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oregonscientific.meep.serialization.BoundField;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;
import com.oregonscientific.meep.serialization.TypeAdapterRuntimeTypeWrapper;

/**
 * The bound field that de/serializes the internal properties Map of 
 * a message. The bound field requires that the map keys can be serialized
 * as string.
 * 
 * The bound field will encode maps as arrays of map entries if complex key
 * type is detected. Each map entry is a two element array containing a key
 * and a value
 *
 * @param <K> The type of the keys maintained
 * @param <V> The type of maintained value
 */
public class MessagePropertyBoundField<K, V> extends BoundField {
	
	final protected Gson context;
	final TypeToken<?> fieldType;
	
	private final TypeAdapter<K> keyTypeAdapter;
	private final TypeAdapter<V> valueTypeAdapter;
	private final PropertyTypeAdapterFactory factory;
	private final ObjectConstructor<? extends Map<K, V>> constructor;
	
	@SuppressWarnings("unchecked")
	protected MessagePropertyBoundField(
			Gson context,
			PropertyTypeAdapterFactory factory,
			Field field,
			String name,
			TypeToken<?> fieldType,
			boolean serialize,
			boolean deserialize) {
		
		super(name, field, serialize, deserialize);
		
		this.context = context;
		this.fieldType = fieldType;
		this.factory = factory;
		
		Type type = fieldType.getType();
		Class<?> rawTypeOfSrc = $Gson$Types.getRawType(type);
		
		// Creates the key and value type adapters
		Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
		TypeAdapter<?> keyAdapter = getKeyAdapter(context, keyAndValueTypes[0]);
		TypeAdapter<?> valueAdapter = context.getAdapter(TypeToken.get(keyAndValueTypes[1]));
		
		keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(
				context,
				(TypeAdapter<K>) keyAdapter, 
				keyAndValueTypes[0]);
		valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(
				context, 
				(TypeAdapter<V>) valueAdapter, 
				keyAndValueTypes[1]);
		constructor = (ObjectConstructor<? extends Map<K, V>>) new ConstructorConstructor().get(fieldType);
	}
	
	/**
	 * Returns a type adapter that writes the value as a string.
	 */
	private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
		return (keyType == boolean.class || keyType == Boolean.class) 
				? TypeAdapters.BOOLEAN_AS_STRING
				: context.getAdapter(TypeToken.get(keyType));
	}
	
	/**
	 * Returns a type adapter that reads/writes the property of the object
	 */
	private TypeAdapter<? extends V> getValueAdapter(Gson context, Object container, Object key) {
		TypeAdapter<? extends V> result = valueTypeAdapter;
		// Query the {@link PropertyTypeAdapterFactory} for a more appropriate
		// {@link TypeAdapter} to serialize or deserialize value of the property
		if (factory != null) {
			TypeAdapter<? extends V> typeAdapter = factory.create(context, key);
			result = ((typeAdapter == null) ? result : typeAdapter);
		}return result;
	}
	
	/**
	 * Creates the bound field object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static MessagePropertyBoundField create(
			Gson context, 
			PropertyTypeAdapterFactory factory,
			Field field, 
			String name, 
			TypeToken<?> fieldType,
			boolean serialize,
			boolean deserialize) {
		
		Class<?> rawType = fieldType.getRawType();
	    if (!Map.class.isAssignableFrom(rawType)) {
	      return null;
	    }
	    
		return new MessagePropertyBoundField(context, factory, field, name, fieldType, serialize, deserialize);
	}
		
	// the type adapter and field type always agree
	@SuppressWarnings({ "unchecked" })
	@Override
	public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
		Map<K, V> instance = (Map<K, V>) field.get(value);
		
		// Quick return if there is nothing to process
		if (instance == null) {
			return;
		}
		
		boolean hasComplexKeys = false;
		List<JsonElement> keys = new ArrayList<JsonElement>(instance.size());
		
		List<V> values = new ArrayList<V>(instance.size());
		for (Map.Entry<K, V> entry : instance.entrySet()) {
			JsonElement keyElement = keyTypeAdapter.toJsonTree(entry.getKey());
			keys.add(keyElement);
			values.add(entry.getValue());
			hasComplexKeys |= keyElement.isJsonArray() || keyElement.isJsonObject();
		}
		
		if (hasComplexKeys) {
			for (int i = 0; i < keys.size(); i++) {
				writer.beginArray(); // entry array
				Streams.write(keys.get(i), writer);
				TypeAdapter<V> valueAdapter = (TypeAdapter<V>) getValueAdapter(context, value, keys.get(i));
				valueAdapter.write(writer, values.get(i));
				writer.endArray();
			}
		} else {
			for (int i = 0; i < keys.size(); i++) {
				JsonElement keyElement = keys.get(i);
				writer.name(keyToString(keyElement));
				TypeAdapter<V> valueAdapter = (TypeAdapter<V>) getValueAdapter(context, value, keys.get(i));
				valueAdapter.write(writer, values.get(i));
			}
		}
	}
	
	private String keyToString(JsonElement keyElement) {
		if (keyElement.isJsonPrimitive()) {
			JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
			if (primitive.isNumber()) {
				return String.valueOf(primitive.getAsNumber());
			} else if (primitive.isBoolean()) {
				return Boolean.toString(primitive.getAsBoolean());
			} else if (primitive.isString()) {
				return primitive.getAsString();
			} else {
				throw new AssertionError();
			}
		} else if (keyElement.isJsonNull()) {
			return "null";
		} else {
			throw new AssertionError();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void read(
			JsonReader reader, 
			final Object value, 
			String name) throws IOException, IllegalAccessException {
		
		Map<K, V> instance = (Map<K, V>) field.get(value);
		if (instance == null) {
			instance = constructor.construct();
		}
		
		// If the key was quoted, then parse the key as string, else
		// use the type adapter to read the JSON
		JsonReader in = null;
		if (name != null) {
			StringReader stringReader = new StringReader(name);
			in = new JsonReader(stringReader);
			in.setLenient(true);
		} else {
			in = reader;
		}
		
		K key = keyTypeAdapter.read(in);
		TypeAdapter<? extends V> valueAdapter = getValueAdapter(context, value, key);
		V val = valueAdapter.read(reader);
		Object replaced = instance.put(key, val);
		if (replaced != null) {
			throw new JsonSyntaxException("Duplicate key: " + name);
		}
		field.set(value, instance);
	}
	
}
