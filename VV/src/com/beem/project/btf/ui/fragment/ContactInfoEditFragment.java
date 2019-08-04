package com.beem.project.btf.ui.fragment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.CutomerlimitEditText;
import com.beem.project.btf.bbs.view.EditNameDialogView;
import com.beem.project.btf.bbs.view.EditNameDialogView.EditNameDialogType;
import com.beem.project.btf.bbs.view.LimitEditView;
import com.beem.project.btf.bbs.view.RadioButtonTwoView;
import com.beem.project.btf.bbs.view.RadioButtonTwoView.CheckedListener;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.imagefilter.crop.CropActivity;
import com.beem.project.btf.imagefilter.crop.CropExtras;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.ui.activity.ContactInfoActivity.ContactFrgmtStatus;
import com.beem.project.btf.ui.activity.ContactInfoActivity.IFragmentTransaction;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.BottomPopupWindow;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionListener;
import com.beem.project.btf.ui.views.BottomPopupWindow.PopupActionType;
import com.beem.project.btf.ui.views.DateTimePickerView;
import com.beem.project.btf.ui.views.DateTimePickerView.Viewcount;
import com.beem.project.btf.ui.views.ProvinceCityView2;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.update.UploadUtil.UpdateTaskCallback;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.UIHelper;
import com.beem.project.btf.utils.BBSUtils.DbidType;
import com.beem.project.btf.utils.FileUtil;
import com.btf.push.UserInfoPacket;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.butterfly.vv.camera.GalleryActivity;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.view.grid.ImageGridAdapter;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import de.greenrobot.event.EventBus;

/**
 * @func 个人信息界面编辑界面 我的资料
 * @author yuedong bao
 * @time 2015-1-6 下午5:25:29
 */
@SuppressLint("ValidFragment")
public class ContactInfoEditFragment extends Fragment implements
		OnClickListener, IFragmentTransaction, IEventBusAction {
	private static final String TAG = "ContactInfoEditFragment";
	private BBSCustomerDialog blurDlg;
	private View view;
	private Context mContext;
	private RelativeLayout layout_nickname;
	private TextView layout_nickname_tv;
	private RelativeLayout layout_email;
	private TextView layout_email_tv;
	private RelativeLayout layout_sex;
	private TextView layout_sex_tv;
	private View layout_birthday;
	private TextView layout_bday_tv;
	private LinearLayout layout_signature;
	private TextView layout_signature_tv;
	private RelativeLayout layout_city;
	private TextView layout_city_tv;
	private RelativeLayout layout_university;
	private TextView layout_school_tv;
	private RelativeLayout layout_major;
	private TextView layout_major_tv;
	private RelativeLayout layout_en_time;
	private TextView layout_enroltime_tv;
	private LinearLayout layout_hobby;
	private TextView layout_hobby_tv;
	private UserInfoPacket modifyPacket = new UserInfoPacket();
	private PopupActionListener itemscontrolOnClick;
	private ImageGridAdapter mAlbumGridAdapter;
	private BottomPopupWindow filterPopupWindow;
	private GridView gridView;
	private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
	private SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Contact contact;

	public ContactInfoEditFragment() {
		super();
	}
	public ContactInfoEditFragment(Contact contact) {
		this.contact = contact;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//LogUtils.i("onCreateView");
		mContext = getActivity();
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			showInfo();
			return view;
		}
		view = inflater.inflate(R.layout.contactinfo_edit, container, false);
		// 昵称
		layout_nickname = (RelativeLayout) view.findViewById(R.id.layout_name);
		layout_nickname_tv = (TextView) view.findViewById(R.id.profile_tv_name);
		layout_nickname.setOnClickListener(this);
		// 邮箱
		layout_email = (RelativeLayout) view.findViewById(R.id.layout_email);
		layout_email_tv = (TextView) view.findViewById(R.id.layout_email_tv);
		layout_email.setOnClickListener(this);
		// 性别
		layout_sex = (RelativeLayout) view.findViewById(R.id.layout_sex);
		layout_sex_tv = (TextView) view.findViewById(R.id.profile_tv_sex);
		layout_sex.setOnClickListener(this);
		// 生日
		layout_birthday = view.findViewById(R.id.layout_birthday);
		layout_bday_tv = (TextView) view.findViewById(R.id.profile_tv_birthday);
		layout_birthday.setOnClickListener(this);
		// 签名
		layout_signature = (LinearLayout) view.findViewById(R.id.layout_sign);
		layout_signature_tv = (TextView) view
				.findViewById(R.id.profile_tv_sign);
		layout_signature.setOnClickListener(this);
		// 所在城市
		layout_city = (RelativeLayout) view.findViewById(R.id.layout_city);
		layout_city_tv = (TextView) view.findViewById(R.id.profile_tv_city);
		layout_city.setOnClickListener(this);
		// 毕业院校
		layout_university = (RelativeLayout) view
				.findViewById(R.id.layout_univ);
		layout_school_tv = (TextView) view.findViewById(R.id.profile_tv_univ);
		layout_university.setOnClickListener(this);
		// 专业
		layout_major = (RelativeLayout) view.findViewById(R.id.layout_company);
		layout_major_tv = (TextView) view.findViewById(R.id.profile_tv_major);
		layout_major.setOnClickListener(this);
		// 入学时间
		layout_en_time = (RelativeLayout) view
				.findViewById(R.id.layout_en_time);
		layout_enroltime_tv = (TextView) view
				.findViewById(R.id.profile_tv_en_date);
		layout_en_time.setOnClickListener(this);
		// 兴趣爱好
		layout_hobby = (LinearLayout) view.findViewById(R.id.layout_interest);
		layout_hobby_tv = (TextView) view
				.findViewById(R.id.profile_tv_interest);
		layout_hobby.setOnClickListener(this);
		gridView = (GridView) view.findViewById(R.id.myGrid_info);
		mAlbumGridAdapter = new ImageGridAdapter((Activity) mContext,
				new ArrayList<VVImage>(), ContactFrgmtStatus.EditContactInfo);
		gridView.setAdapter(mAlbumGridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mAlbumGridAdapter.getCount() == 1) {
					// 最后的+号
					itemscontrolOnClick = new MenuIMGsrcOnclickLT(arg2);
					filterPopupWindow = new BottomPopupWindow(
							(Activity) mContext, itemscontrolOnClick,
							PopupActionType.TAKEPHOTO);
					filterPopupWindow.showAtLocation(getActivity()
							.findViewById(R.id.id_content), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
				} else {
					if (arg2 == 0) {
						itemscontrolOnClick = new MenuIMGsrcOnclickLT(arg2);
						filterPopupWindow = new BottomPopupWindow(
								(Activity) mContext, itemscontrolOnClick,
								PopupActionType.DEL);
						filterPopupWindow.showAtLocation(getActivity()
								.findViewById(R.id.id_content), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
					} else if (arg2 == mAlbumGridAdapter.getCount() - 1) {
						// 最后的+号
						itemscontrolOnClick = new MenuIMGsrcOnclickLT(arg2);
						filterPopupWindow = new BottomPopupWindow(
								(Activity) mContext, itemscontrolOnClick,
								PopupActionType.TAKEPHOTO);
						filterPopupWindow.showAtLocation(getActivity()
								.findViewById(R.id.id_content), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
					} else {
						itemscontrolOnClick = new MenuIMGsrcOnclickLT(arg2);
						filterPopupWindow = new BottomPopupWindow(
								(Activity) mContext, itemscontrolOnClick,
								PopupActionType.COVER_DEL);
						filterPopupWindow.showAtLocation(getActivity()
								.findViewById(R.id.id_content), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
					}
				}
			}
		});
		gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));
		//LogUtils.i("~~onCreateView~~");
		showInfo();
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onClick(View v) {
		blurDlg = BBSCustomerDialog.newInstance(mContext, R.style.blurdialog);
		if (v == layout_nickname) {
			// 昵称
			EditNameDialogView editNameView = new EditNameDialogView(mContext);
			editNameView
					.setText(layout_nickname_tv.getText().toString().trim());
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("昵称 :");
			dilaogView.setContentView(editNameView.getView());
			dilaogView.setMargin();
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					String newNickName = ((EditText) contentView
							.findViewById(R.id.nickNameEdit)).getText()
							.toString().trim();
					if (!newNickName.isEmpty()) {
						blurDlg.dismiss();
						layout_nickname_tv.setText(newNickName);
						modifyPacket
								.setField(UserInfoKey.nickname, newNickName);
					} else {
						CToast.showToast(mContext, "昵称不能为空", Toast.LENGTH_SHORT);
					}
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_email) {
			final EditNameDialogView editNameView = new EditNameDialogView(
					mContext);
			editNameView.setText(layout_email_tv.getText().toString());
			editNameView.setHint("请输入邮箱");
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("邮箱 :");
			dilaogView.setMargin();
			dilaogView.setContentView(editNameView.getView());
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					String content = editNameView.getText().trim();
					if (BBSUtils.isMatchEmail(content)) {
						blurDlg.dismiss();
						layout_email_tv.setText(content);
						modifyPacket.setField(UserInfoKey.email, content);
					} else {
						CToast.showToast(mContext, "邮箱格式错误，请重新输入！",
								Toast.LENGTH_SHORT);
					}
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_sex) {
			// 填充两个单选按钮
			RadioButtonTwoView buttonTwoView = new RadioButtonTwoView(mContext);
			buttonTwoView.setText("男", "女");
			buttonTwoView.initiaData(layout_sex_tv.getText().toString()
					.equals("男"));
			buttonTwoView.setCheckedListener(new CheckedListener() {
				@Override
				public void check(boolean status) {
					blurDlg.dismiss();
					String sex = status ? "男" : "女";
					layout_sex_tv.setText(sex);
					if (!sex.equals(modifyPacket.getField(UserInfoKey.sex))) {
					}
					modifyPacket.setField(UserInfoKey.sex, status ? "1" : "0");
				}
			});
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("性别 :");
			// 隐藏底部按钮
			dilaogView.setBtnGone();
			dilaogView.setMargin();
			dilaogView.setContentView(buttonTwoView.getView());
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_birthday) {
			// 获取当前年份
			Calendar calendar = Calendar.getInstance();
			int endyear = calendar.get(Calendar.YEAR);
			final DateTimePickerView TimePickerView = new DateTimePickerView(
					mContext, 0, endyear, Viewcount.three);
			// 拆解字符串，设置滚轮默认值
			String date = contact.getBday();
			String[] datas = date.split("-");
			TimePickerView.setcurrentData(datas);
			TimePickerView.setShadowColor(0xFFF7F6FA, 0x99F7F6FA, 0x00F7F6FA);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("生日 :");
			dilaogView.setContentView(TimePickerView.getmView());
			dilaogView.setPositiveButton("完成", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					// 通过contentView读取内容区域的值
					int[] data = TimePickerView.getcurrentData();
					String month = BBSUtils.padStrBefore(
							String.valueOf(data[1]), '0', 2);
					String day = BBSUtils.padStrBefore(String.valueOf(data[2]),
							'0', 2);
					layout_bday_tv.setText(data[0] + "-" + month + "-" + day);
					try {
						Date enroltime = sf.parse(layout_bday_tv.getText()
								.toString().trim());
						modifyPacket.setField(UserInfoKey.bday,
								sf.format(enroltime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_signature) {
			// 填充一个字数限制输入框
			LimitEditView limitNameView = new LimitEditView(mContext);
			limitNameView.setHint("输入你的个性签名(最多40个字)");
			limitNameView.setText(layout_signature_tv.getText().toString());
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("个人签名 :");
			dilaogView.setMargin();
			dilaogView.setContentView(limitNameView.getView());
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					String signature = ((CutomerlimitEditText) contentView
							.findViewById(R.id.limitedit)).getText().toString()
							.trim();
					layout_signature_tv.setText(signature);
					modifyPacket.setField(UserInfoKey.signature, signature);
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_city) {
			final ProvinceCityView2 provinceview = new ProvinceCityView2(
					mContext);
			provinceview.setShadowColor(0xFFF7F6FA, 0x99F7F6FA, 0x00F7F6FA);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setNoPadding();
			dilaogView.setTitle("所在城市 :");
			dilaogView.setContentView(provinceview.getmView());
			dilaogView.setPositiveButton("完成", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					// 通过contentView读取内容区域的值
					blurDlg.dismiss();
					String[] currentCodes = provinceview
							.getCurrentCodesAndName();
					layout_city_tv.setText(currentCodes[0]);
					modifyPacket.setField(UserInfoKey.city, currentCodes[1]);
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_university) {
			// 选择学校
			final EditNameDialogView editNameView = new EditNameDialogView(
					mContext, EditNameDialogType.search);
			editNameView.setHint("请输入您的毕业学校");
			editNameView.setText(layout_school_tv.getText().toString());
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("毕业学校 :");
			dilaogView.setContentView(editNameView.getView());
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					String university = editNameView.getText().trim();
					String university_id = editNameView.getid();
					if (!university.isEmpty()) {
						if (university_id != null) {
							layout_school_tv.setText(university);
							blurDlg.dismiss();
							modifyPacket.setField(UserInfoKey.school,
									university_id);
						} else {
							CToast.showToast(mContext, "请在列表中选择一项",
									Toast.LENGTH_SHORT);
						}
					} else {
						CToast.showToast(mContext, "毕业学校不能为空",
								Toast.LENGTH_SHORT);
					}
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_major) {
			// 输入专业
			final EditNameDialogView editNameView = new EditNameDialogView(
					mContext);
			editNameView.setHint("请输入您的院系专业");
			editNameView.setMaxlenth(20);
			editNameView.setText(layout_major_tv.getText().toString());
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("专业 :");
			dilaogView.setMargin();
			dilaogView.setContentView(editNameView.getView());
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					String majorName = ((EditText) contentView
							.findViewById(R.id.nickNameEdit)).getText()
							.toString().trim();
					if (!majorName.isEmpty()) {
						blurDlg.dismiss();
						layout_major_tv.setText(majorName);
						modifyPacket.setField(UserInfoKey.major, majorName);
					} else {
						CToast.showToast(mContext, "专业不能为空", Toast.LENGTH_SHORT);
					}
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_en_time) {
			// 获取当前年份
			Calendar calendar = Calendar.getInstance();
			int endyear = calendar.get(Calendar.YEAR);
			final DateTimePickerView TimePickerView = new DateTimePickerView(
					mContext, 0, endyear, Viewcount.one);
			TimePickerView.setShadowColor(0xFFF7F6FA, 0x99F7F6FA, 0x00F7F6FA);
			String[] datas = { layout_enroltime_tv.getText().toString() };
			TimePickerView.setcurrentData(datas);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("入学时间 :");
			dilaogView.setContentView(TimePickerView.getmView());
			dilaogView.setPositiveButton("完成", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					// 通过contentView读取内容区域的值
					int[] data = TimePickerView.getcurrentData();
					String en_time = String.valueOf(data[0]);
					layout_enroltime_tv.setText(en_time);
					try {
						Date date = sf1.parse(en_time);
						en_time = sf2.format(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Log.i("format", en_time);
					blurDlg.dismiss();
					modifyPacket.setField(UserInfoKey.enroltime, en_time);
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == layout_hobby) {
			// 填充一个字数限制输入框
			LimitEditView limitNameView = new LimitEditView(mContext);
			limitNameView.setHint("输入你的兴趣爱好(最多40个字)");
			limitNameView.setText(layout_hobby_tv.getText().toString());
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("兴趣爱好 :");
			dilaogView.setMargin();
			dilaogView.setContentView(limitNameView.getView());
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					String hobby = ((CutomerlimitEditText) contentView
							.findViewById(R.id.limitedit)).getText().toString()
							.trim();
					layout_hobby_tv.setText(hobby);
					modifyPacket.setField(UserInfoKey.hobby, hobby);
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		}
	}
	public void modifyContactInfo() {
		final Map<UserInfoKey, String> modifyMaps = modifyPacket
				.cloneFieldMaps();
		if (modifyMaps.isEmpty()) {
			EventBus.getDefault().post(
					new EventBusData(EventAction.ModifyContactInfo, null));
			return;
		}
		new VVBaseLoadingDlg<PacketResult>(new VVBaseLoadingDlgCfg(
				getActivity()).setShowWaitingView(true)) {
			@Override
			protected PacketResult doInBackground() {
				PacketResult result = ContactService.getInstance()
						.modifyContactInfo(modifyMaps);
				return result;
			}
			@Override
			protected void onPostExecute(PacketResult result) {
				if (result != null && result.isOk()) {
					modifyPacket.clearFileds();
					contact.saveData(modifyMaps);
					EventBus.getDefault().post(
							new EventBusData(EventAction.ModifyContactInfo,
									modifyMaps));
					CToast.showToast(mContext, "修改资料成功", Toast.LENGTH_SHORT);
				} else {
					CToast.showToast(mContext, "修改资料失败", Toast.LENGTH_SHORT);
					EventBus.getDefault().post(
							new EventBusData(EventAction.ModifyContactInfo,
									null));
				}
			}
		}.execute();
	}
	private void showInfo() {
		// 头像
		String photo_big = contact.getPhoto_big();
		String photo_small = contact.getPhoto_small();
		List<VVImage> vvImages = new ArrayList<VVImage>();
		if (!TextUtils.isEmpty(photo_big)) {
			String[] strs_big = BBSUtils
					.splitPhotos(DBKey.photo_big, photo_big);
			String[] strs_small = BBSUtils.splitPhotos(DBKey.photo_small,
					photo_small);
			//LogUtils.i("photo_big:" + photo_big + " photo_small:" + photo_small + " big.len:" + strs_big.length
			//					+ " small.len:" + strs_small.length);
			if (strs_big != null) {
				for (int i = 0; i < strs_big.length; i++) {
					VVImage vvImg = new VVImage();
					vvImg.setPath(strs_big[i]);
					vvImg.setPathThumb(strs_small[i]);
					vvImages.add(vvImg);
				}
			}
		}
		mAlbumGridAdapter.clearItems();
		mAlbumGridAdapter.addItems(vvImages);
		mAlbumGridAdapter.notifyDataSetChanged();
		// 其他
		for (final DBKey keyDB : DBKey.values()) {
			switch (keyDB) {
				case sex: {
					layout_sex_tv.setText(contact.getFieldStr(keyDB)
							.equals("0") ? "女" : "男");
					break;
				}
				case email: {
					layout_email_tv.setText(contact.getFieldStr(keyDB));
					break;
				}
				case bday: {
					layout_bday_tv.setText(contact.getFieldStr(keyDB));
					break;
				}
				case nickName: {
					layout_nickname_tv.setText(contact.getFieldStr(keyDB));
					break;
				}
				case signature: {
					layout_signature_tv.setText(contact.getFieldStr(keyDB));
					break;
				}
				case cityId: {
					String field = (String) contact.getField(keyDB);
					String queryData = "";
					if (field != null) {
						queryData = BBSUtils.dbquery(field, mContext,
								DbidType.CITY);
						layout_city_tv.setText(queryData);
					} else {
						layout_city_tv.setText("");
					}
					break;
				}
				case schoolId: {
					new AsyncTask<String, Integer, String>() {
						@Override
						protected String doInBackground(String... params) {
							String school = BBSUtils.dbquery(params[0],
									mContext, DbidType.COLLEAGE);
							Log.i(TAG,
									TAG + "~~SchoolId~~"
											+ contact.getFieldStr(keyDB)
											+ "~~school~~" + school);
							return school;
						}
						@Override
						protected void onPostExecute(String result) {
							super.onPostExecute(result);
							layout_school_tv.setText(result);
						}
					}.execute(contact.getFieldStr(keyDB));
					break;
				}
				case major: {
					layout_major_tv.setText(contact.getFieldStr(keyDB));
					break;
				}
				case enroltime: {
					// 入学时间
					if ((TextUtils.isEmpty(contact.getEnroltime()) || contact
							.getEnroltime().equals("0000-00-00"))) {
						layout_enroltime_tv.setText("");
					} else {
						try {
							Date enroltime = sf.parse(contact
									.getFieldStr(keyDB));
							layout_enroltime_tv.setText(sf1.format(enroltime));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					break;
				}
				case hobby: {
					layout_hobby_tv.setText(contact.getFieldStr(keyDB));
					break;
				}
			}
		}
	}

	class MenuIMGsrcOnclickLT implements PopupActionListener {
		int position;

		public MenuIMGsrcOnclickLT() {
			super();
		}
		public MenuIMGsrcOnclickLT(int position) {
			this.position = position;
		}
		@Override
		public void itemsClick(PopupActionType type, int i) {
			filterPopupWindow.dismiss();
			switch (type) {
				case DEL: {
					if (i == 0) {
						deletePhoto(position);
					}
					break;
				}
				case COVER_DEL: {
					if (i == 0) {
						setCoverPhoto(position);
					} else if (i == 1) {
						deletePhoto(position);
					}
					break;
				}
				case TAKEPHOTO: {
					if (i == 0) {
						photoUri = BBSUtils.takePhoto(
								ContactInfoEditFragment.this,
								Constants.TAKEPHOTO);
					} else if (i == 1) {
						doPickPhotoFromGallery();
					}
					break;
				}
			}
		}
	}

	/**
	 * 调用相册
	 * @Title: doPickPhotoFromGallery
	 * @Description:
	 * @return: void
	 */
	protected void doPickPhotoFromGallery() {
		// 跳转到相册库
		Intent intent = new Intent(getActivity(), GalleryActivity.class);
		//intent.putExtra("dopick", true);
		//intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, CameraActivity.GALLERY_TYPE_CONTACTCARD);
		intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, 1);
		intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
		intent.putExtra(GalleryActivity.GALLERY_CROP, true);
		intent.putExtra(GalleryActivity.GALLERY_PICKABLE, true);
		intent.putExtra(CropExtras.KEY_ASPECT_X, 1);
		intent.putExtra(CropExtras.KEY_ASPECT_Y, 1);
		intent.putExtra(GalleryActivity.GALLERY_OUTPUT,
				FileUtil.getCropTempPhotoUri(getActivity()));
		startActivityForResult(intent, Constants.PICKPHOTO);
	}
	private void setCoverPhoto(int position) {
		List<VVImage> temp = new ArrayList<VVImage>();
		if (position == 0) {
			return;
		} else {
			temp.add(mAlbumGridAdapter.getItem(position));
			for (int i = 0; i < mAlbumGridAdapter.getItems().size(); i++) {
				if (i == position) {
					continue;
				}
				temp.add(mAlbumGridAdapter.getItem(i));
			}
			mAlbumGridAdapter.clearItems();
			mAlbumGridAdapter.addItems(temp);
		}
		String photoBig = BBSUtils.makePhotos(DBKey.photo_big,
				mAlbumGridAdapter.getItems());
		String photoSmall = BBSUtils.makePhotos(DBKey.photo_small,
				mAlbumGridAdapter.getItems());
		modifyPacket.setField(UserInfoKey.big, photoBig);
		modifyPacket.setField(UserInfoKey.small, photoSmall);
		mAlbumGridAdapter.notifyDataSetChanged();
	}

	/**
	 * 调用相机
	 * @Title: takePhoto
	 * @Description:
	 * @return: void
	 */
	/** 使用相册中的图片 */
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	/** 获取到的图片路径 */
	private String picPath;
	private Uri photoUri;
	/** 从Intent获取图片路径的KEY */
	public static final String KEY_PHOTO_PATH = "photo_path";

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//LogUtils.i("onActivityResult:" + getClass().getSimpleName() + " requestCode:" + requestCode + " resultCode:"
		//				+ resultCode);
		if (requestCode == Constants.TAKEPHOTO
				&& resultCode == Activity.RESULT_OK) {
			picPath = BBSUtils.getTakePhotoPath(getActivity());
			if (picPath != null) {
				//				ClipPictureActivity.launch(this, picPath);
				Intent intent = new Intent(getActivity(), CropActivity.class);
				//intent.putExtra("dopick", true);
				//intent.putExtra(CameraActivity.CAMERA_GALLERY_TYPE, CameraActivity.GALLERY_TYPE_CONTACTCARD);
				Uri cameraUri = FileUtil.getCameraPhotoUri(getActivity());
				intent.setData(cameraUri);
				intent.putExtra(CropExtras.KEY_ASPECT_X, 1);
				intent.putExtra(CropExtras.KEY_ASPECT_Y, 1);
				intent.putExtra(GalleryActivity.GALLERY_OUTPUT,
						FileUtil.getCropTempPhotoUri(getActivity()));
				startActivityForResult(intent, Constants.PICKPHOTO);
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"选择图片文件不正确", Toast.LENGTH_SHORT).show();
			}
		} else if (Constants.PICKPHOTO == requestCode
				&& resultCode == Activity.RESULT_OK) {
			final String path = FileUtil.getCropTempPhotoUri(mContext)
					.getPath();
			String url = AppProperty.getInstance().VVAPI
					+ AppProperty.getInstance().UPLOAD_PORTRAIT;
			UploadUtil.updateAccountIcon(path, url, new UpdateTaskCallback() {
				@Override
				public void preExecute() {
					UIHelper.showDialogForLoading(
							mContext,
							getString(R.string.please_wait_picture_is_uploading),
							true);
				}
				@Override
				public void postExecute(String[] uploadUrl) {
					UIHelper.hideDialogForLoading();
					if (uploadUrl != null) {
						// 删除拍照图片
						new File(path).delete();
						setContactCardField(uploadUrl);
						CToast.showToast(mContext,
								R.string.image_uploaded_successfully,
								Toast.LENGTH_SHORT);
					} else {
						CToast.showToast(mContext,
								R.string.image_upload_failed,
								Toast.LENGTH_SHORT);
					}
				}
				@Override
				public void cancelExecute() {
					// TODO Auto-generated method stub
				}
				@Override
				public void onUploading(String[] result) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	private void setContactCardField(String[] savePhoto) {
		if (savePhoto != null) {
			int size = mAlbumGridAdapter.getItems().size();
			VVImage vvImage = new VVImage();
			vvImage.setPath(savePhoto[0]);
			vvImage.setPathThumb(savePhoto[1]);
			mAlbumGridAdapter.addItem(0, vvImage);
			if (size >= 8) {
				for (int i = 8; i < mAlbumGridAdapter.getItems().size(); i++) {
					mAlbumGridAdapter.removeItem(i);
				}
			}
			//LogUtils.i("tempVvImages.size:" + mAlbumGridAdapter.getItems().size());
			mAlbumGridAdapter.notifyDataSetChanged();
			String photoBig = BBSUtils.makePhotos(DBKey.photo_big,
					mAlbumGridAdapter.getItems());
			String photoSmall = BBSUtils.makePhotos(DBKey.photo_small,
					mAlbumGridAdapter.getItems());
			modifyPacket.setField(UserInfoKey.big, photoBig);
			modifyPacket.setField(UserInfoKey.small, photoSmall);
			//LogUtils.i("savePhoto:" + Arrays.asList(savePhoto) + " photoBig:" + photoBig + " photoSmall:" + photoSmall);
		}
	}
	@Override
	public boolean onBackPressed() {
		if (!modifyPacket.isEmpty()) {
			final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
					mContext, R.style.blurdialog);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("保存修改");
			dilaogView.setTextContent("是否保存对更改内容的修改");
			dilaogView.setPositiveButton("是", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					modifyContactInfo();
				}
			});
			dilaogView.setNegativeButton("否", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					modifyPacket.clearFileds();
					EventBus.getDefault().post(
							new EventBusData(EventAction.ModifyContactInfo,
									null));
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else {
			EventBus.getDefault().post(
					new EventBusData(EventAction.ModifyContactInfo, null));
		}
		return true;
	}
	//TODO
	public void deletePhoto(int position) {
		mAlbumGridAdapter.removeItem(position);
		String photoBig = BBSUtils.makePhotos(DBKey.photo_big,
				mAlbumGridAdapter.getItems());
		String photoSmall = BBSUtils.makePhotos(DBKey.photo_small,
				mAlbumGridAdapter.getItems());
		modifyPacket.setField(UserInfoKey.big, photoBig);
		modifyPacket.setField(UserInfoKey.small, photoSmall);
		mAlbumGridAdapter.notifyDataSetChanged();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (data.getAction() == EventAction.SendResultFAlbum) {
			setContactCardField((String[]) data.getMsg());
		}
	}
}
