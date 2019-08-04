package com.beem.project.btf.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.service.DataOperation;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.CommonService;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

/**
 * @ClassName: ImageFolderItemManager
 * @Description: 图片组管理类 数据获取策略： (1)先取url，看url是否为空或者url是否过期 .(2)url为空或者过期，则网络获取;否则从数据获取.
 * @author: yuedong bao
 * @date: 2015-4-9 下午5:32:05
 */
public class ImageFolderItemManager {
	private static ImageFolderItemManager instance = new ImageFolderItemManager();

	public static ImageFolderItemManager getInstance() {
		return instance;
	}

	public class ImageFolderOperation extends DataOperation<ImageFolderItem> {
		protected String jid, gid, createTime;
		protected String url;
		protected String[] names;
		protected Object[] values;

		private ImageFolderOperation() {
			super();
		}
		public ImageFolderOperation setParams(Class<ImageFolderItem> cls,
				String jid, String gid, String createTime) {
			this.jid = VVXMPPUtils.makeJidParsed(jid);
			this.gid = gid;
			this.createTime = createTime;
			url = AppProperty.getInstance().VVAPI + "/get_photogroup_detail";
			names = new String[] { "tm_id", "owner_tm_id", "gid",
					"create_time", "session_id", "version"};
			values = new Object[] { LoginManager.getInstance().getJidParsed(),
					jid, gid, createTime,
					LoginManager.getInstance().getSessionId(), BBSUtils.getVersionName()};
			setParams(cls, "photo_detail" + jid + gid + createTime);
			return this;
		}
		@Override
		protected ImageFolderItem getDataFromNetwork() {
			ImageFolderItem folderItem = null;
			long t1 = System.currentTimeMillis();
			try {
				String response = Caller.doGet(url, names, values);
				//				//LogUtils.i("getPhoto_detail_parseMills:" + (System.currentTimeMillis() - t1) + " jid:" + jid + " gid:"
				//						+ gid + " createTime:" + createTime);
				JSONObject responseJObject = new JSONObject(response);
				if (responseJObject.getString("result").equals(
						String.valueOf(Constants.RESULT_OK))) {
					JSONArray data = responseJObject.getJSONArray("data");
					if (data != null && data.length() > 0) {
						JSONObject dataItem = data.getJSONObject(0);
						if (dataItem != null) {
							ArrayList<VVImage> vvImage = CommonService
									.parseImage(dataItem.getJSONArray("photo"),
											gid, createTime, jid);
							ImageFolder folderDB = new ImageFolder();
							folderDB.setField(DBKey.thumbupCount, Integer
									.parseInt(dataItem.getString("thumb_up")));
							folderDB.setField(DBKey.signature,
									dataItem.getString("signature_line"));
							folderDB.setField(DBKey.commentCount, Integer
									.parseInt(dataItem
											.getString("comment_count")));
							folderDB.setField(DBKey.gid, gid);
							folderDB.setField(DBKey.createTime, createTime);
							folderDB.setField(DBKey.lat, Double
									.parseDouble(dataItem.getString("lat")));
							folderDB.setField(DBKey.lon, Double
									.parseDouble(dataItem.getString("lon")));
							folderDB.setField(DBKey.authority,
									dataItem.getString("authority"));
							folderDB.setField(DBKey.jid, jid);
							folderDB.setField(DBKey.photoCount, vvImage.size());
							if (dataItem.has("album_url"))
								folderDB.setField(DBKey.album_url,
										dataItem.getString("album_url"));
							//该图片组是否点过赞
							ImageFolder folder = ImageFolder.queryForFirst(jid,
									gid, createTime);
							folderDB.setField(DBKey.isThumbup,
									folder == null ? false : folder.isThumbup());
							folderDB.saveToDatabaseAsync();
							fillupImageFolderNotify(folderDB);
							ArrayList<CommentItem> comments = CommonService
									.parseComment(
											dataItem.getJSONArray("comment"),
											gid, createTime, jid);
							folderItem = new ImageFolderItem();
							folderItem.setContact(ContactService.getInstance()
									.getContact(jid));
							folderItem.setImageFolder(folderDB);
							folderItem.setVVImages(vvImage);
							folderItem.addComments(comments);
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (WSError e) {
				e.printStackTrace();
			}
			//			//LogUtils.i("getPhoto_detail_costMills:" + (System.currentTimeMillis() - t1) + " jid:" + jid + " gid:" + gid
			//					+ " createTime:" + createTime);
			return folderItem;
		}
		private void fillupImageFolderNotify(ImageFolder folderDB) {
			//自己的才去查询时光提醒
			if (LoginManager.getInstance().isMyJid(jid)) {
				ImageFolderNotify notify = ImageFolderNotify.queryForFirst(jid,
						gid, createTime);
				if (notify != null) {
					folderDB.setField(DBKey.notify_time,
							notify.getField(DBKey.notify_time));
					folderDB.setField(DBKey.notify_valid,
							notify.getField(DBKey.notify_valid));
				}
			}
		}
		@Override
		protected ImageFolderItem getDataFromDB() {
			long prevMills = System.currentTimeMillis();
			ArrayList<VVImage> images = (ArrayList<VVImage>) VVImage.queryAll(
					jid, gid, createTime);
			ImageFolder folder = ImageFolder
					.queryForFirst(jid, gid, createTime);
			if (LoginManager.getInstance().isMyJid(jid)) {
				fillupImageFolderNotify(folder);
			}
			ImageFolderItem folderItem = new ImageFolderItem();
			folderItem.setContact(ContactService.getInstance().getContact(jid,
					true, true));
			folderItem.setImageFolder(folder);
			folderItem.setVVImages(images);
			List<CommentItem> items = TimeflyService.getPhotoGroupComments(jid,
					gid, createTime, new Start(null), 4, true);
			folderItem.addComments(items);
			//			//LogUtils.i("get ImageFolderItem offline cost_Mills:" + (System.currentTimeMillis() - prevMills));
			return folderItem;
		}
	}

	public ImageFolderItem getImageFolderItem(String jid, String gid,
			String createTime, boolean isGetoff, boolean isReload) throws WSError {
		ImageFolderOperation imageFolderOprt = new ImageFolderOperation();
		imageFolderOprt.setParams(ImageFolderItem.class, jid, gid, createTime);
		ImageFolderItem folderItem = imageFolderOprt.getData(isGetoff, isReload);
		return folderItem;
	}
	public ImageFolderItem getImageFolderItem(String jid, String gid,
			String createTime) throws WSError {
		return getImageFolderItem(jid, gid, createTime,
				!BeemApplication.isNetworkOk(), false);
	}
	// 获取今天的图片组数据
	public ImageFolder getImageFolderNow(String jid) {
		Date nowDate = LoginManager.getInstance().getSysytemTimeDate();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String dateLike = sf.format(nowDate);
		ImageFolder folder = ImageFolder.queryForFirstLike(jid, dateLike);
		return folder;
	}
	// 获取今天的图片组数据
	public ImageFolderItem getImageFolderItemNow(String jid) throws WSError {
		ImageFolder folder = getImageFolderNow(jid);
		if (folder == null)
			return null;
		ImageFolderItem folderItem = getImageFolderItem(jid, folder.getGid(),
				folder.getCreateTime(), true, true);
		return folderItem;
	}
}
