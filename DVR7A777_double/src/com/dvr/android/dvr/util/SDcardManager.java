package com.dvr.android.dvr.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.DVRBackService;
import com.dvr.android.dvr.bean.VideoFile;
import com.dvr.android.dvr.msetting.SettingBean;
import com.dvr.android.dvr.mshowplayback.MediaTool;
//import com.lidroid.xutils.DbUtils;
//import com.lidroid.xutils.db.sqlite.Selector;
//import com.lidroid.xutils.exception.DbException;

public class SDcardManager {
	
	public static  int DELETE_FILE_VIDEO = 0;
	public static int DELETE_FILE_PIC = 3;
	public static int nNeedDeleteFileMaxNums = 3;
	public static int nNeedDeleteFileNums = 3;

    /**
     * 检测SD卡挂载状态
     */
    public static boolean checkSDCardMount() {
        boolean mExtAvailable = false;//mExternalStorageAvailable
        String m_path = "/mnt/sdcard2";
        /*String m_path = Config.SAVE_PATH;
        boolean bl = m_path.contains("sdcard0");
        if (bl)
            m_path = "/mnt/sdcard";
        else
            m_path = "/mnt/sdcard2";*/

        File path = new File(m_path + Config.SAVE_FLOADER + "/");
        if (path.isFile()) {
            path.delete();
        }
        path.mkdirs();
        if (path.exists()) {
        	mExtAvailable = true;
        }
        return mExtAvailable;
    	/*boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    	return sdCardExist;*/
    }
    
    public static boolean checkDVRSDCard() {
        boolean mExtAvailable = false;//mExternalStorageAvailable
        String m_path = "/mnt/sdcard2";

        File path = new File(m_path + Config.SAVE_FLOADER + "/");  Log.i("PLJ", "SDcardManager---->checkDVRSDCard:"+path.getAbsolutePath()+"  "+path.exists());
        if (path.exists()) {
        	mExtAvailable = true;
        }
        return mExtAvailable;
    }

    public static String getExtensionName(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length() - 1))) {    
                return filename.substring(dot + 1);    
            }    
        }    
        return filename;    
    }

    private static boolean deleteFile(File file, int m_nDeletFileType, Context context) {
        boolean isSucceed = false;
        String fileAbsolutePath = file.getAbsolutePath();
        String fileName = file.getName();
        String strExt = getExtensionName(fileAbsolutePath);
        //  如果是xml，直接返回了
        if (strExt.equalsIgnoreCase("xml")) {
            return false;
        } else {
            boolean bmp4File = strExt.equalsIgnoreCase("mp4");
            boolean bAviFile = strExt.equalsIgnoreCase("avi");
            // 如果不是mp4或者avi，删除有可能报错
            if((bmp4File || bAviFile) == false) {
                Log.e("DVRSDcardManager", "---RecursionDeleteFile not mp4 file--- : " + fileAbsolutePath);  
                file.delete();
                nNeedDeleteFileNums--;
                return false;
            }

            // 文件名长度-4后大于15就不再去搜索数据库
            if ((fileName.length() - 4) > 16) {
                nNeedDeleteFileNums--;
                return false;
            }

            int nDeleteResult = MediaTool.deleteMedia_from_path(fileAbsolutePath, m_nDeletFileType, context);
            Log.e("DVRSDcardManager", "---nDeleteResult " + nDeleteResult + " " + file.getName());
            if (nDeleteResult == 1) { // 已经删除
                nNeedDeleteFileNums--;
                isSucceed = true;
                if (file.exists()) {
                    isSucceed = file.delete();
                }
                if (isSucceed && bmp4File) {
                    int indexExt = fileAbsolutePath.indexOf(".mp4");
                    String strXmlPath = fileAbsolutePath.substring(0, indexExt) + ".xml";
                    File deleteFile = new File(strXmlPath);
                    Log.e("DVRSDcardManager", "<<>>-------delete xml:" + deleteFile.delete());
                    nNeedDeleteFileNums--;
                }
            } else if(nDeleteResult == 0) { //数据库里面不存在这个文件
                //数据库里面不存在，数据库有可能破坏了，先判断是否可以删除
                if((fileName.length() - 4) == 16) {  //-4是减去后�?��不可删除文件名如f20130110112033_l  len=15
                    nNeedDeleteFileNums--;
                    isSucceed = file.delete();
                    if(isSucceed && bmp4File) {
                        int indexExt = fileAbsolutePath.indexOf(".mp4");
                        String strXmlPath = fileAbsolutePath.substring(0, indexExt) + ".xml";
                        File deleteFile = new File(strXmlPath);
                        if (deleteFile.exists()) {
                            Log.e("DVRSDcardManager", "<<>>-------delete xml:" + deleteFile.delete());
                            nNeedDeleteFileNums--;
                        }
                    }
                }
            } else if (nDeleteResult == 2) {
                nNeedDeleteFileNums--;
            }
            return isSucceed;
        }
    }

    public static void recursionDeleteFile(File file, int m_nDeletFileType, Context context){
    	if(Config.gbEnteredPlayList) { //如果进入回放列表，有可能访问数据库的时�?产生冲突 
    		return;
    	}

        if (file.isFile()) {
            if (nNeedDeleteFileNums > 0) {
                boolean isSucceed = deleteFile(file, m_nDeletFileType, context);
                Log.e("tag_tony", "<><>--del file :" + file.getPath() + " -succeed : " + isSucceed);
	            return;
        	}
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles(); // 得到当前文件夹下所有文件
            String dirPath = file.getAbsolutePath(); // 得到当前的路径
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            ArrayList<String> items = new ArrayList<String>();
            for (int i = 0; i < childFile.length; i++) {
                items.add(childFile[i].getName());
            }
            Collections.sort(items, String.CASE_INSENSITIVE_ORDER); // 按首字母排序
            for (int i = 0; i < childFile.length; i++) {
                if (nNeedDeleteFileNums > 0) {
                    String filePath = dirPath + "/" + items.get(i);
                    File deleteFile = new File(filePath);
                    recursionDeleteFile(deleteFile, m_nDeletFileType, context);
                }
            }

            childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static void recursionDeleteFilepenglj(File file, int m_nDeletFileType, Context context){
    	if(Config.gbEnteredPlayList) { //如果进入回放列表，有可能访问数据库的时�?产生冲突
    		return;
    	}

        if (file.isFile()) {
            if (nNeedDeleteFileNums > 0) {
                boolean isSucceed = deleteFilepenglj(file, m_nDeletFileType, context);
	            return;
        	}
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles(); // 得到当前文件夹下所有文件
            String dirPath = file.getAbsolutePath(); // 得到当前的路径
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            
            ArrayList<String> items = new ArrayList<String>();
            for (int i = 0; i < childFile.length; i++) {
                items.add(childFile[i].getName());
            }
            Collections.sort(items, String.CASE_INSENSITIVE_ORDER); // 按首字母排序
            for (int i = 0; i < childFile.length; i++) {
                if (nNeedDeleteFileNums > 0) {
                    String filePath = dirPath + "/" + items.get(i);
                    File deleteFile = new File(filePath);  Log.i("PLJ", "SDcardManager---->recursionDeleteFilepenglj000:"+filePath);
                    recursionDeleteFilepenglj(deleteFile, m_nDeletFileType, context);
                }else{
                	break;
                }
            }
        }
    }
    
    private static boolean deleteFilepenglj(File file, int m_nDeletFileType, Context context) {
        boolean isSucceed = false;
        String fileAbsolutePath = file.getAbsolutePath();
        String fileName = file.getName();
        String strExt = getExtensionName(fileAbsolutePath);
        boolean bmp4File = strExt.equalsIgnoreCase("mp4");
        boolean bAviFile = strExt.equalsIgnoreCase("avi");
        if(bmp4File || bAviFile){
            // 文件名长度-4后大于15就不再去搜索数据库
            if ((fileName.length() - 4) > 16) {
                return false;
            }

            int nDeleteResult = MediaTool.deleteMedia_from_path(fileAbsolutePath, m_nDeletFileType, context);
            
            if (nDeleteResult == 1) { Log.i("PLJ", "SDcardManager---->deleteFilepenglj111--11:"+nDeleteResult+"  "+fileAbsolutePath);// 已经删除
                nNeedDeleteFileNums--;
                if (bmp4File) {
                    int indexExt = fileAbsolutePath.indexOf(".mp4");
                    String strXmlPath = fileAbsolutePath.substring(0, indexExt) + ".xml";
                    File deleteFile = new File(strXmlPath); Log.i("PLJ", "SDcardManager---->deleteFilepenglj111--22:"+deleteFile.exists()+"  "+strXmlPath);
                    if (deleteFile.exists()) {
                        deleteFile.delete();
                    }
                }
            } else if(nDeleteResult == 0) { Log.i("PLJ", "SDcardManager---->deleteFilepenglj222--11:"+nDeleteResult+"  "+fileAbsolutePath);// 已经删除 //数据库里面不存在这个文件
                //数据库里面不存在，数据库有可能破坏了，先判断是否可以删除
                if((fileName.length() - 4) == 16) {  //-4是减去后�?��不可删除文件名如f20130110112033_l  len=15
                    if (file.exists()) {
                        isSucceed = file.delete();
                    }
                    if(isSucceed && bmp4File) {
                    	nNeedDeleteFileNums--;
                        int indexExt = fileAbsolutePath.indexOf(".mp4");
                        String strXmlPath = fileAbsolutePath.substring(0, indexExt) + ".xml";
                        File deleteFile = new File(strXmlPath);  Log.i("PLJ", "SDcardManager---->deleteFilepenglj222--22:"+deleteFile.exists()+"  "+strXmlPath);
                        if (deleteFile.exists()) {
                            deleteFile.delete();
                        }
                    }
                }
            } else if (nDeleteResult == 2) { Log.i("PLJ", "SDcardManager---->deleteFilepenglj333:"+nDeleteResult+"  "+fileAbsolutePath);// 已经删除
                //nNeedDeleteFileNums--;
            }
            return isSucceed;
        }else{
        	boolean tmpFile = strExt.equalsIgnoreCase("tmp");
            boolean datFile = strExt.equalsIgnoreCase("dat");
            if(tmpFile || datFile){
            	File delFile = new File(fileAbsolutePath);
                if (delFile.exists()) {
                	delFile.delete();
                }
            }
        	return false;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static int DeleteSdFile(int mnDeletFileType, Context context) {
        if (mnDeletFileType == DELETE_FILE_VIDEO) {
            File file = new File(Config.SAVE_RECORD_PATH);
            recursionDeleteFilepenglj(file, mnDeletFileType, context); //recursionDeleteFile(file, m_nDeletFileType, context);

//            if (videoFiles.size() > 0) {
//                recursionDeleteFile(new File(videoFiles.get(0)), m_nDeletFileType, context);
//                videoFiles.remove(0);
//            }
        } else if (mnDeletFileType == DELETE_FILE_PIC) {
            File file = new File(Config.SAVE_CAPTURE_PATH);
            recursionDeleteFile(file, mnDeletFileType, context);
        }
        return nNeedDeleteFileNums;
    }
    
    //penglj add
    public static void DeleteScratchFile() {
        File file = new File(Config.SAVE_RECORD_PATH);
        DeleteScratchFileSub(file);
    }
    public static void DeleteScratchFileSub(File file){
        if (file.isFile() && file.exists()) {
            if (nNeedDeleteFileNums > 0) {
            	String fileAbsolutePath = file.getAbsolutePath();
                String strExt = getExtensionName(fileAbsolutePath);
                boolean datFile = strExt.equalsIgnoreCase("dat");
                boolean tmpFile = strExt.equalsIgnoreCase("tmp");
                if(datFile || tmpFile){
                	file.delete();
                }
        	}
        } else if (file.isDirectory()) {
            File[] childFile = file.listFiles(); // 得到当前文件夹下所有文件
            String dirPath = file.getAbsolutePath(); // 得到当前的路径
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            
            ArrayList<String> items = new ArrayList<String>();
            for (int i = 0; i < childFile.length; i++) {
                items.add(childFile[i].getName());
            }
            Collections.sort(items, String.CASE_INSENSITIVE_ORDER); // 按首字母排序
            for (int i = 0; i < childFile.length; i++) {
                if (nNeedDeleteFileNums > 0) {
                    String filePath = dirPath + "/" + items.get(i);
                    File deleteFile = new File(filePath);
                    DeleteScratchFileSub(deleteFile);
                }else{
                	break;
                }
            }
        }
    }

    private static List<String> paths = new ArrayList<String>();
    private static List<String> names = new ArrayList<String>();
    private static List<String> absolutePaths = new ArrayList<String>();

    /**
     * 读取某个文件夹下的所有文件
     * 排序是按插入序列
     */
    public static void readfile(String filepath) {
        File file = new File(filepath);
        if (!file.isDirectory()) {
            paths.add(file.getPath());
            absolutePaths.add(file.getAbsolutePath());
            names.add(file.getName());
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                readfile(filepath + "/" + filelist[i]);
            }
        }
    }

    private static long getFlashAvailableMemorySize(String path) {
        StatFs stat = new StatFs(path);// (path.getPath());

        long blockSize = stat.getBlockSize(); // 每个block的大
        long availableBlocks = stat.getAvailableBlocks(); // 可用block的大�?

        return (availableBlocks * blockSize);
    }
    
    /**
     * �?��SD卡可用空间是否足够，临界值为Config.MIN_AVAILABLE_SIZE
     * bDealVideo = true表示当前进行录像的操�?
     */
    public static boolean checkSDCardAvailableSize(Context context, boolean bDealVideo) {
    	boolean bool = true;
    	MediaTool.deleteLockMedia(context);
        try {
        	String m_path = "/mnt/sdcard2";
            /*String m_path = Config.SAVE_PATH;
            boolean bl = m_path.contains("sdcard0");*/
            long lMinMemorySizeRequred;
            if (bDealVideo) {
            	lMinMemorySizeRequred = 1600 * 1024 * 1024;
                /*lMinMemorySizeRequred = Config.MIN_AVAILABLE_SIZE + 600 * 1024 * 1024;
                lMinMemorySizeRequred = lMinMemorySizeRequred * 2;*/
            } else {
                lMinMemorySizeRequred = Config.MIN_AVAILABLE_SIZE_CAPTURE;
            }

            /*if (bl)
                m_path = "/mnt/sdcard";
            else
                m_path = "/mnt/sdcard2";*/

            long lAvailableMemorySize = getFlashAvailableMemorySize(m_path);
            if (lAvailableMemorySize <= lMinMemorySizeRequred) {
            	while (lAvailableMemorySize <= lMinMemorySizeRequred){ Log.i("PLJ", "SDcardManager---->checkSDCardAvailableSize:"+lAvailableMemorySize+"  "+lMinMemorySizeRequred);
            		    int delInt = DeleteSdFile(DELETE_FILE_VIDEO, context);
            		    lAvailableMemorySize = getFlashAvailableMemorySize(m_path);
            		    
                        if (delInt < nNeedDeleteFileMaxNums) {
                        	nNeedDeleteFileNums = nNeedDeleteFileMaxNums;
                        	if (lAvailableMemorySize > lMinMemorySizeRequred){
                        		bool = true; break;
                            }
                        }else if (delInt == nNeedDeleteFileMaxNums){
                        	bool = false; break; //dvr没文件可删 直返
                        }
            	    };
            } else {
            	bool = true;
            }
        } catch (IllegalArgumentException e) {
        	bool = false;
        }
        return bool;
    }

    private static List<String> videoFiles = new ArrayList<String>();

    /**
     * 扫描SD卡里面指定目录的资源文件
     * */
    public static void scanFileByPath(String path) {
        videoFiles.clear();
        getVideoFiles(path);
        Collections.sort(videoFiles, String.CASE_INSENSITIVE_ORDER); // 按首字母排序
    }

    /**
     * 得到指定目录下所有视频文件
     * */
    private static void getVideoFiles(String path) {
        File file = new File(path);
        if (file.isFile() && (file.getName().endsWith(".mp4") || file.getName().endsWith(".avi")) && file.getName().length() == 18) {
            videoFiles.add(file.getAbsolutePath());
        } else {
            String[] files = file.list();
            if (files == null || files.length == 0) {
                file.delete();
                return;
            }
            for (int i = 0; i < files.length; i++) {
                getVideoFiles(file.getAbsolutePath() + "/" + files[i]);
            }
        }
    }
    
}
