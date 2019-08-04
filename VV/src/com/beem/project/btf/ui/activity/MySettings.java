package com.beem.project.btf.ui.activity;

import java.io.File;
import java.util.Map;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.CheckBoxTwoView;
import com.beem.project.btf.bbs.view.RadioButtonThreeView;
import com.beem.project.btf.bbs.view.RadioButtonThreeView.CheckedButtonThreeListener;
import com.beem.project.btf.bbs.view.RadioButtonTwoView;
import com.beem.project.btf.bbs.view.RadioButtonTwoView.CheckedListener;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DataCleanManager;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.butterfly.vv.AboutUsActivity;
import com.butterfly.vv.camera.Utils;
import com.butterfly.vv.vv.utils.CToast;

import de.greenrobot.event.EventBus;

public class MySettings extends VVBaseActivity {
	private View layout_accountSettings,layout_upload_zip, layout_localSettings;
	private TextView account_pwd, blacklist, uploadpath_list_preference, tvw_uploadZip,
			notification, uploadpath_list_clearcachetime, clear_cache_left,
			about_screen_preference, card_info_name, card_info_note;
	private Button exit_screen_preference;
	private BBSCustomerDialog blurDlg;
	private ImageView headimage;
	private Contact contact;
	private ViewGroup card_info_wraper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mysettings);
		EventBus.getDefault().register(this);
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setVisibility(View.GONE);
		btBack.setTextAndImgRes("设置", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(settingListener);
		layout_accountSettings = findViewById(R.id.layout_accountSettings);
//		layout_upload_zip = findViewById(R.id.layout_upload_zip);
		layout_localSettings = findViewById(R.id.layout_localSettings);
		card_info_wraper = (ViewGroup) findViewById(R.id.card_info_wraper);
		headimage = (ImageView) findViewById(R.id.card_info);
		account_pwd = (TextView) findViewById(R.id.account_pwd);
		blacklist = (TextView) findViewById(R.id.blacklist);
		uploadpath_list_preference = (TextView) findViewById(R.id.uploadpath_list_preference);
		notification = (TextView) findViewById(R.id.notification);
		uploadpath_list_clearcachetime = (TextView) findViewById(R.id.uploadpath_list_clearcachetime);
		clear_cache_left = (TextView) findViewById(R.id.clear_cache_left);
		about_screen_preference = (TextView) findViewById(R.id.about_screen_preference);
		exit_screen_preference = (Button) findViewById(R.id.exit_screen_preference);
		card_info_name = (TextView) findViewById(R.id.card_info_name);
		card_info_note = (TextView) findViewById(R.id.card_info_note);
//		tvw_uploadZip = (TextView) findViewById(R.id.tvw_uploadZip);
		// 设置各条目监听器
		account_pwd.setOnClickListener(settingListener);
		blacklist.setOnClickListener(settingListener);
		uploadpath_list_preference.setOnClickListener(settingListener);
		notification.setOnClickListener(settingListener);
		uploadpath_list_clearcachetime.setOnClickListener(settingListener);
		clear_cache_left.setOnClickListener(settingListener);
		about_screen_preference.setOnClickListener(settingListener);
		exit_screen_preference.setOnClickListener(settingListener);
		card_info_wraper.setOnClickListener(settingListener);
//		tvw_uploadZip.setOnClickListener(settingListener);
		showing();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			  Uri uri = data.getData(); 
			  String path = uri.getPath();
			  if (!path.endsWith("zip")) {
				  CToast.showToast(this, "请选择zip文件", Toast.LENGTH_SHORT);
				  return;
			  }
			  File file = new File(path);
			  UploadUtil.uploadFile(file, null);
		}
	}
	private void showing() {
		if (LoginManager.getInstance().isLogined()) {
			String name = null;
			contact = LoginManager.getInstance().getMyContact();
			if (contact != null) {
				try {
					name = contact.getSuitableName();
				} catch (IllegalArgumentException e) {
					LogUtils.w(e.toString());
				}
			}
			// It has logged in
			layout_accountSettings.setVisibility(View.VISIBLE);
//			layout_upload_zip.setVisibility(View.VISIBLE);
			contact.displayPhoto(headimage);
			card_info_name.setText(name);
			card_info_note.setText(contact.getSignature());
			exit_screen_preference.setText(R.string.settings_logout);
		} else {
			// It has not logged in
			layout_accountSettings.setVisibility(View.GONE);
//			layout_upload_zip.setVisibility(View.GONE);
			card_info_name.setText(R.string.timefly_unlogin);
			exit_screen_preference.setText(R.string.settings_login);
		}
	}

	private OnClickListener settingListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.card_info_wraper: {
					if (LoginManager.getInstance().isLogined()) {
						ContactInfoActivity.launch(mContext, contact);
					} else {
						ActivityController.getInstance().gotoLogin();
					}
					break;
				}
				case R.id.account_pwd: {
					// 修改密码
					Intent intent = new Intent(MySettings.this,
							ChangePassWordActivity.class);
					startActivity(intent);
					break;
				}
				case R.id.blacklist: {
					// 黑名单
					Intent intent = new Intent(MySettings.this,
							BlacklistActivity.class);
					intent.setAction(Constants.ACTION_BLACKROSTER);
					startActivity(intent);
					break;
				}
				case R.id.uploadpath_list_preference: {
					// 上传图片默认属性设置
					// 防止出现多个对话框
					if (blurDlg != null && blurDlg.isShowing()) {
						blurDlg.dismiss();
					}
					blurDlg = BBSCustomerDialog.newInstance(mContext,
							R.style.blurdialog);
					// 填充三个单选按钮
					final RadioButtonThreeView buttonThreeView = new RadioButtonThreeView(
							mContext);
					// 填充默认值
					String status = SharedPrefsUtil.getValue(mContext,
							SettingKey.album_auhtority, AlbumAuthority.all);
					buttonThreeView.setData(status);
					buttonThreeView
							.setCheckedListener(new CheckedButtonThreeListener() {
								@Override
								public void check(String status) {
									// TODO Auto-generated method stub
									SharedPrefsUtil.putValue(mContext,
											SettingKey.album_auhtority, status);
									blurDlg.dismiss();
								}
							});
					SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(
							mContext);
					dilaogView.setTitle("上传图片默认属性 :");
					dilaogView.setMargin();
					dilaogView.setBtnGone();
					dilaogView.setContentView(buttonThreeView.getView());
					blurDlg.setContentView(dilaogView.getmView());
					blurDlg.show();
					break;
				}
				case R.id.notification: {
					// 消息通知
					blurDlg = BBSCustomerDialog.newInstance(mContext,
							R.style.blurdialog);
					// 填充两个单选按钮
					final CheckBoxTwoView button = new CheckBoxTwoView(mContext);
					boolean[] status = {
							SharedPrefsUtil.getValue(mContext,
									SettingKey.msg_sound, true),
							SharedPrefsUtil.getValue(mContext,
									SettingKey.msg_vibrate, true) };
					button.setdata(status);
					SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(
							mContext);
					dilaogView.setTitle("消息通知 :");
					dilaogView.setMargin();
					// 隐藏底部按钮
					dilaogView.setPositiveButton("确定", new BtnListener() {
						@Override
						public void ensure(View contentView) {
							// 通过contentView读取内容区域的值
							boolean[] status = button.getdata();
							SharedPrefsUtil.putValue(mContext,
									SettingKey.msg_sound, status[0]);
							SharedPrefsUtil.putValue(mContext,
									SettingKey.msg_vibrate, status[1]);
							blurDlg.dismiss();
						}
					});
					dilaogView.setContentView(button.getView());
					blurDlg.setContentView(dilaogView.getmView());
					blurDlg.show();
					break;
				}
				case R.id.uploadpath_list_clearcachetime: {
					// 设置缓存时间
					blurDlg = BBSCustomerDialog.newInstance(mContext,
							R.style.blurdialog);
					// 填充两个单选按钮
					long cacheMills = SharedPrefsUtil.getValue(mContext,
							SettingKey.cacheMills, CacheType._3MonthMills.val);
					RadioButtonTwoView buttonTwoView = new RadioButtonTwoView(
							mContext);
					buttonTwoView
							.setData(cacheMills == CacheType._forververMills.val);
					buttonTwoView.setText("永久缓存数据", "缓存三个月");
					buttonTwoView.setCheckedListener(new CheckedListener() {
						@Override
						public void check(boolean status) {
							long cacheTime = status ? CacheType._forververMills.val
									: CacheType._3MonthMills.val;
							SharedPrefsUtil.putValue(mContext,
									SettingKey.cacheMills, cacheTime);
							blurDlg.dismiss();
						}
					});
					SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(
							mContext);
					dilaogView.setTitle("缓存时间 :");
					dilaogView.setMargin();
					// 隐藏底部按钮
					dilaogView.setBtnGone();
					dilaogView.setContentView(buttonTwoView.getView());
					blurDlg.setContentView(dilaogView.getmView());
					blurDlg.show();
					break;
				}
				case R.id.clear_cache_left: {
					// 清除缓存
					blurDlg = BBSCustomerDialog.newInstance(mContext,
							R.style.blurdialog);
					SimpleDilaogView simpleDilaogView = new SimpleDilaogView(
							mContext);
					// 设置标题
					simpleDilaogView.setTitle("要清除所有本地缓存数据吗?");
					// 分别设置两个按钮及定义操作
					simpleDilaogView.setPositiveButton("确定",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									blurDlg.dismiss();
									new VVBaseLoadingDlg<Boolean>(
											new VVBaseLoadingDlgCfg(mContext)
													.setShowWaitingView(true)
													.setTimeOut(10 * 1000)
													.setCancelable(true)) {
										@Override
										protected Boolean doInBackground() {
											return DataCleanManager.cleanApplicationData(
													BeemApplication
															.getContext(), true);
										}
										@Override
										protected void onPostExecute(
												Boolean result) {
											super.onPostExecute(result);
										}
									}.execute();
								}
							});
					simpleDilaogView.setNegativeButton("取消",
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									blurDlg.dismiss();
								}
							});
					blurDlg.setContentView(simpleDilaogView.getmView());
					blurDlg.show();
					break;
				}
				case R.id.about_screen_preference: {
					Intent intent = new Intent(MySettings.this,
							AboutUsActivity.class);
					startActivity(intent);
					break;
				}
				case R.id.exit_screen_preference: {
					if (LoginManager.getInstance().isLogined()) {
						BeemServiceHelper.getInstance(getApplicationContext())
								.xmppLogout();
						finish();
					} else {
						ActivityController.getInstance().gotoLogin();
					}
					break;
				}
				/*case R.id.tvw_uploadZip: {
					showUpzipDialog();
				}
					break;*/
				case R.id.btn_upload: {
					showFileChooser();
				}
					break;
				case R.id.leftbtn1:
					finish();
					break;
				default:
					break;
			}
		}
	};
	
	/** 调用文件选择软件来选择文件 **/  
	private void showFileChooser() {  
	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
	    intent.setType("*/zip");  
	    intent.addCategory(Intent.CATEGORY_OPENABLE);  
	    try {  
	        startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),  
	                1);  
	    } catch (android.content.ActivityNotFoundException ex) {  
	        // Potentially direct the user to the Market with a Dialog  
	        Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT)  
	                .show();  
	    }  
	} 

	@Override
	public void registerVVBroadCastReceivers() {
	}
	private void updateCardImage(Map<UserInfoKey, String> modifyMap) {
		// 刷新头像
		if (modifyMap != null) {
			contact = LoginManager.getInstance().getMyContact();
			if (contact != null) {
				contact.displayPhoto(headimage);
				card_info_name.setText(contact.getSuitableName());
				card_info_note.setText(contact.getSignature());
			}
		}
	}
	public void onEventMainThread(final EventBusData data) {
		switch (data.getAction()) {
			case ModifyContactInfo: {
				Map<UserInfoKey, String> modifyMap = (Map<UserInfoKey, String>) data
						.getMsg();
				updateCardImage(modifyMap);
				break;
			}
			case LOGIN_SUCCESS: {
				finish();
			}
				break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	private void showUpzipDialog() {
		View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_uploadzip, null);
		contentView.findViewById(R.id.btn_upload).setOnClickListener(settingListener);
		final WindowManager wm = getWindowManager();
		contentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				wm.removeView(v);
			}
		});
		WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
		wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wlp.format = PixelFormat.RGBA_8888;
		wlp.width = Utils.DipToPx(338, this);
		wlp.height = Utils.DipToPx(339, this);
		wlp.dimAmount = .5f;
		wm.addView(contentView, wlp);
	}

	/**
	 * @ClassName: CacheType
	 * @Description: 缓存类型
	 * @author: yuedong bao
	 * @date: 2015-9-9 下午2:02:59
	 */
	public enum CacheType {
		_3MonthMills(1000L * 60 * 60 * 24 * 30 * 3), _forververMills(1000L * 60
				* 60 * 24 * 365 * 100);
		CacheType(long value) {
			this.val = value;
		}

		public final long val;
	}
	
	private class UploadTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String file = params[0];
			String url = params[1];
			String res = UploadUtil.uploadFile(new File(file), url);
			return res != null;
		}

		@Override
		protected void onPreExecute() {
			NotificationManager nm = (NotificationManager) getApplicationContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}
}
