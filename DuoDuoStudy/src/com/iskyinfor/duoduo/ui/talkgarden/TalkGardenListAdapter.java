package com.iskyinfor.duoduo.ui.talkgarden;

import com.iskyinfor.duoduo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TalkGardenListAdapter extends BaseAdapter {
	private Context mContext = null;
	LayoutInflater factory = null;
	
	private String [] userData = 
	{
	    "刘维:","好美丽：","黄河：","钟云：","蓝心：","郑梦：","小浩：","龙浩然：","王小骞：","阿芙："
	};
	
	private String [] contentData = 
	{
		"发个自定义表情看看","我这没有呀，怎么发？","发个自定义表情看看~~~",
		"我这没有呀，怎么发？","我这没有呀，怎么发？",
		"你自己看看自己的东西","是什么让我找到你","你的世界很精彩",
		"找到温暖的回忆","没有我的日子 怎么渡过"
	};
	
	private String [] timeData = 
	{
		"13:15","13:20","13:25","13:29","13:56","14:20","14:36","15:20","16:20","17:20"	
	};
	
	public TalkGardenListAdapter(Context c)
	{
		mContext = c;
		factory = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount()
	{
		return userData.length;
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
	public View getView(int position, View view, ViewGroup parent)
	{
		ListHolder holder = null ;
		if(view == null)
		{
			view = factory.inflate(R.layout.talk_garden_list_items, null);
			holder = new ListHolder();
			holder.userName = (TextView) view.findViewById(R.id.talk_username_text);
			holder.sendContent = (TextView) view.findViewById(R.id.talk_content_text);
			holder.sendTime = (TextView) view.findViewById(R.id.talk_time_text);
			view.setTag(holder);
		}
		else
		{
			holder = (ListHolder) view.getTag();
		}
		
		holder.userName.setText(userData[position]);
		holder.sendContent.setText(contentData[position]);
		holder.sendTime.setText(timeData[position]);
		
		return view;
	}

	static class ListHolder
	{
		TextView userName,sendContent,sendTime;
	}
	
}
