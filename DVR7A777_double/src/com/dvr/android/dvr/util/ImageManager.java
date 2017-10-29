package com.dvr.android.dvr.util;

import java.io.File;
import java.io.IOException;

import com.dvr.android.dvr.Config;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

public class ImageManager {
	
	private static final String TAG = "ImageManager";
	
	private static final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
    
	public static Uri addImage(ContentResolver cr, String title, long dateTaken,
            Location location, File file) {
        Log.i(TAG, "write jpeg data to file");
        String fileName = title + Config.IMAGE_SUFFIX;
        String filePath = file.getAbsolutePath();
        int degree = getExifOrientation(filePath);
        long size = file.length();
        
        ContentValues values = new ContentValues(9);
        
        values.put(MediaColumns.TITLE, title);
        values.put(MediaColumns.DISPLAY_NAME, fileName);
        values.put(ImageColumns.DATE_TAKEN, dateTaken);
        values.put(MediaColumns.MIME_TYPE, "image/jpeg");
        values.put(ImageColumns.ORIENTATION, degree);
        values.put(MediaColumns.DATA, filePath);
        values.put(MediaColumns.SIZE, size);

        if (location != null) {
            values.put(ImageColumns.LATITUDE, location.getLatitude());
            values.put(ImageColumns.LONGITUDE, location.getLongitude());
        }
        return cr.insert(STORAGE_URI, values);
	}

	public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e(TAG, "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch(orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }
	
}
