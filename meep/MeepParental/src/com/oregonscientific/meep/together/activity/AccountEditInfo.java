package com.oregonscientific.meep.together.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;

public class AccountEditInfo extends AccountGeneral {

	ImageButton barLeftAccountEditBack;
	MyProgressDialog loading;
	TextView title;
	TextView label;
	String text;
	ImageButton usericon;

	private static final int SELECT_PICTURE = 0;
	protected boolean needUploadImage = false;
	protected Bitmap uploadImage;

	protected void initEdit() {
		title = (TextView) findViewById(R.id.editTitle);
		label = (TextView) findViewById(R.id.editLabel);
		title.setText(String.format(this.getResources().getString(R.string.main_page_account_manage_edit), text));
		label.setText(String.format(this.getResources().getString(R.string.main_page_account_manage_edit_label), text));
		barLeftAccountEditBack = (ImageButton) findViewById(R.id.barImageButtonBack);
		barLeftAccountEditBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		usericon = (ImageButton) findViewById(R.id.user_icon);
		usericon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPicture();
			}
		});
		loading = new MyProgressDialog(this);
		loading.setMessage(this.getResources().getString(R.string.loading_text));
		needUploadImage = false;
	}

	protected void selectPicture() {
		//default android
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);
		
		//customised activity
//		Intent intent = new Intent();
//		intent.setClass(getApplicationContext(), ImagePickerActivity.class);
//		startActivityForResult(intent,SELECT_PICTURE);
		
		
		//copy 
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_PICK);
//		try {
//			startActivityForResult(intent, SELECT_PICTURE);
//		} catch (Exception e) {
//			Log.e("test",e.getMessage());
//		}
	}
	
	

	protected void onActivityResult(int requestCode, int resultCode, 
  	       Intent returnIntent) {
  	    super.onActivityResult(requestCode, resultCode, returnIntent); 

  	    switch(requestCode) { 
  	    case SELECT_PICTURE:
  	        if(resultCode == RESULT_OK){ 
  	        	//default android
  	            Uri selectedImage = returnIntent.getData();
  	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

  	            Cursor cursor = getContentResolver().query(
  	                               selectedImage, filePathColumn, null, null, null);
  	            cursor.moveToFirst();

  	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
  	            String filePath = cursor.getString(columnIndex);
  	            cursor.close();
  	        	
  	        	//customised activity
//  	        	String filePath = returnIntent.getStringExtra("path");
//
//  	            Bitmap yourSelectedImage = decodeFile(filePath);
//            	needUploadImage = true;
//            	uploadImage = yourSelectedImage;
//            	//TODO:delete
//            	usericon.setImageBitmap(uploadImage);
  	        	
  	            //copy
//  	        	if (returnIntent == null) {
//					return;
//				}		
//				Uri uri = returnIntent.getData();
//				
//				if (uri == null) {
//					return;
//				}
//				String filePath = getRealPathFromURI(uri);
				Bitmap yourSelectedImage = decodeFile(filePath);
            	needUploadImage = true;
            	uploadImage = yourSelectedImage;
            	usericon.setImageBitmap(uploadImage);
  	        }
  	    };
  	            
    }
	
	public String getRealPathFromURI(Uri contentUri) {
		String result;
		try {
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			result = cursor.getString(columnIndex);
			cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("test", "Cannot retrieve path from " + contentUri
					+ " because " + e);
			result = null;
		}

		return result;
	}

	private Bitmap decodeFile(String f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 300;
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
		int newIconWidth = 300;
		int newIconHeight = 300;
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
	
	
	
}
