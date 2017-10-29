package com.iskyinfor.duoduo.ui.shop;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;


/**
 * 异步加载图片类
 * @author zhoushidong
 *
 */
public class ImageTask extends AsyncTask<String, Integer, byte[]> {

	private View view;
	private Bitmap bitmap;
	String urlStr;
	public ImageTask() {
		super();
	}
	
	public ImageTask(View view) {
		super();
		this.view = view;
	}
	/**
	 * 进行后台处理
	 */
	@Override
	protected byte[] doInBackground(String... params) {
		urlStr = params[0];
		URL url = null;
		InputStream is = null;
		byte [] buff = new byte[2048];
		ByteArrayOutputStream baos = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			is = http.getInputStream();
			//drawable = Drawable.createFromStream(is, "src");
			baos = new ByteArrayOutputStream();
			
			int temp = 0;
			while ((temp = is.read(buff)) != -1) {
				baos.write(buff , 0 , temp);
			}
			buff = baos.toByteArray();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					
				}
				if (baos != null) {
					baos.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return buff;
	}

	/**
	 * 最先执行
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 完成任务。。跟UI交互
	 */
	@Override
	protected void onPostExecute(byte[] result) 
	{
		bitmap = BitmapFactory.decodeByteArray(result , 0 , result.length);
		File file = new File(StaticData.IMAGE_DOWNLOAD_ADDR);
		FileOutputStream fos = null;
		int end = urlStr.lastIndexOf('/');
		
		//判断是否有 可用的空间
		//if (SdcardUtil.isAvailableBlocks(file, new Long(result.length))) {
			
		
		file = new File(StaticData.IMAGE_DOWNLOAD_ADDR);
		boolean createSuccess = true;
		if (!file.exists()) {
			createSuccess = file.mkdirs();
		}
		try {
			if (createSuccess) {
				file = new File(StaticData.IMAGE_DOWNLOAD_ADDR + urlStr.substring(end));
				fos = new FileOutputStream(file);
				fos.write(result);
			} else {
				//Toast.makeText(BookstoreActivity.this, "", Toast.LENGTH_SHORT);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		if (bitmap == null) {
			((ImageView) view).setImageResource(R.drawable.default_icon);
		}
		
		if (view instanceof ImageView) {
			((ImageView) view).setImageBitmap(bitmap);
		}
		
		super.onPostExecute(result);
	}

	/**
	 * 更新进度条
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	
}
