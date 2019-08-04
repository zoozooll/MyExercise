/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.ui.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.tsz.afinal.http.AjaxCallBack;

import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.util.StringUtils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.bean.DraftInfo;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.DraftDao;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.AudioFinalHttp;
import com.beem.project.btf.network.AudioFinalHttp.IAudioPlayListener;
import com.beem.project.btf.network.ImageFinalHttp;
import com.beem.project.btf.providers.AvatarProvider;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.Message;
import com.beem.project.btf.service.PresenceAdapter;
import com.beem.project.btf.service.aidl.IBeemRosterListener;
import com.beem.project.btf.service.aidl.IChat;
import com.beem.project.btf.service.aidl.IChatManager;
import com.beem.project.btf.service.aidl.IChatManagerListener;
import com.beem.project.btf.service.aidl.IMessageListener;
import com.beem.project.btf.service.aidl.IRoster;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.fragment.TalkFragement;
import com.beem.project.btf.ui.fragment.TalkFragement.FragmentType;
import com.beem.project.btf.ui.fragment.TalkFragement.SendmsgListenter;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ExpressionUtil;
import com.beem.project.btf.utils.ExpressionUtil.ExpressionSizeType;
import com.beem.project.btf.utils.Status;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.camera.GalleryActivity;
import com.butterfly.vv.db.ormhelper.bean.ChatDB;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.view.CircleImageView;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.pullToRefresh.ui.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * This class represents an activity which allows the user to chat with his/her contacts.
 * @author Jean-Manuel Da Silva <dasilvj at beem-project dot com>
 */
public class ChatActivity extends VVBaseFragmentActivity implements
		IEventBusAction {
	private static final String TAG = "ChatActivity";
	private static final Intent SERVICE_INTENT = new Intent();
	public static final String PATTERN = "[a-z]em[0-9]{1,2}";
	static {
		SERVICE_INTENT.setComponent(new ComponentName("com.beem.project.btf",
				"com.beem.project.btf.BeemService"));
	}
	private Handler mHandler = new Handler();
	private IRoster mRoster;
	private Contact mContact;
	private PullToRefreshListView mMessagesListView;
	private final List<MessageText> mListMessages = new ArrayList<MessageText>();
	private IChat mChat;
	private IChatManager mChatManager;
	private final IMessageListener mMessageListener = new OnMessageListener();
	private final IChatManagerListener mChatManagerListener = new ChatManagerListener();
	private MessagesListAdapter mMessagesListAdapter = new MessagesListAdapter();
	private final ServiceConnection mConn = new BeemServiceConnection();
	// 动态更新聊天人的状态：在线，头像
	private final BeemRosterListener mBeemRosterListener = new BeemRosterListener();
	private IXmppFacade mXmppFacade;
	private String mCurrentAvatarId;
	private boolean mBinded;
	private boolean mCompact;
	private TextView contacts_textView2;
	private Context mContext;
	private DraftDao draftDao;
	private DraftInfo draftInfo;
	private TalkFragement talkFragement;
	// 每页显示多少条聊天消息
	private final int ITEMS_PAGE = 15;

	// 跳转到聊天Activity
	public static void launch(Context ctx, Contact contact) {
		if (!LoginManager.getInstance().isLogined()) {
			CToast.showToast(ctx, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
			ActivityController.getInstance().gotoLogin();
			return;
		}
		Intent i = new Intent(ctx, ChatActivity.class);
		i.putExtra("contact", contact);
		ctx.startActivity(i);
		// 打开聊天后,消息模块的数据更改
		Item item = new Item(contact.getJIDParsed(), null);
		item.setMsgtype(MsgType.chat);
		EventBus.getDefault().post(
				new EventBusData(EventAction.MessageChatOpen, item));
	}
	@Override
	protected void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		mContext = this;
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		}
		if (!LoginManager.getInstance().isLogined()) {
			finish();
			CToast.showToast(this, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
			return;
		}
		mContact = getIntent().getParcelableExtra("contact");
		mCompact = settings.getBoolean(BeemApplication.USE_COMPACT_CHAT_UI_KEY,
				false);
		// 聊天界面
		setContentView(R.layout.chat);
		draftDao = new DraftDao(mContext);
		EventBus.getDefault().register(this);
		// 导航条设置
		CustomTitleBtn back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		CustomTitleBtn info = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		back.setTextAndImgRes("聊天", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		info.setTextViewVisibility(View.GONE)
				.setImgResource(R.drawable.user_info_selector)
				.setViewPaddingRight().setVisibility(View.VISIBLE);
		info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ContactInfoActivity.launch(mContext, mContact);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SaveUnSendMSG();
				ChatActivity.this.finish();
			}
		});
		contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		setTitleText();
		// 聊天条目
		mMessagesListView = (PullToRefreshListView) findViewById(R.id.chat_messages);
		mMessagesListView.getRefreshableView().setDivider(null);
		mMessagesListView.getRefreshableView().setDividerHeight(0);
		mMessagesListView.getRefreshableView().setAdapter(mMessagesListAdapter);
		mMessagesListView.getRefreshableView().setFastScrollEnabled(true);
		mMessagesListView.getRefreshableView().setFadingEdgeLength(0);
		mMessagesListView.getRefreshableView().setFocusable(true);
		mMessagesListView.getRefreshableView().setSmoothScrollbarEnabled(true);
		mMessagesListView.getRefreshableView().setStackFromBottom(false);
		mMessagesListView.getRefreshableView().setTranscriptMode(
				AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mMessagesListView.setPullLoadEnabled(false);
		mMessagesListView.setOnRefreshListener(xlistViewLis);
		mMessagesListView.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true));
		// 加载输入框碎片
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		talkFragement = TalkFragement.newFragment(FragmentType.normal);
		transaction.add(R.id.input_wraper, talkFragement);
		transaction.commit();
		draftInfo = draftDao.query(mContact.getJid(), "normal");
		// 设置发消息接口
		talkFragement.setSendmsgListenter(new SendmsgListenter() {
			@Override
			public void sendmsg(TalkFragement fragment, String str) {
				MessageText messageText = new MessageText(LoginManager
						.getInstance().getJidParsed(), LoginManager
						.getInstance().getJidParsed(), null, false,
						LoginManager.getInstance().getSysytemTimeDate());
				messageText.setMessage(str);
				messageText.setMsgState(MessageState.preloading, mChat);
				mListMessages.add(messageText);
				mMessagesListAdapter.notifyDataSetChanged();
			}
		});
		mMessagesListView.getRefreshableView().setOnTouchListener(
				new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (talkFragement != null) {
							talkFragement.closeInput();
						}
						return false;
					}
				});
	}
	/**
	 * 储存输入框中未发送的信息
	 */
	private void SaveUnSendMSG() {
		if (draftInfo == null) {
			String id = mContact.getJid();
			String type = "normal";
			String content = talkFragement.getContent();
			if (!content.equals("")) {
				draftInfo = new DraftInfo(id, type, content);
				int addDraftInfo = draftDao.addDraftInfo(draftInfo.getId(),
						draftInfo.getType(), draftInfo.getContent());
			}
		} else {
			String id = mContact.getJid();
			String type = "normal";
			String content = talkFragement.getContent();
			draftInfo.setContent(content);
			int update = draftDao.update(draftInfo.getId(),
					draftInfo.getType(), draftInfo.getContent());
		}
	}

	public interface IMessageStateListener {
		public void onPostExecute(String url, MessageState state);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (mChat != null) {
				mChat.setOpen(false);
				mChat.removeMessageListener(mMessageListener);
			}
			if (mRoster != null)
				mRoster.removeRosterListener(mBeemRosterListener);
			if (mChatManager != null)
				mChatManager.removeChatCreationListener(mChatManagerListener);
		} catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		}
		if (mBinded) {
			unbindService(mConn);
			mBinded = false;
		}
		mXmppFacade = null;
		mRoster = null;
		mChat = null;
		mChatManager = null;
		EventBus.getDefault().unregister(this);
	}
	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// 关闭录音播放
		AudioFinalHttp.getInstance().stopAllPlaying();
		//如果正在录音,则关闭录音释放资源
		if (talkFragement != null) {
			talkFragement.stopRecord();
		}
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}
	/**
	 * {@inheritDoc}.
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	/**
	 * Change the displayed chat.
	 * @param contact the targeted contact of the new chat
	 * @throws RemoteException If a Binder remote-invocation error occurred.
	 */
	private void changeCurrentChat(Contact contact) throws RemoteException {
		if (mChat != null) {
			mChat.setOpen(false);
			mChat.removeMessageListener(mMessageListener);
		}
		mChat = mChatManager.getChat(contact.getJid());
		//LogUtils.i("mchat is null ?:" + (mChat == null) + " contact.getJid():" + contact.getJid());
		if (mChat == null) {
			// 如果mChatManager中先前没有则创建一个本地chat
			mChat = mChatManager
					.createChat(mContact.getJid(), mMessageListener);
			mChat.setOpen(true);
		}
		if (mChat != null) {
			mChat.setOpen(true);
			mChat.addMessageListener(mMessageListener);
			mChatManager.deleteChatNotification(mChat);
			updateOtrInformations(mChat.getOtrStatus());
		}
		//LogUtils.i(" contact.getJID():" + contact.getJid());
		String res = contact.getSelectedRes();
		if (mContact == null)
			mContact = contact;
		if (!"".equals(res)) {
			mContact.setSelectedRes(res);
		}
		updateContactInformations();
		updateContactStatusIcon();
		playRegisteredTranscript();
	}
	/**
	 * Get all messages from the current chat and refresh the activity with them.
	 * @throws RemoteException If a Binder remote-invocation error occurred.
	 */
	private void playRegisteredTranscript() throws RemoteException {
		mListMessages.clear();
		if (mChat != null) {
			List<MessageText> msgList = convertMessagesList(mChat.getMessages());
			mListMessages.addAll(msgList);
			// 如果现有消息少于ITEMS_PAGE条从历史数据补齐显示
			if (msgList.size() < ITEMS_PAGE) {
				pullHistoryAsyn(ITEMS_PAGE - msgList.size(),
						new OnHistroyResultLis() {
							@Override
							public void onResut(List<MessageText> msgTestDBs) {
//								mListMessages.addAll(0, msgTestDBs);
								// 去重；
								for (int i = msgTestDBs.size() - 1 ; i >=0; i--) {
									boolean flag = false;
									MessageText mt = msgTestDBs.get(i);
									for (MessageText mt1 : mListMessages) {
										if (mt1.getTimestamp().equals(mt.getTimestamp())) {
											flag = true;
											break;
										}
									}
									if (!flag)
										mListMessages.add(0, mt);
								}
								mMessagesListAdapter.notifyDataSetChanged();
								
							}
						});
			} else {
				mMessagesListAdapter.notifyDataSetChanged();
			}
		}
	}
	/**
	 * Convert a list of Message coming from the service to a list of MessageText that can be
	 * displayed in UI.
	 * @param chatMessages the list of Message
	 * @return a list of message that can be displayed.
	 */
	private List<MessageText> convertMessagesList(List<Message> chatMessages) {
		List<MessageText> result = new ArrayList<MessageText>(
				chatMessages.size());
		String remoteName = mContact.getNickName();
		String localName = getString(R.string.chat_self);
		MessageText lastMessage = null;
		for (Message m : chatMessages) {
			String name = remoteName;
			String fromBareJid = StringUtils.parseBareAddress(m.getFrom());
			if (m.getType() == Message.MSG_TYPE_ERROR) {
				lastMessage = null;
				result.add(new MessageText(fromBareJid, name, m.getBody(),
						true, m.getTimestamp()));
			} else if (m.getType() == Message.MSG_TYPE_INFO) {
				lastMessage = new MessageText("", "", m.getBody(), false);
				result.add(lastMessage);
			} else if (m.getType() == Message.MSG_TYPE_CHAT) {
				if (fromBareJid == null) { // nofrom or from == yours
					name = localName;
					fromBareJid = "";
				}
				if (m.getBody() != null) {
					lastMessage = new MessageText(fromBareJid, name,
							m.getBody(), false, m.getTimestamp());
					//LogUtils.i("convertMessagesList messageJid: " + lastMessage.getBareJidParsed());
					if (m.getSubType() == MsgTypeSub.systemPrompt) {
						lastMessage
								.setMsgType(MessageType.MESSAGE_TYPE_SYSTEMPROMP);
					} else if (m.getSubType() == MsgTypeSub.html) {
						lastMessage
								.setMsgType(MessageType.MESSAGE_TYPE_OTHER_HTML);
					} else {
						String body = m.getBody();
						lastMessage.setMessage(body);
					}
					lastMessage.setMsgState(m.getMsgState(), mChat);
					result.add(lastMessage);
				}
			}
		}
		return result;
	}
	// 发送最后一条message给其他模块,比如[消息列表,状态栏]
	private void toSendLastMsg() {
		if (mListMessages.size() >= 1) {
			MessageText msg = mListMessages.get(mListMessages.size() - 1);
			Log.i("VV", msg.getName() + msg.getMessage());
			Intent intentmessage = new Intent("com.vv.im.last.message");
			intentmessage.putExtra("lastmsg", msg.getMessage());
			intentmessage.putExtra("name", msg.getBareJid());
			if (msg.getTimestamp() != null) {
				DateFormat df = DateFormat.getDateTimeInstance(
						DateFormat.SHORT, DateFormat.MEDIUM);
				String date = df.format(msg.getTimestamp());
				intentmessage.putExtra("date", date);
			}
			// 本地广播
			LocalBroadcastManager.getInstance(this)
					.sendBroadcast(intentmessage);
		}
	}
	/**
	 * Send an XMPP message. 封装发送消息
	 */
	// TODO
	private void sendMessage(String messageRemote) {
		if (TextUtils.isEmpty(messageRemote)) {
			// 内容为空不发送
			return;
		}
		// 需要先判断mChatManager是否为空，为空不能发送消息
		if (mChatManager == null) {
			return;
		}
		Message msgToSend = new Message(mContact.getJIDCompleted(),
				Message.MSG_TYPE_CHAT);
		msgToSend.setBody(messageRemote);
		msgToSend.setFrom(LoginManager.getInstance().getJidCompleted());
		msgToSend.setTimestampStr(LoginManager.getInstance().getSystemTime());
		msgToSend.setMsgState(MessageState.success);
		try {
			if (mChat == null) {
				mChat = mChatManager.createChat(mContact.getJid(),
						mMessageListener);
				mChat.setOpen(true);
			}
			mChat.sendMessage(msgToSend);
			toSendLastMsg();
		} catch (RemoteException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	/**
	 * Update the contact informations.
	 */
	private void updateContactInformations() {
		// Check for a contact name update
		String name = mContact.getNickName();
		if (name != null && name.equals(mContact.getJid())) {
			name = StringUtils.parseName(mContact.getJid());
		}
		String res = mContact.getSelectedRes();
		if (!"".equals(res))
			name += "(" + res + ")";
		if (!mCompact) {
			setTitleText();
			// Check for a contact status message update
		} else {
			Mode m = Status.getPresenceModeFromStatus(mContact.getStatus());
			if (m == null)
				setTitle(getString(R.string.chat_name) + " " + name + " ("
						+ getString(R.string.contact_status_msg_offline) + ")");
			else
				setTitle(getString(R.string.chat_name) + " " + name + " ("
						+ m.name() + ")");
		}
	}
	private void setTitleText() {
		String title = mContact.getAlias();
		if (TextUtils.isEmpty(title)) {
			title = mContact.getNickName();
		}
		contacts_textView2.setText(BBSUtils.replaceBlank(title));
	}
	/**
	 * Update the OTR informations.
	 * @param otrState the otr state
	 */
	private void updateOtrInformations(final String otrState) {
		/*String text = null;
		if ("ENCRYPTED".equals(otrState)) {
			text = ChatActivity.this.getString(R.string.chat_otrstate_encrypted);
		} else if ("FINISHED".equals(otrState)) {
			text = ChatActivity.this.getString(R.string.chat_otrstate_finished);
		} else if ("AUTHENTICATED".equals(otrState)) {
			text = ChatActivity.this.getString(R.string.chat_otrstate_authenticated);
		} else {
			text = ChatActivity.this.getString(R.string.chat_otrstate_plaintext);
		}*/
	}
	/**
	 * Update the contact status icon.
	 */
	private void updateContactStatusIcon() {
		if (mCompact)
			return;
		String id = mContact.getAvatarId();
		if (id == null)
			id = "";
		Log.d(TAG, "update contact icon  : " + id + " ****" + mCurrentAvatarId);
		if (!id.equals(mCurrentAvatarId)) {
			Drawable avatar = getAvatarDrawable(mContact.getAvatarId());
		}
	}
	/**
	 * Get a Drawable containing the avatar icon.
	 * @param avatarId the avatar id to retrieve or null to get default
	 * @return a Drawable
	 */
	private Drawable getAvatarDrawable(String avatarId) {
		Drawable avatarDrawable = null;
		if (avatarId != null) {
			Uri uri = AvatarProvider.CONTENT_URI.buildUpon()
					.appendPath(avatarId).build();
			InputStream in = null;
			try {
				try {
					in = getContentResolver().openInputStream(uri);
					avatarDrawable = Drawable.createFromStream(in, avatarId);
				} finally {
					if (in != null)
						in.close();
				}
			} catch (IOException e) {
				Log.w(TAG, "Error while setting the avatar", e);
			}
		}
		if (avatarDrawable == null)
			avatarDrawable = getResources().getDrawable(
					R.drawable.default_head_selector);
		return avatarDrawable;
	}

	/**
	 * {@inheritDoc}.
	 */
	private final class BeemServiceConnection implements ServiceConnection {
		/**
		 * Constructor.
		 */
		public BeemServiceConnection() {
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mXmppFacade = IXmppFacade.Stub.asInterface(service);
			try {
				mRoster = mXmppFacade.getRoster();
				if (mRoster != null)
					mRoster.addRosterListener(mBeemRosterListener);
				mChatManager = mXmppFacade.getChatManager();
				if (mChatManager != null) {
					mChatManager.addChatCreationListener(mChatManagerListener);
					changeCurrentChat(mContact);
				}
			} catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		/**
		 * {@inheritDoc}.
		 */
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mXmppFacade = null;
			try {
				mRoster.removeRosterListener(mBeemRosterListener);
				mChatManager.removeChatCreationListener(mChatManagerListener);
			} catch (RemoteException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	private class BeemRosterListener extends IBeemRosterListener.Stub {
		/**
		 * Constructor.
		 */
		public BeemRosterListener() {
		}
		/**
		 * {@inheritDoc}.
		 */
		@Override
		public void onEntriesAdded(List<String> addresses)
				throws RemoteException {
		}
		/**
		 * {@inheritDoc}.
		 */
		@Override
		public void onEntriesDeleted(List<String> addresses)
				throws RemoteException {
		}
		/**
		 * {@inheritDoc}.
		 */
		@Override
		public void onEntriesUpdated(List<String> addresses)
				throws RemoteException {
		}
		/**
		 * {@inheritDoc}.
		 */
		@Override
		public void onPresenceChanged(final PresenceAdapter presence)
				throws RemoteException {
			if (mContact.getJid().equals(
					StringUtils.parseBareAddress(presence.getFrom()))) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mContact.setStatus(presence.getStatus());
						mContact.setMsgState(presence.getStatusText());
						updateContactInformations();
						updateContactStatusIcon();
					}
				});
			}
		}
		@Override
		public void onRostersUpdate(List<Contact> rosters)
				throws RemoteException {
		}
		@Override
		public void onNewFriendsUpdate(List<Contact> newfriends)
				throws RemoteException {
		}
		@Override
		public void onBlacklistUpdate(List<Contact> blacklist)
				throws RemoteException {
		}
		@Override
		public void onBlacklistedUpdate(List<String> blacklisted)
				throws RemoteException {
		}
		@Override
		public void onUserUpdate(String user, Contact contact)
				throws RemoteException {
		}
		@Override
		public void onActionResult(String action, boolean state)
				throws RemoteException {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 此处处理拍照结果
		if (requestCode == Constants.TAKEPHOTO) {
			if (data != null) {
				Bundle b = data.getExtras();
				if (b != null) {
					Object o = b.get("data");
					if (o != null) {
						Bitmap bitmap = (Bitmap) o;
						String capturePath = BBSUtils.getTakePhotoPath(this,
								"capture_" + System.currentTimeMillis()
										+ ".jpg");
						BBSUtils.SaveBitmapToSDCard(this, bitmap, capturePath);
						//LogUtils.i("take_photo:" + capturePath);
						if (resultCode == RESULT_OK) {
							// notice:因为同一使用ImageLoader显示本地图片，所以本地上传的路径统一带上了前缀：fill://
							String str = Constants.MESSAGE_IMAGE_LINK_START
									+ Scheme.FILE.wrap(capturePath)
									+ Constants.MESSAGE_IMAGE_LINK_END;
							setMessageToChatList(str);
						}
					}
				} else {
					//LogUtils.e("take photo's bitmap is null~");
				}
			} else {
				//LogUtils.e("take photo's result is null~");
			}
		} else if (requestCode == Constants.PICKPHOTO) {
			if (resultCode == RESULT_OK) {
				ArrayList<CharSequence> msg = data
						.getCharSequenceArrayListExtra(GalleryActivity.GALLERY_RESULT_FULLPATH);
				for (CharSequence str : msg) {
					String send = Constants.MESSAGE_IMAGE_LINK_START
							+ Scheme.FILE.wrap((String) str)
							+ Constants.MESSAGE_IMAGE_LINK_END;
					setMessageToChatList(send);
				}
			}
		}
	}
	private void setMessageToChatList(String str) {
		//Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
		//还原输入框状态
		if (talkFragement != null) {
			talkFragement.closeInput();
		}
		MessageText messageText = new MessageText(LoginManager.getInstance()
				.getJidParsed(), LoginManager.getInstance().getJidParsed(),
				null, false, LoginManager.getInstance().getSysytemTimeDate());
		messageText.setMessage(str);
		messageText.setMsgState(MessageState.preloading, mChat);
		mListMessages.add(messageText);
		mMessagesListAdapter.notifyDataSetChanged();
	}

	/**
	 * @func 聊天消息监听器，处理聊天的消息
	 * @author yuedong bao
	 * @time 2014-11-26 下午7:31:36
	 */
	private class OnMessageListener extends IMessageListener.Stub {
		@Override
		public void processMessage(IChat chat, final Message msg)
				throws RemoteException {
			final String from = StringUtils.parseName(msg.getFrom());
			final String thisUser = StringUtils.parseName(mContact.getJid());
			//LogUtils.i("chat#receive msg:" + msg.getBody() + " from:" + from + " me:" + thisUser);
			if (thisUser.equals(from)) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (msg.getType() == Message.MSG_TYPE_ERROR) {
							mListMessages.add(new MessageText(from, mContact
									.getNickName(), msg.getBody(), true, msg
									.getTimestamp()));
							mMessagesListAdapter.notifyDataSetChanged();
						} else if (msg.getBody() != null) {
							if (msg.getBody() != null) {
								String body = msg.getBody();
								MessageText messageText = new MessageText(from,
										mContact.getNickName(), msg.getBody(),
										false, msg.getTimestamp());
								messageText.setMessage(body);
								messageText.setMsgState(
										MessageState.preloading, mChat);
								mListMessages.add(messageText);
							}
							mMessagesListAdapter.notifyDataSetChanged();
						}
						toSendLastMsg();
					}
				});
			}
		}
		/**
		 * {@inheritDoc}.
		 */
		@Override
		public void stateChanged(IChat chat) throws RemoteException {
			final String state = chat.getState();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					String text = null;
					if ("active".equals(state)) {
						text = ChatActivity.this
								.getString(R.string.chat_state_active);
					} else if ("composing".equals(state)) {
						text = ChatActivity.this
								.getString(R.string.chat_state_composing);
					} else if ("gone".equals(state)) {
						text = ChatActivity.this
								.getString(R.string.chat_state_gone);
					} else if ("inactive".equals(state)) {
						text = ChatActivity.this
								.getString(R.string.chat_state_inactive);
					} else if ("paused".equals(state)) {
						text = ChatActivity.this
								.getString(R.string.chat_state_active);
					}
				}
			});
		}
		@Override
		public void otrStateChanged(final String otrState)
				throws RemoteException {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					updateOtrInformations(otrState);
					mListMessages.add(new MessageText("", "", otrState, false));
					mMessagesListAdapter.notifyDataSetChanged();
				}
			});
		}
		@Override
		public void interceptMessage(IChat chat, Message msg)
				throws RemoteException {
		}
	}

	/** 消息列表适配器 */
	private class MessagesListAdapter extends BaseAdapter {
		private HashMap<MessageType, Integer> layoutMap;
		private Contact myContact = LoginManager.getInstance().getMyContact();

		/**
		 * Constructor.
		 */
		public MessagesListAdapter() {
		}
		/**
		 * Returns the number of messages contained in the messages list.
		 * @return The number of messages contained in the messages list.
		 */
		@Override
		public int getCount() {
			return mListMessages.size();
		}
		/**
		 * Return an item from the messages list that is positioned at the position passed by
		 * parameter.
		 * @param position The position of the requested item.
		 * @return The item from the messages list at the requested position.
		 */
		@Override
		public Object getItem(int position) {
			return mListMessages.get(position);
		}
		/**
		 * Return the id of an item from the messages list that is positioned at the position passed
		 * by parameter.
		 * @param position The position of the requested item.
		 * @return The id of an item from the messages list at the requested position.
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MessageType type = mListMessages.get(position).getMsgType();
			BaseHolder vh_base;
			if (convertView == null) {
				if (layoutMap == null) {
					layoutMap = new HashMap<ChatActivity.MessageType, Integer>();
					layoutMap.put(MessageType.MESSAGE_TYPE_OTHER_AUDIO,
							R.layout.message_audio_receive_template);
					layoutMap.put(MessageType.MESSAGE_TYPE_MINE_AUDIO,
							R.layout.message_audio_send_template);
					layoutMap.put(MessageType.MESSAGE_TYPE_OTHER_IMAGE,
							R.layout.message_image_receive_template);
					layoutMap.put(MessageType.MESSAGE_TYPE_MINE_IMAGE,
							R.layout.message_image_send_template);
					layoutMap.put(MessageType.MESSAGE_TYPE_OTHER_TEXT,
							R.layout.message_text_receive_template);
					layoutMap.put(MessageType.MESSAGE_TYPE_MINE_TEXT,
							R.layout.message_text_send_template);
					layoutMap.put(MessageType.MESSAGE_TYPE_SYSTEMPROMP,
							R.layout.message_systemprompt);
					layoutMap.put(MessageType.MESSAGE_TYPE_OTHER_HTML,
							R.layout.message_html_receive_template);
				}
				convertView = LayoutInflater.from(mContext).inflate(
						layoutMap.get(type), null);
				if (type == MessageType.MESSAGE_TYPE_OTHER_TEXT
						|| type == MessageType.MESSAGE_TYPE_MINE_TEXT) {
					vh_base = new TextHolder();
				} else if (type == MessageType.MESSAGE_TYPE_OTHER_AUDIO
						|| type == MessageType.MESSAGE_TYPE_MINE_AUDIO) {
					vh_base = new AudioHolder();
				} else if (type == MessageType.MESSAGE_TYPE_OTHER_IMAGE
						|| type == MessageType.MESSAGE_TYPE_MINE_IMAGE) {
					vh_base = new ImageHolder();
				} else if (type == MessageType.MESSAGE_TYPE_SYSTEMPROMP) {
					vh_base = new SystempromptHolder();
				} else if (type == MessageType.MESSAGE_TYPE_OTHER_HTML) {
					vh_base = new HtmlHolder();
				} else {
					vh_base = new HtmlHolder();
				}
				vh_base.fillHolder(convertView);
				convertView.setTag(vh_base);
			} else {
				vh_base = (BaseHolder) convertView.getTag();
			}
			vh_base.bindView(position);
			return convertView;
		}
		// 根据数据状态显隐进度条
		private void setProgressBarVisibility(MessageState state,
				BaseHolder holder) {
			switch (state) {
				case preloading:
				case loading: {
					holder.message_state_failed.setVisibility(View.INVISIBLE);
					holder.progressBar1.setVisibility(View.VISIBLE);
					break;
				}
				case failed: {
					holder.message_state_failed.setVisibility(View.VISIBLE);
					holder.progressBar1.setVisibility(View.INVISIBLE);
					break;
				}
				case success: {
					holder.message_state_failed.setVisibility(View.INVISIBLE);
					holder.progressBar1.setVisibility(View.INVISIBLE);
					break;
				}
				default:
					break;
			}
		}

		private abstract class BaseHolder {
			protected TextView msgDate;
			protected RelativeLayout msgDate_layout;
			protected ImageView message_state_failed;
			protected ProgressBar progressBar1;

			public void fillHolder(View convertView) {
				msgDate = (TextView) convertView
						.findViewById(R.id.message_tv_timestamp);
				msgDate_layout = (RelativeLayout) convertView
						.findViewById(R.id.message_layout_timecontainer);
				message_state_failed = (ImageView) convertView
						.findViewById(R.id.message_state_failed);
				progressBar1 = (ProgressBar) convertView
						.findViewById(R.id.progressBar1);
			}
			public void excuteHolderTask(final MessageText msgTxt) {
				// 设置进度条显隐
				setProgressBarVisibility(msgTxt.getMsgState(), this);
				// 设置上传失败后的点击事件
				message_state_failed.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!BeemApplication.isNetworkOk()) {
							CToast.showToast(mContext, "网络异常:未能连上服务器",
									Toast.LENGTH_SHORT);
						} else {
							msgTxt.setMsgState(MessageState.preloading, mChat);
							excuteHolderTask(msgTxt);
						}
					}
				});
			};
			public final void bindView(int position) {
				resetView(position);
				bindViewInner(position);
			};
			public abstract void bindViewInner(int position);
			public abstract void resetView(int position);
		}

		private class ImageHolder extends BaseHolder {
			private CircleImageView avatar;
			private ImageView message_image;

			@Override
			public void fillHolder(View convertView) {
				super.fillHolder(convertView);
				avatar = (CircleImageView) convertView
						.findViewById(R.id.message_iv_userphoto);
				message_image = (ImageView) convertView
						.findViewById(R.id.message_image);
			}
			@Override
			public void bindViewInner(int position) {
				final MessageText msgTxt = mListMessages.get(position);
				MessageType type = msgTxt.getMsgType();
				setMessageDate(this, msgTxt, position);
				Contact contact = type == MessageType.MESSAGE_TYPE_MINE_IMAGE ? myContact
						: mContact;
				ImageLoader.getInstance().displayImage(contact.getPhoto(),
						avatar,
						ImageLoaderConfigers.sexOpt[mContact.getSexInt()]);
				excuteHolderTask(msgTxt);
				if (type == MessageType.MESSAGE_TYPE_MINE_IMAGE) {
					ImageLoader.getInstance().displayImage(msgTxt.getMessage(),
							message_image, ImageLoaderConfigers.bubbleOpts[0]);
				} else {
					ImageLoader.getInstance().displayImage(msgTxt.getMessage(),
							message_image, ImageLoaderConfigers.bubbleOpts[1],
							new SimpleImageLoadingListener() {
								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									super.onLoadingComplete(imageUri, view,
											loadedImage);
									msgTxt.setMsgState(MessageState.success,
											mChat);
									excuteHolderTask(msgTxt);
								}
							});
				}
			}
			@Override
			public void excuteHolderTask(final MessageText msgTxt) {
				MessageType msgType = msgTxt.getMsgType();
				MessageState state = msgTxt.getMsgState();
				if (state == MessageState.preloading) {
					if (msgType == MessageType.MESSAGE_TYPE_MINE_IMAGE) {
						msgTxt.setMsgState(MessageState.loading, mChat);
						ImageFinalHttp.getInstance().uploadImageFile(
								Scheme.FILE.crop(msgTxt.getMessage()),
								new IMessageStateListener() {
									@Override
									public void onPostExecute(String url,
											MessageState state) {
										if (state == MessageState.success) {
											final String url_send = Constants.MESSAGE_IMAGE_LINK_START
													+ url
													+ Constants.MESSAGE_IMAGE_LINK_END;
											ThreadUtils.executeTask(new Runnable() {

												@Override
												public void run() {
													sendMessage(url_send);
												}
												
											});
											msgTxt.setMessage(url_send);
										}
										msgTxt.setMsgState(state, mChat);
										excuteHolderTask(msgTxt);
									}
								});
					}
				} else if (state == MessageState.success) {
					message_image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 点击查看大图
							ImageFolderItem folderItem = new ImageFolderItem();
							folderItem.setContact(null);
							ArrayList<VVImage> vvImages = new ArrayList<VVImage>();
							VVImage image = new VVImage();
							image.setPath(msgTxt.getMessage());
							image.setPathThumb(msgTxt.getMessage());
							vvImages.add(image);
							folderItem.setVVImages(vvImages);
							ImageGalleryActivity
									.launch(mContext, 0, folderItem);
						}
					});
				}
				super.excuteHolderTask(msgTxt);
			}
			@Override
			public void resetView(int position) {
			}
		}

		private class SystempromptHolder extends BaseHolder {
			private TextView system_prompt;

			@Override
			public void fillHolder(View convertView) {
				super.fillHolder(convertView);
				system_prompt = (TextView) convertView
						.findViewById(R.id.system_prompt);
			}
			@Override
			public void bindViewInner(int position) {
				MessageText msgTxt = mListMessages.get(position);
				setMessageDate(this, msgTxt, position);
				system_prompt.setText(msgTxt.getMessage());
			}
			@Override
			public void excuteHolderTask(MessageText msgTxt) {
			}
			@Override
			public void resetView(int position) {
			}
		}

		private class AudioHolder extends BaseHolder {
			private CircleImageView avatar;
			private TextView audio_duration;
			private View message_layout_messagecontainer;
			private ImageView audio_icon;
			private ProgressBar audio_play_progressBar;

			@Override
			public void fillHolder(View convertView) {
				super.fillHolder(convertView);
				avatar = (CircleImageView) convertView
						.findViewById(R.id.message_iv_userphoto);
				message_layout_messagecontainer = convertView
						.findViewById(R.id.message_layout_messagecontainer);
				audio_duration = (TextView) convertView
						.findViewById(R.id.audio_duration);
				audio_icon = (ImageView) convertView
						.findViewById(R.id.audio_icon);
				audio_play_progressBar = (ProgressBar) convertView
						.findViewById(R.id.audio_play_progressBar);
			}
			@Override
			public void resetView(int position) {
			}
			@Override
			public void bindViewInner(int position) {
				MessageText msgTxt = mListMessages.get(position);
				MessageType type = msgTxt.getMsgType();
				setMessageDate(this, msgTxt, position);
				Contact contact = type == MessageType.MESSAGE_TYPE_MINE_AUDIO ? myContact
						: mContact;
				ImageLoader.getInstance().displayImage(contact.getPhoto(),
						avatar,
						ImageLoaderConfigers.sexOpt[mContact.getSexInt()]);
				excuteHolderTask(msgTxt);
			}
			@Override
			public void excuteHolderTask(final MessageText msgTxt) {
				if (msgTxt.getMsgState() == MessageState.preloading) {
					msgTxt.setMsgState(MessageState.loading, mChat);
					if (msgTxt.getMsgType() == MessageType.MESSAGE_TYPE_OTHER_AUDIO) {
						// 别人发过来的音频文件
						AudioFinalHttp.getInstance().downloadAudio(
								msgTxt.getMessage(), new AjaxCallBack<File>() {
									@Override
									public void onFailure(Throwable t,
											int errorNo, String strMsg) {
										super.onFailure(t, errorNo, strMsg);
										msgTxt.setMsgState(MessageState.failed,
												mChat);
										CToast.showToast(
												BeemApplication.getContext(),
												"download the audio failed!",
												Toast.LENGTH_SHORT);
										//LogUtils.e("download the audio failed! t:" + t + " strMsg:" + strMsg + " errorNo:"
										//										+ errorNo);
										excuteHolderTask(msgTxt);
									}
									@Override
									public void onSuccess(File t) {
										super.onSuccess(t);
										msgTxt.setMsgState(
												MessageState.success, mChat);
										excuteHolderTask(msgTxt);
										//LogUtils.i("download the audio success!", Toast.LENGTH_SHORT);
									}
								});
					} else {
						// 自己上传的音频文件
						AudioFinalHttp.getInstance().uploadAudio(
								msgTxt.getMessage(),
								new IMessageStateListener() {
									@Override
									public void onPostExecute(String url,
											MessageState state) {
										if (state == MessageState.success) {
											// 发送到对端
											final String msg_send = Constants.MESSAGE_AUDIO_LINK_START
													+ url
													+ Constants.MESSAGE_AUDIO_LINK_SPLIT
													+ msgTxt.getRecordSecond()
													+ Constants.MESSAGE_AUDIO_LINK_END;
											ThreadUtils.executeTask(new Runnable() {

												@Override
												public void run() {
													sendMessage(msg_send);
												}
												
											});
											msgTxt.setMessage(msg_send);
										} else if (state == MessageState.failed) {
											CToast.showToast(BeemApplication
													.getContext(), "发送录音失败",
													Toast.LENGTH_SHORT);
											//LogUtils.i("uploat the audio failed! url:" + url);
										}
										msgTxt.setMsgState(state, mChat);
										excuteHolderTask(msgTxt);
									}
								});
					}
				}
				audio_duration.setText(msgTxt.getRecordSecond() + "\"");
				message_layout_messagecontainer
						.setOnClickListener(new OnClickListener() {
							// 点击播放音频文件
							@Override
							public void onClick(View v) {
								AudioFinalHttp.getInstance().startPlaying(
										msgTxt.getMessage(),
										new IAudioPlayListener() {
											@Override
											public void onCompletion() {
												// 播放结束回调
												audio_duration
														.setVisibility(View.VISIBLE);
												audio_icon.setSelected(false);
												audio_play_progressBar
														.setVisibility(View.GONE);
												audio_play_progressBar
														.setProgress(0);
											}
											@Override
											public void onStart() {
												// 播放开始
												audio_duration
														.setVisibility(View.GONE);
												audio_icon.setSelected(true);
												audio_play_progressBar
														.setVisibility(View.VISIBLE);
											}
											@Override
											public void onPlaying(
													final int current,
													final int all) {
												// 播放进度监听
												audio_play_progressBar
														.setProgress(current
																* 100 / all);
											}
										});
							}
						});
				super.excuteHolderTask(msgTxt);
			}
		}

		private class TextHolder extends BaseHolder {
			private CircleImageView avatar;
			private TextView msgText;

			@Override
			public void fillHolder(View convertView) {
				super.fillHolder(convertView);
				avatar = (CircleImageView) convertView
						.findViewById(R.id.message_iv_userphoto);
				msgText = (TextView) convertView
						.findViewById(R.id.message_chat_text);
			}
			@Override
			public void bindViewInner(int position) {
				MessageText msgTxt = mListMessages.get(position);
				MessageType type = msgTxt.getMsgType();
				setMessageDate(this, msgTxt, position);
				if (type == MessageType.MESSAGE_TYPE_OTHER_TEXT
						|| type == MessageType.MESSAGE_TYPE_MINE_TEXT) {
					Contact contact = type == MessageType.MESSAGE_TYPE_MINE_TEXT ? myContact
							: mContact;
					ImageLoader.getInstance().displayImage(contact.getPhoto(),
							avatar,
							ImageLoaderConfigers.sexOpt[mContact.getSexInt()]);
					// 显示聊天内容表情(如有表情自动转换)
					ExpressionUtil.showEmoteInListview(mContext, msgText,
							ExpressionSizeType.middle, msgTxt.getMessage());
				}
				excuteHolderTask(msgTxt);
			}
			@Override
			public void excuteHolderTask(MessageText msgTxt) {
				if (msgTxt.getMsgState() == MessageState.preloading) {
					if (!BeemApplication.isNetworkOk()) {
						msgTxt.setMsgState(MessageState.failed, mChat);
					} else {
						if (msgTxt.getMsgType() == MessageType.MESSAGE_TYPE_MINE_TEXT) {
							final String message = msgTxt.getMessage();
							ThreadUtils.executeTask(new Runnable() {

								@Override
								public void run() {
									sendMessage(message);
								}
								
							});
						}
						msgTxt.setMsgState(MessageState.success, mChat);
					}
				}
				super.excuteHolderTask(msgTxt);
			}
			@Override
			public void resetView(int position) {
			}
		}

		private class HtmlHolder extends BaseHolder {
			private WebView webView;

			@Override
			public void fillHolder(View convertView) {
				msgDate = (TextView) convertView
						.findViewById(R.id.message_tv_timestamp);
				msgDate_layout = (RelativeLayout) convertView
						.findViewById(R.id.message_layout_timecontainer);
				webView = (WebView) convertView.findViewById(R.id.msg_webview);
			}
			@Override
			public void bindViewInner(int position) {
				MessageText msgTxt = mListMessages.get(position);
				webView.loadUrl(msgTxt.getMessage());
				setMessageDate(this, msgTxt, position);
			}
			@Override
			public void excuteHolderTask(MessageText msgTxt) {
			}
			@Override
			public void resetView(int position) {
			}
		}

		@Override
		public int getItemViewType(int position) {
			MessageText messageText = mListMessages.get(position);
			int type = messageText.getMsgType().ordinal();
			return type;
		}
		@Override
		public int getViewTypeCount() {
			return MessageType.values().length;
		}
		//TODO
		private void setMessageDate(BaseHolder vh, MessageText messageText,
				int position) {
			if (messageText.getTimestamp() != null) {
				Date sysytemTimeDate = LoginManager.getInstance()
						.getSysytemTimeDate();
				Date timestamp = messageText.getTimestamp();
				SimpleDateFormat df = null;
				String date = null;
				long time = sysytemTimeDate.getTime();
				long stampTime = timestamp.getTime();
				long diffTime = time - stampTime;
				if (diffTime >= 0 && diffTime <= 1000 * 3600 * 24) {
					df = new SimpleDateFormat("HH:mm", Locale.getDefault());
					date = df.format(timestamp);
				} else if (diffTime > 1000 * 3600 * 24
						&& diffTime <= 1000 * 3600 * 48) {
					df = new SimpleDateFormat("HH:mm", Locale.getDefault());
					date = "昨天 " + df.format(timestamp);
				} else if (diffTime > 1000 * 3600 * 48
						&& diffTime <= 1000 * 3600 * 24 * 7) {
					df = new SimpleDateFormat("EEE HH:mm", Locale.getDefault());
					date = df.format(timestamp);
				} else if (diffTime > 1000 * 3600 * 24 * 7
						&& diffTime <= 1000 * 3600 * 24 * 365) {
					df = new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
					date = df.format(timestamp);
				} else {
					df = new SimpleDateFormat("yy年MM月dd日 HH:mm", Locale.getDefault());
					date = df.format(timestamp);
				}
				if (position == 0) {
					vh.msgDate_layout.setVisibility(View.VISIBLE);
					vh.msgDate.setText(date);
				} else {
					if ((stampTime - mListMessages.get(position - 1)
							.getTimestamp().getTime()) / 60000 <= 5) {
						vh.msgDate_layout.setVisibility(View.GONE);
					} else {
						vh.msgDate_layout.setVisibility(View.VISIBLE);
						vh.msgDate.setText(date);
					}
				}
			}
		}
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			mMessagesListView.getRefreshableView().setSelection(
					mMessagesListView.getRefreshableView().getCount() - 1);
		}
	}

	public enum MessageType {
		MESSAGE_TYPE_MINE_TEXT, MESSAGE_TYPE_MINE_IMAGE,
		MESSAGE_TYPE_MINE_AUDIO, MESSAGE_TYPE_OTHER_TEXT,
		MESSAGE_TYPE_OTHER_IMAGE, MESSAGE_TYPE_OTHER_AUDIO,
		MESSAGE_TYPE_SYSTEMPROMP, MESSAGE_TYPE_OTHER_HTML;
	}

	public enum MessageState {
		preloading, loading, failed, success;
	}

	/**
	 * Class which simplify an Xmpp text message.
	 * @author Jean-Manuel Da Silva <dasilvj at beem-project dot com>
	 */
	public static class MessageText {
		private String mBareJid;
		private String mName;
		private String mMessage;
		private boolean mIsError;
		private Date mTimestamp;
		private MessageType msgType;
		private MessageState msgState;
		private int recordSecond;// 录音时长

		/**
		 * Constructor.
		 * @param bareJid A String containing the bare JID of the message's author.
		 * @param name A String containing the name of the message's author.
		 * @param message A String containing the message.
		 */
		public MessageText(final String bareJid, final String name,
				final String message) {
			mBareJid = bareJid;
			mName = name;
			mMessage = message;
			mIsError = false;
		}
		/**
		 * Constructor.
		 * @param bareJid A String containing the bare JID of the message's author.
		 * @param name A String containing the name of the message's author.
		 * @param message A String containing the message.
		 * @param isError if the message is an error message.
		 */
		public MessageText(final String bareJid, final String name,
				final String message, final boolean isError) {
			mBareJid = bareJid;
			mName = name;
			mMessage = message;
			mIsError = isError;
		}
		/**
		 * Constructor.
		 * @param bareJid A String containing the bare JID of the message's author.
		 * @param name A String containing the name of the message's author.
		 * @param message A String containing the message.
		 * @param isError if the message is an error message.
		 * @param date the time of the message.
		 */
		public MessageText(final String bareJid, final String name,
				final String message, final boolean isError, final Date date) {
			mBareJid = bareJid;
			mName = name;
			mMessage = message;
			mIsError = isError;
			mTimestamp = date;
		}
		/**
		 * JID attribute accessor.
		 * @return A String containing the bare JID of the message's author.
		 */
		public String getBareJid() {
			return mBareJid;
		}
		public String getBareJidParsed() {
			return StringUtils.parseName(mBareJid);
		}
		/**
		 * Name attribute accessor.
		 * @return A String containing the name of the message's author.
		 */
		public String getName() {
			return mName;
		}
		/**
		 * Message attribute accessor.
		 * @return A String containing the message.
		 */
		public String getMessage() {
			return mMessage;
		}
		/**
		 * JID attribute mutator.
		 * @param bareJid A String containing the author's bare JID of the message.
		 */
		@SuppressWarnings("unused")
		public void setBareJid(String bareJid) {
			mBareJid = bareJid;
		}
		/**
		 * Name attribute mutator.
		 * @param name A String containing the author's name of the message.
		 */
		@SuppressWarnings("unused")
		public void setName(String name) {
			mName = name;
		}
		/**
		 * Message attribute mutator.
		 * @param message A String containing a message.
		 */
		private void setMessageInner(String message) {
			mMessage = message;
		}
		/**
		 * Get the message type.
		 * @return true if the message is an error message.
		 */
		public boolean isError() {
			return mIsError;
		}
		/**
		 * Set the Date of the message.
		 * @param date date of the message.
		 */
		public void setTimestamp(Date date) {
			mTimestamp = date;
		}
		/**
		 * Get the Date of the message.
		 * @return if it is a delayed message get the date the message was sended.
		 */
		public Date getTimestamp() {
			return mTimestamp;
		}
		public MessageType getMsgType() {
			return msgType;
		}
		public void setMsgType(MessageType msgType) {
			this.msgType = msgType;
		}
		public MessageState getMsgState() {
			return msgState;
		}
		public void setMsgState(MessageState msgState, IChat chat) {
			this.msgState = msgState;
			if (chat != null) {
				try {
					chat.setMessageState(msgState.ordinal(), getTimestamp()
							.getTime());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		public int getRecordSecond() {
			return recordSecond;
		}
		public void setRecordSecond(int recordSecond) {
			this.recordSecond = recordSecond;
		}
		public void setMessage(String body) {
			boolean isMyself = LoginManager.getInstance().isMyJid(mBareJid);
			if (body.startsWith(Constants.MESSAGE_AUDIO_LINK_START)
					&& body.endsWith(Constants.MESSAGE_AUDIO_LINK_END)) {
				// Audio
				String url_time = body.substring(
						Constants.MESSAGE_AUDIO_LINK_START.length(),
						body.indexOf(Constants.MESSAGE_AUDIO_LINK_END));
				String[] url_times = url_time
						.split(Constants.MESSAGE_AUDIO_LINK_SPLIT);
				setMessageInner(url_times[0]);
				setRecordSecond(Integer.parseInt(url_times[1]));
				setMsgType(isMyself ? MessageType.MESSAGE_TYPE_MINE_AUDIO
						: MessageType.MESSAGE_TYPE_OTHER_AUDIO);
			} else if (body.startsWith(Constants.MESSAGE_IMAGE_LINK_START)
					&& body.endsWith(Constants.MESSAGE_IMAGE_LINK_END)) {
				// Image
				String url = body.substring(
						Constants.MESSAGE_IMAGE_LINK_START.length(),
						body.indexOf(Constants.MESSAGE_IMAGE_LINK_END));
				setMessageInner(url);
				setMsgType(isMyself ? MessageType.MESSAGE_TYPE_MINE_IMAGE
						: MessageType.MESSAGE_TYPE_OTHER_IMAGE);
			} else {
				setMessageInner(body);
				setMsgType(isMyself ? MessageType.MESSAGE_TYPE_MINE_TEXT
						: MessageType.MESSAGE_TYPE_OTHER_TEXT);
			}
		}
	}

	/**
	 * This class is in charge of getting the new chat in the activity if someone talk to you.
	 */
	private class ChatManagerListener extends IChatManagerListener.Stub {
		/**
		 * Constructor.
		 */
		public ChatManagerListener() {
		}
		@Override
		public void chatCreated(IChat chat, boolean locally) {
			if (locally)
				return;
			try {
				String contactJid = mContact.getJIDCompleted();
				String chatJid = chat.getParticipant().getJIDCompleted();
				//LogUtils.i("chatCreated:contactJid:" + contactJid + " chatJid:" + chatJid);
				if (chatJid.equals(contactJid)) {
					// This should not be happened but to be sure
					if (mChat != null) {
						mChat.setOpen(false);
						mChat.removeMessageListener(mMessageListener);
					}
					mChat = chat;
					mChat.setOpen(true);
					mChat.addMessageListener(mMessageListener);
					mChatManager.deleteChatNotification(mChat);
				}
			} catch (RemoteException ex) {
				Log.e(TAG,
						"A remote exception occurs during the creation of a chat",
						ex);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		//LogUtils.i(" mBinded:" + mBinded);
		if (draftInfo != null) {
			talkFragement.setContent(draftInfo.getContent());
		}
		if (!mBinded) {
			bindService(SERVICE_INTENT, mConn, BIND_AUTO_CREATE);
			mBinded = true;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			SaveUnSendMSG();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (data.getAction() == EventAction.SendPathTChat) {
			String msg = (String) data.getMsg();
			String str = Constants.MESSAGE_IMAGE_LINK_START
					+ Scheme.FILE.wrap(msg) + Constants.MESSAGE_IMAGE_LINK_END;
			setMessageToChatList(str);
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}

	// listview下拉刷新监听器
	private OnRefreshListener<ListView> xlistViewLis = new OnRefreshListener<ListView>() {
		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView, PullType pullType) {
			pullHistoryAsyn(ITEMS_PAGE, new OnHistroyResultLis() {
				@Override
				public void onResut(List<MessageText> msgTestDBs) {
					if (msgTestDBs.isEmpty()) {
						CToast.showToast(ChatActivity.this, "没有更多历史消息了",
								Toast.LENGTH_SHORT);
					} else {
						// 去重；
						for (int i = msgTestDBs.size() - 1 ; i >=0; i--) {
							boolean flag = false;
							MessageText mt = msgTestDBs.get(i);
							for (MessageText mt1 : mListMessages) {
								if (mt1.getTimestamp().equals(mt.getTimestamp())) {
									flag = true;
									break;
								}
							}
							if (!flag)
								mListMessages.add(0, mt);
						}
//						mListMessages.addAll(0, msgTestDBs);
						mMessagesListAdapter.notifyDataSetChanged();
						
					}
					mMessagesListView.onPullRefreshComplete();
				}
			});
		}
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView,
				PullType pullType) {
		}
	};

	private interface OnHistroyResultLis {
		// 运行在主线程
		public void onResut(List<MessageText> msgTestDBs);
	}

	/**
	 * @Title: pullHistoryAsyn
	 * @Description: 异步获取count条历史数据
	 * @param: @param historyCount
	 * @param: @param resultLis
	 * @return: void
	 * @throws:
	 */
	public void pullHistoryAsyn(int count, final OnHistroyResultLis resultLis) {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				long lastTimeMills = System.currentTimeMillis();
				// 从数据库中获取早先的15条消息
				String jid_send = mContact.getJIDParsed();
				String jid_receive = LoginManager.getInstance().getJidParsed();
				String lt_msgTime = "9";
				if (mListMessages.size() > 0) {
					lt_msgTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(mListMessages.get(0).getTimestamp());
				}
				List<Message> messages = ChatDB.querySome(jid_send,
						jid_receive, lt_msgTime, ITEMS_PAGE);
				final List<MessageText> msgTestDBs = convertMessagesList(messages);
				long nowTimeMillis = System.currentTimeMillis();
				int leastLoadMills = 800;
				if (nowTimeMillis - lastTimeMills >= leastLoadMills) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							resultLis.onResut(msgTestDBs);
						}
					});
				} else {
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							resultLis.onResut(msgTestDBs);
						}
					}, leastLoadMills - (nowTimeMillis - lastTimeMills));
				}
			}
		});
	}
}
