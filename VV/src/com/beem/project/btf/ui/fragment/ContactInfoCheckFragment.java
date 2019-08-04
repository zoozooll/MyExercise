package com.beem.project.btf.ui.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.EditNameDialogView;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity.ContactFrgmtStatus;
import com.beem.project.btf.ui.activity.ContactInfoActivity.IFragmentTransaction;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.ImageGalleryActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.BBSUtils.DbidType;
import com.btf.push.UserInfoPacket;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.view.grid.ImageGridAdapter;
import com.butterfly.vv.vv.utils.CToast;

import de.greenrobot.event.EventBus;

/**
 * @func 个人信息查看界面
 * @author yuedong bao
 * @time 2015-1-6 下午5:25:10
 */
public class ContactInfoCheckFragment extends Fragment implements
		IFragmentTransaction, OnClickListener, IEventBusAction {
	private static final String tag = ContactInfoCheckFragment.class
			.getSimpleName();
	private View view;
	private TextView layout_age_tv;
	private TextView layout_constellation_tv;// 星座
	private TextView layout_distance_tv;
	private TextView layout_loginTime_tv;
	private TextView layout_nickName_tv;
	private TextView layout_signature_tv;
	private TextView layout_city_tv;
	private TextView layout_school_tv;
	private TextView layout_major_tv;
	private TextView layout_enroltime_tv;
	private TextView layout_hobby_tv;
	private TextView layout_timeid_tv;
	private View profile_layout_bottom;
	private Contact contact;
	private ContactType contactType;
	private TextView layout_phone_tv;
	private TextView layout_email_tv;
	private ViewGroup layout_phone, layout_email, layout_signature,
			layout_school, layout_major, layout_en_time, layout_hobby;
	private ImageGridAdapter mAlbumGridAdapter;
	private GridView gridView;
	private ArrayList<VVImage> tempVvImages;
	// 备注名
	private View remark;
	// 删除好友
	private View breakFriends;
	// 发送消息
	private View sendMsg;
	// 加为好友
	private View makeFriends;
	// 拉黑
	private View blacklist;
	private ScrollView scroll_lt;
	private Activity activity = null;
	private boolean isBlackList;
	private TextView tv_black;

	public ContactInfoCheckFragment(Contact contact, ContactType type) {
		super();
		this.contact = contact;
		this.contactType = type;
	}
	public ContactInfoCheckFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//LogUtils.i("onCreateView");
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		Bundle bundle = getArguments();
		isBlackList = bundle.getBoolean(ContactInfoActivity.isInBlacklist);
		view = inflater.inflate(R.layout.contactinfo_check, container, false);
		scroll_lt = (ScrollView) view.findViewById(R.id.scroll_lt);
		layout_timeid_tv = (TextView) view.findViewById(R.id.profile_tv_momoid);
		layout_age_tv = (TextView) view.findViewById(R.id.profile_tv_agenew);
		layout_constellation_tv = (TextView) view
				.findViewById(R.id.profile_tv_constellation);
		layout_distance_tv = (TextView) view
				.findViewById(R.id.profile_tv_distance);
		layout_loginTime_tv = (TextView) view
				.findViewById(R.id.profile_tv_time);
		layout_nickName_tv = (TextView) view.findViewById(R.id.profile_name);
		layout_phone_tv = (TextView) view.findViewById(R.id.layout_phone_tv);
		layout_phone = (ViewGroup) view.findViewById(R.id.layout_phone);
		layout_email = (ViewGroup) view.findViewById(R.id.layout_email);
		layout_email_tv = (TextView) view.findViewById(R.id.layout_email_tv);
		layout_signature_tv = (TextView) view
				.findViewById(R.id.profile_tv_signature);
		layout_signature = (ViewGroup) view.findViewById(R.id.layout_signature);
		layout_city_tv = (TextView) view.findViewById(R.id.tv_industry);
		layout_school_tv = (TextView) view.findViewById(R.id.profile_tv_school);
		layout_school = (ViewGroup) view.findViewById(R.id.layout_school);
		layout_major = (ViewGroup) view.findViewById(R.id.layout_major);
		layout_major_tv = (TextView) view.findViewById(R.id.profile_tv_major);
		layout_enroltime_tv = (TextView) view
				.findViewById(R.id.profile_tv_school_time);
		layout_en_time = (ViewGroup) view.findViewById(R.id.layout_en_time);
		layout_hobby_tv = (TextView) view
				.findViewById(R.id.profile_tv_hobby_sign);
		layout_hobby = (ViewGroup) view.findViewById(R.id.layout_hobby);
		profile_layout_bottom = view.findViewById(R.id.profile_layout_bottom);
		remark = view.findViewById(R.id.profile_layout_start_chat);
		breakFriends = view.findViewById(R.id.profile_layout_unfollow);
		makeFriends = view.findViewById(R.id.profile_layout_follow);
		sendMsg = view.findViewById(R.id.profile_layout_start_schat);
		blacklist = view.findViewById(R.id.profile_layout_report);
		tv_black = (TextView) view.findViewById(R.id.tv_black);
		tempVvImages = new ArrayList<VVImage>();
		mAlbumGridAdapter = new ImageGridAdapter(getActivity(), tempVvImages,
				ContactFrgmtStatus.CheckContactInfo);
		gridView = (GridView) view.findViewById(R.id.myGrid_info);
		gridView.setAdapter(mAlbumGridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ImageFolderItem folderItem = new ImageFolderItem();
				folderItem.setVVImages(tempVvImages);
				folderItem.setContact(contact);
				ImageGalleryActivity.launch(getActivity(), arg2, folderItem,
						contact.getSex(), true);
			}
		});
		tv_black.setText(isBlackList ? "解除黑名单" : "拉黑");
		activity = getActivity();
		remark.setOnClickListener(this);
		breakFriends.setOnClickListener(this);
		makeFriends.setOnClickListener(this);
		sendMsg.setOnClickListener(this);
		blacklist.setOnClickListener(this);
		//LogUtils.i("~~onCreateView~~");
		return view;
	}
	private void showInfo() {
		if (contact == null) {
			//LogUtils.e("the contact is null");
			return;
		}
		setPhoto();
		setAge();
		setDistance();
		setLoginTime();
		setJID();
		setNikeName();
		setPhone();
		setEmail();
		setSignature();
		setCity();
		setColleage();
		setMajor();
		setEnrolTime();
		setHobby();
		showBottomLayout(contactType);
		scroll_lt.fullScroll(View.FOCUS_UP);
	}

	public enum ContactType {
		MySelf, Friend, Stranger, GenerNum;
	}

	private void showBottomLayout(ContactType type) {
		remark.setVisibility(View.GONE);
		breakFriends.setVisibility(View.GONE);
		sendMsg.setVisibility(View.GONE);
		makeFriends.setVisibility(View.GONE);
		blacklist.setVisibility(View.VISIBLE);
		switch (type) {
			case MySelf:
			case GenerNum: {
				profile_layout_bottom.setVisibility(View.GONE);
				break;
			}
			case Friend: {
				profile_layout_bottom.setVisibility(View.VISIBLE);
				remark.setVisibility(View.VISIBLE);
				breakFriends.setVisibility(View.VISIBLE);
				break;
			}
			case Stranger: {
				profile_layout_bottom.setVisibility(View.VISIBLE);
				sendMsg.setVisibility(View.VISIBLE);
				makeFriends.setVisibility(View.VISIBLE);
				break;
			}
			default:
				break;
		}
		
	}
	/** 设置头像 */
	private void setPhoto() {
		String photo_big = contact.getPhoto_big();
		String photo_small = contact.getPhoto_small();
		List<VVImage> vvImages = new ArrayList<VVImage>();
		String[] strs_big = BBSUtils.splitPhotos(DBKey.photo_big, photo_big);
		String[] strs_small = BBSUtils.splitPhotos(DBKey.photo_small,
				photo_small);
		if (strs_small != null) {
			//可能出现大图为空的情况,如没有查看个人资料，大图信息没有下载
			if (strs_big != null && strs_big.length != strs_small.length) {
				//LogUtils.e("photo_big:" + photo_big + " photo_small:" + photo_small + " strs_big:"
				//						+ Arrays.asList(strs_big) + " 's length not equals " + Arrays.asList(strs_small));
			}
			for (int i = 0; i < strs_small.length; i++) {
				VVImage vvImg = new VVImage();
				vvImg.setPath(strs_big == null ? strs_small[i] : strs_big[i]);
				vvImg.setPathThumb(strs_small[i]);
				vvImages.add(vvImg);
			}
		}
		tempVvImages.clear();
		tempVvImages.addAll(vvImages);
		if (tempVvImages.size() == 0) {
			VVImage image = new VVImage();
			image.setPath("");
			image.setPathThumb("");
			tempVvImages.add(image);
		}
		mAlbumGridAdapter.setSex(contact.getSex());
		mAlbumGridAdapter.setItems(tempVvImages);
		mAlbumGridAdapter.notifyDataSetChanged();
	}
	/** 设置登录时间 */
	private void setLoginTime() {
		this.layout_loginTime_tv.setText(contact.getLoginTimeRlt());
	}
	/** 设置年龄及星座 */
	private void setAge() {
		layout_age_tv.setEnabled(contact.getSex().equals("0"));
		layout_age_tv.setText(BBSUtils.getAgeByBithday(contact.getBday()));
		layout_constellation_tv.setText(BBSUtils.getConstellation(contact
				.getBday()));
	}
	/** 设置距离 */
	private void setDistance() {
		layout_distance_tv.setText(LoginManager.getInstance().latlon2Distance(
				contact.getLat(), contact.getLon()));
	}
	/** 设置时光号 */
	private void setJID() {
		layout_timeid_tv.setText(contact.getJIDParsed());
	}
	/** 设置昵称 */
	private void setNikeName() {
		layout_nickName_tv
				.setText(BBSUtils.replaceBlank(contact.getNickName()));
	}
	/** 设置邮箱 */
	private void setEmail() {
		String email = contact.getEmail();
		if (TextUtils.isEmpty(email)
				&& !LoginManager.getInstance().isMyJid(contact.getJid())) {
			layout_email.setVisibility(View.GONE);
		} else {
			layout_email.setVisibility(View.VISIBLE);
			layout_email_tv.setText(email);
		}
	}
	/** 设置签名 */
	private void setSignature() {
		String signature = contact.getSignature();
		if (TextUtils.isEmpty(signature)
				&& !LoginManager.getInstance().isMyJid(contact.getJid())) {
			layout_signature.setVisibility(View.GONE);
		} else {
			layout_signature.setVisibility(View.VISIBLE);
			Log.i("yang", "签名:" + signature);
			layout_signature_tv.setText(signature);
		}
	}
	/** 设置手机号码 */
	private void setPhone() {
		if (LoginManager.getInstance().isMyJid(contact.getJid())) {
			layout_phone_tv.setText(contact.getPhoneNum());
		} else {
			layout_phone.setVisibility(View.GONE);
		}
	}
	/** 设置城市 */
	private void setCity() {
		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				String city = "";
				if (TextUtils.isEmpty(params[0])) {
					city = "广东省-深圳市";
				} else {
					city = BBSUtils.dbquery(params[0], getActivity(),
							DbidType.CITY);
				}
				return city;
			}
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				layout_city_tv.setText(result);
			}
		}.execute(contact.getCityId());
	}
	/** 设置学校 */
	private void setColleage() {
		new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				String school = "";
				if (TextUtils.isEmpty(params[0])) {
					school = "";
				} else {
					school = BBSUtils.dbquery(params[0], getActivity(),
							DbidType.COLLEAGE);
				}
				return school;
			}
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (TextUtils.isEmpty(result)
						&& !LoginManager.getInstance()
								.isMyJid(contact.getJid())) {
					layout_school.setVisibility(View.GONE);
				} else {
					layout_school.setVisibility(View.VISIBLE);
					layout_school_tv.setText(result);
				}
			}
		}.execute(contact.getSchoolId());
	}
	/** 设置专业 */
	private void setMajor() {
		if (TextUtils.isEmpty(contact.getMajor())
				&& !LoginManager.getInstance().isMyJid(contact.getJid())) {
			layout_major.setVisibility(View.GONE);
		} else {
			layout_major.setVisibility(View.VISIBLE);
			layout_major_tv.setText(contact.getMajor());
		}
	}
	/** 设置爱好 */
	private void setHobby() {
		if (TextUtils.isEmpty(contact.getHobby())
				&& !LoginManager.getInstance().isMyJid(contact.getJid())) {
			layout_hobby.setVisibility(View.GONE);
		} else {
			layout_hobby.setVisibility(View.VISIBLE);
			layout_hobby_tv.setText(contact.getHobby());
		}
	}
	/** 设置入学时间 */
	private void setEnrolTime() {
		if ((TextUtils.isEmpty(contact.getEnroltime()) || contact
				.getEnroltime().equals("0000-00-00"))) {
			if (LoginManager.getInstance().isMyJid(contact.getJid())) {
				layout_en_time.setVisibility(View.VISIBLE);
				layout_enroltime_tv.setText("");
			} else {
				layout_en_time.setVisibility(View.GONE);
			}
		} else {
			layout_en_time.setVisibility(View.VISIBLE);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sf1 = new SimpleDateFormat("yyyy");
			try {
				Date enroltime = sf.parse(contact.getEnroltime());
				layout_enroltime_tv.setText(sf1.format(enroltime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public boolean onBackPressed() {
		FragmentManager frgmtManager = getActivity()
				.getSupportFragmentManager();
		FragmentTransaction frgmtTransaction = frgmtManager.beginTransaction();
		frgmtTransaction.remove(this);
		getActivity().finish();
		return true;
	}
	@Override
	public void onClick(View v) {
		while (activity.getParent() != null) {
			activity = activity.getParent();
		}
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				activity, R.style.blurdialog);
		if (v == remark) {
			EditNameDialogView editNameView = new EditNameDialogView(activity);
			editNameView.setText(contact.getAlias());
			editNameView.setHint("请输入备注姓名");
			editNameView.setMaxlenth(10);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(activity);
			dilaogView.setTitle("备注:");
			dilaogView.setContentView(editNameView.getView());
			dilaogView.setMargin();
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					final String alias = ((EditText) contentView
							.findViewById(R.id.nickNameEdit)).getText()
							.toString();
					//					// 为空代表取消备注
					//if (!alias.isEmpty()) {
					blurDlg.dismiss();
					new VVBaseLoadingDlg<PacketResult>(new VVBaseLoadingDlgCfg(
							activity).setShowWaitingView(true).setBindXmpp(true)) {
						@Override
						protected PacketResult doInBackground() {
							return ContactService.getInstance()
									.modifyFriendAlias(contact.getJid(), alias);
						}
						@Override
						protected void onPostExecute(PacketResult result) {
							if (result.isOk()) {
								CToast.showToast(activity, "修改成功",
										Toast.LENGTH_SHORT);
								contact.setAlias(alias);
								EventBus.getDefault()
										.post(new EventBusData(
												EventAction.AliasChange, alias));
							} else {
								CToast.showToast(activity,
										"修改失败:" + result.getError(),
										Toast.LENGTH_SHORT);
							}
						}
					}.execute();
					//} else {
					//	CToast.showToast(activity, "不能为空!", Toast.LENGTH_SHORT);
					//}
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
		} else if (v == breakFriends) {
			if (!LoginManager.getInstance().isLogined()) {
				CToast.showToast(activity, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
				return ;
			}
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(activity);
			dilaogView.setTitle("删除好友:");
			dilaogView.setTextContent("确定删除好友,但对方仍将保留我的好友信息?");
			dilaogView.setPositiveButton("删除", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					new VVBaseLoadingDlg<PacketResult>(new VVBaseLoadingDlgCfg(
							activity).setShowWaitingView(true)) {
						@Override
						protected PacketResult doInBackground() {
							PacketResult result = ContactService.getInstance()
									.removeFriend(contact.getJid());
							return result;
						}
						@Override
						protected void onPostExecute(PacketResult result) {
							if (result.isOk()) {
								activity.finish();
								CToast.showToast(activity, "操作成功",
										Toast.LENGTH_SHORT);
							} else {
								CToast.showToast(activity,
										"操作失败:" + result.getError(),
										Toast.LENGTH_SHORT);
							}
						}
					}.execute();
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
		} else if (v == blacklist) {
			if (!LoginManager.getInstance().isLogined()) {
				CToast.showToast(activity, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
				return ;
			}
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(activity);
			Log.i(tag, "blacklist" + isBlackList);
			if (!isBlackList) {
				dilaogView.setTitle("拉黑:");
				dilaogView.setTextContent("是否将对方拉黑");
				dilaogView.setPositiveButton("拉黑", new BtnListener() {
					@Override
					public void ensure(View contentView) {
						blurDlg.dismiss();
						new VVBaseLoadingDlg<PacketResult>(
								new VVBaseLoadingDlgCfg(activity)
										.setShowWaitingView(true)) {
							@Override
							protected PacketResult doInBackground() {
								return ContactService.getInstance()
										.addBlacklist(contact.getJid());
							}
							@Override
							protected void onPostExecute(PacketResult result) {
								if (result != null && result.isOk()) {
									CToast.showToast(activity, "操作成功",
											Toast.LENGTH_SHORT);
								} else if (result != null) {
									CToast.showToast(activity,
											"操作失败：" + result.getError(),
											Toast.LENGTH_SHORT);
								}
							}
						}.execute();
					}
				});
				dilaogView.setNegativeButton("取消", new BtnListener() {
					@Override
					public void ensure(View contentView) {
						blurDlg.dismiss();
					}
				});
			} else {
				dilaogView.setTitle("解除黑名单:");
				dilaogView.setTextContent("是否将将对方解除黑名单");
				dilaogView.setPositiveButton("解除黑名单", new BtnListener() {
					@Override
					public void ensure(View contentView) {
						blurDlg.dismiss();
						new VVBaseLoadingDlg<PacketResult>(
								new VVBaseLoadingDlgCfg(activity)
										.setShowWaitingView(true)) {
							@Override
							protected PacketResult doInBackground() {
								return ContactService.getInstance()
										.removeBlacklist(contact.getJid());
							}
							@Override
							protected void onPostExecute(PacketResult result) {
								if (result != null && result.isOk()) {
									// 删除黑名单
									CToast.showToast(activity, "操作成功",
											Toast.LENGTH_SHORT);
								} else if (result != null) {
									CToast.showToast(activity,
											"操作失败：" + result.getError(),
											Toast.LENGTH_SHORT);
								}
							}
						}.execute();
					}
				});
				dilaogView.setNegativeButton("取消", new BtnListener() {
					@Override
					public void ensure(View contentView) {
						blurDlg.dismiss();
					}
				});
			}
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		} else if (v == sendMsg) {
			if (!LoginManager.getInstance().isLogined()) {
				CToast.showToast(activity, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
				return ;
			}
			ChatActivity.launch(activity, contact);
		} else if (v == makeFriends) {
			if (!LoginManager.getInstance().isLogined()) {
				CToast.showToast(activity, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
				return ;
			}
			ContactServiceDlg.showAddContactDlg(activity, contact, v);
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		showInfo();
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
		switch (data.getAction()) {
			case ModifyContactInfo: {
				showInfo();
				break;
			}
			case SendBroadCastDel: {
				mAlbumGridAdapter.removeItem((Integer) data.getMsg());
				String photoBig = BBSUtils.makePhotos(DBKey.photo_big,
						mAlbumGridAdapter.getItems());
				String photoSmall = BBSUtils.makePhotos(DBKey.photo_small,
						mAlbumGridAdapter.getItems());
				UserInfoPacket modifyPacket = new UserInfoPacket();
				modifyPacket.setField(UserInfoKey.big, photoBig);
				modifyPacket.setField(UserInfoKey.small, photoSmall);
				mAlbumGridAdapter.notifyDataSetChanged();
				modifyContactInfo(modifyPacket);
				break;
			}
			case RemoveBlacklist:
			case AddBlacklist: {
				isBlackList = data.getAction() == EventAction.AddBlacklist;
				tv_black.setText(isBlackList ? "解除黑名单" : "拉黑");
				break;
			}
		}
	}
	public void modifyContactInfo(final UserInfoPacket modifyPacket) {
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
					CToast.showToast(
							ContactInfoCheckFragment.this.getActivity(),
							"修改资料成功", Toast.LENGTH_SHORT);
				} else {
					CToast.showToast(
							ContactInfoCheckFragment.this.getActivity(),
							"修改资料失败", Toast.LENGTH_SHORT);
					EventBus.getDefault().post(
							new EventBusData(EventAction.ModifyContactInfo,
									null));
				}
			}
		}.execute();
	}
}
