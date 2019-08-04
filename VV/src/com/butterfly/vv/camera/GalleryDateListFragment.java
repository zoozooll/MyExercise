/**
 * 
 */
package com.butterfly.vv.camera;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.butterfly.vv.camera.base.DateImageHolder;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotoChoiceChangeListener;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.camera.date.DateGridCellGetData;
import com.butterfly.vv.camera.date.HalfRoundProgressBar;
import com.butterfly.vv.camera.date.HalfRoundProgressBar.OnSeekChangeListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示时间数序的列表的Fragment
 * @author Aaron Lee Created at 下午1:48:11 2015-12-28
 */
public class GalleryDateListFragment extends GalleryBaseFragment implements
		PhotoChoiceChangeListener {
	//先是有关的VIEW
	private View view;
	private ListView dateListView;
	private Button mPopSeekButton;
	private GalleryDateListAdapter dateListAdapter;
	private com.butterfly.vv.camera.GalleryDateListFragment.DateRoundHolder mDrh;
	private View mPopWindowView;
	private View layout_loading;
	private HalfRoundProgressBar mRoundSeekBar;
	// 图片数据
	private List<DateImageHolder> mDateImageList;
	private int mCurrRoundProgress;
	private int mCurrPopviewPos;
	private PopupWindow mPopupWindow;
	private DateGridCellGetData calendarDataGetter;
	// 数组用于放置0-9数字
	private static final int[] DAY_PHOTO_RESIDS = new int[] { R.drawable.pop_0,
			R.drawable.pop_1, R.drawable.pop_2, R.drawable.pop_3,
			R.drawable.pop_4, R.drawable.pop_5, R.drawable.pop_6,
			R.drawable.pop_7, R.drawable.pop_8, R.drawable.pop_9 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		calendarDataGetter = DateGridCellGetData.getInstance(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
		} else {
			view = inflater.inflate(R.layout.xc_lay2, container, false);
			initializeViews();
		}
		loadImageData();
		return view;
	}
	private void initializeViews() {
		layout_loading = view.findViewById(R.id.layout_loading);
		dateListView = (ListView) view.findViewById(R.id.autolist1);
		dateListView.setDivider(null);
		dateListView.setDividerHeight(0);
		dateListView.setEmptyView(view.findViewById(android.R.id.empty));
		dateListAdapter = new GalleryDateListAdapter(getActivity());
		dateListView.setAdapter(dateListAdapter);
		// 初始化遮罩按钮
		mPopSeekButton = (Button) view.findViewById(R.id.popup_seek_button);
		// 日期刷新加载
		mPopSeekButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopWindow(getActivity(), view);
			}
		});
		initPopupWindow();
	}
	// 日期刷新加载
	private void initPopupWindow() {
		mDrh = new DateRoundHolder();
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopWindowView = inflater.inflate(R.layout.xc_popwindow, null);
		mRoundSeekBar = (HalfRoundProgressBar) mPopWindowView
				.findViewById(R.id.half_round_seek_bar);
		RoundSeekBarChangeListener mRoundSeekBarListener = new RoundSeekBarChangeListener();
		mRoundSeekBar.setSeekBarChangeListener(mRoundSeekBarListener);
		mDrh.ten = (ImageView) mPopWindowView.findViewById(R.id.ten); // 初始化十位
		mDrh.single = (ImageView) mPopWindowView.findViewById(R.id.single); // 初始化个位
		mDrh.monyearText = (TextView) mPopWindowView
				.findViewById(R.id.showmonthyear);
		mDrh.timeDuration = (TextView) mPopWindowView
				.findViewById(R.id.date_duration);
		mDrh.photoCount = (TextView) mPopWindowView
				.findViewById(R.id.image_count_tv);
		mDrh.hourImage = (ImageView) mPopWindowView
				.findViewById(R.id.left_date_hour);
		mDrh.minImage = (ImageView) mPopWindowView
				.findViewById(R.id.left_date_min);
		mPopWindowView.setFocusable(true);
		mPopWindowView.setFocusableInTouchMode(true);
		mPopWindowView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismisssPopupWindow();
					return true;
				}
				return false;
			}
		});
		mPopWindowView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isTouchNotInPopupArea(event)) {
						dismisssPopupWindow();
						return true;
					}
				}
				return false;
			}
		});
	}
	/** 展示弹出窗口 */
	private void showPopWindow(Context context, View parent) {
		/*
		 * mDateGvIndicator.setVisibility(View.GONE); mDateGv.setVisibility(View.GONE);
		 */
		if (mDateImageList == null && mDateImageList.isEmpty()) {
			return;
		}
		int currPos = 0;
		int firstPos = dateListView.getFirstVisiblePosition();
		int lastPos = dateListView.getLastVisiblePosition();
		int dateCount = mDateImageList.size();
		if (mCurrPopviewPos >= firstPos && mCurrPopviewPos <= lastPos) {
			currPos = mCurrPopviewPos;
		} else {
			currPos = firstPos;
		}
		if (currPos >= mDateImageList.size()) {
			currPos = mDateImageList.size() - 1;
		}
		int seekProgress = 0;
		if (mRoundSeekBar != null) {
			int maxProgress = Integer.MAX_VALUE;
			if (dateCount > 100) {
				maxProgress = 1000;
			} else {
				maxProgress = 100;
			}
			mRoundSeekBar.setMaxProgress(maxProgress);
			seekProgress = (currPos * maxProgress) / dateCount;
			mRoundSeekBar.setProgress(seekProgress);
		}
		mCurrPopviewPos = currPos;
		DateImageHolder currDateHolder = mDateImageList.get(currPos);
		setDatePopupView(currDateHolder);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int paddingTopBtm = (int) getResources().getDimension(
				R.dimen.round_seekbar_padding_top_bottom);
		int popWidth = dm.widthPixels;
		int popHeight = popWidth / 2 + paddingTopBtm;
		mPopupWindow = new PopupWindow(mPopWindowView, popWidth, popHeight,
				true);
		mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		mPopupWindow.setFocusable(false);
		mPopupWindow.setOutsideTouchable(true);
	}
	private void updatePopupView(int newPos) {
		if (mDateImageList != null && mCurrPopviewPos != newPos) {
			mCurrPopviewPos = newPos;
			DateImageHolder currDateHolder = mDateImageList.get(newPos);
			setDatePopupView(currDateHolder);
		}
	}
	private void updatePopupClockView(int newProgress) {
		if (mCurrRoundProgress == newProgress) {
			return;
		}
		mCurrRoundProgress = newProgress;
		float minDegree = -(mCurrRoundProgress % 4) * 90; // 负数表示逆时针旋转，时光倒流
		float hourDegree = -(mCurrRoundProgress % 6) * 60;
		int hourWidth = mDrh.hourImage.getWidth();
		int hourHeight = mDrh.hourImage.getHeight();
		Matrix hMatrix = new Matrix();
		hMatrix.setRotate(hourDegree, hourWidth / 2, hourHeight / 2);
		mDrh.hourImage.setImageMatrix(hMatrix);
		int minWidth = mDrh.minImage.getWidth();
		int minHeight = mDrh.minImage.getHeight();
		Matrix mMatrix = new Matrix();
		mMatrix.setRotate(minDegree, minWidth / 2, minHeight / 2);
		mDrh.minImage.setImageMatrix(mMatrix);
		// mDrh.hourImage.setRotation(hourDegree); //2.3上没有这两个方法
		// mDrh.minImage.setRotation(minDegree);
	}
	private void setDatePopupView(DateImageHolder holder) {
		if (holder.mDateString.length() < ImageInfoHolder.BASE_DATE_LENGTH) {
			mDrh.year = -1;
			mDrh.month = -1;
			mDrh.date = -1;
			changIntToImage(mDrh.date); // 更改天数为ImageView
			mDrh.monyearText.setText("00"
					+ getResources().getString(R.string.str_month) + "0000"
					+ getResources().getString(R.string.str_year));
			mDrh.photoCount.setText(holder.mDateImageList.size()
					+ getResources().getString(R.string.str_img_piece));
			mDrh.timeDuration.setText(R.string.str_detail_unknown);
		} else {
			Date dt = null;
			try {
				dt = MyDateFormat.getDateByString(holder.mDateString,
						"yyyy:MM:dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int year, month, date, hour, min, second;
			long passMills = 0;
			if (dt != null) {
				year = dt.getYear();
				month = dt.getMonth();
				mDrh.date = dt.getDate();
				mDrh.year = year + 1900;
				mDrh.month = month + 1;
				changIntToImage(mDrh.date);
				mDrh.monyearText.setText(mDrh.month
						+ getResources().getString(R.string.str_month)
						+ mDrh.year
						+ getResources().getString(R.string.str_year));
				mDrh.photoCount.setText(holder.mDateImageList.size()
						+ getResources().getString(R.string.str_img_piece));
				Date dt2 = new Date(System.currentTimeMillis());
				Date dt3 = new Date(dt2.getYear(), dt2.getMonth(),
						dt2.getDate(), 23, 59, 59);
				passMills = dt3.getTime() - dt.getTime();
				String durationString = MyDateFormat
						.getIntervalDayFromMillis(passMills);
				mDrh.timeDuration.setText(durationString);
			}
		}
	}
	private boolean isTouchNotInPopupArea(MotionEvent event) {
		// 这里好像Y小于0是点击popup window区域之外
		if (event.getY() < 0) {
			return true;
		} else {
			return false;
		}
	}
	/*
	 * function changIntToImage 将数字天数转换成图片
	 */
	private void changIntToImage(int dayOfMonth) {
		switch (dayOfMonth) {
			case -1:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 1:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			case 2:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[2]);
				break;
			case 3:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[3]);
				break;
			case 4:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[4]);
				break;
			case 5:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[5]);
				break;
			case 6:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[6]);
				break;
			case 7:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[7]);
				break;
			case 8:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[8]);
				break;
			case 9:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[9]);
				break;
			case 10:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 11:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			case 12:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[2]);
				break;
			case 13:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[3]);
				break;
			case 14:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[4]);
				break;
			case 15:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[5]);
				break;
			case 16:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[6]);
				break;
			case 17:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[7]);
				break;
			case 18:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[8]);
				break;
			case 19:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[9]);
				break;
			case 20:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 21:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			case 22:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[2]);
				break;
			case 23:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[3]);
				break;
			case 24:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[4]);
				break;
			case 25:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[5]);
				break;
			case 26:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[6]);
				break;
			case 27:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[7]);
				break;
			case 28:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[8]);
				break;
			case 29:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[9]);
				break;
			case 30:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[3]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 31:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[3]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			default:
				break;
		}
	}
	@TargetApi(11)
	private void updateDatePhotoListView(int newPos) {
		if (mDateImageList == null) {
			return;
		}
		int dateCount = mDateImageList.size();
		if (newPos >= dateCount) {
			newPos = dateCount - 1;
		}
		int pos = dateListAdapter.getTopIndexOfDate(newPos);
		if (dateListView != null) {
			dateListView.setSelected(true);
			if (Utils.getAndroidSDKVersion() >= 11) {
				dateListView.smoothScrollToPositionFromTop(pos, 5);
			} else {
				dateListView.smoothScrollToPosition(pos);
			}
			//dateListView.setSelection(newPos);
		}
	}
	// 关闭PopupWindow，判断是否存在，存在就把它dismiss掉
	private void dismisssPopupWindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
	}
	@Override
	public boolean onBack() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			dismisssPopupWindow();
			return true;
		}
		return false;
	}
	@Override
	public void refreshImagesData() {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPhotoChoiceChange() {
		if (callback != null) {
			callback.onPhotoChoiceChange();
		}
	}
	@Override
	protected void loadImageData() {
		layout_loading.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				mDateImageList = calendarDataGetter.getPathAllDateImageInfo2();
				handler.sendEmptyMessage(LOAD_COMPLEMENT);
				//				updatePhotoScreen();
			}
		}, "loadDateImagesData").start();
	}
	@Override
	protected void loadDataComplete() {
		layout_loading.setVisibility(View.GONE);
		dateListAdapter.setData(mDateImageList);
		dateListAdapter.notifyDataSetInvalidated();
	}
	
	@Override
	protected void notifyDataUpdate() {
		dateListAdapter.notifyDataSetChanged();
	}
	public static GalleryBaseFragment newInstance(String tag) {
		GalleryDateListFragment f = new GalleryDateListFragment();
		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("tag", tag);
		f.setArguments(args);
		return f;
	}

	// 环形日期显示容器
	private class DateRoundHolder {
		private TextView monyearText; // 显示年月
		private int year; // 年
		private int month; // 月
		private int date; // 日
		private ImageView ten;// 日期十位数
		private ImageView single;// 日期个位数
		private TextView timeDuration;
		private TextView photoCount;
		private ImageView hourImage;
		private ImageView minImage;
	}

	public class RoundSeekBarChangeListener implements OnSeekChangeListener {
		@Override
		public void onProgressChange(HalfRoundProgressBar view, int newProgress) {
			if (mDateImageList == null) {
				return;
			}
			int maxProgress = view.getMaxProgress();
			int dateCount = mDateImageList.size();
			int newPos = (newProgress * dateCount) / maxProgress;
			if (newPos >= dateCount) {
				newPos = dateCount - 1;
			}
			updatePopupView(newPos);
			updateDatePhotoListView(newPos);
			updatePopupClockView(newProgress);
		}
	}

	/**
	 * @author Aaron Lee Created at 上午11:12:37 2015-9-7
	 */
	public class GalleryDateListAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater layoutInflater; // 布局加载器
		//	private List<DateImageHolder> data; 
		private List<Object> data;
		private int itemCount;
		private int colorIndex;
		//	private Collection<String> checkedImagePaths = new HashSet<String>();
		//		public PhotoChoiceChangeListener mPhotoCheckedListener;
		private boolean mIsMarkMode;
		private final int[] COLORIMAGERESID = { R.drawable.date_clock1,
				R.drawable.date_clock2, R.drawable.date_clock3,
				R.drawable.date_clock4, R.drawable.date_clock5,
				R.drawable.date_clock6, R.drawable.date_clock7,
				R.drawable.date_clock8 };
		private PhotosChooseManager chooser;
		private List<Integer> datePositions;

		/**
		 * @param context
		 */
		public GalleryDateListAdapter(Context context) {
			super();
			this.context = context;
			layoutInflater = LayoutInflater.from(context);
			chooser = PhotosChooseManager.getInstance();
		}
		public void setData(List<DateImageHolder> dateImages) {
			if (data == null) {
				data = new ArrayList<Object>();
				datePositions = new ArrayList<Integer>(dateImages.size());
			} else {
				data.clear();
				datePositions.clear();
			}
			if (dateImages == null) {
				return;
			}
			for (DateImageHolder h : dateImages) {
				Date dt = null;
				try {
					dt = MyDateFormat.getDateByString(h.mDateString,
							"yyyy:MM:dd");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				DateTitle dateTitle = new DateTitle(dt, h.mDateImageList.size());
				datePositions.add(data.size());
				data.add(dateTitle);
				int index = 0;
				ImageViewsItem infoItem = null;
				for (ImageInfoHolder info : h.mDateImageList) {
					int j = index % 3;
					if (j == 0) {
						infoItem = new ImageViewsItem();
						infoItem.items = new ImageInfoHolder[3];
						data.add(infoItem);
					}
					if (infoItem != null)
						infoItem.items[j] = info;
					index++;
				}
			}
		}
		/*public Collection<String> getCheckListimagespath() {
			return checkedImagePaths;
		}*/
		/*public void setPhotoCheckedListener(PhotoChoiceChangeListener l) {
			mPhotoCheckedListener = l;
		}*/
		public int getMarkedImageCount() {
			return chooser.getChoosedCount();
		}
		public void resetMarkModeStatus() {
			//		checkedImagePaths.clear();
			chooser.exitChooseMode();
			checkAndSetMarkStatus();
			notifyDataSetChanged();
		}
		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			if (data != null) {
				return data.size();
			}
			return 0;
		}
		@Override
		public int getViewTypeCount() {
			return 2;
		}
		@Override
		public int getItemViewType(int position) {
			return getItemType(position).ordinal();
		}
		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			if (data != null) {
				return data.get(position);
			}
			return null;
		}
		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}
		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderGroup groupHolder = null;
			ViewHolderChild childHolder = null;
			final ItemType type = getItemType(position);
			if (convertView == null) {
				switch (type) {
					case TITLE: {
						groupHolder = new ViewHolderGroup();
						convertView = layoutInflater.inflate(
								ViewHolderGroup.VIEWID, null);
						groupHolder.clock = (ImageView) convertView
								.findViewById(R.id.clock);
						groupHolder.month_day = (TextView) convertView
								.findViewById(R.id.month_day);
						groupHolder.num = (TextView) convertView
								.findViewById(R.id.num);
						groupHolder.year_place = (TextView) convertView
								.findViewById(R.id.year_place);
						convertView.setTag(groupHolder);
					}
						break;
					case CHILD: {
						childHolder = new ViewHolderChild();
						convertView = layoutInflater.inflate(
								ViewHolderChild.VIEWID, null);
						childHolder.layout_ImageItems = new View[3];
						childHolder.layout_ImageItems[0] = convertView
								.findViewById(R.id.layout_ImageItem0);
						childHolder.layout_ImageItems[1] = convertView
								.findViewById(R.id.layout_ImageItem1);
						childHolder.layout_ImageItems[2] = convertView
								.findViewById(R.id.layout_ImageItem2);
						convertView.setTag(childHolder);
					}
						break;
					default:
						break;
				}
			} else {
				switch (type) {
					case TITLE: {
						groupHolder = (ViewHolderGroup) convertView.getTag();
					}
						break;
					case CHILD: {
						childHolder = (ViewHolderChild) convertView.getTag();
					}
						break;
					default:
						break;
				}
			}
			// set data
			switch (type) {
				case TITLE: {
					DateTitle item = (DateTitle) getItem(position);
					groupHolder.year_place.setText(context.getString(
							R.string.timefly_yearSting,
							item.date.getYear() + 1900));
					groupHolder.month_day.setText(context.getString(
							R.string.gallery_dateStringFormat,
							item.date.getMonth() + 1, item.date.getDate()));
					groupHolder.num.setText(context.getString(
							R.string.timefly_picCountSting, item.imagesCount));
					groupHolder.clock
							.setImageResource(COLORIMAGERESID[colorIndex]);
					colorIndex++;
					if (colorIndex >= COLORIMAGERESID.length) {
						colorIndex = 0;
					}
				}
					break;
				case CHILD: {
					ImageViewsItem item = (ImageViewsItem) getItem(position);
					for (int i = 0; i < 3; i++) {
						ImageView item_image = (ImageView) childHolder.layout_ImageItems[i]
								.findViewById(R.id.item_image);
						//					CheckBox item_check = (CheckBox) childHolder.layout_ImageItems[i].findViewById(R.id.item_check);
						ImageView btn_showDetail = (ImageView) childHolder.layout_ImageItems[i]
								.findViewById(R.id.btn_showDetail);
						ImageView imv_check = (ImageView) childHolder.layout_ImageItems[i]
								.findViewById(R.id.imv_check);
						bindChildView(childHolder.layout_ImageItems[i],
								item.items[i]);
					}
				}
					break;
				default:
					break;
			}
			return convertView;
		}
		private ItemType getItemType(int position) {
			Object o = getItem(position);
			if (o instanceof DateTitle) {
				return ItemType.TITLE;
			} else if (o instanceof ImageViewsItem) {
				return ItemType.CHILD;
			}
			return ItemType.OTHER;
		}
		private void bindChildView(View parent, final ImageInfoHolder item) {
			if (item != null) {
				parent.setVisibility(View.VISIBLE);
				final ImageView item_image = (ImageView) parent
						.findViewById(R.id.item_image);
				final ImageView btn_showDetail = (ImageView) parent
						.findViewById(R.id.btn_showDetail);
				final ImageView imv_check = (ImageView) parent
						.findViewById(R.id.imv_check);
				//			item_image.setImageResource(R.drawable.friends_sends_pictures_no);
				//			ImageLoaderLocal.getInstance().loadImage(item.mImagePath, item_image);
				/*ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(item.mImagePath), item_image,
						ImageLoaderConfigers.sDefaultGalleryConfig);*/
				//			Log.i(TAG, "setChecked " + position +" " + checkedImagePaths.contains(item.mImagePath) + " " + item.mImagePath);
				ThumbImageFetcher.getInstance(context).loadImage(
						item.mImagePath, item_image);
				boolean choosed = chooser.isChoosed(item);
				imv_check.setVisibility(choosed ? View.VISIBLE : View.GONE);
				btn_showDetail
						.setVisibility(choosed ? View.GONE : View.VISIBLE);
				/*item_check.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (((CheckBox) v).isChecked()) {
							// 记录勾选图片路径
							if (!chooser.choosePhoto(item)) {
								Toast.makeText(context, R.string.showimage_uploadCountFull, Toast.LENGTH_SHORT).show();
								((CheckBox) v).setChecked(false);
							}
						} else {
							// 销毁不勾选图片路径
							chooser.unChoose(item);
						}
						// 检测是否有勾选
						checkAndSetMarkStatus();
						if (mPhotoCheckedListener != null) {
							mPhotoCheckedListener.onPhotoChoiceChange();
						}
					}
				});*/
				OnClickListener l = new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (v == item_image) {
							// 记录勾选图片路径
							if (!chooser.choosePhoto(item)) {
								Toast.makeText(context,
										R.string.showimage_uploadCountFull,
										Toast.LENGTH_SHORT).show();
							} else {
								imv_check.setVisibility(View.VISIBLE);
								btn_showDetail.setVisibility(View.GONE);
								// 检测是否有勾选
								checkAndSetMarkStatus();
								callback.onPhotoChoiceChange();
							}
						} else if (v == imv_check) {
							// 销毁不勾选图片路径
							chooser.unChoose(item);
							// 检测是否有勾选
							checkAndSetMarkStatus();
							v.setVisibility(View.GONE);
							btn_showDetail.setVisibility(View.VISIBLE);
							callback.onPhotoChoiceChange();
						} else if (btn_showDetail == v) {
							/*String titleString = context.getResources().getString(R.string.tabtitle_photo);
							RenewDetailBaseActivity.setBigImageInfoList(DateGridCellGetData.getInstance(context)
									.getmImageInfoList());
							Intent intent = new Intent(context, RenewDetailBaseActivity.class);
							intent.putExtra(CameraActivity.IMAGE_VIEW_HOLDER_EXTRA, item);
							intent.putExtra(CameraActivity.IMAGE_DETAIL_TITLE_STRING, titleString);
							intent.putExtra(CameraActivity.IMAGE_DETAIL_TYPE,
									RenewDetailBaseActivity.DATE_GRID_BIG_IMAGE_TYPE);
							//intent.putExtra(CameraActivity.IMAGE_DETAIL_COUNT, count);
							CameraActivity activity = (CameraActivity) context;
							intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, activity.cameraType);
							activity.startActivityForResult(intent, CameraActivity.REQUESTCODE_ENTER);*/
							callback.showPhotoDetail(-1, DateGridCellGetData
									.getInstance(context).getmImageInfoList(),
									item);
						}
					}
				};
				item_image.setOnClickListener(l);
				imv_check.setOnClickListener(l);
				btn_showDetail.setOnClickListener(l);
			} else {
				parent.setVisibility(View.INVISIBLE);
			}
		}
		public void checkAndSetMarkStatus() {
			mIsMarkMode = chooser.isChooseMode();
		}
		public boolean getMarkModeStatus() {
			return mIsMarkMode;
		}
		public int getTopIndexOfDate(int newPos) {
			if (datePositions != null && datePositions.size() > newPos) {
				return datePositions.get(newPos);
			}
			return 0;
		}

		private class ViewHolderGroup {
			private static final int VIEWID = R.layout.listitem_gallery_titleview;
			private ImageView clock;
			private TextView month_day;
			private TextView num;
			private TextView year_place;
		}

		private class ViewHolderChild {
			private static final int VIEWID = R.layout.listitem_gallery_childview;
			private View[] layout_ImageItems;
		}

		private class DateTitle {
			private Date date;
			private int imagesCount;

			public DateTitle(Date date, int imagesCount) {
				super();
				this.date = date;
				this.imagesCount = imagesCount;
			}
		}

		private class ImageViewsItem {
			private ImageInfoHolder[] items;
		}
	}

	private enum ItemType {
		TITLE, CHILD, OTHER
	}

}
