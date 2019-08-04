package com.tcl.base.http;

import org.json.JSONObject;

/** 
 * @Description: JSON解析接口
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午5:42:23 
 * @copyright TCL-MIE
 */

public interface JsonParser {
	/**
	 * 解析JSON对象
	 * @param obj JSON对像
	 * @return 返回解析是否成功
	 */
	int parse(JSONObject obj);
}
