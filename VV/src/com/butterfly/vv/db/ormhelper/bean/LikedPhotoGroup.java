/**
 * 
 */
package com.butterfly.vv.db.ormhelper.bean;

import java.sql.SQLException;

import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder.StatementType;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.CompiledStatement;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.support.DatabaseResults;

/**
 * @author hongbo ke
 *
 */
public class LikedPhotoGroup extends BaseDB {
	
	@DatabaseField(columnName = "_id", generatedId = true)
	private int id;
	@DatabaseField
	private String gid;
	@DatabaseField
	private String gid_creat_time;
	@DatabaseField
	private String gid_jid;
	@DatabaseField
	private long likedTime;
	@DatabaseField
	private String birthTime;
	
	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGid_creat_time() {
		return gid_creat_time;
	}

	public void setGid_creat_time(String gid_creat_time) {
		this.gid_creat_time = gid_creat_time;
	}

	public String getGid_jid() {
		return gid_jid;
	}

	public void setGid_jid(String gid_jid) {
		this.gid_jid = gid_jid;
	}

	public long getLikedTime() {
		return likedTime;
	}

	public void setLikedTime(long likedTime) {
		this.likedTime = likedTime;
	}

	/* (non-Javadoc)
	 * @see com.butterfly.vv.db.ormhelper.bean.BaseDB#saveToDatabase()
	 */
	@Override
	public void saveToDatabase() {
		Dao<LikedPhotoGroup, Void> dao = DBHelper.getInstance().getDao(this.getClass());
		try {
			dao.createOrUpdate(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isLiked(String gid, String gid_creat_time, String gidjid) {
		boolean flag = false;
		try {
			Dao<LikedPhotoGroup, Void> dao = DBHelper.getInstance().getDao(LikedPhotoGroup.class);
			QueryBuilder<LikedPhotoGroup, Void> qb = dao.queryBuilder();
			Where<LikedPhotoGroup, Void> where = qb.where()
				.eq("gid", gid)
				.and()
				.eq("gid_creat_time", BBSUtils.getShortGidCreatTime(gid_creat_time))
				.and()
				.eq("gid_jid", gidjid);
			LikedPhotoGroup rs = qb.queryForFirst();
			flag = (rs != null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
				
	} 
	
	public static boolean clearAll() {
		boolean flag = false;
		try {
			Dao<LikedPhotoGroup, Void> dao = DBHelper.getInstance().getDao(LikedPhotoGroup.class);
			dao.queryRaw("delete from likedphotogroup");
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
				
	} 
}
