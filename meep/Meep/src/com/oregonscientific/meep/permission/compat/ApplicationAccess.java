/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.permission.compat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oregonscientific.meep.permission.Permission;

/**
 * The object is retained for backward compatibility
 */
public class ApplicationAccess {
	
	/**
	 * An enumeration of access rights
	 */
	public static enum Level {

		ALLOW("allow"), 
		LOW("low"),
		MEDIUM("medium"), 
		APPROVAL("approval"),
		DENY("deny"),
		HIGH("high");

		private String name = null;

		Level(String accessString) {
			this.name = accessString;
		}

		public String toString() {
			return name;
		}
		
		public static Level fromString(String text) {
			if (text != null) {
				for (Level b : Level.values()) {
					if (text.equalsIgnoreCase(b.name)) {
						return b;
					}
				}
			}
			return DENY;
		}
	}

	/** Whether or not the app can be accessed by user */
	@Expose
	protected String access = null;
	
	/** The amount of time the app can be accessed per day */
	@SerializedName("timelimit")
	@Expose
	protected long timeLimit = 0;
	
	ApplicationAccess(String access, long timeLimit) {
		this.access = access;
		this.timeLimit = timeLimit;
	}
	
	private ApplicationAccess(Permission permission) {
		if (permission == null) {
			throw new NullPointerException();
		}
		
		access = permission.getAccessLevel().toString();
		// Convert time limit from milliseconds to seconds
		timeLimit = permission.getTimeLimit() / 60000;
	}
	
	/**
	 * Converts the ApplicationAccess into its {@link Permission} representation.
	 * 
	 * @return the Permission representation of this object
	 */
	public Permission toPermission() {
		// Convert time limit from seconds to milliseconds
		return new Permission(access, timeLimit * 60000);
	}
	
	/**
	 * Parses the {@code permission} into an ApplicationAccess object
	 * 
	 * @param permission the {@link Permission} object to parse
	 * @return An ApplicationAccess object represented by {@code permission}
	 */
	public static ApplicationAccess parsePermission(Permission permission) {
		return permission == null ? null : new ApplicationAccess(permission);
	}
}
	
