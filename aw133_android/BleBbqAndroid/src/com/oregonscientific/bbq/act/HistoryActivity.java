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
import com.oregonscientific.bbq.ble.ParseManager;
import com.oregonscientific.bbq.dao.DatabaseManager;
import com.oregonscientific.bbq.dao.SharingPreferenceDao;
import com.oregonscientific.bbq.drawer.NavDrawerItem;
import com.oregonscientific.bbq.drawer.NavDrawerListAdapter;
import com.oregonscientific.bbq.utils.BbqConfig;
import com.oregonscientific.bbq.view.CalendarFilperView;
import com.oregonscientific.bbq.view.CalendarFilperView.OnDateTapListener;
import com.oregonscientific.bbq.view.CalendarFilperView.OnMonthChangedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
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
public class HistoryActivity extends Activity implements OnClickListener,OnItemClickListener{
	//protected static final String Context = null;
	private String[] donenessLevelStrs1;
	private ListView listView; 
	private CalendarFilperView cfv;
	Cursor myCursor;
	int intentyear;
	int intentmonth;
	int intentdayOfMonth;
	private DatabaseManager myDatabaseManager;
	private SharingPreferenceDao dao;

	private DrawerLayout drawer;
	private ImageView menuimg;
	private ListView left_drawer;
	private String[] mNavMenuTitles;
	private TypedArray mNavMenuIconsTypeArray;
	private List<NavDrawerItem> mNavDrawerItems;
	private NavDrawerListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_history);
        myDatabaseManager = DatabaseManager.instance(this);
        donenessLevelStrs1 = getResources().getStringArray(R.array.donenessLevelStrs);
        
        cfv = (CalendarFilperView) findViewById(R.id.calendarView1);
        
        Calendar cal = Calendar.getInstance();  
        int year = cal.get(Calendar.YEAR);//��ȡ���  
        int month=cal.get(Calendar.MONTH)+ 1;//��ȡ�·�   
        int day=cal.get(Calendar.DATE);//��ȡ��   
        int hour=cal.get(Calendar.HOUR);//Сʱ   
        int minute=cal.get(Calendar.MINUTE);//��              
        int second=cal.get(Calendar.SECOND);//��   
        intentyear = year; intentmonth = month; intentdayOfMonth = day;
        Set<Integer> days = new HashSet<Integer>();
        days = myDatabaseManager.getDaysInRecorded(year,month);
        
        /*
        Set<Integer> days = new HashSet<Integer>();
        days.add(1);
        days.add(10);
        days.add(17);
        days.add(31);
        */
        //if(days != null)
        cfv.setTimeStampsByRange(days);
        
        cfv.setOnDateTapListener(listenerDate);
        cfv.setOnMonthChangedListener(listenerMonth);
        listView=(ListView) findViewById(R.id.history_ListView);
        
        listView.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
            	
                //Toast.makeText(HistoryActivity.this, "�����" + arg2 + "����Ŀ",Toast.LENGTH_LONG).show();
                long temp_ID = myCursor.getLong(0);                
    			Intent intent = new Intent(HistoryActivity.this, HistoryCurveActivity.class);    			
    			intent.putExtra("temp_ID", temp_ID);
    			intent.putExtra("intentyear", intentyear);
    			intent.putExtra("intentmonth", intentmonth);
    			intent.putExtra("intentdayOfMonth", intentdayOfMonth);
    	        startActivity(intent);
            }  
        });  
        dao = SharingPreferenceDao.getInstance(this);
        
        { //显示当天listview       	
        	myCursor = myDatabaseManager.queryRecordForOneday(year,month,day);
            MyCursorAdapter listCursorAdpter =new MyCursorAdapter(HistoryActivity.this, myCursor);
            listView.setAdapter(listCursorAdpter);       	
        	
        }
        
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuimg = (ImageView) findViewById(R.id.menuimg);
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        
        mNavMenuTitles = getResources().getStringArray(R.array.nav_drawer_title);
     	mNavMenuIconsTypeArray = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mNavDrawerItems = new ArrayList<NavDrawerItem>();
        
        mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[0], mNavMenuIconsTypeArray.getResourceId(0, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[1], mNavMenuIconsTypeArray.getResourceId(1, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[2], mNavMenuIconsTypeArray.getResourceId(2, -1)));
		mNavDrawerItems.add(new NavDrawerItem(mNavMenuTitles[3], mNavMenuIconsTypeArray.getResourceId(3, -1)));
		
		mNavMenuIconsTypeArray.recycle();
		mAdapter = new NavDrawerListAdapter(getApplicationContext(),mNavDrawerItems);
		TextView tv = new TextView(getApplicationContext());
		tv.setText(R.string.munu_drawerlayout);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setBackgroundResource(R.drawable.actionbarbg);
		left_drawer.addHeaderView(tv,null,false);
		left_drawer.setAdapter(mAdapter);
		left_drawer.setOnItemClickListener(this);
		
		menuimg.setOnClickListener(this);
	}
  
    
    OnDateTapListener listenerDate = new OnDateTapListener() {
        @Override
        public void onTabDate(CalendarFilperView view, int year, int month,int dayOfMonth) {
            // TODO Auto-generated method stub  显示某一天的listview   
        	intentyear = year; intentmonth = month; intentdayOfMonth = dayOfMonth;
        	myCursor = myDatabaseManager.queryRecordForOneday(year,month,dayOfMonth);
            MyCursorAdapter listCursorAdpter =new MyCursorAdapter(HistoryActivity.this, myCursor);
            listView.setAdapter(listCursorAdpter); 
        }
    };
    

    OnMonthChangedListener listenerMonth = new OnMonthChangedListener() {
        @Override
        public void onFocusMonthChange(CalendarFilperView view, int year, int month) {
            // TODO Auto-generated method stub 显示一个月有记录的标志       	
            Set<Integer> days = myDatabaseManager.getDaysInRecorded(year,month + 1);
            cfv.setTimeStampsByRange(days);
            myCursor = null;
            MyCursorAdapter listCursorAdpter =new MyCursorAdapter(HistoryActivity.this, myCursor);
            listView.setAdapter(listCursorAdpter); 
		}    

    };

    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

    class MyCursorAdapter extends CursorAdapter {
		private Context context;
		private Cursor c;

		public MyCursorAdapter(Context context, Cursor c) {
			super(context, c);
			this.context = context;
			this.c = c;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder vh = (ViewHolder) view.getTag();
			int currentMode = cursor.getInt(cursor.getColumnIndex("mode"));
        	switch (currentMode) {
        	case 1://MEAN_TYPE_MODE
        		int[] meattypeIconId = ParseManager.MEAT_TYPE_ICONS;
        		int MeatType = cursor.getInt(cursor.getColumnIndex("set_meattype"));
        		vh.img.setImageResource(meattypeIconId[MeatType]); 
        		
        		String set_donenesslevel = cursor.getString(cursor.getColumnIndex("set_donenesslevel"));
        		vh.title1.setText(donenessLevelStrs1[Integer.parseInt(set_donenesslevel)]);
        		break;
        	case 2://TARGET_TEMPERATURE_MODE
        		vh.img.setImageResource(R.drawable.icon_temperature);        		
        		float set_targe_temperature = cursor.getFloat(cursor.getColumnIndex("set_targe_temperature"));   
        		// modified by aaronli at Mar 19, 2014. For showing in C/F temperature unit changing.
        		String str = "";
        		if (dao.getShowingTemperatureUnit().endsWith(BbqConfig.TEMPERATURE_UNIT_C)) {
        			set_targe_temperature = ParseManager.tranFahrenheitToCelsius(set_targe_temperature);
        			str = String.format("%.0f%s", set_targe_temperature, getResources().getString(R.string.tempc));
        		} else if (dao.getShowingTemperatureUnit().endsWith(BbqConfig.TEMPERATURE_UNIT_F)) {
        			str = String.format("%.0f%s", set_targe_temperature, getResources().getString(R.string.tempf));
        		}
        		vh.title1.setText(str);
        		//vh.title1.setText(set_targe_temperature + "��");
        		break;
        	case 3://TIMER_MODE
        		vh.img.setImageResource(R.drawable.icon_timer);
        		
        		int set_totaltime = cursor.getInt(cursor.getColumnIndex("set_totaltime"));
        		vh.title1.setText(countToString1(set_totaltime));
        		break;
        	default:
        		break;
        	}
        	
        	
        	long finished_date = cursor.getLong(cursor.getColumnIndex("finished_date"));			
			Date SecondcurDate = new  Date(finished_date);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			String datetime = dateFormat.format(SecondcurDate);
			if(SecondcurDate.getHours() < 12)
				vh.title2.setText(datetime + "am");
			else 
				vh.title2.setText(datetime + "pm");
		}
		
		public String countToString1(int count){
			String min = (count/60)+" m ";
			String sec = (count%60)+" s";
			return min+sec;
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.history_list_items, parent, false);
			ViewHolder v = new ViewHolder();
			v.img = (ImageView) view.findViewById(R.id.history_img);
			v.title1 = (TextView) view.findViewById(R.id.history_line1);
			v.title2 = (TextView) view.findViewById(R.id.history_line2);
			view.setTag(v);
			return view;
		}
	}

	class ViewHolder {
        public ImageView img;  
        public TextView title1;
        public TextView title2;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem(position);
	}
	private void selectItem(int position) {
		switch (position) {
		case 1:
			Intent intent1 = new Intent(HistoryActivity.this, OperationActivity.class);
	        startActivity(intent1);
	        finish();
			break;
		case 2:
			Intent intent2 = new Intent(HistoryActivity.this, HistoryActivity.class);
	        startActivity(intent2);
	        finish();
			break;
		case 3:
			Intent intent3 = new Intent(HistoryActivity.this, RecipeActivity.class);
	        startActivity(intent3);
	        finish();
			break;
		case 4:
			Intent intent4 = new Intent(HistoryActivity.this, SharingActivity.class);
	        startActivity(intent4);
	        finish();
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.menuimg:
				drawer.openDrawer(left_drawer); 
				break;
			default:
				break;
		}
	}

}


