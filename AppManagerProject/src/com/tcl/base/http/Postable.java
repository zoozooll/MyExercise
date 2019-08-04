package com.tcl.base.http;

import java.util.Map;

/** 
 * @Description: 可上传数据接口
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午5:48:48 
 * @copyright TCL-MIE
 */

public interface Postable {
	/**
	 * 获取上传实体集合
	 * @return 返回需要上传的实体集合
	 */
	Map<String, byte[]> getPostEntities();
}
