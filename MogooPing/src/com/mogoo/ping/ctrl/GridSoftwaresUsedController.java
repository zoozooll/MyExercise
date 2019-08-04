/**
 * 
 */
package com.mogoo.ping.ctrl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import com.mogoo.ping.app.MessageDialog;
import com.mogoo.ping.utils.UsedDataManager;
import com.mogoo.ping.vo.UsedActivityItem;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

/**
 * @author Aaron Lee
 * @Date 下午4:40:38  2012-11-2
 */
public class GridSoftwaresUsedController {
	
	private Context mContext;
	private boolean isShowInFront;
	private GridView mGridView;
	private SoftwaresUsedAdapter<UsedActivityItem> mAdapter;
	private int mTag;
	private Runnable mSyncContentThread;
	private Handler mResultHandler;
	
	public GridSoftwaresUsedController(Context context, GridView mGridView, SoftwaresUsedAdapter<UsedActivityItem> mAdapter) {
		mContext = context;
		this.mGridView = mGridView;
		this.mAdapter = mAdapter;
		init();
	}
	
	private void init() {
		mResultHandler = new Handler() {

			/* (non-Javadoc)
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
				super.handleMessage(msg);
			}
			
		};
		mSyncContentThread = new Runnable() {
			
			@Override
			public void run() {
				List< UsedActivityItem> list = UsedDataManager.getFullyUsedApks(mContext);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = list;
				mResultHandler.sendMessage(msg);
			}
		};
	}

	/**
	 * @return the isShowInFront
	 */
	public boolean isShowInFront() {
		return isShowInFront;
	}

	/**
	 * @param isShowInFront the isShowInFront to set
	 */
	public void setShowInFront(boolean isShowInFront) {
		this.isShowInFront = isShowInFront;
	}
}
