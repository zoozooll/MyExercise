package com.idthk.meep.ota.notification;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;

import com.idthk.meep.ota.R;
import com.idthk.meep.ota.ui.MainActivity;
import com.idthk.meep.ota.utility.Constants;
import com.oregonscientific.meep.customdialog.CommonPopup;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickCloseButtonListener;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;

public class NotificationOtaActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty_layout);
		
		String versionName = getIntent().getStringExtra(Constants.KEY_TITLE);
		String message = String.format(getResources().getString(R.string.ota_available_message),versionName);
////		((TextView)findViewById(R.id.title)).setText(title);
//		((TextView)findViewById(R.id.text)).setText(message);
//		((Button)findViewById(R.id.btnProceed)).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				startOTAapp();
//			}
//		});
//		((Button)findViewById(R.id.btnCancel)).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
		CommonPopup cp = new CommonPopup(this,R.string.ota,message);
		cp.setEnableTwoButtonPanel(true);
		cp.setOnClickOkButtonListener(new OnClickOkButtonListener() {
			
			@Override
			public void onClickOk() {
				startOTAapp();
			}
		});
		cp.setOnClickCloseButtonListener(new OnClickCloseButtonListener() {
			
			@Override
			public void onClickClose() {
				finish();
			}
		});
		cp.show();
	}

	public void startOTAapp()
	{
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}

}
