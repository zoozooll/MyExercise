package com.oregonscientific.meep.browser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.oregonscientific.meep.Build;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.customdialog.CommonPopup;

public class BrowserUtility {
	Gson mGson = new Gson();

	public static boolean isValidDomain(String url) {
		String regex = new StringBuilder().append("((?:(http|https|Http|Https|rtsp|Rtsp):").append("\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)").append("\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_").append("\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?").append("((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+") // named
																																																																																															// host
		.append("(?:") // plus top level domain
		.append("(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])").append("|(?:biz|b[abdefghijmnorstvwyz])").append("|(?:cat|com|coop|c[acdfghiklmnoruvxyz])").append("|d[ejkmoz]").append("|(?:edu|e[cegrstu])").append("|f[ijkmor]").append("|(?:gov|g[abdefghilmnpqrstuwy])").append("|h[kmnrtu]").append("|(?:info|int|i[delmnoqrst])").append("|(?:jobs|j[emop])").append("|k[eghimnrwyz]").append("|l[abcikrstuvy]").append("|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])").append("|(?:name|net|n[acefgilopruz])").append("|(?:org|om)").append("|(?:pro|p[aefghklmnrstwy])").append("|qa").append("|r[eouw]").append("|s[abcdeghijklmnortuvyz]").append("|(?:tel|travel|t[cdfghjklmnoprtvwz])").append("|u[agkmsyz]").append("|v[aceginu]").append("|w[fs]").append("|y[etu]").append("|z[amw]))").append("|(?:(?:25[0-5]|2[0-4]") // or
																																																																																																																																																																																																										// ip
																																																																																																																																																																																																										// address
		.append("[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]").append("|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1]").append("[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}").append("|[1-9][0-9]|[0-9])))").append("(?:\\:\\d{1,5})?)") // plus
																																																																										// option
																																																																										// port
																																																																										// number
		.append("(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~") // plus
																	// option
																	// query
																	// params
		.append("\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?").append("(?:\\b|$)").toString();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}

	public static void hideKeyboard(Context context, View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		// networkInfo will be null if no network is available, otherwise
		// connected
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static String getDirs(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}

	public static String readFile(String path) {
		// Get the text file
		File file = new File(path);
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
		Log.d("test", text.toString());
		return text.toString();
	}

	public static String getAccountToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(Consts.PREFERENCE_KEY_TOKEN, "");
	}

	// public static String getAccountMeeptag(Context context) {
	// SharedPreferences sp =
	// context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME,
	// Context.MODE_PRIVATE);
	// return sp.getString(Consts.PREFERENCE_KEY_MEEPTAG, "");
	// }
	public static String getAccountID(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(Consts.PREFERENCE_KEY_ID, "");
	}

	public static void setAccountInformation(Context context, Account account) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.putString(Consts.PREFERENCE_KEY_TOKEN, account.getToken());
		edit.putString(Consts.PREFERENCE_KEY_FIRSTNAME, account.getFirstName());
		edit.putString(Consts.PREFERENCE_KEY_LASTNAME, account.getLastName());
		edit.putString(Consts.PREFERENCE_KEY_MEEPTAG, account.getMeepTag());
		edit.putString(Consts.PREFERENCE_KEY_ID, account.getId());
		edit.commit();
	}

	public static void alertMessage(Context context, String title,
			String message) {
		CommonPopup popup = new CommonPopup(context, title, message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public static void alertMessage(Context context, int title, int message) {
		CommonPopup popup = new CommonPopup(context, title, message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public static void alertDebugMessage(Context context, String message) {
		CommonPopup popup = new CommonPopup(context, "DEBUG", "FOR DEBUG: "
				+ message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public static void printLogcatDebugMessage(String text) {
		if (BuildConfig.DEBUG)
			Log.d("MeepWebBrowser", text);
	}

	static long timestamp = 0;
	static long diff = 0;
	static String title = "";

	public static void printLogcatMessageWithTimeStamp(String text) {
		if(Build.DEBUG)
		{
			long currentTime = System.currentTimeMillis();
			Log.d("MeepWebBrowser","timestamp:"+ currentTime + " " + text);
			//TODO:
			// record(text, currentTime);
		}

	}
	public static synchronized void record(String text,long currentTime)
	{
		if (text.equals("EOF")) {
			timestamp = 0;
			new RecordTimeTask().execute("\n");
		} else {

			if (timestamp != 0) {
				diff = currentTime - timestamp;
				new RecordTimeTask().execute(diff + "ms : " + text);
			}
			timestamp = currentTime;
		}
	}

	public static Message generateAlertMessageObject() {
		Bundle bundle = new Bundle();
		bundle.putInt(Consts.BUNDLE_KEY_ALERT_MESSAGE_CONTENT, R.string.cannot_use_now);
		Message message = new Message();
		message.what = Consts.MESSAGE_WHAT_ALERT_MESSAGE_CONTENT;
		message.setData(bundle);
		return message;

	}

	public static void openWifiSettings(Context context) {

		final Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
		intent.setComponent(cn);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
