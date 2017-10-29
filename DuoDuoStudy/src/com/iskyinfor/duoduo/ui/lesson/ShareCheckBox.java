package com.iskyinfor.duoduo.ui.lesson;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iskyinfor.duoduo.R;

public class ShareCheckBox extends LinearLayout implements OnClickListener
{
	private Context mContext = null;
	private LayoutInflater factory = null;
	private ImageView iv = null;
	private TextView tv = null;
	private boolean flag = false;
	
	public ShareCheckBox(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
		factory = LayoutInflater.from(mContext);
		getView();
	}

	public ShareCheckBox(Context context) 
	{
		super(context);
		mContext = context;
		factory = LayoutInflater.from(mContext);
		getView();
	}

	private void getView()
	{
		View view = factory.inflate(R.layout.lesson_iamge_text, this);
		iv = (ImageView) view.findViewById(R.id.iamge);
		iv.setOnClickListener(this);
		tv = (TextView) view.findViewById(R.id.text);
		tv.setOnClickListener(this);
	}

	public void setTextViewValue(String text)
	{
			tv .setText(text);
			tv.setTextSize(10);
			tv.setTextColor(Color.BLACK);
	}
	
	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.iamge:
			if(flag)
			{
				iv.setImageResource(R.drawable.chececk_box_pull);
				flag = false;
			}else{
				iv.setImageResource(R.drawable.check_box_press);
				flag = true;
			}
			break;
		case R.id.text:
			break;
		}
	}
	
	public boolean isFlag() 
	{
		return flag;
	}
	
	public void setFlag(boolean flag) 
	{
		this.flag = flag;
	}
	
}
