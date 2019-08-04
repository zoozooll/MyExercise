package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;

public class FinishActivityDialog extends Dialog implements OnClickListener {
	Button okButton;
	TextView text;
	Context mContext;

	public FinishActivityDialog(Context context,int resource) {
		super(context);
		initDialog();
		text.setText(resource);
		mContext = context;
	}
	public FinishActivityDialog(Context context,String t) {
		super(context);
		initDialog();
		text.setText(t);
		mContext = context;
	}
	
	public void initDialog()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.common_message);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		text = (TextView) findViewById(R.id.text);
		okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == okButton)
		{
			finishWhichOne();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			finishWhichOne();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void finishWhichOne()
	{
		if (((Activity)mContext)  instanceof MeepTogetherMainActivity) {
			((MeepTogetherMainActivity)mContext).finish();
		}
		if (((Activity)mContext)  instanceof MeepTogetherSplashScreen) {
			((MeepTogetherSplashScreen)mContext).finish();
		}
		if (((Activity)mContext)  instanceof MeepTogetherStartUsingActivity) {
			((MeepTogetherStartUsingActivity)mContext).finish();
		}
		dismiss();
	}
	
	
}
