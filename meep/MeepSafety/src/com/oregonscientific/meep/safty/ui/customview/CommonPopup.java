package com.oregonscientific.meep.safty.ui.customview;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.customfont.MyButton;

import com.oregonscientific.meep.safty.R;


public class CommonPopup extends Dialog implements android.view.View.OnClickListener{
	public interface OnClickOkButtonListener {
		public void onClickOk();
	}
	OnClickOkButtonListener onClickOkButtonListener = null;
	
	MyButton okButton;
	MyTextView title;
	TextView text;
	ImageButton imageViewQuit;
	
	public CommonPopup(Context context,int titleResourceId,int contentResourceId) {
		super(context);
		initDialog();
		title.setText(titleResourceId);
		text.setText(contentResourceId);
	}
	public CommonPopup(Context context,String titleString,String contentString) {
		super(context);
		initDialog();
		title.setText(titleString);
		text.setText(contentString);
	}
	public CommonPopup(Context context) {
		super(context);
		initDialog();
	}
	
	public void initDialog()
	{
		/*requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.popup);
		onClickOkButtonListener = null;
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		title = (MyTextView) findViewById(R.id.title);
		text = (TextView) findViewById(R.id.text);
		okButton = (MyButton) findViewById(R.id.btnOk);
		okButton.setOnClickListener(this);*/
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.notification_message);
		onClickOkButtonListener = null;
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		title = (MyTextView) findViewById(R.id.textViewNotice);
		text = (TextView) findViewById(R.id.textViewMessage);
		okButton = (MyButton) findViewById(R.id.textViewOkBtn);
		imageViewQuit = (ImageButton) findViewById(R.id.imageViewQuit);
		okButton.setOnClickListener(this);
		imageViewQuit.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == okButton)
		{
			dismiss();
			if(onClickOkButtonListener!=null) onClickOkButtonListener.onClickOk();
		}else if (v == imageViewQuit)
		{
			dismiss();
			if(onClickOkButtonListener!=null) onClickOkButtonListener.onClickOk();
		}
	}
	public void setTextGravity(int gravity)
	{
		text.setGravity(gravity);
	}
	public void setTextSize(float size)
	{
		text.setTextSize(size);
	}
	public void setTitleSize(float size)
	{
		title.setTextSize(size);
	}
	
	public void blockBackButton()
	{
		this.setCancelable(false);
	}
	public void setOnClickOkButtonListener(
			OnClickOkButtonListener onClickOkButtonListener) {
		this.onClickOkButtonListener = onClickOkButtonListener;
	}
}
