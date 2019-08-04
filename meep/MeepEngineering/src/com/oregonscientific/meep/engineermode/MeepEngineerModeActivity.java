package com.oregonscientific.meep.engineermode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.oregonscientific.meep.engineermode.OtaUpgradeUtility.ProgressListener;
import com.oregonscientific.meep.engineermode.R;

public class MeepEngineerModeActivity extends Activity {
    /** Called when the activity is first created. */
	
	String otaPath = "/mnt/extsd/ota/ota.zip";
	String apkPath = "/mnt/extsd/em/";
	String previousSN = "";
	static String TAG = "MeepEngineerMode";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnChangeSerialNum = (Button) findViewById(R.id.btnChangeSerialNum);
        btnChangeSerialNum.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {				
				TextView new_sn = (TextView) findViewById(R.id.txtSerialNum);
				writeSerialNumToFile(new_sn.getText().toString());
			}        	
        	
        } );   

        Button btnResetSerialNum = (Button) findViewById(R.id.btnResetSerialNum);
        btnResetSerialNum.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				TextView sn = (TextView) findViewById(R.id.txtSerialNum);
				previousSN = getSerialNum();
				sn.setText(previousSN);
			}        	
        	
        } );   
                
        Button btnUpgrade = (Button) findViewById(R.id.btnUpgrade);
        btnUpgrade.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				runOTA(otaPath);
			}        	
        	
        } );   

        Button btnRestore = (Button) findViewById(R.id.btnRestore);
        btnRestore.setOnClickListener( new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				installApp();
			}        	
        	
        } );   
                

		TextView sn = (TextView) findViewById(R.id.txtSerialNum);
		previousSN = getSerialNum();
		sn.setText(previousSN);
		
		TextView ver = (TextView) findViewById(R.id.txtCurrentVersion);
		ver.setText(getVersion());
        
		TextView mac = (TextView) findViewById(R.id.txtMacaddress);
		mac.setText(getMacAddress());
        
        Log.d(TAG,"getVersion="+getVersion());
        Log.d(TAG,"getSerialNum="+getSerialNum());
        Log.d(TAG,"getMacAddress="+getMacAddress());
        

    }

	


    @Override
	protected void onStart() {
    	//showMeepPopUpMessage(R.string.browser_title_uh_oh, R.string.browser_msg_no_network);
		super.onStart();
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	
	public String getMacAddress(){
		WifiManager wimanager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		return wimanager.getConnectionInfo().getMacAddress();
	}

	public String getSerialNumber(){
		WifiManager wimanager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		return wimanager.getConnectionInfo().getMacAddress();
	}
	
	private String getVersion() {
		// Get the text file
		File file = new File("/mnt/private/meepver.txt");
		String line = null;
		// Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				text.append(line);
			}
		} catch (Exception e) {
			// You'll need to add proper error handling here
		}
		Log.e(TAG, "version text: " + text);

		return text.toString();
	}

	private String getSerialNum() {
		// Get the text file
		File file = new File("/mnt/private/sn.txt");
		String line = null;
		// Read text from file
		StringBuilder text = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				text.append(line);
			}
		} catch (Exception e) {
			return "";
		}
		Log.e(TAG, "sn text: " + text);
		return text.toString().toUpperCase();
	}



	private void writeSerialNumToFile(String in_sn) {
		try {
			String fileName = "/mnt/private/sn.txt";
			File file = new File(fileName);
			Writer writer = new BufferedWriter(new FileWriter(file));
			String sn = in_sn.toUpperCase();
			if (sn != null) {
				writer.write(sn);
			}
			writer.close();
			ShowAlertDialog("Update Serial no","Success");
		} catch (Exception ex) {
			ShowAlertDialog("Update Serial no","Error:"+ex.toString());
			Log.e(TAG, "Cannot write locale file because: " + ex.toString());
		}
	}	

	
	public void installApp(){
	
	    String root_sd = Environment.getExternalStorageDirectory().toString();
	    File file = new File(apkPath) ;       
	    File list[] = file.listFiles();
	    if(list!=null){
	    	String filePath;
	    	for( int i=0; i< list.length; i++)
	        {
	    		if(list[i].getAbsolutePath().indexOf(".apk")>0 && list[i].getAbsolutePath().indexOf("._")<0){
	    			filePath = list[i].getAbsolutePath();
	    			Intent installIntent = new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
	    			installIntent.putExtra("MEEP_INSTALL", true);
	    			startActivity(installIntent);
	    		}
	        }            
	    }
	    	
	}
	
	public static boolean installAppProc(Context context, String path) {
		
		try {
			Log.d("installApp", "installing app " + path);
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			
			installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
			installIntent.putExtra("MEEP_INSTALL", true);
			context.startActivity(installIntent);
		} catch (Exception ex) {
			new File(path).deleteOnExit();
			Log.e(TAG, "install-app exception:" + ex.toString());
			return false;
		}
		return true;
	}
 
	public void runOTA(String path){
		try{
			
			
			OtaUpgradeUtility ota = new OtaUpgradeUtility(this);
			ota.beginUpgrade(new File(path),new ProgressListener() {
				TextView txtOTA = (TextView) findViewById(R.id.txtOTA);
			
    			@Override
    			public void onProgress(int progress) {
    				Log.d(TAG,"progress:"+progress);    				
    			}

    			@Override
    			public void onVerifyFailed(int errorCode, Object object) {
    				ShowAlertDialog("OTA Upgrade","Error: Verify Failed:"+errorCode);
    				Log.e(TAG,"errorCode:"+errorCode);
    			}

    			@Override
    			public void onCopyProgress(int progress) {
    				txtOTA.setText("OTA preparing: "+progress+"%..");
    				Log.d(TAG,"copy progress:"+progress);
    			}

    			@Override
    			public void onCopyFailed(int errorCode, Object object) {
    				ShowAlertDialog("OTA Upgrade","Error: copy error:"+errorCode);
    				Log.e(TAG,"copy error:"+errorCode);
    			}
    		});
		}catch(Exception e){
			ShowAlertDialog("OTA Upgrade","Error:"+e.toString());
			Log.e("MeepEngineerMode",e.toString());
		}
	}

	
	private void ShowAlertDialog(String title, String msg)
	{
	 Builder MyAlertDialog = new AlertDialog.Builder(this);
	 MyAlertDialog.setTitle(title);
	 MyAlertDialog.setMessage(msg);
	 MyAlertDialog.show();
	}	
	
}