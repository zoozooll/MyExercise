package com.iskyinfor.duoduo;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.iskyinfor.duoduo.R;

public class CallbackTabMenu {
	private Context context = null;
	private LinearLayout layout = null;
	private GridView popupGridView = null;
	private BaseAdapter adapter = null;
	private PopupWindow popupWindow = null;
	private View view = null;
	
	public CallbackTabMenu(Context con,BaseAdapter bAdapter,View v) 
	{
		context = con;
		adapter = bAdapter;
		view = v;
	}
	
	public void setPupopWindow()
	{
		layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		popupGridView = new GridView(context);
		popupGridView.setLayoutParams(new LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		popupGridView.setNumColumns(2);
		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		popupGridView.setVerticalSpacing(1);
		popupGridView.setHorizontalSpacing(1);
		popupGridView.setGravity(Gravity.CENTER);
		popupGridView.setAdapter(adapter);
		layout.addView(popupGridView);
		
		popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.menu_bg));	       
		popupWindow.setFocusable(true);
		popupWindow.update();
		
		popupWindow.showAtLocation(view,Gravity.BOTTOM, 0, 30);
	}
	
}
