package com.butterfly.vv.db.ormhelper.bean;

import java.util.List;

import android.text.TextUtils;

import com.beem.project.btf.manager.LoginManager;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @ClassName: MessageTableDB
 * @Description: MessageTableChatDB，MessageTableCommentDB，MessageTableFriendDB共同基类 ，保存相同字段
 * @author: yuedong bao
 * @date: 2015-4-1 下午2:41:49
 */

public class MessageDB extends BaseDB {
	@DatabaseField(id = true)
	protected String id;
	@DatabaseField
	protected String jid_receive;// 接受方jid
	@DatabaseField
	protected MsgType type;
	@DatabaseField
	protected MsgTypeSub subType;
	@DatabaseField
	protected String jid_send;// 发送方jid
	@DatabaseField
	protected String msgtime;
	@DatabaseField
	protected String content;
	@DatabaseField
	protected String gidJid;
	@DatabaseField
	protected String gid;
	@DatabaseField
	protected String createTime;
	@DatabaseField
	protected int unReadMsgCount;
	@DatabaseField
	protected boolean isChecked;
	@DatabaseField
	protected long birthTime;
	@DatabaseField
	protected String cid;

	@Override
	public void saveToDatabase() {
		MsgType type = (MsgType) getField(DBKey.type);
		String jid_send = (String) getField(DBKey.jid_send);
		String jid_receive = (String) getField(DBKey.jid_receive);
		String gid = (String) getField(DBKey.gid);
		String createTime = (String) getField(DBKey.createTime);
		id = createID(jid_send, jid_receive, type, gid, createTime);
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	public static void delete(String jid_send, String jid_receive,
			MsgType type, String gid, String createTime) {
		String id = createID(jid_send, jid_receive, type, gid, createTime);
		DBHelper.getInstance().delete(MessageDB.class,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	private static String createID(String jid_send, String jid_receive,
			MsgType type, String gid, String createTime) {
		String id;
		if (type == null || TextUtils.isEmpty(jid_send)
				|| TextUtils.isEmpty(jid_receive)) {
			throw new IllegalArgumentException(
					"save MessgeDB must has the type");
		}
		if (type == MsgType.comment
				&& (TextUtils.isEmpty(gid) || TextUtils.isEmpty(createTime))) {
			throw new IllegalArgumentException(
					"save MessgeDB_comment must has the gid and createTime");
		}
		if (type == MsgType.chat) {
			id = jid_send + jid_receive + type;
		} else if (type == MsgType.friend_require) {
			id = jid_send + jid_receive + type;
		} else if (type == MsgType.comment) {
			id = jid_receive + type;
		} else if (type == MsgType.like) {
			id = jid_receive + type;
		} else {
			throw new IllegalArgumentException("error type");
		}
		return id;
	}
	
	private static String createChatID(String jid_send, String jid_receive) {
		
		return jid_send + jid_receive + MsgType.chat.toString();
	}
	public static List<MessageDB> queryAll(String jid_login, int limit) {
		List<MessageDB> retVal = DBHelper.getInstance().queryAll(
				MessageDB.class, (DBOrder) null, limit,
				new DBWhere(DBKey.jid_receive, DBWhereType.eq, jid_login));
		return retVal;
	}
	/**
	 * @Title: checkOutMessage
	 * @Description: 查阅消息
	 * @param: @param item
	 * @return: void
	 * @throws:
	 */
	public static void checkOutMessage(Item item) {
		MessageDB editor = new MessageDB();
		saveDatabaseMust(item, editor);
		editor.setField(DBKey.isChecked, true);
		editor.saveToDatabaseAsync();
	}
	/**
	 * @Title: readMessage
	 * @Description:
	 * @param: @param item
	 * @return: void
	 * @throws:
	 */
	public static void readMessage(Item item) {
		MessageDB editor = new MessageDB();
		saveDatabaseMust(item, editor);
		editor.setField(DBKey.unReadMsgCount, item.getUnReadMsgCount());
		editor.saveToDatabaseAsync();
	}
	private static void saveDatabaseMust(Item item, MessageDB editor) {
		editor.setField(DBKey.jid_send, item.getJid());
		editor.setField(DBKey.jid_receive, LoginManager.getInstance()
				.getJidParsed());
		editor.setField(DBKey.type, item.getMsgtype());
		if (item.getMsgtype() == MsgType.comment) {
			editor.setField(DBKey.gid, item.getGid());
			editor.setField(DBKey.createTime, item.getGidCreateTime());
			editor.setField(DBKey.cid, item.getCid());
			editor.setField(DBKey.gidJid, item.getGidJid());
		}
	}
	public Item toItem() {
		Item item = new Item(jid_send, null);
		item.setMsgtype(type);
		item.setGid(gid);
		item.setGidCreateTime(createTime);
		item.setMessage(content);
		item.setChecked(isChecked);
		item.setMsgTypeSub(subType);
		item.setUnReadMsgCount(unReadMsgCount);
		item.setTimestamp(msgtime);
		item.setGidJid(gidJid);
		item.setCid(cid);
		return item;
	}
	@Override
	public String toString() {
		return "MessageDB [id=" + id + ", jid_receive=" + jid_receive
				+ ", type=" + type + ", subType=" + subType + ", jid_send="
				+ jid_send + ", msgtime=" + msgtime + ", content=" + content
				+ ", gidJid=" + gidJid + ", gid=" + gid + ", createTime="
				+ createTime + ", unReadMsgCount=" + unReadMsgCount
				+ ", isChecked=" + isChecked + ", birthTime=" + birthTime
				+ ", cid=" + cid + "]";
	}
}
