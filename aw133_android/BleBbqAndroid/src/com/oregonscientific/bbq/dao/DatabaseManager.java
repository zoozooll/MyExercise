package com.oregonscientific.bbq.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.oregonscientific.bbq.bean.BBQDataSet.CookingStatus;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;
import com.oregonscientific.bbq.bean.BBQDataSet.Mode;
import com.oregonscientific.bbq.bean.BbqRecord;
import com.oregonscientific.bbq.bean.DonenessTemperature;

import static com.oregonscientific.bbq.dao.BbqRecordsConfig.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库管理类
 * 
 * @author amy
 * 
 */
public class DatabaseManager {

	private AndroidSQLiteOpenHelper dbHelper;
	private SQLiteDatabase db;
	
	private static DatabaseManager instance;
	
	public synchronized static DatabaseManager instance(Context context) {
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
		return instance;
	}
	
	private DatabaseManager(Context context) {
		dbHelper = new AndroidSQLiteOpenHelper(context);
	}
	
	public void open() {
		if (db == null || !db.isOpen()) {
			db = dbHelper.getWritableDatabase();
		}
	}
	
	public void close () {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	

	public Cursor queryRecords(long begindatetime,long endDatetime){
		//String sqlStr = "select * from "+BPRecordsConfig.TABLENAME +"where "+BPRecordsConfig.COL_DATeTIME +">"+ begindatetime +"and"+ BPRecordsConfig.COL_DATeTIME+"<"+ endDatetime +"and userid = "+userId;
		//String sqlStr = "select * from "+TABLENAME+" where datetime >= ? and datetime <= ?  order by datetime desc";
		open();
		String sqlStr = "select * from "+TABLENAME+" where "+COL_FINISHED_DATE+" >= ? and "+COL_FINISHED_DATE+" <= ?";
		Cursor cursor = db.rawQuery(sqlStr,new String[]{String.valueOf(begindatetime),String.valueOf(endDatetime)});
		//Log.e("SQLite", "BPRecords size = "+cursor.getColumnCount());
		while (cursor.moveToNext()) {
			//Log.e("SQLite", "BPRecords = "+cursor.getFloat(cursor.getColumnIndexOrThrow(BPRecordsConfig.COL_SYSTOLIC)));
		}
		return cursor;
	}
	
	public long insert(String table, ContentValues value){
		open();
		db.beginTransaction();
		long _id;
		try {
			_id = db.insert(table, null, value);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return -1;
		} finally {
			db.endTransaction();
		}
		return _id;
	}
	
	public boolean insertAll(String table, List<ContentValues> values){
		open();
		db.beginTransaction();
		try {
			for(ContentValues cv : values) {
				db.insert(table, null, cv);
			}
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
		}
		return true;
	}
	
	public boolean delete(String table, long id) {
		open();
		db.beginTransaction();
		try {
			db.delete(table, "_id = ? ", new String[]{String.valueOf(id)});
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
		}
		return true;
		
	}
	
	public boolean update(String table,long id,  ContentValues value)  {
		open();
		db.beginTransaction();
		try {
			db.update(table, value, " _id = ? ", new String[]{String.valueOf(id)});
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			
			return false;
		} finally {
			db.endTransaction();
		}
		return true;
	}
	
	public Cursor queryAll(String table) {
		open();
		Cursor cursor = null;
		try {
			cursor = db.query(table, null, null, null, null, null, " _id desc ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public Cursor querySingle(String table, long id) {
		open();
		Cursor cursor = null;
		try {
			cursor = db.query(table, null, " _id = ? ", new String[]{String.valueOf(id)} , null, null, null, " 0,1 ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public long insertSingleRecord(BbqRecord record) {
		ContentValues cv = new ContentValues();
		cv.put(COL_FINISHED_DATE, record.getFinishedDate());
		cv.put(COL_CHANNEL, record.getChannel());
//		cv.put(COL_MODE, record.getMode().ordinal());
		cv.put(COL_FINISHED_TEMPERATURE, record.getFinishedTemperature());
		cv.put(COL_COSTD_TIME, record.getCostTime());
		cv.put(COL_COOKING_STATE, record.getCookingState().ordinal());
		cv.put(COL_GRAPH, record.getGraphyPath());
		cv.put(COL_TEMPERATURES_FILE, record.getTemperaturesFilepath());
		cv.put(COL_MEMO, record.getMemo());
	/*	cv.put(COL_SET_MEATTYPE         , record.getSetMeattype());
		cv.put(COL_SET_DONENESSLEVEL    , record.getSetDoneness().ordinal());
		cv.put(COL_SET_TARGE_TEMPERATURE, record.getSetTargeTemperature());
		cv.put(COL_SET_TOTALTIME        , record.getSetTotalTime());
		cv.put(COL_SET_DONENESS_R       , record.getSetDonenessR());
		cv.put(COL_SET_DONENESS_MR      , record.getSetDonenessRM());
		cv.put(COL_SET_DONENESS_M       , record.getSetDonenessM());
		cv.put(COL_SET_DONENESS_MW      , record.getSetDonenessMW());
		cv.put(COL_SET_DONENESS_W       , record.getSetDonenessW());*/
		Mode m = record.getMode();
		switch (m) {
		case MEAN_TYPE_MODE:
			cv.put(COL_SET_MEATTYPE, record.getSetMeattype());
			cv.put(COL_SET_DONENESSLEVEL, record.getSetDoneness().ordinal());
			cv.put(COL_SET_DONENESS_R       , record.getSetDonenessR());
			cv.put(COL_SET_DONENESS_MR      , record.getSetDonenessRM());
			cv.put(COL_SET_DONENESS_M       , record.getSetDonenessM());
			cv.put(COL_SET_DONENESS_MW      , record.getSetDonenessMW());
			cv.put(COL_SET_DONENESS_W       , record.getSetDonenessW());
			break;
		case TARGET_TEMPERATURE_MODE:
			cv.put(COL_SET_TARGE_TEMPERATURE, record.getSetTargeTemperature());
			break;
		case TIMER_MODE:
			cv.put(COL_SET_TOTALTIME, record.getSetTotalTime());
			break;
		default:
			break;
		}
		cv.put(COL_MODE, m.ordinal());
		
		return insert(TABLENAME, cv);
	}

	public BbqRecord querySingleRecord(long id) {
		BbqRecord record = null;
		Cursor c = querySingle(TABLENAME, id);
		if (c != null && c.moveToNext()) {
			record = new BbqRecord();
			record.setChannel(c.getInt(c.getColumnIndexOrThrow(COL_CHANNEL)));
			record.setCookingState(CookingStatus.get(c.getInt(c.getColumnIndexOrThrow(COL_COOKING_STATE))));
			record.setCostTime(c.getInt(c.getColumnIndexOrThrow(COL_COSTD_TIME)));
			record.setFinishedDate(c.getLong(c.getColumnIndexOrThrow(COL_FINISHED_DATE)));
			record.setFinishedTemperature(c.getInt(c.getColumnIndexOrThrow(COL_FINISHED_TEMPERATURE)));
			record.setGraphyPath(c.getString(c.getColumnIndexOrThrow(COL_GRAPH)));
			record.setId(c.getLong(c.getColumnIndexOrThrow("_id")));
			record.setMemo(c.getString(c.getColumnIndexOrThrow(COL_MEMO)));
			record.setMode(Mode.get(c.getInt(c.getColumnIndexOrThrow(COL_MODE))));
			record.setTemperaturesFilepath(c.getString(c.getColumnIndexOrThrow(COL_TEMPERATURES_FILE)));
			Mode m = record.getMode();
			switch (m) {
				case MEAN_TYPE_MODE:
					record.setSetMeattype(c.getInt(c.getColumnIndexOrThrow(COL_SET_MEATTYPE)));
					record.setSetDoneness(DonenessLevel.get(c.getInt(c.getColumnIndexOrThrow(COL_SET_DONENESSLEVEL))));
					record.setSetDonenessR(c.getFloat(c.getColumnIndexOrThrow(COL_SET_DONENESS_R)));
					record.setSetDonenessRM(c.getFloat(c.getColumnIndexOrThrow(COL_SET_DONENESS_MR)));
					record.setSetDonenessM(c.getFloat(c.getColumnIndexOrThrow(COL_SET_DONENESS_M)));
					record.setSetDonenessMW(c.getFloat(c.getColumnIndexOrThrow(COL_SET_DONENESS_MW)));
					record.setSetDonenessW(c.getFloat(c.getColumnIndexOrThrow(COL_SET_DONENESS_W)));
					break;
				case TARGET_TEMPERATURE_MODE:
					record.setSetTargeTemperature(c.getFloat(c.getColumnIndexOrThrow(COL_SET_TARGE_TEMPERATURE)));
					break;
				case TIMER_MODE:
					record.setSetTotalTime(c.getInt(c.getColumnIndexOrThrow(COL_SET_TOTALTIME)));
					break;
				default:
					break;	
			}
		}
		c.close();
		return record;
	}
	/**
	 * update the record for id
	 * @param id
	 * @param dt
	 */
	public void updateRecorsForDoneness(long id, DonenessTemperature dt) {
		ContentValues value = new ContentValues();
		float flag = dt.getRareTemperature();
		if (flag > 0) 
			value.put(COL_SET_DONENESS_R, flag);
		flag = dt.getMediumrareTemperature();
		if (flag > 0) 
			value.put(COL_SET_DONENESS_MR, flag);
		flag = dt.getMediumTemperature();
		if (flag > 0) 
			value.put(COL_SET_DONENESS_M, flag);
		flag = dt.getMediumwellTemperature();
		if (flag > 0) 
			value.put(COL_SET_DONENESS_MW, flag);
		flag = dt.getWelldoneTemperature();
		if (flag > 0) 
			value.put(COL_SET_DONENESS_W, flag);
		update(TABLENAME, id, value);
	}
	
	public void writeMemoForRecord(long id, String memo) {
		ContentValues data = new ContentValues();
		data.put(COL_MEMO, memo);
		update(TABLENAME, id, data);
	}
	
	public Cursor queryRecordForOneday(int year, int month, int day) {
		Calendar flag = Calendar.getInstance();
		flag.set(year, month - 1, day, 0, 0, 0);
		long begin = flag.getTimeInMillis();
		flag.add(Calendar.DATE, 1);
		long end = flag.getTimeInMillis() -1;
		return queryRecords(begin, end);
	}
	
	public Set<Integer> getDaysInRecorded(int year, int month) {
		Set<Integer> set = null;
		Calendar flag = Calendar.getInstance();
		flag.set(year, month - 1, 0, 0, 0, 0);
		long begin = flag.getTimeInMillis();
		flag.add(Calendar.MONTH, 1);
		long end = flag.getTimeInMillis() -1;
		open();
		//String sqlStr = "select _id from "+TABLENAME+" where datetime >= ? and datetime <= ?";
		//String sqlStr = "select _id from "+TABLENAME+" where "+COL_FINISHED_DATE+" >= ? and " + COL_FINISHED_DATE + " <= ?";
		String sqlStr = "select _id,"+COL_FINISHED_DATE+" from "+TABLENAME+" where "+COL_FINISHED_DATE+" >= ? and " + COL_FINISHED_DATE + " <= ?";
		Cursor cursor = db.rawQuery(sqlStr,new String[]{String.valueOf(begin),String.valueOf(end)});
		//Log.e("SQLite", "BPRecords size = "+cursor.getColumnCount());
		while (cursor.moveToNext()) {
			//Log.e("SQLite", "BPRecords = "+cursor.getFloat(cursor.getColumnIndexOrThrow(BPRecordsConfig.COL_SYSTOLIC)));
			flag.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(COL_FINISHED_DATE)));
			int day = flag.get(Calendar.DAY_OF_MONTH);
			if (set == null) {
				set = new HashSet<Integer>();
			}
			set.add(day);
		}
		cursor.close();
		return set;
	}
	
}