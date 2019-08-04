/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jivesoftware.smack.packet.Message.Comment;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.packet.DelayInformation;

import android.os.Parcel;
import android.os.Parcelable;

import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.activity.ChatActivity.MessageState;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;

/**
 * This class represents a instant message.
 * @author darisk
 */
public class Message implements Parcelable {
	/** Normal message type. Theese messages are like an email, with subject. */
	public static final int MSG_TYPE_NORMAL = 100;
	/** Chat message type. */
	public static final int MSG_TYPE_CHAT = 200;
	/** Group chat message type.群聊功能 */
	public static final int MSG_TYPE_GROUP_CHAT = 300;
	/** Error message type. */
	public static final int MSG_TYPE_ERROR = 400;
	/** Informational message type. */
	public static final int MSG_TYPE_INFO = 500;
	public static final int MSG_TYPE_NOTIFICATION = 600;
	public static final int MSG_TYPE_COMMENT = 700;
	/** Parcelable.Creator needs by Android. */
	public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
		@Override
		public Message createFromParcel(Parcel source) {
			return new Message(source);
		}
		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}
	};
	private int mType;
	private MsgTypeSub subType;
	private String mId;
	private String mBody;
	private String mSubject;
	private String mTo;
	private String mFrom;
	private String mThread;
	private Date mTimestamp;
	private final Comment comment = new Comment();
	private MessageState msgState = MessageState.preloading;

	// TODO ajouter l'erreur
	/**
	 * Constructor.
	 * @param to the destinataire of the message
	 * @param type the message type
	 */
	public Message(final String to, final int type) {
		mTo = to;
		mType = type;
		mId = "";
		mBody = "";
		mSubject = "";
		mThread = "";
		mFrom = null;
		mTimestamp = new Date();
	}
	/**
	 * Constructor a message of type chat.
	 * @param to the destinataire of the message
	 */
	public Message(final String to) {
		this(to, MSG_TYPE_CHAT);
	}
	/**
	 * Construct a message from a smack message packet.
	 * @param smackMsg Smack message packet
	 */
	/*
	 * public Message(final org.jivesoftware.smack.packet.Message smackMsg) {
	 * this(smackMsg.getTo()); switch (smackMsg.getType()) { case chat: mType = MSG_TYPE_CHAT;
	 * break; case groupchat: mType = MSG_TYPE_GROUP_CHAT; break; case normal: mType =
	 * MSG_TYPE_NORMAL; break; // TODO gerer les message de type error // this a little work around
	 * waiting for a better handling of error // messages case error: mType = MSG_TYPE_ERROR; break;
	 * default: mType = MSG_TYPE_NORMAL; break; } this.mFrom = smackMsg.getFrom(); //TODO better
	 * handling of error messages if (mType == MSG_TYPE_ERROR) { XMPPError er = smackMsg.getError();
	 * String msg = er.getMessage(); if (msg != null) mBody = msg; else mBody = er.getCondition(); }
	 * else { mBody = smackMsg.getBody(); mSubject = smackMsg.getSubject(); mThread =
	 * smackMsg.getThread(); } PacketExtension pTime = smackMsg.getExtension("delay",
	 * "urn:xmpp:delay"); if (pTime instanceof DelayInformation) { mTimestamp = ((DelayInformation)
	 * pTime).getStamp(); } else { mTimestamp = new Date(); } }
	 */
	public Message(final org.jivesoftware.smack.packet.Message smackMsg) {
		this(smackMsg.getTo());
		switch (smackMsg.getType()) {
			case headline:
				mType = MSG_TYPE_NOTIFICATION;
				break;
			case chat:
				mType = MSG_TYPE_CHAT;
				break;
			case groupchat:
				mType = MSG_TYPE_GROUP_CHAT;
				break;
			case normal:
				mType = MSG_TYPE_NORMAL;
				break;
			// TODO gerer les message de type error
			// this a little work around waiting for a better handling of error
			// messages
			case error:
				mType = MSG_TYPE_ERROR;
				break;
			case comment:
				mType = MSG_TYPE_COMMENT;
				break;
			default:
				mType = MSG_TYPE_NORMAL;
				break;
		}
		this.mFrom = smackMsg.getFrom();
		// TODO better handling of error messages
		mId = smackMsg.getPacketID();
		if (mType == MSG_TYPE_ERROR) {
			XMPPError er = smackMsg.getError();
			String msg = er.getMessage();
			if (msg != null)
				mBody = msg;
			else
				mBody = er.getCondition();
		} else {
			mBody = smackMsg.getBody();
			mSubject = smackMsg.getSubject();
			mThread = smackMsg.getThread();
		}
		PacketExtension pTime = smackMsg
				.getExtension("delay", "urn:xmpp:delay");
		if (pTime instanceof DelayInformation) {
			mTimestamp = ((DelayInformation) pTime).getStamp();
		} else {
			mTimestamp = new Date();
		}
		// 增加time字段#baoyuedong 2015-03-16 added
		setTimestampStr(smackMsg.getTime());
	}
	/**
	 * Construct a message from a parcel.
	 * @param in parcel to use for construction
	 */
	private Message(final Parcel in) {
		mType = in.readInt();
		mId = in.readString();
		mTo = in.readString();
		mBody = in.readString();
		mSubject = in.readString();
		mThread = in.readString();
		mFrom = in.readString();
		mTimestamp = new Date(in.readLong());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(mType);
		dest.writeString(mId);
		dest.writeString(mTo);
		dest.writeString(mBody);
		dest.writeString(mSubject);
		dest.writeString(mThread);
		dest.writeString(mFrom);
		dest.writeLong(mTimestamp.getTime());
	}
	/**
	 * Get the type of the message.
	 * @return the type of the message.
	 */
	public int getType() {
		return mType;
	}
	/**
	 * Set the type of the message.
	 * @param type the type to set
	 */
	public void setType(int type) {
		mType = type;
	}
	/**
	 * Get the body of the message.
	 * @return the Body of the message
	 */
	public String getBody() {
		return mBody;
	}
	public String getId() {
		return mId;
	}
	public void setId(String id) {
		mId = id;
	}
	/**
	 * Set the body of the message.
	 * @param body the body to set
	 */
	public void setBody(String body) {
		mBody = body;
	}
	/**
	 * Get the subject of the message.
	 * @return the subject
	 */
	public String getSubject() {
		return mSubject;
	}
	/**
	 * Set the subject of the message.
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		mSubject = subject;
	}
	/**
	 * Get the destinataire of the message.
	 * @return the destinataire of the message
	 */
	public String getTo() {
		return mTo;
	}
	/**
	 * Set the destinataire of the message.
	 * @param to the destinataire to set
	 */
	public void setTo(String to) {
		mTo = to;
	}
	/**
	 * Set the from field of the message.
	 * @param from the mFrom to set
	 */
	public void setFrom(String from) {
		this.mFrom = from;
	}
	/**
	 * Get the from field of the message.
	 * @return the mFrom
	 */
	public String getFrom() {
		return mFrom;
	}
	/**
	 * Get the thread of the message.
	 * @return the thread
	 */
	public String getThread() {
		return mThread;
	}
	/**
	 * Set the thread of the message.
	 * @param thread the thread to set
	 */
	public void setThread(String thread) {
		mThread = thread;
	}
	/**
	 * Set the Date of the message.
	 * @param date date of the message.
	 */
	public void setTimestamp(Date date) {
		mTimestamp = date;
	}
	public void setTimestampStr(String dataStr) {
		try {
			mTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Get the Date of the message.
	 * @return if it is a delayed message get the date the message was sended.
	 */
	public Date getTimestamp() {
		return mTimestamp;
	}
	public String getTimestampStr() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mTimestamp);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	protected org.jivesoftware.smack.packet.Message toSmackMessage() {
		org.jivesoftware.smack.packet.Message send = new org.jivesoftware.smack.packet.Message();
		String msgBody = this.getBody();
		send.setTo((this.getTo()));
		send.setBody(msgBody);
		send.setThread(this.getThread());
		send.setSubject(this.getSubject());
		send.setType(org.jivesoftware.smack.packet.Message.Type.chat);
		send.setTime(getTimestampStr());
		return send;
	}
	public Item toItem(boolean isLocal) {
		Item item = new Item(isLocal ? mTo : mFrom, null);
		item.setTimestamp(getTimestampStr());
		item.setMessage(mBody);
		item.setMsgState(msgState);
		if (mType == MSG_TYPE_CHAT) {
			item.setMsgtype(MsgType.chat);
			if (item.getMessage()
					.startsWith(Constants.MESSAGE_IMAGE_LINK_START)) {
				item.setMsgTypeSub(MsgTypeSub.image);
			} else if (item.getMessage().startsWith(
					Constants.MESSAGE_AUDIO_LINK_START)) {
				item.setMsgTypeSub(MsgTypeSub.audio);
			}
		} else {
			throw new IllegalArgumentException("message type is wrong,type"
					+ mType);
		}
		return item;
	}
	public Comment getComment() {
		return comment;
	}
	public MessageState getMsgState() {
		return msgState;
	}
	public void setMsgState(MessageState msgState) {
		this.msgState = msgState;
	}
	public MsgTypeSub getSubType() {
		return subType;
	}
	public void setSubType(MsgTypeSub subType) {
		this.subType = subType;
	}
}
