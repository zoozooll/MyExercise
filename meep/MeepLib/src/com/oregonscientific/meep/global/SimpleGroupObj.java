package com.oregonscientific.meep.global;

public class SimpleGroupObj {
	private int mId;
	private String mName = null;
	private String mIconAddr = null;
	
	public SimpleGroupObj() {
		setId(0);
		setName("");
		setIconAddr("");
	}
	
	public SimpleGroupObj(int id, String name, String iconAddr){
		setId(id);
		setName(name);
		setIconAddr(iconAddr);
	}
	
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		this.mId = id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getIconAddr() {
		return mIconAddr;
	}
	public void setIconAddr(String iconAddr) {
		this.mIconAddr = iconAddr;
	}

	
}
