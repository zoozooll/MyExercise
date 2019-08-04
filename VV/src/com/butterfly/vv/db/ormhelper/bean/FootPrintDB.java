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

public class FootPrintDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String distance;
	@DatabaseField
	private String jid_login;
	@DatabaseField
	private long birthTime;

	@Override
	public void saveToDatabase() {
		id = new StringBuffer().append(getFieldStr(DBKey.jid_login))
				.append(createID(jid, gid, createTime)).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, getFieldStr(DBKey.id)));
	}
	public static void deleteAll(String login) {
		if (TextUtils.isEmpty(login)) {
			DBHelper.getInstance().deleteAsync(FootPrintDB.class);
		} else {
			DBHelper.getInstance().deleteAsync(FootPrintDB.class,
					new DBWhere(DBKey.jid_login, DBWhereType.eq, login));
		}
	}
	/**
	 * @Title: query
	 * @Description: TODO
	 * @param start 上一次查询的jdc数据,本次查询从此查起，结果中并不含start对应的数据
	 * @param limit :查询的数目
	 * @return
	 * @return: List<FootPrintDB>
	 */
	public static List<FootPrintDB> query(String start, int limit) {
		List<FootPrintDB> retVal;
		if (TextUtils.isEmpty(start)) {
			retVal = DBHelper.getInstance().queryAll(FootPrintDB.class,
					new DBOrder(DBKey.distance, DBOrderType.asc), limit);
		} else {
			List<FootPrintDB> all = DBHelper.getInstance().queryAll(
					FootPrintDB.class,
					new DBOrder(DBKey.distance, DBOrderType.asc), -1);
			boolean isStart = false;
			retVal = new ArrayList<FootPrintDB>();
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
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getJid_login() {
		return jid_login;
	}
	public void setJid_login(String jid_login) {
		this.jid_login = jid_login;
	}
}
