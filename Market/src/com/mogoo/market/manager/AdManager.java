package com.mogoo.market.manager;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告业务管理类
 * 
 * @author 张永辉
 * @Date 2012-2-9
 */
public class AdManager {
	/**
	 * 取得广告地址
	 */
	public static String URL_AD_URL = "/getAppPosition.action";

	/**
	 * 服务器应用上下文名称
	 */
	private static final String SERVER_AD = "/AD";

	/**
	 * 应用ID
	 */
	public static final String PARAM_APPID = "appId";
	/**
	 * 广告位置ID
	 */
	public static final String PARAM_POSITION_ID = "positionId";
	/**
	 * 广告ID
	 */
	public static final String PARAM_AD_ID = "adId";

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

	/** 商城版本 */
	public static final String PARAM_VS = "vs";
	/**
	 * 广告类型
	 */
	// 列表
	public static final int OPEN_TYPE_LIST = 0;
	// 详情
	public static final int OPEN_TYPE_DETAIL = 1;

	private static AdManager instance;

	private AdManager() {
	}

	/**
	 * 获取实例
	 * 
	 * @author 张永辉
	 * @date 2012-2-9
	 * @return
	 */
	public static AdManager getInstance() {
		if (instance == null) {
			instance = new AdManager();
		}
		return instance;
	}

	/**
	 * 取得广告位地址
	 * 
	 * @author 张永辉
	 * @date 2012-2-10
	 * @param uid
	 *            设置ID
	 * @param akey
	 *            鉴权ID
	 * @param aid
	 *            用户ID
	 * @param appId
	 *            应用ID
	 * @param serverAddr
	 *            服务器地址，广告服务器和MAS服务器共用一个服务器地址,但属于服务器中不同的应用,
	 *            例如：服务器地址为http://192.168.0.177:8088,
	 *            则MAS服务器的地址为：http://192.168.0.177:8088/MAS，
	 *            广告服务器的地址为：http://192.168.0.177:8088/AD.
	 * @return
	 */
	public String getAdPositionUrl(String serverAddr, String uid, String akey,
			String aid, int appId, String positionId) {
		String url = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		putBaseParamPair(paramMap, uid, akey, aid);
		paramMap.put(PARAM_APPID, appId + "");
		paramMap.put(PARAM_POSITION_ID, positionId);

		url = getADServer(serverAddr) + URL_AD_URL + createParamPair(paramMap);

		return url;
	}

	/**
	 * 
	 * 取得广告位地址
	 * @date 2012-2-10
	 * @param uid 设置ID
	 * @param akey 鉴权ID
	 * @param aid 用户ID
	 * @param appId 应用ID
	 * @param serverAddr
	 *            服务器地址，广告服务器和MAS服务器共用一个服务器地址,但属于服务器中不同的应用,
	 *            例如：服务器地址为http://192.168.0.177:8088,
	 *            则MAS服务器的地址为：http://192.168.0.177:8088/MAS，
	 *            广告服务器的地址为：http://192.168.0.177:8088/AD.
	 */
	public String getAdPositionUrl(String serverAddr, String uid, String akey,
			String aid, int appId, String positionId, String seltVersion) {
		String url = null;

		Map<String, String> paramMap = new HashMap<String, String>();
		putBaseParamPair(paramMap, uid, akey, aid);
		paramMap.put(PARAM_APPID, appId + "");
		paramMap.put(PARAM_POSITION_ID, positionId);
		paramMap.put(PARAM_VS, seltVersion);

		url = getADServer(serverAddr) + URL_AD_URL + createParamPair(paramMap);

		return url;
	}
	
	/**
	 * 创键广告点击的URL地址
	 * 
	 * @author 张永辉
	 * @date 2012-2-10
	 * @param url
	 * @param uid
	 * @param akey
	 * @param aid
	 * @param appId
	 * @param postionId
	 * @param adId
	 * @return
	 */
	// public String buildAdClickUrl(String url,String uid,String akey,
	// String aid,int appId,String postionId,String adId){
	//
	// Map<String,String> paramMap = new HashMap<String, String>() ;
	// putBaseParamPair(paramMap, uid, akey, aid) ;
	// paramMap.put(PARAM_APPID, appId+"") ;
	// paramMap.put(PARAM_POSITION_ID, postionId) ;
	// paramMap.put(PARAM_AD_ID, adId) ;
	//
	// return url + createParamPair(paramMap) ;
	// }

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

	private static void putBaseParamPair(Map<String, String> paramMap,
			String uid, String akey, String aid) {
		paramMap.put(PARAM_AKEY, akey);
		paramMap.put(PARAM_UID, uid);
		paramMap.put(PARAM_AID, aid);
	}

	/**
	 * 取得AD服务器地址
	 * 
	 * @author 张永辉
	 * @date 2011-12-20
	 * @return
	 */
	public static String getADServer(String serverAddr) {
		return serverAddr + SERVER_AD;
	}

}
