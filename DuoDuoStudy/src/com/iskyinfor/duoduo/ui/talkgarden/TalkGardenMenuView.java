package com.iskyinfor.duoduo.ui.talkgarden;

import com.iskyinfor.duoduo.ui.IndexActivity;
import com.iskyinfor.duoduo.ui.book.BookShelfReadActivity;
import com.iskyinfor.duoduo.ui.downloader.DowanloadManagerActivity;
import com.iskyinfor.duoduo.ui.lesson.LessonActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TalkGardenMenuView implements OnItemClickListener {
    private GridView popupGridView;
	private Context context;
	LayoutInflater inflater;
	
	public TalkGardenMenuView(Context context)
	{
		inflater=LayoutInflater.from(context);
		this.context=context;
	}
	
	public View createView()
	{
		initView();
		LinearLayout layout =new LinearLayout(this.context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(popupGridView);
		layout.setFocusable(true);
		return layout;
	}
	
	public void initView()
	{
		popupGridView=new GridView(context);
		popupGridView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		popupGridView.setNumColumns(2);
		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		popupGridView.setHorizontalSpacing(1);
		popupGridView.setVerticalSpacing(1);
		popupGridView.setGravity(Gravity.CENTER);
		popupGridView.setOnItemClickListener(this);
		TalkGardenMenuTitleAdapter adapter=new TalkGardenMenuTitleAdapter(this.context, new String[]{"我的主页","学习圈子"}, 15, 0xFF000000);
		popupGridView.setAdapter(adapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(position==0)//
    	{
    		
    	}
    	if(position==1)//
    	{
    		Intent intent=new Intent(context,TalkLearncirclesActivity.class);
    		context.startActivity(intent);
    	}
	}
	
	public class TalkGardenMenuTitleAdapter extends BaseAdapter {

		private Context mContext;
		private int fontColor;
		private TextView[] title;

		public TalkGardenMenuTitleAdapter(Context context, String[] titles,
				int fontSize, int color) {
			this.mContext = context;
			this.fontColor = color;
			this.title = new TextView[titles.length];

			for (int i = 0; i < titles.length; i++) {
				title[i] = new TextView(mContext);
				title[i].setText(titles[i]);
				title[i].setId(i);
				title[i].setTextSize(fontSize);
				title[i].setTextColor(fontColor);
				title[i].setGravity(Gravity.CENTER);
				title[i].setPadding(10, 10, 10, 10);
				// title[i].setOnClickListener(new BookShelfMenu());
				// title[i].setBackgroundColor(R.drawable.bookshelf_readss);
				// title[i].setBackgroundResource(R.drawable.green_btn);
			}
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return title[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return title[position].getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if (convertView == null) {
				view = title[position];
			} else {
				view = convertView;
			}
			return view;
		}

	}

}
