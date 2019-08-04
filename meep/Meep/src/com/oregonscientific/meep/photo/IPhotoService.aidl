/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.photo;

/**
 * Interface for interacting with the PhotoService
 */
 
 interface IPhotoService {
	 
	 /**
	  * Return the total numbers of photo in the device
	  * 
	  * @return return the number of photos in the device
	  */
	 int getPhotoCount();
	 
	 /**
	  * Returns a list of file {@link android.net.Uri} for the photos in the device
	  * 
	  * @return return a list of file Uri for the Photos, null if there is no photo in the device
	  */
	 List<String> getPhotos();
 }