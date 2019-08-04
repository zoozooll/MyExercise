package com.mogoo.components.ad;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 双行，可滑动
 *
 * @author Administrator
 *
 */
class DoubleRowSlideLayout extends MogooLayoutParent
{

	private static final String tag = "DoubleRowSlideLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mCurScreen;
	private int mDefaultScreen = 0;
	public static final int TOUCH_STATE_REST = 0;
	public static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 250;
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private DoubleRowView[] DoubleRowViewArr;
	private long mLastSnapTime = 0;
	private Handler mHandler = new Handler();
	
	private void Log(String str)
	{
		MogooInfo.Log(tag, str);
	}

	public DoubleRowSlideLayout(Context context, LayoutParams localLayoutParams)
	{
		super(context, localLayoutParams);
		initLayout(context);
	}

	public void updateUi()
	{
		int r = 0;
		int size2 = DoubleRowViewArr.length;
		List<AdPositionItem> itemList = new ArrayList<AdPositionItem>();
		int size = AdDataCache.adPositionItemList.size();
		Log("更新展示位数据...");
		Log("AdDataCache.adPositionItemList.size():" + size);

		try
		{
			for (int i = 1; i < size + 1; i++)
			{

				if (size < ((r + 1) * 4))
				{

					Log("is short item start i:::::::::::::::::::" + i);

					// 清掉之前的itemList数据
					itemList.clear();

					for (int j = i; j < size + 1; j++)
					{
						itemList.add(AdDataCache.adPositionItemList.get(j - 1));
					}

					// Log("is short item end i:::::::::::::::::::" + j);

					if (r < size2)
					{
						Log("is  item ad :::::::::::::::::::" + r);
						DoubleRowViewArr[r].setAdData(itemList);
						DoubleRowViewArr[r].setAdOnClickListener(mListener);
						itemList.clear();
						break;
					}
				}

				itemList.add(AdDataCache.adPositionItemList.get(i - 1));

				if (i % 4 == 0)
				{
					Log("i:::::::::::::::::::" + i);
					if (r < size2)
					{
						DoubleRowViewArr[r].setAdData(itemList);
						DoubleRowViewArr[r].setAdOnClickListener(mListener);
						itemList.clear();
					}
					r++;
				}

			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}

		invalidate();
	}

	private void initLayout(Context context)
	{
		mContext = context;
		mScroller = new Scroller(context);
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
		// setPadding(MogooInfo.AD_PADING, MogooInfo.AD_PADING,
		// MogooInfo.AD_PADING, MogooInfo.AD_PADING);

		DoubleRowViewArr = new DoubleRowView[MogooInfo.PAGE];
		DoubleRowView view = null;
		for (int i = 0; i < MogooInfo.PAGE; i++)
		{
			view = new DoubleRowView(mContext);
			DoubleRowViewArr[i] = view;
			addView(view);
		}
		
		//add by csq:自动翻页进程开启
		if(MogooInfo.autoSnapTime>0)
		{
			// new AutoSnapThread().start();
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					long duration = System.currentTimeMillis() - mLastSnapTime;
					if (duration >= 5000) {
						if (mCurScreen + 1 >= MogooInfo.PAGE) {
							isAutoSnapAdd = false;
							snapToScreen(--mCurScreen);
						} else if (mCurScreen - 1 < 0) {
							isAutoSnapAdd = true;
							snapToScreen(++mCurScreen);
						} else {
							if (isAutoSnapAdd) {
								snapToScreen(++mCurScreen);
							} else {
								snapToScreen(--mCurScreen);
							}
						}
						mTouchState = TOUCH_STATE_REST;
						duration = 0;
					}
					mHandler.postDelayed(this, MogooInfo.autoSnapTime - duration);
				}
			}, MogooInfo.autoSnapTime);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		if (changed)
		{
			int childLeft = 0;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++)
			{
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE)
				{
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		Log("onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		/*if (widthMode != MeasureSpec.EXACTLY)
		{
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}*/
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		/*if (heightMode != MeasureSpec.EXACTLY)
		{
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}*/
		final int count = getChildCount();
		for (int i = 0; i < count; i++)
		{
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	public void snapToDestination()
	{
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen)
	{

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth()))
		{
			final int delta = whichScreen * getWidth() - getScrollX();
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);
			mCurScreen = whichScreen;
			Log("第几页:" + mCurScreen);
			// listAdView.get(mCurScreen).startHttpThread();
			if(0 <= whichScreen && whichScreen < DoubleRowViewArr.length) {
				DoubleRowViewArr[whichScreen].refreshImageViews();
			}
			invalidate(); // Redraw the layout
			
			//add by csq
			if(getButtomView()!=null)
			{
				getButtomView().invalidateView(mCurScreen);
			}
			mLastSnapTime = System.currentTimeMillis();
		}
	}

	public void setToScreen(int whichScreen)
	{
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
	}

	public int getCurScreen()
	{
		return mCurScreen;
	}

	@Override
	public void computeScroll()
	{

		if (mScroller.computeScrollOffset())
		{
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mVelocityTracker == null)
		{
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			Log("event down!");
			// Log.i(TAG, "Find focus in " + this + ": flags="
			// + isFocused() + ", child=" + getFocusedChild());
			if (!mScroller.isFinished())
			{
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			mLastMotionX = x;
			scrollBy(deltaX, 0);
			break;
		case MotionEvent.ACTION_UP:
			Log("event : up");
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			Log("velocityX:" + velocityX);
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0)
			{
				// Fling enough to move left
				Log("snap left");
				snapToScreen(mCurScreen - 1);

			}
			else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1)
			{
				// Fling enough to move right
				Log("snap right");
				snapToScreen(mCurScreen + 1);
			}
			else
			{
				snapToDestination();
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			snapToDestination();
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		// Log.e(TAG, "onInterceptTouchEvent()");
		// TODO Auto-generated method stub
		// Log.i(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop + "; action = "
		// + ev.getAction());

		// Log.i(TAG, "Find focus in " + this + ": flags=" + isFocused()
		// + ", child=" + getFocusedChild());
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST))
		{
			Log("onInterceptTouchEvent-action:" + action);
			return true;
		}
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action)
		{
		case MotionEvent.ACTION_MOVE:

			final int xDiff = (int) Math.abs(mLastMotionX - x);
			Log("onInterceptTouchEvent-move: xDiff = " + xDiff
					+ "; mLastMotionX = " + mLastMotionX);
			if (xDiff > mTouchSlop)
			{

				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			Log("onInterceptTouchEvent-down: x = " + x + "; y = " + y);
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Log("onInterceptTouchEvent-up: mLastMotionX = " + mLastMotionX);
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}
	
	/**
	 * @author csq:自动翻页进程UI更新Handler
	 */
	private boolean isAutoSnapAdd = true;
	Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			removeMessages(1);
			if(mCurScreen + 1 >=  MogooInfo.PAGE)
			{
				isAutoSnapAdd = false;
				snapToScreen(--mCurScreen);
			}
			else if(mCurScreen - 1 < 0)
			{
				isAutoSnapAdd = true;
				snapToScreen(++mCurScreen);
			}
			else
			{
				if(isAutoSnapAdd)
				{
					snapToScreen(++mCurScreen);
				}
				else
				{
					snapToScreen(--mCurScreen);
				}
			}
			
			mTouchState = TOUCH_STATE_REST;
			super.handleMessage(msg);
		}
	};
	
	/**
	 * @author csq:自动翻页进程
	 */
	class AutoSnapThread extends Thread
	{
		@Override
		public void run() 
		{
			while(MogooInfo.autoSnapTime > 0)
			{
				if(mTouchState!=TOUCH_STATE_SCROLLING)
				{
					handler.sendEmptyMessage(1);
					try {
						Thread.currentThread();
						Thread.sleep(MogooInfo.autoSnapTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			super.run();
		}
	}

	/**
	 * @author csq:设置触摸状态
	 */
	public void setmTouchState(int mTouchState) 
	{
		this.mTouchState = mTouchState;
	}

}
