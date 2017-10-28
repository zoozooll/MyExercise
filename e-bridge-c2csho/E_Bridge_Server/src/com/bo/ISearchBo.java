package com.bo;

import java.util.List;

import com.vo.PageBean;

public interface ISearchBo{
	/**
	 * 
	 * @param entityName pojo类名
	 * @param curPage 当前页
	 * @param maxResults 一页最在显示数
	 * @param condition 查询条件
	 * @return
	 */
	public PageBean list(String entityName, Integer curPage,
			Integer maxResults, String condition)throws Exception ;
	
	public Integer countList(String entityName) throws Exception ;
}
