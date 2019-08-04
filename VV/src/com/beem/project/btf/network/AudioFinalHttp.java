package com.beem.project.btf.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.ChatActivity.IMessageStateListener;
import com.beem.project.btf.ui.activity.ChatActivity.MessageState;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.vv.utils.CToast;

/**
 * @ClassName: AudioFinalHttp
 * @Description: 此类负责Audio的上传下载,audio默认保存路径：/mnt/sdcard/Android/data/com.beem.project .btf/audio/目录下
 * @author: yuedong bao
 * @date: 2015-4-17 下午3:12:14
 */
public class AudioFinalHttp {
	private FinalHttp fh;
	private Map<String, MediaPlayer> mediaPlays;
	private Map<String, IAudioPlayListener> audioPlayerLis;
	private Timer timer;

	private static class SingletonHolder {
		private static AudioFinalHttp instance = new AudioFinalHttp();
	}

	public static AudioFinalHttp getInstance() {
		return SingletonHolder.instance;
	}
	private AudioFinalHttp() {
		fh = new FinalHttp();
		mediaPlays = new ConcurrentHashMap<String, MediaPlayer>();
		audioPlayerLis = new ConcurrentHashMap<String, IAudioPlayListener>();
	}
	public synchronized void downloadAudio(final String url,
			final AjaxCallBack<File> callBack) {
		String localPath = BBSUtils.getAudioSavePath(url);
		//		//LogUtils.i("download audio url:" + url + " localPath:" + localPath);
		final File file = new File(localPath);
		final Handler downloadHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case 0:
						callBack.onSuccess(file);
						break;
					case 1:
						int errorNo = msg.arg1;
						String strMsg = (String) msg.obj;
						callBack.onFailure(null, errorNo, strMsg);
						break;
					default:
						break;
				}
			}
		};
		if (file.exists()) {
			// 已经存在的不要再下载
			//			//LogUtils.i("the file has downloaded!");
			callBack.onSuccess(file);
			return;
		} else {
			//			//LogUtils.i("the file Start to download " + localPath);
			//fh.download(url, localPath, callBack);
			new Thread("download audio file thread") {
				@Override
				public void run() {
					try {
						if (downloadFile(url, file)) {
							downloadHandler.sendEmptyMessage(0);
						} else {
							Message msg = downloadHandler.obtainMessage(1);
							if (msg == null) {
								msg = new Message();
								msg.what = 1;
								msg.obj = "download failed";
							}
							downloadHandler.sendMessage(msg);
						}
					} catch (IOException e) {
						Message msg = downloadHandler.obtainMessage(1);
						if (msg == null) {
							msg = new Message();
							msg.what = 1;
							msg.obj = "download exception " + e.toString();
						}
						downloadHandler.sendMessage(msg);
					}
				}
			}.start();
		}
	}
	public synchronized void uploadAudio(String recordLocalPath,
			IMessageStateListener lis) {
		new UploadFileTask(recordLocalPath, lis).execute();
	}

	/** 异步任务-录音图片上传 */
	public class UploadFileTask extends AsyncTask<String, Integer, String> {
		private final String recordLocalPath;
		private final IMessageStateListener lis;

		private UploadFileTask(String recordLocalPath, IMessageStateListener lis) {
			super();
			this.recordLocalPath = BBSUtils.getAudioSavePath(recordLocalPath);
			this.lis = lis;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... parameters) {
			File getFile = new File(recordLocalPath);
			String url = AppProperty.getInstance().VVAPI
					+ AppProperty.getInstance().UPLOAD_CHAT;
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("tm_id", LoginManager.getInstance().getJidParsed());
			params.put("session_id", LoginManager.getInstance().getSessionId());
			String result = UploadUtil.uploadFile(getFile, url, params,
					"chat_file");
			if (!TextUtils.isEmpty(result)) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("url");
					if (jsonArray != null && jsonArray.length() > 0) {
						//如果上传成功则将先前的本地文件拷贝到与url有关的路径
						String audio_url = jsonArray.getString(0);
						File oldFile = getFile;
						File newFile = new File(
								BBSUtils.getAudioSavePath(audio_url));
						BBSUtils.copyFile(oldFile, newFile);
						oldFile.delete();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			try {
				//				//LogUtils.i(" upload record result:" + result + " local record file:" + recordLocalPath);
				if (TextUtils.isEmpty(result)) {
					lis.onPostExecute(result, MessageState.failed);
				} else {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("url");
					if (jsonArray != null && jsonArray.length() > 0) {
						String url = jsonArray.getString(0);
						lis.onPostExecute(url, MessageState.success);
					} else {
						//						//LogUtils.e("upload chat file failed:" + result);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				lis.onPostExecute(result, MessageState.failed);
			}
		}
	}

	public interface IAudioPlayListener {
		public void onStart();
		public void onCompletion();
		public void onPlaying(int current, int all);
	}

	public synchronized void startPlaying(final String url,
			final IAudioPlayListener lis) {
		String file = BBSUtils.getAudioSavePath(url);
		//		//LogUtils.i("startPlaying this audio file url:" + url + " file:" + file);
		if (!new File(file).exists()) {
			//			//LogUtils.e("this audio file not exist ,url:" + url + " flie:" + file);
			AudioFinalHttp.getInstance().downloadAudio(url,
					new AjaxCallBack<File>() {
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							CToast.showToast(BeemApplication.getContext(),
									"此音频文件不存在", Toast.LENGTH_SHORT);
							lis.onCompletion();
						}
						@Override
						public void onSuccess(File t) {
							super.onSuccess(t);
							startPlaying(url, lis);
						}
					});
			return;
		}
		MediaPlayer mPlayer = mediaPlays.get(url);
		for (String mediaUrl : audioPlayerLis.keySet()) {
			if (!mediaUrl.equals(url)) {
				stopPlaying(mediaUrl);
			}
		}
		try {
			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
				// 获取绝对路径来播放音频
				mPlayer.setDataSource(file);
				mPlayer.prepare();
				mPlayer.start();
				mediaPlays.put(url, mPlayer);
				audioPlayerLis.put(url, lis);
				final int allTimeCount = mPlayer.getDuration();
				if (timer == null) {
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							lis.onPlaying(mediaPlays.get(url)
									.getCurrentPosition(), allTimeCount);
						}
					}, 0, 100);
				}
				mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						//						//LogUtils.i("~~onCompletion~~");
						stopPlaying(url);
					}
				});
				lis.onStart();
			} else {
				if (mPlayer.isPlaying()) {
					stopPlaying(url);
					return;
				}
			}
		} catch (IOException e) {
			stopPlaying(url);
		}
	}
	private synchronized void stopPlaying(String url) {
		MediaPlayer mPlayer = mediaPlays.get(url);
		if (mPlayer != null) {
			if (timer != null) {
				timer.purge();
				timer.cancel();
				timer = null;
			}
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
			IAudioPlayListener lis = audioPlayerLis.get(url);
			if (lis != null)
				lis.onCompletion();
			mediaPlays.remove(url);
			audioPlayerLis.remove(url);
		}
	}
	public synchronized void stopAllPlaying() {
		for (String mediaUrl : audioPlayerLis.keySet()) {
			stopPlaying(mediaUrl);
		}
	}
	public boolean downloadFile(String downloadUrl, File saveFilePath)
			throws IOException {
		int fileSize = -1;
		int downFileSize = 0;
		int progress = 0;
		URL url = new URL(downloadUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (null == conn) {
			return false;
		}
		// 读取超时时间 毫秒级
		conn.setReadTimeout(10000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			fileSize = conn.getContentLength();
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(saveFilePath);
			byte[] buffer = new byte[1024];
			int i = 0;
			int tempProgress = -1;
			while ((i = is.read(buffer)) != -1) {
				downFileSize = downFileSize + i;
				// 下载进度
				progress = (int) (downFileSize * 100.0 / fileSize);
				fos.write(buffer, 0, i);
			}
			fos.flush();
			fos.close();
			is.close();
		}
		return true;
	}
}
