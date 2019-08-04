package com.oregonscientific.meep.browser.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.oregonscientific.meep.browser.R;
import com.oregonscientific.meep.browser.WebBrowserActivity;

public class MenuActionBarFragment extends Fragment{
	
	public static final String TAG = "MenuActionBarFragment";
	EditText edittext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		View view = inflater.inflate(R.layout.main_menu_actionbar, container, false);
		edittext = (EditText) view.findViewById(R.id.searchBox);
//		view.findViewById(R.id.btnMenu).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				 WebBrowserActivity.menu.showSecondaryMenu();
//			}
//		});
//		view.findViewById(R.id.btnPrevious).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				toPreviousPage();
//			}
//		});
//		view.findViewById(R.id.btnNext).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				toNextPage();
//			}
//		});
//		view.findViewById(R.id.btnMainPage).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				initMainPage();
//			}
//		});
//		view.findViewById(R.id.btnSearch).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
//		view.findViewById(R.id.btnAddBookmark).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				addBookmark(edittext.getText().toString());
//			}
//		});
		return view;
	}
	
//	public void initMainPage()
//	{
//		
//	}
//	
//	public boolean toPreviousPage()
//	{
//		//firstPage return false;
//		
//		return true;
//	}
//	
//	public boolean toNextPage()
//	{
//		//lastPage return false;
//		
//		return true;
//	}
//	
//	public void reloadCurrentPage()
//	{
//		
//	}
//	
//	public void addBookmark(String url)
//	{
//		
//	}
//	

}
