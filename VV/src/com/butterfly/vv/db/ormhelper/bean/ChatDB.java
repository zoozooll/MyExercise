package com.butterfly.vv.db.ormhelper.bean;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.service.Message;
import com.beem.project.btf.ui.activity.ChatActivity.MessageState;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder;
import com.butterfly.vv.db.ormhelper.DBHelper.DBOrder.DBOrderType;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.j256.ormlite.field.DatabaseField;

public class ChatDB extends BaseDB {
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
	// isLocal true-发送方是自己 false-发送方是对方
	@DatabaseField
	protected boolean isLocal;
	@DatabaseField
	protected MessageState msgState;
	@DatabaseField
	protected long birthTime;

	@Override
	public void saveToDatabase() {
		String jid_send = (String) getField(DBKey.jid_send);
		String jid_receive = (String) getField(DBKey.jid_receive);
		String msgtime = (String) getField(DBKey.msgtime);
		id = new StringBuffer().append(jid_send).append(jid_receive)
				.append(msgtime).toString();
		fields.put(DBKey.id.toString(), id);
		DBHelper.getInstance().saveToDatabaseSync(this,
				new DBWhere(DBKey.id, DBWhereType.eq, id));
	}
	/**
	 * @Title: querySome
	 * @Description:
	 * @param: @param jid_send 发送方的jid
	 * @param: @param jid_receive 接受方的jid
	 * @param: @param lt_msgTime 小于此时间的limit条消息
	 * @param: @param limit 取limit条数据
	 * @param: @return
	 * @return: List<ChatDB>
	 * @throws:
	 */
	public static List<Message> querySome(String jid_send, String jid_receive,
			String lt_msgTime, int limit) {
		//LogUtils.i("jid_send:" + jid_send + " jid_receive:" + jid_receive + " lt_msgTime:" + lt_msgTime + " limit:"
		//				+ limit);
		List<Message> retVal = new ArrayList<Message>();
		List<ChatDB> queryDatas = DBHelper.getInstance().queryAll(ChatDB.class,
				new DBOrder[] { new DBOrder(DBKey.msgtime, DBOrderType.desc) },
				new DBWhere(DBKey.jid_send, DBWhereType.eq, jid_send),
				new DBWhere(DBKey.jid_receive, DBWhereType.eq, jid_receive),
				new DBWhere(DBKey.msgtime, DBWhereType.lt, lt_msgTime));
		//LogUtils.i("queryAll:" + queryAll(jid_send, jid_receive, -1, -1));
		if (queryDatas != null) {
			int i = 0;
			for (ChatDB chatDBOne : queryDatas) {
				if (i >= limit) {
					break;
				}
				retVal.add(0, chatDBOne.toMessage());
			}
		}
		return retVal;
	}
	public static List<ChatDB> queryAll(String jid_send, String jid_receive,
			int limit, int offset) {
		return DBHelper.getInstance().queryAll(ChatDB.class,
				new DBOrder[] { new DBOrder(DBKey.msgtime, DBOrderType.desc) },
				limit, offset,
				new DBWhere(DBKey.jid_send, DBWhereType.eq, jid_send),
				new DBWhere(DBKey.jid_receive, DBWhereType.eq, jid_receive));
	}
	public Message toMessage() {
		Message message = null;
		if (isLocal) {
			message = new Message(jid_send);
			message.setFrom(jid_receive);// 发送方是自己
		} else {
			message = new Message(jid_receive);
			message.setFrom(jid_send);// 发送方是对方
		}
		message.setType(Message.MSG_TYPE_CHAT);
		message.setSubType(subType);
		message.setBody(content);
		message.setMsgState(msgState);
		message.setTimestampStr(msgtime);
		return message;
	}
	@Override
	public String toString() {
		return "ChatDB [id=" + id + ", jid_receive=" + jid_receive + ", type="
				+ type + ", subType=" + subType + ", jid_send=" + jid_send
				+ ", msgtime=" + msgtime + ", content=" + content
				+ ", isLocal=" + isLocal + ", msgState=" + msgState + "]";
	}
}
