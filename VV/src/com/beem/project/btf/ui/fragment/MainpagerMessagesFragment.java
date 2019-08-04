/**
 * 
 */
package com.beem.project.btf.ui.fragment;

import java.util.List;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.XmppFacade;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.activity.CommentNotifiesActivity;
import com.beem.project.btf.ui.activity.FriendRequestActivity;
import com.beem.project.btf.ui.activity.LikedNotifiesActivity;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;
import com.beem.project.btf.ui.views.SessionHeanLineHandler;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.butterfly.vv.adapter.SessionsImAdapter;
import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.butterfly.vv.db.ormhelper.bean.CommentNotifies;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.db.ormhelper.bean.LikedNotifies;
import com.pullToRefresh.ui.PullToRefreshListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.beem.project.btf.service.MessageManager;

/**
 * @author hongbo ke
 */
public class MainpagerMessagesFragment extends MainpagerAbstractFragment implements OnClickListener {
	private View view;
	private PullToRefreshListView messageListView;
	private SessionsImAdapter sessionsImAdapter;
	private IXmppFacade mXmppFacade;
	private View network_invalid_layout;
	private TextView tvw_TimeflyLoginStatus;
	private MessageManager messageManager;
	private View newCommentsView;
	private View newLikedView;
	private View newFriendRequestView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regristerBroadReceiver();
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mXmppFacade = BeemServiceHelper.getInstance(activity).getXmppFacade();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
		} else {
			view = inflater.inflate(R.layout.gps_search_grid, null);
			network_invalid_layout = view.findViewById(R.id.network_invalid_layout);
			tvw_TimeflyLoginStatus = (TextView) view
					.findViewById(R.id.tvw_TimeflyLoginStatus);
			messageListView = (PullToRefreshListView) view
					.findViewById(R.id.sessionlist);
			messageListView.setPullRefreshEnabled(false);
			messageListView.setPullLoadEnabled(false);
			messageListView.setListViewDivider();
			setupHeaderViews(inflater);
			/*messageListView.getEmptydataProcessView().setEmptydataImg(
				R.drawable.timefly_session_nopic);
			messageListView.getEmptydataProcessView().setloadEmptyBtn("附近人",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							// 切换到附近的人
							callback.onSwitchFragment(TabName.FRIEND);
						}
					});
			messageListView.getEmptydataProcessView().setloadEmptyText(
					"你还没有任何消息记录,看看附近的人吧..."); */
			sessionsImAdapter = new SessionsImAdapter(mContext,
					messageListView.getRefreshableView());
			messageListView.getRefreshableView().setAdapter(sessionsImAdapter);
			if (mXmppFacade != null && mXmppFacade instanceof XmppFacade) {
				messageManager = ((XmppFacade) mXmppFacade).getMessageManager();
			}
			// 添加Top bar及Top bar监听器
			new SessionHeanLineHandler(mContext, view, sessionsImAdapter);
		}
		showLoginStatus();
		return view;
	}
	
	private void setupHeaderViews(LayoutInflater inflater) {
		newLikedView = generalHeaserView(inflater, R.drawable.message_likeds, getString(R.string.share_like));
		newLikedView.setOnClickListener(this);
		newCommentsView = generalHeaserView(inflater, R.drawable.message_commentlist, getString(R.string.share_comment));
		newCommentsView.setOnClickListener(this);
		newFriendRequestView = generalHeaserView(inflater, R.drawable.message_friendrequest, getString(R.string.messagenotify_friendrequest));
		newFriendRequestView.setOnClickListener(this);
		messageListView.getRefreshableView().addHeaderView(newLikedView);
		messageListView.getRefreshableView().addHeaderView(newCommentsView);
		messageListView.getRefreshableView().addHeaderView(newFriendRequestView);
	}
	private View generalHeaserView(LayoutInflater inflater,int imgResId, String title) {
		View view = inflater.inflate(R.layout.message_item_header, null);
		TextView tvw_title = (TextView) view.findViewById(R.id.tvw_title);
		ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
		tvw_title.setText(title);
		avatar.setImageResource(imgResId);
		return view;
	}
	
	@Override
	public void onStart() {
		loadMessageData();
		loadNofifyData(FriendRequest.class);
		loadNofifyData(CommentNotifies.class);
		loadNofifyData(LikedNotifies.class);
		super.onStart();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregristerBroadReceiver();
	}
	public void onEventMainThread(final EventBusData data) {
		switch (data.getAction()) {
			case LOGIN_TIMEOUT:
				showLoginStatus();
				break;
			case LOGIN_FAILED:
				showLoginStatus();
				break;
			case NETWORK_ACTIVE:
				showLoginStatus();
				break;
		}
	}
	public void onReceivedNewMessage() {
		
	}
	private void showLoginStatus() {
		boolean networkOK = BeemApplication.isNetworkOk();
		boolean logined = LoginManager.getInstance().isLogined();
		network_invalid_layout.setVisibility((!networkOK || !logined) ? View.VISIBLE
						: View.GONE);
		if (!logined) {
			tvw_TimeflyLoginStatus.setText(R.string.timefly_unlogin);
		} else if (!networkOK) {
			tvw_TimeflyLoginStatus.setText(R.string.timefly_network_failed);
		}
	}
	@Override
	public void autoAuthentificateCompleted() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onClick(View v) {
		if (v == newLikedView) {
			Intent i = new Intent(mContext, LikedNotifiesActivity.class);
			startActivity(i);
		} else if (v == newCommentsView) {
			Intent i = new Intent(mContext, CommentNotifiesActivity.class);
			startActivity(i);
		} else if (v == newFriendRequestView) {
			Intent i = new Intent(mContext, FriendRequestActivity.class);
			startActivity(i);
		}
		
	}
	
	private void regristerBroadReceiver() {
		// 消息数量接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgType.chat.toString());
		filter.addAction(MsgType.friend_require.toString());
		filter.addAction(MsgType.comment.toString());
		filter.addAction(MsgType.like.toString());
		LocalBroadcastManager.getInstance(mContext).registerReceiver(sessionModelReceiver, filter);
	}
	private void unregristerBroadReceiver() {
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(sessionModelReceiver);
	}
	
	private VVBaseBroadCastReceiver sessionModelReceiver = new VVBaseBroadCastReceiver(
			true) {
		@Override
		public void onReceive(Context context, Intent intent) {
			Item item = intent.getParcelableExtra("item");
			// LogUtils.i("received message:" + item.getMessage() + " jid:" +
			// item.getJidParsed() + " unReadMsgCount:"
			// + item.getUnReadMsgCount());
			((XmppFacade) mXmppFacade).getMessageManager().checkMessages();
			switch (item.getMsgtype()) {
				case chat:
					loadMessageData();
					break;
				case friend_require:
					loadNofifyData(FriendRequest.class);
					break;
				case comment:
					loadNofifyData(CommentNotifies.class);
					break;
				case like:
					loadNofifyData(LikedNotifies.class);
					break;
				default:
					break;
			}
		}
	};
	
	private void loadMessageData() {
		new VVBaseLoadingDlg<List<Item>>(new VVBaseLoadingDlgCfg(mContext)
				.setBindXmpp(true).setShowWaitingView(true)) {
			@Override
			protected List<Item> doInBackground() {
				if (messageManager != null) {
					messageManager.loadMessageItems();
					return messageManager.getAllMessages();
				}
				return null;
			}

			@Override
			protected void onTimeOut() {
				
				super.onTimeOut();
			}

			@Override
			protected void onPostExecute(List<Item> result) {
				if (result != null) {
					sessionsImAdapter.clearItems();
					sessionsImAdapter.addItems(result);
					sessionsImAdapter.notifyDataSetChanged();
				}
			}
		}.execute();
	}
	
	private <T extends BaseDB> void loadNofifyData(final Class<T> clazz) {
		new VVBaseLoadingDlg<Integer>(new VVBaseLoadingDlgCfg(mContext)
				.setBindXmpp(true).setShowWaitingView(true)) {
			@Override
			protected Integer doInBackground() {
				if (messageManager != null) {
					return messageManager.loadUnreadMessageCount(clazz);
				}
				return 0;
			}

			@Override
			protected void onTimeOut() {
				
				super.onTimeOut();
			}

			@Override
			protected void onPostExecute(Integer result) {
				if (result != null) {
					if (clazz.equals(FriendRequest.class)) {
						TextView tvw = (TextView) newFriendRequestView.findViewById(R.id.msg_count);
						tvw.setVisibility(result > 0 ? View.VISIBLE : View.GONE);
						tvw.setText(String.valueOf(result));
					} else if (clazz.equals(CommentNotifies.class)) {
						TextView tvw = (TextView) newCommentsView.findViewById(R.id.msg_count);
						tvw.setVisibility(result > 0 ? View.VISIBLE : View.GONE);
						tvw.setText(String.valueOf(result));
					} if (clazz.equals(LikedNotifies.class)) {
						TextView tvw = (TextView) newLikedView.findViewById(R.id.msg_count);
						tvw.setVisibility(result > 0 ? View.VISIBLE : View.GONE);
						tvw.setText(String.valueOf(result));
					}
				}
			}
		}.execute();
	}
}
