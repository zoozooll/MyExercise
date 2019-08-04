package com.oregonscientific.meep.store2.ctrl;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.oregonscientific.meep.store2.db.TableStoreImageCache;
import com.oregonscientific.meep.store2.db.TableStoreItemScreenShotCache;
import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class StoreItemCacheCtrl {
	
	SQLiteDatabase mDb;
	
	public StoreItemCacheCtrl(SQLiteDatabase db ){
		
		mDb = db;
	}
	
	public Bitmap getImageCache(String itemId){
		String sql = TableStoreImageCache.getSelectItemSql(itemId);
		Cursor c = mDb.rawQuery(sql, null);
		if(c.getCount()>0){
			c.moveToFirst();
			byte[] bytes = c.getBlob(0);
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}else{
			return null;
		}
	}
	
	public void saveImageCache(String itemId, byte[] bytes){
		String selectSql = TableStoreImageCache.getSelectItemSql(itemId);
		Cursor c = mDb.rawQuery(selectSql, null);
		if(c.getCount()>0) return;
		
		String sql = TableStoreImageCache.getInsertSql(itemId);
		SQLiteStatement insertStmt = mDb.compileStatement(sql);
		insertStmt.clearBindings();
		insertStmt.bindBlob(1, bytes);
		insertStmt.executeInsert();	
	}
	
	public ArrayList<Bitmap> getScreenShotCache(String itemId){
		String sql = TableStoreImageCache.getSelectItemSql(itemId);
		ArrayList<Bitmap> screenShots = new ArrayList<Bitmap>();
		Cursor c = mDb.rawQuery(sql, null);
		int rowNum = c.getCount();
		if(c.getCount()>0){
			c.moveToFirst();
			for(int i=0; i< rowNum; i++){
				byte[] bytes = c.getBlob(0);
				Bitmap screen = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				screenShots.add(screen);
				c.moveToNext();
			}
			return screenShots;
		}else{
			return null;
		}
	}
	
	public void saveScreenShot(String itemId, int orderNo, byte[] bytes){
		String sql = TableStoreItemScreenShotCache.getInsertSql(itemId,orderNo);
		SQLiteStatement insertStmt = mDb.compileStatement(sql);
		insertStmt.clearBindings();
		insertStmt.bindBlob(1, bytes);
		insertStmt.executeInsert();	
	}
	
	public ArrayList<MeepStoreItem> getAllCacheImage(ArrayList<MeepStoreItem> itemList){
		for(int i =0; i< itemList.size(); i++)
		{
			MeepStoreItem item = itemList.get(i);
			if(item.getIcon()==null){
				item.setIcon(getImageCache(item.getItemId()));
			}	
		}
		return itemList;
	}
	
	public void clearImageOutofDate()
	{
		String sql = TableStoreImageCache.getDelete1DayCacheSql();
		mDb.execSQL(sql);
	}
}
