package com.iskyinfor.duoduo.ui.talkgarden;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;

public class TalkGardenActivity extends Activity implements OnClickListener
{
	public  PopupWindow mPopupWindow = null;
	private Gallery mGallery = null;
	private ListView mListView = null;
	private ImageView smailImage = null;
	private PopupWindow popup = null;
	private TextView textView = null;
	private PopupWindow menuWindow;
	private ExpandableListView talkexpandablelistview;
	private TalkGardenShowQQ showqq;
	
	//取到屏幕
    private int screenWidth;   
    private int screenHeight;   
    private int dialgoWidth;   
    private int dialgoheight;
	private ImageView ImgMenubutton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talk_garden_activity);
		initView();
	}

	private void initView() 
	{
		ImgMenubutton=(ImageView)findViewById(R.id.duoduo_lesson_list_img);
		ImgMenubutton.setOnClickListener(this);
		RelativeLayout listhead=(RelativeLayout)getLayoutInflater().inflate(R.layout.talk_user_list_items, null);
		talkexpandablelistview=(ExpandableListView) listhead.findViewById(R.id.talk_expandableListView);
		talkexpandablelistview.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(TalkGardenActivity.this,String.valueOf(childPosition)+" child "+ parent.getId()+" "+parent.toString() , 1).show();
				return false;
			}
		});
		//人物个性图片
		mGallery = (Gallery) findViewById(R.id.talk_main_gallery);	
		mGallery.setSpacing(10);
		TalkGardenGalleryAdapter galleryAdapter = new TalkGardenGalleryAdapter(TalkGardenActivity.this);
		mGallery.setAdapter(galleryAdapter);
		//显示好友列表
		textView = (TextView) findViewById(R.id.talk_message_text);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				showqq=new TalkGardenShowQQ(TalkGardenActivity.this,textView,"TalkGardenActivity");
				showqq.showQQScren();
			} });
		//聊天消息
//		mListView = (ListView) findViewById(R.id.talk_main_listview);
//		TalkGardenListAdapter listAdapter = new TalkGardenListAdapter(TalkGardenActivity.this);
//		mListView.setAdapter(listAdapter);
		//发送图标
		smailImage = (ImageView) findViewById(R.id.talk_bottom_smail);
		smailImage.setOnClickListener(new View.OnClickListener()
		{   @Override
			public void onClick(View v) 
			{showSmailIcon();}
		});
		
	}

	protected void showSmailIcon() 
	{
		LinearLayout layout = new LinearLayout(TalkGardenActivity.this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		GridView gridView = new GridView(TalkGardenActivity.this);
		gridView.setLayoutParams(new LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		gridView.setNumColumns(10);
		gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		gridView.setVerticalSpacing(1);
		gridView.setHorizontalSpacing(1);
		gridView.setGravity(Gravity.CENTER);
		gridView.setAdapter(new TalkGardenGridViewAdapter(TalkGardenActivity.this));
		
		layout.addView(gridView);
		
		popup = new PopupWindow(layout, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.talk_bg_botom)); 
		popup.setFocusable(true);
		popup.update();
		popup.showAtLocation(smailImage, Gravity.BOTTOM, 0, 80);
	}

	
	private void getScreenMethod()
	{
		screenWidth = TalkGardenActivity.this.getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = TalkGardenActivity.this.getWindowManager().getDefaultDisplay().getHeight();
	
		dialgoWidth = mPopupWindow.getWidth();
		dialgoheight = mPopupWindow.getHeight();
	}

	static class QQHolder
	{
		ExpandableListView listView = null;
	}

    @Override
    public void onClick(View v)
    {
    	switch(v.getId())
    	{
    	     case R.id.duoduo_lesson_back_img:
        		finish();
    	        break;
    	     case R.id.duoduo_lesson_list_img:
    	    	 if(menuWindow!=null&&menuWindow.isShowing())
    	    	 {menuWindow.dismiss();}
    	    	 else{
    	    		 TalkGardenMenuView talkgardenmenuview=new TalkGardenMenuView(TalkGardenActivity.this);
    	    		 View menuView=talkgardenmenuview.createView();
    	    		 menuWindow=new PopupWindow(menuView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    	    		 menuWindow.setBackgroundDrawable(getResources().getDrawable(
    							R.drawable.bookshelf_indextopback)); // 设置menu菜单背景
    					menuWindow.update();
    					menuWindow.setFocusable(true);
    					menuWindow.showAtLocation(ImgMenubutton, Gravity.BOTTOM, 0, 80);
    	    	 }
    	    	 break;
    	     default:	
    	    	 break;
    	}
    }


}
