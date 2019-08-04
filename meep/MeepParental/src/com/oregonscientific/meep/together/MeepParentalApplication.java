package com.oregonscientific.meep.together;


import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.library.database.DatabaseHelper;

import android.app.Application;

public class MeepParentalApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		UserFunction.setDataHelp(new DatabaseHelper(this));
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		UserFunction.disableDataHelp();
	}
	
	
}
