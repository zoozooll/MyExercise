package com.iskyinfor.duoduo.ui.shop.task;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IFilePathTranslate0200040001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.FilePathTranslate0200040001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.ui.lesson.LessonPlayVideoActivity;

public class VideoTask extends AsyncTask<Void, Void, Void> {
	private Context mContext = null;
	private String videoId = null;
	private Map<String, String> videoMap = null;
	IFilePathTranslate0200040001 videoPath = null;
	DownloadServiceStub downloadServiceStub = null;

	public VideoTask(Context context, String proid) {
		mContext = context;
		videoId = proid;
	}

	// 预处理
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		videoPath = new FilePathTranslate0200040001Impl();
		videoMap = new HashMap<String, String>();
		downloadServiceStub = new DownloadServiceStub(mContext);
	}

	// 后台运行
	@Override
	protected Void doInBackground(Void... params) {
		try {
			videoMap = videoPath.getProPath(videoId);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 更新数据
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	// 返回结果
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		String fileName = null;

		if (videoMap != null) {
			fileName = videoMap.get(DataConstant.PRO_VIEW_PATH_KEY);
			Log.i("james", "从WEB端取到的视屏名称是：：" + fileName);

			//判断该文件是否在SD卡，如果在就直接播放；反之去下载。。
			if (fileName != null || !"".equals(fileName)) 
			{
				if (downloadServiceStub.isFileExit(fileName)) 
				{
					Intent intent = new Intent(mContext, LessonPlayVideoActivity.class);
					String filePath = Environment.getExternalStorageDirectory()
							+ File.separator
							+ SettingUtils.DOWNLOAD_SAVE_BASE_DIR
							+ File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR
							+ File.separator + fileName;

					Log.i("james", "要播放视频的SD卡路径：" + filePath);

					intent.putExtra("file_name", filePath);
					this.mContext.startActivity(intent);

				} 
				else 
				{
					String url = CommArgs.PATH + fileName;
					Log.i("james", "要下载视频的路径：" + url);
					// String url="http://58.60.230.54/DuoWeb/upload/test.swf";
					downloadServiceStub.addDownloadTaskByUrl(url);
					startService();
					Toast.makeText(mContext, R.string.start_downloading_video,Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(mContext, R.string.downloading_path_notnull,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.i("james", "找不到WEB端视频的路径");
		}
	}

	/**
	 * 启动下载服务
	 */
	private void startService() 
	{
		Intent intent = new Intent();
		intent.setAction(Constants.ACTION_START_SERVICE);
		this.mContext.startService(intent);
	}

}
