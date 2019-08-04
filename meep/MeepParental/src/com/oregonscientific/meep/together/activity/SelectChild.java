package com.oregonscientific.meep.together.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.adapter.ListAdapterChild;
import com.oregonscientific.meep.together.bean.Kid;
import com.oregonscientific.meep.together.bean.MigrateChild;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.library.rest.listener.OnMigrateChild;

public class SelectChild extends Activity {

	// ====Layout Select====
	ImageButton barLeftSelect;
	ImageButton barRightSelect;
	ImageView childTick;
	// listview
	ListView listKids;
	static ListAdapterChild kidsAdapter;
	ArrayList<HashMap<String, Object>> arraylist_Kids;
	private String sn;

	MyProgressDialog  loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main_child_select);
		loading = UserFunction.initLoading(this);
		initUI();
		arraylist_Kids = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("list_kids");
		sn = getIntent().getStringExtra("sn");
		if (arraylist_Kids != null) {
			initKidsListItem();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		kidsAdapter.notifyDataSetChanged();
	}

	public void initUI() {
		// ===select child===
		barLeftSelect = (ImageButton) findViewById(R.id.barImageButtonBack);
		barRightSelect = (ImageButton) findViewById(R.id.barImageButtonAdd);
		listKids = (ListView) findViewById(R.id.listKids);

		// ======Select Child=====
		barLeftSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		barRightSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// //to add child
				// Intent add = new Intent(SelectChild.this,AddChild.class);
				// startActivity(add);
				createNewChildAccount(sn);
			}
		});
	}

	/**
	 * KID
	 */
	public void initKidsListItem() {
		// arraylist_Kids = new ArrayList<HashMap<String, Object>>();
		kidsAdapter = new ListAdapterChild(this, R.layout.item_child, arraylist_Kids);
		listKids.setAdapter(kidsAdapter);
		listKids.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (childTick != null) {
					childTick.setVisibility(View.GONE);
				}
				childTick = (ImageView) arg1.findViewById(R.id.isCurrentKid);
				childTick.setVisibility(View.VISIBLE);
				// Intent intent = getIntent();
				// intent.putExtra("index", arg2);
				// setResult(RESULT_OK,intent);
				// finish();
				Kid kid = new Gson().fromJson((String) arraylist_Kids.get(arg2).get("kid"), Kid.class);

				loading.show();
				migrateAccount(kid.getUserId());

			}
		});
	}

	public void migrateAccount(String userid) {
		// send request
		UserFunction.getRestHelp().setOnMigrateChild(new OnMigrateChild() {
			
			@Override
			public void onMigrateSuccess() {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				setResult(RESULT_OK, intent);
				finish();
			}
			
			@Override
			public void onMigrateFailure(ResponseBasic r) {
//				UserFunction.popupMessage(R.string.please_retry, SelectChild.this,loading);
				UserFunction.popupResponse(r.getStatus(), SelectChild.this,loading);
			}

			@Override
			public void onMigrateTimeout() {
				UserFunction.popupMessage(R.string.please_retry, SelectChild.this,loading);
			}
		});
		UserFunction.getRestHelp().migrateChild(new MigrateChild(userid, sn));

	}

	public static void refresh() {
		kidsAdapter.notifyDataSetChanged();
	}

	public void createNewChildAccount(String sn) {
		Intent start = new Intent(this, MeepTogetherStartUsingActivity.class);
		if (sn != null) {
			start.putExtra("sn", sn);
		}
		startActivityForResult(start,CREATE_NEW_CHILD_ACCOUNT);
	}
	private static final int CREATE_NEW_CHILD_ACCOUNT =0;
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(loading.isShowing())
		{
			loading.dismiss();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == CREATE_NEW_CHILD_ACCOUNT)
		{
			this.finish();
		}
	}
}
