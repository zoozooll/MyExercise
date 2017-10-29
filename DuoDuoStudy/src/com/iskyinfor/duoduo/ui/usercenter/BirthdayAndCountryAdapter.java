package com.iskyinfor.duoduo.ui.usercenter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BirthdayAndCountryAdapter extends BaseAdapter {

	private Context mContext = null;
	private TextView title = null;
	private ArrayList<String> data = null;

	
	public BirthdayAndCountryAdapter(Context con) 
	{
		mContext = con;
	}

	public BirthdayAndCountryAdapter(Context con,ArrayList<String> yearList)
	{
		mContext = con;
		data = yearList;
	}

	@Override
	public int getCount() 
	{
		return data.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		title = new TextView(mContext);

		if (convertView == null) 
		{
			title.setText(position+"");
		}
		else 
		{
			title = (TextView) convertView;
		}
		
		title.setText(data.get(position).toString());
		title.setTextSize(10);
		title.setTextColor(Color.BLUE);
		title.setGravity(Gravity.CENTER);
		title.setPadding(10, 10, 10, 10);
		
		return title;
	}

}
