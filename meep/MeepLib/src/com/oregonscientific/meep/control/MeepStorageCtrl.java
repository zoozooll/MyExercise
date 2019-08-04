package com.oregonscientific.meep.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.global.object.MoveFileToDataDirectory;
import com.oregonscientific.meep.opengl.MediaManager;

public class MeepStorageCtrl {

	private static final String FOLDER_HOME = "/data/home/";
	private static final String FOLDER_MOVIE = "/data/home/movie/";
	private static final String FOLDER_MUSIC = "/data/home/music/";
	private static final String FOLDER_EBOOK = "/data/home/ebook/";
	private static final String FOLDER_PHOTO = "/data/home/photo/";
	private static final String FOLDER_APP = "/data/home/app/";
	private static final String FOLDER_GAME = "/data/home/game/";
	private static final String FOLDER_FRIEND = "/data/home/friend/";

	private static final String SUB_FOLDER_ICON = "icon/";
	private static final String SUB_FOLDER_LARGE_ICON = "icon_l/";
	private static final String SUB_FOLDER_LARGE_DIM_ICON = "icon_ld/";
	private static final String SUB_FOLDER_LARGE_MIRROR_ICON = "icon_lm/";
	private static final String SUB_FOLDER_SMALL_ICON = "icon_s/";
	private static final String SUB_FOLDER_DATA = "data/";
	private static final String SUB_FOLDER_DEFAULT = "default/";
	private static final String SUB_FOLDER_TEMP = "temp/";
	private static final String SUB_FOLDER_UNZIP = "unzip/";

	private static final String PUBLIC_FOLDER_MOVIE = "/mnt/sdcard/home/movie/";
	private static final String PUBLIC_FOLDER_MUSIC = "/mnt/sdcard/home/music/";
	private static final String PUBLIC_FOLDER_EBOOK = "/mnt/sdcard/home/ebook/";
	private static final String PUBLIC_FOLDER_PHOTO = "/mnt/sdcard/home/photo/";
	private static final String PUBLIC_FOLDER_APP = "/mnt/sdcard/home/app/";
	private static final String PUBLIC_FOLDER_GAME = "/mnt/sdcard/home/game/";
	private static final String PUBLIC_FOLDER_FRIEND = "/mnt/sdcard/home/friend/";

	// for meep2 3.0
	public static final String PUBLIC_FOLDER_HOME = Environment.getExternalStorageDirectory()
			+ "/data/home/";

	CountDownTimer timer = new CountDownTimer(10000, 10000) {

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			MoveFileToDataDirectory dir = new MoveFileToDataDirectory();
			timer.cancel();
		}
	};

	public MeepStorageCtrl() {
		createDefaultFolders();
		timer.start();
	}

	private void createDefaultFolders() {
		Log.d("MeepStorageCtrl", "create default folder");

		createFolder(FOLDER_HOME);

		createFolder(FOLDER_MOVIE);
		createDefaultSubFolders(FOLDER_MOVIE);
		createDefaultSubFolders(PUBLIC_FOLDER_MOVIE);
		createFolder(FOLDER_MUSIC);
		createDefaultSubFolders(FOLDER_MUSIC);
		createDefaultSubFolders(PUBLIC_FOLDER_MUSIC);
		createFolder(FOLDER_EBOOK);
		createDefaultSubFolders(FOLDER_EBOOK);
		createDefaultSubFolders(PUBLIC_FOLDER_EBOOK);
		createFolder(FOLDER_APP);
		createDefaultSubFolders(FOLDER_APP);
		createDefaultSubFolders(PUBLIC_FOLDER_APP);
		createFolder(FOLDER_FRIEND);
		createDefaultSubFolders(FOLDER_FRIEND);
		createDefaultSubFolders(PUBLIC_FOLDER_FRIEND);
		createFolder(FOLDER_GAME);
		createDefaultSubFolders(FOLDER_GAME);
		createDefaultSubFolders(PUBLIC_FOLDER_GAME);
		createFolder(FOLDER_PHOTO);
		createDefaultSubFolders(FOLDER_PHOTO);
		createDefaultSubFolders(PUBLIC_FOLDER_PHOTO);
		Log.d("MeepStorageCtrl", "end create default folder");
	}

	private void createDefaultSubFolders(String path) {
		createFolder(path + SUB_FOLDER_DATA);
		createFolder(path + SUB_FOLDER_ICON);
		createFolder(path + SUB_FOLDER_LARGE_DIM_ICON);
		createFolder(path + SUB_FOLDER_LARGE_ICON);
		createFolder(path + SUB_FOLDER_LARGE_MIRROR_ICON);
		createFolder(path + SUB_FOLDER_SMALL_ICON);
		createFolder(path + SUB_FOLDER_DEFAULT);
		createFolder(path + SUB_FOLDER_TEMP);
		createFolder(path + SUB_FOLDER_UNZIP);
	}

	private void createFolder(String path) {
		Log.d("MeepStorageCtrl", "create folder:" + path);
		File folder = new File(path);
		if (!folder.exists()) {
			if (folder.mkdir()) {
				changeFilePermission(folder);
			} else {
				Log.d("MeepStorageCtrl", "cannot create folder:" + path);
			}
		} else if (!folder.canWrite()) {
			changeFilePermission(folder);
		}
	}

	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

	public static int changeFilePermission(File file) {
		int rtn = -1;
		for (int i = 0; i < 10 && rtn != 0; i++) {
			try {
				rtn = changeFilePermission2(file);
				if (rtn == 0) {
					break;
				}
				Thread.sleep(1000 + (i / 3));
			} catch (Exception e) {
			}
		}
		return rtn;
	}

	private static int changeFilePermission2(File file) {
		int errorCode = -1;
		try {
			errorCode = (Integer) Class.forName("android.os.FileUtils").getMethod("setPermissions", String.class, int.class, int.class, int.class).invoke(null, file.getAbsolutePath(), 0777, -1, -1);
			Log.d("changeFilePermission2", "changing permission for file '"
					+ file.getAbsolutePath() + "' [error code = " + errorCode
					+ "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorCode;
	}

	public static int[] getFilePermission(File file) {
		int[] modeUidAndGid = new int[3];
		try {
			Class.forName("android.os.FileUtils").getMethod("getPermissions", String.class, int[].class).invoke(null, file.getAbsolutePath(), modeUidAndGid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modeUidAndGid;
	}

	public static void changeFolderPermission(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] fileList = file.listFiles();
				for (File f : fileList) {
					changeFolderPermission(f);
				}
			}
			changeFilePermission(file);
			Log.d("permission", "change file permission:"
					+ file.getAbsolutePath());
		} else {
			Log.d("permission", "change file permission: file not exist");
		}
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static String replaceSpaceWithUnderscore(String str) {
		return rewordPath(str);
	}

	private static String rewordPath(String path) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i) == ' ' || path.charAt(i) == '\"'
					|| path.charAt(i) == ':' || path.charAt(i) == '\\'
					|| path.charAt(i) == '<' || path.charAt(i) == '>'
					|| path.charAt(i) == '?' || path.charAt(i) == '|'
					|| path.charAt(i) == '*') {
				// sb.append('_');
			} else {
				sb.append(path.charAt(i));
			}
		}
		return sb.toString();
	}

	public static String getFreeSpace(Context context) {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(context, availableBlocks * blockSize);
	}

	public static int getBitmapSize(Bitmap bmp) {
		return bmp.getRowBytes() * bmp.getHeight();
	}

	public static void saveImage(Bitmap bmp, String path) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bmp.compress(CompressFormat.PNG, 80, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.w("mediaManager", "saveImage to data/home fail:" + e.toString());
		}
	}

	public static String getIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_ICON;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_ICON;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_ICON;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_ICON;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_ICON;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_ICON;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getDataFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_DATA;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_DATA;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_DATA;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_DATA;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_DATA;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_DATA;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_DATA;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getDefaultIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_DEFAULT;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_DEFAULT;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_DEFAULT;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_DEFAULT;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_DEFAULT;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_DEFAULT;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_DEFAULT;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getLargeIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_LARGE_ICON;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_LARGE_ICON;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_LARGE_ICON;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_LARGE_ICON;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_LARGE_ICON;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_LARGE_ICON;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_LARGE_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getLargeDimIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getLargeMirrorIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getSmallIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_SMALL_ICON;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_SMALL_ICON;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_SMALL_ICON;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_SMALL_ICON;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_SMALL_ICON;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_SMALL_ICON;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_SMALL_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getTempFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = FOLDER_APP + SUB_FOLDER_TEMP;
			break;
		case Ebook:
			path = FOLDER_EBOOK + SUB_FOLDER_TEMP;
			break;
		case Game:
			path = FOLDER_GAME + SUB_FOLDER_TEMP;
			break;
		case Movie:
			path = FOLDER_MOVIE + SUB_FOLDER_TEMP;
			break;
		case Music:
			path = FOLDER_MUSIC + SUB_FOLDER_TEMP;
			break;
		case Photo:
			path = FOLDER_PHOTO + SUB_FOLDER_TEMP;
			break;
		case Communicator:
			path = FOLDER_FRIEND + SUB_FOLDER_TEMP;
			break;
		default:
			break;
		}
		return path;
	}

	// *************************

	public static String getPublicIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_ICON;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_ICON;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_ICON;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_ICON;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_ICON;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_ICON;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getPublicDataFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_DATA;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_DATA;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_DATA;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_DATA;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_DATA;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_DATA;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_DATA;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getPublicDefaultFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_DEFAULT;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_DEFAULT;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_DEFAULT;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_DEFAULT;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_DEFAULT;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_DEFAULT;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_DEFAULT;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getPublicLargeIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_LARGE_ICON;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_LARGE_ICON;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_LARGE_ICON;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_LARGE_ICON;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_LARGE_ICON;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_LARGE_ICON;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_LARGE_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getPublicLargeDimIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_LARGE_DIM_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getPublicLargeMirrorIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_LARGE_MIRROR_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	public static String getPublicSmallIconFolderPath(AppType appType) {
		String path = null;
		switch (appType) {
		case App:
			path = PUBLIC_FOLDER_APP + SUB_FOLDER_SMALL_ICON;
			break;
		case Ebook:
			path = PUBLIC_FOLDER_EBOOK + SUB_FOLDER_SMALL_ICON;
			break;
		case Game:
			path = PUBLIC_FOLDER_GAME + SUB_FOLDER_SMALL_ICON;
			break;
		case Movie:
			path = PUBLIC_FOLDER_MOVIE + SUB_FOLDER_SMALL_ICON;
			break;
		case Music:
			path = PUBLIC_FOLDER_MUSIC + SUB_FOLDER_SMALL_ICON;
			break;
		case Photo:
			path = PUBLIC_FOLDER_PHOTO + SUB_FOLDER_SMALL_ICON;
			break;
		case Communicator:
			path = PUBLIC_FOLDER_FRIEND + SUB_FOLDER_SMALL_ICON;
			break;
		default:
			break;
		}
		return path;
	}

	/**
	 * get mirrored preview icon from /data/home/[app]/icon_lm, if file not
	 * exit, convert icon from /data/home/[app]/icon_l
	 * 
	 * @param fileName
	 *            eg.image.png
	 * @param absolutePath
	 *            eg. /mnt/estsd/image.png
	 */
	public static Bitmap getPrivateMirrorIcon(String fileName,
			String absolutePath, AppType appType, Bitmap bmpBg, Bitmap bmpTop) {
		// Log.d("loadBtnImage", "getPrivateMirrorIcon");
		File file = new File(getLargeMirrorIconFolderPath(appType) + fileName);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			Bitmap privateLargeIcon = getPrivateLargeIcon(fileName, absolutePath, appType, bmpBg, bmpTop);
			if (privateLargeIcon != null) {
				Bitmap mirrorImg = MediaManager.getReflectionImage(privateLargeIcon);
				MediaManager.saveImageToExternal(mirrorImg, file.getAbsolutePath());
				return mirrorImg;
			} else {
				Bitmap publicLargeIcon = getPublicLargeIcon(fileName, absolutePath, appType, bmpBg, bmpTop);
				if (publicLargeIcon != null) {
					Bitmap mirrorImg = MediaManager.getReflectionImage(publicLargeIcon);
					MediaManager.saveImageToExternal(mirrorImg, file.getAbsolutePath());
					return mirrorImg;
				} else {
					return null;
				}
			}
		}
	}

	public static Bitmap getPrivateLargeDimImage(String fileName,
			String absolutePath, AppType appType) {

		File file = new File(getLargeDimIconFolderPath(appType) + fileName);
		// Log.d("loadBtnImage", "getPrivateLargeDimImage :" +
		// file.getAbsolutePath());
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			Log.d("loadBtnImage", "getPrivateLargeDimImage null");
			return null;
		}
	}

	public static Bitmap getPrivateSmallIcon(String fileName,
			String absolutePath, AppType appType, Bitmap bmpBg, Bitmap bmpTop) {
		// Log.d("loadBtnImage", "getPrivateSmallIcon");
		// 2013-4-3 add (+".png") show default image
		File file = new File(getSmallIconFolderPath(appType) + fileName
				+ ".png");
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			Bitmap privateLargeIcon = getPrivateLargeIcon(fileName, absolutePath, appType, bmpBg, bmpTop);
			if (privateLargeIcon != null) {
				Bitmap smallImg = getPhotoListViewImage(privateLargeIcon);
				MediaManager.saveImageToExternal(smallImg, file.getAbsolutePath());
				return smallImg;
			} else {
				Bitmap publicLargeIcon = getPublicLargeIcon(fileName, absolutePath, appType, bmpBg, bmpTop);
				if (publicLargeIcon != null) {
					Bitmap smallImg = MediaManager.getReflectionImage(publicLargeIcon);
					MediaManager.saveImageToExternal(smallImg, file.getAbsolutePath());
					return smallImg;
				} else {
					return null;
				}
			}
		}
	}

	private static Bitmap getPhotoListViewImage(Bitmap originalBmap) {
		// Log.d("loadBtnImage", "getPhotoListViewImage");
		float scaleFactor = 80f / originalBmap.getHeight();
		Matrix m = new Matrix();
		m.postScale(scaleFactor, scaleFactor);
		return Bitmap.createBitmap(originalBmap, 0, 0, originalBmap.getWidth(), originalBmap.getHeight(), m, false);
	}

	/**
	 * get preview icon from /data/home/[app]/icon_l, if file not exit, convert
	 * icon from /data/home/[app]/icon
	 * 
	 * @param fileName
	 *            eg.image.png
	 * @param absolutePath
	 *            eg. /mnt/estsd/image.png
	 */
	private static Bitmap getPrivateLargeIcon(String fileName,
			String absolutePath, AppType appType, Bitmap bmpBg, Bitmap bmpTop) {
		// Log.d("loadBtnImage", "getPrivateLargeIcon");
		File file = new File(getLargeIconFolderPath(appType) + fileName);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			Bitmap privateIcon = getPrivateIcon(fileName, absolutePath, appType);
			Bitmap convertedIcon = null;
			if (privateIcon != null) {
				switch (appType) {
				case Movie:
					convertedIcon = MediaManager.getMovieLargeIcon(privateIcon, bmpBg, bmpTop);
					break;
				case Music:
					convertedIcon = MediaManager.getMusicLargeIcon(privateIcon, bmpBg, bmpTop);
					break;
				case Photo:
					convertedIcon = MediaManager.getPhotoLargeIcon(privateIcon, bmpBg, bmpTop, 247, 200, 28, 28);
					break;
				default:
					convertedIcon = MediaManager.bitmapResizeToLargePreviewIcon(privateIcon);
					break;
				}
				MediaManager.saveImageToExternal(convertedIcon, file.getAbsolutePath());
				return convertedIcon;
			} else {
				if (appType == AppType.Photo) {
					Bitmap dataIcon = getPhotoDataIcon(absolutePath);
					convertedIcon = MediaManager.getPhotoLargeIcon(dataIcon, bmpBg, bmpTop, 247, 200, 28, 28);
					MediaManager.saveImageToExternal(convertedIcon, file.getAbsolutePath());
					return convertedIcon;
				} else {
					return null;
				}
			}
		}
	}

	/**
	 * get icon from /data/home/[app]/icon, if file not exit, return null
	 * 
	 * @param fileName
	 *            eg.image.png
	 * @param absolutePath
	 *            eg. /mnt/estsd/image.png
	 */
	private static Bitmap getPrivateIcon(String fileName, String absolutePath,
			AppType appType) {
		// Log.d("loadBtnImage", "getPrivateIcon");
		File file = new File(getIconFolderPath(appType) + fileName);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			return null;
		}
	}

	/**
	 * get preview icon from /mnt/sdcard/home/[app]/icon_l, if file not exit,
	 * convert icon from /mnt/sdcard/home/[app]/icon
	 * 
	 * @param fileName
	 *            eg.image.png
	 * @param absolutePath
	 *            eg. /mnt/estsd/image.png
	 */
	private static Bitmap getPublicLargeIcon(String fileName,
			String absolutePath, AppType appType, Bitmap bmpBg, Bitmap bmpTop) {
		// Log.d("loadBtnImage", "getPublicLargeIcon");
		File file = new File(getPublicLargeIconFolderPath(appType) + fileName);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			Bitmap icon = getPublicIcon(fileName, absolutePath, appType);
			Bitmap convertedIcon = null;
			if (icon != null) {
				switch (appType) {
				case Movie:
					convertedIcon = MediaManager.getMovieLargeIcon(convertedIcon, bmpBg, bmpTop);
					break;
				case Music:
					convertedIcon = MediaManager.getMusicLargeIcon(convertedIcon, bmpBg, bmpTop);
					break;
				case Photo:
					convertedIcon = MediaManager.getPhotoLargeIcon(convertedIcon, bmpBg, bmpTop, 247, 200, 28, 28);
					break;
				default:
					convertedIcon = MediaManager.bitmapResizeToLargePreviewIcon(convertedIcon);
					break;
				}
				return convertedIcon;
			} else {
				if (appType == AppType.Photo) {
					Bitmap dataIcon = getPhotoDataIcon(absolutePath);
					convertedIcon = MediaManager.getPhotoLargeIcon(dataIcon, bmpBg, bmpTop, 247, 200, 28, 28);
					MediaManager.saveImageToExternal(convertedIcon, file.getAbsolutePath());
					return convertedIcon;
				} else {
					return null;
				}
			}
		}
	}

	/**
	 * get icon from /mnt/sdcard/home/[app]/icon, if file not exit, return null
	 * 
	 * @param fileName
	 *            eg.image.png
	 * @param absolutePath
	 *            eg. /mnt/estsd/image.png
	 */
	private static Bitmap getPublicIcon(String fileName, String absolutePath,
			AppType appType) {
		// Log.d("loadBtnImage", "getPublicIcon");
		File file = new File(getPublicIconFolderPath(appType) + fileName);
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		} else {
			return null;
		}
	}

	/**
	 * get image of photo by path, if file not exit, return null
	 * 
	 * @param path
	 *            eg. /mnt/extsd/image.png
	 */
	private static Bitmap getPhotoDataIcon(String path) {
		// Log.d("loadBtnImage", "getPhotoDataIcon");
		File file = new File(path);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			options.inSampleSize = calculateInSampleSize(options, 247, 200);
			options.inJustDecodeBounds = false;

			Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			return image;
		} else {
			return null;
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		// * 2 to double the inSampleSize, to ensure that big photos won't crash
		// our app
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round(((float) height / (float) reqHeight) * 2);
			} else {
				inSampleSize = Math.round(((float) width / (float) reqWidth) * 2);
			}
		}
		return inSampleSize;
	}

	/**
	 * 
	 * @param bitmap2
	 *            the source Bitmap
	 * @param dstw
	 * @param dsth
	 * @return the new bitmap the right size.
	 * @author winderhao
	 */
	public static Bitmap resizeImage(Bitmap bitmap2, int dstw, int dsth) {
		float scale;
		Bitmap resizedBitmap = null;
		int srcw = bitmap2.getWidth();
		int srch = bitmap2.getHeight();
		Matrix matrix = new Matrix();
		// System.out.println("srcw = " + srcw + "---" + "srch = " + srch);
		// System.out.println("dstw = " + dstw + "---" + "dsth = " + dsth);
		if ((dstw < srcw) && (dsth < srch)) {
			if (((float) dstw / (float) srcw) > ((float) dsth / (float) srch)) {

				// System.out.println((float) dstw / (float) srcw);
				// System.out.println((float) dsth / (float) srch);

				// Log.v("X1", "x1");

				scale = ((float) dstw) / srcw;
				matrix.postScale(scale, scale);
				int mHeight = (srcw * dsth) / dstw;
				int y = (srch - mHeight) / 2;

				// System.out.println(dstw / srcw + "xxxxxxxxxxx");
				resizedBitmap = Bitmap.createBitmap(bitmap2, 0, y, srcw, (srcw * dsth)
						/ dstw, matrix, true);
				int afterh = resizedBitmap.getHeight();
				int afterw = resizedBitmap.getWidth();
				// System.out.println("afterw = " + afterw + " * " + "afterh = "
				// + afterh);

			}
			if (((float) dstw / (float) srcw) <= ((float) dsth / (float) srch)) {
				// System.out.println("222222222222");
				scale = ((float) dsth) / srch;// dsth 136
				matrix.postScale(scale, scale);
				int mWidth = (dstw * srch) / dsth;
				int x = (srcw - mWidth) / 2;
				resizedBitmap = Bitmap.createBitmap(bitmap2, x, 0, (dstw * srch)
						/ dsth, srch, matrix, true);
				// int afterh = resizedBitmap.getHeight();
				// int afterw = resizedBitmap.getWidth();
				// System.out.println("afterw = " + afterw + " * " + "afterh = "
				// + afterh);
			}
		}
		if ((dstw > srcw) || (dsth > srch)) {

			if (((float) dstw / (float) srcw) > ((float) dsth / (float) srch)) {
				// System.out.println("333333333");

				// System.out.println((float) dstw / (float) srcw);
				// System.out.println((float) dsth / (float) srch);

				// Log.v("X1", "x1");

				scale = ((float) dstw) / srcw;
				matrix.postScale(scale, scale);
				int mHeight = (srcw * dsth) / dstw;
				int y = (srch - mHeight) / 2;
				// System.out.println((dstw * srch) / dsth + "xxxxxxxxxxx");
				// System.out.println((srcw * dsth) / dstw + "xxxxxxxxxxx");
				resizedBitmap = Bitmap.createBitmap(bitmap2, 0, y, srcw, (srcw * dsth)
						/ dstw, matrix, true);
				// int afterh = resizedBitmap.getHeight();
				// int afterw = resizedBitmap.getWidth();
				// System.out.println("afterw = " + afterw + " * " +
				// "afterh = "+ afterh);

			}
			if (((float) dstw / (float) srcw) <= ((float) dsth / (float) srch)) {
				// System.out.println("44444444444");
				scale = ((float) dsth) / srch;// dsth 136
				matrix.postScale(scale, scale);
				int mWidth = (dstw * srch) / dsth;
				int x = (srcw - mWidth) / 2;
				// System.out.println((dstw * srch) / dsth + "xxxxxxxxxxx");
				// System.out.println((srcw * dsth) / dstw + "xxxxxxxxxxx");
				resizedBitmap = Bitmap.createBitmap(bitmap2, x, 0, (dstw * srch)
						/ dsth, srch, matrix, true);
				// int afterh = resizedBitmap.getHeight();
				// int afterw = resizedBitmap.getWidth();
				// System.out.println("afterw = " + afterw + " * " + "afterh = "
				// + afterh);
			}
		}
		// bitmap2.recycle();
		return resizedBitmap;
	}

	/**
	 * make the roundedCorner Bitmap .the four corners was rounded
	 * 
	 * @param bitmap
	 * @return
	 * @author zoya
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 29;

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap == null || bitmap.isRecycled()) {
			return null;
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 5;

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * draw the bitmap the round corner bitmap
	 * 
	 * @author aaronli at Jun27 2013
	 * @param input
	 *            the source
	 * @param pixels
	 *            the angle of pixels
	 * @param w
	 *            the dist bitmap's width
	 * @param h
	 *            the dist bitmap's width
	 * @param squareTL
	 * @param squareTR
	 * @param squareBL
	 * @param squareBR
	 * @return the bitmap with round corner
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap input, int pixels,
			int w, int h, boolean squareTL, boolean squareTR, boolean squareBL,
			boolean squareBR) {
		// int time1 =(int) System.currentTimeMillis();

		/*
		 * Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.app).copy(Bitmap.Config.ARGB_8888, true); Bitmap bitmap2 =
		 * ((BitmapDrawable)
		 * getResources().getDrawable(R.drawable.app_icon_frame)).getBitmap();
		 */
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		// final float densityMultiplier =
		// context.getResources().getDisplayMetrics().density;

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		// make sure that our rounded corner is scaled appropriately
		// final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, pixels, pixels, paint);

		// draw rectangles over the corners we want to be square
		if (squareTL) {
			canvas.drawRect(0, 0, w / 2, h / 2, paint);
		}
		if (squareTR) {
			canvas.drawRect(w / 2, 0, w, h / 2, paint);
		}
		if (squareBL) {
			canvas.drawRect(0, h / 2, w / 2, h, paint);
		}
		if (squareBR) {
			canvas.drawRect(w / 2, h / 2, w, h, paint);
		}

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(input, 0, 0, paint);
		// int time2 =(int) System.currentTimeMillis();
		// System.out.println(time2-time1+"耗时事件毫秒");
		return output;
	}
	
	
	/**
	 * The character checking adding the European languages.<br/>
	 * The excepted characters are that regex
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isRegexBadCharctors(String str) {
		String regex = "^.*[\\u0000-\\u001f\\u0021-\\u002f\\u003a-\\u003c\\u003f-\\u0040\\u005b-\\u005e\\u0060\\u007b-\\u0080\\u0081-\\u0089\\u008b-\\u008d\\u008f-\\u0099\\u009b-\\u009d\\u00a0\\u00a2-\\u00b4\\u00b6-\\u00bf\\u00d7\\u00f7\\u00fe-\\u00ff].*$";
		if (str != null && !"".equals(str.trim())) {
			return str.matches(regex);
		}
		return false;
	}
}
