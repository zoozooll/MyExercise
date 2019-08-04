package com.butterfly.vv.db;

import com.j256.ormlite.field.DatabaseField;

/**
 * who add me to their blackroster list
 * @author xun zhong
 */
public class Blacklisted {
	@DatabaseField(id = true)
	private String mJid;

	public void setUId(String user) {
		mJid = user;
	}
	public String getUId() {
		return mJid;
	}
}
