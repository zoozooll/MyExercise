package com.oregonscientific.meep.app;


import java.io.File;
import java.util.List;
import com.oregonscientific.meep.app.AppManager.OnApkListChangeListener;
import com.oregonscientific.meep.app.FullListViewActivity.EditMode;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.message.common.OsListViewItem;
import com.oregonscientific.meep.opengl.OSButton;
import com.oregonscientific.meep.tool.ImageDownloader;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout.LayoutParams;

public class GridViewFragment extends Fragment{
	private AppManager mAppManager;
	private List<OsListViewItem> mListViewItemList = null;
	private Thread mThread = null;
	private ViewGroup mViewGroup = null;
	private RelativeLayout mOptionLayout = null;
	private RelativeLayout mRenameLayout = null;
	
	private final static String CACHE_DIR = "ebook";
//private AbsoluteLayout mMainLayout = null;
	private Bitmap frameImage;
	private Bitmap bgImage;
	private String[] mGameArray = null;
	private String[] mBlockArray = null;
	private GestureDetector mGuestureDetector = null;
	private GestureDetector mItemGuestureDetector = null;
	private View mSelectedView = null;
	private EditMode mEditMode = EditMode.VIEW;
	private Handler mHandlerReadImg = null;
	private EditText mTxtRename = null;
	private ImageView mSnackIcon = null;
	private Button mBtnConfirm = null;
	private FullListViewActivity mContext;
	private AppsAdapter appAdapter =null;
	private GridView myApps = null;
	private LoadImagesThread appsLoadThread = null;
	//private MeepDbCommunicationCtrl mMeepDbCommunicationCtrl = null;

	private float mInitSpan = 0;

	private int mCorX = 0;
	private int mCorY = 0;

	private OSButton button;
	private ImageDownloader imageDownloader = null;
	private DialogFragment popupFragment;
	private ImageView ring_delete;
	private ImageView ring_rename;


	
	public static GridViewFragment newInstance(String tag) {
		GridViewFragment f = new GridViewFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("tag", tag);
        f.setArguments(args);

        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.full_list_view, container, false);
		
		mContext = (FullListViewActivity) this.getActivity();
		mAppManager = new AppManager(mContext);
		mAppManager.setmOnApkListChangeListener(mOnApkListChangeListener);
		mGuestureDetector = new GestureDetector(mContext, mOnGuestureListener);
		mItemGuestureDetector = new GestureDetector(mContext, mOnGuestureListener);
	    frameImage = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_frame);
	    bgImage = BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_mask);
	    mBlockArray = mContext.getIntent().getStringArrayExtra(Global.STRING_BLOCK_LIST);
		mGameArray = mContext.getIntent().getStringArrayExtra(Global.STRING_GAME_LIST);
		imageDownloader = new ImageDownloader(mContext, CACHE_DIR);
		imageDownloader.setmImageDownloadListener(mDownloadListener);
		//mMeepDbCommunicationCtrl = new MeepDbCommunicationCtrl(mContext, Global.AppType.App);
		initHandler();
		initUIComponent(v);

		// 2013-6-13 - Zoya - update shelves
		appsLoadThread = new LoadImagesThread();
		appsLoadThread.start();

		// 2013-6-13 - Zoya - Monitor application installation and uninstall events
		mInstallReceiver();

		return v;
	}
	
	// 2013-6-13 - Zoya - MyInstalledReceiver class
	private final BroadcastReceiver MyInstalledReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("tag", "sdcard action----" + action);
			if (action.equals("android.intent.action.PACKAGE_ADDED")) {// install
				appsLoadThread = new LoadImagesThread();
				appsLoadThread.start();
			} else if (action.equals("android.intent.action.PACKAGE_REMOVED")) {// uninstall
				appsLoadThread = new LoadImagesThread();
				appsLoadThread.start();
			}
		}
	};

	private void mInstallReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		intentFilter.addDataScheme("package");
		mContext.registerReceiver(MyInstalledReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(MyInstalledReceiver);
		super.onDestroy();
	}

	private void initUIComponent(View v) {
		//2013-3-20 - Amy - update shelves use GridView
		myApps = (GridView)v.findViewById(R.id.myApp);
		myApps.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myApps.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
					hideRingMenu();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
		appAdapter = new AppsAdapter();
		myApps.setAdapter(appAdapter);
		
		/*mBtnOptionDelete.setOnClickListener(onDeleteBtnClickListener);
		mBtnOptionEdit.setOnClickListener(btnEditOnClickListener);*/
		
		mSnackIcon = (ImageView)v.findViewById(R.id.imageViewSnackView);
		
		mSnackIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToMeepAppView();
			}
		});
		
		/*mBtnConfirm = (Button)v.findViewById(R.id.buttonConfirmButton);
		mBtnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mEditMode == EditMode.DELETE)
				{
					for(int i = 0; i< mListViewItemList.size(); i++)
					{
						if(mListViewItemList.get(i).isChecked())
						{
							//deleteFile(mListViewItemList.get(i).getPath());
							deleteItem(mListViewItemList.get(i));
						}
							
					}
				}
			}
		});*/
		
		mRenameLayout = (RelativeLayout)v.findViewById(R.id.relativeLayoutRename);
		mTxtRename = (EditText)v.findViewById(R.id.editTextRename);
		/*mTxtRename.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER)
				{
					
				}
				return false;
			}
		});	*/
		mViewGroup = (ViewGroup)v. findViewById(R.id.appFullListViewLayout);
	}
	//2013-3-20 -Amy- Use PopupWindow replace RingMenu
	PopupWindow popWindow = null;
	private View ringMenuShow = null;
	
	private void showRingMenu(float x, float y, OsListViewItem item) {
		if(Global.DISABLE_RING_MENU){
			Log.e("MeepApp-showRingMenu","DISABLE_RING_MENU");
			return;
		}
		
		int left = 0;
		int right = 1;
		int buttonNum = 1;
		
		if (y < 0){
			y= -20;
		}
		//2013-4-16 -Amy- modified to change pop images
		FrameLayout two_button_left;
		RelativeLayout contentLayout;
		int xx = (int)x+55;
		int yy = (int)y+5;
		LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_delete, null);
		two_button_left = (FrameLayout) ringMenuShow.findViewById(R.id.two_button_left);
		contentLayout = (RelativeLayout) ringMenuShow.findViewById(R.id.contentLayoutleft);
		android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) contentLayout.getLayoutParams();
		layoutParams.leftMargin = xx;
		//Log.e("cdf",""+yy);
		if(yy>230){
			yy=230;
		}
		layoutParams.topMargin = yy;
		contentLayout.setLayoutParams(layoutParams);
		popWindow.setContentView(ringMenuShow);
		popWindow.update();
		two_button_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ringMenuShow.setVisibility(View.GONE);
			}
		});
	
		popWindow.showAtLocation(myApps, Gravity.START, (int)x+70, (int)y-120);
		ringMenuHandler(left, buttonNum, item);
	}
	
	private void hideRingMenu() {
		/*mViewGroup.removeView(ringMenu);
		ringMenu = null;*/
		//2013-3-20 -Amy- Use PopupWindow replace RingMenu
		if(popWindow != null && popWindow.isShowing()){
			popWindow.dismiss();
		}
		ringMenuShow = null;
	}
	
	private void ringMenuHandler(int leftRight, int buttonNum, OsListViewItem item){
		if (leftRight == 0  && buttonNum == 1) {
			//left menu with two button
			leftMenuOneButton(item);
		}
	}
	
	private void leftMenuOneButton(final OsListViewItem item) {
		//2013-4-17 -Zoya- update ring menu layout.
		ring_delete = (ImageView) ringMenuShow.findViewById(R.id.btnDelete);
		//ring_menu_right_one = (ImageView) ringMenuShow.findViewById(R.id.ring_menu_right_one3); 

		ring_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View pV) {	
				mViewGroup.removeView(ringMenuShow);
				//deleteItem1(item);
				deleteItem(item);
				//notification = new NotificationMessage(mContext, null, DELETE_TITLE, DELETE_MESSAGE);
				//2013-3-20 -Amy- Use PopupWindow replace RingMenu
				appAdapter.notifyDataSetChanged();
				hideRingMenu();
			}
		});
		
		/*ring_menu_right_one.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View pV) {	
				mViewGroup.removeView(ringMenuShow);
				deleteItem(item);	
				appAdapter.notifyDataSetChanged();
				hideRingMenu();
			}
		});
*/
//		ring_rename.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View pV) {
//				mViewGroup.removeView(ringMenu);
//				//renameLayer(item);
//			}
//		});
	}
	
	//*******load image thread*******
	private void initHandler() {
		mHandlerReadImg = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:{
					//mListViewItemList = mAppManager.getAllItems(false);
					//for (int position = 0; position < list.size(); position  ++) {
						//drawImage(list.get(position), position);
					//}
					appAdapter.notifyDataSetChanged();
					break;
				}
				case 2: //draw shelf only
					//drawShelfOnly();
					 break;
				default:
					break;
				}
				//2013-3-20 - Amy - update shelves use GridView
				//mLayout.invalidate();
				super.handleMessage(msg);
			}
		};
		//mLayout.removeAllViews();
		//mListViewItemList.clear();
		//mAppManager.clearAppItems();
		mThread = new Thread(runReadImage);
		mThread.start();
	}

	private Runnable runReadImage = new Runnable() {

		@Override
		public void run() {
			mHandlerReadImg.sendEmptyMessage(1);
			//loadAndDrawImage();
			/*try {
				mThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
	};
	
	//2013-3-19 - Amy - update shelves use GridView
	private static class ViewHolder {  
		ImageView pic;
		TextView pic_title;
	}  
	private class AppsAdapter extends BaseAdapter
	{
		private LayoutInflater inflater = LayoutInflater.from(mContext);
		
		@Override
		public int getCount() {
			//Log.e("cdf","mListViewItemList.size() == "+mListViewItemList.size()); 
			if(mListViewItemList == null) {
				return 0;
			}else {
				return mListViewItemList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			if (mListViewItemList == null) {
				return null;
			}
			return mListViewItemList.get(position);
			
		}

		@Override
		public long getItemId(int position) {
			if (mListViewItemList == null) {
				return -1;
			} 
			return mListViewItemList.get(position).hashCode();
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;			
			if(convertView == null){
				holder = new ViewHolder();				
				convertView = inflater.inflate(R.layout.gridview_item, null);				
				holder.pic = (ImageView)convertView.findViewById(R.id.appic);
				holder.pic_title = (TextView) convertView.findViewById(R.id.appic_title);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final OsListViewItem item = mListViewItemList.get(position);
			holder.pic_title.setText(item.getName());				
			//holder.pic.setImageBitmap(item.getImage());
			imageDownloader.download(item.getPath(), holder.pic, bgImage, null);
			//2013-7-18 -Amy- Apps with same name cannot be launched correctly in grid view
			holder.pic.setTag(R.id.TAG, item);
			//Log.e("cdf","item.getName() == 2  "+item.getName());
			holder.pic.setOnClickListener(btnItemClickListener);
			holder.pic.setOnLongClickListener(btnItemLongClickListener);
			convertView.setVisibility(View.VISIBLE);
			return convertView;
		}
	}
		
	//-------end load image thread----------

	//******start define listener******
	private OnGestureListener mOnGuestureListener = new OnGestureListener() {
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (mEditMode == EditMode.VIEW) {
				if(mSelectedView!=null)
				{
					String name = (String) mSelectedView.getTag();
					OsListViewItem item = mAppManager.findListViewItemByName(name);
					if (mSelectedView.getTag() != null) {
						if (item != null) {
							goToApp(item.getPath());
						}
					}
				}
			}
			else
			{
				hideOption();
			}
			return false;
		}
		
		@Override
		public void onShowPress(MotionEvent e) {
			//do nothing	
		}
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			//do nothing
			return false;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
//			float x = e.getX() - 120;
//			float y = e.getY() - 90;
//			showOption(x,y);
//			setEditMode(EditMode.OPTION_SHOWN);
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			//do nothing
			return false;
		}
		
		@Override
		public boolean onDown(MotionEvent e) {
			//do nothing
			return false;
		}
	};

	private OnClickListener btnItemClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//String name = (String) v.getTag(R.id.TAG);
			//OsListViewItem item = mAppManager.findListViewItemByName(name);
			//2013-7-18 -Amy- Apps with same name cannot be launched correctly in grid view
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
			if (item != null && ringMenuShow == null) {
				goToApp(item.getPath());
			}		
			hideRingMenu();
		}
	};
	
	private OnLongClickListener btnItemLongClickListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			final int[] location = new int[2]; 
			v.getLocationOnScreen(location);
			mCorX = location[0];
			mCorY = location[1];
			
			float x = mCorX - 90;
			float y = mCorY - 75;
			hideRingMenu();

			//String name = (String) v.getTag(R.id.TAG);
			//OsListViewItem item = mAppManager.findListViewItemByName(name);
			//2013-7-18 -Amy- Apps with same name cannot be launched correctly in grid view
			OsListViewItem item = (OsListViewItem) v.getTag(R.id.TAG);
			showRingMenu(x, y, item);	

			return true;
		}
	};
	

	// 2013-6-13 - Zoya - update shelves
	class LoadImagesThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mListViewItemList != null) {
				mListViewItemList.clear();
			}
			Message message = new Message();
			// Log.e("cdf","cdf-----------");
			loadAndDrawImage();
			message.what = 1;
			mHandlerReadImg.sendMessage(message);
			super.run();
		}
	}
	

	private void loadAndDrawImage() {
		mListViewItemList = mAppManager.getAllItems(false);		
	}
	
	
	private boolean isAppExist(String name, String[] nameArray) {
		String tempName = name + Global.FILE_TYPE_PNG;
		
		if (nameArray != null) {
			for(int i =0; i< nameArray.length; i++) {
				if(nameArray[i].equals(tempName)) {
					return true;
				}
			}
		}
		return false;
	}
	

	
	private String getPackageName(OsListViewItem item)
	{
		if(item != null)
		{
			String content = item.getPath();
			String[] strArr = content.split("/");
			return strArr[0];
		}
		return null;
	}
			
	
	private String getFileNameOnly(String fileName)
	{
		return fileName.substring(0,fileName.lastIndexOf('.'));
	}
	
	private void goToApp(String path) {
		Intent intent = new Intent(mContext, FullListViewActivity.class);
		intent.setComponent(ComponentName
				.unflattenFromString(path));
		intent.addCategory("android.intent.category.LAUNCHER");
		//intent.putExtra(Global.STRING_PATH, path);
		try {
			startActivity(intent);

	        MeepLogger meepLogger = new MeepLogger(mContext);
	        meepLogger.p("has opened: " + path);
		} catch (Exception ex) {
			Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
			ex.printStackTrace();
		}

	}
	
	private void showOption(float x, float y) {
		//Log.d("testOnTouch", "x:" + x + " y:" + y + " mcorX:" + mCorX + " mcorY:" + mCorY );
		mOptionLayout.setX(x+mCorX);
		mOptionLayout.setY(y + mCorY);
		mOptionLayout.setVisibility(View.VISIBLE);
	}

	private void hideOption() {
		hideRingMenu();
		mOptionLayout.setVisibility(View.GONE);
		setEditMode(EditMode.VIEW);
	}

	private void setEditMode(EditMode editMode) {
		mEditMode = editMode;
		Toast.makeText(mContext, editMode.toString(), Toast.LENGTH_SHORT).show();
		if(editMode == EditMode.DELETE)
		{
			//2013-3-20 - Amy - update shelves use GridView
			//showAllDeleteIcons();
			mBtnConfirm.setVisibility(View.VISIBLE);
		}
		else
		{
			//hideAllDeleteIcons();
			mBtnConfirm.setVisibility(View.GONE);
		}
	}
	

	
	private void changeToDeleteMode()
	{
		
	}
	public LayoutInflater getLayoutInflater() {
        return this.getActivity().getWindow().getLayoutInflater();
    }

	private void deleteItem(OsListViewItem item) {
		
		//uninstallItemName = item.getPath().substring(0, item.getPath().lastIndexOf("/"));
		//Log.d("delete app", "delete app:" + itemName + ".png");
		uninstallApplication(getPackageName(item));
		//uninstallPackageName = getPackageName(item);
//		deleteItemFile(PATH_SMALL_ICON_DIR + itemName + ".png");
//		deleteItemFile(PATH_LARGE_ICON_DIR+ itemName + ".png");
//		deleteItemFile(PATH_LARGE_DIM_ICON_DIR + itemName + ".png");
		
//		mListViewItemList.remove(item);
//		redrawListView();
	}
	
	private void deleteItemFile(String path) {
		// delete the real item
		File file = new File(path);
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			Log.e("MovieFullListView", "delete file error : " + e.toString());
		}
	}
	
	private void uninstallApplication(String packageName)
	{
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		startActivity(uninstallIntent);
	}
	
	
	private void goToMeepAppView(){
		mContext.showDetails(FullListViewActivity.STATE_FRAG_SNACK);
		/*Intent myIntent = new Intent(this.getActivity(), MeepAppActivity.class);
		myIntent.putExtra(Global.STRING_TYPE, "app");
		myIntent.putExtra(Global.STRING_GAME_LIST, mGameArray);
		myIntent.putExtra(Global.STRING_BLOCK_LIST, mBlockArray);
		
		try {
			startActivityForResult(myIntent, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}		*/
	}
	
	private OnApkListChangeListener mOnApkListChangeListener = new OnApkListChangeListener() {
		
		@Override
		public void onApkLoadFail() {
			//Log.d(TAG, "onApkLoadFail");
		}
		
		@Override
		public void onApkListChanged(List<OsListViewItem> apks) {
			//Log.d(TAG, "onApkListChanged");
			// deleted by aaronli Mar21 throws exceptions
			//initHandler();
		}

		@Override
		public void onGetServiceAccount(String user) {
			//initHandler();
			
		}
	};
	
	private ImageDownloader.ImageDownloadListener mDownloadListener = new ImageDownloader.ImageDownloadListener() {

		@Override
		public void onDownloadSuccessed(ImageView view, Bitmap bitmap) {
			view.setImageBitmap(bitmap);

		}

		@Override
		public void onDownloadFromCache(ImageView view, Bitmap bitmapFromCache) {
			view.setImageBitmap(bitmapFromCache);

		}

		@Override
		public void onDownloadFail(ImageView view) {
			view.setImageBitmap(bgImage);

		}

		/*@Override
		public Bitmap loadImageFromUrl(String url) {
			// TODO Auto-generated method stub
			return null;
		}*/

		@Override
		public Bitmap loadImageFromUrl(String url) {
			return mAppManager.getAppIconBitmap(AppManager.getPackageName(url),frameImage, bgImage);
		}
	};


}
		
