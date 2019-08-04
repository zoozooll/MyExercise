package com.aaron.filter;

import com.aaron.filter.shade.ShadeView;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new ShadeView(this));
	}

	
}
