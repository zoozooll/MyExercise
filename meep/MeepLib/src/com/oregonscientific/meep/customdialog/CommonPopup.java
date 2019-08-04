package com.oregonscientific.meep.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyTextView;
import com.osgd.meep.library.R;

public class CommonPopup extends Dialog implements android.view.View.OnClickListener{
	public interface OnClickOkButtonListener {
		public void onClickOk();
	}
	public interface OnClickCloseButtonListener {
		public void onClickClose();
	}
	OnClickOkButtonListener onClickOkButtonListener = null;
	OnClickCloseButtonListener onClickCloseButtonListener = null;
	
	Button okButton;
	Button cancelButton;
	Button proceedButton;
	ImageButton closeButton;
	View panelTwoButton;
	MyTextView title;
	TextView text;
	
	public CommonPopup(Context context,int titleResourceId,int contentResourceId) {
		super(context);
		initDialogDefault();
		title.setText(titleResourceId);
		text.setText(contentResourceId);
	}
	public CommonPopup(Context context,String titleString,String contentString) {
		super(context);
		initDialogDefault();
		title.setText(titleString);
		text.setText(contentString);
	}
	public CommonPopup(Context context,int titleResourceId,String contentString) {
		super(context);
		initDialogDefault();
		title.setText(titleResourceId);
		text.setText(contentString);
	}
	public CommonPopup(Context context) {
		super(context);
		initDialogDefault();
	}
	
	public void initDialog()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.popup);
		onClickOkButtonListener = null;
		onClickCloseButtonListener = null;
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		title = (MyTextView) findViewById(R.id.title);
		text = (TextView) findViewById(R.id.text);
	}
	
	public void initDialogDefault()
	{
		initDialog();
		configOneButtonPanel();
	}
	public void initDialogTwoButton()
	{
		initDialog();
		configTwoButtonPanel();
	}
	public void configTwoButtonPanel()
	{
		okButton.setVisibility(View.GONE);
		proceedButton = (MyButton) findViewById(R.id.btnProceed);
		cancelButton = (MyButton) findViewById(R.id.btnCancel);
		proceedButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}
	
	private void configOneButtonPanel()
	{
		okButton = (MyButton) findViewById(R.id.btnOk);
		okButton.setVisibility(View.VISIBLE);
		okButton.setOnClickListener(this);
	}
	
	public void setEnableCloseButton(boolean enable)
	{
		closeButton = (ImageButton)findViewById(R.id.btnClose);
		if (enable) {
			closeButton.setVisibility(View.VISIBLE);
			closeButton.setOnClickListener(this);
		} else {
			closeButton.setVisibility(View.GONE);
			closeButton.setOnClickListener(null);
		}
		
	}
	public void setEnableTwoButtonPanel(boolean enable)
	{
		panelTwoButton = findViewById(R.id.panel_two_button);
		if(enable)
		{
			panelTwoButton.setVisibility(View.VISIBLE);
			configTwoButtonPanel();
		}
		else
		{
			panelTwoButton.setVisibility(View.GONE);
			configOneButtonPanel();
		}
	}

	public void onClick(View v) {
		dismiss();
		if(v == okButton || v == proceedButton)
		{
			if(onClickOkButtonListener != null) onClickOkButtonListener.onClickOk();
		}
		if(v == closeButton || v == cancelButton)
		{
			if(onClickCloseButtonListener != null) onClickCloseButtonListener.onClickClose();
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
	public void setOnClickCloseButtonListener(
			OnClickCloseButtonListener onClickCloseButtonListener) {
		this.onClickCloseButtonListener = onClickCloseButtonListener;
	}
}
