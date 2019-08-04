/**
 * 
 */
package com.oregonscientific.bbq.act;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oregonscientific.bbq.R;
import com.oregonscientific.bbq.R.drawable;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.dao.DatabaseManager;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.history.WriteReadTempFile;
import com.oregonscientific.bbq.utils.BbqConfig;
import com.oregonscientific.bbq.view.CalendarFilperView;
import com.oregonscientific.bbq.view.CalendarFilperView.OnDateTapListener;
import com.oregonscientific.bbq.view.CalendarFilperView.OnMonthChangedListener;
import com.oregonscientific.bbq.view.DonenessCoordinate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author zjz
 *
 */
public class HistoryCurveActivity extends Activity {
		DatabaseManager myDatabaseManager;
		private SharingPreferenceDao dao;
		private String[] donenessLevelStrs1;
		private DonenessCoordinate coordinate;
		private static final String TEMP_MEMO="temp_memo";
		private EditText EditText01;
		private SharedPreferences pre;
		private String MEMOcontent;
		private int currentMode;
		private long Current_ID;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
        setContentView(R.layout.history2d);
        TextView textView2,textView4,textView5;
        ImageView imageView3;
        int cost_time;
        
        myDatabaseManager = DatabaseManager.instance(this);
        dao = SharingPreferenceDao.getInstance(this);
        donenessLevelStrs1 = getResources().getStringArray(R.array.donenessLevelStrs);
        
        imageView3 = (ImageView) this.findViewById(R.id.imageView3);
        textView2 = (TextView) this.findViewById(R.id.textView2);
        textView4 = (TextView) this.findViewById(R.id.textView4);
        textView5 = (TextView) this.findViewById(R.id.textView5);
        EditText01 = (EditText) this.findViewById(R.id.EditText01);
        coordinate = (DonenessCoordinate) findViewById(R.id.DonenessCoordinate1);        
        
        //	cursor	---------------------------------------------------------
        Intent intent = this.getIntent();
        Current_ID =  intent.getLongExtra("temp_ID",0);
        int year = intent.getIntExtra("intentyear", 0);
        int month = intent.getIntExtra("intentmonth", 0);
        int dayOfMonth = intent.getIntExtra("intentdayOfMonth", 0);
        
        Cursor cursor = myDatabaseManager.queryRecordForOneday(year,month,dayOfMonth);
        Cursor cursortemp = cursor;
        cursortemp.moveToFirst();
        long first_ID = cursortemp.getLong(0);
        cursor.moveToPosition((int)(Current_ID-first_ID));
        //float finished_temperature = myCursor.getFloat(myCursor.getColumnIndex("finished_temperature"));  
        currentMode = cursor.getInt(cursor.getColumnIndex("mode"));
    	switch (currentMode) {
    	case 1://MEAN_TYPE_MODE
    		int[] meattypeIconId = ParseManager.MEAT_TYPE_ICONS;
    		int MeatType = cursor.getInt(cursor.getColumnIndex("set_meattype"));
    		imageView3.setImageResource(meattypeIconId[MeatType]); 
    		
    		String set_donenesslevel = cursor.getString(cursor.getColumnIndex("set_donenesslevel"));
    		textView4.setText(donenessLevelStrs1[Integer.parseInt(set_donenesslevel)]);
    		
    		cost_time = cursor.getInt(cursor.getColumnIndex("cost_time"));
    		textView5.setText(countToString1(cost_time));  
    		break;
    	case 2://TARGET_TEMPERATURE_MODE
    		imageView3.setImageResource(R.drawable.icon_temp);        		
    		float set_targe_temperature = cursor.getFloat(cursor.getColumnIndex("set_targe_temperature"));        		
    		String str = "";
    		if (dao.getShowingTemperatureUnit().endsWith(BbqConfig.TEMPERATURE_UNIT_C)) {
    			set_targe_temperature = ParseManager.tranFahrenheitToCelsius(set_targe_temperature);
    			str = String.format("%.0f%s", set_targe_temperature, getResources().getString(R.string.tempc));
    		} else if (dao.getShowingTemperatureUnit().endsWith(BbqConfig.TEMPERATURE_UNIT_F)) {
    			str = String.format("%.0f%s", set_targe_temperature, getResources().getString(R.string.tempf));
    		}
    		textView4.setText(str);
    	//	textView4.setText(set_targe_temperature + "℉");
    		//textView4.setText(set_targe_temperature + "℃");
    		
    		cost_time = cursor.getInt(cursor.getColumnIndex("cost_time"));
    		textView5.setText(countToString1(cost_time));  
    		break;
    	case 3://TIMER_MODE 	
    		imageView3.setImageResource(R.drawable.icon_timer);        	
    		int set_totaltime = cursor.getInt(cursor.getColumnIndex("set_totaltime"));
    		textView4.setText(countToString1(set_totaltime));
    		
    		cost_time = cursor.getInt(cursor.getColumnIndex("cost_time"));
    		cost_time = set_totaltime - cost_time;
    		textView5.setText(countToString1(cost_time));
    		break;
    	default:
    		break;
    	}   	
    	
    	long finished_date = cursor.getLong(cursor.getColumnIndex("finished_date"));			
		Date SecondcurDate = new  Date(finished_date);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String datetime = dateFormat.format(SecondcurDate);
		textView2.setText(datetime);	
		
		     
		
		/*/MEMO-----------------------------------------------------
		pre = getSharedPreferences(TEMP_MEMO, MODE_WORLD_READABLE);
		MEMOcontent = pre.getString(Long.toString(Current_ID), "");
        EditText01.setText(MEMOcontent);
		*/
		EditText01.setOnKeyListener(new EditText.OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2)
			{
				// TODO Auto-generated method stub
				myDatabaseManager.writeMemoForRecord(Current_ID,EditText01.getText().toString());
				return false;
			}
		});
		
		MEMOcontent = cursor.getString(cursor.getColumnIndex("memo"));
		EditText01.setText(MEMOcontent);
		
		
		// coordinate = new DonenessCoordinate(this);----------------------------
        String temppath = cursor.getString(cursor.getColumnIndex("temperatures_file"));
        WriteReadTempFile writeReadTempFile = new WriteReadTempFile();	
        ArrayList<Float> Alltemp1 = writeReadTempFile.readTempFromFile(temppath);
        // modified by aaronli at Mar 21 2014. Fixed none pointer exception when return null temperatures array.
        if (Alltemp1 != null) {
        	float[] Alltemp = new float[Alltemp1.size()];
        	for(int i=0; i < Alltemp1.size();i++)
        	{
        		Alltemp[i] = Alltemp1.get(i);
        	}
        	coordinate.setTemperatures(Alltemp);
        }
        coordinate.setTemperatureUnit(dao.getShowingTemperatureUnit());
        /*�Զ���ֵ*/
		/*coordinate.setTemperatures(new float[]{
				100f, 115f, 130f, 145f, 155.f,  164.5f, 168.f, 172.f, 
				150f, 144f, 130f, 118f, 98f, 84f,
				70f, 45f, 39f, 12f, 50f, 80f, 94f, 120f, 
				180f, 185f, 190f, 200f, 205f, 230f, 234f,238.5f, 241.5f, 245.f, 249.5f, 253.f,258.f,
				280f, 300f, 332f, 360f
		});*/
		coordinate.setPadding(50, 100, 50, 100);
		if (currentMode == Mode.MEAN_TYPE_MODE.ordinal()){
			Resources res = getResources();
			int[] colors = new int[] { 
					res.getColor(R.color.doneness_rare), 
					res.getColor(R.color.doneness_rare), 
					res.getColor(R.color.doneness_mediumrare),
					res.getColor(R.color.doneness_medium),
					res.getColor(R.color.doneness_mediumwell),
					res.getColor(R.color.doneness_welldone),
					res.getColor(R.color.doneness_overdone )};
			
			float[] temperatures = new float[] {cursor.getFloat(cursor.getColumnIndex("set_doneness_r")), 
					cursor.getFloat(cursor.getColumnIndex("set_doneness_mr")), 
					cursor.getFloat(cursor.getColumnIndex("set_doneness_m")), 
					cursor.getFloat(cursor.getColumnIndex("set_doneness_mw")), 
					cursor.getFloat(cursor.getColumnIndex("set_doneness_w")), 											
			};
			int j;
			for(int i=0; i<5; i++){
				if(temperatures[i] == 0){
					for(j=0; j<(4-i); j++){
						temperatures[j] = temperatures[j+1];
						colors[j+1] = colors[j+1+1];
					}
					colors[j+1] = colors[j+1+1];	//doneness_overdone
				}
			}
			
			//float[] temperatures = new float[] {135f, 140f, 145f, 160f, 170f};//�Զ���ֵ
			coordinate.setDonenessColors(colors, temperatures);
		} else {
			coordinate.setDonenessColors(null, null);
		}
		
	} 
	/*
	@Override
	protected void onStop() {
    	super.onStop();
    	SharedPreferences.Editor editor = getSharedPreferences(TEMP_MEMO, MODE_WORLD_WRITEABLE).edit();

    	editor.putString(Long.toString(Current_ID), EditText01.getText().toString());
        editor.commit();

    	} 
*/

	public String countToString1(int count){
		String min = (count/60)+" m ";
		String sec = (count%60)+" s";
		return min+sec;
	}

    
  
}


