package com.tcl.base.http;

import org.apache.http.HttpEntity;

/** 
 * @Description: http响应通用解析器
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午5:44:28 
 * @copyright TCL-MIE
 */

public interface EntityParser {
	/**
	 * 解析http响应
	 * @param entity 响应数据
	 * @return 返回解析是否成功
	 */
	int parse(HttpEntity entity);
}
