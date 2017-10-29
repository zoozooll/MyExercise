package com.xianle.traffic_sh;

import java.io.File;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private ArrayList<RowModel> directoryEntries = new ArrayList<RowModel>();
	TextView tv;
	private DataDownloader downloader = null;
	private File currentDirectory;
	private ProgressDialog mDialog;
	
	// because the zip lib doesn't support chinese file name 
	// so, we have to define a hashtable to map english file name to chinese file name 
	final  Hashtable<String, String> mFileName = new Hashtable<String, String>();
	
	
	
	private void initFileNameMap() {
		mFileName.put("shanghai", this.getResources().getString(R.string.shanghai));
		mFileName.put("beijing", this.getResources().getString(R.string.beijing));
		mFileName.put("guangzhou", this.getResources().getString(R.string.guangzhou));
		mFileName.put("shenzhen", this.getResources().getString(R.string.shenzhen));
		mFileName.put("chengdu", this.getResources().getString(R.string.chengdu));
		mFileName.put("fuzhou", this.getResources().getString(R.string.fuzhou));
		mFileName.put("hefei", this.getResources().getString(R.string.hefei));
		mFileName.put("wuhan", this.getResources().getString(R.string.wuhan));
		mFileName.put("zhixiashi", this.getResources().getString(R.string.zhixiashi));
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		initFileNameMap();
		unzip();
		// browseToRoot();
	}

	protected void OnPause() {
		super.onPause();
		if (downloader != null) {
			synchronized (downloader) {
				downloader.setStatusField(null);
			}
		}
	}

	protected void OnResume() {
		super.onResume();
		if (downloader != null) {
			synchronized (downloader) {
				downloader.setStatusField(tv);
				// if( downloader.DownloadComplete )
				// initSDL();
			}
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	boolean result = true;
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		if(currentDirectory.getName().equals("busline")) {
    			finish();
    		} else {
    			browseTo(currentDirectory.getParentFile(),0);
    		}
    	} 
        return result;
    }
	

	/**
	 * move the file in asset to directory /sdcard/busline
	 */
	private void unzip() {
		Log.v(Globals.TAG, "start unzip file or download file");
		tv = new TextView(this);
		class CallBack implements Runnable {
			public MainActivity mParent;

			public void run() {
				if (mParent.downloader == null)
					mParent.downloader = new DataDownloader(mParent, tv);
			}
		}
		CallBack cb = new CallBack();
		cb.mParent = this;
		this.runOnUiThread(cb);
		mDialog = CreateDialog();
		mDialog.show();
	}
	
	protected ProgressDialog CreateDialog() {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog
				.setMessage(this.getResources().getString(
						R.string.pregress_diag));
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}

	private void browseTo(final File aDirectory, final long id) {
		if (aDirectory.isDirectory()) {
			this.currentDirectory = aDirectory;
			fill(aDirectory.listFiles());
		} else {
			DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
				// @Override
				public void onClick(DialogInterface arg0, int arg1) {
					try {
						InputStream checkFile = null;
						try {
							Intent in = new Intent(MainActivity.this,
									Traffic.class);
							in.putExtra(Globals.FILENAME, aDirectory.getPath());
							in.putExtra(Globals.Title, directoryEntries.get((int)id).mChineseName);
							MainActivity.this.startActivity(in);
						} catch (Exception e) {
							Context context = getApplicationContext();
							CharSequence text = MainActivity.this
									.getResources().getString(
											R.string.diag_err);
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text,
									duration);
							toast.show();
						}
						;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			};
			DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
				// @Override
				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss();
				}
			};
			AlertDialog ad = new AlertDialog.Builder(this).setMessage(
					R.string.diag_msg).setPositiveButton(android.R.string.ok,
					okButtonListener).setNegativeButton(
					android.R.string.cancel, cancelButtonListener).create();
			ad.show();
		}
	}

	private void fill(File[] files) {
		this.directoryEntries.clear();
		
		int type = 0;
		for (File file : files) {
			if (!file.getName().endsWith(".txt") && !file.isDirectory())
				continue;
			final String name;
			if (!file.isDirectory()) {
				name = file.getName().substring(0,
						file.getName().lastIndexOf('.'));
				type = 0;
			} else {
				type = 1;
				name = file.getName();
			}
			this.directoryEntries.add(new RowModel(type, name));
		}

		Comparator<RowModel> cmp = new ChinsesCharComp();
		Collections.sort(directoryEntries, cmp);
	
		IconAdapter directoryList = new IconAdapter(directoryEntries);

		this.setListAdapter(directoryList);
	}

	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		File clickedFile = null;
	
		if(this.directoryEntries.get(position).mRowtype == 1) {
			
			Log.v(Globals.TAG, "is a directory");
			clickedFile = new File(this.currentDirectory.getAbsolutePath()
					+ File.separator + this.directoryEntries.get(position).mLabel);
			this.browseTo(clickedFile, id);
			return;
		} 
		clickedFile = new File(this.currentDirectory.getAbsolutePath()
				+ File.separator + this.directoryEntries.get(position).mLabel + ".txt");
		
		try {
			if (clickedFile != null && clickedFile.isFile())
				this.browseTo(clickedFile, id);
				
		} catch (Exception e) {
			//don't throw
		}
		
	}
	class IconAdapter extends ArrayAdapter<RowModel> {
		
		IconAdapter(List<RowModel> _items) {
		    super(MainActivity.this, R.layout.row, _items);  
	    }
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.row, parent, false);
			}
			TextView tv= (TextView)row.findViewById(R.id.label);
			tv.setText(directoryEntries.get(position).mChineseName);
			ImageView iv = (ImageView)row.findViewById(R.id.icon);
			if(directoryEntries.get(position).mRowtype == 0) {
				iv.setImageResource(R.drawable.alert_dialog_icon);
			} else if (directoryEntries.get(position).mRowtype == 1) {
				iv.setImageResource(R.drawable.icon);
			}
			return row;
		}
		
	}
	
	class ChinsesCharComp implements Comparator<RowModel> {

		public int compare(RowModel o1, RowModel o2) {
			String c1 = (String) o1.mChineseName;
			String c2 = (String) o2.mChineseName;
			Collator myCollator = Collator.getInstance(java.util.Locale.CHINA);
			if (myCollator.compare(c1, c2) < 0)
				return -1;
			else if (myCollator.compare(c1, c2) > 0)
				return 1;
			else
				return 0;
		}
	}

	public void getFileList() {
		mDialog.dismiss();
		browseTo(new File(Globals.DataDir),0);
	}
	
	class RowModel {
		int mRowtype; //0:file 1:directory
		String mLabel;
		String mChineseName;
		
		RowModel(int type, String label) {
			mRowtype = type;
			mChineseName = mLabel = label;
			String temp = mFileName.get(label);
			if (temp != null) {
				mChineseName = temp;
			}
		}
		public String toString() {
			return mChineseName;
		}
		
	}
}
