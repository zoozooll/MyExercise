/**
 * 
 */
package com.oregonscientific.bbq.view;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.utils.BbqConfig;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

/**
 * @author aaronli
 *
 */
public class DonenessCoordinate extends View {
	
	private final static String TAG = "DonenessCoordinate";
	private float[] temperatures;
	
	private int viewWidth;
	private int viewHeight;
	
	private float firstX, lastX, firstY, lastY;
	
	private Paint mPaint;
	private LinearGradient colorShader;
	private int[] colors;
	private float[] positions;
	
	private float preX, preY;
	private String temperatureUnit;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DonenessCoordinate(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		/*temperatures = new float[]{
				100f, 115f, 130f, 145f, 155.f,  164.5f, 168.f, 172.f,
				180f, 185f, 190f, 200f, 205f, 230f, 234f,238.5f, 241.5f, 245.f, 249.5f, 253.f,258.f
		};*/
		mPaint = new Paint();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DonenessCoordinate(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 */
	public DonenessCoordinate(Context context) {
		this(context, null);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		viewHeight = bottom - top;
		viewWidth = right - left;
		firstX = getPaddingLeft();
		lastX = viewWidth - getPaddingRight();
		firstY = viewHeight - getPaddingBottom() - 50;
		lastY = getPaddingTop();
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		Log.d(TAG, "width and heidht "+ viewWidth + " "+ viewHeight);
		
		// draw the line at the bottom
		mPaint.reset();
		mPaint.setStrokeWidth(5);
		canvas.drawLine(firstX, firstY, lastX, firstY, mPaint);
		
		// draw the line at 180 F;
		float y = getYfromTemperature(180f);
		mPaint.reset();
		mPaint.setStrokeWidth(4);
		mPaint.setStyle(Paint.Style.STROKE);          
		mPaint.setColor(Color.LTGRAY);          
		Path path = new Path();               
		path.moveTo(firstX, y);          
		path.lineTo(lastX, y);     
		
		PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);          
		mPaint.setPathEffect(effects);          
		canvas.drawPath(path, mPaint);
		mPaint.reset();
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.DKGRAY);
		mPaint.setTextSize(30f);
		if (BbqConfig.TEMPERATURE_UNIT_C.equals(temperatureUnit)) {
			canvas.drawText("100"+getResources().getString(R.string.tempc), firstX, y-15f, mPaint);
		} else if (BbqConfig.TEMPERATURE_UNIT_F.equals(temperatureUnit)) {
			canvas.drawText("180"+getResources().getString(R.string.tempf), firstX, y-15f, mPaint);
		}
		
		// draw line for every 5 minutes and text
		if (temperatures != null) {
			for (int i = 1, size = temperatures.length/10; i <= size;  i++) {
				mPaint.reset();
				mPaint.setStrokeWidth(4);
				mPaint.setStyle(Paint.Style.STROKE);          
				mPaint.setColor(Color.LTGRAY); 
				float x = getXfromMinute(i * 10);
				path.reset();
				path.moveTo(x, firstY);
				path.lineTo(x, lastY);
				canvas.drawPath(path, mPaint);
				// draw text
				mPaint.reset();
				mPaint.setTextAlign(Align.CENTER);
				mPaint.setColor(Color.DKGRAY);
				mPaint.setTextSize(30f);
				canvas.drawText((i * 5)+"m", x, firstY +5 +30f, mPaint);
			}
		}
		canvas.drawText("0m", firstX, firstY +5 +30f, mPaint);

		
		// draw line for every temperatue
		path.reset();
		mPaint.reset();
		mPaint.setXfermode(null);
		if (colors != null && positions != null) {
			//mPaint.setAlpha(0xff);
//			if (colorShader ==null) {
				/*colorShader = new LinearGradient(firstX, firstY, firstX, lastY, 
						new int[]{Color.RED, Color.BLUE, Color.CYAN, Color.BLACK, Color.DKGRAY,
						Color.GREEN, Color.LTGRAY, Color.WHITE, Color.YELLOW, Color.RED},
						new float[] {0.0f, 0.1f, 0.2f, 0.35f, 0.48f
							, 0.55f, 0.60f, 0.75f, 0.85f,1.0f},
						TileMode.CLAMP);*/
				colorShader = new LinearGradient(firstX, firstY, firstX, lastY, 
						colors,
						positions,
						TileMode.CLAMP);
//			} 
		} else {
			mPaint.setColor(getResources().getColor(android.R.color.holo_orange_light));
			
		}
		
		mPaint.setShader(colorShader);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(25f);
		if (temperatures != null) {
			for (int i = 0, size = temperatures.length; i < size; i++) {
				y = getYfromTemperature(temperatures[i]);
				if (i == 0 ) {
					path.moveTo(firstX, y);
					preX = firstX;
					preY = y;
				} else if (i == size -1) {
					float x = getXfromMinute(i);
					path.lineTo(x, y);
				} else {
					float x = getXfromMinute(i);
//				Log.d(TAG, "line to "+ x +" "+y);
					path.quadTo(preX, preY, (preX + x)* 0.5f, (preY + y) *0.5f);
					preX = x;
					preY = y;
				}
			}
		}
		canvas.drawPath(path, mPaint);
	}
	
	public void setTemperatures(float[] temperatures) {
		this.temperatures = temperatures;
		
	}
	
	/**
	 * @deprecated
	 * @param donenessColors
	 * @param endColor
	 */
	public void setDonenessColors(SparseArray<Float> donenessColors, int endColor) {
		colors = new int[donenessColors.size() + 2];
		positions = new float[donenessColors.size() + 2];
		colors[0] = donenessColors.keyAt(0);
		positions[0] = 0f;
		for (int i = 0, size = donenessColors.size(); i < size; i++) {
			Log.d(TAG, "donenessColors at "+i+" "+donenessColors.keyAt(i) +":"+ donenessColors.valueAt(i));
			colors[i + 1] = donenessColors.keyAt(i);
			positions[i + 1] = donenessColors.valueAt(i) / 375f;
		}
		colors[colors.length - 1] = endColor;
		positions[positions.length - 1] = 1.f;
	}
	
	/**
	 * @param donenessColors
	 * @param endColor
	 */
	public void setDonenessColors(int[] colors, float[] temperatures) {
		// modified by aaronli Mar 20 2014. showing if the color is null
		this.colors = colors;
		if (temperatures != null) {
			positions = new float[temperatures.length +2];
			positions[0] = 0f;
			for (int i = 0, size = temperatures.length; i < size; i++) {
				positions[i + 1] = temperatures[i] / 375f;
			}
			positions[positions.length - 1] = 1.f;
		} else {
			positions = null;
		}
	}
	
	
	private float getYfromTemperature(float tem) {
		return (lastY + (375.f - tem) * (firstY - lastY) / 375f);
	}
	
	private float getDistanceBetweenMinute() {
		if(temperatures.length == 1) {
			return 0;
		}
		return (lastX - firstX)/ (float)(temperatures.length - 1);
	}
	
	private float getXfromMinute(int minute) {
		float dis = getDistanceBetweenMinute();
		return firstX + (minute) * dis;
	}

	/**
	 * @param temperatureUnit the temperatureUnit to set
	 */
	public void setTemperatureUnit(String temperatureUnit) {
		this.temperatureUnit = temperatureUnit;
	}

}
