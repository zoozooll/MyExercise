package com.beem.project.btf.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.service.DataOperation;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.FootPrintDB;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.TopNDB;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

/**
 * @ClassName: ShareRankingManager
 * @Description: 排名脚印管理器
 * @author: yuedong bao
 * @date: 2015-4-14 上午11:21:19
 */
public class TopNFootprintManager {
	private static final String tag = TopNFootprintManager.class
			.getSimpleName();
	private static TopNFootprintManager instance = new TopNFootprintManager();

	private TopNFootprintManager() {
	}
	public static TopNFootprintManager getInstance() {
		return instance;
	}
	public List<ImageFolderItem> getTopn(Start start, int limit) throws WSError {
		topnOprt.setParams(start, limit);
		return topnOprt.getData(!BeemApplication.isNetworkOk());
	}
	public List<ImageFolderItem> getFootprint(Start start, int limit) throws WSError {
		footprintOprt.setParams(start, limit);
		return footprintOprt.getData(!BeemApplication.isNetworkOk());
	}

	private abstract class TopnFootprintOprt extends
			DataOperation<List<ImageFolderItem>> {
		protected Start start;
		protected int limit;

		public abstract void setParams(Start start, int limit);
	}

	private TopnFootprintOprt topnOprt = new TopnFootprintOprt() {
		@Override
		protected List<ImageFolderItem> getDataFromNetwork() throws WSError {
			List<ImageFolderItem> adapter = getTopnInner(start, limit);
			return adapter;
		}
		@Override
		protected List<ImageFolderItem> getDataFromDB() {
			List<ImageFolderItem> adapter = new ArrayList<ImageFolderItem>();
			List<TopNDB> listDB = TopNDB.query(start.getVal(), limit);
			List<ImageFolderItem> folderItems = new ArrayList<ImageFolderItem>();
			for (TopNDB footDB : listDB) {
				String jid = footDB.getFieldStr(DBKey.jid);
				String gid = footDB.getFieldStr(DBKey.gid);
				String createTime = footDB.getFieldStr(DBKey.createTime);
				ImageFolderItem folderItem;
				try {
					folderItem = ImageFolderItemManager
							.getInstance().getImageFolderItem(jid, gid, createTime, true, false);
					folderItems.add(folderItem);
				} catch (WSError e) {
					e.printStackTrace();
				}
			}
			return adapter;
		}
		@Override
		public void setParams(Start start, int limit) {
			this.start = start;
			this.limit = limit;
			setParams(TopNFootprintManager.class,
					"topnOprt:" + LoginManager.getInstance().getJidParsed()
							+ start.getVal() + limit);
		}
	};
	private TopnFootprintOprt footprintOprt = new TopnFootprintOprt() {
		@Override
		protected List<ImageFolderItem> getDataFromNetwork() throws WSError {
			List<ImageFolderItem> adapter = getFootprintInner(start,
					String.valueOf(limit));
			return adapter;
		}
		@Override
		protected List<ImageFolderItem> getDataFromDB() {
			List<FootPrintDB> listDB = FootPrintDB.query(start.getVal(), limit);
			List<ImageFolderItem> folderItems = new ArrayList<ImageFolderItem>();
			for (FootPrintDB footDB : listDB) {
				String jid = footDB.getFieldStr(DBKey.jid);
				String gid = footDB.getFieldStr(DBKey.gid);
				String createTime = footDB.getFieldStr(DBKey.createTime);
				ImageFolderItem folderItem;
				try {
					folderItem = ImageFolderItemManager
							.getInstance().getImageFolderItem(jid, gid, createTime, true, false);
					folderItems.add(folderItem);
				} catch (WSError e) {
					e.printStackTrace();
				}
			}
			return folderItems;
		}
		@Override
		public void setParams(Start start, int limit) {
			this.start = start;
			this.limit = limit;
			setParams(TopNFootprintManager.class,
					"footprint:" + LoginManager.getInstance().getJidParsed()
							+ start.getVal() + limit);
		}
	};

	private List<ImageFolderItem> pareFootprintOrTopN(String url,
			String[] names, Object[] values, Start start) throws WSError {
		List<ImageFolderItem> retVal = new ArrayList<ImageFolderItem>();
		long t1 = System.currentTimeMillis();
		try {
			String response = Caller.doGet(url, names, values);
			JSONObject data_json = new JSONObject(response);
			JSONArray data_jsonArr = data_json.getJSONArray("data");
			if (data_jsonArr != null) {
				//				//LogUtils.i("data_jsonArr.length:" + data_jsonArr.length());
				for (int i = 0; i < data_jsonArr.length(); i++) {
					Log.i(tag, "length:" + data_jsonArr.length());
					JSONObject dataItem_Json = data_jsonArr.getJSONObject(i);
					String jid = dataItem_Json.getString("tm_id");
					String gid = dataItem_Json.getString("gid");
					String createTime = dataItem_Json
							.getString("gid_create_time");
					ImageFolderItem folderItem = ImageFolderItemManager
							.getInstance().getImageFolderItem(jid, gid,
									createTime);
					if (folderItem != null) {
						retVal.add(folderItem);
					}
				}
				if (data_jsonArr.length() > 0)
					start.setVal(data_json.getString("next"));
				//				//LogUtils.i("retVal.size():" + retVal.size());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		//LogUtils.i(" pareFootprintOrTopN cost2(ms): " + (System.currentTimeMillis() - t1));
		return retVal;
	}
	// 获取脚印
	private List<ImageFolderItem> getFootprintInner(Start start, String limit) throws WSError {
		long before = System.currentTimeMillis();
		String url = AppProperty.getInstance().VVAPI + "/get_footprint";
		String[] names = null;
		Object[] values = null;
		String jid = "0", sessionId = "0";
		try {
			jid = LoginManager.getInstance().getJidParsed();
			sessionId = LoginManager.getInstance().getSessionId();
		} catch (IllegalStateException e) {
		}
		if (TextUtils.isEmpty(start.getVal())) {
			names = new String[] { "tm_id", "session_id", "lat", "lon", "limit" };
			values = new Object[] { jid, sessionId,
					BDLocator.getInstance().getLat(),
					BDLocator.getInstance().getLon(), limit };
		} else {
			names = new String[] { "tm_id", "session_id", "lat", "lon",
					"start", "limit" };
			values = new Object[] { jid, sessionId,
					BDLocator.getInstance().getLat(),
					BDLocator.getInstance().getLon(), start.getVal(), limit };
		}
		List<ImageFolderItem> retVal = pareFootprintOrTopN(url, names, values,
				start);
		if (retVal != null) {
			for (ImageFolderItem folderItem : retVal) {
				ImageFolder folder = folderItem.getImageFolder();
				FootPrintDB footprintDB = new FootPrintDB();
				footprintDB.setField(DBKey.jid, folder.getJid());
				footprintDB.setField(DBKey.gid, folder.getGid());
				footprintDB.setField(DBKey.createTime, folder.getCreateTime());
				footprintDB.setField(DBKey.distance,
						folder.getField(DBKey.distance));
				footprintDB.saveToDatabase();
			}
			//			//LogUtils.i("getFootprint cost time(ms):" + (System.currentTimeMillis() - before));
		}
		return retVal;
	}
	// 获取排行
	private List<ImageFolderItem> getTopnInner(Start start, int limit) throws WSError {
		long before = System.currentTimeMillis();
		String url = AppProperty.getInstance().VVAPI + "/get_top_n";
		String[] names = null;
		String[] values = null;
		if (TextUtils.isEmpty(start.getVal())) {
			names = new String[] { "tm_id", "session_id", "limit" };
			values = new String[] { LoginManager.getInstance().getJidParsed(),
					LoginManager.getInstance().getSessionId(),
					String.valueOf(limit) };
		} else {
			names = new String[] { "tm_id", "session_id", "start", "limit" };
			values = new String[] { LoginManager.getInstance().getJidParsed(),
					LoginManager.getInstance().getSessionId(), start.getVal(),
					String.valueOf(limit) };
		}
		List<ImageFolderItem> retVal = pareFootprintOrTopN(url, names, values,
				start);
		if (retVal != null) {
			for (ImageFolderItem folderItem : retVal) {
				ImageFolder folder = folderItem.getImageFolder();
				TopNDB topNDB = new TopNDB();
				topNDB.setField(DBKey.jid, folder.getJid());
				topNDB.setField(DBKey.gid, folder.getGid());
				topNDB.setField(DBKey.createTime, folder.getCreateTime());
				topNDB.setField(DBKey.thumbupCount, folder.getThumbupCount());
				topNDB.saveToDatabase();
			}
		}
		//		//LogUtils.i("getTopn cost time(ms):" + (System.currentTimeMillis() - before));
		return retVal;
	}
}
