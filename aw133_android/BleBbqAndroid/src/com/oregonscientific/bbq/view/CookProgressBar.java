/**
 * 
 */
package com.oregonscientific.bbq.view;

import com.oregonscientific.bbq.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author aaronli
 *
 */
public class CookProgressBar extends View {

	private Rect area;
	
	private Paint paint;
	
	private Bitmap layerBottom;
	
	private Bitmap layerXfer;
	
	private Bitmap layerFront;
	
	private int[] emptyPixles;

	private Canvas xFerCanvas;

	private float sweep;

	private RectF rectF;

	private Rect rect;
	
	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CookProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		Resources res = getResources();
		Options op = new Options();
		op.inPreferredConfig = Config.ARGB_8888;
		layerBottom = BitmapFactory.decodeResource(res, R.drawable.progress_bar_bottom, op);
		layerFront = BitmapFactory.decodeResource(res, R.drawable.progress_ring, op);
		rect = new Rect(0, 0, layerBottom.getWidth(), layerBottom.getHeight());
		rectF = new RectF(rect);
		toShaped(-90, 0);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public CookProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 */
	public CookProgressBar(Context context) {
		this(context, null);
	}
	
	public void setPercential(float percent) {
		if(percent < 0 ) {
			percent = 0;
		} else if (percent > 100) {
			percent = 100f;
		}
		sweep = -360 * (percent/100f);
		toShaped(-90, sweep);
		invalidate(); 
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		
		if (area == null) {
			final int viewWidth = getWidth();
			final int viewHeight = getHeight();
			// Modified by aaronli at Mar 10 2014. Fixed the round acr drawing out of bound.
			final int minWH = Math.min(viewWidth, viewHeight) - 10;
			area = new Rect((viewWidth - minWH) >> 1, (viewHeight - minWH) >> 1, ((viewWidth - minWH) >> 1) +minWH, ((viewHeight - minWH) >> 1)+minWH);
		}
		//canvas.drawRect(area, null);
		//canvas.drawColor(Color.BLUE);
        //canvas.drawArc(area, -90, -180, true, paint);
		canvas.drawBitmap(layerXfer, null, area, paint);
		canvas.drawBitmap(layerFront, null, area, paint);
		canvas.restore();
		
		/*toShaped(-90, sweep) ;
		sweep--;
		if (sweep == -360) {
			sweep = 0;
		}
		invalidate();*/
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		layerBottom.recycle();
		layerXfer.recycle();
		layerFront.recycle();
		super.finalize();
	}

	private  void toShaped(float start, float sweep) {

		if (layerXfer == null || layerXfer.isRecycled()) {
			layerXfer = Bitmap.createBitmap(layerBottom.getWidth(),
					layerBottom.getHeight(), Config.ARGB_8888);
			emptyPixles = new int[layerBottom.getWidth() * layerBottom.getHeight()];
			layerXfer.eraseColor(Color.TRANSPARENT);
			xFerCanvas = new Canvas(layerXfer);
		} else {
			layerXfer.setPixels(emptyPixles, 0, layerBottom.getWidth(), 0, 0, layerBottom.getWidth() , layerBottom.getHeight());
		}

		xFerCanvas.save();
		
		//final float roundPx = pixels;

		paint.setAntiAlias(true);

		//xFerCanvas.drawARGB(0, 0, 0, 0);


		//canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		xFerCanvas.drawArc(rectF, start, sweep, true, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		xFerCanvas.drawBitmap(layerBottom, rect, rectF, paint);
		
		xFerCanvas.restore();
		paint.reset();

	}

	
}
