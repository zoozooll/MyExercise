package com.oregonscientific.meep.store2.ctrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileStorageCtrl {

	
	public static void saveFileToPrivate(String fileName, String msg, Context context){
		try {
			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(msg.getBytes());
			fos.close();
		} catch (IOException e) {
			Log.e("reportVersion", "save error, file name:" + fileName + " msg:" + msg );
			Log.e("reportVersion", "save error:" + e.toString());
			e.printStackTrace();
		}
		
	}
	
	public static String loadPrivatesFile(String fileName, Context context){
		File file = new File(fileName);
		if(!file.exists()){
			return null;
		}
		
		FileInputStream fis;
		try {
			fis = context.openFileInput(fileName);
			StringBuilder text = new StringBuilder();
			byte[] buffer = new byte[1024];

			while ((fis.read(buffer)) != -1) {
				text.append(new String(buffer));
			}

			return text.toString();
		} catch (FileNotFoundException e) {
			Log.e("reportVersion", "load error:" + e.toString());
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
