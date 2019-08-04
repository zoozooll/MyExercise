package com.mogoo.components.ad;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

class ADPanel
{
	static final int a = Color.argb(160, 255, 255, 255);
	static final int b = Color.argb(50, 255, 255, 255);
	static final int c = Color.argb(128, 255, 255, 255);
	static final int d = Color.argb(128, 0, 0, 0);
	static final int e = Color.argb(80, 0, 0, 0);
	static final int f = Color.argb(150, 135, 206, 250);
	static final LinearGradient g = new LinearGradient(0.0F, 0.0F, 0.0F, 19.0F,
			a, b, Shader.TileMode.CLAMP);
	static final LinearGradient h = new LinearGradient(0.0F, 0.0F, 0.0F, 24.0F,
			a, b, Shader.TileMode.CLAMP);
	static final LinearGradient i = new LinearGradient(0.0F, 0.0F, 0.0F, 32.0F,
			a, b, Shader.TileMode.CLAMP);

	static LinearGradient a(int paramInt)
	{
		if (paramInt == 38)
			return g;
		if (paramInt == 48)
			return h;
		return i;
	}

	static Bitmap panelDrawable(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4)
	{
		try
		{
			Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2,
					Bitmap.Config.ARGB_8888);
			Canvas localCanvas = new Canvas(localBitmap);
			Paint localPaint = new Paint();
			drawableBitmap(localCanvas, localPaint, paramInt1, paramInt2,
					paramInt3, paramInt4);
			return localBitmap;
		} catch (Exception localException)
		{
		}
		return null;
	}

	public static void drawableBitmap(Canvas paramCanvas, Paint paramPaint,
			int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	{
		try
		{
			Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2,
					Bitmap.Config.ARGB_8888);
			paramPaint.reset();
			Canvas localCanvas = new Canvas(localBitmap);
			localCanvas.drawColor(paramInt3);
			paramPaint.reset();
			paramPaint.setShader(a(paramInt2));
			Rect localRect = new Rect();
			localRect.top = 0;
			localRect.left = 0;
			localRect.bottom = (paramInt2 / 2);
			localRect.right = paramInt1;
			localCanvas.drawRect(localRect, paramPaint);
			paramPaint.reset();
			paramPaint.setColor(d);
			localCanvas.drawLine(0.0F, 0.0F, paramInt1, 0.0F, paramPaint);
			paramPaint.reset();
			paramPaint.setColor(c);
			localCanvas.drawLine(0.0F, 1.0F, paramInt1, 1.0F, paramPaint);
			paramPaint.reset();
			paramPaint.setColor(e);
			localCanvas.drawLine(0.0F, paramInt2 - 1, paramInt1, paramInt2 - 1,
					paramPaint);
			paramPaint.reset();

			paramPaint.setAlpha(paramInt4);
			paramCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, paramPaint);
			try
			{
				localBitmap.recycle();
			} catch (Exception localException2)
			{
				// F.a(localException2.getMessage(), 190);
			}
		} catch (Exception localException1)
		{
			// F.a(localException1.getMessage(), 191);
		}
	}
}
