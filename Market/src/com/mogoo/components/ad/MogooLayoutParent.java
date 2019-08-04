package com.mogoo.components.ad;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

public abstract class MogooLayoutParent extends LinearLayout {
	private static final String tag = "MogooLayoutParent";
	protected Context mContext;
	// protected MogooAnimationManager mogooAnimation;
	protected AdOnClickListener mListener;
	
	private DoubleRowSlideButtomView mButtomView;   //add by csq

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AdDataCache.loadingOk: {
				MogooInfo.Log(tag, "数据加载完毕asdfasd!!!");
				updateUi();
				break;
			}
			case AdDataCache.loadingFail: {
				MogooInfo.Log(tag, "加载服务器数据或解释数据时失败");
				break;
			}

			}

			super.handleMessage(msg);
		}
	};

	protected MogooLayoutParent(Context context, LayoutParams localLayoutParams) {
		super(context);
		this.mContext = context;
		this.setLayoutParams(localLayoutParams);
		this.setOrientation(LinearLayout.VERTICAL);
		// mogooAnimation = new MogooAnimationManager(context);
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	protected void setAdOnClickListener(AdOnClickListener listener) {
		this.mListener = listener;
	}

	/**
	 * 当View附加到一个窗体上时，调用此方法<br>
	 */
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		MogooInfo.Log(tag, "onAttachedToWindow()");
		requestData();
		startTimerUpdateUi();
		startTimerUpdateRequestData();
		// startTimer();
	}

	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		clearAll();
	}

	private void clearAll() {
		MogooInfo.resetValue();
		AdDataCache.adPositionItemList.clear();
		stopTimer();
	}

	/**
	 * 请求服务器获取数据,不管是否成功获取到数据都会通知mHandler
	 */
	private void requestData() {
		MogooInfo.Log(tag, "请求服务器获取数据!!!!!!");
		if (MogooInfo.url != null) {
			AdDataCache.startThread(MogooInfo.url, mHandler);
		} else {
			MogooInfo.Log(tag, "未设置URL，无法获取数据!!!");
		}
	}

	/**
	 * 刷新界面
	 */
	protected abstract void updateUi();

	// 使用定时器，每过一段时间请求服务器获取新的数据
	private TimerTask updateTask = new TimerTask() {
		public void run() {
			requestData();

		}
	};

	// 轮播
	private TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = AdDataCache.loadingOk;
			mHandler.sendMessage(message);
		}
	};

	private Timer timer = new Timer();

	// 停止timer
	private void stopTimer() {
		MogooInfo.Log(tag, "停止timer!!!!!!!!!");
		if (timer != null) {
			timer.cancel();
			timer = null;
		}

	}

	// 启动timer
	// private void startTimer()
	// {
	// if (timer == null)
	// return;
	//
	// // 定时刷新界面
	// if (!InitInfo.IS_DOUBLE_ROW && InitInfo.refreshTime > 0)
	// timer.schedule(task, 1000 * 4, 1000 * InitInfo.refreshTime);
	//
	// // 定时请求服务器获取新的数据,当获取到数据后亦会刷新一次界面
	// if (InitInfo.updateTime > 0)
	// timer.schedule(updateTask, 1000 * 4, 1000 * InitInfo.updateTime);
	// }

	/**
	 * 启动轮播
	 */
	protected void startTimerUpdateUi() {
		if (timer == null)
			return;

		// 定时刷新界面
		if (!MogooInfo.IS_DOUBLE_ROW && MogooInfo.refreshTime > 0)
			timer.schedule(task, 1000 * 4, 1000 * MogooInfo.refreshTime);
	}

	/**
	 * 定时请求服务器获取新的数据,当获取到数据后亦会刷新一次界面
	 */
	protected void startTimerUpdateRequestData() {
		if (timer == null)
			return;
		// 定时请求服务器获取新的数据,当获取到数据后亦会刷新一次界面
		if (MogooInfo.updateTime > 0)
			timer.schedule(updateTask, 1000 * 4, 1000 * MogooInfo.updateTime);
	}
	
	
	/**
	 * add by csq:获得广告下面显示当前页及页数的圆点视图
	 * @return
	 */
	public DoubleRowSlideButtomView getButtomView() {
		return mButtomView;
	}

	/**
	 * add by csq:设置广告下面显示当前页及页数的圆点视图
	 * @return
	 */
	public void setButtomView(DoubleRowSlideButtomView buttomView) {
		this.mButtomView = buttomView;
		
		buttomView.setAdView(this);
	}

}
