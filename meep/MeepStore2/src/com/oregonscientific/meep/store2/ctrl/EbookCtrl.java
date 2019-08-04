package com.oregonscientific.meep.store2.ctrl;

import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.object.EncodingBase64;
import com.oregonscientific.meep.store2.db.TableStorePurchasedItem;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.StoreImageItem;

public class EbookCtrl {
	private final static String PATH_DATA_HOME = MEEPEnvironment.getMediaStorageDirectory().getAbsolutePath()+"/";
	public static boolean isEbookInstalled(String name){
		String file_name = EncodingBase64.encode(name);
			ArrayList<String> paths = new ArrayList<String>(3);
			paths.add(PATH_DATA_HOME + "ebook/data/" + file_name	 + Global.FILE_TYPE_EPUB);
			paths.add(PATH_DATA_HOME + "ebook/data/" + file_name + Global.FILE_TYPE_PDF);
			paths.add(PATH_DATA_HOME + "ebook/unzip/" + file_name + "/");
			
			Log.w("test",file_name);
			for (String path : paths) {
			    if ((new File(path)).exists()) {
			        MeepStoreLog.logcatMessage("isEbookInstalled", "folder path: " + path  + " installed: YES");
			        return true;
			    }
			}
		return false;
	}
//	public static boolean isEbookInstalled(String id, SQLiteDatabase db){
//		ArrayList<StoreImageItem> itemlist = getPurchasedItemFromDb(db);
//		
//		for(StoreImageItem item: itemlist){
//			if(item.getId().equals(id)){
//				ArrayList<String> paths = new ArrayList<String>(3);
//				paths.add(PATH_DATA_HOME + "ebook/data/" + item.getName() + Global.FILE_TYPE_EPUB);
//				paths.add(PATH_DATA_HOME + "ebook/data/" + item.getName() + Global.FILE_TYPE_PDF);
//				paths.add(PATH_DATA_HOME + "ebook/unzip/" + item.getName() + "/");
//				
//				Log.w("test",item.getName());
//				for (String path : paths) {
//					if ((new File(path)).exists()) {
//						MeepStoreLog.logcatMessage("isEbookInstalled", "folder path: " + path  + " installed: YES");
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}
	
	private static ArrayList<StoreImageItem> getPurchasedItemFromDb(SQLiteDatabase db){
		ArrayList<StoreImageItem> itemList = new ArrayList<StoreImageItem>();
		Cursor c = db.rawQuery(TableStorePurchasedItem.getSelectItemSql(), null);
		int rowNum = c.getCount();
		if(rowNum>0){
			c.moveToFirst();
			for(int i=0; i< rowNum; i++){
				String id = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_ID));
				String name = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_NAME));
				String type = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_TYPE));
				String iconUrl = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_ICON_URL));
				StoreImageItem item = new StoreImageItem(id, name, type, iconUrl);
				itemList.add(item);
				c.moveToNext();
			}
		}
		return itemList;
	}
	
	public static void removeEbook(MeepStoreItem item){
		String epubpath = PATH_DATA_HOME + "ebook/data/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_EPUB;
		deleteFile(epubpath);
		String pdfpath = PATH_DATA_HOME + "ebook/data/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_PDF;
		deleteFile(pdfpath);
		String iconPath = PATH_DATA_HOME + "ebook/icon/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_PNG;
		deleteFile(iconPath);
		String iconldPath = PATH_DATA_HOME + "ebook/icon_lm/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_PNG;
		deleteFile(iconldPath);
		String iconlmPath = PATH_DATA_HOME + "ebook/icon_ld/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_PNG;
		deleteFile(iconlmPath);
		String iconsPath = PATH_DATA_HOME + "ebook/icon_s/" + EncodingBase64.encode(item.getName()) + Global.FILE_TYPE_PNG;
		deleteFile(iconsPath);
		String folderpath = PATH_DATA_HOME + "ebook/unzip/" + EncodingBase64.encode(item.getName()) + "/";
		MeepStorageCtrl.deleteDirectory(new File(folderpath));
	}
	
	private static void deleteFile(String path){
		File file = new File(path);
//		file.deleteOnExit();
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			Log.e("isEbookInstalled", "delete file error : " + e.toString());
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
}
