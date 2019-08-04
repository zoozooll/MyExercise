package com.oregonscientific.meep.store2;

import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

import com.oregonscientific.meep.store2.adapter.ImageThreadLoader;
import com.oregonscientific.meep.store2.adapter.ListAdapterShelf;
import com.oregonscientific.meep.store2.adapter.ImageThreadLoader.ImageLoadedListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;

public class ScreenShotFragment extends DialogFragment {
	public static final int SCREENSHOT = 10;

	private MeepStoreApplication mApp;
	private static ArrayList<String> shots;
	private static int pictureIndex;

	public static int getPictureIndex() {
		return pictureIndex;
	}

	public static void setPictureIndex(int pictureIndex) {
		ScreenShotFragment.pictureIndex = pictureIndex;
	}

	private float touchDownX;
	private float touchUpX;
	String prefix;
	ImageSwitcher imageSwitcher;
//	ImageThreadLoader imageLoader;

	public interface FeedBackListener {
		public abstract void OnPopupFeedBack(String option, Object variable);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mApp = (MeepStoreApplication) getActivity().getApplicationContext();
	}

	public static ScreenShotFragment newInstance(int title, int index, ArrayList<String> screenshot) {
		ScreenShotFragment myDialogFragment = new ScreenShotFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		myDialogFragment.setArguments(bundle);
		if (shots != null && shots.size() > 0 && !shots.get(0).equals(screenshot.get(0))) {
			// shots.clear();
			shots = null;
		}
		shots = screenshot;
		
		pictureIndex = index;
		return myDialogFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		int args = getArguments().getInt("title");
		View v = null;
		switch (args) {
		case SCREENSHOT:
			v = inflater.inflate(R.layout.sreenshot, container, false);
			prefix = mApp.getLoginInfo().url_prefix;
//			imageLoader = mApp.getImageShotLoader();
			initSwitcher(v);
			createSreenshot(v);
			break;
		}
		return v;
	}


	public void createSreenshot(View v) {
		ImageButton btnClose = (ImageButton) v.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ScreenShotFragment.this.dismiss();
			}
		});
		loadImage();
	}

	public void loadImage() {
		// image

//		Bitmap cachedImage = null;
//		try {
//			cachedImage = imageLoader.loadImage(prefix + shots.get(pictureIndex), new ImageLoadedListener() {
//						public void imageLoaded(Bitmap imageBitmap) {
//							imageSwitcher.setInAnimation(null);
//							imageSwitcher.setOutAnimation(null);
//							imageSwitcher.setImageDrawable(new BitmapDrawable(mApp.getApplicationContext().getResources(), imageBitmap));
//						}
//					});
//		} catch (MalformedURLException e) {
//			Log.e("test", "Bad remote image URL: " + e.toString());
//		}
//
//		if (cachedImage != null) {
//			imageSwitcher.setImageDrawable(new BitmapDrawable(mApp.getApplicationContext().getResources(), cachedImage));
//		} else {
//			imageSwitcher.setImageResource(R.drawable.meep_store_image_dummy);
//		}
//		mApp.getImageDownloader().download(prefix + shots.get(pictureIndex), imageSwitcher);
		mApp.getImageDownloader().download(prefix + shots.get(pictureIndex), 200, 200, (ImageView) imageSwitcher.getNextView());
		imageSwitcher.showNext();
	}

	public void initSwitcher(View v) {
		imageSwitcher = (ImageSwitcher) v.findViewById(R.id.screenshots);
		imageSwitcher.setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				ImageView imageView = new ImageView(mApp.getApplicationContext());
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
						if (!shots.isEmpty()) {
							pictureIndex = pictureIndex == 0 ? shots.size() - 1 : pictureIndex - 1;
							imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
									mApp.getApplicationContext(), R.anim.slide_in_left_alpha));
							imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
									mApp.getApplicationContext(), R.anim.slide_out_right_alpha));
							loadImage();
						}
					} else if (touchDownX - touchUpX > 100) {
						if (!shots.isEmpty()) {
							pictureIndex = pictureIndex == shots.size() - 1 ? 0 : pictureIndex + 1;
							imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
									mApp.getApplicationContext(), R.anim.slide_in_right_alpha));
							imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
									mApp.getApplicationContext(), R.anim.slide_out_left_alpha));
							loadImage();
						}
					}
					return true;
				}
				return false;
			}
		});
    }
    
    
	@Override
	public void dismiss() {
		try {
			if(getActivity() instanceof GenericStoreActivity){
				ListAdapterShelf listAdapterShelf = ((GenericStoreActivity)getActivity()).getShelfListAdapter();
				listAdapterShelf.enableItemButton();
			}
			super.dismiss();
		} catch (Exception e) {
			MeepStoreLog.LogMsg( "current activity has been closed");
		}
	}

}