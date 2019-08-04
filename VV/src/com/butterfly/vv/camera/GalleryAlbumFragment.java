/**
 * 
 */
package com.butterfly.vv.camera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.camera.displayimage.PhotoFolderImageView;
import com.butterfly.vv.camera.photo.PhotoFolderGetData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 显示相册列表（文件夹）的图片列表的Fragment
 * @author Aaron Lee Created at 下午3:03:15 2015-12-28
 */
public class GalleryAlbumFragment extends GalleryBaseFragment {
	public static final String TRANSFER_PHOTO_FOLDER_STRING = "transfer_photo_folder_string";
	private View view;
	private GridView mGridView;
	private ChildAdapter adapter;
	private ImageFolderInfo imageFolderInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageFolderInfo = callback.getDataService().getCurrentAlbum();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
		} else {
			view = inflater.inflate(R.layout.fragment_gallery_album, container,
					false);
			mGridView = (GridView) view.findViewById(R.id.photo_gv);
			mGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//				if (!dopick) {
					/*int count = parent.getCount();
					ChildAdapter tempAdapter = (ChildAdapter) parent.getAdapter();
					ImageInfoHolder imageInfo = tempAdapter.mImageInfoList.get(position);
					int currentCount = position + 1;
					String titleString = imageFolderInfo.mFolderName;
					RenewDetailBaseActivity.setBigImageInfoList(tempAdapter.mImageInfoList);
					Intent intent = new Intent(getActivity(), RenewDetailBaseActivity.class);
					intent.putExtra(IMAGE_VIEW_HOLDER_EXTRA, imageInfo);
					intent.putExtra(IMAGE_DETAIL_TITLE_STRING, titleString);
					intent.putExtra(CameraActivity.IMAGE_DETAIL_TYPE, RenewDetailBaseActivity.PHOTO_GRID_BIG_IMAGE_TYPE);
					intent.putExtra(CameraActivity.IMAGE_DETAIL_CURR_INDEX, currentCount);
					intent.putExtra(CameraActivity.IMAGE_DETAIL_COUNT, count);
					intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, type);
					startActivityForResult(intent, CameraActivity.REQUESTCODE_ENTER);*/
					//				} else {
					/*				ChildAdapter tempAdapter = (ChildAdapter) parent.getAdapter();
								ImageInfoHolder imageInfo = tempAdapter.mImageInfoList.get(position);
								lastIntent = new Intent();
								lastIntent.putExtra(KEY_PHOTO_PATH, imageInfo.mImagePath);
								lastIntent.setClass(ShowImageActivity.this, ClipPictureActivity.class);
								startActivity(lastIntent);*/
					//				}
				}
			});
		}
		String folderName = imageFolderInfo.getmFolderPath();
		List<String> list = PhotoFolderGetData.getInstance().getImageList(
				folderName);
		adapter = new ChildAdapter(getActivity(), imageFolderInfo, list,
				mGridView);
		mGridView.setAdapter(adapter);
		return view;
	}
	public static GalleryBaseFragment newInstance(String tag) {
		GalleryBaseFragment f = new GalleryAlbumFragment();
		Bundle args = new Bundle();
		args.putString("tag", tag);
		f.setArguments(args);
		return f;
	}
	@Override
	public void refreshImagesData() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void loadImageData() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void loadDataComplete() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void notifyDataUpdate() {
		adapter.notifyDataSetChanged();
		
	}

	public class ChildAdapter extends BaseAdapter {
		private Context mContext;
		/**
		 * 用来存储图片的选中情况
		 */
		//	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
		//	private Set<String> mSelectPaths = new HashSet<String>();
		private GridView mGridView;
		private List<String> list;
		protected LayoutInflater mInflater;
		/**
		 * 获取gridview图片的路径
		 */
		//	List<String> listImagePath = new ArrayList<String>();
		/**
		 * 获取图片详细信息集合
		 */
		public List<ImageInfoHolder> mImageInfoList = new ArrayList<ImageInfoHolder>();
		ImageFolderInfo mFolderInfo;
		private int type;

		public ChildAdapter(Context context, ImageFolderInfo infoFolderInfo,
				List<String> list, GridView mGridView) {
			mContext = context;
			this.list = list;
			this.mGridView = mGridView;
			this.mFolderInfo = infoFolderInfo;
			mInflater = LayoutInflater.from(context);
			getFolderImageFile();
		}
		public ChildAdapter(Context context, ImageFolderInfo infoFolderInfo,
				List<String> list, GridView mGridView, int type) {
			mContext = context;
			this.list = list;
			getFolderImageFile();
			this.mGridView = mGridView;
			this.mFolderInfo = infoFolderInfo;
			mInflater = LayoutInflater.from(context);
			this.type = type;
			chooser = PhotosChooseManager.getInstance();
		}
		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			}
			return 0;
		}
		@Override
		public Object getItem(int position) {
			if (list != null) {
				return list.get(position);
			}
			return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;
			final String path = list.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.grid_child_item, null);
				viewHolder = new ViewHolder();
				viewHolder.mImageView = (PhotoFolderImageView) convertView
						.findViewById(R.id.child_image);
				viewHolder.btn_showDetail = (ImageView) convertView
						.findViewById(R.id.btn_showDetail);
				viewHolder.imv_check = (ImageView) convertView
						.findViewById(R.id.imv_check);
				//			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				viewHolder.mImageView
						.setImageResource(R.drawable.deafult_imgloading);
			}
			viewHolder.mImageView.setTag(path);
			boolean isChoosed = chooser.isChoosed(mImageInfoList.get(position));
			viewHolder.imv_check.setVisibility(isChoosed ? View.VISIBLE
					: View.GONE);
			viewHolder.btn_showDetail.setVisibility(isChoosed ? View.GONE
					: View.VISIBLE);
			ThumbImageFetcher.getInstance(mContext).loadImage(path,
					viewHolder.mImageView);
			OnClickListener l = new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageInfoHolder imageInfo = mImageInfoList.get(position);
					if (v == viewHolder.mImageView) {
						// 记录勾选图片路径
						if (!chooser.choosePhoto(imageInfo)) {
							Toast.makeText(mContext,
									R.string.showimage_uploadCountFull,
									Toast.LENGTH_SHORT).show();
						} else {
							viewHolder.imv_check.setVisibility(View.VISIBLE);
							viewHolder.btn_showDetail.setVisibility(View.GONE);
							// 检测是否有勾选
							callback.onPhotoChoiceChange();
						}
					} else if (v == viewHolder.imv_check) {
						// 销毁不勾选图片路径
						chooser.unChoose(imageInfo);
						// 检测是否有勾选
						v.setVisibility(View.GONE);
						viewHolder.btn_showDetail.setVisibility(View.VISIBLE);
						// 检测是否有勾选
						callback.onPhotoChoiceChange();
					} else if (viewHolder.btn_showDetail == v) {
						/*int count = mImageInfoList.size();
						int currentCount = position + 1;
						String titleString = mFolderInfo.getmFolderName();
						RenewDetailBaseActivity.setBigImageInfoList(mImageInfoList);
						Intent intent = new Intent(mContext, RenewDetailBaseActivity.class);
						intent.putExtra(ShowImageActivity.IMAGE_VIEW_HOLDER_EXTRA, imageInfo);
						intent.putExtra(ShowImageActivity.IMAGE_DETAIL_TITLE_STRING, titleString);
						intent.putExtra(CameraActivity.IMAGE_DETAIL_TYPE, RenewDetailBaseActivity.PHOTO_GRID_BIG_IMAGE_TYPE);
						intent.putExtra(CameraActivity.IMAGE_DETAIL_CURR_INDEX, currentCount);
						intent.putExtra(CameraActivity.IMAGE_DETAIL_COUNT, count);
						intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, type);
						((Activity) mContext).startActivityForResult(intent, CameraActivity.REQUESTCODE_ENTER);*/
						callback.showPhotoDetail(position, mImageInfoList, null);
					}
				}
			};
			viewHolder.mImageView.setOnClickListener(l);
			viewHolder.imv_check.setOnClickListener(l);
			viewHolder.btn_showDetail.setOnClickListener(l);
			return convertView;
		}
		public void removeAllSelectedItems() {
			chooser.exitChooseMode();
			notifyDataSetChanged();
		}

		private class ViewHolder {
			public PhotoFolderImageView mImageView;
			//		public CheckBox mCheckBox;
			public ImageView btn_showDetail;
			public ImageView imv_check;
		}

		public List<ImageInfoHolder> getFolderImageFile() {
			// mImageInfoList =
			// fUtil.getAllImageInfoExceptSmall(PhotoCell.mExceptSmallImgSizeKb);
			if (list == null) {
				return null;
			}
			mImageInfoList.clear();
			for (int i = 0; i < list.size(); i++) {
				File f = new File(list.get(i));
				long modifiedTime = f.lastModified();
				ImageInfoHolder holder = new ImageInfoHolder();
				holder.mImageModifiedTime = MyDateFormat.getDateByTimeMillis(
						modifiedTime, "yyyy:MM:dd HH:mm:ss");
				holder.mImagePath = f.getPath();
				mImageInfoList.add(holder); // 将文件的路径添加到list集合中
			}
			// sort by photo shoot time, latest photo is in the head
			/*if (Constants.isDebug) {
				Collections.sort(mImageInfoList, new Comparator<ImageInfoHolder>() {
					@Override
					public int compare(ImageInfoHolder lhs, ImageInfoHolder rhs) {
						// 左边比右边，1表示降序
						int result = rhs.mLatestTime.compareTo(lhs.mLatestTime);
						return result;
					}
				});
			}*/
			return mImageInfoList;
		}
	}

	
}
