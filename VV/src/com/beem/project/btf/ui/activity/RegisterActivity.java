package com.beem.project.btf.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.fragment.RegisterCheckFragment;
import com.beem.project.btf.ui.fragment.RegisterCheckFragment.IFragment2ActivityTransaction;
import com.beem.project.btf.ui.fragment.RegisterEditInfoFragment;
import com.beem.project.btf.ui.views.CustomTitleBtn;

public class RegisterActivity extends VVBaseFragmentActivity implements
		OnClickListener, IFragment2ActivityTransaction {
	private RegisterCheckFragment checkFragment;
	private RegisterEditInfoFragment editInfoFragment;
	private CustomTitleBtn back;
	private RegisterFrgmtStatus status;
	private final static String STATUS = "STATUS";

	public interface IFragmentTransaction {
		public boolean onBackPressed();
	}

	public enum RegisterFrgmtStatus {
		CheckRegister, EditRegisterInfo;
	}

	public static void launch(final Context ctx, RegisterFrgmtStatus status) {
		Intent intent = new Intent(ctx, RegisterActivity.class);
		intent.putExtra(RegisterActivity.STATUS, status);
		ctx.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		status = (RegisterFrgmtStatus) getIntent().getSerializableExtra(
				RegisterActivity.STATUS);
		setContentView(R.layout.activity_register);
		back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setImgResource(R.drawable.bbs_back_selector).setViewPaddingLeft()
				.setVisibility(View.VISIBLE);
		TextView title = (TextView) findViewById(R.id.topbar_title);
		title.setVisibility(View.GONE);
		setupRes(status);
	}
	private void setupRes(RegisterFrgmtStatus status) {
		checkFragment = new RegisterCheckFragment(status);
		editInfoFragment = new RegisterEditInfoFragment(status);
		back.setOnClickListener(this);
		switchFragment(status);
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	public void switchFragment(RegisterFrgmtStatus status) {
		//LogUtils.i("switchFragment..." + status);
		this.status = status;
		switchFragmentOpt(status);
	}
	private void switchFragmentOpt(RegisterFrgmtStatus status) {
		FragmentManager frgmtManager = getSupportFragmentManager();
		FragmentTransaction frgmtTransaction = frgmtManager.beginTransaction();
		if (status == RegisterFrgmtStatus.CheckRegister) {
			back.setText("注册 1/2");
			if (frgmtManager.findFragmentByTag("registerCheckFrgmt") == null) {
				frgmtTransaction.add(R.id.id_content_layout, checkFragment,
						"registerCheckFrgmt");
			} else {
				frgmtTransaction.replace(R.id.id_content_layout, checkFragment);
			}
			frgmtTransaction.commit();
		} else {
			back.setText("注册 2/2");
			frgmtTransaction.replace(R.id.id_content_layout, editInfoFragment);
			frgmtTransaction.commit();
		}
	}
	@Override
	public void onClick(View v) {
		if (v == back) {
			onBackPressed();
		}
	}
	@Override
	public void onBackPressed() {
		if (status == RegisterFrgmtStatus.EditRegisterInfo
				&& editInfoFragment != null) {
			switchFragment(RegisterFrgmtStatus.CheckRegister);
		} else if (status == RegisterFrgmtStatus.CheckRegister) {
			if (checkFragment != null) {
				checkFragment.onBackPressed();
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	public void onSendBundle(RegisterFrgmtStatus status, String phone) {
		switchFragment(status);
		Bundle bundle = new Bundle();
		bundle.putString("phone", phone);
		editInfoFragment.setArguments(bundle);
	}
	//点击屏幕关闭软键盘
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}
}
