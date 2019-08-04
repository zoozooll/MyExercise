package com.butterfly.vv.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

public class Utils {
	private static String TAG = "Utils";

	public static String getCurrentTime() {
		String format = "yyyy-MM-dd  HH:mm:ss";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static final int GB = 1024 * 1024 * 1024;
	public static final int MB = 1024 * 1024;
	public static final int KB = 1024;

	public static String formatDataSize(long size) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.setMaximumFractionDigits(2);
		String strSize = null;
		float fSize = 0;
		float fRetSize = 0;
		if (size > GB) {
			fSize = size;
			fRetSize = fSize / GB;
			strSize = df.format(fRetSize) + "GB";
		} else if (size > MB) {
			fSize = size;
			fRetSize = fSize / MB;
			strSize = df.format(fRetSize) + "MB";
		} else if (size > KB) {
			fSize = size;
			fRetSize = fSize / KB;
			strSize = df.format(fRetSize) + "KB";
		} else {
			strSize = size + "B";
		}
		return strSize;
	}
	public static long formatDataSizeInKB(long size) {
		long retSize = 0;
		if (size > KB) {
			retSize = size / KB;
		} else {
			retSize = 1;
		}
		return retSize;
	}
	public static String readFile(String path) {
		StringBuilder sb = new StringBuilder("");
		String readString = "";
		char[] readChar = new char[1024];
		int len = readChar.length;
		File file = new File(path);
		if (file.exists()) {
			try {
				FileReader in = new FileReader(file);
				len = in.read(readChar);
				while (len > 0) {
					Log.d(TAG, "readFile, len = " + len);
					readString = new String(readChar, 0, len);
					sb.append(readString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		readString = sb.toString();
		Log.d(TAG, "readFile, readString len = " + readString.length());
		return readString;
	}
	public static int getBmSampleSizeForPhotoCell(long sizeInKB) {
		int sampleSize = 8;
		if (sizeInKB < 512) {
			sampleSize = 4;
		} else if (sizeInKB < 1024) {
			sampleSize = 8;
		} else if (sizeInKB < 2048) {
			sampleSize = 16;
		} else if (sizeInKB < 4096) {
			sampleSize = 24;
		} else {
			sampleSize = 32;
		}
		return sampleSize;
	}
	public static int getBmSampleSizeForCameraCell(long sizeInKB) {
		int sampleSize = 8;
		if (sizeInKB < 512) {
			sampleSize = 4;
		} else if (sizeInKB < 1024) {
			sampleSize = 6;
		} else if (sizeInKB < 2048) {
			sampleSize = 8;
		} else if (sizeInKB < 4096) {
			sampleSize = 16;
		} else {
			sampleSize = 24;
		}
		return sampleSize;
	}
	public static int getBmSampleSizeForGridCell(int sizeInKB) {
		int sampleSize = 8;
		if (sizeInKB < 512) {
			sampleSize = 6;
		} else if (sizeInKB < 1024) {
			sampleSize = 8;
		} else if (sizeInKB < 2048) {
			sampleSize = 16;
		} else if (sizeInKB < 2048) {
			sampleSize = 24;
		} else {
			sampleSize = 32;
		}
		return sampleSize;
	}
	public static int getBmSampleSizeForBigImage(int sizeInKB) {
		int sampleSize = 4;
		if (sizeInKB < 1024) {
			sampleSize = 2;
		} else if (sizeInKB < 2048) {
			sampleSize = 4;
		} else if (sizeInKB < 3072) {
			sampleSize = 6;
		} else if (sizeInKB < 4096) {
			sampleSize = 8;
		} else {
			sampleSize = 12;
		}
		return sampleSize;
	}
	public static Bitmap getBitmapFormFile(String imgPath, int sampleSize) {
		InputStream iStream = null;
		Bitmap bmTemp = null;
		try {
			iStream = new FileInputStream(new File(imgPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// private Bitmap decodeThumbBitmapForFile(String path, int
			// viewWidth, int viewHeight) {
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// // 设置为true,表示解析Bitmap对象，该对象不占内存
			// options.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(path, options);
			// // 设置缩放比例
			// options.inSampleSize = computeScale(options, viewWidth,
			// viewHeight);
			// // 设置为false,解析Bitmap对象加入到内存中
			// options.inJustDecodeBounds = false;
			// return BitmapFactory.decodeFile(path, options);
			// }
			BitmapFactory.Options opt = new BitmapFactory.Options();
			// 设置为true,表示解析Bitmap对象，该对象不占内存
			// opt.inJustDecodeBounds = true;
			opt.inSampleSize = sampleSize;
			// 设置为false,解析Bitmap对象加入到内存中
			opt.inJustDecodeBounds = false;
			bmTemp = BitmapFactory.decodeStream(iStream, null, opt);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			Log.d(TAG, "getBitmapFormFile, OutOfMemoryError");
		}
		return bmTemp;
	}
	public static Bitmap getBitmapFormFile(String imgPath, int viewWidth,
			int viewHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgPath, options);
		// 设置缩放比例
		options.inSampleSize = calculateInSampleSize(options, viewWidth,
				viewHeight);
		// 设置为false,解析Bitmap对象加入到内存中
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imgPath, options);
	}
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
	public static int DipToPx(int dipIn, Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float densityScale = metrics.density;
		Log.d("Utils", "DipToPx, densityScale = " + densityScale);
		float px1 = dipIn * densityScale;
		return Math.round(px1);
	}
	public static int getAndroidSDKVersion() {
		int version = 8;
		// version = Integer.valueOf(android.os.Build.VERSION.SDK);
		version = android.os.Build.VERSION.SDK_INT;
		return version;
	}
	public static void saveToFile(String str, String sdcardPath) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory(), sdcardPath));
			os.write(str.getBytes());
			os.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static File getPictureFolder(Context context) {
		File dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		return dir;
	}
	public static File getCameraFolder(Context context) {
		return new File(getPictureFolder(context), "Camera");
	}
}
