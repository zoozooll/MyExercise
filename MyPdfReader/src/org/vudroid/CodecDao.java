package org.vudroid;

import java.util.HashMap;
import java.util.Map;

import org.vudroid.core.codec.CodecContext;
import org.vudroid.core.codec.CodecDocument;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;

import com.iskyinfor.zoom.ImageZoomManager;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class CodecDao {

	CodecDocument document;
	
	private void setDocument(String pdfPath) {
		CodecContext context = new PdfContext();
		document = context.openDocument(pdfPath);
	}
	
	/**
	 * 获得所有的参量，封装到Map里面
	 * @param pdfPath Pdf文档
	 * @param drawWidth
	 * @param drawHeight
	 * @param rectFLeft
	 * @param rectFTop
	 * @param rectFRight
	 * @param rectFBottom
	 * @return
	 * @Author Aaron Lee
	 * @date 2011-7-20 下午08:25:48
	 */
	public Map<String, Object> searchAll(String pdfPath, int drawWidth, int drawHeight,
			int rectFLeft, int rectFTop, int rectFRight, int rectFBottom) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		if (document == null) {
			setDocument(pdfPath);
		}
		// 获得总页数
		int pageCount = document.getPageCount();
		// 获得pdf的内容
		CodecPage code = document.getPage(0);
		// 获得pdf内容的宽高页
		int width = code.getWidth();
		int height = code.getHeight();
		Bitmap bm = code.renderBitmap(drawWidth, drawHeight, new RectF(
				rectFLeft, rectFTop, rectFRight, rectFBottom));
		data.put(ImageZoomManager.DOCUMENT_COUNT, pageCount);
		data.put(ImageZoomManager.DOCUMENT_WIDTH, width);
		data.put(ImageZoomManager.DOUCUMENT_HEIGHT, height);
		data.put(ImageZoomManager.DOCUMENT_BITMAP, bm);
		return data;
	}
	
	/**
	 * 获得所有的参量，封装到Map里面
	 * @param pdfPath
	 * @param drawWidth
	 * @param drawHeight
	 * @param rectFLeft
	 * @param rectFTop
	 * @param rectFRight
	 * @param rectFBottom
	 * @return 
	 * @Author Aaron Lee
	 * @date 2011-7-20 下午08:25:48
	 */
	public Map<String, Object> searchAll(String pdfPath, int drawWidth, int drawHeight) throws Exception{
		Map<String, Object> data = new HashMap<String, Object>();
		if (document == null){
			setDocument(pdfPath);
		}
        //获得总页数
        int pageCount = document.getPageCount();
        //获得pdf的内容
        CodecPage code = document.getPage(0);
        //获得pdf内容的宽高页
        int width = code.getWidth();
        int height = code.getHeight();
        Bitmap bm = code.renderBitmap(drawWidth, drawHeight, new RectF(0.0F,0.0f,1.0f,1.0f));
        data.put(ImageZoomManager.DOCUMENT_COUNT, pageCount);
        data.put(ImageZoomManager.DOCUMENT_WIDTH, width);
        data.put(ImageZoomManager.DOUCUMENT_HEIGHT, height);
        data.put(ImageZoomManager.DOCUMENT_BITMAP, bm);
        return data;
	}
	
	/**
	 * 根据页码获得图片；
	 * @param pdfPath
	 * @param drawWidth
	 * @param drawHeight
	 * @param pageIndex
	 * @return
	 * @Author Aaron Lee
	 * @date 2011-7-21 上午09:32:36
	 */
	public Bitmap searchPage(String pdfPath, int drawWidth, int drawHeight,
			int pageIndex) throws Exception {
		if (document == null) {
			setDocument(pdfPath);
		}
		CodecPage code = document.getPage(pageIndex);
		Bitmap bm = code.renderBitmap(drawWidth, drawHeight, new RectF(0.0F,
				0.0f, 1.0f, 1.0f));
		return bm;
	}
}
