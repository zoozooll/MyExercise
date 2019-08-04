/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.ui.views.BottomPopupWindow;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionListener;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionType;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.vv.adapter.CommonPhotoAdapter;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.Debug;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.teleca.jamendo.dialog.LoadingDialog;

/**
 * @func 黑名单Activity
 * @author yuedong bao
 * @time 2014-11-27 下午3:18:27
 */
public class BlacklistActivity extends VVBaseActivity {
	private PullToProcessStateListView mListView;
	private ImageAdapter imageAdapter;
	private TextView contacts_textView2;
	private CustomTitleBtn editbutton;
	private TextView text_remove_tip;
	private boolean isTip = true;
	// 标示当前是否进入选择状态
	private boolean isSelectingState = false;
	// 正在加载进度条
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private BeemServiceHelper helper;
	private CustomTitleBtn back;
	private BottomPopupWindow bottomPopupWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_search_refresh_listview);
		// 返回按键
		back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setText("返回").setImgResource(R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSelectingState) {
					setSelectingState(false);
					return;
				}
				BlacklistActivity.this.finish();
			}
		});
		editbutton = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		editbutton.setText("").setImgVisibility(View.GONE)
				.setViewPaddingRight().setVisibility(View.VISIBLE);
		editbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isSelectingState) {
					editbutton.setText(imageAdapter.isSelectAll() ? "全不选"
							: "全选");
					imageAdapter.selectOrCancelAll(!imageAdapter.isSelectAll());
				}
			}
		});
		contacts_textView2 = (TextView) getWindow().findViewById(
				R.id.topbar_title);
		contacts_textView2.setText("黑名单");
		text_remove_tip = (TextView) findViewById(R.id.text_remove_tip);
		text_remove_tip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				text_remove_tip.setVisibility(View.GONE);
				isTip = false;
			}
		});
		mListView = (PullToProcessStateListView) findViewById(R.id.app_list);
		mListView.getRefreshableView().setDivider(
				getResources().getDrawable(R.drawable.divider_line));
		mListView.setPullLoadEnabled(false);
		mListView.setPullRefreshEnabled(false);
		mListView.getEmptydataProcessView().setEmptydataImg(
				R.drawable.blacklist_empty);
		mListView.getEmptydataProcessView().setloadEmptyText("没有你讨厌的人哟");
		imageAdapter = new ImageAdapter(this, mListView.getRefreshableView());
		mListView.setAdapter(imageAdapter);
		// 显示选中了几条列表项
		imageAdapter.setSelectLis(new SelectListener() {
			@Override
			public void onSelectSome(int selecSum) {
				back.setText("已选择" + selecSum + "项");
				editbutton.setText(imageAdapter.isSelectAll() ? "全不选" : "全选");
				if (selecSum == 0 && bottomPopupWindow != null) {
					bottomPopupWindow.dismiss();
				} else if (selecSum > 0 && bottomPopupWindow != null) {
					bottomPopupWindow.showAtLocation(
							findViewById(R.id.blacklist_wraper), Gravity.BOTTOM
									| Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			}
		});
		imageAdapter.setItemChangedListener(new ItemChangedListener() {
			@Override
			public void ItemChanged(int count) {
				if (count == 0) {
					// 数据条目为空显示空数据布局
					mListView.setProcessState(ProcessState.Emptydata);
					text_remove_tip.setVisibility(View.GONE);
				} else {
					mListView.setProcessState(ProcessState.Succeed);
					if (isTip) {
						text_remove_tip.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		setSelectingState(isSelectingState);
	}

	private PopupActionListener itemsOnClick = new PopupActionListener() {
		@Override
		public void itemsClick(PopupActionType type, int i) {
			bottomPopupWindow.dismiss();
			if (type == PopupActionType.BLACKLIST_DEL) {
				if (i == 0) {
					String[] selectJids = new String[imageAdapter.selectPosSp
							.size()];
					for (int j = 0; j < selectJids.length; j++) {
						selectJids[j] = imageAdapter.getItem(
								(imageAdapter.selectPosSp.get(j))).getJid();
					}
					// 移除黑名单
					new DistanceUsersDialog(BlacklistActivity.this,
							imageAdapter.selectPosSp, selectJids).execute();
				}
			}
		}
	};

	public void setSelectingState(boolean isSelecting) {
		if (!isSelecting) {
			imageAdapter.selectOrCancelAll(false);
			back.setText("返回");
			contacts_textView2.setText("黑名单");
			if (bottomPopupWindow != null) {
				bottomPopupWindow.dismiss();
			}
			editbutton.setText("");
			editbutton.setEnabled(false);
		} else {
			back.setText("已选择0项");
			contacts_textView2.setText("");
			editbutton.setText("全选");
			editbutton.setEnabled(true);
			text_remove_tip.setVisibility(View.GONE);
		}
		this.isSelectingState = isSelecting;
	}
	@Override
	protected void onStart() {
		super.onStart();
		/*helper = new BeemServiceHelper(mContext, new IBeemServiceConnection() {
			@Override
			public void onServiceDisconnectAct(IXmppFacade xmppFacade, ComponentName name) {
			}
			@Override
			public void onServiceConnectAct(IXmppFacade xmppFacade, ComponentName name, IBinder service) {
				
			}
		});
		helper.bindBeemService();*/
		// 获取黑名单
		List<Contact> contactlist = ContactService.getInstance().getBlacklist();
		imageAdapter.clearItems();
		imageAdapter.addItems(contactlist);
		imageAdapter.notifyDataSetChanged();
	};
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	// 处理选项数目的接口
	public interface SelectListener {
		void onSelectSome(int selecSum);
	}

	// 处理数据条目变化的接口
	public interface ItemChangedListener {
		public void ItemChanged(int count);
	}

	public class ImageAdapter extends CommonPhotoAdapter<Contact> {
		private final SparseIntArray selectPosSp = new SparseIntArray();
		private SelectListener selectLis;
		private ItemChangedListener itemChangedListener;
		private Context mContext;

		public ImageAdapter(Context c, AdapterView<?> listView) {
			super(listView);
			mContext = c;
		}
		public void setSelectLis(SelectListener selectLis) {
			this.selectLis = selectLis;
		}
		public void setItemChangedListener(
				ItemChangedListener itemChangedListener) {
			this.itemChangedListener = itemChangedListener;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public boolean isSelectAll() {
			return isSelectingState && selectPosSp.size() == getCount();
		}
		public boolean isSelect() {
			return isSelectingState && selectPosSp.size() > 0;
		}
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
			if (itemChangedListener != null) {
				itemChangedListener.ItemChanged(getCount());
			}
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.friendtlistcontact,
						parent, false);
				vh.age = (TextView) convertView
						.findViewById(R.id.profile_tv_agenew);
				vh.nickname = (TextView) convertView
						.findViewById(R.id.contactlistpseudo);
				vh.jid = (TextView) convertView
						.findViewById(R.id.contactlistmsgperso);
				vh.headPhoto = (ImageView) convertView
						.findViewById(R.id.avatar);
				vh.zone = (ImageView) convertView.findViewById(R.id.vvzone);
				vh.list = (RelativeLayout) convertView.findViewById(R.id.list);
				vh.zhezao = (RelativeLayout) convertView
						.findViewById(R.id.zhezao);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			bindView(convertView, position, vh);
			return convertView;
		}

		private class ViewHolder {
			public TextView jid;
			public TextView age;
			public TextView nickname;
			public ImageView headPhoto;
			public ImageView zone;
			public RelativeLayout list;
			public RelativeLayout zhezao;
		}

		// 设置列表项遮罩同时记录选中个数
		public void setMaskVisible(int position, boolean isVisible, View optView) {
			for (int i = 0; i < selectPosSp.size(); i++) {
				//LogUtils.i("selectPosSp key:" + selectPosSp.keyAt(i) + "->value:" + selectPosSp.valueAt(i));
			}
			//LogUtils.i("setMaskVisible~~position-->" + position + "~~selectPosSp.size-->" + selectPosSp.size());
			if (isVisible)
				selectPosSp.put(position, View.VISIBLE);
			else
				selectPosSp.delete(position);
			optView.setVisibility(selectPosSp.get(position, View.GONE));
			if (selectLis != null) {
				selectLis.onSelectSome(selectPosSp.size());
			}
		}
		public int getMaskVisible(int position) {
			return selectPosSp.keyAt(position);
		}
		// 选择全部或取消全部
		public void selectOrCancelAll(boolean isSelectAll) {
			selectPosSp.clear();
			if (isSelectAll) {
				for (int i = 0; i < getCount(); i++) {
					selectPosSp.put(i, View.VISIBLE);
				}
			}
			notifyDataSetChanged();
			if (selectLis != null) {
				selectLis.onSelectSome(selectPosSp.size());
			}
		}

		private class ZoneListener implements OnClickListener {
			private int mposition;
			com.beem.project.btf.service.Contact curContact;

			public ZoneListener(int position,
					com.beem.project.btf.service.Contact curContact) {
				// TODO Auto-generated constructor stub
				this.mposition = position;
				this.curContact = curContact;
			}
			@Override
			public void onClick(View v) {
				if (!isSelectAll()) {
					Debug.getDebugInstance().log(
							getClass().getName() + " zone l " + mposition + " "
									+ curContact.getJid());
					OtherTimeFlyMain.launch(mContext, curContact);
				}
			}
		}

		private class InfoListener implements OnClickListener {
			private Contact c;

			public InfoListener(Contact c) {
				this.c = c;
			}
			@Override
			public void onClick(View v) {
				if (!isSelectAll()) {
					ContactInfoActivity.launch(mContext, c);
				}
			}
		}

		@Override
		public List<Contact> initItems() {
			return new ArrayList<Contact>();
		}
		protected void bindView(View view, final int position,
				final ViewHolder vh) {
			Contact curContact = getItem(position);
			vh.age.setText(BBSUtils.getAgeByBithday(curContact.getBday()));
			vh.nickname.setText(curContact.getNickName());
			vh.age.setSelected(curContact.getSexInt() == 0);
			imageLoader.displayImage(curContact.getPhoto(), vh.headPhoto,
					ImageLoaderConfigers.sexOpt[curContact.getSexInt()]);
			vh.headPhoto.setOnClickListener(new InfoListener(curContact));
			vh.zone.setOnClickListener(new ZoneListener(position, curContact));
			vh.zhezao.setVisibility(selectPosSp.get(position, View.GONE));
			vh.jid.setText("时光号：" + StringUtils.parseName(curContact.getJid()));
			vh.list.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					setSelectingState(true);
					setMaskVisible(position, true, vh.zhezao);
					if (bottomPopupWindow == null) {
						bottomPopupWindow = new BottomPopupWindow(
								(Activity) mContext, itemsOnClick,
								PopupActionType.BLACKLIST_DEL, false);
					}
					bottomPopupWindow.showAtLocation(
							findViewById(R.id.blacklist_wraper), Gravity.BOTTOM
									| Gravity.CENTER_HORIZONTAL, 0, 0);
					return true;
				}
			});
			vh.list.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isSelectingState) {
						// 黑名单勾选状态
						setMaskVisible(position,
								vh.zhezao.getVisibility() == View.GONE,
								vh.zhezao);
					} else {
						// 聊天
						ChatActivity.launch(mContext, getItem(position));
					}
				}
			});
		}
	}

	// 按返回键取消选中的列表项
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isSelectingState) {
				setSelectingState(false);
				return false;
			}
			BlacklistActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private class DistanceUsersDialog extends
			LoadingDialog<String, PacketResult> {
		private SparseIntArray delpositionArr;
		private String[] delJids;

		public DistanceUsersDialog(Activity activity,
				SparseIntArray positionArr, String[] delJids) {
			super(activity);
			this.delpositionArr = positionArr;
			this.delJids = delJids;
			UIHelper.showDialogForLoading(mContext, "请稍后", true);
		}
		@Override
		public PacketResult doInBackground(String... params) {
			return ContactService.getInstance().removeBlacklist(delJids);
		}
		@Override
		public void doStuffWithResult(PacketResult result) {
			UIHelper.hideDialogForLoading();
			if (result != null && result.isOk()) {
				// 删除黑名单
				List<Contact> rstContacts = new ArrayList<Contact>();
				for (int i = 0; i < delpositionArr.size(); i++) {
					if (delpositionArr.valueAt(i) == View.VISIBLE) {
						rstContacts.add(imageAdapter.getItem(delpositionArr
								.keyAt(i)));
					}
				}
				for (Contact c : rstContacts) {
					imageAdapter.removeItem(c);
				}
				imageAdapter.notifyDataSetChanged();
				CToast.showToast(mContext, "操作成功", Toast.LENGTH_SHORT);
			} else {
				if (result != null)
					CToast.showToast(mContext, "操作失败:" + result.getError(),
							Toast.LENGTH_SHORT);
			}
			delpositionArr.clear();
			setSelectingState(false);
		}
	}

	@Override
	public void registerVVBroadCastReceivers() {
	}
}
