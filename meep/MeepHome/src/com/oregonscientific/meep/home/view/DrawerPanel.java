/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.home.R;

/**
 * 
 * Fixed by http://blog.csdn.net/lovehong0306/article/details/7451264
 * 
 */
public class DrawerPanel extends LinearLayout {

	private static final String TAG = "Panel";

	private static final float MAXIMUM_MAJOR_VELOCITY = 200.0f;
	private static final float MAXIMUM_ACCELERATION = 1500.0f;
	private static final int MSG_ANIMATE = 1000;
	private static final int MSG_PREPARE_ANIMATE = 2000;
	private static final int ANIMATION_FRAME_DURATION = 250 / 60;

	private final Handler mHandler = new SlidingHandler(this);
	
	private float mAnimatedAcceleration;
	private long mAnimationLastTime;
	private long mCurrentAnimationTime;
	private boolean mAnimating;

	private final int mMaximumMajorVelocity;
	private final int mMaximumAcceleration;

	private float lastRawX, lastRawY, curRawX, curRawY;
	private float lastEventTime, curEventTime;

	/**
	 * Callback invoked when the panel is opened/closed.
	 */
	public static interface OnPanelListener {
		/**
		 * Invoked when the panel becomes fully closed.
		 */
		public void onPanelClosed(DrawerPanel panel);

		/**
		 * Invoked when the panel becomes fully opened.
		 */
		public void onPanelOpened(DrawerPanel panel);
	}

	private boolean mIsShrinking;
	private int mPosition;
	private int mDuration;
	private boolean mLinearFlying;
	private int mHandleId;
	private int mContentId;
	private int mTapAreaId;
	private View mHandle;
	private View mContent;
	private View mTapArea;
	private Drawable mOpenedHandle;
	private Drawable mClosedHandle;
	private float mTrackX;
	private float mTrackY;
	private float mVelocity;

	private OnPanelListener panelListener;

	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	private enum State {
		ABOUT_TO_ANIMATE, ANIMATING, READY, TRACKING, FLYING, CLICK
	};

	private State mState;
	private Interpolator mInterpolator;
	private GestureDetector mGestureDetector;
	private int mContentHeight;
	private int mContentWidth;
	private int mOrientation;
	private float mWeight;
	private PanelOnGestureListener mGestureListener;
	private boolean mBringToFront;
	private int mContentVisibility = GONE;

	@SuppressWarnings("deprecation")
	public DrawerPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Panel);
		mDuration = a.getInteger(R.styleable.Panel_animationDuration, 750); // duration defaults to 750 ms
		mPosition = a.getInteger(R.styleable.Panel_position, BOTTOM); // position defaults to BOTTOM
		mLinearFlying = a.getBoolean(R.styleable.Panel_linearFlying, false); // linearFlying defaults to false
		mWeight = a.getFraction(R.styleable.Panel_weight, 0, 1, 0.0f); // weight defaults to 0.0
		if (mWeight < 0 || mWeight > 1) {
			mWeight = 0.0f;
			Log.w(TAG, a.getPositionDescription() + ": weight must be > 0 and <= 1");
		}
		mOpenedHandle = a.getDrawable(R.styleable.Panel_openedHandle);
		mClosedHandle = a.getDrawable(R.styleable.Panel_closedHandle);

		RuntimeException e = null;
		mHandleId = a.getResourceId(R.styleable.Panel_handle, 0);
		if (mHandleId == 0) {
			e = new IllegalArgumentException(a.getPositionDescription()
					+ ": The handle attribute is required and must refer to a valid child.");
		}
		mContentId = a.getResourceId(R.styleable.Panel_content, 0);
		if (mContentId == 0) {
			e = new IllegalArgumentException(a.getPositionDescription()
					+ ": The content attribute is required and must refer to a valid child.");
		}
		mTapAreaId = a.getResourceId(R.styleable.Panel_tapArea, 0);
		a.recycle();

		float density = getResources().getDisplayMetrics().density;
		mMaximumMajorVelocity = (int) (MAXIMUM_MAJOR_VELOCITY * (density + 0.5f));
		mMaximumAcceleration = (int) (MAXIMUM_ACCELERATION * (density + 0.5f));

		if (e != null) {
			throw e;
		}
		mOrientation = (mPosition == TOP || mPosition == BOTTOM) ? VERTICAL : HORIZONTAL;
		setOrientation(mOrientation);
		mState = State.READY;
		mGestureListener = new PanelOnGestureListener();
		mGestureDetector = new GestureDetector(mGestureListener);
		mGestureDetector.setIsLongpressEnabled(false);

		// i DON'T really know why i need this...
		setBaselineAligned(false);
	}

	/**
	 * Sets the listener that receives a notification when the panel becomes
	 * open/close.
	 * 
	 * @param onPanelListener
	 *            The listener to be notified when the panel is opened/closed.
	 */
	public void setOnPanelListener(OnPanelListener onPanelListener) {
		panelListener = onPanelListener;
	}

	/**
	 * Gets Panel's mHandle
	 * 
	 * @return Panel's mHandle
	 */
	public View getHandle() {
		return mHandle;
	}

	/**
	 * Gets Panel's mContent
	 * 
	 * @return Panel's mContent
	 */
	public View getContent() {
		return mContent;
	}

	/**
	 * Sets the acceleration curve for panel's animation.
	 * 
	 * @param i
	 *            The interpolator which defines the acceleration curve
	 */
	public void setInterpolator(Interpolator i) {
		mInterpolator = i;
	}

	/**
	 * Set the opened state of Panel.
	 * 
	 * @param open
	 *            True if Panel is to be opened, false if Panel is to be closed.
	 * @param animate
	 *            True if use animation, false otherwise.
	 * 
	 * @return True if operation was performed, false otherwise.
	 * 
	 */
	public boolean setOpen(boolean open, boolean animate) {
		if (mState == State.READY && isOpen() ^ open) {
			mIsShrinking = !open;
			if (animate) {
				mState = State.ABOUT_TO_ANIMATE;
				if (!mIsShrinking) {
					// this could make flicker so we test mState in dispatchDraw()
					// to see if is equal to ABOUT_TO_ANIMATE
					setContentVisibility(VISIBLE);
				}
				long now = SystemClock.uptimeMillis();
				mAnimationLastTime = now;
				mCurrentAnimationTime = now + ANIMATION_FRAME_DURATION;
				mHandler.removeMessages(MSG_ANIMATE);
				mHandler.removeMessages(MSG_PREPARE_ANIMATE);
				mHandler.sendMessageAtTime(
						mHandler.obtainMessage(MSG_PREPARE_ANIMATE),
						mCurrentAnimationTime);
			} else {
				setContentVisibility(open ? VISIBLE : GONE);
				postProcess();
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns the opened status for Panel.
	 * 
	 * @return True if Panel is opened, false otherwise.
	 */
	public boolean isOpen() {
		return mContent.getVisibility() == VISIBLE;
//		return mContentVisibility == VISIBLE;
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mHandle = findViewById(mHandleId);
		if (mHandle == null) {
			String name = getResources().getResourceEntryName(mHandleId);
			throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id."
					+ name + "'");
		}
		mTapArea = findViewById(mTapAreaId);
		mTapArea.setClickable(true);
		mTapArea.setOnTouchListener(touchListener);
		mHandle.setClickable(true);
		mHandle.setOnTouchListener(touchListener);
		if (mHandle instanceof OnPanelListener) {
			setOnPanelListener((OnPanelListener) mHandle);
		}

		mContent = findViewById(mContentId);
		if (mContent == null) {
			String name = getResources().getResourceEntryName(mHandleId);
			throw new RuntimeException("Your Panel must have a child View whose id attribute is 'R.id."
					+ name + "'");
		}

		if (mClosedHandle != null) {
			mHandle.setBackgroundDrawable(mClosedHandle);
		}
		mContent.setClickable(true);
		setContentVisibility(GONE);
		if (mWeight > 0) {
			ViewGroup.LayoutParams params = mContent.getLayoutParams();
			if (mOrientation == VERTICAL) {
				params.height = ViewGroup.LayoutParams.FILL_PARENT;
			} else {
				params.width = ViewGroup.LayoutParams.FILL_PARENT;
			}
			mContent.setLayoutParams(params);
		}
		Log.d(TAG, "Finished inflating the layout");
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		ViewParent parent = getParent();
		if (parent != null && parent instanceof FrameLayout) {
			mBringToFront = true;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		if (mWeight > 0 && mContent.getVisibility() == VISIBLE) {
		if (mWeight > 0 && isOpen()) {
			View parent = (View) getParent();
			if (parent != null) {
				if (mOrientation == VERTICAL) {
					heightMeasureSpec = MeasureSpec.makeMeasureSpec(
							(int) (parent.getHeight() * mWeight),
							MeasureSpec.EXACTLY);
				} else {
					widthMeasureSpec = MeasureSpec.makeMeasureSpec(
							(int) (parent.getWidth() * mWeight),
							MeasureSpec.EXACTLY);
				}
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mContentWidth = mContent.getWidth();
		mContentHeight = mContent.getHeight();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// this is why 'mState' was added:
		// avoid flicker before animation start
		if (mState == State.ABOUT_TO_ANIMATE && !mIsShrinking) {
			int delta = mOrientation == VERTICAL ? mContentHeight : mContentWidth;
			if (mPosition == LEFT || mPosition == TOP) {
				delta = -delta;
			}
			if (mOrientation == VERTICAL) {
				canvas.translate(0, delta);
			} else {
				canvas.translate(delta, 0);
			}
		}
		if (mState == State.TRACKING 
				|| mState == State.FLYING
				|| mState == State.CLICK) {
			canvas.translate(mTrackX, mTrackY);
		}
		super.dispatchDraw(canvas);
	}

	private float ensureRange(float v, int min, int max) {
		v = Math.max(v, min);
		v = Math.min(v, max);
		return v;
	}

	OnTouchListener touchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			if (mAnimating) {
				// we are animating
				return true;
			}

			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				if (mBringToFront) {
					bringToFront();
				}
			}

			if (!mGestureDetector.onTouchEvent(event)) {
				if (action == MotionEvent.ACTION_UP) {
					// tup up after scrolling
					
					long now = SystemClock.uptimeMillis();
					mAnimationLastTime = now;
					mCurrentAnimationTime = now + ANIMATION_FRAME_DURATION;
					mHandler.removeMessages(MSG_ANIMATE);
					mHandler.removeMessages(MSG_PREPARE_ANIMATE);
					mHandler.sendMessageAtTime(
							mHandler.obtainMessage(MSG_PREPARE_ANIMATE),
							mCurrentAnimationTime);
					Log.e(TAG, "Delivered animation message");
				}
			}
			return false;
		}
	};

	protected boolean initChange() {
		if (mState != State.READY) {
			// we are animating or just about to animate
			return false;
		}
		mState = State.ABOUT_TO_ANIMATE;
		mIsShrinking = isOpen();
		if (!mIsShrinking) {
			// this could make flicker so we test mState in dispatchDraw()
			// to see if is equal to ABOUT_TO_ANIMATE
			if (mContent != null) {
				// Setting visibility here must be done without posting to the
				// handler to synchronize view state
				mContent.setVisibility(VISIBLE);
				mContentVisibility = VISIBLE;
			}
		}
		return true;
	}
	
	private final Runnable mPostProcessRunnable = new Runnable() {

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			Log.e(TAG, "Current state: " + getState().name());
			if (getState() != State.READY) {
				return;
			}
			
			if (mIsShrinking && mClosedHandle != null) {
				mHandle.setBackgroundDrawable(mClosedHandle);
			} else if (!mIsShrinking && mOpenedHandle != null) {
				mHandle.setBackgroundDrawable(mOpenedHandle);
			}
			// invoke listener if any
			if (panelListener != null) {
				if (!isOpen()) {
					panelListener.onPanelClosed(DrawerPanel.this);
				} else {
					panelListener.onPanelOpened(DrawerPanel.this);
				}
			}
			Log.e(TAG, "The panel is ready with mIsShrinking = " + mIsShrinking);
		}
		
	};

	private void postProcess() {
		if (getState() != State.READY) {
			return;
		}
		
		mHandler.post(mPostProcessRunnable);
	}

	class PanelOnGestureListener implements OnGestureListener {
		float scrollY;
		float scrollX;

		@Override
		public boolean onDown(MotionEvent e) {
			if (Build.DEBUG) {
				Log.e(TAG, "Touch down...");
			}
			
			scrollX = scrollY = 0;
			lastRawX = curRawX = lastRawY = curRawY = -1;
			lastEventTime = curEventTime = -1;
			initChange();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (Build.DEBUG) {
				Log.e(TAG, "Flinged...");
			}
			setState(State.FLYING);

			float velocityX2, velocityY2;
			if (lastRawX == -1 && lastRawY == -1) 
			{
				velocityX2 = (curRawX - e1.getRawX())
						/ (curEventTime - e1.getEventTime()) * 1000; // px/s
				velocityY2 = (curRawY - e1.getRawY())
						/ (curEventTime - e1.getEventTime()) * 1000;
			} else {
				velocityX2 = (curRawX - lastRawX)
						/ (curEventTime - lastEventTime) * 1000;
				velocityY2 = (curRawY - lastRawY)
						/ (curEventTime - lastEventTime) * 1000;
			}

			mVelocity = mOrientation == VERTICAL ? velocityY2 : velocityX2;

			if (Math.abs(mVelocity) > 50) {
				if (mVelocity > 0) {
					mAnimatedAcceleration = mMaximumAcceleration;
				} else {
					mAnimatedAcceleration = -mMaximumAcceleration;
				}

				long now = SystemClock.uptimeMillis();
				mAnimationLastTime = now;
				mCurrentAnimationTime = now + ANIMATION_FRAME_DURATION;
				mAnimating = true;
				mHandler.removeMessages(MSG_ANIMATE);
				mHandler.removeMessages(MSG_PREPARE_ANIMATE);
				mHandler.sendMessageAtTime(
						mHandler.obtainMessage(MSG_ANIMATE),
						mCurrentAnimationTime);
				return true;
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// not used
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if (Build.DEBUG) {
				Log.e(TAG, "Scrolled...");
			}
			setState(State.TRACKING);
			
			float tmpY = 0, tmpX = 0;
			if (mOrientation == VERTICAL) {
				scrollY -= distanceY;
				if (mPosition == TOP) {
					tmpY = ensureRange(scrollY, -mContentHeight, 0);
				} else {
					tmpY = ensureRange(scrollY, 0, mContentHeight);
				}
			} else {
				scrollX -= distanceX;
				if (mPosition == LEFT) {
					tmpX = ensureRange(scrollX, -mContentWidth, 0);
				} else {
					tmpX = ensureRange(scrollX, 0, mContentWidth);
				}
			}

			if (tmpX != mTrackX || tmpY != mTrackY) {
				mTrackX = tmpX;
				mTrackY = tmpY;
			}
			invalidate();

			lastRawX = curRawX;
			lastRawY = curRawY;
			lastEventTime = curEventTime;
			curRawX = e2.getRawX();
			curRawY = e2.getRawY();
			curEventTime = e2.getEventTime();
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// not used
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// not used
			setState(State.CLICK);
			mIsShrinking = isOpen();
			return false;
		}
	}

	private void prepareAnimation() {
		if (Build.DEBUG) {
			Log.e(TAG, "Preparing animation with state: " + getState().name());
		}
		
		switch (mPosition) {
		case LEFT:
			if (mIsShrinking) {
				mVelocity = -mMaximumMajorVelocity;
				mAnimatedAcceleration = -mMaximumAcceleration;

			} else {
				mVelocity = mMaximumMajorVelocity;
				mAnimatedAcceleration = mMaximumAcceleration;
				if (mTrackX == 0 && mState == State.ABOUT_TO_ANIMATE) {
					mTrackX = -mContentWidth;
				}
			}
			break;
		case RIGHT:
			if (mIsShrinking) {
				mVelocity = mMaximumMajorVelocity;
				mAnimatedAcceleration = mMaximumAcceleration;
			} else {
				mVelocity = -mMaximumMajorVelocity;
				mAnimatedAcceleration = -mMaximumAcceleration;

				if (mTrackX == 0 && mState == State.ABOUT_TO_ANIMATE) {
					mTrackX = mContentWidth;
				}
			}
			break;
		case TOP:
			if (mIsShrinking) {
				mVelocity = -mMaximumMajorVelocity;
				mAnimatedAcceleration = -mMaximumAcceleration;
			} else {
				mVelocity = mMaximumMajorVelocity;
				mAnimatedAcceleration = mMaximumAcceleration;

				if (mTrackX == 0 && mState == State.ABOUT_TO_ANIMATE) {
					mTrackY = -mContentHeight;
				}
			}
			break;
		case BOTTOM:
			if (mIsShrinking) {
				mVelocity = mMaximumMajorVelocity;
				mAnimatedAcceleration = mMaximumAcceleration;
			} else {
				mVelocity = -mMaximumMajorVelocity;
				mAnimatedAcceleration = -mMaximumAcceleration;

				if (mTrackX == 0 && mState == State.ABOUT_TO_ANIMATE) {
					mTrackY = mContentHeight;
				}
			}
			break;
		}

		if (mState == State.TRACKING) {
			if (mIsShrinking) {
				if ((mOrientation == VERTICAL && Math.abs(mTrackY) < mContentHeight / 2)
						|| (mOrientation == HORIZONTAL && Math.abs(mTrackX) < mContentWidth / 2)) {
					mVelocity = -mVelocity;
					mAnimatedAcceleration = -mAnimatedAcceleration;
					mIsShrinking = !mIsShrinking;
				}
			} else {
				if ((mOrientation == VERTICAL && Math.abs(mTrackY) > mContentHeight / 2)
						|| (mOrientation == HORIZONTAL && Math.abs(mTrackX) > mContentWidth / 2)) {
					mVelocity = -mVelocity;
					mAnimatedAcceleration = -mAnimatedAcceleration;
					mIsShrinking = !mIsShrinking;
				}
			}
		}
		
		if (mState != State.FLYING && mState != State.TRACKING) {
			mState = State.CLICK;
		}
		mAnimating = true;
	}
	
	private void setContentVisibility(final int visibility) {
		mHandler.postAtFrontOfQueue(new Runnable() {

			@Override
			public void run() {
				if (Build.DEBUG) {
					Log.d(TAG, "Setting visibility...");
				}
				mContent.setVisibility(visibility);
				mContentVisibility = visibility;
			}
			
		});
	}

	private void doAnimation() {
		if (mAnimating) {
			long now = SystemClock.uptimeMillis();
			float t = (now - mAnimationLastTime) / 1000.0f; // ms -> s
			final float v = mVelocity; // px/s
			final float a = mAnimatedAcceleration; // px/s/s
			mVelocity = v + (a * t); // px/s
			mAnimationLastTime = now;

			switch (mPosition) {
			case LEFT:
				mTrackX = mTrackX + (v * t) + (0.5f * a * t * t); // px
				if (mTrackX >= 0) {
					mTrackX = 0;
					setState(State.READY);
					mAnimating = false;
				} else if (mTrackX <= -mContentWidth) {
					mTrackX = -mContentWidth;
					setContentVisibility(GONE);
					setState(State.READY);
					mAnimating = false;
				}
				break;
			case RIGHT:
				mTrackX = mTrackX + (v * t) + (0.5f * a * t * t);
				if (mTrackX <= 0) {
					mTrackX = 0;
					setState(State.READY);
					mAnimating = false;
				} else if (mTrackX >= mContentWidth) {
					mTrackX = mContentWidth;
					setContentVisibility(GONE);
					setState(State.READY);
					mAnimating = false;
				}
				break;
			case TOP:
				mTrackY = mTrackY + (v * t) + (0.5f * a * t * t);
				if (mTrackY >= 0) {
					mTrackY = 0;
					setState(State.READY);
					mAnimating = false;
				} else if (mTrackY <= -mContentHeight) {
					mTrackY = -mContentHeight;
					setContentVisibility(GONE);
					setState(State.READY);
					mAnimating = false;
				}
				break;
			case BOTTOM:
				mTrackY = mTrackY + (v * t) + (0.5f * a * t * t);
				if (mTrackY <= 0) {
					mTrackY = 0;
					setState(State.READY);
					mAnimating = false;
				} else if (mTrackY >= mContentHeight) {
					mTrackY = mContentHeight;
					setContentVisibility(GONE);
					setState(State.READY);
					mAnimating = false;
				}
				break;
			}
			postInvalidate();

			if (!mAnimating) {
				postProcess();
				return;
			}
			mCurrentAnimationTime += ANIMATION_FRAME_DURATION - (SystemClock.uptimeMillis() - now);
			mHandler.sendMessageAtTime(
					mHandler.obtainMessage(MSG_ANIMATE),
					mCurrentAnimationTime);

		}
	}
	
	private final Object mLock = new Object();
	private void setState(State newState) {
		synchronized (mLock) {
			mState = newState;
		}
	}
	
	private State getState() {
		synchronized (mLock) {
			return mState;
		}
	}

	private static class SlidingHandler extends Handler {
		
		private WeakReference<DrawerPanel> ref;
		private final ExecutorService executor = Executors.newSingleThreadExecutor();
		private Future<?> future;
		
		private Runnable animateRunnable = new Runnable() {

			@Override
			public void run() {
				DrawerPanel panel = ref.get();
				if (panel != null) {
					panel.doAnimation();
				}
			}
			
		};
		
		private Runnable prepareAnimateRunnable = new Runnable() {

			@Override
			public void run() {
				DrawerPanel panel = ref.get();
				if (panel != null) {
					panel.prepareAnimation();
					panel.doAnimation();
				}
			}
			
		};
		
		private SlidingHandler(DrawerPanel p) {
			ref = new WeakReference<DrawerPanel>(p);
		}
		
		public void handleMessage(Message m) {
			
			if (future != null && !(future.isDone() || future.isCancelled())) {
				// We can skip a frame if the rendering of the previous frame
				// has not yet completed and there are more messages to render a
				// frame
				if (m.what == MSG_ANIMATE && hasMessages(MSG_ANIMATE)) {
					Log.e(TAG, "Skipped a frame...");
					return;
				}
				
				// Stop the previous rendering message
				if (m.what == MSG_PREPARE_ANIMATE) {
					Log.e(TAG, "Cancelling a previous call to draw frame...");
					future.cancel(true);
					future = null;
				}
			}
			
			switch (m.what) {
			case MSG_ANIMATE:
				future = executor.submit(animateRunnable);
				break;
			case MSG_PREPARE_ANIMATE:
				future = executor.submit(prepareAnimateRunnable);
				break;
			}
		}
	}
}
