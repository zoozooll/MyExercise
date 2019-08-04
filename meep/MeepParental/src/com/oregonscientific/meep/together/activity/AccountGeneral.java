package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.Kid;
import com.oregonscientific.meep.together.library.database.table.TableUser;

public class AccountGeneral extends Activity{

	protected Kid getCurrentKid()
	{
		Gson gson = new Gson();
		
		String kidString = getIntentBundle().getString("kid");
		
		Kid kid = gson.fromJson(kidString, Kid.class);
		
		return kid;
	}
	
	protected TableUser getCurrentUser()
	{
		Gson gson = new Gson();
		
		String parentString = getIntentBundle().getString("parent");
		
		TableUser user = gson.fromJson(parentString, TableUser.class);
		
		return user;
	}
	
	protected Bundle getIntentBundle()
	{
		Bundle bundle = getIntent().getBundleExtra("account");
		return bundle;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.layout_rever_full_to_null_slide,R.anim.layout_rever_null_to_full_slide);
	}
}
