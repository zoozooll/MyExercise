/**
 * 
 */
package com.butterfly.vv.db.ormhelper.bean;

import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author hongbo ke
 */
@DatabaseTable(tableName = "like_notifies")
public class LikedNotifies extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String time;
	@DatabaseField
	private String fromJid;
	@DatabaseField
	private String toJid;
	@DatabaseField
	private String type;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String gidJid;
	@DatabaseField
	private String gidCreatTime;
	@DatabaseField
	private String likeTime;
	@DatabaseField (defaultValue = "false")
	private boolean isCheck;
	@DatabaseField
	protected long birthTime;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		fields.put("time", time);
		this.time = time;
	}

	public String getFromJid() {
		return fromJid;
	}

	public void setFromJid(String fromJid) {
		fields.put("fromJid", fromJid);
		this.fromJid = fromJid;
	}

	public String getToJid() {
		return toJid;
	}

	public void setToJid(String toJid) {
		fields.put("toJid", toJid);
		this.toJid = toJid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		fields.put("type", type);
		this.type = type;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		fields.put("gid", gid);
		this.gid = gid;
	}

	public String getGidJid() {
		return gidJid;
	}

	public void setGidJid(String gidJid) {
		fields.put("gidJid", gidJid);
		this.gidJid = gidJid;
	}

	public String getGidCreatTime() {
		return gidCreatTime;
	}

	public void setGidCreatTime(String gidCreatTime) {
		fields.put("gidCreatTime", gidCreatTime);
		this.gidCreatTime = gidCreatTime;
	}

	public String getLikeTime() {
		return likeTime;
	}

	public void setLikeTime(String likeTime) {
		fields.put("likeTime", likeTime);
		this.likeTime = likeTime;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		fields.put("isCheck", isCheck);
		this.isCheck = isCheck;
	}

	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.butterfly.vv.db.ormhelper.bean.BaseDB#saveToDatabase()
	 */
	@Override
	public void saveToDatabase() {
		id = gid + VVXMPPUtils.makeJidParsed(gidJid)
				+ BBSUtils.getShortGidCreatTime(gidCreatTime) + fromJid;
		fields.put("id", id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
}
