package com.iskyinfor.duoduo.ui.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.ConsoleEditText;
import com.iskyinfor.duoduo.ui.IndexActivity;
import com.iskyinfor.duoduo.ui.UIPublicConstant;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.BookFavoriteActivity;
import com.iskyinfor.duoduo.ui.shop.OrderListActivity;
import com.iskyinfor.duoduo.ui.shop.ShoppingCartActivity;
import com.iskyinfor.duoduo.ui.shop.task.BookFavoriteTask;
import com.iskyinfor.duoduo.ui.shop.task.OrderListTask;
import com.iskyinfor.duoduo.ui.shop.task.ShoppingCartTask;
/**
 * 充值页面
 * 
 * @author zhoushidong
 * 
 */
public class RushMoneyActivity extends Activity implements OnClickListener{
	private TextView balanceText = null;
	private Button orderCarBtn, collectBtn, orderMenuBtn, myAcountBtn,shopIndexBtn;
	private EditText editAccount,editPassword;
	private Button payBtn = null;
	private CheckBox checkBox = null;
	private SharedPreferences preference = null;
	public boolean flag = false;
	private TextView tvwWrongToast;
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) {
			case UIPublicConstant.USER_BALANCE:
				int blance = (Integer) msg.obj;
				Log.i("yyj","current money is =========== >>>>>> " + blance);
				balanceText.setText(blance);
				break;

			default:
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rushmoney_activity);
		initView();
		setEvent();
		
		preference =  getSharedPreferences(UIPublicConstant.UserInfo, MODE_PRIVATE);
		UserInfoTask balanceTask = new UserInfoTask(this, editAccount.getText().toString(),UIPublicConstant.QUERY_ALLUSER_MARK, mHandler);
		balanceTask.execute();
	}

	

	private void initView() 
	{
		balanceText = (TextView) findViewById(R.id.balanceValue);
		
		editAccount = (EditText) findViewById(R.id.editAccount);
		editPassword = (EditText) findViewById(R.id.editPassword);
		/**
		 * 控制输入的数字的长度
		 */
		ConsoleEditText.ConsoleEditTextMethod(editPassword);
		tvwWrongToast = (TextView) findViewById(R.id.tvwWrongToast);
		orderCarBtn = (Button) findViewById(R.id.btn_shopping_cart);
		collectBtn = (Button) findViewById(R.id.btn_favorite);
		orderMenuBtn = (Button) findViewById(R.id.btn_order);
		myAcountBtn = (Button) findViewById(R.id.btn_account);
		shopIndexBtn = (Button) findViewById(R.id.btn_shop_index);
		
		myAcountBtn.setBackgroundResource(R.drawable.btn_blue_selector);
		
		//判断状态
		checkBox = (CheckBox) findViewById(R.id.othersRushValue);
		editAccount.setText(UiHelp.getUserShareID(RushMoneyActivity.this));
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					flag = preference.getBoolean("isChecked", isChecked);
					editAccount.setEnabled(isChecked);
					editAccount.setFocusable(isChecked);
					isChecked = true;
				}
				else
				{
					flag = preference.getBoolean("isChecked", isChecked);
					editAccount.setEnabled(false);
					editAccount.setText(UiHelp.getUserShareID(RushMoneyActivity.this));
					editAccount.setFocusable(isChecked);
				}
			}
		});
		
		//充值
		payBtn = (Button) findViewById(R.id.btnRushMoney);
		payBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if(editPassword.getText().length() ==24)
				{
					UserInfoTask task = new UserInfoTask(RushMoneyActivity.this,
							editAccount.getText().toString(),
							editPassword.getText().toString(),
							flag,UIPublicConstant.PAYFOR_MONEY_MARK,
							mHandler);
					task.execute();
					tvwWrongToast.setVisibility(View.GONE);
				}else
				{
					Toast.makeText(RushMoneyActivity.this, "输入的密码有误，请输入24位", Toast.LENGTH_SHORT).show();
					tvwWrongToast.setVisibility(View.VISIBLE);
				}
			}
		});
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(this);
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
	}
	
	private void setEvent() {
		orderCarBtn.setOnClickListener(this);
		collectBtn.setOnClickListener(this);
		orderMenuBtn.setOnClickListener(this);
		myAcountBtn.setOnClickListener(this);
		shopIndexBtn.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId()) 
		{
		case R.id.btn_shopping_cart:
			Intent intent = new Intent(RushMoneyActivity.this,ShoppingCartActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_favorite:
			Intent intent2 = new Intent(RushMoneyActivity.this,BookFavoriteActivity.class);
			startActivity(intent2);
			break;
		case R.id.btn_order:
			Intent intent3 = new Intent(RushMoneyActivity.this,OrderListActivity.class);
			startActivity(intent3);
			break;
		case R.id.btn_account:
			Intent intent4 = new Intent(RushMoneyActivity.this,RushMoneyActivity.class);
			startActivity(intent4);
			break;
		case R.id.btn_shop_index:
			Intent intent5 = new Intent(RushMoneyActivity.this,IndexActivity.class);
			startActivity(intent5);
			break;
		case R.id.duoduo_lesson_back_img:
			finish();
			break;
		default:
			break;
		}		
	}
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}


}
