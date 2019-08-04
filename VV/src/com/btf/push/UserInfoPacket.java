package com.btf.push;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

import android.text.TextUtils;

import com.beem.project.btf.service.Contact;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;

/**
 * @func 个人信息Packet
 * @author yuedong bao
 * @time 2014-12-26 上午11:46:44
 */
public class UserInfoPacket extends IQ {
	private Map<UserInfoKey, String> fields = new Hashtable<UserInfoKey, String>();
	private static UserInfoKey[] embedKeys = new UserInfoKey[] {
			UserInfoKey.lat, UserInfoKey.lon, UserInfoKey.small,
			UserInfoKey.big, UserInfoKey.school, UserInfoKey.enroltime,
			UserInfoKey.major };

	public UserInfoPacket(String user) {
		this.setTo(user);
	}
	public UserInfoPacket() {
	}

	public enum UserInfoKey {
		jid, small, big, bday, sex, constellation, lat, lon, logintime,
		onlinetime, nickname, email, signature, city, school, major, enroltime,
		hobby, phonenum;
		private enum EmbededKey {
			portrait, edu, geo;
		}
	}

	public Map<UserInfoKey, String> cloneFieldMaps() {
		Map<UserInfoKey, String> retVal = new Hashtable<UserInfoKey, String>();
		for (UserInfoKey key : fields.keySet()) {
			retVal.put(key, fields.get(key));
		}
		return retVal;
	}
	public void setField(UserInfoKey key, String Val) {
		fields.put(key, Val);
	}
	public void setField(String key, String Val) {
		setField(UserInfoKey.valueOf(key), Val);
	}
	public String getField(UserInfoKey key) {
		return fields.get(key);
	}
	public void clearFileds() {
		fields.clear();
	}
	public boolean isEmpty() {
		return fields.isEmpty();
	}
	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		new VCardWriter(buf).write();
		return buf.toString();
	}

	public static class Item {
		public String jid;
		public String bday;
		public String sex;
		public String nickName;
		public String city;
		public String locality;
		public String street;
		public String curpos;
		public String school;
		public String time;
		public String spec;
		public String lat;
		public String lon;
		public String note;
		public String desc;
		public String binval;
		public String onlineTime;

		public Item(String user) {
			this.jid = user;
			nickName = "";
			bday = "";
			city = "";
			locality = "";
			street = "";
			curpos = "";
			school = "";
			time = "";
			spec = "";
			lat = "";
			lon = "";
			sex = "";
			note = "";
			desc = "";
			binval = "";
			onlineTime = "";
		}
		@Override
		public String toString() {
			return "Item [jid=" + jid + ", bday=" + bday + ", sex=" + sex
					+ ", nickName=" + nickName + ", city=" + city
					+ ", locality=" + locality + ", street=" + street
					+ ", curpos=" + curpos + ", school=" + school + ", time="
					+ time + ", spec=" + spec + ", lat=" + lat + ", lon=" + lon
					+ ", note=" + note + ", desc=" + desc + ", binval="
					+ binval + ", onlineTime=" + onlineTime + "]";
		}
	}

	private class VCardWriter {
		private final StringBuilder sb;

		VCardWriter(StringBuilder sb) {
			this.sb = sb;
		}
		public void write() {
			boolean b = hasContent();
			appendTag("vCard", "xmlns", "info", b, new ContentBuilder() {
				@Override
				public void addTagContent() {
					buildActualContent();
				}
			});
		}
		private boolean hasContent() {
			// noinspection OverlyComplexBooleanExpression
			return hasGeoFields() || hasSchoolFields() || hasPhotoFields()
					|| fields != null;
		}
		private void buildActualContent() {
			if (fields != null) {
				appendGeo();
				appendPhoto();
				appendSchool();
				appendGenericFields();
			}
		}
		private void appendGenericFields() {
			Map<String, String> otherSimpleFields = new HashMap<String, String>();
			List<UserInfoKey> appendKeys = new ArrayList<UserInfoPacket.UserInfoKey>(
					Arrays.asList(UserInfoKey.values()));
			for (UserInfoKey removeK : embedKeys) {
				appendKeys.remove(removeK);
			}
			for (UserInfoKey appendKey : appendKeys) {
				if (getField(appendKey) != null) {
					otherSimpleFields.put(appendKey.toString(),
							getField(appendKey));
				}
			}
			Iterator<Map.Entry<String, String>> it = otherSimpleFields
					.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				appendTag(entry.getKey().toString(),
						StringUtils.escapeForXML(entry.getValue()));
			}
		}
		private boolean hasGeoFields() {
			return getField(UserInfoKey.lat) != null
					|| getField(UserInfoKey.lon) != null;
		}
		private boolean hasSchoolFields() {
			return getField(UserInfoKey.school) != null
					|| getField(UserInfoKey.enroltime) != null
					|| getField(UserInfoKey.major) != null;
		}
		private boolean hasPhotoFields() {
			return getField(UserInfoKey.small) != null
					|| getField(UserInfoKey.big) != null;
		}
		private void appendGeo() {
			if (hasGeoFields()) {
				appendTag(UserInfoKey.EmbededKey.geo.toString(), true,
						new ContentBuilder() {
							@Override
							public void addTagContent() {
								appendTag(
										UserInfoKey.lat.toString(),
										StringUtils
												.escapeForXML(getField(UserInfoKey.lat)));
								appendTag(
										UserInfoKey.lon.toString(),
										StringUtils
												.escapeForXML(getField(UserInfoKey.lon)));
							}
						});
			}
		}
		private void appendSchool() {
			if (hasSchoolFields()) {
				appendTag(UserInfoKey.EmbededKey.edu.toString(), true,
						new ContentBuilder() {
							@Override
							public void addTagContent() {
								UserInfoKey[] keys = new UserInfoKey[] {
										UserInfoKey.school,
										UserInfoKey.enroltime,
										UserInfoKey.major };
								for (UserInfoKey key : keys) {
									appendTag(key.toString(), StringUtils
											.escapeForXML(getField(key)));
								}
							}
						});
			}
		}
		private void appendPhoto() {
			if (hasPhotoFields()) {
				appendTag(UserInfoKey.EmbededKey.portrait.toString(), true,
						new ContentBuilder() {
							@Override
							public void addTagContent() {
								UserInfoKey[] keys = new UserInfoKey[] {
										UserInfoKey.small, UserInfoKey.big, };
								for (UserInfoKey key : keys) {
									appendTag(key.toString(), StringUtils
											.escapeForXML(getField(key)));
								}
							}
						});
			}
		}
		private void appendTag(String tag, String attr, String attrValue,
				boolean hasContent, ContentBuilder builder) {
			sb.append('<').append(tag);
			if (attr != null) {
				sb.append(' ').append(attr).append('=').append('\'')
						.append(attrValue).append('\'');
			}
			if (hasContent) {
				sb.append('>');
				builder.addTagContent();
				sb.append("</").append(tag).append(">");
			} else {
				sb.append("/> ");
			}
		}
		private void appendTag(String tag, boolean hasContent,
				ContentBuilder builder) {
			appendTag(tag, null, null, hasContent, builder);
		}
		private void appendTag(String tag, final String tagText) {
			if (tagText == null)
				return;
			final ContentBuilder contentBuilder = new ContentBuilder() {
				@Override
				public void addTagContent() {
					sb.append(tagText.trim());
				}
			};
			appendTag(tag, true, contentBuilder);
		}
	}

	// 将UserInfoPacket转换成Contact
	/**
	 * @Title: toContact
	 * @Description: TODO
	 * @return
	 * @return: Contact
	 */
	public Contact toContact() {
		Contact contact = new Contact();
		for (UserInfoKey keyOne : fields.keySet()) {
			switch (keyOne) {
				case jid:
					contact.setField(DBKey.jid, getField(UserInfoKey.jid));
					break;
				case nickname:
					contact.setField(DBKey.nickName,
							getField(UserInfoKey.nickname));
					break;
				case email:
					contact.setField(DBKey.email, getField(UserInfoKey.email));
					break;
				case sex:
					contact.setField(DBKey.sex, getField(UserInfoKey.sex));
					break;
				case bday:
					contact.setField(DBKey.bday,
							formateBday(getField(UserInfoKey.bday)));
					break;
				case signature:
					contact.setField(DBKey.signature,
							getField(UserInfoKey.signature));
					break;
				case hobby:
					contact.setField(DBKey.hobby, getField(UserInfoKey.hobby));
					break;
				case city:
					contact.setField(DBKey.cityId, getField(UserInfoKey.city));
					break;
				case school:
					contact.setField(DBKey.schoolId,
							getField(UserInfoKey.school));
					break;
				case major:
					contact.setField(DBKey.major, getField(UserInfoKey.major));
					break;
				case enroltime:
					contact.setField(DBKey.enroltime,
							getField(UserInfoKey.enroltime));
					break;
				case lat:
					contact.setField(DBKey.lat,
							Double.parseDouble(getField(UserInfoKey.lat)));
					break;
				case lon:
					contact.setField(DBKey.lon,
							Double.parseDouble(getField(UserInfoKey.lon)));
					break;
				case phonenum:
					contact.setField(DBKey.phoneNum,
							getField(UserInfoKey.phonenum));
					break;
				case logintime:
					contact.setField(DBKey.logintime,
							getField(UserInfoKey.logintime));
					break;
				case onlinetime:
					contact.setField(DBKey.onlinetime,
							getField(UserInfoKey.onlinetime));
					break;
				case small:
					contact.setField(DBKey.photo_small,
							getField(UserInfoKey.small));
					break;
				case big:
					contact.setField(DBKey.photo_big, getField(UserInfoKey.big));
					break;
				default:
					break;
			}
		}
		return contact;
	}
	private String formateBday(String in) {
		String retVal = "";
		if (TextUtils.isEmpty(in))
			return retVal;
		if (in.length() == 10) {
			return in;
		} else {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.US);
			SimpleDateFormat sfOut = new SimpleDateFormat("yyyy-MM-dd",
					Locale.US);
			try {
				retVal = sfOut.format(sf.parse(in));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return retVal;
	}

	private interface ContentBuilder {
		void addTagContent();
	}
}
