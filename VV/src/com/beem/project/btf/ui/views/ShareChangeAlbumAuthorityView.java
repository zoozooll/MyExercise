package com.beem.project.btf.ui.views;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.CutomerlimitEditText;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.butterfly.vv.service.TimeflyService;
import com.teleca.jamendo.api.WSError;

public class ShareChangeAlbumAuthorityView {
	private Context mContext;
	private Dialog blurDlg;
	private View blurView;
	private CutomerlimitEditText albumSignEdit;
	private RadioGroup selecRange;
	private RadioGroup selecRange2;
	private ArrayList<String> dataList = new ArrayList<String>();
	private Button sure, cancel;
	private String uid, albumID;
	private String albumAuthority = AlbumAuthority.all;
	private SharedPreferences mSettings;
	private String signal;
	private String create_time;
	private IAlbumOptCallBack optCallBack;

	public static final class AlbumAuthority {
		public static final String all = String.valueOf(0xffff);
		public static final String privateMe = String.valueOf(0x0000);
		public static final String friend = String.valueOf(0x0001);

		public static String getAlbumAuthorityText(String str) {
			if (str.equals(all))
				return "公开";
			if (str.equals(privateMe))
				return "私有";
			if (str.equals(friend))
				return "好友可见";
			return "未知";
		}
	}

	public static enum InvokedPlace {
		Setting, timeflyMain
	}

	// 是否正在清理checkGroup
	private Boolean changingGroup = true;
	private OnCheckedChangeListener selectRangeLis = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (changingGroup && group.getId() == selecRange.getId()) {
				changingGroup = false;
				selecRange2.clearCheck();
				changingGroup = true;
			} else if (changingGroup && group.getId() == selecRange2.getId()) {
				changingGroup = false;
				selecRange.clearCheck();
				changingGroup = true;
			}
			switch (checkedId) {
				case R.id.All: {
					albumAuthority = AlbumAuthority.all;
					break;
				}
				case R.id.friends: {
					albumAuthority = AlbumAuthority.friend;
					break;
				}
				case R.id.privateMe: {
					albumAuthority = AlbumAuthority.privateMe;
					break;
				}
			}
			// 以下是指定好友功能，待以后完善
			if (checkedId == R.id.specifyFriends) {
				dataList.clear();
				for (int i = 0; i < 10; i++) {
					dataList.add("" + i);
				}
			} else {
				dataList.clear();
			}
		}
	};

	public ShareChangeAlbumAuthorityView(Context mContext, Dialog blurDlg) {
		super();
		this.mContext = mContext;
		mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		blurView = inflater.inflate(R.layout.share_changephotoauthority, null);
		selecRange = (RadioGroup) blurView.findViewById(R.id.selecRange);
		selecRange2 = (RadioGroup) blurView.findViewById(R.id.selecRange2);
		sure = (Button) blurView.findViewById(R.id.sure);
		albumSignEdit = (CutomerlimitEditText) blurView
				.findViewById(R.id.albumSignEdit);
		cancel = (Button) blurView.findViewById(R.id.cancel);
		sure.setOnClickListener(clickLis);
		cancel.setOnClickListener(clickLis);
		selecRange.setOnCheckedChangeListener(selectRangeLis);
		selecRange2.setOnCheckedChangeListener(selectRangeLis);
		this.blurDlg = blurDlg;
	}
	public ShareChangeAlbumAuthorityView setData(String uid, String albumID,
			String albumAuthority, String signal, String create_time,
			IAlbumOptCallBack albumOperation) {
		this.uid = uid;
		this.albumID = albumID;
		this.albumAuthority = albumAuthority;
		this.signal = signal == null ? "" : signal;
		this.create_time = create_time;
		this.optCallBack = albumOperation;
		int id = R.id.All;
		if (albumAuthority.equals(AlbumAuthority.all)) {
			id = R.id.All;
		} else if (albumAuthority.equals(AlbumAuthority.friend)) {
			id = R.id.friends;
		} else if (albumAuthority.equals(AlbumAuthority.privateMe)) {
			id = R.id.privateMe;
		}
		albumSignEdit.setText(signal);
		((RadioButton) blurView.findViewById(id)).setChecked(true);
		albumSignEdit.setSelection(signal == null ? 0 : signal.length());
		return this;
	}
	public View getLayout() {
		return blurView;
	}

	// 修改相册属性回调
	public interface IAlbumOptCallBack {
		public void operateRst(boolean isSuccess, String signal,
				String authority);
	}

	private OnClickListener clickLis = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.sure: {
					if (!albumID.equals("-1")) {
						new VVBaseLoadingDlg<Boolean>(new VVBaseLoadingDlgCfg(
								mContext)) {
							@Override
							protected void onPreExecute() {
								blurDlg.dismiss();
							}
							@Override
							protected Boolean doInBackground() {
								Map<String, Object> resultMap = null;
								try {
									resultMap = TimeflyService
											.managePhotogroup(uid, albumID,
													create_time, signal,
													albumAuthority);
								} catch (WSError e) {
									e.printStackTrace();
									setManulaTimeOut(true);
								}
								if (resultMap != null) {
									return Integer.valueOf((String) resultMap
											.get("result")) == Constants.RESULT_OK;
								}
								return true;
							}
							@Override
							protected void onPostExecute(Boolean result) {
								optCallBack.operateRst(result, albumAuthority,
										albumSignEdit.getText().toString());
							}
						}.execute();
					} else {
						// 修改本地权限
						SharedPreferences.Editor editor = mSettings.edit();
						editor.putString(Constants.SET_ALBUMSIGNAL,
								albumSignEdit.getText().toString());
						editor.putString(Constants.SET_ALBUMSIGNAL,
								albumAuthority);
						editor.commit();
						Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT)
								.show();
						blurDlg.dismiss();
					}
					break;
				}
				case R.id.cancel: {
					blurDlg.dismiss();
					break;
				}
			}
		}
	};

	public interface ViewChangeListener {
		void change(int changID);
	}
}
