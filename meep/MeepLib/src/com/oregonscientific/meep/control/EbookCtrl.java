package com.oregonscientific.meep.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.provider.ContactsContract.Directory;
import android.util.Log;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.object.EncodingBase64;

public class EbookCtrl {
	private static final String TAG = "EbookCtrl";
	public static final String[] TYPES = new String[]{Global.FILE_TYPE_EPUB, Global.FILE_TYPE_PDF};
	
	
	/** get all the .epub file in the system
	 * @return list of .epub file
	 */
	public static List<File> getEbookList(){
		List<File> ebookFileList = new ArrayList<File>();
		//add unziped ebook first
		File unzipDir = new File("/data/home/ebook/unzip/");
		
		if(unzipDir.exists() && unzipDir.isDirectory()){
			File[] unzipArr = unzipDir.listFiles();
			for(File f:unzipArr){
				ebookFileList.add(f);
			}
		}else{
			Log.d(TAG, "unzip path not exist :");
		}
		
		//search epub file
		
		//2013-03-26 - raymond - remove /data/home/ebook/data/
		String[] dirArray = new String[]{
				"/mnt/sdcard/",
				"/mnt/extsd/"
		};
		
		for(String path : dirArray){
			List<File> fileList = getEbookListByPath(path);
			if(fileList!= null){
				for (File f : fileList) {
					boolean sameName = false;
					String extension = getExtension(f.getAbsolutePath()); 
					
					for (File f2 : ebookFileList) {
						if (f.getName().substring(0, f.getName().length() - 5).equals(f2.getName())) {
							sameName = true;
							break;
						}
					}
					
					Log.w("ST", extension);
					
					if(!sameName && (extension.equals(Global.FILE_TYPE_EPUB) || extension.equals(Global.FILE_TYPE_PDF))) {
						Log.w("ST", f.getAbsolutePath());
						ebookFileList.add(f);
					}
				}
				//ebookFileList.addAll(fileList);
			}
		}
		
		
		Log.d(TAG, "get number of ebook:" + ebookFileList.size());
		return ebookFileList;
		
	}

	private static List<File> getEbookListByPath(String path) {
		File dir = new File(path);
		List<File> fileList = new ArrayList<File>();
		if (dir.exists()) {
			MeepResourceCtrl.traverseDir(dir, fileList, TYPES);
			return fileList;
		} else {
			return null;
		}
	}
	
	public static void deleteEbook(String filePath){
		try {
			Log.d("deleteEbook", "delete ebook:" + filePath);
			File file = new File(filePath);
			String fileNameWoType = null;
			
			if(file.isDirectory()){
				fileNameWoType = file.getName();
				MeepStorageCtrl.deleteDirectory(file);
			}else{
				
				fileNameWoType = file.getName().substring(0, file.getName().lastIndexOf("."));
				String ebookdataDir = file.getParentFile().getAbsolutePath() + fileNameWoType;
				file.delete();
				//delete dir with same name in same folder
				File dir = new File(ebookdataDir);
				if(dir.exists() && dir.isDirectory()){
					MeepStorageCtrl.deleteDirectory(dir);
				}
				
			}

			String iconPath = Global.PATH_DATA_HOME + "ebook/icon/" + fileNameWoType + Global.FILE_TYPE_PNG;
			new File(iconPath).delete();

			String iconldPath = Global.PATH_DATA_HOME + "ebook/icon_ld/" + fileNameWoType + Global.FILE_TYPE_PNG;
			new File(iconldPath).delete();

			String iconlmPath = Global.PATH_DATA_HOME + "ebook/icon_lm/" + fileNameWoType + Global.FILE_TYPE_PNG;
			new File(iconlmPath).delete();

			String iconsPath = Global.PATH_DATA_HOME + "ebook/icon_s/" + fileNameWoType + Global.FILE_TYPE_PNG;
			new File(iconsPath).delete();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author aaronli
	 * @data Mar21 2013
	 * @param filePath
	 * @param newFileName
	 * @return the new file's absolute path string
	 */
	public static String renameEbook(String filePath, String newFileName){
		try {
			//Log.d("rename", "rename ebook:" + filePath);
			File file = new File(filePath);
			File newFile = new File(file.getParentFile().getAbsolutePath() + "/" + newFileName);
			String fileNameWoType = null;
			if(file.isDirectory()){
				fileNameWoType = file.getName();
				newFileName =  EncodingBase64.encode(newFileName);
				newFile = new File(file.getParentFile(), newFileName);
				file.renameTo(newFile);
				//Log.d("rename", "rename from:" + file.getAbsolutePath()  + "  to:" + newFile.getAbsolutePath());
			}else{
				//rename epub file
				newFile = null;
				//2013-3-18 -Amy- Rename after refres
				//if (filePath.substring(0, filePath.length() - 5) == Global.FILE_TYPE_EPUB) {
				//if ((filePath.substring(filePath.length() - 5, filePath.length())).equals(Global.FILE_TYPE_EPUB)) {
				// modified by aaronli Mar21 2013
				if ((filePath.substring(filePath.lastIndexOf("."))).equals(Global.FILE_TYPE_EPUB)) {
			        newFile = new File(file.getParentFile().getAbsolutePath()+ "/" + newFileName + Global.FILE_TYPE_EPUB);
				//} else if (filePath.substring(0, filePath.length() - 5) == Global.FILE_TYPE_PDF) {
				//} else if ((filePath.substring(filePath.length() - 4, filePath.length())).equals(Global.FILE_TYPE_PDF)) {
			    } else if ((filePath.substring(filePath.lastIndexOf("."))).equals(Global.FILE_TYPE_PDF)) {
				    newFile = new File(file.getParentFile().getAbsolutePath()+ "/" + newFileName + Global.FILE_TYPE_PDF);
				}
				// modified end
				
				if (newFile != null) {
    				file.renameTo(newFile);
    				Log.d("rename", "rename from:" + file.getAbsolutePath()  + "  to:" + newFile.getAbsolutePath());
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

			String iconPath = Global.PATH_DATA_HOME + "ebook/icon/" + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconPath = Global.PATH_DATA_HOME + "ebook/icon/" + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconPath "+newIconPath);
			new File(iconPath).renameTo(new File(newIconPath));

			String iconldPath = Global.PATH_DATA_HOME + "ebook/icon_ld/" + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconldPath = Global.PATH_DATA_HOME + "ebook/icon_ld/" + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconldPath "+newIconldPath);
			new File(iconldPath).renameTo(new File(newIconldPath));

			String iconlmPath = Global.PATH_DATA_HOME + "ebook/icon_lm/" + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconlmPath = Global.PATH_DATA_HOME + "ebook/icon_lm/" + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconlmPath "+newIconlmPath);
			new File(iconlmPath).renameTo(new File(newIconlmPath));
			

			String iconsPath = Global.PATH_DATA_HOME + "ebook/icon_s/" + fileNameWoType + Global.FILE_TYPE_PNG;
			String newIconsPath = Global.PATH_DATA_HOME + "ebook/icon_s/" + newFileName +Global.FILE_TYPE_PNG;
			//Log.d(TAG, "newIconsPath "+newIconsPath);
			new File(iconsPath).renameTo(new File(newIconsPath));
			return newFile.getAbsolutePath();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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

}
