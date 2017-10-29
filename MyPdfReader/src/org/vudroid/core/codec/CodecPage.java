package org.vudroid.core.codec;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Parcelable;

public interface CodecPage
{
    boolean isDecoding();

    void waitForDecode();

    int getWidth();

    int getHeight();

    Bitmap renderBitmap(int width, int height, RectF pageSliceBounds);

    void recycle();
}
