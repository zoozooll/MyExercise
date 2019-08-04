package com.oregonscientific.meep.together.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;

public class CustomAlertDialog extends Dialog implements OnClickListener {
	Button okButton;
	Button cancelButton;
	TextView text;
	private Context mContext;
	OnOkListener onOkListener;
	public OnOkListener getOnOkListener() {
		return onOkListener;
	}
	public void setOnOkListener(OnOkListener onOkListener) {
		this.onOkListener = onOkListener;
	}

	public interface OnOkListener
	{
		public void onOk();
	}

	public CustomAlertDialog(Context context,int resource) {
		super(context);
		initDialog();
		text.setText(resource);
		mContext = context;
	}
	public CustomAlertDialog(Context context,String t) {
		super(context);
		initDialog();
		text.setText(t);
	}
	
	public void initDialog()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.alert_message);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		text = (TextView) findViewById(R.id.text);
		text.setMovementMethod(new ScrollingMovementMethod());
		okButton = (Button) findViewById(R.id.ok);
		cancelButton = (Button) findViewById(R.id.cancel);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == okButton)
		{
//			if (((Activity)mContext)  instanceof MeepTogetherMainActivity) {
//				((MeepTogetherMainActivity)mContext).clearTimer();
//			}
			if(onOkListener!=null) onOkListener.onOk();
			this.dismiss();
		}
		if (v == cancelButton)
			this.dismiss();
	}
}
