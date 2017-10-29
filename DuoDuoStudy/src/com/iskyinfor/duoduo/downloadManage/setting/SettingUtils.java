package com.iskyinfor.duoduo.downloadManage.setting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * <pre>
 * 暂时用来统计
 * </pre>
 * 
 * @author pKF29007
 * 
 */
public class SettingUtils {
	// public static int DEFAULT_MAX_RUNING_ITEM_COUNT = 3 ;

	/**
	 * 最多下载任务数
	 */
	public static int MAX_RUNING_ITEM_COUNT = 3;
	/**
	 * 下载失败尝试连接次数
	 */
	public static int MAX_RETRY_COUNT = 3;

	/**
	 * SD卡的基本保存路径
	 */
	public static String DOWNLOAD_SAVE_BASE_DIR = "duoduo_download";

	/**
	 * 重试时间
	 */
	public static int BASE_RETRY_TIME = 10;

	/**
	 * HTTP下载前的链接嗅探次数
	 */
	public static int DEFAULT_SNIFFER_COUNT = 3;

	/**
	 * 下载任务的最小刷新时间
	 */
	public static int MIN_REFRUSH_TIME = 2000;

	/**
	 * 下载任务的最小刷新大小
	 */
	public static int MIN_REFRUSH_SIZE = 102400;


	/**
	 * 图片类型下载保存路径
	 */
	public static String SAVE_IMAGE_DIR = "image";

	/**
	 * 音频类型下载保存路径
	 */
	public static String SAVE_AUDIO_DIR = "audio";

	/**
	 * 视频类型下载保存路径
	 */
	public static String SAVE_VIDEO_DIR = "video";

	/**
	 * 文本类型下载保存路径
	 */
	public static String SAVE_TEXT_DIR = "text";

	/**
	 * 其他类型下载保存路径
	 */
	public static String SAVE_OTHER_DIR = "other";

	/**
	 * 应用类型下载保存路径
	 */
	public static String SAVE_APPLICATION_DIR = "bookfile";

	/**
	 * 临时文件的文件后缀
	 */
	public static String TEMP_FIlE_SUFFIX = ".dstmp";

	/**
	 * 下载缓存文件的大小
	 */
	public static int DOWNLOAD_CACHE_SIZE = 1024;

	/**
	 * 数据库最大保存记录数
	 */
	public static int MAX_DOWNLOADS = 200;
	/**
	 * 通知类型
	 */
	public static String NoticType = "通知";

	private  final static String SP_NAME="com.huawei.appmarket_preferences";
	private  final static  int SP_MODLE=Activity.MODE_PRIVATE;
	
	/**
	 *TODO 设定可执行的下载的网络类型
	 * 
	 * 比如Wifi ，GPRS均可用， 还是Wifi可用。 还是GPRS可用
	 */

	/**
	 * TODO 配置点击终止的事件 正在下载 进入 正在下载界面 ; 下载完成 进入 下载文件 还是打开文件 可配置;
	 */

	public static void updateSettingValue(Context con) {
		
			upSetValue(con,"download_sava_base_dir","/sdcard/downloads",DOWNLOAD_SAVE_BASE_DIR);
			upSetValue(con,"save_application_dir","/sdcard/downloads/application",SAVE_APPLICATION_DIR);
			upSetValue(con,"save_image_dir","/sdcard/downloads/image",SAVE_IMAGE_DIR);
			upSetValue(con,"save_audio_dir","/sdcard/downloads/audio",SAVE_AUDIO_DIR);
			upSetValue(con,"save_video_dir","/sdcard/downloads/video",SAVE_VIDEO_DIR);
			upSetValue(con,"save_text_dir","/sdcard/downloads/text",SAVE_TEXT_DIR);
			upSetValue(con,"save_other_dir","/sdcard/downloads/other",SAVE_OTHER_DIR);
			
			upSetValue(con,"temp_file_suffix",".dstmp",TEMP_FIlE_SUFFIX);
			upSetValue(con,"max_down_no",6,MAX_RUNING_ITEM_COUNT);
			upSetValue(con,"max_retry_no",3,MAX_RETRY_COUNT);
			upSetValue(con,"base_retry_time",10,BASE_RETRY_TIME);
			upSetValue(con,"default_suiffer_count",3,DEFAULT_SNIFFER_COUNT);
			upSetValue(con,"downloas_cache_size",1024,DOWNLOAD_CACHE_SIZE);
			upSetValue(con,"max_downloads",200,MAX_DOWNLOADS);
			upSetValue(con,"download_notify","通知,广播",NoticType);
			upSetValue(con,"min_refrush_time",2000,MIN_REFRUSH_TIME);
			upSetValue(con,"min_refrush_size",1024,MIN_REFRUSH_SIZE);
		}
	/**
	 * 更新设置的值
	 * @param con 上下文
	 * @param preKey SharedPreferences的key
	 * @param defalutValue 默认值
	 * @param changeValue  需要更新的值
	 */
	public static void upSetValue(Context con,String preKey,String defalutValue,String changeValue){
		SharedPreferences ms=con.getSharedPreferences(SP_NAME, SP_MODLE);
		changeValue=ms.getString(preKey, defalutValue);
	}
	public static void upSetValue(Context con,String preKey,int defalutValue,int changeValue){
		SharedPreferences ms=con.getSharedPreferences(SP_NAME, SP_MODLE);
		changeValue=ms.getInt(preKey, defalutValue);
	}
}
