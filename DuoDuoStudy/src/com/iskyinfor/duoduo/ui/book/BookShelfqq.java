package com.iskyinfor.duoduo.ui.book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

import com.iskinfor.servicedata.pojo.User;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.talkgarden.TalkGardenActivity;
import com.iskyinfor.duoduo.ui.talkgarden.TalkGardenShowQQ;
import com.iskyinfor.duoduo.ui.talkgarden.UserListAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class BookShelfqq extends Context {

	public PopupWindow mPopupWindow = null;
	public PopupWindow menuWindow;
	private ExpandableListView talkexpandablelistview;
	public Activity mcontext;
	public TextView frinedlist;
	public String actname;
	private String[] list;
	private ArrayList<Map<String, Object>> listchild;
	private Map<String, Object> mDataMap;
	private ArrayList<User> classesList, teacherList, homeList, friendList,
			totalList;
	private ArrayList<User> checkedList = new ArrayList<User>();
	private TextView bookshelfgiftgiftname;
	private String strname;
	private String[] strUserid,reqUserID;
	private String strActivityName;
	ProgressDialog myLoadingDialog;

	public BookShelfqq(Activity context,
			TextView editlist, Map<String, Object> dataMap,
			String stractivityname,ProgressDialog loadingDialog,String[] userID) {
		mcontext = context;
		frinedlist = editlist;
		mDataMap = dataMap;
		classesList = (ArrayList<User>) mDataMap.get("class_array");
		teacherList = (ArrayList<User>) mDataMap.get("teacher_array");
		homeList = (ArrayList<User>) mDataMap.get("home_array");
		friendList = (ArrayList<User>) mDataMap.get("friend_array");
		totalList = new ArrayList<User>();
		totalList.addAll(classesList);
		totalList.addAll(teacherList);
		totalList.addAll(homeList);
		totalList.addAll(friendList);
		strActivityName = stractivityname;
		myLoadingDialog=loadingDialog;
		reqUserID=userID;	
	}

	protected void showQQScren() {
		this.list = list;
		this.classesList = classesList;
		final QQHolder holder = new QQHolder();
		View view = LayoutInflater.from(mcontext).inflate(R.layout.bookshelf_gift_editlist, null);
		holder.grid = (GridView) view.findViewById(R.id.bookshelfgiftedit_grid);
		holder.grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				CheckBox itemCheckBox = (CheckBox) view
//						.findViewById(R.id.bookshelfgift_listName);
//				User iteUser = (User) itemCheckBox.getTag();
//				checkedList.add(iteUser);
			}
		});

		holder.bookshelfgift_all=(RadioButton)view.findViewById(R.id.bookshelfgift_all);
		holder.bookshelfgift_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.grid.setAdapter(new BookShelfGiftqqAdapter(mcontext, totalList,reqUserID));
			}
		});
		holder.bookshelfgift_class=(RadioButton)view.findViewById(R.id.bookshelfgift_class);
		holder.bookshelfgift_class.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.grid.setAdapter(new BookShelfGiftqqAdapter(mcontext, classesList,reqUserID));
			}
		});
		holder.bookshelfgift_home=(RadioButton)view.findViewById(R.id.bookshelfgift_home);
		holder.bookshelfgift_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.grid.setAdapter(new BookShelfGiftqqAdapter(mcontext, homeList,reqUserID));
			}
		});
		holder.bookshelfgift_taecher=(RadioButton)view.findViewById(R.id.bookshelfgift_taecher);
		holder.bookshelfgift_taecher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.grid.setAdapter(new BookShelfGiftqqAdapter(mcontext, teacherList,reqUserID));
			}
		});
		holder.bookshelfgift_friend=(RadioButton)view.findViewById(R.id.bookshelfgift_friend);
		holder.bookshelfgift_friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.grid.setAdapter(new BookShelfGiftqqAdapter(mcontext, friendList,reqUserID));
			}
		});
	
		holder.btnok = (TextView) view.findViewById(R.id.bookshelf_ok);
		holder.btnok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < totalList.size(); i++) {
					User user = totalList.get(i);
					if (user.isUserChecked()) {
						checkedList.add(user);
					}
				}
				if (checkedList.size() > 0) {
					strname = "";
					strUserid = new String[checkedList.size()];
					for (int i = 0; i < checkedList.size(); i++) {
						if (i == checkedList.size() - 1) {
							strname += checkedList.get(i).getUserName();
						} else {
							strname += checkedList.get(i).getUserName() + ",";
						}
						strUserid[i] = checkedList.get(i).getUserId();

						checkedList.get(i).setUserChecked(false);
					}
					
					if(strActivityName.equals("BookShelfGiftEditActivity"))
					{((BookShelfGiftEditActivity) mcontext).setUserId(strUserid,strname);}
					else if(strActivityName.equals("BookShelfRecommendEditActivity"))
					{((BookShelfRecommendEditActivity) mcontext).setUserId(strUserid,strname);}
					
				}
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});

		holder.grid.setAdapter(new BookShelfGiftqqAdapter(mcontext, totalList,reqUserID));
		view.setTag(holder);

		mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(mcontext.getResources().getDrawable(
				R.drawable.talk_qqback));
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.showAsDropDown(frinedlist, 100, 10);

	}

	private void getScreenMethod() {
		((TalkGardenActivity) (mcontext)).getWindowManager()
				.getDefaultDisplay().getWidth();
		((TalkGardenActivity) (mcontext)).getWindowManager()
				.getDefaultDisplay().getHeight();

		mPopupWindow.getWidth();
		mPopupWindow.getHeight();
	}

	static class QQHolder {
		GridView grid = null;
		TextView btnok;
		
		RadioButton bookshelfgift_all;
		RadioButton bookshelfgift_class;
		RadioButton bookshelfgift_home;
		RadioButton bookshelfgift_taecher;
		RadioButton bookshelfgift_friend;
	}

	@Override
	public AssetManager getAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PackageManager getPackageManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentResolver getContentResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Looper getMainLooper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTheme(int resid) {
		// TODO Auto-generated method stub

	}

	@Override
	public Theme getTheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageResourcePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPackageCodePath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SharedPreferences getSharedPreferences(String name, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileInputStream openFileInput(String name)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileOutputStream openFileOutput(String name, int mode)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteFile(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getFileStreamPath(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFilesDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalFilesDir(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getExternalCacheDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] fileList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getDir(String name, int mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteDatabase(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getDatabasePath(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] databaseList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drawable getWallpaper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Drawable peekWallpaper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWallpaperDesiredMinimumWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWallpaperDesiredMinimumHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWallpaper(Bitmap bitmap) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWallpaper(InputStream data) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWallpaper() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startIntentSender(IntentSender intent, Intent fillInIntent,
			int flagsMask, int flagsValues, int extraFlags)
			throws SendIntentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBroadcast(Intent intent, String receiverPermission) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendOrderedBroadcast(Intent intent, String receiverPermission,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendStickyBroadcast(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendStickyOrderedBroadcast(Intent intent,
			BroadcastReceiver resultReceiver, Handler scheduler,
			int initialCode, String initialData, Bundle initialExtras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeStickyBroadcast(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent registerReceiver(BroadcastReceiver receiver,
			IntentFilter filter, String broadcastPermission, Handler scheduler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterReceiver(BroadcastReceiver receiver) {
		// TODO Auto-generated method stub

	}

	@Override
	public ComponentName startService(Intent service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stopService(Intent service) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean bindService(Intent service, ServiceConnection conn, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean startInstrumentation(ComponentName className,
			String profileFile, Bundle arguments) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getSystemService(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int checkPermission(String permission, int pid, int uid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingOrSelfPermission(String permission) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void enforcePermission(String permission, int pid, int uid,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingPermission(String permission, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingOrSelfPermission(String permission, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
		// TODO Auto-generated method stub

	}

	@Override
	public void revokeUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub

	}

	@Override
	public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int checkUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingUriPermission(Uri uri, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enforceUriPermission(Uri uri, String readPermission,
			String writePermission, int pid, int uid, int modeFlags,
			String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context createPackageContext(String packageName, int flags)
			throws NameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
