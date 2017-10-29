package com.iskyinfor.duoduo.ui.downloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.iskyinfor.duoduo.R;


public class DataBaseDialog {
	private Activity activity;
	private LayoutInflater layoutInflater;
	private AlertDialog alertDialog;
	private View dialogView;
	public DataBaseDialog(Activity activity){
		this.activity=activity;
		layoutInflater=LayoutInflater.from(activity);
		dialogView=layoutInflater.inflate(R.layout.download_database_dialog_layout, null);
		alertDialog=new AlertDialog.Builder(activity).show();
	}
	
	
	public void setContentView(){
		if(dialogView!=null){
			dialogView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			alertDialog.getWindow().setContentView(dialogView);
		
		}
		
	}
	
}
