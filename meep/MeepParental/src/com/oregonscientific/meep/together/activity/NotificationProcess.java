package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.Kid;
import com.oregonscientific.meep.together.bean.Notification;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.library.rest.listener.RequestRespondedListener;

public class NotificationProcess extends Activity{

	//===Request Measure
	//button
	ImageButton barLeftBack;
	Button btnApprove;
	Button btnReject;
	//text
	TextView textRequest;
	TextView kidName;
	ImageView kidIcon;
	
	MyProgressDialog loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
		setContentView(R.layout.layout_main_notification_measure);
		loading = UserFunction.initLoading(this);
		initUI();
	}
	
	public void initUI()
	{

    	barLeftBack = (ImageButton)findViewById(R.id.mainImageButtonBack);
    	kidName =(TextView) findViewById(R.id.KidName);
    	kidIcon =(ImageButton) findViewById(R.id.user_icon);
    	textRequest =(TextView) findViewById(R.id.textRequest);
    	btnApprove =(Button) findViewById(R.id.btnApprove);
    	btnReject =(Button) findViewById(R.id.btnReject);
    	
    	kidName.setText(String.format(getResources().getString(R.string.main_page_notification_after), getCurrentKid().getName()));
    	UserFunction.loadImage(kidIcon,getCurrentKid().getAvatar());
    	
    	String message = getCurrentNotification().getMessage();
    	String type = getCurrentNotification().getType();
    	if (Notification.S_TYPE_GOOGLEPLAY_DOWNLOAD.equals(type)) {
			message = String.format(getResources().getString(R.string.noti_googleplay),MeepTogetherMainActivity.currentKidName,message);
		} else if (Notification.S_TYPE_STORE_PURCHASE.equals(type)) {
			message = String.format(getResources().getString(R.string.noti_store),MeepTogetherMainActivity.currentKidName,message);
		} else if (Notification.S_TYPE_COIN_REQUEST.equals(type)) {
			message = String.format(getResources().getString(R.string.noti_request_coins),MeepTogetherMainActivity.currentKidName);
		} else if (Notification.S_TYPE_FRIEND_REQUEST.equals(type)) {
//			message = String.format(getResources().getString(R.string.noti_add_friend),UserFunction.filterFirstName(message),UserFunction.filterSecondName(message));
			String requester = getCurrentNotification().getRequester();
			String requestee = getCurrentNotification().getRequestee();
			message = String.format(getResources().getString(R.string.noti_add_friend),requester,requestee);
		}
    	textRequest.setText(message);
    	
		btnApprove.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setButtonEnable(false);
				loading.show();
				if(!UserFunction.isNetworkAvailable(getApplicationContext()))
		    	{
		    		UserFunction.popupMessage(R.string.no_network,NotificationProcess.this,loading);
		    		setButtonEnable(true);
		    	} else {
					//send approve notification
					updateNotification(Notification.S_APPROVAL_APPROVE);
		    	}
				
			}
		});
		btnReject.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setButtonEnable(false);
				if(!UserFunction.isNetworkAvailable(getApplicationContext()))
		    	{
		    		UserFunction.popupMessage(R.string.no_network,NotificationProcess.this,loading);
		    		setButtonEnable(true);
		    	} else {
		    		updateNotification(Notification.S_APPROVAL_REJECT);
		    	}
			}
		});
		barLeftBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
	
	private Notification getCurrentNotification()
	{
		Gson gson = new Gson();
		
		String notificationString = getIntentBundle().getString("notification");
		
		Notification notification = gson.fromJson(notificationString, Notification.class);
		
		return notification;
	}
	
	private Kid getCurrentKid()
	{
		Gson gson = new Gson();
		
		String kidString = getIntentBundle().getString("kid");
		
		Kid kid = gson.fromJson(kidString, Kid.class);
		
		return kid;
	}
	
	private String convertReturnNotification(Notification n)
	{
		Gson gson = new Gson();
		
		return gson.toJson(n);
	}
	
	private Bundle getIntentBundle()
	{
		Bundle bundle = getIntent().getBundleExtra("request");
		return bundle;
	}
	
	public void updateNotification(final String approval)
	{
		//send reject notification
		UserFunction.getRestHelp().respondNotification(approval.toLowerCase(), getCurrentNotification().getId());
		UserFunction.getRestHelp().setResponseListener(new RequestRespondedListener() {
			
			@Override
			public void onRespondSuccess(ResponseBasic r) {
				setButtonEnable(true);
				Intent i = getIntent();
				
				//update notification
				Notification n = getCurrentNotification();
				n.setApproval(approval);
				String notificationString = convertReturnNotification(n);
				
				//add into return intent
				Bundle bundle = getIntentBundle();
				bundle.putString("update-notification", notificationString);
				i.putExtra("result", bundle);
				
				//set result
				NotificationProcess.this.setResult(RESULT_OK,i);
				NotificationProcess.this.finish();
			}
			
			@Override
			public void onRespondFailure(ResponseBasic r) {
				UserFunction.popupResponse(r.getStatus(), NotificationProcess.this, loading);
				setButtonEnable(true);
			}

			@Override
			public void onRespondTimeout() {
				UserFunction.popupMessage(R.string.please_retry, NotificationProcess.this, loading);
				setButtonEnable(true);
			}
		});
	}
	
	public static String filterFriendName(String message)
	{
		String friendName = message.substring(message.indexOf('\'')+1, message.lastIndexOf('\''));
		return friendName;
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.layout_rever_full_to_null_slide,R.anim.layout_rever_null_to_full_slide);
	}
	public void setButtonEnable(boolean enable)
	{
		if(enable)
		{
			loading.dismiss();
		}
		btnApprove.setEnabled(enable);
		btnReject.setEnabled(enable);
	}
	
	
}
