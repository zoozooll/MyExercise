package com.iskyinfor.duoduo.ui.book;

import java.io.File;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IFilePathTranslate0200040001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.FilePathTranslate0200040001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.ui.lesson.LessonPlayVideoActivity;
//import com.kang.pdfreader.MyPdfReaderActivity;

public class RequestBookUrlTask extends AsyncTask<Void, Integer, Object> {

	private Context context;
	private ProgressDialog progressdialog;
	private IFilePathTranslate0200040001 iFilePathTranslate = null;
	private DownloadServiceStub downloadServiceStub;
	private int exceptionCode;
	private static final int TIME_OUT_EXCEPTION = 1;
	private String bookId;
	private Map<String, String> bookshelfData;
	private int type=-1;
	private String bookName;
	public RequestBookUrlTask(Context contexts, String bookId,String name,int type ) {
		context = contexts;
		this.bookId = bookId;
		this.bookName=name;
		downloadServiceStub = new DownloadServiceStub(contexts);
		iFilePathTranslate = new FilePathTranslate0200040001Impl();
		this.type=type;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		loadDataProgress();
		
	}

	private void loadDataProgress() {
		progressdialog = new ProgressDialog(context);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setTitle("Load...");
		progressdialog.setMessage("请求下载链接");
		progressdialog.show();
	}

	@Override
	protected Object doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.i("liu", "bookId===:" + bookId);
		try {
			if (bookId != null) {
				bookshelfData = iFilePathTranslate.getProPath(bookId);
			}
		} catch (TimeoutException te) {
			exceptionCode = TIME_OUT_EXCEPTION;
			publishProgress(exceptionCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return bookshelfData;
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
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if (progressdialog != null && progressdialog.isShowing()) {
			progressdialog.dismiss();
		}
		if (bookshelfData != null) {

//			 String url=bookshelfdata.get(DataConstant.PRO_REAL_PATH_KEY);
			String fileName = bookshelfData.get(DataConstant.PRO_VIEW_PATH_KEY);
			Log.i("liu", "fileName===:" + fileName);
			if (fileName != null && !"".equals(fileName)) {
				if (downloadServiceStub.isFileExit(fileName)) {
					Toast.makeText(context, "文件已下载，进入播放界面!", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(this.context,LessonPlayVideoActivity.class);
					switch (this.type) {
					case 0:
						String pafPath = Environment.getExternalStorageDirectory()
						+ File.separator
						+ SettingUtils.DOWNLOAD_SAVE_BASE_DIR
						+ File.separator
						+ SettingUtils.SAVE_APPLICATION_DIR
						+ File.separator +fileName;
						Log.i("PLJ", "pafPath==>"+pafPath);
						intent.putExtra("file_name", pafPath);
						break;
					case 1:
						
						String filePath = Environment.getExternalStorageDirectory()
								+ File.separator
								+ SettingUtils.DOWNLOAD_SAVE_BASE_DIR
								+ File.separator
								+ SettingUtils.SAVE_APPLICATION_DIR
								+ File.separator + "test.swf";
						Log.i("PLJ", "filePath==>"+filePath);
						intent.putExtra("file_name", filePath);
						break;
					default:
						break;
					}
//					Intent intent = new Intent();
//					//MyPdfReaderActivity.class.getPackage().getName();
//					//MyPdfReaderActivity.class.getName();
//					intent.setClassName("com.kang.pdfreader", "com.kang.pdfreader.MyPdfReaderActivity");
//					intent.putExtra("book_id", this.bookId);
//					intent.putExtra("book_name", this.bookName);
					this.context.startActivity(intent);
				} else {
					String url = CommArgs.PATH + fileName;
					// String url="http://58.60.230.54/DuoWeb/upload/test.swf";
					downloadServiceStub.addDownloadTaskByUrl(url);
					startService();
					Toast.makeText(context, "开始下载!", Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(context, "该文件不存在!", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(context,
					context.getResources().getString(R.string.userinfoerror),
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 启动下载服务
	 */
	private void startService() {
		Intent intent = new Intent();
		intent.setAction(Constants.ACTION_START_SERVICE);
		this.context.startService(intent);
	}
}
