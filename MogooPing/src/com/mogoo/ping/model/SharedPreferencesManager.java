/**
 * 
 */
package com.mogoo.ping.model;

import com.mogoo.ping.ctrl.RemoteApksManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Aaron Lee
 * 管理SharedPreferences的有关设定
 * @Date 上午11:17:38  2012-9-20
 */
public class SharedPreferencesManager {
	
	private final static String SHAREDPREFERENCES_ROOT = "mogoo_config";
	private final static String KEY_USED_LAST_FRUSH_TIEM = "used_last_frush_time";
	private final static String KEY_HAVERUN = "have_run_before";
	public final static String KEY_LASTUPDATE_MILLION_APPS_LASTED = "last_update_millions_applications_lasted";
	public final static String KEY_LASTUPDATE_MILLION_APPS_RECOMEND = "last_update_millions_applications_recomend";
	public final static String KEY_LASTUPDATE_MILLION_GAMES_LASTED = "last_update_millions_games_lasted";
	public final static String KEY_LASTUPDATE_MILLION_GAMES_RECOMEND = "last_update_millions_games_recomend";
	
	private static SharedPreferencesManager instance;
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	
	private SharedPreferencesManager(Context context) {
		if (mSharedPreferences == null) {
			mSharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_ROOT, Context.MODE_PRIVATE);
		}
	}
	
	public static SharedPreferencesManager getSharedPreferencesManager(Context context) {
		if (instance == null) {
			instance = new SharedPreferencesManager(context);
		}
		return instance;
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * 获得上一次清空“最近使用软件”列表的时间戳
	 * @return 上一次清空
	 * @Date 上午11:31:03  2012-9-20
	 */
	public long getUsedLastCleanTime() {
		return mSharedPreferences.getLong(KEY_USED_LAST_FRUSH_TIEM, 0);
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * 更新“最近使用软件”列表上一次清空的时间
	 * @param time
	 * @Date 上午11:37:25  2012-9-20
	 */
	public void saveOrUpdateUsedLastCleanTime(long time) {
		mSharedPreferences.edit().putLong(KEY_USED_LAST_FRUSH_TIEM, time).commit();
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * 之前是否曾经运行过
	 * @return
	 * @Date 上午11:29:58  2012-9-28
	 */
	public boolean isFirstRunning () {
		return mSharedPreferences.getBoolean(KEY_HAVERUN, false);
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * 第一次运行将写入，以后不用
	 * @Date 上午11:32:14  2012-9-28
	 */
	public void writeFirstRunning () {
		mSharedPreferences.edit().putBoolean(KEY_HAVERUN, true).commit();
	}
	
	
	public long getLastUpdateTimeMillions(int flag) {
		switch (flag) {
		case RemoteApksManager.TAG_APPLICATIONS_LASTED:
			return mSharedPreferences.getLong(KEY_LASTUPDATE_MILLION_APPS_LASTED, 0);
		case RemoteApksManager.TAG_APPLICATIONS_RECOMEND:
			return mSharedPreferences.getLong(KEY_LASTUPDATE_MILLION_APPS_RECOMEND, 0);
		case RemoteApksManager.TAG_GAME_LASTED:
			return mSharedPreferences.getLong(KEY_LASTUPDATE_MILLION_GAMES_LASTED, 0);
		case RemoteApksManager.TAG_GAME_RECOMEND:
			return mSharedPreferences.getLong(KEY_LASTUPDATE_MILLION_GAMES_RECOMEND, 0);
		default:
			return 0;
		}
	}
	
	public void updateLastUpdateTimeMillions (int flag, long millions) {
		final String key;
		switch (flag) {
		case RemoteApksManager.TAG_APPLICATIONS_LASTED:
			key = KEY_LASTUPDATE_MILLION_APPS_LASTED;
			break;
		case RemoteApksManager.TAG_APPLICATIONS_RECOMEND:
			key = KEY_LASTUPDATE_MILLION_APPS_RECOMEND;
			break;
		case RemoteApksManager.TAG_GAME_LASTED:
			key = KEY_LASTUPDATE_MILLION_GAMES_LASTED;
			break;
		case RemoteApksManager.TAG_GAME_RECOMEND:
			key = KEY_LASTUPDATE_MILLION_GAMES_RECOMEND;
			break;
		default:
			return ;
		}
		mSharedPreferences.edit().putLong(key, millions).commit();
	}
}
