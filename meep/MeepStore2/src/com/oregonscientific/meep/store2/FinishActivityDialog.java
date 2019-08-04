package com.oregonscientific.meep.store2;

import com.oregonscientific.meep.store2.inapp.PurchaseActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FinishActivityDialog extends Dialog implements OnClickListener {
	Button okButton;
	TextView text;
	Context mContext;

	public FinishActivityDialog(Context context,int resource) {
		super(context);
		mContext = context;
		initDialog();
		text.setText(resource);
	}
	public FinishActivityDialog(Context context,String t) {
		super(context);
		mContext = context;
		initDialog();
		text.setText(t);
	}
	
	public void initDialog()
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		if((Activity)mContext instanceof PurchaseActivity)
		{
			setContentView(R.layout.common_message_inapp);
		}
		else{
			setContentView(R.layout.common_message);
			
		}
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		text = (TextView) findViewById(R.id.exchangeCode);
		okButton = (Button) findViewById(R.id.ok);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == okButton)
		{
			if (((Activity)mContext)  instanceof MainActivity) {
				((MainActivity)mContext).finish();
			}
			if((Activity)mContext instanceof PurchaseActivity)
			{
				((PurchaseActivity)mContext).returnCancel();
			}
			dismiss();
		}
	}
}
