/**
 * 
 */
package com.tcl.manager.battery;

import java.io.IOException;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
//import static com.tcl.manager.battery.BatteryUsageHelper.BatteryUsageContract.BatteryUsageEntry.*;

/**
 * @author zuokang.li
 *
 */
	
public class BatteryUsageTask extends AsyncTask<Object, Void, Void> {
	private static final String TAG = "BatteryUsageTask";

	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Object... objs) {
		if (objs.length < 1) {
			return null;
		}
		Context context = (Context) objs[0];
		try {
			boolean state = (Boolean) (objs.length >= 2 ? objs[1] : false);
			//saveBatteryUsage(context, jumpCurrentBatteryFromSys(context, state));
			BatteryUsageProvider p = new BatteryUsageProvider();
			p.saveBatteryUsage(context, state);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}
	
	

}
