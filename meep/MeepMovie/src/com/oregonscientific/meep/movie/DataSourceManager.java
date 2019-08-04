/**
 * 
 */
package com.oregonscientific.meep.movie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.graphics.Region;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;
import android.view.View;

import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.database.table.TableMovie;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.opengl.OSButton;

/**
 * 
 * @author aaronli
 *
 */
public class DataSourceManager {
	
	private static final String TAG = "DataSourceManager";
	//private static Context mContext;
	private static final String BASIC_STORAGE_PATH = MEEPEnvironment.getMediaStorageDirectory().getPath() + File.separatorChar;
	private static List<OsListViewItem> mListViewItemList ;

	//movie
	private static final String MOVIE_PATH_DATA_DIR =  BASIC_STORAGE_PATH + "movie/data/";
	private static final String MOVIE_PATH_LARGE_ICON_DIR =  BASIC_STORAGE_PATH + "movie/icon_l/";
	private static final String MOVIE_PATH_LARGE_MIRROR_ICON_DIR =  BASIC_STORAGE_PATH + "movie/icon_lm/";
	private static final String MOVIE_PATH_LARGE_DIM_ICON_DIR =  BASIC_STORAGE_PATH + "movie/icon_ld/";
	private static final String MOVIE_PATH_ICON_DIR =  BASIC_STORAGE_PATH + "movie/icon/";
	private static final String MOVIE_PATH_SMALL_ICON_DIR =  BASIC_STORAGE_PATH + "movie/icon_s/";
	
	//scan path
	private static final String PATH_MOVIE_BT_DIR = "/sdcard/bluetooth/";
	
	public static final Map<String, String> SUPPORT_VIDEO_FORMAT = new HashMap<String, String>();
	public static final String MEDIA_SUPPORTED = "support";
	
	static {
		SUPPORT_VIDEO_FORMAT.put("avi", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("h263", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("h264", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("xvid", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("mp4", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("mp1", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("mpeg", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("mpg", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("tp", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("3gp", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("mkv", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("rm", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("rmvb", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("tp", MEDIA_SUPPORTED);
		SUPPORT_VIDEO_FORMAT.put("ts", MEDIA_SUPPORTED);
	}
	
	//private final static String BASE_STORAGE_PATH = "//home/";
	//private final String PATH_PHOTO_DATA_DIR = BASE_STORAGE_PATH + "movie/data/";
	public DataSourceManager(Context context) {
		super();
		//this.mContext = context;
	}

	//private static Bitmap mDummyImage = null;
	//private static List<OSButton> mButtonList = null;
	//private static int mLastIndex = 0;
	//private static List<OSButton> mButtonPool = null;
	//private static List<File> mFileList = null;
	//private boolean mIsLoadingButtonImages = false;
	//private boolean mIsInitSnackShape = false;
	//private int mDummyImageTxtID = -1;
	/**
	 * scan the all format folder with images 
	 * @return the file List contain all image files.
	 */
	public static List<File> scanDirectList() {
		List<File> mFileList = new ArrayList<File>();
		
		traverseDir(new File("/mnt/sdcard/DCIM/Camera/"), mFileList);
		//traverseDir(Environment.getExternalStorageDirectory(), mFileList);
        traverseDir(new File(Environment.getExternalStorageDirectory(), "Movies"), mFileList);
        traverseDir(new File(MOVIE_PATH_DATA_DIR), mFileList);
	       // traverseDir(new File("/mnt/sdcard/home/movie/data/"), mFileList);
	    traverseDir(new File("/storage/external_storage/"), mFileList);
	    traverseDir(new File(PATH_MOVIE_BT_DIR), mFileList);
		
	    //Collections.sort(mFileList);
		//Collections.reverse(mFileList);
		
		return mFileList;
	
	}
	
	/**
	 * make the {@link OsListViewItem} List with file List
	 * @param fileList The data source,make from
	 * @param items The items which to be add.if null should catch exception
	 */
	public static void getDataSource(List<File> fileList, List<OsListViewItem> items) {
		//Log.d(TAG, "cdList " + Arrays.toString(fileList.toArray()));
		// modified by aaronli May22. The movie's name should not with extension.
		for (File thisFile : fileList) {
			String name = getFileNameWithoutExten(thisFile.getAbsolutePath());
			//2013-3-18 -Amy- Rename after refres
			//String photoName = filename.substring(0,filename.lastIndexOf("."));
			
			
			// 2013-4-12 Winder Hao  To determine whether the file can be taken to the thumbnail, in order to determine the video file true
			/*Bitmap mBitmap= ThumbnailUtils.createVideoThumbnail(thisFile.getAbsolutePath(), Thumbnails.MINI_KIND);
			if (mBitmap!=null)
			{*/
				OsListViewItem item = new OsListViewItem();
				//item.setImage(bmap);
				item.setName(name);
				item.setPath(thisFile.getAbsolutePath());
				items.add(item);
				//mBitmap.recycle();
				
			//}
			
		}
	}
	

	//winder
	public static List<OsListViewItem>	getAllItems(boolean  refresh)
	{	
		if (refresh || mListViewItemList == null || mListViewItemList.size() == 0){
		if (mListViewItemList == null) {
			mListViewItemList = new Vector<OsListViewItem>();
		} else 
			{
			mListViewItemList.clear();
			}
		synchronized (mListViewItemList) {
			List<File> fileList = scanDirectList();
			getDataSource(fileList, mListViewItemList);
		}
		}
		return mListViewItemList;
	}
	
	// added by aaronli May23,2013
	public static void deleteItem(OsListViewItem... items) {
		for (OsListViewItem o : items) {
			try {
				mListViewItemList.remove(o);
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveImage(Bitmap bmp, String path)
	{
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bmp.compress(CompressFormat.PNG, 80, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getExtension(File f) {
		return (f != null) ? getExtension(f.getName()) : "";
	}

	public static String getExtension(String filename) {
		return getExtension(filename, "");
	}

	public static String getExtension(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1);
			}
		}
		return defExt;
	}

	public static String trimExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');
			if ((i > -1) && (i < (filename.length()))) {
				return filename.substring(0, i);
			}
		}
		return filename;
	}

	public static String getFileNameWithoutExten(String path) {
		int beginIndex = path.lastIndexOf(File.separator) + 1;
		if (beginIndex < 0) {
			beginIndex = 0;
		}
		int endIndex = path.lastIndexOf('.');
		if (endIndex < beginIndex) {
			endIndex = path.length();
		}
		return path.substring(beginIndex, endIndex);
	}
	
	private static void traverseDir(File dir, List<File> theList) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					traverseDir(files[i], theList);
				} else {
					//2013-6-28 -Amy- add .3gp/... formate extension in video
					if (!files[i].isHidden()) {
						String extension = getExtension(files[i]).toLowerCase();
						if (SUPPORT_VIDEO_FORMAT.containsKey(extension)) {
							theList.add(files[i]);
						}
					}
				}
			}
		}
	}
	
	/** This method should combine to traverseDir
	 * 
	 * @param dir
	 * @param theList
	 */
	/*private static void traverseDirOnce(File dir, List<File> theList) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					//traverseDir(files[i], theList);
				} else {
					if (!files[i].isHidden() && (files[i].getName().toLowerCase().contains(Global.FILE_TYPE_MP4))) {
						theList.add(files[i]);
						Log.e("music-traversal", files[i].getAbsolutePath());
					}
				}
			}
		}
	}

	private static void traverseFileList(List<File> List1, List<File> finalList) {
		//Log.i("photo-traversal", "traverseDir LOADED");
		if (List1 != null) {
			for (int i = 0; i < List1.size(); i++) {
                // skip all files or folders starting with a dot
                if (List1.get(i).getName().startsWith(".")) {
                    continue;
                }

                if (!List1.get(i).isHidden()) {
					finalList.add(List1.get(i));
					//Log.i("traverseFileList", List1.get(i).getAbsolutePath());

				}
			}
		}
	}*/
	/*pathDataDir = MOVIE_PATH_DATA_DIR;
	pathLargeIconDir = MOVIE_PATH_LARGE_ICON_DIR;
	pathLargeMirrorIconDir = MOVIE_PATH_LARGE_MIRROR_ICON_DIR;
	pathLargeDimIconDir = MOVIE_PATH_LARGE_DIM_ICON_DIR;
	pathSmallIconDir = MOVIE_PATH_SMALL_ICON_DIR;
	pathIcon = MOVIE_PATH_ICON_DIR;*/

	/**
	 * @author aaronli at Mar18 2013
	 * @param button the button loading images in
	 */
	public static void loadingMovieButtonImages(OSButton button,Bitmap mBmpBg) {
		
		
		try {				
			//get icon image
			String imgName = button.getName();
			final String mirrorImagePath = MOVIE_PATH_LARGE_MIRROR_ICON_DIR + imgName + Global.FILE_TYPE_PNG;
			Bitmap mirrorImg = BitmapFactory.decodeFile(mirrorImagePath);
			if(mirrorImg== null)
			{
				mirrorImg = getLargeBitmap(imgName, mBmpBg, button.getContentPath().get(0));
				if(mirrorImg != null) {
					MediaManager.saveImageToExternal(mirrorImg, mirrorImagePath);
				} else {
					return;
				}
			}
				
			button.setTextureBmp(mirrorImg);
			
			Bitmap dimBmap =  BitmapFactory.decodeFile(MOVIE_PATH_LARGE_DIM_ICON_DIR + imgName + ".png");
			if(dimBmap== null) {
				dimBmap = MediaManager.getDimImage2(mirrorImg);
				saveImage(dimBmap, MOVIE_PATH_LARGE_DIM_ICON_DIR + imgName + ".png");		
			}
			
			button.setDimTextureBmp(dimBmap);
			button.setName(imgName);
			//resetDummyIndex();
		} catch (Exception ex) {
			Log.e("FunctionMenuAnimationCtrl", "initFunctionMenu->cannot load icon bitmap :" + ex.toString(), ex);
		}
	}
	
	
	public static Bitmap getLargebitmapBitmap(Bitmap mBmpBg, String path) {
		return getLargeBitmap(getFileNameWithoutExten(path), mBmpBg, path);
	}
	
	private static Bitmap getLargeBitmap(String imgName, Bitmap mBmpBg, String path) {
		String largeImagePath = MOVIE_PATH_LARGE_ICON_DIR + imgName  + ".png";
		Bitmap largeBmap = null;
		try {
			
			largeBmap = BitmapFactory.decodeFile(largeImagePath);
			
			if (largeBmap == null) {
				Bitmap icon = BitmapFactory.decodeFile(MOVIE_PATH_ICON_DIR + imgName + ".png");
				// modified by aaronli Jun27 2013,fixed 4706
				if (icon == null) {
					
					icon = MediaManager.generateMovieThumbnail(path);
				} 
				
				if (icon != null) {
					largeBmap = getMovieLargeIcon(icon, mBmpBg);
				}
				// fixed end
			}
			if (largeBmap != null) {
				MediaManager.saveImageToExternal(largeBmap, largeImagePath);
				//mirrorImg = convertReflectionImage(largeBmap);
				//System.out.println(button.getContentPath().get(0)+"*222222222222");
				//largeBmap.recycle();
				//System.out.println(button.getContentPath().get(0)+"*333333333333");
				//System.out.println(button.getContentPath().get(0)+"*111111111111111");
	
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return largeBmap;
	}
	
	/*public int getDummyImageTxtID() {
		if (mDummyImageTxtID == -1) {
			//Bitmap bmp = BitmapFactory.decodeFile( BASIC_STORAGE_PATH + "app/default/dummy.png");
			//mDummyImageTxtID =  MediaManager.loadTexture(bmp);
			mDummyImageTxtID =  MediaManager.loadTexture(mDummyImage);
			//Log.d("dummyTextureId", "dummy textureID:" + mDummyImageTxtID);
		}
		//Log.d("mDummyImageTxtID", ""+mDummyImageTxtID);
		return mDummyImageTxtID;
	}
	
	public synchronized static void resetDummyIndex() {
		
//		for (int i = 0; i < mButtonPool.size(); i++) {
//			Log.d("resetDummyIndex", "" + i + ":" + mButtonPool.get(i).getTexureID() );
//		}

		for (int i = 0; i < mButtonList.size(); i++) {
			if (mButtonList.get(i).getTexureID() == 0) {
				if (i != 0) {
					mLastIndex = i-1;
					//Log.d("resetDummyIndex", "size: " +mButtonList.size()+ " Dummy index:" + i);
					return;
				}
			}
		}
		mLastIndex = mButtonList.size()-1;
		
	}
	
	private synchronized OSButton findOsButtonByName(String name)
	{
		for(int i = 0; i<mButtonPool.size();i++)
		{
			if(mButtonPool.get(i).getName().equals(name)){
				return mButtonPool.get(i);
			}
		}
		return null;
	}*/
	private static Bitmap getMovieLargeIcon(Bitmap oriBmp, Bitmap bmpBg) {
		/*try {
			
			//Bitmap bmp = oriBmp;
			float scale = 296f / oriBmp.getWidth();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);

			Bitmap resizedBitmap = Bitmap.createBitmap(oriBmp, 0, 0, oriBmp.getWidth(), oriBmp.getHeight(), matrix, true);
			int cuty = (int) (resizedBitmap.getHeight() / 2 - 89);
			Bitmap cropBitmap = null;
			if (resizedBitmap.getHeight() > 178) {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, cuty, resizedBitmap.getWidth(), 178);
			} else {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
			}

			Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(mutableBm);
			canvas.drawBitmap(oriBmp, null, new Rect(30, 54, 275, 190), null);

			return mutableBm;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;*/
		
		// 防止出现Immutable bitmap passed to Canvas constructor错误
		Bitmap bitmap1 = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		//Log.d(TAG, "bitmap1 "+bitmap1.getWidth() +" "+bitmap1.getHeight());
		
		int w = 330;
		int h = 185;
		
		Bitmap newbitmap = MeepStorageCtrl.resizeImage(oriBmp, w, h);
		int nw = newbitmap.getWidth(); 
		int nh = newbitmap.getHeight();
		//System.out.println("w = " + nw + "  *h = " + nh);
		// Bitmap newBitmap = null;
		//Paint paint = new Paint();
		// newBitmap = Bitmap.createBitmap(bitmap1);
		Canvas canvas = new Canvas(bitmap1);
		// canvas.drawColor(Color.BLACK);

		/*
		 * int w = bitmap1.getWidth(); int h = bitmap1.getHeight();
		 * System.out.println("w = " + w + "  *h = " + h); int w_2 =
		 * bitmap2.getWidth(); int h_2 = bitmap2.getHeight();
		 * System.out.println("w_2 = " + w_2 + "  *h_2 = " + h_2);
		 */
		// paint.setColor(Color.TRANSPARENT);
		// paint.setAlpha(0);
		//canvas.drawRect(100, 100, bitmap1.getWidth(), bitmap1.getHeight(),paint);
		//
		// paint = new Paint();
		canvas.drawBitmap(newbitmap, 40, 70, null);
		// System.out.println("w-w_2 = " + Math.abs(w - w_2) / 2 + "  h-h_2 = "
		// + Math.abs(h - h_2) / 2);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		
		canvas.clipRect(31, 54, 243, 137);
		canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE); 
		// 存储新合成的图片
		canvas.restore(); 
		Log.d(TAG, "bitmap2 "+bitmap1.getWidth() +" "+bitmap1.getHeight());
		return bitmap1;
	}

	public static void deleteItemFiles(OsListViewItem item) {
		
		/*TableMovie movie = new TableMovie();
		movie.setDataAddr(item.getPath());*/
		if (item != null) {
			deleteItemFile(item.getPath());
			deleteItemFile(MOVIE_PATH_SMALL_ICON_DIR + item.getName() + ".png");
			deleteItemFile(MOVIE_PATH_LARGE_DIM_ICON_DIR + item.getName()+ ".png");
			deleteItemFile(MOVIE_PATH_LARGE_ICON_DIR + item.getName()+ ".png");
		}
	}
	private static void deleteItemFile(String path)
	{
		//delete the real item
				File file = new File(path);
				try {
					if (file.exists()) {
						file.delete();
					}
				} catch (Exception e) {
					Log.e("MovieFullListView", "delete file error : " + e.toString());
				}
	}
	public static boolean deleteDataItems(OsListViewItem... items) {
		for (OsListViewItem o : items) {
			mListViewItemList.remove(o);
		}
		return true;
	}


	public static boolean fileNameisAlreadyExecised(String name) {
		for (int i = 0; i < mListViewItemList.size(); i++)
		{
			// Log.d("All photo name", "All photo name: " +
			// mListViewItemList.get(i).getName());
			// Log.d("All photo name", "All photo name: " +
			// mListViewItemList.get(i).getName().substring(0,
			// mListViewItemList.get(i).getName().lastIndexOf(".")));
			if (mListViewItemList.get(i).getName().toLowerCase()
					.equals(name.toLowerCase()))
			{
				return true;
				
			}
		}
		return false;
	}

	public static void renameFile(String renameFileName, OsListViewItem item) {
		/*String pathTo = item.getPath().substring(0,
				item.getPath().length() - item.getName().length() - 4)
				+ renameFileName;*/
		
//			TableMovie movie = new TableMovie();
//			movie.setDataAddr(pathTo + ".mp4");
//			
		//String a = item.getPath().substring(0, item.getPath().indexOf("."));
		final String fullPath = item.getPath();
		// fixed renamed error.the error appeared after the data folder change to /sdcard/.home/
		// modified by aaronli at Jul16 2013
		String b = getExtension(fullPath);
		//Log.d(TAG, "From:" + item.getPath());
		
		File from = new File(fullPath);
		File to = new File(from.getParentFile(), renameFileName +'.' + b);
		boolean  f = from.renameTo(to);
		//Log.d(TAG, "to " + to.getPath()+"  ->  "+ f);
		//2013-6-28 -Amy- Change the extension and reset the path in renameFile method
		if (f) {
			item.setPath(to.getPath());
			// modified end
			File fromIcon = new File(MOVIE_PATH_LARGE_ICON_DIR + item.getName() + ".png");
			File toIcon = new File(MOVIE_PATH_LARGE_ICON_DIR + renameFileName + ".png");
			fromIcon.renameTo(toIcon);
			
			fromIcon = new File(MOVIE_PATH_LARGE_DIM_ICON_DIR + item.getName() + ".png");
			toIcon = new File(MOVIE_PATH_LARGE_DIM_ICON_DIR + renameFileName + ".png");
			fromIcon.renameTo(toIcon);
			
			fromIcon = new File(MOVIE_PATH_SMALL_ICON_DIR + item.getName()+ ".png");
			toIcon = new File(MOVIE_PATH_SMALL_ICON_DIR + renameFileName + ".png");
			fromIcon.renameTo(toIcon);
			
			fromIcon = new File(MOVIE_PATH_ICON_DIR + item.getName()+ ".png");
			toIcon = new File(MOVIE_PATH_ICON_DIR + renameFileName + ".png");
			fromIcon.renameTo(toIcon);
			
			fromIcon = new File(MOVIE_PATH_LARGE_MIRROR_ICON_DIR + item.getName()+ ".png");
			toIcon = new File(MOVIE_PATH_LARGE_MIRROR_ICON_DIR + renameFileName + ".png");
			fromIcon.renameTo(toIcon);
		}
	}
	
	
	/*private static Bitmap convertReflectionImage(Bitmap bmap) {
		// change image color to dim
		int argb;
		int width = bmap.getWidth();
		int oriHeight = bmap.getHeight();
		int height = (int) (bmap.getHeight() * 1.5f);
		Bitmap bmapDim = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		for (int j = 0; j < width; j++) {
			for (int k = 0; k < oriHeight; k++) {
				argb = bmap.getPixel(j, k);
				bmapDim.setPixel(j, k, argb);
			}
		}

		int half = (int) (bmap.getHeight() / 2);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < half; j++) {
				argb = bmap.getPixel(i, bmap.getHeight() - 1 - j);

				int alpha = argb >> 24;
				int red = (argb & 0x00FF0000) >> 16;
				int green = (argb & 0x0000FF00) >> 8;
				int blue = (argb & 0x000000FF);

				if (alpha != 0) {
					alpha = (int) (128 * (1 - (j * 1.0f / half)));
				}
				// Log.d("alpha", "alpha:"+alpha);
				bmapDim.setPixel(i, oriHeight + j, Color.argb(alpha, red, green, blue));
			}
		}
		return bmapDim;
	}
	private static Bitmap convertSnackCdThumbnail(Bitmap orgImage, String name, Bitmap bmpBg, Bitmap bmpTop) {
		try {
			Bitmap bmp = orgImage;
			float scale = 300f / bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);

			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			int cutx = (int) (resizedBitmap.getWidth() / 2 - 150);
			Bitmap cropBitmap = null;
			int moveX = 0;
			if (resizedBitmap.getWidth() > 300) {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 300, resizedBitmap.getHeight());
			} else {
				moveX = (int)((300 - resizedBitmap.getWidth())/2);
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
			}
			resizedBitmap.recycle();

			Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(mutableBm);
			
			canvas.drawBitmap(cropBitmap, 40 + moveX, 10, null);
			canvas.drawBitmap(bmpTop, 0, 0, null);
			
			cropBitmap.recycle();
			bmpBg.recycle();
			bmpTop.recycle();

			//saveImage(mutableBm, path);
			
			return mutableBm;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	
	private static void convertFullListViewThumbnail(Bitmap orgImage, String name, Bitmap bmpBg, Bitmap bmpTop) {
		try {
			Bitmap bmp = orgImage;
			float scale = 79f / bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);

			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			int cutx = (int) (resizedBitmap.getWidth() / 2 - 46);
			Bitmap cropBitmap = null;
			int moveX = 0;
			if (resizedBitmap.getWidth() > 90) {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 90, resizedBitmap.getHeight());
			} else {
				moveX = (int)((90 - resizedBitmap.getWidth())/2);
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
			}

			Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(mutableBm);
			
			canvas.drawBitmap(cropBitmap, 10 + moveX, 2, null);
			canvas.drawBitmap(bmpTop, 0, 0, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}*/

	
	
	
}
