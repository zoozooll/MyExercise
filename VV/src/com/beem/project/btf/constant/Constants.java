package com.beem.project.btf.constant;

public class Constants {
	public static final int AVAILABLE_FOR_CHAT_IDX = 0;
	public static final int AVAILABLE_IDX = 1;
	public static final int BUSY_IDX = 2;
	public static final int AWAY_IDX = 3;
	public static final int UNAVAILABLE_IDX = 4;
	public static final int DISCONNECTED_IDX = 5;
	public static final int SEX_POPUP = 1;
	public static final int SORT_POPUP = 2;
	public static final String BY_DISTANCE = "com.vv.distance";
	public static final String BY_CITY = "com.vv.city";
	public static final String BY_COLLEGE = "com.vv.colleage";
	public static final String TO_ADD_CONTACTS = "com.vv.addcontacts";
	public static final String TO_MANAGE_BLACKROSTER = "com.vv.blackroster.vcardinfo";
	public static final String ACTION_STANGER = "com.vv.stranger.vcardinfo";
	public static final String ACTION_BLACKROSTER = "com.vv.blackroster.vcardinfo";
	public static final int TO_REMOVE_BLACK_ROSTER = 100;
	public static final String ACTION_ALBUM_CREAT = "com.vv.album.creat";
	public static final String ACTION_ALBUM_AUTH_UPDATE = "com.vv.album.auth.update";
	public static final String ACTION_VVACCOUNT_REGISTRY_SUCCESS = "com.vv.account.regisry.success";
	public static final String ACTION_ALBUM_LIST_FROM_ORDER = "com.vv.other.album.list";
	public static final int XF_ADAPTER_ORDER_COMMENT = 1;
	public static final int XF_ADAPTER_TIME_FLY = 2;
	public static final int XF_ADAPTER_TRACE = 3;
	public static final int XF_ADAPTER_SHOW_OTHER_USER = 4;
	public static final int XF_ADAPTER_VV_ALL_IMAGES = 5;
	public static final int XF_ADAPTER_SHOW_OTHER_USER_DATE_ALBUMS = 6;
	public static final int XF_ADAPTER_SESSIONS = 7;
	public static final int XF_ADAPTER_ITEM_COMMENTS = 8;
	public static final int XF_ADAPTER_MY_ALBUM = 9;
	// 圆角大小
	public static final int CORNERSIZE = 5;
	// 好友
	public static final String ACTION_FRIEND_REMOVE_BLACKLIST = "ACTION_FRIEND_REMOVE_BLACKLIST";
	public static final String ACTION_FRIEND_ADD_BLACKLIST = "ACTION_FRIEND_ADD_BLACKLIST";
	public static final String ACTION_FRIEND_APPROVED_FRIEND = "ACTION_FRIEND_APPROVED_FRIEND";// 自己的好友请求被对方同意
	public static final String ACTION_FRIEND_APPROVE_FRIEND = "ACTION_FRIEND_APPROVE_FRIEND";// 自己同意对方好友请求
	public static final String ACTION_FRIEND_REMOVE_FRIEND = "ACTION_FRIEND_REMOVE_FRIEND";
	public static final String ACTION_FRIEND_REMOVED_FRIEND = "ACTION_FRIEND_REMOVED_FRIEND";
	public static final String ACTION_FRIEND_UPDATE_FRIEND = "ACTION_FRIEND_UPDATE_FRIEND";
	public static final String ACTION_FRIEND_MODIFY_ALIAS = "ACTION_FRIEND_MODIFY_ALIAS";
	public static final String EXTRA_FRIEND_ISUPDATE_FRIENDS = "EXTRA_FRIEND_ISUPDATE_FRIENDS";
	public static final String EXTRA_FRIEND_UPDATE_FRIENDS = "EXTRA_FRIEND_UPDATE_FRIENDS";
	public static final String EXTRA_FRIEND_UPDATE_ALIAS = "EXTRA_FRIEND_UPDATE_ALIAS";
	public static final String EXTRA_FRIEND_ADD_FRIENDS = "EXTRA_FRIEND_ADD_FRIENDS  ";
	public static final String EXTRA_FRIEND_DELETE_FRIENDS = "EXTRA_FRIEND_DELETE_FRIENDS";
	// 时光
	public static final int TIMEFLY_GETDATA_TYPE = 200;
	public static final int TIMEOUT = 201;
	public static final int TIMEOUT_TIME = 6000;
	public static final String CURRENT_DATE = "000000";
	public static final String TIMEFLY_PIC_SET_TYPE = "TIMEFLY_PIC_SET_TYPE";
	public static final int MAGN_RADIUS_DIP = 56; //放大镜的半径
	// 分享
	public static final int ORDER_TO_FOOTPRINT_TYPE = 300;
	public static final int ORDER_TO_RANKING_TYPE = 301;
	public static final int ORDER_TO_OFFLINE_TYPE = 302;
	public static final int ORDER_TO_ONLINE_TYPE = 303;
	// BBS
	public static final int BBS_POSTLIST_TYPE = 400;
	public static final int BBS_POSTDETAIL_TYPE = 401;
	public static final int BBS_HEADIMG_TYPE = 402;
	public static final int BBS_POSTIMG_TYPE = 403;
	public static final String BBS_IS_GETFOCUS = "BBS_IS_GETFOCUS";
	public static final String BBS_COMMA = "_.delimiter._";
	public static final String BBS_CURRENT_COLLEGENAME = "BBS_CURRENT_COLLEGENAME";
	public static final String BBS_CURRENT_COLLEGEID = "BBS_CURRENT_COLLEGEID";
	public static final String BBS_CURRENT_CITYNAME = "BBS_CURRENT_CITYNAME";
	public static final String BBS_CURRENT_CITYID = "BBS_CURRENT_CITYID";
	public static final String BBS_CURRENT_PROVINCENAME = "BBS_CURRENT_PROVINCENAME";
	public static final String BBS_CURRENT_PROVINCEID = "BBS_CURRENT_PROVINCEID";
	public static final String BBS_DEFAULT_PROVINCEID = "230200";
	// 短消息
	public static final int MESSAGE_FIRST = 500;
	public static boolean NEED_ADD_FOOTVIEW_IN_TIME_FLY_LIST = false;
	public static final int ISACCEPT = 501;
	// 图片消息占位符
	public static final String MESSAGE_IMAGE_LINK_START = "&$#@~^@[{:";
	public static final String MESSAGE_IMAGE_LINK_END = ":}]&$~@#@";
	// 音频消息占位符
	public static final String MESSAGE_AUDIO_LINK_SPLIT = "#recordTime:";
	public static final String MESSAGE_AUDIO_LINK_START = ":}]&$~@#@";
	public static final String MESSAGE_AUDIO_LINK_END = "&$#@~^@[{:";
	public static final String MESSAGE_HTML_LINK_START = "";
	public static final String MESSAGE_HTML_LINK_END = "";
	// 登录
	public static final int LOGIN_CONNECTION = 600;
	public static final String LOGIN_PERFORM = "LOGIN_PERFORM";
	// 设置
	public static final String SET_ALBUMAUTHORITY = "SET_ALBUMAUTHORITY";
	public static final String SET_ALBUMSIGNAL = "SET_ALBUMSIGNAL";
	public static final String SET_CACHETIME = "SET_CACHETIME";
	// BeemService
	public static final String ACTION_XMPP_DISCONNECT = "com.beem.project.btf.xmppconn.disconnet";
	public static final String ACTION_XMPP_CONNECT = "com.beem.project.btf.xmppconn.connect";
	// 素材管理
	public static final String TIMECAMERA_TEXTSPLIT = "<@note@>";
	public static final String TIMECAMERA_MAPPINGSPLIT = ",";
	public static final String TIMECAMERA_TEXTPOSITIONSPLIT = ",";

	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	public static final String HTTP_RES_INFO = "fail";
	public static boolean LOAD_MORE_VISIBALE = false;
	public static final int width = 80;
	public static final int height = 80;
	public static final int max = 4;
	public static boolean isDebug = false;
	public static final int TAKEPHOTO = 0x100;
	public static final int CLIPPHOTO = TAKEPHOTO + 1;
	public static final int PICKPHOTO = CLIPPHOTO + 1;
	public static final int UPLOADPHOTO = PICKPHOTO + 1;
	public static final int RESULT_OK = 0;
	public static final int RESULT_FAILED = -1;
	public static final String IMAGE_URL_NAME = "image_url_name";
	public static final int SHARED_ISEMPTY = 3;
	public static final int EMPTY = 5;
	public static final int uploadpicMaxNum = 20;
	public static final int SCAN_COMPLETE = 224;
	/**
	 * 上传文件响应
	 */
	public static final int UPLOAD_FILE_DONE = 6;
	/**
	 * 上传初始化
	 */
	public static final int UPLOAD_INIT_PROCESS = 7;
	/**
	 * 上传中
	 */
	public static final int UPLOAD_IN_PROCESS = 8;
	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 9;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 10;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 11;
	public static final String CONFIG_SP = "config";
	public static final int SMSCONTENT_OBSERVER = 1;
	public static final String GENER_NUM = "10000";
	public static final int ChatGarray = 10;
	/**
	 * 更新状态
	 */
	public static final int SHOW_CHOOSEUPDATE = 300;
	public static final int SHOW_FORCEUPDATE = 301;
	public static final int SHOW_NOINFO = 302;
	public static final int SHOW_WSError = 304;
	public static final int SHOW_JSONException = 305;
	public static final int SHOW_ISDOWNING = 306;
	public static final int DOWNINGERROR = 307;
	public static final int DOWN_SUCCESS = 308;
	public static final int SENDTIME = 307;
}
