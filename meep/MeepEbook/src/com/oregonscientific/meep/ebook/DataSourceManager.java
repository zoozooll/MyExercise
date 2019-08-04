package com.oregonscientific.meep.ebook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;

import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.control.EbookCtrl;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.object.EncodingBase64;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.opengl.OSButton;

public class DataSourceManager 
{	
	private static List<OsListViewItem> mListViewItemList ;
	private static final String TAG = "DataSourceManager";
	private static final String BASIC_STORAGE_PATH = MEEPEnvironment.getMediaStorageDirectory().getPath()+File.separatorChar;
	/*pathDataDir = EBOOK_PATH_DATA_DIR;
	pathLargeIconDir = EBOOK_PATH_LARGE_ICON_DIR;
	pathLargeMirrorIconDir = EBOOK_PATH_LARGE_MIRROR_ICON_DIR;
	pathLargeDimIconDir = EBOOK_PATH_LARGE_DIM_ICON_DIR;
	pathSmallIconDir = EBOOK_PATH_SMALL_ICON_DIR;
	pathIcon = EBOOK_PATH_DATA_ICON_DIR;
	private final String EBOOK_PATH_DATA_DIR =  BASIC_STORAGE_PATH + "ebook/temp/";
	private final String EBOOK_PATH_DATA_ICON_DIR =  BASIC_STORAGE_PATH + "ebook/icon/";
	private final String EBOOK_PATH_LARGE_ICON_DIR =  BASIC_STORAGE_PATH + "ebook/icon_l/";
	private final String EBOOK_PATH_LARGE_MIRROR_ICON_DIR =  BASIC_STORAGE_PATH + "ebook/icon_lm/";
	private final String EBOOK_PATH_LARGE_DIM_ICON_DIR =  BASIC_STORAGE_PATH + "ebook/icon_ld/";
	private final String EBOOK_PATH_SMALL_ICON_DIR =  BASIC_STORAGE_PATH + "ebook/icon_s/";
	*/
	
	private static final String PATHDATADIR = BASIC_STORAGE_PATH + "ebook/data/";
	private static final String PATHDATADIR_ZIP = BASIC_STORAGE_PATH + "ebook/unzip/";
	//private static final String BASIC_CACHES_PATH = "/data/home/";
	private static final String PATHLARGEICONDIR = BASIC_STORAGE_PATH + "ebook/icon_l/";
	private static final String RATHLARGEDIMICONDIR = BASIC_STORAGE_PATH + "ebook/icon_ld/";
	private static final String PATHLARGEMIRRORICONDIR = BASIC_STORAGE_PATH + "ebook/icon_lm/";
	private static final String PATHSMALLICONDIR = BASIC_STORAGE_PATH + "ebook/icon_s/";
	private static final String PATHICON = BASIC_STORAGE_PATH + "ebook/icon/";
	//private static final String TAG = "EbookCtrl";
	public static final String[] TYPES = new String[]{Global.FILE_TYPE_EPUB, Global.FILE_TYPE_PDF};
	
	
	public DataSourceManager(Context context) {
		super();
	}

	public static List<File> getEbookList(){
		List<File> ebookFileList = new ArrayList<File>();
		//add unziped ebook first
		//Log.d(TAG, "PATHDATADIR_ZIP "+PATHDATADIR_ZIP);
		File unzipDir = new File(PATHDATADIR_ZIP);
		
		if(unzipDir.exists() && unzipDir.isDirectory()){
			File[] unzipArr = unzipDir.listFiles();
			for(File f:unzipArr){
				ebookFileList.add(f);
			}
		}else{
			Log.w(TAG, "unzip path not exist :" + PATHDATADIR_ZIP);
		}
		
		//search epub file
		
		//2013-03-26 - raymond - remove /data/home/ebook/data/
		String[] dirArray = new String[]{
				//Environment.getExternalStorageDirectory().getAbsolutePath()+File.separatorChar,
				PATHDATADIR,
				"/mnt/extsd/"
				,"/storage/external_storage/"
		};
		
		for(String path : dirArray){
			List<File> fileList = getEbookListByPath(path);
			if(fileList!= null){
				for (File f : fileList) {
					boolean sameName = false;
					String extension = getExtension(f.getAbsolutePath()); 
					
					for (File f2 : ebookFileList) { 
						if (getFileNameWithoutExten(f.getName()).equals(f2.getName())) {
							sameName = true;
							break;
						}
					}
					
					
					if(!sameName && (extension.equals(Global.FILE_TYPE_EPUB) || extension.equals(Global.FILE_TYPE_PDF))) {
						ebookFileList.add(f);
					}
				}
				//ebookFileList.addAll(fileList);
			}
		}
		
		return ebookFileList;
		
	}
	
	/**
	 *  list file with the MEEP APP type
	 * @param dir - the folder to search
	 * @param theList	-	the return file list
	 * @param types	- 	the APP type (Ebook, Game...)
	 */
	public static void traverseDir(File dir, List<File> theList, String[] types) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i=0; i<files.length; i++) {
				if (files[i].isDirectory() && !files[i].isHidden()) {
					traverseDir(files[i], theList, types);
				} else {
					if(!files[i].isHidden()){
						//Log.d(TAG, "traverseDir LOADED " + files[i].getAbsolutePath());
						for(String type :types){
							String fileName = files[i].getName().toLowerCase();
							
							if(fileName.contains(type)){
								//Log.d("MeepResourceCtrl", "get file name:" + files[i].getAbsolutePath());
								String fileType = getFileHeader(files[i].getAbsolutePath());
								//Log.e("ebook","type--"+ fileType+"path-- " + files[i].getAbsolutePath());
								if (fileType.equals("504B03") || fileType.equals("255044")) {
									theList.add(files[i]);
								}				
								break;
							}
						}
					}
				}
			}
		}
	}
	//2013-6-21 - Zoya - slove #4482
	public static String getFileHeader(String filePath) {
		FileInputStream is = null;
		String value = null;
		try {
			is = new FileInputStream(filePath);
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			value = bytesToHexString(b);
		} catch (Exception e) {
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}

	private static String bytesToHexString(byte[] src) {
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
		return builder.toString();

	}

	// get ListItemlist
	public static List<OsListViewItem> getAllItems(boolean refresh) {
		if (refresh || mListViewItemList == null
				|| mListViewItemList.size() == 0) {
			if (mListViewItemList == null) {
				mListViewItemList = new Vector<OsListViewItem>();
			} else {
				mListViewItemList.clear();
			}
			synchronized (mListViewItemList) {
				List<File> fileList = getEbookList();
				getDataSource(fileList, mListViewItemList);
			}
		}
		return mListViewItemList;
	}

	public static void getDataSource(List<File> fileList, List<OsListViewItem> items) {
		//Log.d(TAG, "cdList " + Arrays.toString(fileList.toArray()));
		for (File thisFile : fileList) {
			String name = thisFile.getName();
			//2013-3-18 -Amy- Rename after refres
			//String photoName = filename.substring(0,filename.lastIndexOf("."));
			
			OsListViewItem item = new OsListViewItem();
			//item.setImage(bmap);
			String decodedName=null;
			int lengthToReduce = 5;
			decodedName = getFileNameWithoutExten(name);
			if (thisFile.isDirectory())
			{	
				
				decodedName = EncodingBase64.decode(name);
				
			}
			// modified by aaronli at Jul12 2013/ decoded the name of file which in data folder.
			// fixed #5139
			if (thisFile.getParentFile().getPath().equals(PATHDATADIR)) 
			{
				decodedName = EncodingBase64.decode(decodedName);

			}else {
				

				//decodedName = thisFile.getName().substring(0, thisFile.getName().length() - lengthToReduce);

			}
			item.setName(decodedName);
			item.setPath(thisFile.getAbsolutePath());
			items.add(item);
		}
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
			Log.e("EbookFullListView", "delete file error : " + e.toString());
		}
	}
	
	public static void deleteItemFiles(OsListViewItem item) {
		EbookCtrl.deleteEbook(item.getPath());
	}
	
	public static boolean deleteDataItems(OsListViewItem... items) {
		for (OsListViewItem o : items) {
			mListViewItemList.remove(o);
		}
		return true;
	}
	
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
	

	/**
	 * @param dirPathDimPath
	 * @param imageName
	 * @param button
	 */
	public static void loadingEbookImage(OSButton button,Bitmap mBmpBg) {
		String bookPath = button.getContentPath().get(0);
		String imageName = getEbookImgName(bookPath);
		try {
			// get icon image
			Bitmap mirrorImg = null;
			
			final String filePath = PATHLARGEMIRRORICONDIR + imageName + Global.FILE_TYPE_PNG;
			File largeMirrorIcon = new File(filePath);
			
			if(largeMirrorIcon.exists())
			{
				mirrorImg = BitmapFactory.decodeFile(filePath);

			}
			if (mirrorImg==null) {  // If PATHLARGEMIRRORICONDIR wrong, should find in  PATHLARGEICONDIR
				//File largeIcon = null;
				//mirrorImg = getSnackIcon(getLargeIconEbook(imageName), mBmpBg);
				mirrorImg = getLargeIconEbook(imageName, mBmpBg);
			}

			if (mirrorImg != null) {
				MediaManager.saveImageToExternal(mirrorImg, filePath);
				button.setTextureBmp(mirrorImg);
				
				String dimImgPath = RATHLARGEDIMICONDIR + imageName + Global.FILE_TYPE_PNG;

				File dimImgFile = new File(dimImgPath);
				Bitmap dimBmap = null;
				

				if (dimImgFile.exists()) {
					// delete the old image
					if (dimImgFile.lastModified() < largeMirrorIcon.lastModified()) {
						dimImgFile.delete();
						dimBmap = MediaManager.getDimImage2(mirrorImg);
						saveImage(dimBmap, dimImgPath);

					} else {
						dimBmap = BitmapFactory.decodeFile(dimImgPath);
						if (dimBmap == null) {
							dimBmap = MediaManager.getDimImage2(mirrorImg);
							saveImage(dimBmap, dimImgPath);
						}
					}
					
				} else {
					dimBmap = MediaManager.getDimImage2(mirrorImg);
					saveImage(dimBmap, dimImgPath);

				}
				if (dimBmap != null) {
					button.setDimTextureBmp(dimBmap);
				} else {
				}

			} 

			//resetDummyIndex();
		} catch (Exception ex) {
			Log.w("FunctionMenuAnimationCtrl", "initFunctionMenu->cannot load icon bitmap", ex);
		}
	}

	public static String getEbookImgName(String bookPath) {
		String imageName = null;
		File thisFile = new File(bookPath);
		if(thisFile.isDirectory()){
			imageName = thisFile.getName(); 
		} else {
			/*if (thisFile.getAbsolutePath().contains(PATHDATADIR)) {
				imageName = getFileNameWithoutExten(bookPath);
			} else {
				imageName = EncodingBase64.encode(getFileNameWithoutExten(bookPath));
			}*/
			imageName = getFileNameWithoutExten(bookPath);
		}
		return imageName;
	}

	public static Bitmap getLargeIconEbook(String imageName, Bitmap bgImage) {
		String largeFilePath = PATHLARGEICONDIR + imageName + Global.FILE_TYPE_PNG;
		//largeIcon = new File(largeFilePath);
		Bitmap largeImage = null;
		if (new File(largeFilePath).exists()) {

			// get the mirror image from large icon
			Options op = new Options();
			op.inPreferredConfig = Config.ARGB_8888;
			largeImage = BitmapFactory.decodeFile(largeFilePath, op);
			
		} 
		
		if (largeImage == null){	// If PATHLARGEICONDIR wrong, should find in  PATHICON

			String iconFilePath = PATHICON + imageName + Global.FILE_TYPE_PNG;
			if (new File(iconFilePath).exists()) {
				largeImage = BitmapFactory.decodeFile(iconFilePath);
			}

			if (largeImage != null) {
				largeImage = getLargeCacheBitmap(largeImage, bgImage);
				MediaManager.saveImageToExternal(largeImage, largeFilePath);
			}
		}
		return largeImage;
	}

	public static Bitmap loadListImage(Bitmap mBmpBg, String imageName) {
		String smallFilePath = PATHSMALLICONDIR + imageName + Global.FILE_TYPE_PNG;
		Bitmap mutableBitmap = null;
		Bitmap flag = null;
		if (new File(smallFilePath).exists()) {
			mutableBitmap = BitmapFactory.decodeFile(smallFilePath);
		}
		/*if (mutableBitmap == null) {
			flag = getLargeIconEbook(imageName);
			if (flag != null) {
				mutableBitmap = mBmpBg.copy(Bitmap.Config.ARGB_8888, true);
				Canvas canvas = new Canvas(mutableBitmap);
				canvas.drawBitmap(flag, null, new Rect(21, 12, 183, 256), null);
				MediaManager.saveImageToExternal(mutableBitmap, smallFilePath);
			} 
		} */
		if (mutableBitmap == null) {
			mutableBitmap = getLargeIconEbook(imageName, mBmpBg);
		}
		return mutableBitmap;
	}
	
	private static Bitmap getLargeCacheBitmap (Bitmap scrBmp, Bitmap bgBmp) {

		Bitmap bitmap1 = bgBmp.copy(Bitmap.Config.ARGB_8888, true);
		//bitmap2 =((BitmapDrawable) getResources().getDrawable(R.drawable.po))).getBitmap();

		int w = 130;
		int h = 186;
		
		Bitmap newbitmap = MeepStorageCtrl.resizeImage(scrBmp, w, h);
		int nw = newbitmap.getWidth(); int nh = newbitmap.getHeight();
		Canvas canvas = new Canvas(bitmap1);
		canvas.drawBitmap(newbitmap, 15, 14, null);

		return bitmap1;
	}

	/**
	 * Get file name without the extension from file's abstract path<br> 
	 * @param path file's abstract path.<br>If the file's path is folder and end with {@link File.separatorChar},it should show wrong 
	 * @return the file name without extension
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

	private static List<File> getEbookListByPath(String path) {
		File dir = new File(path);
		List<File> fileList = new ArrayList<File>();
		if (dir.exists()) {
			traverseDir(dir, fileList, TYPES);
			return fileList;
		} else {
			return null;
		}
	}
	
	public static boolean fileNameisAlreadyExecised(String name) {
		for (int i = 0; i < mListViewItemList.size(); i++){
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
	
	private static String getExtension(String filename) {
		// dot is included
		int dotIndex = filename.lastIndexOf(".");
		
		if (dotIndex >= 0) {
			return filename.substring(dotIndex,filename.length()).toLowerCase();
		} else {
			return "";
		}
	}
	
	public static void renameFile(String renameFileName, OsListViewItem item){
		//2013-3-18 -Amy- Rename after refres
		// 2013-3-21 -aaronli- and return the new file path
		String newPath = renameEbook(item.getPath(), renameFileName);
		item.setPath(newPath);
		item.setName(renameFileName);
		//EbookCtrl.renameEbook(item.getPath(), EncodingBase64.encode(renameFileName));
		
//		String oldName = EncodingBase64.decode(item.getName().substring(0, item.getName().length()));
//		String pathTo = item.getPath().substring(0, item.getPath().lastIndexOf("/")+1);
//			
//		Log.i("path from", "path from: " + item.getPath());
//		Log.i("path to", "path to: " + pathTo + renameFileName + ".epub");
//
//		
//		File from = new File(item.getPath());
//		File to = new File(pathTo + renameFileName + ".epub");
//		from.renameTo(to);
//		
//		File fromIcon = new File(PATH_ICON_DIR + oldName + ".png");
//		File toIcon = new File(PATH_ICON_DIR + renameFileName + ".png");
//		fromIcon.renameTo(toIcon);
//		
//		File fromLargeIcon = new File(PATH_LARGE_ICON_DIR + oldName + ".png");
//		File toLargeIcon = new File(PATH_LARGE_ICON_DIR + renameFileName + ".png");
//		fromLargeIcon.renameTo(toLargeIcon);
//		
//		File fromDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + oldName + ".png");
//		File toDimIcon = new File(PATH_LARGE_DIM_ICON_DIR + renameFileName + ".png");
//		fromDimIcon.renameTo(toDimIcon);
//		
//		File fromSmallIcon = new File(PATH_SMALL_ICON_DIR + oldName + ".png");
//		File toSmallIcon = new File(PATH_SMALL_ICON_DIR + renameFileName + ".png");
//		fromSmallIcon.renameTo(toSmallIcon);
	}
	
	/**
	 * @author aaronli
	 * @data Mar21 2013
	 * @param filePath
	 * @param newFileName
	 * @return the new file's absolute path string
	 */
	private static String renameEbook(String filePath, String newFileName){
		try {
			//Log.d("rename", "rename ebook:" + filePath);
			File file = new File(filePath);
			File newFile = new File(file.getParentFile().getAbsolutePath() + File.separatorChar + newFileName);
			String fileNameWoType = null;
			if(file.isDirectory()){
				fileNameWoType = file.getName();
				newFileName =  EncodingBase64.encode(newFileName);
				newFile = new File(file.getParentFile(), newFileName);
				file.renameTo(newFile);
				//Log.d("rename", "rename from:" + file.getAbsolutePath()  + "  to:" + newFile.getAbsolutePath());
			}else{
				// modified by aaronli Jul16 2013
				//renamed epub and pdf path which in data dir,it need to encode the name.;
				if (file.getParent().equalsIgnoreCase(PATHDATADIR)) {
					newFileName =  EncodingBase64.encode(newFileName);
				}
				newFile = new File(file.getParent(), newFileName + getExtension(filePath));
				// modified end.
				if (newFile != null) {
    				file.renameTo(newFile);
    				///Log.d("rename", "rename from:" + file.getAbsolutePath()  + "  to:" + newFile.getAbsolutePath());
    				fileNameWoType = file.getName().substring(0, file.getName().lastIndexOf("."));
    				
    				String ebookdataDir = file.getParentFile().getAbsolutePath() + "/"+ fileNameWoType;
    				String newEbookDataDir = file.getParentFile().getAbsolutePath()+ "/" + newFileName;
    				//2013-3-18 -Amy- Rename after refres
    				//rename dir with same name in same folder
    				File dir = new File(ebookdataDir);
    				if(dir.exists() && dir.isDirectory()){
    					dir.renameTo(new File(newEbookDataDir));
    					//Log.d("rename", "rename from:" + dir.getAbsolutePath()  + "  to:" +newEbookDataDir);
    				}
				}
			}
			//2013-7-22 -Amy- add Add cache directory
			String iconlPath = PATHLARGEICONDIR + fileNameWoType + Global.FILE_TYPE_PNG;
		    String newIconlPath = PATHLARGEICONDIR + newFileName +Global.FILE_TYPE_PNG;
		    //Log.d(TAG, "newIconPath "+newIconPath);
		    new File(iconlPath).renameTo(new File(newIconlPath));
			   
			String iconPath = PATHICON + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconPath = PATHICON + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconPath "+newIconPath);
			new File(iconPath).renameTo(new File(newIconPath));
			
			String iconldPath = RATHLARGEDIMICONDIR + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconldPath = RATHLARGEDIMICONDIR + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconldPath "+newIconldPath);
			new File(iconldPath).renameTo(new File(newIconldPath));

			String iconlmPath = PATHLARGEMIRRORICONDIR + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconlmPath = PATHLARGEMIRRORICONDIR + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconlmPath "+newIconlmPath);
			new File(iconlmPath).renameTo(new File(newIconlmPath));
			

			String iconsPath = PATHSMALLICONDIR + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconsPath = PATHSMALLICONDIR + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconsPath "+newIconsPath);
			new File(iconsPath).renameTo(new File(newIconsPath));
			return newFile.getAbsolutePath();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static Bitmap getSnackIcon(Bitmap bitmap, Bitmap bmpBg)
	{
		//Bitmap bmp = bitmap.getWidth();
		//Log.d(TAG, "getEbookLargeIcon "+bitmap.getWidth() +" "+bitmap.getHeight());
		/*bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 275);*/
		
		Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		
		canvas.drawBitmap(bitmap, null, new Rect(80, 12, 241, 256), null);
		
		return mutableBm;
	}
}
