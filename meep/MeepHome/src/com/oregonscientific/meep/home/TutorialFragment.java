/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * A Fragment class for Tutorial with first launching.
 * Handling receive the layout resource id in bundle and create the fragment 
 * @author joyaether
 *
 */
public class TutorialFragment extends Fragment{
	
	public final static String KEY_RESOURCES_ID = "resId";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		
		if (bundle == null) {
			return null;
		}
		
		int resourceId = bundle.getInt(KEY_RESOURCES_ID, 0);
		View v = inflater.inflate(resourceId, container, false);
		//disable the touch event with this fragment
		
		v.findViewById(R.id.back_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((TutorialActivity) getActivity()).goToNextPage(false);
			}
			
		});
		
		v.findViewById(R.id.next_button).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((TutorialActivity) getActivity()).goToNextPage(true);
			}
			
		});
		return v;	
	}


}
