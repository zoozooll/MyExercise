package com.oregonscientific.meep.browser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import android.os.AsyncTask;
import android.util.Log;

public class RecordTimeTask extends AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... arg0) {
		for(String s:arg0)
		{
			writeToFile(s);
		}
		return null;
	}

	private void writeToFile(String string)  {

		File file = new File("/sdcard/websites.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		    writer.write(string);
		    writer.newLine();
		    writer.flush();
		    writer.close();
		} catch (Exception e) {
			Log.d("Error in File write: ", "" + e.getMessage());
		} 
	}
}
