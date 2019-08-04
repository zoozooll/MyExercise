/**
 * 
 */
package com.butterfly.vv.db.ormhelper.bean;

import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.utils.BBSUtils;
import com.btf.push.Item.MsgType;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author hongbo ke
 *
 */
@DatabaseTable (tableName="comment_notifies")
public class CommentNotifies extends BaseDB {
	
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String time;
	@DatabaseField
	private String fromJid;
	@DatabaseField
	private Status type;
	@DatabaseField
	private String toJid;
	@DatabaseField
	private String commentId;
	@DatabaseField
	private String toCommentCid;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String gidJid;
	@DatabaseField
	private String gidCreatTime;
	@DatabaseField
	private String body;
	@DatabaseField
	private String comment_time;
	@DatabaseField(defaultValue = "false")
	private boolean isCheck;
	@DatabaseField
	protected long birthTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public Status getType() {
		return type;
	}
	public void setType(Status type) {
		fields.put("type", type);
		this.type = type;
	}
	public String getToJid() {
		return toJid;
	}
	public void setToJid(String toJid) {
		fields.put("toJid", toJid);
		this.toJid = toJid;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		fields.put("commentId", commentId);
		this.commentId = commentId;
	}
	public String getToCommentCid() {
		return toCommentCid;
	}
	public void setToCommentCid(String toCommentCid) {
		fields.put("toCommentCid", toCommentCid);
		this.toCommentCid = toCommentCid;
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
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		fields.put("body", body);
		this.body = body;
	}
	public String getComment_time() {
		return comment_time;
	}
	public void setComment_time(String comment_time) {
		fields.put("comment_time", comment_time);
		this.comment_time = comment_time;
	}
	/* (non-Javadoc)
	 * @see com.butterfly.vv.db.ormhelper.bean.BaseDB#saveToDatabase()
	 */
	@Override
	public void saveToDatabase() {
		id = gid + BBSUtils.getShortGidCreatTime(gidCreatTime) + VVXMPPUtils.makeJidParsed(gidJid) + commentId;
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		fields.put("isCheck", isCheck);
		this.isCheck = isCheck;
	}

	public static enum Status {
		UNCHECK, CHECK
	}
}
