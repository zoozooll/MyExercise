package com.oregonscientific.meep.store2;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class ActivityGame extends GenericStoreActivity {

	public final static String TAG = "STORE_GAME";
	DialogFragment newFragment;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_game);

		initUiComponent(GAME_MODE);

		type = MeepStoreItem.TYPE_GAME;

		showItem(currentPage);
		initListeners();
	}

	public void initListeners() {
		sortCategory.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				newFragment = CategoryDialogFragment.newInstance(CategoryDialogFragment.CATEGORY_GAME_DIALOG_ID);
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

	}

}