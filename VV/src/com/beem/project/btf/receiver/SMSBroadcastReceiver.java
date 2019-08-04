package com.beem.project.btf.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private static MessageListener mMessageListener;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	private static final String tag = SMSBroadcastReceiver.class
			.getSimpleName();

	public SMSBroadcastReceiver() {
		super();
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				String sender = smsMessage.getDisplayOriginatingAddress();
				// 短信内容
				String content = smsMessage.getDisplayMessageBody();
				long date = smsMessage.getTimestampMillis();
				Date tiemDate = new Date(date);
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String time = simpleDateFormat.format(tiemDate);
				// 过滤不需要读取的短信的发送号码
				Log.i(tag, "sender" + sender);
				if ("10657563288924".equals(sender)) {
					mMessageListener.onReceived(content);
					abortBroadcast();
				}
				// mMessageListener.onReceived(content);
			}
		}
	}

	// 回调接口
	public interface MessageListener {
		public void onReceived(String message);
	}

	public void setOnReceivedMessageListener(MessageListener messageListener) {
		SMSBroadcastReceiver.mMessageListener = messageListener;
	}
}
