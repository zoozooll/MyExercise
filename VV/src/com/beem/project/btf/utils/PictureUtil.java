package com.beem.project.btf.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.imagefilter.crop.CropActivity;

/**
 * @ClassName: PictureUtil
 * @Description: 图片工具类
 * @author: yuedong bao
 * @date: 2015-4-24 下午1:10:41
 */
public class PictureUtil {
	private final static int MAX_NUM_PIXELS = 320 * 490;
	private final static int MIN_SIDE_LENGTH = 350;
	private static Uri clipimageUri = null;
	private static Uri unclipimageUri = null;
	private static Uri cartoonimageUri = null;

	/**
	 * 把bitmap转换成String
	 * @param filePath
	 * @return
	 */
	public static String bitmapToString(String filePath) {
		Bitmap bm = getSmallBitmap(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}
	/**
	 * 计算图片的缩放值
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = (int) Math.ceil(1f * height / reqHeight);
			final int widthRatio = (int) Math.ceil(1f * width / reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = com.butterfly.vv.camera.Utils
				.calculateInSampleSize(options, 480, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	/**
	 * 根据路径删除图片
	 * @param path
	 */
	public static void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}
	/**
	 * 获取保存图片的目录
	 * @return
	 */
	public static File getAlbumDir() {
		return BBSUtils.getAppCacheDir(BeemApplication.getContext(), "album");
	}
	/**
	 * @Title: getBytes
	 * @Description: 将bitmap转成byte数组
	 * @param bitmap
	 * @return
	 * @return: byte[]
	 */
	public static byte[] getBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 90, baos);// 压缩位图
			byte[] bytes = baos.toByteArray();// 创建分配字节数组
			return bytes;
		} catch (Exception e) {
			//LogUtils.e(e.getMessage());
			return null;
		} finally {
			if (null != baos) {
				try {
					baos.flush();
					baos.close();
				} catch (IOException e) {
					//LogUtils.e(e.getMessage());
				}
			}
		}
	}
	/** 获得图片，并进行适当的 缩放。 图片太大的话，是无法展示的。 */
	public static Bitmap getBitMapFromPath(Context context, String imageFilePath) {
		int dw = BBSUtils.getScreenWH(context)[0];
		int dh = BBSUtils.getScreenWH(context)[1];
		// Load up the image's dimensions not the image itself
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) dh);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) dw);
		// If both of the ratios are greater than 1,
		// one of the sides of the image is greater than the screen
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				// Height ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				// Width ratio is larger, scale according to it
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		// Decode it for real
		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
		return bmp;
	}
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			//LogUtils.e(e.getMessage());
		}
		return degree;
	}
	public static Bitmap revitionImage(String path) {
		int[] wh = BBSUtils.getScreenWH(BeemApplication.getContext());
		//LogUtils.i("System's w:" + wh[0] + " h:" + wh[1]);
		return revitionImage(path, Math.min(720, wh[0]), Math.min(1280, wh[1]));
	}
	public static Bitmap revitionImage(String path, int sampleWidth,
			int sampleHeight) {
		if (null == path || TextUtils.isEmpty(path) || !new File(path).exists())
			return null;
		BufferedInputStream in = null;
		try {
			// int degree = readPictureDegree(path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			options.inSampleSize = com.butterfly.vv.camera.Utils
					.calculateInSampleSize(options, sampleWidth, sampleHeight);
			options.inJustDecodeBounds = false;
			options.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			////LogUtils.i(/"revitionImage_bitmap.getWidth:" + bitmap.getWidth() + " bitmap.getHeight:" + bitmap.getHeight());
			////LogUtils.i("revitionImage_bitmap.size:" + bitmap.getRowBytes() * bitmap.getHeight());
			return bitmap;
		} catch (Exception e) {
			//LogUtils.e(e.getMessage());
			return null;
		} finally {
			try {
				if (null != in) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(float angle, Bitmap bitmap) {
		if (null == bitmap) {
			return null;
		}
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		bitmap = null;
		return resizedBitmap;
	}
	/**
	 * @Description 生成图片的压缩图
	 * @param filePath
	 * @return
	 */
	public static Bitmap createImageThumbnail(String filePath) {
		if (null == filePath || !new File(filePath).exists())
			return null;
		Bitmap bitmap = null;
		int degree = readPictureDegree(filePath);
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, MAX_NUM_PIXELS);
			//LogUtils.d("image#opts.inSampleSize:%d", opts.inSampleSize);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (Exception e) {
			//LogUtils.e(e.getMessage());
			return null;
		}
		Bitmap newBitmap = rotaingImageView(degree, bitmap);
		return newBitmap;
	}
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? MIN_SIDE_LENGTH : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	// /////////////////////////////////////////////以下方法与手绘相机相关////////////////////////////////////
	/**
	 * @author le yang
	 * @param bitmap
	 * @param Context
	 * @return String path
	 * @category 保存手绘相机拍照生成的图片
	 */
	public static String saveToSDCard(final Context context, Bitmap bitmap)
			throws IOException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		String mBmPath = BBSUtils.getCartoonPath(context, filename);
		FileOutputStream fileOS = new FileOutputStream(mBmPath); // 文件输出流
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOS);
		fileOS.flush();
		fileOS.close();
		return mBmPath;
	}
	public static String saveToDICM(final Context context, Bitmap bitmap)
			throws IOException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		String mBmPath = getCameraFolder(context).getAbsolutePath()
				+ File.separator + filename;
		FileOutputStream fileOS = new FileOutputStream(mBmPath); // 文件输出流
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOS);
		fileOS.flush();
		fileOS.close();
		return mBmPath;
	}
	
	public static String saveToTemp(final Context context, Bitmap bitmap)
			throws IOException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		File folder = getTempImageFolder(context);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File mBmPath = new File(folder, filename);
		FileOutputStream fileOS = new FileOutputStream(mBmPath); // 文件输出流
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOS);
		fileOS.flush();
		fileOS.close();
		return mBmPath.getPath();
	}
	public static File getPictureFolder(Context context) {
		File dir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		return dir;
	}
	public static File getCameraFolder(Context context) {
		return new File(getPictureFolder(context), "Camera");
	}
	public static File getTempImageFolder(Context context) {
		if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
			return new File(context.getExternalCacheDir(), "temp");
		} else {
			return new File(context.getCacheDir(), "temp");
		}
	}
	/**
	 * @author le yang
	 * @category 根据图片绝对路径获取bitmap
	 * @param String 图片路径
	 * @param int 压缩比例
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFormFile(String imgPath, int sampleSize) {
		Bitmap bmTemp = null;
		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = sampleSize;
			// 设置为false,解析Bitmap对象加入到内存中
			opt.inJustDecodeBounds = false;
			bmTemp = BitmapFactory.decodeFile(imgPath, opt);
			////LogUtils.v(" bmTemp:" + bmTemp);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return bmTemp;
	}
	/**
	 * 镜像垂直翻转
	 */
	public static Bitmap convertYBmp(Bitmap bmp) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(1, -1);
		Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
		return convertBmp;
	}
	/**
	 * 镜像水平翻转
	 */
	public static Bitmap convertXBmp(Bitmap bmp) {
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(-1, 1);
		Bitmap convertBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
		return convertBmp;
	}
	/**
	 * 图片缩放处理
	 */
	public static Bitmap ScaleBitmap(float[] scales, Bitmap bmp) {
		Matrix matrix = new Matrix();
		matrix.setScale(scales[0], scales[1]);
		Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		return bm;
	}
	public static Bitmap ScaleBitmap(float scale, Bitmap bmp) {
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap bm = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		return bm;
	}
	/** 缩放图片以适应屏幕 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int ImageShowWidth,
			int ImageShowHeight) {
		Matrix matrix = new Matrix();
		matrix.reset();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float widthratio = ImageShowWidth / (width * 1.0f);
		float heightratio = ImageShowHeight / (height * 1.0f);
		float ratio = Math.min(widthratio, heightratio);
		matrix.postScale(ratio, ratio);
		Bitmap mBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
				true);
		return mBm;
	}
	public static boolean saveBitmapFile(Bitmap bmp, String file) {
		String ext = getFileExt(file);
		CompressFormat format;
		if ("png".equalsIgnoreCase(ext)) {
			format = CompressFormat.PNG;
		} else {
			format = CompressFormat.JPEG;
		}
		try {
			bmp.compress(format, 100, new FileOutputStream(file));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String getFileExt(String file) {
		File f = new File(file);
		String name = f.getName();
		return name.substring(name.lastIndexOf(".") + 1, name.length());
	}
	public static Uri getClipTempImage() {
		//创建一个临时存放剪切图像的路径
		if (clipimageUri == null) {
			clipimageUri = Uri.fromFile(BBSUtils.getCartoonFile(
					BeemApplication.getContext(), "newscamera_temp.jpg"));
		}
		return clipimageUri;
	}
	public static Uri getUnClipTempImage() {
		if (unclipimageUri == null) {
			unclipimageUri = Uri
					.fromFile(new File(BBSUtils.getTakePhotoPath(
							BeemApplication.getContext(),
							"newscamera_toptile_temp.jpg")));
		}
		return unclipimageUri;
	}
	public static Uri getCartoonTempImage() {
		if (cartoonimageUri == null) {
			cartoonimageUri = Uri.fromFile(new File(BBSUtils.getAppCacheDir(
					BeemApplication.getContext(), "temp"), "cartoon_temp.jpg"));
		}
		return cartoonimageUri;
	}
	public static Bitmap decodeUriAsBitmap(Uri paramUri) {
		InputStream in = null;
		try {
			in = BeemApplication.getContext().getContentResolver()
					.openInputStream(paramUri);
			////LogUtils.v("decodeUriAsBitmap_in:" + in);
			Bitmap localBitmap = BitmapFactory.decodeStream(in);
			return localBitmap;
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	/*删除临时图片*/
	public static void delTempImage() {
		delImage(clipimageUri);
		delImage(unclipimageUri);
	}
	private static void delImage(Uri uri) {
		if (uri != null) {
			Log.i("PictureUtil", "imageUri~" + uri + " " + uri.getPath());
			String filepath = uri.getPath();
			//String filepath = UriUtil.getImagePathFromUri(imageUri);
			FileUtil.deleteFile(filepath);
		} else {
			Log.i("PictureUtil", "imageUri~" + uri);
		}
	}
	public static void photoClip(Activity activity, Uri uri, int x, int y) {
		// 调用系统中自带的图片剪裁
		//		Intent localIntent = new Intent("com.android.camera.action.CROP");
		Intent localIntent = new Intent(activity, CropActivity.class);
		localIntent.setDataAndType(uri, "image/*");
		localIntent.putExtra("crop", "true");
		localIntent.putExtra("aspectX", x);
		localIntent.putExtra("aspectY", y);
		localIntent.putExtra("outputX", x);
		localIntent.putExtra("outputY", y);
		localIntent.putExtra("scale", true);
		localIntent.putExtra("output", getClipTempImage());
		localIntent.putExtra("return-data", false);
		localIntent.putExtra("outputFormat",
				Bitmap.CompressFormat.JPEG.toString());
		localIntent.putExtra("noFaceDetection", true);
		activity.startActivityForResult(localIntent, Constants.CLIPPHOTO);
	}
	public static boolean CompressTempBitmap(String filepath,
			String destfilepath) throws IOException {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float maxlenth = 1400f;//宽高都不能大于1400
		int be = 1;
		if (w >= h && w > maxlenth) {
			be = (int) (newOpts.outWidth / maxlenth);
		} else if (w < h && h > maxlenth) {
			be = (int) (newOpts.outHeight / maxlenth);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = nextPowerOfTwo(be);
		Log.i("PictureUtil", "be:" + be + " newOpts.inSampleSize:"
				+ newOpts.inSampleSize);
		bitmap = BitmapFactory.decodeFile(filepath, newOpts);
		FileOutputStream fileOS = new FileOutputStream(destfilepath); // 文件输出流
		boolean isok = bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOS);
		fileOS.flush();
		fileOS.close();
		return isok;
	}
	public static final int nextPowerOfTwo(final int n) {
		int k = n;
		if (k == 0) {
			return 1;
		}
		k--;
		for (int i = 1; i < 32; i <<= 1) {
			k = k | k >> i;
		}
		return k + 1;
	}
	
	public static boolean downloadFile(String downloadUrl, File saveFilePath)
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
