package com.beem.project.btf.bbs.view.gif;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;

public class GifDrawalbe extends AnimationDrawable {
	private InputStream gifStream;
	private GifHelper helper;
	private int gifCount;

	public GifDrawalbe(Context context, int id) {
		helper = new GifHelper();
		gifStream = context.getResources().openRawResource(id);
		helper.read(gifStream);
		try {
			gifStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		gifCount = helper.getFrameCount();
		//		//LogUtils.i("======gifCount" + gifCount);
		if (gifCount <= 0) {
			return;
		}
		BitmapDrawable bd = new BitmapDrawable(null, helper.getImage());
		addFrame(bd, helper.getDelay(0));
		for (int i = 1; i < helper.getFrameCount(); i++) {
			addFrame(new BitmapDrawable(null, helper.nextBitmap()),
					helper.getDelay(i));
		}
		setBounds(0, 0, helper.getImage().getWidth(), helper.getImage()
				.getHeight());
		bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());
		invalidateSelf();
	}
	public boolean isGifDrawable() {
		return gifCount <= 0 ? false : true;
	}
}
