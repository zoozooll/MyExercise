package com.oregonscientific.meep.youtube.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.oregonscientific.meep.youtube.R;

public class MenuActionBarFragment extends Fragment{
	
	public static final String TAG = "MenuActionBarFragment";
	EditText edittext;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		View view = inflater.inflate(R.layout.main_menu_actionbar, container, false);
		edittext = (EditText) view.findViewById(R.id.searchBox);
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
