package com.iskyinfor.duoduo.ui.talkgarden;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iskyinfor.duoduo.R;

public class UserListActivity extends Activity 
{
	private ListView messageList = null;
	private LinearLayout layout = null;
	private FrameLayout frame = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talk_user_listview_right);
		initView();
	}
	
	private void initView() 
	{
		messageList = (ListView) findViewById(R.id.user_listview_message);
		TalkGardenListAdapter userAdapter = new TalkGardenListAdapter(UserListActivity.this);
		messageList.setAdapter(userAdapter);
		
		layout = (LinearLayout) findViewById(R.id.friends_name_llayout);
		String args[] ={"成寐","鸿达","季虹","蓝心雨","白莲花","姜水","柠檬","德文","窝文","紫薯","蒙恬"};
		
		for(int i =0;i<5;i++)
		{
			frame = (FrameLayout) findViewById(R.id.user_friends_name_flayout);
			TextView textView = new TextView(UserListActivity.this);
			textView.setGravity(Gravity.CENTER);
			textView.setText(args[i]);
			frame.addView(textView);
		}
		
		layout.addView(frame);
		
	}

	
//	private ExpandableListView listView = null;
//	private void initView()
//	{
//		listView = (ExpandableListView)findViewById(R.id.talk_expandableListView);
//	    listView.setGroupIndicator(null);  //去掉箭头图标
//		UserListAdapter  userAdapter = new UserListAdapter(UserListActivity.this);
//	    listView.setAdapter(userAdapter);
//	}	
}