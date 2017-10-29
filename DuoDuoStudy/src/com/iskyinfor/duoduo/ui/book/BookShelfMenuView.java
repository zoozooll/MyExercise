package com.iskyinfor.duoduo.ui.book;

import android.app.Activity;
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
import com.iskyinfor.duoduo.ui.downloader.DowanloadManagerActivity;
import com.iskyinfor.duoduo.ui.lesson.LessonActivity;

public class BookShelfMenuView implements OnItemClickListener {
	private GridView popupGridView;
	private Context context;
	LayoutInflater inflater;
	/**
	 * 判断是书架还是同步教学
	 */
	private int screenSign = 0;

	public BookShelfMenuView(Activity context,int screenSign)
	{
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.screenSign = screenSign;
	}

	private void initView() {
		popupGridView = new GridView(context);
		popupGridView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		popupGridView.setNumColumns(3);
		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		popupGridView.setVerticalSpacing(1);
		popupGridView.setHorizontalSpacing(1);
		popupGridView.setGravity(Gravity.CENTER);
		popupGridView.setOnItemClickListener(this);
		BookShelfMenuTitleAdapter adapter = new BookShelfMenuTitleAdapter(this.context,
				//new String[] { "继续阅读", "下载管理", "书架管理", "我的赠送", "我的推荐", "我的评论" },
				new String[] { "下载管理" },
				15, 0xFF000000);
		popupGridView.setAdapter(adapter);
	}

	public View createView() {
		initView();
		LinearLayout layout = new LinearLayout(this.context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(popupGridView);
		layout.setFocusable(true);
		return layout;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i("liu", "position");
		// TODO 书架menu跳转
		if(position==0)//继续阅读
    	{
			Intent intent =new Intent(context,DowanloadManagerActivity.class);
    		context.startActivity(intent);  
    	}
    	if(position==1)//下载管理
    	{
    		Intent intent =new Intent(context,DowanloadManagerActivity.class);
    		context.startActivity(intent);  
    	}
    	if(position==2)//书架管理
    	{ 
    		Intent intent =new Intent(context,BookShelfManagementActivity.class);
    		context.startActivity(intent); 
    	}
    	if(position==3)//我的赠送
    	{ Intent intent =new Intent(context,BookShelfGiftActivity.class);
    	context.startActivity(intent); }
    	if(position==4)//我的推荐 
    	{ Intent intent =new Intent(context,BookShelfRecommendActivity.class);
    	context.startActivity(intent);  }
    	if(position==5)//我的评论
    	{
    		Intent intent =new Intent(context,BookshelfCommentListActivity.class);
    		context.startActivity(intent);  }
    	
    	if(screenSign == 1)
    	{
    		((BookShelfActivity)context).popDismiss();
    	}

    	if(screenSign == 2)
    	{
    		((LessonActivity)context).popDismiss();
    	}

	}
	
	public class BookShelfMenuTitleAdapter extends BaseAdapter {

		private Context mContext;
		private int fontColor;
		private TextView[] title;

		public BookShelfMenuTitleAdapter(Context context, String[] titles,
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
