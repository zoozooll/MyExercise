package com.iskyinfor.duoduo.ui.talkgarden;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;

public class TalkLearncirclesActivity extends Activity implements PageListView.NetetworkDataInterface,OnClickListener{
    private PageListView talklearncircleslistview;
    private ProgressDialog progressDialog;
    private TalkLearncircleAdapter talklearncircleadapter;
    private ArrayList<TalkGarden> talkgarden = new ArrayList<TalkGarden>();
    private ImageView ImgMenubutton; 
    private ImageView ImgBackbutton;
    private LinearLayout talk_bottom_linear1;
    private TalkGardenShowQQ showqq;
    private TextView textView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.talk_learncircles);
		initView();
		loadData();
	}
	
	public void initView()
	{
		talklearncircleslistview=(PageListView)findViewById(R.id.talk_learncircles_listview);
		ImgMenubutton=(ImageView)findViewById(R.id.duoduo_lesson_list_img);
		ImgBackbutton=(ImageView)findViewById(R.id.duoduo_lesson_back_img);
		ImgBackbutton.setOnClickListener(this);
		ImgMenubutton.setVisibility(View.GONE);
		talk_bottom_linear1=(LinearLayout)findViewById(R.id.talk_bottom_linear1);
		talk_bottom_linear1.setVisibility(View.GONE);
		textView = (TextView) findViewById(R.id.talk_message_text);
		textView.setOnClickListener(this); 
		progressDialog = new ProgressDialog(TalkLearncirclesActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();
	}
	
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
    	{
    	     case R.id.duoduo_lesson_back_img:
        		finish();
    	        break;
    	     case R.id.talk_message_text:
    	    	 showqq=new TalkGardenShowQQ(TalkLearncirclesActivity.this,textView,"TalkLearncirclesActivity");
 				 showqq.showQQScren();
    	     default:	
    	    	 break;
    	}
		
	}
	private void loadData() {
		//PageBookEventListener pageBookEventListener = new PageBookEventListener(progressDialog);
		talklearncircleadapter = new TalkLearncircleAdapter(TalkLearncirclesActivity.this,talkgarden);
		talklearncircleslistview.setListAdapter(talklearncircleadapter);
		//talklearncircleslistview.setPageEvenListener(pageBookEventListener);
		talklearncircleslistview.setNetetworkDataInterface(this);
		talklearncircleslistview.loadPage(1);
		progressDialog.dismiss();
	}
	@Override
	public PageinateContainer condition(int reqPage) {
		PageinateContainer pageinateContainer = new PageinateContainer();

		try {
			pageinateContainer.totalPageCount=2;
			pageinateContainer.totalCount=20;
			for(int i=0;i<20;i++){
				TalkGarden talk=new TalkGarden();
				talk.setTalkName("李恩熙+");
	    		pageinateContainer.responseData.add(talk);
	    	}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageinateContainer;
	}

	@Override
	public PageinateContainer condition(int reqPage, String strSearch,
			int intGiftType) {
		// TODO Auto-generated method stub
		return null;
	}

}
