package com.butterfly.vv.db.ormhelper;

import java.io.File;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.butterfly.vv.db.ImageDelayedTask;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder.DBOrderType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.butterfly.vv.db.ormhelper.bean.ChatDB;
import com.butterfly.vv.db.ormhelper.bean.CommentNotifies;
import com.butterfly.vv.db.ormhelper.bean.FootPrintDB;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.LikedNotifies;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.db.ormhelper.bean.MessageDB;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact;
import com.butterfly.vv.db.ormhelper.bean.SynDataDB;
import com.butterfly.vv.db.ormhelper.bean.TopNDB;
import com.butterfly.vv.db.ormhelper.bean.UrlDB;
import com.butterfly.vv.db.ormhelper.bean.UserBlackListDB;
import com.butterfly.vv.db.ormhelper.bean.UserFriendDB;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.UserNeighborDB;
import com.butterfly.vv.model.Comment;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.ui.entity.NewsTextInfo;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.beem.project.btf.utils.LogUtils;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.StatementBuilder;
import com.j256.ormlite.stmt.ThreadLocalSelectArg;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private final static String DATABASE_NAME = "vv.db";
//	private final static String DATABASE_NAME = Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "vv.db";
	private final static int DATABASE_VERSION = 13;
	private volatile static DBHelper instance;
	private Object lock = new Object();
	private Map<Class<?>, Dao<Class<?>, ?>> daoMap = new Hashtable<Class<?>, Dao<Class<?>, ?>>();
	private Set<Class<? extends BaseDB>> tables = new HashSet<Class<? extends BaseDB>>();
	{
		tables.add(UserBlackListDB.class);
		tables.add(UserFriendDB.class);
		tables.add(ImageFolder.class);
		tables.add(VVImage.class);
		tables.add(Contact.class);
		tables.add(Comment.class);
		tables.add(UrlDB.class);
		tables.add(UserNeighborDB.class);
		tables.add(ImageFolderNotify.class);
		tables.add(FootPrintDB.class);
		tables.add(TopNDB.class);
		tables.add(TimeCameraImageInfo.class);
		tables.add(PhoneContact.class);
		tables.add(SynDataDB.class);
		tables.add(MessageDB.class);
		tables.add(ChatDB.class);
		tables.add(NewsTextInfo.class);
		tables.add(NewsImageInfo.class);
		tables.add(FriendRequest.class);
		tables.add(CommentNotifies.class);
		tables.add(LikedNotifies.class);
		tables.add(LikedPhotoGroup.class);
	}
	private final ExecutorService listenerExecutor;
	private Map<BaseDB, StackTraceElement[]> threadMap = new ConcurrentHashMap<BaseDB, StackTraceElement[]>();
	private Map<BaseDB, Integer> indexMap = new ConcurrentHashMap<BaseDB, Integer>();

	public static DBHelper getInstance() {
		if (instance == null) {
			synchronized (DBHelper.class) {
				if (instance == null) {
					instance = new DBHelper(BeemApplication.getContext());
				}
			}
		}
		return instance;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <D extends Dao<T, ?>, T> D getDao(Class<T> cls) {
		if (daoMap.get(cls) == null) {
			synchronized (lock) {
				if (daoMap.get(cls) == null) {
					try {
						daoMap.put(cls, (Dao<Class<?>, ?>) super.getDao(cls));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (D) daoMap.get(cls);
	}
	public <T extends BaseDB> List<T> queryAll(Class<T> field, DBOrder[] order,
			DBWhere... wheres) {
		return queryAll(field, order, -1, wheres);
	}
	public <T extends BaseDB> List<T> queryAll(Class<T> field,
			DBWhere... wheres) {
		return queryAll(field, null, -1, -1, wheres);
	}
	public <T extends BaseDB> List<T> queryAll(Class<T> field, DBOrder order,
			int limit, DBWhere... wheres) {
		return queryAll(field, new DBOrder[] { order }, limit, -1, wheres);
	}
	public <T extends BaseDB> List<T> queryAll(Class<T> field,
			DBOrder[] orders, int limit, DBWhere... wheres) {
		return queryAll(field, orders, limit, -1, wheres);
	}
	public <T extends BaseDB> List<T> queryAll(Class<T> field,
			DBOrder[] orders, int limit, long offset, DBWhere... wheres) {
		try {
			Dao<T, ?> dao = getDao(field);
			QueryBuilder<T, ?> queryBuilder = dao.queryBuilder();
			try {
				if (limit > 0)
					queryBuilder.limit(limit);
				if (offset > 0)
					queryBuilder.offset(offset);
				fillupWhere(queryBuilder, wheres);
				fillupOrder(queryBuilder, orders);
				if (limit > 0) {
					queryBuilder.limit(limit);
				}
				// //LogUtils.i(queryBuilder.prepareStatementString());
				return queryBuilder.query();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public <T extends BaseDB> List<T> queryOrAll(Class<T> field,
			DBOrder[] orders, int limit, long offset, DBWhere... wheres) {
		try {
			Dao<T, ?> dao = getDao(field);
			QueryBuilder<T, ?> queryBuilder = dao.queryBuilder();
			try {
				if (limit > 0)
					queryBuilder.limit(limit);
				if (offset > 0)
					queryBuilder.offset(offset);
				fillupWhere(queryBuilder, wheres);
				fillupOrder(queryBuilder, orders);
				if (limit > 0) {
					queryBuilder.limit(limit);
				}
				// //LogUtils.i(queryBuilder.prepareStatementString());
				return queryBuilder.query();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public <T extends BaseDB> T queryForFirst(Class<T> field, DBWhere... wheres) {
		try {
			Dao<T, ?> dao = getDao(field);
			QueryBuilder<T, ?> queryBuilder = dao.queryBuilder();
			try {
				fillupWhere(queryBuilder, wheres);
				T retVal = queryBuilder.queryForFirst();
				if (retVal == null && field != UserFriendDB.class) {
					////LogUtils.e("query db error:null . " + queryBuilder.prepareStatementString(), 5);
				}
				return retVal;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public <T extends BaseDB> void saveToDatabaseAsync(final T field) {
		threadMap.put(field, Thread.currentThread().getStackTrace());
		indexMap.put(field, LogUtils.getCallStackPos(threadMap.get(field)));
		listenerExecutor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					field.saveToDatabase();
				} catch (Exception e) {
					e.printStackTrace();
					//LogUtils.e("save_db_error:"
					//							+ //LogUtils.getCallBackStr(threadMap.get(field), indexMap.get(field), 100));
				}
			}
		});
	}
	/**
	 * @Title: saveToDatabaseSync
	 * @Description: 同步保存数据库
	 * @param: @param cls
	 * @param: @param fields
	 * @param: @param wheres
	 * @return: void
	 * @throws:
	 */
	public <T extends BaseDB> void saveToDatabaseSync(T field,
			DBWhere... wheres) {
//		DBKey idKey = DBKey.id;
		Class<? extends BaseDB> cls = field.getClass();
		Map<String, Object> fieldMap = field.getFieldMap();
		/*if (cls.isAssignableFrom(Contact.class)) {
			idKey = DBKey.jid;
		} else if (cls.isAssignableFrom(PhoneContact.class)) {
			idKey = DBKey.phoneNum;
		} else if (cls.isAssignableFrom(UrlDB.class)) {
			idKey = DBKey.url;
		} else if (cls.isAssignableFrom(ImageDelayedTask.class)) {
			idKey = DBKey.uri;
		}
		if (!fieldMap.containsKey(idKey) || fieldMap.get(idKey) == null) {
			throw new IllegalArgumentException("save map must have id key.");
		}*/
		synchronized (lock) {
			//如果不加锁,写数据会出现插入两条相同的记录，抛出android.database.sqlite.SQLiteConstraintException: error code 19: constraint failed
			@SuppressWarnings("unchecked")
			Dao<T, ?> dao = (Dao<T, ?>) getDao(cls);
			try {
				if (fieldMap.size() > 0) {
					UpdateBuilder<T, ?> updateBuilder = dao.updateBuilder();
					if (!fieldMap.containsKey(DBKey.birthTime)) {
						field.setField(DBKey.birthTime,
								System.currentTimeMillis());
					}
					for (String key : fieldMap.keySet()) {
						if (fieldMap.get(key) instanceof String) {
							String val = (String) fieldMap.get(key);
							if (val.contains("'")) {
								String val_new = val.replaceAll("'", "''");
								fieldMap.put(key, val_new);
							}
						}
						updateBuilder.updateColumnValue(key,
								new ThreadLocalSelectArg(fieldMap.get(key)));
					}
					fillupWhere(updateBuilder, wheres);
					int num = updateBuilder.update();
					if (num < 1) {
						// create的时候才去保存birthTime
						////LogUtils.i("update failed create:" + updateBuilder.prepareStatementString() + " "+ fieldMap.toString());
						// 防止重复创建
						dao.create(field);
						////LogUtils.i("create:" + fields.toString());
					}
				} else {
					//LogUtils.e("create or update a empty data is not permitted!");
				}
			} catch (Exception e) {
				e.printStackTrace();
//				if (threadMap.containsKey(field)) {
//					//LogUtils.e("saveDBError:" + //LogUtils.getCallBackStr(threadMap.get(field), indexMap.get(field), 100));
//				}
			} finally {
				if (threadMap.containsKey(field)) {
					threadMap.remove(field);
					indexMap.remove(field);
				}
			}
		}
	}
	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		listenerExecutor = Executors
				.newSingleThreadExecutor(new ThreadFactory() {
					@Override
					public Thread newThread(Runnable runnable) {
						Thread thread = new Thread(runnable, "save_db_thread");
						thread.setPriority(Thread.MIN_PRIORITY);
						return thread;
					}
				});
	}
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource) {
		try {
			for (Class<?> cls : tables) {
				TableUtils.createTable(connectionSource, cls);
				//LogUtils.i("create table:" + cls.getName() + " DATABASE_NAME:" + DATABASE_NAME + " DATABASE_VERSION:"
				//						+ DATABASE_VERSION);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource, int arg2, int arg3) {
		try {
			for (Class<?> cls : tables) {
				TableUtils.dropTable(connectionSource, cls, true);
			}
			onCreate(sqLiteDatabase, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public <T extends BaseDB> int delete(Class<T> daoCls, DBWhere... clauses) {
		try {
			Dao<T, ?> dao = getDao(daoCls);
			DeleteBuilder<T, ?> deleteBuilder = dao.deleteBuilder();
			fillupWhere(deleteBuilder, clauses);
			// //LogUtils.i(deleteBuilder.prepareStatementString());
			return deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	public <T extends BaseDB> void deleteAsync(final Class<T> daoCls,
			final DBWhere... clauses) {
		listenerExecutor.submit(new Runnable() {
			@Override
			public void run() {
				delete(daoCls, clauses);
			}
		});
	}
	@Override
	public void close() {
		super.close();
		daoMap.clear();
	}
	private <T extends BaseDB> void fillupWhere(StatementBuilder<T, ?> builder,
			DBWhere... wheres) {
		if (wheres.length < 1) {
			return;
		}
		int i = 0;
		try {
			Where<T, ?> where = builder.where();
			for (DBWhere clause : wheres) {
				if (i > 0) {
					where.and();
				}
				i++;
				switch (clause.getClauseType()) {
					case between: {
						where.between(
								clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]),
								new ThreadLocalSelectArg(clause.getValues()[1]));
					}
						break;
					case eq: {
						where.eq(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case gt: {
						where.gt(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case ge: {
						where.ge(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case le: {
						where.le(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case lt: {
						where.lt(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case like: {
						where.like(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case ne: {
						where.ne(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()[0]));
						break;
					}
					case notIn: {
						where.notIn(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()));
						break;
					}
					case in: {
						where.in(clause.getColumnName().toString(),
								new ThreadLocalSelectArg(clause.getValues()));
						break;
					}
					default:
						//LogUtils.w("Error:something go wrong,shoudn't here,clause.getClauseType():"
						//								+ clause.getClauseType());
						break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private <T extends BaseDB> void fillupOrder(QueryBuilder<T, ?> builder,
			DBOrder[] orders) {
		if (orders == null) {
			return;
		}
		for (DBOrder order : orders)
			if (order != null) {
				builder.orderBy(order.getColumnName().toString(),
						order.getOrderType() == DBOrderType.asc);
			}
	}

	public static class DBOrder {
		private final DBKey columnName;
		private final DBOrderType orderType;

		public DBOrder(DBKey columnName, DBOrderType orderType) {
			super();
			this.columnName = columnName;
			this.orderType = orderType;
		}

		public enum DBOrderType {
			asc("asc"), desc("desc");
			DBOrderType(String explanation) {
				this.explanation = explanation;
			}

			public final String explanation;
		}

		public DBKey getColumnName() {
			return columnName;
		}
		public DBOrderType getOrderType() {
			return orderType;
		}
	}

	public static class DBWhere {
		private DBKey columnName;
		private Object[] values;
		private DBWhereType clauseType;

		public enum DBWhereType {
			eq("="), gt(">"), ge(">="), le("<="), lt("<"), like("like"), ne(
					"<>"), between("between..and"), notIn("not in"), in("in");
			DBWhereType(String explanation) {
				this.explanation = explanation;
			}

			public String explanation;
		}

		public DBWhere(DBKey columnName, DBWhereType type, Object... values) {
			if (columnName.toString().contains("jid")) {
				for (int i = 0; i < values.length; i++) {
					String str = (String) values[i];
					if (str != null && str.contains("@")) {
						throw new IllegalArgumentException(
								"jid should not contain the @");
					}
				}
			}
			this.columnName = columnName;
			this.values = values;
			this.clauseType = type;
		}
		public DBKey getColumnName() {
			return columnName;
		}
		public void setColumnName(DBKey columnName) {
			this.columnName = columnName;
		}
		public Object[] getValues() {
			return values;
		}
		public void setValues(Object[] values) {
			this.values = values;
		}
		public DBWhere.DBWhereType getClauseType() {
			return clauseType;
		}
		public void setClauseType(DBWhere.DBWhereType clauseType) {
			this.clauseType = clauseType;
		}
	}

	/**
	 * @Title: clearCache
	 * @Description: 清除数据库缓存
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void clearCache(long cacheMills) {
		try {
			long t1 = System.currentTimeMillis();
			for (Class<? extends BaseDB> cls : tables) {
				try {
					cls.newInstance().clearCache(cacheMills);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			//LogUtils.i("clear cache success.costMills:" + (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			e.printStackTrace();
			//LogUtils.i("clear cache failed.");
		}
	}
	
}
