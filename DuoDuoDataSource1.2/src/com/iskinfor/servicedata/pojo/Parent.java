package com.iskinfor.servicedata.pojo;

import java.util.List;
/**
 * 家长用户
 * @author Administrator
 *
 */
public class Parent extends User{
	/**
	 * 家长 的孩子们
	 */
	private List<Child> childs;

	public List<Child> getChilds() {
		return childs;
	}

	public void setChilds(List<Child> childs) {
		this.childs = childs;
	}
	
}
