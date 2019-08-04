/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.logging;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {@code Level} objects are used to indicate the level of logging. There are a
 * set of predefined logging levels, each associated with an integer value.
 * Enabling a certain logging level also enables all logging levels with larger
 * values.
 * <p>
 * The predefined levels in ascending order are DEBUG, VERBOSE, CONFIG,
 * INFO, WARNING, SEVERE.
 */
public class Level implements Parcelable {
	
	private static final List<Level> levels = new ArrayList<Level>(9);
	
	/**
	 * The SEVERE level provides severe failure messages.
	 */
	public static final Level SEVERE = new Level("SEVERE", 1000);

	/**
	 * The WARNING level provides warnings.
	 */
	public static final Level WARNING = new Level("WARNING", 900);

	/**
	 * The INFO level provides informative messages.
	 */
	public static final Level INFO = new Level("INFO", 800);

	/**
	 * The CONFIG level provides static configuration messages.
	 */
	public static final Level CONFIG = new Level("CONFIG", 700);

	/**
	 * The VERBOSE level provides verbose messages.
	 */
	public static final Level VERBOSE = new Level("VERBOSE", 600);

	/**
	 * The VERBOSE level provides verbose messages.
	 */
	public static final Level DEBUG = new Level("DEBUG", 500);
	
	/**
     * Parses a level name into a {@code Level} object.
     *
     * @param name
     *            the name of the desired {@code level}, which cannot be
     *            {@code null}.
     * @return the level with the specified name.
     * @throws NullPointerException
     *             if {@code name} is {@code null}.
     * @throws IllegalArgumentException
     *             if {@code name} is not valid.
     */
	public static Level parse(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new NullPointerException("name == null");
        }

        boolean isNameAnInt;
        int nameAsInt;
        try {
            nameAsInt = Integer.parseInt(name);
            isNameAnInt = true;
        } catch (NumberFormatException e) {
            nameAsInt = 0;
            isNameAnInt = false;
        }

        synchronized (levels) {
            for (Level level : levels) {
                if (name.equals(level.getName())) {
                    return level;
                }
            }

            if (isNameAnInt) {
                /*
                 * Loop through levels a second time, so that the returned
                 * instance will be passed on the order of construction.
                 */
                for (Level level : levels) {
                    if (nameAsInt == level.intValue()) {
                        return level;
                    }
                }
            }
        }

        if (!isNameAnInt) {
            throw new IllegalArgumentException("Cannot parse name '" + name + "'");
        }

        return new Level(name, nameAsInt);
    }

	/**
	 * The name of this Level.
	 * 
	 * @serial
	 */
	private final String name;

	/**
	 * The integer value indicating the level.
	 * 
	 * @serial
	 */
	private final int value;
  
	/**
	 * Constructs an instance of {@code Level} taking the supplied name and
	 * level value.
	 * 
	 * @param name
	 *            the name of the level.
	 * @param level
	 *            an integer value indicating the level.
	 * @throws NullPointerException
	 *             if {@code name} is {@code null}.
	 */
	protected Level(String name, int level) {
		if (name == null) {
			throw new NullPointerException("Name of a Level cannot be null");
		}
		this.name = name;
		value = level;
	}
	
	/**
   * Gets the name of this level.
   *
   * @return this level's name.
   */
	public String getName() {
		return name;
	}
	
	/**
   * Gets the integer value indicating this level.
   *
   * @return this level's integer value.
   */
	public final int intValue() {
		return value;
	}
	
	/**
   * Compares two {@code Level} objects for equality. They are considered to
   * be equal if they have the same level value.
   *
   * @param o
   *            the other object to compare this level to.
   * @return {@code true} if this object equals to the supplied object,
   *         {@code false} otherwise.
   */
  @Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Level)) {
			return false;
		}

		return ((Level) o).intValue() == value;
	}

  /**
   * Returns the hash code of this {@code Level} object.
   *
   * @return this level's hash code.
   */
  @Override
	public int hashCode() {
		return value;
	}

  /**
   * Returns the string representation of this {@code Level} object. In
   * this case, it is the level's name.
   *
   * @return the string representation of this level.
   */
  @Override
	public final String toString() {
		return name;
	}

	@Override
	public int describeContents() {
		return value;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(value);
	}
	
	/** Implement the Parcelable interface {@hide} */
	public static final Creator<Level> CREATOR = 
		new Creator<Level>() {
		
			@Override
			public Level createFromParcel(Parcel source) {
				return new Level(source.readString(), source.readInt()); 
			}

			@Override
			public Level[] newArray(int size) {
				return new Level[size];
			}
	};

}
