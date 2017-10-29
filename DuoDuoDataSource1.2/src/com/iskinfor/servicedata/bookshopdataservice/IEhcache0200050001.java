package com.iskinfor.servicedata.bookshopdataservice;

import java.util.List;

public interface IEhcache0200050001 {
	/**
	 * 关键字查询
	 * @param keyWord
	 * @return
	 * @throws Exception
	 */
	List<String> getKeyWord(String keyWord) throws Exception;
	
	/**
	 * 关键字插入
	 * @param keyWord
	 * @return
	 * @throws Exception
	 */
	boolean putKeyWord(String keyWord) throws Exception;

}
