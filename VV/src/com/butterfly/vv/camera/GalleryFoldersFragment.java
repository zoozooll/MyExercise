/**
 * 
 */
package com.butterfly.vv.camera;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.photo.PhotoFolderGetData;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 显示相册列表（文件夹）的Fragment
 * @author Aaron Lee Created at 下午2:33:37 2015-12-28
 */
public class GalleryFoldersFragment extends GalleryBaseFragment {
	private View view;
	private GridView mGroupGridViewCamero;
	private GroupAdapter cameroAdapter;
	private List<ImageFolderInfo> listfolder;
	private View emptyView;
	private View progressView;
	private PhotoFolderGetData folderAlbumDataGetter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		folderAlbumDataGetter = PhotoFolderGetData.getInstance();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
		} else {
			view = inflater.inflate(R.layout.xc_lay4, container, false);
			mGroupGridViewCamero = (GridView) view.findViewById(R.id.main_grid);
			mGroupGridViewCamero.setEmptyView(view
					.findViewById(android.R.id.empty));
			cameroAdapter = new GroupAdapter(getActivity(), listfolder);
			mGroupGridViewCamero.setAdapter(cameroAdapter);
			mGroupGridViewCamero
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							//				List<String> childList = mGruopMapFolderInfo.get(listfolder.get(position).getmFolderName());
							// Intent mIntent = new
							// Intent(CameraActivity.this,
							// PhotoGridViewActivity.class);
							/*	Intent mIntent = new Intent(getActivity(), ShowImageActivity.class);
							//				mIntent.putStringArrayListExtra("data", listfolder.get(position).getmFolderName());
							mIntent.putExtra("cameraType", ((CameraActivity) getActivity()).cameraType);
							mIntent.putExtra("transfer_photo_folder_string", listfolder.get(position));
							startActivityForResult(mIntent, CameraActivity.REQUESTCODE_ENTER);*/
							callback.showPhotoAlbum(listfolder.get(position));
						}
					});
			emptyView = view.findViewById(android.R.id.empty);
			mGroupGridViewCamero.setEmptyView(emptyView);
			progressView = view.findViewById(R.id.layout_loading);
		}
		loadImageData();
		return view;
	}
	public static GalleryBaseFragment newInstance(String tag) {
		GalleryBaseFragment f = new GalleryFoldersFragment();
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
		progressView.setVisibility(View.VISIBLE);
		new Thread("load folder data") {
			@Override
			public void run() {
				listfolder = folderAlbumDataGetter.getData(getActivity(), true);
				handler.sendEmptyMessage(LOAD_COMPLEMENT);
			}
		}.start();
	}
	@Override
	protected void loadDataComplete() {
		if (listfolder != null) {
			cameroAdapter.setDataList(listfolder);
			cameroAdapter.notifyDataSetChanged();
		}
		progressView.setVisibility(View.GONE);
	}
	
	@Override
	protected void notifyDataUpdate() {
		cameroAdapter.notifyDataSetChanged();
	}

	public class GroupAdapter extends BaseAdapter {
		private Context mContext;
		private List<ImageFolderInfo> list = new ArrayList<ImageFolderInfo>();
		private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
		protected LayoutInflater mInflater;

		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			return list.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		public GroupAdapter(Context context, List<ImageFolderInfo> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			if (list != null)
				this.list.addAll(list);
		}
		public void setDataList(List<ImageFolderInfo> list) {
			this.list.clear();
			if (list != null)
				this.list.addAll(list);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder viewHolder;
			ImageFolderInfo mImageBean = list.get(position);
			String path = mImageBean.getmFolderFirstImgPath();
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.grid_group_item, null);
				viewHolder.mImageView = (ImageView) convertView
						.findViewById(R.id.group_image);
				viewHolder.mTextViewTitle = (TextView) convertView
						.findViewById(R.id.group_title);
				viewHolder.mTextViewCounts = (TextView) convertView
						.findViewById(R.id.group_count);
				viewHolder.tvw_groupPath = (TextView) convertView
						.findViewById(R.id.tvw_groupPath);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				viewHolder.mImageView
						.setImageResource(R.drawable.deafult_imgloading);
			}
			viewHolder.mTextViewTitle.setText(mImageBean.getmFolderName());
			viewHolder.mTextViewCounts.setText(mContext.getString(
					R.string.timefly_picCountSting,
					mImageBean.getmFolderImgCount()));
			ThumbImageFetcher.getInstance(mContext).loadImage(path,
					viewHolder.mImageView);
			viewHolder.tvw_groupPath.setText(mImageBean.getmFolderPath());
			return convertView;
		}

		public class ViewHolder {
			public ImageView mImageView;
			public TextView mTextViewTitle;
			public TextView mTextViewCounts;
			public TextView tvw_groupPath;
		}
	}

}
