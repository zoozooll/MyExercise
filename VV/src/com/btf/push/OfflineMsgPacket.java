package com.btf.push;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Comment;

import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.btf.push.base.BaseIQ;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;

public class OfflineMsgPacket extends BaseIQ {
	static final public String element = "query";
	static final public String xmlns = "offlinemsg";

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<" + element + " xmlns=\"" + xmlns + "\" ");
		buf.append(">");
		buf.append("<item");
		for (String name : fields.keySet()) {
			Object value = fields.get(name);
			buf.append(" ").append(name.toString()).append("=")
					.append("\"" + value + "\"");
		}
		buf.append("/>");
		buf.append("</" + element + ">");
		return buf.toString();
	}
	public Message toSmackMessage() {
		Message msg = new Message();
		msg.setType(Message.Type.valueOf(getFieldStr(BaseIQKey.type)));
		msg.setFrom(getFieldStr(BaseIQKey.jid));
		msg.setTime(getFieldStr(BaseIQKey.time));
		msg.setBody(getFieldStr(BaseIQKey.content));
		msg.setSubject(getFieldStr(BaseIQKey.subject));
		if (msg.getType() == Message.Type.comment) {
			msg.getComment().gid_create_time = getFieldStr(BaseIQKey.gid_create_time);
			msg.getComment().gid = getFieldStr(BaseIQKey.gid);
			msg.getComment().jid = (String) getField("gjid");
			msg.getComment().cid = getFieldStr(BaseIQKey.cid);
		}
		return msg;
	}
	public Item toItem() {
		Item item = new Item(getFieldStr(BaseIQKey.jid), null);
		item.setTimestamp(getFieldStr(BaseIQKey.time));
		item.setMessage(getFieldStr(BaseIQKey.content));
		item.setSubject(getFieldStr(BaseIQKey.subject));
		Message.Type type = Message.Type.valueOf(getFieldStr(BaseIQKey.type));
		if (type == Message.Type.comment) {
			item.setMsgtype(MsgType.comment);
			item.setGid(getFieldStr(BaseIQKey.gid));
			item.setGidJid((String) getField("gjid"));
			item.setGidCreateTime(getFieldStr(BaseIQKey.gid_create_time));
			item.setCid(getFieldStr(BaseIQKey.cid));
			Comment comment = new Comment();
			comment.cid = getFieldStr(BaseIQKey.cid);
			comment.comment_time = getFieldStr(BaseIQKey.time);
			comment.gid_create_time  = getFieldStr(BaseIQKey.gid_create_time);
			comment.gid = getFieldStr(BaseIQKey.gid);
			comment.to_cid = (String) getField("to_cid");
			comment.jid = (String) getField("jid");
			item.setComment(comment);
		} else if (type == Message.Type.chat) {
			item.setMsgtype(MsgType.chat);
			if (item.getMessage()
					.startsWith(Constants.MESSAGE_IMAGE_LINK_START)) {
				item.setMsgTypeSub(MsgTypeSub.image);
			} else if (item.getMessage().startsWith(
					Constants.MESSAGE_AUDIO_LINK_START)) {
				item.setMsgTypeSub(MsgTypeSub.audio);
			}
		} else if (type == Message.Type.html) {
			item.setMsgtype(MsgType.chat);
			item.setMsgTypeSub(MsgTypeSub.html);
		} else if (type == Message.Type.like){
			item.setMsgtype(MsgType.like);
			item.setGid(getFieldStr(BaseIQKey.gid));
			item.setGidCreateTime(getFieldStr(BaseIQKey.gid_create_time));
			item.setGidJid(LoginManager.getInstance().getJidParsed());
			item.setCid(getFieldStr(BaseIQKey.cid));
		} else {
			return null;
		}
		return item;
	}
	public com.beem.project.btf.service.Message toMessage() {
		com.beem.project.btf.service.Message msg = new com.beem.project.btf.service.Message(
				"");
		msg.setFrom(getFieldStr(BaseIQKey.jid));
		msg.setTimestampStr(getFieldStr(BaseIQKey.time));
		msg.setBody(getFieldStr(BaseIQKey.content));
		Message.Type type = Message.Type.valueOf(getFieldStr(BaseIQKey.type));
		if (type == Message.Type.comment) {
			msg.setType(com.beem.project.btf.service.Message.MSG_TYPE_COMMENT);
		} else if (type == Message.Type.chat) {
			msg.setType(com.beem.project.btf.service.Message.MSG_TYPE_CHAT);
		} else if (type == Message.Type.error) {
			msg.setType(com.beem.project.btf.service.Message.MSG_TYPE_ERROR);
		} else {
			//LogUtils.e("message type is wrong,type" + toMessage().getType(), 3);
		}
		msg.setType(type == Message.Type.comment ? com.beem.project.btf.service.Message.MSG_TYPE_COMMENT
				: com.beem.project.btf.service.Message.MSG_TYPE_CHAT);
		if (type == Message.Type.comment) {
			msg.getComment().gid_create_time = getFieldStr(BaseIQKey.gid_create_time);
			msg.getComment().gid = getFieldStr(BaseIQKey.gid);
			msg.getComment().jid = getFieldStr(BaseIQKey.jid);
			msg.getComment().cid = getFieldStr(BaseIQKey.cid);
		}
		return msg;
	}
}
