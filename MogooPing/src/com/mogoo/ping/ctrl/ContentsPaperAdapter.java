package com.mogoo.ping.ctrl;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.mogoo.ping.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author Aaron Lee
 * TODO
 * @Date 下午6:01:35  2012-10-30
 */
public class ContentsPaperAdapter extends PagerAdapter {
	
	private static final String TAG = "ContentsPaperAdapter";
	private Context mContext;
	private LayoutInflater inflater;
	private Map<Integer, View> temp = new HashMap<Integer, View>();

	public ContentsPaperAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.grid_contents, null);
		temp.put(0, v);
		v = inflater.inflate(R.layout.grid_contents, null);
		temp.put(1, v);
		v = inflater.inflate(R.layout.grid_contents, null);
		temp.put(2, v);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
	 */
	@Override
	public boolean isViewFromObject(View view, Object o) {
		
		return view == ((View) o);
	}
	
	public Object instantiateItem(View container, int position) {
		Log.d(TAG, "instantiateItem "+position);
		View v = temp.get(position);
		((ViewPager) container).addView(v, 0);
		return v;
	}
	
	@Override
	public void destroyItem(View container, int position, Object view) {
		Log.d(TAG, "destroyItem "+position);
		((ViewPager) container).removeView((View) view);
	}
	
	public View getItemView(int position) {
		return temp.get(position);
	}

}
