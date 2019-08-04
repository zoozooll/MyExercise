package com.tcl.manager.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CornerImageView extends ImageView {

	private final float density = getContext().getResources().getDisplayMetrics().density;
	private float roundness;

	public CornerImageView(Context context) {
		super(context);

		init();
	}

	public CornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public CornerImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	
	/**
	 * 获取圆角位图的方法
	 * @param bitmap 需要转化成圆角的位图
	 * @param pixels 圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
//	@Override
//	public void setImageBitmap(Bitmap bm) {
//
//		super.setImageBitmap(toRoundCorner(bm, (int) getRoundness()));
//	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		if( drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			drawable = new BitmapDrawable(getResources(), toRoundCorner(bitmap, (int) getRoundness()));
		}
		super.setImageDrawable(drawable);
		
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		// final Bitmap composedBitmap;
		// final Bitmap originalBitmap;
		// final Canvas composedCanvas;
		// final Canvas originalCanvas;
		// final Paint paint;
		// final int height;
		// final int width;
		//
		// width = getWidth();
		//
		// height = getHeight();
		//
		// try {
		//
		// composedBitmap = Bitmap.createBitmap(width, height,
		// Bitmap.Config.ARGB_8888);
		// originalBitmap = Bitmap.createBitmap(width, height,
		// Bitmap.Config.ARGB_8888);
		//
		// composedCanvas = new Canvas(composedBitmap);
		// originalCanvas = new Canvas(originalBitmap);
		//
		// paint = new Paint();
		// paint.setAntiAlias(true);
		// paint.setColor(Color.BLACK);
		//
		// super.draw(originalCanvas);
		//
		// composedCanvas.drawARGB(0, 0, 0, 0);
		//
		// composedCanvas.drawRoundRect(new RectF(0, 0, width, height),
		// this.roundness, this.roundness, paint);
		//
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		//
		// composedCanvas.drawBitmap(originalBitmap, 0, 0, paint);
		//
		// canvas.drawBitmap(composedBitmap, 0, 0, new Paint());
		// } catch (Exception e) {
		//
		// }
		
//		composedBitmap.recycle();
//		
//		originalBitmap.recycle();
	}

	public float getRoundness() {
		return this.roundness / this.density;
	}

	public void setRoundness(float roundness) {
		this.roundness = roundness * this.density;
	}

	private void init() {
		setRoundness(5);
	}
}