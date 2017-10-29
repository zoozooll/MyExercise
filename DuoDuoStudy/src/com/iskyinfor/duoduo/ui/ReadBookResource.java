package com.iskyinfor.duoduo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

//import com.kang.pdfreader.MyPdfReaderActivity;

public class ReadBookResource {
	@SuppressWarnings("unused")
	private static Context mContext = null;

	public ReadBookResource(){}
	
	public ReadBookResource(Context context)
	{
		mContext = context;
	}

	//播放视频
	public static void getVideoFileIntent(Activity context,String path)
	{
		try {
			Uri uri = Uri.parse(path);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri, "video/*");
			context.startActivity(intent);
			context.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//阅读pdf电子书
	public static void getPDFFileIntent(Context context, String path,String bookid,String bookName) {
		/**
		 * pdf阅读所需参数
		 */
		
//		com.kang.pdfreader.MyPdfReaderActivity.INTENT_BOOKID
//		com.kang.pdfreader.MyPdfReaderActivity.INTENT_BOOKNAME
//		com.kang.pdfreader.MyPdfReaderActivity.INTENT_BOOKPATH

		
		
//		File file = new File(param);
//		Uri path = Uri.fromFile(file);
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.addCategory("android.intent.category.DEFAULT");
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setDataAndType(path, "application/pdf");
//		context.startActivity(intent);
		Log.i("getPDFFileIntent", "path===:"+path);
		Log.i("getPDFFileIntent", "bookid===:"+bookid);
		Log.i("getPDFFileIntent", "bookName===:"+bookName);
		/*Intent intent = new Intent();
		intent.setClass(context, MyPdfReaderActivity.class);
		intent.putExtra(MyPdfReaderActivity.INTENT_BOOKID,bookid );
		intent.putExtra(MyPdfReaderActivity.INTENT_BOOKNAME, bookName);
		intent.putExtra(MyPdfReaderActivity.INTENT_BOOKPATH, path);
		context.startActivity(intent);*/
	}

}
