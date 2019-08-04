package com.butterfly.vv.db;

import com.j256.ormlite.field.DatabaseField;

/**
 * @func 聊天联系人的资料
 * @author yuedong bao
 * @date 2015-2-13 上午11:25:26
 */
public class ImOwner {
	@DatabaseField(id = true)
	private String o_jid;
	@DatabaseField
	private String o_name;
	@DatabaseField
	private int o_id;
	@DatabaseField
	private String o_avatar;
	@DatabaseField
	private String o_pwd;

	public String getO_name() {
		return o_name;
	}
	public void setO_name(String o_name) {
		this.o_name = o_name;
	}
	public int getO_id() {
		return o_id;
	}
	public void setO_id(int o_id) {
		this.o_id = o_id;
	}
	public String getO_jid() {
		return o_jid;
	}
	public void setO_jid(String o_jid) {
		this.o_jid = o_jid;
	}
	public String getO_avatar() {
		return o_avatar;
	}
	public void setO_avatar(String o_avatar) {
		this.o_avatar = o_avatar;
	}
	public String getO_pwd() {
		return o_pwd;
	}
	public void setO_pwd(String o_pwd) {
		this.o_pwd = o_pwd;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("o_jid=").append(o_jid).append(" o_name=")
				.append(o_name).append(" o_pwd=").append(o_pwd)
				.append(" o_avatar=").append(o_avatar);
		return builder.toString();
	}
}
