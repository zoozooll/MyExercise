package com.beem.project.btf.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo.NewsTextType;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.ui.entity.NewsTextInfo;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

public class NewsCameraMaterialUtil {
	private static String newscameratvlocal = "image/NewsCameraTv/NewsCameraTVLocal.json";

	//private static String newscameratvservice="image/NewsCameraTv/NewsCameraTVService.json";
	/**
	 * 初始化素材数据库
	 */
	public static void initTVMaterialDB() {
		SaveToDB(getJsonData(newscameratvlocal), true);
		//SaveToDB(getJsonData(newscameratvservice), true);
	}
	public static void initTopTitleMaterialDB(String filename) {
		SaveToDB(getJsonData(filename), true);
	}
	/**
	 * 解析json并保存到数据库
	 */
	public static void SaveToDB(String str, boolean islocal) {
		try {
			JSONObject responseJObject = new JSONObject(str);
			JSONArray data = responseJObject.getJSONArray("data");
			if (data != null && data.length() > 0) {
				for (int i = 0; i < data.length(); i++) {
					JSONObject item = data.getJSONObject(i);
					// 表中没有的素材才去记录
					//if (!NewsImageInfo.idExist(item.getString("id"))) {
					//保存图片实体类
					NewsImageInfo ImageInfo = new NewsImageInfo();
					String id = item.optString("id");
					ImageInfo.setField(DBKey.id, id);
					ImageInfo.setField(DBKey.isLocal, islocal);
					if (islocal) {
						ImageInfo.setField(DBKey.isDownloaded, true);
					}
					ImageInfo.setField(DBKey.path, item.optString("path"));
					ImageInfo
							.setField(DBKey.groupid, item.optString("groupid"));
					ImageInfo.setField(DBKey.pathThumb,
							item.optString("paththumb"));
					ImageInfo.setField(DBKey.srcbmposition,
							item.optString("srcbmposition"));
					ImageInfo.setField(DBKey.srcbmsize,
							item.optString("srcbmsize"));
					ImageInfo.saveToDatabase();
					//保存娱乐相机文字实体类
					JSONArray sub = item.optJSONArray("subtitle");
					JSONArraySaveToDb(sub, id);
					JSONArray big = item.optJSONArray("bigtitle");
					JSONArraySaveToDb(big, id);
					JSONArray small = item.optJSONArray("smalltitle");
					JSONArraySaveToDb(small, id);
					JSONArray marquee = item.optJSONArray("marquee");
					JSONArraySaveToDb(marquee, id);
					JSONArray area = item.optJSONArray("area");
					JSONArraySaveToDb(area, id);
					JSONArray time = item.optJSONArray("time");
					JSONArraySaveToDb(time, id);
					JSONArray toptitle = item.optJSONArray("toptitle");
					saveTopTitleToDb(toptitle, id);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	//将头条新闻的数据写到数据库中
	private static void saveTopTitleToDb(JSONArray jarray, String id)
			throws JSONException {
		if (jarray != null && jarray.length() > 0) {
			NewsTextInfo textinfo = new NewsTextInfo();
			for (int i = 0; i < jarray.length(); i++) {
				JSONObject jsonObj = jarray.getJSONObject(i);
				textinfo.setField(DBKey.id,
						id + NewsTextType.toptitle.toString() + i);
				textinfo.setField(DBKey.templateid, id);
				textinfo.setField(DBKey.texttype,
						NewsTextType.toptitle.toString());
				textinfo.setField(DBKey.textposition,
						jsonObj.optString("textPosition"));
				textinfo.setField(DBKey.textsize, jsonObj.optString("textSize"));
				textinfo.setField(DBKey.textcolor,
						jsonObj.optString("textColor"));
				textinfo.setField(DBKey.textBound,
						jsonObj.optString("textBound"));
				textinfo.setField(DBKey.isshadow,
						jsonObj.optString("hasShadow"));
				textinfo.setField(DBKey.fontPath, jsonObj.optString("fontPath"));
				textinfo.setField(DBKey.notetext, jsonObj.optString("noteText"));
				textinfo.setField(DBKey.gravity,
						jsonObj.optString("textGravity"));
				textinfo.setField(DBKey.linenum, jsonObj.optInt("linenum", 1));
				textinfo.setField(DBKey.isBold,
						jsonObj.optBoolean("isBold", false));
				textinfo.saveToDatabase();
			}
		}
	}
	/**
	 * json数组存储到数据库
	 * @throws JSONException
	 */
	private static void JSONArraySaveToDb(JSONArray jarray, String id)
			throws JSONException {
		if (jarray != null && jarray.length() > 0) {
			NewsTextInfo textinfo = new NewsTextInfo();
			for (int i = 0; i < jarray.length(); i++) {
				switch (i) {
					case 0: {
						textinfo.setField(DBKey.id, id + jarray.optString(i));
						textinfo.setField(DBKey.templateid, id);
						textinfo.setField(DBKey.texttype, jarray.optString(i));
						break;
					}
					case 1: {
						textinfo.setField(DBKey.textposition,
								jarray.optString(i));
						break;
					}
					case 2: {
						textinfo.setField(DBKey.textsize, jarray.optString(i));
						break;
					}
					case 3: {
						textinfo.setField(DBKey.textcolor, jarray.optString(i));
						break;
					}
					case 4: {
						textinfo.setField(DBKey.iscenter, jarray.optString(i));
						break;
					}
					case 5: {
						textinfo.setField(DBKey.isshadow, jarray.optString(i));
						break;
					}
					case 6: {
						textinfo.setField(DBKey.notetext, jarray.optString(i));
						break;
					}
				}
			}
			textinfo.saveToDatabase();
		}
	}
	/**
	 * 从assert文件夹中读取json文件
	 */
	private static String getJsonData(String filename) {
		Context context = BeemApplication.getContext();
		String str = null;
		try {
			StringBuilder sb = new StringBuilder();
			InputStream is = context.getAssets().open(filename);
			int len;
			// 用reader方式读取文本的文件，而不直接用inputstream方式。防止utf-8编码出错问题。
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			char[] buf = new char[256];
			while ((len = br.read(buf)) > -1) {
				sb.append(buf, 0, len);
			}
			is.close();
			br.close();
			str = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Log.i("NewsTVEditorFragment", "json:"+str);
		return str;
	}
	/** 获取数据库数据(所有数据) */
	public static List<NewsCameraImageInfo> queryAll() {
		List<NewsImageInfo> newsImageInfos = NewsImageInfo.queryDownload();
		List<NewsCameraImageInfo> newsCameraImageInfos = combineNewsCameraInfos(newsImageInfos);
		return newsCameraImageInfos;
	}
	/** 分组获取下载好的素材 */
	public static List<NewsCameraImageInfo> queryDownload(String groudId) {
		List<NewsImageInfo> newsImageInfos = NewsImageInfo
				.queryDownload(groudId);
		List<NewsCameraImageInfo> newsCameraImageInfos = combineNewsCameraInfos(newsImageInfos);
		return newsCameraImageInfos;
	}
	/** 获取已经下载好的网络素材模板 */
	public static List<NewsImageInfo> queryWebDownloadMaterialTemplate(
			String groudId) {
		List<NewsImageInfo> newsImageInfos = NewsImageInfo
				.queryWebDownloadMaterial(groudId);
		return newsImageInfos;
	}
	/** 获取所有素材 */
	public static List<NewsImageInfo> queryWebAllMaterialTemplate(String groudId) {
		List<NewsImageInfo> newsImageInfos = NewsImageInfo
				.queryWebAllMaterial(groudId);
		return newsImageInfos;
	}
	/** 获取单个数据 */
	public static NewsCameraImageInfo querySingleMaterial(
			NewsImageInfo newsImageInfo) {
		return combineNewsCameraInfo(newsImageInfo);
	}
	/**
	 * @Title: combineNewsCameraInfos
	 * @Description: 查询NewsImageInfo，组装NewsCameraImageInfo
	 * @param: @param newsImageInfos
	 * @param: @return
	 * @return: List<NewsCameraImageInfo>
	 * @throws:
	 */
	private static List<NewsCameraImageInfo> combineNewsCameraInfos(
			List<NewsImageInfo> newsImageInfos) {
		List<NewsCameraImageInfo> newsCameraImageInfos = null;
		if (newsImageInfos != null && newsImageInfos.size() > 0) {
			newsCameraImageInfos = new ArrayList<NewsCameraImageInfo>();
			for (int i = 0; i < newsImageInfos.size(); i++) {
				NewsImageInfo newsImageInfo = newsImageInfos.get(i);
				NewsCameraImageInfo nImageInfo = querySingleMaterial(newsImageInfo);
				newsCameraImageInfos.add(nImageInfo);
			}
		}
		return newsCameraImageInfos;
	}
	/**
	 * @Title: combineNewsCameraInfo
	 * @Description: 查询NewsImageInfo，组装NewsCameraImageInfo,取单项数据
	 * @param: @param newsImageInfos
	 * @param: @return
	 * @return: NewsCameraImageInfo
	 */
	private static NewsCameraImageInfo combineNewsCameraInfo(
			NewsImageInfo newsImageInfo) {
		NewsCameraImageInfo nImageInfo = null;
		if (newsImageInfo != null) {
			nImageInfo = new NewsCameraImageInfo();
			nImageInfo.setImageinfo(newsImageInfo);
			nImageInfo.setSubtitle(NewsTextInfo.queryItemsById(newsImageInfo
					.getId() + NewsTextType.subtitle.toString()));
			nImageInfo.setBigtitle(NewsTextInfo.queryItemsById(newsImageInfo
					.getId() + NewsTextType.bigtitle.toString()));
			nImageInfo.setSmalltitle(NewsTextInfo.queryItemsById(newsImageInfo
					.getId() + NewsTextType.smalltitle.toString()));
			nImageInfo.setMarquee(NewsTextInfo.queryItemsById(newsImageInfo
					.getId() + NewsTextType.marquee.toString()));
			nImageInfo.setArea(NewsTextInfo.queryItemsById(newsImageInfo
					.getId() + NewsTextType.area.toString()));
			nImageInfo.setTime(NewsTextInfo.queryItemsById(newsImageInfo
					.getId() + NewsTextType.time.toString()));
			List<NewsTextInfo> toptitles = NewsTextInfo
					.querySomeItemsByTemplateId(newsImageInfo.getId(),
							NewsTextType.toptitle);
			nImageInfo.setToptitlesTextInfo(toptitles);
		}
		return nImageInfo;
	}
	public static void deleteWebDownloadedMaterial(String id) {
	}
	public static void downloadWebMaterial(
			NewsCameraImageInfo newsCameraImageInfo, ImageLoadingListener lis) {
		NewsImageInfo newsImageInfo = newsCameraImageInfo.getImageinfo();
		ImageLoader.getInstance().loadImage(newsImageInfo.getPath(), lis);
	}
	/**
	 * @Title: requestNetwork
	 * @Description: 请求网络
	 * @param: @param groupid 组id
	 * @param: @return
	 * @return: String
	 * @throws WSError 
	 * @throws:
	 */
	public static String requestNetwork(String groupid) throws WSError {
		String response = null;
		String url = AppProperty.getInstance().VVAPI + "/get_camera_materials";
		String[] names = new String[] { "tm_id", "group_id", "session_id" };
		String jid = "0", sessionId = "0";
		try {
			jid = LoginManager.getInstance().getJidParsed();
			sessionId = LoginManager.getInstance().getSessionId();
		} catch (IllegalStateException e) {
		}
		Object[] values = new Object[] { jid, groupid, sessionId };
		response = Caller.doGet(url, names, values);
		return response;
	}
}
