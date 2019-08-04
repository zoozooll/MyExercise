/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;

/**
 * A class containing utility methods for reflection.
 */
public class Iterables {
	
	/**
	 * Returns a fixed size array containing all of the elements in the object
	 * 
	 * @param object the object to turn into an array
	 * @return an array containing all of the elements in the object, <code>null</code> if the object is not iterable
	 */
	public static Object[] asArray(Object object) {
		if (isArrayType(object)) {
			return (Object[]) object;
		} else if (isCollectionType(object)) {
			return ((Collection<?>) object).toArray();
		}
		
		return null;
	}
	
	/**
	 * Returns the type of the elements in an iterable object 
	 * 
	 * @param object the object to examine
	 * @return the type of the elements in an iterable object, <code>null</code> if object is not iterable
	 */
	public static Type getElementType(Object object) {
		if (!isIterable(object)) {
			return null;
		}
		
		TypeToken<?> typeToken = TypeToken.get(object.getClass());
		Type type = typeToken.getType();
		if (isArrayType(object)) {
			return $Gson$Types.getArrayComponentType(type);
		} else if (isCollectionType(object)) {
			return $Gson$Types.getCollectionElementType(type, typeToken.getRawType());
		}
		
		return null;
	}

	/**
	 * Determines whether or not the given object is iterable.
	 * 
	 * @param object the object to examine
	 * @return {@code true} is the object is iterable, {@code false} otherwise
	 */
	public static boolean isIterable(Object object) {
		if (object == null) {
			return false;
		}
		
		Class<?> clazz = object.getClass();
		if (clazz.isArray()) {
			return true;
		}
		
		TypeToken<?> token = TypeToken.get(clazz);
		return isIterable(token.getType());
	}
	
	/**
	 * Returns whether the given type is iterable 
	 * 
	 * @param type the type to examine
	 * @return {@code true} is the type is iterable, {@code false} otherwise
	 */
	public static boolean isIterable(Type type) {
		TypeToken<?> token = TypeToken.get(type);
		if (type instanceof Class && isIterableClass(token.getRawType())) {
			return true;
		}
		
		if (type instanceof ParameterizedType) {
			return isIterable(((ParameterizedType) type).getRawType());
		}
		
		if (type instanceof WildcardType) {
			Type[] upperBounds = ((WildcardType) type).getUpperBounds();
			return upperBounds.length != 0 && isIterable(upperBounds[0]);
		}
		return false;
	}
	
	/**
	 * Returns whether the given object is an array
	 * 
	 * @param object the object to examine
	 * @return {@code true} is the object is an array, {@code false} otherwise
	 */
	private static boolean isArrayType(Object object) {
		TypeToken<?> typeToken = TypeToken.get(object.getClass());
		Type type = typeToken.getType();
		boolean result = false;
		
		if (type instanceof GenericArrayType && type instanceof Class && ((Class<?>) type).isArray()) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Returns whether or not the given object is a collection
	 * 
	 * @param object the object to examine
	 * @return {@code true} is the object is an array, {@code false} otherwise
	 */
	private static boolean isCollectionType(Object object) {
		TypeToken<?> typeToken = TypeToken.get(object.getClass());
		boolean result = false;
		
		if (Collection.class.isAssignableFrom(typeToken.getRawType())) {
			result = true;
		}
		return result;
	}
	
	/**
	 * Checks whether the specified class parameter is an instance of a
	 * collection class.
	 * 
	 * @param clazz
	 *            <code>Class</code> to check.
	 * 
	 * @return <code>true</code> is <code>clazz</code> is instance of a
	 *         collection class, <code>false</code> otherwise.
	 */
	private static boolean isIterableClass(Class<?> clazz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		computeClassHierarchy(clazz, classes);
		return classes.contains(Iterable.class);
	}
	
	/**
	 * Get all superclasses and interfaces recursively.
	 * 
	 * @param clazz
	 *            The class to start the search with.
	 * @param classes
	 *            List of classes to which to add all found super classes and
	 *            interfaces.
	 */
	private static void computeClassHierarchy(Class<?> clazz, List<Class<?>> classes) {
		// Quick return if the call to this method cannot be processed
		if (classes == null) {
			return;
		}
		
		for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
			if (classes.contains(current)) {
				return;
			}
			
			classes.add(current);
			for (Class<?> currentInterface : current.getInterfaces()) {
				computeClassHierarchy(currentInterface, classes);
			}
		}
	}
}
