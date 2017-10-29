package com.iskyinfor.duoduo.ui.lesson;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.iskyinfor.duoduo.R;

public class LessonSpinnerAdapter extends BaseAdapter {
	private Context context = null;
	private int [] data = null;
	
	public LessonSpinnerAdapter(Context c,int[] selectData)
	{
		context = c;
		data = selectData;
	}
	
	public int getCount() 
	{
		return data.length;
	}

	public Object getItem(int position) 
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		TextView btn = new TextView(context);
		btn.setText("" + context.getResources().getText(data[position]));
		btn.setPadding(1, 1, 1, 1);
		btn.setGravity(Gravity.CENTER);
		
		switch (position) {
		case 0:
			btn.setBackgroundResource(R.drawable.lesson_spinner_first);
			break;
		case 1:
			btn.setBackgroundResource(R.drawable.lesson_spinner_second);
			break;
		case 2:
			btn.setBackgroundResource(R.drawable.lesson_spinner_second);
			break;
		case 3:
			btn.setBackgroundResource(R.drawable.lesson_spinner_second);
			break;
		case 4:
			btn.setBackgroundResource(R.drawable.lesson_spinner_second);
			break;
		case 5:
			btn.setBackgroundResource(R.drawable.lesson_spinner_second);
			break;
		case 6:
			btn.setBackgroundResource(R.drawable.lesson_spinner_fourth);
			break;
		default:
			break;
		}


		btn.setTextSize(12);
		btn.setTextColor(Color.BLACK);
		return btn;
	}
}
