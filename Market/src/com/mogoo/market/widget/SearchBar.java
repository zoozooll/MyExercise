package com.mogoo.market.widget;


import com.mogoo.market.R;

import android.R.color;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 搜索 widget
 * @author john-deng 
 *
 */
public class SearchBar extends LinearLayout {

	public EditText mEditText;
	public Button mSearBtn;
	
	public LinearLayout mMainFrame;
	public FrameLayout mRLayout;
	
	
	public SearchBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SearchBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate( R.layout.search_bar, this, true);
		
		mMainFrame = (LinearLayout)findViewById(R.id.mogoo_search_header);
		mEditText =(EditText)findViewById(R.id.search_edit);
		mRLayout =(FrameLayout)findViewById(R.id.search_edit_frame);
		mSearBtn = (Button)findViewById(R.id.search_btn);
	
		mSearBtn.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					v.setBackgroundResource(R.drawable.btn_search_pressed);
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					v.setBackgroundResource(R.drawable.btn_search_normal);
				}
				return false;
			}
			
		});

	}

	  public void setBackground(Drawable drawable)
	  {
		  mMainFrame.setBackgroundDrawable(drawable);
	  }
	  public void setBackground(int resId)
	  {
		  mMainFrame.setBackgroundResource(resId);
	  }
	  
	  public void setEditText(CharSequence text)
	  {
		  mEditText.setText(text);
	  }
	  public void setEditText(int resid)
	  {
		  mEditText.setText(resid);
	  }
	  
	  public void setSearchButtonBackground(Drawable drawable)
	  {
		  mSearBtn.setBackgroundDrawable(drawable);
	  }
	  public void setSearchButtonBackground(int resId)
	  {
		  mSearBtn.setBackgroundResource(resId);
	  }

}
