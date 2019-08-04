package com.oregonscientific.meep.together.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.CustomDialog.OnClickOkButtonListener;
import com.oregonscientific.meep.together.activity.ImageThreadLoader.ImageLoadedListener;
import com.oregonscientific.meep.together.bean.LoginUser;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseLogin;
import com.oregonscientific.meep.together.library.database.DatabaseHelper;
import com.oregonscientific.meep.together.library.database.table.AuthInfo;
import com.oregonscientific.meep.together.library.database.table.TableUser;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;

public class UserFunction {

	private static DatabaseHelper dataHelp;
	private static RestClientUsage restHelp = new RestClientUsage();
	public static boolean isTerms = false;
	public static String currentKid;
	public static boolean isOnline = false;
	public static ImageThreadLoader imageLoader = new ImageThreadLoader();
	private static final String PREFERENCE_KEY_TOKEN = "token";
	private static final String PREFERENCE_KEY_MEEPTAG = "meeptag";
	private static final String PREFERENCE_KEY_IDENTITY_STRING = "identity";
	private static final String PREFERENCE_KEY_ID = "id";
	private static final String PREFERENCE_FILE_NAME = "user-info";
	private static final String PREFERENCE_FILE_NAME_VERIFY_STATUS="verify-status";
	private static HashMap<String, Long> appsList = new HashMap<String,Long>();
	private static HashMap<String, Component> appsComponentsList = new HashMap<String,Component>();
	

	private static TableUser tableuser;

	public static LoginUser generateLoginUser(String email, String password) {
		// Building Parameters
		LoginUser user = new LoginUser();
		user.setPassword(password);
		user.setEmail(email);
		return user;
	}

	public String registerUser(String name, String email, String password) {
		// Building Parameters
		return "";
	}

	/**
	 * Function to logout user Reset Database
	 * */
	public boolean logoutUser(Context context) {

		return true;
	}

	/**
	 * Function to disable or release all views
	 * 
	 * @param enable
	 * @param layout
	 */
	public static void disableAllViewsController(boolean enable,
			ViewGroup layout) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			View child = layout.getChildAt(i);
			child.setEnabled(enable);
			if (child instanceof ViewGroup) {
				disableAllViewsController(enable, (ViewGroup) child);
			}
		}
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// networkInfo will be null if no network is available, otherwise
		// connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static TableUser getUserInfo() {
		return getDataHelp().getUserInfoFromDB();
	}

	public static void storeUserInDatabase(ResponseLogin lr, LoginUser user) {
		// getDataHelp().dropAll();
		// // //create TableUser in database
		// getDataHelp().createAll();
		// //delete data in database
		getDataHelp().deleteAll();
		// //insert
		tableuser = generateTableUser(lr);
		getDataHelp().insertUser(tableuser.getInsertSql());
		AuthInfo authinfo = generateAuthInfo(user);
		getDataHelp().insertUser(authinfo.getInsertSql());
	}

	public static TableUser generateTableUser(ResponseLogin info) {
		TableUser tableuser = new TableUser();
		tableuser.setFirstName(info.getFirstName());
		tableuser.setLastName(info.getLastName());
		tableuser.setToken(info.getToken());
		tableuser.setIconAddr(info.getAvatar());
		tableuser.setCoins(info.getCoins());
		return tableuser;
	}

	public static AuthInfo generateAuthInfo(LoginUser user) {
		AuthInfo auth = new AuthInfo();
		auth.setEmail(user.getEmail());
		auth.setPassword(user.getPassword());
		return auth;
	}

	public static void sendSavePermission(Permissions p) {
		restHelp.savePermission(UserFunction.currentKid, p);
	}

	// databse
	public static DatabaseHelper getDataHelp() {
		return dataHelp;
	}

	public static void setDataHelp(DatabaseHelper dataHelp) {
		UserFunction.dataHelp = dataHelp;
	}
	public static void disableDataHelp() {
		DatabaseHelper.close();
		UserFunction.dataHelp = null;
		
	}

	public static RestClientUsage getRestHelp() {
		return restHelp;
	}

	public static ImageThreadLoader getImageLoader() {
		return imageLoader;
	}

	// toast
	// //toast
	// public static void toastMessage(int resId,Context context)
	// {
	// String toastMessage = context.getResources().getString(resId);
	// Toast toast = Toast.makeText(context,
	// toastMessage, Toast.LENGTH_LONG);
	// toast.setGravity(Gravity.CENTER, 0, 0);
	// toast.show();
	// }
	// public static void toastMessageString(String s,Context context)
	// {
	// Toast toast = Toast.makeText(context,
	// s, Toast.LENGTH_LONG);
	// toast.setGravity(Gravity.CENTER, 0, 0);
	// toast.show();
	// }
	public static void openWifiSettings(Context context) {

		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void popupMessage(int resId, final Context context,
			MyProgressDialog loading) {
		if (loading != null)
			loading.dismiss();
		if(resId == 0) resId = R.string.please_retry;
		
		CustomDialog dialog = new CustomDialog(context, resId);
		if(resId == R.string.no_network)
		{
			dialog.setOnClickOkButtonListener(new OnClickOkButtonListener() {
				
				@Override
				public void onClickOk() {
					openWifiSettings(context);
				}
			});
		}
		dialog.show();
	}
	public static void popupResponse(String status, Context context,
			MyProgressDialog loading) {
		int resId = context.getResources().getIdentifier(
				UserFunction.StringFilter(status), "string",
				context.getPackageName());
		if (resId == 0) {
			resId = R.string.please_retry;
		}
		popupMessage(resId, context, loading);
	}

	public static MyProgressDialog initLoading(Context context) {
		MyProgressDialog loading = new MyProgressDialog(context);
		loading.setMessage(context.getResources().getString(
				R.string.loading_text));
		loading.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		return loading;
	}

	// public static void popupMessageString(String s, Context context) {
	// // AlertDialog.Builder builder = new AlertDialog.Builder(context);
	// // builder.setMessage(s)
	// // .setCancelable(false)
	// // .setPositiveButton(android.R.string.ok, new
	// // DialogInterface.OnClickListener() {
	// // public void onClick(DialogInterface dialog, int id) {
	// // //do things
	// // }
	// // });
	// // AlertDialog alert = builder.create();
	// // alert.show();
	// CustomDialog dialog = new CustomDialog(context, s);
	// dialog.show();
	// }

	public static TableUser getUser() {
		return tableuser;
	}

	public static void setUser(TableUser u) {
		tableuser = u;
	}

	public static boolean checkBirthday(String year, String month, String day) {
		SimpleDateFormat fy = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		Date birthday;
		try {
			birthday = fy.parse(year + "-" + month + "-" + day);
			if (birthday.getYear() > 0
					&& birthday.getYear() < today.getYear() + 2) {
				Utils.printLogcatDebugMessage( birthday.toString());
				Utils.printLogcatDebugMessage( birthday.getMonth() + "");
				if (birthday.getMonth() + 1 == Integer.parseInt(month)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static void loadImage(final ImageView image, String imageUrl) {
		// ***image***
		Bitmap cachedImage = null;
		try {
			cachedImage = getImageLoader().loadImage(imageUrl,
					new ImageLoadedListener() {
						public void imageLoaded(Bitmap imageBitmap) {
							// notifyDataSetChanged();
							image.setImageBitmap(imageBitmap);
						}
					});
		} catch (MalformedURLException e) {
			Log.e("ICON", "Bad remote image URL: " + e.toString());
		}
		if (cachedImage != null) {
			image.setImageBitmap(cachedImage);
		}
	}

	public static String getSerialNumber() {
		// Get the text file
		File file = new File("/mnt/private/sn.txt");
		String line = null;
		// Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				text.append(line);
			}
		} catch (IOException e) {
			// You'll need to add proper error handling here
		}

		// normal return
		return text.toString().trim();

		// non-register sn
//		 return "ZZ0000SYZ58";

		// register sn
//		 return "ZZ0000ABOWB";
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		String regEx = "[^a-zA-Z]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll(" ").trim().replaceAll(" +", "_");
	}

	public static String getCurrentTimeStamp() {
		Long tsLong = System.currentTimeMillis() / 1000;
		String ts = tsLong.toString();
		System.out.println(ts);
		return ts;
	}

//	// ****for notification***//
//	public static String filterSecondName(String message) {
//		String friendName = message.substring(message.indexOf('\'') + 1,
//				message.lastIndexOf('\''));
//		return friendName;
//	}
//
//	public static String filterFirstName(String message) {
//		String[] name = message.split(" ");
//		if(name[0].equals("wants"))
//		{
//			return MeepTogetherMainActivity.currentKidName;
//		}
//		return name[0];
//	}
	
	public static String getAccountToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(PREFERENCE_KEY_TOKEN, "");
	}
//	public static String getAccountMeeptag(Context context) {
//		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
//		return sp.getString(PREFERENCE_KEY_MEEPTAG, "");
//	}
	public static String getAccountID(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(PREFERENCE_KEY_ID, "");
	}
	public static Identity getAccountIdentity(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		String json = sp.getString(PREFERENCE_KEY_IDENTITY_STRING, "");
		if(!json.equals(""))
		{
			Utils.printLogcatDebugMessage(json);
			return mGson.fromJson(json, Identity.class);
		}
		return null;
	}

	private static Gson mGson = new Gson();
	public static void setAccountInformation(Context context,Account account) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.putString(PREFERENCE_KEY_TOKEN ,account.getToken());
		edit.putString(PREFERENCE_KEY_MEEPTAG ,account.getMeepTag());
		edit.putString(PREFERENCE_KEY_IDENTITY_STRING,mGson.toJson(account.getIdentity()));
		edit.putString(PREFERENCE_KEY_ID,account.getId());
		edit.commit();
	}
	/**
	 * 
	 * @param id
	 * @param appname
	 */
	public static void recordAppId(String appName,long id)
	{
		appsList.put(appName,id);
	}
	/**
	 * 
	 * @param appname
	 * @return
	 */
	public static long getAppId(String appName)
	{
		return appsList.get(appName);
	}
	
	public static void recordAppComponent(String appName,Component component)
	{
		appsComponentsList.put(appName, component);
	}
	
	public static Component getAppComponent(String appName)
	{
		return appsComponentsList.get(appName);
	}
	
	private static final String PREFERENCE_KEY_CREDIT_CARD_VERFIED = "credit_card_verfied";
	public static boolean isCreditCardVerified(Context context){
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME_VERIFY_STATUS, Context.MODE_PRIVATE);
		verified =  sp.getBoolean(PREFERENCE_KEY_CREDIT_CARD_VERFIED, true);
		return verified;
	}
	private static boolean verified = true;
	public static boolean isCreditCardVerified(){
		return verified;
	}
	
	public static  void setCreditCardVerfied(Context context,boolean v) {
		verified = v;
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILE_NAME_VERIFY_STATUS, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.putBoolean(PREFERENCE_KEY_CREDIT_CARD_VERFIED,v);
		edit.commit();
	}
	
	public static void hideKeyboard(View v,Context context) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
