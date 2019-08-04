package com.oregonscientific.meep.notification.view;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.oregonscientific.meep.app.DialogMessage;
import com.oregonscientific.meep.notification.Notification;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.notification.R;
import com.oregonscientific.meep.notification.view.ExpandableTextView.OnExpandAndCollapseListener;
import com.oregonscientific.meep.permission.PermissionManager;

/**
 * An {@link android.widget.Adapter} that contains a list of {@link com.oregonscientific.meep.notification.Notification}
 */
public class MessageAdapter extends ArrayAdapter<Notification> {
	
	private final String TAG = getClass().getSimpleName();
	
	private final int PADDING_LEFT_ITEM_EVEN = 30;
	private final int PADDING_LEFT_ITEM_ODD = 120;
	
	private final float ALPHA_READ = 0.7f;
	private final float ALPHA_UNREAD = 1.f;
	
	private HashMap<Notification, Boolean> mExpandedMap;
	
	/**
	 * Used for invoking the {@link android.content.Intent} in the {@link Notification}
	 */
	private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
	private Future<?> mFuture;
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	
	private static final String VIEW_TAG_MESSAGE_BOX_ITEM = "MessageBoxItem";
	
	public MessageAdapter(Context context, List<Notification> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, getLayoutId(position), convertView);
	}
	
	/**
	 * Returns the position of the item with the given {@code id}
	 * 
	 * @param id the id of the item to retrieve the position of
	 * @return The position of the item with the given {@code id}, -1 if no object matched the {@code id}
	 */
	public int getPosition(long id) {
		int result = -1;
		for (int i = 0; i < getCount(); i++) {
			Notification n = getItem(i);
			if (n.id == id) {
				result = i;
				break;
			}
		}
		return result;
	}
	
	@Override
	public int getCount() {
		return super.getCount();
	}
	
	protected int getLayoutId(int position) {
		return R.layout.message_list_item;
	}
	
	protected MessageBoxItem getMessageBoxItem() {
		return new MessageBoxItem(getContext());
	}
	
	@Override
	public int getViewTypeCount() {    
		// We only have 1 type of view for {@link MessageAdapter}
		return 1;
	}

	@Override
	public int getItemViewType(int position) {
		// We ignore the view type
	    return IGNORE_ITEM_VIEW_TYPE;
	}
	
	protected Drawable getMessageBoxTopDrawable(int position) {
		Notification n = getItem(position);
		if (n != null) {
			if (Notification.KIND_ALERT.equals(n.kind)) {
				
			} else if (Notification.KIND_MESSAGE.equals(n.kind)) {
				
			} else if (Notification.KIND_STORE.equals(n.kind)) {
				return getContext().getResources().getDrawable(R.drawable.meep_store);
			} else if (Notification.KIND_NEWS.equals(n.kind)) {
				if (position % 2 == 0) {
					return getContext().getResources().getDrawable(R.drawable.n_bg_up2);
				}
			}
		}
		
		return null;
	}
	
	protected View getView(int position, int layoutId, View convertView) {
		MessageBox messageBox = null;
		MessageBoxItem messageBoxItem = null;
		
		if (convertView == null) {
			convertView = View.inflate(getContext(), layoutId, null);
			messageBoxItem = getMessageBoxItem();
			messageBoxItem.setTag(VIEW_TAG_MESSAGE_BOX_ITEM);
		} else {
			messageBoxItem = (MessageBoxItem) convertView.findViewWithTag(VIEW_TAG_MESSAGE_BOX_ITEM);
		}
		
		final Notification n = getItem(position);
		messageBox = (MessageBox) convertView.findViewById(R.id.message_list_item_message_box);
		messageBox.setContent(messageBoxItem);
		
		if (messageBox != null && n != null && messageBoxItem != null) {
			messageBox.setListener(new OnExpandAndCollapseListener() {
				
				@Override
				public void onSettingText(boolean expandable) {
				}
				
				@Override
				public void onExpanded() {
					updateExpandedState(n, true);
				}
				
				@Override
				public void onCollapsed() {
					updateExpandedState(n, false);

				}
			});
			
			// Sets background of the notification according to the type of the notification
			Drawable drawable = getMessageBoxTopDrawable(position);
			if (drawable != null) {
				messageBox.setTopBackground(drawable);
			}
			
			messageBox.setAlpha(hasRead(position) ? ALPHA_READ : ALPHA_UNREAD);
			messageBox.setTime(n.when);
			
			final int index = position;
			messageBoxItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (index < getCount()) {
						final Notification n = (Notification) getItem(index);
						if (n != null) {
							startIntent(n.contentIntent, n.contentAction, new Runnable() {

								@Override
								public void run() {
									n.flags |= Notification.FLAG_READ;
									NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
									notificationManager.markAsRead(n.id, true);

									mHandler.post(new Runnable() {

										@Override
										public void run() {
											notifyDataSetChanged();
										}

									});
								}

							});
						}

					}
				}
			});
			
			messageBoxItem.setIntentButtonsEnabled(true);
			messageBoxItem.setTitle(n.title);
			messageBoxItem.setIcon(n.icon);
			messageBoxItem.setMessage(n.text);
			messageBoxItem.setImage(n.picture);
			messageBoxItem.setTextColor(n.kind == Notification.KIND_NEWS ? Color.BLACK : getContext().getResources().getColor(R.color.brown));
			
			if (n.negativeIntent != null) {
				final MessageBoxItem self = messageBoxItem;
				messageBoxItem.setNegativeButton(new OnClickListener(){

					@Override
					public void onClick(View v) {
						self.setIntentButtonsEnabled(false);
						startIntent(n.negativeIntent, n.negativeAction, new Runnable() {

							@Override
							public void run() {
								removeNotificationItem(n);
							}
							
						});
					}	
				});
			}
			
			if (n.positiveIntent != null) {
				final MessageBoxItem self = messageBoxItem;
				messageBoxItem.setPositiveButton(new OnClickListener(){

					@Override
					public void onClick(View v) {
						self.setIntentButtonsEnabled(false);
						startIntent(n.positiveIntent, n.positiveAction, new Runnable() {

							@Override
							public void run() {
								removeNotificationItem(n);
							}
							
						});
					}		
				});
			}
			
			if (n.style == Notification.STYLE_PROGRESS) {
				if (n.progressIndeterminate) {
					messageBoxItem.setProgressIndeterminate(n.progressIndeterminate);
				} else {
					messageBoxItem.setProgress(n.progressMax, n.progress);
				}
			}
			
			if (mExpandedMap != null) {
				Boolean bool = mExpandedMap.get(n);
				messageBox.expand(bool == null ? false : bool.booleanValue());
			}
		}
		
		// Make the notification staggered
		if (position % 2 == 1) {
			convertView.setPadding(PADDING_LEFT_ITEM_ODD, 0, 0, 0);
		} else {
			convertView.setPadding(PADDING_LEFT_ITEM_EVEN, 0, 0, 0);
		}
		
		ImageView hasReadBubble = (ImageView) convertView.findViewById(R.id.message_list_item_bubble);
		if (hasReadBubble != null) {
			hasReadBubble.setVisibility(hasRead(position) ? View.INVISIBLE : View.VISIBLE);
		}
		
		return convertView;
	}
	
	/**
	 * Returns whether or not the item in the give {@code position} is marked as "read"
	 */
	protected boolean hasRead(int position) {
		Notification notification = getItem(position);
		return notification == null ? false : (notification.flags & Notification.FLAG_READ) == Notification.FLAG_READ;
	}
	
	protected void startIntent(final Intent intent, final String action, final Runnable success) {
		// Ignore the request if the previous one has not yet completed
		// execution
		if (mFuture != null && !mFuture.isDone()) {
			return;
		}
		
		mFuture = mExecutor.submit(new Runnable() {

			@Override
			public void run() {
				try {
					// Tries to enforce permission on the package to be launched
					ComponentName component = intent.getComponent();
					if (component == null) {
						String packageName = intent.getPackage();
						if (packageName != null) {
							PackageManager packageManager = getContext().getPackageManager();
							Intent i = packageManager.getLaunchIntentForPackage(packageName);
							component = i.getComponent();
						}
					}
					
					Log.d(TAG, component + " is about to be launched...");
					if (component != null) {
						AccountManager am = (AccountManager) ServiceManager.getService(getContext(), ServiceManager.ACCOUNT_SERVICE);
						Account account = am.getLastLoggedInAccountBlocking();
						if (account == null) {
							account = am.getDefaultAccount();
						}
						
						// Ask PermissionManager whether or not the given
						// discrete component is accessible using {@code account}
						PermissionManager pm = (PermissionManager) ServiceManager.getService(getContext(), ServiceManager.PERMISSION_SERVICE);
						int reason = pm.isAccessibleBlocking(account.getId(), component);
						Log.d(TAG, account + " is " + reason + " to access " + component);
						if (reason != PermissionManager.FLAG_PERMISSION_OK) {
							int resId = reason == PermissionManager.FLAG_PERMISSION_TIMEOUT ? R.string.title_timeout : R.string.title_oops;
							final DialogFragment dialog = DialogFragment.newInstance(
									getContext().getString(resId), 
									DialogMessage.getPackagePermissionViolatedMessage(getContext(), component.getPackageName(), reason));
							
							dialog.setPositiveButton(
									getContext().getString(R.string.alert_button_ok), 
									new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogFragment dialog, int which) {
											dialog.dismiss();
										}
									});
							
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									Activity activity = (Activity) getContext();
									dialog.show(activity.getFragmentManager());
								}
								
							});
							
							return;
						}
					}
					
					if (Notification.ACTION_SEND_BROADCAST.equals(action)) {
						getContext().sendBroadcast(intent);
					} else if (Notification.ACTION_START_ACTIVITY.equals(action)) {
						getContext().startActivity(intent);
					} else if (Notification.ACTION_START_SERVICE.equals(action)) {
						getContext().startService(intent);
					}
					
					// Runs the success block after the intent was started
					if (success != null) {
						success.run();
					}
				} catch (Exception ex) {
					Log.e(TAG, "Cannot start " + intent + " because " + ex);
				}
			}
			
		});
		
	}
	
	@Override
	public void remove(Notification n) {
		super.remove(n);
		
		if (mExpandedMap != null) {
			mExpandedMap.remove(n);
		}
	}
	
	/**
	 * Remove the notification item after user click the positive or negative button
	 * @param notification
	 */
	private void removeNotificationItem(Notification notification) {
		//Can not continue with pass in object is null
		if (notification == null) {
			return;
		} 
		
		NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(getContext(), ServiceManager.NOTIFICATION_SERVICE);
		notificationManager.cancel(notification.id);
	}
	
	private void updateExpandedState(Notification n, boolean expanded) {
		if (mExpandedMap == null) {
			mExpandedMap = new HashMap<Notification, Boolean> ();
		}
		mExpandedMap.put(n, Boolean.valueOf(expanded));
	}
	
}
