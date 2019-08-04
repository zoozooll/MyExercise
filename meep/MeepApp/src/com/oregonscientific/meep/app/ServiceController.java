/**
 * Controll the service. than when to connect to service,and when get Account Message.
 */
package com.oregonscientific.meep.app;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.global.AppPermissionControl;
import com.oregonscientific.meep.permission.Category;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.PermissionManager;

/**
 * @author aaronli
 *
 */
public class ServiceController {

	private static String user;
	private static List<String> mBlockList_full;
	
	
	public static String getUser() {
		return user;
	}

	public static boolean isContainBadWord(Context context, String word) {
		if (user != null)
		{
			PermissionManager pm = (PermissionManager) ServiceManager.getService(context,
					com.oregonscientific.meep.ServiceManager.PERMISSION_SERVICE);

			if (pm.containsBadword(user, word) == true)
			{
				return true;
			}
		}
		return false;
	}
	
	public static void getAccount(final Context context) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable()
		{
			@Override
			public void run() {
				//Log.e("run", "run method  executed");
				AccountManager am = (AccountManager) ServiceManager
						.getService(context, ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLoggedInAccountBlocking();
				// Account account = am.getLoggedInAccount();

				if (account != null)
				{
					//Log.e("account", "account != null");
					Identity identiry = account.getIdentity();
					user = identiry.getName();
				}
			}
		});

	}
	
	public static List<String> getBlockAppList(Context context) {
		/*if (user == null) {
			return null;
		}
		PermissionManager pm = (PermissionManager) ServiceManager.getService(context,
				ServiceManager.PERMISSION_SERVICE);

		List<Component> components_black = pm.getComponentsBlocking(user, Category.CATEGORY_BLACKLIST);
		for (int i = 0; i < components_black.size(); i++)
		{
			mBlockList_full.add(components_black.get(i).getName());
		}
		return mBlockList_full;*/
		
		return mBlockList_full = AppPermissionControl.getDefaultBlockList();
	}
}
