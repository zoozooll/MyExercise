/**
 * 
 */
package com.iskyinfor.zoom;


import org.vudroid.CodecDao;

import com.kang.pdfreader.MyPdfReaderActivity;

import android.os.AsyncTask;

/**
 * @author Aaron Lee
 * @date 2011-7-20 下午07:49:41
 */
public class ImageZoomManager extends AsyncTask<Integer, Void, Object> {
	
	public static final int CODEC_FIRST_OPEN = 0x21;
	public static final int CODEC_GET_NEXT = 0x22;
	public static final int CODEC_GET_PREVENT = 0x23;
	public static final int CODEC_GOTOPAGE = 0x24;
	
	public static final String DOCUMENT_COUNT = "pageCount";
	public static final String DOCUMENT_WIDTH = "width";
	public static final String DOUCUMENT_HEIGHT = "height";
	public static final String DOCUMENT_BITMAP = "bitmap";
	
	private MyPdfReaderActivity activity;
	private CodecDao codecDao;
	
	private String pdfPath;
	private int drawWidth;
	private int drawHeight;
	private int pageIndex;

	public ImageZoomManager(MyPdfReaderActivity activity, String pdfPath,
			int drawWidth, int drawHeight) {
		super();
		this.activity = activity;
		this.pdfPath = pdfPath;
		this.drawWidth = drawWidth;
		this.drawHeight = drawHeight;
	}

	public int getDrawWidth() {
		return drawWidth;
	}

	public void setDrawWidth(int drawWidth) {
		this.drawWidth = drawWidth;
	}

	public int getDrawHeight() {
		return drawHeight;
	}

	public void setDrawHeight(int drawHeight) {
		this.drawHeight = drawHeight;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	@Override
	protected Object doInBackground(Integer... params) {
		int flag = params[0];
		Object object = null;
		switch (flag) {
		case CODEC_FIRST_OPEN:
			object = firstOpen();
			break;
		
		case CODEC_GET_NEXT:
			object = getNext();		
			break;
			
		case CODEC_GET_PREVENT:
			object = getPrevent();
			break;
			
		case CODEC_GOTOPAGE:
			int pageIndex = params[1];
			object = gotoPage(pageIndex);
			break;
		}
		return object;
	}

	@Override
	protected void onPreExecute() {
		if (codecDao == null) {
			codecDao = new CodecDao();
		}
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
	}
	
	private Object firstOpen(){
		Object o = null;
		try {
			o = codecDao.searchAll(pdfPath, drawWidth, drawHeight);
			pageIndex = 0 ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
		
	}
	
	private Object getNext() {
		Object o = null;
		try {
			o = codecDao.searchPage(pdfPath, drawWidth, drawHeight, pageIndex++);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o ;
	}
	
	private Object getPrevent() {
		Object o = null;
		try {
			codecDao.searchPage(pdfPath, drawWidth, drawHeight, pageIndex--);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}
	
	private Object gotoPage(int pageIndex){
		Object o = null;
		try {
			o = codecDao.searchPage(pdfPath, drawWidth, drawHeight, pageIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o ;
	}
}
