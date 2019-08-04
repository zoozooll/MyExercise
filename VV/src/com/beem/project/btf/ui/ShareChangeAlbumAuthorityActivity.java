package com.beem.project.btf.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.CutomerlimitEditText;
import com.beem.project.btf.bbs.view.CutomerlimitEditText.OnChanageListener;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.ui.activity.MainpagerActivity;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.ui.views.DateTimePickerView;
import com.beem.project.btf.ui.views.DateTimePickerView.Viewcount;
import com.beem.project.btf.ui.views.DateTimePickerView.getDataListener;
import com.beem.project.btf.ui.views.ShareChangeAlbumAuthorityView.AlbumAuthority;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.butterfly.vv.adapter.ImageUploadGridViewAdapter;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.vv.utils.CToast;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

/**
 * 图片上传界面,所有图片上传20150323
 * @ClassName: ShareChangeAlbumAuthorityActivity
 * @Description: TODO
 * @author: zhenggen xie
 * @date: 2015年3月16日 下午2:02:30
 * @tip::防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan" 希望动态调整高度
 *                 android:windowSoftInputMode="adjustResize"
 */
public class ShareChangeAlbumAuthorityActivity extends VVBaseActivity implements
		OnClickListener, RadioGroup.OnCheckedChangeListener {
	private static final String TAG = ShareChangeAlbumAuthorityActivity.class
			.getSimpleName();
	private CustomTitleBtn ok, back;
	private TextView contacts_textView2, notify_d, notify_MY, text_remind_off;
	private CutomerlimitEditText albumSignEdit;
	private LinearLayout remind_wraper, upload_wraper, remind_time_wraper;;
	private GridView gridview;
	private ToggleButton remind_toggle;
	private BBSCustomerDialog blurDlg;
	private ImageFolderItem folderItem;
	private ScrollView list;
	/**
	 * 获取图片的地址
	 */
	private ArrayList<String> listimagePath;
	private RadioGroup selecRange;
	private Handler mHandler = new Handler();
	private EventAction uploadPhotoAction;
	private boolean showAddMore;
	private int UploadedNum = 0;
	private String[] datas = new String[3];

	public interface IntentKey {
		String SIGNATURE = "SIGNATURE";
		String CREATETIME = "CREATETIME";
		String ALBUMAUTHORITY = "ALBUMAUTHORITY";
		String JID = "JID";
		String ALBUMID = "ALBUMID";
		String LISTIMAGEPATH = "LISTIMAGEPATH";
		String FOLDERITEM = "folderItem";
	}

	// 跳转到上传界面
	public static void launch(Context ctx, ImageFolderItem folderItem,
			ArrayList<String> pics) {
		Intent intent = new Intent(ctx, ShareChangeAlbumAuthorityActivity.class);
		intent.putExtra(IntentKey.FOLDERITEM, folderItem);
		intent.putStringArrayListExtra(IntentKey.LISTIMAGEPATH, pics);
		ctx.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.timefly_remindactivity_layout);
		if (getIntent().hasExtra(IntentKey.FOLDERITEM)) {
			folderItem = getIntent().getParcelableExtra(IntentKey.FOLDERITEM);
		}
		listimagePath = getIntent().getStringArrayListExtra(
				IntentKey.LISTIMAGEPATH);
		if (listimagePath == null || listimagePath.isEmpty()) {
			listimagePath = new ArrayList<String>();
			Set<ImageInfoHolder> set = PhotosChooseManager.getInstance()
					.getChoosedSet();
			for (ImageInfoHolder info : set) {
				listimagePath.add(info.mImagePath);
			}
			showAddMore = true;
		}
		initViews();
		if (!TextUtils.isEmpty(LoginManager.getInstance().getJidCompleted())) {
			new WorkThread().start();
		} else {
			ActivityController.getInstance().gotoLogin();
		}
	}
	private void initViews() {
		// 导航条设置
		ok = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		ok.setText("确定").setImgVisibility(View.GONE).setViewPaddingRight()
				.setVisibility(View.VISIBLE);
		ok.setOnClickListener(this);
		back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		contacts_textView2 = (TextView) findViewById(R.id.topbar_title);// 标题
		//内容
		list = (ScrollView) findViewById(R.id.list);// scrollview
		selecRange = (RadioGroup) findViewById(R.id.selecRange);
		albumSignEdit = (CutomerlimitEditText) findViewById(R.id.albumSignEdit);
		remind_wraper = (LinearLayout) findViewById(R.id.remind_wraper);
		remind_time_wraper = (LinearLayout) findViewById(R.id.remind_time_wraper);
		notify_d = (TextView) findViewById(R.id.text_day);
		notify_MY = (TextView) findViewById(R.id.text_monthandyear);
		text_remind_off = (TextView) findViewById(R.id.text_remind_off);
		remind_toggle = (ToggleButton) findViewById(R.id.remind_toggle);
		upload_wraper = (LinearLayout) findViewById(R.id.upload_wraper);
		gridview = (GridView) findViewById(R.id.gridview);
		// 初始化内容控件
		selecRange.setOnCheckedChangeListener(this);
		albumSignEdit.setOnChangeListener(new OnChanageListener() {
			@Override
			public void onChange(String editTextStr) {
				if (folderItem != null) {
					folderItem.getImageFolder().setSignature(editTextStr);
				}
			}
		});
		/** 添加时间滚动选择start */
		Calendar calendar = Calendar.getInstance();
		int Syear = calendar.get(Calendar.YEAR);
		DateTimePickerView selectTime = new DateTimePickerView(mContext, Syear,
				2050, Viewcount.three, 5);
		selectTime.setShadowColor(0xFFF9FEFF, 0x99F9FEFF, 0x00F9FEFF);
		selectTime.setDataListener(new getDataListener() {
			@Override
			public void setdata(int[] data) {
				notify_d.setText(data[2] + "");
				notify_MY.setText(data[1] + "/" + data[0]);
				for (int i = 0; i < datas.length; i++) {
					datas[i] = String.valueOf(data[i]);
				}
				String notify_time = new StringBuffer()
						.append(data[0])
						.append("-")
						.append(BBSUtils.padStrBefore(String.valueOf(data[1]),
								'0', 2))
						.append("-")
						.append(BBSUtils.padStrBefore(String.valueOf(data[2]),
								'0', 2)).append(" 00:00:00").toString();
				if (folderItem != null) {
					folderItem.getImageFolder().setField(DBKey.notify_time,
							notify_time);
				}
			}
		});
		String[] date = new String[] {
				notify_MY.getText().toString().split("/")[0],
				notify_MY.getText().toString().split("/")[1],
				notify_d.getText().toString() };
		selectTime.setcurrentData(date);
		View selectTimeview = selectTime.getmView();
		selectTimeview.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		remind_time_wraper.addView(selectTimeview);
		/** 添加时间滚动选择end */
		remind_time_wraper.setVisibility(View.GONE);
		remind_wraper.setOnClickListener(this);
		// 监听开关
		remind_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					text_remind_off.setText("已开启");
					text_remind_off.setTextColor(mContext.getResources()
							.getColor(R.color.tab_blue));
					remind_time_wraper.setVisibility(View.VISIBLE);
					folderItem.getImageFolder().setField(DBKey.notify_valid,
							Valid.open.val);
				} else {
					text_remind_off.setText("未开启");
					text_remind_off.setTextColor(mContext.getResources()
							.getColor(R.color.bg_brown));
					remind_time_wraper.setVisibility(View.GONE);
					folderItem.getImageFolder().setField(DBKey.notify_valid,
							Valid.close.val);
				}
			}
		});
		upload_wraper.setVisibility(View.VISIBLE);
		final ImageUploadGridViewAdapter adapter = new ImageUploadGridViewAdapter(
				mContext, listimagePath, showAddMore);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (showAddMore && position == adapter.getCount() - 1) {
					// 添加现有图片集合到历史图片集合
					// Intent intent = new Intent(ShareChangeAlbumAuthorityActivity.this,
					// CameraActivity.class);
					// startActivity(intent);
					// 应该是跳回之前已经选择的界面，然后默认选中了已有的选项。而不应该重新再打开一个页面。
					finish();
				}
			}
		});
	}
	/**
	 * 有关folderItem信息的放在线程之后处理；
	 */
	private void onPostExecute() {
		albumSignEdit.setText(folderItem.getImageFolder().getSignature());
		int authorityId = R.id.All;
		if (folderItem.getImageFolder().getAuthority()
				.equals(AlbumAuthority.all)) {
			authorityId = R.id.All;
		} else if (folderItem.getImageFolder().getAuthority()
				.equals(AlbumAuthority.friend)) {
			authorityId = R.id.friends;
		} else if (folderItem.getImageFolder().getAuthority()
				.equals(AlbumAuthority.privateMe)) {
			authorityId = R.id.privateMe;
		}
		selecRange.check(authorityId);
		UploadedNum = folderItem.getImageFolder().getPhotoCount();
		setNotifyDate(folderItem.getImageFolder().getNotify_time(), folderItem
				.getImageFolder().getNotify_valid());
		contacts_textView2
				.setText("你还可以上传"
						+ (Constants.uploadpicMaxNum - listimagePath.size() - UploadedNum)
						+ "张");
	}
	@Override
	public void onClick(View v) {
		if (ok == v) {
			// 发送广播给上传图片界面，更新上传图片列表
			if (!LoginManager.getInstance().isLogined()) {
				CToast.showToast(mContext, R.string.timefly_unlogin,
						Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
			} else {
				if (listimagePath == null || listimagePath.size() <= 0) {
					CToast.showToast(mContext, "未选中图片，请重试！", Toast.LENGTH_SHORT);
				} else if (listimagePath.size() > Constants.uploadpicMaxNum
						- UploadedNum) {
					CToast.showToast(mContext,
							R.string.showimage_uploadCountFull,
							Toast.LENGTH_SHORT);
				} else {
					String createTime = folderItem.getImageFolder()
							.getCreateTime();
					if (createTime == null || createTime.length() <= 0) {
						Date nowDate = LoginManager.getInstance()
								.getSysytemTimeDate();
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						String dateLike = sf.format(nowDate);
						folderItem.getImageFolder().setField(DBKey.createTime,
								dateLike);
					}
					folderItem.getImageFolder().setField(DBKey.jid,
							LoginManager.getInstance().getJidParsed());
					folderItem.getImageFolder().setField(DBKey.photoCount,
							UploadedNum + listimagePath.size());
					Object[] obj = new Object[] { folderItem, remind_toggle,
							albumSignEdit.getText().toString(),
							folderItem.getImageFolder().getField(DBKey.authority).toString(), datas };
					EventBusData data = new EventBusData(uploadPhotoAction,
							folderItem, listimagePath);
					EventBus.getDefault().post(data);
					EventBus.getDefault().post(
							new EventBusData(EventAction.TimeflyAlert, obj));
				}
				Intent intent = new Intent(ShareChangeAlbumAuthorityActivity.this,
						MainpagerActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			ShareChangeAlbumAuthorityActivity.this.finish();
			PhotosChooseManager.getInstance().exitChooseMode();
		} else if (back == v) {
			//			PhotosChooseManager.getInstance().exitChooseMode();
			ShareChangeAlbumAuthorityActivity.this.finish();
		} else if (remind_wraper == v) {
			int visible = remind_time_wraper.getVisibility() == View.VISIBLE ? View.GONE
					: View.VISIBLE;
			remind_time_wraper.setVisibility(visible);
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.friends: {
				folderItem.getImageFolder().setField(DBKey.authority,
						AlbumAuthority.friend);
				break;
			}
			case R.id.All: {
				folderItem.getImageFolder().setField(DBKey.authority,
						AlbumAuthority.all);
				break;
			}
			case R.id.privateMe: {
				folderItem.getImageFolder().setField(DBKey.authority,
						AlbumAuthority.privateMe);
				break;
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (gridview != null) {
			gridview = null;
		}
	}

	/**
	 * @ClassName: WorkThread
	 * @Description: 工作线程
	 * @author: yuedong bao
	 * @date: 2015-5-28 上午10:58:29
	 */
	private class WorkThread extends Thread {
		private WorkThread() {
			super();
			//			ContactService.getInstance().synGeoInfo();
			setName("upload_timefly_photo_thread");
		}
		@Override
		public void run() {
			uploadPhotoAction = EventAction.UploadTimeflyPhotoAdd;
			if (folderItem == null) {
				try {
					folderItem = ImageFolderItemManager.getInstance()
							.getImageFolderItemNow(
									LoginManager.getInstance().getJidParsed());
				} catch (WSError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (folderItem == null) {
				// 今天还没有上传过图片
				uploadPhotoAction = EventAction.UploadTimeflyPhotoCreate;
				folderItem = new ImageFolderItem();
				ImageFolder folder = new ImageFolder();
				folder.setField(DBKey.signature, "");
				folder.setField(DBKey.authority, SharedPrefsUtil.getValue(
						mContext, SettingKey.album_auhtority,
						AlbumAuthority.all));
				folder.setField(DBKey.jid, LoginManager.getInstance()
						.getJidParsed());
				folder.setField(DBKey.notify_time, LoginManager
						.getInstance().getSystemTime());
				folder.setField(DBKey.notify_valid, Valid.close.val);
				folder.setField(DBKey.lat, BDLocator.getInstance().getLat());
				folder.setField(DBKey.lon, BDLocator.getInstance().getLon());
				folderItem.setContact(LoginManager.getInstance()
						.getMyContact());
				folderItem.setImageFolder(folder);
				folderItem.setVVImages(new ArrayList<VVImage>());
			}
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					onPostExecute();
				}
			});
		}
	}

	// 设置时光提醒UI
	private void setNotifyDate(String time, String valid) {
		boolean isOpen = Valid.open.val.equals(valid);
		remind_toggle.setChecked(isOpen);
		if (TextUtils.isEmpty(time)) {
			time = LoginManager.getInstance().getSystemTime();
		}
		int[] date = BBSUtils.parseDate(time);
		notify_d.setText(String.valueOf(date[2]));
		notify_MY.setText(String.valueOf(date[1] + "/" + date[0]));
	}
}
