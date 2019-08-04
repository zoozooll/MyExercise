package com.butterfly.vv.db;

import com.j256.ormlite.field.DatabaseField;

/**
 * @func 聊天类(本地保存历史聊天数据)
 * @author yuedong bao
 * @date 2015-2-13 上午10:41:25
 */
public class ImSession {
	@DatabaseField(generatedId = true)
	private int s_id;
	@DatabaseField
	private String s_name;
	@DatabaseField
	private String s_who_send;
	@DatabaseField
	private String s_date;
	@DatabaseField
	private String s_msg;
	@DatabaseField
	private int my_s_id;
	@DatabaseField
	private int type;

	public String getS_name() {
		return s_name;
	}
	public void setS_name(String s_name) {
		this.s_name = s_name;
	}
	public String getS_who_send() {
		return s_who_send;
	}
	public void setS_who_send(String s_who_send) {
		this.s_who_send = s_who_send;
	}
	public String getS_date() {
		return s_date;
	}
	public void setS_date(String s_date) {
		this.s_date = s_date;
	}
	public String getS_msg() {
		return s_msg;
	}
	public void setS_msg(String s_msg) {
		this.s_msg = s_msg;
	}
	public int getS_id() {
		return s_id;
	}
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	public int getMy_s_id() {
		return my_s_id;
	}
	public void setMy_s_id(int my_s_id) {
		this.my_s_id = my_s_id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
