package com.butterfly.vv.camera.renew;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.utils.ThreadUtils;
import com.butterfly.vv.camera.Utils;
import com.butterfly.vv.camera.base.CameraBaseListElement;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListViewAdapter extends BaseAdapter {
	private String TAG = ListViewAdapter.class.getSimpleName();
	private List<CameraBaseListElement> resultList; // 相册列表元素
	private Context context; // 上下文
	private LayoutInflater layoutInflater; // 布局加载器
	private int cellTypeCount = 1;
	public static int MAX_LIST_ITEM = 20;
	private final int FIRST_LOAD_COUNT = 4;
	public static List<Bitmap> mBmList = new ArrayList<Bitmap>();
	public static List<Boolean> mCheckList = new ArrayList<Boolean>();
	private MyHandler mMyHandler;
	private final int MSG_UPDATE_IMAGE = 1;

	/** List<String> pathList */
	public ListViewAdapter(Context context) {
		this.context = context;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService("layout_inflater");
		this.resultList = new ArrayList<CameraBaseListElement>();
		mMyHandler = new MyHandler();
	}
	/*
	 * public ListViewAdapter(Context context,List<CameraBaseListElement> list){
	 * this.resultList=list; this.context=context; }
	 */
	/*
	 * public ListViewAdapter(CameraActivity cameraActivity, List<Bitmap> bmList) { this.context =
	 * context; this.mBmList = bmList; }
	 */
	// 获取当前cell的类型
	@Override
	public int getItemViewType(int position) {
		this.resultList.get(position);
		// TODO Auto-generated method stub
		return CameraBaseListElement.cellType;
	}
	// 获取当前适配器有几种cell
	@Override
	public int getViewTypeCount() {
		return cellTypeCount;
	}
	@Override
	public int getCount() {
		int count = resultList.size();
		// Log.d(TAG,"Latest image count:" + count);
		return count;
	}
	@Override
	public Object getItem(int position) {
		return this.resultList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void clearDataList() {
		resultList.clear();
		if (mBmList != null && mBmList.size() > 0) {
			for (Bitmap bm : mBmList) {
				if (bm != null && bm.isRecycled() == false) {
					bm.recycle();
					bm = null;
				}
			}
			mBmList.clear();
		}
	}
	public final List<CameraBaseListElement> getDataList() {
		return resultList;
	}
	public void addList(List<CameraBaseListElement> elements) {
		// resultList.addAll(elements);
		// return elements.size();
		int oldCount = resultList.size();
		int inCount = elements.size();
		int addCount = 0;
		for (int i = oldCount; i < oldCount + inCount; i++) {
			if (i < MAX_LIST_ITEM) {
				resultList.add(i, elements.get(i - oldCount));
				addCount++;
			} else {
				break;
			}
		}
		if (mCheckList.size() == 0) {
			for (int i = 0; i < getCount(); i++) {
				mCheckList.add(Boolean.valueOf(false));
			}
		} else {
			for (int i = 0; i < mCheckList.size(); i++) {
				mCheckList.set(i, false);
			}
		}
		int sampleSize = 0;
		Bitmap bmTemp = null;
		CameraCell tempCell;
		for (int i = 0; i < FIRST_LOAD_COUNT; i++) {
			tempCell = (CameraCell) resultList.get(i);
			sampleSize = Utils
					.getBmSampleSizeForCameraCell(tempCell.mImageInfo.mImageSizeKB);
			bmTemp = Utils.getBitmapFormFile(tempCell.mImageInfo.mImagePath,
					sampleSize);
			mBmList.add(bmTemp);
		}
		Message msg = mMyHandler.obtainMessage();
		msg.what = MSG_UPDATE_IMAGE;
		mMyHandler.sendMessage(msg);
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					int sampleSize = 0;
					Bitmap bmTemp = null;
					int count = resultList.size();
					CameraCell tempCell;
					for (int i = FIRST_LOAD_COUNT; i < count; i++) {
						tempCell = (CameraCell) resultList.get(i);
						sampleSize = Utils
								.getBmSampleSizeForCameraCell(tempCell.mImageInfo.mImageSizeKB);
						bmTemp = Utils.getBitmapFormFile(
								tempCell.mImageInfo.mImagePath, sampleSize);
						mBmList.add(bmTemp);
					}
					Message msg = mMyHandler.obtainMessage();
					msg.what = MSG_UPDATE_IMAGE;
					mMyHandler.sendMessage(msg);
				}
			}
		});
	}
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		Log.d(TAG, "getView, position = " + position);
		view = resultList.get(position).getViewForElement(position,
				layoutInflater, context, view);
		return view;
	}
	public static boolean getItemCheckedStatus(int position) {
		boolean status = false;
		if (mCheckList.size() > position) {
			status = mCheckList.get(position);
		}
		return status;
	}
	public static void setItemCheckedStatus(int position, boolean status) {
		if (mCheckList.size() > position) {
			mCheckList.set(position, status);
		}
	}
	public static void setCheckListAll(boolean result) {
		for (int i = 0; i < mCheckList.size(); i++) {
			mCheckList.set(i, result);
		}
	}
	public static int getSelectedCount() {
		int selectedCount = 0;
		int allCount = mCheckList.size();
		for (int i = 0; i < allCount; i++) {
			if (mCheckList.get(i).booleanValue()) {
				selectedCount++;
			}
		}
		return selectedCount;
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_UPDATE_IMAGE:
					ListViewAdapter.this.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	}
}
