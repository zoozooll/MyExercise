/**
 * 
 */
package com.idt.bw.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.idt.bw.bean.Alarm;
import com.idt.bw.bean.User;
import com.idt.bw.bean.UserSettings;
import com.idt.bw.bean.UserSettings.NotifyLoopMode;
import com.idt.bw.database.OperatingTable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.text.TextUtils;
import android.text.format.DateFormat;

/**
 * @author aaronli
 *
 */
public class AlarmService extends Service {

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String[] users = new String[1];
		long notifyTime = calculateNextAlert(users);
		if (notifyTime > 0) {
			setNextAlert(users[0], notifyTime);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void setNextAlert (String userName, long notifyTime ) {
		Intent intent = new Intent(this, CallAlarmNotification.class);
        intent.putExtra("music", true);
        intent.putExtra("UserName", userName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager am;
        
        //获取系统进程
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMi lis()+(10*1000), (24*60*60*1000), pendingIntent);
	}
	
	private long calculateNextAlert(String[] userName) {
        long minTime = Long.MAX_VALUE;
        Calendar now = Calendar.getInstance();

        Set<Alarm> alarms = new HashSet<Alarm>();
        
        // get usersettings 
        OperatingTable dao = OperatingTable.instance(this);
        List<UserSettings> settings =  dao.queryAllEnableNotifies();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendarSetting = Calendar.getInstance();
        if(settings != null){
	        for (UserSettings item : settings) {
	        	Date d;
				try {
					long millis;
					d = format.parse(item.getNotifyDate()+ " " + item.getNotifyTime());
					calendarSetting.setTime(d);
					String[] loopStr = item.getNotifyLoop();
					List<Integer> loopArr = loopStringToNumber(loopStr);
					if (loopArr.isEmpty()) {
						if (now.before(calendarSetting)) {
							millis = calendarSetting.getTimeInMillis();
						} else {
							continue;
						}
					} else {
						int day = calendarSetting.get(Calendar.DAY_OF_WEEK) - 1;
						if (loopArr.contains(day) && (compareHourMinute(now, calendarSetting) > 0)) {
							calendarSetting.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
							millis = calendarSetting.getTimeInMillis();
						} else {
							int weekdayNow = now.get(Calendar.DAY_OF_WEEK) -1;
							int beDays = findNextDayOfSettingWeek(weekdayNow, loopArr);
							calendarSetting.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
							calendarSetting.add(Calendar.DAY_OF_MONTH, beDays);
							millis = calendarSetting.getTimeInMillis();
						}
					}
					Alarm a = new Alarm();
					a.setNotifyTime(millis);
					a.setMessage(getResources().getString(R.string.notify_timeon));
					a.setUser(item.getUserId());
					alarms.add(a);
				} catch (ParseException e) {
					e.printStackTrace();
				}
	        }
        }else{
        	return -1;
        }
        Alarm alarm = null;

        for (Alarm a : alarms) {
            if (a.getNotifyTime() < minTime) {
                minTime = a.getNotifyTime();
                alarm = a;
            }
        }
        if (alarm != null) {
        	List<User> users =  dao.query(String.valueOf(alarm.getUser()));
        	if (users != null && !users.isEmpty()) {
        		userName[0] = users.get(0).getUserName();
        	}
        	return alarm.getNotifyTime();
        } 
        return -1;
    }

	private List<Integer> loopStringToNumber(String[] str) {
		List<Integer> list = new ArrayList<Integer>(7);
		for (int i = 0; i < str.length; i++) { 
			if (TextUtils.isDigitsOnly(str[i])) {
				list.add(i);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @param c1
	 * @param c2
	 * @return 
	 */
	private int compareHourMinute(Calendar c1, Calendar c2) {
		int hour1 = c1.get(Calendar.HOUR_OF_DAY);
		int minute1 = c1.get(Calendar.MINUTE);
		int hour2 = c2.get(Calendar.HOUR_OF_DAY);
		int minute2 = c2.get(Calendar.MINUTE);
		if (hour1 != hour2) {
			return (hour2 - hour1) * 60;
		} else {
			return minute2 - minute1;
		}
	}
	
	private int findNextDayOfSettingWeek(int weekdayNow, List<Integer> weekdaySettings) {
		
		int nextDay = -1;
		for (int i = 0, size = weekdaySettings.size(); i < size; i ++) {
			if (weekdaySettings.get(i) > weekdayNow) {
				nextDay = weekdaySettings.get(i);
				break;
			}
		}
		if (nextDay == -1) {
			nextDay = weekdaySettings.get(0);
		}
		int betweenDays = nextDay - weekdayNow;
		if (betweenDays <= 0) {
			betweenDays += 7;
		}
		return betweenDays;
	}

}
