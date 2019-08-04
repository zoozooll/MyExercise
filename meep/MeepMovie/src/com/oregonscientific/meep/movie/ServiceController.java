/**
 * Controll the service. than when to connect to service,and when get Account Message.
 */
package com.oregonscientific.meep.movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.permission.PermissionManager;

/**
 * @author aaronli
 *
 */
public class ServiceController {

	private static String user;
	private static PermissionManager spm;

	
/*	public static String getUser() {
		return user;
	}*/

	public static boolean isContainBadWord(Context context, String word) {
		if (user != null&&spm!=null)
		{
		/*	PermissionManager pm = (PermissionManager) ServiceManager.getService(context,
					com.oregonscientific.meep.ServiceManager.PERMISSION_SERVICE);*/

			if (spm.containsBadwordBlocking(user, word) == true)
			{
				return true;
			}
			else {
				return false;

			}
	
		}
		return true;
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

				System.out.println("getLoggedInAccountBlocking " + account);

				String username = null;

				// TODO: perform additional actions after retrieving the
				// currently logged in {@link Account}

				if (account != null)
				{
					Log.e("account", "account != null");
					user = account.getId();
					username = account.getMeepTag();

					//Identity identiry = account.getIdentity();
					//username = identiry.getName();
					Log.e("account", "user " + user);
					System.out.println(username);
				}
				spm = (PermissionManager) ServiceManager.getService(context,
						com.oregonscientific.meep.ServiceManager.PERMISSION_SERVICE);

				/*PermissionManager pm = (PermissionManager) ServiceManager.getService(mContext,
						ServiceManager.PERMISSION_SERVICE);
				List<Component> blockedComponents = pm.getComponentsBlocking(username,
						com.oregonscientific.meep.permission.Category.CATEGORY_BLACKLIST);
				List<Component> games = pm.getComponents(username,
						com.oregonscientific.meep.permission.Category.CATEGORY_GAMES);*/
			}
		});

	}
	public static String getUser() {
		return user;
}
	}
