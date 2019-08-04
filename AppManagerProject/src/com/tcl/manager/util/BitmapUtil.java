package com.tcl.manager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtil {
    public static Bitmap zoomBitmap(Context context, Bitmap bitmap, float rate) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(rate, rate);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return bmp;
    }
}
