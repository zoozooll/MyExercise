package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.FolderItemNewsActivity;
import com.beem.project.btf.ui.activity.FolderItemVideoPlayerActivity;
import com.beem.project.btf.ui.activity.ImageGalleryActivity;
import com.beem.project.btf.ui.activity.ShareRankingActivity;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.ShareType;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.view.grid.SharedImageGridAdapter;
import com.butterfly.vv.view.timeflyView.HolderTwowayView;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vv.image.gallery.viewer.ScrollingViewPager;

public class ShareTranceCommentsAdapter extends
		CommonPhotoAdapter<ImageFolderItem> {
	private static final String tag = SharedImageGridAdapter.class
			.getSimpleName();
	private Context mContext;
	// 记录点赞
	private SparseArray<View> convertViews = new SparseArray<View>();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ScrollingViewPager mPager;
	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transparent)
			.showImageForEmptyUri(R.drawable.transparent)
			.showImageOnFail(R.drawable.transparent).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private DisplayImageOptions options2 = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageForEmptyUri(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private ShareTranceViewType currentType = ShareTranceViewType.user_type;
	private LayoutInflater inflater;

	public enum ShareTranceViewType {
		user_type, news_type1, news_type2, news_type3
	}

	public ShareTranceCommentsAdapter(Context c, AdapterView<?> listView) {
		super(listView);
		this.mContext = c;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setScollingViewPager(ScrollingViewPager pager) {
		mPager = pager;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserViewHolder userViewHolder = null;
		NewsViewHolder newsViewHolder = null;
		currentType = ShareTranceViewType.values()[getItemViewType(position)];
		if (convertView == null) {
			switch (currentType) {
				case user_type: {
					convertView = inflater.inflate(
							R.layout.big_photo_with_repost_item_2, parent,
							false);
					userViewHolder = new UserViewHolder();
					userViewHolder.comment_lv = (ListView) convertView
							.findViewById(R.id.comment_lv);
					userViewHolder.myGrid_info = (HolderTwowayView) convertView
							.findViewById(R.id.myGrid_info);
					userViewHolder.myGrid_info.setHolder(mPager);
					userViewHolder.myGrid_info.setItemMargin(3);
					userViewHolder.mark = (TextView) convertView
							.findViewById(R.id.mark);
					userViewHolder.viedo_btn = (ImageView) convertView
							.findViewById(R.id.viedo_btn);
					userViewHolder.head = (ImageView) convertView
							.findViewById(R.id.head);
					userViewHolder.name = (TextView) convertView
							.findViewById(R.id.name);
					userViewHolder.signature = (TextView) convertView
							.findViewById(R.id.signature);
					userViewHolder.foldertime = (TextView) convertView
							.findViewById(R.id.foldertime);
					userViewHolder.picNum = (TextView) convertView
							.findViewById(R.id.picNum);
					userViewHolder.support_count = (TextView) convertView
							.findViewById(R.id.support_count);
					userViewHolder.comment_count = (TextView) convertView
							.findViewById(R.id.comment_count);
					userViewHolder.pg_detail_llt = convertView
							.findViewById(R.id.pg_detail_llt);
					userViewHolder.gradeHotImageview = (ImageView) convertView
							.findViewById(R.id.gradeHotImageview);
					convertView.setTag(userViewHolder);
					break;
				}
				case news_type1: {
					convertView = inflater.inflate(
							R.layout.shareadapter_layout1, parent, false);
					newsViewHolder = new NewsViewHolder();
					newsViewHolder.wraper = (ViewGroup) convertView
							.findViewById(R.id.wraper);
					newsViewHolder.title = (TextView) convertView
							.findViewById(R.id.title);
					newsViewHolder.thumb_1 = (ImageView) convertView
							.findViewById(R.id.thumb_1);
					newsViewHolder.thumb_2 = (ImageView) convertView
							.findViewById(R.id.thumb_2);
					newsViewHolder.thumb_3 = (ImageView) convertView
							.findViewById(R.id.thumb_3);
					newsViewHolder.foldertime = (TextView) convertView
							.findViewById(R.id.foldertime);
					convertView.setTag(newsViewHolder);
					break;
				}
				case news_type2: {
					convertView = inflater.inflate(
							R.layout.shareadapter_layout2, parent, false);
					newsViewHolder = new NewsViewHolder();
					newsViewHolder.wraper = (ViewGroup) convertView
							.findViewById(R.id.wraper);
					newsViewHolder.title = (TextView) convertView
							.findViewById(R.id.title);
					newsViewHolder.thumb_1 = (ImageView) convertView
							.findViewById(R.id.thumb_1);
					newsViewHolder.foldertime = (TextView) convertView
							.findViewById(R.id.foldertime);
					convertView.setTag(newsViewHolder);
					break;
				}
				case news_type3: {
					convertView = inflater.inflate(
							R.layout.shareadapter_layout3, parent, false);
					newsViewHolder = new NewsViewHolder();
					newsViewHolder.wraper = (ViewGroup) convertView
							.findViewById(R.id.wraper);
					newsViewHolder.title = (TextView) convertView
							.findViewById(R.id.title);
					newsViewHolder.thumb_1 = (ImageView) convertView
							.findViewById(R.id.thumb_1);
					newsViewHolder.foldertime = (TextView) convertView
							.findViewById(R.id.foldertime);
					convertView.setTag(newsViewHolder);
					break;
				}
			}
		} else {
			switch (currentType) {
				case user_type:
					userViewHolder = (UserViewHolder) convertView.getTag();
					break;
				case news_type1:
					newsViewHolder = (NewsViewHolder) convertView.getTag();
					break;
				case news_type2:
					newsViewHolder = (NewsViewHolder) convertView.getTag();
					break;
				case news_type3:
					newsViewHolder = (NewsViewHolder) convertView.getTag();
					break;
			}
		}
		switch (currentType) {
			case user_type:
				convertViews.put(position, convertView);
				bindUserView(convertView, position, userViewHolder);
				break;
			case news_type1:
				bindNewsView(convertView, position, newsViewHolder);
				break;
			case news_type2:
				bindNewsView(convertView, position, newsViewHolder);
				break;
			case news_type3:
				bindNewsView(convertView, position, newsViewHolder);
				break;
		}
		return convertView;
	}

	private static class UserViewHolder {
		public View pg_detail_llt;
		public ListView comment_lv;
		public HolderTwowayView myGrid_info;
		public TextView mark;
		public TextView support_count;
		public TextView comment_count;
		public ImageView viedo_btn;
		public TextView signature;
		public TextView name;
		public ImageView head;
		public TextView foldertime;
		public TextView picNum;
		public ImageView gradeHotImageview;
	}

	private static class NewsViewHolder {
		public ViewGroup wraper;
		public TextView title;
		public ImageView thumb_1;
		public ImageView thumb_2;
		public ImageView thumb_3;
		public TextView foldertime;
	}

	/** 主要点击事件监听器 */
	public class MainClickLis implements OnClickListener {
		private Context mContext;
		private int pos;
		public UserViewHolder vh;

		public MainClickLis(Context mContext, int pos, UserViewHolder vh) {
			super();
			this.mContext = mContext;
			this.pos = pos;
			this.vh = vh;
		}
		@Override
		public void onClick(View v) {
			final ImageFolderItem shareItem = getItem(pos);
			switch (v.getId()) {
			// 跳转图片组详情
				case R.id.pg_detail_llt: {
					// LogUtils.i("shareItem" + shareItem);
					/*ShareRankingActivity.launch(mContext, shareItem, pos,
							shareType);*/
					launcherShareRankingActivity(shareItem.getImageFolder().getJid(),
							shareItem.getImageFolder().getGid(), shareItem
									.getImageFolder().getCreateTime());
					break;
				}
				// 回复
				case R.id.comment_count: {
					/*ShareRankingActivity.launch(mContext, shareItem, pos,
							shareType);*/
					launcherShareRankingActivity(shareItem.getImageFolder().getJid(),
							shareItem.getImageFolder().getGid(), shareItem
									.getImageFolder().getCreateTime());
					break;
				}
				// 赞
				case R.id.support_count: {
					optSupport(pos, vh);
					break;
				}
				case R.id.head: {
					ContactInfoActivity
							.launch(mContext, shareItem.getContact());
					break;
				}
				case R.id.viedo_btn: {
					String url = shareItem.getImageFolder().getAlbum_url();
					if (!TextUtils.isEmpty(url)) {
						Intent i = new Intent(mContext,
								FolderItemVideoPlayerActivity.class);
						i.setData(Uri.parse(shareItem.getImageFolder()
								.getAlbum_url()));
						mContext.startActivity(i);
					} else {
						Toast.makeText(mContext, R.string.shareranking_novideo,
								Toast.LENGTH_SHORT).show();
					}
					break;
				}
				default:
					break;
			}
		}
	}

	/**
	 * @func 赞
	 * @param pos
	 * @param vh
	 */
	public void optSupport(final int pos, final UserViewHolder vh) {
		final ImageFolder folder = getItem(pos).getImageFolder();
		if (!LoginManager.getInstance().isLogined()) {
			CToast.showToast(BeemApplication.getContext(), R.string.like_error_unlogin,
					Toast.LENGTH_SHORT);
			ActivityController.getInstance().gotoLogin();
			return;
		}
		TimeflyService.executeThumbUp(mContext, folder,
			new onPacketResult<Map<String, Object>>() {
				@SuppressLint("NewApi")
				@Override
				public void onResult(Map<String, Object> result,
						boolean timeout, Start start) {
					folder.setThumbupCount(folder.getThumbupCount() + 1);
					folder.setThumbup(true);
					vh.support_count.setText(String.valueOf(folder
							.getThumbupCount()));
					vh.support_count.setSelected(folder.isThumbup());
					LikedPhotoGroup lpg = new LikedPhotoGroup();
					lpg.setLikedTime(System.currentTimeMillis());
					lpg.setGid(folder.getGid());
					lpg.setGid_creat_time(BBSUtils.getShortGidCreatTime(folder.getCreateTime()));
					lpg.setGid_jid(folder.getJid());
					lpg.saveToDatabase();
				}
			});
	}
	@SuppressLint("NewApi")
	protected void bindUserView(View view, final int position,
			final UserViewHolder vh) {
		ImageFolderItem shareItem = getItem(position);
		Contact userContact = shareItem.getContact();
		ImageFolder folder = shareItem.getImageFolder();
		userContact.displayPhoto(vh.head);
		vh.name.setText(userContact.getSuitableName());
		vh.foldertime.setText(BBSUtils.getTimeDurationString(folder
				.getCreateTime()));
		String note = folder.getSignature();
		if (TextUtils.isEmpty(note)) {
			vh.signature.setVisibility(View.GONE);
		} else {
			vh.signature.setVisibility(View.VISIBLE);
			vh.signature.setText(folder.getSignature());
		}
		vh.support_count.setText(String.valueOf(folder.getThumbupCount()));
		vh.support_count.setSelected(folder.isThumbup());
		vh.comment_count.setText(String.valueOf(folder.getCommentCount()));
		vh.picNum.setText("(" + shareItem.getVVImages().size() + "张)");
		if (!TextUtils.isEmpty(shareItem.getImageFolder().getTopic())) {
			vh.mark.setVisibility(View.VISIBLE);
			vh.mark.setText(folder.getTopic());
		} else {
			vh.mark.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(folder.getGrade())) {
			ImageLoader.getInstance().displayImage(folder.getGrade(),
					vh.gradeHotImageview, options);
		}
		refreshPhotosView(vh, position, shareItem);
		refreshCommentsView(vh, position, shareItem);
		// 设置事件监听
		setUpViewClickListener(view, position, vh);
	}
	protected void bindNewsView(View view, final int position,
			final NewsViewHolder vh) {
		final ImageFolderItem shareItem = getItem(position);
		ImageFolder folder = shareItem.getImageFolder();
		vh.title.setText(folder.getTopic());
		vh.foldertime.setText(BBSUtils.getTimeDurationString(folder
				.getCreateTime()));
//		Log.i("yangle", currentType.toString());
		switch (currentType) {
			case news_type1:
				LayoutParams para1 = vh.thumb_1.getLayoutParams();
				LayoutParams para2 = vh.thumb_2.getLayoutParams();
				LayoutParams para3 = vh.thumb_3.getLayoutParams();
				int itemw = (DimenUtils.getScreenMetrics(mContext).x - DimenUtils
						.dip2px(mContext, 30)) / 3;
				para1.width = itemw;
				para2.width = itemw;
				para3.width = itemw;
				vh.thumb_1.setLayoutParams(para1);
				vh.thumb_2.setLayoutParams(para2);
				vh.thumb_3.setLayoutParams(para3);
				if (shareItem.getVVImages().size() >= 3) {
					ImageLoader.getInstance().displayImage(
							shareItem.getVVImages().get(0).getPathThumb(),
							vh.thumb_1, options2);
					ImageLoader.getInstance().displayImage(
							shareItem.getVVImages().get(1).getPathThumb(),
							vh.thumb_2, options2);
					ImageLoader.getInstance().displayImage(
							shareItem.getVVImages().get(2).getPathThumb(),
							vh.thumb_3, options2);
				}
				break;
			default:
				if (shareItem.getVVImages().size() >= 1) {
					ImageLoader.getInstance().displayImage(
							shareItem.getVVImages().get(0).getPathThumb(),
							vh.thumb_1, options2);
				}
				break;
		}
		vh.wraper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FolderItemNewsActivity.launch(mContext,
						Uri.parse(shareItem.getImageFolder().getAlbum_url()));
			}
		});
	}
	// 刷新评论界面
	private void refreshCommentsView(UserViewHolder vh, int position,
			ImageFolderItem shareItem) {
		CommentAdapter commentAdapter = null;
		if (shareItem.getComments().size() > 0) {
			vh.comment_lv.setVisibility(View.VISIBLE);
		} else {
			vh.comment_lv.setVisibility(View.GONE);
		}
		if (vh.comment_lv.getAdapter() == null) {
			commentAdapter = new CommentAdapter(mContext, 3);
			for (int i = 0; i < Math.min(4, shareItem.getComments().size()); i++) {
				commentAdapter.addItem(shareItem.getComments().get(i));
			}
			vh.comment_lv.setAdapter(commentAdapter);
		} else {
			commentAdapter = (CommentAdapter) vh.comment_lv.getAdapter();
			commentAdapter.clearItems();
			for (int i = 0; i < shareItem.getComments().size(); i++) {
				CommentItem commentItem = shareItem.getComments().get(i);
				commentAdapter.addItem(commentItem);
			}
			commentAdapter.notifyDataSetChanged();
		}
	}
	// 刷新图片
	private void refreshPhotosView(UserViewHolder vh, int position,
			ImageFolderItem shareItem) {
		// 加载内容图片
		if (vh.myGrid_info.getAdapter() == null) {
			SharedImageGridAdapter mAlbumGridAdapter = new SharedImageGridAdapter(
					(Activity) mContext, shareItem.getVVImages(), null);
			vh.myGrid_info.setAdapter(mAlbumGridAdapter);
		} else {
			SharedImageGridAdapter mAlbumGridAdapter = (SharedImageGridAdapter) vh.myGrid_info
					.getAdapter();
			try {
				List<VVImage> oldImages = mAlbumGridAdapter.getItems();
				List<VVImage> newImages = shareItem.getVVImages();
				if (!oldImages.equals(newImages)) {
					mAlbumGridAdapter.setItems(shareItem.getVVImages());
					mAlbumGridAdapter.notifyDataSetChanged();
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}
	private void setUpViewClickListener(View view, int pos, UserViewHolder vh) {
		OnClickListener l = new MainClickLis(mContext, pos, vh);
		vh.support_count.setOnClickListener(l);
		vh.comment_count.setOnClickListener(l);
		vh.viedo_btn.setOnClickListener(l);
		vh.head.setOnClickListener(l);
		vh.pg_detail_llt.setOnClickListener(l);
		vh.myGrid_info.setOnItemClickListener(new IMGlt(pos));
		vh.comment_lv.setOnItemClickListener(new CommentClickListener(mContext,
				pos));
	}

	class IMGlt implements OnItemClickListener {
		int whichfolder;

		public IMGlt(int fld) {
			this.whichfolder = fld;
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ImageGalleryActivity.launch(mContext, arg2, getItem(whichfolder));
		}
	}

	class CommentClickListener implements OnItemClickListener {
		private Context mContext;
		private int pos;

		public CommentClickListener(Context mContext, int pos) {
			this.mContext = mContext;
			this.pos = pos;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			final ImageFolderItem shareItem = getItem(pos);
//			ShareRankingActivity.launch(mContext, shareItem, pos, shareType);
			launcherShareRankingActivity(shareItem.getImageFolder().getJid(),
					shareItem.getImageFolder().getGid(), shareItem
							.getImageFolder().getCreateTime());
		}
	}

	@Override
	public void addItems(Collection<ImageFolderItem> trancItems) {
		for (ImageFolderItem folderItemNew : trancItems) {
			addItem(folderItemNew);
		}
	}
	
	@Override
	public void addItem(ImageFolderItem folderItemNew) {
		ImageFolder newFolder = folderItemNew.getImageFolder();
		if (newFolder != null) {
			Iterator<ImageFolderItem> it = getItems().iterator();
			while (it.hasNext()) {
				if (folderItemNew.equals(it.next())) {
					it.remove();
					break;
				}
			}
			super.addItem(folderItemNew);
		}
	}
	// 响应内存值的改变,更新点赞和评论
	public void updateSupportList(PraiseEventBusData data,
			EventAction eventAction) {
		ImageFolder folder = null;
		for (int i = 0; i < getCount(); i++) {
			folder = getItem(i).getImageFolder();
			// 三个参数确定一个唯一的图片组
			if (data.getJid().equals(folder.getJid())
					&& data.getGid().equals(folder.getGid())
					&& data.getCreateTime().equals(folder.getCreateTime())) {
				if (eventAction == EventAction.ShareSupportChange) {
					folder.setThumbupCount(folder.getThumbupCount() + 1);
					folder.setThumbup(true);
				} else if (eventAction == EventAction.ShareCommentChange) {
					getItem(i).addComment(data.getComment());
					// 评论数目增1
					folder.setCommentCount(folder.getCommentCount() + 1);
				}
				updateSingleRow(i);
			}
		}
	}
	@Override
	public List<ImageFolderItem> initItems() {
		return new ArrayList<ImageFolderItem>();
	}
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		ImageFolderItem shareItem = getItem(position);
		ImageFolder folder = shareItem.getImageFolder();
		if ("0".equals(folder.getType())) {
			currentType = ShareTranceViewType.user_type;
		} else if ("1".equals(folder.getType())) {
			currentType = ShareTranceViewType.news_type1;
		} else if ("2".equals(folder.getType())) {
			currentType = ShareTranceViewType.news_type2;
		} else if ("3".equals(folder.getType())) {
			currentType = ShareTranceViewType.news_type3;
		} else {
			currentType = ShareTranceViewType.user_type;
		}
		return currentType.ordinal();
	}
	@Override
	public int getViewTypeCount() {
		return ShareTranceViewType.values().length;
	}
	
	private void launcherShareRankingActivity(String jid, String gid, String gidCreateTime) {
		Intent i = new Intent(mContext, ShareRankingActivity.class);
		i.putExtra("jid", jid);
		i.putExtra("gid", gid);
		i.putExtra("gidCreatTime", gidCreateTime);
		mContext.startActivity(i);
	}
}
