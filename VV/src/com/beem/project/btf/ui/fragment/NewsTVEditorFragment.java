package com.beem.project.btf.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lucasr.twowayview.TwoWayView;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.EditNameDialogView;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.activity.MaterialLoadingActivity;
import com.beem.project.btf.ui.adapter.NewsTVFragmentGridAdapter;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo;
import com.beem.project.btf.ui.entity.NewsCameraImageInfo.NewsTextType;
import com.beem.project.btf.ui.entity.NewsTextInfo;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType;
import com.beem.project.btf.ui.fragment.NewsCameraImageFragement.NewsMaterialType.PartnerType;
import com.beem.project.btf.ui.views.NormalWheelView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.utils.NewsCameraMaterialUtil;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.UIHelper;
import com.butterfly.vv.camera.GalleryActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class NewsTVEditorFragment extends Fragment {
	private Context mContext;
	private View view;
	private TwoWayView horizontalListView;
	private Galleryadapter mAdapter;
	private NewsCameraImageInfo currentImageInfo = new NewsCameraImageInfo();
	private static final String TAG = "NewsTVEditorFragment";
	private GridView Items_gridview;
	private ImageView Image_Pic;
	private BBSCustomerDialog blurDlg;
	private NewsTVFragmentGridAdapter gridAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		view = inflater.inflate(R.layout.news_tv_editor, container, false);
		Image_Pic = (ImageView) view.findViewById(R.id.Image_Pic);
		initHListView();
		initGridview();
		initLocalImageData();
		initPhoto();
		return view;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.PICKPHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				Image_Pic.setImageBitmap(currentImageInfo.combineBitmap());
			}
		}
	}
	/** 初始化水平列表 */
	private void initHListView() {
		horizontalListView = (TwoWayView) view
				.findViewById(R.id.horizontalListView1);
		mAdapter = new Galleryadapter(mContext, null);
		horizontalListView.setAdapter(mAdapter);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position != (mAdapter.getCount() - 1)) {
					currentImageInfo = mAdapter.getItem(position);
					mAdapter.setCurrentPotion(position);
					mAdapter.notifyDataSetChanged();
					gridAdapter.notifyDataSetChanged();
					gridAdapter.setTextInfo(currentImageInfo);
					gridAdapter.notifyDataSetChanged();
					ChangeViewSetting();
				} else {
					// 图片下载管理界面
					MaterialLoadingActivity.launch(mContext, NewsMaterialType
							.getMaterialTypeEx(currentImageInfo.getImageinfo()
									.getGroupid()));
				}
			}
		});
	}
	/** 初始化gridview */
	private void initGridview() {
		Items_gridview = (GridView) view.findViewById(R.id.Items_gridview);
		gridAdapter = new NewsTVFragmentGridAdapter(mContext);
		gridAdapter.setLis(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch ((Integer) (v.getTag())) {
					case 0: {
						initEditDialog(v, NewsTextType.subtitle);
						break;
					}
					case 1: {
						initEditDialog(v, NewsTextType.bigtitle);
						break;
					}
					case 2: {
						initEditDialog(v, NewsTextType.smalltitle);
						break;
					}
					case 3: {
						initEditDialog(v, NewsTextType.marquee);
						break;
					}
					case 4: {
						String[] datas = mContext.getResources()
								.getStringArray(R.array.AreaTitle);
						initSelectDialog(v, NewsTextType.area, datas);
						break;
					}
					case 5: {
						String[] datas = mContext.getResources()
								.getStringArray(R.array.RightTopArray);
						initSelectDialog(v, NewsTextType.righttop, datas);
						break;
					}
					case 6: {
						String[] datas = mContext.getResources()
								.getStringArray(R.array.MovieArray);
						if (currentImageInfo.getMovie().equals(datas[0])) {
							currentImageInfo.setMovie(datas[1]);
							((TextView) v.findViewById(R.id.tv_small_item))
									.setText(datas[1]);
						} else {
							currentImageInfo.setMovie(datas[0]);
							((TextView) v.findViewById(R.id.tv_small_item))
									.setText(datas[0]);
						}
						ChangeViewSetting();
						break;
					}
					case 7: {
						String[] datas = mContext.getResources()
								.getStringArray(R.array.LiveArray);
						initSelectDialog(v, NewsTextType.live, datas);
						break;
					}
				}
			}
		});
		Items_gridview.setAdapter(gridAdapter);
	}
	/**
	 * 编辑对话框
	 */
	private void initEditDialog(final View view, final NewsTextType type) {
		if (blurDlg != null && blurDlg.isShowing()) {
			blurDlg.dismiss();
		}
		blurDlg = BBSCustomerDialog.newInstance(mContext, R.style.blurdialog);
		final EditNameDialogView editNameView = new EditNameDialogView(mContext);
		editNameView.setHint("请输入" + type.getName());
		final NewsTextInfo textInfo = currentImageInfo.getTextInfo(type);
		if (textInfo != null) {
			editNameView.setText(textInfo.getNotetext());
		}
		SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
		dilaogView.setTitle(type.getName());
		dilaogView.setMargin();
		dilaogView.setContentView(editNameView.getView());
		dilaogView.setPositiveButton("确定", new BtnListener() {
			@Override
			public void ensure(View contentView) {
				String content = ((EditText) contentView
						.findViewById(R.id.nickNameEdit)).getText().toString();
				if (TextUtils.isEmpty(content)) {
					if (textInfo != null) {
						content = textInfo.getDefaultNoteText();
					}
				}
				if (view != null) {
					((TextView) view.findViewById(R.id.tv_small_item))
							.setText(content);
				}
				if (textInfo != null) {
					textInfo.setNotetext(content);
				}
				ChangeViewSetting();
				blurDlg.dismiss();
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
	/**
	 * 滚轮选择对话框
	 */
	private void initSelectDialog(final View view, final NewsTextType type,
			String[] datas) {
		if (blurDlg != null && blurDlg.isShowing()) {
			blurDlg.dismiss();
		}
		blurDlg = BBSCustomerDialog.newInstance(mContext, R.style.blurdialog);
		final NormalWheelView areaview = new NormalWheelView(mContext, datas);
		areaview.setShadowColor(0xFFF7F6FA, 0x99F7F6FA, 0x00F7F6FA);
		SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
		dilaogView.setTitle(type.getName());
		dilaogView.setContentView(areaview.getmView());
		dilaogView.setPositiveButton("确定", new BtnListener() {
			@Override
			public void ensure(View contentView) {
				String content = areaview.getcurrentData();
				if (view != null) {
					((TextView) view.findViewById(R.id.tv_small_item))
							.setText(content);
				}
				switch (type) {
					case area: {
						NewsTextInfo textInfo = currentImageInfo
								.getTextInfo(NewsTextType.area);
						if (textInfo != null) {
							textInfo.setNotetext(content);
						}
						break;
					}
					case righttop: {
						currentImageInfo.setRightTop(content);
						break;
					}
					case live: {
						currentImageInfo.setLive(content);
						break;
					}
				}
				ChangeViewSetting();
				blurDlg.dismiss();
			}
		});
		blurDlg.setContentView(dilaogView.getmView());
		blurDlg.show();
	}
	/**
	 * 获取图像
	 */
	private void initPhoto() {
		Image_Pic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPickPhotoFromGallery();
			}
		});
	}
	/**
	 * 调用相册
	 */
	protected void doPickPhotoFromGallery() {
		// 跳转到相册库
		/*Intent intent = new Intent();
		intent.setAction("android.intent.action.vv.camera.photo.main");
		intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, CameraActivity.GALLERY_TYPE_NEWSTV);
		startActivity(intent);*/
		Intent intent = new Intent(this.getActivity(), GalleryActivity.class);
		intent.putExtra(GalleryActivity.GALLERY_PICKABLE, true);
		intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, 1);
		intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
		intent.putExtra(GalleryActivity.GALLERY_CROP, true);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 900);
		intent.putExtra("aspectY", 602);
		intent.putExtra("outputX", 900);
		intent.putExtra("outputY", 602);
		intent.putExtra("scale", true);
		intent.putExtra("output", PictureUtil.getClipTempImage());
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, Constants.PICKPHOTO);
	}
	/**
	 * 初始化数据
	 */
	private void initLocalImageData() {
		new AsyncTask<String, Integer, List<NewsCameraImageInfo>>() {
			@Override
			protected void onPreExecute() {
				UIHelper.showDialogForLoading(mContext, "初始化数据中....", false);
			};
			@Override
			protected List<NewsCameraImageInfo> doInBackground(String... params) {
				NewsCameraMaterialUtil.initTVMaterialDB();
				List<NewsCameraImageInfo> imagedata = new ArrayList<NewsCameraImageInfo>();
				imagedata = NewsCameraMaterialUtil
						.queryDownload(NewsMaterialType.TVNews.getGroupId());
				return imagedata;
			}
			@Override
			protected void onPostExecute(List<NewsCameraImageInfo> result) {
				//Log.i(TAG, "result~~"+result.toString());
				if (result != null && result.size() > 0) {
					currentImageInfo = result.get(0);
					Log.i(TAG,
							"currentImageInfo~~" + currentImageInfo.toString());
					mAdapter.setItems(result);
					mAdapter.setCurrentPotion(0);
					mAdapter.notifyDataSetChanged();
					gridAdapter.setTextInfo(currentImageInfo);
					gridAdapter.notifyDataSetChanged();
					ChangeViewSetting();
				} else {
					Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
				}
				UIHelper.hideDialogForLoading();
				InnerGuideHelper.showNewsTvGuide(getActivity());
			}
		}.execute();
	}
	/**
	 * 刷新界面
	 */
	private void ChangeViewSetting() {
		currentImageInfo.ChangePaintSetting();
		Image_Pic.setImageBitmap(currentImageInfo.combineBitmap());
	}

	/**
	 * 水平列表适配器
	 */
	public class Galleryadapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private List<NewsCameraImageInfo> mDatas;
		private int currentPosition = -1;
		private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.transparent)
				.showImageOnFail(R.drawable.transparent).cacheInMemory(true)
				.cacheOnDisk(true).build();

		public Galleryadapter(Context context, List<NewsCameraImageInfo> mDatas) {
			mInflater = LayoutInflater.from(context);
			this.mDatas = mDatas;
		}
		@Override
		public int getCount() {
			return mDatas == null ? 1 : mDatas.size() + 1;
		}
		@Override
		public NewsCameraImageInfo getItem(int position) {
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
				viewHolder.galleryitem.setSelected(true);
			} else {
				viewHolder.galleryitem.setSelected(false);
			}
			if (position == (getCount() - 1)) {
				viewHolder.galleryitem
						.setImageResource(R.drawable.add_materials_icon_h);
			} else {
				ImageLoader.getInstance().displayImage(
						mDatas.get(position).getImageinfo().getPathThumb(),
						viewHolder.galleryitem, defaultOptions);
			}
			return convertView;
		}
		public void setItems(List<NewsCameraImageInfo> list) {
			this.mDatas = list;
			currentPosition = -1;
		}

		class ViewHolder {
			public ImageView galleryitem;
		}
	}

	//响应数据更新
	public void onEventMainThread(final EventBusData data) {
		switch (data.getAction()) {
			case SendClipPhoto: {
				Image_Pic.setImageBitmap(currentImageInfo.combineBitmap());
				break;
			}
			case ImageAdd:
			case ImageDelete: {
				final String groupid = (String) data.getMsg();
				if (!groupid.contains(PartnerType.TV.getString())) {
					return;
				}
				new AsyncTask<Void, Integer, List<NewsCameraImageInfo>>() {
					@Override
					protected List<NewsCameraImageInfo> doInBackground(
							Void... params) {
						List<NewsCameraImageInfo> imagedata = NewsCameraMaterialUtil
								.queryDownload(groupid);
						return imagedata;
					}
					@Override
					protected void onPostExecute(
							List<NewsCameraImageInfo> result) {
						// 替换数据
						mAdapter.setItems(result);
						mAdapter.notifyDataSetChanged();
						horizontalListView
								.setSelection(mAdapter.getCount() - 1);
					};
				}.execute();
				break;
			}
		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	public String saveBitmap() {
		try {
			Bitmap temp = currentImageInfo.getCombineBitmap();
			if (temp != null) {
				return PictureUtil.saveToSDCard(mContext, temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean onBackPressed() {
		return false;
	}
}
