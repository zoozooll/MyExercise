package com.oregonscientific.meep.meepopenbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxViewManager;

/**
 * Activity for Greetings page
 * @author Charles
 *
 */
public class MeepOpenBoxGreetings extends MeepOpenBoxBaseActivity implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_box_greetings_layout);
		
		Button backButton = (Button) findViewById(R.id.greetingsBackBtn);
		backButton.setOnClickListener(this);
		Button nextButton = (Button) findViewById(R.id.greetingsNextBtn);
		nextButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.greetingsBackBtn) {
			onBackPressed();
		}
		if (view.getId() == R.id.greetingsNextBtn) {
			MeepOpenBoxViewManager.goToNextPage(MeepOpenBoxGreetings.this);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}
	
	@Override
	public void hideBackButton() {
		Button backButton = (Button) findViewById(R.id.greetingsBackBtn);
		backButton.setVisibility(View.INVISIBLE);
		TextView backButtonText = (TextView) findViewById(R.id.greetingsBackText);
		backButtonText.setVisibility(View.INVISIBLE);
	}
	
}