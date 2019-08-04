package com.butterfly.vv.db.ormhelper.bean;

import android.os.SystemClock;

import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;

/**
 * @ClassName: UrlDB
 * @Description:请求url信息：生成时间，数据寿命
 * @author: yuedong bao
 * @date: 2015-3-31 下午12:02:00
 */
public class UrlDB extends BaseDB {
	private static final long DEF_LIFE_TIMEMILLIONS = 20 * 1000l;
	@DatabaseField(id = true)
	private String url;
	@DatabaseField
	protected long birthTime;
	@DatabaseField
	protected long lifetime = DEF_LIFE_TIMEMILLIONS;

	public UrlDB() {
		super();
	}
	public UrlDB(String url) {
		this(url, DEF_LIFE_TIMEMILLIONS);
	}
	/**
	 * @Title:UrlDB
	 * @Description:TODO
	 * @param url 请求的url
	 * @param lifetime 数据寿命(单位秒)
	 */
	public UrlDB(String url, long lifetime) {
		this();
		setField(DBKey.url, url);
		setField(DBKey.lifetime, lifetime);
		setField(DBKey.birthTime, SystemClock.elapsedRealtime());
	}
	@Override
	public void saveToDatabase() {
		//LogUtils.i("born the url:" + url + " at:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(birthTime)
		//				+ " life time(ms):" + (lifetime));
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.url, DBWhereType.eq, url));
	}
	public boolean isDead() {
		boolean isDead = (birthTime + lifetime - System.currentTimeMillis()) < 0;
		if (isDead) {
			//LogUtils.i("url isDead:" + url + " " + "[ birthTime:" + birthTime + ",lifeTime:" + lifetime + ",curTime:"
			//					+ System.currentTimeMillis() + "]");
		}
		return isDead;
	}
	@Override
	public String toString() {
		return "UrlDB [url=" + url + ", birthTime=" + birthTime + ", lifetime="
				+ lifetime + "]";
	}
}
