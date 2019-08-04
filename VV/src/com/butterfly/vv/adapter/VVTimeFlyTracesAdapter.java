package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lucasr.twowayview.TwoWayView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.agimind.widget.SlideHolder;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.FolderItemVideoPlayerActivity;
import com.beem.project.btf.ui.activity.ImageGalleryActivity;
import com.beem.project.btf.ui.activity.ShareRankingActivity;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.ui.views.ChangeAlbumAuthorityView;
import com.beem.project.btf.ui.views.ChangeAlbumAuthorityView.onChangeAlbumListen;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;
import com.beem.project.btf.ui.views.SimpleAuthorityDilaogView;
import com.beem.project.btf.ui.views.SimpleAuthorityDilaogView.ButtonClickListener;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SortedList;
import com.beem.project.btf.utils.SortedList.Equalable;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder.onLocationLis;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.view.timeflyView.HolderTwowayView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class VVTimeFlyTracesAdapter extends CommonPhotoAdapter<ImageFolderItem> {
	private LayoutInflater mInflater;
	private Context mContext;
	private SlideHolder slideHolder;
	// 模糊对话框
	private BBSCustomerDialog blurDlg;
	private ChangeAlbumAuthorityView blurView;
	private int[] colors = { R.color.oval_1, R.color.oval_2, R.color.oval_3,
			R.color.oval_4, R.color.oval_5 };
	private ArrayList<ViewHolder> mViewHolderList = new ArrayList<ViewHolder>();
	// 记录点赞
	private SparseArray<View> convertViews = new SparseArray<View>();
	private final boolean isShowComments;

	public VVTimeFlyTracesAdapter(Context context,
			android.widget.AdapterView<?> listView, boolean isShowComments) {
		super(listView);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.isShowComments = isShowComments;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.time_fly_listitem, parent,
					false);
			vh = new ViewHolder();
			vh.picGroupSign = (TextView) convertView
					.findViewById(R.id.contactlistpseudo);
			vh.picGrouploc = (TextView) convertView
					.findViewById(R.id.picgroup_loc);
			vh.Edit_more = (TextView) convertView.findViewById(R.id.Edit_more);
			vh.imageGallery = (HolderTwowayView) convertView
					.findViewById(R.id.item_images);
			vh.imageGallery.setItemMargin(3);
			vh.date_day = (TextView) convertView.findViewById(R.id.date_day);
			vh.data_yearAndmonth = (TextView) convertView
					.findViewById(R.id.date_yearAndmonth);
			vh.date_wraper = (ViewGroup) convertView
					.findViewById(R.id.date_wraper);
			vh.date_wraper_divider = convertView
					.findViewById(R.id.date_wraper_divider);
			vh.picNum = (TextView) convertView
					.findViewById(R.id.contactlistmsgperso);
			vh.authority = (TextView) convertView.findViewById(R.id.limit_flag);
			vh.comment_lv = (ListView) convertView
					.findViewById(R.id.comment_lv);
			vh.me_timefly_btns = (ViewGroup) convertView
					.findViewById(R.id.me_timefly_btns);
			vh.other_timefly_btns = (ViewGroup) convertView
					.findViewById(R.id.other_timefly_btns);
			vh.support_count = (TextView) convertView
					.findViewById(R.id.support_count);
			vh.comment_count = (TextView) convertView
					.findViewById(R.id.comment_count);
			vh.viedo_btn = (ImageView) convertView.findViewById(R.id.viedo_btn);
			vh.support_count = (TextView) convertView
					.findViewById(R.id.support_count);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		mViewHolderList.add(vh);
		vh.position = position;
		convertViews.put(position, convertView);
		bindView(convertView, position, vh);
		return convertView;
	}

	public class ViewHolder {
		private ImageView viedo_btn;
		private TextView comment_count;
		private TextView support_count;
		private ViewGroup other_timefly_btns;
		private ViewGroup me_timefly_btns;
		private ListView comment_lv;
		private TextView picGroupSign;
		private TextView picGrouploc;
		private TextView picNum;
		private HolderTwowayView imageGallery;
		private TextView Edit_more;
		private TextView authority;
		private TextView date_day;
		private TextView data_yearAndmonth;
		private ViewGroup date_wraper;
		private View date_wraper_divider;
		private int position;

		public TwoWayView getImageGallery() {
			return this.imageGallery;
		}
	}

	protected void bindView(View convertView, final int position,
			final ViewHolder vh) {
		ImageFolderItem folderItem = getItem(position);
		ImageFolder folder = folderItem.getImageFolder();
		folderItem.getImageFolder().showLocation(new onLocationLis() {
			int mPos = position;

			@Override
			public void onResult(String location) {
				// 防止listview滑动viewholder重用导致显示错误
				if (mPos == vh.position) {
					if (TextUtils.isEmpty(location)) {
						vh.picGrouploc.setText("深圳");
					} else {
						vh.picGrouploc.setText(location);
					}
				} else {
					//LogUtils.i("vh.position:" + vh.position + " mPos:" + mPos);
				}
			}
		});
		vh.picNum.setText("(" + folderItem.getImageFolder().getPhotoCount()
				+ "张)");
		String note = folderItem.getImageFolder().getSignature();
		if (TextUtils.isEmpty(note)) {
			vh.picGroupSign.setVisibility(View.GONE);
		} else {
			vh.picGroupSign.setVisibility(View.VISIBLE);
			vh.picGroupSign.setText(note);
		}
		vh.imageGallery.setHolder(slideHolder);
		if (vh.imageGallery.getAdapter() == null) {
			ImageAdapter galleryAdapter = new ImageAdapter(
					folderItem.getVVImages());
			vh.imageGallery.setAdapter(galleryAdapter);
		} else {
			ImageAdapter galleryAdapter = (ImageAdapter) vh.imageGallery
					.getAdapter();
			galleryAdapter.setVVImages(folderItem.getVVImages());
			galleryAdapter.notifyDataSetChanged();
		}
		if (isShowComments) {
			vh.comment_lv.setVisibility(View.VISIBLE);
			vh.comment_lv.setOnItemClickListener(new CommentClickListener(
					mContext, position));
			if (vh.comment_lv.getAdapter() == null) {
				CommentAdapter commentAdapter = new CommentAdapter(mContext, 4);
				for (int i = 0; i < folderItem.getComments().size(); i++) {
					commentAdapter.addItem(folderItem.getComments().get(i));
				}
				vh.comment_lv.setAdapter(commentAdapter);
			} else {
				CommentAdapter commentAdapter = (CommentAdapter) vh.comment_lv
						.getAdapter();
				if (folderItem.getComments() != null) {
					commentAdapter.clearItems();
					commentAdapter.addItems(folderItem.getComments());
					commentAdapter.notifyDataSetChanged();
				}
			}
		} else {
			vh.comment_lv.setVisibility(View.GONE);
		}
		if (folderItem.getContact() != null
				&& LoginManager.getInstance().isMyJid(
						folderItem.getContact().getJid())) {
			vh.me_timefly_btns.setVisibility(View.VISIBLE);
			vh.other_timefly_btns.setVisibility(View.GONE);
		} else {
			vh.me_timefly_btns.setVisibility(View.GONE);
			vh.other_timefly_btns.setVisibility(View.VISIBLE);
			vh.support_count.setSelected(folder.isThumbup());
			vh.support_count.setText(String.valueOf(folderItem.getImageFolder()
					.getThumbupCount()));
			vh.comment_count.setText(String.valueOf(folderItem.getImageFolder()
					.getCommentCount()));
			vh.viedo_btn.setOnClickListener(new MainClickLis(mContext,
					position, vh));
			vh.comment_count.setOnClickListener(new MainClickLis(mContext,
					position, vh));
			vh.support_count.setOnClickListener(new MainClickLis(mContext,
					position, vh));
		}
		String[] dates = folderItem.getImageFolder().getCreateTimeYMD();
		vh.imageGallery.setOnItemClickListener(new MyItemClickLis(folderItem));
		if (dates != null && dates.length >= 2) {
			vh.date_day.setText(dates[2]);
			vh.data_yearAndmonth.setText(dates[0] + "." + dates[1]);
		}
		setViewBackgroundLevel(position, vh);
		vh.Edit_more.setOnClickListener(new ChangeAlbumLT(position));
		vh.authority.setText(AlbumAuthority.getAlbumAuthorityText(folderItem
				.getImageFolder().getAuthority()));
		convertView.setOnClickListener(new MyClickListener(position));
	}
	private void setViewBackgroundLevel(int index, ViewHolder vh) {
		if (getCount() == (index + 1)) {
			vh.date_wraper_divider.setVisibility(View.GONE);
		} else {
			vh.date_wraper_divider.setVisibility(View.VISIBLE);
			vh.date_wraper_divider.setBackgroundColor(mContext.getResources()
					.getColor(colors[index % 5]));
		}
		Drawable drawable = vh.date_wraper.getBackground();
		drawable.setLevel(index % 5);
		vh.date_wraper.setBackgroundDrawable(drawable);
	}

	private class MainClickLis implements OnClickListener {
		private Context mContext;
		private int pos;
		private ViewHolder vh;

		public MainClickLis(Context mContext, int pos, ViewHolder vh) {
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
				// 回复
				case R.id.comment_count: {
//					ShareRankingActivity.launch(mContext, shareItem, pos, null);
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
				default:
					break;
			}
		}
	}

	// 响应内存值的改变,更新点赞和评论
	public void updateSupportList(PraiseEventBusData data,
			EventAction eventAction) {
		for (int i = 0; i < getCount(); i++) {
			ImageFolderItem shareItem = getItem(i);
			// 三个参数确定一个唯一的图片组
			if (data.getJid().equals(shareItem.getImageFolder().getJid())
					&& data.getGid()
							.equals(shareItem.getImageFolder().getGid())
					&& data.getCreateTime().equals(
							shareItem.getImageFolder().getCreateTime())) {
				if (eventAction == EventAction.ShareSupportChange) {
					shareItem.getImageFolder().setThumbupCount(
							shareItem.getImageFolder().getThumbupCount() + 1);
					shareItem.getImageFolder().setThumbup(true);
				} else if (eventAction == EventAction.ShareCommentChange) {
					shareItem.addComment(data.getComment());
					//评论数目增1
					shareItem.getImageFolder().setCommentCount(
							shareItem.getImageFolder().getCommentCount() + 1);
				}
				updateSingleRow(i);
			}
		}
	}
	private void optSupport(final int pos, final ViewHolder vh) {
		final ImageFolder folder = getItem(pos).getImageFolder();
		TimeflyService.executeThumbUp(mContext, folder,
				new onPacketResult<Map<String, Object>>() {
					@Override
					public void onResult(Map<String, Object> result,
							boolean timeout, Start start) {
						folder.setThumbupCount(folder.getThumbupCount() + 1);
						folder.setThumbup(true);
						vh.support_count.setText(String.valueOf(folder
								.getThumbupCount()));
						vh.support_count.setSelected(folder.isThumbup());
						updateSingleRow(pos);
						LikedPhotoGroup lpg = new LikedPhotoGroup();
						lpg.setLikedTime(System.currentTimeMillis());
						lpg.setGid(folder.getGid());
						lpg.setGid_creat_time(BBSUtils.getShortGidCreatTime(folder.getCreateTime()));
						lpg.setGid_jid(folder.getJid());
						lpg.saveToDatabase();
					}
				});
	}

	private class MyItemClickLis implements OnItemClickListener {
		private ImageFolderItem folderItem;

		public MyItemClickLis(ImageFolderItem folderItem) {
			this.folderItem = folderItem;
		}
		@Override
		public void onItemClick(android.widget.AdapterView<?> parent,
				View view, int position, long id) {
			ImageGalleryActivity.launch(mContext, position, folderItem);
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
//			ShareRankingActivity.launch(mContext, sha?>?reItem, pos,? null);
			launcherShareRankingActivity(shareItem.getImageFolder().getJid(),
					shareItem.getImageFolder().getGid(), shareItem
							.getImageFolder().getCreateTime());
		}
	}

	private class MyClickListener implements OnClickListener {
		private int pos;

		public MyClickListener(int position) {
			this.pos = position;
		}
		@Override
		public void onClick(View arg0) {
			ImageFolderItem folderItem = getItem(pos);
//			ShareRankingActivity.launch(mContext, folderItem);
			launcherShareRankingActivity(folderItem.getImageFolder().getJid(),
					folderItem.getImageFolder().getGid(), folderItem
							.getImageFolder().getCreateTime());
		}
	}

	//TODO
	private class ChangeAlbumLT implements OnClickListener {
		private int position;

		//		private Handler mHandler;
		public ChangeAlbumLT(int position) {
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			blurDlg = BBSCustomerDialog.newInstance(mContext,
					R.style.blurdialog);
			blurView = new ChangeAlbumAuthorityView(mContext);
			blurView.setRes(getItem(position).getImageFolder());
			SimpleAuthorityDilaogView dilaogView = new SimpleAuthorityDilaogView(
					mContext);
			dilaogView.setPositiveButton(new ButtonClickListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					//					blurView.setHandler(mHandler);
					String replaceAll = getItem(position).getImageFolder()
							.getCreateTime().replaceAll("-", "");
					blurView.setSendTime(Integer.valueOf(replaceAll));
					blurView.changeAlbumProperty(new onChangeAlbumListen() {
						@Override
						public void onChangePhoto(String signature,
								String authority) {
							getItem(position).getImageFolder().setSignature(
									signature);
							getItem(position).getImageFolder().setAuthority(
									authority);
							updateSingleRow(position);
						}
						@Override
						public void onChangeNotify(String valid, String time) {
							getItem(position).getImageFolder().setNotify_valid(
									valid);
							getItem(position).getImageFolder().setNotify_time(
									time);
							updateSingleRow(position);
						}
					});
				}
			});
			dilaogView.setNegativeButton(new ButtonClickListener() {
				@Override
				public void ensure(View contentView) {
					// TODO Auto-generated method stub
					blurDlg.dismiss();
				}
			});
			dilaogView.setContentView(blurView.getmView());
			blurDlg.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// 弹出框关闭时隐藏掉软键盘.有时候报空指针
					try {
						InputMethodManager imm = (InputMethodManager) mContext
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(((Activity) mContext)
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
					} catch (Exception e) {
					}
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		}
	}

	SimpleImageLoadingListener animateFirstListener = new SimpleImageLoadingListener() {
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				FadeInBitmapDisplayer.animate(imageView, 500);
			}
		}
	};

	public class ImageAdapter extends BaseAdapter {
		private List<VVImage> vvImages;
		private List<ViewHolderTL> listViewHolderTL = new ArrayList<ViewHolderTL>();

		public ImageAdapter(List<VVImage> vvImages) {
			this.vvImages = vvImages;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderTL viewHolder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.gridviewitem, parent,
						false);
				viewHolder = new ViewHolderTL();
				viewHolder.galleryitem = (ImageView) convertView
						.findViewById(R.id.galleryImageView);
				viewHolder.cirleProgressbar = (ProgressBar) convertView
						.findViewById(R.id.loading_cirle_progress);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderTL) convertView.getTag();
			}
			listViewHolderTL.add(viewHolder);
			boolean isLoading = vvImages.get(position).getImageisLoading();
			//LogUtils.i("---ImageAdapter position=" + position + ", isLoading=" + isLoading);
			if (isLoading) {
				viewHolder.cirleProgressbar.setVisibility(View.VISIBLE);
			} else {
				viewHolder.cirleProgressbar.setVisibility(View.GONE);
			}
			ImageLoader.getInstance().cancelDisplayTask(viewHolder.galleryitem);
			ImageLoader.getInstance().displayImage(
					vvImages.get(position).getPathThumb(),
					viewHolder.galleryitem, ImageLoaderConfigers.commonOpt);
			return convertView;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public int getCount() {
			return vvImages.size();
		}
		public void setProgressisVisible(int position) {
			if (vvImages.get(position).getImageisLoading()) {
				//LogUtils.i("position=" + position + ", view is visible =" + View.VISIBLE);
				listViewHolderTL.get(position).cirleProgressbar
						.setVisibility(View.VISIBLE);
			} else {
				//LogUtils.i("position=" + position + ",  view is invisible =" + View.INVISIBLE);
				listViewHolderTL.get(position).cirleProgressbar
						.setVisibility(View.INVISIBLE);
			}
		}
		public List<ViewHolderTL> getListViewHolderTL() {
			return listViewHolderTL;
		}
		public void setVVImages(List<VVImage> vvImages) {
			this.vvImages = vvImages;
		}
	}

	private static class ViewHolderTL {
		public ImageView galleryitem;
		public ProgressBar cirleProgressbar;
	}

	public void setSlideHolder(SlideHolder slideHolder) {
		this.slideHolder = slideHolder;
	}
	public ArrayList<ViewHolder> getListViewHolder() {
		return mViewHolderList;
	}
	@Override
	public List<ImageFolderItem> initItems() {
		return new SortedList<ImageFolderItem>(
				new ArrayList<ImageFolderItem>(),
				new Comparator<ImageFolderItem>() {
					@Override
					public int compare(ImageFolderItem o1, ImageFolderItem o2) {
						return o2.getImageFolder().getCreateTime()
								.compareTo(o1.getImageFolder().getCreateTime());
					}
				}, new Equalable<ImageFolderItem>() {
					@Override
					public boolean equal(ImageFolderItem t1, ImageFolderItem t2) {
						ImageFolder folder1 = t1.getImageFolder();
						ImageFolder folder2 = t2.getImageFolder();
						return folder1.getGid().equals(folder2.getGid())
								&& folder1.getCreateTime().equals(
										folder2.getCreateTime())
								&& folder1.getJid().equals(folder2.getJid());
					}
				});
	}
	// 获取列表中第一条数据的创建时间
	public String getStartTime() {
		if (getCount() > 0) {
			return getItem(0).getImageFolder().getCreateTime();
		} else {
			return "";
		}
	}
	// 获取最后一条的创建时间
	public String getEndTime() {
		if (getCount() > 0) {
			return getItem(getCount() - 1).getImageFolder().getCreateTime();
		} else {
			return "";
		}
	}
	@Override
	public void addItems(Collection<ImageFolderItem> inputs) {
		for (ImageFolderItem input : inputs) {
			addItem(input);
		}
	}
	@Override
	public void addItem(ImageFolderItem input) {
		if (input.getImageFolder() != null) {
			for (Iterator<ImageFolderItem> it = getItems().iterator(); it
					.hasNext();) {
				String createTime = it.next().getImageFolder().getCreateTime();
				if (createTime.equals(input.getImageFolder().getCreateTime())) {
					it.remove();
					break;
				}
			}
			super.addItem(input);
		}
	}
	
	private void launcherShareRankingActivity(String jid, String gid, String gidCreateTime) {
		Intent i = new Intent(mContext, ShareRankingActivity.class);
		i.putExtra("jid", jid);
		i.putExtra("gid", gid);
		i.putExtra("gidCreatTime", gidCreateTime);
		mContext.startActivity(i);
	}
}
