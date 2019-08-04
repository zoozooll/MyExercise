package com.beem.project.btf.service;

import com.butterfly.vv.db.ormhelper.bean.UrlDB;
import com.teleca.jamendo.api.WSError;

/**
 * @ClassName: ContactOperation
 * @Description:数据操作类
 * @Description (1)根据url，设置url的生命周期，如在生命周期从本地数据库中取；否则从网络中取，有效减少客户的流量。
 * @Description (2)需要实现两个方法：(1)从网络获取数据(2)从本地数据库获取数据
 * @author: yuedong bao
 * @date: 2015-4-2 下午2:10:17
 * @param <T>
 */
public abstract class DataOperation<T> {
	protected Class<?> cls;
	protected String param;

	public DataOperation() {
		super();
	}
	public DataOperation<T> setParams(Class<?> cls, String params) {
		this.cls = cls;
		this.param = params;
		return this;
	}
	private final T getDataInner(boolean isFromDB) throws WSError {
		T t;
		if (isFromDB) {
			t = getDataFromDB();
			//			//LogUtils.v("getDataFromDB result:" + (t != null ? t.getClass() : ""));
		} else {
			t = getDataFromNetwork();
			//			//LogUtils.v("getDataFromNetwork result:" + (t != null ? t.getClass() : ""));
			if (t != null) {
				UrlConfigUtil.savePacketUrl(cls, param);
			}
		}
		return t;
	}
	/**
	 * (未来架构，未实现)
	 * 获取数据，会判断从本地获取或者网络获取。将由两个参数构成。
	 * 判断过程：<br/>
	 * (1) {@link isFromDB} 判断是否通过缓存。true-->(2), false-->(3)
	 * (2)	如果缓存已经过期，true-->(3);false-->(4)
	 * (3) 判断能否通过网络获取，参数{@link isReloads}判断是否需要登录，判断是首先看网络连接是否打开；
	 *     如果{@link isReloads} == true; 则需要判断是否在登录状态；true-->(5); false-->(4)
	 * (4) 从本地数据库取数据
	 * (5) 从网络取数据,如果超时-->(4)
	 * 	
	 * @param isFromDB 是否通过缓存。并不是最终一定通过缓存。如果为true，则优先判断缓存是否过期；
	 *                 如果为false则无视缓存过期直接进入下一步。
	 * @param isReloads
	 * @return
	 * @throws WSError
	 */
	public final T getData(boolean isFromDB, boolean... needLogin) throws WSError {
		T retVal = null;
		boolean isReload = needLogin.length > 0 ? needLogin[0] : false;
		boolean isReloadOne = isReload;
		if (isReloadOne) {
			return getDataInner(isFromDB);
		} else {
			if (!isFromDB) {
				// 从服务器获取
				UrlDB urlDB = UrlConfigUtil.getPacketUrl(cls, param);
				// 死亡状态
				if (urlDB == null || urlDB.isDead()) {
					retVal = getDataInner(false);
				} else {
					retVal = getDataInner(true);
				}
			} else {
 				retVal = getDataInner(true);
			}
		}
		return retVal;
	}
	/**
	 * @Title: getDataFromNetwork
	 * @Description: 从网络中获取数据
	 * @param param
	 * @return
	 * @return: T
	 * @throws WSError 
	 */
	protected abstract T getDataFromNetwork() throws WSError;
	/**
	 * @Title: getDataFromDB
	 * @Description: 从数据库中获取数据
	 * @param param
	 * @return
	 * @return: T
	 */
	protected abstract T getDataFromDB();
}
