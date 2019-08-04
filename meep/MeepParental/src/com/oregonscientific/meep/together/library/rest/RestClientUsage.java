package com.oregonscientific.meep.together.library.rest;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.activity.Utils;
import com.oregonscientific.meep.together.bean.Coin;
import com.oregonscientific.meep.together.bean.Kid;
import com.oregonscientific.meep.together.bean.LoginUser;
import com.oregonscientific.meep.together.bean.MigrateChild;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.RegisterMeeper;
import com.oregonscientific.meep.together.bean.RegisterUser;
import com.oregonscientific.meep.together.bean.RegisterUserCombined;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseFeedback;
import com.oregonscientific.meep.together.bean.ResponseLoadCoin;
import com.oregonscientific.meep.together.bean.ResponseLoadKid;
import com.oregonscientific.meep.together.bean.ResponseLoadLogs;
import com.oregonscientific.meep.together.bean.ResponseLoadNotification;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.bean.ResponseLogin;
import com.oregonscientific.meep.together.bean.ResponseProfileKid;
import com.oregonscientific.meep.together.bean.ResponseProfileParent;
import com.oregonscientific.meep.together.bean.Supervisor;
import com.oregonscientific.meep.together.library.database.table.AuthInfo;
import com.oregonscientific.meep.together.library.rest.listener.OnAllocateCoinsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnGetOwnerIdListerner;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadLogsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadNotificationsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadPortalSettingsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadProfileKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadProfileParentListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoginListener;
import com.oregonscientific.meep.together.library.rest.listener.OnMigrateChild;
import com.oregonscientific.meep.together.library.rest.listener.OnPasswordSentListener;
import com.oregonscientific.meep.together.library.rest.listener.OnRegisterListener;
import com.oregonscientific.meep.together.library.rest.listener.OnRegisterMeeperListener;
import com.oregonscientific.meep.together.library.rest.listener.OnSnVerifyListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdateCoinListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdateProfileKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdateProfileParentListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUploadAvatarKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUploadAvatarParentListener;
import com.oregonscientific.meep.together.library.rest.listener.RequestRespondedListener;
import com.oregonscientific.meep.together.library.rest.listener.ResendEmailListener;

public class RestClientUsage {
	private static String TAG = "RestClient";
	private Gson mGson = new Gson();
	private CryptoText crypto = new CryptoText();
	private static LoginUser mLoginUser = null;
	public static ResponseLogin infoParent = null;
	public static ResponseLoadKid infoKid = null;
	public static ResponseLoadNotification infoNotification = null;
	public static ResponseLoadPermission infoPermission = null;
	public static ResponseLoadCoin infoCoin = null;
	public static boolean isInUse = false;

	public void authenticationLocally(LoginUser user, AuthInfo auth) {
		// email
		String email = user.getEmail().trim().toLowerCase();
		// password
		crypto.getChallenge();
		crypto.cryptoPass(email, user.getPassword());
		String password = crypto.getPbk_pass();

		if (auth.getEmail().equals(email)
				&& auth.getPassword().equals(password)) {
			ResponseLogin lr = new ResponseLogin();
			lr.setStage(ResponseLogin.STAGE_OFFLINE);
			if (onLoginListener != null)
				onLoginListener.onLoginSuccess(lr);

		} else {
			if (onLoginListener != null)
				onLoginListener.onLoginFailure(null);
		}

	}

	// login
	public void authentication(LoginUser user, final Context c) {
		mLoginUser = new LoginUser(user.getEmail(), user.getPassword());
		// email
		String email = user.getEmail().trim().toLowerCase();
		// challenge
		String challenge = crypto.getChallenge();

		String password = null;
		String pbkpass = null;
		// if(UserFunction.isMainPage)
		// {
		// pbkpass = user.getPassword();
		// password = crypto.HmacSHA1(challenge, user.getPassword());
		// }
		// else
		// {
		// password
		password = crypto.cryptoPass(email, user.getPassword());
		pbkpass = crypto.getPbk_pass();

		// }
		user.setEmail(null);
		user.setChallenge(challenge);
		user.setPassword(password);
		String arg = mGson.toJson(user);

		// store email&encrypted pwd
		user.setEmail(email);
		user.setPassword(pbkpass);
		// sent authentication request
		Log.i(TAG, "Login Message Sent");
		RestClient.initVersion(2);
		RestClient.postJson("account/authentication/" + email, arg, new JsonHttpResponseHandler() {
			ResponseBasic r;

			@Override
			public void onSuccess(JSONObject arg0) {
				super.onSuccess(arg0);
				// convert meesage from server
				Utils.printLogcatDebugMessage(TAG, "Login Response -- "
						+ arg0.toString());
				infoParent = mGson.fromJson(arg0.toString(), ResponseLogin.class);
				Utils.printLogcatDebugMessage(TAG, "Login Response -- "
						+ infoParent.getStatus() + ":" + infoParent.getCode());
				Utils.printLogcatDebugMessage(TAG, "Login Response -- "
						+ infoParent.getStage());
				if(infoParent.getStage().equals(ResponseLogin.STAGE_NORMAL))
				{
					UserFunction.setCreditCardVerfied(c, true);
				}
				// UI modification
				if (onLoginListener != null)
					onLoginListener.onLoginSuccess(infoParent);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				Log.w(TAG, "Login Error -- " + arg1.toString());
				r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
				if (onLoginListener != null)
					onLoginListener.onLoginFailure(r);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.w(TAG, "Login Error -- " + content);
				if (onLoginListener != null)
					onLoginListener.onLoginTimeOut();

			}
		});
	}

	// login
	public void reAuthentication(Context c) {
		authentication(mLoginUser, c);

	}

	public void forgotPassword(String email) {
		RestClient.initVersion(1);
		RestClient.getJson("account/forgot/" + email, new JsonHttpResponseHandler() {
			ResponseBasic r;

			@Override
			public void onSuccess(JSONObject arg0) {
				super.onSuccess(arg0);
				r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
				Utils.printLogcatDebugMessage(TAG, "Forget Response -- "
						+ r.getStatus() + ":" + r.getCode());
				if (onPasswordSentListener != null)
					onPasswordSentListener.onPasswordSentSuccess(r);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				Log.w(TAG, "Forget Error");
				Utils.printLogcatDebugMessage(TAG, "Forget -- "
						+ arg1.toString());
				r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
				if (onPasswordSentListener != null)
					onPasswordSentListener.onPasswordSentFailure(r);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.w(TAG, "Forget Error -- " + content);
				if (onPasswordSentListener != null)
					onPasswordSentListener.onPasswordSentTimeout();

			}
		});
	}

	public void serialVerify(String serialno) {
		RestClient.initVersion(1);
		RestClient.postJson("device/precheck/" + serialno, "", new JsonHttpResponseHandler() {
			ResponseBasic r;

			@Override
			public void onSuccess(JSONObject arg0) {
				super.onSuccess(arg0);
				// Utils.printLogcatDebugMessage(TAG,
				// "SerailVerify -- "+arg0.toString());
				r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
				Utils.printLogcatDebugMessage(TAG, "SerailVerify Response -- "
						+ r.getStatus() + ":" + r.getCode());
				if (onSnVerifyListener != null)
					onSnVerifyListener.onSnVerifySuccess(r);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				// Utils.printLogcatDebugMessage(TAG,
				// "SerailVerify -- "+arg1.toString());
				r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
				Log.w(TAG, "SerailVerify Error -- " + r.getStatus() + ":"
						+ r.getCode());
				if (onSnVerifyListener != null)
					onSnVerifyListener.onSnVerifyFailure(r);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.w(TAG, "SerailVerify Error -- " + content);
				if (onSnVerifyListener != null)
					onSnVerifyListener.onSnVerifyTiemOut();

			}
		});

	}

	public void registration(String serialno, RegisterUserCombined user) {
		Utils.printLogcatDebugMessage(TAG, "Registration TEST:"
				+ mGson.toJson(user));
		// TODO:check version 2
		RestClient.initVersion(2);
		RestClient.postJson("account/registration/" + serialno, mGson.toJson(user), new JsonHttpResponseHandler() {
			ResponseBasic r;

			@Override
			public void onSuccess(JSONObject arg0) {
				r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
				Utils.printLogcatDebugMessage(TAG, "Registration Response -- "
						+ r.getStatus() + ":" + r.getCode());
				// Utils.printLogcatDebugMessage(TAG,
				// "Registration -- "+arg0.toString());
				onRegisterListener.onRegisterSuccess();
				super.onSuccess(arg0);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
				Log.w(TAG, "Registration Error -- " + r.getStatus() + ":"
						+ r.getCode());
				// Utils.printLogcatDebugMessage(TAG,
				// "Registration -- "+arg1.toString());
				onRegisterListener.onRegisterFailure(r);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.w(TAG, "Registration Error -- " + content);
				onRegisterListener.onRegisterTimeout();

			}
		});
	}

	public void reloadAllKids() {
		RestClient.initVersion(1);
		if (infoParent != null) {
			// Utils.printLogcatDebugMessage(TAG,"token:"+infoParent.getToken());
			RestClient.getJsonAuth("account/kids", infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					Utils.printLogcatDebugMessage(TAG, "Kids -- "
							+ arg0.toString());
					infoKid = mGson.fromJson(arg0.toString(), ResponseLoadKid.class);
					Utils.printLogcatDebugMessage(TAG, "Kids Loading Response - "
							+ infoKid.getStatus() + ":" + infoKid.getCode());
					if (onLoadKidListener != null)
						onLoadKidListener.onLoadKidSuccess(filterSupervisorKid(infoKid.getKids()));
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Kids Loading Error");
					Log.w(TAG, arg1.toString());
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoadKidListener != null)
						onLoadKidListener.onLoadKidFailure();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Kids Loading Error -- " + content);
					if (onLoadKidListener != null)
						onLoadKidListener.onLoadKidTimeout();

				}
			});
		}

	}

	public Kid[] filterSupervisorKid(Kid[] kids) {
		ArrayList<Kid> newKids = new ArrayList<Kid>();
		for (Kid k : kids) {
			if (k.getType().equals(Kid.S_TYPE_PARENT)) {
				newKids.add(k);
			}
		}
		return newKids.toArray(new Kid[] {});
	}

	public void refreshLogs(String userId, String lastId) {
		RestClient.initVersion(1);
		// notification ID is not specified
		if (lastId == null) {
			lastId = "ffffffffffffffffffffffff";
		}
		// Utils.printLogcatDebugMessage(TAG,"token:"+infoParent.getToken());

		if (userId != null)
			RestClient.getJsonAuth("parental/logs/" + userId + "/" + lastId, infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "Logs -- "+arg0.toString());
					ResponseLoadLogs infoLogs = mGson.fromJson(arg0.toString(), ResponseLoadLogs.class);
					Utils.printLogcatDebugMessage(TAG, "Logs Loading Response - "
							+ infoLogs.getStatus() + ":" + infoLogs.getCode());
					if (onLoadLogsListener != null)
						onLoadLogsListener.onLoadLogsSuccess(infoLogs);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Logs Loading Error");
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoadLogsListener != null)
						onLoadLogsListener.onLoadLogsFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Logs Loading Error -- " + content);

				}
			});
	}

	public void refreshNotification(String userId, String lastId) {
		RestClient.initVersion(1);
		// notification ID is not specified
		if (lastId == null) {
			lastId = "ffffffffffffffffffffffff";
		}
		// Utils.printLogcatDebugMessage(TAG,"token:"+infoParent.getToken());
		if (userId != null)
			RestClient.getJsonAuth("parental/notifications/" + userId + "/"
					+ lastId, infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					 Utils.printLogcatDebugMessage(TAG,
					 "Notification -- "+arg0.toString());
					infoNotification = mGson.fromJson(arg0.toString(), ResponseLoadNotification.class);
					Utils.printLogcatDebugMessage(TAG, "Notification Loading Response - "
							+ infoNotification.getStatus()
							+ ":"
							+ infoNotification.getCode());
					if (onLoadNotificationsListener != null)
						onLoadNotificationsListener.onLoadNotificationsSuccess(infoNotification);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Notification Loading Error");
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoadNotificationsListener != null)
						onLoadNotificationsListener.onLoadNotificationsFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Notification Loading Error -- " + content);

				}
			});
	}

	public void refreshPermission(String userId) {
		RestClient.initVersion(1);
		if (userId != null)
			RestClient.getJsonAuth("parental/permission/" + userId, infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "Permission -- "+arg0.toString());
					infoPermission = mGson.fromJson(arg0.toString(), ResponseLoadPermission.class);
					infoPermission.getPermission().setLimit();
					Utils.printLogcatDebugMessage(TAG, "Permission Loading Response - "
							+ infoPermission.getStatus()
							+ ":"
							+ infoPermission.getCode());
					if (onLoadPortalSettingsListener != null)
						onLoadPortalSettingsListener.onLoadPortalSettingsSuccess(infoPermission);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Permission Loading Error");
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoadPortalSettingsListener != null)
						onLoadPortalSettingsListener.onLoadPortalSettingsFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Permission Loading Error -- " + content);

				}
			});
	}

	public void savePermission(String userId, Permissions permission) {
		RestClient.initVersion(1);
		infoPermission.getPermission().clearLimit();
		if (userId != null)
			RestClient.postJsonAuth("parental/permission/" + userId, mGson.toJson(permission), infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// TODO: get new permission
					Utils.printLogcatDebugMessage(TAG, "Permission save-- "
							+ arg0.toString());
					infoPermission = mGson.fromJson(arg0.toString(), ResponseLoadPermission.class);
					infoPermission.getPermission().setLimit();
					Utils.printLogcatDebugMessage(TAG, "Permission Saving Response - "
							+ infoPermission.getStatus()
							+ ":"
							+ infoPermission.getCode());
					// Utils.printLogcatDebugMessage(TAG,"Permission Json -- "+mGson.toJson(infoPermission));
					if (onUpdatePortalSettingsListener != null)
						onUpdatePortalSettingsListener.onUpdatePortalSettingsSuccess(infoPermission);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Permission Saving Error");
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onUpdatePortalSettingsListener != null)
						onUpdatePortalSettingsListener.onUpdatePortalSettingsFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Permission Saving Error -- " + content);
					if (onUpdatePortalSettingsListener != null)
						onUpdatePortalSettingsListener.onUpdatePortalSettingsTimeout();

				}
			});
	}

	public void refreshCoin(String userId) {
		RestClient.initVersion(1);
		if (userId != null)
			RestClient.getJsonAuth("account/coins/" + userId, infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "Coins -- "+arg0.toString());
					infoCoin = mGson.fromJson(arg0.toString(), ResponseLoadCoin.class);
					Utils.printLogcatDebugMessage(TAG, "Coins Loading Response - "
							+ infoCoin.getStatus() + ":" + infoCoin.getCode());
					// Utils.printLogcatDebugMessage(TAG,"Coins Json -- "+mGson.toJson(infoCoin));
					if (onUpdateCoinListener != null)
						onUpdateCoinListener.onUpdateCoinSuccess(infoCoin);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Coins Loading Error");
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onUpdateCoinListener != null)
						onUpdateCoinListener.onUpdateCoinFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Coins Loading Error -- " + content);
					if (onUpdateCoinListener != null)
						onUpdateCoinListener.onUpdateCoinTimeout();

				}
			});
	}

	public void saveAllocation(String userId, int allocate) {
		RestClient.initVersion(1);
		Coin coin = new Coin();
		coin.setCoins(allocate);
		if (userId != null)
			RestClient.postJsonAuth("account/coins/" + userId, mGson.toJson(coin), infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "Coins Allocation -- "+arg0.toString());
					infoCoin = mGson.fromJson(arg0.toString(), ResponseLoadCoin.class);
					Utils.printLogcatDebugMessage(TAG, "Coins Allocation Response - "
							+ infoCoin.getStatus() + ":" + infoCoin.getCode());
					// Utils.printLogcatDebugMessage(TAG,"Coins Allocation Json -- "+mGson.toJson(infoPermission));
					if (onAllocateCoinsListener != null)
						onAllocateCoinsListener.onAllocateCoinsSuccess(infoCoin);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Coins Allocation  Error");
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onAllocateCoinsListener != null)
						onAllocateCoinsListener.onAllocateCoinsFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Coins Allocation Error -- " + content);
					if (onAllocateCoinsListener != null)
						onAllocateCoinsListener.onAllocateCoinsTimeout();

				}
			});
	}

	public void respondNotification(String type, String id) {
		RestClient.initVersion(1);
		RestClient.postJsonAuth("parental/approval/" + type + "/" + id, null, infoParent.getToken(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				super.onSuccess(arg0);
				Utils.printLogcatDebugMessage(TAG, "Notification Approval -- "
						+ arg0.toString());
				ResponseBasic r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
				Utils.printLogcatDebugMessage(TAG, "Notification Approval Response - "
						+ r.getStatus() + ":" + r.getCode());
				if (responseListener != null)
					responseListener.onRespondSuccess(r);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				Log.w(TAG, "Notification Approval Error");
				Log.w(TAG, arg1.toString());
				ResponseBasic r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
				if (responseListener != null)
					responseListener.onRespondFailure(r);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.w(TAG, "Notification Approval Error -- " + content);
				if (responseListener != null)
					responseListener.onRespondTimeout();
			}
		});
	}

	// public void addNewKidOrUpdate()
	// {
	// RestClient.initVersion(1);
	// if(infoParent!=null)
	// {
	// RestClient.getJsonAuth("account/kids",infoParent.getToken(),new
	// JsonHttpResponseHandler()
	// {
	// @Override
	// public void onSuccess(JSONObject arg0) {
	// super.onSuccess(arg0);
	// infoKid = mGson.fromJson(arg0.toString(), ResponseLoadKid.class);
	// Utils.printLogcatDebugMessage(TAG, "Kids Updating Response - "
	// +infoKid.getStatus()+":"+infoKid.getCode());
	// if(onAddNewKidOrUpdate!=null)
	// onAddNewKidOrUpdate.onAddOrUpdateSuccess(infoKid);
	// }
	// @Override
	// public void onFailure(Throwable arg0, JSONObject arg1) {
	// super.onFailure(arg0, arg1);
	// Log.w(TAG, "Kids Loading Error");
	// Log.w(TAG, arg1.toString());
	// ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
	// if(onAddNewKidOrUpdate!=null)
	// onAddNewKidOrUpdate.onAddOrUpdateFailure(r);
	// }
	// @Override
	// public void onFailure(Throwable error, String content) {
	// super.onFailure(error, content);
	// Log.w(TAG,"Kids Loading Error -- "+ content);
	//
	// }
	// });
	// }
	// }

	public void registerMeeper(RegisterMeeper user) {
		RestClient.initVersion(1);
		Utils.printLogcatDebugMessage(TAG, mGson.toJson(user));
		if (infoParent != null && user != null)
			RestClient.postJsonAuth("device/registration/"
					+ user.getSerial_no(), mGson.toJson(user), infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "Register Meep -- "+arg0.toString());
					ResponseBasic r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
					Utils.printLogcatDebugMessage(TAG, "Register Meep Response - "
							+ r.getStatus() + ":" + r.getCode());
					if (onRegisterMeeperListener != null)
						onRegisterMeeperListener.onRegisterMeeperSuccess();
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					Log.w(TAG, "Register Meep Error - " + arg1.toString());
					if (onRegisterMeeperListener != null)
						onRegisterMeeperListener.onRegisterMeeperFailure(r.getStatus()
								+ ":" + r.getCode());
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Register Meep Error -- " + content);
					if (onRegisterMeeperListener != null)
						onRegisterMeeperListener.onRegisterMeeperTimeout();
				}
			});
	}

	// ====Profile====
	public void getProfileParent() {
		RestClient.initVersion(1);
		if (infoParent != null) {
			// Utils.printLogcatDebugMessage(TAG,
			// "PROFILE parent -- "+infoParent.getToken());
			RestClient.getJsonAuth("account/profile", infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "PROFILE parent -- "+arg0.toString());
					ResponseProfileParent profileParent = mGson.fromJson(arg0.toString(), ResponseProfileParent.class);
					Utils.printLogcatDebugMessage(TAG, "PROFILE parent -- "
							+ profileParent.getStatus() + ":"
							+ profileParent.getCode());
					if (onLoadProfileParentListener != null)
						onLoadProfileParentListener.onLoadProfileSuccess(profileParent);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "PROFILE parent Loading Error");
					Log.w(TAG, arg1.toString());
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoadProfileParentListener != null)
						onLoadProfileParentListener.onLoadProfileFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "PROFILE parent Loading Error -- " + content);
				}
			});
		}

	}

	public void getProfileKid(String userId) {
		RestClient.initVersion(1);
		if (infoParent != null && userId != null) {
			RestClient.getJsonAuth("account/kids/profile/" + userId, infoParent.getToken(), new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					// Utils.printLogcatDebugMessage(TAG,
					// "PROFILE kid -- "+arg0.toString());
					ResponseProfileKid profileKid = mGson.fromJson(arg0.toString(), ResponseProfileKid.class);
					Utils.printLogcatDebugMessage(TAG, "PROFILE kid -- "
							+ profileKid.getStatus() + ":"
							+ profileKid.getCode());
					if (onLoadProfileKidListener != null)
						onLoadProfileKidListener.onLoadProfileSuccess(profileKid);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "PROFILE kid Loading Error");
					Log.w(TAG, arg1.toString());
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoadProfileKidListener != null)
						onLoadProfileKidListener.onLoadProfileFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "PROFILE kid Loading Error -- " + content);
				}
			});
		}

	}

	public void updateProfileParent(RegisterUser user) {
		RestClient.initVersion(1);
		if (infoParent != null) {
			RestClient.postJsonAuth("account/profile", mGson.toJson(user), infoParent.getToken(), new JsonHttpResponseHandler() {
				ResponseBasic r;

				@Override
				public void onSuccess(JSONObject arg0) {
					r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
					Utils.printLogcatDebugMessage(TAG, "PROFILE parent Updating Response - "
							+ r.getStatus() + ":" + r.getCode());
					if (onUpdateProfileParentListener != null)
						onUpdateProfileParentListener.onUpdateProfileParentSuccess(r);
					super.onSuccess(arg0);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					Log.w(TAG, "PROFILE parent Updating Error");
					Log.w(TAG, arg1.toString());
					if (onUpdateProfileParentListener != null)
						onUpdateProfileParentListener.onUpdateProfileParentFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "PROFILE parent Updating Error -- " + content);
					if (onUpdateProfileParentListener != null)
						onUpdateProfileParentListener.onUpdateProfileParentTimeout();

				}
			});
		}

	}

	public void updateProfileKid(String userId, RegisterMeeper meeper) {
		// Utils.printLogcatDebugMessage(TAG, "PROFILE kid Updating Response - "
		// + mGson.toJson(meeper));
		RestClient.initVersion(1);
		if (infoParent != null && userId != null && meeper != null) {
			RestClient.postJsonAuth("account/kids/profile/" + userId, mGson.toJson(meeper), infoParent.getToken(), new JsonHttpResponseHandler() {
				ResponseBasic r;

				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
					Utils.printLogcatDebugMessage(TAG, "PROFILE kid Updating Response - "
							+ r.getStatus() + ":" + r.getCode());
					if (onUpdateProfileKidListener != null)
						onUpdateProfileKidListener.onUpdateProfileKidSuccess(r);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					Log.w(TAG, "PROFILE kid Updating Error");
					Log.w(TAG, arg1.toString());
					if (onUpdateProfileKidListener != null)
						onUpdateProfileKidListener.onUpdateProfileKidFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "PROFILE kid Updating Error -- " + content);
					if (onUpdateProfileKidListener != null)
						onUpdateProfileKidListener.onUpdateProfileKidTimeout();
				}
			});
		}

	}

	/**
	 * Get Kid's userid in server side
	 * 
	 * @param sn
	 * @param token
	 */
	public void getChildUserId(String sn, String token) {
		RestClient.initVersion(1);
		if (sn != null && token != null) {
			RestClient.getJsonAuth2("device/owner/" + sn, token, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					Utils.printLogcatDebugMessage(TAG, "Owner ID Loading Success");
					Utils.printLogcatDebugMessage(TAG, arg0.toString());
					try {
						if (onGetOwnerIdListener != null)
							onGetOwnerIdListener.onGetSuccess(arg0.getString("userid"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Owner ID Loading Error");
					Log.w(TAG, arg1.toString());
					if (onGetOwnerIdListener != null)
						onGetOwnerIdListener.onGetFailure();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, content);
					if (onGetOwnerIdListener != null)
						onGetOwnerIdListener.onGetTimeout();
				}
			});
		} else {
			if (onGetOwnerIdListener != null)
				onGetOwnerIdListener.onGetTimeout();
		}
	}

	/**
	 * send confirm letter email again
	 * 
	 * @param email
	 */
	public void resendConfirmEmail(String email) {
		if (email != null && !email.isEmpty()) {
			RestClient.initVersion(1);
			Supervisor s = new Supervisor();
			s.setEmail(email.toLowerCase());

			RestClient.postJson("account/confirmemail/resend", mGson.toJson(s), new JsonHttpResponseHandler() {
				ResponseBasic r;

				@Override
				public void onSuccess(JSONObject arg0) {
					super.onSuccess(arg0);
					Utils.printLogcatDebugMessage(TAG, "Resend Email Success");
					Utils.printLogcatDebugMessage(TAG, arg0.toString());
					r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
					if (resendEmailListener != null)
						resendEmailListener.onResendSuccess(r);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Resend Email Error");
					Log.w(TAG, arg1.toString());
					r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (resendEmailListener != null)
						resendEmailListener.onResendFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, content);
					if (resendEmailListener != null)
						resendEmailListener.onResendTimeout();
				}
			});
		}

	}

	public void getKidNumber() {
		RestClient.initVersion(1);
		if (infoParent != null) {
			Utils.printLogcatDebugMessage(TAG, "token:" + infoParent.getToken());
			RestClient.getJsonAuth("account/kids", infoParent.getToken(), new JsonHttpResponseHandler() {
				ResponseBasic r;

				@Override
				public void onSuccess(JSONObject arg0) {
					// Utils.printLogcatDebugMessage(TAG,
					// "Kids -- "+arg0.toString());
					infoKid = mGson.fromJson(arg0.toString(), ResponseLoadKid.class);
					Utils.printLogcatDebugMessage(TAG, "Kids Loading Response - "
							+ infoKid.getStatus() + ":" + infoKid.getCode());
					// Utils.printLogcatDebugMessage(TAG,
					// "Kids Json -- "+mGson.toJson(infoKid));
					if (infoKid.getKids().length == 0) {
						infoParent.setStage(ResponseLogin.STAGE_CREATE_ACCOUNT);
					} else {
						infoParent.setStage(ResponseLogin.STAGE_NORMAL);
					}
					if (onLoginListener != null)
						onLoginListener.onLoginSuccess(infoParent);
					super.onSuccess(arg0);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					Log.w(TAG, "Kids Loading Error");
					Log.w(TAG, arg1.toString());
					r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					if (onLoginListener != null)
						onLoginListener.onLoginFailure(r);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, "Kids Loading Error -- " + content);
					if (onLoginListener != null)
						onLoginListener.onLoginTimeOut();
				}
			});
		}

	}

	public void uploadAvatar(Bitmap avatar, final boolean isParent) {
		RestClient.initVersion(1);
		if (infoParent != null) {
			RestClient.uploadImage("objectstore/avatar", avatar, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(String arg0) {
					super.onSuccess(arg0);
					Utils.printLogcatDebugMessage(TAG, "UPLOAD IMAGE - "
							+ arg0.toString());
					ResponseFeedback r = mGson.fromJson(arg0.toString(), ResponseFeedback.class);
					Utils.printLogcatDebugMessage(TAG, "UPLOAD IMAGE - "
							+ r.getStatus() + ":" + r.getCode());
					if (isParent)
						onUploadAvatarParentListener.onUploadAvatarParentSuccess(r);
					else
						onUploadAvatarKidListener.onUploadAvatarKidSuccess(r);
				}

				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					super.onFailure(arg0, arg1);
					ResponseBasic r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
					Utils.printLogcatDebugMessage(TAG, "UPLOAD IMAGE - "
							+ r.getStatus() + ":" + r.getCode());
					Log.w(TAG, arg1.toString());
					if (isParent)
						onUploadAvatarParentListener.onUploadAvatarParentFailure(r);
					else
						onUploadAvatarKidListener.onUploadAvatarKidFailure(r);
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					Log.w(TAG, content);
					ResponseBasic r = new ResponseBasic();
					r.setCode(500);
					r.setStatus(content);
					Utils.printLogcatDebugMessage(TAG, "UPLOAD IMAGE - "
							+ r.getStatus() + ":" + r.getCode());
					if (isParent)
						onUploadAvatarParentListener.onUploadAvatarParentFailure(r);
					else
						onUploadAvatarKidListener.onUploadAvatarKidFailure(r);
				}
			});

		}
	}

	public void migrateChild(MigrateChild child) {
		RestClient.initVersion(1);
		RestClient.postJsonAuth("device/migrate", mGson.toJson(child), infoParent.getToken(), new JsonHttpResponseHandler() {
			ResponseBasic r;

			@Override
			public void onSuccess(JSONObject arg0) {
				super.onSuccess(arg0);
				r = mGson.fromJson(arg0.toString(), ResponseBasic.class);
				Utils.printLogcatDebugMessage(TAG, "migrate sucess "
						+ arg0.toString());
				if (onMigrateChild != null)
					onMigrateChild.onMigrateSuccess();
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				r = mGson.fromJson(arg1.toString(), ResponseBasic.class);
				Utils.printLogcatDebugMessage(TAG, "migrate fail "
						+ arg1.toString());
				if (onMigrateChild != null)
					onMigrateChild.onMigrateFailure(r);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Utils.printLogcatDebugMessage(TAG, "migrate fail " + content);
				if (onMigrateChild != null)
					onMigrateChild.onMigrateTimeout();
			}
		});
	}

	RequestRespondedListener responseListener;
	OnSnVerifyListener onSnVerifyListener;
	OnRegisterListener onRegisterListener;
	OnRegisterMeeperListener onRegisterMeeperListener;
	OnPasswordSentListener onPasswordSentListener;
	OnLoginListener onLoginListener;
	OnLoadKidListener onLoadKidListener;
	OnUploadAvatarParentListener onUploadAvatarParentListener;
	OnUploadAvatarKidListener onUploadAvatarKidListener;
	OnUpdateProfileParentListener onUpdateProfileParentListener;
	OnUpdateProfileKidListener onUpdateProfileKidListener;
	OnAllocateCoinsListener onAllocateCoinsListener;
	OnLoadLogsListener onLoadLogsListener;
	OnLoadNotificationsListener onLoadNotificationsListener;
	OnUpdateCoinListener onUpdateCoinListener;
	OnLoadPortalSettingsListener onLoadPortalSettingsListener;
	OnUpdatePortalSettingsListener onUpdatePortalSettingsListener;
	OnLoadProfileParentListener onLoadProfileParentListener;
	OnLoadProfileKidListener onLoadProfileKidListener;
	OnGetOwnerIdListerner onGetOwnerIdListener;
	ResendEmailListener resendEmailListener;
	OnMigrateChild onMigrateChild;

	public OnMigrateChild getOnMigrateChild() {
		return onMigrateChild;
	}

	public void setOnMigrateChild(OnMigrateChild onMigrateChild) {
		this.onMigrateChild = onMigrateChild;
	}

	public ResendEmailListener getResendEmailListener() {
		return resendEmailListener;
	}

	public void setResendEmailListener(ResendEmailListener resendEmailListener) {
		this.resendEmailListener = resendEmailListener;
	}

	public OnGetOwnerIdListerner getOnGetOwnerIdListener() {
		return onGetOwnerIdListener;
	}

	public void setOnGetOwnerIdListener(
			OnGetOwnerIdListerner onGetOwnerIdListener) {
		this.onGetOwnerIdListener = onGetOwnerIdListener;
	}

	public OnUpdateCoinListener getOnUpdateCoinListener() {
		return onUpdateCoinListener;
	}

	public void setOnUpdateCoinListener(
			OnUpdateCoinListener onUpdateCoinListener) {
		this.onUpdateCoinListener = onUpdateCoinListener;
	}

	public OnAllocateCoinsListener getOnAllocateCoinsListener() {
		return onAllocateCoinsListener;
	}

	public void setOnAllocateCoinsListener(
			OnAllocateCoinsListener onAllocateCoinsListener) {
		this.onAllocateCoinsListener = onAllocateCoinsListener;
	}

	public OnLoadKidListener getOnLoadKidListener() {
		return onLoadKidListener;
	}

	public void setOnLoadKidListener(OnLoadKidListener onLoadKidListener) {
		this.onLoadKidListener = onLoadKidListener;
	}

	public RequestRespondedListener getResponseListener() {
		return responseListener;
	}

	public void setResponseListener(RequestRespondedListener responseListener) {
		this.responseListener = responseListener;
	}

	public OnSnVerifyListener getOnSnVerifyListener() {
		return onSnVerifyListener;
	}

	public void setOnSnVerifyListener(OnSnVerifyListener onSnVerifyListener) {
		this.onSnVerifyListener = onSnVerifyListener;
	}

	public OnRegisterListener getOnRegisterListener() {
		return onRegisterListener;
	}

	public void setOnRegisterListener(OnRegisterListener onRegisterListener) {
		this.onRegisterListener = onRegisterListener;
	}

	public OnRegisterMeeperListener getOnRegisterMeeperListener() {
		return onRegisterMeeperListener;
	}

	public void setOnRegisterMeeperListener(
			OnRegisterMeeperListener onRegisterMeeperListener) {
		this.onRegisterMeeperListener = onRegisterMeeperListener;
	}

	public OnPasswordSentListener getOnPasswordSentListener() {
		return onPasswordSentListener;
	}

	public void setOnPasswordSentListener(
			OnPasswordSentListener onPasswordSentListener) {
		this.onPasswordSentListener = onPasswordSentListener;
	}

	public OnLoginListener getOnLoginListener() {
		return onLoginListener;
	}

	public void setOnLoginListener(OnLoginListener onLoginListener) {
		this.onLoginListener = onLoginListener;
	}

	public OnUploadAvatarParentListener getOnUploadAvatarParentListener() {
		return onUploadAvatarParentListener;
	}

	public void setOnUploadAvatarParentListener(
			OnUploadAvatarParentListener onUploadAvatarParentListener) {
		this.onUploadAvatarParentListener = onUploadAvatarParentListener;
	}

	public OnUploadAvatarKidListener getOnUploadAvatarKidListener() {
		return onUploadAvatarKidListener;
	}

	public void setOnUploadAvatarKidListener(
			OnUploadAvatarKidListener onUploadAvatarKidListener) {
		this.onUploadAvatarKidListener = onUploadAvatarKidListener;
	}

	public OnUpdateProfileParentListener getOnUpdateProfileParentListener() {
		return onUpdateProfileParentListener;
	}

	public void setOnUpdateProfileParentListener(
			OnUpdateProfileParentListener onUpdateProfileParentListener) {
		this.onUpdateProfileParentListener = onUpdateProfileParentListener;
	}

	public OnUpdateProfileKidListener getOnUpdateProfileKidListener() {
		return onUpdateProfileKidListener;
	}

	public void setOnUpdateProfileKidListener(
			OnUpdateProfileKidListener onUpdateProfileKidListener) {
		this.onUpdateProfileKidListener = onUpdateProfileKidListener;
	}

	public OnLoadLogsListener getOnLoadLogsListener() {
		return onLoadLogsListener;
	}

	public void setOnLoadLogsListener(OnLoadLogsListener onLoadLogsListener) {
		this.onLoadLogsListener = onLoadLogsListener;
	}

	public OnLoadNotificationsListener getOnLoadNotificationsListener() {
		return onLoadNotificationsListener;
	}

	public void setOnLoadNotificationsListener(
			OnLoadNotificationsListener onLoadNotificationsListener) {
		this.onLoadNotificationsListener = onLoadNotificationsListener;
	}

	public OnLoadPortalSettingsListener getOnLoadPortalSettingsListener() {
		return onLoadPortalSettingsListener;
	}

	public void setOnLoadPortalSettingsListener(
			OnLoadPortalSettingsListener onLoadPortalSettingsListener) {
		this.onLoadPortalSettingsListener = onLoadPortalSettingsListener;
	}

	public OnUpdatePortalSettingsListener getOnUpdatePortalSettingsListener() {
		return onUpdatePortalSettingsListener;
	}

	public void setOnUpdatePortalSettingsListener(
			OnUpdatePortalSettingsListener onUpdatePortalSettingsListener) {
		this.onUpdatePortalSettingsListener = onUpdatePortalSettingsListener;
	}

	public OnLoadProfileParentListener getOnLoadProfileParentListener() {
		return onLoadProfileParentListener;
	}

	public void setOnLoadProfileParentListener(
			OnLoadProfileParentListener onLoadProfileParentListener) {
		this.onLoadProfileParentListener = onLoadProfileParentListener;
	}

	public OnLoadProfileKidListener getOnLoadProfileKidListener() {
		return onLoadProfileKidListener;
	}

	public void setOnLoadProfileKidListener(
			OnLoadProfileKidListener onLoadProfileKidListener) {
		this.onLoadProfileKidListener = onLoadProfileKidListener;
	}

}
