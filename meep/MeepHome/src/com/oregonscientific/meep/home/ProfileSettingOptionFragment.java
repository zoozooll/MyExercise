/**
 * Copyright (C) 2013 IDT International Ltd.
 */

package com.oregonscientific.meep.home;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.oregonscientific.meep.MEEPEnvironment;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.app.DialogFragment;
import com.oregonscientific.meep.app.DialogInterface;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.widget.StrokedTextView;

/**
 * A Fragment for handle the view action and will show after selected change
 * profile picture or wallpaper
 * 
 */
public class ProfileSettingOptionFragment extends Fragment {

	public static final int REQUEST_PICK_PHOTO = 0;
	public static final int REQUEST_TAKE_PHOTO = 1;
	
	public final static int TYPE_PROFILE_PICTURE = 0;
	public final static int TYPE_WALLPAPER = 1;
	
	public static final String TAG_PROFILE_PICTURE = "profile_picture";
	public static final String TAG_WALLPAPER = "wallpaper";
	
	public final static String KEY_TYPE = "type";
	
	private final String DIRECTORY_CAMERA = "Camera";
	
	private Uri outputFileUri;
	private String TAG = getClass().getSimpleName();
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			
			Bundle bundle = getArguments();
			if(bundle == null) {
				return;
			}
			int type = bundle.getInt(KEY_TYPE);
			switch(requestCode) {
				case REQUEST_PICK_PHOTO:
					if (data == null) {
						return;
					}		
					Uri uri = data.getData();
					
					if (uri == null) {
						return;
					}
					
					handleResult(type, uri, requestCode);
					break;
				case REQUEST_TAKE_PHOTO:
					if (outputFileUri == null) {
						return;
					}
					handleResult(type, outputFileUri, requestCode);
					break;
			}
			getActivity().setResult(resultCode);

		}

	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.profile_setting_option, container, false);
		Bundle bundle = getArguments();
		if (bundle == null) {
			return null;
		}
		int type = bundle.getInt(KEY_TYPE);
		
		String title = null;
		int color = Color.WHITE;
		
		if (type == TYPE_PROFILE_PICTURE) {
			// Change the skin to type of profile picture  
			v.setBackgroundResource(R.drawable.profile_slip_in1);
			title = getString(R.string.profile_setting_option_title2);
			color = getActivity().getResources().getColor(R.color.dark_yellow);
		} else if (type == TYPE_WALLPAPER) {
			// Change the skin to type of wallpaper 
			v.setBackgroundResource(R.drawable.profile_slip_in2);
			title = getString(R.string.profile_setting_option_title);
			color = getActivity().getResources().getColor(R.color.shadow_blue);
		}
		
		if (title != null) {
			StrokedTextView textView = (StrokedTextView) v.findViewById(R.id.options_title);
			if (textView != null) {
				textView.setText(title);
				textView.setStrokeColor(color);
			}

		}
		
		v.findViewById(R.id.options_pick_photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_PICK);
				try {
					startActivityForResult(intent, REQUEST_PICK_PHOTO);
				} catch (Exception e) {
					Log.e(TAG,e.getMessage());
				}
				
			}
			
		});
		
		v.findViewById(R.id.options_camera).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				try {
					File cameraDirectory = new File(MEEPEnvironment.getStoragePublicDirectory(MEEPEnvironment.DIRECTORY_DCIM), DIRECTORY_CAMERA);
					if (!cameraDirectory.exists()) {
						cameraDirectory.mkdirs();
					}
					
					File tmpFile = new File(cameraDirectory, UUID.randomUUID().toString() + ".jpg");
					outputFileUri = Uri.fromFile(tmpFile);
					tmpFile.createNewFile();
					
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					startActivityForResult(intent, REQUEST_TAKE_PHOTO);
				} catch (Exception e) {
					Log.e(TAG, "Cannot take image with camera because " + e);
				}
			}
			
		});		
		
		return v;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	
	/**
	 * 
	 * @param type
	 * @param path
	 */
	private void handleResult(int type, Uri uri, int requestCode) {
		switch(type) {
			case TYPE_PROFILE_PICTURE:
				setProfilePicture(uri, requestCode);
				break;
			case TYPE_WALLPAPER:
				setWallpaper(uri, requestCode);
				DialogFragment dialog = DialogFragment.newInstance(
						getString(R.string.title_notice), 
						getString(R.string.alert_profile_wallpaper_updated));
				dialog.setPositiveButton(getString(R.string.alert_button_ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogFragment dialog, int which) {
						dialog.dismiss();
					}
					
				});
				dialog.show(getFragmentManager());
				break;
				
		}
	}
	
	/**
	 * 
	 * @param path
	 */
	private void setProfilePicture(final Uri uri, int requestCode) {
		String path = null;
		if (requestCode == REQUEST_PICK_PHOTO) {
			path = getRealPathFromURI(uri);
		} else if (requestCode == REQUEST_TAKE_PHOTO) {
			path = uri.getPath();
		} else {
			return;
		}
		
		if (path != null) {
			// Cannot continue if the image file referenced does not exist
			final File file = new File(path);
			if (!file.exists()) {
				return;
			}
			
			// Cannot continue if can not find the ProfileSettingFragment
			ProfileSettingFragment fragment = (ProfileSettingFragment) getActivity().getFragmentManager().findFragmentById(R.id.left_fragment_wrapper);
			if (fragment != null) {
				fragment.showProgressBar(true);
			}
			
			ExecutorService service = Executors.newSingleThreadExecutor();
			service.execute(new Runnable() {

				@Override
				public void run() {
					// Update account profile image
					AccountManager accountManager = (AccountManager)ServiceManager.getService(getActivity(), ServiceManager.ACCOUNT_SERVICE);
					Account account = accountManager.getLastLoggedInAccountBlocking();
					
					// We cannot proceed if the {@link Account} is not found.
					if (account == null) {
						return;
					}
					account.setIconAddress(Uri.fromFile(file).toString());
					accountManager.updateUserAccount(account);
				}
				
			});
		}
	}
	
	/**
	 * 
	 * @param path the file path of image be set be wallpaper
	 */
	private void setWallpaper(Uri uri, int requestCode) {
		WallpaperManager wallPaperManager = WallpaperManager.getInstance(getActivity());
		Point point = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(point);
		String path = null;
		if (requestCode == REQUEST_PICK_PHOTO) {
			path = getRealPathFromURI(uri);
		} else if(requestCode == REQUEST_TAKE_PHOTO) {
			path = uri.getPath();
		} else {
			return;
		}
		try {
			//the camera intent will not pass the intent come back, need to define the path and pass it before launch camera intent 
			File file = new File(path);
			Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(
					file, point.x, point.y);
			
			if (bitmap == null) {
				return;
			}
			wallPaperManager.suggestDesiredDimensions(point.x, point.y);
			wallPaperManager.setBitmap(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get real file path with using the uri 
	 * @param contentUri the file uri
	 * @return a String of file path
	 */
	public String getRealPathFromURI(Uri contentUri) {
		String result;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			CursorLoader cursorLoader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
			Cursor cursor = cursorLoader.loadInBackground();
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(columnIndex);
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Cannot retrieve path from " + contentUri + " because " + e);
			result = null;
		}
		
		return result;
	}

	/**
	 * @return the outputFileUri
	 */
	public Uri getOutputFileUri() {
		return outputFileUri;
	}

	/**
	 * @param outputFileUri the outputFileUri to set
	 */
	public void setOutputFileUri(Uri outputFileUri) {
		this.outputFileUri = outputFileUri;
	}

}
