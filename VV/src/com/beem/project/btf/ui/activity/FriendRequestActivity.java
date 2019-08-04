package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.ApplyChoiceActivity;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.BBSUtils;
import com.btf.push.Item.MsgType;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DatabaseDao;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.service.ContactService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pullToRefresh.ui.PullToRefreshListView;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendRequestActivity extends VVBaseFragmentActivity implements
		OnClickListener, OnItemClickListener {
	private View viewBack;
	private TextView title;
	private PullToRefreshListView list;
	private MyAdapter adapter;
	private BroadcastReceiver sessionModelReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			loadData();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_request);
		initViews();
	}
	@Override
	protected void onStart() {
		super.onStart();
		loadData();
		regristerBroadReceiver();
	}
	@Override
	protected void onStop() {
		super.onStop();
		unregristerBroadReceiver();
	}
	private void initViews() {
		viewBack = findViewById(R.id.tvw_back);
		title = (TextView) findViewById(R.id.tvw_Title);
		list = (PullToRefreshListView) findViewById(R.id.list);
		list.setPullRefreshEnabled(false);
		list.setPullLoadEnabled(false);
		viewBack.setOnClickListener(this);
		adapter = new MyAdapter(this);
		list.getRefreshableView().setAdapter(adapter);
		try {
			title.setText(getPackageManager().getActivityInfo(getComponentName(), 0).labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		list.getRefreshableView().setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == viewBack) {
			finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView listView = (ListView) parent;
		int realPosition = position - listView.getHeaderViewsCount();
		Item item = (Item) parent.getAdapter().getItem(realPosition);
		Intent i = new Intent(this, ApplyChoiceActivity.class);
		i.putExtra(ApplyChoiceActivity.KEY_APPLY_FRIEND_TIMESTAMP, item.datetime);
		i.putExtra(ApplyChoiceActivity.KEY_APPLY_FRIEND_MESSAGE, item.content);
		i.putExtra(ApplyChoiceActivity.KEY_APPLY_FRIEND_STATUS, item.status);
		i.putExtra(ApplyChoiceActivity.KEY_APPLY_FRIEND_FROMJID, item.jid);
		startActivity(i);
	}
	
	private void regristerBroadReceiver() {
		// 消息数量接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgType.friend_require.toString());
		LocalBroadcastManager.getInstance(mContext).registerReceiver(sessionModelReceiver , filter);
	}
	private void unregristerBroadReceiver() {
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(sessionModelReceiver);
	}
	
	public void onDataResult(List<Item>result) {
		adapter.setData(result);
		adapter.notifyDataSetChanged();
	}
	private void loadData() {
		new VVBaseLoadingDlg<List<Item>>(
				new VVBaseLoadingDlg.VVBaseLoadingDlgCfg(
						FriendRequestActivity.this).setShowWaitingView(true)) {
			@Override
			protected List<Item> doInBackground() {
				DatabaseDao dao = DatabaseDao.getInstance();
				List<FriendRequest> friendRequests = dao.getFriendRequest();
				if (friendRequests == null) {
					return null;
				}
				List<Item> items = new ArrayList<FriendRequestActivity.Item>();
				for (FriendRequest fr : friendRequests) {
					FriendRequestActivity.Item item = new Item();
					item.datetime = fr.getTime();
					item.status = fr.getStatus();
					item.content = fr.getContent();
					item.id = fr.getId();
					item.jid = fr.getJidFrom();
					Contact contact = ContactService.getInstance().getContact(
							fr.getJidFrom());
					if (contact != null) {
						item.imgUrl = contact.getPhoto();
						item.name = contact.getNickName();
						item.sex = contact.getSexInt();
					}
					items.add(item);
				}
				return items;
			}
			@Override
			protected void onTimeOut() {
				super.onTimeOut();
			}
			@Override
			protected void onPostExecute(List<Item> result) {
				onDataResult(result);
			}
		}.execute();
	}

	private class Item {
		protected String jid;
		public String imgUrl;
		public String name;
		public String content;
		public String datetime;
		public int status;
		public String id;
		public int sex;
	}

	private class MyAdapter extends BaseAdapter {
		private Context c;
		private List<Item> data;
		private LayoutInflater inflater;

		public MyAdapter(Context c) {
			this.c = c;
			inflater = LayoutInflater.from(c);
		}
		public void setData(List<Item> newData) {
			if (data == null) {
				data = new ArrayList<FriendRequestActivity.Item>(newData);
			} else {
				data.clear();
				data.addAll(newData);
			}
		}
		@Override
		public int getCount() {
			if (data != null) {
				return data.size();
			}
			return 0;
		}
		@Override
		public Item getItem(int position) {
			if (data != null) {
				return data.get(position);
			}
			return null;
		}
		@Override
		public long getItemId(int position) {
			if (data != null) {
				return data.get(position).id.hashCode();
			}
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.listitem_friendrequests, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
				holder.tvw_name = (TextView) convertView.findViewById(R.id.tvw_name);
				holder.tvw_message = (TextView) convertView.findViewById(R.id.tvw_message);
				holder.sessiondate = (TextView) convertView.findViewById(R.id.sessiondate);
				holder.tvw_status = (TextView) convertView.findViewById(R.id.tvw_status);
				holder.imv_verify = convertView.findViewById(R.id.imv_verify);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			bindViews(holder, getItem(position));
			return convertView;
		}
		private void bindViews(ViewHolder holder, Item item) {
			ImageLoader.getInstance().displayImage(item.imgUrl, holder.avatar, ImageLoaderConfigers.sexOpt[item.sex]);
			holder.tvw_name.setText(item.name);
			holder.tvw_message.setText(item.content);
			holder.sessiondate.setText(BBSUtils.getTimeDurationString(item.datetime));
			if (item.status == 0) {
				holder.imv_verify.setVisibility(View.VISIBLE);
				holder.tvw_status.setVisibility(View.GONE);
			} else {
				holder.imv_verify.setVisibility(View.GONE);
				holder.tvw_status.setVisibility(View.VISIBLE);
				if (item.status == 1) {
					holder.tvw_status.setText(R.string.friend_accessed_message);
				} else {
					holder.tvw_status.setText(R.string.friend_refused_message);
				}
			}
		}

		private class ViewHolder {
			private ImageView avatar;
			private TextView tvw_name;
			private TextView tvw_message;
			private TextView sessiondate;
			private TextView tvw_status;
			private View imv_verify;
		}
	}

}
