package com.oregonscientific.meep.browser;

public class Consts {
	public static final int TYPE_OS_RECOMMENDATION = 0;
	public static final int TYPE_PARENTS_RECOMMENDATION = 1;
	public static final int TYPE_BOOKMARK = 2;
	public static final int MARGIN_LEFT = 0;
	public static final int MARGIN_TOP = 1;
	
	public static final String PATH_RECENTLY_PICTURE = "/recently.jpg";
	public static final String PATH_RECENTLY_TEXT= "/recently.txt";
	
	public static final String PREFERENCE_FILE_NAME = "user_info";
	public static final String PREFERENCE_KEY_TOKEN = "token";
	public static final String PREFERENCE_KEY_MEEPTAG= "meeptag";
	public static final String PREFERENCE_KEY_ID= "id";
	public static final String PREFERENCE_KEY_FIRSTNAME = "first_name";
	public static final String PREFERENCE_KEY_LASTNAME = "last_name";
	
	
	public static final int MESSAGE_WHAT_ALERT_MESSAGE_CONTENT = 0;
	public static final int MESSAGE_WHAT_GET_ALL_RECOMMENDATIONS = 1;
	protected static final int MESSAGE_WHAT_UPDATE_ALL_RECOMMENDATIONS = 2;
	public static final String BUNDLE_KEY_ALERT_MESSAGE_CONTENT = "alert_message";
	public static final String GOOGLE_SEARCH_PREFIX = "https://www.google.com/search?safe=active&q=";
	
	
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_BLOCKED_BADWORD = 2;
	public static final int RESULT_BLOCKED_BLACKLIST = 3;
	public static final int RESULT_CANCELLED = 4;
	public static final int RESULT_EMPTY_SEARCH = 5;
}
