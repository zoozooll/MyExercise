/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Creates type adapters for the internal properties of an object.
 * 
 * <p>Type adapter factories select the type adapter for a given property
 * in the internal properties map of an object. Factories should expect
 * {@code create} to be called on them for each property maintained in
 * an object. If the factory does not provide a custom type adapter for
 * a given property, <code>null</code> should be returned.
 */
public interface PropertyTypeAdapterFactory {
	
	/**
	 * Returns a type adapter for {@code key}, or <code>null</code> if this
	 * factory does not provide a custom type adapter for the property
	 * 
	 * @param gson The gson context 
	 * @param key the key identifying the property
	 * @return the type adapter for the given property or null if the factory
	 * does not provide a custom type adapter
	 */
	<T> TypeAdapter<T> create(Gson gson, Object key);

}
