package com.oregonscientific.meep.ringmenu;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.osgd.meep.library.R;

public class RingMenu extends RelativeLayout {
	
	View view;

	public RingMenu(Context context, AttributeSet attrs, int leftRight, int buttonNum){
	   super(context, attrs);

		if (leftRight == 0  && buttonNum == 1) {
			//left menu with one button
			leftMenuOneButton(context, attrs);
		}else if (leftRight == 1  && buttonNum == 1) {
			//right menu with one button
			rightMenuOneButton(context, attrs);
		}else if (leftRight == 0  && buttonNum == 2) {
			//left menu with two button
			leftMenuTwoButton(context, attrs);
		}else if (leftRight == 1  && buttonNum == 2) {
			//right menu with two button
			rightMenuTwoButton(context, attrs);
		}
		
	}
	
	private void leftMenuOneButton(Context context, AttributeSet attrs){
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.ring_menu_left_one_button, this);
			
	}
	
	private void rightMenuOneButton(Context context, AttributeSet attrs){
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.ring_menu_right_one_button, this);
			
	}
	
	private void leftMenuTwoButton(Context context, AttributeSet attrs){
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.ring_menu_left_two_button, this);
		
	}
	
	private void rightMenuTwoButton(Context context, AttributeSet attrs){
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.ring_menu_right_two_button, this);
		
	}
}
