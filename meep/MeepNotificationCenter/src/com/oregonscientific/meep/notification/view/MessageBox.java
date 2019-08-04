package com.oregonscientific.meep.notification.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.notification.R;
import com.oregonscientific.meep.notification.view.ExpandableTextView.OnExpandAndCollapseListener;

@SuppressLint("DrawAllocation")
public class MessageBox extends RelativeLayout {
	
	protected Bitmap top, middle, bottom;
	private Drawable topDrawable, middleDrawable, bottomDrawable, expandedDrawable, collapsedDrawable;
	protected RectF topRect, contentRect, bottomRect;
	
	private ImageView button;
	private MessageBoxItem mContent;
	
	// The listener to invoke when the view is expanded or collapsed
	private OnExpandAndCollapseListener mListener;
	
	public enum State {
		NOT_EXPANDABLE, EXPANDED, COLLPAPSED
	}
	
	public MessageBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MessageBox);
		topDrawable = a.getDrawable(R.styleable.MessageBox_topBackground);
		middleDrawable = a.getDrawable(R.styleable.MessageBox_contentBackground);
		bottomDrawable = a.getDrawable(R.styleable.MessageBox_bottomBackground);
		expandedDrawable = a.getDrawable(R.styleable.MessageBox_expandDrawable);
		collapsedDrawable = a.getDrawable(R.styleable.MessageBox_collapseDrawable);
		a.recycle();
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		setWillNotDraw(false);
		if (topDrawable != null) {
			top = ((BitmapDrawable) topDrawable).getBitmap();
			topDrawable = null;
		}

		if (middleDrawable != null) {
			middle = ((BitmapDrawable) middleDrawable).getBitmap();
			middleDrawable = null;
		}

		if (bottomDrawable != null) {
			bottom = ((BitmapDrawable) bottomDrawable).getBitmap();
			bottomDrawable = null;

		}
		
		button = (ImageView) findViewById(R.id.message_list_item_button);
		if (button != null && expandedDrawable != null) {
			button.setImageDrawable(expandedDrawable);
		}
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawBackground(canvas);
	}
	
	protected void drawBackground(Canvas canvas) {
		if (top != null && topRect != null) {
			canvas.drawBitmap(top, null, topRect, null);
		}
		
		if (middle != null && contentRect != null) {
			canvas.drawBitmap(middle, null, contentRect, null);
		}
		
		if (bottom != null && bottomRect != null) {
			canvas.drawBitmap(bottom, null, bottomRect, null);
		}
	}
	
	OnExpandAndCollapseListener expandAndCollapseListener = new OnExpandAndCollapseListener() {
		
		private OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleExpand();
			}
			
		};
		
		@Override
		public void onExpanded() {
			button.setImageDrawable(collapsedDrawable);
			if (mListener != null) {
				mListener.onExpanded();
			}
			
			requestLayout();
			invalidate();
		}

		@Override
		public void onCollapsed() {
			button.setImageDrawable(expandedDrawable);
			if (mListener != null) {
				mListener.onCollapsed();
			}
			
			requestLayout();
			invalidate();
		}

		@Override
		public void onSettingText(boolean expandable) {
			if (button != null) {
				button.setVisibility(expandable ? View.VISIBLE : View.GONE);
			}
			
			View buttonWrapper = findViewById(R.id.message_list_item_button_wrapper);
			if (buttonWrapper != null) {
				buttonWrapper.setOnClickListener(expandable ? clickListener : null);
			}
		}
	};
	
	public void setContent(MessageBoxItem view) {
		if (view == null) {
			return;
		}
		
		RelativeLayout contentWrapper = (RelativeLayout) findViewById(R.id.message_list_item_content);
		if (contentWrapper != null) {
			contentWrapper.removeAllViews();
			contentWrapper.addView(view);
		}
		
		mContent = view;
		final ExpandableTextView expandableTextView = (ExpandableTextView) mContent.findViewById(R.id.messagebox_item_message);
		if (expandableTextView != null) {
			expandableTextView.setOnExpandAndCollapseListener(expandAndCollapseListener);
			expandableTextView.setText(null);
			expandableTextView.expand(false, null);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		if (top != null && bottom != null) {
			topRect = new RectF(0 , 0, 600, top.getHeight());
			contentRect = new RectF(53, topRect.height() , topRect.width(), h  - bottom.getHeight());
			bottomRect = new RectF(53, h - bottom.getHeight() , topRect.width(), h);
			bottomRect = new RectF(53, h - bottom.getHeight() , 605, h);
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public void setTime(long time) {
		TextView textView = (TextView)findViewById(R.id.notification_time);
		if (textView == null) {
			return;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MMMM dd, yyyy");
		String result = formatter.format(new Date(time));
		if (result == null) {
			return;
		}
		textView.setText(result);
	}

	public boolean isExpanded() {
		ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.messagebox_item_message);
		return expandableTextView == null ? false : expandableTextView.isExpanded();
	}
	
	public void toggleExpand() {
		if (mContent != null) {
			expand(!isExpanded());
		}
	}
	
	public void expand(boolean b) {
		if (mContent != null) {
			mContent.showBigPicture(b);
			mContent.expandMessage(b);
		}
	}
	
	public void setListener (OnExpandAndCollapseListener listener) {
		mListener = listener;
	}
	
	public void setTopBackground(Drawable drawable) {
		setTopBackground(((BitmapDrawable)drawable).getBitmap());
	}
	
	private void setTopBackground(Bitmap bitmap) {
		this.top = bitmap;
		invalidate();
	}
	
	
}
