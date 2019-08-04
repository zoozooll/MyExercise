package com.idt.bw.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.idt.bw.bean.Record;
import com.idt.bw.bean.User;
import com.idt.bw.bean.UserSettings;
import com.idt.bw.bean.UserSettings.NotifyLoopMode;
import com.idt.bw.database.OperatingTable;
import com.idt.bw.utils.DateManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
public class DataInputActivity extends Activity {
	private Calendar calendar ;
	private int curYear,curMonth,curDay,curhour,curminute,cursecond;
	private Button input_date;
	private OperatingTable myOperatingTable;
	private long userID;
	private float Height, targetWeight;
	private String curDate;
	private Record myRecord;
	private float inputweight,inputherght;
	private EditText input_Weight_date2;
	private EditText input_height_date2;
	private TextView input_weight_unit,input_height_unit;
	private TextView DataInputDone;
	private ImageButton inputL,inputR;
	private ArrayList<User> user;
	private DecimalFormat df;
	private java.text.DateFormat showingDateFormat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_input);
		
		df = new DecimalFormat("#0.0");
		showingDateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG);
		myOperatingTable = OperatingTable.instance(this);
		Intent intent = this.getIntent();
		userID  = Long.parseLong(intent.getStringExtra("userID"));
		Height  =  Float.parseFloat(intent.getStringExtra("Height"));
		//Log.e("cdf","Height value ==================== "+Height);
		
		targetWeight = intent.getFloatExtra("targetWeight", 0);
		input_Weight_date2 =  (EditText) findViewById(R.id.input_data_date2);			
		input_height_date2 =  (EditText) findViewById(R.id.input_data_date3);
		inputL = (ImageButton) findViewById(R.id.inputL);
		inputR = (ImageButton) findViewById(R.id.inputR);
		DataInputDone = (TextView) findViewById(R.id.inputDataDone);	
		input_weight_unit = (TextView) findViewById(R.id.input_weight_unit);
		input_height_unit = (TextView) findViewById(R.id.input_height_unit);
		
		input_date = (Button) findViewById(R.id.input_data_date1);		
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		curYear = calendar.get(Calendar.YEAR); 
		curMonth = calendar.get(Calendar.MONTH); 
		curDay = calendar.get(Calendar.DAY_OF_MONTH);
        curhour=calendar.get(Calendar.HOUR);//Сʱ   
        curminute=calendar.get(Calendar.MINUTE);//��              
        cursecond=calendar.get(Calendar.SECOND);//�� 
        /*if((curMonth+1) < 10)
        	curDate = curYear + "-0" + (curMonth+1);
        else
        	curDate = curYear + "-" + (curMonth+1);
        if(curDay<10)
        	curDate = curDate + "-0" + curDay;
        else
        	curDate = curDate + "-" + curDay;*/
        curDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        //curDate = String.valueOf(curYear) + String.valueOf(curMonth+1) + String.valueOf(curDay);
        //curDate = "2014-03-28";
        myRecord = myOperatingTable.getSingleRecordFromDate(userID,curDate);
        
        user = myOperatingTable.query(String.valueOf(userID));
        if(myRecord != null){
        	if(myRecord.getId() != 0){
        		showWeightHeight();
            }
        }else{
        	showWeightHeightelse();
        }
        
        
        //NotifyTimer = NotifyDate + " " + NotifyTime;
        //input_date.setText(curDate);	
        input_date.setText(showingDateFormat.format(calendar.getTime()));		
        input_date.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{ 
				Date d2 = null;
				try {
					d2 = showingDateFormat.parse(input_date.getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				Calendar c2 = Calendar.getInstance(); 
				c2.setTime(d2);
				   new DatePickerDialog(DataInputActivity.this,  
				           new DatePickerDialog.OnDateSetListener() {
				               @Override  
				               public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {   
				            	   //input_date.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
				            	   //curDate = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
				            	   curYear = year; curMonth = monthOfYear; curDay = dayOfMonth;
				            	   calendar.set(year, monthOfYear, dayOfMonth);
				            	  /* if((monthOfYear+1) < 10)
				                   	curDate = year + "-0" + (monthOfYear+1);
				                   else
				                   	curDate = year + "-" + (monthOfYear+1);
				                   if(curDay<10)
				                   	curDate = curDate + "-0" + dayOfMonth;
				                   else
				                   	curDate = curDate + "-" + dayOfMonth;*/
				                   //input_date.setText(curDate);
				            	   curDate = showingDateFormat.format(calendar.getTime()).toString();
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
									input_date.setText(showingDateFormat.format(c.getTime()));
				                   myRecord = myOperatingTable.getSingleRecordFromDate(userID,curDate);
				                   if(myRecord != null){
				                	   showWeightHeight();
				                   }else{
				                	   showWeightHeightelse();
				                   }

				               }  
				           }  
				           , c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH)).show();  


			}
		});
        
      //--------------Done
        
        DataInputDone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Record inputRecord = new Record();
				inputweight = Float.parseFloat(input_Weight_date2.getText().toString());
				inputherght = Float.parseFloat(input_height_date2.getText().toString());
				if("kg".equals(user.get(0).getUserWeightUnit())){
					//inputweight = Float.parseFloat(input_Weight_date2.getText().toString());
					inputRecord.setWeight(inputweight);	
		        	input_weight_unit.setText("kg");
				}else{
					inputweight = (float) (inputweight*0.45359237);
					inputRecord.setWeight(inputweight);	
		        	input_weight_unit.setText("lb");
				}
				if("cm".equals(user.get(0).getUserHeightUnit())){
					//inputherght = Float.parseFloat(input_height_date2.getText().toString());
					inputRecord.setCurrentHeight(inputherght);
					input_height_unit.setText("cm");
				}else{
					inputherght = (float) (inputherght*30.48);
					inputRecord.setCurrentHeight(inputherght);
					input_height_unit.setText("ft");
				}
				inputRecord.setUserId(userID);
				inputRecord.setDatetime(DateFormat.format("yyyy-MM-dd", calendar).toString());
				inputRecord.setTargetWeight(targetWeight);
				myOperatingTable.updateRecordInput(inputRecord);
				finish();
			}
		});	
        inputL.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
				calendar.add(Calendar.DATE, -1); 
				curYear = calendar.get(Calendar.YEAR); 
				curMonth = calendar.get(Calendar.MONTH); 
				curDay = calendar.get(Calendar.DAY_OF_MONTH);
		       /* if((curMonth+1) < 10)
		        	curDate = curYear + "-0" + (curMonth+1);
		        else
		        	curDate = curYear + "-" + (curMonth+1);
		        if(curDay<10)
		        	curDate = curDate + "-0" + curDay;
		        else
		        	curDate = curDate + "-" + curDay;*/
				curDate = showingDateFormat.format(calendar.getTime());
				//input_date.setText(curDate);
		        input_date.setText(curDate);	
                myRecord = myOperatingTable.getSingleRecordFromDate(userID,curDate);
                if(myRecord != null){
                	showWeightHeight();
                }else{
                	showWeightHeightelse();
                }
			}
		});	
        inputR.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				calendar.add(Calendar.DATE, +1); 
				curYear = calendar.get(Calendar.YEAR); 
				curMonth = calendar.get(Calendar.MONTH); 
				curDay = calendar.get(Calendar.DAY_OF_MONTH);
		        /*if((curMonth+1) < 10)
		        	curDate = curYear + "-0" + (curMonth+1);
		        else
		        	curDate = curYear + "-" + (curMonth+1);
		        if(curDay<10)
		        	curDate = curDate + "-0" + curDay;
		        else
		        	curDate = curDate + "-" + curDay;*/
				//input_date.setText(curDate);
				curDate = showingDateFormat.format(calendar.getTime()).toString();
		        input_date.setText(curDate);	
                myRecord = myOperatingTable.getSingleRecordFromDate(userID,curDate);
                if(myRecord != null){
                	showWeightHeight();
                }else{
                	showWeightHeightelse();
                }
			}
		});	
	}
	
	public void showWeightHeight(){
		if("kg".equals(user.get(0).getUserWeightUnit())){
			input_Weight_date2.setText(myRecord.getWeight()+"");
        	input_weight_unit.setText("kg");
		}else{
			input_Weight_date2.setText(df.format(myRecord.getWeight()*0.45359237));
        	input_weight_unit.setText("lb");
		}
		if("cm".equals(user.get(0).getUserHeightUnit())){
			input_height_date2.setText(myRecord.getCurrentHeight() + "");
			input_height_unit.setText("cm");
		}else{
			input_height_date2.setText(df.format((myRecord.getCurrentHeight())/30.48));
			input_height_unit.setText("ft");
		}
	}
	public void showWeightHeightelse(){
		input_Weight_date2.setText("0.0");
    	if("kg".equals(user.get(0).getUserWeightUnit())){
        	input_weight_unit.setText("kg");
		}else{
        	input_weight_unit.setText("lb");
		}
		if("cm".equals(user.get(0).getUserHeightUnit())){
			input_height_date2.setText(Height + "");
			input_height_unit.setText("cm");
		}else{
			input_height_date2.setText(df.format(Height/30.48));
			input_height_unit.setText("ft");
		}
	}

}
