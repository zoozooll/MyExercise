/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep;

import java.io.File;

/**
 * Provides access to environment variables.
 */
public class MEEPEnvironment {

	private static final String TAG = "MEEPEnvironment";
	
	/**
	 * Standard directory in which to place any audio files that are available to the user.
	 * Note that this is primarily a convention for the top-level public
	 * directory, as the media scanner will find and collect music in any
	 * directory.
	 */
	public static final String DIRECTORY_MUSIC = "music";
	
	/**
	 * Standard directory in which to place movies that are available to the user.
	 * Note that this is primarily a convention for the top-level public
	 * directory, as the media scanner will find and collect movies in any
	 * directory.
	 */
	public static final String DIRECTORY_MOVIE = "movie";

	/**
	 * Standard directory in which to place ebooks that are available to the user.
	 * Note that this is primarily a convention for the top-level public
	 * directory, as the media scanner will find and collect e-books in any
	 * directory.
	 */
	public static final String DIRECTORY_EBOOK = "ebook";
	
	/**
	 * Standard directory in which to place games that are available to the user
	 */
	public static final String DIRECTORY_GAME = "game";
	
	/**
     * The traditional location for pictures and videos when mounting the
     * device as a camera.  Note that this is primarily a convention for the
     * top-level public directory, as this convention makes no sense elsewhere.
     */
    public static String DIRECTORY_DCIM = "DCIM";
    
    /**
     * The hidden external media storage directory 
     */
    private static String DIRECTORY_HOME =".home";
	
	/**
	 * Standard directory in which to place apps that are downloaded from a remote
	 * location
	 */
	public static final String DIRECTORY_APP = "app";
	
	private static final File EXTERNAL_STORAGE_DIRECTORY = getDirectory("/sdcard");
	
	private static final File DOWNLOAD_CACHE_DIRECTORY = getDirectory("/sdcard/.cache");
	
	private static File getDirectory(String path) {
		return path == null ? null : new File(path);
	}
	
	/**
	 * Gets the MEEP download/cache content directory.
	 */
	public static File getDownloadCacheDirectory() {
		return DOWNLOAD_CACHE_DIRECTORY;
	}

	/**
	 * Gets the MEEP media storage directory.
	 */
	public static File getMediaStorageDirectory() {
		return new File(getExternalStorageDirectory(), DIRECTORY_HOME);
	}
	
	/**
	 * Returns the external storage directory
	 */
	public static File getExternalStorageDirectory() {
		return EXTERNAL_STORAGE_DIRECTORY;
	}

	/**
	 * Return the media storage directory for a user. This is for use by
	 * applications to store files relating to the user. This directory shoulbe
	 * be deleted when the user is removed.
	 * 
	 * @hide
	 */
	public static File getUserMediaStorageDirectory(String meepTag) {
		return new File(new File(getExternalStorageDirectory(), "users"), meepTag);
	}

	/**
	 * Get a top-level public media storage directory for placing files of a
	 * particular type. This is where applications should place and manage their
	 * files for the given type.
	 * 
	 * @param type
	 *            The type of media to store. Should be one of
	 *            {@link #DIRECTORY_EBOOK}, {@link #DIRECTORY_APP},
	 *            {@link #DIRECTORY_GAME}, {@link #DIRECTORY_MOVIE} or
	 *            {@link #DIRECTORY_MUSIC}, {@link #DIRECTORY_DCIM}. May not be
	 *            null.
	 * 
	 * @return The File object of the given directory type
	 */
	public static File getStoragePublicDirectory(String type) {
		File result = new File(getExternalStorageDirectory(), type);
		createIfNotExists(result);
		return result;
	}

	/**
	 * Gets a top-level media storage directory isolated for the given user for
	 * placing files of a particular type.
	 * 
	 * @param meepTag
	 *            The unique MEEP user id. May not be null.
	 * @param type
	 *            The type of media to store. Should be one of
	 *            {@link #DIRECTORY_EBOOK}, {@link #DIRECTORY_APP},
	 *            {@link #DIRECTORY_GAME}, {@link #DIRECTORY_MOVIE} or
	 *            {@link #DIRECTORY_MUSIC}, {@link #DIRECTORY_DCIM}. May not be
	 *            null.
	 * 
	 * @return The File object of the given directory type isolated for the
	 *         given user
	 */
	public static File getStoragePublicDirectory(String meepTag, String type) {
		File result = new File(getUserMediaStorageDirectory(meepTag), type);
		createIfNotExists(result);
		return result;
	}

	/**
	 * Creates the directory and missing parent directories named by this file.
	 * 
	 * @param file
	 *            The object to create if it does not already exist
	 */
	private static void createIfNotExists(File file) {
		if (file != null && !file.exists()) {
			file.mkdirs();
		}
	}
	
}
