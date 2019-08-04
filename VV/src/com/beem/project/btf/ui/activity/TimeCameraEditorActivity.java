package com.beem.project.btf.ui.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.xmlpull.v1.XmlPullParser;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.views.CartoonShareActivity.RestartActivityType;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.beem.project.btf.ui.fragment.TimeCameraImageFragement.DecadeType;
import com.beem.project.btf.ui.views.CartoonShareActivity;
import com.beem.project.btf.ui.views.DragTextView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.ui.views.SingleTouchView.SingleTouchView;
import com.beem.project.btf.ui.views.SingleTouchView.StaticWrapLayout;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.TimeCameraMaterialUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.piqs.vvcartoon.CartoonLib;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.view.timeflyView.HorizontalListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import de.greenrobot.event.EventBus;

public class TimeCameraEditorActivity extends VVBaseActivity {
	private static final String TAG = "TimeCameraEditorActivity";
	private ImageView layout_bg, layout_bg1, layout_bg2;
	private Galleryadapter mAdapter;
	private HorizontalListView horizontalListView;
	private SingleTouchView singleTouchView;
	private DragTextView myTetx;
	private static Bitmap peopleBm;// 人物抠图
	private Mat mat = new Mat();
	private HashMap<String, List<TimeCameraImageInfo>> LoaclImageDataMap = new HashMap<String, List<TimeCameraImageInfo>>();
	private HashMap<String, View> ViewMap = new HashMap<String, View>();
	private Typeface Tface;
	private String mBmPath;
	private boolean isSaved = false;
	private Context mContext;
	private BBSCustomerDialog blurDlg;
	private String[] noteInfo, textposition;
	private TimeCameraImageInfo currentImageInfo = new TimeCameraImageInfo();
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private String currentdecade;
	private StaticWrapLayout staticWrapLayout;
	private String cid = "";
	private String cgroupid = "";
	private LinearLayout decade_wraper;
	private boolean isprocess = false;
	private Bitmap bgbitmap1, bgbitmap2, bgbitmap3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_camera_editor);
		mContext = this;
		// 导航条设置
		decade_wraper = (LinearLayout) findViewById(R.id.decade_wraper);
		GalleryNavigation mMyNavigationView = (GalleryNavigation) findViewById(R.id.editor_navigation_view);
		mMyNavigationView.setBtnLeftListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mMyNavigationView.setBtnUploadListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddAndSave();
			}
		});
		mMyNavigationView.setBtnLeftIcon(R.drawable.bbs_back_selector);
		mMyNavigationView.setStringTitle("抠图", null);
		mMyNavigationView.setChoiseMode("保存");
		staticWrapLayout = (StaticWrapLayout) findViewById(R.id.staticWrapLayout);
		layout_bg = (ImageView) findViewById(R.id.layout_bg);
		layout_bg1 = (ImageView) findViewById(R.id.layout_bg2);
		layout_bg2 = (ImageView) findViewById(R.id.layout_bg3);
		singleTouchView = (SingleTouchView) findViewById(R.id.singleTouchView1);
		singleTouchView.setImageBitamp(peopleBm, true);
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView1);
		horizontalListView.setNeedResetLocate(false);
		myTetx = (DragTextView) findViewById(R.id.myText);
		// 获取自定义字体库
		Tface = Typeface.createFromAsset(getAssets(), "fonts/dazhibao.ttf");
		myTetx.setTypeface(Tface);
		mAdapter = new Galleryadapter(this, null);
		horizontalListView.setAdapter(mAdapter);
		initViewDatas();
		initLocalImageData();
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position != (mAdapter.getCount() - 1)) {
					currentImageInfo = (TimeCameraImageInfo) mAdapter
							.getItem(position);
					showImageLayout(Integer.parseInt(currentImageInfo
							.getLaycount()));
					mAdapter.setCurrentPotion(position);
					mAdapter.notifyDataSetChanged();
					if (!isprocess) {
						isprocess = true;
						processPeopleBitmap();
					}
					Log.i(TAG, "currentImageInfo" + currentImageInfo.toString());
				} else {
					// 图片下载管理界面
					MaterialLoadingActivity.launch(
							TimeCameraEditorActivity.this,
							DecadeType.getMaterialTypeEx(currentdecade));
				}
			}
		});
		myTetx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						TimeCameraEditorTextActivity.class);
				if (noteInfo != null) {
					intent.putExtra("Text", noteInfo);
				}
				intent.putExtra("CurrentText", myTetx.getText().toString());
				startActivity(intent);
			}
		});
		EventBus.getDefault().register(this);
	}
	/**
	 * 封装标签
	 */
	private void initViewDatas() {
		makeTextview();
		for (final Entry<String, View> entry : ViewMap.entrySet()) {
			entry.getValue().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (String key : ViewMap.keySet()) {
						ViewMap.get(key).setSelected(false);
					}
					List<TimeCameraImageInfo> datas = LoaclImageDataMap
							.get(entry.getKey());
					mAdapter.setItems(datas);
					v.setSelected(true);
					currentdecade = entry.getKey();
				}
			});
		}
	}
	/**
	 * 生成textview
	 */
	private void makeTextview() {
		if (decade_wraper.getChildCount() > 0) {
			decade_wraper.removeAllViews();
		}
		for (DecadeType dt : DecadeType.values()) {
			TextView temp = new TextView(this);
			// 设置样式
			temp.setText(dt.getGroupId() + "年代");
			temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f);
			temp.setClickable(true);
			try {
				XmlPullParser xrp = getResources().getXml(
						R.color.voice_btn_text_selector);
				ColorStateList csl = ColorStateList.createFromXml(
						getResources(), xrp);
				temp.setTextColor(csl);
			} catch (Exception e) {
			}
			temp.setGravity(Gravity.CENTER);
			temp.setBackgroundResource(R.drawable.time_camera_editor_tabbackground_selector);
			temp.setPadding(0, DimenUtils.dip2px(mContext, 15), 0,
					DimenUtils.dip2px(mContext, 10));
			temp.setCompoundDrawablesWithIntrinsicBounds(
					null,
					null,
					null,
					getResources().getDrawable(
							R.drawable.time_camera_editor_dot_selector));
			android.widget.LinearLayout.LayoutParams lp = new LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);
			lp.weight = 1.0f;
			temp.setLayoutParams(lp);
			decade_wraper.addView(temp);
			ViewMap.put(dt.getGroupId(), temp);
		}
	}
	/**
	 * @param 图层层数laycount 加载图片
	 */
	private void showImageLayout(final int laycount) {
		// TODO
		layout_bg.setVisibility(View.VISIBLE);
		if (laycount == 2) {
			layout_bg.setVisibility(View.VISIBLE);
			layout_bg1.setVisibility(View.VISIBLE);
			layout_bg2.setVisibility(View.GONE);
			loadImage(currentImageInfo.getLaypath1(), 1);
			loadImage(currentImageInfo.getLaypath2(), 2);
		} else if (laycount == 3) {
			layout_bg.setVisibility(View.VISIBLE);
			layout_bg1.setVisibility(View.VISIBLE);
			layout_bg2.setVisibility(View.VISIBLE);
			loadImage(currentImageInfo.getLaypath1(), 1);
			loadImage(currentImageInfo.getLaypath2(), 2);
			loadImage(currentImageInfo.getLaypath3(), 3);
		}
		// 用于确保父控件大小测量完成
		getRootView().getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					private int lastHeight = -1;
					private Runnable runnable;
					private OnGlobalLayoutListener instance;

					@Override
					public void onGlobalLayout() {
						if (runnable == null) {
							instance = this;
							runnable = new Runnable() {
								@Override
								public void run() {
									setTextFiled();
									getRootView().getViewTreeObserver()
											.removeGlobalOnLayoutListener(
													instance);
								}
							};
						}
						layout_bg1.removeCallbacks(runnable);
						// 如果layout_bg2上次的高度跟本次获得的高度相等（去除高度为1的情况）表示layout_bg2绘制完成
						if (lastHeight == layout_bg1.getHeight()) {
							runnable.run();
						} else {
							lastHeight = layout_bg1.getHeight();
							layout_bg1.postDelayed(runnable, 500);
						}
					}
				});
	}
	private void loadImage(String path, final int count) {
		ImageLoader.getInstance().loadImage(path, defaultOptions,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
						switch (count) {
							case 1: {
								layout_bg.setImageBitmap(loadedImage);
								bgbitmap1 = loadedImage;
								break;
							}
							case 2: {
								layout_bg1.setImageBitmap(loadedImage);
								bgbitmap2 = loadedImage;
								break;
							}
							case 3: {
								layout_bg2.setImageBitmap(loadedImage);
								bgbitmap3 = loadedImage;
								break;
							}
						}
					}
				});
	}
	/**
	 * 设置文字位置大小等属性
	 */
	private void setTextFiled() {
		//LogUtils.i("staticWrapLayout.getMeasuredWidth():" + staticWrapLayout.getMeasuredWidth());
		if (TextUtils.isEmpty(currentImageInfo.getText())) {
			myTetx.setVisibility(View.GONE);
		} else {
			myTetx.setVisibility(View.VISIBLE);
			noteInfo = BBSUtils.splitString(Constants.TIMECAMERA_TEXTSPLIT,
					currentImageInfo.getText());
			myTetx.setText(noteInfo[0]);
			if (!TextUtils.isEmpty(currentImageInfo.getTextsize())) {
				myTetx.setTextSize(TypedValue.COMPLEX_UNIT_PX,
						Integer.parseInt(currentImageInfo.getTextsize())
								* staticWrapLayout.getMeasuredWidth() / 100);
			}
			if (!TextUtils.isEmpty(currentImageInfo.getTextcolor())) {
				myTetx.setTextColor(Color.parseColor(currentImageInfo
						.getTextcolor()));
			}
			if (!TextUtils.isEmpty(currentImageInfo.getTextposition())) {
				textposition = BBSUtils.splitString(
						Constants.TIMECAMERA_TEXTPOSITIONSPLIT,
						currentImageInfo.getTextposition());
				myTetx.setCenterPoint(
						(staticWrapLayout.getMeasuredWidth() * Integer
								.parseInt(textposition[0])) / 100,
						(staticWrapLayout.getMeasuredHeight() * Integer
								.parseInt(textposition[1])) / 100);
			}
		}
	}
	// 接受传过来的bitmap
	public static void setCartoonBm(Bitmap bm) {
		peopleBm = bm;
	}
	/**
	 * 初始化数据
	 */
	private void initLocalImageData() {
		new AsyncTask<String, Integer, HashMap<String, List<TimeCameraImageInfo>>>() {
			@Override
			protected HashMap<String, List<TimeCameraImageInfo>> doInBackground(
					String... params) {
				// TimeCameraMaterialUtil.initMaterialDB();
				for (DecadeType values : DecadeType.values()) {
					// 根据年代去封装每一个年代的数据
					List<TimeCameraImageInfo> imagedata = new ArrayList<TimeCameraImageInfo>();
					imagedata = TimeCameraImageInfo.queryDownload(values
							.getGroupId());
					LoaclImageDataMap.put(values.getGroupId(), imagedata);
				}
				return LoaclImageDataMap;
			}
			@Override
			protected void onPostExecute(
					HashMap<String, List<TimeCameraImageInfo>> result) {
				if (result != null) {
					int index = 0;
					// 读取用户偏好设置
					cgroupid = SharedPrefsUtil.getValue(mContext,
							SettingKey.TimeCameraMaterial_groupid,
							DecadeType.sixty.getGroupId());
					currentdecade = cgroupid;
					// 判断图片在哪个年代
					List<TimeCameraImageInfo> list = result.get(cgroupid);
					cid = SharedPrefsUtil.getValue(mContext,
							SettingKey.TimeCameraMaterial_id, list.get(0)
									.getId());
					// Log.i(TAG, "currentImageInfo~~id~~"+cid);
					// 判断图片在哪个位置
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getId().equals(cid)) {
							// 根据获取的素材id获取当前的素材实体
							index = i;
							currentImageInfo = list.get(index);
							// Log.i(TAG, "currentImageInfo~~"+currentImageInfo.toString());
						}
					}
					mAdapter.setItems(list);
					mAdapter.setCurrentPotion(index);
					mAdapter.notifyDataSetChanged();
					// 选中某个标签
					for (String key : ViewMap.keySet()) {
						ViewMap.get(key).setSelected(false);
					}
					ViewMap.get(currentdecade).setSelected(true);
					showImageLayout(Integer.parseInt(currentImageInfo
							.getLaycount()));
					processPeopleBitmap();
				}
			}
		}.execute();
	}
	private int[] getIntArrayForString() {
		return TimeCameraMaterialUtil.getIntArrayForString(currentImageInfo);
	}
	/**
	 * 响应数据更新
	 */
	public void onEventMainThread(final EventBusData data) {
		// 判断消息类型
		if (data.getAction() == EventAction.ImageAdd
				|| data.getAction() == EventAction.ImageDelete) {
			new AsyncTask<Void, Integer, List<TimeCameraImageInfo>>() {
				@Override
				protected List<TimeCameraImageInfo> doInBackground(
						Void... params) {
					List<TimeCameraImageInfo> imageurls = TimeCameraImageInfo
							.queryDownload((String) data.getMsg());
					return imageurls;
				}
				@Override
				protected void onPostExecute(List<TimeCameraImageInfo> result) {
					// 替换数据
					LoaclImageDataMap.put((String) data.getMsg(), result);
					for (final Entry<String, View> entry : ViewMap.entrySet()) {
						if (entry.getValue().isSelected()
								&& entry.getKey().equals(data.getMsg())) {
							mAdapter.setItems(result);
							horizontalListView
									.setSelection(mAdapter.getCount() - 1);
						}
					}
				};
			}.execute();
		} else if (data.getAction() == EventAction.TimeCameraNoteTextChange) {
			myTetx.setText((String) data.getMsg());
		} else if (data.getAction() == EventAction.FinishActivity) {
			finish();
		}
	}
	/**
	 * 合成图像并保存
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void AddAndSave() {
		// TODO
		isSaved = true;
		Bitmap bitMap = null;
		Paint paint = new Paint();
		Canvas canvas = null;
		int bmpWidth = 0;
		int bmpHeight = 0;
		if (bgbitmap1 != null) {
			bmpWidth = bgbitmap1.getWidth();
			bmpHeight = bgbitmap1.getHeight();
		}
		int bgWidth = layout_bg.getWidth(), bgHeight = layout_bg.getHeight();
		float scale = (float) bmpWidth / (float) bgWidth;
		bitMap = Bitmap
				.createBitmap(bmpWidth, bmpHeight, bgbitmap1.getConfig());
		canvas = new Canvas(bitMap);
		canvas.drawBitmap(bgbitmap1, 0, 0, paint);
		// 人物图片添加到背景图中
		Bitmap touchBmp = singleTouchView.getBitmap();
		final int touchViewLeft = (int) (singleTouchView.getViewPostion()[0] * scale);
		final int touchViewTop = (int) (singleTouchView.getViewPostion()[1] * scale);
		final int touchRight = (int) (touchViewLeft + touchBmp.getWidth()
				* scale);
		final int touchBottom = (int) (touchViewTop + touchBmp.getHeight()
				* scale);
		final Rect dist = new Rect(touchViewLeft, touchViewTop, touchRight,
				touchBottom);
		canvas.drawBitmap(touchBmp, null, dist, paint);
		canvas.drawBitmap(bgbitmap3, 0, 0, paint);
		// 文字添加到背景图中
		if (myTetx.getVisibility() == View.VISIBLE) {
			// 从文字中获取bitmap
			myTetx.setDrawingCacheEnabled(true);
			myTetx.setFrameDraw(false);
			int dstLeft;
			int dstTop;
			Bitmap textbitmap = myTetx.getDrawingCache();
			if (Build.VERSION.SDK_INT >= 11) {
				dstLeft = (int) (myTetx.getX() * scale);
				dstTop = (int) (myTetx.getY() * scale);
			} else {
				dstLeft = (int) (myTetx.getLeft() * scale);
				dstTop = (int) (myTetx.getRight() * scale);
			}
			int dstRight = (int) (dstLeft + textbitmap.getWidth() * scale);
			int dstBottom = (int) (dstTop + textbitmap.getHeight() * scale);
			Rect dst = new Rect(dstLeft, dstTop, dstRight, dstBottom);
			canvas.drawBitmap(textbitmap, myTetx.getX() * scale, myTetx.getY()
					* scale, paint);
			myTetx.setDrawingCacheEnabled(false);
			myTetx.setFrameDraw(true);
		}
		// 保存并跳转
		try {
			saveToSDCard(bitMap);
			//Log.i(TAG, "~mBmPath~"+mBmPath);
			CartoonShareActivity.launch(TimeCameraEditorActivity.this, mBmPath,
					RestartActivityType.TimeCamera.toString());
			EventBus.getDefault().post(
					new EventBusData(EventAction.FinishActivity, null));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将拍下来的照片存放在SD卡中
	 * @param data
	 * @throws IOException
	 */
	public void saveToSDCard(Bitmap bitmap) throws IOException {
		mBmPath = PictureUtil.saveToSDCard(mContext, bitmap);
	}

	/**
	 * 水平列表适配器
	 */
	public class Galleryadapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<TimeCameraImageInfo> mDatas;
		private int currentPosition = -1;

		public Galleryadapter(Context context, List<TimeCameraImageInfo> mDatas) {
			mInflater = LayoutInflater.from(context);
			this.mDatas = mDatas;
		}
		@Override
		public int getCount() {
			return mDatas == null ? 1 : mDatas.size() + 1;
		}
		@Override
		public Object getItem(int position) {
			return mDatas.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public void setCurrentPotion(int position) {
			this.currentPosition = position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.time_camera_editor_imgitem, null);
				viewHolder.galleryitem = (ImageView) convertView
						.findViewById(R.id.id_index_gallery_item_image);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (currentPosition == position) {
				convertView.setSelected(true);
			} else {
				convertView.setSelected(false);
			}
			if (position == (getCount() - 1)) {
				viewHolder.galleryitem
						.setImageResource(R.drawable.add_materials_icon_h);
			} else {
				ImageLoader.getInstance().displayImage(
						mDatas.get(position).getPathThumb(),
						viewHolder.galleryitem, defaultOptions);
			}
			return convertView;
		}
		public void setItems(List<TimeCameraImageInfo> list) {
			this.mDatas = list;
			currentPosition = -1;
			notifyDataSetChanged();
		}

		class ViewHolder {
			public ImageView galleryitem;
		}
	}

	@Override
	public void registerVVBroadCastReceivers() {
	}
	/**
	 * 处理人物抠图颜色匹配
	 */
	private void processPeopleBitmap() {
		new AsyncTask<Void, Integer, Integer>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(mContext, "图片正在处理请稍候....", false);
			};
			@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
				// 对人物进行背景融合
				Mat tempm = new Mat();
				Utils.bitmapToMat(peopleBm, mat, true);
				Utils.bitmapToMat(getcurrentThumbBitmap(), tempm);
				// int state = CartoonLib.ForeBackProc(mat.getNativeObjAddr(),
				// getIntArrayForString());
				int state = CartoonLib.ForeBackProcWithBackGround(
						tempm.getNativeObjAddr(), mat.getNativeObjAddr(),
						getIntArrayForString());
				return state;
			}
			@Override
			protected void onPostExecute(Integer result) {
				UIHelper.hideDialogForLoading();
				if (result == CartoonLib.SUCESS) {
				}
				Utils.matToBitmap(mat, peopleBm, true);
				singleTouchView.setImageBitamp(peopleBm, true);
				resetBm();
				if (isprocess) {
					isprocess = false;
				}
			};
		}.execute();
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	/* 获取当前缩略图数据 */
	private Bitmap getcurrentThumbBitmap() {
		Bitmap bitmap = ImageLoader.getInstance().loadImageSync(
				currentImageInfo.getPathThumb());
		return bitmap;
	}
	/* 调整人物位置大小 */
	private void resetBm() {
		String bitmapposition[] = BBSUtils.splitString(
				Constants.TIMECAMERA_TEXTPOSITIONSPLIT,
				currentImageInfo.getBitmapposition());
		int x = Integer.parseInt(bitmapposition[0])
				* staticWrapLayout.getMeasuredWidth() / 100;
		int y = Integer.parseInt(bitmapposition[1])
				* staticWrapLayout.getMeasuredHeight() / 100;
		/*
		 * Log.w(TAG, "bitmapposition[0]:" + bitmapposition[0] + "~bitmapposition[1]:" +
		 * bitmapposition[1] + "~staticWrapLayout.getMeasuredWidth():" +
		 * staticWrapLayout.getMeasuredWidth() + "~staticWrapLayout.getMeasuredHeight():" +
		 * staticWrapLayout.getMeasuredHeight() + "~x:" + x + "~y:" + y);
		 */
		int scale = Integer.parseInt(bitmapposition[2]);
		// Log.w(TAG, "1~scale:" + scale + " " + singleTouchView.getViewWidth());
		float tempw = staticWrapLayout.getMeasuredWidth() * scale / 100f;
		float scale2 = tempw / singleTouchView.getViewWidth();
		/*
		 * Log.w(TAG, "2~scale2:" + scale2 + "~tempw:" + tempw + "~singleTouchView.getViewWidth():"
		 * + singleTouchView.getViewWidth());
		 */
		singleTouchView.setImageScale(scale2);
		// Log.w(TAG, "width afeter scale "+ singleTouchView.getViewWidth() );
		x = (int) (x + singleTouchView.getViewWidth() * scale2 / 2);
		y = (int) (y + singleTouchView.getViewHeight() * scale2 / 2);
		singleTouchView.setCenterPoint(new PointF(x, y));
	}
	/** 检测未保存时按返回键 */
	private void checkIsSaved() {
		if (!isSaved) {
			blurDlg = BBSCustomerDialog.newInstance(mContext,
					R.style.blurdialog);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("提示:");
			dilaogView.setTextContent("当前图片还未保存，确认退出吗?");
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					finish();
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		isSaved = false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		if (!peopleBm.isRecycled()) {
			peopleBm.recycle();
		}
		if (bgbitmap1 != null && !bgbitmap1.isRecycled()) {
			bgbitmap1.recycle();
		}
		if (bgbitmap2 != null && !bgbitmap2.isRecycled()) {
			bgbitmap2.recycle();
		}
		if (bgbitmap3 != null && !bgbitmap3.isRecycled()) {
			bgbitmap3.recycle();
		}
	}
}
