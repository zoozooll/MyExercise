package com.oregonscientific.meep.together.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;


public class ListAdapterAppsPermissionConfig extends ArrayAdapter<String> {
	private final static String TAG = "ListNotification";
	private int resourceId = 0;
	private LayoutInflater inflater;
	
	public ListAdapterAppsPermissionConfig(Context context, int resourceId, List<String> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void refresh()
	{
		notifyDataSetChanged();  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View view;
	    String permission = getItem(position);
                
    	view = inflater.inflate(resourceId, null);	
    	
    	((TextView)view.findViewById(R.id.textPermission)).setText(permission);
    	
//    	view.setFocusableInTouchMode(true);
//    	view.setFocusable(true);
//    	view.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				((Activity)getContext()).finish();
//				
//			}
//		});
    	
    	
    	if(position%2 == 0)
    	{
    		view.setBackgroundResource(R.color.item_bkg_one);
    	}
    	else
    	{
    		view.setBackgroundResource(R.color.item_bkg_two);
    	}
            
	    return view;
	}
	
}
