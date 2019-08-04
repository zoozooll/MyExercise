package com.idt.bw.activity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.R.color;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idt.bw.activity.BWChartFragment.OnSelectItemChangedEvent;
import com.idt.bw.bean.Record;
import com.idt.bw.bean.ReferenceDate;
import com.idt.bw.bean.User;
import com.idt.bw.bean.UserSettings;
import com.idt.bw.ble.CommandManager;
import com.idt.bw.ble.CommandManager.BLECommandCallback;
import com.idt.bw.ble.WeightData;
import com.idt.bw.database.OperatingTable;
import com.idt.bw.utils.DateManager;
import com.idt.bw.view.BMILevelView;
import com.idt.bw.view.NavDrawerItem;
import com.shinobicontrols.charts.CandlestickSeries;
import com.shinobicontrols.charts.ChartFragment;
import com.shinobicontrols.charts.DataAdapter;
import com.shinobicontrols.charts.DateTimeAxis;
import com.shinobicontrols.charts.NumberAxis;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.charts.SimpleDataAdapter;

public class MainActivity extends Activity implements OnItemClickListener,OnClickListener {
	private ListView left_drawerlist;
	private List<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter drawerListAdapter;
	private TypedArray mNavMenuIconsTypeArray;
	private String[] mNavMenuTitles;
	private RelativeLayout menutopRelative;
	private ImageView menuImg;
	private DrawerLayout drawer_layout;
	private Calendar calendar ;
//	private int curYear,curMonth,curDay,curhour,curminute,cursecond;
	private User user;
	private RelativeLayout main_user,top_bar,main_weightvalue_before,main_weightvalue_after;
	private ImageView munu_button,main_user_pic,change_layout_icon,crease_image,step_one, step_one_people;
	private TextView main_user_name,/*main_get_weight,*/current_weight_value, current_weight_value_lbs, step_one_text;
	private TextView crease_value,input_data_date1;
	private Bitmap photoBitmap;
	private ImageView char_weight_arrow_land,char_height_arrow_land,char_bmi_arrow_land,char_back_icon,char_weight_land,char_height_land,char_bmi_land;
	private ImageView char_weight_arrow,char_height_arrow,char_bmi_arrow,char_weight,char_height,char_bmi;
	private TextView bmi_value,bmi_state;
	private RelativeLayout main_user_graph;
	private RelativeLayout main_user_graph_land;
	private LinearLayout main_user_pic_name;
	private ImageButton previousDay,nextDay;
	private TextView choose_weekly,choose_monthly,choose_yearly;
	
	private BMILevelView bmiLevelView; 
	
	private ViewPager viewPager;  
    private ArrayList<View> pageViews;  
    private ViewGroup main, group;  
    private ImageView imageView;  
    private ImageView[] imageViews; 
    private OperatingTable myOperatingTable;
    private CommandManager BLEcmd;
//    private String curDate ;
    private UserSettings UserSettingsRecord;
    private Date datenow,datestart;
    private long times,firstTime;
    private NotificationManager mNotificationManager;
    private BWChartFragment mTimeSeriesChart;
    private Date dt;
    private ArrayList<Record> mRecord;
    private ArrayList<User> mUser;
    private SimpleDateFormat mformat;
    private float targetWeight;
    // for purple' scaling;
    private float parentWeight;
    private int stepScale;
    private ArrayList<Record> recordsList = null;
    
    private String userBirth ;
    private String userGender;
    private Date mNow,inputNow,x0Time ;
    private double X0,X1;
    private long age;
    private DatePickerDialog myDatePicker;
    private Calendar myCalendar ;
    private String curDate;
	private int curYear,curMonth,curDay;
	private LayoutInflater inflater;
	private java.text.DateFormat showingDateFormat;
	
	
    //private int chartID =1; //默认weight=1;height=2;BMI=3
   // private long maxWeek,minWeek=0;//最早称重月份，最晚称重月份		12*10=120 or 12*19=288
   // public int chartType = 1;//默认day=1;按钮高亮   month=2;year=3
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Intent ii = getIntent();
		Bundle bundle = ii.getExtras();
		user = (User) bundle.getSerializable("user");
		
		/*Log.e("cdf","user info >>>>>>>>> "+user.getId()+" "+user.getUserBirth()+" "+user.getUserCategory()+" "+user.getUserGender()
				+" "+user.getUserHeight()+" "+user.getUserName()+" "+user.getUserPhoto()+" "+user.getUserPregnancyWeeks()
				+" "+user.getUserPregnancyDays()+" "+user);*/
		showingDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG);
		init();
		myOperatingTable = OperatingTable.instance(this);
		BLEcmd = new CommandManager(this);
		BLEcmd.setCallback(BLEcallback);
		
		/*if(user.getId() != null){
			recordsList = myOperatingTable.getAllRecords(Long.parseLong(user.getId()));
			//recordsList = myOperatingTable.getMonthsRecords(Long.parseLong(user.getId()));
			//recordsList = myOperatingTable.getYearsRecords(Long.parseLong(user.getId()));
			//recordsList = myOperatingTable.getDaysRecords(Long.parseLong(user.getId()));
			//Log.e("cdf","record size------"+recordsList.size());
			
			Record array_element;
			for (int i = 0; i < recordsList.size(); i++) {
				array_element = recordsList.get(i);
				Log.e("cdf","record id,weight ================ "+array_element.getId()+" , "+array_element.getWeight());
			}
		}else{
			Log.e("cdf","record id,weight  null---------------");
		}*/
		stepScale = 0;
		UserSettingsRecord = myOperatingTable.getUserSetting(Long.parseLong(user.getId()));
		if(UserSettingsRecord != null){
			Intent intent = new Intent();
	        intent.setAction("com.idt.bw.activity.AlarmService");
	        startService(intent);
	        /*
			//mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			//setNotification("please weigh again","weigh reminder","weigh reminder notification",R.drawable.arrow_l);
			/*int NotifyLoop = myOperatingTable.getUserSetting(Long.parseLong(user.getId())).getNotifyLoop().ordinal();
			String NotifyTime = myOperatingTable.getUserSetting(Long.parseLong(user.getId())).getNotifyTime();
			String NotifyDate = myOperatingTable.getUserSetting(Long.parseLong(user.getId())).getNotifyDate();
			boolean NotifyOn =  myOperatingTable.getUserSetting(Long.parseLong(user.getId())).isNotifyOn();
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			datenow = new Date(System.currentTimeMillis());		
			//String now = dateFormat.format(datenow);
			String start = NotifyDate + " " + NotifyTime + ":00";
			 //得到毫秒数
			try {
				if((NotifyDate == null)&&(NotifyTime == null))
					datestart = datenow;
				else
				datestart = dateFormat.parse(start);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(datestart.getTime() >= datenow.getTime()){
				if((datestart.getTime() - datenow.getTime()) > 30*1000)
					firstTime = datestart.getTime() - 30*1000;
				else
				firstTime = datestart.getTime();
			}else{
				firstTime = datestart.getTime() + 1*24*60*60*1000;
			}
			
			if(NotifyLoop == 0){
				times = 1*24*60*60*1000;//everyday
			}
			else if(NotifyLoop == 1){
				times = 7*24*60*60*1000;//everyweek
			}
			else{
				times = 0;
			}
			if((times != 0)&&(NotifyOn == true)){
				Intent intent = new Intent(MainActivity.this,CallAlarmNotification.class);
				//intent.putExtra("NotifyLoop", NotifyLoop);
				//intent.putExtra("NotifyTime", NotifyTime);
				PendingIntent pt = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);
				AlarmManager ar = (AlarmManager) getSystemService(ALARM_SERVICE);
				ar.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, times, pt);
			}*/
			targetWeight = UserSettingsRecord.getTargetWeight();
		}
		//show define chart
//		double GirllineValue[] = {50,60,65};
//		mTimeSeriesChart.chart = mTimeSeriesChart.createChart("Weight(kg)",GirllineValue);	        
//		mTimeSeriesChart.setChart(mTimeSeriesChart.chart);
		//chartID=1;
		//chartType = 1;
		initChart();
		//show define chart end
				
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		try {
			user = OperatingTable.instance(this).query(user.getId()).get(0);
			UserSettingsRecord = myOperatingTable.getUserSetting(Long.parseLong(user.getId()));
			mTimeSeriesChart.setChartCategoryChanged(7, user.getUserWeightUnit(), user.getUserHeightUnit());
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onStart();
	}

	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		BLEcmd.startScalage();
		
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		BLEcmd.close();
		BLEcmd.scanLeDevice(false);
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		BLEcmd.scanLeDevice(false);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BLEcmd.close();
		BLEcmd.setCallback(null);
	}
	
	
	public void init(){
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
		left_drawerlist = (ListView) findViewById(R.id.left_drawerlist);
		
		mNavDrawerItems = new ArrayList<NavDrawerItem>();
		mNavMenuIconsTypeArray = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_title);
	        
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIconsTypeArray.getResourceId(0, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIconsTypeArray.getResourceId(1, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIconsTypeArray.getResourceId(2, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIconsTypeArray.getResourceId(3, -1)));
		
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		menutopRelative = (RelativeLayout) inflater.inflate(R.layout.menutop, null);
		menuImg = (ImageView)menutopRelative.findViewById(R.id.menuImg);
		menuImg.setOnClickListener(this);
		mNavMenuIconsTypeArray.recycle();
		drawerListAdapter = new NavDrawerListAdapter();
		left_drawerlist.addHeaderView(menutopRelative,null,false);
		left_drawerlist.setAdapter(drawerListAdapter);
		left_drawerlist.setOnItemClickListener(this);
		
		myCalendar = Calendar.getInstance();
		curYear = myCalendar.get(Calendar.YEAR); 
		curMonth = myCalendar.get(Calendar.MONTH); 
		curDay = myCalendar.get(Calendar.DAY_OF_MONTH);
        if((curMonth+1) < 10)
        	curDate = curYear + "-0" + (curMonth+1);
        else
        	curDate = curYear + "-" + (curMonth+1);
        if(curDay<10)
        	curDate = curDate + "-0" + curDay;
        else
        	curDate = curDate + "-" + curDay;
		
		if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			LayoutInflater inflater = getLayoutInflater();  
	        pageViews = new ArrayList<View>();  
	        View viewTop = inflater.inflate(R.layout.main_top, null);
	        View viewBottom = inflater.inflate(R.layout.main_bottom, null);
	        pageViews.add(viewTop);  
	        pageViews.add(viewBottom);  
	        mTimeSeriesChart = (BWChartFragment) getFragmentManager().findFragmentById(R.id.TimeSeriesChart);     
	        // group是R.layou.main中的负责包裹小圆点的LinearLayout.  
	        group = (ViewGroup)findViewById(R.id.viewGroup);  
	        viewPager = (ViewPager)findViewById(R.id.guidePages); 
	        
	        munu_button = (ImageView)viewTop.findViewById(R.id.munu_button);
			main_user_pic = (ImageView)viewTop.findViewById(R.id.main_user_pic);
			main_user_name = (TextView)viewTop.findViewById(R.id.main_user_name);
			change_layout_icon = (ImageView)viewTop.findViewById(R.id.change_layout_icon);
//			main_get_weight = (TextView)viewTop.findViewById(R.id.main_get_weight);
			main_weightvalue_before = (RelativeLayout) viewTop.findViewById(R.id.main_weightvalue_before);
			main_weightvalue_after = (RelativeLayout) viewTop.findViewById(R.id.main_weightvalue_after);
			step_one_people = (ImageView)viewTop.findViewById(R.id.step_one_people);
			step_one = (ImageView) viewTop.findViewById(R.id.step_one);
			step_one_text = (TextView) viewTop.findViewById(R.id.step_one_text);
			bmi_value = (TextView) viewTop. findViewById(R.id.bmi_value);
			bmi_state = (TextView)  viewTop.findViewById(R.id.bmi_state);
			char_back_icon = (ImageView)viewBottom.findViewById(R.id.char_back_icon);
			char_weight_arrow_land = (ImageView) viewBottom.findViewById(R.id.char_weight_arrow_land);
			char_height_arrow_land = (ImageView) viewBottom.findViewById(R.id.char_height_arrow_land);
			char_bmi_arrow_land = (ImageView) viewBottom.findViewById(R.id.char_bmi_arrow_land);
			char_weight_land = (ImageView) viewBottom.findViewById(R.id.char_weight_land);
			char_height_land = (ImageView) viewBottom.findViewById(R.id.char_height_land);
			char_bmi_land = (ImageView) viewBottom.findViewById(R.id.char_bmi_land);
			
			choose_weekly = (TextView)viewBottom.findViewById(R.id.choose_weekly);
			choose_monthly = (TextView)viewBottom.findViewById(R.id.choose_monthly);
			choose_yearly = (TextView)viewBottom.findViewById(R.id.choose_yearly);
			previousDay = (ImageButton)viewBottom.findViewById(R.id.previousDay);
			nextDay = (ImageButton)viewBottom.findViewById(R.id.nextDay);
			input_data_date1 = (TextView) viewBottom.findViewById(R.id.input_data_date1);
			//main_user_graph_land = (RelativeLayout)viewBottom. findViewById(R.id.main_user_graph_land);
			main_user_pic_name = (LinearLayout)viewTop. findViewById(R.id.main_user_pic_name);
			bmiLevelView = (BMILevelView)viewTop.findViewById(R.id.weight_bmi);
//			mTimeSeriesChart.setOnSelectItemChangedListener(this);
			current_weight_value = (TextView)viewTop.findViewById(R.id.current_weight_value);
			current_weight_value_lbs = (TextView)viewTop.findViewById(R.id.current_weight_value_lbs);
			crease_image =  (ImageView)viewTop.findViewById(R.id.crease_image);
			crease_value = (TextView)viewTop.findViewById(R.id.crease_value);
//			crease_value_unit = (TextView)viewTop.findViewById(R.id.crease_value_unit);
			
	        //input_data_date1.setText(curDate);	
	        input_data_date1.setText(showingDateFormat.format(myCalendar.getTime()));
	        //setContentView(main);  
	        if("1".equals(user.getUserCategory())){
				viewPager.setBackgroundResource(R.drawable.bg03_iphone4_orange);
				step_one_people.setImageResource(R.drawable.step01_gfx_orange);
				char_weight_arrow_land.setImageResource(R.drawable.arrow_r_orange);
				char_height_arrow_land.setImageResource(R.drawable.arrow_r_orange);
				char_bmi_arrow_land.setImageResource(R.drawable.arrow_r_orange);
			}else if("2".equals(user.getUserCategory())){
				viewPager.setBackgroundResource(R.drawable.bg03_iphone4_pink);
				step_one_people.setImageResource(R.drawable.step01_gfx_pink);
				char_weight_arrow_land.setImageResource(R.drawable.arrow_r_pink);
				char_height_arrow_land.setImageResource(R.drawable.arrow_r_pink);
				char_bmi_arrow_land.setImageResource(R.drawable.arrow_r_pink);
			}else if("3".equals(user.getUserCategory())){
				viewPager.setBackgroundResource(R.drawable.bg03_iphone4_green);
				step_one_people.setImageResource(R.drawable.step01_gfx_green);
				char_weight_arrow_land.setImageResource(R.drawable.arrow_r_green);
				char_height_arrow_land.setImageResource(R.drawable.arrow_r_green);
				char_bmi_arrow_land.setImageResource(R.drawable.arrow_r_green);
			}else if("4".equals(user.getUserCategory())){
				viewPager.setBackgroundResource(R.drawable.bg03_iphone4_purple);
				step_one_people.setImageResource(R.drawable.step01_gfx_purple);
				char_weight_arrow_land.setImageResource(R.drawable.arrow_r_purple);
				char_height_arrow_land.setImageResource(R.drawable.arrow_r_purple);
				char_bmi_arrow_land.setImageResource(R.drawable.arrow_r_purple);
			}


	        viewPager.setAdapter(new GuidePageAdapter());  
	        if(user.getUserPhoto()!=null){
	        	try {
					
	        		photoBitmap = getRoundedCornerBitmap(BitmapFactory.decodeFile(user.getUserPhoto()),false);
	        		main_user_pic.setImageBitmap(photoBitmap);
				} catch (Exception e) {
					main_user_pic.setBackgroundResource(R.drawable.profile_picture);
				}
			}else{
				//main_user_pic.setImageBitmap(null);
				main_user_pic.setBackgroundResource(R.drawable.profile_picture);
			}
			main_user_name.setText(user.getUserName().toString());
			
			main_user_pic_name.setOnClickListener(this);
			change_layout_icon.setOnClickListener(this);
			munu_button.setOnClickListener(this);
//			main_get_weight.setOnClickListener(this);
			char_back_icon.setOnClickListener(this);
			char_weight_land.setOnClickListener(this);
			char_height_land.setOnClickListener(this);
			char_bmi_land.setOnClickListener(this);
			choose_weekly.setOnClickListener(this);
			choose_monthly.setOnClickListener(this);
			choose_yearly.setOnClickListener(this);
			nextDay.setOnClickListener(this);
			previousDay.setOnClickListener(this);
			current_weight_value.setOnClickListener(this);
	        
		}else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			main_user = (RelativeLayout) findViewById(R.id.main_user);
			main_user_pic_name = (LinearLayout) findViewById(R.id.main_user_pic_name);
			main_user_graph = (RelativeLayout) findViewById(R.id.main_user_graph);
			main_weightvalue_before = (RelativeLayout) findViewById(R.id.main_weightvalue_before);
			main_weightvalue_after = (RelativeLayout) findViewById(R.id.main_weightvalue_after);
			top_bar = (RelativeLayout) findViewById(R.id.top_bar);
			munu_button = (ImageView) findViewById(R.id.munu_button);
			main_user_pic = (ImageView) findViewById(R.id.main_user_pic);
			change_layout_icon = (ImageView) findViewById(R.id.change_layout_icon);
			main_user_name = (TextView) findViewById(R.id.main_user_name);
//			main_get_weight = (TextView) findViewById(R.id.main_get_weight);
			bmi_value = (TextView) findViewById(R.id.bmi_value);
			bmi_state = (TextView) findViewById(R.id.bmi_state);
			char_weight_arrow = (ImageView) findViewById(R.id.char_weight_arrow);
			char_height_arrow = (ImageView) findViewById(R.id.char_height_arrow);
			char_bmi_arrow = (ImageView) findViewById(R.id.char_bmi_arrow);
			char_weight = (ImageView) findViewById(R.id.char_weight);
			char_height = (ImageView) findViewById(R.id.char_height);
			char_bmi = (ImageView) findViewById(R.id.char_bmi);
			
			step_one_people = (ImageView) findViewById(R.id.step_one_people);
			step_one = (ImageView) findViewById(R.id.step_one);
			step_one_text = (TextView) findViewById(R.id.step_one_text);
			current_weight_value = (TextView) findViewById(R.id.current_weight_value);
			current_weight_value_lbs = (TextView) findViewById(R.id.current_weight_value_lbs);
			crease_image =  (ImageView) findViewById(R.id.crease_image);
			crease_value = (TextView) findViewById(R.id.crease_value);
//			crease_value_unit = (TextView) findViewById(R.id.crease_value_unit);
			choose_weekly = (TextView)findViewById(R.id.choose_weekly);
			choose_monthly = (TextView)findViewById(R.id.choose_monthly);
			choose_yearly = (TextView)findViewById(R.id.choose_yearly);
			previousDay = (ImageButton)findViewById(R.id.previousDay);
			nextDay = (ImageButton)findViewById(R.id.nextDay);
			input_data_date1 = (TextView) findViewById(R.id.input_data_date1);
			bmiLevelView = (BMILevelView) findViewById(R.id.weight_bmi);
			
			mTimeSeriesChart = (BWChartFragment) getFragmentManager().findFragmentById(R.id.TimeSeriesChart);   
			
			//input_data_date1.setText(curDate);
			input_data_date1.setText(showingDateFormat.format(myCalendar.getTime()));
			if("1".equals(user.getUserCategory())){
				main_user.setBackgroundResource(R.drawable.bg02_iphone4_orange);
				step_one_people.setImageResource(R.drawable.step01_gfx_orange);
				char_weight_arrow.setImageResource(R.drawable.arrow_up_orange);
				char_height_arrow.setImageResource(R.drawable.arrow_up_orange);
				char_bmi_arrow.setImageResource(R.drawable.arrow_up_orange);
			}else if("2".equals(user.getUserCategory())){
				main_user.setBackgroundResource(R.drawable.bg02_iphone4_pink);
				step_one_people.setImageResource(R.drawable.step01_gfx_pink);
				char_weight_arrow.setImageResource(R.drawable.arrow_up_pink);
				char_height_arrow.setImageResource(R.drawable.arrow_up_pink);
				char_bmi_arrow.setImageResource(R.drawable.arrow_up_pink);
			}else if("3".equals(user.getUserCategory())){
				main_user.setBackgroundResource(R.drawable.bg02_iphone4_green);
				step_one_people.setImageResource(R.drawable.step01_gfx_green);
				char_weight_arrow.setImageResource(R.drawable.arrow_up_green);
				char_height_arrow.setImageResource(R.drawable.arrow_up_green);
				char_bmi_arrow.setImageResource(R.drawable.arrow_up_green);
			}else if("4".equals(user.getUserCategory())){
				main_user.setBackgroundResource(R.drawable.bg02_iphone4_purple);
				step_one_people.setImageResource(R.drawable.step01_gfx_purple);
				char_weight_arrow.setImageResource(R.drawable.arrow_up_purple);
				char_height_arrow.setImageResource(R.drawable.arrow_up_purple);
				char_bmi_arrow.setImageResource(R.drawable.arrow_up_purple);
			}
			if(user.getUserPhoto()!=null){
				try {
					photoBitmap = getRoundedCornerBitmap(BitmapFactory.decodeFile(user.getUserPhoto()),false);
					main_user_pic.setImageBitmap(photoBitmap);
				} catch (Exception e) {
					main_user_pic.setBackgroundResource(R.drawable.profile_picture);
				}
			}else{
				//main_user_pic.setImageBitmap(null);
				main_user_pic.setBackgroundResource(R.drawable.profile_picture);
			}
			main_user_name.setText(user.getUserName().toString());
			
			char_weight.setOnClickListener(this);
			char_height.setOnClickListener(this);
			char_bmi.setOnClickListener(this);
			munu_button.setOnClickListener(this);
//			main_get_weight.setOnClickListener(this);
			main_user_pic_name.setOnClickListener(this);
			choose_weekly.setOnClickListener(this);
			choose_monthly.setOnClickListener(this);
			choose_yearly.setOnClickListener(this);
			input_data_date1.setOnClickListener(this);
			nextDay.setOnClickListener(this);
			previousDay.setOnClickListener(this);
			current_weight_value.setOnClickListener(this);
//			mTimeSeriesChart.setOnSelectItemChangedListener(this);
		}
	}
	
	private void initChart() {  
		
		mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_WEIGHT, user.getUserWeightUnit(), user.getUserHeightUnit());
//		mTimeSeriesChart.setChartType(1);
		/* ShinobiChart shinobiChart = mTimeSeriesChart.getShinobiChart();

         // TODO: replace <license_key_here> with you trial license key
         shinobiChart.setLicenseKey("<license_key_here>");
         
         DateTimeAxis xAxis = new DateTimeAxis ();
         xAxis.setTitle("Date");
         xAxis.enableGesturePanning(true);
         xAxis.enableGestureZooming(true);
         shinobiChart.addXAxis(xAxis);

         NumberAxis yAxis = new NumberAxis ();
         yAxis.setTitle("Price (USD)");
         yAxis.enableGesturePanning(true);
         yAxis.enableGestureZooming(true);
         shinobiChart.addYAxis(yAxis);

         CandlestickSeries series = new CandlestickSeries ();
         DataAdapter<Date, Double> dataAdapter = new SimpleDataAdapter<Date, Double> ();
         series.setDataAdapter(dataAdapter);
         shinobiChart.addSeries(series);*/
		
	}
	
	public void setTimeSeriesChartData() {
		int category = mTimeSeriesChart.getChartCategory();
		String per = "daily";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		boolean isTarget = false, isReference = false, isMarker = false;
		/*int type = mTimeSeriesChart.getChartType();
		if(type==1){//dayly
			mRecord = myOperatingTable.getDaysRecords(Long.parseLong(user.getId()));
		}
		else if(type==2){//monthly 
			mRecord = myOperatingTable.getMonthsRecords(Long.parseLong(user.getId()));			
		}
		else if(type==3){//yearly
			mRecord = myOperatingTable.getYearsRecords(Long.parseLong(user.getId()));
		}*/
		mRecord = myOperatingTable.getDaysRecords(Long.parseLong(user.getId()));
		if (mRecord == null || mRecord.isEmpty()) {
			return;
		}
		// setting if showing target line;
		if (UserSettingsRecord != null && UserSettingsRecord.isTargetlOn()) {
			if (category == BWChartFragment.CATEGORY_WEIGHT) {
				mTimeSeriesChart.setShowingTargetLine(true);
				isTarget = true;
			} else {
				mTimeSeriesChart.setShowingTargetLine(false);
				isTarget = false;
			}
		}
		// setting data
		mTimeSeriesChart.clearChart();
		double highestRecord = 0.;
		for (Record r : mRecord) {
			String datetime = r.getDatetime();
			float weight = r.getWeight();
			float height = r.getCurrentHeight();
			//float bmi = weight / (height/100) / (height/100);
			float value = 0;
			if (category == BWChartFragment.CATEGORY_WEIGHT) {
				value = r.getWeight();
				if ("lb".equals(user.getUserWeightUnit())) {
					value = value/0.45359237f;
				}
			} else if (category == BWChartFragment.CATEGORY_HEIGHT) {
				value = r.getCurrentHeight();
				if ("ft".equals(user.getUserHeightUnit())) {
					value = value/30.48f;
				}
			} else if (category == BWChartFragment.CATEGORY_BMI) {
				value = r.getWeight() * 10000.f / (r.getCurrentHeight() * r.getCurrentHeight());
			}
			Date period;
			try {
				period = format.parse(r.getDatetime());
				mTimeSeriesChart.setChartValue(period, value);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (value > highestRecord) {
				highestRecord = value;
			}
		}
		mTimeSeriesChart.setCharHighestValue(highestRecord);
		if (isTarget) {
			double targetValue = 0;
			if (UserSettingsRecord != null )
				targetValue = UserSettingsRecord.getTargetWeight();
			mTimeSeriesChart.setChartTarget(targetValue);
		}
		// setting if showing reference line;
		Date beginTime = null;
		Date birthday = null;
		if(UserSettingsRecord != null) {
			if ( UserSettingsRecord.isGeneralOn()) {
				mTimeSeriesChart.setShowingReferenceLine(true);
				isReference = true;
			} else {
				mTimeSeriesChart.setShowingReferenceLine(false);
				isReference = false;
			}
		}
		// show reference data chart
		if (isReference) {
			//Calendar beginCalendar = Calendar.getInstance();
			//beginCalendar.setTimeInMillis(beginTime.getTime());
			//Calendar endCalender = Calendar.getInstance();
//			try {
//				endCalender.setTimeInMillis(format.parse(mRecord.get(mRecord.size() - 1).getDatetime()).getTime());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				birthday = new SimpleDateFormat("yyyy-MM-dd").parse(user.getUserBirth());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar birthCalender = Calendar.getInstance();
			birthCalender.setTimeInMillis(birthday.getTime());
			String categoryStr = null;
			switch (category) {
			case BWChartFragment.CATEGORY_WEIGHT:
				categoryStr = "weight";
				break;
			case BWChartFragment.CATEGORY_HEIGHT:
				categoryStr = "height";
				break;
			case BWChartFragment.CATEGORY_BMI:
				categoryStr = "bmi";
				break;
			default:
				break;
			}
			/*Map<String, Float> map = myOperatingTable.queryReference(
					beginCalendar,
					endCalender,
					birthCalender, 
					"1".equals(user.getUserGender()) ? 0 : 1, 
							categoryStr, 
							per);
			
			 for (Map.Entry<String, Float> entry : map.entrySet()) {   
		            System.out.println("key= " + entry.getKey() + "  and  value= "  
		                    + entry.getValue());   
				 	Date period ;
					try {
						period = format.parse(entry.getKey());
						 mTimeSeriesChart.setChartReference(period, entry.getValue());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					switch (type) {
					case 1:
						try {
							period = new Day(format.parse(entry.getKey()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						break;
					case 2:
						try {
							period = new Month(format.parse(entry.getKey()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						try {
							period = new Year(format.parse(entry.getKey()));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						break;

					default:
						break;
					}
				
		     }  */
			
			List<ReferenceDate> list = myOperatingTable.queryReference(birthCalender, "1".equals(user.getUserGender()) ? 0 : 1, categoryStr);
			 for (ReferenceDate item : list) {   
		           /* System.out.println("key= " + entry.getKey() + "  and  value= "  
		                    + entry.getValue()); */  
				 mTimeSeriesChart.setChartReference(item.getPeriod(), item.getValue());
			}
		}
		// show marker lines
		if (BWChartFragment.CATEGORY_BMI == category) {
			mTimeSeriesChart.setShowingMarkerLines(true);
			try {
				mTimeSeriesChart.setChartMarker(format.parse(mRecord.get(0)
						.getDatetime()), format.parse(mRecord.get(
						mRecord.size() - 1).getDatetime()), new float[] { 30.f,
						25f, 18.5f, 0f });
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			mTimeSeriesChart.setShowingMarkerLines(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.munu_button:
			drawer_layout.openDrawer(left_drawerlist); 
			break;
		case R.id.menuImg:
			drawer_layout.closeDrawer(left_drawerlist);
			break;
		case R.id.change_layout_icon:
//			Log.e("cdf", "to right----------");
			break;
		case R.id.main_user_pic_name:
			Intent i = new Intent(MainActivity.this,UpdateUserActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("user", user);
			i.putExtras(b);
			startActivity(i);
			finish();
			break;
		/*case R.id.main_get_weight:
			main_weightvalue_before.setVisibility(View.GONE);
			main_weightvalue_after.setVisibility(View.VISIBLE);
			if (main_get_weight.getText().equals(getResources().getString(R.string.main_scale))) {
				main_get_weight.setText(R.string.main_scaling);
				BLEcmd.startScalage();
			}
				
			break;*/
		case R.id.char_weight:
			char_weight.setImageResource(R.drawable.chart_weight_hl);
			char_height.setImageResource(R.drawable.chart_height_normal);
			char_bmi.setImageResource(R.drawable.chart_bmi_normal);
			char_weight_arrow.setVisibility(View.VISIBLE);
			char_height_arrow.setVisibility(View.INVISIBLE);
			char_bmi_arrow.setVisibility(View.INVISIBLE);
			mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_WEIGHT, user.getUserWeightUnit(), user.getUserHeightUnit());
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.char_height:
			char_weight.setImageResource(R.drawable.chart_weight_normal);
			char_height.setImageResource(R.drawable.chart_height_hl);
			char_bmi.setImageResource(R.drawable.chart_bmi_normal);
			char_weight_arrow.setVisibility(View.INVISIBLE);
			char_height_arrow.setVisibility(View.VISIBLE);
			char_bmi_arrow.setVisibility(View.INVISIBLE);
			mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_HEIGHT, user.getUserWeightUnit(), user.getUserHeightUnit());
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.char_bmi:
			char_weight.setImageResource(R.drawable.chart_weight_normal);
			char_height.setImageResource(R.drawable.chart_height_normal);
			char_bmi.setImageResource(R.drawable.chart_bmi_hl);
			char_weight_arrow.setVisibility(View.INVISIBLE);
			char_height_arrow.setVisibility(View.INVISIBLE);
			char_bmi_arrow.setVisibility(View.VISIBLE);
			mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_BMI, null, null);
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.char_weight_land:
			char_weight_land.setImageResource(R.drawable.chart_weight_hl);
			char_height_land.setImageResource(R.drawable.chart_height_normal);
			char_bmi_land.setImageResource(R.drawable.chart_bmi_normal);
			char_weight_arrow_land.setVisibility(View.VISIBLE);
			char_height_arrow_land.setVisibility(View.INVISIBLE);
			char_bmi_arrow_land.setVisibility(View.INVISIBLE);
			mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_WEIGHT, user.getUserWeightUnit(), user.getUserHeightUnit());
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.char_height_land:
			char_weight_land.setImageResource(R.drawable.chart_weight_normal);
			char_height_land.setImageResource(R.drawable.chart_height_hl);
			char_bmi_land.setImageResource(R.drawable.chart_bmi_normal);
			char_weight_arrow_land.setVisibility(View.INVISIBLE);
			char_height_arrow_land.setVisibility(View.VISIBLE);
			char_bmi_arrow_land.setVisibility(View.INVISIBLE);
			mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_HEIGHT, user.getUserWeightUnit(), user.getUserHeightUnit());
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.char_bmi_land:
			char_weight_land.setImageResource(R.drawable.chart_weight_normal);
			char_height_land.setImageResource(R.drawable.chart_height_normal);
			char_bmi_land.setImageResource(R.drawable.chart_bmi_hl);
			char_weight_arrow_land.setVisibility(View.INVISIBLE);
			char_height_arrow_land.setVisibility(View.INVISIBLE);
			char_bmi_arrow_land.setVisibility(View.VISIBLE);
			mTimeSeriesChart.setChartCategoryChanged(BWChartFragment.CATEGORY_BMI, null, null);
			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.char_back_icon:
//			Log.e("cdf", "to left----------");
			break;
			
			// deleted by aaronli at May 9 2014. removed listener
		case R.id.input_data_date1:
			//datepicker
			Date d2 = null;
			try {
				d2 = showingDateFormat.parse(input_data_date1.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Calendar c2 = Calendar.getInstance(); 
			c2.setTime(d2);
			new DatePickerDialog(MainActivity.this,  
			           new DatePickerDialog.OnDateSetListener() {
			               @Override  
			               public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {   
			            	   //input_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
			            	   //curDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
			            	   /*curYear = year; curMonth = monthOfYear; curDay = dayOfMonth;
			            	   myCalendar.set(year, monthOfYear, dayOfMonth);
			            	   if((monthOfYear+1) < 10)
			                   	curDate = year + "-0" + (monthOfYear+1);
			                   else
			                   	curDate = year + "-" + (monthOfYear+1);
			                   if(curDay<10)
			                   	curDate = curDate + "-0" + dayOfMonth;
			                   else
			                   	curDate = curDate + "-" + dayOfMonth;*/
			                   //input_data_date1.setText(curDate);
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
								input_data_date1.setText(showingDateFormat.format(c.getTime()));
			               }  
			           }  
			           , c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH)).show();
		     break;
		case R.id.previousDay:
			Date d1 = null;
			try {
				d1 = showingDateFormat.parse(input_data_date1.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Calendar c1 = Calendar.getInstance(); 
			c1.setTime(d1);
			c1.add(Calendar.DATE, -1); 
			input_data_date1.setText(showingDateFormat.format(c1.getTime()));
	        
	        //input_data_date1减一天显示，在曲线上显示当天信息
			//mTimeSeriesChart.setSelectPrevious();
			break;
		case R.id.nextDay:
			Date d3 = null;
			try {
				d3 = showingDateFormat.parse(input_data_date1.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Calendar c3 = Calendar.getInstance(); 
			c3.setTime(d3);
			c3.add(Calendar.DATE, +1); 
			input_data_date1.setText(showingDateFormat.format(c3.getTime()));
	        
			//input_data_date1加一天显示，在曲线上显示当天信息
			//mTimeSeriesChart.setSelectNext();
			break;
		case R.id.choose_weekly:
			//显示每天的曲线值（使用数据库接口，实际读取每天最大值）
			//chartType = 1;//day=1;month=2;year=3
			/*choose_weekly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundselect);
			choose_monthly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundunselected);
			choose_yearly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundunselected);*/
			//initChartSub();//initChart();
//			mTimeSeriesChart.setChartCategoryChanged("weight");
//			mTimeSeriesChart.setChartType(1);
//			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_WEEKLY);
			break;
		case R.id.choose_monthly:
			//显示每月的曲线值（使用数据库接口，实际读取每月最大值）
			//chartType = 2;//day=1;month=2;year=3
			/*choose_weekly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundunselected);
			choose_monthly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundselect);
			choose_yearly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundunselected);*/
//			initChartSub();//initChart();
//			mTimeSeriesChart.setChartCategoryChanged("weight");
//			mTimeSeriesChart.setChartType(2);
//			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_MONTHLY);
			break;
		case R.id.choose_yearly:
			//显示每年的曲线值（使用数据库接口，实际读取每年最大值）
//			chartType = 3;//day=1;month=2;year=3
			/*choose_weekly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundunselected);
			choose_monthly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundunselected);
			choose_yearly.setBackgroundResource(R.drawable.listcanlendarviewbackgroundselect);*/
//			initChartSub();//initChart();
//			mTimeSeriesChart.setChartCategoryChanged("weight");
//			mTimeSeriesChart.setChartType(3);
//			setTimeSeriesChartData();
			mTimeSeriesChart.setShowingScale(myCalendar.getTime(), BWChartFragment.PER_YEARLY);
			break;

		case R.id.current_weight_value:
			if("1".equals(user.getUserCategory())){
				//main_user.setBackgroundResource(R.drawable.bg02_iphone4_orange);
				step_one_people.setImageResource(R.drawable.step01_gfx_orange);
			}else if("2".equals(user.getUserCategory())){
				//main_user.setBackgroundResource(R.drawable.bg02_iphone4_pink);
				step_one_people.setImageResource(R.drawable.step01_gfx_pink);
			}else if("3".equals(user.getUserCategory())){
				//main_user.setBackgroundResource(R.drawable.bg02_iphone4_green);
				step_one_people.setImageResource(R.drawable.step01_gfx_green);
			}else if("4".equals(user.getUserCategory())){
				//main_user.setBackgroundResource(R.drawable.bg02_iphone4_purple);
				step_one_people.setImageResource(R.drawable.step01_gfx_purple);
			}
			//init();
//			step_one_people.setImageResource(R.drawable.step01_gfx_purple);
			step_one.setImageResource(R.drawable.step01_num);
			step_one_text.setText(R.string.step_one);
			main_weightvalue_before.setVisibility(View.VISIBLE);
			main_weightvalue_after.setVisibility(View.GONE);
			crease_image.setVisibility(View.GONE);
			crease_value.setVisibility(View.GONE);
			bmi_value.setVisibility(View.GONE);
			bmi_state.setVisibility(View.GONE);
			BLEcmd.startScalage();  
			
			break;
			
		default:
			break;
		}
	}
	
	/*@Override
	public void onSelectItemChanged(Date time, float value) {
		input_data_date1.setText(new SimpleDateFormat("yyyy-MM-dd").format(time));
		
	}*/
	
	/** 指引页面Adapter */
    class GuidePageAdapter extends PagerAdapter {  
        @Override  
        public int getCount() {  
            return pageViews.size();  
        }  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
        @Override  
        public int getItemPosition(Object object) {  
            return super.getItemPosition(object);  
        }  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
        }  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            ((ViewPager) arg0).addView(pageViews.get(arg1));  
            return pageViews.get(arg1);  
        }  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
  
        }  
        @Override  
        public Parcelable saveState() {  
            return null;  
        }  
        @Override  
        public void startUpdate(View arg0) {  
  
        }  
        @Override  
        public void finishUpdate(View arg0) {  
  
        }  
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
	class NavDrawerListAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mNavDrawerItems.size();
		}
		@Override
		public Object getItem(int position) {		
			return mNavDrawerItems.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
	        }
	        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
	        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
	        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
	        imgIcon.setImageResource(mNavDrawerItems.get(position).getIcon());        
	        txtTitle.setText(mNavDrawerItems.get(position).getTitle());
	        
	        // displaying count
	        // check whether it set visible or not
	        if(mNavDrawerItems.get(position).getCounterVisibility()){
	        	txtCount.setText(mNavDrawerItems.get(position).getCount());
	        }else{
	        	// hide the counter view
	        	txtCount.setVisibility(View.GONE);
	        }
	        return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 1:
			//Log.e("cdf","go choose user activity !!!");
			// finishing this activity and return to choose another user instead of start new activity to choose new user;
			// modified by aaronli at Mar 25 2014
			/*Intent i = new Intent(MainActivity.this, ChooseUserActivity.class);
			startActivity(i);*/
			finish();
			break;
		case 2:
			//Log.e("cdf","go settings user activity !!!!!!!!");
			Intent ii = new Intent(MainActivity.this, SettingsActivity.class);
			ii.putExtra("userID", user.getId());
			ii.putExtra("Height", user.getUserHeight());
			startActivity(ii);
			break;
		case 3:
			//Log.e("cdf","go share user activity !!!!!!!!!!!!!!!!!!!");
//			Intent iii = new Intent(MainActivity.this, ShareActivity.class);
//			startActivity(iii);
			shareScaleWeight();
			break;
		case 4:
			//Log.e("cdf","go input user activity !!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Intent iiii = new Intent(MainActivity.this, DataInputActivity.class);
			iiii.putExtra("userID", user.getId());
			iiii.putExtra("Height", user.getUserHeight());
			iiii.putExtra("targetWeight", targetWeight);
			startActivity(iiii);
			break;

		default:
			break;
		}
	}
	
	// added by aaronli at May 2014. For sharing.
	private void shareScaleWeight() {
		mTimeSeriesChart.screenShot();
		// sharing picture
		

	}

	private BLECommandCallback BLEcallback = new BLECommandCallback() {
		
		@Override
		public void onReturnResult(final WeightData measurementData) {
			MainActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					float temp=0;
					if(UserSettingsRecord != null && UserSettingsRecord.isClothlOn() == true){
						temp = measurementData.getWeight() - UserSettingsRecord.getClothweight();
					}
					else 
						temp = measurementData.getWeight();
//					current_weight_value.setText(measurementData.getWeight() + "");
					
					/*if(measurementData.getUnit() == 0)
						crease_value_unit.setText(R.string.create_weight_kg);
					else
						crease_value_unit.setText(R.string.create_weight_lb);	*/
					// if the user category is purple, for two steps to scale.
					if ("4".equals(user.getUserCategory())) {
						// ready to enter step 2.
						if (stepScale == 0) {
							stepScale = 1;
							parentWeight = temp;
							step_one_people.setImageResource(R.drawable.step02_gfx_purple);
							step_one.setImageResource(R.drawable.step02_num);
							step_one_text.setText(R.string.step_two);
							main_weightvalue_before.setVisibility(View.VISIBLE);
							main_weightvalue_after.setVisibility(View.GONE);
							try {
								BLEcmd.startScalage();  
							} catch (Exception e) {
								e.printStackTrace();
							}  
//							main_get_weight.setText(R.string.main_scale);
							return;
						} else {
							temp -= parentWeight;
							parentWeight = 0;
							stepScale = 0;
							step_one_people.setImageResource(R.drawable.step01_gfx_purple);
							step_one.setImageResource(R.drawable.step01_num);
							step_one_text.setText(R.string.step_one);
						}
					}
					crease_image.setVisibility(View.VISIBLE);
					crease_value.setVisibility(View.VISIBLE);
					bmi_value.setVisibility(View.VISIBLE);
					bmi_state.setVisibility(View.VISIBLE);
//					main_get_weight.setText(R.string.main_scaled);
					//current_weight_value.setText(String.format("%.2f", temp));
					
					if(temp > targetWeight){
						crease_image.setImageResource(R.drawable.weight_increase);
						
					}else{
						crease_image.setImageResource(R.drawable.weight_decrease);
					}
					//crease_value.setText(String.format("%.2f", temp-targetWeight));
					
					if("kg".equals(user.getUserWeightUnit())){
//						crease_value_unit.setText("kg");
						current_weight_value_lbs.setText("kg");
						crease_value.setText(String.format("%.2f", temp-targetWeight));
						current_weight_value.setText(String.format("%.2f", temp));
					}else{
//						crease_value_unit.setText("lb");
						current_weight_value_lbs.setText("lb");
						crease_value.setText(String.format("%.2f", (temp-targetWeight)/0.45359237));
						current_weight_value.setText(String.format("%.2f", temp/0.45359237));
					}
					//save data to DB
					calendar = Calendar.getInstance();
					/*curYear = calendar.get(Calendar.YEAR); 
					curMonth = calendar.get(Calendar.MONTH); 
					curDay = calendar.get(Calendar.DAY_OF_MONTH);
			        curhour=calendar.get(Calendar.HOUR);//Сʱ   
			        curminute=calendar.get(Calendar.MINUTE);//��              
			        cursecond=calendar.get(Calendar.SECOND);//�� 
*/			        //curDate = curYear + "-" + (curMonth+1) + "-" + curDay;
			       /* if((curMonth+1) < 10)
			        	curDate = curYear + "-0" + (curMonth+1);
			        else
			        	curDate = curYear + "-" + (curMonth+1);
			        if(curDay<10)
			        	curDate = curDate + "-0" + curDay;
			        else
			        	curDate = curDate + "-" + curDay;*/
			        			        
					//float herght = myOperatingTable.getLastRecord(Long.parseLong(user.getId())).getCurrentHeight();
					float height = Float.parseFloat(user.getUserHeight());
			        /*try {
			        	targetWeight = myOperatingTable.getUserSetting(Long.parseLong(user.getId())).getTargetWeight();
					} catch (Exception e) {
						targetWeight = 65;
					}*/
					String curDate = DateFormat.format("yyyy-MM-dd HH:mm", calendar).toString();
					Record myRecord = new Record();					
					myRecord.setWeight(temp);
					//BMI = kg/m2    /////////////////////////////////////
					float bmi = (float) ((temp*10000)/(height*height));
					bmi_value.setText("BMI:"+String.format("%.2f",bmi));
					bmiLevelView.setBmiValue(bmi);
					if(bmi<18.50){
						bmi_state.setText(getResources().getString(R.string.bmi_state_underweight));
					}else if(bmi<25.0 && bmi>=18.50){
						bmi_state.setText(getResources().getString(R.string.bmi_state_normal));
					}else if(bmi<30 && bmi>=25.00){
						bmi_state.setText(getResources().getString(R.string.bmi_state_pre_obese));
					}else if(bmi>=30){
						bmi_state.setText(getResources().getString(R.string.bmi_state_obese));
					}
					////////////////////////////////////////////////////
					myRecord.setUserId(Long.parseLong(user.getId()));
					myRecord.setCurrentHeight(height);
					myRecord.setDatetime(curDate);
					myRecord.setTargetWeight(targetWeight);
					myOperatingTable.addRecord(myRecord);
				}
			});
		}
		
		@Override
		public void onPairSuccess() {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					main_get_weight.setText(R.string.main_scaled);
					main_weightvalue_before.setVisibility(View.GONE);
					main_weightvalue_after.setVisibility(View.VISIBLE);
				}
			});
		}

		@Override
		public void onNotSuport() {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(MainActivity.this, R.string.connect_fail, Toast.LENGTH_SHORT).show();
					if("1".equals(user.getUserCategory())){
						//main_user.setBackgroundResource(R.drawable.bg02_iphone4_orange);
						step_one_people.setImageResource(R.drawable.step01_gfx_orange);
					}else if("2".equals(user.getUserCategory())){
						//main_user.setBackgroundResource(R.drawable.bg02_iphone4_pink);
						step_one_people.setImageResource(R.drawable.step01_gfx_pink);
					}else if("3".equals(user.getUserCategory())){
						//main_user.setBackgroundResource(R.drawable.bg02_iphone4_green);
						step_one_people.setImageResource(R.drawable.step01_gfx_green);
					}else if("4".equals(user.getUserCategory())){
						//main_user.setBackgroundResource(R.drawable.bg02_iphone4_purple);
						step_one_people.setImageResource(R.drawable.step01_gfx_purple);
					}
					//init();
//					step_one_people.setImageResource(R.drawable.step01_gfx_purple);
					step_one.setImageResource(R.drawable.step01_num);
					step_one_text.setText(R.string.step_one);
					main_weightvalue_before.setVisibility(View.VISIBLE);
					main_weightvalue_after.setVisibility(View.GONE);
					crease_image.setVisibility(View.GONE);
					crease_value.setVisibility(View.GONE);
					bmi_value.setVisibility(View.GONE);
					bmi_state.setVisibility(View.GONE);
					BLEcmd.startScalage();  
				}
			});
		}

		@Override
		public void onDisconnect(BluetoothDevice device) {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					/*if (main_get_weight.getText().equals(getResources().getString(R.string.main_scaling))) {
						Toast.makeText(MainActivity.this, R.string.main_toast_scaling_failed, Toast.LENGTH_SHORT).show();
						return;
					}*/
//					main_get_weight.setText(R.string.main_scaled);
					/*if ("4".equals(user.getUserCategory())) {
						if (stepScale == 1) {
							step_one_people.setImageResource(R.drawable.step02_gfx_purple);
							main_weightvalue_before.setVisibility(View.VISIBLE);
							main_weightvalue_after.setVisibility(View.GONE);
							main_get_weight.setText(R.string.main_scale);
						} else {
							step_one_people.setImageResource(R.drawable.step01_gfx_purple);
							step_one.setImageResource(R.drawable.step01_num);
							step_one_text.setText(R.string.step_one);
						}
					}*/
				}
			});
		}

		@Override
		public void onDevicesNotFound() {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(MainActivity.this, R.string.connect_fail, Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onConnectSuccess(BluetoothDevice device) {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {

				}
			});
		}

		@Override
		public void onConnectFail(BluetoothDevice device) {
			MainActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(MainActivity.this, R.string.connect_fail, Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onVariableData(final WeightData measurementData) {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {	
					float a = 0;
					if(UserSettingsRecord != null && UserSettingsRecord.isClothlOn() == true){
						a = measurementData.getWeight() - UserSettingsRecord.getClothweight();
					}
					else 
						a = measurementData.getWeight();
					if("kg".equals(user.getUserWeightUnit())){
//						crease_value_unit.setText("kg");
						current_weight_value_lbs.setText("kg");
						current_weight_value.setText(String.valueOf(a));
					}else{
//						crease_value_unit.setText("lb");
						current_weight_value_lbs.setText("lb");
						current_weight_value.setText(String.format("%.2f", a/0.45359237));
					}
//					Log.e("cdf","getWeight----------"+a);
					/*current_weight_value.setText(String.valueOf(measurementData.getWeight()));
					if(measurementData.getUnit() == 0)
						current_weight_value_lbs.setText("kg");
					else
						current_weight_value_lbs.setText("lbs");*/

				}
			});
		}

		@Override
		public void onScalingFail() {
			runOnUiThread(new Runnable() {
				public void run() {
					
					Toast.makeText(MainActivity.this, R.string.main_toast_scaling_failed, Toast.LENGTH_SHORT).show();
					stepScale = 0;
				}
			});
		}
	};
	
	 void setNotification(String tickerText,String title,String content,int drawable){
		long a = System.currentTimeMillis();
		Notification notification=new Notification(drawable, tickerText, System.currentTimeMillis());
		Intent intent=new Intent(this, ChooseUserActivity.class);
		//intent.setClass(this, ChooseUserActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification.setLatestEventInfo(this, title, content, pendingIntent);
		mNotificationManager.notify(123, notification);
	}

	
	
}
