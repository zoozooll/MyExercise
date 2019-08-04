package com.oregonscientific.meep.together.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.together.R;

public class ImagePickerActivity extends Activity implements
		OnItemClickListener{

	private Context mContext = null;
	private Gallery gallery;
	private ImageAdapter imageAdapter;
	private List<Bitmap> list;
	private ArrayList<String> listPath;
//	MyProgressDialog loading;
	private int count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_picker);
//		loading = UserFunction.initLoading(this);
		mContext = this;
		
//		loading.show();
		
		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setOnItemClickListener(ImagePickerActivity.this);
		imageAdapter = new ImageAdapter(ImagePickerActivity.this);
		gallery.setAdapter(imageAdapter);
		new LoadImageList().execute();
		
	}
	
	
	

	public class ImageAdapter extends BaseAdapter {
		private Context mContext;

		public ImageAdapter(Context context) {
			mContext = context;
			
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(list.get(position % list.size()));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
//			imageView.setBackgroundResource(mGalleryItemBackground);
			imageView.setBackgroundResource(R.drawable.image_stroke_old);
			return imageView;
		}
	}
	
	
	private void getBitmap() {
		List <File> fileList = MediaManager.getAllImagesFiles();
		list = new ArrayList<Bitmap>();
		list = Collections.synchronizedList(list); ;
		listPath = new ArrayList<String>();
		for (int j = 0; j < fileList.size(); j++) {
			Bitmap newBmp = decodeFile(fileList.get(j).getAbsolutePath());
			list.add(newBmp);
			listPath.add(fileList.get(j).getAbsolutePath());
			count++;
			Utils.printLogcatDebugMessage(newBmp.getByteCount()+"");
			mHandler.sendEmptyMessage(1);
		}
//		mHandler.sendEmptyMessage(0);
	}
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
//			case 0:
//				loading.dismiss();
//				break;
			case 1:
				imageAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.putExtra("path", listPath.get(arg2%list.size()));
		setResult(RESULT_OK,intent);
		finish();
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private Bitmap decodeFile(String f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 100;
			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return fixSize(BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2));
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public Bitmap fixSize(Bitmap bitmapAPIcon) {
		int iconWidth = 0;
		int iconHeight = 0;
		int newIconWidth = 100;
		int newIconHeight = 100;
		iconWidth = bitmapAPIcon.getWidth();
		iconHeight = bitmapAPIcon.getHeight();
		float scaleIconWidth = ((float) newIconWidth) / iconWidth;
		float scaleIconHeight = ((float) newIconHeight) / iconHeight;
		Matrix iconMatrix = new Matrix();
		iconMatrix.postScale(scaleIconWidth, scaleIconHeight);
		Bitmap finalAPicon = Bitmap.createBitmap(bitmapAPIcon, 0, 0, iconWidth,
				iconHeight, iconMatrix, true);

		return finalAPicon;
	}
	
	private class LoadImageList extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... v) {
			try {
				getBitmap();
				Utils.printLogcatDebugMessage("test");
			} catch (Exception e) {
			}
			return null;
		}

		protected void onPostExecute() {
		}
	}
}
