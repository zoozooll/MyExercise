package com.oregonscientific.meep.together.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.adapter.ListAdapterAppsTimeLimit;

public class PortalSettingsTimeLimit extends Activity
{
	//listview
	ListView listTimeLimit;
	private ListAdapterAppsTimeLimit timeLimitAdapter;
	private ArrayList<HashMap<String, Object>> arraylist_TimeLimit;
	private Integer[] timelimit = { 0,15,30,60,120,240,480,720,1440};
	public static ImageView timeTick;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
		setContentView(R.layout.layout_main_portal_setting_time);
		
		//get listview
		listTimeLimit = (ListView) findViewById(R.id.listTimeLimit);
		
		
		//set title
		TextView x = (TextView) findViewById(R.id.dialogBarTitle);
		String title = getIntent().getStringExtra("title");
		int index = getIntent().getIntExtra("index", 0);
		title = String.format(this.getResources().getString(R.string.main_portal_setting_label_timelimit), title);
		x.setText(title);
		
		initTimeLimitListItem(index);
		
		//back button
		findViewById(R.id.mainImageButtonBack).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
    public void initTimeLimitListItem(int index)
	{
    	//apps time limit 
    	arraylist_TimeLimit = new ArrayList<HashMap<String, Object>>(); 
		timeLimitAdapter = new ListAdapterAppsTimeLimit(this,index, R.layout.item_time, arraylist_TimeLimit);
		listTimeLimit.setAdapter(timeLimitAdapter);
		listTimeLimit.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(timeTick != null)
				{
					timeTick.setVisibility(View.GONE);
				}
				timeTick = (ImageView) arg1.findViewById(R.id.isTick);
				timeTick.setVisibility(View.VISIBLE);
				
				Intent intent = getIntent();
				intent.putExtra("index_time", arg2);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		for(int x:timelimit)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("time", x);
			arraylist_TimeLimit.add(map);
		}
		timeLimitAdapter.notifyDataSetChanged();
	}
    @Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.layout_rever_full_to_null_slide,R.anim.layout_rever_null_to_full_slide);
	}

}
