package com.oregonscientific.meep.opengl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ThumbnailUtils;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;

import com.oregonscientific.meep.global.Global;

public class MediaManager {
	
	public MediaManager()
	{
		mMediaItemList = new ArrayList<MediaItem>();
	}

	private String[] getFilesOfDirectory(String folder) {
		File file = new File(Environment.getExternalStorageDirectory(), folder);
		if (file.exists()) {
			if (file.isDirectory()) {
				return file.list();
			}
		}
		return null;
	}
	
	private String getBitmapFileAddr(String addr)
	{
		File file = new File(Environment.getExternalStorageDirectory(), addr);
		if(file.exists())
		{
			return file.getAbsolutePath();
		}
		else
		{
			return "";
		}
	}
	
	public enum MediaType
	{
		Movie,
		Audio
	}
	
	public void LoadMedia(MediaType mediaType)
	{
		mMediaItemList.clear();
		String[] files = getFilesOfDirectory(mediaType.toString());
		for(int i =0; i<files.length; i++)
		{
			if(files[i].contains(".3gp"))
			{
				MediaItem item = new MediaItem();
				String name = files[i].substring(0, files[i].length()-4);
				item.setName(name);
				item.setImageAddr(getBitmapFileAddr(mediaType.toString() + File.separator + name + ".png"));
				item.setPath(Environment.getExternalStorageDirectory() +File.separator+ mediaType.toString() + File.separator+files[i]);
				item.setAngle(INIT_ANGLE + SEP_ANGLE * mMediaItemList.size());
				item.setTransform(new ESTransform());
				mMediaItemList.add(item);
			}
			
			if(files[i].contains(".mp3"))
			{
				MediaItem item = new MediaItem();
				String name = files[i].substring(0, files[i].length()-4);
				item.setName(name);
				item.setImageAddr(getBitmapFileAddr(mediaType.toString() + File.separator + name + ".png"));
				item.setPath(Environment.getExternalStorageDirectory() +File.separator+ mediaType.toString() + File.separator+files[i]);
				item.setAngle(INIT_ANGLE + SEP_ANGLE * mMediaItemList.size());
				item.setTransform(new ESTransform());
				mMediaItemList.add(item);
			}
		}
	}
	public static Bitmap LoadBitmapFile(String path) throws Exception{
		FileInputStream in;
		BufferedInputStream buf;
		try {
			//in = new FileInputStream("/sdcard/test2.png");
			in = new FileInputStream(path);
			buf = new BufferedInputStream(in);
			byte[] bMapArray = new byte[buf.available()];
			buf.read(bMapArray);
			BitmapFactory.Options bmOption = new BitmapFactory.Options();
	        bmOption.inPreferredConfig = Config.ARGB_8888;
	        bmOption.inSampleSize = 2;
			Bitmap bMap = BitmapFactory.decodeByteArray(bMapArray, 0,
					bMapArray.length,bmOption);
			if (in != null) {
				in.close();
			}
			if (buf != null) {
				buf.close();
			}
			return bMap;
		} catch (Exception e) {
			Log.e("Error reading file", e.toString());
		}
		return null;
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	static int[] genTextureIds(int[] ids) {
		GLES20.glGenTextures(ids.length, ids, 0);
		return ids;
	}
	
	public static int[] combine(int[] a, int[] b)
	{
		int alen = a.length;
		int blen = b.length;
		int clen = alen+blen;
	
		int[] c = new int[clen];
		System.arraycopy(a,0,c,0,alen);
		System.arraycopy(b,0,c,alen,blen);
	
		return c;
	}
	
	///
    //  Load texture from resource
    //
    public static int loadTexture ( Bitmap bitmap )
    {
    	//long time1 = System.currentTimeMillis();
        int[] textureId = new int[1];
            
        GLES20.glGenTextures ( 1, textureId, 0 );
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] );
    
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
        
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        
        try{
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        
       // Log.d("bitmap", "load bitmap:" + bitmap + " return :" + textureId[0]);
//        long timediff = System.currentTimeMillis() - time1;
//        
//        Log.d("performance", "load texture:" + timediff);
        
        return textureId[0];
    }
    
    /**
     * bind texture with a general texture id, and draw the texture
     * @author aaronli
     * @since Mar 12,2013
     * @param textureId
     * @param b
     */
    public static void loadSingleTexImage(int textureId, Bitmap b) {
    	//long time1 = System.currentTimeMillis();
    	GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId );
    	GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
		GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
		GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
		GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		try{
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		//long timediff = System.currentTimeMillis() - time1;
//     //  
       //Log.d("performance", "load texture: " + textureId + "     " + timediff);
    	
    }
    
    public static Bitmap loadProperImage(String path, int reqWidth, int reqHeight)
    {
    	 // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
    
    
    public static void releaseTexture(int id)
    {
    	 GLES20.glDeleteTextures(1, IntBuffer.wrap(new int[] {id}));
    }
    public static int getTitleTextureId(String text, int fontSize, int color, Align align)
    {
    	return getTextureIdByText(text, fontSize, color, 550, 50,0,35, align);
    }
    
    public static int getTitleTextureId(String text, int fontSize, int color)
    {
    	return getTextureIdByText(text, fontSize, color, 550, 50,0,35);
    }
    
    public static int getIconTextureId(String text, int fontSize, int color)
    {
    	return getTextureIdByText(text, fontSize, color, 128, 128, 10,115);
    }
    
    private static int getTextureIdByText(String text, int fontSize, int color, int w, int h, int x, int y,Align align )
    {
    	//long time1 = System.currentTimeMillis();
    	
    	// Create an empty, mutable bitmap
    	Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    	// get a canvas to paint over the bitmap
    	Canvas canvas = new Canvas(bitmap);
    	bitmap.eraseColor(0);

    	// Draw the text
    	Paint textPaint = new Paint();
    	textPaint.setTextSize(fontSize);
    	textPaint.setAntiAlias(true);
    	textPaint.setTextAlign(align);
    	textPaint.setFakeBoldText(true);
    	//textPaint.setARGB(0xff, 0xff, 0x00, 0x00);
    	textPaint.setColor(color);
    	// deleted by aaronli Jul8 2013/ removed the shadow of titles
    	//textPaint.setShadowLayer(5f, 2f, 2f, Color.argb(180, 0, 0, 0));
    	
    	// draw the text centered
		canvas.drawText(text, x, y, textPaint);

		int[] textureId = new int[1];
		GLES20.glGenTextures(1, textureId, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		MediaManager.saveImageToExternal(bitmap, "/mnt/sdcard/5.png");
		Log.d("getTextTextureId", "save img");

    	//Clean up
    	bitmap.recycle();
    	
    	 //long timediff = System.currentTimeMillis() - time1;
         
        // Log.d("performance", "load texture(draw text):" + timediff);
    	
    	return textureId[0];
    }
    
    private static int getTextureIdByText(String text, int fontSize, int color, int w, int h, int x, int y)
    {
    	//long time1 = System.currentTimeMillis();
    	
    	// Create an empty, mutable bitmap
    	Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    	// get a canvas to paint over the bitmap
    	Canvas canvas = new Canvas(bitmap);
    	bitmap.eraseColor(0);

    	// Draw the text
    	Paint textPaint = new Paint();
    	textPaint.setTextSize(fontSize);
    	textPaint.setAntiAlias(true);
    	textPaint.setTextAlign(Align.CENTER);
    	textPaint.setFakeBoldText(true);
    	//textPaint.setARGB(0xff, 0xff, 0x00, 0x00);
    	textPaint.setColor(color);
    	textPaint.setShadowLayer(5f, 2f, 2f, Color.argb(180, 0, 0, 0));
    	
    	// draw the text centered
		canvas.drawText(text, x, y, textPaint);

		int[] textureId = new int[1];
		GLES20.glGenTextures(1, textureId, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
				GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
				GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		MediaManager.saveImageToExternal(bitmap, "/mnt/sdcard/5.png");
		Log.d("getTextTextureId", "save img");

    	//Clean up
    	bitmap.recycle();
    	
    	 //long timediff = System.currentTimeMillis() - time1;
         
        // Log.d("performance", "load texture(draw text):" + timediff);
    	
    	return textureId[0];
    }
    
    public static Bitmap getDimImage(Bitmap bitmap)
	{
    	long timea = System.currentTimeMillis();
    	int opacity = 64;

        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        //draw the bitmap into a canvas
        Canvas canvas = new Canvas(mutableBitmap);

        //create a color with the specified opacity
        int colour = (opacity & 0xFF) << 24;

        //draw the colour over the bitmap using PorterDuff mode DST_IN
        canvas.drawColor(colour, PorterDuff.Mode.DST_IN);
        
        long diff = System.currentTimeMillis() - timea;
        Log.d("bitmapspeed", "bitmapspeed for dim1: " + diff);
        //now return the adjusted bitmap
        return mutableBitmap;
	}
    
    public static Bitmap getDimImage2(Bitmap bitmap)
	{
    	long timea = System.currentTimeMillis();
    	 //make sure bitmap is mutable (copy of needed)
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        int[] pixels = new  int[w*h];
        
        bitmap.getPixels(pixels, 0, w, 0, 0, w,h);
        
        for(int i=0; i< pixels.length; i++)
        {
        	int pixel = pixels[i];
        	int a = (pixel & 0xFF000000) >> 24;
        	int r = ((pixel & 0x00FF0000) >> 16)/2;
        	int g = ((pixel & 0x0000FF00) >> 8)/2;
        	int b = (pixel & 0x000000FF)/2;
        	pixels[i] = Color.argb(a, r, g, b);
        }
        mutableBitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        
        long diff = System.currentTimeMillis() - timea;
        Log.d("bitmapspeed", "bitmapspeed for dim2: " + diff);
        
        //now return the adjusted bitmap
        return mutableBitmap;
	}
    
	public static Bitmap getDimImage3(Bitmap oriImage)
	{
		long timea = System.currentTimeMillis();
		
		int width = oriImage.getWidth();
		int height = oriImage.getHeight();
		int argb = 0;
		Bitmap bmapCoverDim = Bitmap.createBitmap(width,height, Config.ARGB_8888);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				argb = oriImage.getPixel(i, j);
				int alpha = argb >> 24;
				int red = (argb & 0x00FF0000) >> 16;
				int green = (argb & 0x0000FF00) >> 8;
				int blue = (argb & 0x000000FF); 
				red = (int) (red * 0.5f);
				green = (int) (green * 0.5f);
				blue = (int) (blue * 0.5f);
				bmapCoverDim.setPixel(i, j, Color.argb(alpha, red, green, blue));
			}
		}
		
        long diff = System.currentTimeMillis() - timea;
        Log.d("bitmapspeed", "bitmapspeed for dim3: " + diff);
        
		return bmapCoverDim;
	}
	
	public static void saveImageToExternalWithFullQuality(Bitmap bmp, String path) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bmp.compress(CompressFormat.PNG, 100, os);
			Log.w("mediaManager", "saveImage to :" + path);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.w("mediaManager", "saveImage fail:" + e.toString());
		}
	}
  
    public static void saveImageToExternal(Bitmap bmp, String path)
	{
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			bmp.compress(CompressFormat.PNG, 80, os);
			Log.w("mediaManager","saveImage to :" + path);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.w("mediaManager","saveImage fail:" + e.toString());
		}
	}
    
    public static void saveImageToInternal(Context context, Bitmap bmp, String fileName){
    	FileOutputStream fos;
		try {
			fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bmp.compress(CompressFormat.PNG, 80, fos);
			
	    	try {
	    		fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void saveImageToInternal(Bitmap bmp, String fullFilePath){
    	FileOutputStream fos;
		try {
			File file = new File(fullFilePath);
			fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.PNG, 80, fos);
	    	try {
	    		fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    
    public static boolean deleteInternalFile(Context context, String mPath)
    {
    	File file = context.getFilesDir();
    	return file.delete();
    }
    
	public static File getDataDir(Context context) {
		return context.getDir("data", Context.MODE_PRIVATE);
	}
    
    public static File getLargeIconDir(Context context)
    {
    	File file = context.getDir("icon_l", Context.MODE_PRIVATE);
    	return file;
    }
    public static File getDimIconDir(Context context)
    {
    	File file = context.getDir("icon_ld", Context.MODE_PRIVATE);
    	return file;
    }
    
    public static File getMirrorIconDir(Context context)
    {
    	File file = context.getDir("icon_lm", Context.MODE_PRIVATE);
    	return file;
    }
    
    public static File getSmallIconDir(Context context)
    {
    	File file = context.getDir("icon_s", Context.MODE_PRIVATE);
    	return file;
    }
    
	public static Bitmap loadInternalImage(Context context, String fileName) {
		String path = Environment.getDataDirectory().toString() + File.separator + "data" + File.separator + context.getPackageName() + File.separator + "files" + File.separator +fileName;
		Log.d("mediaMgr", "loadInternalImage path:" + path);
		return BitmapFactory.decodeFile(path);
	}
	
	public static File getDir(Context context, String name)
	{
		return context.getDir(name, Context.MODE_PRIVATE);
	}
	
	
	public static String getInternalFilePath(Context context, String fileName) {
		return Environment.getDataDirectory().toString() + File.separator + "data" + File.separator + context.getPackageName() + File.separator + "files" + File.separator +fileName;
	}


    public static Bitmap getReflectionImage(Bitmap bmap) {
    	long timea = System.currentTimeMillis();

		// change image color to dim
    	int pixelCount =0;
		int width = bmap.getWidth();
		int oriHeight = bmap.getHeight();
		int height = (int) (bmap.getHeight() * 1.5f);
		int MAX_PIXEL_COUNT = width * height -1;
		//Bitmap copyBmp = bmap.copy(Config.ARGB_8888, true);
		
		Bitmap bmapMirror = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		int[] pixels = new int[width * height];
		bmap.getPixels(pixels, 0, width, 0, 0, width, oriHeight);
		pixelCount = width* oriHeight -1;

		int half = (int) (bmap.getHeight() / 2);
		//int[] mirrorPixel = new int[width * (height-oriHeight)];

		for(int i=oriHeight-1; i>=height - oriHeight; i--)
		{
			int alpha = 0;
			if (oriHeight - 1 > 128) {
				alpha = (int) (128 * ((i - 128) * 1.0f / half));
			}
			else
			{
				alpha = (int) (128 * ((i - half) * 1.0f / half));
			}
			//Log.d("performance", "load pixel row:" + i + " alpha:" + alpha);
			int[] row = new int[width];
			bmap.getPixels(row, 0, width, 0, i, width, 1);
			for (int j = 0; j < row.length; j++) {
				int argb = row[j];
				int a = (argb & 0xFF000000) >> 24;
				int red = (argb & 0x00FF0000) >> 16;
				int green = (argb & 0x0000FF00) >> 8;
				int blue = (argb & 0x000000FF);
				//Log.d("performance", "load pixel row:" + i + " alpha:" + alpha + "  a:" + a);
				int pixel = Color.argb(alpha, red, green, blue);
				
				if (a == 0) {
					pixels[pixelCount] = row[j];
				} else {
					
					pixels[pixelCount] = pixel;
				}
				pixelCount++;
				if(pixelCount>MAX_PIXEL_COUNT)
				{
					Log.w("getReflectionImage", "the pixel count larger than max count");
					break;
				}
			}
		
		}

		Log.d("mirror test", "mirror test");
		bmapMirror.setPixels(pixels,0,width,0,0,width, height);		
		
        long diff = System.currentTimeMillis() - timea;
        Log.d("bitmapspeed", "bitmapspeed for dim2: " + diff);
        
		return bmapMirror;
	}
    
    //covert to list view icon
	public static Bitmap getIconForListView(Bitmap largeImg)
	{
		float scaleFactor = 80f/largeImg.getHeight();
		Matrix m = new Matrix();
		m.postScale(scaleFactor, scaleFactor);
		return Bitmap.createBitmap(largeImg, 0, 0, largeImg.getWidth(), largeImg.getHeight(), m, false);
	}
	
	
	public static Bitmap generateMovieThumbnail(String path)
	{
		return ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND);	
	}
	
	public static List<File> getAllImagesFiles()
	{
		String cameraImagePath = "/mnt/sdcard/DCIM/Camera/";
		String photoPath =  Global.PATH_PHOTO_DATA;
		String externalPath = "/mnt/extsd/";
		List<File> fileList = new ArrayList<File>();
		File cameraDir = new File(cameraImagePath);
		traverseDirForPhoto(cameraDir, fileList);
		File photoDir = new File(photoPath);
		traverseDirForPhoto(photoDir, fileList);
		File externalDir = new File(externalPath);
		traverseDirForPhoto(externalDir, fileList); 
		Log.d("getAllImagesFiles", "count:" + fileList.size());
		return fileList;
	}
	
	private static void traverseDirForPhoto(File dir, List<File> theList) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
			    // skip all files or folders starting with a dot
			    if (files[i].getName().startsWith(".")) {
			        continue;
			    }
			    
				if (files[i].isDirectory()) {
					traverseDirForPhoto(files[i], theList);
				} else {
					if (!files[i].isHidden() && (files[i].getName().contains(Global.FILE_TYPE_PNG) || files[i].getName().contains(Global.FILE_TYPE_JPG))) {
						theList.add(files[i]);
					}
				}
			}
		}
	}
	
	public static Bitmap bitmapResizeToLargePreviewIcon(Bitmap bmp) {
		final float MAX_W  = 256;
		final float MAX_H = 256;
		int h = bmp.getHeight();
		int w = bmp.getWidth();
		double scale = 1;
		if (h > w) {
			if (h < MAX_H) {
				return bmp;
			}
			scale = MAX_H / h;

		} else {
			if (w < MAX_W) {
				return bmp;
			}
			scale = MAX_W / w;
		}
		h = (int) (h * scale);
		w = (int) (w * scale);
		return Bitmap.createScaledBitmap(bmp, w, h, false);
	}
	
	public static Bitmap bitmapResizeToUserIcon(Bitmap bmp)
	{
		Bitmap mutable = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutable);
		Paint paint = new Paint();
		paint.setFilterBitmap(false);

		int oriH = bmp.getHeight();
		int oriW = bmp.getWidth();
		int h = 0;
		int w = 0;
		int x = 0;
		int y = 0;
		Bitmap resizeBmp = null;
		Bitmap cropedBmp = null;
		if (oriH > 80) {
			//for pixel > 80;
			if (oriH > oriW) {
				w = 80;
				h = (int) ((80f / oriW) * oriH);
				Log.d("usericon", "1 w:" + w + " h:" + h + " ow:" + oriW + " oh:" + oriH);
				resizeBmp = Bitmap.createScaledBitmap(bmp, w, h, true);
				cropedBmp = Bitmap.createBitmap(resizeBmp, 0, (int) ((h - 80) / 2), 80, 80);
			} else {
				h = 80;
				w = (int) ((80f / oriH) * oriW);
				Log.d("usericon", "2 w:" + w + " h:" + h + " ow:" + oriW + " oh:" + oriH);
				resizeBmp = Bitmap.createScaledBitmap(bmp, w, h, true);
				cropedBmp = Bitmap.createBitmap(resizeBmp, (int) ((w - 80) / 2), 0, 80, 80);
			}
		}
		else
		{
			//for pixel < 80;
			y = (int)((80 - oriH)/2);
			if(oriW >80)
			{
				cropedBmp = Bitmap.createBitmap(bmp, (int) ((oriW - 80) / 2), 0, 80, oriH);
			}
			else
			{
				cropedBmp = bmp;
				x = (int)((80 - oriW)/2);
			}
		}
		//Log.d("usericon", "3 w:" + cropedBmp.getWidth() + " h:" + cropedBmp.getHeight() + " ow:" + mask.getWidth() + " oh:" + mask.getHeight());
		// Bitmap resizeBmp = bmp.createScaledBitmap(bmp, dstWidth, dstHeight,
		// filter)

		canvas.drawBitmap(cropedBmp,x, y, paint);
		return mutable;
	}
	
	public static Bitmap bitmapResizeToUserIcon300(Bitmap bmp)
	{
		Bitmap mutable = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutable);
		Paint paint = new Paint();
		paint.setFilterBitmap(false);

		int oriH = bmp.getHeight();
		int oriW = bmp.getWidth();
		int h = 0;
		int w = 0;
		int x = 0;
		int y = 0;
		Bitmap resizeBmp = null;
		Bitmap cropedBmp = null;
		if (oriH > 30) {
			//for pixel > 300;
			if (oriH > oriW) {
				w = 300;
				h = (int) ((300f / oriW) * oriH);
				Log.d("usericon", "1 w:" + w + " h:" + h + " ow:" + oriW + " oh:" + oriH);
				resizeBmp = Bitmap.createScaledBitmap(bmp, w, h, true);
				cropedBmp = Bitmap.createBitmap(resizeBmp, 0, (int) ((h - 300) / 2), 300, 300);
			} else {
				h = 300;
				w = (int) ((300f / oriH) * oriW);
				Log.d("usericon", "2 w:" + w + " h:" + h + " ow:" + oriW + " oh:" + oriH);
				resizeBmp = Bitmap.createScaledBitmap(bmp, w, h, true);
				cropedBmp = Bitmap.createBitmap(resizeBmp, (int) ((w - 300) / 2), 0, 300, 300);
			}
		}
		else
		{
			//for pixel < 300;
			y = (int)((300 - oriH)/2);
			if(oriW >300)
			{
				cropedBmp = Bitmap.createBitmap(bmp, (int) ((oriW - 300) / 2), 0, 300, oriH);
			}
			else
			{
				cropedBmp = bmp;
				x = (int)((300 - oriW)/2);
			}
		}
		//Log.d("usericon", "3 w:" + cropedBmp.getWidth() + " h:" + cropedBmp.getHeight() + " ow:" + mask.getWidth() + " oh:" + mask.getHeight());
		// Bitmap resizeBmp = bmp.createScaledBitmap(bmp, dstWidth, dstHeight,
		// filter)

		canvas.drawBitmap(cropedBmp,x, y, paint);
		return mutable;
	}
	
	
	public static Bitmap generateUserIcon(Bitmap bmp, Bitmap bg, Bitmap mask, Bitmap cover) {

		if (bmp != null && bg != null && mask != null && cover != null) {
			Bitmap mutable = Bitmap.createBitmap(80, 80, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mutable);
			Paint paint = new Paint();
			paint.setFilterBitmap(false);
			canvas.drawBitmap(bmp, 0, 0, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			canvas.drawBitmap(mask, 0, 0, paint);
			paint.setXfermode(null);
			canvas.drawBitmap(cover, 0, 0, paint);

			return mutable;
		}
		return null;

	}
	
	public static Bitmap generateUserIcon300(Bitmap bmp, Bitmap bg, Bitmap mask, Bitmap cover) {

		if (bmp != null && bg != null && mask != null && cover != null) {
			Bitmap mutable = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mutable);
			Paint paint = new Paint();
			paint.setFilterBitmap(false);
			canvas.drawBitmap(bmp, 0, 0, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			canvas.drawBitmap(mask, 0, 0, paint);
			paint.setXfermode(null);
			canvas.drawBitmap(cover, 0, 0, paint);

			return mutable;
		}
		return null;

	}
	
	private List<MediaItem> mMediaItemList;
	
	public List<MediaItem> getMediaItemList() {
		return mMediaItemList;
	}

	public void setmMediaItemList(List<MediaItem> mediaItemList) {
		this.mMediaItemList = mediaItemList;
	}
	
	public boolean changeFilePermission(File file) {

		try {
			if (file.exists()) {
				String command = "chmod 777 " + file.getAbsolutePath();
				Log.i("zyl", "command = " + command);
				Runtime runtime = Runtime.getRuntime();
				Process proc = runtime.exec(command);
				proc.getOutputStream();
				return true;
			}
		} catch (IOException e) {
			Log.i("zyl", "chmod fail!!!!");
			e.printStackTrace();
		}

		return false;
	}

	private final float INIT_ANGLE = -75;
	private final float SEP_ANGLE = 15;
	
	
	public static Bitmap getMovieLargeIcon(Bitmap oriBmp, Bitmap bmpBg, Bitmap bmpTop) {
		try {
			if (oriBmp != null && bmpBg != null) {
				Bitmap bmp = oriBmp;
				float scale = 296f / bmp.getWidth();
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);

				Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				int cuty = (int) (resizedBitmap.getHeight() / 2 - 89);
				Bitmap cropBitmap = null;
				if (resizedBitmap.getHeight() > 178) {
					cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, cuty, resizedBitmap.getWidth(), 178);
				} else {
					cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
				}

				Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
				Canvas canvas = new Canvas(mutableBm);
				canvas.drawBitmap(cropBitmap, 0, 60, null);

				return mutableBm;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;

	}
	
	public static Bitmap getMusicLargeIcon(Bitmap bmp, Bitmap bmpBg, Bitmap bmpTop)
	{
		if (bmp == null || bmpBg == null || bmpTop == null) {
			return null;
		}	
		
		try {
			float scale = 300f / bmp.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);

			Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			int cutx = (int) (resizedBitmap.getWidth() / 2 - 150);
			Bitmap cropBitmap = null;
			int moveX = 0;
			if (resizedBitmap.getWidth() > 300) {
				cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, 300, resizedBitmap.getHeight());
			} else {
				moveX = (int)((300 - resizedBitmap.getWidth())/2);
				cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
			}
			resizedBitmap.recycle();

			Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
			Canvas canvas = new Canvas(mutableBm);
			
			canvas.drawBitmap(cropBitmap, 40 + moveX, 10, null);
			canvas.drawBitmap(bmpTop, 0, 0, null);
			
			cropBitmap.recycle();
			bmpBg.recycle();
			bmpTop.recycle();
			
			return mutableBm;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static Bitmap getPhotoLargeIcon(Bitmap orgImage, Bitmap bottom, Bitmap top, int w, int h, int leftMargin, int topMargin)
	{
		Bitmap bmpBg = bottom;
		Bitmap bmpTop = top;
		Bitmap bmp = orgImage;
		float scale = (float)h / bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		int cutx = (int) (resizedBitmap.getWidth() / 2 - w/2);
		Bitmap cropBitmap = null;
		int moveX = 0;
		if (resizedBitmap.getWidth() > w) {
			cropBitmap = Bitmap.createBitmap(resizedBitmap, cutx, 0, w, resizedBitmap.getHeight());
		} else {
			moveX = (int)((w - resizedBitmap.getWidth())/2);
			cropBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
		}

		Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		
		canvas.drawBitmap(cropBitmap, leftMargin + moveX, topMargin, null);
		canvas.drawBitmap(bmpTop, 0, 0, null);
		
		return mutableBm;
	}
	
	public static Bitmap getEbookLargeIcon(Bitmap bitmap, Bitmap iconBg)
	{
		Bitmap bmpBg = iconBg;
		//Bitmap bmp = bitmap;
		int h = 278;
		int w = 191;
		
		Bitmap cropBitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h);

		Bitmap mutableBm = bmpBg.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBm);
		
		canvas.drawBitmap(cropBitmap, 55, 17, null);
		
		return mutableBm;
	}
	
	public static Bitmap getGameLargeIcon(Bitmap bitmap, Bitmap iconBg, Bitmap iconTop)
	{
		Bitmap cropBitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);

			Bitmap mutable = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mutable);
			Paint paint = new Paint();
			paint.setFilterBitmap(false);
			canvas.drawBitmap(cropBitmap, 0, 0, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			canvas.drawBitmap(iconBg, 0, 0, paint);
			paint.setXfermode(null);
			canvas.drawBitmap(iconTop, 0, 0, paint);

			return mutable;
	}
	
	public static Bitmap getGameLargeIcon2(Bitmap bitmap, Bitmap iconBg, Bitmap iconTop)
	{
		Bitmap cropBitmap = null;
		if (bitmap.getHeight() >= 300 || bitmap.getWidth() >= 300) {
			cropBitmap = ThumbnailUtils.extractThumbnail(bitmap, 300, 300);
		}else{
			Log.d("gameLargeIconSize", "w:" + bitmap.getWidth() + " h:" + bitmap.getHeight());
			cropBitmap = bitmap;
		}

			Bitmap mutable = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(mutable);
			Paint paint = new Paint();
			paint.setFilterBitmap(false);
			canvas.drawBitmap(cropBitmap, 0, 0, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
			canvas.drawBitmap(iconBg, 0, 0, paint);
			paint.setXfermode(null);
			canvas.drawBitmap(iconTop, 0, 0, paint);

			return mutable;
	}
	
	public static Bitmap getIconForGooglePlayItem(Bitmap bitmap, Bitmap iconBg, Bitmap iconTop) {
		if (bitmap.getWidth() < 300) {
			bitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, false);
		}
		
		Bitmap mutable = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(mutable);
		Paint paint = new Paint();
		canvas.drawBitmap(iconBg, 0, 0, paint);
		canvas.drawBitmap(bitmap, 98, 52, paint);
		
//		if (iconTop != null) {
//			canvas.drawBitmap(iconTop, 0, 0, paint);
//		}
		
		return mutable;
	}

}
