package com.butterfly.vv.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.SQLException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.network.BDLocator.LocationInfo;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.DataOperation;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.service.VVPacketAdapter;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.AddRosterPacket;
import com.btf.push.AddRosterPacket.Operation;
import com.btf.push.BlackRosterPacket;
import com.btf.push.BlackRosterPacket.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.btf.push.NeighborHoodPacket;
import com.btf.push.NeighborHoodPacket.NeighborHoodType;
import com.btf.push.PhoneContactPacket;
import com.btf.push.PhoneGetPacket;
import com.btf.push.SchoolMatePacket;
import com.btf.push.SynDataPacket;
import com.btf.push.UserInfoPacket;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.btf.push.base.BaseIQ.BaseIQKey;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere;
import com.butterfly.vv.db.ormhelper.DBHelper.DBWhere.DBWhereType;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact.PhoneNumRelation;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact.PhoneNumState;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.UserBlackListDB;
import com.butterfly.vv.db.ormhelper.bean.UserFriendDB;
import com.butterfly.vv.db.ormhelper.bean.UserNeighborDB;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.vv.utils.VVXMPPUtils;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: ContactManager
 * @Description: 个人信息管理类[从Roster中抽离]
 * @author: yuedong bao
 * @date: 2015-4-1 下午4:32:18
 */
public class ContactService {
	private static ContactService instance;
	private XMPPConnection connection;
	private int id = 0;

	public interface onPacketResult<T> {
		void onResult(T result, boolean timeout, Start start);
	}

	// 注意静态成员变量也存在多线程问题
	public synchronized void initXMMPConnnection(XMPPConnection pConnection) {
		if (id > 0) {
			// throw new
			// IllegalStateException("you can init the xmppconnection  only one time.");
		}
		id++;
		connection = pConnection;
	}
	public static ContactService getInstance() {
		if (instance == null) {
			synchronized (ContactService.class) {
				if (instance == null) {
					instance = new ContactService();
				}
			}
		}
		return instance;
	}
	/**
	 * @Title: synGeoInfo
	 * @Description: 同步用户的地理位置信息，并将本用户的地理位置信息上传到服务器
	 * @notice:BDLocator.getInstance()必须运行在UI线程，否则报错
	 */
	public void synGeoInfo(double lat, double lon) {
		//LocationInfo locationInfo = BDLocator.getInstance().requestLatlon();
		//double lat = locationInfo.lat;
		//double lon = locationInfo.lon;
		//		if (locationInfo.lat != LoginManager.getInstance().getLat()
		//				|| lon != LoginManager.getInstance().getLon()) {
		//LogUtils.i("prev_lat:" + LoginManager.getInstance().getLat() + " now_lat:" + lat + " prev_lat:"
		//					+ LoginManager.getInstance().getLon() + " now_lon:" + lon);
		LoginManager.getInstance().getMyContact().setLat(lat);
		LoginManager.getInstance().getMyContact().setLon(lon);
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				Map<UserInfoKey, String> latlonMap = new HashMap<UserInfoPacket.UserInfoKey, String>();
				latlonMap.put(UserInfoKey.lat,
						String.valueOf(BDLocator.getInstance().getLat()));
				latlonMap.put(UserInfoKey.lon,
						String.valueOf(BDLocator.getInstance().getLon()));
				modifyContactInfo(latlonMap);
			}
		});
		//		}
	}
	private ContactService() {
		//LogUtils.d("ContactService ContactService() enter");
	}

	private abstract class ContactOperation<T> extends DataOperation<T> {
		protected String jid;

		@Override
		public DataOperation<T> setParams(Class<?> cls, String params) {
			jid = VVXMPPUtils.makeJidParsed(params);
			return super.setParams(cls, jid);
		}
	}

	private class ContactInfoOperation extends ContactOperation<Contact> {
		@Override
		protected Contact getDataFromNetwork() {
			UserInfoPacket info = new UserInfoPacket();
			info.setType(Type.GET);
			if (!TextUtils.isEmpty(jid)) {
				info.setTo(VVXMPPUtils.makeJidCompleted(jid));
			}
			try {
				Contact contact = null;
				// Get data with xmpp
				if (BeemServiceHelper.getInstance(BeemApplication.getContext()).isAuthentificated()) {
					Packet p = request(info);
					if (p == null) {
						return null;
					}
					UserInfoPacket retVal = (UserInfoPacket) p;
					if (retVal == null || retVal.isEmpty()) {
						//LogUtils.e("Find the contact info failed,jid:" + jid);
						return null;
					}
					contact = retVal.toContact();
				} else {
					// Get data with http
					String url = AppProperty.getInstance().VVAPI + "/get_vcard?tm_id=" + jid;
					String response = Caller.doGet(url);
					JSONObject responseJObject = new JSONObject(response);
					if (responseJObject.getString("result").equals(
							String.valueOf(Constants.RESULT_OK))) {
						JSONObject data = responseJObject.getJSONObject("data");
						if (data != null) {
							contact = new Contact();
							if (!data.isNull("city")) contact.setCityId(data.getString("city"));
							if (!data.isNull("school")) contact.setSchoolId(data.getString("school"));
							if (!data.isNull("tm_id")) contact.setJid(VVXMPPUtils.makeJidParsed(data.getString("tm_id")));
							if (!data.isNull("bday")) contact.setBday(data.getString("bday"));
							if (!data.isNull("signature")) contact.setSignature(data.getString("signature"));
							if (!data.isNull("lon")) contact.setLon(data.getDouble("lon"));
							if (!data.isNull("major")) contact.setMajor(data.getString("major"));
							if (!data.isNull("enroltime")) contact.setEnroltime(data.getString("enroltime"));
							if (!data.isNull("sex")) contact.setSex(data.getString("sex"));
							if (!data.isNull("portrait_small")) contact.setPhoto_small(data.getString("portrait_small"));
							if (!data.isNull("lat")) contact.setLat(data.getDouble("lat"));
							if (!data.isNull("hobby")) contact.setHobby(data.getString("hobby"));
							if (!data.isNull("portrait_big")) contact.setPhoto_big(data.getString("portrait_big"));
							if (!data.isNull("nickname")) contact.setNickName(data.getString("nickname"));
							if (!data.isNull("logintime")) contact.setLogintime(data.getString("logintime"));
						}
					}
				}
				if (contact != null) {
					if (jid.length() == 11) {
						//如果通过手机号搜索出来的联系人，保存下手机号
						contact.setField(DBKey.phoneNum, jid);
					}
					// 取消异步保存，可能花费时间稍多，但是可以保证消息的准确性。
					contact.saveToDatabase();
					// 可能不是好友
					UserFriendDB friendDB = UserFriendDB.queryForFirst(
							LoginManager.getInstance().getJidParsed(), jid);
					if (friendDB != null) {
						contact.setAlias(friendDB.getFieldStr(DBKey.alias));
					}
					return contact;
				}
			}  catch (WSError e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected Contact getDataFromDB() {
			//			long lastMills = System.currentTimeMillis();
			Contact contact = null;
			if (jid.length() == 11) {
				//通过手机号码查询的联系人
				contact = Contact.queryByPhonenum(jid);
				if (contact == null) {
					return null;
				}
				jid = contact.getJid();
			} else {
				contact = Contact.queryForFirst(jid);
			}
			UserFriendDB friendDB = UserFriendDB.queryForFirst(LoginManager
					.getInstance().getJidParsed(), jid);
			if (contact == null) {
				//LogUtils.e("Error:this should not be here,jid:" + jid + " the contact db:"
				//						+ DBHelper.getInstance().queryAll(Contact.class));
				return null;
			}
			if (friendDB != null) {
				contact.setAlias(friendDB.getFieldStr(DBKey.alias));
			}
			//			//LogUtils.i("Contact_getDataFromDB costMills:" + (System.currentTimeMillis() - lastMills));
			return contact;
		}
		@Override
		public DataOperation<Contact> setParams(Class<?> cls, String params) {
			return super.setParams(cls, VVXMPPUtils.makeJidParsed(params));
		}
	}

	/**
	 * @Title: getContact
	 * @Description: TODO
	 * @param jid
	 * @param isReload 在线时有用，忽略缓存时间强制从服务器拉取
	 * @return
	 * @return: Contact
	 */
	public Contact getContact(String jid, boolean... isReload) {
		return getContact(jid, false, BeemApplication.isNetworkOk());
	}
	public Contact getContact(String jid, boolean isoff, boolean isReload) {
		jid = StringUtils.parseName(jid);
		ContactOperation<Contact> contactInfoOpt = new ContactInfoOperation();
		contactInfoOpt.setParams(UserInfoPacket.class, jid);
		Contact contact = null;;
		try {
			contact = contactInfoOpt.getData(isoff, isReload);
		} catch (WSError e) {
			e.printStackTrace();
		}
//		if (contact == null) {
//			//LogUtils.i("getContact get null data;jid:" + jid);
//		}
		return contact;
	}
	private Packet request(Packet req) throws XMPPException {
		if (connection == null || !connection.isConnected()) {
			// 表示是否连接,主机地址是否可用
			throw new XMPPException("Not connected to server.");
		}
		if (!connection.isAuthenticated()) {
			// isAuthenticated表示是否认证,账户密码是否验证通过，
			throw new XMPPException("Not logged in to server.");
		}
		PacketCollector collector = connection
				.createPacketCollector(new PacketIDFilter(req.getPacketID()));
		connection.sendPacket(req);
		Packet response = collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		collector.cancel();
		if (response == null) {
			throw new XMPPException("No response from server on status set.");
		} else if (response.getError() != null) {
			throw new XMPPException(response.getError());
		}
		return response;
	}
	// 获取黑名单信息 isReload:是否重新从服务器加载
	public List<Contact> getBlacklist(boolean... isReload) {
		return getBlacklist(LoginManager.getInstance().getJidParsed(), isReload);
	}
	/**
	 * @Title: getBlacklist
	 * @Description: 获取某个人的黑名单
	 * @param: @param jid
	 * @param: @param isReload
	 * @param: @return
	 * @return: List<Contact>
	 * @throws:
	 */
	public List<Contact> getBlacklist(String jid, boolean... isReload) {
		ContactOperation<List<Contact>> blackOpt = new BlackOperation();
		blackOpt.setParams(BlackRosterPacket.class, jid);
		List<Contact> contacts = new ArrayList<Contact>();
		List<Contact> retVal = null;;
		try {
			retVal = blackOpt.getData(!LoginManager.getInstance()
					.isLogined(), isReload);
		} catch (WSError e) {
			e.printStackTrace();
		}
		if (retVal != null) {
			contacts.addAll(retVal);
		}
		return contacts;
	}

	private class BlackOperation extends ContactOperation<List<Contact>> {
		@Override
		protected List<Contact> getDataFromNetwork() {
			List<Contact> retVal = new ArrayList<Contact>();
			BlackRosterPacket bp = new BlackRosterPacket();
			bp.setType(Type.GET);
			try {
				BlackRosterPacket newCard = (BlackRosterPacket) request(bp);
				if (newCard != null) {
					Iterator<BlackRosterPacket.Item> iterator = newCard
							.getRosterItems().iterator();
					while (iterator.hasNext()) {
						BlackRosterPacket.Item item = iterator.next();
						saveUserBlackDB(item);
						Contact contact = new Contact();
						contact.setField(DBKey.jid, item.getUser());
						contact.setField(DBKey.photo_small, item.getPhoto());
						contact.setField(DBKey.nickName, item.getNickname());
						contact.setField(DBKey.sex, item.getRole());
						contact.setField(DBKey.bday, item.getBday());
						contact.saveToDatabase();
						retVal.add(contact);
					}
				}
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			return retVal;
		}
		private void saveUserBlackDB(BlackRosterPacket.Item item) {
			UserBlackListDB userBlackListDB = new UserBlackListDB();
			userBlackListDB.setField(DBKey.jid, LoginManager.getInstance()
					.getJidParsed());
			userBlackListDB.setField(DBKey.jid_black, item.getUser());
			userBlackListDB.saveToDatabase();
		}
		@Override
		protected List<Contact> getDataFromDB() {
			List<Contact> retVal = new ArrayList<Contact>();
			List<UserBlackListDB> blacks = DBHelper.getInstance().queryAll(
					UserBlackListDB.class,
					new DBWhere(DBKey.jid, DBWhereType.eq, jid));
			if (blacks != null) {
				for (UserBlackListDB blackOne : blacks) {
					Contact black_contact = getContact(blackOne
							.getFieldStr(DBKey.jid_black));
					retVal.add(black_contact);
				}
			}
			return retVal;
		}
	}

	// 好友操作类
	private class FriendOperation extends ContactOperation<List<Contact>> {
		@Override
		protected List<Contact> getDataFromNetwork() {
			if (connection == null || !connection.isAuthenticated()) {
				// throw new IllegalStateException("Not logged in to server.");
				//LogUtils.e("Not logged in to server.");
				return null;
			}
			if (connection.isAnonymous()) {
				// throw new IllegalStateException("Anonymous users can't have a roster.");
				//LogUtils.e("Anonymous users can't have a roster.");
				return null;
			}
			RosterPacket packet = new RosterPacket();
			List<Contact> retVal = new ArrayList<Contact>();
			try {
				RosterPacket result = (RosterPacket) request(packet);
				if (result != null && result.getType().equals(IQ.Type.RESULT)
						&& result.getExtensions().isEmpty()) {
					for (RosterPacket.Item item : result.getRosterItems()) {
						if (item.getJid() == null) {
							//LogUtils.e("the server response a wrong data,drop it.");
						} else {
							if (!RosterPacket.ItemType.remove.equals(item
									.getItemType())) {
								saveUserFriendDB(item);
								Contact contact = new Contact();
								contact.setField(DBKey.jid, item.getJid());
								contact.setField(DBKey.logintime,
										item.getLogintime());
								contact.setField(DBKey.lat,
										Double.parseDouble(item.getLat()));
								contact.setField(DBKey.lon,
										Double.parseDouble(item.getLon()));
								contact.setField(DBKey.signature,
										item.getSignature());
								contact.setField(DBKey.sex, item.getSex());
								contact.setField(DBKey.alias, item.getAlias());
								contact.setField(DBKey.bday, item.getBday());
								contact.setField(DBKey.photo_small,
										item.getHeadPhoto());
								contact.setField(DBKey.nickName,
										item.getNickName());
								contact.saveToDatabaseAsync();
								retVal.add(contact);
							}
						}
					}
					return retVal;
				}
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			return retVal;
		}
		private void saveUserFriendDB(RosterPacket.Item item) {
			UserFriendDB userFrinedB = new UserFriendDB();
			userFrinedB.setField(DBKey.jid, LoginManager.getInstance()
					.getJidParsed());
			userFrinedB.setField(DBKey.jid_friend, item.getJid());
			userFrinedB.setField(DBKey.alias, item.getAlias());
			userFrinedB.setField(DBKey.nickName, item.getNickName());
			userFrinedB.saveToDatabaseAsync();
		}
		@Override
		protected List<Contact> getDataFromDB() {
			List<UserFriendDB> friendDBs = UserFriendDB.queryAll(jid);
			List<Contact> retVal = new ArrayList<Contact>(friendDBs.size());
			for (UserFriendDB friendDBOne : friendDBs) {
				Contact contact = getContact(friendDBOne
						.getFieldStr(DBKey.jid_friend));
				retVal.add(contact);
			}
			return retVal;
		}
	}

	public List<Contact> getFriendListInner(String jid, boolean... isReload) {
		ContactOperation<List<Contact>> friendOpt = new FriendOperation();
		friendOpt.setParams(RosterPacket.class, jid);
		List<Contact> contacts = null;
		try {
			contacts = friendOpt.getData(!LoginManager.getInstance()
					.isLogined(), isReload);
		} catch (WSError e) {
			e.printStackTrace();
		}
		if (contacts == null) {
			contacts = new ArrayList<Contact>();
		}
		return Collections.unmodifiableList(new ArrayList<Contact>(contacts));
	}
	public List<Contact> getFriendlist(boolean... isReload) {
		return getFriendlist(LoginManager.getInstance().getJidParsed(),
				isReload);
	}
	public List<Contact> getFriendlist(String jid, boolean... isReload) {
		List<Contact> coList = new ArrayList<Contact>();
		Map<String, Contact> retVal = new HashMap<String, Contact>();
		for (Contact contact : getFriendListInner(jid, isReload)) {
			retVal.put(contact.getJid(), contact);
		}
		for (Contact contact : getBlacklist(isReload)) {
			retVal.remove(contact.getJid());
		}
		coList.addAll(retVal.values());
		return coList;
	}
	// 删除好友
	public PacketResult removeFriend(String jid) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		RosterPacket packet = new RosterPacket();
		packet.setType(Type.SET);
		RosterPacket.Item item = new RosterPacket.Item(
				VVXMPPUtils.makeJidCompleted(jid), null);
		item.setItemType(ItemType.remove);
		packet.addRosterItem(item);
		PacketResult result;
		try {
			result = toPacketResult(request(packet));
		} catch (XMPPException e) {
			e.printStackTrace();
			result = toPacketResult(e);
		}
		if (result.isOk()) {
			UserFriendDB.deleteDatabase(LoginManager.getInstance()
					.getJidParsed(), jid);
			sendFriendOptBR(Constants.ACTION_FRIEND_REMOVE_FRIEND, true);
		}
		return result;
	}
	private PacketResult toPacketResult(Packet packet) {
		if (packet == null) {
			return toNullPacketResult();
		}
		if (!LoginManager.getInstance().isLogined()) {
			return toPacketResult("网络异常，不能进行此操作！");
		}
		PacketResult result = new PacketResult();
		result.setOk(packet.getError() == null);
		result.setError(packet.getError() == null ? null : packet.getError()
				.toString());
		return result;
	}
	private PacketResult toPacketResult(String error) {
		PacketResult result = new PacketResult();
		result.setOk(false);
		result.setError(error);
		return result;
	}
	public boolean friendYet(String user) {
		UserFriendDB friendDB = UserFriendDB.queryForFirst(LoginManager
				.getInstance().getJidParsed(), user);
		return friendDB != null;
	}
	// 删除黑名单
	public PacketResult removeBlacklist(final String... jids) {
		if (!LoginManager.getInstance().isLogined()) {
			return toPacketResult("网络异常不能操作");
		}
		if (jids == null) {
			return toNullPacketResult();
		}
		PacketResult packetResult = null;
		try {
			for (String jid : jids) {
				BlackRosterPacket blackRosterPacket = new BlackRosterPacket();
				blackRosterPacket.setType(IQ.Type.SET);
				Item item = new Item(VVXMPPUtils.makeJidCompleted(jid), null,
						null);
				item.setAction("remove");
				blackRosterPacket.addRosterItem(item);
				Packet packet = request(blackRosterPacket);
				packetResult = toPacketResult(packet);
				if (packetResult != null && packetResult.isOk()) {
					UserBlackListDB.deleteDatabase(LoginManager.getInstance()
							.getJidParsed(), StringUtils.parseName(jid));
					ContactService.getInstance().sendFriendOptBR(
							Constants.ACTION_FRIEND_REMOVE_BLACKLIST,
							isFriendIn(jids));
				}
			}
			if (packetResult != null && packetResult.isOk()) {
				EventBus.getDefault().post(
						new EventBusData(EventAction.RemoveBlacklist, jids));
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			packetResult = toPacketResult(e);
		}
		return packetResult;
	}
	// 添加黑名单
	public PacketResult addBlacklist(String... jids) {
		if (jids == null) {
			return toNullPacketResult();
		}
		List<String> removeJids = new ArrayList<String>();
		for (String jid : jids) {
			String jidParsed = VVXMPPUtils.makeJidParsed(jid);
			if (!blackYet(jidParsed)) {
				removeJids.add(jidParsed);
			}
		}
		if (removeJids.isEmpty()) {
			PacketResult paResult = new PacketResult();
			paResult.setError("你选择的用户都已在黑名单中，不需重复添加！");
			paResult.setOk(false);
			return paResult;
		}
		BlackRosterPacket blackRosterPacket = new BlackRosterPacket();
		blackRosterPacket.setType(IQ.Type.SET);
		blackRosterPacket.setDoaction("add");
		for (String jid : removeJids) {
			blackRosterPacket.addRosterItem(new BlackRosterPacket.Item(
					VVXMPPUtils.makeJidCompleted(jid), null, null));
		}
		PacketResult packetResult;
		try {
			Packet packet = request(blackRosterPacket);
			packetResult = toPacketResult(packet);
		} catch (XMPPException e) {
			e.printStackTrace();
			packetResult = toPacketResult(e);
		}
		if (packetResult.isOk()) {
			for (String jid : removeJids) {
				String jidParsed = VVXMPPUtils.makeJidParsed(jid);
				UserBlackListDB userBlackListDB = new UserBlackListDB();
				userBlackListDB.setField(DBKey.jid, LoginManager.getInstance()
						.getJidParsed());
				userBlackListDB.setField(DBKey.jid_black, jidParsed);
				userBlackListDB.saveToDatabase();
			}
			// 发送广播给上层
			sendFriendOptBR(Constants.ACTION_FRIEND_ADD_BLACKLIST,
					isFriendIn(jids));
			EventBus.getDefault().post(
					new EventBusData(EventAction.AddBlacklist, jids));
		}
		return packetResult;
	}
	private boolean isFriendIn(String... users) {
		for (String jid : users) {
			if (friendYet(jid)) {
				return true;
			}
		}
		return false;
	}
	private PacketResult toPacketResult(XMPPException e) {
		PacketResult result = new PacketResult();
		result.setError(e.getMessage());
		result.setOk(false);
		return result;
	}
	private PacketResult toNullPacketResult() {
		PacketResult result = new PacketResult();
		result.setError("data is null");
		result.setOk(false);
		return result;
	}
	public boolean blackYet(String user) {
		UserBlackListDB blackDB = UserBlackListDB.queryForFirst(LoginManager
				.getInstance().getJidParsed(), VVXMPPUtils.makeJidParsed(user));
		return blackDB != null;
	}
	// 添加好友
	public PacketResult addFriend(String jid, String content, String operation) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		if (friendYet(jid)) {
			//LogUtils.e("It should not be here;this jid:" + jid + " has be in my friendlist.");
		}
		AddRosterPacket addRoster = new AddRosterPacket();
		addRoster.setType(IQ.Type.SET);
		addRoster.setField(BaseIQKey.time, LoginManager.getInstance()
				.getSystemTime());
		addRoster.setField(BaseIQKey.jid, VVXMPPUtils.makeJidCompleted(jid));
		if (Operation.require.toString().equals(operation)) {
			addRoster.setField(BaseIQKey.subscription, "verify");
			addRoster.setField(BaseIQKey.content, content);
		} else if (Operation.agree.toString().equals(operation)) {
			addRoster.setField(BaseIQKey.subscription, "result");
			addRoster.setField(BaseIQKey.approved, "true");
		} else if (Operation.refuse.toString().equals(operation)) {
			addRoster.setField(BaseIQKey.subscription, "result");
			addRoster.setField(BaseIQKey.approved, "false");
		}
		PacketResult result;
		try {
			result = toPacketResult(request(addRoster));
		} catch (XMPPException e) {
			e.printStackTrace();
			result = toPacketResult(e);
		}
		return result;
	}
	// 修改好友备注
	public PacketResult modifyFriendAlias(String jid, String alias) {
		jid = VVXMPPUtils.makeJidParsed(jid);
		RosterPacket packet = new RosterPacket();
		packet.setType(Type.SET);
		RosterPacket.Item item = new RosterPacket.Item(
				VVXMPPUtils.makeJidCompleted(jid), null);
		item.setAlias(alias);
		item.setItemType(ItemType.modify);
		packet.addRosterItem(item);
		PacketResult result;
		try {
			result = toPacketResult(request(packet));
		} catch (XMPPException e) {
			e.printStackTrace();
			result = toPacketResult(e);
		}
		if (result.isOk()) {
			sendFriendOptBR(Constants.ACTION_FRIEND_MODIFY_ALIAS,
					isFriendIn(jid), jid);
		}
		return result;
	}
	public PacketResult modifyContactInfo(Map<UserInfoKey, String> modifyMap) {
		UserInfoPacket packet = new UserInfoPacket();
		packet.setType(Type.SET);
		for (Iterator<UserInfoKey> keyIt = modifyMap.keySet().iterator(); keyIt
				.hasNext();) {
			UserInfoKey key = keyIt.next();
			packet.setField(key, modifyMap.get(key));
		}
		PacketResult result;
		try {
			result = toPacketResult(request(packet));
			if (result.isOk()) {
				modifyMap.put(UserInfoKey.jid, LoginManager.getInstance()
						.getJidParsed());
				Contact contact = new Contact();
				contact.saveData(modifyMap);
				contact.saveToDatabase();
				LoginManager.getInstance().getMyContact().saveData(modifyMap);
			}
		} catch (XMPPException e) {
			e.printStackTrace();
			result = toPacketResult(e);
		}
		return result;
	}
	// 获取附近的人  TODO
	private IQ getNeighborHoodOne(double lon, double lat,
			NeighborHoodType neighborType, String start, String limit) throws XMPPException {
		if (!connection.isConnected()) {
			return null;
		}
		NeighborHoodPacket info = new NeighborHoodPacket();
		info.setType(Type.GET);
		info.setField(BaseIQKey.lon, String.valueOf(lon));
		info.setField(BaseIQKey.lat, String.valueOf(lat));
		if (!TextUtils.isEmpty(start)) {
			info.setField(BaseIQKey.start, start);
		}
		info.setField(BaseIQKey.limit, limit);
		if (neighborType != NeighborHoodType.all) {
			info.setField(BaseIQKey.sex, neighborType.val);
		}
		IQ retVal;
		retVal = (IQ) request(info);
		return retVal;
	}
	// 获取附近的人列表
	/**
	 * @Title: getNeighborHoodList
	 * @Description: TODO
	 * @param mXmppFacade
	 * @param lon
	 * @param lat
	 * @param neighborType
	 * @param start
	 * @param limit
	 * @return
	 * @return: List<Contact>
	 * @throws WSError 
	 * @throws XMPPException 
	 */
	public List<Contact> getNeighborHoodList(double lon, double lat,
			NeighborHoodType neighborType, Start start, String limit) throws WSError, XMPPException {
		List<Contact> results = new ArrayList<Contact>();
		// If the account is authenticated, get data by xmpp;
		if (connection != null && connection.isAuthenticated()) {
			NeighborHoodPacket nbPacket = (NeighborHoodPacket) getNeighborHoodOne(
					lon, lat, neighborType, start.getVal(), limit);
			if (nbPacket != null && nbPacket.getType() == Type.RESULT) {
				start.setVal(nbPacket.getFieldStr(BaseIQKey.next));
				@SuppressWarnings("unchecked")
				List<NeighborHoodPacket> nbItems = (List<NeighborHoodPacket>) nbPacket
						.getField(BaseIQKey.item);
				if (nbItems == null || nbItems.isEmpty()) {
					start.setVal(Start.END_VAL);
				} else {
					for (NeighborHoodPacket nbItem : nbItems) {
						Contact contact = new Contact();
						String jid_neighbor = VVXMPPUtils.makeJidParsed(nbItem
								.getFieldStr(BaseIQKey.jid));
						contact.setField(DBKey.jid, jid_neighbor);
						contact.setField(DBKey.logintime,
								nbItem.getFieldStr(BaseIQKey.logintime));
						contact.setField(DBKey.lon, Double.parseDouble(nbItem
								.getFieldStr(BaseIQKey.lon)));
						contact.setField(DBKey.lat, Double.parseDouble(nbItem
								.getFieldStr(BaseIQKey.lat)));
						contact.setField(DBKey.bday,
								nbItem.getFieldStr(BaseIQKey.bday));
						contact.setField(DBKey.sex,
								nbItem.getFieldStr(BaseIQKey.sex));
						contact.setField(DBKey.photo_small,
								nbItem.getFieldStr(BaseIQKey.portrait_small));
						contact.setField(DBKey.nickName,
								nbItem.getFieldStr(BaseIQKey.nickname));
						contact.setField(DBKey.signature,
								nbItem.getFieldStr(BaseIQKey.signature));
						contact.setField(DBKey.distance,
								nbItem.getFieldStr(BaseIQKey.distance));
						UserFriendDB friend = UserFriendDB.queryForFirst(
								LoginManager.getInstance().getJidParsed(),
								jid_neighbor);
						if (friend != null) {
							contact.setField(DBKey.alias,
									friend.getField(DBKey.alias));
						}
						contact.saveToDatabaseAsync();
						UserNeighborDB neighborDB = new UserNeighborDB();
						neighborDB.setField(DBKey.jid, LoginManager
								.getInstance().getJidParsed());
						neighborDB.setField(DBKey.jid_neighbor,
								nbItem.getFieldStr(BaseIQKey.jid));
						neighborDB.setField(DBKey.distance, Double
								.parseDouble(nbItem
										.getFieldStr(BaseIQKey.distance)));
						neighborDB.setField(DBKey.neighborType,
								NeighborHoodType.getTypeByValue(contact
										.getSex()));
						neighborDB.setField(DBKey.nickName,
								nbItem.getFieldStr(BaseIQKey.nickname));
						neighborDB.setField(DBKey.logintime,
								nbItem.getFieldStr(BaseIQKey.logintime));
						neighborDB
								.setField(DBKey.lon, Double.parseDouble(nbItem
										.getFieldStr(BaseIQKey.lon)));
						neighborDB
								.setField(DBKey.lat, Double.parseDouble(nbItem
										.getFieldStr(BaseIQKey.lat)));
						neighborDB.setField(DBKey.portrait_small,
								nbItem.getFieldStr(BaseIQKey.portrait_small));
						neighborDB.setField(DBKey.bday,
								nbItem.getFieldStr(BaseIQKey.bday));
						neighborDB.saveToDatabaseAsync();
						results.add(contact);
					}
				}
			}
		} else if (BeemApplication.isNetworkOk()) {
			// If the account is not authenticated but network is ok, get data by http;
			try {
				StringBuilder sb = new StringBuilder(
						AppProperty.getInstance().VVAPI);
				sb.append("/");
				sb.append(AppProperty.GET_NEIGHBORS);
				sb.append("?lon=").append(lon).append("&lat=").append(lat);
				if (start.getVal() != null)
					sb.append("&start=").append(start.getVal());
				sb.append("&limit=").append(limit);
				if (neighborType == NeighborHoodType.male
						|| neighborType == NeighborHoodType.female)
					sb.append("&sex=").append(neighborType.val);
				String url = sb.toString();
				String result = Caller.doGet(url);
				if (!TextUtils.isEmpty(result)) {
					JSONObject json = new JSONObject(result);
					String success = json.getString("description");
					if ("success".equals(success)) {
						JSONArray data_jsonArr = json.getJSONArray("data");
						for (int i = 0, lengh = data_jsonArr.length(); i < lengh; i++) {
							JSONObject dataItem_Json = data_jsonArr
									.getJSONObject(i);
							Contact contact = new Contact();
							String jid_neighbor = VVXMPPUtils
									.makeJidParsed(dataItem_Json
											.getString("tm_id"));
							contact.setField(DBKey.jid, jid_neighbor);
							contact.setField(DBKey.logintime,
									dataItem_Json.getString("logintime"));
							contact.setField(DBKey.lon,
									Double.parseDouble(dataItem_Json
											.getString("lon")));
							contact.setField(DBKey.lat,
									Double.parseDouble(dataItem_Json
											.getString("lat")));
							contact.setField(DBKey.bday,
									dataItem_Json.getString("bday"));
							contact.setField(DBKey.sex,
									dataItem_Json.getString("sex"));
							contact.setField(DBKey.photo_small,
									dataItem_Json.getString("portrait_small"));
							contact.setField(DBKey.nickName,
									dataItem_Json.getString("nickname"));
							//							contact.setField(DBKey.signature, dataItem_Json.getString("signature"));
							contact.setField(DBKey.distance,
									dataItem_Json.getString("distance"));
							UserFriendDB friend = UserFriendDB.queryForFirst(
									LoginManager.getInstance().getJidParsed(),
									jid_neighbor);
							if (friend != null) {
								contact.setField(DBKey.alias,
										friend.getField(DBKey.alias));
							}
							contact.saveToDatabaseAsync();
							UserNeighborDB neighborDB = new UserNeighborDB();
							neighborDB.setField(DBKey.jid, LoginManager
									.getInstance().getJidParsed());
							neighborDB.setField(DBKey.jid_neighbor,
									dataItem_Json.getString("tm_id"));
							neighborDB.setField(DBKey.distance, Double
									.parseDouble(dataItem_Json
											.getString("distance")));
							neighborDB.setField(DBKey.neighborType,
									NeighborHoodType
											.getTypeByValue(dataItem_Json
													.getString("sex")));
							neighborDB.setField(DBKey.nickName,
									dataItem_Json.getString("nickname"));
							neighborDB.setField(DBKey.logintime,
									dataItem_Json.getString("logintime"));
							neighborDB.setField(DBKey.lon,
									Double.parseDouble(dataItem_Json
											.getString("lon")));
							neighborDB.setField(DBKey.lat,
									Double.parseDouble(dataItem_Json
											.getString("lat")));
							neighborDB.setField(DBKey.portrait_small,
									dataItem_Json.getString("portrait_small"));
							neighborDB.setField(DBKey.bday,
									dataItem_Json.getString("bday"));
							neighborDB.saveToDatabaseAsync();
							results.add(contact);
						}
						start.setVal(json.getString("next"));
					}
				}
			}  catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			List<UserNeighborDB> neighbors = UserNeighborDB.queryAll(
					LoginManager.getInstance().getJidParsed(),
					Integer.parseInt(limit), start.getValOff(), neighborType);
			if (neighbors != null && neighbors.size() > 0) {
				for (UserNeighborDB nbOne : neighbors) {
					String jid = nbOne.getFieldStr(DBKey.jid_neighbor);
					Contact contact = getContact(jid, true, true);
					if (contact != null) results.add(contact);
				}
			}
			if (results.isEmpty()) {
				start.setValOff(Start.END_VAL);
			} else {
				start.setValOff(neighbors.get(neighbors.size() - 1)
						.getFieldStr(DBKey.id));
			}
		}
		return results;
	}
	// 获取手机联系人
	public List<com.btf.push.Item> getPhoneContactList(Start start, int limit,
			String lastLocalPhoneNum) throws XMPPException {
		PhoneGetPacket packet = new PhoneGetPacket();
		if (!TextUtils.isEmpty(start.getVal())) {
			packet.setField(BaseIQKey.start, start.getVal());
		}
		packet.setField(BaseIQKey.limit, String.valueOf(limit));
		List<com.btf.push.Item> results = new ArrayList<com.btf.push.Item>();
		if (!LoginManager.getInstance().isLogined()) {
		} else {
			if (lastLocalPhoneNum == null) {
				// 没有加载过本地图片组说明远端数据还有
				PhoneGetPacket resultPacket = (PhoneGetPacket) request(packet);
				if (resultPacket != null
						&& resultPacket.getType() == Type.RESULT) {
					start.setVal(resultPacket.getFieldStr(BaseIQKey.next));
					List<PhoneGetPacket> items = resultPacket.getField(
							BaseIQKey.item, List.class);
					for (PhoneGetPacket findItem : items) {
						String jid = VVXMPPUtils.makeJidParsed(findItem
								.getFieldStr(BaseIQKey.jid));
						String nickName = findItem
								.getFieldStr(BaseIQKey.nickname);
						com.btf.push.Item item = new com.btf.push.Item(jid,
								nickName);
						item.setSex(findItem.getFieldStr(BaseIQKey.sex));
						item.setPhoto(findItem
								.getFieldStr(BaseIQKey.portrait_small));
						item.setBday(findItem.getFieldStr(BaseIQKey.bday));
						item.setLat(Double.parseDouble(findItem
								.getFieldStr(BaseIQKey.lat)));
						item.setLon(Double.parseDouble(findItem
								.getFieldStr(BaseIQKey.lon)));
						item.setLogintime(findItem
								.getFieldStr(BaseIQKey.logintime));
						item.setPhoneNum(findItem
								.getFieldStr(BaseIQKey.phonenum));
						item.setPhoneName(findItem.getFieldStr(BaseIQKey.name));
						item.setMsgtype(MsgType.msg_phone);
						if (ContactService.getInstance().isFriendIn(jid)) {
							item.setMsgTypeSub(MsgTypeSub.added);
						} else {
							item.setMsgTypeSub(MsgTypeSub.unadded);
						}
						PhoneContact phoneContact = new PhoneContact();
						phoneContact.setField(DBKey.name,
								findItem.getFieldStr(BaseIQKey.name));
						phoneContact.setField(DBKey.nameOld,
								findItem.getFieldStr(BaseIQKey.name));
						phoneContact.setField(DBKey.phoneNum,
								findItem.getFieldStr(BaseIQKey.phonenum));
						phoneContact.setField(DBKey.phoneNumRelation,
								PhoneNumRelation.register);
						phoneContact.setField(DBKey.phoneNumState,
								PhoneNumState.uploaded);
						phoneContact.saveToDatabase();
						results.add(item);
					}
				}
			}
		}
		if (Integer.valueOf(limit) > results.size()) {
			List<PhoneContact> phoneContacts = PhoneContact
					.querySomeUnregister(lastLocalPhoneNum,
							limit - results.size());
			if (phoneContacts != null && phoneContacts.size() > 0) {
				for (PhoneContact phoneContact : phoneContacts) {
					com.btf.push.Item item = new com.btf.push.Item("", "");
					item.setPhoneName(phoneContact.getFieldStr(DBKey.name));
					item.setPhoneNum(phoneContact.getFieldStr(DBKey.phoneNum));
					item.setMsgtype(MsgType.msg_phone);
					item.setSex("0");
					item.setMsgTypeSub(MsgTypeSub.unregister);
					//查询手机号已经注册
					results.add(item);
				}
			}
		}
		if (Integer.valueOf(limit) > results.size()) {
			// 下面没有数据了
			start.setVal(Start.END_VAL);
		}
		return results;
	}
	// 获取校友
	public List<com.btf.push.Item> getSchoolMateList(String schoolId,
			Start start, int limit) throws XMPPException {
		List<com.btf.push.Item> results = new ArrayList<com.btf.push.Item>();
		SchoolMatePacket nbPacket = (SchoolMatePacket) getSchoolMateOne(
				schoolId, start, limit);
		if (nbPacket != null && nbPacket.getType() == Type.RESULT) {
			List<SchoolMatePacket> nbItems = nbPacket.getField(BaseIQKey.item,
					List.class);
			start.setVal(nbPacket.getFieldStr(BaseIQKey.next));
			for (SchoolMatePacket nbItem : nbItems) {
				// sex lon nickname logintime age jid lat portrait_small
				Contact contact = new Contact();
				contact.setField(DBKey.jid, nbItem.getFieldStr(BaseIQKey.jid));
				contact.setField(DBKey.logintime,
						nbItem.getFieldStr(BaseIQKey.logintime));
				contact.setField(DBKey.lon,
						Double.parseDouble(nbItem.getFieldStr(BaseIQKey.lon)));
				contact.setField(DBKey.lat,
						Double.parseDouble(nbItem.getFieldStr(BaseIQKey.lat)));
				contact.setField(DBKey.bday, nbItem.getFieldStr(BaseIQKey.bday));
				contact.setField(DBKey.sex, nbItem.getFieldStr(BaseIQKey.sex));
				contact.setField(DBKey.photo_small,
						nbItem.getFieldStr(BaseIQKey.portrait_small));
				contact.setField(DBKey.nickName,
						nbItem.getFieldStr(BaseIQKey.nickname));
				contact.saveToDatabaseAsync();
				com.btf.push.Item item = new com.btf.push.Item(
						contact.getJid(), contact.getNickName());
				item.setSex(contact.getSex());
				item.setBday(contact.getBday());
				item.setLat(contact.getLat());
				item.setLon(contact.getLon());
				item.setLogintime(contact.getLogintime());
				item.setPhoto(contact.getPhoto());
				item.setMsgtype(MsgType.msg_school);
				item.setMsgTypeSub(MsgTypeSub.unadded);
				results.add(item);
			}
		}
		return results;
	}
	private IQ getSchoolMateOne(String schoolId, Start start, int limit)
			throws XMPPException {
		SchoolMatePacket schoolPacket = new SchoolMatePacket();
		schoolPacket.setType(Type.GET);
		schoolPacket.setField(BaseIQKey.school, schoolId);
		if (!TextUtils.isEmpty(start.getVal())) {
			schoolPacket.setField(BaseIQKey.start, start.getVal());
		}
		schoolPacket.setField(BaseIQKey.limit, String.valueOf(limit));
		return (IQ) request(schoolPacket);
	}
	// 同步服务器数据
	public Packet SynData(BaseIQKey synKey) {
		SynDataPacket errorPacket = new SynDataPacket();
		errorPacket.setType(Type.ERROR);
		switch (synKey) {
			case syndata_blacklist:
			case syndata_roster:
			case syndata_systime:
			case syndata_tm:
			case syndata_vcard: {
				break;
			}
			default:
				errorPacket.setError(new XMPPError(new Condition(
						"wrong parameter")));
				return errorPacket;
		}
		SynDataPacket sendPacket = new SynDataPacket();
		SynDataPacket itemPacket = new SynDataPacket();
		itemPacket.setField(BaseIQKey.xmlns.toString(), synKey.val);
		List<SynDataPacket> itemPackets = new ArrayList<SynDataPacket>();
		itemPackets.add(itemPacket);
		sendPacket.setField(BaseIQKey.item, itemPackets);
		try {
			return request(sendPacket);
		} catch (XMPPException e) {
			e.printStackTrace();
			errorPacket.setError(e.getXMPPError());
			return errorPacket;
		}
	}
	// todo
	public boolean uploadPhoneContacts(IXmppFacade mXmppFacade,
			Map<String, PhoneContact> localPhoneContacts, int limit) {
		if (!LoginManager.getInstance().isLogined()) {
			return false;
		}
		//LogUtils.i("~~uploadPhoneContacts~~");
		long t1 = System.currentTimeMillis();
		ArrayList<PhoneContact> addPhoneContacts = new ArrayList<PhoneContact>();
		ArrayList<PhoneContact> removePhoneContacts = new ArrayList<PhoneContact>();
		ArrayList<PhoneContact> modifyPhoneContacts = new ArrayList<PhoneContact>();
		try {
			// dbPhoneContacts:本地数据库中的	localPhoneContacts:手机联系人的
			ArrayList<PhoneContact> dbPhoneContacts = (ArrayList<PhoneContact>) PhoneContact
					.queryForAll();
			Map<String, PhoneContact> dbPhoneContactMap = new HashMap<String, PhoneContact>();
			if (dbPhoneContacts != null) {
				for (PhoneContact phoneContactOne : dbPhoneContacts) {
					dbPhoneContactMap.put(
							phoneContactOne.getFieldStr(DBKey.phoneNum),
							phoneContactOne);
				}
			}
			// 找出那些是增加，那些是修改的，那些是删除的
			for (String phoneNum : localPhoneContacts.keySet()) {
				// 手机联系人有本地数据库没有，表示新增的
				PhoneContact localPhoneContact = localPhoneContacts
						.get(phoneNum);
				if (!dbPhoneContactMap.containsKey(phoneNum)) {
					localPhoneContact.setField(DBKey.phoneNumState,
							PhoneNumState.add);
					addPhoneContacts.add(localPhoneContact);
				}
			}
			for (String phoneNum : dbPhoneContactMap.keySet()) {
				PhoneContact dbPhoneContact = dbPhoneContactMap.get(phoneNum);
				// 本地数据库有但手机联系人没有,并且以前服务器没有上传过，表示删除了
				if (!localPhoneContacts.containsKey(phoneNum)
						&& dbPhoneContact.getField(DBKey.phoneNumState) != PhoneNumState.uploaded) {
					dbPhoneContact.setField(DBKey.phoneNumState,
							PhoneNumState.remove);
					removePhoneContacts.add(dbPhoneContact);
				} else {
					PhoneContact localPhoneContact = localPhoneContacts
							.get(phoneNum);
					// 手机联系人有本地数据库有,但名字更改了，表示变更的
					if (!dbPhoneContact.getField(DBKey.name).equals(
							localPhoneContact.getField(DBKey.name))) {
						// 旧的号码为dbPhoneContact中的号码
						dbPhoneContact.setField(DBKey.nameOld,
								dbPhoneContact.getField(DBKey.name));
						dbPhoneContact.setField(DBKey.name,
								localPhoneContact.getField(DBKey.name));
						dbPhoneContact.setField(DBKey.phoneNumState,
								PhoneNumState.modify);
						modifyPhoneContacts.add(dbPhoneContact);
					} else {
						// 此处表示上传没上传完的,注意上传成功的统一状态设置成uploaded
						switch (dbPhoneContact.getField(DBKey.phoneNumState,
								PhoneNumState.class)) {
							case add:
								addPhoneContacts.add(dbPhoneContact);
								break;
							case modify:
								modifyPhoneContacts.add(dbPhoneContact);
							case remove:
								removePhoneContacts.add(dbPhoneContact);
								break;
							case uploaded:
								//alread uploaded,do nothing
								break;
							default:
								break;
						}
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		ArrayList<PhoneContact> uploadContacts = new ArrayList<PhoneContact>();
		uploadContacts.addAll(addPhoneContacts);
		uploadContacts.addAll(removePhoneContacts);
		uploadContacts.addAll(modifyPhoneContacts);
		if (uploadContacts.size() > 0) {
			try {
				//每次至多上传limit条数据
				for (int k = 0; k < uploadContacts.size(); k += limit) {
					List<PhoneContact> splitContacts = uploadContacts.subList(k
							* limit,
							Math.min(uploadContacts.size(), (k + 1) * limit));
					uploadPhoneContactLimit(mXmppFacade, splitContacts, limit);
				}
				return true;
			} finally {
				// 保存数据库
				int k = 0;
				for (PhoneContact uploadContact : uploadContacts) {
					uploadContact.saveToDatabaseAsync();
					if (uploadContact.getField(DBKey.phoneNumState) == PhoneNumState.uploaded) {
						k++;
					}
				}
				//LogUtils.i("save uploadContacts,costMills:" + (System.currentTimeMillis() - t1) + " k:" + k
				//						+ " uploadContacts.size:" + uploadContacts.size());
			}
		}
		return false;
	}
	private void uploadPhoneContactLimit(IXmppFacade mXmppFacade,
			List<PhoneContact> uploadContacts, int limit) {
		PhoneContactPacket packet = new PhoneContactPacket();
		packet.setType(IQ.Type.SET);
		List<PhoneContactPacket> itemPackets = new ArrayList<PhoneContactPacket>();
		List<PhoneContact> uploadedPhoneContacts = new ArrayList<PhoneContact>();
		int i = 0;
		for (PhoneContact uploadContact : uploadContacts) {
			if (i >= Math.min(uploadContacts.size(), limit)) {
				break;
			}
			packet.pushPacktContact(itemPackets, uploadContact);
			uploadedPhoneContacts.add(uploadContact);
			i++;
		}
		packet.setField(BaseIQKey.item, itemPackets);
		IQ resultIQ = (IQ) VVPacketAdapter.collectVVPacket(mXmppFacade, packet);
		if (resultIQ != null && resultIQ.getType() == Type.RESULT) {
			// 保存到数据库
			for (PhoneContact pContact : uploadedPhoneContacts) {
				pContact.setField(DBKey.phoneNumState, PhoneNumState.uploaded);
			}
		}
		//LogUtils.v("upload " + itemPackets.size() + " phoneContacts success.");
	}
	// 发送广播给上层
	public void sendFriendOptBR(String action, boolean isUpdate,
			final String... jids) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra(Constants.EXTRA_FRIEND_ISUPDATE_FRIENDS, isUpdate);
		for (String jid : jids) {
			if (action.equals(Constants.ACTION_FRIEND_MODIFY_ALIAS)) {
				intent.putExtra(Constants.EXTRA_FRIEND_UPDATE_ALIAS,
						getContact(jid).getAlias());
			}
		}
		LocalBroadcastManager.getInstance(BeemApplication.getContext())
				.sendBroadcast(intent);
	}
	
	/**
	 * insert friend messages into table 'UserFriendDB'.耗时操作.
	 * @param jid
	 */
	public void insertFriendInto(String jid) {
		Contact c = getContact(jid);
		UserFriendDB friend = new UserFriendDB();
		friend.setField("jid", LoginManager.getInstance().getJidParsed());
		friend.setField("jid_friend", c.getJIDParsed());
		friend.setField("alias", c.getAlias());
		friend.setField("nickName", c.getNickName());
		friend.saveToDatabase();
	}
}
