package com.iskyinfor.duoduo.ui;

public class UIPublicConstant {

	// 设置登陆时输入的字符和数字
	public static char inputInfo[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9' };

	// 登陆时记住密码的文件
	public static final String UserInfo = "duoduoShareFile";
	
	// 同步教学
	public static final int LESSON_DATA_VIDEO_MESSAGE_TAG = 1;
	public static final int LESSON_DATA_TEACHER_MESSAGE_TAG = 2;
	

	// 判断异步线程执行的标志
	public final static int UPADATE_PSWD_MARK = 0x000001;
	public final static int PAYFOR_MONEY_MARK = 0x000002;
	public final static int QUERY_ALLUSER_MARK = 0x000003;

	//用户中心
	public static final int USERCENTER_BASERES = 11;
	
	// 用户中心  -- 弹出PopupWindow的标示
	public final static int intYear = 1;
	public final static int intMonth = 2;
	public final static int intDay = 3;
	public final static int intProvince = 4;
	public final static int intCity = 5;
	public final static int intCountry = 6;
	
	//用户中心--时间和地点的标示
	public final static int INT_YEAR_CODE = 1;
	public final static int INT_MONTH_CODE = 2;
	public final static int INT_DAY_CODE = 3;
	public final static int INT_PROVINCE_CODE = 4;
	public final static int INT_CITY_CODE = 5;
	public final static int INT_COUNTRY_CODE = 6;
	
	//取到用户账号的余额
	public final static int USER_BALANCE = 1;
	
	//允许充值密码域中输入的字符
	public final static int MAX_TEXT_INPUT_LENGTH = 24;
	
}