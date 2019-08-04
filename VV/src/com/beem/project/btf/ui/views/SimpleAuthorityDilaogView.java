package com.beem.project.btf.ui.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.beem.project.btf.R;

public class SimpleAuthorityDilaogView {
	private Context mContext;
	private LinearLayout editwraper;
	private View mView;
	private Button ensurebtn, dismissbtn;

	public SimpleAuthorityDilaogView(Context mContext) {
		this.mContext = mContext;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.simple_authority_dialogview_layout,
				null);
		editwraper = (LinearLayout) mView.findViewById(R.id.editwraper);
		ensurebtn = (Button) mView.findViewById(R.id.ensurebtn);
		dismissbtn = (Button) mView.findViewById(R.id.dismissbtn);
	}
	public View getmView() {
		return mView;
	}
	public void setContentView(View child) {
		editwraper.addView(child);
	}
	public void setPositiveButton(final ButtonClickListener buttonListener) {
		if (buttonListener != null) {
			ensurebtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					buttonListener.ensure(mView);
				}
			});
		}
	}
	public void setNegativeButton(final ButtonClickListener buttonListener) {
		if (buttonListener != null) {
			dismissbtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					buttonListener.ensure(mView);
				}
			});
		}
	}

	public interface ButtonClickListener {
		public void ensure(View contentView);
	}
}
