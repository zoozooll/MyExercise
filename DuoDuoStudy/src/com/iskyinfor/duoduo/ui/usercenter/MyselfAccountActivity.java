package com.iskyinfor.duoduo.ui.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.book.BookShelfActivity;

public class MyselfAccountActivity extends Activity 
{
	private ListView listView = null;
	private Button baseRes, updatePswd, accountMsg;
	private Button backShelf = null;
	private Button orderBookBtn = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.myself_account_activity);
    	initView();
    }

	private void initView() 
	{
		listView = (ListView) findViewById(R.id.user_query_listview);
//		listView.setDivider(null); 可以设置不同的分割线
		AccountAdapter adapter = new AccountAdapter(this);
		listView.setAdapter(adapter);
		
		//基本信息
		baseRes = (Button) findViewById(R.id.myself_base_resource);
		baseRes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MyselfAccountActivity.this,MyselfInfoActivity.class);
				startActivity(intent);
			}
		});
		
		//修改密码
		updatePswd = (Button) findViewById(R.id.myself_updata_resource);
		updatePswd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MyselfAccountActivity.this,UpdataPasswordActivity.class);
				startActivity(intent);
			}
		});

		//账户信息
		accountMsg = (Button) findViewById(R.id.myself_account_resource);
		accountMsg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MyselfAccountActivity.this,MyselfAccountActivity.class);
				startActivity(intent);
			}
		});
		
		//返回书架
		backShelf = (Button) findViewById(R.id.myself_title_btn);
		backShelf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MyselfAccountActivity.this,BookShelfActivity.class);
				startActivity(intent);
			}
		});	
		
		//购买 
		orderBookBtn = (Button) findViewById(R.id.price_recharge_btn);
		orderBookBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(MyselfAccountActivity.this,RushMoneyActivity.class);
				startActivity(intent);
			}
		});
		
	}
}
