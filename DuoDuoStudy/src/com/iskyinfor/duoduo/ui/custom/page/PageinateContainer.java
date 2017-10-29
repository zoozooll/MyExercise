package com.iskyinfor.duoduo.ui.custom.page;

import java.util.ArrayList;

/**
 * 分页数据容器
 * 
 */
public class PageinateContainer<T> {
	public boolean state;
	/**
	 * 总页数
	 */
	public int totalPageCount = Integer.MIN_VALUE;

	/**
	 *总条目数
	 */
	public int totalCount = Integer.MIN_VALUE;

	/**
	 * 存放数据容器
	 */
	public ArrayList<T> responseData = new ArrayList<T>();

}

