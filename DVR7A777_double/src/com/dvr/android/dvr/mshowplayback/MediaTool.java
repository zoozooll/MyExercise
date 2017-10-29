package com.dvr.android.dvr.mshowplayback;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;

import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.util.SDcardManager;

public class MediaTool
{
	private static final String TAG = "MediaTool";
	
	private static final boolean DEBUG = false;
	
    public static final int MEDIA_IMAGE_TYPE = 2;
    public static final int MEDIA_VIDEO_TYPE = 0;
    public static final int MEDIA_MUSIC_TYPE = 1;
    public static String whereClause = MediaStore.Video.Media.DATA + " like '" + Config.SAVE_PATH + "%'";

    private static String[] mediaColumns = new String[] {MediaStore.Video.Media.DATA,
        MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE, MediaStore.Video.Media.MIME_TYPE
        ,MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.SIZE,MediaStore.Video.Media.DURATION 
        ,MediaStore.Video.Media.TAGS };
    
    private static String[] imageColumns = new String[] {MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.MIME_TYPE
        , MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.SIZE,MediaStore.Images.Media.DESCRIPTION};

    //hanJ
    private static void debug(String log) {
    	if (DEBUG) {
    		android.util.Log.e(TAG, log);
    	}
    }
    
    public static ArrayList<FileDes> getAllVideos(ArrayList<FileDes> fileDess, Context context, boolean bVideoLock){
    	boolean bIsSDcardExist;
        fileDess.clear();
        Cursor cursor =
            context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns,
                whereClause,
                null,
                null);
        if(cursor!=null){
	        System.out.println("buweinull");
	        System.out.println(cursor.getCount());
        }
        
        if (!SDcardManager.checkSDCardMount())
        {
        	bIsSDcardExist = false;
		}
        else
        {
        	bIsSDcardExist = true;
        }
        
        if (cursor.moveToFirst()){
            do
            {   Log.i("PLJ", "MediaTool---->getAllVideos111:"+"-------->:"+cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.TITLE)));
                FileDes info = new FileDes();
                
                info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
                
                if(bIsSDcardExist)
                {
                	File file = new File(info.filePath);
                 	if(file.exists() == false)
                 	{
                 		//不存在把数据库里面的文件删除
                         info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                 		 context.getContentResolver()
                         .delete(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, info.id),
                             null,
                             null);
                 		continue;
                 	}  
                }
                
                String canDelete = cursor.getString(cursor.getColumnIndexOrThrow(VideoColumns.TAGS));
                info.canDelete = canDelete != null && canDelete.equals("true");               
                if(bVideoLock)  //找不允许删除
                {
                	if(info.canDelete)
                	{
                		continue;
                	}
                }
                else
                {
                	if(!info.canDelete)
                	{
                		continue;
                	}
                }
                
                info.mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.MIME_TYPE));
                info.fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.TITLE));
                info.isDir = false;
                info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                info.disname = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
                info.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaColumns.SIZE));
                info.duration = cursor.getLong(cursor.getColumnIndexOrThrow(VideoColumns.DURATION));               
                info.fileType = FileDes.FILE_TYPE_VIDEO; Log.i("PLJ", "MediaTool---->getAllVideos222:"+"-------->:"+info.fileName);
                //debug("getView fileName = " + info.fileName);
                fileDess.add(info);
            }
            while (cursor.moveToNext());
        }
        Collections.sort(fileDess, new FilePathComparator());
        cursor.close();
        return fileDess;
    }
    /**
     * 获取�?��图片
     * @param fileDess
     * @param context
     * @return
     */
    public static ArrayList<FileDes> getAllImage(ArrayList<FileDes> fileDess, Context context)
    {
    	boolean bIsSDcardExist;
        fileDess.clear();
        // 查询出信�?
        Cursor cursor =
            context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns,
                whereClause,
                null,
                null);
        //我猜当没有sdcard�? cusor为null  当有sdcard 无视频时 cusor不为null cusor的个数为0

        if (!SDcardManager.checkSDCardMount())
        {
        	bIsSDcardExist = false;
		}
        else
        {
        	bIsSDcardExist = true;
        }
        
        if (cursor.moveToFirst()){
            do {
                FileDes info = new FileDes();
                info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
                
                if(bIsSDcardExist)
                {
                	File file = new File(info.filePath);
                	if(file.exists() == false)
                	{
                		//不存在把数据库里面的文件删除
                        info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                		context.getContentResolver()
                        .delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, info.id),
                            null,
                            null);
                		continue;
                	} 
                }                              
                
                info.mimeType =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.MIME_TYPE));
                info.fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.TITLE));
                info.isDir = false;
                info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                info.disname = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME));
                info.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaColumns.SIZE));
                info.discript = cursor.getString(cursor.getColumnIndexOrThrow(ImageColumns.DESCRIPTION));
                info.fileType = FileDes.FILE_TYPE_IMAGE;
                //info.fileIcon = FileIntentInfo.getFileIcon(info.filePath);
                fileDess.add(info);
            }
            while (cursor.moveToNext());
        }
        else{
        }
        Collections.sort(fileDess, new FilePathComparator());
        cursor.close();
        return fileDess;
    }
    /**
     * 删除�?��媒体记录 或图片，或视频，或音�?
     * 
     * @param id id
     * @param type 媒体类型
     */
    public static void deleteMedia(long id, int type, Context context){
        switch (type)
        {
            // 图片
            case MEDIA_IMAGE_TYPE:
                context.getContentResolver()
                    .delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id),
                        null,
                        null);
                break;
           
            // 视频
            case MEDIA_VIDEO_TYPE:
            
                context.getContentResolver()
                    .delete(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id),
                        null,
                        null);
                break;
        }
    }
    
    /**
     * 删除�?��媒体记录 或图片，或视频，或音�?
     * 
     * @param id id
     * @param type 媒体类型
     */
    public static int deleteMedia_from_path(String filePath, int type, Context context) {
        int nRet = 0;
        switch (type) {
        // 图片
        case MEDIA_IMAGE_TYPE: {
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    mediaColumns, whereClause, null, null);

            if (cursor.moveToFirst()) {
                do {
                    FileDes info = new FileDes();

                    info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
                    info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));

                    if (info.filePath == filePath) {
                        context.getContentResolver().delete(
                                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, info.id),
                                null, null);

                        File deleteFile = new File(filePath);
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                        }
                        nRet = 1;
                        break;
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
            break;
        // 视频
        case MEDIA_VIDEO_TYPE: {
            Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    mediaColumns, MediaColumns.DATA + "=?" , new String[]{filePath} , null); //whereClause

            if (cursor.moveToFirst()) {
                //do {
                    FileDes info = new FileDes();
                    info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
                    info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));

                    if (info.filePath.equalsIgnoreCase(filePath)) {
                        String canDelete = cursor.getString(cursor.getColumnIndexOrThrow(VideoColumns.TAGS));
                        info.canDelete = canDelete != null && canDelete.equals("true");
                        if (info.canDelete) {
                            context.getContentResolver().delete(
                                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, info.id),
                                    null, null);
                            File deleteFile = new File(filePath);
                            if (deleteFile.exists()) {
                            	nRet = 1;
                                deleteFile.delete();
                            }
                        } else {
                            nRet = 2; // 数据库存在这条记录，但是不能删除
                        }
                        break;
                    }
                //} while (cursor.moveToNext());
            }

            cursor.close();
        }

            break;
        }

        return nRet;
    }
    
    public static void deleteLockMedia(Context context){
    	Cursor cursorLock = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, MediaStore.Video.Media.TAGS + "=?" , new String[]{ "false" } , null);
    	if(cursorLock.getCount() > 10){
    		int i=0;  int forcount = cursorLock.getCount() - 10;
	    	if (cursorLock.moveToFirst()) {
	            do {
	            	String strPath = cursorLock.getString(cursorLock.getColumnIndexOrThrow(MediaColumns.DATA));
	                if(forcount > i){
		                File deleteFile = new File(strPath);
	                    if (deleteFile.exists()) {
	                        deleteFile.delete(); i++;
	                    }
	                }else{
	                	break;
	                }
	            } while (cursorLock.moveToNext());
	        }
    	}

    	cursorLock.close();
    }

    public static void deleteAllMedia(int type, Context context){
        switch (type)
        {
            // 图片
            case MEDIA_IMAGE_TYPE:
            	/*context.getContentResolver()
                    .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        whereClause,
                        null);*/
            {
            	Cursor cursor =
                        context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            mediaColumns,
                            whereClause,
                            null,
                            null);
            	
		    	if (cursor.moveToFirst()){
		            do
		            {
		                FileDes info = new FileDes();
		                
		                info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
		                info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
		                
		                context.getContentResolver()
	                    .delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, info.id),
	                        null,
	                        null);
	
	                	File deleteFile = new File(info.filePath);
	                	if(deleteFile.exists())
	                	{
	                		deleteFile.delete();
	                	}
		            }
		            while (cursor.moveToNext());
		        }
		    	
		    	cursor.close();
		    }
            break;
           
            // 视频
            case MEDIA_VIDEO_TYPE:
            	// URI 路径 :  content://media/external/video/media
            	// whereClause  	_data like '/mnt/sdcard/DVR%'	
                /*context.getContentResolver()
                    .delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        whereClause,
                        null);*/
            {
            	Cursor cursor =
                        context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            mediaColumns,
                            whereClause,
                            null,
                            null);
            	
		    	if (cursor.moveToFirst()){
		            do
		            {
		                FileDes info = new FileDes();
		                
		                info.id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
		                context.getContentResolver()
                        .delete(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, info.id),
                            null,
                            null);
                    	//
                        info.filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
                    	File deleteFile = new File(info.filePath);
                    	if(deleteFile.exists())
                    	{
                    		deleteFile.delete();
                    	}
		            }
		            while (cursor.moveToNext());
		        }
		    	
		    	cursor.close();
            }
            Log.d("pz", "---MediaStore.Video.Media.EXTERNAL_CONTENT_URI--- : " + MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            break;
        }
    }
}
