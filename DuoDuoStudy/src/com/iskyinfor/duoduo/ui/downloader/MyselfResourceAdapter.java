package com.iskyinfor.duoduo.ui.downloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.lesson.NeedDataInformation;

public class MyselfResourceAdapter extends BaseAdapter
{
	private Context context = null;
	LayoutInflater factory = null;
	private String [] res  = NeedDataInformation.myRes;
	
	public MyselfResourceAdapter(Context c)
	{
		context = c;
		factory = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return res.length;
	}

	public Object getItem(int position) 
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) 
	{
		ViewHolder holder = null;
		try {
			if(view == null)
			{
				view = factory.inflate(R.layout.download_myres_items, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) view.findViewById(R.id.myres_imge_item);
				holder.tv = (TextView) view.findViewById(R.id.myres_txt_item);
				view.setTag(holder);
			}
			else
			{
				view.getTag();
			}
			
			holder.iv.setImageResource(R.drawable.download_sign);
			holder.tv.setText(res[position]);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return view;
	}
	
	static class ViewHolder 
	{
		ImageView iv = null;
		TextView tv = null;
	}
	
}
