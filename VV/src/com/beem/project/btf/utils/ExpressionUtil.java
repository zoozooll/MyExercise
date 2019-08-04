package com.beem.project.btf.utils;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.butterfly.vv.VVprotocolActivity;

public class ExpressionUtil {
	/**
	 * 表情的尺寸
	 */
	public enum ExpressionSizeType {
		small(20), middle(30), larger(50);
		private int size;

		private ExpressionSizeType(int size) {
			this.size = size;
		}
		public int getsize() {
			return size;
		}
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 */
	public static void matchExpression(Context context,
			SpannableString spannableString, Pattern patten, int start,
			ExpressionSizeType sizeType) throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			if (matcher.start() < start) {
				continue;
			}
			Field field = R.drawable.class.getDeclaredField(key);
			int resId = field.getInt(null); // 通过上面匹配得到的字符串来生成图片资源id
			if (resId != 0) {
				VerticalImageSpan imageSpan = new VerticalImageSpan(context,
						getBitmap(context, resId, sizeType));// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				int end = matcher.start() + key.length(); // 计算该图片名字的长度，也就是要替换的字符串的长度
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
				if (end < spannableString.length()) { // 如果整个字符串还未验证完，则继续。。
					matchExpression(context, spannableString, patten, end,
							sizeType);
				}
				break;
			}
		}
	}
	public static void matchExpression(Context context,
			SpannableString spannableString, Pattern patten, int start) {
		try {
			matchExpression(context, spannableString, patten, start,
					ExpressionSizeType.middle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 根据资源id去创建一个bitmap
	 * @param context
	 * @param resid
	 * @return Bitmap
	 */
	private static Bitmap getBitmap(Context context, int resId,
			ExpressionSizeType sizeType) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resId);
		bitmap = Bitmap.createScaledBitmap(bitmap,
				DimenUtils.dip2px(context, sizeType.getsize()),
				DimenUtils.dip2px(context, (int) (sizeType.getsize() / 1.22f)),
				true);
		return bitmap;
	}
	/**
	 * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
	 * @param context
	 * @param str
	 * @return SpannableString
	 */
	public static SpannableString getExpressionString(Context context,
			String str, String zhengze, ExpressionSizeType sizeType) {
		SpannableString spannableString = new SpannableString(str);
		Pattern sinaPatten = Pattern.compile(zhengze); // 通过传入的正则表达式来生成一个pattern
		try {
			matchExpression(context, spannableString, sinaPatten, 0, sizeType);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}
	public static SpannableString getExpressionString(Context context,
			String str, String zhengze) {
		return getExpressionString(context, str, zhengze,
				ExpressionSizeType.middle);
	}
	/**
	 * 得到一个SpanableString对象，通过传入的字符串和资源id
	 * @param context
	 * @param str
	 * @param id
	 * @return SpannableString
	 */
	public static SpannableString getExpressionString(Context context,
			String str, int id, ExpressionSizeType sizeType) {
		SpannableString spannableString = new SpannableString(str);
		VerticalImageSpan imageSpan = new VerticalImageSpan(context, getBitmap(
				context, id, sizeType));
		spannableString.setSpan(imageSpan, 0, str.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}
	public static SpannableString getExpressionString(Context context,
			String str, int id) {
		return getExpressionString(context, str, id, ExpressionSizeType.middle);
	}
	public static void setLinkedTextView(Context ctx,
			TextView mFreeRegisterText, String str, int startPos, int endPos) {
		SpannableString sp = new SpannableString(str);
		sp.setSpan(new URLSpan("http://www.baidu.com"), startPos, endPos,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mFreeRegisterText.setText(sp);// SpannableString对象设置给TextView
		mFreeRegisterText.setMovementMethod(LinkMovementMethod.getInstance()); // 设置TextView可点击
		CharSequence text = mFreeRegisterText.getText();
		if (text instanceof Spannable) {
			int end = text.length();
			Spannable spannable = (Spannable) mFreeRegisterText.getText();
			URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
			SpannableStringBuilder styleBuilder = new SpannableStringBuilder(
					text);
			styleBuilder.clearSpans();
			MyClickableSpan myClickableSpan = new MyClickableSpan(ctx);
			for (URLSpan urlSpan : urlSpans) {
				styleBuilder.setSpan(myClickableSpan,
						spannable.getSpanStart(urlSpan),
						spannable.getSpanEnd(urlSpan),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			}
			mFreeRegisterText.setText(styleBuilder);
		}
	}

	public static class MyClickableSpan extends ClickableSpan {
		private Context mContext;

		public MyClickableSpan(Context mContext) {
			super();
			this.mContext = mContext;
		}
		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(Color.argb(255, 26, 174, 241)); // 设置链接文字颜色
			ds.setUnderlineText(true); // 去掉下划线
			ds.setTextSize(TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP, 14, mContext.getResources()
							.getDisplayMetrics()));
		}
		@Override
		public void onClick(View widget) {
			Intent intent = new Intent(mContext, VVprotocolActivity.class);
			mContext.startActivity(intent);
		}
	}

	/**
	 * 垂直居中的ImageSpan
	 * @author yangle
	 */
	public static class VerticalImageSpan extends ImageSpan {
		public VerticalImageSpan(Context context, Bitmap bitmap) {
			super(context, bitmap);
		}
		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end,
				Paint.FontMetricsInt fontMetricsInt) {
			Drawable drawable = getDrawable();
			Rect rect = drawable.getBounds();
			if (fontMetricsInt != null) {
				Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
				int fontHeight = fmPaint.bottom - fmPaint.top;
				int drHeight = rect.bottom - rect.top;
				int top = drHeight / 2 - fontHeight / 4;
				int bottom = drHeight / 2 + fontHeight / 4;
				fontMetricsInt.ascent = -bottom;
				fontMetricsInt.top = -bottom;
				fontMetricsInt.bottom = top;
				fontMetricsInt.descent = top;
			}
			return rect.right;
		}
		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end,
				float x, int top, int y, int bottom, Paint paint) {
			Drawable drawable = getDrawable();
			canvas.save();
			int transY = 0;
			transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
			canvas.translate(x, transY);
			drawable.draw(canvas);
			canvas.restore();
		}
	}

	/**
	 * @func
	 * @param msgText
	 * @param content
	 * @param params 是三个参数，姓名，内容，用户uid
	 */
	public static void showEmoteInListview(final Context mContext,
			TextView msgText, ExpressionSizeType sizeType, String... params) {
		try {
			String content = params.length > 0 ? params[0] : "";
			String author = params.length > 1 ? params[1] : "";
			String commentdetail;
			SpannableString spannableString;
			if (!TextUtils.isEmpty(author)) {
				author = "@" + author;
				commentdetail = author + "" + content;
				ClickableSpan headClickSpan = new ClickableSpan() {
					int color = -1;

					@Override
					public void updateDrawState(TextPaint ds) {
						if (color == -1) {
							ds.setColor(Color.argb(255, 24, 173, 239));
						} else {
							ds.setColor(color);
						}
						ds.setUnderlineText(false);
					}
					@Override
					public void onClick(View paramView) {
					}
				};
				spannableString = getExpressionString(mContext, commentdetail,
						ChatActivity.PATTERN, sizeType);
				int length = author.length();
				// 设置姓名相应时间和点击特效
				spannableString.setSpan(headClickSpan, 0, length,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				commentdetail = content;
				spannableString = getExpressionString(mContext, commentdetail,
						ChatActivity.PATTERN, sizeType);
			}
			msgText.setText(spannableString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
