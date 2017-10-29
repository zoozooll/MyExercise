package com.iskyinfor.duoduo.downloadManage.pool;

import org.apache.commons.pool.KeyedObjectPool;

import org.apache.commons.pool.KeyedObjectPoolFactory;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.StackKeyedObjectPoolFactory;

import com.iskyinfor.duoduo.downloadManage.Constants;




/**
 * ���Ӷ����
 * 
 * @author pKF29007
 *
 */
public class HttpURLConnectionPool {
	// �������
	private static KeyedObjectPool httppool = null;
	
	/**
	 * ���ؿ����ö�����
	 */
	private static int MAX_POOL_SIZE = 10;
	
	/**
	 * ��ʼ���ؿ��ö�����
	 */
	private static int INIT_POOL_SIZE = 3;
	
	/**
	 * ��ȡ����ObjectPool����
	 * @return ����KeyedObjectPool����
	 */
	public synchronized static KeyedObjectPool getInstance() {
		if (httppool == null) {
			KeyedPoolableObjectFactory factory = new HttpURLConnectionPoolableObjectFactory();
			KeyedObjectPoolFactory poolFactory = new StackKeyedObjectPoolFactory(
					factory,MAX_POOL_SIZE, INIT_POOL_SIZE);
			httppool = poolFactory.createPool();
		}
		return httppool;
	}
}
