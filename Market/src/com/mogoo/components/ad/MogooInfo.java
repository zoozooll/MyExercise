package com.mogoo.components.ad;

import com.mogoo.components.ad.utils.FileUtil;

import android.graphics.Color;
import android.util.Log;
import android.view.animation.Animation;

class MogooInfo
{
	/**
	 * 是否打印debug消息
	 */
	static boolean debug = false;

	static void Log(String str, String tag)
	{
		if (debug)
		{
			if (tag == null)
			{
				Log.d("MogooAd", "----------------" + str + "----------------");
			}
			else
			{
				Log.d(tag, "----------------" + str + "----------------");
			}
		}
	}

	/**
	 * 轮播的动画效果
	 */
	static Animation animation;

	// 缓存到SD卡上的广告图片目录名称
	static final String pitureFolder = "mogoo";
	private static FileUtil sdpath = new FileUtil();
	// 缓存到SD卡上的广告图片的具体路径
	static String picturepath = sdpath.getSDPath() + pitureFolder + "/";

	/**
	 * 文本广告的文字颜色
	 */
	static int textColor = Color.WHITE;
	/**
	 * 广告区域的背景色
	 */
	static int backgroundColor = 0xff000000;
	/**
	 * 广告区域的透明度
	 */
	static int backgroundTransparent = 255;

	/**
	 * 服务器地址
	 */
	// static final String base_url = "http://192.168.0.177:8088/";
	// static final String base_url =
	// "http://www.goodboyenglish.com/test";
	// static String base_url = null;
	/**
	 * 展示数据的URL
	 */
	static String url = null;

	/**
	 * 展示区域的高度
	 */
	public static int adHeight = -1;
	/**
	 * 展示区域宽度
	 */
	static int adWidth;

	/**
	 * 缺省的广告图片资源id,如R.drawable.abc
	 */
	static int DEFAULT_AD_PIC_RES_ID = 0;
	/**
	 * 是否为双行显示（每行显示两个，共四个）
	 */
	static boolean IS_DOUBLE_ROW = false;
	/**
	 * 双行显示时，展示位之间的空隙
	 */
	static int AD_PADING = 3;

	/**
	 * 可滑动的页数
	 */
	static int PAGE = 1;

	/**
	 * 请求服务器间隔时长(单位为:秒),默认为0，表示只请求一次服务器
	 */
	static int updateTime = 0;
	/**
	 * 轮播间隔时长(单位为:秒),默认为-1,表示由服务器返回的数据决定
	 */
	static int refreshTime = -1;

	/**
	 * 应用ID
	 */
	static String appId;
	/**
	 * 手机唯一标识
	 */
	static String uid;
	/**
	 * 鉴权akey
	 */
	static String akey;
	/**
	 * 账号Id
	 */
	static String aid;

	/**
	 * 广告文本字体大小,默认是18
	 */
	static int txtSize=18;
	/**
	 * 统计商务广告点击量时用到的IP地址<br>
	 * 假如统计接口为:http://192.168.0.177:8088/AD/sendAppAdvertise.action?appId=
	 * 4654644654&uid=546545&adId=12&aid=#&akey=asdf6545465<br>
	 * <br>
	 * 那么:应该设的IP地址为:http://192.168.0.177:8088
	 */
	static String statisticsIp = "http://192.168.0.177:8088/";
	
	/**
	 * add by csq:多页广告，自动翻页时间
	 */
	public static int autoSnapTime;

	static void resetValue()
	{
		animation = null;
		textColor = Color.WHITE;
		backgroundColor = 0xff000000;
		backgroundTransparent = 255;
		url = null;
		adHeight = -1;
		DEFAULT_AD_PIC_RES_ID = 0;
		IS_DOUBLE_ROW = false;
		AD_PADING = 5;
		//PAGE = 1;   //remove by csq:几个界面都调用同一个广告组件，切换时不能将页数复位
		updateTime = 0;
		refreshTime = -1;
	}

}
