package com.butterfly.vv.db.ormhelper.bean;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.field.DatabaseField;

public class ImageFolderNotify extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String notify_time;
	@DatabaseField
	private String notify_valid;
	@DatabaseField
	private long birthTime;

	@Override
	public void saveToDatabase() {
		id = createID(jid, gid, createTime);
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static ImageFolderNotify queryForFirst(String jid, String gid,
			String createTime) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		return DBHelper.getInstance().queryForFirst(ImageFolderNotify.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.gid, DBWhereType.eq, gid),
				new DBWhere(DBKey.createTime, DBWhereType.eq, createTime));
	}
}
