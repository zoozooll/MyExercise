package com.vstar3d.Obj;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;

public class Base {
	private static final String TAG = "Base";
	public static final float MaxWidth=4000.0f;
	public static Bitmap loadImageAssets(Context context,String ImgName)
     {
     	Bitmap bmp=null;
     	try {
				BufferedInputStream bis = new BufferedInputStream(context.getAssets().open(
						ImgName));
				bmp = BitmapFactory.decodeStream(bis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	return bmp;
     }

	public static boolean getBluetoothEnabled()
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter != null)
		{
			return bluetoothAdapter.isEnabled();
		}
		return false;
	}

	public static Bitmap initBitmap() {
		Bitmap bmp = Bitmap.createBitmap(50, 24, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
//		canvas.drawColor(Color.BLACK);
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(20);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		p.setAntiAlias(true);
		String time = getCurrentTime();
		canvas.drawText(time, 1, 18, p);
		return bmp;
	}

	public static Bitmap initBitmap(String title) {
		Bitmap bmp = Bitmap.createBitmap(70, 24, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
//		canvas.drawColor(Color.BLACK);
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(16);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		p.setAntiAlias(true);

		int tw=(int)p.measureText(title);
		int l=0;
		if(tw>70)
			l=0;
		else
			l=(70-tw)/2;

		canvas.drawText(title, l, 18, p);
		return bmp;
	}


	public static String getCurrentTime(){
		long l = System.currentTimeMillis();
		return new SimpleDateFormat("HH:mm", Locale.CHINA).format(new Date(l));
	}

	public static void startApp(Context context, String string) {

		Intent intent = context.getPackageManager().getLaunchIntentForPackage(string);
		if (intent == null) {
			Toast.makeText(context, "打开应用失败,请检查是否正确安装", Toast.LENGTH_SHORT).show();
			return;
		}
		context.startActivity(new Intent(intent));
	}

}
