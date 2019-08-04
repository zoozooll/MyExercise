/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.idthk.meep.ota.utility;

import java.lang.reflect.Method;

/**
 * Gives access to the system properties store.  The system properties
 * store contains a list of string key-value pairs.
 */
public class SystemProperties {
	
	public static final int PROP_NAME_MAX = 31;
    public static final int PROP_VALUE_MAX = 91;
    
    /** The full class name to access Android system properties */
    private static final String CLASS_NAME = "android.os.SystemProperties";
    
    /** Value used for when a build property is unknown. */
    public static final String UNKNOWN = "unknown";
    
    /**
     * Get the value for the given key.
     * @return an empty string if the key isn't found
     */
    public static String get(String key) {
    	String result = "";
    	try {
    		Class<?> clazz = Class.forName(CLASS_NAME);
    		Method m = clazz.getMethod("get", String.class);
    		result = (String) m.invoke(clazz, key);
    	} catch (Exception ignored) {
    		
    	}
    	return result;
    }
    
    /**
     * Get the value for the given key.
     * @return if the key isn't found, return def if it isn't null, or an empty string otherwise
     */
    public static String get(String key, String def) {
    	String result = "";
    	try {
    		Class<?> clazz = Class.forName(CLASS_NAME);
    		Method m = clazz.getMethod("get", String.class, String.class);
    		result = (String) m.invoke(clazz, key, def);
    	} catch (Exception ignored) {
    		
    	}
    	return result;
    }
    
    /**
     * Get the value for the given key, and return as an integer.
     * @param key the key to lookup
     * @param def a default value to return
     * @return the key parsed as an integer, or def if the key isn't found or
     *         cannot be parsed
     */
    public static int getInt(String key, int def) {
    	int result = def;
    	try {
    		Class<?> clazz = Class.forName(CLASS_NAME);
    		Method m = clazz.getMethod("getInt", String.class, Integer.class);
    		result = (Integer) m.invoke(clazz, key, def);
    	} catch (Exception ignored) {
    		
    	}
    	return result;
    }
    
    /**
     * Get the value for the given key, and return as a long.
     * @param key the key to lookup
     * @param def a default value to return
     * @return the key parsed as a long, or def if the key isn't found or
     *         cannot be parsed
     */
    public static long getLong(String key, long def) {
    	long result = def;
    	try {
    		Class<?> clazz = Class.forName(CLASS_NAME);
    		Method m = clazz.getMethod("getLong", String.class, Long.class);
    		result = (Long) m.invoke(clazz, key, def);
    	} catch (Exception ignored) {
    		
    	}
    	return result;
    }
    
    /**
     * Get the value for the given key, returned as a boolean.
     * Values 'n', 'no', '0', 'false' or 'off' are considered false.
     * Values 'y', 'yes', '1', 'true' or 'on' are considered true.
     * (case sensitive).
     * If the key does not exist, or has any other value, then the default
     * result is returned.
     * @param key the key to lookup
     * @param def a default value to return
     * @return the key parsed as a boolean, or def if the key isn't found or is
     *         not able to be parsed as a boolean.
     */
    public static boolean getBoolean(String key, boolean def) {
    	boolean result = def;
    	try {
    		Class<?> clazz = Class.forName(CLASS_NAME);
    		Method m = clazz.getMethod("getBoolean", String.class, Boolean.class);
    		result = (Boolean) m.invoke(clazz, key, def);
    	} catch (Exception ignored) {
    		
    	}
    	return result;
    }
    
    /**
     * Set the value for the given key.
     * @throws IllegalArgumentException if the key exceeds 32 characters
     * @throws IllegalArgumentException if the value exceeds 92 characters
     */
    public static void set(String key, String val) {
    	if (key == null || key.length() > PROP_NAME_MAX) {
    		throw new IllegalArgumentException(key + "length > " + PROP_NAME_MAX);
    	}
    	if (val == null || val.length() > PROP_VALUE_MAX) {
    		throw new IllegalArgumentException(val + ".length > " + PROP_NAME_MAX);
    	}
    	try {
    		Class<?> clazz = Class.forName(CLASS_NAME);
    		Method m = clazz.getMethod("set", String.class, String.class);
    		m.invoke(clazz, key, val);
    	} catch (Exception ignored) {
    		// Ignored
    	}
    }

}
