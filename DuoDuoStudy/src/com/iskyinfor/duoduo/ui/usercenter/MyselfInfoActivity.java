package com.iskyinfor.duoduo.ui.usercenter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.pojo.User;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UIPublicConstant;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.book.BookShelfActivity;
import com.iskyinfor.duoduo.ui.lesson.ShareSpinnerWidget;
import com.iskyinfor.duoduo.ui.shop.ImageTask;

public class MyselfInfoActivity extends Activity implements OnClickListener {

	private TextView userNameText, userIdText = null;
	private Button baseRes, updatePswd, accountMsg, backShelf;
	private EditText schoolEdit, classEdit, phoneEdit, emailEdit, qqEdit,languageEdit;
	private ImageView personImage, maleIamge, femaleImage, backIndex;
	private ListView publicList; // 弹出Popupwindow是公用的ListView
	private PopupWindow popupWindow = null;
	private ShareSpinnerWidget yearImage, monthImage, dayImage,
			provincespinner, citySpinner, countrySpinner;

	private User user = null;
	private String userid, uname, sex, brithday, school, strClass, cellPhone,
			email, qq, provience, city,country, userImage, language;

	private Handler mHander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UIPublicConstant.USERCENTER_BASERES:
				user = (User) msg.obj;
				getUserData();
				break;
			default:
				break;
			}
		}

		/**
		 * 取到用户的相关信息
		 */
		private void getUserData() {
			// userid = user.getUserId();
			uname = user.getUserName();
			sex = user.getSex();
			brithday = user.getBirthday();
			school = user.getSchoollName();
			strClass = user.getClassName();
			cellPhone = user.getUserPhone();
			email = user.getUserEmail();
			qq = user.getUserEmail();
			provience = user.getProvinceName();
			city = user.getCityName();
			country = user.getArrealName();
			userImage = user.getUseImgPath();
			language = user.getMotto(); // 座右铭
			userImage = user.getUseImgPath(); //图像

			userNameText.setText(uname);
			schoolEdit.setText(school);
			classEdit.setText(strClass);
			phoneEdit.setText(cellPhone);
			emailEdit.setText(email);
			qqEdit.setText(qq);
			languageEdit.setText(language);
			provincespinner.setSpinnerData(provience, UIPublicConstant.INT_PROVINCE_CODE);
			citySpinner.setSpinnerData(city, UIPublicConstant.INT_CITY_CODE);
			countrySpinner.setSpinnerData(country, UIPublicConstant.INT_COUNTRY_CODE);
		
			//取到图像
			if (userImage != null || !"".equals(userImage)) {
				try {
					ImageTask imageTask = new ImageTask(personImage);
					imageTask.execute(CommArgs.PATH + userImage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myself_infomation);

		// 取到用户ID
		userid = this.getSharedPreferences(UIPublicConstant.UserInfo,
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE)
				.getString("account", "");

		// 取到数据
		new UserInfoTask(MyselfInfoActivity.this, userid,
				UIPublicConstant.QUERY_ALLUSER_MARK, mHander).execute();
		
		initWidget();
	}

	private void initWidget() {

		userIdText = (TextView) findViewById(R.id.user_id_text);
		userNameText = (TextView) findViewById(R.id.user_user_text);
		userIdText.setText(userid);

		// 用户的图像
		personImage = (ImageView) findViewById(R.id.myself_pictures);

		// 学校，班级，电话，邮箱，QQ，座右铭，隐私设置
		schoolEdit = (EditText) findViewById(R.id.school_name_edit);

		classEdit = (EditText) findViewById(R.id.class_name_edit);

		phoneEdit = (EditText) findViewById(R.id.phone_name_edit);

		emailEdit = (EditText) findViewById(R.id.email_name_edit);

		qqEdit = (EditText) findViewById(R.id.qq_name_edit);

		languageEdit = (EditText) findViewById(R.id.myself_language_edit);

		// 性别
		maleIamge = (ImageView) findViewById(R.id.radioMale_image);
		maleIamge.setOnClickListener(this);
		femaleImage = (ImageView) findViewById(R.id.radioFemale_image);
		femaleImage.setOnClickListener(this);

		// 用户资料
		baseRes = (Button) findViewById(R.id.myself_base_resource);
		baseRes.setOnClickListener(this);
		updatePswd = (Button) findViewById(R.id.myself_updata_resource);
		updatePswd.setOnClickListener(this);
		accountMsg = (Button) findViewById(R.id.myself_account_resource);
		accountMsg.setOnClickListener(this);
		backShelf = (Button) findViewById(R.id.myself_title_btn); // 返回书架
		backShelf.setOnClickListener(this);
		backIndex = (ImageView) findViewById(R.id.myself_bottom_back_img); // 返回
		backIndex.setOnClickListener(this);

		// 生日
		yearImage = (ShareSpinnerWidget) findViewById(R.id.brithday_year_spinner);
		yearImage.setOnClickListener(this);
		yearImage.setSpinnerData("1988", UIPublicConstant.INT_YEAR_CODE);
		monthImage = (ShareSpinnerWidget) findViewById(R.id.brithday_month_spinner);
		monthImage.setOnClickListener(this);
		monthImage.setSpinnerData("05", UIPublicConstant.INT_MONTH_CODE);
		dayImage = (ShareSpinnerWidget) findViewById(R.id.brithday_day_spinner);
		dayImage.setOnClickListener(this);
		dayImage.setSpinnerData("23",UIPublicConstant.INT_DAY_CODE);
		
		// 地区
		provincespinner = (ShareSpinnerWidget) findViewById(R.id.place_province_spinner);
		provincespinner.setOnClickListener(this);
		citySpinner = (ShareSpinnerWidget) findViewById(R.id.place_city_spinner);
		citySpinner.setOnClickListener(this);
		countrySpinner = (ShareSpinnerWidget) findViewById(R.id.place_country_spinner);
		countrySpinner.setOnClickListener(this);
	}

	/**
	 * 判断性别
	 * 
	 * @param flag
	 */
	protected void isSelectMale(boolean flag) {
		if (flag == true) {
			maleIamge.setImageResource(R.drawable.sex_true);
			femaleImage.setImageResource(R.drawable.sex_false);
			flag = false;
		} else {
			femaleImage.setImageResource(R.drawable.sex_true);
			maleIamge.setImageResource(R.drawable.sex_false);
			flag = true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.brithday_year_spinner:
			// TODO 获取年份数据
			showData(UIPublicConstant.intYear,UiHelp.getAllyearData());
			break;
		case R.id.brithday_month_spinner:
			// TODO 获取月份数据
			showData(UIPublicConstant.intMonth,UiHelp.getAllMonthData());
			break;
		case R.id.brithday_day_spinner:
			// TODO 获取天数据
			showData(UIPublicConstant.intDay,UiHelp.getAllDayData());
			break;
		case R.id.radioMale_image:
			// TODO 性别 男
			isSelectMale(true);
			break;
		case R.id.radioFemale_image:
			isSelectMale(false);
			// TODO 性别 女
			break;
		case R.id.myself_bottom_back_img:
			// TODO 返回按钮
			MyselfInfoActivity.this.finish();
			break;
		case R.id.myself_base_resource:
			// TODO 基本资料界面
			if (getBaseContext() instanceof MyselfInfoActivity)
				return;

			break;

		case R.id.myself_updata_resource:
			// TODO 修改密码界面
			Intent intent2 = new Intent(MyselfInfoActivity.this,
					UpdataPasswordActivity.class);
			startActivity(intent2);
			break;

		case R.id.myself_account_resource:
			// TODO 账户信息界面
			Intent intent3 = new Intent(MyselfInfoActivity.this,
					MyselfAccountActivity.class);
			startActivity(intent3);
			break;

		case R.id.myself_title_btn:
			// TODO 返回书架按钮
			Intent intent = new Intent(MyselfInfoActivity.this,
					BookShelfActivity.class);
			startActivity(intent);
			break;

		case R.id.place_province_spinner:
			// TODO 获取省份的数据
		//	showData(UIPublicConstant.intCountry,UiHelp.getAllyearData());
			break;

		case R.id.place_city_spinner:
			// TODO 获取市的数据
		//	showData(UIPublicConstant.intCity,UiHelp.getAllyearData());
			break;

		case R.id.place_country_spinner:
			// TODO 获取县的数据
		//	showData(UIPublicConstant.intCountry,UiHelp.getAllyearData());
			break;

		default:
			break;
		}
	}

	// 显示popupwindow及其数据
	private void showData(int data,ArrayList<String> arrayList)
	{
		LayoutInflater factory = LayoutInflater.from(MyselfInfoActivity.this);
		View view = factory.inflate(R.layout.lesson_listview_popup, null);
		publicList = (ListView) view.findViewById(R.id.duoduo_lesson_selcet_listview);
		
		if(popupWindow==null)
		{
			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			popupWindow.setFocusable(false);
		}
		
		switch (data) {
		case UIPublicConstant.intYear:
			
			BirthdayAndCountryAdapter yearAdapter =
			new BirthdayAndCountryAdapter(MyselfInfoActivity.this,arrayList);
			publicList.setAdapter(yearAdapter);
			
//			publicList.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
//				{
//					popupWindow.dismiss();
//				}
//			});
			
			PopupShowMethod(yearImage,0,0);
			
			break;

		case UIPublicConstant.intMonth:
			
			BirthdayAndCountryAdapter monthAdapter = 
			new BirthdayAndCountryAdapter(MyselfInfoActivity.this, arrayList);
			publicList.setAdapter(monthAdapter);
			PopupShowMethod(monthImage,0,0);
			
			break;

		case UIPublicConstant.intDay:
			
			BirthdayAndCountryAdapter dayAdapter = 
			new BirthdayAndCountryAdapter(MyselfInfoActivity.this, arrayList);
			publicList.setAdapter(dayAdapter);
			PopupShowMethod(dayImage,0,0);
			
			break;

		case UIPublicConstant.intProvince:

			BirthdayAndCountryAdapter provinceAdapter = new BirthdayAndCountryAdapter(
					MyselfInfoActivity.this, arrayList);
			publicList.setAdapter(provinceAdapter);
			PopupShowMethod(dayImage,0,0);
			break;

		case UIPublicConstant.intCity:
			popupWindow.update();
			popupWindow.showAsDropDown(countrySpinner, 0, 0);
			break;

		case UIPublicConstant.intCountry:
			popupWindow.update();
			popupWindow.showAsDropDown(dayImage, 0, 0);
			break;
		}
	}

	/**
	 * 判断popupwindow是否显示
	 * 
	 */
	private void PopupShowMethod(View view,int x,int y) {
		
		Log.i("liu", "popupWindow===:"+popupWindow);
		
			if (popupWindow!=null&&popupWindow.isShowing()){
				popupWindow.dismiss();
			}
			else 
			{
				popupWindow.showAsDropDown(view,x, y);
			}
	}


}