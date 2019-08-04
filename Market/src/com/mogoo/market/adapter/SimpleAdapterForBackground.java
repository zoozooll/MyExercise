package com.mogoo.market.adapter;

import java.util.List;
import java.util.Map;

import com.mogoo.market.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class SimpleAdapterForBackground extends SimpleAdapter {
	
	
	   public SimpleAdapterForBackground(Context context, List<Map<String, Object>> items, int resource, String[] from, int[] to) {   
		  super(context, items, resource, from, to);  
		  }

		   
		  @Override   
		  public View getView(int position, View convertView, ViewGroup parent) {   
		  convertView = null;

		  convertView = super.getView(position, convertView, parent);   
		  
		  convertView.setBackgroundResource(R.drawable.list_item_bg);
		  
//		  ViewGroup.LayoutParams params=convertView.getLayoutParams();
//		  params.height=50;
//		  convertView.setLayoutParams(params);

		  return convertView;
		    
		  }
		   	
}
