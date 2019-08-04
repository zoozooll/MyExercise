package com.aaron.filter.shade;

import com.aaron.filter.R;
import com.aaron.filter.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ShadeView extends View {
	private SweepGradient mSweepGradient;
	private Paint mPaint = new Paint();

    public ShadeView(Context context) {
        super(context);
        init(null, 0);
    }

    public ShadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ShadeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        
        /* 构建SweepGradient对象 */
		mSweepGradient = new SweepGradient(240,400,new int[]{Color.GREEN,Color.RED,Color.BLUE,Color.WHITE},null);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制梯度渐变
        mPaint .setShader(mSweepGradient);
		canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

}
