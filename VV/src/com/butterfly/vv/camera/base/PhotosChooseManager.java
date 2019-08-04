/**
 * 
 */
package com.butterfly.vv.camera.base;

import java.util.HashSet;
import java.util.Set;

/**
 * SingleTon. Manage to photos chooser
 * @author Aaron Lee Created at 下午1:47:45 2015-9-16
 */
public class PhotosChooseManager {
	private static final String TAG = "PhotosChooseManager";
	private boolean chooseMode;
	private Set<ImageInfoHolder> choosedImages = new HashSet<ImageInfoHolder>();
	private int chooseCountMax;

	private PhotosChooseManager() {
	}
	public Set<ImageInfoHolder> getChoosedSet() {
		return choosedImages;
	}
	public int getChoosedCount() {
		return choosedImages.size();
	}
	public synchronized void unChoose(ImageInfoHolder cancelItem) {
		choosedImages.remove(cancelItem);
		if (choosedImages.size() == 0) {
			chooseMode = false;
		}
		// It need to send local broadcast (eventbus) in feature;
	}
	public boolean isChooseMode() {
		return chooseMode;
	}
	public synchronized void exitChooseMode() {
		chooseMode = false;
		choosedImages.clear();
		// It need to send local broadcast (eventbus) in feature;
	}
	public boolean isChoosed(ImageInfoHolder item) {
		return choosedImages.contains(item);
	}
	public synchronized boolean choosePhoto(ImageInfoHolder item) {
		if (chooseCountMax > choosedImages.size()) {
			choosedImages.add(item);
			chooseMode = true;
			return true;
		} else {
			return false;
		}
	}
	public int getChooseCountMax() {
		return chooseCountMax;
	}
	/**
	 * It can set max chooseCount when ChooseMode is false. If chosemode is true,it will reset
	 * choosemode and clear all of the choosed items and exit choose mode.
	 * @param chooseCountMax The max choose picture count. It will not add item to choosed when
	 *            getChoosedCount() is added to chooseCountMax.
	 */
	public void resetChooseCountMax(int chooseCountMax) {
		if (chooseMode) {
			exitChooseMode();
		}
		this.chooseCountMax = chooseCountMax;
	}

	/** Use inner class to get single reference. It will be easy to get but hard to destroy */
	private static class PhotosChooseHolder {
		private static final PhotosChooseManager INSTANCE = new PhotosChooseManager();
	}

	public static PhotosChooseManager getInstance() {
		return PhotosChooseHolder.INSTANCE;
	}
}
