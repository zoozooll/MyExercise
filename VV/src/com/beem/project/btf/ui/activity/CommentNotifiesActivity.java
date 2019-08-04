package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.Item.MsgType;
import com.butterfly.vv.db.ormhelper.DBHelper;
import com.butterfly.vv.db.ormhelper.DatabaseDao;
import com.butterfly.vv.db.ormhelper.bean.CommentNotifies;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pullToRefresh.ui.PullToRefreshListView;
import com.teleca.jamendo.api.WSError;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Photo;
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

public class CommentNotifiesActivity extends VVBaseFragmentActivity implements
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
		loadData();
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
	private void regristerBroadReceiver() {
		// 消息数量接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgType.comment.toString());
		LocalBroadcastManager.getInstance(mContext).registerReceiver(sessionModelReceiver , filter);
	}
	private void unregristerBroadReceiver() {
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(sessionModelReceiver);
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
		Intent i = new Intent(this, ShareRankingActivity.class);
		i.putExtra("jid", item.gidJid);
		i.putExtra("gid", item.gid);
		i.putExtra("gidCreatTime", item.gidCreatTime);
		startActivity(i);
	}
	public void onDataResult(List<Item> result) {
		adapter.setData(result);
		adapter.notifyDataSetChanged();
	}
	private void loadData() {
		new VVBaseLoadingDlg<List<Item>>(
				new VVBaseLoadingDlg.VVBaseLoadingDlgCfg(
						CommentNotifiesActivity.this).setShowWaitingView(true)) {
			@Override
			protected List<Item> doInBackground() {
				DatabaseDao dao = DatabaseDao.getInstance();
				final List<CommentNotifies> result = dao.getComentNotifies();
				if (result == null) {
					return null;
				}
				ThreadUtils.executeTask(new Runnable() {
					@Override
					public void run() {
						for (CommentNotifies cn : result) {
							cn.setCheck(true);
							cn.saveToDatabaseAsync();
						}
					}
				});
				List<Item> items = new ArrayList<CommentNotifiesActivity.Item>();
				for (CommentNotifies cn : result) {
					CommentNotifiesActivity.Item item = new Item();
					item.datetime = cn.getTime();
//					item.status = cn.isCheck();
					item.content = cn.getBody();
					item.id = cn.getId();
					item.gid = cn.getGid();
					item.gidCreatTime = cn.getGidCreatTime();
					item.gidJid = cn.getGidJid();
					item.cid = cn.getCommentId();
					item.toCid = cn.getToCommentCid();
					Contact contact = ContactService.getInstance().getContact(
							cn.getFromJid());
					item.imgUrl = contact.getPhoto();
					item.name = contact.getNickName();
					item.sex = contact.getSexInt();
					try {
						ImageFolderItem folder = ImageFolderItemManager.getInstance().getImageFolderItem(item.gidJid,
								item.gid,
								item.gidCreatTime);
						item.gidImgUrl = folder.getVVImages().get(0).getPathThumb();
					} catch (WSError e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
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
		public int sex;
		public String imgUrl;
		public String name;
		public String content;
		public String datetime;
		public int status;
		public String id;
		public String gid;
		public String gidCreatTime;
		public String gidJid;
		public String cid;
		public String toCid;
		public String gidImgUrl;
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
				data = new ArrayList<CommentNotifiesActivity.Item>(newData);
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
						R.layout.listitem_messages_comments, null);
				holder = new ViewHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
				holder.tvw_name = (TextView) convertView.findViewById(R.id.tvw_name);
				holder.twv_description = (TextView) convertView.findViewById(R.id.twv_description);
				holder.tvw_message = (TextView) convertView.findViewById(R.id.tvw_message);
				holder.sessiondate = (TextView) convertView.findViewById(R.id.sessiondate);
				holder.comment_img = (CircleImageView) convertView.findViewById(R.id.comment_img);
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
			holder.twv_description
					.setText("0".equals(item.toCid) ? R.string.commented_photogroup_message
							: R.string.commented_tocomment_message);
			ImageLoader.getInstance().displayImage(item.gidImgUrl, holder.comment_img, ImageLoaderConfigers.comment_img_options);
		}

		private class ViewHolder {
			private ImageView avatar;
			private TextView tvw_name;
			private TextView twv_description;
			private TextView tvw_message;
			private TextView sessiondate;
			private CircleImageView comment_img;
		}
	}

}
