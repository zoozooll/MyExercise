package com.iskyinfor.duoduo.downloadManage.provider;

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
	public static final String DB_NAME = "download.db";

	/**
	 * �����
	 */
	public static final String TB_NBAME = "task";

	/**
	 * ��ݿ�汾
	 */
	public static final int VERSION = 10;

	/**
	 * ��ݿ��ʾ
	 */
	public static final String AUTHORITY = "com.iskyinfor.duoduo.downloader.provider";

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
	public static final class TaskTbField {

		public static final String _ID = "_id";

		/**
		 *  ���urlʹ��MD5�����32λ���ص�ַ
		 */
		public static final String RESID = "resourceId";

		/**
		 * �ļ������ص�ַ
		 */
		public static final String URL = "url";

		/**
		 * �ļ�����·����ֻ����·�� �����ļ���
		 */
		public static final String FILEPATH = "filepath";

		/**
		 * �ļ������չ��
		 */
		public static final String FILENAME = "filename";
		
		/**
		 * �ļ���չ��
		 */
		public static final String EXTENDNAME = "extendName";


		/**
		 * �ļ���MetaType���ͣ�һ����ͨ����չ����������á�
		 * �ļ�����Ҳ��ͨ��MetaType�������
		 */
		public static final String METATYPE = "metaType";

		
		
		/**
		 * �ļ�չʾ�õ����
		 */
		public static final String NAME = "name";
		
		
		/**
		 * �ļ���С
		 */
		public static final String TOTALSIZE = "totalSize";

		/**
		 * ��ǰ�ļ���С������֧�ֶϵ���
		 */
		public static final String CURRENTSIZE = "currentSize";
		
		
		/**
		 * ��������ĵ�ǰ״̬
		 */
		public static final String TASKSTATE = "taskState";

		/**
		 * ���������֪ͨ��ʾ��Ϊ�˷�ֹ֪ͨ�ظ�
		 */
		public static final String NOTIFYTAG = "notifyTag";

		/**
		 * ���ؼ�¼������޸�ʱ�䣬��������
		 */
		public static final String LASTMOD = "lastmod";

		/**
		 * ���������ʧ�ܴ�����ڴ����ж��Ƿ���ֹ����������
		 *  ����ʧ��3�Σ�Ĭ��Ϊ����ʧ�ܣ�ÿ���������¿�ʼʱfailCount���㡣
		 */
		public static final String FAILCOUNT = "failCount";

		/**
		 * ����ʧ��ʱ���ٴ�����ʱ��
		 */
		public static final String RETRYTIME = "retryTime";

		/**
		 * �ж������Ƿ�������ɣ�ʵ��������ɵ������δ���������������
		 */
		public static final String ISFINISH = "isFinish";

		/**
		 *  ��ʾ�ļ������ͣ�һ�������֣���Ƶ��ͼƬ��Ӧ�ã������ �ļ����Ϳ����������� ��
		 *  Ҳ����ͨ��MetaType���ж�
		 */
		public static final String FILETYPE = "fileType";


		/**
		 * ָ���ļ����������ͣ����ھ����ļ�����ĸ�·��
		 */
		public static final String DOWNTYPE = "downType";

		/**
		 * �ļ���������Ĵ������ͣ��м��ֳ���Ĵ������� 
		 */
		public static final String ERRORCODE = "errorCode";

	
		/**
		 * ���App���ͱ�������
		 */
		public static final String PACKAGENAME = "packageName";
		
		
		/**
		 * Ӧ�õ����ʱ�䣬��ͬ��lastmod������޸�ʱ��
		 */
		public static final String CREATETIME = "createTime";

		/**
		 * Ӧ�����ع���л�ͬʱ����֪ͨ����silentMode��������������ع���Ƿ�Ĭ��
		 * ������֪�����صĹ��
		 */
		public static final String SILENTMODE = "silentMode";
		/**
		 * Ӧ�����ع���л�ͬʱ����֪ͨ����silentMode��������������ع���Ƿ�Ĭ��
		 * ������֪�����صĹ��
		 */
		public static final String NETSTATE = "netState";

	}

}
