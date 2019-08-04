package com.butterfly.vv.service;

import java.util.ArrayList;
import java.util.Map;

import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.Comment;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

/**
 * @func 通用服务，处理注册，修改/找回密码
 * @author yuedong bao
 * @time 2015-1-9 上午9:21:15
 */
public class CommonService {
	private static final String tag = CommonService.class.getSimpleName();

	private CommonService() {
	}
	// 注册
	public static Map<String, Object> register(String nickName,
			String pass_md5, String token_md5, String phoneNum, String sex)
			throws WSError {
		String url = AppProperty.getInstance().VVAPI + "/sign_up";
		String pwMd5 = pass_md5;
		String token = token_md5;// phoneNum + "butterfly"
		String names[] = new String[] { "nickname", "password", "phone", "sex",
				"token" };
		String values[] = new String[] { nickName, pwMd5, phoneNum, sex, token };
		Map<String, Object> retVal = JsonParseUtils.parseToMap(Caller.doPost(
				url, names, values));
		if (JsonParseUtils.getResult(retVal)) {
			// 保存到数据库
		}
		return retVal;
	}
	// 更新
	public static String update(String url) throws WSError {
		String doGet = Caller.doGet(AppProperty.getInstance().VVAPI + url,
				new String[] { "version" }, 
				new String[] {BBSUtils.getVersionName()});
		return doGet;
	}
	// 忘记密码
	public static String forgetPassword(String str) throws WSError {
		String url = null;
		if (str.length() == 11) {
			url = AppProperty.getInstance().VVAPI
					+ AppProperty.getInstance().FORGET_PASSWORD_MOBILE + str;
		} else {
			url = AppProperty.getInstance().VVAPI
					+ AppProperty.getInstance().FORGET_PASSWORD_TMID + str;
		}
		return Caller.doGet(url);
	}
	// 重置密码
	public static String modifyPassword(String num, Verify_Type type,
			String token, String newPassword) throws WSError {
		String url = null;
		String[] names = null;
		if (num.length() == 11) {
			url = AppProperty.getInstance().VVAPI + "/modify_password_sms";
			names = new String[] { "mobile", "verify_type", "token", "password" };
		} else {
			url = AppProperty.getInstance().VVAPI + "/modify_password";
			names = new String[] { "tm_id", "verify_type", "token", "password" };
		}
		String[] values = new String[] { num, type.val, token, newPassword };
		return Caller.doPost(url, names, values);
	}
	// 修改密码
	public static Map<String, Object> modifyPassword(String jid, String token,
			String newPassword, Verify_Type type) throws WSError {
		String url = AppProperty.getInstance().VVAPI + "/modify_password";
		String names[] = new String[] { "tm_id", "verify_type", "token",
				"password" };
		String values[] = new String[] { StringUtils.parseName(jid), type.val,
				token, newPassword };
		return JsonParseUtils.parseToMap(Caller.doPost(url, names, values));
	}
	// 验证验证码
	public static String verifyVerifyCode(String str, String verfify_code)
			throws WSError {
		String[] names = null;
		String[] values = null;
		String url = null;
		if (str.length() == 11) {
			names = new String[] { "mobile", "verify_code", "flag" };
			values = new String[] { str, verfify_code, "1" };
			url = AppProperty.getInstance().VVAPI + "/verify_vcode_sms";
		} else {
			names = new String[] { "tm_id", "verify_code", "flag" };
			values = new String[] { str, verfify_code, "2" };
			url = AppProperty.getInstance().VVAPI + "/verify_vcode_tm";
		}
		return Caller.doPost(url, names, values);
	}

	// 修改密码方式：1-验证码（忘记密码) 2-旧密码(修改)
	public enum Verify_Type {
		oldPw("2"), identifyCode("1");
		String val;

		Verify_Type(String val) {
			this.val = val;
		}
	}

	// 解析图片
	public static ArrayList<VVImage> parseImage(JSONArray photo, String gid,
			String createTime, String jid) {
		ArrayList<VVImage> vvImage = new ArrayList<VVImage>();
		if (photo != null) {
			for (int j = 0; j < photo.length(); j++) {
				try {
					JSONObject photoItem = photo.getJSONObject(j);
					JSONArray photo_url = photoItem.getJSONArray("photo_url");
					VVImage image = new VVImage();
					image.setField(DBKey.pathThumb, photo_url.getString(0));
					image.setField(DBKey.path, photo_url.getString(1));
					image.setField(DBKey.gid, gid);
					image.setField(DBKey.pid, photoItem.getString("pid"));
					image.setField(DBKey.createTime, createTime);
					image.setField(DBKey.jid, jid);
					image.saveToDatabaseAsync();
					vvImage.add(image);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return vvImage;
	}
	// 解析图片组评论
	public static ArrayList<CommentItem> parseComment(JSONArray commentJson,
			String gid, String createTime, String jid) {
		if (commentJson == null)
			return null;
		ArrayList<CommentItem> commentItems = new ArrayList<CommentItem>();
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
				comment.saveToDatabaseAsync();
				// 评论者自己
				commentContact.setField(DBKey.jid,
						commentItemJson.getString("reply_tm_id"));
				commentContact.setField(DBKey.nickName,
						commentItemJson.getString("reply_nickname"));
				commentContact.setField(DBKey.sex,
						commentItemJson.getString("reply_sex"));
				commentContact.setField(DBKey.photo_small,
						commentItemJson.getString("reply_portrait_small"));
				if (!LoginManager.getInstance()
						.isMyJid(commentContact.getJid())) {
					commentContact.saveToDatabaseAsync();
				}
				// 被评论者
				commentedContact.setField(DBKey.jid,
						commentItemJson.getString("to_tm_id"));
				commentedContact.setField(DBKey.nickName,
						commentItemJson.getString("to_nickname"));
				if (!LoginManager.getInstance().isMyJid(
						commentedContact.getJid())) {
					commentedContact.saveToDatabaseAsync();
				}
				commentItem.setComent(comment);
				commentItem.setCommentContact(commentContact);
				commentItem.setCommentedContact(commentedContact);
				commentItems.add(commentItem);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return commentItems;
	}
	/**
	 * Save the user credentials.
	 * @param jid the jid of the user
	 * @param pass the password of the user
	 */
	public static void saveCredential(Context ctx, String jid, String pass) {
		SharedPrefsUtil.putValue(ctx, SettingKey.account_token, jid);
		SharedPrefsUtil.putValue(ctx, SettingKey.account_password, pass);
	}
}
