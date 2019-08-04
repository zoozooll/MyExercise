package com.mogoo.ping.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.network.NetworkWorking;
import com.mogoo.ping.vo.UsedActivityItem;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.Log;

public class UsedDataManager {
	
	private static final String TAG = "UsedDataManager"	;
	private static List<UsedActivityItem> sUsedApks;
	private static final int MAX_TASK_NUM = 28;
	
    public static List<UsedActivityItem> getTaskList(Context context) {  
        final PackageManager pm = context.getPackageManager();
        final ActivityManager am = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RecentTaskInfo> recentTasks =
                am.getRecentTasks(MAX_TASK_NUM, 2);
        ActivityInfo homeInfo = 
            new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
                    .resolveActivityInfo(pm, 0);

        // Performance note:  Our android performance guide says to prefer Iterator when
        // using a List class, but because we know that getRecentTasks() always returns
        // an ArrayList<>, we'll use a simple index instead.
        int index = 0;
        int numTasks = recentTasks.size();
        UsedActivityItem item;
        for (int i = 0; i < numTasks && (index < MAX_TASK_NUM); ++i) {
            final ActivityManager.RecentTaskInfo info = recentTasks.get(i);

            Intent intent = new Intent(info.baseIntent);
            if (info.origActivity != null) {
                intent.setComponent(info.origActivity);
            }

            // Skip the current home activity.
            if (homeInfo != null) {
                if (homeInfo.packageName.equals(
                        intent.getComponent().getPackageName())
                        && homeInfo.name.equals(
                                intent.getComponent().getClassName())) {
                    continue;
                }
            }
            try {
            	intent.setFlags((intent.getFlags()&~Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                final ResolveInfo resolveInfo = pm.resolveActivity(intent, 0);
                if (resolveInfo != null) {
                    final ActivityInfo activityInfo = resolveInfo.activityInfo;
                    if (context.getPackageName().equals(activityInfo.packageName)) {
                    	continue;
                    }
                    final float size = Utilities.dpiToPixle(context.getResources(), 70);
                    Drawable icon = ImageTools.getFormatDrawable(resolveInfo.loadIcon(pm), (int)size, (int)size);
                    item = new UsedActivityItem();
                    item.setIcon(icon);
                    item.setName(resolveInfo.loadLabel(pm));
                    item.setPackageName(activityInfo.packageName);
                    item.setClassName(activityInfo.name);
                    item.setLaunchIntent(intent);
                    sUsedApks.add(item);
                }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		saveCurrentUsedApkitems(context);
        return sUsedApks;
    }  
    
    public static void clear() {
    	sUsedApks.clear();
    	sUsedApks = null;
    }
    
	public static List<UsedActivityItem> getFullyUsedApks(Context context) {
		Log.d(TAG, "getFullyUsedApks");
		if (sUsedApks == null){
        	sUsedApks = new LinkedList<UsedActivityItem>();
        } else {
        	sUsedApks.clear();
        }
		synchronized (sUsedApks) {
			getTaskList(context);
			Log.d(TAG, "getFullyUsedApks first list "+sUsedApks.size());
			final PackageManager pm = context.getPackageManager();
			try{
				
				if (sUsedApks != null && sUsedApks.size() < MAX_TASK_NUM) {
					final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			        //List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
			        
					/*//mogoo add xuzhenqin 20121011 begin
					saveCurrentUsedApkitems(context, sUsedApks);
					
					
					//mogoo end
	*/				ApksDao dao = ApksDao.getInstance(context);
					//mogoo add xuzhenqin 20121011 begin
					Cursor cursor = dao.queryUsedApks(
							DataBaseConfig.ApplicationsUsedTable.TABLE_NAME,MAX_TASK_NUM*2);
					//sUsedApks.clear();
					//mogoo end
					UsedActivityItem cursorItem;
					Set<UsedActivityItem> tempSet = new HashSet<UsedActivityItem>(sUsedApks);
					Log.d(TAG, "tempSet "+tempSet.size());
					Log.d(TAG, "cursor "+cursor.getCount());
					for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
							.moveToNext()) {
						String packageName = cursor
								.getString(cursor
										.getColumnIndexOrThrow(DataBaseConfig.ApplicationsUsedTable.COLUMN_PACKAGE_NAME));
						String compandName = cursor
								.getString(cursor
										.getColumnIndexOrThrow(DataBaseConfig.ApplicationsUsedTable.COLUMN_COMPANDNAME));
						Drawable icon = null;
						
						//mogoo add xuzhenqin 20121015 begin
						if (packageName != null && compandName != null) {
							
							Log.d(TAG, "getFullyUsedApks third list "+compandName);
							String name = null;
							cursorItem = new UsedActivityItem();
							cursorItem.setPackageName(packageName);
							cursorItem.setClassName(compandName);
							Intent intent = new Intent();
							ComponentName componentName = new ComponentName(packageName, compandName);
			    			intent.setComponent(componentName);
			    			intent.setAction(Intent.ACTION_MAIN);
			    			intent.addCategory(Intent.CATEGORY_DEFAULT);
			    			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			    			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			                cursorItem.setLaunchIntent(intent);
							try {
								name = context.getPackageManager().getActivityInfo(componentName, PackageManager.GET_META_DATA).loadLabel(pm).toString();
								icon = context.getPackageManager().getActivityIcon(intent);
								cursorItem.setName(name);
								cursorItem.setIcon(icon);
								//mogoo add xuzhenqin 20121011 begin
								if (sUsedApks.size() < MAX_TASK_NUM
										&& tempSet.add(cursorItem)
										&& !context.getPackageName().equals(
												packageName)) {
									Log.d(TAG, "getFullyUsedApks forth list "
											+ compandName);
									sUsedApks.add(cursorItem);
								}
								//mogoo end
								//}
							} catch (NameNotFoundException e) {
								e.printStackTrace();
							}
						}
						//mogoo end
					}
					tempSet.clear();
					tempSet = null;
				}
				Log.d(TAG, "getFullyUsedApks second list "+sUsedApks.size());
			}catch(Exception ex){
				Log.d("aaron","信息错误", ex);
			}
		}
		return sUsedApks;
	}
    
    /*public static void saveCurrentUsedApkitems(Context context, List<UsedActivityItem> data) {
    	ApksDao dao = ApksDao.getInstance(context);
    	//mogoo add xuzhenqin 20121011 begin
    	//dao.clearData(DataBaseConfig.ApplicationsUsedTable.TABLE_NAME);
    	//mogoo end
    	dao.addItemsForUsed(DataBaseConfig.ApplicationsUsedTable.TABLE_NAME, data);
    }*/
    
    public static void saveCurrentUsedApkitems(Context context) {
    	ApksDao dao = ApksDao.getInstance(context);
    	//mogoo add xuzhenqin 20121011 begin
    	//dao.clearData(DataBaseConfig.ApplicationsUsedTable.TABLE_NAME);
    	//mogoo end
    	Log.d(TAG, "saveCurrentUsedApkitems "+sUsedApks.size());
    	dao.addItemsForUsed(DataBaseConfig.ApplicationsUsedTable.TABLE_NAME, sUsedApks);
    }
    
    public static void remoteUninstallPackages(Context context, String packageName) {
    	ApksDao dao = ApksDao.getInstance(context);
    	dao.removeSinglePackage(DataBaseConfig.ApplicationsUsedTable.TABLE_NAME, packageName);
    }
	
}
