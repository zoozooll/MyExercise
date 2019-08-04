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

import java.io.ObjectOutputStream.PutField;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.beem.project.btf.R;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.Status;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * This class contains informations on a jabber contact.
 * @author darisk
 */
/**
 * @ClassName: Contact
 * @Description:
 * @author: yuedong bao
 * @date: 2015-8-27 下午4:15:49
 */
@DatabaseTable
public class Contact extends BaseDB implements Parcelable, Cloneable {
	@DatabaseField(id = true)
	private String jid;
	@DatabaseField
	private String password;
	@DatabaseField
	private String email;
	@DatabaseField
	private String alias;
	@DatabaseField(defaultValue = "")
	private String logintime = "";
	@DatabaseField
	private String onlinetime;
	@DatabaseField
	private String photo_big;
	@DatabaseField
	private String photo_small;
	@DatabaseField
	private String schoolId;
	@DatabaseField
	private String cityId;
	@DatabaseField
	private String major;
	@DatabaseField(defaultValue = "")
	private String enroltime = "";
	@DatabaseField
	private String hobby;
	@DatabaseField
	private String nickName;
	@DatabaseField(defaultValue = "0")
	private String sex = "0";
	@DatabaseField(defaultValue = "0000-00-00")
	private String bday = "0000-00-00";
	@DatabaseField
	private double lon;
	@DatabaseField
	private double lat;
	@DatabaseField
	private String signature;
	@DatabaseField
	private String phoneNum;
	@DatabaseField
	private String name;
	@DatabaseField
	private long birthTime;
	@DatabaseField
	private String distance;
	// 以下字段未用
	@DatabaseField
	private int mStatus;
	@DatabaseField
	private String mSelectedRes;
	@DatabaseField
	private String mMsgState;
	@DatabaseField
	private String mAvatarId;
	@DatabaseField
	private String region;
	@DatabaseField
	private String locality;
	private final List<String> mRes = new ArrayList<String>();
	private final List<String> mGroups = new ArrayList<String>();
	/** Parcelable.Creator needs by Android. */
	public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
		@Override
		public Contact createFromParcel(Parcel source) {
			return new Contact(source);
		}
		@Override
		public Contact[] newArray(int size) {
			return new Contact[size];
		}
	};

	public Contact() {
	}
	public Contact(String jid, String nickName) {
		this.jid = jid;
		this.nickName = nickName;
	}
	/**
	 * Construct a contact from a parcel.
	 * @param in parcel to use for construction
	 */
	private Contact(final Parcel in) {
		readFromParcel(in);
	}
	/**
	 * Constructor.
	 * @param jid JID of the contact
	 */
	public Contact(final String jid) {
		this.jid = "".equals(StringUtils.parseBareAddress(jid)) ? jid
				: StringUtils.parseBareAddress(jid);
		nickName = jid;
		mStatus = Status.CONTACT_STATUS_DISCONNECT;
		mMsgState = null;
		String res = jid;// StringUtils.parseResource(jid);
		mSelectedRes = res;
		if (!"".equals(res))
			mRes.add(res);
	}
	/**
	 * Create a contact from a Uri.
	 * @param uri an uri for the contact
	 * @throws IllegalArgumentException if it is not a xmpp uri
	 */
	public Contact(final Uri uri) {
		if (!"xmpp".equals(uri.getScheme()))
			throw new IllegalArgumentException();
		String enduri = uri.getEncodedSchemeSpecificPart();
		jid = StringUtils.parseBareAddress(enduri);
		nickName = jid;
		mStatus = Status.CONTACT_STATUS_DISCONNECT;
		mMsgState = null;
		String res = StringUtils.parseResource(enduri);
		mSelectedRes = res;
		mRes.add(res);
	}
	/**
	 * Make an xmpp uri for a spcific jid.
	 * @param jid the jid to represent as an uri
	 * @return an uri representing this jid.
	 */
	public static Uri makeXmppUri(String jid) {
		StringBuilder build = new StringBuilder("xmpp:");
		String name = StringUtils.parseName(jid);
		build.append(name);
		Uri u = Uri.parse(build.toString());
		return u;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jid);
		dest.writeString(email);
		dest.writeString(alias);
		dest.writeString(logintime);
		dest.writeInt(mStatus);
		dest.writeString(mSelectedRes);
		dest.writeString(mMsgState);
		dest.writeString(mAvatarId);
		dest.writeString(distance);
		dest.writeString(photo_big);
		dest.writeString(photo_small);
		dest.writeString(signature);
		dest.writeString(region);
		dest.writeString(locality);
		dest.writeString(schoolId);
		dest.writeString(cityId);
		dest.writeString(major);
		dest.writeString(enroltime);
		dest.writeString(hobby);
		dest.writeString(nickName);
		dest.writeString(sex);
		dest.writeString(bday);
		dest.writeDouble(lon);
		dest.writeDouble(lat);
		dest.writeString(password);
		dest.writeString(phoneNum);
		dest.writeStringList(getMRes());
		dest.writeStringList(getGroups());
	}
	public void readFromParcel(Parcel dest) {
		jid = dest.readString();
		email = dest.readString();
		alias = dest.readString();
		logintime = dest.readString();
		mStatus = dest.readInt();
		mSelectedRes = dest.readString();
		mMsgState = dest.readString();
		mAvatarId = dest.readString();
		distance = dest.readString();
		photo_big = dest.readString();
		photo_small = dest.readString();
		signature = dest.readString();
		region = dest.readString();
		locality = dest.readString();
		schoolId = dest.readString();
		cityId = dest.readString();
		major = dest.readString();
		enroltime = dest.readString();
		hobby = dest.readString();
		nickName = dest.readString();
		sex = dest.readString();
		bday = dest.readString();
		lon = dest.readDouble();
		lat = dest.readDouble();
		password = dest.readString();
		phoneNum = dest.readString();
		dest.readStringList(mRes);
		dest.readStringList(mGroups);
	}
	/**
	 * Add a resource for this contact.
	 * @param res the resource to add
	 */
	public void addRes(String res) {
		if (!mRes.contains(res))
			mRes.add(res);
	}
	/**
	 * Delete a resource for this contact.
	 * @param res the resource de delete
	 */
	public void delRes(String res) {
		mRes.remove(res);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	/**
	 * Get the groups the contact is in.
	 * @return the mGroups
	 */
	public List<String> getGroups() {
		return mGroups;
	}
	/**
	 * Get the Jabber ID of the contact.
	 * @return the Jabber ID
	 */
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		setField("jid", jid);
		this.jid = jid;
	}
	/**
	 * Get selected resource.
	 * @return the selected resource.
	 */
	public String getSelectedRes() {
		return mSelectedRes;
	}
	/**
	 * Get the list of resource for the contact.
	 * @return the mRes
	 */
	public List<String> getMRes() {
		return mRes;
	}
	/**
	 * Get the message status of the contact.
	 * @return the message status of the contact.
	 */
	public String getMsgState() {
		return mMsgState;
	}
	/**
	 * Get the status of the contact.
	 * @return the mStatus
	 */
	public int getStatus() {
		return mStatus;
	}
	/**
	 * Get the avatar id of the contact.
	 * @return the avatar id or null if there is not
	 */
	public String getAvatarId() {
		return mAvatarId;
	}
	/**
	 * Set the groups the contact is in.
	 * @param groups list of groups
	 */
	public void setGroups(Collection<RosterGroup> groups) {
		this.mGroups.clear();
		for (RosterGroup rosterGroup : groups) {
			mGroups.add(rosterGroup.getName());
		}
	}
	/**
	 * Set the groups the contact is in.
	 * @param groups the mGroups to set
	 */
	public void setGroups(List<String> groups) {
		mGroups.clear();
		mGroups.addAll(groups);
	}
	/**
	 * set the id of te contact on the phone contact list.
	 * @param mid the mID to set
	 */
	/**
	 * Set the avatar id of the contact.
	 * @param avatarId the avatar id
	 */
	public void setAvatarId(String avatarId) {
		setField("mAvatarId", avatarId);
		mAvatarId = avatarId;
	}
	/**
	 * Set the resource of the contact.
	 * @param resource to set.
	 */
	public void setSelectedRes(String resource) {
		setField("mSelectedRes", resource);
		mSelectedRes = resource;
	}
	/**
	 * Set the message status of the contact.
	 * @param msgState the message status of the contact to set
	 */
	public void setMsgState(String msgState) {
		setField("mMsgState", msgState);
		mMsgState = msgState;
	}
	/**
	 * Set the status of the contact.
	 * @param status the mStatus to set
	 */
	public void setStatus(int status) {
		setField("mStatus", status);
		mStatus = status;
	}
	/**
	 * Set the status of the contact using a presence packet.
	 * @param presence the presence containing status
	 */
	public void setStatus(Presence presence) {
		mStatus = Status.getStatusFromPresence(presence);
		mMsgState = presence.getStatus();
	}
	/**
	 * Set status for the contact.
	 * @param presence The presence packet which contains the status
	 */
	public void setStatus(PresenceAdapter presence) {
		mStatus = presence.getStatus();
		mMsgState = presence.getStatusText();
	}
	/**
	 * Get a URI to access the contact.
	 * @return the URI
	 */
	public Uri toUri() {
		return makeXmppUri(jid);
	}
	/**
	 * Get a URI to access the specific contact on this resource.
	 * @param resource the resource of the contact
	 * @return the URI
	 */
	public Uri toUri(String resource) {
		StringBuilder build = new StringBuilder("xmpp:");
		String name = StringUtils.parseName(jid);
		build.append(name);
		if (!"".equals(name))
			build.append('@');
		build.append(StringUtils.parseServer(jid));
		if (!"".equals(resource)) {
			build.append('/');
			build.append(resource);
		}
		Uri u = Uri.parse(build.toString());
		return u;
	}
	public String getJIDCompleted() {
		return VVXMPPUtils.makeJidCompleted(jid);
	}
	public String getJIDParsed() {
		return StringUtils.parseName(jid);
	}
	public String getBareJid() {
		return StringUtils.parseBareAddress(getJIDCompleted());
	}
	public String getDistance() {
		return distance;
	}
	public String getDistanceFormat() {
		double distance_double = Double.parseDouble(distance);
		if (distance_double < 1) {
			return (int) (distance_double * 1000) + "m";
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("0.00");
			return (decimalFormat.format(distance_double)) + "km";
		}
	}
	public void setDistance(String distance) {
		setField("distance", distance);
		this.distance = distance;
	}
	public String getPhoto() {
		String[] photoSplites = BBSUtils.splitPhotos(DBKey.photo_small,
				photo_small);
		if (photoSplites != null) {
			return photoSplites[0];
		}
		return "";
	}
	public String getBday() {
		return bday;
	}
	public void setBday(String bday) {
		setField("bday", bday);
		this.bday = bday;
	}
	public void setSignature(String field) {
		setField("signature", field);
		this.signature = field;
	}
	public String getSignature() {
		return signature;
	}
	public String getAge() {
		return BBSUtils.getAgeByBithday(bday);
	}
	public void setRegion(String addressFieldWork) {
		setField("region", region);
		this.region = addressFieldWork;
	}
	public void setLocality(String addressFieldWork) {
		this.locality = addressFieldWork;
		setField("locality", locality);
	}
	public void setSchoolId(String schoolname) {
		this.schoolId = schoolname;
		setField("schoolId", schoolId);
	}
	public void setHobby(String field) {
		this.hobby = field;
		setField("hobby", hobby);
	}
	public String getRegion() {
		return region;
	}
	public String getLocality() {
		return locality;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public String getMajor() {
		return major;
	}
	public String getHobby() {
		return hobby;
	}
	// 获取合适的名称:从备注名-昵称-jid依次取
	public String getSuitableName() {
		if (!TextUtils.isEmpty(alias)) {
			return alias;
		}
		if (!TextUtils.isEmpty(nickName)) {
			return nickName;
		}
		if (!TextUtils.isEmpty(jid)) {
			return getJIDParsed();
		}
		throw new IllegalArgumentException("should not be here!");
	}
	public String getSuitableNameMix() {
		if (!TextUtils.isEmpty(alias)) {
			return alias + "(" + nickName + ")";
		}
		if (!TextUtils.isEmpty(nickName)) {
			return nickName;
		}
		if (!TextUtils.isEmpty(jid)) {
			return getJIDParsed();
		}
		throw new IllegalArgumentException("should not be here!");
	}
	public String getSex() {
		return sex;
	}
	public int getSexInt() {
		if (TextUtils.isEmpty(sex)) {
			return 0;
		}
		return Integer.parseInt(sex);
	}
	public void setSex(String sex) {
		this.sex = sex;
		setField("sex", sex);
	}
	public int getmStatus() {
		return mStatus;
	}
	public void setmStatus(int mStatus) {
		this.mStatus = mStatus;
		setField("mStatus", mStatus);
	}
	public String getmSelectedRes() {
		return mSelectedRes;
	}
	public void setmSelectedRes(String mSelectedRes) {
		this.mSelectedRes = mSelectedRes;
		setField("mSelectedRes", mSelectedRes);
	}
	public List<String> getmRes() {
		return mRes;
	}
	public String getmAvatarId() {
		return mAvatarId;
	}
	public void setmAvatarId(String mAvatarId) {
		this.mAvatarId = mAvatarId;
		setField("mAvatarId", mAvatarId);
	}
	public static Parcelable.Creator<Contact> getCreator() {
		return CREATOR;
	}
	public List<String> getmGroups() {
		return mGroups;
	}
	public void setMajor(String major) {
		this.major = major;
		setField("major", major);
	}
	public void setEnroltime(String enroltime) {
		this.enroltime = enroltime;
		setField("enroltime", enroltime);
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
		setField("lon", lon);
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
		setField("lat", lat);
	}
	// 获取相对登录时间
	public String getLoginTimeRlt() {
		return BBSUtils.getTimeDurationString(logintime);
	}
	public void setLogintime(String loginTime) {
		this.logintime = loginTime;
		setField("logintime", logintime);
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
		setField("alias", alias);
	}
	@Override
	public String toString() {
		return "Contact [jid=" + jid + ", password=" + password + ", email="
				+ email + ", alias=" + alias + ", logintime=" + logintime
				+ ", onlinetime=" + onlinetime + ", photo_big=" + photo_big
				+ ", photo_small=" + photo_small + ", schoolId=" + schoolId
				+ ", cityId=" + cityId + ", major=" + major + ", enroltime="
				+ enroltime + ", hobby=" + hobby + ", nickName=" + nickName
				+ ", sex=" + sex + ", bday=" + bday + ", lon=" + lon + ", lat="
				+ lat + ", signature=" + signature + ", phoneNum=" + phoneNum
				+ ", name=" + name + ", mStatus=" + mStatus + ", mSelectedRes="
				+ mSelectedRes + ", mMsgState=" + mMsgState + ", mAvatarId="
				+ mAvatarId + ", distance=" + distance + ", region=" + region
				+ ", locality=" + locality + ", mRes=" + mRes + ", mGroups="
				+ mGroups + "]";
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String city) {
		this.cityId = city;
		setField("cityId", cityId);
	}
	public String getPhoto_big() {
		return photo_big;
	}
	public String getPhoto_small() {
		return photo_small;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		setField("email", email);
	}
	@Override
	public void saveToDatabase() {
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
	}
	@Override
	public void saveToDatabaseAsync() {
		super.saveToDatabaseAsync();
	}
	public boolean checkData() {
		boolean retVal = true;
		if (fields.containsKey(DBKey.photo_big)
				&& fields.containsKey(DBKey.photo_small)) {
			boolean big = TextUtils.isEmpty(photo_big);
			boolean small = TextUtils.isEmpty(photo_small);
			boolean bigAndsmall = big && small;
			boolean bigOrSmall = big || small;
			if (bigAndsmall != bigOrSmall) {
				retVal = false;
			}
			if (!TextUtils.isEmpty(photo_big)
					&& !TextUtils.isEmpty(photo_small)) {
				String[] strs_big = BBSUtils.splitPhotos(DBKey.photo_big,
						photo_big);
				String[] strs_small = BBSUtils.splitPhotos(DBKey.photo_small,
						photo_small);
				if (strs_big.length != strs_small.length) {
					retVal = false;
				}
			}
		}
		return retVal;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void saveData(Map<UserInfoKey, String> modifyMap) {
		for (UserInfoKey infokey : modifyMap.keySet()) {
			switch (infokey) {
				case bday:
					setField(DBKey.bday, modifyMap.get(infokey));
					break;
				case city:
					setField(DBKey.cityId, modifyMap.get(infokey));
					break;
				case school:
					setField(DBKey.schoolId, modifyMap.get(infokey));
					break;
				case sex:
					setField(DBKey.sex, modifyMap.get(infokey));
					break;
				case big:
					setField(DBKey.photo_big, modifyMap.get(infokey));
					break;
				case small:
					setField(DBKey.photo_small, modifyMap.get(infokey));
					break;
				case email:
					setField(DBKey.email, modifyMap.get(infokey));
					break;
				case enroltime:
					setField(DBKey.enroltime, modifyMap.get(infokey));
					break;
				case hobby:
					setField(DBKey.hobby, modifyMap.get(infokey));
					break;
				case jid:
					setField(DBKey.jid, modifyMap.get(infokey));
					break;
				case lat:
					setField(DBKey.lat,
							Double.parseDouble(modifyMap.get(infokey)));
					break;
				case lon:
					setField(DBKey.lon,
							Double.parseDouble(modifyMap.get(infokey)));
					break;
				case major:
					setField(DBKey.major, modifyMap.get(infokey));
					break;
				case nickname:
					setField(DBKey.nickName, modifyMap.get(infokey));
					break;
				case phonenum:
					setField(DBKey.phoneNum, modifyMap.get(infokey));
					break;
				case signature:
					setField(DBKey.signature, modifyMap.get(infokey));
					break;
				default:
					throw new IllegalAccessError(
							"modifyMap has field not save,infokey" + infokey);
			}
		}
	}
	public static List<Contact> queryAll() {
		return DBHelper.getInstance().queryAll(Contact.class);
	}
	public static Contact queryForFirst(String jid) {
		return DBHelper.getInstance().queryForFirst(Contact.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
	}
	public static Contact queryByPhonenum(String phonenum) {
		return DBHelper.getInstance().queryForFirst(Contact.class,
				new DBWhere(DBKey.phoneNum, DBWhereType.eq, phonenum));
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
		ImageLoader.getInstance().displayImage(getPhoto(), imageView,
				options[getSexInt()]);
	}
	public void displayRoundPhoto(ImageView imageView) {
		Bitmap bitmap = BitmapFactory.decodeResource(imageView.getResources(),
				R.drawable.default_headw);
		Drawable dw = new RoundedBitmapDisplayer.RoundedDrawable(bitmap, 1000,
				0);
		Bitmap bitmap2 = BitmapFactory.decodeResource(imageView.getResources(),
				R.drawable.default_head);
		Drawable d = new RoundedBitmapDisplayer.RoundedDrawable(bitmap2, 1000,
				0);
		DisplayImageOptions[] options = new DisplayImageOptions[] {
				new DisplayImageOptions.Builder().showImageOnLoading(dw)
						.showImageForEmptyUri(dw).cacheInMemory(true)
						.cacheOnDisk(true)
						.displayer(new RoundedBitmapDisplayer(1000)).build(),
				new DisplayImageOptions.Builder().showImageOnLoading(d)
						.showImageForEmptyUri(d).cacheInMemory(true)
						.cacheOnDisk(true)
						.displayer(new RoundedBitmapDisplayer(1000)).build() };
		ImageLoader.getInstance().displayImage(getPhoto(), imageView,
				options[getSexInt()]);
	}
	public void displayPhoto(ImageView imageView, ImageLoadingListener lis) {
		ImageLoader.getInstance().cancelDisplayTask(imageView);
		DisplayImageOptions[] options = new DisplayImageOptions[] {
				new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.default_headw_selector)
						.showImageForEmptyUri(R.drawable.default_headw_selector)
						.cacheInMemory(true).cacheOnDisk(true).build(),
				new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.default_head_selector)
						.showImageForEmptyUri(R.drawable.default_head_selector)
						.cacheInMemory(true).cacheOnDisk(true).build() };
		ImageLoader.getInstance().displayImage(getPhoto(), imageView,
				options[getSexInt()], lis);
	}
	public static Contact createNullContact() {
		Contact contact = new Contact();
		contact.setSex("1");
		return contact;
	}
	public String getOnlinetime() {
		return onlinetime;
	}
	public void setOnlinetime(String onlinetime) {
		this.onlinetime = onlinetime;
	}
	public String getNickName() {
		return BBSUtils.replaceBlank(nickName);
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getmMsgState() {
		return mMsgState;
	}
	public void setmMsgState(String mMsgState) {
		this.mMsgState = mMsgState;
	}
	public String getLogintime() {
		return logintime;
	}
	public String getEnroltime() {
		return enroltime;
	}
	public void setPhoto_big(String photo_big) {
		this.photo_big = photo_big;
	}
	public void setPhoto_small(String photo_small) {
		this.photo_small = photo_small;
	}
}
