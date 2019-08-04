package com.beem.project.btf.ui.views;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.beem.project.btf.R;

public class TimeflyDueRemindView {
	private View mView;
	private Context mContext;
	private TextView remindContent;
	private Button remindBtn;
	private boolean flag;

	public TimeflyDueRemindView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.timefly_due_remind_layout, null);
		remindContent = (TextView) mView.findViewById(R.id.remindContent);
		remindBtn = (Button) mView.findViewById(R.id.remindBtn);
	}
	public TimeflyDueRemindView(Context context, boolean flag) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.timefly_due_remind_layout, null);
		remindContent = (TextView) mView.findViewById(R.id.remindContent);
		remindBtn = (Button) mView.findViewById(R.id.remindBtn);
		this.flag = flag;
	}
	public View getmView() {
		return mView;
	}
	public void setText(String str) {
		if (TextUtils.isEmpty(str)) {
			remindContent.setText("");
		} else {
			String content = null;
			if (!flag) {
				content = "您在" + str + "天前通过时光隧道为现在的自己发送了一封时光信件!赶紧查看吧~";
			} else {
				content = "您在" + str + "前通过时光隧道为现在的自己发送了一封时光信件!赶紧查看吧~";
			}
			int end = str.length() + 2;
			Spannable WordtoSpan = new SpannableString(content);
			WordtoSpan.setSpan(new RelativeSizeSpan(2.5f), 2, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			remindContent.setText(WordtoSpan);
		}
	}
	public void setBtnClickListener(OnClickListener clickListener) {
		if (clickListener != null) {
			remindBtn.setOnClickListener(clickListener);
		}
	}
}
