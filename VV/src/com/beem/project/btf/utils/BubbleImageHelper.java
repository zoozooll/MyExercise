package com.beem.project.btf.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * @author le yang 产生气泡图片
 */
public class BubbleImageHelper {
	private Context context = null;
	private static BubbleImageHelper instance = null;

	private enum SizeType {
		width, height
	}

	public static synchronized BubbleImageHelper getInstance(Context c) {
		if (null == instance) {
			instance = new BubbleImageHelper(c);
		}
		return instance;
	}
	private BubbleImageHelper(Context c) {
		context = c;
	}
	private Bitmap getScaleImage(Bitmap bitmap, float width, float height) {
		if (null == bitmap || width < 0.0f || height < 0.0f) {
			return null;
		}
		Matrix matrix = new Matrix();
		float scaleWidth = width / bitmap.getWidth();
		float scaleHeight = height / bitmap.getHeight();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}
	public Bitmap getScaleImage(float scales, Bitmap bmp) {
		Matrix matrix = new Matrix();
		matrix.setScale(scales, scales);
		Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		return bm;
	}
	/**
	 * 1.计算图片最大宽高 2.缩放到合适大小 3.缩放蒙版到合适大小 4.合成图片
	 */
	public Bitmap getBubbleImageBitmap(Bitmap srcBitmap,
			int backgroundResourceID, boolean isNinePatch) {
		if (null == srcBitmap) {
			return null;
		}
		Bitmap background = null;
		Bitmap newBitmap = null;
		float srcWidth = srcBitmap.getWidth();
		float srcHeight = srcBitmap.getHeight();
		background = BitmapFactory.decodeResource(context.getResources(),
				backgroundResourceID);
		if (null == background) {
			return null;
		}
		try {
			NinePatch mNinePatch = null;
			if (isNinePatch) {
				// 如果是点9图
				mNinePatch = new NinePatch(background,
						background.getNinePatchChunk(), null);
			}
			// 计算图片可以达到的最大宽高
			int maxBitmapWidth = getMaxElementSize(context, SizeType.width);
			int maxBitmapHeight = getMaxElementSize(context, SizeType.height);
			//LogUtils.i("maxBitmapWidth:" + maxBitmapWidth + " maxBitmapHeight:" + maxBitmapHeight + " srcWidth:"
			//					+ srcWidth + " srcHeight:" + srcHeight);
			maxBitmapWidth = maxBitmapHeight = Math.max(maxBitmapWidth,
					maxBitmapHeight);
			// 计算原图缩放
			float scaleRatio = Math.min(maxBitmapWidth / srcWidth,
					maxBitmapHeight / srcHeight);
			int scaleWidth = (int) (srcBitmap.getWidth() * scaleRatio);
			int scaleHeight = (int) (srcBitmap.getHeight() * scaleRatio);
			// 合成图片
			newBitmap = Bitmap.createBitmap(scaleWidth, scaleHeight,
					Bitmap.Config.ARGB_8888);
			//LogUtils.i("scaleWidth:" + scaleWidth + " scaleHeight:" + scaleHeight);
			Canvas newCanvas = new Canvas(newBitmap);
			Paint paint = new Paint();
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
			int left = 0;
			int top = 0;
			int right = scaleWidth;
			int bottom = scaleHeight;
			if (isNinePatch) {
				mNinePatch.draw(newCanvas, new Rect(left, top, right, bottom));
			} else {
				// 计算蒙版缩放
				background = getScaleImage(background, scaleWidth, scaleHeight);
				newCanvas.drawBitmap(background, 0, 0, null);
			}
			Matrix matrix = new Matrix();
			matrix.setScale(scaleRatio, scaleRatio);
			newCanvas.drawBitmap(srcBitmap, matrix, paint);
			//LogUtils.i("newBitmap.getWidth:" + newBitmap.getWidth() + " newBitmap.getHeight:" + newBitmap.getHeight());
		} finally {
			background.recycle();
		}
		return newBitmap;
	}
	/**
	 * 获取图片限定宽高
	 */
	private int getMaxElementSize(Context context, SizeType type) {
		int size = 0;
		if (context != null) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			int screenWidth = dm.widthPixels;
			int screenHeight = dm.heightPixels;
			switch (type) {
				case width: {
					size = screenWidth / 3;
					break;
				}
				case height: {
					size = screenHeight / 8;
					break;
				}
			}
		}
		return size;
	}
}
