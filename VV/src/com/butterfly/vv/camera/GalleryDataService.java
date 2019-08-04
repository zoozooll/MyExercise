/**
 * 
 */
package com.butterfly.vv.camera;

import java.util.List;

import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;

/**
 * 控制图片数据，即图片路径以及图片所属文件夹等信息；
 * @author Aaron Lee Created at 下午7:21:38 2016-1-14
 */
public class GalleryDataService {
	private int currentPosition = -1;
	// 当前正在查看的图片信息
	private ImageInfoHolder currentPhoto;
	// 当前查看的图片所在的目录的信息。有可能是路径目录，也有可能是日期目录；
	private ImageFolderInfo currentAlbum;
	// 当前在显示的图片列表（在详情页使用）
	private List<ImageInfoHolder> mBigImageList;

	public List<ImageInfoHolder> getmBigImageList() {
		return mBigImageList;
	}
	public void setmBigImageList(List<ImageInfoHolder> mBigImageList) {
		this.mBigImageList = mBigImageList;
	}
	public ImageInfoHolder getCurrentPhoto() {
		return currentPhoto;
	}
	public void setCurrentPhoto(ImageInfoHolder currentPhoto) {
		this.currentPhoto = currentPhoto;
	}
	public ImageFolderInfo getCurrentAlbum() {
		return currentAlbum;
	}
	public void setCurrentAlbum(ImageFolderInfo currentAlbum) {
		this.currentAlbum = currentAlbum;
	}
	public int getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	public void setDetailData(List<ImageInfoHolder> mBigImageList,
			int position, ImageInfoHolder currentPhoto) {
		this.mBigImageList = mBigImageList;
		if (position >= 0) {
			// If position >= 0, set this.currentPhoto as the item of mBigImageList with index as position, igonre currentPhoto
			currentPosition = position;
			this.currentPhoto = mBigImageList.get(position);
		} else {
			// If position < 0, set this.currentPhoto as currentPhoto,and set this.position value as the index of currentPhoto
			// in mBigImageList;
			this.currentPhoto = currentPhoto;
			currentPosition = mBigImageList.indexOf(currentPhoto);
		}
	}
}
