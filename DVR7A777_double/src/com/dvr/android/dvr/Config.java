package com.dvr.android.dvr;

import android.os.Environment;

import com.dvr.android.dvr.file.PathHelper;

public class Config
{
    // 应用保存总路�?
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String EXCARD_PATH = "/mnt/sdcard2";
    public static final String SAVE_FLOADER = "/DVR";
    public static final String SDCARD_SAVE_PATH = SDCARD_PATH + SAVE_FLOADER;
    public static final String EXCARD_SAVE_PATH = EXCARD_PATH + SAVE_FLOADER;
    //public static String SAVE_PATH = SDCARD_SAVE_PATH;
    public static String SAVE_PATH = EXCARD_SAVE_PATH;
    public static boolean bSecondActivityCanBack = false;
    public static boolean bBackCameraExist = true; //liujie add 1009
    public static boolean isBoot = false;
    public static int isMicroVideoPass = 0;

    // 循环录制视频保存路径
    public static String SAVE_LOOP_PATH = "";//= "/" + "loop/";
    // 常规录制视频保存路径
    public static String SAVE_REGULAR_PATH = "/" + "regular/";
    // 照片保存路径
    public static String SAVE_CAPTURE_PATH = SAVE_PATH + "/" + "pic/";
    // 录制保存路径
    public static String SAVE_RECORD_PATH = SAVE_PATH + "/video/";
    // 录制保存路径
    public static String SAVE_MICRO_PATH = SAVE_PATH + "/micro/";

    // 常规录制保存视频时长
    public static final int REG_DURATION = 10 * 60 * 1000;
    /*
     * 录像相关画质参数 0 - audioBitRate 1 - audioChannels 2 - audioCodec 3 - audioSampleRate 4 - duration 5 - fileFormat 6 -
     * quality 7 - videoBitRate 8 - videoCodec 9 - videoFrameHeight 10 - videoFrameRate 11 - videoFrameWidth
     */
    /*public static final int[][] VIDEO_PROFILES = {
        {128000, 2, 3, 16000, 30, 1, 3, 10000000, 3, 480, 30, 864 },
        {128000, 2, 3, 16000, 60, 1, 1, 8334000, 3, 480, 30, 720 },
        {28500, 1, 2, 16000, 60, 1, 2, 2000000, 2, 288, 30, 352 },
        {12200, 1, 1, 8000, 30, 1, 0, 1000000 , 2, 144, 30, 176 } };*/
    public static final int[][] VIDEO_PROFILES = { 
        {128000, 2, 3, 16000, 30, 1, 3, 12500000, 3, 1080, 30, 1920 },   //no use
        {128000, 2, 3, 48000, 60, 1, 3, 12500000, 3, 1080, 30, 1920 },   //first
        {128000, 2, 3, 16000, 60, 1, 1, 10000000, 3, 720, 30, 1280 }, 
        {28500, 1, 2, 16000, 60, 1, 2, 5*1024*1024, 2, 480, 30, 640 }};

    // 视频保存文件扩展�?
    public static final String VIDEO_SUFFIX = ".mp4";// //".3gp";//
    public static final String VIDEO_SUFFIX_BACK = ".avi";
    // 拍照保存文件扩展�?
    public static final String IMAGE_SUFFIX = ".jpg";
    //加锁文件标志
    public static final String LOCK_FILE_SUFFIX = "_l";
    //前置摄像头标�?
    public static final String FRONT_CAMERA_SUFFIX = "-f";
    public static final String FRONT_CAMERA_FILE_SUFFIX = "f";
    //后置摄像头标�?
    public static final String BACK_CAMERA_SUFFIX = "-b";
    public static final String BACK_CAMERA_FILE_SUFFIX = "b";
    // 微视频文件扩展名
    public static final String FRONT_MICRO_SUFFIX = "-w";
    // 视频、照片信息文件扩展名
    public static final String INFO_SUFFIX = PathHelper.XML_SUFFIX;
    // 是否支持后台录制
    public static final boolean SUPPORT_BACKGROUND_RECORD = false;
    // 导航地图包名 (默认凯利�?
    public static final String[] NAVIGATION_CMP = {"cld.navi.mainframe", "cld.navi.mainframe.MainActivity" };
    // 判断SDcard可用空间的时间间�?
    public static final int CHECK_SDCARD_PERIOD = 10 * 1000;
    // SDcard可用空间临界�?，小于此值将进行提示
    public static final long MIN_AVAILABLE_SIZE = 200 * 1024 * 1024;
    public static final long MIN_AVAILABLE_SIZE_CAPTURE = 1 * 1024 * 1024;   //1M
    //
    public static final long SHOULD_DELETE_LOOP_FILE_SIZE = 200 * 1024 * 1024;
    
    public static final int MSG_ACTIVITY_BROADCASE_START = 300;
    public static final int MSG_ACTIVITY_BROADCASE_RESTART_SERVICE = MSG_ACTIVITY_BROADCASE_START;
    public static final int MSG_ACTIVITY_BROADCASE_STOP_ACTIVITY = MSG_ACTIVITY_BROADCASE_RESTART_SERVICE + 1;
    public static boolean mScreenIs800 = true;
    
    public static final int STATUS_IDLE = 11;
    public static final int DIALOG_GLOBAL_ID_START= 149;

    // global value
    public static boolean gbOpenSetting = false;
    public static boolean gbRecordStatus = false;
    public static boolean gbEnteredPlayList = false;

    // 小窗口的大小、位置
    public static final int SMALL_CAMERA_WINDOW_X = 35;
    public static final int SMALL_CAMERA_WINDOW_Y = 50;
    public static final int SMALL_CAMERA_WINDOW_WIDTH = 200;
    public static final int SMALL_CAMERA_WINDOW_HEIGHT = 200;
    // 主窗口的大小、位置
    public static final int MAIN_CAMERA_WINDOW_X = 0;
    public static final int MAIN_CAMERA_WINDOW_Y = 0;
    public static final int MAIN_CAMERA_WINDOW_WIDTH = 1024;
    public static final int MAIN_CAMERA_WINDOW_HEIGHT = 560;
    
    //avin 
    public static boolean AvinHomeBack = true;
}
