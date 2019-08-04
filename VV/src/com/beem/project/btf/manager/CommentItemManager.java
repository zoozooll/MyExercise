package com.beem.project.btf.manager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.DataOperation;
import com.beem.project.btf.utils.AppProperty;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.model.Comment;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.CommentItemAdapter;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

/**
 * @ClassName: CommentItemManager
 * @Description:评论管理类
 * @author: yuedong bao
 * @date: 2015-4-11 下午3:52:49
 */
public class CommentItemManager {
	private static class SingletonHolder {
		private static CommentItemManager instance = new CommentItemManager();
	}

	public static CommentItemManager getInstance() {
		return SingletonHolder.instance;
	}
	public CommentItemAdapter getCommentItem(String jid, String gid,
			String createTime, int limit, String start, boolean... isReloads) throws WSError {
		CommentItemOprt oprt = new CommentItemOprt();
		oprt.setParams(CommentItem.class, jid, gid, createTime, limit, start);
		return oprt.getData(!BeemApplication.isNetworkOk(), isReloads);
	}

	private class CommentItemOprt extends DataOperation<CommentItemAdapter> {
		private String url;
		private String[] names;
		private Object[] values;
		private String jid, gid, createTime, start;
		private int limit;

		public void setParams(Class<CommentItem> cls, String jid, String gid,
				String createTime, int limit, String start) {
			this.url = AppProperty.getInstance().VVAPI
					+ "/get_photogroup_comments";
			this.jid = jid;
			this.gid = gid;
			this.createTime = createTime;
			this.limit = limit;
			this.start = start;
			if (TextUtils.isEmpty(start)) {
				names = new String[] { "tm_id", "owner_tm_id", "session_id",
						"gid", "gid_create_time", "limit" };
				values = new String[] {
						LoginManager.getInstance().getJidParsed(),
						VVXMPPUtils.makeJidParsed(jid),
						LoginManager.getInstance().getSessionId(), gid,
						createTime, String.valueOf(limit) };
			} else {
				names = new String[] { "tm_id", "owner_tm_id", "session_id",
						"gid", "gid_create_time", "start", "limit" };
				values = new String[] {
						LoginManager.getInstance().getJidParsed(),
						VVXMPPUtils.makeJidParsed(jid),
						LoginManager.getInstance().getSessionId(), gid,
						createTime, start, String.valueOf(limit) };
			}
			setParams(cls, "comment" + jid + gid + createTime + limit + start);
		}
		@Override
		protected CommentItemAdapter getDataFromNetwork() {
			CommentItemAdapter retVal = new CommentItemAdapter();
			ArrayList<CommentItem> commentItems = new ArrayList<CommentItem>();
			try {
				String response = Caller.doGet(url, names, values);
				JSONObject response_json = new JSONObject(response);
				parseComment(commentItems, response_json.getJSONArray("data"),
						gid, createTime, jid);
				retVal.setCommentItems(commentItems);
				retVal.setNextOnline(response_json.getString("next"));
				setNextOffline(commentItems, retVal);
			} catch (WSError e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return retVal;
		}
		@Override
		protected CommentItemAdapter getDataFromDB() {
			CommentItemAdapter retVal = new CommentItemAdapter();
			List<Comment> comments = Comment.queryAll(jid, gid, createTime,
					limit, start);
			ArrayList<CommentItem> commentItems = new ArrayList<CommentItem>();
			if (comments != null) {
				for (Comment comment : comments) {
					CommentItem commentItem = new CommentItem();
					commentItem.setCommentContact(ContactService.getInstance()
							.getContact(comment.getJidComment()));
					commentItem.setCommentedContact(ContactService
							.getInstance()
							.getContact(comment.getJidCommented()));
					commentItems.add(commentItem);
				}
				retVal.setCommentItems(commentItems);
			}
			setNextOffline(commentItems, retVal);
			return retVal;
		}
	}

	private void setNextOffline(List<CommentItem> commentItems,
			CommentItemAdapter commentAdapter) {
		// 保存最后一条评论
		if (commentItems.size() > 0) {
			CommentItem item = commentItems.get(commentItems.size() - 1);
			commentAdapter.setNextOffline(item.packageOfflineNext());
		}
	}
	// 解析图片组评论
	public void parseComment(List<CommentItem> commentItems,
			JSONArray commentJson, String gid, String createTime, String jid) {
		if (commentJson == null)
			return;
		for (int j = 0; j < commentJson.length(); j++) {
			try {
				JSONObject commentItemJson = commentJson.getJSONObject(j);
				CommentItem commentItem = new CommentItem();
				Comment comment = new Comment();
				Contact commentContact = new Contact();
				Contact commentedContact = new Contact();
				// 评论自己
				comment.setField(DBKey.content,
						commentItemJson.getString("content"));
				comment.setField(DBKey.gid, gid);
				comment.setField(DBKey.createTime, createTime);
				comment.setField(DBKey.jid_photogroup, jid);
				comment.setField(DBKey.cid, commentItemJson.getString("cid"));
				comment.setField(DBKey.toCid,
						commentItemJson.getString("to_cid"));
				comment.setField(DBKey.commentTime,
						commentItemJson.getString("comment_time"));
				comment.setField(DBKey.jid_commented,
						commentItemJson.getString("to_tm_id"));
				comment.setField(DBKey.jid,
						commentItemJson.getString("reply_tm_id"));
				comment.saveToDatabase();
				// 评论者自己
				commentContact = ContactService.getInstance().getContact(
						commentItemJson.getString("reply_tm_id"));
				// 被评论者
				commentedContact = ContactService.getInstance().getContact(
						commentItemJson.getString("to_tm_id"));
				commentItem.setComent(comment);
				commentItem.setCommentContact(commentContact);
				commentItem.setCommentedContact(commentedContact);
				commentItems.add(commentItem);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
