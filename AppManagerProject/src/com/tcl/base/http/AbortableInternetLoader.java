package com.tcl.base.http;

/** 
 * @Description: 可中断的网络资源加载器
 * @author wenbiao.xie 
 * @date 2014年10月8日 下午5:26:16 
 * @copyright TCL-MIE
 */

public interface AbortableInternetLoader {
	/**
	 * 网络加载信息
	 * @return 返回执行结果
	 */
	boolean load();
	
	/**
	 * 取消网络加载过程
	 */
	void cancel();
}
