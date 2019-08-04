package com.butterfly.vv.view.grid;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class OptimizeGridView extends GridView {
	/**
	 * 要用能包含重复元素的集合
	 * @param <T> 自定义gridview是为了解决与scrollView的嵌套问题
	 */
	public interface OptimizeGridAdapter<T> {
		List<T> getItems();
		/**
		 * Should notify the listView data is changed
		 * @param items
		 */
		void setItems(List<T> items);
		T getNullItem();
		boolean isNullItem(T item);
	}

	public OptimizeGridView(Context context) {
		super(context);
	}
	public OptimizeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public OptimizeGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		/*int numColumns = AUTO_FIT;
		final boolean isApi11 = false;// getResources().getBoolean(R.bool.api11);
		if (isApi11) {
			// API level 11 引入该函数，在低�?1版本�?使用反射获取列数
			 numColumns = getNumColumns();
		} else {
			try {
				Field numColumnsField = GridView.class.getDeclaredField("mNumColumns");
				numColumnsField.setAccessible(true);
				numColumns = numColumnsField.getInt(this);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
			} catch (NoSuchFieldException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		} */
	}
}
