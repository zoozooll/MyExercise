package com.mogoo.components.ad;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 *类说明：双行，可滑动,下面显示当前页及页数的小圆点
 *@author csq
 */
public class DoubleRowSlideButtomView extends View 
{
	private Paint mCurrentDotPaint;   //当前圆点画笔
	private Paint mOtherDotPaint;     //其他圆点画笔
	
	private int pages = 0;
	private int currentPage = 0;
	
	private int height,width,dotLayoutWidth;
	private float radius;
	
	private HashMap<Integer, centerInfo> centers = new HashMap<Integer, centerInfo>();
	
	private MogooLayoutParent adView;
	
	public DoubleRowSlideButtomView(Context context) 
	{
		this(context,null);
	}
	
	public DoubleRowSlideButtomView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		
		mCurrentDotPaint = new Paint();
		mCurrentDotPaint.setColor(0xff4a6a9d);
		mCurrentDotPaint.setAntiAlias(true);
		
		mOtherDotPaint = new Paint();
		mOtherDotPaint.setColor(0xff93a4b8);
		mOtherDotPaint.setAntiAlias(true);
	}
	
	/**
	 * 设置圆点直径
	 * @param diameter 直径
	 */
	public void setDotDiameter(float diameter) 
	{
		this.radius = diameter/2;
	}
	
	/**
	 * 设置当前圆点颜色
	 * @param color 颜色值如0xff93a4b8
	 */
	public void setCurrentDotColor(int color)
	{
		mCurrentDotPaint.setColor(color);
	}
	
	/**
	 * 设置圆点父视图宽度，用来调整圆点间隙，>圆点直径
	 * @param width
	 */
	public void setDotLayoutWidth(int width)
	{
		this.dotLayoutWidth = width;
	}
	
	/**
	 * 设置其他圆点颜色
	 * @param color 颜色值如0xff93a4b8
	 */
	public void setOtherDotColor(int color)
	{
		mOtherDotPaint.setColor(color);
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		// TODO Auto-generated method stub
		if(height<1)
		{
			height = this.getHeight();
			width = this.getWidth();
			if(dotLayoutWidth <= 0) {
				dotLayoutWidth = (int) (1.2*height);
			}
			if(radius <= 0) {
				radius = (float) (0.3*height);
			}
		}
		
		
		int y = height/2;
		for(int i = 0; i < pages; ++i)
		{
			int x = width/2-pages*dotLayoutWidth/2+dotLayoutWidth/2+i*dotLayoutWidth;
			if(i==currentPage)
			{
				canvas.drawCircle(x, y, radius, mCurrentDotPaint);
			}
			else
			{
				canvas.drawCircle(x, y, radius, mOtherDotPaint);
			}
			
			if(!centers.containsKey(i))
			{
				centers.put(i, new centerInfo(x, y));
			}
		}
		
		super.onDraw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		for(int i = 0; i < pages; ++i)
		{
			if(centers.get(i)!=null && centers.get(i).containPosition(event.getX(), event.getY()))
			{
				Log.e("", "====== touch position = "+i);
				if(adView!=null && adView instanceof DoubleRowSlideLayout)
				{
					((DoubleRowSlideLayout)adView).snapToScreen(i);
					((DoubleRowSlideLayout)adView).setmTouchState(DoubleRowSlideLayout.TOUCH_STATE_REST);
				}
				break;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	public void setPages(int pages) 
	{
		this.pages = pages;
	}
	
	/**
	 * 设置当前页数并更新视图
	 */
	public void invalidateView(int currentPage) 
	{
		this.currentPage = currentPage;
		this.invalidate();
	}
	
	/**
	 * @author csq 圆点中心点信息
	 */
	class centerInfo
	{
		private int centerX = 0;
		private int centerY = 0;
		
		public centerInfo(int centerX, int centerY) 
		{
			super();
			this.centerX = centerX;
			this.centerY = centerY;
		}
		
		public boolean containPosition(float f,float g)
		{
			if(Math.abs(f-centerX)<radius && Math.abs(g-centerY)<radius)
			{
				return true;
			}
			return false;
		}
		
	}
	
	

	public MogooLayoutParent getAdView() {
		return adView;
	}

	public void setAdView(MogooLayoutParent adView) {
		this.adView = adView;
	}

}
