/**
 * 
 */
package com.butterfly.vv.db.ormhelper;

import java.sql.SQLException;
import java.util.List;

import com.butterfly.vv.db.ormhelper.bean.CommentNotifies;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.db.ormhelper.bean.LikedNotifies;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import android.content.Context;

/**
 * @author hongbo ke
 *
 */
public class DatabaseDao {
	
	
	private DatabaseDao() {
	}
	
	public List<FriendRequest> getFriendRequest() {
		List<FriendRequest> result = null;
		try {
			Dao<FriendRequest, Void> dao = DBHelper.getInstance().getDao(FriendRequest.class);
			QueryBuilder<FriendRequest, Void> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy("status", true);
			queryBuilder.orderBy("time", false);
			result = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<CommentNotifies> getComentNotifies() {
		List<CommentNotifies> result = null;
		try {
			Dao<CommentNotifies, Void> dao = DBHelper.getInstance().getDao(CommentNotifies.class);
			QueryBuilder<CommentNotifies, Void> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy("time", false);
			result = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<LikedNotifies> getLikedNotifies() {
		List<LikedNotifies> result = null;
		try {
			Dao<LikedNotifies, Void> dao = DBHelper.getInstance().getDao(LikedNotifies.class);
			QueryBuilder<LikedNotifies, Void> queryBuilder = dao.queryBuilder();
			queryBuilder.orderBy("time", false);
			result = queryBuilder.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public volatile static DatabaseDao instance;
	
	public static DatabaseDao getInstance() {
		if (instance == null) {
			synchronized (DatabaseDao.class) {
				if (instance == null) {
					instance = new DatabaseDao();
				}
			}
		}
		return instance;
	}
}
