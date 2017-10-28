package com.dao;

import java.util.List;

public interface ISearchDao{
	/**
	 * 
	 * @param entityName pojo类名
	 * @param start 当前页
	 * @param maxResults 一页最在显示数
	 * @param condition 查询条件
	 * @return
	 */
	public List<Object> list(String entityName, Integer start,
			Integer maxResults, String condition)throws Exception ;
	
	public Integer countList(String entityName) throws Exception ;
}
