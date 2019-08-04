package com.oregonscientific.meep.store2;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.oregonscientific.meep.store2.adapter.ListAdapterShelf;
import com.oregonscientific.meep.store2.ctrl.RestRequest.SearchItemListener;
import com.oregonscientific.meep.store2.custom.scrollview.FlowLayout;
import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class SearchResult extends GenericStoreActivity {

	private String searchtext;
	
	private TextView mNumberOfResult;

	public final static String TAG = "STORE_SEARCH";
	
	private boolean isPreviousPage = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_results);
		
		initUiComponent(SEARCH_MODE);
		
		btnSearch.setEnabled(false);
		searchtext = getIntent().getExtras().getString("keywords");
		searchText = (EditText) findViewById(R.id.searchtext);
		mNumberOfResult = (TextView) findViewById(R.id.numberOfResult);
		mNumberOfResult.setText("");

		if (searchtext != null) {
			TextView searchText = (TextView) findViewById(R.id.searchtext);
			searchText.setText(searchtext);
		}
		
		showItem(currentPage);
		initListeners();
	}

	public void showItem(final int page) {
		// Reset
		mNumberOfResult.setText("");
		
		// show items
		if (searchtext != null) {
			if (mApp.isNetworkAvailable(getApplicationContext())) {
				handler.sendEmptyMessage(4);
				// show items
				mApp.getRestRequest().searchItem(searchtext, page, ITEM_PER_PAGE, mApp.getUserToken(), null);
				mApp.getRestRequest().setSearchItemListener(new SearchItemListener() {
	
					@Override
					public void onSearchItemReceived(int code, String msg, ArrayList<MeepStoreItem> itemList, int total) {
						switch (code) {
						case 200:
							//2013-03-19 - raymond - fix search feature not work
							isPreviousPage = currentPage > page;
							currentPage = page;
							
							updateShelfUIByResults(total);
							mNumberOfResult.setText(getResources().getQuantityString(R.plurals.number_of_results, total, total));
							addItemsToShelf(itemList);
							break;
						case 999:
							handler.sendEmptyMessage(5);
							break;
						default:
							break;
						}
						left.setEnabled(true);
						right.setEnabled(true);
						btnSearch.setEnabled(true);
						
						if (popupFragment != null)
							popupFragment.dismiss();
					}
	
				});
			} else {
				handler.sendEmptyMessage(3);
			}
		}
	}

	public void initListeners() {
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setEnabled(false);
				performSearch();

			}
		});
		searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
					// get search text
					performSearch();
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	protected void toResultPage(String text) {
		// refresh Result page
		currentPage = 0;
		showItem(currentPage);
	}
}