package com.iskyinfor.duoduo.ui.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.iskyinfor.duoduo.CallbackTabMenu;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UIPublicConstant;
import com.iskyinfor.duoduo.ui.book.BookShelfActivity;
import com.iskyinfor.duoduo.ui.lesson.LessonMenuTitleAdapter;

public class UpdataPasswordActivity extends Activity implements OnClickListener{
	@SuppressWarnings("unused")
	private Button baseRes, updatePswd, accountMsg;
	@SuppressWarnings("unused")
	private Button backShelf,confitmBtn;
	private ImageView list_menu,back_page;
	@SuppressWarnings("unused")
	private EditText currentPswdEdit,updatePswdEdit,confirmPswdEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myself_updata_pswd);
		initWidget();
		initBottomView(); 
	}

	private void initWidget()
	{
		backShelf = (Button) findViewById(R.id.myself_title_btn); //返回书架
		confitmBtn = (Button) findViewById(R.id.confirm_btn); //确定
		
		baseRes = (Button) findViewById(R.id.myself_base_resource);
		updatePswd = (Button) findViewById(R.id.myself_updata_resource);
		accountMsg = (Button) findViewById(R.id.myself_account_resource);
		
		currentPswdEdit = (EditText) findViewById(R.id.current_pswd_edit);
		updatePswdEdit =(EditText) findViewById(R.id.new_pswd_edit);
		confirmPswdEdit =(EditText) findViewById(R.id.confirm_pswd_edit);
	}
	
	
	private void initBottomView()
	{
		list_menu = (ImageView) findViewById(R.id.duoduo_lesson_list_img);
		list_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				LessonMenuTitleAdapter adapter = new LessonMenuTitleAdapter(UpdataPasswordActivity.this, 
				new String[] {"搜索","删除", "全选","完成" }, 16, 0xFFFFFFFF);
				new CallbackTabMenu(UpdataPasswordActivity.this, adapter, list_menu).setPupopWindow();
			}
		});
		
		back_page = (ImageView) findViewById(R.id.duoduo_lesson_back_img);
		back_page.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				UpdataPasswordActivity.this.finish();
			}
		});
	}
	
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) {
		case R.id.myself_base_resource:
			Intent intent = new Intent(UpdataPasswordActivity.this,MyselfInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.myself_updata_resource:
			Intent intentUpdate = new Intent(UpdataPasswordActivity.this,UpdataPasswordActivity.class);
			startActivity(intentUpdate);
			break;
		case R.id.myself_account_resource:
			Intent intentAccount = new Intent(UpdataPasswordActivity.this,MyselfAccountActivity.class);
			startActivity(intentAccount);
			break;
		case R.id.myself_title_btn: //返回书架
			Intent intentShelf = new Intent(UpdataPasswordActivity.this,BookShelfActivity.class);
			startActivity(intentShelf);
			break;
		case R.id.confirm_btn:  //确定 
			UserInfoTask task = new UserInfoTask(UpdataPasswordActivity.this,
					"0202", currentPswdEdit.getText().toString(), 
					updatePswdEdit.getText().toString(),UIPublicConstant.UPADATE_PSWD_MARK);
			task.execute();
			break;
		default:
			break;
		}		
	}
}