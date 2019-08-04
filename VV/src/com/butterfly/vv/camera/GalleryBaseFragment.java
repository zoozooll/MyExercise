/**
 * 
 */
package com.butterfly.vv.camera;

import java.util.List;

import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotosChooseManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

/**
 * @author Aaron Lee Created at 下午3:23:03 2015-12-28
 */
public abstract class GalleryBaseFragment extends Fragment {
	public static final int LOAD_COMPLEMENT = 0;
	protected OnGalleryFramentCallback callback;
	protected Handler handler;
	protected GalleryDataService dataService;
	protected PhotosChooseManager chooser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataService = callback.getDataService();
		chooser = callback.getChooser();
	}
	public boolean onBack() {
		return false;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnGalleryFramentCallback) {
			callback = (OnGalleryFramentCallback) activity;
		}
		handler = new Handler(getActivity().getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case LOAD_COMPLEMENT:
						loadDataComplete();
						break;
					default:
						break;
				}
			}
		};
	}
	@Override
	public void onDetach() {
		super.onDetach();
		callback = null;
		handler.removeCallbacksAndMessages(null);
		handler = null;
	}
	public abstract void refreshImagesData();
	protected abstract void loadImageData();
	protected abstract void loadDataComplete();
	protected abstract void notifyDataUpdate();
	public void setOnGalleryFramentCallback(OnGalleryFramentCallback callback) {
		this.callback = callback;
	}

	public static interface OnGalleryFramentCallback {
		public void onPhotoChoiceChange();
		public void onDataLoading();
		public void onDateLoadedComplete();
		public void showPhotoDetail(int position,
				List<ImageInfoHolder> mBigImageList,
				ImageInfoHolder currentPhoto);
		public void showPhotoAlbum(ImageFolderInfo imageFolderInfo);
		public void setTitleString(String title0, String title1);
		public void onFragmentExit(Fragment f);
		public GalleryDataService getDataService();
		public PhotosChooseManager getChooser();
	}
}
