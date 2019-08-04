package com.oregonscientific.meep.store2;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.store2.PopUpDialogFragment.UpdateUserCoinsListener;
import com.oregonscientific.meep.store2.ctrl.ImageDownloadCtrl.DownloadListener;
import com.oregonscientific.meep.store2.ctrl.RestRequest.GetStoreItemListener;
import com.oregonscientific.meep.store2.ctrl.RestRequest.LoginListener;
import com.oregonscientific.meep.store2.ctrl.RestRequest.OtaUpdateListener;
import com.oregonscientific.meep.store2.ctrl.StoreItemDownloadCtrl;
import com.oregonscientific.meep.store2.db.DbAdapter;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadImageItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreLoginInfo;
import com.oregonscientific.meep.store2.object.OtaUpdateFeedback;

public class MainActivity extends BaseActivity implements UpdateUserCoinsListener {

	private Context mContext;
	private MeepStoreApplication mApp;
	private MeepStoreService mService;
	boolean mBound = false;
	private int pictureIndex;
	private float touchDownX;
	private float touchUpX;
	ArrayList<Drawable> arrayPictures = new ArrayList<Drawable>();
	ArrayList<String> bannerIds = new ArrayList<String>();
	BannarTimeTask myTask = new BannarTimeTask();
	Timer myTimer = new Timer();
	private final Object mLock = new Object();

	TextView usercoins;

	View background;
	View eye;
	ImageButton btnSearch;
	ImageButton btnMyapps;
	ImageButton btnMedia;
	ImageButton btnGame;
	ImageButton btnApps;
	ImageButton btnEbook;
	ImageButton screenLeft;
	ImageButton screenRight;
	ImageButton coupon;
	ImageButton ota;

	public ImageSwitcher imageSwitcher;

	DialogFragment myAppsFragment;
	DialogFragment detailFragment;

	private final int NO_NETWORK = 3;
	private final int LOADING = 4;
	private final int TIMEOUT = 5;
	private final int OTA_UPGRADE = 6;
	private final int TOKEN_NULL_DIALOG = 7;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		loading();

		retriveAccountInformation();

		if (!isMyServiceRunning()) {
			Intent intent = new Intent(this, MeepStoreService.class);
			startService(intent);
		}

		mContext = this;
		mApp = (MeepStoreApplication) this.getApplicationContext();

		usercoins = (TextView) findViewById(R.id.usercoins);
		searchText = (EditText) findViewById(R.id.searchtext);

		background = findViewById(R.id.mainmenu);
		eye = findViewById(R.id.eye);
		btnSearch = (ImageButton) findViewById(R.id.searchbtn);
		btnMyapps = (ImageButton) findViewById(R.id.btnMyapps);
		btnMedia = (ImageButton) findViewById(R.id.btnMedia);
		btnGame = (ImageButton) findViewById(R.id.btnGame);
		btnApps = (ImageButton) findViewById(R.id.btnApps);
		btnEbook = (ImageButton) findViewById(R.id.btnEbook);
		screenLeft = (ImageButton) findViewById(R.id.screenLeft);
		screenRight = (ImageButton) findViewById(R.id.screenRight);
		coupon = (ImageButton) findViewById(R.id.couponicon);
		ota = (ImageButton) findViewById(R.id.otaicon);

		imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		new Thread(mInitRunnable).start();
		// initComponent();
		// new InitStore().execute();
		initListeners();
		myHandler.postDelayed(eyeSplash, 5000);
	}

	@Override
	public void onLowMemory() {
		mApp.getImageDownloader().clearCache();
	}

	public void previousBannar() {
		if (!arrayPictures.isEmpty()) {
			pictureIndex = pictureIndex == 0 ? arrayPictures.size() - 1
					: pictureIndex - 1;
			imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left));
			imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_right));
			imageSwitcher.setImageDrawable(arrayPictures.get(pictureIndex));
		}
	}

	public void nextBannar() {
		if (!arrayPictures.isEmpty()) {
			pictureIndex = pictureIndex == arrayPictures.size() - 1 ? 0
					: pictureIndex + 1;
			imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right));
			imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_left));
			imageSwitcher.setImageDrawable(arrayPictures.get(pictureIndex));
		}
	}

	public String checkSearchText() {
		String text = null;
		TextView search = (TextView) findViewById(R.id.searchtext);
		if (search != null) {
			text = search.getText().toString();
			if (text != null && text.trim().equals("")) {
				text = null;
			}
		}
		return text;
	}

	

	public void initListeners() {
		btnMyapps.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					flag = 2;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					flag = 0;
				}
				return false;
			}
		});
		searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
					performSearch();
					return true;
				}
				return false;
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				performSearch();
			}
		});
		screenLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				previousBannar();
			}
		});
		screenRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nextBannar();
			}
		});
		coupon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.COUPON_CODE_EDIT);
				popupFragment.show(getFragmentManager(), "dialog");
			}
		});
		ota.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mApp.getLoginInfo().ota != null) {
					if (!mApp.isNetworkAvailable(mContext)) {
						handler.sendEmptyMessage(NO_NETWORK);
					} else {
						handler.sendEmptyMessage(LOADING);
						// send url of OTA
						mApp.getRestRequest().otaUpdate(mApp.getLoginInfo().ota, mApp.getUserToken());
						mApp.getRestRequest().setOtaUpdateListener(new OtaUpdateListener() {

							@Override
							public void onUpdateReceived(
									OtaUpdateFeedback otaUpdateFeedback) {
								if (popupFragment != null) {
									popupFragment.dismiss();
								}
								switch (otaUpdateFeedback.getCode()) {
								case 200:
									Bundle bundle = new Bundle();
									bundle.putString("OTA", otaUpdateFeedback.getUrl());
									bundle.putString("message", otaUpdateFeedback.getChangelog());
									bundle.putString("version", otaUpdateFeedback.getVersionName());
									Message message = new Message();
									message.setData(bundle);
									message.what = OTA_UPGRADE;
									handler.sendMessage(message);

									break;
								case 999:
									handler.sendEmptyMessage(TIMEOUT);
									break;
								default:
									// error
									break;

								}
							}
						});
					}

				}
			}
		});
		imageSwitcher.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(MainActivity.this);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				return imageView;
			}
		});

		imageSwitcher.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					touchDownX = event.getX();
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					touchUpX = event.getX();
					if (touchUpX - touchDownX > 100) {
						previousBannar();
					} else if (touchDownX - touchUpX > 100) {
						nextBannar();
					} else {
						imageSwitcher.setEnabled(false);
						// imageSwitcher.setClickable(false);
						try {
							if (bannerIds.size() > 0) {
								if (mApp.isNetworkAvailable(mContext)) {
									handler.sendEmptyMessage(4);
									String id = bannerIds.get(pictureIndex);
									setGetSingleStoreItemListener();
									mApp.getRestRequest().getStoreItem(id, mApp.getUserToken());
								} else {
									handler.sendEmptyMessage(3);
								}
							}
						} catch (Exception e) {

						}
					}
					return true;
				}
				return false;
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Intent intent = new Intent(this, MeepStoreService.class);
		// bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		if (mApp != null && mApp.getLoginInfo() != null
				&& mApp.getLoginInfo().coins != null) {
			usercoins.setText(mApp.getLoginInfo().coins);
		}
	}

	/**
	 * get package name in Uri data
	 * 
	 * @param intent
	 * @return package name
	 */
	public String getPackageNameStoreItem(Intent intent) {
		Uri data = intent.getData();
		if (data != null) {
			String packageName = data.getQueryParameter("id");
			return packageName;
		}
		return null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		btnSearch.setEnabled(true);
		mHoldButtonStatus = false;
		
		if (mApp != null && mApp.getLoginInfo() != null&& mApp.getLoginInfo().coins != null)
		{
			// get package name successfully
			String packageName = checkReceivedItemPackageName();
			if (packageName != null) {
				if(detailFragment!=null &&detailFragment.isVisible() && packageName.equals(DetailFragment.getPackageNameOfItem()))
				{
					return;
				}
				handler.sendEmptyMessage(LOADING);
				openSingleStoreItem(packageName);
			}
		}

	}

	public void openSingleStoreItem(String packageName) {
		setGetSingleStoreItemListener();
		mApp.getRestRequest().getStoreItem(packageName, mApp.getUserToken());
	}

	public String checkReceivedItemPackageName() {
		// via direct intent
		String packageName = getPackageNameStoreItem(getIntent());
		MeepStoreLog.LogMsg( "open detail page (activity):" + packageName);

		// through broadcast
		if (packageName == null) {
			packageName = getIntent().getStringExtra(Constants.KEY_PAKCAGE_NAME_SINGLE_ITEM);
			//clear this extra
			getIntent().removeExtra(Constants.KEY_PAKCAGE_NAME_SINGLE_ITEM);
			MeepStoreLog.LogMsg( "open detail page (broadcast):" + packageName);
		}
		else
		{
			//clear data
			getIntent().setData(null);
		}
		return packageName;

	}

	public void setGetSingleStoreItemListener() {
		mApp.getRestRequest().setGetStoreItemListener(new GetStoreItemListener() {

			@Override
			public void onStoreItemReceived(MeepStoreItem feedback) {
				try {
					if (popupFragment != null) {
						popupFragment.dismiss();
					}
					imageSwitcher.setEnabled(true);
					// imageSwitcher.setClickable(true);
					if (feedback != null) {
						displayDetailPage(feedback);
					} else {
						handler.sendEmptyMessage(5);
					}
				} catch (Exception e) {
					MeepStoreLog.LogMsg( "current activity has been closed");
				}
			}
		});
	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (MeepStoreService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	DbAdapter dbApt;

	private void initComponent() {
		if (!mApp.isNetworkAvailable(mContext)) {
			MeepStoreLog.LogMsg( "network isn't available");
			handler.sendEmptyMessage(NO_NETWORK);
			return;
		}

		// get token in SharedPreference
		if (!checkAbleLogin()) {
			handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
		} else {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					initComponent2();
				}
			});
		}
		if (mApp.getImageLoader() != null) {
			mApp.getImageLoader().clearImageCacheInDB();
		}
		BannarTimeTask myTask = new BannarTimeTask();
		myTimer.schedule(myTask, 0, 5000);
	}

	private Runnable mInitRunnable = new Runnable() {
		public void run() {
			initComponent();
		}
	};

	private boolean checkTokenNotNull() {
		// get token in SharedPreference
		String mUserToken = mApp.getAccountToken(MainActivity.this);
		MeepStoreLog.LogMsg( mUserToken);
		if ("null".equalsIgnoreCase(mUserToken) || mUserToken == null
				|| mUserToken.length() == 0) {
			return false;
		}
		return true;
	}

	private boolean checkAbleLogin() {
		int i = 0;
		for (i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (checkTokenNotNull()) {
				return true;
			}
		}
		return false;
	}

	private DownloadListener downloadListener = new DownloadListener() {
		@Override
		public void onDownloadCompleted(boolean downloadAborted,
				DownloadImageItem item) {
			// TODO Auto-generated method stub

		}
	};

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("meepStoreService", "disconnected");
			mBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MeepStoreService.LocalBinder) service).getService();
			mBound = true;
			Log.d("meepStoreService", "connected");
		}
	};

	private void initComponent2() {
		StoreItemDownloadCtrl storeDownloadCtrl = mApp.getStoreDownloadCtrl();
		if (mApp.getDatabaseAdapter() == null) {
			mApp.setDatabaseAdapter(new DbAdapter(mContext));
		}

		if (storeDownloadCtrl == null) {
			storeDownloadCtrl = new StoreItemDownloadCtrl(this, mApp.getDatabaseAdapter().getDatabase());
		}
		mApp.getImageDownloadCtrl().setDownloadListener(downloadListener);

		// =========test=============
		// Login
		if (mApp.isNetworkAvailable(mContext)) {
			mApp.getRestRequest().login(mApp.getUserToken());
			mApp.getRestRequest().setLoginListener(new LoginListener() {
				@Override
				public void onLoginReceived(MeepStoreLoginInfo loginInfo) {
					if (popupFragment != null) {
						popupFragment.dismiss();
					}
					if (loginInfo != null) {
						if (loginInfo.code == 200) {
							Log.i("login", "login ok -" + loginInfo.name);
							Log.i("login", "login ok-" + loginInfo.coins);
							// should OTA or not?
							if (loginInfo.ota != null) {
								if (!loginInfo.ota.trim().equals("")) {
									ota.setVisibility(View.VISIBLE);
								} else {
									ota.setVisibility(View.GONE);
								}
							} else {
								ota.setVisibility(View.GONE);
							}
							// Update prefix for image-download
							if (loginInfo.url_prefix.lastIndexOf('/') + 1 != loginInfo.url_prefix.length()) {
								loginInfo.url_prefix = loginInfo.url_prefix
										+ "/";
							}
							// Change the Store Theme
							if (loginInfo.theme != null) {
								changeTheme(loginInfo.theme);
							}

							// get package name successfully
							String packageName = checkReceivedItemPackageName();
							if (packageName != null) {
								handler.sendEmptyMessage(LOADING);
								openSingleStoreItem(packageName);
							}

							// set LoginInfo into Global Variable
							mApp.setLoginInfo(loginInfo);
							handler.sendEmptyMessage(1);
							mApp.getImageDownloadCtrl().downloadBanner(loginInfo.banner);
							mApp.getImageDownloadCtrl().setDownloadListener(new DownloadListener() {
								@Override
								public void onDownloadCompleted(
										boolean downloadAborted,
										DownloadImageItem item) {
									if (!downloadAborted) {
										mApp.getLoginInfo().updateBannerImage(item.getId(), item.getImage());

										Drawable drawable = new BitmapDrawable(getResources(), item.getImage());
										synchronized (mLock) {
											arrayPictures.add(drawable);
										}

										String id = item.getId().substring(item.getId().lastIndexOf('?') + 1);
										synchronized (mLock) {
											bannerIds.add(id);
										}
									}
								}
							});

							mApp.getImageDownloadCtrl().startDownload();
						} else {
							// Login Failure
							handler.sendEmptyMessage(2);
						}
					} else {
						handler.sendEmptyMessage(TIMEOUT);
					}
				}

			});

		} else {
			handler.sendEmptyMessage(3);
		}

	}

	private class BannarTimeTask extends TimerTask {
		public void run() {
			handler.sendEmptyMessage(0);

		}
	}

	public Handler getHandler() {
		return handler;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case 0:
					nextBannar();
					break;
				case 1:
					usercoins.setText(mApp.getLoginInfo().coins);
					break;
				case 2:
					showLoginInFailedDialog();
					break;
				case NO_NETWORK:
					showNoNetwork();
					break;
				case LOADING:
					loading();
					break;
				case TIMEOUT:
					showTimeOut();
					break;
				case OTA_UPGRADE:
					String otaUrl = msg.getData().getString("OTA");
					String message = msg.getData().getString("message");
					String version = msg.getData().getString("version");
					popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.OTA_UPGRADE, otaUrl, message, version);
					if (getFragmentManager() != null)
						popupFragment.show(getFragmentManager(), "dialog");
					break;
				case TOKEN_NULL_DIALOG:
					FinishActivityDialog dialog = new FinishActivityDialog(MainActivity.this, R.string.login_title_msg);
					dialog.show();
					break;
				}
			} catch (Exception e) {
				MeepStoreLog.LogMsg( "current activity has been closed");
			}

		}
	};

	public static final int THEME_CHRISTMAS = 0;
	public static final int THEME_EASTER = 1;
	public static final int THEME_HALLOWEEN = 2;
	public static final int THEME_BACK_TO_SCHOOL = 3;

	/**
	 * change theme according to festival
	 * 
	 * @param festivalName
	 */
	public void changeTheme(String festivalName) {
		if (festivalName.equals(MeepStoreLoginInfo.CHRISTMAS)) {
			handlerTheme.sendEmptyMessage(THEME_CHRISTMAS);
		} else if (festivalName.equals(MeepStoreLoginInfo.EASTER)) {
			handlerTheme.sendEmptyMessage(THEME_EASTER);
		} else if (festivalName.equals(MeepStoreLoginInfo.HALLOWEEN)) {
			handlerTheme.sendEmptyMessage(THEME_HALLOWEEN);
		} else if (festivalName.equals(MeepStoreLoginInfo.BACK_TO_SCHOOL)) {
			handlerTheme.sendEmptyMessage(THEME_BACK_TO_SCHOOL);
		}
	}

	private Handler handlerTheme = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
				case THEME_CHRISTMAS:
					background.setBackgroundResource(R.drawable.meep_market_mainmenu_bg_xmas);
					btnMyapps.setImageResource(R.drawable.btn_myapps_xmas);
					break;
				case THEME_EASTER:
					background.setBackgroundResource(R.drawable.meep_market_mainmenu_bg_easter);
					btnMyapps.setImageResource(R.drawable.btn_myapps_easter);
					break;
				case THEME_HALLOWEEN:
					background.setBackgroundResource(R.drawable.meep_market_mainmenu_bg_halloween);
					btnMyapps.setImageResource(R.drawable.btn_myapps_halloween);
					break;
				case THEME_BACK_TO_SCHOOL:
					background.setBackgroundResource(R.drawable.meep_market_mainmenu_bg_school);
					btnMyapps.setImageResource(R.drawable.btn_myapps_school);
					break;
				}
			} catch (Exception e) {
				MeepStoreLog.LogMsg( "current activity has been closed");
			}

		}
	};

	private void showLoginInFailedDialog() {
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOGIN_FAILED);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	private void showNoNetwork() {
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	private void loading() {
		if(popupFragment!=null)
		{
			popupFragment.dismiss();
		}
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	private void showTimeOut() {
		popupFragment.dismiss();
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
		popupFragment.show(getFragmentManager(), "dialog");
	}
	
	private void showMediaNotAvailableDialog() {
		popupFragment.dismiss();
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.MEDIA_NOT_AVAILABLE);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	@Override
	protected void onStop() {
		// dbApt.close();
		if (mBound) {
			unbindService(mServiceConnection);
			mBound = false;
		}
		super.onStop();
	}

	@Override
	public void updateUserCoinsListener() {
		handler.sendEmptyMessage(1);
	}

	// eye
	Thread eyeSplash = new Thread(new Runnable() {
		@Override
		public void run() {
			if (flag == 2) {
				myHandler.postDelayed(eyeSplash2, 1000);
			} else {
				eye.setVisibility(View.VISIBLE);
				myHandler.postDelayed(eyeSplash2, 150);
			}
		}
	});
	Handler myHandler = new Handler();
	private int flag = 0;
	Thread eyeSplash2 = new Thread(new Runnable() {
		@Override
		public void run() {
			eye.setVisibility(View.GONE);
			if (flag == 0) {
				myHandler.postDelayed(eyeSplash, 150);
				flag = 1;
			} else if (flag == 1) {
				flag = 0;
				myHandler.postDelayed(eyeSplash, 5000);
			} else if (flag == 2) {
				myHandler.postDelayed(eyeSplash, 3000);
			}
		}
	});

	private boolean mHoldButtonStatus = false;

	public void onButtonClicked(View v) {
		if (mHoldButtonStatus)
			return;
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.searchbtn:
			mHoldButtonStatus = true;
			v.setEnabled(false);
			performSearch();
			break;
		case R.id.btnMyapps:
			mHoldButtonStatus = true;
			intent.setClass(MainActivity.this, ActivityMyApps.class);
			startActivity(intent);
			break;
		case R.id.btnMedia:
			mHoldButtonStatus = true;
			showMediaNotAvailableDialog();
			mHoldButtonStatus = false;
			// Intent intent = new Intent();
			// intent.setClass(MainActivity.this, ActivityMedia.class);
			// startActivity(intent);
			break;
		case R.id.btnGame:
			mHoldButtonStatus = true;
			intent.setClass(MainActivity.this, ActivityGame.class);
			startActivity(intent);
			break;
		case R.id.btnApps:
			mHoldButtonStatus = true;
			intent.setClass(MainActivity.this, ActivityApp.class);
			startActivity(intent);
			break;
		case R.id.btnEbook:
			mHoldButtonStatus = true;
			intent.setClass(MainActivity.this, ActivityEbook.class);
			startActivity(intent);
			break;
		}
	}

	public void displayDetailPage(MeepStoreItem item) {
		if (detailFragment == null ) {
			detailFragment = DetailFragment.newInstance(item);
			detailFragment.show(getFragmentManager(), "dialog");
		} else {
			if (!detailFragment.isVisible()) {
				DetailFragment.setStoreItem(item);
				detailFragment.show(getFragmentManager(), "dialog");
			} else{
				detailFragment.dismiss();
				detailFragment = DetailFragment.newInstance(item);
				detailFragment.show(getFragmentManager(), "dialog");
			}
				
		}
	}

	public void retriveAccountInformation() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(MainActivity.this, ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					MeepStoreLog.LogMsg( "AccountManager from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					return;
				}
				Account account = accountManager.getLoggedInAccountBlocking();
				if (account == null) {
					MeepStoreLog.LogMsg( "Account from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					return;
				}
				// store account information to preference
				mApp.setAccountInformation(account);
			}

		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		mApp.resetAccountInformation(this);
		ServiceManager.unbindServices(this);
	}

}
