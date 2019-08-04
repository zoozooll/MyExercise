/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.msm.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.serialization.BoundField;
import com.oregonscientific.meep.serialization.PropertyTypeAdapterFactory;

/**
 * Type adapter that reflects over the fields and methods of a 'Message' class.
 */
public class MessageTypeAdapterFactory implements TypeAdapterFactory {
	
	/** The field name to store internal properties of the object */ 
	private final String PROPERTIES_FIELD_NAME = "mProperties";
	
	private final ConstructorConstructor constructorConstructor;
	private final FieldNamingStrategy fieldNamingPolicy;
	private final Excluder excluder;
	
	private boolean omitProperties = false;
	private PropertyTypeAdapterFactory propertyTypeAdapterFactory;
	
	public MessageTypeAdapterFactory(
			ConstructorConstructor constructorConstructor,
			FieldNamingStrategy fieldNamingPolicy, 
			Excluder excluder) {
		
		this.constructorConstructor = constructorConstructor;
		this.fieldNamingPolicy = fieldNamingPolicy;
		this.excluder = excluder;
	}
	
	/**
	 * Tells GSON to skip parsing properties in a {@link Message} 
	 * 
	 * @return the newly configured {@link MessageTypeAdapterFactory}
	 */
	public MessageTypeAdapterFactory omitParsingProperties() {
		omitProperties = true;
		return this;
	}
	
	/**
	 * Tells GSON to parse properties in a {@link Message} 
	 * 
	 * @return the newly configured {@link MessageTypeAdapterFactory}
	 */
	public MessageTypeAdapterFactory parseProperties() {
		omitProperties = false;
		return this;
	}
	
	/**
	 * Tells GSON to use {@code factory} to retrieve the {@link TypeAdapter} for parsing properties
	 * in a {@link Message} 
	 * 
	 * @param factory the {@link PropertyTypeAdapterFactory} to use
	 * @return the newly configured {@link MessageTypeAdapterFactory}
	 */
	public MessageTypeAdapterFactory usePropertyTypeAdapterFactory(PropertyTypeAdapterFactory factory) {
		propertyTypeAdapterFactory = factory;
		return this;
	}

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		Class<? super T> raw = type.getRawType();

		if (!Message.class.isAssignableFrom(raw)) {
			return null; // it's not a Message!
		}
		
		ObjectConstructor<T> constructor = constructorConstructor.get(type);
		@SuppressWarnings({ "unchecked", "rawtypes" })
		TypeAdapter<T> result = (TypeAdapter<T>) new MessageTypeAdapter(constructor, getBoundFields(gson, type, raw));
		return result;
	}
	
	/**
	 * Returns true if the given field should be excluded in serialization, false
	 * otherwise
	 * @param f the field to serialize
	 * @param serialize true if it was serializing, false if it was deserializing
	 * @return true if the field should be included in de/serialization, false otherwise
	 */
	private boolean includeField(Field f, boolean serialize) {
		// The internal properties field should never be excluded unless {@link
		// parseRequiredFieldsOnly} is set to {@code true}
		if (PROPERTIES_FIELD_NAME.equals(f.getName()) && !omitProperties) {
			return true;
		}
		
		return !excluder.excludeClass(f.getType(), serialize) && !excluder.excludeField(f, serialize);
	}
	
	/**
	 * Retrieves the name of the given field
	 * @param f the field to retrieve the name for serialization
	 * @return The name of to use for the given field during serialization
	 */
	private String getFieldName(Field f) {
		SerializedName serializedName = f.getAnnotation(SerializedName.class);
		return serializedName == null ? fieldNamingPolicy.translateName(f) : serializedName.value();
	}
	
	/**
	 * Creates a bound field
	 * 
	 * @param context the gson context
	 * @param field the field associated with the bound field
	 * @param name the name of the bound field
	 * @param fieldType the type of the field
	 * @param serialize true to serialize the field, false otherwise
	 * @param deserialize true to deserialzie the field, false otherwise
	 * @return the BoundField object associated with the given Field
	 */
	protected BoundField createBoundField(
			final Gson context,
			final Field field,
			final String name,
			final TypeToken<?> fieldType,
			boolean serialize,
			boolean deserialize) {
		
		if (PROPERTIES_FIELD_NAME.equals(name)) {
			return MessagePropertyBoundField.create(context, propertyTypeAdapterFactory, field, name, fieldType, serialize, deserialize);
		} else {
			return MessageBoundField.create(context, field, name, fieldType, serialize, deserialize);
		}
	}
	
	/**
	 * Creates the bound fields for the given type
	 * @param context the Gson context
	 * @param type the type of the class
	 * @param raw the raw type of the class
	 * @return a map of bound fields
	 */
	private Map<String, BoundField> getBoundFields(
			Gson context,
			TypeToken<?> type,
			Class<?> raw) {
		
		Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
		if (raw.isInterface()) {
			return result;
		}
		
		Type declaredType = type.getType();
		while (raw != Object.class) {
			Field[] fields = raw.getDeclaredFields();
			for (Field field : fields) {
				boolean serialize = includeField(field, true);
				boolean deserialize = includeField(field, false);
				
				if (!serialize && !deserialize) {
					continue;
				}
				
				field.setAccessible(true);
				Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
				BoundField boundField = createBoundField(context, field, getFieldName(field), TypeToken.get(fieldType), serialize, deserialize);
				
				BoundField previous = result.put(boundField.name, boundField);
				if (previous != null) {
					throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
				}
			}
			
			type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
			raw = type.getRawType();
		}
		
		return result;
	}

}
