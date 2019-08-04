package com.beem.project.btf.providers;

import com.beem.project.btf.constant.Constants;
import com.butterfly.vv.vv.utils.CToast;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SMSContentObserver extends ContentObserver {
	private static final String tag = SMSContentObserver.class.getSimpleName();
	private Context mContext;
	private Handler mHandler;
	private Cursor cursor = null;
	private ContentObserverType type;

	public SMSContentObserver(Context context, Handler handler) {
		super(handler);
		mHandler = handler;
		mContext = context;
	}
	public SMSContentObserver(Context context, Handler handler,
			ContentObserverType type) {
		super(handler);
		mHandler = handler;
		mContext = context;
		this.type = type;
	}

	public enum ContentObserverType {
		RegisterType, ForgetPasswordType;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		if (ContentObserverType.ForgetPasswordType.equals(type)) {
			try {
				// 读取收件箱中指定号码的短信
				cursor = mContext.getContentResolver().query(
						Uri.parse("content://sms/inbox"),
						new String[] { "_id", "address", "read", "body" },
						" body like ? and read=?",
						new String[] { "%找回密码的验证码%", "0" }, "_id desc");
			} catch (IllegalArgumentException e) {
				CToast.showToast(mContext, "已禁止读取短信权限,请手动输入",
						Toast.LENGTH_SHORT);
			}
		} else {
			try {
				// 读取收件箱中指定号码的短信
				cursor = mContext.getContentResolver().query(
						Uri.parse("content://sms/inbox"),
						new String[] { "_id", "address", "read", "body" },
						" body like ? and read=?",
						new String[] { "%是您的注册验证码%", "0" }, "_id desc");
			} catch (IllegalArgumentException e) {
				CToast.showToast(mContext, "已禁止读取短信权限,请手动输入",
						Toast.LENGTH_SHORT);
			}
		}
		Log.i(tag, "cursor==null" + (cursor == null));
		if (cursor != null && cursor.getCount() > 0) {
			/*
			 * ContentValues values = new ContentValues(); values.put("read", "1"); // 修改短信为已读模式
			 */
			Message msg = Message.obtain();
			if (cursor.moveToNext()) {
				int smsbodyColumn = cursor.getColumnIndex("body");
				String smsBody = cursor.getString(smsbodyColumn);
				int addressColumn = cursor.getColumnIndex("address");
				String address = cursor.getString(addressColumn);
				msg.what = Constants.SMSCONTENT_OBSERVER;
				Bundle bundle = new Bundle();
				bundle.putString("messagecode", smsBody);
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			}
		}
		// 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
		if (Build.VERSION.SDK_INT < 14) {
			cursor.close();
		}
	}
}
