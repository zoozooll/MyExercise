package com.beem.project.btf.ui.fragment;

import java.io.File;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.imagefilter.crop.CropActivity;
import com.beem.project.btf.imagefilter.crop.CropExtras;
import com.beem.project.btf.ui.activity.RegisterActivity.IFragmentTransaction;
import com.beem.project.btf.ui.activity.RegisterActivity.RegisterFrgmtStatus;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.ui.views.BottomPopupWindow;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionListener;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionType;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.ExpressionUtil;
import com.butterfly.vv.camera.GalleryActivity;
import com.butterfly.vv.service.CommonService;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.butterfly.vv.vv.utils.JsonParseUtils.FindParam;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

@SuppressLint("ValidFragment")
public class RegisterEditInfoFragment extends Fragment implements
		IFragmentTransaction, OnClickListener, IEventBusAction {
	private static final String tag = RegisterEditInfoFragment.class
			.getSimpleName();
	private EditText username;
	private EditText password;
	private EditText confirmPassword;
	private CreateAccountTask task;
	private Context mContext;
	private Button register_btn;
	private String sex = "1";
	private boolean isNameOk, isPassWordOk, isRePassWordOk;
	private View view;
	private BottomPopupWindow filterPopupWindow;
	private Bitmap showIconBmp;
	private String savePath;
	private ImageView login_photo_logo;
	private RegisterFrgmtStatus status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	@Override
	public void onClick(View v) {
		if (v == register_btn) {
			checkUserName();
			checkPasswords();
			checkRePasswords();
			if (isNameOk && isPassWordOk && isRePassWordOk) {
				String nickname = username.getText().toString();
				String pass = password.getText().toString();
				Bundle bundle = this.getArguments();
				String phone = bundle.getString("phone");
				Log.i(tag, "register" + nickname + pass + phone + sex);
				task = new CreateAccountTask(nickname, pass, phone, sex);
				task.execute();
			}
		}
	}
	/**
	 * Callback called when the account is successfully created.
	 * @param jid the jid of the account.
	 * @param pass the pass_md5 of the account.
	 */
	private void onAccountCreationSuccess(String jid, String pass) {
		CommonService.saveCredential(getActivity(), jid, pass);
		Bundle bundle = new Bundle();
		bundle.putString("jid", jid);
		bundle.putString("pass", pass);
		if (!TextUtils.isEmpty(savePath)) {
			bundle.putString("savePhoto", savePath);
		}
		EventBus.getDefault().post(
				new EventBusData(EventAction.RegisterSuccess, bundle));
		getActivity().onBackPressed();
		getActivity().finish();
	}
	public void phoneClickHandler(String artistname,
			ContentResolver contentResolver) {
	}
	/**
	 * Callback called when the account failed to create.
	 * @param e the exception which occurs
	 */
	private void onAccountCreationFailed(XMPPException e) {
		XMPPError error = e.getXMPPError();
		if (error != null
				&& XMPPError.Condition.conflict.value.equals(error
						.getCondition())) {
		} else {
		}
		Log.v(tag, "Unable to create an account on xmpp server", e);
	}
	/**
	 * Check the format of the email.
	 * @return true if the email is valid.
	 */
	private void checkUserName() {
		String name = username.getText().toString();
		if (TextUtils.isEmpty(name.toString().trim())) {
			username.setError("昵称不能为空");
			isNameOk = false;
		} else {
			isNameOk = true;
		}
	}
	/**
	 * Check if the fields pass_md5 and confirm pass_md5 match.
	 * @return return true if pass_md5 & confirm pass_md5 fields match, else false
	 */
	private void checkPasswords() {
		String pass = password.getText().toString().trim();
		boolean matches = pass.matches(".{6,20}+");
		if (!TextUtils.isEmpty(pass) && matches) {
			isPassWordOk = true;
		} else {
			password.setError(getString(R.string.unqualified_password));
			isPassWordOk = false;
		}
	}
	private void checkRePasswords() {
		CharSequence pass = confirmPassword.getText();
		if (TextUtils.equals(pass, password.getText())) {
			isRePassWordOk = true;
		} else {
			isRePassWordOk = false;
			confirmPassword.setError(getString(R.string.mismatching_password));
		}
	}

	/**
	 * AsyncTask use to create an XMPP account on a server.
	 */
	private class CreateAccountTask extends
			VVBaseLoadingDlg<Map<String, Object>> {
		private String nickName;
		private String pass_md5;
		private final String token_md5;
		private String phone;
		private String sex;

		public CreateAccountTask(String nickname, String pass, String phone,
				String sex) {
			super(new VVBaseLoadingDlgCfg(mContext).setShowWaitingView(true)
					.setTimeOut(10 * 1000));
			this.nickName = nickname;
			this.pass_md5 = BBSUtils.Md5(pass);
			this.phone = phone;
			this.token_md5 = BBSUtils.Md5(phone + "butterfly");
			this.sex = sex;
		}
		@Override
		protected Map<String, Object> doInBackground() {
			//			Log.d(tag, "Xmpp CreateAccountTask task");
			try {
				return CommonService.register(nickName, pass_md5, token_md5, phone,
						sex);
			} catch (WSError e) {
				e.printStackTrace();
				setManulaTimeOut(true);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			if (JsonParseUtils.getResult(result)) {
				String jid = JsonParseUtils.getParseValue(result, String.class,
						new FindParam("data", 0), new FindParam("tm_id"));
				CToast.showToast(getActivity(), "注册成功，时光号：" + jid,
						Toast.LENGTH_SHORT);
				onAccountCreationSuccess(phone, pass_md5);
			} else {
				CToast.showToast(mContext, "注册失败", Toast.LENGTH_SHORT);
			}
		}
	}

	public RegisterEditInfoFragment(RegisterFrgmtStatus status) {
		super();
		this.status = status;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		view = inflater.inflate(R.layout.create_account_edit, container, false);
		login_photo_logo = (ImageView) view.findViewById(R.id.login_photo_logo);
		login_photo_logo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//关掉软件盘
				//closeSoftInput();
				MenuIMGsrcOnclickLT itemscontrolOnClick = new MenuIMGsrcOnclickLT();
				filterPopupWindow = new BottomPopupWindow((Activity) mContext,
						itemscontrolOnClick, PopupActionType.TAKEPHOTO);
				filterPopupWindow.showAtLocation(
						getActivity().findViewById(R.id.id_content_layout),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
		register_btn = (Button) view.findViewById(R.id.register_btn);
		register_btn.setOnClickListener(this);
		username = (EditText) view.findViewById(R.id.create_account_username);
		username.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View paramView, boolean paramBoolean) {
				if (!paramBoolean) {
					checkUserName();
				}
			}
		});
		password = (EditText) view.findViewById(R.id.create_account_password);
		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View paramView, boolean paramBoolean) {
				if (!paramBoolean) {
					checkPasswords();
				}
			}
		});
		confirmPassword = (EditText) view
				.findViewById(R.id.create_account_confirm_password);
		confirmPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View paramView, boolean paramBoolean) {
				if (!paramBoolean) {
					checkRePasswords();
				}
			}
		});
		confirmPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}
			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {
			}
			@Override
			public void afterTextChanged(Editable paramEditable) {
				checkRePasswords();
			}
		});
		TextView rg_tv_note = (TextView) view.findViewById(R.id.rg_tv_note);
		String rg_tv_noteStr = mContext
				.getString(R.string.login_register_protocol);
		ExpressionUtil.setLinkedTextView(mContext, rg_tv_note, rg_tv_noteStr,
				8, 15);
		ToggleButton sex_toggle = (ToggleButton) view
				.findViewById(R.id.sex_toggle);
		sex_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton paramCompoundButton,
					boolean paramBoolean) {
				if (paramBoolean) {
					sex = "0";
				} else {
					sex = "1";
				}
			}
		});
		return view;
	}

	/**
	 * 调用相机
	 * @Title: takePhoto
	 * @Description: TODO
	 * @return: void
	 */
	/** 使用相册中的图片 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	// 上传图片返回的结果
	private static final int CLIP_PICTURE_A = 20;
	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;
	/** 从Intent获取图片路径的KEY */
	public static final String KEY_PHOTO_PATH = "photo_path";
	private String flag;
	private Bundle mBundle;

	class MenuIMGsrcOnclickLT implements PopupActionListener {
		int position;
		ImageView mview;

		public MenuIMGsrcOnclickLT() {
			super();
		}
		@Override
		public void itemsClick(PopupActionType type, int i) {
			filterPopupWindow.dismiss();
			if (type == PopupActionType.TAKEPHOTO) {
				if (i == 0) {
					photoUri = BBSUtils.takePhoto(
							RegisterEditInfoFragment.this, Constants.TAKEPHOTO);
				} else if (i == 1) {
					doPickPhotoFromGallery();
				}
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.TAKEPHOTO
				&& resultCode == Activity.RESULT_OK) {
			picPath = BBSUtils.getTakePhotoPath(getActivity());
			//			Log.i(tag, "picPath" + picPath);
			if (picPath != null) {
				//				ClipPictureActivity.launch(RegisterEditInfoFragment.this, picPath, CLIP_PICTURE_A, true);
				Uri uri = data.getData();
				Intent localIntent = new Intent(getActivity(),
						CropActivity.class);
				localIntent.setDataAndType(uri, "image/*");
				localIntent.putExtra("crop", "true");
				localIntent.putExtra("aspectX", 1);
				localIntent.putExtra("aspectY", 1);
				localIntent.putExtra("scale", true);
				savePath = BBSUtils.getTakePhotoPath(getActivity(),
						System.currentTimeMillis() + ".jpg");
				localIntent
						.putExtra("output", Uri.fromFile(new File(savePath)));
				localIntent.putExtra("return-data", false);
				localIntent.putExtra("outputFormat",
						Bitmap.CompressFormat.JPEG.toString());
				localIntent.putExtra("noFaceDetection", true);
				startActivityForResult(localIntent, Constants.CLIPPHOTO);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"选择图片文件不正确", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == Constants.CLIPPHOTO
				|| requestCode == Constants.PICKPHOTO) {
			if (resultCode == Activity.RESULT_OK) {
				//login_photo_logo.setImageBitmap(showIconBmp);
				if ("1".equals(sex)) {
					ImageLoader.getInstance().displayImage(
							Scheme.FILE.wrap(savePath), login_photo_logo,
							ImageLoaderConfigers.sexOpt[0]);
				} else {
					ImageLoader.getInstance().displayImage(
							Scheme.FILE.wrap(savePath), login_photo_logo,
							ImageLoaderConfigers.sexOpt[1]);
				}
			}
		}
	}
	/**
	 * 调用相册
	 * @Title: doPickPhotoFromGallery
	 * @Description:
	 * @return: void
	 */
	protected void doPickPhotoFromGallery() {
		Intent intent = new Intent(getActivity(), GalleryActivity.class);
		intent.putExtra(GalleryActivity.GALLERY_PICKABLE, true);
		intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, 1);
		intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
		intent.putExtra(GalleryActivity.GALLERY_CROP, true);
		intent.putExtra(CropExtras.KEY_ASPECT_X, 1);
		intent.putExtra(CropExtras.KEY_ASPECT_Y, 1);
		savePath = BBSUtils.getTakePhotoPath(getActivity(),
				System.currentTimeMillis() + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(savePath)));
		startActivityForResult(intent, Constants.PICKPHOTO);
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public boolean onBackPressed() {
		return true;
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		/*if (showIconBmp != null && !showIconBmp.isRecycled()) {
			showIconBmp.recycle();
		}*/
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (EventAction.RegisterCacheImage.equals(data.getAction())) {
			String savePhoto = (String) data.getMsg();
			//showIconBmp = PictureUtil.revitionImage(savePhoto);
			savePath = savePhoto;
			//login_photo_logo.setImageBitmap(showIconBmp);
			if ("1".equals(sex)) {
				ImageLoader.getInstance().displayImage(
						Scheme.FILE.wrap(savePhoto), login_photo_logo,
						ImageLoaderConfigers.sexOptRound[0]);
			} else {
				ImageLoader.getInstance().displayImage(
						Scheme.FILE.wrap(savePhoto), login_photo_logo,
						ImageLoaderConfigers.sexOptRound[1]);
			}
		}
	}

	interface IOnChange {
		void onChange();
	}
}
