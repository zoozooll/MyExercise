package com.butterfly.vv.db.ormhelper.bean;

import java.util.List;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @func 黑名单表
 * @author yuedong bao
 * @time 2015-1-21 下午12:28:12
 */
@DatabaseTable
public class UserBlackListDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String jid_black;
	@DatabaseField
	protected long birthTime;

	@Override
	public String toString() {
		return "UserBlackListDB [id=" + id + ", jid=" + jid + ", jid_b="
				+ jid_black + "]";
	}
	@Override
	public void saveToDatabase() {
		id = new StringBuffer().append(jid).append(jid_black).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static void deleteDatabase(String jid, String jid_black) {
		String quereyId = new StringBuffer().append(jid).append(jid_black)
				.toString();
		DBHelper.getInstance().delete(UserBlackListDB.class,
				new DBWhere(DBKey.id, DBWhereType.eq, quereyId));
	}
	// 查阅jid的所有黑名单信息
	public static List<UserBlackListDB> queryAll(String jid) {
		return DBHelper.getInstance().queryAll(UserBlackListDB.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
	}
	// 查阅jid的所有黑名单信息
	public static List<UserBlackListDB> queryAll() {
		return DBHelper.getInstance().queryAll(UserBlackListDB.class);
	}
	public static UserBlackListDB queryForFirst(String jid, String jid_black) {
		return DBHelper.getInstance().queryForFirst(UserBlackListDB.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.jid_black, DBWhereType.eq, jid_black));
	}
}
