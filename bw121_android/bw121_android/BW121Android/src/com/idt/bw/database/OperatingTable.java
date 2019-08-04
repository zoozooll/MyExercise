package com.idt.bw.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.MonthDisplayHelper;

import com.idt.bw.activity.BuildConfig;
import com.idt.bw.bean.Alarm;
import com.idt.bw.bean.Record;
import com.idt.bw.bean.ReferenceDate;
import com.idt.bw.bean.User;
import com.idt.bw.bean.UserSettings;
import com.idt.bw.bean.UserSettings.NotifyLoopMode;

public class OperatingTable {
	private MyDatabase myDbHelper;
	public static OperatingTable operatingTableInstant;
	private SQLiteDatabase database;
	
	public OperatingTable(Context context) {
		// TODO Auto-generated constructor stub
		myDbHelper = new MyDatabase(context);
	}

	public synchronized static OperatingTable instance(Context context) {
		if (operatingTableInstant == null) {
			operatingTableInstant = new OperatingTable(context);
		}
		return operatingTableInstant;
	}
	
	public void open() {
		if (database == null || !database.isOpen()) {
			database = myDbHelper.getWritableDatabase();
		}
	}
	
	public void close () {
		if (database != null && database.isOpen()) {
			database.close();
		}
	}
	
	public long insertRow(String table, ContentValues data) {
		open();
		long insertId = -1;
		try {
			insertId = database.insert(table, null, data);
		} catch (SQLException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return insertId;
	}
	
	public int updateRow(String table, long id, ContentValues data) {
		open();
		int flag = 0;
		try {
			flag = database.update(table, data, "_id = ?", new String[]{String.valueOf(id)});
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return flag;
	}
	
	public int deleteRow(String table, long id) {
		open();
		int flag = 0;
		try {
			flag = database.delete(table, "_id = ?", new String[]{String.valueOf(id)});
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return flag;
	}
	
	public int clearRows(String table) {
		open();
		int flag = 0;
		try {
			flag = database.delete(table, null, null);
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return flag;
	}
	
	public Cursor queryAllRows(String table) {
		open();
		Cursor c = null;
		try {
			c = database.query(table, null, null, null, null, null, null);
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return c;
	}
	
	public Cursor queryAllRows(String table, String where, String[] args) {
		open();
		Cursor c = null;
		try {
			c = database.query(table, null, where, args, null, null, null);
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return c;
	}
	
	public Cursor querySingleRow(String table, String where, String[] agrs) {
		open();
		Cursor c = null;
		try {
			c = database.query(true, table, null, where,agrs,null, null, null, "0, 1");
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
		}
		return c;
	}
	
	// 插入记录
	public long insertUser(User user) {
//		Log.e("SQLite", "----insert----");
		long id;
		open();
		database.beginTransaction();
		try {
			/*database.execSQL("insert into " + User.TABLENAME + " values(?,?,?,?,?,?,?,?,?,?,?)",new String[]{user.getId(),
					user.getUserPhoto(),user.getUserName(),user.getUserBirth(),user.getUserGender(),
					user.getUserPregnancyWeeks(),user.getUserPregnancyDays(),
					user.getUserHeight(),user.getUserCategory(),user.getUserWeightUnit(),user.getUserHeightUnit()});*/
			// Modified by aaronli at Mar 28 2014. 
			// return the current inserted row' id
			ContentValues values = new ContentValues();
			values.put("userPhoto", user.getUserPhoto());
			values.put("userName", user.getUserName());
			values.put("userBirth", user.getUserBirth());
			values.put("userGender", user.getUserGender());
			values.put("userPregnancyWeeks", user.getUserPregnancyWeeks());
			values.put("userPregnancyDays", user.getUserPregnancyDays());
			values.put("userHeight", user.getUserHeight());
			values.put("userCategory", user.getUserCategory());
			values.put("userWeightUnit", user.getUserWeightUnit());
			values.put("userHeightUnit", user.getUserHeightUnit());
			id = database.insertOrThrow(User.TABLENAME, null, values);
			database.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} finally {
			database.endTransaction();
		}
		return id;
	}
	
	// 查询记录
	public ArrayList<User> query(String id) {
		//Log.e("SQLite", "----query----");
		open();
		Cursor cursor;
		User user;
		ArrayList<User> list = new ArrayList<User>();
		// 若fileId为null或""则查询所有记录
		if (id == null || id.equals("")) {
			cursor = database.rawQuery("select * from " + User.TABLENAME, null);
		} else {
			cursor = database.rawQuery("select * from " + User.TABLENAME + " where _id=?", new String[] { id });
		}
		//Log.e("cdf","cursor == "+cursor.getCount());
		//cursor.moveToFirst();
		while (cursor.moveToNext()) {
			user = new User();
			//Log.e("SQLite", "user------"+user.toString());
			user.setId(cursor.getString(cursor.getColumnIndex("_id")));
			user.setUserPhoto(cursor.getString(cursor.getColumnIndex("userPhoto")));
			user.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
			user.setUserBirth(cursor.getString(cursor.getColumnIndex("userBirth")));
			user.setUserGender(cursor.getString(cursor.getColumnIndex("userGender")));
			user.setUserPregnancyWeeks(cursor.getString(cursor.getColumnIndex("userPregnancyWeeks")));
			user.setUserPregnancyDays(cursor.getString(cursor.getColumnIndex("userPregnancyDays")));
			user.setUserHeight(cursor.getString(cursor.getColumnIndex("userHeight")));
			user.setUserCategory(cursor.getString(cursor.getColumnIndex("userCategory")));
			user.setUserWeightUnit(cursor.getString(cursor.getColumnIndex("userWeightUnit")));
			user.setUserHeightUnit(cursor.getString(cursor.getColumnIndex("userHeightUnit")));
			
			list.add(user);
		}
		
		cursor.close();
		
		if (list.size() == 0) {
		}else{
			//Log.e("SQLite", "****表中you数据****"+list.size());
		}
		return list;
	}
	
	//delete one user 
	public void deleteUserAndSettingRecords(String id) {
		open();
		try {
			database.delete(User.getTablename(), "_id=? ", new String[]{String.valueOf(id)});
			database.delete(RecordConfig.TABLE_NAME, "user_id=? ", new String[]{String.valueOf(id)});
			database.delete(UserSettingsConfig.TABLE_NAME, "user_id=? ", new String[]{String.valueOf(id)});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public Cursor queryAll(String table) {
		open();
		Cursor cursor = null;
		try {
			cursor = database.query(table, null, null, null, null, null, " _id desc ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	// 更新记录
	public int update(User user) {
		//Log.e("SQLite", "----update----");
		open();
		database.beginTransaction();
		try {
			database.execSQL("update " + User.TABLENAME
					+ " set userPhoto=?, userName=?, userBirth=?,userGender=?,userPregnancyWeeks=?, " +
					"userPregnancyDays=?,userHeight=?,userCategory=?,userWeightUnit=?,userHeightUnit=? where _id=?", new String[] {
					user.getUserPhoto(),user.getUserName(), user.getUserBirth(), user.getUserGender(),user.getUserPregnancyWeeks(),
					user.getUserPregnancyDays(),user.getUserHeight(),user.getUserCategory(),user.getUserWeightUnit(),user.getUserHeightUnit(), user.getId()});
			
			database.setTransactionSuccessful();
		} catch (SQLException e) {

			e.printStackTrace();
			return 0;
		} finally {
			database.endTransaction();
		}
		return 1;
	}
	
	/**
	 * Add a record to database;
	 * @param record
	 * @return
	 */
	public long addRecord(Record record) {
		ContentValues cv = new ContentValues();
		long id;
		cv.put(RecordConfig.COL_WEIGHT, record.getWeight());
		cv.put(RecordConfig.COL_UNIT, record.getUnit());
		cv.put(RecordConfig.COL_FOREIGN_KEY, record.getUserId());
		cv.put(RecordConfig.COL_DATETIME, record.getDatetime());
		cv.put(RecordConfig.COL_TARGE_WEIGHT, record.getTargetWeight());
		cv.put(RecordConfig.COL_CURRENT_HEIGHT, record.getCurrentHeight());
		/*Cursor cursor = querySingleRow(RecordConfig.TABLE_NAME, RecordConfig.COL_FOREIGN_KEY + " =? and "+RecordConfig.COL_DATETIME + " =? ", new String[] {String.valueOf(record.getUserId()), record.getDatetime()});
		if (cursor != null && cursor.moveToNext()) {
			id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
			//Log.e("cdf","Record id -------------->"+id);
			updateRow(RecordConfig.TABLE_NAME, id, cv);
		} else {*/
		id = insertRow(RecordConfig.TABLE_NAME, cv);
		//}
		//cursor.close();
		return id;
	}
	
	/**
	 * Add a record to database;
	 * @param record
	 * @return
	 */
	public long updateRecordInput(Record record) {
		long id = -1;
		Cursor cursor = queryAllRows(RecordConfig.TABLE_NAME, RecordConfig.COL_FOREIGN_KEY + " =? and substr("+RecordConfig.COL_DATETIME + ", 1, 10) =? ", new String[] {String.valueOf(record.getUserId()), record.getDatetime()});
		int index = 0;
		while (cursor != null && cursor.moveToNext()) {
			index ++;
			id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
			if (index == 1) {
				//Log.e("cdf","Record id -------------->"+id);
				ContentValues cv = new ContentValues();
				cv.put(RecordConfig.COL_WEIGHT, record.getWeight());
//				cv.put(RecordConfig.COL_UNIT, 0);
				cv.put(RecordConfig.COL_FOREIGN_KEY, record.getUserId());
				cv.put(RecordConfig.COL_DATETIME, record.getDatetime() + " 00:00");
//				cv.put(RecordConfig.COL_TARGE_WEIGHT, record.getTargetWeight());
				cv.put(RecordConfig.COL_CURRENT_HEIGHT, record.getCurrentHeight());
				updateRow(RecordConfig.TABLE_NAME, id, cv);
			} else {
				deleteRow(RecordConfig.TABLE_NAME, id);
			}
		} 
		cursor.close();
		if (index == 0) {
			record.setDatetime(record.getDatetime() + " 00:00");
			addRecord(record);
		}
		return id;
	}
	
	/**
	 * Add a record to database, if the settings' user id have been set in database, it will update the row.
	 * @param record
	 * @return 
	 */
	public long addUserSettings(UserSettings settings) {
		ContentValues cv = new ContentValues();
		long id;
		StringBuilder stringBuilder = new StringBuilder();   
		for (int i = 0; i < settings.getNotifyLoop().length; i++) {   
			stringBuilder.append(settings.getNotifyLoop()[i] + ",");   
		} 
		cv.put(UserSettingsConfig.COL_FOREIGN_KEY_USER, settings.getUserId());
		cv.put(UserSettingsConfig.COL_TARGET_WEIGHT, settings.getTargetWeight());
		cv.put(UserSettingsConfig.COL_Cloth_WEIGHT, settings.getClothweight());
		cv.put(UserSettingsConfig.COL_Cloth_ON, settings.isClothlOn());
		cv.put(UserSettingsConfig.COL_Target_ON, settings.isTargetlOn());
		cv.put(UserSettingsConfig.COL_General_ON, settings.isGeneralOn());
		cv.put(UserSettingsConfig.COL_NOTIFY_ON, settings.isNotifyOn());
		cv.put(UserSettingsConfig.COL_NOTIFY_TIME, settings.getNotifyTime());
		cv.put(UserSettingsConfig.COL_NOTIFY_LOOP, stringBuilder
				.toString());
		cv.put(UserSettingsConfig.COL_CURRENT_HEIGHT,
				settings.getCurrentHeight());
		cv.put(UserSettingsConfig.COL_NOTIFY_DATE, settings.getNotifyDate());
		cv.put(UserSettingsConfig.COL_NOTIFY_FEATURE,
				settings.isNotifyFeature());
		Cursor cursor = querySingleRow(UserSettingsConfig.TABLE_NAME, UserSettingsConfig.COL_FOREIGN_KEY_USER + " = ? ", new String[] {String.valueOf(settings.getUserId())});
		if (cursor != null && cursor.moveToNext()) {
			id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
			updateRow(UserSettingsConfig.TABLE_NAME, id, cv);
		} else {
			id = insertRow(UserSettingsConfig.TABLE_NAME, cv);
		}
		cursor.close();
		return id;
	}
	
	public Cursor getRecordFromUser(long userId) {
		return queryAllRows(RecordConfig.TABLE_NAME, RecordConfig.COL_FOREIGN_KEY +" ? ", new String[]{String.valueOf(userId)});
		
	}
	
	public UserSettings getUserSetting(long userId) {
		Cursor c = querySingleRow(UserSettingsConfig.TABLE_NAME, UserSettingsConfig.COL_FOREIGN_KEY_USER + "=? ", new String[]{String.valueOf(userId)});
		UserSettings result = null;
		if (c != null && c.moveToNext()) {
			String[] arrayStr =new String[]{};
			String str = c.getString(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_LOOP));
		    arrayStr = str.split(",");
		    
			result = new UserSettings();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(UserSettingsConfig.COL_FOREIGN_KEY_USER)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(UserSettingsConfig.COL_TARGET_WEIGHT)));
			result.setClothweight(c.getFloat(c.getColumnIndexOrThrow(UserSettingsConfig.COL_Cloth_WEIGHT)));
			result.setTargetlOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_Cloth_ON)) != 0);
			result.setClothlOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_Target_ON)) != 0);
			result.setGeneralOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_General_ON)) != 0);
			result.setNotifyOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_ON)) != 0);
			result.setNotifyTime(c.getString(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_TIME)));
			result.setNotifyLoop(arrayStr);
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(UserSettingsConfig.COL_CURRENT_HEIGHT)));
			result.setNotifyDate(c.getString(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_DATE)));
			result.setNotifyFeature(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_FEATURE)) != 0);
		}
		return result;
	} 
	
	// Amy get user's all weight records --------------------------
	public ArrayList<Record> getAllRecords(long userId) {
		ArrayList<Record> recordsList = new ArrayList<Record>();
		Record result = null;
		String sql = "select *, max(datetime) lasttime from weight_records where user_id = ? group by substr(datetime, 1, 10) order by lasttime";
		Cursor c = database.rawQuery(sql, new String[]{String.valueOf(userId)});
		//Log.e("cdf","cursor size------"+c.getColumnCount());
		while (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
			recordsList.add(result);
		}
		c.close();
		return recordsList;
	}
	
	public ArrayList<Record> getDaysRecords(long userId) {
		ArrayList<Record> recordsList = new ArrayList<Record>();
		Record result = null;
		String sql = "select *, max(datetime) lasttime from "+RecordConfig.TABLE_NAME+" where user_id = ? group by substr(datetime, 1, 10) order by lasttime";
		Cursor c = database.rawQuery(sql, new String[]{String.valueOf(userId)});
//		Log.e("cdf","cursor days size >>>>>>>>>>>>>>>>>>>> "+c.getCount());
		while (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
			recordsList.add(result);
		}
		c.close();
		return recordsList;
	}
	
	public ArrayList<Record> getMonthsRecords(long userId) {
		ArrayList<Record> recordsList = new ArrayList<Record>();
		Record result = null;
		String sql = "select  _id, weight_unit, user_id, max(weight_value) weight_value, height, datetime, targeweight, strftime('%Y-%m', datetime, 'localtime') from "
		+RecordConfig.TABLE_NAME+" where user_id = ? group by strftime('%Y-%m', [datetime], 'localtime') order by datetime asc";
		Cursor c = database.rawQuery(sql, new String[]{String.valueOf(userId)});
		while (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
			recordsList.add(result);
		}
		c.close();
		return recordsList;
	}
	
	public ArrayList<Record> getYearsRecords(long userId) {
		ArrayList<Record> recordsList = new ArrayList<Record>();
		Record result = null;
		String sql = "select  _id, weight_unit, user_id, max(weight_value) weight_value, height, datetime, targeweight, strftime('%Y', datetime, 'localtime') from "
		+RecordConfig.TABLE_NAME+" where user_id = ? group by strftime('%Y', [datetime], 'localtime') order by datetime asc";
		Cursor c = database.rawQuery(sql, new String[]{String.valueOf(userId)});
		while (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
			recordsList.add(result);
		}
		c.close();
		return recordsList;
	}
	// ---------------------------------------
	public Record getSingleRecord(long id) {
		Cursor c = querySingleRow(RecordConfig.TABLE_NAME, "_id=? ", new String[]{String.valueOf(id)});
		Record result = null;
		if (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
		}
		c.close();
		return result;
	}
	
	public Record getSingleRecordFromDate(long userId, String date) {
		String sql = "SELECT DISTINCT *,max(datetime) FROM weight_records WHERE user_id =? and strftime('%Y-%m-%d', datetime) =?  Group by strftime('%Y-%m-%d', datetime)";
		Cursor c = database.rawQuery(sql, new String[] {String.valueOf(userId), date});
		Record result = null;
		if (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
		}
		c.close();
		return result;
	}
	
	public Record getLastRecord(long userId) {
		Cursor c = null;
		try {
			open();
			c = database.query(RecordConfig.TABLE_NAME, null, RecordConfig.COL_FOREIGN_KEY + "=? ", new String[] {String.valueOf(userId)}, null, null, RecordConfig.COL_DATETIME + " desc", "0,1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Record result = null;
		if (c != null && c.moveToNext()) {
			result = new Record();
			result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			result.setWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_WEIGHT)));
			result.setUnit(c.getInt(c.getColumnIndexOrThrow(RecordConfig.COL_UNIT)));
			result.setUserId(c.getLong(c.getColumnIndexOrThrow(RecordConfig.COL_FOREIGN_KEY)));
			result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_CURRENT_HEIGHT)));
			result.setDatetime(c.getString(c.getColumnIndexOrThrow(RecordConfig.COL_DATETIME)));
			result.setTargetWeight(c.getFloat(c.getColumnIndexOrThrow(RecordConfig.COL_TARGE_WEIGHT)));
		}
		c.close();
		return result;
	}
	
	// add by aaronli at Apr25 2014 for get reference data from database.
	
	public Map<String, Float> queryReference(Calendar beginDate,
			Calendar endDate, Calendar birthday, int gender, String category,
			String per) {
		int beginAgeForMonth = countMonthsAge(beginDate, birthday);
		int endAgeForMonth = countMonthsAge(endDate, birthday);
		String formatStr = null;
		//if ("daily".equals(per)) {
			formatStr = "yyyy-MM-dd";
		/*}else if ("monthly".equals(per)) {
			formatStr = "yyyy-MM";
		} else if ("yearly".equals(per)) {
			formatStr = "yyyy";
		}*/
		
		Map<String, Float> result = new HashMap<String, Float>();
		if ("daily".equals(per) && endAgeForMonth <= 4) {
			// select reference data from weekly
			int beginWeeks = countWeeksAge(beginDate, birthday);
			int endWeeks = countWeeksAge(endDate, birthday);
			String sql = "select value from reference_data_weekly where gender = ? and category = ? and weeks >= ? and weeks <= ? order by weeks";
			Cursor c = null;
			try {
				open();
				c = database.rawQuery(sql, new String[] {String.valueOf(gender), category, String.valueOf(beginWeeks), String.valueOf(endWeeks) });
				while (c.moveToNext()) {
					String dateStr = DateFormat.format(formatStr, beginDate).toString();
					float value = c.getFloat(c.getColumnIndexOrThrow("value"));
					result.put(dateStr, value);
					beginDate.add(Calendar.WEEK_OF_YEAR, 1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (c != null) {
					c.close();
				}
			}
		} else if (beginAgeForMonth < 228) {
			// select reference data from monthly
			if (!"yearly".equals(per) ) {
				String sql = "select value from reference_data_monthly where gender = ? and category = ? and months >= ? and months <= ? order by months";
				Cursor c = null;
				try {
					open();
					c = database.rawQuery(sql, new String[] { String.valueOf(gender), category, String.valueOf(beginAgeForMonth), String.valueOf(endAgeForMonth) });
					while (c.moveToNext()) {
						String dateStr = DateFormat.format(formatStr, beginDate).toString();
						float value = c.getFloat(c.getColumnIndexOrThrow("value"));
						result.put(dateStr, value);
						beginDate.add(Calendar.MONTH, 1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (c != null) {
						c.close();
					}
				}
			} else {
				
				String sql = "select max(value) years, months/12 age from reference_data_monthly where gender = ? and category = ? and age >= ? and age <= ? group by age order by age";
				Cursor c = null;
				try {
					int beginYearsAge = countYearsAge(beginDate, birthday);
					int endYearsAge = countYearsAge(endDate, birthday);
					open();
					c = database.rawQuery(sql, new String[] {String.valueOf(gender), category, String.valueOf(beginYearsAge), String.valueOf(endYearsAge) });
					while (c.moveToNext()) {
						String dateStr = DateFormat.format(formatStr, beginDate).toString();
						float value = c.getFloat(c.getColumnIndexOrThrow("years"));
						result.put(dateStr, value);
						beginDate.add(Calendar.YEAR, 1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (c != null) {
						c.close();
					}
				}
			}		
			
			
		}
		return result;
	}
	
	public List<ReferenceDate> queryReference( Calendar birthday, int gender, String category) {
		List<ReferenceDate> result = new ArrayList<ReferenceDate>();
		String sql = "select value from reference_data_weekly where gender = ? and category = ? order by weeks";
		Cursor c = null, c2 = null;
		try {
			open();
			c = database.rawQuery(sql, new String[] {String.valueOf(gender), category });
			Calendar cal = (Calendar) birthday.clone();
			while (c.moveToNext()) {
				float value = c.getFloat(c.getColumnIndexOrThrow("value"));
				result.add(new ReferenceDate(cal.getTime(), value));
				cal.add(Calendar.WEEK_OF_YEAR, 1);
			}
			sql = "select value from reference_data_monthly where gender = ? and category = ? and months >= 3 order by months";
			c2 = database.rawQuery(sql, new String[] {String.valueOf(gender), category });
			cal = (Calendar) birthday.clone();
			cal.add(Calendar.MONTH, 4);
			while (c2.moveToNext()) {
				float value = c2.getFloat(c2.getColumnIndexOrThrow("value"));
				result.add(new ReferenceDate(cal.getTime(), value));
				cal.add(Calendar.MONTH, 1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
			if (c2 != null) {
				c2.close();
			}
		}
		return result;
	}
	
	public List<UserSettings> queryAllEnableNotifies() {
		Cursor c = null;
		List<UserSettings> results = null;
		try {
			open();
			c = database.query(UserSettingsConfig.TABLE_NAME, null, UserSettingsConfig.COL_NOTIFY_ON + "=? ", new String[] {"1"}, null, null, UserSettingsConfig.COL_NOTIFY_DATE);
			while (c != null && c.moveToNext()) {
				String[] arrayStr =new String[]{};
				String str = c.getString(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_LOOP));
			    arrayStr = str.split(",");
			    
				UserSettings result = new UserSettings();
				result.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
				result.setClothlOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_Cloth_ON)) != 0);
				result.setTargetlOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_Target_ON)) != 0);
				result.setGeneralOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_General_ON)) != 0);
				result.setNotifyOn(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_ON)) != 0);
				result.setNotifyTime(c.getString(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_TIME)));
				result.setNotifyLoop(arrayStr);
				result.setCurrentHeight(c.getFloat(c.getColumnIndexOrThrow(UserSettingsConfig.COL_CURRENT_HEIGHT)));
				result.setNotifyDate(c.getString(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_DATE)));
				result.setNotifyFeature(c.getInt(c.getColumnIndexOrThrow(UserSettingsConfig.COL_NOTIFY_FEATURE)) != 0);
				result.setUserId(c.getLong(c.getColumnIndexOrThrow(UserSettingsConfig.COL_FOREIGN_KEY_USER)));
				if (results  == null ) {
					results = new ArrayList<UserSettings>();
				}
				results.add(result);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return results;
	}
	
	public int countMonthsAge (Calendar c1, Calendar c2) {
		int year = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		int month = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		return year * 12 + month;
	}
	
	public int countWeeksAge (Calendar c1, Calendar c2) {
		return (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / (long)(7 * 24 * 60 * 60 * 1000));
	}
	
	public int countYearsAge (Calendar c1, Calendar c2) {
		return (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / (long)(7 * 24 * 60 * 60 * 1000));
	}
	
	// end
}
