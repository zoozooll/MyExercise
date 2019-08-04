package com.butterfly.vv.db.ormhelper.bean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @func 好友列表
 * @author yuedong bao
 * @time 2015-1-21 下午12:22:02
 */
@DatabaseTable
public class UserFriendDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String jid_friend;
	@DatabaseField
	private String alias;
	@DatabaseField
	private String nickName;
	@DatabaseField
	protected long birthTime;

	@Override
	public String toString() {
		return "UserFriendDB [id=" + id + ", jid=" + jid + ", jid_friend="
				+ jid_friend + ", alias=" + alias + ", nickName=" + nickName
				+ "]";
	}
	@Override
	public void saveToDatabase() {
		id = new StringBuffer().append(jid).append(jid_friend).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static void deleteDatabase(String jid, String jid_friend) {
		DBHelper.getInstance().delete(UserFriendDB.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.jid_friend, DBWhereType.eq, jid_friend));
	}
	public static UserFriendDB queryForFirst(String jid, String jid_friend) {
		return DBHelper.getInstance().queryForFirst(UserFriendDB.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.jid_friend, DBWhereType.eq, jid_friend));
	}
	public static List<UserFriendDB> queryAll(String jid) {
		return DBHelper.getInstance().queryAll(UserFriendDB.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
	}
	public static List<UserFriendDB> queryAllLike(String jid, String likeVal) {
		try {
			Dao<UserFriendDB, ?> dao = DBHelper.getInstance().getDao(
					UserFriendDB.class);
			QueryBuilder<UserFriendDB, ?> queryBuilder = dao.queryBuilder();
			queryBuilder.where()
					.like(DBKey.jid_friend.toString(), "%" + likeVal + "%")
					.or().like(DBKey.nickName.toString(), "%" + likeVal + "%")
					.or().like(DBKey.alias.toString(), "%" + likeVal + "%")
					.and().eq(DBKey.jid.toString(), jid);
			return queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<UserFriendDB>();
	}
}
