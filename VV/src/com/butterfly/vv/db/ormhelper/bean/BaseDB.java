package com.butterfly.vv.db.ormhelper.bean;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.smack.util.StringUtils;

import android.text.TextUtils;

import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.model.Comment;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

/**
 * 秦王扫六合，虎视何雄哉。挥剑决浮云，诸侯尽西来。
 */
@SuppressWarnings("unchecked")
public abstract class BaseDB {
	
	private final static SimpleDateFormat sf = new SimpleDateFormat(
			"yyyy-MM-dd");
	private final static SimpleDateFormat sf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	protected Map<String, Object> fields = new HashMap<String, Object>();
	
	List<Class<? extends BaseDB>> friendBlackList = Arrays.asList(
			UserFriendDB.class, UserBlackListDB.class);
	List<Class<? extends BaseDB>> simpleList = Arrays.asList(
			ImageFolder.class, VVImage.class, Comment.class, UrlDB.class,
			UserNeighborDB.class, ImageFolderNotify.class,
			FootPrintDB.class, TopNDB.class, SynDataDB.class,
			MessageDB.class, ChatDB.class);

	public enum DBKey {
		jid, jid_friend, alias, nickName, phoneNum, lon, lat, path, laypath1,
		laypath2, laypath3, laycount, pathThumb, pathThumbLarge, logintime,
		onlinetime, bday, sex, signature, email, cityId, schoolId, major,
		enroltime, hobby, createTime, authority, password, gid, thumbupCount,
		chat, jid_reply, content, photoCount, jid_black, notify_time,
		notify_valid, commentCount, cid, toCid, jid_commented, commentTime,
		jid_photogroup, pid, photo_small, photo_big, url, birthTime, lifetime,
		msgtime, type, subType, jid_send, jid_receive, jid_neighbor, distance,
		neighborType, id, jid_rank, jid_login, groupid, isDownloaded, isLocal,
		name, nameOld, phoneNumState, phoneNumWhere, notetext, digest,
		unReadMsgCount, isChecked, msgState, isLoading, textposition, textsize,
		mapping, location, isThumbup, uri, posepath, textcolor, bitmapposition,
		templateid, texttype, phoneNumRelation, iscenter, isshadow,
		srcbmposition, srcbmsize, textBound, fontPath, gravity, linenum,
		isBold, album_url, topic, grade, imagefoldertype, portrait_small, gidJid;
		public String getDBStr() {
			return new StringBuffer().append('`').append(toString())
					.append('`').toString();
		}
	}

	public BaseDB() {
	}
	public BaseDB setField(DBKey key, Object val) {
		if (key.toString().startsWith("jid")) {
			// 保存的jid都是不带后缀的，如123456789@vv,应该保存为123456789
			val = StringUtils.parseName((String) val);
		}
		if (val == null) {
			//LogUtils.e("val should not be empty,val:" + val + "  key:" + key, 5);
		}
		fields.put(key.toString(), val);
		setFieldReflex(key.toString(), val);
		return this;
	}
	
	public BaseDB setField(String key, Object val) {
		if (key.toString().startsWith("jid")) {
			// 保存的jid都是不带后缀的，如123456789@vv,应该保存为123456789
			val = StringUtils.parseName((String) val);
		}
		if (val == null) {
			//LogUtils.e("val should not be empty,val:" + val + "  key:" + key, 5);
		}
		fields.put(key, val);
		setFieldReflex(key, val);
		return this;
	}
	public Object getField(DBKey key) {
		Object retVal = fields.get(key.toString());
		if (retVal == null) {
			retVal = getFieldInner(key.toString());
			fields.put(key.toString(), retVal);
		}
		return retVal;
	}
	public Object getField(String key) {
		Object retVal = fields.get(key);
		if (retVal == null) {
			retVal = getFieldInner(key);
			fields.put(key, retVal);
		}
		return retVal;
	}
	public <T> T getField(DBKey key, Class<T> cls) {
		return (T) getFieldInner(key.toString());
	}
	// 默认时利用反射赋值
	protected void setFieldReflex(String key, Object val) {
		try {
			Class<?> cls = getClass();
			Field field = cls.getDeclaredField(key.toString());
			boolean isAccessible = field.isAccessible();
			field.setAccessible(true);
			field.set(this, val);
			field.setAccessible(isAccessible);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	};
	protected Object getFieldInner(String key) {
		Object retVal = null;
		try {
			Class<?> cls = getClass();
			Field field = cls.getDeclaredField(key.toString());
			boolean isAccessible = field.isAccessible();
			field.setAccessible(true);
			retVal = field.get(this);
			field.setAccessible(isAccessible);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return retVal;
	};
	public String getFieldStr(DBKey key) {
		return (String) getField(key);
	}
	public Map<String, Object> getFieldMap() {
		return fields;
	}
	// 保存到数据库
	public abstract void saveToDatabase();
	public void saveToDatabaseAsync() {
		DBHelper.getInstance().saveToDatabaseAsync(this);
	}
	public int getSize() {
		int size = 0;
		Field[] fileds = getClass().getDeclaredFields();
		for (Field field : fileds) {
			if (int.class.equals(field.getType())
					&& !field.getName().equals("size")) {
				size += 4;
				continue;
			}
			if (String.class.equals(field.getType())) {
				try {
					String str = ((String) field.get(this));
					if (str != null) {
						size += str.getBytes().length;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				continue;
			}
			if (long.class.equals(field.getType())) {
				size += 8;
				continue;
			}
			if (boolean.class.equals(field.getType())
					|| byte.class.equals(field.getType())) {
				size += 1;
				continue;
			}
		}
		return size;
	};
	/**
	 * @Title: createID
	 * @Description: 产生主键
	 * @param: @param saveMap
	 * @param: @param keys
	 * @param: @return
	 * @return: String
	 * @throws:
	 */
	public static String createID(Map<DBKey, Object> saveMap, DBKey[] keys) {
		StringBuffer sb = new StringBuffer();
		for (DBKey keyOne : keys) {
			String val = (String) saveMap.get(keyOne);
			if (TextUtils.isEmpty(val)) {
				throw new IllegalArgumentException("createID failed,the DBKey:"
						+ keyOne + " 's val is " + val);
			} else {
				sb.append(val);
			}
		}
		return sb.toString();
	}
	public String createID(DBKey... vals) {
		StringBuffer sb = new StringBuffer();
		for (DBKey keyOne : vals) {
			String val = (String) fields.get(keyOne);
			if (TextUtils.isEmpty(val)) {
				throw new IllegalArgumentException("createID failed,the DBKey:"
						+ keyOne + " 's val is " + val);
			} else {
				sb.append(val);
			}
		}
		return sb.toString();
	}
	/**
	 * @Title: clearCache
	 * @Description: 清除缓存
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public final void clearCache(long cacheMills) {
		
		if (friendBlackList.contains(getClass())) {
			clearCacheFriendBlack(cacheMills);
		} else if (getClass() == Contact.class) {
			clearCacheContact(cacheMills);
		} else {
			//LogUtils.i("not clear cache:" + getClass().getSimpleName());
			clearCacheSimple(cacheMills);
		}
	}
	
	/**
	 * @Title: clearCacheSimple
	 * @Description: 清除缓存建议实现
	 * @param: @param cacheMills
	 * @param: @return
	 * @return: int
	 * @throws:
	 */
	protected final int clearCacheSimple(long cacheMills) {
		List<? extends BaseDB> outDateList = DBHelper.getInstance().queryAll(
				getClass(),
				new DBWhere(DBKey.birthTime, DBWhereType.lt, System
						.currentTimeMillis() - cacheMills));
		int deleteNum = DBHelper.getInstance().delete(
				getClass(),
				new DBWhere(DBKey.birthTime, DBWhereType.lt, System
						.currentTimeMillis() - cacheMills));
		if (deleteNum != outDateList.size()) {
			throw new IllegalStateException("Delete"
					+ getClass().getSimpleName() + " exception_deleteNum:"
					+ deleteNum + " outDateList.size:" + outDateList.size());
		} else {
			//LogUtils.i("clear_" + getClass().getSimpleName() + "_delteNum:" + deleteNum + " " + outDateList);
		}
		return deleteNum;
	}
	protected final int clearCacheFriendBlack(long cacheMills) {
		List<? extends BaseDB> outDateList = DBHelper.getInstance().queryAll(
				getClass(),
				new DBWhere(DBKey.jid, DBWhereType.ne, LoginManager
						.getInstance().getJidParsed()),
				new DBWhere(DBKey.birthTime, DBWhereType.lt, System
						.currentTimeMillis() - cacheMills));
		int deleteNum = DBHelper.getInstance().delete(
				getClass(),
				new DBWhere(DBKey.jid, DBWhereType.ne, LoginManager
						.getInstance().getJidParsed()),
				new DBWhere(DBKey.birthTime, DBWhereType.lt, System
						.currentTimeMillis() - cacheMills));
		if (deleteNum != outDateList.size()) {
			throw new IllegalStateException("Delete"
					+ getClass().getSimpleName() + " exception_deleteNum:"
					+ deleteNum + " outDateList.size:" + outDateList.size());
		} else {
			//LogUtils.i("clear_" + getClass().getSimpleName() + "_delteNum:" + deleteNum + " " + outDateList);
		}
		return deleteNum;
	}
	protected final int clearCacheContact(long cacheMills) {
		// 自己，好友，黑名单的资料不删除
		String myJid = LoginManager.getInstance().getJidParsed();
		List<UserBlackListDB> blackJids = UserBlackListDB.queryAll(myJid);
		List<UserFriendDB> friendJids = UserFriendDB.queryAll(myJid);
		Set<String> jidSets = new HashSet<String>();
		jidSets.add(myJid);
		if (blackJids != null && blackJids.size() > 0) {
			for (UserBlackListDB db : blackJids) {
				jidSets.add(db.getFieldStr(DBKey.jid_black));
			}
		}
		if (friendJids != null && friendJids.size() > 0) {
			for (UserFriendDB db : friendJids) {
				jidSets.add(db.getFieldStr(DBKey.jid_friend));
			}
		}
		// 每次保存删除那些过期的
		int deleteNum = DBHelper.getInstance().delete(
				getClass(),
				new DBWhere(DBKey.jid, DBWhereType.notIn, jidSets.toArray()),
				new DBWhere(DBKey.birthTime, DBWhereType.lt, System
						.currentTimeMillis() - cacheMills));
		return deleteNum;
	}
	public static String createID(String jid, String gid, String createTime) {
		if (TextUtils.isEmpty(jid) || TextUtils.isEmpty(gid)
				|| TextUtils.isEmpty(createTime)) {
			/*throw new IllegalArgumentException("createID cannot have null value,jid:" + jid + " gid:" + gid
					+ " createTime:" + createTime);*/
			return "";
		}
		//格式化时间
		if (createTime.length() != 10) {
			try {
				Date date = sf1.parse(createTime);
				createTime = sf.format(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jid.contains("@") || createTime.length() != 10) {
			/*throw new IllegalArgumentException("createID cannot have invalid value,jid:" + jid + " gid:" + gid
					+ " createTime:" + createTime);*/
			return "";
		}
		return new StringBuffer().append(jid).append(gid).append(createTime)
				.toString();
	}
}
