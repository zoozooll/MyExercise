package com.dvr.android.dvr.util;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.net.Uri;

import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.bean.VideoInfo;

/**
 * 对循环视频保存目录下的视频文件和信息xml文件进行处理
 */
public class LoopManager {
	//循环目录
	private static File DIRECTORY = new File(Config.SAVE_LOOP_PATH+"/");
	//循环目录下的视频信息文件集合
	private static ArrayList<File> mInfoFiles = new ArrayList<File>();
	//数据库中视频文件的信息集�?
	private static ArrayList<VideoInfo> mVideoInfos = new ArrayList<VideoInfo>();
	
	//获取循环目录下的可删除录像集合与信息文件集合
	public static void get(ContentResolver cr) {
		if (!DIRECTORY.exists()) {
			DIRECTORY.mkdir();
		} else {
			mInfoFiles.clear();
			mVideoInfos.clear();
			//到数据库中查找可删除的循环视频文�?
	    	ArrayList<VideoInfo> videoInfos = mVideoInfos = VideoManager.quearyLoopVideo(cr, true);
	    	//记录可删除的循环视频对应的xml文件
	    	int size = videoInfos.size();
	    	if (size <= 0) {
	    		return;
	    	}
	    	String videoName = null;
	    	int index = -1;
	    	File infoFile;
	    	for (int i = 0 ; i < size; i++) {
	    		videoName = videoInfos.get(i).getVideoName();
	    		index = videoName.indexOf(".");
	    		if (index <= 0) {
	    			continue;
	    		}
	    		infoFile = new File(Config.SAVE_LOOP_PATH, videoName.substring(0, index) + Config.INFO_SUFFIX);
	    		if (infoFile.exists()) {
	    			mInfoFiles.add(infoFile);
	    		}
	    	}
		}
	}
	
	//删除可删除录像的数据库数据及信息文件
	public static void delete(ContentResolver cr) {
		//删除数据库对应数�?
		final ArrayList<VideoInfo> videoInfos = mVideoInfos;
		if (videoInfos != null && videoInfos.size() > 0) {
			int size = videoInfos.size();
			for (int i = 0; i < size; i++) {
				Uri uri = videoInfos.get(i).getUri();
				if (uri != null) {
					VideoManager.deleteVideo(cr, uri);
				}
			}
			videoInfos.clear();
		}
		//删除对应用的xml文件
		final ArrayList<File> infoFiles = mInfoFiles;
		if (infoFiles != null && infoFiles.size() > 0) {
			int size = infoFiles.size();
			for (int i = 0; i < size; i++) {
				File file = infoFiles.get(i);
				if (file != null && file.exists()) {
					file.delete();
				}
			}
			infoFiles.clear();
		}
	}
	
	//添加可删除录像的信息文件及向数据库中添加数据
	public static void add(ContentResolver cr, File infoFile, String videoName, Uri uri) {
		//添加信息文件
		if (infoFile != null && infoFile.exists()) {
			mInfoFiles.add(infoFile);
		}
		//添加对应录像文件
		if (uri != null) {
			mVideoInfos.add(new VideoInfo(uri, true, videoName));
		}
	}
}
