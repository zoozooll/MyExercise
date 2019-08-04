package com.oregonscientific.meep.notification.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.notification.NotificationCenterApplication;
import com.oregonscientific.meep.notification.R;
import com.oregonscientific.meep.util.ImageDownloader;

public class MessageBoxItem extends RelativeLayout {
	
	private boolean mHasPicture = false;

	public MessageBoxItem(Context context) {
		this(context, null);
	}
	
	public MessageBoxItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		createChildWithStyle();
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		createChildWithStyle();
	}

	public Bitmap drawShadowWithBitmap(Bitmap source) {
		return drawShadowWithBitmap(source);
	}

	public Bitmap drawShadowWithBitmap(Bitmap source, int offsetX, int offsetY) {
		Bitmap bitmap = Bitmap.createBitmap(source.getWidth() + offsetX, source.getHeight() + offsetY, source.getConfig());
		Canvas canvas = new Canvas(bitmap);
		Paint shadow = new Paint();
		shadow.setShadowLayer(10f, offsetX, offsetY, Color.GRAY);
		canvas.drawBitmap(source, 0, 0, shadow);
		return bitmap;
	}

	protected void createChildWithStyle() {
		removeAllViews();
		View v = null;
		v = View.inflate(getContext(), R.layout.messagebox_item, null);
		if (v != null) {
			addView(v);
		}
	}
	
	public void setMessage(String message) {
		ExpandableTextView textView = (ExpandableTextView) findViewById(R.id.messagebox_item_message);
		if (textView != null) {
			textView.setText(message);
		}
	}
	
	/**
	 * Expands or collapse the message 
	 * 
	 * @param b {@code true} to expand, {@code false} otherwise
	 */
	public void expandMessage(boolean b) {
		ExpandableTextView textView = (ExpandableTextView) findViewById(R.id.messagebox_item_message);
		if (textView != null) {
			textView.expand(b);
		}
	}
	
	public void setIcon(String url) {
		ImageView imageView = (ImageView) findViewById(R.id.message_item_icon);
		if (imageView != null && url != null) {
			NotificationCenterApplication app = (NotificationCenterApplication) getContext().getApplicationContext();
			ImageDownloader downloader = app.getDownloader(NotificationCenterApplication.CACHE_NOTIFICATION);
			downloader.download(url, imageView);
		}
	}
	
	public void setImage(String url) {
		ImageView imageView = (ImageView) findViewById(R.id.messagebox_item_picture);
		mHasPicture = url != null && url.length() > 0;
		if (imageView != null && url != null) {
			NotificationCenterApplication app = (NotificationCenterApplication) getContext().getApplicationContext();
			ImageDownloader downloader = app.getDownloader(NotificationCenterApplication.CACHE_NOTIFICATION);
			downloader.download(url, imageView);
		}
	}
	
	public void showBigPicture(boolean b) {
		ImageView imageView = (ImageView) findViewById(R.id.messagebox_item_picture);
		if (imageView != null) {
			imageView.setAdjustViewBounds(b);
			imageView.setVisibility(b && mHasPicture ? View.VISIBLE : View.GONE);
		}		
	}
	
	public void setProgress(int max, int progress) {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.messagebox_item_progress_bar);
		if (progressBar != null) {
			progressBar.setVisibility(progress >= 0 ? View.VISIBLE : View.GONE);
			progressBar.setMax(max);
			progressBar.setProgress(Math.min(max, progress));
		}
	}
	
	public void setProgressIndeterminate(boolean indeterminate) {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.messagebox_item_progress_bar);
		if (progressBar != null) {
			progressBar.setIndeterminate(indeterminate);
		}
	}
	
	public void setTitle(String title) {
		TextView textView = (TextView) findViewById(R.id.messagebox_item_title);
		if (textView != null && title != null) {
			textView.setTextSize(32);
			textView.setText(title);
		}
		
		Resources r = getContext().getResources();
		float marginTop = title != null && title.length() > 0 ? TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics()) : 0.f;
		ImageView imageView = (ImageView) findViewById(R.id.message_item_icon);
		if (imageView != null) {
			RelativeLayout.LayoutParams params = (LayoutParams) imageView.getLayoutParams();
			params.setMargins(params.leftMargin, (int) marginTop, params.rightMargin, params.bottomMargin);
			imageView.setLayoutParams(params);
		}
	}
	
	public void setTextColor(int color) {
		TextView titleView = (TextView) findViewById(R.id.messagebox_item_title);
		ExpandableTextView messageView = (ExpandableTextView)findViewById(R.id.messagebox_item_message);
		if (titleView != null) {
			titleView.setTextColor(color);
		}
		
		if (messageView != null) {
			messageView.setTextColor(color);
		}
	}
	
	public void setNegativeButton(OnClickListener onClickListener) {
		setButton(R.id.negative_button, onClickListener);
	}
	
	public void setPositiveButton(OnClickListener onClickListener) {
		setButton(R.id.positive_button, onClickListener);
	}
	
	private void setButton(int buttonId, OnClickListener onClickListener) {
		Button button = (Button)findViewById(buttonId);
		if (onClickListener != null) {
			ViewGroup viewGroup = (ViewGroup) button.getParent();
			if (viewGroup == null) {
				return;
			}
			viewGroup.setVisibility(View.VISIBLE);
		}
		if (button != null && button.getVisibility() == View.VISIBLE) {
			button.setOnClickListener(onClickListener);
		} 
	}
	
	/**
	 * Sets the enabled stated of the positive and negative buttons
	 * 
	 * @param enabled {@code true} to enable the buttons, {@code false} to disable
	 */
	public void setIntentButtonsEnabled(boolean enabled) {

		Button positiveButton = (Button) findViewById(R.id.positive_button);
		Button negativeButton = (Button) findViewById(R.id.negative_button);

		if (positiveButton != null) {
			positiveButton.setEnabled(enabled);
		}

		if (negativeButton != null) {
			negativeButton.setEnabled(enabled);
		}
	}
	
}
