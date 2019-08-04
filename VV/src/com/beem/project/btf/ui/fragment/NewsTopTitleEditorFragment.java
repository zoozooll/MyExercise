package com.beem.project.btf.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.adapter.DepthPageTransformer;
import com.beem.project.btf.ui.adapter.NewsCameraViewPagerAdapter;
import com.beem.project.btf.ui.adapter.NewsPopAdapter.CurrentPotionListener;
import com.beem.project.btf.ui.dialog.NewsTopTitlePopupWindow;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo;
import com.beem.project.btf.ui.entity.NewsImageInfo;
import com.beem.project.btf.ui.fragment.BaseImageGridFragment.IMaterialType;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType.PartnerType;
import com.beem.project.btf.ui.views.NewsToptitleFramelayout;
import com.beem.project.btf.ui.views.NewsToptitleViewPager;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.NewsCameraMaterialUtil;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.vv.camera.GalleryActivity;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import de.greenrobot.event.EventBus;

public class NewsTopTitleEditorFragment extends Fragment {
	private static final String TAG = "NewsTopTitleEditorFragment";
	private View view;
	private Context mContext;
	private ImageView arrow_icon;
	private HorizontalScrollView mHorizontalScrollView;
	private LinearLayout mLinearLayout;
	private int item_width;
	private NewsTopTitlePopupWindow popupWindow;
	private ViewGroup rl_listView;
	private int screenWidth;
	private int screenHeight;
	private NewsCameraImageInfo currentCameraImageInfo;
	private List<NewsImageInfo> currentimagedatas = new ArrayList<NewsImageInfo>();//popupwindow正在选择的素材列表
	private List<NewsImageInfo> selectImagedatas;//viewpager的素材列表
	private NewsToptitleViewPager viewpager;
	private int currentposition = 0;
	private NewsCameraViewPagerAdapter pageradapter;
	private boolean isProcess = false;
	private HashMap<String, View> ViewMap = new HashMap<String, View>();
	private boolean isNeedUpdateMaterialViewpager;
	private String currentGroupId;
	private boolean isFirstFashionClick, isFirstPaperClick;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		Log.i(TAG, "~onCreate~");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "~onCreateView~" + (null != view));
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		mContext = getActivity();
		view = inflater
				.inflate(R.layout.news_camera_toptitle, container, false);
		DisplayMetrics dm = BeemApplication.getContext().getResources()
				.getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		arrow_icon = (ImageView) view.findViewById(R.id.arrow_icon);
		mHorizontalScrollView = (HorizontalScrollView) view
				.findViewById(R.id.hsv_view);
		mLinearLayout = (LinearLayout) view.findViewById(R.id.hsv_content);
		rl_listView = (ViewGroup) view.findViewById(R.id.rl_listView);
		viewpager = (NewsToptitleViewPager) view
				.findViewById(R.id.newsToptitleViewPager);
		currentGroupId = NewsMaterialType.Pictorial.getGroupId();
		initArrowAnim();
		initNav();
		initPop();
		initLocalImageData();
		return view;
	}
	/** 初始化popWindow */
	private void initPop() {
		popupWindow = new NewsTopTitlePopupWindow(mContext);
		popupWindow.setCurrentPotionListener(new CurrentPotionListener() {
			@Override
			public void getcurrentpotion(int position) {
				Log.i(TAG, "~position~" + position + "~currentposition~"
						+ currentposition);
				//LogUtils.v("getcurrentpotion_isNeedUpdateMaterialViewpager:" + isNeedUpdateMaterialViewpager);
				if (isNeedUpdateMaterialViewpager) {
					//存在切换标题则需要刷新ViewPager
					if (currentimagedatas != null
							&& currentimagedatas.size() > 0) {
						selectImagedatas = currentimagedatas;
					}
					pageradapter.setCount(selectImagedatas.size());
					pageradapter.notifyDataSetChanged();
					isNeedUpdateMaterialViewpager = false;
					//如果刷新了ViewPager的Adapter且position与先前position的一致,下面的setCurrentItem不会触发OnPageChangeListener的onPageSelect需要手动调用
					if (position == currentposition) {
						pageChangeListener.onPageSelected(position);
					}
				}
				currentGroupId = popupWindow.getGroupid();
				currentposition = position;
				viewpager.setCurrentItem(currentposition);
				NewsToptitleFramelayout newsTopFrllt = (NewsToptitleFramelayout) viewpager
						.findViewById(currentposition);
				newsTopFrllt.getTitleHelper().tinkleAllTiltles();
			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				closePopupWindow();
				selectCurrentGroupId();
				//还原listview数据,与viewpager中的一致
				currentimagedatas = selectImagedatas;
				popupWindow.setDatas(currentGroupId, currentimagedatas,
						currentposition);
			}
		});
	}
	/** 初始化箭头动画 */
	private void initArrowAnim() {
		Log.i(TAG, "~initArrowAnim~");
		// 初始化旋转动画
		arrow_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				} else {
					showPopupWindow();
					setSelection();
				}
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (requestCode == Constants.PICKPHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				new AsyncTask<String, Integer, Boolean>() {
					@Override
					protected void onPreExecute() {
						UIHelper.showDialogForLoading(mContext, "请稍候....",
								false);
					};
					@Override
					protected Boolean doInBackground(String... params) {
						// TODO Auto-generated method stub
						boolean result = false;
						try {
							result = PictureUtil.CompressTempBitmap(data
									.getData().getPath(), PictureUtil
									.getUnClipTempImage().getPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
					}
					@Override
					protected void onPostExecute(Boolean result) {
						if (result) {
							((NewsToptitleFramelayout) viewpager
									.findViewById(currentposition))
									.setEditImageView();
						}
						UIHelper.hideDialogForLoading();
						InnerGuideHelper
								.showNewsTopChangePicGuide(getActivity());
					};
				}.execute();
			}
		}
	}
	//展示popupWindow相关动作
	private void showPopupWindow() {
		if (popupWindow.isShowing())
			popupWindow.dismiss();
		if (ViewHelper.getRotation(arrow_icon) == 180) {
			ObjectAnimator.ofFloat(arrow_icon, "rotation", 180.0f, 0.0f)
					.setDuration(200).start();
		}
		popupWindow.showAtLocation(rl_listView, Gravity.BOTTOM, 0,
				DimenUtils.dip2px(mContext, 90));
	}
	//关闭popupWindow相关动作
	private void closePopupWindow() {
		Log.i(TAG,
				"~ViewHelper.getRotation(arrow_icon)~"
						+ ViewHelper.getRotation(arrow_icon));
		if (ViewHelper.getRotation(arrow_icon) == 0) {
			ObjectAnimator.ofFloat(arrow_icon, "rotation", 0.0f, 180.0f)
					.setDuration(200).start();
		}
		popupWindow.setAlphaAnim(false);
	}
	/** 初始化底部导航栏 */
	private void initNav() {
		Log.i(TAG, "~initNav~");
		item_width = (screenWidth - DimenUtils.dip2px(mContext, 50) - arrow_icon
				.getWidth()) / 4;
		for (final IMaterialType titleType : NewsMaterialType
				.getPartnerMaterials(PartnerType.Top)) {
			TextView textview = new TextView(mContext);
			textview.setText(titleType.getTitle());
			textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f);
			textview.setGravity(Gravity.CENTER);
			textview.setClickable(true);
			try {
				XmlPullParser xrp = getResources().getXml(
						R.color.voice_btn_text_selector);
				ColorStateList csl = ColorStateList.createFromXml(
						getResources(), xrp);
				textview.setTextColor(csl);
			} catch (Exception e) {
			}
			mLinearLayout.addView(textview, item_width,
					DimenUtils.dip2px(mContext, 47));
			ViewMap.put(titleType.getGroupId(), textview);
		}
		for (final Entry<String, View> entry : ViewMap.entrySet()) {
			entry.getValue().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//现在选择的以前没有点击过
					if (!ViewMap.get(entry.getKey()).isSelected()) {
						for (String key : ViewMap.keySet()) {
							ViewMap.get(key).setSelected(
									key.equals(entry.getKey()));
						}
						//TODO
						//获取新的数据
						currentimagedatas = NewsImageInfo.queryDownload(entry
								.getKey());
						int selectPos = currentGroupId.equals(entry.getKey()) ? currentposition
								: -1;
						popupWindow.setDatas(entry.getKey(), currentimagedatas,
								selectPos);
						isNeedUpdateMaterialViewpager = true;
					}
					if (!popupWindow.isShowing()) {
						showPopupWindow();
					}
					if (currentGroupId.equals(popupWindow.getGroupid())) {
						popupWindow.setSelection(currentposition);
					}
				}
			});
		}
		selectCurrentGroupId();
	}
	private void selectCurrentGroupId() {
		// 选中某个标签
		for (String key : ViewMap.keySet()) {
			ViewMap.get(key).setSelected(key.equals(currentGroupId));
		}
	}
	/**
	 * 初始化数据
	 */
	private void initLocalImageData() {
		Log.i(TAG, "~initLocalImageData~");
		new AsyncTask<String, Integer, List<NewsImageInfo>>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(mContext, "初始化数据中....", false);
			};
			@Override
			protected List<NewsImageInfo> doInBackground(String... params) {
				NewsCameraMaterialUtil
						.initTopTitleMaterialDB("image/NewsCameraToptitle/NewsCameraImageLocalInfo1.json");
				NewsCameraMaterialUtil
						.initTopTitleMaterialDB("image/NewsCameraToptitle/NewsCameraImageLocalInfo2.json");
				NewsCameraMaterialUtil
						.initTopTitleMaterialDB("image/NewsCameraToptitle/NewsCameraImageLocalInfo3.json");
				NewsCameraMaterialUtil
						.initTopTitleMaterialDB("image/NewsCameraToptitle/NewsCameraImageLocalInfo4.json");
				currentimagedatas = NewsImageInfo.queryDownload(currentGroupId);
				if (currentimagedatas == null || currentimagedatas.isEmpty()) {
					return null;
				}
				NewsImageInfo currentImageInfo = currentimagedatas.get(0);
				currentCameraImageInfo = NewsCameraMaterialUtil
						.querySingleMaterial(currentImageInfo);
				return currentimagedatas;
			}
			@Override
			protected void onPostExecute(List<NewsImageInfo> result) {
				if (currentimagedatas != null && currentimagedatas.size() > 0) {
					initViewPager();
					popupWindow.setDatas(currentGroupId, currentimagedatas,
							currentposition);
					showPopupWindow();
				} else {
					Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
				}
				UIHelper.hideDialogForLoading();
				InnerGuideHelper.showNewsTopGuide(getActivity());
			}
		}.execute();
	}
	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		Log.i(TAG, "~initViewPager~");
		if (currentimagedatas != null && currentimagedatas.size() > 0) {
			selectImagedatas = currentimagedatas;
		}
		pageradapter = new NewsCameraViewPagerAdapter(this,
				currentimagedatas.size());
		viewpager.setCanScroll(true);
		viewpager.setAdapter(pageradapter);
		viewpager.setOnPageChangeListener(pageChangeListener);
		viewpager.setPageTransformer(true, new DepthPageTransformer());
		((NewsToptitleFramelayout) viewpager.findViewById(0))
				.setCurrentCameraImageInfo(currentCameraImageInfo);
	}
	public NewsToptitleFramelayout generalNewsToptitleView() {
		NewsToptitleFramelayout mview = new NewsToptitleFramelayout(mContext,
				viewpager.getWidth(), viewpager.getHeight());
		mview.setClickListener(clickListener);
		mview.setLongClickListener(longClickListener);
		return mview;
	}
	/** 定位到某个数据 */
	private void setSelection() {
		//必须是popWindow的组id和viewpager的gid一致时才设置popWindow
		if (currentGroupId.equals(popupWindow.getGroupid())) {
			popupWindow.setCurrentPotion(currentposition);
			popupWindow.setSelection(currentposition);
		}
	}

	/**
	 * 页面监听事件
	 */
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			currentposition = arg0;
			popupWindow.setCurrentPotion(currentposition);
			if (!isProcess) {
				isProcess = true;
				new AsyncTask<String, Integer, NewsCameraImageInfo>() {
					@Override
					protected void onPreExecute() {
						UIHelper.showDialogForLoading(mContext, "请稍候....",
								false);
					};
					@Override
					protected NewsCameraImageInfo doInBackground(
							String... params) {
						NewsImageInfo currentImageInfo = selectImagedatas
								.get(currentposition);
						currentCameraImageInfo = NewsCameraMaterialUtil
								.querySingleMaterial(currentImageInfo);
						return null;
					}
					@Override
					protected void onPostExecute(NewsCameraImageInfo result) {
						UIHelper.hideDialogForLoading();
						if (currentCameraImageInfo != null) {
							((NewsToptitleFramelayout) viewpager
									.findViewById(currentposition))
									.setCurrentCameraImageInfo(currentCameraImageInfo);
						}
						isProcess = false;
					};
				}.execute();
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	//响应数据更新
	public void onEventMainThread(final EventBusData data) {
		switch (data.getAction()) {
			case SendUnClipPhoto: {
				new AsyncTask<String, Integer, Boolean>() {
					@Override
					protected void onPreExecute() {
						UIHelper.showDialogForLoading(mContext, "请稍候....",
								false);
					};
					@Override
					protected Boolean doInBackground(String... params) {
						// TODO Auto-generated method stub
						boolean result = false;
						try {
							result = PictureUtil.CompressTempBitmap(
									(String) data.getMsg(), PictureUtil
											.getUnClipTempImage().getPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return result;
					}
					@Override
					protected void onPostExecute(Boolean result) {
						if (result) {
							((NewsToptitleFramelayout) viewpager
									.findViewById(currentposition))
									.setEditImageView();
						}
						UIHelper.hideDialogForLoading();
						InnerGuideHelper
								.showNewsTopChangePicGuide(getActivity());
					};
				}.execute();
				break;
			}
			case ImageAdd:
			case ImageDelete: {
				final String groupid = (String) data.getMsg();
				if (!groupid.contains(PartnerType.Top.getString())
						|| !popupWindow.getGroupid().equals(groupid)) {
					return;
				}
				new AsyncTask<Void, Integer, List<NewsImageInfo>>() {
					@Override
					protected List<NewsImageInfo> doInBackground(Void... params) {
						//List<NewsCameraImageInfo> imagedata = NewsCameraMaterialUtil.queryDownload(groupid);
						currentimagedatas = NewsImageInfo
								.queryDownload(groupid);
						return currentimagedatas;
					}
					@Override
					protected void onPostExecute(List<NewsImageInfo> result) {
						// 替换数据
						popupWindow.setDatas(groupid, currentimagedatas,
								currentposition);
						isNeedUpdateMaterialViewpager = true;
					};
				}.execute();
				break;
			}
		}
	};

	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doPickPhotoFromGallery();
		}
	};
	private OnLongClickListener longClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
					mContext, R.style.blurdialog);
			SimpleDilaogView simpleDilaogView = new SimpleDilaogView(mContext);
			simpleDilaogView.setTitle("是否替换当前图片?");
			simpleDilaogView.setPositiveButton("确定", new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					blurDlg.dismiss();
					doPickPhotoFromGallery();
				}
			});
			blurDlg.setContentView(simpleDilaogView.getmView());
			blurDlg.show();
			return false;
		}
	};

	/**
	 * 调用相册
	 */
	private void doPickPhotoFromGallery() {
		// 跳转到相册库
		/*Intent intent = new Intent();
		intent.setAction("android.intent.action.vv.camera.photo.main");
		intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, CameraActivity.GALLERY_TYPE_NEWSTOPTITLE);
		startActivity(intent);*/
		Intent intent = new Intent(this.getActivity(), GalleryActivity.class);
		intent.putExtra(GalleryActivity.GALLERY_PICKABLE, true);
		intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, 1);
		intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
		intent.putExtra(GalleryActivity.GALLERY_CROP, false);
		startActivityForResult(intent, Constants.PICKPHOTO);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	public String saveBitmap() {
		try {
			Bitmap temp = ((NewsToptitleFramelayout) viewpager
					.findViewById(currentposition)).combineBitmap();
			if (temp != null) {
				return PictureUtil.saveToSDCard(mContext, temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//设置popupWindow是否关闭
	public void dismissPopupWindow() {
		if (popupWindow != null) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		}
	}
	public boolean onBackPressed() {
		boolean isHandle = false;
		if (popupWindow != null && popupWindow.isShowing()) {
			arrow_icon.performClick();
			isHandle = true;
		}
		return isHandle;
	}
}
