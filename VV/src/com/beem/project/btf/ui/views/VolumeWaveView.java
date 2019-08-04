package com.beem.project.btf.ui.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author le yang
 * @category 绘制音量波形图
 */
public class VolumeWaveView extends View {
	private Paint mPaint = new Paint();
	private boolean isRun = false;
	private int angle = 0;// 角度偏移类似偏移量
	private float[] mPoints;
	private float amplitude = 50f;// 最大振幅
	private Rect mRect = new Rect();
	private ArrayList<Float> amplitudeList = new ArrayList<Float>();

	public VolumeWaveView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public VolumeWaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public VolumeWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		// handler.sendEmptyMessage(1);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		for (int i = 0; i < 8; i++) {
			amplitudeList.add(0.0f);
		}
		// 设置背景透明
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int height = getHeight();
		int width = getWidth();
		// 设置画笔
		float paintwidth = (width / 16) / 4;
		mPaint.setStrokeWidth(paintwidth);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.rgb(255, 255, 255));
		mPoints = new float[64];// 存储16根线的坐标,一根线需要4个值来表示
		mRect.set(0, 0, width, height);
		int xOrdinate = 16;
		for (int i = 0; i < 16; i++) {
			// 记录x1坐标,加8f是因为画笔有8f粗
			mPoints[i * 4] = (mRect.width() / xOrdinate) * i + paintwidth;
			// 记录x2坐标
			mPoints[i * 4 + 2] = (mRect.width() / xOrdinate) * i + paintwidth;
			// 记录y1和y2坐标,加5f是保证最短的线还能看到
			// 0~7反着读取,8~16顺着读取数据
			if (i >= 0 && i < 8) {
				mPoints[i * 4 + 1] = ((mRect.height()) / 2 + 5f)
						+ amplitudeList.get(7 - i);
				mPoints[i * 4 + 3] = ((mRect.height()) / 2 - 5f)
						- amplitudeList.get(7 - i);
			} else if (i >= 8 && i < 16) {
				mPoints[i * 4 + 1] = ((mRect.height()) / 2 + 5f)
						+ amplitudeList.get(i - 8);
				mPoints[i * 4 + 3] = ((mRect.height()) / 2 - 5f)
						- amplitudeList.get(i - 8);
			}
		}
		canvas.drawLines(mPoints, mPaint);
	}
	// 提供一个方法来修改偏移和振幅
	public void updataAM(int angle, float amplitude) {
		this.angle = angle;
		this.amplitude = amplitude;
		// 用一个arraylist记录下来,把新弄进来的数据放到首位,移出最后一条数据
		amplitudeList.remove(7);
		amplitudeList.add(0, amplitude);
		invalidate();
	}
}
