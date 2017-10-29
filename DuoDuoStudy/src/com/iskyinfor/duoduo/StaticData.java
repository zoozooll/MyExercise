package com.iskyinfor.duoduo;

import android.graphics.Bitmap;
import android.os.Environment;

public abstract class StaticData {
	
	public static final int ALL = 0;
	
	public static final int BOOKS = 1;
	
	public static final int COURSEWARE = 2;
	
	public static final int EXAM = 3;
	
	public static final int EXAMPAPER = 4;
	
	public static final int GROUPSHOP = 5;
	
	//关于传递数值的参数；
	
	public static final String STORE_KEY_PRODUCTES = "productes";
	
	public static final String STORE_KEY_TOTAL = "total";
	
	public static final String STORE_KEY_ORDERES = "orderes";
	 
	//public static final String userId = "0001";
	
	public static int DEFAULT_PAGE = 1;
	
	public static Bitmap [] book_small_bitmap = null;
	
	public static int currentPage;
	
	public static int cuurentType;
	
	public static String methodName;
	
	//图片保存地址
	public static final String IMAGE_DOWNLOAD_ADDR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/duoduo/images";
	
    //
	public static boolean boolSended=false; 
}
