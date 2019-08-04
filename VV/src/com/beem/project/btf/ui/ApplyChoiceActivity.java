package com.beem.project.btf.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.OtherTimeFlyMain;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.AddRosterPacket.Operation;
import com.btf.push.Item;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.db.ormhelper.bean.FriendRequest;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.vv.utils.CToast;

import de.greenrobot.event.EventBus;

public class ApplyChoiceActivity extends VVBaseActivity implements
		OnClickListener {
	public static final String KEY_APPLY_FRIEND_TIMESTAMP = "KEY_APPLY_FRIEND_TIMESTAMP";
	public static final String KEY_APPLY_FRIEND_MESSAGE = "KEY_APPLY_FRIEND_message";
	public static final String KEY_APPLY_FRIEND_FROMJID = "KEY_APPLY_FRIEND_fromJid";
	public static final String KEY_APPLY_FRIEND_STATUS = "KEY_APPLY_FRIEND_status";
	private LinearLayout agree;
	private LinearLayout refuse;
	private Intent intent;
	private CustomTitleBtn back;
	private Contact mContact;
	private TextView name;
	private ImageView photo;
	private LinearLayout scroll_child;
	private TextView profile_iv_sexAge;
	private TextView layout_distance_tv;
	private TextView layout_loginTime_tv;
	private TextView layout_signature_tv;
	private ImageView vvzone;
	//	private Item item;
	private View apply_choice;
	private TextView agree_tv;
	private TextView refuse_tv;
	private TextView tvw_applyResult;
	// Extras messsages
	private String timeStamp;
	private String message;
	private String fromJid;
	private int status;

	public static void launch(Activity context, Item item, int requestCode) {
		Intent intent = new Intent(context, ApplyChoiceActivity.class);
		intent.putExtra("choice", item);
		context.startActivityForResult(intent, requestCode);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfriend_apply);
		photo = (ImageView) findViewById(R.id.avatar);
		profile_iv_sexAge = (TextView) findViewById(R.id.profile_tv_agenew);
		layout_distance_tv = (TextView) findViewById(R.id.profile_tv_distance);
		layout_loginTime_tv = (TextView) findViewById(R.id.profile_tv_time);
		layout_signature_tv = (TextView) findViewById(R.id.contactlistmsgperso);
		vvzone = (ImageView) findViewById(R.id.vvzone);
		name = (TextView) findViewById(R.id.contactlistpseudo);
		apply_choice = findViewById(R.id.apply_choice);
		agree = (LinearLayout) findViewById(R.id.agree);
		refuse = (LinearLayout) findViewById(R.id.refuse);
		agree_tv = (TextView) agree.findViewById(R.id.agree_tv);
		refuse_tv = (TextView) refuse.findViewById(R.id.refuse_tv);
		back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		tvw_applyResult = (TextView) findViewById(R.id.tvw_applyResult);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		scroll_child = (LinearLayout) findViewById(R.id.scroll_child);
		scroll_child.setVisibility(View.GONE);
		CustomTitleBtn back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		TextView apply_msg = (TextView) findViewById(R.id.apply_msg);
		TextView contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		contacts_textView2.setVisibility(View.GONE);
		getExtras();
		apply_msg.setText(message);
		agree.setOnClickListener(this);
		refuse.setOnClickListener(this);
		showStatus(status);
		new VVBaseLoadingDlg<Contact>(
				new VVBaseLoadingDlgCfg(mContext).setShowWaitingView(true)) {
			@Override
			protected Contact doInBackground() {
				return ContactService.getInstance().getContact(fromJid);
			}
			@Override
			protected void onPostExecute(Contact result) {
				super.onPostExecute(result);
				initViewState(result);
			}
		}.execute();
	}
	private void getExtras() {
		intent = getIntent();
		timeStamp = intent.getStringExtra(KEY_APPLY_FRIEND_TIMESTAMP);
		message = intent.getStringExtra(KEY_APPLY_FRIEND_MESSAGE);
		fromJid = intent.getStringExtra(KEY_APPLY_FRIEND_FROMJID);
		status = intent.getIntExtra(KEY_APPLY_FRIEND_STATUS, 0);
	}
	// 初始化界面状态
	private void initViewState(Contact pContact) {
		this.mContact = pContact;
		name.setText(mContact.getSuitableName());
		mContact.displayPhoto(photo);
		scroll_child.setVisibility(View.VISIBLE);
		profile_iv_sexAge.setSelected(mContact.getSexInt() == 0);
		profile_iv_sexAge.setText(BBSUtils.getAgeByBithday(mContact.getBday()));
		layout_distance_tv.setText(LoginManager.getInstance().latlon2Distance(
				mContact.getLat(), mContact.getLon()));
		layout_loginTime_tv.setText(mContact.getLoginTimeRlt());
		layout_signature_tv.setText("时光号：" + mContact.getJIDParsed());
		vvzone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OtherTimeFlyMain.launch(mContext, mContact);
			}
		});
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContactInfoActivity.launch(mContext, mContact);
			}
		});
	}
	private void showStatus(int showStatus) {
		switch (showStatus) {
			case 0: // 请求，未同意
			{
				apply_choice.setVisibility(View.VISIBLE);
				tvw_applyResult.setVisibility(View.GONE);
			}
				break;
			case 1: // 已同意
			{
				apply_choice.setVisibility(View.GONE);
				tvw_applyResult.setVisibility(View.VISIBLE);
				tvw_applyResult.setText(getResources().getString(
						R.string.friend_apply,
						getResources().getString(
								R.string.friend_accessed_message)));
			}
				break;
			case 2: // 已拒绝
			{
				apply_choice.setVisibility(View.GONE);
				tvw_applyResult.setVisibility(View.VISIBLE);
				tvw_applyResult.setText(getResources().getString(
						R.string.friend_apply,
						getResources().getString(
								R.string.friend_refused_message)));
			}
				break;
			default:
				break;
		}
	}
	@Override
	public void onClick(final View v) {
		if (v == agree || v == refuse) {
			Operation operation = v == agree ? Operation.agree
					: Operation.refuse;
			ContactServiceDlg.executeContactOprt(mContext, mContact.getJid(),
					null, operation,
					new ContactService.onPacketResult<PacketResult>() {
						@Override
						public void onResult(PacketResult result,
								boolean timeout, Start start) {
							if (result != null && result.isOk()) {
								if (v == agree) {
									status = 1;
								} else if (v == refuse) {
									status = 2;
								}
								showStatus(status);
								doApplyResult();
							} else {
								CToast.showToast(ApplyChoiceActivity.this,
										"操作失败", Toast.LENGTH_SHORT);
							}
						}
					});
		} else if (v == back) {
			//			setResult(RESULT_OK, intent);
			finish();
		}
	}
	
	private void doApplyResult() {
		ThreadUtils.executeTask(new Runnable() {
			public void run() {
				FriendRequest fr = new FriendRequest();
				fr.setJidFrom(fromJid);
				fr.setJidTo(LoginManager.getInstance().getJidParsed());
				fr.setStatus(status);
				fr.saveToDatabase();
				if (status == 1)
					ContactService.getInstance().insertFriendInto(fromJid);
			}
		});
	}
	@Override
	public void onBackPressed() {
		back.performClick();
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
