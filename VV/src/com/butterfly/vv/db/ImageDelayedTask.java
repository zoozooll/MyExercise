package com.butterfly.vv.db;

import java.io.Serializable;

import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.j256.ormlite.field.DatabaseField;

/**
 * @ClassName: ImageDelayedTask
 * @Description:等待上传的图片任务
 * @author: yuedong bao
 * @date: 2015-5-19 上午10:48:51
 */
public class ImageDelayedTask extends BaseDB implements Serializable {
	private static final long serialVersionUID = 1L;
	@DatabaseField(id = true)
	String uri;
	@DatabaseField
	String folderId;
	@DatabaseField
	String jid;
	@DatabaseField
	String lon;
	@DatabaseField
	String lat;
	public int pos;

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public ImageDelayedTask(String uri, String folderId, String jid,
			String result, String lon, String lat) {
		this.uri = uri;
		this.folderId = folderId;
		this.jid = jid;
		this.lon = lon;
		this.lat = lat;
	}
	public ImageDelayedTask() {
	}
	@Override
	public void saveToDatabase() {
	}
}
