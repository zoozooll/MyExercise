package com.beem.project.btf.ui.entity;

import java.util.List;

import android.text.TextUtils;

import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.j256.ormlite.field.DatabaseField;

/** 娱乐相机图片场景实体类,相当于素材表 */
public class NewsImageInfo extends BaseDB implements CommonCameraInfo {
	//唯一标示场景与NewsTextInfo中的templateid对应
	@DatabaseField(id = true)
	private String id;
	//组id：标示娱乐相机素材所属板块
	@DatabaseField
	private String groupid;
	//场景图片路径
	@DatabaseField
	private String path;
	//场景图中镂空的位置
	@DatabaseField
	private String srcbmposition;
	//镂空的大小
	@DatabaseField
	private String srcbmsize;
	//场景缩略图路径
	@DatabaseField
	private String pathThumb;
	//标示是否下载过了
	@DatabaseField
	private boolean isDownloaded;
	//标示是否本地图片
	@DatabaseField
	private boolean isLocal;
	@DatabaseField
	private long birthTime;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public float[] getSrcbmposition() {
		if (!TextUtils.isEmpty(srcbmposition)) {
			String bitmapposition[] = BBSUtils.splitString(
					Constants.TIMECAMERA_TEXTPOSITIONSPLIT, srcbmposition);
			return new float[] { Float.parseFloat(bitmapposition[0]),
					Float.parseFloat(bitmapposition[1]) };
		} else {
			return new float[] { 0.0f, 0.0f };
		}
	}
	public void setSrcbmposition(String srcbmposition) {
		this.srcbmposition = srcbmposition;
	}
	public float[] getSrcbmsize() {
		if (!TextUtils.isEmpty(srcbmsize)) {
			String bitmapsize[] = BBSUtils.splitString(
					Constants.TIMECAMERA_TEXTPOSITIONSPLIT, srcbmsize);
			return new float[] { Float.parseFloat(bitmapsize[0]),
					Float.parseFloat(bitmapsize[1]) };
		} else {
			return new float[] { 0.0f, 0.0f };
		}
	}
	public void setSrcbmsize(String srcbmsize) {
		this.srcbmsize = srcbmsize;
	}
	@Override
	public void saveToDatabase() {
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	//判断某条数据是否存在
	public static boolean idExist(String id) {
		NewsImageInfo dbInfo = DBHelper.getInstance().queryForFirst(
				NewsImageInfo.class, new DBWhere(DBKey.id, DBWhereType.eq, id));
		return dbInfo != null;
	}
	//查询已下载的数据
	public static List<NewsImageInfo> queryDownload() {
		List<NewsImageInfo> retVal = DBHelper.getInstance().queryAll(
				NewsImageInfo.class,
				new DBWhere(DBKey.isDownloaded, DBWhereType.eq, true));
		return retVal;
	}
	//分组查询已下载的数据
	public static List<NewsImageInfo> queryDownload(String groupid) {
		List<NewsImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(NewsImageInfo.class,
				new DBWhere(DBKey.isDownloaded, DBWhereType.eq, true),
				new DBWhere(DBKey.groupid, DBWhereType.eq, groupid));
		return retVal;
	}
	//查询从网络下载的素材
	public static List<NewsImageInfo> queryWebDownloadMaterial(String groupid) {
		List<NewsImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(NewsImageInfo.class,
				new DBWhere(DBKey.isDownloaded, DBWhereType.eq, true),
				new DBWhere(DBKey.groupid, DBWhereType.eq, groupid),
				new DBWhere(DBKey.isLocal, DBWhereType.eq, false));
		return retVal;
	}
	//查询网络素材
	public static List<NewsImageInfo> queryWebAllMaterial(String groupid) {
		List<NewsImageInfo> retVal = null;
		retVal = DBHelper.getInstance().queryAll(NewsImageInfo.class,
				new DBWhere(DBKey.isLocal, DBWhereType.eq, false),
				new DBWhere(DBKey.groupid, DBWhereType.eq, groupid));
		return retVal;
	}
	public String getGroupid() {
		return groupid;
	}
	@Override
	public String toString() {
		return "NewsImageInfo [id=" + id + ", groupid=" + groupid + ", path="
				+ path + ", srcbmposition=" + srcbmposition + ", srcbmsize="
				+ srcbmsize + ", pathThumb=" + pathThumb + ", isDownloaded="
				+ isDownloaded + ", isLocal=" + isLocal + ", birthTime="
				+ birthTime + "]";
	}
	@Override
	public String getPathThumbLarge() {
		return pathThumb;
	}
}
