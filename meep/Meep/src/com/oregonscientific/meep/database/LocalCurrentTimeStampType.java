/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.database;

import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.TimeStampType;

/**
 * Type that persists a {@link java.sql.Timestamp} object. The value is updated
 * to the current time whenever the data object containing this type is updated.
 */
public class LocalCurrentTimeStampType extends TimeStampType {
	
	private static final LocalCurrentTimeStampType singleton = new LocalCurrentTimeStampType();
	
	public LocalCurrentTimeStampType() {
		super(SqlType.DATE, new Class<?>[] { java.sql.Timestamp.class });
	}
	
	public static LocalCurrentTimeStampType getSingleton() {
		return singleton;
	}
	
	@Override
	public Object moveToNextValue(Object currentValue) {
		long newVal = System.currentTimeMillis();
		if (currentValue == null) {
			return new java.sql.Timestamp(newVal);
		} else if (newVal == ((java.sql.Timestamp) currentValue).getTime()) {
			return new java.sql.Timestamp(newVal + 1L);
		} else {
			return new java.sql.Timestamp(newVal);
		}
	}
}