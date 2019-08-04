package com.tcl.manager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画一个圆形
 * 
 * @author jiaquan.huang
 * 
 */
public class CircularView extends View {
    int radius = 200;// 半径
    private Paint paint = new Paint();
    int backgroundColor = 0;

    public CircularView(Context context, AttributeSet attrs) {
        super(context, attrs);
        backgroundColor = attrs.getAttributeIntValue(android.R.attr.background, Color.BLACK);
        init();
    }

    /**
     * @param context
     */
    public CircularView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(backgroundColor);
    }

    public void setBackground(int colorValue) {
        paint.setColor(colorValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    public void setWidth(int width) {
        this.getLayoutParams().width = width;
    }

    public void setHeight(int height) {
        this.getLayoutParams().height = height;
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(-radius, b - radius / 2, 0, b + radius / 2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);
    }
}
