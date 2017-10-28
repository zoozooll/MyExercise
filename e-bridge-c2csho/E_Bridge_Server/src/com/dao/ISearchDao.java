package com.dao;

import java.util.List;

public interface ISearchDao{
	/**
	 * 
	 * @param entityName pojo����
	 * @param start ��ǰҳ
	 * @param maxResults һҳ������ʾ��
	 * @param condition ��ѯ����
	 * @return
	 */
	public List<Object> list(String entityName, Integer start,
			Integer maxResults, String condition)throws Exception ;
	
	public Integer countList(String entityName) throws Exception ;
}
