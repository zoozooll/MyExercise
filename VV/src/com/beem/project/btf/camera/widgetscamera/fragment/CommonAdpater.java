package com.beem.project.btf.camera.widgetscamera.fragment;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @ClassName: CommonAdpater
 * @Description: 万能Adapter
 * @author: yuedong bao
 * @date: 2015-11-10 下午2:50:05
 * @param <T>
 */
public abstract class CommonAdpater<T> extends BaseAdapter {
	protected LayoutInflater inflater;
	protected final int layoutId;
	protected final List<T> datas;

	public CommonAdpater(Context context, List<T> datas, int layout_id) {
		super();
		this.datas = datas;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layoutId = layout_id;
	}
	@Override
	public int getCount() {
		return datas.size();
	}
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommonViewHolder holder = CommonViewHolder.getViewHolder(position, convertView, parent, layoutId, inflater);
		buildViewHolder(holder, datas.get(position));
		return holder.getConvertView();
	}
	//构建ViewHolder里面的值
	public abstract void buildViewHolder(CommonViewHolder holder, T item);
}
