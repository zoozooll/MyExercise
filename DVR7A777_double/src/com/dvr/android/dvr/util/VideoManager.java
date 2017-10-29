package com.dvr.android.dvr.util;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video;
import android.provider.MediaStore.Video.VideoColumns;

import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.bean.VideoInfo;

public class VideoManager {
	
	private static final String TAG = "VideoManager";
	
	private static final boolean DEBUG = false;
	
	private static final Uri STORAGE_URI = Video.Media.EXTERNAL_CONTENT_URI;
    
	//hanJ
	private static final String WHERE = Video.Media.DATA + " like '" + 
		Config.SAVE_RECORD_PATH + "%" + Config.SAVE_LOOP_PATH + "%'";
    
	private static final String[] COLUMNS = new String[] {
		Video.Media.DATA,         Video.Media._ID,  Video.Media.TITLE,    Video.Media.MIME_TYPE,
		Video.Media.DISPLAY_NAME, Video.Media.SIZE, Video.Media.DURATION, Video.Media.TAGS 
	};
	
	public static Uri addVideo(ContentResolver cr, String title, long dateTaken,long duration, File file, boolean canDelete) {
        String fileName = title + Config.VIDEO_SUFFIX;
        String filePath = file.getAbsolutePath();
        long size = file.length();
        
        ContentValues values = new ContentValues(8);//liujie modify 1104
        
        values.put(MediaColumns.TITLE, title);
        values.put(MediaColumns.DISPLAY_NAME, fileName);
        values.put(VideoColumns.DATE_TAKEN, dateTaken);
        values.put(VideoColumns.DURATION, duration);
        values.put(MediaColumns.MIME_TYPE, "video/mp4");//"video/3gpp");
        values.put(MediaColumns.DATA, filePath);
        values.put(MediaColumns.SIZE, size);
        values.put(VideoColumns.TAGS, canDelete ? "true" : "false");
        return cr.insert(STORAGE_URI, values);
	}

	//penglj_lockfile_20150601 start
	public static Uri addBackVideo(ContentResolver cr, String title, long dateTaken,long duration, File file, boolean canDelete) {
        String fileName = title + Config.VIDEO_SUFFIX;
        if(!canDelete){
        	fileName = title + Config.VIDEO_SUFFIX;//VIDEO_SUFFIX_BACK
        }
        String filePath = file.getAbsolutePath();
        long size = file.length();
        
        ContentValues values = new ContentValues(8);//liujie modify 1104
        
        values.put(Video.Media.TITLE, title);
        values.put(Video.Media.DISPLAY_NAME, fileName);
        values.put(Video.Media.DATE_TAKEN, dateTaken);
        values.put(Video.Media.DURATION, duration);
        if(canDelete){
        	values.put(Video.Media.MIME_TYPE, "video/mp4");
        }else{
        	values.put(Video.Media.MIME_TYPE, "video/avi");
        }
        values.put(Video.Media.DATA, filePath);
        values.put(Video.Media.SIZE, size);
        values.put(Video.Media.TAGS, canDelete ? "true" : "false");
        return cr.insert(STORAGE_URI, values);
	}//penglj_lockfile_20150601 end
	
	/**
	 * 获取循环目录下的视频
	 * @param tagCanDelete 按此标志位获取对应的视频集合
	 */
	public static ArrayList<VideoInfo> quearyLoopVideo(ContentResolver cr, boolean tagCanDelete) {
		debug("query video");
        ArrayList<VideoInfo> list = new ArrayList<VideoInfo>();
		Cursor cursor = cr.query(STORAGE_URI, COLUMNS, WHERE, null, null);
        long id = -1;
        Uri uri = null;
        String tags = null;
        boolean canDelete = false;
        String videoName = null;
		if(cursor!=null && cursor.moveToFirst()){
        	do {
        		id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        		uri = ContentUris.withAppendedId(STORAGE_URI, id);
        		tags = cursor.getString(cursor.getColumnIndexOrThrow(VideoColumns.TAGS)); 
        		canDelete = tags != null && tags.equals("true");
        		videoName = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
        		if (canDelete == tagCanDelete) {
        			list.add(new VideoInfo(uri, canDelete, videoName));
                }
        	}
        	while (cursor.moveToNext());
		}
        return list;
	}
	
	//hanJ
	public static void deleteOldestVideo(ContentResolver cr) {
		debug("deleteOldestVideo");
		Cursor cursor = cr.query(STORAGE_URI, COLUMNS, WHERE, null, null);
        long id = -1;
        Uri uri = null;
        if(cursor!=null && cursor.moveToFirst()) {
        	id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
        	uri = ContentUris.withAppendedId(STORAGE_URI, id);
        	String fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
        	deleteVideo(cr, uri);
        	//datasheet can not delete file ni external sd card
        	File deleteFile = new File(fileName);
        	if(deleteFile.exists())
        	{
        		deleteFile.delete();
        	}
        	int index = fileName.indexOf(".mp4");
        	if (index >= 0) {
        		deleteXmlFile(fileName.substring(0, index) + ".xml");
        	}
        }
        if (cursor != null) {
        	cursor.close();
        	cursor = null;
        }
	}
	
	public static void deleteXmlFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static void deleteVideo(ContentResolver cr, Uri deleteUri) {
		debug("deleteVideo");
		if (deleteUri != null) {
			cr.delete(deleteUri, null, null);
		}
	}
	
	public static void debug(String log) {
		if (DEBUG) {
			android.util.Log.e(TAG, log);
		}
	}
}
