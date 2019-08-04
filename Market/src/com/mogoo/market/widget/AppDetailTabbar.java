package com.mogoo.market.widget;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mogoo.market.R;

/**
 * @author csq 软件详情“软件简介”、”用户评论“切换卡
 */
public class AppDetailTabbar extends View 
{
	private int tabIndex = 0;
	private HashMap<Integer, DetailTabMember> members = new HashMap<Integer, DetailTabMember>();
	private int currentTab = 0;

	private Paint mbgPaint;
	private Paint mTextPaint;

	private int textSize;
	private int space;

	private ViewGroup container = null;
	
	private Bitmap buttonLeftNormal = null;
	private Bitmap buttonLeftPressed = null;
	private Bitmap buttonMidNormal = null;
	private Bitmap buttonMidPressed = null;
	private Bitmap buttonRightNormal = null;
	private Bitmap buttonRightPressed = null;

	public AppDetailTabbar(Context context) {
		this(context, null);
	}

	public AppDetailTabbar(Context context, AttributeSet attrs) 
	{
		super(context, attrs);

		// 字体大小、字体距底端的距离在xml里面获得
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.appdetail_tabbar);
		textSize = ta.getDimensionPixelSize(R.styleable.appdetail_tabbar_textSize, 12);
		space = ta.getDimensionPixelSize(R.styleable.appdetail_tabbar_space,5);

		mbgPaint = new Paint();
		mTextPaint = new Paint();

		mbgPaint.setStyle(Paint.Style.FILL);
		mbgPaint.setColor(0xFFF0F0F0);
		mbgPaint.setAntiAlias(true);

		mTextPaint.setTextAlign(Align.CENTER);
		mTextPaint.setTextSize(textSize);
		mTextPaint.setColor(0xFF264766);
		mTextPaint.setAntiAlias(true);

		currentTab = 0;
		
		Resources rs = context.getResources();
		buttonLeftNormal = BitmapFactory.decodeResource(rs, R.drawable.appdetail_top_button_left_normal);
		buttonLeftPressed = BitmapFactory.decodeResource(rs, R.drawable.appdetail_top_button_left_preaaes);
		buttonRightNormal = BitmapFactory.decodeResource(rs, R.drawable.appdetail_top_button_right_normal);
		buttonRightPressed = BitmapFactory.decodeResource(rs, R.drawable.appdetail_top_button_right_pressed);
		ta.recycle();
	}

	public void setContainer(ViewGroup container) {
		this.container = container;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Rect r = new Rect();
		this.getDrawingRect(r);

		int singleTabWidth = r.right / (members.size() != 0 ? members.size() : 1);

		canvas.drawRect(r, mbgPaint);
		int size = members.size();
		for (int i = 0; i < size; i++) 
		{
			DetailTabMember tabMember = members.get(i);

			FontMetrics fm = mTextPaint.getFontMetrics();
			int tabTextHeight = (int) (fm.descent - fm.ascent);
			
			int tabTextCenterX = singleTabWidth * i + singleTabWidth / 2;
			int tabTextCenterY = (tabTextHeight+r.height())/2-2;

			Rect src1 = new Rect();
			Rect src2 = new Rect();
			
			if (currentTab == i) 
			{
				if(i==0)
				{
					src1.left = 0;
					src1.right = buttonLeftPressed.getWidth()-2;
					src1.top = 0;
					src1.bottom = buttonLeftPressed.getHeight();
					Rect dst1 = new Rect();
					dst1.left=r.left+space;
					dst1.right=r.left+space+buttonLeftPressed.getWidth()-2;
					dst1.top=r.top+space;
					dst1.bottom=r.bottom-space;
					canvas.drawBitmap(buttonLeftPressed, src1, dst1, null);
					
					
					src2.left = buttonLeftPressed.getWidth()-4;
					src2.right = buttonLeftPressed.getWidth()-2;
					src2.top = 0;
					src2.bottom = buttonLeftPressed.getHeight();
					Rect dst2 = new Rect();
					dst2.left=r.left+space+buttonLeftPressed.getWidth()-2;
					dst2.right=singleTabWidth;
					dst2.top=r.top+space;
					dst2.bottom=r.bottom-space;
					canvas.drawBitmap(buttonLeftPressed, src2, dst2, null);
				}
				else if(i==size-1)
				{
					src1.left = 2;
					src1.right = buttonRightPressed.getWidth();
					src1.top = 0;
					src1.bottom = buttonRightPressed.getHeight();
					Rect dst1 = new Rect();
					dst1.left=r.right-space-buttonRightPressed.getWidth()+2;
					dst1.right=r.right-space;
					dst1.top=r.top+space;
					dst1.bottom=r.bottom-space;
					canvas.drawBitmap(buttonRightPressed, src1, dst1, null);
					
					
					src2.left = 2;
					src2.right = 4;
					src2.top = 0;
					src2.bottom = buttonRightPressed.getHeight();
					Rect dst2 = new Rect();
					dst2.left=singleTabWidth*i;
					dst2.right=r.right-space-buttonRightPressed.getWidth()+2;
					dst2.top=r.top+space;
					dst2.bottom=r.bottom-space;
					canvas.drawBitmap(buttonRightPressed, src2, dst2, null);
				}
				else
				{
					
				}
			}
			else 
			{
				if(i==0)
				{
					src1.left = 0;
					src1.right = buttonLeftNormal.getWidth()-2;
					src1.top = 0;
					src1.bottom = buttonLeftNormal.getHeight();
					Rect dst1 = new Rect();
					dst1.left=r.left+space;
					dst1.right=r.left+space+buttonLeftNormal.getWidth()-2;
					dst1.top=r.top+space;
					dst1.bottom=r.bottom-space;
					canvas.drawBitmap(buttonLeftNormal, src1, dst1, null);
					
					
					src2.left = buttonLeftNormal.getWidth()-4;
					src2.right = buttonLeftNormal.getWidth()-2;
					src2.top = 0;
					src2.bottom = buttonLeftNormal.getHeight();
					Rect dst2 = new Rect();
					dst2.left=r.left+space+buttonLeftNormal.getWidth()-2;
					dst2.right=singleTabWidth;
					dst2.top=r.top+space;
					dst2.bottom=r.bottom-space;
					canvas.drawBitmap(buttonLeftNormal, src2, dst2, null);
				}
				else if(i==size-1)
				{
					src1.left = 2;
					src1.right = buttonRightNormal.getWidth();
					src1.top = 0;
					src1.bottom = buttonRightNormal.getHeight();
					Rect dst1 = new Rect();
					dst1.left=r.right-space-buttonRightNormal.getWidth()+2;
					dst1.right=r.right-space;
					dst1.top=r.top+space;
					dst1.bottom=r.bottom-space;
					canvas.drawBitmap(buttonRightNormal, src1, dst1, null);
					
					
					src2.left = 2;
					src2.right = 4;
					src2.top = 0;
					src2.bottom = buttonRightNormal.getHeight();
					Rect dst2 = new Rect();
					dst2.left=singleTabWidth*i;
					dst2.right=r.right-space-buttonRightNormal.getWidth()+2;
					dst2.top=r.top+space;
					dst2.bottom=r.bottom-space;
					canvas.drawBitmap(buttonRightNormal, src2, dst2, null);
				}
				else
				{
					
				}
				
			}
			canvas.drawText(tabMember.mText, tabTextCenterX,tabTextCenterY, mTextPaint);
		}

		setView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Rect r = new Rect();
		this.getDrawingRect(r);
		float singleTabWidth = r.right
				/ (members.size() != 0 ? members.size() : 1);

		int touchTab = (int) ((event.getX() / singleTabWidth) - (event.getX() / singleTabWidth) % 1);

		if (touchTab != currentTab) {
			currentTab = touchTab;
			this.invalidate();
		}

		return super.onTouchEvent(event);
	}

	private void setView() {
		if (container != null) {
			container.removeAllViews();
			View v = members.get(currentTab).getTabView();
			container.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		}
	}

	public void addTab(DetailTabMember member) {
		members.put(tabIndex++, member);
	}

	public static class DetailTabMember {
		protected String mText;
		protected View tabView;

		public DetailTabMember(String mText, View tabView) {
			super();
			this.mText = mText;
			this.tabView = tabView;
		}

		public String getmText() {
			return mText;
		}

		public View getTabView() {
			return tabView;
		}
	}

}
