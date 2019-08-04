package com.beem.project.btf.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.adapter.ThumbnailAdapter;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.ShareType;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.ui.fragment.TalkFragement;
import com.beem.project.btf.ui.fragment.TalkFragement.FragmentType;
import com.beem.project.btf.ui.fragment.TalkFragement.SendmsgListenter;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.wxapi.InfoMessage;
import com.btf.push.Item;
import com.butterfly.vv.adapter.SharerankingCommentAdapter;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.LikedPhotoGroup;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.Comment;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.view.timeflyView.HorizontalListView;
import com.butterfly.vv.vv.utils.CToast;
import com.mob.tools.utils.UIHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.pullToRefresh.ui.PullToRefreshListView;

import de.greenrobot.event.EventBus;

public class ShareRankingActivity extends VVBaseFragmentActivity implements
		IEventBusAction, OnClickListener, PlatformActionListener, Callback {
	protected static final String TAG = ShareRankingActivity.class
			.getSimpleName();
	private int position;
	private ShareType page;
	public ArrayList<VVImage> mUpImageList = new ArrayList<VVImage>();
	private ImageFolderItem folderItem;
	private String nextAct;
	private View layout_topbar;
	private ImageView imv_shareEdit;
	private TextView tvw_Title;
	private ImageView imv_playenter;
	private PullToRefreshListView app_list;
	private ListView listView;
	//	private TextView ownername;
	private View replyLvHeadView;
	private ViewPager bigPicPager;
	private ImageView pic_btn;
	private TextView name;
	private TextView pager_current;
	private HorizontalListView thumbnail_list;
	private View imv_arrow;
	private View comment_nodata;
	private ThumbnailAdapter thumbnailAdapter;
	//	private View tags_bar;
	private TextView signText;
	private View layout_shareLike;
	private View layout_shareComment;
	private View layout_shareShare;
	private View pwViewShare;
	private PopupWindow pwShare;
	private SharerankingCommentAdapter replyListViewAdapter;
	private DisplayImageOptions defaultOptions = ImageLoaderConfigers.sDefaultOptions;
	private PicPagerAdapter picPagerAdapter;
	// Field for animation of topbar;
	private int topbarMaxHeight;
	private int topbarMinHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_shareranking);
		Intent intent = getIntent();
//		folderItem = intent.getParcelableExtra("shareTrancItem");
		nextAct = intent.getStringExtra("next");
		initViews();
		if (intent.hasExtra("position") && intent.hasExtra("page")) {
			position = intent.getIntExtra("position", 0);
			page = ShareType.values()[intent.getIntExtra("page", 0)];
		}
		
		String jid = intent.getStringExtra("jid");
		String gid = intent.getStringExtra("gid");
		String gidCreatTime = intent.getStringExtra("gidCreatTime");
		if (gidCreatTime.length() > 10) {
			gidCreatTime = gidCreatTime.substring(0, 10);
		}
		loadFolderItem(jid, gid, gidCreatTime);
		EventBus.getDefault().register(this);
		ShareSDK.initSDK(mContext);
	}
	private void initViews() {
		// 导航条
		layout_topbar = findViewById(R.id.layout_topbar);
		View back = findViewById(R.id.back);
		imv_shareEdit = (ImageView) findViewById(R.id.imv_shareEdit);
		tvw_Title = (TextView) findViewById(R.id.tvw_Title);
		tvw_Title.setOnClickListener(this);
		imv_playenter = (ImageView) findViewById(R.id.imv_playenter);
		//中部列表
		app_list = (PullToRefreshListView) findViewById(R.id.app_list);
		app_list.setPullRefreshEnabled(false);
		listView = app_list.getRefreshableView();
		imv_playenter.setOnClickListener(this);
		back.setOnClickListener(this);
		// 列表头部（图片详情信息放在列表的headerview）
		if (replyLvHeadView == null) {
			replyLvHeadView = LayoutInflater.from(this).inflate(
					R.layout.include_shareranking_headview, null);
		}
		listView.addHeaderView(replyLvHeadView);
		setupHeaderView(replyLvHeadView);
		// 底部按钮栏；
		layout_shareLike = findViewById(R.id.layout_shareLike);
		layout_shareComment = findViewById(R.id.layout_shareComment);
		layout_shareShare = findViewById(R.id.layout_shareShare);
		layout_shareLike.setOnClickListener(this);
		layout_shareComment.setOnClickListener(this);
		layout_shareShare.setOnClickListener(this);
	}
	private void setupHeaderView(View view) {
		bigPicPager = (ViewPager) replyLvHeadView.findViewById(R.id.picPager);
		pic_btn = (ImageView) view.findViewById(R.id.pic_btn);
		name = (TextView) view.findViewById(R.id.name);
		pager_current = (TextView) view.findViewById(R.id.pager_current);
		//		tags_bar = view.findViewById(R.id.tags_bar);
		signText = (TextView) view.findViewById(R.id.tags_content);
		thumbnail_list = (HorizontalListView) view
				.findViewById(R.id.thumbnail_list);
		imv_arrow = findViewById(R.id.imv_arrow);
		comment_nodata = findViewById(R.id.comment_nodata);
	}
	private void onDetailDateLoaded() {
		tvw_Title.setText(BBSUtils.getTimeDurationString(folderItem
				.getImageFolder().getCreateTime()));
		String descripter = folderItem.getImageFolder().getSignature();
		signText.setVisibility(TextUtils.isEmpty(descripter) ? View.GONE : View.VISIBLE);
		SpannableString spanString = new SpannableString("    ");
		Drawable d = getResources().getDrawable(R.drawable.share_signiconl);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		ImageSpan span = new ImageSpan(d, DynamicDrawableSpan.ALIGN_BASELINE);
		spanString.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		signText.setText(spanString);
		signText.append(descripter);
		name.setText(BBSUtils.replaceBlank(folderItem.getContact()
				.getNickName()));
		// 根据性别加载头像
		folderItem.getContact().displayPhoto(pic_btn);
		pic_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				ContactInfoActivity.launch(mContext, folderItem.getContact());
			}
		});
		thumbnail_list.setNeedResetLocate(false);
		thumbnailAdapter = new ThumbnailAdapter(mContext,
				folderItem.getVVImages());
		thumbnail_list.setAdapter(thumbnailAdapter);
		thumbnail_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				bigPicPager.setCurrentItem(position);
				thumbnailAdapter.setSelectedPosition(position);
//				thumbnail_list.setSelection(position);
			}
		});
		
		setUpDots();
		// 头部图片适配器
		picPagerAdapter = new PicPagerAdapter(mContext);
		bigPicPager.setAdapter(picPagerAdapter);
		bigPicPager.setOnPageChangeListener(picPagerLis);
		
		// 回复列表适配器
		replyListViewAdapter = new SharerankingCommentAdapter(mContext);
		listView.setAdapter(replyListViewAdapter);
		replyListViewAdapter.addItems(folderItem.getComments());
		replyListViewAdapter.notifyDataSetChanged();
		boolean noMoreData = replyListViewAdapter.getCount() >= folderItem
				.getImageFolder().getCommentCount();
		app_list.onPullRefreshComplete(noMoreData);
		app_list.setOnRefreshListener(commentRefreshLis);
		layout_shareLike.setSelected(LikedPhotoGroup.isLiked(folderItem
				.getImageFolder().getGid(), folderItem.getImageFolder()
				.getCreateTime(), folderItem.getImageFolder().getJid()));
		((TextView) layout_shareLike.findViewById(R.id.tvw_shareLike))
				.setText(folderItem.getImageFolder().isThumbup() ? getString(R.string.share_liked)
						+ "  " + folderItem.getImageFolder().getThumbupCount()
						: getString(R.string.share_like) + "  "
								+ folderItem.getImageFolder().getThumbupCount());
		((TextView) layout_shareComment.findViewById(R.id.tvw_shareComment))
				.setText(getString(R.string.share_comment) + "  "
						+ folderItem.getImageFolder().getCommentCount());
		imv_arrow
				.setVisibility(folderItem.getImageFolder().getCommentCount() > 0 ? View.VISIBLE
						: View.GONE);
		comment_nodata.setVisibility(folderItem.getImageFolder()
				.getCommentCount() > 0 ? View.GONE : View.VISIBLE);
		new DownTask(this, folderItem, nextAct, new IGetCommentRst() {
			@Override
			public void onResult(List<CommentItem> commentItem, Start next,
					boolean isTimeout) {
				if (commentItem != null && commentItem.size() > 0) {
					folderItem.addComments(commentItem);
				}
				onCommentDataLoaded();
			}
		}, false).execute();
	}

	private OnRefreshListener<ListView> commentRefreshLis = new OnRefreshListener<ListView>() {
		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView, PullType pullType) {
			downDask(false);
		}
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView,
				PullType pullType) {
			downDask(true);
		}
	};

	private void downDask(final boolean isLoadMore) {
		new DownTask(mContext, folderItem, nextAct, new IGetCommentRst() {
			@Override
			public void onResult(List<CommentItem> comments, Start next,
					boolean isTimeout) {
				if (comments != null && !comments.isEmpty()) {
					for (CommentItem commentItem2 : comments) {
						replyListViewAdapter.addItem(commentItem2);
					}
					replyListViewAdapter.notifyDataSetChanged();
				}
				if (!next.isEnd()) {
					if (isLoadMore) {
						nextAct = next.getVal();
					} else {
						nextAct = null;
					}
				}
				boolean noMoreData = replyListViewAdapter.getCount() >= folderItem
						.getImageFolder().getCommentCount();
				app_list.onPullRefreshComplete(noMoreData);
			}
		}, false).execute();
	}
	private void showSharePopup() {
		pwShare = new PopupWindow(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		final WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.5f;
		getWindow().setAttributes(lp);
		if (pwViewShare == null) {
			pwViewShare = LayoutInflater.from(this).inflate(
					R.layout.popup_share, null);
			pwViewShare.findViewById(R.id.layout_share_wechatmoment)
					.setOnClickListener(this);
			pwViewShare.findViewById(R.id.layout_share_wechat)
					.setOnClickListener(this);
			pwViewShare.findViewById(R.id.layout_share_qq).setOnClickListener(
					this);
			pwViewShare.findViewById(R.id.layout_share_qqzone)
					.setOnClickListener(this);
			pwViewShare.findViewById(R.id.layout_share_weibo)
					.setOnClickListener(this);
			pwViewShare.findViewById(R.id.layout_share_more)
					.setOnClickListener(this);
		}
		pwShare.setBackgroundDrawable(new ColorDrawable());
		pwShare.setContentView(pwViewShare);
		pwShare.setOutsideTouchable(true);
		pwShare.showAtLocation(getRootView(), Gravity.BOTTOM, 0, 0);
		pwShare.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
	}
	private boolean dismissSharePop() {
		if (pwShare != null && pwShare.isShowing()) {
			pwShare.dismiss();
			return true;
		}
		return false;
	}
	@Override
	public void onBackPressed() {
		if (!dismissSharePop()) {
			super.onBackPressed();
		}
	}
	// 设置页数
	private void setPagerCount(int pageIndex, int pageTotal) {
		pager_current.setText(pageIndex + 1 + "/" + pageTotal);
	}
	private void onCommentDataLoaded() {
	}
	// 初始化页数并装载图片集合
	private void setUpDots() {
		int count = folderItem.getVVImages().size();
		setPagerCount(0, count);
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		EventAction action = data.getAction();
		switch (action) {
			case UploadTimeflyPhotoAdd: {
				folderItem = (ImageFolderItem) data.getMsg();
				picPagerAdapter.notifyDataSetChanged();
				break;
			}
			case TimeflyImageDelete: {
				String[] imageid = (String[]) data.getMsg();
				folderItem.getVVImages().remove(Integer.parseInt(imageid[1]));
				int count = folderItem.getVVImages().size();
				setPagerCount(0, count);
				picPagerAdapter.notifyDataSetChanged();
				bigPicPager.setCurrentItem(0);
				break;
			}
			case ShareCommentChange: {
				PraiseEventBusData pBusData = (PraiseEventBusData) data
						.getMsg();
				CommentItem commentItem = pBusData.getComment();
				replyListViewAdapter.addItem(commentItem);
				replyListViewAdapter.notifyDataSetChanged();
				folderItem.getImageFolder().setCommentCount(
						folderItem.getImageFolder().getCommentCount() + 1);
				TextView replyNum_tv = (TextView) layout_shareComment
						.findViewById(R.id.tvw_shareComment);
				replyNum_tv.setText(getString(R.string.share_comment) + "  "
						+ (folderItem.getImageFolder().getCommentCount()));
				imv_arrow.setVisibility(folderItem.getImageFolder()
						.getCommentCount() > 0 ? View.VISIBLE : View.GONE);
				comment_nodata.setVisibility(folderItem.getImageFolder()
						.getCommentCount() > 0 ? View.GONE : View.VISIBLE);
				break;
			}
			default:
				break;
		}
	}
	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			case R.id.layout_shareComment: {
				if (!LoginManager.getInstance().isLogined()) {
					CToast.showToast(BeemApplication.getContext(), R.string.like_error_unlogin,
							Toast.LENGTH_SHORT);
					ActivityController.getInstance().gotoLogin();
					return;
				}
				TalkFragement talkFragement = TalkFragement.newFragment(FragmentType.dialog);
				talkFragement.show(((FragmentActivity) mContext)
						.getSupportFragmentManager(), "");
				String id = folderItem.getContact().getJid()
						+ folderItem.getImageFolder().getGid()
						+ folderItem.getImageFolder().getCreateTime();
				id = id.trim().replace("-", "");
				talkFragement.setTypeTDialog(id);
				talkFragement.setCommentedSuitableName(folderItem.getContact()
						.getSuitableName());
				// 发送评论
				talkFragement.setSendmsgListenter(new SendmsgListenter() {
					@Override
					public void sendmsg(TalkFragement fragment, String str) {
						TimeflyService.executeCommentPG(mContext, folderItem
								.getContact().getJid(), folderItem
								.getImageFolder().getGid(), folderItem
								.getImageFolder().getCreateTime(), str,
								Comment.cid_first, folderItem.getContact(),
								null);
					}
				});
				break;
			}
			case R.id.layout_shareLike: {
				if (!LoginManager.getInstance().isLogined()) {
					CToast.showToast(BeemApplication.getContext(), R.string.like_error_unlogin,
							Toast.LENGTH_SHORT);
					ActivityController.getInstance().gotoLogin();
					return;
				}
				if (folderItem == null) {
					return ;
				}
				final ImageFolder folder = folderItem.getImageFolder();
				if (folder == null) {
					return ;
				}
				if (v.isSelected()) {
					CToast.showToast(BeemApplication.getContext(), R.string.like_error_double,
							Toast.LENGTH_SHORT);
					return;
				}
				TimeflyService.executeThumbUp(mContext, folder,
						new onPacketResult<Map<String, Object>>() {
							@Override
							public void onResult(Map<String, Object> result,
									boolean timeout, Start start) {
								folder.setThumbupCount(folder.getThumbupCount() + 1);
								folderItem.getImageFolder().setThumbup(true);
								TextView praise_tv = (TextView) v
										.findViewById(R.id.tvw_shareLike);
								v.setSelected(true);
								praise_tv
										.setText(getString(R.string.share_liked)
												+ "  "
												+ folder.getThumbupCount());
								PraiseEventBusData praisedata = new PraiseEventBusData(
										folderItem.getImageFolder().getJid(),
										folderItem.getImageFolder().getGid(),
										folderItem.getImageFolder()
												.getCreateTime(), true);
								EventBus.getDefault().post(
										new EventBusData(
												EventAction.ShareSupportChange,
												praisedata));
								LikedPhotoGroup lpg = new LikedPhotoGroup();
								lpg.setLikedTime(System.currentTimeMillis());
								lpg.setGid(folder.getGid());
								lpg.setGid_creat_time(BBSUtils.getShortGidCreatTime(folder.getCreateTime()));
								lpg.setGid_jid(folder.getJid());
								lpg.saveToDatabase();
							}
						});
				
			}
				break;
			case R.id.layout_shareShare:
				showSharePopup();
				break;
			case R.id.tvw_Title:
			case R.id.back:
				onBackPressed();
				break;
			case R.id.imv_playenter: {
				String url = folderItem.getImageFolder().getAlbum_url();
				if (!TextUtils.isEmpty(url)) {
					Intent i = new Intent(this,
							FolderItemVideoPlayerActivity.class);
					i.setData(Uri.parse(url));
					startActivity(i);
				} else {
					//					Intent i = new Intent(this, FolderItemVideoPlayerActivity.class);
					//					startActivity(i);
					Toast.makeText(this, R.string.shareranking_novideo,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case R.id.layout_share_wechatmoment: {
				share_circleFriend();
				break;
			}
			case R.id.layout_share_wechat: {
				share_wxFriend();
				break;
			}
			case R.id.layout_share_qq: {
				share_qqFriend();
				break;
			}
			case R.id.layout_share_qqzone: {
				share_qzone();
				break;
			}
			case R.id.layout_share_weibo: {
				share_sinaWeibo();
				break;
			}
			case R.id.layout_share_more: {
				Intent localIntent = new Intent("android.intent.action.SEND");
				localIntent.setType("image/*");
				File imageFile = new File(folderItem.getVVImages().get(0)
						.getPath());
				Uri imageUri = Uri.fromFile(imageFile);
				localIntent.putExtra("android.intent.extra.STREAM", imageUri);
				startActivity(Intent.createChooser(localIntent, "分享至"));
				break;
			}
		}
	}
	
	private void loadFolderItem(final String jid, final String gid, final String gidCreatTime) {
		new VVBaseLoadingDlg<ImageFolderItem>(
				new VVBaseLoadingDlgCfg(this).setShowWaitingView(true)) {
			@Override
			protected ImageFolderItem doInBackground() {
				ImageFolderItem imageFolderItem = TimeflyService
						.getPhotoGroupDetail(jid, gid, gidCreatTime,
								!BeemApplication.isNetworkOk());
				return imageFolderItem;
			}
			@Override
			protected void onPostExecute(ImageFolderItem result) {
				if (result != null) {
					folderItem = result;
					onDetailDateLoaded();
				} else {
					CToast.showToast(ShareRankingActivity.this, "获取数据失败", Toast.LENGTH_SHORT);
				}
			}
		}.execute();
	}

	private class PicPagerAdapter extends PagerAdapter {
		public PicPagerAdapter(Context mContext) {
			super();
		}
		@Override
		public int getCount() {
			return folderItem.getImageFolder().getPhotoCount();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(View container, final int position) {
			final ImageView imgView = new ImageView(mContext);
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			imgView.setLayoutParams(lp);
			imgView.setScaleType(ScaleType.CENTER_CROP);
			imgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 跳转到图片浏览器
					ImageGalleryActivity.launch(mContext, position, folderItem);
				}
			});
			folderItem.getVVImages().get(position).getPath();
			((ViewPager) container).addView(imgView);
			ImageLoader.getInstance().displayImage(
					folderItem.getVVImages().get(position).getPathThumb(),
					imgView, defaultOptions, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							ImageLoader.getInstance()
									.displayImage(
											folderItem.getVVImages()
													.get(position).getPath(),
											imgView, defaultOptions);
						}
					});
			return imgView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	// 联网获取数据，从一个activity跳转进来，先启动界面再加载数据，在oncreate()方法里就启动此任
	public static class DownTask extends VVBaseLoadingDlg<List<CommentItem>> {
		private final IGetCommentRst rstLis;
		private final ImageFolderItem imageFolderItem;
		private Start start;

		public DownTask(Context context, ImageFolderItem folderItem,
				String next, IGetCommentRst rstLis, boolean... isShowWaitDlg) {
			super(new VVBaseLoadingDlgCfg(context)
							.setShowWaitingView(isShowWaitDlg.length > 0 ? isShowWaitDlg[0]
									: false));
			this.rstLis = rstLis;
			this.imageFolderItem = folderItem;
			this.start = new Start(next);
		}
		@Override
		// 执行联网的耗时操作
		protected List<CommentItem> doInBackground() {
			if (imageFolderItem != null) {
				List<CommentItem> comments = TimeflyService
						.getPhotoGroupComments(imageFolderItem.getContact()
								.getJid(), imageFolderItem.getImageFolder()
								.getGid(), imageFolderItem.getImageFolder()
								.getCreateTime(), start, 10, !BeemApplication
								.isNetworkOk());
				return comments;
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<CommentItem> comment) {
			if (rstLis != null) {
				rstLis.onResult(comment, start, false);
			}
		}
		@Override
		protected void onTimeOut() {
			super.onTimeOut();
			if (rstLis != null) {
				rstLis.onResult(null, start, true);
			}
		}
	}

	// 页面切换监听器
	private OnPageChangeListener picPagerLis = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int pos) {
			setPagerCount(pos, folderItem.getVVImages().size());
			thumbnailAdapter.setSelectedPosition(pos);
			thumbnailAdapter.notifyDataSetChanged();
			thumbnail_list.setSelection(pos);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	public interface IGetCommentRst {
		void onResult(List<CommentItem> comments, Start start, boolean isTimeout);
	}

	/**
	 * 分享到朋友圈
	 */
	private void share_circleFriend() {
		ShareParams wechatMoments = new ShareParams();
		String suitableName = getSuitableName();
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getTopic())) {
			wechatMoments.setTitle(folderItem.getImageFolder().getTopic());
		} else {
			wechatMoments.setTitle(suitableName + "的时光相册");
		}
		wechatMoments.setUrl(folderItem.getImageFolder().getAlbum_url());
		wechatMoments.setImageUrl(folderItem.getVVImages().get(0)
				.getPathThumb());
		wechatMoments.setShareType(Platform.SHARE_WEBPAGE);
		Platform weixin = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
		weixin.setPlatformActionListener(this);
		weixin.share(wechatMoments);
	}
	/**
	 * 分享到微信好友
	 */
	private void share_wxFriend() {
		ShareParams wechat = new ShareParams();
		String suitableName = getSuitableName();
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getTopic())) {
			wechat.setTitle(folderItem.getImageFolder().getTopic());
		} else {
			wechat.setTitle(suitableName + "的时光相册");
		}
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getSignature())) {
			String substring = folderItem.getImageFolder().getSignature()
					.substring(0, 50);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(substring).append("...");
			wechat.setText(stringBuffer.toString());
		} else {
			wechat.setText("我刚刚上传了一组自己的时光相册,大家快来围观吧!");
		}
		wechat.setImageUrl(folderItem.getVVImages().get(0).getPathThumb());
		wechat.setUrl(folderItem.getImageFolder().getAlbum_url());
		wechat.setShareType(Platform.SHARE_WEBPAGE);
		Platform weixin = ShareSDK.getPlatform(mContext, Wechat.NAME);
		weixin.setPlatformActionListener(this);
		weixin.share(wechat);
	}
	/**
	 * 分享到qq好友
	 */
	private void share_qqFriend() {
		ShareParams qq = new ShareParams();
		String suitableName = getSuitableName();
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getTopic())) {
			qq.setTitle(folderItem.getImageFolder().getTopic());
		} else {
			qq.setTitle(suitableName + "的时光相册");
		}
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getSignature())) {
			String substring = folderItem.getImageFolder().getSignature()
					.substring(0, 50);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(substring).append("...");
			qq.setText(stringBuffer.toString());
		} else {
			qq.setText("我刚刚上传了一组自己的时光相册,大家快来围观吧!");
		}
		qq.setImageUrl(folderItem.getVVImages().get(0).getPathThumb());
		qq.setTitleUrl(folderItem.getImageFolder().getAlbum_url());
		qq.setShareType(Platform.SHARE_WEBPAGE);
		Platform qqq = ShareSDK.getPlatform(mContext, QQ.NAME);
		qqq.setPlatformActionListener(this);
		qqq.share(qq);
	}
	/**
	 * 分享到qq空间
	 */
	private void share_qzone() {
		ShareParams qq = new ShareParams();
		String suitableName = getSuitableName();
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getTopic())) {
			qq.setTitle(folderItem.getImageFolder().getTopic());
		} else {
			qq.setTitle(suitableName + "的时光相册");
		}
		if (!TextUtils.isEmpty(folderItem.getImageFolder().getSignature())) {
			String substring = folderItem.getImageFolder().getSignature()
					.substring(0, 50);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(substring).append("...");
			qq.setText(stringBuffer.toString());
		} else {
			qq.setText("我刚刚上传了一组自己的时光相册,大家快来围观吧!");
		}
		qq.setImageUrl(folderItem.getVVImages().get(0).getPathThumb());
		qq.setTitleUrl(folderItem.getImageFolder().getAlbum_url());
		qq.setSite("时光机_时光团队");
		qq.setSiteUrl(folderItem.getImageFolder().getAlbum_url());
		Platform qqq = ShareSDK.getPlatform(mContext, QZone.NAME);
		qqq.setPlatformActionListener(this);
		qqq.share(qq);
	}
	/**
	 * 分享到新浪微博
	 */
	private void share_sinaWeibo() {
		ShareParams sp = new ShareParams();
		String suitableName = getSuitableName();
		if (!TextUtils.isEmpty(suitableName)) {
			String substring = folderItem.getImageFolder().getSignature()
					.substring(0, 50);
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(substring).append("...");
			sp.setText(stringBuffer.toString());
		} else {
			sp.setText("我刚刚上传了一组自己的时光相册,大家快来围观吧!");
		}
		sp.setImageUrl(folderItem.getVVImages().get(0).getPath());
		Platform weibo = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
		weibo.setPlatformActionListener(this); // 设置分享事件回调
		weibo.SSOSetting(true);
		// 执行图文分享
		weibo.share(sp);
	}
	
	private String getSuitableName() {
		String suitableName = folderItem.getContact().getNickName();
		if (TextUtils.isEmpty(suitableName)) {
			suitableName = folderItem.getContact().getName();
		}
		if (TextUtils.isEmpty(suitableName)) {
			suitableName = folderItem.getContact().getJIDParsed();
		}
		if (TextUtils.isEmpty(suitableName)) {
			suitableName = getString(R.string.app_name);
		}
		return suitableName;
	}
	@Override
	public boolean handleMessage(Message msg) {
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
			case 1:
				// 成功
				text = "分享成功";
				InfoMessage.showMessage(this, text);
				ShareRankingActivity.this.finish();
				break;
			case 2:
				// 失败
				text = "分享失败";
				break;
			case 3:
				// 取消
				text = "分享已取消";
				break;
		}
		InfoMessage.showMessage(this, text);
		return false;
	}
	@Override
	public void onCancel(Platform palt, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}
	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> arg2) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}
	@Override
	public void onError(Platform arg0, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		UIHandler.sendMessage(msg, this);
	}
	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
			case Platform.ACTION_AUTHORIZING:
				return "ACTION_AUTHORIZING";
			case Platform.ACTION_GETTING_FRIEND_LIST:
				return "ACTION_GETTING_FRIEND_LIST";
			case Platform.ACTION_FOLLOWING_USER:
				return "ACTION_FOLLOWING_USER";
			case Platform.ACTION_SENDING_DIRECT_MESSAGE:
				return "ACTION_SENDING_DIRECT_MESSAGE";
			case Platform.ACTION_TIMELINE:
				return "ACTION_TIMELINE";
			case Platform.ACTION_USER_INFOR:
				return "ACTION_USER_INFOR";
			case Platform.ACTION_SHARE:
				return "ACTION_SHARE";
			default: {
				return "UNKNOWN";
			}
		}
	}
	private void startShareActivity(String shareAction, int type) {
		HashMap<String, ResolveInfo> hashMapResolveInfo = new HashMap<String, ResolveInfo>();
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		PackageManager pm = getPackageManager();
		final List<ResolveInfo> infos = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		CharSequence[] names = new CharSequence[infos.size()];
		for (int i = 0; i < infos.size(); i++) {
			names[i] = infos.get(i).loadLabel(pm);
			String uipn = infos.get(i).activityInfo.name;
			if (uipn.equals(shareAction)) {
				hashMapResolveInfo.put(uipn, infos.get(i));
				break;
			}
		}
		ResolveInfo info = hashMapResolveInfo.get(shareAction);
		if (info == null) {
			switch (type) {
				case 5:
					Toast.makeText(ShareRankingActivity.this, "请安装微博客户端",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
			return;
		}
	}
	
	
}
