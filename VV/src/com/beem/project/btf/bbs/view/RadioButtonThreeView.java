package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 此类为对话框填充三个单选按钮，实现上传图片权限控制
 */
public class RadioButtonThreeView {
	private Context mContext;
	private View mView;
	private RadioGroup selecRange;
	private String status = AlbumAuthority.all;
	private CheckedButtonThreeListener checkedListener;

	public RadioButtonThreeView(Context context) {
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.radiobutton_three_view_layout, null);
		selecRange = (RadioGroup) mView.findViewById(R.id.selecRange);
		selecRange.setOnCheckedChangeListener(selectRangeLis);
	}
	public View getView() {
		return mView;
	}

	// 是否正在清理checkGroup
	private OnCheckedChangeListener selectRangeLis = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
				case R.id.All: {
					status = AlbumAuthority.all;
					break;
				}
				case R.id.friends: {
					status = AlbumAuthority.friend;
					break;
				}
				case R.id.privateMe: {
					status = AlbumAuthority.privateMe;
					break;
				}
			}
			if (checkedListener != null) {
				checkedListener.check(status);
			}
		}
	};

	// 设置默认的选项
	public void setData(String status) {
		this.status = status;
		int id = R.id.All;
		if (status.equals(AlbumAuthority.all)) {
			id = R.id.All;
		} else if (status.equals(AlbumAuthority.friend)) {
			id = R.id.friends;
		} else if (status.equals(AlbumAuthority.privateMe)) {
			id = R.id.privateMe;
		}
		((RadioButton) mView.findViewById(id)).setChecked(true);
	}
	public String getData() {
		return status;
	}

	public interface CheckedButtonThreeListener {
		public void check(String status);
	}

	public CheckedButtonThreeListener getCheckedListener() {
		return checkedListener;
	}
	public void setCheckedListener(CheckedButtonThreeListener checkedListener) {
		this.checkedListener = checkedListener;
	}
}
