package com.oregonscientific.meep.together.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.oregonscientific.meep.together.R;

public class MenuFragment extends Fragment {

	public static final String TAG = "MenuFragment";
	// button
	Button menuNotification;
	Button menuAccount;
	Button menuCoins;
	Button menuParental;
	Button menuGooglePlay;
	Button menuLogout;
	// image
	ImageButton image;
	Button btnTerms;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		View view = inflater.inflate(R.layout.layout_main_menu, container, false);
		menuNotification = (Button) view.findViewById(R.id.menuNotification);
		menuAccount = (Button) view.findViewById(R.id.menuAccount);
		menuCoins = (Button) view.findViewById(R.id.menuCoins);
		menuParental = (Button) view.findViewById(R.id.menuParental);
		menuGooglePlay = (Button) view.findViewById(R.id.menuGooglePlay);
		menuLogout = (Button) view.findViewById(R.id.menuLogout);

		btnTerms = (Button) view.findViewById(R.id.btnTerms);
		image = (ImageButton) view.findViewById(R.id.user_icon);
		
		menuNotification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuNotification();
			}
		});
		menuAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuAccount();
			}
		});
		menuParental.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuParental();
			}
		});
		menuCoins.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuCoins();
			}
		});
		menuGooglePlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuGooglePlay();
			}
		});
		menuLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuLogout();
			}
		});
		
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickUserIcon();
				
			}
		});
		btnTerms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MeepTogetherMainActivity)getActivity()).clickMenuTerm();
			}
		});
		
		return view;
	}

	public void loadImage(String url) {
		UserFunction.loadImage(image, url);
		
	}

}
