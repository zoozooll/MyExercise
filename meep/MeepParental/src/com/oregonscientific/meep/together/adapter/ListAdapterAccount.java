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

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.bean.Kid;


public class ListAdapterAccount extends ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "ListAccount";
	private int resourceId = 0;
	private LayoutInflater inflater;
	
	static int i = 0;

	public ListAdapterAccount(Context context, int resourceId, List<HashMap<String, Object>> Items) {
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
	    
    	TextView textName;
	    TextView textCoins;
	    final ImageView image;

	    view = inflater.inflate(resourceId,null);

	    try {
	    	textName = (TextView)view.findViewById(R.id.textName);
	    	textCoins = (TextView)view.findViewById(R.id.textCoins);
	    	image = (ImageView) view.findViewById(R.id.user_icon);
	    } catch( ClassCastException e ) {
	    	Log.e(TAG, "Wrong resourceId", e);
	    	throw e;
	    }
	    
	    Kid kid = new Gson().fromJson((String)item.get("kid"),Kid.class);
	    
	    textName.setText(kid.getName());
	    textCoins.setText(Long.toString(kid.getCoins()));
	    
	    if(position%2 == 0)
    	{
    		view.setBackgroundResource(R.color.item_bkg_single);
    	}
    	else
    	{
    		view.setBackgroundResource(R.color.item_bkg_double);
    	}
	    
	    //image
	    UserFunction.loadImage(image, kid.getAvatar());
        view.setTag(kid);
	    return view;
	}
}
