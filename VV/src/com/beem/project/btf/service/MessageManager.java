package com.beem.project.btf.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Comment;
import org.jivesoftware.smack.packet.Message.TimeflyNotify;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.BeemService;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.aidl.IChat;
import com.beem.project.btf.service.aidl.IChatManagerListener;
import com.beem.project.btf.service.aidl.IMessageListener;
import com.beem.project.btf.ui.activity.ShareRankingActivity;
import com.beem.project.btf.ui.activity.StartActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.TimeflyDueRemindView;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.AckPacket;
import com.btf.push.AddRosterPacket;
import com.btf.push.IQAckPacket;
import com.btf.push.Item;
import com.btf.push.MessageAckPacket;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.btf.push.OfflineMsgPacket;
import com.btf.push.base.BaseIQ.BaseIQKey;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ChatDB;
import com.butterfly.vv.db.ormhelper.bean.CommentNotifies;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.LikedNotifies;
import com.butterfly.vv.db.ormhelper.bean.MessageDB;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.table.DatabaseTableConfig;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: MessageManager
 * @Description: VV消息管理类 消息有三种类型：好友请求，聊天，时光评论
 * @author: yuedong bao
 * @date: 2015-3-13 下午6:12:32
 */
public class MessageManager {
	private final BeemService service;
	private final BeemChatManager mChatManager;
	private XMPPConnection xmppConn;
	private static String prefix = StringUtils.randomString(5);
	private static long id = 0;
	// 存储[chat,friend_agree, friend_ask,comment ]类型的message
	private Map<MsgType, Map<String, Item>> allMessages = new ConcurrentHashMap<MsgType, Map<String, Item>>();
	private int unreadCommentCount;
	private int verifingFriendrequestCount;
	private int unreadLikedCount;
	public static final String CHECK_MESSAGE = "CHECK_MESSAGE";
	private List<TimeflyNotify> timeNotifies = Collections
			.synchronizedList(new ArrayList<TimeflyNotify>());

	private static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}
	public MessageManager(XMPPConnection connection, BeemService service,
			final BeemChatManager chatManagerAdapter) {
		this.xmppConn = connection;
		this.service = service;
		this.mChatManager = chatManagerAdapter;
		try {
			addFriendPacketListener();
			addPGCommentPacketListener();
			addChatAndPGCommentListener();
			addPGLikePacketListener() ;
			addTimeFlyMentionListner();
			ThreadUtils.executeTask(new Runnable() {
				@Override
				public void run() {
					loadMessageItems();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		//LogUtils.i("create MessageManager,id:" + nextID());
	}
	private void addTimeFlyMentionListner() {
		xmppConn.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				//				//LogUtils.i("receive the mention message.");
				Message message = (Message) packet;
				//if (BBSUtils.isTopActivy(service, ContactList.class.getSimpleName())) {
				// 主界面已经加载进来
				EventBusData data = new EventBusData(EventAction.TimeflyNotify,
						message.getTimeflyNotify());
				EventBus.getDefault().post(data);
				//} else {
				// 获取到时光提醒
				timeNotifies.add(message.getTimeflyNotify());
				//}
				sendAck(packet, "message", message.getXmlns());
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return Message.class.isInstance(packet)
						&& ((Message) packet).getType() == Type.mention;
			}
		});
	}
	private void addChatAndPGCommentListener() {
		// vv custom：增加离线消息[聊天+评论]
		xmppConn.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				processOfflinePacket(packet);
			}
		}, new PacketTypeFilter(OfflineMsgPacket.class));
		// 本地在线的消息
		try {
			mChatManager
					.addChatCreationListener(new IChatManagerListener.Stub() {
						@Override
						public void chatCreated(IChat chat, boolean locally)
								throws RemoteException {
							chat.addMessageListener(new IMessageListener.Stub() {
								@Override
								public void stateChanged(IChat chat)
										throws RemoteException {
								}
								@Override
								public void processMessage(IChat chat,
										com.beem.project.btf.service.Message msg)
										throws RemoteException {
									// 收到对端发来的消息
									Item item = msg.toItem(false);
									item.setUnReadMsgCount(chat
											.getUnreadMessageCount());
									sendNewMessageBroadCast( item,
											!chat.isOpen(), false);
								}
								@Override
								public void otrStateChanged(String otrState)
										throws RemoteException {
								}
								@Override
								public void interceptMessage(IChat chat,
										com.beem.project.btf.service.Message msg)
										throws RemoteException {
									// 截获本端发送的消息
									Item item = msg.toItem(true);
									item.setUnReadMsgCount(chat
											.getUnreadMessageCount());
									sendNewMessageBroadCast( item, false, true);
								}
							});
						}
					});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private void processOfflinePacket(Packet packet) {
		//LogUtils.i("processOfflinePacket:" + packet);
		OfflineMsgPacket offlinePacket = (OfflineMsgPacket) packet;
		List<OfflineMsgPacket> items = offlinePacket.getField(BaseIQKey.item,
				List.class);
		if (items != null && items.size() > 0) {
			List<Item> itemsTwo = new ArrayList<Item>(items.size());
			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i).toItem();
				if (item != null) {
					if (item.getMsgtype() == MsgType.chat) {
						ChatAdapter adapter = (ChatAdapter) mChatManager
								.createChat(item.getJid(), null);
						adapter.addMessage(item, false);
					}
					itemsTwo.add(item);
				}
			}
			sendNewMessageBroadCast(itemsTwo, true, false);
		}
		sendIQAck(packet, OfflineMsgPacket.element, OfflineMsgPacket.xmlns);
	}
	private void addPGMessage(Message packet, String jid, boolean isOpen) {
		Item item = new Item(jid, null);
		if (packet.getType() == Type.chat) {
			item.setMsgtype(MsgType.chat);
		} else if (packet.getType() == Type.comment) {
			item.setMsgtype(MsgType.comment);
			Comment comment = packet.getComment();
			item.setComment(comment);
			item.setGidJid(comment.jid);
		} else {
			throw new IllegalArgumentException(
					"the packet's msgtype is not correct,pakcket:"
							+ packet.toXML());
		}
		item.setMessage(packet.getBody());
		item.setTimestamp(packet.getTime());
		unreadCommentCount ++;
		item.setUnReadMsgCount(unreadCommentCount);
		sendNewMessageBroadCast(item, true, false);
	}
	private void addPGLikeMessage(Message packet, String jid, boolean isOpen) {
		Item item = new Item(jid, null);
		if (packet.getType() == Type.like) {
			item.setMsgtype(MsgType.like);
			Comment coment = packet.getComment();
			item.setGid(coment.gid);
			item.setGidCreateTime(coment.gid_create_time);
			item.setGidJid(coment.jid);
		} else {
			throw new IllegalArgumentException(
					"the packet's msgtype is not correct,pakcket:"
							+ packet.toXML());
		}
		item.setMessage(packet.getBody());
		item.setTimestamp(packet.getTime());
		unreadLikedCount ++;
		item.setUnReadMsgCount(unreadLikedCount);
		sendNewMessageBroadCast(item, true, false);
	}
	private void addFriendPacketListener() {
		xmppConn.addPacketSendingListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				AddRosterPacket addRosterPacket = (AddRosterPacket) packet;
				// 同意对方的好友请求
				Item item = new Item(addRosterPacket.getField(BaseIQKey.jid,
						String.class), null);
				item.setMsgtype(MsgType.chat);
				item.setMsgTypeSub(MsgTypeSub.systemPrompt);
				ContactService.getInstance().sendFriendOptBR(
						Constants.ACTION_FRIEND_APPROVE_FRIEND, true);
				item.setMessage("您同意了TA的好友请求，现在可以开始聊天了。");
				item.setTimestamp(addRosterPacket.getField(BaseIQKey.time,
						String.class));
				ChatAdapter adapter = (ChatAdapter) mChatManager.createChat(
						item.getJid(), null);
				adapter.addMessage(item, true);
				sendNewMessageBroadCast(item, false, true);
				sendIQAck(packet, AddRosterPacket.element, AddRosterPacket.xmlns);
			}
		}, new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				if (packet instanceof AddRosterPacket) {
					AddRosterPacket addRoster = (AddRosterPacket) packet;
					return "true".equals(addRoster
							.getFieldStr(BaseIQKey.approved));
				}
				return false;
			}
		});
		xmppConn.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				try {
					List<AddRosterPacket> packets = ((AddRosterPacket) packet)
							.getField(BaseIQKey.item, List.class);
					List<Item> items = new ArrayList<Item>(packets.size());
					boolean isShowNotify = true;
					for (int i = 0; i < packets.size(); i++) {
						AddRosterPacket addRosterPacket = packets.get(i);
						Item item = new Item(addRosterPacket.getField(
								BaseIQKey.jid, String.class), null);
						String approved = addRosterPacket
								.getFieldStr(BaseIQKey.approved);
						String subscription = addRosterPacket
								.getFieldStr(BaseIQKey.subscription);
						if ("verify".equals(subscription)) {
							// 请求加好友
							item.setMsgtype(MsgType.friend_require);
							item.setMessage(addRosterPacket.getField(
									BaseIQKey.content, String.class));
							item.setTimestamp(addRosterPacket.getField(
									BaseIQKey.time, String.class));
							item.setMsgTypeSub(MsgTypeSub.friend_ask);
							verifingFriendrequestCount ++;
							item.setUnReadMsgCount(verifingFriendrequestCount);
						} else if ("result".equals(subscription)) {
							if ("true".equals(approved)) {
								// 好友同意消息也被认为时聊天消息
								item.setMsgtype(MsgType.chat);
								item.setMsgTypeSub(MsgTypeSub.systemPrompt);
								item.setMessage("TA同意了您的好友请求，现在可以开始聊天了。");
								ContactService
										.getInstance()
										.sendFriendOptBR(
												Constants.ACTION_FRIEND_APPROVED_FRIEND,
												true);
								item.setTimestamp(addRosterPacket.getField(
										BaseIQKey.time, String.class));
								ChatAdapter adapter = (ChatAdapter) mChatManager
										.createChat(item.getJid(), null);
								adapter.addMessage(item, false);
							} else {
								//								//LogUtils.i("you friend ask has be rufused:" + item);
								isShowNotify = false;
							}
						}
						items.add(item);
						LogUtils.d("addFriendPacketListener " +item + " " + isShowNotify);
					}
					if (isShowNotify) {
						sendNewMessageBroadCast(items, true, false);
					}
					sendIQAck(packet, AddRosterPacket.element,
							AddRosterPacket.xmlns);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new PacketTypeFilter(AddRosterPacket.class));
	}
	public void addMessage(Item item) {
		String key = null;
		switch (item.getMsgtype()) {
			case chat:
			{
				key = StringUtils.parseName(item.getJid());
				break;
			}
			case friend_require: 
			case comment:
			case like: {
				key = item.getMsgtype().toString();
				break;
			}
			default:
				throw new IllegalArgumentException("wrong message type:" + item);
		}
		Map<String, Item> itemMap = allMessages.get(item.getMsgtype());
		if (itemMap == null) {
			itemMap = new ConcurrentHashMap<String, Item>();
			allMessages.put(item.getMsgtype(), itemMap);
		}
		itemMap.put(key, item);
	}
	private void addPGCommentPacketListener() {
		// 在线消息监听器
		PacketFilter filter = new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof Message
						&& ((Message) packet).getType() == Type.comment;
			}
		};
		xmppConn.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				addPGMessage(message, StringUtils.parseName(message.getFrom()),
						false);
				sendAck(packet, "message", message.getXmlns());
			}
		}, filter);
	}
	
	// 点赞监听器
	private void addPGLikePacketListener() {
		// 在线消息监听器
		PacketFilter filter = new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				return packet instanceof Message
						&& ((Message) packet).getType() == Type.like;
			}
		};
		xmppConn.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				Message message = (Message) packet;
				addPGLikeMessage(message, StringUtils.parseName(message.getFrom()),
						false);
				sendAck(packet, "message", message.getXmlns());
				
			}
		}, filter);
	}
	public List<Item> getAllMessages() {
		List<Item> items = new ArrayList<Item>();
		for (Entry<MsgType, Map<String, Item>> entry : allMessages.entrySet()) {
			for (Entry<String, Item> entrySecond : entry.getValue().entrySet()) {
				items.add(entrySecond.getValue());
			}
		}
		return items;
	}
	private void sendAck(Packet fromPacket, String element, String xmlns) {
		// 给服务器一个ackpacket，表示已经收到消息
		AckPacket ackPacket = new AckPacket(fromPacket.getPacketID(), element, xmlns);
		xmppConn.sendPacket(ackPacket);
	}
	private void sendIQAck(Packet fromPacket, String element, String xmlns) {
		// 给服务器一个ackpacket，表示已经收到消息
		IQAckPacket ackPacket = new IQAckPacket(element, xmlns);
		ackPacket.setPacketID(fromPacket.getPacketID());
		xmppConn.sendPacket(ackPacket);
	}
	//显示在通知栏
	private void notifyNewMessage(Item message) {
		//LogUtils.i("notifyNewChat msgBody:" + message.getMessage());
		Builder notif = new NotificationCompat.Builder(service);
		notif.setContentIntent(makeChatIntent());
		notif.setAutoCancel(true).setWhen(System.currentTimeMillis());
		notif.setSmallIcon(R.drawable.appicon);
		// notif.setLargeIcon(BitmapFactory.decodeResource(service.getResources(),
		// R.drawable.appicon));
		MsgType msgType = message.getMsgtype();
		int noticifaction_id = BeemService.NOTIFICATION_CHAT_ID;
		Map<String, Item> map = allMessages.get(msgType);
		int unReadCount = 0;
		int unReadPerson = 0;
		for (String key : map.keySet()) {
			Item item = map.get(key);
			if (!item.isChecked()) {
				unReadCount += item.getUnReadMsgCount();
				unReadPerson++;
			}
		}
		if (unReadPerson == 1) {
			String contactJid = message.getJidParsed();
			Contact contact = ContactService.getInstance().getContact(
					contactJid);
			String suitableName = contact.getSuitableName();
			notif.setTicker(suitableName).setContentTitle(suitableName);
			notif.setNumber(message.getUnReadMsgCount());
			if (msgType == MsgType.chat) {
				notif.setContentText("消息:" + message.getMessage());
			} else if (msgType == MsgType.friend_require) {
				notif.setContentText("好友请求\"" + message.getMessage() + "\"");
			} else if (msgType == MsgType.comment) {
				notif.setContentText("图片评论:" + message.getMessage());
			} else if (msgType == MsgType.like) {
				notif.setContentText(message.getJid() + "赞了你的图片");
			}
		} else {
			notif.setContentTitle("时光机 ");
			notif.setNumber(unReadCount);
			if (msgType == MsgType.chat) {
				notif.setContentText("消息:" + unReadPerson + "个联系人发来了"
						+ unReadCount + "条未读消息");
			} else if (msgType == MsgType.friend_require) {
				notif.setContentText("好友请求\"" + unReadPerson + "个联系人请求加好友\"");
			} else if (msgType == MsgType.comment) {
				notif.setContentText("图片评论:" + unReadPerson + "个联系人评论了你的图片");
			} else if (msgType == MsgType.like) {
				notif.setContentText(unReadPerson + "个人赞评论了你的图片");
			}
		}
		switch (msgType) {
			case chat: {
				noticifaction_id = BeemService.NOTIFICATION_CHAT_ID;
				break;
			}
			case friend_require: {
				noticifaction_id = BeemService.NOTIFICATION_FRIENDASK_ID;
				break;
			}
			case comment: {
				noticifaction_id = BeemService.NOTIFICATION_COMMENT_ID;
				break;
			}
			case like: {
				noticifaction_id = BeemService.NOTIFICATION_LIKE_ID;
				break;
			}
			default:
				break;
		}
		service.sendNotificationEx(noticifaction_id, notif.getNotification());
	}
	// 当用户查看未读消息时置为true
	public void checkMessages() {
		for (Iterator<Map.Entry<MsgType, Map<String, Item>>> iterator = allMessages
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<MsgType, Map<String, Item>> entryOne = iterator.next();
			for (Iterator<Map.Entry<String, Item>> iteratorTwo = entryOne
					.getValue().entrySet().iterator(); iteratorTwo.hasNext();) {
				Item item = iteratorTwo.next().getValue();
				if (!item.isChecked()) {
					item.setChecked(true);
					// 写数据库
					MessageDB.checkOutMessage(item);
				}
			}
		}
		service.deleteNotificationAll();
	}
	// 阅读Message,将未读数目置为零,并写数据库
	public void readMessage(Item item) {
		String key = null;
		switch (item.getMsgtype()) {
			case chat:
			case friend_require: {
				key = StringUtils.parseName(item.getJid());
				break;
			}
			case comment: {
				key = item.getGidCreateTime();
				break;
			}
			default:
				throw new IllegalArgumentException("wrong message type:" + item);
		}
		Item itemMapOne = allMessages.get(item.getMsgtype()).get(key);
		if (itemMapOne != null) {
			itemMapOne.setUnReadMsgCount(0);
			MessageDB editor = new MessageDB();
			editor.setField(DBKey.jid_send, item.getJid());
			editor.setField(DBKey.jid_receive, LoginManager.getInstance()
					.getJidParsed());
			editor.setField(DBKey.type, item.getMsgtype());
			if (item.getMsgtype() == MsgType.comment || item.getMsgtype() == MsgType.like) {
				editor.setField(DBKey.gid, item.getGid());
				editor.setField(DBKey.createTime, item.getGidCreateTime());
			}
			editor.setField(DBKey.unReadMsgCount,
					itemMapOne.getUnReadMsgCount());
			editor.saveToDatabaseAsync();
		}
	}
	public void deleteItem(String jid_send, String jid_receive, MsgType type,
			String gid, String createTime) {
		int i = 0;
		for (Iterator<Map.Entry<MsgType, Map<String, Item>>> iterator = allMessages
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<MsgType, Map<String, Item>> entryOne = iterator.next();
			if (entryOne.getKey() == type) {
				for (Iterator<Map.Entry<String, Item>> iteratorTwo = entryOne
						.getValue().entrySet().iterator(); iteratorTwo
						.hasNext();) {
					Item entryTwo = iteratorTwo.next().getValue();
					boolean whereOne = entryTwo.getJid().equals(jid_send);
					boolean whereTwo = true;
					if (type == MsgType.comment || type == MsgType.like) {
						whereTwo = entryTwo.getGid().equals(gid)
								&& entryTwo.getGidCreateTime().equals(
										createTime);
					}
					if (whereOne && whereTwo) {
						iteratorTwo.remove();
						MessageDB.delete(jid_send, jid_receive, type, gid,
								createTime);
						i++;
					}
				}
				if (i > 1) {
					//LogUtils.e("deleteItem more databasedata-->jid_send:" + jid_send + " jid_receive:" + jid_receive
					//							+ " type:" + type + " gid:" + gid + " createTime:" + createTime);
				}
			}
		}
		if (i == 0) {
			//LogUtils.e("deleteItem failed--jid_send:" + jid_send + " jid_receive:" + jid_receive + " type:" + type
			//					+ " gid:" + gid + " createTime:" + createTime);
		}
	}
	/**
	 * @Title: isMessageChecked
	 * @Description: 是否已经查看了所有的消息
	 * @param: @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isMessageChecked() {
		for (Iterator<Map.Entry<MsgType, Map<String, Item>>> iterator = allMessages
				.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry<MsgType, Map<String, Item>> entryOne = iterator.next();
			for (Iterator<Map.Entry<String, Item>> iteratorTwo = entryOne
					.getValue().entrySet().iterator(); iteratorTwo.hasNext();) {
				Map.Entry<String, Item> entryTwo = iteratorTwo.next();
				if (!entryTwo.getValue().isChecked()) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * @Title: sendNewMessageBroadCast
	 * @Description: 此处发送广播给上层UI
	 * @param: @param items
	 * @param: @param isNotify 是否响铃、震动
	 * @param: @param isLocal 是否本端产生的
	 * @return: void
	 * @throws:
	 */
	public void sendNewMessageBroadCast(List<Item> items, boolean isNotify,
			boolean isLocal) {
		if (items != null && items.size() > 0) {
			for (Item item : items) {
				LogUtils.d("sendNewMessageBroadCast " +item);
				Contact contact = ContactService.getInstance().getContact(
						item.getJid());
				if (contact != null) {
					item.setNickname(contact.getNickName());
					item.setAlias(contact.getAlias());
					item.setBday(contact.getBday());
					item.setSex(contact.getSex());
					item.setPhoto(contact.getPhoto());
				} 
				item.setLocal(isLocal);
				saveMessageToDB(item);
				Intent intent = new Intent();
				intent.putExtra("item", item);
				intent.setAction(item.getMsgtype().toString());
				LocalBroadcastManager.getInstance(service)
						.sendBroadcast(intent);
//				addMessage(item);
			}
			if (isNotify) {
				boolean[] vibrateRingParams = BBSUtils
						.getVibrateRingParams(service);
				service.vibrateAndRing(vibrateRingParams[0],
						vibrateRingParams[1]);
				if (!BBSUtils.isRunningForeground(service)) {
					notifyNewMessage(items.get(items.size() - 1));
				}
			}
		}
	}
	
	/**
	 * @Title: sendNewMessageBroadCast
	 * @Description: 此处发送广播给上层UI
	 * @param: @param items
	 * @param: @param isNotify 是否响铃、震动
	 * @param: @param isLocal 是否本端产生的
	 * @return: void
	 * @throws:
	 */
	public void sendNewMessageBroadCast(Item item, boolean isNotify,
			boolean isLocal) {
		Contact contact = ContactService.getInstance()
				.getContact(item.getJid());
		if (contact != null) {
			item.setNickname(contact.getNickName());
			item.setAlias(contact.getAlias());
			item.setBday(contact.getBday());
			item.setSex(contact.getSex());
			item.setPhoto(contact.getPhoto());
		}
		item.setLocal(isLocal);
		saveMessageToDB(item);
		Intent intent = new Intent();
		intent.putExtra("item", item);
		intent.setAction(item.getMsgtype().toString());
		LocalBroadcastManager.getInstance(service).sendBroadcast(intent);
		//				addMessage(item);
		if (isNotify) {
			boolean[] vibrateRingParams = BBSUtils
					.getVibrateRingParams(service);
			service.vibrateAndRing(vibrateRingParams[0], vibrateRingParams[1]);
			if (!BBSUtils.isRunningForeground(service)) {
				notifyNewMessage(item);
			}
		}
	}
	private void saveMessageToDB(Item item) {
		LogUtils.d("saveMessageToDB " + item);
		switch (item.getMsgtype()) {
			case chat: {
				// 聊天类型信息
				ChatDB chatDBEditor = new ChatDB();
				chatDBEditor.setField(DBKey.type, item.getMsgtype());
				chatDBEditor.setField(DBKey.msgtime, item.getTimestamp());
				chatDBEditor.setField(DBKey.content, item.getMessage());
				chatDBEditor.setField(DBKey.jid_receive, LoginManager
						.getInstance().getJidParsed());
				chatDBEditor.setField(DBKey.jid_send, item.getJidParsed());
				chatDBEditor.setField(DBKey.subType, item.getMsgTypeSub());
				chatDBEditor.setField(DBKey.isLocal, item.isLocal());
				chatDBEditor.setField(DBKey.msgState, item.getMsgState());
				chatDBEditor.saveToDatabase();
				// 新增的数据全保存
				MessageDB messageEditor = new MessageDB();
				messageEditor.setField(DBKey.jid_send, item.getJidParsed());
				messageEditor.setField(DBKey.jid_receive, LoginManager.getInstance().getJidParsed());
				messageEditor.setField(DBKey.type, item.getMsgtype());
				messageEditor.setField(DBKey.subType, item.getMsgTypeSub());
				messageEditor.setField(DBKey.msgtime, item.getTimestamp());
				messageEditor.setField(DBKey.content, item.getMessage());
				messageEditor.setField(DBKey.unReadMsgCount, item.getUnReadMsgCount());
				messageEditor.setField(DBKey.isChecked, item.isChecked());
				messageEditor.saveToDatabase();
			}
				break;
			case comment: {
				CommentNotifies cn = new CommentNotifies();
				cn.setBody(item.getMessage());
				cn.setComment_time(item.getComment().comment_time);
				cn.setCommentId(item.getComment().cid);
				cn.setFromJid(item.getJidParsed());
				cn.setGid(item.getComment().gid);
				cn.setGidCreatTime(BBSUtils.getShortGidCreatTime(item.getComment().gid_create_time));
				cn.setGidJid(VVXMPPUtils.makeJidParsed(item.getGidJid()));
				cn.setTime(item.getTimestamp());
				cn.setToCommentCid(item.getComment().to_cid);
				cn.setToJid(LoginManager.getInstance().getJidParsed());
				cn.setCheck(false);
				cn.saveToDatabase();
			}
				break;
			case like:  {
				LikedNotifies ln = new LikedNotifies();
				ln.setCheck(false);
				ln.setFromJid(item.getJidParsed());
				ln.setGid(item.getGid());
				ln.setGidCreatTime(BBSUtils.getShortGidCreatTime(item.getGidCreateTime()));
				ln.setGidJid(VVXMPPUtils.makeJidParsed(item.getGidJid()));
				ln.setLikeTime(item.getTimestamp());
				ln.setTime(item.getTimestamp());
				ln.setToJid(LoginManager.getInstance().getJidParsed());
				ln.saveToDatabase();
			}
				break;
			case friend_require: {
				
				FriendRequest fr = new FriendRequest();
				fr.setContent(item.getMessage());
				fr.setJidFrom(item.getJidParsed());
//				fr.setJidTo(LoginManager.getInstance().getJidParsed());
				fr.setStatus(0);
				fr.setTime(item.getTimestamp());
				fr.saveToDatabase();
			}
				break;
			default:
				break;
		}
		
	}
	
	private PendingIntent makeChatIntent() {
		Intent chatIntent = new Intent(service, StartActivity.class);
		chatIntent.setAction(MessageManager.CHECK_MESSAGE);
		chatIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(service, 0,
				chatIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		SharedPrefsUtil.putValue(BeemApplication.getContext(),
				SettingKey.offline_msg, true);
		return contentIntent;
	}
	public List<TimeflyNotify> getTimeNotifies() {
		return timeNotifies;
	}
	// 查看时光提醒
	public void checkTimeflyNotifyDlg(final Activity ctx) {
		//LogUtils.i("checkTimeflyNotifyDlg");
		if (!timeNotifies.isEmpty()) {
			final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
					ctx, R.style.blurdialog);
			TimeflyDueRemindView remindview = new TimeflyDueRemindView(ctx);
			// 此处服务器传递的是完整时间，需要换算成XX天
			final TimeflyNotify notify = timeNotifies.get(0);
			remindview.setText(String.valueOf(BBSUtils
					.getIntervalDayAgo(notify.mention_create_time)));
			remindview.setBtnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					blurDlg.dismiss();
					timeNotifies.remove(notify);
					/*ShareRankingActivity
							.launch(ctx, LoginManager.getInstance()
									.getJidParsed(), notify.gid,
									notify.gid_create_time);*/
					Intent i = new Intent(ctx, ShareRankingActivity.class);
					i.putExtra("jid", LoginManager.getInstance().getJidParsed());
					i.putExtra("gid", notify.gid);
					i.putExtra("gidCreatTime", notify.gid_create_time);
					ctx.startActivity(i);
					ImageFolderNotify notifyDB = new ImageFolderNotify();
					notifyDB.setField(DBKey.jid, LoginManager.getInstance()
							.getJidParsed());
					notifyDB.setField(DBKey.gid, notify.gid);
					notifyDB.setField(DBKey.createTime, notify.gid_create_time);
					notifyDB.setField(DBKey.notify_valid, Valid.close.val);
					notifyDB.saveToDatabaseAsync();
					//更新界面数据
					EventBusData data = new EventBusData(
							EventAction.CheckTimeflyNotify, new Object[] {
									notify.gid_create_time, Valid.close.val });
					EventBus.getDefault().post(data);
				}
			});
			blurDlg.setContentView(remindview.getmView());
			blurDlg.getWindow().setType(
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			blurDlg.setCancelable(true);
			blurDlg.show();
		}
	}
	
	public void loadMessageItems() {
		List<MessageDB> dbMsgs = MessageDB.queryAll(LoginManager
				.getInstance().getJidParsed(), -1);
		for (MessageDB dbMsgOne : dbMsgs) {
			Item item = dbMsgOne.toItem();
			Contact contact = ContactService.getInstance()
					.getContact(item.getJid(), true, false);
			item.setNickname(contact.getNickName());
			item.setAlias(contact.getAlias());
			item.setBday(contact.getBday());
			item.setSex(contact.getSex());
			item.setPhoto(contact.getPhoto());
			addMessage(item);
			dbMsgOne.setField("unReadMsgCount", 0);
			dbMsgOne.saveToDatabase();
		}
	}
	
	public <T extends BaseDB> int loadUnreadMessageCount(Class<T> c) {
		Dao<T, ?> dao = DBHelper.getInstance().getDao(c);
		try {
			DatabaseTableConfig<T> config = DatabaseTableConfig.fromClass(dao.getConnectionSource(), c);
			String tableName = config.getTableName();
			String checkerColumn;
			String arg;
			if ("CommentNotifies".equals(c.getSimpleName())) {
				checkerColumn = "isCheck";
			} else if ("LikedNotifies".equals(c.getSimpleName())) {
				checkerColumn = "isCheck";
			} else if ("FriendRequest".endsWith(c.getSimpleName())) {
				checkerColumn = "status";
			} else {
				return 0;
			}
			arg = "0";
			GenericRawResults<String[]> rrs = dao.queryRaw("select count(id) from "+tableName+" where "+checkerColumn+" = ?", arg);
			List<String[]> rs = rrs.getResults();
			if (rs != null && !rs.isEmpty()) {
				String[] strs = rs.get(0);
				if (strs != null) {
					return Integer.valueOf(strs[0]);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static boolean isReviewItem(Item one, Item other) {
		boolean flat = false;
		if (one == null || other == null) {
			return flat;
		}
		if (one.getMsgtype() != other.getMsgtype()) {
			return flat;
		}
		switch (one.getMsgtype() ) {
			case comment:
			case like: {
				if (one.getGid().equals(other.getGid())
						&& one.getGidCreateTime().equals(other.getGidCreateTime())
						&& one.getGidJid().equals(other.getGidJid())) {
					flat = true;
				}
			}
				break;
			default:
				flat = one.getJidParsed().equals(other.getJidParsed());
				break;
		}
		
		return flat;
	}
	
}
