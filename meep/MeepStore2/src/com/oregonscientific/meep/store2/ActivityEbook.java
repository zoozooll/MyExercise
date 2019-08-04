package com.oregonscientific.meep.store2;

import android.app.DialogFragment;
import android.os.Bundle;

import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class ActivityEbook extends GenericStoreActivity {

	public final static String TAG = "STORE_EBOOK";
	DialogFragment newFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ebook);
		
		initUiComponent(EBOOK_MODE);

		type = MeepStoreItem.TYPE_EBOOK;
		
		showItem(currentPage);
//		initListeners();
	}
}