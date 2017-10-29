package com.dvr.android.dvr.mshowplayback;

import android.graphics.Bitmap;

public interface OnImageLoadListener
{
    public void onImageGetSuccess(Bitmap bitmap, long id, int type);
}
