package com.btf.push;

import org.jivesoftware.smack.packet.Message.Comment;
import org.jivesoftware.smack.util.StringUtils;

import com.beem.project.btf.R;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.ChatActivity.MessageState;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

/**
 * A roster item, which consists of a JID, their name, the type of subscription, and the groups the
 * roster item belongs to.
 */
public class Item implements Parcelable {
	private String jid;
	private String nickname;
	private String phoneName;
	private String phoneNum;
	private String distance;
	private String photo;
	private String loginTime;
	private String bday;
	private String sex;
	private String signature;
	private String binval;
	private String mMessage;
	private boolean mIsError;
	private String timeStamp;
	private String alias;
	private double lon;
	private double lat;
	private String gidJid;
	private String gid;
	private String gidCreateTime;
	private String cid;
	private String subject;
	private int msgtype;
	private int msgTypeSub;
	private int unReadMsgCount = 1;
	private boolean isLocal;
	private MessageState msgState = MessageState.preloading;
	// 以下不需要序列化
	private boolean isChecked;
	private int delViewVisibility = View.GONE;
	private ImageFolderItem folderItem;
	private Comment comment;

	public enum MsgType {
		chat, friend_require, comment, msg_city, msg_school, msg_phone,
		search_rst, timefly_notify, like;
	}

	public enum MsgTypeSub {
		zone, unadded, added, unregister, friend_refuse, friend_agree,
		friend_ask, html, systemPrompt, audio, image, plainText;
	}

	/**
	 * Creates a new roster item.
	 * @param jid the user.
	 * @param nickname the user's name.
	 */
	public Item(String jid, String nickname) {
		setJid(jid);
		this.nickname = nickname;
		this.distance = null;
	}
	public Item(Parcel in) {
		readFromParcel(in);
	}
	/**
	 * Returns the user.
	 * @return the user.
	 */
	public String getJid() {
		return jid;
	}
	public String getJidParsed() {
		return StringUtils.parseName(jid);
	}
	/**
	 * Returns the user's name.
	 * @return the user's name.
	 */
	public String getNickname() {
		return nickname;
	}
	public String toXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<item jid=\"").append(jid).append("\"");
		if (nickname != null) {
			buf.append(" nickname=\"").append(nickname).append("\"");
		}
		if (distance != null) {
			buf.append(" distance=\"").append(distance).append("\"");
		}
		if (photo != null) {
			buf.append(" photo=\"").append(photo).append("\"");
		}
		if (loginTime != null) {
			buf.append(" onlineTime=\"").append(loginTime).append("\"");
		}
		if (bday != null) {
			buf.append(" bday=\"").append(bday).append("\"");
		}
		if (sex != null) {
			buf.append(" role=\"").append(sex).append("\"");
		}
		if (signature != null) {
			buf.append(" note=\"").append(signature).append("\"");
		}
		if (binval != null) {
			buf.append(" photo=\"").append(binval).append("\"");
		}
		buf.append(">");
		buf.append("</item>");
		return buf.toString();
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getPhoto() {
		return photo;
	}
	public String getMessage() {
		return mMessage;
	}
	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}
	public boolean ismIsError() {
		return mIsError;
	}
	public void setmIsError(boolean mIsError) {
		this.mIsError = mIsError;
	}
	public String getTimestamp() {
		return timeStamp;
	}
	public void setTimestamp(String mTimestamp) {
		this.timeStamp = mTimestamp;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLogintime(String pLoginTime) {
		this.loginTime = pLoginTime;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBday() {
		return bday;
	}
	public void setBday(String bday) {
		this.bday = bday;
	}
	public String getSex() {
		return sex;
	}
	public int getSexInt() {
		return Integer.parseInt(sex);
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String pSignature) {
		this.signature = pSignature;
	}
	public String getBinval() {
		return binval;
	}
	public void setBinval(String binval) {
		this.binval = binval;
	}
	public Item.MsgType getMsgtype() {
		return MsgType.values()[msgtype];
	}
	public void setMsgtype(Item.MsgType msgtype) {
		this.msgtype = msgtype.ordinal();
	}
	public void setJid(String jid) {
		this.jid = VVXMPPUtils.makeJidParsed(jid);
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setPhoto(String headPhoto) {
		this.photo = headPhoto;
	}
	public String getPhoneName() {
		return phoneName;
	}
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Item.MsgTypeSub getMsgTypeSub() {
		return MsgTypeSub.values()[msgTypeSub];
	}
	public void setMsgTypeSub(Item.MsgTypeSub msgTypeSub) {
		this.msgTypeSub = msgTypeSub.ordinal();
	}
	public int getUnReadMsgCount() {
		return unReadMsgCount;
	}
	public void setUnReadMsgCount(int unReadMsgCount) {
		this.unReadMsgCount = unReadMsgCount;
	}
	public String getGidJid() {
		return gidJid;
	}
	public void setGidJid(String gidJid) {
		this.gidJid = gidJid;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getGidCreateTime() {
		return gidCreateTime;
	}
	public void setGidCreateTime(String gidCreateTime) {
		this.gidCreateTime = gidCreateTime;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jid);
		dest.writeString(nickname);
		dest.writeString(phoneName);
		dest.writeString(phoneNum);
		dest.writeString(distance);
		dest.writeString(photo);
		dest.writeString(loginTime);
		dest.writeString(bday);
		dest.writeString(sex);
		dest.writeString(signature);
		dest.writeString(binval);
		dest.writeString(mMessage);
		dest.writeInt(mIsError ? 0 : 1);
		dest.writeString(timeStamp);
		dest.writeString(alias);
		dest.writeDouble(lon);
		dest.writeDouble(lat);
		dest.writeString(gidJid);
		dest.writeString(gid);
		dest.writeString(gidCreateTime);
		dest.writeString(cid);
		dest.writeInt(msgtype);
		dest.writeInt(msgTypeSub);
		dest.writeInt(unReadMsgCount);
		dest.writeInt(isLocal ? 0 : 1);
		dest.writeSerializable(msgState);
		dest.writeString(subject);
	}
	protected void readFromParcel(Parcel dest) {
		jid = dest.readString();
		nickname = dest.readString();
		phoneName = dest.readString();
		phoneNum = dest.readString();
		distance = dest.readString();
		photo = dest.readString();
		loginTime = dest.readString();
		bday = dest.readString();
		sex = dest.readString();
		signature = dest.readString();
		binval = dest.readString();
		mMessage = dest.readString();
		mIsError = dest.readInt() == 0;
		timeStamp = dest.readString();
		alias = dest.readString();
		lon = dest.readDouble();
		lat = dest.readDouble();
		gidJid = dest.readString();
		gid = dest.readString();
		gidCreateTime = dest.readString();
		cid = dest.readString();
		msgtype = dest.readInt();
		msgTypeSub = dest.readInt();
		unReadMsgCount = dest.readInt();
		isLocal = dest.readInt() == 0;
		msgState = (MessageState) dest.readSerializable();
		subject = dest.readString();
	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}
		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

	public Contact toContact() {
		Contact c = new Contact();
		if (jid != null)
			c.setField(DBKey.jid, jid);
		if (nickname != null)
			c.setField(DBKey.nickName, nickname);
		if (phoneNum != null)
			c.setField(DBKey.phoneNum, phoneNum);
		if (photo != null)
			c.setField(DBKey.photo_small, photo);
		if (loginTime != null)
			c.setField(DBKey.logintime, loginTime);
		if (bday != null)
			c.setField(DBKey.bday, bday);
		if (sex != null)
			c.setField(DBKey.sex, sex);
		if (signature != null)
			c.setField(DBKey.signature, signature);
		if (lat != 0)
			c.setField(DBKey.lat, lat);
		if (lon != 0)
			c.setField(DBKey.lon, lon);
		if (alias != null)
			c.setField(DBKey.alias, alias);
		return c;
	}
	@Override
	public String toString() {
		return "Item [jid=" + jid + ", nickname=" + nickname + ", phoneName="
				+ phoneName + ", phoneNum=" + phoneNum + ", distance="
				+ distance + ", photo=" + photo + ", loginTime=" + loginTime
				+ ", bday=" + bday + ", sex=" + sex + ", signature="
				+ signature + ", binval=" + binval + ", mMessage=" + mMessage
				+ ", mIsError=" + mIsError + ", timeStamp=" + timeStamp
				+ ", alias=" + alias + ", lon=" + lon + ", lat=" + lat
				+ ", gid=" + gid + ", gidCreateTime=" + gidCreateTime
				+ ", cid=" + cid + ", msgtype=" + msgtype + ", msgTypeSub="
				+ msgTypeSub + ", unReadMsgCount=" + unReadMsgCount
				+ ", isLocal=" + isLocal + ", msgState=" + msgState
				+ ", isChecked=" + isChecked + ", delViewVisibility="
				+ delViewVisibility + "]";
	}
	public int getDelViewVisibility() {
		return delViewVisibility;
	}
	public void setDelViewVisibility(int delViewVisibility) {
		this.delViewVisibility = delViewVisibility;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public void displayPhoto(ImageView imageView) {
		DisplayImageOptions[] options = new DisplayImageOptions[] {
				new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.default_headw_selector)
						.showImageForEmptyUri(R.drawable.default_headw_selector)
						.cacheInMemory(true).cacheOnDisk(true).build(),
				new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.default_head_selector)
						.showImageForEmptyUri(R.drawable.default_head_selector)
						.cacheInMemory(true).cacheOnDisk(true).build() };
		ImageLoader.getInstance().displayImage(photo, imageView,
				options[getSexInt()]);
	}
	public void displayPhoto(ImageView imageView, ImageLoadingListener lis) {
		DisplayImageOptions[] options = new DisplayImageOptions[] {
				new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.default_headw_selector)
						.showImageForEmptyUri(R.drawable.default_headw_selector)
						.cacheInMemory(true).cacheOnDisk(true).build(),
				new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.default_head_selector)
						.showImageForEmptyUri(R.drawable.default_head_selector)
						.cacheInMemory(true).cacheOnDisk(true).build() };
		ImageLoader.getInstance().displayImage(photo, imageView,
				options[getSexInt()], lis);
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public MessageState getMsgState() {
		return msgState;
	}
	public void setMsgState(MessageState msgState) {
		this.msgState = msgState;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public ImageFolderItem getFolderItem() {
		return folderItem;
	}
	public void setFolderItem(ImageFolderItem folderItem) {
		this.folderItem = folderItem;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
}
