package com.iskyinfor.duoduo.ui.provider;

import android.net.Uri;

/**
 * ���������ݿ⹫����Ϣ������
 * 
 * @author pKF29007
 * 
 */
public class DbConstants {

	public static final String TAG = "DbConstant";

	public static final String _ID = "_id";

	/**
	 * ��ݿ����
	 */
	public static final String DB_NAME = "duoduo.db";

	/**
	 * �����
	 */
	public static final String TB_NBAME = "book";


	
	/**
	 * ��ݿ�汾
	 */
	public static final int VERSION = 10;

	/**
	 * ��ݿ��ʾ
	 */
	public static final String AUTHORITY = "com.iskyinfor.duoduo.book.provider";

	/**
	 * URI��Ӧ��
	 */
	public static final int ITEM = 1;

	/**
	 * ��ݿ����ģʽ
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + "item" + "/");

	/**
	 * Task���ֶ�˵��
	 * 
	 * @author pKF29007
	 */
	public static final class BookTBField {

//		书名
		//
//			ID
//			书的log网络地址
//			书的LOG本地地址
//			作者
//			
		
		public static final String _ID = "_id";

		/**
		 * 书籍的id
		 */
		public static final String RESID = "resourceId";

		/**
		 * 下载书籍的网络地址
		 */
		public static final String URL = "bookurl";
        /**
         * 书籍保存的本地地址
         */
		public static final String BOOK_FILE_PATH = "bookpath";
		/**
		 * 作者
		 */
		public static final String BOOK_AUTHOR="book_author";
	/**
	 * 	评分
	 */
			public static final String BOOK_COMMENT_PAINT="book_comment_paint";
		/**
		 * 上次阅读时间 YYYY-M-D 时：分：秒
		 */
		public static final String BOOK_READ_REMMBER_TIME="remmber_read_time";
		/**
		 * 书籍logo的网络地址
		 */
		public static final String BOOK_LOGO_PATH="book_logo_url";
	}

}
