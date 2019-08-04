package com.mogoo.parser;

import org.xml.sax.Attributes;

/**
 * 解析接口 
 * 用于各业务模块实现具体的结果解析
 * @ author 张永辉
 * @ date 2011-11-16
 */
public abstract class UnderlineResultCallback {
	/**
	 * 返回解析结果
	 */
	public Object getResult() {
		return null;
	}
	
	/**
	 * 处理返回数据格式为带下滑线的
	 */
	public Object handleUnderline(String [] params) {
		return null ;
	}
}
