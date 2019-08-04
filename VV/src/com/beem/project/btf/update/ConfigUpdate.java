package com.beem.project.btf.update;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.beem.project.btf.R;

public class ConfigUpdate {
	private static final String TAG = "Config";
	public static final String UPDATE_SAVENAME = "update.apk";
	// public static final String UPDATE_SERVER =
	// "http://192.168.12.114/web/myapp/";
	public static final String UPDATE_SERVER = "http://192.168.12.38/web/apps/web/myapp/";
	public static final String UPDATE_APKNAME = "update.apk";
	public static final String UPDATE_VERJSON = "ver.json";

	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					"com.beem.project.btf", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					"com.beem.project.btf", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}
	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}
	public static void update2(Context context) {
		try {
			String verjson = NetworkToolUpdate
					.getContent(ConfigUpdate.UPDATE_SERVER
							+ ConfigUpdate.UPDATE_VERJSON);
			Log.i("info", "newVerJSON=" + verjson);
			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				int vercode = ConfigUpdate.getVerCode(context);
				JSONObject obj = array.getJSONObject(0);
				int newVerCode = Integer.parseInt(obj.getString("verCode"));
				String newVerName = obj.getString("verName");
				Log.i("info", "newVerCode=" + newVerCode);
				Log.i("info", "newVerName=" + newVerName);
				//LogUtils.i("vercode=" + vercode + ";    vercode=" + vercode + ";    getServerVerCode()=");
				if (newVerCode > vercode) {
					doNewVersionUpdate(context, newVerName, vercode);
				} else {
					notNewVersionShow(context);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void notNewVersionShow(Context context) {
		int verCode = ConfigUpdate.getVerCode(context);
		String verName = ConfigUpdate.getVerName(context);
		StringBuffer sb = new StringBuffer();
		sb.append(context.getString(R.string.vvim_current_version));
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(context.getString(R.string.vvim_new_version));
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.vvim_software_update))
				.setMessage(sb.toString())
				.setPositiveButton(context.getString(R.string.OkButton),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		dialog.show();
	}
	private static void doNewVersionUpdate(final Context context,
			String newVerName, int newVerCode) {
		int verCode = ConfigUpdate.getVerCode(context);
		String verName = ConfigUpdate.getVerName(context);
		StringBuffer sb = new StringBuffer();
		sb.append(context.getString(R.string.vvim_current_version));
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(context.getString(R.string.vvim_discover_new_version));
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(context.getString(R.string.vvim_whether_the_update));
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.vvim_software_update))
				.setMessage(sb.toString())
				.setPositiveButton(context.getString(R.string.UpdateButton),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent(context,
										UpdateService.class);
								intent.putExtra(
										"app_name",
										context.getResources().getString(
												R.string.app_name));
								context.startService(intent);
							}
						})
				.setNegativeButton(
						context.getString(R.string.vvim_temporarily_update),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						}).create();
		dialog.show();
	}
	public static void update(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), ConfigUpdate.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
