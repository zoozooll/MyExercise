/**
 * 
 */
package com.mogoo.ping.utils;

import java.io.File;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;

/**
 * @author Aaron Lee
 * Image tools. All the tool methods will be written here
 * @Date 下午3:48:22  2012-10-15
 */
public class ImageTools {
	
	public static Bitmap getBitmapFromFile(String imagePath, int width, int height) {
	    if (imagePath != null && new File(imagePath).exists()) {
	        BitmapFactory.Options opts = null;
	        if (width > 0 && height > 0) {
	            opts = new BitmapFactory.Options();
	            opts.inJustDecodeBounds = true;
	            Bitmap b = BitmapFactory.decodeFile(imagePath, opts);
	            // math the scale of the image
	            final int minSideLength = Math.min(width, height);
	            opts.inSampleSize = computeSampleSize(opts, minSideLength,
	                    width * height);
	            opts.inJustDecodeBounds = false;
	            opts.inInputShareable = true;
	            opts.inPurgeable = true;
	            if (b != null) {
	            	b.recycle();
	            }
	        }
	        try {
	            return BitmapFactory.decodeFile(imagePath, opts);
	        } catch (OutOfMemoryError e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}
	
	public static int computeSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options, minSideLength,
	            maxNumOfPixels);

	    int roundedSize;
	    if (initialSize <= 8) {
	        roundedSize = 1;
	        while (roundedSize < initialSize) {
	            roundedSize <<= 1;
	        }
	    } else {
	        roundedSize = (initialSize + 7) / 8 * 8;
	    }

	    return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
	        int minSideLength, int maxNumOfPixels) {
	    double w = options.outWidth;
	    double h = options.outHeight;

	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
	            .sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
	            .floor(w / minSideLength), Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
	        return lowerBound;
	    }

	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	        return 1;
	    } else if (minSideLength == -1) {
	        return lowerBound;
	    } else {
	        return upperBound;
	    }
	}
	
	 public static Drawable getFormatDrawable(Drawable preDrawable, int resizeWidth, int resizeHeight) {
	    	final int iconWidth = preDrawable.getIntrinsicWidth();
	        final int iconHeight = preDrawable.getIntrinsicHeight();
	        Rect rect = new Rect();

	        if (preDrawable instanceof PaintDrawable) {
	            PaintDrawable painter = (PaintDrawable) preDrawable;
	            painter.setIntrinsicWidth(resizeWidth);
	            painter.setIntrinsicHeight(resizeHeight);
	            return painter;
	        }

	        if (resizeWidth > 0 && resizeHeight > 0 ) {
	            final float ratio = (float) iconWidth / iconHeight;

	            if (iconWidth > iconHeight) {
	            	resizeHeight = (int) (resizeWidth / ratio);
	            } else if (iconHeight > iconWidth) {
	            	resizeWidth = (int) (resizeHeight * ratio);
	            }

	            final Bitmap.Config c =
	            		preDrawable.getOpacity() != PixelFormat.OPAQUE ?
	                        Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
	            final Bitmap thumb = Bitmap.createBitmap(resizeWidth, resizeHeight, c);
	            final Canvas canvas = new Canvas(thumb);
	            canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, 0));
	            // Copy the old bounds to restore them later
	            // If we were to do oldBounds = icon.getBounds(),
	            // the call to setBounds() that follows would
	            // change the same instance and we would lose the
	            // old bounds
	            rect.set(preDrawable.getBounds());
	            preDrawable.setBounds(0, 0, resizeWidth, resizeHeight);
	            preDrawable.draw(canvas);
	            preDrawable.setBounds(rect);
	            preDrawable = new BitmapDrawable(thumb);
	            //thumb.recycle();
	            //rect.setEmpty();
	        }
	        return preDrawable;
	 }
}
