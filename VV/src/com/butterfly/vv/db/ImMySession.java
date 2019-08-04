package com.butterfly.vv.db;

import com.j256.ormlite.field.DatabaseField;

public class ImMySession {
	@DatabaseField(generatedId = true)
	private int s_id;
	@DatabaseField
	private String chatwith;
	@DatabaseField
	private int o_id;

	public String getChatwith() {
		return chatwith;
	}
	public void setChatwith(String chatwith) {
		this.chatwith = chatwith;
	}
	public int getS_id() {
		return s_id;
	}
	public void setS_id(int s_id) {
		this.s_id = s_id;
	}
	public int getO_id() {
		return o_id;
	}
	public void setO_id(int o_id) {
		this.o_id = o_id;
	}
}
