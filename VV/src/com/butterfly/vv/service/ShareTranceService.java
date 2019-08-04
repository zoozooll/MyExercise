package com.butterfly.vv.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.UrlConfigUtil;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.ShareType;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.LogUtils;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.butterfly.vv.db.ormhelper.bean.FootPrintDB;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.db.ormhelper.bean.TopNDB;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

public class ShareTranceService {
	// 获取脚印
	/**
	 * @Title: getFootprint
	 * @Description: TODO
	 * @param start
	 * @param limit
	 * @param isGetoff 是否获取离线
	 * @return
	 * @return: List<ImageFolderItem>
	 */
	public static List<ImageFolderItem> getFootprintOrTopN(Start start,
			int limit, boolean isGetoff, ShareType type) throws WSError {
		if (type == ShareType.FootPrint) {
			return getFootprint(start, limit, isGetoff);
		} else {
			return getTopn(start, limit, isGetoff);
		}
	}
	private static List<ImageFolderItem> getFootprint(Start start, int limit,
			boolean isGetoff) throws WSError {
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
			names = new String[] { "tm_id", "session_id", "lat", "lon",
					"limit", "version" };
			values = new Object[] { jid, sessionId,
					BDLocator.getInstance().getLat(),
					BDLocator.getInstance().getLon(),
					String.valueOf(limit), BBSUtils.getVersionName() };
		} else {
			names = new String[] { "tm_id", "session_id", "lat", "lon",
					"start", "limit", "version" };
			values = new Object[] { jid, sessionId,
					BDLocator.getInstance().getLat(),
					BDLocator.getInstance().getLon(), start.getVal(),
					String.valueOf(limit), BBSUtils.getVersionName() };
		}
		url = BBSUtils.AssembleUrl(url, names, values);
		List<ImageFolderItem> retVal = new ArrayList<ImageFolderItem>();
		if (isGetoff && !UrlConfigUtil.isUrlTimeout(url + start + limit)) {
			List<FootPrintDB> queryList = FootPrintDB.query(start.getValOff(),
					limit);
			if (queryList != null) {
				for (FootPrintDB queryOne : queryList) {
					ImageFolderItem result = ImageFolderItemManager
							.getInstance().getImageFolderItem(
									queryOne.getFieldStr(DBKey.jid),
									queryOne.getFieldStr(DBKey.gid),
									queryOne.getFieldStr(DBKey.createTime),
									true, true);
					if (result != null) {
						retVal.add(result);
					} else {
						//LogUtils.e("query error:the result not exist.");
					}
				}
				if (queryList.size() > 0) {
					start.setValOff(queryList.get(queryList.size() - 1)
							.getFieldStr(DBKey.id));
				}
			} else {
				//LogUtils.e("query error:the result not exist.");
			}
		} else {
			
			boolean success = pareFootprintOrTopN(url, start,
					retVal, ShareType.FootPrint);
			if (success) {
				UrlConfigUtil.saveUrlDB(url);
			}
			//LogUtils.i("getFootprint cost time(ms):" + (System.currentTimeMillis() - before) + " retVal.size:"
			//					+ retVal.size());
		}
		return retVal;
	}
	// 获取排行
	private static List<ImageFolderItem> getTopn(Start start, int limit,
			boolean isOffline) throws WSError {
		List<ImageFolderItem> retVal = new ArrayList<ImageFolderItem>();
		String url = AppProperty.getInstance().VVAPI + "/get_top_n";
		String[] names = null;
		String[] values = null;
		String jid = "0", sessionId = "0";
		try {
			jid = LoginManager.getInstance().getJidParsed();
			sessionId = LoginManager.getInstance().getSessionId();
		} catch (IllegalStateException e) {
		}
		if (TextUtils.isEmpty(start.getVal())) {
			names = new String[] { "tm_id", "session_id", "limit",
					"version" };
			values = new String[] { jid, sessionId, String.valueOf(limit),
					BBSUtils.getVersionName() };
		} else {
			names = new String[] { "tm_id", "session_id", "start", "limit",
					"version" };
			values = new String[] { jid, sessionId, start.getVal(),
					String.valueOf(limit), BBSUtils.getVersionName() };
		}
		url = BBSUtils.AssembleUrl(url, names, values);
		if (isOffline && !UrlConfigUtil.isUrlTimeout(url)) {
			List<TopNDB> queryList = TopNDB.query(start.getValOff(), limit);
			if (queryList != null) {
				for (TopNDB queryOne : queryList) {
					ImageFolderItem result = ImageFolderItemManager
							.getInstance().getImageFolderItem(
									queryOne.getFieldStr(DBKey.jid),
									queryOne.getFieldStr(DBKey.gid),
									queryOne.getFieldStr(DBKey.createTime),
									true, true);
					if (result != null) {
						retVal.add(result);
					} else {
						//LogUtils.e("query error:the result not exist.");
					}
				}
				if (queryList.size() > 0) {
					start.setValOff(queryList.get(queryList.size() - 1)
							.getFieldStr(DBKey.id));
				}
			} else {
				//LogUtils.e("query error:the result not exist.");
			}
		} else {
			//			long before = System.currentTimeMillis();
			
			boolean success = pareFootprintOrTopN(url, start,
					retVal, ShareType.TopN);
			if (success) {
				UrlConfigUtil.saveUrlDB(url);
			}
		}
		return retVal;
	}
	private static boolean pareFootprintOrTopN(String url, Start start, List<ImageFolderItem> retVal,
			ShareType type) throws WSError {
		try {
			String response = Caller.doGet(url);
			//			LogUtils.i("pareFootprintOrTopN_cgi:" + (System.currentTimeMillis() - t1) + " " + type);
//			LogUtils.i(String.format("response url:%s RS==>%s", url, response));
			JSONObject data_json = new JSONObject(response);
			JSONArray data_jsonArr = data_json.getJSONArray("data");
			String jid = LoginManager.getInstance().getJidParsed();
			if (data_jsonArr != null) {
				if (type == ShareType.FootPrint) {
					FootPrintDB.deleteAll(jid);
				} else if (type == ShareType.TopN) {
					TopNDB.deleteAll(jid);
				}
				List<ImageFolder> list = ImageFolder.queryThumbUpAll();
				Map<String, ImageFolder> map = new Hashtable<String, ImageFolder>();
				if (list != null) {
					for (ImageFolder folderDB : list) {
						map.put(folderDB.getId(), folderDB);
					}
				}
				for (int i = 0; i < data_jsonArr.length(); i++) {
					JSONObject dataItem_Json = data_jsonArr.getJSONObject(i);
					ImageFolderItem folderItem = new ImageFolderItem();
					ImageFolder folder = new ImageFolder();
					folder.setField(DBKey.thumbupCount, Integer
							.parseInt(dataItem_Json.optString("thumb_up")));
					ArrayList<CommentItem> commentItems = CommonService
							.parseComment(
									dataItem_Json.optJSONArray("comment"),
									dataItem_Json.optString("gid"),
									dataItem_Json.optString("gid_create_time"),
									dataItem_Json.optString("tm_id"));
					ArrayList<VVImage> images = CommonService.parseImage(
							dataItem_Json.getJSONArray("photo"),
							dataItem_Json.optString("gid"),
							dataItem_Json.optString("gid_create_time"),
							dataItem_Json.optString("tm_id"));
					folder.setField(DBKey.createTime,
							dataItem_Json.optString("gid_create_time"));
					folder.setField(DBKey.lon, Double.parseDouble(dataItem_Json
							.optString("pg_lon")));
					folder.setField(DBKey.lat, Double.parseDouble(dataItem_Json
							.optString("pg_lat")));
					folder.setField(DBKey.jid, dataItem_Json.optString("tm_id"));
					folder.setField(DBKey.signature,
							dataItem_Json.optString("signature"));
					folder.setField(DBKey.commentCount, Integer
							.parseInt(dataItem_Json.optString("comment_count")));
					folder.setField(DBKey.gid, dataItem_Json.optString("gid"));
					folder.setField(DBKey.photoCount, Integer
							.parseInt(dataItem_Json.optString("photo_count")));
					folder.setField(DBKey.topic,
							dataItem_Json.optString("topic"));
					folder.setField(DBKey.grade,
							dataItem_Json.optString("grade"));
					folder.setField(DBKey.album_url,
							dataItem_Json.optString("album_url"));
					folder.setField(DBKey.imagefoldertype,
							dataItem_Json.optString("type"));
					if (dataItem_Json.has("distance")) {
						folder.setField(DBKey.distance,
								dataItem_Json.optString("distance"));
					} else {
						// 排名界面服务器给的距离单位是km
						double distance = BBSUtils.latlon2Distance(LoginManager.getInstance().getLat(), 
								LoginManager.getInstance().getLon(),
								Double.parseDouble(dataItem_Json
										.optString("pg_lat")), Double
										.parseDouble(dataItem_Json
												.optString("pg_lon"))) / 1000;
						folder.setField(DBKey.distance,
								String.valueOf(distance));
					}
					ImageFolder thumbUpFolder = map.get(BaseDB.createID(
							folder.getJid(), folder.getGid(),
							folder.getCreateTime()));
					folder.setThumbup(LikedPhotoGroup.isLiked(folder.getGid(), folder.getCreateTime(), folder.getJid()));
					folder.saveToDatabaseAsync();
					//设置ImageFolder的点赞状态
					Contact contact = new Contact();
					contact.setField(DBKey.jid,
							dataItem_Json.optString("tm_id"));
					contact.setField(DBKey.sex, dataItem_Json.optString("sex"));
					contact.setField(DBKey.lat, Double
							.parseDouble(dataItem_Json.optString("user_lat")));
					contact.setField(DBKey.lon, Double
							.parseDouble(dataItem_Json.optString("user_lon")));
					contact.setField(DBKey.photo_small,
							dataItem_Json.optString("portrait_small"));
					contact.setField(DBKey.logintime,
							dataItem_Json.optString("logintime"));
					contact.setField(DBKey.bday,
							dataItem_Json.optString("bday"));
					contact.setField(DBKey.nickName,
							dataItem_Json.optString("nickname"));
					if (!LoginManager.getInstance().isMyJid(contact.getJid())) {
						contact.saveToDatabaseAsync();
					}
					folderItem.setImageFolder(folder);
					folderItem.setContact(contact);
					folderItem.setVVImages(images);
					folderItem.addComments(commentItems);
					retVal.add(folderItem);
					if (type == ShareType.FootPrint) {
						FootPrintDB saveData = new FootPrintDB();
						saveData.setField(DBKey.jid_login, jid);
						saveData.setField(DBKey.jid, folder.getJid());
						saveData.setField(DBKey.gid, folder.getGid());
						saveData.setField(DBKey.createTime,
								folder.getCreateTime());
						saveData.setField(
								DBKey.distance,
								LoginManager.getInstance().latlon2Distance(
										folder.getLat(), folder.getLon()));
						saveData.saveToDatabaseAsync();
					} else if (type == ShareType.TopN) {
						TopNDB saveData = new TopNDB();
						saveData.setField(DBKey.jid_login, jid);
						saveData.setField(DBKey.jid, folder.getJid());
						saveData.setField(DBKey.gid, folder.getGid());
						saveData.setField(DBKey.createTime,
								folder.getCreateTime());
						saveData.setField(DBKey.thumbupCount,
								folder.getThumbupCount());
						saveData.saveToDatabaseAsync();
					}
				}
				if (!retVal.isEmpty()) {
					start.setVal(data_json.getString("next"));
				}
			}
			return true;
			//LogUtils.i("pareFootprintOrTopN_parse:" + (System.currentTimeMillis() - t1) + " " + type);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
