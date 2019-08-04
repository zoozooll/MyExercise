package com.beem.project.btf.ui.listpreference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;

public class MyListPreference extends ListPreference {
	private static final String LOGTAG = "MyListPreference";
	private CharSequence[] entries;
	private CharSequence[] entryValues;
	private int selectedId;
	private Context cxt;
	private int indexOfValue;

	public MyListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		cxt = context;
	}
	// 重写这个方法，用于同步Summary
	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
		// TODO Auto-generated method stub
		super.onSetInitialValue(restoreValue, defaultValue);
		entries = getEntries();
		entryValues = getEntryValues();
		String value = getValue();// 这个可以删除，只是用于debug
		indexOfValue = this.findIndexOfValue(getSharedPreferences().getString(
				this.getKey(), ""));
		Log.e(LOGTAG, "index:" + indexOfValue + ",value:" + value);
		if (indexOfValue >= 0) {
			String key = String.valueOf(entries[indexOfValue]);
			Log.e(LOGTAG, "key:" + key + ",value:" + value);
			if (null != key) {
				setSummary(key);
			}
		}
	}
	// 重写这个方法，添加一个OK按钮
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		// super.onPrepareDialogBuilder(builder);//不能调用父类的这个方法，否则点击列表项会关闭对话框
		builder.setSingleChoiceItems(entries, indexOfValue,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.v(LOGTAG, String.valueOf(which));
						selectedId = which;
					}
				});
		// builder.setPositiveButton(null,
		// null);//ListPreference源码中设置是这样写的，这里我们需要重写
		builder.setPositiveButton(getPositiveButtonText() == null ? "确定"
				: getPositiveButtonText(),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface,
							int paramInt) {
						if (selectedId >= 0) {
							setSummary(entries[selectedId]);
							paramDialogInterface.dismiss();
							MyListPreference.this
									.persistString(entryValues[selectedId]
											.toString());
							MyListPreference.this
									.callChangeListener(entryValues[selectedId]);
						}
					}
				});
	}
}
