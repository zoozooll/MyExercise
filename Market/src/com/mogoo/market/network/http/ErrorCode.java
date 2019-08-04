package com.mogoo.market.network.http;

/**
 * 我的账号： Errorcode 0 - 999 MAS平台 Errorcode 1000 - 1999 Mogoo： Errorcode 2000 -
 * 2999 广告系统 Errorcode 3000 - 3999
 */
public class ErrorCode {

	private ErrorCode() {
	}

	public static final int ERROR_UNKNOW = -1;

	public static final int SUCCESS = 0;

	public static final int ERROR_AID_WRONG = 1;// AID格式错误
	public static final int ERROR_AID_EMPTY = 2;// AID为空
	public static final int ERROR_AID_NOT_EXIST = 3;// AID不存在
	public static final int ERROR_PWD_WRONG = 4;// 密码错误
	public static final int ERROR_PWD_ERROR = 5;// 用户名或者密码错误
	public static final int ERROR_AID_ALREADY_EXIST = 6;// AID已经存在
	public static final int ERROR_RECORD_NOT_EXIST = 7;// 没有记录(查询时,查询记录数为0)
	public static final int ERROR_OLD_PWD_WRONG = 8;// 旧的密码不正确
	public static final int ERROR_PWD_EMPTY = 9;// 密码为空
	public static final int ERROR_LESS_BALANCE = 10;// 余额不足
	public static final int ERROR_CHARGEBACK_FAILURE = 11;// 扣费失败
	public static final int ERROR_OLD_AID_WRONG = 12;// 旧的用户名不存在
	public static final int ERROR_PWD_NOT_EXIST = 13;// 邮箱不正确
	public static final int ERROR_EMAIL_ALREADY_EXIST = 14;// 邮箱已经存在
	public static final int ERROR_NAME_EMPTY = 15;// 姓名为空
	public static final int ERROR_SEX_EMPTY = 16;// 性别为空
	public static final int ERROR_BIRTHDAY_EMPTY = 17;// 生日为空
	public static final int ERROR_AGE_EMPTY = 18;// 年龄为空
	public static final int ERROR_MESSAGE_EMPTY = 19;// 留言为空
	public static final int ERROR_MOBILE_WRONG = 20;// 手机号码不正确
	public static final int ERROR_SYSTEM_WRONG = 21;// 系统错误
	public static final int ERROR_EMAIL_EMPTY = 22;// 邮箱为空
	public static final int ERROR_MOBILE_EMPTY = 23;// 电话为空
	public static final int ERROR_OLD_PWD_EMPTY = 24;// 旧密码为空
	public static final int ERROR_EMAIL_WRONG = 25;// 邮箱格式错误
	public static final int ERROR_FRIEND_AID_EMPTY = 26;// 朋友AID为空
	public static final int ERROR_CONTENT_EMPTY = 27;// 更新用户信息为空
	public static final int ERROR_BIND_EMPTY = 28;// 绑定邮箱和密码不能都为空
	public static final int ERROR_NEW_PWD_EMPTY = 29;// 新密码为空
	public static final int ERROR_AID_TOO_LONG = 30;// AID太长
	public static final int ERROR_OLD_AID_TOO_LONG = 31;// 旧的AID太长
	public static final int ERROR_PWD_TOO_LONG = 32;// 密码太长
	public static final int ERROR_OLD_PWD_TOO_LONG = 33;// 旧的密码太长
	public static final int ERROR_NEW_PWD_TOO_LONG = 34;// 新密码太长
	public static final int ERROR_EMAIL_TOO_LONG = 35;// 邮箱太长
	public static final int ERROR_NAME_TOO_LONG = 36;// 姓名太长
	public static final int ERROR_SEX_TOO_LONG = 37;// 性别太长
	public static final int ERROR_BIRTHDAY_TOO_LONG = 38;// 生日太长
	public static final int ERROR_AGE_TOO_LONG = 39;// 年龄太长
	public static final int ERROR_MESSAGE_TOO_LONG = 40;// 留言太长
	public static final int ERROR_BIND_EMAIL_WRONG = 41;// 绑定邮箱格式不正确
	public static final int ERROR_FRIEND_AID_TOO_LONG = 42;// 朋友AID太长
	public static final int ERROR_CONTENT_TOO_LONG = 43;// 用户信息太长
	public static final int ERROR_APPID_TOO_LONG = 44;// 商品类型太长
	public static final int ERROR_ITEMID_TOO_LONG = 45;// 商品ID太长
	public static final int ERROR_SMS_CEMTER_NMUBER_TOO_LONG = 46;// 短息中心号太长
	public static final int ERROR_SMS_CEMTER_NMUBER_TOO_SHORT = 47;// 短息中心号太短
	public static final int ERROR_BIRTHDAY_WRONG = 48;// 生日格式错误必须为（yyyy-mm-dd)
	public static final int ERROR_PWD_HAVE_SPACE = 49;// 密码不允许有空格
	public static final int ERROR_SEX_WROGN = 50;// 性别格式有误，必须为男、女
	public static final int ERROR_AGE_WROGN = 51;// 年龄必须为数字

	public static final int ERROR_AKEY_WRONG = 1001; // akey 错误
	public static final int ERROR_ARGS_MISMATCH = 1002; // 参数错误
	public static final int ERROR_SESSION_INVALID = 1003; // session 无效
	public static final int ERROR_AID_INVALID = 1004; // Aid 无效
	// 群组名重复
	public static final int ERROR_GROUP_EXIST = 2001;
	// 好友结果集为空
	public static final int ERROR_FRIEND_NULL = 2002;
	// 群组结果集为空
	public static final int ERROR_GROUP_NULL = 2003;
	// 黑名单结果集为空
	public static final int ERROR_BLACK_NULL = 2004;
	// 兴趣爱好结果集为空
	public static final int ERROR_INTEREST_NULL = 2005;
	// 自定义结果集为空
	public static final int ERROR_ITEM_NULL = 2006;
	// 搜索结果集为空
	public static final int ERROR_SEARCH_NULL = 2007;
	// 搜索关键字结果集为空
	public static final int ERROR_SEARCH_KEY_NULL = 2008;
	// 没有发现可推送用户
	public static final int ERROR_PUSH_USER_NULL = 2009;
	// 特殊字符
	public static final int ERROR_SPECIAL_STRING = 2010;
	// 用户结果集为空
	public static final int ERROR_USER_NULL = 2011;
	// 文件参数错误
	public static final int ERROR_FILE_FAIL = 2012;
	// 搜索参数错误
	public static final int ERROR_SEARCH_FAIL = 2013;
	// 黑名单参数错误
	public static final int ERROR_BLACK_FAIL = 2014;
	// 好友参数错误
	public static final int ERROR_FRIEND_FAIL = 2015;
	// 群组参数错误
	public static final int ERROR_GROUP_FAIL = 2016;
	// 关注参数错误
	public static final int ERROR_ATTENTION_FAIL = 2017;
	// 年龄输入参数有误
	public static final int ERROR_AGE_FAIL = 2018;
	// 好友与群组关系结果为空
	public static final int ERROR_FRIEND_GROUP_NULL = 2019;
	// 好友备注参数输入太长
	public static final int FRIEND_NAME_LONG = 2020;
	// 好友ID输入太长
	public static final int FRIEND_ID_LONG = 2021;
	// 群组名称输入太长
	public static final int GROUP_NAME_LONG = 2022;
	// 关注好友存在
	public static final int ERROR_ATTENTION_EXIST = 2023;
	// 签名为空
	public static final int ERROR_SIGN_EMPTY = 2024;
	// 签名输入太长
	public static final int ERROR_SIGN_LONG = 2025;
	// 关注好友不存在
	public static final int ERROR_ATTENTION_NULL = 2026;
	// 群组不存在
	public static final int ERROR_GROUP_NOT_EXIST = 2027;
	// 数据库查询错误
	public static final int ERROR_DATABASE_SQL = 2028;
	// 数据库中结果集为空
	public static final int ERROR_DATABASE_NULL = 2029;
	// 项目编号为空
	public static final int ERROR_PROJECT_CODE_NULL = 2030;
	// 项目版本号为空
	public static final int ERROR_PROJECT_VERSION_NULL = 2031;

	/** 蘑菇商城 */
	// page格式错误
	public static final int ERROR_STORE_PAGE_FAIL = 6000;
	// pagesize格式错误
	public static final int ERROR_STORE_PAGESIZE_FAIL = 6001;
	// parentId格式错误
	public static final int ERROR_PARENT_ID_FAIL = 6002;
	// 分类格式错误
	public static final int ERROR_ASSORT_FAIL = 6003;
	// 子分类格式错误
	public static final int ERROR_SUBCATEGORY_FAIL = 6004;
	// 应用id为空
	public static final int ERROR_APPID_EMPTY = 6005;
	// 无法安装字段必须为0或者1
	public static final int ERROR_INSTALLATION_FIELD = 6006;
	// 运行出错必须为0或者1
	public static final int ERROR_RUN_FAIL = 6007;
	// 数据库运行错误
	public static final int ERROR_STORE_DATABASE_FAIL = 6008;
	// 评分必须为浮点数
	public static final int ERROR_GRADE_FAIL = 6009;
	// apkId格式错误
	public static final int ERROR_APKID_FAIL = 6010;
	// apkId为空
	public static final int ERROR_APKID_EMPTY = 6011;
	// 应用软件app为空
	public static final int ERROR_APP_EMPTY = 6012;
	// 评论标题为空
	public static final int ERROR_COMMENT_TITLE_EMPTY = 6013;
	// 被评论的软件版本号为空
	public static final int ERROR_COMMENT_VERSION_EMPTY = 6014;
	// 用户名为空
	public static final int ERROR_USERNAME_EMPTY = 6015;
	// 评分为空
	public static final int ERROR_GRADE_EMPTY = 6016;
	// 评分时间为空
	public static final int ERROR_GRADE_TIME_EMPTY = 6017;
	// 日期格式错误
	public static final int ERROR_TIME_FAIL = 6018;
	// apkId不存在
	public static final int ERROR_APKID_NOT_EXIST = 6019;
	// topicId格式错误
	public static final int ERROR_TOPICID_FAIL = 6020;

	/**
	 * 判断是否为成功的代码
	 * 
	 * @author 张永辉
	 * @date 2011-10-27
	 * @param code
	 * @return
	 */
	public static boolean isSuccessCode(String errorCode) {
		int code = ErrorCode.ERROR_UNKNOW;

		try {
			code = Integer.parseInt(errorCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (code == SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

}
