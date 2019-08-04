/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beem.project.btf.ui.loadimages;

import java.io.FileDescriptor;
import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.beem.project.btf.BuildConfig;

/**
 * A simple subclass of {@link ImageWorker} that resizes images from resources given a target width
 * and height. Useful for when the input images might be too large to simply load directly into
 * memory.
 */
public class ImageResizer extends ImageWorker {
	private static final String TAG = "ImageResizer";
	protected int mImageWidth;
	protected int mImageHeight;

	/**
	 * Initialize providing a single target image size (used for both width and height);
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageResizer(Context context, int imageWidth, int imageHeight) {
		super(context);
		setImageSize(imageWidth, imageHeight);
	}
	/**
	 * Initialize providing a single target image size (used for both width and height);
	 * @param context
	 * @param imageSize
	 */
	public ImageResizer(Context context, int imageSize) {
		super(context);
		setImageSize(imageSize);
	}
	public ImageResizer(Context context, ImageSize size) {
		super(context);
		setImageSize(size.width, size.height);
	}
	/**
	 * Set the target image width and height.
	 * @param width
	 * @param height
	 */
	public void setImageSize(int width, int height) {
		mImageWidth = width;
		mImageHeight = height;
	}
	/**
	 * Set the target image size (width and height will be the same).
	 * @param size
	 */
	public void setImageSize(int size) {
		setImageSize(size, size);
	}
	/**
	 * The main processing method. This happens in a background task. In this case we are just
	 * sampling down the bitmap and returning it from a resource.
	 * @param resId
	 * @return
	 */
	public Bitmap processBitmap(int resId) {
		if (BuildConfig.DEBUG) {
			Log.d(TAG, "processBitmap - " + resId);
		}
		return decodeSampledBitmapFromResource(mResources, resId, mImageWidth,
				mImageHeight, getImageCache());
	}
	/**
	 * @deprecated or the param Object is at least not understood by users;
	 */
	@Deprecated
	@Override
	protected Bitmap processBitmap(Object data) {
		return processBitmap(Integer.parseInt(String.valueOf(data)));
	}
	/**
	 * Decode and sample down a bitmap from resources to the requested width and height.
	 * @param res The resources object containing the image data
	 * @param resId The resource id of the image data
	 * @param reqWidth The requested width of the resulting bitmap
	 * @param reqHeight The requested height of the resulting bitmap
	 * @param cache The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
	 *         that are equal to or greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight, ImageCache cache) {
		// BEGIN_INCLUDE (read_bitmap_dimensions)
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// END_INCLUDE (read_bitmap_dimensions)
		// If we're running on Honeycomb(11) or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= 11) {
			addInBitmapOptions(options, cache);
		}
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	/**
	 * Decode and sample down a bitmap from a file to the requested width and height.
	 * @param filename The full path of the file to decode
	 * @param reqWidth The requested width of the resulting bitmap
	 * @param reqHeight The requested height of the resulting bitmap
	 * @param cache The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
	 *         that are equal to or greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename,
			int reqWidth, int reqHeight, ImageCache cache) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		// Calculate inSampleSize
		Log.d(TAG, "decodeSampledBitmapFromFile previous " + options.outWidth
				+ "," + options.outHeight);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// If we're running on Honeycomb or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= 11) {
			addInBitmapOptions(options, cache);
		}
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Log.d(TAG, "decodeSampledBitmapFromFile inSampleSize "
				+ options.inSampleSize);
		try {
			Bitmap bmp = BitmapFactory.decodeFile(filename, options);
			if (bmp != null) {
				Log.d(TAG, "decodeSampledBitmapFromFile after    "
						+ options.outWidth + "," + options.outHeight
						+ "==>total:" + options.outWidth * options.outHeight
						* 4);
			}
			return bmp;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Decode and sample down a bitmap from a file input stream to the requested width and height.
	 * @param fileDescriptor The file descriptor to read from
	 * @param reqWidth The requested width of the resulting bitmap
	 * @param reqHeight The requested height of the resulting bitmap
	 * @param cache The ImageCache used to find candidate bitmaps for use with inBitmap
	 * @return A bitmap sampled down from the original with the same aspect ratio and dimensions
	 *         that are equal to or greater than the requested width and height
	 */
	public static Bitmap decodeSampledBitmapFromDescriptor(
			FileDescriptor fileDescriptor, int reqWidth, int reqHeight,
			ImageCache cache) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		// If we're running on Honeycomb or newer, try to use inBitmap
		if (Build.VERSION.SDK_INT >= 11) {
			addInBitmapOptions(options, cache);
		}
		return BitmapFactory
				.decodeFileDescriptor(fileDescriptor, null, options);
	}
	@TargetApi(11)
	private static void addInBitmapOptions(BitmapFactory.Options options,
			ImageCache cache) {
		// BEGIN_INCLUDE(add_bitmap_options)
		// inBitmap only works with mutable bitmaps so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true;
		if (cache != null) {
			// Try and find a bitmap to use for inBitmap
			Bitmap inBitmap = cache.getBitmapFromReusableSet(options);
			if (inBitmap != null) {
				options.inBitmap = inBitmap;
			}
		}
		// END_INCLUDE(add_bitmap_options)
	}
	/**
	 * Calculate an inSampleSize for use in a {@link android.graphics.BitmapFactory.Options} object
	 * when decoding bitmaps using the decode* methods from {@link android.graphics.BitmapFactory}.
	 * This implementation calculates the closest inSampleSize that is a power of 2 and will result
	 * in the final decoded bitmap having a width and height equal to or larger than the requested
	 * width and height.
	 * @param options An options object with out* params already populated (run through a decode*
	 *            method with inJustDecodeBounds==true
	 * @param reqWidth The requested width of the resulting bitmap
	 * @param reqHeight The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// BEGIN_INCLUDE (calculate_sample_size)
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger inSampleSize).
			long totalPixels = width * height / (inSampleSize * inSampleSize);
			// Anything more than 2x the requested pixels we'll sample down further
			final long totalReqPixelsCap = reqWidth * reqHeight * 4;
			while (totalPixels > totalReqPixelsCap) {
				inSampleSize *= 2;
				totalPixels /= 2;
			}
		}
		return inSampleSize;
		// END_INCLUDE (calculate_sample_size)
	}
	/**
	 * 根据ImageView获得适当的压缩的宽和高
	 * @param imageView
	 * @return
	 */
	protected static ImageSize getImageViewWidth(ImageView imageView) {
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();
		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get
								// actual
								// image
								// width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
																	// maxWidth
																	// parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get
								// actual
								// image
								// height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
																		// maxHeight
																		// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;
	}
	/**
	 * 反射获得ImageView设置的最大宽度和高度
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
				Log.e("TAG", value + "");
			}
		} catch (Exception e) {
		}
		return value;
	}

	protected static class ImageSize {
		int width;
		int height;
	}
}
