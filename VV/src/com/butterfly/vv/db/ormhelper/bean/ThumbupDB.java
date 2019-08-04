package com.butterfly.vv.db.ormhelper.bean;

import android.text.TextUtils;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;

public class ThumbupDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String jid_photogroup;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String gid;
	@DatabaseField
	private boolean isThumbUp;

	@Override
	public void saveToDatabase() {
		id = createId(jid, jid_photogroup, createTime, gid);
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	//查询是否点赞过某个图片组
	public static boolean queryThumbup(String jid, String jid_photogroup,
			String createTime, String gid) {
		ThumbupDB thumbupDBOne = DBHelper.getInstance().queryForFirst(
				ThumbupDB.class,
				new DBWhere(DBKey.id, DBWhereType.eq, createId(jid,
						jid_photogroup, createTime, gid)));
		return thumbupDBOne != null ? thumbupDBOne.isThumbUp : false;
	}
	private static String createId(String jid, String jid_photogroup,
			String createTime, String gid) {
		if (TextUtils.isEmpty(jid) || TextUtils.isEmpty(jid_photogroup)
				|| TextUtils.isEmpty(createTime) || TextUtils.isEmpty(gid)) {
			throw new IllegalArgumentException("save to thumb error,jid:" + jid
					+ " jid_photogroup:" + jid_photogroup + " createTime:"
					+ createTime + " gid:" + gid);
		}
		return new StringBuffer().append(jid)
				.append(createID(jid_photogroup, gid, createTime)).toString();
	}
}
