package com.idthk.meep.ota.open;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	//broadcast receiver
	BroadcastReceiver mReceiver;
	
	//static variables
	private static final String PACKAGE_NAME_MEEP_OTA = "com.idthk.meep.ota";
	private static final String FILE_NAME_MEEP_OTA = "MeepOTA.apk";
	private static final String FILE_EXTERNAL_PATH_MEEP_OTA = "/mnt/sdcard/";
	private static final String TAG = "install_ota";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init broadcast receiver
        mReceiver = new BroadcastReceiver()
        {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED))
				{
					//extra package name
					String packageName = intent.getDataString().substring(8);   
					Log.d(TAG,"installed package : " + packageName);
					// if package name equals to MeepOTA's
					if (PACKAGE_NAME_MEEP_OTA.equals(packageName)) {
						// delete the temporary package file
						File installPackage = new File(FILE_EXTERNAL_PATH_MEEP_OTA + FILE_NAME_MEEP_OTA);
						if(installPackage.exists()) {
							deleteFile(installPackage);
						}

						launchApp();
					}
				}
				
			}
        };
        //intent filter
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        
        //register 
        registerReceiver(mReceiver, intentFilter);
        
    }
    
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
		try {
			PackageManager packageManager = getPackageManager();
			packageManager.getApplicationInfo(PACKAGE_NAME_MEEP_OTA, 0);
			
			launchApp();
		} catch (Exception e) {
			checkAndInstall();
		}
    }
    
    private void checkAndInstall()
    {
        Resources res = this.getApplicationContext().getResources();
    	File installPackage = new File(FILE_EXTERNAL_PATH_MEEP_OTA + FILE_NAME_MEEP_OTA);
        
        // delete old install package if exist
    	if (installPackage.exists()) {
    		deleteFile(installPackage);
    	}

		// copy install package
		if (!copyAssetsTo(FILE_EXTERNAL_PATH_MEEP_OTA, FILE_NAME_MEEP_OTA)) {
			alert(res.getString(R.string.title),
					res.getString(R.string.alert_message_storage),
					res.getString(R.string.alert_ok));
		}

		// file exists
		if (installPackage != null && installPackage.exists()) {
			Log.d(TAG, "install file path : " + installPackage.getAbsolutePath());
			installApk(installPackage);
			Log.d(TAG, "start install");

		} else {
			alert(res.getString(R.string.title),
					res.getString(R.string.alert_message_file),
					res.getString(R.string.alert_ok));
		}
    }
    
    private boolean launchApp() {
    	while (true) {
			try {
				PackageManager packageManager = getPackageManager();
				packageManager.getApplicationInfo(PACKAGE_NAME_MEEP_OTA, 0);
	
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	
				// start intent for open MeepOTA app
				Intent newIntent = new Intent(Intent.ACTION_MAIN);   
				newIntent.setClassName(PACKAGE_NAME_MEEP_OTA, PACKAGE_NAME_MEEP_OTA + ".ui.MainActivity");   
				this.startActivity(newIntent);
				
				// finish current acitvity
				MainActivity.this.finish();
				
				break;
			} catch (NameNotFoundException e1) {
			}
    	}

    	return true;
    }
    
    /**
     * install apk
     * @param file
     */
    private void installApk(File file){
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("MEEP_INSTALL", true);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
	}
    
    /**
     * copy a file from assets to external path
     * @param path
     * @param name
     * @return
     */
    private boolean copyAssetsTo(String path,String name) {
    	//get assets manager
        AssetManager assetManager = getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			//init stream
			in = assetManager.open(name);
			out = new FileOutputStream(path+name);
			
			//copy file in assets to sdcard
			copyFile(in, out);
			
			//finish copying,close and clear all streams
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			
			//return copy success
			return true;
		} catch(IOException e) {
			Log.e(TAG, "Failed to copy asset file: " + name, e);
		}    
		//return copy error
        return false;
    }
    
    /**
     * Copy file
     * 
     * @param in
     * @param out
     * @throws IOException
     */
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
          out.write(buffer, 0, read);
        }
    }
    
    public boolean deleteFile(File file) {
		try {
			if (file.exists()) {
				file.delete();
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG, "delete file error : " + e.toString());
		}
		return false;
	}
    
    private void alert(String title,String message,String ok)
    {
    	new AlertDialog.Builder(this)
        .setTitle(title).setMessage(message)
        .setPositiveButton(ok,
         new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int which) {
        	 MainActivity.this.finish();
          }
          }).show();
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    }
}
