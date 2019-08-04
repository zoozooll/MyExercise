package com.oregonscientific.meep.store2;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class ActivityApp extends GenericStoreActivity {

	public final static String TAG = "STORE_APP";
	DialogFragment newFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_app);

		initUiComponent(APP_MODE);
		type = MeepStoreItem.TYPE_APP;

		showItem(currentPage);
		initListeners();
	}

	public void initListeners() {
		sortCategory.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				newFragment = CategoryDialogFragment.newInstance(CategoryDialogFragment.CATEGORY_APP_DIALOG_ID);
				newFragment.show(getFragmentManager(), "dialog");

				// disable button to prevent duplicate click event
				enableSortingButtons(false);
			}
		});
		
		sortPrice.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				newFragment = CategoryDialogFragment.newInstance(CategoryDialogFragment.PRICE_DIALOG_ID);
				newFragment.show(getFragmentManager(), "dialog");

				// disable button to prevent duplicate click event
				enableSortingButtons(false);
			}
		});

		// startActivity(new
		// Intent(android.provider.Settings.ACTION_APPLICATION_SETTINGS));
	}

}