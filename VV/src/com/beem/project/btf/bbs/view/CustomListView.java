package com.beem.project.btf.bbs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.beem.project.btf.R;

public class CustomListView extends ListView {
	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFadingEdgeLength(0);
		setScrollingCacheEnabled(false);
		setDividerHeight(0);
		setSelector(R.color.transparent);
	}
	public CustomListView(Context context) {
		super(context);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpec;
		if (getLayoutParams().height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
			// The great Android "hackatlon", the love, the magic.
			// The two leftmost bits in the height measure spec have
			// a special meaning, hence we can't use them to describe height.
			heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
					MeasureSpec.AT_MOST);
		} else {
			// Any other height should be respected as is.
			heightSpec = heightMeasureSpec;
		}
		super.onMeasure(widthMeasureSpec, heightSpec);
	}
	public void updateView(int itemIndex, IUpdateListener lis) {
		int visiblePosition = getFirstVisiblePosition();
		View v = getChildAt(itemIndex - visiblePosition);
		lis.onUpdate(v, itemIndex);
	}

	public interface IUpdateListener {
		void onUpdate(View convertView, int position);
	}
}
