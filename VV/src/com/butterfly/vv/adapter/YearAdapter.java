package com.butterfly.vv.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.beem.project.btf.R;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.tjerkw.slideexpandable.library.SlideExpandableListView;

public class YearAdapter extends BaseAdapter {
	private Context mContext;
	private SlideHolder slideHolder;
	private Map<String, Object> yearhashMap;
	private String curYear;
	private Map<String, Object> monHashMap;
	private YearMapItemListener yearmaplistener;
	private Map<String, ImageFolder> folderTotalMap = new TreeMap<String, ImageFolder>(
			new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o2.compareTo(o1);
				}
			});
	private updataImageCountListener countListener;

	public void setCountListener(updataImageCountListener countListener) {
		this.countListener = countListener;
	}
	public YearAdapter(Context mContext) {
		this.mContext = mContext;
		this.yearhashMap = new TreeMap<String, Object>(
				new Comparator<String>() {
					@Override
					public int compare(String lhs, String rhs) {
						return lhs.compareTo(rhs);
					}
				});
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return yearhashMap.size();
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
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.year_list_layout, parent,
					false);
			vh = new ViewHolder();
			vh.year_icon = (ImageView) convertView.findViewById(R.id.year_icon);
			vh.year_text = (TextView) convertView.findViewById(R.id.year_text);
			vh.monthlistview = (SlideExpandableListView) convertView
					.findViewById(R.id.monthlistview);
			vh.connectline_top = convertView.findViewById(R.id.connectline_top);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		setUpView(convertView, position, vh);
		return convertView;
	}
	@SuppressWarnings("unchecked")
	private void setUpView(View convertView, int position, final ViewHolder vh) {
		vh.connectline_top.setVisibility(position == 0 ? View.GONE
				: View.VISIBLE);
		final String yearStr = (String) yearhashMap.keySet().toArray()[position];
		setYear(yearStr, vh.year_text);
		// 隐藏最后一根竖线
		if (position == getCount() - 1) {
			convertView.findViewById(R.id.connectline).setVisibility(View.GONE);
		} else {
			convertView.findViewById(R.id.connectline).setVisibility(
					View.VISIBLE);
		}
		monHashMap = (Map<String, Object>) yearhashMap.get(String
				.valueOf(curYear));
		MonthAdapter monthAdapter = new MonthAdapter(mContext, monHashMap,
				slideHolder, yearmaplistener);
		monthAdapter.setMonHashMap(folderTotalMap, curYear);
		vh.monthlistview.setAdapter(monthAdapter);
	}

	class ViewHolder {
		ImageView year_icon;
		TextView year_text;
		SlideExpandableListView monthlistview;
		View connectline_top;
	}

	private void setYear(String yearInt, TextView yTxt) {
		yTxt.setText(yearInt + "年");
	}
	public void setSlideHolder(SlideHolder slideHolder) {
		this.slideHolder = slideHolder;
	}

	// 此接口用于更新选中项的详情
	public interface YearMapItemListener {
		public void UpdateImage(String time, Map<String, Object> monHashMap);
	}

	public void setYearmaplistener(YearMapItemListener yearmaplistener) {
		this.yearmaplistener = yearmaplistener;
	}
	public void addDatas(Map<String, ImageFolder> yearfolderMap) {
		for (String key : yearfolderMap.keySet()) {
			changeData(key, yearfolderMap.get(key), false);
		}
		updataImageCount();
	}
	private void updataImageCount() {
		int ImgCount = 0, DayCount = 0;
		// 遍历folderTotalMap获取总天数和总图片数
		DayCount = folderTotalMap.size();
		for (Iterator<ImageFolder> it = folderTotalMap.values().iterator(); it
				.hasNext();) {
			ImageFolder folderOne = it.next();
			ImgCount = ImgCount + folderOne.getPhotoCount();
		}
		if (countListener != null) {
			countListener.updataCount(ImgCount, DayCount);
		}
	}
	public void changeData(String createTime, ImageFolder folder,
			boolean isChangeCount) {
		if (createTime.length() >= 10) {
			String[] YMD = createTime.substring(0, 10).split("-");
			String year = YMD[0];
			String month = YMD[1];
			String day = YMD[2];
			setYM(year, month, day, String.valueOf(folder.getPhotoCount()),
					yearhashMap);
			if (yearhashMap.size() > 0) {
				curYear = (String) yearhashMap.keySet().toArray()[0];
			}
			folderTotalMap.put(createTime, folder);
			for (Iterator<ImageFolder> it = folderTotalMap.values().iterator(); it
					.hasNext();) {
				ImageFolder folderOne = it.next();
				if (folderOne.getPhotoCount() == 0) {
					it.remove();
				}
			}
			if (isChangeCount) {
				updataImageCount();
			}
		}
	}
	// 配置年月日map
	@SuppressWarnings("unchecked")
	public static void setYM(String year, String month, String day,
			String picNum, Map<String, Object> yearMap) {
		Map<String, Object> monthMap = (Map<String, Object>) yearMap.get(year);
		if (monthMap == null) {
			monthMap = new TreeMap<String, Object>(new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					// 降序
					return rhs.compareTo(lhs);
				}
			});
			yearMap.put(year, monthMap);
		}
		Map<String, Object> dayMap = (Map<String, Object>) monthMap.get(month);
		if (dayMap == null) {
			dayMap = new TreeMap<String, Object>(new Comparator<String>() {
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
	public List<ImageFolder> getImageFolders(String start, int num) {
		if (start == null)
			start = "30000";
		List<ImageFolder> retVals = new ArrayList<ImageFolder>();
		for (String key : folderTotalMap.keySet()) {
			if (retVals.size() >= num)
				break;
			if (key.compareTo(start) < 0) {
				retVals.add(folderTotalMap.get(key));
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

	public interface updataImageCountListener {
		public void updataCount(int imagecount, int daycount);
	}
}
