/**
 * 
 */
package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;

/**
 * @author hongbo ke
 */
public class TimeflySliderbarAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private LayoutInflater mFlater;
	private static final int[] MonthId = new int[] {
			R.drawable.timefly_month_1, R.drawable.timefly_month_2,
			R.drawable.timefly_month_3, R.drawable.timefly_month_4,
			R.drawable.timefly_month_5, R.drawable.timefly_month_6,
			R.drawable.timefly_month_7, R.drawable.timefly_month_8,
			R.drawable.timefly_month_9, R.drawable.timefly_month_10,
			R.drawable.timefly_month_11, R.drawable.timefly_month_12 };
	private static String[] monthCh = new String[] { "一月", "二月", "三月", "四月",
			"五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
	private Map<String, Map<String, Map<String, String>>> yearMap;
	private Map<String, ImageFolder> folderTotalMap = new TreeMap<String, ImageFolder>(
			new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
	private YearMapItemListener listener;

	public TimeflySliderbarAdapter(Context context) {
		super();
		mContext = context;
		mFlater = LayoutInflater.from(context);
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	@Override
	public int getGroupCount() {
		if (yearMap != null) {
			return yearMap.size();
		}
		return 0;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	@Override
	public int getChildrenCount(int paramInt) {
		Map<String, Map<String, String>> childItem = getGroup(paramInt);
		if (childItem != null) {
			return childItem.size();
		}
		return 0;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	@Override
	public Map<String, Map<String, String>> getGroup(int paramInt) {
		if (yearMap != null) {
			String key = (String) yearMap.keySet().toArray()[paramInt];
			return yearMap.get(key);
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	@Override
	public Map<String, String> getChild(int paramInt1, int paramInt2) {
		Map<String, Map<String, String>> monthMap = getGroup(paramInt1);
		if (monthMap != null) {
			String monthKey = (String) (monthMap.keySet().toArray()[paramInt2]);
			return monthMap.get(monthKey);
		}
		return null;
	}
	public String getDay(int yearIndex, int monthIndex, int dayIndex) {
		Map<String, String> days = getChild(yearIndex, monthIndex);
		if (days != null) {
			return days.get(days.keySet().toArray()[dayIndex]);
		}
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	@Override
	public long getGroupId(int paramInt) {
		String year = (String) yearMap.keySet().toArray()[paramInt];
		if (year != null) {
			return Long.valueOf(year);
		}
		return 0;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	@Override
	public long getChildId(int paramInt1, int paramInt2) {
		String year = (String) yearMap.keySet().toArray()[paramInt1];
		if (year != null) {
			String month = (String) (yearMap.get(year).keySet().toArray()[paramInt2]);
			if (month != null) {
				return Long.valueOf(month);
			}
		}
		return 0;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getGroupView(int groudPosition, boolean paramBoolean,
			View convertView, ViewGroup paramViewGroup) {
		GroupViewholder holder;
		if (convertView == null) {
			convertView = mFlater
					.inflate(R.layout.listitem_timefly_group, null);
			holder = new GroupViewholder();
			holder.year_icon = (ImageView) convertView
					.findViewById(R.id.year_icon);
			holder.year_text = (TextView) convertView
					.findViewById(R.id.year_text);
			holder.up_toggle = (ImageView) convertView
					.findViewById(R.id.up_toggle);
			holder.connectline = convertView.findViewById(R.id.connectline);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewholder) convertView.getTag();
		}
		int year = (int) getGroupId(groudPosition);
		if (year > 0) {
			showYearView(groudPosition, year, holder, paramBoolean);
		}
		return convertView;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean lastChild, View convertView, ViewGroup paramViewGroup) {
		ChildViewholder holder;
		if (convertView == null) {
			convertView = mFlater
					.inflate(R.layout.listitem_timefly_child, null);
			holder = new ChildViewholder();
			holder.expandable_toggle_button = convertView
					.findViewById(R.id.expandable_toggle_button);
			holder.connectline_top = convertView
					.findViewById(R.id.connectline_top);
			holder.imv_MonthIcon = (ImageView) convertView
					.findViewById(R.id.imv_MonthIcon);
			holder.tvw_MonthTitle = (TextView) convertView
					.findViewById(R.id.tvw_MonthTitle);
			holder.up_toggle = (ImageView) convertView
					.findViewById(R.id.up_toggle);
			holder.connectline = convertView.findViewById(R.id.connectline);
			holder.expandable = (LinearLayout) convertView
					.findViewById(R.id.expandable);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewholder) convertView.getTag();
		}
		int month = (int) getChildId(groupPosition, childPosition);
		Map<String, String> daysData = getChild(groupPosition, childPosition);
		showMonthView(groupPosition, childPosition, month, daysData, holder,
				lastChild);
		return convertView;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	@Override
	public boolean isChildSelectable(int groudPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	private void showYearView(int groupPosition, int year,
			GroupViewholder holder, boolean bool) {
		holder.year_text.setText(mContext.getResources().getString(
				R.string.timefly_yearSting, year));
		if (bool) {
			holder.up_toggle.setImageResource(R.drawable.month_down);
		} else {
			holder.up_toggle.setImageResource(R.drawable.month_up);
		}
		if (groupPosition < getGroupCount() - 1 && !bool) {
			holder.connectline.setVisibility(View.VISIBLE);
		} else {
			holder.connectline.setVisibility(View.GONE);
		}
	}
	private void showMonthView(final int groupPosition,
			final int childPosition, final int month,
			Map<String, String> daysData, final ChildViewholder holder,
			final boolean lastChild) {
		holder.imv_MonthIcon
				.setImageResource(MonthId[Math.abs(month - 1) % 12]);
		holder.tvw_MonthTitle.setText(monthCh[month - 1]);
		holder.expandable_toggle_button
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						holder.isExpanded = !holder.isExpanded;
						expandDays(holder.isExpanded, holder, lastChild);
					}
				});
		holder.expandable.removeAllViews();
		for (Map.Entry<String, String> entry : daysData.entrySet()) {
			final String day = entry.getKey();
			View dayView = showDayView(day, entry.getValue());
			holder.expandable.addView(dayView,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			OnClickListener l = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listener != null) {
						int year = (int) getGroupId(groupPosition);
						String dateTime = String.format("%d-%02d-%s", year,
								month, day);
						listener.updateImage(dateTime);
					}
				}
			};
			dayView.setOnClickListener(l);
		}
		if (childPosition == 0) {
			holder.connectline_top.setVisibility(View.VISIBLE);
		} else {
			holder.connectline_top.setVisibility(View.GONE);
		}
		expandDays(holder.isExpanded, holder, lastChild);
	}
	private void expandDays(boolean show, ChildViewholder holder,
			boolean lastChild) {
		if (show) {
			holder.up_toggle.setImageResource(R.drawable.month_down);
			holder.expandable.setVisibility(View.VISIBLE);
		} else {
			holder.up_toggle.setImageResource(R.drawable.month_up);
			holder.expandable.setVisibility(View.GONE);
		}
		if (!show && !lastChild) {
			holder.connectline.setVisibility(View.VISIBLE);
		} else {
			holder.connectline.setVisibility(View.GONE);
		}
	}
	private View showDayView(String day, String photoCount) {
		View v = mFlater.inflate(R.layout.timefly_slider_day_item, null);
		TextView text = (TextView) v.findViewById(R.id.text);
		TextView picnum = (TextView) v.findViewById(R.id.picnum);
		text.setText(mContext.getResources().getString(
				R.string.timefly_daySting, day));
		picnum.setText(mContext.getResources().getString(
				R.string.timefly_picCountSting, photoCount));
		return v;
	}
	public void addDatas(Map<String, ImageFolder> yearfolderMap) {
		for (String key : yearfolderMap.keySet()) {
			changeData(key, yearfolderMap.get(key));
		}
	}
	
	public void clearDatas() {
		folderTotalMap.clear();
	}
	public void changeData(String createTime, ImageFolder folder) {
		if (createTime.length() >= 10) {
			String[] YMD = createTime.substring(0, 10).split("-");
			String year = YMD[0];
			String month = YMD[1];
			String day = YMD[2];
			setYM(year, month, day, String.valueOf(folder.getPhotoCount()));
		}
		folderTotalMap.put(createTime, folder);
		for (Iterator<ImageFolder> it = folderTotalMap.values().iterator(); it
				.hasNext();) {
			ImageFolder folderOne = it.next();
			if (folderOne.getPhotoCount() == 0) {
				it.remove();
			}
		}
	}
	public void setYM(final String year, final String month, final String day,
			final String picNum) {
		if (yearMap == null) {
			yearMap = new TreeMap<String, Map<String, Map<String, String>>>(
					new Comparator<String>() {
						@Override
						public int compare(String lhs, String rhs) {
							// 降序
							return rhs.compareTo(lhs);
						}
					});
		}
		Map<String, Map<String, String>> monthMap = yearMap.get(year);
		if (monthMap == null) {
			monthMap = new TreeMap<String, Map<String, String>>(
					new Comparator<String>() {
						@Override
						public int compare(String lhs, String rhs) {
							// 降序
							return rhs.compareTo(lhs);
						}
					});
			yearMap.put(year, monthMap);
		}
		Map<String, String> dayMap = monthMap.get(month);
		if (dayMap == null) {
			dayMap = new TreeMap<String, String>(new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					// 降序
					return rhs.compareTo(lhs);
				}
			});
			monthMap.put(month, dayMap);
		}
		if (Integer.parseInt(picNum) == 0) {
			// 移除图片数为0的日
			dayMap.remove(day);
		} else {
			dayMap.put(day, picNum);
		}
		// 如果daymap为空,删除monthMap中这个月的
		if (dayMap.isEmpty()) {
			monthMap.remove(month);
		}
		// 如果monthMap为空,删除yearMap中这一年的
		if (monthMap.isEmpty()) {
			yearMap.remove(year);
		}
	}
	public void setYearmaplistener(YearMapItemListener listener) {
		this.listener = listener;
	}

	private static class GroupViewholder {
		ImageView year_icon;
		TextView year_text;
		ImageView up_toggle;
		View connectline;
	}

	private static class ChildViewholder {
		View expandable_toggle_button;
		View connectline_top;
		ImageView imv_MonthIcon;
		TextView tvw_MonthTitle;
		ImageView up_toggle;
		View connectline;
		LinearLayout expandable;
		boolean isExpanded;
	}

	public List<ImageFolder> getImageFolders(String start, int num) {
		if (start == null)
			start = "3000";
		List<ImageFolder> retVals = new ArrayList<ImageFolder>();
		for (String key : folderTotalMap.keySet()) {
			if (retVals.size() >= num)
				break;
			if (num == 1) {
				if (key.compareTo(start) == 0) {
					retVals.add(folderTotalMap.get(key));
				}
			} else {
				if (key.compareTo(start) < 0) {
					retVals.add(folderTotalMap.get(key));
				}
			}
		}
		return retVals;
	}
	public List<ImageFolder> getImageFolders(String start, String endTime) {
		List<ImageFolder> retVals = new ArrayList<ImageFolder>();
		if (endTime.compareTo(start) < 0) {
			return retVals;
		}
		for (String key : folderTotalMap.keySet()) {
			if (key.compareTo(start) > 0 && key.compareTo(endTime) <= 0) {
				retVals.add(folderTotalMap.get(key));
			}
		}
		return retVals;
	}

	public interface YearMapItemListener {
		/**
		 * Call back when selected a date;
		 * @param dateTime The selected date string, format like "yyyy-MM-dd"
		 */
		public void updateImage(String dateTime);
		/**
		 * Call back when folderTotalMap changed
		 * @param the folderTotalMap
		 */
		public void onDataChange(Map<String, ImageFolder> folderTotalMap);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (listener != null) {
			listener.onDataChange(Collections.unmodifiableMap(folderTotalMap));
		}
	}
}
