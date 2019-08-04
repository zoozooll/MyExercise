package com.butterfly.vv.db.ormhelper.bean;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder.DBOrderType;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;

public class TopNDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String jid;
	@DatabaseField
	private int thumbupCount;
	@DatabaseField
	private String jid_login;
	@DatabaseField
	private long birthTime;

	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getJid_login() {
		return jid_login;
	}
	public void setJid_login(String jid_login) {
		this.jid_login = jid_login;
	}
	public long getBirthTime() {
		return birthTime;
	}
	public void setBirthTime(long birthTime) {
		this.birthTime = birthTime;
	}
	// 新增数据库的字段时，添加set方法，并且在其中调用putMap
	@Override
	public void saveToDatabase() {
		id = new StringBuffer().append(getField(DBKey.jid_login))
				.append(createID(jid, gid, createTime)).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, getFieldStr(DBKey.id)));
	}
	public static void deleteAll(String login) {
		DBHelper.getInstance().deleteAsync(TopNDB.class,
				new DBWhere(DBKey.jid_login, DBWhereType.eq, login));
	}
	public static List<TopNDB> query(String start, int limit) {
		List<TopNDB> retVal;
		if (TextUtils.isEmpty(start)) {
			retVal = DBHelper.getInstance().queryAll(TopNDB.class,
					new DBOrder(DBKey.thumbupCount, DBOrderType.desc), limit);
		} else {
			List<TopNDB> all = DBHelper.getInstance().queryAll(TopNDB.class,
					new DBOrder(DBKey.thumbupCount, DBOrderType.desc), -1);
			boolean isStart = false;
			retVal = new ArrayList<TopNDB>();
			for (int i = 0; i < all.size(); i++) {
				if (retVal.size() >= limit) {
					break;
				}
				if (isStart) {
					retVal.add(all.get(i));
				} else {
					if (start.equals(all.get(i).getField(DBKey.id))) {
						isStart = true;
					}
				}
			}
		}
		return retVal;
	}
}
