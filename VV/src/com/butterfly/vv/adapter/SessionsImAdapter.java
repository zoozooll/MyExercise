package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.MessageManager;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.service.XmppFacade;
import com.beem.project.btf.ui.ApplyChoiceActivity;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.ShareRankingActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ExpressionUtil;
import com.beem.project.btf.utils.ExpressionUtil.ExpressionSizeType;
import com.beem.project.btf.utils.SortedList;
import com.beem.project.btf.utils.SortedList.Equalable;
import com.btf.push.AddRosterPacket.Operation;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.db.ormhelper.bean.MessageDB;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.view.CircleImageView;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.teleca.jamendo.api.WSError;

/**
 * 消息模块适配器
 */
public class SessionsImAdapter extends CommonPhotoAdapter<Item> {
	// 记录当前列表各子项的删除状态
	private boolean isDelState = false;
	// 处理选项数目的接口
	private SelectListener selectLis;
	// 条目更改监听接口
	private ItemChangedListener itemChangedListener;
	private Context mContext;
	private DisplayImageOptions comment_img_options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.comment_img_deafult)
			.showImageForEmptyUri(R.drawable.comment_img_deafult)
			.showImageOnFail(R.drawable.comment_img_deafult)
			.cacheInMemory(true).cacheOnDisk(true).build();
	private SparseArray<ViewHolder> vhHolders = new SparseArray<SessionsImAdapter.ViewHolder>();

	public SelectListener getSelectLis() {
		return selectLis;
	}
	public void setSelectLis(SelectListener selectLis) {
		this.selectLis = selectLis;
	}

	public interface SelectListener {
		void select(boolean isSelectMod, int selecSum, int NumAll);
	}

	public SessionsImAdapter(Context c, AdapterView<?> listView) {
		super(listView);
		this.mContext = c;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.sessionlistcontact, parent, false);
			vh.nickName = (TextView) convertView.findViewById(R.id.contactlistpseudo);
			vh.headPhoto = (ImageView) convertView.findViewById(R.id.avatar);
			vh.sessiondate = (TextView) convertView.findViewById(R.id.sessiondate);
			vh.msg_count = (TextView) convertView.findViewById(R.id.msg_count);
			vh.right_ifo_wraper = (RelativeLayout) convertView.findViewById(R.id.right_ifo_wraper);
			vh.msg_list = (ViewGroup) convertView.findViewById(R.id.msg_list);
			vh.session_delselect = (LinearLayout) convertView.findViewById(R.id.session_delselect);
			vh.offlineMsg = (TextView) convertView.findViewById(R.id.offlineMsg);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			vhHolders.clear();
		}
		vhHolders.put(position, vh);
		bindView(convertView, position, vh);
		return convertView;
	}

	private class ViewHolder {
		private TextView offlineMsg;
		private TextView nickName;
		private ImageView headPhoto;
		private TextView sessiondate;
		private TextView msg_count;
		private RelativeLayout right_ifo_wraper;
		private ViewGroup msg_list;
		private LinearLayout session_delselect;
	}

	@Override
	public void addItem(Item item) {
		//LogUtils.i("item.toString:" + item.toString());
		for (Iterator<Item> it = getItems().iterator(); it.hasNext();) {
			Item itemOne = it.next();
			if (MessageManager.isReviewItem(item, itemOne)) {
				it.remove();
				itemOne.setUnReadMsgCount(item.getUnReadMsgCount());
				itemOne.setMessage(item.getMessage());
				itemOne.setTimestamp(item.getTimestamp());
				itemOne.setMsgTypeSub(item.getMsgTypeSub());
				item = itemOne;
				break;
			}
		}
		super.addItem(item);
	}
	@Override
	public void removeItem(Item item) {
		if (getItems().indexOf(item) > -1) {
			removeItem(item);
		}
	}
	public void removeItemAtIndex(int index) {
		removeItem(index);
	}
	public void clearItem() {
		clearItems();
	}
	protected void bindView(View view, final int position, final ViewHolder vh) {
		Item item = getItem(position);
		vh.nickName.setText(item.getNickname());
		vh.headPhoto.setOnClickListener(new VcardListener(position));
		ImageLoader.getInstance().displayImage(item.getPhoto(), vh.headPhoto,
				ImageLoaderConfigers.sexOpt[item.getSexInt()]);
		vh.sessiondate.setText(BBSUtils.getTimeDurationString(item
				.getTimestamp()));
		setupViewByMsgType(position, vh);
		// 长按选中列表项及单击选取或取消选择
		vh.session_delselect.setVisibility(item.getDelViewVisibility());
		vh.msg_list.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View paramView) {
				isDelState = true;
				setMaskVisible(
						position,
						vh.session_delselect.getVisibility() == View.VISIBLE ? View.GONE
								: View.VISIBLE, vh.session_delselect,
						isDelState);
				return true;
			}
		});
		vh.msg_list.setOnClickListener(new AdapterClickListener(position, vh));
	}
	private void setupViewByMsgType(int position, final ViewHolder vh) {
		final Item item = getItem(position);
		switch (item.getMsgtype()) {
			case chat: {
				vh.right_ifo_wraper.setVisibility(View.VISIBLE);
				vh.msg_count.setVisibility(View.VISIBLE);
				if (item.getMsgTypeSub() == MsgTypeSub.html) {
					vh.offlineMsg.setText("[时光机新闻]" + item.getSubject());
				} else if (item.getMsgTypeSub() == MsgTypeSub.audio) {
					vh.offlineMsg.setText("[语音]");
				} else if (item.getMsgTypeSub() == MsgTypeSub.image) {
					vh.offlineMsg.setText("[图片]");
				} else {
					ExpressionUtil.showEmoteInListview(mContext, vh.offlineMsg,
							ExpressionSizeType.middle, item.getMessage());
				}
				vh.msg_count
						.setVisibility(item.getUnReadMsgCount() < 1 ? View.GONE
								: View.VISIBLE);
				vh.msg_count.setText(String.valueOf(item.getUnReadMsgCount()));
				break;
			}
			case friend_require: {
				vh.right_ifo_wraper.setVisibility(View.VISIBLE);
				vh.msg_count
						.setVisibility(item.getUnReadMsgCount() < 1 ? View.GONE
								: View.VISIBLE);
				vh.msg_count.setText(String.valueOf(item.getUnReadMsgCount()));
				vh.offlineMsg.setText(item.getMessage());
				MsgTypeSub subMsgType = item.getMsgTypeSub();
				
				break;
			}
			case comment: {
				vh.right_ifo_wraper.setVisibility(View.GONE);
				vh.offlineMsg.setText(item.getMessage());
				break;
			}
			default:
				break;
		}
	}

	private class AdapterClickListener implements OnClickListener {
		private int position;
		private ViewHolder vh;

		public AdapterClickListener(int position, ViewHolder vh) {
			super();
			this.position = position;
			this.vh = vh;
		}
		@Override
		public void onClick(final View v) {
			if (isDelState) {
				// 进入选择状态
				setMaskVisible(
						position,
						vh.session_delselect.getVisibility() == View.VISIBLE ? View.GONE
								: View.VISIBLE, vh.session_delselect,
						isDelState);
			} else {
				if (v == vh.msg_list) {
					getItem(position).setUnReadMsgCount(0);
					vh.msg_count.setVisibility(View.GONE);
					MessageDB.readMessage(getItem(position));
					switch (getItem(position).getMsgtype()) {
						case chat: {
							ChatActivity.launch(mContext, getItem(position)
									.toContact());
							break;
						}
						case friend_require: {
							ApplyChoiceActivity.launch(((Activity) mContext),
									getItem(position), Constants.ISACCEPT);
							break;
						}
						case comment: {
//							ShareRankingActivity.launch(mContext,
//									getItem(position));
							Item item = getItem(position);
							Intent i = new Intent(mContext, ShareRankingActivity.class);
							i.putExtra("jid", item.getGidJid());
							i.putExtra("gid", item.getGid());
							i.putExtra("gidCreatTime", item.getGidCreateTime());
							mContext.startActivity(i);
							break;
						}
						default: {
							// 已拒绝或已同意都可以聊天
							// ChatActivity.launch(mContext, getItem(position).toContact());
							break;
						}
					}
				} 
			}
		}
	}

	public void setMaskVisible(int position, int visibility, View optView,
			Boolean flag) {
		getItem(position).setDelViewVisibility(visibility);
		optView.setVisibility(getItem(position).getDelViewVisibility());
		if (selectLis != null) {
			int selectSum = 0;
			int length = getItems().size();
			for (int i = 0; i < length; i++) {
				if (getItem(i).getDelViewVisibility() == View.VISIBLE) {
					selectSum++;
				}
			}
			selectLis.select(flag, selectSum, length);
			if (selectSum == 0) {
				//如果当前没有选择的,取消cancel
				Cancel();
			}
		}
	}

	private class VcardListener implements OnClickListener {
		private int vposition;

		public VcardListener(int vposition) {
			this.vposition = vposition;
		}
		@Override
		public void onClick(View v) {
			if (!isDelState) {
				Contact c = new Contact();
				c.setJid(getItem(vposition).getJid());
				c.setNickName(getItem(vposition).getNickname());
				ContactInfoActivity.launch(mContext, c);
			}
		}
	}

	// 删除
	public void setDelState(boolean isDelState) {
		this.isDelState = isDelState;
	}
	public void Cancel() {
		delCancel();
		isDelState = false;
		if (selectLis != null) {
			selectLis.select(isDelState, 0, 0);
		}
	}
	public void delCancel() {
		int length = getItems().size();
		for (int i = 0; i < length; i++) {
			getItem(i).setDelViewVisibility(View.GONE);
			vhHolders.get(i).session_delselect.setVisibility(View.GONE);
		}
		if (selectLis != null) {
			int selectSum = 0;
			for (int i = 0; i < length; i++) {
				if (getItem(i).getDelViewVisibility() == View.VISIBLE) {
					selectSum++;
				}
			}
			selectLis.select(isDelState, selectSum, length);
		}
	}
	public void delSelcetAll() {
		int length = getItems().size();
		for (int i = 0; i < length; i++) {
			getItem(i).setDelViewVisibility(View.VISIBLE);
		}
		if (selectLis != null) {
			int selectSum = 0;
			for (int i = 0; i < length; i++) {
				if (getItem(i).getDelViewVisibility() == View.VISIBLE) {
					selectSum++;
				}
			}
			selectLis.select(isDelState, selectSum, length);
		}
		notifyDataSetChanged();
	}
	public void delSessionByPos() {
		new VVBaseLoadingDlg<Void>(
				new VVBaseLoadingDlgCfg(mContext).setBindXmpp(true)) {
			@Override
			protected Void doInBackground() {
				XmppFacade facade = (XmppFacade) mDlgXmppFacade;
				// 删除数据库数据
				for (Iterator<Item> iterator = getItems().iterator(); iterator
						.hasNext();) {
					Item item = iterator.next();
					if (item.getDelViewVisibility() == View.VISIBLE) {
						facade.getMessageManager().deleteItem(
								item.getJidParsed(),
								LoginManager.getInstance().getJidParsed(),
								item.getMsgtype(), item.getGid(),
								item.getGidCreateTime());
						iterator.remove();
					}
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				notifyDataSetChanged();
				Cancel();
			}
		}.execute();
	}
	@Override
	public List<Item> initItems() {
		return new SortedList<Item>(new ArrayList<Item>(),
				new Comparator<Item>() {
					@Override
					public int compare(Item lhs, Item rhs) {
						return rhs.getTimestamp().compareTo(lhs.getTimestamp());
					}
				}, new Equalable<Item>() {
					@Override
					public boolean equal(Item t1, Item t2) {
						if (t1.getMsgtype() == t2.getMsgtype()) {
							if (t1.getJidParsed().equals(t2.getJidParsed())) {
								return true;
							}
						}
						return false;
					}
				});
	}
	public boolean isDelState() {
		return isDelState;
	}
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (itemChangedListener != null) {
			itemChangedListener.ItemChanged(getCount());
		}
	}

	public interface ItemChangedListener {
		public void ItemChanged(int count);
	}

	public void setItemChangedListener(ItemChangedListener itemChangedListener) {
		this.itemChangedListener = itemChangedListener;
	}
	// 阅读聊天Item并且刷新VhHolders
	public void readChatItemAndRefresh(Item item) {
		for (int i = 0; i < getCount(); i++) {
			if (item.getJidParsed().equals(getItem(i).getJidParsed())
					&& item.getMsgtype() == getItem(i).getMsgtype()) {
				getItem(i).setUnReadMsgCount(0);
				vhHolders.get(i).msg_count.setVisibility(View.GONE);
			}
		}
	}
	
	
}
