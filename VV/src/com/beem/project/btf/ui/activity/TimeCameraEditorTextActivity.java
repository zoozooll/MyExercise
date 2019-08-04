package com.beem.project.btf.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.CutomerlimitEditText;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.CustomTitleBtn;

import de.greenrobot.event.EventBus;

public class TimeCameraEditorTextActivity extends VVBaseActivity {
	private static final String TAG = "TimeCameraEditorTextActivity";
	private Context mContext;
	private String[] noteinfo;
	private ListView notelist;
	private CutomerlimitEditText limitedit;
	private String CurrentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_camera_editortext);
		mContext = this;
		Intent intent = getIntent();
		if (intent.hasExtra("Text")) {
			noteinfo = intent.getStringArrayExtra("Text");
		} else {
			noteinfo = null;
		}
		CurrentText = intent.getStringExtra("CurrentText");
		notelist = (ListView) findViewById(R.id.notelist);
		if (noteinfo != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
					R.layout.time_camera_editortext_item, R.id.edittext,
					noteinfo);
			notelist.setAdapter(adapter);
		}
		limitedit = (CutomerlimitEditText) findViewById(R.id.limitedit);
		limitedit.setHint("请编辑文字");
		limitedit.setText(CurrentText);
		// 设置导航条
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		CustomTitleBtn btright = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		btright.setText("完成").setImgVisibility(View.GONE).setViewPaddingRight()
				.setVisibility(View.VISIBLE);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setText("文字编辑");
		btBack.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		notelist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView temp = (TextView) arg1.findViewById(R.id.edittext);
				limitedit.setText(temp.getText().toString());
			}
		});
		btright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String temp = limitedit.getText().toString();
				if (TextUtils.isEmpty(temp)) {
					Toast.makeText(mContext, "请输入文字", Toast.LENGTH_SHORT)
							.show();
				} else {
					EventBus.getDefault()
							.post(new EventBusData(
									EventAction.TimeCameraNoteTextChange, temp));
					finish();
				}
			}
		});
	}
	@Override
	public void registerVVBroadCastReceivers() {
		// TODO Auto-generated method stub
	}
}
