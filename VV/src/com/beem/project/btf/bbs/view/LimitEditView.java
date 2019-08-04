package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 此类做为SimpleEditDilaogView setcontentview方法的参数 在对话框中生成一个编辑框
 */
public class LimitEditView {
	private Context mContext;
	private View mView;
	CutomerlimitEditText limitedit;

	public LimitEditView(Context context) {
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.limiteditview_layout, null);
		limitedit = (CutomerlimitEditText) mView.findViewById(R.id.limitedit);
	}
	public void setText(String data) {
		limitedit.setText(data);
		limitedit.setSelection(limitedit.getText().length());
	}
	public View getView() {
		return mView;
	}
	public void setHint(String text) {
		limitedit.setHint(text);
	}
}
