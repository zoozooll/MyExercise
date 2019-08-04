package com.butterfly.vv.db.ormhelper.bean;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;

/**
 * @ClassName: SynDataDB
 * @Description: 存储同步数据的hash值
 * @author: yuedong bao
 * @date: 2015-7-14 下午3:06:47
 */
public class SynDataDB extends BaseDB {
	@DatabaseField(id = true)
	protected String id;
	@DatabaseField
	protected String jid;
	@DatabaseField
	protected SynDataType synDataType;
	@DatabaseField
	protected String digest;
	@DatabaseField
	protected long birthTime;

	@Override
	public void saveToDatabase() {
		id = synDataType + jid;
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	// 查询同步数据的hash值
	public static SynDataDB query(SynDataType type, String jid) {
		String id = type + jid;
		return DBHelper.getInstance().queryForFirst(SynDataDB.class,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}

	public enum SynDataType {
		roster, contactInfo, blacklist;
	}
}
