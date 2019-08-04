package com.butterfly.vv.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "provice")
public class Province {
	@DatabaseField
	long _id;
	@DatabaseField(id = true)
	String pcode;
	@DatabaseField
	String pname;

	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	@Override
	public String toString() {
		return "Province [_id=" + _id + ", pcode=" + pcode + ", pname=" + pname
				+ "]";
	}
}
