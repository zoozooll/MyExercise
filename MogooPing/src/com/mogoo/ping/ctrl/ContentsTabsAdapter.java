/**
 * 
 */
package com.mogoo.ping.ctrl;

import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.astuetz.viewpager.extensions.TabsAdapter;
import com.mogoo.ping.R;

/**
 * @author Aaron Lee
 * TODO
 * @Date 下午3:09:38  2012-10-30
 */
public class ContentsTabsAdapter implements TabsAdapter {
	
	private Context mContext;
	private int[] titleRes = {R.string.secondtitle_lasted, R.string.secondtitle_recommend, R.string.secondtitle_used};
	private LayoutInflater inflater;
	
	private Map<Integer, View> temp = new WeakHashMap<Integer, View>();
	public ContentsTabsAdapter(Context context) {
		super();
		mContext = context;
		inflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see com.astuetz.viewpager.extensions.TabsAdapter#getView(int)
	 */
	@Override
	public View getView(int position) {
		TextView b = null;
		if (position < titleRes.length) {
			b = (Button) temp.get(titleRes[position]);
			if (b == null) {
				b = (TextView) inflater.inflate(R.layout.tab_second_title_button, null);
				b.setText(titleRes[position]);
				temp.put(titleRes[position], b);
			} 
		}
		return  b;
	}
}
