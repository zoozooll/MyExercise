package com.iskyinfor.duoduo.ui.lesson;

import com.iskyinfor.duoduo.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonMenuTitleAdapter extends BaseAdapter 
{
	private Context mContext;
	private int fontColor;
	private TextView[] title;
	
	public LessonMenuTitleAdapter(Context context, String[] titles, int fontSize,int color) {
		this.mContext = context;
		this.fontColor = color;
		this.title = new TextView[titles.length];
		
		for (int i = 0; i < titles.length; i++)
		{
			title[i] = new TextView(mContext);
			title[i].setText(titles[i]);
			title[i].setTextSize(fontSize);
			title[i].setTextColor(fontColor);
			title[i].setGravity(Gravity.CENTER);
			title[i].setPadding(10, 10, 10, 10);
			title[i].setBackgroundResource(R.drawable.toolbar_menu_release);
		}
	}
	
	public int getCount()
	{
		return title.length;
	}

	public Object getItem(int position) 
	{
		return title[position];
	}

	public long getItemId(int position)
	{
		return title[position].getId();
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		if (convertView == null)
		{
			view = title[position];
		} else {
			view = convertView;
		}
		return view;
	}

}
