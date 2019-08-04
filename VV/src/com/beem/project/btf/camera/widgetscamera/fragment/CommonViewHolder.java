package com.beem.project.btf.camera.widgetscamera.fragment;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: BaseViewHolder
 * @Description: 通用ViewHolder
 * @author: yuedong bao
 * @date: 2015-11-10 下午2:26:53
 */
public class CommonViewHolder {
	private final SparseArray<View> views;
	private final View convertView;
	private final ViewGroup parent;
	private final int position;

	public int getPosition() {
		return position;
	}
	public CommonViewHolder(SparseArray<View> views, View convertView, ViewGroup parent, int pos) {
		super();
		this.views = views;
		this.convertView = convertView;
		this.position = pos;
		this.parent = parent;
	}
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int id) {
		CommonViewHolder holder = (CommonViewHolder) convertView.getTag();
		if (holder == null) {
			throw new IllegalStateException("you must call the getViewHolder first");
		}
		T retView = (T) holder.views.get(id);
		if (retView == null) {
			retView = (T) convertView.findViewById(id);
			holder.views.put(id, retView);
		}
		return retView;
	}
	public static CommonViewHolder getViewHolder(int position, View convertView, ViewGroup parent, int layoutId,
			LayoutInflater flater) {
		CommonViewHolder holder = null;
		if (convertView == null) {
			convertView = flater.inflate(layoutId, parent, false);
			holder = new CommonViewHolder(new SparseArray<View>(), convertView, parent, position);
			convertView.setTag(holder);
		} else {
			holder = (CommonViewHolder) convertView.getTag();
		}
		return holder;
	}
	public View getConvertView() {
		return convertView;
	}
	public void update() {
		// 精确计算GridView的item高度	
		convertView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			public void onGlobalLayout() {
				convertView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				// 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等	
				if (position > 0 && position % 2 == 1) {
					View v = convertView;
					int height = v.getHeight();
					View view = views.get(position - 1);
					int lastheight = view.getHeight();
					// 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可	
					if (height > lastheight) {
						view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, height));
					} else if (height < lastheight) {
						v.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
								lastheight));
					}
				}
			}
		});
	}
	//设置文字
	public CommonViewHolder setText(int id, CharSequence ch) {
		TextView textView = getView(id);
		textView.setText(ch);
		return this;
	}
	//为ImageView设置图片
	public CommonViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);
		return this;
	}
	//为ImageView设置图片 
	public CommonViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}
	//为ImageView设置图片
	public CommonViewHolder displayImage(int viewId, String url) {
		ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId));
		return this;
	}
	//设置View显隐
	public void setVisibility(int viewId, int visibility) {
		View view = getView(viewId);
		view.setVisibility(visibility);
	}
	public ViewGroup getParent() {
		return parent;
	}
}
