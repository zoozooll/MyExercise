package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CutomerlimitEditText extends RelativeLayout implements TextWatcher {
	private EditText mEditText;
	private TextView mTextView;
	private int maxLength = 0;
	private RelativeLayout mRelativeLayout;
	// 监听输入框的变化
	private OnChanageListener onChanageListener;

	public CutomerlimitEditText(Context context) {
		super(context);
	}
	public CutomerlimitEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	public CutomerlimitEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	public void init(Context context, AttributeSet attrs) {
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.HintEditText);
		maxLength = mTypedArray.getInt(R.styleable.HintEditText_maxLength, 0);
		mRelativeLayout = (RelativeLayout) LayoutInflater.from(context)
				.inflate(R.layout.limitedittext_layout, this, true);
		mEditText = (EditText) mRelativeLayout.findViewById(R.id.ed);
		mTextView = (TextView) mRelativeLayout.findViewById(R.id.tv);
		mTextView.setHint(String.valueOf(maxLength));
		mTextView.setVisibility(View.GONE);
		// 限定最多可输入多少字符
		mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				maxLength) });
		mEditText.addTextChangedListener(this);
		mTypedArray.recycle();
	}
	public void setText(CharSequence text) {
		if (text == null)
			text = "";
		mEditText.setText(text);
		mEditText.setSelection(mEditText.getText().length());
	}
	public CharSequence getText() {
		return mEditText.getText();
	}
	public void setHint(CharSequence text) {
		mEditText.setHint(text);
	}
	public void setSelection(int length) {
		mEditText.setSelection(length);
	}
	public void setError(CharSequence text) {
		mEditText.setError(text);
	}
	/**
	 * 设置回调接口
	 * @param onChanageListener
	 */
	public void setOnChangeListener(OnChanageListener onChanageListener) {
		this.onChanageListener = onChanageListener;
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		mTextView.setHint((maxLength - s.toString().length()) + "");
		mTextView.setVisibility(View.VISIBLE);
		if (onChanageListener != null) {
			onChanageListener.onChange(s.toString());
		}
	}

	/**
	 * 监听编辑框文本变化
	 */
	public interface OnChanageListener {
		/**
		 * 当EditText输入字符变化时
		 * @param editTextStr 最后的text
		 */
		public void onChange(String editTextStr);
	}
}
