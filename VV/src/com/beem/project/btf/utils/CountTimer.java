package com.beem.project.btf.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.beem.project.btf.R;

public class CountTimer extends CountDownTimer {
	public static final int TIME_COUNT = 121000;// 时间防止从119s开始显示（以倒计时120s为例子）
	private TextView btn;
	private int endStrRid;
	private Context context;

	/**
	 * 参数 millisInFuture 倒计时总时间（如60S，120s等） 参数 countDownInterval 渐变时间（每次倒计1s） 参数 btn
	 * 点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView） 参数 endStrRid 倒计时结束后，按钮对应显示的文字
	 */
	public CountTimer(long millisInFuture, long countDownInterval,
			TextView btn, int endStrRid) {
		super(millisInFuture, countDownInterval);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}
	public CountTimer(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}
	/**
	 * 参数上面有注释
	 */
	public CountTimer(TextView btn, int endStrRid) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = endStrRid;
	}
	public CountTimer(TextView btn, int endStrRid, Context context) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = endStrRid;
		this.context = context;
	}
	public CountTimer(TextView btn) {
		super(TIME_COUNT, 1000);
		this.btn = btn;
		this.endStrRid = R.string.register_get_testnum;
	}
	// 计时完毕时触发
	@Override
	public void onFinish() {
		btn.setText(endStrRid);
		btn.setEnabled(true);
	}
	// 计时过程显示
	@Override
	public void onTick(long millisUntilFinished) {
		btn.setEnabled(false);
		btn.setText("重新获取/" + (millisUntilFinished - 100) / 1000 + "s");
	}
	public static String getDynamicPassword(String str) {
		// 6是验证码的位数一般为六位
		Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
				+ 6 + "})(?![0-9])");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while (m.find()) {
			System.out.print(m.group());
			dynamicPassword = m.group();
		}
		return dynamicPassword;
	}
}
