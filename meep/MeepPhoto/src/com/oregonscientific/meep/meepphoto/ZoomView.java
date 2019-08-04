package com.oregonscientific.meep.meepphoto;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ZoomView extends ImageView {
	
	private static final String TAG = "ZoomView";

	private static float MIN_ZOOM = 0.5f;
	private static float MAX_ZOOM = 5f;
	float translateStartX, translateStartY;
	float translateX, translateY;
	float fitWidth, fitHeight;

	float mExtraX = 0;
	float mExtraY = 0;

	boolean isDraggable = false;
	boolean isRight = false;
	boolean isLeft = false;

	boolean expand = false;

	float scaleFactor;
	float zoomFactor = 1.f;

	private ScaleGestureDetector mScaleDetector;
	private GestureDetector mDetector;
	private OnPageScaleListener externalScaleListener;

	interface ZoomViewListener {
		public abstract void onZoomViewFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
	}

	private ZoomViewListener onZoomViewListener = null;

	public void setOnZoomViewListener(ZoomViewListener zoomViewListener) {
		onZoomViewListener = zoomViewListener;
	}

	public ZoomView(Context context) {
		super(context);
		mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		mDetector = new GestureDetector(getContext(), new DoubleTapListener());
	}

	public ZoomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		mDetector = new GestureDetector(getContext(), new DoubleTapListener());
	}

	public ZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
		mDetector = new GestureDetector(getContext(), new DoubleTapListener());
	}
	
	/**
	 * the first state value of ZoomView;
	 */
	public void initZoom() {
		zoomFactor = 1;
		translateX = translateStartX;
		translateY = translateStartY;

		isDraggable = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mScaleDetector.onTouchEvent(event);
		mDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d(TAG, "onTouchEvent ACTION_UP");
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {

		try {
			canvas.save();

			Matrix m = new Matrix();

			m.postScale(zoomFactor / scaleFactor, zoomFactor / scaleFactor);

			if (expand == true)
				if (zoomFactor < 1) {
					if (zoomFactor < 0.98) {
						zoomFactor += 0.02;
						if (zoomFactor < 0.75)
							zoomFactor += 0.05;
						float missedX = ((800 - fitWidth * zoomFactor) / 2) - translateX;
						if (missedX != 0) {
							Log.d("auto", "auto");
							if (Math.abs(missedX) > 5) {
								float diff = (missedX / 5);
								translateX += diff;
							}
						}
						float missedY = ((450 - fitHeight * zoomFactor) / 2) - translateY;
						if (missedY != 0) {
							if (Math.abs(missedY) > 5) {
								float diff = (missedY / 5);
								translateY += diff;
							}
							translateY -= 5;
						}
					} else {
						zoomFactor = 1;
						translateY = ((450 - fitHeight) / 2 - 1);
						translateX = ((800 - fitWidth) / 2);
					}
					isDraggable = false;
				}
			if (BuildConfig.DEBUG) {
				Log.i("onDraw", "zoomFactor: " + zoomFactor);
				Log.i("onDraw", "translateX: " + translateX);
				Log.i("onDraw", "translateStartX: " + translateStartX);
				Log.i("onDraw", "translateY: " + translateY);
				Log.i("onDraw", "translateStartY: " + translateStartY);
				
				
			}

			bound();

//			if (translateY < 0.01) {
//				translateY = 0;
//			}
			if (Double.isNaN(zoomFactor)) {
				zoomFactor = 1;
			}
			
			m.postTranslate(translateX, translateY);

			setImageMatrix(m);
			setScaleType(ScaleType.MATRIX);

			canvas.restore();
		}

		catch (Exception ex) {
			Log.d("ex", "error :" + ex.toString());

		}
		super.onDraw(canvas);
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

		float beforeTransX, beforeTransY, beforeZoom;

		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			// if (detector.getScaleFactor() >= 1 || detector.getScaleFactor() <
			// 0.9){
			zoomFactor *= ((detector.getScaleFactor() - 1) * 0.8) + 1;
			zoomFactor = Math.max(MIN_ZOOM, Math.min(zoomFactor, MAX_ZOOM));
			// }

			if (zoomFactor > 1) {
				if (fitWidth * zoomFactor < 800) {
					translateX = (800 - fitWidth * zoomFactor) / 2;
				} else {
					// translateX = (beforeTransX -
					// detector.getFocusX());//*(1-zoomFactor);

					translateX = detector.getFocusX() - (detector.getFocusX() - beforeTransX) * zoomFactor / beforeZoom;
					translateX = translateX + detector.getFocusX() - mExtraX;

				}
				if (fitHeight * zoomFactor < 450) {
					translateY = (450 - fitHeight * zoomFactor) / 2;
				} else {
					// translateY = (beforeTransY -
					// detector.getFocusY());//*(1-zoomFactor);

					translateY = detector.getFocusY() - (detector.getFocusY() - beforeTransY) * zoomFactor / beforeZoom;
					translateY = translateY + detector.getFocusY() - mExtraY;
				}
			}

			else if (zoomFactor < 1) {
				// if (fitWidth * zoomFactor < 780 && fitHeight * zoomFactor <
				// 468) {
				translateX = detector.getFocusX() - (fitWidth * zoomFactor / 2);
				translateY = detector.getFocusY() - (fitHeight * zoomFactor / 2);
				// translateX = detector.getFocusX() -
				// detector.getFocusX()*zoomFactor;
				// translateY = detector.getFocusY() -
				// detector.getFocusY()*zoomFactor;

				//
				// } else {
				// translateX = (800 - fitWidth * zoomFactor) / 2;
				// translateY = (450 - fitHeight * zoomFactor) / 2;
				// }
			}
		/*	if (BuildConfig.DEBUG) {
				Log.d("onScale", "zoomFactor: " + zoomFactor);
				Log.d("onScale", "fitWidth: " + fitWidth + ", fitHeight: " + fitHeight);
				Log.d("onScale", "fitWidth*zoomFactor: " + fitWidth * zoomFactor);
				Log.d("onScale", "focusX: " + detector.getFocusX() + ", focusY: " + detector.getFocusY());
				Log.d("onScale", "translateX: " + translateX + ", translateY: " + translateY);
				Log.d("onScale", "translateStartX: " + translateStartX + ", translateStartY: " + translateStartY);
			}*/

			invalidate();
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			invalidate();
			if (externalScaleListener != null) {
				externalScaleListener.onScaleBegin();
			}

			expand = false;
			// beforeTransX = translateX - detector.getFocusX();
			// beforeTransY = translateY - detector.getFocusY();
			beforeZoom = zoomFactor;
			beforeTransX = translateX;
			beforeTransY = translateY;

			mExtraX = detector.getFocusX();
			mExtraY = detector.getFocusY();

			return super.onScaleBegin(detector);
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			expand = true;
			mExtraX = 0;
			mExtraY = 0;
			invalidate();
			if (externalScaleListener != null) {
				externalScaleListener.onScaleEnd(zoomFactor);
			}
			super.onScaleEnd(detector);
		}

	}
	
	public void setOnPageScaleListener(OnPageScaleListener onScaleListener) {
        this.externalScaleListener = onScaleListener;
    }

	private class DoubleTapListener implements OnDoubleTapListener, OnGestureListener {

		public boolean onDoubleTap(MotionEvent e) {

			//Log.d("showPress", "guesture onDoubleTap");
			if (zoomFactor > 1) {
				initZoom();

			} else if (zoomFactor <= 1) {

				// beforeX, beforeY: relative touch point before zoom
				// afterX, afterY: relative point after zoom
				float beforeX, beforeY, afterX, afterY;
				if (fitWidth * zoomFactor >= 800 && fitHeight * zoomFactor >= 450) {
					beforeX = Math.abs(translateX - e.getX()) / (fitWidth * zoomFactor);
					beforeY = Math.abs(translateY - e.getY()) / (fitHeight * zoomFactor);
				} else {
					beforeX = (e.getX() - translateStartX) / fitWidth;
					beforeY = (e.getY() - translateStartY) / fitHeight;
				}
				zoomFactor = (float) 4;
				afterX = beforeX * fitWidth * zoomFactor;
				afterY = beforeY * fitHeight * zoomFactor;

				translateX = 400 - afterX;
				translateY = 240 - afterY;

				// Log.d("DoubleTap", "fitWidth: " + fitWidth + ", fitHeight: "
				// + fitHeight);
				// Log.d("DoubleTap", "e.getX: " + e.getX() + ", e.getY: " +
				// e.getY());
				// Log.d("DoubleTap", "translateX: " + translateX +
				// ", translateY: " + translateY);
				// Log.d("DoubleTap", "translateStartX: " + translateStartX +
				// ", translateStartY: " + translateStartY);
				// Log.d("DoubleTap", "zoomFactor: " + zoomFactor);
				// Log.d("DoubleTap", "fitWidth*zoomFactor: " + fitWidth *
				// zoomFactor);

			}
			invalidate();
			//Log.e("DoubleTap", "isLeft: " + isLeft + " ,isRight: " + isRight);
			//.e("DoubleTap", "isDrag: " + isDraggable);
			if (externalScaleListener != null) {
				externalScaleListener.onScaleEnd(zoomFactor);
			}
			return true;
		}

		public boolean onDoubleTapEvent(MotionEvent e) {

			return true;
		}

		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (onZoomViewListener != null) {
				onZoomViewListener.onZoomViewFling(e1, e2, velocityX, velocityY);
			}
			return false;
		}

		public void onLongPress(MotionEvent e) {
		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			// if (isDraggable) {
			if (zoomFactor > 1) {

				// if (fitWidth * zoomFactor < 800) {
				// translateX = (800 - fitWidth * zoomFactor) / 2;
				// } else {
				translateX += -distanceX;
				// }
				// if (fitHeight * zoomFactor < 450) {
				// translateY = (450 - fitHeight * zoomFactor) / 2;
				// } else {
				translateY += -distanceY;
				// }
			/*	Log.e("onScroll", "TranslateX: " + translateX);
				Log.e("onScroll", "TranslateY: " + translateY);
				Log.e("onScroll", "IsLeft: " + isLeft);
				Log.e("onScroll", "IsRight: " + isRight);
				Log.e("onScroll", "IsDrag:" + isDraggable);*/

				if (translateX < (800 - (fitWidth + 100) * zoomFactor)) {
					isRight = true;
				}
				else if (translateX > -100 * zoomFactor) {
					isLeft = true;
				} else {
					isLeft = false;
					isRight = false;
				}

				Log.d("onScroll", "isLeft: " + isLeft + " ,isRight: " + isRight);
				Log.d("onScroll", "isDrag: " + isDraggable);
				Log.d("onScroll", "fitWidth: " + fitWidth);
				/*if (externalScaleListener != null && isLeft ) {
					externalScaleListener.onImageToLeft();
				}
				if (externalScaleListener != null && isRight) {
					externalScaleListener.onImageToRight();
				}*/
			}
			// }

			// Log.d("onScroll", "size: " + fitWidth + ", size: :" + fitHeight);
			// Log.d("onScroll", "translate " + translateX + "/" + translateY);
			
			invalidate();
			return false;
		}

		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
	}

	private void bound() {
		if (zoomFactor >= 1) {
			if (fitWidth * zoomFactor >= 800 && fitHeight * zoomFactor >= 450) {
				translateX = Math.max(800 - fitWidth * zoomFactor, Math.min(translateX, 0));
				translateY = Math.max(450 - fitHeight * zoomFactor, Math.min(translateY, 0));
			} else if (fitWidth * zoomFactor < 800) {
				translateY = Math.max(450 - fitHeight * zoomFactor, Math.min(translateY, 0));
			} else if (fitHeight * zoomFactor < 450) {
				 translateX = Math.max(800 - fitWidth * zoomFactor,Math.min(translateX, 0));
			}
		}
	}

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	public void setMatrix(Matrix m) {
		matrix.set(m);
	}

	public void setSavedMatrix(Matrix m) {
		savedMatrix.set(m);
	}

}
