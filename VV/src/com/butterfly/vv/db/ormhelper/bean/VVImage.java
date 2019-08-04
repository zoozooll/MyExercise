package com.butterfly.vv.db.ormhelper.bean;

import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.field.DatabaseField;

public class VVImage extends BaseDB implements Serializable {
	private static final long serialVersionUID = 4942805189278313981L;
	@DatabaseField(id = true)
	protected String id;
	@DatabaseField
	private String path;
	@DatabaseField
	private String pathThumb;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String jid;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String pid;
	// 图片是否上传完成标志
	@DatabaseField
	private boolean isLoading;
	// 图片原始路径
	@DatabaseField
	private String diskPath;
	@DatabaseField
	private long birthTime;

	// 以下字段未用
	public String getGid() {
		return gid;
	}
	public String getPid() {
		return pid;
	}
	public String getPath() {
		return path;
	}
	public String getJid() {
		return jid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public void setPid(String id) {
		this.pid = id;
	}
	public void setPath(String path) {
		if (path == null) {
			path = "";
		}
		this.path = path;
	}
	public VVImage setJid(String uid) {
		this.jid = uid;
		return this;
	}
	public String getPathThumb() {
		return pathThumb;
	}
	public void setPathThumb(String path_thumb) {
		this.pathThumb = path_thumb;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public boolean getImageisLoading() {
		return isLoading;
	}
	public void setImageisLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
	public String getDiskPath() {
		return diskPath;
	}
	public void setDiskPath(String originalPath) {
		this.diskPath = originalPath;
	}
	@Override
	public void saveToDatabase() {
		if (TextUtils.isEmpty(jid) || TextUtils.isEmpty(createTime)
				|| TextUtils.isEmpty(pid) || TextUtils.isEmpty(gid)) {
			throw new IllegalArgumentException(
					"VVImage_db must have the id,jid:" + jid + " createTime:"
							+ createTime + " pid:" + pid + " gid:" + gid);
		}
		id = new StringBuffer().append(createID(jid, gid, createTime))
				.append(pid).toString();
		setField(DBKey.id, id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static List<VVImage> queryAll(String jid, String gid,
			String createTime) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		List<VVImage> vvimage = DBHelper.getInstance().queryAll(VVImage.class,
				new DBWhere(DBKey.gid, DBWhereType.eq, gid),
				new DBWhere(DBKey.createTime, DBWhereType.eq, createTime),
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
		return vvimage;
	}
	public static void delete(String jid, String gid, String createTime,
			String pid) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		DBHelper.getInstance().delete(VVImage.class,
				new DBWhere(DBKey.gid, DBWhereType.eq, gid),
				new DBWhere(DBKey.createTime, DBWhereType.eq, createTime),
				new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.pid, DBWhereType.eq, pid));
	}
	@Override
	public String toString() {
		return "VVImage [path=" + path + ", pathThumb=" + pathThumb
				+ ", createTime=" + createTime + ", jid=" + jid + ", gid="
				+ gid + ", pid=" + pid + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result
				+ ((pathThumb == null) ? 0 : pathThumb.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VVImage))
			return false;
		VVImage other = (VVImage) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (pathThumb == null) {
			if (other.pathThumb != null)
				return false;
		} else if (!pathThumb.equals(other.pathThumb))
			return false;
		return true;
	}
}
