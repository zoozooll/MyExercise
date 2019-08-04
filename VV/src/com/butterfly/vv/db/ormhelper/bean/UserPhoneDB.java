package com.butterfly.vv.db.ormhelper.bean;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;

/**
 * @ClassName: UserPhoneDB
 * @Description: 用户手机联系人表
 * @author: yuedong bao
 * @date: 2015-4-8 上午9:22:47
 */
public class UserPhoneDB extends BaseDB {
	@DatabaseField(id = true)
	protected String id;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String jid_phone;
	@DatabaseField
	protected long birthTime;

	@Override
	public void saveToDatabase() {
		id = new StringBuffer().append(jid).append(jid_phone).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
}
