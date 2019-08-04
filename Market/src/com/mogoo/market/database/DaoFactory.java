package com.mogoo.market.database;

import android.content.Context;

import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.Apps;
import com.mogoo.market.model.Game;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.model.Topic;


public class DaoFactory {

	private DaoFactory() {
		
	}

	public static IBeanDao<Topic> getTopicDao(Context context) {
		return TopicDaoImpl.getInstance(context);
	}
	
	public static IBeanDao<Apps> getAppsCateDao(Context context) {
		return AppsCateDaoImpl.getInstance(context);
	}
	
	public static IBeanDao<Game> getGameCateDao(Context context) {
		return GameCateDaoImpl.getInstance(context);
	}
	
	public static IBeanDao<HotApp> getHotDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_HOT);
	}
	
	public static IBeanDao<HotApp> getLatestDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_LATEST);
	}
	
	public static IBeanDao<HotApp> getNecessaryDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_NECESSARY);
	}
	
	public static IBeanDao<HotApp> getTopRankingDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_TOP_RANKING);
	}
	
	public static IBeanDao<HotApp> getAppsRankingDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_APPS_RANKING);
	}
	
	public static IBeanDao<HotApp> getGameRankingDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_GAME_RANKING);
	}

	public static DownloadInfoDaoImpl getDownloadInfoDao(Context context) 
	{
		return DownloadInfoDaoImpl.getInstance(context, DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO);
	}
		
	public static IBeanDao<HotApp> getChildCateDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_CHILD_CATE);
	}
	
	public static IBeanDao<HotApp> getQualityAppsDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_QUALITY_APPS);
	}
	
	public static IBeanDao<HotApp> getNewestAppsDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_NEWEST_APPS);
	}
	
	public static IBeanDao<HotApp> getQualityGameDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_QUALITY_GAME);
	}
	
	public static IBeanDao<HotApp> getNewestGameDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_NEWEST_GAME);
	}
	
	public static IBeanDao<HotApp> getInstallousDao(Context context) {
		return ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_INSTALLOUS);
	}
	
	public static void clearDataBase(Context context) {
		TopicDaoImpl.getInstance(context).clearAllBean();
		AppsCateDaoImpl.getInstance(context).clearAllBean();
		GameCateDaoImpl.getInstance(context).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_HOT).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_LATEST).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_NECESSARY).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_TOP_RANKING).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_APPS_RANKING).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_GAME_RANKING).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_CHILD_CATE).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_QUALITY_APPS).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_NEWEST_APPS).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_QUALITY_GAME).clearAllBean();
		ApkListDaoImpl.getInstance(context, ApkListSQLTable.TABLE_NEWEST_GAME).clearAllBean();
		//DownloadInfoDaoImpl不能清除
	}
}
