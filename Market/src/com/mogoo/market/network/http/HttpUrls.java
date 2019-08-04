package com.mogoo.market.network.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.mogoo.market.MarketApplication;
import com.mogoo.market.network.IBEManager;

/**
 * 
 * @author 张永辉
 * @date 2011-10-27
 */
public class HttpUrls {
	/**
	 * 请求参数列表
	 */
	/**
	 * akey
	 */
	public static final String PARAM_AKEY = "akey";
	/**
	 * uid
	 */
	public static final String PARAM_UID = "uid";
	/**
	 * aid
	 */
	public static final String PARAM_AID = "aid";
	/**
	 * appid
	 */
	public static final String PARAM_APPID = "appid";
	/**
	 * apkid
	 */
	public static final String PARAM_APKID = "apkId";

	/**
	 * MAS服务器地址
	 */
	private static final String masServerUrl;

	    static {
		if (MarketApplication.debug)
			masServerUrl = "http://192.168.10.5:9000";
			//masServerUrl = "http://test.imogoo.cn:8088/Store";
		else
			masServerUrl = IBEManager.getInstance().getMasServer();
                        //masServerUrl ="http://test.htw.cc:9000/MAS";
	}

	/**
	 * 服务器接口地址列表
	 */
	// 推荐列表
	public static String URL_RECOMMEND_APP_LIST = masServerUrl
			+ "/Store/recommend.do";
	// 最新列表
	public static String URL_LATEST_APP_LIST = masServerUrl
			+ "/Store/latest.do";
	// 必备列表
	public static String URL_NECESSARY_APP_LIST = masServerUrl
			+ "/Store/necessary.do";
	
	//2.0搜索列表
	public static String URL_SEARCH_APP_LIST = masServerUrl + "/Store/newsearch.do";

	// 软件更新
	public static String URL_APP_UPDATES = masServerUrl + "/Store/getUpdates.do";

	// 热关键词
	public static String URL_HOT_KEY_LIST = masServerUrl + "/Store/hotKeyList.do";

	// 上传apk信息
	public static String URL_UPLOAD_APP_LIST = masServerUrl
			+ "/Store/uploadAppList.do";

	// 软件详情
	public static String URL_APP_DETAILS = masServerUrl + "/Store/introduction.do";
	// 提交评论
	public static String URL_POST_COMMENT = masServerUrl + "/Store/insertComment.do" ;
	// 获取评论
	public static String URL_COMMENTS_LIST = masServerUrl + "/Store/getComments.do" ;
		
	// 专题
	public static String URL_TOPIC_LIST = masServerUrl + "/Store/gettopic.do";
	// 应用
	public static String URL_APPS_LIST = masServerUrl + "/Store/getapp.do";
	// 游戏
	public static String URL_GAME_LIST = masServerUrl + "/Store/getgame.do";
	// 专题详细
	public static String URL_TOPIC_INFO = masServerUrl + "/Store/gettopicinfo.do";
	// 游戏及应用详细
	public static String URL_CHILD_CATE_INFO = masServerUrl + "/Store/getclassinfo.do";
	
	// 热门排行
	public static String URL_TOP_HOT_LIST = masServerUrl + "/Store/gethot.do";
	// 应用排行
	public static String URL_TOP_APPS_LIST = masServerUrl + "/Store/getapprank.do";
	// 游戏排行
	public static String URL_TOP_GAME_LIST = masServerUrl + "/Store/getgamerank.do";
	
	// 最新应用
	public static String URL_NEWEST_APPS_LIST = masServerUrl + "/Store/newappinfo.do";
	// 最新游戏
	public static String URL_NEWEST_GAME_LIST = masServerUrl + "/Store/newgameinfo.do";
	
	// IBE不存在通知
	public static String URL_IBE_NO_EXIST = masServerUrl + "/Conf/userReplaceApp.do";
	/**
	 * 取得MAS服务器地址
	 * 
	 * @author 张永辉
	 * @date 2011-12-24
	 * @return
	 */
	public static String getMasServerUrl() {
		return masServerUrl;
	}

	/**
	 * 取得标配的参数对
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @return
	 */
	public static Map<String, String> getBaseParamPair() {
		Map<String, String> baseParamMap = new HashMap<String, String>();
		baseParamMap.put(PARAM_AKEY, IBEManager.getInstance().getAKey());
		baseParamMap.put(PARAM_UID, IBEManager.getInstance().getUid());
		baseParamMap.put(PARAM_AID, IBEManager.getInstance().getAid());
		return baseParamMap;
	}

	/**
	 * 生成参数对，用于GET方式
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @param paramMap
	 * @return
	 */
	public static String createParamPair(Map<String, String> paramMap) {
		if (paramMap == null) {
			throw new IllegalArgumentException("ParamMap is null .");
		}

		StringBuilder sb = new StringBuilder();
		sb.append("?");
		for (String key : paramMap.keySet()) {
			sb.append(key).append("=").append(paramMap.get(key)).append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 生成参数对，用于POST方式
	 * 
	 * @author 张永辉
	 * @date 2011-11-14
	 * @param paramMap
	 * @return
	 */
	public static List<NameValuePair> createNameValuePairs(
			Map<String, String> paramMap) {
		if (paramMap == null) {
			throw new IllegalArgumentException("ParamMap is null .");
		}
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (String key : paramMap.keySet()) {
			pairs.add(new BasicNameValuePair(key, paramMap.get(key)));
		}
		return pairs;
	}
	
	public static String createBaseUrlWithPairs(String baseUrl) {
		return baseUrl + createParamPair(getBaseParamPair());
	}
	
	public static String createBaseUrlWithExtendPairs(String baseUrl, Map<String, String> paramMap) {
		return baseUrl + createParamPair(paramMap);
	}
	
	public static String createBaseUrlWithBaseParamAndExtendPairs(String baseUrl,Map<String, String> paramMap) {
		paramMap.putAll(getBaseParamPair());
		return createBaseUrlWithExtendPairs(baseUrl, paramMap);
	}
}
