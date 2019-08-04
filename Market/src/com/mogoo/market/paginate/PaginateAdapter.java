package com.mogoo.market.paginate;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 列表适配器
 * 
 * @author xjx-motone
 * 
 * @param <T>
 */
public abstract class PaginateAdapter<T> extends BaseAdapter {

	/**
	 * 列表数据集
	 */
	private ArrayList<T> mList = new ArrayList<T>();

	public ArrayList<T> getList() {
		return mList;
	}

	public void setList(ArrayList<T> list) {
		this.mList = list;
	}

	public void add(ArrayList<T> list2) {
		mList.addAll(list2);
	}

	public int getCount() {
		return (mList != null) ? mList.size() : 0;
	}

	public Object getItem(int position) {
		return (mList != null) ? mList.get(position) : null;
	}

	public long getItemId(int position) {
		return position;
	}

	public void clear() {
		this.mList.clear();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Object item = mList.get(position);
		return getItemView(item, position, convertView, parent);
	}

	/**
	 * 初始化列表中的每个条目
	 * 
	 * @param item
	 *            --每个条目的实体对象
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getItemView(Object item, int position,
			View convertView, ViewGroup parent);

}
