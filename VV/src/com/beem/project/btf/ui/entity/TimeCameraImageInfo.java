package com.beem.project.btf.ui.entity;

import java.util.List;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 图片素材信息实体
 */
@DatabaseTable
public class TimeCameraImageInfo extends BaseDB implements CommonCameraInfo {
	private static final String tag = TimeCameraImageInfo.class.getSimpleName();
	@DatabaseField(id = true)
	private String id;// 唯一标示
	@DatabaseField
	private String groupid;// 表示在哪一年代
	@DatabaseField
	private String laypath1;// 图层一路径
	@DatabaseField
	private String laypath2;// 图层二路径
	@DatabaseField
	private String laypath3;// 图层三路径
	@DatabaseField
	private String pathThumb;// 小缩略图路径
	@DatabaseField
	private String pathThumbLarge;// 大缩略图路径
	@DatabaseField(defaultValue = "false")
	private boolean isDownloaded;// 标示是否下载过了
	@DatabaseField
	private boolean isLocal;// 标示是否本地图片
	@DatabaseField
	private String posepath;// 素材中包含的人物动作路径
	@DatabaseField
	private String notetext;// 素材中的标语
	@DatabaseField
	private String laycount;// 图层数量
	@DatabaseField(defaultValue = "0,0")
	private String textposition;// 文本位置
	@DatabaseField(defaultValue = "20")
	private String textsize;// 文字大小
	@DatabaseField
	private String mapping;// 直方图信息
	@DatabaseField
	private String textcolor;//文字颜色
	@DatabaseField
	private String bitmapposition;//图片相对位置和相对大小
	@DatabaseField
	private long birthTime;

	public TimeCameraImageInfo() {
	}
	public String getLaypath1() {
		return laypath1;
	}
	public void setLaypath1(String laypath1) {
		this.laypath1 = laypath1;
	}
	public String getLaypath2() {
		return laypath2;
	}
	public void setLaypath2(String laypath2) {
		this.laypath2 = laypath2;
	}
	public String getLaycount() {
		return laycount;
	}
	public void setLaycount(String laycount) {
		this.laycount = laycount;
	}
	public String getLaypath3() {
		return laypath3;
	}
	public void setLaypath3(String laypath3) {
		this.laypath3 = laypath3;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getPathThumb() {
		return pathThumb;
	}
	public void setPathThumb(String pathThumb) {
		this.pathThumb = pathThumb;
	}
	@Override
	public boolean isDownloaded() {
		return isDownloaded;
	}
	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public String getText() {
		return notetext;
	}
	public void setText(String text) {
		this.notetext = text;
	}
	@Override
	public String getPathThumbLarge() {
		return pathThumb;
	}
	public String getTextposition() {
		return textposition;
	}
	public void setTextposition(String textposition) {
		this.textposition = textposition;
	}
	public String getTextsize() {
		return textsize;
	}
	public void setTextsize(String textsize) {
		this.textsize = textsize;
	}
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	public String getPose() {
		return posepath;
	}
	public void setPose(String pose) {
		this.posepath = pose;
	}
	public String getTextcolor() {
		return textcolor;
	}
	public void setTextcolor(String textcolor) {
		this.textcolor = textcolor;
	}
	public String getBitmapposition() {
		return bitmapposition;
	}
	public void setBitmapposition(String bitmapposition) {
		this.bitmapposition = bitmapposition;
	}
	@Override
	public void saveToDatabase() {
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static boolean idExist(String id) {
		TimeCameraImageInfo dbInfo = DBHelper.getInstance().queryForFirst(
				TimeCameraImageInfo.class,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
		return dbInfo != null;
	}
	// 根据groupid来查询数据集合,查询的是非本地图片
	public static List<TimeCameraImageInfo> queryAll(String groupid) {
		List<TimeCameraImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(TimeCameraImageInfo.class,
				new DBWhere(DBKey.groupid, DBWhereType.eq, groupid),
				new DBWhere(DBKey.isLocal, DBWhereType.eq, false));
		return retVal;
	}
	// 查询所有
	public static List<TimeCameraImageInfo> queryAll() {
		List<TimeCameraImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(TimeCameraImageInfo.class);
		return retVal;
	}
	// 根据groupid和isDownloaded来查询数据集合
	public static List<TimeCameraImageInfo> queryDownload(String groupid) {
		List<TimeCameraImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(TimeCameraImageInfo.class,
				new DBWhere(DBKey.groupid, DBWhereType.eq, groupid),
				new DBWhere(DBKey.isDownloaded, DBWhereType.eq, true));
		//		Log.i(tag, "retVal" + retVal);
		return retVal;
	}
	// 查询所有标识下载过的数据(包含本地和网络图片)
	public static List<TimeCameraImageInfo> queryDownload() {
		List<TimeCameraImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(TimeCameraImageInfo.class,
				new DBWhere(DBKey.isDownloaded, DBWhereType.eq, true));
		// Log.i(tag, "retVal" + retVal);
		return retVal;
	}
	// 根据groupid和isDownloaded和isLocal来查询数据集合
	public static List<TimeCameraImageInfo> queryWebDownload(String groupid) {
		List<TimeCameraImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(TimeCameraImageInfo.class,
				new DBWhere(DBKey.groupid, DBWhereType.eq, groupid),
				new DBWhere(DBKey.isDownloaded, DBWhereType.eq, true),
				new DBWhere(DBKey.isLocal, DBWhereType.eq, false));
		return retVal;
	}
	// 根据id来删除某一项
	public static List<TimeCameraImageInfo> deleteByid(String id, String groupid) {
		DBHelper.getInstance().delete(TimeCameraImageInfo.class,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
		return queryDownload(groupid);
	}
	@Override
	public String toString() {
		return "TimeCameraImageInfo [id=" + id + ", groupid=" + groupid
				+ ", laypath1=" + laypath1 + ", laypath2=" + laypath2
				+ ", laypath3=" + laypath3 + ", pathThumb=" + pathThumb
				+ ", pathThumbLarge=" + pathThumbLarge + ", isDownloaded="
				+ isDownloaded + ", isLocal=" + isLocal + ", posepath="
				+ posepath + ", notetext=" + notetext + ", laycount="
				+ laycount + ", textposition=" + textposition + ", textsize="
				+ textsize + ", mapping=" + mapping + ", textcolor="
				+ textcolor + ", bitmapposition=" + bitmapposition
				+ ", birthTime=" + birthTime + "]";
	}
}
