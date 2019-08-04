package com.butterfly.vv.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.VVPacketAdapter;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.Comment;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.upload.AppachUpload;
import com.butterfly.vv.upload.NetParamter;
import com.butterfly.vv.upload.NetParamter.NetParamterType;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

import de.greenrobot.event.EventBus;

/**
 * @func 时光业务类
 * @author yuedong bao
 * @time 2014-12-27 上午11:23:06
 */
public class TimeflyService {
	/**
	 * 单例模式获取上传类
	 * @return
	 */
	private static class TimeflyServiceHolder {
		public static TimeflyService instance = new TimeflyService();
	}

	public static TimeflyService getInstance() {
		return TimeflyServiceHolder.instance;
	}

	private static AppachUpload vvAppachUpload = new AppachUpload("");

	/**
	 * 上传图片到服务器
	 * @param authority 权限
	 * @param lon 经度
	 * @param lat 纬度
	 * @param pathToImage 图片所在路径
	 * @param gid 图片文件夹ID
	 * @param gid_create_time 图片创建时间
	 * @return
	 */
	public static Map<String, Object> uploadPicture(String authority,
			String lon, String lat, String pathToImage, String gid,
			String gid_create_time) {
		String galleryUrl = AppProperty.getInstance().VVAPI + "/upload_img";
		NetParamter[] nps = null;
		if (TextUtils.isEmpty(gid) || TextUtils.isEmpty(gid_create_time)) {
			nps = new NetParamter[] {
					new NetParamter("tm_id", LoginManager.getInstance()
							.getJidParsed()),
					new NetParamter("session_id", LoginManager.getInstance()
							.getSessionId()),
					new NetParamter("authority", authority),
					new NetParamter("lon", lon),
					new NetParamter("lat", lat),
					new NetParamter("photo_file", pathToImage,
							NetParamterType.type_file, true), };
		} else {
			nps = new NetParamter[] {
					new NetParamter("tm_id", LoginManager.getInstance()
							.getJidParsed()),
					new NetParamter("session_id", LoginManager.getInstance()
							.getSessionId()),
					new NetParamter("gid", gid),
					new NetParamter("gid_create_time", gid_create_time),
					new NetParamter("authority", authority),
					new NetParamter("lon", lon),
					new NetParamter("lat", lat),
					new NetParamter("photo_file", pathToImage,
							NetParamterType.type_file, true), };
		}
		try {
			return vvAppachUpload.sendPostImageToGallery2(galleryUrl, nps);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @func 获取时光图片组列表
	 * @param owner_tm_id 查询的时光id
	 * @param session_id 会话id
	 * @param nowTime 查询的现在时间
	 * @return
	 */
	public static Map<String, ImageFolder> getPhotoGroupList(
			final String owner_tm_id, String nowTime, boolean... isLoadLocal) {
		final Comparator<String> timeDescendComp = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		};
		final Map<String, ImageFolder> yearMap = new TreeMap<String, ImageFolder>(
				timeDescendComp);
		final String url = AppProperty.getInstance().VVAPI + "/get_photogroups";
		int prevTime = 2015;
		String[] nowTimes = nowTime.split("-");
		int yearNow = Integer.valueOf(nowTimes[0]);
		int monthNow = Integer.valueOf(nowTimes[1]);
		if (!BeemApplication.isNetworkOk()) {
			Map<String, ImageFolder> privateMap = new TreeMap<String, ImageFolder>(
					timeDescendComp);
			Map<String, ImageFolder> friendMap = new TreeMap<String, ImageFolder>(
					timeDescendComp);
			Map<String, ImageFolder> publicMap = new TreeMap<String, ImageFolder>(
					timeDescendComp);
			List<ImageFolder> folderList = ImageFolder.queryAll(owner_tm_id);
			if (folderList != null) {
				String createTime, authority;
				for (ImageFolder folder : folderList) {
					createTime = folder.getCreateTime();
					authority = folder.getAuthority();
					int authorityInt = Integer.parseInt(authority);
					int authority_all = Integer.parseInt(AlbumAuthority.all);
					int authority_friend = Integer
							.parseInt(AlbumAuthority.friend);
					if ((authorityInt & authority_all) == authority_all) {
						publicMap.put(createTime, folder);
					} else if ((authorityInt & authority_friend) == authority_friend) {
						friendMap.put(createTime, folder);
					} else {
						privateMap.put(createTime, folder);
					}
					// 对yearMap加锁
					if (LoginManager.getInstance().isMyJid(owner_tm_id)) {
						// 如果是自己，都可以查看
						yearMap.putAll(publicMap);
						yearMap.putAll(friendMap);
						yearMap.putAll(privateMap);
					} else if (ContactService.getInstance().friendYet(
							owner_tm_id)) {
						// 好友则去除私有的
						yearMap.putAll(publicMap);
						yearMap.putAll(friendMap);
					} else {
						if (publicMap.size() >= 3) {
							yearMap.putAll(publicMap);
						} else {
							yearMap.putAll(publicMap);
							int numSupplyment = 3 - yearMap.size();
							Object[] keys = friendMap.keySet().toArray();
							Object[] values2 = friendMap.values().toArray();
							for (int i = 0; i < Math.min(numSupplyment,
									friendMap.size()); i++) {
								yearMap.put((String) keys[i],
										(ImageFolder) values2[i]);
							}
						}
					}
				}
			}
		} else {
			final int count = Math.max((yearNow - prevTime) * 12 + monthNow, 0);
			List<Runnable> dividedTasks = new ArrayList<Runnable>(count);
			int month = monthNow;
			for (int year = yearNow; year >= prevTime; year--) {
				for (; month >= 1; month--) {
					final int curYear = year;
					final int curMonth = month;
					dividedTasks.add(new Runnable() {
						String names[] = new String[] { "tm_id", "owner_tm_id",
								"session_id", "start_time", "end_time" };
						String values[] = new String[] {
								LoginManager.getInstance().getJidParsed(),
								owner_tm_id,
								LoginManager.getInstance().getSessionId(),
								new StringBuffer()
										.append(curYear)
										.append("-")
										.append(BBSUtils.padStrBefore(
												String.valueOf(curMonth), '0',
												2)).append("-01").toString(),
								new StringBuffer()
										.append(curYear)
										.append("-")
										.append(BBSUtils.padStrBefore(
												String.valueOf(curMonth), '0',
												2)).append("-31").toString() };

						@Override
						public void run() {
							try {
								Map<String, ImageFolder> privateMap = new TreeMap<String, ImageFolder>(
										timeDescendComp);
								Map<String, ImageFolder> friendMap = new TreeMap<String, ImageFolder>(
										timeDescendComp);
								Map<String, ImageFolder> publicMap = new TreeMap<String, ImageFolder>(
										timeDescendComp);
								String response = Caller.doGet(url, names,
										values);
								JSONObject response_json = new JSONObject(
										response);
								try {
									JSONArray data_jsonArr = response_json
											.getJSONArray("data");
									if (data_jsonArr != null) {
										String createTime, authority;
										int photo_count;
										for (int i = 0; i < data_jsonArr
												.length(); i++) {
											JSONObject dataItem_json = data_jsonArr
													.getJSONObject(i);
											createTime = dataItem_json
													.getString("create_time");
											photo_count = Integer.parseInt(dataItem_json
													.getString("photo_count"));
											authority = dataItem_json
													.getString("authority");
											ImageFolder folder = new ImageFolder();
											folder.setField(DBKey.gid,
													dataItem_json
															.getString("gid"));
											folder.setField(DBKey.createTime,
													createTime);
											folder.setField(DBKey.jid,
													owner_tm_id);
											folder.setField(DBKey.photoCount,
													photo_count);
											folder.setField(DBKey.authority,
													authority);
											//											folder.setField(DBKey.album_url, dataItem_json.getString("album_url"));
											folder.saveToDatabaseAsync();
											int authorityInt = Integer
													.parseInt(authority);
											int authority_all = Integer
													.parseInt(AlbumAuthority.all);
											int authority_friend = Integer
													.parseInt(AlbumAuthority.friend);
											if ((authorityInt & authority_all) == authority_all) {
												publicMap.put(createTime,
														folder);
											} else if ((authorityInt & authority_friend) == authority_friend) {
												friendMap.put(createTime,
														folder);
											} else {
												privateMap.put(createTime,
														folder);
											}
										}
									}
									// 对yearMap加锁
									synchronized (yearMap) {
										if (LoginManager.getInstance().isMyJid(
												owner_tm_id)) {
											// 如果是自己，都可以查看
											yearMap.putAll(publicMap);
											yearMap.putAll(friendMap);
											yearMap.putAll(privateMap);
										} else if (ContactService.getInstance()
												.friendYet(owner_tm_id)) {
											// 好友则去除私有的
											yearMap.putAll(publicMap);
											yearMap.putAll(friendMap);
										} else {
											if (publicMap.size() >= 3) {
												yearMap.putAll(publicMap);
											} else {
												yearMap.putAll(publicMap);
												int numSupplyment = 3 - yearMap
														.size();
												Object[] keys = friendMap
														.keySet().toArray();
												Object[] values2 = friendMap
														.values().toArray();
												for (int i = 0; i < Math.min(
														numSupplyment,
														friendMap.size()); i++) {
													yearMap.put(
															(String) keys[i],
															(ImageFolder) values2[i]);
												}
											}
										}
									}
									//LogUtils.i("publicMap.size:" + publicMap.size() + " friendMap.size:"
									//											+ friendMap.size() + " privateMap_size:" + privateMap.size());
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} catch (WSError e) {
								e.printStackTrace();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
				}
				month = 12;
			}
			ThreadUtils.executeDivideTasks(dividedTasks);
		}
		//LogUtils.i("getPhotoList_cost_time:" + (System.currentTimeMillis() - t1) + " yearMap.size:" + yearMap.size());
		return yearMap;
	}
	/**
	 * @func 点赞图片组
	 * @param ownerJid 图片组所有者jid
	 * @param gid 图片组id
	 * @param create_time 创建时间
	 * @return
	 */
	private static Map<String, Object> thumbUpPhotogroup( IXmppFacade mDlgXmppFacade,String ownerJid,
			String gid, String create_time) {
		Map<String, Object> retVal = new HashMap<String, Object>();
//		ownerJid = VVXMPPUtils.makeJidParsed(ownerJid);
		Message msg = likePhotoGroup(mDlgXmppFacade, ownerJid, gid,
				create_time);
		
		if (msg == null || msg.getType() == Message.Type.error) {
			retVal.put("result", "-1");
			if (msg.getError() != null) retVal.put("description", "点赞失败");
		}else{
			retVal.put("result", "0");
			retVal.put("description", "success");
			
		}
		return retVal;
	}
	/** 执行图片组点赞
	 * 
	 * @param context
	 * @param folder
	 * @param lis
	 */
	public static void executeThumbUp(Context context,
			final ImageFolder folder,
			final onPacketResult<Map<String, Object>> lis) {
		if (LikedPhotoGroup.isLiked(folder.getGid(), folder.getCreateTime(), folder.getJid())) {
			CToast.showToast(BeemApplication.getContext(), R.string.like_error_double,
					Toast.LENGTH_SHORT);
			return;
		}
		
		new VVBaseLoadingDlg<Map<String, Object>>(new VVBaseLoadingDlgCfg(
				context).setTimeOut(1000).setShowTimeOutPromp(false).setBindXmpp(true)) {
			@Override
			protected Map<String, Object> doInBackground() {
 				return thumbUpPhotogroup(mDlgXmppFacade, VVXMPPUtils.makeJidParsed(folder.getJid()), folder.getGid(),
						BBSUtils.getShortGidCreatTime(folder.getCreateTime()));
			}
			@Override
			protected void onPostExecute(Map<String, Object> result) {
				super.onPostExecute(result);
				if (result == null || !JsonParseUtils.getResult(result)) {
					LogUtils.e("thumb_up failed:result:" + result);
					CToast.showToast(BeemApplication.getContext(), R.string.like_error_failed,
							Toast.LENGTH_SHORT);
				} else {
					if (lis != null) {
						lis.onResult(null, false, null);
					}
					/*CToast.showToast(BeemApplication.getContext(), R.string.like_success,
							Toast.LENGTH_SHORT);*/
				}
			}
		}.execute();
		// 点赞成功保存数据库
		ImageFolder dbEditor = new ImageFolder();
		dbEditor.setField(DBKey.jid, folder.getJid());
		dbEditor.setField(DBKey.gid, folder.getGid());
		dbEditor.setField(DBKey.createTime, folder.getCreateTime());
		dbEditor.setField(DBKey.thumbupCount, folder.getThumbupCount() + 1);
		dbEditor.setField(DBKey.isThumbup, true);
		dbEditor.saveToDatabaseAsync();
	}
	// 配置年月日map
	@SuppressWarnings("unchecked")
	public static void setYM(String year, String month, String day,
			String picNum, Map<String, Object> yearMap) {
		Map<String, Object> monthMap = (Map<String, Object>) yearMap.get(year);
		if (monthMap == null) {
			monthMap = new TreeMap<String, Object>(new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					// 降序
					return rhs.compareTo(lhs);
				}
			});
			yearMap.put(year, monthMap);
		}
		Map<String, Object> dayMap = (Map<String, Object>) monthMap.get(month);
		if (dayMap == null) {
			dayMap = new TreeMap<String, Object>(new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					// 降序
					return rhs.compareTo(lhs);
				}
			});
			monthMap.put(month, dayMap);
		}
		if (Integer.parseInt(picNum) == 0) {
			// 移除图片数为0的日
			dayMap.remove(day);
		} else {
			dayMap.put(day, picNum);
		}
		// 如果daymap为空,删除monthMap中这个月的
		if (dayMap.isEmpty()) {
			monthMap.remove(month);
		}
		// 如果monthMap为空,删除yearMap中这一年的
		if (monthMap.isEmpty()) {
			yearMap.remove(year);
		}
	}
	/**
	 * @Title: getPhotoGroupDetail
	 * @Description: 获取jid的gid的图片组详情
	 * @param jid 查询对方的jid
	 * @param gid 图片组id
	 * @param createTime 图片组创建时间
	 * @return: ImageFolderItem
	 */
	public static ImageFolderItem getPhotoGroupDetail(String jid, String gid,
			String createTime, boolean isGetOff) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		ImageFolderItem folderItem = null;
		if (!BeemApplication.isNetworkOk()) {
			ImageFolder folderDB = ImageFolder.queryForFirst(jid, gid,
					createTime);
			if (folderDB == null) {
				return null;
			}
			folderItem = new ImageFolderItem();
			ArrayList<VVImage> vvImage = new ArrayList<VVImage>(
					VVImage.queryAll(jid, gid, createTime));
			List<CommentItem> comments = TimeflyService.getPhotoGroupComments(
					jid, gid, createTime, new Start(null), 4, isGetOff);
			Contact c = ContactService.getInstance().getContact(jid, true, false);
			if (c == null) {
				return null;
			}
			folderItem.setContact(c);
			folderItem.setImageFolder(folderDB);
			folderItem.setVVImages(vvImage);
			folderItem.addComments(comments);
		} else {
			String url = AppProperty.getInstance().VVAPI
					+ "/get_photogroup_detail";
			String[] names = new String[] { "tm_id", "owner_tm_id", "gid",
					"create_time", "session_id","version" };
			Object[] values = new Object[] {
					LoginManager.getInstance().getJidParsed(), jid, gid,
					createTime, LoginManager.getInstance().getSessionId(),BBSUtils.getVersionName() };
			try {
				String response = Caller.doGet(url, names, values);
				try {
					JSONObject responseJObject = new JSONObject(response);
					JSONArray data = responseJObject.getJSONArray("data");
					if (data != null && data.length() > 0) {
						JSONObject dataItem = data.getJSONObject(0);
						if (dataItem != null) {
							folderItem = new ImageFolderItem();
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
							folderDB.setField(DBKey.album_url,
									dataItem.getString("album_url"));
							folderDB.setField(DBKey.jid, jid);
							ArrayList<VVImage> vvImage = CommonService
									.parseImage(dataItem.getJSONArray("photo"),
											gid, createTime, jid);
							folderDB.setField(DBKey.photoCount, vvImage.size());
							folderDB.saveToDatabaseAsync();
							ArrayList<CommentItem> comments = CommonService
									.parseComment(
											dataItem.getJSONArray("comment"),
											gid, createTime, jid);
							folderItem.setContact(ContactService.getInstance()
									.getContact(jid));
							folderItem.setImageFolder(folderDB);
							folderItem.setVVImages(vvImage);
							folderItem.addComments(comments);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (WSError e) {
				e.printStackTrace();
			}
		}
		return folderItem;
	}
	/**
	 * 设置图片组权限以及签名
	 * @param jid 用户id
	 * @param gid 图片组id
	 * @param creat_time 创建时间
	 * @param signature 签名
	 * @param authority 权限
	 * @return
	 * @throws WSError 
	 */
	public static Map<String, Object> managePhotogroup(String jid, String gid,
			String creat_time, String signature, String authority) throws WSError {
		String url = AppProperty.getInstance().VVAPI + "/manage_photogroup";
		String[] names = new String[] { "tm_id", "gid", "create_time",
				"signature", "authority", "session_id" };
		String[] values = new String[] { StringUtils.parseName(jid), gid,
				creat_time, signature, authority,
				LoginManager.getInstance().getSessionId() };
			Map<String, Object> retVal = JsonParseUtils.parseToMap(Caller
					.doPost(url, names, values));
			if (JsonParseUtils.getResult(retVal)) {
				ImageFolder folderDB = new ImageFolder();
				folderDB.setField(DBKey.signature, signature);
				folderDB.setField(DBKey.gid, gid);
				folderDB.setField(DBKey.createTime, creat_time);
				folderDB.setField(DBKey.authority, authority);
				folderDB.setField(DBKey.jid, LoginManager.getInstance()
						.getJidParsed());
				folderDB.saveToDatabaseAsync();
			}
			return retVal;
	}
	/**
	 * 删除图片
	 * @param jid 用户jid
	 * @param pid 图片id
	 * @param photoCreateTime 图片创建时间
	 * @param photoGroupId 图片组id
	 * @return
	 * @throws WSError 
	 */
	public static Map<String, Object> deletePhoto(String jid, String pid,
			String photoCreateTime, String photoGroupId) throws WSError {
		Map<String, Object> retVal = null;
		String url = AppProperty.getInstance().VVAPI + "/delete_img";
		String names[] = new String[] { "tm_id", "session_id", "pid",
				"gid_create_time", "gid" };
		String values[] = new String[] { StringUtils.parseName(jid),
				LoginManager.getInstance().getSessionId(), pid,
				photoCreateTime, photoGroupId };
		retVal =  JsonParseUtils.parseToMap(Caller.doPost(url, names, values));
		return retVal;
	}
	/**
	 * 获取图片组评论
	 * @param jid
	 * @param gid
	 * @param create_time
	 * @param start
	 * @param limit
	 * @return
	 */
	public static List<CommentItem> getPhotoGroupComments(String jid,
			String gid, String create_time, Start start, int limit,
			boolean isGetOff) {
//		long prevMills = System.currentTimeMillis();
		List<CommentItem> parseComment = new ArrayList<CommentItem>();
		if (isGetOff) {
			for (Comment comment : Comment.queryAll(jid, gid, create_time,
					limit, start.getVal())) {
				CommentItem commentItem = new CommentItem();
				commentItem.setComent(comment);
				commentItem.setCommentContact(ContactService.getInstance()
						.getContact(comment.getJidComment(), true, false));
				commentItem.setCommentedContact(ContactService.getInstance()
						.getContact(comment.getJidCommented(), true, false));
				parseComment.add(commentItem);
			}
			//LogUtils.i("getPhotoGroupComments_offline_rst:" + parseComment);
		} else {
			String url = AppProperty.getInstance().VVAPI
					+ "/get_photogroup_comments";
			String names[];
			Object values[];
			jid = VVXMPPUtils.makeJidParsed(jid);
			if (TextUtils.isEmpty(start.getVal())) {
				names = new String[] { "tm_id", "owner_tm_id", "session_id",
						"gid", "gid_create_time", "limit" };
				values = new Object[] {
						LoginManager.getInstance().getJidParsed(),
						VVXMPPUtils.makeJidParsed(jid),
						LoginManager.getInstance().getSessionId(), gid,
						create_time, limit };
			} else {
				names = new String[] { "tm_id", "owner_tm_id", "session_id",
						"gid", "gid_create_time", "start", "limit" };
				values = new Object[] {
						LoginManager.getInstance().getJidParsed(),
						VVXMPPUtils.makeJidParsed(jid),
						LoginManager.getInstance().getSessionId(), gid,
						create_time, start.getVal(), limit };
			}
			try {
				String response = Caller.doGet(url, names, values);
				//LogUtils.i("getPhotoGroupComments_cgi_costMills:" + (System.currentTimeMillis() - prevMills));
				JSONObject response_json = new JSONObject(response);
				if (response_json.has("data")) {
					parseComment.addAll(CommonService.parseComment(
							response_json.getJSONArray("data"), gid,
							create_time, jid));
					String next = (String) response_json.get("next");
					start.setVal(next);
				}
			} catch (WSError e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		//LogUtils.i("getPhotoGroupComments_total_costMills:" + (System.currentTimeMillis() - prevMills));
		return parseComment;
	}
	/**
	 * 评论图片组
	 * @param facade
	 * @param pgJid 评论图片组拥有者的时光号
	 * @param gid 评论图片组的id
	 * @param gid_create_time 评论图片组创建时间
	 * @param body 评论内容
	 * @param to_cid 被评论的评论id
	 * @param commentedJid 被评论人的时光号
	 * @return
	 */
	private static Message commentPhotoGroup(IXmppFacade facade, String pgJid,
			String gid, String gid_create_time, String body, String to_cid,
			String commentedJid) {
		Message commentMsg = new Message();
		commentMsg.setTo(VVXMPPUtils.makeJidCompleted(commentedJid));
		commentMsg.setType(Type.comment);
		commentMsg.getComment().gid = gid;
		commentMsg.getComment().jid = VVXMPPUtils.makeJidCompleted(pgJid);
		commentMsg.getComment().to_cid = to_cid;
		commentMsg.getComment().gid_create_time = gid_create_time;
		commentMsg.setBody(body);
		return (Message) VVPacketAdapter.collectVVPacket(facade, commentMsg);
	}
	
	/**
	 * @func 点赞图片组
	 * @param facade
	 * @param pgJid 评论图片组拥有者的时光号
	 * @param gid 评论图片组的id
	 * @param gid_create_time 评论图片组创建时间
	 * @param body 评论内容
	 * @param to_cid 被评论的评论id
	 * @param commentedJid 被评论人的时光号
	 * @return
	 */
	private static Message likePhotoGroup(IXmppFacade facade, String pgJid,
			String gid, String gid_create_time) {
		Message commentMsg = new Message();
		commentMsg.setTo(VVXMPPUtils.makeJidCompleted(pgJid));
		commentMsg.setType(Type.like);
		commentMsg.getComment().gid = gid;
		commentMsg.getComment().jid = pgJid;
		commentMsg.getComment().gid_create_time = gid_create_time;
		commentMsg.setBody("");
		return (Message) VVPacketAdapter.collectVVPacket(facade, commentMsg);
	}
	
	/** 执行图片组评论
	 * @param mContext
	 * @param pgJid
	 * @param gid
	 * @param gid_create_time
	 * @param body
	 * @param to_cid
	 * @param commentedContact
	 * @param listener
	 */
	public static void executeCommentPG(Context mContext, String pgJid,
			String gid, String gid_create_time, String body, String to_cid,
			Contact commentedContact, onPacketResult<CommentItem> listener) {
		if (TextUtils.isEmpty(body)) {
			CToast.showToast(mContext, R.string.comment_empty_show, Toast.LENGTH_SHORT);
			return;
		}
		if (!LoginManager.getInstance().isLogined()) {
			CToast.showToast(mContext, R.string.comment_unlogin_show, Toast.LENGTH_SHORT);
			return;
		}
		new VVBaseLoadingDlg<CommentItem>(new VVBaseLoadingDlgCfg(mContext)
				.setBindXmpp(true)
				.setShowWaitingView(true)
				.setParams(pgJid, gid, gid_create_time, body, to_cid,
						commentedContact, listener)) {
			private String pgJid = (String) config.params[0];
			private String gid = (String) config.params[1];
			private String gid_create_time = (String) config.params[2];
			private String body = (String) config.params[3];
			private String to_cid = (String) config.params[4];
			private Contact commentedContact = (Contact) config.params[5];
			private onPacketResult<CommentItem> listener = (onPacketResult<CommentItem>) config.params[6];

			@Override
			protected CommentItem doInBackground() {
				Message msg = commentPhotoGroup(mDlgXmppFacade, pgJid, gid,
						gid_create_time, body, to_cid,
						commentedContact.getJIDParsed());
				CommentItem retVal = null;
				if (msg != null) {
					org.jivesoftware.smack.packet.Message.Comment msgComment = msg
							.getComment();
					String commentJid = LoginManager.getInstance()
							.getJidParsed();
					Comment comment = new Comment();
					comment.setField(DBKey.cid, msgComment.cid);
					comment.setField(DBKey.toCid, to_cid);
					comment.setField(DBKey.content, body);
					comment.setField(DBKey.commentTime, msgComment.comment_time);
					comment.setField(DBKey.gid, gid);
					comment.setField(DBKey.createTime, gid_create_time);
					comment.setField(DBKey.jid_photogroup, pgJid);
					comment.setField(DBKey.jid, commentJid);
					comment.setField(DBKey.jid_commented,
							commentedContact.getJIDParsed());
					comment.saveToDatabaseAsync();
					ImageFolder imageFolder = ImageFolder.queryForFirst(pgJid,
							gid, gid_create_time);
					imageFolder.setField(DBKey.jid, pgJid);
					imageFolder.setField(DBKey.gid, gid);
					imageFolder.setField(DBKey.createTime, gid_create_time);
					imageFolder.setField(DBKey.commentCount,
							imageFolder.getCommentCount() + 1);
					imageFolder.saveToDatabaseAsync();
					Contact commentContact = LoginManager.getInstance()
							.getMyContact();
					retVal = new CommentItem();
					retVal.setComent(comment);
					retVal.setCommentContact(commentContact);
					retVal.setCommentedContact(commentedContact);
				}
				return retVal;
			}
			@Override
			protected void onPostExecute(CommentItem result) {
				if (result != null) {
					if (listener != null) {
						listener.onResult(result, false, null);
					}
					// 发送一个EventBus给其他模块刷新
					PraiseEventBusData praisedata = new PraiseEventBusData(
							pgJid, gid, gid_create_time, result);
					EventBusData data = new EventBusData(
							EventAction.ShareCommentChange, praisedata);
					EventBus.getDefault().post(data);
				} else {
					CToast.showToast(BeemApplication.getContext(), R.string.comment_failed,
							Toast.LENGTH_SHORT);
				}
			}
			@Override
			protected void onTimeOut() {
				if (listener != null) {
					listener.onResult(null, true, null);
				}
				CToast.showToast(BeemApplication.getContext(), R.string.comment_timeout,
						Toast.LENGTH_SHORT);
			}
		}.execute();
	}
	
	/**
	 * 获取用于所有的图片组提醒
	 * @param signature
	 * @param create_time
	 * @return
	 */
	public static void getPhotoGroupNotify() {
		if (!LoginManager.getInstance().isLogined()) {
			// 离线
		} else {
			// 在线
			String url = AppProperty.getInstance().VVAPI
					+ "/get_photogroup_notify";
			String names[] = new String[] { "tm_id", "session_id" };
			String values[] = new String[] {
					LoginManager.getInstance().getJidParsed(),
					LoginManager.getInstance().getSessionId() };
			try {
				String responce = Caller.doGet(url, names, values);
				JSONObject responce_json = new JSONObject(responce);
				JSONArray data_jsonArr = responce_json.getJSONArray("data");
				for (int i = 0; i < data_jsonArr.length(); i++) {
					JSONObject dataItem_json = data_jsonArr.getJSONObject(i);
					ImageFolderNotify notifyDB = new ImageFolderNotify();
					notifyDB.setField(DBKey.gid, dataItem_json.getString("gid"));
					notifyDB.setField(DBKey.createTime,
							dataItem_json.getString("gid_create_time"));
					notifyDB.setField(DBKey.jid, LoginManager.getInstance()
							.getJidParsed());
					notifyDB.setField(DBKey.notify_time,
							dataItem_json.getString("mention_time"));
					notifyDB.setField(DBKey.notify_valid,
							dataItem_json.getString("valid"));
					notifyDB.saveToDatabaseAsync();
				}
			} catch (WSError e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 设置图片组提醒
	 * @param gid
	 * @param create_time
	 * @param notify_time
	 * @param valid
	 * @return
	 */
	public static Map<String, Object> setPhotoGroupNotify(String gid,
			String create_time, String notify_time, Valid valid) {
		String url = AppProperty.getInstance().VVAPI + "/set_photogroup_notify";
		String names[] = new String[] { "tm_id", "session_id", "gid",
				"create_time", "notify_time", "valid" };
		String values[] = new String[] {
				LoginManager.getInstance().getJidParsed(),
				LoginManager.getInstance().getSessionId(), gid, create_time,
				notify_time, valid.val };
		try {
			Map<String, Object> retVal = JsonParseUtils.parseToMap(Caller
					.doPost(url, names, values));
			if (JsonParseUtils.getResult(retVal)) {
				// 修改成功后保存notify
				ImageFolderNotify notifyDB = new ImageFolderNotify();
				notifyDB.setField(DBKey.gid, gid);
				notifyDB.setField(DBKey.createTime, create_time);
				notifyDB.setField(DBKey.jid, LoginManager.getInstance()
						.getJidParsed());
				notifyDB.setField(DBKey.notify_time, notify_time);
				notifyDB.setField(DBKey.notify_valid, valid.val);
				notifyDB.saveToDatabaseAsync();
			}
			return retVal;
		} catch (WSError e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 本地时光提醒
	 */
	public static boolean setLocalPhotoGroupNotify(String gid, String jid,
			String create_time, String notify_time, Valid valid,
			String signature, String authority) {
		ImageFolder folderDB = new ImageFolder();
		folderDB.setField(DBKey.signature, signature);
		folderDB.setField(DBKey.gid, gid);
		folderDB.setField(DBKey.createTime, create_time);
		folderDB.setField(DBKey.authority, authority);
		folderDB.setField(DBKey.jid, LoginManager.getInstance().getJidParsed());
		folderDB.saveToDatabaseAsync();
		// 修改成功后保存notify
		ImageFolderNotify notifyDB = new ImageFolderNotify();
		notifyDB.setField(DBKey.gid, gid);
		notifyDB.setField(DBKey.createTime, create_time);
		notifyDB.setField(DBKey.jid, LoginManager.getInstance().getJidParsed());
		notifyDB.setField(DBKey.notify_time, notify_time);
		notifyDB.setField(DBKey.notify_valid, valid.val);
		notifyDB.saveToDatabaseAsync();
		return true;
	}
	/**
	 *  获取limit条本地离线数据
	 * @param query_jid :查询的时光号
	 * @param owner_tm_id ：拥有者的时光号
	 * @param limit
	 * @return: List<ImageFolderItem>
	 */
	public static List<ImageFolderItem> getSomeOfflineImageFolderItem(
			String owner_tm_id, int limit) {
		List<ImageFolderItem> retVal = new ArrayList<ImageFolderItem>();
		// 查出来并且判断权限
		List<ImageFolder> folderList = ImageFolder.queryAll(owner_tm_id);
		Comparator<String> timeDescendComp = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o2.compareTo(o1);
			}
		};
		Map<String, ImageFolder> yearMap = new TreeMap<String, ImageFolder>(
				timeDescendComp);
		Map<String, ImageFolder> privateMap = new TreeMap<String, ImageFolder>(
				timeDescendComp);
		Map<String, ImageFolder> friendMap = new TreeMap<String, ImageFolder>(
				timeDescendComp);
		Map<String, ImageFolder> publicMap = new TreeMap<String, ImageFolder>(
				timeDescendComp);
		if (folderList != null) {
			String createTime, authority;
			for (ImageFolder folder : folderList) {
				createTime = folder.getCreateTime();
				authority = folder.getAuthority();
				int authorityInt = Integer.parseInt(authority);
				int authority_all = Integer.parseInt(AlbumAuthority.all);
				int authority_friend = Integer.parseInt(AlbumAuthority.friend);
				if ((authorityInt & authority_all) == authority_all) {
					publicMap.put(createTime, folder);
				} else if ((authorityInt & authority_friend) == authority_friend) {
					friendMap.put(createTime, folder);
				} else {
					privateMap.put(createTime, folder);
				}
			}
		}
		if (LoginManager.getInstance().isMyJid(owner_tm_id)) {
			// 如果是自己，都可以查看
			yearMap.putAll(publicMap);
			yearMap.putAll(friendMap);
			yearMap.putAll(privateMap);
		} else if (ContactService.getInstance().friendYet(owner_tm_id)) {
			// 好友则去除私有的
			yearMap.putAll(publicMap);
			yearMap.putAll(friendMap);
		} else {
			if (publicMap.size() >= 3) {
				yearMap.putAll(publicMap);
			} else {
				yearMap.putAll(publicMap);
				int numSupplyment = 3 - yearMap.size();
				Object[] keys = friendMap.keySet().toArray();
				Object[] values = friendMap.values().toArray();
				for (int i = 0; i < Math.min(numSupplyment, friendMap.size()); i++) {
					yearMap.put((String) keys[i], (ImageFolder) values[i]);
				}
			}
		}
		//LogUtils.i("publicMap.size:" + publicMap.size() + " friendMap.size:" + friendMap.size() + " privateMap_size:"
		//				+ privateMap.size());
		for (String createTimeKey : yearMap.keySet()) {
			if (retVal.size() >= limit) {
				break;
			}
			ImageFolder folder = yearMap.get(createTimeKey);
			ImageFolderItem folderItem = null;
			try {
				folderItem = ImageFolderItemManager.getInstance()
						.getImageFolderItem(folder.getJid(), folder.getGid(),
								folder.getCreateTime(), true, true);
			} catch (WSError e) {
				e.printStackTrace();
			}
			if (folderItem != null) {
				retVal.add(folderItem);
			} else {
				//LogUtils.e("Error:can not find the imagefolderItem.");
			}
		}
		//LogUtils.i("retVal.size:" + retVal.size() + "ImageFolderAull:");
		return retVal;
	}

	public enum Valid {
		open("1"), close("0"), outdate("-1");
		Valid(String val) {
			this.val = val;
		}

		public String val;
	}
}
