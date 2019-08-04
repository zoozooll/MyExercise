package com.beem.project.btf.service;

import java.util.HashMap;

import org.jivesoftware.smack.packet.RosterPacket;

import com.beem.project.btf.manager.TopNFootprintManager;
import com.btf.push.BlackRosterPacket;
import com.btf.push.UserInfoPacket;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.UrlDB;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

/**
 * @Description: Tool methods managing whether urls are time out for cache.
 * @author: yuedong bao
 * @date: 2015-4-2 上午11:04:23
 */
public class UrlConfigUtil {
	// 保存数据结构缓存时间map，单位s
	private static HashMap<Class<?>, Integer> map = null;
	static {
		map = new HashMap<Class<?>, Integer>();
		map.put(RosterPacket.class, 120 * 1000);
		map.put(UserInfoPacket.class, 120 * 1000);
		map.put(BlackRosterPacket.class, 120 * 1000);
		map.put(ImageFolderItem.class, 0);
		map.put(CommentItem.class, 0);
		map.put(TopNFootprintManager.class, 0);
	}

	/**
	 * @Title: savePacketUrl
	 * @Description: 保存发送命令的url，缓存所用
	 * @param cls
	 * @param params
	 * @return: void
	 */
	public static void savePacketUrl(Class<?> cls, String params) {
		String url = cls.getName() + VVXMPPUtils.makeJidParsed(params);
		UrlDB urlDB = new UrlDB(url, map.get(cls));
		urlDB.saveToDatabase();
	}
	public static UrlDB getPacketUrl(Class<?> cls, String params) {
		String url = null;
		url = cls.getName() + VVXMPPUtils.makeJidParsed(params);
		return DBHelper.getInstance().queryForFirst(UrlDB.class,
				new DBWhere(DBKey.url, DBWhereType.eq, url));
	}
	public static void saveUrlDB(String url) {
		UrlDB urlDB = new UrlDB(url);
		urlDB.saveToDatabase();
	}
	public static UrlDB geUrlDB(String url) {
		return DBHelper.getInstance().queryForFirst(UrlDB.class,
				new DBWhere(DBKey.url, DBWhereType.eq, url));
	}
	/**
	 * Check if the cache for url is time out;
	 * @param url
	 * @return Return true when url is
	 */
	public static boolean isUrlTimeout(String url) {
		UrlDB db = geUrlDB(url);
		return (db == null) || (db.isDead());
	}
}
