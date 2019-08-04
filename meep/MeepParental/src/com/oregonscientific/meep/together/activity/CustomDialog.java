package com.oregonscientific.meep.together.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;
import com.oregonscientific.meep.together.R;

public class CustomDialog extends Dialog implements OnClickListener {
	Button okButton;
	TextView text;
	public interface OnClickOkButtonListener {
		public void onClickOk();
	}
	OnClickOkButtonListener onClickOkButtonListener = null;

	public CustomDialog(Context context,int resource) {
		super(context);
		initDialog();
		text.setText(resource);
	}
	public CustomDialog(Context context,String t) {
		super(context);
		initDialog();
		text.setText(t);
	}
	
	public void initDialog()
	{
		onClickOkButtonListener = null;
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.common_message);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		text = (TextView) findViewById(R.id.text);
		text.setMovementMethod(new ScrollingMovementMethod());
		okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == okButton)
		{
			dismiss();
			if(onClickOkButtonListener!=null) onClickOkButtonListener.onClickOk();
		}
	}
	public void setOnClickOkButtonListener(
			OnClickOkButtonListener onClickOkButtonListener) {
		this.onClickOkButtonListener = onClickOkButtonListener;
	}
}
