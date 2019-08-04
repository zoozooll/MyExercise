package com.beem.project.btf.ui.views;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.CutomerlimitEditText;
import com.beem.project.btf.bbs.view.CutomerlimitEditText.OnChanageListener;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.TimeflyLocalNotify;
import com.beem.project.btf.ui.views.DateTimePickerView.Viewcount;
import com.beem.project.btf.ui.views.DateTimePickerView.getDataListener;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

public class ChangeAlbumAuthorityView implements
		android.widget.RadioGroup.OnCheckedChangeListener {
	protected static final String tag = ChangeAlbumAuthorityView.class
			.getSimpleName();
	private TextView notify_d, notify_MY, notify_prompt, remind_note;
	private CutomerlimitEditText albumSignEdit;
	private RadioGroup authorityRG;
	private LinearLayout remind_wraper, remind_time_wraper;
	private ToggleButton remind_toggle;
	private Context mContext;
	private View mView, selectTimeview;
	private DateTimePickerView selectTime;
	private String authority;
	private ImageFolder folder;
	private ToastCommon toastCommon;
	private String time;
	private boolean isLocal;
	private int count;
	private ImageFolder imageFolder;
	private int sendTime;

	/*private Handler handler = new Handler() {
	};*/
	public interface onChangeAlbumListen {
		void onChangePhoto(String signature, String authority);
		void onChangeNotify(String valid, String time);
	}

	// 设置时光提醒UI
	private void setNotifyDate(String valid, String time) {
		boolean isOpen = Valid.open.val.equals(valid);
		remind_toggle.setChecked(isOpen);
		if (isOpen) {
			remind_time_wraper.setVisibility(View.VISIBLE);
		} else {
			remind_time_wraper.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(time)) {
			time = LoginManager.getInstance().getSystemTime();
		}
		int[] date = BBSUtils.parseDate(time);
		notify_d.setText(String.valueOf(date[2]));
		notify_MY.setText(String.valueOf(date[1] + "/" + date[0]));
		selectTime.setcurrentData(date);
	}
	public ChangeAlbumAuthorityView(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.timefly_remind_layout, null);
		notify_d = (TextView) mView.findViewById(R.id.text_day);
		notify_MY = (TextView) mView.findViewById(R.id.text_monthandyear);
		remind_note = (TextView) mView.findViewById(R.id.remind_note);
		remind_note.setVisibility(View.GONE);
		albumSignEdit = (CutomerlimitEditText) mView
				.findViewById(R.id.albumSignEdit);
		albumSignEdit.setHint("今天图片还没签名哟!");
		albumSignEdit.setOnChangeListener(new OnChanageListener() {
			@Override
			public void onChange(String editTextStr) {
				folder.setSignature(editTextStr);
			}
		});
		authorityRG = (RadioGroup) mView.findViewById(R.id.selecRange);
		authorityRG.setOnCheckedChangeListener(this);
		remind_wraper = (LinearLayout) mView.findViewById(R.id.remind_wraper);
		remind_time_wraper = (LinearLayout) mView
				.findViewById(R.id.remind_time_wraper);
		remind_time_wraper.setVisibility(View.GONE);
		/** 添加时间滚动选择start */
		Calendar calendar = Calendar.getInstance();
		int Syear = calendar.get(Calendar.YEAR);
		selectTime = new DateTimePickerView(mContext, Syear, 2050,
				Viewcount.three, 5);
		selectTime.setShadowColor(0xFFF7F6FA, 0x99F7F6FA, 0x00F7F6FA);
		selectTime.setDataListener(new getDataListener() {
			@Override
			public void setdata(int[] data) {
				notify_d.setText(data[2] + "");
				notify_MY.setText(data[1] + "/" + data[0]);
			}
		});
		selectTimeview = selectTime.getmView();
		selectTimeview.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		remind_time_wraper.addView(selectTimeview);
		/** 添加时间滚动选择end */
		notify_prompt = (TextView) mView.findViewById(R.id.text_remind_off);
		remind_toggle = (ToggleButton) mView.findViewById(R.id.remind_toggle);
		// 监听开关
		remind_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					notify_prompt.setText("已开启");
					notify_prompt.setTextColor(mContext.getResources()
							.getColor(R.color.tab_blue));
					remind_time_wraper.setVisibility(View.VISIBLE);
				} else {
					notify_prompt.setText("未开启");
					notify_prompt.setTextColor(mContext.getResources()
							.getColor(R.color.bg_brown));
					remind_time_wraper.setVisibility(View.GONE);
				}
			}
		});
	}
	public void setRes(ImageFolder folder) {
		this.folder = folder;
		albumSignEdit.setText(folder.getSignature());
		int checkedId;
		authority = folder.getAuthority();
		if (authority.equals(AlbumAuthority.all)) {
			checkedId = R.id.All;
		} else if (authority.equals(AlbumAuthority.friend)) {
			checkedId = R.id.friends;
		} else {
			checkedId = R.id.privateMe;
		}
		authorityRG.check(checkedId);
		setNotifyDate(folder.getNotify_valid(), folder.getNotify_time());
	}
	public void changeAlbumProperty(final onChangeAlbumListen lis) {
		if (!LoginManager.getInstance().isLogined()) {
			CToast.showToast(mContext, "离线不能进行此操作", Toast.LENGTH_SHORT);
			return;
		}
		new VVBaseLoadingDlg<Boolean>(
				new VVBaseLoadingDlgCfg(mContext).setShowWaitingView(true)) {
			private Valid valid;
			private String notify_time;

			@SuppressWarnings("deprecation")
			@Override
			protected Boolean doInBackground() {
				int[] ymd = selectTime.getcurrentData();
				Date date = new Date();
				valid = remind_toggle.isChecked() ? Valid.open : Valid.close;
				if (date.getYear() + 1900 == ymd[0]
						&& date.getMonth() + 1 == ymd[1]
						&& date.getDate() == ymd[2]) {
					time = "1分钟";
					isLocal = true;
					notify_time = new StringBuffer()
						.append(ymd[0])
						.append("-")
						.append(BBSUtils.padStrBefore(
								String.valueOf(ymd[1]), '0', 2))
						.append("-")
						.append(BBSUtils.padStrBefore(
								String.valueOf(ymd[2]), '0', 2))
						.append(" 00:00:00").toString();
				} else {
					isLocal = false;
					int year = (date.getYear() + 1900) * 365;
					int month = (date.getMonth() + 1) * 30;
					int day = date.getDate();
					int a = ymd[0] * 365 + ymd[1] * 30 + ymd[2];
					time = a - year - month - day + "天";
					notify_time = new StringBuffer()
							.append(ymd[0])
							.append("-")
							.append(BBSUtils.padStrBefore(
									String.valueOf(ymd[1]), '0', 2))
							.append("-")
							.append(BBSUtils.padStrBefore(
									String.valueOf(ymd[2]), '0', 2))
							.append(" 00:00:00").toString();
				}
				Map<String, Object> rstOne = null;;
				try {
					rstOne = TimeflyService
							.managePhotogroup(LoginManager.getInstance()
									.getJidParsed(), folder.getGid(), folder
									.getCreateTime(), albumSignEdit.getText()
									.toString(), authority);
				} catch (WSError e) {
					e.printStackTrace();
					setManulaTimeOut(true);
				}
				Map<String, Object> rstTwo = TimeflyService
						.setPhotoGroupNotify(folder.getGid(),
								folder.getCreateTime(), notify_time, valid);
				return JsonParseUtils.getResult(rstOne)
						&& JsonParseUtils.getResult(rstTwo);
			}
			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					if (valid == Valid.open ) {
						toastCommon = ToastCommon.createToastConfig();
						toastCommon.ToastShow(mContext, null,
								LoginManager.getInstance().getMyContact()
										.getSuitableName(), time);
						if (isLocal) {
							//							Message message = Message.obtain();
							//							message.obj = new Object[] { folder, time };
							//							message.what = sendTime;
							//							mHandler.removeMessages(sendTime);
							//							mHandler.sendMessageDelayed(message, 60 * 1000);
							EventBus.getDefault().post(new TimeflyLocalNotify(folder, time, valid));
						} else {
						}
					}
					if (lis != null) {
						lis.onChangeNotify(valid.val, notify_time);
						lis.onChangePhoto(albumSignEdit.getText().toString(),
								authority);
					}
				} else {
					CToast.showToast(mContext, "修改失败", Toast.LENGTH_SHORT);
				}
			}
		}.execute();
	}
	public View getmView() {
		return mView;
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == authorityRG) {
			switch (checkedId) {
				case R.id.All: {
					authority = AlbumAuthority.all;
					break;
				}
				case R.id.privateMe: {
					authority = AlbumAuthority.privateMe;
					break;
				}
				case R.id.friends: {
					authority = AlbumAuthority.friend;
					break;
				}
				default:
					break;
			}
		}
	}
	//	public void setHandler(Handler mHandler) {
	//		this.mHandler = mHandler;
	//	}
	public void setImageFolder(ImageFolder imageFolder) {
		this.imageFolder = imageFolder;
	}
	public void setSendTime(int sendTime) {
		this.sendTime = sendTime;
	}
}
