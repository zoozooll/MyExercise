/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009-2011 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.ui.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.LoginHelper;
import com.beem.project.btf.service.VVXmppExceptionAdapter;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.butterfly.vv.vv.utils.CToast;

import de.duenndns.ssl.MemorizingTrustManager;

/**
 * This class is an activity which display an animation during the connection with the server.
 * @author Da Risk <darisk972@gmail.com>
 */
public class LoginAnim extends VVBaseFragmentActivity {
	private static final String TAG = "LoginAnim";
	private static final int RECEIVER_PRIORITY = 50;
	private BroadcastReceiver mSslReceiver;
	private final int COLORCHANGE = 123;
	private final int STEP = 100;
	private int[] startColor;
	private int[] endcolor;
	private int[] temp = new int[3];
	RelativeLayout login_anim_wraper;
	private Timer bgColorChangeTimer;
	private IXmppFacade mXmppFacade;
	private BeemServiceHelper xmppHelper;
	//	private LoginTask mTask;
	private ImageView roundImageViewEx;
	private String jid, password;
	private VVXmppExceptionAdapter tempResult;
	private Handler mHandler = new Handler();

	public static void launch(Activity activity, String jid, String password, int requestcode) {
		Intent i = new Intent(activity, LoginAnim.class);
		i.putExtra("jid", jid);
		i.putExtra("password", password);
		activity.startActivityForResult(i, requestcode);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_anim);
		login_anim_wraper = (RelativeLayout) findViewById(R.id.login_anim_wraper);
		login_anim_wraper.setBackgroundColor(Color.rgb(160, 213, 104));
		startColor = colorTorgb("#97C761");
		endcolor = colorTorgb("#FDCF54");
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		mSslReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context ctx, Intent i) {
				try {
					PendingIntent pi = i.getParcelableExtra(MemorizingTrustManager.INTERCEPT_DECISION_INTENT_LAUNCH);
					pi.send();
					abortBroadcast();
				} catch (PendingIntent.CanceledException e) {
					Log.e(TAG, "Error while displaying the SSL dialog", e);
				}
			}
		};
		// 背景改变动画
		bgColorChangeTimer = new Timer();
		bgColorChangeTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(COLORCHANGE);
			}
		}, 0, 10);
		xmppHelper = new BeemServiceHelper(this, new BeemServiceHelper.IBeemServiceConnection() {
			@Override
			public void onServiceDisconnectAct(IXmppFacade xmppFacade, ComponentName name) {
				mXmppFacade = xmppFacade;
			}
			@Override
			public void onServiceConnectAct(IXmppFacade xmppFacade, ComponentName name, IBinder service) {
				mXmppFacade = xmppFacade;
				LoginHelper.login(xmppFacade, jid, password, new LoginHelper.OnLoginResult() {
					@Override
					public void onLoginSuccess() {
						String path = SharedPrefsUtil.getValue(mContext, SettingKey.savePhoto, "");
						if (TextUtils.isEmpty(path)) {
							LoginAnim.this.setResult(Activity.RESULT_OK);
							LoginAnim.this.finish();
						} else {
							String audioAddress = AppProperty.getInstance().VVAPI
									+ AppProperty.getInstance().UPLOAD_PORTRAIT;
							ThreadUtils.executeTask(new UpLoadFileRunnable(path, audioAddress));
						}
					}
					@Override
					public void onLoginFailed(XMPPException error) {
						Intent i = new Intent();
						i.putExtra("ErrorMessage", BBSUtils.getXmppErrorMsg(mContext, error));
						LoginAnim.this.setResult(Activity.RESULT_CANCELED, i);
						LoginAnim.this.finish();
					}
				});
			}
		});
		roundImageViewEx = (ImageView) findViewById(R.id.roundImageViewEx);
		jid = getIntent().getStringExtra("jid");
		password = getIntent().getStringExtra("password");
		loadPhoto(jid, roundImageViewEx);
	}
	private void loadPhoto(final String jid, final ImageView imageView) {
		new VVBaseLoadingDlg<Contact>(new VVBaseLoadingDlgCfg(mContext)) {
			@Override
			protected Contact doInBackground() {
				Contact contact = null;
				if (jid.length() == 11) {
					//通过手机号码查询的联系人
					contact = Contact.queryByPhonenum(jid);
				} else {
					contact = Contact.queryForFirst(jid);
				}
				if (contact == null) {
					contact = new Contact();
					contact.setSex("1");
				}
				return contact;
			}
			@Override
			protected void onPostExecute(Contact result) {
				super.onPostExecute(result);
				result.displayPhoto(imageView);
			}
		}.execute();
	}

	private Handler handler = new Handler() {
		int step = 0;
		int diretction = 1;

		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == COLORCHANGE) {
				if (diretction > 0) {
					step++;
					if (step >= STEP) {
						diretction = -1;
					}
					changeColor(step);
				} else if (diretction < 0) {
					step--;
					if (step <= 0) {
						diretction = 1;
					}
					changeColor(step);
				}
			}
		}
	};

	/**
	 * #FDCF54转成rgb
	 */
	private int[] colorTorgb(String str) {
		int r = Integer.parseInt(str.substring(1, 3), 16);
		int g = Integer.parseInt(str.substring(3, 5), 16);
		int b = Integer.parseInt(str.substring(5, 7), 16);
		int[] array = { r, g, b };
		return array;
	}
	private void changeColor(int N) {
		for (int i = 0; i < 3; i++) // RGB通道分别进行计算
		{
			temp[i] = startColor[i] + (endcolor[i] - startColor[i]) / STEP * N;
		}
		login_anim_wraper.setBackgroundColor(Color.rgb(temp[0], temp[1], temp[2]));
	}
	@Override
	protected void onStart() {
		super.onStart();
		/*if (mTask == null)
			mTask = new LoginTask();*/
		IntentFilter filter = new IntentFilter(MemorizingTrustManager.INTERCEPT_DECISION_INTENT + "/"
				+ getPackageName());
		filter.setPriority(RECEIVER_PRIORITY);
		registerReceiver(mSslReceiver, filter);
		xmppHelper.bindBeemService();
	}
	@Override
	protected void onStop() {
		super.onStop();
		/*if (mTask.getStatus() != AsyncTask.Status.RUNNING) {
			xmppHelper.unBindBeemSerivice();
			mXmppFacade = null;
			mTask.cancel(true);
		}*/
		xmppHelper.unBindBeemSerivice();
		mXmppFacade = null;
		unregisterReceiver(mSslReceiver);
		if (bgColorChangeTimer != null) {
			bgColorChangeTimer.cancel();
			bgColorChangeTimer = null;
		}
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO use onBackPressed on Eclair (2.0)
		if (keyCode == KeyEvent.KEYCODE_BACK && mTask.getStatus() != AsyncTask.Status.FINISHED) {
			if (!mTask.cancel(true)) {
				Log.d(TAG, "Can't interrupt the connection");
			}
			setResult(Activity.RESULT_CANCELED);
		}
		return super.onKeyDown(keyCode, event);
	}*/
	/**
	 * Asynchronous class for connection.
	 */
	/*private class LoginTask extends LoginAsyncTask {
		private IXmppConnection mConnection;
		private String mErrorMessage;

		LoginTask() {
		}
		@Override
		protected Boolean doInBackground(IXmppFacade... params) {
			boolean result = true;
			IXmppFacade facade = params[0];
			//LogUtils.i("~~LoginTask~~");
			try {
				publishProgress(STATE_CONNECTION_RUNNING);
				mConnection = facade.createConnection();
				if (!mConnection.connect()) {
					mErrorMessage = mConnection.getErrorMessage();
					return false;
				}
				publishProgress(STATE_LOGIN_RUNNING);
				if (!mConnection.login()) {
					mErrorMessage = mConnection.getErrorMessage();
					publishProgress(STATE_LOGIN_FAILED);
					return false;
				}
				publishProgress(STATE_LOGIN_SUCCESS);
			} catch (RemoteException e) {
				mErrorMessage = "Exception during connection :" + e;
				result = false;
				try {
					mConnection.disconnect();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if (result == null) {
				LoginAnim.this.setResult(Activity.RESULT_CANCELED);
			} else if (!result) {
				Intent i = new Intent();
				i.putExtra("ErrorMessage", getErrorMessage());
				LoginAnim.this.setResult(Activity.RESULT_CANCELED, i);
			} else {
				LoginAnim.this.setResult(Activity.RESULT_OK);
				LoginAnim.this.finish();
			}
			LoginAnim.this.finish();
		}
		@Override
		public String getErrorMessage() {
			return mErrorMessage;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
		}
		@Override
		protected void onCancelled() {
			try {
				if (mConnection != null && mConnection.isAuthentificated()) {
					mConnection.disconnect();
				}
			} catch (RemoteException e) {
				Log.d(TAG, "Remote exception", e);
			}
		}
	}*/
	private class UpLoadFileRunnable implements Runnable {
		private File getFile;
		private String url;
		private ProgressDialog progressDlg;

		private UpLoadFileRunnable(String path, String url) {
			super();
			this.getFile = new File(path);
			this.url = url;
		}
		@Override
		public void run() {
			HashMap<String, String> params = new HashMap<String, String>();
			String jidParsed = LoginManager.getInstance().getJidParsed();
			params.put("tm_id", jidParsed);
			params.put("session_id", LoginManager.getInstance().getSessionId());
			String result = UploadUtil.uploadImage(new String[] { getFile.getPath() }, url, params, "portrait_file",
					true);
			String[] uploadUrl = null;
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray("url");
				uploadUrl = new String[] { jsonArray.getString(0), jsonArray.getString(1) };
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				onPostExecute(uploadUrl);
			}
			return;
		}
		protected void onPostExecute(final String[] uploadUrl) {
			//			Looper.prepare();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (uploadUrl != null) {
						// 删除拍照图片
						getFile.delete();
						SharedPrefsUtil.putValue(mContext, SettingKey.savePhoto, "");
						Intent myintent = new Intent(getApplicationContext(), ContactList.class);
						Bundle bundle = new Bundle();
						bundle.putCharSequenceArray("VCard_path", uploadUrl);
						myintent.putExtras(bundle);
						startActivity(myintent);
						finish();
					} else {
						Intent i = new Intent();
						if (tempResult != null) {
							i.putExtra("ErrorMessage", BBSUtils.getXmppErrorMsg(mContext, tempResult.getException()));
							LoginAnim.this.setResult(Activity.RESULT_CANCELED, i);
						}
						CToast.showToast(mContext, "上传失败", Toast.LENGTH_SHORT);
						LoginAnim.this.finish();
					}
					if (progressDlg != null) {
						progressDlg.dismiss();
					}
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
}
