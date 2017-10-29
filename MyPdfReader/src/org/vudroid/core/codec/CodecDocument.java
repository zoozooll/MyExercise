package org.vudroid.core.codec;

public interface CodecDocument {
	
	/**
	 * 获得内容页
	 * @param pageNumber
	 * @return 内容页，封装的类
	 */
    CodecPage getPage(int pageNumber);
    /**
     * 获得总页数；
     * @return 读取的文件的总页数；
     */
    int getPageCount();
    
    /**
     * 释放资源
     */
    void recycle();
}
