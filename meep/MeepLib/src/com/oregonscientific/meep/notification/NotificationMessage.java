package com.oregonscientific.meep.notification;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.customfont.MyTextView;
import com.osgd.meep.library.R;

public class NotificationMessage extends RelativeLayout {
	
	ImageView imageViewIcon;
	ImageView imageViewQuit;
	TextView textViewMessage;
	TextView textViewTitle;

	public NotificationMessage(Context context, AttributeSet attrs, String title, String message) {
		super(context, attrs);
		
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.notification_message);
		dialog.setCancelable(true); 
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.notification_message, this);
		
//		TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
//		textViewTitle.setText(title);
		
		TextView textViewOkBtn = (TextView) dialog.findViewById(R.id.textViewOkBtn);

		TextView textViewMessage = (TextView) dialog.findViewById(R.id.textViewMessage);
		textViewMessage.setText(message);
		textViewMessage.setVisibility(VISIBLE);
//		
//		ImageView imageViewIcon = (ImageView) dialog.findViewById(R.id.imageViewIcon);
//		imageViewIcon.setImageResource(R.drawable.ic_launcher);

		imageViewQuit = (ImageView) dialog.findViewById(R.id.imageViewQuit);
		//imageViewQuit.setImageResource(R.drawable.quit_room);

		imageViewQuit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View pV) {
				dialog.dismiss();
			}
		});

		textViewOkBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View pV) {
				dialog.dismiss();
			}	
		});
		
		dialog.show();
	}
	
	
	public NotificationMessage(Context context, AttributeSet attrs, String title, String message,final ViewGroup vg) {
		super(context, attrs);
		//2013-7-5 -Amy- popup notice screen, both text and background are white, can't see clearly
		/*final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.notification_message);
		dialog.setCancelable(true); 
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));*/

		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = layoutInflater.inflate(R.layout.notification_message, this);
		
//		TextView textViewTitle = (TextView) dialog.findViewById(R.id.textViewTitle);
//		textViewTitle.setText(title);
		
		TextView textViewOkBtn = (TextView) view.findViewById(R.id.textViewOkBtn);
		
		//2013-4-18 -Amy- add "Notice" 
		MyTextView textViewNotice = (MyTextView) view.findViewById(R.id.textViewNotice);
		textViewNotice.setText(R.string.notice);
		
		TextView textViewMessage = (TextView) view.findViewById(R.id.textViewMessage);
		textViewMessage.setText(message);
		textViewMessage.setVisibility(VISIBLE);
//		
//		ImageView imageViewIcon = (ImageView) dialog.findViewById(R.id.imageViewIcon);
//		imageViewIcon.setImageResource(R.drawable.ic_launcher);

		imageViewQuit = (ImageView) view.findViewById(R.id.imageViewQuit);
		//2013-4-18 -Amy- no need setImageResource
//		imageViewQuit.setImageResource(R.drawable.quit_room);

		imageViewQuit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View pV) {
				vg.removeView(view);
			}
		});

		textViewOkBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View pV) {
				//view.setVisibility(VISIBLE);
				vg.removeView(view);
			}	
		});
		
		//dialog.show();
		vg.addView(view);
	}

}
