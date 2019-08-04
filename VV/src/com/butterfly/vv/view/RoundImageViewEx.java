package com.butterfly.vv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beem.project.btf.R;

public class RoundImageViewEx extends RelativeLayout {
	private Bitmap cover;
	private Bitmap src;
	private ImageView srcView;
	private ImageView coverView;
	private ImageType type = ImageType.Normal;
	private int padding_src = (int) TypedValue
			.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
					.getDisplayMetrics());
	private int padding_cover;
	private float radius_weight = 0.94f;

	public enum ImageType {
		Normal, Round, Ring, RoundCorner;
	}

	public RoundImageViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
		setGravity(Gravity.CENTER);
		srcView = new ImageView(context);
		LayoutParams lp_src = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		addView(srcView, lp_src);
		coverView = new ImageView(context);
		LayoutParams lp_cover = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		addView(coverView, lp_cover);
		readAttrs(context, attrs);
	}
	private void readAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.roundimageview);
		for (int i = 0; i < ta.length(); i++) {
			int attr = ta.getIndex(i);
			switch (attr) {
				case R.styleable.roundimageview_img_src: {
					src = BitmapFactory.decodeResource(getResources(),
							ta.getResourceId(attr, 0));
					setImageBitmap(src);
					break;
				}
				case R.styleable.roundimageview_img_cover: {
					cover = BitmapFactory.decodeResource(getResources(),
							ta.getResourceId(attr, 0));
					setCover(cover);
					break;
				}
				case R.styleable.roundimageview_type: {
					type = ImageType.values()[ta.getInt(attr, 0)];
					break;
				}
				case R.styleable.roundimageview_padding_src: {
					padding_src = ta.getDimensionPixelSize(attr, padding_src);
					setPaddingSrc(padding_src);
					//LogUtils.i("~padding_src~" + padding_src);
					break;
				}
				case R.styleable.roundimageview_padding_cover: {
					padding_cover = ta.getDimensionPixelSize(attr,
							padding_cover);
					setPaddingCover(padding_cover);
					break;
				}
				case R.styleable.roundimageview_radius_weight: {
					radius_weight = ta.getFloat(attr, radius_weight);
					break;
				}
				default:
					break;
			}
		}
		ta.recycle();
	}
	public void setPaddingSrc(int paddingSrc) {
		this.padding_src = paddingSrc;
		srcView.setPadding(paddingSrc, paddingSrc, paddingSrc, paddingSrc);
	}
	public void setPaddingCover(int paddingCover) {
		this.padding_cover = paddingCover;
		coverView.setPadding(paddingCover, paddingCover, paddingCover,
				paddingCover);
	}
	public void setCover(Bitmap foreGround) {
		this.cover = type == ImageType.Ring ? foreGround : null;
		coverView.setImageBitmap(cover);
	}
	public void setImageBitmap(Bitmap bmp, ImageType... typeOpt) {
		if (typeOpt.length > 0) {
			this.type = typeOpt[0];
		}
		switch (type) {
			case Ring:
			case Round:
				bmp = ImageHelper.toRoundBitmap(bmp);
				break;
			case RoundCorner:
				bmp = ImageHelper.toRoundCornerBitmap(bmp, 2);
				break;
			default:
				break;
		}
		srcView.setImageBitmap(bmp);
	}

	public static class ImageHelper {
		/**
		 * 转成圆形btimap
		 * @param bitmap
		 * @return
		 */
		public static Bitmap toRoundBitmap(Bitmap bitmap) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;
				top = 0;
				bottom = width;
				left = 0;
				right = width;
				height = width;
				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;
				float clip = (width - height) / 2;
				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;
				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}
			Bitmap output = Bitmap
					.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right,
					(int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top,
					(int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);
			return output;
		}
		/**
		 * 转换成圆角图片
		 * @param bitmap
		 * @param pixels
		 * @return
		 */
		public static Bitmap toRoundCornerBitmap(Bitmap bitmap, float pixels) {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
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
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int padding = (int) (Math.min(coverView.getMeasuredWidth(),
				coverView.getMeasuredHeight()) * (1 - radius_weight));
		setPaddingSrc(padding);
	}
}
