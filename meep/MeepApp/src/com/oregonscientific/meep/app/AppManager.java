/**
 * 
 */
package com.oregonscientific.meep.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.global.AppPermissionControl;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.permission.Component;

/**
 * 
 * The Class used to manage the installed applications the applications caches
 * 
 * @author aaronli
 * 
 */
public class AppManager {

	private static final String TAG = "AppManager";
	private static List<OsListViewItem> sAppListItems;

	public final static String KEY_APPS_CATEGORY = "apps_category";
	// "base store page used /sdcard/home instead of data/home"
	private final static String BASE_STORAGE_DIR = MEEPEnvironment.getMediaStorageDirectory().getPath()+File.separatorChar;
	//private final static String BASE_STORAGE_DIR = "/data/home/";
	private final static String PATH_ICON_DIR = BASE_STORAGE_DIR + "app/icon/";
	private final static String PATH_SMALL_ICON_DIR = BASE_STORAGE_DIR + "app/icon_s/";
	private final static String PATH_LARGE_ICON_DIR = BASE_STORAGE_DIR + "app/icon_l/";
	private final static String PATH_LARGE_DIM_ICON_DIR = BASE_STORAGE_DIR + "app/icon_ld/";
	private final static String PATH_LARGE_MIRROR_ICON_DIR = BASE_STORAGE_DIR + "app/icon_lm/";
	private final static Rect RECT_SNACK = new Rect(69, 36, 69 + 190, 36 + 190);
	private String[] mGameArray = null;
	private String[] mBlockArray = null;
	// public ArrayList<String> mBlockList_full; // For Game launcher & App

	private ArrayList<String> mBlockList; // For Game launcher & App
	private ArrayList<String> mGameList;
	private Context mContext;
	private PackageManager packageManager;
	private OnApkListChangeListener mOnApkListChangeListener;
	public static Account mAccount;
	private String user;
	private static List<Component> components_black;
	
	public AppManager(Context mContext)
	{
		super();
		this.mContext = mContext;
	}

	public List<OsListViewItem> getAllItems(boolean refresh) {

		if (refresh || sAppListItems == null || sAppListItems.size() == 0)
		{
			if (sAppListItems == null)
			{
				sAppListItems = new Vector<OsListViewItem>();
			} else
			{
				sAppListItems.clear();
			}
			synchronized (sAppListItems)
			{
				loadAppListItem();
			}
		}
		return sAppListItems;
	}
	/**
	 * load the app list and add messages to caches;
	 */
	public static boolean skipThisApps(String name, AppType appType, List<String> blockList) {
		boolean gameFound = false;
		if (appType == AppType.App)
		{
			// Skip our app
			if (name.indexOf("com.oregonscientific") == 0)
			{
				return true;
			}
		}

		if (blockList != null)
		{
			// loadAppListItem();

			for (int i = 0; i < blockList.size(); i++)
			{

				if (name.equalsIgnoreCase(blockList.get(i)))
				{
					return true;
				}
			}
		}
		return false;
	}
		
	private List<OsListViewItem> loadAppListItem() {
		final List<String> mBlockList_full = ServiceController.getBlockAppList(mContext);
		if (mBlockList_full == null) {
			return sAppListItems;
		}
		if (packageManager == null)
		{
			packageManager = mContext.getPackageManager();
		}

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent,
				PackageManager.GET_INTENT_FILTERS);
		Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));

		// draw default 3 shell
		/*
		 * Message msg2 = new Message(); msg2.what = 2;
		 * mHandlerReadImg.sendMessage(msg2);
		 */
		// AppPermissionControl.skipThisApp(packageName,
		// AppType.App, mGameList, mBlockList);
		for (int j = 0; j < appList.size(); j++)
		{
			String packageName = appList.get(j).activityInfo.packageName;
			// ArrayList<String> mBlockList1 =mBlockList;
			if (packageName != null && !skipThisApps(packageName, AppType.App, mBlockList_full))
			{
				String content = appList.get(j).activityInfo.packageName + "/"
						+ appList.get(j).activityInfo.name;
				String filename = (String) appList.get(j).loadLabel(packageManager);
				// Bitmap bmap = getAppIconBitmap(frameImage,
				// appList.get(i),packageName);
				OsListViewItem item = new OsListViewItem();
				// item.setImage(bmap);

				item.setName(filename);
				item.setPath(content);

				sAppListItems.add(item);
			}
		}
		// add the listener when the apk list loaded;
		// add by aaronli at Mar20
		if (mOnApkListChangeListener != null)
		{
			mOnApkListChangeListener.onApkListChanged(sAppListItems);
		}
		return sAppListItems;
	}

	 /**
	  * get app icon when the pervent icon 's width or height less than 150px
	  * @param packageName package name
	  * @param frameImage frame bitmap
	  * @param bgImage
	  * @return
	  */
	public Bitmap getAppIconBitmap(String packageName, Bitmap frameImage, Bitmap bgImage) {
		String smallImgPath = PATH_SMALL_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
		
		Bitmap bmap = null;
		if (new File(smallImgPath).exists()) {
			
			bmap = BitmapFactory.decodeFile(smallImgPath);
		}

		if (bmap == null)
		{
			String iconpath = PATH_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
			//bmap = MediaManager.loadProperImage(iconpath, 150, 150);
			if (new File(iconpath).exists()) {
				
				bmap = BitmapFactory.decodeFile(iconpath);
			}

			if (bmap == null)
			{
				if (packageManager == null)
				{
					packageManager = mContext.getPackageManager();
				}
				try
				{
					
					bmap = drawableToBitmap(packageManager.getApplicationIcon(packageName));
				} catch (NameNotFoundException e)
				{
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				
			/*	 * if (newIcon == null || newIcon.getWidth() < 300) { bmap =
				 * MediaManager.getIconForGooglePlayItem(newIcon, frameImage,
				 * null); } else { bmap = newIcon; }*/
				 
				MediaManager.saveImageToExternal(bmap, smallImgPath);
			}
		}
		
		if (bmap != null) {
			bmap = getAppMirrorImg(bmap, frameImage, bgImage);
			MediaManager.saveImageToExternal(bmap, PATH_LARGE_ICON_DIR + packageName + Global.FILE_TYPE_PNG);
		}
		return bmap;
	}

	/**
	 * Get Bitmap for snack view
	 * 
	 * @param button
	 * @param frameImage
	 * @param mBmpBg
	 * @param mBmpTop
	 */
	public void loadingAppBitmap(OSButton button, Bitmap frameImage, Bitmap mBmpBg, Bitmap mBmpTop) {
		try
		{
			final String packageName = getPackageName(button.getContentPath().get(0));
			Bitmap mirrorImg = null;
			final String filePath = PATH_LARGE_MIRROR_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
			if (new File(filePath).exists())
			{
				mirrorImg = BitmapFactory.decodeFile(filePath);
			}

			/*File largeIcon = null;

			if (mirrorImg == null)
			{
				final String largeIconPath = PATH_LARGE_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
				Bitmap largeImage = null;
				largeIcon = new File(largeIconPath);
				if (largeIcon.exists())
				{
					// get the mirror image from large icon
					Options op = new Options();
					op.inPreferredConfig = Config.ARGB_8888;
					largeImage = BitmapFactory.decodeFile(largeIconPath, op);
				}
				
				 * if (largeImage == null){ String iconPath = PATH_ICON_DIR +
				 * packageName + Global.FILE_TYPE_PNG; if (new
				 * File(iconPath).exists()) { largeImage =
				 * BitmapFactory.decodeFile(iconPath); } } if (largeImage ==
				 * null){ String smallPath = PATH_SMALL_ICON_DIR + packageName +
				 * Global.FILE_TYPE_PNG; if (new File(smallPath).exists()) {
				 * largeImage = BitmapFactory.decodeFile(smallPath); } }
				 
				if (largeImage == null)
				{
					largeImage = getAppIconBitmap(packageName);
				}

				if (largeImage != null)
				{
					// largeImage = getGameLargeIcon(largeImage, mBmpBg,
					// mBmpTop);
					MediaManager.saveImageToExternal(largeImage, largeIconPath);
					mirrorImg = getAppMirrorImg(largeImage, frameImage);
					// create app icon
					
					 * String smallIconFile = PATH_SMALL_ICON_DIR + packageName
					 * + Global.FILE_TYPE_PNG; if (!new
					 * File(smallIconFile).exists()) { Bitmap smallIcon =
					 * MediaManager.getIconForListView(largeImage);
					 * MediaManager.saveImageToExternal(smallIcon,
					 * smallIconFile); }
					 
				} else
				{
					// get the mirror image from app
					// Bitmap newIcon = getAppIconBitmap(frameImage,
					// packageName);
					return;
					
					 * if (newIcon.getWidth() < 300) { newIcon =
					 * MediaManager.getIconForGooglePlayItem(newIcon,
					 * frameImage, mBmpTop); }
					 

					// mirrorImg = MediaManager.getReflectionImage(newIcon);
					// newIcon.recycle();
					// // get the mirror image from app
					// Bitmap largeImage = ((BitmapDrawable)
					// mAppList.get(i).loadIcon(packageManager)).getBitmap();
					// mirrorImg = MediaManager.getReflectionImage(largeImage);
					// largeImage.recycle();
				}
				// MediaManager.saveImageToExternal(mirrorImg,
				// PATH_LARGE_MIRROR_ICON_DIR + packageName +
				// Global.FILE_TYPE_PNG);
			}*/
			
			if (mirrorImg == null) {
				String smallImgPath = PATH_SMALL_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
				Bitmap bmap = null;
				if (new File(smallImgPath).exists()) {
					bmap = BitmapFactory.decodeFile(smallImgPath);
				}

				if (bmap == null)
				{
					String iconpath = PATH_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
					bmap = BitmapFactory.decodeFile(iconpath);

					if (bmap == null)
					{
						if (packageManager == null)
						{
							packageManager = mContext.getPackageManager();
						}
						try
						{
							bmap = drawableToBitmap(packageManager.getApplicationIcon(packageName));
						} catch (NameNotFoundException e)
						{
							e.printStackTrace();
						}

						/*
						 * if (newIcon == null || newIcon.getWidth() < 300) { bmap =
						 * MediaManager.getIconForGooglePlayItem(newIcon, frameImage,
						 * null); } else { bmap = newIcon; }
						 */
						MediaManager.saveImageToExternal(bmap, smallImgPath);
					}
				}
				mirrorImg = getAppMirrorImg(bmap,frameImage, mBmpBg);
				MediaManager.saveImageToExternal(mirrorImg, PATH_LARGE_ICON_DIR + packageName + Global.FILE_TYPE_PNG);
			}

			if (mirrorImg != null)
			{
				MediaManager.saveImageToExternal(mirrorImg, filePath);
				button.setTextureBmp(mirrorImg);

				// load dim snack image
				Bitmap dimImage = null;
				String largeDimImagePath = PATH_LARGE_DIM_ICON_DIR + packageName + Global.FILE_TYPE_PNG;
				File largeDimImgFile = new File(largeDimImagePath);
				if (!largeDimImgFile.exists())
				{
					// Log.w("app", "large dim not exist");
					dimImage = MediaManager.getDimImage2(mirrorImg);
					MediaManager.saveImageToExternal(dimImage, largeDimImagePath);
				} else
				{
					// Log.i("app", "large dim exist");
					// delete the old icon
					dimImage = BitmapFactory.decodeFile(largeDimImagePath);
					if (dimImage == null)
					{
						dimImage = MediaManager.getDimImage2(mirrorImg);
						MediaManager.saveImageToExternal(dimImage, largeDimImagePath);
					}
				}
				button.setDimTextureBmp(dimImage);
			}

		} catch (Exception ex)
		{
			Log.w("FunctionMenuAnimationCtrl", "initFunctionMenu->cannot load icon bitmap ", ex);
		}
	}

	/*private Bitmap getAppMirrorImg(Bitmap largeImage, Bitmap frameImage) {
		Bitmap flag = frameImage.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(flag);
		int frameWidth = frameImage.getWidth();
		int frameHeight = frameImage.getHeight();
		// Log.d(KEY_APPS_CATEGORY, "frameImage " + frameWidth + " " +
		// frameHeight);
		int largeWidth = largeImage.getWidth();
		int largeHeight = largeImage.getHeight();
		// Log.d(KEY_APPS_CATEGORY, "largeImage " + largeWidth + " " +
		// largeHeight);
		int paddingLeft = 38;
		int paddingTop = 11;
		int paddingRight = 44;
		int paddingBottom = 66;
		int left, top, right, bottom;

		if (largeWidth > frameWidth - paddingLeft - paddingRight)
		{
			left = paddingLeft;
			right = frameWidth - paddingRight;
		} else
		{
			left = (paddingLeft + frameWidth - paddingRight - largeWidth) / 2;
			right = (paddingLeft + frameWidth - paddingRight + largeWidth) / 2;
		}
		if (largeHeight > frameHeight - paddingTop - paddingBottom)
		{
			top = paddingTop;
			bottom = frameHeight - paddingBottom;
		} else
		{
			top = (paddingTop + frameHeight - paddingBottom - largeHeight) / 2;
			bottom = (paddingTop + frameHeight - paddingBottom + largeHeight) / 2;
		}
		Rect dst = new Rect(left, top, right, bottom);
		// Log.d(KEY_APPS_CATEGORY, "dst " +dst);
		canvas.drawBitmap(largeImage, null, dst, null);
		return flag;
	}
*/
	/**
	 * 
	 * @param largeImage
	 * @param frameImage
	 * @param bgImage
	 * @return
	 */
	private Bitmap getAppMirrorImg(Bitmap largeImage, Bitmap frameImage, Bitmap bgImage){
		
		// modified by aaronli Jun17 2013. showing the frame icon when the icon size is too small.
		//Log.v(bitmap2.getWidth(), "bitmap2.getWidth()="+bitmap2.getWidth());
		if (largeImage.getWidth()<150 || largeImage.getHeight()<150)
		{
			return threeoverlying(largeImage, frameImage, bgImage);
		} else
		{
			return roundcorner(largeImage, bgImage);
   		}
	}
	
	 private Bitmap threeoverlying(Bitmap largeImage, Bitmap frameImage, Bitmap bgImage) {

		  // 防止出现Immutable bitmap passed to Canvas constructor错误
		  Bitmap bitmap1 = bgImage.copy(Bitmap.Config.ARGB_8888, true);
		  int w = 164;
		  int h = 165;
		  Canvas canvas = new Canvas(bitmap1);
		  Bitmap newbitmap = MeepStorageCtrl.resizeImage(frameImage, w, h);
		  //float roundPx = 25;
		  //Bitmap insideafter = Photo.getRoundedCornerBitmap(newbitmap,roundPx);
		  canvas.drawBitmap(newbitmap, 6, 3, null);
		  int insideW = 61;
		  int insideH = 61;  
		  float roundPx = 4;
		  
		  newbitmap = MeepStorageCtrl.resizeImage(largeImage, insideW, insideH);
		  newbitmap = MeepStorageCtrl.getRoundedCornerBitmap(newbitmap,roundPx);  
		  canvas.drawBitmap(newbitmap, 54, 27, null);

		  canvas.save(Canvas.ALL_SAVE_FLAG); 
		  // 存储新合成的图片
		  canvas.restore();

		  return bitmap1;
	}
	 
	
	
	private Bitmap roundcorner(Bitmap largeImage, Bitmap bgImage) {
		// 防止出现Immutable bitmap passed to Canvas constructor错误
			Bitmap bitmap1 = bgImage.copy(Bitmap.Config.ARGB_8888, true);
			
			int w = 161;
			int h = 164;
			
			Bitmap newbitmap = MeepStorageCtrl.resizeImage(largeImage, w, h);
			// modified by aaronli at Jun27 2013. make three rounded corners and one right angle corner bitmap. fixed #4581
			Bitmap cornerafter = MeepStorageCtrl.getRoundedCornerBitmap(newbitmap,25,w,h,true,false,false,false);;
			Canvas canvas = new Canvas(bitmap1);
			canvas.drawBitmap(cornerafter, 8, 5, null);

			return bitmap1;
			
			
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public void deleteAppItem() {

	}

	/**
	 * Clear the caches.But has never used before
	 * */
	public void clearAppItems() {
		if (sAppListItems != null)
		{
			sAppListItems.clear();
		}
	}

	/**
	 * find the OsListViewItem from the caches
	 * @deprecated because may make mistake
	 * @param name
	 *            {@link OsListViewItem}
	 * @return the item of cache with the name
	 */
	public OsListViewItem findListViewItemByName(String name) {
		if (sAppListItems == null)
		{
			return null;
		}
		for (int i = 0; i < sAppListItems.size(); i++)
		{
			if (sAppListItems.get(i).getName().equals(name))
			{
				return sAppListItems.get(i);
			}
		}
		return null;
	}

	public void setAppFiltering(String[] appsCategory) {
		mGameList = AppPermissionControl.getGameList(appsCategory);
		mBlockList = AppPermissionControl.getBlockList(appsCategory);
		try
		{
			SharedPreferences sp = mContext.getSharedPreferences("apps", Context.MODE_PRIVATE);
			sp.edit().putString(KEY_APPS_CATEGORY, Arrays.toString(appsCategory)).commit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getPackageName(String appPath) {
		final int index = appPath.indexOf('/');
		if (index >= 0)
		{
			return appPath.substring(0, index);
		}
		return null;
	}

	private boolean getGameAndBlockApps() {
		SharedPreferences sp = mContext.getSharedPreferences("apps", Context.MODE_PRIVATE);
		final String s = sp.getString(KEY_APPS_CATEGORY, null);
		if (s != null)
		{
			String[] appsCategory = s.substring(1, s.length() - 1).split(", ");
			if (appsCategory != null)
			{
				mGameList = AppPermissionControl.getGameList(appsCategory);
				mBlockList = AppPermissionControl.getBlockList(appsCategory);
				if (mGameList.size() > 0 && mBlockList.size() > 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return the mOnApkListChangeListener
	 */
	public OnApkListChangeListener getmOnApkListChangeListener() {
		return mOnApkListChangeListener;
	}

	/**
	 * @param mOnApkListChangeListener
	 *            the mOnApkListChangeListener to set
	 */
	public void setmOnApkListChangeListener(OnApkListChangeListener mOnApkListChangeListener) {
		this.mOnApkListChangeListener = mOnApkListChangeListener;
	}

	private static Bitmap getGameLargeIcon(Bitmap bitmap, Bitmap iconBg, Bitmap iconTop) {
		Bitmap cropBitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);

		Bitmap mutable = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutable);
		Paint paint = new Paint();
		paint.setFilterBitmap(false);
		canvas.drawBitmap(cropBitmap, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		canvas.drawBitmap(iconBg, 0, 0, paint);
		paint.setXfermode(null);
		canvas.drawBitmap(iconTop, 0, 0, paint);

		return mutable;
	}

	/**
	 * the listener when the apk list changed,it need to refresh the showing
	 * 
	 * @author aaronli
	 * @Date Mar20 2013
	 */
	public interface OnApkListChangeListener {
		/**
		 * to do when apk list was changed
		 * 
		 * @param apks
		 *            : the refreshed apk list
		 * @Date Mar20 2013
		 */
		public void onApkListChanged(List<OsListViewItem> apks);

		/**
		 * to do when apk list was loaded fail,and not return any apks
		 * 
		 * @Date Mar20 2013
		 */
		public void onApkLoadFail();
		
		
		public void onGetServiceAccount(String user) ;
	}
	
	 

}
