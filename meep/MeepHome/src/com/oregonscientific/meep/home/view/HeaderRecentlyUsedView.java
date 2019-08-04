/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.oregonscientific.meep.app.DialogMessage;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.widget.OutlinedTextView;

/**
 * This is a View of MenuHeaderItem content about recently used application
 */
public class HeaderRecentlyUsedView extends LinearLayout {
	
	private static final String TAG = "HeaderRecentlyUsedView";
	
	private ImageView imageView;
	private OutlinedTextView mApplicationLabel;
	
	private Handler handler;
	private HashMap<String, String> meepAppIconList;
	
	private final static int MAX_WIDTH = 90;
	private final static int MAX_HEIGHT = 90;
	
	public HeaderRecentlyUsedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		View v = View.inflate(context, R.layout.recently_used, this);
		imageView = (ImageView) v.findViewById(R.id.menu_item_header_recently_used_image);
		mApplicationLabel = (OutlinedTextView) v.findViewById(R.id.menu_item_header_recently_used);
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		
		meepAppIconList = new HashMap<String, String>();
		meepAppIconList = parseStringArray(R.array.meep_app_icon);
		handler = new Handler();
		// disable update recently used for now, may be enable later
		//updateRecentlyUsed();
	}

	/**
	 * Updates the recently used application info at home menu
	 */
	public void updateRecentlyUsed() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
				Account account = am.getLastLoggedInAccountBlocking();
				
				// If there was no last logged in user, that means no user has ever logged into the MEEP. Use the default in this case
				if (account == null) {
					account = am.getDefaultAccountBlocking();
				}
				
				final PermissionManager permissionManager = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
				final Component component = permissionManager.getMostRecentAppBlocking(account.getId());
				
				// Cannot continue if the component is not found
				if (component == null) {
					return;
				}
				
				try {
					PackageManager packageManager = getContext().getPackageManager();
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(component.getName(), 0);
					
					// Do not continue if information on the application cannot be retrieved
					if (applicationInfo == null) {
						return;
					}
					
					setLabel((String) applicationInfo.loadLabel(packageManager));
					setDrawable(getApplicationIcon(component, packageManager));
					final String userId = account == null ? null : account.getId();
					
					setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String packageName = component.getName();
							// Cannot continue if the package is not found
							if (packageName == null) {
								return;
							}
							
							Intent launchIntent = packageName == null ? null : getContext().getPackageManager().getLaunchIntentForPackage(packageName);
							int isAccessible = launchIntent == null ? PermissionManager.FLAG_PERMISSION_DENIED : permissionManager.isAccessible(userId, launchIntent.getComponent());
							
							// Cannot continue if is not accessible
							if (isAccessible != PermissionManager.FLAG_PERMISSION_OK) {
								int resId = isAccessible == PermissionManager.FLAG_PERMISSION_TIMEOUT ? R.string.title_timeout : R.string.title_oops;
								DialogFragment dialog = DialogFragment.newInstance(
										getContext().getString(resId), 
										DialogMessage.getPackagePermissionViolatedMessage(getContext(), packageName, isAccessible));
								dialog.setPositiveButton(
										getContext().getString(R.string.alert_button_ok), 
										new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(DialogFragment dialog, int which) {
												dialog.dismiss();
											}
											
										});
								return;
							}
							
							getContext().startActivity(launchIntent);
						}
						
					});
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Failed to update view because " + e);
				}
			}			
		});
	}
	
	/**
	 * Sets the text to be displayed in this view
	 * 
	 * @param label the textual context to be displayed
	 */
	private void setLabel(final String label) {
		if (label != null && mApplicationLabel != null) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					mApplicationLabel.setText(label);
				}
						
			});
		}
	}
	
	/**
	 * Parses the given string resource into a {@link Map}
	 * 
	 * @param resourceId the identifier of the string resource
	 * @return a {@link Map} resulted from parsing the given string resource
	 */
	private HashMap<String, String> parseStringArray(int resourceId) {
	    String[] stringArray = getResources().getStringArray(resourceId);
	    HashMap<String, String> outputArray = new HashMap<String,String>();
	    for (String entry : stringArray) {
	        String[] splitResult = entry.split("\\|", 2);
	        outputArray.put(splitResult[0], splitResult[1]);
	    }
	    return outputArray;
	}
	
	/**
	 * Returns icon of associated with the given package  
	 * 
	 * @param component the component to return the icon
	 * @param packageManager The object that returns information on application packages
	 * @return the {@link Drawable} of the icon associated with the {@code component}
	 */
	private Drawable getApplicationIcon(final Component component, PackageManager packageManager) {
		Drawable result = null;

		try {
			// Retrieves the icon associated with the given {@link Component}
			
			//get the application icon from the MEEP icon list  
			result = getApplicationIcon(component.getName());
			// if the application icon is not contain in the MEEP icon list then will get the individual application icon
			if (result == null) {
				result = getContext().getPackageManager().getApplicationIcon(component.getName());
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}			
		
		return result;
	}
	
	/**
	 * Returns icon of associated with the given package  
	 * 
	 * @param packageName the name of the package to return the icon
	 * @return a drawable representing the icon
	 */
	private Drawable getApplicationIcon(String packageName) {
		if (!meepAppIconList.containsKey(packageName)) {
			return null;
		}
		int drawableId = getContext().getResources().getIdentifier(meepAppIconList.get(packageName), "drawable", getContext().getPackageName());
		if (drawableId == 0) {
			return null;
		}
		
		return getContext().getResources().getDrawable(drawableId);
	}
	
	
	/**
	 * Sets the image to be displayed in the icon area of this view
	 * 
	 * @param icon the drawable to be displayed
	 */
	private void setDrawable(Drawable icon) {				
		// set default app icon if the appIcon is not found or null
		if (icon == null) {
			Bitmap b = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.right_recently_used_arrow, MAX_WIDTH, MAX_HEIGHT);
			icon = new BitmapDrawable(getResources(), b);
		}
		
		final Drawable image = icon;
		handler.post(new Runnable() {

			@Override
			public void run() {
				imageView.setImageDrawable(image);
			}
						
		});
	}

}
