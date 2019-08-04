package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.beem.project.btf.R;
import com.butterfly.vv.adapter.DayAdapter.Item;
import com.butterfly.vv.adapter.YearAdapter.YearMapItemListener;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.view.grid.OptimizeGridView;

public class MonthAdapter extends BaseAdapter {
	private static final String TAG = "MonthAdapter";
	private Map<String, Object> monHashMap;
	private Context mContext;
	private SlideHolder slideHolder;
	private YearMapItemListener yearitemListener;
	private Map<String, ImageFolder> folderTotalMap;
	private String curYear;
	private final int[] MonthId = new int[] { R.drawable.timefly_month_1,
			R.drawable.timefly_month_2, R.drawable.timefly_month_3,
			R.drawable.timefly_month_4, R.drawable.timefly_month_5,
			R.drawable.timefly_month_6, R.drawable.timefly_month_7,
			R.drawable.timefly_month_8, R.drawable.timefly_month_9,
			R.drawable.timefly_month_10, R.drawable.timefly_month_11,
			R.drawable.timefly_month_12 };
	private String[] monthCh = new String[] { "一月", "二月", "三月", "四月", "五月",
			"六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };

	public MonthAdapter(Context mContext, Map<String, Object> monHashMap) {
		this(mContext, monHashMap, null, null);
	}
	public MonthAdapter(Context mContext, Map<String, Object> monHashMap,
			SlideHolder slideHolder, YearMapItemListener yearmaplistener) {
		this.mContext = mContext;
		this.monHashMap = monHashMap;
		this.slideHolder = slideHolder;
		this.yearitemListener = yearmaplistener;
	}
	@Override
	public int getCount() {
		return monHashMap.size();
	}
	@Override
	public Object getItem(int position) {
		return monHashMap.get(String.valueOf(position));
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			LayoutInflater inflate = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflate.inflate(R.layout.grid_expandable_list_item,
					null);
			vh = new ViewHolder();
			vh.connectline_top = convertView.findViewById(R.id.connectline_top);
			vh.text = (TextView) convertView.findViewById(R.id.text);
			vh.image = (ImageView) convertView.findViewById(R.id.image);
			vh.expandable_toggle_button = convertView
					.findViewById(R.id.expandable_toggle_button);
			vh.expandable = convertView.findViewById(R.id.expandable);
			vh.myGrid_info = (OptimizeGridView) convertView
					.findViewById(R.id.myGrid_info);
			convertView.setTag(R.layout.grid_expandable_list_item, vh);
			convertView.setTag(position);
			vh.expandable_toggle_button.setTag(position);
			vh.expandable.setTag(position);
		} else {
			vh = (ViewHolder) convertView
					.getTag(R.layout.grid_expandable_list_item);
		}
		setUpView(convertView, position, vh);
		return convertView;
	}
	private void setUpView(View convertView, int position, final ViewHolder vh) {
		vh.connectline_top.setVisibility(position == 0 ? View.VISIBLE
				: View.GONE);
		final String monthStr = (String) monHashMap.keySet().toArray()[position];
		int monthInt = Integer.parseInt(monthStr);
		setMonth(monthInt, vh.text, vh.image);
		// 隐藏最后一根竖线
		if (position == getCount() - 1) {
			convertView.findViewById(R.id.connectline).setVisibility(View.GONE);
		} else {
			convertView.findViewById(R.id.connectline).setVisibility(
					View.VISIBLE);
		}
		if (vh.myGrid_info != null) {
			vh.myGrid_info.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					String day = ((TextView) arg1.findViewById(R.id.text))
							.getText().toString();
					String month = monthStr;
					String year = curYear;
					String clickTime = "";
					String timeLike = year + "-" + month + "-" + day;
					for (String key : folderTotalMap.keySet()) {
						if (key.startsWith(timeLike)) {
							clickTime = timeLike;
							break;
						}
					}
					if (slideHolder != null) {
						slideHolder.toggle();
						Log.i(TAG, "~~当前年份~~" + curYear);
					}
					// 此接口用于更新选中项的详情
					if (yearitemListener != null) {
						yearitemListener.UpdateImage(clickTime, monHashMap);
					}
				}
			});
			String month = (String) monHashMap.keySet().toArray()[position];
			@SuppressWarnings("unchecked")
			Map<String, Object> days = (Map<String, Object>) monHashMap
					.get(month);
			// 天数适配器
			DayAdapter itemggAdapter = new DayAdapter(mContext);
			List<Item> mItems = new ArrayList<DayAdapter.Item>();
			for (Iterator<String> iterator = days.keySet().iterator(); iterator
					.hasNext();) {
				String dayNum = iterator.next();
				String picNumStr = (String) days.get(dayNum);
				DayAdapter.Item item = new DayAdapter.Item();
				item.dayNum = Integer.parseInt(dayNum);
				item.picNum = Integer.parseInt(picNumStr);
				mItems.add(item);
			}
			itemggAdapter.setItems(mItems);
			vh.myGrid_info.setAdapter(itemggAdapter);
		}
	}
	private void setMonth(int monthInt, TextView monthTxt, ImageView monthImgV) {
		monthImgV.setBackgroundResource(MonthId[Math.abs(monthInt - 1) % 12]);
		monthTxt.setText(monthCh[monthInt - 1]);
	}

	private class ViewHolder {
		View connectline_top;
		View expandable_toggle_button;
		View expandable;
		OptimizeGridView myGrid_info;
		TextView text;
		ImageView image;
	}

	public void setMonHashMap(Map<String, Object> monHashMap) {
		this.monHashMap = monHashMap;
	}
	public void setMonHashMap(Map<String, Object> monHashMap,
			Map<String, ImageFolder> folderTotalMap, String curyaer) {
		this.monHashMap = monHashMap;
		this.folderTotalMap = folderTotalMap;
		this.curYear = curyaer;
	}
	public void setMonHashMap(Map<String, ImageFolder> folderTotalMap,
			String curyaer) {
		this.folderTotalMap = folderTotalMap;
		this.curYear = curyaer;
	}
}
