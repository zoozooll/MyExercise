package com.iskyinfor.duoduo.ui.lesson;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UIPublicConstant;

/**
 * 自定义的Spinner
 * 
 * @author yyj
 * 
 */
public class ShareSpinnerWidget extends LinearLayout {
	private Context mContext = null;
	LayoutInflater factory = null;
	private EditText mEditText = null;
	@SuppressWarnings("unused")
	private TextView iBtn = null;

	public ShareSpinnerWidget(Context context) {
		super(context);
		mContext = context;
		factory = LayoutInflater.from(mContext);
		getView();
	}

	public ShareSpinnerWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		factory = LayoutInflater.from(mContext);
		getView();
	}

	private void getView() {
		View view = factory.inflate(R.layout.lesson_spinner_listview, null);
		mEditText = (EditText) view.findViewById(R.id.spinner_edittext);
		iBtn = (TextView) view.findViewById(R.id.spinner_textview);
		this.addView(view);
	}

	// 为EditText动态的赋值
	public void setSpinnerData(String text, int code) {
		switch (code) {
		case UIPublicConstant.INT_YEAR_CODE:
			mEditText.setText(text);
			break;
		case UIPublicConstant.INT_MONTH_CODE:
			mEditText.setText(text);
			break;
		case UIPublicConstant.INT_DAY_CODE:
			mEditText.setText(text);
			break;
		case UIPublicConstant.INT_PROVINCE_CODE:
			mEditText.setText(text);
			break;
		case UIPublicConstant.INT_CITY_CODE:
			mEditText.setText(text);
			break;
		case UIPublicConstant.INT_COUNTRY_CODE:
			mEditText.setText(text);
			break;
		default:
			break;
		}
	}
	
}