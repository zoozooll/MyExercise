package com.oregonscientific.meep.global;

import com.oregonscientific.meep.MEEPEnvironment;

import android.Manifest.permission;
import android.os.Environment;

public class Global {
	
	//Global variable to control ring menu
	public static final boolean DISABLE_RING_MENU = false;
	//Global variable to control Wifi Setup popup in OpenBox
	public static final boolean DISABLE_WIFI_SETUP = false;

	public static final boolean DEBUG = false;
	
	//action string prefix (for send broadcast message)
	public static final String INTENT_MSG_PREFIX = "com.osgd.meep.";
	public static final String ACTION_JUNIT_TEST = INTENT_MSG_PREFIX + "JUnit";
	public static final String URL_HTTP_PERFIX= "http";

	//command
	public static final String STRING_CMD = "cmd";
	public static final String CMD_SIGNIN = "SIGNIN";
	public static final String CMD_SIGNOUT = "SIGNOUT";
	public static final String CMD_SET_NICK_NAME = "SET_NICKNAME";
	public static final String CMD_GET_FRIEND_LIST = "GET_FRIENDS_LIST";
	public static final String CMD_SEND_TEXT = "SEND_TEXT";
	public static final String CMD_NOTIFY = "NOTIFY";
	public static final String CMD_GET_VIDEOS_LIST = "GET_VIDEOS_LIST";
	
	//status
	public static final String STRING_STATUS = "status";
	public static final String STATUS_OK = "OK";
	public static final String STATUS_AVAILABLE = "Available";

	// friend list
	public static final String STRING_GROUP = "group";
	public static final String STRING_NICK_NAME = "nickname";
	public static final String STRING_LIST = "list";
	public static final String STRING_TARGET = "target";
	public static final String STRING_BODY = "body";
	public static final String STRING_EMAIL = "email";
	public static final String STRING_ID = "id";
	public static final String STRING_NAME = "name";
	public static final String STRING_DESCRIPTION = "description";
	public static final String STRING_TOKEN = "token";
	public static final String STRING_SENDER = "sender";
	public static final String STRING_MESSAGE = "msg";
	public static final String STRING_ALERT = "alert";
	public static final String STRING_TITLE = "title";
	public static final String STRING_USER_ICON = "userIcon";
	public static final String STRING_USER_ICON_300_PX = "userIcon300";
	
	//path control
	public static final String FOLDER_HOME = "home";
	public static final String FOLDER_PREVIEW = "preview";
	public static final String FOLDER_DATA = "data";
	public static final String FOLDER_DIM = "dim";
	public static final String FOLDER_THUMBNAIL = "thumbnail";
	public static final String FOLDER_GROUP = "group";
	public static final String FOLDER_PROFILE = "profile";
	
	//file type
	public static final String FILE_TYPE_PNG = ".png";
	public static final String FILE_TYPE_JPG = ".jpg";
	public static final String FILE_TYPE_JEPG = ".jpeg";
	public static final String FILE_TYPE_MP3 = ".mp3";
	public static final String FILE_TYPE_MP4 = ".mp4";
	public static final String FILE_TYPE_EPUB = ".epub";
	public static final String FILE_TYPE_PDF = ".pdf";
	public static final String FILE_TYPE_AMR = ".amr";
	public static final String FILE_TYPE_WAV = ".wav";
	
	//Movie
	public static final String STRING_MOVIE = "movie";
	public static final String STRING_MOVIE_PATH = "movie_path";
	
	public static final String STRING_GAME_LIST = "game_list";
	public static final String STRING_BLOCK_LIST = "blocked_list";
	
	//group
	public static final String GROUP_ARTS = "arts";
	public static final String GROUP_BOYS = "boys";
	public static final String GROUP_BUDDY = "buddy";
	public static final String GROUP_CLASSMATE = "classmate";
	public static final String GROUP_FAMILY = "family";
	public static final String GROUP_GENERAL = "general";
	public static final String GROUP_GIRLS = "girls";
	public static final String GROUP_MUSIC = "music";
	public static final String GROUP_NEIGHBORHOOD = "neighborhood";
	public static final String GROUP_SPORT = "sport";
	public static final String GROUP_Add = "add";
	public static final String GROUP_ADD_FRIEND = "addfriend";
	

	//2013-02-26 - Raymond - Add JP Yahoo Control button in MeepHome! for JP Lang only
	public static final String STRING_BROWSER_DEFAULT_PATH="defaultpath";  
		
	
	public static final String STRING_PATH = "path";
	public static final String STRING_TYPE = "type";
	
	public static final String APP_HOME_PACKAGE_STRING 		= "com.oregonscientific.meep/.home.MeepHomeActivity"; 
	public static final String APP_MOVIE_PACKAGE_STRING 	= "com.oregonscientific.meep.movie/.FullListViewActivity";
	public static final String APP_MUSIC_PACKAGE_STRING 	= "com.oregonscientific.meep.meepmusic/.FullListViewActivity";
	public static final String APP_GAME_PACKAGE_STRING 		= "com.oregonscientific.meep.game/.FullListViewActivity";
	public static final String APP_PHOTO_PACKAGE_STRING 	= "com.oregonscientific.meep.meepphoto/.FullListViewActivity";
	public static final String APP_COMMUNICATOR_PACKAGE_STRING 	= "com.oregonscientific.meep.communicator/.MeepCommunicatorActivity";
	public static final String APP_APPLICATION_PACKAGE_STRING 	= "com.oregonscientific.meep.app/.FullListViewActivity";
	public static final String APP_EBOOK_PACKAGE_STRING 	= "com.oregonscientific.meep.ebook/.FullListViewActivity";
	public static final String APP_PHOTO_VIEWER_PACKAGE_STRING 	= "com.oregonscientific.meep.meepphoto/.MeepPhotoGalleryActivity";
	public static final String APP_MUSIC_PLAYER_PACKAGE_STRING 	= "com.oregonscientific.meep.musicplayer/.MusicPlayerActivity";
	public static final String APP_CAMERA_PACKAGE_STRING 	= "com.android.camera/.Camera";
	public static final String APP_WEB_BROWSER_PACKAGE_STRING = "com.oregonscientific.meep.browser/.WebBrowserActivity";
	public static final String APP_SETTINGS_PACKAGE_STRING = "Settings";
	//2013-02-25 - Raymond - Add Partent Control button in MeepHome!
	public static final String APP_PARENT_PACKAGE_STRING = "com.oregonscientific.meep.together/.activity.MeepTogetherSplashScreen";
	//2013-02-26 - Raymond - Add JP Yahoo Control button in MeepHome! for JP Lang only
	public static final String APP_JPYAHOO_PACKAGE_STRING = "JPYahoo";
	public static final String APP_SAFE_PACKAGE_STRING = "Safe";
	public static final String APP_HELP_PACKAGE_STRING = "Help";	
	//public static final String APP_MEEP_STORE_PACKAGE_STRING = "com.oregonscientific.meep.store/.MeepStoreClientActivity";
	public static final String APP_MEEP_STORE_PACKAGE_STRING = "com.oregonscientific.meep.store2/.MainActivity";
																 
	public static final String APP_YOUTUBE_PACKAGE_STRING = "com.oregonscientific.meep.youtube/.MeepYoutubeActivity";
	
	
	//DB
	public static int DB_NO_ERR = 0;
	public static int DB_ERR = 1;
	
	public static final String DB_STR_CDOE = "code";
	public static final String DB_STR_ERR = "error";
	
	//path
	public static final String PATH_COMMUNICATOR_GROUP_LARGE_ICON = Environment.getExternalStorageDirectory() + "/home/friend/friend_group/icon_l/";
	public static final String PATH_COMMUNICATOR_GROUP_LARGE_DIM_ICON = Environment.getExternalStorageDirectory() + "/home/friend/friend_group/icon_ld/";
	
	public static final String PATH_COMMUNICATOR_FRIEND_LARGE_ICON = Environment.getExternalStorageDirectory() + "/home/friend/friend/icon_l/";
	public static final String PATH_COMMUNICATOR_FRIEND_LARGE_DIM_ICON = Environment.getExternalStorageDirectory() + "/home/friend/friend/icon_ld/";
	
	public static final String PATH_DEFAULT_GROUP_ICON = Environment.getExternalStorageDirectory() + "/home/friend/default/friend_group/icon_l/";
	
	public static final String PATH_PHOTO_DATA = Environment.getExternalStorageDirectory() + "/home/photo/data/";
	public static final String PATH_PHOTO_ICON = Environment.getExternalStorageDirectory() + "/home/photo/icon/";
	
	public static final String PATH_MOVIE_DATA = Environment.getExternalStorageDirectory() + "/home/movie/data/";
	public static final String PATH_MOVIE_ICON = Environment.getExternalStorageDirectory() + "/home/movie/icon/";
	
	public static final String PATH_MUSIC_DATA = Environment.getExternalStorageDirectory() + "/home/music/data/";
	public static final String PATH_MUSIC_ICON = Environment.getExternalStorageDirectory() + "/home/music/icon/";
	
	public static final String PATH_EBOOK_DATA = Environment.getExternalStorageDirectory() + "/home/ebook/data/";
	public static final String PATH_EBOOK_ICON = Environment.getExternalStorageDirectory() + "/home/ebook/icon/";
	
	public static final String PATH_APP_DATA = Environment.getExternalStorageDirectory() + "/home/app/data/";
	public static final String PATH_APP_ICON = Environment.getExternalStorageDirectory() + "/home/app/icon/";
	
	public static final String PATH_GAME_DATA = Environment.getExternalStorageDirectory() + "/home/game/data/";
	public static final String PATH_GAME_ICON = Environment.getExternalStorageDirectory() + "/home/game/icon/";
	
	public static final String PATH_DATA_HOME = MEEPEnvironment.getMediaStorageDirectory().getAbsolutePath()+"/"; 
	
	public static final String DIR_USER_ICON = "user_icon";
	
	public static final String EXTRA_CATEGORY = "system-opcode";
	public static final String EXTRA_OPCODE = "opcode";
	
	public enum AppType {
		Ebook, Game,App, OtherApp, Home, IM, Movie, Music, Photo, Youtube, MeepMessage, Communicator, Browser, NotDefined, UserInfo, Store

	}
	
	
	
}
