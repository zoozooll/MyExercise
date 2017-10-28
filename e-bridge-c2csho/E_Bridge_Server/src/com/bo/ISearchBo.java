package com.bo;

import java.util.List;

import com.vo.PageBean;

public interface ISearchBo{
	/**
	 * 
	 * @param entityName pojo����
	 * @param curPage ��ǰҳ
	 * @param maxResults һҳ������ʾ��
	 * @param condition ��ѯ����
	 * @return
	 */
	public PageBean list(String entityName, Integer curPage,
			Integer maxResults, String condition)throws Exception ;
	
	public Integer countList(String entityName) throws Exception ;
}
