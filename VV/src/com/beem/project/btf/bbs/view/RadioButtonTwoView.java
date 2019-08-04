package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 此类为对话框填充两个单选按钮，实现缓存时间单选或男女单选
 */
public class RadioButtonTwoView {
	private Context mContext;
	private View mView;
	private RadioGroup selecRange;
	private RadioButton rBtnUp, rBtnDown;
	private boolean status = false;
	private CheckedListener checkedListener;

	public RadioButtonTwoView(Context context) {
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.cachetimeview_layout, null);
		selecRange = (RadioGroup) mView.findViewById(R.id.selecRange);
		selecRange.setOnCheckedChangeListener(selectRangeLis);
		rBtnUp = (RadioButton) mView.findViewById(R.id.rBtnUp);
		rBtnDown = (RadioButton) mView.findViewById(R.id.rBtnDown);
	}
	public View getView() {
		return mView;
	}

	// 是否正在清理checkGroup
	private OnCheckedChangeListener selectRangeLis = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
				case R.id.rBtnUp: {
					status = true;
					if (checkedListener != null)
						checkedListener.check(status);
					break;
				}
				case R.id.rBtnDown: {
					status = false;
					if (checkedListener != null)
						checkedListener.check(status);
					break;
				}
			}
		}
	};

	// 设置默认的选项
	public void setData(boolean status) {
		this.status = status;
		int id = 0;
		if (status) {
			id = R.id.rBtnUp;
		} else {
			id = R.id.rBtnDown;
		}
		((RadioButton) mView.findViewById(id)).setChecked(true);
	}
	// 设置单选按钮文本
	public void setText(String text1, String text2) {
		rBtnUp.setText(text1);
		rBtnDown.setText(text2);
	}
	public void initiaData(boolean status) {
		this.status = status;
		rBtnUp.setChecked(status);
		rBtnDown.setChecked(!status);
	}

	// 定义一个回调接口，用于处理选择后的值保存等操作
	public interface CheckedListener {
		public void check(boolean status);
	}

	public CheckedListener getCheckedListener() {
		return checkedListener;
	}
	public void setCheckedListener(CheckedListener checkedListener) {
		this.checkedListener = checkedListener;
	}
}
