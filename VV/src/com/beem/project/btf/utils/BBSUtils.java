package com.beem.project.btf.utils;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.BuildConfig;
import com.beem.project.btf.R;
import com.beem.project.btf.R.drawable;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.butterfly.vv.db.SQLdm;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact.PhoneNumState;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact.PhoneNumWhere;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @date 2013-11-16-下午12:48:43
 * @author yuedong bao
 * @func 工具类
 */
public class BBSUtils {
	private static final String TAG = "BBSUtils";
	/*
	 * 16进制数字字符集
	 */
	private static String hexString = "0123456789ABCDEF";
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	/**
	 * @Title: lonlat2Distance
	 * @Description: 根据经纬度计算距离
	 * @param lat_a
	 * @param lng_a
	 * @param lat_b
	 * @param lng_b
	 * @return
	 * @return: double (m)
	 */
	public static double latlon2Distance(double lat_a, double lng_a,
			double lat_b, double lng_b) {
		double pk = 180 / Math.PI;
		double a1 = lat_a / pk;
		double a2 = lng_a / pk;
		double b1 = lat_b / pk;
		double b2 = lng_b / pk;
		double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
		double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
		double t3 = Math.sin(a1) * Math.sin(b1);
		double tt = Math.acos(t1 + t2 + t3);
		return 6378137 * tt;
	}
	public static double latlon2Distance(String lat_a, String lng_a,
			String lat_b, String lng_b) {
		return latlon2Distance(Double.parseDouble(lat_a),
				Double.parseDouble(lng_a), Double.parseDouble(lat_b),
				Double.parseDouble(lng_b));
	}
	public static String TimeStamp2Date(String timestampString) {
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
				timestamp));
		return date;
	}
	public static String TimeStamp2DateLong(Long timestampString) {
		Long timestamp = (long) (timestampString * 1000);
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
				timestamp));
		return date;
	}
	public static void insertImg2EditText(Context context,
			EditText mInputField, int resId, String value) {
		ImageSpan is = new ImageSpan(context, resId);
		SpannableString expression = new SpannableString(value);
		expression.setSpan(is, 0, value.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		int index = mInputField.getSelectionStart();
		Editable text = mInputField.getText();
		if (index >= 0 && index < text.length()) {
			text.insert(index, expression);
		} else {
			text.append(expression);
		}
	}
	public static void insertImg2EditText(Context context,
			EditText mInputField, Bitmap bitmap, String value) {
		ImageSpan is = new ImageSpan(context, bitmap);
		SpannableString expression = new SpannableString(value);
		expression.setSpan(is, 0, value.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		int index = mInputField.getSelectionStart();
		Editable text = mInputField.getText();
		if (index >= 0 && index < text.length()) {
			text.insert(index, expression);
		} else {
			text.append(expression);
		}
	}
	public static void loadExpression(String regularExpression,
			ArrayList<Integer> ids, HashMap<Integer, String> names) {
		Class<drawable> drawable = R.drawable.class;
		Field[] fields = drawable.getDeclaredFields();
		for (Field field : fields) {
			String name = field.getName();
			if (field.getName().matches(regularExpression)) {
				try {
					int id = field.getInt(null);
					ids.add(id);
					names.put(id, name);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void getImageView(String uri, ImageView tImageView,
			ImageLoadingListener listener, DisplayImageOptions... option) {
		if (uri == null || uri.equals("")) {
			return;
		}
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options;
		if (option == null || option.length < 1) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.deafult_imgloading)
					.showImageForEmptyUri(R.drawable.deafult_imgloading)
					.showImageOnFail(R.drawable.deafult_imgloading)
					.cacheInMemory(true).cacheOnDisk(true).build();
		} else {
			options = option[0];
		}
		ImageLoadingListener animateFirstListener = listener;
		imageLoader
				.displayImage(uri, tImageView, options, animateFirstListener);
	}
	public static void getImageView(String uri, ImageView tImageView,
			boolean... isThumb) {
		if (uri == null || uri.equals("")) {
			return;
		}
		if (isThumb != null && isThumb.length > 0 && isThumb[0]) {
			uri += "&type=thumb";
		}
		getImageView(uri, tImageView, (ImageLoadingListener) null);
	}
	public static void getImageView(Uri uri, ImageView tImageView,
			boolean... isThumb) {
		if (uri == null) {
			return;
		}
		String uriString = uri.getPath();
		if (isThumb != null && isThumb.length > 0 && isThumb[0]) {
			uriString += "&type=thumb";
		}
		getImageView(uriString, tImageView, (ImageLoadingListener) null);
	}
	public static void closeWindowKeyBoard(Context mcontext) {
		InputMethodManager imm = (InputMethodManager) mcontext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive())
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public static void hideSystemKeyBoard(Context context, View v) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive())
			inputMethodManager.toggleSoftInput(
					InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
	}
	public static void showSystemKeyBoard(final Context context, final View v) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
	/**
	 * @param mContext
	 * @param unit 传入value的类型：TypedValue.COMPLEX_UNIT_PX
	 *            TypedValue.COMPLEX_UNIT_DIPTypedValue.COMPLEX_UNIT_SP TypedValue.COMPLEX_UNIT_PT
	 *            TypedValue.COMPLEX_UNIT_IN TypedValue.COMPLEX_UNIT_MM
	 * @param value
	 * @return 像素值
	 */
	public static float toPixel(Context mContext, int unit, float value) {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		switch (unit) {
			case TypedValue.COMPLEX_UNIT_PX:
				return value;
			case TypedValue.COMPLEX_UNIT_DIP:
				return value * metrics.density;
			case TypedValue.COMPLEX_UNIT_SP:
				return value * metrics.scaledDensity;
			case TypedValue.COMPLEX_UNIT_PT:
				return value * metrics.xdpi * (1.0f / 72);
			case TypedValue.COMPLEX_UNIT_IN:
				return value * metrics.xdpi;
			case TypedValue.COMPLEX_UNIT_MM:
				return value * metrics.xdpi * (1.0f / 25.4f);
		}
		return 0;
	}
	/**
	 * @param jsonData
	 * @param rstList
	 * @param params
	 * @func hashmap追加字段
	 */
	public static void JsonToHashMap(JSONObject jsonData,
			Map<String, Object> rstList, String... params) {
		try {
			for (Iterator<String> keyStr = jsonData.keys(); keyStr.hasNext();) {
				String key1 = keyStr.next().trim();
				if (jsonData.get(key1) instanceof JSONObject) {
					HashMap<String, Object> mapObj = new LinkedHashMap<String, Object>();
					JsonToHashMap((JSONObject) jsonData.get(key1), mapObj,
							params);
					rstList.put(key1, mapObj);
					continue;
				}
				if (jsonData.get(key1) instanceof JSONArray) {
					ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
					JsonToHashMap((JSONArray) jsonData.get(key1), arrayList,
							params);
					rstList.put(key1, arrayList);
					continue;
				}
				JsonToHashMap(key1, jsonData.get(key1), rstList);
			}
			// 追加字段
			if (params != null && params.length == 2) {
				rstList.put(params[0], params[1]);
			}
			if (params != null && params.length == 4) {
				rstList.put(params[0], params[1]);
				rstList.put(params[2], params[3]);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void JsonToHashMap(JSONArray jsonarray,
			List<Map<String, Object>> rstList, String... params) {
		try {
			for (int i = 0; i < jsonarray.length(); i++) {
				if (jsonarray.get(i) instanceof JSONObject) {
					HashMap<String, Object> mapObj = new LinkedHashMap<String, Object>();
					JsonToHashMap((JSONObject) jsonarray.get(i), mapObj, params);
					rstList.add(mapObj);
					continue;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public static void JsonToHashMap(String key, Object value,
			Map<String, Object> rstList) {
		key = BBSUtils.replaceBlank(key);
		if (value instanceof String) {
			rstList.put(key, BBSUtils.replaceBlank((String) value));
			return;
		}
		rstList.put(key, value);
	}
	/**
	 * @param str
	 * @return
	 * @func 替换字符串中空格，换行，回车 制表符
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	public static void createDatabase(Context myContext, String fileName) {
		InputStream assetsDB = null;
		OutputStream dbOut = null;
		try {
			assetsDB = myContext.getAssets().open(fileName);
			StringBuffer sb = new StringBuffer("/data/data");
			sb.append("/" + myContext.getPackageName() + "/databases/");
			//LogUtils.i("===========sb.toString()" + sb.toString());
			File file = new File(sb.toString());
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(sb.append(fileName).toString());
			dbOut = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = assetsDB.read(buffer)) > 0) {
				dbOut.write(buffer, 0, length);
			}
			dbOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (dbOut != null)
					dbOut.close();
				if (assetsDB != null)
					assetsDB.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * @return
	 * @func 获取系统版本号
	 */
	public static int getSystemVersion() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}
	/**
	 * @return
	 * @func 获取系统信息
	 */
	public static String getSystemVersion2() {
		return "Product Model: " + android.os.Build.MODEL + ","
				+ android.os.Build.VERSION.SDK + ","
				+ android.os.Build.VERSION.RELEASE;
	}
	/**
	 * @param ctx
	 * @return
	 * @throws Exception
	 * @func 获取软件版本名字
	 */
	public static String getVersionName() {
		Context context = BeemApplication.getContext();
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}
	public static int getResIDByName(Context ctx, String... params) {
		//LogUtils.i("ctx.getPackageName()" + ctx.getPackageName());
		if (params.length == 1) {
			return ctx.getResources().getIdentifier(params[0], "drawable",
					ctx.getPackageName());
		} else {
			return ctx.getResources().getIdentifier(params[0], params[1],
					ctx.getPackageName());
		}
	}
	public static String getRaw(Context context, int RawId) {
		try {
			InputStream is = context.getResources().openRawResource(RawId);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			// StringBuffer线程安全；StringBuilder线程不安全
			StringBuffer sb = new StringBuffer();
			for (String str = null; (str = reader.readLine()) != null;) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getAsset(Context context, String fileName) {
		try {
			InputStream is = context.getResources().getAssets().open(fileName);
			// StringBuffer线程安全；StringBuilder线程不安全
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			for (String str = null; (str = reader.readLine()) != null;) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @func
	 * @param time 单位是ms
	 * @return
	 */
	public static String toHours(String time, Boolean... isMs) {
		return getTimeDurationString(time);
	}

	public static final long MIN_IN_MILLIS = 60 * 1000;
	public static final long HOUR_IN_MILLIS = MIN_IN_MILLIS * 60;
	public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;
	public static final long YEAR_IN_MILLIS = DAY_IN_MILLIS * 365;

	// 将登录时间转换成间隔时间,如 dateTime为'2015-03-05 11:16:00',系统时间为'2015-03-05
	// 10:00:00',则返回'1小时前'
	public static String getTimeDurationString(String dateTime) {
		if (TextUtils.isEmpty(dateTime))
			return "";
		String timeString = null;
		Date dt = null;
		Date systemDt = null;
		try {
			dt = getDateByString(dateTime);
			systemDt = getDateByString(LoginManager.getInstance()
					.getSystemTime());
		} catch (ParseException e) {
			Log.d(TAG, "getTimeDurationString, Exception happen!");
			e.printStackTrace();
		}
		long passMills = -1;
		if (dt != null && systemDt != null) {
			passMills = systemDt.getTime() - dt.getTime();
		}
		timeString = getIntervalTimeFromMillis(passMills);
		return timeString;
	}
	/**
	 * @Title: getIntervalDayAgo
	 * @Description: 获取相差多少天
	 * @param: @param dateTime
	 * @param: @return
	 * @return: long
	 * @throws:
	 */
	public static long getIntervalDayAgo(String dateTime) {
		if (TextUtils.isEmpty(dateTime))
			return 0;
		Date dt = null;
		Date systemDt = null;
		try {
			dt = getDateByString(dateTime);
			systemDt = getDateByString(LoginManager.getInstance()
					.getSystemTime());
		} catch (ParseException e) {
			Log.d(TAG, "getTimeDurationString, Exception happen!");
			e.printStackTrace();
		}
		long passMills = 0;
		if (dt != null && systemDt != null) {
			passMills = systemDt.getTime() - dt.getTime();
		}
		return passMills / DAY_IN_MILLIS;
	}
	// 从字符串中获取日期
	private static Date getDateByString(String date) throws ParseException {
		if (date.length() == 10) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} else {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
		}
	}
	public static String getIntervalTimeFromMillis(long millis) {
		String retStr = null;
		if (millis > YEAR_IN_MILLIS) {
			long days = millis / YEAR_IN_MILLIS;
			retStr = days + "年前";
		} else if (millis > DAY_IN_MILLIS) {
			long days = millis / DAY_IN_MILLIS;
			retStr = days + "天前";
		} else if (millis > HOUR_IN_MILLIS) {
			long hours = millis / HOUR_IN_MILLIS;
			retStr = hours + "小时前";
		} else if (millis > MIN_IN_MILLIS) {
			long mins = millis / MIN_IN_MILLIS;
			retStr = mins + "分钟前";
		} else if (millis >= 0) {
			retStr = "刚刚";
		} else {
			//LogUtils.i("millis-->" + millis);
			retStr = "刚刚";
		}
		return retStr;
	}
	/**
	 * @func scrollview嵌套listview后，重新计算listview宽高
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	public static void setAllComponentsName(Object f) {
		// 获取f对象对应类中的所有属性域
		Field[] fields = f.getClass().getDeclaredFields();
		for (int i = 0, len = fields.length; i < len; i++) {
			// 对于每个属性，获取属性名
			String varName = fields[i].getName();
			try {
				// 获取原来的访问控制权限
				boolean accessFlag = fields[i].isAccessible();
				// 修改访问控制权限
				fields[i].setAccessible(true);
				// 获取在对象f中属性fields[i]对应的对象中的变量
				Object o = fields[i].get(f);
				System.out.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
				// 恢复访问控制权限
				fields[i].setAccessible(accessFlag);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			}
		}
	}
	public static Object getFieldValue(String fieldName, Object obj) {
		Object o = null;
		try {
			Field retField = obj.getClass().getDeclaredField(fieldName);
			boolean accessFlag = retField.isAccessible();
			// 修改访问控制权限
			retField.setAccessible(true);
			// 获取在对象f中属性fields[i]对应的对象中的变量
			o = retField.get(obj);
			System.out.println("fieldName：" + fieldName + " = " + o);
			// 恢复访问控制权限
			retField.setAccessible(accessFlag);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return o;
	}
	public static String AssembleUrl(String url, String[] names, Object[] values) {
		if (names == null || values == null) {
			return url;
		}
		StringBuffer sBuffer = new StringBuffer(url);
		for (int i = 0; i < names.length; i++) {
			if (i != 0) {
				sBuffer.append("&").append(names[i]).append("=")
						.append(values[i]);
			} else {
				sBuffer.append("?").append(names[i]).append("=")
						.append(values[i]);
			}
		}
		return sBuffer.toString();
	}
	/**
	 * 逗号本地化：将，替换成_.delimiter._
	 * @param src
	 * @return
	 */
	public static String CommaLocalize(String src) {
		if (src == null)
			return "";
		return src.replaceAll(",", Constants.BBS_COMMA);
	}
	/**
	 * 将_.delimiter._反转成，
	 * @param src
	 * @return
	 */
	public static String CommaAntiLocalize(String src) {
		if (src == null)
			return "";
		return src.replaceAll(Constants.BBS_COMMA, ",");
	}
	public static Dialog getDialog(final Context context,
			final View contentView, Dialog d) {
		// 防止dialog重复弹出
		if (d != null && d.isShowing()) {
			return d;
		}
		Dialog dialog = new Dialog(context, R.style.myDialog);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
		int[] wh = getDeviceWH(context);
		windowParams.x = 0;
		windowParams.y = wh[1];
		//LogUtils.i("" + Arrays.toString(wh));
		// 控制dialog停放位置
		window.setAttributes(windowParams);
		window.setBackgroundDrawable(new BitmapDrawable());
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				//LogUtils.i("~~onDismiss dialog~~");
				hideSystemKeyBoard(context, contentView);
			}
		});
		dialog.setContentView(contentView);
		// dialog.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		// 最终决定dialog的大小,实际由contentView确定了
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		return dialog;
	}
	// 终端设备的WH
	public static int[] getDeviceWH(Context context) {
		int[] wh = new int[2];
		int w = 0;
		int h = 0;
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		w = dm.widthPixels;
		h = dm.heightPixels;
		wh[0] = w;
		wh[1] = h;
		return wh;
	}
	/**
	 * @param context
	 * @param bitmap
	 * @param picPath 图片名字：如a.jpg
	 * @return
	 */
	public static boolean SaveBitmapToSDCard(Context context,
			final Bitmap bitmap, String assemblePath) {
		boolean savedSuccessfully = false;
		File assembleFile = new File(assemblePath);
		if (assembleFile.getParentFile() != null
				&& !assembleFile.getParentFile().exists()) {
			assembleFile.getParentFile().mkdirs();
		}
		OutputStream os = null;
		try {
			assembleFile.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(assembleFile),
					1024);
			savedSuccessfully = bitmap.compress(CompressFormat.JPEG, 100, os);
			return savedSuccessfully;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//LogUtils.i("FileNotFoundException e:" + e.getMessage());
		} catch (IOException e) {
			//LogUtils.i("IOException e:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (os != null) {
				IoUtils.closeSilently(os);
			}
		}
		return savedSuccessfully;
	}
	/**
	 * 获取保存图片的路径
	 * @param context
	 * @param picName
	 * @return
	 */
	public static String getSavedBitmapAbsulutePath(Context context,
			String picName) {
		String extStorageDirectory = Environment.getExternalStorageDirectory()
				.toString();
		String picStoragePath = "/PIQSTime/download/";
		String assemblePath = extStorageDirectory + picStoragePath + picName;
		return assemblePath;
	}
	public static String AssembleJsonString(String[] names, Object[] values) {
		if (names == null || values == null) {
			return "{}";
		}
		StringBuffer sb = new StringBuffer().append("{");
		for (int i = 0; i < names.length; i++) {
			if (values[i] instanceof String) {
				sb.append("\"").append(names[i]).append("\"").append(":")
						.append("\"").append(values[i]).append("\"")
						.append(",");
			} else {
				sb.append("\"").append(names[i]).append("\"").append(":")
						.append(values[i]).append(",");
			}
		}
		sb = sb.replace(sb.lastIndexOf(","), sb.length(), "");
		sb.append("}");
		return sb.toString();
	}
	public static String AssemblePhpString(String pre, String after) {
		return pre + "/photo/" + after + ".php?";
	}
	public static boolean isMatchEmail(String str) {
		// "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$"
		return str
				.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	}
	// md5加密:plainText-原始字符串 ;is16bit-是否生成16位字符串（十进制）
	public static String Md5(String plainText, boolean... is16bit) {
		String retVal = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			if (is16bit.length > 0 && is16bit[0]) {
				// 16位的加密
				retVal = buf.toString().substring(8, 24);
			} else {
				// 32位的加密
				retVal = buf.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// //LogUtils.i("plainText:" + plainText + " retVal: " + retVal);
		return retVal;
	}
	/**
	 * 获取屏幕宽高
	 * @param context
	 * @return
	 */
	public static int[] getScreenWH(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return new int[] { wm.getDefaultDisplay().getWidth(),
				wm.getDefaultDisplay().getHeight() };
	}
	/**
	 * @param month 月
	 * @param day 日
	 * @return 星座
	 */
	public static String getConstellation(String bDay) {
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(bDay);
			int month = date.getMonth() + 1;
			int day = date.getDate();
			int[] constellationEdgeDay = { 20, 19, 21, 20, 21, 22, 23, 23, 23,
					24, 23, 22 };
			String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座",
					"巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
			if (month == 0 || day == 0 || month > 12)
				return "外星人";
			month = day < constellationEdgeDay[month - 1] ? month - 1 : month;
			return month > 0 ? constellationArr[month - 1]
					: constellationArr[constellationEdgeDay.length - 1];
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @param birthDay 出生日期
	 * @return 年龄
	 */
	public static String getAgeByBithday(String bDay) {
		if (TextUtils.isEmpty(bDay) || bDay.equals("0000-00-00 00:00:00")
				|| bDay.equals("0000-00-00")) {
			return "";
		}
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sf_bDay = sf;
			if (bDay.length() == 10) {
				sf_bDay = new SimpleDateFormat("yyyy-MM-dd");
			}
			cal.setTime(sf.parse(LoginManager.getInstance().getSystemTime()));
			int yearNow = cal.get(Calendar.YEAR);
			int monthNow = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
			Date birthDay = sf_bDay.parse(bDay);
			cal.setTime(birthDay);
			int yearBirth = cal.get(Calendar.YEAR);
			int monthBirth = cal.get(Calendar.MONTH) + 1;
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
			int age = yearNow - yearBirth;
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						age--;
					}
				} else {
					age--;
				}
			}
			return String.valueOf(age);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "-1";
	}
	/**
	 * @func 获取手机联系人
	 * @note:“+86138xxxxxxxxx”和非11位的手机号不会收录,手机号码不允许重复
	 * @param where
	 * @param context
	 * @param phoneContacts
	 */
	public static void getPhoneContact(PhoneNumWhere where, Context context,
			Map<String, PhoneContact> phoneContacts) {
		if (where == PhoneNumWhere.all) {
			getPhoneContact(PhoneNumWhere.phone, context, phoneContacts);
			getPhoneContact(PhoneNumWhere.sim, context, phoneContacts);
		} else if (where == PhoneNumWhere.phone || where == PhoneNumWhere.sim) {
			Uri phoneUri = where == PhoneNumWhere.phone ? Phone.CONTENT_URI
					: Uri.parse("content://icc/adn");// content://sim/adn
			Cursor phoneCursor = null;
			try {
				phoneCursor = context.getContentResolver().query(phoneUri,
						new String[] { Phone.DISPLAY_NAME, Phone.NUMBER },
						null, null, null);
				if (phoneCursor != null) {
					while (phoneCursor.moveToNext()) {
						// 得到手机号码
						String name = phoneCursor.getString(0);
						String phoneNum = phoneCursor.getString(1);
						if (isPhoneNumberValid(phoneNum)) {
							PhoneContact phoneContact = new PhoneContact();
							phoneContact.setField(DBKey.name, name);
							phoneContact.setField(DBKey.phoneNum, phoneNum);
							phoneContact.setField(DBKey.phoneNumWhere, where);
							phoneContact.setField(DBKey.phoneNumState,
									PhoneNumState.add);
							phoneContacts.put(phoneNum, phoneContact);
						}
					}
				} else {
					//LogUtils.e("getPhoneContact_get " + where + " is null");
				}
			} catch (Exception e) {
				if (BuildConfig.DEBUG)
					e.printStackTrace();
				//捕获禁止读取联系人的异常
			} finally {
				if (phoneCursor != null)
					phoneCursor.close();
			}
		}
	}
	public static boolean isPhoneNumberValid(String phone) {
		try {
			if ((phone.length() == 11)
					&& (Long.parseLong(phone) >= 13000000000L)
					&& phone.subSequence(0, 1).equals("1")
					&& (phone.subSequence(1, 2).equals("3")
							|| phone.subSequence(1, 2).equals("4")
							|| phone.subSequence(1, 2).equals("5") || phone
							.subSequence(1, 2).equals("8"))) {
				return true;
			}
		} catch (Exception e) {
			// 防止如“+8613888888888”的非法电话号码
		}
		return false;
	}
	// 是否安装sim卡
	public static int isSimExist(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = mTelephonyManager.getSimState();
		String mString = null;
		switch (simState) {
			case TelephonyManager.SIM_STATE_ABSENT:
				mString = "无卡";
				break;
			case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
				mString = "需要NetworkPIN解锁";
				break;
			case TelephonyManager.SIM_STATE_PIN_REQUIRED:
				mString = "需要PIN解锁";
				break;
			case TelephonyManager.SIM_STATE_PUK_REQUIRED:
				mString = "需要PUN解锁";
				break;
			case TelephonyManager.SIM_STATE_READY:
				mString = "良好";
				break;
			case TelephonyManager.SIM_STATE_UNKNOWN:
				mString = "未知状态";
				break;
		}
		//LogUtils.i("current phone state:" + mString);
		return simState;
	}
	// 调用发送短信界面
	public static void sendSMS(Context context, String phoneNum, String smsBody) {
		Uri smsToUri = Uri.parse("smsto:" + phoneNum);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		context.startActivity(intent);
	}
	// 将字符串补全到指定长度
	public static String padStrBefore(final String pString,
			final char pPadChar, final int pLength) {
		final int padCount = pLength - pString.length();
		if (padCount <= 0) {
			return pString;
		} else {
			final StringBuilder sb = new StringBuilder();
			for (int i = padCount - 1; i >= 0; i--) {
				sb.append(pPadChar);
			}
			sb.append(pString);
			return sb.toString();
		}
	}
	public static String padStrAfter(final String pString, final char pPadChar,
			final int pLength) {
		final int padCount = pLength - pString.length();
		if (padCount <= 0) {
			return pString;
		} else {
			final StringBuilder sb = new StringBuilder();
			sb.append(pString);
			for (int i = padCount - 1; i >= 0; i--) {
				sb.append(pPadChar);
			}
			return sb.toString();
		}
	}
	// 将年月日转换成Date
	public static int[] parseDate(String str) {
		SimpleDateFormat sf = str.length() == 10 ? new SimpleDateFormat(
				"yyyy-MM-dd") : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sf.parse(str);
			return new int[] { date.getYear() + 1900, date.getMonth() + 1,
					date.getDate(), date.getHours(), date.getMinutes(),
					date.getSeconds() };
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static boolean timeCompare(String time1, String time2) {
		return time1.compareTo(time2) > 0;
	}

	public enum DbidType {
		// 查询城市还是学校
		CITY, COLLEAGE
	}

	/**
	 * 通过id查询数据库
	 * @param id 某段数据id
	 * @param context
	 * @param dt 枚举
	 */
	public static String dbquery(String id, Context context, DbidType dt) {
		SQLiteDatabase db = null;
		String result = null;
		try {
			db = SQLdm.openDatabase(context);
			switch (dt) {
				case COLLEAGE: {
					Cursor cursor = db.rawQuery(
							"select uname from colleage where ucode=" + id,
							null);
					if (cursor.moveToFirst()) {
						result = cursor.getString(cursor
								.getColumnIndex("uname"));
					}
					cursor.close();
					break;
				}
				case CITY: {
					String pcode = "", ccode = "";
					if (id.length() == 6) {
						pcode = id.substring(0, 2);
						ccode = id.substring(2, 6);
					} else if (id.length() == 4) {
						pcode = id.substring(0, 2);
						ccode = id;
					}
					Cursor cursor = db.query("provice",
							new String[] { "pname" }, "pcode=?",
							new String[] { pcode }, null, null, null);
					String[] codes = new String[2];
					if (cursor.moveToPosition(0)) {
						String pname = cursor.getString(0);
						codes[0] = pname;
						Cursor cursor2 = db.query("city",
								new String[] { "cname" }, "ccode=?",
								new String[] { ccode }, null, null, null);
						if (cursor2.moveToPosition(0)) {
							String cname = cursor2.getString(0);
							codes[1] = cname;
						}
						if (codes[0].equals(codes[1])) {
							result = codes[0];
						} else {
							result = codes[0] + "-" + codes[1];
						}
						cursor2.close();
					}
					cursor.close();
					break;
				}
			}
		} finally {
			if (db != null)
				db.close();
		}
		return result;
	}
	// 检查姓名是否含有特殊字符
	public static void checkNameValid(Context ctx, String name) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(name);
		if (m.find()) {
			Toast.makeText(ctx, "姓名不允许输入特殊符号！", Toast.LENGTH_LONG).show();
		}
	}
	// 获取Audio保存路径[网络路径md5映射成本地路径]
	public static String getAudioSavePath(String url) {
		// 如果是网络路径则将起MD5后作为本地路劲
		url = BBSUtils.Md5(url);
		String path = null;
		File appCacheDir = getAppCacheDir(BeemApplication.getContext(), "audio");
		if (appCacheDir != null) {
			path = new File(appCacheDir, url).getPath();
		}
		//LogUtils.v("audo save path:" + path);
		return path;
	}
	// 获取应用缓存目录
	public static File getAppCacheDir(Context context, String parentDir) {
		File file = new File(StorageUtils.getCacheDirectory(context), parentDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}
	//获取PIQSTime目录子目录，没有创建
	public synchronized static File getPIQSTimeChildPath(Context context,
			String parentDir) {
		File file = null;
		if (isSDCardAvaliable(context)) {
			file = new File(Environment.getExternalStorageDirectory(),
					"PIQSTime" + File.separatorChar + parentDir);
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			Toast.makeText(context, "请安装SD卡！", Toast.LENGTH_SHORT).show();
		}
		return file;
	}
	/**
	 * Returns application cache directory. Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache")</i> (if card is mounted and app has appropriate
	 * permission) or on device's file system depending incoming parameters.
	 * @param context Application context
	 * @param preferExternal Whether prefer external location for cache
	 * @return Cache {@link File directory}.<br />
	 *         <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
	 *         {@link android.content.Context#getCacheDir() Context.getCacheDir()} returns null).
	 */
	public static File getTestCartoonDir(Context context, String path,
			boolean preferExternal) {
		File appCacheDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		}
		boolean hasExternalPermission = context
				.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
		if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState)
				&& hasExternalPermission) {
			appCacheDir = new File(context.getExternalCacheDir(), "/vv/" + path);
			if (!appCacheDir.exists()) {
				if (!appCacheDir.mkdirs()) {
					L.w("Unable to create external cache directory");
					return null;
				}
				try {
					new File(appCacheDir, ".nomedia").createNewFile();
				} catch (IOException e) {
					L.i("Can't create \".nomedia\" file in application external cache directory");
				}
			}
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			throw new IllegalArgumentException("can not create the cartoon dir");
		}
		return appCacheDir;
	}
	public static String getTakePhotoPath(Context context, String filename) {
		String path = null;
		File appCacheDir = getAppCacheDir(context, "capture");
		if (appCacheDir != null) {
			File file = new File(appCacheDir, filename);
			path = file.getPath();
		}
		return path;
	}
	public static String getCartoonPath(Context context, String filename) {
		String path = null;
		//File appCacheDir = getAppCacheDir(context, "cartoon");
		File appCacheDir = getPIQSTimeChildPath(context, "cartoon");
		if (appCacheDir != null) {
			File file = new File(appCacheDir, filename);
			path = file.getAbsolutePath();
		}
		return path;
	}
	public static File getCartoonFile(Context context, String filename) {
		File file = null;
		//File appCacheDir = getAppCacheDir(context, "cartoon");
		File appCacheDir = getPIQSTimeChildPath(context, "cartoon");
		if (appCacheDir != null) {
			file = new File(appCacheDir, filename);
			//path = file.getAbsolutePath();
		}
		return file;
	}
	public static String getProvicePath(Context context, String filename) {
		String path = null;
		File appCacheDir = getAppCacheDir(context, "provice");
		if (appCacheDir != null) {
			File file = new File(appCacheDir, filename);
			path = file.getAbsolutePath();
		}
		return path;
	}
	public static void takePhoto(Activity activity) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		activity.startActivityForResult(intent, Constants.TAKEPHOTO);
	}
	public static Uri takePhoto(Fragment fragment, int requestCode) {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			//Uri photoUri = fragment.get.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			Uri photoUri = FileUtil.getCameraPhotoUri(fragment.getActivity());
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			fragment.startActivityForResult(intent, requestCode);
			return photoUri;
		} else {
			Toast.makeText(BeemApplication.getContext(), "内存卡不存在",
					Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	/**
	 * @param activity
	 * @param requestCode
	 * @param output The Uri will be return
	 * @return return output uri when start camera app success, or null when failed;
	 */
	public static Uri takePhoto(Activity activity, int requestCode, Uri output) {
		try {
			String SDState = Environment.getExternalStorageState();
			// 执行拍照前，应该先判断SD卡是否存在
			if (SDState.equals(Environment.MEDIA_MOUNTED)) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						output);
				activity.startActivityForResult(intent, requestCode);
				return output;
			} else {
				Toast.makeText(BeemApplication.getContext(), "内存卡不存在",
						Toast.LENGTH_SHORT).show();
				return null;
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			Toast.makeText(BeemApplication.getContext(), "相机错误，不能拍照",
					Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	public static Uri takePhoto(Activity activity, int requestCode) {
		Uri photoUri = FileUtil.getCameraPhotoUri(activity);
		return takePhoto(activity, requestCode, photoUri);
	}
	/**
	 * @Title: getTakePhotoPath
	 * @Description: 获取拍照后的图片路径
	 * @param requestCodeOut 拍照后返回的请求码
	 * @param requestCodeIn 拍照前给定的请求吗
	 * @param data 返回的intent
	 * @param activity
	 * @return
	 * @return: String 拍照图片路径
	 */
	public static String getTakePhotoPath(Context c) {
		/*if (photoUri == null) {
			//LogUtils.e(" the takephoto is null~");
			return null;
		}
		String picPath = null;
		String[] pojo = { MediaColumns.DATA };
		Cursor cursor = activity.managedQuery(photoUri, pojo, null, null, null);
		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			// Android在4.0以上会自动关闭游标
			if (Build.VERSION.SDK_INT < 14) {
				cursor.close();
			}
		}
		return picPath;*/
		return FileUtil.getCameraPhotoUri(c).getPath();
	}
	// 系统和软件设置项共同决定是否震动响铃[值0_震动 值1_响铃]
	public static boolean[] getVibrateRingParams(Context mAppContext) {
		boolean[] retVal = new boolean[] { true, true };
		AudioManager volMgr = (AudioManager) mAppContext
				.getSystemService(Context.AUDIO_SERVICE);
		boolean isSound = SharedPrefsUtil.getValue(mAppContext,
				SettingKey.msg_sound, true);
		boolean isVibrate = SharedPrefsUtil.getValue(mAppContext,
				SettingKey.msg_vibrate, true);
		switch (volMgr.getRingerMode()) {// 获取系统设置的铃声模式
			case AudioManager.RINGER_MODE_SILENT: {// 静音模式，值为0，这时候不震动，不响铃
				retVal[0] = false;
				retVal[1] = false;
				break;
			}
			case AudioManager.RINGER_MODE_VIBRATE: {// 震动模式，值为1，这时候震动，不响铃
				retVal[0] = isVibrate;
				retVal[1] = false;
				break;
			}
			case AudioManager.RINGER_MODE_NORMAL: {// 常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
				retVal[1] = isSound;
				if (volMgr.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
					// 不震动
					retVal[0] = false;
				} else if (volMgr
						.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ONLY_SILENT) {
					// 只在静音时震动
					retVal[0] = false;
				} else {
					// 震动
					retVal[0] = isVibrate;
				}
				break;
			}
			default:
				break;
		}
		return retVal;
	}
	public static void setAlarmParams(Context mAppContext,
			Notification notification, boolean isSound, boolean isVibrate) {
		// AudioManager provides access to volume and ringer mode control.
		AudioManager volMgr = (AudioManager) mAppContext
				.getSystemService(Context.AUDIO_SERVICE);
		//LogUtils.i("isSound:" + isSound + " isVibrate:" + isVibrate + " mode:" + volMgr.getRingerMode());
		switch (volMgr.getRingerMode()) {// 获取系统设置的铃声模式
			case AudioManager.RINGER_MODE_SILENT: {// 静音模式，值为0，这时候不震动，不响铃
				notification.sound = null;
				notification.vibrate = null;
				break;
			}
			case AudioManager.RINGER_MODE_VIBRATE: {// 震动模式，值为1，这时候震动，不响铃
				notification.sound = null;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				break;
			}
			case AudioManager.RINGER_MODE_NORMAL: {// 常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
				if (isSound) {
					notification.defaults |= Notification.DEFAULT_SOUND;
				} else {
					notification.sound = null;
				}
				if (isVibrate) {
					if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
						// 不震动
						notification.vibrate = null;
					} else if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ONLY_SILENT) {
						// 只在静音时震动
						notification.vibrate = null;
					} else {
						// 震动
						notification.defaults |= Notification.DEFAULT_VIBRATE;
					}
				} else {
					notification.vibrate = null;
				}
				notification.flags |= Notification.FLAG_SHOW_LIGHTS;// 都给开灯
				break;
			}
			default:
				break;
		}
	}
	// 应用是否前台运行
	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName)
				&& currentPackageName.equals(context.getPackageName())) {
			return true;
		}
		return false;
	}
	//判断Acitivity是否存在于AndroidManifest.xml中
	public static boolean isAcitivityExist(Context ctx, Class<?> cls) {
		Intent intent = new Intent(ctx, cls);
		if (ctx.getPackageManager().resolveActivity(intent, 0) != null) {
			// 说明系统中存在这个activity
			//LogUtils.i(cls.getSimpleName() + " exist.");
			return true;
		}
		//LogUtils.i(cls.getSimpleName() + " not exist.");
		return false;
	}
	//判断Acitivity是否存在于AndroidManifest.xml中
	public static boolean isAcitivityExist(Context ctx, String packageName,
			String className) {
		Intent intent = new Intent();
		intent.setClassName(packageName, className);
		if (ctx.getPackageManager().resolveActivity(intent, 0) != null) {
			// 说明系统中不存在这个activity
			//LogUtils.i(className + " exist.");
			return true;
		}
		//LogUtils.i(className + " not exist.");
		return false;
	}
	// activity是否前台显示
	public static boolean isTopActivy(Context context, String activityName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cName = am.getRunningTasks(1).size() > 0 ? am
				.getRunningTasks(1).get(0).topActivity : null;
		if (null == cName)
			return false;
		//LogUtils.i("top Activity:" + cName.getClassName());
		return cName.getClassName().equals(activityName);
	}
	public static void backHome(Context context) {
		Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
		mHomeIntent.addCategory(Intent.CATEGORY_HOME);
		mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		context.startActivity(mHomeIntent);
	}
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		/*
		 * 必须把我们要的值弄到最低位去，有人说不移位这样做也可以， result[0] = (byte)(i & 0xFF000000);
		 * ，这样虽然把第一个字节取出来了，但是若直接转换为byte类型，会超出byte的界限，出现error。再提下数
		 * 之间转换的原则（不管两种类型的字节大小是否一样，原则是不改变值，内存内容可能会变，比如int转为
		 * float肯定会变）所以此时的int转为byte会越界，只有int的前三个字节都为0的时候转byte才不会越界。虽 然result[0] = (byte)(i &
		 * 0xFF000000); 这样不行，但是我们可以这样 result[0] = (byte)((i & 0xFF000000)>>24);
		 */
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}
	// 大端转小端
	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0xFF) << shift;// 往高位游
		}
		return value;
	}
	public static int byteArrayToInt(byte[] b, int start) {
		int value = 0;
		int end = start + 4;
		for (int i = start; i < end; i++) {
			int shift = (end - 1 - i) * 8;
			value += (b[i] & 0xFF) << shift;// 往高位游
		}
		return value;
	}
	// 字符数组拼接
	public static byte[] byteArrayCat(byte[]... byteArrs) {
		int len = 0;
		for (byte[] byteArrOne : byteArrs) {
			len += byteArrOne.length;
		}
		byte[] retVal = new byte[len];
		int dstPos = 0;
		for (byte[] byteArrOne : byteArrs) {
			System.arraycopy(byteArrOne, 0, retVal, dstPos, byteArrOne.length);
			dstPos += byteArrOne.length;
		}
		return retVal;
	}
	/**
	 * 将指定byte数组以16进制的形式打印到控制台
	 * @param hint String
	 * @param b byte[]
	 * @return void
	 */
	public static void printHexString(String hint, byte[] b) {
		System.out.print(hint);
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase() + " ");
		}
		System.out.println("");
	}
	/**
	 * @param b byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}
	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * @param src0 byte
	 * @param src1 byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	 * @param src String
	 * @return byte[]
	 */
	public static byte[] HexString2Bytes(String src) {
		byte[] ret = new byte[8];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < 8; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}
	// 转化字符串为十六进制编码
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
	// 转化十六进制编码为字符串
	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	/*
	 * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	 */
	public static String encode(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}
	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}
	public static char[] getChars(byte[] bytes, int offset, int byteCount) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes, offset, byteCount);
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}
	// 组装头像地址
	public static String makePhotos(DBKey key, String... str) {
		if (key == DBKey.photo_big || key == DBKey.photo_small) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length; i++) {
				if (i == 0) {
					sb.append(str[i]);
				} else {
					sb.append("|").append(str[i]);
				}
			}
			return sb.toString();
		}
		return null;
	}
	// 组装头像地址
	public static String makePhotos(DBKey key, List<VVImage> images) {
		String[] imagePaths = new String[Math.min(8, images.size())];
		for (int i = 0; i < imagePaths.length; i++) {
			if (key == DBKey.photo_big) {
				imagePaths[i] = images.get(i).getPath();
			} else if (key == DBKey.photo_small) {
				imagePaths[i] = images.get(i).getPathThumb();
			}
		}
		return makePhotos(key, imagePaths);
	}
	/**
	 * @Title: splitPhotos
	 * @Description: 分割头像地址
	 * @param key 头像的key，DBKey.photo_big:大图 DBKey.photo_small:小图
	 * @param fieldStr
	 * @return
	 * @return: String[]
	 */
	public static String[] splitPhotos(DBKey key, String fieldStr) {
		if (TextUtils.isEmpty(fieldStr))
			return null;
		if (key == DBKey.photo_big || key == DBKey.photo_small) {
			String splitCut = "<@&image#";
			if (fieldStr.contains("<@&image#")) {
				splitCut = "<@&image#";
			} else {
				splitCut = "\\|";
			}
			String[] splitOnes = fieldStr.split(splitCut);
			return splitOnes;
		}
		return null;
	}
	// 通用分割字符串
	public static String[] splitString(String spliter, String tag) {
		if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(spliter)) {
			return null;
		} else {
			return tag.split(spliter);
		}
	}
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	public int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
	public static String getXmppErrorMsg(Context context, XMPPException e) {
		//		if (e.getXMPPError() == null || e.getXMPPError().getCondition() == null) {
		return e.getMessage();
		//		}
		/*Log.d(TAG, "getXmppErrorMsg id " + e.getXMPPError().getCondition());
		String str = context.getResources().getString(
				context.getResources().getIdentifier(e.getXMPPError().getCondition().replace("-", "_"), "string",
						"com.beem.project.btf"));*/
		//		   return str;
	}
	/**
	 * @Title: saveFile
	 * @Description: 保存输入流到指定文件
	 * @param: @param savePath
	 * @param: @param in
	 * @param: @throws IOException
	 * @return: void
	 * @throws:
	 */
	public static void saveFile(InputStream in, File f) throws IOException {
		OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
		try {
			byte[] data = new byte[1024];
			int nbread;
			while ((nbread = in.read(data)) != -1)
				os.write(data, 0, nbread);
		} finally {
			in.close();
			os.close();
		}
	}
	/**
	 * @Title: isSDCardAvaliable
	 * @Description: sdcard是否可用
	 * @param: @param context
	 * @param: @return
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isSDCardAvaliable(Context context) {
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		}
		if (Environment.MEDIA_MOUNTED.equals(externalStorageState)
				&& hasExternalStoragePermission(context)) {
			return true;
		}
		return false;
	}
	/**
	 * @Title: hasExternalStoragePermission
	 * @Description: 是否有写sdcard权限
	 * @param: @param context
	 * @param: @return
	 * @return: boolean
	 * @throws:
	 */
	public static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	/**
	 * @Title: copyFile
	 * @Description: 拷贝File
	 * @param: @param oldFile
	 * @param: @param newFile
	 * @return: void
	 * @throws:
	 */
	public static void copyFile(File oldFile, File newFile) {
		if (oldFile.exists()) {
			InputStream inStream = null;
			FileOutputStream fs = null;
			try {
				inStream = new FileInputStream(oldFile); //读入原文件 
				fs = new FileOutputStream(newFile);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (inStream != null) {
					try {
						inStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fs != null) {
					try {
						fs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			//LogUtils.e("the oldFile not exist.");
		}
	}
	/**
	 * 获取状态栏的高度
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}
	
	public static String getShortGidCreatTime(String input) {
		if (input.length() > 10) {
			return input.substring(0, 10);
		}
		return input;
	}
}
