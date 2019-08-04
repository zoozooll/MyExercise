package com.idt.bw.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.idt.bw.bean.Record;
import com.idt.bw.bean.User;
import com.idt.bw.bean.UserSettings;
import com.idt.bw.database.OperatingTable;
import com.idt.bw.utils.DateManager;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.idt.bw.bean.UserSettings.NotifyLoopMode;

 public class SettingsActivity extends Activity implements OnClickListener{
	private Switch ReminderOnOff,settings_reference_choose,settings_target_choose,settings_cloth_choose;
	private EditText Target_weight,cloth_weight;
	private View settings_reminder_datetime_repeat;
	private RelativeLayout settings_reminder_repeat,settings_reminder_datetime;
	private RelativeLayout settings_product_tour,settings_target_weight_val,settings_cloth_weight_val;
	private ImageView reminder_line;
	//private ImageButton ReminderButton;

	private TextView RepeatDays,myVersion,targetWeightUnit,clothWeightUnit;

	//private int[] whichone;
	//private float TargetweightNum;
	private TextView settingsUserDone;
	OperatingTable myOperatingTable;
	UserSettings UserSettingsRecord;
	private Button reminder_date,reminder_timer;
	private Calendar calendar ;
	private int curYear,curMonth,curDay,curhour,curminute,cursecond;
	private Date datenow,datestart;
    private long times,firstTime;
	private String NotifyTime,NotifyDate;
	private boolean NotifyOn = false, NotifyFeature = false ,showWeightStatus = true,showTarget = false,showClothWeight=false;
	private long userID,Height;
	private String[] NotifyLoop;
	private String[] RepeatitemsShow = null;
	private String[] Repeatitems = null;
	private boolean[] checkedWeekdayItems = new boolean[7];
	private final boolean[] arrayFruitSelected = new boolean[] {false, false, false, false, false, false, false};
	private int arrayFruitSelectedNum;
	private ArrayList<User> user;
	private DecimalFormat df;
	
	private AlertDialog.Builder builder;
	
	private String timeFormat;
	protected java.text.DateFormat showingDateFormat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		showingDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG);
		//for(int i=0; i<7; i++){
		RepeatitemsShow=SettingsActivity.this.getResources().getStringArray(R.array.RepeatitemsShow0);
		Repeatitems=SettingsActivity.this.getResources().getStringArray(R.array.Repeatitems0);
		//}
		ContentResolver cv = this.getContentResolver();
        String timeFormat = android.provider.Settings.System.getString(cv,android.provider.Settings.System.TIME_12_24);
        
		myOperatingTable = OperatingTable.instance(this);
		df = new DecimalFormat("#0.0");
				
		Target_weight = (EditText) this.findViewById(R.id.target_weight);
		cloth_weight = (EditText) this.findViewById(R.id.cloth_weight);
		//TargetweightNum = Integer.parseInt("65"); 
		settings_reminder_datetime_repeat = findViewById(R.id.settings_reminder_datetime_repeat);
		settings_product_tour =  (RelativeLayout) findViewById(R.id.settings_product_tour);
		settings_target_weight_val =  (RelativeLayout) findViewById(R.id.settings_target_weight_val);
		settings_cloth_weight_val =  (RelativeLayout) findViewById(R.id.settings_cloth_weight_val);
		settings_reminder_repeat =  (RelativeLayout) findViewById(R.id.settings_reminder_repeat);
		settings_reminder_datetime = (RelativeLayout) findViewById(R.id.settings_reminder_datetime);
		reminder_line = (ImageView)  findViewById(R.id.reminder_line);
		RepeatDays = (TextView) this.findViewById(R.id.create_birthday_edit);
		myVersion = (TextView) this.findViewById(R.id.myVersion);
		targetWeightUnit = (TextView) this.findViewById(R.id.settings_weight_value_lbs);
		clothWeightUnit = (TextView) this.findViewById(R.id.settings_cloth_value_lbs);
		//ProductButton =  (ImageButton) findViewById(R.id.settings_product);
		//ReminderButton =  (ImageButton) findViewById(R.id.reminderbutton);
		settingsUserDone = (TextView) findViewById(R.id.settingsUserDone);
		reminder_timer = (Button) findViewById(R.id.settings_reminder_time);
		reminder_date = (Button) findViewById(R.id.settings_reminder_date);		
		ReminderOnOff = (Switch) findViewById(R.id.settings_reminder_choose); 
		settings_reference_choose = (Switch) findViewById(R.id.settings_reference_choose); 
		settings_target_choose = (Switch) findViewById(R.id.settings_target_choose); 
		settings_cloth_choose = (Switch) findViewById(R.id.settings_cloth_choose); 
		
		settings_target_weight_val.setVisibility(View.GONE);
		settings_cloth_weight_val.setVisibility(View.GONE);
		Intent intent = this.getIntent();
		userID  = Long.parseLong(intent.getStringExtra("userID"));
		//Height  =  Long.parseLong(intent.getStringExtra("Height"));
		user = myOperatingTable.query(userID+"");
		
		//--------------reminder
		
		calendar = Calendar.getInstance();
		curYear = calendar.get(Calendar.YEAR); 
		curMonth = calendar.get(Calendar.MONTH); 
		curDay = calendar.get(Calendar.DAY_OF_MONTH);
        curhour=calendar.get(Calendar.HOUR_OF_DAY);//get(Calendar.HOUR)//12灏忔椂
        curminute=calendar.get(Calendar.MINUTE);//锟斤拷              
        cursecond=calendar.get(Calendar.SECOND);//锟斤拷 
        //NotifyDate = curYear + "-" + (curMonth+1) + "-" + curDay;
        if("kg".equals(user.get(0).getUserWeightUnit())){
			targetWeightUnit.setText("kg");
			clothWeightUnit.setText("kg");
		}else{
			targetWeightUnit.setText("lb");
			clothWeightUnit.setText("lb");
		}
		myVersion.setText(getVersion(this));
		NotifyLoop = new String[] {"", "", "", "", "", "", ""};
		UserSettingsRecord = myOperatingTable.getUserSetting(userID);
		if(UserSettingsRecord != null){
			float targetweightNum = UserSettingsRecord.getTargetWeight();
			float clothWeight = UserSettingsRecord.getClothweight();
			if(targetweightNum != 0 || clothWeight != 0){
				if("kg".equals(user.get(0).getUserWeightUnit())){
					targetWeightUnit.setText("kg");
					clothWeightUnit.setText("kg");
					Target_weight.setText(df.format(targetweightNum));
					//Log.e("cdf","---"+clothWeight);
					cloth_weight.setText(df.format(clothWeight));
				}else{
					Target_weight.setText(df.format(targetweightNum/0.45359237));
					//Log.e("cdf","==="+clothWeight/0.45359237);
					cloth_weight.setText(df.format(clothWeight/0.45359237));
					targetWeightUnit.setText("lb");
					clothWeightUnit.setText("lb");
				}
			}
			showWeightStatus = UserSettingsRecord.isGeneralOn();
			showTarget = UserSettingsRecord.isTargetlOn();
			showClothWeight = UserSettingsRecord.isClothlOn();
			if(showWeightStatus){
				settings_reference_choose.setChecked(true);
				
			}else{
				settings_reference_choose.setChecked(false);
			}
			if(showTarget){
				settings_target_choose.setChecked(true);
				settings_target_weight_val.setVisibility(View.VISIBLE);
			}else{
				settings_target_choose.setChecked(false);
				settings_target_weight_val.setVisibility(View.GONE);
			}
			if(showClothWeight){
				settings_cloth_choose.setChecked(true);
				settings_cloth_weight_val.setVisibility(View.VISIBLE);
			}else{
				settings_cloth_choose.setChecked(false);
				settings_cloth_weight_val.setVisibility(View.GONE);
			}
			NotifyOn = UserSettingsRecord.isNotifyOn();
			if(NotifyOn == true){ 
				settings_reminder_datetime_repeat.setVisibility(View.VISIBLE);
			}
        	NotifyTime = UserSettingsRecord.getNotifyTime();
			if(NotifyTime != null){
				//if("12".equals(timeFormat)){
					SimpleDateFormat dfformat = new SimpleDateFormat("hh:mm");
					Date d = null;
					try {
						d = dfformat.parse(NotifyTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					reminder_timer.setText(DateFormat.getTimeFormat(this.getApplicationContext()).format(d));
				/*}else{
					try {
						reminder_timer.setText(DateFormat.getTimeFormat(this.getApplicationContext()).parse(NotifyTime).toString());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}*/
			}
			NotifyDate = UserSettingsRecord.getNotifyDate();
			if(NotifyDate != null){
				//reminder_date.setText(NotifyDate);
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dfformat = new SimpleDateFormat("yyyy-MM-dd");
				Date d = null;
				try {
					d = dfformat.parse(NotifyDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c.setTime(d);
				reminder_date.setText(showingDateFormat.format(c.getTime()));
			}
			String[] settingLoop = UserSettingsRecord.getNotifyLoop();
			StringBuilder stringBuilder = new StringBuilder();   
			arrayFruitSelectedNum = 0;
			if (settingLoop != null && settingLoop.length > 0) {
				NotifyLoop = settingLoop;
				for (int i = 0; i < NotifyLoop.length; i++) { 
					if (!TextUtils.isEmpty(NotifyLoop[i]) && !NotifyLoop[i].equals("#"))  {
						stringBuilder.append(RepeatitemsShow[i] + " ");  
						//stringBuilder.append(RepeatitemsShow[i] + " ");  
						arrayFruitSelectedNum ++;
					} 
				}
			} 
			String a = stringBuilder.toString();
			//if(stringBuilder.toString().equals("Sun Mon Tue Wed Thu Fri Sat "))
			if(arrayFruitSelectedNum == 7)
				{
					RepeatDays.setText(R.string.settings_repeat_every);
					reminder_date.setVisibility(View.GONE);
				}
			//else if((stringBuilder.toString().equals("       "))||((stringBuilder.toString().equals(""))))
			else if(arrayFruitSelectedNum == 0)
				{
					RepeatDays.setText(R.string.settings_repeat_never);
					reminder_date.setVisibility(View.VISIBLE);
				}
			else
				{
					RepeatDays.setText(stringBuilder.toString());
					reminder_date.setVisibility(View.GONE);
				}
			ReminderOnOff.setChecked(NotifyOn);
			//}
			
		}else{
        	/*settings_reminder_repeat.setVisibility(View.GONE);	
        	settings_reminder_datetime.setVisibility(View.GONE);
        	reminder_line.setVisibility(View.GONE);	*/
			settings_reminder_datetime_repeat.setVisibility(View.GONE);
		}
		
		
		settings_target_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//Log.e("cdf", "changed true1");
					showTarget = true;
					settings_target_weight_val.setVisibility(View.VISIBLE);
				} else {
					//Log.e("cdf", "changed false1");
					showTarget = false;
					settings_target_weight_val.setVisibility(View.GONE);
				}
			}
		});
		
		settings_cloth_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//Log.e("cdf", "changed true1");
					showTarget = true;
					settings_cloth_weight_val.setVisibility(View.VISIBLE);
				} else {
					//Log.e("cdf", "changed false1");
					showTarget = false;
					settings_cloth_weight_val.setVisibility(View.GONE);
				}
			}
		});
		
		
		settings_reference_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					//Log.e("cdf", "changed true");
					showWeightStatus = true;
				} else {
					//Log.e("cdf", "changed false");
					showWeightStatus = false;
				}
			}  
		});
		ReminderOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
			              
			            @Override  
			            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
			                if(ReminderOnOff.isChecked())  
			                {
			                	NotifyOn = true;
//			                	reminder_date.setVisibility(View.VISIBLE);
//			                	reminder_timer.setVisibility(View.VISIBLE);
//			                	RepeatDays.setVisibility(View.VISIBLE);
			                	/*settings_reminder_repeat.setVisibility(View.VISIBLE);
			                	settings_reminder_datetime.setVisibility(View.VISIBLE);
			                	reminder_line.setVisibility(View.VISIBLE);
			                	*/
			                	settings_reminder_datetime_repeat.setVisibility(View.VISIBLE);
			                	calendar = Calendar.getInstance();
		                		curhour=calendar.get(Calendar.HOUR_OF_DAY);//get(Calendar.HOUR)//12灏忔椂
		    			        curminute=calendar.get(Calendar.MINUTE);//锟斤拷        
		    			        curYear = calendar.get(Calendar.YEAR); 
		    					curMonth = calendar.get(Calendar.MONTH); 
		    					curDay = calendar.get(Calendar.DAY_OF_MONTH);
			                	if(UserSettingsRecord == null || TextUtils.isEmpty( UserSettingsRecord.getNotifyTime())){
			                	
				                					                	
			    			        if(curminute<10){
			                    		   NotifyTime = curhour + ":0" + (curminute+1);
			                    	   }else{
			                    		   NotifyTime = curhour + ":" + (curminute+1);
			                    	   }
			    			        /*if("12".equals(timeFormat)){
			    						SimpleDateFormat dfformat = new SimpleDateFormat("hh:mm");
			    						Date d = null;
			    						try {
			    							d = dfformat.parse(NotifyTime);
			    						} catch (ParseException e) {
			    							// TODO Auto-generated catch block
			    							e.printStackTrace();
			    						}
			    						reminder_timer.setText(DateFormat.getTimeFormat(this.getApplicationContext()).format(d));
			    					}else{
			    						try {
			    							reminder_timer.setText(DateFormat.getTimeFormat(this.getApplicationContext()).parse(NotifyTime).toString());
			    						} catch (ParseException e1) {
			    							// TODO Auto-generated catch block
			    							e1.printStackTrace();
			    						}
			    					}*/
			    					
			    					if((curMonth+1) < 10)
			    			        	NotifyDate = curYear + "-0" + (curMonth+1);
			    			        else
			    			        	NotifyDate = curYear + "-" + (curMonth+1);
			    			        if(curDay<10)
			    			        	NotifyDate = NotifyDate + "-0" + curDay;
			    			        else
			    			        	NotifyDate = NotifyDate + "-" + curDay;
			    			        //reminder_date.setText(NotifyDate);
			    						Calendar c = Calendar.getInstance();
			    						SimpleDateFormat dfformat = new SimpleDateFormat("yyyy-MM-dd");
			    						Date d = null;
			    						try {
			    							d = dfformat.parse(NotifyDate);
			    						} catch (ParseException e) {
			    							// TODO Auto-generated catch block
			    							e.printStackTrace();
			    						}
			    						c.setTime(d);
			    						reminder_date.setText(showingDateFormat.format(c.getTime()));
			    						reminder_timer.setText(DateFormat.getTimeFormat(getApplicationContext()).format(d));
			                	}
			                	//ReminderOnOff.setChecked(NotifyOn);
			                }else {  
			                	NotifyOn = false;

//			                	reminder_date.setVisibility(View.GONE);
//			                	reminder_timer.setVisibility(View.GONE);
//			                	RepeatDays.setVisibility(View.GONE);	
			                	/*settings_reminder_repeat.setVisibility(View.GONE);	
			                	settings_reminder_datetime.setVisibility(View.GONE);
			                	reminder_line.setVisibility(View.GONE);	*/	
			                	settings_reminder_datetime_repeat.setVisibility(View.GONE);
			    				//reminder_timer.setText("");
			    				//reminder_date.setText("");
			    				
			    				//RepeatDays.setText("Never");
			    				//ReminderOnOff.setChecked(NotifyOn);
			                }  
			                  
			            }  
			        }); 
		
        builder = new AlertDialog.Builder(this);
        
        
        reminder_date.setOnClickListener(this);
		reminder_timer.setOnClickListener(this);  
        settings_reminder_repeat.setOnClickListener(this);
        settings_product_tour.setOnClickListener(this);
        settingsUserDone.setOnClickListener(this);	
	}

	public static String getVersion(Context context)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return context.getString(R.string.version_unknown);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.settingsUserDone:
			float TargetweightNum = Float.parseFloat(Target_weight.getText().toString());
			float clothWeight = Float.parseFloat(cloth_weight.getText().toString());
			if (UserSettingsRecord  == null) {
				UserSettingsRecord = new UserSettings();
			}
			//Log.e("cdf","--------"+clothWeight);
			//Log.e("cdf","set weight -- "+TargetweightNum);
			if("kg".equals(user.get(0).getUserWeightUnit())){
				UserSettingsRecord.setTargetWeight(TargetweightNum);
				UserSettingsRecord.setClothweight(clothWeight);
				targetWeightUnit.setText("kg");
				clothWeightUnit.setText("kg");
			}else{
				//TargetweightNum = (float) (TargetweightNum/0.45359237);
				UserSettingsRecord.setTargetWeight((float) (TargetweightNum*0.45359237));
				UserSettingsRecord.setClothweight((float) (clothWeight*0.45359237));
				targetWeightUnit.setText("lb");
				clothWeightUnit.setText("lb");
			}
			
			//UserSettingsRecord = new UserSettings();
			//UserSettingsRecord.setId(10);
			UserSettingsRecord.setUserId(userID);
			//UserSettingsRecord.setTargetWeight(TargetweightNum);
			//Log.e("cdf", "show showTarget============= "+showTarget);
			//Log.e("cdf", "show showWeightStatus============= "+showWeightStatus);
			UserSettingsRecord.setClothlOn(showClothWeight);
			UserSettingsRecord.setTargetlOn(showTarget);
			UserSettingsRecord.setGeneralOn(showWeightStatus);
			UserSettingsRecord.setNotifyOn(NotifyOn);
			UserSettingsRecord.setNotifyTime(NotifyTime);
			UserSettingsRecord.setNotifyLoop(NotifyLoop);
			//UserSettingsRecord.setCurrentHeight(Height);
			UserSettingsRecord.setNotifyDate(NotifyDate);
			UserSettingsRecord.setNotifyFeature(NotifyFeature);
			
			myOperatingTable.addUserSettings(UserSettingsRecord);
			finish();
			
			Intent intent = new Intent();
	        intent.setAction("com.idt.bw.activity.AlarmService");
	        startService(intent);
			break;
		case R.id.settings_product_tour:
			Intent intent1 = new Intent(SettingsActivity.this,GuideActivity.class);  
            startActivity(intent1);
			break;
		case R.id.settings_reminder_repeat:
			//--------------Repeat
			arrayFruitSelectedNum = 0;
			//final String[] Repeatitems = {"Every Sunday","Every Monday","Every Tuesday","Every Wednesday","Every Thursday","Every Friday","Every Saturday",};//{"EveryDay","EveryWeek","Never"};
			//final boolean[] arrayFruitSelected = new boolean[] {false, false, false, false, false, false, false};
			for (int i = 0; i < NotifyLoop.length; i++) { 
				arrayFruitSelected[i] = (NotifyLoop[i].matches("\\d"));
			}
			
			builder.setTitle(getResources().getString(R.string.choose_sure_cancel))
			.setMultiChoiceItems(Repeatitems,arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {					
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						arrayFruitSelected[which] = isChecked;  							
					}
				});		
			builder.setPositiveButton(getResources().getString(R.string.choose_sure), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StringBuilder stringBuilder = new StringBuilder();   
					for (int i = 0; i < arrayFruitSelected.length; i++) {   
						if (arrayFruitSelected[i] == true)   
						{   
							//stringBuilder.append(RepeatitemsShow[i] + " ");
							stringBuilder.append(RepeatitemsShow[i] + " ");							
							NotifyLoop[i] = i + "";
							arrayFruitSelectedNum++;
						}
						else
							NotifyLoop[i] = "#";
					} 
					//if(stringBuilder.toString().equals("Sun Mon Tue Wed Thu Fri Sat "))
					if(arrayFruitSelectedNum == 7)
						{
							RepeatDays.setText("Every day");
							reminder_date.setVisibility(View.GONE);
						}
					//else if((stringBuilder.toString().equals("       "))||((stringBuilder.toString().equals(""))))
					else if(arrayFruitSelectedNum == 0)
						{
							RepeatDays.setText("Never");
							reminder_date.setVisibility(View.VISIBLE);
						}
					else
						{
							RepeatDays.setText(stringBuilder.toString());
							reminder_date.setVisibility(View.GONE);
						}
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.choose_cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Toast.makeText(SettingsActivity.this, "锟斤拷选锟斤拷锟斤拷取锟斤拷钮锟斤拷", Toast.LENGTH_SHORT).show();
				}
			});
			builder.show();
			break;
		case R.id.settings_reminder_date:
			Date d2 = null;
			try {
				d2 = showingDateFormat.parse(reminder_date.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if (d2 == null) {
				d2 = new Date();
			}
			Calendar c2 = Calendar.getInstance(); 
			c2.setTime(d2);
			new DatePickerDialog(SettingsActivity.this,  
			           new DatePickerDialog.OnDateSetListener() {
			               @Override  
			               public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { 
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
								reminder_date.setText(showingDateFormat.format(c.getTime()));
			            	   //reminder_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
			            	   //NotifyDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
			            	   if((monthOfYear+1) < 10)
			                   	NotifyDate = year + "-0" + (monthOfYear+1);
			                   else
			                   	NotifyDate = year + "-" + (monthOfYear+1);
			                   if(curDay<10)
			                   	NotifyDate = NotifyDate + "-0" + dayOfMonth;
			                   else
			                   	NotifyDate = NotifyDate + "-" + dayOfMonth;
			            	   //NotifyTimer = NotifyDate + " " + NotifyTime;
			               }  
			           }  
			           , c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH)).show();  
			break;
		 
		case R.id.settings_reminder_time:
			Date d1 = null;
			try {
				//Log.e("cdf","---------"+reminder_timer.getText().toString());
				d1 = DateFormat.getTimeFormat(getApplicationContext()).parse(reminder_timer.getText().toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			Calendar c1 = Calendar.getInstance(); 
			if(d1!=null){
				c1.setTime(d1);
			}
           new TimePickerDialog(SettingsActivity.this,  
                   new TimePickerDialog.OnTimeSetListener() {  
                       @Override  
                       public void onTimeSet(TimePicker view,  
                               int hourOfDay, int minute) {  
                    	   if(minute<10){
                    		   NotifyTime = hourOfDay + ":0" + minute;
                    	   }else{
                    		   NotifyTime = hourOfDay + ":" + minute;
                    	   }
                    	   //if("12".equals(timeFormat)){
	           					SimpleDateFormat dfformat = new SimpleDateFormat("HH:mm");
	           					Date d = null;
	           					try {
	           						d = dfformat.parse(NotifyTime);
	           					} catch (ParseException e) {
	           						// TODO Auto-generated catch block
	           						e.printStackTrace();
	           					}
	           					reminder_timer.setText(DateFormat.getTimeFormat(getApplicationContext()).format(d));
	           				/*}else{
	           					Log.e("cdf","ddddd===="+NotifyTime);
	           					try {
	           						Log.e("cdf","ddddd======"+DateFormat.getTimeFormat(getApplicationContext()).parse(NotifyTime).toString());
	           						reminder_timer.setText(DateFormat.getTimeFormat(getApplicationContext()).parse(NotifyTime).toString());
	           					} catch (ParseException e1) {
	           						// TODO Auto-generated catch block
	           						e1.printStackTrace();
	           					}
	           				}*/
                       }  
                   }  
                   , c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), true).show();  
			break;
		default:
			break;
		}
	}
}
