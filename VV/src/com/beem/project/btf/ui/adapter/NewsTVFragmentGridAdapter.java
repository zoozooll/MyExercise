package com.beem.project.btf.ui.adapter;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo.NewsTextType;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description:gridview的Adapter
 */
public class NewsTVFragmentGridAdapter extends BaseAdapter {
	private Context mContext;
	public NewsTextType[] img_text = { NewsTextType.subtitle,
			NewsTextType.bigtitle, NewsTextType.smalltitle,
			NewsTextType.marquee, NewsTextType.area, NewsTextType.righttop,
			NewsTextType.movie, NewsTextType.live };
	public int[] img_small_text = { R.string.Subtitle, R.string.BigTitle,
			R.string.SmallTitle, R.string.Marquee, R.string.Area,
			R.string.RightTop, R.string.Movie, R.string.Live };
	public int[] imgs = { R.drawable.subtitle_selector,
			R.drawable.bigtitle_selector, R.drawable.smalltitle_selector,
			R.drawable.marquee_selector, R.drawable.area_selector,
			R.drawable.righttop_selector, R.drawable.movie_selector,
			R.drawable.live_selector };
	private NewsCameraImageInfo currentImageInfo;
	private SparseArray<Boolean> currentlist;
	private OnClickListener lis;

	public NewsTVFragmentGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public void setTextInfo(NewsCameraImageInfo tempinfo) {
		this.currentImageInfo = tempinfo;
		currentlist = new SparseArray<Boolean>();
		for (NewsTextType type : NewsTextType.values()) {
			currentlist.put(type.ordinal(), true);
			if (currentImageInfo.getTextInfo(type) == null) {
				if (type.ordinal() < 5) {
					currentlist.setValueAt(type.ordinal(), false);
				}
			}
		}
	}
	public void setLis(OnClickListener lis) {
		this.lis = lis;
	}
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.newstv_fragment_grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		TextView tv_small = BaseViewHolder.get(convertView, R.id.tv_small_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		View view = BaseViewHolder.get(convertView, R.id.tv_wraper);
		iv.setBackgroundResource(imgs[position]);
		tv.setText(img_text[position].getName());
		//关闭某些功能
		view.setTag(position);
		view.setOnClickListener(lis);
		if (currentlist != null) {
			boolean isenabled = currentlist.get(position);
			Log.i("yang", "isenabled~" + isenabled + " position:" + position);
			if (isenabled) {
				if (position < 5) {
					tv_small.setText(currentImageInfo.getTextInfo(
							img_text[position]).getNotetext());
				} else
					tv_small.setText(currentImageInfo
							.getTextString(img_text[position]));
			} else {
				tv_small.setText(BeemApplication.getContext().getResources()
						.getString(img_small_text[position]));
			}
			tv.setEnabled(isenabled);
			tv_small.setEnabled(isenabled);
			iv.setEnabled(isenabled);
			view.setClickable(isenabled);
			view.setEnabled(isenabled);
		}
		return convertView;
	}
}
