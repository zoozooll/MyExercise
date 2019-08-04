/**
 * 
 */
package com.butterfly.vv.db.ormhelper.bean;

import android.util.Base64;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author hongbo ke
 *
 */
@DatabaseTable (tableName="friend_request")
public class FriendRequest extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String jidFrom;
	@DatabaseField
	private String jidTo;
	@DatabaseField
	private String time;
	@DatabaseField
	private String content;
	@DatabaseField
	private int status;
	@DatabaseField
	protected long birthTime;
	
	public String getJidFrom() {
		return jidFrom;
	}

	public void setJidFrom(String jidFrom) {
		fields.put("jidFrom", jidFrom);
		this.jidFrom = jidFrom;
	}

	public String getJidTo() {
		return jidTo;
	}

	public void setJidTo(String jidTo) {
		fields.put("jidTo", jidTo);
		this.jidTo = jidTo;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		fields.put("time", time);
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	/***
	 * set status of FriendRequest object:
	 * 0 .... verify;
	 * 1 .... confirm;
	 * 2 .... refuse;
	 * @param status
	 */
	public void setStatus(int status) {
		fields.put("status", status);
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		fields.put("content", content);
		this.content = content;
	}

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.butterfly.vv.db.ormhelper.bean.BaseDB#saveToDatabase()
	 */
	@Override
	public void saveToDatabase() {
		id = Base64.encodeToString((jidFrom).getBytes(), Base64.DEFAULT);
		fields.put("id", id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
}
