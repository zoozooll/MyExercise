package com.dvr.android.dvr.usbcamera;

import java.nio.ByteBuffer;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dvr.android.dvr.R;
import com.dvr.android.dvr.libucamera;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    protected Context context;
    private boolean usbCameraExists = false;
    private Rect rect;
    private Bitmap bmp, backWarningLine;
    private ByteBuffer mImageBuffer;
    private boolean isBack = false;// 是否倒车
    private boolean isPreSwitch = true;
    static final int IMG_WIDTH=640;
	static final int IMG_HEIGHT=480;

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context,attrs);
        //this.context = context;
        //setFocusable(true);
    }
    
    public void setCameraPreview(Context context) {
        this.context = context;
        setFocusable(true);
    }

    /**
     * 绘制线程
     * */
    class DrawImage extends Thread {
        @Override
        public void run() {
            while (usbCameraExists) {
                int width = CameraPreview.this.getWidth();
                int height = CameraPreview.this.getHeight();
                rect = new Rect(0, 0, width, height);
                
                libucamera.native_setbmp(bmp);
            	
           	    Canvas canvas = getHolder().lockCanvas();
                if (canvas != null)
                {
                	// draw camera bmp on canvas
                	canvas.drawBitmap(bmp,null,rect,null);
                	getHolder().unlockCanvasAndPost(canvas);
                }
                /*int nDateLength = BackCameraReadFrame();
                if (nDateLength > 0 && rect != null && isPreSwitch) {
                    Canvas canvas = getHolder().lockCanvas();
                    if (canvas != null) {
                        // bmp = BitmapFactory.decodeByteArray(mImageBuffer.array(), 0, nDateLength);
                        bmp = decodeSampledBitmapFromData(mImageBuffer.array(), nDateLength, width, height);
                        if (bmp != null) {
                            canvas.drawBitmap(bmp, null, rect, null);
                        }
                        //if (isBack) {
                        //    backWarningLine = decodeSampledBitmapFromResource(R.drawable.back_warning_line, width, height);
                        //    if (backWarningLine != null) {
                        //        canvas.drawBitmap(backWarningLine, null, rect, null);
                        //    }
                        //}
                        getHolder().unlockCanvasAndPost(canvas);
                    }
                }*/
            }
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromResource(int id, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), id);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

    private static Bitmap decodeSampledBitmapFromData(byte[] data, int nDateLength, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, nDateLength, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, nDateLength, options);
    }

    private void initImageBuffer() {
        if (CameraPreview.this.getWidth() == 800) {
            mImageBuffer = ByteBuffer.allocateDirect(640 * 480 * 3);
        } else {
            mImageBuffer = ByteBuffer.allocateDirect(640 * 480 * 3);
        }
        if (mImageBuffer != null) {
            libucamera.native_ucameracmd2(libucamera.JCMD_SETFRAMEBUF, "", 0, 0, 0, mImageBuffer);
        }
    }
    public int usbCameraInit(int p1) { Log.i("PLJ", "CameraPreview---->usbCameraInit:"+p1);
        int micro = libucamera.native_ucameracmd(libucamera.JCMD_INIT, "", p1, 0, 0);
        //if(micro != -3){
           usbCameraPreView();
        //}
		return micro;
    }

    public void usbCameraPreView(){
    	if(bmp==null){
			bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.RGB_565);
		}
    	libucamera.native_initBmp(bmp);
    	
    	usbCameraExists = true;
        libucamera.native_ucameracmd(libucamera.JCMD_PREVIEW, "", 0, 0, 0);
        initImageBuffer();
        try {
            new DrawImage().start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    public int BackCameraClose() {
        return libucamera.native_ucameracmd(libucamera.JCMD_CLOSE, "", 0, 0, 0);
    }

    public void BackCameraStopPrev() {
        libucamera.native_ucameracmd(libucamera.JCMD_STOPPREVIEW, "", 0, 0, 0);
    }

    public void BackCameraRecord(String savePath,int p1) { Log.i("PLJ", "CameraPreview---->BackCameraRecord:"+p1);
        libucamera.native_ucameracmd(libucamera.JCMD_REC, savePath, p1, 0, 0);
    }

    public void BackCameraStopRec(String savePath,int p1) {
        libucamera.native_ucameracmd(libucamera.JCMD_STOPREC, savePath, p1, 0, 0);
    }

    public int BackCameraReadFrame() {
        int nDateLength = libucamera.native_ucameracmd(libucamera.JCMD_READFRAME, "", 0, 0, 0);
        return nDateLength;
    }

    public void BackCameraSetTimeInfo(String time) {
        libucamera.native_ucameracmd(libucamera.JCMD_SETTIMEINFO, time, 0, 0, 0);
    }
    
    public void BackCameraSetGpsInfo(String gps) {
        libucamera.native_ucameracmd(libucamera.JCMD_SETGPSINFO, gps, 0, 0, 0);
    }
    
    public void FrontCameraSetResolution(int width,int height,int rate) {
    	libucamera.native_ucameracmd(libucamera.JCMD_SETRESOLUTION, "", width,height,rate);//320,240,15
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        usbCameraExists = true;
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        usbCameraExists = false;
        BackCameraStopPrev();
        BackCameraClose();
    }

    public void setBack(boolean isBack) {
        this.isBack = isBack;
    }
    
    public void setPreSwitch(boolean isPreSwitch) {
        this.isPreSwitch = isPreSwitch;
    }

}
