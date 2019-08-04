package com.beem.project.btf.ui.views;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.wheelviewWidget.OnWheelChangedListener;
import com.beem.project.btf.ui.views.wheelviewWidget.WheelView;
import com.beem.project.btf.ui.views.wheelviewWidget.adapters.NumericWheelAdapter;

/**
 * @author le yang 日期滚轮选择器
 */
public class DateTimePickerView {
	private View mView;
	private Context mContext;
	private WheelView wv_year, wv_month, wv_day;
	private List<String> list_big, list_little;
	private int currentyear = 0, currentmonth = 0, currentday = 0;
	private int startyear = 1945, endyear = 2050, month = 0;
	private int visibleItems = 9;
	private int data[] = { 0, 0, 0 };

	public enum Viewcount {
		one, two, three
	}

	private getDataListener dataListener;

	public void setDataListener(getDataListener dataListener) {
		this.dataListener = dataListener;
	}

	public interface getDataListener {
		public void setdata(int[] data);
	}

	private Viewcount vcount = Viewcount.three;
	private Calendar calendar;

	public DateTimePickerView(Context context, int Syear, int Eyear,
			Viewcount count, int visibleItems) {
		this.mContext = context;
		this.visibleItems = visibleItems;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.setting_birthday, null);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		list_big = Arrays.asList(months_big);
		list_little = Arrays.asList(months_little);
		vcount = count;
		// 年
		wv_year = (WheelView) mView.findViewById(R.id.year);
		// 月
		wv_month = (WheelView) mView.findViewById(R.id.month);
		// 日
		wv_day = (WheelView) mView.findViewById(R.id.day);
		calendar = Calendar.getInstance();
		// 初始化起始年份
		if (Syear == 0) {
			startyear = 1945;
		} else {
			startyear = Syear;
		}
		// 初始化终止年份
		if (Eyear == 0) {
			endyear = 2050;
		} else {
			endyear = Eyear;
		}
		// 防止逻辑错误
		if ((Eyear - Syear) < 0) {
			startyear = 1945;
			endyear = 2050;
		}
		month = calendar.get(Calendar.MONTH);
		// 初始化滚轮个数
		switch (vcount) {
			case one: {
				wv_year.setVisibility(View.VISIBLE);
				wv_month.setVisibility(View.GONE);
				wv_day.setVisibility(View.GONE);
				initYear();
				break;
			}
			case two: {
				wv_year.setVisibility(View.VISIBLE);
				wv_month.setVisibility(View.VISIBLE);
				wv_day.setVisibility(View.GONE);
				initYear();
				initMonth();
				break;
			}
			case three: {
				wv_year.setVisibility(View.VISIBLE);
				wv_month.setVisibility(View.VISIBLE);
				wv_day.setVisibility(View.VISIBLE);
				initYear();
				initMonth();
				initDay();
				break;
			}
		}
	}
	public DateTimePickerView(Context context, int Syear, int Eyear,
			Viewcount count) {
		this(context, Syear, Eyear, count, 9);
	}
	public DateTimePickerView(Context context) {
		this(context, 1945, 2050, Viewcount.three, 9);
	}
	// 根据当前年月设置日期的方法
	private void setDay() {
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(currentmonth))) {
			wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 31));
		} else if (list_little.contains(String.valueOf(currentmonth))) {
			wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 30));
		} else {
			// 闰年
			if ((currentyear % 4 == 0 && currentyear % 100 != 0)
					|| currentyear % 400 == 0)
				wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 29));
			else
				wv_day.setViewAdapter(new NumericWheelAdapter(mContext, 1, 28));
		}
		currentday = wv_day.getCurrentItem() + 1;
		Log.i("DateTimePickerView", "~~currentday~~" + currentday);
	}
	// 初始化年份
	private void initYear() {
		wv_year.setViewAdapter(new NumericWheelAdapter(mContext, startyear,
				endyear));
		wv_year.setCyclic(false);
		wv_year.setCurrentItem(calendar.get(Calendar.YEAR) - startyear);// 默认显示当前年
		wv_year.setVisibleItems(visibleItems);
		wv_year.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				currentyear = startyear + newValue;
				if (vcount == Viewcount.three) {
					setDay();
					if (dataListener != null) {
						dataListener.setdata(getcurrentData());
					}
				}
			}
		});
	}
	// 初始化月份
	private void initMonth() {
		wv_month.setViewAdapter(new NumericWheelAdapter(mContext, 1, 12));
		wv_month.setCyclic(true);
		wv_month.setVisibleItems(visibleItems);
		wv_month.setCurrentItem(month);
		wv_month.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				currentmonth = newValue + 1;
				if (vcount == Viewcount.three) {
					setDay();
					if (dataListener != null) {
						dataListener.setdata(getcurrentData());
					}
				}
			}
		});
	}
	// 初始化日期
	private void initDay() {
		wv_day.setCyclic(true);
		wv_day.setVisibleItems(visibleItems);
		currentyear = wv_year.getCurrentItem() + startyear;
		currentmonth = wv_month.getCurrentItem() + 1;
		setDay();
		wv_day.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				currentday = wv_day.getCurrentItem() + 1;
				if (dataListener != null) {
					dataListener.setdata(getcurrentData());
				}
			}
		});
	}
	// 返回布局对象
	public View getmView() {
		return mView;
	}
	// 从外部设置默认值
	public void setcurrentData(String[] str) {
		boolean isTakeDefaultVal = false;
		if (str == null) {
			isTakeDefaultVal = true;
		} else {
			for (String strOne : str) {
				if (TextUtils.isEmpty(strOne)) {
					isTakeDefaultVal = true;
				}
			}
		}
		if (isTakeDefaultVal) {
			str = new String[] {
					String.valueOf(Calendar.getInstance().get(Calendar.YEAR)),
					String.valueOf(Calendar.getInstance().get(Calendar.MONTH)),
					String.valueOf(Calendar.getInstance().get(
							Calendar.DAY_OF_MONTH)), };
		}
		switch (vcount) {
			case one: {
				wv_year.setCurrentItem(Integer.parseInt(str[0]) - startyear);
				break;
			}
			case two: {
				wv_year.setCurrentItem(Integer.parseInt(str[0]) - startyear);
				wv_month.setCurrentItem(Integer.parseInt(str[1]) - 1);
				break;
			}
			case three: {
				wv_year.setCurrentItem(Integer.parseInt(str[0]) - startyear);
				wv_month.setCurrentItem(Integer.parseInt(str[1]) - 1);
				wv_day.setCurrentItem(Integer.parseInt(str[2]) - 1);
				break;
			}
		}
	}
	// 从外部设置默认值
	public void setcurrentData(int[] ymd) {
		switch (vcount) {
			case one: {
				wv_year.setCurrentItem(ymd[0] - startyear);
				break;
			}
			case two: {
				wv_year.setCurrentItem(ymd[0] - startyear);
				wv_month.setCurrentItem(ymd[1] - 1);
				break;
			}
			case three: {
				wv_year.setCurrentItem(ymd[0] - startyear);
				wv_month.setCurrentItem(ymd[1] - 1);
				wv_day.setCurrentItem(ymd[2] - 1);
				break;
			}
		}
	}
	// 从外部设置滚轮渐变色
	public void setShadowColor(int start, int middle, int end) {
		switch (vcount) {
			case one: {
				wv_year.setShadowColor(start, middle, end);
				break;
			}
			case two: {
				wv_year.setShadowColor(start, middle, end);
				wv_month.setShadowColor(start, middle, end);
				break;
			}
			case three: {
				wv_year.setShadowColor(start, middle, end);
				wv_month.setShadowColor(start, middle, end);
				wv_day.setShadowColor(start, middle, end);
			}
		}
	}
	// 返回选中的值
	public int[] getcurrentData() {
		data[0] = currentyear;
		data[1] = currentmonth;
		data[2] = currentday;
		return data;
	}
}
