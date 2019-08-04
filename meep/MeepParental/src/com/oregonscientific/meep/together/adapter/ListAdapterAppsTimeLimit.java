package com.oregonscientific.meep.together.adapter;


import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.PortalSettingsPermission;
import com.oregonscientific.meep.together.activity.PortalSettingsTimeLimit;


public class ListAdapterAppsTimeLimit extends ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "ListNotification";
	private int resourceId = 0;
	private LayoutInflater inflater;
	private String[] units;
	private int index;

	public ListAdapterAppsTimeLimit(Context context,int index,int resourceId, List<HashMap<String, Object>> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		units = context.getResources().getStringArray(R.array.unit_string);
		this.index = index;
	}
	
	public void refresh()
	{
		notifyDataSetChanged();  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View view;
	    HashMap<String, Object> item = getItem(position);
                
    	TextView textTime;
    	TextView textTimeUnit;
    	ImageView tick;
    	
    	view = inflater.inflate(resourceId,null);
    	try {
    		textTime = (TextView)view.findViewById(R.id.textTime);
    		textTimeUnit = (TextView)view.findViewById(R.id.textUnit);
    		tick = (ImageView)view.findViewById(R.id.isTick);
    	} catch( ClassCastException e ) {
    		Log.e(TAG, "Wrong resourceId", e);
    		throw e;
    	}
    	int t = (Integer) item.get("time");
    	String time = "";
    	String unit = "";
    	
    	switch(t)
    	{
        	case 1440: 
        		time = units[0]; 
        		break;
        	case 720:
        	case 480:
        	case 240:
        	case 120:
        		time = Integer.toString(t/60); unit=" "+units[1]; 
        		break;
        	case 60: 
        		time = Integer.toString(t/60); unit=" "+units[2]; 
        		break;
        	case 30:
        	case 15:
        		time = Integer.toString(t); unit=" "+units[3];
        		break;
        	case 0:
        		time=units[4]; 
        		break;
    	}
    	
    	if(index == position)
    	{
    		tick.setVisibility(View.VISIBLE);
    		PortalSettingsTimeLimit.timeTick = tick;
    	}
    	//set
    	textTime.setText(time);
    	textTimeUnit.setText(unit);
    	
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
