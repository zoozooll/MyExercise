package com.oregonscientific.meep.together.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.RegisterUser;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseFeedback;
import com.oregonscientific.meep.together.bean.ResponseProfileParent;
import com.oregonscientific.meep.together.library.database.table.TableUser;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadProfileParentListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdateProfileParentListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUploadAvatarParentListener;

public class AccountEditInfoParent extends AccountEditInfo {

	// parent
	EditText pFirstname;
	EditText pLastname;
//	EditText pTel;
//	EditText pAddress;
	CheckBox pPromotion;
	Button pConfirm;
	private TableUser tmpUser;
	private String url;
	ToggleButton pGender;
	TextView textMale;
	TextView textFemale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main_account_edit_parent);
		text = this.getResources().getString(R.string.main_page_account_manage_boss);
		initEdit();
		UserFunction.loadImage(usericon, getCurrentUser().getIconAddr());
		tmpUser = getCurrentUser();
		// ---edit boss
		pFirstname = (EditText) findViewById(R.id.textFirstName);
		pLastname = (EditText) findViewById(R.id.textLastName);
		pGender = (ToggleButton) findViewById(R.id.togglebutton);
//		pTel = (EditText) findViewById(R.id.textTelMobile);
//		pAddress = (EditText) findViewById(R.id.textAddress);
		pPromotion = (CheckBox) findViewById(R.id.promotion);
		pConfirm = (Button) findViewById(R.id.btnConfirm);
		textMale = (TextView) findViewById(R.id.textMale);
		textFemale = (TextView) findViewById(R.id.textFemale);
		pConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// update parent profile
				checkProfileParent();

			}
		});
		if (!UserFunction.isNetworkAvailable(this)) {
			UserFunction.popupMessage(R.string.no_network, this, loading);
		} else {
			getCurrentProfile();
		}

		UserFunction.getRestHelp().setOnUploadAvatarParentListener(new OnUploadAvatarParentListener() {

			@Override
			public void onUploadAvatarParentSuccess(ResponseFeedback r) {
				// if(r.getCode()==200)
				// onUploadAvatarParent(r.getStatus(),r.getUrl());
				// onUpdateProfileParent(r.getStatus(), true);
				
				//TODO:login again
				tmpUser.setIconAddr(r.getPrefix() +"/"+ r.getUrl());
				requestUpdateProfile(r.getUrl());
			}

			@Override
			public void onUploadAvatarParentFailure(ResponseBasic r) {
				// onUpdateProfileParent(r.getStatus(), false);
				// UserFunction.popupMessage(R.string.profile_update_failure,
				// AccountEditInfoParent.this, loading);
				UserFunction.popupResponse(r.getStatus(), AccountEditInfoParent.this, loading);
			}
		});

		pGender.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked)
				{
					setTextOnToggleButton(textFemale, R.color.text_gray, R.color.text_white, false);
					setTextOnToggleButton(textMale, R.color.text_white, R.color.text_black, true);
				}
				else
				{
					setTextOnToggleButton(textFemale, R.color.text_white, R.color.text_black, true);
					setTextOnToggleButton(textMale, R.color.text_gray, R.color.text_white, false);
				}

			}
		});

	}
	
	public void setTextOnToggleButton(TextView textview,int textColor,int shadowColor,boolean clickable)
	{
		textview.setTextColor(getResources().getColor(textColor));
		textview.setShadowLayer(0.1f, 0, -1, getResources().getColor(shadowColor));
		textview.setClickable(clickable);
	}

	public void checkProfileParent() {
		String firstName = pFirstname.getText().toString().trim();
		String lastName = pLastname.getText().toString().trim();
		if (firstName == null || firstName.equals("") || lastName == null
				|| lastName.equals("")) {
			UserFunction.popupMessage(R.string.register_null_name, this, loading);
		} else {
			if (UserFunction.isNetworkAvailable(this)) {
				loading.show();
				if (needUploadImage) {
					// requestUpdateAvatar();
					new UploadAvatarTask().execute();
				} else {
					requestUpdateProfile(null);
				}
			} else {
				UserFunction.popupMessage(R.string.no_network, this, loading);
			}

		}

	}

	public void setProfileParent(ResponseProfileParent profileParent) {
		RegisterUser parent = profileParent.getProfile();
		pFirstname.setText(parent.getFirst_name());
		pLastname.setText(parent.getLast_name());
//		pAddress.setText(parent.getAddress());
//		pTel.setText(parent.getTel());
		if (parent.getGender().equals(RegisterUser.FEMALE)) {
			pGender.setChecked(false);
		} else {
			pGender.setChecked(true);
		}

		pPromotion.setChecked(parent.getPromotion_optin());
	}

	private void requestUpdateProfile(String url) {
		RegisterUser parent = new RegisterUser();
		parent.setFirst_name(pFirstname.getText().toString());
		parent.setLast_name(pLastname.getText().toString());
//		parent.setAddress(pAddress.getText().toString());
//		parent.setTel(pTel.getText().toString());
		if (url != null) {
			parent.setAvatar(url);
		}
		if (pGender.isChecked())
			parent.setGender(RegisterUser.MALE);
		else
			parent.setGender(RegisterUser.FEMALE);
		parent.setPromotion_optin(pPromotion.isChecked());

		if (!UserFunction.isNetworkAvailable(this)) {
			UserFunction.popupMessage(R.string.no_network, this, loading);
		} else {
			sendUpdatedProfile(parent);
		}
	}

	private void requestUpdateAvatar() {
		UserFunction.getRestHelp().uploadAvatar(uploadImage, true);
	}

	public void sendUpdatedProfile(RegisterUser parent) {
		UserFunction.getRestHelp().setOnUpdateProfileParentListener(new OnUpdateProfileParentListener() {
			
			@Override
			public void onUpdateProfileParentSuccess(ResponseBasic r) {
				loading.dismiss();
				if (r.getCode() == 200) {
					sendBackInformation(pFirstname.getText().toString(), pLastname.getText().toString());
				} else {
					// UserFunction.popupMessage(R.string.profile_update_no_change,
					// AccountEditInfoParent.this, loading);
					UserFunction.popupResponse(r.getStatus(), AccountEditInfoParent.this, loading);
				}
			}
			
			@Override
			public void onUpdateProfileParentFailure(ResponseBasic r) {
				// UserFunction.popupMessage(R.string.profile_update_failure,
				// AccountEditInfoParent.this, loading);
				UserFunction.popupResponse(r.getStatus(), AccountEditInfoParent.this, loading);
			}
			
			@Override
			public void onUpdateProfileParentTimeout() {
				UserFunction.popupMessage(R.string.please_retry, AccountEditInfoParent.this, loading);
			}
		});
		UserFunction.getRestHelp().updateProfileParent(parent);
	}

	public void getCurrentProfile() {
		UserFunction.getRestHelp().getProfileParent();
		UserFunction.getRestHelp().setOnLoadProfileParentListener(new OnLoadProfileParentListener() {

			@Override
			public void onLoadProfileSuccess(ResponseProfileParent profileParent) {
				setProfileParent(profileParent);
			}

			@Override
			public void onLoadProfileFailure(ResponseBasic r) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private void sendBackInformation(String first, String last) {
		Intent intent = getIntent();
		tmpUser.setFirstName(first);
		tmpUser.setLastName(last);
		Gson gson = new Gson();
		String parentString = gson.toJson(tmpUser);
		Utils.printLogcatDebugMessage( parentString);
		Bundle bundle = new Bundle();
		bundle.putString("update-parent", parentString);
		intent.putExtra("result", bundle);

		intent.putExtra("parent", 0);
		setResult(RESULT_OK, intent);
		finish();
	}

	private class UploadAvatarTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... v) {
			try {
				requestUpdateAvatar();
			} catch (Exception e) {
			}
			return null;
		}

		protected void onPostExecute() {
			// TODO: check this.exception
			// TODO: do something with the feed
		}
	}
}
