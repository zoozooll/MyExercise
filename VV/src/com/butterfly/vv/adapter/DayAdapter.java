package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.butterfly.vv.view.grid.OptimizeGridView.OptimizeGridAdapter;

/**
 * 天数适配器
 */
public class DayAdapter extends BaseAdapter implements
		OptimizeGridAdapter<DayAdapter.Item> {
	public static class Item {
		public int dayNum;
		public int picNum;
		public int resId;

		@Override
		public String toString() {
			return "Item [dayNum=" + dayNum + ", picNum=" + picNum + ", resId="
					+ resId + "]";
		}
	}

	private List<Item> mItems = new ArrayList<DayAdapter.Item>();
	private Context mContext;

	public DayAdapter(Context context) {
		// super(context);
		mContext = context;
	}
	@Override
	public int getCount() {
		return mItems.size();
	}
	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.timefly_slider_day_item, parent, false);
			vh = new ViewHolder();
			vh.text = (TextView) convertView.findViewById(R.id.text);
			vh.picnum = (TextView) convertView.findViewById(R.id.picnum);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Item item = (Item) getItem(position);
		vh.text.setText(item.dayNum < 10 ? item.dayNum + "  号" : item.dayNum
				+ "号");
		vh.picnum.setText(item.picNum < 10 ? "( " + item.picNum + "  张" + " )"
				: "( " + item.picNum + "张" + " )");
		return convertView;
	}

	class ViewHolder {
		TextView text;
		TextView picnum;
	}

	public static Item NULL_ITEM = new Item();

	@Override
	public List<Item> getItems() {
		return mItems;
	}
	@Override
	public void setItems(List<Item> items) {
		mItems = items;
		notifyDataSetChanged();
	}
	@Override
	public Item getNullItem() {
		return NULL_ITEM;
	}
	@Override
	public boolean isNullItem(Item item) {
		return item == NULL_ITEM;
	}
}
