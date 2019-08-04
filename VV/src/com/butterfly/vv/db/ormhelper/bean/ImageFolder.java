package com.butterfly.vv.db.ormhelper.bean;

import java.io.Serializable;
import java.util.List;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult.AddressComponent;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.network.BDLocator.onReverseGeoCodeRstListener;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.field.DatabaseField;

public class ImageFolder extends BaseDB implements Serializable {
	private static final long serialVersionUID = -797799481736220831L;
	@DatabaseField(id = true)
	private String id;// 因为服务器的gid不唯一故用自动生成的id

	public String getId() {
		return id;
	}

	@DatabaseField
	private String jid;
	@DatabaseField
	private String createTime;
	@DatabaseField
	private String gid;
	@DatabaseField(defaultValue = "0")
	private String authority;
	@DatabaseField
	private String signature;
	@DatabaseField
	private int thumbupCount;
	@DatabaseField
	private double lon;
	@DatabaseField
	private double lat;
	@DatabaseField
	private int photoCount;
	@DatabaseField
	private int commentCount;
	@DatabaseField
	private String cityId;
	@DatabaseField
	private String notify_time;
	@DatabaseField
	private String notify_valid;
	@DatabaseField
	private String distance;
	@DatabaseField
	private String location;
	@DatabaseField
	private boolean isThumbup;
	@DatabaseField
	private long birthTime;
	@DatabaseField
	private String album_url;
	@DatabaseField
	private String topic;
	@DatabaseField
	private String grade;
	@DatabaseField
	private String imagefoldertype;

	public int getPhotoCount() {
		return photoCount;
	}
	public void setThumbupCount(int upNum) {
		this.thumbupCount = upNum;
	}
	public String getJid() {
		return jid;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public String getGid() {
		return gid;
	}
	public String getLocation() {
		return "";
	}
	public String getSignature() {
		return signature;
	}
	public int getThumbupCount() {
		return thumbupCount;
	}
	public void setJid(String authid) {
		this.jid = authid;
	}
	public void setCommentCount(int commentNum) {
		this.commentCount = commentNum;
	}
	public void setGid(String id) {
		this.gid = id;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setSignature(String signal) {
		this.signature = signal;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String folderAccess) {
		this.authority = folderAccess;
	}
	public String[] getCreateTimeYMD() {
		return createTime.split("-");
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getJidGidCreatTime() {
		return new StringBuffer().append(jid).append("_").append(gid)
				.append("_").append(createTime).toString();
	}
	public void setCreateTime(String create_time) {
		this.createTime = create_time;
	}
	public String getNotify_valid() {
		return notify_valid;
	}
	public void setNotify_valid(String notify_valid) {
		this.notify_valid = notify_valid;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
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
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public long getBirthTime() {
		return birthTime;
	}
	public void setBirthTime(long birthTime) {
		this.birthTime = birthTime;
	}
	public String getAlbum_url() {
		return album_url;
	}
	public void setAlbum_url(String album_url) {
		this.album_url = album_url;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getType() {
		return imagefoldertype;
	}
	public void setType(String type) {
		this.imagefoldertype = type;
	}
	@Override
	public String toString() {
		return "ImageFolder [id=" + id + ", jid=" + jid + ", createTime="
				+ getMyCreatTime(createTime) + ", gid=" + gid + ", authority=" + authority
				+ ", signature=" + signature + ", thumbupCount=" + thumbupCount
				+ ", lon=" + lon + ", lat=" + lat + ", photoCount="
				+ photoCount + ", commentCount=" + commentCount + ", cityId="
				+ cityId + ", notify_time=" + notify_time + ", notify_valid="
				+ notify_valid + ", distance=" + distance + ", location="
				+ location + ", isThumbup=" + isThumbup + ", birthTime="
				+ birthTime + ", album_url=" + album_url + ", topic=" + topic
				+ ", grade=" + grade + ", type=" + imagefoldertype + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createTime == null) ? 0 : getMyCreatTime(createTime).hashCode());
		result = prime * result + ((gid == null) ? 0 : gid.hashCode());
		result = prime * result + ((jid == null) ? 0 : jid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageFolder other = (ImageFolder) obj;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!getMyCreatTime(createTime).equals(getMyCreatTime(other.createTime)))
			return false;
		if (gid == null) {
			if (other.gid != null)
				return false;
		} else if (!gid.equals(other.gid))
			return false;
		if (jid == null) {
			if (other.jid != null)
				return false;
		} else if (!jid.equals(other.jid))
			return false;
		return true;
	}
	@Override
	public void saveToDatabase() {
		String gid = (String) getField(DBKey.gid);
		String createTime = (String) getField(DBKey.createTime);
		String jid = (String) getField(DBKey.jid);
		id = createID(jid, gid, createTime);
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static List<ImageFolder> queryAll(String jid) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		List<ImageFolder> retVal = null;
		retVal = DBHelper.getInstance().queryAll(ImageFolder.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
		return retVal;
	}
	public static List<ImageFolder> queryAll() {
		List<ImageFolder> retVal = null;
		retVal = DBHelper.getInstance().queryAll(ImageFolder.class);
		return retVal;
	}
	public static List<ImageFolder> queryThumbUpAll() {
		List<ImageFolder> retVal = null;
		retVal = DBHelper.getInstance().queryAll(ImageFolder.class,
				new DBWhere(DBKey.isThumbup, DBWhereType.eq, true));
		return retVal;
	}
	public static ImageFolder queryForFirst(String jid, String gid,
			String createTime) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		ImageFolder retVal = DBHelper.getInstance().queryForFirst(
				ImageFolder.class, new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.createTime, DBWhereType.eq, createTime),
				new DBWhere(DBKey.gid, DBWhereType.eq, gid));
		return retVal;
	}
	// 删除某一个具体的图片组
	public static void delete(String jid, String gid, String createTime) {
		String id = createID(jid, gid, createTime);
		DBHelper.getInstance().delete(ImageFolder.class,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	// 删除属于某个人的图片组
	public static void delete(String jid) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		DBHelper.getInstance().delete(ImageFolder.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid));
	}
	// 删除某人从monthStart到monthEnd之间的图片组
	public static int deleteBetween(String jid, String monthStart,
			String monthEnd) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		return DBHelper.getInstance().delete(
				ImageFolder.class,
				new DBWhere(DBKey.createTime, DBWhereType.between, monthStart,
						monthEnd), new DBWhere(DBKey.jid, DBWhereType.eq, jid));
	}
	public static ImageFolder queryForFirstLike(String jid,
			String createTimeLike) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		ImageFolder retVal = DBHelper.getInstance().queryForFirst(
				ImageFolder.class,
				new DBWhere(DBKey.jid, DBWhereType.eq, jid),
				new DBWhere(DBKey.createTime, DBWhereType.like, createTimeLike
						+ "%"));
		return retVal;
	}
	public String getDistanceFormat() {
		double distance_double = Double.parseDouble(distance);
		if (distance_double < 1) {
			return (int) (distance_double * 1000) + "m";
		} else {
			return (int) (distance_double * 1) + "km";
		}
	}
	/**
	 * @Title: showLocation
	 * @Description: 显示地理位置
	 * @param: @param imageView
	 * @return: void
	 * @throws:
	 */
	public void showLocation(onLocationLis lis) {
		if (TextUtils.isEmpty(location)) {
			if (TextUtils.isEmpty(jid) || TextUtils.isEmpty(gid)
					|| TextUtils.isEmpty(createTime)) {
				// 本地正在上传的图片没有gid
				//LogUtils.i("showLocation_empty_data:" + getJidGidCreatTime());
				return;
			}
			new AsyncTask<Object, Integer, String>() {
				onLocationLis lis;

				@Override
				protected String doInBackground(Object... params) {
					lis = (onLocationLis) params[0];
					String result = null;
					// 先查看数据库中有没有location
					ImageFolder queryImageFolder = ImageFolder.queryForFirst(
							jid, gid, createTime);
					if (queryImageFolder != null
							&& queryImageFolder.getLat() == lat
							&& queryImageFolder.getLon() == lon) {
						// 数据库中的经纬度没有改变时，location才有效
						result = queryImageFolder.location;
					}
					return result;
				}
				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					location = result;
					if (TextUtils.isEmpty(result)) {
						if (BeemApplication.isNetworkOk()) {
							BDLocator.getInstance().reverseGeoCode(lat, lon,
									new onReverseGeoCodeRstListener() {
										@Override
										public void onGetReverseGeoCodeResult(
												ReverseGeoCodeResult arg0) {
											AddressComponent component = arg0
													.getAddressDetail();
											if (component.province
													.equals(component.city)) {
												//直辖市的省和市相同，去重
												location = new StringBuffer()
														.append(component.province)
														.append(component.district)
														.toString();
											} else {
												location = new StringBuffer()
														.append(component.province)
														.append(component.city)
														.append(component.district)
														.toString();
											}
											lis.onResult(location);
											// 保存到数据库
											ImageFolder dbEditor = new ImageFolder();
											dbEditor.setField(DBKey.jid, jid);
											dbEditor.setField(DBKey.gid, gid);
											dbEditor.setField(DBKey.createTime,
													createTime);
											dbEditor.setField(DBKey.location,
													location);
											dbEditor.saveToDatabaseAsync();
										}
									});
						} else {
							//LogUtils.i("Not connect the network,location is " + location);
						}
					} else {
						lis.onResult(result);
					}
				}
			}.execute(lis);
		} else {
			lis.onResult(location);
		}
	}

	public interface onLocationLis {
		void onResult(String location);
	}

	// 判断是否点赞
	public boolean isThumbup() {
		return isThumbup;
	}
	// 设置点赞
	public void setThumbup(boolean pThumbup) {
		this.isThumbup = pThumbup;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDistance() {
		return distance;
	}
	
	private static String getMyCreatTime(String in) {
		String out;
		if (in.length() > 10) {
			out = in.substring(0, 10);
		} else {
			out = in;
		}
		return out;
	}
}
