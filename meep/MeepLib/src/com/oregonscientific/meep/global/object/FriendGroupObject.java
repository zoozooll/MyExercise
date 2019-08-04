package com.oregonscientific.meep.global.object;

public class FriendGroupObject {

	private int mId = -1;
	private String mName = null;
	private String mIconAddr = null;
	private int mChildCount = 0;
	
	public FriendGroupObject(int id, String name, String iconAddr, int childCount)
	{
		mId = id;
		mName = name;
		mIconAddr = iconAddr;
		mChildCount = childCount;
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
	public int getChildCount() {
		return mChildCount;
	}
	public void setChildCount(int childCount) {
		this.mChildCount = childCount;
	}
}
