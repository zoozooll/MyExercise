package com.iskyinfor.duoduo.ui.talkgarden;

import com.iskyinfor.duoduo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserListAdapter extends BaseExpandableListAdapter {
	private Activity mContext = null;
	private Context showcontext;
	private LayoutInflater factory = null;
	private String activityname;
	private String[] groups = 
	{ "我的好友", "同城好友", "邻里乡亲", "爱呗同事" };
	
	private String[][] children = 
    {
      { "胡算林", "张俊峰", "王志军", "秋香" ,"龙啸天"},
      { "李秀婷", "蔡乔", "别高", "余音" },
      { "摊派新", "张爱明" },
      { "马超", "司道光" }
    };
	
	
	public UserListAdapter(Activity context,Context shwcontext,String actname)
	{
		mContext = context;
		factory = LayoutInflater.from(mContext);
		activityname=actname;
		showcontext=shwcontext;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return children[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return children[groupPosition].length;
	}
	
	
	@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) 
	{
		final int index=childPosition;
		final View v= convertView;
		ViewHolder holder = null;
		if(convertView == null)
		{
			convertView = factory.inflate(R.layout.talk_user_list_items_child, null);
			holder = new ViewHolder();
			holder.iamge = (ImageView) convertView.findViewById(R.id.talk_current_userinfo_image);
			holder.username = (TextView) convertView.findViewById(R.id.talk_uname_text);
			holder.usersign = (TextView) convertView.findViewById(R.id.talk_signature_text);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.iamge.setImageResource(R.drawable.person_liweiwei);
		holder.username.setText(getChild(groupPosition, childPosition).toString());
		holder.usersign.setText("从此你的天空不再落寞！！");
		
		holder.iamge.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(mContext,String.valueOf(index)+" child "+ v.getId()+" "+v.toString() , 1).show();
                ((TalkGardenShowQQ)showcontext).mPopupWindow.dismiss(); 
	
			}
		});
		
		
		
		return convertView;
	}

//==================分组处理方法=======================
	
	@Override
	public Object getGroup(int groupPosition)
	{
		return groups[groupPosition];
	}

	@Override
	public int getGroupCount() 
	{
		return groups.length;
	}

	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View view, ViewGroup parent) 
	{
		 TextView textView = getGenericView();
         textView.setText(getGroup(groupPosition).toString());
         return textView;       
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) 
	{
		return true;
	}

	//==== 设置分组TextView的属性 ====
    public TextView getGenericView()
    {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        textView.setTextSize(15);
        textView.setTextColor(Color.BLACK);
        return textView;
    }


    static class ViewHolder
    {
    	ImageView iamge;
    	TextView username,usersign;
    }
}
