package com.beem.project.btf.ui.entity;

import java.io.Serializable;

import com.butterfly.vv.model.CommentItem;

/**
 * 点赞和评论传递数据更新的实体
 * @author le yang
 */
public class PraiseEventBusData implements Serializable {
	private static final long serialVersionUID = 280352630297723833L;
	private String jid;
	private String createTime;
	private String gid;
	private boolean isThumbUped;
	private CommentItem Comment;

	public PraiseEventBusData() {
	}
	/**
	 * @param jid时光号
	 * @param gid图片组id
	 * @param createTime图片组创建时间
	 * @param isThumbUped是否点赞
	 */
	public PraiseEventBusData(String jid, String gid, String createTime,
			boolean isThumbUped) {
		this(jid, gid, createTime, isThumbUped, null);
	}
	/**
	 * @param jid时光号
	 * @param gid图片组id
	 * @param createTime图片组创建时间
	 * @param Comment评论组合体
	 */
	public PraiseEventBusData(String jid, String gid, String createTime,
			CommentItem Comment) {
		this(jid, gid, createTime, false, Comment);
	}
	/**
	 * @param jid时光号
	 * @param gid图片组id
	 * @param createTime图片组创建时间
	 * @param isThumbUped是否点赞
	 * @param Comment评论组合体
	 */
	public PraiseEventBusData(String jid, String gid, String createTime,
			boolean isThumbUped, CommentItem Comment) {
		this.jid = jid;
		this.gid = gid;
		this.createTime = createTime;
		this.isThumbUped = isThumbUped;
		this.Comment = Comment;
	}
	/**
	 * @param jid时光号
	 * @param gid图片组id
	 * @param createTime图片组创建时间
	 * @param isThumbUped是否点赞
	 * @param Comment评论组合体
	 */
	public PraiseEventBusData(String jid, String gid, String createTime,
			boolean isThumbUped, CommentItem Comment, String str) {
		this.jid = jid;
		this.gid = gid;
		this.createTime = createTime;
		this.isThumbUped = isThumbUped;
		this.Comment = Comment;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public boolean isThumbUped() {
		return isThumbUped;
	}
	public void setThumbUped(boolean isThumbUped) {
		this.isThumbUped = isThumbUped;
	}
	public CommentItem getComment() {
		return Comment;
	}
	public void setComment(CommentItem comment) {
		this.Comment = comment;
	}
	@Override
	public String toString() {
		return "PraiseEventBusData [jid=" + jid + ", createTime=" + createTime
				+ ", gid=" + gid + ", isThumbUped=" + isThumbUped
				+ ", Comment=" + Comment + "]";
	}
}
