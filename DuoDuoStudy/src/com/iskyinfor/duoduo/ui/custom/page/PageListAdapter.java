package com.iskyinfor.duoduo.ui.custom.page;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class PageListAdapter<T> extends BaseAdapter {
	protected Context context;

	protected ArrayList<T> arrayList;

	public PageListAdapter(Context context, ArrayList<T> arrayList) {
		this.context = context;
		this.arrayList = arrayList;
	}

	@Override
	public int getCount() {
		if (arrayList == null){
			return 0;
		}
		return this.arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		if (arrayList == null){
			return null;
		}
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object object=arrayList.get(position);
		return initItemView(convertView, object, position );
	}

	public abstract View initItemView(View v,Object object,int position );


	public ArrayList<T> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<T> arrayList) {
		this.arrayList.addAll(arrayList);
	}

}
