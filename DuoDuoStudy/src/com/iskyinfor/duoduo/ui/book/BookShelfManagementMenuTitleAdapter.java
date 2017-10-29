package com.iskyinfor.duoduo.ui.book;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BookShelfManagementMenuTitleAdapter extends BaseAdapter {
	private Context mContext;
	private int fontColor;
	private TextView[] title;
	
	public BookShelfManagementMenuTitleAdapter(Context context, String[] titles, int fontSize,int color) {
		this.mContext = context;
		this.fontColor = color;
		this.title = new TextView[titles.length];
		
		for (int i = 0; i < titles.length; i++)
		{
			title[i] = new TextView(mContext);
			title[i].setText(titles[i]);
			title[i].setId(i);
			title[i].setTextSize(fontSize);
			title[i].setTextColor(fontColor);
			title[i].setGravity(Gravity.CENTER);
			title[i].setPadding(10, 10, 10, 10);
			title[i].setOnClickListener(new BookShelfMenu());
			//title[i].setBackgroundColor(R.drawable.bookshelf_readss);
				//title[i].setBackgroundResource(R.drawable.green_btn);
		}
	}
	@Override
	public int getCount() {
		return title.length;
	}

	@Override
	public Object getItem(int position) {
		return title[position];
	}

	@Override
	public long getItemId(int position) {
		return title[position].getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null)
		{
			view = title[position];
		} else {
			view = convertView;
		}
		return view;
	}
	
	private final class BookShelfMenu implements View.OnClickListener{
    	public void onClick(View v){
    
    	if(v.getId()==0)//
    	{
    		Toast.makeText( mContext, "全选", 1).show();
    	}
    	if(v.getId()==1)//
    	{
    		Toast.makeText(mContext, "删除", 1).show();
    	}
    	
    }
}


}
