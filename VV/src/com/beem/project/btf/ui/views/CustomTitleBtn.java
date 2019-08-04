package com.beem.project.btf.ui.views;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.utils.DimenUtils;

/**
 * 带缺口的复合按钮
 **/
public class CustomTitleBtn extends RelativeLayout {
	private ImageView iv;
	private TextView tv;
	private View view;
	private Context mContext;
	private Bitmap mSlideIcon;
	private Bitmap mShadow;
	private int mCurrentSlideX;
	private int mCurrentSlideY;
	private Rect mLeftDrawRect;
	private Rect mRightDrawRect;
	private boolean isDrawSlideIcon = false;
	private boolean isSelected = false;
	private ColorStateList tv_ColorStateList;

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomTitleBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	/**
	 * @param context
	 */
	public CustomTitleBtn(Context context) {
		super(context);
		init(mContext, null);
	}
	public CustomTitleBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	private void init(Context context, AttributeSet attrs) {
		mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.customtitlebtn,
				this, false);
		iv = (ImageView) view.findViewById(R.id.image_btn);
		tv = (TextView) view.findViewById(R.id.text_btn);
		this.mSlideIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.menubtn_arr);
		this.mShadow = BitmapFactory.decodeResource(getResources(),
				R.drawable.menubtn_shadow);
		iv.setPadding(0, mSlideIcon.getHeight(), 0, 0);
		tv.setPadding(0, mSlideIcon.getHeight(), 0, 0);
		this.mLeftDrawRect = new Rect();
		this.mRightDrawRect = new Rect();
		LinearLayout slideLayout = new LinearLayout(this.mContext);
		slideLayout.setOrientation(LinearLayout.VERTICAL);
		slideLayout.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		slideLayout.addView(view, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
		View localView = new View(this.mContext);
		slideLayout.addView(localView, new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				this.mSlideIcon.getHeight()));
		addView(slideLayout, new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT));
		if (attrs != null)
			readAttrs(attrs);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isDrawSlideIcon) {
			// 被选中就画一个缺口
			this.mLeftDrawRect.set(0, this.mCurrentSlideY, this.mCurrentSlideX,
					this.mCurrentSlideY + this.mShadow.getHeight());
			canvas.drawBitmap(this.mShadow, null, this.mLeftDrawRect, null);
			this.mRightDrawRect.set(
					this.mCurrentSlideX + this.mSlideIcon.getWidth(),
					this.mCurrentSlideY, getWidth(), this.mCurrentSlideY
							+ this.mShadow.getHeight());
			canvas.drawBitmap(this.mShadow, null, this.mRightDrawRect, null);
			canvas.drawBitmap(this.mSlideIcon, this.mCurrentSlideX,
					this.mCurrentSlideY, null);
		} else {
			this.mLeftDrawRect.set(0, this.mCurrentSlideY, this.mCurrentSlideX,
					this.mCurrentSlideY + this.mShadow.getHeight());
			canvas.drawBitmap(this.mShadow, null, this.mLeftDrawRect, null);
			this.mRightDrawRect.set(this.mCurrentSlideX, this.mCurrentSlideY,
					getWidth(), this.mCurrentSlideY + this.mShadow.getHeight());
			canvas.drawBitmap(this.mShadow, null, this.mRightDrawRect, null);
		}
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		this.mCurrentSlideX = ((getMeasuredWidth() - this.mSlideIcon.getWidth()) / 2);
		this.mCurrentSlideY = (b - t - this.mSlideIcon.getHeight());
	}
	public void readAttrs(AttributeSet attrs) {
		TypedArray typeArr = mContext.obtainStyledAttributes(attrs,
				R.styleable.customtitlebtn);
		for (int i = 0; i < typeArr.length(); i++) {
			int attr = typeArr.getIndex(i);
			switch (attr) {
				case R.styleable.customtitlebtn_imgHeight: {
					float height = typeArr.getDimension(attr,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					setImgHeight(height);
					break;
				}
				case R.styleable.customtitlebtn_imgWidth: {
					float width = typeArr.getDimension(attr,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					setImgWidth(width);
					break;
				}
				case R.styleable.customtitlebtn_text: {
					int resId = typeArr.getResourceId(attr, -1);
					if (resId != -1) {
						setText(resId);
						break;
					}
					CharSequence res2 = typeArr.getString(attr);
					setText(res2);
					break;
				}
				case R.styleable.customtitlebtn_src: {
					int resId = typeArr.getResourceId(attr, 0);
					iv.setImageResource(resId);
					break;
				}
				case R.styleable.customtitlebtn_textColor: {
					int resId = typeArr.getResourceId(attr, -1);
					if (resId != -1) {
						setTextColor(mContext.getResources().getColor(resId));
						//setTextColorSelector(typeArr.getColorStateList(attr));
					}
					break;
				}
				case R.styleable.customtitlebtn_imgBackgroud: {
					int resId = typeArr.getResourceId(attr, 0);
					setImgBackgroundResource(resId);
					break;
				}
				default:
					break;
			}
		}
		typeArr.recycle();
		tv_ColorStateList = tv.getTextColors();
	}
	public CustomTitleBtn setTextAndImgBgRes(String txt, int imgId) {
		setText(txt);
		setImgBackgroundResource(imgId);
		return this;
	}
	public CustomTitleBtn setTextAndImgRes(String txt, int imgId) {
		setText(txt);
		setImgResource(imgId);
		return this;
	}
	public CustomTitleBtn setText(String txt) {
		tv.setText(txt);
		return this;
	}
	public CustomTitleBtn setTextBackground(int resId) {
		tv.setBackgroundResource(resId);
		return this;
	}
	public CharSequence getText() {
		return tv.getText();
	}
	public CustomTitleBtn setText(CharSequence txt) {
		tv.setText(txt);
		return this;
	}
	public CustomTitleBtn setText(int txt) {
		tv.setText(txt);
		return this;
	}
	public CustomTitleBtn setTextColor(int color) {
		tv.setTextColor(color);
		return this;
	}
	public CustomTitleBtn setTextColorSelector(int resid) {
		ColorStateList colorlist = null;
		try {
			XmlPullParser xrp = getResources().getXml(resid);
			colorlist = ColorStateList.createFromXml(getResources(), xrp);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv.setTextColor(colorlist);
		tv_ColorStateList = colorlist;
		return this;
	}
	public CustomTitleBtn setImgVisibility(int visibility) {
		iv.setVisibility(visibility);
		return this;
	}
	public CustomTitleBtn setTextViewVisibility(int visibility) {
		tv.setVisibility(visibility);
		return this;
	}
	public CustomTitleBtn setImgBackgroundResource(int imgId) {
		iv.setBackgroundResource(imgId);
		return this;
	}
	public CustomTitleBtn setImgResource(int imgId) {
		iv.setImageResource(imgId);
		return this;
	}
	public CustomTitleBtn setDrableLeftPadding(float leftpadding) {
		tv.setPadding((int) leftpadding, 0, 0, 0);
		return this;
	}
	public CustomTitleBtn setDrableLeftMargin(float leftMargin) {
		LayoutParams tvLP = (LayoutParams) tv.getLayoutParams();
		tvLP.leftMargin = (int) leftMargin;
		tv.setLayoutParams(tvLP);
		return this;
	}
	public CustomTitleBtn setImgWidth(float width) {
		LayoutParams lp = (LayoutParams) iv.getLayoutParams();
		lp.width = (int) width;
		iv.setLayoutParams(lp);
		return this;
	}
	public CustomTitleBtn setImgHeight(float height) {
		LayoutParams lp = (LayoutParams) iv.getLayoutParams();
		lp.height = (int) height;
		iv.setLayoutParams(lp);
		return this;
	}
	public CustomTitleBtn setViewPaddingLeft() {
		// 左上右下
		view.setPadding(DimenUtils.dip2px(mContext, 12), 0, 0, 0);
		return this;
	}
	public CustomTitleBtn setViewPaddingRight() {
		view.setPadding(0, 0, DimenUtils.dip2px(mContext, 12), 0);
		return this;
	}
	@Override
	public boolean isSelected() {
		return isSelected;
	}
	@Override
	public void setSelected(boolean isSelected) {
		this.isDrawSlideIcon = isSelected;
		this.isSelected = isSelected;
		tv.setSelected(isSelected);
		iv.setSelected(isSelected);
		invalidate();
	}
	public void setSelected(boolean isSelected, boolean isDrawSlideIcon) {
		this.isDrawSlideIcon = isDrawSlideIcon;
		this.isSelected = isSelected;
		tv.setSelected(isSelected);
		iv.setSelected(isSelected);
		invalidate();
	}
}
