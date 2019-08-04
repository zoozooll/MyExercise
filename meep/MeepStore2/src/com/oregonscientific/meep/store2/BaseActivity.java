package com.oregonscientific.meep.store2;

import com.oregonscientific.meep.store2.global.MeepStoreApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class BaseActivity extends Activity{
	protected MeepStoreApplication mApp;
	protected PopUpDialogFragment popupFragment;
	protected EditText searchText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = (MeepStoreApplication) this.getApplicationContext();
	}
	
	public void performSearch() {
		// get search text
		String text = getSearchText();
		if (text != null) {
			if (mApp.containsBadwords(text)) {
				new CommonDialog(this, R.string.this_word_is_blocked).show();
			} else {
				toResultPage(text);
			}
		}
		else
		{
			showEmptySerach();
			return;
		}
	}
	
	protected void toResultPage(String text)
	{
		// to Result page
		Intent intent = new Intent();
		intent.setClass(this, SearchResult.class);
		intent.putExtra("keywords", text);
		startActivity(intent);
		searchText.setText("");
	}

	public String getSearchText() {
		String text = null;
		if (searchText != null) {
			text = searchText.getText().toString();
			if (text != null && text.trim().equals("")) {
				text = null;
			}
		}
		return text;
	}
	private void showEmptySerach() {
		popupFragment.dismiss();
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.EMPTY_SEARCH);
		popupFragment.show(getFragmentManager(), "dialog");
	}
}
