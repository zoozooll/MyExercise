package com.butterfly.vv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder.DBOrderType;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.j256.ormlite.field.DatabaseField;

/**
 * @func 评论原型
 * @author yuedong bao
 * @date 2015-2-11 下午4:39:18
 */
public class Comment extends BaseDB implements Serializable {
	private static final long serialVersionUID = 7926564422593049051L;
	// 以下为评论表自己的内容
	@DatabaseField(id = true)
	protected String id;
	@DatabaseField
	private String cid;
	@DatabaseField
	private String toCid = cid_first;
	@DatabaseField
	private String content;
	@DatabaseField
	private String commentTime;
	// 以下为关联ImageFolder和Contact表
	@DatabaseField
	private String gid;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String jid_photogroup;// 评论所处图片组的jid
	@DatabaseField
	private String jid;// 评论者自己的jid
	@DatabaseField
	private String jid_commented;// 被评论者的jid
	@DatabaseField
	private long birthTime;
	public static final String cid_first = "0";

	public String getContent() {
		return content;
	}
	public String getCid() {
		return cid;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public void setCommentTime(String time) {
		this.commentTime = time;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String folderId) {
		this.gid = folderId;
	}

	public final static class ColumnName {
		public static String folderId = "folderId";
	}

	public String getToCid() {
		return toCid;
	}
	public void setToCid(String toCid) {
		this.toCid = toCid;
	}
	// 是否一级评论
	public boolean isCommentLayFirst() {
		return cid_first.equals(toCid);
	}
	public String getGidCreateTime() {
		return createTime;
	}
	public void setGidCreateTime(String gidCreateTime) {
		this.createTime = gidCreateTime;
	}
	public String getId() {
		return new StringBuffer()
				.append(createID(jid_photogroup, gid, createTime))
				.append(getField(DBKey.cid)).toString();
	}
	public String getJidComment() {
		return jid;
	}
	public void setJidComment(String jidComment) {
		this.jid = jidComment;
	}
	public String getJidCommented() {
		return jid_commented;
	}
	public void setJidCommented(String jidCommented) {
		this.jid_commented = jidCommented;
	}
	public String getJidPG() {
		return jid_photogroup;
	}
	public void setJidPG(String jidPG) {
		this.jid_photogroup = jidPG;
	}
	@Override
	public void saveToDatabase() {
		this.id = getId();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static List<Comment> queryAll(String jid, String gid,
			String createTime, int limit, String cid_start) {
		Comment commentPrev = !TextUtils.isEmpty(cid_start) ? DBHelper
				.getInstance().queryForFirst(
						Comment.class,
						new DBWhere(DBKey.jid_photogroup, DBWhereType.eq, jid),
						new DBWhere(DBKey.gid, DBWhereType.eq, gid),
						new DBWhere(DBKey.createTime, DBWhereType.eq,
								createTime),
						new DBWhere(DBKey.cid, DBWhereType.eq, cid_start))
				: null;
		List<Comment> retVals = new ArrayList<Comment>();
		// 表示可能符合条件的一级评论
		List<Comment> commentFirsts;
		if (commentPrev == null) {
			// 先前没有查询过，从头查起
			commentFirsts = DBHelper.getInstance().queryAll(Comment.class,
					new DBOrder(DBKey.commentTime, DBOrderType.desc), limit,
					new DBWhere(DBKey.jid_photogroup, DBWhereType.eq, jid),
					new DBWhere(DBKey.gid, DBWhereType.eq, gid),
					new DBWhere(DBKey.createTime, DBWhereType.eq, createTime),
					new DBWhere(DBKey.toCid, DBWhereType.eq, cid_first));
		} else {
			String commentTimeFirst = commentPrev.getCommentTime();
			if (!commentPrev.isCommentLayFirst()) {
				// 如果是二级评论，则查所属一级评论,再查比这个一级评论更早的评论（含本一级评论）
				Comment commentFirsts2 = DBHelper.getInstance().queryForFirst(
						Comment.class,
						new DBWhere(DBKey.jid_photogroup, DBWhereType.eq, jid),
						new DBWhere(DBKey.gid, DBWhereType.eq, gid),
						new DBWhere(DBKey.createTime, DBWhereType.eq,
								createTime),
						new DBWhere(DBKey.cid, DBWhereType.eq, commentPrev
								.getToCid()));
				commentTimeFirst = commentFirsts2.getCommentTime();
			}
			commentFirsts = DBHelper.getInstance().queryAll(
					Comment.class,
					new DBOrder(DBKey.commentTime, DBOrderType.desc),
					limit,
					new DBWhere(DBKey.jid_photogroup, DBWhereType.eq, jid),
					new DBWhere(DBKey.gid, DBWhereType.eq, gid),
					new DBWhere(DBKey.createTime, DBWhereType.eq, createTime),
					new DBWhere(DBKey.toCid, DBWhereType.eq, cid_first),
					new DBWhere(DBKey.commentTime, DBWhereType.le,
							commentTimeFirst));
		}
		//LogUtils.i("commentFirsts.size-->" + commentFirsts.size());
		for (Comment comment : commentFirsts) {
			if (retVals.size() >= limit) {
				break;
			}
			// 添加一级评论的情况:(1)先前没有查询过评论时，一级评论的第一条（2）一级评论的第二条及以后
			if ((commentFirsts.indexOf(comment) == 0 && commentPrev == null)
					|| commentFirsts.indexOf(comment) > 0) {
				retVals.add(comment);
			}
			List<Comment> commentSecond = null;
			// 查询二级评论：
			if (commentFirsts.indexOf(comment) == 0) {
				// // 第一次查询:
				// (1)先前没查询或者先前查询的是第一级评论
				if (commentPrev == null || commentPrev.isCommentLayFirst()) {
					commentSecond = DBHelper.getInstance().queryAll(
							Comment.class,
							new DBOrder(DBKey.commentTime, DBOrderType.asc),
							limit,
							new DBWhere(DBKey.jid_photogroup, DBWhereType.eq,
									jid),
							new DBWhere(DBKey.gid, DBWhereType.eq, gid),
							new DBWhere(DBKey.createTime, DBWhereType.eq,
									createTime),
							new DBWhere(DBKey.toCid, DBWhereType.eq, comment
									.getCid()));
				} else {
					// (2)先前查询的是二级评论
					commentSecond = DBHelper.getInstance().queryAll(
							Comment.class,
							new DBOrder(DBKey.commentTime, DBOrderType.asc),
							limit,
							new DBWhere(DBKey.jid_photogroup, DBWhereType.eq,
									jid),
							new DBWhere(DBKey.gid, DBWhereType.eq, gid),
							new DBWhere(DBKey.createTime, DBWhereType.eq,
									createTime),
							new DBWhere(DBKey.toCid, DBWhereType.eq, comment
									.getCid()),
							new DBWhere(DBKey.commentTime, DBWhereType.gt,
									comment.getCommentTime()));
				}
			} else {
				// // 非第一次查询：
				commentSecond = DBHelper.getInstance().queryAll(
						Comment.class,
						new DBOrder(DBKey.commentTime, DBOrderType.asc),
						limit,
						new DBWhere(DBKey.jid_photogroup, DBWhereType.eq, jid),
						new DBWhere(DBKey.gid, DBWhereType.eq, gid),
						new DBWhere(DBKey.createTime, DBWhereType.eq,
								createTime),
						new DBWhere(DBKey.toCid, DBWhereType.eq, comment
								.getCid()));
			}
			for (Comment commentTwo : commentSecond) {
				if (retVals.size() >= limit) {
					break;
				}
				retVals.add(commentTwo);
			}
		}
		//LogUtils.i("retVal.size---->" + retVals.size());
		return retVals;
	}
	@Override
	public String toString() {
		return "Comment [cid=" + cid + ", content=" + content + ", gid=" + gid
				+ ", createTime=" + createTime + ", jid_photogroup="
				+ jid_photogroup + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cid == null) ? 0 : cid.hashCode());
		result = prime * result + ((gid == null) ? 0 : gid.hashCode());
		result = prime * result
				+ ((jid_photogroup == null) ? 0 : jid_photogroup.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Comment))
			return false;
		Comment other = (Comment) obj;
		if (cid == null) {
			if (other.cid != null)
				return false;
		} else if (!cid.equals(other.cid))
			return false;
		if (gid == null) {
			if (other.gid != null)
				return false;
		} else if (!gid.equals(other.gid))
			return false;
		if (jid_photogroup == null) {
			if (other.jid_photogroup != null)
				return false;
		} else if (!jid_photogroup.equals(other.jid_photogroup))
			return false;
		return true;
	}
}
