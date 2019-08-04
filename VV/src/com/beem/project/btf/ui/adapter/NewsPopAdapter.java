package com.beem.project.btf.ui.adapter;

import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.MaterialLoadingActivity;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType;
import com.beem.project.btf.utils.DimenUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NewsPopAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<NewsImageInfo> mDatas;
	private int currentPosition = -1;
	private int oldPosition = -1;
	private Animation mRotateUpAnim;
	private final float TRANSLATIONY;
	private boolean isAlphaAnim = true;
	private CurrentPotionListener currentpotionlistener;
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.transparent)
			.showImageOnFail(R.drawable.transparent).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private SparseArray<ViewHolder> holders = new SparseArray<NewsPopAdapter.ViewHolder>();
	private String groupid;

	public NewsPopAdapter(Context context, List<NewsImageInfo> mDatas) {
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
		TRANSLATIONY = -DimenUtils.dip2px(mContext, 5);
	}

	public interface CurrentPotionListener {
		void getcurrentpotion(int position);
	}

	public CurrentPotionListener getCurrentpotionlistener() {
		return currentpotionlistener;
	}
	public void setCurrentpotionlistener(
			CurrentPotionListener currentpotionlistener) {
		this.currentpotionlistener = currentpotionlistener;
	}
	@Override
	public int getCount() {
		return mDatas == null ? 1 : mDatas.size() + 1;
	}
	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void setCurrentPotion(int position, boolean isAnim) {
		if (currentPosition != position) {
			oldPosition = currentPosition;
			currentPosition = position;
			final ViewHolder viewHolder1 = holders.get(currentPosition);
			final ViewHolder viewHolder2 = holders.get(oldPosition);
			if (isAnim) {
				if (viewHolder1 != null) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(
							viewHolder1.galleryitem, "translationY", 0,
							TRANSLATIONY).setDuration(80);
					anim.addListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							super.onAnimationEnd(animation);
							viewHolder1.galleryitem.setSelected(true);
							if (viewHolder2 != null
									&& viewHolder2 != viewHolder1) {//viewholder会有重用的现象
								if (ViewHelper
										.getTranslationY(viewHolder2.galleryitem) == TRANSLATIONY) {//可能viewholder被 重用位置复原到0了，所以只对没复原的做动画
									ObjectAnimator anim = ObjectAnimator
											.ofFloat(viewHolder2.galleryitem,
													"translationY",
													TRANSLATIONY, 0.0f)
											.setDuration(80);
									anim.start();
									viewHolder2.galleryitem.setSelected(false);
								}
							}
						}
					});
					anim.start();
				}
			} else {
				if (viewHolder1 != null) {
					ViewHelper.setTranslationY(viewHolder1.galleryitem,
							TRANSLATIONY);
					viewHolder1.galleryitem.setSelected(true);
				}
				if (viewHolder2 != null && viewHolder1 != viewHolder2) {
					ViewHelper.setTranslationY(viewHolder2.galleryitem, 0);
					viewHolder2.galleryitem.setSelected(false);
				}
			}
		}
		if (currentpotionlistener != null && currentPosition != getCount() - 1) {
			currentpotionlistener.getcurrentpotion(currentPosition);
		}
	}
	public void setAlphaAnim(boolean isanim) {
		if (!isanim || !isAlphaAnim) {
			isAlphaAnim = isanim;
		}
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.news_camera_editor_imgitem, parent, false);
			viewHolder.galleryitem = (ImageView) convertView
					.findViewById(R.id.news_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		holders.put(position, viewHolder);
		if (isAlphaAnim) {
			ObjectAnimator anim1 = ObjectAnimator.ofFloat(
					viewHolder.galleryitem, "alpha", 0.0f, 1.0f).setDuration(
					500);
			anim1.start();
		}
		if (currentPosition == position) {
			ViewHelper.setTranslationY(viewHolder.galleryitem, TRANSLATIONY);
			viewHolder.galleryitem.setSelected(true);
		} else {
			ViewHelper.setTranslationY(viewHolder.galleryitem, 0.0f);
			viewHolder.galleryitem.setSelected(false);
		}
		if (position == (getCount() - 1)) {
			viewHolder.galleryitem
					.setImageResource(R.drawable.add_materials_icon_v);
		} else {
			ImageLoader.getInstance().displayImage(
					mDatas.get(position).getPathThumb(),
					viewHolder.galleryitem, defaultOptions);
		}
		viewHolder.galleryitem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (position == getCount() - 1) {
					// 图片下载管理界面
					MaterialLoadingActivity.launch(mContext,
							NewsMaterialType.getMaterialTypeEx(groupid));
				} else {
					setAlphaAnim(false);
					setCurrentPotion(position, true);
				}
			}
		});
		return convertView;
	}
	public void setItems(String groupid, List<NewsImageInfo> list,
			int currentPosition) {
		this.mDatas = list;
		this.currentPosition = currentPosition;
		this.oldPosition = -1;
		this.groupid = groupid;
	}

	class ViewHolder {
		public ImageView galleryitem;
	}

	public String getGroupid() {
		return groupid;
	}
}
