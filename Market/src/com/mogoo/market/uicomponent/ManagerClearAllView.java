package com.mogoo.market.uicomponent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mogoo.market.R;

public class ManagerClearAllView extends LinearLayout 
{
	/** 整个XML代表的view */
	private View view;
	/** 整个XML代表的view */
	private Button clearAllButton;
	
	private String text;
	
	public ManagerClearAllView(Context context,String text) 
	{
		super(context);
		this.text = text;
		initView();
	}
	
	private void initView() 
	{
		this.view = LayoutInflater.from(getContext()).inflate(R.layout.manager_clear_all_layout, null);
		this.clearAllButton = (Button) this.view.findViewById(R.id.btn_clear_all);
		this.clearAllButton.setText(text);
		addView(view, new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Button getClearAllButton() {
		return clearAllButton;
	}

	public void setClearAllButton(Button clearAllButton) {
		this.clearAllButton = clearAllButton;
	}
	
}
