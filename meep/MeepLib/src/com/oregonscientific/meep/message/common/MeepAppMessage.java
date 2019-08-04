package com.oregonscientific.meep.message.common;

import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.database.table.TableAppsCategory;
import com.oregonscientific.meep.database.table.TableChat;
import com.oregonscientific.meep.database.table.TableChatGroup;
import com.oregonscientific.meep.database.table.TableChatGroupMember;
import com.oregonscientific.meep.database.table.TableFriendGroup;
import com.oregonscientific.meep.database.table.TableRecommendation;
import com.oregonscientific.meep.database.table.TablePermission;
import com.oregonscientific.meep.database.table.TableUser;


public class MeepAppMessage {
	
	// opCode
	public static final String OPCODE_TEXT_MSG = "text-msg";
	public static final String OPCODE_IMG = "img";
	public static final String OPCODE_UPGRADE_OTA= "upgrade-ota";
	public static final String OPCODE_GROUP_MSG = "group-msg";
	public static final String OPCODE_GROUP_IMG = "group-img";
	public static final String OPCODE_GET_ALL_FRIEND_GROUPS = "get-all-friend-groups";
	public static final String OPCODE_GET_FRIEND_GROUP = "get-friend-group";
	public static final String OPCODE_CREATE_FRIEND_GROUP = "create-friend-group";
	public static final String OPCODE_UPDATE_FRIEND_GROUP_NAME = "update-friend-group-name";
	public static final String OPCODE_UPDATE_FRIEND_GROUP_IMG = "update-friend-group-img";
	public static final String OPCODE_DELETE_FRIEND_GROUP = "delete-friend-group";
	public static final String OPCODE_GET_FRIENDS_BY_GROUP = "get-friends-by-group";
	public static final String OPCODE_GET_FRIEND = "get-friend";
	public static final String OPCODE_GET_FRIEND_LIST = "get-friend-list";
	public static final String OPCODE_SEARCH_FRIEND = "search-friend";
	public static final String OPCODE_ADD_FRIEND = "add-friend";
	public static final String OPCODE_UPDATE_FRIEND_NAME = "update-friend-name";
	public static final String OPCODE_UPDATE_FRIEND_IMAGE = "update-friend-image";
	public static final String OPCODE_DELETE_FRIEND = "delete-friend";
	public static final String OPCODE_SWAP_FRIEND_GROUP = "swap-friend-group";
	public static final String OPCODE_CREATE_CHAT_GROUP = "create-chat-group";
	public static final String OPCODE_GET_ITEM = "get-item";
	public static final String OPCODE_GET_PURCHASE_ITEMS = "get-purchased-items";
	public static final String OPCODE_GET_BLACKLIST = "get-blacklist";
	public static final String OPCODE_GET_TIME_LIMIT = "get-time-limit";
	public static final String OPCODE_SYS_LOG = "sys-log";
	public static final String OPCODE_GET_RECOMMENDED_LIST = "get-recommended-list";
	
	public static final String OPCODE_SET_USER_NICKNAME = "set-nickname";
	
	public static final String OPCODE_FRIEND_REQUEST = "friend-request";
	public static final String OPCODE_ACCEPT_FRIEND = "accept-friend";
	
	///////
	public static final String OPCODE_SIGN_IN = "sign-in";
	public static final String OPCODE_SIGN_OUT = "sign-out";
	public static final String OPCODE_CHECK_PURCHASED_ITEM = "check-purchased-item";
	public static final String OPCODE_FRIEND_ONLINE_NOTICE = "presence";
	public static final String OPCODE_GET_SCHEDULE = "get-permission";
	public static final String OPCODE_UPDATE_SCHEDULE = "update-permission";
	public static final String OPCODE_CHECK_URL = "check-url";
	
	public static final String OPCODE_DATABASE_FRIEND_GROUP = TableFriendGroup.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_CHAT = TableChat.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_CHAT_GROUP = TableChatGroup.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_CHAT_GROUP_MEMBER = TableChatGroupMember.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_USER = TableUser.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_GET_CHATING_REC = TableUser.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_BLACK_LIST = TableBlacklist.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_GET_APPS_CATEGORY = TableAppsCategory.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_GET_RECOMMENDATION = TableRecommendation.S_TABLE_NAME;
	public static final String OPCODE_DATABASE_GET_PERMISSION = TablePermission.S_TABLE_NAME;
	
	public static final String OPCODE_START_PERMISSION_TIMER = "start-permission-timer";
	public static final String OPCODE_STOP_PERMISSION_TIMER = "stop-permission-timer";
	public static final String OPCODE_KILL_APP = "kill-app";
	
	public static final String OPCODE_DOWNLOAD_MUSIC = "download-music";
	public static final String OPCODE_DOWNLOAD_MOVIE = "download-movie";
	public static final String OPCODE_DOWNLOAD_EBOOK = "download-ebook";

	public static final String OPCODE_DOWNLOAD_APP = "download-app";
	public static final String OPCODE_DOWNLOAD_GAME = "download-game";
	
	public static final String OPCODE_GET_USER_TOKEN = "get-user-token";
	public static final String OPCODE_UPDATE_APPS_CATEGORY = "update-apps-category";
	
	public static final String OPCODE_OPEN_MEEP_STORE = "open-meep-store";
	
	Category catagory = Category.NOT_DEFINED;
	String opcode = null;
	String from = null;
	String message = null;
	
	public enum Category
	{
		DATABASE_QUERY,
		DATABASE_ALTER,
		PARENTAL_CONTROL,
		SERVER,
		LOG,
		NOT_DEFINED,
		SYSTEM
	}
	
	public MeepAppMessage(Category catogory, String opcode, String message, String from)
	{
		this.catagory= catogory;
		this.opcode = opcode;
		this.message = message;
		this.from = from;
	}
	
	public Category getCatagory()
	{
		return this.catagory;
	}
	
	public String getOpcode()
	{
		return opcode;
	}

	
	public String getMessage()
	{
		return message;
	}
	
	public String getFrom()
	{
		return from;
	}

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
