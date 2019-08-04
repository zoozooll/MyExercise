package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CheckBoxTwoView {
	private Context mContext;
	private View mView;
	private CheckBox rBtnVoice, rBtnVibrate;
	private boolean[] oncheced = new boolean[2];

	public CheckBoxTwoView(Context context) {
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.notification_setting_layout, null);
		rBtnVoice = (CheckBox) mView.findViewById(R.id.rBtnVoice);
		rBtnVoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton paramCompoundButton,
					boolean paramBoolean) {
				// TODO Auto-generated method stub
				oncheced[0] = paramBoolean;
			}
		});
		rBtnVibrate = (CheckBox) mView.findViewById(R.id.rBtnVibrate);
		rBtnVibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton paramCompoundButton,
					boolean paramBoolean) {
				oncheced[1] = paramBoolean;
			}
		});
	}
	public View getView() {
		return mView;
	}
	public void setdata(boolean[] oncheced) {
		rBtnVoice.setChecked(oncheced[0]);
		rBtnVibrate.setChecked(oncheced[1]);
	}
	public boolean[] getdata() {
		return oncheced;
	}
}
