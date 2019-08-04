package com.oregonscientific.meep.message.common;

import java.util.Date;

/**
 * @author marshalschan
 *
 */
public class Chat {
	private String mGroupName;
	private String mFriendName;
	private String mContent;
	private int mContentType;
	private Date mTime;
	private boolean mIsMsgIn;
	
	
	public Chat()
	{
		setGroupName(null);
		setFriendName(null);
		setContent(null);
		setContentType(0);
		setTime(null);
		setMsgIn(false);
	}
	
	public Chat(String groupName, String friendName, String content, int type, Date time, Boolean isMsgIn)
	{
		setGroupName(groupName);
		setFriendName(friendName);
		setContent(content);
		setContentType(0);
		setTime(time);
		setMsgIn(isMsgIn);
	}
	
	public String getGroupName() {
		return mGroupName;
	}

	public void setGroupName(String groupName) {
		this.mGroupName = groupName;
	}

	public String getFriendName() {
		return mFriendName;
	}

	public void setFriendName(String friendName) {
		this.mFriendName = friendName;
	}
	
	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}
	public int getContentType() {
		return mContentType;
	}
	
	
	/**
	 * @param contentType 0: text, 1: url
	 */
	public void setContentType(int contentType) {
		this.mContentType = contentType;
	}
	public Date getTime() {
		return mTime;
	}
	public void setTime(Date time) {
		this.mTime = time;
	}
	public boolean IsMsgIn() {
		return mIsMsgIn;
	}
	public void setMsgIn(boolean isMsgIn) {
		this.mIsMsgIn = isMsgIn;
	}
}
