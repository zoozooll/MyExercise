package com.kang.pdfreader;

import java.util.ArrayList;
import java.util.Map;

import org.vudroid.CodecDao;

import com.iskyinfor.zoom.ImageZoomManager;
import com.iskyinfor.zoom.ImageZoomView;
import com.iskyinfor.zoom.SimpleZoomListener;
import com.iskyinfor.zoom.SimpleZoomListener.ControlType;
import com.iskyinfor.zoom.dao.BooksDao;
import com.iskyinfor.zoom.dao.DBHelper;
import com.iskyinfor.zoom.ZoomState;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 
 * 
 * @author Aaron Lee
 * 
 * @date 2011-7-5 上午11:31:49
 */
public class MyPdfReaderActivity extends Activity {

	public static final String AUTHOR_TAG = "aaron";

	private static final String[] scalings = { "25", "50", "75", "100",
			"150", "175", "200", "250" };
	
	

	/** screen's width */
	private int screenWidth;

	/** screen's height */
	private int screenHeight;

	private int indexPage;
	private int curScaling = 3;
	/** Constant used as menu item id for setting zoom control type */
	private static final int MENU_ID_ZOOM = 0;

	/** Constant used as menu item id for setting pan control type */
	private static final int MENU_ID_PAN = 1;

	/** Constant used as menu item id for resetting zoom state */
	private static final int MENU_ID_RESET = 2;

	/** Constant used as menu item id for turn to next page */
	private static final int MENU_ID_NEXTPAGE = 3;

	/** Constant used as menu item id for turn to prevent page */
	private static final int MENU_ID_PREVENTPAGE = 4;

	/** Constant used as menu item id for turn to prevent page */
	private static final int MENU_ID_BOOKMARK = 5;

	private static final String PDF_PATH = "/sdcard/book/代码之美.pdf";

	private int bookId;

	/** Zoom state */
	private ZoomState mZoomState;

	/** Decoded bitmap image */
	private Bitmap mBitmap;

	/** On touch listener for zoom view */
	private SimpleZoomListener mZoomListener;

	// 各类view

	private ImageZoomView izvw;
	private ViewFlipper vfpMainContent;
	private TextView tvwBookname;
	private TextView tvwPage;
	private TextView tvwScalingPercent;
	private SeekBar sbrContent;
	private Button btnPreventPage;
	private Button btnNextPage;

	/**
	 * 控制跳转页面的PopupWindow
	 */
	/*
	 * PopupWindow pwGotoPage;
	 *//**
	 * 控制放大缩小的PopupWindow
	 */
	/*
	 * PopupWindow pwScalingCtrl;
	 */

	private Dialog dialog;

	private CodecDao codecDao;

	/** 页数总数 */
	private int pageCount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initViews(); // 获取view的属性
		addEvent(); // 获取event:

		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		Log.i("view", "height:" + display.getHeight());
		Log.i("view", "width:" + display.getWidth());

		izvw = new ImageZoomView(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		mZoomState = new ZoomState();

		codecDao = new CodecDao();
		Map<String, Object> data = null;
		try {
			data = codecDao.searchAll(PDF_PATH, screenWidth, screenHeight);

		} catch (Exception e) {
			e.printStackTrace();
		}
		mBitmap = (Bitmap) data.get(ImageZoomManager.DOCUMENT_BITMAP);
		pageCount = (Integer) data.get(ImageZoomManager.DOCUMENT_COUNT);

		mZoomListener = new SimpleZoomListener();
		mZoomListener.setZoomState(mZoomState);

		izvw.setZoomState(mZoomState);
		izvw.setImage(mBitmap);
		izvw.setOnTouchListener(mZoomListener);
		vfpMainContent.addView(izvw, 0, params);
		resetZoomState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * menu.add(Menu.NONE, MENU_ID_ZOOM, 0, R.string.menu_zoom);
		 * menu.add(Menu.NONE, MENU_ID_PAN, 1, R.string.menu_pan);
		 */
		menu.add(Menu.NONE, MENU_ID_RESET, 2, R.string.menu_reset);
		menu.add(Menu.NONE, MENU_ID_NEXTPAGE, 3, R.string.menuNextpage);
		menu.add(Menu.NONE, MENU_ID_PREVENTPAGE, 4, R.string.menuPeventnpage);
		menu.add(Menu.NONE, MENU_ID_BOOKMARK, MENU_ID_BOOKMARK,
				R.string.menuBookmark);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * case MENU_ID_ZOOM: mZoomListener.setControlType(ControlType.ZOOM);
		 * break;
		 * 
		 * case MENU_ID_PAN: mZoomListener.setControlType(ControlType.PAN);
		 * break;
		 */

		case MENU_ID_RESET:
			resetZoomState();
			break;

		case MENU_ID_PREVENTPAGE:
			gotoPreventpage();
			break;

		case MENU_ID_NEXTPAGE:
			gotoNextPage();
			break;
		case MENU_ID_BOOKMARK:
			addBookMark(bookId, indexPage);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void initViews() {
		vfpMainContent = (ViewFlipper) findViewById(R.id.vfpMainContent);
		tvwBookname = (TextView) findViewById(R.id.tvwBookname);
		tvwPage = (TextView) findViewById(R.id.tvwPage);
		tvwScalingPercent = (TextView) findViewById(R.id.tvwScalingPercent);
		sbrContent = (SeekBar) findViewById(R.id.sbrContent);
		btnPreventPage = (Button) findViewById(R.id.btnPreventPage);
		btnNextPage = (Button) findViewById(R.id.btnNextPage);
	}

	private void addEvent() {
		MyPdfReaderOnClickListener onclickListener = new MyPdfReaderOnClickListener(
				this);
		tvwPage.setOnClickListener(onclickListener);
		tvwScalingPercent.setOnClickListener(onclickListener);
		btnPreventPage.setOnClickListener(onclickListener);
		btnNextPage.setOnClickListener(onclickListener);
	}

	void addBookMark(int bookId, int page) {
		BooksDao dao = BooksDao.getDao(this);
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.BOOK_ID, bookId);
		cv.put(DBHelper.PAGE, page);
		long millionTime = System.currentTimeMillis();
		cv.put(DBHelper.MARK_DATE, millionTime);
		dao.insert(DBHelper.TABLE_BOOK_MARK, cv);
	}

	void showBookMarks() {

	}
	
	void showScaling(float percent) {
		mZoomState.setZoom(percent);
		mZoomState.notifyObservers();
	}

	void gotoNextPage() {
		if (indexPage < pageCount - 1) {

			ImageZoomView izvw1 = new ImageZoomView(this);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			indexPage++;
			Bitmap mBitmap1 = null;
			try {
				mBitmap1 = codecDao.searchPage(PDF_PATH, screenWidth,
						screenHeight, indexPage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mZoomListener.setZoomState(mZoomState);
			izvw1.setZoomState(mZoomState);
			izvw1.setImage(mBitmap1);
			izvw1.setOnTouchListener(mZoomListener);
			Log.i(AUTHOR_TAG,
					"displayedIndexBeforeAdd:"
							+ vfpMainContent.getDisplayedChild());
			vfpMainContent.addView(izvw1, 1, params);
			Log.i(AUTHOR_TAG,
					"viewCountAfterAdd:" + vfpMainContent.getChildCount());
			vfpMainContent.showNext();
			Log.i(AUTHOR_TAG,
					"displayedIndexAfterAdd:"
							+ vfpMainContent.getDisplayedChild());
			resetZoomState();
			vfpMainContent.removeViewAt(0);
			Log.i(AUTHOR_TAG,
					"viewCountAfterRemove" + vfpMainContent.getChildCount());
			izvw.clear();
			izvw = izvw1;
			Log.i(AUTHOR_TAG,
					"displayedIndexAfterRemove:"
							+ vfpMainContent.getDisplayedChild());
			Log.i(AUTHOR_TAG, "page:" + indexPage);
		}

	}

	void gotoPreventpage() {
		if (indexPage > 0) {
			ImageZoomView izvw1 = new ImageZoomView(this);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			indexPage--;
			Bitmap mBitmap2 = null;
			try {
				mBitmap2 = codecDao.searchPage(PDF_PATH, screenWidth,
						screenHeight, indexPage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mZoomListener.setZoomState(mZoomState);
			izvw1.setZoomState(mZoomState);
			izvw1.setImage(mBitmap2);
			izvw1.setOnTouchListener(mZoomListener);
			Log.i(AUTHOR_TAG,
					"displayedIndexBeforeAdd:"
							+ vfpMainContent.getDisplayedChild());
			vfpMainContent.addView(izvw1, 0, params);
			Log.i(AUTHOR_TAG,
					"viewCountAfterAdd:" + vfpMainContent.getChildCount());
			vfpMainContent.showPrevious();
			Log.i(AUTHOR_TAG,
					"displayedIndexAfterAdd:"
							+ vfpMainContent.getDisplayedChild());
			resetZoomState();
			vfpMainContent.removeViewAt(1);
			Log.i(AUTHOR_TAG,
					"viewCountAfterRemove:" + vfpMainContent.getChildCount());
			izvw.clear();
			izvw = izvw1;
			Log.i(AUTHOR_TAG,
					"displayedIndexAfterRemove:"
							+ vfpMainContent.getDisplayedChild());
			Log.i(AUTHOR_TAG, "page:" + indexPage);
		}

	}

	void gotoPageContent(int page) {
		if (page >= 0 && page < pageCount && page != indexPage) {
			ImageZoomView izvw1 = new ImageZoomView(this);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			indexPage = page;
			Bitmap mBitmap2 = null;
			try {
				mBitmap2 = codecDao.searchPage(PDF_PATH, screenWidth,
						screenHeight, page);
			} catch (Exception e) {
				e.printStackTrace();
			}
			mZoomListener.setZoomState(mZoomState);
			izvw1.setZoomState(mZoomState);
			izvw1.setImage(mBitmap2);
			izvw1.setOnTouchListener(mZoomListener);
			Log.i(AUTHOR_TAG,
					"displayedIndexBeforeAdd:"
							+ vfpMainContent.getDisplayedChild());
			vfpMainContent.addView(izvw1, 1, params);
			Log.i(AUTHOR_TAG,
					"viewCountAfterAdd:" + vfpMainContent.getChildCount());
			vfpMainContent.showPrevious();
			Log.i(AUTHOR_TAG,
					"displayedIndexAfterAdd:"
							+ vfpMainContent.getDisplayedChild());
			resetZoomState();
			vfpMainContent.removeViewAt(0);
			Log.i(AUTHOR_TAG,
					"viewCountAfterRemove:" + vfpMainContent.getChildCount());
			izvw.clear();
			izvw = izvw1;
			Log.i(AUTHOR_TAG,
					"displayedIndexAfterRemove:"
							+ vfpMainContent.getDisplayedChild());
			Log.i(AUTHOR_TAG, "page:" + indexPage);
		}
	}

	/**
	 * Reset zoom state and notify observers We shold make all the protected to
	 * the begin state
	 */
	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}

	void showPageDialog() {
		OnClickListener listener = new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					EditText etxPage = (EditText) ((Dialog) dialog)
							.findViewById(R.id.etxPage);
					try {

						int page = Integer.parseInt(etxPage.getText()
								.toString());
						gotoPageContent(page);
					} catch (NumberFormatException e) {
						Toast.makeText(MyPdfReaderActivity.this, "请输入正确的数字",
								Toast.LENGTH_SHORT).show();
						Log.w("dialog_gotoPage", e);
					}
					dialog.cancel();
					break;

				default:
					dialog.cancel();
					break;
				}
			}
		};

		View v = LayoutInflater.from(this).inflate(
				R.layout.dg_content_gotopage, null);
		dialog = new AlertDialog.Builder(this).setTitle("跳转").setView(v)
				.setNegativeButton("取消", listener)
				.setPositiveButton("确定", listener).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	void gotoShowScalingDialog() {

		ListView lvwScaling = (ListView) LayoutInflater.from(this).inflate(R.layout.lvw_scaling, null);
		ListAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, scalings);
		lvwScaling.setAdapter(adapter);
		lvwScaling.setSelected(true);
		lvwScaling.setItemChecked(curScaling, true);
		
		OnItemClickListener listener = new OnItemClickListener(){


			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				float scalingPercent =Float.parseFloat(scalings[position])/100.0f;
				showScaling(scalingPercent);
				curScaling = position;
				dialog.dismiss();
				
			}
		};
		lvwScaling.setOnItemClickListener(listener);
		
		dialog = new  AlertDialog.Builder(this)
					.setTitle("设置大小")
					.setView(lvwScaling)
					.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		lvwScaling.setSelection(3);
	}

}