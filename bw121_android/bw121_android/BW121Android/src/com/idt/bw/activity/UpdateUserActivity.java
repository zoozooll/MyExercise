package com.idt.bw.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.idt.bw.bean.User;
import com.idt.bw.database.OperatingTable;
import com.idt.bw.utils.DateManager;

public class UpdateUserActivity extends Activity implements OnClickListener,OnValueChangeListener,OnScrollListener,Formatter{
	private TextView createUserDone;
	
	private ImageView userPicture,usergender;
	private EditText username,userheight;
	private TextView userbirth,height_unit,weight_unit,pregnancy_weeks_days,userheightunit;
	private NumberPicker userweeksPicker,userdaysPicker;
	private Switch userPregnancy;
	private DatePickerDialog birthDatePicker;
	private PopupWindow photoPop,heightUnitPop,weightUnitPop;
	
	//private TextView unit_cm,unit_ft,unit_kg,unit_lb;
	private Calendar calendar ;
	private int age;
	private int curYear,curMonth,curDay;
	
	private ViewGroup myChooseView = null;
	private ViewGroup myHeightView = null;
	private ViewGroup myWeightView = null;
	private LayoutInflater inflater;
	private String path;
	private String photoName;
	private Bitmap roundBm;
	private static final int CASE_CAMERA = 0;
	private static final int RESULT_LOAD_IMAGE = 1;
	private final static int PHOTO_ZOOM = 3;
	private final static int TAKE_PHOTO = 4;
	private final static int PHOTO_RESULT = 5;
	private static final String IMAGE_UNSPECIFIED = "image/*";
	
	private int chooseHeight = 0;
	private int chooseWeight = 0;
	private int count;
	private RelativeLayout create_gender_isPregnancy,weeksdays;
	private RelativeLayout create_height_unit,create_weight_unit;
	private ImageView lineweeksdays;
	
	private OperatingTable operateTable;
	private User user;
	private java.text.DateFormat showingDateFormat;
	private String weeksStr;
	private String daysStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user);
		
		Intent ii = getIntent();
		Bundle bundle = ii.getExtras();
		user = (User) bundle.getSerializable("user");
		init();
	}
	
	public void init(){
		showingDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG);
		operateTable = OperatingTable.instance(this);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		createUserDone = (TextView) findViewById(R.id.createUserDone);
		userPicture = (ImageView) findViewById(R.id.profile_pic);
		username = (EditText) findViewById(R.id.create_name_edit);
		userbirth = (TextView) findViewById(R.id.create_birthday_edit);
		usergender = (ImageView) findViewById(R.id.create_gender_choose);
		userheight = (EditText) findViewById(R.id.create_height_edit);
		userheightunit = (TextView) findViewById(R.id.create_height_text);
		userPregnancy = (Switch) findViewById(R.id.isPregnancy_choose);
		userweeksPicker = (NumberPicker) findViewById(R.id.isPregnancy_weeks_picker);
		userdaysPicker = (NumberPicker) findViewById(R.id.isPregnancy_days_picker);
		height_unit = (TextView) findViewById(R.id.create_height_pop);
		weight_unit = (TextView) findViewById(R.id.create_weight_pop);
		create_height_unit = (RelativeLayout) findViewById(R.id.create_height_unit);
		create_weight_unit = (RelativeLayout) findViewById(R.id.create_weight_unit);
		pregnancy_weeks_days = (TextView) findViewById(R.id.pregnancy_weeks_days);
		
		create_gender_isPregnancy = (RelativeLayout) findViewById(R.id.create_gender_isPregnancy);
		lineweeksdays = (ImageView) findViewById(R.id.lineweeksdays);
		weeksdays = (RelativeLayout) findViewById(R.id.weeksdays);
		
		calendar = Calendar.getInstance();
		curYear = calendar.get(Calendar.YEAR); 
		curMonth = calendar.get(Calendar.MONTH); 
		curDay = calendar.get(Calendar.DAY_OF_MONTH);
		age = catorgeryYear(userbirth.getText().toString());
		//userbirth.setText(curYear + "-" + (curMonth+1) + "-" + curDay);
		
		
		
		count = Integer.parseInt(user.getUserGender());
		
		String path = user.getUserPhoto();
		if(TextUtils.isEmpty(path) || !new File(path).exists()){
			userPicture.setImageResource(R.drawable.profile_picture);
		}else{
			Bitmap bm = BitmapFactory.decodeFile(path);
			Bitmap roundBm = getRoundedCornerBitmap(bm,false);
			userPicture.setImageBitmap(roundBm);
		}
		
		/*Calendar c = Calendar.getInstance();
		SimpleDateFormat dfformat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = dfformat.parse(user.getUserBirth());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.setTime(d);
		userbirth.setText(DateFormat.format("d MMM yyyy",c));*/
		
		username.setText(user.getUserName());
		//userbirth.setText(user.getUserBirth());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = format.parse(user.getUserBirth());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userbirth.setText(showingDateFormat.format(d));
		
		if("1".equals(user.getUserGender())){
			usergender.setImageResource(R.drawable.gender_female);
			if("2".equals(user.getUserCategory())){
				userPregnancy.setChecked(true);
				lineweeksdays.setVisibility(View.VISIBLE);
				weeksdays.setVisibility(View.VISIBLE);
				
				pregnancy_weeks_days.setText(user.getUserPregnancyWeeks()+ getResources().getString(R.string.pregnancy_weeks) +
						" , "+user.getUserPregnancyDays()+getResources().getString(R.string.pregnancy_days));
				//Log.e("cdf",user.getUserPregnancyWeeks()+".........................."+user.getUserPregnancyDays());
			}else{
				userPregnancy.setChecked(false);
				lineweeksdays.setVisibility(View.GONE);
				weeksdays.setVisibility(View.GONE);
			}
		}else{
			usergender.setImageResource(R.drawable.gender_male);
		}
		if("kg".equals(user.getUserWeightUnit())){
			chooseWeight = 0;
		}else{
			chooseWeight = 1;
		}
		if("cm".equals(user.getUserHeightUnit())){
			userheight.setText(user.getUserHeight());
			userheightunit.setText("cm");
			chooseHeight = 0;
		}else{
			DecimalFormat df = new DecimalFormat("#0.0");
			userheight.setText(df.format((Double.parseDouble(user.getUserHeight())/30.48)));
			userheightunit.setText("ft");
			chooseHeight = 1;
		}
		weight_unit.setText(user.getUserWeightUnit());
		height_unit.setText(user.getUserHeightUnit());
		
		
		pregnancy_weeks_days.setOnClickListener(this);
		userPicture.setOnClickListener(this);
		userbirth.setOnClickListener(this);
		usergender.setOnClickListener(this);
		//Log.e("cdf","Update preg is click before age : "+age);
		userPregnancy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				age = catorgeryYear(userbirth.getText().toString());
				//Log.e("cdf","Update== preg is click before age : "+age);
				// TODO Auto-generated method stub
				if(isChecked){
					if(!(16<age && age<50)){
						//user.setUserCategory("2");//yes 
						userPregnancy.setChecked(false);
						lineweeksdays.setVisibility(View.GONE);
						weeksdays.setVisibility(View.GONE);
					}else{
						userPregnancy.setChecked(true);
						lineweeksdays.setVisibility(View.VISIBLE);
						weeksdays.setVisibility(View.VISIBLE);
					}
				}else{
					lineweeksdays.setVisibility(View.GONE);
					weeksdays.setVisibility(View.GONE);
				}
			}
		});
		
		createUserDone.setOnClickListener(this);
		//height_unit.setOnClickListener(this);
		//weight_unit.setOnClickListener(this);
		create_height_unit.setOnClickListener(this);
		create_weight_unit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.profile_pic:
			if (myChooseView == null) {
				myChooseView = (ViewGroup) inflater.inflate(R.layout.choosepic, null, true);
				photoPop =new PopupWindow(myChooseView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				TextView chooseFromPhoto = (TextView) myChooseView.findViewById(R.id.chooseFromPhoto);
				TextView chooseFromgallery = (TextView) myChooseView.findViewById(R.id.chooseFromgallery);
//				photoPop.setContentView(myChooseView);
				photoPop.setBackgroundDrawable(new BitmapDrawable());
				photoPop.setOutsideTouchable(true);
				chooseFromPhoto.setOnClickListener(this);
				chooseFromgallery.setOnClickListener(this);
			}
//			photoPop.update();
			photoPop.showAsDropDown(v, v.getWidth()/2, 0);
			break;
		case R.id.chooseFromPhoto:
			myChooseView.destroyDrawingCache();
			photoPop.dismiss();
			//myChooseView.setVisibility(View.GONE);
			Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   
			startActivityForResult(intent1, CASE_CAMERA);
			break;
		case R.id.chooseFromgallery:
			myChooseView.destroyDrawingCache();
			photoPop.dismiss();
			//myChooseView.setVisibility(View.GONE);
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(IMAGE_UNSPECIFIED);
            Intent wrapperIntent=Intent.createChooser(intent, null);
            startActivityForResult(wrapperIntent, PHOTO_ZOOM);
			break;
		case R.id.pregnancy_weeks_days:
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			//View view = findViewById(R.layout.pregnancy_choose);
			View view = inflater.inflate(R.layout.pregnancy_choose,null);
			
			userweeksPicker = (NumberPicker)view.findViewById(R.id.pregnancy_weeks_picker);
			userdaysPicker = (NumberPicker)view.findViewById(R.id.pregnancy_days_picker);
			
			//userweeksPicker.setFormatter(this);
			userweeksPicker.setOnValueChangedListener(this);
			userweeksPicker.setOnScrollListener(this);
			userweeksPicker.setMaxValue(42);
			userweeksPicker.setMinValue(0);
			//userweeksPicker.setValue(32);
			
			//userdaysPicker.setFormatter(this);
			userdaysPicker.setOnValueChangedListener(this);
			userdaysPicker.setOnScrollListener(this);
			userdaysPicker.setMaxValue(6);
			userdaysPicker.setMinValue(0);
			//userdaysPicker.setValue(3);
			userweeksPicker.setValue(Integer.parseInt(user.getUserPregnancyWeeks()));
			userdaysPicker.setValue(Integer.parseInt(user.getUserPregnancyDays()));
			
			builder.setTitle(getResources().getString(R.string.choose_sure_cancel)).setView(view);
						
			builder.setPositiveButton(getResources().getString(R.string.choose_sure), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					weeksStr = ""+userweeksPicker.getValue();
					daysStr = userdaysPicker.getValue()+" ";
					pregnancy_weeks_days.setText(weeksStr+ getResources().getString(R.string.pregnancy_weeks) +
							" , "+daysStr+getResources().getString(R.string.pregnancy_days));
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.choose_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Toast.makeText(SettingsActivity.this, "��ѡ����ȡ��ť��", Toast.LENGTH_SHORT).show();
				}
			});
			builder.show();
			break;
		case R.id.create_birthday_edit:
			DatePickerDialog.OnDateSetListener dateListener =  
				     new DatePickerDialog.OnDateSetListener() { 
						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							/*if(catorgeryYear(year + "-" + (monthOfYear+1) + "-" + dayOfMonth)==-1){
								userbirth.setText(0 + "-" + 0 + "-" + 0);
								Toast.makeText(CreateUserActivity.this, R.string.birthToast, 1).show();
								//userbirth.setText("Birthday can not biger than today");
							}else{*/
								//userbirth.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
								Calendar c = Calendar.getInstance();
								SimpleDateFormat dfformat = new SimpleDateFormat("yyyy-MM-dd");
								Date d = null;
								try {
									d = dfformat.parse(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								c.setTime(d);
								userbirth.setText(showingDateFormat.format(c.getTime()));
								age = catorgeryYear(userbirth.getText().toString());
								if(16<age && age<50 && count == 1){
									create_gender_isPregnancy.setVisibility(View.VISIBLE);
								}else{
									create_gender_isPregnancy.setVisibility(View.GONE);
								}
							//}
						} 
				     };
		    Date d2 = null;
			try {
				d2 = showingDateFormat.parse(userbirth.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Calendar c2 = Calendar.getInstance(); 
			c2.setTime(d2);
			birthDatePicker = new DatePickerDialog(this,dateListener,c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.DAY_OF_MONTH)); 
			birthDatePicker.show();
			break;
		
		case R.id.create_gender_choose:
			if(count%2 == 1){
				usergender.setImageResource(R.drawable.gender_male);
				count = 0;
				create_gender_isPregnancy.setVisibility(View.GONE);
				lineweeksdays.setVisibility(View.GONE);
				weeksdays.setVisibility(View.GONE);
				userPregnancy.setChecked(false);
			}else{
				usergender.setImageResource(R.drawable.gender_female);
				count = 1;
				if(16<age && age<50){
					create_gender_isPregnancy.setVisibility(View.VISIBLE);
				}
			}
			//Log.e("cdf","count == "+count);
			break;
		case R.id.create_height_unit:
			//Log.e("cdf","+++++++++++++++++++");
			Dialog dialog=new AlertDialog.Builder(UpdateUserActivity.this)
			.setTitle(getResources().getString(R.string.choose_height_unit))
			.setPositiveButton(getResources().getString(R.string.choose_sure), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//CreateUserActivity.this.mysel.setText("您的选择是"+MainActivity.this.getResources().getStringArray(R.array.company)[chNum]);
				}
			})
			.setNegativeButton(getResources().getString(R.string.choose_cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.setSingleChoiceItems(getResources().getStringArray(R.array.height_unit), chooseHeight, new DialogInterface.OnClickListener() {
				//上边第一个表示的是公司数组列表，第二个参数表示默认选择的，第三个监听器
				public void onClick(DialogInterface dialog, int which) {
					chooseHeight=which;
					height_unit.setText(UpdateUserActivity.this.getResources().getStringArray(R.array.height_unit)[which]);
				}
			})
			.create();
			dialog.show();

			break;
		case R.id.create_weight_unit:
			//Log.e("cdf","+++++++++++++++++++-------------------");
			
			Dialog dialogweight=new AlertDialog.Builder(UpdateUserActivity.this)
			.setTitle(getResources().getString(R.string.choose_weight_unit))
			.setPositiveButton(getResources().getString(R.string.choose_sure), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//CreateUserActivity.this.mysel.setText("您的选择是"+MainActivity.this.getResources().getStringArray(R.array.company)[chNum]);
				}
			})
			.setNegativeButton(getResources().getString(R.string.choose_cancel), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.setSingleChoiceItems(getResources().getStringArray(R.array.weight_unit), chooseWeight, new DialogInterface.OnClickListener() {
				//上边第一个表示的是公司数组列表，第二个参数表示默认选择的，第三个监听器
				public void onClick(DialogInterface dialog, int which) {
					chooseWeight=which;
					weight_unit.setText(UpdateUserActivity.this.getResources().getStringArray(R.array.weight_unit)[which]);
				}
			})
			.create();
			dialogweight.show();
			break;
		case R.id.createUserDone:
			age = catorgeryYear(userbirth.getText().toString());
			if("".equals(username.getText().toString()) || username.getText().toString() == null ){
				Toast.makeText(this, R.string.nameisnull, Toast.LENGTH_LONG).show();
			}else if(age < 0){
				Toast.makeText(this, R.string.birthisbig, Toast.LENGTH_LONG).show();
			}else{
				setToUser();
				Intent i = new Intent(UpdateUserActivity.this,MainActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("user", user);
				i.putExtras(b);
				startActivity(i);
				finish();
			}
			break;
		default:
			break;
		}
	}
	
	public void setToUser(){
//		user = new User();
		
		if(!(photoName == null)){
			user.setUserPhoto(Environment.getExternalStorageDirectory()+"/data/bw121/"+photoName);
		}
		user.setUserName(username.getText().toString());
		//user.setUserBirth(userbirth.getText().toString());
		Date d = null;
		try {
			d = showingDateFormat.parse(userbirth.getText().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUserBirth(DateFormat.format("yyyy-MM-dd", d).toString());
		
		age = catorgeryYear(userbirth.getText().toString());
		if(count == 1){
			user.setUserGender("1");
			//1=normal 2=pregnancy 3=kids 4=baby with adult
			if(0<=age && age<=1){
				user.setUserCategory("4");
				user.setUserPregnancyWeeks("");
				user.setUserPregnancyDays("");
			}else if(1<age && age<=10){
				user.setUserCategory("3");
				user.setUserPregnancyWeeks("");
				user.setUserPregnancyDays("");
			}else if(10<age && age<120){
				user.setUserCategory("1");
				user.setUserPregnancyWeeks("");
				user.setUserPregnancyDays("");
				if(userPregnancy.isChecked() && 16<age && age<50){
					user.setUserCategory("2");//yes 
					user.setUserPregnancyWeeks(weeksStr);
					user.setUserPregnancyDays(daysStr);
				}
			}
		}else{
			user.setUserGender("0");

			if(0<=age && age<=1){
				user.setUserCategory("4");
				user.setUserPregnancyWeeks("");
				user.setUserPregnancyDays("");
			}else if(1<age && age<=10){
				user.setUserCategory("3");
				user.setUserPregnancyWeeks("");
				user.setUserPregnancyDays("");
			}else if(10<age && age<120){
				user.setUserCategory("1");
				user.setUserPregnancyWeeks("");
				user.setUserPregnancyDays("");
			}
		}
		float height = Float.parseFloat(userheight.getText().toString());
		if("cm".equals(user.getUserHeightUnit())){
			userheightunit.setText("cm");
			if(height>300||height==300){
				height = 299;
				user.setUserHeight(height+"");
				userheight.setText(height+"");
			}else if(height<0||height == 0){
				height = 1;
				user.setUserHeight(height+"");
				userheight.setText(height+"");
			}
			chooseHeight = 0;
		}else{
			DecimalFormat df = new DecimalFormat("#0.0");
			userheight.setText(df.format(height*30.48));
			if(height>9.8){
				height = (float) 9.8;
				userheight.setText(height+"");
				user.setUserHeight(df.format(height*30.48));
			}else if(height<0||height == 0){
				height = (float) 0.1;
				userheight.setText(height+"");
				user.setUserHeight(df.format(height*30.48));
			}
			userheightunit.setText("ft");
			chooseHeight = 1;
		}
		user.setUserWeightUnit(weight_unit.getText().toString());
		user.setUserHeightUnit(height_unit.getText().toString());
		operateTable.update(user);
		//Log.e("cdf","operateTable.update(user)======"+operateTable.update(user));
	}
	
	public int catorgeryYear(String birth){
		SimpleDateFormat dfformat = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat dfformat2 = new SimpleDateFormat("d MMM yyyy");
		java.text.DateFormat dfformat2 =  showingDateFormat;

		String curDate = curYear+"-"+(curMonth+1)+"-"+curDay;
		int betweenYears = 0;
		int betweenDays = 0;
		try{
			Date d1 = dfformat.parse(curDate); 
			Date d2 = dfformat2.parse(birth); 
			Calendar c1 = Calendar.getInstance(); 
			Calendar c2 = Calendar.getInstance(); 
			c1.setTime(d1); 
			c2.setTime(d2); 
			// 保证第二个时间一定大于第一个时间 
			/*if(c1.after(c2)){ 
				c1 = c2; 
				c2.setTime(d1); 
			} */
			betweenYears =c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR); 
			betweenDays = c1.get(Calendar.DAY_OF_YEAR)-c2.get(Calendar.DAY_OF_YEAR);
			//Log.e("cdf","year======"+betweenYears+":"+betweenDays);
			if(betweenDays < 0){
				betweenYears --;
			}
		}catch (Exception e){

		}
		return betweenYears;
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
		    if (requestCode ==  CASE_CAMERA) {
			        Bitmap bm = (Bitmap) data.getExtras().get("data");
			        path = Environment.getExternalStorageDirectory()+"/data/bw121/"+System.currentTimeMillis()+".png";
			        photoName = System.currentTimeMillis()+".png";
			        //Log.e("cdf","add bm  ==------------------ "+bm);
			        //Log.e("cdf","add path  ==------------------ "+path);
			        roundBm = getRoundedCornerBitmap(bm,false);
			        storeInSD(bm,photoName);
			        userPicture.setImageBitmap(roundBm);
		    }else if (requestCode == RESULT_LOAD_IMAGE && null != data) {
		        Uri selectedImage = data.getData();
		        String[] filePathColumn = { MediaStore.Images.Media.DATA };
		        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
		        cursor.moveToFirst();
		        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		        String picturePath = cursor.getString(columnIndex);
		        //Log.e("cdf","picturePath--------------"+picturePath);
		        cursor.close();
		        Bitmap bm = BitmapFactory.decodeFile(picturePath);
		        userPicture.setImageBitmap(bm);
		    }else if (requestCode == PHOTO_ZOOM) {
	            photoZoom(data.getData());
	        }else if (requestCode == PHOTO_RESULT) {
	            Bundle extras = data.getExtras();
	            if (extras != null) {
	                Bitmap photoBm = extras.getParcelable("data");
	                //Log.e("cdf"," choose from gallery-->"+photoBm);
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                photoBm.compress(Bitmap.CompressFormat.JPEG, 60, stream);
	                photoName = System.currentTimeMillis()+".png";
			        storeInSD(photoBm,photoName);
			        roundBm = getRoundedCornerBitmap(photoBm,false);
			        userPicture.setImageBitmap(roundBm);
	            }
	        }
		}
	}
	
	// 图片缩放
	public void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean recyclePrevious){
		int side = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap outBitmap = Bitmap.createBitmap(side, side, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        final Paint paint = new Paint();
        final Rect square = new Rect(0,0,side,side);
        final RectF rectF = new RectF(square);
        final float roundPX = side/2;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, square, paint);
        if (recyclePrevious) {
        	bitmap.recycle();
        }
        return outBitmap;
    }
	
	private void storeInSD(Bitmap bitmap, String path) {
        File file = new File(Environment.getExternalStorageDirectory()+"/data/bw121/");
        if (!file.exists()) {
            file.mkdirs();
        }
        File imageFile = new File(file,path);
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(CompressFormat.PNG, 50, fos);
            //Log.e("cdf","why----????-------------");
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (photoPop != null && photoPop.isShowing()) {
			photoPop.dismiss();
			myChooseView.destroyDrawingCache();
			//photoPop = null;
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		boolean flag = false;
		if (photoPop != null && photoPop.isShowing()) {
			photoPop.dismiss();
			myChooseView.destroyDrawingCache();
			//photoPop = null;
			flag = true;
		}
		if (!flag) {
			super.onBackPressed();
		}
	}

	public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

	@Override
	public void onScrollStateChange(NumberPicker view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub
		
	}

}
