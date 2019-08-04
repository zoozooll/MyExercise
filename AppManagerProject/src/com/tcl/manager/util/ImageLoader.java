package com.tcl.manager.util;

import android.content.Context;
import android.graphics.Bitmap;
 
import com.tcl.live.bitmap.BitmapUtils;
import com.tcl.live.bitmap.config.BitmapDisplayConfig;
import com.tcl.manager.application.AMDirType;
import com.tcl.manager.application.AMDirectorManager;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.mie.manager.R;

public class ImageLoader
{
	private static ImageLoader imageLoader = new ImageLoader();
	public BitmapDisplayConfig iconConfig;
	public BitmapDisplayConfig bigBannerConfig;
	public BitmapDisplayConfig smallBannerConfig;
	public BitmapDisplayConfig infoConfig;
	public BitmapDisplayConfig userConfig;
	public BitmapUtils bitmapUtils = null;
	private Context context;

	public ImageLoader()
	{

		this.context = ManagerApplication.sApplication.getApplicationContext();

		if (bitmapUtils == null)
		{
			bitmapUtils = new BitmapUtils(context, AMDirectorManager.getInstance().getDirectoryPath(AMDirType.image));
		}

		initBitmapUtils();
	}

	public static ImageLoader getInstance()
	{
		return imageLoader;
	}

	private void initBitmapUtils()
	{
		if (iconConfig == null)
		{
			iconConfig = new BitmapDisplayConfig();
			 iconConfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_default));
			 iconConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_default));
			iconConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		}

		if (bigBannerConfig == null)
		{
			bigBannerConfig = new BitmapDisplayConfig();
			// bigBannerConfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.banner_big_default));
			// bigBannerConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.banner_big_default));
			bigBannerConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		}
		if (smallBannerConfig == null)
		{
			smallBannerConfig = new BitmapDisplayConfig();
			// smallBannerConfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.banner_small_default));
			// smallBannerConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.banner_small_default));
			smallBannerConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		}

		if (infoConfig == null)
		{
			infoConfig = new BitmapDisplayConfig();
			// infoConfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.screenshot_default));
			// infoConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.screenshot_default));
			infoConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		}

		if (userConfig == null)
		{
			userConfig = new BitmapDisplayConfig();
			// userConfig.setLoadingDrawable(context.getResources().getDrawable(R.drawable.ic_user_small));
			// userConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.drawable.ic_user_small));
			userConfig.setBitmapConfig(Bitmap.Config.RGB_565);
		}
	}
}
