/**
 * 
 */
package com.beem.project.btf.ui.loadimages;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.utils.AndroidDeviceUtil;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.BubbleImageHelper;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

/**
 * @author Aaron Lee Created at 下午2:00:17 2015-9-1
 */
public class ImageLoaderConfigers {
	public static final FileNameGenerator fileNameGenerator = new Md5FileNameGenerator();
	public static final String CACHE_LOACL_PATH = "ThumbCaches";
	public static final String CACHE_IMAGELOADER_PATH = "ImageLoader";

	public static void initImageLoader(Context context) {
		// 配置ImageLoader框架参数
		try {
			Runtime runtime = Runtime.getRuntime();
			int memSize = (int) Math.min(
					((runtime.maxMemory() - runtime.totalMemory() + runtime
							.freeMemory()) * 0.875f), Runtime.getRuntime()
							.maxMemory() >> 3);
			//LogUtils.v("memSize:" + memSize);
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					context)
					.threadPriority(Thread.MAX_PRIORITY)
					.threadPoolSize(5)
					.denyCacheImageMultipleSizesInMemory()
					// .diskCacheFileNameGenerator(new Md5FileNameGenerator())
					// .diskCacheSize(50 * 1024 * 1024)
					// .memoryCache(new
					// LruMemoryCache(MAX_MEMORY_CACHE_SIZE)).memoryCacheSize(MAX_MEMORY_CACHE_SIZE)
					.memoryCache(new LruMemoryCache(memSize))
					// .memoryCache(new WeakMemoryCache())
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.defaultDisplayImageOptions(sDefaultGalleryConfig);
			if (BBSUtils.isSDCardAvaliable(context)) {
				File cacheDir = new File(context.getApplicationContext()
						.getCacheDir(), CACHE_IMAGELOADER_PATH);
				builder.diskCache(new LruDiskCache(cacheDir, fileNameGenerator,
						1024 * 1024 * 64));
			} else {
				builder.diskCacheSize(1024 * 1024 * 4);
			}
			if (BeemApplication.sDebuggerEnabled) {
				builder.writeDebugLogs();
			}
			if (ImageLoader.getInstance().isInited()) {
				ImageLoader.getInstance().destroy();
			}
			ImageLoader.getInstance().init(builder.build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void initThumbImageFetcher(Context context) {
		int reqWidth = AndroidDeviceUtil.getScreenWidth(context) / 3;
		int reqHeight = context.getResources().getDimensionPixelSize(
				R.dimen.date_photo_grid_item_height);
		ThumbImageFetcher.getInstance(context).init(context, reqWidth,
				reqHeight);
	}
	public static void destroyThumbImageFetcher() {
		ThumbImageFetcher.destroy();
	}

	public static DisplayImageOptions sDefaultOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(false)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.build();
	public static DisplayImageOptions sDefaultGalleryConfig = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.deafult_imgloading)
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheOnDisk(false)
			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
			.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(false)
			.cacheInMemory(true).
			// displayer(new RoundedBitmapDisplayer(4)).
			build();
	
	// 性别option，如头像
	public final static DisplayImageOptions sexOpt[] = new DisplayImageOptions[] {
			// woman
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.default_headw_selector)
					.showImageForEmptyUri(R.drawable.default_headw_selector)
					.showImageOnFail(R.drawable.default_headw_selector)
					.cacheInMemory(true).cacheOnDisk(true).build(),
			// man
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.default_head_selector)
					.showImageForEmptyUri(R.drawable.default_head_selector)
					.showImageOnFail(R.drawable.default_head_selector)
					.cacheInMemory(true).cacheOnDisk(true).build(), };
	// 性别option，如头像,圆角
	public final static DisplayImageOptions sexOptRound[] = new DisplayImageOptions[] {
			// woman
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.default_headw_selector)
					.showImageForEmptyUri(R.drawable.default_headw_selector)
					.displayer(new RoundedBitmapDisplayer(1000))
					.showImageOnFail(R.drawable.default_headw_selector)
					.cacheInMemory(true).cacheOnDisk(true).build(),
			// man
			new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.default_head_selector)
					.showImageForEmptyUri(R.drawable.default_head_selector)
					.displayer(new RoundedBitmapDisplayer(1000))
					.showImageOnFail(R.drawable.default_head_selector)
					.cacheInMemory(true).cacheOnDisk(true).build(), };
	// 常用option，如时光的图片
	public final static DisplayImageOptions commonOpt = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.deafult_imgloading)
			.showImageForEmptyUri(R.drawable.deafult_imgloading)
			.showImageOnFail(R.drawable.deafult_imgloading).cacheInMemory(true)
			.cacheOnDisk(true).build();
	// 重用图片组的option
	public final static DisplayImageOptions comment_img_options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.comment_img_deafult)
		.showImageForEmptyUri(R.drawable.comment_img_deafult)
		.showImageOnFail(R.drawable.comment_img_deafult)
		.cacheInMemory(true).cacheOnDisk(true).build();
	// 聊天气泡option
	public final static DisplayImageOptions[] bubbleOpts = new DisplayImageOptions[] {
			new DisplayImageOptions.Builder()
					.postProcessor(new BitmapProcessor() {
						@Override
						public Bitmap process(Bitmap bitmap) {
							bitmap = BubbleImageHelper.getInstance(
									BeemApplication.getContext())
									.getBubbleImageBitmap(bitmap,
											R.drawable.bg_msgbox_send_normal,
											true);
							return bitmap;
						}
					}).showImageOnLoading(R.drawable.bg_msgbox_send_normal)
					.showImageForEmptyUri(R.drawable.bg_msgbox_send_normal)
					.showImageOnFail(R.drawable.bg_msgbox_send_normal)
					.cacheInMemory(true).cacheOnDisk(true).build(),
			new DisplayImageOptions.Builder()
					.postProcessor(new BitmapProcessor() {
						@Override
						public Bitmap process(Bitmap bitmap) {
							bitmap = BubbleImageHelper
									.getInstance(BeemApplication.getContext())
									.getBubbleImageBitmap(
											bitmap,
											R.drawable.bg_msgbox_receive_normal,
											true);
							return bitmap;
						}
					}).showImageOnLoading(R.drawable.bg_msgbox_receive_normal)
					.showImageForEmptyUri(R.drawable.bg_msgbox_receive_normal)
					.showImageOnFail(R.drawable.bg_msgbox_receive_normal)
					.cacheInMemory(true).cacheOnDisk(true).build() };
}
