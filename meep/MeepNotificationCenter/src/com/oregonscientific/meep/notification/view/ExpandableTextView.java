package com.oregonscientific.meep.notification.view;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class ExpandableTextView extends TextView {
	
	private final int DEFAULT_LINE_COUNT = 3;
	
	public interface OnExpandAndCollapseListener {
		// Called when a group is expanded.
		public void onExpanded();
		
		// Called when a group is collapsed.
		public void onCollapsed();
		
		// Called when setting text if the text is over with max line
		public void onSettingText(boolean expandable);
	}
	
	private OnExpandAndCollapseListener onExpandAndCollapseListener;
	
	private boolean expanded;
	
	public ExpandableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		setEllipsize(TruncateAt.END);
		expand(false);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
		onExpandAndCollapseStateChanged();
	}
	
	@Override
	public void setText(CharSequence text, TextView.BufferType type) {
		super.setText(text, type);
		
		requestLayout();
	}
	
	/**
	 * Invoked when the expand/collapse state changed
	 */
	protected void onExpandAndCollapseStateChanged() {
		if (getOnExpandAndCollapseListener() != null) {
			if (!isEllipsized()) {
				getOnExpandAndCollapseListener().onSettingText(isExpandable());
			} else {
				getOnExpandAndCollapseListener().onSettingText(true);
			}
		}
	}

	/**
	 * Returns the listener to invoke when the text view is expanded or collapsed
	 */
	public OnExpandAndCollapseListener getOnExpandAndCollapseListener() {
		return onExpandAndCollapseListener;
	}

	/**
	 * Sets the listener to invoke when the text view is expanded or collapsed
	 * 
	 * @param listener the listener to invoke when the text view is expanded or collapsed
	 */
	public void setOnExpandAndCollapseListener(OnExpandAndCollapseListener listener) {
		this.onExpandAndCollapseListener = listener;
	}
	
	protected boolean isExpandable() {
		boolean result = isEllipsized();
		if (!result) {
			Layout l = getLayout();
			if (l != null) {
				result = l.getLineCount() > DEFAULT_LINE_COUNT;
			}
		}
		return result;
	}
	
	public void toggleExpand() {
		if (isEllipsized()) {
			expand(true);
		} else {
			expand(false);
		}
	}
	
	/**
	 * Expands or collapse the text view 
	 * 
	 * @param expand {@code true} to expand the text view, {@code false} to collapse
	 */
	public void expand(final boolean b) {
		Runnable callback = new Runnable() {

			@Override
			public void run() {
				if (getOnExpandAndCollapseListener() != null) {
					if (b) {
						getOnExpandAndCollapseListener().onExpanded();
					} else {
						getOnExpandAndCollapseListener().onCollapsed();
					}
				}
			}
			
		};
		expand(b, callback);
	}
	
	public void expand(boolean b, Runnable callback) {
		setExpanded(b);
		int lines = b ? Integer.MAX_VALUE : DEFAULT_LINE_COUNT;
		setMaxLines(lines);
		if (callback != null) {
			callback.run();
		}
	}
	
	public boolean isEllipsized() {
		Layout l = getLayout();
		if (l != null) {
			int lines = l.getLineCount();
			if (lines > 0)
				if (l.getEllipsisCount(lines - 1) > 0) {
					return true;
				}
		}
        return false;
	}

	/**
	 * Returns whether the text view is expanded
	 * @return {@code true} if the text view is expanded, {@code false} otherwise
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * Sets whether the text view is expanded or collapsed
	 * 
	 * @param expanded
	 *            {@code true} to indicate the text view is expanded,
	 *            {@code false} to indicate the text view is collapsed
	 */
	private void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	
	
}
