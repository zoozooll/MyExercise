/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.util;

import java.io.File;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * A class containing utility methods related to Bitmaps
 * 
 * @author Stanley Lam
 */
public class BitmapUtils {
	
	/**
	 * Calculates the sample size based on the required {@code} reqWidth and {@code} reqHeight
	 * 
	 * @param options The bitmap options
	 * @param reqWidth The required width
	 * @param reqHeight the required height
	 * @return the in sample size
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	/**
	 * Decodes bitmap from the given {@code} resource  
	 * 
	 * @param resource The resouse object in which to retrieve the bitmap resouce
	 * @param resId The id of the bitmap resource
	 * @param reqWidth The required width of the resulting bitmap in pixels
	 * @param reqHeight The required height of the resulting bitmap in pixels
	 * @return The decoded bitmap if the resource exists, null otherwise
	 */
	public static Bitmap decodeSampledBitmapFromResource(
			Resources resource, 
			int resId, 
			int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resource, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(resource, resId, options);
	}
	
	/**
	 * Decodes bitmap from the given {@code} resource  
	 * 
	 * @param file The file to decode
	 * @param reqWidth The required width of the resulting bitmap in pixels
	 * @param reqHeight The required height of the resulting bitmap in pixels
	 * @return The decoded bitmap if the resource exists, null otherwise
	 */
	public static Bitmap decodeSampledBitmapFromFile(File file, int reqWidth, int reqHeight) {
		if (file == null || !file.exists()) {
			return null;
		}
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(file.getAbsolutePath(), options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
	}
	
	/**
	 * Decode an immutable bitmap from the specified byte array.
	 * 
	 * @param data byte array of compressed image data
	 * @param offset offset into imageData for where the decoder should begin parsing.
	 * @param length the number of bytes, beginning at offset, to parse
	 * @param reqWidth The required width of the resulting bitmap in pixels
	 * @param reqHeight The required height of the resulting bitmap in pixels
	 * @return The decoded bitmap, or null if the image data could not be decoded.
	 */
	public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
		if (data == null || length == 0) {
			return null;
		}
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, length, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, length, options);
	}

}
