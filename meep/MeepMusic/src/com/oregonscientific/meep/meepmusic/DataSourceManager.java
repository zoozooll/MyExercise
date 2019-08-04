package com.oregonscientific.meep.meepmusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.widget.MediaController;

import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.opengl.OSButton;

/**
 * 
 * @author aaronli
 *
 */
public class DataSourceManager {
	
	private static List<OsListViewItem> mListViewItemList ;
	private static String user;
	private static final String TAG = "DataSourceManager";
	private static final String BASIC_STORAGE_PATH = MEEPEnvironment.getMediaStorageDirectory().getPath() + File.separatorChar;
	private static final String PATH_MUSIC_EXTSD_DIR1 = "/storage/external_storage/";
	private static final String PATH_MUSIC_EXTSD_DIR = "/mnt/extsd/";
	private static final String PATH_MUSIC_DATA_DIR = BASIC_STORAGE_PATH + "music/data/";
	private static final String PATH_MUSIC_HOME_DIR = "/mnt/sdcard/home/music/data/";
	private static final String PATH_MUSIC_BT_DIR = "/sdcard/bluetooth/";
	
	//music
	//private static final String BASIC_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/home/";
	private static final String MUSIC_PATH_LARGE_ICON_DIR =  BASIC_STORAGE_PATH + "music/icon_l/";
	private static final String MUSIC_PATH_LARGE_MIRROR_ICON_DIR =  BASIC_STORAGE_PATH + "music/icon_lm/";
	private static final String MUSIC_PATH_LARGE_DIM_ICON_DIR =  BASIC_STORAGE_PATH + "music/icon_ld/";
	private static final String MUSIC_PATH_SMALL_ICON_DIR =  BASIC_STORAGE_PATH + "music/icon_s/";
	private static final String MUSIC_PATH_DATA_ICON_DIR =  BASIC_STORAGE_PATH + "music/icon/";

	//2013-7-12 -Amy- add music formate
	public static final Map<String, String> SUPPORT_MUSIC_FORMAT = new HashMap<String, String>();
	public static final String MEDIA_SUPPORTED = "support";
	static {
		SUPPORT_MUSIC_FORMAT.put("mp3", MEDIA_SUPPORTED);
		SUPPORT_MUSIC_FORMAT.put("wav", MEDIA_SUPPORTED);
		SUPPORT_MUSIC_FORMAT.put("m4a", MEDIA_SUPPORTED);
		SUPPORT_MUSIC_FORMAT.put("ogg", MEDIA_SUPPORTED);
		SUPPORT_MUSIC_FORMAT.put("ape", MEDIA_SUPPORTED);
		SUPPORT_MUSIC_FORMAT.put("flac", MEDIA_SUPPORTED);
	}
	
	private static String sAlbumPath;
	
	public DataSourceManager(Context context) {
		super();
	}

	/**
	 * scan the all format folder with images 
	 * @return the file List contain all image files.
	 */
	public static List<File> scanDirectList() {
		List<File> mFileList = new ArrayList<File>();
		List<File> cdList = new ArrayList<File>();

		//traverseDir(Environment.getExternalStorageDirectory(), mFileList);
		traverseDir(new File(PATH_MUSIC_DATA_DIR), mFileList);
        traverseDir(new File(PATH_MUSIC_EXTSD_DIR), mFileList);
		//traverseDir(new File(PATH_MUSIC_HOME_DIR), mFileList);
        traverseDir(new File(PATH_MUSIC_BT_DIR), mFileList);
		traverseDir(new File(PATH_MUSIC_EXTSD_DIR1), mFileList);
		//Log.d(TAG, "mFileList " + Arrays.toString(mFileList.toArray()));
		for (int i = 0; i < mFileList.size(); i++) {
			boolean hasRecord = false;
			File thisParentFile = mFileList.get(i).getParentFile();
			//Log.i("music....", thisParentFile.toString());
			// Check
			for (int j = 0; j < cdList.size(); j++) {
				if (thisParentFile.equals(cdList.get(j))) {
					hasRecord = true;
					break;
				}
			}
			// Record not found, add record
			if (!hasRecord) {
				cdList.add(thisParentFile);
			} else {
			}
		}
		return cdList;
	}
	
	/**
	 * make the {@link OsListViewItem} List with file List
	 * @param fileList The data source,make from
	 * @param items The items which to be add.if null should catch exception
	 */
	public static void getDataSource(List<File> fileList, List<OsListViewItem> items) {

		//Log.d(TAG, "cdList " + Arrays.toString(fileList.toArray()));
		//Log.d(TAG, "cdList " + Arrays.toString(fileList.toArray()));

		for (File thisFile : fileList) {
			

			String filename = thisFile.getName();

			//2013-3-18 -Amy- Rename after refres
			//String photoName = filename.substring(0,filename.lastIndexOf("."));
			
			OsListViewItem item = new OsListViewItem();
			//item.setImage(bmap);
			item.setName(filename);

			item.setPath(thisFile.getAbsolutePath()+File.separator);
			items.add(item);
		}
	}
	//winder
	public static List<OsListViewItem>	getAllItems(boolean  refresh)
	{	
		if (refresh || mListViewItemList == null || mListViewItemList.size() == 0){
		if (mListViewItemList == null) {
			mListViewItemList = new Vector<OsListViewItem>();
		} else 
			{
			mListViewItemList.clear();
			}
		synchronized (mListViewItemList) {
			List<File> fileList = scanDirectList();
			getDataSource(fileList, mListViewItemList);
		}
		}
		return mListViewItemList;
	}
	
	public static String[] getSongFileList(String dataFolderPath) {
		List<String> songFileNameList = null;
			
		File dataFolder = new File(dataFolderPath);
		//Log.d("musicplayer", "dataFolder:" + dataFolder);
		
		if(dataFolder.exists() && dataFolder.isDirectory()){
			File[] tempSongList = dataFolder.listFiles();
			songFileNameList = new ArrayList<String>();
			for (File tempSongListItem : tempSongList) {
				//2013-7-19 -Amy- use map
				//String songPath = tempSongList[i].getPath();
				String extension = getExtension(tempSongListItem.getName()).toLowerCase();
				if (DataSourceManager.SUPPORT_MUSIC_FORMAT.containsKey(extension) && !tempSongListItem.getName().startsWith(".")) {
					//boolean fileType = getFileHeader(tempSongListItem.getPath());
					//if(fileType){
					//Log.d("musicplayer", "tempSongListItem:" + tempSongListItem);
						
						songFileNameList.add(tempSongListItem.getPath());
					//}
					
				}
			}
		}
		if (songFileNameList != null) {
			return songFileNameList.toArray(new String[songFileNameList.size()]);
		} else {
			return null;
		}
		
	}
		
	//2013-7-2 - Zoya - fixed music player will be stopped after play an invalid MP3 format file.
	public static boolean getFileHeader(String filePath) {
		boolean type = false;
		FileInputStream is = null;
		StringBuilder value = null;
		StringBuilder sum = new StringBuilder() ;
		try {
			is = new FileInputStream(filePath);
			int c = 0; 
			boolean startBit = false;
			while(c!=-1){
				byte[] b = new byte[3];
				c = is.read(b, 0, b.length);
				value = bytesToHexString(b);
				if (value.toString().contains("FFFB")){
					startBit = true;
				}
				if(startBit) {
					sum.append(value);
				}
				if(sum.toString().contains("FFFB")){
					type=true;
					break;
				}
			}
		} catch (Exception e) {
			
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}

			}

		}
		return type;
	}

	
	public static boolean deleteDataItems(OsListViewItem... items) {
		for (OsListViewItem o : items) {
			mListViewItemList.remove(o);
		}
		return true;
	}
	
	public static void renameFile(String renameFileName, OsListViewItem item) {

		File from = new File(item.getPath());
		File to = new File(from.getParent(), renameFileName);
		boolean isSuccess = from.renameTo(to);
		//Log.d(TAG, "rename isSuccess: "+ isSuccess);
		if (!isSuccess) {
			return;
		}
		File fromLargeIcon = new File(MUSIC_PATH_LARGE_ICON_DIR + item.getName() + ".png");
		File toLargeIcon = new File(MUSIC_PATH_LARGE_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 2-- "+to);
		fromLargeIcon.renameTo(toLargeIcon);

		File fromDimIcon = new File(MUSIC_PATH_LARGE_DIM_ICON_DIR + item.getName() + ".png");
		File toDimIcon = new File(MUSIC_PATH_LARGE_DIM_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 3-- "+to);
		fromDimIcon.renameTo(toDimIcon);

		File fromSmallIcon = new File(MUSIC_PATH_SMALL_ICON_DIR + item.getName() + ".png");
		File toSmallIcon = new File(MUSIC_PATH_SMALL_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 4-- "+to);
		fromSmallIcon.renameTo(toSmallIcon);

		// 2013-3-26 -Amy- fix issue # 3163��The default album cover will
		// disappear after rename
		File fromIcon = new File(MUSIC_PATH_DATA_ICON_DIR + item.getName() + ".png");
		File toIcon = new File(MUSIC_PATH_DATA_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 5-- "+to);
		fromIcon.renameTo(toIcon);

		File fromLMIcon = new File(MUSIC_PATH_LARGE_MIRROR_ICON_DIR + item.getName() + ".png");
		File toLMIcon = new File(MUSIC_PATH_LARGE_MIRROR_ICON_DIR + renameFileName + ".png");
		// Log.e("cdf","to 6-- "+to);
		fromLMIcon.renameTo(toLMIcon);
		item.setName(renameFileName);
		item.setPath(to.getPath() + File.separatorChar);
	}
	
	public static boolean fileNameisAlreadyExecised(String name) {
		for (int i = 0; i < mListViewItemList.size(); i++)
		{
			// Log.d("All photo name", "All photo name: " +
			// mListViewItemList.get(i).getName());
			// Log.d("All photo name", "All photo name: " +
			// mListViewItemList.get(i).getName().substring(0,
			// mListViewItemList.get(i).getName().lastIndexOf(".")));
			if (mListViewItemList.get(i).getName().toLowerCase().equals(name.toLowerCase()))
			{
				return true;
				
			}
		}
		return false;
	}

	public static void loadingSnackTexture(OSButton button, Bitmap bmpPreviewBg) {
		// modified by aaronli at Mar21 2013 for loading image of music cd error
		String dirPathDimPath = MUSIC_PATH_LARGE_DIM_ICON_DIR;
		String name = button.getName();
		try {
			String cdDirPath = button.getContentPath().get(0) + File.separator;
			
			// if(cdDirPath.contains( BASIC_STORAGE_PATH + "music/")){
			// get icon image

			Bitmap mirrorImg = null;

			final String filePath = MUSIC_PATH_LARGE_MIRROR_ICON_DIR + name + Global.FILE_TYPE_PNG;
			File largeMirrorIcon = new File(filePath);
			if (largeMirrorIcon.exists()) { // icon from mirrorImage folder cache.
				mirrorImg = BitmapFactory.decodeFile(filePath); 
			}
				
			if (mirrorImg==null) {
				
				
				//File iconFile = new File(fileIconPath);
				//File sameFolderIcon = new File(sameFolerIconPath);
				Bitmap largeImage = null;
				largeImage = loadLargeMusicBitmap(name, cdDirPath, bmpPreviewBg);
				//final String  largeFilePath = MUSIC_PATH_LARGE_ICON_DIR + name + Global.FILE_TYPE_PNG;
				//largeImage = BitmapFactory.decodeFile(largeFilePath);
				if (largeImage != null){
					
					//mirrorImg = getMusicIconShaped(largeImage, bmpPreviewBg);
					MediaManager.saveImageToExternal(mirrorImg, filePath);
				}
				mirrorImg = largeImage;
			}

			//button.setTextureBmp(mirrorImg);
			//
			if (mirrorImg != null) {
				// the mp3 contains cover image
				
				button.setTextureBmp(mirrorImg);

				String dimImgPath = dirPathDimPath + name + Global.FILE_TYPE_PNG;
				File dimImgFile = new File(dimImgPath);
				Bitmap dimBmap = null;
				if (dimImgFile.exists()) {
					//delete the old image
					if (dimImgFile.lastModified() < largeMirrorIcon.lastModified()) {
						dimImgFile.delete();
						dimBmap = MediaManager.getDimImage2(mirrorImg);
						saveImage(dimBmap, dimImgPath);
					} else {
						dimBmap = BitmapFactory.decodeFile(dimImgPath);
					}
				} else {
					dimBmap = MediaManager.getDimImage2(mirrorImg);
					saveImage(dimBmap, dimImgPath);
				}
				if (dimBmap != null) {
					button.setDimTextureBmp(dimBmap);
					//Log.d("loadImage", "loadImage : name: " + name);
				} else {
					//button.setDimTextureBmp(mDefaultImageDim);
					//Log.d("loadImage", "loadEbookImage null ");
				}
			} else {
				//button.setTextureBmp(mDefaultImage.copy(Bitmap.Config.ARGB_8888, true));
				//button.setDimTextureBmp(mDefaultImageDim.copy(Bitmap.Config.ARGB_8888, true));
				//button.setTexureID(getDefaultImageTxtID());
				//button.setDimTexId(getDefaultImageDimTxtId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Bitmap loadFullListViewIcons(String cdDirPath, Bitmap bg) {
		// add by aaronli at Apr22 2013. Saved the cache files in music/icon_s/
		String cdName = getMusicCdName(cdDirPath);
		Bitmap icon = null;
		String iconSmallPath = MUSIC_PATH_SMALL_ICON_DIR + cdName + Global.FILE_TYPE_PNG;
		try {
			File iconSmallFile = new File(iconSmallPath);
			if (iconSmallFile.exists() && iconSmallFile.lastModified() < new File(cdDirPath).lastModified()) {
				icon = BitmapFactory.decodeFile(iconSmallPath); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (icon == null) {
			icon = loadLargeMusicBitmap(cdName, cdDirPath, bg);
			//String iconString = MUSIC_PATH_LARGE_ICON_DIR + cdName + Global.FILE_TYPE_PNG;
			//icon = BitmapFactory.decodeFile(iconString);
			//icon = getMusicIconFullList(icon, bg);
			MediaManager.saveImageToExternal(icon, iconSmallPath);
		}
		return icon;
	}
	
	/** 
	 * 
	 * @param cdDirPath
	 * @return
	 *//*
	public static Bitmap loadLargeMusicBitmap(String cdDirPath) {
		return loadLargeMusicBitmap(getMusicCdName(cdDirPath), cdDirPath);
	}*/

	/**
	 * 
	 * @param name
	 * @param cdDirPath
	 * @return
	 */
	public static Bitmap loadLargeMusicBitmap(String name, String cdDirPath, Bitmap bmpPreviewBg) {
		//Log.d(TAG, "loadLargeMusicBitmap "+name +" "+cdDirPath);
		Bitmap largeImage = null;
		final String  largeFilePath = MUSIC_PATH_LARGE_ICON_DIR + name + Global.FILE_TYPE_PNG;
		final String fileIconPath = MUSIC_PATH_DATA_ICON_DIR + name + Global.FILE_TYPE_PNG;
		File largeIcon = new File(largeFilePath);
		
		
		if (largeIcon.exists()) {	// large icons were from icon_l
			largeImage = BitmapFactory.decodeFile(largeFilePath);
		} 
		
		if (largeImage == null) {
			Bitmap icon = getBitmapFromIcon(name, fileIconPath, cdDirPath);
			if (icon != null) {
				largeImage = makeLargeMusicBitmap(icon, bmpPreviewBg);
			}
			if (largeImage != null) {
				MediaManager.saveImageToExternal(largeImage, largeFilePath);
			}
		}
		return largeImage;
	}
	
	private static Bitmap getBitmapFromIcon(String name, String fileIconPath, String cdDirPath) {
		Bitmap b = null;
		final String sameFolerIconPath = cdDirPath + name + Global.FILE_TYPE_PNG;
		final String sameFolerIconJEPG = cdDirPath + name + Global.FILE_TYPE_JPG;
		File cdDir = new File(cdDirPath);
		if (new File(fileIconPath).exists()){	// comment icons were from icon
			b = BitmapFactory.decodeFile(fileIconPath);
		} 
		if (b == null) {
			
			if(new File(sameFolerIconPath).exists()){	// cd folder icons were from cd folder
				b = BitmapFactory.decodeFile(sameFolerIconPath);
			} else if (new File(sameFolerIconJEPG).exists()) {
				b = BitmapFactory.decodeFile(sameFolerIconJEPG);
			} else {	// else, we need to decode from music files
				String[] songList = cdDir.list();
				//2013-7-17 -Amy- rename special characters,its crash and has NullPointerException
				if(songList != null){
					for (int j = 0; j < songList.length; j++) {
						final String lowercaseName = songList[j].toLowerCase();
						if (lowercaseName.contains(".mp3") || lowercaseName.contains(".wav") || lowercaseName.contains(".amr")) {
							b = getCdImage(cdDirPath + songList[j]);
							if (b != null) {
								//largeImage = convertSnackCdThumbnail(largeImage, name, bmpPreviewBg, bmpPrevieTop);
								/*if (largeImage != null) {
					MediaManager.saveImageToExternal(largeImage, largeFilePath);
					}*/
								//b = maskCdShape(largeImage, name);
								
								break;
							}
						}
					}
				}
				// largeImage is the cd image without shape.so we need to
				// fixed the shape.The shape images can only save in
				// DimPath(icon_lm)
			}
			
			if (b != null) {
				MediaManager.saveImageToExternal(b, fileIconPath);
			}
		}
		return b;
	}
	
	private static Bitmap makeLargeMusicBitmap (Bitmap bitmap2, Bitmap bgBitmap) {
		// 防止出现Immutable bitmap passed to Canvas constructor错误
		Bitmap bitmap1 = bgBitmap.copy(Bitmap.Config.ARGB_8888, true);
		//bitmap2 =((BitmapDrawable) getResources().getDrawable(R.drawable.po))).getBitmap();

		int w = 171;
		int h = 153;
		
		Bitmap newbitmap = MeepStorageCtrl.resizeImage(bitmap2, w, h);
		int nw = newbitmap.getWidth(); 
		int nh = newbitmap.getHeight();
		Canvas canvas = new Canvas(bitmap1);
		canvas.drawBitmap(newbitmap, 24, 10, null);

		return bitmap1;
	}
	
	/**
	 * 
	 * @param bmp the bmp need to be saved.
	 * @param path the abstract path need to saved to.
	 * @author aaronli at Apr22 2013
	 */
	public static void saveImage(Bitmap bmp, String path)
	{
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bmp.compress(CompressFormat.PNG, 80, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteItemFiles(OsListViewItem item) {
		deleteFolder(item.getPath());
		deleteItemFile(MUSIC_PATH_LARGE_ICON_DIR + item.getName() + Global.FILE_TYPE_PNG);
		deleteItemFile(MUSIC_PATH_LARGE_DIM_ICON_DIR + item.getName() + Global.FILE_TYPE_PNG);
		deleteItemFile(MUSIC_PATH_LARGE_ICON_DIR + item.getName() + Global.FILE_TYPE_PNG);
		deleteItemFile(MUSIC_PATH_LARGE_MIRROR_ICON_DIR + item.getName() + Global.FILE_TYPE_PNG);
		deleteItemFile(MUSIC_PATH_SMALL_ICON_DIR + item.getName() + Global.FILE_TYPE_PNG);
	}
	
	
	private static void deleteFolder(String path) {
		File dir = new File(path);
		if (dir.exists() && dir.isDirectory())
		{
			String[] fileList = dir.list();
			for (int i = 0; i < fileList.length; i++)
			{
				deleteItemFile(path + fileList[i]);

			}
			// dir.deleteOnExit();
			dir.delete();
		}
	}

	private static void deleteItemFile(String path) {
		// delete the real item
		File file = new File(path);
		try
		{
			if (file.exists())
			{
				file.delete();
			}
		} catch (Exception e)
		{
			Log.e("MusicFullListView", "delete file error : " + e.toString());
		}
	}
	
	public static String getExtension(File f) {
		return (f != null) ? getExtension(f.getName()) : "";
	}

	public static String getExtension(String filename) {
		return getExtension(filename, "");
	}

	public static String getExtension(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return defExt;
	}

	public static String trimExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}

	/**
	 * Get file name without the extension from file's abstract path<br> 
	 * @param path file's abstract path.<br>If the file's path is folder and end with {@link File.separatorChar},it should show wrong 
	 * @return 
	 */
	public static String getFileNameWithoutExten(String path) {
		int beginIndex = path.lastIndexOf(File.separatorChar) + 1;
		if (beginIndex < 0) {
			beginIndex = 0;
		}
		int endIndex = path.lastIndexOf('.');
		if (endIndex < beginIndex) {
			endIndex = path.length();
		}
		return path.substring(beginIndex, endIndex);
	}
	
	/**
	 * Get the CD name from cd file's abstract path
	 * @param cdPath cd file's abstract path
	 * @return
	 */
	public static String getMusicCdName(String cdPath) {
		int lastSeparatorChar = cdPath.lastIndexOf(File.separatorChar, cdPath.length()-2);
		int lastIndex = cdPath.lastIndexOf(File.separatorChar);
		if (lastSeparatorChar <=0) {
			lastSeparatorChar = 0;
		}
		if (lastSeparatorChar >= lastIndex) {
			lastIndex = cdPath.length()- 1;
		}
		Log.d(TAG, "getMusicCdName "+ lastIndex +"   "+lastIndex);
		return cdPath.substring(lastSeparatorChar, lastIndex);
	}
	

	private static void traverseDir(File dir, List<File> theList) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					traverseDir(files[i], theList);
				} else {
					//2013-7-1 -Amy- add .m4a/.ogg/.ape/.flac formate to music
					if(!files[i].isHidden()){
						String extension = getExtension(files[i]).toLowerCase();
						if (SUPPORT_MUSIC_FORMAT.containsKey(extension)) {
							theList.add(files[i]);
							//Log.e("sss", files[i].getAbsolutePath());
						}
					}
				}
			}
		}
	}
	
	/** This method should combine to traverseDir
	 * 
	 * @param dir
	 * @param theList
	 */
	private static void traverseDirOnce(File dir, List<File> theList) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
			
				if (files[i].isDirectory()) {
					//traverseDir(files[i], theList);
				} else {
					if (!files[i].isHidden() && (files[i].getName().toLowerCase().contains(Global.FILE_TYPE_MP3) || files[i].getName().toLowerCase().contains(Global.FILE_TYPE_WAV) || files[i].getName().toLowerCase().contains(Global.FILE_TYPE_AMR))) {
						theList.add(files[i]);
						Log.e("music-traversal", files[i].getAbsolutePath());
					}
				}
			}
		}
	}
/*
	private static void traverseFileList(List<File> List1, List<File> finalList) {
		//Log.i("photo-traversal", "traverseDir LOADED");
		if (List1 != null) {
			for (int i = 0; i < List1.size(); i++) {
                // skip all files or folders starting with a dot
                if (List1.get(i).getName().startsWith(".")) {
                    continue;
                }

                if (!List1.get(i).isHidden()) {
					finalList.add(List1.get(i));
					//Log.i("traverseFileList", List1.get(i).getAbsolutePath());

				}
			}
		}
	}*/
	
	private static Bitmap getCdImage(String filePath)
	{
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		try {
			mmr.setDataSource(filePath);
			byte[] art = mmr.getEmbeddedPicture();
			if (art != null) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
				/*Matrix mx = new Matrix(); 
				float scalef = 256f/bitmap.getHeight();
				mx.postScale(scalef, scalef);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),bitmap.getHeight(), mx , false);*/
				//bitmap.recycle();
				return bitmap;
			}
	    	
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static Bitmap maskCdShape(Bitmap cdCoverBmp, String name)
	{
		int argb;
		int width = 256;
		int height = 256;
		
		//get cd shape image
		//Bitmap cdCoverBmp = convertSnackCdThumbnail(bmap, name);
		Bitmap bmapCdCoverDim = Bitmap.createBitmap(width,height, Config.ARGB_8888);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				argb = cdCoverBmp.getPixel(i, j);
				int red = (argb & 0x00FF0000) >> 16;
				int green = (argb & 0x0000FF00) >> 8;
				int blue = (argb & 0x000000FF); 
				red = (int) (red * 0.5f);
				green = (int) (green * 05f);
				blue = (int) (blue * 0.5f);
				bmapCdCoverDim.setPixel(i, j, argb);
			}
		}
		
		//get small image(list view)
		//convertFullListViewThumbnail(bmap, name);
		//bmap.recycle();
		//bmapMask.recycle();
		//bmapCdShapeDim.recycle();
		return cdCoverBmp;
	}
	
	/**
	 * The tool method that make the icon showing in snack view
	 * 
	 * @param orgImage
	 * @param bottom
	 * @return
	 */
	
	private static Bitmap getMusicIconShaped(Bitmap orgImage, Bitmap bottom)
	{
		Bitmap bmpBg = bottom;
		Bitmap bmp = orgImage;
		int w = bottom.getWidth()-95;
		int h = bottom.getHeight()-111;
		float scale;
		if (w <= h) {
			scale = (float)w / bmp.getWidth();
		} else {
			scale = (float)h / bmp.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		//bmp.recycle();
		int cutx = (int) (bmp.getWidth() / 2 - w/2);
		//Bitmap cropBitmap = null;
		//int moveX = 0;
		if (cutx > 0) {
			bmp = Bitmap.createBitmap(bmp, cutx, 0, w, bmp.getHeight());
		} else {
			//moveX = (int)((w - bmp.getWidth())/2);
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight());
		}
		//resizedBitmap.recycle();
		Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		
		canvas.drawBitmap(bmp, 32, 12, null);
		//cropBitmap.recycle();
		return mutableBm;
	}
	
	private static Bitmap getMusicIconFullList(Bitmap bmp, Bitmap bmpBg)
	{
		if (bmp == null || bmpBg == null) {
			return null;
		}
		int w = bmpBg.getWidth() - 90;
		int h = bmpBg.getHeight() - 118;
		float scale = h / (float)bmp.getHeight();;
		/*if (w <= h) {
			scale = w / (float)bmp.getWidth();
		} else {
			scale = h / (float)bmp.getHeight();
		}*/
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		//bmp.recycle();
		int cutx = (int) (bmp.getWidth() / 2 - w/2);
		//Bitmap cropBitmap = null;
		//int moveX = 0;
		if (cutx > 0) {
			bmp = Bitmap.createBitmap(bmp, cutx, 0, w, bmp.getHeight());
		} else {
			//moveX = (int)((w - bmp.getWidth())/2);
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight());
		}
		//resizedBitmap.recycle();
		Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		
		canvas.drawBitmap(bmp, 42, 60, null);
		//cropBitmap.recycle();
		return mutableBm;
	}
	
	private static StringBuilder bytesToHexString(byte[] src) {
		StringBuilder builder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		String hv;
		for (int i = 0; i < src.length; i++) {
			hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder;
	}

	/**
	 * @return the sAlbumPath
	 */
	public static String getAlbumPath() {
		return sAlbumPath;
	}

	/**
	 * @param sAlbumPath the sAlbumPath to set
	 */
	public static void setAlbumPath(String sAlbumPath) {
		DataSourceManager.sAlbumPath = sAlbumPath;
	}
	

	/*private static Bitmap convertSnackCdThumbnail(Bitmap orgImage, String name, Bitmap bmpBg, Bitmap bmpTop) {
=======
	
	private static Bitmap convertSnackCdThumbnail(Bitmap orgImage, String name, Bitmap bmpBg, Bitmap bmpTop) {
>>>>>>> .r2514
		try {
			Bitmap bmp = orgImage;
			float scale = 300f / bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);

			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			int cutx = (int) (resizedBitmap.getWidth() / 2 - 150);
			Bitmap cropBitmap = null;
			int moveX = 0;
			if (resizedBitmap.getWidth() > 300) {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 300, resizedBitmap.getHeight());
			} else {
				moveX = (int)((300 - resizedBitmap.getWidth())/2);
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
			}
			resizedBitmap.recycle();

			Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(mutableBm);
			
			canvas.drawBitmap(cropBitmap, 40 + moveX, 10, null);
			canvas.drawBitmap(bmpTop, 0, 0, null);
			
			cropBitmap.recycle();
			bmpBg.recycle();
			bmpTop.recycle();

			//saveImage(mutableBm, path);
			
			return mutableBm;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private static void convertFullListViewThumbnail(Bitmap orgImage, String name, Bitmap bmpBg, Bitmap bmpTop) {
		try {
			Bitmap bmp = orgImage;
			float scale = 79f / bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);

			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			int cutx = (int) (resizedBitmap.getWidth() / 2 - 46);
			Bitmap cropBitmap = null;
			int moveX = 0;
			if (resizedBitmap.getWidth() > 90) {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 90, resizedBitmap.getHeight());
			} else {
				moveX = (int)((90 - resizedBitmap.getWidth())/2);
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
			}

			Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(mutableBm);
			
			canvas.drawBitmap(cropBitmap, 10 + moveX, 2, null);
			canvas.drawBitmap(bmpTop, 0, 0, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/
}
