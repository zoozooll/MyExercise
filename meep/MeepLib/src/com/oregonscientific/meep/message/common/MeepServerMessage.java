package com.oregonscientific.meep.message.common;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public class MeepServerMessage {

	//proc
	public static final String PROC_CHAT = "chat";
//	public static final String PROC_MOVIE = "movie";
//	public static final String PROC_MUSIC = "music";
//	public static final String PROC_GAME = "game";
//	public static final String PROC_APP = "app";
//	public static final String PROC_LOG = "log";
//	public static final String PROC_EBOOK = "ebook";
//	public static final String PROC_YOUTUBE = "youtube";
//	public static final String PROC_WEB_BROWSER = "web-browser";
//	public static final String PROC_VERIFICATION = "verification";
	public static final String PROC_ACCOUNT = "account";
	public static final String PROC_PARENTAL = "parental";
	public static final	String PROC_SYSTEM = "system";
	public static final	String PROC_STORE = "store";
	
	
	//opCode
	public static final String OPCODE_ACK = "ack";
	public static final String OPCODE_TEXT_MSG = "text-msg";
	public static final String OPCODE_IMG = "mm-msg";
//	public static final String OPCODE_GROUP_MSG = "group-msg";
//	public static final String OPCODE_GROUP_IMG = "group-img";
//	public static final String OPCODE_GET_ALL_FRIEND_GROUPS = "get-all-friend-groups";
//	public static final String OPCODE_GET_FRIEND_GROUP = "get-friend-group";
	public static final String OPCODE_CREATE_FRIEND_GROUP = "create-group";
//	public static final String OPCODE_UPDATE_FRIEND_GROUP_NAME = "update-friend-group-name";
//	public static final String OPCODE_UPDATE_FRIEND_GROUP_IMG = "update-friend-group-img";
	public static final String OPCODE_DELETE_FRIEND_GROUP = "delete-group";
//	public static final String OPCODE_GET_FRIENDS_BY_GROUP = "get-friends-by-group";

	public static final String OPCODE_GET_FRIEND_LIST = "get-friend-list";
//	public static final String OPCODE_GET_FRIEND = "get-friend";
	public static final String OPCODE_SEARCH_FRIEND = "search-friend";
	public static final String OPCODE_ADD_FRIEND = "add-friend";

//	public static final String OPCODE_UPDATE_FRIEND_NAME = "update-friend-name";
//	public static final String OPCODE_UPDATE_FRIEND_IMAGE = "update-friend-image";
	public static final String OPCODE_DELETE_FRIEND = "delete-friend";
	public static final String OPCODE_SWAP_FRIEND_GROUP = "assign-group";
//	public static final String OPCODE_CREATE_CHAT_GROUP = "create-chatgroup";
//	public static final String OPCODE_GET_ITEM = "get-item";
//	public static final String OPCODE_GET_PURCHASE_ITEMS = "get-purchased-items";

	public static final String OPCODE_GET_BLACKLIST = "get-blacklist";
//	public static final String OPCODE_GET_TIME_LIMIT = "get-time-limit";
	public static final String OPCODE_SYS_LOG = "log";
	public static final String OPCODE_GET_RECOMMENDED_LIST = "get-recommendation";
	public static final String OPCODE_GET_APPS_CATEGORY = "get-apps-category";
	public static final String OPCODE_SIGN_IN = "sign-in";
	public static final String OPCODE_SIGN_OUT = "sign-out";
	public static final String OPCODE_CHECK_PURCHASED_ITEM = "validate-purchase";
	public static final String OPCODE_FRIEND_ONLINE_NOTICE = "presence";
	public static final String OPCODE_FRIEND_REQUEST = "friend-request";
	public static final String OPCODE_ACCEPT_FRIEND = "accept-friend";
	public static final String OPCODE_RUN_COMMAND = "run-command";
	public static final String OPCODE_GET_PERMISSION = "get-permission";
	public static final String OPCODE_REPORT_RUNNING = "report-running";
	public static final String OPCODE_REPORT_VERSION = "report-version";
    public static final String OPCODE_REPORT_SYSTEM = "report-system";
	public static final String OPCODE_SET_USER_NICKNAME = "set-nickname";
	public static final String OPCODE_CHECK_URL = "check-url";
	
	public static final String OPCODE_DOWNLOAD_MUSIC = "download-music";
	public static final String OPCODE_DOWNLOAD_MOVIE = "download-movie";
	public static final String OPCODE_DOWNLOAD_EBOOK = "download-ebook";

	public static final String OPCODE_DOWNLOAD_APP = "download-app";
	public static final String OPCODE_DOWNLOAD_GAME = "download-game";
	
	public static final String OPCODE_REMOTE_DOWNLOAD= "remote-download";
	public static final String OPCODE_REMOTE_CONSOLE= "remote-console";
	
	//2013-03-19 Meng - upload permission operation code
	public static final String OPCODE_SET_PERMISSION= "set-permission";

	
	//variable
	public static final String STR_PROCESS = "proc";
	public static final String STR_OPCODE = "opcode";
	public static final String STR_USER_ID = "user-id";
	
	public static final String STATUS_ONLINE = "online";
	
	//return message code
	public static final int CODE_OK = 200;
	public static final int CODE_CLIENT_ERROR = 400;
	public static final int CODE_UNAUTHORIZED = 401;
	public static final int CODE_NOT_FOUND = 404;
	public static final int CODE_SERVER_ERROR = 500;

//	public static final String STR_VAR1 = "var1";
//	public static final String STR_VAR2 = "var2";
//	public static final String STR_VAR3 = "var3";
	
	private String opcode = null;
	private String proc= null;
	private int code = -1;
	private String status = null;
	
	private long requestid = -1;
	
	@SerializedName("messageid")
	private UUID messageID = null;
	private boolean received = false;
	
	private static long requestIdSequence = 0;
	synchronized static private long generateRequestId(){
		return ++requestIdSequence;
	}
	
//	private String mVar1 = null;
//	private String mVar2 = null;
//	private String mVar3 = null;

	//	public MeepServerMessage(String proc, String opcode, String var1, String var2, String var3) {
//		setProc(proc);
//		setOpcode(opcode);
//		setVar1(var1);
//		setVar2(var2);
//		setVar3(var3);
//	}
//	
	public MeepServerMessage(String proc, String opcode) {
		setProc(proc);
		setOpcode(opcode);
		setRequestid(generateRequestId());
		this.messageID = UUID.randomUUID();
	}
	
	public UUID getMessageID() {
		return messageID;
	}
	
	public void setReceived(boolean received) {
		this.received = received;
	}
	
	public boolean isReceived() {
		return received;
	}

	public String getOpcode() {
		return opcode;
	}

	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}

	public String getProc() {
		return proc;
	}

	public void setProc(String proc) {
		this.proc = proc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public long getRequestid() {
		return requestid;
	}

	public void setRequestid(long requestid) {
		this.requestid = requestid;
	}
}
