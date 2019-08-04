package com.oregonscientific.meep.store2.db;

import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TableMeepStoreDownloadQueue {
	public static final String S_TABLE_NAME = "MeepStoreDownloadQueue";
	public static final String S_ID = "id";
	public static final String S_NAME = "name";
	public static final String S_URL = "url";
	public static final String S_STORE_PATH = "storePath";
	public static final String S_IMAGE_URL = "imageUrl";
	public static final String S_IMAGE_PATH = "imagePath";
	public static final String S_FIlE_STATUS = "fileStatus"; //0 not start, 1: downloading, 2 completed, 3 installed, 4 uninstalled
	public static final String S_IMAGE_STATUS = "imageStatus"; //0 not start, 1: downloading, 2 completed
	public static final String S_CREATE_TIME = "createTime";
	public static final String S_COMPLETE_TIME = "completeTime";
	public static final String S_TYPE = "type";
	public static final String S_CHECKSUM = "checksum";
	public static final String S_PACKAGE_NAME = "packageName";
	
	public static final int STATUS_WAITING = 0;
	public static final int STATUS_DOWNLOADING = 1;
	public static final int STATUS_COMPLETED = 2;
	public static final int STATUS_INSTALLED = 3;
	public static final int STATUS_UNINSTALLED = 4;
	public static final int STATUS_DOWNLOAD_FAIL = -1;
	public static final int STATUS_INSTALL_FAIL = -2;
	
	
	public String id;
	public String Name;
	public String Url;
	public String StorePath;
	public String ImageUrl;
	public String ImagePath;
	public int FileStatus;
	public int ImageStatus;
	public Date CreateTime;
	public Date CompletdTime;
	public String Type;
	public String checksum;
	public String packageName;
	
	
	public TableMeepStoreDownloadQueue(String id, String type, String name, String url, String storePath, String imageUrl, String imagePath, int fileStatus, int imageStatus, String checksum, String packageName){
		this.id = id;
		this.Type = type;
		this.Name = name;
		this.Url = url;
		this.StorePath = storePath;
		this.ImageUrl = imageUrl;
		this.ImagePath = imagePath;
		this.FileStatus = fileStatus;
		this.checksum = checksum;
		this.packageName = packageName;
	}

	public static String getCreateSql(){
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ID); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_TYPE); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_NAME); sb.append( " VARCHAR NOT NULL ,");
        sb.append( S_PACKAGE_NAME); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_URL); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_STORE_PATH); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_IMAGE_URL); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_IMAGE_PATH); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_CHECKSUM); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_FIlE_STATUS ); sb.append(" INTEGER NOT NULL,");
		sb.append( S_IMAGE_STATUS ); sb.append(" INTEGER NOT NULL,");
		
		sb.append( S_CREATE_TIME ); sb.append(" DATETIME NOT NULL,");
		sb.append( S_COMPLETE_TIME ); sb.append(" DATETIME");
		
		sb.append(" )");
		return sb.toString();
	}
	
	
	public String getInsertSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(",");
		sb.append(S_NAME);
		sb.append(",");
        sb.append(S_PACKAGE_NAME);
        sb.append(",");
		sb.append(S_TYPE);
		sb.append(",");
		sb.append(S_URL);
		sb.append(",");
		sb.append(S_STORE_PATH);
		sb.append(",");
		sb.append(S_IMAGE_URL);
		sb.append(",");
		sb.append(S_IMAGE_PATH);
		sb.append(",");
		sb.append(S_CHECKSUM);
		sb.append(",");
		sb.append(S_FIlE_STATUS);
		sb.append(",");
		sb.append(S_IMAGE_STATUS);
		sb.append(",");
		sb.append(S_CREATE_TIME);
		sb.append(") VALUES ('");
		sb.append(this.id);
		sb.append("','");
		try {
            sb.append(URLEncoder.encode(this.Name, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            sb.append("<unknown>");
        }
        sb.append("','");
        sb.append(this.packageName);
		sb.append("','");
		sb.append(this.Type);
		sb.append("','");
		sb.append(this.Url);
		sb.append("','");
		sb.append(this.StorePath);
		sb.append("','");
		sb.append(this.ImageUrl);
		sb.append("','");
		sb.append(this.ImagePath);
		sb.append("','");
		sb.append(this.checksum);
		sb.append("',");
		sb.append(this.FileStatus);
		sb.append(",");
		sb.append(this.ImageStatus);
		sb.append(",datetime()");
		sb.append(")");
		return sb.toString();
	}
	
	
	public static String getUpdateFileDownloadStatusSql(String id, int status)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_FIlE_STATUS ); sb.append(" = "); sb.append( status ); 
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = '"); sb.append( id ); sb.append("'");
		return sb.toString();
	}
	
	public static String getUpdateImageDownloadStatusSql(String id, int status)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_IMAGE_STATUS ); sb.append(" = "); sb.append( status ); 
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = '"); sb.append( id ); sb.append("'");
		return sb.toString();
	}
	
	public static String getUpdateImagePathSql(String id, String type, String imagePath){
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_STORE_PATH ); sb.append(" = '"); sb.append( imagePath ); sb.append("'");
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = '"); sb.append( id );  sb.append( "' ");
		return sb.toString();
	}
	
	
	public static String getSelectImcompletedSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM "); sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_FIlE_STATUS ); sb.append(" < 2 OR "); 
		sb.append( S_IMAGE_STATUS );  sb.append( "< 2");
		return sb.toString();
	}
	public static String getSelectFirstImcompletedSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM "); sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_FIlE_STATUS ); sb.append(" < 2 OR "); 
		sb.append( S_IMAGE_STATUS );  sb.append( "< 2 LIMIT 1");
		return sb.toString();
	}
	
	
	public static String getSelectItemByIdSql(String id){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM "); sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = '"); sb.append( id );  sb.append( "' ");
		return sb.toString();
	}
	
	public static String getDeleteSql(String id){
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "); sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = '"); 
		sb.append( id );  sb.append( "'");
		return sb.toString();
	}
	
	public static boolean isCheckSumColumnExisted(SQLiteDatabase db){
		String sql = "SELECT " + S_CHECKSUM + " FROM " + S_TABLE_NAME;
		try{
			db.rawQuery(sql, null);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static boolean isItemIdColumnExisted(SQLiteDatabase db){
		String sql = "SELECT " + S_ID + " FROM " + S_TABLE_NAME;
		try{
			db.rawQuery(sql, null);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static boolean isPackageNameColumnExisted(SQLiteDatabase db){
		String sql = "SELECT " + S_PACKAGE_NAME + " FROM " + S_TABLE_NAME;
		try{
			db.rawQuery(sql, null);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	public static void dropTable(SQLiteDatabase db){
		String sql = "DROP TABLE " + S_TABLE_NAME;
		try{
			db.execSQL(sql);
		}catch(Exception e){
			Log.e(S_TABLE_NAME, e.toString());
		}
		
	}
	
	public static String getDeleteItemByIdSql(String id)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "); sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = '"); sb.append( id ); sb.append("'");
		return sb.toString();
	}
	
}
