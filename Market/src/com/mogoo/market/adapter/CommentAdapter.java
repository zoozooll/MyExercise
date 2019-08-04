package com.mogoo.market.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mogoo.market.R;
import com.mogoo.market.model.Comments;

/**
 *@description 评论的数据适配器
 */
public class CommentAdapter<T> extends ArrayAdapter<T> 
{
	private int layId=R.layout.app_comment_item;
	private  LayoutInflater inflater;
	private Context mContext;
	private ViewHolder hotHolder;
	
	public CommentAdapter(Context context,List<T> list) 
	{
		super(context, -1, list);
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		hotHolder=null;
		final Comments comments=(Comments)getItem(position);

		if(convertView==null)
		{
			hotHolder=new ViewHolder(); 
			
			convertView = inflater.inflate(layId, null);			
			
			hotHolder.app_comment_item_lay=(LinearLayout)convertView.findViewById(R.id.app_comment_item_lay);
			hotHolder.cm_name=(TextView)convertView.findViewById(R.id.cm_name);
			hotHolder.cm_date=(TextView)convertView.findViewById(R.id.cm_date);	
			hotHolder.cm_rating=(RatingBar)convertView.findViewById(R.id.cm_rating);
			hotHolder.cm_content=(TextView)convertView.findViewById(R.id.cm_content);
					
			convertView.setTag(hotHolder);
		}
		else
		{
			hotHolder =(ViewHolder) convertView.getTag();
		}
		
	    
		if(comments.getName()==null || (comments.getName()!=null && comments.getName().equals("null")) )
		{
			hotHolder.cm_name.setText(R.string.niming);
		}
		else
		{
			hotHolder.cm_name.setText(comments.getName());
		}
	    hotHolder.cm_date.setText(comments.getDate());
	    hotHolder.cm_rating.setRating(Float.parseFloat(comments.getRating()));
	    hotHolder.cm_content.setText(comments.getComment());
		  
		return convertView;
	}

	static class ViewHolder 
	{
		public LinearLayout app_comment_item_lay;
		
		public TextView cm_name; // 评论的用户名		
		public TextView cm_date;// 评论日期
		public RatingBar cm_rating; // 评论软件
		
		public TextView cm_content; // 软件评论
	}

}
