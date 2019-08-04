package com.oregonscientific.meep.together.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.Kid;
import com.oregonscientific.meep.together.bean.RegisterMeeper;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseFeedback;
import com.oregonscientific.meep.together.bean.ResponseProfileKid;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadProfileKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdateProfileKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUploadAvatarKidListener;

public class AccountEditInfoKid extends AccountEditInfo {
	// kid
	EditText kFirstname;
	EditText kLastname;
//	EditText kId;
	EditText kYear;
	EditText kMonth;
	EditText kDay;
	Button kConfirm;

	private Kid tmpKid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
		setContentView(R.layout.layout_main_account_edit_kid);
		text = this.getResources().getString(R.string.main_page_account_manage_er);
		initEdit();
		UserFunction.loadImage(usericon, getCurrentKid().getAvatar());

		tmpKid = getCurrentKid();
		// ---edit meeper
		kFirstname = (EditText) findViewById(R.id.textFirstName);
		kLastname = (EditText) findViewById(R.id.textLastName);
//		kId = (EditText) findViewById(R.id.textMeeptag);
		kYear = (EditText) findViewById(R.id.textYear);
		kMonth = (EditText) findViewById(R.id.textMonth);
		kDay = (EditText) findViewById(R.id.textDay);
		kConfirm = (Button) findViewById(R.id.btnConfirm);
		kConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// update child profile
				checkProfileKid();
			}
		});
		if (!UserFunction.isNetworkAvailable(this)) {
			UserFunction.popupMessage(R.string.no_network, this, loading);
		} else {
			UserFunction.getRestHelp().getProfileKid(getCurrentKid().getUserId());
			UserFunction.getRestHelp().setOnLoadProfileKidListener(new OnLoadProfileKidListener() {

				@Override
				public void onLoadProfileSuccess(ResponseProfileKid profileKid) {
					setProfileKid(profileKid);
				}

				@Override
				public void onLoadProfileFailure(ResponseBasic r) {
					setResult(RESULT_CANCELED);
					finish();
				}
			});
		}

		UserFunction.getRestHelp().setOnUploadAvatarKidListener(new OnUploadAvatarKidListener() {

			@Override
			public void onUploadAvatarKidSuccess(ResponseFeedback r) {
				//TODO:login again
				tmpKid.setAvatar(r.getPrefix()+"/" + r.getUrl());
				requestUpdateProfile(r.getUrl());
			}

			@Override
			public void onUploadAvatarKidFailure(ResponseBasic r) {
//				UserFunction.popupMessage(R.string.profile_update_failure, AccountEditInfoKid.this, loading);
				UserFunction.popupResponse(r.getStatus(), AccountEditInfoKid.this, loading);
			}
		});

	}

	public void checkProfileKid() {
		String firstName = kFirstname.getText().toString().trim();
		String lastName = kLastname.getText().toString().trim();
		String year = kYear.getText().toString();
		String month = kMonth.getText().toString();
		String day = kDay.getText().toString();
		if (firstName == null || firstName.equals("") || lastName == null
				|| lastName.equals("")) {
			UserFunction.popupMessage(R.string.register_null_name, this, loading);
		} else if (day == null || day.equals("") || month == null
				|| month.equals("") || year == null || year.equals("")) {
			UserFunction.popupMessage(R.string.register_null_birthday, this, loading);
		} else if (!UserFunction.checkBirthday(year, month, day)) {
			UserFunction.popupMessage(R.string.register_wrong_birthday, this, loading);
		} else {
			if (UserFunction.isNetworkAvailable(this)) {
				loading.show();
				if (needUploadImage) {
					// requestUpdateAvatar();
					new UploadAvatarTask().execute();
				} else {
					requestUpdateProfile(null);
				}

			}
		}
	}

	public void setProfileKid(ResponseProfileKid profileKid) {
		RegisterMeeper kid = profileKid.getProfile();
		kFirstname.setText(kid.getFirst_name());
		kLastname.setText(kid.getLast_name());
//		kId.setText(kid.getMeeptag());

		String[] b = kid.getBirthday().split("T");
		String[] date = b[0].split("-");
		kYear.setText(date[0]);
		kMonth.setText(date[1]);
		kDay.setText(date[2]);
	}

	private void requestUpdateProfile(String url) {
		String year = kYear.getText().toString();
		String month = kMonth.getText().toString();
		String day = kDay.getText().toString();
		RegisterMeeper kid = new RegisterMeeper();
		kid.setFirst_name(kFirstname.getText().toString());
		kid.setLast_name(kLastname.getText().toString());
//		kid.setMeeptag(kId.getText().toString());
		kid.setBirthday(year + "-" + month + "-" + day);
		if (url != null) {
			kid.setAvatar(url);
		}

		if (!UserFunction.isNetworkAvailable(this)) {
			UserFunction.popupMessage(R.string.no_network, this, loading);
		} else {
			UserFunction.getRestHelp().updateProfileKid(tmpKid.getUserId(), kid);
			UserFunction.getRestHelp().setOnUpdateProfileKidListener(new OnUpdateProfileKidListener() {

				@Override
				public void onUpdateProfileKidSuccess(ResponseBasic r) {
					loading.dismiss();
					if (r.getCode() == 200) {
						sendBackInformation(kFirstname.getText().toString());
					} else {
//						UserFunction.popupMessage(R.string.profile_update_no_change, AccountEditInfoKid.this, loading);
						UserFunction.popupResponse(r.getStatus(), AccountEditInfoKid.this, loading);
					}
				}

				@Override
				public void onUpdateProfileKidFailure(ResponseBasic r) {
//					UserFunction.popupMessage(R.string.profile_update_failure, AccountEditInfoKid.this, loading);
					UserFunction.popupResponse(r.getStatus(), AccountEditInfoKid.this, loading);
				}

				@Override
				public void onUpdateProfileKidTimeout() {
					UserFunction.popupMessage(R.string.please_retry, AccountEditInfoKid.this, loading);
					
				}
			});
		}
	}

	private void requestUpdateAvatar() {
		UserFunction.getRestHelp().uploadAvatar(uploadImage, false);
	}

	private void sendBackInformation(String first) {
		Intent intent = getIntent();
		tmpKid.setName(first);
		Gson gson = new Gson();
		String kidString = gson.toJson(tmpKid);
		Utils.printLogcatDebugMessage( kidString);
		Bundle bundle = new Bundle();
		bundle.putString("update-kid", kidString);
		intent.putExtra("result", bundle);
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
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.layout_rever_full_to_null_slide,R.anim.layout_rever_null_to_full_slide);
	}

}
