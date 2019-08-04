package com.oregonscientific.meep.together.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;

public class MyProgressDialog extends ProgressDialog {

	public MyProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setCanceledOnTouchOutside(false);
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Show your Alert Box here
        	return true;
        }
        return false;
    }
	@Override
	public void dismiss() {
		try {
			super.dismiss();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
