package com.oregonscientific.meep.together.adapter;


import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.Logs;


public class ListAdapterLogs extends ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "ListLogs";
	private int resourceId = 0;
	private LayoutInflater inflater;

	public ListAdapterLogs(Context context, int resourceId, List<HashMap<String, Object>> Items) {
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
	    HashMap<String, Object> item = getItem(position);
            if(item.get("isSection")!=null){
            	view = inflater.inflate(R.layout.section_logs,null);
 
            	view.setOnClickListener(null);
            	view.setOnLongClickListener(null);
            	view.setLongClickable(false);
 
                final TextView sectionView = (TextView) view.findViewById(R.id.textLogsDate);
                sectionView.setText(Html.fromHtml("<u>"+(String) item.get("date")+"</u>"));
                
            }else{
            	TextView textLogsInfo;
        	    TextView textLogsTime;
        	    ImageView category;

        	    view = inflater.inflate(resourceId,null);

        	    try {
        	    	textLogsInfo = (TextView)view.findViewById(R.id.textLogsInfo);
        	    	textLogsTime = (TextView)view.findViewById(R.id.textLogsTime);
        	    	category = (ImageView) view.findViewById(R.id.category);
        	    } catch( ClassCastException e ) {
        	    	Log.e(TAG, "Wrong resourceId", e);
        	    	throw e;
        	    }
        	    
        	    textLogsInfo.setText(((String) item.get("message")).replace('_', ' '));
        	    textLogsTime.setText((String) item.get("time"));
        	    
        	    String c = (String) item.get("category");
        	    if(c.equals(Logs.CATEGORY_APPS))
        	    {
        	    	category.setImageResource(R.drawable.log_app);
        	    }
        	    else if(c.equals(Logs.CATEGORY_EBOOK))
        	    {
        	    	category.setImageResource(R.drawable.log_ebook);
        	    }
        	    else if(c.equals(Logs.CATEGORY_GAME))
        	    {
        	    	category.setImageResource(R.drawable.log_game_icon);
        	    }
        	    else if(c.equals(Logs.CATEGORY_INTERNET))
        	    {
        	    	category.setImageResource(R.drawable.log_internet_icon);
        	    }
        	    else if(c.equals(Logs.CATEGORY_MUSIC))
        	    {
        	    	category.setImageResource(R.drawable.log_music_icon);
        	    }
        	    else if(c.equals(Logs.CATEGORY_MOVIE))
        	    {
        	    	category.setImageResource(R.drawable.log_video_icon);
        	    }
        	    else if(c.equals(Logs.CATEGORY_SYSTEM))
        	    {
        	    	category.setImageResource(R.drawable.log_meep_icon);
        	    }
        	    else
        	    {
        	    	//do nothing 
        	    }
        	    
        	    if(position%2 == 0)
            	{
            		view.setBackgroundResource(R.color.item_bkg_one);
            	}
            	else
            	{
            		view.setBackgroundResource(R.color.item_bkg_two);
            	}
            }
            
           
	    
         
	    return view;
	}
}
