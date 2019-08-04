package com.oregonscientific.meep.browser.ui.fragment;

import java.io.File;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.browser.BrowserUtility;
import com.oregonscientific.meep.browser.Consts;
import com.oregonscientific.meep.browser.R;
import com.oregonscientific.meep.browser.WebBrowserActivity;
import com.oregonscientific.meep.util.BitmapUtils;

public class MenuRecentlyFragment extends Fragment {

	public static final String TAG = "MenuRecentlyFragment";
	ImageView recently_image;
	TextView recently_url;
	View content;
	Bitmap bitmap;

	public static MenuRecentlyFragment newInstance() {
		Log.d(TAG, "new Instance");
		// create a new content fragment
		MenuRecentlyFragment f = new MenuRecentlyFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d(TAG, "onCreateView");

		View view = inflater.inflate(R.layout.main_menu_recently, container, false);
		// ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewpager);
		//
		// CursorPagerAdapter<Fragment> pagerAdapter = new
		// CursorPagerAdapter<Fragment>(this.getFragmentManager(),Fragment.class,((WebBrowserActivity)
		// getActivity()).getHistoryCursor());
		//
		// viewpager.setAdapter(pagerAdapter);
		recently_image = (ImageView) view.findViewById(R.id.recently_image);
		recently_url = (TextView) view.findViewById(R.id.recently_url);
		content = view.findViewById(R.id.content);

		// recently viewed
		displayRecently();
		return view;
	}

	public void displayRecently() {
		String prefix = getActivity().getApplicationContext().getFilesDir().getAbsolutePath();
		File imageFile = new File(prefix + Consts.PATH_RECENTLY_PICTURE);
		if (imageFile != null && imageFile.exists()) {
			releaseBitmap();
			bitmap = BitmapUtils.decodeSampledBitmapFromFile(imageFile, 800, 444);
			final String text = BrowserUtility.readFile(prefix
					+ Consts.PATH_RECENTLY_TEXT);
			recently_image.setImageBitmap(bitmap);
			recently_url.setText(text);
			content.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((WebBrowserActivity) getActivity()).startBrowserWebsiteTask(text, Boolean.TRUE.toString());
				}
			});
		}
	}

	public void releaseBitmap() {
		if (bitmap != null)
			bitmap.recycle();
		bitmap = null;
	}

}
