package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.study.service.IQuerryStudyOperater0100020001;
import com.iskinfor.servicedata.study.serviceimpl.QuerryStudyOperater0100020001Impl;
import com.iskyinfor.duoduo.R;

public class BookShelfAsyncTask extends AsyncTask<Void, Integer,  Map<String, Object>> {

	private Activity context;
	private int pagenum=0;
	private ProgressDialog progressdialog;
	private IQuerryStudyOperater0100020001 bookshelfData = null;
	private int exceptionCode;
	private static final int TIME_OUT_EXCEPTION = 1;
	ArrayList<BookShelf> bookshelfdata=null;
	private Map<String, Object> resultData ;
	
	public BookShelfAsyncTask(){
		super();
	} 
	
	public BookShelfAsyncTask(Activity contexts, int pagenums){
		context= contexts;
		pagenum= pagenums;
	} 
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		loadDataProgress();
	 	bookshelfData = new QuerryStudyOperater0100020001Impl();
	}
	 
	private void loadDataProgress() {
		progressdialog = new ProgressDialog(context);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressdialog.setTitle("Load...");
		progressdialog.setMessage("我的书架信息加载中...");
		//progressdialog.setIcon(R.drawable.person);
		progressdialog.setIndeterminate(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	
	@Override
	protected Map<String, Object> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		
//		try {
////			bookshelfdata = bookshelfData.getBookShelf("0002", "12", "", "", pagenum, "", "", "", "", "");
//			Log.i("PLJ", "bookshelfdata==>"+bookshelfdata);
//		} catch (TimeoutException te) {
//			exceptionCode = TIME_OUT_EXCEPTION;
//			publishProgress(exceptionCode);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			
//		}
		
		
		return resultData; 
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		
		if (progressdialog != null && progressdialog.isShowing()) {
			progressdialog.dismiss();
		}

		switch (exceptionCode) {
		case TIME_OUT_EXCEPTION:
			Toast.makeText(
					context,
					context.getResources().getString(
							R.string.time_out_exception), Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onPostExecute( Map<String, Object> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (progressdialog != null && progressdialog.isShowing()) {
			progressdialog.dismiss();
		}
		if (bookshelfdata != null) 
		{
//			 ((BookShelfActivity)context).refreshView(bookshelfdata);
			
//			Intent intent = new Intent(context, BookShelfActivity.class);
//			
//			Log.i("PLJ", "bookshelfdata++==>"+bookshelfdata);
//			
//			intent.putExtra("bookshelfdata", bookshelfdata);
//			((Activity)context).startActivity(intent);
		}
		else 
		{
			Toast.makeText(context,
					context.getResources().getString(R.string.userinfoerror),
					Toast.LENGTH_LONG).show();
		}
		
	}
}
