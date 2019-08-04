package com.beem.project.btf.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.fragment.ContactInfoCheckFragment;
import com.beem.project.btf.ui.fragment.ContactInfoCheckFragment.ContactType;
import com.beem.project.btf.ui.fragment.ContactInfoEditFragment;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

import de.greenrobot.event.EventBus;

/**
 * @func 个人信息界面(以前的VcardInfo写的tmd渣！)
 * @author yuedong bao
 * @time 2015-1-6 下午4:54:23
 */
public class ContactInfoActivity extends VVBaseFragmentActivity implements
		OnClickListener, IEventBusAction {
	private ContactInfoCheckFragment contactCheckFrgmt;
	private ContactInfoEditFragment contactEditFrgmt;
	private CustomTitleBtn back;
	private TextView layout_name;
	private CustomTitleBtn editbutton;
	private ContactFrgmtStatus status;
	private final static String JID = "JID";
	private final static String STATUS = "STATUS";
	private final static String NIKENAME = "NIKENAME";
	private ContactType contactType;
	private Contact contact;
	private boolean isBlackList;
	public static String isInBlacklist = "isInBlacklist";
	public static final int REQUEST_PICK_PHOTO = 1;

	public interface IFragmentTransaction {
		public boolean onBackPressed();
	}

	public enum ContactFrgmtStatus {
		CheckContactInfo, EditContactInfo;
	}

	public static void launch(Context ctx, Contact contact) {
		launch(ctx, contact.getJid(), contact.getNickName(),
				ContactFrgmtStatus.CheckContactInfo);
	}
	public static void launch(Context ctx, String jid, String nikename,
			ContactFrgmtStatus status) {
		/*if (!LoginManager.getInstance().isMyJid(jid)
				&& TextUtils.isEmpty(LoginManager.getInstance().getMyContact()
						.getPhoto())) {
			ContactServiceDlg.CreateTipdialog(ctx, "请先上传头像",
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							launch(v.getContext(), LoginManager.getInstance()
									.getJidParsed(), "我的资料",
									ContactFrgmtStatus.EditContactInfo);
						}
					});
			return;
		}*/
		if (LoginManager.getInstance().isMyJid(jid)) {
			nikename = "我的资料";
		}
		Intent intent = new Intent(ctx, ContactInfoActivity.class);
		intent.putExtra(ContactInfoActivity.JID, VVXMPPUtils.makeJidParsed(jid));
		intent.putExtra(ContactInfoActivity.STATUS, status);
		intent.putExtra(ContactInfoActivity.NIKENAME, nikename);
		ctx.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String jid = getIntent().getStringExtra(ContactInfoActivity.JID);
		String nickName = getIntent().getStringExtra(
				ContactInfoActivity.NIKENAME);
		status = (ContactFrgmtStatus) getIntent().getSerializableExtra(
				ContactInfoActivity.STATUS);
		setContentView(R.layout.fragment_activity_main);
		back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setVisibility(View.VISIBLE);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft();
		layout_name = (TextView) findViewById(R.id.topbar_title);
		layout_name.setText(nickName);
		editbutton = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		editbutton.setViewPaddingRight();
		editbutton.setVisibility(View.GONE);
		back.setOnClickListener(this);
		new VVBaseLoadingDlg<Contact>(new VVBaseLoadingDlgCfg(mContext)
				.setShowWaitingView(true).setCancelable(false).setParams(jid)) {
			private String jid = (String) config.params[0];

			@Override
			protected Contact doInBackground() {
				isBlackList = ContactService.getInstance().blackYet(jid);
				return ContactService.getInstance().getContact(jid);
			}
			@Override
			protected void onPostExecute(Contact result) {
				if (result != null) {
					ContactType contactType;
					if (LoginManager.getInstance().isMyJid(jid)) {
						contactType = ContactType.MySelf;
					} else if (jid.equals(Constants.GENER_NUM)) {
						contactType = ContactType.GenerNum;
					} else {
						contactType = ContactService.getInstance().friendYet(
								jid) ? ContactType.Friend
								: ContactType.Stranger;
					}
					setupRes(result, contactType, isBlackList);
				} else {
					CToast.showToast(mContext, "获取用户信息失败", Toast.LENGTH_SHORT);
				}
			}
		}.execute();
		EventBus.getDefault().register(this);
	}
	private void setupRes(Contact contact, ContactType type, boolean isBlackList) {
		if (contact == null) {
			return;
		}
		this.contact = contact;
		this.contactType = type;
		contactCheckFrgmt = new ContactInfoCheckFragment(contact, contactType);
		contactEditFrgmt = new ContactInfoEditFragment(contact);
		back.setOnClickListener(this);
		editbutton.setOnClickListener(this);
		switchFragment(status);
		if (LoginManager.getInstance().isMyJid(contact.getJid())) {
			editbutton.setVisibility(View.VISIBLE);
		} else {
			editbutton.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(contact.getAlias())) {
			layout_name.setText(contact.getAlias());
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		// 取消异步任务，关闭界面
	}
	public void switchFragment(ContactFrgmtStatus status) {
		//LogUtils.i("switchFragment..." + status);
		this.status = status;
		switchFragmentOpt(status);
	}
	private void switchFragmentOpt(ContactFrgmtStatus status) {
		FragmentManager frgmtManager = getSupportFragmentManager();
		FragmentTransaction frgmtTransaction = frgmtManager.beginTransaction();
		if (status == ContactFrgmtStatus.CheckContactInfo) {
			editbutton.setTextAndImgRes("",
					R.drawable.timefly_profileedit_selector);
			if (frgmtManager.findFragmentByTag("contactCheckFrgmt") == null) {
				//LogUtils.i("~add the checkFragment~");
				frgmtTransaction.add(R.id.id_content, contactCheckFrgmt,
						"contactCheckFrgmt");
				Bundle bundle = new Bundle();
				bundle.putBoolean(ContactInfoActivity.isInBlacklist,
						isBlackList);
				contactCheckFrgmt.setArguments(bundle);
				frgmtTransaction.commit();
			} else {
				frgmtManager.popBackStack();
				//LogUtils.i("~popBackStack~");
			}
		} else {
			if (frgmtManager.findFragmentByTag("contactCheckFrgmt") == null) {
				switchFragmentOpt(ContactFrgmtStatus.CheckContactInfo);
			}
			if (frgmtManager.findFragmentByTag("contactEditFrgmt") == null) {
				//LogUtils.i("~add the editFragment~");
				editbutton.setTextAndImgRes("保存", 0);
				frgmtTransaction.add(R.id.id_content, contactEditFrgmt,
						"contactEditFrgmt");
				frgmtTransaction.addToBackStack(null);
				frgmtTransaction.commit();
			}
		}
	}
	@Override
	public void onClick(View v) {
		if (v == back) {
			onBackPressed();
		} else if (v == editbutton) {
			if (!BeemApplication.isNetworkOk()) {
				CToast.showToast(mContext, "当前处于离线模式，不能编辑个人资料",
						Toast.LENGTH_SHORT);
				return;
			}
			if (status == ContactFrgmtStatus.CheckContactInfo) {
				switchFragment(ContactFrgmtStatus.EditContactInfo);
			} else if (status == ContactFrgmtStatus.EditContactInfo) {
				contactEditFrgmt.modifyContactInfo();
			}
		}
	}
	@Override
	public void onBackPressed() {
		if (status == ContactFrgmtStatus.EditContactInfo) {
			if (contactEditFrgmt != null) {
				contactEditFrgmt.onBackPressed();
				return;
			}
		} else if (status == ContactFrgmtStatus.CheckContactInfo) {
			if (contactCheckFrgmt != null) {
				contactCheckFrgmt.onBackPressed();
				return;
			}
		}
		super.onBackPressed();
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (data.getAction() == EventAction.AliasChange) {
			layout_name.setText(contact.getSuitableName());
		} else if (data.getAction() == EventAction.ModifyContactInfo) {
			// 更改资料后更改本地数据
			switchFragment(ContactFrgmtStatus.CheckContactInfo);
		}
	}
}
