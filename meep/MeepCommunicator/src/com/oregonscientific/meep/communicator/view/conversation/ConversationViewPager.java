package com.oregonscientific.meep.communicator.view.conversation;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager of conversations
 */
public class ConversationViewPager extends ViewPager {
	
	private boolean isScrollingEnabled;
	
	private OnClickListener mListener = null;
	
	private int mSelectedIndex = -1;
	
	/**
	 * Constructor for Conversation View Pager
	 * @param context current context
	 * @param attrs attribute set
	 */
	public ConversationViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.isScrollingEnabled = true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.isScrollingEnabled) {
			return super.onTouchEvent(event);
		}
		
		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.isScrollingEnabled) {
			return super.onInterceptTouchEvent(event);
		}
		
		return false;
	}
	
	/**
	 * Set listener when an item is clicked in view pager
	 * @param listener listener to be set
	 */
	public void setOnItemClickListener(OnClickListener listener) {
		mListener = listener;
	}
	
	/**
	 * Called when an item in one of the pages in the pager is clicked
	 * @param position the position of the item in the underlying data-set
	 */
	public void onItemClick(int position) {
		mSelectedIndex = position;
		if (mListener != null) {
			mListener.onClick(position);
		}
	}
	
	/**
	 * Returns the index of the selected item in the underlying data-set
	 * @return index of the selected item in the underlying data-set, -1 if no
	 *         item is selected
	 */
	public int getSelectedIndex() {
		return mSelectedIndex;
	}
	
	/**
	 * Set the enabled state of scrolling
	 * @param enable true if scrolling is enabled, false otherwise
	 */
	public void setScrollingEnabled(boolean enable) {
		this.isScrollingEnabled = enable;
	}
	
	/**
	 * Interface definition for a callback to be invoked when a view inside one of
	 * the pages in the pager is clicked.
	 */
	public interface OnClickListener {
		
		/**
		 * Called when a view has been clicked.
		 * @param position The position of the item clicked in the underlying
		 *          dataset
		 */
		public void onClick(int position);
	}
	
}