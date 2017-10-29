package com.iskyinfor.duoduo.downloadManage.pool;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
/**
 * �������ӳع���
 * 
 * @author pKF29007
 *
 */
public class HttpURLConnectionPoolableObjectFactory implements
		KeyedPoolableObjectFactory {

    /**
     * ���ӳ�ʱʱ��
     */
	private static final int CONNECT_TIME_OUT = 20000;

	/**
	 * ����HttpURLConnection����
	 */
	@Override
	public Object makeObject(Object url) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL((String) url)
				.openConnection();
		conn.setConnectTimeout(CONNECT_TIME_OUT);
		conn.setReadTimeout(CONNECT_TIME_OUT);
		conn.addRequestProperty("Andorid/1.0", "NetFox");
		return conn;
	}

	/**
	 * ���HttpURLConnection����
	 */
	@Override
	public void destroyObject(Object url, Object connect) throws Exception {
		connect = null;
	}

	/**
	 * У��HttpURLConnection����
	 */
	@Override
	public boolean validateObject(Object url, Object connect) {
		boolean result = false;
		if (connect instanceof HttpURLConnection) {
			result = true;
		}
		return result;
	}

	/**
	 * �ۻ�HttpURLConnection����
	 */
	@Override
	public void passivateObject(Object url, Object connect) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) connect;
		conn.disconnect();
	}

	/**
	 * ����HttpURLConnection����
	 */
	@Override
	public void activateObject(Object url, Object connect) throws Exception {
	}
}
