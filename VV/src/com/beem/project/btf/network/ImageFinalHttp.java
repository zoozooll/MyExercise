package com.beem.project.btf.network;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.ChatActivity.IMessageStateListener;
import com.beem.project.btf.ui.activity.ChatActivity.MessageState;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @ClassName: ImageFinalHttp
 * @Description: 发送消息图片类
 * @author: yuedong bao
 * @date: 2015-4-20 下午5:45:32
 */
public class ImageFinalHttp {
	private boolean done;
	private Thread writerThread;
	private final BlockingQueue<ImageInfo> queue;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	public static class ImageInfo {
		private String uri;
		private IMessageStateListener lis;

		private ImageInfo(String uri, IMessageStateListener lis) {
			super();
			this.uri = uri;
			this.lis = lis;
		}
	}

	private static class SingleHolder {
		private static ImageFinalHttp instance = new ImageFinalHttp();
	}

	public static ImageFinalHttp getInstance() {
		return SingleHolder.instance;
	}
	private ImageFinalHttp() {
		this.queue = new ArrayBlockingQueue<ImageInfo>(20, true);
		init();
	}
	public void uploadImageFile(String uri, IMessageStateListener lis) {
		sendImageInfo(new ImageInfo(uri, lis));
	}
	private ImageInfo nextImage() {
		ImageInfo packet = null;
		// Wait until there's a packet or we're done.
		while (!done && (packet = queue.poll()) == null) {
			try {
				synchronized (queue) {
					// 如有有sendPacket后台writerThread先阻塞,将cpu提供给前台线程，提高响应速度
					queue.wait();
				}
			} catch (InterruptedException ie) {
				// Do nothing
			}
		}
		return packet;
	}
	public void sendImageInfo(ImageInfo imageInfo) {
		if (!done) {
			try {
				queue.put(imageInfo);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
				return;
			}
			synchronized (queue) {
				queue.notifyAll();
			}
		}
	}
	protected void init() {
		done = false;
		writerThread = new Thread() {
			@Override
			public void run() {
				writeImageInfos(this);
			}
		};
		writerThread.setName("ImageFinalHttp_writerThread");
		writerThread.setDaemon(true);
		writerThread.start();
	}
	private void writeImageInfos(Thread thisThread) {
		while (!done && (writerThread == thisThread)) {
			final ImageInfo imageInfo = nextImage();
			if (imageInfo != null) {
				File getFile = new File(imageInfo.uri);
				if (getFile != null && getFile.exists()) {
					try {
						String url = AppProperty.getInstance().VVAPI
								+ AppProperty.getInstance().UPLOAD_CHAT;
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("tm_id", LoginManager.getInstance()
								.getJidParsed());
						params.put("session_id", LoginManager.getInstance()
								.getSessionId());
						String response = UploadUtil.uploadImage(
								new String[] { imageInfo.uri }, url, params,
								"chat_file");
						if (TextUtils.isEmpty(response)) {
							// 可能由于网络的原因导致结果返回失败
							postToUI(imageInfo.lis, null, MessageState.failed);
						} else {
							JSONObject jsonObject = new JSONObject(response);
							if (jsonObject.has("url")) {
								JSONArray jsonArray = jsonObject
										.getJSONArray("url");
								for (int i = 0; i < jsonArray.length(); i++) {
									final String image_url = jsonArray
											.getString(i);
									// 换成网络路径并把先前的文件拷到ImageLoader下面去
									String localName = ImageLoaderConfigers.fileNameGenerator
											.generate(image_url);
									File oldFile = getFile;
									File newFile = new File(ImageLoader
											.getInstance().getDiskCache()
											.getDirectory(), localName + ".0");
									BBSUtils.copyFile(oldFile, newFile);
									ImageLoader.getInstance().getMemoryCache()
											.remove(imageInfo.uri);
									ImageLoader.getInstance().getDiskCache()
											.remove(imageInfo.uri);
									//通知UI刷新
									postToUI(imageInfo.lis, image_url,
											MessageState.success);
								}
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
						postToUI(imageInfo.lis, null, MessageState.failed);
					}
				} else {
					postToUI(imageInfo.lis, null, MessageState.failed);
				}
			}
		}
	}
	private void postToUI(final IMessageStateListener lis, final String url,
			final MessageState state) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				lis.onPostExecute(url, state);
			}
		});
	}
}
